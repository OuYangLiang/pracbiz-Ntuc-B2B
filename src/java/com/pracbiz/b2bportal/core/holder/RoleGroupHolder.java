package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class RoleGroupHolder extends BaseHolder
{
    private static final long serialVersionUID = 7942065625125804888L;

    private BigDecimal groupOid;
    private BigDecimal roleOid;


    public BigDecimal getGroupOid()
    {
        return groupOid;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }


    public BigDecimal getRoleOid()
    {
        return roleOid;
    }


    public void setRoleOid(BigDecimal roleOid)
    {
        this.roleOid = roleOid;
    }


    @Override
    public String getCustomIdentification()
    {
        return roleOid.toString() + groupOid.toString();
    }
}