//*****************************************************************************
//
// File Name       :  DispersionJob.java
// Date Created    :  Aug 4, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 4, 2013 10:46:10 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.core.jobs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.client.utils.ClientConfigHelper;
import com.pracbiz.client.utils.MailboxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class DispersionJob extends QuartzJobBean
{
private static final Logger log = LoggerFactory.getLogger(DispersionJob.class);
    
    private static boolean isRunning = false;
    
    private ClientConfigHelper clientConfig;
    private MailboxUtil mboxUtil;
    

    @Override
    protected void executeInternal(JobExecutionContext arg0)
        throws JobExecutionException
    {
        if(isRunning)
        {
            return;
        }
        
        isRunning = true;
        
        try
        {
            synchronized(DownloadJob.lock)
            {
                while (DownloadJob.anyJobProcessing)
                {
                    try
                    {
                        DownloadJob.lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
                
                DownloadJob.anyJobProcessing = true;
            }
            
            process();
        }
        finally
        {
            synchronized (DownloadJob.lock)
            {
                DownloadJob.anyJobProcessing = false;

                DownloadJob.lock.notifyAll();
            }
            
            isRunning = false;
        }
    }

    
    private void process()
    {
        if (!clientConfig.autoDispersion())
            return;
        
        try
        {
            FileUtil.getInstance().createDir(new File(clientConfig.getDispersionPrivateIn()));
            FileUtil.getInstance().createDir(new File(clientConfig.getDispersionPrivateOut()));
            FileUtil.getInstance().createDir(new File(clientConfig.getDispersionDoc()));
        }
        catch(IOException e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            return;
        }
        
        
        log.info("Start to process.");
        
        moveSeparateFilesToPrivateIn();
        
        movePrivateOutFilesToSeparateMailboxes();
        
        log.info("Process ended.");
    }
    
    
    private void moveSeparateFilesToPrivateIn()
    {
        List<String> serverIds = clientConfig.getAllServerId();
        
        for (String serverId : serverIds)
        {
            List<String> mboxIds = clientConfig.getMboxIds(serverId.trim());
            
            for (String mboxId : mboxIds)
            {
                log.info("Start to process for mailbox [" + mboxId + "] for portal [" + serverId + "].");
                
                this.moveSeparateFilesToPrivateIn(serverId.trim(), mboxId.trim());
                
                log.info("Mailbox [" + mboxId + "] for portal [" + serverId + "] process ended.");
            }
        }
    }
    
    
    private void moveSeparateFilesToPrivateIn(final String serverId, String mboxId) 
    {
        File privateInDir = mboxUtil.getPrivateInPath(serverId, mboxId);
        
        File[] files = privateInDir.listFiles();

        File[] stableFiles = FileUtil.getInstance().getStableFiles(files);
        
        if (stableFiles == null || stableFiles.length <= 0)
        {
            log.info("No stable file found in [" + privateInDir.getPath() + "]");
            
            return;
        }

        for (File doc : stableFiles)
        {
            log.info("File [" + doc.getName() + "] detected, move it to the dispersion folder.");
            
            File docPath = new File(clientConfig.getDispersionDoc());
            File inPath  = new File(clientConfig.getDispersionPrivateIn());
            
            
            if (doc.getName().toUpperCase(Locale.US).endsWith(".PDF"))
            {
                if (!doc.renameTo(new File(docPath, doc.getName())))
                {
                    log.error("Failed to move file [" + doc.getName() + "] to [" + docPath.getPath() + "] folder.");
                    
                    continue;
                }
            }
            else
            {
                if (!doc.renameTo(new File(inPath, doc.getName())))
                {
                    log.error("Failed to move file [" + doc.getName() + "] to [" + inPath.getPath() + "] folder.");
                    
                    continue;
                }
            }
            
            log.info("File [" + doc.getName() + "] moved to the dispersion folder successfully.");
        }
        
    }
    
    
    private void movePrivateOutFilesToSeparateMailboxes()
    {
        File privOut = new File(clientConfig.getDispersionPrivateOut());
        
        File[] files = privOut.listFiles();
        
        File[] stableFiles = FileUtil.getInstance().getStableFiles(files);
        
        if (stableFiles == null || stableFiles.length <= 0)
        {
            log.info("No stable file found in [" + privOut.getPath() + "]");
            
            return;
        }
        
        for (File file : stableFiles)
        {
            String filename = file.getName();
            
            String buyerCode = null;
            String supplCode = null;
            
            try
            {
                String[] parts = filename.split("_");
                buyerCode = parts[1];
                supplCode = parts[2];
            }
            catch (Exception e)
            {
                log.error("Failed to parse buyer code and supplier code from filename [" + file.getName() + "].");
                ErrorHelper.getInstance().logError(log, e);
                
                continue;
            }
            
            
            Map<String, String> mboxMap = clientConfig.getMboxIdViaBuyerCodeAndBuyerGivenSupplierCode(buyerCode, supplCode);
            File targetPath = mboxUtil.getPrivateOutPath(mboxMap.get("portailId"), mboxMap.get("mailbox"));
            
            log.info("Moving file [" + file.getName() + "] to [" + targetPath.getPath() + "].");
            
            if (!file.renameTo(new File(targetPath, file.getName())))
            {
                log.error("Failed to move file [" + file.getName() + "] to [" + targetPath.getPath() + "].");
                
                continue;
            }
            
            log.info("File [" + file.getName() + "] moved to [" + targetPath.getPath() + "] successfully.");
        }
        
    }
    
    
    //*********************************************
    // setter and getter method
    //*********************************************
    
    public void setClientConfig(ClientConfigHelper clientConfig)
    {
        this.clientConfig = clientConfig;
    }


    public void setMboxUtil(MailboxUtil mboxUtil)
    {
        this.mboxUtil = mboxUtil;
    }
}
