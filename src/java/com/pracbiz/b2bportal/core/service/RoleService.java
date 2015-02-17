package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.MakerCheckerService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.RoleTmpExHolder;

public interface RoleService extends BaseService<RoleHolder>,
    DBActionService<RoleHolder>,
    MakerCheckerService<RoleHolder, RoleTmpHolder>,
    PaginatingService<RoleTmpExHolder>
{
    public RoleHolder selectAdminRoleById(String roleId) throws Exception;


    public RoleHolder selectBuyerRoleById(BigDecimal buyerOid, String roleId)
            throws Exception;


    public RoleHolder selectSupplierRoleById(BigDecimal supplierOid,
            String roleId) throws Exception;

    
    public List<RoleHolder> selectBuyerRolesByBuyerOidAndUserType(
            BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception;


    public List<RoleHolder> selectSupplierRolesBySupplierOidAndUserType(
            BigDecimal supplierOid, BigDecimal userTypeOid) throws Exception;

    
    public List<RoleHolder> selectRolesByUserType(BigDecimal userTypeOid) throws Exception;
    
    
    public RoleHolder selectByKey(BigDecimal roleOid) throws Exception;

    
    public void createRoleProfile(CommonParameterHolder cp, RoleTmpHolder role)
            throws Exception;


    public List<RoleHolder> selectRolesByUserOid(BigDecimal userOid) throws Exception;

    
    public void updateRoleProfile(CommonParameterHolder cp,
        RoleTmpHolder oldRole, RoleTmpHolder newRole) throws Exception;
    
    
    public void removeRoleProfile(CommonParameterHolder cp,
        RoleTmpHolder oldRole) throws Exception;
    
    
    public void approveRoleProfile(CommonParameterHolder cp, RoleTmpHolder tmpRole)
        throws Exception;
    
    
    public void rejectRoleProfile(CommonParameterHolder cp, RoleTmpHolder tmpRole)
        throws Exception;
    
    
    public void withdrawRoleProfile(CommonParameterHolder cp, RoleTmpHolder tmpRole)
        throws Exception;

    
    public List<RoleHolder> selectRolesByGroupOid(BigDecimal groupOid) throws Exception;
    
    
    public List<RoleHolder> selectRolesByTmpGroupOid(BigDecimal groupOid) throws Exception;

    
    public List<BigDecimal> selectSupplierOidsByRoleOid(BigDecimal roleOid)
        throws Exception;
    

    public List<RoleHolder> selectByRoleId(String roleId) throws Exception;

}
