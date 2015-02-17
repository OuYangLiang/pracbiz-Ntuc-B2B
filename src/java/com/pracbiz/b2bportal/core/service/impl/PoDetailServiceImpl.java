//*****************************************************************************
//
// File Name       :  PoDetailServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-11$
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
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.mapper.PoDetailMapper;
import com.pracbiz.b2bportal.core.service.PoDetailService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PoDetailServiceImpl extends
    DBActionServiceDefaultImpl<PoDetailHolder> implements PoDetailService
{
    @Autowired
    private PoDetailMapper mapper;

    @Override
    public void delete(PoDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(PoDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PoDetailHolder oldObj_,
        PoDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PoDetailHolder oldObj_,
        PoDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public List<PoDetailHolder> selectPoDetailsByPoOid(BigDecimal poOid)
        throws Exception
    {
        if (poOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoDetailHolder parameter = new PoDetailHolder();
        parameter.setPoOid(poOid);
        
        return mapper.select(parameter);
    }

}
