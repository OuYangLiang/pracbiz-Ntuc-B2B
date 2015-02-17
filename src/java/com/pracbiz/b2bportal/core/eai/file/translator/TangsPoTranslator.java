//*****************************************************************************
//
// File Name       :  TangsPoTranslator.java
// Date Created    :  Apr 4, 2014
// Last Changed By :  $Author: eidt $
// Last Changed On :  $Date: Apr 4, 2014 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.translator;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.bytecode.opencsv.CSVReader;

import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.canonical.PoDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NumberValidator;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class TangsPoTranslator extends SourceTranslator
{
    private static final Logger log = LoggerFactory.getLogger(TangsPoTranslator.class);
    private static final String FILE_PREFIX_POL = "POL";
    private static final String ORDER_BASE_UNIT_U = "U";
    private static final String FILE_TYPE_POCN = "POCN";
    private static final String DOC_ACTION_ADD = "A";
    private static final String DOC_ACTION_CANCELLED = "D";
    
    @Autowired PoDocFileHandler conanicalDocFileHandler;
    @Autowired MailBoxUtil mboxUtil;
    
    @Override
    protected Map<String, List<String>> validateFile(Map<String, byte[]> files)
        throws Exception
    {
        if (files == null || files.isEmpty())
        {
            return null;
        }
        
        Map<String, List<String>> errorMessages = new HashMap<String, List<String>>();
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        for (Map.Entry<String, byte[]>  entry : files.entrySet())
        {
            CSVReader reader = null;
            List<String> contents = fileUtil.readLines(entry.getValue());
            for (int i = 1 ; i < contents.size() ; i++)
            {
                reader = new CSVReader(new StringReader(contents.get(i)), ',');
                String[] rowContents = reader.readNext();
                if (entry.getKey().startsWith(FILE_PREFIX_POL))
                {
                    validateDetailFile(rowContents, errorMessages, entry.getKey());
                }
                else
                {
                    validateHeaderFile(rowContents, errorMessages, entry.getKey());
                }
            }
        }
        
        //invalid the validation yet
        return null;
    }


    @Override
    protected Map<String, byte[]> translate(Map<String, byte[]> files,
        String outPath, BuyerHolder buyer, File sourceFile)
        throws Exception
    {
        if (files == null || files.isEmpty())
        {
            return null;
        }

        Map<String, byte[]> fileMap = new HashMap<String, byte[]>();
        Map<String, byte[]> pocnFileMap = new HashMap<String, byte[]>();
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        for (Map.Entry<String, byte[]>  entry : files.entrySet())
        {
            if (entry.getKey().startsWith(FILE_PREFIX_POL))
            {
                continue;
            }
            
            CSVReader reader = null;
            List<String> contents = fileUtil.readLines(entry.getValue());
            
            String subFilename = FILE_PREFIX_POL + entry.getKey().substring(entry.getKey().indexOf('_'), entry.getKey().lastIndexOf("_")) + ".csv";
            List<String> detailContents = fileUtil.readLines(files.get(subFilename));

            
            PoHeaderHolder header = null;
            PoLocationHolder location = null;
            List<PoDetailHolder>  details = null;
            List<PoLocationHolder> locations = null;
            List<PoHeaderExtendedHolder> hexList = null;
            List<PoDetailExtendedHolder> dexList = null;
            List<PoLocationDetailHolder> locationDetails = null;
            
            int locationSeq = 1;
            for (int i = 1 ; i < contents.size() ; i++)
            {
                reader = new CSVReader(new StringReader(contents.get(i)), ',');
                String[] rowContents = reader.readNext();
                
                locations = new ArrayList<PoLocationHolder>();
                
                header = initPoHeader(rowContents, buyer);
                String conanicalFilename = null;
                if (!header.getDocAction().equalsIgnoreCase(DOC_ACTION_ADD))
                {
                    conanicalFilename = MsgType.POCN + DOC_FILENAME_DELIMITOR
                        + header.getBuyerCode() + DOC_FILENAME_DELIMITOR
                        + header.getSupplierCode() + DOC_FILENAME_DELIMITOR
                        + StringUtil.getInstance().convertDocNo(header.getPoNo())+ ".txt";
                    
                    byte[] bytes = new byte[0];
                    pocnFileMap.put(conanicalFilename, bytes);
                }
                else
                {
                    hexList = initPoHex(rowContents, buyer);
                    location = initLocation(rowContents, locationSeq);
                    locations.add(location);
                    
                    if (files.containsKey(subFilename))
                    {
                       dexList = new ArrayList<PoDetailExtendedHolder>();
                       details = initPoDetailsAndDetailExtendeds(detailContents, dexList, buyer, header);
                       locationDetails = initLocationDetails(details, locations,  buyer);
                    }
                    
                    PoHolder po = new PoHolder();
                    po.setPoHeader(header);
                    po.setDetails(details);
                    po.setLocations(locations);
                    po.setLocationDetails(locationDetails);
                    po.setHeaderExtendeds(hexList);
                    po.setDetailExtendeds(dexList);
                    
                    conanicalFilename = MsgType.PO + DOC_FILENAME_DELIMITOR
                        + header.getBuyerCode() + DOC_FILENAME_DELIMITOR
                        + header.getSupplierCode() + DOC_FILENAME_DELIMITOR
                        + StringUtil.getInstance().convertDocNo(header.getPoNo())+ ".txt";
                    
                    byte[] bytes = conanicalDocFileHandler.getFileByte(po, null, DocFileHandler.CANONICAL);
                    
                    fileMap.put(conanicalFilename, bytes);
                }
            }
        }
        
        
        if (pocnFileMap != null && !pocnFileMap.isEmpty())
        {
            byte[] data = ("type:Portal\r\n" + sourceFile.getName()).getBytes(CommonConstants.ENCODING_UTF8);
            pocnFileMap.put(outPath + File.separator + "source_" + 
                DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".txt", data);
            
            this.writeTranslatedPocn(pocnFileMap, FILE_TYPE_POCN, buyer, outPath);
            
            String archivePath = mboxUtil.getFolderInBuyerArchOutPath(buyer.getMboxId(), DateUtil.getInstance().getCurrentYearAndMonth());
            FileUtil.getInstance().moveFile(sourceFile, archivePath);
        }
        
        return fileMap;
    }
    
    
    private PoHeaderHolder initPoHeader(String[] rowContents, BuyerHolder buyer)
    {
        if (rowContents == null || rowContents.length == 0)
        {
            return null;
        }
        
        PoHeaderHolder poHeader = new PoHeaderHolder();
        poHeader.setPoType(PoType.SOR);
        poHeader.setPoSubType("2");
        poHeader.setActionDate(new Date());
        poHeader.setPoDate(DateUtil.getInstance().convertStringToDate(rowContents[16].trim(), DateUtil.DEFAULT_DATE_FORMAT));
        poHeader.setDeliveryDateFrom(DateUtil.getInstance().convertStringToDate(rowContents[11].trim(), DateUtil.DEFAULT_DATE_FORMAT));
        poHeader.setDeliveryDateTo(DateUtil.getInstance().convertStringToDate(rowContents[11].trim(), DateUtil.DEFAULT_DATE_FORMAT));
        poHeader.setExpiryDate(DateUtil.getInstance().convertStringToDate(rowContents[12].trim(), DateUtil.DEFAULT_DATE_FORMAT));
        poHeader.setDocAction(rowContents[10].trim().equalsIgnoreCase("CANCELLED")?DOC_ACTION_CANCELLED : DOC_ACTION_ADD);
        poHeader.setPoNo(rowContents[0].trim());
        poHeader.setBuyerCode(buyer.getBuyerCode());
        poHeader.setBuyerName(buyer.getBuyerName());
        poHeader.setSupplierCode(rowContents[1].trim());
        poHeader.setSupplierName(rowContents[2].trim());
        poHeader.setSupplierAddr1(rowContents[3].trim());
        poHeader.setSupplierAddr2(rowContents[4].trim());
        poHeader.setSupplierAddr3(rowContents[5].trim());
        poHeader.setSupplierPostalCode(rowContents[6].trim());
        poHeader.setSupplierCity(rowContents[7].trim());
        poHeader.setSupplierCtryCode(rowContents[8].trim());
        poHeader.setDeptCode(rowContents[14].trim());
        poHeader.setShipToCode(rowContents[15].trim());
        poHeader.setCreditTermDesc(rowContents[20].trim());
        poHeader.setCreditTermCode(rowContents[21].trim());
        poHeader.setOrderRemarks(rowContents[23].trim());
        poHeader.setTotalCost(BigDecimalUtil.getInstance().convertStringToBigDecimal(rowContents[27].trim(), 4));
        poHeader.setAdditionalDiscountAmount(BigDecimalUtil.getInstance().convertStringToBigDecimal(rowContents[28].trim(), 4));
        poHeader.setNetCost(BigDecimalUtil.getInstance().convertStringToBigDecimal(rowContents[29].trim(), 4));
        poHeader.setShipToAddr1(rowContents[37].trim());
        poHeader.setShipToAddr2(rowContents[38].trim());

        return poHeader;
    }
    
    
    private List<PoHeaderExtendedHolder> initPoHex(String[] rowContents, BuyerHolder buyer) throws Exception
    {
        if (rowContents == null || rowContents.length == 0)
        {
            return null;
        }
        List<PoHeaderExtendedHolder> hexList = new ArrayList<PoHeaderExtendedHolder>();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        PoHeaderExtendedHolder faxNo = this.initHeaderExtended("fax_no", EXTENDED_TYPE_STRING, null, rowContents[9].trim(), null, null, null);
        PoHeaderExtendedHolder agent = this.initHeaderExtended("agent", EXTENDED_TYPE_STRING, null, rowContents[13].trim(), null, null, null);
        PoHeaderExtendedHolder returnable = this.initHeaderExtended("returnable", EXTENDED_TYPE_STRING, null, rowContents[17].trim(), null, null, null);
        PoHeaderExtendedHolder currencyCode = this.initHeaderExtended("currency_code", EXTENDED_TYPE_STRING, null, rowContents[18].trim(), null, null, null);
        PoHeaderExtendedHolder exchangeRate = this.initHeaderExtended("exchange_rate", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[19].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder incoterm = this.initHeaderExtended("incoterm", EXTENDED_TYPE_STRING, null, rowContents[22].trim(), null, null, null);
        PoHeaderExtendedHolder discount1 = this.initHeaderExtended("discount1", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[24].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder discount2 = this.initHeaderExtended("discount2", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[25].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder discount3 = this.initHeaderExtended("discount3", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[26].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder gst = this.initHeaderExtended("gst", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[30].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder payable = this.initHeaderExtended("payable", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[31].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder fidos = this.initHeaderExtended("fidos", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[32].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder others = this.initHeaderExtended("others", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[33].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder costOfGoods = this.initHeaderExtended("cost_of_goods", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[34].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder retailWoGst = this.initHeaderExtended("retail_wo_gst", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[35].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder retailWGst = this.initHeaderExtended("retail_w_gst", EXTENDED_TYPE_FLOAT, decimalUtil.convertStringToBigDecimal(rowContents[36].trim(), 4), null, null, null, null);
        PoHeaderExtendedHolder poStatus = this.initHeaderExtended("po_status", EXTENDED_TYPE_STRING, null, rowContents[10].trim(), null, null, null);
        
        hexList.add(faxNo);
        hexList.add(agent);
        hexList.add(returnable);
        hexList.add(currencyCode);
        hexList.add(exchangeRate);
        hexList.add(incoterm);
        hexList.add(discount1);
        hexList.add(discount2);
        hexList.add(discount3);
        hexList.add(gst);
        hexList.add(payable);
        hexList.add(fidos);
        hexList.add(others);
        hexList.add(costOfGoods);
        hexList.add(retailWoGst);
        hexList.add(retailWGst);
        hexList.add(poStatus);
        
        return hexList;
    }
    
    
    private List<PoDetailHolder> initPoDetailsAndDetailExtendeds(List<String> detailContents, List<PoDetailExtendedHolder> dexList,
        BuyerHolder buyer, PoHeaderHolder header) 
    {
        if(detailContents == null || detailContents.isEmpty())
        {
            return null;
        }

        List<PoDetailHolder> poDetails = new ArrayList<PoDetailHolder>();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        int lineSeq = 1;
        for(int i = 1; i < detailContents.size(); i++)
        {
            CSVReader reader = null;
            try
            {
                reader = new CSVReader(new StringReader(
                    detailContents.get(i)), ',');
                
                String[] rowContents = reader.readNext();
                if (!rowContents[0].trim().equalsIgnoreCase(header.getPoNo()))
                {
                    continue;
                }
                
                PoDetailHolder poDetail = new PoDetailHolder();
                poDetail.setLineSeqNo(lineSeq);
                poDetail.setBuyerItemCode(rowContents[2].trim());
                poDetail.setSupplierItemCode(rowContents[3].trim());
                poDetail.setItemDesc(rowContents[4].trim());
                poDetail.setOrderQty(decimalUtil.convertStringToBigDecimal(rowContents[5].trim(), 4));
                poDetail.setUnitCost(decimalUtil.convertStringToBigDecimal(rowContents[6].trim(), 4));
                poDetail.setItemCost(decimalUtil.convertStringToBigDecimal(rowContents[7].trim(), 4));
                poDetail.setItemRetailAmount(decimalUtil.convertStringToBigDecimal(rowContents[9].trim(), 4));
                poDetail.setOrderUom(rowContents[10].trim());
                poDetail.setNetPackCost(BigDecimal.ZERO);
                poDetail.setNetUnitCost(BigDecimal.ZERO);
                poDetail.setPackCost(BigDecimal.ZERO);
                poDetail.setCostDiscountAmount(BigDecimal.ZERO);
                poDetail.setOrderBaseUnit(ORDER_BASE_UNIT_U);
                poDetail.setPackingFactor(BigDecimal.ONE);
                poDetail.setItemRemarks(rowContents[11].trim());
               
                PoDetailExtendedHolder uop =  initDetailExtended("uop", EXTENDED_TYPE_STRING, null, rowContents[8].trim(), null, null, null);
                uop.setLineSeqNo(lineSeq);
                PoDetailExtendedHolder vendorArticle2 =  initDetailExtended("vendorArticle2", EXTENDED_TYPE_STRING, null, rowContents[12].trim(), null, null, null);
                vendorArticle2.setLineSeqNo(lineSeq);
                
                lineSeq ++ ;
                poDetails.add(poDetail);
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
            catch(Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
            finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch(IOException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
            }
        }
        return poDetails;
    }
    
    
    private PoLocationHolder initLocation(String[] rowContents, int locationSeq)
    {
        PoLocationHolder location = new PoLocationHolder();
        location.setLocationCode(rowContents[15].trim());
        location.setLineSeqNo(locationSeq);
        
        return location;
    }
    
    private List<PoLocationDetailHolder> initLocationDetails(List<PoDetailHolder> details, List<PoLocationHolder> locations, BuyerHolder buyer)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        List<PoLocationDetailHolder> poLocationDetails = new ArrayList<PoLocationDetailHolder>();
        
        for (PoDetailHolder detail : details)
        {
            PoLocationDetailHolder locationDetail = new PoLocationDetailHolder();
            locationDetail.setDetailLineSeqNo(detail.getLineSeqNo());
            locationDetail.setLocationLineSeqNo(locations.get(0).getLineSeqNo());
            locationDetail.setLocationShipQty(detail.getOrderQty());
            poLocationDetails.add(locationDetail);
        }
        
        return poLocationDetails;
    }
    
    
    private PoHeaderExtendedHolder initHeaderExtended(String fieldName,
           String fieldType, BigDecimal floatValue, String stringValue,
           Date dateValue, Integer intValue, Boolean boolValue)
    {
        PoHeaderExtendedHolder extended = new PoHeaderExtendedHolder();
        extended.setFieldName(fieldName);
        extended.setFieldType(fieldType);
        if (fieldType.equalsIgnoreCase("S"))
        {
            extended.setStringValue(stringValue);
        }
        else if (fieldType.equalsIgnoreCase("B"))
        {
            extended.setBoolValue(boolValue);
        }
        else if (fieldType.equalsIgnoreCase("D"))
        {
            extended.setDateValue(dateValue);;
        }
        else if (fieldType.equalsIgnoreCase("F"))
        {
            extended.setFloatValue(floatValue);;
        }
        else if (fieldType.equalsIgnoreCase("I"))
        {
            extended.setIntValue(intValue);
        }
        return extended;
    }
    
    
    private PoDetailExtendedHolder initDetailExtended(String fieldName,
        String fieldType, BigDecimal floatValue, String stringValue,
        Date dateValue, Integer intValue, Boolean boolValue)
    {
        PoDetailExtendedHolder extended = new PoDetailExtendedHolder();
        extended.setFieldName(fieldName);
        extended.setFieldType(fieldType);
        if(fieldType.equalsIgnoreCase("S"))
        {
            extended.setStringValue(stringValue);
        }
        else if(fieldType.equalsIgnoreCase("B"))
        {
            extended.setBoolValue(boolValue);
        }
        else if(fieldType.equalsIgnoreCase("D"))
        {
            extended.setDateValue(dateValue);
            ;
        }
        else if(fieldType.equalsIgnoreCase("F"))
        {
            extended.setFloatValue(floatValue);
            ;
        }
        else if(fieldType.equalsIgnoreCase("I"))
        {
            extended.setIntValue(intValue);
        }
        return extended;
    }
    
    
    private void validateHeaderFile(String[] rowContents, Map<String, List<String>> errorMessages, String headerFilename)
    {
        if (rowContents == null || rowContents.length == 0)
        {
            return ;
        }
        String error = null;
        List<String> errors = new ArrayList<String>();
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        boolean validatePoDate = new DateValidator(DateUtil.DEFAULT_DATE_FORMAT).isValid(rowContents[16].trim(), DateUtil.DEFAULT_DATE_FORMAT, false);
        if (!validatePoDate)
        {
            error =  "[CREATED_DATE]" + " is invalid, the correct format is '"+ DateUtil.DEFAULT_DATE_FORMAT +"'";
            errors.add(error);
        }
        
        boolean validateDeliveryDate = new DateValidator(DateUtil.DEFAULT_DATE_FORMAT).isValid(rowContents[11].trim(), DateUtil.DEFAULT_DATE_FORMAT, false);
        if (!validateDeliveryDate)
        {
            error =  "[PO_NOT_BEFORE_DATE]" + " is invalid, the correct format is '"+ DateUtil.DEFAULT_DATE_FORMAT +"'";
            errors.add(error);
        }
        
        boolean validateExpiryDate = new DateValidator(DateUtil.DEFAULT_DATE_FORMAT).isValid(rowContents[12].trim(), DateUtil.DEFAULT_DATE_FORMAT, false);
        if (!validateExpiryDate)
        {
            error =  "[PO_NOT_BEFORE_DATE]" + " is invalid, the correct format is '"+ DateUtil.DEFAULT_DATE_FORMAT +"'";
            errors.add(error);
        }
        
        FieldContentValidator validateTotalCost = null;
        validateTotalCost = new NumberValidator();
        fileParseUtil.addError(errors, validateTotalCost.validate(rowContents[27].trim(), "[SUB_TOTAL]"));
        
        FieldContentValidator validateAdditionalDiscountAmount = null;
        validateAdditionalDiscountAmount = new NumberValidator();
        fileParseUtil.addError(errors, validateAdditionalDiscountAmount.validate(rowContents[28].trim(), "[DISCOUNT]"));
        
        FieldContentValidator validateNetCost = null;
        validateNetCost = new NumberValidator();
        fileParseUtil.addError(errors, validateNetCost.validate(rowContents[29].trim(), "[NET_AMT]"));
        
        FieldContentValidator validateExchangeRate = null;
        validateExchangeRate = new NumberValidator();
        fileParseUtil.addError(errors, validateExchangeRate.validate(rowContents[19].trim(), "[EXCHANGE_RATE]"));
        
        FieldContentValidator validateDiscount1 = null;
        validateDiscount1 = new NumberValidator();
        fileParseUtil.addError(errors, validateDiscount1.validate(rowContents[24].trim(), "[DISCOUNT1]"));
        
        FieldContentValidator validateDiscount2 = null;
        validateDiscount2 = new NumberValidator();
        fileParseUtil.addError(errors, validateDiscount2.validate(rowContents[25].trim(), "[DISCOUNT2]"));
        
        FieldContentValidator validateDiscount3 = null;
        validateDiscount3 = new NumberValidator();
        fileParseUtil.addError(errors, validateDiscount3.validate(rowContents[26].trim(), "[DISCOUNT3]"));
        
        FieldContentValidator validateGst = null;
        validateGst = new NumberValidator();
        fileParseUtil.addError(errors, validateGst.validate(rowContents[30].trim(), "[GST]"));
        
        FieldContentValidator validatePayable = null;
        validatePayable = new NumberValidator();
        fileParseUtil.addError(errors, validatePayable.validate(rowContents[31].trim(), "[PAYABLE]"));
        
        FieldContentValidator validateFidos = null;
        validateFidos = new NumberValidator();
        fileParseUtil.addError(errors, validateFidos.validate(rowContents[31].trim(), "[FIDOS]"));
        
        FieldContentValidator validateOthers = null;
        validateOthers = new NumberValidator();
        fileParseUtil.addError(errors, validateOthers.validate(rowContents[32].trim(), "[OTHERS]"));
        
        FieldContentValidator validateCostOfGoods = null;
        validateCostOfGoods = new NumberValidator();
        fileParseUtil.addError(errors, validateCostOfGoods.validate(rowContents[34].trim(), "[COST_OF_GOODS]"));
        
        FieldContentValidator validateRetailWoGst = null;
        validateRetailWoGst = new NumberValidator();
        fileParseUtil.addError(errors, validateRetailWoGst.validate(rowContents[35].trim(), "[RETAIL_WO_GST]"));
        
        FieldContentValidator validateRetailWGst = null;
        validateRetailWGst = new NumberValidator();
        fileParseUtil.addError(errors, validateRetailWGst.validate(rowContents[36].trim(), "[RETAIL_W_GST]"));
        
        if (!errors.isEmpty())
        {
            errorMessages.put(headerFilename, errors);
        }
    }
    
    
    private void validateDetailFile(String[] rowContents, Map<String, List<String>> errorMessages, String headerFilename)
    {
        if (rowContents == null || rowContents.length == 0)
        {
            return ;
        }
        
        List<String> errors = new ArrayList<String>();
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        FieldContentValidator validateOrderQty = null;
        validateOrderQty = new NumberValidator();
        fileParseUtil.addError(errors, validateOrderQty.validate(rowContents[5].trim(), "[QTY]"));
        
        FieldContentValidator validateUnitCost = null;
        validateUnitCost = new NumberValidator();
        fileParseUtil.addError(errors, validateUnitCost.validate(rowContents[6].trim(), "[UNIT_PRICE]"));
        
        FieldContentValidator validateItemCost = null;
        validateItemCost = new NumberValidator();
        fileParseUtil.addError(errors, validateItemCost.validate(rowContents[7].trim(), "[TOTAL_COST]"));
        
        FieldContentValidator validateItemRetailAmount = null;
        validateItemRetailAmount = new NumberValidator();
        fileParseUtil.addError(errors, validateItemRetailAmount.validate(rowContents[9].trim(), "[RETAIL_AMT]"));
        
        if (!errors.isEmpty())
        {
            errorMessages.put(headerFilename, errors);
        }
    }
    
    
    private void writeTranslatedPocn(Map<String, byte[]> filesMap, String fileType, BuyerHolder buyer, String targetPath) throws Exception
    {
        List<File> files = new ArrayList<File>();
        for (Map.Entry<String, byte[]> entry : filesMap.entrySet())
        {
            File file = new File(entry.getKey());
            FileUtil.getInstance().writeByteToDisk(entry.getValue(), entry.getKey());
            files.add(file);
        }
        
        GZIPHelper.getInstance().doStandardZip(files, fileType, buyer.getBuyerCode(), targetPath);
    }
}
