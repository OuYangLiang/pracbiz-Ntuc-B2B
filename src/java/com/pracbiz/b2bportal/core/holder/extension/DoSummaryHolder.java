//*****************************************************************************
//
// File Name       :  DoSummaryHolder.java
// Date Created    :  2012-12-12
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-12 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class DoSummaryHolder extends MsgTransactionsExHolder
{
    private static final long serialVersionUID = -3297958129426381146L;

    private String doNo;
    private String poNo;
    private Date deliveryDate;
    private Date expiryDate;
    private String doRemarks;
    private Date poDateFrom;
    private Date poDateTo;
    private Date doDate;
    private Date doDateFrom;
    private Date doDateTo;
    private Date poDate;
    private BigDecimal doOid;

    public BigDecimal getDoOid()
    {
        return doOid;
    }

    public void setDoOid(BigDecimal doOid)
    {
        this.doOid = doOid;
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getPoDate()
    {
        return poDate == null ? null : (Date)poDate.clone();
    }

    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date)poDate.clone();
    }

    public Date getDoDateFrom()
    {
        return doDateFrom == null ? null : (Date)doDateFrom.clone();
    }

    public void setDoDateFrom(Date doDateFrom)
    {
        this.doDateFrom = doDateFrom == null ? null : (Date)doDateFrom.clone();
    }

    public Date getDoDateTo()
    {
        return doDateTo == null ? null : (Date)doDateTo.clone();
    }

    public void setDoDateTo(Date doDateTo)
    {
        this.doDateTo = doDateTo == null ? null : (Date)doDateTo.clone();
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getDoDate()
    {
        return doDate == null ? null : (Date)doDate.clone();
    }

    public void setDoDate(Date doDate)
    {
        this.doDate = doDate == null ? null : (Date)doDate.clone();
    }

    public Date getPoDateFrom()
    {
        return poDateFrom == null ? null : (Date)poDateFrom.clone();
    }

    public void setPoDateFrom(Date poDateFrom)
    {
        this.poDateFrom = poDateFrom == null ? null : (Date)poDateFrom.clone();
    }

    public Date getPoDateTo()
    {
        return poDateTo == null ? null : (Date)poDateTo.clone();
    }

    public void setPoDateTo(Date poDateTo)
    {
        this.poDateTo = poDateTo == null ? null : (Date)poDateTo.clone();
    }

    public String getDoNo()
    {
        return doNo;
    }

    public void setDoNo(String doNo)
    {
        this.doNo = doNo;
    }

    public String getPoNo()
    {
        return poNo;
    }

    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }

    public Date getDeliveryDate()
    {
        return deliveryDate == null ? null : (Date)deliveryDate.clone();
    }

    public void setDeliveryDate(Date deliveryDate)
    {
        this.deliveryDate = deliveryDate == null ? null : (Date)deliveryDate.clone();
    }

    public Date getExpiryDate()
    {
        return expiryDate == null ? null : (Date)expiryDate.clone();
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate == null ? null : (Date)expiryDate.clone();
    }

    public String getDoRemarks()
    {
        return doRemarks;
    }

    public void setDoRemarks(String doRemarks)
    {
        this.doRemarks = doRemarks;
    }

}
