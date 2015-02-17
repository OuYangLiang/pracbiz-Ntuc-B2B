//*****************************************************************************
//
// File Name       :  ConcessDocFileConvert.java
// Date Created    :  Feb 18, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Feb 18, 2014 3:33:38 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.convert;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import au.com.bytecode.opencsv.CSVReader;


import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.file.canonical.PoDocFileHandler;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class ConPoDocFileConvert implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(ConPoDocFileConvert.class);
    
//    @Autowired PoDocFileHandler canonicalPoDocFileHandler;
    private static final String DETAIL_EX_FIELD_TYPE_STRING = "S";
    private static final String DETAIL_EX_FIELD_TYPE_FLOAT = "F";
    private static final String DETAIL_EX_FIELD_TYPE_DATE = "D";
    private static final String DOC_ACTION_A = "A";
    
    
    public List<File> createFile(File filePath, Map<String, PoHolder> map, BuyerHolder buyer, PoDocFileHandler canonicalPoDocFileHandler) throws Exception
    {
        String file = null;
        List<File> files = new ArrayList<File>();
        if (map != null && !map.isEmpty())
        {
            for (Map.Entry<String, PoHolder> entry : map.entrySet())
            {
                file = filePath
                    + File.separator
                    + "PO_"
                    + buyer.getBuyerCode()
                    + "_"
                    + entry.getValue().getPoHeader().getSupplierCode()
                    + "_"
                    + entry.getValue().getPoHeader().getPoNo() + ".txt";
                
                File targetFile = new File(file);
                canonicalPoDocFileHandler.createFile(entry.getValue(), targetFile, PoDocFileHandler.CANONICAL);
                files.add(targetFile);
            }
        }
        
        return files;
    }

    
    public Map<String, PoHolder> readFile(File file, BuyerHolder buyer) throws Exception
    {
        Map<String, List<String>> concessMap = null;
        String[] contents = null;
        Map<String, PoHolder> map = null;
        if (file.getName().endsWith(".csv"))
        {
            contents = FileParserUtil.getInstance().readLines(file);
        }
        else
        {
            return null;
        }

        concessMap = new HashMap<String, List<String>>();
        //skip first line.
        for (int i = 1 ; i < contents.length; i++)
        {
            CSVReader reader = new CSVReader(new StringReader(contents[i]), ',', '\"');
            String[] lineContent = null;
            try
            {
                lineContent = reader.readNext();
            }
            finally
            {
                reader.close();
                reader = null;
            }
            String supplierCode = lineContent[0];
            if (concessMap.containsKey(supplierCode))
            {
                concessMap.get(supplierCode).add(contents[i]);
            }
            else
            {
                List<String> groupContents = new ArrayList<String>();
                groupContents.add(contents[i]);
                concessMap.put(supplierCode, groupContents);
            }
        }
        
        
        if (concessMap != null && !concessMap.isEmpty())
        {
            map = new HashMap<String, PoHolder>();
            
            for (Map.Entry<String, List<String>> entry : concessMap.entrySet())
            {
                PoHolder po = initPoHolder(entry.getValue(), buyer);
                
                if (po != null)
                {
                    map.put(entry.getKey(), po);
                }
            }
        }
        
        return map;
    }
    
    
    private PoHolder initPoHolder(List<String> contents, BuyerHolder buyer)throws Exception
    {
        if (contents == null || contents.isEmpty())
        {
            return null;
        }
        
        PoHeaderHolder poHeader = null;
        List<PoDetailHolder> poDetails = new ArrayList<PoDetailHolder>();
        List<PoDetailExtendedHolder> detailExtendeds = new ArrayList<PoDetailExtendedHolder>();
        List<PoLocationHolder> locations = new ArrayList<PoLocationHolder>();
        List<PoLocationDetailHolder> locationDetails = new ArrayList<PoLocationDetailHolder>();
        Map<String, PoLocationHolder> locationMap = new HashMap<String, PoLocationHolder>();
        
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        NumberFormat numberFormat  = NumberFormat.getNumberInstance();
        
        int lineSeqNo = 1;
        int locLineSeqNo = 1;
        for (int i = 0; i < contents.size(); i++)
        {
            CSVReader reader = new CSVReader(new StringReader(contents.get(i)), ',', '\"');
            String[] lineContent = null;
            try
            {
                lineContent = reader.readNext();
            }
            catch(Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
            finally
            {
                try
                {
                    reader.close();
                    reader = null;
                }
                catch(IOException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
            
            //skip the wrong rows.
            if (lineContent.length != 20)
            {
                continue;
            }
            
            if (poHeader == null)
            {
                poHeader = new PoHeaderHolder();
                poHeader.setPoNo(lineContent[0].trim()+ DateUtil.getInstance().convertDateToString(new Date(), DateUtil.DATE_FORMAT_YYYYMMDDHH));
                poHeader.setDocAction(DOC_ACTION_A);
                poHeader.setActionDate(new Date());
                poHeader.setPoType(PoType.CON);
                poHeader.setPoDate(new Date());
                poHeader.setBuyerCode(buyer.getBuyerCode());
                poHeader.setSupplierCode(lineContent[0].trim());
                poHeader.setTotalCost(BigDecimal.ZERO);
                poHeader.setAdditionalDiscountAmount(BigDecimal.ZERO);
                poHeader.setNetCost(BigDecimal.ZERO);
                poHeader.setGrossProfitMargin(BigDecimal.ZERO);
                poHeader.setTotalSharedCost(BigDecimal.ZERO);
                poHeader.setTotalGrossCost(decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[13].trim()).toString(), 4));
                poHeader.setPeriodStartDate(DateUtil.getInstance()
                        .convertStringToDate(lineContent[7].trim(),DateUtil.DATE_FORMAT_MMDDYYYY));
                poHeader.setPeriodEndDate(DateUtil.getInstance()
                        .convertStringToDate(lineContent[8].trim(),DateUtil.DATE_FORMAT_MMDDYYYY));
            }
            else
            {
                BigDecimal detailItemGrossCost = decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[13].trim()).toString(), 4);
                poHeader.setTotalGrossCost(poHeader.getTotalGrossCost()
                        .add(detailItemGrossCost));
                
                //period start date and end date
                if (poHeader.getPeriodStartDate().after(DateUtil.getInstance()
                        .convertStringToDate(lineContent[7].trim(),DateUtil.DATE_FORMAT_MMDDYYYY)))
                {
                    poHeader.setPeriodStartDate(DateUtil.getInstance()
                        .convertStringToDate(lineContent[7].trim(),DateUtil.DATE_FORMAT_MMDDYYYY));
                }
                
                if (poHeader.getPeriodEndDate().before(DateUtil.getInstance()
                        .convertStringToDate(lineContent[8].trim(),DateUtil.DATE_FORMAT_MMDDYYYY)))
                {
                    poHeader.setPeriodEndDate(DateUtil.getInstance()
                        .convertStringToDate(lineContent[8].trim(),DateUtil.DATE_FORMAT_MMDDYYYY));
                }
            }
           
            PoDetailHolder poDetail = new PoDetailHolder();

            poDetail.setLineSeqNo(lineSeqNo);
            poDetail.setBuyerItemCode(lineContent[5].trim());
            poDetail.setItemDesc(lineContent[6].trim());
            poDetail.setPackingFactor(BigDecimal.ONE);
            poDetail.setOrderBaseUnit("U");
            poDetail.setOrderUom("UNIT");
            poDetail.setOrderQty(BigDecimal.ZERO);
            poDetail.setUnitCost(BigDecimal.ZERO);
            poDetail.setPackCost(BigDecimal.ZERO);
            poDetail.setCostDiscountAmount(BigDecimal.ZERO);
            poDetail.setNetUnitCost(BigDecimal.ZERO);
            poDetail.setNetPackCost(BigDecimal.ZERO);
            poDetail.setItemCost(BigDecimal.ZERO);
            poDetail.setItemSharedCost(BigDecimal.ZERO);
            poDetail.setItemGrossCost(decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[13].trim()).toString(), 4));
            poDetail.setRetailPrice(BigDecimal.ZERO);
            poDetail.setRetailDiscountAmount(decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[14].trim()).toString(), 4));
            poDetail.setItemRetailAmount(BigDecimal.ZERO);
            
            //dept
            PoDetailExtendedHolder detailExDept = new PoDetailExtendedHolder();
            detailExDept.setFieldName("deptCode");
            detailExDept.setFieldType(DETAIL_EX_FIELD_TYPE_STRING);
            detailExDept.setLineSeqNo(lineSeqNo);
            detailExDept.setStringValue(lineContent[2].trim());
            detailExtendeds.add(detailExDept);
            
            //comp
            PoDetailExtendedHolder detailExComp = new PoDetailExtendedHolder();
            detailExComp.setFieldName("comp");
            detailExComp.setFieldType(DETAIL_EX_FIELD_TYPE_STRING);
            detailExComp.setLineSeqNo(lineSeqNo);
            detailExComp.setStringValue(lineContent[3].trim());
            detailExtendeds.add(detailExComp);
            
            //sales period from
            PoDetailExtendedHolder detailExPeriodStart = new PoDetailExtendedHolder();
            detailExPeriodStart.setFieldName("periodStartDate");
            detailExPeriodStart.setFieldType(DETAIL_EX_FIELD_TYPE_DATE);
            detailExPeriodStart.setLineSeqNo(lineSeqNo);
            detailExPeriodStart.setDateValue(DateUtil.getInstance().convertStringToDate(lineContent[7].trim(), DateUtil.DATE_FORMAT_MMDDYYYY));
            detailExtendeds.add(detailExPeriodStart);
            
            //sales period to
            PoDetailExtendedHolder detailExPeriodEnd = new PoDetailExtendedHolder();
            detailExPeriodEnd.setFieldName("periodEndDate");
            detailExPeriodEnd.setFieldType(DETAIL_EX_FIELD_TYPE_DATE);
            detailExPeriodEnd.setLineSeqNo(lineSeqNo);
            detailExPeriodEnd.setDateValue(DateUtil.getInstance().convertStringToDate(lineContent[8].trim(), DateUtil.DATE_FORMAT_MMDDYYYY));
            detailExtendeds.add(detailExPeriodEnd);
            
            //rebate basis
            PoDetailExtendedHolder detailExBasis = new PoDetailExtendedHolder();
            detailExBasis.setFieldName("rebateBasis");
            detailExBasis.setFieldType(DETAIL_EX_FIELD_TYPE_STRING);
            detailExBasis.setLineSeqNo(lineSeqNo);
            detailExBasis.setStringValue(lineContent[9].trim());
            detailExtendeds.add(detailExBasis);
            
            //rebate basis desc
            PoDetailExtendedHolder detailExBasisDesc = new PoDetailExtendedHolder();
            detailExBasisDesc.setFieldName("rebateBasisDesc");
            detailExBasisDesc.setFieldType(DETAIL_EX_FIELD_TYPE_STRING);
            detailExBasisDesc.setLineSeqNo(lineSeqNo);
            detailExBasisDesc.setStringValue(lineContent[10].trim());
            detailExtendeds.add(detailExBasisDesc);
            
            //comm tangs
            PoDetailExtendedHolder detailExCommTangs = new PoDetailExtendedHolder();
            detailExCommTangs.setFieldName("CommTangs");
            detailExCommTangs.setFieldType(DETAIL_EX_FIELD_TYPE_FLOAT);
            detailExCommTangs.setLineSeqNo(lineSeqNo);
            detailExCommTangs.setFloatValue(decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[11].trim()).toString(), 4));
            detailExtendeds.add(detailExCommTangs);
            
            //disc tangs
            PoDetailExtendedHolder detailExDiscTangs = new PoDetailExtendedHolder();
            detailExDiscTangs.setFieldName("DiscTangs");
            detailExDiscTangs.setFieldType(DETAIL_EX_FIELD_TYPE_FLOAT);
            detailExDiscTangs.setLineSeqNo(lineSeqNo);
            detailExDiscTangs.setFloatValue(decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[12].trim()).toString(), 4));
            detailExtendeds.add(detailExDiscTangs);
            
            //net amount
            PoDetailExtendedHolder detailExNetAmount = new PoDetailExtendedHolder();
            detailExNetAmount.setFieldName("netAmount");
            detailExNetAmount.setFieldType(DETAIL_EX_FIELD_TYPE_FLOAT);
            detailExNetAmount.setLineSeqNo(lineSeqNo);
            detailExNetAmount.setFloatValue(decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[15]).toString(), 4));
            detailExtendeds.add(detailExNetAmount);
            
            //payment wo gst
            PoDetailExtendedHolder detailExPayWoGst = new PoDetailExtendedHolder();
            detailExPayWoGst.setFieldName("PaymentWoGST");
            detailExPayWoGst.setFieldType(DETAIL_EX_FIELD_TYPE_FLOAT);
            detailExPayWoGst.setLineSeqNo(lineSeqNo);
            detailExPayWoGst.setFloatValue(decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[16].trim()).toString(), 4));
            detailExtendeds.add(detailExPayWoGst);
            
            //gp 
            PoDetailExtendedHolder detailExGp = new PoDetailExtendedHolder();
            detailExGp.setFieldName("GP");
            detailExGp.setFieldType(DETAIL_EX_FIELD_TYPE_FLOAT);
            detailExGp.setLineSeqNo(lineSeqNo);
            detailExGp.setFloatValue(decimalUtil.convertStringToBigDecimal(numberFormat.parse(lineContent[17].trim()).toString(), 4));
            detailExtendeds.add(detailExGp);
            
            //RegularOrPromotion
            PoDetailExtendedHolder detailExRegOrPro = new PoDetailExtendedHolder();
            detailExRegOrPro.setFieldName("RegularOrPromotion");
            detailExRegOrPro.setFieldType(DETAIL_EX_FIELD_TYPE_STRING);
            detailExRegOrPro.setLineSeqNo(lineSeqNo);
            detailExRegOrPro.setStringValue(lineContent[18].trim());
            detailExtendeds.add(detailExRegOrPro);
            
            //warning code
            PoDetailExtendedHolder detailWarningCode = new PoDetailExtendedHolder();
            detailWarningCode.setFieldName("WarningCode");
            detailWarningCode.setFieldType(DETAIL_EX_FIELD_TYPE_STRING);
            detailWarningCode.setLineSeqNo(lineSeqNo);
            detailWarningCode.setStringValue(lineContent[19].trim());
            detailExtendeds.add(detailWarningCode);
            
            //init location detail
            PoLocationDetailHolder poLocDetail = new PoLocationDetailHolder();
            poLocDetail.setDetailLineSeqNo(lineSeqNo);
            poLocDetail.setLocationShipQty(BigDecimal.ZERO);
            poLocDetail.setLocationFocQty(BigDecimal.ZERO);
            poLocDetail.setAllEmptyStringToNull();
            
            //init location
            if (!locationMap.containsKey(lineContent[1].trim().toUpperCase(Locale.US)))
            {
                PoLocationHolder poLocation = new PoLocationHolder();
                poLocation.setLineSeqNo(locLineSeqNo);
                poLocation.setLocationCode(lineContent[1].trim());
                poLocation.setLocationName(lineContent[4].trim());
                poLocation.setAllEmptyStringToNull();
                locationMap.put(lineContent[1].trim().toUpperCase(Locale.US), poLocation);
                
                //loc detail line seq no.
                poLocDetail.setLocationLineSeqNo(locLineSeqNo);
                locations.add(poLocation);
                
                locLineSeqNo ++ ;
            }
            else
            {
                poLocDetail.setLocationLineSeqNo(locationMap.get(lineContent[1].trim().toUpperCase(Locale.US)).getLineSeqNo());
            }
            
            
            poDetail.setAllEmptyStringToNull();
            poDetails.add(poDetail);
            locationDetails.add(poLocDetail);
            
            lineSeqNo ++ ;
        }
        
        if (locations.size() > 1)
        {
            poHeader.setPoSubType("1");
        }
        else if (locations.size() == 1)
        {
            poHeader.setPoSubType("2");
            poHeader.setShipToCode(locations.get(0).getLocationCode());
            poHeader.setShipToName(locations.get(0).getLocationName());
        }
        
        PoHolder po = new PoHolder();
        po.setPoHeader(poHeader);
        po.setDetails(poDetails);
        po.setDetailExtendeds(detailExtendeds);
        po.setLocations(locations);
        po.setLocationDetails(locationDetails);
        
        return po;
    }
    
//    public static void main(String[] args) throws Exception
//    {
//        ConPoDocFileConvert convert = new ConPoDocFileConvert();
//        File readFile = new File(
//            "C:/Documents and Settings/liyong/桌面/SRC_CON_CKT_20130221095300.csv");
//        BuyerHolder buyer = new BuyerHolder();
//        buyer.setBuyerCode("ABC");
//        Map<String, PoHolder> map = convert.readFile(readFile, buyer);
//        BigDecimalUtil u = BigDecimalUtil.getInstance();
//        NumberFormat numberFormat = NumberFormat.getNumberInstance();
//        System.out.println(u.convertStringToBigDecimal(
//            numberFormat.parse("").toString(), 4));
//    }
    
}
