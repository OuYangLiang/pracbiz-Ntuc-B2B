//*****************************************************************************
//
// File Name       :  PnDetailServieImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-27 $
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
import com.pracbiz.b2bportal.core.holder.PnDetailHolder;
import com.pracbiz.b2bportal.core.mapper.PnDetailMapper;
import com.pracbiz.b2bportal.core.service.PnDetailService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PnDetailServiceImpl extends
    DBActionServiceDefaultImpl<PnDetailHolder> implements PnDetailService
{
    @Autowired
    private PnDetailMapper mapper;
    

    @Override
    public List<PnDetailHolder> selectPnDetailByKey(BigDecimal pnOid)
        throws Exception
    {
        if (pnOid == null)
        {
            throw new IllegalArgumentException();
        }

        PnDetailHolder parameter = new PnDetailHolder();
        parameter.setPnOid(pnOid);

        List<PnDetailHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(PnDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(PnDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PnDetailHolder oldObj_, PnDetailHolder newObj_)
        throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PnDetailHolder oldObj_,
        PnDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
