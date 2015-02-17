package com.pracbiz.b2bportal.core.service;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.core.holder.CurrencyHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface CurrencyService extends BaseService<CurrencyHolder>
{
    public CurrencyHolder selectByCurrCode(String currCode) throws Exception;
}
