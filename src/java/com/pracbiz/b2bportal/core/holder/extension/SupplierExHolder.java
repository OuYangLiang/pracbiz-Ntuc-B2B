package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.SupplierHolder;

public class SupplierExHolder extends SupplierHolder
{
    private static final long serialVersionUID = 1L;

    private String ctryDesc;
    private String currDesc;
    private List<BigDecimal> supplierOids;
    private String gstPercentStr;
    private String startNumberStr;
    private BigDecimal buyerOid;
    private BigDecimal currentUserTypeOid;
    private BigDecimal currentUserSupplierOid;
    private BigDecimal currentUserOid;
    
    public String getCtryDesc()
    {
        return ctryDesc;
    }

    
    public void setCtryDesc(String ctryDesc)
    {
        this.ctryDesc = ctryDesc;
    }

    
    public String getCurrDesc()
    {
        return currDesc;
    }

    
    public void setCurrDesc(String currDesc)
    {
        this.currDesc = currDesc;
    }


    public List<BigDecimal> getSupplierOids()
    {
        return supplierOids;
    }


    public void setSupplierOids(List<BigDecimal> supplierOids)
    {
        this.supplierOids = supplierOids;
    }

    
    public void addSupplierOid(BigDecimal supplierOid)
    {
        if(supplierOids == null)
        {
            supplierOids = new ArrayList<BigDecimal>();
        }

        supplierOids.add(supplierOid);
    }

    
    public String getGstPercentStr()
    {
        return gstPercentStr;
    }

    
    public void setGstPercentStr(String gstPercentStr)
    {
        this.gstPercentStr = gstPercentStr;
    }

    
    public String getStartNumberStr()
    {
        return startNumberStr;
    }

    
    public void setStartNumberStr(String startNumberStr)
    {
        this.startNumberStr = startNumberStr;
    }

    
    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }

    
    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }

    
    public BigDecimal getCurrentUserTypeOid()
    {
        return currentUserTypeOid;
    }

    
    public void setCurrentUserTypeOid(BigDecimal currentUserTypeOid)
    {
        this.currentUserTypeOid = currentUserTypeOid;
    }

    
    public BigDecimal getCurrentUserSupplierOid()
    {
        return currentUserSupplierOid;
    }

    
    public void setCurrentUserSupplierOid(BigDecimal currentUserSupplierOid)
    {
        this.currentUserSupplierOid = currentUserSupplierOid;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((getSupplierOid() == null) ? 0 : getSupplierOid().hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        SupplierHolder other = (SupplierHolder)obj;
        if(getSupplierOid() == null)
        {
            if(other.getSupplierOid() != null)
                return false;
        }
        else if(!getSupplierOid().equals(other.getSupplierOid()))
            return false;
        return true;
    }


    public BigDecimal getCurrentUserOid()
    {
        return currentUserOid;
    }


    public void setCurrentUserOid(BigDecimal currentUserOid)
    {
        this.currentUserOid = currentUserOid;
    }
    
}
