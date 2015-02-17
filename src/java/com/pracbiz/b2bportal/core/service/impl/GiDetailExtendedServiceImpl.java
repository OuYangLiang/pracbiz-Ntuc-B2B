//*****************************************************************************
//
// File Name       :  GrnDetailExtendedServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2012-12-11$
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

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.GiDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.GiDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.GiDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GiDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<GiDetailExtendedHolder> implements
    GiDetailExtendedService,BaseService<GiDetailExtendedHolder>
{
    @Autowired
    private GiDetailExtendedMapper mapper;

    
    @Override
    public void delete(GiDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    
    @Override
    public void insert(GiDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    
    @Override
    public void updateByPrimaryKey(GiDetailExtendedHolder oldObj,
        GiDetailExtendedHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    
    @Override
    public void updateByPrimaryKeySelective(GiDetailExtendedHolder oldObj,
        GiDetailExtendedHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    
    @Override
    public List<GiDetailExtendedHolder> select(GiDetailExtendedHolder param)
        throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public List<GiDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal giOid)throws Exception
    {
        if (giOid == null)
        {
            throw new IllegalArgumentException();
        }

        GiDetailExtendedHolder parameter = new GiDetailExtendedHolder();
        parameter.setGiOid(giOid);

        List<GiDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }


}
