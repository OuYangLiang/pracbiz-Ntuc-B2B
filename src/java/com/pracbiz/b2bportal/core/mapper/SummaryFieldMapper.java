package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.SummaryFieldHolder;

public interface SummaryFieldMapper extends BaseMapper<SummaryFieldHolder>,
        DBActionMapper<SummaryFieldHolder>
{
    public List<SummaryFieldHolder> selectAvailableSummaryFields(SummaryFieldHolder parameter);
}
