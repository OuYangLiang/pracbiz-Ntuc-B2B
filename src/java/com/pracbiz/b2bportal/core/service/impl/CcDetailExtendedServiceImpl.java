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
import com.pracbiz.b2bportal.core.holder.CcDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.CcDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.CcDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CcDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<CcDetailExtendedHolder> implements
    CcDetailExtendedService
{
    @Autowired
    private CcDetailExtendedMapper mapper;


    @Override
    public void delete(CcDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(CcDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(CcDetailExtendedHolder oldObj_,
        CcDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(CcDetailExtendedHolder oldObj_,
        CcDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public List<CcDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal invOid)
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }

        CcDetailExtendedHolder parameter = new CcDetailExtendedHolder();
        parameter.setInvOid(invOid);

        List<CcDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public List<CcDetailExtendedHolder> selectDetailExtendedByKeyAndLineSeqNo(
        BigDecimal invOid, Integer lineSeqNo)
    {
        if (invOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }

        CcDetailExtendedHolder parameter = new CcDetailExtendedHolder();
        parameter.setInvOid(invOid);
        parameter.setLineSeqNo(lineSeqNo);

        List<CcDetailExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

}
