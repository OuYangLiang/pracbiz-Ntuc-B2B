package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class BuyerRuleHolder extends BaseHolder
{
    private static final long serialVersionUID = 4666738260461126345L;
    
    private BigDecimal ruleOid;
    private BigDecimal buyerOid;
    private BigDecimal numValue;
    private String stringValue;
    private Boolean boolValue;


    public BigDecimal getRuleOid()
    {
        return ruleOid;
    }


    public void setRuleOid(BigDecimal ruleOid)
    {
        this.ruleOid = ruleOid;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
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


    public Boolean getBoolValue()
    {
        return boolValue;
    }


    public void setBoolValue(Boolean boolValue)
    {
        this.boolValue = boolValue;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(buyerOid + "" + ruleOid);
    }

}