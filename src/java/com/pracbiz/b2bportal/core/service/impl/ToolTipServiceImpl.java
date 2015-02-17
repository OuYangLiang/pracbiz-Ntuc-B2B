package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.ToolTipHolder;
import com.pracbiz.b2bportal.core.holder.extension.ToolTipExHolder;
import com.pracbiz.b2bportal.core.mapper.ToolTipMapper;
import com.pracbiz.b2bportal.core.service.ToolTipService;

public class ToolTipServiceImpl extends
        DBActionServiceDefaultImpl<ToolTipHolder> implements ToolTipService,
        ApplicationContextAware
{
    private ApplicationContext ctx;
    @Autowired
    private ToolTipMapper mapper;


    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException
    {
        this.ctx = context;
    }


    public ToolTipService getMeBean()
    {
        return ctx.getBean("ToolTipService", ToolTipService.class);
    }


    @Override
    public Map<String, List<ToolTipHolder>> selectTooltipsByPageIdAndAccessType(
            String pageId, String accessType, BaseAction action)
            throws Exception
    {
        if (StringUtils.isBlank(pageId) || StringUtils.isBlank(accessType)
                || action == null)
        {
            throw new IllegalArgumentException();
        }

        ToolTipExHolder parameter = new ToolTipExHolder();
        parameter.setAccessType(accessType);
        parameter.setPageId(pageId);

        List<ToolTipHolder> tooltips = mapper.selectAvailableTooltip(parameter);
        Map<String, List<ToolTipHolder>> rlts = new HashMap<String, List<ToolTipHolder>>();

        Iterator<ToolTipHolder> it = tooltips.iterator();

        while (it.hasNext())
        {
            ToolTipExHolder toolTip = (ToolTipExHolder) it.next();
            toolTip.setTooltipFieldLabel(action.getText(toolTip
                    .getTooltipFieldLabelKey()));
            if (rlts.containsKey(toolTip.getFieldId()))
            {
                List<ToolTipHolder> list = rlts.get(toolTip.getFieldId());
                list.add(toolTip);
                continue;
            }

            List<ToolTipHolder> list = new ArrayList<ToolTipHolder>();
            list.add(toolTip);
            rlts.put(toolTip.getFieldId(), list);
        }

        return rlts;
    }


    @Override
    public List<ToolTipHolder> selectTooltipsByPageIdAndAccessTypeAndFieldId(
            String pageId, String accessType, String fieldId, BaseAction action)
            throws Exception
    {
        if (StringUtils.isBlank(pageId) || StringUtils.isBlank(accessType)
                || StringUtils.isBlank(fieldId) || action == null)
        {
            throw new IllegalArgumentException(
                    ToolTipServiceImpl.class.getName()
                            + ".selectTooltipsByPageIdAndAccessTypeAndFieldId(){} with parameters - pageId:"
                            + pageId + ", accessType:" + accessType
                            + ", fieldId:" + fieldId + ", action:" + action);
        }

        Map<String, List<ToolTipHolder>> tooltips = this
                .getMeBean()
                .selectTooltipsByPageIdAndAccessType(pageId, accessType, action);

        return tooltips.get(fieldId);
    }


    @Override
    public List<ToolTipHolder> selectByFieldOid(BigDecimal fieldOid)
            throws Exception
    {
        if (fieldOid == null)
        {
            throw new IllegalArgumentException();
        }
        ToolTipHolder param = new ToolTipHolder();
        param.setFieldOid(fieldOid);
        return this.select(param);
    }


    @Override
    public List<ToolTipHolder> select(ToolTipHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void delete(ToolTipHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(ToolTipHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(ToolTipHolder oldObj, ToolTipHolder newObj)
            throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(ToolTipHolder oldObj,
            ToolTipHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }

}
