package com.pracbiz.b2bportal.core.eai.file.validator.util;

import java.math.BigDecimal;

public class NegativeValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    FieldContentValidator validator;
    private Boolean canBeZero;
    
    public NegativeValidator(boolean canBeZero)
    {
        this.canBeZero = canBeZero;
    }
    
    
    public NegativeValidator(boolean canBeZero, FieldContentValidator validator)
    {
        this.canBeZero = canBeZero;
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
            return desc + " must be a negative value.";
        }
        
        
        
        return null;
    }
    
    
    private boolean isValid(String value)
    {
        BigDecimal b = new BigDecimal(value.trim());
        
        if (canBeZero)
        {
            return b.compareTo(BigDecimal.ZERO) <= 0;
        }
        else
        {
            return b.compareTo(BigDecimal.ZERO) < 0;
        }
        
    }
    
}
