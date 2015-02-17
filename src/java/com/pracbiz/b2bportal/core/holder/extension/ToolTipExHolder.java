package com.pracbiz.b2bportal.core.holder.extension;

import com.pracbiz.b2bportal.core.holder.ToolTipHolder;

public class ToolTipExHolder extends ToolTipHolder
{
    private static final long serialVersionUID = 7906361046278810870L;

    private String fieldId;
    private String tooltipFieldId;
    private String tooltipFieldLabelKey;
    private String tooltipFieldLabel;
    private String pageId;
    private String accessType;
    
    

    public String getFieldId()
    {
        return fieldId;
    }

    public void setFieldId(String fieldId)
    {
        this.fieldId = fieldId;
    }

    public String getTooltipFieldLabelKey()
    {
        return tooltipFieldLabelKey;
    }

    public void setTooltipFieldLabelKey(String tooltipFieldLabelKey)
    {
        this.tooltipFieldLabelKey = tooltipFieldLabelKey;
    }

    public String getTooltipFieldLabel()
    {
        return tooltipFieldLabel;
    }

    public void setTooltipFieldLabel(String tooltipFieldLabel)
    {
        this.tooltipFieldLabel = tooltipFieldLabel;
    }

    public String getPageId()
    {
        return pageId;
    }

    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }

    public String getAccessType()
    {
        return accessType;
    }

    public void setAccessType(String accessType)
    {
        this.accessType = accessType;
    }

    public String getTooltipFieldId()
    {
        return tooltipFieldId;
    }

    public void setTooltipFieldId(String tooltipFieldId)
    {
        this.tooltipFieldId = tooltipFieldId;
    }
}
