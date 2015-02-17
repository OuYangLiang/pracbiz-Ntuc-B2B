//*****************************************************************************
//
// File Name       :  GroupProfileServiceTest.java
// Date Created    :  Sep 17, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 17, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

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
import com.pracbiz.b2bportal.core.constants.GroupType;
import com.pracbiz.b2bportal.core.constants.LastUpdateFrom;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.mapper.GroupSupplierMapper;
import com.pracbiz.b2bportal.core.mapper.GroupSupplierTmpMapper;
import com.pracbiz.b2bportal.core.mapper.GroupTradingPartnerMapper;
import com.pracbiz.b2bportal.core.mapper.GroupTradingPartnerTmpMapper;
import com.pracbiz.b2bportal.core.mapper.GroupUserMapper;
import com.pracbiz.b2bportal.core.mapper.GroupUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.RoleGroupMapper;
import com.pracbiz.b2bportal.core.mapper.RoleGroupTmpMapper;
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
import com.pracbiz.b2bportal.core.service.RoleGroupService;
import com.pracbiz.b2bportal.core.service.RoleGroupTmpService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class GroupProfileServiceTest extends BaseServiceTestCase
{
    
    private OidService  oidService;
    private BuyerService buyerService;
    private SupplierService supplierService;
    private UserProfileService userProfileService;
    private UserProfileTmpService userProfileTmpService;
    private GroupService groupService;
    private GroupTmpService groupTmpService;
    private GroupUserService groupUserService;
    private GroupUserTmpService groupUserTmpService;
    private GroupSupplierService groupSupplierService;
    private GroupSupplierTmpService groupSupplierTmpService;
    private RoleGroupService roleGroupService;
    private RoleGroupTmpService roleGroupTmpService;
    private GroupTradingPartnerService groupTradingPartnerService;
    private GroupTradingPartnerTmpService groupTradingPartnerTmpService;
    private RoleTmpService roleTmpService;
    private RoleService roleService;
    private TradingPartnerService tradingPartnerService;
    
    private GroupUserMapper groupUserMapper;
    private GroupUserTmpMapper groupUserTmpMapper;
    private RoleGroupMapper roleGroupMapper;
    private RoleGroupTmpMapper roleGroupTmpMapper;
    private GroupSupplierMapper groupSupplierMapper;
    private GroupSupplierTmpMapper groupSupplierTmpMapper;
    private GroupTradingPartnerMapper groupTradingPartnerMapper;
    private GroupTradingPartnerTmpMapper groupTradingPartnerTmpMapper;
    
    private List<RoleTmpHolder> roles;
    private List<RoleTmpHolder> buyerAdminRoles;
    private List<RoleTmpHolder> supplierAdminRoles;
    private List<RoleTmpHolder> buyerUserRoles;
    private List<RoleTmpHolder> supplierUserRoles;
    
    private List<UserProfileTmpHolder> users;
    private List<UserProfileTmpHolder> buyerAdmins;
    private List<UserProfileTmpHolder> buyerUsers;
    private List<UserProfileTmpHolder> supplierAdmins;
    private List<UserProfileTmpHolder> supplierUsers;
    
    private List<TradingPartnerHolder> tradingPartners;
    
    private List<GroupUserHolder> groupUsers;
    private List<RoleGroupHolder> roleGroups;
    private List<GroupTradingPartnerHolder> groupTps;
    private List<GroupSupplierHolder> groupSuppliers;
    
    private GroupTmpHolder g1, g2, g3, g4;
    private BuyerHolder buyer;
    private SupplierHolder supplier;
    private UserProfileTmpHolder admin;
    
    
    private static final String GROUP1_ID = "testId1";
    private static final String GROUP2_ID = "testId2";
    private static final String GROUP3_ID = "testId3";
    private static final String GROUP4_ID = "testId4";
    
    private static final String GROUP1_NAME = "testName1";
    private static final String GROUP2_NAME = "testName2";
    private static final String GROUP3_NAME = "testName3";
    private static final String GROUP4_NAME = "testName4";
    
    private static final String NEW_GROUP_ID = "testNewGroupId";
    private static final String NEW_GROUP_NAME = "new group name"; 
    
    private static final String CREATOR = "JunitTester";
    
    
    public void testBasicCrd() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        int originalNumOfRecords = 0;
        List<GroupHolder> records = groupService.select(new GroupTmpHolder());
        
        if (null != records && !records.isEmpty())
        {
            originalNumOfRecords = records.size();
        }
        
        int originalNumOfRecordsTmp = 0;
        List<GroupTmpHolder> recordsTmp = groupTmpService.select(new GroupTmpHolder());
        if (null != recordsTmp && !recordsTmp.isEmpty())
        {
            originalNumOfRecordsTmp = recordsTmp.size();
        }
        
        groupTmpService.insert(g1);
        groupTmpService.insert(g2);
        groupTmpService.insert(g3);
        groupTmpService.insert(g4);
        
        groupService.insert(g1);
        groupService.insert(g2);
        groupService.insert(g3);
        groupService.insert(g4);

        records = groupService.select(new GroupHolder());
        recordsTmp = groupTmpService.select(new GroupTmpHolder());
        
        assertTrue(records.size() == originalNumOfRecords + 4);
        assertTrue(recordsTmp.size() == originalNumOfRecordsTmp + 4);
        
        GroupHolder group = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(group);
        assertEquals(group.getGroupId(), g1.getGroupId());
       
        GroupTmpHolder groupTmp = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(groupTmp);
        assertEquals(groupTmp.getGroupId(), g1.getGroupId());
       
        groupService.delete(g1);
        groupService.delete(g2);
        groupService.delete(g3);
        groupService.delete(g4);
        
        groupTmpService.delete(g1);
        groupTmpService.delete(g2);
        groupTmpService.delete(g3);
        groupTmpService.delete(g4);

        records = this.groupService.select(new GroupTmpHolder());
        if (originalNumOfRecords == 0)
        {
            assertTrue(records == null || records.isEmpty());
        }
        else
        {
            assertTrue(records.size() == originalNumOfRecords);
        }
        
        recordsTmp = this.groupTmpService.select(new GroupTmpHolder());
        if (originalNumOfRecordsTmp == 0)
        {
            assertTrue(recordsTmp == null || recordsTmp.isEmpty());
        }
        else
        {
            assertTrue(recordsTmp.size() == originalNumOfRecordsTmp);
        }
    }
    
    
    public void testSelectGroupsByUserType() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        userProfileTmpService.insert(buyerAdmins.get(0));
        userProfileService.insert(buyerAdmins.get(0));
        
        
        int originalNumOfRecordsByUserType = 0;
        List<GroupHolder> recordsByUserType= groupService.selectGroupsByUserType(g1.getUserTypeOid());
        
        if (null != recordsByUserType && !recordsByUserType.isEmpty())
        {
            originalNumOfRecordsByUserType = recordsByUserType.size();
        }
        
        int originalNumOfRecordsTmpByUserType = 0;
        List<GroupTmpHolder> recordsTmpByUserType = groupTmpService.selectGroupsByUserType(g1.getUserTypeOid());
        if (null != recordsTmpByUserType && !recordsTmpByUserType.isEmpty())
        {
            originalNumOfRecordsTmpByUserType = recordsTmpByUserType.size();
        }
        
        groupTmpService.insert(g1);
        groupTmpService.insert(g2);
        groupTmpService.insert(g3);
        groupTmpService.insert(g4);
        
        groupService.insert(g1);
        groupService.insert(g2);
        groupService.insert(g3);
        groupService.insert(g4);

        List<GroupUserHolder> groupUsers = this.initGroupUsers(buyerAdmins, g1);
        this.groupUsers.addAll(groupUsers);
        GroupUserTmpHolder groupUser = (GroupUserTmpHolder)groupUsers.get(0);
        groupUserTmpMapper.insert(groupUser);
        groupUserMapper.insert(groupUser);
        
        List<GroupHolder> groups = groupService.selectGroupsByUserType(g1.getUserTypeOid());
        assertNotNull(groups);
        assertTrue(groups.size() == originalNumOfRecordsByUserType + 1);
        
        List<GroupTmpHolder> groupsTmp = groupTmpService.selectGroupsByUserType(g1.getUserTypeOid());
        assertNotNull(groupsTmp);
        assertTrue(groupsTmp.size() == originalNumOfRecordsTmpByUserType + 1);
        
    }
    
    
    public void testSelectGroupByUserOid() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        userProfileTmpService.insert(buyerAdmins.get(0));
        userProfileService.insert(buyerAdmins.get(0));
        
        groupTmpService.insert(g1);
        groupTmpService.insert(g2);
        groupTmpService.insert(g3);
        groupTmpService.insert(g4);
        
        groupService.insert(g1);
        groupService.insert(g2);
        groupService.insert(g3);
        groupService.insert(g4);

        List<GroupUserHolder> groupUsers = this.initGroupUsers(buyerAdmins, g1);
        this.groupUsers.addAll(groupUsers);
        GroupUserTmpHolder groupUser = (GroupUserTmpHolder)groupUsers.get(0);
        groupUserTmpMapper.insert(groupUser);
        groupUserMapper.insert(groupUser);
        
        GroupHolder group = groupService.selectGroupByUserOid(groupUser.getUserOid());
        assertNotNull(group);
        assertEquals(group.getGroupId(), g1.getGroupId());
        
        GroupTmpHolder groupTmp = groupTmpService.selectGroupTmpByUserOid(groupUser.getUserOid());
        assertNotNull(groupTmp);
        assertEquals(groupTmp.getGroupId(), g1.getGroupId());
    }
    
    
    public void testUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        groupService.insert(g1);
        groupService.insert(g2);
        
        groupTmpService.insert(g1);
        groupTmpService.insert(g2);
        
        GroupTmpHolder g = new GroupTmpHolder();
        g.setGroupName(NEW_GROUP_NAME);
        g.setGroupOid(g1.getGroupOid());
        groupService.updateByPrimaryKeySelective(g1, g);
        
        GroupHolder newGroup = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(newGroup);
        assertEquals(g1.getGroupId(), newGroup.getGroupId());
        assertEquals(g.getGroupName(), newGroup.getGroupName());
        
    
        groupTmpService.updateByPrimaryKeySelective(g1, g);
        GroupHolder newGroupTmp = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(newGroupTmp);
        assertEquals(g1.getGroupId(), newGroupTmp.getGroupId());
        assertEquals(g.getGroupName(), newGroupTmp.getGroupName());
        
        g = new GroupTmpHolder();
        BeanUtils.copyProperties(g2, g);
        g.setGroupId(NEW_GROUP_ID);
        g.setGroupName(NEW_GROUP_NAME);
        
        groupService.updateByPrimaryKey(g2, g);
        newGroup = groupService.selectGroupByKey(g2.getGroupOid());
        assertNotNull(newGroup);
        assertEquals(g.getGroupId(), newGroup.getGroupId());
        assertEquals(g.getGroupName(), newGroup.getGroupName());
        
        groupTmpService.updateByPrimaryKey(g2, g);
        newGroupTmp = groupTmpService.selectGroupTmpByKey(g2.getGroupOid());
        assertNotNull(newGroupTmp);
        assertEquals(g.getGroupId(), newGroupTmp.getGroupId());
        assertEquals(g.getGroupName(), newGroupTmp.getGroupName());
    }
    
    
    public void testMkCreate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        // buyer user group
        List<GroupSupplierHolder> groupSupplier3 = this.initGroupSuppliers(suppliers, g3);
        groupSuppliers.addAll(groupSupplier3);
        List<RoleGroupHolder> roleGroup3 = this.initRoleGroups(buyerUserRoles, g3);
        roleGroups.addAll(roleGroup3);
        List<GroupUserHolder> groupUser3 = this.initGroupUsers(buyerUsers, g3);
        groupUsers.addAll(groupUser3);
        
        g3.setGroupSuppliers(groupSupplier3);
        g3.setGroupUsers(groupUser3);
        g3.setRoleGroups(roleGroup3);
        
        
        // supplier admin group
        List<GroupTradingPartnerHolder> groupTp2 = this.initGroupTp(this.tradingPartners, g2);
        groupTps.addAll(groupTp2);
        List<RoleGroupHolder> roleGroup2 = this.initRoleGroups(supplierAdminRoles, g2);
        roleGroups.addAll(roleGroup2);
        List<GroupUserHolder> groupUser2 = this.initGroupUsers(supplierAdmins, g2);
        groupUsers.addAll(groupUser2);    
        
        g2.setGroupTradingPartners(groupTp2);
        g2.setGroupUsers(groupUser2);
        g2.setRoleGroups(roleGroup2);
        
        // supplier user group
        List<GroupTradingPartnerHolder> groupTp4 = this.initGroupTp(this.tradingPartners, g4);
        groupTps.addAll(groupTp4);
        List<RoleGroupHolder> roleGroup4 = this.initRoleGroups(supplierUserRoles, g4);
        roleGroups.addAll(roleGroup4);
        List<GroupUserHolder> groupUser4 = this.initGroupUsers(supplierUsers, g4);
        groupUsers.addAll(groupUser4);    
        
        g4.setGroupTradingPartners(groupTp4);
        g4.setGroupUsers(groupUser4);
        g4.setRoleGroups(roleGroup4);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        for (TradingPartnerHolder tp : tradingPartners)
        {
            tradingPartnerService.insert(tp);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkCreate(cp, g2);
        groupService.mkCreate(cp, g3);
        groupService.mkCreate(cp, g4);
        
        // buyer admin 
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.CREATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.PENDING, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNull(rlt);
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertTrue(groupUserList == null || groupUserList.isEmpty());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplierTmpList.size(), groupSupplier1.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierList == null || groupSupplierList.isEmpty());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroupTmpList.size(), roleGroup1.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupList == null || roleGroupList.isEmpty());
        
        //buyer user
        tmpRlt = groupTmpService.selectGroupTmpByKey(g3.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g3.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g3.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.CREATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.PENDING, tmpRlt.getCtrlStatus());
        
        rlt = groupService.selectGroupByKey(g3.getGroupOid());
        assertNull(rlt);
        
        groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g3.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUserTmpList.size(), groupUser3.size());
        
        groupUserList = groupUserService.selectGroupUserByGroupOid(g3.getGroupOid());
        assertTrue(groupUserList == null || groupUserList.isEmpty());
        
        groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g3.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier3.size(), groupSupplierTmpList.size());
        
        groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g3.getGroupOid());
        assertTrue(groupSupplierList == null || groupSupplierList.isEmpty());
        
        roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g3.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup3.size(),roleGroupTmpList.size());
        
        roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g3.getGroupOid());
        assertTrue(roleGroupList == null || roleGroupList.isEmpty());
        
        // supplier admin
        tmpRlt = groupTmpService.selectGroupTmpByKey(g2.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.SUPPLIER,tmpRlt.getGroupType());
        assertEquals(g2.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g2.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.CREATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.PENDING, tmpRlt.getCtrlStatus());
        
        rlt = groupService.selectGroupByKey(g2.getGroupOid());
        assertNull(rlt);
        
        groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g2.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser2.size(), groupUserTmpList.size());
        
        groupUserList = groupUserService.selectGroupUserByGroupOid(g2.getGroupOid());
        assertTrue(groupUserList == null || groupUserList.isEmpty());
        
        List<GroupTradingPartnerTmpHolder> groupTpTmpList = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(g2.getGroupOid());
        assertNotNull(groupTpTmpList);
        assertEquals(groupTp2.size(), groupTpTmpList.size());
        
        List<GroupTradingPartnerHolder> groupTpList = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(g2.getGroupOid());
        assertTrue(groupTpList == null || groupTpList.isEmpty());
        
        roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g2.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup2.size(), roleGroupTmpList.size());
        
        roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g2.getGroupOid());
        assertTrue(roleGroupList == null || roleGroupList.isEmpty());
        
        // supplier user
        tmpRlt = groupTmpService.selectGroupTmpByKey(g4.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.SUPPLIER,tmpRlt.getGroupType());
        assertEquals(g4.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g4.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.CREATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.PENDING, tmpRlt.getCtrlStatus());
        
        rlt = groupService.selectGroupByKey(g4.getGroupOid());
        assertNull(rlt);
        
        groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g4.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser4.size(), groupUserTmpList.size());
        
        groupUserList = groupUserService.selectGroupUserByGroupOid(g4.getGroupOid());
        assertTrue(groupUserList == null || groupUserList.isEmpty());
        
        groupTpTmpList = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(g4.getGroupOid());
        assertNotNull(groupTpTmpList);
        assertEquals( groupTp4.size(), groupTpTmpList.size());
        
        groupTpList = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(g4.getGroupOid());
        assertTrue(groupTpList == null || groupTpList.isEmpty());
        
        roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g4.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup4.size(), roleGroupTmpList.size());
        
        roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g4.getGroupOid());
        assertTrue(roleGroupList == null || roleGroupList.isEmpty());
        
    }
    
    
    public void testMkUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.CREATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.PENDING, tmpRlt.getCtrlStatus());
        
        GroupTmpHolder newGroup = new GroupTmpHolder();
        BeanUtils.copyProperties(g1, newGroup);
        newGroup.setGroupName(NEW_GROUP_NAME);
        newGroup.setGroupId(NEW_GROUP_ID);
        newGroup.setUpdateDate(new Date());
        newGroup.setUpdateBy(CREATOR);
        
        groupService.mkUpdate(cp, g1, newGroup);
        tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(newGroup.getGroupId(), tmpRlt.getGroupId());
        assertEquals(newGroup.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.CREATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.PENDING, tmpRlt.getCtrlStatus());
        assertEquals(CREATOR, tmpRlt.getUpdateBy());
        assertEquals(newGroup.getUpdateDate().toString(), tmpRlt.getUpdateDate().toString());
        
        newGroup.setCtrlStatus(MkCtrlStatus.APPROVED);
        newGroup.setUpdateDate(new Date());
        newGroup.setUpdateBy(CREATOR);
        groupService.mkUpdate(cp, g1, newGroup);
        
        tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(newGroup.getGroupId(), tmpRlt.getGroupId());
        assertEquals(newGroup.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.UPDATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.PENDING, tmpRlt.getCtrlStatus());
        assertEquals(CREATOR, tmpRlt.getUpdateBy());
        assertEquals(newGroup.getUpdateDate().toString(), tmpRlt.getUpdateDate().toString());
    }
    
    
    public void testMkDelete() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkDelete(cp, g1);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.DELETE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.PENDING, tmpRlt.getCtrlStatus());
    }
    
    
    public void testMkApproveCreate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkApprove(cp, null, g1);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.CREATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.APPROVED, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(rlt);
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserList);
        assertEquals(groupUser1.size(), groupUserList.size());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier1.size(), groupSupplierTmpList.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierList);
        assertEquals(groupSupplier1.size(), groupSupplierList.size());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup1.size(), roleGroupTmpList.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupList);
        assertEquals(roleGroup1.size(), roleGroupList.size());
        
    }
    
    
    public void testMkApproveUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkApprove(cp, null, g1);
        
        GroupTmpHolder newGroup = new GroupTmpHolder();
        BeanUtils.copyProperties(g1, newGroup);
        newGroup.setGroupName(NEW_GROUP_NAME);
        newGroup.setGroupId(NEW_GROUP_ID);
        newGroup.setUpdateDate(new Date());
        newGroup.setUpdateBy(CREATOR);
        
        groupService.mkUpdate(cp, g1, newGroup);
        groupService.mkApprove(cp, g1, newGroup);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(newGroup.getGroupId(), tmpRlt.getGroupId());
        assertEquals(newGroup.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.UPDATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.APPROVED, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(rlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(newGroup.getGroupId(), rlt.getGroupId());
        assertEquals(newGroup.getGroupName(), rlt.getGroupName());
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserList);
        assertEquals(groupUser1.size(), groupUserList.size());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier1.size(), groupSupplierTmpList.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierList);
        assertEquals(groupSupplier1.size(), groupSupplierList.size());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup1.size(), roleGroupTmpList.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupList);
        assertEquals(roleGroup1.size(), roleGroupList.size());
        
    }
    
    
    public void testMkApproveDelete() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkApprove(cp, null, g1);
        groupService.mkDelete(cp, g1);
        groupService.mkApprove(cp, g1, g1);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNull(tmpRlt);
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNull(rlt);
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertTrue(groupUserTmpList == null || groupUserTmpList.isEmpty());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertTrue(groupUserList == null || groupUserList.isEmpty());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierTmpList == null || groupSupplierTmpList.isEmpty());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierList == null || groupSupplierList.isEmpty());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupTmpList == null || roleGroupTmpList.isEmpty());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupList == null || roleGroupList.isEmpty());
        
    }
    
    
    public void testMkRejectCreate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkReject(cp, null, g1);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNull(tmpRlt);
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNull(rlt);
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertTrue(groupUserTmpList == null || groupUserTmpList.isEmpty());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertTrue(groupUserList == null || groupUserList.isEmpty());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierTmpList == null || groupSupplierTmpList.isEmpty());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierList == null || groupSupplierList.isEmpty());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupTmpList == null || roleGroupTmpList.isEmpty());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupList == null || roleGroupList.isEmpty());
        
    }
    
    
    public void testMkRejectUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkApprove(cp, null, g1);
        
        GroupTmpHolder newGroup = new GroupTmpHolder();
        BeanUtils.copyProperties(g1, newGroup);
        newGroup.setGroupName(NEW_GROUP_NAME);
        newGroup.setGroupId(NEW_GROUP_ID);
        newGroup.setUpdateDate(new Date());
        newGroup.setUpdateBy(CREATOR);
        
        groupService.mkUpdate(cp, g1, newGroup);
        
        GroupHolder oldGroup = groupService.selectGroupByKey(g1.getGroupOid());
        oldGroup.setGroupUsers(g1.getGroupUsers());
        oldGroup.setRoleGroups(g1.getRoleGroups());
        oldGroup.setGroupSuppliers(g1.getGroupSuppliers());
        groupService.mkReject(cp, oldGroup, newGroup);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.UPDATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.REJECTED, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(rlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), rlt.getGroupId());
        assertEquals(g1.getGroupName(), rlt.getGroupName());
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserList);
        assertEquals(groupUser1.size(), groupUserList.size());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier1.size(), groupSupplierTmpList.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierList);
        assertEquals(groupSupplier1.size(), groupSupplierList.size());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup1.size(), roleGroupTmpList.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupList);
        assertEquals(roleGroup1.size(), roleGroupList.size());
        
    }
    
    
    public void testRejectDelete() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkApprove(cp, null, g1);
        
        GroupTmpHolder group = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        group.setGroupUsers(g1.getGroupUsers());
        group.setRoleGroups(g1.getRoleGroups());
        group.setGroupSuppliers(g1.getGroupSuppliers());
        
        groupService.mkDelete(cp, group);
        
        GroupHolder oldGroup = groupService.selectGroupByKey(g1.getGroupOid());
        oldGroup.setGroupUsers(g1.getGroupUsers());
        oldGroup.setRoleGroups(g1.getRoleGroups());
        oldGroup.setGroupSuppliers(g1.getGroupSuppliers());
        groupService.mkReject(cp, oldGroup, group);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.DELETE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.REJECTED, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(rlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), rlt.getGroupId());
        assertEquals(g1.getGroupName(), rlt.getGroupName());
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserList);
        assertEquals(groupUser1.size(), groupUserList.size());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier1.size(), groupSupplierTmpList.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierList);
        assertEquals(groupSupplier1.size(), groupSupplierList.size());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup1.size(), roleGroupTmpList.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupList);
        assertEquals(roleGroup1.size(), roleGroupList.size());
    }
    
    
    public void testMkWithdrawCreate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkWithdraw(cp, null, g1);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNull(tmpRlt);
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNull(rlt);
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertTrue(groupUserTmpList == null || groupUserTmpList.isEmpty());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertTrue(groupUserList == null || groupUserList.isEmpty());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierTmpList == null || groupSupplierTmpList.isEmpty());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierList == null || groupSupplierList.isEmpty());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupTmpList == null || roleGroupTmpList.isEmpty());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupList == null || roleGroupList.isEmpty());
        
    }
    
    
    public void testMkWithdrawUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkApprove(cp, null, g1);
        
        GroupTmpHolder newGroup = new GroupTmpHolder();
        BeanUtils.copyProperties(g1, newGroup);
        newGroup.setGroupName(NEW_GROUP_NAME);
        newGroup.setGroupId(NEW_GROUP_ID);
        newGroup.setUpdateDate(new Date());
        newGroup.setUpdateBy(CREATOR);
        
        groupService.mkUpdate(cp, g1, newGroup);
        
        GroupHolder oldGroup = groupService.selectGroupByKey(g1.getGroupOid());
        oldGroup.setGroupUsers(g1.getGroupUsers());
        oldGroup.setRoleGroups(g1.getRoleGroups());
        oldGroup.setGroupSuppliers(g1.getGroupSuppliers());
        groupService.mkWithdraw(cp, oldGroup, newGroup);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.UPDATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.WITHDRAWN, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(rlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), rlt.getGroupId());
        assertEquals(g1.getGroupName(), rlt.getGroupName());
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserList);
        assertEquals(groupUser1.size(), groupUserList.size());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier1.size(), groupSupplierTmpList.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierList);
        assertEquals(groupSupplier1.size(), groupSupplierList.size());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup1.size(), roleGroupTmpList.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupList);
        assertEquals(roleGroup1.size(), roleGroupList.size());
        
    }
    
    
    public void testMkWithdrawDelete() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(true);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.mkCreate(cp, g1);
        groupService.mkApprove(cp, null, g1);
        
        GroupTmpHolder group = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        group.setGroupUsers(g1.getGroupUsers());
        group.setRoleGroups(g1.getRoleGroups());
        group.setGroupSuppliers(g1.getGroupSuppliers());
        
        groupService.mkDelete(cp, group);
        
        GroupHolder oldGroup = groupService.selectGroupByKey(g1.getGroupOid());
        oldGroup.setGroupUsers(g1.getGroupUsers());
        oldGroup.setRoleGroups(g1.getRoleGroups());
        oldGroup.setGroupSuppliers(g1.getGroupSuppliers());
        groupService.mkWithdraw(cp, oldGroup, group);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.DELETE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.WITHDRAWN, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(rlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), rlt.getGroupId());
        assertEquals(g1.getGroupName(), rlt.getGroupName());
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserList);
        assertEquals(groupUser1.size(), groupUserList.size());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier1.size(), groupSupplierTmpList.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierList);
        assertEquals(groupSupplier1.size(), groupSupplierList.size());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup1.size(), roleGroupTmpList.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupList);
        assertEquals(roleGroup1.size(), roleGroupList.size());
    }

    
    public void testCreateGroupProfile() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(false);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.createGroupProfile(cp, g1);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(g1.getGroupId(), tmpRlt.getGroupId());
        assertEquals(g1.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.CREATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.APPROVED, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(rlt);
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserList);
        assertEquals(groupUser1.size(), groupUserList.size());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier1.size(), groupSupplierTmpList.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierList);
        assertEquals(groupSupplier1.size(), groupSupplierList.size());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup1.size(), roleGroupTmpList.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupList);
        assertEquals(roleGroup1.size(), roleGroupList.size());
        
    }
    
    
    public void testUpdateGroupProfile() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(false);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.createGroupProfile(cp, g1);
        
        GroupTmpHolder newGroup = new GroupTmpHolder();
        BeanUtils.copyProperties(g1, newGroup);
        newGroup.setGroupName(NEW_GROUP_NAME);
        newGroup.setGroupId(NEW_GROUP_ID);
        newGroup.setUpdateDate(new Date());
        newGroup.setUpdateBy(CREATOR);
        
        groupService.updateGroupProfile(cp, g1, newGroup);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNotNull(tmpRlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(newGroup.getGroupId(), tmpRlt.getGroupId());
        assertEquals(newGroup.getGroupName(), tmpRlt.getGroupName());
        assertEquals(DbActionType.UPDATE,tmpRlt.getActionType());
        assertEquals(cp.getLoginId(), tmpRlt.getActor());
        assertEquals(MkCtrlStatus.APPROVED, tmpRlt.getCtrlStatus());
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNotNull(rlt);
        assertEquals(GroupType.BUYER,tmpRlt.getGroupType());
        assertEquals(newGroup.getGroupId(), rlt.getGroupId());
        assertEquals(newGroup.getGroupName(), rlt.getGroupName());
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserTmpList);
        assertEquals(groupUser1.size(), groupUserTmpList.size());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertNotNull(groupUserList);
        assertEquals(groupUser1.size(), groupUserList.size());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierTmpList);
        assertEquals(groupSupplier1.size(), groupSupplierTmpList.size());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertNotNull(groupSupplierList);
        assertEquals(groupSupplier1.size(), groupSupplierList.size());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupTmpList);
        assertEquals(roleGroup1.size(), roleGroupTmpList.size());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertNotNull(roleGroupList);
        assertEquals(roleGroup1.size(), roleGroupList.size());
        
    }
    
    
    public void testRemoveGroupProfile() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        CommonParameterHolder cp = this.getCommonParameter(false);
        List<SupplierHolder> suppliers = new ArrayList<SupplierHolder>();
        suppliers.add(supplier);
        // buyer admin group
        List<GroupSupplierHolder> groupSupplier1 = this.initGroupSuppliers(suppliers, g1);
        groupSuppliers.addAll(groupSupplier1);
        List<RoleGroupHolder> roleGroup1 = this.initRoleGroups(buyerAdminRoles, g1);
        roleGroups.addAll(roleGroup1);
        List<GroupUserHolder> groupUser1 = this.initGroupUsers(buyerAdmins, g1);
        groupUsers.addAll(groupUser1);
        
        g1.setGroupSuppliers(groupSupplier1);
        g1.setGroupUsers(groupUser1);
        g1.setRoleGroups(roleGroup1);
        
        for (RoleTmpHolder role : roles)
        {
            roleTmpService.insert(role);
            roleService.insert(role);
        }
        
        for (UserProfileTmpHolder user : users)
        {
            userProfileTmpService.insert(user);
            userProfileService.insert(user);
        }
        
        groupService.createGroupProfile(cp, g1);
        groupService.removeGroupProfile(cp, g1);
        
        GroupTmpHolder tmpRlt = groupTmpService.selectGroupTmpByKey(g1.getGroupOid());
        assertNull(tmpRlt);
        
        GroupHolder rlt = groupService.selectGroupByKey(g1.getGroupOid());
        assertNull(rlt);
        
        List<GroupUserTmpHolder> groupUserTmpList = groupUserTmpService.selectGroupUserTmpByGroupOid(g1.getGroupOid());
        assertTrue(groupUserTmpList == null || groupUserTmpList.isEmpty());
        
        List<GroupUserHolder> groupUserList = groupUserService.selectGroupUserByGroupOid(g1.getGroupOid());
        assertTrue(groupUserList == null || groupUserList.isEmpty());
        
        List<GroupSupplierTmpHolder> groupSupplierTmpList = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierTmpList == null || groupSupplierTmpList.isEmpty());
        
        List<GroupSupplierHolder> groupSupplierList = groupSupplierService.selectGroupSupplierByGroupOid(g1.getGroupOid());
        assertTrue(groupSupplierList == null || groupSupplierList.isEmpty());
        
        List<RoleGroupTmpHolder> roleGroupTmpList = roleGroupTmpService.selectRoleGroupTmpByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupTmpList == null || roleGroupTmpList.isEmpty());
        
        List<RoleGroupHolder> roleGroupList = roleGroupService.selectRoleGroupByGroupOid(g1.getGroupOid());
        assertTrue(roleGroupList == null || roleGroupList.isEmpty());
        
    }
    
    
    // ****************************************************
    // private methods
    // ****************************************************
    
    @Before
    public void setUp() throws Exception
    {
        oidService = ctx.getBean("oidService", OidService.class);
        
        buyerService = ctx.getBean("buyerService", BuyerService.class);
        supplierService = ctx.getBean("supplierService", SupplierService.class);
        userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
        groupService = ctx.getBean("groupService", GroupService.class);
        groupTmpService = ctx.getBean("groupTmpService",GroupTmpService.class);
        userProfileTmpService = ctx.getBean("userProfileTmpService", UserProfileTmpService.class);
        groupUserService = ctx.getBean("groupUserService",GroupUserService.class);
        groupUserTmpService = ctx.getBean("groupUserTmpService", GroupUserTmpService.class);
        groupSupplierService = ctx.getBean("groupSupplierService",GroupSupplierService.class);
        groupSupplierTmpService = ctx.getBean("groupSupplierTmpService",GroupSupplierTmpService.class);
        roleGroupService = ctx.getBean("roleGroupService", RoleGroupService.class);
        roleGroupTmpService = ctx.getBean("roleGroupTmpService", RoleGroupTmpService.class);
        groupTradingPartnerService = ctx.getBean("groupTradingPartnerService",GroupTradingPartnerService.class);
        groupTradingPartnerTmpService = ctx.getBean("groupTradingPartnerTmpService", GroupTradingPartnerTmpService.class);
        roleService = ctx.getBean("roleService", RoleService.class);
        roleTmpService = ctx.getBean("roleTmpService", RoleTmpService.class);
        tradingPartnerService = ctx.getBean("tradingPartnerService",TradingPartnerService.class);
        
        
        groupUserMapper = ctx.getBean("groupUserMapper",GroupUserMapper.class);
        groupUserTmpMapper = ctx.getBean("groupUserTmpMapper",GroupUserTmpMapper.class);
        roleGroupMapper = ctx.getBean("roleGroupMapper", RoleGroupMapper.class);
        roleGroupTmpMapper = ctx.getBean("roleGroupTmpMapper",RoleGroupTmpMapper.class);
        groupSupplierMapper = ctx.getBean("groupSupplierMapper", GroupSupplierMapper.class);
        groupSupplierTmpMapper = ctx.getBean("groupSupplierTmpMapper",GroupSupplierTmpMapper.class);
        groupTradingPartnerMapper = ctx.getBean("groupTradingPartnerMapper",GroupTradingPartnerMapper.class);
        groupTradingPartnerTmpMapper = ctx.getBean("groupTradingPartnerTmpMapper", GroupTradingPartnerTmpMapper.class);
        
        this.initBuyers();
        this.initSuppliers();
        this.initUsers();
        this.initTradingPartners();
        this.initGroups();
        this.initRoles();
        groupUsers = new ArrayList<GroupUserHolder>();
        groupTps = new ArrayList<GroupTradingPartnerHolder>();
        roleGroups = new ArrayList<RoleGroupHolder>();
        groupSuppliers = new ArrayList<GroupSupplierHolder>();
    }
    
    
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        for (GroupUserHolder groupUser : groupUsers)
        {
            GroupUserTmpHolder delete = new GroupUserTmpHolder();
            delete.setGroupOid(groupUser.getGroupOid());
            groupUserMapper.delete(delete);
            groupUserTmpMapper.delete(delete);
        }
        
        for (RoleGroupHolder roleGroup : roleGroups)
        {
            RoleGroupTmpHolder delete = new RoleGroupTmpHolder();
            delete.setGroupOid(roleGroup.getGroupOid());
            roleGroupMapper.delete(delete);
            roleGroupTmpMapper.delete(delete);
        }
        
        for (GroupTradingPartnerHolder groupTp : groupTps)
        {
            GroupTradingPartnerTmpHolder delete = new GroupTradingPartnerTmpHolder();
            delete.setGroupOid(groupTp.getGroupOid());
            groupTradingPartnerMapper.delete(delete);
            groupTradingPartnerTmpMapper.delete(delete);
        }
        
        for (GroupSupplierHolder groupSupp : groupSuppliers)
        {
            GroupSupplierTmpHolder delete = new GroupSupplierTmpHolder();
            delete.setGroupOid(groupSupp.getGroupOid());
            groupSupplierMapper.delete(delete);
            groupSupplierTmpMapper.delete(delete);
        }
        
        for (UserProfileHolder user : users)
        {
            UserProfileTmpHolder delete = new UserProfileTmpHolder();
            delete.setUserOid(user.getUserOid());
            userProfileService.delete(delete);
            userProfileTmpService.delete(delete);
        }
        
        for (RoleHolder role : roles)
        {
            RoleTmpHolder delete = new RoleTmpHolder();
            delete.setRoleOid(role.getRoleOid());
            roleService.delete(delete);
            roleTmpService.delete(delete);
        }
        
        for (TradingPartnerHolder tp : tradingPartners)
        {
            TradingPartnerHolder delete = new TradingPartnerHolder();
            delete.setTradingPartnerOid(tp.getTradingPartnerOid());
            tradingPartnerService.delete(delete);
        }
        
        this.deleteGroups(g1,g2,g3,g4);
        BuyerHolder deleteBuyer = new BuyerHolder();
        deleteBuyer.setBuyerOid(buyer.getBuyerOid());
        buyerService.delete(deleteBuyer);
        
        SupplierHolder deleteSupplier = new SupplierHolder();
        deleteSupplier.setSupplierOid(supplier.getSupplierOid());
        supplierService.delete(deleteSupplier);
        
    }
    
    
    private void initRoles() throws Exception
    {
        roles = new ArrayList<RoleTmpHolder>();
        buyerAdminRoles = new ArrayList<RoleTmpHolder>();
        RoleTmpHolder r1 = new RoleTmpHolder();
        r1.setRoleOid(oidService.getOid());
        r1.setRoleId("buyerAdmin1");
        r1.setRoleName("buyer Admin 1");
        r1.setRoleType(RoleType.BUYER);
        r1.setBuyerOid(buyer.getBuyerOid());
        r1.setCreateDate(new Date());
        r1.setCreateBy(CREATOR);
        r1.setUserTypeOid(BigDecimal.valueOf(2));
        r1.setActor("testJunter");
        r1.setActionType(DbActionType.CREATE);
        r1.setActionDate(new Date());
        r1.setCtrlStatus(MkCtrlStatus.APPROVED);
        buyerAdminRoles.add(r1);
        
        
        RoleTmpHolder r2 = new RoleTmpHolder();
        BeanUtils.copyProperties(r1, r2);
        r2.setRoleOid(oidService.getOid());
        r2.setRoleId("buyerAdmin2");
        r2.setRoleName("buyer Admin 2");
        buyerAdminRoles.add(r2);
        
        buyerUserRoles = new ArrayList<RoleTmpHolder>();
        RoleTmpHolder r3 = new RoleTmpHolder();
        BeanUtils.copyProperties(r1, r3);
        r3.setRoleOid(oidService.getOid());
        r3.setRoleId("buyerUser1");
        r3.setRoleName("buyer user 1");
        r3.setUserTypeOid(BigDecimal.valueOf(4));
        buyerUserRoles.add(r3);
        
        RoleTmpHolder r4 = new RoleTmpHolder();
        BeanUtils.copyProperties(r3, r4);
        r4.setRoleOid(oidService.getOid());
        r4.setRoleId("buyerUser2");
        r4.setRoleName("buyer user 2");
        buyerUserRoles.add(r4);
        
        supplierAdminRoles = new ArrayList<RoleTmpHolder>();
        RoleTmpHolder r5 = new RoleTmpHolder();
        r5.setRoleOid(oidService.getOid());
        r5.setRoleId("supplierAdmin1");
        r5.setRoleName("supplier Admin 1");
        r5.setRoleType(RoleType.SUPPLIER);
        r5.setCreateDate(new Date());
        r5.setCreateBy(CREATOR);
        r5.setUserTypeOid(BigDecimal.valueOf(3));
        r5.setActor("testJunter");
        r5.setActionType(DbActionType.CREATE);
        r5.setActionDate(new Date());
        r5.setCtrlStatus(MkCtrlStatus.APPROVED);
        supplierAdminRoles.add(r5);
        
        RoleTmpHolder r6 = new RoleTmpHolder();
        BeanUtils.copyProperties(r5, r6);
        r6.setRoleOid(oidService.getOid());
        r6.setRoleId("supplierAdmin2");
        r6.setRoleName("supplier Admin 2");
        supplierAdminRoles.add(r6);
        
        supplierUserRoles = new ArrayList<RoleTmpHolder>();
        RoleTmpHolder r7 = new RoleTmpHolder();
        BeanUtils.copyProperties(r5, r7);
        r7.setRoleOid(oidService.getOid());
        r7.setRoleId("supplierUser1");
        r7.setRoleName("supplier User 1");
        r7.setUserTypeOid(BigDecimal.valueOf(5));
        supplierUserRoles.add(r7);
        
        RoleTmpHolder r8 = new RoleTmpHolder();
        BeanUtils.copyProperties(r7, r8);
        r8.setRoleOid(oidService.getOid());
        r8.setRoleId("supplierUser2");
        r8.setRoleName("supplier User 2");
        supplierUserRoles.add(r8);
        
        roles.addAll(buyerAdminRoles);
        roles.addAll(buyerUserRoles);
        roles.addAll(supplierAdminRoles);
        roles.addAll(supplierUserRoles);
    }
    
    
    private void initUsers() throws Exception
    {
        users = new ArrayList<UserProfileTmpHolder>();
        admin = new UserProfileTmpHolder();
        admin.setUserOid(oidService.getOid());
        admin.setLoginId("testAdmin");
        admin.setUserName("admin");
        admin.setLoginMode("PASSWORD");
        admin.setEmail("jiangming@pracbiz.com");
        admin.setGender("M");
        admin.setActive(true);
        admin.setBlocked(false);
        admin.setInit(false);
        admin.setCreateDate(new Date());
        admin.setCreateBy(CREATOR);
        admin.setUserType(BigDecimal.ONE);
        admin.setActionType(DbActionType.CREATE);
        admin.setCtrlStatus(MkCtrlStatus.APPROVED);
        admin.setActor(admin.getLoginId());
        admin.setActionDate(new Date());
        users.add(admin);
        
        buyerAdmins = new ArrayList<UserProfileTmpHolder>();
        UserProfileTmpHolder buyerAdmin1 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(admin, buyerAdmin1);
        buyerAdmin1.setUserOid(oidService.getOid());
        buyerAdmin1.setLoginId("testBuyerAdmin1");
        buyerAdmin1.setUserName("BuyerAdmin1");
        buyerAdmin1.setUserType(BigDecimal.valueOf(2));
        buyerAdmin1.setBuyerOid(buyer.getBuyerOid());
        buyerAdmins.add(buyerAdmin1);
        
        UserProfileTmpHolder buyerAdmin2 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(buyerAdmin1, buyerAdmin2);
        buyerAdmin2.setUserOid(oidService.getOid());
        buyerAdmin2.setLoginId("testBuyerAdmin2");
        buyerAdmin2.setUserName("BuyerAdmin1");
        buyerAdmins.add(buyerAdmin2);
        
        UserProfileTmpHolder buyerAdmin3 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(buyerAdmin1, buyerAdmin3);
        buyerAdmin3.setUserOid(oidService.getOid());
        buyerAdmin3.setLoginId("testBuyerAdmin3");
        buyerAdmin3.setUserName("BuyerAdmin3");
        buyerAdmins.add(buyerAdmin3);
        
        UserProfileTmpHolder buyerAdmin4 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(buyerAdmin1, buyerAdmin4);
        buyerAdmin4.setUserOid(oidService.getOid());
        buyerAdmin4.setLoginId("testBuyerAdmin4");
        buyerAdmin4.setUserName("BuyerAdmin4");
        buyerAdmins.add(buyerAdmin4);
        
        buyerUsers = new ArrayList<UserProfileTmpHolder>();
        UserProfileTmpHolder buyerUser1 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(admin, buyerUser1);
        buyerUser1.setUserOid(oidService.getOid());
        buyerUser1.setLoginId("testBuyerUser1");
        buyerUser1.setUserName("BuyerUser1");
        buyerUser1.setUserType(BigDecimal.valueOf(4));
        buyerUser1.setBuyerOid(buyer.getBuyerOid());
        buyerUsers.add(buyerUser1);
        
        UserProfileTmpHolder buyerUser2 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(buyerUser1, buyerUser2);
        buyerUser2.setUserOid(oidService.getOid());
        buyerUser2.setLoginId("testBuyerUser2");
        buyerUser2.setUserName("testBuyerUser2");
        buyerUsers.add(buyerUser2);
        
        UserProfileTmpHolder buyerUser3 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(buyerUser1, buyerUser3);
        buyerUser3.setUserOid(oidService.getOid());
        buyerUser3.setLoginId("testBuyerUser3");
        buyerUser3.setUserName("testBuyerUser3");
        buyerUsers.add(buyerUser3);
        
        UserProfileTmpHolder buyerUser4 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(buyerUser1, buyerUser4);
        buyerUser4.setUserOid(oidService.getOid());
        buyerUser4.setLoginId("testBuyerUser4");
        buyerUser4.setUserName("testBuyerUser4");
        buyerUsers.add(buyerUser4);
        
        supplierAdmins = new ArrayList<UserProfileTmpHolder>();
        UserProfileTmpHolder supplierAdmin1 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(admin, supplierAdmin1);
        supplierAdmin1.setUserOid(oidService.getOid());
        supplierAdmin1.setLoginId("testSuppAdmin1");
        supplierAdmin1.setUserName("SuppAdmin1");
        supplierAdmin1.setUserType(BigDecimal.valueOf(3));
        supplierAdmin1.setSupplierOid(supplier.getSupplierOid());
        supplierAdmins.add(supplierAdmin1);
        
        UserProfileTmpHolder supplierAdmin2 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(supplierAdmin1, supplierAdmin2);
        supplierAdmin2.setUserOid(oidService.getOid());
        supplierAdmin2.setLoginId("testSuppAdmin2");
        supplierAdmin2.setUserName("SuppAdmin2");
        supplierAdmins.add(supplierAdmin2);
        
        UserProfileTmpHolder supplierAdmin3 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(supplierAdmin1, supplierAdmin3);
        supplierAdmin3.setUserOid(oidService.getOid());
        supplierAdmin3.setLoginId("testSuppAdmin3");
        supplierAdmin3.setUserName("SuppAdmin3");
        supplierAdmins.add(supplierAdmin3);
        
        UserProfileTmpHolder supplierAdmin4 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(supplierAdmin1, supplierAdmin4);
        supplierAdmin4.setUserOid(oidService.getOid());
        supplierAdmin4.setLoginId("testSuppAdmin4");
        supplierAdmin4.setUserName("SuppAdmin4");
        supplierAdmins.add(supplierAdmin4);
        
        supplierUsers = new ArrayList<UserProfileTmpHolder>();
        UserProfileTmpHolder supplierUser1 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(supplierAdmin1, supplierUser1);
        supplierUser1.setUserOid(oidService.getOid());
        supplierUser1.setLoginId("testSuppUser1");
        supplierUser1.setUserName("SuppUser1");
        supplierUsers.add(supplierUser1);
        
        UserProfileTmpHolder supplierUser2 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(supplierAdmin1, supplierUser2);
        supplierUser2.setUserOid(oidService.getOid());
        supplierUser2.setLoginId("testSuppUser2");
        supplierUser2.setUserName("SuppUser2");
        supplierUsers.add(supplierUser2);
        
        UserProfileTmpHolder supplierUser3 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(supplierAdmin1, supplierUser3);
        supplierUser3.setUserOid(oidService.getOid());
        supplierUser3.setLoginId("testSuppUser3");
        supplierUser3.setUserName("SuppUser3");
        supplierUsers.add(supplierUser3);
        
        UserProfileTmpHolder supplierUser4 = new UserProfileTmpHolder();
        BeanUtils.copyProperties(supplierAdmin1, supplierUser4);
        supplierUser4.setUserOid(oidService.getOid());
        supplierUser4.setLoginId("testSuppUser4");
        supplierUser4.setUserName("SuppUser4");
        supplierUsers.add(supplierUser4);
        
        users.addAll(buyerAdmins);
        users.addAll(buyerUsers);
        users.addAll(supplierAdmins);
        users.addAll(supplierUsers);
    }
    
    
    private void initTradingPartners() throws Exception
    {
        tradingPartners = new ArrayList<TradingPartnerHolder>();
        TradingPartnerHolder tp1 = new TradingPartnerHolder();
        tp1.setTradingPartnerOid(oidService.getOid());
        tp1.setBuyerOid(buyer.getBuyerOid());
        tp1.setSupplierBuyerCode("buyer1");
        tp1.setBuyerContactTel(buyer.getContactTel());
        tp1.setBuyerContactPerson(buyer.getContactName());
        tp1.setBuyerContactMobile(buyer.getContactMobile());
        tp1.setBuyerContactFax(buyer.getContactFax());
        tp1.setBuyerContactEmail(buyer.getContactEmail());
        tp1.setSupplierOid(supplier.getSupplierOid());
        tp1.setSupplierContactTel(supplier.getContactTel());
        tp1.setSupplierContactPerson(supplier.getContactName());
        tp1.setSupplierContactMobile(supplier.getContactMobile());
        tp1.setSupplierContactFax(supplier.getContactFax());
        tp1.setSupplierContactEmail(supplier.getContactEmail());
        tp1.setBuyerSupplierCode("supplier1");
        tp1.setCreateDate(new Date());
        tp1.setCreateBy(CREATOR);
        tp1.setActive(true);
        tradingPartners.add(tp1);
        
        TradingPartnerHolder tp2 = new TradingPartnerHolder();
        BeanUtils.copyProperties(tp1, tp2);
        tp2.setTradingPartnerOid(oidService.getOid());
        tp2.setSupplierBuyerCode("buyer2");
        tp2.setBuyerSupplierCode("supplier2");
        tradingPartners.add(tp2);
        
        TradingPartnerHolder tp3 = new TradingPartnerHolder();
        BeanUtils.copyProperties(tp1, tp3);
        tp3.setTradingPartnerOid(oidService.getOid());
        tp3.setSupplierBuyerCode("buyer3");
        tp3.setBuyerSupplierCode("supplier3");
        tradingPartners.add(tp3);
        
        TradingPartnerHolder tp4 = new TradingPartnerHolder();
        BeanUtils.copyProperties(tp1, tp4);
        tp4.setTradingPartnerOid(oidService.getOid());
        tp4.setSupplierBuyerCode("buyer4");
        tp4.setBuyerSupplierCode("supplier4");
        tradingPartners.add(tp4);
    }
    
    
    private void initGroups() throws Exception
    {
        g1 = new GroupTmpHolder();
        g1.setGroupOid(oidService.getOid());
        g1.setGroupId(GROUP1_ID);
        g1.setGroupName(GROUP1_NAME);
        g1.setGroupType(GroupType.BUYER);
        g1.setCreateDate(new Date());
        g1.setCreateBy(CREATOR);
        g1.setUserTypeOid(BigDecimal.valueOf(2));
        g1.setBuyerOid(buyer.getBuyerOid());
        g1.setActionType(DbActionType.CREATE);
        g1.setCtrlStatus(MkCtrlStatus.APPROVED);
        g1.setActor(admin.getLoginId());
        g1.setActionDate(new Date());
        
        g2 = new GroupTmpHolder();
        g2.setGroupOid(oidService.getOid());
        g2.setGroupId(GROUP2_ID);
        g2.setGroupName(GROUP2_NAME);
        g2.setGroupType(GroupType.SUPPLIER);
        g2.setCreateDate(new Date());
        g2.setCreateBy(CREATOR);
        g2.setUserTypeOid(BigDecimal.valueOf(3));
        g2.setSupplierOid(supplier.getSupplierOid());
        g2.setActionType(DbActionType.CREATE);
        g2.setCtrlStatus(MkCtrlStatus.APPROVED);
        g2.setActor(admin.getLoginId());
        g2.setActionDate(new Date());
        
        g3 = new GroupTmpHolder();
        g3.setGroupOid(oidService.getOid());
        g3.setGroupId(GROUP3_ID);
        g3.setGroupName(GROUP3_NAME);
        g3.setGroupType(GroupType.BUYER);
        g3.setCreateDate(new Date());
        g3.setCreateBy(CREATOR);
        g3.setUserTypeOid(BigDecimal.valueOf(4));
        g3.setBuyerOid(buyer.getBuyerOid());
        g3.setActionType(DbActionType.CREATE);
        g3.setCtrlStatus(MkCtrlStatus.APPROVED);
        g3.setActor(buyerAdmins.get(0).getLoginId());
        g3.setActionDate(new Date());
        
        g4 = new GroupTmpHolder();
        g4.setGroupOid(oidService.getOid());
        g4.setGroupId(GROUP4_ID);
        g4.setGroupName(GROUP4_NAME);
        g4.setGroupType(GroupType.SUPPLIER);
        g4.setCreateDate(new Date());
        g4.setCreateBy(CREATOR);
        g4.setUserTypeOid(BigDecimal.valueOf(5));
        g4.setSupplierOid(supplier.getSupplierOid());
        g4.setActionType(DbActionType.CREATE);
        g4.setCtrlStatus(MkCtrlStatus.APPROVED);
        g4.setActor(supplierAdmins.get(0).getLoginId());
        g4.setActionDate(new Date());
    }
    
    
    private void initBuyers() throws Exception
    {
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
        buyer.setContactName(CREATOR);
        buyer.setContactTel("1111111111");
        buyer.setContactEmail("testJuniter@pracbiz.com");
        buyer.setChannel("testBuyerChannel");
        buyer.setDeploymentMode(DeploymentMode.LOCAL);
    }
    
    
    private void initSuppliers() throws Exception
    {
        supplier = new SupplierHolder();
        BeanUtils.copyProperties(buyer, supplier);
        supplier.setSupplierOid(oidService.getOid());
        supplier.setSupplierCode("testSupplierCode");
        supplier.setSupplierName("testSupplierName");
        supplier.setAddress1("testSupplierAddr1");
        supplier.setContactName(CREATOR);
        supplier.setMboxId("testSupplierMbox"); 
        supplier.setContactTel("1111111111");
        supplier.setContactEmail("testJuniter@pracbiz.com");
        supplier.setDeploymentMode(DeploymentMode.LOCAL);
        supplier.setClientEnabled(false);
        supplier.setRequireReport(false);
        supplier.setRequireTranslationIn(false);
        supplier.setRequireTranslationOut(false);
    }
    
    
    private List<GroupUserHolder> initGroupUsers(List<UserProfileTmpHolder> users, GroupTmpHolder  ... groups)
    {
        List<GroupUserHolder> groupUsers = new ArrayList<GroupUserHolder>();
        for(GroupTmpHolder group : groups)
        {
            for(UserProfileTmpHolder user : users)
            {
                GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
                groupUser.setGroupOid(group.getGroupOid());
                groupUser.setUserOid(user.getUserOid());
                groupUser.setActionType(DbActionType.CREATE);
                groupUser.setLastUpdateFrom(LastUpdateFrom.GROUP);
                groupUser.setApproved(false);
                groupUsers.add(groupUser);
            }
        }
        
        return groupUsers;
    }
    
    
    private List<RoleGroupHolder> initRoleGroups(List<RoleTmpHolder> roles, GroupTmpHolder  ... groups)
    {
        List<RoleGroupHolder> roleGroups = new ArrayList<RoleGroupHolder>();
        for(GroupTmpHolder group : groups)
        {
            for(RoleTmpHolder role : roles)
            {
                RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
                roleGroup.setGroupOid(group.getGroupOid());
                roleGroup.setRoleOid(role.getRoleOid());
                roleGroups.add(roleGroup);
            }
        }
        
        return roleGroups;
    }
    
    
    private List<GroupTradingPartnerHolder> initGroupTp(List<TradingPartnerHolder> tps, GroupTmpHolder  ... groups)
    {
        List<GroupTradingPartnerHolder> groupTps = new ArrayList<GroupTradingPartnerHolder>();
        for(GroupTmpHolder group : groups)
        {
            for(TradingPartnerHolder tp : tps)
            {
                GroupTradingPartnerTmpHolder groupTp = new GroupTradingPartnerTmpHolder();
                groupTp.setGroupOid(group.getGroupOid());
                groupTp.setTradingPartnerOid(tp.getTradingPartnerOid());
                groupTps.add(groupTp);
            }
        }
        
        return groupTps;
    }
    
    
    private List<GroupSupplierHolder> initGroupSuppliers(List<SupplierHolder> suppliers, GroupTmpHolder  ... groups)
    {
        List<GroupSupplierHolder> groupSuppliers = new ArrayList<GroupSupplierHolder>();
        for (GroupTmpHolder group : groups)
        {
            for (SupplierHolder supplier : suppliers)
            {
                GroupSupplierTmpHolder groupSupp = new GroupSupplierTmpHolder();
                groupSupp.setGroupOid(group.getGroupOid());
                groupSupp.setSupplierOid(supplier.getSupplierOid());
                groupSuppliers.add(groupSupp);
            }
        }
        
        return groupSuppliers;
    }
    
    
    private void deleteGroups(GroupTmpHolder  ... groups) throws Exception
    {
        for (GroupTmpHolder group : groups)
        {
            GroupTmpHolder delete = new GroupTmpHolder();
            delete.setGroupOid(group.getGroupOid());
            groupTmpService.delete(group);
            groupService.delete(delete);
        }
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
