package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.BeanUtils;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.OperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.mapper.RoleUserMapper;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.OperationService;
import com.pracbiz.b2bportal.core.service.RoleOperationService;
import com.pracbiz.b2bportal.core.service.RoleOperationTmpService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.SupplierRoleService;
import com.pracbiz.b2bportal.core.service.SupplierRoleTmpService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class RoleServiceTest extends BaseServiceTestCase
{
    private RoleService roleService;
    private RoleTmpService roleTmpService;
    private BuyerService buyerService;
    private SupplierService supplierService;
    private UserProfileService userProfileService;
    private OperationService operationService;
    private RoleOperationService roleOperationService;
    private RoleOperationTmpService roleOperationTmpService;
    private RoleUserMapper roleUserMapper;
    private SupplierRoleService supplierRoleService;
    private SupplierRoleTmpService supplierRoleTmpService;
    
    private RoleTmpHolder r1, r2, r3;
    private SupplierRoleTmpHolder srt;
    private BuyerHolder buyer;
    private SupplierHolder supplier;
    private UserProfileHolder user;
    private RoleUserHolder roleUser;
    
    private static final String ROLE1_ID = "testId1";
    private static final String ROLE2_ID = "testId2";
    private static final String ROLE3_ID = "testId3";
    
    private static final String ROLE1_NAME = "testName1";
    private static final String ROLE2_NAME = "testName2";
    private static final String ROLE3_NAME = "testName3";
    
    private static final String CREATOR = "JunitTester";
    
    public RoleServiceTest()
    {
        roleService = ctx.getBean("roleService", RoleService.class);
        roleTmpService = ctx.getBean("roleTmpService", RoleTmpService.class);
        buyerService = ctx.getBean("buyerService", BuyerService.class);
        supplierService = ctx.getBean("supplierService", SupplierService.class);
        userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
        operationService = ctx.getBean("operationService", OperationService.class);
        roleUserMapper = ctx.getBean("roleUserMapper", RoleUserMapper.class);
        roleOperationService = ctx.getBean("roleOperationService", RoleOperationService.class);
        roleOperationTmpService = ctx.getBean("roleOperationTmpService", RoleOperationTmpService.class);
        supplierRoleService = ctx.getBean("supplierRoleService", SupplierRoleService.class);
        supplierRoleTmpService = ctx.getBean("supplierRoleTmpService", SupplierRoleTmpService.class);
    }
    
    
    
    /*private void prepareTestingData() throws Exception
    {
        this.prepare();
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        userProfileService.insert(user);
        
        roleService.insert(r1);
        roleService.insert(r2);
        roleService.insert(r3);
        
        
    }*/
    
    
    public void testBasicCrd() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        
        int originalNumOfRecords = 0;
        List<? extends Object> records = roleService.select(new RoleHolder());
        
        if (null != records && !records.isEmpty())
        {
            originalNumOfRecords = records.size();
        }
        
        roleService.insert(r1);
        roleService.insert(r2);
        roleService.insert(r3);
        
        records = roleService.select(new RoleHolder());
        
        assertTrue(records.size() == originalNumOfRecords + 3);
        
        RoleHolder rlt = roleService.selectAdminRoleById(ROLE1_ID);
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), ROLE1_NAME);
        
        
        rlt = roleService.selectBuyerRoleById(buyer.getBuyerOid(), ROLE2_ID);
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), ROLE2_NAME);
        
        rlt = roleService.selectByKey(r1.getRoleOid());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), ROLE1_NAME);
        
        roleService.delete(r1);
        roleService.delete(r2);
        roleService.delete(r3);
        buyerService.delete(buyer);
        supplierService.delete(supplier);
        
        records = roleService.select(new RoleHolder());
        assertTrue(records.size() == originalNumOfRecords);
    }
    
    
    public void testSelectBuyerRolesByBuyerOid() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        roleService.insert(r1);
        roleService.insert(r2);
        roleService.insert(r3);
        supplierRoleService.insert(srt);
        
        List<? extends Object> rlt = roleService.selectBuyerRolesByBuyerOidAndUserType(buyer.getBuyerOid(), r2.getUserTypeOid());
        
        assertNotNull(rlt);
        assertFalse(rlt.isEmpty());
        
        Object obj = rlt.get(0);
        
        assertTrue(obj instanceof RoleHolder);
        
        RoleHolder role = (RoleHolder) obj;
        
        assertEquals(role.getRoleId(), ROLE2_ID);
        assertEquals(role.getRoleName(), ROLE2_NAME);
        
        supplierRoleService.delete(srt);
        roleService.delete(r1);
        roleService.delete(r2);
        roleService.delete(r3);
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testSelectSupplierRolesBySupplierOid() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        roleService.insert(r1);
        roleService.insert(r2);
        roleService.insert(r3);
        supplierRoleService.insert(srt);
        
        List<? extends Object> rlt = roleService.selectSupplierRolesBySupplierOidAndUserType(supplier.getSupplierOid(), r3.getUserTypeOid());
        
        assertNotNull(rlt);
        assertFalse(rlt.isEmpty());
        
        Object obj = rlt.get(0);
        
        assertTrue(obj instanceof RoleHolder);
        
        RoleHolder role = (RoleHolder) obj;
        
        assertEquals(role.getRoleId(), ROLE3_ID);
        assertEquals(role.getRoleName(), ROLE3_NAME);
        supplierRoleService.delete(srt);
        roleService.delete(r1);
        roleService.delete(r2);
        roleService.delete(r3);
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testSelectRolesByUserOid() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        roleService.insert(r1);
        roleService.insert(r2);
        roleService.insert(r3);
        userProfileService.insert(user);
        roleUserMapper.insert(roleUser);
        
        List<? extends Object> rlt = roleService.selectRolesByUserOid(user.getUserOid());
        assertNotNull(rlt);
        assertFalse(rlt.isEmpty());
        Object obj = rlt.get(0);
        assertTrue(obj instanceof RoleHolder);
        
        RoleHolder role = (RoleHolder) obj;
        
        assertEquals(role.getRoleId(), ROLE1_ID);
        assertEquals(role.getRoleName(), ROLE1_NAME);
        
        roleUserMapper.delete(roleUser);
        userProfileService.delete(user);
        roleService.delete(r1);
        roleService.delete(r2);
        roleService.delete(r3);
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        roleService.insert(r1);
        roleService.insert(r2);
        
        RoleHolder r = new RoleHolder();
        r.setRoleName("new role name");
        r.setRoleOid(r1.getRoleOid());
        roleService.updateByPrimaryKeySelective(r1, r);
        
        RoleHolder newRole = roleService.selectAdminRoleById(r1.getRoleId());
        assertNotNull(newRole);
        assertEquals(r1.getRoleType(), newRole.getRoleType());
        assertEquals(r.getRoleName(), newRole.getRoleName());
        
        r = new RoleHolder();
        BeanUtils.copyProperties(r2, r);
        r.setRoleId("newId");
        r.setRoleName("new role name");
        roleService.updateByPrimaryKey(r2, r);
        
        newRole = roleService.selectBuyerRoleById(buyer.getBuyerOid(), r.getRoleId());
        assertNotNull(newRole);
        assertEquals(r.getRoleType(), newRole.getRoleType());
        assertEquals(r.getRoleName(), newRole.getRoleName());
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        oid = r2.getRoleOid();
        r2 = new RoleTmpHolder();
        r2.setRoleOid(oid);
        
        roleService.delete(r1);
        roleService.delete(r2);
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testMkCreate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        CommonParameterHolder cp = this.getCommonParameter(true);
        
        roleService.mkCreate(cp, r1);
        roleService.mkCreate(cp, r2);
        roleService.mkCreate(cp, r3);
        
        RoleTmpHolder rlt = roleTmpService.selectAdminRoleById(r1.getRoleId());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), r1.getRoleName());
        assertEquals(rlt.getActionType(), DbActionType.CREATE);
        assertEquals(rlt.getActor(), cp.getLoginId());
        assertEquals(rlt.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        rlt = roleTmpService.selectBuyerRoleById(buyer.getBuyerOid(), r2.getRoleId());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), r2.getRoleName());
        assertEquals(rlt.getActionType(), DbActionType.CREATE);
        assertEquals(rlt.getActor(), cp.getLoginId());
        assertEquals(rlt.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        rlt = roleTmpService.selectSupplierRoleById(supplier.getSupplierOid(), r3.getRoleId());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), r3.getRoleName());
        assertEquals(rlt.getActionType(), DbActionType.CREATE);
        assertEquals(rlt.getActor(), cp.getLoginId());
        assertEquals(rlt.getCtrlStatus(), MkCtrlStatus.PENDING);
        supplierRoleTmpService.delete(srt);
        supplierRoleService.delete(srt);
        roleTmpService.delete(r1);
        roleTmpService.delete(r2);
        roleTmpService.delete(r3);
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testMkUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        CommonParameterHolder cp = this.getCommonParameter(true);
        
        roleService.mkCreate(cp, r2);
        
        RoleTmpHolder rlt = roleTmpService.selectBuyerRoleById(buyer.getBuyerOid(), r2.getRoleId());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), r2.getRoleName());
        assertEquals(rlt.getActionType(), DbActionType.CREATE);
        assertEquals(rlt.getActor(), cp.getLoginId());
        assertEquals(rlt.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        RoleTmpHolder newRole = new RoleTmpHolder();
        BeanUtils.copyProperties(r2, newRole);
        newRole.setRoleName("new role name");
        
        roleService.mkUpdate(this.getCommonParameter(true), r2, newRole);
        rlt = roleTmpService.selectBuyerRoleById(buyer.getBuyerOid(), r2.getRoleId());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), newRole.getRoleName());
        assertEquals(rlt.getActionType(), DbActionType.CREATE);
        assertEquals(rlt.getActor(), cp.getLoginId());
        assertEquals(rlt.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        newRole.setCtrlStatus(MkCtrlStatus.APPROVED);
        roleService.mkUpdate(this.getCommonParameter(true), r2, newRole);
        
        rlt = roleTmpService.selectBuyerRoleById(buyer.getBuyerOid(), newRole.getRoleId());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), newRole.getRoleName());
        assertEquals(rlt.getActionType(), DbActionType.UPDATE);
        assertEquals(rlt.getActor(), cp.getLoginId());
        assertEquals(rlt.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        BigDecimal oid = r2.getRoleOid();
        r2 = new RoleTmpHolder();
        r2.setRoleOid(oid);
        roleTmpService.delete(r2);
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testMkDelete() throws Exception
    {
        CommonParameterHolder cp = this.getCommonParameter(true);
        
        roleService.mkCreate(cp, r1);
        roleService.mkDelete(cp, r1);
        
        RoleTmpHolder rlt = roleTmpService.selectAdminRoleById(r1.getRoleId());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleName(), r1.getRoleName());
        assertEquals(rlt.getActionType(), DbActionType.DELETE);
        assertEquals(rlt.getActor(), cp.getLoginId());
        assertEquals(rlt.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        roleTmpService.delete(r1);
    }
    
    
    public void testMkApproveCreate() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        
        roleService.mkApprove(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        assertNotNull(tmpRlt);
        assertEquals(tmpRlt.getActionType(), DbActionType.CREATE);
        assertEquals(tmpRlt.getCtrlStatus(), MkCtrlStatus.APPROVED);
        
        RoleHolder rlt = roleService.selectByKey(r1.getRoleOid());
        assertNotNull(rlt);
        assertEquals(rlt.getRoleId(), tmpRlt.getRoleId());
        assertEquals(rlt.getRoleName(), tmpRlt.getRoleName());
        assertEquals(rlt.getCreateDate(), tmpRlt.getCreateDate());
        
        List<? extends Object> opns = operationService.selectOperationByRole(rlt.getRoleOid());
        List<? extends Object> tmpOpns = operationService.selectOperationByRoleTmp(tmpRlt.getRoleOid());
        
        assertEquals(opns.size(), tmpOpns.size());
        
        
        RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
        rot.setRoleOid(tmpRlt.getRoleOid());
        roleOperationTmpService.delete(rot);
        roleOperationService.delete(rot);
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        roleTmpService.delete(r1);
        roleService.delete(r1);
    }
    
    
    public void testMkApproveUpdate() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        roleService.mkApprove(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpOldRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        RoleTmpHolder tmpNewRlt = new RoleTmpHolder();
        BeanUtils.copyProperties(tmpOldRlt, tmpNewRlt);
        tmpOldRlt.setRoleOperations(new ArrayList<RoleOperationHolder>());
        tmpNewRlt.setRoleOperations(new ArrayList<RoleOperationHolder>());
        
        List<? extends Object> tmpOpns = operationService.selectOperationByRoleTmp(tmpOldRlt.getRoleOid());
        
        for (Object opn : tmpOpns)
        {
            ro = new RoleOperationTmpHolder();
            ro.setOpnId(((OperationHolder)opn).getOpnId());
            ro.setRoleOid(tmpOldRlt.getRoleOid());
            tmpOldRlt.getRoleOperations().add(ro);
            tmpNewRlt.getRoleOperations().add(ro);
        }
        
        tmpNewRlt.setRoleName("new Name");
        tmpNewRlt.getRoleOperations().remove(0);
        
        roleService.mkUpdate(this.getCommonParameter(true), tmpOldRlt, tmpNewRlt);
        
        RoleHolder oldRlt = roleService.selectByKey(r1.getRoleOid());
        oldRlt.setRoleOperations(new ArrayList<RoleOperationHolder>());
        List<? extends Object> oldOpns = operationService.selectOperationByRole(oldRlt.getRoleOid());
        
        for (Object opn : oldOpns)
        {
            ro = new RoleOperationTmpHolder();
            ro.setOpnId(((OperationHolder)opn).getOpnId());
            ro.setRoleOid(tmpOldRlt.getRoleOid());
            oldRlt.getRoleOperations().add(ro);
        }
        
        roleService.mkApprove(this.getCommonParameter(true), oldRlt, tmpNewRlt);
        
        RoleHolder newRlt = roleService.selectByKey(r1.getRoleOid());
        newRlt.setRoleOperations(new ArrayList<RoleOperationHolder>());
        List<? extends Object> newOpns = operationService.selectOperationByRole(oldRlt.getRoleOid());
        
        for (Object opn : newOpns)
        {
            ro = new RoleOperationTmpHolder();
            ro.setOpnId(((OperationHolder)opn).getOpnId());
            ro.setRoleOid(tmpOldRlt.getRoleOid());
            newRlt.getRoleOperations().add(ro);
        }
        
        assertEquals(newRlt.getRoleName(), tmpNewRlt.getRoleName());
        assertEquals(newRlt.getRoleOperations().size(), tmpNewRlt.getRoleOperations().size());
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
        rot.setRoleOid(oid);
        roleOperationTmpService.delete(rot);
        roleOperationService.delete(rot);
        roleService.delete(r1);
        roleTmpService.delete(r1);
    }
    
    
    public void testMkApproveDelete() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        roleService.mkApprove(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        tmpRlt.setRoleOperations(r1.getRoleOperations());
        RoleHolder rlt = roleService.selectByKey(r1.getRoleOid());
        rlt.setRoleOperations(r1.getRoleOperations());
        
        roleService.mkDelete(this.getCommonParameter(true), tmpRlt);
        roleService.mkApprove(this.getCommonParameter(true), rlt, tmpRlt);
        
        tmpRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        rlt = roleService.selectByKey(r1.getRoleOid());
        
        assertNull(tmpRlt);
        assertNull(rlt);
    }
    
    
    public void testMkRejectCreate() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        roleService.mkReject(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        assertNull(tmpRlt);
    }
    
    
    public void testMkRejectUpdate() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        roleService.mkApprove(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpOldRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        RoleTmpHolder tmpNewRlt = new RoleTmpHolder();
        BeanUtils.copyProperties(tmpOldRlt, tmpNewRlt);
        
        tmpOldRlt.setRoleOperations(r1.getRoleOperations());
        tmpNewRlt.setRoleOperations(new ArrayList<RoleOperationHolder>());
        tmpNewRlt.getRoleOperations().add(tmpOldRlt.getRoleOperations().get(0));
        tmpNewRlt.setRoleName("new role name");
        
        roleService.mkUpdate(this.getCommonParameter(true), tmpOldRlt, tmpNewRlt);
        roleService.mkReject(this.getCommonParameter(true), r1, tmpNewRlt);
        
        tmpNewRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        RoleHolder newRlt = roleService.selectByKey(r1.getRoleOid());
        assertTrue(!tmpNewRlt.getRoleName().equalsIgnoreCase("new role name"));
        assertEquals(tmpNewRlt.getRoleName(), newRlt.getRoleName());
        assertEquals(tmpNewRlt.getCtrlStatus(), MkCtrlStatus.REJECTED);
        
        assertEquals(
            operationService.selectOperationByRoleTmp(tmpNewRlt.getRoleOid())
                .size(), operationService.selectOperationByRole(newRlt
                .getRoleOid()).size());
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
        rot.setRoleOid(oid);
        roleOperationTmpService.delete(rot);
        roleOperationService.delete(rot);
        roleService.delete(r1);
        roleTmpService.delete(r1);
    }
    
    
    public void testMkRejectDelete() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        roleService.mkApprove(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpRole = roleTmpService.selectRoleByKey(r1.getRoleOid());
        tmpRole.setRoleOperations(roleOperationTmpService.selectByRole(tmpRole.getRoleOid()));
        
        roleService.mkDelete(this.getCommonParameter(true), tmpRole);
        
        tmpRole = roleTmpService.selectRoleByKey(r1.getRoleOid());
        tmpRole.setRoleOperations(roleOperationTmpService.selectByRole(tmpRole.getRoleOid()));
        RoleHolder role = roleService.selectByKey(r1.getRoleOid());
        role.setRoleOperations(roleOperationService.selectByRole(role.getRoleOid()));
        
        roleService.mkReject(this.getCommonParameter(true), role, tmpRole);
        
        tmpRole = roleTmpService.selectRoleByKey(r1.getRoleOid());
        assertNotNull(tmpRole);
        assertEquals(tmpRole.getActionType(), DbActionType.DELETE);
        assertEquals(tmpRole.getCtrlStatus(), MkCtrlStatus.REJECTED);
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
        rot.setRoleOid(oid);
        roleOperationTmpService.delete(rot);
        roleOperationService.delete(rot);
        roleService.delete(r1);
        roleTmpService.delete(r1);
    }
    
    
    public void testMkWithdrawCreate() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        roleService.mkWithdraw(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        assertNull(tmpRlt);
    }
    
    
    public void testMkWithdrawUpdate() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        roleService.mkApprove(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpOldRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        RoleTmpHolder tmpNewRlt = new RoleTmpHolder();
        BeanUtils.copyProperties(tmpOldRlt, tmpNewRlt);
        
        tmpOldRlt.setRoleOperations(r1.getRoleOperations());
        tmpNewRlt.setRoleOperations(new ArrayList<RoleOperationHolder>());
        tmpNewRlt.getRoleOperations().add(tmpOldRlt.getRoleOperations().get(0));
        tmpNewRlt.setRoleName("new role name");
        
        roleService.mkUpdate(this.getCommonParameter(true), tmpOldRlt, tmpNewRlt);
        roleService.mkWithdraw(this.getCommonParameter(true), r1, tmpNewRlt);
        
        tmpNewRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        RoleHolder newRlt = roleService.selectByKey(r1.getRoleOid());
        assertTrue(!tmpNewRlt.getRoleName().equalsIgnoreCase("new role name"));
        assertEquals(tmpNewRlt.getRoleName(), newRlt.getRoleName());
        assertEquals(tmpNewRlt.getCtrlStatus(), MkCtrlStatus.WITHDRAWN);
        
        assertEquals(
            operationService.selectOperationByRoleTmp(tmpNewRlt.getRoleOid())
                .size(), operationService.selectOperationByRole(newRlt
                .getRoleOid()).size());
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
        rot.setRoleOid(oid);
        roleOperationTmpService.delete(rot);
        roleOperationService.delete(rot);
        roleService.delete(r1);
        roleTmpService.delete(r1);
    }
    
    
    public void testMkWithdrawDelete() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.mkCreate(this.getCommonParameter(true), r1);
        roleService.mkApprove(this.getCommonParameter(true), null, r1);
        
        RoleTmpHolder tmpRole = roleTmpService.selectRoleByKey(r1.getRoleOid());
        tmpRole.setRoleOperations(roleOperationTmpService.selectByRole(tmpRole.getRoleOid()));
        
        roleService.mkDelete(this.getCommonParameter(true), tmpRole);
        
        tmpRole = roleTmpService.selectRoleByKey(r1.getRoleOid());
        tmpRole.setRoleOperations(roleOperationTmpService.selectByRole(tmpRole.getRoleOid()));
        RoleHolder role = roleService.selectByKey(r1.getRoleOid());
        role.setRoleOperations(roleOperationService.selectByRole(role.getRoleOid()));
        
        roleService.mkWithdraw(this.getCommonParameter(true), role, tmpRole);
        
        tmpRole = roleTmpService.selectRoleByKey(r1.getRoleOid());
        assertNotNull(tmpRole);
        assertEquals(tmpRole.getActionType(), DbActionType.DELETE);
        assertEquals(tmpRole.getCtrlStatus(), MkCtrlStatus.WITHDRAWN);
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
        rot.setRoleOid(oid);
        roleOperationTmpService.delete(rot);
        roleOperationService.delete(rot);
        roleService.delete(r1);
        roleTmpService.delete(r1);
    }
    
    
    public void testCreateRoleProfile() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.createRoleProfile(this.getCommonParameter(false), r1);
        
        RoleTmpHolder tmpRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        RoleHolder rlt = roleService.selectByKey(r1.getRoleOid());
        
        assertNotNull(rlt);
        assertNotNull(tmpRlt);
        assertEquals(rlt.getRoleId(), tmpRlt.getRoleId());
        assertEquals(rlt.getCreateDate(), tmpRlt.getCreateDate());
        assertEquals(tmpRlt.getActionType(), DbActionType.CREATE);
        assertEquals(tmpRlt.getCtrlStatus(), MkCtrlStatus.APPROVED);
        assertEquals(operationService.selectOperationByRole(rlt.getRoleOid()).size(),
            operationService.selectOperationByRoleTmp(tmpRlt.getRoleOid()).size());
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
        rot.setRoleOid(oid);
        roleOperationTmpService.delete(rot);
        roleOperationService.delete(rot);
        roleService.delete(r1);
        roleTmpService.delete(r1);
    }
    
    
    public void testUpdateRoleProfile() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.createRoleProfile(this.getCommonParameter(false), r1);
        
        RoleTmpHolder tmpOldRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        tmpOldRlt.setRoleOperations(roleOperationTmpService.selectByRole(tmpOldRlt.getRoleOid()));
        
        RoleTmpHolder tmpNewRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        tmpNewRlt.setRoleOperations(roleOperationTmpService.selectByRole(tmpNewRlt.getRoleOid()));
        
        tmpNewRlt.setRoleName("new role name");
        tmpNewRlt.getRoleOperations().remove(0);
        
        roleService.updateRoleProfile(this.getCommonParameter(false), tmpOldRlt, tmpNewRlt);
        
        tmpNewRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        tmpNewRlt.setRoleOperations(roleOperationTmpService.selectByRole(tmpNewRlt.getRoleOid()));
        RoleHolder newRlt = roleService.selectByKey(tmpNewRlt.getRoleOid());
        newRlt.setRoleOperations(roleOperationService.selectByRole(newRlt.getRoleOid()));
        
        assertEquals(newRlt.getRoleName(), "new role name");
        assertEquals(tmpNewRlt.getRoleName(), "new role name");
        assertEquals(newRlt.getRoleOperations().size(), r1.getRoleOperations().size() -1);
        
        BigDecimal oid = r1.getRoleOid();
        r1 = new RoleTmpHolder();
        r1.setRoleOid(oid);
        RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
        rot.setRoleOid(oid);
        roleOperationTmpService.delete(rot);
        roleOperationService.delete(rot);
        roleService.delete(r1);
        roleTmpService.delete(r1);
    }
    
    
    public void testRemoveRoleProfile() throws Exception
    {
        List<RoleOperationHolder> roleOperations = new ArrayList<RoleOperationHolder>();
        RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100001");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100002");
        roleOperations.add(ro);
        ro = new RoleOperationTmpHolder();
        ro.setRoleOid(r1.getRoleOid());
        ro.setOpnId("100003");
        roleOperations.add(ro);
        r1.setRoleOperations(roleOperations);
        
        roleService.createRoleProfile(this.getCommonParameter(false), r1);
        
        roleService.removeRoleProfile(this.getCommonParameter(false), r1);
        
        RoleTmpHolder tmpNewRlt = roleTmpService.selectRoleByKey(r1.getRoleOid());
        RoleHolder newRlt = roleService.selectByKey(r1.getRoleOid());
        
        assertNull(tmpNewRlt);
        assertNull(newRlt);
    }
    
    // ****************************************************
    // private methods
    // ****************************************************
    
    @Before
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
        buyer.setCreateBy(CREATOR);
        buyer.setCreateDate(new Date());
        buyer.setMboxId("testBuyerMbox");
        buyer.setChannel("testBuyerChannel");
        buyer.setDeploymentMode(DeploymentMode.LOCAL);
        
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
        supplier.setCreateBy(CREATOR);
        supplier.setCreateDate(new Date());
        supplier.setMboxId("testSupplierMbox");
        supplier.setDeploymentMode(DeploymentMode.LOCAL);
        supplier.setClientEnabled(false);
        supplier.setRequireReport(false);
        supplier.setRequireTranslationIn(false);
        supplier.setRequireTranslationOut(false);
        
        user = new UserProfileTmpHolder();
        user.setUserOid(oidService.getOid());
        user.setLoginId("testloginid");
        user.setUserName("OYL");
        user.setLoginMode("PASSWORD");
        user.setEmail("louyang2@pracbiz.com");
        user.setGender("M");
        user.setActive(true);
        user.setBlocked(false);
        user.setInit(false);
        user.setCreateDate(new Date());
        user.setCreateBy(CREATOR);
        user.setUserType(BigDecimal.ONE);
        
        r1 = new RoleTmpHolder();
        BigDecimal oid = oidService.getOid();
        r1.setRoleOid(oid);
        r1.setRoleId(ROLE1_ID);
        r1.setRoleName(ROLE1_NAME);
        r1.setRoleType(RoleType.ADMIN);
        r1.setCreateDate(new Date());
        r1.setCreateBy(CREATOR);
        r1.setUserTypeOid(BigDecimal.ONE);
        
        r2 = new RoleTmpHolder();
        oid = oidService.getOid();
        r2.setRoleOid(oid);
        r2.setRoleId(ROLE2_ID);
        r2.setRoleName(ROLE2_NAME);
        r2.setRoleType(RoleType.BUYER);
        r2.setBuyerOid(buyer.getBuyerOid());
        r2.setCreateDate(new Date());
        r2.setCreateBy(CREATOR);
        r2.setUserTypeOid(BigDecimal.valueOf(2));
        
        
        r3 = new RoleTmpHolder();
        oid = oidService.getOid();
        r3.setRoleOid(oid);
        r3.setRoleId(ROLE3_ID);
        r3.setRoleName(ROLE3_NAME);
        r3.setRoleType(RoleType.SUPPLIER);
        r3.setCreateDate(new Date());
        r3.setCreateBy(CREATOR);
        r3.setUserTypeOid(BigDecimal.valueOf(3));
        
        srt = new SupplierRoleTmpHolder();
        srt.setRoleOid(r3.getRoleOid());
        srt.setSupplierOid(supplier.getSupplierOid());
        
        List<SupplierRoleHolder> list = new ArrayList<SupplierRoleHolder>();
        list.add(srt);
        r3.setSupplierRoles(list);
        
        roleUser = new RoleUserHolder();
        roleUser.setUserOid(user.getUserOid());
        roleUser.setRoleOid(r1.getRoleOid());
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
