package com.pracbiz.b2bportal.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SummaryPageSettingHolder;
import com.pracbiz.b2bportal.core.mapper.SummaryPageSettingMapper;
import com.pracbiz.b2bportal.core.service.SummaryPageSettingService;

public class SummaryPageSettingServiceImpl extends
        DBActionServiceDefaultImpl<SummaryPageSettingHolder> implements
        SummaryPageSettingService
{
    @Autowired
    private SummaryPageSettingMapper mapper;
    
    @Override
    public List<SummaryPageSettingHolder> select(SummaryPageSettingHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void delete(SummaryPageSettingHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(SummaryPageSettingHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(SummaryPageSettingHolder oldObj,
            SummaryPageSettingHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(SummaryPageSettingHolder oldObj,
            SummaryPageSettingHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public List<SummaryPageSettingHolder> selectByPageId(String pageId)
            throws Exception
    {
        if (pageId == null || pageId.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        SummaryPageSettingHolder param = new SummaryPageSettingHolder();
        param.setPageId(pageId);
        return this.select(param);
    }

}
