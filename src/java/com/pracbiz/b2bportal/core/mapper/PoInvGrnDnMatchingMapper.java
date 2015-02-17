package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingExHolder;

public interface PoInvGrnDnMatchingMapper extends
        BaseMapper<PoInvGrnDnMatchingHolder>,
        DBActionMapper<PoInvGrnDnMatchingHolder>,
        PaginatingMapper<PoInvGrnDnMatchingExHolder>
{
    //public List<PoInvGrnDnMatchingHolder> selectMatchingRecordNotMatchedStatus();
    
    
    public List<PoInvGrnDnMatchingHolder> selectAllRecordToExport(PoInvGrnDnMatchingHolder holder);
    
    
    public List<PoInvGrnDnMatchingHolder> selectUnclosedPendingExpiredMatchingRecords(Map<String, Object> param);
    
    
    public List<PoInvGrnDnMatchingHolder> selectClosedQtyOrPriceUnmatchedRecordsWhichIsWithoutDn(Map<String, Object> param);
    
    
    public List<PoInvGrnDnMatchingHolder> selectClosedUnmatchedRecords(Map<String, Object> param);
    
    
    public List<PoInvGrnDnMatchingHolder> selectVoidInvoiceMatchingRecords(BigDecimal buyerOid); 
    
    
    public List<PoInvGrnDnMatchingHolder> selectOutDatedPoRecords(BigDecimal buyerOid);
    
    
    public List<PoInvGrnDnMatchingHolder> selectMatchingRecordsWhichCanDoMatching(Map<String, Object> param);

    
    public List<PoInvGrnDnMatchingHolder> selectDiscrepancyRecordByBuyerAndSupplier(Map<String, Object> param);
    
    
    public List<PoInvGrnDnMatchingHolder> selectMatchingRecordByMatchingDateRangeUnionAllPending(Map<String, Object> param);
    
    
    public List<PoInvGrnDnMatchingHolder> selectApprovedMatchingRecordByInvStatusActionDateRange(Map<String, Object> param);
    
    
    public List<PoInvGrnDnMatchingHolder> selectOutstandingRecords(Map<String, Object> param);
    
    
    public List<PoInvGrnDnMatchingHolder> selectBuyerResolutionRecords(Map<String, Object> param);
    
    
    public List<PoInvGrnDnMatchingHolder> selectSupplierResolutionRecords(BigDecimal supplierOid);
}