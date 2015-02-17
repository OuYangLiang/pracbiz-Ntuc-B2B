package com.pracbiz.b2bportal.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.holder.BuyerGivenSupplierOperationHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.BuyerOperationHolder;
import com.pracbiz.b2bportal.core.holder.BuyerRuleHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.OperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerMsgSettingExHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerMsgSettingReportExHolder;
import com.pracbiz.b2bportal.core.holder.extension.RoleTmpExHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerGivenSupplierOperationMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerOperationMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerRuleMapper;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;
import com.pracbiz.b2bportal.core.service.GroupTmpService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.OperationService;
import com.pracbiz.b2bportal.core.service.RoleGroupService;
import com.pracbiz.b2bportal.core.service.RoleOperationService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.RoleUserService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public class BuyerServiceImpl extends DBActionServiceDefaultImpl<BuyerHolder> implements
        BuyerService, ApplicationContextAware, CoreCommonConstants
{  
    private static final String WORKING = "working";

    private static final String ARCHIVE = "archive";

    private static final String OUT = "out";

    private static final String EAI = "eai";

    private ApplicationContext ctx;
    
    @Autowired
    private BuyerMapper mapper;
    @Autowired
    private BuyerOperationMapper buyerOperationMapper;
    @Autowired
    private BuyerGivenSupplierOperationMapper buyerGivenSupplierOperationMapper;
    @Autowired
    private BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired
    private TradingPartnerService tradingPartnerService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileTmpService userProfileTmpService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupTmpService groupTmpService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleTmpService roleTmpService;
    @Autowired
    private RoleOperationService roleOperationService;
    @Autowired
    private RoleGroupService roleGroupService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private GroupTradingPartnerService groupTradingPartnerService;
    @Autowired
    private GroupSupplierService groupSupplierService;
    @Autowired
    private MailBoxUtil mboxUtil;
    @Autowired
    private transient OperationService operationService;
    @Autowired
    private BuyerRuleMapper buyerRuleMapper;
    @Autowired
    protected AllowedAccessCompanyService allowedAccessCompanyService;
    @Autowired
    private SupplierService supplierService;


    @Override
    public List<BuyerHolder> select(BuyerHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<BuyerHolder> selectWithBLOBs(BuyerHolder param) throws Exception
    {
        return mapper.selectWithBLOBs(param);
    }


    @Override
    public void insert(BuyerHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(BuyerHolder oldObj_,
        BuyerHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKeyWithBLOBs(BuyerHolder oldObj_,
        BuyerHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeyWithBLOBs(newObj_);
    }


    @Override
    public void updateByPrimaryKey(BuyerHolder oldObj_, BuyerHolder newObj_)
        throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    
    @Override
    public void delete(BuyerHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public int getCountOfSummary(BuyerHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<BuyerHolder> getListOfSummary(BuyerHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public BuyerHolder selectBuyerByKey(BigDecimal buyerOid) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }

        BuyerHolder parameter = new BuyerHolder();
        parameter.setBuyerOid(buyerOid);

        List<BuyerHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }

        return null;
    }

    
    @Override
    public BuyerHolder selectBuyerWithBlobsByKey(BigDecimal buyerOid)
        throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }

        BuyerHolder parameter = new BuyerHolder();
        parameter.setBuyerOid(buyerOid);

        List<BuyerHolder> rlts = mapper.selectWithBLOBs(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }

        return null;
    }


    @Override
    public void deleteBuyer(CommonParameterHolder cp, BigDecimal buyerOid)
        throws Exception
    {

        //delete group_user,group_supplier,group
        GroupTmpHolder group = new GroupTmpHolder();
        group.setBuyerOid(buyerOid);
        List<GroupTmpHolder> groupTmpList = groupTmpService.select(group);
        if(groupTmpList != null)
        {
            Iterator<GroupTmpHolder> it = groupTmpList.iterator();
            while(it.hasNext())
            {
                GroupTmpHolder oldGroup = it.next();
                List<RoleGroupHolder> roleGroups = roleGroupService.selectRoleGroupByGroupOid(oldGroup.getGroupOid());
                List<GroupUserHolder> groupUsers = groupUserService.selectGroupUserByGroupOid(oldGroup.getGroupOid());
                List<GroupTradingPartnerHolder> groupTradingPartners = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(oldGroup.getGroupOid());
                List<GroupSupplierHolder> groupSuppliers = groupSupplierService.selectGroupSupplierByGroupOid(oldGroup.getGroupOid());
                oldGroup.setRoleGroups(roleGroups);
                oldGroup.setGroupUsers(groupUsers);
                oldGroup.setGroupTradingPartners(groupTradingPartners);
                oldGroup.setGroupSuppliers(groupSuppliers);
                groupService.removeGroupProfile(cp, oldGroup);
            }
        }
        List<GroupHolder> groupList = groupService.select(group);
        if(groupList != null)
        {
            Iterator<GroupHolder> it = groupList.iterator();
            while(it.hasNext())
            {
                GroupTmpHolder oldGroup = new GroupTmpHolder();
                BeanUtils.copyProperties(it.next(), oldGroup);
                List<RoleGroupHolder> roleGroups = roleGroupService.selectRoleGroupByGroupOid(oldGroup.getGroupOid());
                List<GroupUserHolder> groupUsers = groupUserService.selectGroupUserByGroupOid(oldGroup.getGroupOid());
                List<GroupTradingPartnerHolder> groupTradingPartners = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(oldGroup.getGroupOid());
                List<GroupSupplierHolder> groupSuppliers = groupSupplierService.selectGroupSupplierByGroupOid(oldGroup.getGroupOid());
                oldGroup.setRoleGroups(roleGroups);
                oldGroup.setGroupUsers(groupUsers);
                oldGroup.setGroupTradingPartners(groupTradingPartners);
                oldGroup.setGroupSuppliers(groupSuppliers);
                groupService.removeGroupProfile(cp, oldGroup);
            }
        }
        
        //delete role_operation,role_user,role_group,role
        RoleTmpExHolder role = new RoleTmpExHolder();
        role.setBuyerOid(buyerOid);
        List<RoleTmpHolder> roleTmpList = roleTmpService.select(role);
        if(roleTmpList != null)
        {
            Iterator<RoleTmpHolder> it = roleTmpList.iterator();
            while(it.hasNext())
            {
                RoleTmpHolder oldRole = it.next();
                List<RoleOperationHolder> roleOperations = roleOperationService.selectByRole(oldRole.getRoleOid());
                oldRole.setRoleOperations(roleOperations);
                roleService.removeRoleProfile(cp, oldRole);
            }
        }
        List<RoleTmpHolder> roleList = roleTmpService.select(role);
        if(roleList != null)
        {
            Iterator<RoleTmpHolder> it = roleList.iterator();
            while(it.hasNext())
            {
                RoleTmpHolder oldRole = new RoleTmpHolder();
                BeanUtils.copyProperties(it.next(), oldRole);
                List<RoleOperationHolder> roleOperations = roleOperationService.selectByRole(oldRole.getRoleOid());
                oldRole.setRoleOperations(roleOperations);
                roleService.removeRoleProfile(cp, oldRole);
            }
        }
        
        //delete group tradingPartner in supplier group,tradingPartner
        TradingPartnerExHolder tradingPartner = new TradingPartnerExHolder();
        tradingPartner.setBuyerOid(buyerOid);
        List<TradingPartnerHolder> tradingPartnerList =  tradingPartnerService.select(tradingPartner);
        if(tradingPartnerList != null)
        {
            Iterator<TradingPartnerHolder> it = tradingPartnerList.iterator();
            while(it.hasNext())
            {
                TradingPartnerHolder oldTradingPartner = it.next();
                List<GroupTradingPartnerHolder> groupTradingParnters = groupTradingPartnerService.selectGroupTradingPartnerByTradingPartnerOid(oldTradingPartner.getTradingPartnerOid());
                oldTradingPartner.setGroupTradingPartners(groupTradingParnters);
                tradingPartnerService.removeTradingPartner(cp, oldTradingPartner);
            }
        }
        
        //delete user
        UserProfileTmpExHolder userProfile = new UserProfileTmpExHolder();
        userProfile.setBuyerOid(buyerOid);
        List<UserProfileTmpHolder> userProfileTmpList = userProfileTmpService.select(userProfile);
        if(userProfileTmpList != null)
        {
            Iterator<UserProfileTmpHolder> it = userProfileTmpList.iterator();
            while(it.hasNext())
            {
                UserProfileTmpHolder oldUserProfile = it.next();
                List<RoleUserHolder> roleUsers = roleUserService.selectRoleUserByUserOid(oldUserProfile.getUserOid());
                oldUserProfile.setRoleUsers(roleUsers);
                userProfileService.removeUserProfile(cp, oldUserProfile, true);
            }
        }
        List<UserProfileHolder> userProfileList = userProfileService.select(userProfile);
        if(userProfileList != null)
        {
            Iterator<UserProfileHolder> it = userProfileList.iterator();
            while(it.hasNext())
            {
                UserProfileTmpHolder oldUserProfile = new UserProfileTmpHolder();
                BeanUtils.copyProperties(it.next(), oldUserProfile);
                List<RoleUserHolder> roleUsers = roleUserService.selectRoleUserByUserOid(oldUserProfile.getUserOid());
                oldUserProfile.setRoleUsers(roleUsers);
                userProfileService.removeUserProfile(cp, oldUserProfile, true);
            }
        }
        
        //delete msg setting
        BuyerHolder buyer = this.selectBuyerByKey(buyerOid);
        List<BuyerMsgSettingHolder> buyerMsgSettingList = buyerMsgSettingService.selectBuyerMsgSettingsByBuyerOid(buyerOid);
        if(buyerMsgSettingList != null && !buyerMsgSettingList.isEmpty())
        {
            Iterator<BuyerMsgSettingHolder> it = buyerMsgSettingList.iterator();
            while(it.hasNext())
            {
                BuyerMsgSettingHolder obj = it.next();
                buyer.addBuyerMsgSetting(obj);
            }
        }
        BuyerMsgSettingHolder buyerMsg = new BuyerMsgSettingHolder();
        buyerMsg.setBuyerOid(buyerOid);
        buyerMsgSettingService.delete(buyerMsg);
        
        // delete buyer operations and buyer given supplier operations, no need to do audit.
        BuyerOperationHolder bo = new BuyerOperationHolder();
        bo.setBuyerOid(buyerOid);
        BuyerGivenSupplierOperationHolder bgso = new BuyerGivenSupplierOperationHolder();
        bgso.setBuyerOid(buyerOid);
        buyerOperationMapper.delete(bo);
        buyerGivenSupplierOperationMapper.delete(bgso);
        
        //delete business rule
        BuyerRuleHolder br = new BuyerRuleHolder();
        br.setBuyerOid(buyerOid);
        buyerRuleMapper.delete(br);
        
        //delete  buyer profile.
        this.getMeBean().auditDelete(cp, buyer);
        
        //delete mailbox
        //String filePath = appConfig.getBuyerMailboxRootPath()+ File.separator + buyer.getMboxId();
        String filePath = mboxUtil.getBuyerMailBox(buyer.getMboxId());
        FileUtil.getInstance().deleleAllFile(new File(filePath));
    }
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private BuyerService getMeBean()
    {
        return ctx.getBean("buyerService", BuyerService.class);
    }


    @Override
    public void insertBuyerWithMsgSetting(CommonParameterHolder cp,
            BuyerHolder buyer) throws Exception
    {
       this.getMeBean().auditInsert(cp, buyer);
       if(null != buyer.getMsgSetting())
       {
           for(BuyerMsgSettingHolder buyerMsg : buyer.getMsgSetting())
           {
               buyerMsgSettingService.insert(buyerMsg);
           }
       }
       
       this.initBuyerOperations(buyer);
       this.initBuyerGivenSupplierOperations(buyer);
       if (null != buyer.getBuyerOperations())
       {
           for (BuyerOperationHolder bo : buyer.getBuyerOperations())
           {
               buyerOperationMapper.insert(bo);
           }
       }
       
       if (null != buyer.getBuyerGivenSupplierOperations())
       {
           for (BuyerGivenSupplierOperationHolder bo : buyer.getBuyerGivenSupplierOperations())
           {
               buyerGivenSupplierOperationMapper.insert(bo);
           }
       }
       
       createMboxFolder(buyer);
    }


    @Override
    public void updateBuyerWithMsgSetting(CommonParameterHolder cp,
            BuyerHolder oldBuyer, BuyerHolder newBuyer) throws Exception
    {
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldBuyer, newBuyer);
        List<BuyerMsgSettingHolder> newBuyerMsgSettings = newBuyer.getMsgSetting();
        
        BuyerMsgSettingReportHolder reportParam = new BuyerMsgSettingReportHolder();
        reportParam.setBuyerOid(oldBuyer.getBuyerOid());
        buyerMsgSettingReportService.delete(reportParam);
        
        BuyerMsgSettingHolder param = new BuyerMsgSettingHolder();
        param.setBuyerOid(oldBuyer.getBuyerOid());
        buyerMsgSettingService.delete(param);
        
        if (newBuyerMsgSettings == null || newBuyerMsgSettings.isEmpty())
        {
            return;
        }
        
        for (BuyerMsgSettingHolder buyerMsg : newBuyerMsgSettings)
        {
            buyerMsgSettingService.insert(buyerMsg);
            
            Map<String, BuyerMsgSettingReportExHolder> subTypeReportMap = ((BuyerMsgSettingExHolder)buyerMsg).getSubTypeReportMap();
            if (subTypeReportMap == null || subTypeReportMap.isEmpty())
            {
                continue;
            }
            
            for (Map.Entry<String, BuyerMsgSettingReportExHolder> entry : subTypeReportMap.entrySet())
            {
                BuyerMsgSettingReportExHolder subTypeReport  = entry.getValue();
                buyerMsgSettingReportService.insert(subTypeReport);
            }
        }
        
        List<BuyerRuleHolder> buyerRules = newBuyer.getBuyerRules();
        BuyerRuleHolder ruleParam = new BuyerRuleHolder();
        ruleParam.setBuyerOid(newBuyer.getBuyerOid());
        buyerRuleMapper.delete(ruleParam);
        if(buyerRules == null || buyerRules.isEmpty())
        {
            return;
        }
        Iterator<BuyerRuleHolder> it = buyerRules.iterator();
        while(it.hasNext())
        {
            ruleParam = it.next();
            buyerRuleMapper.insert(ruleParam);
        }
    }
    

    @Override
    public List<BuyerHolder> selectActiveBuyers() throws Exception
    {
        BuyerHolder param = new BuyerHolder();
        param.setActive(true);
        
        return mapper.select(param);
    }


    @Override
    public BuyerHolder selectBuyerByBuyerCode(String buyerCode)
            throws Exception
    {
        if(buyerCode == null)
        {
            throw new IllegalArgumentException();
        }
        
        BuyerHolder buyer = new BuyerHolder();
        buyer.setBuyerCode(buyerCode);
        List<BuyerHolder> buyerList = this.select(buyer);
        if(buyerList != null && !buyerList.isEmpty())
        {
            return buyerList.get(0);
        }
        return null;
    }
    
    
    @Override
    public BuyerHolder selectBuyerByBuyerCodeWithBLOBs(String buyerCode)
            throws Exception
    {
        if(buyerCode == null)
        {
            throw new IllegalArgumentException();
        }
        
        BuyerHolder buyer = new BuyerHolder();
        buyer.setBuyerCode(buyerCode);
        List<BuyerHolder> buyerList = this.selectWithBLOBs(buyer);
        if(buyerList != null && !buyerList.isEmpty())
        {
            return buyerList.get(0);
        }
        return null;
    }


    @Override
    public BuyerHolder selectBuyerByMboxId(String mboxId) throws Exception
    {
        if(mboxId == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerHolder buyer = new BuyerHolder();
        buyer.setMboxId(mboxId);
        List<BuyerHolder> buyerList = this.select(buyer);
        if(buyerList != null && !buyerList.isEmpty())
        {
            return buyerList.get(0);
        }
        return null;
    }
    
    
    private void createMboxFolder(BuyerHolder newObj_) throws IOException
    {
        String filePath = mboxUtil.getBuyerMailBox(newObj_.getMboxId());
//        String filePath = appConfig.getBuyerMailboxRootPath()+ PS + newObj_.getMboxId();
        if (!((new File(filePath)).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath));
        }
        if (!((new File(filePath + PS + EAI + PS + "in")).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + EAI + PS + "in"));
        }
        if (!((new File(filePath + PS + EAI + PS + OUT)).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + EAI + PS + OUT));
        }
        if (!((new File(filePath + PS + "in")).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + "in"));
        }
        if (!((new File(filePath + PS + OUT)).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + OUT));
        }
        if (!((new File(filePath + PS + "invalid")).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + "invalid"));
        }
        if (!((new File(filePath + PS + ARCHIVE)).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + ARCHIVE));
        }
        if (!((new File(filePath + PS + ARCHIVE + PS + "in")).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + ARCHIVE + PS + "in"));
        }
        if (!((new File(filePath + PS + ARCHIVE + PS + OUT)).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + ARCHIVE + PS + OUT));
        }
        if (!((new File(filePath + PS + WORKING)).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + WORKING));
        }
        if (!((new File(filePath + PS + WORKING + PS + "in")).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + WORKING + PS + "in"));
        }
        if (!((new File(filePath + PS + WORKING + PS + OUT)).isDirectory()))
        {
            FileUtil.getInstance().createDir(new File(filePath + PS + WORKING + PS + OUT));
        }
    }


    @Override
    public List<BuyerHolder> selectBuyerBySupplierOid(BigDecimal supplierOid)
        throws Exception
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectBuyerBySupplierOid(supplierOid);
    }
    
    
    private void initBuyerOperations(BuyerHolder buyer) throws Exception
    {
        List<OperationHolder> buyerAdminOpns = operationService
            .selectOperationByUserType(BigDecimal.valueOf(2));
        List<OperationHolder> buyerUserOpns = operationService
            .selectOperationByUserType(BigDecimal.valueOf(4));
        List<String> opnIds = new LinkedList<String>();

        for(OperationHolder operation : buyerAdminOpns)
        {
            BuyerOperationHolder bo = new BuyerOperationHolder();
            bo.setBuyerOid(buyer.getBuyerOid());
            bo.setOpnId(operation.getOpnId());

            buyer.addBuyerOperation(bo);
            opnIds.add(operation.getOpnId());
        }

        for(OperationHolder operation : buyerUserOpns)
        {
            if (!opnIds.contains(operation.getOpnId()))
            {
                BuyerOperationHolder bo = new BuyerOperationHolder();
                bo.setBuyerOid(buyer.getBuyerOid());
                bo.setOpnId(operation.getOpnId());

                buyer.addBuyerOperation(bo);
            }
        }
    }
    
    
    private void initBuyerGivenSupplierOperations(BuyerHolder buyer)
        throws Exception
    {
        List<OperationHolder> supAdminOpns = operationService
            .selectOperationByUserType(BigDecimal.valueOf(3));
        List<OperationHolder> supUserOpns = operationService
            .selectOperationByUserType(BigDecimal.valueOf(5));
        List<String> opnIds = new LinkedList<String>();

        for(OperationHolder operation : supAdminOpns)
        {
            BuyerGivenSupplierOperationHolder bgso = new BuyerGivenSupplierOperationHolder();
            bgso.setBuyerOid(buyer.getBuyerOid());
            bgso.setOpnId(operation.getOpnId());

            buyer.addBuyerGivenSupplierOperation(bgso);
            opnIds.add(operation.getOpnId());
        }

        for(OperationHolder operation : supUserOpns)
        {
            if (!opnIds.contains(operation.getOpnId()))
            {
                BuyerGivenSupplierOperationHolder bgso = new BuyerGivenSupplierOperationHolder();
                bgso.setBuyerOid(buyer.getBuyerOid());
                bgso.setOpnId(operation.getOpnId());

                buyer.addBuyerGivenSupplierOperation(bgso);
            }
        }
    }


    @Override
    public void updateBuyerWithBuyerRule(CommonParameterHolder cp,
            BuyerHolder oldBuyer, BuyerHolder newBuyer) throws Exception
    {
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldBuyer, newBuyer);
        List<BuyerRuleHolder> buyerRules = newBuyer.getBuyerRules();
        BuyerRuleHolder param = new BuyerRuleHolder();
        param.setBuyerOid(newBuyer.getBuyerOid());
        buyerRuleMapper.delete(param);
        if(buyerRules == null || buyerRules.isEmpty())
        {
            return;
        }
        Iterator<BuyerRuleHolder> it = buyerRules.iterator();
        while(it.hasNext())
        {
            param = it.next();
            buyerRuleMapper.insert(param);
        }
    }


    @Override
    public void updateBuyerWithBuyerGivenSupplierOperation(
            CommonParameterHolder cp, BuyerHolder oldBuyer, BuyerHolder newBuyer)
            throws Exception
    {
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldBuyer, newBuyer);
        BuyerGivenSupplierOperationHolder param = new BuyerGivenSupplierOperationHolder();
        param.setBuyerOid(newBuyer.getBuyerOid());
        buyerGivenSupplierOperationMapper.delete(param);
        List<BuyerGivenSupplierOperationHolder> buyerGivenSupplierOperations = newBuyer.getBuyerGivenSupplierOperations(); 
        if(buyerGivenSupplierOperations == null || buyerGivenSupplierOperations.isEmpty())
        {
            return;
        }
        for(BuyerGivenSupplierOperationHolder obj : buyerGivenSupplierOperations)
        {
            buyerGivenSupplierOperationMapper.insert(obj);
        }
    }


    @Override
    public void updateBuyerWithBuyerOperation(CommonParameterHolder cp,
            BuyerHolder oldBuyer, BuyerHolder newBuyer) throws Exception
    {
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldBuyer, newBuyer);
        BuyerOperationHolder param = new BuyerOperationHolder();
        param.setBuyerOid(newBuyer.getBuyerOid());
        buyerOperationMapper.delete(param);
        List<BuyerOperationHolder> buyerOperations = newBuyer.getBuyerOperations(); 
        if(buyerOperations == null || buyerOperations.isEmpty())
        {
            return;
        }
        for(BuyerOperationHolder obj : buyerOperations)
        {
            buyerOperationMapper.insert(obj);
        }
    }
    
    
    @Override
    public void auditUpdateByPrimaryKeyWithBLOBs(CommonParameterHolder cp,
            BuyerHolder oldObj_, BuyerHolder newObj_) throws Exception
    {
        this.updateByPrimaryKeyWithBLOBs(oldObj_, newObj_);
    }


    @Override
    public List<BuyerHolder> selectBuyerByBuyerOids(List<BigDecimal> buyerOids)
        throws Exception
    {   
        if (buyerOids == null || buyerOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        Map<String, List<BigDecimal>> map = new HashMap<String, List<BigDecimal>>();
        map.put("buyerOids", buyerOids);
        List<BuyerHolder> buyerList = mapper.selectBuyerByBuyerOids(map);
        
        if(buyerList == null || buyerList.isEmpty())
        {
            buyerList = new ArrayList<BuyerHolder>();
        }
        
        return buyerList;
    }


    @Override
    public List<BuyerHolder> selectBuyersByGroupOid(BigDecimal groupOid)
        throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectBuyersByGroupOid(groupOid);
    }


    @Override
    public List<BuyerHolder> selectAvailableBuyersByUserOid(
        UserProfileHolder user) throws Exception
    {
        if (user == null)
        {
            throw new IllegalArgumentException();
        }
        
        
        List<BuyerHolder> result = null;
        
        /** system admin **/
        if (BigDecimal.ONE.equals(user.getUserType()))
        {
            result = this.select(new BuyerHolder());
        }
        
        /** buyer and store user **/
        else if (user.getBuyerOid() != null)
        {
            result = allowedAccessCompanyService.selectBuyerByUserOid(user.getUserOid());
            result.add(this.selectBuyerByKey(user.getBuyerOid()));
        }
        
        /** supplier user **/
        else if (user.getSupplierOid() != null)
        {
            GroupHolder group = groupService.selectGroupByUserOid(user.getUserOid());
            if (group == null)
            {
                SupplierHolder supplier = supplierService.selectSupplierByKey(user.getSupplierOid());
                if (null == supplier.getSetOid())
                {
                    result = this.selectBuyerBySupplierOid(user.getSupplierOid());
                }
                else
                {
                    result = this.selectBuyersBySetOid(supplier.getSetOid());
                }
            }
            else
            {
                List<GroupTradingPartnerHolder> gtps = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(group.getGroupOid());
                if (gtps.size() == 1 && gtps.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                {
                    result = this.selectBuyerBySupplierOid(user.getSupplierOid());
                }
                else
                {
                    result = this.selectBuyersByGroupOidAndSupplierOid(group.getGroupOid(), user.getSupplierOid());
                }
            }
        }
        
        if (null == result)
            result = new ArrayList<BuyerHolder>();
        
        //filter duplicate
        Map<BigDecimal, BuyerHolder> resultMap = new HashMap<BigDecimal, BuyerHolder>();
        BuyerHolder buyer = null;
        
        for (Iterator<BuyerHolder> itr = result.iterator();itr.hasNext();)
        {   
            buyer = itr.next();
            if (resultMap.containsKey(buyer.getBuyerOid()))
            {
                itr.remove();
                continue;
            }
            else
            {
                resultMap.put(buyer.getBuyerOid(), buyer);
            }
        }
        
        Collections.sort(result, new Comparator<BuyerHolder>(){

            @Override
            public int compare(BuyerHolder b1, BuyerHolder b2)
            {
                return b1.getBuyerName().compareTo(b2.getBuyerName());
            }
            
        });
        
        return result;
    }


    @Override
    public List<BuyerHolder> selectBuyersBySetOid(BigDecimal setOid)
        throws Exception
    {
       if (null == setOid)
       {
           throw new IllegalArgumentException();
       }
       
       Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
       param.put("setOid", setOid);
       
       return mapper.selectBuyersBySetOid(param);
    }


    @Override
    public List<BuyerHolder> selectBuyersByGroupOidAndSupplierOid(
        BigDecimal groupOid, BigDecimal supplierOid) throws Exception
    {

        if (groupOid == null || supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String,BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("groupOid", groupOid);
        param.put("supplierOid", supplierOid);
        
        return mapper.selectBuyersByGroupOidAndSupplierOid(param);
    }
    
    
}
