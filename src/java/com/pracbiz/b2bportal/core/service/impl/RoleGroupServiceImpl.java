//*****************************************************************************
//
// File Name       :  RoleGroupServiceImpl.java
// Date Created    :  Sep 6, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 6, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.RoleGroupHolder;
import com.pracbiz.b2bportal.core.mapper.RoleGroupMapper;
import com.pracbiz.b2bportal.core.service.RoleGroupService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class RoleGroupServiceImpl extends
    DBActionServiceDefaultImpl<RoleGroupHolder> implements RoleGroupService
{
    @Autowired
    private RoleGroupMapper mapper;

    @Override
    public List<RoleGroupHolder> selectRoleGroupByGroupOid(BigDecimal groupOid)
        throws Exception
    {
        if(groupOid == null)
        {
            throw new IllegalArgumentException();
        }

        RoleGroupHolder param = new RoleGroupHolder();
        param.setGroupOid(groupOid);

        return mapper.select(param);
    }

    
    @Override
    public void insert(RoleGroupHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    
    @Override
    public void updateByPrimaryKeySelective(RoleGroupHolder oldObj_,
        RoleGroupHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    
    @Override
    public void updateByPrimaryKey(RoleGroupHolder oldObj_,
        RoleGroupHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    
    @Override
    public void delete(RoleGroupHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<RoleGroupHolder> selectRoleGroupByRoleOid(BigDecimal roleOid)
            throws Exception
    {
        if (roleOid == null)
        {
            throw new IllegalArgumentException();
        }
        RoleGroupHolder param = new RoleGroupHolder();
        param.setRoleOid(roleOid);
        return mapper.select(param);
    }


    @Override
    public RoleGroupHolder selectByKey(BigDecimal groupOid, BigDecimal roleOid)
            throws Exception
    {
        if (groupOid == null || roleOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleGroupHolder param = new RoleGroupHolder();
        param.setGroupOid(groupOid);
        param.setRoleOid(roleOid);
        
        List<RoleGroupHolder> result = mapper.select(param);
        
        if (result == null || result.isEmpty())
        {
            return null;
        }
        
        return result.get(0);
    }

}
