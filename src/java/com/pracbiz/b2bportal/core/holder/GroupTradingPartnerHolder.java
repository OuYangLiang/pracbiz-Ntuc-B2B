package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class GroupTradingPartnerHolder extends BaseHolder
{
    private static final long serialVersionUID = -7172453147121572533L;
    private BigDecimal groupOid;
    private BigDecimal tradingPartnerOid;


    public BigDecimal getGroupOid()
    {
        return groupOid;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }


    public BigDecimal getTradingPartnerOid()
    {
        return tradingPartnerOid;
    }


    public void setTradingPartnerOid(BigDecimal tradingPartnerOid)
    {
        this.tradingPartnerOid = tradingPartnerOid;
    }


    @Override
    public String getCustomIdentification()
    {
        return groupOid.toString() + tradingPartnerOid.toString();
    }



}