package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.RoleHolder;

public interface RoleMapper extends BaseMapper<RoleHolder>,
    DBActionMapper<RoleHolder>
{
    List<RoleHolder> selectRolesByUserOid(BigDecimal userOid);
    
    
    List<RoleHolder> selectRolesByTmpGroupOid(BigDecimal groupOid);


    List<RoleHolder> selectRolesByGroupOid(BigDecimal groupOid);
    
    
    RoleHolder selectAdminRoleById(String roleId);
    
    
    RoleHolder selectSupplierRoleById(Map<String, Object> param);
    
    
    List<RoleHolder> selectSupplierRolesBySupplierOidAndUserType(Map<String, BigDecimal> param);
}