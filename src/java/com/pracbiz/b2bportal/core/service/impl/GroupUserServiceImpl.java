package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.mapper.GroupUserMapper;
import com.pracbiz.b2bportal.core.service.GroupUserService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class GroupUserServiceImpl extends
    DBActionServiceDefaultImpl<GroupUserHolder> implements GroupUserService
{
    @Autowired
    private GroupUserMapper mapper;


    @Override
    public List<GroupUserHolder> select(GroupUserHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<GroupUserHolder> selectGroupUserByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupUserHolder param = new GroupUserHolder();
        param.setUserOid(userOid);

        return select(param);
    }


    @Override
    public List<GroupUserHolder> selectGroupUserByGroupOid(BigDecimal groupOid)
            throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupUserHolder param = new GroupUserHolder();
        param.setGroupOid(groupOid);

        return select(param);
    }


    @Override
    public void delete(GroupUserHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(GroupUserHolder newObj) throws Exception
    {
        mapper.insert(newObj);

    }


    @Override
    public void updateByPrimaryKey(GroupUserHolder oldObj,
        GroupUserHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(GroupUserHolder oldObj,
        GroupUserHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public GroupUserHolder selectByUserOid(BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        GroupUserHolder param = new GroupUserHolder();
        param.setUserOid(userOid);
        List<GroupUserHolder> result = this.select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        return result.get(0);
    }

}
