package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SalesHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.SalesHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.SalesHeaderExtendedService;

public class SalesHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<SalesHeaderExtendedHolder> implements
    SalesHeaderExtendedService
{
    @Autowired
    private SalesHeaderExtendedMapper mapper;
    
    @Override
    public List<SalesHeaderExtendedHolder> select(
        SalesHeaderExtendedHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(SalesHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(SalesHeaderExtendedHolder oldObj_,
        SalesHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(SalesHeaderExtendedHolder oldObj_,
        SalesHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(SalesHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<SalesHeaderExtendedHolder> selectSalesHeaderExtendedByKey(
        BigDecimal salesOid) throws Exception
    {
        if (salesOid == null)
        {
            throw new IllegalArgumentException();
        }

        SalesHeaderExtendedHolder parameter = new SalesHeaderExtendedHolder();
        parameter.setSalesOid(salesOid);

        return mapper.select(parameter);
    }

}
