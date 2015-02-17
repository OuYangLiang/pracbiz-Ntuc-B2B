package com.pracbiz.b2bportal.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.SalesDateLocationDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.SalesDateLocationDetailExtendedService;

public class SalesDateLocationDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<SalesDateLocationDetailExtendedHolder> implements
    SalesDateLocationDetailExtendedService
{
    @Autowired
    private SalesDateLocationDetailExtendedMapper mapper;
    
    @Override
    public List<SalesDateLocationDetailExtendedHolder> select(
        SalesDateLocationDetailExtendedHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(SalesDateLocationDetailExtendedHolder newObj_)
        throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(
        SalesDateLocationDetailExtendedHolder oldObj_,
        SalesDateLocationDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(
        SalesDateLocationDetailExtendedHolder oldObj_,
        SalesDateLocationDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(SalesDateLocationDetailExtendedHolder oldObj_)
        throws Exception
    {
        mapper.delete(oldObj_);
    }

}
