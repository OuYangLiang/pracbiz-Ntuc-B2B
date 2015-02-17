package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;

public interface RoleTmpService extends BaseService<RoleTmpHolder>,
    DBActionService<RoleTmpHolder>
{
    public RoleTmpHolder selectAdminRoleById(String roleId) throws Exception;

    
    public RoleTmpHolder selectBuyerRoleById(BigDecimal buyerOid, String roleId)
        throws Exception;

    
    public RoleTmpHolder selectSupplierRoleById(BigDecimal supplierOid,
        String roleId) throws Exception;

    
    public List<RoleTmpHolder> selectRolesByUserType(BigDecimal userTypeOid)
        throws Exception;

    
    public RoleTmpHolder selectRoleByKey(BigDecimal roleOid) throws Exception;

    
    public List<RoleTmpHolder> selectRolesByUserOid(BigDecimal userOid)
        throws Exception;

    
    public List<RoleTmpHolder> selectRolesByRoleOids(List<BigDecimal> roleOids)//???
        throws Exception;
    
    
    public List<BigDecimal> selectSupplierOidsByRoleOid(BigDecimal roleOid)
        throws Exception;
}
