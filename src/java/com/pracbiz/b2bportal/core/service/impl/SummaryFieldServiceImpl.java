package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SummaryFieldHolder;
import com.pracbiz.b2bportal.core.holder.ToolTipHolder;
import com.pracbiz.b2bportal.core.holder.extension.SummaryFieldExHolder;
import com.pracbiz.b2bportal.core.mapper.SummaryFieldMapper;
import com.pracbiz.b2bportal.core.service.SummaryFieldService;
import com.pracbiz.b2bportal.core.service.ToolTipService;

public class SummaryFieldServiceImpl extends
        DBActionServiceDefaultImpl<SummaryFieldHolder> implements
        SummaryFieldService, ApplicationContextAware
{
    private ApplicationContext ctx;
    @Autowired
    private SummaryFieldMapper mapper;
    @Autowired
    private ToolTipService toolTipService;


    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException
    {
        this.ctx = context;
    }


    public SummaryFieldService getMeBean()
    {
        return ctx.getBean("summaryFieldService", SummaryFieldService.class);
    }


    @Override
    public List<SummaryFieldHolder> select(SummaryFieldHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<SummaryFieldHolder> selectSummaryAvailableFieldsByPageIdAndAccessType(
            String pageId, String accessType, BaseAction action)
            throws Exception
    {
        if (StringUtils.isBlank(pageId) || StringUtils.isBlank(accessType)
                || action == null)
        {
            throw new IllegalArgumentException();
        }

        SummaryFieldExHolder parameter = new SummaryFieldExHolder();
        parameter.setAccessType(accessType);
        parameter.setPageId(pageId);
        parameter.setAvailable(true);
        List<SummaryFieldHolder> rlt = mapper
                .selectAvailableSummaryFields(parameter);

        if (rlt == null || rlt.isEmpty()) return rlt;

        for (SummaryFieldHolder field : rlt)
        {
            SummaryFieldExHolder param = (SummaryFieldExHolder) field;
            param.setFieldLabel(action.getText(param.getFieldLabelKey()));
        }

        return rlt;
    }


    @Override
    public Map<String, String> selectSortFieldByPageIdAndAccessType(
            String pageId, String accessType, BaseAction action)
            throws Exception
    {
        Map<String, String> sortMap = new HashMap<String, String>();

        List<SummaryFieldHolder> availableFields = this.getMeBean()
                .selectSummaryAvailableFieldsByPageIdAndAccessType(pageId,
                        accessType, action);

        if (availableFields == null || availableFields.isEmpty())
        {
            return sortMap;
        }

        for (SummaryFieldHolder field : availableFields)
        {
            if (field.getSortable())
            {
                sortMap.put(field.getFieldId(), field.getFieldColumn());
            }
        }

        return sortMap;
    }


    @Override
    public List<SummaryFieldHolder> selectBySettingOid(BigDecimal settingOid)
            throws Exception
    {
        if (settingOid == null)
        {
            throw new IllegalArgumentException();
        }
        SummaryFieldHolder param = new SummaryFieldHolder();
        param.setSettingOid(settingOid);
        return this.select(param);
    }


    @Override
    public SummaryFieldHolder selectByKey(BigDecimal fieldOid) throws Exception
    {
        if (fieldOid == null)
        {
            throw new IllegalArgumentException();
        }
        SummaryFieldHolder param = new SummaryFieldHolder();
        param.setFieldOid(fieldOid);
        List<SummaryFieldHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }


    @Override
    public void delete(SummaryFieldHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(SummaryFieldHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(SummaryFieldHolder oldObj,
            SummaryFieldHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(SummaryFieldHolder oldObj,
            SummaryFieldHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public void updateWithToolTip(CommonParameterHolder cp,
            SummaryFieldHolder oldField, SummaryFieldHolder newField)
            throws Exception
    {
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldField,
                newField);
        ToolTipHolder tip = new ToolTipHolder();
        tip.setFieldOid(newField.getFieldOid());
        toolTipService.delete(tip);
        List<ToolTipHolder> toolTips = newField.getToolTips();
        if(toolTips == null || toolTips.isEmpty())
        {
            return;
        }
        Iterator<ToolTipHolder> it = toolTips.iterator();
        while (it.hasNext())
        {
            ToolTipHolder obj = it.next();
            toolTipService.insert(obj);
        }
    }
}
