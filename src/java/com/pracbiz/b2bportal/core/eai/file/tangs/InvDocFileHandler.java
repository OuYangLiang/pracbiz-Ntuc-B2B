//*****************************************************************************
//
// File Name       :  InvDocFileHandler.java
// Date Created    :  Feb 20, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Feb 20, 2014 6:10:06 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.tangs;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class InvDocFileHandler extends DefaultDocFileHandler<InvDocMsg, InvHolder> implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(InvDocFileHandler.class);
    @Override
    public byte[] getFileByte(InvHolder data, File targetFile,
        String expectedFormat) throws Exception
    {
        InvHolder inv = data;
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("Vendor_code,Inv_No,Inv_Date,Inv_Type,Po_No,Po_Date,Doc_Type,Amount_No_GST,GST_Amount,Amount_with_GST")
              .append(END_LINE)
              .append(inv.getHeader().getSupplierCode()).append(COMMA_DELIMITOR)
              .append(inv.getHeader().getInvNo()).append(COMMA_DELIMITOR)
              .append(DateUtil.getInstance().convertDateToString(inv.getHeader().getInvDate(), DateUtil.DATE_FORMAT_MMDDYYYY)).append(COMMA_DELIMITOR)
              .append(inv.getHeader().getInvType().name()).append(COMMA_DELIMITOR)
              .append(inv.getHeader().getPoNo()).append(COMMA_DELIMITOR)
              .append(DateUtil.getInstance().convertDateToString(inv.getHeader().getPoDate(), DateUtil.DATE_FORMAT_MMDDYYYY)).append(COMMA_DELIMITOR)
              .append(MsgType.INV.name()).append(COMMA_DELIMITOR)
              .append(inv.getHeader().getInvAmountNoVat() == null? BigDecimal.ZERO : inv.getHeader().getInvAmountNoVat().toString()).append(COMMA_DELIMITOR)
              .append(inv.getHeader().getVatAmount() == null ? BigDecimal.ZERO : inv.getHeader().getVatAmount().toString()).append(COMMA_DELIMITOR)
              .append(inv.getHeader().getInvAmountWithVat()== null ? BigDecimal.ZERO : inv.getHeader().getInvAmountWithVat().toString());
        
        return buffer.toString().getBytes(Charset.defaultCharset());

    }

    @Override
    public String getTargetFilename(InvHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }
        log.info("inv header : " + data.getHeader());
        return MsgType.INV + DOC_FILENAME_DELIMITOR
            + data.getHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getHeader().getInvNo())
            + DOC_FILENAME_DELIMITOR + data.getHeader().getInvOid() + ".csv";
    
    }

    @Override
    protected String processFormat()
    {
        return TANGS;
    }

    @Override
    public void readFileContent(InvDocMsg docMsg, byte[] input)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected String translate(InvDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);

        return targetFile.getName();
    }

}
