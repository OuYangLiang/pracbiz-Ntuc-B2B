//*****************************************************************************
//
// File Name       :  PnDetailExtendedServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.PnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.PnDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.PnDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PnDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<PnDetailExtendedHolder> implements
    PnDetailExtendedService
{
    @Autowired
    private PnDetailExtendedMapper mapper;
    

    
    
    @Override
    public List<PnDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal pnOid)throws Exception
    {
        if (pnOid == null)
        {
            throw new IllegalArgumentException();
        }

        PnDetailExtendedHolder parameter = new PnDetailExtendedHolder();
        parameter.setPnOid(pnOid);

        List<PnDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public List<PnDetailExtendedHolder> selectDetailExtendedByKeyAndLineSeqNo(
        BigDecimal pnOid, Integer lineSeqNo)throws Exception
    {
        if (pnOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }

        PnDetailExtendedHolder parameter = new PnDetailExtendedHolder();
        parameter.setPnOid(pnOid);
        parameter.setLineSeqNo(lineSeqNo);

        List<PnDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(PnDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(PnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PnDetailExtendedHolder oldObj_,
        PnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PnDetailExtendedHolder oldObj_,
        PnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
