//*****************************************************************************
//
// File Name       :  ReadStatusReportHolder.java
// Date Created    :  2013-7-30
// Last Changed By :  $Author: xuchengqing $
// Last Changed On :  $Date: 2011-07-01 10:56:27 +0800 (÷‹ŒÂ, 01 ∆ﬂ‘¬ 2011) $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */
public class ReadStatusReportHolder extends BaseHolder
{
    private static final long serialVersionUID = 7050685203820821160L;
    
    private BigDecimal docOid;
    private String msgType;
    private String msgRefNo;
    private String buyerCode;
    private String buyerName;
    private BigDecimal supplierOid;
    private String supplierCode;
    private String supplierName;
    private Date sentDate;
    
    
    public BigDecimal getDocOid()
    {
        return docOid;
    }


    public void setDocOid(BigDecimal docOid)
    {
        this.docOid = docOid;
    }


    public String getMsgType()
    {
        return msgType;
    }


    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }


    public String getMsgRefNo()
    {
        return msgRefNo;
    }


    public void setMsgRefNo(String msgRefNo)
    {
        this.msgRefNo = msgRefNo;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }


    public String getSupplierCode()
    {
        return supplierCode;
    }


    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }


    public Date getSentDate()
    {
        return sentDate == null ? null : (Date)sentDate.clone();
    }


    public void setSentDate(Date sentDate)
    {
        this.sentDate = sentDate == null ? null : (Date)sentDate.clone();
    }


    @Override
    public String getCustomIdentification()
    {
        return docOid == null ? null : docOid.toString();
    }

}
