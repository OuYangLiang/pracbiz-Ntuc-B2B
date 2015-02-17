package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.RoleTmpExHolder;

public interface RoleTmpMapper extends BaseMapper<RoleTmpHolder>,
    DBActionMapper<RoleTmpHolder>, PaginatingMapper<RoleTmpExHolder>
{
    RoleTmpHolder selectAdminRoleById(String roleId);

    List<RoleTmpHolder> selectRolesByUserOid(BigDecimal userOid);
    
    RoleTmpHolder selectSupplierRoleById(Map<String, Object> param);
}