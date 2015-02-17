package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SummaryFieldHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -8042139461813400186L;

    private BigDecimal fieldOid;
    
    private String fieldId;

    private String fieldColumn;

    private String fieldLabelKey;

    private Boolean available;

    private BigDecimal fieldWidth;

    private Boolean sortable;

    private Integer showOrder;

    private BigDecimal settingOid;
    
    private List<ToolTipHolder> toolTips;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId == null ? null : fieldId.trim();
    }

    public BigDecimal getFieldOid()
    {
        return fieldOid;
    }

    public void setFieldOid(BigDecimal fieldOid)
    {
        this.fieldOid = fieldOid;
    }

    public String getFieldColumn()
    {
        return fieldColumn;
    }

    public void setFieldColumn(String fieldColumn)
    {
        this.fieldColumn = fieldColumn;
    }

    public String getFieldLabelKey()
    {
        return fieldLabelKey;
    }

    public void setFieldLabelKey(String fieldLabelKey)
    {
        this.fieldLabelKey = fieldLabelKey;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public BigDecimal getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(BigDecimal fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public BigDecimal getSettingOid() {
        return settingOid;
    }

    public void setSettingOid(BigDecimal settingOid) {
        this.settingOid = settingOid;
    }

    public List<ToolTipHolder> getToolTips()
    {
        return toolTips;
    }

    public void setToolTips(List<ToolTipHolder> toolTips)
    {
        this.toolTips = toolTips;
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(fieldOid);
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return fieldId;
    }
}