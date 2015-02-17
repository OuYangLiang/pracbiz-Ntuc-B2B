package com.pracbiz.b2bportal.core.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserSessionHolder;
import com.pracbiz.b2bportal.core.service.LoginService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserSessionService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;


public class SessionListener implements HttpSessionListener, CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(SessionListener.class);
    
    public void sessionCreated(HttpSessionEvent event)
    {
        // TODO Auto-generated method stub
    }


    public void sessionDestroyed(HttpSessionEvent event)
    {
        String session_id = event.getSession().getId();
        
        if(session_id == null)
            return;
        
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getSession().getServletContext());
        
        UserSessionService userSessionService =  ctx.getBean("userSessionService",UserSessionService.class);
        UserProfileService userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
        LoginService loginService = ctx.getBean("loginService",LoginService.class);
       
        try
        {
            UserSessionHolder userSession = userSessionService.selectUserSessionByKey(session_id);
            if ( userSession == null)
            {
                return;
            }
            
            UserProfileHolder user = userProfileService.selectUserProfileByKey(userSession.getUserOid());
            if (user == null)
            {
                return;
            }
       
            CommonParameterHolder cp = (CommonParameterHolder)event.getSession().getAttribute(SESSION_KEY_COMMON_PARAM);
            
            loginService.doLogout(cp, user, session_id);
            userSessionService.deleteUserSession(userSession);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

    }

}
