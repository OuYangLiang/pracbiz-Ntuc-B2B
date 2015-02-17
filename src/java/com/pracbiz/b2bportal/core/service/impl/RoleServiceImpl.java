package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.holder.RoleGroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.RoleTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.RoleGroupMapper;
import com.pracbiz.b2bportal.core.mapper.RoleGroupTmpMapper;
import com.pracbiz.b2bportal.core.mapper.RoleMapper;
import com.pracbiz.b2bportal.core.mapper.RoleOperationMapper;
import com.pracbiz.b2bportal.core.mapper.RoleOperationTmpMapper;
import com.pracbiz.b2bportal.core.mapper.RoleTmpMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.SupplierRoleMapper;
import com.pracbiz.b2bportal.core.mapper.SupplierRoleTmpMapper;
import com.pracbiz.b2bportal.core.service.RoleOperationService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.SupplierRoleService;

public class RoleServiceImpl extends DBActionServiceDefaultImpl<RoleHolder>
    implements RoleService, ApplicationContextAware
{
    private ApplicationContext ctx;
    
    @Autowired private RoleTmpService roleTmpService;
    @Autowired private SupplierRoleService supplierRoleService;
    @Autowired private RoleMapper roleMapper;
    @Autowired private RoleTmpMapper roleTmpMapper;
    @Autowired private RoleOperationMapper roleOperationMapper;
    @Autowired private RoleOperationTmpMapper roleOperationTmpMapper;
    @Autowired private RoleUserMapper roleUserMapper;
    @Autowired private RoleUserTmpMapper roleUserTmpMapper;
    @Autowired private RoleGroupMapper roleGroupMapper;
    @Autowired private RoleGroupTmpMapper roleGroupTmpMapper;
    @Autowired private RoleOperationService roleOperationService;
    @Autowired private SupplierRoleMapper supplierRoleMapper;
    @Autowired private SupplierRoleTmpMapper supplierRoleTmpMapper;
    

    @Override
    public List<RoleHolder> select(RoleHolder param) throws Exception
    {
        return roleMapper.select(param);
    }


    @Override
    public void insert(RoleHolder newObj_) throws Exception
    {
        roleMapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(RoleHolder oldObj_,
        RoleHolder newObj_) throws Exception
    {
        roleMapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(RoleHolder oldObj_, RoleHolder newObj_)
        throws Exception
    {
        roleMapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(RoleHolder oldObj_) throws Exception
    {
        roleMapper.delete(oldObj_);
    }


    @Override
    public void mkCreate(CommonParameterHolder cp, RoleTmpHolder newObj_)
            throws Exception
    {
        RoleTmpHolder role = newObj_;
        role.setActor(cp.getLoginId());
        role.setActionType(DbActionType.CREATE);
        role.setActionDate(role.getCreateDate());
        role.setCtrlStatus(MkCtrlStatus.PENDING);
        
        roleTmpMapper.insert(role);
        
        List<RoleOperationHolder> roleOperations = role.getRoleOperations();
        
        if (null != roleOperations)
        {
            for (RoleOperationHolder ro : roleOperations)
            {
                roleOperationTmpMapper.insert((RoleOperationTmpHolder) ro);
            }
        }
        
        List<SupplierRoleHolder> srList = role.getSupplierRoles();
        
        if (null != srList)
        {
            for (SupplierRoleHolder sr : srList)
            {
                supplierRoleTmpMapper.insert((SupplierRoleTmpHolder) sr);
            }
        }
    }


    @Override
    public void mkUpdate(CommonParameterHolder cp, RoleTmpHolder oldObj_,
        RoleTmpHolder newObj_) throws Exception
    {
        RoleTmpHolder role = newObj_;
        role.setActor(cp.getLoginId());
        role.setActionDate(role.getUpdateDate());
        
        if(!(DbActionType.CREATE.equals(role.getActionType())
            && MkCtrlStatus.PENDING.equals(role.getCtrlStatus())))
        {
            role.setActionType(DbActionType.UPDATE);
        }
        
        role.setCtrlStatus(MkCtrlStatus.PENDING);
        
        roleTmpMapper.updateByPrimaryKeySelective(role);
        
        // delete old operations.
        RoleOperationTmpHolder param = new RoleOperationTmpHolder();
        param.setRoleOid(role.getRoleOid());
        
        roleOperationTmpMapper.delete(param);
        
        // insert new operations.
        if (null != role.getRoleOperations())
        {
            for (RoleOperationHolder ro : role.getRoleOperations())
            {
                roleOperationTmpMapper.insert((RoleOperationTmpHolder) ro);
            }
        }
        
        
        if (BigDecimal.valueOf(3).equals(role.getUserTypeOid()))
        {
            // delete old supplier role.
            SupplierRoleTmpHolder srParam = new SupplierRoleTmpHolder();
            srParam.setRoleOid(role.getRoleOid());
            
            supplierRoleTmpMapper.delete(srParam);
            
            // insert new supplier role.
            if ( null != role.getSupplierRoles())
            {
                for (SupplierRoleHolder sr : role.getSupplierRoles())
                {
                    SupplierRoleTmpHolder srt = new SupplierRoleTmpHolder();
                    BeanUtils.copyProperties(sr, srt);
                    supplierRoleTmpMapper.insert(srt);
                }
            }
        }
        
        if (BigDecimal.valueOf(5).equals(role.getUserTypeOid()) && null != role.getBuyerOid())
        {
            // delete old supplier role.
            SupplierRoleTmpHolder srParam = new SupplierRoleTmpHolder();
            srParam.setRoleOid(role.getRoleOid());
            
            supplierRoleTmpMapper.delete(srParam);
            
            // insert new supplier role.
            if ( null != role.getSupplierRoles())
            {
                for (SupplierRoleHolder sr : role.getSupplierRoles())
                {
                    SupplierRoleTmpHolder srt = new SupplierRoleTmpHolder();
                    BeanUtils.copyProperties(sr, srt);
                    supplierRoleTmpMapper.insert(srt);
                }
            }
        }
    }


    @Override
    public void mkDelete(CommonParameterHolder cp, RoleTmpHolder oldObj_)
            throws Exception
    {
        RoleTmpHolder role = oldObj_;
        role.setActor(cp.getLoginId());
        role.setActionType(DbActionType.DELETE);
        role.setActionDate(new Date());
        role.setCtrlStatus(MkCtrlStatus.PENDING);
        
        roleTmpMapper.updateByPrimaryKeySelective(role);
    }


    @Override
    public void mkApprove(CommonParameterHolder cp, RoleHolder main,
        RoleTmpHolder tmp) throws Exception
    {
        RoleTmpHolder tmpRole = tmp;
        RoleHolder mainRole = main;
        
        switch (tmpRole.getActionType())
        {
            case CREATE:
                tmpRole.setCtrlStatus(MkCtrlStatus.APPROVED);
                tmpRole.setActor(cp.getLoginId());
                tmpRole.setActionDate(new Date());

                roleTmpMapper.updateByPrimaryKey(tmpRole);

                this.insert(tmpRole);

                for(RoleOperationHolder ro : tmpRole.getRoleOperations())
                {
                    roleOperationMapper.insert(ro);
                }
                
                if (null != tmpRole.getSupplierRoles())
                {
                    for (SupplierRoleHolder sr : tmpRole.getSupplierRoles())
                    {
                        supplierRoleMapper.insert(sr);
                    }
                }
                
                break;
            case UPDATE:
                tmpRole.setCtrlStatus(MkCtrlStatus.APPROVED);
                tmpRole.setActor(cp.getLoginId());
                tmpRole.setActionDate(new Date());

                roleTmpMapper.updateByPrimaryKey(tmpRole);
                
                RoleOperationHolder param = new RoleOperationHolder();
                param.setRoleOid(mainRole.getRoleOid());
                roleOperationMapper.delete(param);
                
                this.updateByPrimaryKey(mainRole, tmpRole);
                
                for (RoleOperationHolder ro : tmpRole.getRoleOperations())
                {
                    roleOperationMapper.insert(ro);
                }
                
                if (BigDecimal.valueOf(3).equals(mainRole.getUserTypeOid()))
                {
                    SupplierRoleHolder srParam = new SupplierRoleHolder();
                    srParam.setRoleOid(mainRole.getRoleOid());
                    supplierRoleMapper.delete(srParam);
                    
                    for (SupplierRoleHolder sr : tmpRole.getSupplierRoles())
                    {
                        supplierRoleMapper.insert(sr);
                    }
                }
                break;
            case DELETE:
                // Remove records from role-user & t-role-user tables.
                RoleUserTmpHolder ruParam = new RoleUserTmpHolder();
                ruParam.setRoleOid(mainRole.getRoleOid());
                
                roleUserMapper.delete(ruParam);
                roleUserTmpMapper.delete(ruParam);
                
                
                // Remove records from role-group & t-role-group tables.
                RoleGroupTmpHolder rgParam = new RoleGroupTmpHolder();
                rgParam.setRoleOid(mainRole.getRoleOid());
                
                roleGroupMapper.delete(rgParam);
                roleGroupTmpMapper.delete(rgParam);
                
                // Remove records from role-operation & t-role-operation tables.
                RoleOperationTmpHolder roParam = new RoleOperationTmpHolder();
                roParam.setRoleOid(mainRole.getRoleOid());

                roleOperationMapper.delete(roParam);
                roleOperationTmpMapper.delete(roParam);
                
                // Remove records from supplier-role & t-supplier-role tables.
                SupplierRoleTmpHolder srParam = new SupplierRoleTmpHolder();
                srParam.setRoleOid(mainRole.getRoleOid());
                
                supplierRoleMapper.delete(srParam);
                supplierRoleTmpMapper.delete(srParam);
                
                this.delete(mainRole);
                roleTmpService.delete(tmpRole);
                break;
            default:
                throw new Exception("Unknow action type.");
        }
    }

    
    @Override
    public void mkReject(CommonParameterHolder cp, RoleHolder main,
        RoleTmpHolder tmp) throws Exception
    {
        RoleTmpHolder tmpRole = (RoleTmpHolder) tmp;
        RoleHolder mainRole = main;
        
        switch (tmpRole.getActionType())
        {
            case CREATE:
                // Remove records from t-role-operation tables.
                RoleOperationTmpHolder roParam = new RoleOperationTmpHolder();
                roParam.setRoleOid(tmpRole.getRoleOid());

                roleOperationTmpMapper.delete(roParam);
                
                // Remove records from supplier-role & t-supplier-role tables.
                SupplierRoleTmpHolder srParam = new SupplierRoleTmpHolder();
                srParam.setRoleOid(tmpRole.getRoleOid());
                
                supplierRoleTmpMapper.delete(srParam);
                
                roleTmpService.delete(tmpRole);
                break;
            case UPDATE:
                BeanUtils.copyProperties(mainRole, tmpRole);
                tmpRole.setCtrlStatus(MkCtrlStatus.REJECTED);
                tmpRole.setActor(cp.getLoginId());
                tmpRole.setActionDate(new Date());

                roleTmpMapper.updateByPrimaryKey(tmpRole);
                
                RoleOperationTmpHolder param = new RoleOperationTmpHolder();
                param.setRoleOid(tmpRole.getRoleOid());
                
                roleOperationTmpMapper.delete(param);
                
                for(RoleOperationHolder ro : mainRole.getRoleOperations())
                {
                    RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
                    rot.setRoleOid(ro.getRoleOid());
                    rot.setOpnId(ro.getOpnId());
                    
                    roleOperationTmpMapper.insert(rot);
                }
                
                if (BigDecimal.valueOf(3).equals(tmpRole.getUserTypeOid()))
                {
                    SupplierRoleTmpHolder srtParam = new SupplierRoleTmpHolder();
                    srtParam.setRoleOid(tmpRole.getRoleOid());
                    supplierRoleTmpMapper.delete(srtParam);
                    
                    for (SupplierRoleHolder sr : mainRole.getSupplierRoles())
                    {
                        srtParam = new SupplierRoleTmpHolder();
                        srtParam.setRoleOid(sr.getRoleOid());
                        srtParam.setSupplierOid(sr.getSupplierOid());
                        
                        supplierRoleTmpMapper.insert(srtParam);
                    }
                }
                break;
            case DELETE:
                tmpRole.setCtrlStatus(MkCtrlStatus.REJECTED);
                tmpRole.setActor(cp.getLoginId());
                tmpRole.setActionDate(new Date());

                roleTmpMapper.updateByPrimaryKey(tmpRole);
                break;
            default:
                throw new Exception("Unknow action type.");
        }
    }

    
    @Override
    public void mkWithdraw(CommonParameterHolder cp, RoleHolder main,
        RoleTmpHolder tmp) throws Exception
    {
        RoleTmpHolder tmpRole = tmp;
        RoleHolder mainRole = main;
        
        switch (tmpRole.getActionType())
        {
            case CREATE:
                // Remove records from t-role-operation tables.
                RoleOperationTmpHolder roParam = new RoleOperationTmpHolder();
                roParam.setRoleOid(tmpRole.getRoleOid());

                roleOperationTmpMapper.delete(roParam);
                
                // Remove records from supplier-role & t-supplier-role tables.
                SupplierRoleTmpHolder srParam = new SupplierRoleTmpHolder();
                srParam.setRoleOid(tmpRole.getRoleOid());
                
                supplierRoleTmpMapper.delete(srParam);
                
                roleTmpService.delete(tmpRole);
                break;
            case UPDATE:
                BeanUtils.copyProperties(mainRole, tmpRole);
                tmpRole.setCtrlStatus(MkCtrlStatus.WITHDRAWN);
                tmpRole.setActor(cp.getLoginId());
                tmpRole.setActionDate(new Date());

                roleTmpMapper.updateByPrimaryKey(tmpRole);
                
                RoleOperationTmpHolder param = new RoleOperationTmpHolder();
                param.setRoleOid(tmpRole.getRoleOid());
                
                roleOperationTmpMapper.delete(param);
                
                for(RoleOperationHolder ro : mainRole.getRoleOperations())
                {
                    RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
                    rot.setRoleOid(ro.getRoleOid());
                    rot.setOpnId(ro.getOpnId());
                    
                    roleOperationTmpMapper.insert(rot);
                }
                
                if (BigDecimal.valueOf(3).equals(tmpRole.getUserTypeOid()))
                {
                    SupplierRoleTmpHolder srtParam = new SupplierRoleTmpHolder();
                    srtParam.setRoleOid(tmpRole.getRoleOid());
                    supplierRoleTmpMapper.delete(srtParam);
                    
                    for (SupplierRoleHolder sr : mainRole.getSupplierRoles())
                    {
                        srtParam = new SupplierRoleTmpHolder();
                        srtParam.setRoleOid(sr.getRoleOid());
                        srtParam.setSupplierOid(sr.getSupplierOid());
                        
                        supplierRoleTmpMapper.insert(srtParam);
                    }
                }
                break;
            case DELETE:
                tmpRole.setCtrlStatus(MkCtrlStatus.WITHDRAWN);
                tmpRole.setActor(cp.getLoginId());
                tmpRole.setActionDate(new Date());

                roleTmpMapper.updateByPrimaryKey(tmpRole);
                break;
            default:
                throw new Exception("Unknow action type.");
        }
    }


    @Override
    public int getCountOfSummary(RoleTmpExHolder param) throws Exception
    {
        return roleTmpMapper.getCountOfSummary(param);
    }


    @Override
    public List<RoleTmpExHolder> getListOfSummary(RoleTmpExHolder param)
        throws Exception
    {
        return roleTmpMapper.getListOfSummary(param);
    }


    @Override
    public RoleHolder selectAdminRoleById(String roleId) throws Exception
    {
        return roleMapper.selectAdminRoleById(roleId);
    }


    @Override
    public RoleHolder selectBuyerRoleById(BigDecimal buyerOid, String roleId)
            throws Exception
    {
        RoleHolder param = new RoleHolder();
        param.setBuyerOid(buyerOid);
        param.setRoleId(roleId);
        
        List<RoleHolder> rlt = this.select(param);
        if (null != rlt && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public RoleHolder selectSupplierRoleById(BigDecimal supplierOid,
            String roleId) throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleId", roleId);
        param.put("supplierOid", supplierOid);
        
        return roleMapper.selectSupplierRoleById(param);
    }


    @Override
    public List<RoleHolder> selectRolesByUserType(BigDecimal userTypeOid) throws Exception
    {
        if (userTypeOid == null)
        {
            throw new IllegalArgumentException();
        }
        RoleHolder param = new RoleHolder();
        param.setUserTypeOid(userTypeOid);
        param.setSortField("ROLE_NAME");
        param.setSortOrder("ASC");
        
        List<RoleHolder> rlt =  this.select(param);
        List<RoleHolder> availableRoles = new ArrayList<RoleHolder>();
        
        for (RoleHolder role : rlt)
        {
            if (!BigDecimal.valueOf(1).equals(role.getRoleOid()) && !BigDecimal.valueOf(2).equals(role.getRoleOid()))
            {
                availableRoles.add(role);
            }
        }
        
        return availableRoles;
    }


    @Override
    public List<RoleHolder> selectRolesByUserOid(BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return roleMapper.selectRolesByUserOid(userOid);
    }


    @Override
    public List<RoleHolder> selectBuyerRolesByBuyerOidAndUserType(
            BigDecimal buyerOid, BigDecimal userTypeOid)
            throws Exception
    {
        if (userTypeOid == null || buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        RoleHolder param = new RoleHolder();
        param.setBuyerOid(buyerOid);
        param.setUserTypeOid(userTypeOid);
        param.setSortField("ROLE_NAME");
        param.setSortOrder("ASC");
        
        return this.select(param);
    }


    @Override
    public List<RoleHolder> selectSupplierRolesBySupplierOidAndUserType(
            BigDecimal supplierOid, BigDecimal userTypeOid)
            throws Exception
    {
        if (userTypeOid == null || supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("supplierOid", supplierOid);
        param.put("userTypeOid", userTypeOid);
        
        return this.roleMapper.selectSupplierRolesBySupplierOidAndUserType(param);
    }
    
    
    @Override
    public void createRoleProfile(CommonParameterHolder cp, RoleTmpHolder role)
            throws Exception
    {
        if (cp.getMkMode())
        {
            // maker checker mode
            this.getMeBean().mkCreate(cp, role);
        }
        else
        {
            // non maker checker mode
            role.setActor(cp.getLoginId());
            role.setActionType(DbActionType.CREATE);
            role.setActionDate(role.getCreateDate());
            role.setCtrlStatus(MkCtrlStatus.APPROVED);
            
            this.getMeBean().auditInsert(cp, role);
            roleTmpService.insert(role);
            
            List<RoleOperationHolder> roleOperations = role.getRoleOperations();
            
            if (null != roleOperations)
            {
                for (RoleOperationHolder ro : roleOperations)
                {
                    roleOperationMapper.insert(ro);
                    roleOperationTmpMapper.insert((RoleOperationTmpHolder) ro);
                }
            }
            
            List<SupplierRoleHolder> srList = role.getSupplierRoles();
            
            if (null != srList)
            {
                for (SupplierRoleHolder sr : srList)
                {
                    supplierRoleMapper.insert(sr);
                    supplierRoleTmpMapper.insert((SupplierRoleTmpHolder) sr);
                }
            }
        }
        
    }


    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private RoleService getMeBean()
    {
        return ctx.getBean("roleService", RoleService.class);
    }


    @Override
    public void updateRoleProfile(CommonParameterHolder cp,
        RoleTmpHolder oldRole, RoleTmpHolder newRole) throws Exception
    {
        if (cp.getMkMode())
        {
            // maker checker mode
            this.getMeBean().mkUpdate(cp, oldRole, newRole);
        }
        else
        {
            // non maker checker mode
            newRole.setActor(cp.getLoginId());
            newRole.setActionType(DbActionType.UPDATE);
            newRole.setActionDate(newRole.getUpdateDate());
            newRole.setCtrlStatus(MkCtrlStatus.APPROVED);
            
            this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldRole, newRole);
            roleTmpService.updateByPrimaryKeySelective(oldRole, newRole);
            
            // delete old operations.
            RoleOperationTmpHolder param = new RoleOperationTmpHolder();
            param.setRoleOid(oldRole.getRoleOid());
            
            roleOperationMapper.delete(param);
            roleOperationTmpMapper.delete(param);
            
            // insert new operations.
            if (null != newRole.getRoleOperations())
            {
                for (RoleOperationHolder ro : newRole.getRoleOperations())
                {
                    RoleOperationTmpHolder rot = new RoleOperationTmpHolder();
                    BeanUtils.copyProperties(ro, rot);
                    roleOperationMapper.insert(rot);
                    roleOperationTmpMapper.insert(rot);
                }
            }
            
            if (BigDecimal.valueOf(3).equals(oldRole.getUserTypeOid()))
            {
                // delete old supplier role.
                SupplierRoleTmpHolder srParam = new SupplierRoleTmpHolder();
                srParam.setRoleOid(oldRole.getRoleOid());
                
                supplierRoleTmpMapper.delete(srParam);
                supplierRoleMapper.delete(srParam);
                
                // insert new supplier role.
                if ( null != newRole.getSupplierRoles())
                {
                    for (SupplierRoleHolder sr : newRole.getSupplierRoles())
                    {
                        SupplierRoleTmpHolder srt = new SupplierRoleTmpHolder();
                        BeanUtils.copyProperties(sr, srt);
                        supplierRoleMapper.insert(srt);
                        supplierRoleTmpMapper.insert(srt);
                    }
                }
            }
            
            if (BigDecimal.valueOf(5).equals(oldRole.getUserTypeOid()) && null != oldRole.getBuyerOid())
            {
                // delete old supplier role.
                SupplierRoleTmpHolder srParam = new SupplierRoleTmpHolder();
                srParam.setRoleOid(oldRole.getRoleOid());
                
                supplierRoleTmpMapper.delete(srParam);
                supplierRoleMapper.delete(srParam);
                
                // insert new supplier role.
                if ( null != newRole.getSupplierRoles())
                {
                    for (SupplierRoleHolder sr : newRole.getSupplierRoles())
                    {
                        SupplierRoleTmpHolder srt = new SupplierRoleTmpHolder();
                        BeanUtils.copyProperties(sr, srt);
                        supplierRoleMapper.insert(srt);
                        supplierRoleTmpMapper.insert(srt);
                    }
                }
            }
            
        }
    }


    @Override
    public RoleHolder selectByKey(BigDecimal roleOid) throws Exception
    {
        RoleHolder param = new RoleHolder();
        param.setRoleOid(roleOid);
        
        List<RoleHolder> rlt = this.select(param);
        
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public void removeRoleProfile(CommonParameterHolder cp,
        RoleTmpHolder oldRole) throws Exception
    {
        if (cp.getMkMode())
        {
            // maker checker mode
            this.getMeBean().mkDelete(cp, oldRole);
        }
        else
        {
            // non maker checker mode
            
            // Remove records from role-user & t-role-user tables.
            RoleUserTmpHolder ruParam = new RoleUserTmpHolder();
            ruParam.setRoleOid(oldRole.getRoleOid());
            
            roleUserMapper.delete(ruParam);
            roleUserTmpMapper.delete(ruParam);
            
            
            // Remove records from role-group & t-role-group tables.
            RoleGroupTmpHolder rgParam = new RoleGroupTmpHolder();
            rgParam.setRoleOid(oldRole.getRoleOid());
            
            roleGroupMapper.delete(rgParam);
            roleGroupTmpMapper.delete(rgParam);
            
            // Remove records from role-operation & t-role-operation tables.
            RoleOperationTmpHolder roParam = new RoleOperationTmpHolder();
            roParam.setRoleOid(oldRole.getRoleOid());

            roleOperationMapper.delete(roParam);
            roleOperationTmpMapper.delete(roParam);
            
            // Remove records from supplier-role & t-supplier-role tables.
            SupplierRoleTmpHolder srParam = new SupplierRoleTmpHolder();
            srParam.setRoleOid(oldRole.getRoleOid());
            
            supplierRoleMapper.delete(srParam);
            supplierRoleTmpMapper.delete(srParam);
            
            this.getMeBean().auditDelete(cp, oldRole);
            roleTmpService.delete(oldRole);
            
        }
    }

    
    @Override
    public void approveRoleProfile(CommonParameterHolder cp, RoleTmpHolder tmpRole)
        throws Exception
    {
        if (DbActionType.CREATE.equals(tmpRole.getActionType()))
        {
            this.getMeBean().mkApprove(cp, null, tmpRole);
            return;
        }
        
        RoleHolder mainRole = this.selectByKey(tmpRole.getRoleOid());
        mainRole.setRoleOperations(roleOperationService.selectByRole(mainRole.getRoleOid()));
        if (RoleType.SUPPLIER.equals(mainRole.getRoleType()))
        {
            mainRole.setSupplierRoles(supplierRoleService.selectByRole(mainRole.getRoleOid()));
        }
        
        this.getMeBean().mkApprove(cp, mainRole, tmpRole);
    }


    @Override
    public void rejectRoleProfile(CommonParameterHolder cp,
        RoleTmpHolder tmpRole) throws Exception
    {
        if (DbActionType.CREATE.equals(tmpRole.getActionType()))
        {
            this.getMeBean().mkReject(cp, null, tmpRole);
            return;
        }
        
        RoleHolder mainRole = this.selectByKey(tmpRole.getRoleOid());
        mainRole.setRoleOperations(roleOperationService.selectByRole(mainRole.getRoleOid()));
        if (RoleType.SUPPLIER.equals(mainRole.getRoleType()))
        {
            mainRole.setSupplierRoles(supplierRoleService.selectByRole(mainRole.getRoleOid()));
        }
        
        this.getMeBean().mkReject(cp, mainRole, tmpRole);
    }


    @Override
    public void withdrawRoleProfile(CommonParameterHolder cp,
        RoleTmpHolder tmpRole) throws Exception
    {
        if (DbActionType.CREATE.equals(tmpRole.getActionType()))
        {
            this.getMeBean().mkWithdraw(cp, null, tmpRole);
            return;
        }
        
        RoleHolder mainRole = this.selectByKey(tmpRole.getRoleOid());
        mainRole.setRoleOperations(roleOperationService.selectByRole(mainRole.getRoleOid()));
        if (RoleType.SUPPLIER.equals(mainRole.getRoleType()))
        {
            mainRole.setSupplierRoles(supplierRoleService.selectByRole(mainRole.getRoleOid()));
        }
        
        this.getMeBean().mkWithdraw(cp, mainRole, tmpRole);
    }


   
    @Override
    public List<RoleHolder> selectRolesByTmpGroupOid(BigDecimal groupOid)
        throws Exception
    {
        if(groupOid == null )
        {
            throw new IllegalArgumentException();
        }
        
        return roleMapper.selectRolesByTmpGroupOid(groupOid);
    }


    @Override
    public List<RoleHolder> selectRolesByGroupOid(BigDecimal groupOid)
        throws Exception
    {
        if(groupOid == null)
        {
            throw new IllegalArgumentException();
        }
        return roleMapper.selectRolesByGroupOid(groupOid);
    }
    
    
    @Override
    public List<BigDecimal> selectSupplierOidsByRoleOid(BigDecimal roleOid)
        throws Exception
    {
        SupplierRoleHolder param = new SupplierRoleHolder();
        param.setRoleOid(roleOid);
        
        List<SupplierRoleHolder> srList = supplierRoleMapper.select(param);
        
        if (srList == null || srList.isEmpty())
        {
            return null;
        }
        
        List<BigDecimal> rlt = new ArrayList<BigDecimal>();
        
        for (SupplierRoleHolder sr : srList)
        {
            rlt.add(sr.getSupplierOid());
        }
        
        return rlt;
    }
    
    
    @Override
    public List<RoleHolder> selectByRoleId(String roleId) throws Exception
    {
        if(roleId == null || "".equals(roleId.trim()))
        {
            throw new IllegalArgumentException();
        }
        
        RoleHolder param = new RoleHolder();
        param.setRoleId(roleId);
        
        return this.select(param);
        
    }

}
