//*****************************************************************************
//
// File Name       :  DnSummaryHolder.java
// Date Created    :  2012-12-13
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-13$
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

import com.pracbiz.b2bportal.core.constants.DnBuyerStatus;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DnSummaryHolder extends MsgTransactionsExHolder
{

    private static final long serialVersionUID = 1822309173047567679L;

    private String dnType;
    private Date dnDate;
    private Date dnDateFrom;
    private Date dnDateTo;
    private BigDecimal totalCost;
    private String reasonDesc;
    private String dnRemarks;
    private String poNo;
    private String invNo;
    private String rtvNo;
    private String giNo;
    private Date poDateFrom;
    private Date poDateTo;
    private Date invDate;
    private Date invDateFrom;
    private Date invDateTo;
    private Date poDate;
    private BigDecimal dnOid;
    private String dnNo;
    private Boolean sentToSupplier;
    private Boolean markSentToSupplier;
    private Boolean supplierFlag;// to mark if the login user is a supplier type user.
    private DnStatus dnStatus;
    private Boolean duplicate;
    private DnBuyerStatus buyerStatus;
    private DnPriceStatus priceStatus;
    private DnQtyStatus qtyStatus;
    private Boolean dispute;
    private Boolean closed;
    private String closedRemarks;
    private String closedBy;
    private Date closedDate;
    private String storeCode;
    private Integer disputeWindow;
    private Integer detailCount;
    private Boolean allowMatchingDnDispute;
    private Boolean priceDisputed;
    private Boolean qtyDisputed;
    private String taskListType;
    private List<BigDecimal> taskListOids;
    private String alertWindow;
    private boolean priceApprove;
    private boolean pendingForClosing;

    public String getDnNo()
    {
        return dnNo;
    }

    public void setDnNo(String dnNo)
    {
        this.dnNo = dnNo;
    }

    public BigDecimal getDnOid()
    {
        return dnOid;
    }

    public void setDnOid(BigDecimal dnOid)
    {
        this.dnOid = dnOid;
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

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getInvDate()
    {
        return invDate == null ? null : (Date)invDate.clone();
    }

    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date)invDate.clone();
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

    public String getDnType()
    {
        return dnType;
    }

    public void setDnType(String dnType)
    {
        this.dnType = dnType;
    }

    public Date getDnDateFrom()
    {
        return dnDateFrom == null ? null : (Date)dnDateFrom.clone();
    }

    public void setDnDateFrom(Date dnDateFrom)
    {
        this.dnDateFrom = dnDateFrom == null ? null : (Date)dnDateFrom.clone();
    }

    public Date getDnDateTo()
    {
        return dnDateTo == null ? null : (Date)dnDateTo.clone();
    }

    public void setDnDateTo(Date dnDateTo)
    {
        this.dnDateTo = dnDateTo == null ? null : (Date)dnDateTo.clone();
    }

    public BigDecimal getTotalCost()
    {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost)
    {
        this.totalCost = totalCost;
    }

    public String getReasonDesc()
    {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc)
    {
        this.reasonDesc = reasonDesc;
    }

    public String getDnRemarks()
    {
        return dnRemarks;
    }

    public void setDnRemarks(String dnRemarks)
    {
        this.dnRemarks = dnRemarks;
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getDnDate()
    {
        return dnDate == null ? null : (Date)dnDate.clone();
    }

    public void setDnDate(Date dnDate)
    {
        this.dnDate = dnDate == null ? null : (Date)dnDate.clone();
    }

    public String getPoNo()
    {
        return poNo;
    }

    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }

    public String getInvNo()
    {
        return invNo;
    }

    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }

    public String getRtvNo()
    {
        return rtvNo;
    }

    public void setRtvNo(String rtvNo)
    {
        this.rtvNo = rtvNo;
    }

    public String getGiNo()
    {
        return giNo;
    }

    public void setGiNo(String giNo)
    {
        this.giNo = giNo;
    }

    public Boolean getSentToSupplier()
    {
        return sentToSupplier;
    }

    public void setSentToSupplier(Boolean sentToSupplier)
    {
        this.sentToSupplier = sentToSupplier;
    }

    public Boolean getMarkSentToSupplier()
    {
        return markSentToSupplier;
    }

    public void setMarkSentToSupplier(Boolean markSentToSupplier)
    {
        this.markSentToSupplier = markSentToSupplier;
    }

    public Boolean getSupplierFlag()
    {
        return supplierFlag;
    }

    public void setSupplierFlag(Boolean supplierFlag)
    {
        this.supplierFlag = supplierFlag;
    }

    public DnStatus getDnStatus()
    {
        return dnStatus;
    }

    public void setDnStatus(DnStatus dnStatus)
    {
        this.dnStatus = dnStatus;
    }

    public Boolean getDuplicate()
    {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }

    public DnBuyerStatus getBuyerStatus()
    {
        return buyerStatus;
    }

    public void setBuyerStatus(DnBuyerStatus buyerStatus)
    {
        this.buyerStatus = buyerStatus;
    }

    public DnPriceStatus getPriceStatus()
    {
        return priceStatus;
    }

    public void setPriceStatus(DnPriceStatus priceStatus)
    {
        this.priceStatus = priceStatus;
    }

    public DnQtyStatus getQtyStatus()
    {
        return qtyStatus;
    }

    public void setQtyStatus(DnQtyStatus qtyStatus)
    {
        this.qtyStatus = qtyStatus;
    }

    public Boolean getDispute()
    {
        return dispute;
    }

    public void setDispute(Boolean dispute)
    {
        this.dispute = dispute;
    }

    public Boolean getClosed()
    {
        return closed;
    }

    public void setClosed(Boolean closed)
    {
        this.closed = closed;
    }

    public String getClosedRemarks()
    {
        return closedRemarks;
    }

    public void setClosedRemarks(String closedRemarks)
    {
        this.closedRemarks = closedRemarks;
    }

    public String getClosedBy()
    {
        return closedBy;
    }

    public void setClosedBy(String closedBy)
    {
        this.closedBy = closedBy;
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getClosedDate()
    {
        return closedDate == null ? null : (Date)closedDate.clone();
    }

    public void setClosedDate(Date closedDate)
    {
        this.closedDate = closedDate == null ? null : (Date)closedDate.clone();
    }

    public String getStoreCode()
    {
        return storeCode;
    }

    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
    }

    public Integer getDisputeWindow()
    {
        return disputeWindow;
    }

    public void setDisputeWindow(Integer disputeWindow)
    {
        this.disputeWindow = disputeWindow;
    }

    public Integer getDetailCount()
    {
        return detailCount;
    }

    public void setDetailCount(Integer detailCount)
    {
        this.detailCount = detailCount;
    }

    public Boolean getAllowMatchingDnDispute()
    {
        return allowMatchingDnDispute;
    }

    public void setAllowMatchingDnDispute(Boolean allowMatchingDnDispute)
    {
        this.allowMatchingDnDispute = allowMatchingDnDispute;
    }

    public Boolean getPriceDisputed()
    {
        return priceDisputed;
    }

    public void setPriceDisputed(Boolean priceDisputed)
    {
        this.priceDisputed = priceDisputed;
    }

    public Boolean getQtyDisputed()
    {
        return qtyDisputed;
    }

    public void setQtyDisputed(Boolean qtyDisputed)
    {
        this.qtyDisputed = qtyDisputed;
    }

    public String getTaskListType()
    {
        return taskListType;
    }

    public void setTaskListType(String taskListType)
    {
        this.taskListType = taskListType;
    }

    public List<BigDecimal> getTaskListOids()
    {
        return taskListOids;
    }

    public void setTaskListOids(List<BigDecimal> taskListOids)
    {
        this.taskListOids = taskListOids;
    }

	public String getAlertWindow() {
		return alertWindow;
	}

	public void setAlertWindow(String alertWindow) {
		this.alertWindow = alertWindow;
	}

    public boolean isPriceApprove()
    {
        return priceApprove;
    }

    public void setPriceApprove(boolean priceApprove)
    {
        this.priceApprove = priceApprove;
    }

    public boolean isPendingForClosing()
    {
        return pendingForClosing;
    }

    public void setPendingForClosing(boolean pendingForClosing)
    {
        this.pendingForClosing = pendingForClosing;
    }

}
