//*****************************************************************************
//
// File Name       :  SupplierRoleServiceImpl.java
// Date Created    :  Mar 6, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Mar 6, 2013 9:58:15 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SupplierRoleHolder;
import com.pracbiz.b2bportal.core.mapper.SupplierRoleMapper;
import com.pracbiz.b2bportal.core.service.SupplierRoleService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class SupplierRoleServiceImpl extends
        DBActionServiceDefaultImpl<SupplierRoleHolder> implements
        SupplierRoleService
{
    @Autowired
    private SupplierRoleMapper mapper;


    @Override
    public List<SupplierRoleHolder> select(SupplierRoleHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<SupplierRoleHolder> selectByRole(BigDecimal roleOid)
            throws Exception
    {
        if (roleOid == null)
        {
            throw new IllegalArgumentException();
        }

        SupplierRoleHolder param = new SupplierRoleHolder();
        param.setRoleOid(roleOid);

        return this.select(param);
    }


    @Override
    public void delete(SupplierRoleHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(SupplierRoleHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(SupplierRoleHolder oldObj,
            SupplierRoleHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKeySelective(SupplierRoleHolder oldObj,
            SupplierRoleHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public SupplierRoleHolder selectByRoleAndSupplier(BigDecimal roleOid,
            BigDecimal supplierOid) throws Exception
    {
        if (roleOid == null || supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        SupplierRoleHolder param = new SupplierRoleHolder();
        param.setRoleOid(roleOid);
        param.setSupplierOid(supplierOid);
        List<SupplierRoleHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

}
