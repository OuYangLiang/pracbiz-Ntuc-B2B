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
import com.pracbiz.b2bportal.core.holder.PoLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.PoLocationDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.PoLocationDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PoLocationDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<PoLocationDetailExtendedHolder> implements
    PoLocationDetailExtendedService
{
    @Autowired
    private PoLocationDetailExtendedMapper mapper;
    

    @Override
    public List<PoLocationDetailExtendedHolder> selectPoLocationDetailExtendedsByPoOid(
        BigDecimal poOid)
    {
        if (poOid == null)
        {
            throw new IllegalArgumentException();
        }

        PoLocationDetailExtendedHolder parameter = new PoLocationDetailExtendedHolder();
        parameter.setPoOid(poOid);

        List<PoLocationDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    
    }

    @Override
    public void delete(PoLocationDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(PoLocationDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
        
    }

    @Override
    public void updateByPrimaryKey(PoLocationDetailExtendedHolder oldObj_,
        PoLocationDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PoLocationDetailExtendedHolder oldObj_,
        PoLocationDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
