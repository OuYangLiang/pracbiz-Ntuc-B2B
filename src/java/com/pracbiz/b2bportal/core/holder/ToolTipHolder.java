package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class ToolTipHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -458325987075892923L;

    private BigDecimal fieldOid;

    private BigDecimal tooltipFieldOid;

    private Integer showOrder;



    public BigDecimal getFieldOid()
    {
        return fieldOid;
    }

    public void setFieldOid(BigDecimal fieldOid)
    {
        this.fieldOid = fieldOid;
    }

    public BigDecimal getTooltipFieldOid()
    {
        return tooltipFieldOid;
    }

    public void setTooltipFieldOid(BigDecimal tooltipFieldOid)
    {
        this.tooltipFieldOid = tooltipFieldOid;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(fieldOid + "" + tooltipFieldOid);
    }
}