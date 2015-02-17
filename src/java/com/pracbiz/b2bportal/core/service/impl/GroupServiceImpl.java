package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.GroupType;
import com.pracbiz.b2bportal.core.constants.LastUpdateFrom;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
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
import com.pracbiz.b2bportal.core.holder.extension.GroupTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.GroupMapper;
import com.pracbiz.b2bportal.core.mapper.GroupSupplierMapper;
import com.pracbiz.b2bportal.core.mapper.GroupSupplierTmpMapper;
import com.pracbiz.b2bportal.core.mapper.GroupTmpMapper;
import com.pracbiz.b2bportal.core.mapper.GroupTradingPartnerMapper;
import com.pracbiz.b2bportal.core.mapper.GroupTradingPartnerTmpMapper;
import com.pracbiz.b2bportal.core.mapper.GroupUserMapper;
import com.pracbiz.b2bportal.core.mapper.GroupUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.RoleGroupMapper;
import com.pracbiz.b2bportal.core.mapper.RoleGroupTmpMapper;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.RoleGroupService;

public class GroupServiceImpl extends DBActionServiceDefaultImpl<GroupHolder>
    implements GroupService, ApplicationContextAware
{
    private ApplicationContext ctx;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupTmpMapper groupTmpMapper;
    @Autowired
    private RoleGroupMapper roleGroupMapper;
    @Autowired
    private RoleGroupTmpMapper roleGroupTmpMapper;
    @Autowired
    private GroupUserMapper groupUserMapper;
    @Autowired
    private GroupUserTmpMapper groupUserTmpMapper;
    @Autowired
    private GroupSupplierMapper groupSupplierMapper;
    @Autowired
    private GroupSupplierTmpMapper groupSupplierTmpMapper;
    @Autowired
    private GroupTradingPartnerMapper groupTradingPartnerMapper;
    @Autowired
    private GroupTradingPartnerTmpMapper groupTradingPartnerTmpMapper;
    @Autowired
    private RoleGroupService roleGroupService;
    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private GroupTradingPartnerService groupTradingPartnerService;
    @Autowired
    private GroupSupplierService groupSupplierService;
    
    private static final String DELETE_GROUP_USERS = "DELETE";
    private static final String INSERT_GROUP_USERS = "INSERT";
    private static final String UPDATE_GROUP_USERS = "UPDATE";

    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private GroupService getMeBean()
    {
        return ctx.getBean("groupService", GroupService.class);
    }
    
    @Override
    public List<GroupHolder> select(GroupHolder param) throws Exception
    {
        return groupMapper.select(param);
    }

    
    @Override
    public void insert(GroupHolder newObj_) throws Exception
    {
        groupMapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(GroupHolder oldObj_,
        GroupHolder newObj_) throws Exception
    {
        groupMapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(GroupHolder oldObj_, GroupHolder newObj_)
        throws Exception
    {
        groupMapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(GroupHolder oldObj_) throws Exception
    {
        groupMapper.delete(oldObj_);
    }


    @Override
    public void mkCreate(CommonParameterHolder cp, GroupTmpHolder newObj_)
        throws Exception
    {
        GroupTmpHolder group = newObj_;
        group.setActor(cp.getLoginId());
        group.setActionType(DbActionType.CREATE);
        group.setActionDate(group.getCreateDate());
        group.setCtrlStatus(MkCtrlStatus.PENDING);

        groupTmpMapper.insert(group);
        
        List<? extends RoleGroupHolder> roleGroups = group.getRoleGroups();
        if (roleGroups != null)
        {
            for (RoleGroupHolder roleGroup : roleGroups)
            {
                roleGroupTmpMapper.insert((RoleGroupTmpHolder)roleGroup);
            }
        }
        
        List<? extends GroupUserHolder> groupUsers = group.getGroupUsers();
        if (groupUsers != null)
        {
            for (GroupUserHolder groupUser : groupUsers)
            {
                groupUserTmpMapper.insert((GroupUserTmpHolder)groupUser);
            }
        }
        
        List<? extends GroupSupplierHolder> groupSuppliers = group.getGroupSuppliers();
        if (groupSuppliers != null)
        {
            for (GroupSupplierHolder groupSupplier : groupSuppliers)
            {
                groupSupplierTmpMapper.insert((GroupSupplierTmpHolder)groupSupplier);
            }
        }
        
        List<? extends GroupTradingPartnerHolder> groupTradingPartners = group.getGroupTradingPartners();
        if (groupTradingPartners != null)
        {
            for (GroupTradingPartnerHolder groupTradingPartner : groupTradingPartners)
            {
                groupTradingPartnerTmpMapper.insert((GroupTradingPartnerTmpHolder)groupTradingPartner);
            }
        }
    }


    @Override
    public void mkUpdate(CommonParameterHolder cp, GroupTmpHolder oldObj_,
        GroupTmpHolder newObj_) throws Exception
    {
        GroupTmpHolder group = newObj_;
        group.setActor(cp.getLoginId());
        group.setActionDate(group.getUpdateDate());
        if(!(DbActionType.CREATE.equals(group.getActionType())
            && MkCtrlStatus.PENDING.equals(group.getCtrlStatus())))
        {
            group.setActionType(DbActionType.UPDATE);
        }
        
        group.setCtrlStatus(MkCtrlStatus.PENDING);
        
        groupTmpMapper.updateByPrimaryKey(group);
        
        BigDecimal groupOid = group.getGroupOid();
        RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
        roleGroup.setGroupOid(groupOid);
        
        roleGroupTmpMapper.delete(roleGroup);
        
        Map<String,List<GroupUserHolder>> map = this.splitGroupUsers(group, (GroupTmpHolder)oldObj_);
        List<GroupUserHolder> deleteGroupUsers = map.get(DELETE_GROUP_USERS);
        
        if (deleteGroupUsers != null)
        {
            for (GroupUserHolder groupUser : deleteGroupUsers)
            {
                GroupUserTmpHolder groupUserTmp = (GroupUserTmpHolder)groupUser;
                groupUserTmp.setActionType(DbActionType.DELETE);
                groupUserTmp.setLastUpdateFrom(LastUpdateFrom.GROUP);
                groupUserTmp.setApproved(false);
                groupUserTmpMapper.updateByPrimaryKey(groupUserTmp);
            }
        }
        
        List<GroupUserHolder> updateGroupUsers = map.get(UPDATE_GROUP_USERS);
        
        if (updateGroupUsers != null)
        {
            for (GroupUserHolder groupUser : updateGroupUsers)
            {
                GroupUserTmpHolder groupUserTmp = (GroupUserTmpHolder)groupUser;
                groupUserTmp.setActionType(DbActionType.CREATE);
                groupUserTmp.setLastUpdateFrom(LastUpdateFrom.GROUP);
                groupUserTmp.setApproved(false);
                groupUserTmpMapper.updateByPrimaryKey(groupUserTmp);
            }
        }
        
        GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
        groupSupplier.setGroupOid(groupOid);
        
        groupSupplierTmpMapper.delete(groupSupplier);
        
        GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder();
        groupTradingPartner.setGroupOid(groupOid);
        
        groupTradingPartnerTmpMapper.delete(groupTradingPartner);
    
        
        List<? extends RoleGroupHolder> roleGroups = group.getRoleGroups();
        if (roleGroups != null)
        {
            for (RoleGroupHolder role : roleGroups)
            {
                roleGroupTmpMapper.insert((RoleGroupTmpHolder)role);
            }
        }
        
        List<GroupUserHolder> inserts = map.get(INSERT_GROUP_USERS);
        if (inserts != null)
        {
            for (GroupUserHolder user : inserts)
            {
                GroupUserTmpHolder groupUserTmp = (GroupUserTmpHolder)user;
                groupUserTmp.setApproved(false);
                groupUserTmp.setActionType(DbActionType.CREATE);
                groupUserTmp.setLastUpdateFrom(LastUpdateFrom.GROUP);
                groupUserTmpMapper.insert(groupUserTmp);
            }
        }
        
        
        List<? extends GroupSupplierHolder> groupSuppliers = group.getGroupSuppliers();
        if (groupSuppliers != null)
        {
            for (GroupSupplierHolder supplier : groupSuppliers)
            {
                groupSupplierTmpMapper.insert((GroupSupplierTmpHolder)supplier);
            }
        }
        
        List<? extends GroupTradingPartnerHolder> groupTradingPartners = group.getGroupTradingPartners();
        if (groupTradingPartners != null)
        {
            for (GroupTradingPartnerHolder tradingPartner : groupTradingPartners)
            {
                groupTradingPartnerTmpMapper.insert((GroupTradingPartnerTmpHolder)tradingPartner);
            }
        }
    }


    @Override
    public void mkDelete(CommonParameterHolder cp, GroupTmpHolder oldObj_)
        throws Exception
    {
        GroupTmpHolder group = oldObj_;
        group.setActor(cp.getLoginId());
        group.setActionType(DbActionType.DELETE);
        group.setActionDate(group.getCreateDate());
        group.setCtrlStatus(MkCtrlStatus.PENDING);
        
        List<? extends GroupUserHolder> users = group.getGroupUsers();
        if (users != null)
        {
            for (GroupUserHolder groupUser : users)
            {
                GroupUserTmpHolder update = (GroupUserTmpHolder)groupUser;
                update.setApproved(false);
                update.setActionType(DbActionType.DELETE);
                update.setLastUpdateFrom(LastUpdateFrom.GROUP);
                groupUserTmpMapper.updateByPrimaryKey(update);
            }
        }
        
        groupTmpMapper.updateByPrimaryKeySelective((GroupTmpHolder) oldObj_);
    }


    @Override
    public void mkApprove(CommonParameterHolder cp, GroupHolder main,
        GroupTmpHolder tmp) throws Exception
    {
        GroupTmpHolder tmpGroup = tmp;
        GroupHolder mainGroup = main;
        
        DbActionType actionType = tmpGroup.getActionType();
        if (DbActionType.CREATE.equals(actionType))
        {
            tmpGroup.setCtrlStatus(MkCtrlStatus.APPROVED);
            tmpGroup.setActor(cp.getLoginId());
            tmpGroup.setActionDate(new Date());
            
            groupTmpMapper.updateByPrimaryKey(tmpGroup);
            this.insert(tmpGroup);
            
            List<? extends RoleGroupHolder> roleGroups = tmpGroup.getRoleGroups();
            if (roleGroups != null)
            {
                for (RoleGroupHolder roleGroup : roleGroups)
                {
                    roleGroupMapper.insert(roleGroup);
                }
            }
            
            List<? extends GroupUserHolder> groupUsers = tmpGroup.getGroupUsers();
            BigDecimal groupOid = tmpGroup.getGroupOid();
            
            for (Iterator<? extends GroupUserHolder> it = groupUsers.iterator(); it.hasNext();)
            {
                GroupUserTmpHolder groupTmp = (GroupUserTmpHolder)it.next();
                if (DbActionType.DELETE.equals(groupTmp.getActionType()))
                {
                    GroupUserTmpHolder delete = new GroupUserTmpHolder();
                    delete.setGroupOid(groupOid);
                    delete.setUserOid(groupTmp.getUserOid());
                    groupUserTmpMapper.delete(delete);
                    it.remove();
                }
            }
            
            for (GroupUserHolder groupUser : groupUsers)
            {
                groupUserMapper.insert(groupUser);
                GroupUserTmpHolder groupUserTmp = (GroupUserTmpHolder)groupUser;
                groupUserTmp.setApproved(true);
                groupUserTmpMapper.updateByPrimaryKey(groupUserTmp);
            }
            
            List<? extends GroupSupplierHolder> groupSuppliers = tmpGroup.getGroupSuppliers();
            if (groupSuppliers != null)
            {
                for (GroupSupplierHolder groupSupplier : groupSuppliers)
                {
                    groupSupplierMapper.insert(groupSupplier);
                }
            }
            
            List<? extends GroupTradingPartnerHolder> groupTradingPartners = tmpGroup.getGroupTradingPartners();
            if (groupTradingPartners != null)
            {
                for (GroupTradingPartnerHolder groupTradingPartner : groupTradingPartners)
                {
                    groupTradingPartnerMapper.insert(groupTradingPartner);
                }
            }
            
            return;
        }
        
        if (DbActionType.UPDATE.equals(actionType))
        {
            tmpGroup.setCtrlStatus(MkCtrlStatus.APPROVED);
            tmpGroup.setActor(cp.getLoginId());
            tmpGroup.setActionDate(new Date());
            
            groupTmpMapper.updateByPrimaryKey(tmpGroup);
            this.updateByPrimaryKey(mainGroup, tmpGroup);
            
            BigDecimal groupOid = tmpGroup.getGroupOid();
            RoleGroupHolder roleGroup = new RoleGroupHolder();
            roleGroup.setGroupOid(groupOid);
            
            roleGroupMapper.delete(roleGroup);
            
            BigDecimal oldCompanyOid = this.getCompanyOid(mainGroup);
            BigDecimal newCompanyOid = this.getCompanyOid(tmpGroup);
            
            List<? extends GroupUserHolder> groupUsers = tmpGroup.getGroupUsers();
            
            if (oldCompanyOid.toString().equals(newCompanyOid.toString()))
            {
                handleApproveGroupUsers(groupUsers, groupOid);
            }
            else
            {
                GroupUserTmpHolder delete = new GroupUserTmpHolder();
                delete.setGroupOid(groupOid);
                groupUserMapper.delete(delete);
                
                for (Iterator<? extends GroupUserHolder> it = groupUsers.iterator(); it.hasNext();)
                {
                    GroupUserTmpHolder groupTmp = (GroupUserTmpHolder)it.next();
                    if (!groupTmp.getApproved()
                        && DbActionType.DELETE.equals(groupTmp.getActionType()))
                    {
                        delete.setUserOid(groupTmp.getUserOid());
                        groupUserTmpMapper.delete(delete);
                        it.remove();
                        continue;
                    }
                    groupTmp.setApproved(true);
                    groupUserTmpMapper.updateByPrimaryKey(groupTmp);
                }
                
                for (GroupUserHolder group : groupUsers)
                {
                    groupUserMapper.insert(group);
                }
            }
            
            GroupSupplierHolder groupSupplier = new GroupSupplierHolder();
            groupSupplier.setGroupOid(groupOid);
            
            groupSupplierMapper.delete(groupSupplier);
            
            GroupTradingPartnerHolder groupTradingPartner = new GroupTradingPartnerHolder();
            groupTradingPartner.setGroupOid(groupOid);
            
            groupTradingPartnerMapper.delete(groupTradingPartner);
        
            
            List<? extends RoleGroupHolder> roleGroups = tmpGroup.getRoleGroups();
            if (roleGroups != null)
            {
                for (RoleGroupHolder role : roleGroups)
                {
                    roleGroupMapper.insert(role);
                }
            }
            
            
            List<? extends GroupSupplierHolder> groupSuppliers = tmpGroup.getGroupSuppliers();
            if (groupSuppliers != null)
            {
                for (GroupSupplierHolder supplier : groupSuppliers)
                {
                    groupSupplierMapper.insert(supplier);
                }
            }
            
            List<? extends GroupTradingPartnerHolder> groupTradingPartners = tmpGroup.getGroupTradingPartners();
            if (groupTradingPartners != null)
            {
                for (GroupTradingPartnerHolder tradingPartner : groupTradingPartners)
                {
                    groupTradingPartnerMapper.insert(tradingPartner);
                }
            }
            return;
        }
        
        if (DbActionType.DELETE.equals(actionType))
        {
            BigDecimal groupOid = tmpGroup.getGroupOid();
            RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
            roleGroup.setGroupOid(groupOid);
            
            roleGroupTmpMapper.delete(roleGroup);
            roleGroupMapper.delete(roleGroup);
            
            GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
            groupUser.setGroupOid(groupOid);
            
            groupUserTmpMapper.delete(groupUser);
            groupUserMapper.delete(groupUser);
            
            GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
            groupSupplier.setGroupOid(groupOid);
            
            groupSupplierTmpMapper.delete(groupSupplier);
            groupSupplierMapper.delete(groupSupplier);
            
            GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder();
            groupTradingPartner.setGroupOid(groupOid);
            
            groupTradingPartnerTmpMapper.delete(groupTradingPartner);
            groupTradingPartnerMapper.delete(groupTradingPartner);
            this.delete(mainGroup);
            groupTmpMapper.delete(tmpGroup);
            return;
        }
        
        throw new Exception("Unknow action type.");
    }
    

    @Override
    public void mkReject(CommonParameterHolder cp, GroupHolder main,
        GroupTmpHolder tmp) throws Exception
    {
        GroupTmpHolder tmpGroup = tmp;
        GroupHolder mainGroup = main;
        
        DbActionType actionType = tmpGroup.getActionType();
        if (DbActionType.CREATE.equals(actionType))
        {
            BigDecimal groupOid = tmpGroup.getGroupOid();
            
            RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
            roleGroup.setGroupOid(groupOid);
            roleGroupTmpMapper.delete(roleGroup);
            
            GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
            groupUser.setGroupOid(groupOid);
            groupUserTmpMapper.delete(groupUser);
            
            GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
            groupSupplier.setGroupOid(groupOid);
            groupSupplierTmpMapper.delete(groupSupplier);
            
            GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder();
            groupTradingPartner.setGroupOid(groupOid);
            groupTradingPartnerTmpMapper.delete(groupTradingPartner);
            groupTmpMapper.delete(tmpGroup);
            
            return;
        }
        
        if (DbActionType.UPDATE.equals(actionType))
        {
            List<? extends GroupUserHolder> groupUsers = tmpGroup.getGroupUsers();
            
            BeanUtils.copyProperties(mainGroup, tmpGroup);
            tmpGroup.setCtrlStatus(MkCtrlStatus.REJECTED);
            tmpGroup.setActor(cp.getLoginId());
            tmpGroup.setActionDate(new Date());
            
            groupTmpMapper.updateByPrimaryKey(tmpGroup);
            BigDecimal groupOid = mainGroup.getGroupOid();
            
            RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
            roleGroup.setGroupOid(groupOid);
            roleGroupTmpMapper.delete(roleGroup);
            
            Map<BigDecimal,GroupUserHolder> map = this.convertMap(mainGroup.getGroupUsers());
            for (Iterator<? extends GroupUserHolder> it = groupUsers.iterator(); it.hasNext();)
            {
                GroupUserTmpHolder groupTmp = (GroupUserTmpHolder)it.next();
                if (groupTmp.getApproved()
                    || !LastUpdateFrom.GROUP.equals(groupTmp.getLastUpdateFrom()))
                {
                    continue;
                }
                
                if (DbActionType.DELETE.equals(groupTmp.getActionType()))
                {
                    groupTmp.setActionType(DbActionType.CREATE);
                    groupTmp.setApproved(true);
                    groupUserTmpMapper.updateByPrimaryKey(groupTmp);
                    continue;
                }
                
                if (DbActionType.CREATE.equals(groupTmp.getActionType())
                    && !map.containsKey(groupTmp.getUserOid()))
                {
                    GroupUserTmpHolder delete = new GroupUserTmpHolder(); 
                    delete.setGroupOid(groupOid);
                    delete.setUserOid(groupTmp.getUserOid());
                    groupUserTmpMapper.delete(delete);
                    continue;
                }
                
                groupTmp.setApproved(true);
                groupUserTmpMapper.updateByPrimaryKey(groupTmp);
            }
            
            GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
            groupSupplier.setGroupOid(groupOid);
            groupSupplierTmpMapper.delete(groupSupplier);
            
            GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder();
            groupTradingPartner.setGroupOid(groupOid);
            groupTradingPartnerTmpMapper.delete(groupTradingPartner);
        
            
            List<? extends RoleGroupHolder> roleGroups = mainGroup.getRoleGroups();
            if (roleGroups != null)
            {
                for (RoleGroupHolder role : roleGroups)
                {
                    RoleGroupTmpHolder tmpRoleGroup = new RoleGroupTmpHolder();
                    tmpRoleGroup.setGroupOid(groupOid);
                    tmpRoleGroup.setRoleOid(role.getRoleOid());
                    roleGroupTmpMapper.insert(tmpRoleGroup);
                }
            }
            
            List<? extends GroupSupplierHolder> groupSuppliers = mainGroup.getGroupSuppliers();
            if (groupSuppliers != null)
            {
                for (GroupSupplierHolder supplier : groupSuppliers)
                {
                    GroupSupplierTmpHolder tmpSupplier = new GroupSupplierTmpHolder();
                    tmpSupplier.setGroupOid(groupOid);
                    tmpSupplier.setSupplierOid(supplier.getSupplierOid());
                    groupSupplierTmpMapper.insert(tmpSupplier);
                }
            }
            
            List<? extends GroupTradingPartnerHolder> groupTradingPartners = mainGroup.getGroupTradingPartners();
            if (groupTradingPartners != null)
            {
                for (GroupTradingPartnerHolder tradingPartner : groupTradingPartners)
                {
                    GroupTradingPartnerTmpHolder tmpTradingPartner = new GroupTradingPartnerTmpHolder();
                    tmpTradingPartner.setGroupOid(groupOid);
                    tmpTradingPartner.setTradingPartnerOid(tradingPartner.getTradingPartnerOid());
                    groupTradingPartnerTmpMapper.insert(tmpTradingPartner);
                }
                
            }
            
            return;
        }
        
        if (DbActionType.DELETE.equals(actionType))
        {
            tmpGroup.setCtrlStatus(MkCtrlStatus.REJECTED);
            tmpGroup.setActor(cp.getLoginId());
            tmpGroup.setActionDate(new Date());
            
            groupTmpMapper.updateByPrimaryKey(tmpGroup);
            
            List<? extends GroupUserHolder> users = tmpGroup.getGroupUsers();
            if (users != null)
            {
                for (GroupUserHolder groupUser : users)
                {
                    GroupUserTmpHolder update = (GroupUserTmpHolder)groupUser;
                    update.setApproved(true);
                    update.setActionType(DbActionType.CREATE);
                    update.setLastUpdateFrom(LastUpdateFrom.GROUP);
                    groupUserTmpMapper.updateByPrimaryKey(update);
                }
            }
            
            return;
        }
        
        throw new Exception("Unknow action type.");
    }
    

    @Override
    public void mkWithdraw(CommonParameterHolder cp, GroupHolder main,
        GroupTmpHolder tmp) throws Exception
    {
        GroupTmpHolder tmpGroup = (GroupTmpHolder)tmp;
        GroupHolder mainGroup = main;
        
        DbActionType actionType = tmpGroup.getActionType();
        if (DbActionType.CREATE.equals(actionType))
        {
            BigDecimal groupOid = tmpGroup.getGroupOid();
            
            RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
            roleGroup.setGroupOid(groupOid);
            roleGroupTmpMapper.delete(roleGroup);
            
            GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
            groupUser.setGroupOid(groupOid);
            groupUserTmpMapper.delete(groupUser);
            
            GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
            groupSupplier.setGroupOid(groupOid);
            groupSupplierTmpMapper.delete(groupSupplier);
            
            GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder();
            groupTradingPartner.setGroupOid(groupOid);
            groupTradingPartnerTmpMapper.delete(groupTradingPartner);
        
            groupTmpMapper.delete(tmpGroup);
            return;
        }
        
        if (DbActionType.UPDATE.equals(actionType))
        {
            List<? extends GroupUserHolder> groupUsers = tmpGroup.getGroupUsers();
            BeanUtils.copyProperties(mainGroup, tmpGroup);
            tmpGroup.setCtrlStatus(MkCtrlStatus.WITHDRAWN);
            tmpGroup.setActor(cp.getLoginId());
            tmpGroup.setActionDate(new Date());
            
            groupTmpMapper.updateByPrimaryKey(tmpGroup);
            BigDecimal groupOid = tmpGroup.getGroupOid();
            
            RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
            roleGroup.setGroupOid(groupOid);
            roleGroupTmpMapper.delete(roleGroup);
            
            Map<BigDecimal,GroupUserHolder> map = this.convertMap(mainGroup.getGroupUsers());
            for (Iterator<? extends GroupUserHolder> it = groupUsers.iterator(); it.hasNext();)
            {
                GroupUserTmpHolder groupTmp = (GroupUserTmpHolder)it.next();
                if (groupTmp.getApproved()
                    || !LastUpdateFrom.GROUP.equals(groupTmp.getLastUpdateFrom()))
                {
                    continue;
                }
                
                if (DbActionType.DELETE.equals(groupTmp.getActionType()))
                {
                    groupTmp.setActionType(DbActionType.CREATE);
                    groupTmp.setApproved(true);
                    groupUserTmpMapper.updateByPrimaryKey(groupTmp);
                    continue;
                }
                
                if (DbActionType.CREATE.equals(groupTmp.getActionType())
                    && !map.containsKey(groupTmp.getUserOid()))
                {
                    GroupUserTmpHolder delete = new GroupUserTmpHolder(); 
                    delete.setGroupOid(groupOid);
                    delete.setUserOid(groupTmp.getUserOid());
                    groupUserTmpMapper.delete(delete);
                    continue;
                }
                
                groupTmp.setApproved(true);
                groupUserTmpMapper.updateByPrimaryKey(groupTmp);
            }
            
            GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
            groupSupplier.setGroupOid(groupOid);
            groupSupplierTmpMapper.delete(groupSupplier);
            
            GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder();
            groupTradingPartner.setGroupOid(groupOid);
            groupTradingPartnerTmpMapper.delete(groupTradingPartner);
        
            
            List<? extends RoleGroupHolder> roleGroups = tmpGroup.getRoleGroups();
            if (roleGroups != null)
            {
                for (RoleGroupHolder role : roleGroups)
                {
                    RoleGroupTmpHolder tmpRoleGroup = new RoleGroupTmpHolder();
                    tmpRoleGroup.setGroupOid(groupOid);
                    tmpRoleGroup.setRoleOid(role.getRoleOid());
                    roleGroupTmpMapper.insert(tmpRoleGroup);
                }
            }
            
            
            List<? extends GroupSupplierHolder> groupSuppliers = tmpGroup.getGroupSuppliers();
            if (groupSuppliers != null)
            {
                for (GroupSupplierHolder supplier : groupSuppliers)
                {
                    GroupSupplierTmpHolder tmpSupplier = new GroupSupplierTmpHolder();
                    tmpSupplier.setGroupOid(groupOid);
                    tmpSupplier.setSupplierOid(supplier.getSupplierOid());
                    groupSupplierTmpMapper.insert(tmpSupplier);
                }
            }
            
            List<? extends GroupTradingPartnerHolder> groupTradingPartners = tmpGroup.getGroupTradingPartners();
            if (groupTradingPartners != null)
            {
                for (GroupTradingPartnerHolder tradingPartner : groupTradingPartners)
                {
                    GroupTradingPartnerTmpHolder tmpTradingPartner = new GroupTradingPartnerTmpHolder();
                    tmpTradingPartner.setGroupOid(groupOid);
                    tmpTradingPartner.setTradingPartnerOid(tradingPartner.getTradingPartnerOid());
                    groupTradingPartnerTmpMapper.insert(tmpTradingPartner);
                }
            }
            return;
        }
        
        
        if (DbActionType.DELETE.equals(actionType))
        {
            tmpGroup.setCtrlStatus(MkCtrlStatus.WITHDRAWN);
            tmpGroup.setActor(cp.getLoginId());
            tmpGroup.setActionDate(new Date());
            
            groupTmpMapper.updateByPrimaryKey(tmpGroup);
            
            List<? extends GroupUserHolder> users = tmpGroup.getGroupUsers();
            if (users != null)
            {
                for (GroupUserHolder groupUser : users)
                {
                    GroupUserTmpHolder update = (GroupUserTmpHolder)groupUser;
                    update.setApproved(true);
                    update.setActionType(DbActionType.CREATE);
                    update.setLastUpdateFrom(LastUpdateFrom.GROUP);
                    groupUserTmpMapper.updateByPrimaryKey(update);
                }
            }

            return;
        }
    
        throw new Exception("Unknow action type.");
    }


    @Override
    public int getCountOfSummary(GroupTmpExHolder param) throws Exception
    {
        return groupTmpMapper.getCountOfSummary(param);
    }

    
    @Override
    public List<GroupTmpExHolder> getListOfSummary(GroupTmpExHolder param)
        throws Exception
    {
        return groupTmpMapper.getListOfSummary(param);
    }


    @Override
    public List<GroupHolder> selectGroupsByUserType(BigDecimal userTypeOid)
            throws Exception
    {
        if (userTypeOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        GroupHolder param = new GroupHolder();
        param.setUserTypeOid(userTypeOid);

        return this.select(param);
    }

    
    @Override
    public GroupHolder selectGroupByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return groupMapper.selectGroupByUserOid(userOid);
    }


    @Override
    public void createGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder group) throws Exception
    {
        if (commonParam.getMkMode())
        {
            //maker checker mode
            this.getMeBean().mkCreate(commonParam, group);
            return;
        }
        
        group.setActor(commonParam.getLoginId());
        group.setActionType(DbActionType.CREATE);
        group.setActionDate(group.getCreateDate());
        group.setCtrlStatus(MkCtrlStatus.APPROVED);
        this.getMeBean().auditInsert(commonParam, group);
        groupTmpMapper.insert(group);
        
        List<? extends RoleGroupHolder> roleGroups = group.getRoleGroups();
        if (roleGroups != null)
        {
            for (RoleGroupHolder roleGroup : roleGroups)
            {
                roleGroupMapper.insert(roleGroup);
                roleGroupTmpMapper.insert((RoleGroupTmpHolder)roleGroup);
            }
        }
        
        List<? extends GroupUserHolder> groupUsers = group.getGroupUsers();
        if (groupUsers != null)
        {
            for (GroupUserHolder groupUser : groupUsers)
            {
                groupUserMapper.insert(groupUser);
                GroupUserTmpHolder gut = (GroupUserTmpHolder) groupUser;
                gut.setApproved(true);
                groupUserTmpMapper.insert(gut);
            }
        }
        
        List<? extends GroupSupplierHolder> groupSuppliers = group.getGroupSuppliers();
        if (groupSuppliers != null)
        {
            for (GroupSupplierHolder groupSupplier : groupSuppliers)
            {
                groupSupplierMapper.insert(groupSupplier);
                groupSupplierTmpMapper.insert((GroupSupplierTmpHolder)groupSupplier);
            }
        }
        
        List<? extends GroupTradingPartnerHolder> groupTradingPartners = group.getGroupTradingPartners();
        if (groupTradingPartners != null)
        {
            for (GroupTradingPartnerHolder groupTradingPartner : groupTradingPartners)
            {
                groupTradingPartnerMapper.insert(groupTradingPartner);
                groupTradingPartnerTmpMapper.insert((GroupTradingPartnerTmpHolder)groupTradingPartner);
            }
        }
    }


    @Override
    public GroupHolder selectGroupByKey(BigDecimal groupOid) throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        GroupHolder param = new GroupHolder();
        param.setGroupOid(groupOid);
        
        List<GroupHolder> rlt = groupMapper.select(param);
        
        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
        
        return rlt.get(0);
    }


    @Override
    public void updateGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder oldGroup, GroupTmpHolder currentGroup) throws Exception
    {
        if (commonParam.getMkMode())
        {
            getMeBean().mkUpdate(commonParam, oldGroup, currentGroup);
            return;
        }
        
        currentGroup.setActor(commonParam.getLoginId());
        currentGroup.setActionType(DbActionType.UPDATE);
        currentGroup.setActionDate(currentGroup.getUpdateDate());
        currentGroup.setCtrlStatus(MkCtrlStatus.APPROVED);
        this.getMeBean().auditUpdateByPrimaryKey(commonParam, oldGroup, currentGroup);
        groupTmpMapper.updateByPrimaryKey(currentGroup);
        
        BigDecimal groupOid = oldGroup.getGroupOid();
        RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
        roleGroup.setGroupOid(groupOid);
        
        roleGroupTmpMapper.delete(roleGroup);
        roleGroupMapper.delete(roleGroup);
        
        Map<String,List<GroupUserHolder>> map = this.splitGroupUsers(currentGroup, oldGroup);
        List<GroupUserHolder> deleteGroupUsers = map.get(DELETE_GROUP_USERS);
        
        if (deleteGroupUsers != null)
        {
            for (GroupUserHolder groupUser : deleteGroupUsers)
            {
                GroupUserTmpHolder delete = new GroupUserTmpHolder();
                delete.setUserOid(groupUser.getUserOid());
                delete.setGroupOid(groupOid);
                groupUserTmpMapper.delete(delete);
                groupUserMapper.delete(delete);
            }
        }
        
        List<GroupUserHolder> updateGroupUsers = map.get(UPDATE_GROUP_USERS);
        
        if (updateGroupUsers != null)
        {
            for (GroupUserHolder groupUser : updateGroupUsers)
            {
                GroupUserTmpHolder groupUserTmp = (GroupUserTmpHolder)groupUser;
                groupUserTmp.setActionType(DbActionType.CREATE);
                groupUserTmp.setLastUpdateFrom(LastUpdateFrom.GROUP);
                groupUserTmp.setApproved(true);
                groupUserTmpMapper.updateByPrimaryKey(groupUserTmp);
                groupUserMapper.updateByPrimaryKey(groupUserTmp);
            }
        }   
        
        GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
        groupSupplier.setGroupOid(groupOid);
        
        groupSupplierTmpMapper.delete(groupSupplier);
        groupSupplierMapper.delete(groupSupplier);
        
        GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder();
        groupTradingPartner.setGroupOid(groupOid);
        
        groupTradingPartnerTmpMapper.delete(groupTradingPartner);
        groupTradingPartnerMapper.delete(groupTradingPartner);
        
        List<? extends RoleGroupHolder> roleGroups = currentGroup.getRoleGroups();
        if (roleGroups != null)
        {
            for (RoleGroupHolder role : roleGroups)
            {
                roleGroupMapper.insert(role);
                roleGroupTmpMapper.insert((RoleGroupTmpHolder)role);
            }
        }
        
        List<? extends GroupUserHolder> inserts = map.get(INSERT_GROUP_USERS);
        if (inserts != null)
        {
            for (GroupUserHolder user : inserts)
            {
                groupUserMapper.insert(user);
                GroupUserTmpHolder groupUserTmp = (GroupUserTmpHolder)user;
                groupUserTmp.setApproved(true);
                groupUserTmp.setLastUpdateFrom(LastUpdateFrom.GROUP);
                groupUserTmp.setActionType(DbActionType.CREATE);
                groupUserTmpMapper.insert(groupUserTmp);
            }
        }
        
        
        List<? extends GroupSupplierHolder> groupSuppliers = currentGroup.getGroupSuppliers();
        if (groupSuppliers != null)
        {
            for (GroupSupplierHolder supplier : groupSuppliers)
            {
                groupSupplierMapper.insert(supplier);
                groupSupplierTmpMapper.insert((GroupSupplierTmpHolder)supplier);
            }
        }
        
        List<? extends GroupTradingPartnerHolder> groupTradingPartners = currentGroup.getGroupTradingPartners();
        if (groupTradingPartners != null)
        {
            for (GroupTradingPartnerHolder tradingPartner : groupTradingPartners)
            {
                groupTradingPartnerMapper.insert(tradingPartner);
                groupTradingPartnerTmpMapper.insert((GroupTradingPartnerTmpHolder)tradingPartner);
            }
        }
    }


    @Override
    public void approveGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder tmpGroup) throws Exception
    {
        if (DbActionType.CREATE.equals(tmpGroup.getActionType()))
        {
            this.getMeBean().mkApprove(commonParam, null, tmpGroup);
            return;
        }
        
        BigDecimal groupOid = tmpGroup.getGroupOid();
        GroupHolder mainGroup = this.selectGroupByKey(groupOid);
        mainGroup.setRoleGroups(new ArrayList<RoleGroupHolder>());
        mainGroup.setGroupSuppliers(new ArrayList<GroupSupplierHolder>());
        mainGroup.setGroupUsers(new ArrayList<GroupUserHolder>());
        mainGroup.setGroupTradingPartners(new ArrayList<GroupTradingPartnerHolder>());

        List<? extends RoleGroupHolder> roles = roleGroupService.selectRoleGroupByGroupOid(groupOid);
        if (roles != null)
        {
            mainGroup.setRoleGroups(roles);
        }
        
        List<? extends GroupSupplierHolder> suppliers = groupSupplierService.selectGroupSupplierByGroupOid(groupOid);
        if (suppliers != null)
        {
            mainGroup.setGroupSuppliers(suppliers);
        }
        
        List<? extends GroupUserHolder> users = groupUserService.selectGroupUserByGroupOid(groupOid);
        if (users != null)
        {
            mainGroup.setGroupUsers(users);
        }
        
        List<GroupTradingPartnerHolder> tps = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(groupOid);
        if (tps != null)
        {
            mainGroup.setGroupTradingPartners(tps);
        }
        
        this.getMeBean().mkApprove(commonParam, mainGroup, tmpGroup);
    }


    @Override
    public void rejectGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder tmpGroup) throws Exception
    {
            if (DbActionType.CREATE.equals(tmpGroup.getActionType()))
            {
                this.getMeBean().mkReject(commonParam, null, tmpGroup);
                return;
            }
            
            BigDecimal groupOid = tmpGroup.getGroupOid();
            GroupHolder mainGroup = this.selectGroupByKey(groupOid);
            mainGroup.setRoleGroups(new ArrayList<RoleGroupHolder>());
            mainGroup.setGroupSuppliers(new ArrayList<GroupSupplierHolder>());
            mainGroup.setGroupUsers(new ArrayList<GroupUserHolder>());
            mainGroup.setGroupTradingPartners(new ArrayList<GroupTradingPartnerHolder>());
            
            List<RoleGroupHolder> roles = roleGroupService.selectRoleGroupByGroupOid(groupOid);
            if (roles != null)
            {
               mainGroup.setRoleGroups(roles);
            }
            
            List<GroupSupplierHolder> suppliers = groupSupplierService.selectGroupSupplierByGroupOid(groupOid);
            if (suppliers != null)
            {
                mainGroup.setGroupSuppliers(suppliers);
            }
            
            List<GroupUserHolder> users = groupUserService.selectGroupUserByGroupOid(groupOid);
            if (users != null)
            {
                mainGroup.setGroupUsers(users);
            }
            
            List<GroupTradingPartnerHolder> tps = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(groupOid);
            if (tps != null)
            {
                mainGroup.setGroupTradingPartners(tps);
            }
            
            this.getMeBean().mkReject(commonParam, mainGroup, tmpGroup);   
    }


    @Override
    public void removeGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder oldGroup) throws Exception
    {
        if (commonParam.getMkMode())
        {
            // maker checker mode
            this.getMeBean().mkDelete(commonParam, oldGroup);
        }
        else
        {
            BigDecimal groupOid = oldGroup.getGroupOid();
            RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
            roleGroup.setGroupOid(groupOid);
            
            roleGroupTmpMapper.delete(roleGroup);
            roleGroupMapper.delete(roleGroup);
            
            GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
            groupUser.setGroupOid(groupOid);
            
            groupUserTmpMapper.delete(groupUser);
            groupUserMapper.delete(groupUser);
            
            GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
            groupSupplier.setGroupOid(groupOid);
            
            groupSupplierTmpMapper.delete(groupSupplier);
            groupSupplierMapper.delete(groupSupplier);
            
            GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder();
            groupTradingPartner.setGroupOid(groupOid);
            
            groupTradingPartnerTmpMapper.delete(groupTradingPartner);
            groupTradingPartnerMapper.delete(groupTradingPartner);
            this.getMeBean().auditDelete(commonParam, oldGroup);
            groupTmpMapper.delete(oldGroup);
        }
    }


    /** 
     * {@inheritDoc}
     * @author jiangming
     * @see com.pracbiz.b2bportal.core.service.GroupService#withdrawGroupProfile(com.pracbiz.b2bportal.base.holder.CommonParameterHolder, com.pracbiz.b2bportal.core.holder.GroupTmpHolder)
     */
    @Override
    public void withdrawGroupProfile(CommonParameterHolder cp,
        GroupTmpHolder tmpGroup) throws Exception
    {
        if (DbActionType.CREATE.equals(tmpGroup.getActionType()))
        {
            this.getMeBean().mkWithdraw(cp, null, tmpGroup);
            return;
        }
        
        BigDecimal groupOid = tmpGroup.getGroupOid();
        GroupHolder mainGroup = this.selectGroupByKey(groupOid);
        mainGroup.setRoleGroups(new ArrayList<RoleGroupHolder>());
        mainGroup.setGroupSuppliers(new ArrayList<GroupSupplierHolder>());
        mainGroup.setGroupUsers(new ArrayList<GroupUserHolder>());
        mainGroup.setGroupTradingPartners(new ArrayList<GroupTradingPartnerHolder>());
        
        List<RoleGroupHolder> roles = roleGroupService.selectRoleGroupByGroupOid(groupOid);
        if (roles != null)
        {
            mainGroup.setRoleGroups(roles);
        }
        
        List<GroupSupplierHolder> suppliers = groupSupplierService.selectGroupSupplierByGroupOid(groupOid);
        if (suppliers != null)
        {
            mainGroup.setGroupSuppliers(suppliers);
        }
        
        List<GroupUserHolder> users = groupUserService.selectGroupUserByGroupOid(groupOid);
        if (users != null)
        {
            mainGroup.setGroupUsers(users);
        }
        
        List<GroupTradingPartnerHolder> tps = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(groupOid);
        if (tps != null)
        {
            mainGroup.setGroupTradingPartners(tps);
        }
        
        this.getMeBean().mkWithdraw(cp, mainGroup, tmpGroup);  
    }
    
    
    private Map<String,List<GroupUserHolder>> splitGroupUsers(GroupTmpHolder currGroup, GroupTmpHolder oldGroup)
    {
        List<? extends GroupUserHolder> oldGroupUsers = oldGroup.getGroupUsers();
        List<? extends GroupUserHolder> currGroupUsers = currGroup.getGroupUsers();
        
        Map<BigDecimal,GroupUserTmpHolder> currGroupUserMap = new HashMap<BigDecimal, GroupUserTmpHolder>();
        if (currGroupUsers != null)
        {
            for (GroupUserHolder currGroupUser : currGroupUsers)
            {
                GroupUserTmpHolder currGroupTmpUser = (GroupUserTmpHolder)currGroupUser;
                currGroupUserMap.put(currGroupTmpUser.getUserOid(),currGroupTmpUser);
            }
        }
        
        
        Map<BigDecimal,GroupUserTmpHolder> oldGroupUserMap = new HashMap<BigDecimal, GroupUserTmpHolder>();
        if (oldGroupUsers != null)
        {
            for (GroupUserHolder oldGroupUser : oldGroupUsers)
            {
                GroupUserTmpHolder oldGroupTmpUser = (GroupUserTmpHolder)oldGroupUser;
                oldGroupUserMap.put(oldGroupTmpUser.getUserOid(),oldGroupTmpUser);
            } 
        }
        
        
        Set<BigDecimal> currSet = currGroupUserMap.keySet();
        Set<BigDecimal> oldSet = oldGroupUserMap.keySet();
        Set<BigDecimal> set = new HashSet<BigDecimal>();
        set.addAll(currSet);
        set.addAll(oldSet);
       
        Map<String,List<GroupUserHolder>> rlt = new HashMap<String, List<GroupUserHolder>>();
        for (Iterator<BigDecimal> it = set.iterator(); it.hasNext();)
        {
            BigDecimal userOid = it.next();
            
            if (!currGroupUserMap.containsKey(userOid))
            {
                List<GroupUserHolder> deletes = rlt.get(DELETE_GROUP_USERS);
                if (deletes == null)
                {
                    deletes = new ArrayList<GroupUserHolder>();
                    rlt.put(DELETE_GROUP_USERS, deletes);
                }
                
                deletes.add(oldGroupUserMap.get(userOid));
            }
            
            if (!oldGroupUserMap.containsKey(userOid))
            {
                List<GroupUserHolder> inserts = rlt.get(INSERT_GROUP_USERS);
                if (inserts == null) 
                {
                    inserts = new ArrayList<GroupUserHolder>();
                    rlt.put(INSERT_GROUP_USERS, inserts);
                }
                
                inserts.add(currGroupUserMap.get(userOid));
            }
            
            if (oldGroupUserMap.containsKey(userOid)
                && currGroupUserMap.containsKey(userOid))
            {
                GroupUserTmpHolder oldGroupTmpUser = oldGroupUserMap.get(userOid);
                if (!oldGroupTmpUser.getApproved()
                    && DbActionType.DELETE.equals(oldGroupTmpUser.getActionType())
                    && LastUpdateFrom.GROUP.equals(oldGroupTmpUser.getLastUpdateFrom()))
                {
                    List<GroupUserHolder> updates = rlt.get(UPDATE_GROUP_USERS);
                    if (updates == null) updates = new ArrayList<GroupUserHolder>();
                    updates.add(currGroupUserMap.get(userOid));
                    
                    rlt.put(UPDATE_GROUP_USERS, updates);
                }
            }
        }
        
        return rlt;
    }


    private Map<BigDecimal,GroupUserHolder> convertMap(List<? extends GroupUserHolder> groupUsers)
    {
        Map<BigDecimal,GroupUserHolder> map = new HashMap<BigDecimal, GroupUserHolder>();
        for (GroupUserHolder groupUser : groupUsers)
        {
            map.put(groupUser.getUserOid(), groupUser);
        }
        
        return map;
    }

    
    private BigDecimal getCompanyOid(GroupHolder group)
    {
        if (GroupType.BUYER.equals(group.getGroupType()))
        {
            return group.getBuyerOid();
        }
        
        return group.getSupplierOid();
    }
    
    
    private void handleApproveGroupUsers(List<? extends GroupUserHolder> groupUsers, BigDecimal groupOid)
    {
        
        for (Iterator<? extends GroupUserHolder> it = groupUsers.iterator(); it.hasNext();)
        {
            GroupUserTmpHolder groupTmp = (GroupUserTmpHolder)it.next();
            if (!groupTmp.getApproved()
                && DbActionType.DELETE.equals(groupTmp.getActionType())
                && LastUpdateFrom.GROUP.equals(groupTmp.getLastUpdateFrom()))
            {
                GroupUserTmpHolder delete = new GroupUserTmpHolder();
                delete.setGroupOid(groupOid);
                delete.setUserOid(groupTmp.getUserOid());
                groupUserTmpMapper.delete(delete);
                groupUserMapper.delete(delete);
                it.remove();
                continue;
            }
            
            if (!groupTmp.getApproved()
                && DbActionType.CREATE.equals(groupTmp.getActionType())
                && LastUpdateFrom.GROUP.equals(groupTmp.getLastUpdateFrom()))
            {
                GroupUserTmpHolder delete = new GroupUserTmpHolder();
                delete.setGroupOid(groupOid);
                delete.setUserOid(groupTmp.getUserOid());
                groupUserMapper.delete(delete);
                
                groupTmp.setApproved(true);
                groupUserTmpMapper.updateByPrimaryKey(groupTmp);
                groupUserMapper.insert(groupTmp);
            }
        }
    }


    @Override
    public List<GroupHolder> selectGroupByBuyerOidAndUserTypeOid(
            BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception
    {
        if (buyerOid == null || userTypeOid == null)
        {
            throw new IllegalArgumentException();
        }
        GroupHolder param = new GroupHolder();
        param.setBuyerOid(buyerOid);
        param.setUserTypeOid(userTypeOid);
        List<GroupHolder> result = select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        return result;
    }


    @Override
    public List<GroupHolder> selectGroupBySupplierOidAndUserTypeOid(
            BigDecimal supplierOid, BigDecimal userTypeOid) throws Exception
    {
        if (supplierOid == null || userTypeOid == null)
        { 
            throw new IllegalArgumentException();
        }
        GroupHolder param = new GroupHolder();
        param.setSupplierOid(supplierOid);
        param.setUserTypeOid(userTypeOid);
        List<GroupHolder> result = select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        return result;
    }


    @Override
    public List<GroupHolder> selectGroupByBuyerOidAndAccessUrl(
            BigDecimal buyerOid, String accessUrl) throws Exception
    {
        if (buyerOid == null || accessUrl == null || accessUrl.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("accessUrl", accessUrl);
        
        return groupMapper.selectGroupByBuyerOidAndOperationId(map);
    }
}
