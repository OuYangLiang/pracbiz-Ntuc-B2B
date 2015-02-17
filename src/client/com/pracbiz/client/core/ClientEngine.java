//*****************************************************************************
//
// File Name       :  ClientEngine.java
// Date Created    :  Jun 20, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Jun 20, 2013 4:47:59 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.core;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.client.utils.ClientCommand;
import com.pracbiz.client.utils.FileChangeListener;
import com.pracbiz.client.utils.FileMonitor;
import com.pracbiz.client.utils.PidFileUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class ClientEngine implements FileChangeListener
{
    private static final Logger log = LoggerFactory.getLogger(ClientEngine.class);
    
    private long pos = 0;
    private final ApplicationContext context = getContext();
    
    
    public ClientEngine(boolean listenPidFile) throws IOException
    {
        if (listenPidFile)
            FileMonitor.getInstance().addFileChangeListener(this, PidFileUtil.getPidFile(), 100);
    }
    
    //*****************************************************
    // public methods
    //*****************************************************
    
    public void start()
    {
        try
        {
            this.getScheduler().start();
        }
        catch(SchedulerException e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    public void stop()
    {
        try
        {
            this.getScheduler().shutdown(true);
        }
        catch(SchedulerException e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        
        try
        {
            PidFileUtil.getInstance().removePidFile();
        }
        catch(IOException e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    public void pause()
    {
        try
        {
            this.getScheduler().pauseAll();
        }
        catch(SchedulerException e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    public void resume()
    {
        try
        {
            this.getScheduler().resumeAll();
        }
        catch(SchedulerException e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    //*****************************************************
    // implemented methods
    //*****************************************************

    @Override
    public void fileChanged(File file)
    {
        ClientCommand cmd = null;
        RandomAccessFile raf = null;
        String command = null;
        
        try
        {
            raf = new RandomAccessFile(file, "r");
            raf.seek(pos);
            
            command = raf.readLine();
            
            while(null != command)
            {
                log.info( "Command [" + command + "] detected...");
                
                try
                {
                    cmd = ClientCommand.valueOf(command);
                }
                catch (IllegalArgumentException e)
                {
                    log.error("Un-recognized command [" + command + "].");
                    ErrorHelper.getInstance().logError(log, e);
                }
                
                switch(cmd)
                {
                    case start:
                        start();
                        break;
                    case stop:
                        stop();
                        System.exit(0);
                        break;
                    case pause:
                        this.pause();
                        break;
                    case resume:
                        this.resume();
                        break;
                    case restart:
                    	break;
                    case help:
                    	break;
                    default:
                    	break;
                }
                
                command = raf.readLine();
            }
            
            pos = raf.length();
        }
        catch(IOException e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        finally
        {
            try
            {
                if (null != raf)
                {
                	raf.close();
                	raf = null;
                }
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
        }
    }
    
    
    //*****************************************************
    // getter methods
    //*****************************************************
    
    public ApplicationContext getContext()
    {
        if (context != null)
        {
            return context;
        }

        return new ClassPathXmlApplicationContext("applicationContext.xml");
    }
    
    
    public Scheduler getScheduler()
    {
        return this.getContext().getBean("scheduler", Scheduler.class);
    }

}
