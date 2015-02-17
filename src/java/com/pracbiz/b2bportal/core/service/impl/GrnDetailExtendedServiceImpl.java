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
import com.pracbiz.b2bportal.core.holder.GrnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.GrnDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.GrnDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GrnDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<GrnDetailExtendedHolder> implements
    GrnDetailExtendedService,BaseService<GrnDetailExtendedHolder>
{
    @Autowired
    private GrnDetailExtendedMapper mapper;

    @Override
    public void delete(GrnDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(GrnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(GrnDetailExtendedHolder oldObj_,
        GrnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(GrnDetailExtendedHolder oldObj_,
        GrnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    @Override
    public List<GrnDetailExtendedHolder> select(GrnDetailExtendedHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public List<GrnDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal grnOid)
    {
        if (grnOid == null)
        {
            throw new IllegalArgumentException();
        }

        GrnDetailExtendedHolder parameter = new GrnDetailExtendedHolder();
        parameter.setGrnOid(grnOid);

        List<GrnDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public List<GrnDetailExtendedHolder> selectDetailExtendedByGrnOidAndLineSeqNo(
        BigDecimal grnOid, Integer lineSeqNo)
    {
        if (grnOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }

        GrnDetailExtendedHolder parameter = new GrnDetailExtendedHolder();
        parameter.setGrnOid(grnOid);
        parameter.setLineSeqNo(lineSeqNo);

        List<GrnDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }
    
    

}
