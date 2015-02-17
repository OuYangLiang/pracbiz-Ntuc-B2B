package com.pracbiz.b2bportal.core.eai.backend;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingBuyerStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.extension.BusinessRuleExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileExHolder;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.report.excel.BuyerResolutionReport;
import com.pracbiz.b2bportal.core.report.excel.DnBuyerResolutionReport;
import com.pracbiz.b2bportal.core.report.excel.DnDiscrepancyReport;
import com.pracbiz.b2bportal.core.report.excel.DnExportingReport;
import com.pracbiz.b2bportal.core.report.excel.DnOutstandingReport;
import com.pracbiz.b2bportal.core.report.excel.DnSupplierResolutionReport;
import com.pracbiz.b2bportal.core.report.excel.GrnDisputeSummaryReport;
import com.pracbiz.b2bportal.core.report.excel.InvoiceExportingReport;
import com.pracbiz.b2bportal.core.report.excel.MatchingReportParameter;
import com.pracbiz.b2bportal.core.report.excel.MissingGiReport;
import com.pracbiz.b2bportal.core.report.excel.MissingGiReportParameter;
import com.pracbiz.b2bportal.core.report.excel.MissingGrnReport;
import com.pracbiz.b2bportal.core.report.excel.MissingGrnReportParameter;
import com.pracbiz.b2bportal.core.report.excel.OutstandingMatchingReport;
import com.pracbiz.b2bportal.core.report.excel.RtvGiDnReport;
import com.pracbiz.b2bportal.core.report.excel.RtvGiDnReportParameter;
import com.pracbiz.b2bportal.core.report.excel.SummaryMatchingReport;
import com.pracbiz.b2bportal.core.report.excel.SupplierResolutionReport;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;
import com.pracbiz.b2bportal.core.service.GrnService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingDetailService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.service.RtvLocationService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.customized.fairprice.report.CustomizedFairPriceBuyerResolutionAcceptedReportEngine;
import com.pracbiz.b2bportal.customized.fairprice.report.CustomizedFairPriceBuyerResolutionOthersReportEngine;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class DailyPoInvGrnDnNotificationJob extends BaseJob implements CoreCommonConstants
{
    private static final String PO_INV_GRN_DN = "PoInvGrnDn";
    private static final String MATCHING = "Matching";
    private static final Logger log = LoggerFactory
            .getLogger(DailyPoInvGrnDnNotificationJob.class);
    private static final String ID = "[DailyPoInvGrnDnNotificationJob]";
    
    private static final String UNMATCHED = "UNMATCHED";
    private static final String MATCHED = "MATCHED";
    
    private static final String DAILY_MATCHING_MATCHED_SUBJECT = "Daily Matched Report";
    private static final String DAILY_MATCHING_UNMATCHED_SUBJECT = "Daily UnMatched Report";
    private static final BigDecimal PECENT = BigDecimal.TEN.multiply(BigDecimal.TEN);

    private PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    private StandardEmailSender standardEmailSender;
    private BusinessRuleService businessRuleService;
    private EmailEngine emailEngine;
    private SupplierMsgSettingService supplierMsgSettingService;
    private DnHeaderService dnHeaderService;
    private SummaryMatchingReport summaryMatchingReport;
    private OutstandingMatchingReport outstandingMatchingReport;
    private InvoiceExportingReport invoiceExportingReport;
    private DnExportingReport dnExportingReport;
    private BuyerResolutionReport buyerResolutionReport;
    private SupplierResolutionReport supplierResolutionReport;
    private BuyerService buyerService;
    private PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService;
    private PoService poService;
    private InvoiceService invoiceService;
    private GrnService grnService;
    private PoInvGrnDnMatchingDetailService poInvGrnDnMatchingDetailService;
    private SupplierService supplierService;
    private UserProfileService userProfileService;
    private GrnHeaderService grnHeaderService;
    private GrnDisputeSummaryReport grnDisputeSummaryReport;
    private DnDiscrepancyReport dnDiscrepancyReport;
    private DnSupplierResolutionReport dnSupplierResolutionReport;
    private DnBuyerResolutionReport dnBuyerResolutionReport;
    private DnOutstandingReport dnOutstandingReport;
    private DnService dnService;
    private MissingGrnReport missingGrnReport;
    private PoLocationService poLocationService;
    private BuyerStoreService buyerStoreService;
    private CustomizedFairPriceBuyerResolutionAcceptedReportEngine customizedFairPriceBuyerResolutionAcceptedReportEngine;
    private CustomizedFairPriceBuyerResolutionOthersReportEngine customizedFairPriceBuyerResolutionOthersReportEngine;
    private MailBoxUtil mboxUtil;
    private RtvLocationService rtvLocationService;
    private MissingGiReport missingGiReport;
    private RtvGiDnReport rtvGiDnReport;
    private ControlParameterService controlParameterService;
    
    
    @Override
    protected void init()
    {
        poInvGrnDnMatchingService = this.getBean("poInvGrnDnMatchingService", PoInvGrnDnMatchingService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
        emailEngine = this.getBean("emailEngine", EmailEngine.class);
        supplierMsgSettingService = this.getBean("supplierMsgSettingService", SupplierMsgSettingService.class);
        dnHeaderService = this.getBean("dnHeaderService", DnHeaderService.class);
        summaryMatchingReport = this.getBean("summaryMatchingReport", SummaryMatchingReport.class);
        outstandingMatchingReport = this.getBean("outstandingMatchingReport", OutstandingMatchingReport.class);
        buyerService = this.getBean("buyerService", BuyerService.class);
        poInvGrnDnMatchingGrnService = this.getBean("poInvGrnDnMatchingGrnService", PoInvGrnDnMatchingGrnService.class);
        poService = this.getBean("poService", PoService.class);
        invoiceService = this.getBean("invoiceService", InvoiceService.class);
        grnService = this.getBean("grnService", GrnService.class);
        poInvGrnDnMatchingDetailService = this.getBean("poInvGrnDnMatchingDetailService", PoInvGrnDnMatchingDetailService.class);
        supplierService = this.getBean("supplierService", SupplierService.class);
        invoiceExportingReport = this.getBean("invoiceExportingReport", InvoiceExportingReport.class);
        dnExportingReport = this.getBean("dnExportingReport", DnExportingReport.class);
        buyerResolutionReport = this.getBean("buyerResolutionReport", BuyerResolutionReport.class);
        supplierResolutionReport = this.getBean("supplierResolutionReport", SupplierResolutionReport.class);
        userProfileService = this.getBean("userProfileService", UserProfileService.class);
        grnHeaderService = this.getBean("grnHeaderService", GrnHeaderService.class);
        grnDisputeSummaryReport = this.getBean("grnDisputeSummaryReport", GrnDisputeSummaryReport.class);
        dnDiscrepancyReport = this.getBean("dnDiscrepancyReport", DnDiscrepancyReport.class);
        dnSupplierResolutionReport = this.getBean("dnSupplierResolutionReport", DnSupplierResolutionReport.class);
        dnBuyerResolutionReport = this.getBean("dnBuyerResolutionReport", DnBuyerResolutionReport.class);
        dnOutstandingReport = this.getBean("dnOutstandingReport", DnOutstandingReport.class);
        dnService = this.getBean("dnService", DnService.class);
        missingGrnReport = this.getBean("missingGrnReport", MissingGrnReport.class);
        poLocationService = this.getBean("poLocationService", PoLocationService.class);
        buyerStoreService = this.getBean("buyerStoreService", BuyerStoreService.class);
        customizedFairPriceBuyerResolutionAcceptedReportEngine = this.getBean("customizedFairPriceBuyerResolutionAcceptedReportEngine", CustomizedFairPriceBuyerResolutionAcceptedReportEngine.class);
        customizedFairPriceBuyerResolutionOthersReportEngine = this.getBean("customizedFairPriceBuyerResolutionOthersReportEngine", CustomizedFairPriceBuyerResolutionOthersReportEngine.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        rtvLocationService = this.getBean("rtvLocationService", RtvLocationService.class);
        missingGiReport = this.getBean("missingGiReport", MissingGiReport.class);
        rtvGiDnReport = this.getBean("rtvGiDnReport", RtvGiDnReport.class);
        controlParameterService = this.getBean("controlParameterService", ControlParameterService.class);
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
        
        try
        {
            List<BuyerHolder> buyers = buyerService.selectActiveBuyers();
            
            if (buyers != null && !buyers.isEmpty())
            {
                for (BuyerHolder buyer : buyers)
                {
                    this.processBuyer(buyer);
                }
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        
        
        try
        {
            List<SupplierHolder> suppliers = supplierService.selectActiveSuppliers();
            
            if (suppliers != null && !suppliers.isEmpty())
            {
                for (SupplierHolder supplier : suppliers)
                {
                    sendEmailForSupplierResolutionReport(supplier);
                    sendEmailForDnSupplierResolutionReport(supplier);
                }
                
                sendEmailForUnMatchedRecordsToSuppliers();
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return;
        }
        
        log.info("Process ended.");
    }
    
    
    private void processBuyer(BuyerHolder buyer)
    {
        List<BusinessRuleHolder> businessRuleList = null;
        boolean autoGenDnFromGi = false;
        try
        {
            businessRuleList = businessRuleService
                    .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                            buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN);
            
            autoGenDnFromGi = businessRuleService.isAutoGenDnFromGI(buyer.getBuyerOid());
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
        boolean enableSupplierToDispute = retriveRule("EnableSupplierToDispute", businessRuleList) != null;
        
        //send email for matching
        try
        {
            sendEmailForMatchedRecords(buyer);
            sendEmailForInvoiceExportingReport(buyer, businessRuleList);
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
        if (enableSupplierToDispute)
        {
            sendEmailForSummaryReport(buyer, businessRuleList);
            sendEmailForOutstandingReport(buyer, businessRuleList);
            sendEmailForBuyerResolutionReport(buyer, businessRuleList);
            sendEmailForMatchingDiscrepancyReport(buyer, businessRuleList);
        }
        else
        {
            sendEmailForUnity(buyer);
        }
        
        //send email for dn.
        sendEmailForDnExportingReport(buyer, businessRuleList);
        if (autoGenDnFromGi)
        {
            sendEmailForDnDiscrepancyReport(buyer);
            sendEmailForDnBuyerResolutionReport(buyer);
            sendEmailForDnOutstandingReport(buyer);
        }
        
        //send email for grn
        this.processGrnDisputeEmail(buyer);
        this.processMissingGrnNotificationEmail(buyer);
        
        //send email for gi
        this.processMissingGiNotificationEmail(buyer);
        
        //send rtv-gi-dn matching report
        this.processRtvGiDnExceptionReport(buyer);
        
    }
    
    
    private void sendEmailForSummaryReport(BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList)
    {
        Date today = new Date();
        Date fromDate = DateUtil.getInstance().getFirstTimeOfDay(today);
        Date toDate = DateUtil.getInstance().getLastTimeOfDay(today);
        List<File> files = null;
        File zipFile = null;
        try
        {
            List<PoInvGrnDnMatchingHolder> matchingsOfToday = poInvGrnDnMatchingService.
                    selectMatchingRecordByMatchingDateRangeUnionAllPending(buyer.getBuyerOid(), fromDate, toDate, null);
            List<MatchingReportParameter> parameters = convertMatchingsToReportParameters(matchingsOfToday, businessRuleList);
            if (parameters == null || parameters.isEmpty())
            {
                return;
            }
            
            String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "MatchingJobRecipients");
            if (emails == null)
            {
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
           
            String reportName = "Status of Matching Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
            String subject = "Status of Matching Report - " + buyer.getBuyerCode();
            String template =  "empty_email_template.vm";
            String zipFilenname = "Status of Matching Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
            byte[] result = summaryMatchingReport.exportExcel(parameters, buyer, "Status of Matching Report");
            int zipFileSizeExceed = getLimistSize();
            
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                    map, new String[]{"Status of Matching Report - " + buyer.getBuyerCode() + " - " + ts + ".xls"}, new byte[][]{result});
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
            try
            {
                if (files != null)
                {
                    for (File file : files)
                    {
                        FileUtil.getInstance().deleleAllFile(file);
                    }
                }
                if (zipFile != null)
                {
                    FileUtil.getInstance().deleleAllFile(zipFile);
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }
    
    
    private void sendEmailForInvoiceExportingReport(BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList)
    {
        Date today = new Date();
        Date fromDate = DateUtil.getInstance().getFirstTimeOfDay(today);
        Date toDate = DateUtil.getInstance().getLastTimeOfDay(today);
        
        List<File> files = null;
        File zipFile = null;
        
        try
        {
            List<PoInvGrnDnMatchingHolder> matchingsOfToday = poInvGrnDnMatchingService.
                    selectApprovedMatchingRecordByInvStatusActionDateRange(buyer.getBuyerOid(), fromDate, toDate, null);
            List<MatchingReportParameter> parameters = convertMatchingsToReportParameters(matchingsOfToday, businessRuleList);
            if (parameters == null || parameters.isEmpty())
            {
                return;
            }
            
            String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "InvoiceExportingRecipients");
            if (emails == null)
            {
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            String reportName = "Invoice Export Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
            String subject = "Invoice Export Report - " + buyer.getBuyerCode();
            String template =  "empty_email_template.vm";
            String zipFilenname = "Invoice Export Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
            byte[] result = invoiceExportingReport.exportExcel(parameters, buyer);
            int zipFileSizeExceed = getLimistSize();
            
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, map, new String[]{reportName}, new byte[][]{result});
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
            try
            {
                if (files != null)
                {
                    for (File file : files)
                    {
                        FileUtil.getInstance().deleleAllFile(file);
                    }
                }
                if (zipFile != null)
                {
                    FileUtil.getInstance().deleleAllFile(zipFile);
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
        
    }
    
    
    private void sendEmailForDnExportingReport(BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList)
    {
        log.info("start to send DN exporting report for buyer [" + buyer.getBuyerCode() + "]");
        Date today = new Date();
        Date fromDate = DateUtil.getInstance().getFirstTimeOfDay(today);
        Date toDate = DateUtil.getInstance().getLastTimeOfDay(today);
        
        List<File> files = null;
        File zipFile = null;
        
        try
        {
            List<DnHeaderHolder> parameters = dnHeaderService.selectExportedDnByBuyerAndSupplierAndTimeRange(buyer.getBuyerOid(), null, fromDate, toDate);
            if (parameters == null || parameters.isEmpty())
            {
                log.info("find 0 records to send DN exporting report for buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            
            String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), FUNC_GROUP_DN, FUNC_ID_BACKEND, "DnExportingRecipients");
            if (emails == null)
            {
                log.info("find no email address to send DN exporting report for buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            
            
            
            String reportName = "DN Export Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
            String subject = "DN Export Report - " + buyer.getBuyerCode();
            String template =  "empty_email_template.vm";
            String zipFilenname = "DN Export Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
            byte[] result = dnExportingReport.exportExcel(parameters, buyer);
            int zipFileSizeExceed = getLimistSize();
            
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                    map, new String[]{reportName}, new byte[][]{result});
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
            try
            {
                if (files != null)
                {
                    for (File file : files)
                    {
                        FileUtil.getInstance().deleleAllFile(file);
                    }
                }
                if (zipFile != null)
                {
                    FileUtil.getInstance().deleleAllFile(zipFile);
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }
    
    
    private void sendEmailToFinancialUsersForMatching(final BuyerHolder buyer, List<PoInvGrnDnMatchingHolder> matchingList, 
            List<BusinessRuleHolder> businessRuleList, final int emailType) throws Exception
    {
        boolean isSendMatchingResolutionAndOutstandingByGroup = businessRuleService.isSendMatchingResolutionAndOutstandingByGroup(buyer.getBuyerOid());
        if (!isSendMatchingResolutionAndOutstandingByGroup)
        {
            return;
        }
        
        Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> userDataMap = new HashMap<BigDecimal, List<PoInvGrnDnMatchingHolder>>();
        Map<BigDecimal, UserProfileExHolder> userMap = new HashMap<BigDecimal, UserProfileExHolder>();
        
        for (PoInvGrnDnMatchingHolder matching : matchingList)
        {
            List<UserProfileExHolder> canCloseUsers = userProfileService.selectCanCloseUsersByMatchingOid(matching.getMatchingOid());
            
            if (canCloseUsers != null && !canCloseUsers.isEmpty())
            {
                log.info("find " + canCloseUsers.size() + " users to send matching resolution/outstanding report to for matchingOid " + matching.getMatchingOid() + " and for buyer [" + buyer.getBuyerCode() + "]");
                for (UserProfileExHolder user : canCloseUsers)
                {
                    log.info("login id " + user.getLoginId());
                    if (userDataMap.containsKey(user.getUserOid()))
                    {
                        if (!userDataMap.get(user.getUserOid()).contains(matching))
                        {
                            userDataMap.get(user.getUserOid()).add(matching);
                        }
                    }
                    else
                    {
                        List<PoInvGrnDnMatchingHolder> list = new ArrayList<PoInvGrnDnMatchingHolder>();
                        list.add(matching);
                        userDataMap.put(user.getUserOid(), list);
                        userMap.put(user.getUserOid(), user);
                    }
                    userMap.get(user.getUserOid()).setPriceDiscrepancy(true);
                }
            }
            else
            {
                log.info("find 0 user to send matching resolution/outstanding reprot to for matchingOid " + matching.getMatchingOid() + "and  for buyer [" + buyer.getBuyerCode() + "]");
            }
        }
        
        if (userDataMap == null || userDataMap.isEmpty())
        {
            return;
        }
        
        Map<List<PoInvGrnDnMatchingHolder>, List<UserProfileExHolder>> dataMap = new HashMap<List<PoInvGrnDnMatchingHolder>, List<UserProfileExHolder>>();
        
        for (Entry<BigDecimal, List<PoInvGrnDnMatchingHolder>> entry : userDataMap.entrySet())
        {
            UserProfileExHolder user = userMap.get(entry.getKey());
            if (user.getEmail().trim().isEmpty())
            {
                continue;
            }
            
            if (dataMap.containsKey(entry.getValue()))
            {
                dataMap.get(entry.getValue()).add(user);
            }
            else
            {
                List<UserProfileExHolder> list = new ArrayList<UserProfileExHolder>();
                list.add(user);
                dataMap.put(entry.getValue(), list);
            }
        }
        
        if (dataMap == null || dataMap.isEmpty())
        {
            return;
        }
        
        for (Map.Entry<List<PoInvGrnDnMatchingHolder>, List<UserProfileExHolder>> entry : dataMap.entrySet())
        {
            List<UserProfileExHolder> userList = entry.getValue();
            List<String> emailList = new ArrayList<String>();
            
            for (UserProfileExHolder user : userList)
            {
                emailList.add(user.getEmail());
            }
            
            
            String[] emails = new String[emailList.size()];
            for (int i = 0; i < emailList.size(); i++)
            {
                emails[i] = emailList.get(i);
            }
            
            if (emails.length == 0)
            {
                continue;
            }
            
            if (emailType == 1)
            {
                sendEmailForOutstandingReport(buyer, convertMatchingsToReportParameters(entry.getKey(), businessRuleList), emails);
            }
            else if (emailType == 2)
            {
                sendEmailForBuyerResolutionReport(buyer, businessRuleList, entry.getKey(), emails);
            }
        }
    }
    
    
    private void sendEmailForOutstandingReport(BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList)
    {
        try
        {
            List<PoInvGrnDnMatchingHolder> matchings = poInvGrnDnMatchingService.selectOutstandingRecords(buyer.getBuyerOid(), null, null, null, null);
            List<MatchingReportParameter> parameters = convertMatchingsToReportParameters(matchings, businessRuleList);
            sendEmailForOutstandingReport(buyer, parameters, retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "OutstandingRecipients"));
            
            
            sendEmailToFinancialUsersForMatching(buyer, matchings, businessRuleList, 1);
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
    }
    
    
   
   
    private void sendEmailForOutstandingReport(BuyerHolder buyer, List<MatchingReportParameter> parameters, String[] emails)
    {
        List<File> files = null;
        File zipFile = null;
        
        try
        {
            if (parameters == null || parameters.isEmpty())
            {
                return;
            }
            
            if (emails == null)
            {
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            String reportName = "Matching Outstanding Resolution Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
            String subject = "Matching Outstanding Resolution Report - " + buyer.getBuyerCode();
            String template =  "empty_email_template.vm";
            String zipFilenname = "Matching Outstanding Resolution Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
            byte[] result = outstandingMatchingReport.exportExcel(parameters, buyer);
            int zipFileSizeExceed = getLimistSize();
            
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template,  map, new String[]{reportName}, new byte[][]{result});
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
            try
            {
                if (files != null)
                {
                    for (File file : files)
                    {
                        FileUtil.getInstance().deleleAllFile(file);
                    }
                }
                if (zipFile != null)
                {
                    FileUtil.getInstance().deleleAllFile(zipFile);
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }
    
    
    private void sendEmailForBuyerResolutionReport(BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList)
    {
        try
        {
            List<PoInvGrnDnMatchingHolder> allList = poInvGrnDnMatchingService.selectBuyerResolutionRecords(buyer.getBuyerOid(), new Date(), null, null);
            String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "ResolutionRecipients");
            sendEmailForBuyerResolutionReport(buyer, businessRuleList, allList, emails);
            sendEmailToFinancialUsersForMatching(buyer, allList, businessRuleList, 2);
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }
    
    
    
    private void sendEmailForBuyerResolutionReport(BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList, 
            List<PoInvGrnDnMatchingHolder> allList, String[] emails)
    {
        if (emails == null || emails.length == 0)
        {
            return;
        }
        List<File> files = null;
        File zipFile = null;
        try
        {
            List<PoInvGrnDnMatchingHolder> acceptedList = new ArrayList<PoInvGrnDnMatchingHolder>();
            
            List<PoInvGrnDnMatchingHolder> rejectedList = new ArrayList<PoInvGrnDnMatchingHolder>();
            
            List<PoInvGrnDnMatchingHolder> revisedList = new ArrayList<PoInvGrnDnMatchingHolder>();
            
            List<PoInvGrnDnMatchingHolder> buyerAcceptedList = new ArrayList<PoInvGrnDnMatchingHolder>();
            
            List<PoInvGrnDnMatchingHolder> otherList = new ArrayList<PoInvGrnDnMatchingHolder>();
            
            if (allList == null || allList.isEmpty())
            {
                return;
            }
            
            for (PoInvGrnDnMatchingHolder matching : allList)
            {
                if (matching.getSupplierStatus().equals(PoInvGrnDnMatchingSupplierStatus.ACCEPTED))
                {
                    acceptedList.add(matching);
                }
                else if (matching.getSupplierStatus().equals(PoInvGrnDnMatchingSupplierStatus.REJECTED))
                {
                    rejectedList.add(matching);
                }
                else
                {
                    revisedList.add(matching);
                }
                
                if (matching.getBuyerStatus().equals(PoInvGrnDnMatchingBuyerStatus.ACCEPTED)
                        || (matching.getRevised() && !PoInvGrnDnMatchingStatus.MATCHED.equals(matching.getMatchingStatus())))
                {
                    buyerAcceptedList.add(matching);
                }
                else
                {
                    otherList.add(matching);
                }
            }
            
            List<MatchingReportParameter> acceptedReports = convertMatchingsToReportParameters(acceptedList, businessRuleList);
            List<MatchingReportParameter> rejectedReports = convertMatchingsToReportParameters(rejectedList, businessRuleList);
            List<MatchingReportParameter> revisedReports = convertMatchingsToReportParameters(revisedList, businessRuleList);
            List<MatchingReportParameter> buyerAcceptedReports = convertMatchingsToReportParameters(buyerAcceptedList, businessRuleList);
            List<MatchingReportParameter> otherReports = convertMatchingsToReportParameters(otherList, businessRuleList);
            
            if (acceptedReports == null && rejectedReports == null && revisedReports == null && buyerAcceptedReports == null && otherReports == null)
            {
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            
            ReportEngineParameter<List<MatchingReportParameter>> buyerAcceptedParam = this.retrieveParameter(buyerAcceptedReports, buyer);
            ReportEngineParameter<List<MatchingReportParameter>> othersParam = this.retrieveParameter(otherReports, buyer);
            
            
            byte[] excelFile = null;
            byte[] buyerAcceptedFile = null;
            byte[] otherFile = null;
            
            excelFile = buyerResolutionReport.exportExcel(acceptedReports, rejectedReports, revisedReports, buyer);
            if (buyerAcceptedReports != null)
            {
                buyerAcceptedFile = customizedFairPriceBuyerResolutionAcceptedReportEngine.generateReport(buyerAcceptedParam, DefaultReportEngine.PDF_TYPE_STANDARD);
            }
            if (otherReports != null)
            {
                otherFile = customizedFairPriceBuyerResolutionOthersReportEngine.generateReport(othersParam, DefaultReportEngine.PDF_TYPE_STANDARD);
            }
            
            files = new ArrayList<File>();
            
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            FileUtil.getInstance().writeByteToDisk(excelFile, path + "Matching Resolution Report To Buyer - " + buyer.getBuyerCode() + " - " + ts + ".xls");
            files.add(new File(path + "Matching Resolution Report To Buyer - " + buyer.getBuyerCode() + " - " + ts + ".xls"));
            
            if (null != buyerAcceptedFile)
            {
                FileUtil.getInstance().writeByteToDisk(buyerAcceptedFile, path + "Matching Resolution Report To Buyer(Buyer Accepted) - " + buyer.getBuyerCode() + " - " + ts + ".pdf");
                files.add(new File(path + "Matching Resolution Report To Buyer(Buyer Accepted) - " + buyer.getBuyerCode() + " - " + ts + ".pdf"));
            }
            if (null != otherFile)
            {
                FileUtil.getInstance().writeByteToDisk(otherFile, path + "Matching Resolution Report To Buyer(Buyer Rejected + Sup Accepted) - " + buyer.getBuyerCode() + " - " + ts + ".pdf");
                files.add(new File(path + "Matching Resolution Report To Buyer(Buyer Rejected + Sup Accepted) - " + buyer.getBuyerCode() + " - " + ts + ".pdf"));
            }
            
            GZIPHelper.getInstance().doZip(files, path, "Matching Resolution Report To Buyer - " + buyer.getBuyerCode() + " - " + ts + ".zip");
            zipFile = new File(path + "Matching Resolution Report To Buyer - " + buyer.getBuyerCode() + " - " + ts + ".zip");
            
            emailEngine.sendEmailWithAttachedDocuments(emails, "Matching Resolution Report To Buyer - " + buyer.getBuyerCode(), "empty_email_template.vm", 
                    map, new File[]{zipFile});
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
            try
            {
                if (files != null)
                {
                    for (File file : files)
                    {
                        FileUtil.getInstance().deleleAllFile(file);
                    }
                }
                if (zipFile != null)
                {
                    FileUtil.getInstance().deleleAllFile(zipFile);
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }

    
    private void sendEmailForMatchingDiscrepancyReport(final BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList)
    {
        log.info("start to send email for matching discrepancy report for buyer [" + buyer.getBuyerCode() + "]");
        List<File> files = null;
        List<File> zipFiles = null;
        try
        {
            files = new ArrayList<File>();
            zipFiles = new ArrayList<File>();
            List<PoInvGrnDnMatchingHolder> matchingList = poInvGrnDnMatchingService.selectDiscrepancyRecordByBuyerAndSupplier(buyer.getBuyerOid(), null, null, null);
            if (matchingList == null || matchingList.isEmpty())
            {
                log.info("find 0 matching record to send discrepancy reprot for buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            log.info("find "+ matchingList.size() + " matching records to send discrepancy report for buyer [" + buyer.getBuyerCode() + "]");
            
            List<PoInvGrnDnMatchingHolder> priceDiscrepancyList = new ArrayList<PoInvGrnDnMatchingHolder>();
            List<PoInvGrnDnMatchingHolder> qtyDiscrepancyList = new ArrayList<PoInvGrnDnMatchingHolder>();
            
            for (PoInvGrnDnMatchingHolder matching : matchingList)
            {
                if (matching.getMatchingStatus().equals(PoInvGrnDnMatchingStatus.PRICE_UNMATCHED))
                {
                    priceDiscrepancyList.add(matching);
                }
                else if (matching.getMatchingStatus().equals(PoInvGrnDnMatchingStatus.QTY_UNMATCHED))
                {
                    qtyDiscrepancyList.add(matching);
                }
                else if (matching.getMatchingStatus().equals(PoInvGrnDnMatchingStatus.UNMATCHED))
                {
                    if (matching.getPriceStatus().equals(PoInvGrnDnMatchingPriceStatus.PENDING))
                    {
                        priceDiscrepancyList.add(matching);
                    }
                    if (matching.getQtyStatus().equals(PoInvGrnDnMatchingQtyStatus.PENDING))
                    {
                        qtyDiscrepancyList.add(matching);
                    }
                }
            }
            
            String[] priceDiscrepancyEmails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "PriceDiscrepancyRecipients");
            String[] qtyDiscrepancyEmails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "QtyDiscrepancyRecipients");
            
            if (priceDiscrepancyEmails != null && !priceDiscrepancyList.isEmpty())
            {
                String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                List<MatchingReportParameter> parameters = convertMatchingsToReportParameters(priceDiscrepancyList, businessRuleList);
                
                String reportName = "Matching Discrepancy Report(Price) - " + buyer.getBuyerCode() + " - " + ts + ".xls";
                String subject = "Matching Discrepancy Report(Price) - " + buyer.getBuyerCode();
                String template =  "empty_email_template.vm";
                String zipFilenname = "Matching Discrepancy Report(Price) - " + buyer.getBuyerCode() + " - " + ts +  ".zip";
                byte[] result = summaryMatchingReport.exportExcel(parameters, buyer, "Matching Discrepancy Report");
                int zipFileSizeExceed = getLimistSize();
                
                String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                {
                    files.add(new File(path + reportName));
                    zipFiles.add(new File(path + zipFilenname));
                    File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                    emailEngine.sendEmailWithAttachedDocuments(priceDiscrepancyEmails, subject, template, new HashMap<String, Object>(), file);
                }
                else
                {
                    emailEngine.sendEmailWithAttachedDocuments(priceDiscrepancyEmails, subject, template, 
                        new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                }
            }
            if (qtyDiscrepancyEmails != null && !qtyDiscrepancyList.isEmpty())
            {
                String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                String reportName = "Matching Discrepancy Report(Qty) - " + buyer.getBuyerCode() + " - " + ts + ".xls";
                String subject = "Matching Discrepancy Report(Qty) - " + buyer.getBuyerCode();
                String template =  "empty_email_template.vm";
                String zipFilenname = "Matching Discrepancy Report(Qty) - " + buyer.getBuyerCode() + " - " + ts +  ".zip";
                List<MatchingReportParameter> parameters = convertMatchingsToReportParameters(qtyDiscrepancyList, businessRuleList);
                byte[] result = summaryMatchingReport.exportExcel(parameters, buyer, "Matching Discrepancy Report");
                int zipFileSizeExceed = getLimistSize();
                
                String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                
                if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                {
                    files.add(new File(path + reportName));
                    zipFiles.add(new File(path + zipFilenname));
                    File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                    emailEngine.sendEmailWithAttachedDocuments(priceDiscrepancyEmails, subject, template, new HashMap<String, Object>(), file);
                }
                else
                {
                    emailEngine.sendEmailWithAttachedDocuments(qtyDiscrepancyEmails, subject, template, 
                            new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                }
            }
            
            boolean isSendMatchingDiscrepancyReportToUser = businessRuleService.isSendMatchingDiscrepancyReportToUser(buyer.getBuyerOid());
            
            if (!isSendMatchingDiscrepancyReportToUser)
            {
                return;
            }
            
            Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> userDataMap = new HashMap<BigDecimal, List<PoInvGrnDnMatchingHolder>>();
            Map<BigDecimal, UserProfileExHolder> userMap = new HashMap<BigDecimal, UserProfileExHolder>();
            
            for (PoInvGrnDnMatchingHolder matching : matchingList)
            {
                List<UserProfileExHolder> priceAuditUsers = userProfileService.selectPriceAuditUsersByMatchingOid(matching.getMatchingOid());
                List<UserProfileExHolder> qtyAuditUsers = userProfileService.selectQtyAuditUsersByMatchingOid(matching.getMatchingOid());
                
                if (priceAuditUsers != null && !priceAuditUsers.isEmpty())
                {
                    log.info("find " + priceAuditUsers.size() + " users to send price discrepancy report to for matchingOid " + matching.getMatchingOid() + " and for buyer [" + buyer.getBuyerCode() + "]");
                    for (UserProfileExHolder user : priceAuditUsers)
                    {
                        log.info("login id " + user.getLoginId());
                        if (userDataMap.containsKey(user.getUserOid()))
                        {
                            if (!userDataMap.get(user.getUserOid()).contains(matching))
                            {
                                userDataMap.get(user.getUserOid()).add(matching);
                            }
                        }
                        else
                        {
                            List<PoInvGrnDnMatchingHolder> list = new ArrayList<PoInvGrnDnMatchingHolder>();
                            list.add(matching);
                            userDataMap.put(user.getUserOid(), list);
                            userMap.put(user.getUserOid(), user);
                        }
                        userMap.get(user.getUserOid()).setPriceDiscrepancy(true);
                    }
                }
                else
                {
                    log.info("find 0 user to send price discrepancy reprot to for matchingOid " + matching.getMatchingOid() + "and  for buyer [" + buyer.getBuyerCode() + "]");
                }
                
                if (qtyAuditUsers != null && !qtyAuditUsers.isEmpty())
                {
                    log.info("find " + qtyAuditUsers.size() + " users to send qty discrepancy report to for matchingOid " + matching.getMatchingOid() + "and  for buyer [" + buyer.getBuyerCode() + "]");
                    for (UserProfileExHolder user : qtyAuditUsers)
                    {
                        log.info("login id " + user.getLoginId());
                        if (userDataMap.containsKey(user.getUserOid()))
                        {
                            if (!userDataMap.get(user.getUserOid()).contains(matching))
                            {
                                userDataMap.get(user.getUserOid()).add(matching);
                            }
                        }
                        else
                        {
                            List<PoInvGrnDnMatchingHolder> list = new ArrayList<PoInvGrnDnMatchingHolder>();
                            list.add(matching);
                            userDataMap.put(user.getUserOid(), list);
                            userMap.put(user.getUserOid(), user);
                        }
                        userMap.get(user.getUserOid()).setQtyDiscrepancy(true);
                    }
                }
                else
                {
                    log.info("find 0 user to send qty discrepancy reprot to for matchingOid " + matching.getMatchingOid() + "and  for buyer [" + buyer.getBuyerCode() + "]");
                }
            }
            
            if (userDataMap == null || userDataMap.isEmpty())
            {
                return;
            }
            
            Map<List<PoInvGrnDnMatchingHolder>, List<UserProfileExHolder>> dataMap = new HashMap<List<PoInvGrnDnMatchingHolder>, List<UserProfileExHolder>>();
            
            for (Entry<BigDecimal, List<PoInvGrnDnMatchingHolder>> entry : userDataMap.entrySet())
            {
                UserProfileExHolder user = userMap.get(entry.getKey());
                if (user.getEmail().trim().isEmpty())
                {
                    continue;
                }
                
                if (dataMap.containsKey(entry.getValue()))
                {
                    dataMap.get(entry.getValue()).add(user);
                }
                else
                {
                    List<UserProfileExHolder> list = new ArrayList<UserProfileExHolder>();
                    list.add(user);
                    dataMap.put(entry.getValue(), list);
                }
            }
            
            if (dataMap == null || dataMap.isEmpty())
            {
                return;
            }
            
            final List<Map<String, Object>> emailDataList = new ArrayList<Map<String, Object>>();
            
            for (Map.Entry<List<PoInvGrnDnMatchingHolder>, List<UserProfileExHolder>> entry : dataMap.entrySet())
            {
                List<MatchingReportParameter> parameters = convertMatchingsToReportParameters(entry.getKey(), businessRuleList);
                if (parameters == null || parameters.isEmpty())
                {
                    continue;
                }
                int type = 0; //1 price, 2 qty, 3 price & qty, 4 none
                List<UserProfileExHolder> userList = entry.getValue();
                List<String> emailList = new ArrayList<String>();
                
                for (UserProfileExHolder user : userList)
                {
                    emailList.add(user.getEmail());
                    
                    int currType = 0;
                    
                    if (user.isPriceDiscrepancy() && !user.isQtyDiscrepancy())
                    {
                        currType = 1;
                    }
                    if (!user.isPriceDiscrepancy() && user.isQtyDiscrepancy())
                    {
                        currType = 2;
                    }
                    if (user.isPriceDiscrepancy() && user.isQtyDiscrepancy())
                    {
                        currType = 3;
                    }
                    
                    if (type == 0)
                    {
                        type = currType;
                        continue;
                    }
                    if (type == 4)
                    {
                        continue;
                    }
                    if (type != currType)
                    {
                        type = 4;
                        continue;
                    }
                }
                
                
                String[] emails = new String[emailList.size()];
                for (int i = 0; i < emailList.size(); i++)
                {
                    emails[i] = emailList.get(i);
                }
                
                if (emails.length == 0)
                {
                    continue;
                }
                
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("emails", emails);
                map.put("parameters", parameters);
                map.put("type", type);
                
                emailDataList.add(map);
            }
            
            if (emailDataList.isEmpty())
            {
                return;
            }
            
            final int zipFileSizeExceed = getLimistSize(); 
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    List<File> files = null;
                    List<File> zipFiles = null;
                  
                    try
                    {
                        files = new ArrayList<File>();
                        zipFiles = new ArrayList<File>();
                        for (Map<String, Object> emailData : emailDataList)
                        {
                            String[] emails = (String[]) emailData.get("emails");
                            int type = (Integer) emailData.get("type");
                            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                            List<MatchingReportParameter> parameters = (List<MatchingReportParameter>) emailData.get("parameters");
                            String subjectEx = "";
                            
                            if (type == 1)
                            {
                                subjectEx = "(Price)";
                            }
                            else if (type == 2)
                            {
                                subjectEx = "(Qty)";
                            }
                            else if (type == 3)
                            {
                                subjectEx = "(Price&Qty)";
                            }
                            
                            String reportName = "Matching Discrepancy Report" + subjectEx + " - " + buyer.getBuyerCode() + " - " + ts + ".xls";
                            String subject = "Matching Discrepancy Report" + subjectEx + " - " + buyer.getBuyerCode();
                            String template =  "empty_email_template.vm";
                            String zipFilenname = "Matching Discrepancy Report" + subjectEx + " - " + buyer.getBuyerCode() + " - " + ts + ".zip";
                            byte[] result = summaryMatchingReport.exportExcel(parameters, buyer, "Matching Discrepancy Report");
                            
                            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                            
                            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                            {
                                files.add(new File(path + reportName));
                                zipFiles.add(new File(path + zipFilenname));
                                File[] file = ZipFileForAttachFile(new byte[][]{result},  new String[]{reportName}, zipFilenname, path);
                                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                            }
                            else
                            {
                                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                                    new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                    finally
                    {
                         try
                         {
                             if (files != null)
                             {
                                 for (File file : files)
                                 {
                                     FileUtil.getInstance().deleleAllFile(file);
                                 }
                             }
                             if (zipFiles != null)
                             {
                                for (File zipFile : zipFiles)
                                {
                                    FileUtil.getInstance().deleleAllFile(zipFile);
                                }
                             }
                         }
                         catch (Exception e)
                         {
                             String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                             standardEmailSender.sendStandardEmail(ID, tickNo, e);
                         }
                    }
                }
            }).start();
            
            log.info("send discrepancy email for buyer [" + buyer.getBuyerCode() + "] end.");
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFiles != null)
                 {
                     for (File zipFile : zipFiles)
                     {
                         FileUtil.getInstance().deleleAllFile(zipFile);
                     }
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }
    }
    
    
    private void sendEmailForSupplierResolutionReport(SupplierHolder supplier)
    {
        List<File> files = null;
        File zipFile = null;
        try
        {
            List<PoInvGrnDnMatchingHolder> allList = poInvGrnDnMatchingService.selectSupplierResolutionRecords(supplier.getSupplierOid());
            
            List<PoInvGrnDnMatchingHolder> acceptedList = new ArrayList<PoInvGrnDnMatchingHolder>();
            
            List<PoInvGrnDnMatchingHolder> rejectedList = new ArrayList<PoInvGrnDnMatchingHolder>();
            
            if (allList == null || allList.isEmpty())
            {
                return;
            }
            
            for (PoInvGrnDnMatchingHolder matching : allList)
            {
                if (matching.getSupplierStatus().equals(PoInvGrnDnMatchingSupplierStatus.ACCEPTED))
                {
                    acceptedList.add(matching);
                }
                else
                {
                    rejectedList.add(matching);
                }
            }
            
            Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> acceptedMap = this.groupMatchingRecordsByBuyer(acceptedList);
            
            Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> rejectedMap = this.groupMatchingRecordsByBuyer(rejectedList);
            
            List<MatchingReportParameter> acceptedReports = new ArrayList<MatchingReportParameter>();
            List<MatchingReportParameter> rejectedReports = new ArrayList<MatchingReportParameter>();
            
            if (acceptedMap != null && !acceptedMap.isEmpty())
            {
                for (Map.Entry<BigDecimal, List<PoInvGrnDnMatchingHolder>> entry : acceptedMap.entrySet())
                {
                    List<BusinessRuleHolder> businessRules = businessRuleService
                                .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                                        entry.getKey(), MATCHING, PO_INV_GRN_DN);
                    acceptedReports.addAll(convertMatchingsToReportParameters(entry.getValue(), businessRules));
                }
            }
            
            if (rejectedMap != null && !rejectedMap.isEmpty())
            {
                for (Map.Entry<BigDecimal, List<PoInvGrnDnMatchingHolder>> entry : rejectedMap.entrySet())
                {
                    List<BusinessRuleHolder> businessRules = businessRuleService
                            .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                                    entry.getKey(), MATCHING, PO_INV_GRN_DN);
                    rejectedReports.addAll(convertMatchingsToReportParameters(entry.getValue(), businessRules));
                }
            }
            
            
            String[] emails = retrieveSupplierMsgEmailsByMsgType(supplier.getSupplierOid(), MsgType.INV.name());
            if (emails == null)
            {
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            String reportName = "Matching Resolution Report To Supplier - " + ts + ".xls";
            String subject = "Matching Resolution Report to Supplier";
            String template = "supplier_resolution_email_template.vm";
            String zipFilenname = "Matching Resolution Report To Supplier - " + ts + ".zip";
            byte[] result = supplierResolutionReport.exportExcel(acceptedReports, rejectedReports, supplier);
            int zipFileSizeExceed = getLimistSize();
            
            files = new ArrayList<File>();
            String path = mboxUtil.getSupplierMboxRoot() + File.separator + supplier.getMboxId() + File.separator;
            
            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                    map, new String[]{reportName}, new byte[][]{result});
            }
            
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFile != null)
                 {
                     FileUtil.getInstance().deleleAllFile(zipFile);
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }
        
    }
    
    
    private void sendEmailToFinancialUsersForDn(BuyerHolder buyer, List<DnHeaderHolder> dnHeaderList, int emailType) throws Exception
    {
        boolean isSendDnResolutionAndOutstandingByGroup = businessRuleService.isSendDnResolutionAndOutstandingByGroup(buyer.getBuyerOid());
        if (!isSendDnResolutionAndOutstandingByGroup)
        {
            return;
        }
        
        Map<BigDecimal, List<DnHeaderHolder>> userDataMap = new HashMap<BigDecimal, List<DnHeaderHolder>>();
        Map<BigDecimal, UserProfileExHolder> userMap = new HashMap<BigDecimal, UserProfileExHolder>();
        
        for (DnHeaderHolder header : dnHeaderList)
        {
            List<UserProfileExHolder> canCloseUsers = userProfileService.selectCanCloseUsersByDnOid(header.getDnOid());
            
            if (canCloseUsers != null && !canCloseUsers.isEmpty())
            {
                log.info("find " + canCloseUsers.size() + " users to send dn outstanding/resolution report to for dn " + header.getDnOid() + " and for buyer [" + buyer.getBuyerCode() + "]");
                for (UserProfileExHolder user : canCloseUsers)
                {
                    log.info("login id" + user.getLoginId());
                    if (userDataMap.containsKey(user.getUserOid()))
                    {
                        if (!userDataMap.get(user.getUserOid()).contains(header))
                        {
                            userDataMap.get(user.getUserOid()).add(header);
                        }
                    }
                    else
                    {
                        List<DnHeaderHolder> list = new ArrayList<DnHeaderHolder>();
                        list.add(header);
                        userDataMap.put(user.getUserOid(), list);
                        userMap.put(user.getUserOid(), user);
                    }
                    userMap.get(user.getUserOid()).setPriceDiscrepancy(true);
                }
            }
            else
            {
                log.info("find 0 user to send dn outstanding/resolution report to for dn " + header.getDnOid() + " and for buyer [" + buyer.getBuyerCode() + "]");
            }
        }
        
        if (userDataMap == null || userDataMap.isEmpty())
        {
            return;
        }
        
        Map<List<DnHeaderHolder>, List<UserProfileExHolder>> dataMap = new HashMap<List<DnHeaderHolder>, List<UserProfileExHolder>>();
        
        for (Entry<BigDecimal, List<DnHeaderHolder>> entry : userDataMap.entrySet())
        {
            UserProfileExHolder user = userMap.get(entry.getKey());
            String email = user.getEmail();
            if (email == null || email.trim().isEmpty())
            {
                continue;
            }
            
            if (dataMap.containsKey(entry.getValue()))
            {
                dataMap.get(entry.getValue()).add(user);
            }
            else
            {
                List<UserProfileExHolder> list = new ArrayList<UserProfileExHolder>();
                list.add(user);
                dataMap.put(entry.getValue(), list);
            }
        }
        
        if (dataMap == null || dataMap.isEmpty())
        {
            return;
        }
        
        
        for (Map.Entry<List<DnHeaderHolder>, List<UserProfileExHolder>> entry : dataMap.entrySet())
        {
            List<UserProfileExHolder> userList = entry.getValue();
            List<String> emailList = new ArrayList<String>();
            
            for (UserProfileExHolder user : userList)
            {
                emailList.add(user.getEmail());
            }
            
            
            String[] emails = new String[emailList.size()];
            for (int i = 0; i < emailList.size(); i++)
            {
                emails[i] = emailList.get(i);
            }
            
            if (emails.length == 0)
            {
                continue;
            }
            
            if(emailType == 1)
            {
                sendEmailForDnOutstandingReport(buyer, entry.getKey(), emails);
            }
            else if (emailType == 2)
            {
                sendEmailForDnBuyerResolutionReport(buyer, entry.getKey(), emails);
            }
        }
    }

    
    private void sendEmailForDnOutstandingReport(BuyerHolder buyer)
    {
        log.info("start to send email for dn outstanding report for buyer [" + buyer.getBuyerCode() + "]");
        
        try
        {
            List<DnHeaderHolder> headers = dnHeaderService.selectOutstandingRecordsByBuyer(buyer.getBuyerOid(), null, null, null, null);
            String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), "Dn", "Backend", "OutstandingRecipients");
            sendEmailForDnOutstandingReport(buyer, headers, emails);
            sendEmailToFinancialUsersForDn(buyer, headers, 1);
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }
    
    
    private void sendEmailForDnOutstandingReport(BuyerHolder buyer, List<DnHeaderHolder> headers, String[] emails)
    {
        log.info("start to send email for dn outstanding report for buyer [" + buyer.getBuyerCode() + "]");
        List<File> files = null;
        File zipFile = null;
        try
        {
            if (headers == null || headers.isEmpty())
            {
                log.info("find 0 outstanding dns for buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            log.info("find " + headers.size() + " outstanding dns for buyer [" + buyer.getBuyerCode() + "]");
            
            List<DnHolder> dns = new ArrayList<DnHolder>();
            
            for (DnHeaderHolder header : headers)
            {
                DnHolder dn = dnService.selectDnByKey(header.getDnOid());
                dns.add(dn);
            }
            
            Collections.sort(dns, new Comparator<DnHolder>() {
                
                @Override
                public int compare(DnHolder o1,
                        DnHolder o2)
                {
                    return o1.getDnHeader().calculateDayElapsed().compareTo(
                            o2.getDnHeader().calculateDayElapsed());
                }
            }
                    );
            
            if (emails == null)
            {
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            String reportName = "DN Outstanding Resolution Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
            String subject = "DN Outstanding Resolution Report - " + buyer.getBuyerCode();
            String template = "empty_email_template.vm";
            String zipFilenname = "DN Outstanding Resolution Report - " + buyer.getBuyerCode() + " - " + ts +  ".zip";
            byte[] result = dnOutstandingReport.exportExcel(dns, buyer);
            int zipFileSizeExceed = getLimistSize();
            
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                    map, new String[]{reportName}, new byte[][]{result});
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFile != null)
                 {
                     FileUtil.getInstance().deleleAllFile(zipFile);
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }
    }
    
    
    private void sendEmailForDnDiscrepancyReport(final BuyerHolder buyer)
    {
        log.info("start to send dn discrepancy report for buyer [" + buyer.getBuyerCode() + "]");
        List<File> files = null;
        List<File> zipFiles = null;
        try
        {
            List<DnHeaderHolder> dnHeaderList = dnHeaderService.selectDisputedAndAuditUnfinishedRecord(buyer.getBuyerOid(), null);
            
            if(dnHeaderList == null || dnHeaderList.isEmpty())
            {
                log.info("find 0 dn record to send discrepancy report for buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            log.info("find " + dnHeaderList.size() + " dn records to send discrepancy reprot for buyer [" + buyer.getBuyerCode() + "]");
            
            List<DnHeaderHolder> priceDiscrepancyList = new ArrayList<DnHeaderHolder>();
            List<DnHeaderHolder> qtyDiscrepancyList = new ArrayList<DnHeaderHolder>();
            
            for (DnHeaderHolder header : dnHeaderList)
            {
                DnHolder dn = dnService.selectDnByKey(header.getDnOid());
                if (!dn.priceAuditFinished())
                {
                    priceDiscrepancyList.add(header);
                }
                
                if (!dn.qtyAuditFinished())
                { 
                    qtyDiscrepancyList.add(header);
                }
            }
            
            String[] priceDiscrepancyEmails = retrieveEmailsForReport(buyer.getBuyerOid(), FUNC_GROUP_DN, FUNC_ID_BACKEND, "PriceDiscrepancyRecipients");
            String[] qtyDiscrepancyEmails = retrieveEmailsForReport(buyer.getBuyerOid(), FUNC_GROUP_DN, FUNC_ID_BACKEND, "QtyDiscrepancyRecipients");
            
            final int zipFileSizeExceed = getLimistSize();
            files = new ArrayList<File>();
            zipFiles = new ArrayList<File>();
            
            if (priceDiscrepancyEmails != null && !priceDiscrepancyList.isEmpty())
            {
                String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                String reportName = "DN Discrepancy Report(Price) - " + buyer.getBuyerCode() + " - " + ts + ".xls";
                String subject = "DN Discrepancy Report(Price) - " + buyer.getBuyerCode();
                String template = "empty_email_template.vm";
                String zipFilenname =  "DN Discrepancy Report(Price) - " + buyer.getBuyerCode() + " - " + ts + ".zip";
                byte[] result = dnDiscrepancyReport.exportExcel(priceDiscrepancyList, buyer);
                
                String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                
                if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                {
                    files.add(new File(path + reportName));
                    zipFiles.add(new File(path + zipFilenname));
                    File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                    emailEngine.sendEmailWithAttachedDocuments(priceDiscrepancyEmails, subject, template, new HashMap<String, Object>(), file);
                }
                else
                {
                    emailEngine.sendEmailWithAttachedDocuments(priceDiscrepancyEmails, subject, template, 
                        new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                }
            }
            if (qtyDiscrepancyEmails != null && !qtyDiscrepancyList.isEmpty())
            {
                String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                String reportName = "DN Discrepancy Report(Qty) - " + buyer.getBuyerCode() + " - " + ts + ".xls";
                String subject = "DN Discrepancy Report(Qty) - " + buyer.getBuyerCode();
                String template = "empty_email_template.vm";
                String zipFilenname = "DN Discrepancy Report(Qty) - " + buyer.getBuyerCode() + " - " + ts +  ".zip";
                byte[] result = dnDiscrepancyReport.exportExcel(qtyDiscrepancyList, buyer);
                
                String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                
                if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                {
                    files.add(new File(path + reportName));
                    zipFiles.add(new File(path + zipFilenname));
                    File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                    emailEngine.sendEmailWithAttachedDocuments(priceDiscrepancyEmails, subject, template, new HashMap<String, Object>(), file);
                }
                else
                {
                    emailEngine.sendEmailWithAttachedDocuments(qtyDiscrepancyEmails, subject, template, 
                        new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                }
            }
            
            boolean isSendDnDiscrepancyReportToUser = businessRuleService.isSendDnDiscrepancyReportToUser(buyer.getBuyerOid());
            if (!isSendDnDiscrepancyReportToUser)
            {
                return;
            }
            
            Map<BigDecimal, List<DnHeaderHolder>> userDataMap = new HashMap<BigDecimal, List<DnHeaderHolder>>();
            Map<BigDecimal, UserProfileExHolder> userMap = new HashMap<BigDecimal, UserProfileExHolder>();
            
            for (DnHeaderHolder header : dnHeaderList)
            {
                DnHolder dn = dnService.selectDnByKey(header.getDnOid());
                List<UserProfileExHolder> priceAuditUsers = null;
                List<UserProfileExHolder> qtyAuditUsers = null;
                if (!dn.priceAuditFinished())
                {
                    priceAuditUsers = userProfileService.selectPriceAuditUsersByDnOid(header.getDnOid());
                }
                else
                {
                    log.info("no need to send price discrepancy email for dn " + dn.getDnHeader().getDnOid());
                }
                
                if (!dn.qtyAuditFinished())
                {
                    qtyAuditUsers = userProfileService.selectQtyAuditUsersByDnOid(header.getDnOid());
                }
                else
                {
                    log.info("no need to send qty discrepancy email for dn " + dn.getDnHeader().getDnOid());
                }
                
                if (priceAuditUsers != null && !priceAuditUsers.isEmpty())
                {
                    log.info("find " + priceAuditUsers.size() + " users to send price discrepancy report to for dn " + header.getDnOid() + " and for buyer [" + buyer.getBuyerCode() + "]");
                    for (UserProfileExHolder user : priceAuditUsers)
                    {
                        log.info("login id" + user.getLoginId());
                        if (userDataMap.containsKey(user.getUserOid()))
                        {
                            if (!userDataMap.get(user.getUserOid()).contains(header))
                            {
                                userDataMap.get(user.getUserOid()).add(header);
                            }
                        }
                        else
                        {
                            List<DnHeaderHolder> list = new ArrayList<DnHeaderHolder>();
                            list.add(header);
                            userDataMap.put(user.getUserOid(), list);
                            userMap.put(user.getUserOid(), user);
                        }
                        userMap.get(user.getUserOid()).setPriceDiscrepancy(true);
                    }
                }
                else
                {
                    log.info("find 0 user to send price discrepancy report to for dn " + header.getDnOid() + " and for buyer [" + buyer.getBuyerCode() + "]");
                }
                
                if (qtyAuditUsers != null && !qtyAuditUsers.isEmpty())
                {
                    log.info("find " + qtyAuditUsers.size() + " users to send qty discrepancy report to for dn " + header.getDnOid() + " and for buyer [" + buyer.getBuyerCode() + "]");
                    for (UserProfileExHolder user : qtyAuditUsers)
                    {
                        log.info("login id" + user.getLoginId());
                        if (userDataMap.containsKey(user.getUserOid()))
                        {
                            if (!userDataMap.get(user.getUserOid()).contains(header))
                            {
                                userDataMap.get(user.getUserOid()).add(header);
                            }
                        }
                        else
                        {
                            List<DnHeaderHolder> list = new ArrayList<DnHeaderHolder>();
                            list.add(header);
                            userDataMap.put(user.getUserOid(), list);
                            userMap.put(user.getUserOid(), user);
                        }
                        userMap.get(user.getUserOid()).setQtyDiscrepancy(true);
                    }
                }
                else
                {
                    log.info("find 0 user to send qty discrepancy report to for dn " + header.getDnOid() + " and for buyer [" + buyer.getBuyerCode() + "]");
                }
            }
            
            if (userDataMap == null || userDataMap.isEmpty())
            {
                return;
            }
            
            Map<List<DnHeaderHolder>, List<UserProfileExHolder>> dataMap = new HashMap<List<DnHeaderHolder>, List<UserProfileExHolder>>();
            
            for (Entry<BigDecimal, List<DnHeaderHolder>> entry : userDataMap.entrySet())
            {
                UserProfileExHolder user = userMap.get(entry.getKey());
                String email = user.getEmail();
                if (email == null || email.trim().isEmpty())
                {
                    continue;
                }
                
                if (dataMap.containsKey(entry.getValue()))
                {
                    dataMap.get(entry.getValue()).add(user);
                }
                else
                {
                    List<UserProfileExHolder> list = new ArrayList<UserProfileExHolder>();
                    list.add(user);
                    dataMap.put(entry.getValue(), list);
                }
            }
            
            if (dataMap == null || dataMap.isEmpty())
            {
                return;
            }
            
            final List<Map<String, Object>> emailDataList = new ArrayList<Map<String, Object>>();
            
            for (Map.Entry<List<DnHeaderHolder>, List<UserProfileExHolder>> entry : dataMap.entrySet())
            {
                if (entry.getValue() == null || entry.getValue().isEmpty())
                {
                    continue;
                }
                int type = 0; //1 price, 2 qty, 3 price & qty, 4 none
                List<UserProfileExHolder> userList = entry.getValue();
                List<String> emailList = new ArrayList<String>();
                
                for (UserProfileExHolder user : userList)
                {
                    emailList.add(user.getEmail());
                    
                    int currType = 0;
                    
                    if (user.isPriceDiscrepancy() && !user.isQtyDiscrepancy())
                    {
                        currType = 1;
                    }
                    if (!user.isPriceDiscrepancy() && user.isQtyDiscrepancy())
                    {
                        currType = 2;
                    }
                    if (user.isPriceDiscrepancy() && user.isQtyDiscrepancy())
                    {
                        currType = 3;
                    }
                    
                    if (type == 0)
                    {
                        type = currType;
                        continue;
                    }
                    if (type == 4)
                    {
                        continue;
                    }
                    if (type != currType)
                    {
                        type = 4;
                        continue;
                    }
                }
                
                
                String[] emails = new String[emailList.size()];
                for (int i = 0; i < emailList.size(); i++)
                {
                    emails[i] = emailList.get(i);
                }
                
                if (emails.length == 0)
                {
                    continue;
                }
                
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("emails", emails);
                map.put("parameters", entry.getKey());
                map.put("type", type);
                
                emailDataList.add(map);
            }
            
            if (emailDataList.isEmpty())
            {
                return;
            }
           
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    List<File> files = null;
                    List<File> zipFiles = null;
                        
                    try
                    {
                        files = new ArrayList<File>();
                        zipFiles = new ArrayList<File>();
                        for (Map<String, Object> emailData : emailDataList)
                        {
                            String[] emails = (String[]) emailData.get("emails");
                            int type = (Integer) emailData.get("type");
                            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                            List<DnHeaderHolder> parameters = (List<DnHeaderHolder>) emailData.get("parameters");
                            String subjectEx = "";
                            
                            if (type == 1)
                            {
                                subjectEx = "(Price)";
                            }
                            else if (type == 2)
                            {
                                subjectEx = "(Qty)";
                            }
                            else if (type == 3)
                            {
                                subjectEx = "(Price&Qty)";
                            }
                            
                            String reportName = "DN Discrepancy Report" + subjectEx + " - " + buyer.getBuyerCode() + " - " + ts + ".xls";
                            String subject = "DN Discrepancy Report" + subjectEx + " - " + buyer.getBuyerCode();
                            String template = "empty_email_template.vm";
                            String zipFilenname = "DN Discrepancy Report" + subjectEx + " - " + buyer.getBuyerCode() + " - " + ts +  ".zip";
                            byte[] result = dnDiscrepancyReport.exportExcel(parameters, buyer);
                            
                            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                            
                            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                            {
                                files.add(new File(path + reportName));
                                zipFiles.add( new File(path + zipFilenname));
                                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                            }
                            else
                            {
                                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                                        new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                    finally
                    {
                         try
                         {
                             if (files != null)
                             {
                                 for (File file : files)
                                 {
                                     FileUtil.getInstance().deleleAllFile(file);
                                 }
                             }
                             if (zipFiles != null)
                             {
                                 for (File zipFile : zipFiles)
                                 {
                                     FileUtil.getInstance().deleleAllFile(zipFile);
                                 }
                             }
                         }
                         catch (Exception e)
                         {
                             String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                             standardEmailSender.sendStandardEmail(ID, tickNo, e);
                         }
                    }
                }
            }).start();
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFiles != null)
                 {
                     for (File zipFile : zipFiles)
                     {
                         FileUtil.getInstance().deleleAllFile(zipFile);
                     }
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }
    }
    
    
    private void sendEmailForDnSupplierResolutionReport(SupplierHolder supplier)
    {
        Date today = new Date();
        Date fromDate = DateUtil.getInstance().getFirstTimeOfDay(today);
        Date toDate = DateUtil.getInstance().getLastTimeOfDay(today);
        List<File> files = null;
        File zipFile = null;
        try
        {
            List<DnHeaderHolder> dnHeaderList = dnHeaderService.selectClosedDnRecordsBySupplierAndTimeRange(supplier.getSupplierOid(), fromDate, toDate);
            
            if(dnHeaderList == null || dnHeaderList.isEmpty())
            {
                log.info("0 dn need to send resolution report email to supplier [" + supplier.getSupplierCode() + "]");
                return;
            }
            
            log.info("find " + dnHeaderList.size() + " dns need to send resolution report email to supplier [" + supplier.getSupplierCode() + "]");
            
            List<DnHolder> dnList = new ArrayList<DnHolder>();
            for (DnHeaderHolder header : dnHeaderList)
            {
                DnHolder dn = dnService.selectDnByKey(header.getDnOid());
                dnList.add(dn);
            }
            
            
            String[] emails = retrieveSupplierMsgEmailsByMsgType(supplier.getSupplierOid(), MsgType.DN.name());
            if (emails == null)
            {
                log.info("can not obtain DN msg email address for supplier [" + supplier.getSupplierCode() + "], will not send dn resolution email.");
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            String reportName = "Dn Resolution Report To Supplier - " + ts + ".xls";
            String subject = "Dn Resolution Report";
            String template =  "empty_email_template.vm";
            String zipFilenname = "Matching Resolution Report To Supplier - " + ts + ".zip";
            byte[] result = dnSupplierResolutionReport.exportExcel(dnList, supplier);
            int zipFileSizeExceed = getLimistSize();
            
            files = new ArrayList<File>();
            String path = mboxUtil.getSupplierMboxRoot() + File.separator + supplier.getMboxId() + File.separator;
            
            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                    map, new String[]{reportName},  new byte[][]{result});
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFile != null)
                 {
                     FileUtil.getInstance().deleleAllFile(zipFile);
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }
    }
    
    
    private void sendEmailForDnBuyerResolutionReport(BuyerHolder buyer)
    {
        try
        {
            List<DnHeaderHolder> dnHeaderList = dnHeaderService.selectResolutionRecordsByBuyer(buyer.getBuyerOid(), new Date(), null, null);
            
            if(dnHeaderList == null || dnHeaderList.isEmpty())
            {
                log.info("0 dn need to send resolution report email to buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            
            log.info("find " + dnHeaderList.size() + " dns need to send resolution report email to buyer [" + buyer.getBuyerCode() + "]");
            
            String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), "Dn", "Backend", "ResolutionRecipients");
            sendEmailForDnBuyerResolutionReport(buyer, dnHeaderList, emails);
            sendEmailToFinancialUsersForDn(buyer, dnHeaderList, 2);
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }

    
    private void sendEmailForDnBuyerResolutionReport(BuyerHolder buyer, List<DnHeaderHolder> dnHeaderList, String[] emails)
    {
        List<File> files = null;
        File zipFile = null;
        try
        {
            if(dnHeaderList == null || dnHeaderList.isEmpty())
            {
                log.info("0 dn need to send resolution report email to buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            
            log.info("find " + dnHeaderList.size() + " dns need to send resolution report email to buyer [" + buyer.getBuyerCode() + "]");
            
            List<DnHolder> dnList = new ArrayList<DnHolder>();
            for (DnHeaderHolder header : dnHeaderList)
            {
                DnHolder dn = dnService.selectDnByKey(header.getDnOid());
                dnList.add(dn);
            }
            
            
            if (emails == null)
            {
                log.info("can not obtain dn resolution email for buyer [" + buyer.getBuyerCode() + "], will not send dn resolution email.");
                return;
            }
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            String reportName = "DN Resolution Report To Buyer - " + buyer.getBuyerCode() + " - " + ts + ".xls";
            String subject = "DN Resolution Report - " + buyer.getBuyerCode();
            String template = "empty_email_template.vm";
            String zipFilenname =  "DN Resolution Report To Buyer - " + buyer.getBuyerCode() + " - " + ts + ".zip";
            byte[] result = dnBuyerResolutionReport.exportExcel(dnList, buyer);
            int zipFileSizeExceed = getLimistSize();
            
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile(new byte[][] {result}, new String[] {reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject,
                    template, map, new String[] {reportName},new byte[][] {result});
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFile != null)
                 {
                     FileUtil.getInstance().deleleAllFile(zipFile);
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }
    }
    
    
    private List<MatchingReportParameter> convertMatchingsToReportParameters(List<PoInvGrnDnMatchingHolder> matchingList, 
            List<BusinessRuleHolder> businessRuleList) throws Exception
    {
        if (matchingList == null || matchingList.isEmpty())
        {
            return null;
        }
        
        List<MatchingReportParameter> parameters = new ArrayList<MatchingReportParameter>();
        
        for (PoInvGrnDnMatchingHolder matching : matchingList)
        {
            PoHolder po = poService.selectPoByKey(matching.getPoOid());
            
            InvHolder inv = null;
            if (matching.getInvOid() != null)
            {
                inv = invoiceService.selectInvoiceByKey(matching.getInvOid());
            }
            
            List<GrnHolder> grnList = new ArrayList<GrnHolder>();
            
            List<PoInvGrnDnMatchingGrnHolder> matchingGrnList = poInvGrnDnMatchingGrnService.selectByMatchOid(matching.getMatchingOid());
            for (PoInvGrnDnMatchingGrnHolder grn : matchingGrnList)
            {
                grnList.add(grnService.selectByKey(grn.getGrnOid()));
            }
            
            matching.setGrnList(matchingGrnList);
            
            List<PoInvGrnDnMatchingDetailExHolder> matchingDetailList = poInvGrnDnMatchingDetailService.selectByMatchingOid(matching.getMatchingOid());
            matching.setDetailList(matchingDetailList);
            
            SupplierHolder supplier = supplierService.selectSupplierByKey(matching.getSupplierOid());
            
            parameters.add(new MatchingReportParameter(matching, po, inv, grnList, null, businessRuleList, supplier.getSupplierSource().name(), false));
            
        }
        
        return parameters;
        
    }
    
    
    private void sendEmailForUnity(BuyerHolder buyer)
    {
        log.info("start to send Email for unity");
        Date today = new Date();
        Date fromDate = DateUtil.getInstance().getFirstTimeOfDay(today);
        Date toDate = DateUtil.getInstance().getLastTimeOfDay(today);

        List<PoInvGrnDnMatchingHolder> matchingsOfToday = null;
        
        try
        {
            matchingsOfToday = poInvGrnDnMatchingService.selectMatchingRecordByMatchingDateRangeAndBuyer(fromDate, toDate, buyer.getBuyerOid());
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        if (null == matchingsOfToday || matchingsOfToday.isEmpty())
        {
            log.info("No closed records found for today.");
            log.info("Process ended.");
            
            return;
        }
        
        List<PoInvGrnDnMatchingHolder> unmatchedList = new ArrayList<PoInvGrnDnMatchingHolder>();
        
        for (PoInvGrnDnMatchingHolder matching : matchingsOfToday)
        {
            if (!PoInvGrnDnMatchingStatus.PENDING.equals(matching.getMatchingStatus())
                    && !PoInvGrnDnMatchingStatus.MATCHED.equals(matching.getMatchingStatus())
                    && !PoInvGrnDnMatchingStatus.MATCHED_BY_DN.equals(matching.getMatchingStatus()))
            {
                unmatchedList.add(matching);
            }
        }
        
        if (unmatchedList.isEmpty())
        {
            log.info("find 0 unmatched records today for buyer [" + buyer.getBuyerCode() + "]");
        }
        else
        {
            log.info("find " + unmatchedList.size() +  " unmatched records today for buyer [" + buyer.getBuyerCode() + "]");
        }
        
        
        try
        {
            this.sendEmailForUnMatchedRecords(buyer, unmatchedList);
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
        
        try
        {
            this.sendEmailForUnmatchedRecordsDuetoDnNotApproved(unmatchedList);
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }
    
    
    private void sendEmailForMatchedRecords(BuyerHolder buyer)
            throws Exception
    {

        Date today = new Date();
        Date fromDate = DateUtil.getInstance().getFirstTimeOfDay(today);
        Date toDate = DateUtil.getInstance().getLastTimeOfDay(today);

        List<PoInvGrnDnMatchingHolder> matchingsOfToday = null;
        
        List<File> files = null;
        File zipFile = null;
        try
        {
            matchingsOfToday = poInvGrnDnMatchingService.selectMatchingRecordByMatchingDateRangeAndBuyer(fromDate, toDate, buyer.getBuyerOid());
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        if (null == matchingsOfToday || matchingsOfToday.isEmpty())
        {
            log.info("No closed records found for today.");
            log.info("Process ended.");
            
            return;
        }
        
        List<PoInvGrnDnMatchingHolder> matchedList   = new ArrayList<PoInvGrnDnMatchingHolder>();
        
        for (PoInvGrnDnMatchingHolder matching : matchingsOfToday)
        {
            if (PoInvGrnDnMatchingStatus.MATCHED.equals(matching.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.MATCHED_BY_DN.equals(matching.getMatchingStatus()))
            {
                matchedList.add(matching);
            }
        }
        
        if (matchedList.isEmpty())
        {
            return;
        }
        
        String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN,  "MatchedRecipients");
        
        if (emails == null) return;
        
        Map<String, Object> fillValues = retrieveContent(matchedList);
        fillValues.put("STATUS", MATCHED);
        
        boolean autoApproveMatchedByDN = businessRuleService.isAutoApproveMatchedByDn(buyer.getBuyerOid());
        
        List<BigDecimal> matchedOids = new ArrayList<BigDecimal>();
        List<BigDecimal> matchedByDNOids = new ArrayList<BigDecimal>();
        
        List<PoInvGrnDnMatchingHolder> matchingList = matchedList;
        for (PoInvGrnDnMatchingHolder matching : matchingList)
        {
            if (autoApproveMatchedByDN || PoInvGrnDnMatchingStatus.MATCHED.equals(matching.getMatchingStatus()))
                matchedOids.add(matching.getMatchingOid());
            else
                matchedByDNOids.add(matching.getMatchingOid());
        }
        
        int zipFileSizeExceed = getLimistSize();
        try
        {
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if (autoApproveMatchedByDN || matchedByDNOids.isEmpty())
            {
                String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                String reportName = "daily_matching_report-" + buyer.getBuyerCode() + " - " + ts  + ".xls" ;
                String subject = DAILY_MATCHING_MATCHED_SUBJECT + "- " + buyer.getBuyerCode();
                String template = "mail_po_inv_grn_dn_matching.vm";
                String zipFilenname = "daily_matching_report-" + buyer.getBuyerCode()  + " - " + ts + ".zip";
                byte[] result = poInvGrnDnMatchingService.exportExcel(matchedOids, true);
                byte[][] contents = new byte[][] {result};
                
                if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                {
                    files.add(new File(path + reportName));
                    zipFile = new File(path + zipFilenname);
                    File[] file = ZipFileForAttachFile(contents, new String[]{reportName}, zipFilenname, path);
                    emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                }
                else
                {
                    emailEngine.sendEmailWithAttachedDocuments(emails,
                        subject, template, fillValues,new String[]{reportName}, contents);
                }
            }
            else if (matchedOids.isEmpty())
            {
                String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                String reportName = "daily_matching_report(matched_by_DN)-" + buyer.getBuyerCode()  + " - " + ts + ".xls" ;
                String subject = DAILY_MATCHING_MATCHED_SUBJECT + "- " + buyer.getBuyerCode();
                String template = "mail_po_inv_grn_dn_matching.vm";
                String zipFilenname ="daily_matching_report(matched_by_DN)-" + buyer.getBuyerCode()  + " - " + ts + ".zip";
                byte[] result = poInvGrnDnMatchingService.exportExcel(matchedByDNOids, true);
                byte[][] contents = new byte[][] {result};
                
                if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                {
                    files.add(new File(path + reportName));
                    zipFile = new File(path + zipFilenname);
                    File[] file = ZipFileForAttachFile(contents, new String[]{reportName}, zipFilenname, path);
                    emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                }
                else
                {
                    emailEngine.sendEmailWithAttachedDocuments(emails,
                        subject, template, fillValues, new String[]{reportName}, contents);
                }
                
            }
            else
            {
                String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                String reportName1 = "daily_matching_report(matched)-" + buyer.getBuyerCode() + " - " + ts + ".xls";
                String reportName2 = "daily_matching_report(matched_by_DN)-" + buyer.getBuyerCode()+ " - " + ts +".xls";
                String subject = DAILY_MATCHING_MATCHED_SUBJECT + "- " + buyer.getBuyerCode();
                String template = "mail_po_inv_grn_dn_matching.vm"; 
                String zipFilenname = "daily_matching_report(matched & matched_by_DN)-" + buyer.getBuyerCode() + " - " + ts + ".zip";
                byte[] result1 = poInvGrnDnMatchingService.exportExcel(matchedOids, true);
                byte[] result2 = poInvGrnDnMatchingService.exportExcel(matchedByDNOids, true);
                byte[][] contents = new byte[][] {result1, result2};
                
                if (zipFileSizeExceed != 0 && (result1.length > zipFileSizeExceed || result2.length > zipFileSizeExceed))
                {
                    files.add(new File(path + reportName1));
                    files.add(new File(path + reportName2));
                    zipFile = new File(path + zipFilenname);
                    File[] file = ZipFileForAttachFile(contents, new String[] { reportName1, reportName2 }, zipFilenname, path);
                    emailEngine.sendEmailWithAttachedDocuments(emails, DAILY_MATCHING_UNMATCHED_SUBJECT, template, new HashMap<String, Object>(), file);
                }
                else
                {
                    emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, fillValues,
                        new String[] { reportName1, reportName2 }, contents);
                }
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFile != null)
                 {
                     FileUtil.getInstance().deleleAllFile(zipFile);
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }

    }


    private void sendEmailForUnMatchedRecords(BuyerHolder buyer,
            List<PoInvGrnDnMatchingHolder> matchings)
            throws Exception
    {
        if (matchings == null || matchings.isEmpty())
        {
            return;
        }
        List<File> files = null;
        File zipFile = null;

        String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "UnmatchedRecipients");
        
        if (null == emails) return;
        
        Map<String, Object> fillValues = retrieveContent(matchings);
        fillValues.put("STATUS", UNMATCHED);
        
        List<BigDecimal> matchingOids = new ArrayList<BigDecimal>();
        List<PoInvGrnDnMatchingHolder> matchingList = matchings;
        
        boolean autoSendStockDn = businessRuleService.isAutoSendStockDn(buyer.getBuyerOid());
        
        boolean autoSendCostDn = businessRuleService.isAutoSendCostDn(buyer.getBuyerOid());
        
        List<String> dnList = new ArrayList<String>();
        for (PoInvGrnDnMatchingHolder matching : matchingList)
        {
            matchingOids.add(matching.getMatchingOid());
            
            if (null == matching.getInvOid())
                continue;
            
            if (null != matching.getDnOid())
                continue;
            
            if (!PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(matching.getMatchingStatus())
                    && !PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(matching.getMatchingStatus()))
                continue;
            
            DnHeaderHolder dnHeader = dnHeaderService
                    .selectDnHeaderByInvNo(matching.getBuyerOid(),
                            matching.getSupplierCode(), matching.getInvNo());
            
            if (null != dnHeader)
            {
                if ((DnType.STK_QOC.name().equalsIgnoreCase(dnHeader.getDnType()) && autoSendStockDn)
                        || (DnType.CST_IOC.name().equalsIgnoreCase(dnHeader.getDnType()) && autoSendCostDn))
                {
                    continue;
                }
                if (!dnHeader.getMarkSentToSupplier() && !dnHeader.getSentToSupplier())
                {
                    dnList.add("Pending DN: Dn No. [" + dnHeader.getDnNo() + "].");
                }
            }
        }
        
        fillValues.put("PENDING_DN", dnList);
        
        int zipFileSizeExceed = getLimistSize();
        String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
        String reportName = "daily_matching_report-" + buyer.getBuyerCode() + "-" + ts + ".xls";
        String template = "mail_po_inv_grn_dn_matching.vm"; 
        String zipFilenname = "daily_matching_report-" + buyer.getBuyerCode() + " - " + ts + ".zip";
        
        byte[] result = poInvGrnDnMatchingService.exportExcel(matchingOids, true);
        byte[][] contents = new byte[][] {result};
        
        try
        {
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile( contents,  new String[]{reportName }, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, DAILY_MATCHING_UNMATCHED_SUBJECT + "-" + buyer.getBuyerCode(), template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails,
                    DAILY_MATCHING_UNMATCHED_SUBJECT + "-" + buyer.getBuyerCode(), template, fillValues, new String[]{reportName }, contents);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFile != null)
                 {
                     FileUtil.getInstance().deleleAllFile(zipFile);
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }
    }
    
    
    private void sendEmailForUnMatchedRecordsToSuppliers() throws Exception
    {
        log.info("start to send unmatched Email to supplier");
        Date today = new Date();
        Date fromDate = DateUtil.getInstance().getFirstTimeOfDay(today);
        Date toDate = DateUtil.getInstance().getLastTimeOfDay(today);

        List<PoInvGrnDnMatchingHolder> matchingsOfToday = null;
        
        try
        {
            matchingsOfToday = poInvGrnDnMatchingService.selectMatchingRecordByMatchingDateRangeAndBuyer(fromDate, toDate, null);
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        if (null == matchingsOfToday || matchingsOfToday.isEmpty())
        {
            return;
        }
        
        List<PoInvGrnDnMatchingHolder> unmatchedList = new ArrayList<PoInvGrnDnMatchingHolder>();
        
        for (PoInvGrnDnMatchingHolder matching : matchingsOfToday)
        {
            if (!PoInvGrnDnMatchingStatus.PENDING.equals(matching.getMatchingStatus())
                    && !PoInvGrnDnMatchingStatus.MATCHED.equals(matching.getMatchingStatus())
                    && !PoInvGrnDnMatchingStatus.MATCHED_BY_DN.equals(matching.getMatchingStatus()))
            {
                unmatchedList.add(matching);
            }
        }
        
        if (unmatchedList.isEmpty())
        {
            return;
        }
        
        final Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> supplierGroup = groupMatchingRecordsByInvSupplier(unmatchedList);
        final int zipFileSizeExceed = getLimistSize();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                List<File> files = null;
                List<File> zipFiles = null;
                try
                {
                    files = new ArrayList<File>();
                    zipFiles = new ArrayList<File>();
                    for (Entry<BigDecimal, List<PoInvGrnDnMatchingHolder>> entry : supplierGroup.entrySet() )
                    {
                        String[] emails = retrieveSupplierMsgEmailsByMsgType(entry.getKey(), MsgType.INV.name());
                        
                        if (null == emails)
                        {
                            continue;
                        }
                        
                        Map<String, Object> fillValues = retrieveContent(entry.getValue());
                        fillValues.put("STATUS", UNMATCHED);
                        fillValues.put("NAME", entry.getValue().get(0).getSupplierName());
                        
                        List<BigDecimal> matchingOids = new ArrayList<BigDecimal>();
                        for (PoInvGrnDnMatchingHolder pigdm : entry.getValue())
                        {
                            matchingOids.add(pigdm.getMatchingOid());
                        }
                        
                        SupplierHolder supplier = supplierService.selectSupplierByKey(entry.getKey());
                        
                        String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                        String reportName = "daily_unmatched_report - " + ts + ".xls";
                        String template =  "mail_po_inv_grn_dn_matching.vm";
                        String zipFilenname = "Matching Resolution Report To Supplier - " + ts + ".zip";
                        byte[] result = poInvGrnDnMatchingService.exportExcel(matchingOids, true) ;
                        byte[][] contents = new byte[][] { result };
                        
                        String path = mboxUtil.getSupplierMboxRoot() + File.separator + supplier.getMboxId() + File.separator;
                        
                        if(zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                        {
                            files.add(new File(path + reportName));
                            zipFiles.add(new File(path + zipFilenname));
                            File[] file = ZipFileForAttachFile( new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                            emailEngine.sendEmailWithAttachedDocuments(emails, DAILY_MATCHING_UNMATCHED_SUBJECT, template, new HashMap<String, Object>(), file);
                        }
                        else
                        {
                            emailEngine.sendEmailWithAttachedDocuments(emails,
                                DAILY_MATCHING_UNMATCHED_SUBJECT, template, fillValues,
                                new String[] { reportName }, contents);
                        }
                    }
                }
                catch (Exception e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
                finally
                {
                     try
                     {
                         if (files != null)
                         {
                             for (File file : files)
                             {
                                 FileUtil.getInstance().deleleAllFile(file);
                             }
                         }
                         if (zipFiles != null)
                         {
                             for (File zipFile : zipFiles)
                             {
                                 FileUtil.getInstance().deleleAllFile(zipFile);
                             }
                         }
                     }
                     catch (Exception e)
                     {
                         String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                         standardEmailSender.sendStandardEmail(ID, tickNo, e);
                     }
                }
            }
        }).start();

    }
    

    private void sendEmailForUnmatchedRecordsDuetoDnNotApproved(
        List<PoInvGrnDnMatchingHolder> matchings) throws Exception
    {
        Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> supplierGroup = groupMatchingRecordsByInvSupplier(matchings);
        
        for (Entry<BigDecimal, List<PoInvGrnDnMatchingHolder>> entry : supplierGroup.entrySet() )
        {
            String[] emails = retrieveSupplierMsgEmailsByMsgType(entry.getKey(), MsgType.INV.name());
            
            if (null == emails)
                continue;
            
            
            List<String> mailMatchingList = new ArrayList<String>();
            for (PoInvGrnDnMatchingHolder matching : entry.getValue())
            {
                if (null == matching.getInvOid())
                    continue;
                
                if (null != matching.getDnOid())
                    continue;
                
                if (!PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(matching.getMatchingStatus())
                    && !PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(matching.getMatchingStatus()))
                    continue;
                
                DnHeaderHolder dnHeader = dnHeaderService
                    .selectDnHeaderByInvNo(matching.getBuyerOid(),
                        matching.getSupplierCode(), matching.getInvNo());
                
                if (null != dnHeader && !dnHeader.getMarkSentToSupplier() && !dnHeader.getSentToSupplier())
                {
                    mailMatchingList.add("Matching Record: "
                        + this.formatMatchingDesc(matching) + "\r\n"
                        + "Pending DN: Dn No. [" + dnHeader.getDnNo() + "].");
                }
            }
            
            if (mailMatchingList.isEmpty())
            {
                continue;
            }
            
            Map<String, Object> mailParam = new HashMap<String, Object>();
            mailParam.put("contentList", mailMatchingList);
            
            emailEngine
                .sendHtmlEmail(emails,
                    "Daily Matching Report - [ Unmatched Items ], due to pending Debit Note",
                    "mail_po_inv_grn_dn_matching_to_supplier_on_pending_dn.vm", mailParam);
        }
        
    }
    
    
    private Map<String, Object> retrieveContent(
            List<PoInvGrnDnMatchingHolder> records)
    {
        Map<String, Object> rlt = new HashMap<String, Object>();
        
        rlt.put("NAME", records.get(0).getBuyerName());
        List<Map<String, Object>> contents = new ArrayList<Map<String,Object>>();
        for (PoInvGrnDnMatchingHolder pigdm : records)
        {
            contents.add(pigdm.toMapValues());
        }
        rlt.put("CONTENTS", contents);
        return rlt;
    }
    
    
    private String[] retrieveEmailsForReport(BigDecimal buyerOid, String funcGroup, String funcId,
            String ruleId) throws Exception
    {
        BusinessRuleExHolder businessRule = businessRuleService
                .selectRulesByKey(buyerOid,
                        funcGroup, funcId, ruleId);

        if (businessRule == null)
        {
            return null;
        }

        String receipts = businessRule.getStringValue();
        
        if (receipts == null)
        {
            return null;
        }
        return receipts.split(",");
    }

    
    private String[] retrieveSupplierMsgEmailsByMsgType(BigDecimal supplierOid, String msgType) throws Exception
    {
        SupplierMsgSettingHolder supInvSetting = supplierMsgSettingService.selectByKey(supplierOid, msgType);
        
        String addrs = supInvSetting.getRcpsAddrs();
        
        if (null == addrs || addrs.trim().isEmpty())
            return null;
        
        return addrs.split(",");
    }
    

    private Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> groupMatchingRecordsByBuyer(
            List<PoInvGrnDnMatchingHolder> matchings)
    {
        Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> rlt = new HashMap<BigDecimal, List<PoInvGrnDnMatchingHolder>>();

        for (PoInvGrnDnMatchingHolder pigdm : matchings)
        {
            if (rlt.containsKey(pigdm.getBuyerOid()))
            {
                List<PoInvGrnDnMatchingHolder> pigdmList = rlt.get(pigdm.getBuyerOid());
                pigdmList.add(pigdm);
            }
            else
            {
                List<PoInvGrnDnMatchingHolder> pigdmList = new ArrayList<PoInvGrnDnMatchingHolder>();
                pigdmList.add(pigdm);
                rlt.put(pigdm.getBuyerOid(), pigdmList);
            }
        }
        
        return rlt;
    }
    
    
    private Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> groupMatchingRecordsByInvSupplier(
        List<PoInvGrnDnMatchingHolder> matchings)
    {
        Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> rlt = new HashMap<BigDecimal, List<PoInvGrnDnMatchingHolder>>();

        for(PoInvGrnDnMatchingHolder matching : matchings)
        {
            if (null == matching.getInvOid())
                continue;
            
            if(rlt.containsKey(matching.getSupplierOid()))
            {
                List<PoInvGrnDnMatchingHolder> matchingList = rlt.get(matching.getSupplierOid());
                matchingList.add(matching);
            }
            else
            {
                List<PoInvGrnDnMatchingHolder> matchingList = new ArrayList<PoInvGrnDnMatchingHolder>();
                matchingList.add(matching);
                rlt.put(matching.getSupplierOid(), matchingList);
            }
        }

        return rlt;
    }
    
    
    private String formatMatchingDesc(PoInvGrnDnMatchingHolder matching)
    {
        return "Buyer-Code [" + matching.getBuyerCode()
            + "], Buyer-Given-Supplier-Code [" + matching.getSupplierCode()
            + "], Po No. [" + matching.getPoNo() + "], Store ["
            + (matching.getPoStoreCode() == null ? "" : matching
                .getPoStoreCode()) + "], Inv No. [" + matching.getInvNo()
            + "].";
    }
    
    
    private BusinessRuleHolder retriveRule(String ruleId, List<BusinessRuleHolder> businessRuleList)
    {
        
        if (businessRuleList == null)
        {
            return null;
        }
        for (BusinessRuleHolder businessRule : businessRuleList)
        {
            if (ruleId.equalsIgnoreCase(businessRule.getRuleId()))
            {
                return businessRule;
            }
        }
        return null;
    }
    
    
    private void sendEmailForGrnDisputeReportToStoreUser(BuyerHolder buyer, UserProfileExHolder storeUser, List<GrnHeaderHolder> grnHeaders)throws Exception
    {
        log.info("start to grn dispute email to store user.");
        String[] emails = new String[1];
        emails[0] = storeUser.getEmail();
        Map<String, Object> fillValues = new HashMap<String, Object>();
        fillValues.put("STORE_USER_NAME", storeUser.getUserName());
        final int zipFileSizeExceed = getLimistSize();
        String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
        String reportName = "Goods Receipt Note Dispute Summray Reports-" +  buyer.getBuyerCode() + "-" + ts + ".xls";
        String subject = "Goods Receipt Note Dispute Reports-" + buyer.getBuyerCode();
        String template = "mail_dispute_grn_to_store_user.vm"; 
        String zipFilenname = "Goods Receipt Note Dispute Summray Reports-" + buyer.getBuyerCode() + " - " + ts + ".zip";
        List<File> files = null;
        File zipFile = null;
        
        byte[] result = grnDisputeSummaryReport.exportExcel(grnHeaders, "Goods Receipt Note Dispute Summray Reports");
     
        try
        {
            files = new ArrayList<File>();
            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
            
            if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
            {
                files.add(new File(path + reportName));
                zipFile = new File(path + zipFilenname);
                File[] file = ZipFileForAttachFile( new byte[][] {result}, new String[]{reportName}, zipFilenname, path);
                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emails, subject,
                    template, fillValues, new String[]{reportName}, new byte[][] {result});
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
             try
             {
                 if (files != null)
                 {
                     for (File file : files)
                     {
                         FileUtil.getInstance().deleleAllFile(file);
                     }
                 }
                 if (zipFile != null)
                 {
                     FileUtil.getInstance().deleleAllFile(zipFile);
                 }
             }
             catch (Exception e)
             {
                 String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                 standardEmailSender.sendStandardEmail(ID, tickNo, e);
             }
        }
    }
    
    
    private void sendEmailFroGrnDisputeReportToSuppplier(Map<BigDecimal, List<GrnHeaderHolder>> suppliers, BuyerHolder buyer)throws Exception
    {
       log.info("start to grn dispute email to supplier.");
       List<File> files = null;
       List<File> zipFiles = null;
      
       try
       {
           files = new ArrayList<File>();
           zipFiles = new ArrayList<File>();
           for (Map.Entry<BigDecimal, List<GrnHeaderHolder>> entry : suppliers.entrySet())
           {
               SupplierMsgSettingHolder msgSetting = supplierMsgSettingService.selectByKey(entry.getKey(), "GRN");
               if (msgSetting == null)
               {
                   continue;
               }
               else
               {
                   SupplierHolder supplier = supplierService.selectSupplierByKey(msgSetting.getSupplierOid());
                   Map<String, GrnHeaderHolder> map = new HashMap<String, GrnHeaderHolder>();
                   if (msgSetting.getRcpsAddrs() == null || msgSetting.getRcpsAddrs().isEmpty())
                   {
                       continue;
                   }
                   else
                   {
                       String[] emails = new String[1];
                       emails[0] = msgSetting.getRcpsAddrs();
                       Map<String, Object> fillValues = new HashMap<String, Object>();
                       
                       List<GrnHeaderHolder> grnHeaders = entry.getValue();
                       
                       for (Iterator<GrnHeaderHolder> itr = grnHeaders.iterator(); itr.hasNext();)
                       {
                           GrnHeaderHolder header = itr.next();
                           if (map.containsKey(header.getGrnNo()))
                           {
                               itr.remove();
                           }
                           else
                           {
                               map.put(header.getGrnNo(), header);
                           }
                       }
                       
                       fillValues.put("SUPPLIER_CODE", supplier.getSupplierCode());
                       fillValues.put("SUPPLIER_NAME", supplier.getSupplierName());
                       
                       final int zipFileSizeExceed = getLimistSize();
                       String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                       String reportName = "Goods Receipt Note Dispute Summray Reports - "+ts+".xls";
                       String subject = "Goods Receipt Note Dispute Reports";
                       String template = "mail_dispute_grn_to_supplier.vm"; 
                       String zipFilenname = "Goods Receipt Note Dispute Summray Reports-" + buyer.getBuyerCode() + " - " + ts + ".zip";
                       
                       byte[] result = grnDisputeSummaryReport.exportExcel(grnHeaders, "Goods Receipt Note Dispute Summray Reports");
                       
                       String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                       
                       if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                       {
                            files.add(new File(path + reportName));
                            zipFiles.add(new File(path + zipFilenname));
                            File[] file = ZipFileForAttachFile(new byte[][] {result}, new String[] {reportName}, zipFilenname, path);
                            emailEngine.sendEmailWithAttachedDocuments(emails,
                                subject, template, new HashMap<String, Object>(),file);
                       }
                       else
                       {
                            emailEngine.sendEmailWithAttachedDocuments(emails,"Goods Receipt Note Dispute Reports", template,
                                fillValues, new String[] {reportName}, new byte[][] {result});
                       }
                   }
               }
           }
       }
       catch(Exception e)
       {
            ErrorHelper.getInstance().logTicketNo(log, e);
       }
       finally
       {
            try
            {
                if (files != null)
                {
                    for (File file : files)
                    {
                        FileUtil.getInstance().deleleAllFile(file);
                    }
                }
                if (zipFiles != null)
                {
                    for (File zipFile : zipFiles)
                    {
                        FileUtil.getInstance().deleleAllFile(zipFile);
                    }
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
       }
    }
    
    
    private void processGrnDisputeEmail(BuyerHolder buyer)
    {
        try
        {   
            log.info("start to process grn dispute email .");
            boolean enableSupplierToDisputeForGrn = businessRuleService.isSupplierCanDisputeGRN(buyer.getBuyerOid());
            
            if (enableSupplierToDisputeForGrn)
            {
                List<BigDecimal> userTypes = new ArrayList<BigDecimal>();
                userTypes.add(BigDecimal.valueOf(6));
                userTypes.add(BigDecimal.valueOf(7));
                
                List<UserProfileExHolder> storeUsers = userProfileService
                    .selectUsersByBuyerAndUserTypes(buyer.getBuyerOid(),
                        userTypes);
                
                GrnHeaderHolder param = new GrnHeaderHolder();
                param.setBuyerOid(buyer.getBuyerOid());
                List<GrnHeaderHolder> grnHeaders = null;
                Map<BigDecimal, List<GrnHeaderHolder>> suppliers = new HashMap<BigDecimal, List<GrnHeaderHolder>>();
                

                if (storeUsers != null && !storeUsers.isEmpty())
                {
                    for (UserProfileExHolder storeUser : storeUsers)
                    {
                        grnHeaders = grnHeaderService.selectGrnHeaderByStoreTypeUserCurrentDay(storeUser.getUserOid(), buyer.getBuyerOid());
                        
                        if (grnHeaders != null && !grnHeaders.isEmpty())
                        {
                            
                            for (GrnHeaderHolder grnHeader : grnHeaders)
                            {
                                if (suppliers.containsKey(grnHeader.getSupplierOid()))
                                {
                                    suppliers.get(grnHeader.getSupplierOid()).add(grnHeader);
                                }
                                else
                                {
                                    List<GrnHeaderHolder> rlt = new ArrayList<GrnHeaderHolder>();
                                    rlt.add(grnHeader);
                                    suppliers.put(grnHeader.getSupplierOid(), rlt);
                                }
                            }
                            
                            sendEmailForGrnDisputeReportToStoreUser(buyer, storeUser, grnHeaders);
                        }
                    }
                }
                
                if (suppliers != null && !suppliers.isEmpty())
                {
                    sendEmailFroGrnDisputeReportToSuppplier(suppliers, buyer);
                }
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }
    
    
    private void processMissingGrnNotificationEmail(final BuyerHolder buyer)
    {
        log.info("start to process missing grn notification email .");
        Date today = new Date();
        List<File> files = null;
        List<File> zipFiles = null;
        
        try
        {   
            files = new ArrayList<File>();
            zipFiles = new ArrayList<File>();
            int missingGrnMinBufferingDays = businessRuleService.selectGlobalDailyNotificationJobMissingGrnMinBufferingDays(buyer.getBuyerOid());
            int missingGrnMaxBufferingDays = businessRuleService.selectGlobalDailyNotificationJobMissingGrnMaxBufferingDays(buyer.getBuyerOid());
            final int zipFileSizeExceed = getLimistSize();
            
            Date begin = DateUtils.addDays(today, -missingGrnMaxBufferingDays);
            Date end = DateUtils.addDays(today, -missingGrnMinBufferingDays);
            
            List<MissingGrnReportParameter> params = poLocationService.selectMissingGrnReprotRecords(buyer
                    .getBuyerOid(), null, DateUtil.getInstance()
                    .getFirstTimeOfDay(begin), DateUtil.getInstance()
                    .getLastTimeOfDay(end));
                
            if (params == null || params.isEmpty())
            {
                log.info("Find 0 record for missing grn notification report for buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            String reportName = "Missing GRN Notification Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
            String subject = "Missing GRN Notification Report - " + buyer.getBuyerCode();
            String template = "empty_email_template.vm"; 
            String zipFilenname = "Missing GRN Notification Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
            
            Map<String, Object> map = new HashMap<String, Object>();
                
            String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "MissingGrnNotificationRecipients");
            if (emails != null)
            {
                byte[] result = missingGrnReport.exportExcel(buyer, params);
                
                String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
               
                if (result != null)
                {
                    if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                    {
                        files.add(new File(path + reportName));
                        zipFiles.add(new File(path + zipFilenname));
                        File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                        emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                    }
                    else
                    {
                        emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                            map, new String[]{reportName}, new byte[][]{result});
                    }
                }
            }
            
            Map<BigDecimal, List<MissingGrnReportParameter>> userDataMap = new HashMap<BigDecimal, List<MissingGrnReportParameter>>();
            Map<BigDecimal, UserProfileExHolder> userMap = new HashMap<BigDecimal, UserProfileExHolder>();
            
            List<MissingGrnReportParameter> unknownStoreRecord = new ArrayList<MissingGrnReportParameter>();
            
            for (MissingGrnReportParameter parameter : params)
            {
                BuyerStoreHolder store = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(buyer.getBuyerCode(), parameter.getStoreCode());
                if (store == null)
                {
                    unknownStoreRecord.add(parameter);
                    continue;
                }
                
                List<UserProfileExHolder> storeUsers = null;
                
                if (store.getIsWareHouse())
                {
                    storeUsers = userProfileService.selectWarehouseUsersByBuyerOidAndStoreOid(
                            buyer.getBuyerOid(), parameter.getStoreCode());
                }
                else
                {
                    storeUsers = userProfileService.selectStoreUsersByBuyerOidAndStoreOid(
                            buyer.getBuyerOid(), parameter.getStoreCode());
                }
                
                if (storeUsers != null && !storeUsers.isEmpty())
                {
                    log.info("find " + storeUsers.size() + " users to send missing grn report to store user for store " + parameter.getStoreCode() + " and for buyer [" + buyer.getBuyerCode() + "]");
                    for (UserProfileExHolder user : storeUsers)
                    {
                        log.info("login id - " + user.getLoginId());
                        if (userDataMap.containsKey(user.getUserOid()))
                        {
                            if (!userDataMap.get(user.getUserOid()).contains(parameter))
                            {
                                userDataMap.get(user.getUserOid()).add(parameter);
                            }
                        }
                        else
                        {
                            List<MissingGrnReportParameter> list = new ArrayList<MissingGrnReportParameter>();
                            list.add(parameter);
                            userDataMap.put(user.getUserOid(), list);
                            userMap.put(user.getUserOid(), user);
                        }
                    }
                }
                else
                {
                    log.info("find 0 user to send missing grn report for store " + parameter.getStoreCode() + " and for buyer [" + buyer.getBuyerCode() + "]");
                }
            }
            
            emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "DefaultRecipients");
            if (emails != null)
            {
                byte[] result = missingGrnReport.exportExcel(buyer, unknownStoreRecord);
                if (result != null)
                {
                    subject = "Exception - Missing GRN Notification Report - " + buyer.getBuyerCode();
                    files = new ArrayList<File>();
                    String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                    if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                    {
                        files.add(new File(path + reportName));
                        zipFiles.add(new File(path + zipFilenname));
                        File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                        emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                    }
                    else
                    {
                        emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                            map, new String[]{reportName}, new byte[][]{result});
                    }
                }
            }
            
            if (userDataMap == null || userDataMap.isEmpty())
            {
                return;
            }
            
            Map<List<MissingGrnReportParameter>, List<String>> dataMap = new HashMap<List<MissingGrnReportParameter>, List<String>>();
            
            for (Entry<BigDecimal, List<MissingGrnReportParameter>> entry : userDataMap.entrySet())
            {
                String email = userMap.get(entry.getKey()).getEmail();
                if (email == null || email.trim().isEmpty())
                {
                    continue;
                }
                
                if (dataMap.containsKey(entry.getValue()))
                {
                    dataMap.get(entry.getValue()).add(email.trim());
                }
                else
                {
                    List<String> list = new ArrayList<String>();
                    list.add(email.trim());
                    dataMap.put(entry.getValue(), list);
                }
            }
            
            if (dataMap == null || dataMap.isEmpty())
            {
                return;
            }
            

            final List<Map<String, Object>> emailDataList = new ArrayList<Map<String, Object>>();
            
            for (Map.Entry<List<MissingGrnReportParameter>, List<String>> entry : dataMap.entrySet())
            {
                List<String> emailList = entry.getValue();
                
                String[] storeEmails = new String[emailList.size()];
                for (int i = 0; i < emailList.size(); i++)
                {
                    storeEmails[i] = emailList.get(i);
                }
                
                if (storeEmails.length == 0)
                {
                    continue;
                }
                
                Map<String, Object> storeMap = new HashMap<String, Object>();
                storeMap.put("emails", storeEmails);
                storeMap.put("parameters", entry.getKey());
                
                emailDataList.add(storeMap);
            }
            
            if (emailDataList.isEmpty())
            {
                return;
            }
            
            
            
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    List<File> files = null;
                    List<File> zipFiles = null;
                    try
                    {
                        files = new ArrayList<File>();
                        zipFiles = new ArrayList<File>();
                        for (Map<String, Object> emailData : emailDataList)
                        {
                            String[] emails = (String[]) emailData.get("emails");
                            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                            List<MissingGrnReportParameter> parameters = (List<MissingGrnReportParameter>) emailData.get("parameters");
                            
                            byte[] result = missingGrnReport.exportExcel(buyer, parameters);
                            
                            if (result == null)
                            {
                                return;
                            }

                            String reportName = "Missing GRN Notification Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
                            String subject = "Missing GRN Notification Report - " + buyer.getBuyerCode();
                            String template = "empty_email_template.vm";
                            String zipFilenname = "Missing GRN Notification Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
                            
                            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                            
                            if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                            {
                                files.add(new File(path + reportName));
                                zipFiles.add(new File(path + zipFilenname));
                                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                            }
                            else
                            {
                                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                                    new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                    finally
                    {
                        try
                        {
                            if (files != null)
                            {
                                for (File file : files)
                                {
                                    FileUtil.getInstance().deleleAllFile(file);
                                }
                            }
                            if (zipFiles != null)
                            {
                                for (File zipFile : zipFiles)
                                {
                                    FileUtil.getInstance().deleleAllFile(zipFile);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                            standardEmailSender.sendStandardEmail(ID, tickNo, e);
                        }
                    }
                }
            }).start();
            
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
            try
            {
                if (files != null)
                {
                    for (File file : files)
                    {
                        FileUtil.getInstance().deleleAllFile(file);
                    }
                }
                if (zipFiles != null)
                {
                    for (File zipFile : zipFiles)
                    {
                        FileUtil.getInstance().deleleAllFile(zipFile);
                    }
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }
    
    
    //*************************
    //missing gi report.
    //*************************
    private void processMissingGiNotificationEmail(final BuyerHolder buyer)
    {
        log.info("start to processing missing gi notification email  .");
        Date today = new Date();
        
        List<File> files = null;
        List<File> zipFiles = null;
        try
        {   
            files = new ArrayList<File>();
            zipFiles = new ArrayList<File>();
            int missingGiMinBufferingDays = businessRuleService.selectGlobalDailyNotificationJobMissingGiMinBufferingDays(buyer.getBuyerOid());
            int missingGiMaxBufferingDays = businessRuleService.selectGlobalDailyNotificationJobMissingGiMaxBufferingDays(buyer.getBuyerOid());
            final int zipFileSizeExceed = getLimistSize();

            Date begin = DateUtils.addDays(today, -missingGiMaxBufferingDays);
            Date end = DateUtils.addDays(today, -missingGiMinBufferingDays);
            
            List<MissingGiReportParameter> params = rtvLocationService.selectMisssingGiReportRecords(buyer
                    .getBuyerOid(), null, DateUtil.getInstance()
                    .getFirstTimeOfDay(begin), DateUtil.getInstance()
                    .getLastTimeOfDay(end));
                
            if (params == null || params.isEmpty())
            {
                log.info("Find 0 record for missing gi notification report for buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            
            
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            String reportName = "Missing GI Notification Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
            String subject = "Missing GI Notification Report - " + buyer.getBuyerCode();
            String template = "empty_email_template.vm"; 
            String zipFilenname = "Missing GI Notification Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
            
            Map<String, Object> map = new HashMap<String, Object>();
                
            String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "MissingGiNotificationRecipients");
            if (emails != null)
            {
                byte[] result = missingGiReport.exportExcel(buyer, params);
                
                String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                
                if (result != null)
                {
                    if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                    {
                        files.add(new File(path + reportName));
                        zipFiles.add(new File(path + zipFilenname));
                        File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                        emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                    }
                    else
                    {
                        emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                            map, new String[]{reportName}, new byte[][]{result});
                    }
                }
            }
            
            
            Map<BigDecimal, List<MissingGiReportParameter>> userDataMap = new HashMap<BigDecimal, List<MissingGiReportParameter>>();
            Map<BigDecimal, UserProfileExHolder> userMap = new HashMap<BigDecimal, UserProfileExHolder>();
            
            List<MissingGiReportParameter> unknownStoreRecord = new ArrayList<MissingGiReportParameter>();
            
            for (MissingGiReportParameter parameter : params)
            {
                BuyerStoreHolder store = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(buyer.getBuyerCode(), parameter.getLocationCode());
                if (store == null)
                {
                    unknownStoreRecord.add(parameter);
                    continue;
                }
                
                List<UserProfileExHolder> storeUsers = null;
                
                if (store.getIsWareHouse())
                {
                    storeUsers = userProfileService.selectWarehouseUsersByBuyerOidAndStoreOid(
                            buyer.getBuyerOid(), parameter.getLocationCode());
                }
                else
                {
                    storeUsers = userProfileService.selectStoreUsersByBuyerOidAndStoreOid(
                            buyer.getBuyerOid(), parameter.getLocationCode());
                }
                
                if (storeUsers != null && !storeUsers.isEmpty())
                {
                    log.info("find " + storeUsers.size() + " users to send missing gi report to store user for store " + parameter.getLocationCode() + " and for buyer [" + buyer.getBuyerCode() + "]");
                    for (UserProfileExHolder user : storeUsers)
                    {
                        log.info("login id - " + user.getLoginId());
                        if (userDataMap.containsKey(user.getUserOid()))
                        {
                            if (!userDataMap.get(user.getUserOid()).contains(parameter))
                            {
                                userDataMap.get(user.getUserOid()).add(parameter);
                            }
                        }
                        else
                        {
                            List<MissingGiReportParameter> list = new ArrayList<MissingGiReportParameter>();
                            list.add(parameter);
                            userDataMap.put(user.getUserOid(), list);
                            userMap.put(user.getUserOid(), user);
                        }
                    }
                }
                else
                {
                    log.info("find 0 user to send missing gi report for store " + parameter.getLocationCode() + " and for buyer [" + buyer.getBuyerCode() + "]");
                }
            }
            
            emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "DefaultRecipients");
            if (emails != null)
            {
                byte[] result = missingGiReport.exportExcel(buyer, unknownStoreRecord);
                if (result != null)
                {
                    subject = "Exception - Missing GI Notification Report - " + buyer.getBuyerCode();
                    String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                    if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                    {
                        files.add(new File(path + reportName));
                        zipFiles.add(new File(path + zipFilenname));
                        File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                        emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                    }
                    else
                    {
                        emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                            map, new String[]{reportName}, new byte[][]{result});
                    }
                }
            }
            
            if (userDataMap == null || userDataMap.isEmpty())
            {
                return;
            }
            
            Map<List<MissingGiReportParameter>, List<String>> dataMap = new HashMap<List<MissingGiReportParameter>, List<String>>();
            
            for (Entry<BigDecimal, List<MissingGiReportParameter>> entry : userDataMap.entrySet())
            {
                String email = userMap.get(entry.getKey()).getEmail();
                if (email == null || email.trim().isEmpty())
                {
                    continue;
                }
                
                if (dataMap.containsKey(entry.getValue()))
                {
                    dataMap.get(entry.getValue()).add(email.trim());
                }
                else
                {
                    List<String> list = new ArrayList<String>();
                    list.add(email.trim());
                    dataMap.put(entry.getValue(), list);
                }
            }
            
            if (dataMap == null || dataMap.isEmpty())
            {
                return;
            }

            final List<Map<String, Object>> emailDataList = new ArrayList<Map<String, Object>>();
            
            for (Map.Entry<List<MissingGiReportParameter>, List<String>> entry : dataMap.entrySet())
            {
                List<String> emailList = entry.getValue();
                
                String[] storeEmails = new String[emailList.size()];
                for (int i = 0; i < emailList.size(); i++)
                {
                    storeEmails[i] = emailList.get(i);
                }
                
                if (storeEmails.length == 0)
                {
                    continue;
                }
                
                Map<String, Object> storeMap = new HashMap<String, Object>();
                storeMap.put("emails", storeEmails);
                storeMap.put("parameters", entry.getKey());
                
                emailDataList.add(storeMap);
            }
            
            if (emailDataList.isEmpty())
            {
                return;
            }
            
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    List<File> files = null;
                    List<File> zipFiles = null;
                  
                    try
                    {
                        files = new ArrayList<File>();
                        zipFiles = new ArrayList<File>();
                        for (Map<String, Object> emailData : emailDataList)
                        {
                            String[] emails = (String[]) emailData.get("emails");
                            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                            List<MissingGiReportParameter> parameters = (List<MissingGiReportParameter>) emailData.get("parameters");
                            
                            byte[] result = missingGiReport.exportExcel(buyer, parameters);
                            
                            if (result == null)
                            {
                                return;
                            }
                            String reportName = "Missing GI Notification Report - " + buyer.getBuyerCode() + " - " + ts + ".xls";
                            String subject = "Missing GI Notification Report - " + buyer.getBuyerCode();
                            String template = "empty_email_template.vm"; 
                            String zipFilenname = "Missing GI Notification Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
                            
                            String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                            
                            if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                            {
                                files.add(new File(path + reportName));
                                zipFiles.add(new File(path + zipFilenname));
                                File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                            }
                            else
                            {
                                emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                                    new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                    finally
                    {
                        try
                        {
                            if (files != null)
                            {
                                for (File file : files)
                                {
                                    FileUtil.getInstance().deleleAllFile(file);
                                }
                            }
                            if (zipFiles != null)
                            {
                                for (File zipFile : zipFiles)
                                {
                                    FileUtil.getInstance().deleleAllFile(zipFile);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                            standardEmailSender.sendStandardEmail(ID, tickNo, e);
                        }
                    }
                }
            }).start();
            
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        finally
        {
            try
            {
                if (files != null)
                {
                    for (File file : files)
                    {
                        FileUtil.getInstance().deleleAllFile(file);
                    }
                }
                if (zipFiles != null)
                {
                    for (File zipFile : zipFiles)
                    {
                        FileUtil.getInstance().deleleAllFile(zipFile);
                    }
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }
    
    
    //*************************
    //RTV-GI-DN Exception Report
    //*************************
    private void processRtvGiDnExceptionReport(final BuyerHolder buyer)
    {
        log.info("start to processing rtv-gi-dn exception report.");
       
        try
        {
            final int zipFileSizeExceed = getLimistSize();
            boolean isRtvGiDnQtyToleranceAmount = businessRuleService.isRtvGiDnQtyToleranceTypeWithAmount(buyer.getBuyerOid());
            boolean isRtvGiDnPriceToleranceAmount = businessRuleService.isRtvGiDnPriceToleranceTypeWithAmount(buyer.getBuyerOid());
            BigDecimal rtvGiDnQtyTolerance =  businessRuleService.selectGlobalRtvGiDnQtyTolerance(buyer.getBuyerOid());
            BigDecimal rtvGiDnPriceTolerance =  businessRuleService.selectGlobalRtvGiDnPriceTolerance(buyer.getBuyerOid());
            int searchDateRange = businessRuleService.selectGlobalRtvGiDnReportGeneratingDateRange(buyer.getBuyerOid());
            Date searchDate = DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -searchDateRange));
            
            List<RtvGiDnReportParameter> params = rtvLocationService.selectRtvGiDnWarningReportData(buyer.getBuyerOid(), searchDate);
            
            
            
         
            if (params == null || params.isEmpty())
            {
                log.info("Find 0 record for RTV-GI-DN Exception Report for buyer [" + buyer.getBuyerCode() + "]");
                return;
            }
            
            final Map<String, Map<String, List<RtvGiDnReportParameter>>> groupBySupplierCodeRtvNo = new HashMap<String, Map<String, List<RtvGiDnReportParameter>>>();
            boolean generateReportFlag = false;
            Map<String, String> handlerMap = new HashMap<String, String>();
            for (RtvGiDnReportParameter param : params)
            {
                String mapKey = param.getBuyerItemCode() + "@-@" + param.getRtvNo() + "@-@" + param.getSupplierCode() + "@-@";
                if (handlerMap.containsKey(mapKey))
                {
                    continue;
                }
                else
                {
                    handlerMap.put(mapKey, mapKey);
                }
                //calculate the diff 
                BigDecimal rtvDnPriceDiff = (param.getRtvUnitPrice()==null?BigDecimal.ZERO:param.getRtvUnitPrice()).subtract(param.getDnUnitPrice()==null?BigDecimal.ZERO:param.getDnUnitPrice()).abs();
                BigDecimal rtvGiQtyDiff = (param.getRtvQty()==null?BigDecimal.ZERO : param.getRtvQty()).subtract(param.getGiQty()==null?BigDecimal.ZERO:param.getGiQty()).abs();
                BigDecimal giDnQtyDiff = (param.getGiQty()==null?BigDecimal.ZERO:param.getGiQty()).subtract(param.getDnQty()==null?BigDecimal.ZERO:param.getDnQty()).abs();
                BigDecimal storeAmt = (param.getRtvQty()==null?BigDecimal.ZERO:param.getRtvQty()).multiply(param.getRtvUnitPrice()==null?BigDecimal.ZERO:param.getRtvUnitPrice()).setScale(2);
                
                param.setStoreAmt(storeAmt);
                
                BigDecimal rtvGiQtyDiffPercent = rtvGiQtyDiff.divide((param.getRtvQty()==null || param.getRtvQty().compareTo(BigDecimal.ZERO)==0)?BigDecimal.ONE : param.getRtvQty(), 2);
                BigDecimal giDnQtyDiffPercent = giDnQtyDiff.divide((param.getGiQty()==null || param.getGiQty().compareTo(BigDecimal.ZERO)==0)?BigDecimal.ONE : param.getGiQty(), 2);
                rtvGiQtyDiffPercent =(rtvGiQtyDiffPercent.compareTo(BigDecimal.ONE) < 0)?rtvGiQtyDiffPercent.multiply(PECENT):PECENT;
                giDnQtyDiffPercent = (giDnQtyDiffPercent.compareTo(BigDecimal.ONE) < 0)? giDnQtyDiffPercent.multiply(PECENT):PECENT;
                
                if (isRtvGiDnQtyToleranceAmount)
                {
                    if (rtvGiDnQtyTolerance != null && giDnQtyDiff.compareTo(rtvGiDnQtyTolerance) > 0)
                    {
                        param.setGiDnQtyDiff(giDnQtyDiff);
                        param.setGiDnQtyDiffPercent(giDnQtyDiffPercent);
                        generateReportFlag = true;
                    }
                    
                    if (rtvGiDnQtyTolerance != null && rtvGiQtyDiff.compareTo(rtvGiDnQtyTolerance) > 0)
                    {
                        param.setRtvGiQtyDiff(rtvGiQtyDiff);
                        param.setRtvGiQtyDiffPercent(rtvGiQtyDiffPercent);
                        generateReportFlag = true;
                    }
                }
                else
                {
                    if (rtvGiDnQtyTolerance != null && rtvGiQtyDiffPercent.compareTo(rtvGiDnQtyTolerance) > 0)
                    {
                        param.setRtvGiQtyDiff(rtvGiQtyDiff);
                        param.setRtvGiQtyDiffPercent(rtvGiQtyDiffPercent);
                        generateReportFlag = true;
                    }
                    
                    if (rtvGiDnQtyTolerance != null && giDnQtyDiffPercent.compareTo(rtvGiDnQtyTolerance) > 0)
                    {
                        param.setGiDnQtyDiff(giDnQtyDiff);
                        param.setGiDnQtyDiffPercent(giDnQtyDiffPercent);
                        generateReportFlag = true;
                    }
                }
                
                
                BigDecimal rtvDnPriceDiffPercent = rtvDnPriceDiff.divide((param.getRtvUnitPrice()==null || param.getRtvUnitPrice().compareTo(BigDecimal.ZERO)==0)?BigDecimal.ONE : param.getRtvUnitPrice(), 2);
                rtvDnPriceDiffPercent = rtvDnPriceDiffPercent.compareTo(BigDecimal.ONE)<0?rtvDnPriceDiffPercent.multiply(PECENT):PECENT;
                if (isRtvGiDnPriceToleranceAmount)
                {
                    if (rtvGiDnPriceTolerance != null && rtvDnPriceDiff.compareTo(rtvGiDnPriceTolerance) > 0)
                    {
                        param.setRtvDnPriceDiff(rtvDnPriceDiff);
                        param.setRtvDnPriceDiffPercent(rtvDnPriceDiffPercent);
                        generateReportFlag = true;
                    }
                }
                else
                {
                    if (rtvGiDnPriceTolerance != null && rtvDnPriceDiffPercent.compareTo(rtvGiDnPriceTolerance) > 0)
                    {
                        param.setRtvDnPriceDiff(rtvDnPriceDiff);
                        param.setRtvDnPriceDiffPercent(rtvDnPriceDiffPercent);
                        generateReportFlag = true;
                    }
                }
               
               String key = param.getSupplierName() + "@-@" +param.getSupplierCode();
                
                if (groupBySupplierCodeRtvNo.containsKey(key))
                {
                    
                    if (groupBySupplierCodeRtvNo.get(key).containsKey(param.getStoreCode()+ "@=@" + param.getRtvNo()))
                    {
                        groupBySupplierCodeRtvNo.get(key).get(param.getStoreCode()+ "@=@" + param.getRtvNo()).add(param);
                    }
                    else
                    {
                        List<RtvGiDnReportParameter> lst = new ArrayList<RtvGiDnReportParameter>();
                        lst.add(param);
                        groupBySupplierCodeRtvNo.get(key).put(param.getStoreCode()+ "@=@" + param.getRtvNo(), lst);
                    }
                }
                else
                {
                    Map<String, List<RtvGiDnReportParameter>> map = new HashMap<String, List<RtvGiDnReportParameter>>();
                    List<RtvGiDnReportParameter> lst = new ArrayList<RtvGiDnReportParameter>();
                    lst.add(param);
                    map.put(param.getStoreCode() + "@=@" + param.getRtvNo() , lst);
                    groupBySupplierCodeRtvNo.put(key, map);
                }
            }
            
            
            if (!generateReportFlag)
            {
                log.info("There is no records exceed qty tolerance or price tolerance.");
                return ;
            }
            
            
            for (Map.Entry<String, Map<String, List<RtvGiDnReportParameter>>> entry : groupBySupplierCodeRtvNo.entrySet())
            {
                Map<String, List<RtvGiDnReportParameter>> map = entry.getValue();
                
                Iterator<Map.Entry<String, List<RtvGiDnReportParameter>>>  itr = map.entrySet().iterator();
                while (itr.hasNext())
                {
                    int giDnQtyUnmatchedCount = 0;
                    int rtvGiQtyUnmatchedCount = 0;
                    int rtvDnPriceUnmatchedCount = 0;
                    BigDecimal storeAmt = BigDecimal.ZERO;
                    
                    for (RtvGiDnReportParameter lst : itr.next().getValue())
                    {
                        storeAmt = storeAmt.add(lst.getStoreAmt());
                        
                        if (lst.getGiDnQtyDiff() != null)
                        {
                            giDnQtyUnmatchedCount++;
                        }
                        
                        if (lst.getRtvGiQtyDiff() != null)
                        {
                            rtvGiQtyUnmatchedCount++;
                        }
                        
                        if (lst.getRtvDnPriceDiff() != null)
                        {
                            rtvDnPriceUnmatchedCount++;
                        }
                    }
                    
                    if (giDnQtyUnmatchedCount==0 && rtvGiQtyUnmatchedCount==0 && rtvDnPriceUnmatchedCount==0)
                    {
                        itr.remove();
                    }
                }
            }
            
            final String[] emails = retrieveEmailsForReport(buyer.getBuyerOid(), MATCHING, PO_INV_GRN_DN, "RtvGiDnExceptionReportRecipients");
            
            if (emails == null || emails.length == 0)
            {
                return;
            }
            
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    List<File> files = null;
                    File zipFile = null;
                    try
                    {
                        byte[] result = rtvGiDnReport.exportExcel(buyer, groupBySupplierCodeRtvNo);
                        
                        if (result == null)
                        {
                            return;
                        }
                        
                        String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
                        String reportName = "RTV-GI-DN Exception Report - " + buyer.getBuyerCode() + ts +".xls";
                        String subject = "RTV-GI-DN Exception Report - " + buyer.getBuyerCode();
                        String template = "empty_email_template.vm"; 
                        String zipFilenname = "RTV-GI-DN Exception Report - " + buyer.getBuyerCode() + " - " + ts + ".zip";
                        
                        files = new ArrayList<File>();
                        String path = mboxUtil.getBuyerMboxRoot() + File.separator + buyer.getMboxId() + File.separator;
                      
                        if (zipFileSizeExceed != 0 && result.length >= zipFileSizeExceed)
                        {
                            files.add(new File(path + reportName));
                            zipFile = new File(path + zipFilenname);
                            File[] file = ZipFileForAttachFile(new byte[][]{result}, new String[]{reportName}, zipFilenname, path);
                            emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, new HashMap<String, Object>(), file);
                        }
                        else
                        {
                            emailEngine.sendEmailWithAttachedDocuments(emails, subject, template, 
                                new HashMap<String, Object>(), new String[]{reportName}, new byte[][]{result});
                        }
                      
                    }
                    catch(Exception e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                    finally
                    {
                        try
                        {
                            if (files != null)
                            {
                                for (File file : files)
                                {
                                    FileUtil.getInstance().deleleAllFile(file);
                                }
                            }
                            if (zipFile != null)
                            {
                                FileUtil.getInstance().deleleAllFile(zipFile);
                            }
                        }
                        catch (Exception e)
                        {
                            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                            standardEmailSender.sendStandardEmail(ID, tickNo, e);
                        }
                    }
                }
            }).start();
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    private ReportEngineParameter<List<MatchingReportParameter>> retrieveParameter(
        List<MatchingReportParameter> reports, BuyerHolder buyer) throws Exception
    {
        ReportEngineParameter<List<MatchingReportParameter>> reportEngineParameter = new ReportEngineParameter<List<MatchingReportParameter>>();

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(reports);

        return reportEngineParameter;
    }
    
    
    final private File[] ZipFileForAttachFile(byte[][] result, String[] reportName, String zipFilename, String path) throws Exception
    {
        List<File> files = new ArrayList<File>();
        
        for (int i = 0 ; i < reportName.length; i++)
        {
            FileUtil.getInstance().writeByteToDisk(result[i], path + reportName[i]);
            files.add(new File(path +reportName[i]));
        }
        
        GZIPHelper.getInstance().doZip(files, path, zipFilename);
        File zipFile = new File(path + zipFilename);
        
        return new File[]{zipFile};
    }
    
    
    final private Integer getLimistSize() throws Exception
    {
        //measure unit  (M)
        ControlParameterHolder param = controlParameterService.selectControlParameterByParametersBySectIdAndParamId("CTRL", "ZIP_FILE_SIZE_LIMIT");
        Integer fileSizeLimit = 0;
        if (param != null && param.getNumValue() != null)
        {
            fileSizeLimit = param.getNumValue();
        }
        
        return  fileSizeLimit * 1024 * 1024;
    }
   
}

