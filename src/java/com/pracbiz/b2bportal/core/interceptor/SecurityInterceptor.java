package com.pracbiz.b2bportal.core.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class SecurityInterceptor extends AbstractInterceptor
{

    /**
     * 
     */
    private static final long serialVersionUID = -1528573807435316880L;
    private static final String NOT_YOUR_OID = "noPermit";
    @Autowired
    private transient CustomAppConfigHelper appConfig;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception
    {
        Map<String, Object> session = invocation.getInvocationContext().getSession();
        HttpServletRequest request = ServletActionContext.getRequest();
        List<SecurityInterceptorHelpHolder> methods = appConfig.getSecurityInterceptorMethods();
        if (methods == null || methods.isEmpty())
        {
            return invocation.invoke();
        }
        for (SecurityInterceptorHelpHolder method : methods)
        {
            if (!request.getRequestURI().equalsIgnoreCase(request.getContextPath() + method.getName()))
            {
                continue;
            }
            @SuppressWarnings("unchecked")
            List<String> oidList = (List<String>) session.get(method.getModuleKey());
            if (oidList == null || oidList.isEmpty())
            {
                return NOT_YOUR_OID;
            }
            String[] parts = null;
            String separator = method.getSeparator() == null ? "," : method.getSeparator();
            
            //for po module
            if ((request.getContextPath() + "/po/putParamIntoSession.action").equalsIgnoreCase(request.getRequestURI()))
            {
                parts = handlePoOid(request.getParameter(method.getParam()).split(separator));
            }
            else if ((request.getContextPath() + "/poInvGrnDnMatching/putParamIntoSession.action").equalsIgnoreCase(request.getRequestURI()))
            {
                parts = handleMatchingOid(request.getParameter(method.getParam()).split(separator));
            }
            else
            {
                parts = request.getParameter(method.getParam()).split(separator);
            }
            for (String part : parts)
            {
                if (!oidList.contains(part.replaceAll(" ", "")))
                {
                    return NOT_YOUR_OID;
                }
            }
                
            return invocation.invoke();
        }
        return invocation.invoke();
    }
    
    
    private String[] handlePoOid(String[] parts)
    {
        String[] result = new String[parts.length];
        for (int i = 0; i < parts.length; i++)
        {
            result[i] = parts[i].split("/")[0];
        }
        return result;
    }
    
    private String[] handleMatchingOid(String[] parts)
    {
        String[] result = new String[parts.length];
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].split(",").length == 1)
            {
                result[i] = parts[i];
            }
            else
            {
                result[i] = parts[i].split(",")[1];
            }
        }
        return result;
    }

}
