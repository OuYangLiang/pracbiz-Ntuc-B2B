package com.pracbiz.b2bportal.core.eai.backend;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.file.ItemFileParser;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public class ItemImportJob extends BaseJob implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(ItemImportJob.class);
    private static final String ID = "[NewItemImportJob]";
    
    private static final int PROCESS_TYPE_REPLACE = 0;
    private static final int PROCESS_TYPE_UPDATE_WITH_SELECT_ONE_BY_ONE = 1;
    
    private static final String batchType = "IM";
    
    private static int ERROR_LIMIT = 500;
    
    private StandardEmailSender standardEmailSender;
    private BuyerService buyerService;
    private OidService oidService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private EmailEngine emailEngine;
    private MailBoxUtil mboxUtil;
    private BusinessRuleService businessRuleService;
    private ItemService itemService;
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        oidService = this.getBean("oidService", OidService.class);
        emailEngine = this.getBean("emailEngine", EmailEngine.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
        itemService = this.getBean("itemService", ItemService.class);
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
            log.info("Process ended.\n");
            
            return;
        }
        
        for (BuyerHolder buyer : availableBuyers)
        {
            log.info("Start to process for buyer [" + buyer.getBuyerName() + "].");
            
            Long startTime = System.currentTimeMillis();
            this.processBuyer(buyer);
            
            log.info("Process ended for buyer [" + buyer.getBuyerName() + "].");
            Long endTime = System.currentTimeMillis();
            
            log.info("Processe time for buyer [" + buyer.getBuyerName() + "]: " + (endTime - startTime)/1000 + " second(s)." );
        }
        
        log.info("Process ended.");
    }
    
    
    private void processBuyer(final BuyerHolder buyer)
    {
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

        File[] itemBatchFiles = outPath.listFiles(new FileFilter()
        {

            @Override
            public boolean accept(File file)
            {
                return file.getName().toUpperCase(Locale.US).matches(
                    "IM_" + buyer.getBuyerCode().toUpperCase(Locale.US)
                        + "_[0-9]{14}.ZIP");
            }

        });
        
        
        if (itemBatchFiles != null && itemBatchFiles.length > 0)
        {
            itemBatchFiles = FileUtil.getInstance().getStableFiles(itemBatchFiles);
        }
        

        if (itemBatchFiles == null || itemBatchFiles.length < 1)
        {
            log.info("No Item files found for buyer [" + buyer.getBuyerName()
                + "].");
            
            return;
        }

        List<File> stableFiles = Arrays.asList(itemBatchFiles);

        try
        {
            BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(
                buyer.getBuyerOid(), batchType);
            
            if(null == setting || null == setting.getFileFormat()
                || setting.getFileFormat().trim().isEmpty())
            {
                log.error("Item file format is not set for buyer [" + buyer.getBuyerName() + "].");
                
                return;
            }
            
            boolean doReplace = businessRuleService.isItemDeleteAndInsert(buyer.getBuyerOid());
            boolean doUpdate = businessRuleService.isItemUpdate(buyer.getBuyerOid());
            boolean selectOneByOne = false;
            
            if ((!doReplace && !doUpdate) || (doReplace && doUpdate))
            {
                log.error("Business rule of Item file import is not correctly set for buyer [" + buyer.getBuyerName() + "].");
                
                return;
            }
            
            if (doUpdate)
            {
                selectOneByOne = businessRuleService.isItemSelectOneToCompare(buyer.getBuyerOid());
                
                if (!selectOneByOne)
                {
                    log.error("Business rule of Item file import is not correctly set for buyer [" + buyer.getBuyerName() + "].");
                    
                    return;
                }
            }
            
            int processType = doReplace ? PROCESS_TYPE_REPLACE
                : PROCESS_TYPE_UPDATE_WITH_SELECT_ONE_BY_ONE;

            for(File itemZipFile : stableFiles)
            {
                log.info("Start to process zip file [" + itemZipFile.getName() + "].");
                Long startTime = System.currentTimeMillis();
                
                processItemZipFile(buyer, itemZipFile, setting, processType);
                
                log.info("Process ended for zip file [" + itemZipFile.getName() + "].");
                Long endTime = System.currentTimeMillis();
                
                log.info("Processe time for zip file [" + itemZipFile.getName() + "]: " + (endTime - startTime)/1000 + " second(s)." );
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    private void processItemZipFile(BuyerHolder buyer, File itemZipFile,
        BuyerMsgSettingHolder setting, int processType) throws IOException
    {
        Map<String, List<String>> fileMap = GZIPHelper.getInstance().readFileContentFromZip(itemZipFile);
        Map<String, List<String>> errorMap = new HashMap<String, List<String>>();
        
        for (Iterator<Map.Entry<String,List<String>>> itr = fileMap.entrySet().iterator(); itr.hasNext();)
        {
            Map.Entry<String, List<String>> entry = itr.next();
            List<String> fileErrors = ItemFileParser.getInstance().filterErrorContent(entry.getKey(), entry.getValue(), setting.getFileFormat());
            
            if (fileErrors != null && !fileErrors.isEmpty())
            {
                errorMap.put(entry.getKey(), fileErrors);
            }
            
            if (null == entry.getValue() || entry.getValue().size() < 2)
            {
                // No lines is correct.
                log.warn("File [" + entry.getKey() + "] validated failed, all line(s) is/are incorrect.");
               
                itr.remove();
            }
        }
        
        if (fileMap.isEmpty())
        {
            File invalidPath = new File(mboxUtil.getFolderInBuyerInvalidPath(buyer.getMboxId(), 
                DateUtil.getInstance().getYearAndMonth(new Date())));
            
            FileUtil.getInstance().moveFile(itemZipFile, invalidPath.getPath());
            
            this.sendEmail(setting, itemZipFile, errorMap);
            
            return;
        }
        
        
        
        File workDir = new File(mboxUtil.getBuyerWorkingOutPath(buyer.getMboxId()) + PS
            + itemZipFile.getName().substring(0, itemZipFile.getName().lastIndexOf(".")));
            
        File resultBatchFile = new File(workDir, itemZipFile.getName());
        
        try
        {
            FileUtil.getInstance().moveFile(itemZipFile, workDir.getPath());
            
            processItemFile(buyer, resultBatchFile, setting, fileMap, processType);
            
            File archDir = new File(mboxUtil.getBuyerArchOutPath(buyer.getMboxId()) + PS
                + DateUtil.getInstance().getCurrentYearAndMonth());
            
            FileUtil.getInstance().moveFile(resultBatchFile, archDir.getPath());
            
            if (!errorMap.isEmpty())
            {
                try
                {
                    this.sendEmail(setting, itemZipFile, errorMap);
                }
                catch(Exception e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
        } 
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            File invalidPath = new File(mboxUtil.getFolderInBuyerInvalidPath(buyer.getMboxId(), 
                DateUtil.getInstance().getYearAndMonth(new Date())));
            
            FileUtil.getInstance().moveFile(itemZipFile, invalidPath.getPath());
        }
        finally
        {
            if (workDir.isDirectory())
            {
                FileUtil.getInstance().deleleAllFile(workDir);
            }
        }
    }
    
    
    private void processItemFile(BuyerHolder buyer, File resultBatchFile,
        BuyerMsgSettingHolder setting, Map<String, List<String>> fileMap, int processType) throws Exception
    {
        TransactionBatchHolder transBatch = initTransBatch(resultBatchFile.getName());
        
        List<String> successList = new ArrayList<String>();
        if (PROCESS_TYPE_REPLACE == processType)
        {
            deleteAndInsertItem(buyer, fileMap, setting.getFileFormat(), transBatch, successList);
        }
        else
        {
            updateItem(buyer, fileMap, setting.getFileFormat(), transBatch, successList);
        }
        
        if (null == setting.getRcpsAddrs() || setting.getRcpsAddrs().trim().isEmpty())
        {
            log.info("Email is not configured for msg type [ITEM], notification will not be sent.\n");
            
            return;
        }
        
        if (!setting.getExcludeSucc() && !successList.isEmpty())
        {
            try
            {
                String[] emailTo = setting  
                    .retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
                
                String subject = "Item Status Report- " + resultBatchFile.getName();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("BATCH_FILE", resultBatchFile.getName());
                map.put("BUYER_NAME", buyer.getBuyerName());
                map.put("BUYER_CODE", buyer.getBuyerCode());
                map.put("SUCC_LIST", successList);
                emailEngine.sendHtmlEmail(emailTo, subject, "ALERT_BUYER_ITEM.vm", map);
            }
            catch (Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
        }
    }
    
    
    private void deleteAndInsertItem(BuyerHolder buyer,Map<String, List<String>> fileMap , String fileFormat, TransactionBatchHolder transBatch,  List<String> successList )
        throws Exception
    {
        itemService.deleteAndInsertItem(buyer, fileMap, transBatch, fileFormat, successList);
    }
    
    
    private void updateItem(BuyerHolder buyer, Map<String, List<String>> fileMap, String fileFormat, TransactionBatchHolder transBatch, List<String> successList) throws Exception
    {
        itemService.updateItem(buyer, fileMap,  transBatch, fileFormat, successList);
    }
    
    
//    public static void main(String[] args)
//    {
//        Map<String, String> map = new HashMap<String, String>();
//        
//        map.put("a", "A");
//        map.put("b", "B");
//        map.put("c", "C");
//        
//        Iterator<String> it = map.values().iterator();
//        
//        while (it.hasNext())
//        {
//            String str = it.next();
//            
//            if (str.equals("A"))
//                it.remove();
//        }
//        
//        System.out.println(map);
//    }
    
    
//    private Map<BigInteger, ItemHolder> filterUpdateItemMap(BigDecimal buyerOid, Map<String, ItemHolder> fileItemMap) throws Exception
//    {
//        Map<BigInteger, ItemHolder> rlt = new HashMap<BigInteger, ItemHolder>();
//        
//        Iterator<ItemHolder> it = fileItemMap.values().iterator();
//        
//        while (it.hasNext())
//        {
//            ItemHolder curObj = it.next();
//            
//            ItemHolder oldItem = itemService.selectItemByBuyerOidAndBuyerItemCode(buyerOid, curObj.getBuyerItemCode());
//            
//            if (null != oldItem)
//            {
//                curObj.setItemOid(oldItem.getItemOid());
//                rlt.put(oldItem.getItemOid(), curObj);
//                it.remove();
//            }
//            
//        }
//        
//        return rlt;
//    }
    
    
//    private MsgTransactionsExHolder initMsgTrans(String fileName, BuyerHolder buyer) throws Exception
//    {
//        MsgTransactionsExHolder msgTrans = new MsgTransactionsExHolder();
//        
//        msgTrans.setDocOid(oidService.getOid());
//        msgTrans.setActive(true);
//        msgTrans.setValid(true);
//        msgTrans.setMsgType(batchType);
//        msgTrans.setCreateDate(new Date());
//        msgTrans.setBuyerOid(buyer.getBuyerOid());
//        msgTrans.setBuyerCode(buyer.getBuyerCode());
//        msgTrans.setBuyerName(buyer.getBuyerName());
//        String spliteFileName = FileUtil.getInstance().trimAllExtension(fileName).split("_")[2];
//        msgTrans.setMsgRefNo(spliteFileName);
//        
//        return msgTrans;
//        
//    }
    
    
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
    
    
    private void sendEmail(BuyerMsgSettingHolder setting, File itemZipFile, Map<String, List<String>> errorMap)
    {
        if (null == setting.getRcpsAddrs() || setting.getRcpsAddrs().trim().isEmpty())
        {
            log.warn("Email recipient is not setup, email will not be sent out.");
            return;
        }
        
        Map<String, Object> param = new HashMap<String, Object>();
        String[] emailTo = setting.retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
        String subject = "Item Validate Report - " + itemZipFile.getName();
        param.put("BATCH_FILE_NAME", itemZipFile.getName());
        int size = 0;
        for (Map.Entry<String, List<String>> entry : errorMap.entrySet())
        {
           size = size + entry.getValue().size();
        }
        if (size > ERROR_LIMIT)
        {
            byte[] bytes = null;
            try
            {
                bytes = initErrorAttachedDocument(errorMap, subject);
            }
            catch(Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
            String[] filenames = new String[]{"Item File Batch Validation Report.txt"};
            param.put("ERROR_MSG", "There are " + size + " validation message(s) of Item File (" + itemZipFile.getName()+ "), please refer to the attachment.");
            emailEngine.sendEmailWithAttachedDocuments(emailTo, subject, "ALERT_FILE_ERROR.vm", param, filenames, new byte[][]{bytes});
        }
        else
        {
            param.put("ERROR_MAP", errorMap);
            emailEngine.sendHtmlEmail(emailTo, subject, "ALERT_FILE_ERROR.vm", param);
        }
    }
    
    
    private byte[] initErrorAttachedDocument(Map<String, List<String>> errorMsgs, String subject)
        throws Exception
    {
        StringBuffer contents = new StringBuffer();
        contents.append(subject).append(END_LINE).append(END_LINE)
                .append("The following document(s) has validation error list : ").append(END_LINE).append(END_LINE).append(END_LINE);
        int fileCount = 1;
        for (Map.Entry<String, List<String>> entry : errorMsgs.entrySet())
        {
            contents.append(fileCount+" .").append(entry.getKey()).append(END_LINE);
            
            for(String error : entry.getValue())
            {
                contents.append("    ").append(error).append(END_LINE);
            }
            contents.append(END_LINE);
            fileCount ++;
        }
        return contents.toString().getBytes("utf-8");
    }
    
    public static void main(String[] args)
    {
        Map<String, String> testMap = new HashMap<String, String>();
        for (int i = 0 ; i < 10 ; i++ )
        {
            testMap.put(i + "", i + "");
        }
        
        for (Iterator<Map.Entry<String,String>> itr = testMap.entrySet().iterator(); itr.hasNext();)
        {
            Map.Entry<String, String> entry = itr.next();
            if (entry.getValue().equalsIgnoreCase("3"))
            {
                itr.remove();
            }
        }
        
        System.out.println(testMap);
    }
}

