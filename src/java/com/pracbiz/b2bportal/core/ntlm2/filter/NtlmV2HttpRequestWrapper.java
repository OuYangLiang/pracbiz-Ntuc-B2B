package com.pracbiz.b2bportal.core.ntlm2.filter;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class NtlmV2HttpRequestWrapper extends HttpServletRequestWrapper
{

    /** Stores the NTLM principal holder. */
    private final Principal userPrincipal;


    /**
     * Creates a request wrapper instance.
     * 
     * @param request The wrapped HTTP request.
     */
    public NtlmV2HttpRequestWrapper(HttpServletRequest request, String userName)
    {
        super(request);
        userPrincipal = new NtlmV2Principal(userName);
    }


    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServletRequestWrapper#getRemoteUser()
     */
    @Override
    public String getRemoteUser()
    {
        if (this.userPrincipal == null)
        {
            return super.getRemoteUser();
        }
        return userPrincipal.getName();
    }


    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServletRequestWrapper#getUserPrincipal()
     */
    @Override
    public Principal getUserPrincipal()
    {
        if (this.userPrincipal == null)
        {
            return super.getUserPrincipal();
        }
        return this.userPrincipal;
    }

}
