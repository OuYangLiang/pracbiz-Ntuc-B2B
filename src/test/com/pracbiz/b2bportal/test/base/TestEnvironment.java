package com.pracbiz.b2bportal.test.base;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class TestEnvironment
{
    private static final Logger logger = Logger
        .getLogger(TestEnvironment.class);
    private static ApplicationContext ctx = null;
    private static final String TEST_CONFIG_FILE = "applicationContext.xml";
    private static TestEnvironment instance;
    
    private TestEnvironment(){}
    
    public static TestEnvironment getInstance()
    {
        synchronized(TestEnvironment.class)
        {
            if(instance == null)
            {
                instance = new TestEnvironment();
            }
        }

        return instance;
    }
    
    
    public ApplicationContext getApplicationContext()
    {
        synchronized(TestEnvironment.class)
        {
            if(ctx == null || !validCtx(ctx))
            {
                try
                {
                    ctx = new ClassPathXmlApplicationContext(TEST_CONFIG_FILE);
                }
                catch(Exception e)
                {
                    logger.error(e);
                    return null;
                }
            }
        }

        return ctx;
    }

    
    private boolean validCtx(ApplicationContext ctx)
    {
        try
        {
            return ctx.getBean("jdbcTemplate") != null;
        }
        catch(Exception e)
        {
            return false;
        }
    }

}
