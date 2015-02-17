//*****************************************************************************
//
// File Name       :  PidFile.java
// Date Created    :  Jun 20, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Jun 20, 2013 4:26:37 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;


import com.pracbiz.b2bportal.base.util.FileUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public final class PidFileUtil
{
    private static final String PID_FILENAME = "client.pid";

    public static File getPidFile()
    {
        File userDir = new File(System.getProperty("user.dir"));
        String clientPid = userDir.getParent() + File.separator + "conf"
            + File.separator + PID_FILENAME;
        return new File(clientPid);
    }
    
    
    public static boolean isPidfileExist()
    {
        return getPidFile().exists();
    }
    
    
    public void createPidFile() throws IOException
    {
        if (isPidfileExist())
        {
            return;
        }
        
        if (!getPidFile().getParentFile().isDirectory())
        {
            FileUtil.getInstance().createDir(getPidFile().getParentFile());
        }
        
        if (!getPidFile().createNewFile())
        {
            throw new IOException("Failed to create pidfile.");
        }
    }
    
    
    public void removePidFile() throws IOException
    {
        if (!isPidfileExist())
        {
            return;
        }
        
        if (!getPidFile().delete())
        {
            throw new IOException("Failed to delete pid file.");
        }
    }
    
    
    public boolean isClientRunning() throws IOException
    {
        InputStream input = null;
        String line = null;
        int num = 0;
        
        try
        {
            Process p = Runtime.getRuntime().exec("jps -l");
            input = p.getInputStream();
            LineIterator itr = IOUtils.lineIterator(input, "utf-8");
            
            while(itr.hasNext())
            {
                line = itr.nextLine();
                
                if (line.indexOf("com.pracbiz.client.startup.Bootstrap") != -1)
                {
                    num ++;
                }
                
                if (num > 1)
                {
                    return true;
                }
            }
        }
        finally
        {
            if (null != input)
            {
                input.close();
                input = null;
            }
        }
        
        return false;
    }
    
    //*****************************************************
    // singleton class
    //*****************************************************
    
    private static PidFileUtil instance;
    private PidFileUtil(){}
    
    public static PidFileUtil getInstance()
    {
        synchronized(PidFileUtil.class)
        {
            if (instance == null)
            {
                instance = new PidFileUtil();
            }
        }
        
        return instance;
    }
    
}
