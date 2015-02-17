package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SalesHeaderExtendedHolder extends BaseHolder
{

    private static final long serialVersionUID = 9131224786150642256L;

    private BigDecimal salesOid;
    private String fieldName;
    private String fieldType;
    private Integer intValue;
    private BigDecimal floatValue;
    private String stringValue;
    private Boolean boolValue;
    private Date dateValue;

    public BigDecimal getSalesOid()
    {
        return salesOid;
    }

    public void setSalesOid(BigDecimal salesOid)
    {
        this.salesOid = salesOid;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public String getFieldType()
    {
        return fieldType;
    }

    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }

    public Integer getIntValue()
    {
        return intValue;
    }

    public void setIntValue(Integer intValue)
    {
        this.intValue = intValue;
    }

    public BigDecimal getFloatValue()
    {
        return floatValue;
    }

    public void setFloatValue(BigDecimal floatValue)
    {
        this.floatValue = floatValue;
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

    public Date getDateValue()
    {
        return dateValue == null ? null : (Date)dateValue.clone();
    }

    public void setDateValue(Date dateValue)
    {
        this.dateValue = dateValue == null ? null : (Date)dateValue.clone();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(salesOid + "" + fieldName);
    }
}