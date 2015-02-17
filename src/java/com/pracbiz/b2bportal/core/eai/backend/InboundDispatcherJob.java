//*****************************************************************************
//
// File Name       :  InboundDispatcherJob.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:31:44 AM$
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class InboundDispatcherJob extends BaseJob implements
    CoreCommonConstants
{
    private static final Logger log = LoggerFactory
            .getLogger(InboundDispatcherJob.class);
    private static final String ID = "[InboundDispatcherJob]";
    private static final String DELIMITOR = "_";

    private BuyerService buyerService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private StandardEmailSender standardEmailSender;
    private MailBoxUtil mboxUtil;

    private Date now;
    
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
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
            
            realProcess();
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
            now = new Date();
            //String buyerMailboxRootPath =appConfig.getBuyerMailboxRootPath()
            List<BuyerHolder> activeBuyers = buyerService.selectActiveBuyers();
            for (BuyerHolder buyer : activeBuyers)
            {
                File holdPath = new File(mboxUtil.getBuyerOnHoldPath(buyer.getMboxId()));//  //buyerMailboxRootPath + PS + buyer.getMboxId() + PS + "on-hold"
                String inPath = mboxUtil.getBuyerInPath(buyer.getMboxId()); //buyerMailboxRootPath + PS + buyer.getMboxId() + PS + "in"
                if (holdPath.isDirectory())
                {
                    FileUtil.getInstance().createDir(holdPath);
                }
                File[] detectedFiles = holdPath.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File file)
                    {
                        try
                        {
                            BatchType.valueOf(file.getName().substring(0,
                                    file.getName().indexOf('_')));
                        }
                        catch (Exception e)
                        {
                            // ErrorHelper.getInstance().logError(log, e);
                            return false;
                        }
                        return true;
                    }

                });
                if (detectedFiles != null)
                {
                    File[] stableFiles = FileUtil.getInstance().getStableFiles(
                            detectedFiles);
                    Map<String, List<File>> group = this.groupByType(stableFiles);
                    for(Map.Entry<String, List<File>> entry : group.entrySet())
                    {
                        String type = entry.getKey();
                        BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), type);
                        if(this.isContinue(setting))
                        {
                            String outputName = type + DELIMITOR + buyer.getBuyerCode() + DELIMITOR + DateUtil.getInstance().getCurrentLogicTimeStamp();
                            GZIPHelper.getInstance().doZip(entry.getValue(), inPath, outputName);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }


    // *****************************************************
    // getter and setter
    // *****************************************************
    private Map<String, List<File>> groupByType(File[] files) throws Exception
    {
        Map<String, List<File>> map = new HashMap<String, List<File>>();
        if (files == null || files.length == 0)
        {
            return map;
        }
        for (File file : files)
        {
            int index = file.getName().indexOf(DELIMITOR);
            String type = file.getName().substring(0, index);
            if (map.containsKey(type))
            {
                map.get(type).add(file);
            }
            else
            {
                List<File> list = new ArrayList<File>();
                list.add(file);
                map.put(type, list);
            }
        }
        return map;
    }


    private boolean isContinue(BuyerMsgSettingHolder setting)
    {
        if (setting == null || !"INTERVAL".equals(setting.getAlertFrequency())
                || setting.getAlertInterval() == null)
        {
            return false;
        }

        int interval = setting.getAlertInterval().intValue();

        int hour = getHour();

        int i = 0;

        while (i < 24)
        {
            if (i == hour) return true;

            i += interval;
        }

        return false;
    }


    private int getHour()
    {
        Calendar c = Calendar.getInstance();

        c.setTime(now);

        return c.get(Calendar.HOUR_OF_DAY);
    }

}
