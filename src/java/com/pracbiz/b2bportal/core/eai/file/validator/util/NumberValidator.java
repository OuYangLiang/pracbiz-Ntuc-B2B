//*****************************************************************************
//
// File Name       :  BigDecimalValidator.java
// Date Created    :  Jul 18, 2011
// Last Changed By :  $Author: $
// Last Changed On :  $Date: $
// Revision        :  $Rev: $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2011.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator.util;

import java.math.BigDecimal;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming_2009
 */
public class NumberValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FieldContentValidator validator;
    
    
    public NumberValidator(){}
    
    public NumberValidator(FieldContentValidator validator)
    {
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
        		!this.isValid(value))
        {
            return desc + " is not a valid number.";
        }

        return null;
    }
    
    
    private boolean isValid(String value)
    {
        
        try
        {
            new BigDecimal(value);
        }
        catch (Exception e)
        {
            return false;
        }
        
        return true;
    }

}
