package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class RoleUserHolder extends BaseHolder
{
    private static final long serialVersionUID = 2496440156834489625L;

    private BigDecimal roleOid;
    private BigDecimal userOid;


    public BigDecimal getRoleOid()
    {
        return roleOid;
    }


    public void setRoleOid(BigDecimal roleOid)
    {
        this.roleOid = roleOid;
    }


    public BigDecimal getUserOid()
    {
        return userOid;
    }


    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }


    @Override
    public String getCustomIdentification()
    {
        return roleOid.toString() + userOid.toString();
    }
}