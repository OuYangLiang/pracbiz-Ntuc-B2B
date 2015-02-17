package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;

public class PoLocationDetailExtendedHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = 7528381363120526836L;

    private String fieldName;

    private BigDecimal poOid;
    
    private Integer detailLineSeqNo;

    private Integer locationLineSeqNo;

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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public BigDecimal getPoOid() {
        return poOid;
    }

    public void setPoOid(BigDecimal poOid) {
        this.poOid = poOid;
    }

    public Integer getLocationLineSeqNo() {
        return locationLineSeqNo;
    }

    public void setLocationLineSeqNo(Integer locationLineSeqNo) {
        this.locationLineSeqNo = locationLineSeqNo;
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

    public Integer getDetailLineSeqNo()
    {
        return detailLineSeqNo;
    }

    public void setDetailLineSeqNo(Integer detailLineSeqNo)
    {
        this.detailLineSeqNo = detailLineSeqNo;
    }

    public String toStringWithDelimiterCharacter(String delimiterChar)
    {
        StringBuffer buffer = new StringBuffer();
        //field-name
        buffer.append(fieldName).append(delimiterChar);
        
        //field-type
        buffer.append(fieldType).append(delimiterChar);
        
        //field-value
        if (boolValue != null)
        {
            buffer.append(boolValue.toString().toUpperCase());
        }

        if (stringValue != null)
        {
            buffer.append(stringValue);
        }

        if (floatValue != null)
        {
            buffer.append(floatValue);
        }

        if (intValue != null)
        {
            buffer.append(intValue);
        }
        
        if(dateValue != null)
        {
            buffer.append(DateUtil.getInstance().convertDateToString(dateValue,
                DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(poOid + "" + locationLineSeqNo + "" + fieldName);
    }
}