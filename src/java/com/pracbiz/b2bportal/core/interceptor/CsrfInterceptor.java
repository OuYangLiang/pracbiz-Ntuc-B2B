//*****************************************************************************
//
// File Name       :  CsrfIntercepter.java
// Date Created    :  Nov 20, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 20, 2013 4:31:07 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CsrfInterceptor extends AbstractInterceptor
{
    /**
     * 
     */
    private static final long serialVersionUID = -8513592914053570186L;

    public static final String CSRF_TOKEN = "csrfToken";
    
    public static final String CSRF_UNAUTHENTICATED = "csrf_unauthenticated";
    
    private static final Logger log = Logger.getLogger(CsrfInterceptor.class);
    
    @Override
    public String intercept(ActionInvocation invocation) throws Exception
    {
        String tokenValue = ServletActionContext.getRequest().getParameter(CsrfInterceptor.CSRF_TOKEN);
        
        if (null == tokenValue || !tokenValue.equals(ActionContext.getContext().getSession().get(CSRF_TOKEN)))
        {
            log.info("There is a CSRF(Cross-Site Request Forgery) connect ouccr at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:sss", Locale.US).format(new Date()));
            return CSRF_UNAUTHENTICATED;
        }
        
        return invocation.invoke();
    }

}
