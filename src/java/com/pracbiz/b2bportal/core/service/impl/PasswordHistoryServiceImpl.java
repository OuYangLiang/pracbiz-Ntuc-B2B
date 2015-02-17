package com.pracbiz.b2bportal.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.mapper.PasswordHistoryMapper;
import com.pracbiz.b2bportal.core.service.PasswordHistoryService;

public class PasswordHistoryServiceImpl extends
    DBActionServiceDefaultImpl<PasswordHistoryHolder> implements
    PasswordHistoryService
{
    @Autowired
    private PasswordHistoryMapper mapper;
    
    @Override
    public List<PasswordHistoryHolder> select(PasswordHistoryHolder param)
        throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void delete(PasswordHistoryHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(PasswordHistoryHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(PasswordHistoryHolder oldObj,
        PasswordHistoryHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(PasswordHistoryHolder oldObj,
        PasswordHistoryHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }

}
