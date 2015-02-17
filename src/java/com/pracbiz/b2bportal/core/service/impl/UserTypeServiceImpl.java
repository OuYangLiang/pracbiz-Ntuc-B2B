package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.mapper.UserTypeMapper;
import com.pracbiz.b2bportal.core.service.UserTypeService;

/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */
public class UserTypeServiceImpl implements
        UserTypeService
{
    @Autowired
    private UserTypeMapper mapper;
    
    @Override
    public UserTypeHolder selectByKey(BigDecimal userTypeOid) throws Exception
    {
        if (userTypeOid == null)
        {
            throw new IllegalArgumentException();
        }
        UserTypeHolder param = new UserTypeHolder();
        param.setUserTypeOid(userTypeOid);
        
        List<UserTypeHolder> rlt = this.select(param);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        return null;
    }
    
    
    @Override
    public List<UserTypeHolder> select(UserTypeHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<UserTypeHolder> selectPrivilegedSubUserTypesByUserType(
            BigDecimal userTypeOid) throws Exception
    {
        if (null == userTypeOid)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectPrivilegedSubUserTypesByUserType(userTypeOid);
    }
    
    
    @Override
    public List<UserTypeHolder> selectPrivilegedSubUserTypesByUserTypeInclusively(
            BigDecimal userTypeOid) throws Exception
    {
        if (null == userTypeOid)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectPrivilegedSubUserTypesByUserTypeInclusively(userTypeOid);
    }


    @Override
    public Boolean checkOperateEquativeUserType(BigDecimal userTypeOid)
            throws Exception
    {
        if (userTypeOid == null)
        {
            throw new IllegalArgumentException();
        }
        List<UserTypeHolder> subUserTypes = this.selectPrivilegedSubUserTypesByUserType(userTypeOid);
        if (subUserTypes == null || subUserTypes.isEmpty())
        {
            return false;
        }
        for (UserTypeHolder userType : subUserTypes)
        {
            if (userTypeOid.equals(userType.getUserTypeOid()))
            {
                return true;
            }
        }
        return false;
    }

}
