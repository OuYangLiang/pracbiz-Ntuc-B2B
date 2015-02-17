package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class GiHeaderExtendedHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String fieldName;

    private BigDecimal giOid;
    private String filedType;

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


    public BigDecimal getGiOid()
    {
        return giOid;
    }


    public void setGiOid(BigDecimal giOid)
    {
        this.giOid = giOid;
    }


    public String getFiledType()
    {
        return filedType;
    }


    public void setFiledType(String filedType)
    {
        this.filedType = filedType;
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
        // TODO Auto-generated method stub
        return null;
    }

}