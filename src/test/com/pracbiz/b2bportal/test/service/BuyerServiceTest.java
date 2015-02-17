package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.GroupType;
import com.pracbiz.b2bportal.core.constants.LastUpdateFrom;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;
import com.pracbiz.b2bportal.core.service.GroupSupplierTmpService;
import com.pracbiz.b2bportal.core.service.GroupTmpService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerTmpService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.GroupUserTmpService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class BuyerServiceTest extends BaseServiceTestCase
{
    private BuyerHolder b1, b2, b3;
    private SupplierHolder s1;
    private BuyerMsgSettingHolder bm1, bm2;
    private GroupTmpHolder g1, g2;
    private RoleTmpHolder r1;
    private TradingPartnerHolder tp1;
    private UserProfileTmpHolder u1, u2;
    private GroupSupplierTmpHolder gs;
    private GroupUserTmpHolder gu1, gu2;
    private GroupTradingPartnerTmpHolder gtp;
    private byte[] logo;
    
    
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleTmpService roleTmpService;
    @Autowired
    private BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private OidService oidService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileTmpService userProfileTmpService;
    @Autowired
    private TradingPartnerService tradingPartnerService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupTmpService groupTmpService;
    @Autowired
    private GroupSupplierService groupSupplierService;
    @Autowired
    private GroupSupplierTmpService groupSupplierTmpService;
    @Autowired
    private GroupTradingPartnerService groupTradingPartnerService;
    @Autowired
    private GroupTradingPartnerTmpService groupTradingPartnerTmpService;
    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private GroupUserTmpService groupUserTmpService;
    
    
    private static final String BUYER1_CODE = "testCode1";
    private static final String BUYER2_CODE = "testCode2";
    private static final String BUYER3_CODE = "testCode3";
    
    private static final String BUYER1_NAME = "testName1";
    private static final String BUYER2_NAME = "testName2";
    private static final String BUYER3_NAME = "testName3";
    
    private static final String BUYER1_MBOXID = "testMbox1";
    private static final String BUYER2_MBOXID = "testMbox2";
    private static final String BUYER3_MBOXID = "testMbox3";
    
    private static final String MSG_TYPE_PO = "PO";
    private static final String MSG_TYPE_INV = "INV";
    
    private static final String GROUP1_ID = "GROUP1_ID";
    
    private static final String GROUP1_NAME = "GROUP1_NAME";
    
    private static final String CREATOR = "JunitTester";
    
    public BuyerServiceTest()
    {
        buyerService = ctx.getBean("buyerService", BuyerService.class);
        roleService = ctx.getBean("roleService", RoleService.class);
        roleTmpService = ctx.getBean("roleTmpService", RoleTmpService.class);
        buyerMsgSettingService = ctx.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        oidService = ctx.getBean("oidService", OidService.class);
        supplierService = ctx.getBean("supplierService", SupplierService.class);
        userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
        userProfileTmpService = ctx.getBean("userProfileTmpService", UserProfileTmpService.class);
        tradingPartnerService = ctx.getBean("tradingPartnerService", TradingPartnerService.class);
        groupService = ctx.getBean("groupService", GroupService.class);
        groupTmpService = ctx.getBean("groupTmpService", GroupTmpService.class);
        groupSupplierService = ctx.getBean("groupSupplierService", GroupSupplierService.class);
        groupSupplierTmpService = ctx.getBean("groupSupplierTmpService", GroupSupplierTmpService.class);
        groupTradingPartnerService = ctx.getBean("groupTradingPartnerService", GroupTradingPartnerService.class);
        groupTradingPartnerTmpService = ctx.getBean("groupTradingPartnerTmpService", GroupTradingPartnerTmpService.class);
        groupUserService = ctx.getBean("groupUserService", GroupUserService.class);
        groupUserTmpService = ctx.getBean("groupUserTmpService", GroupUserTmpService.class);
    }
    
    public void testBasicCrd() throws Exception
    {
        int originalNumOfRecords = 0;
        
        List<? extends Object> records = buyerService.select(new BuyerHolder());
        
        if (null != records && !records.isEmpty())
        {
            originalNumOfRecords = records.size();
        }
        
        buyerService.insert(b1);
        buyerService.insert(b2);
        
        records = buyerService.select(new BuyerHolder());
        assertTrue(records.size() == originalNumOfRecords + 2);
        
        BuyerHolder rlt = buyerService.selectBuyerByBuyerCode(BUYER1_CODE);
        assertNotNull(rlt);
        assertEquals(rlt.getBuyerName(), BUYER1_NAME);
        assertEquals(rlt.getMboxId(), BUYER1_MBOXID);
        
        rlt = buyerService.selectBuyerByKey(rlt.getBuyerOid());
        assertNotNull(rlt);
        assertEquals(rlt.getBuyerCode(), BUYER1_CODE);
        assertEquals(rlt.getBuyerName(), BUYER1_NAME);
        assertEquals(rlt.getMboxId(), BUYER1_MBOXID);
        
        rlt = buyerService.selectBuyerWithBlobsByKey(b1.getBuyerOid());
        assertNotNull(rlt);
        assertEquals(rlt.getBuyerCode(), BUYER1_CODE);
        assertEquals(rlt.getBuyerName(), BUYER1_NAME);
        
        rlt = buyerService.selectBuyerByMboxId(BUYER2_MBOXID);
        assertNotNull(rlt);
        assertEquals(rlt.getBuyerCode(), BUYER2_CODE);
        assertEquals(rlt.getBuyerName(), BUYER2_NAME);
        
        records = buyerService.selectActiveBuyers();
        assertTrue(records.size() == originalNumOfRecords + 2);
        BuyerHolder b = new BuyerHolder();
        b.setBuyerOid(b1.getBuyerOid());
        buyerService.delete(b1);
        b.setBuyerOid(b2.getBuyerOid());
        buyerService.delete(b2);
        records = buyerService.select(new BuyerHolder());
        assertTrue(records.size() == originalNumOfRecords);
    }
    
    
    public void testInsertBuyerWithMsgSetting() throws Exception
    {
        int originalNumOfRecords = 0;
        List<? extends Object> records = buyerMsgSettingService.select(new BuyerMsgSettingHolder());
        if (null != records && !records.isEmpty())
        {
            originalNumOfRecords = records.size();
        }
        
        b1.addBuyerMsgSetting(bm1);
        b1.addBuyerMsgSetting(bm2);
        buyerService.insertBuyerWithMsgSetting(this.getCommonParameter(false), b1);
        BuyerHolder rlt = buyerService.selectBuyerByKey(b1.getBuyerOid());
        assertNotNull(rlt);
        assertEquals(rlt.getBuyerCode(), BUYER1_CODE);
        assertEquals(rlt.getBuyerName(), BUYER1_NAME);
        assertEquals(rlt.getMboxId(), BUYER1_MBOXID);
        List<BuyerMsgSettingHolder> list = buyerMsgSettingService.selectBuyerMsgSettingsByBuyerOid(rlt.getBuyerOid());
        assertNotNull(list.contains(bm1));
        assertNotNull(list.contains(bm2));
        
        buyerMsgSettingService.delete(bm1);
        buyerMsgSettingService.delete(bm2);
        BuyerHolder b = new BuyerHolder();
        b.setBuyerOid(b1.getBuyerOid());
        buyerService.delete(b);
    }
    
    
    public void testUpdateByPrimaryKeySelective() throws Exception
    {
        BuyerHolder obj = new BuyerHolder();
        obj.setBuyerOid(b1.getBuyerOid());
        obj.setAddress1("testAddress");
        buyerService.insert(b1);
        buyerService.updateByPrimaryKeySelective(b1, obj);
        BuyerHolder rlt = buyerService.selectBuyerByKey(obj.getBuyerOid());
        assertNotNull(rlt);
        assertEquals(rlt.getAddress1(), obj.getAddress1());
        assertEquals(rlt.getBuyerName(), b1.getBuyerName());
        BuyerHolder b = new BuyerHolder();
        b.setBuyerOid(b1.getBuyerOid());
        buyerService.delete(b);
    }
    
    
    public void testUpdateByPrimaryKey() throws Exception
    {
        b3.setBuyerOid(b1.getBuyerOid());
        buyerService.insert(b1);
        buyerService.updateByPrimaryKeySelective(b1, b3);
        BuyerHolder rlt = buyerService.selectBuyerByKey(b3.getBuyerOid());
        assertNotNull(rlt);
        assertTrue(rlt.getBuyerName().equals(b3.getBuyerName()));
        BuyerHolder b = new BuyerHolder();
        b.setBuyerOid(b1.getBuyerOid());
        buyerService.delete(b);
    }
    
    
    public void testUpdateByPrimaryKeyWithBLOBs() throws Exception
    {
        b3.setBuyerOid(b1.getBuyerOid());
        buyerService.insert(b1);
        buyerService.updateByPrimaryKeySelective(b1, b3);
        BuyerHolder rlt = buyerService.selectBuyerByKey(b3.getBuyerOid());
        assertNotNull(rlt);
        assertEquals(rlt.getAddress1(), b3.getAddress1());
        BuyerHolder b = new BuyerHolder();
        b.setBuyerOid(b1.getBuyerOid());
        buyerService.delete(b);
    }
    
    
    public void testDeleteBuyer() throws Exception
    {
        buyerService.insert(b1);
        buyerMsgSettingService.insert(bm1);
        roleService.insert(r1);
        roleTmpService.insert(r1);
        userProfileService.insert(u1);
        userProfileTmpService.insert(u1);
        supplierService.insert(s1);
        tradingPartnerService.insert(tp1);
        groupService.insert(g1);
        groupTmpService.insert(g1);
        groupSupplierService.insert(gs);
        groupSupplierTmpService.insert(gs);
        groupUserService.insert(gu1);
        groupUserTmpService.insert(gu1);
        groupService.insert(g2);
        groupTmpService.insert(g2);
        groupTradingPartnerService.insert(gtp);
        groupTradingPartnerTmpService.insert(gtp);
        buyerService.deleteBuyer(this.getCommonParameter(false), b1.getBuyerOid());
        supplierService.deleteSupplier(this.getCommonParameter(false), s1.getSupplierOid());
        BaseHolder rlt = buyerService.selectBuyerByKey(b1.getBuyerOid());
        assertNull(rlt);
        List<? extends BaseHolder> list = buyerMsgSettingService.selectBuyerMsgSettingsByBuyerOid(b1.getBuyerOid());
        assertEquals(0, list.size());
        rlt = roleService.selectByKey(r1.getRoleOid());
        assertNull(rlt);
        rlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        assertNull(rlt);
        rlt = userProfileService.selectUserProfileByKey(u1.getUserOid());
        assertNull(rlt);
        rlt = userProfileTmpService.selectUserProfileTmpByKey(u1.getUserOid());
        assertNull(rlt);
        rlt = tradingPartnerService.selectTradingPartnerByKey(tp1.getTradingPartnerOid());
        assertNull(rlt);
        rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNull(rlt);
        rlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNull(rlt);
        list = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertEquals(0, list.size());
        list = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertEquals(0, list.size());
        list = groupUserService.selectGroupUserByGroupOid(gu1.getGroupOid());
        assertEquals(0, list.size());
        list = groupUserTmpService.selectGroupUserTmpByGroupOid(gu1.getGroupOid());
        assertEquals(0, list.size());
        rlt = groupService.selectGroupByKey(g2.getGroupOid());
        assertNull(rlt);
        rlt = groupTmpService.selectGroupTmpByKey(g2.getGroupOid());
        assertNull(rlt);
        list = groupTradingPartnerService.selectGroupTradingPartnerByTradingPartnerOid(tp1.getTradingPartnerOid());
        assertEquals(0, list.size());
        list = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(gtp.getGroupOid());
        assertEquals(0, list.size());
        rlt = supplierService.selectSupplierByKey(s1.getSupplierOid());
        assertNull(rlt);
    }
    
    
    @Before
    public void setUp() throws Exception
    {
        initBuyer();
        initSuppliers();
        initTradingPartner();
        initRole();
        initUsers();
        initBuyerMsgSetting();
        initGroup();
        initGroupSupplier();
        initGroupTradingPartner();
        initGroupUser();
        logo = "buyerLogo".getBytes();
    }
    
    
    public void initBuyerMsgSetting() throws Exception
    {
        bm1 = new BuyerMsgSettingHolder();
        bm1.setBuyerOid(b1.getBuyerOid());
        bm1.setMsgType(MSG_TYPE_PO);
        bm1.setAlertFrequency("BATCH");
        bm1.setExcludeSucc(false);
        
        bm2 = new BuyerMsgSettingHolder();
        bm2.setBuyerOid(b1.getBuyerOid());
        bm2.setMsgType(MSG_TYPE_INV);
        bm2.setAlertFrequency("BATCH");
        bm2.setExcludeSucc(false);
    }
    
    
    private void initBuyer() throws Exception
    {
        b1 = new BuyerHolder();
        b1.setBuyerOid(oidService.getOid());
        b1.setBuyerCode(BUYER1_CODE);
        b1.setBuyerName(BUYER1_NAME);
        b1.setBranch(false);
        b1.setAddress1("testBuyerAddr1");
        b1.setCtryCode("SG");
        b1.setCurrCode("SGD");
        b1.setActive(true);
        b1.setBlocked(false);
        b1.setCreateBy(CREATOR);
        b1.setCreateDate(new Date());
        b1.setMboxId(BUYER1_MBOXID);
        b1.setLogo(logo);
        b1.setChannel("testBuyerChannel");
        
        b2 = new BuyerHolder();
        b2.setBuyerOid(oidService.getOid());
        b2.setBuyerCode(BUYER2_CODE);
        b2.setBuyerName(BUYER2_NAME);
        b2.setBranch(false);
        b2.setAddress1("testBuyerAddr1");
        b2.setCtryCode("SG");
        b2.setCurrCode("SGD");
        b2.setActive(true);
        b2.setBlocked(false);
        b2.setCreateBy(CREATOR);
        b2.setCreateDate(new Date());
        b2.setMboxId(BUYER2_MBOXID);
        b2.setLogo(logo);
        b2.setChannel("testBuyerChannel");
        
        b3 = new BuyerHolder();
        b3.setBuyerOid(oidService.getOid());
        b3.setBuyerCode(BUYER3_CODE);
        b3.setBuyerName(BUYER3_NAME);
        b3.setBranch(false);
        b3.setAddress1("testBuyerAddr1");
        b3.setCtryCode("SG");
        b3.setCurrCode("SGD");
        b3.setActive(true);
        b3.setBlocked(false);
        b3.setCreateBy(CREATOR);
        b3.setCreateDate(new Date());
        b3.setMboxId(BUYER3_MBOXID);
        b3.setLogo(logo);
        b3.setChannel("testBuyerChannel");
    }
    
    
    private void initGroup() throws Exception
    {
        g1 = new GroupTmpHolder();
        g1.setGroupOid(oidService.getOid());
        g1.setGroupId(GROUP1_ID);
        g1.setGroupName(GROUP1_NAME);
        g1.setGroupType(GroupType.BUYER);
        g1.setCreateDate(new Date());
        g1.setCreateBy(CREATOR);
        g1.setUserTypeOid(BigDecimal.valueOf(2));
        g1.setBuyerOid(b1.getBuyerOid());
        g1.setActor(this.getCommonParameter(false).getLoginId());
        g1.setActionType(DbActionType.CREATE);
        g1.setActionDate(new Date());
        g1.setCtrlStatus(MkCtrlStatus.APPROVED);
        
        g2 = new GroupTmpHolder();
        g2.setGroupOid(oidService.getOid());
        g2.setGroupId(GROUP1_ID);
        g2.setGroupName(GROUP1_NAME);
        g2.setGroupType(GroupType.BUYER);
        g2.setCreateDate(new Date());
        g2.setCreateBy(CREATOR);
        g2.setUserTypeOid(BigDecimal.valueOf(2));
        g2.setSupplierOid(s1.getSupplierOid());
        g2.setActor(this.getCommonParameter(false).getLoginId());
        g2.setActionType(DbActionType.CREATE);
        g2.setActionDate(new Date());
        g2.setCtrlStatus(MkCtrlStatus.APPROVED);
    }
    
    
    private void initGroupUser()
    {
        gu1 = new GroupUserTmpHolder();
        gu1.setGroupOid(g1.getGroupOid());
        gu1.setUserOid(u1.getUserOid());
        gu1.setLastUpdateFrom(LastUpdateFrom.GROUP);
        gu1.setActionType(DbActionType.CREATE);
        gu1.setApproved(true);
        
        gu2 = new GroupUserTmpHolder();
        gu2.setGroupOid(g2.getGroupOid());
        gu2.setUserOid(u2.getUserOid());
        gu2.setLastUpdateFrom(LastUpdateFrom.GROUP);
        gu2.setActionType(DbActionType.CREATE);
        gu2.setApproved(true);
    }
    
    
    private void initGroupSupplier()
    {
        gs = new GroupSupplierTmpHolder();
        gs.setGroupOid(g1.getGroupOid());
        gs.setSupplierOid(s1.getSupplierOid());
    }
    
    
    private void initGroupTradingPartner() throws Exception
    {
        gtp = new GroupTradingPartnerTmpHolder();
        gtp.setGroupOid(g2.getGroupOid());
        gtp.setTradingPartnerOid(tp1.getTradingPartnerOid());
    }
    
    
    private void initSuppliers() throws Exception
    {
        s1 = new SupplierHolder();
        s1.setSupplierOid(oidService.getOid());
        s1.setSupplierCode("testSupplierCode");
        s1.setSupplierName("testSupplierName");
        s1.setBranch(false);
        s1.setCtryCode("SG");
        s1.setCurrCode("SGD");
        s1.setActive(true);
        s1.setBlocked(false);
        s1.setCreateBy(this.getCommonParameter(false).getLoginId());
        s1.setCreateDate(new Date());
        s1.setAddress1("testSupplierAddr1");
        s1.setContactName(CREATOR);
        s1.setMboxId("testSupplierMbox"); 
        s1.setContactTel("1111111111");
        s1.setContactEmail("testJuniter@pracbiz.com");
    }
    
    
    private void initUsers() throws Exception
    {
        u1 = new UserProfileTmpHolder();
        u1.setUserOid(oidService.getOid());
        u1.setLoginId("GE");
        u1.setUserName("Ge");
        u1.setLoginMode("PASSWORD");
        u1.setEmail("jkge@pracbiz.com");
        u1.setGender("1");
        u1.setActive(true);
        u1.setBlocked(false);
        u1.setInit(false);
        u1.setCreateDate(new Date());
        u1.setCreateBy(this.getCommonParameter(false).getLoginId());
        u1.setBuyerOid(b1.getBuyerOid());
        u1.setUserType(BigDecimal.valueOf(2));
        u1.setActor(this.getCommonParameter(false).getLoginId());
        u1.setActionType(DbActionType.CREATE);
        u1.setActionDate(new Date());
        u1.setCtrlStatus(MkCtrlStatus.APPROVED);
        
        u2 = new UserProfileTmpHolder();
        u2.setUserOid(oidService.getOid());
        u2.setLoginId("JIAN");
        u2.setUserName("Jian");
        u2.setLoginMode("PASSWORD");
        u2.setEmail("jkge@pracbiz.com");
        u2.setGender("1");
        u2.setActive(true);
        u2.setBlocked(false);
        u2.setInit(false);
        u2.setCreateDate(new Date());
        u2.setCreateBy(this.getCommonParameter(false).getLoginId());
        u2.setSupplierOid(s1.getSupplierOid());
        u2.setUserType(BigDecimal.valueOf(3));
        u2.setActor(this.getCommonParameter(false).getLoginId());
        u2.setActionType(DbActionType.CREATE);
        u2.setActionDate(new Date());
        u2.setCtrlStatus(MkCtrlStatus.APPROVED);
    }
    
    
    private void initTradingPartner() throws Exception
    {
        tp1 = new TradingPartnerHolder();
        tp1.setTradingPartnerOid(oidService.getOid());
        tp1.setBuyerSupplierCode("fds");
        tp1.setSupplierBuyerCode("fds");
        tp1.setBuyerContactPerson("youwenwu");
        tp1.setBuyerContactTel("12345678");
        tp1.setBuyerContactEmail("wwyou@pracbiz.com");
        tp1.setSupplierContactPerson("youwenwu");
        tp1.setSupplierContactTel("12345678");
        tp1.setSupplierContactEmail("wwyou@pracbiz.com");
        tp1.setActive(true);
        tp1.setCreateDate(new Date());
        tp1.setCreateBy(this.getCommonParameter(false).getLoginId());
        tp1.setBuyerOid(b1.getBuyerOid());
        tp1.setSupplierOid(s1.getSupplierOid());
    }
    
    
    private void initRole() throws Exception
    {
        r1 = new RoleTmpHolder();
        BigDecimal oid = oidService.getOid();
        r1.setBuyerOid(b1.getBuyerOid());
        r1.setRoleOid(oid);
        r1.setRoleId("ROLE1_ID");
        r1.setRoleName("ROLE1_NAME");
        r1.setRoleType(RoleType.BUYER);
        r1.setCreateDate(new Date());
        r1.setCreateBy(CREATOR);
        r1.setUserTypeOid(BigDecimal.ONE);
        r1.setActor(this.getCommonParameter(false).getLoginId());
        r1.setActionType(DbActionType.CREATE);
        r1.setActionDate(new Date());
        r1.setCtrlStatus(MkCtrlStatus.APPROVED);
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
