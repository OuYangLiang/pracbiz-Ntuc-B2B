package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.UserProfileTmpMapper;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;

/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */

public class UserProfileTmpServiceImpl extends DBActionServiceDefaultImpl<UserProfileTmpHolder> implements UserProfileTmpService
{
    @Autowired
    private UserProfileTmpMapper mapper;
    
    @Override
    public List<UserProfileTmpHolder> select(UserProfileTmpHolder param)
        throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(UserProfileTmpHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(UserProfileTmpHolder oldObj_,
        UserProfileTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    
    @Override
    public void updateByPrimaryKey(UserProfileTmpHolder oldObj_,
        UserProfileTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(UserProfileTmpHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public int getCountOfSummary(UserProfileTmpExHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<UserProfileTmpExHolder> getListOfSummary(
        UserProfileTmpExHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public boolean isLoginIdExist(String loginId) throws Exception
    {
        return null != this.selectUserProfileTmpByLoginId(loginId);
    }


    @Override
    public UserProfileTmpHolder selectUserProfileTmpByLoginId(String loginId)
            throws Exception
    {
        if (StringUtils.isBlank(loginId))
        {
            throw new IllegalArgumentException();
        }
        
        UserProfileTmpExHolder param = new UserProfileTmpExHolder();
        param.setLoginId(loginId);
        
        List<UserProfileTmpHolder> users = mapper.select(param);
        
        if (users != null && !users.isEmpty())
        {
            return users.get(0);
        }
        
        return null;
    }


    @Override
    public UserProfileTmpHolder selectUserProfileTmpByKey(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        UserProfileTmpExHolder param = new UserProfileTmpExHolder();
        param.setUserOid(userOid);
        
        List<UserProfileTmpHolder> users = mapper.select(param);
        
        if (users != null && !users.isEmpty())
        {
            return users.get(0);
        }
        
        return null;
    }


    @Override
    public List<UserProfileTmpHolder> selectUsersByUserOids(List<BigDecimal> userOids)
        throws Exception
    {
        if (userOids == null || userOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        UserProfileTmpExHolder param = new UserProfileTmpExHolder();
        param.setUserOids(userOids);
        
        return mapper.select(param);
    }


    @Override
    public List<UserProfileTmpHolder> selectUsersBySupplierOid(
        BigDecimal supplierOid) throws Exception
    {
        if (supplierOid == null )
        {
            throw new IllegalArgumentException();
        }
        
        UserProfileTmpHolder param = new UserProfileTmpHolder();
        param.setSupplierOid(supplierOid);
        
        return mapper.select(param);
    }
    
}
