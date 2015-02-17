//*****************************************************************************
//
// File Name       :  FileArrivalNoticeComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:19:10 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.outbound;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Date;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.message.BatchContextRef;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class FileArrivalNoticeComp implements Callable, CoreCommonConstants
{
    private static final Logger log = LoggerFactory
        .getLogger(FileArrivalNoticeComp.class);
    private static final String ID = "[OutboundFileArrivalNoticeComp]";

    @Autowired
    private BuyerService buyerService;
    @Autowired
    private StandardEmailSender standardEmailSender;
    @Autowired
    private MailBoxUtil mboxUtil;
    
    @Override
    public Object onCall(MuleEventContext ctx)
    {
        File detectedFile = (File) ctx.getMessage().getPayload();
        String originalName = detectedFile.getName().split("---")[0];
        
        log.info(":::: Token file ["
            + originalName.substring(0, originalName.lastIndexOf('.'))
            + "] detected.");

        File tokFile = new File(detectedFile.getParent(), originalName);
        
        if (!detectedFile.renameTo(tokFile))
        {
            try
            {
                FileUtil.getInstance().deleleAllFile(detectedFile);
            }
            catch(IOException e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
                return null;
            }
        }
        
        // parse token filename.
        String[] parts = FileUtil.getInstance()
            .trimAllExtension(tokFile.getName()).split("#");
        final String mailboxId = parts[0];
        final String batchNo   = parts[1];
        
        parts = batchNo.split("_");
        final String batchType = parts[0];
        final String senderCode = parts[1];
        
        
        // locate the batch file.
        // File outPath = new File(appConfig.getBuyerMailboxRootPath() + PS
        //+ mailboxId + PS + "out");
        File outPath = new File(mboxUtil.getBuyerOutPath(mailboxId));

        //File workDir = new File(appConfig.getBuyerMailboxRootPath() + PS
        //    + mailboxId + PS + "working" + PS + "out" + PS + batchNo);
        File workDir = new File(mboxUtil.getBuyerWorkingOutPath(mailboxId) + PS + batchNo);
        File batchFile = outPath.listFiles(new FileFilter(){

            @Override
            public boolean accept(File pathname)
            {
                return pathname.getName().startsWith(batchNo);
            }
            
        })[0];
        
        
        // Init BatchMsg object.
        BatchMsg batch = new BatchMsg();
        batch.setBatchFileName(batchFile.getName());
        batch.setBatchNo(batchNo);
        batch.setBatchType(BatchType.valueOf(batchType));
        batch.setSenderCode(senderCode);
        batch.setInDate(new Date());
        
        
        // Validate the batch.
        try
        {
            BuyerHolder buyer = buyerService.selectBuyerByBuyerCodeWithBLOBs(senderCode);
            
            if (null == buyer)
            {
                throw new Exception("Buyer profile [" + senderCode
                    + "] does not exist.");
            }
            
            //check transaction_batch table here.
            
            if (workDir.isDirectory())
            {
                throw new Exception("There is already a folder ["
                    + workDir.getName() + "] under working folder.");
            }
            
            
            FileUtil.getInstance().createDir(workDir);
            FileUtil.getInstance().moveFile(batchFile, workDir.getPath());
            
            batch.setSenderOid(buyer.getBuyerOid());
            batch.setSenderName(buyer.getBuyerName());
            batch.setBuyer(buyer);
            
            BatchContextRef context = new BatchContextRef();
            context.setTokenFilename(tokFile.getName());
            context.setTokenDir(tokFile.getParent());
            context.setSenderMailboxId(mailboxId);
            context.setWorkDir(workDir.getPath());
            
            batch.setContext(context);
            
            return batch;
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            // File invalidPath = new File(appConfig.getBuyerMailboxRootPath()
            //+ PS + mailboxId + PS + "invalid" + PS
            //+ DateUtil.getInstance().getYearAndMonth(batch.getInDate()));
            File invalidPath = new File(mboxUtil.getFolderInBuyerInvalidPath(
                mailboxId, DateUtil.getInstance().getYearAndMonth(
                    batch.getInDate())));
            
            try
            {
                if(!invalidPath.isDirectory())
                {
                    FileUtil.getInstance().createDir(invalidPath);
                }
                FileUtil.getInstance().moveFile(batchFile, invalidPath.getPath());
                FileUtil.getInstance().deleleAllFile(workDir);
                FileUtil.getInstance().changeFileExtension(tokFile, ".err");
            }
            catch(IOException e1)
            {
                String tickNo1 = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo1, e);
            }
            
        }
        
        return null;
    }

}
