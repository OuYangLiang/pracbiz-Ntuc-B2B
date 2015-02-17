package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
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

public class ForgetPasswordServiceTest extends BaseServiceTestCase
{
    private ResetPasswordRequestHistoryHolder rprh1, rprh2;
    private UserProfileTmpHolder u;
    private Map<String, Object> m1, m2;
    private PasswordHistoryHolder ph;
    
    @Autowired
    private OidService oidService;
    @Autowired
    private ResetPasswordRequestHistoryService resetPasswordRequestHistoryService;
    @Autowired
    private EmailEngine emailEngine;
    @Autowired
    private ForgetPasswordService forgetPasswordService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private PasswordHistoryService passwordHistoryService;
    
    
    public static final String TEMPLATE_MAIL_FORGET_PASSWORD_LINK = "mail_forgetPassword_link.vm";
    public static final String TEMPLATE_MAIL_RESET_PASSWORD_RESULT = "mail_resetPassword_success.vm";
    public static final String RESET_URL = "testUrl";
    public static final String HELP_DESK_NO = "123456";
    public static final String COMPANY_NAME = "b2bPortal";
    
    public ForgetPasswordServiceTest()
    {
        forgetPasswordService = ctx.getBean("forgetPasswordService", ForgetPasswordService.class);
        resetPasswordRequestHistoryService = ctx.getBean("resetPasswordRequestHistoryService", ResetPasswordRequestHistoryService.class);
        emailEngine = ctx.getBean("emailEngine", EmailEngine.class);
        oidService = ctx.getBean("oidService", OidService.class);
        userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
        passwordHistoryService = ctx.getBean("passwordHistoryService", PasswordHistoryService.class);
    }
    
    
    public void testResetPassword() throws Exception
    {
        userProfileService.insert(u);
        resetPasswordRequestHistoryService.insert(rprh1);
        forgetPasswordService.resetPassword(rprh1, rprh2, this.getCommonParameter(false), u, emailEngine, m1);
        ResetPasswordRequestHistoryHolder param = new ResetPasswordRequestHistoryHolder();
        param.setHashCode(rprh1.getHashCode());
        List<ResetPasswordRequestHistoryHolder> list = resetPasswordRequestHistoryService.select(param);
        assertNotNull(list);
        assertFalse(list.get(0).getValid());
        param.setHashCode(rprh2.getHashCode());
        list = resetPasswordRequestHistoryService.select(param);
        assertNotNull(list);
        assertTrue(list.get(0).getValid());
        param.setHashCode(null);
        param.setLoginId(u.getLoginId());
        resetPasswordRequestHistoryService.delete(param);
        userProfileService.delete(u);
    }
    
    
    public void testSaveNewPassword() throws Exception
    {
        userProfileService.insert(u);
        resetPasswordRequestHistoryService.insert(rprh1);
        UserProfileTmpHolder user = new UserProfileTmpHolder();
        BeanUtils.copyProperties(u, user);
        user.setLoginPwd(EncodeUtil.getInstance().computePwd(
                "newPassword", user.getUserOid()));
        user.setUpdateBy(this.getCommonParameter(false).getLoginId());
        user.setUpdateDate(new Date());
        user.setLastResetPwdDate(new Date());
        forgetPasswordService.saveNewPassword(ph, user, u, this.getCommonParameter(false), emailEngine, m2);
        ResetPasswordRequestHistoryHolder param = new ResetPasswordRequestHistoryHolder();
        param.setHashCode(rprh1.getHashCode());
        List<? extends BaseHolder> list = resetPasswordRequestHistoryService.select(param);
        assertNotNull(list);
        assertFalse(((ResetPasswordRequestHistoryHolder)list.get(0)).getValid());
        list = passwordHistoryService.select(ph);
        assertNotNull(list);
        UserProfileHolder rlt = userProfileService.selectUserProfileByKey(u.getUserOid());
        assertEquals(user.getLoginPwd(), rlt.getLoginPwd());
        resetPasswordRequestHistoryService.delete(param);
        passwordHistoryService.delete(ph);
        userProfileService.delete(u);
    }
    
    @Before
    public void setUp() throws Exception
    {
        initUser();
        initResetPasswordRequestHistory();
        initMap();
        initPasswordHistory();
    }
    
    
    private void initUser() throws Exception
    {
        u = new UserProfileTmpHolder();
        u.setUserOid(oidService.getOid());
        u.setLoginId("GE");
        u.setUserName("Ge");
        u.setLoginMode("PASSWORD");
        u.setEmail("testJunit@pracbiz.com");
        u.setGender("1");
        u.setActive(true);
        u.setBlocked(false);
        u.setInit(false);
        u.setCreateDate(new Date());
        u.setCreateBy(this.getCommonParameter(false).getLoginId());
        u.setLoginPwd(EncodeUtil.getInstance().computePwd(
                "oldPassword", u.getUserOid()));
        u.setUserType(BigDecimal.valueOf(3));
    }
    
    
    private void initResetPasswordRequestHistory() throws Exception
    {
        rprh1 = new ResetPasswordRequestHistoryHolder();
        String hashCode = EncodeUtil.getInstance().computeSha2Digest(EncodeUtil.getInstance().generateSecureBytes(16));
        rprh1.setHashCode(hashCode);
        rprh1.setLoginId(u.getLoginId());
        rprh1.setRequestTime(new Date());
        rprh1.setClientIp(this.getCommonParameter(false).getClientIp());
        rprh1.setValid(true);
        
        rprh2 = new ResetPasswordRequestHistoryHolder();
        hashCode = EncodeUtil.getInstance().computeSha2Digest(EncodeUtil.getInstance().generateSecureBytes(16));
        rprh2.setHashCode(hashCode);
        rprh2.setLoginId(u.getLoginId());
        rprh2.setRequestTime(new Date());
        rprh2.setClientIp(this.getCommonParameter(false).getClientIp());
        rprh2.setValid(true);
    }
    
    
    private void initMap() throws Exception
    {
        m1 = new HashMap<String, Object>();
        m1.put("name", u.getUserName());
        m1.put("resetUrl", RESET_URL);
        m1.put("expireDate", String.valueOf(new Date()));
        m1.put("helpdeskNumber", HELP_DESK_NO);
        m1.put("companyName", COMPANY_NAME);
        
        m2 = new HashMap<String, Object>();
        m2.put("name", u.getUserName());
        m2.put("companyName", COMPANY_NAME);
    }
    
    
    private void initPasswordHistory() throws Exception
    {
        ph = new PasswordHistoryHolder();
        ph.setChangeDate(new Date());
        ph.setOldLoginPwd(u.getLoginPwd());
        ph.setUserOid(u.getUserOid());
        ph.setChangeReason("C");
        ph.setActor(u.getLoginId());
    }
    
    
    private CommonParameterHolder getCommonParameter(boolean mkMode)
    {
        CommonParameterHolder cp = new CommonParameterHolder();
        cp.setCurrentUserOid(BigDecimal.ONE);
        cp.setLoginId("admin1");
        cp.setMkMode(mkMode);
        cp.setClientIp("localhost");
        
        return cp;
    }
}
