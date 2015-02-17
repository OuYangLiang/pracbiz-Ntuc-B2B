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
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierSharedHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.GroupTmpExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.SupplierMapper;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;
import com.pracbiz.b2bportal.core.service.GroupTmpService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.RoleGroupService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleUserService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierRoleService;
import com.pracbiz.b2bportal.core.service.SupplierRoleTmpService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.SupplierSharedService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.GnupgUtil;
import com.pracbiz.b2bportal.core.util.GnupgUtilWrapper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public class SupplierServiceImpl extends
    DBActionServiceDefaultImpl<SupplierHolder> implements SupplierService,
    ApplicationContextAware, CoreCommonConstants
{
    private static final String WORKING = "working";

    private static final String ARCHIVE = "archive";

    private static final String OUT = "out";

    private static final String EAI = "eai";
    
    
    private ApplicationContext ctx;
    @Autowired
    private SupplierMapper mapper;
    @Autowired
    private SupplierMsgSettingService supplierMsgSettingService;
    @Autowired
    private TermConditionService termConditionService;
    @Autowired
    private TradingPartnerService tradingPartnerService;
    @Autowired
    private RoleGroupService roleGroupService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileTmpService userProfileTmpService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupTmpService groupTmpService;
    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private GroupTradingPartnerService groupTradingPartnerService;
    @Autowired
    private GroupSupplierService groupSupplierService;
    @Autowired
    private CustomAppConfigHelper appConfig;
    @Autowired
    private SupplierRoleService supplierRoleService;
    @Autowired
    private SupplierRoleTmpService supplierRoleTmpService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private transient GnupgUtil gnupgUtil;
    @Autowired
    private transient ControlParameterService controlParameterService;
    @Autowired
    private transient MailBoxUtil mboxUtil;
    @Autowired
    private transient SupplierSharedService supplierSharedService;

    @Override
    public List<SupplierHolder> select(SupplierHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<SupplierHolder> selectWithBLOBs(SupplierHolder param) throws Exception
    {
        return mapper.selectWithBLOBs(param);
    }


    @Override
    public void insert(SupplierHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(SupplierHolder oldObj_,
        SupplierHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKeyWithBLOBs(SupplierHolder oldObj_,
        SupplierHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeyWithBLOBs(newObj_);
    }


    @Override
    public void updateByPrimaryKey(SupplierHolder oldObj_, SupplierHolder newObj_)
            throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(SupplierHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public int getCountOfSummary(SupplierHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<SupplierHolder> getListOfSummary(SupplierHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public SupplierHolder selectSupplierByKey(BigDecimal supplierOid)
            throws Exception
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        SupplierExHolder param = new SupplierExHolder();
        param.setSupplierOid(supplierOid);
        
        List<SupplierHolder> rlts = mapper.select(param);
        
        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }
             
        return null;
    }


    @Override
    public SupplierHolder selectSupplierWithBlobsByKey(BigDecimal supplierOid)
            throws Exception
    {
        if (supplierOid == null)
        {
            return null;
        }
        
        SupplierHolder param = new SupplierHolder();
        param.setSupplierOid(supplierOid);
        
        List<SupplierHolder> rlts = mapper.selectWithBLOBs(param);
        
        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }
        return null;
    }


    @Override
    public void deleteSupplier(CommonParameterHolder cp, BigDecimal supplierOid)
            throws Exception
    {   
      //delete group_user,group_supplier,group
        GroupTmpExHolder group = new GroupTmpExHolder();
        group.setSupplierOid(supplierOid);
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
        
        //delete supplier_role
        SupplierRoleTmpHolder supplierRole = new SupplierRoleTmpHolder();
        supplierRole.setSupplierOid(supplierOid);
        List<BigDecimal> roleOids = new ArrayList<BigDecimal>();
        List<SupplierRoleTmpHolder> supplierRoleTmpList = supplierRoleTmpService.select(supplierRole);
        if (supplierRoleTmpList != null)
        {
            Iterator<SupplierRoleTmpHolder> it = supplierRoleTmpList.iterator();
            while (it.hasNext())
            {
                SupplierRoleTmpHolder oldSupplierRoleTmp = it.next();
                supplierRoleTmpService.delete(oldSupplierRoleTmp);
                roleOids.add(oldSupplierRoleTmp.getRoleOid());
            }
        }
        List<SupplierRoleHolder> supplierRoleList = supplierRoleService.select(supplierRole);
        if (supplierRoleList != null)
        {
            Iterator<SupplierRoleHolder> it = supplierRoleList.iterator();
            while (it.hasNext())
            {
                SupplierRoleHolder oldSupplierRole = it.next();
                supplierRoleService.delete(oldSupplierRole);
                roleOids.add(oldSupplierRole.getRoleOid());
            }
        }
        for (BigDecimal roleOid : roleOids)
        {
            List<SupplierRoleHolder> newSupplierRoles = supplierRoleService.selectByRole(roleOid);
            List<SupplierRoleTmpHolder> newSupplierRoleTmps = supplierRoleTmpService.selectByRole(roleOid);
            if ((newSupplierRoles == null || newSupplierRoles.isEmpty()) && (newSupplierRoleTmps == null || newSupplierRoleTmps.isEmpty()))
            {
                RoleTmpHolder role = new RoleTmpHolder();
                role.setRoleOid(roleOid);
                roleService.removeRoleProfile(cp, role);
            }
        }
        
        //delete tradingPartner
        TradingPartnerExHolder tradingPartner = new TradingPartnerExHolder();
        tradingPartner.setSupplierOid(supplierOid);
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
        
        //delete group supplier in buyer group
        GroupSupplierHolder groupSupplier = new GroupSupplierHolder();
        groupSupplier.setSupplierOid(supplierOid);
        groupSupplierService.delete(groupSupplier);
        
        
        //delete user
        UserProfileTmpExHolder userProfile = new UserProfileTmpExHolder();
        userProfile.setSupplierOid(supplierOid);
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
        
        //delete supplier shared
        List<SupplierSharedHolder> supplierSharedList = supplierSharedService.selectBySelfSupOid(supplierOid);
        if (supplierSharedList != null)
        {
            for (SupplierSharedHolder supplierShared : supplierSharedList)
            {
                supplierSharedService.delete(supplierShared);
            }
        }
        supplierSharedList = supplierSharedService.selectByGrantSupOid(supplierOid);
        if (supplierSharedList != null)
        {
            for (SupplierSharedHolder supplierShared : supplierSharedList)
            {
                supplierSharedService.delete(supplierShared);
            }
        }
        
        
        //delete term-condition,msg setting,supplier
        SupplierHolder supplier = this.selectSupplierByKey(supplierOid);
        List<TermConditionHolder> termConditions = termConditionService.selectTermsConditionsBySupplierOid(supplierOid);
        List<SupplierMsgSettingHolder> msgSettings = supplierMsgSettingService.selectSupplierMsgSettingBySupplierOid(supplierOid);
        supplier.setTermConditions(termConditions);
        supplier.setMsgSetting(msgSettings);
        if(termConditions != null)
        {
            Iterator<TermConditionHolder> it = termConditions.iterator();
            while(it.hasNext())
            {
                TermConditionHolder obj = it.next();
                termConditionService.delete(obj);
            }
        }
        if(msgSettings != null)
        {
            Iterator<SupplierMsgSettingHolder> it = msgSettings.iterator();
            while(it.hasNext())
            {
                SupplierMsgSettingHolder obj = it.next();
                supplierMsgSettingService.delete(obj);
            }
        }
        this.getMeBean().auditDelete(cp, supplier);
        //delete mailbox
        // String filePath = appConfig.getSupplierMailboxRootPath()+ File.separator + supplier.getMboxId();
        String filePath = mboxUtil.getSupplierMailBox(supplier.getMboxId());
        FileUtil.getInstance().deleleAllFile(new File(filePath));
        
        //delete gpg key
        String userId = GnupgUtilWrapper.getInstance().wrapSupplierUserId(appConfig.getServerId(), supplier.getMboxId());
        if (gnupgUtil.isKeyExistInServer(userId))
        {
            gnupgUtil.revokeKey(userId, userId);
            if (gnupgUtil.getUseKeyServer() != null && gnupgUtil.getUseKeyServer())
            {
                gnupgUtil.sendKeyToKeyserver(userId, null);
            }
            gnupgUtil.removeKeyById(userId);
        }
    }
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    private SupplierService getMeBean()
    {
        return ctx.getBean("supplierService", SupplierService.class);
    }


    @Override
    public List<SupplierHolder> selectSupplierByGroupOidAndBuyerOid(BigDecimal groupOid, BigDecimal buyerOid)
        throws Exception
    {
        if (null == groupOid || null == buyerOid)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("groupOid", groupOid);
        param.put("buyerOid", buyerOid);
        
        return this.mapper.selectSupplierByGroupOidAndBuyerOid(param);
    }


    @Override
    public List<SupplierHolder> selectSupplierByTmpGroupOidAndBuyerOid(BigDecimal groupOid, BigDecimal buyerOid)
        throws Exception
    {
        if (null == groupOid || null == buyerOid)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("groupOid", groupOid);
        param.put("buyerOid", buyerOid);
        
        return this.mapper.selectSupplierByTmpGroupOidAndBuyerOid(param);
    }


    @Override
    public List<SupplierHolder> selectSupplierBySupplierOids(
        List<BigDecimal> supplierOids) throws Exception
    {
        if (null == supplierOids || supplierOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        SupplierExHolder param = new SupplierExHolder();
        param.setSupplierOids(supplierOids);
        
        return mapper.select(param);
    }


    @Override
    public void insertSupplierWithMsgSetting(CommonParameterHolder cp,
            SupplierHolder supplier) throws Exception
    {
        this.getMeBean().auditInsert(cp, supplier);
        List<SupplierMsgSettingHolder> supplierMsgSettings = supplier.getMsgSetting();
        if(supplierMsgSettings != null)
        {
            for(SupplierMsgSettingHolder supplierMsg : supplierMsgSettings)
            {
                supplierMsgSettingService.insert(supplierMsg);
            }
        }
        createMboxFolder(supplier);
        
        String userId = GnupgUtilWrapper.getInstance().wrapSupplierUserId(appConfig.getServerId(), supplier.getMboxId());
        if (supplier.getClientEnabled() && !gnupgUtil.isKeyExistInServer(userId))
        {
            gnupgUtil.generateKey(userId, userId, null);
            if (gnupgUtil.getUseKeyServer() != null && gnupgUtil.getUseKeyServer())
            {
                gnupgUtil.sendKeyToKeyserver(userId, null);
            }
        }
    }


    @Override
    public void updateSupplierWithMsgSetting(CommonParameterHolder cp,
            SupplierHolder oldSupplier, SupplierHolder newSupplier)
            throws Exception
    {
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldSupplier, newSupplier);
        List<SupplierMsgSettingHolder> oldSupplierMsgSettings = oldSupplier.getMsgSetting();
        List<SupplierMsgSettingHolder> newSupplierMsgSettings = newSupplier.getMsgSetting();
        if(newSupplierMsgSettings != null)
        {
            if(oldSupplierMsgSettings == null)
            {
                for(SupplierMsgSettingHolder supplierMsg : newSupplierMsgSettings)
                {
                    supplierMsgSettingService.insert(supplierMsg);
                }
            }
            else
            {
                for(SupplierMsgSettingHolder supplierMsg : newSupplierMsgSettings)
                {
                    Iterator<SupplierMsgSettingHolder> it = oldSupplierMsgSettings.iterator();
                    boolean flag = false;
                    while(it.hasNext())
                    {
                        SupplierMsgSettingHolder obj = it.next();
                        if(supplierMsg.getMsgType().equals(obj.getMsgType()))
                        {
                            supplierMsgSettingService.updateByPrimaryKey(obj, supplierMsg);
                            flag = true;
                            break;
                        }
                    }
                    if(!flag)
                    {
                        supplierMsgSettingService.insert(supplierMsg);
                    }
                }
                
                for(SupplierMsgSettingHolder supplierMsg : oldSupplierMsgSettings)
                {
                    Iterator<SupplierMsgSettingHolder> it = newSupplierMsgSettings.iterator();
                    boolean flag = false;
                    while(it.hasNext())
                    {
                        SupplierMsgSettingHolder obj = it.next();
                        if(supplierMsg.getMsgType().equals(obj.getMsgType()))
                        {
                            flag = true;
                            break;
                        }
                    }
                    if(!flag)
                    {
                        supplierMsgSettingService.delete(supplierMsg);
                    }
                }
            }
        }
        
        List<TermConditionHolder> currTermConditions = oldSupplier.getTermConditions();
        List<TermConditionHolder> termConditions = newSupplier.getTermConditions();
        Map<BigDecimal, TermConditionHolder> oidMap = new HashMap<BigDecimal, TermConditionHolder>();
        if(currTermConditions != null)
        {
            Iterator<TermConditionHolder> it = currTermConditions.iterator();
            while(it.hasNext())
            {
                TermConditionHolder obj =  it.next();
                TradingPartnerExHolder tp = new TradingPartnerExHolder();
                tp.setTermConditionOid(obj.getTermConditionOid());
                List<TradingPartnerHolder> tpList = tradingPartnerService.select(tp);
                if(tpList == null || tpList.isEmpty())
                {
                    termConditionService.delete(obj);
                }
                else
                {
                    oidMap.put(obj.getTermConditionOid(), obj);
                }
            }
        }
        if(termConditions != null)
        {
            for(TermConditionHolder term : termConditions)
            {
                if(oidMap.containsKey(term.getTermConditionOid()))
                {
                    termConditionService.updateByPrimaryKeySelective(oidMap.get(term.getTermConditionOid()), term);
                }
                else
                {
                    termConditionService.insert(term);
                }
            }
        }
    }


    @Override
    public List<SupplierHolder> selectActiveSuppliers() throws Exception
    {
        SupplierExHolder supParam = new SupplierExHolder();
        supParam.setActive(true);
        return mapper.select(supParam);
    }


    @Override
    public SupplierHolder selectSupplierByCode(String supplierCode)
        throws Exception
    {
        if (supplierCode == null)
        {
            throw new IllegalArgumentException();
        }
        
        SupplierExHolder param = new SupplierExHolder();
        param.setSupplierCode(supplierCode);
        
        List<SupplierHolder> rlts = mapper.select(param);
        
        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }
             
        return null;
    }


    @Override
    public SupplierHolder selectSupplierByMboxId(String mboxId)
        throws Exception
    {
        if (mboxId == null)
        {
            throw new IllegalArgumentException();
        }
        
        SupplierExHolder param = new SupplierExHolder();
        param.setMboxId(mboxId);
        
        List<SupplierHolder> rlts = mapper.select(param);
        
        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }
             
        return null;
    }
    

    @Override
    public void insertSupplierMasterBatch(MsgTransactionsHolder msg,
            List<SupplierHolder> newSms, List<SupplierHolder> oldSms,
            List<TradingPartnerHolder> newTps,
            List<TradingPartnerHolder> oldTps, Boolean isSendEmail)
            throws Exception
    {
        List<ControlParameterHolder> msgList = controlParameterService
                .selectCacheControlParametersBySectId(SECT_ID_HSEKEEP);
        
        if (newSms != null && !newSms.isEmpty())
        {
            String requestUrl = appConfig.getServerUrl() + "/user/";

            for (SupplierHolder sm : newSms)
            {
                mapper.insert(sm);
                this.createMboxFolder(sm);
//                TermConditionHolder tc = sm.getTermCondition();
//                if (tc != null)
//                {
//                    termConditionService.insert(tc);
//                }
                
                for (ControlParameterHolder cp : msgList)
                {
                    SupplierMsgSettingHolder msgSetting = new SupplierMsgSettingHolder();
                    msgSetting.setSupplierOid(sm.getSupplierOid());
                    msgSetting.setMsgType(cp.getParamId());
                    msgSetting.setExcludeSucc(false);
                    msgSetting.setFileFormat(appConfig.getDefaultFileFormat(cp.getParamId()));
                    supplierMsgSettingService.insert(msgSetting);
                }
                
                if (sm.getSupplierRoles() == null)
                {
                    continue;
                }
                
                supplierRoleService.insert(sm.getSupplierRoles().get(0));
                supplierRoleTmpService.insert((SupplierRoleTmpHolder) sm.getSupplierRoles().get(0));
                
                UserProfileTmpHolder tmpUser = new UserProfileTmpHolder();
                BeanUtils.copyProperties(sm.getUserProfile(), tmpUser);
                
                userProfileService.createUserProfileForImportSupplier(requestUrl, tmpUser, isSendEmail);
                
            }
        }        

        if (oldSms != null && !oldSms.isEmpty())
        {
            for (SupplierHolder sm : oldSms)
            {
                mapper.updateByPrimaryKeySelective(sm);
    
//                TermConditionHolder newTc = sm.getTermCondition();
//                if (newTc != null)
//                {
//                    termConditionService.insert(newTc);
//                }
    
//                List<TermConditionHolder> oldTcs = sm.getTermConditions();
//                if (oldTcs != null && !oldTcs.isEmpty())
//                {
//                    for (TermConditionHolder oldTc : oldTcs)
//                    {
//                        termConditionService.updateByPrimaryKey(null, oldTc);
//                    }
//                }
            }
        }

        if (newTps != null && !newTps.isEmpty())
        {
            for (TradingPartnerHolder newTp : newTps)
            {
                tradingPartnerService.insert(newTp);
            }
        }
        if (oldTps != null && !oldTps.isEmpty())
        {
            for (TradingPartnerHolder oldTp : oldTps)
            {
                tradingPartnerService.updateByPrimaryKeySelective(null, oldTp);
            }
        }
    }


    private void createMboxFolder(SupplierHolder newObj_) throws IOException
    {
        //String filePath = appConfig.getSupplierMailboxRootPath()+ File.separator + newObj_.getMboxId();
        String filePath = mboxUtil.getSupplierMailBox(newObj_.getMboxId());
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
    public List<SupplierHolder> selectSupplierByBuyerOid(BigDecimal buyerOid)
        throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectSupplierByBuyerOid(buyerOid);
    }
    

    @Override
    public void auditUpdateByPrimaryKeyWithBLOBs(CommonParameterHolder cp,
            SupplierHolder oldObj_, SupplierHolder newObj_) throws Exception
    {
        this.updateByPrimaryKeyWithBLOBs(oldObj_, newObj_);
        String userId = GnupgUtilWrapper.getInstance().wrapSupplierUserId(appConfig.getServerId(), newObj_.getMboxId());
        if (newObj_.getClientEnabled() && !gnupgUtil.isKeyExistInServer(userId))
        {
            gnupgUtil.generateKey(userId, userId, null);
            if (gnupgUtil.getUseKeyServer() != null && gnupgUtil.getUseKeyServer())
            {
                gnupgUtil.sendKeyToKeyserver(userId, null);
            }
        }
    }


    @Override
    public List<SupplierHolder> selectSuppliersBySetOid(BigDecimal setOid)
            throws Exception
    {
        if (setOid == null)
        {
            throw new IllegalArgumentException();
        }
        SupplierExHolder param = new SupplierExHolder();
        param.setSetOid(setOid);
        return this.select(param);
    }


    @Override
    public void updateSupplierShared(CommonParameterHolder cp,
            SupplierHolder oldSupplier, SupplierHolder newSupplier)
            throws Exception
    {
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldSupplier, newSupplier);
        SupplierSharedHolder param = new SupplierSharedHolder();
        param.setSelfSupOid(newSupplier.getSupplierOid());
        supplierSharedService.delete(param);
        List<SupplierSharedHolder> supplierShareds = newSupplier.getSupplierShareds();
        if (supplierShareds != null && !supplierShareds.isEmpty())
        {
            for (SupplierSharedHolder supplierShared : supplierShareds)
            {
                supplierSharedService.insert(supplierShared);
            }
        }
    }
    
    @Override
    public List<SupplierHolder> selectSupplierByBuyerOidAndUserOid(
            BigDecimal buyerOid, BigDecimal currentUserOid) throws Exception
    {
        if (buyerOid == null || currentUserOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("buyerOid", buyerOid);
        map.put("currentUserOid", currentUserOid);
        return mapper.selectSupplierByBuyerOidAndUserOid(map);
    }


    @Override
    public List<SupplierHolder> selectAvailableSuppliersByUserOid(
        UserProfileHolder user) throws Exception
    {
        if (user == null)
        {
            throw new IllegalArgumentException();
        }
        
        List<SupplierHolder> result = null;
        
        /** system admin **/
        if (user.getUserType().equals(BigDecimal.ONE))
        {
            result = this.select(new SupplierExHolder());
        }
        
        /** buyer user**/
        else if (BigDecimal.valueOf(2).equals(user.getUserType())
                || BigDecimal.valueOf(4).equals(user.getUserType()))
        {
            GroupHolder group = groupService.selectGroupByUserOid(user.getUserOid());
            if (group != null)
            {
                List<GroupSupplierHolder> groupSuppliers = groupSupplierService.selectGroupSupplierByGroupOid(group.getGroupOid());
                
                if (groupSuppliers != null && groupSuppliers.size() == 1 &&
                        groupSuppliers.get(0).getSupplierOid().equals(BigDecimal.valueOf(-1)))
                {
                    result = this.selectSupplierByBuyerOid(user.getBuyerOid());
                }
                else
                {
                    result = this.selectSupplierByGroupOidAndBuyerOid(group.getGroupOid(), user.getBuyerOid());
                }
            }
        }
        
        /** store user**/
        else if (BigDecimal.valueOf(6).equals(user.getUserType())
                || BigDecimal.valueOf(7).equals(user.getUserType()))
        {
            result = this.selectSupplierByBuyerOid(user.getBuyerOid());
        }
        
        /** supplier user **/
        else if (user.getSupplierOid() != null)
        {
            result = new LinkedList<SupplierHolder>();
            result.add( this.selectSupplierByKey(user.getSupplierOid()) );
        }
        
        if (null == result)
            result = new ArrayList<SupplierHolder>();
        
        Collections.sort(result, new Comparator<SupplierHolder>(){

            @Override
            public int compare(SupplierHolder s1, SupplierHolder s2)
            {
                return s1.getSupplierName().compareTo(s2.getSupplierName());
            }
            
        });
        
        return result;
    }


    @Override
    public void insertNewSupplierFromSupplierMaster(SupplierHolder supplier,
            TradingPartnerHolder tp, List<ControlParameterHolder> msgList,
            Boolean isSendEmail) throws Exception
    {
        if (supplier == null || tp == null)
        {
            throw new IllegalArgumentException();
        }
        mapper.insert(supplier);
        this.createMboxFolder(supplier);
        
        for (ControlParameterHolder cp : msgList)
        {
            SupplierMsgSettingHolder msgSetting = new SupplierMsgSettingHolder();
            msgSetting.setSupplierOid(supplier.getSupplierOid());
            msgSetting.setMsgType(cp.getParamId());
            msgSetting.setExcludeSucc(false);
            msgSetting.setFileFormat(appConfig.getDefaultFileFormat(cp.getParamId()));
            supplierMsgSettingService.insert(msgSetting);
        }
        
        if (supplier.getSupplierRoles() != null)
        {
            supplierRoleService.insert(supplier.getSupplierRoles().get(0));
            supplierRoleTmpService.insert((SupplierRoleTmpHolder) supplier.getSupplierRoles().get(0));
        }
        
        if (supplier.getUserProfile() != null)
        {
            UserProfileTmpHolder tmpUser = new UserProfileTmpHolder();
            BeanUtils.copyProperties(supplier.getUserProfile(), tmpUser);
            tmpUser.setLoginPwd(EncodeUtil.getInstance().computePwd(tmpUser.getLoginPwd(), tmpUser.getUserOid()));
            String requestUrl = appConfig.getServerUrl() + "/user/";
            userProfileService.createUserProfileForImportSupplier(requestUrl, tmpUser, isSendEmail);
        }
        tradingPartnerService.insert(tp);
        
        
    }


    @Override
    public void updateOldSupplierFromSupplierMaster(SupplierHolder supplier,
            TradingPartnerHolder tp, boolean flag) throws Exception
    {
        
        if (supplier == null || tp == null)
        {
            throw new IllegalArgumentException();
        }
        mapper.updateByPrimaryKey(supplier);
        
        if(flag)
        {
            tradingPartnerService.insert(tp);
        }
        else
        {
            tradingPartnerService.updateByPrimaryKeySelective(null, tp);
        }
    }


    @Override
    public void updateSupplierBySupplierOid(BigDecimal setOid,
        BigDecimal supplierOid) throws Exception
    {
        
        if (null == supplierOid)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("setOid", setOid);
        map.put("supplierOid", supplierOid);
        
        mapper.updateBySupplierOid(map);
    }
    
    
    @Override
    public List<SupplierHolder> selectGrantSuppliersBySupplierSideUser(
        UserProfileHolder user) throws Exception
    {
        if (null == user)
        {
            throw new IllegalArgumentException();
        }
        
        GroupHolder group = groupService.selectGroupByUserOid(user.getUserOid());
        
        if (null != group)
            return null;
        
        SupplierHolder supplier = this.selectSupplierByKey(user.getSupplierOid());
        
        if (null == supplier.getSetOid())
            return null;
        
        List<SupplierHolder> rlts = selectSuppliersBySetOid(supplier.getSetOid());
        
        if (null == rlts || rlts.isEmpty())
            return null;
        
        Collections.sort(rlts, new Comparator<SupplierHolder>(){

            @Override
            public int compare(SupplierHolder s1, SupplierHolder s2)
            {
                return s1.getSupplierName().compareTo(s2.getSupplierName());
            }
            
        });
        
        return rlts;
    }
}
