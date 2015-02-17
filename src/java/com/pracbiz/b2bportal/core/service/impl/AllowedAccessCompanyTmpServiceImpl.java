package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.mapper.AllowedAccessCompanyTmpMapper;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyTmpService;

public class AllowedAccessCompanyTmpServiceImpl extends
        DBActionServiceDefaultImpl<AllowedAccessCompanyTmpHolder> implements AllowedAccessCompanyTmpService
{
    @Autowired
    private AllowedAccessCompanyTmpMapper mapper;
    
    @Override
    public List<BuyerHolder> selectBuyerByUserOid(BigDecimal userOid)
            throws Exception
    {
        return mapper.selectBuyerByUserOid(userOid);
    }

    @Override
    public List<AllowedAccessCompanyTmpHolder> selectByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        AllowedAccessCompanyTmpHolder param = new AllowedAccessCompanyTmpHolder();
        param.setUserOid(userOid);
        return this.select(param);
    }

    @Override
    public List<AllowedAccessCompanyTmpHolder> select(
            AllowedAccessCompanyTmpHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void delete(AllowedAccessCompanyTmpHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }

    @Override
    public void insert(AllowedAccessCompanyTmpHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }

    @Override
    public void updateByPrimaryKey(AllowedAccessCompanyTmpHolder oldObj,
            AllowedAccessCompanyTmpHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKeySelective(
            AllowedAccessCompanyTmpHolder oldObj,
            AllowedAccessCompanyTmpHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }


}
