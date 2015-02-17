package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.AuditTrailHolder;

public interface AuditTrailMapper extends BaseMapper<AuditTrailHolder>,
        DBActionMapper<AuditTrailHolder>, PaginatingMapper<AuditTrailHolder>
{
    List<AuditTrailHolder> selectWithBLOBs(AuditTrailHolder record);


    int updateByPrimaryKeyWithBLOBs(AuditTrailHolder record);
}