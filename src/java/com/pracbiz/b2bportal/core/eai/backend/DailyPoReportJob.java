//*****************************************************************************
//
// File Name       :  DailyPoReportJob.java
// Date Created    :  Jul 8, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Jul 8, 2013 4:33:26 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.backend;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.message.PoBatchSummary;
import com.pracbiz.b2bportal.core.eai.message.PoBatchSummaryLine;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class DailyPoReportJob extends BaseJob implements Job,
    CoreCommonConstants
{
    private static final Logger log = LoggerFactory
        .getLogger(DailyPoReportJob.class);
    
    private static final String ID = "[DailyPoReportJob]";
    
    private BuyerService buyerService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private MsgTransactionsService msgTransactionsService;
    private PoService poService;
    private BusinessRuleService businessRuleService;
    
    private StandardEmailSender standardEmailSender;
    private EmailEngine emailEngine;
    private MailBoxUtil mboxUtil;
    
    private int daysBefore;
    
    private static final String LINE_NO = "Line [";
    
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        msgTransactionsService = this.getBean("msgTransactionsService", MsgTransactionsService.class);
        poService = this.getBean("poService", PoService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        emailEngine = this.getBean("emailEngine", EmailEngine.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
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

        log.info("Start to process.");
        
        List<BuyerHolder> availableBuyers = null;
        try
        {
            availableBuyers = buyerService.selectActiveBuyers();
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        if (availableBuyers == null || availableBuyers.isEmpty())
        {
            log.info("No active buyers found.");
            log.info("Process ended.");
            
            return;
        }
        
        for (BuyerHolder buyer : availableBuyers)
        {
            this.processBuyer(buyer);
        }
        
        log.info("Process ended.");
    
    }
    
    
    private void processBuyer(final BuyerHolder buyer)
    {
        log.info("Start to process for buyer [" + buyer.getBuyerName() + "].");
        
        File outPath = new File(mboxUtil.getBuyerOutPath(buyer.getMboxId()));
        
        if (!outPath.isDirectory())
        {
            try
            {
                FileUtil.getInstance().createDir(outPath);
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
                
                return;
            }
        }
        
        File[] dprBatchFiles = outPath.listFiles(new FileFilter(){

            @Override
            public boolean accept(File file)
            {
                return file.getName().toUpperCase(Locale.US)
                    .matches("DPR_" + buyer.getBuyerCode().toUpperCase() + "_[0-9]+.ZIP");
            }
            
        });
        
        if (dprBatchFiles == null || dprBatchFiles.length < 1)
        {
            log.info("No Daily PO Report files found for buyer [" + buyer.getBuyerName() + "].");
            
            return;
        }
        
        dprBatchFiles = FileUtil.getInstance().getStableFiles(dprBatchFiles);
        
        if (dprBatchFiles == null || dprBatchFiles.length < 1)
        {
            log.info("No Daily PO Report files found for buyer [" + buyer.getBuyerName() + "].");
            
            return;
        }
        
        BuyerMsgSettingHolder setting = null;
        try
        {
            daysBefore = businessRuleService.selectGlobalDailyPoReportJobDaysBefore(buyer.getBuyerOid());
            setting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), MsgType.DPR.name());
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        
        for (File dprZipFile : dprBatchFiles)
        {
            try
            {
                this.processZip(buyer, setting, dprZipFile);
            }
            catch(IOException e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }
    
    
    private void processZip(BuyerHolder buyer, BuyerMsgSettingHolder setting,
        File dprZipFile) throws IOException
    {
        log.info("Processing file [" + dprZipFile.getPath() + "].");
        File workDir = new File(mboxUtil.getBuyerWorkingOutPath(buyer.getMboxId()) + PS
            + dprZipFile.getName().substring(0, dprZipFile.getName().lastIndexOf(".")));
        
        File resultBatchFile = new File(workDir, dprZipFile.getName());
        List<File> extractedFiles = null;
        
        try
        {
            try
            {
                FileUtil.getInstance().moveFile(dprZipFile, workDir.getPath());
                
                extractedFiles = GZIPHelper.getInstance()
                    .unZipAndReturnExtractFiles(resultBatchFile, workDir.getPath());
            }
            catch(Exception e)
            {
                File invalidPath = new File(mboxUtil
                    .getFolderInBuyerInvalidPath(buyer.getMboxId(), DateUtil
                        .getInstance().getYearAndMonth(new Date())));
                
                if (dprZipFile.exists())
                {
                    FileUtil.getInstance().moveFile(dprZipFile, invalidPath.getPath());
                }
                else if (resultBatchFile.exists())
                {
                    FileUtil.getInstance().moveFile(resultBatchFile, invalidPath.getPath());
                }
                
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
                
                return;
            }
            
            
            for (File dprFile : extractedFiles)
            {
                try
                {
                    processDprFile(buyer, setting, dprFile);
                }
                catch(Exception e)
                {
                    File invalidPath = new File(mboxUtil
                        .getFolderInBuyerInvalidPath(buyer.getMboxId(), DateUtil
                            .getInstance().getCurrentYearAndMonth()));

                    FileUtil.getInstance().moveFile(dprFile, invalidPath.getPath());
                    
                    String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                    standardEmailSender.sendStandardEmail(ID, tickNo, e);
                    
                    continue;
                }
                
                log.info("Archiving file [" + dprFile.getName() + "].");
                
                File archDir = new File(mboxUtil.getBuyerArchOutPath(buyer.getMboxId()) + PS
                    + DateUtil.getInstance().getCurrentYearAndMonth());
                
                FileUtil.getInstance().moveFile(dprFile, archDir.getPath());
            }
            
        }
        finally
        {
            if (workDir.isDirectory())
            {
                FileUtil.getInstance().deleleAllFile(workDir);
            }
        }
    }
    
    
    private void processDprFile(BuyerHolder buyer,
        BuyerMsgSettingHolder setting, File dprFile) throws Exception
    {
        log.info("Processing file [" + dprFile.getPath() + "].");
        
        if(null == setting || null == setting.getRcpsAddrs()
            || setting.getRcpsAddrs().trim().isEmpty())
        {
            log.warn("Message Setting for buyer [" + buyer.getBuyerName() + "] is not set.");
            
            return;
        }
        FileInputStream fis = null;
        InputStreamReader isr = null;
        CSVReader reader = null;
        List<String[]> contents = null;
        
        try
        {
            fis = new FileInputStream(dprFile);
            isr = new InputStreamReader(fis, CommonConstants.ENCODING_UTF8);
            reader = new CSVReader(isr, ',');
            contents = reader.readAll();
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
                reader = null;
            }
            if (isr != null)
            {
                isr.close();
                isr = null;
            }
            if (fis != null)
            {
                fis.close();
                fis = null;
            }
        }
        
        if (null == contents || contents.size() <= 1)
        {
            throw new IOException("Store Master file [" + dprFile.getName() + "] is empty.");
        }
        
        List<String> unfindedPos = new ArrayList<String>();
        List<String> extraPos = new ArrayList<String>();
        List<String> unmatchedPos = new ArrayList<String>();
        List<String> matchedPos = new ArrayList<String>();
        List<String> fileErrorList = new ArrayList<String>();

        PoBatchSummary summary = new PoBatchSummary();
        for (int i = 1; i < contents.size(); i++)
        {
            String[] content = contents.get(i);
            
            if (null == content || content.length != 4)
            {
                fileErrorList.add(LINE_NO + (i+1) + "] is not correctly formatted.");
                continue;
            }
            
            if (!this.isNumeric(content[1].trim()))
            {
                fileErrorList.add(LINE_NO + (i+1) + "], field [Total Line Items]: " + content[1].trim() + " is not a numeric value.");
                continue;
            }
            
            if (!this.isNumeric(content[2].trim()))
            {
                fileErrorList.add(LINE_NO + (i+1) + "], field [Total Line Item Quantity]: " + content[2].trim() + " is not a numeric value.");
                continue;
            }
            
            if (!this.isNumeric(content[3].trim()))
            {
                fileErrorList.add(LINE_NO + (i+1) + "], field [Total Amount]: " + content[3].trim() + " is not a numeric value.");
                continue;
            }
            
            
            PoBatchSummaryLine item = new PoBatchSummaryLine();
            item.setPoNo(content[0].trim());
            item.setNumOfItems(new BigDecimal(content[1].trim()));
            item.setTotalItemQty(new BigDecimal(content[2].trim()));
            item.setTotalAmt(new BigDecimal(content[3].trim()));
            
            summary.addItem(item);
        }
        
        Date from = this.computeFromDate(daysBefore);
        Date to   = this.computeToDate();
        
        boolean fileError = true; // true -> all lines in file is incorrect.
        
        if (null != summary.getItems() && !summary.getItems().isEmpty())
        {
            fileError = false;
            
            List<MsgTransactionsHolder> poMsgs = this.selectPoMsgByDateRange(
                buyer.getBuyerOid(), from, to);
            
            for (MsgTransactionsHolder poMsg : poMsgs)
            {
                PoBatchSummaryLine item = summary.findItemByPoNo(poMsg
                    .getMsgRefNo());
                
                if (null == item)
                {
                    extraPos.add(poMsg.getMsgRefNo());
                    continue;
                }
                
                summary.removeItemByPoNo(poMsg.getMsgRefNo());
                PoHolder po = poService.selectPoByKey(poMsg.getDocOid());
                
                if (item.getNumOfItems().intValue() != po.getDetails().size())
                {
                    unmatchedPos.add(item.getPoNo()
                        + " --- Number of items does not match, Report file: ["
                        + item.getNumOfItems().intValue() + "], actually: ["
                        + po.getDetails().size() + "].");
                    
                    continue;
                }
                
                if (item.getTotalItemQty().compareTo(po.calculateTotalQty()) != 0)
                {
                    unmatchedPos.add(item.getPoNo()
                        + " --- Total item quantity does not match, Report file: ["
                        + item.getTotalItemQty() + "], actually: ["
                        + po.calculateTotalQty() + "].");
                    
                    continue;
                }
                
                if (item.getTotalAmt().compareTo(po.getPoHeader().getTotalCost()) != 0)
                {
                    unmatchedPos.add(item.getPoNo()
                        + " --- Total amount does not match, Report file: ["
                        + item.getTotalAmt() + "], actually: ["
                        + po.getPoHeader().getTotalCost() + "].");
                    
                    continue;
                }
                
                matchedPos.add(poMsg.getMsgRefNo());
            }
            
            for (PoBatchSummaryLine item: summary.getItems())
            {
                unfindedPos.add(item.getPoNo());
            }
            
        }
        
        if (!fileErrorList.isEmpty() || (fileErrorList.isEmpty() && !setting.getExcludeSucc()))
        {
            Map<String, Object> mailParam = new HashMap<String, Object>();
            if (!matchedPos.isEmpty())
                mailParam.put("matchedPos", matchedPos);
            
            if (!unfindedPos.isEmpty())
                mailParam.put("unfindedPos", unfindedPos);
            
            if (!extraPos.isEmpty())
                mailParam.put("extraPos", extraPos);
            
            if (!unmatchedPos.isEmpty())
                mailParam.put("unmatchedPos", unmatchedPos);
            
            if (!fileErrorList.isEmpty())
                mailParam.put("fileErrorList", fileErrorList);
            
            mailParam.put("buyerName", buyer.getBuyerName());
            mailParam.put("filename", dprFile.getName());
            mailParam.put("dateFrom", DateUtil.getInstance().convertDateToString(from, "yyyy-MM-dd"));
            mailParam.put("dateTo", DateUtil.getInstance().convertDateToString(to, "yyyy-MM-dd"));
            
            emailEngine.sendHtmlEmail(
                setting.retrieveRcpsAddrsByDelimiterChar(COMMA_DELIMITOR),
                "Daily PO Report - " + dprFile.getName(), "ALERT_BUYER_DPR.vm",
                mailParam);
        }
        
        if (fileError)
        {
            // throw an exception so that the file is moved into invalid path.
            throw new Exception("All lines in file is invalid.");
        }
    }
    
    
    private boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    
    
    private List<MsgTransactionsHolder> selectPoMsgByDateRange(
        BigDecimal buyerOid, Date from, Date to) throws Exception
    {
        MsgTransactionsExHolder param = new MsgTransactionsExHolder();
        param.setMsgType(MsgType.PO.name());
        param.setReceivedDateFrom(from);
        param.setReceivedDateTo(to);
        param.setBuyerOid(buyerOid);

        return msgTransactionsService.select(param);
    }

    
    private Date computeFromDate(int n)
    {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        if (n >= 1)
            c.add(Calendar.DAY_OF_YEAR, -n);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        return c.getTime();
    }
    
    
    private Date computeToDate()
    {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_YEAR, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        
        return c.getTime();
    }
    
    
    /*public static void main(String[] args)
    {
        DailyPoReportJob c = new DailyPoReportJob();
        System.out.println(c.computeFromDate(1));
        System.out.println(c.computeToDate());
    }*/

}
