//*****************************************************************************
//
// File Name       :  DnHeaderExtendedServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.DnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.DnHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.DnHeaderExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DnHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<DnHeaderExtendedHolder> implements
    DnHeaderExtendedService
{
    @Autowired
    private DnHeaderExtendedMapper mapper;

    
    @Override
    public List<DnHeaderExtendedHolder> selectHeaderExtendedByKey(
        BigDecimal dnOid)
    {
        if (dnOid == null)
        {
            throw new IllegalArgumentException();
        }

        DnHeaderExtendedHolder parameter = new DnHeaderExtendedHolder();
        parameter.setDnOid(dnOid);

        List<DnHeaderExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(DnHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(DnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(DnHeaderExtendedHolder oldObj_,
        DnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(DnHeaderExtendedHolder oldObj_,
        DnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
