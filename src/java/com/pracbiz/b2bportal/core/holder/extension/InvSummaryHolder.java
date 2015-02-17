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
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class InvSummaryHolder extends MsgTransactionsExHolder
{
    private static final long serialVersionUID = 7001479354722361718L;

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
    private InvStatus invStatus;
    private PoInvGrnDnMatchingStatus matchingStatus;
    private String shipToCode;
    private Boolean inMatching;
    private String matchingStatusValue;
    private Integer detailCount;
    private Boolean canBeVoid;
    
    private boolean statusPending;
    private boolean statusMatched;
    private boolean statusMatchedByDn;
    private boolean statusUnmatched;
    private boolean statusAmountUnmatched;
    private boolean statusPriceUnmatched;
    private boolean statusQtyUnmatched;
    private boolean statusOutdated;
    private boolean statusInsInv;
    
    private List<PoInvGrnDnMatchingStatus> statusList;

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
        this.invDateFrom = invDateFrom == null ? null : (Date)invDateFrom.clone();
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

    public InvStatus getInvStatus()
    {
        return invStatus;
    }

    public void setInvStatus(InvStatus invStatus)
    {
        this.invStatus = invStatus;
    }

    public PoInvGrnDnMatchingStatus getMatchingStatus()
    {
        return matchingStatus;
    }

    public void setMatchingStatus(PoInvGrnDnMatchingStatus matchingStatus)
    {
        this.matchingStatus = matchingStatus;
    }

    public String getShipToCode()
    {
        return shipToCode;
    }

    public void setShipToCode(String shipToCode)
    {
        this.shipToCode = shipToCode;
    }

    public Boolean getInMatching()
    {
        return inMatching;
    }

    public void setInMatching(Boolean inMatching)
    {
        this.inMatching = inMatching;
    }

    public String getMatchingStatusValue()
    {
        return matchingStatusValue;
    }

    public void setMatchingStatusValue(String matchingStatusValue)
    {
        this.matchingStatusValue = matchingStatusValue;
    }

    public Integer getDetailCount()
    {
        return detailCount;
    }

    public void setDetailCount(Integer detailCount)
    {
        this.detailCount = detailCount;
    }

    public Boolean getCanBeVoid()
    {
        return canBeVoid;
    }

    public void setCanBeVoid(Boolean canBeVoid)
    {
        this.canBeVoid = canBeVoid;
    }

    public boolean isStatusPending()
    {
        return statusPending;
    }

    public void setStatusPending(boolean statusPending)
    {
        this.statusPending = statusPending;
    }

    public boolean isStatusMatched()
    {
        return statusMatched;
    }

    public void setStatusMatched(boolean statusMatched)
    {
        this.statusMatched = statusMatched;
    }

    public boolean isStatusMatchedByDn()
    {
        return statusMatchedByDn;
    }

    public void setStatusMatchedByDn(boolean statusMatchedByDn)
    {
        this.statusMatchedByDn = statusMatchedByDn;
    }

    public boolean isStatusUnmatched()
    {
        return statusUnmatched;
    }

    public void setStatusUnmatched(boolean statusUnmatched)
    {
        this.statusUnmatched = statusUnmatched;
    }

    public boolean isStatusAmountUnmatched()
    {
        return statusAmountUnmatched;
    }

    public void setStatusAmountUnmatched(boolean statusAmountUnmatched)
    {
        this.statusAmountUnmatched = statusAmountUnmatched;
    }

    public boolean isStatusPriceUnmatched()
    {
        return statusPriceUnmatched;
    }

    public void setStatusPriceUnmatched(boolean statusPriceUnmatched)
    {
        this.statusPriceUnmatched = statusPriceUnmatched;
    }

    public boolean isStatusQtyUnmatched()
    {
        return statusQtyUnmatched;
    }

    public void setStatusQtyUnmatched(boolean statusQtyUnmatched)
    {
        this.statusQtyUnmatched = statusQtyUnmatched;
    }

    public boolean isStatusOutdated()
    {
        return statusOutdated;
    }

    public void setStatusOutdated(boolean statusOutdated)
    {
        this.statusOutdated = statusOutdated;
    }

    public boolean isStatusInsInv()
    {
        return statusInsInv;
    }

    public void setStatusInsInv(boolean statusInsInv)
    {
        this.statusInsInv = statusInsInv;
    }

    public List<PoInvGrnDnMatchingStatus> getStatusList()
    {
        return statusList;
    }

    public void setStatusList(List<PoInvGrnDnMatchingStatus> statusList)
    {
        this.statusList = statusList;
    }
    
}
