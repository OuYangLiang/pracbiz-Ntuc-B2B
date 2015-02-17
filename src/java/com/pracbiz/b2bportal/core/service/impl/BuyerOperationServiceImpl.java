package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerOperationHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerOperationMapper;
import com.pracbiz.b2bportal.core.service.BuyerOperationService;

public class BuyerOperationServiceImpl extends
        DBActionServiceDefaultImpl<BuyerOperationHolder> implements
        BuyerOperationService
{
    @Autowired
    private BuyerOperationMapper mapper;
    @Override
    public List<BuyerOperationHolder> select(BuyerOperationHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void delete(BuyerOperationHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(BuyerOperationHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(BuyerOperationHolder oldObj,
            BuyerOperationHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);

    }


    @Override
    public void updateByPrimaryKeySelective(BuyerOperationHolder oldObj,
            BuyerOperationHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
        
    }


    @Override
    public List<BuyerOperationHolder> selectByBuyerOid(BigDecimal buyerOid)
            throws Exception
    {
        if(buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerOperationHolder param = new BuyerOperationHolder();
        param.setBuyerOid(buyerOid);
        return this.select(param);
    }
}
