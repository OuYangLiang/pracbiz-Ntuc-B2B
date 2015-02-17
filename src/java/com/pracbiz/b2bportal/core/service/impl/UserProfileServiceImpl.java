package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.LastUpdateFrom;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyHolder;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.FavouriteListSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierAdminRolloutHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.holder.UserClassTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserSessionHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.FavouriteListExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.AllowedAccessCompanyMapper;
import com.pracbiz.b2bportal.core.mapper.AllowedAccessCompanyTmpMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreAreaUserMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreAreaUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreUserMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.GroupUserMapper;
import com.pracbiz.b2bportal.core.mapper.GroupUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.PasswordHistoryMapper;
import com.pracbiz.b2bportal.core.mapper.ResetPasswordRequestHistoryMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.UserClassMapper;
import com.pracbiz.b2bportal.core.mapper.UserClassTmpMapper;
import com.pracbiz.b2bportal.core.mapper.UserProfileMapper;
import com.pracbiz.b2bportal.core.mapper.UserProfileTmpMapper;
import com.pracbiz.b2bportal.core.mapper.UserSubclassMapper;
import com.pracbiz.b2bportal.core.mapper.UserSubclassTmpMapper;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.EmailSendService;
import com.pracbiz.b2bportal.core.service.FavouriteListService;
import com.pracbiz.b2bportal.core.service.FavouriteListSupplierService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PasswordHistoryService;
import com.pracbiz.b2bportal.core.service.RoleUserService;
import com.pracbiz.b2bportal.core.service.SupplierAdminRolloutService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.TransactionBatchService;
import com.pracbiz.b2bportal.core.service.UserClassService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserSessionService;
import com.pracbiz.b2bportal.core.service.UserSubclassService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;


/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */

public class UserProfileServiceImpl extends DBActionServiceDefaultImpl<UserProfileHolder>
        implements UserProfileService, ApplicationContextAware, CoreCommonConstants
{
    private static final String USER_TYPE_LIST = "userTypeList";
    private ApplicationContext ctx;
    private static final String LOCAL_IP = "LOCALHOST";
    @Autowired private UserProfileMapper mapper;
    @Autowired private UserProfileTmpMapper userProfileTmpMapper;
    @Autowired private RoleUserTmpMapper roleUserTmpMapper;
    @Autowired private GroupUserTmpMapper groupUserTmpMapper;
    @Autowired private RoleUserMapper roleUserMapper;
    @Autowired private GroupUserMapper groupUserMapper;
    @Autowired private PasswordHistoryMapper passwordHistoryMapper;
    @Autowired private ResetPasswordRequestHistoryMapper resetPasswordRequestHistoryMapper;
    @Autowired private GroupUserService groupUserService;
    @Autowired private RoleUserService roleUserService;
    @Autowired private PasswordHistoryService passwordHistoryService;
    @Autowired private EmailSendService emailSendService;
    @Autowired private UserSessionService userSessionService;
    @Autowired private BuyerStoreUserMapper buyerStoreUserMapper;
    @Autowired private BuyerStoreUserTmpMapper buyerStoreUserTmpMapper;
    @Autowired private BuyerStoreAreaUserMapper buyerStoreAreaUserMapper;
    @Autowired private BuyerStoreAreaUserTmpMapper buyerStoreAreaUserTmpMapper;
    @Autowired private BuyerStoreUserService buyerStoreUserService;
    @Autowired private BuyerStoreAreaUserService buyerStoreAreaUserService;
    @Autowired private AllowedAccessCompanyMapper allowedAccessCompanyMapper;
    @Autowired private AllowedAccessCompanyTmpMapper allowedAccessCompanyTmpMapper;
    @Autowired private AllowedAccessCompanyService allowedAccessCompanyService;
    @Autowired private MsgTransactionsService msgTransactionsService;
    @Autowired private TransactionBatchService transactionBatchService;
    @Autowired private SupplierMsgSettingService supplierMsgSettingService;
    @Autowired private FavouriteListService favouriteListService;
    @Autowired private FavouriteListSupplierService favouriteListSupplierService;
    @Autowired private OidService oidService;
    @Autowired private SupplierAdminRolloutService supplierAdminRolloutService;
    @Autowired private UserClassMapper userClassMapper;
    @Autowired private UserClassTmpMapper userClassTmpMapper;
    @Autowired private UserSubclassMapper userSubclassMapper;
    @Autowired private UserSubclassTmpMapper userSubclassTmpMapper;
    @Autowired private UserClassService userClassService;
    @Autowired private UserSubclassService userSubclassService;
    @Autowired private BuyerStoreService buyerStoreService;
    @Autowired private BuyerService buyerService;
    
    @Override
    public List<UserProfileHolder> select(UserProfileHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(UserProfileHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public int getCountOfSummary(UserProfileExHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<UserProfileExHolder> getListOfSummary(UserProfileExHolder param)
        throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public void updateByPrimaryKeySelective(UserProfileHolder oldObj_,
        UserProfileHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }




    @Override
    public void updateByPrimaryKey(UserProfileHolder oldObj_,
        UserProfileHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(UserProfileHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

	
    @Override
    public void createUserProfile(CommonParameterHolder cp, String requestUrl, String clientIp,
            UserProfileTmpHolder user, boolean sendEmail) throws Exception
    {
        if (cp.getMkMode())
        {
            getMeBean().mkCreate(cp, user);
        }
        else
        {   
            user.setActor(cp.getLoginId());
            user.setActionType(DbActionType.CREATE);
            user.setActionDate(user.getCreateDate());
            user.setCtrlStatus(MkCtrlStatus.APPROVED);
            
            getMeBean().auditInsert(cp, user);
            userProfileTmpMapper.insert(user);
            
            List<RoleUserHolder> roleUsers = user.getRoleUsers();
            
            if (null != roleUsers && !roleUsers.isEmpty())
            {
                for (RoleUserHolder ru : roleUsers)
                {
                    roleUserMapper.insert(ru);
                    roleUserTmpMapper.insert((RoleUserTmpHolder) ru);
                }
            }

            List<BuyerStoreUserHolder> storeUsers = user.getBuyerStoreUsers();
            
            if (null != storeUsers && !storeUsers.isEmpty())
            {
                for (BuyerStoreUserHolder su : storeUsers)
                {
                    buyerStoreUserMapper.insert(su);
                    buyerStoreUserTmpMapper.insert((BuyerStoreUserTmpHolder) su);
                }
            }
           
            List<BuyerStoreUserHolder> wareHouseUsers = user.getBuyerWareHouseUsers();
           
            if (null != wareHouseUsers && !wareHouseUsers.isEmpty())
            {
                for (BuyerStoreUserHolder sw : wareHouseUsers)
                {
                    buyerStoreUserMapper.insert(sw);
                    buyerStoreUserTmpMapper.insert((BuyerStoreUserTmpHolder) sw);
                }
            }
            
            List<BuyerStoreAreaUserHolder> areaUsers = user.getBuyerStoreAreaUsers();
            
            if (null != areaUsers && !areaUsers.isEmpty())
            {
                for (BuyerStoreAreaUserHolder au : areaUsers)
                {
                    if (au.getAreaOid().compareTo(new BigDecimal(-1)) == 0)
                    {
                        continue;
                    }
                    buyerStoreAreaUserMapper.insert(au);
                    buyerStoreAreaUserTmpMapper.insert((BuyerStoreAreaUserTmpHolder) au);
                }
            }
            
            List<UserClassHolder> ucList = user.getUserClassList();
            
            if (null != ucList && !ucList.isEmpty())
            {
                for (UserClassHolder uc : ucList)
                {
                    userClassMapper.insert(uc);
                    userClassTmpMapper.insert((UserClassTmpHolder) uc);
                }
            }
            
            List<UserSubclassHolder> usList = user.getUserSubclassList();
            
            if (null != usList && !usList.isEmpty())
            {
                for (UserSubclassHolder us : usList)
                {
                    userSubclassMapper.insert(us);
                    userSubclassTmpMapper.insert((UserSubclassTmpHolder) us);
                }
            }
            
            List<AllowedAccessCompanyHolder> allowedBuyerList = user.getAllowedBuyerList();
            
            if (null != allowedBuyerList && !allowedBuyerList.isEmpty())
            {
                for (AllowedAccessCompanyHolder aac : allowedBuyerList)
                {
                    allowedAccessCompanyMapper.insert(aac);
                    allowedAccessCompanyTmpMapper.insert((AllowedAccessCompanyTmpHolder) aac);
                }
            }
            
            List<GroupUserHolder> groupUsers = user.getGroupUsers();
            
            if (null != groupUsers && !groupUsers.isEmpty())
            {
                GroupUserTmpHolder gut = (GroupUserTmpHolder) groupUsers.get(0);
                gut.setActionType(DbActionType.CREATE);
                gut.setLastUpdateFrom(LastUpdateFrom.USER);
                gut.setApproved(true);
                groupUserMapper.insert(gut);
                groupUserTmpMapper.insert(gut);
            }
           
            
            if (sendEmail)
            {
                List<UserProfileTmpHolder> users = new ArrayList<UserProfileTmpHolder>();
                users.add(user);
                emailSendService.sendSetPasswordEmailByCallable(cp, requestUrl, clientIp, users);
            }
            
        }

        
    }
    
    
    @Override
    public void createUserProfileForImportSupplier(String requestUrl,
            UserProfileTmpHolder user, boolean sendEmail) throws Exception
    {
        user.setActor("SYSTEM");
        user.setActionType(DbActionType.CREATE);
        user.setActionDate(user.getCreateDate());
        user.setCtrlStatus(MkCtrlStatus.APPROVED);
        
        mapper.insert(user);
        userProfileTmpMapper.insert(user);
        
        List<RoleUserHolder> roleUsers = user.getRoleUsers();
        
        if (null != roleUsers && !roleUsers.isEmpty())
        {
            for (RoleUserHolder ru : roleUsers)
            {
                roleUserMapper.insert(ru);
                roleUserTmpMapper.insert((RoleUserTmpHolder) ru);
            }
        }
        
        if (sendEmail)
        {
            CommonParameterHolder cp = new CommonParameterHolder();
            cp.setClientIp(LOCAL_IP);
            cp.setMkMode(Boolean.FALSE);
            cp.setLoginId("SYSTEM");

            List<UserProfileTmpHolder> users = new ArrayList<UserProfileTmpHolder>();
            users.add(user);
            emailSendService.setNoAudit(Boolean.TRUE);
            emailSendService.sendSetPasswordEmailByCallable(cp, requestUrl, LOCAL_IP, users);
        }
    
    }
    
    
    @Override
    public void updateUserProfile(CommonParameterHolder cp, UserProfileTmpHolder oldObj_,
            UserProfileTmpHolder newObj_, String requestUrl, String clientIp) throws Exception
    {
        if (cp.getMkMode())
        {
            getMeBean().mkUpdate(cp, oldObj_, newObj_);
        }
        else
        {
            newObj_.setActor(cp.getLoginId());
            newObj_.setActionType(DbActionType.UPDATE);
            newObj_.setActionDate(newObj_.getUpdateDate());
            newObj_.setCtrlStatus(MkCtrlStatus.APPROVED);
            
            getMeBean().auditUpdateByPrimaryKey(cp, oldObj_, newObj_);
            userProfileTmpMapper.updateByPrimaryKey(newObj_);
            
            RoleUserTmpHolder roleUserParam = new RoleUserTmpHolder();
            roleUserParam.setUserOid(oldObj_.getUserOid());
            
            roleUserMapper.delete(roleUserParam);
            roleUserTmpMapper.delete(roleUserParam);
            
            AllowedAccessCompanyTmpHolder allowedAccessCompanyParam = new AllowedAccessCompanyTmpHolder();
            allowedAccessCompanyParam.setUserOid(oldObj_.getUserOid());
            
            allowedAccessCompanyMapper.delete(allowedAccessCompanyParam);
            allowedAccessCompanyTmpMapper.delete(allowedAccessCompanyParam);
            
            BuyerStoreUserTmpHolder storeUserParam = new BuyerStoreUserTmpHolder();
            storeUserParam.setUserOid(oldObj_.getUserOid());
            
            buyerStoreUserMapper.delete(storeUserParam);
            buyerStoreUserTmpMapper.delete(storeUserParam);
            
            BuyerStoreAreaUserTmpHolder areaUserParam = new BuyerStoreAreaUserTmpHolder();
            areaUserParam.setUserOid(oldObj_.getUserOid());
            
            buyerStoreAreaUserMapper.delete(areaUserParam);
            buyerStoreAreaUserTmpMapper.delete(areaUserParam);
            
            UserClassTmpHolder userClassParam = new UserClassTmpHolder();
            userClassParam.setUserOid(oldObj_.getUserOid());
            
            userClassMapper.delete(userClassParam);
            userClassTmpMapper.delete(userClassParam);
            
            UserSubclassTmpHolder userSubclassParam = new UserSubclassTmpHolder();
            userSubclassParam.setUserOid(oldObj_.getUserOid());
            
            userSubclassMapper.delete(userSubclassParam);
            userSubclassTmpMapper.delete(userSubclassParam);
            
            GroupUserTmpHolder groupUserParam = new GroupUserTmpHolder();
            groupUserParam.setUserOid(oldObj_.getUserOid());
            
            groupUserMapper.delete(groupUserParam);
            groupUserTmpMapper.delete(groupUserParam);
            
            List<RoleUserHolder> roleUsers = newObj_.getRoleUsers();
            
            
            if (null != roleUsers && !roleUsers.isEmpty())
            {
                for (RoleUserHolder ru : roleUsers)
                {
                    roleUserMapper.insert(ru);
                    roleUserTmpMapper.insert((RoleUserTmpHolder) ru);
                }
            }
            
            List<AllowedAccessCompanyHolder> allowedAccessCompanys = newObj_.getAllowedBuyerList();
            
            if (null != allowedAccessCompanys && !allowedAccessCompanys.isEmpty())
            {
                for (AllowedAccessCompanyHolder aac : allowedAccessCompanys)
                {
                    allowedAccessCompanyMapper.insert(aac);
                    allowedAccessCompanyTmpMapper.insert((AllowedAccessCompanyTmpHolder)aac);
                }
            }

            List<BuyerStoreUserHolder> storeUsers = newObj_.getBuyerStoreUsers();
            
            if (null != storeUsers && !storeUsers.isEmpty())
            {
                for (BuyerStoreUserHolder su : storeUsers)
                {
                    buyerStoreUserMapper.insert(su);
                    buyerStoreUserTmpMapper.insert((BuyerStoreUserTmpHolder) su);
                }
            }
            
            List<BuyerStoreUserHolder> wareHouseUsers = newObj_.getBuyerWareHouseUsers();
            
            if (null != wareHouseUsers && !wareHouseUsers.isEmpty())
            {
                for (BuyerStoreUserHolder sw : wareHouseUsers)
                {
                    buyerStoreUserMapper.insert(sw);
                    buyerStoreUserTmpMapper.insert((BuyerStoreUserTmpHolder) sw);
                }
            }
            
            List<BuyerStoreAreaUserHolder> areaUsers = newObj_.getBuyerStoreAreaUsers();
            
            if (null != areaUsers && !areaUsers.isEmpty())
            {
                for (BuyerStoreAreaUserHolder au : areaUsers)
                {
                    if (au.getAreaOid().compareTo(new BigDecimal(-1)) == 0)
                    {
                        continue;
                    }
                    buyerStoreAreaUserMapper.insert(au);
                    buyerStoreAreaUserTmpMapper.insert((BuyerStoreAreaUserTmpHolder) au);
                }
            }
            
            List<UserClassHolder> userClassList = newObj_.getUserClassList();
            if (userClassList !=  null && !userClassList.isEmpty())
            {
                for (UserClassHolder uc : userClassList)
                {
                    userClassTmpMapper.insert((UserClassTmpHolder) uc);
                    userClassMapper.insert(uc);
                }
            }
            
            
            List<UserSubclassHolder> userSubclassList = newObj_.getUserSubclassList();
            if (userSubclassList != null && !userSubclassList.isEmpty())
            {
                for (UserSubclassHolder us : userSubclassList)
                {
                    userSubclassTmpMapper.insert((UserSubclassTmpHolder) us);
                    userSubclassMapper.insert(us);
                }
            }
            
            List<GroupUserHolder> groupUsers = newObj_.getGroupUsers();
            
            if (null != groupUsers && !groupUsers.isEmpty())
            {
                groupUserMapper.insert(groupUsers.get(0));
                GroupUserTmpHolder groupUserTmp = (GroupUserTmpHolder) groupUsers.get(0);
                groupUserTmp.setLastUpdateFrom(LastUpdateFrom.USER);
                groupUserTmp.setActionType(DbActionType.CREATE);
                groupUserTmp.setApproved(true);
                groupUserTmpMapper.insert(groupUserTmp);
            }
            
            if(oldObj_.getEmail() != null && newObj_.getEmail() != null
                && !oldObj_.getEmail().trim().equalsIgnoreCase(newObj_.getEmail().trim()))
            {
                List<UserProfileTmpHolder> users = new ArrayList<UserProfileTmpHolder>();
                users.add(newObj_);
                emailSendService.sendSetPasswordEmailByCallable(cp, requestUrl,
                    clientIp, users);
            }

        }
        
    }
    
    
    @Override
    public void removeUserProfile(CommonParameterHolder cp, UserProfileTmpHolder oldObj, boolean force) throws Exception
    {
        if (cp.getMkMode())
        {
            // maker checker mode
            this.getMeBean().mkDelete(cp, oldObj);
        }
        else
        {
            // non maker checker mode
            if(force)
            {
                UserSessionHolder userSession = new UserSessionHolder();
                userSession.setUserOid(oldObj.getUserOid());
                userSessionService.deleteUserSession(userSession);
            }
            // Remove records from role-user & t-role-user tables.
            RoleUserTmpHolder ruParam = new RoleUserTmpHolder();
            ruParam.setUserOid(oldObj.getUserOid());
            
            roleUserMapper.delete(ruParam);
            roleUserTmpMapper.delete(ruParam);
            
            //Remove records from allowed_access_company and t_allowed_access_company
            AllowedAccessCompanyTmpHolder aacParam = new AllowedAccessCompanyTmpHolder();
            aacParam.setUserOid(oldObj.getUserOid());
            
            allowedAccessCompanyService.delete(aacParam);
            allowedAccessCompanyTmpMapper.delete(aacParam);
            
            // Remove records from store-user & t-store-user tables.
            BuyerStoreUserTmpHolder suParam = new BuyerStoreUserTmpHolder();
            suParam.setUserOid(oldObj.getUserOid());
            
            buyerStoreUserMapper.delete(suParam);
            buyerStoreUserTmpMapper.delete(suParam);
            
            // Remove records from store-user & t-store-user tables.
            BuyerStoreAreaUserTmpHolder auParam = new BuyerStoreAreaUserTmpHolder();
            auParam.setUserOid(oldObj.getUserOid());
            
            buyerStoreAreaUserMapper.delete(auParam);
            buyerStoreAreaUserTmpMapper.delete(auParam);
            
            UserClassTmpHolder ucParam = new UserClassTmpHolder();
            ucParam.setUserOid(oldObj.getUserOid());
            
            userClassMapper.delete(ucParam);
            userClassTmpMapper.delete(ucParam);
            
            UserSubclassTmpHolder usParam = new UserSubclassTmpHolder();
            usParam.setUserOid(oldObj.getUserOid());
            
            userSubclassMapper.delete(usParam);
            userSubclassTmpMapper.delete(usParam);
            
            // Remove records from group-user & t-group-user tables.
            GroupUserTmpHolder guParam = new GroupUserTmpHolder();
            guParam.setUserOid(oldObj.getUserOid());
            
            groupUserMapper.delete(guParam);
            groupUserTmpMapper.delete(guParam);
            
            // Remove records from password history table.
            PasswordHistoryHolder phParam = new PasswordHistoryHolder();
            phParam.setUserOid(oldObj.getUserOid());
            
            passwordHistoryMapper.delete(phParam);
            
            ResetPasswordRequestHistoryHolder rprh = new ResetPasswordRequestHistoryHolder();
            rprh.setLoginId(oldObj.getLoginId());
            resetPasswordRequestHistoryMapper.delete(rprh);
            
            this.getMeBean().auditDelete(cp, oldObj);
            userProfileTmpMapper.delete(oldObj);
            
        }
    }
    
    
    @Override
    public void approveUserProfile(CommonParameterHolder cp, String requestUrl,
            String clientIp, UserProfileTmpHolder tmpUser) throws Exception
    {
        if (DbActionType.CREATE.equals(tmpUser.getActionType()))
        {
            this.getMeBean().mkApprove(cp, null, tmpUser);
            return;
        }
        
        UserProfileHolder mainUser = this.selectUserProfileByKey(tmpUser.getUserOid());

        mainUser.setGroupUsers(new ArrayList<GroupUserHolder>());
        mainUser.setRoleUsers(new ArrayList<RoleUserHolder>());
        mainUser.setBuyerStoreUsers(new ArrayList<BuyerStoreUserHolder>());
        mainUser.setBuyerStoreAreaUsers(new ArrayList<BuyerStoreAreaUserHolder>());
        mainUser.setAllowedBuyerList(new ArrayList<AllowedAccessCompanyHolder>());
        mainUser.setUserClassList(new ArrayList<UserClassHolder>());
        mainUser.setUserSubclassList(new ArrayList<UserSubclassHolder>());
        
        List<GroupUserHolder> oldGroupUserList = groupUserService.selectGroupUserByUserOid(mainUser.getUserOid());
        if (oldGroupUserList != null)
        {
            for (GroupUserHolder oldGroupUser : oldGroupUserList)
                mainUser.getGroupUsers().add(oldGroupUser);
        }

        List<RoleUserHolder> oldRoleUserList = roleUserService.selectRoleUserByUserOid(mainUser.getUserOid());
        if (oldRoleUserList != null)
        {
            for (RoleUserHolder oldRoleUser : oldRoleUserList)
                mainUser.getRoleUsers().add(oldRoleUser);
        }
        
        List<AllowedAccessCompanyHolder> oldAllowedAccessCompanyList = allowedAccessCompanyService.selectByUserOid(mainUser.getUserOid());
        if (oldAllowedAccessCompanyList != null)
        {
            for (AllowedAccessCompanyHolder oldAllowedAccessCompany : oldAllowedAccessCompanyList)
                mainUser.getAllowedBuyerList().add(oldAllowedAccessCompany);
        }
        
        List<BuyerStoreUserHolder> oldStoreUserList = buyerStoreUserService
                .selectStoreUserByUserOid(mainUser.getUserOid());
        if (oldStoreUserList != null)
        {
            for (BuyerStoreUserHolder oldStoreUser : oldStoreUserList)
                mainUser.getBuyerStoreUsers().add(oldStoreUser);
        }
        
        List<BuyerStoreAreaUserHolder> oldAreaUserList = buyerStoreAreaUserService
                .selectAreaUserByUserOid(mainUser.getUserOid());
        if (oldAreaUserList != null)
        {
            for (BuyerStoreAreaUserHolder oldAreaUser : oldAreaUserList)
                mainUser.getBuyerStoreAreaUsers().add(oldAreaUser);
        }
        
        List<UserClassHolder> oldUserClassList = userClassService.selectByUserOid(mainUser.getUserOid());
        if (oldUserClassList != null)
        {
            for (UserClassHolder oldUserClass : oldUserClassList)
                mainUser.getUserClassList().add(oldUserClass);
        }
        
        List<UserSubclassHolder> oldUserSubclassList = userSubclassService.selectByUserOid(mainUser.getUserOid());
        if (oldUserSubclassList != null)
        {
            for (UserSubclassHolder oldUserSubclass : oldUserSubclassList)
                mainUser.getUserSubclassList().add(oldUserSubclass);
        }
        
        this.getMeBean().mkApprove(cp, mainUser, tmpUser);
        
        if(tmpUser.getEmail() != null && mainUser.getEmail() != null
            && DbActionType.UPDATE.equals(tmpUser.getActionType())
            && !tmpUser.getEmail().trim().equalsIgnoreCase(mainUser.getEmail().trim()))
        {
            List<UserProfileTmpHolder> users = new ArrayList<UserProfileTmpHolder>();
            users.add(tmpUser);
            emailSendService.sendSetPasswordEmailByCallable(cp, requestUrl,
                clientIp, users);
        }
    }
    
    
    @Override
    public void rejectUserProfile(CommonParameterHolder cp,
            UserProfileTmpHolder tmpUser) throws Exception
    {
        if (DbActionType.CREATE.equals(tmpUser.getActionType()))
        {
            this.getMeBean().mkReject(cp, null, tmpUser);
            return;
        }
        
        UserProfileHolder mainUser = this.selectUserProfileByKey(tmpUser.getUserOid());

        mainUser.setGroupUsers(new ArrayList<GroupUserHolder>());
        mainUser.setRoleUsers(new ArrayList<RoleUserHolder>());
        mainUser.setBuyerStoreUsers(new ArrayList<BuyerStoreUserHolder>());
        mainUser.setBuyerStoreAreaUsers(new ArrayList<BuyerStoreAreaUserHolder>());
        mainUser.setAllowedBuyerList(new ArrayList<AllowedAccessCompanyHolder>());
        mainUser.setUserClassList(new ArrayList<UserClassHolder>());
        mainUser.setUserSubclassList(new ArrayList<UserSubclassHolder>());
        
        List<GroupUserHolder> oldGroupUserList = groupUserService.selectGroupUserByUserOid(mainUser.getUserOid());
        for (GroupUserHolder oldGroupUser : oldGroupUserList)
            mainUser.getGroupUsers().add(oldGroupUser);

        List<RoleUserHolder> oldRoleUserList = roleUserService.selectRoleUserByUserOid(mainUser.getUserOid());
        for (RoleUserHolder oldRoleUser : oldRoleUserList)
            mainUser.getRoleUsers().add(oldRoleUser);
        
        List<AllowedAccessCompanyHolder> oldAllowedAccessCompanyList = allowedAccessCompanyService.selectByUserOid(mainUser.getUserOid());
        for (AllowedAccessCompanyHolder oldAllowedAccessCompany : oldAllowedAccessCompanyList)
            mainUser.getAllowedBuyerList().add(oldAllowedAccessCompany);

        List<BuyerStoreUserHolder> oldStoreUserList = buyerStoreUserService
                .selectStoreUserByUserOid(mainUser.getUserOid());
        for (BuyerStoreUserHolder oldStoreUser : oldStoreUserList)
            mainUser.getBuyerStoreUsers().add(oldStoreUser);
        
        List<BuyerStoreAreaUserHolder> oldAreaUserList = buyerStoreAreaUserService
                .selectAreaUserByUserOid(mainUser.getUserOid());
        for (BuyerStoreAreaUserHolder oldAreaUser : oldAreaUserList)
            mainUser.getBuyerStoreAreaUsers().add(oldAreaUser);
        
        List<UserClassHolder> oldUserClassList = userClassService.selectByUserOid(mainUser.getUserOid());
        if (oldUserClassList != null)
        {
            for (UserClassHolder oldUserClass : oldUserClassList)
                mainUser.getUserClassList().add(oldUserClass);
        }
        
        List<UserSubclassHolder> oldUserSubclassList = userSubclassService.selectByUserOid(mainUser.getUserOid());
        if (oldUserSubclassList != null)
        {
            for (UserSubclassHolder oldUserSubclass : oldUserSubclassList)
                mainUser.getUserSubclassList().add(oldUserSubclass);
        }
        
        this.getMeBean().mkReject(cp, mainUser, tmpUser);
    }


    @Override
    public void withdrawUserProfile(CommonParameterHolder cp,
            UserProfileTmpHolder tmpUser) throws Exception
    {
        if (DbActionType.CREATE.equals(tmpUser.getActionType()))
        {
            this.getMeBean().mkWithdraw(cp, null, tmpUser);
            return;
        }
        
        UserProfileHolder mainUser = this.selectUserProfileByKey(tmpUser.getUserOid());

        mainUser.setGroupUsers(new ArrayList<GroupUserHolder>());
        mainUser.setRoleUsers(new ArrayList<RoleUserHolder>());
        mainUser.setBuyerStoreUsers(new ArrayList<BuyerStoreUserHolder>());
        mainUser.setBuyerStoreAreaUsers(new ArrayList<BuyerStoreAreaUserHolder>());
        mainUser.setAllowedBuyerList(new ArrayList<AllowedAccessCompanyHolder>());
        mainUser.setUserClassList(new ArrayList<UserClassHolder>());
        mainUser.setUserSubclassList(new ArrayList<UserSubclassHolder>());
        
        List<GroupUserHolder> oldGroupUserList = groupUserService.selectGroupUserByUserOid(mainUser.getUserOid());
        for (GroupUserHolder oldGroupUser : oldGroupUserList)
            mainUser.getGroupUsers().add(oldGroupUser);

        List<RoleUserHolder> oldRoleUserList = roleUserService.selectRoleUserByUserOid(mainUser.getUserOid());
        for (RoleUserHolder oldRoleUser : oldRoleUserList)
            mainUser.getRoleUsers().add(oldRoleUser);
        
        List<AllowedAccessCompanyHolder> oldAllowedAccessCompanyList = allowedAccessCompanyService.selectByUserOid(mainUser.getUserOid());
        for (AllowedAccessCompanyHolder oldAllowedAccessCompany : oldAllowedAccessCompanyList)
        {
            mainUser.getAllowedBuyerList().add(oldAllowedAccessCompany);
        }

        List<BuyerStoreUserHolder> oldStoreUserList = buyerStoreUserService
                .selectStoreUserByUserOid(mainUser.getUserOid());
        for (BuyerStoreUserHolder oldStoreUser : oldStoreUserList)
            mainUser.getBuyerStoreUsers().add(oldStoreUser);
        
        List<BuyerStoreAreaUserHolder> oldAreaUserList = buyerStoreAreaUserService
                .selectAreaUserByUserOid(mainUser.getUserOid());
        for (BuyerStoreAreaUserHolder oldAreaUser : oldAreaUserList)
            mainUser.getBuyerStoreAreaUsers().add(oldAreaUser);
        
        List<UserClassHolder> oldUserClassList = userClassService.selectByUserOid(mainUser.getUserOid());
        if (oldUserClassList != null)
        {
            for (UserClassHolder oldUserClass : oldUserClassList)
                mainUser.getUserClassList().add(oldUserClass);
        }
        
        List<UserSubclassHolder> oldUserSubclassList = userSubclassService.selectByUserOid(mainUser.getUserOid());
        if (oldUserSubclassList != null)
        {
            for (UserSubclassHolder oldUserSubclass : oldUserSubclassList)
                
                mainUser.getUserSubclassList().add(oldUserSubclass);
        }
        
        this.getMeBean().mkWithdraw(cp, mainUser, tmpUser);
    }


    @Override
    public UserProfileHolder getUserProfileByLoginId(String loginId)
            throws Exception
    {
        if (StringUtils.isBlank(loginId))
        {
            throw new IllegalArgumentException();
        }
        
        UserProfileTmpExHolder param = new UserProfileTmpExHolder();
        param.setLoginId(loginId);
        
        List<UserProfileHolder> users = mapper.select(param);
        
        if (users != null && !users.isEmpty())
        {
            return users.get(0);
        }
        
        return null;
     }
    
    
    @Override
    public UserProfileHolder selectUserProfileByKey(BigDecimal userOid)
    {
        if (null == userOid)
        {
            throw new IllegalArgumentException();
        }
        
        UserProfileTmpExHolder param = new UserProfileTmpExHolder();
        param.setUserOid(userOid);
        
        List<UserProfileHolder> users = mapper.select(param);
        
        if (users != null && !users.isEmpty())
        {
            return users.get(0);
        }
        
        return null;
    }
    

    @Override
    public void mkCreate(CommonParameterHolder cp, UserProfileTmpHolder newObj_)
            throws Exception
    {
        UserProfileTmpHolder user = newObj_;
        user.setActor(cp.getLoginId());
        user.setActionType(DbActionType.CREATE);
        user.setActionDate(user.getCreateDate());
        user.setCtrlStatus(MkCtrlStatus.PENDING);
        
        userProfileTmpMapper.insert(user);
        
        List<RoleUserHolder> roleUsers = user.getRoleUsers();
        
        if (null != roleUsers && !roleUsers.isEmpty())
        {
            for (RoleUserHolder ru : roleUsers)
            {
                roleUserTmpMapper.insert((RoleUserTmpHolder) ru);
            }
        }
        
        List<BuyerStoreUserHolder> storeUsers = user.getBuyerStoreUsers();
        
        if (null != storeUsers && !storeUsers.isEmpty())
        {
            for (BuyerStoreUserHolder su : storeUsers)
            {
                buyerStoreUserTmpMapper.insert((BuyerStoreUserTmpHolder) su);
            }
        }
        
        List<BuyerStoreUserHolder> wareHouseUsers = user.getBuyerWareHouseUsers();
        
        if (null != wareHouseUsers && !wareHouseUsers.isEmpty())
        {
            for (BuyerStoreUserHolder sw : wareHouseUsers)
            {
                buyerStoreUserTmpMapper.insert((BuyerStoreUserTmpHolder) sw);
            }
        }
        
        List<BuyerStoreAreaUserHolder> areaUsers = user.getBuyerStoreAreaUsers();
        
        if (null != areaUsers && !areaUsers.isEmpty())
        {
            for (BuyerStoreAreaUserHolder au : areaUsers)
            {
                if (au.getAreaOid().compareTo(new BigDecimal(-1)) == 0)
                {
                    continue;
                }
                buyerStoreAreaUserTmpMapper.insert((BuyerStoreAreaUserTmpHolder) au);
            }
        }
        
        List<UserClassHolder> userClassList = user.getUserClassList();
        
        if (null != userClassList && !userClassList.isEmpty())
        {
            for (UserClassHolder uc : userClassList)
            {
                userClassTmpMapper.insert((UserClassTmpHolder) uc);
            }
        }
        
        List<UserSubclassHolder> userSubclassList = user.getUserSubclassList();
        
        if (null != userSubclassList && !userSubclassList.isEmpty())
        {
            for (UserSubclassHolder us : userSubclassList)
            {
                userSubclassTmpMapper.insert((UserSubclassTmpHolder) us);
            }
        }
        
        List<AllowedAccessCompanyHolder> allowedBuyerList = user.getAllowedBuyerList();
        
        if (null != allowedBuyerList && !allowedBuyerList.isEmpty())
        {
            for (AllowedAccessCompanyHolder aac : allowedBuyerList)
            {
                allowedAccessCompanyTmpMapper.insert((AllowedAccessCompanyTmpHolder) aac);
            }
        }
        
        List<GroupUserHolder> groupUsers = user.getGroupUsers();
        
        if (null != groupUsers && !groupUsers.isEmpty())
        {
            GroupUserTmpHolder gut = (GroupUserTmpHolder) groupUsers.get(0);
            gut.setActionType(DbActionType.CREATE);
            gut.setLastUpdateFrom(LastUpdateFrom.USER);
            gut.setApproved(false);
            groupUserTmpMapper.insert(gut);
        }
    }


    @Override
    public void mkUpdate(CommonParameterHolder cp,
        UserProfileTmpHolder oldObj_, UserProfileTmpHolder newObj_)
        throws Exception
    {
        UserProfileTmpHolder user = newObj_;
        user.setActor(cp.getLoginId());
        user.setActionDate(user.getUpdateDate());
        if(!(DbActionType.CREATE.equals(user.getActionType())
                && MkCtrlStatus.PENDING.equals(user.getCtrlStatus())))
        {
            user.setActionType(DbActionType.UPDATE);
        }
        user.setCtrlStatus(MkCtrlStatus.PENDING);        
        
        userProfileTmpMapper.updateByPrimaryKey(user);

        RoleUserTmpHolder roleUserParam = new RoleUserTmpHolder();
        roleUserParam.setUserOid(user.getUserOid());
        
        roleUserTmpMapper.delete(roleUserParam);
        
        List<RoleUserHolder> roleUsers = user.getRoleUsers();
        
        if (null != roleUsers && !roleUsers.isEmpty())
        {
            for (RoleUserHolder ru : roleUsers)
            {
                roleUserTmpMapper.insert((RoleUserTmpHolder) ru);
            }
        }
        
        AllowedAccessCompanyTmpHolder allowedAccessCompanyParam = new AllowedAccessCompanyTmpHolder();
        allowedAccessCompanyParam.setUserOid(user.getUserOid());
        
        allowedAccessCompanyTmpMapper.delete(allowedAccessCompanyParam);
        
        List<AllowedAccessCompanyHolder> allowedAccessCompanys = user.getAllowedBuyerList();
        
        if (null != allowedAccessCompanys && !allowedAccessCompanys.isEmpty())
        {
            for (AllowedAccessCompanyHolder aac : allowedAccessCompanys)
            {
                allowedAccessCompanyTmpMapper.insert((AllowedAccessCompanyTmpHolder)aac);
            }
        }


        BuyerStoreUserTmpHolder soreUserParam = new BuyerStoreUserTmpHolder();
        soreUserParam.setUserOid(user.getUserOid());
        
        buyerStoreUserTmpMapper.delete(soreUserParam);
        
        BuyerStoreAreaUserTmpHolder areaUserParam = new BuyerStoreAreaUserTmpHolder();
        areaUserParam.setUserOid(user.getUserOid());
        
        buyerStoreAreaUserTmpMapper.delete(areaUserParam);
        
        List<BuyerStoreUserHolder> storeUsers = user.getBuyerStoreUsers();
        
        if (null != storeUsers && !storeUsers.isEmpty())
        {
            for (BuyerStoreUserHolder su : storeUsers)
            {
                buyerStoreUserTmpMapper.insert((BuyerStoreUserTmpHolder) su);
            }
        }
        
        List<BuyerStoreUserHolder> wareHouseUsers = user.getBuyerWareHouseUsers();
        
        if (null != wareHouseUsers && !wareHouseUsers.isEmpty())
        {
            for (BuyerStoreUserHolder sw : wareHouseUsers)
            {
                buyerStoreUserTmpMapper.insert((BuyerStoreUserTmpHolder) sw);
            }
        }
        
        List<BuyerStoreAreaUserHolder> areaUsers = user.getBuyerStoreAreaUsers();
        
        if (null != areaUsers && !areaUsers.isEmpty())
        {
            for (BuyerStoreAreaUserHolder au : areaUsers)
            {
                if (au.getAreaOid().compareTo(new BigDecimal(-1)) == 0)
                {
                    continue;
                }
                buyerStoreAreaUserTmpMapper.insert((BuyerStoreAreaUserTmpHolder) au);
            }
        }
        
        UserClassTmpHolder userClassParam = new UserClassTmpHolder();
        userClassParam.setUserOid(user.getUserOid());
        userClassTmpMapper.delete(userClassParam);
        
        List<UserClassHolder> userClassList = user.getUserClassList();
        if (userClassList !=  null && !userClassList.isEmpty())
        {
            for (UserClassHolder uc : userClassList)
            {
                userClassTmpMapper.insert((UserClassTmpHolder) uc);
            }
        }
        
        UserSubclassTmpHolder userSubclassParam = new UserSubclassTmpHolder();
        userSubclassParam.setUserOid(user.getUserOid());
        userSubclassTmpMapper.delete(userSubclassParam);
        
        List<UserSubclassHolder> userSubclassList = user.getUserSubclassList();
        if (userSubclassList != null && !userSubclassList.isEmpty())
        {
            for (UserSubclassHolder us : userSubclassList)
            {
                userSubclassTmpMapper.insert((UserSubclassTmpHolder) us);
            }
        }
        
        UserProfileTmpHolder oldUser = (UserProfileTmpHolder) oldObj_;
        if (oldUser.getGroupUsers() == null || oldUser.getGroupUsers().isEmpty())
        {
            List<GroupUserHolder> groupUsers = user.getGroupUsers();
            
            if (null != groupUsers && !groupUsers.isEmpty())
            {
                GroupUserTmpHolder gut = (GroupUserTmpHolder) groupUsers.get(0);
                gut.setActionType(DbActionType.CREATE);
                gut.setLastUpdateFrom(LastUpdateFrom.USER);
                gut.setApproved(false);
                groupUserTmpMapper.insert(gut);
            }
        }
        else if (oldUser.getGroupUsers().size() == 1)
        {
            List<GroupUserHolder> groupUsers = user.getGroupUsers();
            GroupUserTmpHolder oldGut = (GroupUserTmpHolder) oldUser.getGroupUsers().get(0);
            
            if (null == groupUsers || groupUsers.isEmpty())
            {
                oldGut.setActionType(DbActionType.DELETE);
                oldGut.setApproved(false);
                oldGut.setLastUpdateFrom(LastUpdateFrom.USER);
                groupUserTmpMapper.updateByPrimaryKeySelective(oldGut);
            }
            else if (((GroupUserTmpHolder) groupUsers.get(0)).getGroupOid().equals(oldGut.getGroupOid()) 
                    && oldGut.getLastUpdateFrom().equals(LastUpdateFrom.USER)
                    && oldGut.getActionType().equals(DbActionType.DELETE))
            {
                oldGut.setActionType(DbActionType.CREATE);
                oldGut.setLastUpdateFrom(LastUpdateFrom.USER);
                oldGut.setApproved(false);
                groupUserTmpMapper.updateByPrimaryKeySelective(oldGut);
            }
            else if (!((GroupUserTmpHolder) groupUsers.get(0)).getGroupOid().equals(oldGut.getGroupOid()))
            {
                GroupUserTmpHolder newGut = (GroupUserTmpHolder) groupUsers.get(0);
                List<GroupUserHolder> mainGroupUsers = groupUserService.selectGroupUserByUserOid(user.getUserOid());
                if (DbActionType.CREATE.equals(user.getActionType()) 
                        || (mainGroupUsers == null || mainGroupUsers.isEmpty()))
                {
                    groupUserTmpMapper.delete(oldGut);
                    
                    newGut.setActionType(DbActionType.CREATE);
                    newGut.setLastUpdateFrom(LastUpdateFrom.USER);
                    newGut.setApproved(false);
                    groupUserTmpMapper.insert(newGut);
                }
                else 
                {
                    newGut.setActionType(DbActionType.CREATE);
                    newGut.setApproved(false);
                    newGut.setLastUpdateFrom(LastUpdateFrom.USER);
                    groupUserTmpMapper.insert(newGut);
                    
                    oldGut.setActionType(DbActionType.DELETE);
                    oldGut.setLastUpdateFrom(LastUpdateFrom.USER);
                    oldGut.setApproved(false);
                    groupUserTmpMapper.updateByPrimaryKeySelective(oldGut);
                }
            }
        }
        else if (oldUser.getGroupUsers().size() == 2)
        {
            List<GroupUserHolder> groupUsers = user.getGroupUsers();
            if (null == groupUsers || groupUsers.isEmpty())
            {
                for (Object o : oldUser.getGroupUsers())
                {
                    GroupUserTmpHolder gu = (GroupUserTmpHolder) o;
                    if (gu.getActionType().equals(DbActionType.CREATE))
                    {
                        groupUserTmpMapper.delete(gu);
                    }
                }
            }
            else
            {
                Map<DbActionType, GroupUserTmpHolder> rlt = this.parseGroupUserListToMap(oldUser.getGroupUsers());
                GroupUserTmpHolder oldDelGut = rlt.get(DbActionType.DELETE);
                GroupUserTmpHolder oldCreGut = rlt.get(DbActionType.CREATE);
                GroupUserTmpHolder newGut = (GroupUserTmpHolder) groupUsers.get(0);
                if (newGut.getGroupOid().equals(oldDelGut.getGroupOid()))
                {
                    GroupUserTmpHolder param = new GroupUserTmpHolder();
                    param.setUserOid(user.getUserOid());
                    groupUserTmpMapper.delete(param);
                    
                    oldDelGut.setActionType(DbActionType.CREATE);
                    oldDelGut.setLastUpdateFrom(LastUpdateFrom.USER);
                    oldDelGut.setApproved(false);
                    groupUserTmpMapper.insert(oldDelGut);
                }
                else if (!newGut.getGroupOid().equals(oldCreGut.getGroupOid()))
                {
                    groupUserTmpMapper.delete(oldCreGut);
                    newGut.setActionType(DbActionType.CREATE);
                    newGut.setLastUpdateFrom(LastUpdateFrom.USER);
                    newGut.setApproved(false);
                    groupUserTmpMapper.insert(newGut);
                }
            }
        }
    }


    @Override
    public void mkDelete(CommonParameterHolder cp, UserProfileTmpHolder oldObj_)
        throws Exception
    {
        UserProfileTmpHolder user = oldObj_;
        user.setActor(cp.getLoginId());
        user.setActionType(DbActionType.DELETE);
        user.setActionDate(new Date());
        user.setCtrlStatus(MkCtrlStatus.PENDING);
        
        userProfileTmpMapper.updateByPrimaryKeySelective(user);

        List<GroupUserHolder> groupUsers = user.getGroupUsers();
        
        if (null != groupUsers && !groupUsers.isEmpty())
        {
            GroupUserTmpHolder gut = (GroupUserTmpHolder) groupUsers.get(0);
            gut.setActionType(DbActionType.DELETE);
            gut.setLastUpdateFrom(LastUpdateFrom.USER);
            gut.setApproved(false);
            groupUserTmpMapper.updateByPrimaryKey(gut);
        }
    }


    @Override
    public void mkApprove(CommonParameterHolder cp, UserProfileHolder main,
        UserProfileTmpHolder tmp) throws Exception
    {
        UserProfileTmpHolder tmpUser = tmp;
        UserProfileHolder mainUser = main;
        
        if (DbActionType.CREATE.equals(tmpUser.getActionType()))
        {
            tmpUser.setCtrlStatus(MkCtrlStatus.APPROVED);
            tmpUser.setActor(cp.getLoginId());
            tmpUser.setActionDate(new Date());

            userProfileTmpMapper.updateByPrimaryKey(tmpUser);

            this.insert(tmpUser);

            List<GroupUserHolder> groupUsers = tmpUser.getGroupUsers();
            
            if (null != groupUsers && !groupUsers.isEmpty())
            {
                for (GroupUserHolder gu : groupUsers)
                {
                    groupUserMapper.insert(gu);
                    GroupUserTmpHolder gut = (GroupUserTmpHolder) gu;
                    gut.setApproved(true);
                    groupUserTmpMapper.updateByPrimaryKeySelective(gut);
                }
            }
            List<RoleUserHolder> roleUsers = tmpUser.getRoleUsers();
            
            if (null != roleUsers && !roleUsers.isEmpty())
            {
                for (RoleUserHolder gu : roleUsers)
                {
                    roleUserMapper.insert(gu);
                }
            }
            
            List<AllowedAccessCompanyHolder> allowedAccessCompanys = tmpUser.getAllowedBuyerList();
            
            if (null != allowedAccessCompanys && !allowedAccessCompanys.isEmpty())
            {
               for (AllowedAccessCompanyHolder aac : allowedAccessCompanys) 
               {
                   allowedAccessCompanyService.insert(aac);
               }
            }
            
            List<BuyerStoreUserHolder> storeUsers = tmpUser.getBuyerStoreUsers();
            
            if (null != storeUsers && !storeUsers.isEmpty())
            {
                for (BuyerStoreUserHolder su : storeUsers)
                {
                    buyerStoreUserMapper.insert(su);
                }
            }

            List<BuyerStoreAreaUserHolder> areaUsers = tmpUser.getBuyerStoreAreaUsers();
            
            if (null != areaUsers && !areaUsers.isEmpty())
            {
                for (BuyerStoreAreaUserHolder au : areaUsers)
                {
                    buyerStoreAreaUserMapper.insert((BuyerStoreAreaUserTmpHolder) au);
                }
            }
            
            List<UserClassHolder> userClassList = tmpUser.getUserClassList();
            if (userClassList != null && !userClassList.isEmpty())
            {
                for (UserClassHolder uc : userClassList)
                {
                    userClassMapper.insert(uc);
                }
            }
            
            List<UserSubclassHolder> userSubclassList = tmpUser.getUserSubclassList();
            if (userSubclassList != null && !userSubclassList.isEmpty())
            {
                for (UserSubclassHolder us : userSubclassList)
                {
                    userSubclassMapper.insert(us);
                }
            }
            
        }
        else if (DbActionType.UPDATE.equals(tmpUser.getActionType()))
        {
            tmpUser.setCtrlStatus(MkCtrlStatus.APPROVED);
            tmpUser.setActor(cp.getLoginId());
            tmpUser.setActionDate(new Date());

            userProfileTmpMapper.updateByPrimaryKey(tmpUser);

            RoleUserHolder ruParam = new RoleUserHolder();
            ruParam.setUserOid(mainUser.getUserOid());
            roleUserMapper.delete(ruParam);
            
            AllowedAccessCompanyHolder aacParam = new AllowedAccessCompanyHolder();
            aacParam.setUserOid(mainUser.getUserOid());
            allowedAccessCompanyService.delete(aacParam);

            BuyerStoreUserHolder suParam = new BuyerStoreUserHolder();
            suParam.setUserOid(mainUser.getUserOid());
            
            buyerStoreUserMapper.delete(suParam);
            
            BuyerStoreAreaUserHolder auParam = new BuyerStoreAreaUserHolder();
            auParam.setUserOid(mainUser.getUserOid());
            
            buyerStoreAreaUserMapper.delete(auParam);
            
            UserClassHolder ucParam = new UserClassHolder();
            ucParam.setUserOid(mainUser.getUserOid());
            
            userClassMapper.delete(ucParam);
            
            UserSubclassHolder usParam = new UserSubclassHolder();
            usParam.setUserOid(mainUser.getUserOid());
            
            userSubclassMapper.delete(usParam);
            
            this.updateByPrimaryKey(mainUser, tmpUser);

            List<RoleUserHolder> roleUsers = tmpUser.getRoleUsers();
            
            if (null != roleUsers && !roleUsers.isEmpty())
            {
                for (RoleUserHolder gu : roleUsers)
                {
                    roleUserMapper.insert(gu);
                }
            }
            
            List<AllowedAccessCompanyHolder> allowedBuyers = tmpUser.getAllowedBuyerList();
            
            if (null != allowedBuyers && !allowedBuyers.isEmpty())
            {
                for (AllowedAccessCompanyHolder ab : allowedBuyers)
                {
                    allowedAccessCompanyService.insert(ab);
                }
            }
            
            List<BuyerStoreUserHolder> storeUsers = tmpUser.getBuyerStoreUsers();
            
            if (null != storeUsers && !storeUsers.isEmpty())
            {
                for (BuyerStoreUserHolder su : storeUsers)
                {
                    buyerStoreUserMapper.insert(su);
                }
            }

            List<BuyerStoreAreaUserHolder> areaUsers = tmpUser.getBuyerStoreAreaUsers();
            
            if (null != areaUsers && !areaUsers.isEmpty())
            {
                for (BuyerStoreAreaUserHolder au : areaUsers)
                {
                    buyerStoreAreaUserMapper.insert((BuyerStoreAreaUserTmpHolder) au);
                }
            }
            
            List<UserClassHolder> userClassList = tmpUser.getUserClassList();
            
            if (userClassList != null && !userClassList.isEmpty())
            {
                for (UserClassHolder uc : userClassList)
                {
                    userClassMapper.insert(uc);
                }
            }
            
            List<UserSubclassHolder> userSubclassList = tmpUser.getUserSubclassList();
            
            if (userSubclassList != null && !userSubclassList.isEmpty())
            {
                for (UserSubclassHolder us : userSubclassList)
                {
                    userSubclassMapper.insert(us);
                }
            }
            if (tmpUser.getGroupUsers() != null 
                    && !tmpUser.getGroupUsers().isEmpty())
            {
                for (GroupUserHolder gu : tmpUser.getGroupUsers())
                {
                    GroupUserTmpHolder tmpGut = (GroupUserTmpHolder) gu;
                    if (!tmpGut.getApproved() && tmpGut.getLastUpdateFrom().equals(LastUpdateFrom.USER))
                    {
                        GroupUserHolder guParam = new GroupUserHolder();
                        guParam.setUserOid(mainUser.getUserOid());
                        groupUserMapper.delete(guParam);
                        
                        if (tmpGut.getActionType().equals(DbActionType.CREATE))
                        {
                            GroupUserHolder guc = new GroupUserHolder();
                            guc.setUserOid(tmpGut.getUserOid());
                            guc.setGroupOid(tmpGut.getGroupOid());
                            groupUserMapper.insert(guc);
                            
                            tmpGut.setApproved(true);
                            groupUserTmpMapper.updateByPrimaryKey(tmpGut);
                        }
                        else if (tmpGut.getActionType().equals(DbActionType.DELETE))
                        {
                            groupUserTmpMapper.delete(tmpGut);
                        }
                    }
                }
            }
        }
        else if (DbActionType.DELETE.equals(tmpUser.getActionType()))
        {
            // Remove records from role-user & t-role-user tables.
            RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
            ruTmpParam.setUserOid(mainUser.getUserOid());

            roleUserMapper.delete(ruTmpParam);
            roleUserTmpMapper.delete(ruTmpParam);
            
            // Remove records from allowed_access_company & t_allowed_access_company.
            AllowedAccessCompanyTmpHolder aacTmpParam = new AllowedAccessCompanyTmpHolder();
            aacTmpParam.setUserOid(mainUser.getUserOid());
            
            allowedAccessCompanyMapper.delete(aacTmpParam);
            allowedAccessCompanyTmpMapper.delete(aacTmpParam);

            // Remove records from group-user & t-group-user tables.
            GroupUserTmpHolder guTmpParam = new GroupUserTmpHolder();
            guTmpParam.setUserOid(mainUser.getUserOid());

            groupUserMapper.delete(guTmpParam);
            groupUserTmpMapper.delete(guTmpParam);
            
            // Remove records from store-user & t-store-user tables.
            BuyerStoreUserTmpHolder suParam = new BuyerStoreUserTmpHolder();
            suParam.setUserOid(mainUser.getUserOid());
            
            buyerStoreUserMapper.delete(suParam);
            buyerStoreUserTmpMapper.delete(suParam);
            
            // Remove records from area-user & t-area-user tables.
            BuyerStoreAreaUserTmpHolder auParam = new BuyerStoreAreaUserTmpHolder();
            auParam.setUserOid(mainUser.getUserOid());
            
            buyerStoreAreaUserMapper.delete(auParam);
            buyerStoreAreaUserTmpMapper.delete(auParam);
            
            UserClassTmpHolder ucParam = new UserClassTmpHolder();
            ucParam.setUserOid(mainUser.getUserOid());
            
            userClassMapper.delete(ucParam);
            userClassTmpMapper.delete(ucParam);
            
            UserSubclassTmpHolder usParam = new UserSubclassTmpHolder();
            usParam.setUserOid(mainUser.getUserOid());
            
            userSubclassMapper.delete(usParam);
            userSubclassTmpMapper.delete(usParam);
            
            // Remove records from password history table.
            PasswordHistoryHolder phParam = new PasswordHistoryHolder();
            phParam.setUserOid(mainUser.getUserOid());

            passwordHistoryMapper.delete(phParam);

            ResetPasswordRequestHistoryHolder rprh = new ResetPasswordRequestHistoryHolder();
            rprh.setLoginId(mainUser.getLoginId());
            resetPasswordRequestHistoryMapper.delete(rprh);
            
            this.delete(mainUser);
            userProfileTmpMapper.delete(tmpUser);
        }
        else
        {
            throw new Exception("Unknow action type.");
        }
    }

    
    @Override
    public void mkReject(CommonParameterHolder cp, UserProfileHolder main,
        UserProfileTmpHolder tmp) throws Exception
    {
        UserProfileTmpHolder tmpUser = tmp;
        UserProfileHolder mainUser = main;
        
        if (DbActionType.CREATE.equals(tmpUser.getActionType()))
        {
            RoleUserTmpHolder ruParam = new RoleUserTmpHolder();
            ruParam.setUserOid(tmpUser.getUserOid());

            roleUserTmpMapper.delete(ruParam);
            
            AllowedAccessCompanyTmpHolder aacParam = new AllowedAccessCompanyTmpHolder();
            aacParam.setUserOid(tmpUser.getUserOid());
            
            allowedAccessCompanyTmpMapper.delete(aacParam);
            
            BuyerStoreUserTmpHolder suParam = new BuyerStoreUserTmpHolder();
            suParam.setUserOid(tmpUser.getUserOid());
            
            buyerStoreUserTmpMapper.delete(suParam);
            
            BuyerStoreAreaUserTmpHolder auParam = new BuyerStoreAreaUserTmpHolder();
            auParam.setUserOid(tmpUser.getUserOid());
            
            buyerStoreAreaUserTmpMapper.delete(auParam);
            
            UserClassTmpHolder ucParam = new UserClassTmpHolder();
            ucParam.setUserOid(tmpUser.getUserOid());
            
            userClassTmpMapper.delete(ucParam);
            
            UserSubclassTmpHolder usParam = new UserSubclassTmpHolder();
            usParam.setUserOid(tmpUser.getUserOid());
            
            userSubclassTmpMapper.delete(usParam);

            GroupUserTmpHolder guParam = new GroupUserTmpHolder();
            guParam.setUserOid(tmpUser.getUserOid());

            groupUserTmpMapper.delete(guParam);

            userProfileTmpMapper.delete(tmpUser);
        }
        else if (DbActionType.UPDATE.equals(tmpUser.getActionType()))
        {
            List<GroupUserHolder> tmpGroupUsers = tmpUser.getGroupUsers();
            BeanUtils.copyProperties(mainUser, tmpUser);
            tmpUser.setCtrlStatus(MkCtrlStatus.REJECTED);
            tmpUser.setActor(cp.getLoginId());
            tmpUser.setActionDate(new Date());

            userProfileTmpMapper.updateByPrimaryKey(tmpUser);

            RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
            ruTmpParam.setUserOid(mainUser.getUserOid());

            roleUserTmpMapper.delete(ruTmpParam);

            List<RoleUserHolder> roleUsers = mainUser.getRoleUsers();
            
            if (null != roleUsers && !roleUsers.isEmpty())
            {
                for (RoleUserHolder ru : roleUsers)
                {
                    RoleUserTmpHolder rut = new RoleUserTmpHolder();
                    rut.setRoleOid(ru.getRoleOid());
                    rut.setUserOid(ru.getUserOid());
                    roleUserTmpMapper.insert(rut);
                }
            }
            
            AllowedAccessCompanyTmpHolder aacParam = new AllowedAccessCompanyTmpHolder();
            aacParam.setUserOid(tmpUser.getUserOid());
            
            allowedAccessCompanyTmpMapper.delete(aacParam);
            
            List<AllowedAccessCompanyHolder> allowedAccessCompanys = mainUser.getAllowedBuyerList();
            
            if (null != allowedAccessCompanys && !allowedAccessCompanys.isEmpty())
            {
                for (AllowedAccessCompanyHolder aac : allowedAccessCompanys)
                {
                    AllowedAccessCompanyTmpHolder aact = new AllowedAccessCompanyTmpHolder();
                    aact.setCompanyOid(aac.getCompanyOid());
                    aact.setUserOid(aac.getUserOid());
                    allowedAccessCompanyTmpMapper.insert(aact);
                }
            }

            BuyerStoreUserTmpHolder suParam = new BuyerStoreUserTmpHolder();
            suParam.setUserOid(mainUser.getUserOid());
            
            buyerStoreUserTmpMapper.delete(suParam);
            
            List<BuyerStoreUserHolder> storeUsers = mainUser.getBuyerStoreUsers();
            
            if (null != storeUsers && !storeUsers.isEmpty())
            {
                for (BuyerStoreUserHolder su : storeUsers)
                {
                    BuyerStoreUserTmpHolder sut = new BuyerStoreUserTmpHolder();
                    sut.setStoreOid(su.getStoreOid());
                    sut.setUserOid(su.getUserOid());
                    buyerStoreUserTmpMapper.insert(sut);
                }
            }
            
            BuyerStoreAreaUserTmpHolder auParam = new BuyerStoreAreaUserTmpHolder();
            auParam.setUserOid(mainUser.getUserOid());
            
            buyerStoreAreaUserTmpMapper.delete(auParam);
            
            List<BuyerStoreAreaUserHolder> areaUsers = mainUser.getBuyerStoreAreaUsers();
            
            if (null != areaUsers && !areaUsers.isEmpty())
            {
                for (BuyerStoreAreaUserHolder au : areaUsers)
                {
                    BuyerStoreAreaUserTmpHolder aut = new BuyerStoreAreaUserTmpHolder();
                    aut.setAreaOid(au.getAreaOid());
                    aut.setUserOid(au.getUserOid());
                    buyerStoreAreaUserTmpMapper.insert(aut);
                }
            }
            
            UserClassTmpHolder ucParam = new UserClassTmpHolder();
            ucParam.setUserOid(mainUser.getUserOid());
            
            userClassTmpMapper.delete(ucParam);
            
            List<UserClassHolder> userClassList = mainUser.getUserClassList();
            
            if (userClassList != null && !userClassList.isEmpty())
            {
                for (UserClassHolder uc : userClassList)
                {
                    UserClassTmpHolder uct = new UserClassTmpHolder();
                    uct.setUserOid(uc.getUserOid());
                    uct.setClassOid(uc.getClassOid());
                    userClassTmpMapper.insert(uct);
                }
            }
            
            UserSubclassTmpHolder usParam = new UserSubclassTmpHolder();
            usParam.setUserOid(mainUser.getUserOid());
            
            userSubclassTmpMapper.delete(usParam);
            
            List<UserSubclassHolder> userSubclassList = mainUser.getUserSubclassList();
            
            if (userSubclassList != null && !userSubclassList.isEmpty())
            {
                for (UserSubclassHolder us : userSubclassList)
                {
                    UserSubclassTmpHolder ust = new UserSubclassTmpHolder();
                    ust.setUserOid(us.getUserOid());
                    ust.setSubclassOid(us.getSubclassOid());
                    userSubclassTmpMapper.insert(ust);
                }
            }
            
            if (tmpGroupUsers != null && !tmpGroupUsers.isEmpty())
            {
                for (GroupUserHolder gu : tmpGroupUsers)
                {
                    GroupUserTmpHolder tmpGut = (GroupUserTmpHolder) gu;
                    if (tmpGut.getApproved() || tmpGut.getLastUpdateFrom().equals(LastUpdateFrom.GROUP))
                    {
                        break;
                    }
                    
                    GroupUserTmpHolder guTmpParam = new GroupUserTmpHolder();
                    guTmpParam.setUserOid(tmpGut.getUserOid());
                    groupUserTmpMapper.delete(guTmpParam);
                    
                    List<GroupUserHolder> groupUsers = mainUser.getGroupUsers();
                    
                    if (null == groupUsers || groupUsers.isEmpty())
                    {
                        break;
                    }
                    for (GroupUserHolder guh : groupUsers)
                    {
                        GroupUserTmpHolder param = new GroupUserTmpHolder();
                        param.setUserOid(guh.getUserOid());
                        param.setGroupOid(guh.getGroupOid());
                        param.setApproved(true);
                        param.setActionType(DbActionType.CREATE);
                        param.setLastUpdateFrom(LastUpdateFrom.USER);
                        
                        groupUserTmpMapper.insert(param);
                    }
                }
            }
        }
        else if (DbActionType.DELETE.equals(tmpUser.getActionType()))
        {
            tmpUser.setCtrlStatus(MkCtrlStatus.REJECTED);
            tmpUser.setActor(cp.getLoginId());
            tmpUser.setActionDate(new Date());

            userProfileTmpMapper.updateByPrimaryKey(tmpUser);
            
            if (tmpUser.getGroupUsers() != null 
                    && !tmpUser.getGroupUsers().isEmpty())
            {
                GroupUserTmpHolder tmpGut = (GroupUserTmpHolder) tmpUser.getGroupUsers().get(0);
                tmpGut.setApproved(true);
                groupUserTmpMapper.updateByPrimaryKey(tmpGut);
            }
        }
        else
        {
            throw new Exception("Unknow action type.");
        }
    }

    
    @Override
    public void mkWithdraw(CommonParameterHolder cp, UserProfileHolder main,
        UserProfileTmpHolder tmp) throws Exception
    {
        UserProfileTmpHolder tmpUser = tmp;
        UserProfileHolder mainUser = main;
        
        if (DbActionType.CREATE.equals(tmpUser.getActionType()))
        {
            RoleUserTmpHolder ruParam = new RoleUserTmpHolder();
            ruParam.setUserOid(tmpUser.getUserOid());

            roleUserTmpMapper.delete(ruParam);
            
            AllowedAccessCompanyTmpHolder aacParam = new AllowedAccessCompanyTmpHolder();
            aacParam.setUserOid(tmpUser.getUserOid());
            
            allowedAccessCompanyTmpMapper.delete(aacParam);

            BuyerStoreUserTmpHolder suParam = new BuyerStoreUserTmpHolder();
            suParam.setUserOid(tmpUser.getUserOid());
            
            buyerStoreUserTmpMapper.delete(suParam);
            
            BuyerStoreAreaUserTmpHolder auParam = new BuyerStoreAreaUserTmpHolder();
            auParam.setUserOid(tmpUser.getUserOid());
            
            buyerStoreAreaUserTmpMapper.delete(auParam);
            
            UserClassTmpHolder ucParam = new UserClassTmpHolder();
            ucParam.setUserOid(tmpUser.getUserOid());
            
            userClassTmpMapper.delete(ucParam);
            
            UserSubclassTmpHolder usParam = new UserSubclassTmpHolder();
            usParam.setUserOid(tmpUser.getUserOid());
            
            userSubclassTmpMapper.delete(usParam);
            
            GroupUserTmpHolder guParam = new GroupUserTmpHolder();
            guParam.setUserOid(tmpUser.getUserOid());

            groupUserTmpMapper.delete(guParam);
            userProfileTmpMapper.delete(tmpUser);
        }
        else if (DbActionType.UPDATE.equals(tmpUser.getActionType()))
        {
            List<GroupUserHolder> tmpGroupUsers = tmpUser.getGroupUsers();
            BeanUtils.copyProperties(mainUser, tmpUser);
            tmpUser.setCtrlStatus(MkCtrlStatus.WITHDRAWN);
            tmpUser.setActor(cp.getLoginId());
            tmpUser.setActionDate(new Date());

            userProfileTmpMapper.updateByPrimaryKey(tmpUser);

            RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
            ruTmpParam.setUserOid(mainUser.getUserOid());
            roleUserTmpMapper.delete(ruTmpParam);
            
            List<RoleUserHolder> roleUsers = mainUser.getRoleUsers();
            
            if (null != roleUsers && !roleUsers.isEmpty())
            {
                for (RoleUserHolder ru : roleUsers)
                {
                    RoleUserTmpHolder rut = new RoleUserTmpHolder();
                    rut.setRoleOid(ru.getRoleOid());
                    rut.setUserOid(ru.getUserOid());
                    roleUserTmpMapper.insert(rut);
                }
            }
            
            AllowedAccessCompanyTmpHolder aacParam = new AllowedAccessCompanyTmpHolder();
            aacParam.setUserOid(tmpUser.getUserOid());
            
            allowedAccessCompanyTmpMapper.delete(aacParam);
            
            List<AllowedAccessCompanyHolder> allowedAccessCompanys = mainUser.getAllowedBuyerList();
            
            if (null != allowedAccessCompanys && !allowedAccessCompanys.isEmpty())
            {
                for (AllowedAccessCompanyHolder aac : allowedAccessCompanys)
                {
                    AllowedAccessCompanyTmpHolder aact = new AllowedAccessCompanyTmpHolder();
                    aact.setCompanyOid(aac.getCompanyOid());
                    aact.setUserOid(aac.getUserOid());
                    allowedAccessCompanyTmpMapper.insert(aact);
                }
            }

            BuyerStoreUserTmpHolder suParam = new BuyerStoreUserTmpHolder();
            suParam.setUserOid(mainUser.getUserOid());
            
            buyerStoreUserTmpMapper.delete(suParam);
            
            List<BuyerStoreUserHolder> storeUsers = mainUser.getBuyerStoreUsers();
            
            if (null != storeUsers && !storeUsers.isEmpty())
            {
                for (BuyerStoreUserHolder su : storeUsers)
                {
                    BuyerStoreUserTmpHolder sut = new BuyerStoreUserTmpHolder();
                    sut.setStoreOid(su.getStoreOid());
                    sut.setUserOid(su.getUserOid());
                    buyerStoreUserTmpMapper.insert(sut);
                }
            }
            
            BuyerStoreAreaUserTmpHolder auParam = new BuyerStoreAreaUserTmpHolder();
            auParam.setUserOid(mainUser.getUserOid());
            
            buyerStoreAreaUserTmpMapper.delete(auParam);
            
            List<BuyerStoreAreaUserHolder> areaUsers = mainUser.getBuyerStoreAreaUsers();
            
            if (null != areaUsers && !areaUsers.isEmpty())
            {
                for (BuyerStoreAreaUserHolder au : areaUsers)
                {
                    BuyerStoreAreaUserTmpHolder aut = new BuyerStoreAreaUserTmpHolder();
                    aut.setAreaOid(au.getAreaOid());
                    aut.setUserOid(au.getUserOid());
                    buyerStoreAreaUserTmpMapper.insert(aut);
                }
            }
            
            UserClassTmpHolder ucParam = new UserClassTmpHolder();
            ucParam.setUserOid(mainUser.getUserOid());
            
            userClassTmpMapper.delete(ucParam);
            
            List<UserClassHolder> userClassList = mainUser.getUserClassList();
            
            if (userClassList != null && !userClassList.isEmpty())
            {
                for (UserClassHolder uc : userClassList)
                {
                    UserClassTmpHolder uct = new UserClassTmpHolder();
                    uct.setUserOid(uc.getUserOid());
                    uct.setClassOid(uc.getClassOid());
                    userClassTmpMapper.insert(uct);
                }
            }
            
            UserSubclassTmpHolder usParam = new UserSubclassTmpHolder();
            usParam.setUserOid(mainUser.getUserOid());
            
            userSubclassTmpMapper.delete(usParam);
            
            List<UserSubclassHolder> userSubclassList = mainUser.getUserSubclassList();
            
            if (userSubclassList != null && !userSubclassList.isEmpty())
            {
                for (UserSubclassHolder us : userSubclassList)
                {
                    UserSubclassTmpHolder ust = new UserSubclassTmpHolder();
                    ust.setUserOid(us.getUserOid());
                    ust.setSubclassOid(us.getSubclassOid());
                    userSubclassTmpMapper.insert(ust);
                }
            }
            
            if (tmpGroupUsers != null && !tmpGroupUsers.isEmpty())
            {
                for (GroupUserHolder gu : tmpGroupUsers)
                {
                    GroupUserTmpHolder tmpGut = (GroupUserTmpHolder) gu;
                    if (tmpGut.getApproved() || tmpGut.getLastUpdateFrom().equals(LastUpdateFrom.GROUP))
                    {
                        break;
                    }
                    
                    GroupUserTmpHolder guTmpParam = new GroupUserTmpHolder();
                    guTmpParam.setUserOid(tmpGut.getUserOid());
                    groupUserTmpMapper.delete(guTmpParam);
                    
                    List<GroupUserHolder> groupUsers = mainUser.getGroupUsers();
                    
                    if (null == groupUsers || groupUsers.isEmpty())
                    {
                        break;
                    }
                    for (GroupUserHolder guh : groupUsers)
                    {
                        GroupUserTmpHolder param = new GroupUserTmpHolder();
                        param.setUserOid(guh.getUserOid());
                        param.setGroupOid(guh.getGroupOid());
                        param.setApproved(true);
                        param.setActionType(DbActionType.CREATE);
                        param.setLastUpdateFrom(LastUpdateFrom.USER);
                        
                        groupUserTmpMapper.insert(param);
                    }
                }
            }
            
            
        }
        else if (DbActionType.DELETE.equals(tmpUser.getActionType()))
        {
            tmpUser.setCtrlStatus(MkCtrlStatus.WITHDRAWN);
            tmpUser.setActor(cp.getLoginId());
            tmpUser.setActionDate(new Date());

            userProfileTmpMapper.updateByPrimaryKey(tmpUser);
           
            if (tmpUser.getGroupUsers() != null 
                    && !tmpUser.getGroupUsers().isEmpty())
            {
                GroupUserTmpHolder tmpGut = (GroupUserTmpHolder) tmpUser.getGroupUsers().get(0);
                tmpGut.setApproved(true);
                groupUserTmpMapper.updateByPrimaryKey(tmpGut);
            }
        }
        else
        {
            throw new Exception("Unknow action type.");
        }
    }

    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private UserProfileService getMeBean()
    {
        return ctx.getBean("userProfileService", UserProfileService.class);
    }

    
    @Override
    public void updateMyProfile(CommonParameterHolder cp,
            UserProfileHolder oldUser, UserProfileTmpHolder newUser)
            throws Exception
    {
        getMeBean().auditUpdateByPrimaryKey(cp, oldUser, newUser);
        userProfileTmpMapper.updateByPrimaryKeySelective(newUser);
        
        PasswordHistoryHolder pwdHis_ = new PasswordHistoryHolder();
        pwdHis_.setUserOid(newUser.getUserOid());
        pwdHis_.setChangeDate(new java.util.Date());
        pwdHis_.setOldLoginPwd(newUser.getLoginPwd());
        pwdHis_.setActor(newUser.getLoginId());
        pwdHis_.setChangeReason(CODE_PASSWORD_EXPIRED);

        passwordHistoryService.insert(pwdHis_);
        
        if (newUser.getUserType().equals(BigDecimal.valueOf(2)) || newUser.getUserType().equals(BigDecimal.valueOf(4)))
        {
            favouriteListService.deleteFavouriteListByUserOid(newUser.getUserOid());
            List<FavouriteListExHolder> newFavouriteLists = newUser.getFavouriteLists();
            
            if (newFavouriteLists != null && !newFavouriteLists.isEmpty())
            {
                for (FavouriteListExHolder holder : newFavouriteLists)
                {
                    if (holder == null)
                    {
                        continue;
                    }
                    holder.setListOid(oidService.getOid());
                    holder.setUserOid(newUser.getUserOid());
                    favouriteListService.insert(holder);
                    List<BigDecimal> selectedSupplierOids = holder.getSelectedSupplierOids();
                    if (selectedSupplierOids == null || selectedSupplierOids.isEmpty())
                    {
                        continue;
                    }
                    for (BigDecimal supplierOid : selectedSupplierOids)
                    {
                        FavouriteListSupplierHolder param = new FavouriteListSupplierHolder();
                        param.setSupplierOid(supplierOid);
                        param.setListOid(holder.getListOid());
                        favouriteListSupplierService.insert(param);
                    }
                }
            }
        }
        
    }


    @Override
    public List<UserProfileHolder> selectUsersByTmpGroupOid(BigDecimal groupOid)
        throws Exception
    {
        if (null == groupOid)
        {
            throw new IllegalArgumentException();
        }
        return this.mapper.selectUsersByTmpGroupOid(groupOid);
    }


    @Override
    public List<UserProfileHolder> selectUsersByGroupOid(BigDecimal groupOid)
        throws Exception
    {
        if (null == groupOid)
        {
            throw new IllegalArgumentException();
        }
        
        return this.mapper.selectUsersByGroupOid(groupOid);
    }
    

    @Override
    public List<UserProfileExHolder> selectUsersByStoreOidAndUserTypes(
            BigDecimal storeOid, List<BigDecimal> userTypes) throws Exception
    {
        if (null == storeOid || null == userTypes || userTypes.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(USER_TYPE_LIST, userTypes);
        param.put("storeOid", storeOid);
        
        return this.mapper.selectUsersByStoreOidAndUserTypes(param);
    }
    

    @Override
    public List<UserProfileExHolder> selectUsersByTmpStoreOidAndUserTypes(
            BigDecimal storeOid, List<BigDecimal> userTypes) throws Exception
    {
        if (null == storeOid || null == userTypes || userTypes.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(USER_TYPE_LIST, userTypes);
        param.put("storeOid", storeOid);
        
        return this.mapper.selectUsersByTmpStoreOidAndUserTypes(param);
    }
    

    @Override
    public List<UserProfileExHolder> selectUsersByAreaOidAndUserTypes(
            BigDecimal areaOid, List<BigDecimal> userTypes) throws Exception
    {
        if (null == areaOid || null == userTypes || userTypes.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(USER_TYPE_LIST, userTypes);
        param.put("areaOid", areaOid);
        
        return this.mapper.selectUsersByAreaOidAndUserTypes(param);
    }


    @Override
    public List<UserProfileExHolder> selectUsersByTmpAreaOidAndUserTypes(
            BigDecimal areaOid, List<BigDecimal> userTypes) throws Exception
    {
        if (null == areaOid || null == userTypes || userTypes.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(USER_TYPE_LIST, userTypes);
        param.put("areaOid", areaOid);
        
        return this.mapper.selectUsersByTmpAreaOidAndUserTypes(param);
    }

    
    private Map<DbActionType, GroupUserTmpHolder> parseGroupUserListToMap(List<GroupUserHolder> groupUsers)
    {
        Map<DbActionType, GroupUserTmpHolder> rlt = new HashMap<DbActionType, GroupUserTmpHolder>();
        for (Object o : groupUsers)
        {
            GroupUserTmpHolder gut = (GroupUserTmpHolder) o;
            rlt.put(gut.getActionType(), gut);
        }
        return rlt;
    }


    @Override
    public List<UserProfileExHolder> selectUsersByBuyerAndUserType(
            BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception
    {
        if (null == userTypeOid || null == buyerOid)
        {
            throw new IllegalArgumentException();
        }
        Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("userTypeOid", userTypeOid);
        param.put("buyerOid", buyerOid);
        return this.mapper.selectUsersByBuyerAndUserType(param);
    }
    
    
    @Override
    public List<UserProfileExHolder> selectUsersByBuyerAndUserTypes(
            BigDecimal buyerOid, List<BigDecimal> userTypes) throws Exception
            {
        if (null == userTypes || null == buyerOid)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userTypes", userTypes);
        param.put("buyerOid", buyerOid);
        return this.mapper.selectUsersByBuyerAndUserTypes(param);
    }


    @Override
    public void importUsers(List<UserProfileTmpExHolder> users, List<UserProfileTmpExHolder> updateUsers,
            MsgTransactionsHolder msg, TransactionBatchHolder batch) throws Exception
    {
        if (users != null && !users.isEmpty())
        {
            for (UserProfileTmpExHolder user : users)
            {
                UserProfileTmpHolder userTmp = new UserProfileTmpHolder();
                BeanUtils.copyProperties(user, userTmp);
                //userTmp.setLoginPwd(EncodeUtil.getInstance().computePwd(user.getLoginPwd(), user.getUserOid()));
                userTmp.setActor("System");
                userTmp.setActionType(DbActionType.CREATE);
                userTmp.setActionDate(new Date());
                userTmp.setCtrlStatus(MkCtrlStatus.APPROVED);
                userProfileTmpMapper.insert(userTmp);
                mapper.insert(userTmp);
                
                List<RoleUserHolder> roleUsers = user.getRoleUsers();
                if (roleUsers != null && !roleUsers.isEmpty())
                {
                    for (RoleUserHolder roleUser : roleUsers)
                    {
                        RoleUserTmpHolder roleUserTmp = new RoleUserTmpHolder();
                        BeanUtils.copyProperties(roleUser, roleUserTmp);
                        roleUserTmpMapper.insert(roleUserTmp);
                        roleUserMapper.insert(roleUserTmp);
                    }
                }
                
                List<GroupUserHolder> groupUsers = user.getGroupUsers();
                if (groupUsers != null && !groupUsers.isEmpty())
                {
                    for (GroupUserHolder groupUser : groupUsers)
                    {
                        GroupUserTmpHolder groupUserTmp = new GroupUserTmpHolder();
                        BeanUtils.copyProperties(groupUser, groupUserTmp);
                        groupUserTmp.setLastUpdateFrom(LastUpdateFrom.USER);
                        groupUserTmp.setApproved(true);
                        groupUserTmp.setActionType(DbActionType.CREATE);
                        groupUserTmpMapper.insert(groupUserTmp);
                        groupUserMapper.insert(groupUserTmp);
                    }
                }
                
                List<BuyerStoreUserHolder> buyerStoreUsers = user.getBuyerStoreUsers();
                if (buyerStoreUsers != null && !buyerStoreUsers.isEmpty())
                {
                    for (BuyerStoreUserHolder bsu : buyerStoreUsers)
                    {
                        BuyerStoreUserTmpHolder bsut = new BuyerStoreUserTmpHolder();
                        BeanUtils.copyProperties(bsu, bsut);
                        buyerStoreUserTmpMapper.insert(bsut);
                        buyerStoreUserMapper.insert(bsu);
                    }
                }
                
                List<BuyerStoreUserHolder> buyerWarehouseUsers = user.getBuyerWareHouseUsers();
                if (buyerWarehouseUsers != null && !buyerWarehouseUsers.isEmpty())
                {
                    for (BuyerStoreUserHolder bsu : buyerWarehouseUsers)
                    {
                        BuyerStoreUserTmpHolder bsut = new BuyerStoreUserTmpHolder();
                        BeanUtils.copyProperties(bsu, bsut);
                        buyerStoreUserTmpMapper.insert(bsut);
                        buyerStoreUserMapper.insert(bsu);
                    }
                }
                
                List<BuyerStoreAreaUserHolder> buyerStoreAreaUsers = user.getBuyerStoreAreaUsers();
                if (buyerStoreAreaUsers != null && !buyerStoreAreaUsers.isEmpty())
                {
                    for (BuyerStoreAreaUserHolder bsau : buyerStoreAreaUsers)
                    {
                        BuyerStoreAreaUserTmpHolder bsaut = new BuyerStoreAreaUserTmpHolder();
                        BeanUtils.copyProperties(bsau, bsaut);
                        buyerStoreAreaUserTmpMapper.insert(bsaut);
                        buyerStoreAreaUserMapper.insert(bsaut);
                    }
                }
                
                List<AllowedAccessCompanyHolder> allowedBuyerList = user.getAllowedBuyerList();
                if (allowedBuyerList != null && !allowedBuyerList.isEmpty())
                {
                    for (AllowedAccessCompanyHolder aac : allowedBuyerList)
                    {
                        AllowedAccessCompanyTmpHolder aact = new AllowedAccessCompanyTmpHolder();
                        BeanUtils.copyProperties(aac, aact);
                        allowedAccessCompanyTmpMapper.insert(aact);
                        allowedAccessCompanyMapper.insert(aact);
                    }
                }
                
                List<UserClassHolder> userClassList = user.getUserClassList();
                if (userClassList != null && !userClassList.isEmpty())
                {
                    for (UserClassHolder uc : userClassList)
                    {
                        UserClassTmpHolder uct = new UserClassTmpHolder();
                        BeanUtils.copyProperties(uc, uct);
                        userClassTmpMapper.insert(uct);
                        userClassMapper.insert(uct);
                    }
                }
                
                List<UserSubclassHolder> userSubclassList = user.getUserSubclassList();
                if (userSubclassList != null && !userSubclassList.isEmpty())
                {
                    for (UserSubclassHolder us : userSubclassList)
                    {
                        UserSubclassTmpHolder ust = new UserSubclassTmpHolder();
                        BeanUtils.copyProperties(us, ust);
                        userSubclassTmpMapper.insert(ust);
                        userSubclassMapper.insert(ust);
                    }
                }
            }
        }
        if (updateUsers != null && !updateUsers.isEmpty())
        {
            for (UserProfileTmpExHolder user : updateUsers)
            {
                userProfileTmpMapper.updateByPrimaryKeySelective(user);
                mapper.updateByPrimaryKeySelective(user);
                
                RoleUserTmpHolder roleUserParam = new RoleUserTmpHolder();
                roleUserParam.setUserOid(user.getUserOid());
                roleUserTmpMapper.delete(roleUserParam);
                roleUserMapper.delete(roleUserParam);
                List<RoleUserHolder> roleUsers = user.getRoleUsers();
                if (roleUsers != null && !roleUsers.isEmpty())
                {
                    for (RoleUserHolder roleUser : roleUsers)
                    {
                        RoleUserTmpHolder roleUserTmp = new RoleUserTmpHolder();
                        BeanUtils.copyProperties(roleUser, roleUserTmp);
                        roleUserTmp.setUserOid(user.getUserOid());
                        roleUserTmpMapper.insert(roleUserTmp);
                        roleUserMapper.insert(roleUserTmp);
                    }
                }
                
                GroupUserTmpHolder groupUserParam = new GroupUserTmpHolder();
                groupUserParam.setUserOid(user.getUserOid());
                groupUserTmpMapper.delete(groupUserParam);
                groupUserMapper.delete(groupUserParam);
                List<GroupUserHolder> groupUsers = user.getGroupUsers();
                if (groupUsers != null && !groupUsers.isEmpty())
                {
                    for (GroupUserHolder groupUser : groupUsers)
                    {
                        GroupUserTmpHolder groupUserTmp = new GroupUserTmpHolder();
                        BeanUtils.copyProperties(groupUser, groupUserTmp);
                        groupUserTmp.setUserOid(user.getUserOid());
                        groupUserTmp.setLastUpdateFrom(LastUpdateFrom.USER);
                        groupUserTmp.setApproved(true);
                        groupUserTmp.setActionType(DbActionType.CREATE);
                        groupUserTmpMapper.insert(groupUserTmp);
                        groupUserMapper.insert(groupUserTmp);
                    }
                }
                
                BuyerStoreUserTmpHolder storeUserParam = new BuyerStoreUserTmpHolder();
                storeUserParam.setUserOid(user.getUserOid());
                buyerStoreUserTmpMapper.delete(storeUserParam);
                buyerStoreUserMapper.delete(storeUserParam);
                List<BuyerStoreUserHolder> buyerStoreUsers = user.getBuyerStoreUsers();
                if (buyerStoreUsers != null && !buyerStoreUsers.isEmpty())
                {
                    for (BuyerStoreUserHolder bsu : buyerStoreUsers)
                    {
                        BuyerStoreUserTmpHolder bsut = new BuyerStoreUserTmpHolder();
                        BeanUtils.copyProperties(bsu, bsut);
                        bsut.setUserOid(user.getUserOid());
                        buyerStoreUserTmpMapper.insert(bsut);
                        buyerStoreUserMapper.insert(bsut);
                    }
                }
                
                List<BuyerStoreUserHolder> buyerWarehouseUsers = user.getBuyerWareHouseUsers();
                if (buyerWarehouseUsers != null && !buyerWarehouseUsers.isEmpty())
                {
                    for (BuyerStoreUserHolder bsu : buyerWarehouseUsers)
                    {
                        BuyerStoreUserTmpHolder bsut = new BuyerStoreUserTmpHolder();
                        BeanUtils.copyProperties(bsu, bsut);
                        bsut.setUserOid(user.getUserOid());
                        buyerStoreUserTmpMapper.insert(bsut);
                        buyerStoreUserMapper.insert(bsut);
                    }
                }
                
                BuyerStoreAreaUserTmpHolder areaUserParam = new BuyerStoreAreaUserTmpHolder();
                areaUserParam.setUserOid(user.getUserOid());
                buyerStoreAreaUserTmpMapper.delete(areaUserParam);
                buyerStoreAreaUserMapper.delete(areaUserParam);
                List<BuyerStoreAreaUserHolder> buyerStoreAreaUsers = user.getBuyerStoreAreaUsers();
                if (buyerStoreAreaUsers != null && !buyerStoreAreaUsers.isEmpty())
                {
                    for (BuyerStoreAreaUserHolder bsau : buyerStoreAreaUsers)
                    {
                        BuyerStoreAreaUserTmpHolder bsaut = new BuyerStoreAreaUserTmpHolder();
                        BeanUtils.copyProperties(bsau, bsaut);
                        bsaut.setUserOid(user.getUserOid());
                        buyerStoreAreaUserTmpMapper.insert(bsaut);
                        buyerStoreAreaUserMapper.insert(bsaut);
                    }
                }
                
                AllowedAccessCompanyTmpHolder aactParam = new AllowedAccessCompanyTmpHolder();
                aactParam.setUserOid(user.getUserOid());
                allowedAccessCompanyTmpMapper.delete(aactParam);
                allowedAccessCompanyMapper.delete(aactParam);
                List<AllowedAccessCompanyHolder> allowedBuyerList = user.getAllowedBuyerList();
                if (allowedBuyerList != null && !allowedBuyerList.isEmpty())
                {
                    for (AllowedAccessCompanyHolder aac : allowedBuyerList)
                    {
                        AllowedAccessCompanyTmpHolder aact = new AllowedAccessCompanyTmpHolder();
                        BeanUtils.copyProperties(aac, aact);
                        aact.setUserOid(user.getUserOid());
                        allowedAccessCompanyTmpMapper.insert(aact);
                        allowedAccessCompanyMapper.insert(aact);
                    }
                }
                
                UserClassTmpHolder uctParam = new UserClassTmpHolder();
                uctParam.setUserOid(user.getUserOid());
                userClassTmpMapper.delete(uctParam);
                userClassMapper.delete(uctParam);
                List<UserClassHolder> userClassList = user.getUserClassList();
                if (userClassList != null && !userClassList.isEmpty())
                {
                    for (UserClassHolder uc : userClassList)
                    {
                        UserClassTmpHolder uct = new UserClassTmpHolder();
                        BeanUtils.copyProperties(uc, uct);
                        uct.setUserOid(user.getUserOid());
                        userClassTmpMapper.insert(uct);
                        userClassMapper.insert(uct);
                    }
                }
                
                UserSubclassTmpHolder ustParam = new UserSubclassTmpHolder();
                ustParam.setUserOid(user.getUserOid());
                userSubclassTmpMapper.delete(ustParam);
                userSubclassMapper.delete(ustParam);
                List<UserSubclassHolder> userSubclassList = user.getUserSubclassList();
                if (userSubclassList != null && !userSubclassList.isEmpty())
                {
                    for (UserSubclassHolder us : userSubclassList)
                    {
                        UserSubclassTmpHolder ust = new UserSubclassTmpHolder();
                        BeanUtils.copyProperties(us, ust);
                        ust.setUserOid(user.getUserOid());
                        userSubclassTmpMapper.insert(ust);
                        userSubclassMapper.insert(ust);
                    }
                }
                
            }
        }
        if (batch != null)
        {
            transactionBatchService.insert(batch);
        }
        if (msg != null)
        {
            msgTransactionsService.insert(msg);
        }
    }
    

    public void resetSupplierAdminPwd(List<UserProfileHolder> supplierAdmins,
            List<SupplierAdminRolloutHolder> supplierAdminRolloutList,
            TransactionBatchHolder batch, MsgTransactionsHolder msg,
            Map<BigDecimal, String> userMsgEmailMap) throws Exception
    {
        if (supplierAdmins == null || supplierAdmins.isEmpty()
                || supplierAdminRolloutList == null
                || supplierAdminRolloutList.isEmpty() || batch == null
                || msg == null)
        {
            throw new IllegalArgumentException();
        }
        transactionBatchService.insert(batch);
        msgTransactionsService.insert(msg);
        for (UserProfileHolder user : supplierAdmins)
        {
            UserProfileTmpHolder tmpUser = new UserProfileTmpHolder();
            tmpUser.setUserOid(user.getUserOid());
            List<UserProfileTmpHolder> users = userProfileTmpMapper.select(tmpUser);
            tmpUser = users.get(0);
            tmpUser.setLoginPwd(EncodeUtil.getInstance().computePwd(user.getLoginPwd(), user.getUserOid()));
            tmpUser.setLastResetPwdDate(new Date());
            tmpUser.setEmail(user.getEmail());
            
            userProfileTmpMapper.updateByPrimaryKeySelective(tmpUser);
            mapper.updateByPrimaryKeySelective(tmpUser);
            
            String msgEmail = userMsgEmailMap.get(tmpUser.getUserOid());
            if (msgEmail != null && !msgEmail.trim().isEmpty())
            {
                supplierMsgSettingService.updateEmailAddressBySupplierOid(tmpUser.getSupplierOid(), userMsgEmailMap.get(tmpUser.getUserOid()));
            }
        }
        
        for (SupplierAdminRolloutHolder sar : supplierAdminRolloutList)
        {
            SupplierAdminRolloutHolder oldSar = supplierAdminRolloutService.selectByKey(sar.getSupplierOid());
            if (oldSar == null)
            {
                supplierAdminRolloutService.insert(sar);
            }
            else
            {
                supplierAdminRolloutService.updateByPrimaryKey(oldSar, sar);
            }
        }
    }


    @Override
    public List<UserProfileExHolder> selectPriceAuditUsersByMatchingOid(
            BigDecimal matchingOid) throws Exception
    {
        if (matchingOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectPriceAuditUsersByMatchingOid(matchingOid);
    }


    @Override
    public List<UserProfileExHolder> selectQtyAuditUsersByMatchingOid(
            BigDecimal matchingOid) throws Exception
    {
        if (matchingOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectQtyAuditUsersByMatchingOid(matchingOid);
    }
    
    
    @Override
    public List<UserProfileExHolder> selectCanCloseUsersByMatchingOid(
            BigDecimal matchingOid) throws Exception
    {
        if (matchingOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectCanCloseUsersByMatchingOid(matchingOid);
    }


    @Override
    public List<UserProfileExHolder> selectPriceAuditUsersByDnOid(
            BigDecimal dnOid) throws Exception
    {
        if (dnOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectPriceAuditUsersByDnOid(dnOid);
    }


    @Override
    public List<UserProfileExHolder> selectQtyAuditUsersByDnOid(
            BigDecimal dnOid) throws Exception
    {
        if (dnOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectQtyAuditUsersByDnOid(dnOid);
    }
    
    
    @Override
    public List<UserProfileExHolder> selectCanCloseUsersByDnOid(
            BigDecimal dnOid) throws Exception
    {
        if (dnOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectCanCloseUsersByDnOid(dnOid);
    }


    @Override
    public List<UserProfileExHolder> selectStoreUsersByBuyerOidAndStoreOid(BigDecimal buyerOid, String storeCode)
            throws Exception
    {
        if (buyerOid == null || storeCode == null || storeCode.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        
        BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
        if (buyer == null || buyer.getBuyerCode() == null || buyer.getBuyerCode().trim().isEmpty())
        {
            throw new IllegalArgumentException("can not obtain the buyer with oid " + buyerOid);
        }
        
        BuyerStoreHolder store = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(buyer.getBuyerCode(), storeCode);
        
        if (store == null || store.getStoreOid() == null)
        {
            throw new IllegalArgumentException("can not obtain the store with buyer code [" + buyer.getBuyerCode() + "] and storeCode [" + storeCode + "]");
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("storeOid", store.getStoreOid());
        map.put("buyerOid", buyerOid);
        return mapper.selectStoreUsersByBuyerOidAndStoreOid(map);
    }
    
    
    @Override
    public List<UserProfileExHolder> selectWarehouseUsersByBuyerOidAndStoreOid(BigDecimal buyerOid, String storeCode)
            throws Exception
    {
        if (buyerOid == null || storeCode == null || storeCode.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        
        BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
        if (buyer == null || buyer.getBuyerCode() == null || buyer.getBuyerCode().trim().isEmpty())
        {
            throw new IllegalArgumentException("can not obtain the buyer with oid " + buyerOid);
        }
        
        BuyerStoreHolder store = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(buyer.getBuyerCode(), storeCode);
        
        if (store == null || store.getStoreOid() == null)
        {
            throw new IllegalArgumentException("can not obtain the store with buyer code [" + buyer.getBuyerCode() + "] and storeCode [" + storeCode + "]");
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("storeOid", store.getStoreOid());
        map.put("buyerOid", buyerOid);
        return mapper.selectWarehouseUsersByBuyerOidAndStoreOid(map);
    }


    @Override
    public List<UserProfileHolder> selectUsersBySupplierOid(
            BigDecimal supplierOid) throws Exception
    {
        UserProfileHolder param = new UserProfileHolder();
        param.setSupplierOid(supplierOid);
        return mapper.select(param);
    }
    
}
