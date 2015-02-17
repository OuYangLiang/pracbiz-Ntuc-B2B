package com.pracbiz.b2bportal.core.eai.file.canonical;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.GrnStatus;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.GrnDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.PoDetailService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class GrnDocFileHandler extends DefaultDocFileHandler<GrnDocMsg, GrnHolder>
        implements CoreCommonConstants
{
    @Autowired private PoDetailService poDetailService;
    @Autowired private PoHeaderService poHeaderService;
    
    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }


    @Override
    public void readFileContent(GrnDocMsg docMsg, byte[] input)
            throws Exception
    {
        // read source file and group according by record type
        GrnHolder data = new GrnHolder();
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        BigDecimal grnOid = docMsg.getDocOid();
            
        
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        GrnHeaderHolder header = this.initHeader(headerContents,docMsg);
        data.setHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList = map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<GrnHeaderExtendedHolder> headerExs= new ArrayList<GrnHeaderExtendedHolder>();
            for (String[] headerExArray : headerExList)
            {
                GrnHeaderExtendedHolder headerEx = this.initHeaderEx(headerExArray);
                headerEx.setGrnOid(grnOid);
                headerExs.add(headerEx);
            }
            data.setHeaderExtendeds(headerExs);
        }
        
        
        //record type is 'DET'
        List<GrnDetailHolder> details = new ArrayList<GrnDetailHolder>();
        List<String[]> detailList = map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        
        PoHeaderHolder poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(docMsg.getSenderOid(), header.getPoNo(), docMsg.getReceiverCode());
        List<PoDetailHolder> poDetails = null;
        if (poHeader != null)
        {
            poDetails = poDetailService.selectPoDetailsByPoOid(poHeader.getPoOid());
        }
        Map<String, PoDetailHolder> poDetailMap = groupDetailByBuyerItemCode(poDetails);
        
        for (String[] detailArray : detailList)
        {
            GrnDetailHolder detail = this.initDetail(detailArray);
            detail.setGrnOid(grnOid);
            if (detail.getUnitCost() == null || detail.getUnitCost().compareTo(BigDecimal.ZERO) == 0)
            {
                PoDetailHolder poDetail = poDetailMap.get(detail.getBuyerItemCode());
                if (poDetail != null)
                {
                    detail.setUnitCost(detail.getOrderBaseUnit().equals("P") ? poDetail.getPackCost() : poDetail.getUnitCost());
                    if (detail.getItemCost() == null || detail.getItemCost().compareTo(BigDecimal.ZERO) == 0)
                    {
                        detail.setItemCost(detail.getUnitCost().multiply(detail.getReceiveQty()));
                    }
                }
            }
            details.add(detail);
        }
        data.setDetails(details);
        
        //record type is 'DEX'
        List<String[]> detailExList = map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
        if (detailExList != null && !detailExList.isEmpty())
        {
            List<GrnDetailExtendedHolder> detailExtendeds = new ArrayList<GrnDetailExtendedHolder>();
            for (String[] detailExArray : detailExList)
            {
                GrnDetailExtendedHolder detailEx = this.initDetailEx(detailExArray);
                detailEx.setGrnOid(grnOid);
                detailExtendeds.add(detailEx);
            }
            
            data.setDetailExtendeds(detailExtendeds);
        }
        
        
        // oyl: To compute header total cost if it's null
        if (null == header.getTotalCost() || header.getTotalCost().compareTo(BigDecimal.ZERO) == 0)
        {
            this.computeTotalCost(header, details);
        }
        
        docMsg.setData(data);
    }
    
    
    @Override
    public byte[] getFileByte(GrnHolder data, File targetFile,
        String expectedFormat) throws Exception
    {
        StringBuffer content = new StringBuffer();
        GrnHolder grn = data;
        
        //HDR
        GrnHeaderHolder header = grn.getHeader();
        if (header != null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER).append(VERTICAL_SEPARATE);
            String header2tring = header.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(header2tring);
            content.append(END_LINE);
        }
        
        //HEX
        List<GrnHeaderExtendedHolder> headerExs = grn.getHeaderExtendeds();
        if (headerExs != null && !headerExs.isEmpty())
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED).append(VERTICAL_SEPARATE);
            content.append(headerExs.size()).append(VERTICAL_SEPARATE);
            //field-name,field-type,field-value repeat
            boolean isFirst = true;
            for (GrnHeaderExtendedHolder headerEx : headerExs)
            {
                if (!isFirst) content.append(VERTICAL_SEPARATE);
                String headerEx2String = headerEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(headerEx2String);
                isFirst = false;
            }
            content.append(END_LINE);
        }

        //DET
        List<GrnDetailHolder> details = grn.getDetails();
        List<GrnDetailExtendedHolder> detailExs = grn.getDetailExtendeds();
        Map<Integer, List<GrnDetailExtendedHolder>> detailExToMaps = this.groupByLineSeqNo(detailExs);
        if(details != null && !details.isEmpty())
        {
            for (GrnDetailHolder detail : details)
            {
                content.append(FileParserUtil.RECORD_TYPE_DETAIL).append(VERTICAL_SEPARATE);
                content.append(header.getGrnNo()).append(VERTICAL_SEPARATE);
                String detail2String = detail.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(detail2String);
                content.append(END_LINE);
                
                //DEX
                this.appendDetailExs(content, detailExToMaps, detail.getLineSeqNo());
            }
        }
        
        return content.toString().getBytes(Charset.defaultCharset());
    }
    

    @Override
    protected String translate(GrnDocMsg docMsg)
            throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);
        
        return targetFile.getName();
    }
    

    @Override
    public String getTargetFilename(GrnHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.GRN + DOC_FILENAME_DELIMITOR
            + data.getHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getHeader().getGrnNo()) + DOC_FILENAME_DELIMITOR + data.getHeader().getGrnOid()
            + ".txt";
    }
    
    
    private GrnHeaderHolder initHeader(String[] content, GrnDocMsg docMsg) throws Exception
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        
        GrnHeaderHolder grnHeader = new GrnHeaderHolder();
        grnHeader.setGrnNo(content[1]);
        grnHeader.setDocAction(content[2]);
        grnHeader.setActionDate(fileUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        grnHeader.setGrnDate(fileUtil.dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        grnHeader.setPoNo(content[5]);
        grnHeader.setPoDate(fileUtil.dateValueOf(content[6], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        grnHeader.setCreateDate(fileUtil.dateValueOf(content[7], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        grnHeader.setBuyerCode(content[8]);
        grnHeader.setBuyerName(content[9]);
        grnHeader.setSupplierCode(content[10]);
        grnHeader.setSupplierName(content[11]);
        grnHeader.setReceiveStoreCode(content[12]);
        grnHeader.setReceiveStoreName(content[13]);
        grnHeader.setTotalExpectedQty(fileUtil.decimalValueOf(content[14]));
        grnHeader.setTotalReceivedQty(fileUtil.decimalValueOf(content[15]));
        grnHeader.setItemCount(fileUtil.decimalValueOf(content[16]));
        grnHeader.setDiscountAmount(fileUtil.decimalValueOf(content[17]));
        grnHeader.setNetCost(fileUtil.decimalValueOf(content[18]));
        grnHeader.setTotalCost(fileUtil.decimalValueOf(content[19]));
        grnHeader.setTotalRetailAmount(fileUtil.decimalValueOf(content[20]));
        grnHeader.setGrnRemarks(content[21]);

        
        grnHeader.setGrnOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, grnHeader);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, grnHeader);
        grnHeader.setGrnStatus(GrnStatus.NEW);
        return grnHeader;
    }
    
    
    private GrnHeaderExtendedHolder initHeaderEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        GrnHeaderExtendedHolder headerEx = new GrnHeaderExtendedHolder();
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
    
    
    private GrnDetailHolder initDetail(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        GrnDetailHolder grnDetail = new GrnDetailHolder();
        grnDetail.setLineSeqNo(fileUtil.integerValueOf(content[2]));
        grnDetail.setBuyerItemCode(content[3]);
        grnDetail.setSupplierItemCode(content[4]);
        grnDetail.setBarcode(content[5]);
        grnDetail.setItemDesc(content[6]);
        grnDetail.setBrand(content[7]);
        grnDetail.setColourCode(content[8]);
        grnDetail.setColourDesc(content[9]);
        grnDetail.setSizeCode(content[10]);
        grnDetail.setSizeDesc(content[11]);
        grnDetail.setPackingFactor(fileUtil.decimalValueOf(content[12]));
        grnDetail.setOrderBaseUnit(content[13]);
        grnDetail.setOrderUom(content[14]);
        grnDetail.setOrderQty(fileUtil.decimalValueOf(content[15]));
        grnDetail.setReceiveQty(fileUtil.decimalValueOf(content[16]));
        grnDetail.setFocBaseUnit(content[17]);
        grnDetail.setFocUom(content[18]);
        grnDetail.setFocQty(fileUtil.decimalValueOf(content[19]));
        grnDetail.setFocReceiveQty(fileUtil.decimalValueOf(content[20]));
        grnDetail.setUnitCost(fileUtil.decimalValueOf(content[21]));
        grnDetail.setItemCost(fileUtil.decimalValueOf(content[22]));
        grnDetail.setRetailPrice(fileUtil.decimalValueOf(content[23]));
        grnDetail.setItemRetailAmount(fileUtil.decimalValueOf(content[24]));
        grnDetail.setItemRemarks(content[25]);
        grnDetail.setDeliveryQty(fileUtil.decimalValueOf(content[16]));
        
        return grnDetail;
    }
    
    
    private GrnDetailExtendedHolder initDetailEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        
        GrnDetailExtendedHolder detailEx = new GrnDetailExtendedHolder();
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
    
    
    private void appendDetailExs(StringBuffer content, Map<Integer, List<GrnDetailExtendedHolder>> detailExMaps,Integer lineSeqNo)
    {
        if(detailExMaps == null || detailExMaps.isEmpty())
        {
            return;
        }
        
        List<GrnDetailExtendedHolder> detailExs = detailExMaps.get(lineSeqNo);
        if (detailExs == null || detailExs.isEmpty()) return;
        
        content.append(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED).append(VERTICAL_SEPARATE);
        content.append(detailExs.size()).append(VERTICAL_SEPARATE);
        boolean isFirst = true;
        for(GrnDetailExtendedHolder detailEx : detailExs)
        {
            if (!isFirst) content.append(VERTICAL_SEPARATE);
            String detailEx2String = detailEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(detailEx2String);
            isFirst = false;
        }
        content.append(END_LINE);
    }
    
    
    private Map<Integer, List<GrnDetailExtendedHolder>> groupByLineSeqNo(List<GrnDetailExtendedHolder> details)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        
        Map<Integer, List<GrnDetailExtendedHolder>> map = new HashMap<Integer, List<GrnDetailExtendedHolder>>();
        for (GrnDetailExtendedHolder detail : details)
        {
            Integer lineSeqNo = detail.getLineSeqNo();
            if (map.containsKey(lineSeqNo))
            {
                List<GrnDetailExtendedHolder> list = map.get(lineSeqNo);
                list.add(detail);
                continue;
            }
            List<GrnDetailExtendedHolder> list = new ArrayList<GrnDetailExtendedHolder>();
            list.add(detail);
            map.put(lineSeqNo, list);
        }
        
        return map;
    }
    
    
    private Map<String, PoDetailHolder> groupDetailByBuyerItemCode(List<PoDetailHolder> poDetails)
    {
        Map<String, PoDetailHolder> map = new HashMap<String, PoDetailHolder>();
        if (poDetails != null && !poDetails.isEmpty())
        {
            for (PoDetailHolder detail : poDetails)
            {
                map.put(detail.getBuyerItemCode(), detail);
            }
        }
        return map;
    }
    
    
    private void computeTotalCost(GrnHeaderHolder header,
        List<GrnDetailHolder> details)
    {
        BigDecimal totalCost = BigDecimal.ZERO;

        for(GrnDetailHolder detail : details)
        {
            totalCost = totalCost.add(detail.getItemCost());
        }

        header.setTotalCost(totalCost);
    }
}
