package com.pracbiz.b2bportal.core.eai.file.canonical;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.constants.CnType;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.CnDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.CnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CnDetailHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class CnDocFileHandler extends DefaultDocFileHandler<CnDocMsg, CnHolder> implements
        CoreCommonConstants
{
    private final static Logger log = LoggerFactory.getLogger(CnDocFileHandler.class);
    
    @Override
    public byte[] getFileByte(CnHolder data, File targetFile, String expectedFormat)
            throws Exception
    {
        StringBuffer content = new StringBuffer();
        CnHolder cn = data;
        
        //HDR
        CnHeaderHolder header = cn.getHeader();
        if (header != null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER);
            String headerString = header.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(headerString);
            content.append(END_LINE);
        }

        //DET
        List<CnDetailHolder> details = cn.getDetailList();
        if(details != null && !details.isEmpty())
        {
            for (CnDetailHolder detail : details)
            {
                content.append(FileParserUtil.RECORD_TYPE_DETAIL).append(VERTICAL_SEPARATE);
                content.append(header.getCnNo()).append(VERTICAL_SEPARATE);
                String detailString = detail.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(detailString);
                content.append("").append(VERTICAL_SEPARATE).append("");
                content.append(END_LINE);
            }
        }
        
        return content.toString().getBytes(Charset.defaultCharset());
        
    }

    @Override
    public String getTargetFilename(CnHolder data, String expectedFormat)
    {
        if (expectedFormat != null
                && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.CN.name() + DOC_FILENAME_DELIMITOR
                + data.getHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
                + data.getHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(data.getHeader().getCnNo()) + DOC_FILENAME_DELIMITOR
                + data.getHeader().getCnOid() + CANONICAL_FILE_EXTEND;
    }

    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }

    @Override
    public void readFileContent(CnDocMsg docMsg, byte[] input) throws Exception
    {
        log.info(" :::: start to read conanical Cn  source file");
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        CnHolder cnHolder = new CnHolder();
        BigDecimal cnOid = docMsg.getDocOid();
        
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        CnHeaderHolder header = this.initCnHeader(headerContents,docMsg);
        cnHolder.setHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList= map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<CnHeaderExtendedHolder> headerExtendeds = new ArrayList<CnHeaderExtendedHolder>();
            for (String[] headerExContent : headerExList)
            {
                CnHeaderExtendedHolder cnExHolder = this.initCnHeaderExtended(headerExContent);
                cnExHolder.setCnOid(cnOid);
                headerExtendeds.add(cnExHolder);
            }
            cnHolder.setHeaderExtendedList(headerExtendeds);
        }
        
        //record type is 'DET'
        List<String[]> detailList= map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        List<CnDetailHolder> details = new ArrayList<CnDetailHolder>();
        for (String[] detailContent: detailList)
        {
            CnDetailHolder detail = this.initCnDetail(detailContent);
            detail.setCnOid(cnOid);
            details.add(detail);
        }
        cnHolder.setDetailList(details);
        
        //record type is 'DEX'
        List<String[]> detailExList= map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
        
        if (detailExList != null && !detailExList.isEmpty())
        {
            List<CnDetailExtendedHolder> detailExtendeds = new ArrayList<CnDetailExtendedHolder>();
            for (String[] detailExContent : detailExList)
            {
                CnDetailExtendedHolder detailEx = this.initCnDetailExtended(detailExContent);
                detailEx.setCnOid(cnOid);
                detailExtendeds.add(detailEx);
            }
            cnHolder.setDetailExtendedList(detailExtendeds);
        }
        
        
        docMsg.setData(cnHolder);
        log.info(" :::: end to read conanical Cn source file.");
    }

    @Override
    protected String translate(CnDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
                this.getTargetFilename(docMsg.getData(), CANONICAL));
        this.createFile(docMsg.getData(), targetFile, CANONICAL);

        return targetFile.getName();
    }
    
    private CnHeaderHolder initCnHeader(String[] content,CnDocMsg docMsg) throws Exception
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        
        CnHeaderHolder cnHeader = new CnHeaderHolder();
        cnHeader.setCnNo(content[1]);
        cnHeader.setDocAction(content[2]);
        cnHeader.setActionDate(fileParserUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnHeader.setCnType(CnType.valueOf(content[4]));
        cnHeader.setCnTypeDesc(content[5]);
        cnHeader.setCnDate(fileParserUtil.dateValueOf(content[6], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnHeader.setPoNo(content[7]);
        cnHeader.setPoDate(fileParserUtil.dateValueOf(content[8], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnHeader.setInvNo(content[9]);
        cnHeader.setInvDate(fileParserUtil.dateValueOf(content[10], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnHeader.setRtvNo(content[11]);
        cnHeader.setRtvDate(fileParserUtil.dateValueOf(content[12], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnHeader.setGiNo(content[13]);
        cnHeader.setGiDate(fileParserUtil.dateValueOf(content[14], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnHeader.setBuyerCode(content[15]);
        cnHeader.setBuyerName(content[16]);
        cnHeader.setBuyerAddr1(content[17]);
        cnHeader.setBuyerAddr2(content[18]);
        cnHeader.setBuyerAddr3(content[19]);
        cnHeader.setBuyerAddr4(content[20]);
        cnHeader.setBuyerCity(content[21]);
        cnHeader.setBuyerState(content[22]);
        cnHeader.setBuyerCtryCode(content[23]);
        cnHeader.setBuyerPostalCode(content[24]);
        cnHeader.setSupplierCode(content[25]);
        cnHeader.setSupplierName(content[26]);
        cnHeader.setSupplierAddr1(content[27]);
        cnHeader.setSupplierAddr2(content[28]);
        cnHeader.setSupplierAddr3(content[29]);
        cnHeader.setSupplierAddr4(content[30]);
        cnHeader.setSupplierCity(content[31]);
        cnHeader.setSupplierState(content[32]);
        cnHeader.setSupplierCtryCode(content[33]);
        cnHeader.setSupplierPostalCode(content[34]);
        cnHeader.setStoreCode(content[35]);
        cnHeader.setStoreName(content[36]);
        cnHeader.setStoreAddr1(content[37]);
        cnHeader.setStoreAddr2(content[38]);
        cnHeader.setStoreAddr3(content[39]);
        cnHeader.setStoreAddr4(content[40]);
        cnHeader.setStoreCity(content[41]);
        cnHeader.setStoreState(content[42]);
        cnHeader.setStoreCtryCode(content[43]);
        cnHeader.setStorePostalCode(content[44]);
        cnHeader.setDeptCode(content[45]);
        cnHeader.setDeptName(content[46]);
        cnHeader.setSubDeptCode(content[47]);
        cnHeader.setSubDeptName(content[48]);
        cnHeader.setTotalCost(fileParserUtil.decimalValueOf(content[49]));
        cnHeader.setTotalVat(fileParserUtil.decimalValueOf(content[50]));
        cnHeader.setTotalCostWithVat(fileParserUtil.decimalValueOf(content[51]));
        cnHeader.setVatRate(fileParserUtil.decimalValueOf(content[52]));
        cnHeader.setReasonCode(content[53]);
        cnHeader.setReasonDesc(content[54]);
        cnHeader.setCnRemarks(content[55]);
        
        cnHeader.setCnOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, cnHeader);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, cnHeader);
        cnHeader.setCtrlStatus(CnStatus.SUBMIT);
        return cnHeader;

    }
    
    private CnHeaderExtendedHolder initCnHeaderExtended(String[] content)
    {
        CnHeaderExtendedHolder cnExHolder = new CnHeaderExtendedHolder();
        cnExHolder.setFieldName(content[2]);
        cnExHolder.setFieldType(content[3]);

        //Boolean value
        if(content[3].equals(EXTENDED_TYPE_BOOLEAN))
        {
            cnExHolder.setBoolValue((content[4].equals("TRUE") ? Boolean.TRUE:Boolean.FALSE));
        }
        
        //Float value
        if(content[3].equals(EXTENDED_TYPE_FLOAT))
        {
            cnExHolder.setFloatValue(FileParserUtil.getInstance().decimalValueOf(content[4]));
        }

        //Integer value
        if(content[3].equals(EXTENDED_TYPE_INTEGER))
        {
            cnExHolder.setIntValue(Integer.parseInt(content[4]));
        }

        //String value
        if(content[3].equals(EXTENDED_TYPE_STRING))
        {
            cnExHolder.setStringValue(content[4]);
        }
        
        return cnExHolder;
    }
    
    private CnDetailHolder initCnDetail(String[] content)
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        CnDetailHolder cnDetail = new CnDetailHolder();
        cnDetail.setLineSeqNo(fileParserUtil.integerValueOf(content[2]));
        cnDetail.setBuyerItemCode(content[3]);
        cnDetail.setSupplierItemCode(content[4]);
        cnDetail.setBarcode(content[5]);
        cnDetail.setItemDesc(content[6]);
        cnDetail.setBrand(content[7]);
        cnDetail.setColourCode(content[8]);
        cnDetail.setColourDesc(content[9]);
        cnDetail.setSizeCode(content[10]);
        cnDetail.setSizeDesc(content[11]);
        cnDetail.setPoNo(content[12]);
        cnDetail.setPoDate(fileParserUtil.dateValueOf(content[13], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnDetail.setInvNo(content[14]);
        cnDetail.setInvDate(fileParserUtil.dateValueOf(content[15], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnDetail.setRtvNo(content[16]);
        cnDetail.setRtvDate(fileParserUtil.dateValueOf(content[17], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnDetail.setGiNo(content[18]);
        cnDetail.setGiDate(fileParserUtil.dateValueOf(content[19], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        cnDetail.setPackingFactor(fileParserUtil.decimalValueOf(content[20]));
        cnDetail.setCreditBaseUnit(content[21]);
        cnDetail.setCreditUom(content[22]);
        cnDetail.setCreditQty(fileParserUtil.decimalValueOf(content[23]));
        cnDetail.setUnitCost(fileParserUtil.decimalValueOf(content[24]));
        cnDetail.setCostDiscountAmount(fileParserUtil.decimalValueOf(content[25]));
        cnDetail.setCostDiscountPercent(fileParserUtil.decimalValueOf(content[26]));
        cnDetail.setRetailDiscountAmount(fileParserUtil.decimalValueOf(content[27]));
        cnDetail.setNetUnitCost(fileParserUtil.decimalValueOf(content[28]));
        cnDetail.setItemCost(fileParserUtil.decimalValueOf(content[29]));
        cnDetail.setItemSharedCost(fileParserUtil.decimalValueOf(content[30]));
        cnDetail.setItemGrossCost(fileParserUtil.decimalValueOf(content[31]));
        cnDetail.setRetailPrice(fileParserUtil.decimalValueOf(content[32]));
        cnDetail.setItemRetailAmount(fileParserUtil.decimalValueOf(content[33]));
        cnDetail.setReasonCode(content[34]);
        cnDetail.setReasonDesc(content[35]);
        return cnDetail;
    }
    
    private CnDetailExtendedHolder initCnDetailExtended(String[] content)
    {
        CnDetailExtendedHolder detailEx = new CnDetailExtendedHolder();
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
        
        return detailEx;
    }
}
