package com.pracbiz.b2bportal.core.advice;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.ehcache.Cache;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class MethodCacheAfterAdvice implements AfterReturningAdvice,
    InitializingBean
{
    private static final Logger LOG = Logger
        .getLogger(MethodCacheAfterAdvice.class);
    private Cache cache;

    public void setCache(Cache cache)
    {
        this.cache = cache;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        Assert.notNull(cache,
            "Need a cache. Please use setCache(Cache) create it.");
    }

    @Override
    public void afterReturning(Object arg0, Method arg1, Object[] arg2,
        Object arg3) throws Throwable
    {

        String className = arg3.getClass().getName();
        String key = className.replace("Mapper", "ServiceImpl");
        LOG.info("Start to remove result from cache by key - " + key);
        List<?> list = cache.getKeys();
        for(int i = 0; i < list.size(); i++)
        {
            String cacheKey = String.valueOf(list.get(i));
            if(cacheKey.startsWith(key))
            {
                cache.remove(cacheKey);
                LOG.info("remove cache " + cacheKey);
            }
        }
        
        LOG.info("End to remove result from cache by key - " + key);
    }

}
