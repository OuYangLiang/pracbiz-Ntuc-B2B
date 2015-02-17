//*****************************************************************************
//
// File Name       :  DoDetailServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.DoDetailHolder;
import com.pracbiz.b2bportal.core.mapper.DoDetailMapper;
import com.pracbiz.b2bportal.core.service.DoDetailService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DoDetailServiceImpl extends
    DBActionServiceDefaultImpl<DoDetailHolder> implements DoDetailService
{
    @Autowired
    private DoDetailMapper mapper;

    @Override
    public void delete(DoDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(DoDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(DoDetailHolder oldObj_,
        DoDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(DoDetailHolder oldObj_,
        DoDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

}
