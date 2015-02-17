package com.pracbiz.b2bportal.test.service;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class BuyerMsgSettingServiceTest extends BaseServiceTestCase
{
    private BuyerHolder b1;
    private BuyerMsgSettingHolder bm1, bm2;
    
    @Autowired
    private OidService oidService;
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private BuyerMsgSettingService buyerMsgSettingService;
    
    private static final String MSG_TYPE_PO = "PO";
    private static final String MSG_TYPE_INV = "INV";
    
    
    public BuyerMsgSettingServiceTest()
    {
        oidService = ctx.getBean("oidService", OidService.class);
        buyerService = ctx.getBean("buyerService", BuyerService.class);
        buyerMsgSettingService = ctx.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
    }
    
    
    public void testBasicCrd() throws Exception
    {
        buyerService.insert(b1);
        buyerMsgSettingService.insert(bm1);
        buyerMsgSettingService.insert(bm2);
        List<? extends BaseHolder> list = buyerMsgSettingService.select(bm1);
        assertNotNull(list);
        assertEquals(1, list.size());
        list = buyerMsgSettingService.selectBuyerMsgSettingsByBuyerOid(b1.getBuyerOid());
        assertNotNull(list);
        assertEquals(2, list.size());
        buyerMsgSettingService.delete(bm1);
        buyerMsgSettingService.delete(bm2);
        buyerService.delete(b1);
        list = buyerMsgSettingService.selectBuyerMsgSettingsByBuyerOid(b1.getBuyerOid());
        assertTrue(list.isEmpty());
    }
    
    
    public void testUpdate() throws Exception
    {
        buyerService.insert(b1);
        buyerMsgSettingService.insert(bm1);
        BuyerMsgSettingHolder bm = new BuyerMsgSettingHolder();
        BeanUtils.copyProperties(bm1, bm);
        bm.setRcpsAddrs(null);
        buyerMsgSettingService.updateByPrimaryKeySelective(bm1, bm);
        List<? extends BaseHolder> list = buyerMsgSettingService.select(bm);
        assertEquals(bm1.getRcpsAddrs(), ((BuyerMsgSettingHolder) list.get(0)).getRcpsAddrs());
        buyerMsgSettingService.updateByPrimaryKey((BuyerMsgSettingHolder) list.get(0), bm);
        list = buyerMsgSettingService.select(bm);
        assertNull(((BuyerMsgSettingHolder) list.get(0)).getRcpsAddrs());
        buyerMsgSettingService.delete((BuyerMsgSettingHolder) list.get(0));
        buyerService.delete(b1);
    }
    
    
    @Before
    public void setUp() throws Exception
    {
        initBuyer();
        initBuyerMsgSetting();
    }
    
    
    public void initBuyer() throws Exception
    {
        b1 = new BuyerHolder();
        b1.setBuyerOid(oidService.getOid());
        b1.setBuyerCode("BUYER1_CODE");
        b1.setBuyerName("BUYER1_NAME");
        b1.setBranch(false);
        b1.setAddress1("testBuyerAddr1");
        b1.setCtryCode("SG");
        b1.setCurrCode("SGD");
        b1.setActive(true);
        b1.setBlocked(false);
        b1.setCreateBy("CREATOR");
        b1.setCreateDate(new Date());
        b1.setMboxId("BUYER1_MBOXID");
        b1.setChannel("testBuyerChannel");
    }
    
    
    public void initBuyerMsgSetting()
    {
        bm1 = new BuyerMsgSettingHolder();
        bm1.setBuyerOid(b1.getBuyerOid());
        bm1.setMsgType(MSG_TYPE_PO);
        bm1.setAlertFrequency("BATCH");
        bm1.setRcpsAddrs("wwyou@pracbiz.com");
        bm1.setExcludeSucc(false);
        
        bm2 = new BuyerMsgSettingHolder();
        bm2.setBuyerOid(b1.getBuyerOid());
        bm2.setMsgType(MSG_TYPE_INV);
        bm2.setAlertFrequency("BATCH");
        bm2.setRcpsAddrs("wwyou@pracbiz.com");
        bm2.setExcludeSucc(false);
    }
}
