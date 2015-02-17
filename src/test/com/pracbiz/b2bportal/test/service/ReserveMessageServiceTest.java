package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.ReserveMessageHolder;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.ReserveMessageService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class ReserveMessageServiceTest extends BaseServiceTestCase
{
    private ReserveMessageHolder rm1, rm2;
    
    @Autowired
    private OidService oidService;
    @Autowired
    private ReserveMessageService reserveMessageService;
    
    private static final String TITLE1 = "title1";
    private static final String CONTENT1 = "content1";
    private static final String TITLE2 = "title2";
    private static final String CONTENT2 = "content2";
    
    public ReserveMessageServiceTest()
    {
        oidService = ctx.getBean("oidService", OidService.class);
        reserveMessageService = ctx.getBean("reserveMessageService", ReserveMessageService.class);
    }
    
    
    public void testBasicCrd() throws Exception
    {
        int originalNumOfRecords = 0;
        
        List<? extends Object> records = reserveMessageService.select(new ReserveMessageHolder());
        
        if (null != records && !records.isEmpty())
        {
            originalNumOfRecords = records.size();
        }
        reserveMessageService.insert(rm1);
        reserveMessageService.insert(rm2);
        List<? extends BaseHolder> list = reserveMessageService.select(new ReserveMessageHolder());
        assertEquals(originalNumOfRecords + 2, list.size());
        ReserveMessageHolder rlt = reserveMessageService.selectReserveMessageByKey(rm1.getRsrvMsgOid());
        assertNotNull(rlt);
        assertEquals(rm1.getTitle(), rlt.getTitle());
        list = reserveMessageService.selectValidMessages();
        assertNotNull(list);
        reserveMessageService.delete(rm1);
        reserveMessageService.delete(rm2);
        rlt = reserveMessageService.selectReserveMessageByKey(rm1.getRsrvMsgOid());
        assertNull(rlt);
        rlt = reserveMessageService.selectReserveMessageByKey(rm2.getRsrvMsgOid());
        assertNull(rlt);
    }
    
    
    public void testUpdateByPrimaryKeySelective() throws Exception
    {
        reserveMessageService.insert(rm1);
        ReserveMessageHolder rm = new ReserveMessageHolder();
        BeanUtils.copyProperties(rm1, rm);
        rm.setContent(null);
        rm.setTitle(null);
        reserveMessageService.updateByPrimaryKeySelective(rm1, rm);
        ReserveMessageHolder rlt = reserveMessageService.selectReserveMessageByKey(rm.getRsrvMsgOid());
        assertNotNull(rlt);
        assertNotNull(rlt.getContent());
        assertNotNull(rlt.getTitle());
        reserveMessageService.delete(rlt);
    }
    
    
    @Before
    public void setUp() throws Exception
    {
        initReserveMessage();
    }
    
    
    private void initReserveMessage() throws Exception
    {
        rm1 = new ReserveMessageHolder();
        rm1.setRsrvMsgOid(oidService.getOid());
        rm1.setTitle(TITLE1);
        rm1.setContent(CONTENT1);
        rm1.setValidFrom(new Date());
        rm1.setValidTo(new Date());
        rm1.setCreateBy(this.getCommonParameter(false).getLoginId());
        rm1.setCreateDate(new Date());
        
        rm2 = new ReserveMessageHolder();
        rm2.setRsrvMsgOid(oidService.getOid());
        rm2.setTitle(TITLE2);
        rm2.setContent(CONTENT2);
        rm2.setValidFrom(new Date());
        rm2.setValidTo(new Date());
        rm2.setCreateBy(this.getCommonParameter(false).getLoginId());
        rm2.setCreateDate(new Date());
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
