package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailHolder;
import com.pracbiz.b2bportal.core.mapper.SalesDateLocationDetailMapper;
import com.pracbiz.b2bportal.core.service.SalesDateLocationDetailService;

public class SalesDateLocationDetailServiceImpl extends
    DBActionServiceDefaultImpl<SalesDateLocationDetailHolder> implements SalesDateLocationDetailService
{
    @Autowired
    private SalesDateLocationDetailMapper mapper;
    
    @Override
    public List<SalesDateLocationDetailHolder> select(
        SalesDateLocationDetailHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(SalesDateLocationDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(
        SalesDateLocationDetailHolder oldObj_,
        SalesDateLocationDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(SalesDateLocationDetailHolder oldObj_,
        SalesDateLocationDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(SalesDateLocationDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<SalesDateLocationDetailHolder> selectSalesLocationDetailByKey(
        BigDecimal salesOid) throws Exception
    {
        if (salesOid == null)
        {
            throw new IllegalArgumentException();
        }

        SalesDateLocationDetailHolder parameter = new SalesDateLocationDetailHolder();
        parameter.setSalesOid(salesOid);

        return mapper.select(parameter);
    }

}
