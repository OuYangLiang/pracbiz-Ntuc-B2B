package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class UserTypeOperationHolder extends BaseHolder
{
    private static final long serialVersionUID = 3774012138809777092L;

    private String opnId;

    private BigDecimal userTypeOid;


    public String getOpnId()
    {
        return opnId;
    }


    public void setOpnId(String opnId)
    {
        this.opnId = opnId == null ? null : opnId.trim();
    }


    public BigDecimal getUserTypeOid()
    {
        return userTypeOid;
    }


    public void setUserTypeOid(BigDecimal userTypeOid)
    {
        this.userTypeOid = userTypeOid;
    }


    @Override
    public String getCustomIdentification()
    {
        return userTypeOid == null ? null : userTypeOid.toString() + opnId;
    }
}