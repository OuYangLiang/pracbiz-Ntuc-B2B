package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.GroupType;

public class GroupHolder extends BaseHolder
{
    private static final long serialVersionUID = -2912945262057611305L;

    private BigDecimal groupOid;

    private String groupId;

    private String groupName;

    private GroupType groupType;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;

    private BigDecimal userTypeOid;

    private BigDecimal buyerOid;

    private BigDecimal supplierOid;
    
    private List<? extends RoleGroupHolder> roleGroups;
    
    private List<? extends GroupUserHolder> groupUsers;
    
    private List<? extends GroupTradingPartnerHolder> groupTradingPartners;
    
    private List<? extends GroupSupplierHolder> groupSuppliers;

    public BigDecimal getGroupOid()
    {
        return groupOid;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }


    public String getGroupId()
    {
        return groupId;
    }


    public void setGroupId(String groupId)
    {
        this.groupId = groupId == null ? null : groupId.trim();
    }


    public String getGroupName()
    {
        return groupName;
    }


    public void setGroupName(String groupName)
    {
        this.groupName = groupName == null ? null : groupName.trim();
    }


    public GroupType getGroupType()
    {
        return groupType;
    }


    public void setGroupType(GroupType groupType)
    {
        this.groupType = groupType;
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date)createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date)createDate.clone();
    }


    public String getCreateBy()
    {
        return createBy;
    }


    public void setCreateBy(String createBy)
    {
        this.createBy = createBy == null ? null : createBy.trim();
    }


    public Date getUpdateDate()
    {
        return updateDate == null ? null : (Date)updateDate.clone();
    }


    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate == null ? null : (Date)updateDate.clone();
    }


    public String getUpdateBy()
    {
        return updateBy;
    }


    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }


    public BigDecimal getUserTypeOid()
    {
        return userTypeOid;
    }


    public void setUserTypeOid(BigDecimal userTypeOid)
    {
        this.userTypeOid = userTypeOid;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    /**
     * Getter of roleGroups.
     * @return Returns the roleGroups.
     */
    public List<? extends RoleGroupHolder> getRoleGroups()
    {
        return roleGroups;
    }


    /**
     * Setter of roleGroups.
     * @param roleGroups The roleGroups to set.
     */
    public void setRoleGroups(List<? extends RoleGroupHolder> roleGroups)
    {
        this.roleGroups = roleGroups;
    }


    /**
     * Getter of groupUsers.
     * @return Returns the groupUsers.
     */
    public List<? extends GroupUserHolder> getGroupUsers()
    {
        return groupUsers;
    }


    /**
     * Setter of groupUsers.
     * @param groupUsers The groupUsers to set.
     */
    public void setGroupUsers(List<? extends GroupUserHolder> groupUsers)
    {
        this.groupUsers = groupUsers;
    }


    /**
     * Getter of groupTradingPartners.
     * @return Returns the groupTradingPartners.
     */
    public List<? extends GroupTradingPartnerHolder> getGroupTradingPartners()
    {
        return groupTradingPartners;
    }


    /**
     * Setter of groupTradingPartners.
     * @param groupTradingPartners The groupTradingPartners to set.
     */
    public void setGroupTradingPartners(
        List<? extends GroupTradingPartnerHolder> groupTradingPartners)
    {
        this.groupTradingPartners = groupTradingPartners;
    }


    /**
     * Getter of groupSuppliers.
     * @return Returns the groupSuppliers.
     */
    public List<? extends GroupSupplierHolder> getGroupSuppliers()
    {
        return groupSuppliers;
    }


    /**
     * Setter of groupSuppliers.
     * @param groupSuppliers The groupSuppliers to set.
     */
    public void setGroupSuppliers(List<? extends GroupSupplierHolder> groupSuppliers)
    {
        this.groupSuppliers = groupSuppliers;
    }


    @Override
    public String getCustomIdentification()
    {
        return groupOid == null ? null : groupOid.toString();
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return groupId;
    }

}