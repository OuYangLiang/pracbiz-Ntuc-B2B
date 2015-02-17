//*****************************************************************************
//
// File Name       :  DownloadJob.java
// Date Created    :  Oct 28, 2009
// Last Changed By :  $Author: $
// Last Changed On :  $Date: $
// Revision        :  $Rev: $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2009.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.core.jobs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.util.GnupgUtil;
import com.pracbiz.b2bportal.core.util.GnupgUtilWrapper;
import com.pracbiz.client.utils.ClientConfigHelper;
import com.pracbiz.client.utils.HttpClientHelper;
import com.pracbiz.client.utils.MailboxUtil;


/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class DownloadJob extends QuartzJobBean
{
    private static final Logger log = LoggerFactory.getLogger(DownloadJob.class);
    
    private ClientConfigHelper clientConfig;
    private HttpClientHelper httpClientHelper;
    private GnupgUtil gnupgUtil;
    private MailboxUtil mboxUtil;
    
    private static boolean isRunning = false;
    public final static Map<String, Boolean> keySetup = new HashMap<String, Boolean>();
    
    public final static byte[] lock = new byte[0];
    public static volatile boolean anyJobProcessing = false;
    
    
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
            synchronized(lock)
            {
                while (anyJobProcessing)
                {
                    try
                    {
                        lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
                
                anyJobProcessing = true;
            }
            
            process();
        }
        finally
        {
            synchronized (lock)
            {
                anyJobProcessing = false;

                lock.notifyAll();
            }
            
            isRunning = false;
        }
    }
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private void process()
    {
        List<String> serverIds = clientConfig.getAllServerId();
        
        for (String serverId : serverIds)
        {
            this.process(serverId.trim());
        }
    }
    
    
    private void process(String serverId)
    {
        try
        {
            log.info( "Start to process.");
            
            int retryTime = clientConfig.getRetryTimes(serverId);
            int retryInterval = clientConfig.getRetryInterval(serverId);
            List<String> mboxIds = clientConfig.getMboxIds(serverId);
            
            for (String mboxId : mboxIds)
            {
                log.info("Process for mailbox Id [" + mboxId.trim() + "] of portal [" + serverId + "].");
                
                mboxUtil.createMailboxes(serverId, mboxId.trim());
                
                String cliendKeyId = GnupgUtilWrapper.getInstance().wrapClientId(mboxId.trim());
                if (!gnupgUtil.isKeyExistInServer(cliendKeyId))
                {
                    log.info("Generating client pgp key [" + cliendKeyId + "].");
                    
                    gnupgUtil.generateKey(cliendKeyId, cliendKeyId, null);
                    
                    log.info("key [" + cliendKeyId + "] created successfully.");
                }
                
                
                for(int i = 0; i <= retryTime; i++)
                {
                    try
                    {
                        processHttpChannel(serverId, mboxId.trim());
                        // do not need to retry if successful.
                        break;
                    }
                    catch(Exception e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                        
                        if (i != retryTime)
                        {
                            log.info( "Will re-request in " + retryInterval + " milliseconds.");
                            
                            try
                            {
                                Thread.sleep(retryInterval);
                            }
                            catch(InterruptedException ex){}
                            
                            continue;
                        }
                    }
                }
                
                log.info("Mailbox Id [" + mboxId.trim() + "] of portal [" + serverId + "] process ended.");
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

        log.info( "Process ended.");
    }

    
    private void processHttpChannel(String serverId, String mboxId) throws Exception
    {
        try
        {
            String nonce = httpClientHelper.authorization(serverId, log);
            String credential = httpClientHelper.getCredential(nonce, mboxId);
            
            if ((null == keySetup.get(serverId + mboxId) || !keySetup.get(serverId + mboxId))
                && !httpClientHelper.checkKey(serverId, credential, log))
            {
                log.info("Pgp keys is not setup for mailbox [" + mboxId + "] for portal [" + serverId + "], exchanging public keys with portal.");
                
                File exportedPubKeyFile = null;
                String clientPubKeyId = GnupgUtilWrapper.getInstance().wrapClientId(mboxId);
                
                try
                {
                    exportedPubKeyFile = new File(clientConfig.getLocalMailboxRoot(), clientPubKeyId + ".key");
                    gnupgUtil.exportKey(exportedPubKeyFile.getPath(), clientPubKeyId);
                    
                    log.info("Uploading public key file [" + exportedPubKeyFile.getName() + "].");
                    httpClientHelper.uploadKey(serverId, credential, exportedPubKeyFile, log);
                }
                finally
                {
                    if (null != exportedPubKeyFile)
                    {
                        FileUtil.getInstance().deleleAllFile(exportedPubKeyFile);
                    }
                }
                
                File downloddedPubKeyFile = null;
                try
                {
                    log.info("Import public key from portal.");
                    downloddedPubKeyFile = httpClientHelper.retrievePortalPubKey(serverId, credential, clientConfig.getLocalMailboxRoot(), log);
                    log.info("Importing public key from file [" + downloddedPubKeyFile.getName() + "].");
                    gnupgUtil.importKey(downloddedPubKeyFile.getPath());
                    
                    keySetup.put(serverId + mboxId, Boolean.TRUE);
                }
                finally
                {
                    if (null != downloddedPubKeyFile)
                    {
                        FileUtil.getInstance().deleleAllFile(downloddedPubKeyFile);
                    }
                }
            }
            else
            {
                keySetup.put(serverId + mboxId, Boolean.TRUE);
            }
            
            
            int itemCount = httpClientHelper.qureyInbox(serverId, credential, log);
            
            if (itemCount > 0)
            {
                for (int i = 0; i < itemCount; i++)
                {   
                    httpClientHelper.retrieveInbox(serverId, credential, mboxUtil.getInPath(serverId, mboxId), log);
                }
            }
        }
        finally
        {
            httpClientHelper.releaseHttpClient();
        }
    }

    // *****************************************************
    // getter and setter
    // *****************************************************
    
    public ClientConfigHelper getClientConfig()
    {
        return clientConfig;
    }

    
    public void setClientConfig(ClientConfigHelper clientConfig)
    {
        this.clientConfig = clientConfig;
    }

    
    public HttpClientHelper getHttpClientHelper()
    {
        return httpClientHelper;
    }

    
    public void setHttpClientHelper(HttpClientHelper httpClientHelper)
    {
        this.httpClientHelper = httpClientHelper;
    }

    
    public void setGnupgUtil(GnupgUtil gnupgUtil)
    {
        this.gnupgUtil = gnupgUtil;
    }

    
    public void setMboxUtil(MailboxUtil mboxUtil)
    {
        this.mboxUtil = mboxUtil;
    }
    
}
