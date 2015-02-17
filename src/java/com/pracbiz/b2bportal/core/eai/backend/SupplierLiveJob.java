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
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.GroupTmpService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TransactionBatchService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;


/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class SupplierLiveJob extends BaseJob implements 
        CoreCommonConstants
{
    private static final String LINE_NO = "lineNo : ";
    private static final Logger log = LoggerFactory
            .getLogger(SupplierLiveJob.class);
    private static final String ID = "[SupplierLiveJob]";
    
    private StandardEmailSender standardEmailSender;
    private BuyerService buyerService;
    private OidService oidService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private EmailEngine emailEngine;
    private TransactionBatchService transactionBatchService;
    private MailBoxUtil mboxUtil;
    private SupplierService supplierService;
    private UserProfileTmpService userProfileTmpService;
    private RoleService roleService;
    private GroupTmpService groupTmpService;
    private MsgTransactionsService msgTransactionsService;
    
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        oidService = this.getBean("oidService", OidService.class);
        emailEngine = this.getBean("emailEngine", EmailEngine.class);
        transactionBatchService = this.getBean("transactionBatchService", TransactionBatchService.class);
        supplierService = this.getBean("supplierService", SupplierService.class);
        userProfileTmpService = this.getBean("userProfileTmpService", UserProfileTmpService.class);
        roleService = this.getBean("roleService", RoleService.class);
        groupTmpService = this.getBean("groupTmpService", GroupTmpService.class);
        msgTransactionsService = this.getBean("msgTransactionsService", MsgTransactionsService.class);
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
                    .matches("SL_" + buyer.getBuyerCode().toUpperCase() + "_[0-9]+.ZIP");
            }

        });
        
        if (stBatchFiles == null || stBatchFiles.length < 1)
        {
            log.info("No supplier live files found for buyer [" + buyer.getBuyerName() + "].");
            
            return;
        }
        
        stBatchFiles = FileUtil.getInstance().getStableFiles(stBatchFiles);
        
        if (stBatchFiles == null || stBatchFiles.length < 1)
        {
            log.info("No supplier live files found for buyer [" + buyer.getBuyerName() + "].");
            
            return;
        }

        List<File> stableFiles = Arrays.asList(stBatchFiles);
        
        for (File stZipFile : stableFiles)
        {
            try
            {
                processSlZipFile(buyer, stZipFile);
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
        }
    }
    
    
    private void processSlZipFile(BuyerHolder buyer, File stZipFile) throws IOException
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
            
            Map<String, List<String>> validateMap = new HashMap<String, List<String>>();
            List<String> successList = new ArrayList<String>();
            
            for (File stFile : extractedFiles)
            {
                List<String> validateResult = null;
                
                try
                {
                    validateResult = validateStFile(stFile);
                    
                    if (validateResult != null && !validateResult.isEmpty())
                    {
                        validateMap.put(stFile.getName(), validateResult);
                        continue;
                    }
                    
                    this.processStFile(buyer, transBatch, stFile);
                    
                    successList.add(stFile.getName());
                }
                catch(Exception e)
                {
                    validateResult = new ArrayList<String>();
                    validateResult.add("exception occurs");
                    validateMap.put(stFile.getName(), validateResult);
                    
                    String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                    standardEmailSender.sendStandardEmail(ID, tickNo, e);
                }
            }
            
            BuyerMsgSettingHolder setting = null;
            try
            {
                setting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), MsgType.SL.name());
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
            
            if (setting != null && setting.getRcpsAddrs() != null && !setting.getRcpsAddrs().trim().isEmpty())
            {
                String emailAddress = setting.getRcpsAddrs();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("BUYER_NAME", buyer.getBuyerName());
                map.put("BUYER_CODE", buyer.getBuyerCode());
                map.put("BATCH_FILE", stZipFile.getName());
                map.put("TOTAL_COUNT", extractedFiles.size());
                map.put("SUCCESS_LIST", successList);
                map.put("ERROR_MAP", validateMap);
                
                
                emailEngine.sendHtmlEmail(emailAddress.trim().split(","), "Supplier Live Upload Status - " + buyer.getBuyerCode(), "ALERT_BUYER_SL.vm", map);
            }
            
            if (successList.isEmpty())
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
    
    
    private void processStFile(BuyerHolder buyer, TransactionBatchHolder transBatch, File stFile) throws Exception
    {
        List<String> contents = FileUtils.readLines(stFile);
        
        MsgTransactionsHolder msg = initMsgTransactions(buyer, transBatch, stFile.getName());
        List<SupplierHolder> updateSupplierList = new ArrayList<SupplierHolder>();
        List<RoleUserTmpHolder> updateSupplierUserRoleList = new ArrayList<RoleUserTmpHolder>();
        List<RoleGroupTmpHolder> updateSupplierUserGroupRoleList = new ArrayList<RoleGroupTmpHolder>();
        
        for (int i = 1; i < contents.size(); i++)
        {
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
            
            String supplierCode = content[0];
            String adminRoleId = content[1].trim();
            String userRoleId = content[2].trim();
            
            SupplierHolder supplier = supplierService.selectSupplierByCode(supplierCode.trim());
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sdf.setLenient(false);
            Date liveDate = sdf.parse(content[3]);
            
            
            supplier.setLiveDate(liveDate);
            
            updateSupplierList.add(supplier);
            
            List<UserProfileTmpHolder> users = userProfileTmpService.selectUsersBySupplierOid(supplier.getSupplierOid());
            
            if (users == null || users.isEmpty())
            {
                continue;
            }
            
            RoleHolder adminRole = roleService.selectSupplierRoleById(supplier.getSupplierOid(), adminRoleId);
            
            if (adminRole == null)
            {
                adminRole = roleService.selectSupplierRoleById(BigDecimal.valueOf(-1), adminRoleId);
            }
            
            if (adminRole != null && adminRole.getUserTypeOid().equals(BigDecimal.valueOf(3)))
            {
                for (UserProfileTmpHolder user : users)
                {
                    if (BigDecimal.valueOf(3).equals(user.getUserType()) && !user.getCtrlStatus().equals(MkCtrlStatus.PENDING))
                    {
                        RoleUserTmpHolder roleUser = new RoleUserTmpHolder();
                        roleUser.setUserOid(user.getUserOid());
                        roleUser.setRoleOid(adminRole.getRoleOid());
                        updateSupplierUserRoleList.add(roleUser);
                    }
                }
                
                List<GroupTmpHolder> adminGroups = groupTmpService.selectGroupBySupplierOidAndUserTypeOid(supplier.getSupplierOid(), BigDecimal.valueOf(3));
                if (adminGroups != null && !adminGroups.isEmpty())
                {
                    for (GroupTmpHolder group : adminGroups)
                    {
                        if (group.getCtrlStatus().equals(MkCtrlStatus.PENDING))
                        {
                            continue;
                        }
                        RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
                        roleGroup.setGroupOid(group.getGroupOid());
                        roleGroup.setRoleOid(adminRole.getRoleOid());
                        updateSupplierUserGroupRoleList.add(roleGroup);
                    }
                }
            }
            
            RoleHolder userRole = roleService.selectSupplierRoleById(supplier.getSupplierOid(), userRoleId);
            
            if (userRole == null)
            {
                userRole = roleService.selectSupplierRoleById(BigDecimal.valueOf(-1), userRoleId);
            }
            
            if (userRole != null && userRole.getUserTypeOid().equals(BigDecimal.valueOf(5)))
            {
                for (UserProfileTmpHolder user : users)
                {
                    if (BigDecimal.valueOf(5).equals(user.getUserType()) && !user.getCtrlStatus().equals(MkCtrlStatus.PENDING))
                    {
                        RoleUserTmpHolder roleUser = new RoleUserTmpHolder();
                        roleUser.setUserOid(user.getUserOid());
                        roleUser.setRoleOid(userRole.getRoleOid());
                        updateSupplierUserRoleList.add(roleUser);
                    }
                }
                
                List<GroupTmpHolder> userGroups = groupTmpService.selectGroupBySupplierOidAndUserTypeOid(supplier.getSupplierOid(), BigDecimal.valueOf(5));
                if (userGroups != null && !userGroups.isEmpty())
                {
                    for (GroupTmpHolder group : userGroups)
                    {
                        if (group.getCtrlStatus().equals(MkCtrlStatus.PENDING))
                        {
                            continue;
                        }
                        RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
                        roleGroup.setGroupOid(group.getGroupOid());
                        roleGroup.setRoleOid(userRole.getRoleOid());
                        updateSupplierUserGroupRoleList.add(roleGroup);
                    }
                }
            }
            
        }
        
        msgTransactionsService.insertSupplierLiveDate(msg,updateSupplierList, updateSupplierUserRoleList, updateSupplierUserGroupRoleList);
    }
    
    
    private List<String> validateStFile(File stFile) throws Exception
    {
        List<String> result = new ArrayList<String>();
        List<String> contents = FileUtils.readLines(stFile);
        
        if (contents == null || contents.isEmpty())
        {
            result.add("Store Master file [" + stFile.getName() + "] is empty.");
            return result;
        }
        
        for (int i = 1; i < contents.size(); i++)
        {
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
            
            boolean flag = false;
            
            if (content == null || content.length != 4)
            {
                result.add(LINE_NO + index + "- Length must be 4");
                continue;
            }
            
            String supplierCode = content[0];
            
            SupplierHolder supplier = null;
            
            if (supplierCode == null || supplierCode.trim().isEmpty())
            {
                result.add(LINE_NO + index + "- Supplier Code is required.");
                flag = true;
            }
            else
            {
                supplier = supplierService.selectSupplierByCode(supplierCode.trim());

                if (supplier == null)
                {
                    result.add(LINE_NO + index + "- Supplier does not exist in System.");
                    flag = true;
                }
            }
            
            if (content[3] == null || content[3].trim().isEmpty())
            {
                result.add(LINE_NO + index + "- Live Date is required.");
                flag = true;
            }
            else
            {
                try
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    sdf.setLenient(false);
                    sdf.parse(content[3]);
                }
                catch (Exception e)
                {
                    result.add(LINE_NO + index + "- Live Date must be a date with format yyyy/MM/dd.");
                    flag = true;
                }
            }
            
            
            if (flag)
            {
                continue;
            }
        }
        
        return result;
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
