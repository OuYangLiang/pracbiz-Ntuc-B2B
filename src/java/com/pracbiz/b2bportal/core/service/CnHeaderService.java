package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.CnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public interface CnHeaderService extends BaseService<CnHeaderHolder>,
        PaginatingService<MsgTransactionsExHolder>,
        DBActionService<CnHeaderHolder>
{
    public CnHeaderHolder selectEffectiveCnHeaderByKey(BigDecimal buyerOid, String buyerGivenSupplierCode, String cnNo) throws Exception;
    
    public CnHeaderHolder selectCnHeaderByKey(BigDecimal cnOid) throws Exception;
    
    public CnHeaderHolder selectEffectiveCnHeaderByPoNo(BigDecimal buyerOid, String buyerGivenSupplierCode, String poNo) throws Exception;
    
    public void sendCreditNote(CommonParameterHolder cp, BigDecimal cnOid) throws Exception;
    
    public List<CnSummaryHolder> selectAllRecordToExport(CnSummaryHolder param) throws Exception;
    
}
