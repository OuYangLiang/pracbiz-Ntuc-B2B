package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.CnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public interface CnHeaderMapper extends BaseMapper<CnHeaderHolder>,
        DBActionMapper<CnHeaderHolder>, PaginatingMapper<MsgTransactionsExHolder>
{
    public List<CnSummaryHolder> selectAllRecordToExport(CnSummaryHolder param);
}