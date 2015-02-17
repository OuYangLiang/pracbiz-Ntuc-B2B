package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SalesHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SalesSummaryHolder;
import com.pracbiz.b2bportal.core.mapper.SalesHeaderMapper;
import com.pracbiz.b2bportal.core.service.SalesHeaderService;

public class SalesHeaderServiceImpl extends DBActionServiceDefaultImpl<SalesHeaderHolder>
    implements SalesHeaderService
{
    @Autowired
    private SalesHeaderMapper mapper;
    
    @Override
    public List<SalesHeaderHolder> select(SalesHeaderHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public int getCountOfSummary(MsgTransactionsExHolder param)
        throws Exception
    {
        return mapper.getCountOfSummary(param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }

    @Override
    public void insert(SalesHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(SalesHeaderHolder oldObj_,
        SalesHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(SalesHeaderHolder oldObj_,
        SalesHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(SalesHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public SalesHeaderHolder selectSalesHeaderByKey(BigDecimal salesOid)
        throws Exception
    {
        if (salesOid == null)
        {
            throw new IllegalArgumentException();
        }

        SalesHeaderHolder parameter = new SalesHeaderHolder();
        parameter.setSalesOid(salesOid);

        List<SalesHeaderHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }

        return null;
    }

    @Override
    public List<SalesSummaryHolder> selectAllRecordToExport(
        SalesSummaryHolder param) throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }

}
