//*****************************************************************************
//
// File Name       :  DocTransformerComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:20:53 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.inbound;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.CnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.eai.message.visitor.TransformerVisitor;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.util.ChannelConfigHelper;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class DocTransformerComp implements Callable, CoreCommonConstants,
    ApplicationContextAware
{
    private static final Logger log = LoggerFactory
            .getLogger(DocTransformerComp.class);
    private static final String ID = "[InboundDocTransformerComp]";

    @Autowired
    private CustomAppConfigHelper appConfig;
    @Autowired
    private ChannelConfigHelper channelConfig;
    @Autowired
    private TransformerVisitor transformerVisitor;
    @Autowired
    private OidService oidService;
    @Autowired
    private TradingPartnerService tradingPartnerService;
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired
    private MsgTransactionsService msgTransactionsService;
    @Autowired
    private StandardEmailSender standardEmailSender;
    @Autowired
    private MailBoxUtil mboxUtil;
    
    private ApplicationContext ctx;


    @Override
    public Object onCall(MuleEventContext ctx) throws Exception
    {
        DocMsg docMsg = (DocMsg) ctx.getMessage().getPayload();

        log.info(":::: Start to process doc, Ref-No: " + docMsg.getRefNo()
                + ", Supplier-Code: " + docMsg.getSenderCode()
                + ", Buyer-Code: " + docMsg.getReceiverCode());
        try
        {
            boolean genPdf = false;
            
            docMsg.setProcDate(new Date());
            docMsg.setDocOid(oidService.getOid());
            
            if (docMsg.getErrorMsg() != null && !docMsg.getErrorMsg().isEmpty())
            {
                docMsg.setRemarks(docMsg.getOriginalFilename() + " validate failed.");
                docMsg.setActive(false);
                return docMsg;
            }
            
            this.preCheck(docMsg);
            
            if(DeploymentMode.LOCAL.equals(docMsg.getSupplier()
                .getDeploymentMode()))
            {
                MsgTransactionsHolder msg = msgTransactionsService
                    .selectByMsgTypeAndOriginalFileName(docMsg.getMsgType()
                        .name(), docMsg.getOriginalFilename());
                
                if (msg == null)
                {
                    // doc file received from supplier's backend ERP system.
                    docMsg.setOriginalFileGeneratedOnPortal(false);
                    docMsg.renameDocFileWithNewMsgId();
                    
                    genPdf = true;
                }
                else
                {
                    // if doc file generate on webportal, copy its pdf file from doc folder to current working folder.
                    docMsg.setDocOid(msg.getDocOid());
                    docMsg.setInDate(msg.getCreateDate());
                    docMsg.setOriginalFileGeneratedOnPortal(true);
//                    File pdfFile = new File(
//                        appConfig.getSupplierMailboxRootPath()
//                            + PS + docMsg.getContext().getSenderMailboxId()
//                            + PS + "doc" + PS + "out" + PS
//                            + DateUtil.getInstance().getYearAndMonth(
//                                msg.getCreateDate()), msg.getReportFilename());
                    if (docMsg.isGeneratePdf())
                    {
                        File pdfFile = new File(mboxUtil
                                .getFolderInSupplierDocOutPath(docMsg.getContext()
                                        .getSenderMailboxId(), DateUtil.getInstance()
                                        .getYearAndMonth(msg.getCreateDate())), msg
                                        .getReportFilename());
                        
                        
                        if (pdfFile.exists())
                        {
                            FileUtils.copyFile(pdfFile, new File(docMsg
                                    .getContext().getWorkDir(), pdfFile.getName()));
                            docMsg.setReportFilename(pdfFile.getName());
                        }
                    }
                    
                }
            }
            else
            {
                // remote sender
                docMsg.setOriginalFileGeneratedOnPortal(false);
                docMsg.renameDocFileWithNewMsgId();
            }
            
            this.initInputFormat(docMsg);
            docMsg.accept(transformerVisitor);
            
            if (genPdf && docMsg.isGeneratePdf())
            {
                this.generatePdf(docMsg);
            }

            log.info(":::: Doc processed successfully, Ref-No: "
                    + docMsg.getRefNo() + ", Buyer-Code: "
                    + docMsg.getReceiverCode() + ", Supplier-Code: "
                    + docMsg.getSenderCode());
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            if (docMsg.isActive())
            {
                docMsg.setValid(false);
            }
            docMsg.setRemarks(e.getMessage());
        }

        return docMsg;
    }


    // *****************************************************
    // private methods
    // *****************************************************

    private void preCheck(DocMsg docMsg) throws Exception
    {
        BuyerHolder buyer = buyerService.selectBuyerByBuyerCode(docMsg
                .getReceiverCode());
        if (null == buyer)
        {
            docMsg.setActive(false);
            
            throw new Exception("Buyer [" + docMsg.getReceiverCode()
                + "] not exists.");
        }

        TradingPartnerHolder tradingPartner = tradingPartnerService
                .selectByBuyerAndBuyerGivenSupplierCode(buyer.getBuyerOid(),
                        docMsg.getSenderCode());
        if (null == tradingPartner)
        {
            docMsg.setActive(false);
            
            throw new Exception("Trading Partner [" + docMsg.getReceiverCode()
                    + "-" + docMsg.getSenderCode() + "] not exists.");
        }
        
        docMsg.setReceiverOid(buyer.getBuyerOid());
        docMsg.setReceiverName(buyer.getBuyerName());
        docMsg.getContext().setReceiverMailboxId(buyer.getMboxId());
        docMsg.setBuyer(buyer);
        
        if (!buyer.getActive())
        {
            docMsg.setActive(false);
            
            throw new Exception("Buyer [" + docMsg.getReceiverCode()
                + "] is inactive.");
        }
    }


    private void initInputFormat(DocMsg docMsg) throws Exception
    {
        if (DeploymentMode.LOCAL.equals(docMsg.getBuyer().getDeploymentMode()))
        {
            BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(
                    docMsg.getReceiverOid(), docMsg.getMsgType().name());

            docMsg.setInputFormat(setting.getFileFormat());
        }
        else
        // remote buyer
        {
            docMsg.setInputFormat(channelConfig.getChannelFileFormat(docMsg
                    .getBuyer().getChannel(), docMsg.getMsgType()));
        }
    }


    private void generatePdf(DocMsg docMsg) throws Exception
    {
        String subType = CoreCommonConstants.DEFAULT_SUBTYPE;
        if (docMsg.getMsgType().equals(MsgType.INV))
        {
            subType = ((InvDocMsg) docMsg).getData().getHeader().getInvType().name();
        }
        else if (docMsg.getMsgType().equals(MsgType.CN))
        {
            subType = ((CnDocMsg) docMsg).getData().getHeader().getCnType().name();
        }
        BuyerMsgSettingReportHolder setting = buyerMsgSettingReportService.selectByKey(docMsg.getReceiverOid(), 
                docMsg.getMsgType().name(), subType);
        
        String template = null;
        
        if (setting.getCustomizedReport())
        {
            template = appConfig.getCustomizedReport(
                docMsg.getReceiverCode(), docMsg.getMsgType().name(), setting.getSubType(),
                setting.getReportTemplate());
        }
        else
        {
            template = appConfig.getStandardReport(docMsg.getMsgType().name(), setting.getSubType(), setting.getReportTemplate());
        }
        
        DefaultReportEngine<?> engine = ctx.getBean(template, DefaultReportEngine.class);
        
        @SuppressWarnings("unchecked")
        byte[] content = engine.generateReport(docMsg.computeReportEngineParameter(), DefaultReportEngine.PDF_TYPE_STANDARD);//0 means standard pdf 
        
        FileUtil.getInstance()
        .writeByteToDisk(
            content,
            docMsg.getContext().getWorkDir() + PS
                + docMsg.computePdfFilename());

        docMsg.setReportFilename(docMsg.computePdfFilename());
    }
    
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
        throws BeansException
    {
        this.ctx = ctx;
    }
}
