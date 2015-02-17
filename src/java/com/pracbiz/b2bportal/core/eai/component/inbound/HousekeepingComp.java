//*****************************************************************************
//
// File Name       :  HousekeepingComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:23:21 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.inbound;

import java.io.File;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class HousekeepingComp implements Callable, CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(HousekeepingComp.class);
    private static final String ID = "[InboundHousekeepingComp]";

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

            FileUtil.getInstance().deleleAllFile(
                    new File(batch.getContext().getWorkDir()));

            FileUtil.getInstance().deleleAllFile(
                    new File(batch.getContext().getTokenDir(), batch
                            .getContext().getTokenFilename()));

            log.info(":::: Batch object with No. [" + batch.getBatchNo()
                    + "] processed successfully.");
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }

        return batch;
    }

}
