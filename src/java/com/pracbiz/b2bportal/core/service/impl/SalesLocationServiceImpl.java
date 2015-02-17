package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SalesLocationHolder;
import com.pracbiz.b2bportal.core.mapper.SalesLocationMapper;
import com.pracbiz.b2bportal.core.service.SalesLocationService;

public class SalesLocationServiceImpl extends DBActionServiceDefaultImpl<SalesLocationHolder>
    implements SalesLocationService
{
    @Autowired
    private SalesLocationMapper mapper;
    
    @Override
    public List<SalesLocationHolder> select(SalesLocationHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(SalesLocationHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(SalesLocationHolder oldObj_,
        SalesLocationHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(SalesLocationHolder oldObj_,
        SalesLocationHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(SalesLocationHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<SalesLocationHolder> selectSalesLocationByKey(
        BigDecimal salesOid) throws Exception
    {
        if (salesOid == null)
        {
            throw new IllegalArgumentException();
        }

        SalesLocationHolder parameter = new SalesLocationHolder();
        parameter.setSalesOid(salesOid);

        return mapper.select(parameter);
    }

}
