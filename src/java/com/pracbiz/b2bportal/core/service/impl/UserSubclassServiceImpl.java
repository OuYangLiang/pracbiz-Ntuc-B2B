package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.mapper.UserSubclassMapper;
import com.pracbiz.b2bportal.core.service.UserSubclassService;

public class UserSubclassServiceImpl extends DBActionServiceDefaultImpl<UserSubclassHolder>
        implements UserSubclassService
{
    @Autowired UserSubclassMapper mapper;
    @Override
    public List<UserSubclassHolder> select(UserSubclassHolder param)
            throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(UserSubclassHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(UserSubclassHolder oldObj_,
            UserSubclassHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(UserSubclassHolder oldObj_,
            UserSubclassHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(UserSubclassHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public UserSubclassHolder selectByUserOidAndSubclassOid(BigDecimal userOid,
            BigDecimal subclassOid) throws Exception
    {
        if (userOid == null || subclassOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserSubclassHolder param = new UserSubclassHolder();
        param.setUserOid(userOid);
        param.setSubclassOid(subclassOid);
        List<UserSubclassHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<UserSubclassHolder> selectByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserSubclassHolder param = new UserSubclassHolder();
        param.setUserOid(userOid);
        return this.select(param);
    }

    @Override
    public void deleteBySubclassOid(BigDecimal subclassOid) throws Exception
    {
        if (subclassOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        UserSubclassHolder param = new UserSubclassHolder();
        param.setSubclassOid(subclassOid);
        delete(param);
    }

}
