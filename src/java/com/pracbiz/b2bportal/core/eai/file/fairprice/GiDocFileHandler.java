//*****************************************************************************
//
// File Name       :  GiDocFileHandler.java
// Date Created    :  Nov 13, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 13, 2013 11:16:44 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.fairprice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.GiStatus;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.GiDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class GiDocFileHandler extends DefaultDocFileHandler<GiDocMsg, GiHolder> implements
    CoreCommonConstants
{
    @Override
    protected String processFormat()
    {
        return FAIRPRICE;
    }

    @Override
    public void readFileContent(GiDocMsg docMsg, byte[] input)
        throws Exception
    {
        InputStream inputStream = null;
        try
        {
            // read source file and group according by record type
            GiHolder data = new GiHolder();
            inputStream = new ByteArrayInputStream(input);
            List<String> contents = FileParserUtil.getInstance().readLines(inputStream);
            BigDecimal giOid = docMsg.getDocOid();
            
            GiHeaderHolder header = this.initHeader(contents.get(0),docMsg);
            data.setGiHeader(header);
            
            
            //record type is 'DET'
            List<GiDetailHolder> details = this.initDetail(contents, giOid, header);
            data.setDetails(details);
            
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
    protected String translate(GiDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);
        
        return targetFile.getName();
    }

    @Override
    public byte[] getFileByte(GiHolder data, File targetFile, String expectedFormat)
        throws Exception
    {
        return null;
        
    }

    @Override
    public String getTargetFilename(GiHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        
        return MsgType.GI + DOC_FILENAME_DELIMITOR
            + data.getGiHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + data.getGiHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getGiHeader().getGiNo()) + DOC_FILENAME_DELIMITOR + data.getGiHeader().getGiOid()
            + ".txt";
        
    }
    
    
    private GiHeaderHolder initHeader(String content, GiDocMsg docMsg) throws Exception
    {
        GiHeaderHolder giHeader = new GiHeaderHolder();

        giHeader.setGiOid(docMsg.getDocOid());
        giHeader.setGiNo(StringUtil.getInstance().convertDocNoWithNoLeading(
            content.substring(2, 12).trim(), LEADING_ZERO));
        giHeader.setDocAction("A");
        giHeader.setActionDate(new Date());
        giHeader.setGiDate(DateUtil.getInstance().convertStringToDate(
            content.substring(46, 54).trim(), DateUtil.DATE_FORMAT_YYYYMMDD));
        giHeader.setRtvNo(StringUtil.getInstance().convertDocNoWithNoLeading(
            content.substring(12, 22).trim(), LEADING_ZERO));
        giHeader.setRtvDate(DateUtil.getInstance().convertStringToDate(content.substring(30, 38).trim(), DateUtil.DATE_FORMAT_YYYYMMDD)); //rtvDate
        giHeader.setCreateDate(DateUtil.getInstance().convertStringToDate(content.substring(22, 30).trim(), DateUtil.DATE_FORMAT_YYYYMMDD));
        giHeader.setBuyerOid(docMsg.getSenderOid());
        giHeader.setBuyerCode(docMsg.getSenderCode());
        giHeader.setBuyerName(docMsg.getSenderName());
        giHeader.setSupplierOid(docMsg.getReceiverOid());
        giHeader.setSupplierCode(docMsg.getReceiverCode());
        giHeader.setSupplierName(docMsg.getReceiverName());
        giHeader.setIssuingStoreCode(content.substring(54, 58).trim()); 
        giHeader.setIssuingStoreName(null);
        giHeader.setTotalIssuedQty(null);
        giHeader.setCollectedBy(null);
        giHeader.setItemCount(content.substring(233, content.length()).trim().isEmpty() ? null : Integer.valueOf(content.substring(233, content.length()).trim()));
        giHeader.setTotalCost(null);
        giHeader.setGiRemarks(null);
        
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, giHeader);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, giHeader);
        giHeader.setGiStatus(GiStatus.NEW);
        return giHeader;
    }
    
    
//    private GiHeaderExtendedHolder initHeaderEx(String[] content)
//    {
//        return null;
//    }
    
    
    private List<GiDetailHolder> initDetail(List<String> contents, BigDecimal giOid, GiHeaderHolder header)
    {
        BigDecimal totalIssuedQty = BigDecimal.ZERO;
        List<GiDetailHolder> giDetails = new ArrayList<GiDetailHolder>();
        for (String content : contents)
        {
            GiDetailHolder giDetail = new GiDetailHolder();
            giDetail.setGiOid(giOid);
            giDetail.setLineSeqNo(Integer.valueOf(content.substring(168, 172).trim()));
            giDetail.setBuyerItemCode(StringUtil.getInstance().convertDocNoWithNoLeading(content.substring(172,190).trim(), LEADING_ZERO));
            giDetail.setSupplierItemCode(null);
            giDetail.setBarcode(content.substring(190, 208).trim());
            giDetail.setItemDesc(null); 
            giDetail.setBrand(null);
            giDetail.setColourCode(null);
            giDetail.setColourDesc(null);
            giDetail.setSizeCode(null);
            giDetail.setSizeDesc(null);
            giDetail.setPackingFactor(content.substring(211, 216).trim().isEmpty()? BigDecimal.ONE : new BigDecimal(content.substring(211, 216).trim()));
            giDetail.setRtvBaseUnit("P");
            giDetail.setRtvUom(content.substring(208, 211).trim().isEmpty() ? null : content.substring(208, 211).trim());
            giDetail.setRtvQty(new BigDecimal(content.substring(216, 229).trim()));
            giDetail.setIssuedQty(new BigDecimal(content.substring(216, 229).trim()));
            giDetail.setUnitCost(null);
            giDetail.setItemCost(null);
            giDetail.setItemRemarks(content.substring(68, 168).trim());
            
            giDetails.add(giDetail);
            
            totalIssuedQty = totalIssuedQty.add(giDetail.getIssuedQty());
        }
        header.setTotalIssuedQty(totalIssuedQty);
        return giDetails;
    }
    
    
//    private GiDetailExtendedHolder initDetailEx(String[] content)
//    {
//        return null;
//    }
}
