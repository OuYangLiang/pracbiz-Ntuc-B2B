//*****************************************************************************
//
// File Name       :  DateJsonValueProcessor.java
// Date Created    :  Apr 11, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Apr 11, 2013 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class DateJsonValueProcessor implements JsonValueProcessor
{
    private DateFormat sf;
    private String format = "yyyy-MM-dd HH:mm:ss";  
    
    public DateJsonValueProcessor()  
    {  
        // Default constructor.
    }  
  
    public DateJsonValueProcessor(String format)  
    {  
  
        this.format = format;  
    }  
  
    public Object processArrayValue(Object value, JsonConfig jsonConfig)  
    {  
  
        String[] obj = {};  
        if (value instanceof Date[])  
        {  
            sf = new SimpleDateFormat(format, Locale.US);
            Date[] dates = (Date[]) value;  
            obj = new String[dates.length];  
            for (int i = 0; i < dates.length; i++)  
            {  
                obj[i] = sf.format(dates[i]);  
            }  
        }  
        return obj;  
    }  
  
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig)  
    {  
  
        if (value instanceof Date)  
        {  
            return new SimpleDateFormat(format, Locale.US).format((Date) value);  
        }  
        return value;  
    }  
  
    public String getFormat()  
    {  
  
        return format;  
    }  
  
    public void setFormat(String format)  
    {  
  
        this.format = format;  
    }  
}
