package com.pracbiz.b2bportal.core.interceptor;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.pracbiz.b2bportal.core.holder.OperationUrlHolder;
import com.pracbiz.b2bportal.core.service.OperationUrlService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class AuthorityInterceptor extends AbstractInterceptor implements CoreCommonConstants
{   
    private static final long serialVersionUID = -3349206335812348987L;
    
    @Autowired transient OperationUrlService operationUrlService;
    
    private static List<String> allUrls = new ArrayList<String>();
    
    @SuppressWarnings("unchecked")
    public String intercept(ActionInvocation invocation) throws Exception
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        
        ActionContext ctx=invocation.getInvocationContext();
        List<String> permitUrlList =(List<String>)ctx.getSession().get(SESSION_KEY_PERMIT_URL);
        
        if (permitUrlList==null)
        {
            permitUrlList = new ArrayList<String>();
        }
        
        String uri = StringUtils.remove(request.getRequestURI(), request.getContextPath());
        
        if (this.getAllUrls().contains(uri) && !permitUrlList.contains(uri))
        {
            return "noPermit";
        }
        
        return invocation.invoke();
    }
    
    private List<String> getAllUrls() throws Exception
    {
        if(allUrls.isEmpty())
        {
            List<OperationUrlHolder> rlt = operationUrlService
                .select(new OperationUrlHolder());

            if(null != rlt && !rlt.isEmpty())
            {
                for(OperationUrlHolder ou : rlt)
                {
                    allUrls.add(ou.getAccessUrl());
                }
            }

        }

        return allUrls;
    }

}
