package com.pracbiz.b2bportal.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.holder.CurrencyHolder;
import com.pracbiz.b2bportal.core.mapper.CurrencyMapper;
import com.pracbiz.b2bportal.core.service.CurrencyService;

public class CurrencyServiceImpl implements
        CurrencyService
{
    @Autowired
    private CurrencyMapper mapper;
    
    @Override
    public List<CurrencyHolder> select(CurrencyHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public CurrencyHolder selectByCurrCode(String currCode) throws Exception
    {
        if (null == currCode)
        {
            throw new IllegalArgumentException();
        }

        CurrencyHolder param = new CurrencyHolder();
        param.setCurrCode(currCode);

        List<CurrencyHolder> rlt = this.select(param);

        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }

        return null;
    }

}
