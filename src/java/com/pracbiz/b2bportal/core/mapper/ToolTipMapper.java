package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.ToolTipHolder;
import com.pracbiz.b2bportal.core.holder.extension.ToolTipExHolder;

public interface ToolTipMapper extends BaseMapper<ToolTipHolder>,
        DBActionMapper<ToolTipHolder>
{
    public List<ToolTipHolder> selectAvailableTooltip(ToolTipExHolder parameter);
}
