package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.SalesHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SalesSummaryHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author Yinchi
 */
public interface SalesHeaderService extends BaseService<SalesHeaderHolder>,
    PaginatingService<MsgTransactionsExHolder>, DBActionService<SalesHeaderHolder>

{
    public SalesHeaderHolder selectSalesHeaderByKey(BigDecimal salesOid) throws Exception;
    
    public List<SalesSummaryHolder> selectAllRecordToExport(SalesSummaryHolder param) throws Exception;
}
