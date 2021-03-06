//*****************************************************************************
//
// File Name       :  RtvDetailExtendedServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.RtvDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.RtvDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class RtvDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<RtvDetailExtendedHolder> implements
    RtvDetailExtendedService
{
    @Autowired
    private RtvDetailExtendedMapper mapper;

    @Override
    public List<RtvDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal rtvOid)
    {

        if(rtvOid == null)
        {
            throw new IllegalArgumentException();
        }

        RtvDetailExtendedHolder parameter = new RtvDetailExtendedHolder();
        parameter.setRtvOid(rtvOid);

        List<RtvDetailExtendedHolder> rlts = mapper.select(parameter);

        if(rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(RtvDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(RtvDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(RtvDetailExtendedHolder oldObj_,
        RtvDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(RtvDetailExtendedHolder oldObj_,
        RtvDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public List<RtvDetailExtendedHolder> selectDetailExtendedByRtvOidAndLineSeqNo(
        BigDecimal rtvOid, Integer lineSeqNo)
    {
        if(rtvOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }

        RtvDetailExtendedHolder parameter = new RtvDetailExtendedHolder();
        parameter.setRtvOid(rtvOid);
        parameter.setLineSeqNo(lineSeqNo);

        List<RtvDetailExtendedHolder> rlts = mapper.select(parameter);

        if(rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

}
