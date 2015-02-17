package com.pracbiz.b2bportal.base.service;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;



public interface MakerCheckerService<T /*main holder*/extends BaseHolder, S /*tmp holder*/extends BaseHolder>
{
    public void mkCreate(CommonParameterHolder cp, S newObj_) throws Exception;

    
    public void mkUpdate(CommonParameterHolder cp, S oldObj_, S newObj_)
        throws Exception;

    
    public void mkDelete(CommonParameterHolder cp, S oldObj_) throws Exception;

    
    public void mkApprove(CommonParameterHolder cp, T main, S tmp)
        throws Exception;

    
    public void mkReject(CommonParameterHolder cp, T main, S tmp)
        throws Exception;

    
    public void mkWithdraw(CommonParameterHolder cp, T main, S tmp)
        throws Exception;
}
