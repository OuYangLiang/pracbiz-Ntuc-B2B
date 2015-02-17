package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.UserSessionHolder;

public interface UserSessionService extends BaseService<UserSessionHolder>,
    DBActionService<UserSessionHolder>
{
    public List<UserSessionHolder> selectByUserOid(BigDecimal userOid)
        throws Exception;

    
    public void deleteUserSession(UserSessionHolder userSession)
        throws Exception;

    
    public UserSessionHolder selectUserSessionByKey(String session_id)
        throws Exception;
}
