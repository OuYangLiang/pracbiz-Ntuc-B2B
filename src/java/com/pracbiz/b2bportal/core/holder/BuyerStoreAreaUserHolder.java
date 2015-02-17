package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreAreaUserHolder extends BaseHolder
{
    private static final long serialVersionUID = -5767226745641380058L;
    private BigDecimal areaOid;
    private BigDecimal userOid;


    public BigDecimal getAreaOid()
    {
        return areaOid;
    }


    public void setAreaOid(BigDecimal areaOid)
    {
        this.areaOid = areaOid;
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
        return userOid.toString() + areaOid .toString();
    }

}