package com.pracbiz.b2bportal.core.eai.backend;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.pracbiz.b2bportal.core.eai.file.validator.util.EmptyValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.SpecialCharValidator;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerStoreExHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.TransactionBatchService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;


/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class StoreImportJob extends BaseJob implements 
        CoreCommonConstants
{
    private static final String LINE_NO = "lineNo : ";
    private static final String LEFT_SEPERATE = " [ ";
    private static final String RIGHT_SEPERATE = " ] ";
    private static final Logger log = LoggerFactory
            .getLogger(StoreImportJob.class);
    private static final String ID = "[StoreImportJob]";
    
    private StandardEmailSender standardEmailSender;
    private BuyerService buyerService;
    private BuyerStoreService buyerStoreService;
    private OidService oidService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private EmailEngine emailEngine;
    private TransactionBatchService transactionBatchService;
    private MailBoxUtil mboxUtil;
    
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        buyerStoreService = this.getBean("buyerStoreService", BuyerStoreService.class);
        oidService = this.getBean("oidService", OidService.class);
        emailEngine = this.getBean("emailEngine", EmailEngine.class);
        transactionBatchService = this.getBean("transactionBatchService", TransactionBatchService.class);
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

        File[] stBatchFiles = outPath.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file)
            {
                return file.getName().toUpperCase()
                    .matches("ST_" + buyer.getBuyerCode().toUpperCase() + "_[0-9]+.ZIP");
            }

        });
        
        if (stBatchFiles == null || stBatchFiles.length < 1)
        {
            log.info("No Store Master files found for buyer [" + buyer.getBuyerName() + "].");
            
            return;
        }
        
        stBatchFiles = FileUtil.getInstance().getStableFiles(stBatchFiles);
        
        if (stBatchFiles == null || stBatchFiles.length < 1)
        {
            log.info("No Store Master files found for buyer [" + buyer.getBuyerName() + "].");
            
            return;
        }

        List<File> stableFiles = Arrays.asList(stBatchFiles);
        
        /*Collections.sort(stableFiles, new Comparator<File>(){
            @Override
            public int compare(File o1, File o2) {
                String o1Date = o1.getName().substring(o1.getName().lastIndexOf(CoreCommonConstants.DOC_FILENAME_DELIMITOR), 
                        o1.getName().lastIndexOf("."));
                String o2Date = o2.getName().substring(o2.getName().lastIndexOf(CoreCommonConstants.DOC_FILENAME_DELIMITOR), 
                        o2.getName().lastIndexOf("."));
                return o1Date.compareTo(o2Date);
            }
        });*/

        for (File stZipFile : stableFiles)
        {
            try
            {
                processStZipFile(buyer, stZipFile);
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
        }
    }
    
    
    private void processStZipFile(BuyerHolder buyer, File stZipFile) throws IOException
    {
        File workDir = new File(mboxUtil.getBuyerWorkingOutPath(buyer.getMboxId()) + PS
            + stZipFile.getName().substring(0, stZipFile.getName().lastIndexOf(".")));
        
        File resultBatchFile = new File(workDir, stZipFile.getName());
        List<File> extractedFiles = null;
        TransactionBatchHolder transBatch = null;
        
        try
        {
            try
            {
                FileUtil.getInstance().moveFile(stZipFile, workDir.getPath());
                
                extractedFiles = GZIPHelper.getInstance()
                    .unZipAndReturnExtractFiles(resultBatchFile, workDir.getPath());
                
                transBatch = this.initTransBatch(resultBatchFile.getName());
                transactionBatchService.insert(transBatch);
                
            }
            catch(Exception e)
            {
                File invalidPath = new File(mboxUtil
                    .getFolderInBuyerInvalidPath(buyer.getMboxId(), DateUtil
                        .getInstance().getYearAndMonth(new Date())));
                
                if (stZipFile.exists())
                {
                    FileUtil.getInstance().moveFile(stZipFile, invalidPath.getPath());
                }
                else if (resultBatchFile.exists())
                {
                    FileUtil.getInstance().moveFile(resultBatchFile, invalidPath.getPath());
                }
                
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
                
                return;
            }
            
            boolean flag = false;
            int succCount = 0;
            for (File stFile : extractedFiles)
            {
                try
                {
                    flag = this.processStFile(buyer, transBatch, stFile);
                    
                    if (flag)
                    {
                        succCount++;
                    }
                }
                catch(Exception e)
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
    
    
    private boolean processStFile(BuyerHolder buyer,
        TransactionBatchHolder transBatch, File stFile) throws Exception
    {
        MsgTransactionsHolder msg = initMsgTransactions(buyer, transBatch, stFile.getName());
        List<String> contents = FileUtils.readLines(stFile);
        
        if (contents == null || contents.isEmpty())
        {
            throw new Exception("Store Master file [" + stFile.getName() + "] is empty.");
        }
        
        List<BuyerStoreExHolder> newList = new ArrayList<BuyerStoreExHolder>();
        List<BuyerStoreExHolder> updateList = new ArrayList<BuyerStoreExHolder>();
        List<BuyerStoreHolder> removeList = new ArrayList<BuyerStoreHolder>();
        List<String> unProcessList = new ArrayList<String>();
        
        try
        {
            for (int i = 1; i < contents.size(); i++)
            {
                String obj = contents.get(i);
                int index = i;
                StringReader sr = new StringReader(contents.get(i));
                CSVReader reader = new CSVReader(sr, ',', '\"');
                String[] content = null;
                
                try
                {
                    content = reader.readNext();
                }
                finally
                {
                    if (reader != null)
                    {
                        reader.close();
                        reader = null;
                    }
                    if (sr != null)
                    {
                        sr.close();
                        sr = null;
                    }
                }
                
                if (content.length != 17)
                {
                    log.info("length of [" + obj + "] is not 17");
                    unProcessList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                    continue;
                }
                BuyerStoreExHolder buyerStoreExHolder = initBuyerStoreExHolder(content);
                buyerStoreExHolder.trimAllString();
                buyerStoreExHolder.setAllEmptyStringToNull();
                
                String result = null;
                
                FieldContentValidator storeCodeValidator = null;
                storeCodeValidator = new EmptyValidator();
                storeCodeValidator = new LengthValidator(20, storeCodeValidator);
                result = storeCodeValidator.validate(buyerStoreExHolder.getStoreCode(), LINE_NO + index + " [Store Code]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator storeNameValidator = null;
                storeNameValidator = new LengthValidator(100, storeNameValidator);
                result = storeNameValidator.validate(buyerStoreExHolder.getStoreName(), LINE_NO + index + " [Store Name]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator storeAreaCodeValidator = null;
                storeAreaCodeValidator = new LengthValidator(20, storeAreaCodeValidator);
                result = storeAreaCodeValidator.validate(buyerStoreExHolder.getAreaCode(), LINE_NO + index + " [Area Code]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator storeAreaNameValidator = null;
                storeAreaNameValidator = new LengthValidator(50, storeAreaNameValidator);
                result = storeAreaNameValidator.validate(buyerStoreExHolder.getAreaName(), LINE_NO + index + " [Area Name]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator actionValidator = null;
                actionValidator = new EmptyValidator();
                actionValidator = new SpecialCharValidator(actionValidator, false, "A", "R", "D");
                result = actionValidator.validate(buyerStoreExHolder.getAction(), LINE_NO + index + " [Action]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator addressValidator1 = null;
                addressValidator1 = new LengthValidator(100, addressValidator1);
                result = addressValidator1.validate(buyerStoreExHolder.getStoreAddr1(), LINE_NO + index + " [Store Address1]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator addressValidator2 = null;
                addressValidator2 = new LengthValidator(100, addressValidator2);
                result = addressValidator2.validate(buyerStoreExHolder.getStoreAddr2(), LINE_NO + index + " [Store Address2]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator addressValidator3 = null;
                addressValidator3 = new LengthValidator(100, addressValidator3);
                result = addressValidator3.validate(buyerStoreExHolder.getStoreAddr3(), LINE_NO + index + " [Store Address3]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator addressValidator4 = null;
                addressValidator4 = new LengthValidator(100, addressValidator4);
                result = addressValidator4.validate(buyerStoreExHolder.getStoreAddr4(), LINE_NO + index + " [Store Address4]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator addressValidator5 = null;
                addressValidator5 = new LengthValidator(100, addressValidator5);
                result = addressValidator5.validate(buyerStoreExHolder.getStoreAddr5(), LINE_NO + index + " [Store Address5]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator storeCityValidator = null;
                storeCityValidator = new LengthValidator(50, storeCityValidator);
                result = storeCityValidator.validate(buyerStoreExHolder.getStoreCity(), LINE_NO + index + " [Store City]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator storeStateValidator = null;
                storeStateValidator = new LengthValidator(20, storeStateValidator);
                result = storeStateValidator.validate(buyerStoreExHolder.getStoreState(), LINE_NO + index + " [Store State]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator storeCrtyCodeValidator = null;
                storeCrtyCodeValidator = new LengthValidator(2, storeCrtyCodeValidator);
                result = storeCrtyCodeValidator.validate(buyerStoreExHolder.getStoreCtryCode(), LINE_NO + index + " [Store Country Code]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator storePostCodeValidator = null;
                storePostCodeValidator = new LengthValidator(15, storePostCodeValidator);
                result = storePostCodeValidator.validate(buyerStoreExHolder.getStorePostalCode(), LINE_NO + index + " [Store Postal Code]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator contactPersongValidator = null;
                contactPersongValidator = new LengthValidator(50, contactPersongValidator);
                result = contactPersongValidator.validate(buyerStoreExHolder.getContactPerson(), LINE_NO + index + " [Store Contact Person]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator contactTelValidator = null;
                contactTelValidator = new LengthValidator(20, contactTelValidator);
                result = contactTelValidator.validate(buyerStoreExHolder.getContactTel(), LINE_NO + index + " [Store Telephone]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                FieldContentValidator emailAddrValidator = null;
                emailAddrValidator = new LengthValidator(50, emailAddrValidator);
                result = emailAddrValidator.validate(buyerStoreExHolder.getContactEmail(), LINE_NO + index + " [Store Email Address]");
                if (result != null)
                {
                    unProcessList.add(result);
                    continue;
                }
                
                buyerStoreExHolder.setBuyerCode(buyer.getBuyerCode());
                
                BuyerStoreHolder oldBuyerStore = buyerStoreService
                    .selectBuyerStoreByBuyerCodeAndStoreCode(
                        buyerStoreExHolder.getBuyerCode(),buyerStoreExHolder.getStoreCode());
                
                if ("D".equalsIgnoreCase(buyerStoreExHolder.getAction()))
                {
                    if (oldBuyerStore == null)
                    {
                        unProcessList.add(LINE_NO + index + " there is no record in database with store code [" + buyerStoreExHolder.getBuyerCode() + "] for actioin [D].");
                        
                        continue;
                    }
                    
                    removeList.add(oldBuyerStore);
                    
                    continue;
                }
                
                if (null == oldBuyerStore)
                {
                    newList.add(buyerStoreExHolder);
                }
                else
                {
                    updateList.add(buyerStoreExHolder);
                }
            }
            
            List<BuyerStoreExHolder> recordList = new ArrayList<BuyerStoreExHolder>();
            for (BuyerStoreExHolder store : newList)
                recordList.add(store);
            for (BuyerStoreExHolder store : updateList)
                recordList.add(store);
            
            transactionBatchService.insertStoreMaster(msg, recordList, removeList);
            
            
            BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(
                buyer.getBuyerOid(), MsgType.ST.name());
        
            if(null == setting || null == setting.getRcpsAddrs()
                || setting.getRcpsAddrs().trim().isEmpty())
            {
                return false;
            }
            
            if (!unProcessList.isEmpty() || (unProcessList.isEmpty() && !setting.getExcludeSucc()))
            {
                String[] emailTo = setting
                    .retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
                String subject = "Store Master Status - " + stFile.getName();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("BUYER_NAME", buyer.getBuyerName());
                map.put("BUYER_CODE", buyer.getBuyerCode());
                map.put("STORE_FILE_NAME", stFile.getName());
                map.put("NEW_LIST", newList);
                map.put("UPDATE_LIST", updateList);
                map.put("DELETE_LIST", removeList);
                map.put("UNHANDELED_LIST", unProcessList);
                emailEngine.sendHtmlEmail(emailTo, subject,
                    "ALERT_BUYER_STORE_MASTER.vm", map);
            }
            
            
            if (!newList.isEmpty() || !updateList.isEmpty() || !removeList.isEmpty())
            {
                return true;
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
        return false;
    }
    
    
    private BuyerStoreExHolder initBuyerStoreExHolder(String[] content) throws Exception
    {
        BuyerStoreExHolder buyerStoreExHolder = new BuyerStoreExHolder();
        buyerStoreExHolder.setStoreCode(content[0]);
        buyerStoreExHolder.setStoreName(content[1]);
        buyerStoreExHolder.setAreaCode(content[2]);
        buyerStoreExHolder.setAreaName(null);
        buyerStoreExHolder.setAction(content[3]);
        buyerStoreExHolder.setStoreAddr1(content[4]);
        buyerStoreExHolder.setStoreAddr2(content[5]);
        buyerStoreExHolder.setStoreAddr3(content[6]);
        buyerStoreExHolder.setStoreAddr4(content[7]);
        buyerStoreExHolder.setStoreAddr5(content[8]);
        buyerStoreExHolder.setStoreCity(content[9]);
        buyerStoreExHolder.setStoreState(content[10]);
        buyerStoreExHolder.setStoreCtryCode(content[11]);
        buyerStoreExHolder.setStorePostalCode(content[12]);
        buyerStoreExHolder.setContactPerson(content[13]); 
        buyerStoreExHolder.setContactTel(content[14]);
        buyerStoreExHolder.setContactEmail(content[15]);
        if ("Y".equalsIgnoreCase(content[16]))
        {
            buyerStoreExHolder.setIsWareHouse(true);
        }
        else
        {
            buyerStoreExHolder.setIsWareHouse(false);
        }
        return buyerStoreExHolder;
    }
    
    
    private TransactionBatchHolder initTransBatch(String filename) throws Exception
    {
        TransactionBatchHolder transBatch = new TransactionBatchHolder();

        transBatch.setBatchOid(oidService.getOid());
        transBatch.setBatchNo(FileUtil.getInstance().trimAllExtension(filename));
        transBatch.setBatchFilename(filename);
        transBatch.setCreateDate(new Date());
        transBatch.setAlertSenderDate(null);

        return transBatch;
    }
    
    
    private MsgTransactionsHolder initMsgTransactions(BuyerHolder buyer,
        TransactionBatchHolder transBatch, String originalFilename)
        throws Exception
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();

        msg.setDocOid(oidService.getOid());
        msg.setBatchOid(transBatch.getBatchOid());
        msg.setMsgType(MsgType.ST.name());
        msg.setMsgRefNo(FileUtil.getInstance().trimAllExtension(originalFilename).split("_")[2]);
        msg.setBuyerOid(buyer.getBuyerOid());
        msg.setBuyerCode(buyer.getBuyerCode());
        msg.setBuyerName(buyer.getBuyerName());
        msg.setCreateDate(transBatch.getCreateDate());
        msg.setProcDate(transBatch.getCreateDate());
        msg.setOriginalFilename(originalFilename);
        msg.setActive(true);
        msg.setValid(true);
        
        return msg;
    }
    
}
