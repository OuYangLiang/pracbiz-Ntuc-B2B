package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.mapper.UserClassMapper;
import com.pracbiz.b2bportal.core.service.UserClassService;

public class UserClassServiceImpl extends DBActionServiceDefaultImpl<UserClassHolder>
        implements UserClassService
{
    @Autowired UserClassMapper mapper;
    @Override
    public List<UserClassHolder> select(UserClassHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(UserClassHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(UserClassHolder oldObj_,
            UserClassHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(UserClassHolder oldObj_,
            UserClassHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(UserClassHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public UserClassHolder selectByUserOidAndClassOid(BigDecimal userOid,
            BigDecimal classOid) throws Exception
    {
        if (userOid == null || classOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserClassHolder param = new UserClassHolder();
        param.setUserOid(userOid);
        param.setClassOid(classOid);
        List<UserClassHolder> list = select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<UserClassHolder> selectByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserClassHolder param = new UserClassHolder();
        param.setUserOid(userOid);
        return select(param);
    }

    @Override
    public void deleteByClassOid(BigDecimal classOid) throws Exception
    {
        if (classOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserClassHolder param = new UserClassHolder();
        param.setClassOid(classOid);
        delete(param);
        
    }

}
