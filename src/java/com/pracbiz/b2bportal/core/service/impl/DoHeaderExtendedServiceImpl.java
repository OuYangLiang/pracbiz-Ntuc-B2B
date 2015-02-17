//*****************************************************************************
//
// File Name       :  DoHeaderExtendedServiceImpl.java
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

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.DoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.DoHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.DoHeaderExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DoHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<DoHeaderExtendedHolder> implements
    DoHeaderExtendedService
{
    @Autowired
    private DoHeaderExtendedMapper mapper;

    @Override
    public void delete(DoHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(DoHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(DoHeaderExtendedHolder oldObj_,
        DoHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(DoHeaderExtendedHolder oldObj_,
        DoHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
