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

package com.pracbiz.b2bportal.core.eai.component.outbound;

import java.math.BigDecimal;
import java.util.Date;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.eai.message.visitor.TransformerVisitor;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.util.ChannelConfigHelper;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class DocTransformerComp implements Callable, CoreCommonConstants, ApplicationContextAware
{
    private static final Logger log = LoggerFactory
        .getLogger(DocTransformerComp.class);
    private static final String ID = "[OutboundDocTransformerComp]";
    
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
    private SupplierService supplierService;
    @Autowired
    private SupplierMsgSettingService supplierMsgSettingService;
    @Autowired 
    BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired
    private StandardEmailSender standardEmailSender;
    @Autowired
    private DnHeaderService dnHeaderService;
    
    private ApplicationContext ctx;

    @Override
    public Object onCall(MuleEventContext ctx)
    {
        DocMsg docMsg = (DocMsg)ctx.getMessage().getPayload();
        
        log.info(":::: Start to process doc, Ref-No: " + docMsg.getRefNo()
            + ", Buyer-Code: " + docMsg.getSenderCode() + ", Supplier-Code: "
            + docMsg.getReceiverCode());
        
        try
        {
            BigDecimal docOid = null;
            if (docMsg.getMsgType().equals(MsgType.DN))
            {
                DnHeaderHolder dnHeader = dnHeaderService.selectEffectiveDnHeaderByOriginalFilename(docMsg.getOriginalFilename());
                if (dnHeader != null)
                {
                    docMsg.setOriginalFileGeneratedOnPortal(true);
                    docMsg.setAmended(dnHeader.getDnStatus().equals(DnStatus.NEW) ? false : true);
                    docOid = dnHeader.getDnOid();
                }
            }
            
            if (docOid == null)
            {
                docOid = oidService.getOid();
            }
            
            docMsg.setDocOid(docOid);
            docMsg.setProcDate(new Date());
            docMsg.renameDocFileWithNewMsgId();
            
            if (docMsg.getErrorMsg() != null && !docMsg.getErrorMsg().isEmpty())
            {
                docMsg.setRemarks(docMsg.getOriginalFilename() + " validate failed.");
                docMsg.setActive(false);
                return docMsg;
            }
            
            this.preCheck(docMsg);
            this.initInputFormat(docMsg);
            
            docMsg.accept(transformerVisitor);
            
            if (docMsg.isGeneratePdf())
            {
                this.generatePdf(docMsg);
            }
            
            log.info(":::: Doc processed successfully, Ref-No: "
                + docMsg.getRefNo() + ", Buyer-Code: " + docMsg.getSenderCode()
                + ", Supplier-Code: " + docMsg.getReceiverCode());
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
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private void preCheck(DocMsg docMsg) throws Exception
    {
        TradingPartnerHolder tradingPartner = tradingPartnerService
            .selectByBuyerAndBuyerGivenSupplierCode(docMsg.getSenderOid(),
                docMsg.getReceiverCode());

        if (null == tradingPartner)
        {
            docMsg.setActive(false);

            throw new Exception("Trading Partner [" + docMsg.getSenderCode()
                + "-" + docMsg.getReceiverCode() + "] not exists.");
        }

        SupplierHolder supplier = supplierService
            .selectSupplierByKey(tradingPartner.getSupplierOid());

        if (null == supplier)
        {
            docMsg.setActive(false);
            String remarks = "Supplier [" + docMsg.getReceiverCode()
            + "] not exists.";
            docMsg.setRemarks(remarks);
            throw new Exception(remarks);
        }
        docMsg.setReceiverOid(supplier.getSupplierOid());
        docMsg.setReceiverName(supplier.getSupplierName());
        docMsg.getContext().setReceiverMailboxId(supplier.getMboxId());
        docMsg.setSupplier(supplier);

        if (!supplier.getActive())
        {
            docMsg.setActive(false);
            String remarks = "Supplier [" + docMsg.getReceiverCode()
            + "] is inactive.";
            docMsg.setRemarks(remarks);
            throw new Exception(remarks);
        }
        
    }
    
    
    private void initInputFormat(DocMsg docMsg) throws Exception
    {
        if (DeploymentMode.LOCAL.equals(docMsg.getSupplier().getDeploymentMode()))
        {
            SupplierMsgSettingHolder setting = supplierMsgSettingService
                .selectByKey(docMsg.getReceiverOid(), docMsg.getMsgType()
                    .name());
            
            docMsg.setInputFormat(setting.getFileFormat());
        }
        else//remote supplier
        {
            docMsg.setInputFormat(channelConfig.getChannelFileFormat(docMsg
                .getSupplier().getChannel(), docMsg.getMsgType()));
        }
    }
    
    
    private void generatePdf(DocMsg docMsg) throws Exception
    {
        String subType = CoreCommonConstants.DEFAULT_SUBTYPE;
        if (docMsg.getMsgType().equals(MsgType.PO))
        {
            subType = ((PoDocMsg) docMsg).getData().getPoHeader().getPoType().name();
        }
        BuyerMsgSettingReportHolder setting = buyerMsgSettingReportService.selectByKey(docMsg.getSenderOid(), 
                docMsg.getMsgType().name(), subType);
        
        String template = null;
        
        if (setting.getCustomizedReport())
        {
            template = appConfig.getCustomizedReport(
                docMsg.getSenderCode(), docMsg.getMsgType().name(), setting.getSubType(),
                setting.getReportTemplate());
        }
        else
        {
            template = appConfig.getStandardReport(docMsg.getMsgType().name(), setting.getSubType(), setting.getReportTemplate());
        }
        
        DefaultReportEngine<?> engine = ctx.getBean(template, DefaultReportEngine.class);
        
        @SuppressWarnings("unchecked")
        byte[] content = engine.generateReport(docMsg.computeReportEngineParameter(),  DefaultReportEngine.PDF_TYPE_STANDARD);//0 means standard pdf 
        
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
