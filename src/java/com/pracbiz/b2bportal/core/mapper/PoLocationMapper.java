package com.pracbiz.b2bportal.core.mapper;

import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.report.excel.MissingGrnReportParameter;

public interface PoLocationMapper extends BaseMapper<PoLocationHolder>,
        DBActionMapper<PoLocationHolder>
{
    public List<PoLocationHolder> selectOptionalLocations(PoLocationHolder parameter);
    
    
    public List<MissingGrnReportParameter> selectMissingGrnReprotRecords(Map<String, Object> map);
}
