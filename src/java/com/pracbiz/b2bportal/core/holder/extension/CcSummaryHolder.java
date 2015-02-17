//*****************************************************************************
//
// File Name       :  InvSummaryHolder.java
// Date Created    :  2012-12-13
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-13 $
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


import com.pracbiz.b2bportal.core.constants.CcStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;


/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CcSummaryHolder extends MsgTransactionsExHolder
{
    private static final long serialVersionUID = -627975599559686700L;

    private Date invDate;
    private Date invDateFrom;
    private Date invDateTo;
    private String invType;
    private String poNo;
    private BigDecimal invAmountWithVat;
    private BigDecimal vatAmount;
    private BigDecimal vatRate;
    private String invRemarks;
    private Date poDate;
    private Date poDateFrom;
    private Date poDateTo;
    private String invNo;
    private BigDecimal invOid;
    private CcStatus ccStatus;
    private String storeCode;
    private Boolean duplicate;
    private Integer detailCount;


    public BigDecimal getInvOid()
    {
        return invOid;
    }


    public void setInvOid(BigDecimal invOid)
    {
        this.invOid = invOid;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getInvDate()
    {
        return invDate == null ? null : (Date)invDate.clone();
    }


    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date)invDate.clone();
    }


    public Date getInvDateFrom()
    {
        return invDateFrom == null ? null : (Date)invDateFrom.clone();
    }


    public void setInvDateFrom(Date invDateFrom)
    {
        this.invDateFrom = invDateFrom == null ? null : (Date)invDateFrom
            .clone();
    }


    public Date getInvDateTo()
    {
        return invDateTo == null ? null : (Date)invDateTo.clone();
    }


    public void setInvDateTo(Date invDateTo)
    {
        this.invDateTo = invDateTo == null ? null : (Date)invDateTo.clone();
    }


    public String getInvType()
    {
        return invType;
    }


    public void setInvType(String invType)
    {
        this.invType = invType;
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }


    public BigDecimal getInvAmountWithVat()
    {
        return invAmountWithVat;
    }


    public void setInvAmountWithVat(BigDecimal invAmountWithVat)
    {
        this.invAmountWithVat = invAmountWithVat;
    }


    public BigDecimal getVatAmount()
    {
        return vatAmount;
    }


    public void setVatAmount(BigDecimal vatAmount)
    {
        this.vatAmount = vatAmount;
    }


    public BigDecimal getVatRate()
    {
        return vatRate;
    }


    public void setVatRate(BigDecimal vatRate)
    {
        this.vatRate = vatRate;
    }


    public String getInvRemarks()
    {
        return invRemarks;
    }


    public void setInvRemarks(String invRemarks)
    {
        this.invRemarks = invRemarks;
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


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    public CcStatus getCcStatus()
    {
        return ccStatus;
    }


    public void setCcStatus(CcStatus ccStatus)
    {
        this.ccStatus = ccStatus;
    }


    public String getStoreCode()
    {
        return storeCode;
    }


    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
    }


    public Boolean getDuplicate()
    {
        return duplicate;
    }


    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }


    public Integer getDetailCount()
    {
        return detailCount;
    }


    public void setDetailCount(Integer detailCount)
    {
        this.detailCount = detailCount;
    }

    
}
