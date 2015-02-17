package com.pracbiz.b2bportal.core.eai.file.canonical;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.InvType;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class InvDocFileHandler extends DefaultDocFileHandler<InvDocMsg, InvHolder>
    implements CoreCommonConstants
{

    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }

    @Override
    public void readFileContent(InvDocMsg docMsg, byte[] input)
        throws Exception
    {
        // read source file and group according by record type
        InvHolder invoice = new InvHolder();
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        BigDecimal invOid = docMsg.getDocOid();
        
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        InvHeaderHolder header = this.initInvHeader(headerContents,docMsg);
        invoice.setHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList = map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<InvHeaderExtendedHolder> headerExs = new ArrayList<InvHeaderExtendedHolder>();
            for (String[] headerExArray : headerExList)
            {
                InvHeaderExtendedHolder headerEx = this.initInvHeaderEx(headerExArray);
                headerEx.setInvOid(invOid);
                headerExs.add(headerEx);
            }
            invoice.setHeaderExtendeds(headerExs);
        }
       
        
        //record type is 'DET'
        List<InvDetailHolder> details = new ArrayList<InvDetailHolder>();
        List<String[]> detailList = map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        for (String[] detailArray : detailList)
        {
            InvDetailHolder detail = this.initInvDetail(detailArray);
            detail.setInvOid(invOid);
            details.add(detail);
        }
        invoice.setDetails(details);
        
        //record type is 'DEX'
        List<String[]> detailExList = map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
        
        if (detailExList != null && !detailExList.isEmpty())
        {
            List<InvDetailExtendedHolder> detailExtendeds = new ArrayList<InvDetailExtendedHolder>();
            for (String[] detailExArray : detailExList)
            {
                InvDetailExtendedHolder invDetailEx = this.initInvDetailEx(detailExArray);
                invDetailEx.setInvOid(invOid);
                detailExtendeds.add(invDetailEx);
            }
            invoice.setDetailExtendeds(detailExtendeds);
        }
       
        docMsg.setData(invoice);
    }

    
    public byte[] getFileByte(InvHolder data, File targetFile,
        String expectedFormat) throws Exception
    {
        StringBuffer content = new StringBuffer();
        InvHolder invoice = data;
        
        //HDR
        InvHeaderHolder header = invoice.getHeader();
        if (header != null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER).append(VERTICAL_SEPARATE);
            String header2tring = header.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(header2tring);
            content.append(END_LINE);
        }
        
        //HEX
        List<InvHeaderExtendedHolder> headerExs = invoice.getHeaderExtendeds();
        if (headerExs != null && !headerExs.isEmpty())
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
            content.append(VERTICAL_SEPARATE);
            content.append(headerExs.size()).append(VERTICAL_SEPARATE);
            boolean isFirst = true;
            for (InvHeaderExtendedHolder headerEx : headerExs)
            {
                if (!isFirst) content.append(VERTICAL_SEPARATE);
                String headerEx2String = headerEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(headerEx2String);
                isFirst = false;
            }
            content.append(END_LINE);
        }
        
        
        //DET
        List<InvDetailHolder> invDetails = invoice.getDetails();
        List<InvDetailExtendedHolder> detailExs = invoice.getDetailExtendeds();
        Map<Integer,List<InvDetailExtendedHolder>> detailExMaps = this.groupByLineSeqNo(detailExs);
        if (invDetails != null && !invDetails.isEmpty())
        {
            for (InvDetailHolder detail : invDetails)
            {
                content.append(FileParserUtil.RECORD_TYPE_DETAIL).append(VERTICAL_SEPARATE);
                content.append(header.getInvNo()).append(VERTICAL_SEPARATE);
                String detail2String = detail.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(detail2String);
                content.append(END_LINE);
                
                //DEX
                this.appendDetailExs(content, detailExMaps, detail.getLineSeqNo());
            }
        }
        
        return content.toString().getBytes(Charset.defaultCharset());
    }
    
    
    @Override
    protected String translate(InvDocMsg docMsg)
        throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);
        
        return targetFile.getName();
    }
    
    
    public String getTargetFilename(InvHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.INV + DOC_FILENAME_DELIMITOR
            + data.getHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getHeader().getInvNo()) + DOC_FILENAME_DELIMITOR + data.getHeader().getInvOid()
            + ".txt";
    }

    
    
    private InvHeaderHolder initInvHeader(String[] content, InvDocMsg docMsg) throws Exception
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        
        InvHeaderHolder header = new InvHeaderHolder();
        header.setInvNo(content[1]);
        header.setDocAction(content[2]);
        header.setActionDate(fileParserUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setInvType(InvType.valueOf(content[4]));
        header.setInvDate(fileParserUtil.dateValueOf(content[5], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setPoNo(content[6]);
        header.setPoDate(fileParserUtil.dateValueOf(content[7], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setDeliveryNo(content[8]);
        header.setDeliveryDate(fileParserUtil.dateValueOf(content[9], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
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
        header.setSupplierBizRegNo(content[30]);
        header.setSupplierVatRegNo(content[31]);

        header.setShipToCode(content[32]);
        header.setShipToName(content[33]);
        header.setShipToAddr1(content[34]);
        header.setShipToAddr2(content[35]);
        header.setShipToAddr3(content[36]);
        header.setShipToAddr4(content[37]);
        header.setShipToCity(content[38]);
        header.setShipToState(content[39]);
        header.setShipToCtryCode(content[40]);
        header.setShipToPostalCode(content[41]);

        header.setPayTermCode(content[42]);
        header.setPayTermDesc(content[43]);
        header.setPayInstruct(content[44]);
        header.setAdditionalDiscountAmount(fileParserUtil.decimalValueOf(content[45]));
        header.setAdditionalDiscountPercent(fileParserUtil.decimalValueOf(content[46]));
        header.setInvAmountNoVat(fileParserUtil.decimalValueOf(content[47]));
        header.setVatAmount(fileParserUtil.decimalValueOf(content[48]));
        header.setInvAmountWithVat(fileParserUtil.decimalValueOf(content[49]));
        header.setVatRate(fileParserUtil.decimalValueOf(content[50]));
        header.setInvRemarks(content[51]);
        header.setCashDiscountAmount(fileParserUtil.decimalValueOf(content[52]));
        header.setCashDiscountPercent(fileParserUtil.decimalValueOf(content[53]));
        
        
        header.setInvOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, header);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, header);
        
        return header;
    }
    
    
    private InvHeaderExtendedHolder initInvHeaderEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        InvHeaderExtendedHolder headerEx = new InvHeaderExtendedHolder();
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
    
    
    private InvDetailHolder initInvDetail(String[] content)
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        
        InvDetailHolder detail = new InvDetailHolder();
        detail.setLineSeqNo(fileParserUtil.integerValueOf(content[2]));
        detail.setBuyerItemCode(content[3]);
        detail.setSupplierItemCode(content[4]);
        detail.setBarcode(content[5]);
        detail.setItemDesc(content[6]);
        detail.setBrand(content[7]);
        detail.setColourCode(content[8]);
        detail.setColourDesc(content[9]);
        detail.setSizeCode(content[10]);

        detail.setSizeDesc(content[11]);
        detail.setPackingFactor(fileParserUtil.decimalValueOf(content[12]));
        detail.setInvBaseUnit(content[13]);
        detail.setInvUom(content[14]);
        detail.setInvQty(fileParserUtil.decimalValueOf(content[15]));
        detail.setFocBaseUnit(content[16]);
        detail.setFocUom(content[17]);
        detail.setFocQty(fileParserUtil.decimalValueOf(content[18]));
        detail.setUnitPrice(fileParserUtil.decimalValueOf(content[19]));
        detail.setDiscountAmount(fileParserUtil.decimalValueOf(content[20]));

        detail.setDiscountPercent(fileParserUtil.decimalValueOf(content[21]));
        detail.setNetPrice(fileParserUtil.decimalValueOf(content[22]));
        detail.setItemAmount(fileParserUtil.decimalValueOf(content[23]));
        detail.setNetAmount(fileParserUtil.decimalValueOf(content[24]));
        detail.setItemSharedCost(fileParserUtil.decimalValueOf(content[25]));
        detail.setItemGrossAmount(fileParserUtil.decimalValueOf(content[26]));
        detail.setItemRemarks(content[27]);
        
        return detail;
    }
    
    
    private InvDetailExtendedHolder initInvDetailEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        
        InvDetailExtendedHolder detailEx = new InvDetailExtendedHolder();
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
    
    
    private void appendDetailExs(StringBuffer content, Map<Integer, List<InvDetailExtendedHolder>> detailExMaps,Integer lineSeqNo)
    {
        if(detailExMaps == null || detailExMaps.isEmpty())
        {
            return;
        }
        
        List<InvDetailExtendedHolder> detailExs = detailExMaps.get(lineSeqNo);
        if (detailExs == null || detailExs.isEmpty()) return;
        
        content.append(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED).append(VERTICAL_SEPARATE);
        content.append(detailExs.size()).append(VERTICAL_SEPARATE);
        boolean isFirst = true;
        for(InvDetailExtendedHolder detailEx : detailExs)
        {
            if (!isFirst) content.append(VERTICAL_SEPARATE);
            String detailEx2String = detailEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(detailEx2String);
            isFirst = false;
        }
        content.append(END_LINE);
    }
    
    
    private Map<Integer, List<InvDetailExtendedHolder>> groupByLineSeqNo(List<InvDetailExtendedHolder> details)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        
        Map<Integer, List<InvDetailExtendedHolder>> map = new HashMap<Integer, List<InvDetailExtendedHolder>>();
        for (InvDetailExtendedHolder detail : details)
        {
            Integer lineSeqNo = detail.getLineSeqNo();
            if (map.containsKey(lineSeqNo))
            {
                List<InvDetailExtendedHolder> list = map.get(lineSeqNo);
                list.add(detail);
                continue;
            }
            List<InvDetailExtendedHolder> list = new ArrayList<InvDetailExtendedHolder>();
            list.add(detail);
            map.put(lineSeqNo, list);
        }
        
        return map;
    }
    
}
