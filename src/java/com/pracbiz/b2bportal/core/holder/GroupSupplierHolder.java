package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class GroupSupplierHolder extends BaseHolder
{
    private static final long serialVersionUID = -606227897926790941L;

    private BigDecimal groupOid;

    private BigDecimal supplierOid;


    public BigDecimal getGroupOid()
    {
        return groupOid;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    @Override
    public String getCustomIdentification()
    {
        return groupOid.toString() + supplierOid.toString();
    }

}