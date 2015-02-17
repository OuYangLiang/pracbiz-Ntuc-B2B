package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.CnDetailHolder;
import com.pracbiz.b2bportal.core.mapper.CnDetailMapper;
import com.pracbiz.b2bportal.core.service.CnDetailService;

public class CnDetailServiceImpl extends
        DBActionServiceDefaultImpl<CnDetailHolder> implements CnDetailService
{
    @Autowired CnDetailMapper mapper;
    
    @Override
    public List<CnDetailHolder> select(CnDetailHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(CnDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(CnDetailHolder oldObj_,
            CnDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(CnDetailHolder oldObj_,
            CnDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(CnDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<CnDetailHolder> selectByCnOid(BigDecimal cnOid) throws Exception
    {
        if (cnOid == null)
        {
            throw new IllegalArgumentException();
        }
        CnDetailHolder param = new CnDetailHolder();
        param.setCnOid(cnOid);
        
        return select(param);
    }

}
