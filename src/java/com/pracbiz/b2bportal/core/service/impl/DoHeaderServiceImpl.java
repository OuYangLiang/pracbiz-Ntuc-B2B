//*****************************************************************************
//
// File Name       :  DoHeaderServiceImpl.java
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.DoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.DoSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.DoHeaderMapper;
import com.pracbiz.b2bportal.core.service.DoHeaderService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DoHeaderServiceImpl extends
    DBActionServiceDefaultImpl<DoHeaderHolder> implements DoHeaderService
{
    @Autowired
    private DoHeaderMapper mapper;

    @Override
    public int getCountOfSummary(MsgTransactionsExHolder param)
        throws Exception
    {
        if(!(param instanceof DoSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getCountOfSummary((DoSummaryHolder)param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof DoSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary((DoSummaryHolder)param);
    }

    @Override
    public void delete(DoHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(DoHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(DoHeaderHolder oldObj_,
        DoHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(DoHeaderHolder oldObj_,
        DoHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
