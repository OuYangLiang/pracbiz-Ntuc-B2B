package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
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
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.RoleTmpExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.GroupSupplierMapper;
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
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierRoleService;
import com.pracbiz.b2bportal.core.service.SupplierRoleTmpService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class SupplierServiceTest extends BaseServiceTestCase
{
    private SupplierHolder s1, s2;
    private BuyerHolder b1;
    private SupplierMsgSettingHolder sm1, sm2;
    private BuyerMsgSettingHolder bm1, bm2;
    private GroupTmpHolder g1, g2;
    private RoleTmpHolder r1, r2;
    private SupplierRoleHolder sr1, sr2;
    private SupplierRoleTmpHolder srt1, srt2;
    private TradingPartnerHolder tp1;
    private UserProfileTmpHolder u1, u2;
    private GroupSupplierTmpHolder gs;
    private GroupUserTmpHolder gu1, gu2;
    private GroupTradingPartnerTmpHolder gtp;
    private byte[] logo;
    
    
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private SupplierMsgSettingService supplierMsgSettingService;
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
    @Autowired
    private GroupSupplierMapper groupSupplierMapper;
    @Autowired
    private SupplierRoleService supplierRoleService;
    @Autowired
    private SupplierRoleTmpService supplierRoleTmpService;
    
    
    private static final String SUPP1_CODE = "testCode1";
    private static final String SUPP2_CODE = "testCode2";
    
    private static final String SUPP1_NAME = "testName1";
    private static final String SUPP2_NAME = "testName2";
    
    private static final String SUPP1_MBOXID = "testMbox1";
    private static final String SUPP2_MBOXID = "testMbox2";
    
    private static final String CREATOR = "JunitTester";
    
    private static final String MSG_TYPE_PO = "PO";
    private static final String MSG_TYPE_INV = "INV";
    
    public SupplierServiceTest()
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
        groupSupplierMapper = ctx.getBean("groupSupplierMapper", GroupSupplierMapper.class);
        supplierMsgSettingService = ctx.getBean("supplierMsgSettingService", SupplierMsgSettingService.class);
        supplierRoleService = ctx.getBean("supplierRoleService", SupplierRoleService.class);
        supplierRoleTmpService = ctx.getBean("supplierRoleTmpService", SupplierRoleTmpService.class);
    }
    
    public void testBasicCrd() throws Exception
    {
        int originalNumOfRecords = 0;
        
        List<? extends Object> records = supplierService.select(new SupplierExHolder());
        
        if (null != records && !records.isEmpty())
        {
            originalNumOfRecords = records.size();
        }
        
        supplierService.insert(s1);
        supplierService.insert(s2);
        
        records = supplierService.select(new SupplierExHolder());
        assertTrue(records.size() == originalNumOfRecords + 2);
        
        SupplierHolder rlt = supplierService.selectSupplierByCode(SUPP1_CODE);
        assertNotNull(rlt);
        assertEquals(rlt.getSupplierName(), SUPP1_NAME);
        assertEquals(rlt.getMboxId(), SUPP1_MBOXID);
        
        rlt = supplierService.selectSupplierByKey(s2.getSupplierOid());
        assertNotNull(rlt);
        assertEquals(rlt.getSupplierCode(), SUPP2_CODE);
        assertEquals(rlt.getSupplierName(), SUPP2_NAME);
        assertEquals(rlt.getMboxId(), SUPP2_MBOXID);
        
        rlt = supplierService.selectSupplierByMboxId(SUPP1_MBOXID);
        assertNotNull(rlt);
        assertEquals(rlt.getSupplierCode(), SUPP1_CODE);
        assertEquals(rlt.getSupplierName(), SUPP1_NAME);
        
        records = supplierService.selectWithBLOBs(s1);
        assertNotNull(((SupplierHolder) records.get(0)).getLogo());

        rlt = supplierService.selectSupplierWithBlobsByKey(s1.getSupplierOid());
        assertNotNull(rlt.getLogo());
        
        List<BigDecimal> oids = new ArrayList<BigDecimal>();
        oids.add(s1.getSupplierOid());
        oids.add(s2.getSupplierOid());
        records = supplierService.selectSupplierBySupplierOids(oids);
        assertEquals(2 , records.size());
        
        records = supplierService.selectActiveSuppliers();
        assertTrue(records.size() == originalNumOfRecords + 2);
        
        supplierService.delete(s1);
        supplierService.delete(s2);
        records = supplierService.select(new SupplierExHolder());
        assertTrue(records.size() == originalNumOfRecords);
    }
    
    
    public void testSelectSupplierByGroupOidAndBuyerOid() throws Exception
    {
        supplierService.insert(s1);
        buyerService.insert(b1);
        groupService.insert(g1);
        groupTmpService.insert(g1);
        groupSupplierMapper.insert(gs);
        groupSupplierTmpService.insert(gs);
        tradingPartnerService.insert(tp1);
        List<SupplierHolder> list = supplierService.selectSupplierByGroupOidAndBuyerOid(g1.getGroupOid(), b1.getBuyerOid());
        assertEquals(s1.getSupplierCode(), list.get(0).getSupplierCode());
        list = supplierService.selectSupplierByTmpGroupOidAndBuyerOid(g1.getGroupOid(), b1.getBuyerOid());
        assertEquals(s1.getSupplierCode(), list.get(0).getSupplierCode());
        supplierService.deleteSupplier(this.getCommonParameter(false), s1.getSupplierOid());
        buyerService.deleteBuyer(this.getCommonParameter(false), b1.getBuyerOid());
    }
    
    
    public void testUpdateByPrimaryKeySelective() throws Exception
    {
        supplierService.insert(s1);
        SupplierHolder s3 = new SupplierHolder();
        BeanUtils.copyProperties(s1, s3);
        s3.setSupplierName(null);
        supplierService.updateByPrimaryKeySelective(s1, s3);
        SupplierHolder s = supplierService.selectSupplierByKey(s1.getSupplierOid());
        assertEquals(s1.getSupplierName(), s.getSupplierName());
        supplierService.deleteSupplier(this.getCommonParameter(false), s1.getSupplierOid());
    }
    
    
    public void testUpdateByPrimaryKey() throws Exception
    {
        supplierService.insert(s1);
        SupplierHolder s3 = new SupplierHolder();
        BeanUtils.copyProperties(s1, s3);
        s3.setSupplierName("supp3Name");
        supplierService.updateByPrimaryKey(s1, s3);
        SupplierHolder s = supplierService.selectSupplierByKey(s1.getSupplierOid());
        assertEquals(s3.getSupplierName(), s.getSupplierName());
        supplierService.deleteSupplier(this.getCommonParameter(false), s1.getSupplierOid());
    }
    
    
    public void testUpdateByPrimaryKeyWithBLOBs() throws Exception
    {
        supplierService.insert(s1);
        SupplierHolder s3 = new SupplierHolder();
        BeanUtils.copyProperties(s1, s3);
        s3.setLogo("s3Logo".getBytes());
        supplierService.updateByPrimaryKeyWithBLOBs(s1, s3);
        SupplierHolder s = supplierService.selectSupplierWithBlobsByKey(s1.getSupplierOid());
        assertEquals(new String(s3.getLogo()), new String(s.getLogo()));
        supplierService.deleteSupplier(this.getCommonParameter(false), s1.getSupplierOid());
    }
    
    
    public void testUpdateSupplierWithMsgSetting() throws Exception
    {
        s1.addSupplierMagSetting(sm1);
        supplierService.insertSupplierWithMsgSetting(this.getCommonParameter(false), s1);
        SupplierMsgSettingHolder sm = new SupplierMsgSettingHolder();
        BeanUtils.copyProperties(sm1, sm);
        sm.setRcpsAddrs("testwwyou@pracbiz.com");
        SupplierHolder s = new SupplierHolder();
        BeanUtils.copyProperties(s1, s);
        s.addSupplierMagSetting(sm);
        supplierService.updateSupplierWithMsgSetting(this.getCommonParameter(false), s1, s);
        List<? extends BaseHolder> list = supplierMsgSettingService.selectSupplierMsgSettingBySupplierOid(s.getSupplierOid());
        assertEquals(1, list.size());
        assertEquals(sm.getRcpsAddrs(), ((SupplierMsgSettingHolder)list.get(0)).getRcpsAddrs());
        supplierService.deleteSupplier(this.getCommonParameter(false), s.getSupplierOid());
    }
    
    
    public void testDeleteSupplier() throws Exception
    {
        buyerService.insert(b1);
        s1.addSupplierMagSetting(sm1);
        supplierService.insertSupplierWithMsgSetting(this.getCommonParameter(false), s1);
        tradingPartnerService.insert(tp1);
        roleService.insert(r1);
        roleTmpService.insert(r1);
        supplierRoleService.insert(sr1);
        userProfileService.insert(u1);
        userProfileTmpService.insert(u1);
        groupService.insert(g1);
        groupTmpService.insert(g1);
        groupService.insert(g2);
        groupTmpService.insert(g2);
        groupTradingPartnerService.insert(gtp);
        groupTradingPartnerTmpService.insert(gtp);
        groupUserService.insert(gu1);
        groupUserTmpService.insert(gu1);
        groupSupplierService.insert(gs);
        groupSupplierTmpService.insert(gs);
        assertTrue(supplierRoleService.selectByRole(sr1.getRoleOid()).size() == 1);
        supplierService.deleteSupplier(this.getCommonParameter(false), s1.getSupplierOid());
        buyerService.deleteBuyer(this.getCommonParameter(false), b1.getBuyerOid());
        BaseHolder rlt = supplierService.selectSupplierByKey(s1.getSupplierOid());
        assertNull(rlt);
        List<? extends BaseHolder> list = supplierMsgSettingService.selectSupplierMsgSettingBySupplierOid(s1.getSupplierOid());
        assertTrue(list.isEmpty());
        TradingPartnerExHolder tp = new TradingPartnerExHolder();
        tp.setSupplierOid(s1.getSupplierOid());
        list = tradingPartnerService.select(tp);
        assertTrue(list.isEmpty());
        assertTrue(supplierRoleService.selectByRole(sr1.getRoleOid()).isEmpty());
        RoleTmpExHolder role = new RoleTmpExHolder();
        role.setRoleOid(sr1.getRoleOid());
        list = roleService.select(role);
        assertTrue(list.isEmpty());
        list = roleTmpService.select(role);
        assertTrue(list.isEmpty());
        UserProfileTmpExHolder user = new UserProfileTmpExHolder();
        user.setSupplierOid(s1.getSupplierOid());
        list = userProfileService.select(user);
        assertTrue(list.isEmpty());
        list = userProfileTmpService.select(user);
        assertTrue(list.isEmpty());
        GroupTmpHolder group = new GroupTmpHolder();
        group.setSupplierOid(s1.getSupplierOid());
        list = groupService.select(group);
        assertTrue(list.isEmpty());
        list = groupTmpService.select(group);
        assertTrue(list.isEmpty());
        list = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(g1.getGroupOid());
        assertTrue(list.isEmpty());
        list = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(g1.getGroupOid());
        assertTrue(list.isEmpty());
        list = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertTrue(list.isEmpty());
        list = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertTrue(list.isEmpty());
    }
    
    
    @Before
    public void setUp() throws Exception
    {
        initSupplier();
        initBuyer();
        initSupplierMsgSetting();
        initTradingPartner();
        initRole();
        initSupplierRole();
        initUser();
        initGroup();
        initGroupTradingPartner();
        initGroupUser();
        initGroupSupplier();
    }
    
    
    private void initSupplier() throws Exception
    {
        s1 = new SupplierHolder();
        s1.setSupplierOid(oidService.getOid());
        s1.setSupplierCode(SUPP1_CODE);
        s1.setSupplierName(SUPP1_NAME);
        s1.setBranch(false);
        s1.setCtryCode("SG");
        s1.setCurrCode("SGD");
        s1.setActive(true);
        s1.setBlocked(false);
        s1.setCreateBy(this.getCommonParameter(false).getLoginId());
        s1.setCreateDate(new Date());
        s1.setAddress1("testSupplierAddr1");
        s1.setContactName(CREATOR);
        s1.setMboxId(SUPP1_MBOXID); 
        s1.setContactTel("1111111111");
        s1.setContactEmail("testJuniter@pracbiz.com");
        s1.setLogo("supp1Logo".getBytes());
        s1.setDeploymentMode(DeploymentMode.LOCAL);
        s1.setClientEnabled(false);
        s1.setRequireReport(false);
        s1.setRequireTranslationIn(false);
        s1.setRequireTranslationOut(false);
        
        s2 = new SupplierHolder();
        s2.setSupplierOid(oidService.getOid());
        s2.setSupplierCode(SUPP2_CODE);
        s2.setSupplierName(SUPP2_NAME);
        s2.setBranch(false);
        s2.setCtryCode("SG");
        s2.setCurrCode("SGD");
        s2.setActive(true);
        s2.setBlocked(false);
        s2.setCreateBy(this.getCommonParameter(false).getLoginId());
        s2.setCreateDate(new Date());
        s2.setAddress1("testSupplierAddr1");
        s2.setContactName(CREATOR);
        s2.setMboxId(SUPP2_MBOXID); 
        s2.setContactTel("1111111111");
        s2.setContactEmail("testJuniter@pracbiz.com");
        s2.setLogo("supp2Logo".getBytes());
        s2.setDeploymentMode(DeploymentMode.LOCAL);
        s2.setClientEnabled(false);
        s2.setRequireReport(false);
        s2.setRequireTranslationIn(false);
        s2.setRequireTranslationOut(false);
    }
    
    private void initGroup() throws Exception
    {
        g1 = new GroupTmpHolder();
        g1.setGroupOid(oidService.getOid());
        g1.setGroupId("GROUP1_ID");
        g1.setGroupName("GROUP1_NAME");
        g1.setGroupType(GroupType.BUYER);
        g1.setCreateDate(new Date());
        g1.setCreateBy(CREATOR);
        g1.setUserTypeOid(BigDecimal.valueOf(3));
        g1.setSupplierOid(s1.getSupplierOid());
        g1.setActor(this.getCommonParameter(false).getLoginId());
        g1.setActionDate(new Date());
        g1.setActionType(DbActionType.CREATE);
        g1.setCtrlStatus(MkCtrlStatus.APPROVED);
        
        g2 = new GroupTmpHolder();
        g2.setGroupOid(oidService.getOid());
        g2.setGroupId("GROUP2_ID");
        g2.setGroupName("GROUP2_NAME");
        g2.setGroupType(GroupType.BUYER);
        g2.setCreateDate(new Date());
        g2.setCreateBy(CREATOR);
        g2.setUserTypeOid(BigDecimal.valueOf(2));
        g2.setBuyerOid(b1.getBuyerOid());
        g2.setActor(this.getCommonParameter(false).getLoginId());
        g2.setActionDate(new Date());
        g2.setActionType(DbActionType.CREATE);
        g2.setCtrlStatus(MkCtrlStatus.APPROVED);
    }
    
    private void initBuyer() throws Exception
    {
        b1 = new BuyerHolder();
        b1.setBuyerOid(oidService.getOid());
        b1.setBuyerCode("buyer1_code");
        b1.setBuyerName("buyer1_name");
        b1.setBranch(false);
        b1.setAddress1("testBuyerAddr1");
        b1.setCtryCode("SG");
        b1.setCurrCode("SGD");
        b1.setActive(true);
        b1.setBlocked(false);
        b1.setCreateBy(CREATOR);
        b1.setCreateDate(new Date());
        b1.setMboxId("buyer1_mboxid");
        b1.setLogo(logo);
        b1.setChannel("testBuyerChannel");
        b1.setDeploymentMode(DeploymentMode.LOCAL);
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
    
    private void initGroupSupplier()
    {
        gs = new GroupSupplierTmpHolder();
        gs.setGroupOid(g1.getGroupOid());
        gs.setSupplierOid(s1.getSupplierOid());
    }
    
    private void initSupplierMsgSetting()
    {
        sm1 = new SupplierMsgSettingHolder();
        sm1.setSupplierOid(s1.getSupplierOid());
        sm1.setMsgType(MSG_TYPE_PO);
        sm1.setExcludeSucc(false);
        
        sm2 = new SupplierMsgSettingHolder();
        sm2.setSupplierOid(s1.getSupplierOid());
        sm2.setMsgType(MSG_TYPE_INV);
        sm2.setExcludeSucc(false);
    }
    
    private void initRole() throws Exception
    {
        r1 = new RoleTmpHolder();
        BigDecimal oid = oidService.getOid();
        r1.setRoleOid(oid);
        r1.setRoleId("ROLE1_ID");
        r1.setRoleName("ROLE1_NAME");
        r1.setRoleType(RoleType.BUYER);
        r1.setCreateDate(new Date());
        r1.setCreateBy(CREATOR);
        r1.setUserTypeOid(BigDecimal.valueOf(3));
        r1.setActor(this.getCommonParameter(false).getLoginId());
        r1.setActionType(DbActionType.CREATE);
        r1.setActionDate(new Date());
        r1.setCtrlStatus(MkCtrlStatus.APPROVED);
    }
    
    private void initSupplierRole() throws Exception
    {
        sr1 = new SupplierRoleHolder();
        sr1.setSupplierOid(s1.getSupplierOid());
        sr1.setRoleOid(r1.getRoleOid());
    }
    
    private void initUser() throws Exception
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
        u1.setSupplierOid(s1.getSupplierOid());
        u1.setUserType(BigDecimal.valueOf(3));
        u1.setActor(this.getCommonParameter(false).getLoginId());
        u1.setActionType(DbActionType.CREATE);
        u1.setActionDate(new Date());
        u1.setCtrlStatus(MkCtrlStatus.APPROVED);
        
        u2 = new UserProfileTmpHolder();
        u2.setUserOid(oidService.getOid());
        u2.setLoginId("GE");
        u2.setUserName("Ge");
        u2.setLoginMode("PASSWORD");
        u2.setEmail("jkge@pracbiz.com");
        u2.setGender("1");
        u2.setActive(true);
        u2.setBlocked(false);
        u2.setInit(false);
        u2.setCreateDate(new Date());
        u2.setCreateBy(this.getCommonParameter(false).getLoginId());
        u2.setBuyerOid(b1.getBuyerOid());
        u2.setUserType(BigDecimal.valueOf(2));
        u2.setActor(this.getCommonParameter(false).getLoginId());
        u2.setActionType(DbActionType.CREATE);
        u2.setActionDate(new Date());
        u2.setCtrlStatus(MkCtrlStatus.APPROVED);
    }
    
    private void initGroupTradingPartner() throws Exception
    {
        gtp = new GroupTradingPartnerTmpHolder();
        gtp.setGroupOid(g1.getGroupOid());
        gtp.setTradingPartnerOid(tp1.getTradingPartnerOid());
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
