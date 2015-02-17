package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;

public class BusinessRuleExHolder extends BusinessRuleHolder
{
    private static final long serialVersionUID = 41763724309587615L;
    private String ruleValue;
    private BigDecimal buyerOid;
    private Boolean valid;
    private BigDecimal numValue;
    private String stringValue;

    public String getRuleValue()
    {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue)
    {
        this.ruleValue = ruleValue;
    }

    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }

    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }

    public Boolean getValid()
    {
        return valid;
    }

    public void setValid(Boolean valid)
    {
        this.valid = valid;
    }

    public BigDecimal getNumValue()
    {
        return numValue;
    }

    public void setNumValue(BigDecimal numValue)
    {
        this.numValue = numValue;
    }

    public String getStringValue()
    {
        return stringValue;
    }

    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }

}
