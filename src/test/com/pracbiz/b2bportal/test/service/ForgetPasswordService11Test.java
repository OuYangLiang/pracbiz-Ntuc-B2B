package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.springframework.beans.BeanUtils;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.service.ForgetPasswordService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PasswordHistoryService;
import com.pracbiz.b2bportal.core.service.ResetPasswordRequestHistoryService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class ForgetPasswordService11Test extends BaseServiceTestCase
{
    private static final String TEMPLATE_MAIL_FORGET_PASSWORD_LINK = "mail_forgetPassword_link.vm";
    private static final String TEMPLATE_MAIL_RESET_PASSWORD_RESULT = "mail_resetPassword_success.vm";
    private static final String CREATOR = "JunitTester";

    private ForgetPasswordService forgetPasswordService;
    private ResetPasswordRequestHistoryService resetPasswordRequestHistoryService;
    private EmailEngine emailEngine;
    private OidService oidService;
    private UserProfileService userProfileService;
    private PasswordHistoryService passwordHistoryService;

    private ResetPasswordRequestHistoryHolder r1, r2;
    private CommonParameterHolder cp;
    private UserProfileHolder user;
    private PasswordHistoryHolder p;
    
    
    public ForgetPasswordService11Test()
    {
        forgetPasswordService = ctx.getBean("forgetPasswordService", ForgetPasswordService.class);
        resetPasswordRequestHistoryService = ctx.getBean("resetPasswordRequestHistoryService", ResetPasswordRequestHistoryService.class);
        emailEngine = ctx.getBean("emailEngine", EmailEngine.class);
        oidService = ctx.getBean("oidService", OidService.class);
        userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
    }

    private void testResetPassword() throws Exception
    {
        String hashValue = EncodeUtil.getInstance().computeSha2Digest(EncodeUtil.getInstance().generateSecureBytes(16));
        r1.setHashCode(hashValue);
        r1.setLoginId(user.getLoginId());
        r1.setRequestTime(new java.util.Date());
        r1.setClientIp("localhost");
        r1.setValid(true);
        hashValue = EncodeUtil.getInstance().computeSha2Digest(EncodeUtil.getInstance().generateSecureBytes(16));
        r2.setHashCode(hashValue);
        r2.setLoginId(user.getLoginId());
        r2.setRequestTime(new java.util.Date());
        r2.setClientIp("localhost");
        r2.setValid(true);
        
        Map<String, Object> map = new HashMap<String, Object>();
        String resetUrl = "http://localhost:8080/ntuc/forgetPassword/confirmResetPassword.action?h=" + hashValue;
        map.put("name", user.getUserName());
        map.put("resetUrl", resetUrl);
        map.put("expireDate", DateUtil.getInstance().convertDateToString(
                DateUtil.getInstance().dateAfterDays(new java.util.Date(), 2), "yyyy-MM-dd hh:mm:ss"));
        map.put("helpdeskNumber", "123456");
        map.put("companyName", "NTUC");
        userProfileService.insert(user);
        resetPasswordRequestHistoryService.insert(r1);
        forgetPasswordService.resetPassword(r1, r2, cp, user, emailEngine, map);
        resetPasswordRequestHistoryService.delete(r2);
        resetPasswordRequestHistoryService.delete(r1);
        userProfileService.delete(user);
    }
    
    
    private void testSaveNewPassword() throws Exception
    {
        p = new PasswordHistoryHolder();
        p.setUserOid(user.getUserOid());
        p.setChangeDate(new java.util.Date());
        p.setOldLoginPwd("oldPassword");
        p.setActor(DbActionType.UPDATE.name());
        p.setChangeReason("for a test");
        userProfileService.insert(user);
        passwordHistoryService.insert(p);
        UserProfileTmpHolder newUser = new UserProfileTmpHolder();
        BeanUtils.copyProperties(user, newUser);
        newUser.setLoginPwd("newPassword");
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", user.getUserName());
        map.put("companyName", "NTUC");
//        forgetPasswordService.saveNewPassword(p, newUser, user, cp, emailEngine, map);
        
    }

 
    @Before
    public void setUp() throws Exception
    {
        user = new UserProfileTmpHolder();
        user.setUserOid(oidService.getOid());
        user.setLoginId("testloginid");
        user.setUserName("OYL");
        user.setLoginMode("PASSWORD");
        user.setEmail("wwyou@pracbiz.com");
        user.setGender("M");
        user.setActive(true);
        user.setBlocked(false);
        user.setInit(false);
        user.setCreateDate(new Date());
        user.setCreateBy(CREATOR);
        user.setUserType(BigDecimal.ONE);
        cp = this.getCommonParameter();
    }


    private CommonParameterHolder getCommonParameter()
    {
        CommonParameterHolder cp = new CommonParameterHolder();
        cp.setCurrentUserOid(BigDecimal.ONE);
        cp.setLoginId("admin1");
        cp.setMkMode(false);
        cp.setClientIp("localhost");

        return cp;
    }
}
