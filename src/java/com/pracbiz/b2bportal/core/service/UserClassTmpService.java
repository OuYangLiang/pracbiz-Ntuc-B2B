package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.UserClassTmpHolder;

public interface UserClassTmpService extends BaseService<UserClassTmpHolder>,
        DBActionService<UserClassTmpHolder>
{
    public UserClassTmpHolder selectUserClassTmpByUserOidAndClassOid(
            BigDecimal userOid, BigDecimal classOid) throws Exception;
    
    
    public List<UserClassTmpHolder> selectUserClassTmpByUserOid(
            BigDecimal userOid) throws Exception;
    
    
    public void deleteByClassOid(BigDecimal classOid) throws Exception;
}
