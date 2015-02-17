package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.CnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.CnDetailExtendedMapper;
import com.pracbiz.b2bportal.core.service.CnDetailExtendedService;

public class CnDetailExtendedServiceImpl extends
        DBActionServiceDefaultImpl<CnDetailExtendedHolder> implements
        CnDetailExtendedService
{
    @Autowired CnDetailExtendedMapper mapper;
    
    @Override
    public void insert(CnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(CnDetailExtendedHolder oldObj_,
            CnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(CnDetailExtendedHolder oldObj_,
            CnDetailExtendedHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(CnDetailExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<CnDetailExtendedHolder> select(CnDetailExtendedHolder param)
            throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public List<CnDetailExtendedHolder> selectByCnOid(BigDecimal cnOid)
            throws Exception
    {
        if (cnOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        CnDetailExtendedHolder param = new CnDetailExtendedHolder();
        param.setCnOid(cnOid);
        return select(param);
    }

}
