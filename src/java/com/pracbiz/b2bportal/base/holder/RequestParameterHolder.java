package com.pracbiz.b2bportal.base.holder;

import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;

public class RequestParameterHolder implements Serializable
{
    private static final long serialVersionUID = 4245870039525277916L;
    
    private String paramKey;
    private String paramValue;


    public RequestParameterHolder()
    {
     // Default Constructor
    }


    public String getParamKey()
    {
        return paramKey;
    }


    public void setParamKey(String paramKey)
    {
        this.paramKey = paramKey;
    }


    public String getParamValue()
    {
        return paramValue;
    }


    public void setParamValue(String paramValue)
    {
        this.paramValue = paramValue;
    }
    
    
    public String toString()
    {
        try
        {
            return BeanUtils.describe(this).toString();
        }
        catch (Exception exception)
        {
            return exception.getMessage();
        }
    }
}
