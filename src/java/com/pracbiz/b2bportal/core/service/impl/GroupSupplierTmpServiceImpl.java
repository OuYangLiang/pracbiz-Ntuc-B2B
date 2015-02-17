//*****************************************************************************
//
// File Name       :  GroupSupplierTmpServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.GroupSupplierTmpHolder;
import com.pracbiz.b2bportal.core.mapper.GroupSupplierTmpMapper;
import com.pracbiz.b2bportal.core.service.GroupSupplierTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class GroupSupplierTmpServiceImpl extends DBActionServiceDefaultImpl<GroupSupplierTmpHolder>
        implements GroupSupplierTmpService
{
    @Autowired
    private GroupSupplierTmpMapper mapper;


    @Override
    public List<GroupSupplierTmpHolder> selectGroupSupplierTmpByGroupOid(
        BigDecimal groupOid) throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupSupplierTmpHolder param = new GroupSupplierTmpHolder();
        param.setGroupOid(groupOid);
        return mapper.select(param);
    }


    @Override
    public void delete(GroupSupplierTmpHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(GroupSupplierTmpHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(GroupSupplierTmpHolder oldObj,
        GroupSupplierTmpHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(GroupSupplierTmpHolder oldObj,
        GroupSupplierTmpHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }

}
