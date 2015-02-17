package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoSummaryHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

public interface PoService  extends DBActionService<PoHolder>
{
    public PoHolder selectPoByKey(BigDecimal poOid) throws Exception;
    

    public byte[] exportSummaryExcel(List<PoSummaryHolder> params, boolean storeFlag) throws Exception;
    
    
    public ReportEngineParameter<PoHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception;
}
