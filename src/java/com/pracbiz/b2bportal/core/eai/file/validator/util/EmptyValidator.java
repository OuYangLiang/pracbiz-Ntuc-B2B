package com.pracbiz.b2bportal.core.eai.file.validator.util;

public class EmptyValidator implements FieldContentValidator
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String validate(String value, String desc)
    {
        if (null == value || value.trim().isEmpty())
        {
            return desc + " is empty";
        }

        return null;
    }

}
