//*****************************************************************************
//
// File Name       :  UploadJob.java
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
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.exception.GnupgException;
import com.pracbiz.b2bportal.core.util.GnupgUtil;
import com.pracbiz.b2bportal.core.util.GnupgUtilWrapper;
import com.pracbiz.client.utils.ClientConfigHelper;
import com.pracbiz.client.utils.HttpClientHelper;
import com.pracbiz.client.utils.MailboxUtil;


/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class UploadJob extends QuartzJobBean
{
    private static final Logger log = LoggerFactory.getLogger(UploadJob.class);
    private static boolean isRunning = false;
    
    private ClientConfigHelper clientConfig;
    private MailboxUtil mboxUtil;
    private HttpClientHelper httpClientHelper;
    private GnupgUtil gnupgUtil;
    
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
    
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    
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
    
    
    private void process(String serverId, String mboxId)
    {
    	File[] srcFiles = this.getSrcFiles(mboxUtil.getOutPath(serverId, mboxId));
        File[] stableFiles = null;
        
        if (srcFiles != null && srcFiles.length > 0)
        {
            stableFiles = FileUtil.getInstance().getStableFiles(srcFiles);
        }
        
        if (stableFiles != null && stableFiles.length > 0)
        {
            int maxPerZip = clientConfig.getMaxZipFile(serverId);

            try
            {
                Arrays.sort(stableFiles, new ComparatorFileLastModified());

                this.compress(serverId, stableFiles, mboxId, maxPerZip);
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
        }
        
        File[] zipFiles = this.getPreZippedFile(mboxUtil.getOutPath(serverId, mboxId));
        
        if (zipFiles != null && zipFiles.length > 0)
        {
            String clientPriKeyId = GnupgUtilWrapper.getInstance().wrapClientId(mboxId);
            String portalPubKeyId = GnupgUtilWrapper.getInstance().wrapSupplierUserId(serverId, mboxId);
            
            for (File zip : zipFiles)
            {
                log.info("Encrypting file [" + zip.getPath() + "].");
                try
                {
                    gnupgUtil.encryptAndSign(clientPriKeyId,
                        portalPubKeyId, clientPriKeyId, zip,
                        new File(zip.getParentFile(), zip.getName()
                            + ClientConfigHelper.VALUE_PGP_FILE_EXTENSION));
                    
                    FileUtil.getInstance().deleleAllFile(zip);
                }
                catch(GnupgException e)
                {
                	ErrorHelper.getInstance().logError(log, e);
                }
                catch (IOException e)
                {
                	ErrorHelper.getInstance().logError(log, e);
				}
            }
        }

        File[] pgpFiles = this.getGpgFile(mboxUtil.getOutPath(serverId, mboxId));
        
        if (pgpFiles == null || pgpFiles.length <= 0)
        {
            log.info("No stable file found in [" + mboxUtil.getOutPath(serverId, mboxId).getPath() + "]");
            
            return;
        }
        
        log.info("[" + pgpFiles.length + "] files in [" + mboxUtil.getOutPath(serverId, mboxId).getPath()
            + "] will be sent to portal.");
        
        java.util.Arrays.sort(pgpFiles, new ComparatorFileLastModified());
        
        int retryTime = clientConfig.getRetryTimes(serverId);
        int retryInterval = clientConfig.getRetryInterval(serverId);

        for (int i = 0; i <= retryTime; i++)
        {
            try
            {
                postDocsViaHttpChannel(serverId, mboxId, pgpFiles);
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
    }

    
    private void postDocsViaHttpChannel(String serverId, String mboxId, File[] files) throws Exception
    {
        try
        {
    		String nonce = httpClientHelper.authorization(serverId, log);
            String credential = httpClientHelper.getCredential(nonce, mboxId);
	        
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                
                if (!file.exists())
                {
                    continue;
                }
                
                log.info("Start to upload file [" + file.getName() + "].");
                
                httpClientHelper.postOutbox(serverId, credential, file,
                    !(i < files.length), log);
                
                log.info("File [" + file.getName() + "] uploaded successfully.");
                log.info("Archiving file [" + file.getName() + "].");
                
                if (file.renameTo(new File(mboxUtil.getArchOutPath(serverId, mboxId), file.getName())))
                {
                    log.info("File [" + file.getName() + "] archived to [" + mboxUtil.getArchOutPath(serverId, mboxId).getPath() + "] successfully.");
                }
                else
                {
                    log.error("Failed to archive file [" + file.getName() + "].");
                }
            }
        }
        finally
        {
            httpClientHelper.releaseHttpClient();
        }
    }

    
    private File[] getSrcFiles(File filepath)
    {
        File[] rlt = filepath.listFiles(new FileFilter()
        {
            public boolean accept(File file)
            {
                if(file.getName().toUpperCase().endsWith(".ZIP")
                    || file.getName().toUpperCase().endsWith(".PGP")
                    || ".DS_Store".equals(file.getName()))
                {
                    return false;
                }

                return true;
            }
        });

        if (rlt == null || rlt.length < 1)
        {
            return null;
        }
        
        return rlt;
    }
    
    
    private List<String> compress(String serverId, File[] files, String mboxId, int maxPerZip) throws IOException
    {
        List<String> result = new ArrayList<String>();
        String ts = DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec();
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
        }


        File[] tmpFiles = null;

        int times = files.length / maxPerZip;
        if (files.length % maxPerZip != 0)
        {
            times += 1;
        }

        for (int i = 0; i < times; i++)
        {
            if (files.length - i * maxPerZip < maxPerZip)
            {
                tmpFiles = new File[files.length - i * maxPerZip];
            }
            else
            {
                tmpFiles = new File[maxPerZip];
            }

            for (int j = 0; j < tmpFiles.length; j++)
            {
                tmpFiles[j] = files[i * maxPerZip + j];
            }

            String zipName = mboxId + "_" + tmpFiles.length + "_" + ts
                + StringUtils.leftPad(i + 1 + "", 3, '0')
                + ClientConfigHelper.VALUE_ZIP_FILE_EXTENSION;

            GZIPHelper.getInstance().doZip(Arrays.asList(tmpFiles),
                mboxUtil.getOutPath(serverId, mboxId).getPath(), zipName);
            
            log.info(" :::: Generated file[" + zipName + "] successfully.");

            result.add(zipName);

            for (File file : tmpFiles)
            {
                if (!file.delete())
                {
                    log.error("Failed to remove file [" + file.getPath() + "].");
                }
            }
        }

        return result;
    }
    
    
    private File[] getPreZippedFile(File path)
    {
        File[] rlt = path.listFiles(new FileFilter()
        {
            public boolean accept(File file)
            {
                return StringUtils.lowerCase(file.getName()).endsWith(
                    ClientConfigHelper.VALUE_ZIP_FILE_EXTENSION);
            }
        });

        if (rlt == null || rlt.length < 1)
        {
            return null;
        }
        
        return rlt;
    }
    
    
    private File[] getGpgFile(File path)
    {
        File[] rlt = path.listFiles(new FileFilter()
        {
            public boolean accept(File file)
            {
                return StringUtils.lowerCase(file.getName()).endsWith(
                    ClientConfigHelper.VALUE_PGP_FILE_EXTENSION);
            }
        });

        if (rlt == null || rlt.length < 1)
        {
            return null;
        }
        
        return rlt;
    }
    
    
    //*****************************************************
    // setter and getter methods
    //*****************************************************

    public void setHttpClientHelper(HttpClientHelper httpClientHelper)
    {
        this.httpClientHelper = httpClientHelper;
    }
    
    
    public void setClientConfig(ClientConfigHelper clientConfig)
    {
        this.clientConfig = clientConfig;
    }
    
    
    public void setGnupgUtil(GnupgUtil gnupgUtil)
    {
        this.gnupgUtil = gnupgUtil;
    }
    
    
    public void setMboxUtil(MailboxUtil mboxUtil)
    {
        this.mboxUtil = mboxUtil;
    }

    
    //*****************************************************
    // inner classes
    //*****************************************************
    

    static class ComparatorFileLastModified implements Comparator<File>, Serializable
    {
        private static final long serialVersionUID = 3059500002635391214L;

        public int compare(File f1, File f2)
        {
			if (f1.lastModified() < f2.lastModified())
			{
				return -1;
			}
			else if (f1.lastModified() > f2.lastModified())
			{
				return 1;
			}
			
			return 0;
        }
    }
    
}