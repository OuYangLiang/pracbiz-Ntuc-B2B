package com.pracbiz.b2bportal.test.service;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class TermConditionServiceTest extends BaseServiceTestCase
{
    private SupplierHolder s1;
    private TermConditionHolder tc1, tc2;
    
    @Autowired
    private OidService oidService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private TermConditionService termConditionService;
    
    private static final String MSG_TYPE_PO = "PO";
    private static final String MSG_TYPE_INV = "INV";
    
    
    public TermConditionServiceTest()
    {
        oidService = ctx.getBean("oidService", OidService.class);
        supplierService = ctx.getBean("supplierService", SupplierService.class);
        termConditionService = ctx.getBean("termConditionService", TermConditionService.class);
    }
    
    
    public void testBasicCrd() throws Exception
    {
        supplierService.insert(s1);
        termConditionService.insert(tc1);
        termConditionService.insert(tc2);
        List<? extends BaseHolder> list = termConditionService.select(tc1);
        assertNotNull(list);
        assertEquals(1, list.size());
        list = termConditionService.selectTermsConditionsBySupplierOid(s1.getSupplierOid());
        assertNotNull(list);
        assertEquals(2, list.size());
        TermConditionHolder rlt = termConditionService.selectDefaultTermConditionBySupplierOid(s1.getSupplierOid());
        assertNotNull(rlt);
        assertEquals(tc1.getSeq(), rlt.getSeq());
        rlt = termConditionService.selectTermConditionByKey(tc2.getTermConditionOid());
        assertNotNull(rlt);
        assertEquals(tc2.getSeq(), rlt.getSeq());
        rlt = termConditionService.selectTermsConditionBySupplierOidAndTcCode(s1.getSupplierOid(), tc2.getTermConditionCode());
        assertNotNull(rlt);
        assertEquals(tc2.getSeq(), rlt.getSeq());
        termConditionService.delete(tc1);
        termConditionService.delete(tc2);
        list = termConditionService.selectTermsConditionsBySupplierOid(s1.getSupplierOid());
        assertTrue(list.isEmpty());
        supplierService.delete(s1);
    }
    
    
    public void testUpdate() throws Exception
    {
        supplierService.insert(s1);
        termConditionService.insert(tc1);
        TermConditionHolder tc = new TermConditionHolder();
        BeanUtils.copyProperties(tc1, tc);
        tc.setTermCondition1(null);
        termConditionService.updateByPrimaryKeySelective(tc1, tc);
        TermConditionHolder rlt = termConditionService.selectTermConditionByKey(tc.getTermConditionOid());
        assertNotNull(rlt);
        assertEquals(tc1.getTermCondition1(), rlt.getTermCondition1());
        termConditionService.updateByPrimaryKey(tc1, tc);
        rlt = termConditionService.selectTermConditionByKey(tc.getTermConditionOid());
        assertNotNull(rlt);
        assertNull(rlt.getTermCondition1());
        termConditionService.delete(tc);
        supplierService.delete(s1);
    }
    
    
    @Before
    public void setUp() throws Exception
    {
        initSupplier();
        initTermCondition();
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
    
    
    public void initTermCondition() throws Exception
    {
        tc1 = new TermConditionHolder();
        tc1.setTermConditionOid(oidService.getOid());
        tc1.setTermConditionCode("termConditionCode1");
        tc1.setSupplierOid(s1.getSupplierOid());
        tc1.setSeq(Short.valueOf("1"));
        tc1.setTermCondition1("termCondition1_1");
        tc1.setDefaultSelected(true);
        
        tc2 = new TermConditionHolder();
        tc2.setTermConditionOid(oidService.getOid());
        tc2.setTermConditionCode("termConditionCode2");
        tc2.setSupplierOid(s1.getSupplierOid());
        tc2.setSeq(Short.valueOf("2"));
        tc2.setTermCondition1("termCondition1_2");
        tc2.setDefaultSelected(false);
    }
}
