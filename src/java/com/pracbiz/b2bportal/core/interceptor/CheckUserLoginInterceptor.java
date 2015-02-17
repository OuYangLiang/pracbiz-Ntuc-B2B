package com.pracbiz.b2bportal.core.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.RequestUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;



public class CheckUserLoginInterceptor extends AbstractInterceptor implements CoreCommonConstants
{
 	private static final long serialVersionUID = -2249361625032844646L;

    public static final String ACTION_RESTULT_GET_USER_FROM_CLIENT="getUserName";
    public static final String ACTION_RESTULT_LOST_LOGIN = "lostLogin";
    public static final String ACTION_RESTULT_HOME = "home";
    
    
    @Autowired transient private CustomAppConfigHelper appConfig;
 	
    public String intercept(ActionInvocation invocation) throws Exception
    {       
        
        UserProfileHolder currentUser = (UserProfileHolder)ActionContext.getContext().getSession()
                .get(SESSION_KEY_USER);
        
        if (currentUser != null)
        {
            return invocation.invoke();
        }
        
        if (appConfig.autoLoginEnable())
        {
            return ACTION_RESTULT_GET_USER_FROM_CLIENT;
        }
        
        HttpServletRequest request = ServletActionContext.getRequest();
        String uri = RequestUtils.getServletPath((HttpServletRequest)request);
        if (StringUtils.startsWithIgnoreCase(uri, "/access"))
        {
            return ACTION_RESTULT_HOME;
        }

        return ACTION_RESTULT_LOST_LOGIN;
    }
    
}