package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.UserSubclassTmpHolder;

public interface UserSubclassTmpService extends
        BaseService<UserSubclassTmpHolder>,
        DBActionService<UserSubclassTmpHolder>
{
    public UserSubclassTmpHolder selectUserSubclassTmpByUserOidAndSubClassOid(
            BigDecimal userOid, BigDecimal subclassOid) throws Exception;
    
    
    public List<UserSubclassTmpHolder> selectUserSubclassTmpByUserOid(
            BigDecimal userOid) throws Exception;
    
    
    public void deleteBySubclassOid(BigDecimal subclassOid) throws Exception;
}
