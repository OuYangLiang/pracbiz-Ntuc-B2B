package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.core.holder.OperationUrlHolder;

public interface OperationUrlMapper extends BaseMapper<OperationUrlHolder>
{
    List<OperationUrlHolder> selectOperationUrlByUser(BigDecimal userOid);
    
    
    List<OperationUrlHolder> selectOperationUrlByGroup(BigDecimal groupOid);
}