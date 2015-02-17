package com.pracbiz.b2bportal.core.mapper;

import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public interface InvHeaderMapper extends BaseMapper<InvHeaderHolder>,
    DBActionMapper<InvHeaderHolder>, PaginatingMapper<MsgTransactionsExHolder>
{
    public List<InvHeaderHolder> selectInvToGenerateDn(Map<?, ?> map);
    
    
    public List<InvSummaryHolder> selectAllRecordToExport(InvSummaryHolder param);
}
