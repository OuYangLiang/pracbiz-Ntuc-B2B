//*****************************************************************************
//
// File Name       :  GroupIdDefaultValidation.java
// Date Created    :  Sep 26, 2011
// Last Changed By :  $Author: $
// Last Changed On :  $Date: $
// Revision        :  $Rev: $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2011.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.type.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;

public class GroupIdDefaultValidation extends FieldValidatorSupport
{
    private static final String VLD_PTN_KEY_GROUP_ID = "GROUP_ID";
    @Autowired
    private ValidationConfigHelper validationConfig;
    private boolean trim;


    public GroupIdDefaultValidation()
    {
        trim = true;
    }


    public void validate(Object obj) throws ValidationException
    {
        String fieldName = getFieldName();
        Object fieldValue = getFieldValue(fieldName, obj);
        if (fieldValue == null || fieldValue.toString().trim().length() == 0)
        {
            return;
        }
        String value = (String) fieldValue;
        if (trim)
        {
            value = value.trim();
        }

        Pattern pattern = Pattern.compile(validationConfig
                .getCachePattern(VLD_PTN_KEY_GROUP_ID));
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches())
        {
            addFieldError(fieldName, obj);
        }
    }


    public boolean isTrim()
    {
        return trim;
    }


    public void setTrim(boolean trim)
    {
        this.trim = trim;
    }

}
