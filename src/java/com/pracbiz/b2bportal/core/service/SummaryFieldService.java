package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SummaryFieldHolder;

public interface SummaryFieldService extends BaseService<SummaryFieldHolder>,
        DBActionService<SummaryFieldHolder>
{
    public List<SummaryFieldHolder> selectSummaryAvailableFieldsByPageIdAndAccessType(
            String pageId, String accessType, BaseAction action)
            throws Exception;


    public Map<String, String> selectSortFieldByPageIdAndAccessType(
            String pageId, String accessType, BaseAction action)
            throws Exception;


    public List<SummaryFieldHolder> selectBySettingOid(BigDecimal settingOid)
            throws Exception;


    public SummaryFieldHolder selectByKey(BigDecimal fieldOid) throws Exception;


    public void updateWithToolTip(CommonParameterHolder cp,
            SummaryFieldHolder oldField, SummaryFieldHolder newField)
            throws Exception;
}
