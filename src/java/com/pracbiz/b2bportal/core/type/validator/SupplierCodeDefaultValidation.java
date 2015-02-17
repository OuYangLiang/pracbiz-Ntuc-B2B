package com.pracbiz.b2bportal.core.type.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;

public class SupplierCodeDefaultValidation extends FieldValidatorSupport
{
    private static final String VLD_PTN_KEY_SUPPLIER_CODE = "SUPPLIER_CODE";
    @Autowired
    private ValidationConfigHelper validationConfig;
    private boolean trim;


    public SupplierCodeDefaultValidation()
    {
        trim = true;
    }


    public void validate(Object object) throws ValidationException
    {
        String fieldName = getFieldName();
        Object fieldValue = getFieldValue(fieldName, object);
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
                .getCachePattern(VLD_PTN_KEY_SUPPLIER_CODE));
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches())
        {
            addFieldError(fieldName, object);
        }
    }


    public void setTrim(boolean trim)
    {
        this.trim = trim;
    }


    public boolean isTrim()
    {
        return trim;
    }
}
