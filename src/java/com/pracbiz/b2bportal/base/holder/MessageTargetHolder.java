package com.pracbiz.b2bportal.base.holder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

public class MessageTargetHolder implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String targetURI;
    private String targetBtnTitle;
    private List<RequestParameterHolder> parameters = null;


    public MessageTargetHolder()
    {
        // Default Constructor
    }


    public MessageTargetHolder(String name, String uri)
    {
        targetBtnTitle = name;
        targetURI = uri;
    }


    public void addRequestParam(String paramKey_, String paramValue_)
    {
        if (parameters == null)
            parameters = new ArrayList<RequestParameterHolder>();

        RequestParameterHolder param_ = new RequestParameterHolder();
        param_.setParamKey(paramKey_);
        param_.setParamValue(paramValue_);

        this.parameters.add(param_);
    }


    public List<RequestParameterHolder> getParameters()
    {
        return parameters;
    }


    public void setParameters(List<RequestParameterHolder> parameters)
    {
        this.parameters = parameters;
    }


    public String getTargetURI()
    {
        return targetURI;
    }


    public void setTargetURI(String targetURI)
    {
        this.targetURI = targetURI;
    }


    public String getTargetBtnTitle()
    {
        return targetBtnTitle;
    }


    public void setTargetBtnTitle(String targetBtnTitle)
    {
        this.targetBtnTitle = targetBtnTitle;
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
