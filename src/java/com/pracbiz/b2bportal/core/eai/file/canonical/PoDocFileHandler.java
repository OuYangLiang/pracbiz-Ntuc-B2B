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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class PoDocFileHandler extends DefaultDocFileHandler<PoDocMsg, PoHolder> implements
    CoreCommonConstants
{
    private final static Logger log = LoggerFactory.getLogger(PoDocFileHandler.class);
    
    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }

    
    @Override
    public void readFileContent(PoDocMsg docMsg, byte[] input)
        throws Exception
    {
        log.info(" :::: start to read conanical PO source file");
        // read source file and group according by record type
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        
        PoHolder poHolder = new PoHolder();
        BigDecimal poOid = docMsg.getDocOid();
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        PoHeaderHolder header = this.initPoHeader(headerContents,docMsg);
        poHolder.setPoHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList= map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<PoHeaderExtendedHolder> headerExtendeds = new ArrayList<PoHeaderExtendedHolder>();
            for (String[] headerExContent : headerExList)
            {
                PoHeaderExtendedHolder poExHolder = this.initPoHeaderExtended(headerExContent);
                poExHolder.setPoOid(poOid);
                headerExtendeds.add(poExHolder);
            }
            poHolder.setHeaderExtendeds(headerExtendeds);
        }
        
        //record type is 'DET'
        List<String[]> detailList= map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        List<PoDetailHolder> details = new ArrayList<PoDetailHolder>();
        for (String[] detailContent: detailList)
        {
            PoDetailHolder detail = this.initPoDetail(detailContent);
            detail.setPoOid(poOid);
            details.add(detail);
        }
        poHolder.setDetails(details);
        
        //record type is 'DEX'
        List<String[]> detailExList= map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
        
        if (detailExList != null && !detailExList.isEmpty())
        {
            List<PoDetailExtendedHolder> detailExtendeds = new ArrayList<PoDetailExtendedHolder>();
            for (String[] detailExContent : detailExList)
            {
                PoDetailExtendedHolder detailEx = this.initPoDetailExtended(detailExContent);
                detailEx.setPoOid(poOid);
                detailExtendeds.add(detailEx);
            }
            poHolder.setDetailExtendeds(detailExtendeds);
        }
        
        
        //record type is 'LOC'
        List<String[]> poLocationList= map.get(FileParserUtil.RECORD_TYPE_LOCATION);
        List<PoLocationHolder> locations = new ArrayList<PoLocationHolder>();
        List<PoLocationDetailHolder> locationDetails = new ArrayList<PoLocationDetailHolder>();
        Map<String, String[]> temp = new HashMap<String, String[]>();
        if (poLocationList != null && !poLocationList.isEmpty())
        {
            for (String[] poLocationContents : poLocationList)
            {
                if (!temp.containsKey(poLocationContents[3]))
                {
                    // init PO Location data
                    temp.put(poLocationContents[3], poLocationContents);
                    PoLocationHolder poLocation = this.initPoLocation(poLocationContents);
                    poLocation.setPoOid(poOid);
                    locations.add(poLocation);
                }
                
                // init PO Location detail data
                PoLocationDetailHolder locationDetail = this.initPoLocationDetail(poLocationContents);
                locationDetail.setPoOid(poOid);
                locationDetails.add(locationDetail);
            }
            poHolder.setLocations(locations);
            poHolder.setLocationDetails(locationDetails);
        }
        
        
        //record type is 'LEX'
        List<String[]> poLocationExList= map.get(FileParserUtil.RECORD_TYPE_LOCATION_DETAIL_EXTENDED);
        if (poLocationExList != null && !poLocationExList.isEmpty())
        {
            List<PoLocationDetailExtendedHolder> poLocDetailExtendeds = new ArrayList<PoLocationDetailExtendedHolder>();
            for (String[] poLocationExContents : poLocationExList)
            {
                // init PO Location detail extension data
                PoLocationDetailExtendedHolder locationDetailEx = this.initPoLocationDetailExtended(poLocationExContents);
                locationDetailEx.setPoOid(poOid);
                poLocDetailExtendeds.add(locationDetailEx);
                
            }
            poHolder.setPoLocDetailExtendeds(poLocDetailExtendeds);
        }
        
        docMsg.setData(poHolder);
        
        log.info(" :::: end to read conanical PO source file");
    }

    
    @Override
    public byte[] getFileByte(PoHolder data, File targetFile, String expectedFormat)
        throws Exception
    {
        
        
        StringBuffer content = new StringBuffer();
        
        PoHolder poHolder = data;
        PoHeaderHolder poHeader = poHolder.getPoHeader();
        //HDR 
        if(poHeader!=null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER);
            String heaer = poHeader.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(heaer);
            content.append(END_LINE);
        }
        
        //HEX
        List<PoHeaderExtendedHolder> headerExs = poHolder.getHeaderExtendeds();
        if(headerExs != null && !headerExs.isEmpty())
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED).append(VERTICAL_SEPARATE);
            content.append(headerExs.size()).append(VERTICAL_SEPARATE);
            //field-name,field-type,field-value repeat
            boolean isFirst = true;
            for (PoHeaderExtendedHolder headerExtended : headerExs)
            {
                if (!isFirst) content.append(VERTICAL_SEPARATE);
                String headerEx = headerExtended.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(headerEx);
                isFirst = false;
            }
            content.append(END_LINE);
            //end line;
        }
        
        //DET
        List<PoDetailHolder> details = poHolder.getDetails();
        List<PoDetailExtendedHolder> poDetailExs = poHolder.getDetailExtendeds();
        Map<Integer, List<PoDetailExtendedHolder>> detailExMaps = this.groupByLineSeqNo(poDetailExs);
        if (details != null && !details.isEmpty())
        {
            for (PoDetailHolder poDetailHolder : poHolder.getDetails())
            {
                content.append(FileParserUtil.RECORD_TYPE_DETAIL).append(VERTICAL_SEPARATE);
                content.append(poHeader.getPoNo()).append(VERTICAL_SEPARATE);
                String detail = poDetailHolder.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(detail);
                content.append(END_LINE);
              
                //DEX
                this.appendDetailExs(content, detailExMaps, poDetailHolder.getLineSeqNo());
            }
        }
        
        //LOC
        List<PoLocationDetailHolder> poLocationDetails = poHolder.getLocationDetails();
        List<PoLocationHolder> poLocations = poHolder.getLocations();
        List<PoLocationDetailExtendedHolder> poLocDetailExList = poHolder.getPoLocDetailExtendeds();
        Map<String, List<PoLocationDetailExtendedHolder>> locDetailExMaps = this.groupByLocationSeqNoAndDetailLineSeqNo(poLocDetailExList);
        if (poLocations != null && !poLocations.isEmpty()
            && poLocationDetails != null && !poLocationDetails.isEmpty())
        {
            for (PoLocationHolder poLocation : poLocations)
            {
                Integer locationSeqNo = poLocation.getLineSeqNo();
                for (Iterator<PoLocationDetailHolder> it = poLocationDetails
                    .iterator(); it.hasNext();)
                {
                    PoLocationDetailHolder locDetail = it.next();
                    if (!locationSeqNo.equals(locDetail.getLocationLineSeqNo()))
                    {
                        continue;
                    }
                    
                    content.append(FileParserUtil.RECORD_TYPE_LOCATION).append(VERTICAL_SEPARATE);
                    content.append(poHolder.getPoHeader().getPoNo()).append(VERTICAL_SEPARATE);
                    String locDetailString = locDetail.toStringWithDelimiterCharacter(VERTICAL_SEPARATE, poLocation);
                    content.append(locDetailString);
                    content.append(locDetail.getLocationFocQty()).append(END_LINE);
                    
                    //LEX
                    this.appendLocDetailExs(content, locDetailExMaps, locDetail);
                }
            }
        }
        
        return content.toString().getBytes(Charset.defaultCharset());
    }
    
    
    @Override
    protected String translate(PoDocMsg docMsg)
            throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);
        
        return targetFile.getName();
    }
    

    @Override
    public String getTargetFilename(PoHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.PO + DOC_FILENAME_DELIMITOR
        	+ data.getPoHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getPoHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getPoHeader().getPoNo()) + DOC_FILENAME_DELIMITOR + data.getPoHeader().getPoOid()
            + ".txt";
    }

    
    public PoHeaderHolder initPoHeader(String[] content,PoDocMsg docMsg) throws Exception
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        
        PoHeaderHolder header = new PoHeaderHolder();
        header.setPoNo(content[1]);
        header.setDocAction(content[2]);
        header.setActionDate(fileParserUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setPoType(PoType.valueOf(content[4].toUpperCase(Locale.US)));
        header.setPoSubType(content[5]);
        header.setPoDate(fileParserUtil.dateValueOf(content[6], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setDeliveryDateFrom(fileParserUtil.dateValueOf(content[7], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setDeliveryDateTo(fileParserUtil.dateValueOf(content[8], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setExpiryDate(fileParserUtil.dateValueOf(content[9], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setBuyerCode(content[10]);
        header.setBuyerName(content[11]);
        header.setBuyerAddr1(content[12]);
        header.setBuyerAddr2(content[13]);
        header.setBuyerAddr3(content[14]);
        header.setBuyerAddr4(content[15]);
        header.setBuyerCity(content[16]);
        header.setBuyerState(content[17]);
        header.setBuyerCtryCode(content[18]);
        header.setBuyerPostalCode(content[19]);
        header.setSupplierCode(content[20]);
        header.setSupplierName(content[21]);
        header.setSupplierAddr1(content[22]);
        header.setSupplierAddr2(content[23]);
        header.setSupplierAddr3(content[24]);
        header.setSupplierAddr4(content[25]);
        header.setSupplierCity(content[26]);
        header.setSupplierState(content[27]);
        header.setSupplierCtryCode(content[28]);
        header.setSupplierPostalCode(content[29]);
        header.setShipToCode(content[30]);
        header.setShipToName(content[31]);
        header.setShipToAddr1(content[32]);
        header.setShipToAddr2(content[33]);
        header.setShipToAddr3(content[34]);
        header.setShipToAddr4(content[35]);
        header.setShipToCity(content[36]);
        header.setShipToState(content[37]);
        header.setShipToCtryCode(content[38]);
        header.setShipToPostalCode(content[39]);
        header.setDeptCode(content[40]);
        header.setDeptName(content[41]);
        header.setSubDeptCode(content[42]);
        header.setSubDeptName(content[43]);
        header.setCreditTermCode(content[44]);
        header.setCreditTermDesc(content[45]);
        
        
        header.setTotalCost(fileParserUtil.decimalValueOf(content[46]));
        header.setAdditionalDiscountAmount(fileParserUtil.decimalValueOf(content[47]));
        header.setAdditionalDiscountPercent(fileParserUtil.decimalValueOf(content[48]));
        header.setNetCost(fileParserUtil.decimalValueOf(content[49]));
        header.setGrossProfitMargin(fileParserUtil.decimalValueOf(content[50]));
        header.setTotalSharedCost(fileParserUtil.decimalValueOf(content[51]));
        header.setTotalGrossCost(fileParserUtil.decimalValueOf(content[52]));
        header.setTotalRetailAmount(fileParserUtil.decimalValueOf(content[53]));
        header.setOrderRemarks(content[54]);
        header.setPeriodStartDate(fileParserUtil.dateValueOf(content[55], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setPeriodEndDate(fileParserUtil.dateValueOf(content[56], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setPoSubType2(content[57]);
        
        header.setPoOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, header);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, header);
        header.setPoStatus(PoStatus.NEW);
        return header;

    }
    
    
    public PoHeaderExtendedHolder initPoHeaderExtended(String[] content)
    {
        PoHeaderExtendedHolder poExHolder = new PoHeaderExtendedHolder();

        poExHolder.setFieldName(content[2]);
        poExHolder.setFieldType(content[3]);

        //Boolean value
        if(content[3].equals(EXTENDED_TYPE_BOOLEAN))
        {
            poExHolder.setBoolValue((content[4].equals("TRUE") ? Boolean.TRUE:Boolean.FALSE));
        }
        
        //Float value
        if(content[3].equals(EXTENDED_TYPE_FLOAT))
        {
            poExHolder.setFloatValue(FileParserUtil.getInstance().decimalValueOf(content[4]));
        }

        //Integer value
        if(content[3].equals(EXTENDED_TYPE_INTEGER))
        {
            poExHolder.setIntValue(Integer.parseInt(content[4]));
        }

        //String value
        if(content[3].equals(EXTENDED_TYPE_STRING))
        {
            poExHolder.setStringValue(content[4]);
        }
        
        // Date value
        if (EXTENDED_TYPE_DATE.equals(content[3]))
        {
            poExHolder.setDateValue(FileParserUtil.getInstance().dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        
        return poExHolder;

    }
    
    
    public PoDetailHolder initPoDetail(String[] content)
    {
        FileParserUtil util = FileParserUtil.getInstance();
        PoDetailHolder detail = new PoDetailHolder();
        detail.setLineSeqNo(util.integerValueOf(content[2]));
        detail.setBuyerItemCode(content[3]);
        detail.setSupplierItemCode(content[4]);
        detail.setBarcode(content[5]);
        detail.setItemDesc(content[6]);
        detail.setBrand(content[7]);
        detail.setColourCode(content[8]);
        detail.setColourDesc(content[9]);
        detail.setSizeCode(content[10]);
        detail.setSizeDesc(content[11]);
        detail.setPackingFactor(util.decimalValueOf(content[12]));
        detail.setOrderBaseUnit(content[13]);
        detail.setOrderUom(content[14]);
        detail.setOrderQty(util.decimalValueOf(content[15]));
        detail.setFocBaseUnit(content[16]);
        detail.setFocUom(content[17]);
        detail.setFocQty(util.decimalValueOf(content[18]));
        detail.setUnitCost(util.decimalValueOf(content[19]));
        detail.setPackCost(util.decimalValueOf(content[20]));
        detail.setCostDiscountAmount(util.decimalValueOf(content[21]));
        detail.setCostDiscountPercent(util.decimalValueOf(content[22]));
        detail.setRetailDiscountAmount(util.decimalValueOf(content[23]));
        detail.setNetUnitCost(util.decimalValueOf(content[24]));
        detail.setNetPackCost(util.decimalValueOf(content[25]));
        detail.setItemCost(util.decimalValueOf(content[26]));
        detail.setItemSharedCost(util.decimalValueOf(content[27]));
        detail.setItemGrossCost(util.decimalValueOf(content[28]));
        detail.setRetailPrice(util.decimalValueOf(content[29]));
        detail.setItemRetailAmount(util.decimalValueOf(content[30]));
        detail.setItemRemarks(content[31]);
        
        return detail;
    }
    
    
    public PoDetailExtendedHolder initPoDetailExtended(String[] content)
    {
        PoDetailExtendedHolder detailEx = new PoDetailExtendedHolder();
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
    
    
    public PoLocationHolder initPoLocation(String[] contents)
    {
        PoLocationHolder poLocation = new PoLocationHolder();
        poLocation.setLineSeqNo(Integer.parseInt(contents[3]));
        poLocation.setLocationCode(contents[4]);
        poLocation.setLocationName(contents[5]);
        return poLocation;
    }
    
    
    public PoLocationDetailHolder initPoLocationDetail(String[] contents)
    {
        PoLocationDetailHolder poLocationDetail = new PoLocationDetailHolder();
        poLocationDetail.setDetailLineSeqNo(Integer.parseInt(contents[2]));
        poLocationDetail.setLocationLineSeqNo(Integer.parseInt(contents[3]));
        poLocationDetail.setLocationShipQty(FileParserUtil.getInstance().decimalValueOf(contents[6]));
        poLocationDetail.setLocationFocQty(FileParserUtil.getInstance().decimalValueOf(contents[7]));
        poLocationDetail.setLineRefNo(contents[8]);
        return poLocationDetail;
    }
    
    
    public PoLocationDetailExtendedHolder initPoLocationDetailExtended(String[] content)
    {
        PoLocationDetailExtendedHolder poLocDetailEx = new PoLocationDetailExtendedHolder();
        poLocDetailEx.setDetailLineSeqNo(Integer.valueOf(content[1]));
        poLocDetailEx.setLocationLineSeqNo(Integer.valueOf(content[2]));
        poLocDetailEx.setFieldName(content[3]);
        poLocDetailEx.setFieldType(content[4]);
        
        //Boolean value
        if(EXTENDED_TYPE_BOOLEAN.equals(content[4]))
        {
            poLocDetailEx.setBoolValue(content[5].equalsIgnoreCase("TRUE") ? Boolean.TRUE:Boolean.FALSE);
        }

        //Float value
        if(EXTENDED_TYPE_FLOAT.equals(content[4]))
        {
            poLocDetailEx.setFloatValue(FileParserUtil.getInstance().decimalValueOf(content[5]));
        }

        //Integer value
        if(EXTENDED_TYPE_INTEGER.equals(content[4]))
        {
            poLocDetailEx.setIntValue(Integer.parseInt(content[5]));
        }

        //String value
        if(EXTENDED_TYPE_STRING.equals(content[4]))
        {
            poLocDetailEx.setStringValue(content[5]);
        }
        
        // Date value
        if (EXTENDED_TYPE_DATE.equals(content[3]))
        {
            poLocDetailEx.setDateValue(FileParserUtil.getInstance().dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        
        return poLocDetailEx;
    }
    
    
    private Map<Integer, List<PoDetailExtendedHolder>> groupByLineSeqNo(List<PoDetailExtendedHolder> details)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        
        Map<Integer, List<PoDetailExtendedHolder>> map = new HashMap<Integer, List<PoDetailExtendedHolder>>();
        for (PoDetailExtendedHolder detail : details)
        {
            Integer lineSeqNo = detail.getLineSeqNo();
            if (map.containsKey(lineSeqNo))
            {
                List<PoDetailExtendedHolder> list = map.get(lineSeqNo);
                list.add(detail);
                continue;
            }
            List<PoDetailExtendedHolder> list = new ArrayList<PoDetailExtendedHolder>();
            list.add(detail);
            map.put(lineSeqNo, list);
        }
        
        return map;
    }
    
    
    private Map<String, List<PoLocationDetailExtendedHolder>> groupByLocationSeqNoAndDetailLineSeqNo(List<PoLocationDetailExtendedHolder> details)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        
        Map<String, List<PoLocationDetailExtendedHolder>> map = new HashMap<String, List<PoLocationDetailExtendedHolder>>();
        
        for (PoLocationDetailExtendedHolder detail : details)
        {
            Integer detailLineSeqNo = detail.getDetailLineSeqNo();
            Integer locationLineSeqNo = detail.getLocationLineSeqNo();
            String key = locationLineSeqNo + "-" + detailLineSeqNo;
            if (map.containsKey(key))
            {
                List<PoLocationDetailExtendedHolder> list = map.get(key);
                list.add(detail);
                continue;
            }
            List<PoLocationDetailExtendedHolder> list = new ArrayList<PoLocationDetailExtendedHolder>();
            list.add(detail);
            map.put(key, list);
        }
        return map;
    }
    
    
    private void appendDetailExs(StringBuffer content, Map<Integer, List<PoDetailExtendedHolder>> detailExMaps,Integer lineSeqNo)
    {
        if (detailExMaps == null || detailExMaps.isEmpty())
        {
            return;
        }
        
        List<PoDetailExtendedHolder> currentDetailExs = detailExMaps.get(lineSeqNo);
        if (currentDetailExs == null || currentDetailExs.isEmpty()) return;
        
        content.append(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED).append(VERTICAL_SEPARATE);
        content.append(currentDetailExs.size()).append(VERTICAL_SEPARATE);
        boolean isFirst = true;
        for (PoDetailExtendedHolder poDetailEx : currentDetailExs)
        {
            if (!isFirst) content.append(VERTICAL_SEPARATE);
            String poDetailExString = poDetailEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(poDetailExString);
            isFirst = false;
        }
        content.append(END_LINE);
    }
    
    
    private void appendLocDetailExs(StringBuffer content, Map<String, List<PoLocationDetailExtendedHolder>> locDetailExMaps, PoLocationDetailHolder locDetail)
    {
        if(locDetailExMaps == null || locDetailExMaps.isEmpty())
        {
            return;
        }
            
        String key = locDetail.getLocationLineSeqNo() + "-" + locDetail.getDetailLineSeqNo();
        List<PoLocationDetailExtendedHolder> currentLocDetailExs = locDetailExMaps.get(key);
        if (currentLocDetailExs == null || currentLocDetailExs.isEmpty()) return;
        
        content.append(FileParserUtil.RECORD_TYPE_LOCATION_DETAIL_EXTENDED).append(VERTICAL_SEPARATE);
        content.append(currentLocDetailExs.size()).append(VERTICAL_SEPARATE);
        boolean isFirst = true;
        
        for (PoLocationDetailExtendedHolder poLocDetailEx : currentLocDetailExs)
        {
            if (!isFirst) content.append(VERTICAL_SEPARATE);
            String poLocDetailExString = poLocDetailEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(poLocDetailExString);
            isFirst = false;
        }
        
        content.append(END_LINE);
    }

    
//    private Map.Entry<String,List<PoLocationDetailExtendedHolder>>[] sortedHashtableByKey(Map<String,List<PoLocationDetailExtendedHolder>> map)
//    {
//   
//        Set<Entry<String,List<PoLocationDetailExtendedHolder>>> set = map.entrySet();
//   
//        @SuppressWarnings("unchecked")
//        Map.Entry<String,List<PoLocationDetailExtendedHolder>>[] entries = set.toArray(new Map.Entry[set.size()]);
//   
//        Arrays
//            .sort(
//                entries,
//                new Comparator<Entry<String, List<PoLocationDetailExtendedHolder>>>()
//                {
//                    public int compare(
//                        Entry<String, List<PoLocationDetailExtendedHolder>> entry1,
//                        Entry<String, List<PoLocationDetailExtendedHolder>> entry2)
//                    {
//                        String key1 = entry1.getKey();
//                        String key2 = entry2.getKey();
//
//                        return key1.compareTo(key2);
//                    }
//                });
//   
//        return entries;
//    }
    
    
//    public static void main(String[] args) throws Exception
//    {
//        PoDocFileHandler pdf = new PoDocFileHandler();
//        PoDocMsg docMsg = new PoDocMsg();
//        File file = new File(
//            "/Users/jiangming/Desktop/PO_ROB_111825_20121203113000.TXT");
//        pdf.readFileContent(docMsg, file);
//        System.out.println(docMsg.getData().getPoLocDetailExtendeds());
//        File target = new File("/Users/jiangming/Desktop/PO_ROB_SUPPLIER_0001_20121203113000_OUT.TXT");
//        pdf.translate(docMsg, target);
//        System.out.println("successful");
//    }
}
