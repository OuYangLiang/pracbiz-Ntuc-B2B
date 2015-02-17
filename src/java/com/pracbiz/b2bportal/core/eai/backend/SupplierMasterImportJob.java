package com.pracbiz.b2bportal.core.eai.backend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorFileEmailSender;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.eai.file.SupplierMasterFileParserUtil;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.CountryHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.CountryService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.SupplierRoleService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.TransactionBatchService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class SupplierMasterImportJob extends BaseJob implements
        CoreCommonConstants
{
    public final static byte[] lock = new byte[0];
    public static volatile boolean isAnyJobRunning = false;
    
    private static final Logger log = LoggerFactory
            .getLogger(SupplierMasterImportJob.class);
    private static final String ID = "[SupplierMasterImportJob]";

    private static final String ALERT_BUYER_SM = "ALERT_BUYER_SUPPLIER_MASTER.vm";
    private static final String ALERT_BUYER_NEW_SM = "ALERT_BUYER_NEW_SUPPLIER_MASTER.vm";
    private static final String OPERATE_BY = "SYSTEM";

    private BuyerService buyerService;
    private SupplierService supplierService;
    private EmailEngine emailEngine;
    private StandardEmailSender standardEmailSender;
    private OidService oidService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private ErrorFileEmailSender errorFileEmailSender;
    private CountryService countryService;
    private TradingPartnerService tradingPartnerService;

    private MailBoxUtil mboxUtil;

    private BusinessRuleService businessRuleService;
    private RoleService roleService;
    private TransactionBatchService transactionBatchService;
    private CustomAppConfigHelper appConfig;
    private MsgTransactionsService msgTransactionsService;
    private ControlParameterService controlParameterService;
    private SupplierRoleService supplierRoleService;

    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        supplierService = this.getBean("supplierService", SupplierService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        oidService = this.getBean("oidService", OidService.class);
        appConfig = this.getBean("appConfig", CustomAppConfigHelper.class);
        emailEngine = this.getBean("emailEngine", EmailEngine.class);
        controlParameterService = this.getBean("controlParameterService", ControlParameterService.class);
        tradingPartnerService = this.getBean("tradingPartnerService", TradingPartnerService.class);
        errorFileEmailSender = this.getBean("errorFileEmailSender", ErrorFileEmailSender.class);
        countryService = this.getBean("countryService", CountryService.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
        roleService = this.getBean("roleService", RoleService.class);
        transactionBatchService = this.getBean("transactionBatchService", TransactionBatchService.class);
        msgTransactionsService = this.getBean("msgTransactionsService", MsgTransactionsService.class);
        supplierRoleService = this.getBean("supplierRoleService", SupplierRoleService.class);
    }


    @Override
    protected void process()
    {
        try
        {
            synchronized(lock)
            {
                while (isAnyJobRunning)
                {
                    try
                    {
                        lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
                
                isAnyJobRunning = true;
            }
            
            realProcess();
        }
        finally
        {
            synchronized (lock)
            {
                isAnyJobRunning = false;

                lock.notifyAll();
            }
        }
    }
    
    
    private void realProcess()
    {
        log.info(" :::: Start to process.");

        try
        {
            List<BuyerHolder> buyers = buyerService.selectActiveBuyers();
            
            if (buyers == null || buyers.isEmpty())
            {
                log.info("There is no active buyer in system.");
                return;
            }
            
            for (final BuyerHolder buyer : buyers)
            {
                processBuyer(buyer);
            }

            log.info(" :::: Processed.");
        }

        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }
    
    
    private void processBuyer(final BuyerHolder buyer) throws IOException
    {
        String mboxId = buyer.getMboxId();

        File buyerOutPath = new File(mboxUtil.getBuyerOutPath(mboxId));
        File buyerInvalidPath = new File(mboxUtil.getFolderInBuyerInvalidPath(mboxId, DateUtil.getInstance().getCurrentYearAndMonth()));
        File buyerArchive = new File(mboxUtil.getFolderInBuyerArchOutPath(mboxId, DateUtil.getInstance().getYearAndMonth(new Date())));
        FileUtil.getInstance().createDir(buyerOutPath);
        FileUtil.getInstance().createDir(buyerInvalidPath);
        FileUtil.getInstance().createDir(buyerArchive);

        File[] outFiles = buyerOutPath.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file)
            {
                return file.getName().toUpperCase().matches("SM_" + buyer.getBuyerCode().toUpperCase() + "_[0-9]+.ZIP");
            }

        });

        if (null == outFiles || outFiles.length == 0)
        {
            log.debug(" :::: No Supplier Master files found for Buyer ["
                    + buyer.getBuyerName() + "].");
            return;
        }

        List<File> stableFiles = Arrays.asList(FileUtil.getInstance().getStableFiles(outFiles));

        if (stableFiles == null || stableFiles.isEmpty())
        {
            return;
        }
        

        BuyerMsgSettingHolder setting = null;
        try
        {
            setting = buyerMsgSettingService
                    .selectByKey(buyer.getBuyerOid(), MsgType.SM.name());
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }

        for (File batchFile : stableFiles)
        {
            processBatchFile(buyer, batchFile, setting);
        }
    }
    
    
    private List<File> retrieveExtractedFiles(File path, final String batchFile)
    {
        File[] files = path.listFiles(new FileFilter()
        {
            public boolean accept(File pathname)
            {
                return !batchFile.equals(pathname.getName());
            }
        });

        return Arrays.asList(files);
    }
    
    private void processBatchFile(BuyerHolder buyer, File batchFile, BuyerMsgSettingHolder setting) throws IOException
    {

        File workDir = new File(mboxUtil.getBuyerWorkingOutPath(buyer.getMboxId())+ PS
                + batchFile.getName().substring(0,batchFile.getName().lastIndexOf(".")));
        FileUtil.getInstance().createDir(workDir);
        FileUtil.getInstance().moveFile(batchFile, workDir.getPath());
        File resultBatchFile = new File(workDir, batchFile.getName());
        List<File> extractedFiles = null;
        TransactionBatchHolder transBatch = null;
        try
        {
            try
            {
                transBatch = this.initTransBatch(
                        FileUtil.getInstance().trimAllExtension(resultBatchFile.getName()), resultBatchFile.getName());
                transactionBatchService.insert(transBatch);
                GZIPHelper.getInstance().unZip(resultBatchFile, workDir.getPath());
                
                extractedFiles = this.retrieveExtractedFiles(workDir,batchFile.getName());
                if (extractedFiles == null || extractedFiles.isEmpty())
                {
                    return;
                }
            }
            catch (Exception e)
            {
                File invalidPath = new File(mboxUtil
                        .getFolderInBuyerInvalidPath(buyer.getMboxId(), DateUtil
                            .getInstance().getYearAndMonth(new Date())));
                if (batchFile.exists())
                {
                    FileUtil.getInstance().moveFile(batchFile, invalidPath.getPath());
                }
                else if (resultBatchFile.exists())
                {
                    FileUtil.getInstance().moveFile(batchFile, invalidPath.getPath());
                }
                
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
                
                return;
            }
            
            int succCount = 0;
            for (File file : extractedFiles)
            {
                try
                {
                    if (!validationFileContentCorrect(file, setting))
                    {
                        continue;
                    }
                    
                    this.processFile(file, buyer, new Date(),setting, transBatch);
                    succCount++;
                    
                }
                catch (Exception e)
                {
                    String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                    standardEmailSender.sendStandardEmail(ID, tickNo, e);
                }
            }
            
            if (succCount == 0)
            {
                File invalidPath = new File(mboxUtil
                    .getFolderInBuyerInvalidPath(buyer.getMboxId(), DateUtil
                            .getInstance().getYearAndMonth(new Date())));
            
                FileUtil.getInstance().moveFile(resultBatchFile, invalidPath.getPath());
            }
            else
            {
                File archDir = new File(mboxUtil.getBuyerArchOutPath(buyer.getMboxId()) + PS
                    + DateUtil.getInstance().getCurrentYearAndMonth());
                
                FileUtil.getInstance().moveFile(resultBatchFile, archDir.getPath());
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
    

    private void processFile(File file, BuyerHolder buyer,
            Date uploadStart, BuyerMsgSettingHolder setting, TransactionBatchHolder transBatch) throws Exception
    {
        List<SupplierHolder> newSms = new ArrayList<SupplierHolder>();
        List<SupplierHolder> oldSms = new ArrayList<SupplierHolder>();
        List<SupplierHolder> errorSms = new ArrayList<SupplierHolder>();

        List<SupplierHolder> sms = SupplierMasterFileParserUtil.getInstance().parseFile(file);

        Map<String, String> errorParam = new HashMap<String, String>();
        
        boolean autoGenBr = businessRuleService.isGenResultTxt(buyer.getBuyerOid());
        Boolean isSendEmailToSetPwd = Boolean.TRUE;
        List<ControlParameterHolder> msgList = controlParameterService
                .selectCacheControlParametersBySectId(SECT_ID_HSEKEEP);
        
        String msgRefNo = FileUtil.getInstance()
                .trimAllExtension(file.getName()).split("_")[2];
        MsgTransactionsHolder msg = this.initMsgTransactions(buyer, transBatch,
                msgRefNo);
        msgTransactionsService.insert(msg);
        
        if (autoGenBr)
        {
            isSendEmailToSetPwd = Boolean.FALSE;
        }
        
        boolean autoGenSA = businessRuleService.isGenAdminUser(buyer.getBuyerOid());
        RoleHolder definedRole = null;
        if (autoGenSA)
        {
            definedRole = this.retriveSupplierAdminRole(buyer);
        }
        
        SupplierRoleHolder sr = null;
        if (definedRole != null)
        {
            sr = supplierRoleService.selectByRoleAndSupplier(definedRole.getRoleOid(), BigDecimal.valueOf(-1));
        }
        
        
        for (SupplierHolder sm : sms)
        {
            SupplierHolder supplier = supplierService.selectSupplierByCode(sm
                    .getSupplierCode());

            CountryHolder country = countryService.selectByCtryCode(sm
                    .getCtryCode().toUpperCase());

            if (null == supplier)
            {
                sm.setSupplierOid(oidService.getOid());
                sm.setMboxId(sm.getSupplierCode());
                sm.setCtryCode(country == null ? "SG" : country.getCtryCode());
                sm.setBranch(false);
                sm.setBlocked(false);
                sm.setAutoInvNumber(false);
                sm.setDeploymentMode(DeploymentMode.LOCAL);
                sm.setClientEnabled(false);
                sm.setRequireReport(false);
                sm.setRequireTranslationIn(false);
                sm.setRequireTranslationOut(false);
                initGst(buyer, sm);

                if (!this.validationLogicCorrect(sm, buyer, errorParam))
                {
                    errorSms.add(sm);
                    continue;
                }
                
                if (autoGenSA)
                {
                    this.handleRoleAndAutoGenAdmin(sm, definedRole, sr == null ? true : false);
                }

                TradingPartnerHolder tp = new TradingPartnerHolder();
                tp.setTradingPartnerOid(oidService.getOid());
                tp.setBuyerSupplierCode(sm.getSupplierCode());
                tp.setSupplierBuyerCode(buyer.getBuyerCode());
                tp.setBuyerContactEmail(buyer.getContactEmail());
                tp.setBuyerContactFax(buyer.getContactFax());
                tp.setBuyerContactMobile(buyer.getContactMobile());
                tp.setBuyerContactPerson(buyer.getContactName());
                tp.setBuyerContactTel(buyer.getContactTel());
                tp.setSupplierContactEmail(sm.getContactEmail());
                tp.setSupplierContactFax(sm.getContactFax());
                tp.setSupplierContactMobile(sm.getContactMobile());
                tp.setSupplierContactPerson(sm.getContactName());
                tp.setSupplierContactTel(sm.getContactTel());
                tp.setActive(true);
                tp.setCreateDate(new Date());
                tp.setCreateBy(OPERATE_BY);
                tp.setBuyerOid(buyer.getBuyerOid());
                tp.setSupplierOid(sm.getSupplierOid());

                try
                {
                    supplierService.insertNewSupplierFromSupplierMaster(sm, tp, msgList, isSendEmailToSetPwd);
                    newSms.add(sm);
                }
                catch (Exception e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
            else
            {
                
                supplier.setSupplierName(sm.getSupplierName());
                supplier.setSupplierAlias(sm.getSupplierAlias());
                supplier.setTransMode(sm.getTransMode());
                supplier.setRegNo(sm.getRegNo());
                supplier.setGstRegNo(sm.getGstRegNo());
                if (null == supplier.getGstPercent())
                {
                    initGst(buyer, supplier);
                }
                supplier.setSupplierSource(sm.getSupplierSource());
                supplier.setAddress1(sm.getAddress1());
                supplier.setAddress2(sm.getAddress2());
                supplier.setAddress3(sm.getAddress3());
                supplier.setAddress4(sm.getAddress4());
                supplier.setState(sm.getState());
                supplier.setPostalCode(sm.getPostalCode());
                supplier.setCurrCode(sm.getCurrCode());
                supplier.setContactName(sm.getContactName());
                supplier.setContactTel(sm.getContactTel());
                supplier.setContactMobile(sm.getContactMobile());
                supplier.setContactEmail(sm.getContactEmail());
                supplier.setContactFax(sm.getContactFax());
                supplier.setActive(sm.getActive());
                supplier.setUpdateDate(new Date());
                supplier.setUpdateBy(OPERATE_BY);
                supplier.setCtryCode(country == null ? "SG" : country
                        .getCtryCode());

                TradingPartnerHolder tp = tradingPartnerService.selectByBuyerAndBuyerGivenSupplierCode(
                        buyer.getBuyerOid(), supplier.getSupplierCode());
                boolean flag = false;
                
                if (tp == null)
                {
                    tp = new TradingPartnerHolder();
                    tp.setTradingPartnerOid(oidService.getOid());
                    tp.setBuyerSupplierCode(supplier.getSupplierCode());
                    tp.setSupplierBuyerCode(buyer.getBuyerCode());
                    tp.setBuyerContactEmail(buyer.getContactEmail());
                    tp.setBuyerContactFax(buyer.getContactFax());
                    tp.setBuyerContactMobile(buyer.getContactMobile());
                    tp.setBuyerContactPerson(buyer.getContactName());
                    tp.setBuyerContactTel(buyer.getContactTel());
                    tp.setSupplierContactEmail(supplier.getContactEmail());
                    tp.setSupplierContactFax(supplier.getContactFax());
                    tp.setSupplierContactMobile(supplier.getContactMobile());
                    tp.setSupplierContactPerson(supplier.getContactName());
                    tp.setSupplierContactTel(supplier.getContactTel());
                    tp.setActive(true);
                    tp.setCreateDate(new Date());
                    tp.setCreateBy(OPERATE_BY);
                    tp.setBuyerOid(buyer.getBuyerOid());
                    tp.setSupplierOid(supplier.getSupplierOid());
                    flag = true;
                }
                else
                {
                    tp.setSupplierContactEmail(supplier.getContactEmail());
                    tp.setSupplierContactFax(supplier.getContactFax());
                    tp.setSupplierContactMobile(supplier.getContactMobile());
                    tp.setSupplierContactPerson(supplier.getContactName());
                    tp.setSupplierContactTel(supplier.getContactTel());
                    tp.setUpdateBy(OPERATE_BY);
                    tp.setUpdateDate(new Date());
                    flag = false;
                }
                try
                {
                    supplierService.updateOldSupplierFromSupplierMaster(supplier, tp, flag);
                    oldSms.add(supplier);
                }
                catch (Exception e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
        }
        
        if (!errorSms.isEmpty() || (errorSms.isEmpty() && !setting.getExcludeSucc()))
        {
            byte[] pwdTxt = null;
            byte[] pwdExcel = null;
            if (autoGenBr && newSms != null)
            {
                pwdTxt = this.generatePasswordForNewUser(newSms);
                pwdExcel = this.generatePasswordExcelForNewUser(newSms, buyer.getBuyerCode());
            }
            if (newSms != null && !newSms.isEmpty())
            {
                this.sendHtmlEmail(newSms, buyer, newSms.size(), 0, file.getName(),
                    uploadStart, setting, errorParam, ALERT_BUYER_NEW_SM, isSendEmailToSetPwd, pwdTxt, pwdExcel);
            }
            sms.removeAll(errorSms);
            this.sendHtmlEmail(sms, buyer, newSms.size(), oldSms.size(),
                file.getName(), uploadStart, setting, errorParam, ALERT_BUYER_SM, Boolean.TRUE, pwdTxt, pwdExcel);
        }
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
            TransactionBatchHolder transBatch, String msgRefNo)
            throws Exception
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();

        msg.setDocOid(oidService.getOid());
        msg.setBatchOid(transBatch.getBatchOid());
        msg.setMsgType(MsgType.SM.name());
        msg.setMsgRefNo(msgRefNo);
        msg.setBuyerOid(biz.getBuyerOid());
        msg.setBuyerCode(biz.getBuyerCode());
        msg.setBuyerName(biz.getBuyerName());
        msg.setCreateDate(transBatch.getCreateDate());
        msg.setProcDate(transBatch.getCreateDate());
        msg.setOriginalFilename(transBatch.getBatchFilename());
        msg.setActive(true);
        msg.setValid(true);
        return msg;
    }


    private void sendHtmlEmail(List<SupplierHolder> sms, BuyerHolder buyer,
            int inserted, int updated, String filename, Date uploadStart,
            BuyerMsgSettingHolder setting, Map<String, String> errorParam,
            String templateName, Boolean isSendEmailToSetPwd, byte[] pwdTxt, byte[] pwdExcel)
    {
        try
        {
            if (null == setting)
            {
                log.info(" :::: No message setting found for Bizunit ["
                        + buyer.getBuyerCode() + "], msg-type ["
                        + MsgType.SM.name()
                        + "], email will not be sent out to buyer.");

                return;
            }

            if (null == setting.getRcpsAddrs()
                    || "".equals(setting.getRcpsAddrs().trim()))
            {
                log.info(" :::: email address is empty for Bizunit ["
                        + buyer.getBuyerCode() + "], msg-type ["
                        + MsgType.SM.name()
                        + "], email will not be sent out to buyer.");

                return;
            }
            String emailAddress = setting.getRcpsAddrs();
            
            if (isSendEmailToSetPwd || pwdTxt.length == 0)
            {
                emailEngine.sendHtmlEmail(this.parseEmailAddrs(emailAddress), this
                        .getEmailTitle(), templateName, this.getEmailContent(sms,
                        buyer, inserted, updated, filename, uploadStart, errorParam));
            }
            else
            {
                String attachedFileName = "created-Users_"
                        + System.currentTimeMillis() + ".txt";
                String attachedExcelFileName = "created-Users_"
                        + System.currentTimeMillis() + ".xls";
                emailEngine.sendEmailWithAttachedDocuments(this
                        .parseEmailAddrs(emailAddress), this.getEmailTitle(),
                        templateName, this.getEmailContent(sms, buyer,
                                inserted, updated, filename, uploadStart,
                                errorParam), new String[] { attachedFileName, attachedExcelFileName },
                        new byte[][] { pwdTxt, pwdExcel });
            }
        }
        catch (Exception e)
        {
            log.error(" :::: Exception Occurred in send email step : "
                    + e.getMessage());
        }
    }


    private Map<String, Object> getEmailContent(List<SupplierHolder> sms,
            BuyerHolder buyer, int inserted, int updated, String filename,
            Date uploadStart, Map<String, String> errorParam)
    {
        Map<String, Object> content = new HashMap<String, Object>();

        List<SupplierHolder> inactivated = new ArrayList<SupplierHolder>();
        List<SupplierHolder> activated = new ArrayList<SupplierHolder>();

        for (SupplierHolder supplier : sms)
        {
            if (supplier.getActive())
            {
                activated.add(supplier);
            }
            else
            {
                inactivated.add(supplier);
            }

        }

        content.put("BATCH_FILE", filename);
        content.put("BIZUNIT_NAME", buyer.getBuyerName());
        content.put("BIZUNIT_CODE", buyer.getBuyerCode());

        content.put(
                "UPLOAD_START",
                DateUtil.getInstance().convertDateToString(uploadStart,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));

        content.put(
                "UPLOAD_END",
                DateUtil.getInstance().convertDateToString(new Date(),
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));

        content.put("TOTAL", sms.size() + errorParam.size());
        content.put("TOTAL_SUCC", sms.size());
        content.put("TOTAL_INSERTED", inserted);
        content.put("TOTAL_UPDATED", updated);
        content.put("TOTAL_INACTIVATED", inactivated.size());
        content.put("TOTAL_ACTIVATED", activated.size());
        content.put("TOTAL_ERROR", errorParam.size());
        content.put("SUCC_LIST", sms);
        content.put("INACTIVATED_LIST", inactivated);
        content.put("ACTIVATED_LIST", activated);
        content.put("ERROR_LIST", errorParam);

        return content;
    }


    private String getEmailTitle()
    {
        return "Batch Run Status Report - Supplier Master";
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


    private boolean validationFileContentCorrect(File file,
            BuyerMsgSettingHolder buyerMsgSetting)
    {
        try
        {
            List<String> errors = null;//SupplierMasterFileParserUtil.getInstance().validateFile(file, validationConfig);
                    

            if (null == errors || errors.isEmpty())
            {
                return true;
            }

            log.error(" :::: File [" + file.getName()
                    + "] is incorrect, move it into invalid folder.");

            List<String> errorFiles = new ArrayList<String>();
            errorFiles.add(file.getName());

            Map<File, List<String>> errContent = new HashMap<File, List<String>>();
            errContent.put(file, errors);

            errorFileEmailSender.sendEmail(buyerMsgSetting, errorFiles,
                    errContent);
        }
        catch (Exception e)
        {
            log.error(" :::: Exception Occurred in validation file content step : "
                    + e.getMessage());
            ErrorHelper.getInstance().logError(log, e);
        }

        return false;
    }


    private boolean validationLogicCorrect(SupplierHolder supplier,
            BuyerHolder buyer, Map<String, String> errParam)
    {
        try
        {
            SupplierHolder existingSupplier = supplierService
                    .selectSupplierByMboxId(supplier.getMboxId());
            if (existingSupplier != null)
            {
                String error = "Supplier ["
                        + supplier.getSupplierCode()
                        + "] MailBox ID [ "
                        + supplier.getMboxId()
                        + " ] already been used by another supplier, skip creating it.";
                log.error(error);

                errParam.put(supplier.getSupplierCode(), error);
                return false;
            }

            TradingPartnerHolder tp = tradingPartnerService
                    .selectByBuyerAndBuyerGivenSupplierCode(
                            buyer.getBuyerOid(), supplier.getSupplierCode());
            if (tp != null)
            {
                String error = "Supplier ["
                        + supplier.getSupplierCode()
                        + "] already been used by another trading partner, skip creating it.";
                log.error(error);

                errParam.put(supplier.getSupplierCode(), error);
                return false;
            }
            
            return true;
        }
        catch (Exception e)
        {
            log.error(" :::: Exception Occurred in validation logic step : "
                    + e.getMessage());
        }

        return false;
    }

    
    private void handleRoleAndAutoGenAdmin(SupplierHolder sm, RoleHolder definedRole, boolean roleFlag)
    {
        try
        {
            if (roleFlag)
            {
                SupplierRoleTmpHolder supplierRole = new SupplierRoleTmpHolder();
                supplierRole.setRoleOid(definedRole.getRoleOid());
                supplierRole.setSupplierOid(sm.getSupplierOid());
                sm.addSupplierRole(supplierRole);
            }

            this.initSupplierAdminUser(definedRole, sm);
        }
        catch (Exception e)
        {
            log.error("Exception occured : " + e.getMessage());
            return;
        }
        
    }
    
    
    private void initSupplierAdminUser(RoleHolder role, SupplierHolder sm)
    {
        try
        {
            UserProfileHolder user = new UserProfileHolder();
            String userId = "admin@" + (sm.getSupplierAlias() == null
                    || sm.getSupplierAlias().isEmpty() ? sm.getSupplierCode()
                    : sm.getSupplierAlias());
            user.setUserOid(oidService.getOid());
            user.setUserName(userId);
            user.setLoginId(userId);
            user.setLoginMode("PASSWORD");
            user.setLoginPwd(radmonPassword(6));
            user.setGender("F");
            user.setTel(sm.getContactTel());
            user.setMobile(sm.getContactMobile());
            user.setFax(sm.getContactFax());
            user.setEmail(sm.getContactEmail());
            user.setActive(true);
            user.setBlocked(sm.getActive() ? Boolean.FALSE : Boolean.TRUE);
            user.setCreateBy(OPERATE_BY);
            user.setCreateDate(new Date());
            user.setUserType(new BigDecimal(3));
            user.setSupplierOid(sm.getSupplierOid());
            user.setInit(Boolean.FALSE);
            if (user.getBlocked())
            {
                user.setBlockBy(OPERATE_BY);
                user.setBlockDate(new Date());
                user.setBlockRemarks("Supplier is inactive when importing");
            }
            RoleUserTmpHolder roleUser = new RoleUserTmpHolder();
            roleUser.setRoleOid(role.getRoleOid());
            roleUser.setUserOid(user.getUserOid());
            
            user.addRoleUser(roleUser);
            sm.setUserProfile(user);
        }
        catch (Exception e)
        {
            log.error("Exception occured : " + e.getMessage());
            sm.setSupplierRoles(null);
            sm.setUserProfile(null);
        }
    }
    
    
    private byte[] generatePasswordForNewUser(List<SupplierHolder> suppliers) throws Exception
    {
        List<Map<String, String>> msgParam = new ArrayList<Map<String,String>>();
        for (SupplierHolder supplier : suppliers)
        {
            UserProfileHolder user = supplier.getUserProfile();
            if (user == null)
            {
                continue;
            }
            
            Map<String, String> param = new HashMap<String, String>();
            
            param.put("SUPPLIERNAME", supplier.getSupplierName());
            param.put("LOGINID", user.getLoginId());
            param.put("LOGINPWD", user.getLoginPwd());
            msgParam.add(param);
        }
        return generateTxt(msgParam);
    }
    
    
    private byte[] generateTxt(List<Map<String, String>> msgParam) throws Exception
    {
        if (msgParam != null && !msgParam.isEmpty())
        {
            StringBuffer sb = new StringBuffer(300);

            for (Map<String, String> param : msgParam)
            {
                String receiver = "Dear " + param.get("SUPPLIERNAME") + ",\n\n";
                String loginId = "User Id =" + param.get("LOGINID") + "\n";
                String pwd = "Password =" + param.get("LOGINPWD") + "\n\n";
                sb.append(receiver);
                sb.append("The following User Id and Password has been created for you to login to Supplier Portal.\n\n");
                sb.append(loginId);
                sb.append(pwd);
                
                sb.append("On first login, please change the User Id to your company email address and use a secured password.\n\n"
                    + "Thank you.\n"
                    + "PracBiz B2B Portal System\n"
                    + "Customer Support"
                    + "\n\n\n"
                    + "---------------------------------------------------------\n\n");
            }

            return sb.toString().getBytes(CommonConstants.ENCODING_UTF8);
        }
        return new byte[0];

    }

    
    private byte[] generatePasswordExcelForNewUser(List<SupplierHolder> suppliers, String buyerCode) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        WritableSheet ws = wwb.createSheet("supplier master", 0);
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.CENTRE);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.LEFT);
        
        int col = 0;
        int row = 0;
        ws.addCell(new Label(col, row, "Supplier Name", format1));
        ws.mergeCells(col, row, col + 2, row + 1);
        col = col + 2;
        ws.addCell(new Label(++col, row, "Contact Person", format1));
        ws.mergeCells(col, row, ++col, row + 1);
        ws.addCell(new Label(++col, row, "Contact Tel", format1));
        ws.mergeCells(col, row, ++col, row + 1);
        ws.addCell(new Label(++col, row, "Contact Fax", format1));
        ws.mergeCells(col, row, ++col, row + 1);
        ws.addCell(new Label(++col, row, "Contact Mobile", format1));
        ws.mergeCells(col, row, ++col, row + 1);
        ws.addCell(new Label(++col, row, "Contact Email", format1));
        ws.mergeCells(col, row, ++col, row + 1);
        ws.addCell(new Label(++col, row, "Postcal Code", format1));
        ws.mergeCells(col, row, ++col, row + 1);
        ws.addCell(new Label(++col, row, "Login Admin", format1));
        ws.mergeCells(col, row, col + 3, row);
        ws.addCell(new Label(col, row + 1, "ID", format1));
        ws.mergeCells(col, row + 1, col + 1, row + 1);
        ws.addCell(new Label(col + 2, row + 1, "Password", format1));
        ws.mergeCells(col + 2, row + 1, col + 3, row + 1);
        col = col + 3;
        ws.addCell(new Label(++col, row, "Trading Partner", format1));
        ws.mergeCells(col, row, col + 3, row);
        ws.addCell(new Label(col, row + 1, "Buyer", format1));
        ws.mergeCells(col, row + 1, col + 1, row + 1);
        ws.addCell(new Label(col + 2, row + 1, "Supplier Code", format1));
        ws.mergeCells(col + 2, row + 1, col + 3, row + 1);
        row = row + 2;
        for (SupplierHolder supplier : suppliers)
        {
            UserProfileHolder user = supplier.getUserProfile();
            if (user == null)
            {
                continue;
            }
            col = 0;
            ws.addCell(new Label(col, row, supplier.getSupplierName(), format2));
            ws.mergeCells(col, row, col + 2, row);
            col = col + 2;
            ws.addCell(new Label(++col, row, supplier.getContactName(), format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, supplier.getContactTel(), format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, supplier.getContactFax(), format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, supplier.getContactMobile(), format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, supplier.getContactEmail(), format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, supplier.getPostalCode(), format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, user.getLoginId(), format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, user.getLoginPwd(), format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, buyerCode, format2));
            ws.mergeCells(col, row, ++col, row);
            ws.addCell(new Label(++col, row, supplier.getSupplierCode(), format2));
            ws.mergeCells(col, row, ++col, row);
            row ++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }
    
    
    private void initGst(BuyerHolder buyer, SupplierHolder supplier) throws Exception
    {
        BigDecimal currentGst = BigDecimal.ZERO;
        ControlParameterHolder control = controlParameterService.selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL, PARAM_ID_CURRENT_GST);
        if (control != null && control.getStringValue() != null && !control.getStringValue().trim().isEmpty())
        {
            currentGst = new BigDecimal(control.getStringValue());
        }
        Map<String, String> map = appConfig.getSupplierMasterGstConfig();
        if (map.containsKey(buyer.getBuyerCode()) && "UNITY".equalsIgnoreCase(map.get(buyer.getBuyerCode())))
        {
            initGstForUnity(supplier);
        }
        else if (supplier.getGstRegNo() != null)
        {
            supplier.setGstPercent(currentGst);
        }
    }
    
    
    private void initGstForUnity(SupplierHolder supplier)
    {
        if ("EX".equalsIgnoreCase(supplier.getRegNo()) || "S7".equalsIgnoreCase(supplier.getRegNo()))
        {
            if (supplier.getGstRegNo() != null && !supplier.getGstRegNo().isEmpty())
            {
                supplier.setGstPercent(BigDecimal.valueOf(7));
            }
        }
        else if ("NA".equalsIgnoreCase(supplier.getRegNo()) || supplier.getRegNo() == null 
                || "".equals(supplier.getRegNo().trim()))
        {
            supplier.setGstPercent(null);
        }
        else if(supplier.getGstRegNo() != null
            && !supplier.getGstRegNo().isEmpty()
            && "ZR".equalsIgnoreCase(supplier.getRegNo()))
        {
            supplier.setGstPercent(BigDecimal.ZERO);
        }
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
    
    
    private RoleHolder retriveSupplierAdminRole(BuyerHolder buyer) throws Exception
    {
        String brAdminRule = businessRuleService.selectSMAdminRole(buyer.getBuyerOid());
        if (brAdminRule == null || brAdminRule.trim().isEmpty())
        {
            log.info("Skip to generate supplier admin user, cause admin rule defined in business rule is empty.");
            return null;
        }
        
        List<RoleHolder> roles = roleService.selectByRoleId(brAdminRule);
        
        if (roles == null || roles.isEmpty())
        {
            log.info("Skip to generate supplier admin user, cause admin rule defined in business rule is invalid.");
            return null;
        }
        for (RoleHolder role : roles)
        {
            if (role.getBuyerOid().equals(buyer.getBuyerOid()) && role.getUserTypeOid().equals(new BigDecimal(3)))
            {
                return role;
            }
        }
        return null;
    }

}
