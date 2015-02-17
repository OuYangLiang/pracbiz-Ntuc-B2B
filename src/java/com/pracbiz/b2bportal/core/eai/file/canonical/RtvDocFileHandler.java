package com.pracbiz.b2bportal.core.eai.file.canonical;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.RtvStatus;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.RtvDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class RtvDocFileHandler extends DefaultDocFileHandler<RtvDocMsg, RtvHolder>
        implements CoreCommonConstants
{
    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }


    @Override
    public void readFileContent(RtvDocMsg docMsg, byte[] input)
            throws Exception
    {
        // read source file and group according by record type
        RtvHolder data = new RtvHolder();
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        BigDecimal rtvOid = docMsg.getDocOid();
        
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        RtvHeaderHolder header = this.initHeader(headerContents,docMsg);
        data.setRtvHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList = map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<RtvHeaderExtendedHolder> headerExs= new ArrayList<RtvHeaderExtendedHolder>();
            for (String[] headerExArray : headerExList)
            {
                RtvHeaderExtendedHolder headerEx = this.initHeaderEx(headerExArray);
                headerEx.setRtvOid(rtvOid);
                headerExs.add(headerEx);
            }
            data.setHeaderExtended(headerExs);
        }
        
        //record type is 'DET'
        List<RtvDetailHolder> details = new ArrayList<RtvDetailHolder>();
        List<String[]> detailList = map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        for (String[] detailArray : detailList)
        {
            RtvDetailHolder detail = this.initDetail(detailArray);
            detail.setRtvOid(rtvOid);
            details.add(detail);
        }
        data.setRtvDetail(details);
        
        //record type is 'DEX'
        List<String[]> detailExList = map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
        if (detailExList != null && !detailExList.isEmpty())
        {
            List<RtvDetailExtendedHolder> detailExtendeds = new ArrayList<RtvDetailExtendedHolder>();
            for (String[] detailExArray : detailExList)
            {
                RtvDetailExtendedHolder detailEx = this.initDetailEx(detailExArray);
                detailEx.setRtvOid(rtvOid);
                detailExtendeds.add(detailEx);
            }
            data.setDetailExtended(detailExtendeds);
        }
        
      //record type is 'LOC'
        List<String[]> rtvLocationList= map.get(FileParserUtil.RECORD_TYPE_LOCATION);
        List<RtvLocationHolder> locations = new ArrayList<RtvLocationHolder>();
        List<RtvLocationDetailHolder> locationDetails = new ArrayList<RtvLocationDetailHolder>();
        Map<String, String[]> temp = new HashMap<String, String[]>();
        if (rtvLocationList != null && !rtvLocationList.isEmpty())
        {
            for (String[] rtvLocationContents : rtvLocationList)
            {
                if (!temp.containsKey(rtvLocationContents[3]))
                {
                    // init RTV Location data
                    temp.put(rtvLocationContents[3], rtvLocationContents);
                    RtvLocationHolder rtvLocation = this.initRtvLocation(rtvLocationContents);
                    rtvLocation.setRtvOid(rtvOid);
                    locations.add(rtvLocation);
                }
                
                // init RTV Location detail data
                RtvLocationDetailHolder locationDetail = this.initRtvLocationDetail(rtvLocationContents);
                locationDetail.setRtvOid(rtvOid);
                locationDetails.add(locationDetail);
            }
            data.setLocations(locations);
            data.setLocationDetails(locationDetails);
        }
        
        
        //record type is 'LEX'
        List<String[]> rtvLocationExList= map.get(FileParserUtil.RECORD_TYPE_LOCATION_DETAIL_EXTENDED);
        if (rtvLocationExList != null && !rtvLocationExList.isEmpty())
        {
            List<RtvLocationDetailExtendedHolder> rtvLocDetailExtendeds = new ArrayList<RtvLocationDetailExtendedHolder>();
            for (String[] rtvLocationExContents : rtvLocationExList)
            {
                // init RTV Location detail extension data
                RtvLocationDetailExtendedHolder locationDetailEx = this.initRtvLocationDetailExtended(rtvLocationExContents);
                locationDetailEx.setRtvOid(rtvOid);
                rtvLocDetailExtendeds.add(locationDetailEx);
                
            }
            data.setRtvLocDetailExtendeds(rtvLocDetailExtendeds);
        }
       
        docMsg.setData(data);
    }
    
    
    @Override
    public byte[] getFileByte(RtvHolder data, File targetFile,
        String expectedFormat) throws Exception
    {
        StringBuffer content = new StringBuffer();
        RtvHolder rtv = data;
        
        //HDR
        RtvHeaderHolder header = rtv.getRtvHeader();
        if (header != null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER).append(VERTICAL_SEPARATE);
            String header2tring = header.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(header2tring);
            content.append(END_LINE);
        }
        
        //HEX
        List<RtvHeaderExtendedHolder> headerExs = rtv.getHeaderExtended();
        if (headerExs != null && !headerExs.isEmpty())
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED).append(VERTICAL_SEPARATE);
            content.append(headerExs.size()).append(VERTICAL_SEPARATE);
            //field-name,field-type,field-value repeat
            boolean isFirst = true;
            for (RtvHeaderExtendedHolder headerEx : headerExs)
            {
                if (!isFirst) content.append(VERTICAL_SEPARATE);
                String headerEx2String = headerEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(headerEx2String);
                isFirst = false;
            }
            content.append(END_LINE);
        }

        //DET
        List<RtvDetailHolder> details = rtv.getRtvDetail();
        List<RtvDetailExtendedHolder> detailExs = rtv.getDetailExtended();
        Map<Integer,List<RtvDetailExtendedHolder>> detailExMaps = this.groupByLineSeqNo(detailExs);
        if(details != null && !details.isEmpty())
        {
            for (RtvDetailHolder detail : details)
            {
                content.append(FileParserUtil.RECORD_TYPE_DETAIL).append(VERTICAL_SEPARATE);
                content.append(header.getRtvNo()).append(VERTICAL_SEPARATE);
                String detail2String = detail.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(detail2String);
                content.append(END_LINE);
                
                //DEX
                this.appendDetailExs(content, detailExMaps, detail.getLineSeqNo());
            }
        }
        
        //LOC
        List<RtvLocationDetailHolder> rtvLocationDetails = data.getLocationDetails();
        List<RtvLocationHolder> rtvLocations = data.getLocations();
        List<RtvLocationDetailExtendedHolder> rtvLocDetailExList = data.getRtvLocDetailExtendeds();
        Map<String, List<RtvLocationDetailExtendedHolder>> locDetailExMaps = this.groupByLocationSeqNoAndDetailLineSeqNo(rtvLocDetailExList);
        if (rtvLocations != null && !rtvLocations.isEmpty()
            && rtvLocationDetails != null && !rtvLocationDetails.isEmpty())
        {
            for (RtvLocationHolder rtvLocation : rtvLocations)
            {
                Integer locationSeqNo = rtvLocation.getLineSeqNo();
                for (Iterator<RtvLocationDetailHolder> it = rtvLocationDetails
                    .iterator(); it.hasNext();)
                {
                    RtvLocationDetailHolder locDetail = it.next();
                    if (!locationSeqNo.equals(locDetail.getLocationLineSeqNo()))
                    {
                        continue;
                    }
                    
                    content.append(FileParserUtil.RECORD_TYPE_LOCATION).append(VERTICAL_SEPARATE);
                    content.append(data.getRtvHeader().getRtvNo()).append(VERTICAL_SEPARATE);
                    String locDetailString = locDetail.toStringWithDelimiterCharacter(VERTICAL_SEPARATE, rtvLocation);
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
    protected String translate(RtvDocMsg docMsg)
            throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);
        
        return targetFile.getName();
    }
    

    @Override
    public String getTargetFilename(RtvHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.RTV + DOC_FILENAME_DELIMITOR
            + data.getRtvHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getRtvHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getRtvHeader().getRtvNo()) + DOC_FILENAME_DELIMITOR + data.getRtvHeader().getRtvOid()
            + ".txt";
    }
    
    
    private RtvHeaderHolder initHeader(String[] content, RtvDocMsg docMsg) throws Exception
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        RtvHeaderHolder header = new RtvHeaderHolder();
        header.setRtvNo(content[1]);
        header.setDocAction(content[2]);
        header.setActionDate(fileUtil.dateValueOf(content[3],CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setRtvDate(fileUtil.dateValueOf(content[4],CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setCollectionDate(fileUtil.dateValueOf(content[5],CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setDoNo(content[6]);
        header.setDoDate(fileUtil.dateValueOf(content[7],CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setInvNo(content[8]);
        header.setInvDate(fileUtil.dateValueOf(content[9],CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
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
        header.setDeptCode(content[30]);
        header.setDeptName(content[31]);
        header.setSubDeptCode(content[32]);
        header.setSubDeptName(content[33]);
        header.setTotalCost(fileUtil.decimalValueOf(content[34]));
        header.setReasonCode(content[35]);
        header.setReasonDesc(content[36]);
        header.setRtvRemarks(content[37]);
        
        header.setRtvOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, header);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, header);
        header.setRtvStatus(RtvStatus.NEW);
        
        return header;
    }
    
    
    private RtvHeaderExtendedHolder initHeaderEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        RtvHeaderExtendedHolder headerEx = new RtvHeaderExtendedHolder();
        headerEx.setFieldName(content[2]);
        headerEx.setFieldType(content[3]);
        //Boolean value
        if(EXTENDED_TYPE_BOOLEAN.equals(content[3]))
        {
            headerEx.setBoolValue(content[4].equalsIgnoreCase("TRUE") ? Boolean.TRUE:Boolean.FALSE);
        }

        //Float value
        if(EXTENDED_TYPE_FLOAT.equals(content[3]))
        {
            headerEx.setFloatValue(fileUtil.decimalValueOf(content[4]));
        }

        //Integer value
        if(EXTENDED_TYPE_INTEGER.equals(content[3]))
        {
            headerEx.setIntValue(fileUtil.integerValueOf(content[4]));
        }

        //String value
        if(EXTENDED_TYPE_STRING.equals(content[3]))
        {
            headerEx.setStringValue(content[4]);
        }
        
        // Date value
        if (EXTENDED_TYPE_DATE.equals(content[3]))
        {
            headerEx.setDateValue(fileUtil.dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        
        return headerEx;
    }
    
    
    private RtvDetailHolder initDetail(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        RtvDetailHolder detail = new RtvDetailHolder();
        detail.setLineSeqNo(fileUtil.integerValueOf(content[2]));
        detail.setBuyerItemCode(content[3]);
        detail.setSupplierItemCode(content[4]);
        detail.setBarcode(content[5]);
        detail.setItemDesc(content[6]);
        detail.setBrand(content[7]);
        detail.setColourCode(content[8]);
        detail.setColourDesc(content[9]);
        detail.setSizeCode(content[10]);
        detail.setSizeDesc(content[11]);
        detail.setDoNo(content[12]);
        detail.setDoDate(fileUtil.dateValueOf(content[13], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        detail.setInvNo(content[14]);
        detail.setInvDate(fileUtil.dateValueOf(content[15], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        detail.setPackingFactor(fileUtil.decimalValueOf(content[16]));
        detail.setReturnBaseUnit(content[17]);
        detail.setReturnUom(content[18]);
        detail.setReturnQty(fileUtil.decimalValueOf(content[19]));
        detail.setUnitCost(fileUtil.decimalValueOf(content[20]));
        detail.setCostDiscountAmount(fileUtil.decimalValueOf(content[21]));
        detail.setItemCost(fileUtil.decimalValueOf(content[22]));
        detail.setRetailPrice(fileUtil.decimalValueOf(content[23]));
        detail.setItemRetailAmount(fileUtil.decimalValueOf(content[24]));
        detail.setReasonCode(content[25]);
        detail.setReasonDesc(content[26]);
        detail.setLineRefNo(content[27]);
      
        return detail;
    }
    
    
    private RtvDetailExtendedHolder initDetailEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        
        RtvDetailExtendedHolder detailEx = new RtvDetailExtendedHolder();
        detailEx.setLineSeqNo(fileUtil.integerValueOf(content[1]));
        detailEx.setFieldName(content[2]);
        detailEx.setFieldType(content[3]);
        //Boolean value
        if(EXTENDED_TYPE_BOOLEAN.equals(content[3]))
        {
            detailEx.setBoolValue(content[4].equalsIgnoreCase("TRUE") ? Boolean.TRUE:Boolean.FALSE);
        }

        //Float value
        if(EXTENDED_TYPE_FLOAT.equals(content[3]))
        {
            detailEx.setFloatValue(fileUtil.decimalValueOf(content[4]));
        }

        //Integer value
        if(EXTENDED_TYPE_INTEGER.equals(content[3]))
        {
            detailEx.setIntValue(fileUtil.integerValueOf(content[4]));
        }

        //String value
        if(EXTENDED_TYPE_STRING.equals(content[3]))
        {
            detailEx.setStringValue(content[4]);
        }
        
        // Date value
        if (EXTENDED_TYPE_DATE.equals(content[3]))
        {
            detailEx.setDateValue(fileUtil.dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        return detailEx;
    }

    private void appendDetailExs(StringBuffer content, Map<Integer, List<RtvDetailExtendedHolder>> detailExMaps,Integer lineSeqNo)
    {
        if(detailExMaps == null || detailExMaps.isEmpty())
        {
            return;
        }
        
        List<RtvDetailExtendedHolder> detailExs = detailExMaps.get(lineSeqNo);
        if (detailExs == null || detailExs.isEmpty()) return;
        
        content.append(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED).append(VERTICAL_SEPARATE);
        content.append(detailExs.size()).append(VERTICAL_SEPARATE);
        boolean isFirst = true;
        for(RtvDetailExtendedHolder detailEx : detailExs)
        {
            if (!isFirst) content.append(VERTICAL_SEPARATE);
            String detailEx2String = detailEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(detailEx2String);
            isFirst = false;
        }
        content.append(END_LINE);
    }
    
    
    private Map<Integer, List<RtvDetailExtendedHolder>> groupByLineSeqNo(List<RtvDetailExtendedHolder> details)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        
        Map<Integer, List<RtvDetailExtendedHolder>> map = new HashMap<Integer, List<RtvDetailExtendedHolder>>();
        for (RtvDetailExtendedHolder detail : details)
        {
            Integer lineSeqNo = detail.getLineSeqNo();
            if (map.containsKey(lineSeqNo))
            {
                List<RtvDetailExtendedHolder> list = map.get(lineSeqNo);
                list.add(detail);
                continue;
            }
            List<RtvDetailExtendedHolder> list = new ArrayList<RtvDetailExtendedHolder>();
            list.add(detail);
            map.put(lineSeqNo, list);
        }
        
        return map;
    }
    
    
    private RtvLocationHolder initRtvLocation(String[] contents)
    {
        RtvLocationHolder rtvLocation = new RtvLocationHolder();
        rtvLocation.setLineSeqNo(Integer.parseInt(contents[3]));
        rtvLocation.setLocationCode(contents[4]);
        rtvLocation.setLocationName(contents[5]);
        return rtvLocation;
    }
    
    
    private RtvLocationDetailHolder initRtvLocationDetail(String[] contents)
    {
        RtvLocationDetailHolder rtvLocationDetail = new RtvLocationDetailHolder();
        rtvLocationDetail.setDetailLineSeqNo(Integer.parseInt(contents[2]));
        rtvLocationDetail.setLocationLineSeqNo(Integer.parseInt(contents[3]));
        rtvLocationDetail.setLocationShipQty(FileParserUtil.getInstance().decimalValueOf(contents[6]));
        rtvLocationDetail.setLocationFocQty(FileParserUtil.getInstance().decimalValueOf(contents[7]));
        return rtvLocationDetail;
    }
    
    
    private RtvLocationDetailExtendedHolder initRtvLocationDetailExtended(String[] content)
    {
        RtvLocationDetailExtendedHolder rtvLocDetailEx = new RtvLocationDetailExtendedHolder();
        rtvLocDetailEx.setDetailLineSeqNo(Integer.valueOf(content[1]));
        rtvLocDetailEx.setLocationLineSeqNo(Integer.valueOf(content[2]));
        rtvLocDetailEx.setFieldName(content[3]);
        rtvLocDetailEx.setFieldType(content[4]);
        
        //Boolean value
        if(EXTENDED_TYPE_BOOLEAN.equals(content[4]))
        {
            rtvLocDetailEx.setBoolValue(content[5].equalsIgnoreCase("TRUE") ? Boolean.TRUE:Boolean.FALSE);
        }

        //Float value
        if(EXTENDED_TYPE_FLOAT.equals(content[4]))
        {
            rtvLocDetailEx.setFloatValue(FileParserUtil.getInstance().decimalValueOf(content[5]));
        }

        //Integer value
        if(EXTENDED_TYPE_INTEGER.equals(content[4]))
        {
            rtvLocDetailEx.setIntValue(Integer.parseInt(content[5]));
        }

        //String value
        if(EXTENDED_TYPE_STRING.equals(content[4]))
        {
            rtvLocDetailEx.setStringValue(content[5]);
        }
        
        // Date value
        if (EXTENDED_TYPE_DATE.equals(content[3]))
        {
            rtvLocDetailEx.setDateValue(FileParserUtil.getInstance().dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        
        return rtvLocDetailEx;
    }
    
    
    private Map<String, List<RtvLocationDetailExtendedHolder>> groupByLocationSeqNoAndDetailLineSeqNo(List<RtvLocationDetailExtendedHolder> details)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        
        Map<String, List<RtvLocationDetailExtendedHolder>> map = new HashMap<String, List<RtvLocationDetailExtendedHolder>>();
        
        for (RtvLocationDetailExtendedHolder detail : details)
        {
            Integer detailLineSeqNo = detail.getDetailLineSeqNo();
            Integer locationLineSeqNo = detail.getLocationLineSeqNo();
            String key = locationLineSeqNo + "-" + detailLineSeqNo;
            if (map.containsKey(key))
            {
                List<RtvLocationDetailExtendedHolder> list = map.get(key);
                list.add(detail);
                continue;
            }
            List<RtvLocationDetailExtendedHolder> list = new ArrayList<RtvLocationDetailExtendedHolder>();
            list.add(detail);
            map.put(key, list);
        }
        return map;
    }
    
    
    private void appendLocDetailExs(StringBuffer content, Map<String, List<RtvLocationDetailExtendedHolder>> locDetailExMaps, RtvLocationDetailHolder locDetail)
    {
        if(locDetailExMaps == null || locDetailExMaps.isEmpty())
        {
            return;
        }
            
        String key = locDetail.getLocationLineSeqNo() + "-" + locDetail.getDetailLineSeqNo();
        List<RtvLocationDetailExtendedHolder> currentLocDetailExs = locDetailExMaps.get(key);
        if (currentLocDetailExs == null || currentLocDetailExs.isEmpty()) return;
        
        content.append(FileParserUtil.RECORD_TYPE_LOCATION_DETAIL_EXTENDED).append(VERTICAL_SEPARATE);
        content.append(currentLocDetailExs.size()).append(VERTICAL_SEPARATE);
        boolean isFirst = true;
        
        for (RtvLocationDetailExtendedHolder rtvLocDetailEx : currentLocDetailExs)
        {
            if (!isFirst) content.append(VERTICAL_SEPARATE);
            String rtvLocDetailExString = rtvLocDetailEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(rtvLocDetailExString);
            isFirst = false;
        }
        
        content.append(END_LINE);
    }

}
