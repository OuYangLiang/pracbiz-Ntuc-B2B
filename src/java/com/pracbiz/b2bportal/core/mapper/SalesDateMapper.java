package com.pracbiz.b2bportal.core.mapper;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.SalesDateHolder;

public interface SalesDateMapper extends BaseMapper<SalesDateHolder>,
    DBActionMapper<SalesDateHolder>, PaginatingMapper<SalesDateHolder>
{
    
}