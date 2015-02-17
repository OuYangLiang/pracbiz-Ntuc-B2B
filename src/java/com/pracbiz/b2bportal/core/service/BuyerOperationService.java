package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerOperationHolder;

public interface BuyerOperationService extends
        BaseService<BuyerOperationHolder>,
        DBActionService<BuyerOperationHolder>
{
    public List<BuyerOperationHolder> selectByBuyerOid(BigDecimal buyerOid)
            throws Exception;
}
