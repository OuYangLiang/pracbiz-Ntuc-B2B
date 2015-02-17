package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SupplierSharedHolder;
import com.pracbiz.b2bportal.core.mapper.SupplierSharedMapper;
import com.pracbiz.b2bportal.core.service.SupplierSharedService;

public class SupplierSharedServiceImpl extends DBActionServiceDefaultImpl<SupplierSharedHolder>
        implements SupplierSharedService
{
    @Autowired private SupplierSharedMapper mapper;
    
    @Override
    public List<SupplierSharedHolder> select(SupplierSharedHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void delete(SupplierSharedHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(SupplierSharedHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(SupplierSharedHolder oldObj,
            SupplierSharedHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKeySelective(SupplierSharedHolder oldObj,
            SupplierSharedHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public List<SupplierSharedHolder> selectBySelfSupOid(BigDecimal selfSupOid)
            throws Exception
    {
        if (selfSupOid == null)
        {
            throw new IllegalArgumentException();
        }
        SupplierSharedHolder param = new SupplierSharedHolder();
        param.setSelfSupOid(selfSupOid);
        return this.select(param);
    }


    @Override
    public List<SupplierSharedHolder> selectByGrantSupOid(BigDecimal grantSupOid)
            throws Exception
    {
        if (grantSupOid == null)
        {
            throw new IllegalArgumentException();
        }
        SupplierSharedHolder param = new SupplierSharedHolder();
        param.setGrantSupOid(grantSupOid);
        return this.select(param);
    }

}
