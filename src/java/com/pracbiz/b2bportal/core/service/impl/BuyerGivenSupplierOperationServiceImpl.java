package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerGivenSupplierOperationHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerGivenSupplierOperationMapper;
import com.pracbiz.b2bportal.core.service.BuyerGivenSupplierOperationService;

public class BuyerGivenSupplierOperationServiceImpl extends
        DBActionServiceDefaultImpl<BuyerGivenSupplierOperationHolder> implements
        BuyerGivenSupplierOperationService
{
    @Autowired
    private BuyerGivenSupplierOperationMapper mapper;
    @Override
    public List<BuyerGivenSupplierOperationHolder> selectByBuyerOid(
            BigDecimal buyerOid) throws Exception
    {
        if(buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerGivenSupplierOperationHolder param = new BuyerGivenSupplierOperationHolder();
        param.setBuyerOid(buyerOid);
        return this.select(param);
    }

    @Override
    public List<BuyerGivenSupplierOperationHolder> select(
            BuyerGivenSupplierOperationHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void delete(BuyerGivenSupplierOperationHolder oldObj)
            throws Exception
    {
        mapper.delete(oldObj);
    }

    @Override
    public void insert(BuyerGivenSupplierOperationHolder newObj)
            throws Exception
    {
        mapper.insert(newObj);
    }

    @Override
    public void updateByPrimaryKey(BuyerGivenSupplierOperationHolder oldObj,
            BuyerGivenSupplierOperationHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
        
    }

    @Override
    public void updateByPrimaryKeySelective(
            BuyerGivenSupplierOperationHolder oldObj,
            BuyerGivenSupplierOperationHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
        
    }
}
