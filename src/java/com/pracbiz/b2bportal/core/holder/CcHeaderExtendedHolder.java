//*****************************************************************************
//
// File Name       :  CcHeaderExtendedHolder.java
// Date Created    :  Dec 23, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 23, 2013 5:35:44 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CcHeaderExtendedHolder extends BaseHolder 
{

    private static final long serialVersionUID = -4860900122961637623L;

    private BigDecimal invOid;

    private String fieldName;

    private String fieldType;

    private Integer intValue;

    private BigDecimal floatValue;

    private String stringValue;

    private Boolean boolValue;
    
    private Date dateValue;
    
    
    public BigDecimal getInvOid()
    {
        return invOid;
    }


    public void setInvOid(BigDecimal invOid)
    {
        this.invOid = invOid;
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


    public String toStringWithDelimiterCharacter(String delimiterChar)
    {
        StringBuffer buffer = new StringBuffer();
        //field-name
        buffer.append(fieldName).append(delimiterChar);
        
        //field-type
        buffer.append(fieldType).append(delimiterChar);
        
        //field-value
        if(boolValue != null)
        {
            buffer.append(boolValue.toString().toUpperCase());
        }

        if(stringValue != null)
        {
            buffer.append(stringValue);
        }

        if (floatValue != null)
        {
            buffer.append(floatValue);
        }

        if (intValue!= null)
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
        return String.valueOf(invOid+""+fieldName);
    }

}
