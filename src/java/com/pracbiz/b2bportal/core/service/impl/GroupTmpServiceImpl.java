package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.mapper.GroupTmpMapper;
import com.pracbiz.b2bportal.core.service.GroupTmpService;

public class GroupTmpServiceImpl extends
    DBActionServiceDefaultImpl<GroupTmpHolder> implements GroupTmpService
{
    @Autowired
    private GroupTmpMapper mapper;


    @Override
    public List<GroupTmpHolder> select(GroupTmpHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<GroupTmpHolder> selectGroupsByUserType(BigDecimal userTypeOid)
        throws Exception
    {
        if (userTypeOid == null)
        {
            throw new IllegalArgumentException();
        }
        GroupTmpHolder param = new GroupTmpHolder();
        param.setUserTypeOid(userTypeOid);

        return this.select(param);
    }


    @Override
    public GroupTmpHolder selectGroupTmpByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }

        return mapper.selectGroupTmpByUserOid(userOid);
    }


    @Override
    public boolean isGroupIdExist(String groupId, BigDecimal companyOid,
        boolean isBuyer) throws Exception
    {
        if (StringUtils.isBlank(groupId) || companyOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupTmpHolder param = new GroupTmpHolder();
        param.setGroupId(groupId);
        if (isBuyer)
        {
            param.setBuyerOid(companyOid);
        }
        else
        {
            param.setSupplierOid(companyOid);
        }

        List<GroupTmpHolder> rlt = mapper.select(param);

        if (rlt == null || rlt.isEmpty())
        {
            return false;
        }

        return true;
    }


    @Override
    public void insert(GroupTmpHolder newObj_) throws Exception
    {
        this.mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(GroupTmpHolder oldObj_,
        GroupTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(GroupTmpHolder oldObj_,
        GroupTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(GroupTmpHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public GroupTmpHolder selectGroupTmpByKey(BigDecimal groupOid)
        throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupTmpHolder param = new GroupTmpHolder();
        param.setGroupOid(groupOid);

        List<GroupTmpHolder> rlt = mapper.select(param);

        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }

        return rlt.get(0);
    }

    
    @Override
    public List<GroupTmpHolder> selectGroupBySupplierOidAndUserTypeOid(
            BigDecimal supplierOid, BigDecimal userTypeOid) throws Exception
    {
        if (supplierOid == null || userTypeOid == null)
        { 
            throw new IllegalArgumentException();
        }
        GroupTmpHolder param = new GroupTmpHolder();
        param.setSupplierOid(supplierOid);
        param.setUserTypeOid(userTypeOid);
        List<GroupTmpHolder> result = select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        return result;
    }
}
