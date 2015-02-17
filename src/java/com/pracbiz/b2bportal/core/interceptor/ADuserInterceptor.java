package com.pracbiz.b2bportal.core.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.ntlmv2.liferay.NtlmUserAccount;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;



public class ADuserInterceptor extends AbstractInterceptor implements CoreCommonConstants
{
 	private static final long serialVersionUID = -2249361625032844646L;

    public static final String ACTION_RESTULT_AD_LOGIN = "adLogin";
    public static final String ACTION_RESTULT_LOST_LOGIN = "lostLogin";
 	
    @Autowired transient private UserProfileService userProfileService;
    
    public String intercept(ActionInvocation invocation) throws Exception
    {   
        UserProfileHolder currentUser = (UserProfileHolder)ActionContext
            .getContext().getSession().get(SESSION_KEY_USER);

        if(currentUser != null)
        {
            return invocation.invoke();
        }
        
        
        HttpServletRequest request = ServletActionContext.getRequest();
        NtlmUserAccount ntlmUser = (NtlmUserAccount)request.getSession().getAttribute(NTLM_USER_ACCOUNT);
        
        if (ntlmUser != null)
        {
            return invocation.invoke();
        }
        
        
        String userName = (String)request.getAttribute(CLIENT_USER_NAME);
        
        if (StringUtils.isNotBlank(userName))
        {
            UserProfileHolder user = userProfileService.getUserProfileByLoginId(userName);
            
            if (user != null && TYPE_AUTH_ID_AD.equals(user.getLoginMode()))
            {
                return ACTION_RESTULT_AD_LOGIN;
            }
        }
        
        return ACTION_RESTULT_LOST_LOGIN;
        
    }
    
}