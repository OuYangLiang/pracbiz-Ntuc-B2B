//*****************************************************************************
//
// File Name       :  PoLocationExtendedServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2012-12-11 $
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
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.RtvLocationDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author yinchi
 */
public class RtvLocationDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<RtvLocationDetailExtendedHolder> implements
    RtvLocationDetailExtendedService
{
    @Autowired
    private RtvLocationDetailExtendedMapper mapper;
    


    @Override
    public void delete(RtvLocationDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(RtvLocationDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
        
    }

    @Override
    public void updateByPrimaryKey(RtvLocationDetailExtendedHolder oldObj_,
        RtvLocationDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(RtvLocationDetailExtendedHolder oldObj_,
        RtvLocationDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public List<RtvLocationDetailExtendedHolder> selectRtvLocationDetailExByRtvOid(
        BigDecimal rtvOid) throws Exception
    {
        if (rtvOid == null)
        {
            throw new IllegalArgumentException();
        }

        RtvLocationDetailExtendedHolder parameter = new RtvLocationDetailExtendedHolder();
        parameter.setRtvOid(rtvOid);

        List<RtvLocationDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    
    }

}
