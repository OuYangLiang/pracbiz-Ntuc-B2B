//*****************************************************************************
//
// File Name       :  GrnHeaderExtendedServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-27 $
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
import com.pracbiz.b2bportal.core.holder.GrnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.GrnHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.GrnHeaderExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GrnHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<GrnHeaderExtendedHolder> implements
    GrnHeaderExtendedService, BaseService<GrnHeaderExtendedHolder>
{
    @Autowired
    private GrnHeaderExtendedMapper mapper;

    @Override
    public List<GrnHeaderExtendedHolder> selectHeaderExtendedByKey(
        BigDecimal grnOid)
    {
        if (grnOid == null)
        {
            throw new IllegalArgumentException();
        }

        GrnHeaderExtendedHolder parameter = new GrnHeaderExtendedHolder();
        parameter.setGrnOid(grnOid);

        List<GrnHeaderExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(GrnHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(GrnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(GrnHeaderExtendedHolder oldObj_,
        GrnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(GrnHeaderExtendedHolder oldObj_,
        GrnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    @Override
    public List<GrnHeaderExtendedHolder> select(GrnHeaderExtendedHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

}
