package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.ResetPasswordRequestHistoryService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class ResetPasswordRequestHistoryServiceTest extends BaseServiceTestCase
{
    private ResetPasswordRequestHistoryHolder rprh1, rprh2;
    private UserProfileHolder u1;
    
    @Autowired
    private OidService oidService;
    @Autowired
    private ResetPasswordRequestHistoryService resetPasswordRequestHistoryService;
    @Autowired
    private UserProfileService userProfileService;
    
    public ResetPasswordRequestHistoryServiceTest()
    {
        oidService = ctx.getBean("oidService", OidService.class);
        resetPasswordRequestHistoryService = ctx.getBean("resetPasswordRequestHistoryService", ResetPasswordRequestHistoryService.class);
        userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
    }
    
    
    public void testBasicCrd() throws Exception
    {
        int originalNumOfRecords = 0;
        
        List<? extends Object> records = resetPasswordRequestHistoryService.select(new ResetPasswordRequestHistoryHolder());
        
        if (null != records && !records.isEmpty())
        {
            originalNumOfRecords = records.size();
        }
        userProfileService.insert(u1);
        resetPasswordRequestHistoryService.insert(rprh1);
        resetPasswordRequestHistoryService.insert(rprh2);
        records = resetPasswordRequestHistoryService.select(new ResetPasswordRequestHistoryHolder());
        assertEquals(originalNumOfRecords + 2, records.size());
        records = resetPasswordRequestHistoryService.select(rprh1);
        assertFalse(records.isEmpty());
        assertEquals(rprh1.getHashCode(), ((ResetPasswordRequestHistoryHolder) records.get(0)).getHashCode());
        resetPasswordRequestHistoryService.delete(rprh1);
        resetPasswordRequestHistoryService.delete(rprh2);
        records = resetPasswordRequestHistoryService.select(rprh2);
        assertTrue(records.isEmpty());
        userProfileService.delete(u1);
    }
    
    
    public void testUpdate() throws Exception
    {
        userProfileService.insert(u1);
        resetPasswordRequestHistoryService.insert(rprh1);
        ResetPasswordRequestHistoryHolder rprh = new ResetPasswordRequestHistoryHolder();
        BeanUtils.copyProperties(rprh1, rprh);
        rprh.setLoginId(null);
        resetPasswordRequestHistoryService.updateByPrimaryKeySelective(rprh1, rprh);
        List<ResetPasswordRequestHistoryHolder> records = resetPasswordRequestHistoryService.select(rprh);
        assertFalse(records.isEmpty());
        assertEquals(rprh1.getLoginId(), (records.get(0)).getLoginId());
        rprh.setLoginId("test");
        resetPasswordRequestHistoryService.updateByPrimaryKeySelective(rprh1, rprh);
        records = resetPasswordRequestHistoryService.select(rprh);
        assertFalse(records.isEmpty());
        assertEquals(rprh.getLoginId(), (records.get(0)).getLoginId());
        resetPasswordRequestHistoryService.delete(rprh1);
        records = resetPasswordRequestHistoryService.select(rprh2);
    }
    
    
    @Before
    public void setUp() throws Exception
    {
        initUser();
        initResetPasswordRequestHistory();
    }
    
    
    private void initResetPasswordRequestHistory() throws Exception
    {
        rprh1 = new ResetPasswordRequestHistoryHolder();
        String hashCode = EncodeUtil.getInstance().computeSha2Digest(EncodeUtil.getInstance().generateSecureBytes(16));
        rprh1.setHashCode(hashCode);
        rprh1.setLoginId(u1.getLoginId());
        rprh1.setRequestTime(new Date());
        rprh1.setClientIp(this.getCommonParameter(false).getClientIp());
        rprh1.setValid(true);
        
        rprh2 = new ResetPasswordRequestHistoryHolder();
        hashCode = EncodeUtil.getInstance().computeSha2Digest(EncodeUtil.getInstance().generateSecureBytes(16));
        rprh2.setHashCode(hashCode);
        rprh2.setLoginId(u1.getLoginId());
        rprh2.setRequestTime(new Date());
        rprh2.setClientIp(this.getCommonParameter(false).getClientIp());
        rprh2.setValid(true);
    }
    
    
    private void initUser() throws Exception
    {
        u1 = new UserProfileTmpHolder();
        u1.setUserOid(oidService.getOid());
        u1.setUserName("Ge");
        u1.setLoginId("GE");
        u1.setLoginMode("PASSWORD");
        u1.setGender("1");
        u1.setEmail("wwyou@pracbiz.com");
        u1.setActive(true);
        u1.setBlocked(false);
        u1.setInit(false);
        u1.setCreateDate(new Date());
        u1.setCreateBy(this.getCommonParameter(false).getLoginId());
        u1.setUserType(BigDecimal.valueOf(2));
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
