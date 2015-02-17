package com.pracbiz.b2bportal.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SalesDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.SalesDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.SalesDetailExtendedService;

public class SalesDetailExtendedServiceImpl extends
    DBActionServiceDefaultImpl<SalesDetailExtendedHolder> implements SalesDetailExtendedService
{
    @Autowired
    private SalesDetailExtendedMapper mapper;
    
    @Override
    public List<SalesDetailExtendedHolder> select(
        SalesDetailExtendedHolder param) throws Exception
    {
        return null;
    }

    @Override
    public void insert(SalesDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(SalesDetailExtendedHolder oldObj_,
        SalesDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(SalesDetailExtendedHolder oldObj_,
        SalesDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(SalesDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

}
