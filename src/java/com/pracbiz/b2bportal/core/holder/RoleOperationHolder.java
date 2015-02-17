package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class RoleOperationHolder extends BaseHolder
{
    private static final long serialVersionUID = -7527509368897804717L;

    private String opnId;

    private BigDecimal roleOid;


    public String getOpnId()
    {
        return opnId;
    }


    public void setOpnId(String opnId)
    {
        this.opnId = opnId == null ? null : opnId.trim();
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
        return roleOid == null ? null : roleOid.toString() + 
            this.getOpnId();
    }
}