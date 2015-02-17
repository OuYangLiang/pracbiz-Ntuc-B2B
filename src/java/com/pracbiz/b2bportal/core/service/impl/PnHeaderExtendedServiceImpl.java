//*****************************************************************************
//
// File Name       :  PnHeaderExtendedServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.PnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.PnHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.PnHeaderExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PnHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<PnHeaderExtendedHolder> implements
    PnHeaderExtendedService
{
    @Autowired
    private PnHeaderExtendedMapper mapper;

    
    
    @Override
    public List<PnHeaderExtendedHolder> selectHeaderExtendedByKey(
        BigDecimal pnOid)
    {
        if (pnOid == null)
        {
            throw new IllegalArgumentException();
        }

        PnHeaderExtendedHolder parameter = new PnHeaderExtendedHolder();
        parameter.setPnOid(pnOid);

        List<PnHeaderExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    
    }

    @Override
    public void delete(PnHeaderExtendedHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }

    @Override
    public void insert(PnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PnHeaderExtendedHolder oldObj,
        PnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PnHeaderExtendedHolder oldObj,
        PnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
