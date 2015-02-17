//*****************************************************************************
//
// File Name       :  SupplierRoleTmpServiceImpl.java
// Date Created    :  Mar 6, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Mar 6, 2013 9:44:33 PM$
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
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;
import com.pracbiz.b2bportal.core.mapper.SupplierRoleTmpMapper;
import com.pracbiz.b2bportal.core.service.SupplierRoleTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class SupplierRoleTmpServiceImpl extends
        DBActionServiceDefaultImpl<SupplierRoleTmpHolder> implements
        SupplierRoleTmpService
{
    @Autowired
    private SupplierRoleTmpMapper mapper;


    @Override
    public List<SupplierRoleTmpHolder> select(SupplierRoleTmpHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<SupplierRoleTmpHolder> selectByRole(BigDecimal roleOid)
            throws Exception
    {
        if (roleOid == null)
        {
            throw new IllegalArgumentException();
        }

        SupplierRoleTmpHolder param = new SupplierRoleTmpHolder();
        param.setRoleOid(roleOid);

        return this.select(param);
    }


    @Override
    public void delete(SupplierRoleTmpHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(SupplierRoleTmpHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(SupplierRoleTmpHolder oldObj,
            SupplierRoleTmpHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKeySelective(SupplierRoleTmpHolder oldObj,
            SupplierRoleTmpHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
