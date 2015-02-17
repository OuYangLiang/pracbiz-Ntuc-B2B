package com.pracbiz.b2bportal.core.interceptor;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.pracbiz.b2bportal.base.action.BaseAction;

public class MethodCacheInterceptor implements MethodInterceptor,
    InitializingBean
{
    private static final Logger log = LoggerFactory.getLogger(MethodInterceptor.class);
    private Cache cache;

    public void setCache(Cache cache)
    {
        this.cache = cache;
    }

    public MethodCacheInterceptor()
    {
        super();
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        Assert.notNull(cache,
            "Need a cache. Please use setCache(Cache) create it.");
    }

    /** 
     * Intercept the Service/DAO's method, 
     * and search the result whether exist in cache,
     * if true will return result from cache else get result from database
     * and set the result to cache. 
     */ 
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        Object[] arguments = invocation.getArguments();
        Object result;

        log.info("Find object from cache is " + cache.getName());

        String cacheKey = getCacheKey(targetName, methodName, arguments);
        Element element = cache.get(cacheKey);

        if(element == null)
        {
            log.info("Hold up method , Get method result and create cache with key is - [" + cacheKey + "].");
            result = invocation.proceed();
            element = new Element(cacheKey, (Serializable)result);
            cache.put(element);
        }
        
        return element.getValue();
    }
    
    /** 
     * get the key of the cache, the key is unique Element in cache.
     * the cache key include package name + class name + method name
     * eg:com.pracbiz.b2bportal.core.service.impl
     * .SummaryFieldServiceImpl.getSummaryFieldsByPageIdAndAccessType() 
     */ 
    private String getCacheKey(String targetName, String methodName,
        Object[] arguments)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(targetName).append('.').append(methodName);
        if((arguments != null) && (arguments.length != 0))
        {
            for(int i = 0; i < arguments.length; i++)
            {
                if (arguments[i] instanceof BaseAction)
                {
                    continue;
                }
                sb.append('.').append(arguments[i]);
            }
        }
        return sb.toString();
    }

}
