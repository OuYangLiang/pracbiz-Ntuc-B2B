package com.pracbiz.b2bportal.base.service;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;

public abstract class DBActionServiceDefaultImpl<T extends BaseHolder>
    implements DBActionService<T>
{

    @Override
    public void auditInsert(CommonParameterHolder cp, T newObj_)
        throws Exception
    {
        this.insert(newObj_);
    }

    
    @Override
    public void auditUpdateByPrimaryKeySelective(CommonParameterHolder cp,
        T oldObj_, T newObj_) throws Exception
    {
        this.updateByPrimaryKeySelective(oldObj_, newObj_);
    }

    
    @Override
    public void auditUpdateByPrimaryKey(CommonParameterHolder cp, T oldObj_,
        T newObj_) throws Exception
    {
        this.updateByPrimaryKey(oldObj_, newObj_);
    }

    
    @Override
    public void auditDelete(CommonParameterHolder cp, T oldObj_)
        throws Exception
    {
        this.delete(oldObj_);
    }

}
