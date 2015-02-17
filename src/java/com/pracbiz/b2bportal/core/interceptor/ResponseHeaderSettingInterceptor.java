package com.pracbiz.b2bportal.core.interceptor;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ResponseHeaderSettingInterceptor extends AbstractInterceptor
{
    private static final long serialVersionUID = 1900080485624298720L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception
    {
        HttpServletResponse resp = ServletActionContext.getResponse();
        
        resp.setHeader("Cache-Control", "private,no-cache,no-store,max-age=0");
        resp.setHeader("Pragma", "no-cache");
        
        return invocation.invoke();
    }

}
