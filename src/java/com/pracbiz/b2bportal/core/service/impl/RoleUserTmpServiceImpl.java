package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.mapper.RoleUserTmpMapper;
import com.pracbiz.b2bportal.core.service.RoleUserTmpService;

/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */
public class RoleUserTmpServiceImpl implements RoleUserTmpService
{
    @Autowired private RoleUserTmpMapper mapper;
    
    @Override
    public List<RoleUserTmpHolder> select(RoleUserTmpHolder param)
        throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<RoleUserTmpHolder> selectRoleUserTmpByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleUserTmpHolder param = new RoleUserTmpHolder();
        param.setUserOid(userOid);
        
        return select(param);
    }


    @Override
    public List<RoleUserTmpHolder> selectRoleUserTmpByRoleOid(BigDecimal roleOid)
            throws Exception
    {
        if (roleOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleUserTmpHolder param = new RoleUserTmpHolder();
        param.setRoleOid(roleOid);
        
        return select(param);
    }

    
    @Override
    public RoleUserTmpHolder selectByKey(BigDecimal userOid, BigDecimal roleOid)
            throws Exception
    {
        if (userOid == null || roleOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleUserTmpHolder param = new RoleUserTmpHolder();
        param.setUserOid(userOid);
        param.setRoleOid(roleOid);
        
        List<RoleUserTmpHolder> result = select(param);
        
        if (result == null || result.isEmpty())
        {
            return null;
        }
        
        return result.get(0);
    }
}
