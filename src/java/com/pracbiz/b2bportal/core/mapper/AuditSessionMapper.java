package com.pracbiz.b2bportal.core.mapper;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.AuditSessionHolder;
import com.pracbiz.b2bportal.core.holder.extension.AuditSessionExHolder;

public interface AuditSessionMapper extends BaseMapper<AuditSessionHolder>,
    DBActionMapper<AuditSessionHolder>, PaginatingMapper<AuditSessionExHolder>
{

}