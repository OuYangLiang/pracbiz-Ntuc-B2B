package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreUserHolder extends BaseHolder
{
    private static final long serialVersionUID = -400979777740500206L;
    private BigDecimal storeOid;
    private BigDecimal userOid;


    public BigDecimal getStoreOid()
    {
        return storeOid;
    }


    public void setStoreOid(BigDecimal storeOid)
    {
        this.storeOid = storeOid;
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
        return userOid.toString() + storeOid.toString();
    }

}
