package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.ReadStatusReportHolder;

public interface MsgTransactionsMapper extends
        BaseMapper<MsgTransactionsHolder>,
        DBActionMapper<MsgTransactionsHolder>
{
    public List<ReadStatusReportHolder> selectMsgsForReport(MsgTransactionsHolder param);
}
