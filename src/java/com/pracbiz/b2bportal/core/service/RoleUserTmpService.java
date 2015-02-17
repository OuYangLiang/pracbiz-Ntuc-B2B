package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface RoleUserTmpService extends BaseService<RoleUserTmpHolder>
{
    public List<RoleUserTmpHolder> selectRoleUserTmpByUserOid(BigDecimal userOid)
        throws Exception;
    
    
    public List<RoleUserTmpHolder> selectRoleUserTmpByRoleOid(BigDecimal roleOid)
            throws Exception;
    
    public RoleUserTmpHolder selectByKey(BigDecimal userOid, BigDecimal roleOid)
            throws Exception;

}
