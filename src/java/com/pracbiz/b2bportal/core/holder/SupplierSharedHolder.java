package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SupplierSharedHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private BigDecimal selfSupOid;
    private BigDecimal grantSupOid;
    
    

    public BigDecimal getSelfSupOid()
    {
        return selfSupOid;
    }



    public void setSelfSupOid(BigDecimal selfSupOid)
    {
        this.selfSupOid = selfSupOid;
    }



    public BigDecimal getGrantSupOid()
    {
        return grantSupOid;
    }



    public void setGrantSupOid(BigDecimal grantSupOid)
    {
        this.grantSupOid = grantSupOid;
    }



    @Override
    public String getCustomIdentification()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
