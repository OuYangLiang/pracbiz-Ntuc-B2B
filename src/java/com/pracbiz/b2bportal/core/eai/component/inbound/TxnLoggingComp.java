//*****************************************************************************
//
// File Name       :  TxnLoggingComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:22:05 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.inbound;

import java.io.File;
import java.io.IOException;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.backend.SupplierMasterImportJob;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.service.TransactionBatchService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class TxnLoggingComp implements Callable, CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(TxnLoggingComp.class);
    private static final String ID = "[InboundTxnLoggingComp]";

    @Autowired 
    private MailBoxUtil mboxUtil;
    @Autowired 
    private TransactionBatchService transactionBatchService;
    @Autowired
    private StandardEmailSender standardEmailSender;
    
    @Override
    public Object onCall(MuleEventContext ctx) throws Exception
    {
        BatchMsg batch = (BatchMsg) ctx.getMessage().getPayload();

        log.info(":::: Batch object with No. [" + batch.getBatchNo()
                + "] received.");

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
            
            transactionBatchService.insertInboundBatch(batch);
            
            log.info(":::: Batch object with No. [" + batch.getBatchNo()
                + "] processed successfully.");
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            //appConfig.getSupplierMailboxRootPath()
            //+ PS + batch.getContext().getSenderMailboxId() + PS + "invalid" + PS
            //+ DateUtil.getInstance().getYearAndMonth(batch.getInDate())
            File invalidPath = new File(mboxUtil
                .getFolderInSupplierInvalidPath(batch.getContext()
                    .getSenderMailboxId(), DateUtil.getInstance()
                    .getYearAndMonth(batch.getInDate())));

            try
            {
                if(!invalidPath.isDirectory())
                {
                    FileUtil.getInstance().createDir(invalidPath);
                }
                
                File workDir = new File(batch.getContext().getWorkDir());
                File batchFile = new File(workDir, batch.getBatchFileName());
                
                FileUtil.getInstance().moveFile(batchFile,
                    invalidPath.getPath());
                FileUtil.getInstance().deleleAllFile(workDir);
                FileUtil.getInstance().changeFileExtension(
                    new File(batch.getContext().getTokenDir(), batch
                        .getContext().getTokenFilename()), ".err");
            }
            catch(IOException e1)
            {
                String tickNo1 = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo1, e);
            }
            
            return null;
        }
        finally
        {
            synchronized (SupplierMasterImportJob.lock)
            {
                SupplierMasterImportJob.isAnyJobRunning = false;

                SupplierMasterImportJob.lock.notifyAll();
            }
        }

        return batch;
    }

}
