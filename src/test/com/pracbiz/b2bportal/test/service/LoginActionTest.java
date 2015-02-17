//*****************************************************************************
//
// File Name       :  LoginActionTest.java
// Date Created    :  Oct 15, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Oct 15, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.test.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.annotations.After;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.core.action.LoginAction;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.ReserveMessageHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserSessionHolder;
import com.pracbiz.b2bportal.core.mapper.RoleUserMapper;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.ReserveMessageService;
import com.pracbiz.b2bportal.core.service.RoleOperationService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserSessionService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.test.base.BaseActionTestCase;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class LoginActionTest extends BaseActionTestCase
{
    @Autowired private OidService oidService;
    @Autowired private ReserveMessageService reserveMessageService;
    @Autowired private UserSessionService userSessionService;
    @Autowired private UserProfileService userProfileService;
    @Autowired private SupplierService supplierService;
    @Autowired private ControlParameterService controlParameterService;
    @Autowired private RoleService roleService;
    @Autowired private RoleOperationService roleOperationService;
    @Autowired private RoleUserMapper roleUserMapper;
    
    private SupplierHolder supplier;
    private UserProfileTmpHolder supplierAdmin;
    private ControlParameterHolder  ctrlParam;
    private RoleTmpHolder role;
    private RoleUserHolder roleUser;
    
    private ReserveMessageHolder message;
    private static final String FIELD_LOGIN_ID="loginId";
    private static final String FIELD_LOGIN_PASSWORD="password";
    private static final String SUPPLIER_ADMIN="testSuppAdmin1";
    private static final String SUPPLIER_ADMIN_PASSWORD = "password";
    private static final String LOGIN_WITH_OUT_MSG="loginWithOutMsg";
    private static final String LOGIN_SUCCESS="success";
    private static final String CREATOR="juniter";
    private static final String LOGIN_MODULE_PASSWORD="PASSWORD";
    private static final String LOGIN_MODULE_AD="AD";
    private static final String LOGIN_ACTION="/login.action";
    private static final String AUTO_LOGIN_ACTION="/autoLogin.action";
    

    @Test
    public void testInitLoginWithOutMsg() throws Exception
    {
        ActionProxy actionProxy = this.getActionProxy("/home.action");
        this.init(actionProxy);
        
        // execute home action.
        String result = actionProxy.execute();
        LoginAction action = (LoginAction)actionProxy.getAction();
        assertEquals(0, action.getActionErrors().size());
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }
    
    @Test
    public void testInitLoginWithMsg() throws Exception
    {
        // initialize message
        message = new ReserveMessageHolder();
        message.setRsrvMsgOid(oidService.getOid());
        message.setTitle("title1");
        message.setContent("message1");
        message.setCreateDate(new Date());
        message.setValidFrom(DateUtil.getInstance().convertStringToDate("2012-10-01"));
        message.setValidTo(DateUtil.getInstance().convertStringToDate("2012-10-30"));
        message.setCreateBy("juniter");
        reserveMessageService.insert(message);
        
        ActionProxy actionProxy = this.getActionProxy("/home.action");
        this.init(actionProxy);
        String result = actionProxy.execute();
        assertEquals(LOGIN_SUCCESS, result);
    }
    
    
    @Test
    public void testLoginWithPassword() throws Exception
    {
        // initialize supplier & user
        this.initSupplier(true, false);
        this.initUser(true, false, LOGIN_MODULE_PASSWORD);
        
        // initialize request parameter
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        ActionProxy actionProxy = this.getActionProxy("/login.action");
        
        this.init(actionProxy);
        
        // execute login action.
        String result = actionProxy.execute();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNull(errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }
    
    
    @Test
    public void testLoginWithUserBlocked() throws Exception
    {
        // initialize supplier & user
        this.initSupplier(true, false);
        this.initUser(true, true, LOGIN_MODULE_PASSWORD);
        
        // initialize request parameter
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        ActionProxy actionProxy = this.getActionProxy(LOGIN_ACTION);
        
        this.init(actionProxy);
        
        // execute login action.
        String result = actionProxy.execute();
        LoginAction action = (LoginAction)actionProxy.getAction();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNotNull(errorMsg);
        assertEquals(action.getText("B2BPC0709",new String[]{SUPPLIER_ADMIN}), errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }
    
    
    @Test
    public void testLoginWithUserInactived() throws Exception
    {
        // initialize supplier & user
        this.initSupplier(true, false);
        this.initUser(false, false, LOGIN_MODULE_PASSWORD);
        
        // initialize request parameter
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        ActionProxy actionProxy = this.getActionProxy(LOGIN_ACTION);
        
        this.init(actionProxy);
        
        // execute login action.
        String result = actionProxy.execute();
        LoginAction action = (LoginAction)actionProxy.getAction();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNotNull(errorMsg);
        assertEquals(action.getText("B2BPC0708",new String[]{SUPPLIER_ADMIN}), errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }
    
    
    @Test
    public void testLoginWithCompanyBlocked() throws Exception
    {
        // initialize supplier & user
        this.initSupplier(true, true);
        this.initUser(true, false, LOGIN_MODULE_PASSWORD);
        
        // initialize request parameter
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        ActionProxy actionProxy = this.getActionProxy(LOGIN_ACTION);
        
        this.init(actionProxy);
        
        // execute login action.
        String result = actionProxy.execute();
        LoginAction action = (LoginAction)actionProxy.getAction();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNotNull(errorMsg);
        assertEquals(action.getText("B2BPC0722",new String[]{SUPPLIER_ADMIN}), errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }
    
    
    @Test
    public void testLoginWithCompanyInactived() throws Exception
    {
        // initialize supplier & user
        this.initSupplier(false, false);
        this.initUser(true, false, LOGIN_MODULE_PASSWORD);
        
        // initialize request parameter
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        ActionProxy actionProxy = this.getActionProxy(LOGIN_ACTION);
        
        this.init(actionProxy);
        
        // execute login action.
        String result = actionProxy.execute();
        LoginAction action = (LoginAction)actionProxy.getAction();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNotNull(errorMsg);
        assertEquals(action.getText("B2BPC0728",new String[]{SUPPLIER_ADMIN}), errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }
    
    
    @Test
    public void testLoginWithUserAutoBlocked() throws Exception
    {
        // account auto block after failed 
        // login attempts exceeded preset threshold.
        
        // initialize supplier & user
        this.initSupplier(true, false);
        this.initUser(true, false, LOGIN_MODULE_PASSWORD);
        
        // initialize request parameter
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, "1231231");
        // execute login action.
        ControlParameterHolder maxAttmptLogin = controlParameterService.selectCacheControlParameterBySectIdAndParamId(
            CoreCommonConstants.SECT_ID_CTRL, CoreCommonConstants.PARAM_ID_MAX_ATTEMPT_LOGIN);
        
        for (int i = 0; i < maxAttmptLogin.getNumValue(); i++)
        {
            ActionProxy actionProxy = this.getActionProxy(LOGIN_ACTION);
            this.init(actionProxy);
            actionProxy.execute();
        }
        
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        ActionProxy actionProxy = this.getActionProxy(LOGIN_ACTION);
        this.init(actionProxy);
        String result = actionProxy.execute();
        LoginAction action = (LoginAction)actionProxy.getAction();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNotNull(errorMsg);
        assertEquals(action.getText("B2BPC0709",new String[]{SUPPLIER_ADMIN}), errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }

    
    @Test
    public void testAllowMultipleLoginBySameUser() throws Exception
    {
        // initialize supplier & user
        this.initSupplier(true, false);
        this.initUser(true, false, LOGIN_MODULE_PASSWORD);
        
        // initialize request parameter
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        
        ctrlParam = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(CoreCommonConstants.SECT_ID_CTRL,
                CoreCommonConstants.PARAM_ID_REPEATED_LOGIN);
        ControlParameterHolder newCtrlParam = new ControlParameterHolder();
        BeanUtils.copyProperties(ctrlParam, newCtrlParam);
        newCtrlParam.setValid(true);
        controlParameterService.updateByPrimaryKey(null, newCtrlParam);
        
        // first login execute
        ActionProxy actionProxy = this.getActionProxy(LOGIN_ACTION);
        this.init(actionProxy);
        String result = actionProxy.execute();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNull(errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
        
        ActionContext actionContext = actionProxy.getInvocation().getInvocationContext();
        Map<String, Object> application  = actionContext.getApplication();
        initServletMockObjects();
        
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        // second login execute
        actionProxy = this.getActionProxy(LOGIN_ACTION);
        actionContext = actionProxy.getInvocation().getInvocationContext();
        actionContext.setApplication(application);
        this.init(actionProxy);
        result = actionProxy.execute();
        errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNull(errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }

    @Test
    public void testCannotMultipleLoginBySameUser() throws Exception
    {
        // initialize supplier & user
        this.initSupplier(true, false);
        this.initUser(true, false, LOGIN_MODULE_PASSWORD);
        
        // initialize request parameter
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        
        ctrlParam = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(CoreCommonConstants.SECT_ID_CTRL,
                CoreCommonConstants.PARAM_ID_REPEATED_LOGIN);
        ControlParameterHolder newCtrlParam = new ControlParameterHolder();
        BeanUtils.copyProperties(ctrlParam, newCtrlParam);
        newCtrlParam.setValid(false);
        controlParameterService.updateByPrimaryKey(null, newCtrlParam);
        
        // first login execute
        ActionProxy actionProxy = this.getActionProxy(LOGIN_ACTION);
        this.init(actionProxy);
        String result = actionProxy.execute();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNull(errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
        ActionContext actionContext = actionProxy.getInvocation().getInvocationContext();
        Map<String, Object> application  = actionContext.getApplication();
        initServletMockObjects();
        
        request.setParameter(FIELD_LOGIN_ID, SUPPLIER_ADMIN);
        request.setParameter(FIELD_LOGIN_PASSWORD, SUPPLIER_ADMIN_PASSWORD);
        
        // second login execute
        actionProxy = this.getActionProxy(LOGIN_ACTION);
        actionContext = actionProxy.getInvocation().getInvocationContext();
        actionContext.setApplication(application);
        this.init(actionProxy);
        result = actionProxy.execute();
        errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNotNull(errorMsg);
        
        LoginAction action = (LoginAction)actionProxy.getAction();
        assertEquals(action.getText("B2BPC0710",new String[]{SUPPLIER_ADMIN}), errorMsg);
        assertEquals(LOGIN_WITH_OUT_MSG, result);
    }

    
    @Test
    public void testAutoLoginWithAdUser() throws Exception
    {
        // initialize supplier & user
        this.initSupplier(true, false);
        this.initUser(true, false, LOGIN_MODULE_AD);
        
        // initialize request parameter
        request.setAttribute(CoreCommonConstants.CLIENT_USER_NAME, SUPPLIER_ADMIN);
        
        // first login execute
        ActionProxy actionProxy = this.getActionProxy(AUTO_LOGIN_ACTION);
        this.init(actionProxy);
        String result = actionProxy.execute();
        String errorMsg = (String)request.getAttribute(LoginAction.MSG_ERROR_LOGIN_FAILED);
        Assert.assertNull(errorMsg);
        assertEquals(LOGIN_SUCCESS, result);
    }
    
    
    //*************************************************************
    // private method
    //*************************************************************
    private void init(ActionProxy actionProxy)
    {
        // initialize session
        Map<String, Object> session = new HashMap<String, Object>();  
        ActionContext actionContext = actionProxy.getInvocation().getInvocationContext();
        actionContext.setSession(session);
        
        // initialize application
        Map<String, Object> application  = actionContext.getApplication();
        if (application == null)
        {
            application = new HashMap<String, Object>(); 
            actionContext.setApplication(application);
        }
    }
    

    @Override
    @After
    public void tearDown() throws Exception
    {
        if (message != null)
        {
            ReserveMessageHolder delete = new ReserveMessageHolder();
            delete.setRsrvMsgOid(message.getRsrvMsgOid());
            reserveMessageService.delete(delete);
        }
        
        if (supplierAdmin != null)
        {
            roleUserMapper.delete(roleUser);
            RoleOperationHolder deleteRoleOpt = new RoleOperationHolder();
            deleteRoleOpt.setRoleOid(role.getRoleOid());
            roleOperationService.delete(deleteRoleOpt);
            roleService.delete(role);
            
            UserSessionHolder deleteSession = new UserSessionHolder();
            deleteSession.setUserOid(supplierAdmin.getUserOid());
            userSessionService.delete(deleteSession);
            
            UserProfileTmpHolder user = new UserProfileTmpHolder();
            user.setUserOid(supplierAdmin.getUserOid());
            userProfileService.delete(user);
            
            SupplierHolder deleteSupp = new SupplierHolder();
            deleteSupp.setSupplierOid(supplier.getSupplierOid());
            supplierService.delete(deleteSupp);
        }
        
        if (ctrlParam != null)
        {
            controlParameterService.updateByPrimaryKey(null, ctrlParam);
        }
        
        super.tearDown();
    }
    
    
    private void initSupplier(boolean active,boolean blocked) throws Exception
    {
        supplier = new SupplierHolder();
        supplier.setSupplierOid(oidService.getOid());
        supplier.setSupplierCode("testSupplierCode");
        supplier.setSupplierName("testSupplierName");
        supplier.setAddress1("testSupplierAddr1");
        supplier.setMboxId("testSupplierMbox"); 
        supplier.setContactTel("1111111111");
        supplier.setContactEmail("testJuniter@pracbiz.com");
        supplier.setBranch(false);
        supplier.setAddress1("testSuppAddr1");
        supplier.setCtryCode("SG");
        supplier.setCurrCode("SGD");
        supplier.setActive(active);
        supplier.setBlocked(blocked);
        supplier.setCreateBy(CREATOR);
        supplier.setCreateDate(new Date());
        supplier.setMboxId("testBuyerMbox");
        supplier.setContactName(CREATOR);
        
        supplierService.insert(supplier);
    }
    
    
    private void initUser(boolean active,boolean blocked, String loginModule) throws Exception
    {
        role = new RoleTmpHolder();
        role.setRoleOid(oidService.getOid());
        role.setRoleId("ROLE1");
        role.setRoleName("Role1 Name");
        role.setRoleType(RoleType.SUPPLIER);
        role.setCreateDate(new Date());
        role.setCreateBy("SYSADMIN");
        role.setUserTypeOid(BigDecimal.valueOf(3));
        roleService.insert(role);
        
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(role.getRoleOid());
        ro.setOpnId("100001");
        roleOperationService.insert(ro);
        
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(role.getRoleOid());
        ro.setOpnId("100002");
        roleOperationService.insert(ro);
        
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(role.getRoleOid());
        ro.setOpnId("100003");
        roleOperationService.insert(ro);
        
        supplierAdmin = new UserProfileTmpHolder();
        supplierAdmin.setUserOid(oidService.getOid());
        supplierAdmin.setLoginMode(loginModule);
        supplierAdmin.setEmail("juniter@pracbiz.com");
        supplierAdmin.setGender("M");
        supplierAdmin.setActive(active);
        supplierAdmin.setBlocked(blocked);
        supplierAdmin.setInit(false);
        supplierAdmin.setCreateDate(new Date());
        supplierAdmin.setCreateBy(CREATOR);
        supplierAdmin.setActionType(DbActionType.CREATE);
        supplierAdmin.setCtrlStatus(MkCtrlStatus.APPROVED);
        supplierAdmin.setActor("SYSTEM");
        supplierAdmin.setActionDate(new Date());
        supplierAdmin.setLoginId(SUPPLIER_ADMIN);
        supplierAdmin.setUserName("SuppAdmin1");
        supplierAdmin.setUserType(BigDecimal.valueOf(3));
        supplierAdmin.setSupplierOid(supplier.getSupplierOid());
        supplierAdmin.setLoginPwd(EncodeUtil.getInstance().computePwd(SUPPLIER_ADMIN_PASSWORD, supplierAdmin.getUserOid()));
        userProfileService.insert(supplierAdmin);
        
        RoleUserHolder roleUser = new RoleUserHolder();
        roleUser.setUserOid(supplierAdmin.getUserOid());
        roleUser.setRoleOid(role.getRoleOid());
        roleUserMapper.insert(roleUser);
    }
}
