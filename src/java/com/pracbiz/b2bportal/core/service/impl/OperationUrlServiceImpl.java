package com.pracbiz.b2bportal.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.holder.OperationUrlHolder;
import com.pracbiz.b2bportal.core.mapper.OperationUrlMapper;
import com.pracbiz.b2bportal.core.service.OperationUrlService;

public class OperationUrlServiceImpl implements OperationUrlService
{
    @Autowired
    private OperationUrlMapper mapper;

    @Override
    public List<OperationUrlHolder> select(OperationUrlHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

}
