//*****************************************************************************
//
// File Name       :  InvDetailExtendedServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.InvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.InvDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.InvDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class InvDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<InvDetailExtendedHolder> implements
    InvDetailExtendedService
{
    @Autowired
    private InvDetailExtendedMapper mapper;


    @Override
    public void delete(InvDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(InvDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(InvDetailExtendedHolder oldObj_,
        InvDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(InvDetailExtendedHolder oldObj_,
        InvDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public List<InvDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal invOid)
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }

        InvDetailExtendedHolder parameter = new InvDetailExtendedHolder();
        parameter.setInvOid(invOid);

        List<InvDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public List<InvDetailExtendedHolder> selectDetailExtendedByKeyAndLineSeqNo(
        BigDecimal invOid, Integer lineSeqNo)
    {
        if (invOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }

        InvDetailExtendedHolder parameter = new InvDetailExtendedHolder();
        parameter.setInvOid(invOid);
        parameter.setLineSeqNo(lineSeqNo);

        List<InvDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

}
