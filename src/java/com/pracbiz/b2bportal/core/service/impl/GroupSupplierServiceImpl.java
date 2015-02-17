//*****************************************************************************
//
// File Name       :  GroupSupplierServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.mapper.GroupSupplierMapper;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class GroupSupplierServiceImpl extends
    DBActionServiceDefaultImpl<GroupSupplierHolder> implements
    GroupSupplierService
{
    @Autowired
    private GroupSupplierMapper mapper;


    @Override
    public List<GroupSupplierHolder> select(GroupSupplierHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<GroupSupplierHolder> selectGroupSupplierByGroupOid(BigDecimal groupOid)
            throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupSupplierHolder param = new GroupSupplierHolder();
        param.setGroupOid(groupOid);
        return mapper.select(param);
    }


    @Override
    public void delete(GroupSupplierHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(GroupSupplierHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(GroupSupplierHolder oldObj,
        GroupSupplierHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);

    }


    @Override
    public void updateByPrimaryKeySelective(GroupSupplierHolder oldObj,
        GroupSupplierHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }

}
