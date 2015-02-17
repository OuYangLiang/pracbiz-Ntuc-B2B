package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author YinChi
 */
public interface SalesDateLocationDetailService extends BaseService<SalesDateLocationDetailHolder>,
        DBActionService<SalesDateLocationDetailHolder>
{
    public List<SalesDateLocationDetailHolder> selectSalesLocationDetailByKey(BigDecimal salesOid) throws Exception;
}
