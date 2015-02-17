package com.pracbiz.b2bportal.test.service;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class SupplierMsgSettingServiceTest extends BaseServiceTestCase
{
    private SupplierHolder s1;
    private SupplierMsgSettingHolder sm1, sm2;
    
    @Autowired
    private OidService oidService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierMsgSettingService supplierMsgSettingService;
    
    private static final String MSG_TYPE_PO = "PO";
    private static final String MSG_TYPE_INV = "INV";
    
    
    public SupplierMsgSettingServiceTest()
    {
        oidService = ctx.getBean("oidService", OidService.class);
        supplierService = ctx.getBean("supplierService", SupplierService.class);
        supplierMsgSettingService = ctx.getBean("supplierMsgSettingService", SupplierMsgSettingService.class);
    }
    
    
    public void testBasicCrd() throws Exception
    {
        supplierService.insert(s1);
        supplierMsgSettingService.insert(sm1);
        supplierMsgSettingService.insert(sm2);
        List<? extends BaseHolder> list = supplierMsgSettingService.select(sm1);
        assertNotNull(list);
        assertEquals(1, list.size());
        list = supplierMsgSettingService.selectSupplierMsgSettingBySupplierOid(s1.getSupplierOid());
        assertNotNull(list);
        assertEquals(2, list.size());
        supplierMsgSettingService.delete(sm1);
        supplierMsgSettingService.delete(sm2);
        supplierService.delete(s1);
        list = supplierMsgSettingService.selectSupplierMsgSettingBySupplierOid(s1.getSupplierOid());
        assertTrue(list.isEmpty());
    }
    
    
    public void testUpdate() throws Exception
    {
        supplierService.insert(s1);
        supplierMsgSettingService.insert(sm1);
        SupplierMsgSettingHolder sm = new SupplierMsgSettingHolder();
        BeanUtils.copyProperties(sm1, sm);
        sm.setRcpsAddrs(null);
        supplierMsgSettingService.updateByPrimaryKeySelective(sm1, sm);
        List<? extends BaseHolder> list = supplierMsgSettingService.select(sm);
        assertEquals(sm1.getRcpsAddrs(), ((SupplierMsgSettingHolder) list.get(0)).getRcpsAddrs());
        supplierMsgSettingService.delete((SupplierMsgSettingHolder) list.get(0));
        supplierService.delete(s1);
    }
    
    
    @Before
    public void setUp() throws Exception
    {
        initSupplier();
        initSupplierMsgSetting();
    }
    
    public void initSupplier() throws Exception
    {
        s1 = new SupplierHolder();
        s1.setSupplierOid(oidService.getOid());
        s1.setSupplierCode("SUPP1_CODE");
        s1.setSupplierName("SUPP1_NAME");
        s1.setBranch(false);
        s1.setCtryCode("SG");
        s1.setCurrCode("SGD");
        s1.setActive(true);
        s1.setBlocked(false);
        s1.setCreateBy("CREATOR");
        s1.setCreateDate(new Date());
        s1.setAddress1("testSupplierAddr1");
        s1.setContactName("CREATOR");
        s1.setMboxId("SUPP1_MBOXID"); 
        s1.setContactTel("1111111111");
        s1.setContactEmail("testJuniter@pracbiz.com");
        s1.setLogo("supp1Logo".getBytes());
    }
    
    
    public void initSupplierMsgSetting()
    {
        sm1 = new SupplierMsgSettingHolder();
        sm1.setSupplierOid(s1.getSupplierOid());
        sm1.setMsgType(MSG_TYPE_PO);
        sm1.setRcpsAddrs("wwyou@pracbiz.com");
        sm1.setExcludeSucc(false);
        
        sm2 = new SupplierMsgSettingHolder();
        sm2.setSupplierOid(s1.getSupplierOid());
        sm2.setMsgType(MSG_TYPE_INV);
        sm2.setRcpsAddrs("wwyou@pracbiz.com");
        sm2.setExcludeSucc(false);
    }
}
