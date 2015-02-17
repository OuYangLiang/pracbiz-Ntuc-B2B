package com.pracbiz.b2bportal.core.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.GrnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public interface GrnHeaderMapper extends BaseMapper<GrnHeaderHolder>,
    DBActionMapper<GrnHeaderHolder>, PaginatingMapper<MsgTransactionsExHolder>
{
    public Date selectMaxGrnReceiveDateByBuyerSupplierPoReceiveStoreCode(Map<?, ?> map);
    
    
    public List<GrnHeaderHolder> selectGrnHeaderByStoreTypeUser(Map<String, Object> map);
    
    
    public List<GrnSummaryHolder> selectAllRecordToExport(GrnSummaryHolder param);
}
