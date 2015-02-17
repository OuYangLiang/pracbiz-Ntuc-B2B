//*****************************************************************************
//
// File Name       :  RtvDetailServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.mapper.RtvDetailMapper;
import com.pracbiz.b2bportal.core.service.RtvDetailService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class RtvDetailServiceImpl extends
    DBActionServiceDefaultImpl<RtvDetailHolder> implements RtvDetailService
{
    @Autowired
    private RtvDetailMapper mapper;
    

    @Override
    public List<RtvDetailHolder> selectRtvDetailByKey(BigDecimal rtvOid)
        throws Exception
    {
        if (rtvOid == null)
        {
            throw new IllegalArgumentException();
        }

        RtvDetailHolder parameter = new RtvDetailHolder();
        parameter.setRtvOid(rtvOid);

        List<RtvDetailHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(RtvDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(RtvDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(RtvDetailHolder oldObj_,
        RtvDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(RtvDetailHolder oldObj_,
        RtvDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
