package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.RtvSummaryHolder;

public interface RtvHeaderMapper extends BaseMapper<RtvHeaderHolder>,
        DBActionMapper<RtvHeaderHolder>,PaginatingMapper<MsgTransactionsExHolder>
{
    public List<RtvSummaryHolder> selectAllRecordToExport(RtvSummaryHolder param);
}
