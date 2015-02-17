package com.pracbiz.b2bportal.core.eai.file.validator.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FieldContentValidator validator;
    private String datePattern;
    
    
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
        		!this.isValid(value, datePattern, true))
        {
            return desc + " is invalid, the correct format is '"
                    + datePattern + "'";
        }

        return null;
    }
    
    
    public DateValidator(String datePattern)
    {
        this.datePattern = datePattern;
    }
    
    
    public DateValidator(String datePattern, FieldContentValidator validator)
    {
        this.datePattern = datePattern;
        this.validator = validator;
    }

    /**
     * <p>Checks if the field is a valid date.  The pattern is used with
     * <code>java.text.SimpleDateFormat</code>.  If strict is true, then the
     * length will be checked so '2/12/1999' will not pass validation with
     * the format 'MM/dd/yyyy' because the month isn't two digits.
     * The setLenient method is set to <code>false</code> for all.</p>
     *
     * @param value The value validation is being performed on.
     * @param datePattern The pattern passed to <code>SimpleDateFormat</code>.
     * @param strict Whether or not to have an exact match of the datePattern.
     * @return true if the date is valid.
     */
    public boolean isValid(String value, String datePattern, boolean strict)
    {

        if (value == null || datePattern == null || datePattern.length() <= 0)
        {
            return false;
        }

        if (strict && (datePattern.length() != value.length()))
        {
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        formatter.setLenient(false);

        try
        {
            formatter.parse(value);
        }
        catch (ParseException e)
        {
            return false;
        }

        return true;
    }

}
