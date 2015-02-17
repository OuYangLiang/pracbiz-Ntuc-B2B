//*****************************************************************************
//
// File Name       :  DistributionComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:22:37 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.outbound;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.util.ChannelConfigHelper;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class DistributionComp implements Callable, CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(DistributionComp.class);
    private static final String ID = "[OutboundDistributionComp]";
    
    @Autowired
    private ChannelConfigHelper channelConfig;
    @Autowired
    private StandardEmailSender standardEmailSender;
    @Autowired
    private MsgTransactionsService msgTransactionsService;
    @Autowired
    private MailBoxUtil mboxUtil;

    @Override
    public Object onCall(MuleEventContext ctx)
    {
        BatchMsg batch = (BatchMsg)ctx.getMessage().getPayload();
        
        log.info(":::: Batch object with No. [" + batch.getBatchNo()
            + "] received.");
        
        String yyyymm = DateUtil.getInstance().getYearAndMonth(batch.getInDate());
        Map<SupplierHolder, List<DocMsg>> groupDocs = this
            .groupDocsBySupplier(batch.getDocs());
        
        try
        {
            for(Map.Entry<SupplierHolder, List<DocMsg>> entry : groupDocs
                .entrySet())
            {
                SupplierHolder supplier = entry.getKey();
                if (supplier == null)
                {
                    continue;
                }
                
                File archPath = this.getArchPath(supplier, yyyymm);
                File docPath  = this.getDocPath(supplier, yyyymm);
                File targetPath = this.getTargetPath(supplier);
                File eaiPath = this.getEaiPath(supplier);
                
                boolean flag = false;//used to indicate whether any files copied to channel mailbox.
                
                for (DocMsg doc : entry.getValue())
                {
         
                    if (!(doc.isValid() && doc.isActive()))
                    {
                        File invalidPath = this.getInvalidPath(supplier, batch);
                        FileUtil.getInstance().moveFile(
                                new File(doc.getContext().getWorkDir(), doc
                                        .getOriginalFilename()),
                                invalidPath.getAbsolutePath());
                        continue;
                    }
                    //copy target file.
                    if (DeploymentMode.LOCAL.equals(supplier
                            .getDeploymentMode()))
                    {
                        if (supplier.getClientEnabled() != null && supplier.getClientEnabled())
                        {
                            if (supplier.getRequireTranslationIn())
                            {
                                FileUtil.getInstance().copyFile(
                                        new File(doc.getContext().getWorkDir(), doc
                                                .getTargetFilename()),
                                                new File(eaiPath, doc.getTargetFilename()),
                                                true);
                            }
                            else
                            {
                                FileUtil.getInstance().copyFile(
                                        new File(doc.getContext().getWorkDir(), doc.getTargetFilename()),
                                        new File(targetPath, doc.getTargetFilename()), true);
                            }
                            
                            FileUtil.getInstance().copyFile(
                                    new File(doc.getContext().getWorkDir(), doc.getTargetFilename()),
                                    new File(archPath, doc.getTargetFilename()), true);
                        }
                    }
                    else
                    {
                        FileUtil.getInstance().copyFile(
                                new File(doc.getContext().getWorkDir(), doc.getTargetFilename()),
                                new File(targetPath, doc.getTargetFilename()), true);
                    }
                    //copy report file.
                    if (doc.getReportFilename() != null)
                    {
                        if (DeploymentMode.REMOTE.equals(supplier.getDeploymentMode()))
                        {
                            FileUtil.getInstance().copyFile(
                                    new File(doc.getContext().getWorkDir(), doc.getReportFilename()),
                                    new File(targetPath, doc.getReportFilename()), true);
                            
                            flag = true;
                        }
                        else if (supplier.getClientEnabled() != null && supplier.getClientEnabled() && supplier.getRequireReport())
                        {
                            FileUtil.getInstance().copyFile(
                                    new File(doc.getContext().getWorkDir(), doc.getReportFilename()),
                                    new File(targetPath, doc.getReportFilename()), true);
                        }
                        
                        FileUtil.getInstance().copyFile(
                                new File(doc.getContext().getWorkDir(), doc.getReportFilename()),
                                new File(docPath, doc.getReportFilename()), true);
                    }
                    
                    //update sent Date
                    if (DeploymentMode.LOCAL.equals(supplier
                            .getDeploymentMode()))
                    {
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(doc.getDocOid());
                        if (msg == null)
                        {
                            continue;
                        }
                        msg.setSentDate(new Date());
                        msg.setOutDate(msg.getSentDate());
                        msgTransactionsService.updateByPrimaryKey(null, msg);
                    }
                }
                
                if (flag)
                {
                    //appConfig.getChannelMailboxRootPath() + PS
                    //+ "indicator" + PS + "dispatcher"
                    FileUtil.getInstance().touchTok(
                        new File(mboxUtil.getChannelIndicatorDispatcherPath()),
                        supplier.getChannel());
                }
                
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            return null;
        }
        
        log.info(":::: Batch object with No. [" + batch.getBatchNo()
            + "] processed successfully.");
        
        return batch;
    }
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private File getTargetPath(SupplierHolder supplier) throws IOException
    {
        File rlt = null;
        
        if (DeploymentMode.LOCAL.equals(supplier.getDeploymentMode()))
        {
            //rlt = new File(appConfig.getSupplierMailboxRootPath() + PS
            //+ supplier.getMboxId() + PS + "in");
            rlt = new File(mboxUtil.getSupplierInPath(supplier.getMboxId()));
        }
        else
        {
            //rlt = new File(appConfig.getChannelMailboxRootPath() + PS
            //+ channelConfig.getChannelMailbox(supplier.getChannel()) + PS
            //+ "in");
            rlt = new File(mboxUtil.getChannelInPath(channelConfig
                .getChannelMailbox(supplier.getChannel())));
 }

        if(!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }

    private File getArchPath(SupplierHolder supplier, String yyyymm)
        throws IOException
    {
        //File rlt = new File(appConfig.getSupplierMailboxRootPath() + PS
        //+ supplier.getMboxId() + PS + "archive" + PS + "in" + PS + yyyymm);
        File rlt = new File(mboxUtil.getFolderInSupplierArchInPath(supplier.getMboxId(), yyyymm));

        if(!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }

    private File getDocPath(SupplierHolder supplier, String yyyymm)
        throws IOException
    {
        //File rlt = new File(appConfig.getSupplierMailboxRootPath() + PS
        //+ supplier.getMboxId() + PS + "doc" + PS + "in" + PS + yyyymm);
        File rlt = new File(mboxUtil.getFolderInSupplierDocInPath(supplier.getMboxId(), yyyymm));

        if(!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }
    
    
    private File getEaiPath(SupplierHolder supplier) throws Exception
    {
        // File rlt = new File(appConfig.getSupplierMailboxRootPath() + PS
        // + supplier.getMboxId() + PS + "eai" + PS + "in" + PS +"pending");
        File rlt = new File(mboxUtil.getSupplierEaiInPendingPath(supplier.getMboxId()));

        if(!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }
    
    
    private File getInvalidPath(SupplierHolder supplier, BatchMsg batch) throws Exception
    {
        //File rlt = new File(appConfig.getSupplierMailboxRootPath()
        //+ PS + supplier.getMboxId() + PS + "invalid" + PS
        //+ DateUtil.getInstance().getYearAndMonth(batch.getInDate()));
        File rlt = new File(mboxUtil.getFolderInSupplierInvalidPath(supplier
            .getMboxId(), DateUtil.getInstance().getYearAndMonth(
            batch.getInDate())));

        if(!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }
    
    private Map<SupplierHolder, List<DocMsg>> groupDocsBySupplier(
        List<DocMsg> docs)
    {
        Map<SupplierHolder, List<DocMsg>> rlt = 
            new HashMap<SupplierHolder, List<DocMsg>>();
        
        for (DocMsg docMsg : docs)
        {
            if (rlt.containsKey(docMsg.getSupplier()))
            {
                rlt.get(docMsg.getSupplier()).add(docMsg);
            }
            else
            {
                List<DocMsg> list = new LinkedList<DocMsg>();
                list.add(docMsg);
                rlt.put(docMsg.getSupplier(), list);
            }
        }
        
        return rlt;
    }

}
