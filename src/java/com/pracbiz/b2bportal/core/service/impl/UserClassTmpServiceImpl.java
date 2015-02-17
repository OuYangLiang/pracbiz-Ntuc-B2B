package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.UserClassTmpHolder;
import com.pracbiz.b2bportal.core.mapper.UserClassTmpMapper;
import com.pracbiz.b2bportal.core.service.UserClassTmpService;

public class UserClassTmpServiceImpl extends
        DBActionServiceDefaultImpl<UserClassTmpHolder> implements
        UserClassTmpService
{
    @Autowired UserClassTmpMapper mapper;
    @Override
    public List<UserClassTmpHolder> select(UserClassTmpHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(UserClassTmpHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(UserClassTmpHolder oldObj_,
            UserClassTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(UserClassTmpHolder oldObj_,
            UserClassTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(UserClassTmpHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public UserClassTmpHolder selectUserClassTmpByUserOidAndClassOid(
            BigDecimal userOid, BigDecimal classOid) throws Exception
    {
        if (userOid == null || classOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserClassTmpHolder param = new UserClassTmpHolder();
        param.setUserOid(userOid);
        param.setClassOid(classOid);
        List<UserClassTmpHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }


    @Override
    public List<UserClassTmpHolder> selectUserClassTmpByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserClassTmpHolder param = new UserClassTmpHolder();
        param.setUserOid(userOid);
        return this.select(param);
    }


    @Override
    public void deleteByClassOid(BigDecimal classOid) throws Exception
    {
        if (classOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        UserClassTmpHolder param = new UserClassTmpHolder();
        param.setClassOid(classOid);
        mapper.delete(param);
    }

}
