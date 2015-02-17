package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.RunningNumberHolder;

public interface RunningNumberService extends
        DBActionService<RunningNumberHolder>
{
    public String generateNumber(BigDecimal companyOid, String numberType,
            Integer length) throws Exception;
}
