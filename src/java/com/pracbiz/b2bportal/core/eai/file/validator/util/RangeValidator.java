//*****************************************************************************
//
// File Name       :  RangValidator.java
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
public class RangeValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FieldContentValidator validator;
    private Number max;
    private Number min;


    public RangeValidator(Number min, Number max)
    {
        this.max = max;
        this.min = min;
    }


    public RangeValidator(Number min, Number max, FieldContentValidator validator)
    {
        this.validator = validator;
        this.max = max;
        this.min = min;
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
            return desc + " is " + value + ", it must be between [" + min + "] and ["
                    + max + "].";
        }
        
        return null;
    }


    private boolean isValid(String value)
    {
        BigDecimal decimal = new BigDecimal(value);

        return (decimal.doubleValue() >= min.doubleValue() && decimal
                .doubleValue() <= max.doubleValue());
    }

}
