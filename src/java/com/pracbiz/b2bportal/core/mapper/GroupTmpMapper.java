package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.GroupTmpExHolder;

public interface GroupTmpMapper extends BaseMapper<GroupTmpHolder>,
    DBActionMapper<GroupTmpHolder>, PaginatingMapper<GroupTmpExHolder>
{
    GroupTmpHolder selectGroupTmpByUserOid(BigDecimal userOid);
}