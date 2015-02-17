package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.CcHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.CcSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public interface CcHeaderMapper extends BaseMapper<CcHeaderHolder>,
    DBActionMapper<CcHeaderHolder>, PaginatingMapper<MsgTransactionsExHolder>
{
    public List<CcSummaryHolder> selectAllRecordToExport(CcSummaryHolder param);
}
