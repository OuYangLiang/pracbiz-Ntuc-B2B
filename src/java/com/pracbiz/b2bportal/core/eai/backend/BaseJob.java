//*****************************************************************************
//
// File Name       :  BaseJob.java
// Date Created    :  Aug 26, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 26, 2013 3:29:34 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.backend;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.pracbiz.b2bportal.base.util.ErrorHelper;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public abstract class BaseJob implements Job
{
    private static final Logger log = LoggerFactory.getLogger(BaseJob.class);
    
    private static Map<String, Boolean> isRunning = new HashMap<String, Boolean>();
    private static ApplicationContext context;

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException
    {
        if (null != isRunning.get(this.getClass().getName()) &&
            isRunning.get(this.getClass().getName()).booleanValue())
        {
            return;
        }

        isRunning.put(this.getClass().getName(), true);

        try
        {
            if (null == context)
                context = (ApplicationContext)ctx.getScheduler().getContext().get("applicationContext");
            
            init();
            
            process();
        }
        catch(SchedulerException e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        finally
        {
            isRunning.put(this.getClass().getName(), false);
        }
    }
    
    
    protected abstract void init();
    
    
    protected abstract void process();
    
    
    protected <T> T getBean(String name, Class<T> t)
    {
        return context.getBean(name, t);
    }

}
