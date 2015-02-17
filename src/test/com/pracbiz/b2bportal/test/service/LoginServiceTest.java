//*****************************************************************************
//
// File Name       :  LoginServiceTest.java
// Date Created    :  Oct 8, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Oct 8, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.core.action.LoginAction;
import com.pracbiz.b2bportal.core.constants.AccessActionType;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.holder.AuditAccessHolder;
import com.pracbiz.b2bportal.core.holder.AuditSessionHolder;
import com.pracbiz.b2bportal.core.holder.OperationUrlHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserSessionHolder;
import com.pracbiz.b2bportal.core.holder.extension.ModuleExHolder;
import com.pracbiz.b2bportal.core.mapper.RoleUserMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserTmpMapper;
import com.pracbiz.b2bportal.core.service.AuditAccessService;
import com.pracbiz.b2bportal.core.service.AuditSessionService;
import com.pracbiz.b2bportal.core.service.LoginService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.RoleOperationService;
import com.pracbiz.b2bportal.core.service.RoleOperationTmpService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.service.UserSessionService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class LoginServiceTest extends BaseServiceTestCase
{
    private LoginService loginService;
    private UserProfileService userProfileService;
    private UserProfileTmpService userProfileTmpService;
    private RoleService roleService;
    private RoleTmpService roleTmpService;
    private RoleOperationService roleOperationService;
    private RoleOperationTmpService roleOperationTmpService;
    private RoleUserMapper roleUserMapper;
    private RoleUserTmpMapper roleUserTmpMapper;
    private UserSessionService userSessionService;
    private AuditAccessService auditAccessService;
    private AuditSessionService auditSessionService;
    
    List<RoleOperationHolder> roleOperations;
    List<RoleUserHolder> ruList;
    private UserProfileTmpHolder admin;
    private RoleTmpHolder role;
    private UserSessionHolder session;
    private AuditAccessHolder auditAccess;
    private AuditSessionHolder auditSession;
    
    private static final String CREATOR = "JunitTester";
    private static final String IP = "localhost";
    private static final String TEST_SESSION_ID = "testSessionId";
    
    public void testSelectMenusByUserOid() throws Exception
    {
        List<ModuleExHolder> rlt = loginService.selectMenusByUserOid(new LoginAction(), admin.getUserOid());
        assertNotNull(rlt);
        assertEquals(1, rlt.size());
    }
    
    
    public void testSelectOperationUrlsByUserOid() throws Exception
    {
        List<OperationUrlHolder> rlt = loginService.selectOperationUrlsByUserOid(admin.getUserOid());
        assertNotNull(rlt);
        assertEquals(roleOperations.size(), rlt.size());
    }
    
    
    public void testSelectgetPermitURLsByUserOid() throws Exception
    {
        List<ModuleExHolder> modules = loginService.selectMenusByUserOid(new LoginAction(), admin.getUserOid());
        List<String> rlt = loginService.selectgetPermitURLsByUserOid(admin.getUserOid(), modules);
        assertNotNull(rlt);
        assertEquals(4, rlt.size());
    }
    
    
    public void testDoLogin() throws Exception
    {
        UserProfileHolder newUser = new UserProfileHolder();
        BeanUtils.copyProperties(admin, newUser);
        newUser.setFailAttempts(0);
        Date lastLoginDate = new Date();
        newUser.setLastLoginDate(lastLoginDate);
        
        loginService.doLogin(getCommonParameter(false), newUser, admin, TEST_SESSION_ID);
        UserProfileHolder user = userProfileService.selectUserProfileByKey(admin.getUserOid());
        assertNotNull(user);
        assertEquals(0, user.getFailAttempts().intValue());
        assertEquals(DateUtil.getInstance().convertDateToString(lastLoginDate),DateUtil.getInstance().convertDateToString(user.getLastLoginDate()));
        
        session = userSessionService.selectUserSessionByKey(TEST_SESSION_ID);
        assertNotNull(session);
        assertEquals(admin.getUserOid(), session.getUserOid());
        
        auditAccess = new AuditAccessHolder();
        auditAccess.setLoginId(admin.getLoginId());
        List<AuditAccessHolder> rlt = auditAccessService.select(auditAccess);
        assertNotNull(rlt);
        assertEquals(1, rlt.size());
        assertEquals(AccessActionType.IN,rlt.get(0).getActionType());
        
        auditSession = new AuditSessionHolder();
        auditSession.setUserOid(admin.getUserOid());
        auditSession.setLoginId(admin.getLoginId());
        
        List<AuditSessionHolder> auditSessions = auditSessionService.select(auditSession);
        assertNotNull(auditSessions);
        assertEquals(1, auditSessions.size());
        assertNotNull(auditSessions.get(0).getStartDate());
        assertNull(auditSessions.get(0).getEndDate());
    }
    
    
    public void testDoLogout() throws Exception
    {
        UserProfileHolder newUser = new UserProfileHolder();
        BeanUtils.copyProperties(admin, newUser);
        newUser.setFailAttempts(0);
        Date lastLoginDate = new Date();
        newUser.setLastLoginDate(lastLoginDate);
        CommonParameterHolder cp = getCommonParameter(false);
        loginService.doLogin(cp, newUser, admin, TEST_SESSION_ID);
        loginService.doLogout(cp, admin, TEST_SESSION_ID);
        
        
        UserProfileHolder user = userProfileService.selectUserProfileByKey(admin.getUserOid());
        assertNotNull(user);
        assertEquals(0, user.getFailAttempts().intValue());
        assertEquals(DateUtil.getInstance().convertDateToString(lastLoginDate),DateUtil.getInstance().convertDateToString(user.getLastLoginDate()));
        
        session = userSessionService.selectUserSessionByKey(TEST_SESSION_ID);
        assertNull(session);
        
        auditAccess = new AuditAccessHolder();
        auditAccess.setLoginId(admin.getLoginId());
        List<AuditAccessHolder> rlt = auditAccessService.select(auditAccess);
        assertNotNull(rlt);
        assertEquals(2, rlt.size());
        
        auditSession = new AuditSessionHolder();
        auditSession.setUserOid(admin.getUserOid());
        auditSession.setLoginId(admin.getLoginId());
        
        List<AuditSessionHolder> auditSessions = auditSessionService.select(auditSession);
        assertNotNull(auditSessions);
        assertEquals(1, auditSessions.size());
        assertNotNull(auditSessions.get(0).getStartDate());
        assertNotNull(auditSessions.get(0).getEndDate());
    }

    
    @Override
    protected void setUp() throws Exception
    {
        OidService oidService = ctx.getBean("oidService", OidService.class);
        userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
        userProfileTmpService = ctx.getBean("userProfileTmpService",UserProfileTmpService.class);
        loginService = ctx.getBean("loginService",LoginService.class);
        roleService = ctx.getBean("roleService",RoleService.class);
        roleTmpService = ctx.getBean("roleTmpService", RoleTmpService.class);
        roleOperationService = ctx.getBean("roleOperationService", RoleOperationService.class);
        roleOperationTmpService = ctx.getBean("roleOperationTmpService",RoleOperationTmpService.class);
        roleUserMapper = ctx.getBean("roleUserMapper", RoleUserMapper.class);
        roleUserTmpMapper = ctx.getBean("roleUserTmpMapper",RoleUserTmpMapper.class);
        userSessionService = ctx.getBean("userSessionService", UserSessionService.class);
        auditAccessService = ctx.getBean("auditAccessService", AuditAccessService.class);
        auditSessionService = ctx.getBean("auditSessionService", AuditSessionService.class);
        
        role = new RoleTmpHolder();
        role.setRoleOid(oidService.getOid());
        role.setRoleId("ROLE1");
        role.setRoleName("Role1 Name");
        role.setRoleType(RoleType.ADMIN);
        role.setCreateDate(new Date());
        role.setCreateBy("SYSADMIN");
        role.setUserTypeOid(BigDecimal.valueOf(1));
        
        roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(role.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(role.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(role.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        
        role.setRoleOperations(roleOperations);
        roleService.createRoleProfile(this.getCommonParameter(false), role);
    
        admin = new UserProfileTmpHolder();
        admin.setUserOid(oidService.getOid());
        admin.setLoginId("testAdmin");
        admin.setUserName("admin");
        admin.setLoginPwd(EncodeUtil.getInstance().computePwd("password", admin.getUserOid()));
        admin.setLoginMode("PASSWORD");
        admin.setEmail("jiangming@pracbiz.com");
        admin.setGender("M");
        admin.setActive(true);
        admin.setBlocked(false);
        admin.setInit(true);
        admin.setCreateDate(new Date());
        admin.setCreateBy(CREATOR);
        admin.setUserType(BigDecimal.ONE);
        admin.setActionType(DbActionType.CREATE);
        admin.setCtrlStatus(MkCtrlStatus.APPROVED);
        admin.setActor(admin.getLoginId());
        admin.setActionDate(new Date());
        
        ruList = new ArrayList<RoleUserHolder>();
        RoleUserTmpHolder roleUser = new RoleUserTmpHolder();
        roleUser.setUserOid(admin.getUserOid());
        roleUser.setRoleOid(role.getRoleOid());
        ruList.add(roleUser);
        admin.setRoleUsers(ruList);
        
        userProfileService.createUserProfile(this.getCommonParameter(false), "http://localhost:8080/b2bportal/user/", IP, admin,true);
    }
    
    

    @Override
    protected void tearDown() throws Exception
    {
        for (RoleOperationHolder roleOpt : roleOperations)
        {
            RoleOperationTmpHolder delete = new RoleOperationTmpHolder();
            delete.setRoleOid(roleOpt.getRoleOid());
            
            roleOperationTmpService.delete(delete);
            roleOperationService.delete(delete);
        }
        
        
        for (RoleUserHolder roleUser : ruList)
        {
            RoleUserTmpHolder delete = new RoleUserTmpHolder();
            delete.setUserOid(roleUser.getUserOid());
            
            roleUserMapper.delete(delete);
            roleUserTmpMapper.delete(delete);
        }
        
        
        RoleTmpHolder deleteRole = new RoleTmpHolder();
        deleteRole.setRoleOid(role.getRoleOid());
        roleService.delete(deleteRole);
        roleTmpService.delete(deleteRole);
        
        userSessionService.delete(session);
        auditAccessService.delete(auditAccess);
        auditSessionService.delete(auditSession);
        
        UserProfileTmpHolder delete = new UserProfileTmpHolder();
        delete.setUserOid(admin.getUserOid());
        userProfileTmpService.delete(delete);
        userProfileService.delete(delete);
        
        super.tearDown();
    }
    
    
    private CommonParameterHolder getCommonParameter(boolean mkMode)
    {
        CommonParameterHolder cp = new CommonParameterHolder();
        cp.setCurrentUserOid(BigDecimal.ONE);
        cp.setLoginId("admin1");
        cp.setMkMode(mkMode);
        cp.setClientIp(IP);
        
        return cp;
    }

}
