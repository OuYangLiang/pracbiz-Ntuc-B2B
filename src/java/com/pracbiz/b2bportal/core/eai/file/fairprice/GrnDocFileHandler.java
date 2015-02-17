package com.pracbiz.b2bportal.core.eai.file.fairprice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
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
 * @author liyong
 */
public class GrnDocFileHandler extends DefaultDocFileHandler<GrnDocMsg, GrnHolder>
        implements CoreCommonConstants
{
    @Autowired private PoDetailService poDetailService;
    @Autowired private PoHeaderService poHeaderService;
    
    @Override
    protected String processFormat()
    {
        return FAIRPRICE;
    }


    @Override
    public void readFileContent(GrnDocMsg docMsg, byte[] input)
            throws Exception
    {
        InputStream inputStream = null;

        try
        {
            GrnHolder data = new GrnHolder();
            inputStream = new ByteArrayInputStream(input);
            List<String> contents = FileParserUtil.getInstance().readLines(inputStream);
            BigDecimal grnOid = docMsg.getDocOid();
                
            BigDecimal totalReceiveQty = BigDecimal.ZERO;
            GrnHeaderHolder header = this.initHeader(contents.get(0),docMsg);
            
            List<GrnDetailHolder> details = new ArrayList<GrnDetailHolder>();
            PoHeaderHolder poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(docMsg.getSenderOid(), header.getPoNo(), docMsg.getReceiverCode());
           
            List<PoDetailHolder> poDetails = null;
            if (poHeader != null)
            {
                poDetails = poDetailService.selectPoDetailsByPoOid(poHeader.getPoOid());
                header.setPoDate(poHeader.getPoDate());
            }
            
            Map<String, PoDetailHolder> poDetailMap = groupDetailByBuyerItemCode(poDetails);
            
            for (String content : contents)
            {
                GrnDetailHolder detail = this.initDetail(content);
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
                
                totalReceiveQty = totalReceiveQty.add(detail.getReceiveQty());
                details.add(detail);
            }
            data.setDetails(details);
            
            header.setTotalReceivedQty(totalReceiveQty);
            data.setHeader(header);
            
            // oyl: To compute header total cost if it's null
            if (null == header.getTotalCost() || header.getTotalCost().compareTo(BigDecimal.ZERO) == 0)
            {
                this.computeTotalCost(header, details);
            }
            
            docMsg.setData(data);
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        
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
    
    
    private GrnHeaderHolder initHeader(String content, GrnDocMsg docMsg) throws Exception
    {
        GrnHeaderHolder grnHeader = new GrnHeaderHolder();
        grnHeader.setGrnNo(StringUtil.getInstance().convertDocNoWithNoLeading(
            content.substring(2, 12).trim(), LEADING_ZERO));
        grnHeader.setDocAction("A");
        grnHeader.setActionDate(new Date());
        grnHeader.setGrnDate(DateUtil.getInstance().convertStringToDate(content.substring(46, 54).trim(), DateUtil.DATE_FORMAT_YYYYMMDD));
        grnHeader.setPoNo(StringUtil.getInstance().convertDocNoWithNoLeading(
            content.substring(12, 22).trim(), LEADING_ZERO));
        grnHeader.setPoDate(DateUtil.getInstance().convertStringToDate(content.substring(30, 38).trim(), DateUtil.DATE_FORMAT_YYYYMMDD));
        grnHeader.setCreateDate(DateUtil.getInstance().convertStringToDate(content.substring(22, 30).trim(), DateUtil.DATE_FORMAT_YYYYMMDD));
        grnHeader.setBuyerCode(docMsg.getSenderCode());
        grnHeader.setBuyerName(docMsg.getSenderName());
        grnHeader.setSupplierCode(docMsg.getReceiverCode());
        grnHeader.setSupplierName(docMsg.getReceiverName());
        grnHeader.setReceiveStoreCode(content.substring(54, 58).trim());
        grnHeader.setReceiveStoreName(null);
        grnHeader.setTotalExpectedQty(null);
        grnHeader.setTotalReceivedQty(new BigDecimal(content.substring(216, 229).trim()));//**
        grnHeader.setItemCount(content.substring(233, content.length()).trim().isEmpty() ? null : new BigDecimal(content.substring(233, content.length()).trim()));
        grnHeader.setDiscountAmount(null);
        grnHeader.setNetCost(null);
        grnHeader.setTotalCost(null);
        grnHeader.setTotalRetailAmount(null);
        grnHeader.setGrnRemarks(null);

        
        grnHeader.setGrnOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, grnHeader);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, grnHeader);
        grnHeader.setGrnStatus(GrnStatus.NEW);
        return grnHeader;
    }
    
    
//    private GrnHeaderExtendedHolder initHeaderEx(String[] content)
//    {
//        return null;
//    }
    
    
    private GrnDetailHolder initDetail(String content)
    {
        
            GrnDetailHolder grnDetail = new GrnDetailHolder();
            grnDetail.setLineSeqNo(Integer.valueOf(content.substring(168, 172).trim()));
            grnDetail.setBuyerItemCode(StringUtil.getInstance().convertDocNoWithNoLeading(content.substring(172,190).trim(), LEADING_ZERO));
            grnDetail.setSupplierItemCode(null);
            grnDetail.setBarcode(content.substring(190, 208).trim());
            grnDetail.setItemDesc(null);
            grnDetail.setBrand(null);
            grnDetail.setColourCode(null);
            grnDetail.setColourDesc(null);
            grnDetail.setSizeCode(null);
            grnDetail.setSizeDesc(null);
            grnDetail.setPackingFactor(content.substring(211, 216).trim().isEmpty()? BigDecimal.ONE : new BigDecimal(content.substring(211, 216).trim()));
            grnDetail.setOrderBaseUnit("P");
            grnDetail.setOrderUom(content.substring(208, 211).trim());
            grnDetail.setOrderQty(new BigDecimal(content.substring(216, 229).trim()));
            grnDetail.setReceiveQty(new BigDecimal(content.substring(216, 229).trim()));
            grnDetail.setFocBaseUnit(null);
            grnDetail.setFocUom(null);
            grnDetail.setFocQty(null);
            grnDetail.setFocReceiveQty(null);
            grnDetail.setUnitCost(null);
            grnDetail.setItemCost(BigDecimal.ZERO);
            grnDetail.setRetailPrice(null);
            grnDetail.setItemRetailAmount(null);
            grnDetail.setItemRemarks(content.substring(68, 168).trim());
            grnDetail.setDeliveryQty(new BigDecimal(content.substring(216, 229).trim()));
            
        return grnDetail;
    }
    
    
//    private GrnDetailExtendedHolder initDetailEx(String[] content)
//    {
//        return null;
//    }
    
    
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
            totalCost = totalCost.add(detail.getItemCost() == null ? BigDecimal.ZERO : detail.getItemCost());
        }

        header.setTotalCost(totalCost);
    }
}
