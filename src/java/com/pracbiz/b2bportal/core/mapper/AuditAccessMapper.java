package com.pracbiz.b2bportal.core.mapper;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.AuditAccessHolder;


public interface AuditAccessMapper extends BaseMapper<AuditAccessHolder>,
    DBActionMapper<AuditAccessHolder>,PaginatingMapper<AuditAccessHolder>
{
    public AuditAccessHolder selectLastRecordByLoginId(String loginId);
}