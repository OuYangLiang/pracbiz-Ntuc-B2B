package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.GroupType;
import com.pracbiz.b2bportal.core.constants.RoleType;

public class UserTypeHolder extends BaseHolder
{
    private static final long serialVersionUID = -2081336931379148074L;

    private BigDecimal userTypeOid;

    private String userTypeId;

    private String userTypeDesc;

    private BigDecimal parentId;

    private GroupType groupType;

    private RoleType roleType;


    public BigDecimal getUserTypeOid()
    {
        return userTypeOid;
    }


    public void setUserTypeOid(BigDecimal userTypeOid)
    {
        this.userTypeOid = userTypeOid;
    }


    public String getUserTypeId()
    {
        return userTypeId;
    }


    public void setUserTypeId(String userTypeId)
    {
        this.userTypeId = userTypeId == null ? null : userTypeId.trim();
    }


    public String getUserTypeDesc()
    {
        return userTypeDesc;
    }


    public void setUserTypeDesc(String userTypeDesc)
    {
        this.userTypeDesc = userTypeDesc == null ? null : userTypeDesc.trim();
    }


    public BigDecimal getParentId()
    {
        return parentId;
    }


    public void setParentId(BigDecimal parentId)
    {
        this.parentId = parentId;
    }


    public GroupType getGroupType()
    {
        return groupType;
    }


    public void setGroupType(GroupType groupType)
    {
        this.groupType = groupType;
    }


    public RoleType getRoleType()
    {
        return roleType;
    }


    public void setRoleType(RoleType roleType)
    {
        this.roleType = roleType;
    }


    @Override
    public String getCustomIdentification()
    {
        return userTypeOid == null ? null : userTypeOid.toString();
    }
}