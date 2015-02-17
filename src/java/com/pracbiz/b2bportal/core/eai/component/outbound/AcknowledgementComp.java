//*****************************************************************************
//
// File Name       :  AcknowledgementComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:23:00 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.outbound;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
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
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.ChannelConfigHelper;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class AcknowledgementComp implements Callable,CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(AcknowledgementComp.class);
    private static final String ID = "[OutboundAcknowledgementComp]";
    @Autowired
    private ChannelConfigHelper channelConfig;
    @Autowired
    private StandardEmailSender standardEmailSender;
    @Autowired
    private MailBoxUtil mboxUtil;
    
    @Override
    public Object onCall(MuleEventContext ctx)
    {
        BatchMsg batch = (BatchMsg)ctx.getMessage().getPayload();
        
        if (batch.getBuyer().getDeploymentMode().equals(DeploymentMode.LOCAL))
        {
            // only applies to remote buyers.
            return batch;
        }
        
        log.info(":::: Batch object with No. [" + batch.getBatchNo()
            + "] received.");
        
        String yyyymm = DateUtil.getInstance().getYearAndMonth(batch.getInDate());
        
        boolean succ = true;
        boolean flag = false;//used to indicate whether any files copied into channel mailbox.
        
        for (DocMsg docMsg : batch.getDocs())
        {
            if (!(docMsg.isActive() && docMsg.isValid()))
            {
                continue;
            }
            try
            {
                File archPath = this.getArchPath(docMsg.getSupplier(), yyyymm);
                File targetPath = this.getTargetPath(batch.getBuyer().getChannel());
                
                byte[] ackContent = this.generateACK(docMsg);
                FileUtil.getInstance().writeByteToDisk(
                    ackContent,
                    new File(archPath, docMsg.computeRemoteAckFilename())
                        .getPath());
                
                FileUtil.getInstance().writeByteToDisk(
                    ackContent,
                    new File(targetPath, docMsg.computeRemoteAckFilename())
                        .getPath());
                
                flag = true;
            }
            catch(Exception e)
            {
                succ = false;
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
        
        if (flag)
        {
            try
            {
                // FileUtil.getInstance().touchTok(
                //new File(appConfig.getChannelMailboxRootPath() + PS
                //    + "indicator" + PS + "dispatcher"),
                //batch.getBuyer().getChannel());
                FileUtil.getInstance().touchTok(
                    new File(mboxUtil.getChannelIndicatorDispatcherPath()),
                    batch.getBuyer().getChannel());
            }
            catch(IOException e)
            {
                succ = false;
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
                
            }
        }
        
        if (succ)
        {
            log.info(":::: Batch object with No. [" + batch.getBatchNo()
                + "] processed successfully.");
        }
        
        return batch;
    }
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private File getTargetPath(String channel)
    {
        //File(appConfig.getChannelMailboxRootPath() + PS
        //+ channelConfig.getChannelMailbox(channel) + PS + "in");
        return new File(mboxUtil.getChannelInPath(channelConfig.getChannelMailbox(channel)));
    }
    
    private File getArchPath(SupplierHolder supplier, String yyyymm)
        throws IOException
    {
        //File(appConfig.getSupplierMailboxRootPath() + PS
        //+ supplier.getMboxId() + PS + "archive" + PS + "in" + PS + yyyymm);
        File rlt = new File(mboxUtil.getFolderInSupplierArchInPath(supplier.getMboxId(), yyyymm));

        if(!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }
    
    private byte[] generateACK(DocMsg docMsg) throws IOException
    {
        OutputStream os = null;
        byte[] rlt = null;

        Document document = new Document();
        Element root = new Element("ackFile");
        root.addContent(new Element("fileName").setText(docMsg
            .computeRemoteAckFilename()));
        document.setRootElement(root);

        try
        {
            XMLOutputter o = new XMLOutputter(Format.getPrettyFormat());
            os = new ByteArrayOutputStream();
            o.output(document, os);

            rlt = ((ByteArrayOutputStream)os).toByteArray();
        }
        finally
        {
            if(os != null)
            {
                os.close();
                os = null;
            }
        }

        return rlt;
    }
    
}
