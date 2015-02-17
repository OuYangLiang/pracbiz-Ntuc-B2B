package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SalesDateHolder;
import com.pracbiz.b2bportal.core.mapper.SalesDateMapper;
import com.pracbiz.b2bportal.core.service.SalesDateService;

public class SalesDateServiceImpl extends DBActionServiceDefaultImpl<SalesDateHolder>
    implements SalesDateService
{
    @Autowired
    private SalesDateMapper mapper;
    
    @Override
    public List<SalesDateHolder> select(SalesDateHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(SalesDateHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(SalesDateHolder oldObj_,
        SalesDateHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(SalesDateHolder oldObj_,
        SalesDateHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(SalesDateHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<SalesDateHolder> selectSalesDateByKey(BigDecimal salesOid)
        throws Exception
    {
        if (salesOid == null)
        {
            throw new IllegalArgumentException();
        }

        SalesDateHolder parameter = new SalesDateHolder();
        parameter.setSalesOid(salesOid);

        return mapper.select(parameter);
    }

}
