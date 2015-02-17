package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.mapper.RoleUserMapper;
import com.pracbiz.b2bportal.core.service.RoleUserService;

/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */
public class RoleUserServiceImpl implements RoleUserService
{
    @Autowired private RoleUserMapper mapper;
    
    @Override
    public List<RoleUserHolder> select(RoleUserHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<RoleUserHolder> selectRoleUserByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleUserHolder param = new RoleUserHolder();
        param.setUserOid(userOid);
        
        return select(param);
    }


    @Override
    public List<RoleUserHolder> selectRoleUserByRoleOid(BigDecimal roleOid)
            throws Exception
    {
        if (roleOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleUserHolder param = new RoleUserHolder();
        param.setRoleOid(roleOid);
        
        return select(param);
    }


    @Override
    public RoleUserHolder selectByKey(BigDecimal userOid, BigDecimal roleOid)
            throws Exception
    {
        if (userOid == null || roleOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleUserHolder param = new RoleUserHolder();
        param.setUserOid(userOid);
        param.setRoleOid(roleOid);
        
        List<RoleUserHolder> result = select(param);
        
        if (result == null || result.isEmpty())
        {
            return null;
        }
        
        return result.get(0);
    }

}
