package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class SupplierSetHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = -4770353219013880548L;

    private BigDecimal setOid;

    private String setId;

    private String setDescription;
    
    private List<SupplierHolder> supplierList;
    


    public BigDecimal getSetOid()
    {
        return setOid;
    }


    public void setSetOid(BigDecimal setOid)
    {
        this.setOid = setOid;
    }


    public String getSetId()
    {
        return setId;
    }


    public void setSetId(String setId)
    {
        this.setId = setId;
    }


    public String getSetDescription()
    {
        return setDescription;
    }


    public void setSetDescription(String setDescription)
    {
        this.setDescription = setDescription;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(setOid);
    }


    public List<SupplierHolder> getSupplierList()
    {
        return supplierList;
    }


    public void setSupplierList(List<SupplierHolder> supplierList)
    {
        this.supplierList = supplierList;
    }
    
    
    public void addOidToSupplierList(BigDecimal supOid)
    {
        if (null == supplierList)
            supplierList = new LinkedList<SupplierHolder>();
        
        SupplierHolder obj = new SupplierHolder();
        obj.setSupplierOid(supOid);
        
        supplierList.add(obj);
    }


    @Override
    public String getLogicalKey()
    {
        return this.getSetId();
    }
}
