package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerGivenSupplierOperationHolder;

public interface BuyerGivenSupplierOperationService extends
        BaseService<BuyerGivenSupplierOperationHolder>,
        DBActionService<BuyerGivenSupplierOperationHolder>
{
    public List<BuyerGivenSupplierOperationHolder> selectByBuyerOid(
            BigDecimal buyerOid) throws Exception;
    
}
