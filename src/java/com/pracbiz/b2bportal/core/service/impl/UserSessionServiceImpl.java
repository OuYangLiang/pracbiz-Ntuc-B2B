package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.UserSessionHolder;
import com.pracbiz.b2bportal.core.mapper.UserSessionMapper;
import com.pracbiz.b2bportal.core.service.UserSessionService;

public class UserSessionServiceImpl extends
    DBActionServiceDefaultImpl<UserSessionHolder> implements UserSessionService
{
    @Autowired private UserSessionMapper mapper;
    
    @Override
    public List<UserSessionHolder> select(UserSessionHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(UserSessionHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(UserSessionHolder oldObj_,
        UserSessionHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(UserSessionHolder oldObj_, UserSessionHolder newObj_)
            throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(UserSessionHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<UserSessionHolder> selectByUserOid(BigDecimal userOid)
            throws Exception
    {
        if(userOid == null)
        {
            throw new IllegalArgumentException();
        }

        UserSessionHolder param = new UserSessionHolder();
        param.setUserOid(userOid);
        
        List<UserSessionHolder> rlt = this.select(param);
        
        if (rlt == null || rlt.isEmpty())
        	return null;
        
        return rlt;
    }
    
    
    @Override
    public UserSessionHolder selectUserSessionByKey(String sessionId)
            throws Exception
    {
        if(StringUtils.isBlank(sessionId))
        {
            throw new IllegalArgumentException();
        }

        UserSessionHolder param = new UserSessionHolder();
        param.setSessionId(sessionId);
        
        List<UserSessionHolder> sessions = mapper.select(param);
        
        if (sessions != null && !sessions.isEmpty())
        {
            return sessions.get(0);
        }
        
        return null;
    }

    
    @Override
    public void deleteUserSession(UserSessionHolder userSession)
        throws Exception
    {
        this.mapper.delete(userSession);
    }

}
