//*****************************************************************************
//
// File Name       :  InvHeaderExtendedServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-11 $
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
import com.pracbiz.b2bportal.core.holder.InvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.InvHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.InvHeaderExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class InvHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<InvHeaderExtendedHolder> implements
    InvHeaderExtendedService
{
    @Autowired
    private InvHeaderExtendedMapper mapper;

    
    @Override
    public List<InvHeaderExtendedHolder> selectHeaderExtendedByKey(
        BigDecimal invOid)
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }

        InvHeaderExtendedHolder parameter = new InvHeaderExtendedHolder();
        parameter.setInvOid(invOid);

        List<InvHeaderExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    
    }

    @Override
    public void delete(InvHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(InvHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(InvHeaderExtendedHolder oldObj_,
        InvHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(InvHeaderExtendedHolder oldObj_,
        InvHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
