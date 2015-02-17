package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.OperationUrlHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.extension.ModuleExHolder;

public interface LoginService
{
    public List<ModuleExHolder> selectMenusByUserOid(BaseAction action,
            BigDecimal userOid) throws Exception;


    public List<OperationUrlHolder> selectOperationUrlsByUserOid(
            BigDecimal userOid) throws Exception;


    public void doLogin(CommonParameterHolder cp, BaseHolder newObj,
            BaseHolder oldObj, String sessionId)
            throws Exception;
    
    public List<String> selectgetPermitURLsByUserOid(BigDecimal userOid,
        List<ModuleExHolder> modules) throws Exception;
    
    
    public void doLogout(CommonParameterHolder cp,UserProfileHolder user, String sessionId)
        throws Exception; 
}
