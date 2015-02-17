package com.pracbiz.b2bportal.core.eai.backend;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierAdminRolloutHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.RunningNumberService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public class SupplierAdminImportJob extends BaseJob implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(SupplierAdminImportJob.class);
    private static final String ID = "[SupplierAdminImportJob]";
    private static final String LINE_NO = "lineNo : ";
    private static final String LEFT_SEPERATE = " [ ";
    private static final String RIGHT_SEPERATE = " ] ";
    
    private BuyerService buyerService;
    private MailBoxUtil mboxUtil;
    private BuyerMsgSettingService buyerMsgSettingService;
    private OidService oidService;
    private StandardEmailSender standardEmailSender;
    private SupplierService supplierService;
    private UserProfileService userProfileService;
    private CustomAppConfigHelper appConfig;
    private EmailEngine emailEngine;
    private ControlParameterService controlParameterService;
    private TradingPartnerService tradingPartnerService;
    private RunningNumberService runningNumberService;
    private BuyerStoreService buyerStoreService;
    
    @SuppressWarnings("rawtypes")
    private DocFileHandler poDocFileHandler;
    
    
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        supplierService = this.getBean("supplierService", SupplierService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        oidService = this.getBean("oidService", OidService.class);
        userProfileService = this.getBean("userProfileService", UserProfileService.class);
        appConfig = this.getBean("appConfig", CustomAppConfigHelper.class);
        emailEngine = this.getBean("emailEngine", EmailEngine.class);
        controlParameterService = this.getBean("controlParameterService", ControlParameterService.class);
        tradingPartnerService = this.getBean("tradingPartnerService", TradingPartnerService.class);
        runningNumberService = this.getBean("runningNumberService", RunningNumberService.class);
        buyerStoreService = this.getBean("buyerStoreService", BuyerStoreService.class);
        poDocFileHandler = this.getBean("canonicalPoDocFileHandler", DocFileHandler.class);
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
            try
            {
                this.processBuyer(buyer);
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
        }
        
        log.info("Process ended.");
    
    }
    
    
    private void processBuyer(final BuyerHolder buyer) throws IOException
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

        File[] stBatchFiles = outPath.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file)
            {
                return file.getName().toUpperCase()
                    .matches("SA_" + buyer.getBuyerCode().toUpperCase() + "_[0-9]+.CSV");
            }

        });
        
        if (stBatchFiles != null && stBatchFiles.length > 0)
        {
        	stBatchFiles = FileUtil.getInstance().getStableFiles(stBatchFiles);
        }
        
        if (stBatchFiles == null || stBatchFiles.length < 1)
        {
            log.info("No Supplier Admin Master files found for buyer [" + buyer.getBuyerName() + "].");
            
            return;
        }
        

        List<File> stableFiles = Arrays.asList(stBatchFiles);
        
        BuyerMsgSettingHolder setting = null;
        try
        {
            setting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), MsgType.SA.name());
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
        
        for (File csvFile : stableFiles)
        {
            File workDir = new File(mboxUtil.getBuyerWorkingOutPath(buyer.getMboxId()) + PS
                    + csvFile.getName().substring(0, csvFile.getName().lastIndexOf(".")));
            
            try
            {
            	try
                {
                	FileUtil.getInstance().createDir(workDir);
                	FileUtil.getInstance().moveFile(csvFile, workDir.getPath());
                }
                catch (IOException e)
                {
                	File invalidPath = new File(mboxUtil.getFolderInBuyerInvalidPath(buyer.getMboxId(),
                			DateUtil.getInstance().getYearAndMonth(new Date())));
                    
                    if (csvFile.exists())
                    {
                        FileUtil.getInstance().moveFile(csvFile, invalidPath.getPath());
                    }
                    
                    String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                    standardEmailSender.sendStandardEmail(ID, tickNo, e);
                    
                    continue;
                }
                
                File resultCsvFile = new File(workDir, csvFile.getName());
                
                try
                {
                	processCsvFile(buyer, resultCsvFile, setting, workDir);
                }
                catch (Exception e)
                {
                	File invalidPath = new File(mboxUtil.getFolderInBuyerInvalidPath(buyer.getMboxId(),
                			DateUtil.getInstance().getYearAndMonth(new Date())));
                    
                    if (resultCsvFile.exists())
                    {
                        FileUtil.getInstance().moveFile(resultCsvFile, invalidPath.getPath());
                    }

                    String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                    standardEmailSender.sendStandardEmail(ID, tickNo, e);
                    
                    continue;
                }
                
                File archPath = new File(mboxUtil.getFolderInBuyerArchOutPath(
                        buyer.getMboxId(), DateUtil.getInstance().getYearAndMonth(new Date())));
                    
    			if (!archPath.isDirectory())
    			{
    				FileUtil.getInstance().createDir(archPath);
    			}
                    
                FileUtil.getInstance().copyFile(resultCsvFile,
                    new File(archPath, resultCsvFile.getName()), true);
            }
            finally
            {
            	if (workDir.isDirectory())
            	{
            		FileUtil.getInstance().deleleAllFile(workDir);
            	}
            }
        }
    }
    
    
    private void processCsvFile(BuyerHolder buyer, File resultCsvFile, BuyerMsgSettingHolder setting, File workDir) throws Exception
    {
        List<String> successList = new ArrayList<String>();
        List<String> lengthErrorList = new ArrayList<String>();
        List<String> supplierCodeEmptyList = new ArrayList<String>();
        List<String> supplierCodeNotExistList = new ArrayList<String>();
        List<String> adminNotExistList = new ArrayList<String>();
        List<String> emailEmptyList = new ArrayList<String>();
        List<String> emailInvalidList = new ArrayList<String>();
        List<String> msgEmailErrorList = new ArrayList<String>();
        List<String> dateEmptyList = new ArrayList<String>();
        List<String> dateInvalidList = new ArrayList<String>();
        List<String> batchNoInvalidList = new ArrayList<String>();
        List<String> fileEmptyList = new ArrayList<String>();
        List<UserProfileHolder> supplierAdmins = new ArrayList<UserProfileHolder>();
        List<SupplierAdminRolloutHolder> supplierAdminRolloutList = new ArrayList<SupplierAdminRolloutHolder>();
        Map<UserProfileHolder, String> emailMap = new HashMap<UserProfileHolder, String>();
        Map<UserProfileHolder, String> supplierNameMap = new HashMap<UserProfileHolder, String>();
        Map<UserProfileHolder, String> informationMap = new HashMap<UserProfileHolder, String>();
        Map<UserProfileHolder, String> liveDateMap = new HashMap<UserProfileHolder, String>();
        Map<BigDecimal, String> userMsgEmailMap = new HashMap<BigDecimal, String>();
        
        List<String> contents = FileUtils.readLines(resultCsvFile);
        if (contents == null || contents.isEmpty() || contents.size() == 1)
        {
            fileEmptyList.add(resultCsvFile.getName());
            
            File invalid = new File(mboxUtil.getBuyerInvalidPath(buyer.getMboxId()) + PS + DateUtil.getInstance().getYearAndMonth(new Date()));
            FileUtil.getInstance().copyFile(resultCsvFile,
                new File(invalid, resultCsvFile.getName()), true);
            
            String emailAddress = setting.getRcpsAddrs();
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("BUYER_NAME", buyer.getBuyerName());
            map.put("BUYER_CODE", buyer.getBuyerCode());
            map.put("SA_FILE_NAME", resultCsvFile.getName());
            map.put("FILE_EMPTY_LIST", fileEmptyList);
            emailEngine.sendHtmlEmail(this.parseEmailAddrs(emailAddress), "Supplier Admin Import Status", "ALERT_BUYER_SA.vm", map);
            
            return;
        }
        TransactionBatchHolder batch = this.initTransBatch(
                FileUtil.getInstance().trimAllExtension(resultCsvFile.getName()), resultCsvFile.getName());
        String msgRefNo = FileUtil.getInstance().trimAllExtension(resultCsvFile.getName()).split("_")[2];
        MsgTransactionsHolder msg = this.initMsgTransactions(buyer, batch, msgRefNo, resultCsvFile.getName());
        ControlParameterHolder helpNo = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL,
                        PARAM_ID_HELPDESK_NO);
        ControlParameterHolder helpEmail = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL,
                        PARAM_ID_HELPDESK_EMAIL);
        for (int i = 1; i < contents.size(); i ++)
        {
            String obj = contents.get(i);
            int index = i;
            CSVReader reader = new CSVReader(new StringReader(contents.get(i)), ',');
            String[] content = null;
            try
            {
                content = reader.readNext();
            }
            finally
            {
                reader.close();
                reader = null;
            }
            
            /** length **/
            if (content == null || content.length != 5)
            {
                log.info("length of [" + obj + "] is not 5, skip");
                lengthErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                continue;
            }
            
            /** supplier code empty **/
            if (content[0].trim().isEmpty())
            {
                log.info("supplier code of [" + obj + "] is empty, skip");
                supplierCodeEmptyList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                continue;
            }
            
            /** supplier code does not exist in database**/
            SupplierHolder supplier = supplierService.selectSupplierByCode(content[0].trim());
            if (supplier == null)
            {
                log.info("supplier of [" + obj + "] does not exist in database, skip");
                supplierCodeNotExistList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                continue;
            }
            
            /** supplier admin does not exist in database **/
            UserProfileHolder user = new UserProfileHolder();
            user.setSupplierOid(supplier.getSupplierOid());
            user.setCreateBy("System");
            List<UserProfileHolder> userList  = userProfileService.select(user);
            if (userList == null || userList.isEmpty())
            {
                log.info("supplier admin created by supplier master job of [" + obj + "] does not exist in database, skip");
                adminNotExistList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                continue;
            }
            user = userList.get(0);
            
            /** email address **/
            if (content[1].trim().isEmpty())
            {
                log.info("email of [" + obj + "] is empty, skip");
                emailEmptyList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                continue;
            }
            if (!content[1].trim().matches(appConfig.getEmailPattern()))
            {
                log.info("email of [" + obj + "] is invalid, skip");
                emailInvalidList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                continue;
            }
            
            /** reset email for supplier user **/
            user.setEmail(content[1].trim());
            
            /** msg email valid **/
            if (!content[2].trim().isEmpty())
            {
                StringBuffer buf = new StringBuffer();

                String[] msgEmails = content[2].trim().split("\\|");
                boolean flag = true;
                for (String msgEmail : msgEmails)
                {
                    if (!msgEmail.trim().matches(appConfig.getEmailPattern()))
                    {
                        flag = false;
                        break;
                    }
                    buf.append(',').append(msgEmail);
                }
                if (flag)
                {
                    userMsgEmailMap.put(user.getUserOid(), buf.substring(1));
                }
                else
                {
                    log.info("msg email of [" + obj + "] is invalid, will not update supplier msg setting");
                    msgEmailErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                    continue;
                }
            }
            
            /** live date valid **/
            if (content[3].trim().isEmpty())
            {
                log.info("live date of [" + obj + "] is empty, skip");
                dateEmptyList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                continue;
            }
            if (!content[3].trim().isEmpty())
            {
                try
                {
                    SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_DDMMYYYY, Locale.US);
                    df.parse(content[3].trim());
                    String[] date = content[3].trim().split("/");
                    if ((date[0].length() != 1 && date[0].length() != 2) || (date[1].length() != 1 && date[1].length() != 2) 
                        || date[2].length() != 4)
                    {
                        log.info("live date of [" + obj + "] is invalid, skip");
                        dateInvalidList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                        continue;
                    }
                }
                catch (Exception e)
                {
                    log.info("live date of [" + obj + "] is invalid, skip");
                    dateInvalidList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                    continue;
                }
            }
            
            
            /** batch no **/
            if (!content[4].trim().isEmpty() && content[4].trim().length() > 50)
            {
                log.info("batch no of [" + obj + "] is invalid, max length is 50, skip");
                batchNoInvalidList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                continue;
            }
            
            /** reset password for supplier user **/
            user.setLoginPwd(radmonPassword(6));
            user.setLastResetPwdDate(new Date());
            supplierAdmins.add(user);
            
            SupplierAdminRolloutHolder sar = new SupplierAdminRolloutHolder();
            sar.setSupplierOid(supplier.getSupplierOid());
            sar.setPasswdSendDate(new Date());
            sar.setLiveDate(DateUtil.getInstance().convertStringToDate(content[3].trim(), DateUtil.DATE_FORMAT_DDMMYYYY));
            sar.setBatchNo(content[4].trim());
            supplierAdminRolloutList.add(sar);
            
            emailMap.put(user, content[1].trim());
            supplierNameMap.put(user, supplier.getSupplierName());
            informationMap.put(user, LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
            liveDateMap.put(user, content[3].trim());
        }
        
        if (supplierAdmins.isEmpty())
        {
            File invalid = new File(mboxUtil.getBuyerInvalidPath(buyer.getMboxId()) + PS + DateUtil.getInstance().getYearAndMonth(new Date()));
            FileUtil.getInstance().moveFile(resultCsvFile, invalid.getPath());
        }
        else
        {
            userProfileService.resetSupplierAdminPwd(supplierAdmins, supplierAdminRolloutList, batch, msg, userMsgEmailMap);
            generateTestPoFile(supplierAdmins, workDir.getPath() + PS);
        }
        
        
        /** send email to supplier **/
        List<String> attachments = appConfig.getAttachmentsForSAJob();
        List<File> documents = new ArrayList<File>();
        for (int i = 0; i < attachments.size(); i++)
        {
            File file = new File(attachments.get(i));
            if (file.isFile())
            {
                documents.add(file);
            }
        }
        for (UserProfileHolder user : supplierAdmins)
        {
            TradingPartnerExHolder tp = new TradingPartnerExHolder();
            tp.setSupplierOid(user.getSupplierOid());
            tp.setBuyerOid(buyer.getBuyerOid());
            List<TradingPartnerHolder> tps = tradingPartnerService.select(tp);
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("SUPPLIER_NAME", supplierNameMap.get(user));
            map.put("LOGIN_ID", user.getLoginId());
            map.put("PASSWORD", user.getLoginPwd());
            map.put("TELPHONE", helpNo.getStringValue());
            map.put("HELP_EMAIL", helpEmail.getStringValue());
            map.put("LIVE_DATE", liveDateMap.get(user));
            map.put("COUNT_PO", tps.size());
            emailEngine.sendEmailWithAttachedDocuments(new String[]{emailMap.get(user)}, 
                    "Supplier Admin Account", "ALERT_SUPPLIER_SUPPLIERADMIN.vm", map, documents.toArray(new File[documents.size()]));
                    successList.add(informationMap.get(user));
        }
        
        /** send email to buyer **/
        if (null == setting)
        {
            log.info(" :::: No message setting found for Bizunit ["
                    + buyer.getBuyerCode() + "], msg-type ["
                    + MsgType.SA.name()
                    + "], email will not be sent out to buyer.");
            return;
        }

        if (null == setting.getRcpsAddrs()
                || "".equals(setting.getRcpsAddrs().trim()))
        {
            log.info(" :::: email address is empty for Buyer ["
                    + buyer.getBuyerCode() + "], msg-type ["
                    + MsgType.SA.name()
                    + "], email will not be sent out to buyer.");

            return;
        }
        if (setting.getExcludeSucc() && lengthErrorList.isEmpty() && supplierCodeEmptyList.isEmpty() 
                && supplierCodeNotExistList.isEmpty() && adminNotExistList.isEmpty() && emailEmptyList.isEmpty())
        {
            log.info(" :::: message setting for Buyer ["
                    + buyer.getBuyerCode() + "], msg-type ["
                    + MsgType.SA.name()
                    + "], is set to exclude success, email will not be sent out to buyer.");
            return;
        }
        String emailAddress = setting.getRcpsAddrs();
        int errorCount = lengthErrorList.size() + supplierCodeEmptyList.size() 
            + supplierCodeNotExistList.size() + adminNotExistList.size() 
            + emailEmptyList.size() + emailInvalidList.size() + msgEmailErrorList.size() + dateEmptyList.size() 
            + dateInvalidList.size() + batchNoInvalidList.size();
        
        if (errorCount != 0 || (errorCount == 0 && !setting.getExcludeSucc()))
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("BUYER_NAME", buyer.getBuyerName());
            map.put("BUYER_CODE", buyer.getBuyerCode());
            map.put("SA_FILE_NAME", resultCsvFile.getName());
            map.put("SUCCESS_LIST", successList);
            map.put("LENGTH_ERROR_LIST", lengthErrorList);
            map.put("SUPPLIER_CODE_EMPTY_LIST", supplierCodeEmptyList);
            map.put("SUPPLIER_CODE_NOT_EXIST_LIST", supplierCodeNotExistList);
            map.put("ADMIN_NOT_EXIST_LIST", adminNotExistList);
            map.put("EMAIL_EMPTY_LIST", emailEmptyList);
            map.put("EMAIL_INVALID_LIST", emailInvalidList);
            map.put("MSG_EMAIL_ERROR_LIST", msgEmailErrorList);
            map.put("DATE_EMPTY_LIST", dateEmptyList);
            map.put("DATE_INVALID_LIST", dateInvalidList);
            map.put("BATCH_NO_INVALID_LIST", batchNoInvalidList);
            map.put("SUCCESS_COUNT", successList.size());
            map.put("UNPROCESS_COUNT", errorCount);
            emailEngine.sendHtmlEmail(this.parseEmailAddrs(emailAddress), "Supplier Admin Import Status", "ALERT_BUYER_SA.vm", map);
        }
        
    }
    
    
    @SuppressWarnings("unchecked")
    private void generateTestPoFile(List<UserProfileHolder> supplierAdmins, String workDir) 
    {
        
        try
        {
            //get all tradingpartners by supplier
            List<TradingPartnerHolder> allTps = new ArrayList<TradingPartnerHolder>();
            for (UserProfileHolder supplierUser : supplierAdmins)
            {
                if(null == supplierUser.getSupplierOid())
                {
                    continue;
                }
                allTps.addAll(tradingPartnerService.selectBySupplierOid(supplierUser.getSupplierOid()));
            }
            
            //group all tradingpartners by buyer
            List<BigDecimal> buyerOids = new ArrayList<BigDecimal>();
            Map<BigDecimal,List<TradingPartnerHolder>> groupByBuyer = new HashMap<BigDecimal,List<TradingPartnerHolder>>();
            for (TradingPartnerHolder tp : allTps)
            {
                if(!buyerOids.contains(tp.getBuyerOid()))
                {
                    buyerOids.add(tp.getBuyerOid());
                }
                
                if(groupByBuyer.containsKey(tp.getBuyerOid()))
                {
                    groupByBuyer.get(tp.getBuyerOid()).add(tp);
                }
                else
                {
                    List<TradingPartnerHolder> tradingPartners = new ArrayList<TradingPartnerHolder>();
                    tradingPartners.add(tp);
                    groupByBuyer.put(tp.getBuyerOid(), tradingPartners);
                }
            }
            
            //generate test po file for every trading partner
            for (BigDecimal buyerOid : buyerOids)
            {
                List<File> files = new ArrayList<File>();
                //get buyer out path
                BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
                String out = mboxUtil.getBuyerOutPath(buyer.getMboxId()) + PS;

                //get the list of TradingPartners which has been grouped
                List<TradingPartnerHolder> groups = groupByBuyer.get(buyerOid);
                String format = buyerMsgSettingService.selectByKey(buyerOid, MsgType.PO.name()).getFileFormat();

                //get location code
                String locCode = null;
                List<BuyerStoreHolder> list = buyerStoreService.selectBuyerStoresByBuyer(buyer.getBuyerCode());
                if (list == null || list.isEmpty())
                {
                    locCode = "A";
                }
                else
                {
                    locCode = list.get(0).getStoreCode();
                }

                for(TradingPartnerHolder group : groups)
                {
                    PoHolder poHolder = this.initTestPoData(buyer, group, locCode);
                    File targetFile = new File(workDir + poDocFileHandler.getTargetFilename(poHolder, format));
                    poDocFileHandler.createFile(poHolder, targetFile, format);
                    files.add(targetFile);
                }
                
                String[] parts = FileUtil.getInstance().trimExtension(files.get(0).getName()).split("_");
                String outputName = parts[0] + "_" + parts[1] + "_" + DateUtil.getInstance().getCurrentLogicTimeStamp() + ".zip";

                //generate zip file for all files
                GZIPHelper.getInstance().doZip(files, out, outputName);
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
    }
    
    
    private PoHolder initTestPoData(BuyerHolder buyer, TradingPartnerHolder tp, String LocCode)
        throws Exception
    {
        String poNo = "test" + runningNumberService.generateNumber(buyer.getBuyerOid(), "P", 4);
        SupplierHolder supplier = supplierService.selectSupplierByKey(tp.getSupplierOid());
        
        //init po header data
        PoHeaderHolder header = new PoHeaderHolder();
        header.setPoOid(oidService.getOid());
        header.setPoNo(poNo);
        header.setDocAction("A");
        header.setActionDate(new Date());
        header.setPoType(PoType.SOR);
        header.setPoDate(new Date());
        header.setPoSubType("1");
        header.setDeliveryDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
        header.setDeliveryDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        header.setExpiryDate(new Date());
        header.setBuyerCode(buyer.getBuyerCode());
        header.setBuyerCtryCode(buyer.getCtryCode());
        header.setBuyerName(buyer.getBuyerName());
        header.setSupplierCode(tp.getBuyerSupplierCode());
        header.setSupplierName(supplier.getSupplierName());
        header.setSupplierCtryCode(supplier.getCtryCode());
        header.setTotalCost(new BigDecimal(100));
        header.setAdditionalDiscountAmount(BigDecimal.ZERO);
        header.setNetCost(new BigDecimal(100));
        
        //init po detail data
        PoDetailHolder detail = new PoDetailHolder();
        List<PoDetailHolder> details = new ArrayList<PoDetailHolder>();
        detail.setLineSeqNo(1);
        detail.setBuyerItemCode("123456");
        detail.setItemDesc("ITEM DESC1");
        detail.setPackingFactor(BigDecimal.ONE);
        detail.setOrderBaseUnit("P");
        detail.setOrderUom("Uom");
        detail.setOrderQty(new BigDecimal(100));
        detail.setUnitCost(BigDecimal.ONE);
        detail.setPackCost(BigDecimal.ONE);
        detail.setCostDiscountAmount(BigDecimal.ZERO);
        detail.setNetUnitCost(BigDecimal.ONE);
        detail.setNetPackCost(BigDecimal.ONE);
        detail.setItemCost(new BigDecimal(100));
        
        details.add(detail);
        
        //init po location data
        PoLocationHolder location = new PoLocationHolder();
        PoLocationDetailHolder locDetail = new PoLocationDetailHolder();
        List<PoLocationHolder> locations = new ArrayList<PoLocationHolder>();
        List<PoLocationDetailHolder> locDetails = new ArrayList<PoLocationDetailHolder>();
        location.setLineSeqNo(1);
        location.setLocationCode(LocCode);
        locDetail.setDetailLineSeqNo(1);
        locDetail.setLocationLineSeqNo(1);
        locDetail.setLocationShipQty(new BigDecimal(100));
        locDetail.setLocationFocQty(BigDecimal.ZERO);
        
        locations.add(location);
        locDetails.add(locDetail);
        
        PoHolder poHolder = new PoHolder();
        poHolder.setPoHeader(header);
        poHolder.setDetails(details);
        poHolder.setLocations(locations);
        poHolder.setLocationDetails(locDetails);
        
        return poHolder;
    }
    

    private TransactionBatchHolder initTransBatch(String batchNo,
            String filename) throws Exception
    {
        TransactionBatchHolder transBatch = new TransactionBatchHolder();

        transBatch.setBatchOid(oidService.getOid());
        transBatch.setBatchNo(batchNo);
        transBatch.setBatchFilename(filename);
        transBatch.setCreateDate(new Date());
        transBatch.setAlertSenderDate(null);

        return transBatch;
    }
    
    private MsgTransactionsHolder initMsgTransactions(BuyerHolder biz,
            TransactionBatchHolder transBatch, String msgRefNo, String originalFilename)
            throws Exception
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();

        msg.setDocOid(oidService.getOid());
        msg.setBatchOid(transBatch.getBatchOid());
        msg.setMsgType(MsgType.SA.name());
        msg.setMsgRefNo(msgRefNo);
        msg.setBuyerOid(biz.getBuyerOid());
        msg.setBuyerCode(biz.getBuyerCode());
        msg.setBuyerName(biz.getBuyerName());
        msg.setCreateDate(transBatch.getCreateDate());
        msg.setProcDate(transBatch.getCreateDate());
        msg.setOriginalFilename(originalFilename);
        msg.setActive(true);
        msg.setValid(true);
        return msg;
    }
    
    public static String radmonPassword(int length)
    {
        java.util.Random rdm = new java.util.Random();
        StringBuffer newPasswd = new StringBuffer();

        while (newPasswd.length() < length)
        {
            long value = rdm.nextLong();
            if (value < 0)
            {
                value = -value;
            }
            String temp = Long.toString(value);
            newPasswd.append(temp);
        }

        return newPasswd.substring(0, length);
    }
    
    private String[] parseEmailAddrs(String addrs)
    {
        String[] parts = addrs.trim().split(",");

        for (String part : parts)
        {
            part = part.trim();
        }

        return parts;
    }
    
}
