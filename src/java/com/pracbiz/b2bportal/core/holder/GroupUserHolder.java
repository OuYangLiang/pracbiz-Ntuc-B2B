package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class GroupUserHolder extends BaseHolder
{
    private static final long serialVersionUID = -1481781925605012016L;

    private BigDecimal groupOid;
    private BigDecimal userOid;


    public BigDecimal getGroupOid()
    {
        return groupOid;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
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
        return groupOid.toString() + userOid.toString();
    }
}