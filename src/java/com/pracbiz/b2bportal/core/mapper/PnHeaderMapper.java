package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.PnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PnSummaryHolder;

public interface PnHeaderMapper extends BaseMapper<PnHeaderHolder>,
    DBActionMapper<PnHeaderHolder>, PaginatingMapper<MsgTransactionsExHolder>
{
    public List<PnSummaryHolder> selectAllRecordToExport(PnSummaryHolder param);
}
