package com.pracbiz.b2bportal.core.eai.file.validator.util;

public class NoSpaceValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FieldContentValidator validator;
    
    public NoSpaceValidator(){}
    
    public NoSpaceValidator(FieldContentValidator validator)
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
        		!isValid(value))
        {
            return desc + " can not contain space character";
        }
        
        return null;
    }
    
    public boolean isValid(String value)
    {
        return !value.trim().contains(" ");
    }

}
