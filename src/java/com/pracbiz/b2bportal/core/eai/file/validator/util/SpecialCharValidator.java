//*****************************************************************************
//
// File Name       :  ContainSpecialCharValidator.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming_2009
 */
public class SpecialCharValidator implements FieldContentValidator
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> allowed;
    private boolean caseSensitive;
    private FieldContentValidator validator;
    
    public SpecialCharValidator(boolean caseSensitive, String...values)
    {
        allowed = new ArrayList<String>();
        
        for (String value : values)
        {
            if (caseSensitive)
            {
                allowed.add(value);
            }
            else
            {
                allowed.add(value.toUpperCase());
            }
        }
        
        this.caseSensitive = caseSensitive;
    }
    
    
    public SpecialCharValidator(FieldContentValidator validator, boolean caseSensitive, String...values)
    {
        allowed = new ArrayList<String>();
        
        for (String value : values)
        {
            if (caseSensitive)
            {
                allowed.add(value);
            }
            else
            {
                allowed.add(value.toUpperCase());
            }
        }
        
        this.caseSensitive = caseSensitive;
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
            return desc + " must be one of " + allowed.toString();
        }
        return null;
    }

    
    public boolean isValid(String value)
    {
        if (caseSensitive)
        {
            return allowed.contains(value);
        }
        else
        {
            return allowed.contains(value.toUpperCase());
        }
    }
}
