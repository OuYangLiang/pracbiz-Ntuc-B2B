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
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.DnDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DnDocFileHandler extends DefaultDocFileHandler<DnDocMsg, DnHolder> implements
        CoreCommonConstants
{
    private final static Logger log = LoggerFactory.getLogger(DnDocFileHandler.class);
    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }

    @Override
    public void readFileContent(DnDocMsg docMsg, byte[] input)
            throws Exception
    {
        log.info(" :::: start to read conanical Dn  source file");
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        DnHolder dnHolder = new DnHolder();
        BigDecimal dnOid = docMsg.getDocOid();
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        DnHeaderHolder header = this.initDnHeader(headerContents,docMsg);
        dnHolder.setDnHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList= map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<DnHeaderExtendedHolder> headerExtendeds = new ArrayList<DnHeaderExtendedHolder>();
            for (String[] headerExContent : headerExList)
            {
                DnHeaderExtendedHolder dnExHolder = this.initDnHeaderExtended(headerExContent);
                dnExHolder.setDnOid(dnOid);
                headerExtendeds.add(dnExHolder);
            }
            dnHolder.setHeaderExtended(headerExtendeds);
        }
        
        //record type is 'DET'
        List<String[]> detailList= map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        List<DnDetailExHolder> details = new ArrayList<DnDetailExHolder>();
        for (String[] detailContent: detailList)
        {
            DnDetailExHolder detail = this.initDnDetail(detailContent);
            detail.setDnOid(dnOid);
            details.add(detail);
        }
        dnHolder.setDnDetail(details);
        
        //record type is 'DEX'
        List<String[]> detailExList= map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
        
        if (detailExList != null && !detailExList.isEmpty())
        {
            List<DnDetailExtendedHolder> detailExtendeds = new ArrayList<DnDetailExtendedHolder>();
            for (String[] detailExContent : detailExList)
            {
                DnDetailExtendedHolder detailEx = this.initDnDetailExtended(detailExContent);
                detailEx.setDnOid(dnOid);
                detailExtendeds.add(detailEx);
            }
            dnHolder.setDetailExtended(detailExtendeds);
        }
        
        
        docMsg.setData(dnHolder);
        
        log.info(" :::: end to read conanical Dn source file.");
        
    }

    @Override
    public byte[] getFileByte(DnHolder data, File targetFile, String expectedFormat)
            throws Exception
    {
        StringBuffer content = new StringBuffer();
        DnHolder dn = data;
        
        //HDR
        DnHeaderHolder header = dn.getDnHeader();
        if (header != null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER);
            String headerString = header.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(headerString);
            content.append(END_LINE);
        }

        //DET
        List<DnDetailExHolder> details = dn.getDnDetail();
        if(details != null && !details.isEmpty())
        {
            for (DnDetailExHolder detail : details)
            {
                content.append(FileParserUtil.RECORD_TYPE_DETAIL).append(VERTICAL_SEPARATE);
                content.append(header.getDnNo()).append(VERTICAL_SEPARATE);
                String detailString = detail.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(detailString);
                content.append("").append(VERTICAL_SEPARATE).append("");
                content.append(END_LINE);
            }
        }
        
        return content.toString().getBytes(Charset.defaultCharset());
    }

    @Override
    protected String translate(DnDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
                this.getTargetFilename(docMsg.getData(), CANONICAL));
        this.createFile(docMsg.getData(), targetFile, CANONICAL);

        return targetFile.getName();
    }

    @Override
    public String getTargetFilename(DnHolder data, String expectedFormat)
    {
        if (expectedFormat != null
                && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.DN.name() + DOC_FILENAME_DELIMITOR
                + data.getDnHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
                + data.getDnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(data.getDnHeader().getDnNo()) + DOC_FILENAME_DELIMITOR
                + data.getDnHeader().getDnOid() + CANONICAL_FILE_EXTEND;
    }

    private DnHeaderHolder initDnHeader(String[] content,DnDocMsg docMsg) throws Exception
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        
        DnHeaderHolder dnHeader = new DnHeaderHolder();
        dnHeader.setDnNo(content[1]);
        dnHeader.setDocAction(content[2]);
        dnHeader.setActionDate(fileParserUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        dnHeader.setDnType(content[4]);
        dnHeader.setDnTypeDesc(content[5]);
        dnHeader.setDnDate(fileParserUtil.dateValueOf(content[6], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        dnHeader.setPoNo(content[7]);
        dnHeader.setPoDate(fileParserUtil.dateValueOf(content[8], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        dnHeader.setInvNo(content[9]);
        dnHeader.setInvDate(fileParserUtil.dateValueOf(content[10], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        dnHeader.setRtvNo(content[11]);
        dnHeader.setRtvDate(fileParserUtil.dateValueOf(content[12], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        dnHeader.setGiNo(content[13]);
        dnHeader.setGiDate(fileParserUtil.dateValueOf(content[14], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        dnHeader.setBuyerCode(content[15]);
        dnHeader.setBuyerName(content[16]);
        dnHeader.setBuyerAddr1(content[17]);
        dnHeader.setBuyerAddr2(content[18]);
        dnHeader.setBuyerAddr3(content[19]);
        dnHeader.setBuyerAddr4(content[20]);
        dnHeader.setBuyerCity(content[21]);
        dnHeader.setBuyerState(content[22]);
        dnHeader.setBuyerCtryCode(content[23]);
        dnHeader.setBuyerPostalCode(content[24]);
        dnHeader.setSupplierCode(content[25]);
        dnHeader.setSupplierName(content[26]);
        dnHeader.setSupplierAddr1(content[27]);
        dnHeader.setSupplierAddr2(content[28]);
        dnHeader.setSupplierAddr3(content[29]);
        dnHeader.setSupplierAddr4(content[30]);
        dnHeader.setSupplierCity(content[31]);
        dnHeader.setSupplierState(content[32]);
        dnHeader.setSupplierCtryCode(content[33]);
        dnHeader.setSupplierPostalCode(content[34]);
        dnHeader.setStoreCode(content[35]);
        dnHeader.setStoreName(content[36]);
        dnHeader.setStoreAddr1(content[37]);
        dnHeader.setStoreAddr2(content[38]);
        dnHeader.setStoreAddr3(content[39]);
        dnHeader.setStoreAddr4(content[40]);
        dnHeader.setStoreCity(content[41]);
        dnHeader.setStoreState(content[42]);
        dnHeader.setStoreCtryCode(content[43]);
        dnHeader.setStorePostalCode(content[44]);
        dnHeader.setStoreCode(content[45]);
        dnHeader.setDeptCode(content[46]);
        dnHeader.setDeptName(content[47]);
        dnHeader.setSubDeptCode(content[48]);
        dnHeader.setSubDeptName(content[49]);
        dnHeader.setTotalCost(fileParserUtil.decimalValueOf(content[50]));
        dnHeader.setTotalVat(fileParserUtil.decimalValueOf(content[51]));
        dnHeader.setTotalCostWithVat(fileParserUtil.decimalValueOf(content[52]));
        dnHeader.setVatRate(fileParserUtil.decimalValueOf(content[53]));
        dnHeader.setReasonCode(content[54]);
        dnHeader.setReasonDesc(content[55]);
        dnHeader.setDnRemarks(content[56]);
        
        dnHeader.setDnOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, dnHeader);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, dnHeader);
        dnHeader.setDnStatus(DnStatus.NEW);
        return dnHeader;

    }
    
    private DnHeaderExtendedHolder initDnHeaderExtended(String[] content)
    {
        DnHeaderExtendedHolder dnExHolder = new DnHeaderExtendedHolder();
        dnExHolder.setFieldName(content[2]);
        dnExHolder.setFieldType(content[3]);

        //Boolean value
        if(content[3].equals(EXTENDED_TYPE_BOOLEAN))
        {
            dnExHolder.setBoolValue((content[4].equals("TRUE") ? Boolean.TRUE:Boolean.FALSE));
        }
        
        //Float value
        if(content[3].equals(EXTENDED_TYPE_FLOAT))
        {
            dnExHolder.setFloatValue(FileParserUtil.getInstance().decimalValueOf(content[4]));
        }

        //Integer value
        if(content[3].equals(EXTENDED_TYPE_INTEGER))
        {
            dnExHolder.setIntValue(Integer.parseInt(content[4]));
        }

        //String value
        if(content[3].equals(EXTENDED_TYPE_STRING))
        {
            dnExHolder.setStringValue(content[4]);
        }
        
        return dnExHolder;
    }
    
    
    private DnDetailExHolder initDnDetail(String[] content)
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        DnDetailExHolder dnDetail = new DnDetailExHolder();
        dnDetail.setLineSeqNo(fileParserUtil.integerValueOf(content[2]));
        dnDetail.setBuyerItemCode(content[3]);
        dnDetail.setSupplierItemCode(content[4]);
        dnDetail.setBarcode(content[5]);
        dnDetail.setItemDesc(content[6]);
        dnDetail.setBrand(content[7]);
        dnDetail.setColourCode(content[8]);
        dnDetail.setColourDesc(content[9]);
        dnDetail.setSizeCode(content[10]);
        dnDetail.setSizeDesc(content[11]);
        dnDetail.setPoNo(content[12]);
        dnDetail.setPoDate(fileParserUtil.dateValueOf(content[13], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        dnDetail.setInvNo(content[14]);
        dnDetail.setInvDate(fileParserUtil.dateValueOf(content[15], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        dnDetail.setPackingFactor(fileParserUtil.decimalValueOf(content[16]));
        dnDetail.setDebitBaseUnit(content[17]);
        dnDetail.setOrderUom(content[18]);
        dnDetail.setDebitQty(fileParserUtil.decimalValueOf(content[19]));
        dnDetail.setUnitCost(fileParserUtil.decimalValueOf(content[20]));
        dnDetail.setCostDiscountAmount(fileParserUtil.decimalValueOf(content[21]));
        dnDetail.setCostDiscountPercent(fileParserUtil.decimalValueOf(content[22]));
        dnDetail.setRetailDiscountAmount(fileParserUtil.decimalValueOf(content[23]));
        dnDetail.setNetUnitCost(fileParserUtil.decimalValueOf(content[24]));
        dnDetail.setItemCost(fileParserUtil.decimalValueOf(content[25]));
        dnDetail.setItemSharedCode(fileParserUtil.decimalValueOf(content[26]));
        dnDetail.setItemGrossCost(fileParserUtil.decimalValueOf(content[27]));
        dnDetail.setRetailPrice(fileParserUtil.decimalValueOf(content[28]));
        dnDetail.setItemRetailAmount(fileParserUtil.decimalValueOf(content[29]));
        dnDetail.setItemRemarks(content[30]);
        return dnDetail;
    }
    
    
    private DnDetailExtendedHolder initDnDetailExtended(String[] content)
    {
        DnDetailExtendedHolder detailEx = new DnDetailExtendedHolder();
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
