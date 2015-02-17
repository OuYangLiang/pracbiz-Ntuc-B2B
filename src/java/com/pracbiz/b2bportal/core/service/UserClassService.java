package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;

public interface UserClassService extends BaseService<UserClassHolder>,
        DBActionService<UserClassHolder>
{
    public UserClassHolder selectByUserOidAndClassOid(BigDecimal userOid,
            BigDecimal classOid) throws Exception;
    
    
    public List<UserClassHolder> selectByUserOid(BigDecimal userOid)
            throws Exception;
    
    
    public void deleteByClassOid(BigDecimal classOid) throws Exception;
}
