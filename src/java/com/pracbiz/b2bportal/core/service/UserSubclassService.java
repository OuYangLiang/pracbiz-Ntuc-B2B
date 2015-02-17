package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;

public interface UserSubclassService extends BaseService<UserSubclassHolder>,
        DBActionService<UserSubclassHolder>
{
    public UserSubclassHolder selectByUserOidAndSubclassOid(BigDecimal userOid,
            BigDecimal subclassOid) throws Exception;
    
    
    public List<UserSubclassHolder> selectByUserOid(BigDecimal userOid)
            throws Exception;
    
    
    public void deleteBySubclassOid(BigDecimal subclassOid) throws Exception;
}
