package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.mapper.RoleOperationMapper;
import com.pracbiz.b2bportal.core.service.RoleOperationService;

public class RoleOperationServiceImpl extends DBActionServiceDefaultImpl<RoleOperationHolder>
    implements RoleOperationService
{
    @Autowired private RoleOperationMapper mapper;

    @Override
    public List<RoleOperationHolder> select(RoleOperationHolder param)
        throws Exception
    {
        return mapper.select(param);
    }
    

    @Override
    public void insert(RoleOperationHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }
    

    @Override
    public void updateByPrimaryKeySelective(RoleOperationHolder oldObj_,
        RoleOperationHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    
    @Override
    public void updateByPrimaryKey(RoleOperationHolder oldObj_,
        RoleOperationHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    
    @Override
    public void delete(RoleOperationHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    
    @Override
    public List<RoleOperationHolder> selectByRole(BigDecimal roleOid) throws Exception
    {
        if (null == roleOid)
        {
            throw new IllegalArgumentException();
        }
        
        RoleOperationHolder param = new RoleOperationHolder();
        param.setRoleOid(roleOid);
        
        return this.select(param);
    }

}
