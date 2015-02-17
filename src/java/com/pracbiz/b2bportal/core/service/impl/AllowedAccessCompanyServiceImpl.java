package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.mapper.AllowedAccessCompanyMapper;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyService;

public class AllowedAccessCompanyServiceImpl extends
        DBActionServiceDefaultImpl<AllowedAccessCompanyHolder> implements
        AllowedAccessCompanyService
{
    @Autowired
    private AllowedAccessCompanyMapper allowedAccessCompanyMapper;
    
    @Override
    public List<AllowedAccessCompanyHolder> select(
            AllowedAccessCompanyHolder param) throws Exception
    {
        return allowedAccessCompanyMapper.select(param);
    }


    @Override
    public void delete(AllowedAccessCompanyHolder oldObj) throws Exception
    {
        allowedAccessCompanyMapper.delete(oldObj);
    }


    @Override
    public void insert(AllowedAccessCompanyHolder newObj) throws Exception
    {
        allowedAccessCompanyMapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(AllowedAccessCompanyHolder oldObj,
            AllowedAccessCompanyHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKeySelective(AllowedAccessCompanyHolder oldObj,
            AllowedAccessCompanyHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public List<AllowedAccessCompanyHolder> selectByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        AllowedAccessCompanyHolder param = new AllowedAccessCompanyHolder();
        param.setUserOid(userOid);
        return this.select(param);
    }


    @Override
    public List<BuyerHolder> selectBuyerByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        return allowedAccessCompanyMapper.selectBuyerByUserOid(userOid);
    }

}
