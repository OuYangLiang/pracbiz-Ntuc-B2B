package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.SupplierSetHolder;

public class SupplierSetExHolder extends SupplierSetHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = -1625119595183181764L;
    private List<BigDecimal> supplierOidList;
    private String supplierListString;


    public List<BigDecimal> getSupplierOidList()
    {
        return supplierOidList;
    }


    public void setSupplierOidList(List<BigDecimal> supplierOidList)
    {
        this.supplierOidList = supplierOidList;
    }

    
    public String getSupplierListString()
    {
        return supplierListString;
    }


    public void setSupplierListString(String supplierListString)
    {
        this.supplierListString = supplierListString;
    }


    public void addSupplierOid(BigDecimal supplierOid)
    {
        if (this.getSupplierOidList() == null)
        {
            this.setSupplierOidList(new ArrayList<BigDecimal>());
        }
        this.getSupplierOidList().add(supplierOid);
    }
}
