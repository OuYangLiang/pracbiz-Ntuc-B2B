package com.pracbiz.b2bportal.core.mapper;

import java.util.List;
import java.util.Map;



import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.report.excel.MissingGiReportParameter;
import com.pracbiz.b2bportal.core.report.excel.RtvGiDnReportParameter;

public interface RtvLocationMapper extends BaseMapper<RtvLocationHolder>,
        DBActionMapper<RtvLocationHolder>
{
    public List<RtvLocationHolder> selectOptionalLocations(RtvLocationHolder parameter);
    
    public List<MissingGiReportParameter> selectMissingGiReportRecords(Map<String, Object> map);
    
    public List<RtvGiDnReportParameter> selectRtvGiDnWarningReportData(Map<String, Object> map);
}
