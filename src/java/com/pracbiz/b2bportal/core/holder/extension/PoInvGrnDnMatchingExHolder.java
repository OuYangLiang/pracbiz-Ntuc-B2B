package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingBuyerStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingInvStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;

public class PoInvGrnDnMatchingExHolder extends PoInvGrnDnMatchingHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = -4986929273749491414L;
    private String grnOid;
    private String grnNo;
    private BigDecimal grnAmt;
    private String grnDate;
    private List<BuyerHolder> buyerList;
    private List<SupplierHolder> supplierList;
    private Date poDateFrom;
    private Date poDateTo;
    private Date invDateFrom;
    private Date invDateTo;
    private Date dnDateFrom;
    private Date dnDateTo;
    private Date grnDateFrom;
    private Date grnDateTo;
    private String matchingStatusValue;
    private String invStatusValue;
    private String supplierStatusValue;
    private String buyerStatusValue;
    private String qtyStatusValue;
    private String priceStatusValue;
    private List<String> buyerStoreAccessList;
    private BigDecimal userOid;
    private List<BigDecimal> supplierSharedOids;
    
    
    private BigDecimal currentUserType;
    private BigDecimal currentUserOid;
    private BigDecimal currentUserBuyerOid;
    private BigDecimal currentUserSupplierOid;
    private BigDecimal currentUserGroupOid;
    private Boolean visiable;
    private Boolean fullGroupPriv;
    private Boolean validSupplierSet;
    private BigDecimal setOid;
    private String moreThanDays;
    private Boolean fullClassPriv;
    private String taskListType;
    private List<BigDecimal> taskListOids;
    private boolean allStoreFlag;
    private boolean statusPending;
    private boolean statusMatched;
    private boolean statusMatchedByDn;
    private boolean statusUnmatched;
    private boolean statusAmountUnmatched;
    private boolean statusPriceUnmatched;
    private boolean statusQtyUnmatched;
    private boolean statusOutdated;
    private boolean statusInsInv;
    private boolean pendingForClosing;
    private boolean enableSupplierDispute;
    private boolean pendingForApproving;
    private boolean priceApprove;
    
    public String getGrnOid()
    {
        return grnOid;
    }

    public void setGrnOid(String grnOid)
    {
        this.grnOid = grnOid;
    }
    
    public String getGrnNo()
    {
        return grnNo;
    }

    public void setGrnNo(String grnNo)
    {
        this.grnNo = grnNo;
    }

    public BigDecimal getGrnAmt()
    {
        return grnAmt;
    }

    public void setGrnAmt(BigDecimal grnAmt)
    {
        this.grnAmt = grnAmt;
    }

    public String getGrnDate()
    {
        return grnDate;
    }

    public void setGrnDate(String grnDate)
    {
        this.grnDate = grnDate;
    }

    public BigDecimal getCurrentUserBuyerOid()
    {
        return currentUserBuyerOid;
    }

    public void setCurrentUserBuyerOid(BigDecimal currentUserBuyerOid)
    {
        this.currentUserBuyerOid = currentUserBuyerOid;
    }

    public BigDecimal getCurrentUserSupplierOid()
    {
        return currentUserSupplierOid;
    }

    public void setCurrentUserSupplierOid(BigDecimal currentUserSupplierOid)
    {
        this.currentUserSupplierOid = currentUserSupplierOid;
    }
    
    public List<BuyerHolder> getBuyerList()
    {
        if (this.buyerList == null)
        {
            return new ArrayList<BuyerHolder>();
        }
        return buyerList;
    }

    public void setBuyerList(List<BuyerHolder> buyerList)
    {
        this.buyerList = buyerList;
    }

    public List<SupplierHolder> getSupplierList()
    {
        if (this.supplierList == null)
        {
            return new ArrayList<SupplierHolder>();
        }
        return supplierList;
    }

    public void setSupplierList(List<SupplierHolder> supplierList)
    {
        this.supplierList = supplierList;
    }


    public void addBuyer(BuyerHolder buyer)
    {
        if (this.buyerList == null)
        {
            this.buyerList = new ArrayList<BuyerHolder>();
        }
        this.getBuyerList().add(buyer);
    }
    
    public void addSupplier(SupplierHolder supplier)
    {
        if (this.supplierList == null)
        {
            this.supplierList = new ArrayList<SupplierHolder>();
        }
        this.getSupplierList().add(supplier);
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

    public Date getGrnDateFrom()
    {
        return grnDateFrom == null ? null : (Date)grnDateFrom.clone();
    }

    public void setGrnDateFrom(Date grnDateFrom)
    {
        this.grnDateFrom = grnDateFrom == null ? null : (Date)grnDateFrom.clone();
    }

    public Date getGrnDateTo()
    {
        return grnDateTo == null ? null : (Date)grnDateTo.clone();
    }

    public void setGrnDateTo(Date grnDateTo)
    {
        this.grnDateTo = grnDateTo == null ? null : (Date)grnDateTo.clone();
    }

    public String getMatchingStatusValue()
    {
        return matchingStatusValue;
    }

    public void setMatchingStatusValue(String matchingStatusValue)
    {
        this.matchingStatusValue = matchingStatusValue;
    }

    public String getInvStatusValue()
    {
        return invStatusValue;
    }

    public void setInvStatusValue(String invStatusValue)
    {
        this.invStatusValue = invStatusValue;
    }

    public String getSupplierStatusValue()
    {
        return supplierStatusValue;
    }

    public void setSupplierStatusValue(String supplierStatusValue)
    {
        this.supplierStatusValue = supplierStatusValue;
    }

    public String getBuyerStatusValue()
    {
        return buyerStatusValue;
    }

    public void setBuyerStatusValue(String buyerStatusValue)
    {
        this.buyerStatusValue = buyerStatusValue;
    }

    public String getQtyStatusValue()
    {
        return qtyStatusValue;
    }

    public void setQtyStatusValue(String qtyStatusValue)
    {
        this.qtyStatusValue = qtyStatusValue;
    }

    public String getPriceStatusValue()
    {
        return priceStatusValue;
    }

    public void setPriceStatusValue(String priceStatusValue)
    {
        this.priceStatusValue = priceStatusValue;
    }

    public List<String> getBuyerStoreAccessList()
    {
        return buyerStoreAccessList;
    }

    public void setBuyerStoreAccessList(List<String> buyerStoreAccessList)
    {
        this.buyerStoreAccessList = buyerStoreAccessList;
    }

    public BigDecimal getUserOid()
    {
        return userOid;
    }

    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }

    public List<BigDecimal> getSupplierSharedOids()
    {
        return supplierSharedOids;
    }

    public void setSupplierSharedOids(List<BigDecimal> supplierSharedOids)
    {
        this.supplierSharedOids = supplierSharedOids;
    }

    public BigDecimal getCurrentUserType()
    {
        return currentUserType;
    }

    public void setCurrentUserType(BigDecimal currentUserType)
    {
        this.currentUserType = currentUserType;
    }

    public BigDecimal getCurrentUserOid()
    {
        return currentUserOid;
    }

    public void setCurrentUserOid(BigDecimal currentUserOid)
    {
        this.currentUserOid = currentUserOid;
    }

    public BigDecimal getCurrentUserGroupOid()
    {
        return currentUserGroupOid;
    }

    public void setCurrentUserGroupOid(BigDecimal currentUserGroupOid)
    {
        this.currentUserGroupOid = currentUserGroupOid;
    }

    public Boolean getVisiable()
    {
        return visiable;
    }

    public void setVisiable(Boolean visiable)
    {
        this.visiable = visiable;
    }

    public Boolean getFullGroupPriv()
    {
        return fullGroupPriv;
    }

    public void setFullGroupPriv(Boolean fullGroupPriv)
    {
        this.fullGroupPriv = fullGroupPriv;
    }

    public Boolean getValidSupplierSet()
    {
        return validSupplierSet;
    }

    public void setValidSupplierSet(Boolean validSupplierSet)
    {
        this.validSupplierSet = validSupplierSet;
    }

    public BigDecimal getSetOid()
    {
        return setOid;
    }

    public void setSetOid(BigDecimal setOid)
    {
        this.setOid = setOid;
    }

    public String getMoreThanDays()
    {
        return moreThanDays;
    }

    public void setMoreThanDays(String moreThanDays)
    {
        this.moreThanDays = moreThanDays;
    }

    public Boolean getFullClassPriv()
    {
        return fullClassPriv;
    }

    public void setFullClassPriv(Boolean fullClassPriv)
    {
        this.fullClassPriv = fullClassPriv;
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

    public boolean isAllStoreFlag()
    {
        return allStoreFlag;
    }

    public void setAllStoreFlag(boolean allStoreFlag)
    {
        this.allStoreFlag = allStoreFlag;
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
    
    public boolean isPendingForClosing()
    {
        return pendingForClosing;
    }

    public void setPendingForClosing(boolean pendingForClosing)
    {
        this.pendingForClosing = pendingForClosing;
    }

    public boolean isEnableSupplierDispute()
    {
        return enableSupplierDispute;
    }

    public void setEnableSupplierDispute(boolean enableSupplierDispute)
    {
        this.enableSupplierDispute = enableSupplierDispute;
    }

    public boolean isPendingForApproving()
    {
        return pendingForApproving;
    }

    public void setPendingForApproving(boolean pendingForApproving)
    {
        this.pendingForApproving = pendingForApproving;
    }

    public boolean isPriceApprove()
    {
        return priceApprove;
    }

    public void setPriceApprove(boolean priceApprove)
    {
        this.priceApprove = priceApprove;
    }

    public List<PoInvGrnDnMatchingStatus> getStatusList()
    {
        List<PoInvGrnDnMatchingStatus> statusList = new ArrayList<PoInvGrnDnMatchingStatus>();
        
        if (isStatusPending())
            statusList.add(PoInvGrnDnMatchingStatus.PENDING);
        
        if (isStatusMatched())
            statusList.add(PoInvGrnDnMatchingStatus.MATCHED);
        
        if (isStatusMatchedByDn())
            statusList.add(PoInvGrnDnMatchingStatus.MATCHED_BY_DN);
        
        if (isStatusAmountUnmatched())
            statusList.add(PoInvGrnDnMatchingStatus.AMOUNT_UNMATCHED);
        
        if (isStatusUnmatched())
            statusList.add(PoInvGrnDnMatchingStatus.UNMATCHED);
        
        if (isStatusPriceUnmatched())
            statusList.add(PoInvGrnDnMatchingStatus.PRICE_UNMATCHED);
        
        if (isStatusQtyUnmatched())
            statusList.add(PoInvGrnDnMatchingStatus.QTY_UNMATCHED);
        
        if (isStatusOutdated())
            statusList.add(PoInvGrnDnMatchingStatus.OUTDATED);
        
        if (isStatusInsInv())
            statusList.add(PoInvGrnDnMatchingStatus.INSUFFICIENT_INV);
        
        return statusList;
    }
    
    public List<PoInvGrnDnMatchingStatus> getMatchingStatusList()
    {
        List<PoInvGrnDnMatchingStatus> matchingStatusList = new ArrayList<PoInvGrnDnMatchingStatus>();
        
        if (!isStatusPending() && !isStatusMatched() && !isStatusMatchedByDn() && !isStatusAmountUnmatched()
            && !isStatusUnmatched() && !isStatusPriceUnmatched() && !isStatusQtyUnmatched() && !isStatusOutdated()
            && !isStatusInsInv())
        {
            matchingStatusList.add(PoInvGrnDnMatchingStatus.PENDING);
            matchingStatusList.add(PoInvGrnDnMatchingStatus.MATCHED);
            matchingStatusList.add(PoInvGrnDnMatchingStatus.MATCHED_BY_DN);
            matchingStatusList.add(PoInvGrnDnMatchingStatus.AMOUNT_UNMATCHED);
            matchingStatusList.add(PoInvGrnDnMatchingStatus.UNMATCHED);
            matchingStatusList.add(PoInvGrnDnMatchingStatus.PRICE_UNMATCHED);
            matchingStatusList.add(PoInvGrnDnMatchingStatus.QTY_UNMATCHED);
            matchingStatusList.add(PoInvGrnDnMatchingStatus.OUTDATED);
            matchingStatusList.add(PoInvGrnDnMatchingStatus.INSUFFICIENT_INV);
        }
        
        return matchingStatusList;
    }
    
    
    public List<PoInvGrnDnMatchingInvStatus> getInvStatusList()
    {
        List<PoInvGrnDnMatchingInvStatus> invStatusList = new ArrayList<PoInvGrnDnMatchingInvStatus>();
        
        if (getInvStatus() == null)
        {
            invStatusList.add(PoInvGrnDnMatchingInvStatus.PENDING);
            invStatusList.add(PoInvGrnDnMatchingInvStatus.APPROVED);
            invStatusList.add(PoInvGrnDnMatchingInvStatus.SYS_APPROVED);
        }
        
        return invStatusList;
    }
    
    
    public List<PoInvGrnDnMatchingSupplierStatus> getSupplierStatusList()
    {
        List<PoInvGrnDnMatchingSupplierStatus> supplierStatusList = new ArrayList<PoInvGrnDnMatchingSupplierStatus>();
        
        if (getSupplierStatus() == null)
        {
            supplierStatusList.add(PoInvGrnDnMatchingSupplierStatus.ACCEPTED);
            supplierStatusList.add(PoInvGrnDnMatchingSupplierStatus.REJECTED);
            supplierStatusList.add(PoInvGrnDnMatchingSupplierStatus.PENDING);
        }
        
        return supplierStatusList;
    }
    
    
    public List<PoInvGrnDnMatchingBuyerStatus> getBuyerStatusList()
    {
        List<PoInvGrnDnMatchingBuyerStatus> buyerStatusList = new ArrayList<PoInvGrnDnMatchingBuyerStatus>();
        
        if (getBuyerStatus() == null)
        {
            buyerStatusList.add(PoInvGrnDnMatchingBuyerStatus.PENDING);
            buyerStatusList.add(PoInvGrnDnMatchingBuyerStatus.ACCEPTED);
            buyerStatusList.add(PoInvGrnDnMatchingBuyerStatus.REJECTED);
        }
        
        return buyerStatusList;
    }
    
    
    public List<PoInvGrnDnMatchingPriceStatus> getPriceStatusList()
    {
        List<PoInvGrnDnMatchingPriceStatus> priceStatusList = new ArrayList<PoInvGrnDnMatchingPriceStatus>();
        
        if (getPriceStatus() == null)
        {
            priceStatusList.add(PoInvGrnDnMatchingPriceStatus.PENDING);
            priceStatusList.add(PoInvGrnDnMatchingPriceStatus.ACCEPTED);
            priceStatusList.add(PoInvGrnDnMatchingPriceStatus.REJECTED);
        }
        
        return priceStatusList;
    }
    
    
    public List<PoInvGrnDnMatchingQtyStatus> getQtyStatusList()
    {
        List<PoInvGrnDnMatchingQtyStatus> qtyStatusList = new ArrayList<PoInvGrnDnMatchingQtyStatus>();
        
        if (getQtyStatus() == null)
        {
            qtyStatusList.add(PoInvGrnDnMatchingQtyStatus.PENDING);
            qtyStatusList.add(PoInvGrnDnMatchingQtyStatus.ACCEPTED);
            qtyStatusList.add(PoInvGrnDnMatchingQtyStatus.REJECTED);
        }
        
        return qtyStatusList;
    }
}
