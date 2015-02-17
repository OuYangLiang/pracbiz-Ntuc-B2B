package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class MsgTransactionsExHolder extends MsgTransactionsHolder
{
    private static final long serialVersionUID = -5400110866089931183L;
    private Date createDateFrom;
    private Date createDateTo;
    private Date sentDateFrom;
    private Date sentDateTo;
    private Date receivedDate;
    private Date receivedDateFrom;
    private Date receivedDateTo;
    private List<BigDecimal> supplierOids;
    private List<BigDecimal> buyerOids;
    private BigDecimal userTypeOid;
    private List<String> buyerStoreAccessList;
    private List<BigDecimal> accessCompanyOids;
    private List<BigDecimal> supplierSharedOids;
    
    private BigDecimal currentUserType;
    private BigDecimal currentUserOid;
    private BigDecimal currentUserBuyerOid;
    private BigDecimal currentUserSupplierOid;
    private BigDecimal currentUserGroupOid;
    private Boolean visiable;
    private Boolean fullGroupPriv;
    
    private BigDecimal selectBuyerOid;
    private BigDecimal selectSupplierOid;
    
    private String beforeHour;
    private boolean isAllSupplier;
    private BigDecimal groupOid;
    private BigDecimal listOid;
    private Boolean validSupplierSet;
    private BigDecimal setOid;
    
    private boolean allStoreFlag;
    
    private Boolean fullClassPriv;
    

    public List<BigDecimal> getBuyerOids()
    {
        return buyerOids;
    }

    public void setBuyerOids(List<BigDecimal> buyerOids)
    {
        this.buyerOids = buyerOids;
    }

    public List<BigDecimal> getSupplierOids()
    {
        return supplierOids;
    }

    public void setSupplierOids(List<BigDecimal> supplierOids)
    {
        this.supplierOids = supplierOids;
    }

    public Date getCreateDateFrom()
    {
        return createDateFrom == null ? null : (Date)createDateFrom.clone();
    }

    public void setCreateDateFrom(Date createDateFrom)
    {
        this.createDateFrom = createDateFrom == null ? null : (Date)createDateFrom.clone();
    }

    public Date getCreateDateTo()
    {
        return createDateTo == null ? null : (Date)createDateTo.clone();
    }

    public void setCreateDateTo(Date createDateTo)
    {
        this.createDateTo = createDateTo == null ? null : (Date)createDateTo.clone();
    }

    public Date getSentDateFrom()
    {
        return sentDateFrom == null ? null : (Date)sentDateFrom.clone();
    }

    public void setSentDateFrom(Date sentDateFrom)
    {
        this.sentDateFrom = sentDateFrom == null ? null : (Date)sentDateFrom.clone();
    }

    public Date getSentDateTo()
    {
        return sentDateTo == null ? null : (Date)sentDateTo.clone();
    }

    public void setSentDateTo(Date sentDateTo)
    {
        this.sentDateTo = sentDateTo == null ? null : (Date)sentDateTo.clone();
    }

    public Date getReceivedDateFrom()
    {
        return receivedDateFrom == null ? null : (Date)receivedDateFrom.clone();
    }

    public void setReceivedDateFrom(Date receivedDateFrom)
    {
        this.receivedDateFrom = receivedDateFrom == null ? null : (Date)receivedDateFrom.clone();
    }

    public Date getReceivedDateTo()
    {
        return receivedDateTo == null ? null : (Date)receivedDateTo.clone();
    }

    public void setReceivedDateTo(Date receivedDateTo)
    {
        this.receivedDateTo = receivedDateTo == null ? null : (Date)receivedDateTo.clone();
    }
    
    @JSON(format=CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getReceivedDate()
    {
        return receivedDate == null ? null : (Date)receivedDate.clone();
    }

    public void setReceivedDate(Date receivedDate)
    {
        this.receivedDate = receivedDate == null ? null : (Date)receivedDate.clone();
    }

    public BigDecimal getUserTypeOid()
    {
        return userTypeOid;
    }

    public void setUserTypeOid(BigDecimal userTypeOid)
    {
        this.userTypeOid = userTypeOid;
    }

    public List<String> getBuyerStoreAccessList()
    {
        return buyerStoreAccessList;
    }

    public void setBuyerStoreAccessList(List<String> buyerStoreAccessList)
    {
        this.buyerStoreAccessList = buyerStoreAccessList;
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

    public BigDecimal getSelectBuyerOid()
    {
        return selectBuyerOid;
    }

    public void setSelectBuyerOid(BigDecimal selectBuyerOid)
    {
        this.selectBuyerOid = selectBuyerOid;
    }

    public BigDecimal getSelectSupplierOid()
    {
        return selectSupplierOid;
    }

    public void setSelectSupplierOid(BigDecimal selectSupplierOid)
    {
        this.selectSupplierOid = selectSupplierOid;
    }

    public String getBeforeHour()
    {
        return beforeHour;
    }

    public void setBeforeHour(String beforeHour)
    {
        this.beforeHour = beforeHour;
    }

    public boolean isAllSupplier()
    {
        return isAllSupplier;
    }

    public void setAllSupplier(boolean isAllSupplier)
    {
        this.isAllSupplier = isAllSupplier;
    }

    public BigDecimal getGroupOid()
    {
        return groupOid;
    }

    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }

    public BigDecimal getListOid()
    {
        return listOid;
    }

    public void setListOid(BigDecimal listOid)
    {
        this.listOid = listOid;
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

    public boolean isAllStoreFlag()
    {
        return allStoreFlag;
    }

    public void setAllStoreFlag(boolean allStoreFlag)
    {
        this.allStoreFlag = allStoreFlag;
    }

    public Boolean getFullClassPriv()
    {
        return fullClassPriv;
    }

    public void setFullClassPriv(Boolean fullClassPriv)
    {
        this.fullClassPriv = fullClassPriv;
    }

    public List<BigDecimal> getAccessCompanyOids()
    {
        return accessCompanyOids;
    }

    public void setAccessCompanyOids(List<BigDecimal> accessCompanyOids)
    {
        this.accessCompanyOids = accessCompanyOids;
    }
    
    public void addAccessCompanyOids(BigDecimal accessCompanyOid)
    {
        if (this.accessCompanyOids == null)
        {
            accessCompanyOids = new ArrayList<BigDecimal>();
        }
        
        accessCompanyOids.add(accessCompanyOid);
    }

}
