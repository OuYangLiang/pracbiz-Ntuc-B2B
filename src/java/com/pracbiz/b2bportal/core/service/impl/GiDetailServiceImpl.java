package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;
import com.pracbiz.b2bportal.core.mapper.GiDetailMapper;
import com.pracbiz.b2bportal.core.service.GiDetailService;

public class GiDetailServiceImpl extends
        DBActionServiceDefaultImpl<GiDetailHolder> implements GiDetailService
{
    @Autowired
    GiDetailMapper mapper;


    @Override
    public void insert(GiDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(GiDetailHolder oldObj_,
            GiDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(GiDetailHolder oldObj_,
            GiDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(GiDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<GiDetailHolder> select(GiDetailHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<GiDetailHolder> selectByGiOid(BigDecimal giOid)
            throws Exception
    {
        if (giOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        GiDetailHolder param = new GiDetailHolder();
        param.setGiOid(giOid);
        return select(param);
    }

    
    @Override
    public List<GiDetailHolder> selectGiDetailByKey(BigDecimal giOid)
        throws Exception
    {
        if (giOid == null)
        {
            throw new IllegalArgumentException();
        }

        GiDetailHolder parameter = new GiDetailHolder();
        parameter.setGiOid(giOid);

        List<GiDetailHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

}
