package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;

public interface ControlParameterMapper extends
    BaseMapper<ControlParameterHolder>, DBActionMapper<ControlParameterHolder>
{
    List<ControlParameterHolder> getAllControlParameters();
}