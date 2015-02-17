package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.GroupHolder;

public interface GroupMapper extends BaseMapper<GroupHolder>,
    DBActionMapper<GroupHolder>
{
    GroupHolder selectGroupByUserOid(BigDecimal userOid);
    
    List<GroupHolder> selectGroupByBuyerOidAndOperationId(Map<String, Object> map);
}