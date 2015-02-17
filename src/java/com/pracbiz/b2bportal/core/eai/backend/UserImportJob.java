package com.pracbiz.b2bportal.core.eai.backend;

import java.io.File;
import java.io.FileFilter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.ClassExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.ClassService;
import com.pracbiz.b2bportal.core.service.EmailSendService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.SubclassService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class UserImportJob extends BaseJob implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(UserImportJob.class);
    private static final String ID = "[UserImportJob]";
    private static final String LINE_NO = "lineNo : ";
    private static final String LEFT_SEPERATE = " [ ";
    private static final String RIGHT_SEPERATE = " ] ";
    private static final String SEPERATOR = "\\|";
    
    private BuyerService buyerService;
    private MailBoxUtil mboxUtil;
    private OidService oidService;
    private UserProfileService userProfileService;
    private UserProfileTmpService userProfileTmpService;
    private RoleService roleService;
    private GroupService groupService;
    private StandardEmailSender standardEmailSender;
    private BuyerStoreService buyerStoreService;
    private BuyerStoreAreaService buyerStoreAreaService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private EmailEngine emailEngine;
    private CustomAppConfigHelper appConfig;
    private EmailSendService emailSendService;
    private ClassService classService;
    private SubclassService subclassService;
    
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        oidService = this.getBean("oidService", OidService.class);
        userProfileService = this.getBean("userProfileService", UserProfileService.class);
        appConfig = this.getBean("appConfig", CustomAppConfigHelper.class);
        emailEngine = this.getBean("emailEngine", EmailEngine.class);
        buyerStoreService = this.getBean("buyerStoreService", BuyerStoreService.class);
        userProfileTmpService = this.getBean("userProfileTmpService", UserProfileTmpService.class);
        roleService = this.getBean("roleService", RoleService.class);
        groupService = this.getBean("groupService", GroupService.class);
        buyerStoreAreaService = this.getBean("buyerStoreAreaService", BuyerStoreAreaService.class);
        emailSendService = this.getBean("emailSendService", EmailSendService.class);
        classService = this.getBean("classService", ClassService.class);
        subclassService = this.getBean("subclassService", SubclassService.class);
        
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
            List<BuyerHolder> buyerList = buyerService.select(new BuyerHolder());
            if (buyerList == null || buyerList.isEmpty())
            {
                log.info("No buyer exist in system.");
                return;
            }
            
            for (final BuyerHolder buyer : buyerList)
            {
                if (buyer == null || !buyer.getActive())
                {
                    log.info("Buyer [" + buyer.getBuyerCode() + "] is not active, skip it ...");
                    
                    continue;
                }
                
                File outPath = new File(mboxUtil.getBuyerOutPath(buyer.getMboxId()));

                File[] detectedFiles = outPath.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File file)
                    {
                        return file.getName().toUpperCase(Locale.US)
                            .matches("UM_" + buyer.getBuyerCode().toUpperCase() + "_[0-9]+.ZIP");
                    }

                });
                
                if (null == detectedFiles || detectedFiles.length == 0)
                {
                    log.info(" No User Master Files found under [" + buyer.getBuyerCode() + "]'s out folder...");
                    
                    continue;
                }

                List<File> stableFiles = Arrays.asList(FileUtil.getInstance()
                    .getStableFiles(detectedFiles));
                
                if (null == stableFiles || stableFiles.isEmpty())
                {
                    log.info(" No User Master Files found under [" + buyer.getBuyerCode() + "]'s out folder...");
                    
                    continue;
                }
                
                Collections.sort(stableFiles, new Comparator<File>(){
                    @Override
                    public int compare(File o1, File o2) {
                        String o1Date = o1.getName().substring(o1.getName().lastIndexOf(CoreCommonConstants.DOC_FILENAME_DELIMITOR), 
                                o1.getName().lastIndexOf("."));
                        String o2Date = o2.getName().substring(o2.getName().lastIndexOf(CoreCommonConstants.DOC_FILENAME_DELIMITOR), 
                                o2.getName().lastIndexOf("."));
                        return o1Date.compareTo(o2Date);
                    }
                });
                
                BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(
                        buyer.getBuyerOid(), MsgType.UM.name());
    
                for (File batchFile : stableFiles)
                {
                    File workDir = new File(mboxUtil.getBuyerWorkingOutPath(buyer.getMboxId())+ PS
                        + batchFile.getName().substring(0,batchFile.getName().lastIndexOf(".")));
                    
                    FileUtil.getInstance().createDir(workDir);
                    FileUtil.getInstance().moveFile(batchFile, workDir.getPath());
                    File resultBatchFile = new File(workDir, batchFile.getName());
                    
                    try
                    {
                        GZIPHelper.getInstance().unZip(resultBatchFile, workDir.getPath());
                        List<File> extractedFiles = this.retrieveExtractedFiles(workDir,
                                batchFile.getName());
                        if (extractedFiles == null || extractedFiles.isEmpty())
                        {
                            continue;
                        }
                        
                        for (File docFile : extractedFiles)
                        {
                            List<String> newList = new ArrayList<String>();
                            List<String> updateList = new ArrayList<String>();
                            List<String> userTypeErrorList = new ArrayList<String>();
                            List<String> userNameErrorList = new ArrayList<String>();
                            List<String> loginIdErrorList = new ArrayList<String>();
                            List<String> loginIdDupList = new ArrayList<String>();
                            List<String> emailEmptyList = new ArrayList<String>();
                            List<String> emailInvalidList = new ArrayList<String>();
                            List<String> roleErrorList = new ArrayList<String>();
                            List<String> groupErrorList = new ArrayList<String>();
                            List<String> lengthErrorList = new ArrayList<String>();
                            List<String> userPendingList = new ArrayList<String>();
                            List<String> loginIdList = new ArrayList<String>();
                            List<UserProfileTmpExHolder> users = new ArrayList<UserProfileTmpExHolder>();
                            List<UserProfileTmpExHolder> updateUsers = new ArrayList<UserProfileTmpExHolder>();
                            
                            List<String> contents = FileUtils.readLines(docFile);
                            if (contents == null || contents.isEmpty())
                            {
                                continue;
                            }
                            TransactionBatchHolder transBatch = this.initTransBatch(
                                    FileUtil.getInstance().trimAllExtension(docFile.getName()), docFile.getName());

                            String msgRefNo = FileUtil.getInstance().trimAllExtension(docFile.getName()).split("_")[2];
                            MsgTransactionsHolder msg = this.initMsgTransactions(buyer, transBatch, msgRefNo, docFile.getName());
                            
                            for (int i = 1; i < contents.size(); i ++)
                            {
                                String obj = contents.get(i);
                                int index = i;
                                CSVReader reader = new CSVReader(new StringReader(contents.get(i)), ',', '\"');
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
                                if (content == null)
                                {
                                    continue;
                                }
                                
                                
                                if (content.length != 13)
                                {
                                    log.info("length of [" + obj + "] is not 12, skip");
                                    lengthErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                
                                UserProfileTmpExHolder user = new UserProfileTmpExHolder();
                                
                                BigDecimal userOid = oidService.getOid();
                                user.setUserOid(userOid);
                                
                                /*set user type*/
                                BigDecimal userType = initUserType(content[0]);
                                if (userType == null || !(userType.equals(BigDecimal.valueOf(2)) || userType.equals(BigDecimal.valueOf(4))
                                        || userType.equals(BigDecimal.valueOf(6)) || userType.equals(BigDecimal.valueOf(7))))
                                {
                                    log.info("user type of [" + obj + "] is not in (BA, BU, STA, STU, skip");
                                    userTypeErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                user.setUserType(userType);
                                
                                /*set user name*/
                                if (content[1].trim().isEmpty() || content[1].trim().length() > 50)
                                {
                                    log.info("user name of [" + obj + "] is not between 0 and 50, skip");
                                    userNameErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                user.setUserName(content[1]);
                                
                                /*set login id*/
                                if (content[2].trim().isEmpty() || content[2].trim().length() > 50)
                                {
                                    log.info("login id of [" + obj + "] is not between 0 and 50, skip");
                                    loginIdErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                
                                if (loginIdList.contains(content[2]))
                                {
                                    log.info("login id of [" + obj + "] is duplicate in file, skip");
                                    loginIdDupList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                else
                                {
                                    loginIdList.add(content[2]); 
                                }
                                user.setLoginId(content[2]);
                                
                                /*set email address*/
                                if (content[3].trim().isEmpty() || content[3].trim().length() > 100)
                                {
                                    log.info("email address of [" + obj + "] is not between 0 and 100, skip");
                                    emailEmptyList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                if (!content[3].trim().matches(appConfig.getEmailPattern()))
                                {
                                    log.info("email address of [" + obj + "] is not valid, skip");
                                    emailInvalidList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                user.setEmail(content[3].trim());
                                
                                /*set active*/
                                user.setActive(content[4].trim().equalsIgnoreCase("N") ? false : true);
                                
                                /*set blocked*/
                                user.setBlocked(content[5].trim().equalsIgnoreCase("Y") ? true : false);
                                
                                /*set role*/
                                List<RoleUserHolder> roleUsers = new ArrayList<RoleUserHolder>();
                                if (content[6].trim().isEmpty())
                                {
                                    log.info("role of [" + obj + "] is empty, skip");
                                    roleErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                
                                List<RoleHolder> roleList = roleService.selectBuyerRolesByBuyerOidAndUserType(buyer.getBuyerOid(), userType);
                                Map<String, RoleHolder> roleMap = convertToRoleMapUseRoleIdAsKey(roleList);
                                if (roleMap.isEmpty())
                                {
                                    log.info("no valid role for user type [" + userType +"] of buyer [" + buyer.getBuyerCode() + "], skip");
                                    roleErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                String[] roles = content[6].split(SEPERATOR);
                                for (String roleId : roles)
                                {
                                    if (roleMap.containsKey(roleId.trim().toUpperCase()))
                                    {
                                        RoleUserHolder roleUser = new RoleUserHolder();
                                        roleUser.setRoleOid(roleMap.get(roleId.trim().toUpperCase()).getRoleOid());
                                        roleUser.setUserOid(userOid);
                                        roleUsers.add(roleUser);
                                    }
                                }
                                if (roleUsers.isEmpty())
                                {
                                    log.info("neither one of roles of [" + obj + "] is valid, skip");
                                    roleErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    continue;
                                }
                                user.setRoleUsers(roleUsers);
                                
                                /*set group*/
                                if (!content[7].trim().isEmpty())
                                {
                                    List<GroupUserHolder> groupUsers = new ArrayList<GroupUserHolder>();
                                    List<GroupHolder> groups = groupService.selectGroupByBuyerOidAndUserTypeOid(buyer.getBuyerOid(), userType);
                                    Map<String, GroupHolder> groupMap = convertToGroupMapUseGroupIdAsKey(groups);
                                    if (!groupMap.containsKey(content[7].trim().toUpperCase()))
                                    {
                                        log.info("group of [" + obj + "] is not valid, skip");
                                        groupErrorList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                        continue;
                                    }
                                    GroupUserHolder groupUser = new GroupUserHolder();
                                    groupUser.setGroupOid(groupMap.get(content[7].trim().toUpperCase()).getGroupOid());
                                    groupUser.setUserOid(userOid);
                                    groupUsers.add(groupUser);
                                    user.setGroupUsers(groupUsers);
                                }
                                
                                /*set store*/
                                if (!content[8].trim().isEmpty())
                                {
                                    List<BuyerStoreUserHolder> buyerStoreUsers = new ArrayList<BuyerStoreUserHolder>();
                                    if (content[8].trim().equalsIgnoreCase("ALL"))
                                    {
                                        BuyerStoreUserHolder bsu = new BuyerStoreUserHolder();
                                        bsu.setStoreOid(BigDecimal.valueOf(-3));
                                        bsu.setUserOid(userOid);
                                        buyerStoreUsers.add(bsu);
                                    }
                                    else
                                    {
                                        List<BuyerStoreHolder> buyerStores = buyerStoreService.selectBuyerStoresByBuyer(buyer.getBuyerCode());
                                        Map<String, BuyerStoreHolder> storeMap = convertToStoreMapUseStoreCodeAsKey(buyerStores);
                                        String[] storeCodes = content[8].trim().split(SEPERATOR);
                                        for (String storeCode : storeCodes)
                                        {
                                            if (storeMap.containsKey(storeCode.trim().toUpperCase()))
                                            {
                                                BuyerStoreUserHolder bsu = new BuyerStoreUserHolder();
                                                bsu.setStoreOid(storeMap.get(storeCode.trim().toUpperCase()).getStoreOid());
                                                bsu.setUserOid(userOid);
                                                buyerStoreUsers.add(bsu);
                                            }
                                        }
                                    }
                                    user.setBuyerStoreUsers(buyerStoreUsers);
                                }
                                
                                /*set area*/
                                if (!content[9].trim().isEmpty())
                                {
                                    List<BuyerStoreAreaUserHolder> buyerStoreAreaUsers = new ArrayList<BuyerStoreAreaUserHolder>();
                                    if (content[9].trim().equalsIgnoreCase("ALL"))
                                    {
                                        BuyerStoreAreaUserHolder bsau = new BuyerStoreAreaUserHolder();
                                        bsau.setAreaOid(BigDecimal.valueOf(-2));
                                        bsau.setUserOid(userOid);
                                        buyerStoreAreaUsers.add(bsau);
                                    }
                                    else
                                    {
                                        List<BuyerStoreAreaHolder> buyerStoreAreas = buyerStoreAreaService.
                                                selectBuyerStoreAreaByBuyer(buyer.getBuyerCode());
                                        Map<String, BuyerStoreAreaHolder> areaMap = convertToAreaMapUseAreaCodeAsKey(buyerStoreAreas);
                                        String[] areaCodes = content[9].trim().split(SEPERATOR);
                                        for (String areaCode : areaCodes)
                                        {
                                            if (areaMap.containsKey(areaCode.trim().toUpperCase()))
                                            {
                                                BuyerStoreAreaUserHolder bsau = new BuyerStoreAreaUserHolder();
                                                bsau.setAreaOid(areaMap.get(areaCode.trim().toUpperCase()).getAreaOid());
                                                bsau.setUserOid(userOid);
                                                buyerStoreAreaUsers.add(bsau);
                                            }
                                        }
                                    }
                                    user.setBuyerStoreAreaUsers(buyerStoreAreaUsers);
                                }
                                
                                /*set warehouse*/
                                if (!content[10].trim().isEmpty())
                                {
                                    List<BuyerStoreUserHolder> buyerWarehouseUsers = new ArrayList<BuyerStoreUserHolder>();
                                    if (content[10].trim().equalsIgnoreCase("ALL"))
                                    {
                                        BuyerStoreUserHolder bsu = new BuyerStoreUserHolder();
                                        bsu.setStoreOid(BigDecimal.valueOf(-4));
                                        bsu.setUserOid(userOid);
                                        buyerWarehouseUsers.add(bsu);
                                    }
                                    else
                                    {
                                        List<BuyerStoreHolder> buyerStores = buyerStoreService.selectBuyerStoresByBuyer(buyer.getBuyerCode());
                                        Map<String, BuyerStoreHolder> storeMap = convertToStoreMapUseStoreCodeAsKey(buyerStores);
                                        String[] storeCodes = content[10].trim().split(SEPERATOR);
                                        for (String storeCode : storeCodes)
                                        {
                                            if (storeMap.containsKey(storeCode.trim().toUpperCase()))
                                            {
                                                BuyerStoreUserHolder bsu = new BuyerStoreUserHolder();
                                                bsu.setStoreOid(storeMap.get(storeCode.trim().toUpperCase()).getStoreOid());
                                                bsu.setUserOid(userOid);
                                                buyerWarehouseUsers.add(bsu);
                                            }
                                        }
                                    }
                                    user.setBuyerWareHouseUsers(buyerWarehouseUsers);
                                }
                                
                                /*set grant buyer*/
                                if (!content[11].trim().isEmpty())
                                {
                                    List<AllowedAccessCompanyHolder> allowedBuyerList = new ArrayList<AllowedAccessCompanyHolder>();
                                    Map<String, BuyerHolder> buyerCodeMap = convertTpBuyerMapUserBuyerCodeAsKey(buyerList);
                                    String[] buyerCodes = content[11].trim().split(SEPERATOR);
                                    for (String buyerCode : buyerCodes)
                                    {
                                        if (buyerCodeMap.containsKey(buyerCode.trim().toUpperCase()) && 
                                                !buyer.getBuyerCode().equalsIgnoreCase(buyerCode.trim()))
                                        {
                                            allowedBuyerList.add(new AllowedAccessCompanyHolder(user.getUserOid(), 
                                                    buyerCodeMap.get(buyerCode.toUpperCase(Locale.US)).getBuyerOid()));
                                        }
                                    }
                                    user.setAllowedBuyerList(allowedBuyerList);
                                }
                                
                                /*set class and subclass*/
                                if (!content[12].trim().isEmpty())
                                {
                                    List<AllowedAccessCompanyHolder> allowedBuyerList = new ArrayList<AllowedAccessCompanyHolder>();
                                    allowedBuyerList.addAll(user.getAllowedBuyerList());
                                    allowedBuyerList.add(new AllowedAccessCompanyHolder(userOid, buyer.getBuyerOid()));
                                    Map<String, BuyerHolder> buyerCodeMap = convertTpBuyerMapUserBuyerCodeAsKey(buyerList);
                                    
                                    String[] buyerStrs = content[12].split("\\}\\|");
                                    for (String buyerStr : buyerStrs)
                                    {
                                        String buyerCode = buyerStr.split("\\{")[0];
                                        
                                        BuyerHolder accessBuyer = buyerCodeMap.get(buyerCode);
                                        
                                        if (accessBuyer == null)
                                        {
                                            log.info("Assigned buyer [" + buyerCode + "] does not exist in system for user " + user.getLoginId() + ".");
                                            continue;
                                        }
                                        
                                        boolean available = false;
                                        for (AllowedAccessCompanyHolder allowedCompany : allowedBuyerList)
                                        {
                                            if (allowedCompany.getCompanyOid().equals(accessBuyer.getBuyerOid()))
                                            {
                                                available = true;
                                                break;
                                            }
                                        }
                                        
                                        if (!available)
                                        {
                                            continue;
                                        }
                                        
                                        if (buyerStr.indexOf("{") == -1)
                                        { 
                                            continue;
                                        }
                                        
                                        int lastIndex = buyerStr.lastIndexOf("}");
                                        String classStrs = buyerStr.substring(buyerStr.indexOf("{") + 1, lastIndex == -1 ? buyerStr.length() : lastIndex);
                                        if ("ALL".equals(classStrs.trim().toUpperCase()))
                                        {
                                            List<ClassExHolder> allClass = classService.selectClassByBuyerOid(accessBuyer.getBuyerOid());
                                            if (allClass == null || allClass.isEmpty())
                                            {
                                                log.info("Assigned buyer [" + buyerCode + "] has no class in system for user " + user.getLoginId() + ".");
                                                continue;
                                            }
                                            
                                            for (ClassExHolder _class : allClass)
                                            {
                                                UserClassHolder userClass = new UserClassHolder(userOid, _class.getClassOid());
                                                if(user.getUserClassList() == null || !user.getUserClassList().contains(userClass))
                                                {
                                                    user.addUserClass(userClass);
                                                }
                                            }
                                            
                                            continue;
                                        }
                                        
                                        for (String classStr : classStrs.split("\\|"))
                                        {
                                            if (classStr == null || classStr.isEmpty())
                                            {
                                                continue;
                                            }
                                            String[] classStrArray = classStr.split("\\(");
                                            String classCode = classStrArray[0];
                                            ClassHolder _class = classService.selectByBuyerOidAndClassCode(accessBuyer.getBuyerOid(), classCode);
                                            if (_class == null)
                                            {
                                                continue;
                                            }
                                            
                                            if (classStrArray.length == 1 || classStr.lastIndexOf(")") == -1 || classStr.indexOf("(") > classStr.lastIndexOf(")"))
                                            {
                                                UserClassHolder userClass = new UserClassHolder(userOid, _class.getClassOid());
                                                if(user.getUserClassList() == null || !user.getUserClassList().contains(userClass))
                                                {
                                                    user.addUserClass(userClass);
                                                }
                                                continue;
                                            }
                                            
                                            String subclassCodes = classStr.substring(classStr.indexOf("(") + 1, classStr.lastIndexOf(")"));
                                            if (subclassCodes == null || subclassCodes.isEmpty())
                                            {
                                                UserClassHolder userClass = new UserClassHolder(userOid, _class.getClassOid());
                                                if(user.getUserClassList() == null || !user.getUserClassList().contains(userClass))
                                                {
                                                    user.addUserClass(userClass);
                                                }
                                                continue;
                                            }
                                            
                                            for (String subclassCode : subclassCodes.split("\\/"))
                                            {
                                                SubclassHolder subclass = subclassService.selectByClassOidSubclassCode(_class.getClassOid(), subclassCode);
                                                if (subclass == null)
                                                {
                                                    continue;
                                                }
                                                UserSubclassHolder userSubclass = new UserSubclassHolder(userOid, subclass.getSubclassOid());
                                                if (user.getUserSubclassList() == null || !user.getUserSubclassList().contains(userSubclass))
                                                {
                                                    user.addUserSubclass(userSubclass);
                                                }
                                            }
                                        }
                                    }
                                }
                                
                                /*set other fields*/
                                user.setBuyerOid(buyer.getBuyerOid());
                                
                                //user.setLoginPwd(radmonPassword(6));
                                
                                UserProfileTmpHolder existUser = userProfileTmpService.selectUserProfileTmpByLoginId(user.getLoginId());
                                if (existUser == null)
                                {
                                    user.setLoginMode("PASSWORD");
                                    user.setGender("M");
                                    user.setInit(false);
                                    user.setCreateDate(new Date());
                                    user.setCreateBy(CoreCommonConstants.SYSTEM);
                                    users.add(user);
                                    newList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                }
                                else
                                {
                                    if (MkCtrlStatus.PENDING.equals(existUser.getCtrlStatus()))
                                    {
                                        userPendingList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                        continue;
                                    }
                                    user.setUpdateDate(new Date());
                                    user.setUpdateBy(CoreCommonConstants.SYSTEM);
                                    user.setUserOid(existUser.getUserOid());
                                    updateUsers.add(user);
                                    updateList.add(LINE_NO + index + LEFT_SEPERATE + obj + RIGHT_SEPERATE);
                                    
                                }
                            }
                            
                            if (!users.isEmpty() || !updateUsers.isEmpty())
                            {
                                userProfileService.importUsers(users, updateUsers, msg, transBatch);
                                
                                String requestUrl =  appConfig.getServerUrl() + "/user/";
                                String clientIp = InetAddress.getLocalHost().getHostAddress();
                                emailSendService.setNoAudit(true);
                                List<UserProfileTmpHolder> userTmpList = new ArrayList<UserProfileTmpHolder>();
                                for (UserProfileTmpExHolder user : users)
                                {
                                    userTmpList.add(user);
                                }
                                emailSendService.sendSetPasswordEmailByCallable(null, requestUrl, clientIp, userTmpList);
                            }
                            
                            
                            if (null == setting || null == setting.getRcpsAddrs()
                                    || setting.getRcpsAddrs().trim().isEmpty())
                            {
                                continue;
                            }
                            
                            int errorCount = lengthErrorList.size() + userTypeErrorList.size() + userNameErrorList.size() 
                                + loginIdErrorList.size() + loginIdDupList.size() + emailEmptyList.size() 
                                + emailInvalidList.size() + roleErrorList.size() + groupErrorList.size() + userPendingList.size();
                            
                            if (errorCount != 0 || (errorCount == 0 && !setting.getExcludeSucc()))
                            {
                                String[] emailTo = setting.retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
                                String subject = "User Master Status - " + batchFile.getName();
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("BUYER_NAME", buyer.getBuyerName());
                                map.put("BUYER_CODE", buyer.getBuyerCode());
                                map.put("USER_FILE_NAME", batchFile.getName());
                                map.put("NEW_LIST", newList);
                                map.put("UPDATE_LIST", updateList);
                                map.put("LENGTH_ERROR_LIST", lengthErrorList);
                                map.put("USER_TYPE_ERROR_LIST", userTypeErrorList);
                                map.put("USER_NAME_ERROR_LIST", userNameErrorList);
                                map.put("LOGIN_ID_ERROR_LIST", loginIdErrorList);
                                map.put("LOGIN_ID_DUPLICATE_LIST", loginIdDupList);
                                map.put("EMAIL_EMPTY_LIST", emailEmptyList);
                                map.put("EMAIL_INVALID_LIST", emailInvalidList);
                                map.put("ROLE_ERROR_LIST", roleErrorList);
                                map.put("GROUP_ERROR_LIST", groupErrorList);
                                map.put("USER_PENDING_LIST", userPendingList);
                                map.put("UNPROCESS_COUNT", errorCount);
                                /*byte[] content = generateReportExcel(allUsers, transBatch.getBatchNo());
                                if (content != null && content.length > 0)
                                {
                                    emailEngine.sendEmailWithAttachedDocuments(emailTo, subject, "ALERT_BUYER_USER_MASTER.vm", map, 
                                            new String[]{"user master import report_" + transBatch.getBatchNo()+ ".xls"}, 
                                            new byte[][]{content});
                                }
                                else
                                {
                                    emailEngine.sendHtmlEmail(emailTo, subject, "ALERT_BUYER_USER_MASTER.vm", map);
                                }*/
                                emailEngine.sendHtmlEmail(emailTo, subject, "ALERT_BUYER_USER_MASTER.vm", map);
                            }
                            
                        }
                        
                        File archPath = new File(mboxUtil.getFolderInBuyerArchOutPath(buyer.getMboxId(), 
                                DateUtil.getInstance().getYearAndMonth(new Date())));
    
                        if (!archPath.isDirectory())
                        {
                            FileUtil.getInstance().createDir(archPath);
                        }
                        
                        FileUtil.getInstance().copyFile(resultBatchFile, new File(archPath,resultBatchFile.getName()), true);
                        FileUtil.getInstance().deleleAllFile(workDir);
                        
                    }
                    catch (Exception e)
                    {
                        File invalidPath = new File(mboxUtil
                            .getFolderInBuyerInvalidPath(buyer.getMboxId(), DateUtil
                                .getInstance().getYearAndMonth(new Date())));
    
                        if (!invalidPath.isDirectory())
                        {
                            FileUtil.getInstance().createDir(invalidPath);
                        }
                        
                        if (resultBatchFile.exists())
                        {
                            FileUtil.getInstance().copyFile(resultBatchFile, new File(invalidPath,resultBatchFile.getName()), true);
                        }
                        if (workDir.exists())
                        {
                            FileUtil.getInstance().deleleAllFile(workDir);
                        }
                        String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                        standardEmailSender.sendStandardEmail(ID, tickNo, e);
                    }
                }
            }
            log.info(":::: process successfully.");
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
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
    
    
    private MsgTransactionsHolder initMsgTransactions(BuyerHolder biz,
            TransactionBatchHolder transBatch, String msgRefNo, String originalFilename)
            throws Exception
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();

        msg.setDocOid(oidService.getOid());
        msg.setBatchOid(transBatch.getBatchOid());
        msg.setMsgType(MsgType.ST.name());
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
    
    
    private BigDecimal initUserType(String userType)
    {
        if ("BA".equalsIgnoreCase(userType))
        {
            return BigDecimal.valueOf(2);
        }
        if ("BU".equalsIgnoreCase(userType))
        {
            return BigDecimal.valueOf(4);
        }
        if ("STA".equalsIgnoreCase(userType))
        {
            return BigDecimal.valueOf(6);
        }
        if ("STU".equalsIgnoreCase(userType))
        {
            return BigDecimal.valueOf(7);
        }
        return null;
    }
    
    
    /*private String initUserTypeDesc(BigDecimal userType)
    {
        if (BigDecimal.valueOf(2).equals(userType))
        {
            return "BuyerAdmin";
        }
        if (BigDecimal.valueOf(4).equals(userType))
        {
            return "BuyerUser";
        }
        if (BigDecimal.valueOf(6).equals(userType))
        {
            return "StoreAdmin";
        }
        if (BigDecimal.valueOf(7).equals(userType))
        {
            return "StoreUser";
        }
        return "";
    }*/
    
    
    private Map<String, RoleHolder> convertToRoleMapUseRoleIdAsKey(List<RoleHolder> roles)
    { 
        Map<String, RoleHolder> map = new HashMap<String, RoleHolder>();
        if (roles == null || roles.isEmpty())
        {
            return map;
        }
        for (RoleHolder role : roles)
        {
            map.put(role.getRoleId().toUpperCase(), role);
        }
        return map;
    }
    
    
    private Map<String, GroupHolder> convertToGroupMapUseGroupIdAsKey(List<GroupHolder> groups)
    { 
        Map<String, GroupHolder> map = new HashMap<String, GroupHolder>();
        if (groups == null || groups.isEmpty())
        {
            return map;
        }
        for (GroupHolder group : groups)
        {
            map.put(group.getGroupId().toUpperCase(), group);
        }
        return map;
    }
    
    
    private Map<String, BuyerStoreHolder> convertToStoreMapUseStoreCodeAsKey(List<BuyerStoreHolder> buyerStores)
    {
        Map<String, BuyerStoreHolder> map = new HashMap<String, BuyerStoreHolder>();
        if (buyerStores == null || buyerStores.isEmpty())
        {
            return map;
        }
        for (BuyerStoreHolder buyerStore : buyerStores)
        {
            map.put(buyerStore.getStoreCode().toUpperCase(), buyerStore);
        }
        return map;
    }
    
    
    private Map<String, BuyerStoreAreaHolder> convertToAreaMapUseAreaCodeAsKey(List<BuyerStoreAreaHolder> buyerStoreAreas)
    {
        Map<String, BuyerStoreAreaHolder> map = new HashMap<String, BuyerStoreAreaHolder>();
        if (buyerStoreAreas == null || buyerStoreAreas.isEmpty())
        {
            return map;
        }
        for (BuyerStoreAreaHolder buyerStoreArea : buyerStoreAreas)
        {
            map.put(buyerStoreArea.getAreaCode().toUpperCase(), buyerStoreArea);
        }
        return map;
    }
    
    
    private Map<String, BuyerHolder> convertTpBuyerMapUserBuyerCodeAsKey(List<BuyerHolder> buyerList)
    {
        Map<String, BuyerHolder> map = new HashMap<String, BuyerHolder>();
        for (BuyerHolder buyer : buyerList)
        {
            map.put(buyer.getBuyerCode().toUpperCase(), buyer);
        }
        return map;
    }
    
    
    /*private byte[] generateReportExcel(List<UserProfileHolder> users, String batchNo) throws Exception
    {
        if (users == null || users.isEmpty())
        {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        WritableSheet ws = wwb.createSheet("User Master Import Report-" + batchNo, 0);
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.CENTRE);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 11));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.LEFT);
        ws.addCell(new Label(0, 0, "User Type", format1));
        ws.mergeCells(0, 0, 1, 0);
        ws.addCell(new Label(2, 0, "User Name", format1));
        ws.mergeCells(2, 0, 3, 0);
        ws.addCell(new Label(4, 0, "Login Id", format1));
        ws.mergeCells(4, 0, 5, 0);
        ws.addCell(new Label(6, 0, "Password", format1));
        ws.mergeCells(6, 0, 7, 0);
        int row = 1;
        for (UserProfileHolder user : users)
        {
            ws.addCell(new Label(0, row, initUserTypeDesc(user.getUserType()), format2));
            ws.mergeCells(0, row, 1, row);
            ws.addCell(new Label(2, row, user.getUserName(), format2));
            ws.mergeCells(2, row, 3, row);
            ws.addCell(new Label(4, row, user.getLoginId(), format2));
            ws.mergeCells(4, row, 5, row);
            ws.addCell(new Label(6, row, user.getLoginPwd(), format2));
            ws.mergeCells(6, row, 7, row);
            row++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }*/
    
    
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

}
