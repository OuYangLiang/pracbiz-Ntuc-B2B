package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.ToolTipHolder;

public interface ToolTipService extends BaseService<ToolTipHolder>,
        DBActionService<ToolTipHolder>
{
    public List<ToolTipHolder> selectTooltipsByPageIdAndAccessTypeAndFieldId(
            String pageId, String accessType, String fieldId, BaseAction action)
            throws Exception;


    public Map<String, List<ToolTipHolder>> selectTooltipsByPageIdAndAccessType(
            String pageId, String accessType, BaseAction action)
            throws Exception;


    public List<ToolTipHolder> selectByFieldOid(BigDecimal fieldOid)
            throws Exception;
}
