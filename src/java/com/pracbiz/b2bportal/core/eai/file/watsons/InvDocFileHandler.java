//*****************************************************************************
//
// File Name       :  InvDocFileHandler.java
// Date Created    :  Dec 17, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 17, 2013 4:53:48 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.watsons;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class InvDocFileHandler extends
    DefaultDocFileHandler<InvDocMsg, InvHolder> implements CoreCommonConstants
{
    private String DELIMITER_WATSONS = "^";
    @Override
    public byte[] getFileByte(InvHolder data, File targetFile,
        String expectedFormat) throws Exception
    {
        StringBuffer content = new StringBuffer();
        InvHolder invoice = data;
        
        InvHeaderHolder header = invoice.getHeader();
        initHeader(header, content);
        
        List<InvDetailHolder> invDetails = invoice.getDetails();
        initDetails(invDetails, content);
        
        return content.toString().getBytes(Charset.defaultCharset());
    }

    @Override
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
            + StringUtil.getInstance().convertDocNo(data.getHeader().getInvNo()) 
            + DOC_FILENAME_DELIMITOR + data.getHeader().getInvOid()
            + ".txt";
    }

    @Override
    protected String processFormat()
    {
        return WATSONS;
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
    
    
    private void initHeader(InvHeaderHolder header, StringBuffer content)
    {
        StringUtil su = StringUtil.getInstance();
        BigDecimalUtil bu = BigDecimalUtil.getInstance();
        boolean postfix = true;
        boolean prefix = false;
        if (header != null)
        {
            content
                .append(su.convertStringWithPrefixOrPostfix("T1", postfix, ' ', 2)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix("21", postfix, ' ', 2)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix("1", postfix, ' ', 1)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix("1", postfix, ' ', 1)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix("O", postfix, ' ', 1)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(header.getInvNo(), postfix, ' ', 30)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 5)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(
                        DateUtil.getInstance().convertDateToString(
                            header.getInvDate(), DateUtil.DATE_FORMAT_YYYYMMDD),postfix, ' ', 8)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(bu.convertBigDecimalToStringIntegerWithNoScale(header.getInvAmountNoVat(), 2), prefix, ' ', 12)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix("1", postfix, ' ', 1)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(bu.convertBigDecimalToStringIntegerWithNoScale(header.getVatAmount(), 2), prefix, ' ', 10)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(bu.convertBigDecimalToStringIntegerWithNoScale(header.getInvAmountWithVat(), 2), prefix, ' ', 12)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 8)).append(DELIMITER_WATSONS) //invBAN
                .append(su.convertStringWithPrefixOrPostfix(header.getShipToCode(), postfix, ' ', 13)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 60)).append(DELIMITER_WATSONS) //invoiceName
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 70)).append(DELIMITER_WATSONS) //invAddr
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 20)).append(DELIMITER_WATSONS) //invContact
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 1)).append(DELIMITER_WATSONS) //CWMark
                .append(su.convertStringWithPrefixOrPostfix("PO", postfix, ' ', 3)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(header.getPoNo(), postfix, ' ', 20)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix("DT", postfix, ' ', 3)).append(DELIMITER_WATSONS)
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 20)).append(DELIMITER_WATSONS) //reference 2
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 12)).append(DELIMITER_WATSONS) //recepit seller
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 100)).append(DELIMITER_WATSONS) //remarks
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 100)).append(DELIMITER_WATSONS) //reserved
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 8)).append(DELIMITER_WATSONS) //seller BAN
                .append(su.convertStringWithPrefixOrPostfix(header.getSupplierCode(), postfix, ' ', 13)).append(DELIMITER_WATSONS) //seller EAN
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 60)).append(DELIMITER_WATSONS) //seller Name
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 70)).append(DELIMITER_WATSONS) //seller Addr
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 20)).append(DELIMITER_WATSONS) //seller tel
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 10)).append(DELIMITER_WATSONS) //seller Rep
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 20)).append(DELIMITER_WATSONS) //seller
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 10)).append(DELIMITER_WATSONS) // digitApproval Agency
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 8)).append(DELIMITER_WATSONS) // digital Approval Date
                .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 20)).append(DELIMITER_WATSONS)
                .append(END_LINE); // digital Approval Doc
        }
    }
    
    
    private void initDetails(List<InvDetailHolder> invDetails, StringBuffer content)
    {
        StringUtil su = StringUtil.getInstance();
        boolean postfix = true;
        boolean prefix = false;
        
        if (invDetails != null && !invDetails.isEmpty())
        {
            for (InvDetailHolder detail : invDetails)
            {
                content
                    .append(su.convertStringWithPrefixOrPostfix("T2", postfix, ' ', 2)).append(DELIMITER_WATSONS)
                    .append(su.convertStringWithPrefixOrPostfix(detail.getLineSeqNo().toString(), prefix, ' ', 5)).append(DELIMITER_WATSONS)
                    .append(su.convertStringWithPrefixOrPostfix(detail.getBuyerItemCode(), postfix, ' ', 20)).append(DELIMITER_WATSONS)
                    .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 20)).append(DELIMITER_WATSONS)//seller product code
                    .append(su.convertStringWithPrefixOrPostfix(detail.getBarcode(), postfix, ' ', 20)).append(DELIMITER_WATSONS)//barcode
                    .append(su.convertStringWithPrefixOrPostfix(null, postfix, ' ', 70)).append(DELIMITER_WATSONS) // product nam
                    .append(su.convertStringWithPrefixOrPostfix(detail.getInvQty().toString(), prefix, ' ', 11)).append(DELIMITER_WATSONS)
                    .append(su.convertStringWithPrefixOrPostfix("1", prefix, ' ', 6)).append(DELIMITER_WATSONS)//unit
                    .append(su.convertStringWithPrefixOrPostfix(detail.getUnitPrice().toString(), prefix, ' ', 15)).append(DELIMITER_WATSONS)
                    .append(su.convertStringWithPrefixOrPostfix(detail.getItemAmount().toString(), prefix, ' ', 12)).append(DELIMITER_WATSONS)
                    .append(su.convertStringWithPrefixOrPostfix(null, prefix, ' ', 40)).append(DELIMITER_WATSONS)//reserved
                    .append(END_LINE); 
            }
        }
    }
    
}
