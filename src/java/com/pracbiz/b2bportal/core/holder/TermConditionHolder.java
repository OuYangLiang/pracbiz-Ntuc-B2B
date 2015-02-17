package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class TermConditionHolder extends BaseHolder
{
    private static final long serialVersionUID = -8321184705580427713L;

    private BigDecimal termConditionOid;

    private Short seq;

    private String termConditionCode;

    private String termCondition1;

    private String termCondition2;

    private String termCondition3;

    private String termCondition4;

    private Boolean defaultSelected;

    private BigDecimal supplierOid;


    public BigDecimal getTermConditionOid()
    {
        return termConditionOid;
    }


    public void setTermConditionOid(BigDecimal termConditionOid)
    {
        this.termConditionOid = termConditionOid;
    }


    public String getTermConditionCode()
    {
        return termConditionCode;
    }


    public void setTermConditionCode(String termConditionCode)
    {
        this.termConditionCode = termConditionCode;
    }


    public Short getSeq()
    {
        return seq;
    }


    public void setSeq(Short seq)
    {
        this.seq = seq;
    }


    public String getTermCondition1()
    {
        return termCondition1;
    }


    public void setTermCondition1(String termCondition1)
    {
        this.termCondition1 = termCondition1 == null ? null : termCondition1
                .trim();
    }


    public String getTermCondition2()
    {
        return termCondition2;
    }


    public void setTermCondition2(String termCondition2)
    {
        this.termCondition2 = termCondition2 == null ? null : termCondition2
                .trim();
    }


    public String getTermCondition3()
    {
        return termCondition3;
    }


    public void setTermCondition3(String termCondition3)
    {
        this.termCondition3 = termCondition3 == null ? null : termCondition3
                .trim();
    }


    public String getTermCondition4()
    {
        return termCondition4;
    }


    public void setTermCondition4(String termCondition4)
    {
        this.termCondition4 = termCondition4 == null ? null : termCondition4
                .trim();
    }


    public Boolean getDefaultSelected()
    {
        return defaultSelected;
    }


    public void setDefaultSelected(Boolean defaultSelected)
    {
        this.defaultSelected = defaultSelected;
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
        return termConditionOid == null ? null : termConditionOid.toString();
    }
}