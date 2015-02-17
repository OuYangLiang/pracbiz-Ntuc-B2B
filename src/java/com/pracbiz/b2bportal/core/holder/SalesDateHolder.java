package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SalesDateHolder extends BaseHolder
{
    private static final long serialVersionUID = -759660964535342039L;

    private BigDecimal salesOid;
    private Integer lineSeqNo;
    private Date salesDate;

    public BigDecimal getSalesOid()
    {
        return salesOid;
    }

    public void setSalesOid(BigDecimal salesOid)
    {
        this.salesOid = salesOid;
    }

    public Integer getLineSeqNo()
    {
        return lineSeqNo;
    }

    public void setLineSeqNo(Integer lineSeqNo)
    {
        this.lineSeqNo = lineSeqNo;
    }

    public Date getSalesDate()
    {
        return salesDate == null ? null : (Date)salesDate.clone();
    }

    public void setSalesDate(Date salesDate)
    {
        this.salesDate = salesDate == null ? null : (Date)salesDate.clone();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(salesOid + "" + lineSeqNo);
    }
}