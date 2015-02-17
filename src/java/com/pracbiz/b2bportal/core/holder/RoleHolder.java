package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.RoleType;

public class RoleHolder extends BaseHolder
{
    private static final long serialVersionUID = -3474267461816379179L;

    private BigDecimal roleOid;

    private String roleId;

    private String roleName;

    private RoleType roleType;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;
    
    private Boolean createdFromSysadmin;

    private BigDecimal userTypeOid;

    private BigDecimal buyerOid;
    
    private List<RoleOperationHolder> roleOperations;
    
    private List<SupplierRoleHolder> supplierRoles;


    public BigDecimal getRoleOid()
    {
        return roleOid;
    }


    public void setRoleOid(BigDecimal roleOid)
    {
        this.roleOid = roleOid;
    }


    public String getRoleId()
    {
        return roleId;
    }


    public void setRoleId(String roleId)
    {
        this.roleId = roleId == null ? null : roleId.trim();
    }


    public String getRoleName()
    {
        return roleName;
    }


    public void setRoleName(String roleName)
    {
        this.roleName = roleName == null ? null : roleName.trim();
    }


    public RoleType getRoleType()
    {
        return roleType;
    }


    public void setRoleType(RoleType roleType)
    {
        this.roleType = roleType;
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


    public List<RoleOperationHolder> getRoleOperations()
    {
        return roleOperations;
    }


    public void setRoleOperations(List<RoleOperationHolder> roleOperations)
    {
        this.roleOperations = roleOperations;
    }
    
    
    public List<SupplierRoleHolder> getSupplierRoles()
    {
        return supplierRoles;
    }


    public void setSupplierRoles(List<SupplierRoleHolder> supplierRoles)
    {
        this.supplierRoles = supplierRoles;
    }


    public Boolean getCreatedFromSysadmin()
    {
        return createdFromSysadmin;
    }


    public void setCreatedFromSysadmin(Boolean createdFromSysadmin)
    {
        this.createdFromSysadmin = createdFromSysadmin;
    }


    @Override
    public String getCustomIdentification()
    {
        return roleOid == null ? null : roleOid.toString();
    }


    @Override
    public String getLogicalKey()
    {
        return roleId;
    }
}