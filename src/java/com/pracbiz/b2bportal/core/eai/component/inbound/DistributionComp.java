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

package com.pracbiz.b2bportal.core.eai.component.inbound;

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
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
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
    private static final String ID = "[InboundDistributionComp]";
    
    @Autowired
    private ChannelConfigHelper channelConfig;
    @Autowired
    private BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private StandardEmailSender standardEmailSender;
    @Autowired
    private MsgTransactionsService msgTransactionsService;
    @Autowired
    private MailBoxUtil mboxUtil;


    @Override
    public Object onCall(MuleEventContext ctx) throws Exception
    {
        BatchMsg batch = (BatchMsg) ctx.getMessage().getPayload();

        log.info(":::: Batch object with No. [" + batch.getBatchNo()
                + "] received.");

        Map<BuyerHolder, List<DocMsg>> groupDocs = this
                .groupDocsByBuyer(batch.getDocs());
        SupplierHolder supplier = batch.getSupplier();
        try
        {
            for (Map.Entry<BuyerHolder, List<DocMsg>> entry : groupDocs
                    .entrySet())
            {
                BuyerHolder buyer = entry.getKey();
                for (DocMsg doc : entry.getValue())
                {
                    String yyyymm = DateUtil.getInstance().getYearAndMonth(doc.getInDate());
                    if (!(doc.isValid() && doc.isActive()))
                    {
                        File invalidPath = this.getInvalidPath(supplier, yyyymm);
                        FileUtil.getInstance().copyFile(
                                new File(doc.getContext().getWorkDir(), doc
                                        .getOriginalFilename()),
                                new File(invalidPath, doc.getOriginalFilename()),
                                true);
                        continue;
                    }
                    File archPath = this.getArchPath(supplier, yyyymm);
                    File docPath = this.getDocPath(supplier, yyyymm);
                    File targetPath = this.getTargetPath(buyer, doc.getMsgType().name());
                    
                    FileUtil.getInstance().copyFile(
                            new File(doc.getContext().getWorkDir(), doc
                                    .getOriginalFilename()),
                                    new File(archPath, doc.getOriginalFilename()),
                                    true);
                    FileUtil.getInstance().copyFile(
                            new File(doc.getContext().getWorkDir(), doc
                                    .getTargetFilename()),
                                    new File(archPath, doc.getTargetFilename()),
                                    true);
                    if (!(doc.isOriginalFileGeneratedOnPortal() || doc.getReportFilename() == null || doc.getReportFilename().trim().isEmpty()))
                    {
                        FileUtil.getInstance().copyFile(
                                new File(doc.getContext().getWorkDir(), doc
                                        .getReportFilename()),
                                new File(docPath, doc.getReportFilename()),
                                true);
                    }
                    
                    //do distribution
                    FileUtil.getInstance()
                    .copyFile(
                            new File(doc.getContext().getWorkDir(), doc
                                    .getTargetFilename()),
                                    new File(targetPath, doc
                                            .getTargetFilename()), true);
                    if(DeploymentMode.REMOTE.equals(buyer.getDeploymentMode()))
                    {
                        if(!doc.getReportFilename().trim().isEmpty())
                        {
                            FileUtil.getInstance()
                            .copyFile(
                                    new File(doc.getContext().getWorkDir(), doc
                                            .getReportFilename()),
                                            new File(targetPath, doc
                                                    .getReportFilename()), true);
                        }
                        FileUtil.getInstance().touchTok(
                                new File(mboxUtil.getChannelIndicatorDispatcherPath()),//appConfig.getChannelMailboxRootPath() + PS+ "indicator" + PS + "dispatcher"
                                        buyer.getChannel());
                    }
                    
                    // update sent Date
                    if (DeploymentMode.LOCAL.equals(buyer.getDeploymentMode()))
                    {
                        MsgTransactionsHolder msg = msgTransactionsService
                                .selectByKey(doc.getDocOid());
                        if (msg == null)
                        {
                            continue;
                        }
                        msg.setSentDate(new Date());
                        msgTransactionsService.updateByPrimaryKey(null, msg);
                    }
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


    // *****************************************************
    // private methods
    // *****************************************************

    private File getTargetPath(BuyerHolder buyer, String type) throws Exception
    {
        File rlt = null;

        if (DeploymentMode.LOCAL.equals(buyer.getDeploymentMode()))
        {
            if (type.equalsIgnoreCase(MsgType.INV.name()))
            {
                rlt = new File(mboxUtil.getBuyerMailBox(buyer.getMboxId()) + PS
                    + "tmp");//appConfig.getBuyerMailboxRootPath() + PS + buyer.getMboxId() + PS + "tmp"
            }
            else
            {
                BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), type);
                if ("INTERVAL".equals(setting.getAlertFrequency().toString()))
                {
                    rlt = new File(mboxUtil.getBuyerOnHoldPath(buyer.getMboxId()));//appConfig.getBuyerMailboxRootPath() + PS + buyer.getMboxId() + PS + "on-hold"
                }
                else
                {
                    rlt = new File(mboxUtil.getBuyerInPath(buyer.getMboxId()));//appConfig.getBuyerMailboxRootPath() + PS + buyer.getMboxId() + PS + "in"
                }
            }
        }
        else
        {
            //rlt = new File(appConfig.getChannelMailboxRootPath() + PS
            //+ channelConfig.getChannelMailbox(buyer.getChannel())
            //+ PS + "in");
            rlt = new File(mboxUtil.getChannelInPath(channelConfig.getChannelMailbox(buyer.getChannel())));
        }

        if (!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }


    private File getArchPath(SupplierHolder supplier, String yyyymm)
            throws IOException
    {
        // File rlt = new File(appConfig.getSupplierMailboxRootPath() + PS
        //+ supplier.getMboxId() + PS + "archive" + PS + "out" + PS
        //+ yyyymm);
        File rlt = new File(mboxUtil.getFolderInSupplierArchOutPath(supplier.getMboxId(), yyyymm));

        if (!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }


    private File getDocPath(SupplierHolder supplier, String yyyymm)
            throws IOException
    {
        //appConfig.getSupplierMailboxRootPath() + PS
        //+ supplier.getMboxId() + PS + "doc" + PS + "out" + PS + yyyymm
        File rlt = new File(mboxUtil.getFolderInSupplierDocOutPath(supplier.getMboxId(), yyyymm));

        if (!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }
    
    
    private File getInvalidPath(SupplierHolder supplier, String yyyymm)
    throws IOException
    {
        //appConfig.getSupplierMailboxRootPath() + PS
        //+ supplier.getMboxId() + PS + "invalid" + PS + yyyymm
        File rlt = new File(mboxUtil.getFolderInSupplierInvalidPath(supplier.getMboxId(), yyyymm));
        
        if (!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }
        
        return rlt;
    }


    private Map<BuyerHolder, List<DocMsg>> groupDocsByBuyer(List<DocMsg> docs)
    {
        Map<BuyerHolder, List<DocMsg>> rlt = new HashMap<BuyerHolder, List<DocMsg>>();

        for (DocMsg docMsg : docs)
        {
            if (rlt.containsKey(docMsg.getBuyer()))
            {
                rlt.get(docMsg.getBuyer()).add(docMsg);
            }
            else
            {
                List<DocMsg> list = new LinkedList<DocMsg>();
                list.add(docMsg);
                rlt.put(docMsg.getBuyer(), list);
            }
        }

        return rlt;
    }

}
