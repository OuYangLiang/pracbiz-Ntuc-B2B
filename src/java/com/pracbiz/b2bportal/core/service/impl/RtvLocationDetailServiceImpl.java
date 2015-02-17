package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;
import com.pracbiz.b2bportal.core.mapper.RtvLocationDetailMapper;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailService;

public class RtvLocationDetailServiceImpl extends
    DBActionServiceDefaultImpl<RtvLocationDetailHolder> implements
    RtvLocationDetailService
{
    @Autowired transient private RtvLocationDetailMapper mapper;
    
    @Override
    public void insert(RtvLocationDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(RtvLocationDetailHolder oldObj_,
        RtvLocationDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(RtvLocationDetailHolder oldObj_,
        RtvLocationDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(RtvLocationDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<RtvLocationDetailHolder> selectRtvLocationDetailByRtvOid(
        BigDecimal rtvOid) throws Exception
    {
        if (rtvOid == null)
        {
            throw new IllegalArgumentException();
        }

        RtvLocationDetailHolder parameter = new RtvLocationDetailHolder();
        parameter.setRtvOid(rtvOid);

        List<RtvLocationDetailHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

}
