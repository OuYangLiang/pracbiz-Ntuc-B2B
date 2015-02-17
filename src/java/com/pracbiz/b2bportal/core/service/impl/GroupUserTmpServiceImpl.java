package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.GroupUserTmpHolder;
import com.pracbiz.b2bportal.core.mapper.GroupUserTmpMapper;
import com.pracbiz.b2bportal.core.service.GroupUserTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class GroupUserTmpServiceImpl extends
    DBActionServiceDefaultImpl<GroupUserTmpHolder> implements
    GroupUserTmpService
{
    @Autowired
    private GroupUserTmpMapper mapper;


    @Override
    public List<GroupUserTmpHolder> select(GroupUserTmpHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<GroupUserTmpHolder> selectGroupUserTmpByUserOid(
        BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupUserTmpHolder param = new GroupUserTmpHolder();
        param.setUserOid(userOid);

        return select(param);
    }

    
    @Override
    public List<GroupUserTmpHolder> selectGroupUserTmpByGroupOid(
        BigDecimal groupOid) throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupUserTmpHolder param = new GroupUserTmpHolder();
        param.setGroupOid(groupOid);

        return select(param);
    }


    @Override
    public void delete(GroupUserTmpHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }

    
    @Override
    public void insert(GroupUserTmpHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(GroupUserTmpHolder oldObj,
        GroupUserTmpHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(GroupUserTmpHolder oldObj,
        GroupUserTmpHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }

}
