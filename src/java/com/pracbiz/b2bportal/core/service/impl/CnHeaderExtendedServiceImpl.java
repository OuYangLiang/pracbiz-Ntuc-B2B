package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.CnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.CnHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.CnHeaderExtendedService;

public class CnHeaderExtendedServiceImpl extends
        DBActionServiceDefaultImpl<CnHeaderExtendedHolder> implements
        CnHeaderExtendedService
{
    @Autowired CnHeaderExtendedMapper mapper;
    
    @Override
    public void insert(CnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(CnHeaderExtendedHolder oldObj_,
            CnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(CnHeaderExtendedHolder oldObj_,
            CnHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(CnHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<CnHeaderExtendedHolder> selectByKey(BigDecimal cnOid)
            throws Exception
    {
        if (cnOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        CnHeaderExtendedHolder param = new CnHeaderExtendedHolder();
        param.setCnOid(cnOid);
        
        return this.select(param);
    }

    @Override
    public List<CnHeaderExtendedHolder> select(CnHeaderExtendedHolder param)
            throws Exception
    {
        return mapper.select(param);
    }

}
