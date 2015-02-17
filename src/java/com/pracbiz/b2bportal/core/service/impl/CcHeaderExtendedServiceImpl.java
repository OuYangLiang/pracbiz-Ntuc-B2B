//*****************************************************************************
//
// File Name       :  InvHeaderExtendedServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.CcHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.CcHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.CcHeaderExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CcHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<CcHeaderExtendedHolder> implements
    CcHeaderExtendedService
{
    @Autowired
    private CcHeaderExtendedMapper mapper;

    
    @Override
    public List<CcHeaderExtendedHolder> selectHeaderExtendedByKey(
        BigDecimal invOid)
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }

        CcHeaderExtendedHolder parameter = new CcHeaderExtendedHolder();
        parameter.setInvOid(invOid);

        List<CcHeaderExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    
    }

    @Override
    public void delete(CcHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(CcHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(CcHeaderExtendedHolder oldObj_,
        CcHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(CcHeaderExtendedHolder oldObj_,
        CcHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
