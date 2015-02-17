package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface RoleUserService extends BaseService<RoleUserHolder>
{
    public List<RoleUserHolder> selectRoleUserByUserOid(BigDecimal userOid)
        throws Exception;
    
    
    public List<RoleUserHolder> selectRoleUserByRoleOid(BigDecimal roleOid)
            throws Exception;
    

    public RoleUserHolder selectByKey(BigDecimal userOid, BigDecimal roleOid)
            throws Exception;

}
