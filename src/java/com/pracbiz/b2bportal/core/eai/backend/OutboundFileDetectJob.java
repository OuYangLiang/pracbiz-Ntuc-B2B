//*****************************************************************************
//
// File Name       :  OutboundFileDetectJob.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:33:37 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.backend;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.file.translator.SourceTranslator;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class OutboundFileDetectJob extends BaseJob implements 
    CoreCommonConstants
{   
    private static final Logger log = LoggerFactory
        .getLogger(OutboundFileDetectJob.class);
    private static final String ID = "[OutboundFileDetectJob]";
    
    
    private BuyerService buyerService;
    private StandardEmailSender standardEmailSender;
    private MailBoxUtil mboxUtil;
    private CustomAppConfigHelper appConfig;
    
    private static final String BATCH_SOURCE_PREFIX = "SRC_";

    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        appConfig = this.getBean("appConfig", CustomAppConfigHelper.class);
    }
    
    
    @Override
    protected void process()
    {
        try
        {
            synchronized(SupplierMasterImportJob.lock)
            {
                while (SupplierMasterImportJob.isAnyJobRunning)
                {
                    try
                    {
                        SupplierMasterImportJob.lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
                
                SupplierMasterImportJob.isAnyJobRunning = true;
            }
            
            realProcess();
        }
        finally
        {
            synchronized (SupplierMasterImportJob.lock)
            {
                SupplierMasterImportJob.isAnyJobRunning = false;

                SupplierMasterImportJob.lock.notifyAll();
            }
        }
    
    
    }
    
    
    private void realProcess()
    {
        log.info(":::: Start to process.");
        
        try
        {
            List<BuyerHolder> activeBuyers = buyerService.selectActiveBuyers();
            
            File indiPath = new File(mboxUtil.getBuyerIndiOutboundPath());
            
            if (indiPath.isDirectory())
            {
                FileUtil.getInstance().createDir(indiPath);
            }
            
            for (final BuyerHolder buyer : activeBuyers)
            {
                File outPath = new File(mboxUtil.getBuyerOutPath(buyer.getMboxId()));
                
                File[] batchSourceFiles = outPath.listFiles(new FileFilter(){

                    @Override
                    public boolean accept(File file)
                    {
                        try
                        {
                            String[] filename = file.getName().split(DOC_FILENAME_DELIMITOR);
                            String suffix = filename[filename.length - 1].substring(filename[filename.length - 1].lastIndexOf(".") + 1, filename[filename.length - 1].length());
                            if (!(suffix.equalsIgnoreCase("csv") || suffix.equalsIgnoreCase("zip")))
                            {
                                return false;
                            }
                            if (!filename[2].equalsIgnoreCase(buyer.getBuyerCode()))
                            {
                                return false;
                            }
                            return file.getName().toUpperCase(Locale.US).startsWith(BATCH_SOURCE_PREFIX);
                        }
                        catch(Exception e)
                        {
                            ErrorHelper.getInstance().logError(log, e);
                            return false;
                        }
                    }
                    
                });
                
                if (batchSourceFiles != null && batchSourceFiles.length > 0 )
                {
                    batchSourceFiles = FileUtil.getInstance().getStableFiles(batchSourceFiles);
                    
                    this.preProcessing(buyer, batchSourceFiles);
                }
                
                File[] batchFiles = outPath.listFiles(new FileFilter(){

                    @Override
                    public boolean accept(File file)
                    {
                        try
                        {
                            String[] parts = file.getName().split(DOC_FILENAME_DELIMITOR);
                            BatchType.valueOf(parts[0]);
                            return buyer.getBuyerCode().equalsIgnoreCase(parts[1]);
                        }
                        catch(Exception e)
                        {
                            return false;
                        }
                    }
                    
                });
                
                if (batchFiles != null && batchFiles.length > 0)
                {
                    batchFiles = FileUtil.getInstance().getStableFiles(batchFiles);
                }
                
                if(batchFiles == null || batchFiles.length < 1)
                {
                    log.info("No files pushed down into Mailbox [" + buyer.getMboxId() + "].");
                    
                    log.info("Buyer [" + buyer.getBuyerName() + "] process ended.");
                    
                    continue;
                }
                
                for (File batchFile : batchFiles)
                {
                    String batchNo = FileUtil.getInstance().trimAllExtension(
                        batchFile.getName());
                    String tokName = buyer.getMboxId() + "#" + batchNo;
                    
                    FileUtil.getInstance().touchTok(indiPath, tokName);
                    
                    log.info(":::: Token file [" + tokName
                            + "] created successfully.");
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    
    }
    
    
    private void preProcessing (final BuyerHolder buyer, File[] detectedPreProcessFiles) throws IOException
    {
        File[] stableFiles = FileUtil.getInstance().getStableFiles(
            detectedPreProcessFiles);
        
        String archivePath = mboxUtil.getFolderInBuyerArchOutPath(buyer.getMboxId(), DateUtil.getInstance().getCurrentYearAndMonth());
        
        List<File> processedFiles = new ArrayList<File>();
        
        Collections.sort(Arrays.asList(stableFiles), new Comparator<File>()
        {
            @Override
            public int compare(File file1, File file2)
            {
                return file1.lastModified() > file2.lastModified() ? 1 : -1;
            }
            
        });
        for (File batchFile : stableFiles)
        {
            try
            {
                String[] filename = batchFile.getName().split(DOC_FILENAME_DELIMITOR);
                String fileType = filename[1].toUpperCase().trim();
                if ("CON".equalsIgnoreCase(fileType))
                {
                    fileType = "PO";
                }
                
                SourceTranslator translator = this.retrievePreTranslator(filename[1].trim(), buyer.getBuyerCode());
                translator.doTranslation(batchFile, fileType, buyer);
                
            }
            catch(Exception e)
            {
                log.info(":::: Batch file [" + batchFile.getName()
                    + "] preProcessing failed, move to invalid folder...");
                
                String yyyymm = DateUtil.getInstance().getCurrentYearAndMonth();
                String invalidPath = mboxUtil.getFolderInBuyerInvalidPath(buyer.getMboxId(), yyyymm);
                FileUtil.getInstance().moveFile(batchFile, invalidPath);

                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
        
        if (!processedFiles.isEmpty())
        {
            for (File file : processedFiles)
            {
                FileUtil.getInstance().moveFile(file, archivePath);
            }
        }
    }
    
    
    private SourceTranslator retrievePreTranslator(String msgType, String buyerCode)
    {
        return this.getBean(appConfig.getCustomizedPreTranslator(buyerCode, msgType),
            SourceTranslator.class);
    }
 
}
