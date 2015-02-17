//*****************************************************************************
//
// File Name       :  CcDocFileHandler.java
// Date Created    :  Dec 24, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 24, 2013 9:39:19 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

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
import com.pracbiz.b2bportal.core.eai.message.outbound.CcDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.CcDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcDetailHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CcHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;


/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CcDocFileHandler extends DefaultDocFileHandler<CcDocMsg, CcHolder>
    implements CoreCommonConstants
{
    @Override
    public byte[] getFileByte(CcHolder data, File targetFile, String expectedFormat)
        throws Exception
    {
        StringBuffer content = new StringBuffer();
        CcHolder creditClaim = data;
        
        //HDR
        CcHeaderHolder header = creditClaim.getCcHeader();
        if (header != null)
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER).append(VERTICAL_SEPARATE);
            String header2tring = header.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(header2tring);
            content.append(END_LINE);
        }
        
        //HEX
        List<CcHeaderExtendedHolder> headerExs = creditClaim.getHeaderExtendeds();
        if (headerExs != null && !headerExs.isEmpty())
        {
            content.append(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
            content.append(VERTICAL_SEPARATE);
            content.append(headerExs.size()).append(VERTICAL_SEPARATE);
            boolean isFirst = true;
            for (CcHeaderExtendedHolder headerEx : headerExs)
            {
                if (!isFirst) content.append(VERTICAL_SEPARATE);
                String headerEx2String = headerEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
                content.append(headerEx2String);
            }
            content.append(END_LINE);
        }
        
        
        //DET
        List<CcDetailHolder> details = creditClaim.getDetails();
        List<CcDetailExtendedHolder> detailExs = creditClaim.getDetailExtendeds();
        Map<Integer,List<CcDetailExtendedHolder>> detailExMaps = this.groupByLineSeqNo(detailExs);
        if (details != null && !details.isEmpty())
        {
            for (CcDetailHolder detail : details)
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
    public String getTargetFilename(CcHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }

        return MsgType.CC
            + DOC_FILENAME_DELIMITOR
            + data.getCcHeader().getBuyerCode()
            + DOC_FILENAME_DELIMITOR
            + data.getCcHeader().getSupplierCode()
            + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(
                data.getCcHeader().getInvNo()) 
            + DOC_FILENAME_DELIMITOR
            + data.getCcHeader().getInvOid() + ".txt";
    }


    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }


    @Override
    public void readFileContent(CcDocMsg docMsg, byte[] input)
        throws Exception
    {

        // read source file and group according by record type
        CcHolder creditClaim = new CcHolder();
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(input);
        BigDecimal invOid = docMsg.getDocOid();
        
        //record type is 'HDR'
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String[] headerContents = headerList.get(0);
        CcHeaderHolder header = this.initCcHeader(headerContents,docMsg);
        creditClaim.setCcHeader(header);
        
        //record type is 'HEX'
        List<String[]> headerExList = map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
        
        if (headerExList != null && !headerExList.isEmpty())
        {
            List<CcHeaderExtendedHolder> headerExs = new ArrayList<CcHeaderExtendedHolder>();
            for (String[] headerExArray : headerExList)
            {
                CcHeaderExtendedHolder headerEx = this.initCcHeaderEx(headerExArray);
                headerEx.setInvOid(invOid);
                headerExs.add(headerEx);
            }
            creditClaim.setHeaderExtendeds(headerExs);
        }
       
        
        //record type is 'DET'
        List<CcDetailHolder> details = new ArrayList<CcDetailHolder>();
        List<String[]> detailList = map.get(FileParserUtil.RECORD_TYPE_DETAIL);
        for (String[] detailArray : detailList)
        {
            CcDetailHolder detail = this.initCcDetail(detailArray);
            detail.setInvOid(invOid);
            details.add(detail);
        }
        creditClaim.setDetails(details);
        
        //record type is 'DEX'
        List<String[]> detailExList = map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
        
        if (detailExList != null && !detailExList.isEmpty())
        {
            List<CcDetailExtendedHolder> detailExtendeds = new ArrayList<CcDetailExtendedHolder>();
            for (String[] detailExArray : detailExList)
            {
                CcDetailExtendedHolder ccDetailEx = this.initCcDetailEx(detailExArray);
                ccDetailEx.setInvOid(invOid);
                detailExtendeds.add(ccDetailEx);
            }
            creditClaim.setDetailExtendeds(detailExtendeds);
        }
       
        docMsg.setData(creditClaim);
    
    }


    @Override
    protected String translate(CcDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), CANONICAL));
        this.createFile(docMsg.getData(), targetFile, CANONICAL);
        
        return targetFile.getName();
    
    }
    
    
    private CcHeaderHolder initCcHeader(String[] content, CcDocMsg docMsg) throws Exception
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        BuyerHolder buyer = docMsg.getBuyer();
        CcHeaderHolder header = new CcHeaderHolder();
        header.setInvNo(content[1]);
        header.setDocAction(content[2]);
        header.setActionDate(fileParserUtil.dateValueOf(content[3], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setInvType(InvType.valueOf(content[4]));
        header.setInvDate(fileParserUtil.dateValueOf(content[5], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setPoNo(content[6]);
        header.setPoDate(fileParserUtil.dateValueOf(content[7], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setDeliveryDate(fileParserUtil.dateValueOf(content[8], CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
        header.setBuyerCode(content[9]);
        header.setBuyerName(content[10]);

        header.setBuyerAddr1(content[11]);
        header.setBuyerAddr2(content[12]);
        header.setBuyerAddr3(content[13]);
        header.setBuyerAddr4(content[14]);
        header.setBuyerCity(content[15]);
        header.setBuyerState(content[16]);
        header.setBuyerCtryCode(content[17]);
        header.setBuyerPostalCode(content[18]);
        header.setSupplierCode(content[19]);
        header.setSupplierName(content[20]);

        header.setSupplierAddr1(content[21]);
        header.setSupplierAddr2(content[22]);
        header.setSupplierAddr3(content[23]);
        header.setSupplierAddr4(content[24]);
        header.setSupplierCity(content[25]);
        header.setSupplierState(content[26]);
        header.setSupplierCtryCode(content[27]);
        header.setSupplierPostalCode(content[28]);
        header.setBuyerBizRegNo(content[29]);
        header.setBuyerVatRegNo(content[30]);
        
        header.setStoreCode(content[31]);
        header.setStoreName(content[32]);
        header.setStoreAddr1(content[33]);
        header.setStoreAddr2(content[34]);
        header.setStoreAddr3(content[35]);
        header.setStoreAddr4(content[36]);
        header.setStoreCity(content[37]);
        header.setStoreState(content[38]);
        header.setStoreCtryCode(content[39]);
        header.setStorePostalCode(content[40]);

        header.setPayTermCode(content[41]);
        header.setPayTermDesc(content[42]);
        header.setPayInstruct(content[43]);
        header.setAdditionalDiscountAmount(fileParserUtil.decimalValueOf(content[44]) == null ? BigDecimal.ZERO : fileParserUtil.decimalValueOf(content[44]));
        header.setAdditionalDiscountPercent(fileParserUtil.decimalValueOf(content[45]));
        header.setInvAmountNoVat(fileParserUtil.decimalValueOf(content[46]));
        
        BigDecimal vatRate = buyer.getGstPercent() == null ? BigDecimal.ZERO : buyer.getGstPercent();
        header
            .setVatAmount(fileParserUtil.decimalValueOf(content[47]) == null ? header
                .getInvAmountNoVat().multiply(
                    vatRate.divide(BigDecimal.valueOf(100))) : fileParserUtil
                .decimalValueOf(content[47]));
        
        header
            .setInvAmountWithVat(fileParserUtil.decimalValueOf(content[48]) == null ? header
                .getInvAmountNoVat().add(header.getVatAmount())
                : fileParserUtil.decimalValueOf(content[48]));
        
        header
            .setVatRate(fileParserUtil.decimalValueOf(content[49]) == null ? vatRate
                : fileParserUtil.decimalValueOf(content[49]));
        
        header.setInvRemarks(content[50]);
        
        
        header.setInvOid(docMsg.getDocOid());
        
        this.initBuyerInfo(buyer, header);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, header);
        
        return header;
    }
    
    
    private CcHeaderExtendedHolder initCcHeaderEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        CcHeaderExtendedHolder headerEx = new CcHeaderExtendedHolder();
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
    
    
    private CcDetailHolder initCcDetail(String[] content)
    {
        FileParserUtil fileParserUtil = FileParserUtil.getInstance();
        
        CcDetailHolder detail = new CcDetailHolder();
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
        detail.setDiscountAmount(fileParserUtil.decimalValueOf(content[20]) == null? BigDecimal.ZERO : fileParserUtil.decimalValueOf(content[20]));

        detail.setDiscountPercent(fileParserUtil.decimalValueOf(content[21]));
        detail.setNetPrice(fileParserUtil.decimalValueOf(content[22]));
        detail.setItemAmount(fileParserUtil.decimalValueOf(content[23]));
        detail.setNetAmount(fileParserUtil.decimalValueOf(content[24]) == null ? detail.getItemAmount().subtract(detail.getDiscountAmount()) : fileParserUtil.decimalValueOf(content[24]));
        detail.setItemSharedCost(fileParserUtil.decimalValueOf(content[25]));
        detail.setItemGrossAmount(fileParserUtil.decimalValueOf(content[26]));
        detail.setItemRemarks(content[27]);
        
        return detail;
    }
    
    
    private CcDetailExtendedHolder initCcDetailEx(String[] content)
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        
        CcDetailExtendedHolder detailEx = new CcDetailExtendedHolder();
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
    
    
    private void appendDetailExs(StringBuffer content, Map<Integer, List<CcDetailExtendedHolder>> detailExMaps,Integer lineSeqNo)
    {
        if(detailExMaps == null || detailExMaps.isEmpty())
        {
            return;
        }
        
        List<CcDetailExtendedHolder> detailExs = detailExMaps.get(lineSeqNo);
        if (detailExs == null || detailExs.isEmpty()) return;
        
        content.append(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED).append(VERTICAL_SEPARATE);
        content.append(detailExs.size()).append(VERTICAL_SEPARATE);
        boolean isFirst = true;
        for(CcDetailExtendedHolder detailEx : detailExs)
        {
            if (!isFirst) content.append(VERTICAL_SEPARATE);
            String detailEx2String = detailEx.toStringWithDelimiterCharacter(VERTICAL_SEPARATE);
            content.append(detailEx2String);
            isFirst = false;
        }
        content.append(END_LINE);
    }
    
    
    private Map<Integer, List<CcDetailExtendedHolder>> groupByLineSeqNo(List<CcDetailExtendedHolder> details)
    {
        if (details == null || details.isEmpty())
        {
            return null;
        }
        
        Map<Integer, List<CcDetailExtendedHolder>> map = new HashMap<Integer, List<CcDetailExtendedHolder>>();
        for (CcDetailExtendedHolder detail : details)
        {
            Integer lineSeqNo = detail.getLineSeqNo();
            if (map.containsKey(lineSeqNo))
            {
                List<CcDetailExtendedHolder> list = map.get(lineSeqNo);
                list.add(detail);
                continue;
            }
            List<CcDetailExtendedHolder> list = new ArrayList<CcDetailExtendedHolder>();
            list.add(detail);
            map.put(lineSeqNo, list);
        }
        
        return map;
    }
    
}
