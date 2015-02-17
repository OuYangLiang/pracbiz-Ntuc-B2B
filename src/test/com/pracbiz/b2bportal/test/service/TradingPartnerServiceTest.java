package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.pracbiz.b2bportal.core.constants.GroupType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;


/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */
public class TradingPartnerServiceTest extends BaseServiceTestCase
{
    private final TradingPartnerService tradingPartnerService;
    private final BuyerService buyerService;
    private final SupplierService supplierService;
    private final TermConditionService termConditionService;
    private final GroupService groupService;
    private final GroupTradingPartnerService groupTradingPartnerService;
    

    private BuyerHolder buyer;
    private SupplierHolder supplier;
    private static final String LOGIN_ID = "SYSTEM";
    private TradingPartnerExHolder tp1, tp2;
    private TermConditionHolder tc1, tc2;
    private GroupHolder group;
    private GroupTradingPartnerHolder gtp;
    
    public TradingPartnerServiceTest()
    {
        tradingPartnerService = ctx.getBean("tradingPartnerService", TradingPartnerService.class);
        buyerService = ctx.getBean("buyerService", BuyerService.class);
        supplierService = ctx.getBean("supplierService", SupplierService.class);
        termConditionService = ctx.getBean("termConditionService", TermConditionService.class);
        groupService = ctx.getBean("groupService", GroupService.class);
        groupTradingPartnerService = ctx.getBean("groupTradingPartnerService", GroupTradingPartnerService.class);
    }
    
    public void testBasicCrd() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        termConditionService.insert(tc1);
        termConditionService.insert(tc2);
        
        int curRecords = 0;
        int newRecordsCount = 0;
        
        List<? extends Object> records = tradingPartnerService.select(new TradingPartnerExHolder());
        
        if (null != records && !records.isEmpty())
        {
            curRecords = records.size();
        }
        
        tradingPartnerService.insert(tp1);
        newRecordsCount ++;
        tradingPartnerService.insert(tp2);
        newRecordsCount ++;
        
        records = tradingPartnerService.select(new TradingPartnerExHolder());
        
        assertTrue(records.size() == curRecords + newRecordsCount);
        
        tradingPartnerService.delete(tp1);
        tradingPartnerService.delete(tp2);

        termConditionService.delete(tc1);
        termConditionService.delete(tc2);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
        
        records = tradingPartnerService.select(new TradingPartnerExHolder());
        
        assertTrue(records.size() == curRecords);
    }
    
    
    public void testUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        termConditionService.insert(tc1);
        termConditionService.insert(tc2);
        
        tradingPartnerService.insert(tp1);
        tradingPartnerService.insert(tp2);
        
        TradingPartnerExHolder tp = new TradingPartnerExHolder();
        tp.setTradingPartnerOid(tp1.getTradingPartnerOid());
        tp.setBuyerSupplierCode("BS_CODE");
        
        tradingPartnerService.updateByPrimaryKeySelective(tp1, tp);
        
        TradingPartnerHolder t = tradingPartnerService.selectTradingPartnerByKey(tp1.getTradingPartnerOid());
        assertNotNull(t);
        assertEquals(t.getBuyerSupplierCode(), tp.getBuyerSupplierCode());
        assertEquals(t.getSupplierBuyerCode(), tp1.getSupplierBuyerCode());

        tp = new TradingPartnerExHolder();
        BeanUtils.copyProperties(tp2, tp);
        tp.setSupplierBuyerCode("SB_CODE");
        
        tradingPartnerService.updateByPrimaryKey(tp2, tp);
        
        t = tradingPartnerService.selectTradingPartnerByKey(tp2.getTradingPartnerOid());
        assertNotNull(t);
        assertEquals(t.getSupplierBuyerCode(), tp.getSupplierBuyerCode());
        
        TradingPartnerHolder tpParam = new TradingPartnerHolder();
        tpParam.setTradingPartnerOid(tp1.getTradingPartnerOid());
        tradingPartnerService.delete(tpParam);
        TradingPartnerHolder tpParam2 = new TradingPartnerHolder();
        tpParam2.setTradingPartnerOid(tp2.getTradingPartnerOid());
        tradingPartnerService.delete(tpParam2);

        termConditionService.delete(tc1);
        termConditionService.delete(tc2);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }

    
    public void testSelectTradingPartnerByGroupOidAndSupplierOid() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        termConditionService.insert(tc1);
        termConditionService.insert(tc2);
        
        tradingPartnerService.insert(tp1);
        tradingPartnerService.insert(tp2);
        
        groupService.insert(group);
        groupTradingPartnerService.insert(gtp);
        
        List<TradingPartnerExHolder> rlt = tradingPartnerService.selectTradingPartnerByGroupOidAndSupplierOid(group.getGroupOid(), supplier.getSupplierOid());

        assertNotNull(rlt);
        assertEquals(rlt.size(), 1);
        assertEquals(rlt.get(0).getTradingPartnerOid(), tp1.getTradingPartnerOid());
        
        groupTradingPartnerService.delete(gtp);
        groupService.delete(group);
        
        tradingPartnerService.delete(tp1);
        tradingPartnerService.delete(tp2);
        
        termConditionService.delete(tc1);
        termConditionService.delete(tc2);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testSelectTradingPartnerByTradingPartnerOids() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        termConditionService.insert(tc1);
        termConditionService.insert(tc2);
        
        tradingPartnerService.insert(tp1);
        tradingPartnerService.insert(tp2);
        
        List<BigDecimal> oids = new ArrayList<BigDecimal>();
        oids.add(tp1.getTradingPartnerOid());
        oids.add(tp2.getTradingPartnerOid());
        
        List<TradingPartnerHolder> rlt = tradingPartnerService.selectTradingPartnerByTradingPartnerOids(oids);
        
        assertNotNull(rlt);
        assertEquals(rlt.size(), 2);
        
        tradingPartnerService.delete(tp1);
        tradingPartnerService.delete(tp2);

        termConditionService.delete(tc1);
        termConditionService.delete(tc2);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testSelectTradingPartnerTradingPartnerOids() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        termConditionService.insert(tc1);
        termConditionService.insert(tc2);
        
        tradingPartnerService.insert(tp1);
        tradingPartnerService.insert(tp2);
        
        List<BigDecimal> oids = new ArrayList<BigDecimal>();
        oids.add(tp1.getTradingPartnerOid());
        oids.add(tp2.getTradingPartnerOid());
        
        TradingPartnerExHolder tpe = new TradingPartnerExHolder();
        tpe.setTradingPartnerOids(oids);
        
        List<TradingPartnerExHolder> rlt = tradingPartnerService.selectTradingPartnerTradingPartnerOids(tpe);
        
        assertNotNull(rlt);
        assertEquals(rlt.size(), 2);
        
        tradingPartnerService.delete(tp1);
        tradingPartnerService.delete(tp2);
        
        termConditionService.delete(tc1);
        termConditionService.delete(tc2);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void setUp() throws Exception
    {
        OidService oidService = ctx.getBean("oidService", OidService.class);

        buyer = new BuyerHolder();
        buyer.setBuyerOid(oidService.getOid());
        buyer.setBuyerCode("testBuyerCode");
        buyer.setBuyerName("testBuyerName");
        buyer.setBranch(false);
        buyer.setAddress1("testBuyerAddr1");
        buyer.setCtryCode("SG");
        buyer.setCurrCode("SGD");
        buyer.setActive(true);
        buyer.setBlocked(false);
        buyer.setCreateBy(LOGIN_ID);
        buyer.setCreateDate(new Date());
        buyer.setMboxId("testBuyerMbox");
        buyer.setChannel("testBuyerChannel");
        buyer.setContactEmail("jkge@pracbiz.com");
        buyer.setContactFax("025-23387491");
        buyer.setContactMobile("086-15150542443");
        buyer.setContactName("JianKuiGe");
        buyer.setContactTel("025-23387492");
        
        supplier = new SupplierHolder();
        supplier.setSupplierOid(oidService.getOid());
        supplier.setSupplierCode("testSupplierCode");
        supplier.setSupplierName("testSupplierName");
        supplier.setBranch(false);
        supplier.setAddress1("testSupplierAddr1");
        supplier.setCtryCode("SG");
        supplier.setCurrCode("SGD");
        supplier.setActive(true);
        supplier.setBlocked(false);
        supplier.setCreateBy(LOGIN_ID);
        supplier.setCreateDate(new Date());
        supplier.setMboxId("testSupplierMbox");
        supplier.setContactEmail("jkge@pracbiz.com");
        supplier.setContactFax("025-23387493");
        supplier.setContactMobile("086-15150542443");
        supplier.setContactName("JianKuiGe");
        supplier.setContactTel("025-23387494");
        
        tc1 = new TermConditionHolder();
        tc1.setTermConditionOid(oidService.getOid());
        tc1.setTermConditionCode("TC_01");
        tc1.setSeq((short) 1);
        tc1.setTermCondition1("TC_01_1");
        tc1.setTermCondition2("TC_01_2");
        tc1.setTermCondition3("TC_01_3");
        tc1.setTermCondition4("TC_01_4");
        tc1.setDefaultSelected(false);
        tc1.setSupplierOid(supplier.getSupplierOid());
        
        tc2 = new TermConditionHolder();
        tc2.setTermConditionOid(oidService.getOid());
        tc2.setTermConditionCode("TC_02");
        tc2.setSeq((short) 2);
        tc2.setTermCondition1("TC_02_1");
        tc2.setTermCondition2("TC_02_2");
        tc2.setTermCondition3("TC_02_3");
        tc2.setTermCondition4("TC_02_4");
        tc2.setDefaultSelected(true);
        tc2.setSupplierOid(supplier.getSupplierOid());
        
        tp1 = new TradingPartnerExHolder();
        tp1.setTradingPartnerOid(oidService.getOid());
        tp1.setCreateDate(new Date());
        tp1.setCreateBy(LOGIN_ID);
        tp1.setBuyerOid(buyer.getBuyerOid());
        tp1.setSupplierOid(supplier.getSupplierOid());
        tp1.setActive(true);
        tp1.setTermConditionOid(tc1.getTermConditionOid());
        tp1.setConcessive(true);
        tp1.setBuyerSupplierCode("SUPPLIER");
        tp1.setSupplierBuyerCode("BUYER");
        tp1.setSupplierCode(supplier.getSupplierCode());
        tp1.setSupplierContactEmail(supplier.getContactEmail());
        tp1.setSupplierContactFax(supplier.getContactFax());
        tp1.setSupplierContactMobile(supplier.getContactMobile());
        tp1.setSupplierContactTel(supplier.getContactTel());
        tp1.setSupplierContactPerson(supplier.getContactName());
        tp1.setBuyerContactEmail(buyer.getContactEmail());
        tp1.setBuyerContactFax(buyer.getContactFax());
        tp1.setBuyerContactMobile(buyer.getContactMobile());
        tp1.setBuyerContactPerson(buyer.getContactName());
        tp1.setBuyerContactTel(buyer.getContactTel());
     
        tp2 = new TradingPartnerExHolder();
        tp2.setTradingPartnerOid(oidService.getOid());
        tp2.setCreateDate(new Date());
        tp2.setCreateBy(LOGIN_ID);
        tp2.setBuyerOid(buyer.getBuyerOid());
        tp2.setSupplierOid(supplier.getSupplierOid());
        tp2.setActive(true);
        tp2.setTermConditionOid(tc2.getTermConditionOid());
        tp2.setConcessive(true);
        tp2.setBuyerSupplierCode("SUPPLIER_2");
        tp2.setSupplierBuyerCode("BUYER_2");
        tp2.setSupplierCode(supplier.getSupplierCode());
        tp2.setSupplierContactEmail(supplier.getContactEmail());
        tp2.setSupplierContactFax(supplier.getContactFax());
        tp2.setSupplierContactMobile(supplier.getContactMobile());
        tp2.setSupplierContactTel(supplier.getContactTel());
        tp2.setSupplierContactPerson(supplier.getContactName());
        tp2.setBuyerContactEmail(buyer.getContactEmail());
        tp2.setBuyerContactFax(buyer.getContactFax());
        tp2.setBuyerContactMobile(buyer.getContactMobile());
        tp2.setBuyerContactPerson(buyer.getContactName());
        tp2.setBuyerContactTel(buyer.getContactTel());
        
        group = new GroupHolder();
        group.setGroupOid(oidService.getOid());
        group.setGroupId("GROUP_ID");
        group.setBuyerOid(buyer.getBuyerOid());
        group.setSupplierOid(supplier.getSupplierOid());
        group.setUserTypeOid(BigDecimal.valueOf(2));
        group.setGroupType(GroupType.BUYER);
        group.setGroupName("Buyer Group");
        group.setCreateBy(LOGIN_ID);
        group.setCreateDate(new Date());
        
        gtp = new GroupTradingPartnerHolder();
        gtp.setTradingPartnerOid(tp1.getTradingPartnerOid());
        gtp.setGroupOid(group.getGroupOid());
    }
}
