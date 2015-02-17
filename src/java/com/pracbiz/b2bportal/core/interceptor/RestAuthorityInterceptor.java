package com.pracbiz.b2bportal.core.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.RestErrorHolder;
import com.pracbiz.b2bportal.core.holder.RestResponseHolder;
import com.pracbiz.b2bportal.core.service.RestRequestAuthenticationService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class RestAuthorityInterceptor extends AbstractInterceptor implements
        CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(RestAuthorityInterceptor.class);
    private static final long serialVersionUID = 1L;
    public static final String VALUE_SUCCESS = "successful";
    public static final String VALUE_OK = "ok";

    @Autowired
    private transient RestRequestAuthenticationService restRequestAuthenticationService;


    public String intercept(ActionInvocation invocation) throws Exception
    {
        RestResponseHolder restResHolder = new RestResponseHolder();
        List<RestErrorHolder> errorList = null;
        String responseData = null;
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        try
        {
            errorList = restRequestAuthenticationService.doRestAuthentication(
                    request, restResHolder);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            
            return null;
        }
        
        if (errorList == null || errorList.isEmpty())
        {
            return invocation.invoke();
        }
        
        restResHolder.setErrorList(errorList);
        responseData = restResHolder.toJsonString();
        restRequestAuthenticationService.sendResponseToClient(response,
            restResHolder.getHttpStatusCode(), responseData);
        
        return null;
    }
}
