package com.pracbiz.b2bportal.core.eai.file.validator.util;

public class LengthValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int maxLength;

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

        if ((value != null && !value.trim().isEmpty()) && 
        		value.trim().length() > maxLength)
        {
            return desc + " length is [" + value.trim().length()
                    + "], max length is [" + maxLength + "].";
        }
        
        return null;
    }


    public LengthValidator(int maxLength)
    {
        this.maxLength = maxLength;
    }


    public LengthValidator(int maxLength, FieldContentValidator validator)
    {
        this.maxLength = maxLength;
        this.validator = validator;
    }

}
