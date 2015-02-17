package com.pracbiz.b2bportal.core.eai.file.validator.util;

import java.math.BigDecimal;

public class IntegerValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    FieldContentValidator validator;


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

        if (value != null && !value.trim().isEmpty())
        {
            try
            {
                BigDecimal source = new BigDecimal(value.trim());
                
                if (source.compareTo(new BigDecimal(source.intValue())) != 0)
                {
                    return desc + " is not an integer.";
                }
            }
            catch(Exception e)
            {
                return desc + " is not an integer.";
            }
        }

        return null;
    }


    public IntegerValidator()
    {

    }


    public IntegerValidator(FieldContentValidator validator)
    {
        this.validator = validator;
    }

}
