//*****************************************************************************
//
// File Name       :  OutboundFileDetectJob.java
// Date Created    :  Mar 5, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Mar 5, 2013 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.core.jobs;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Locale;

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
 * @author jiangming
 */
public class OutboundFileDetectJob extends QuartzJobBean
{
    private static final Logger log = LoggerFactory.getLogger(OutboundFileDetectJob.class);
    
    private static boolean isRunning = false;
    
    private ClientConfigHelper clientConfig;
    private MailboxUtil mboxUtil;

    
    @Override
    protected void executeInternal(JobExecutionContext context)
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
            
            if (null == DownloadJob.keySetup || DownloadJob.keySetup.isEmpty())
            {
                log.info("Key is not setup, please let Download job run first.");
                return;
            }
            
            List<String> serverIds = clientConfig.getAllServerId();
            for (String serverId : serverIds)
            {
                List<String> mboxIds = clientConfig.getMboxIds(serverId);
                
                for (String mboxId : mboxIds)
                {
                    if (!DownloadJob.keySetup.get(serverId.trim() + mboxId.trim()))
                    {
                        log.info("Key is not setup, please let Download job run first.");
                        return;
                    }
                }
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
        log.info("Start to process.");
        
        List<String> serverIds = clientConfig.getAllServerId();
        
        for (String serverId : serverIds)
        {
            List<String> mboxIds = clientConfig.getMboxIds(serverId.trim());
            
            for (String mboxId : mboxIds)
            {
                log.info("Start to process for mailbox [" + mboxId + "] for portal [" + serverId + "].");
                
                this.process(serverId.trim(), mboxId.trim());
                
                log.info("Mailbox [" + mboxId + "] for portal [" + serverId + "] process ended.");
            }
        }
        
        log.info(" :::: Ended to process.");
    }


    private void process(final String serverId, String mboxId) 
    {
        File privateOutDir = mboxUtil.getPrivateOutPath(serverId, mboxId);
        
        File[] files = privateOutDir.listFiles(new FileFilter() 
        {
            public boolean accept(File file)
            {
                List<String> patterns = clientConfig.getOutboundMsgPatterns(serverId);
                
                if (patterns == null || patterns.isEmpty())
                {
                    return false;
                }
                
                for (String regRex : patterns)
                {
                    if (file.getName().toUpperCase(Locale.US).matches(regRex))
                        return true;
                }
                
                return false;
            }
        });

        File[] stableFiles = FileUtil.getInstance().getStableFiles(files);
        
        if (stableFiles == null || stableFiles.length <= 0)
        {
            log.info("No stable file found in [" + privateOutDir.getPath() + "]");
            
            return;
        }

        for (File doc : stableFiles)
        {
            log.info("File [" + doc.getName() + "] detected, move it to the out folder for distribution");
            
            if (!doc.renameTo(new File(mboxUtil.getOutPath(serverId, mboxId), doc.getName())))
            {
                log.error("Failed to move file [" + doc.getName() + "] to the out folder.");
                
                continue;
            }
            
            log.info("File [" + doc.getName() + "] sent to the out folder successfully.");
        }
        
    }
    
    
    // *********************************************
    // setter and getter method
    // *********************************************
    
    public void setClientConfig(ClientConfigHelper clientConfig)
    {
        this.clientConfig = clientConfig;
    }


    public void setMboxUtil(MailboxUtil mboxUtil)
    {
        this.mboxUtil = mboxUtil;
    }
}
