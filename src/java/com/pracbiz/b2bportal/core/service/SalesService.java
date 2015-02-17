package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SalesHolder;

public interface SalesService  extends DBActionService<SalesHolder>
{
    public SalesHolder selectSalesByKey(BigDecimal salesOid) throws Exception;
    

    public byte[] exportExcel(List<BigDecimal> salesOids) throws Exception;
    
}
