package com.pracbiz.b2bportal.core.type.conversion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.conversion.TypeConversionException;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;


public class GlobalDateConversion extends StrutsTypeConverter
{
    protected static final Logger log = LoggerFactory.getLogger(GlobalDateConversion.class);
    @Autowired
    protected CustomAppConfigHelper appConfig;


    public Object convertFromString(Map context,
        String[] values, Class toType)
    {
        if(log.isDebugEnabled())
        {
            log.debug("For global date conversion, convertFromString called.");
        }

        String[] vs = (String[])values;
        if(vs == null || vs.length == 0)
        {
            return null;
        }

        Date toDate = null;
        
        try
        {
            String fromDate = vs[0];

            if(log.isDebugEnabled())
            {
                log.debug("convertFromString, in values=" + fromDate);
            }
            
            if(fromDate == null || fromDate.trim().isEmpty())
            {
                return null;
            }

            String dateFormat = appConfig.getSysDateInputFmt();
            DateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
            df.setLenient(false);
            toDate = df.parse(fromDate);
        }
        catch (Exception exception)
        {
            log.error("Convert date failed from string to date.", exception);
            throw new TypeConversionException(exception);
        }

        return toDate;
    }


    public String convertToString(Map context,
        Object object)
    {
        log.debug("For global date conversion, convertToString called.");
        String toString = null;
        try
        {
            if(object != null)
            {
                String dateFormat = appConfig.getSysDateDisplayFmt();
                DateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
                toString = df.format((Date)object);
            }
        }
        catch(Exception exception)
        {
            log.error("Convert date failed from date to string.", exception);
            throw new TypeConversionException(exception);
        }
        return toString;
    }

}