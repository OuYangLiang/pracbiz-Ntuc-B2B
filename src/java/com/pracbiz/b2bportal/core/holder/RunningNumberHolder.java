package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class RunningNumberHolder extends BaseHolder
{
    private static final long serialVersionUID = 4848654404356639203L;

    private String numberType;
    private Integer maxNumber;
    private BigDecimal companyOid;

    public String getNumberType()
    {
        return numberType;
    }

    public void setNumberType(String numberType)
    {
        this.numberType = numberType;
    }

    public Integer getMaxNumber()
    {
        return maxNumber;
    }

    public void setMaxNumber(Integer maxNumber)
    {
        this.maxNumber = maxNumber;
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
        return companyOid == null ? null : companyOid.toString();
    }

}
