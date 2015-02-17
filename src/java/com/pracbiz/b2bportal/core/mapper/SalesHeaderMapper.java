package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.SalesHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SalesSummaryHolder;

public interface SalesHeaderMapper extends BaseMapper<SalesHeaderHolder>,
        DBActionMapper<SalesHeaderHolder>,PaginatingMapper<MsgTransactionsExHolder>
{
    public List<SalesSummaryHolder> selectAllRecordToExport(SalesSummaryHolder param);
}
