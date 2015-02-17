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
public class MatchingReportHolder extends BaseHolder
{
    private static final long serialVersionUID = -7156003851544337242L;
    
    private BigDecimal buyerOid;
    private BigDecimal supplierOid;
    private String supplierCode;
    private String supplierName;
    private String type;
    private Date reportDateFrom;
    private Date reportDateTo;
    private Date fromDate;
    private Date toDate;
    private String moreThanDays;
    private String msgType;
    private String discrepancyType;
   
    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
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


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public Date getReportDateFrom()
    {
        return reportDateFrom == null ? null : (Date)reportDateFrom.clone();
    }


    public void setReportDateFrom(Date reportDateFrom)
    {
        this.reportDateFrom = reportDateFrom == null ? null : (Date)reportDateFrom.clone();
    }
    
    
    public Date getReportDateTo()
    {
        return reportDateTo == null ? null : (Date)reportDateTo.clone();
    }


    public void setReportDateTo(Date reportDateTo)
    {
        this.reportDateTo = reportDateTo == null ? null : (Date)reportDateTo.clone();
    }


    public Date getFromDate()
    {
        return fromDate == null ? null : (Date)fromDate.clone();
    }


    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate == null ? null : (Date)fromDate.clone();
    }


    public Date getToDate()
    {
        return toDate == null ? null : (Date)toDate.clone();
    }


    public void setToDate(Date toDate)
    {
        this.toDate = toDate == null ? null : (Date)toDate.clone();
    }


    public String getMoreThanDays()
    {
        return moreThanDays;
    }


    public void setMoreThanDays(String moreThanDays)
    {
        this.moreThanDays = moreThanDays;
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }

    
    public String getMsgType()
    {
        return msgType;
    }


    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }


    public String getDiscrepancyType()
    {
        return discrepancyType;
    }


    public void setDiscrepancyType(String discrepancyType)
    {
        this.discrepancyType = discrepancyType;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(buyerOid);
    }

}
