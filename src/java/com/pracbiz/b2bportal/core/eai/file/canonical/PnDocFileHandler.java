package com.pracbiz.b2bportal.core.eai.file.canonical;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.PnStatus;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.PnDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PnHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class PnDocFileHandler extends DefaultDocFileHandler<PnDocMsg, PnHolder>
        implements CoreCommonConstants
{
    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }


    @Override
    public void readFileContent(PnDocMsg docMsg, byte[] input)
            throws Exception
    {
        // read source file and group according by record type
        PnHolder data = new PnHolder();
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        BigDecimal pnOid = docMsg.getDocOid();
        
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        PnHeaderHolder header = this.initHeader(headerContents,docMsg);
        data.setPnHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList = map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<PnHeaderExtendedHolder> headerExs= new ArrayList<PnHeaderExtendedHolder>();
            for (String[] headerExArray : headerExList)
            {
                PnHeaderExtendedHolder headerEx = this.initHeaderEx(headerExArray);
                headerEx.setPnOid(pnOid);
                headerExs.add(headerEx);
            }
            data.setHeaderExtendeds(headerExs);
        }
       
        
        //record type is 'DET'
        List<PnDetailHolder> details = new ArrayList<PnDetailHolder>();
        List<String[]> detailList = map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        for (String[] detailArray : detailList)
        {
            PnDetailHolder detail = this.initDetail(detailArray);
            detail.setPnOid(pnOid);
            details.add(detail);
        }
        data.setDetails(details);
        
        //record type is 'DEX'
        List<String[]> detailExList = map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
        
        if (detailExList != null && !detailExList.isEmpty())
        {
            List<PnDetailExtendedHolder> detailExtendeds = new ArrayList<PnDetailExtendedHolder>();
            for (String[] detailExArray : detailExList)
            {
                PnDetailExtendedHolder detailEx = this.initDetailEx(detailExArray);
                detailEx.setPnOid(pnOid);
                detailExtendeds.add(detailEx);
            }
            
            data.setDetailExtendeds(detailExtendeds);
        }
        
        docMsg.setData(data);
    }

    
    @Override
    public byte[] getFileByte(PnHolder data, File targetFile, String expectedFormat)
        throws Exception
    {
        StringBuffer content = new StringBuffer();
        PnHolder pn = data;
        
        //HDR
        PnHeaderHolder header = pn.getPnHeader();
        if (header != null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER).append(VERTICAL_SEPARATE);
            String header2tring = header.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(header2tring);
            content.append(END_LINE);
        }
        
        //HEX
        List<PnHeaderExtendedHolder> headerExs = pn.getHeaderExtendeds();
        if (headerExs != null && !headerExs.isEmpty())
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED).append(VERTICAL_SEPARATE);
            content.append(headerExs.size()).append(VERTICAL_SEPARATE);
            //field-name,field-type,field-value repeat
            boolean isFirst = true;
            for (PnHeaderExtendedHolder headerEx : headerExs)
            {
                if (!isFirst) content.append(VERTICAL_SEPARATE);
                String headerEx2String = headerEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(headerEx2String);
                isFirst = false;
            }
            content.append(END_LINE);
        }
        
        //DET
        List<PnDetailHolder> details = pn.getDetails();
        List<PnDetailExtendedHolder> detailExs = pn.getDetailExtendeds();
        Map<Integer,List<PnDetailExtendedHolder>> detailExMaps = this.groupByLineSeqNo(detailExs);
        if(details != null && !details.isEmpty())
        {
            for (PnDetailHolder detail : details)
            {
                content.append(FileParserUtil.RECORD_TYPE_DETAIL).append(VERTICAL_SEPARATE);
                content.append(header.getPnNo()).append(VERTICAL_SEPARATE);
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
    protected String translate(PnDocMsg docMsg)
            throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);
        
        return targetFile.getName();
    }
    

    @Override
    public String getTargetFilename(PnHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.PN + DOC_FILENAME_DELIMITOR
            + data.getPnHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getPnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getPnHeader().getPnNo()) + DOC_FILENAME_DELIMITOR + data.getPnHeader().getPnOid()
            + ".txt";
    }
    
    
    private PnHeaderHolder initHeader(String[] content, PnDocMsg docMsg) throws Exception
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        
        PnHeaderHolder header = new PnHeaderHolder();
        header.setPnNo(content[1]);
        header.setDocAction(content[2]);
        header.setActionDate(fileUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setPnDate(fileUtil.dateValueOf(content[4], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setBuyerCode(content[5]);
        header.setBuyerName(content[6]);
        header.setSupplierCode(content[7]);
        header.setSupplierName(content[8]);
        header.setSupplierAddr1(content[9]);
        header.setSupplierAddr2(content[10]);
        header.setSupplierAddr3(content[11]);
        header.setSupplierAddr4(content[12]);
        header.setSupplierCity(content[13]);
        header.setSupplierState(content[14]);
        header.setSupplierCtryCode(content[15]);
        header.setSupplierPostalCode(content[16]);
        header.setPayMethodCode(content[17]);
        header.setPayMethodDesc(content[18]);
        header.setBankCode(content[19]);
        header.setTotalAmount(fileUtil.decimalValueOf(content[20]));
        header.setDiscountAmount(fileUtil.decimalValueOf(content[21]));
        header.setNetTotalAmount(fileUtil.decimalValueOf(content[22]));

        header.setPnOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, header);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, header);
        header.setPnStatus(PnStatus.NEW);
        return header;
    }
    
    
    private PnHeaderExtendedHolder initHeaderEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        PnHeaderExtendedHolder headerEx = new PnHeaderExtendedHolder();
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
    
    
    private PnDetailHolder initDetail(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        PnDetailHolder detail = new PnDetailHolder();
        detail.setLineSeqNo(fileUtil.integerValueOf(content[2]));
        detail.setDocDate(fileUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        detail.setDocType(content[4]);
        detail.setDocRefNo(content[5]);
        detail.setPayTransNo(content[6]);
        detail.setPayRefNo(content[7]);
        detail.setGrossAmount(fileUtil.decimalValueOf(content[8]));
        detail.setDiscountAmount(fileUtil.decimalValueOf(content[9]));
        detail.setNetAmount(fileUtil.decimalValueOf(content[10]));
        return detail;
    }
    
    
    private PnDetailExtendedHolder initDetailEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        
        PnDetailExtendedHolder detailEx = new PnDetailExtendedHolder();
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
    
    
    private void appendDetailExs(StringBuffer content, Map<Integer, List<PnDetailExtendedHolder>> detailExMaps,Integer lineSeqNo)
    {
        if(detailExMaps == null || detailExMaps.isEmpty())
        {
            return;
        }
        
        List<PnDetailExtendedHolder> detailExs = detailExMaps.get(lineSeqNo);
        if (detailExs == null || detailExs.isEmpty()) return;
        
        content.append(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED).append(VERTICAL_SEPARATE);
        content.append(detailExs.size()).append(VERTICAL_SEPARATE);
        boolean isFirst = true;
        for(PnDetailExtendedHolder detailEx : detailExs)
        {
            if (!isFirst) content.append(VERTICAL_SEPARATE);
            String detailEx2String = detailEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(detailEx2String);
            isFirst = false;
        }
        content.append(END_LINE);
    }
    
    
    private Map<Integer, List<PnDetailExtendedHolder>> groupByLineSeqNo(List<PnDetailExtendedHolder> details)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        
        Map<Integer, List<PnDetailExtendedHolder>> map = new HashMap<Integer, List<PnDetailExtendedHolder>>();
        for (PnDetailExtendedHolder detail : details)
        {
            Integer lineSeqNo = detail.getLineSeqNo();
            if (map.containsKey(lineSeqNo))
            {
                List<PnDetailExtendedHolder> list = map.get(lineSeqNo);
                list.add(detail);
                continue;
            }
            List<PnDetailExtendedHolder> list = new ArrayList<PnDetailExtendedHolder>();
            list.add(detail);
            map.put(lineSeqNo, list);
        }
        
        return map;
    }

}
