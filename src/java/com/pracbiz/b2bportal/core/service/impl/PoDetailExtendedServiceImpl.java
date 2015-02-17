//*****************************************************************************
//
// File Name       :  PoDetailExtendedServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.PoDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.PoDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PoDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<PoDetailExtendedHolder> implements
    PoDetailExtendedService
{
    @Autowired
    private PoDetailExtendedMapper mapper;

    @Override
    public List<PoDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal poOid)throws Exception
    {
        if (poOid == null)
        {
            throw new IllegalArgumentException();
        }

        PoDetailExtendedHolder parameter = new PoDetailExtendedHolder();
        parameter.setPoOid(poOid);

        List<PoDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public List<PoDetailExtendedHolder> selectDetailExtendedByKeyAndLineSeqNo(
        BigDecimal poOid, Integer lineSeqNo)throws Exception
    {
        if (poOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }

        PoDetailExtendedHolder parameter = new PoDetailExtendedHolder();
        parameter.setPoOid(poOid);
        parameter.setLineSeqNo(lineSeqNo);

        List<PoDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }


    @Override
    public void delete(PoDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(PoDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PoDetailExtendedHolder oldObj_,
        PoDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PoDetailExtendedHolder oldObj_,
        PoDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
