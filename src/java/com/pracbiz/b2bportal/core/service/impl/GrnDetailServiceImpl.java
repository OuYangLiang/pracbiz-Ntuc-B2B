//*****************************************************************************
//
// File Name       :  GrnDetailServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.mapper.GrnDetailMapper;
import com.pracbiz.b2bportal.core.service.GrnDetailService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GrnDetailServiceImpl extends
    DBActionServiceDefaultImpl<GrnDetailHolder> implements GrnDetailService
{
    @Autowired
    private GrnDetailMapper mapper;

    
    @Override
    public List<GrnDetailHolder> selectGrnDetailByKey(BigDecimal grnOid)
        throws Exception
    {
        if (grnOid == null)
        {
            throw new IllegalArgumentException();
        }

        GrnDetailHolder parameter = new GrnDetailHolder();
        parameter.setGrnOid(grnOid);

        List<GrnDetailHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(GrnDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(GrnDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(GrnDetailHolder oldObj_,
        GrnDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(GrnDetailHolder oldObj_,
        GrnDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    @Override
    public List<GrnDetailHolder> select(GrnDetailHolder param) throws Exception
    {
        return mapper.select(param);
    }

}
