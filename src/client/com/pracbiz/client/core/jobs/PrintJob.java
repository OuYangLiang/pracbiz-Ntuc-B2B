//*****************************************************************************
//
// File Name       :  PrintJob.java
// Date Created    :  Mar 6, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Mar 6, 2013 $
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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pracbiz.b2bportal.base.print.PdfFilePrinter;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.client.utils.ClientConfigHelper;
import com.pracbiz.client.utils.MailboxUtil;
import com.pracbiz.client.utils.PrintFileUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class PrintJob extends QuartzJobBean
{
    private static final Logger log = LoggerFactory.getLogger(PrintJob.class);
    private static boolean isRunning = false;
    
    private ClientConfigHelper clientConfig;
    private MailboxUtil mboxUtil;
    private PdfFilePrinter pdfFilePrinter;
    
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


    private void process(String serverId, String mboxId)
    {
        try
        {
            File printDir = mboxUtil.getPrintPath(serverId, mboxId);
            File invalidPath = mboxUtil.getInvalidInPath(serverId, mboxId);
            
            File[] srcFiles = printDir.listFiles(new FileFilter(){

                @Override
                public boolean accept(File file)
                {
                    return StringUtils.lowerCase(file.getName())
                        .endsWith(StringUtils.lowerCase(ClientConfigHelper.VALUE_PRINT_JOB_FILE_EXTENSION));
                }
                
            });
            
            if (null == srcFiles || srcFiles.length == 0)
            {
                log.info("No print files found in directory [" + printDir.getPath() + "].");
                
                return;
            }
            
            File[] stableFiles = FileUtil.getInstance().getStableFiles(srcFiles);
            
            if (null == stableFiles || stableFiles.length == 0)
            {
                log.info("No print files found in directory [" + printDir.getPath() + "].");
                
                return;
            }
            
            
            for (File file : stableFiles)
            {
                try
                {
                    Map<String, String> printFileContent = PrintFileUtil.getInstance().parsePrintFile(file);
                    
                    String filepath = printFileContent.get(PrintFileUtil.FILE_PATH);
                    String filename = printFileContent.get(PrintFileUtil.FILE_NAME);
                    String supplierCode = printFileContent.get(PrintFileUtil.SUPPLIER_CODE);
                    
                    String printerName = clientConfig.getPrinterBySupplierCode(serverId, supplierCode);
                    
                    File pdffile = new File(filepath, filename);
                    
                    if(!pdffile.exists())
                    {
                        log.error("Pdf file [" + pdffile.getName() + "] is not found based on print file [" + file.getName() + "].");
                        log.error("Move file [" + file.getPath() + "] to invalid path [" + invalidPath.getPath() + "].");
                        
                        FileUtil.getInstance().moveFile(file, invalidPath.getPath());
                        continue;
                    }
                    
                    if (StringUtils.isBlank(printerName))
                    {
                        log.error("Printer not configured for supplier code [" + supplierCode + "].");
                        
                        continue;
                    }
                    
                    pdfFilePrinter.print(printerName, new File(filepath, filename).getPath());
                    FileUtil.getInstance().deleleAllFile(file);
                }
                catch (Exception e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }

    // *********************************************
    // setter and getter method
    // *********************************************

    public void setClientConfig(ClientConfigHelper clientConfig)
    {
        this.clientConfig = clientConfig;
    }


    public void setPdfFilePrinter(PdfFilePrinter pdfFilePrinter)
    {
        this.pdfFilePrinter = pdfFilePrinter;
    }


    public void setMboxUtil(MailboxUtil mboxUtil)
    {
        this.mboxUtil = mboxUtil;
    }
    
}
