//*****************************************************************************
//
// File Name       :  InboundFileDetectJob.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:33:15 AM$
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
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mule.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.message.constants.Direction;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class InboundFileDetectJob extends BaseJob implements 
        CoreCommonConstants
{
    private static final Logger log = LoggerFactory
            .getLogger(InboundFileDetectJob.class);
    private static final String ID = "[InboundFileDetectJob]";

    private SupplierService supplierService;
    private StandardEmailSender standardEmailSender;
    private MailBoxUtil mboxUtil;


    @Override
    protected void init()
    {
        supplierService = this.getBean("supplierService", SupplierService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
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
            
            //if this file is locked, means that somebody is voiding invoice.
            RandomAccessFile input = null;
            try
            {
                String lockFileName = mboxUtil.getSupplierIndiOutboundPath() + PS + "lock";
                
                if (FileUtil.getInstance().isFileExist(mboxUtil.getSupplierIndiOutboundPath(), 
                        lockFileName))
                {
                    FileUtils.createFile(lockFileName);
                }
                
                input = new RandomAccessFile(lockFileName, "rw");
                FileChannel channel = input.getChannel();
                FileLock lock = channel.lock();
                if (lock != null)
                {
                    realProcess();
                    lock.release();
                    channel.close();
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
            finally
            {
                try
                {
                    if (input != null)
                    {
                        input.close();
                        input = null;
                    }
                }
                catch (IOException e)
                {
                    String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                    standardEmailSender.sendStandardEmail(ID, tickNo, e);
                }
            }
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
           //String supplierMailboxRootPath = appConfig.getSupplierMailboxRootPath();
            String supplierMailboxRootPath = mboxUtil.getSupplierMboxRoot(); 
            List<SupplierHolder> activeSuppliers = supplierService
                    .selectActiveSuppliers();

            // File indiPath = new File(supplierMailboxRootPath + PS + "indicator" + PS + "outbound");
            File indiPath = new File(mboxUtil.getSupplierIndiOutboundPath());

            if (indiPath.isDirectory())
            {
                FileUtil.getInstance().createDir(indiPath);
            }

            for (SupplierHolder supplier : activeSuppliers)
            {
                File outPath = new File(supplierMailboxRootPath + PS
                        + supplier.getMboxId() + PS + "out");

                File[] detectedFiles = outPath.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File file)
                    {
                        try
                        {
                            if (file.getName().toLowerCase(Locale.US).endsWith(".zip"))
                            {
                                return false;
                            }
                            
                            MsgType msg = MsgType.valueOf(file.getName().substring(0,
                                    file.getName().indexOf('_')).toUpperCase(Locale.US));
                            if (msg.getDirection().equals(Direction.outbound))
                            {
                                return false;
                            }
                            if ("ack".equalsIgnoreCase(FileUtil.getInstance().getExtension(file)))
                            {
                                return false;
                            }
                        }
                        catch (Exception e)
                        {
                            return false;
                        }
                        
                        return true;
                    }

                });
                
                if (detectedFiles == null || detectedFiles.length == 0)
                {
                    continue;
                }
                
                File[] stableFiles = FileUtil.getInstance().getStableFiles(
                    detectedFiles);
                
                List<File> files = Arrays.asList(stableFiles);
                
                Map<String, List<File>> map = groupFilesByMsgType(files);
                
                if (map != null && !map.isEmpty())
                {
                    for (Map.Entry<String, List<File>> entry : map.entrySet())
                    {
                        String outputName = entry.getKey() + "_" + supplier.getSupplierCode() + "_"
                                + DateUtil.getInstance().getCurrentLogicTimeStamp()
                                + ".zip";
                        GZIPHelper.getInstance().doZip(entry.getValue(), outPath.getPath(), outputName);
                        
                        log.info(":::: Batch file [" + outputName
                                + "] created, creating token file...");
                        String batchNo = FileUtil.getInstance().trimAllExtension(
                                outputName);
                        String tokName = supplier.getMboxId() + "#" + batchNo;
                        
                        FileUtil.getInstance().touchTok(indiPath, tokName);
                        
                        log.info(":::: Token file [" + tokName
                                + "] created successfully.");
                    }
                }
                 
                for (File msgFile : stableFiles)
                {
                    FileUtil.getInstance().deleleAllFile(msgFile);
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }
    
    
    private Map<String, List<File>> groupFilesByMsgType(List<File> fileList)
    {
        if (fileList == null || fileList.isEmpty())
        {
            return null;
        }
        
        Map<String, List<File>> map = new HashMap<String, List<File>>();
        
        for (File file : fileList)
        {
            String msgType = FileUtil.getInstance().trimExtension(file.getName()).split("_")[0].toUpperCase();
            if (map.containsKey(msgType))
            {
                map.get(msgType).add(file);
            }
            else
            {
                List<File> files = new ArrayList<File>();
                files.add(file);
                map.put(msgType, files);
            }
        }
        
        return map;
    }
}
