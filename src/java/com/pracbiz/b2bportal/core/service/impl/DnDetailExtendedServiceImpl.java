//*****************************************************************************
//
// File Name       :  DnDetailExtendedServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.DnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.DnDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.DnDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DnDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<DnDetailExtendedHolder> implements
    DnDetailExtendedService
{
    @Autowired
    private DnDetailExtendedMapper mapper;
    
    @Override
    public List<DnDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal dnOid) throws Exception
    {
        if (dnOid == null)
        {
            throw new IllegalArgumentException();
        }

        DnDetailExtendedHolder parameter = new DnDetailExtendedHolder();
        parameter.setDnOid(dnOid);

        List<DnDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public List<DnDetailExtendedHolder> selectDetailExtendedByKeyAndLineSeqNo(
        BigDecimal dnOid, Integer lineSeqNo)
    {

        if (dnOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }

        DnDetailExtendedHolder parameter = new DnDetailExtendedHolder();
        parameter.setDnOid(dnOid);
        parameter.setLineSeqNo(lineSeqNo);

        List<DnDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    
    }

    @Override
    public void delete(DnDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(DnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(DnDetailExtendedHolder oldObj_,
        DnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(DnDetailExtendedHolder oldObj_,
        DnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
