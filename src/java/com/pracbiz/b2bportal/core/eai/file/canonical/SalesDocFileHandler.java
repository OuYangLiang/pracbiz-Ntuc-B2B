//*****************************************************************************
//
// File Name       :  PoFileHandler.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 4:45:12 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.canonical;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.SalesDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SalesHolder;
import com.pracbiz.b2bportal.core.holder.SalesLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author YinChi
 */
public class SalesDocFileHandler extends DefaultDocFileHandler<SalesDocMsg, SalesHolder> implements
    CoreCommonConstants
{
    private final static Logger log = LoggerFactory.getLogger(SalesDocFileHandler.class);
    @Autowired private transient BuyerStoreService buyerStoreService;
    
    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }

    
    @Override
    public void readFileContent(SalesDocMsg docMsg, byte[] input)
        throws Exception
    {
        log.info(" :::: start to read conanical Sales source file");
        // read source file and group according by record type
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        
        BigDecimal salesOid = docMsg.getDocOid();
        SalesHolder salesHolder = new SalesHolder();
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        SalesHeaderHolder header = this.initSalesHeader(headerContents,docMsg);
        salesHolder.setSalesHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList= map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<SalesHeaderExtendedHolder> headerExtendeds = new ArrayList<SalesHeaderExtendedHolder>();
            for (String[] headerExContent : headerExList)
            {
                SalesHeaderExtendedHolder salesExHolder = this.initSalesHeaderExtended(headerExContent);
                salesExHolder.setSalesOid(salesOid);
                headerExtendeds.add(salesExHolder);
            }
            salesHolder.setHeaderExtendeds(headerExtendeds);
        }
        
        //record type is 'DET'
        List<String[]> detailList= map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        List<SalesDetailHolder> details = new ArrayList<SalesDetailHolder>();
        List<SalesDateHolder> salesDates = new ArrayList<SalesDateHolder>();
        Map<String, SalesDetailHolder> detailMap = new HashMap<String, SalesDetailHolder>();
        Map<Date, SalesDateHolder> salesDateMap = new HashMap<Date, SalesDateHolder>();
        int salesDateLineSeq = 1;
        int detailLineSeq = 1;
        for (String[] detailContent: detailList)
        {
            SalesDetailHolder detail = this.initSalesDetail(detailContent);
            //init sales date holder
            SalesDateHolder salesDate = this.initSalesDate(detail);
            //group sales date by business date(sales date)
            if (!salesDateMap.containsKey(salesDate.getSalesDate()))
            {
                salesDate.setLineSeqNo(salesDateLineSeq);
                salesDate.setSalesOid(salesOid);
                salesDateMap.put(salesDate.getSalesDate(), salesDate);
                salesDateLineSeq++;
            }
            //group detail by buyer item code and barcode
            String key = detail.getBuyerItemCode() + "@@" + detail.getBarcode();
            if (detailMap.containsKey(key))
            {
                SalesDetailHolder holder = detailMap.get(key);
                if (null != detail.getSalesQty())
                {
                    holder.setSalesQty((holder.getSalesQty() == null ? BigDecimal.ZERO : holder.getSalesQty()).add(detail.getSalesQty()));
                }
                if (null != detail.getItemCost())
                {
                    holder.setItemCost((holder.getItemCost() == null ? BigDecimal.ZERO : holder.getItemCost()).add(detail.getItemCost()));
                }
                if (null != detail.getItemSalesAmount())
                {
                    holder.setItemSalesAmount((holder.getItemSalesAmount() == null ? BigDecimal.ZERO : holder.getItemSalesAmount()).add(detail.getItemSalesAmount()));
                }
                if (null != detail.getSalesDiscountAmount())
                {
                    holder.setSalesDiscountAmount((holder.getSalesDiscountAmount() == null ? BigDecimal.ZERO : holder.getSalesDiscountAmount()).add(detail.getSalesDiscountAmount()));
                }
                if (null != detail.getVatAmount())
                {
                    holder.setVatAmount((holder.getVatAmount() == null ? BigDecimal.ZERO : holder.getVatAmount()).add(detail.getVatAmount()));
                }
                if (null != detail.getItemNetSalesAmount())
                {
                    holder.setItemNetSalesAmount((holder.getItemNetSalesAmount() == null ? BigDecimal.ZERO : holder.getItemNetSalesAmount()).add(detail.getItemNetSalesAmount()));
                }
            }
            else
            {
                detail.setSalesOid(salesOid);
                detail.setLineSeqNo(detailLineSeq);
                detailMap.put(key, detail);
                detailLineSeq++;
            }
        }
        for (Map.Entry<String, SalesDetailHolder> entry : detailMap.entrySet())
        {
            details.add(entry.getValue());
        }
        for (Map.Entry<Date, SalesDateHolder> entry : salesDateMap.entrySet())
        {
            salesDates.add(entry.getValue());
        }
        salesHolder.setSalesDates(salesDates);
        salesHolder.setDetails(details);
        
        //record type is 'DEX'
//        List<String[]> detailExList= map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
//        
//        if (detailExList != null && !detailExList.isEmpty())
//        {
//            List<SalesDetailExtendedHolder> detailExtendeds = new ArrayList<SalesDetailExtendedHolder>();
//            for (String[] detailExContent : detailExList)
//            {
//                SalesDetailExtendedHolder detailEx = this.initSalesDetailExtended(detailExContent);
//                detailEx.setSalesOid(salesOid);
//                detailExtendeds.add(detailEx);
//            }
//            salesHolder.setDetailExtendeds(detailExtendeds);
//        }
        
        BuyerHolder buyer = docMsg.getBuyer();
        
        //record type is 'LOC'
        List<String[]> salesLocationList= map.get(FileParserUtil.RECORD_TYPE_LOCATION);
        List<SalesLocationHolder> locations = new ArrayList<SalesLocationHolder>();
        Map<String, String[]> temp = new HashMap<String, String[]>();
        if (salesLocationList != null && !salesLocationList.isEmpty())
        {
            for (String[] salesLocationContents : salesLocationList)
            {
                SalesLocationHolder salesLocation = this.initSalesLocation(salesLocationContents, buyer);
                if (!temp.containsKey(salesLocationContents[3]))
                {
                    // init Sales Location data
                    temp.put(salesLocationContents[3], salesLocationContents);
                    salesLocation.setSalesOid(salesOid);
                    locations.add(salesLocation);
                }
            }
            salesHolder.setLocations(locations);
        }
        
//        //record type is 'LEX'
//        List<String[]> salesDateLocationExList= map.get(FileParserUtil.RECORD_TYPE_LOCATION_DETAIL_EXTENDED);
//        if (salesDateLocationExList != null && !salesDateLocationExList.isEmpty())
//        {
//            List<SalesDateLocationDetailExtendedHolder> salesDateLocDetailExtendeds = new ArrayList<SalesDateLocationDetailExtendedHolder>();
//            for (String[] salesDateLocationExContents : salesDateLocationExList)
//            {
//                // init sales date Location detail extension data
//                SalesDateLocationDetailExtendedHolder locationDetailEx = this.initSalesDateLocationDetailExtended(salesDateLocationExContents);
//                locationDetailEx.setSalesOid(salesOid);
//                salesDateLocDetailExtendeds.add(locationDetailEx);
//                
//            }
//            salesHolder.setSalesDateLocationDetailExtendeds(salesDateLocDetailExtendeds);
//        }
        
        //init sales date location detail
        List<SalesDateLocationDetailHolder> locDetails = new ArrayList<SalesDateLocationDetailHolder>();
        for (String[] detailContent: detailList)
        {
            SalesDetailHolder detail = this.initSalesDetail(detailContent);
            for (String[] salesLocationContents : salesLocationList)
            {
                SalesLocationHolder salesLocation = this.initSalesLocation(salesLocationContents, buyer);
                if (detail.getLineSeqNo().equals(salesLocation.getDetailLineSeqNo()))
                {
                    SalesDateLocationDetailHolder locDetail = this.initSalesDateLocationDetail(salesLocationContents);
                    locDetail.setSalesOid(salesOid);
                    locDetail.setDetailLineSeqNo(detailMap.get(detail.getBuyerItemCode() + "@@" + detail.getBarcode()).getLineSeqNo());
                    locDetail.setDateLineSeqNo(salesDateMap.get(detail.getBusinessDate()).getLineSeqNo());
                    locDetail.setLocationLineSeqNo(salesLocation.getLineSeqNo());
                    locDetail.setSalesPrice(detail.getSalesPrice());
                    
                    locDetails.add(locDetail);
                }
            }
        }
        salesHolder.setSalesDateLocationDetail(locDetails);
        
        docMsg.setData(salesHolder);
        
        log.info(" :::: end to read conanical Sales source file");
    }
    
    
    private SalesHeaderHolder initSalesHeader(String[] content,SalesDocMsg docMsg) throws Exception
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        
        SalesHeaderHolder header = new SalesHeaderHolder();
        header.setSalesNo(content[1]);
        header.setDocAction(content[2]);
        header.setActionDate(fileParserUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setSalesDataType(content[4]);
        header.setSalesDate(fileParserUtil.dateValueOf(content[5], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setBuyerCode(content[6]);
        header.setBuyerName(content[7]);
        header.setBuyerAddr1(content[8]);
        header.setBuyerAddr2(content[9]);
        header.setBuyerAddr3(content[10]);
        header.setBuyerAddr4(content[11]);
        header.setBuyerCity(content[12]);
        header.setBuyerState(content[13]);
        header.setBuyerCtryCode(content[14]);
        header.setBuyerPostalCode(content[15]);
        header.setSupplierCode(content[16]);
        header.setSupplierName(content[17]);
        header.setSupplierAddr1(content[18]);
        header.setSupplierAddr2(content[19]);
        header.setSupplierAddr3(content[20]);
        header.setSupplierAddr4(content[21]);
        header.setSupplierCity(content[22]);
        header.setSupplierState(content[23]);
        header.setSupplierCtryCode(content[24]);
        header.setSupplierPostalCode(content[25]);
        header.setStoreCode(content[26]);
        header.setStoreName(content[27]);
        header.setStoreAddr1(content[28]);
        header.setStoreAddr2(content[29]);
        header.setStoreAddr3(content[30]);
        header.setStoreAddr4(content[31]);
        header.setStoreCity(content[32]);
        header.setStoreState(content[33]);
        header.setStoreCtryCode(content[34]);
        header.setStorePostalCode(content[35]);
        
        
        header.setTotalQty(fileParserUtil.decimalValueOf(content[36]));
        header.setTotalGrossSalesAmount(fileParserUtil.decimalValueOf(content[37]));
        header.setTotalDiscountAmount(fileParserUtil.decimalValueOf(content[38]));
        header.setTotalVatAmount(fileParserUtil.decimalValueOf(content[39]));
        header.setTotalNetSalesAmount(fileParserUtil.decimalValueOf(content[40]));
        header.setPeriodStartDate(fileParserUtil.dateValueOf(content[41], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setPeriodEndDate(fileParserUtil.dateValueOf(content[42], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        
        header.setSalesOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, header);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, header);
        
        header.trimAllString();
        header.setAllEmptyStringToNull();
        return header;

    }
    
    
    private SalesHeaderExtendedHolder initSalesHeaderExtended(String[] content)
    {
        SalesHeaderExtendedHolder salesExHolder = new SalesHeaderExtendedHolder();

        salesExHolder.setFieldName(content[2]);
        salesExHolder.setFieldType(content[3]);

        //Boolean value
        if(content[3].equals(EXTENDED_TYPE_BOOLEAN))
        {
            salesExHolder.setBoolValue((content[4].equals("TRUE") ? Boolean.TRUE:Boolean.FALSE));
        }
        
        //Float value
        if(content[3].equals(EXTENDED_TYPE_FLOAT))
        {
            salesExHolder.setFloatValue(FileParserUtil.getInstance().decimalValueOf(content[4]));
        }

        //Integer value
        if(content[3].equals(EXTENDED_TYPE_INTEGER))
        {
            salesExHolder.setIntValue(Integer.parseInt(content[4]));
        }

        //String value
        if(content[3].equals(EXTENDED_TYPE_STRING))
        {
            salesExHolder.setStringValue(content[4]);
        }
        
        // Date value
        if (EXTENDED_TYPE_DATE.equals(content[3]))
        {
            salesExHolder.setDateValue(FileParserUtil.getInstance().dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        
        return salesExHolder;

    }
    
    
    private SalesDetailHolder initSalesDetail(String[] content) throws Exception
    {
        FileParserUtil util = FileParserUtil.getInstance();
        SalesDetailHolder detail = new SalesDetailHolder();
        detail.setLineSeqNo(util.integerValueOf(content[2]));
        detail.setPosId(content[3]);
        detail.setReceiptNo(content[4]);
        detail.setReceiptDate(FileParserUtil.getInstance().dateValueOf(content[5], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        detail.setBusinessDate(DateUtil.getInstance().getFirstTimeOfDay(FileParserUtil.getInstance().dateValueOf(content[6], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS)));
        detail.setBuyerItemCode(content[7]);
        detail.setSupplierItemCode(content[8]);
        detail.setBarcode(content[9]);
        detail.setItemDesc(content[10]);
        detail.setBrand(content[11]);
        detail.setDeptCode(content[12]);
        detail.setDeptName(content[13]);
        detail.setSubDeptCode(content[14]);
        detail.setSubDeptName(content[15]);
        detail.setColourCode(content[16]);
        detail.setColourDesc(content[17]);
        detail.setSizeCode(content[18]);
        detail.setSizeDesc(content[19]);
        detail.setPackingFactor(util.decimalValueOf(content[20]));
        detail.setSalesBaseUnit(content[21]);
        detail.setSalesUom(content[22]);
        detail.setSalesQty(util.decimalValueOf(content[23]));
        detail.setItemCost(util.decimalValueOf(content[24]));
        detail.setSalesPrice(util.decimalValueOf(content[25]));
        detail.setItemSalesAmount(util.decimalValueOf(content[26]));
        detail.setSalesDiscountAmount(util.decimalValueOf(content[27]));
        detail.setVatAmount(util.decimalValueOf(content[28]));
        detail.setItemNetSalesAmount(util.decimalValueOf(content[29]));
        detail.setItemRemarks(content[30]);
        
        detail.trimAllString();
        detail.setAllEmptyStringToNull();
        
        return detail;
    }
    
    
    private SalesDetailExtendedHolder initSalesDetailExtended(String[] content)
    {
        SalesDetailExtendedHolder detailEx = new SalesDetailExtendedHolder();
        detailEx.setLineSeqNo(Integer.valueOf(content[1]));
        detailEx.setFieldName(content[2]);
        detailEx.setFieldType(content[3]);

        //Boolean value
        if(EXTENDED_TYPE_BOOLEAN.equals(content[3]))
        {
            detailEx.setBoolValue((content[4].equals("TRUE") ? Boolean.TRUE:Boolean.FALSE));
        }

        //Float value
        if(EXTENDED_TYPE_FLOAT.equals(content[3]))
        {
            detailEx.setFloatValue(FileParserUtil.getInstance().decimalValueOf(content[4]));
        }

        //Integer value
        if(EXTENDED_TYPE_INTEGER.equals(content[3]))
        {
            detailEx.setIntValue(Integer.parseInt(content[4]));
        }

        //String value
        if(content[3].equals(EXTENDED_TYPE_STRING))
        {
            detailEx.setStringValue(content[4]);
        }
        
        // Date value
        if (EXTENDED_TYPE_DATE.equals(content[3]))
        {
            detailEx.setDateValue(FileParserUtil.getInstance().dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        
        return detailEx;
    }
    
    
    private SalesLocationHolder initSalesLocation(String[] contents, BuyerHolder buyer) throws Exception
    {
        FileParserUtil util = FileParserUtil.getInstance();
        SalesLocationHolder salesLocation = new SalesLocationHolder();
        salesLocation.setDetailLineSeqNo(Integer.parseInt(contents[2]));
        salesLocation.setLineSeqNo(Integer.parseInt(contents[3]));
        salesLocation.setLocationCode(contents[4]);
        salesLocation.setLocationName(contents[5]);
        
        BuyerStoreHolder holder = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(buyer.getBuyerCode(), salesLocation.getLocationCode());
        if (null != holder)
        {
            salesLocation.setLocationCode(holder.getStoreCode());
            salesLocation.setLocationName(holder.getStoreName());
            salesLocation.setLocationAddr1(holder.getStoreAddr1());
            salesLocation.setLocationAddr2(holder.getStoreAddr2());
            salesLocation.setLocationAddr3(holder.getStoreAddr3());
            salesLocation.setLocationAddr4(holder.getStoreAddr4());
            salesLocation.setLocationAddr5(holder.getStoreAddr5());
            salesLocation.setLocationCity(holder.getStoreCity());
            salesLocation.setLocationState(holder.getStoreState());
            salesLocation.setLocationCtryCode(holder.getStoreCtryCode());
            salesLocation.setLocationPostalCode(holder.getStorePostalCode());
        }
        
        salesLocation.setSalesQty(util.decimalValueOf(contents[6]));
        
        salesLocation.trimAllString();
        salesLocation.setAllEmptyStringToNull();
        
        return salesLocation;
    }
    
    private SalesDateLocationDetailHolder initSalesDateLocationDetail(String[] contents) throws Exception
    {
        FileParserUtil util = FileParserUtil.getInstance();
        SalesDateLocationDetailHolder locDetail = new SalesDateLocationDetailHolder();
        
        locDetail.setSalesQty(util.decimalValueOf(contents[6]));
        locDetail.setItemCost(util.decimalValueOf(contents[7]));
        locDetail.setSalesPrice(util.decimalValueOf(contents[8]));
        locDetail.setSalesAmount(util.decimalValueOf(contents[9]));
        locDetail.setSalesDiscountAmount(util.decimalValueOf(contents[10]));
        locDetail.setVatAmount(util.decimalValueOf(contents[11]));
        locDetail.setSalesNetAmount(util.decimalValueOf(contents[12]));
        
        locDetail.trimAllString();
        locDetail.setAllEmptyStringToNull();
        
        return locDetail;
    }
    
    private SalesDateHolder initSalesDate(SalesDetailHolder detail)
    {
        SalesDateHolder salesDate = new SalesDateHolder();
        salesDate.setSalesDate(DateUtil.getInstance().getFirstTimeOfDay(detail.getBusinessDate()));
        return salesDate;
    }
    
    
    private SalesDateLocationDetailExtendedHolder initSalesDateLocationDetailExtended(String[] content)
    {
        SalesDateLocationDetailExtendedHolder salesDateLocDetailEx = new SalesDateLocationDetailExtendedHolder();
        salesDateLocDetailEx.setDetailLineSeqNo(Integer.valueOf(content[1]));
        salesDateLocDetailEx.setLocationLineSeqNo(Integer.valueOf(content[2]));
        salesDateLocDetailEx.setFieldName(content[3]);
        salesDateLocDetailEx.setFieldType(content[4]);
        
        //Boolean value
        if(EXTENDED_TYPE_BOOLEAN.equals(content[4]))
        {
            salesDateLocDetailEx.setBoolValue(content[5].equalsIgnoreCase("TRUE") ? Boolean.TRUE:Boolean.FALSE);
        }

        //Float value
        if(EXTENDED_TYPE_FLOAT.equals(content[4]))
        {
            salesDateLocDetailEx.setFloatValue(FileParserUtil.getInstance().decimalValueOf(content[5]));
        }

        //Integer value
        if(EXTENDED_TYPE_INTEGER.equals(content[4]))
        {
            salesDateLocDetailEx.setIntValue(Integer.parseInt(content[5]));
        }

        //String value
        if(EXTENDED_TYPE_STRING.equals(content[4]))
        {
            salesDateLocDetailEx.setStringValue(content[5]);
        }
        
        // Date value
        if (EXTENDED_TYPE_DATE.equals(content[3]))
        {
            salesDateLocDetailEx.setDateValue(FileParserUtil.getInstance().dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        
        return salesDateLocDetailEx;
    }


    @Override
    public byte[] getFileByte(SalesHolder data, File targetFile,
        String expectedFormat) throws Exception
    {
        StringBuffer content = new StringBuffer();
        
        SalesHolder salesHolder = data;
        SalesHeaderHolder salesHeader = salesHolder.getSalesHeader();
        //HDR 
        if(salesHeader!=null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER);
            String header = salesHeader.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(header);
            content.append(END_LINE);
        }
        
        //DET
        List<SalesDetailHolder> details = salesHolder.getDetails();
        if (details != null && !details.isEmpty())
        {
            for (SalesDetailHolder salesDetailHolder : salesHolder.getDetails())
            {
                content.append(FileParserUtil.RECORD_TYPE_DETAIL).append(VERTICAL_SEPARATE);
                content.append(salesHeader.getSalesNo());
                String detail = salesDetailHolder.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(detail);
                content.append(END_LINE);
            }
        }
        
        //LOC
        List<SalesDateLocationDetailHolder> salesLocationDetails = salesHolder.getSalesDateLocationDetail();
        List<SalesLocationHolder> salesLocations = salesHolder.getLocations();
        if (salesLocations != null && !salesLocations.isEmpty()
            && salesLocationDetails != null && !salesLocationDetails.isEmpty())
        {
            for (SalesDateLocationDetailHolder locDetail : salesLocationDetails)
            {
                SalesLocationHolder param = null;
                for (SalesLocationHolder salesLocation : salesLocations)
                {
                    if (salesLocation.getLineSeqNo().equals(locDetail.getLocationLineSeqNo()) && salesLocation.getDetailLineSeqNo().equals(locDetail.getDetailLineSeqNo()))
                    {
                        param = salesLocation;
                    }
                }
                content.append(FileParserUtil.RECORD_TYPE_LOCATION).append(VERTICAL_SEPARATE);
                content.append(salesHolder.getSalesHeader().getSalesNo());
                String locDetailString = locDetail.toStringWithDelimiterCharacter(VERTICAL_SEPARATE, param);
                content.append(locDetailString);
                content.append(END_LINE);
            }
        }
        
        return content.toString().getBytes(Charset.defaultCharset());
    }


    @Override
    public String getTargetFilename(SalesHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.PO + DOC_FILENAME_DELIMITOR
            + data.getSalesHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getSalesHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getSalesHeader().getSalesNo()) + DOC_FILENAME_DELIMITOR + data.getSalesHeader().getSalesOid()
            + ".txt";
    }


    @Override
    protected String translate(SalesDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);
        
        return targetFile.getName();
    }
    
}
