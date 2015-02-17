package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.GiSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public interface GiHeaderMapper extends BaseMapper<GiHeaderHolder>,
        DBActionMapper<GiHeaderHolder>,PaginatingMapper<MsgTransactionsExHolder>
{
    List<GiHeaderHolder> selectGiHeadersWithoutDn(BigDecimal buyerOid);
    
    public List<GiSummaryHolder> selectAllRecordToExport(GiSummaryHolder param);
}