package com.pracbiz.b2bportal.core.holder;

import java.math.BigInteger;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class ItemBarcodeHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private BigInteger itemOid;

    private String barcode;


    public BigInteger getItemOid()
    {
        return itemOid;
    }


    public void setItemOid(BigInteger itemOid)
    {
        this.itemOid = itemOid;
    }


    public String getBarcode()
    {
        return barcode;
    }


    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }


    @Override
    public String getCustomIdentification()
    {
        return itemOid + barcode;
    }

}