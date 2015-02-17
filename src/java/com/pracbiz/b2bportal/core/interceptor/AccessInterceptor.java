package com.pracbiz.b2bportal.core.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;



public class AccessInterceptor extends AbstractInterceptor implements CoreCommonConstants
{
 	private static final long serialVersionUID = -2249361625032844646L;

    public static final String ACTION_RESTULT_AD_AUTH_FAILED = "adAuthFailed";
    public static final String ACTION_RESTULT_AD_LOGIN = "adLogin";
 	
    @Autowired transient private UserProfileService userProfileService;
    
    public String intercept(ActionInvocation invocation) throws Exception
    {       
        
        UserProfileHolder currentUser = (UserProfileHolder)ActionContext.getContext().getSession()
                .get(SESSION_KEY_USER);
        
        if (currentUser != null)
        {
            return invocation.invoke();
        }
        HttpServletRequest request = ServletActionContext.getRequest();
        String userName = (String)request.getAttribute(CLIENT_USER_NAME);
        UserProfileHolder user = userProfileService.getUserProfileByLoginId(userName);
        if (user != null && TYPE_AUTH_ID_AD.equals(user.getLoginMode()))
        {
            return ACTION_RESTULT_AD_LOGIN;
        }
        
        return ACTION_RESTULT_AD_AUTH_FAILED;
    }
    
}