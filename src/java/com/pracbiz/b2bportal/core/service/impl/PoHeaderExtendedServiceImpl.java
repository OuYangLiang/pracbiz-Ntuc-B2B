//*****************************************************************************
//
// File Name       :  PoHeaderExtendedServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-11$
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
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.PoHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.PoHeaderExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PoHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<PoHeaderExtendedHolder> implements
    PoHeaderExtendedService
{
    @Autowired
    private PoHeaderExtendedMapper mapper;
    

    @Override
    public List<PoHeaderExtendedHolder> selectHeaderExtendedByKey(
        BigDecimal poOid)
    {
        if (poOid == null)
        {
            throw new IllegalArgumentException();
        }

        PoHeaderExtendedHolder parameter = new PoHeaderExtendedHolder();
        parameter.setPoOid(poOid);

        List<PoHeaderExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    
    }

    @Override
    public void delete(PoHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(PoHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PoHeaderExtendedHolder oldObj_,
        PoHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PoHeaderExtendedHolder oldObj_,
        PoHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
