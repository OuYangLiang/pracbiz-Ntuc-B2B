package com.pracbiz.b2bportal.base.service;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;

public interface DBActionService<T extends BaseHolder>
{
    public void insert(T newObj_) throws Exception;

    public void auditInsert(CommonParameterHolder cp, T newObj_)
        throws Exception;

    
    public void updateByPrimaryKeySelective(T oldObj_, T newObj_)
        throws Exception;

    public void auditUpdateByPrimaryKeySelective(CommonParameterHolder cp,
        T oldObj_, T newObj_) throws Exception;

    
    public void updateByPrimaryKey(T oldObj_, T newObj_) throws Exception;

    public void auditUpdateByPrimaryKey(CommonParameterHolder cp, T oldObj_,
        T newObj_) throws Exception;

    
    public void delete(T oldObj_) throws Exception;

    public void auditDelete(CommonParameterHolder cp, T oldObj_)
        throws Exception;

}
