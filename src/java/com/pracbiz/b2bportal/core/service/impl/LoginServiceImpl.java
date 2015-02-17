package com.pracbiz.b2bportal.core.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.AccessActionType;
import com.pracbiz.b2bportal.core.constants.PrincipalType;
import com.pracbiz.b2bportal.core.holder.AuditAccessHolder;
import com.pracbiz.b2bportal.core.holder.AuditSessionHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.OperationUrlHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserSessionHolder;
import com.pracbiz.b2bportal.core.holder.extension.ModuleExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.ModuleMapper;
import com.pracbiz.b2bportal.core.mapper.OperationUrlMapper;
import com.pracbiz.b2bportal.core.service.AuditAccessService;
import com.pracbiz.b2bportal.core.service.AuditSessionService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.LoginService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserSessionService;

public class LoginServiceImpl implements LoginService
{
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private OperationUrlMapper operationUrlMapper;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private AuditAccessService auditAccessService;
    @Autowired
    private AuditSessionService auditSessionService;
    @Autowired 
    private OidService oidService;
    @Autowired
    GroupService groupService;
    

    @Override
    public List<ModuleExHolder> selectMenusByUserOid(BaseAction action, BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        List<ModuleExHolder> userMdls = null;
        
        GroupHolder curUserGroup = groupService.selectGroupByUserOid(userOid);
        
        if (null == curUserGroup)
        {
            userMdls = moduleMapper.selectModulesByUser(userOid);
        }
        else
        {
            userMdls = moduleMapper.selectModulesByGroup(curUserGroup.getGroupOid());
        }
        
        
        List<ModuleExHolder> firstLevelMdls = this.getFirstLevelMdls(userMdls);
        
        if (firstLevelMdls == null || firstLevelMdls.isEmpty())
        {
            return null;
        }
        
        List<ModuleExHolder> menus = new ArrayList<ModuleExHolder>();
        for (ModuleExHolder parentModule : firstLevelMdls)
        {
            if (StringUtils.isBlank(parentModule.getModuleTitleKey()))
            {
                parentModule.setModuleTitleAftKey(parentModule.getModuleName());
            }
            else
            {
                parentModule.setModuleTitleAftKey(
                        action.getText(parentModule.getModuleTitleKey()));
            }
            
            List<ModuleExHolder> childModules = new ArrayList<ModuleExHolder>();
            for(ModuleExHolder userModule : userMdls)
            {
                
                if (userModule.getModuleLevel() == 1)
                {
                    continue;
                }
                
                ModuleExHolder childModule = (ModuleExHolder) userModule;
                
                if (parentModule.getModuleId().equals(childModule.getParentId()))
                {
                    if (StringUtils.isBlank(childModule.getModuleTitleKey()))
                    {
                        childModule.setModuleTitleAftKey(childModule.getModuleName());
                    }
                    else
                    {
                        childModule.setModuleTitleAftKey(
                                action.getText(childModule.getModuleTitleKey()));
                    }
                    
                    childModules.add(childModule);
                }
            }
            
            if (!childModules.isEmpty())
            {
                parentModule.setChildModules(childModules);
            }
            
            menus.add(parentModule);
        }
        
        return menus;
    }


    @Override
    public List<OperationUrlHolder> selectOperationUrlsByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        GroupHolder curUserGroup = groupService.selectGroupByUserOid(userOid);
        
        if (null == curUserGroup)
        {
            return operationUrlMapper.selectOperationUrlByUser(userOid);
        }
        
        return operationUrlMapper.selectOperationUrlByGroup(curUserGroup.getGroupOid());
    }
    
    
    @Override
    public void doLogin(CommonParameterHolder cp,BaseHolder newObj_,
            BaseHolder oldObj_, String sessionId_)
            throws Exception
    {
        if(!(newObj_ instanceof UserProfileHolder))
        {
            throw new IllegalArgumentException();
        }
        
        if(!(oldObj_ instanceof UserProfileHolder))
        {
            throw new IllegalArgumentException();
        }
        
        UserProfileHolder newUser_ = (UserProfileHolder)newObj_;
        UserProfileTmpExHolder newUserTmpEx = new UserProfileTmpExHolder();
        BeanUtils.copyProperties(newUser_, newUserTmpEx);
        
        UserProfileHolder oldUser_ = (UserProfileHolder)oldObj_;
        UserProfileTmpExHolder oldUserTmpEx = new UserProfileTmpExHolder();
        BeanUtils.copyProperties(oldUser_, oldUserTmpEx);
        
        userProfileService.updateByPrimaryKeySelective(oldUserTmpEx, newUserTmpEx);
        
        //add user session
        UserSessionHolder us_ = new UserSessionHolder();
        us_.setUserOid(newUser_.getUserOid());
        us_.setSessionId(sessionId_);
        us_.setCreateDate(new java.util.Date());
        
        userSessionService.insert(us_);
        
        AuditAccessHolder auditAccess = new AuditAccessHolder();
        auditAccess.setAccessOid(oidService.getOid());
        auditAccess.setPrincipalType(PrincipalType.USER);
        auditAccess.setLoginId(newUser_.getLoginId());
        auditAccess.setPrincipalOid(newUser_.getUserOid());
        auditAccess.setAttemptNo(0);
        auditAccess.setClientIp(cp.getClientIp());
        auditAccess.setSuccess(true);
        auditAccess.setActionType(AccessActionType.IN);
        auditAccess.setActionDate(new Date());
        auditAccess.setUserTypeOid(newUser_.getUserType());
        auditAccess.setCompanyOid(newUser_.getBuyerOid() == null ? 
                (newUser_.getSupplierOid() == null ? null : newUser_.getSupplierOid()) : newUser_.getBuyerOid());
        auditAccessService.insert(auditAccess);
        
        AuditSessionHolder auditSession = new AuditSessionHolder();
        auditSession.setSessionOid(oidService.getOid());
        Date startDate = new Date(); 
        auditSession.setSessionId(DateUtil.getInstance().convertDateToString(
            startDate, DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC));
        auditSession.setStartDate(startDate);
        auditSession.setUserOid(newUser_.getUserOid());
        auditSession.setLoginId(newUser_.getLoginId());
        auditSessionService.insert(auditSession);
        cp.setSessionOid(auditSession.getSessionOid());
    }

    


    /** 
     * {@inheritDoc}
     * @author jiangming
     * @see com.pracbiz.b2bportal.core.service.LoginService#selectgetPermitURLsByUserOid(java.math.BigDecimal, java.util.List)
     */
    @Override
    public List<String> selectgetPermitURLsByUserOid(BigDecimal userOid,
        List<ModuleExHolder> modules) throws Exception
    {
        List<OperationUrlHolder> operations = this.selectOperationUrlsByUserOid(userOid);
        
        if (operations == null || operations.isEmpty())
        {
            return null;
        }
        
        List<String> permitUrls = new ArrayList<String>();
        for (OperationUrlHolder operation : operations)
        {
            permitUrls.add(operation.getAccessUrl());
        }
        
        if (modules == null || modules.isEmpty())
        {
            return permitUrls;
        }
        
        //get all available modules url;
        for (ModuleExHolder module : modules)
        {
            if (StringUtils.isBlank(module.getModuleLink()))
            {
                continue;
            }
            
            permitUrls.add(module.getModuleLink());
        }
        
        
        return permitUrls;
    }

    
    
    private List<ModuleExHolder> getFirstLevelMdls(List<ModuleExHolder> modules)
    {
        if (modules == null || modules.isEmpty())
        {
            throw new IllegalArgumentException();
        }
         
        List<ModuleExHolder> rlts = new ArrayList<ModuleExHolder>();
        for (ModuleExHolder mdl : modules)
        {
            if (mdl.getModuleLevel() == 1)
            {
                rlts.add(mdl);
            }
        }
        
        return rlts;
    }


    /** 
     * {@inheritDoc}
     * @author jiangming
     * @see com.pracbiz.b2bportal.core.service.LoginService#doLogout(com.pracbiz.b2bportal.base.holder.CommonParameterHolder, com.pracbiz.b2bportal.base.holder.BaseHolder, com.pracbiz.b2bportal.base.holder.BaseHolder, java.lang.String, java.lang.String)
     */
    @Override
    public void doLogout(CommonParameterHolder cp,UserProfileHolder user, String sessionId) throws Exception
    {
        UserSessionHolder userSession = new UserSessionHolder();
        userSession.setSessionId(sessionId);
        userSessionService.delete(userSession);
        
        if (cp == null ) return;

        AuditAccessHolder auditAccess = new AuditAccessHolder();
        auditAccess.setAccessOid(oidService.getOid());
        auditAccess.setPrincipalType(PrincipalType.USER);
        auditAccess.setPrincipalOid(user.getUserOid());
        auditAccess.setLoginId(user.getLoginId());
        auditAccess.setClientIp(cp.getClientIp());
        auditAccess.setSuccess(true);
        auditAccess.setActionType(AccessActionType.OUT);
        auditAccess.setActionDate(new Date());
        auditAccess.setAttemptNo(0);
        auditAccess.setUserTypeOid(user.getUserType());
        auditAccess.setCompanyOid(user.getBuyerOid() == null ? 
                (user.getSupplierOid() == null ? null : user.getSupplierOid()) : user.getBuyerOid());
        auditAccessService.insert(auditAccess);
        
        AuditSessionHolder auditSession = this.auditSessionService.selectAuditSessionByKey(cp.getSessionOid());
        auditSession.setEndDate(new Date());
        
        this.auditSessionService.updateByPrimaryKey(null, auditSession);
        
    }

}
