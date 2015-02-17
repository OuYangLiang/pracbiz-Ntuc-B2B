package com.pracbiz.b2bportal.core.service;

import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SummaryPageSettingHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface SummaryPageSettingService extends
        BaseService<SummaryPageSettingHolder>,
        DBActionService<SummaryPageSettingHolder>
{
    public List<SummaryPageSettingHolder> selectByPageId(String pageId)
            throws Exception;
}
