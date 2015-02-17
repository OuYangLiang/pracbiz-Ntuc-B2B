package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class AllowedAccessCompanyHolder extends BaseHolder
{

    private static final long serialVersionUID = 1L;
    private BigDecimal userOid;
    private BigDecimal companyOid;
    
    
    public AllowedAccessCompanyHolder(BigDecimal userOid, BigDecimal companyOid)
    {
        this.userOid = userOid;
        this.companyOid = companyOid;
    }
    
    
    public AllowedAccessCompanyHolder()
    {
        
    }
    

    public BigDecimal getUserOid()
    {
        return userOid;
    }



    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }



    public BigDecimal getCompanyOid()
    {
        return companyOid;
    }



    public void setCompanyOid(BigDecimal companyOid)
    {
        this.companyOid = companyOid;
    }



    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(userOid + "" + companyOid);
    }

}
