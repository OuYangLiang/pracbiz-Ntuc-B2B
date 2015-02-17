package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.GroupType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.GroupTmpMapper;
import com.pracbiz.b2bportal.core.mapper.GroupUserMapper;
import com.pracbiz.b2bportal.core.mapper.GroupUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserTmpMapper;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.GroupUserTmpService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.RoleUserService;
import com.pracbiz.b2bportal.core.service.RoleUserTmpService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */
public class UserProfileServiceTest extends BaseServiceTestCase
{
    
    private final UserProfileService userProfileService;
    private final UserProfileTmpService userProfileTmpService;
    private final GroupUserService groupUserService;
    private final GroupUserTmpService groupUserTmpService;
    private final BuyerService buyerService;
    private final SupplierService supplierService;
    private final RoleService roleService;
    private final RoleTmpService roleTmpService;
    private final GroupService groupService;
    private final GroupTmpMapper groupTmpMapper;
    private final RoleUserService roleUserService;
    private final RoleUserTmpService roleUserTmpService;
    private final RoleUserMapper roleUserMapper;
    private final GroupUserMapper groupUserMapper;
    private final RoleUserTmpMapper roleUserTmpMapper;
    private final GroupUserTmpMapper groupUserTmpMapper;
    
    private UserProfileTmpHolder user1, user2, user3;
    private static final String LOGIN_ID = "SYSTEM";
    private static final String IP = "localhost";
    private static final String NEW_USER_NAME="new user name";
    private RoleUserTmpHolder roleUserTmp1, roleUserTmp2;
    private BuyerHolder buyer;
    private SupplierHolder supplier;
    private RoleTmpHolder role1, role2;
    private GroupTmpHolder group;
    private GroupUserTmpHolder groupUserTmp;
    
    public UserProfileServiceTest()
    {
        userProfileService = ctx.getBean("userProfileService", UserProfileService.class);
        groupUserTmpService = ctx.getBean("groupUserTmpService", GroupUserTmpService.class);
        groupUserService = ctx.getBean("groupUserService", GroupUserService.class);
        buyerService = ctx.getBean("buyerService", BuyerService.class);
        supplierService = ctx.getBean("supplierService", SupplierService.class);
        roleService = ctx.getBean("roleService", RoleService.class);
        roleTmpService = ctx.getBean("roleTmpService", RoleTmpService.class);
        roleUserService = ctx.getBean("roleUserService", RoleUserService.class);
        roleUserTmpService = ctx.getBean("roleUserTmpService", RoleUserTmpService.class);
        groupService = ctx.getBean("groupService", GroupService.class);
        userProfileTmpService = ctx.getBean("userProfileTmpService", UserProfileTmpService.class);
        groupUserMapper = ctx.getBean("groupUserMapper", GroupUserMapper.class);
        roleUserMapper = ctx.getBean("roleUserMapper", RoleUserMapper.class);
        groupUserTmpMapper = ctx.getBean("groupUserTmpMapper", GroupUserTmpMapper.class);
        roleUserTmpMapper = ctx.getBean("roleUserTmpMapper", RoleUserTmpMapper.class);
        groupTmpMapper = ctx.getBean("groupTmpMapper", GroupTmpMapper.class);
    }
    
    
    public void testBasicCrd() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        int curRecords = 0;
        int newRecordsCount = 0;
        
        List<? extends Object> records = userProfileService.select(new UserProfileTmpExHolder());
        
        if (null != records && !records.isEmpty())
        {
            curRecords = records.size();
        }
        
        userProfileService.insert(user1);
        newRecordsCount ++;
        userProfileService.insert(user2);
        newRecordsCount ++;
        userProfileService.insert(user3);
        newRecordsCount ++;
        
        records = userProfileService.select(new UserProfileTmpExHolder());
        
        assertTrue(records.size() == curRecords + newRecordsCount);
        
        userProfileService.delete(user1);
        userProfileService.delete(user2);
        userProfileService.delete(user3);

        buyerService.delete(buyer);
        supplierService.delete(supplier);
        
        records = userProfileService.select(new UserProfileTmpExHolder());
        
        assertTrue(records.size() == curRecords);
    }
    
    
    public void testGetUserProfileByLoginId() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        userProfileService.insert(user1);
        userProfileService.insert(user2);
        userProfileService.insert(user3);
        
        UserProfileHolder user = userProfileService.getUserProfileByLoginId(user1.getLoginId());
        assertNotNull(user);
        assertEquals(user.getUserName(), user1.getUserName());
        assertEquals(user.getLoginId(), user1.getLoginId());

        userProfileService.delete(user1);
        userProfileService.delete(user2);
        userProfileService.delete(user3);

        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testSelectUserProfileByKey() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        userProfileService.insert(user1);
        userProfileService.insert(user2);
        userProfileService.insert(user3);
        
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user1.getUserOid());
        assertNotNull(user);
        assertEquals(user.getUserOid(), user1.getUserOid());
        assertEquals(user.getLoginId(), user1.getLoginId());
        
        userProfileService.delete(user1);
        userProfileService.delete(user2);
        userProfileService.delete(user3);

        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testCreateUserProfile() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        groupService.createGroupProfile(this.getMkCommonParameter(false), group);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role1);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role2);
        List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
        ruList.add(roleUserTmp1);
        ruList.add(roleUserTmp2);
        user2.setRoleUsers(ruList);
        
        List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
        guList.add(groupUserTmp);
        user2.setGroupUsers(guList);
        
        userProfileService.createUserProfile(this.getMkCommonParameter(false), "http://localhost:8080/b2bportal/user/", IP, user2,true);
        
        UserProfileTmpHolder userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        List<? extends Object> tmpRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(user2.getUserOid());
        List<? extends Object> roleUserList = roleUserService.selectRoleUserByUserOid(user2.getUserOid());
        
        List<? extends Object> tmpGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(user2.getUserOid());
        List<? extends Object> groupUserList = groupUserService.selectGroupUserByUserOid(user2.getUserOid());
        
        assertEquals(tmpGroupUserList.size(), groupUserList.size());
        assertEquals(tmpRoleUserList.size(), roleUserList.size());
        assertNotNull(user);
        assertNotNull(userTmp);
        assertEquals(user.getLoginId(), userTmp.getLoginId());
        assertEquals(user.getCreateDate(), userTmp.getCreateDate());
        assertEquals(userTmp.getActionType(), DbActionType.CREATE);
        assertEquals(userTmp.getCtrlStatus(), MkCtrlStatus.APPROVED);
        
        GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
        groupUser.setGroupOid(groupUserTmp.getGroupOid());
        
        groupUserMapper.delete(groupUser);
        groupUserTmpMapper.delete(groupUser);
        
        RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
        ruTmpParam.setUserOid(user2.getUserOid());

        roleUserMapper.delete(ruTmpParam);
        roleUserTmpMapper.delete(ruTmpParam);

        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam);
        UserProfileTmpHolder userTmpParam = new UserProfileTmpHolder();
        userTmpParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userTmpParam);
        
        GroupHolder groupParam = new GroupHolder();
        groupParam.setGroupOid(group.getGroupOid());
        groupService.delete(groupParam);
        
        GroupTmpHolder groupTmpParam = new GroupTmpHolder();
        groupTmpParam.setGroupOid(group.getGroupOid());
        groupTmpMapper.delete(groupTmpParam);

        roleService.delete(role1);
        roleService.delete(role2);
        
        RoleTmpHolder roleTmp1Param = new RoleTmpHolder();
        roleTmp1Param.setRoleOid(role1.getRoleOid());
        roleTmpService.delete(roleTmp1Param);
        
        RoleTmpHolder roleTmp2Param = new RoleTmpHolder();
        roleTmp2Param.setRoleOid(role2.getRoleOid());
        roleTmpService.delete(roleTmp2Param);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testUpdateUserProfile() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        groupService.createGroupProfile(this.getMkCommonParameter(false), group);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role1);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role2);
        List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
        ruList.add(roleUserTmp1);
        ruList.add(roleUserTmp2);
        user2.setRoleUsers(ruList);
        
        List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
        guList.add(groupUserTmp);
        user2.setGroupUsers(guList);
        
        userProfileService.createUserProfile(this.getMkCommonParameter(false), "http://localhost:8080/b2bportal/user/", IP, user2,true);
        
        UserProfileTmpHolder oldUserTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileTmpHolder newUserTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        
        oldUserTmp.setGroupUsers(new ArrayList<GroupUserHolder>());
        oldUserTmp.setRoleUsers(new ArrayList<RoleUserHolder>());
        newUserTmp.setGroupUsers(new ArrayList<GroupUserHolder>());
        newUserTmp.setRoleUsers(new ArrayList<RoleUserHolder>());
        
        List<? extends Object> oldGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(user2.getUserOid());
        for (Object oldGroupUser : oldGroupUserList)
        {
            oldUserTmp.getGroupUsers().add((GroupUserHolder) oldGroupUser);
            newUserTmp.getGroupUsers().add((GroupUserHolder) oldGroupUser);
        }

        List<? extends Object> oldRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(user2.getUserOid());
        for (Object oldRoleUser : oldRoleUserList)
        {
            oldUserTmp.getRoleUsers().add((RoleUserHolder) oldRoleUser);
            newUserTmp.getRoleUsers().add((RoleUserHolder) oldRoleUser);
        }
        
        newUserTmp.setUserName(NEW_USER_NAME);
        newUserTmp.getRoleUsers().remove(0);
        newUserTmp.setUpdateDate(new Date());
        
        userProfileService.updateUserProfile(this.getMkCommonParameter(false), oldUserTmp, newUserTmp, "http://localhost:8080/b2bportal/user/", IP);
        
        List<? extends Object> newGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(user2.getUserOid());
        List<? extends Object> newRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(user2.getUserOid());
        
        UserProfileTmpHolder userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        assertEquals(newGroupUserList.size(), oldGroupUserList.size());
        assertEquals(newRoleUserList.size(), oldRoleUserList.size() -1);
        assertNotNull(user);
        assertNotNull(userTmp);
        assertEquals(user.getUserName(), NEW_USER_NAME);
        assertEquals(userTmp.getUserName(), NEW_USER_NAME);
        assertEquals(user.getCreateDate(), userTmp.getCreateDate());
        assertEquals(userTmp.getActionType(), DbActionType.UPDATE);
        assertEquals(userTmp.getCtrlStatus(), MkCtrlStatus.APPROVED);
        
        GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
        groupUser.setGroupOid(groupUserTmp.getGroupOid());
        
        groupUserMapper.delete(groupUser);
        groupUserTmpMapper.delete(groupUser);
        
        RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
        ruTmpParam.setUserOid(user2.getUserOid());
        
        roleUserMapper.delete(ruTmpParam);
        roleUserTmpMapper.delete(ruTmpParam);
        
        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam);
        UserProfileTmpHolder userTmpParam = new UserProfileTmpHolder();
        userTmpParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userTmpParam);
        
        GroupHolder groupParam = new GroupHolder();
        groupParam.setGroupOid(group.getGroupOid());
        groupService.delete(groupParam);
        
        GroupTmpHolder groupTmpParam = new GroupTmpHolder();
        groupTmpParam.setGroupOid(group.getGroupOid());
        groupTmpMapper.delete(groupTmpParam);
        
        roleService.delete(role1);
        roleService.delete(role2);
        
        RoleTmpHolder roleTmp1Param = new RoleTmpHolder();
        roleTmp1Param.setRoleOid(role1.getRoleOid());
        roleTmpService.delete(roleTmp1Param);
        
        RoleTmpHolder roleTmp2Param = new RoleTmpHolder();
        roleTmp2Param.setRoleOid(role2.getRoleOid());
        roleTmpService.delete(roleTmp2Param);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }

    
    public void testRemoveUserProfile() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        groupService.createGroupProfile(this.getMkCommonParameter(false), group);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role1);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role2);
        List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
        ruList.add(roleUserTmp1);
        ruList.add(roleUserTmp2);
        user2.setRoleUsers(ruList);
        
        List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
        guList.add(groupUserTmp);
        user2.setGroupUsers(guList);
        
        userProfileService.createUserProfile(this.getMkCommonParameter(false), "http://localhost:8080/b2bportal/user/", IP, user2,true);
        userProfileService.removeUserProfile(this.getMkCommonParameter(false), user2, false);
        
        UserProfileTmpHolder userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        assertNull(user);
        assertNull(userTmp);
        
        GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
        groupUser.setGroupOid(groupUserTmp.getGroupOid());
        
        groupUserMapper.delete(groupUser);
        groupUserTmpMapper.delete(groupUser);
        
        RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
        ruTmpParam.setUserOid(user2.getUserOid());
        
        roleUserMapper.delete(ruTmpParam);
        roleUserTmpMapper.delete(ruTmpParam);
        
        GroupHolder groupParam = new GroupHolder();
        groupParam.setGroupOid(group.getGroupOid());
        groupService.delete(groupParam);
        
        GroupTmpHolder groupTmpParam = new GroupTmpHolder();
        groupTmpParam.setGroupOid(group.getGroupOid());
        groupTmpMapper.delete(groupTmpParam);
        
        roleService.delete(role1);
        roleService.delete(role2);
        
        RoleTmpHolder roleTmp1Param = new RoleTmpHolder();
        roleTmp1Param.setRoleOid(role1.getRoleOid());
        roleTmpService.delete(roleTmp1Param);
        
        RoleTmpHolder roleTmp2Param = new RoleTmpHolder();
        roleTmp2Param.setRoleOid(role2.getRoleOid());
        roleTmpService.delete(roleTmp2Param);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }

    
    public void testUpdate() throws Exception
    {

        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        userProfileService.insert(user1);
        userProfileService.insert(user2);
        
        UserProfileTmpHolder nu = new UserProfileTmpHolder();
        nu.setUserOid(user1.getUserOid());
        nu.setUserName(NEW_USER_NAME);
        
        userProfileService.updateByPrimaryKeySelective(user1, nu);
        
        UserProfileHolder u = userProfileService.selectUserProfileByKey(user1.getUserOid());
        assertNotNull(u);
        assertEquals(u.getUserName(), nu.getUserName());
        assertEquals(u.getLoginId(), user1.getLoginId());

        nu = new UserProfileTmpHolder();
        BeanUtils.copyProperties(user2, nu);
        nu.setLoginId("newloginid");
        nu.setUserName(NEW_USER_NAME);
        
        userProfileService.updateByPrimaryKey(user2, nu);
        
        u = userProfileService.selectUserProfileByKey(user2.getUserOid());
        assertNotNull(u);
        assertEquals(u.getUserName(), nu.getUserName());
        assertEquals(u.getLoginId(), nu.getLoginId());
        
        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user1.getUserOid());
        userProfileService.delete(userParam);
        UserProfileHolder userParam2 = new UserProfileHolder();
        userParam2.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam2);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    
    }
    
    
    public void testMkCreate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);

        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        userProfileService.mkCreate(cp, user1);
        userProfileService.mkCreate(cp, user2);
        
        UserProfileTmpHolder tu = userProfileTmpService.selectUserProfileTmpByKey(user1.getUserOid());
        
        assertNotNull(tu);
        assertEquals(tu.getActor(), cp.getLoginId());
        assertEquals(tu.getUserName(), user1.getUserName());
        assertEquals(tu.getActionType(), DbActionType.CREATE);
        assertEquals(tu.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        tu = userProfileTmpService.selectUserProfileTmpByLoginId(user2.getLoginId());

        assertNotNull(tu);
        assertEquals(tu.getActor(), cp.getLoginId());
        assertEquals(tu.getUserName(), user2.getUserName());
        assertEquals(tu.getActionType(), DbActionType.CREATE);
        assertEquals(tu.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        userProfileTmpService.delete(user1);
        userProfileTmpService.delete(user2);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testMkUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        userProfileService.mkCreate(cp, user2);
        UserProfileTmpHolder tu = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        
        assertNotNull(tu);
        assertEquals(tu.getActor(), cp.getLoginId());
        assertEquals(tu.getUserName(), user2.getUserName());
        assertEquals(tu.getActionType(), DbActionType.CREATE);
        assertEquals(tu.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        UserProfileTmpHolder newu = tu;
        newu.setLoginId("newloginid");
        newu.setUserName(NEW_USER_NAME);
        newu.setUpdateDate(new Date());
        
        userProfileService.mkUpdate(cp, tu, newu);
        
        tu = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        assertNotNull(tu);
        assertEquals(tu.getActor(), cp.getLoginId());
        assertEquals(tu.getUserName(), newu.getUserName());
        assertEquals(tu.getLoginId(), newu.getLoginId());
        assertEquals(tu.getActionType(), DbActionType.CREATE);
        assertEquals(tu.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        newu.setCtrlStatus(MkCtrlStatus.APPROVED);
        userProfileService.mkUpdate(cp, tu, newu);
        
        tu = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        assertNotNull(tu);
        assertEquals(tu.getActor(), cp.getLoginId());
        assertEquals(tu.getLoginId(), newu.getLoginId());
        assertEquals(tu.getActionType(), DbActionType.UPDATE);
        assertEquals(tu.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        UserProfileTmpHolder userParam = new UserProfileTmpHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userParam);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    
    }
    

    public void testMkDelete() throws Exception
    {
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        userProfileService.mkCreate(cp, user1);
        userProfileService.mkDelete(cp, user1);
        
        UserProfileTmpHolder tu = userProfileTmpService.selectUserProfileTmpByKey(user1.getUserOid());
        
        assertNotNull(tu);
        assertEquals(tu.getActor(), cp.getLoginId());
        assertEquals(tu.getUserName(), user1.getUserName());
        assertEquals(tu.getActionType(), DbActionType.DELETE);
        assertEquals(tu.getCtrlStatus(), MkCtrlStatus.PENDING);
        
        UserProfileTmpHolder userParam = new UserProfileTmpHolder();
        userParam.setUserOid(user1.getUserOid());
        userProfileTmpService.delete(userParam);
        
    }
    

    public void testMkApproveCreate() throws Exception
    {
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        groupService.createGroupProfile(this.getMkCommonParameter(false), group);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role1);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role2);
        List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
        ruList.add(roleUserTmp1);
        ruList.add(roleUserTmp2);
        user2.setRoleUsers(ruList);
        
        List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
        guList.add(groupUserTmp);
        user2.setGroupUsers(guList);
        
        userProfileService.mkCreate(cp, user2);
        userProfileService.mkApprove(cp, null, user2);
        
        UserProfileTmpHolder userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        List<? extends Object> tmpRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(user2.getUserOid());
        List<? extends Object> roleUserList = roleUserService.selectRoleUserByUserOid(user2.getUserOid());
        
        List<? extends Object> tmpGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(user2.getUserOid());
        List<? extends Object> groupUserList = groupUserService.selectGroupUserByUserOid(user2.getUserOid());
        
        assertEquals(tmpGroupUserList.size(), groupUserList.size());
        assertEquals(tmpRoleUserList.size(), roleUserList.size());
        assertNotNull(user);
        assertNotNull(userTmp);
        assertEquals(user.getLoginId(), userTmp.getLoginId());
        assertEquals(user.getCreateDate(), userTmp.getCreateDate());
        assertEquals(userTmp.getActionType(), DbActionType.CREATE);
        assertEquals(userTmp.getCtrlStatus(), MkCtrlStatus.APPROVED);
        
        GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
        groupUser.setGroupOid(groupUserTmp.getGroupOid());
        
        groupUserMapper.delete(groupUser);
        groupUserTmpMapper.delete(groupUser);
        
        RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
        ruTmpParam.setUserOid(user2.getUserOid());

        roleUserMapper.delete(ruTmpParam);
        roleUserTmpMapper.delete(ruTmpParam);

        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam);
        UserProfileTmpHolder userTmpParam = new UserProfileTmpHolder();
        userTmpParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userTmpParam);
        
        GroupHolder groupParam = new GroupHolder();
        groupParam.setGroupOid(group.getGroupOid());
        groupService.delete(groupParam);
        
        GroupTmpHolder groupTmpParam = new GroupTmpHolder();
        groupTmpParam.setGroupOid(group.getGroupOid());
        groupTmpMapper.delete(groupTmpParam);

        roleService.delete(role1);
        roleService.delete(role2);
        
        RoleTmpHolder roleTmp1Param = new RoleTmpHolder();
        roleTmp1Param.setRoleOid(role1.getRoleOid());
        roleTmpService.delete(roleTmp1Param);
        
        RoleTmpHolder roleTmp2Param = new RoleTmpHolder();
        roleTmp2Param.setRoleOid(role2.getRoleOid());
        roleTmpService.delete(roleTmp2Param);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }
    
    
    public void testMkApproveUpdate() throws Exception
    {
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        groupService.createGroupProfile(this.getMkCommonParameter(false), group);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role1);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role2);
        List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
        ruList.add(roleUserTmp1);
        ruList.add(roleUserTmp2);
        user2.setRoleUsers(ruList);
        
        List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
        guList.add(groupUserTmp);
        user2.setGroupUsers(guList);
        
        userProfileService.mkCreate(cp, user2);
        userProfileService.mkApprove(cp, null, user2);
        
        UserProfileTmpHolder oldUserTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileTmpHolder newUserTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        
        oldUserTmp.setGroupUsers(new ArrayList<GroupUserHolder>());
        oldUserTmp.setRoleUsers(new ArrayList<RoleUserHolder>());
        newUserTmp.setGroupUsers(new ArrayList<GroupUserHolder>());
        newUserTmp.setRoleUsers(new ArrayList<RoleUserHolder>());
        
        List<? extends Object> oldGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(user2.getUserOid());
        for (Object oldGroupUser : oldGroupUserList)
        {
            oldUserTmp.getGroupUsers().add((GroupUserHolder) oldGroupUser);
            newUserTmp.getGroupUsers().add((GroupUserHolder) oldGroupUser);
        }

        List<? extends Object> oldRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(user2.getUserOid());
        for (Object oldRoleUser : oldRoleUserList)
        {
            oldUserTmp.getRoleUsers().add((RoleUserHolder) oldRoleUser);
            newUserTmp.getRoleUsers().add((RoleUserHolder) oldRoleUser);
        }
        
        newUserTmp.setUserName(NEW_USER_NAME);
        newUserTmp.getRoleUsers().remove(0);
        newUserTmp.setUpdateDate(new Date());
        
        userProfileService.mkUpdate(cp, oldUserTmp, newUserTmp);
        userProfileService.mkApprove(cp, oldUserTmp, newUserTmp);
        
        List<? extends Object> newGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(user2.getUserOid());
        List<? extends Object> newRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(user2.getUserOid());
        
        UserProfileTmpHolder userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        assertEquals(newGroupUserList.size(), oldGroupUserList.size());
        assertEquals(newRoleUserList.size(), oldRoleUserList.size() -1);
        assertNotNull(user);
        assertNotNull(userTmp);
        assertEquals(user.getUserName(), NEW_USER_NAME);
        assertEquals(userTmp.getUserName(), NEW_USER_NAME);
        assertEquals(user.getCreateDate(), userTmp.getCreateDate());
        assertEquals(userTmp.getActionType(), DbActionType.UPDATE);
        assertEquals(userTmp.getCtrlStatus(), MkCtrlStatus.APPROVED);
        
        GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
        groupUser.setGroupOid(groupUserTmp.getGroupOid());
        
        groupUserMapper.delete(groupUser);
        groupUserTmpMapper.delete(groupUser);
        
        RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
        ruTmpParam.setUserOid(user2.getUserOid());
        
        roleUserMapper.delete(ruTmpParam);
        roleUserTmpMapper.delete(ruTmpParam);
        
        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam);
        UserProfileTmpHolder userTmpParam = new UserProfileTmpHolder();
        userTmpParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userTmpParam);
        
        GroupHolder groupParam = new GroupHolder();
        groupParam.setGroupOid(group.getGroupOid());
        groupService.delete(groupParam);
        
        GroupTmpHolder groupTmpParam = new GroupTmpHolder();
        groupTmpParam.setGroupOid(group.getGroupOid());
        groupTmpMapper.delete(groupTmpParam);
        
        roleService.delete(role1);
        roleService.delete(role2);
        
        RoleTmpHolder roleTmp1Param = new RoleTmpHolder();
        roleTmp1Param.setRoleOid(role1.getRoleOid());
        roleTmpService.delete(roleTmp1Param);
        
        RoleTmpHolder roleTmp2Param = new RoleTmpHolder();
        roleTmp2Param.setRoleOid(role2.getRoleOid());
        roleTmpService.delete(roleTmp2Param);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }


    public void testMkApproveDelete() throws Exception
    {
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        groupService.createGroupProfile(this.getMkCommonParameter(false), group);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role1);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role2);
        List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
        ruList.add(roleUserTmp1);
        ruList.add(roleUserTmp2);
        user2.setRoleUsers(ruList);
        
        List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
        guList.add(groupUserTmp);
        user2.setGroupUsers(guList);
        
        userProfileService.mkCreate(cp, user2);
        userProfileService.mkApprove(cp, null, user2);
        
        UserProfileTmpHolder userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        userProfileService.mkDelete(cp, userTmp);
        userProfileService.mkApprove(cp, user, userTmp);
        
        userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        assertNull(user);
        assertNull(userTmp);
        
        GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
        groupUser.setGroupOid(groupUserTmp.getGroupOid());
        
        groupUserMapper.delete(groupUser);
        groupUserTmpMapper.delete(groupUser);
        
        RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
        ruTmpParam.setUserOid(user2.getUserOid());
        
        roleUserMapper.delete(ruTmpParam);
        roleUserTmpMapper.delete(ruTmpParam);
        
        GroupHolder groupParam = new GroupHolder();
        groupParam.setGroupOid(group.getGroupOid());
        groupService.delete(groupParam);
        
        GroupTmpHolder groupTmpParam = new GroupTmpHolder();
        groupTmpParam.setGroupOid(group.getGroupOid());
        groupTmpMapper.delete(groupTmpParam);
        
        roleService.delete(role1);
        roleService.delete(role2);
        
        RoleTmpHolder roleTmp1Param = new RoleTmpHolder();
        roleTmp1Param.setRoleOid(role1.getRoleOid());
        roleTmpService.delete(roleTmp1Param);
        
        RoleTmpHolder roleTmp2Param = new RoleTmpHolder();
        roleTmp2Param.setRoleOid(role2.getRoleOid());
        roleTmpService.delete(roleTmp2Param);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }

    
    public void testMkRejectCreate() throws Exception
    {
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        userProfileService.mkCreate(cp, user1);
        userProfileService.mkReject(cp, null, user1);
        
        UserProfileTmpHolder tu = userProfileTmpService.selectUserProfileTmpByKey(user1.getUserOid());
        
        assertNull(tu);
    }
    

    public void testMkRejectUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        userProfileService.mkCreate(cp, user2);
        userProfileService.mkApprove(cp, null, user2);
        
        UserProfileTmpHolder oldUserTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileTmpHolder newUserTmp = oldUserTmp;

        newUserTmp.setUserName(NEW_USER_NAME);
        newUserTmp.setUpdateDate(new Date());
        
        UserProfileHolder mainUesr = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        userProfileService.mkUpdate(cp, oldUserTmp, newUserTmp);
        userProfileService.mkReject(cp, mainUesr, newUserTmp);
        
        newUserTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        assertEquals(mainUesr.getUserName(), newUserTmp.getUserName());
        assertTrue(!newUserTmp.getUserName().equalsIgnoreCase(NEW_USER_NAME));
        assertEquals(newUserTmp.getActionType(), DbActionType.UPDATE);
        assertEquals(newUserTmp.getCtrlStatus(), MkCtrlStatus.REJECTED);

        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam);
        UserProfileTmpHolder userTmpParam = new UserProfileTmpHolder();
        userTmpParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userTmpParam);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    
    }
    

    public void testMkRejectDelete() throws Exception
    {
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        groupService.createGroupProfile(this.getMkCommonParameter(false), group);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role1);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role2);
        List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
        ruList.add(roleUserTmp1);
        ruList.add(roleUserTmp2);
        user2.setRoleUsers(ruList);
        
        List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
        guList.add(groupUserTmp);
        user2.setGroupUsers(guList);
        
        userProfileService.mkCreate(cp, user2);
        userProfileService.mkApprove(cp, null, user2);
        
        UserProfileTmpHolder userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        userProfileService.mkDelete(cp, userTmp);
        userProfileService.mkReject(cp, user, userTmp);
        
        userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        
        assertNotNull(user);
        assertEquals(userTmp.getActionType(), DbActionType.DELETE);
        assertEquals(userTmp.getCtrlStatus(), MkCtrlStatus.REJECTED);
        
        GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
        groupUser.setGroupOid(groupUserTmp.getGroupOid());
        
        groupUserMapper.delete(groupUser);
        groupUserTmpMapper.delete(groupUser);
        
        RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
        ruTmpParam.setUserOid(user2.getUserOid());
        
        roleUserMapper.delete(ruTmpParam);
        roleUserTmpMapper.delete(ruTmpParam);
        
        GroupHolder groupParam = new GroupHolder();
        groupParam.setGroupOid(group.getGroupOid());
        groupService.delete(groupParam);
        
        GroupTmpHolder groupTmpParam = new GroupTmpHolder();
        groupTmpParam.setGroupOid(group.getGroupOid());
        groupTmpMapper.delete(groupTmpParam);
        
        roleService.delete(role1);
        roleService.delete(role2);
        
        RoleTmpHolder roleTmp1Param = new RoleTmpHolder();
        roleTmp1Param.setRoleOid(role1.getRoleOid());
        roleTmpService.delete(roleTmp1Param);
        
        RoleTmpHolder roleTmp2Param = new RoleTmpHolder();
        roleTmp2Param.setRoleOid(role2.getRoleOid());
        roleTmpService.delete(roleTmp2Param);
        
        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam);
        UserProfileTmpHolder userTmpParam = new UserProfileTmpHolder();
        userTmpParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userTmpParam);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }

    
    public void testMkWithdrawCreate() throws Exception
    {
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        userProfileService.mkCreate(cp, user1);
        userProfileService.mkWithdraw(cp, null, user1);
        
        UserProfileTmpHolder tu = userProfileTmpService.selectUserProfileTmpByKey(user1.getUserOid());
        
        assertNull(tu);
    }
    
    
    public void testMkWithdrawUpdate() throws Exception
    {
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        userProfileService.mkCreate(cp, user2);
        userProfileService.mkApprove(cp, null, user2);
        
        UserProfileTmpHolder oldUserTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileTmpHolder newUserTmp = oldUserTmp;

        newUserTmp.setUserName(NEW_USER_NAME);
        newUserTmp.setUpdateDate(new Date());
        
        UserProfileHolder mainUesr = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        userProfileService.mkUpdate(cp, oldUserTmp, newUserTmp);
        userProfileService.mkWithdraw(cp, mainUesr, newUserTmp);
        
        newUserTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        assertEquals(mainUesr.getUserName(), newUserTmp.getUserName());
        assertTrue(!newUserTmp.getUserName().equalsIgnoreCase(NEW_USER_NAME));
        assertEquals(newUserTmp.getActionType(), DbActionType.UPDATE);
        assertEquals(newUserTmp.getCtrlStatus(), MkCtrlStatus.WITHDRAWN);

        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam);
        UserProfileTmpHolder userTmpParam = new UserProfileTmpHolder();
        userTmpParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userTmpParam);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    
    }
    

    public void testMkWithdrawDelete() throws Exception
    {
        CommonParameterHolder cp = this.getMkCommonParameter(true);
        
        buyerService.insert(buyer);
        supplierService.insert(supplier);
        groupService.createGroupProfile(this.getMkCommonParameter(false), group);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role1);
        roleService.createRoleProfile(this.getMkCommonParameter(false), role2);
        List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
        ruList.add(roleUserTmp1);
        ruList.add(roleUserTmp2);
        user2.setRoleUsers(ruList);
        
        List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
        guList.add(groupUserTmp);
        user2.setGroupUsers(guList);
        
        userProfileService.mkCreate(cp, user2);
        userProfileService.mkApprove(cp, null, user2);
        
        UserProfileTmpHolder userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        UserProfileHolder user = userProfileService.selectUserProfileByKey(user2.getUserOid());
        
        userProfileService.mkDelete(cp, userTmp);
        userProfileService.mkWithdraw(cp, user, userTmp);
        
        userTmp = userProfileTmpService.selectUserProfileTmpByKey(user2.getUserOid());
        
        assertNotNull(user);
        assertEquals(userTmp.getActionType(), DbActionType.DELETE);
        assertEquals(userTmp.getCtrlStatus(), MkCtrlStatus.WITHDRAWN);
        
        GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
        groupUser.setGroupOid(groupUserTmp.getGroupOid());
        
        groupUserMapper.delete(groupUser);
        groupUserTmpMapper.delete(groupUser);
        
        RoleUserTmpHolder ruTmpParam = new RoleUserTmpHolder();
        ruTmpParam.setUserOid(user2.getUserOid());
        
        roleUserMapper.delete(ruTmpParam);
        roleUserTmpMapper.delete(ruTmpParam);
        
        GroupHolder groupParam = new GroupHolder();
        groupParam.setGroupOid(group.getGroupOid());
        groupService.delete(groupParam);
        
        GroupTmpHolder groupTmpParam = new GroupTmpHolder();
        groupTmpParam.setGroupOid(group.getGroupOid());
        groupTmpMapper.delete(groupTmpParam);
        
        roleService.delete(role1);
        roleService.delete(role2);
        
        RoleTmpHolder roleTmp1Param = new RoleTmpHolder();
        roleTmp1Param.setRoleOid(role1.getRoleOid());
        roleTmpService.delete(roleTmp1Param);
        
        RoleTmpHolder roleTmp2Param = new RoleTmpHolder();
        roleTmp2Param.setRoleOid(role2.getRoleOid());
        roleTmpService.delete(roleTmp2Param);
        
        UserProfileHolder userParam = new UserProfileHolder();
        userParam.setUserOid(user2.getUserOid());
        userProfileService.delete(userParam);
        UserProfileTmpHolder userTmpParam = new UserProfileTmpHolder();
        userTmpParam.setUserOid(user2.getUserOid());
        userProfileTmpService.delete(userTmpParam);
        
        buyerService.delete(buyer);
        supplierService.delete(supplier);
    }

    
    private CommonParameterHolder getMkCommonParameter(boolean flag)
    {
        CommonParameterHolder commonParam = new CommonParameterHolder();
        commonParam.setLoginId(LOGIN_ID);
        commonParam.setClientIp(IP);
        commonParam.setCurrentUserOid(user1.getUserOid());
        commonParam.setMkMode(flag);
        
        return commonParam;
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
        
        
        role1 = new RoleTmpHolder();
        role1.setRoleOid(oidService.getOid());
        role1.setRoleId("ROLE1");
        role1.setRoleName("Role1 Name");
        role1.setRoleType(RoleType.ADMIN);
        role1.setCreateDate(new Date());
        role1.setCreateBy(LOGIN_ID);
        role1.setUserTypeOid(BigDecimal.valueOf(2));
        
        role2 = new RoleTmpHolder();
        role2.setRoleOid(oidService.getOid());
        role2.setRoleId("ROLE2");
        role2.setRoleName("Role2 Name");
        role2.setRoleType(RoleType.BUYER);
        role2.setCreateDate(new Date());
        role2.setCreateBy(LOGIN_ID);
        role2.setUserTypeOid(BigDecimal.valueOf(2));
        
        group = new GroupTmpHolder();
        group.setGroupOid(oidService.getOid());
        group.setGroupId("testGroupId");
        group.setGroupType(GroupType.BUYER);
        group.setGroupName("testGroupName");
        group.setUserTypeOid(BigDecimal.valueOf(2));
        group.setBuyerOid(buyer.getBuyerOid());
        group.setCreateBy(LOGIN_ID);
        group.setCreateDate(new Date());
        
        user1 = new UserProfileTmpHolder();
        user1.setUserOid(oidService.getOid());
        user1.setLoginId("GE");
        user1.setUserName("Ge");
        user1.setLoginMode("PASSWORD");
        user1.setEmail("jkge@pracbiz.com");
        user1.setGender("1");
        user1.setActive(true);
        user1.setBlocked(false);
        user1.setInit(false);
        user1.setCreateDate(new Date());
        user1.setCreateBy(LOGIN_ID);
        user1.setUserType(BigDecimal.ONE);
        
        user2 = new UserProfileTmpHolder();
        user2.setUserOid(oidService.getOid());
        user2.setLoginId("JIAN");
        user2.setUserName("Jian");
        user2.setLoginMode("PASSWORD");
        user2.setEmail("jkge@pracbiz.com");
        user2.setGender("1");
        user2.setActive(true);
        user2.setBlocked(false);
        user2.setInit(false);
        user2.setCreateDate(new Date());
        user2.setCreateBy(LOGIN_ID);
        user2.setBuyerOid(buyer.getBuyerOid());
        user2.setUserType(BigDecimal.valueOf(2));
        
        user3 = new UserProfileTmpHolder();
        user3.setUserOid(oidService.getOid());
        user3.setLoginId("KUI");
        user3.setUserName("Kui");
        user3.setLoginMode("PASSWORD");
        user3.setEmail("jkge@pracbiz.com");
        user3.setGender("0");
        user3.setActive(true);
        user3.setBlocked(false);
        user3.setInit(false);
        user3.setCreateDate(new Date());
        user3.setCreateBy(LOGIN_ID);
        user1.setSupplierOid(supplier.getSupplierOid());
        user3.setUserType(BigDecimal.valueOf(3));
        
        roleUserTmp1 = new RoleUserTmpHolder();
        roleUserTmp1.setUserOid(user2.getUserOid());
        roleUserTmp1.setRoleOid(role1.getRoleOid());
      
        roleUserTmp2 = new RoleUserTmpHolder();
        roleUserTmp2.setUserOid(user2.getUserOid());
        roleUserTmp2.setRoleOid(role2.getRoleOid());
        
        groupUserTmp = new GroupUserTmpHolder();
        groupUserTmp.setGroupOid(group.getGroupOid());
        groupUserTmp.setUserOid(user2.getUserOid());
    }
    
}
