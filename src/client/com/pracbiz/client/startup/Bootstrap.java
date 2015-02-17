//*****************************************************************************
//
// File Name       :  Bootstrap.java
// Date Created    :  Feb 26, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Feb 26, 2013 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.startup;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.client.core.ClientEngine;
import com.pracbiz.client.utils.ClientCommand;
import com.pracbiz.client.utils.PidFileUtil;


/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public final class Bootstrap
{
    public static void main(String[] args) throws Exception
    {
        Bootstrap bs = Bootstrap.getInstance();
        
        if (args.length == 0 || !(bs.checkCmdArg(args[0]))) 
        {
            bs.showHelpInfo();
            System.exit(1);
            
            return;
        }
        
        ClientCommand cmdType = ClientCommand.valueOf(args[0].toLowerCase());
        
        switch(cmdType)
        {
            case start:
                bs.start();
                break;
            case stop:
                bs.stop();
                break;
            case restart:
                bs.restart();
                break;
            case pause:
                bs.pause();
                break;
            case resume:
                bs.resume();
                break;
            case help:
                bs.showHelpInfo();
                break;
            default:
                bs.showHelpInfo();
        }
    }
    
    
    //*****************************************************
    // singleton class
    //*****************************************************
    
    private static Bootstrap instance;
    private Bootstrap(){}
    
    
    private static final Logger log = Logger.getLogger(Bootstrap.class);
    
    public static Bootstrap getInstance()
    {
        synchronized(Bootstrap.class)
        {
            if (instance == null)
            {
                instance = new Bootstrap();
            }
        }
        
        return instance;
    }
    
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private boolean checkCmdArg(String arg) 
    {
        try
        {
            ClientCommand.valueOf(arg);
        }
        catch (Exception e)
        {
            return false;
        }
        
        return true;
    }
    
    
    private void showHelpInfo()
    {
        log.info("");
        log.info("Usage: startup.sh ( commands ... )");
        log.info("[start/stop/restart/help/resume/pause]");
        log.info("start     - start the client server");
        log.info("stop      - stop the client server");
        log.info("restart   - restart the client server");
        log.info("pause     - pause the client server");
        log.info("resume    - resume the client server");
        log.info("help      - print the help info\n");
    }


    private void start() throws Exception 
    {
        if (PidFileUtil.getInstance().isClientRunning())
        {
            log.error("Client is running, please stop it first.\n");
            return;
        }
        
        int tryTime = 3;
        
        while (PidFileUtil.getPidFile().exists())
        {
            if (tryTime < 1)
            {
                log.error("Failed to remove the pid file.");
                return;
            }
            
            try
            {
                PidFileUtil.getInstance().removePidFile();
            }
            catch (Exception e)
            {
                tryTime --;
                
                try
                {
                    Thread.sleep(2000);
                }
                catch(InterruptedException e1)
                {
                }
            }
        }
        
        PidFileUtil.getInstance().createPidFile();
        
        new ClientEngine(true);
        sendCommandToServer(ClientCommand.start.name());
        
        log.info("Client started.");
    }


    private void stop() throws Exception 
    {
        sendCommandToServer(ClientCommand.stop.name());
    }
    
    
    private void restart() throws Exception
    {
        this.stop();
        
        while(PidFileUtil.getPidFile().exists())
        {
            Thread.sleep(1000);
        }
        
        this.start();
    }

    
    private void pause() throws Exception
    {
        sendCommandToServer(ClientCommand.pause.name());
    }

    
    private void resume() throws Exception
    {
        sendCommandToServer(ClientCommand.resume.name());
    }
    
    
    public static void sendCommandToServer(String cmd) throws IOException
    {
        FileUtil.getInstance().appendLine(PidFileUtil.getPidFile(), cmd + "\r\n");
    }

}
