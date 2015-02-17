package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoSummaryHolder;

public interface PoHeaderService extends
    PaginatingService<MsgTransactionsExHolder>, DBActionService<PoHeaderHolder>

{
    public List<PoHeaderHolder> selectPoHeadersByPoNoBuyerCodeAndSupplierCode(String poNo, String buyerCode, String supplierCode) throws Exception;
    
    
    public PoHeaderHolder selectPoHeaderByKey(BigDecimal docOid) throws Exception;
    
    
    public List<PoHeaderHolder> selectLocalBuyerPoHeaderNotInPoInvGrnDnMatching();
    
    
    public List<PoHeaderHolder> selectPoHeaderWhichIsNotFullyInPoInvGrnDnMatching(BigDecimal buyerOid) throws Exception;
    
    
    public List<PoHeaderHolder> selectPoHeadersByPoNo(
        BigDecimal buyerOid, String poNo, String supplierCode)
        throws Exception;
    

    public PoHeaderHolder selectEffectivePoHeaderByPoNo(BigDecimal buyerOid,
        String poNo, String buyerGivenSupplierCode) throws Exception;
    
    
    public List<BaseHolder> getListOfSummaryQuickPo(PoSummaryHolder param) throws Exception;
    
    
    public List<PoHeaderHolder> selectPoHeaderToGenerateBatchInvoice(Date createDateFrom, Date createDateTo) throws Exception;
    
    
    public List<PoSummaryHolder> selectAllRecordToExport(PoSummaryHolder param) throws Exception;
}
