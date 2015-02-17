//*****************************************************************************
//
// File Name       :  RoleGroupTemServiceImpl.java
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

import com.pracbiz.b2bportal.core.holder.RoleGroupTmpHolder;
import com.pracbiz.b2bportal.core.mapper.RoleGroupTmpMapper;
import com.pracbiz.b2bportal.core.service.RoleGroupTmpService;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class RoleGroupTmpServiceImpl implements RoleGroupTmpService
{
    @Autowired
    private RoleGroupTmpMapper mapper;
    
    @Override
    public List<RoleGroupTmpHolder> selectRoleGroupTmpByGroupOid(BigDecimal groupOid)
        throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleGroupTmpHolder param = new RoleGroupTmpHolder();
        param.setGroupOid(groupOid);
        
        return mapper.select(param);
    }

    
    @Override
    public List<RoleGroupTmpHolder> selectRoleGroupTmpByRoleOid(
            BigDecimal roleOid) throws Exception
    {
        if (roleOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleGroupTmpHolder param = new RoleGroupTmpHolder();
        param.setRoleOid(roleOid);
        
        return mapper.select(param);
    }
    
    
    @Override
    public RoleGroupTmpHolder selectByKey(BigDecimal groupOid, BigDecimal roleOid)
            throws Exception
    {
        if (groupOid == null || roleOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleGroupTmpHolder param = new RoleGroupTmpHolder();
        param.setGroupOid(groupOid);
        param.setRoleOid(roleOid);
        
        List<RoleGroupTmpHolder> result = mapper.select(param);
        
        if (result == null || result.isEmpty())
        {
            return null;
        }
        
        return result.get(0);
    }
}
