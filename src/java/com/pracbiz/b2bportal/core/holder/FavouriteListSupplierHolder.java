package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class FavouriteListSupplierHolder extends BaseHolder
{
    private static final long serialVersionUID = 1L;

    private BigDecimal listOid;
    private BigDecimal supplierOid;


    public BigDecimal getListOid()
    {
        return listOid;
    }


    public void setListOid(BigDecimal listOid)
    {
        this.listOid = listOid;
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
        return listOid.toString() + supplierOid.toString();
    }

}
