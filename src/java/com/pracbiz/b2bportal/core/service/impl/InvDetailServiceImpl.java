//*****************************************************************************
//
// File Name       :  InvDetailServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.mapper.InvDetailMapper;
import com.pracbiz.b2bportal.core.service.InvDetailService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class InvDetailServiceImpl extends
    DBActionServiceDefaultImpl<InvDetailHolder> implements InvDetailService
{
    @Autowired
    private InvDetailMapper mapper;
    

    @Override
    public List<InvDetailHolder> selectInvDetailByKey(BigDecimal invOid)
        throws Exception
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }

        InvDetailHolder parameter = new InvDetailHolder();
        parameter.setInvOid(invOid);

        List<InvDetailHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(InvDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(InvDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKey(InvDetailHolder oldObj_,
        InvDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(InvDetailHolder oldObj_,
        InvDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
