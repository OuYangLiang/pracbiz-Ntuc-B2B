package com.pracbiz.b2bportal.core.interceptor;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsConstants;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.extension.HelpInfoExHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class InitSessionInterceptor extends AbstractInterceptor implements CoreCommonConstants
{
    private static final long serialVersionUID = -7130767874478979301L;
    
    private static final String LAYOUT_THEME = "layoutTheme";
    private static final String DEFAULT_LAYOUTTHEME = "claro";

    private static final String STRUTS_UI_THEME = "theme";
    private static final String DEFAULT_STRUTS_UI_THEME = "custom-default";
    
    private String strutsUiTheme;
    
    private static final String DEV_MODE = "devMode";
    private boolean devMode = false;
    
    
    private static final String DOJO_LOCALE = "dojoLocale";
    
    @Autowired transient private ControlParameterService controlParameterService;


    public String intercept(ActionInvocation invocation) throws Exception
    {
        Map<String, Object> session = ActionContext.getContext().getSession();
        HttpServletRequest request = ServletActionContext.getRequest();
        
        
        // Init theme for jsp pages.
        if (!session.containsKey(LAYOUT_THEME))
        {
            session.put(LAYOUT_THEME, DEFAULT_LAYOUTTHEME);
        }


        // Init theme for struts tags.
        if (!session.containsKey(STRUTS_UI_THEME))
        {
            if (strutsUiTheme == null)
            {
                session.put(STRUTS_UI_THEME, DEFAULT_STRUTS_UI_THEME);
            }
            else
            {
                session.put(STRUTS_UI_THEME, strutsUiTheme);
            }
        }


        // Init dev mode
        
        if (!session.containsKey(DEV_MODE))
        {
            session.put(DEV_MODE, devMode);
        }
        
        
        // Init locale
        if (null == request.getSession().getAttribute(CoreCommonConstants.SESSION_KEY_STRUTS_LOCALE))
        {
            // Default locale for struts
            request.getSession().setAttribute(CoreCommonConstants.SESSION_KEY_STRUTS_LOCALE, Locale.US);
        }
        
        Locale locale = (Locale)request.getSession().getAttribute(CoreCommonConstants.SESSION_KEY_STRUTS_LOCALE);
        
        String language = locale.getLanguage().trim().toLowerCase();
        
        if (language.startsWith("en"))
        {
            session.put(DOJO_LOCALE, "en-us");
        }
        else if (language.startsWith("zh"))
        {
            session.put(DOJO_LOCALE, "zh-cn");
        }
        
        
        if (!session.containsKey(SESSION_KEY_HELP_INFO))
        {
        	HelpInfoExHolder helpInfo = new HelpInfoExHolder();
            ControlParameterHolder helpNo = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL,
                            PARAM_ID_HELPDESK_NO);
            helpInfo.setHelpNo(helpNo.getStringValue());
            
            ControlParameterHolder helpEmail = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL,
                            PARAM_ID_HELPDESK_EMAIL);
            
            helpInfo.setHelpEmail(helpEmail.getStringValue());
            
            session.put(SESSION_KEY_HELP_INFO, helpInfo);
        }
        
        return invocation.invoke();
    }


    @Inject(StrutsConstants.STRUTS_DEVMODE)
    public void setDevMode(String mode)
    {
        this.devMode = "true".equals(mode);
    }


    @Inject(StrutsConstants.STRUTS_UI_THEME)
    public void setDefaultTheme(String theme)
    {
        this.strutsUiTheme = theme;
    }
}
