//*****************************************************************************
//
// File Name       :  NotificationComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:23:46 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.outbound;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.service.NotificationService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class NotificationComp implements Callable
{
    private static final Logger log = LoggerFactory.getLogger(NotificationComp.class);
    private static final String ID = "[OutboundNotificationComp]";

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StandardEmailSender standardEmailSender;


    @Override
    public Object onCall(MuleEventContext ctx)
    {
        BatchMsg batch = (BatchMsg) ctx.getMessage().getPayload();

        log.info(":::: Batch object with No. [" + batch.getBatchNo()
                + "] received.");

        try
        {
            notificationService.sendOutboundNotificationEmail(batch);
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }


        log.info(":::: Batch object with No. [" + batch.getBatchNo()
                + "] processed successfully.");

        return null;
    }
}
