//*****************************************************************************
//
// File Name       :  RegexValidator.java
// Date Created    :  Jul 20, 2011
// Last Changed By :  $Author:GeJianKui $
// Last Changed On :  $Date:{date} $
// Revision        :  $Rev: $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2011.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator.util;


/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class RegexValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String regex;
    private FieldContentValidator validator;


    public RegexValidator(String  regex)
    {
        this.regex = regex;
    }


    public RegexValidator(FieldContentValidator validator, String regex)
    {
        this.regex = regex;
        this.validator = validator;
    }


    @Override
    public String validate(String value, String desc)
    {
        if (null != validator)
        {
            String rlt = validator.validate(value, desc);

            if (rlt != null)
            {
                return rlt;
            }
        }

        if ((value != null && !value.trim().isEmpty()) &&
        		!isValid(value))
        {
            return desc + " can not match " + regex;
        }
        return null;
    }


    public boolean isValid(String value)
    {
        return value.matches(regex);
    }
    
}
