//*****************************************************************************
//
// File Name       :  DoDetailExtendedServiceImpl.java
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

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.DoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.DoDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.DoDetailExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DoDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<DoDetailExtendedHolder> implements
    DoDetailExtendedService
{
    @Autowired
    private DoDetailExtendedMapper mapper;

    @Override
    public void delete(DoDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(DoDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(DoDetailExtendedHolder oldObj_,
        DoDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(DoDetailExtendedHolder oldObj_,
        DoDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
