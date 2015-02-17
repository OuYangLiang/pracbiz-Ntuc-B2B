//*****************************************************************************
//
// File Name       :  TestFilter.java
// Date Created    :  Aug 31, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Aug 31, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.ntlm2.filter;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jcifs.Config;
import jcifs.http.NtlmHttpFilter;
import jcifs.ntlmssp.Type3Message;
import jcifs.util.Base64;
import net.sf.ehcache.CacheException;

import org.ntlmv2.liferay.NtlmManager;
import org.ntlmv2.liferay.util.HttpHeaders;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class GetClientUserNameFilter implements Filter, CoreCommonConstants
{
    private CustomAppConfigHelper appConfig;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException 
    {
        try
        {
            NtlmHttpFilter ntlmFilter = new NtlmHttpFilter();
            ntlmFilter.init(filterConfig);
            
            ApplicationContext application = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
            appConfig = (CustomAppConfigHelper)application.getBean("appConfig");
            
            String jcifsNetboisCachPolicy = appConfig.getJcifsCachePolicy();
            String jcifsSmbClientSoTimeOut = appConfig.getJcifsSmbSoTimeOut();
            Config.setProperty(JCIFS_NETBIOS_CACHE_POLICY, jcifsNetboisCachPolicy);
            Config.setProperty(JCIFS_NETBIOS_CACHE_POLICY, jcifsSmbClientSoTimeOut);
            
        } 
        catch(CacheException e) 
        {
            throw new ServletException("GetClientUserNameFilter filter initialization failed. Reason: " + e, e);
        }
    }
    
    /** 
     * {@inheritDoc}
     * @author jiangming
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException
    {
        
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String auth = httpRequest.getHeader(AUTHORIZATION);
        if (auth == null)
        {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setHeader("WWW-Authenticate", "NTLM");
            httpResponse.flushBuffer();
            return;
        }
        
        
        if (auth.startsWith("NTLM "))
        {
            byte[] msg =  Base64.decode(auth.substring(5));
            if (msg[8] == 1)
            {
                byte[] serverChallenge = new byte[8];
                SecureRandom secureRandom = new SecureRandom();
                secureRandom.nextBytes(serverChallenge);
                NtlmManager ntlmManager = this.getNtlmManager();
                byte[] challengeMessage = ntlmManager.negotiate(msg,
                        serverChallenge);
                auth = Base64.encode(challengeMessage);

                httpResponse.setContentLength(0);
                httpResponse.setHeader(HttpHeaders.WWW_AUTHENTICATE, "NTLM "
                        + auth);
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.flushBuffer();
                return;
            }
            else if (msg[8] == 3)
            {
                Type3Message type3Message = new Type3Message(msg);
                String username = type3Message.getUser();
                request.setAttribute(CLIENT_USER_NAME, username);
            }
        }
        
        httpResponse.sendRedirect(ACTION_AD_LOGIN);
        
    }
    
    protected NtlmManager getNtlmManager()
    {
        String domain = appConfig.getDomain();
        String domainController = appConfig.getDomainCtrl();
        String domainControllerName = appConfig.getDomainCtrlName();
        String serviceAccount = appConfig.getDomainServiceAccount();
        String serviceAccountPwd = appConfig.getDomainServiceAccountPwd();
    
        NtlmManager ntlmManager = new NtlmManager(domain, domainController,
            domainControllerName, serviceAccount, serviceAccountPwd);
        
        return ntlmManager;
    }


    public void destroy()
    {
        // To be implemented.
    }
    
}
