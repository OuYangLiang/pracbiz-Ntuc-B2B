package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;
import java.util.Date;

public class DoHeaderExtendedHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -5990196421115187731L;

    private BigDecimal doOid;

    private String fieldName;

    private String fieldType;

    private Integer intValue;

    private BigDecimal floatValue;

    private String stringValue;

    private Boolean boolValue;
    
    private Date dateValue;
    
    public Date getDateValue()
    {
        return dateValue == null ? null : (Date) dateValue.clone();
    }

    public void setDateValue(Date dateValue)
    {
        this.dateValue = dateValue == null ? null : (Date) dateValue.clone();
    }

    public BigDecimal getDoOid() {
        return doOid;
    }

    public void setDoOid(BigDecimal doOid) {
        this.doOid = doOid;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType == null ? null : fieldType.trim();
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public BigDecimal getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(BigDecimal floatValue) {
        this.floatValue = floatValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue == null ? null : stringValue.trim();
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(doOid + "" + fieldName);
    }
}