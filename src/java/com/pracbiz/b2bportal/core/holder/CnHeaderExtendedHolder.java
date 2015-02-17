package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class CnHeaderExtendedHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String fieldName;
    private BigDecimal cnOid;
    private String fieldType;
    private Integer intValue;
    private BigDecimal floatValue;
    private String stringValue;
    private Boolean boolValue;
    private Date dateValue;


    public String getFieldName()
    {
        return fieldName;
    }


    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }


    public BigDecimal getCnOid()
    {
        return cnOid;
    }


    public void setCnOid(BigDecimal cnOid)
    {
        this.cnOid = cnOid;
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
        return dateValue == null ? null : (Date) dateValue.clone();
    }


    public void setDateValue(Date dateValue)
    {
        this.dateValue = dateValue == null ? null : (Date) dateValue.clone();
    }


    @Override
    public String getCustomIdentification()
    {
        return fieldName + cnOid;
    }

}