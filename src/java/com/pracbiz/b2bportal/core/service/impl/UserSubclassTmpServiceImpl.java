package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.UserSubclassTmpHolder;
import com.pracbiz.b2bportal.core.mapper.UserSubclassTmpMapper;
import com.pracbiz.b2bportal.core.service.UserSubclassTmpService;

public class UserSubclassTmpServiceImpl extends DBActionServiceDefaultImpl<UserSubclassTmpHolder>
        implements UserSubclassTmpService
{
    @Autowired UserSubclassTmpMapper mapper;
    @Override
    public List<UserSubclassTmpHolder> select(UserSubclassTmpHolder param)
            throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(UserSubclassTmpHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(UserSubclassTmpHolder oldObj_,
            UserSubclassTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(UserSubclassTmpHolder oldObj_,
            UserSubclassTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(UserSubclassTmpHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public UserSubclassTmpHolder selectUserSubclassTmpByUserOidAndSubClassOid(
            BigDecimal userOid, BigDecimal subclassOid) throws Exception
    {
        if (userOid == null || subclassOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserSubclassTmpHolder param = new UserSubclassTmpHolder();
        param.setUserOid(userOid);
        param.setSubclassOid(subclassOid);
        List<UserSubclassTmpHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<UserSubclassTmpHolder> selectUserSubclassTmpByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserSubclassTmpHolder param = new UserSubclassTmpHolder();
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
        
        UserSubclassTmpHolder param = new UserSubclassTmpHolder();
        param.setSubclassOid(subclassOid);
        mapper.delete(param);
    }

}
