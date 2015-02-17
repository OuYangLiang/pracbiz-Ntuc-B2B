//*****************************************************************************
//
// File Name       :  CnDocFileHandler.java
// Date Created    :  Feb 20, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Feb 20, 2014 6:10:18 PM $
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

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.CnDocMsg;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;


/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CnDocFileHandler extends DefaultDocFileHandler<CnDocMsg, CnHolder>
    implements CoreCommonConstants
{

    @Override
    public byte[] getFileByte(CnHolder data, File targetFile, String expectedFormat)
        throws Exception
    {
        CnHolder cn = data;
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("Vendor_code,Cn_No,Cn_Date,Cn_Type,Po_No,Po_Date,Doc_Type,Amount_No_GST,GST_Amount,Amount_with_GST")
              .append(END_LINE)
              .append(cn.getHeader().getSupplierCode()).append(COMMA_DELIMITOR)
              .append(cn.getHeader().getCnNo()).append(COMMA_DELIMITOR)
              .append(DateUtil.getInstance().convertDateToString(cn.getHeader().getCnDate(), DateUtil.DATE_FORMAT_MMDDYYYY)).append(COMMA_DELIMITOR)
              .append(cn.getHeader().getCnType().name()).append(COMMA_DELIMITOR)
              .append(cn.getHeader().getPoNo()).append(COMMA_DELIMITOR)
              .append(DateUtil.getInstance().convertDateToString(cn.getHeader().getPoDate(), DateUtil.DATE_FORMAT_MMDDYYYY)).append(COMMA_DELIMITOR)
              .append(MsgType.CN.name()).append(COMMA_DELIMITOR)
              .append(cn.getHeader().getTotalCost() == null ? BigDecimal.ZERO : cn.getHeader().getTotalCost().toString()).append(COMMA_DELIMITOR)
              .append(cn.getHeader().getTotalVat() == null ? BigDecimal.ZERO : cn.getHeader().getTotalVat().toString()).append(COMMA_DELIMITOR)
              .append(cn.getHeader().getTotalCostWithVat() == null ? BigDecimal.ZERO : cn.getHeader().getTotalCostWithVat().toString());
        
        return buffer.toString().getBytes(Charset.defaultCharset());

    }


    @Override
    public String getTargetFilename(CnHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }

        return MsgType.CN + DOC_FILENAME_DELIMITOR
            + data.getHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getHeader().getCnNo())
            + DOC_FILENAME_DELIMITOR + data.getHeader().getCnOid() + ".csv";
    }


    @Override
    protected String processFormat()
    {
        return TANGS;
    }


    @Override
    public void readFileContent(CnDocMsg docMsg, byte[] input) throws Exception
    {
        
    }


    @Override
    protected String translate(CnDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);

        return targetFile.getName();
    }

}
