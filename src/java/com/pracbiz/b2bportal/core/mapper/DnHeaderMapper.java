package com.pracbiz.b2bportal.core.mapper;

import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.report.excel.RtvGiDnReportParameter;

public interface DnHeaderMapper extends BaseMapper<DnHeaderHolder>,
        DBActionMapper<DnHeaderHolder>,PaginatingMapper<MsgTransactionsExHolder>
{
    List<DnHeaderHolder> selectNoDisputeDnHeadersByBuyerAndBufferingDays(Map<String, Object> map);
    
    List<DnHeaderHolder> selectDisputedAndAuditUnfinishedRecord(Map<String, Object> map);
    
    List<DnHeaderHolder> selectClosedDnRecordsBySupplierAndTimeRange(Map<String, Object> map);
    
    List<DnHeaderHolder> selectResolutionRecordsByBuyer(Map<String, Object> map);
    
    List<DnHeaderHolder> selectOutstandingRecordsByBuyer(Map<String, Object> map);
    
    DnHeaderHolder selectEffectiveDnHeaderByOriginalFilename(Map<String, Object> map);
    
    public List<DnSummaryHolder> selectAllRecordToExport(DnSummaryHolder param);
    
}
