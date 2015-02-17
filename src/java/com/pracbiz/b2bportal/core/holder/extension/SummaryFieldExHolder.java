package com.pracbiz.b2bportal.core.holder.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.core.holder.SummaryFieldHolder;

public class SummaryFieldExHolder extends SummaryFieldHolder
{
    private static final long serialVersionUID = 7390536670452079025L;
    
    private String fieldLabel;
    private String pageId;
    private String accessType;
    private List<ToolTipExHolder> selectedToolTips;
    private List<ToolTipExHolder> otherToolTips;

    public String getFieldLabel()
    {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel)
    {
        this.fieldLabel = fieldLabel;
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
    
    public List<ToolTipExHolder> getSelectedToolTips()
    {
        return selectedToolTips;
    }

    public void setSelectedToolTips(List<ToolTipExHolder> selectedToolTips)
    {
        this.selectedToolTips = selectedToolTips;
    }

    public List<ToolTipExHolder> getOtherToolTips()
    {
        return otherToolTips;
    }

    public void setOtherToolTips(List<ToolTipExHolder> otherToolTips)
    {
        this.otherToolTips = otherToolTips;
    }

    public void calculateOtherToolTips(List<ToolTipExHolder> allToolTips, BaseAction action)
    {
        if(allToolTips == null)
        {
            this.setOtherToolTips(new ArrayList<ToolTipExHolder>());
            return;
        }
        if(this.getSelectedToolTips() == null || this.getSelectedToolTips().isEmpty())
        {
            this.setOtherToolTips(allToolTips);
            return;
        }
        List<ToolTipExHolder> rlt = new ArrayList<ToolTipExHolder>();
        rlt.addAll(allToolTips);
        Iterator<ToolTipExHolder> it = allToolTips.iterator();
        while (it.hasNext())
        {
            ToolTipExHolder obj = it.next();
            for(ToolTipExHolder tip : this.getSelectedToolTips())
            {
                if(obj.getTooltipFieldOid().compareTo(tip.getTooltipFieldOid()) == 0)
                {
                    tip.setTooltipFieldLabel(obj.getTooltipFieldLabel());
                    rlt.remove(obj);
                    break;
                }
            }
        }
        this.setOtherToolTips(rlt);
    }
}
