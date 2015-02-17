package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SalesDetailHolder;
import com.pracbiz.b2bportal.core.mapper.SalesDetailMapper;
import com.pracbiz.b2bportal.core.service.SalesDetailService;

public class SalesDetailServiceImpl extends DBActionServiceDefaultImpl<SalesDetailHolder>
    implements SalesDetailService
{
    @Autowired
    private SalesDetailMapper mapper;
    
    @Override
    public List<SalesDetailHolder> select(SalesDetailHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(SalesDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(SalesDetailHolder oldObj_,
        SalesDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(SalesDetailHolder oldObj_,
        SalesDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(SalesDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<SalesDetailHolder> selectSalesDetailByKey(BigDecimal salesOid)
        throws Exception
    {
        if (salesOid == null)
        {
            throw new IllegalArgumentException();
        }

        SalesDetailHolder parameter = new SalesDetailHolder();
        parameter.setSalesOid(salesOid);

        return mapper.select(parameter);
    }

}
