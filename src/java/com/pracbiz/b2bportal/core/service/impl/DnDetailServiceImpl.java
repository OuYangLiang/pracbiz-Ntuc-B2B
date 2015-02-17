//*****************************************************************************
//
// File Name       :  DnDetailServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.mapper.DnDetailMapper;
import com.pracbiz.b2bportal.core.service.DnDetailService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DnDetailServiceImpl extends
    DBActionServiceDefaultImpl<DnDetailExHolder> implements DnDetailService
{
    @Autowired
    private DnDetailMapper mapper;

    
    @Override
    public List<DnDetailExHolder> selectDnDetailByKey(BigDecimal dnOid)
        throws Exception
    {
        if (dnOid == null)
        {
            throw new IllegalArgumentException();
        }

        DnDetailExHolder parameter = new DnDetailExHolder();
        parameter.setDnOid(dnOid);

        List<DnDetailExHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    
    }

    @Override
    public void delete(DnDetailExHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(DnDetailExHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(DnDetailExHolder oldObj_,
        DnDetailExHolder newObj_) throws Exception
    {
       mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(DnDetailExHolder oldObj_,
        DnDetailExHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
