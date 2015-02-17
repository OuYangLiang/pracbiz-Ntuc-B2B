package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class BuyerMsgSettingReportHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal buyerOid;
    private String msgType;
    private String subType;
    private Boolean customizedReport;
    private String reportTemplate;


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public String getMsgType()
    {
        return msgType;
    }


    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }


    public String getSubType()
    {
        return subType;
    }


    public void setSubType(String subType)
    {
        this.subType = subType;
    }


    public Boolean getCustomizedReport()
    {
        return customizedReport;
    }


    public void setCustomizedReport(Boolean customizedReport)
    {
        this.customizedReport = customizedReport;
    }


    public String getReportTemplate()
    {
        return reportTemplate;
    }


    public void setReportTemplate(String reportTemplate)
    {
        this.reportTemplate = reportTemplate;
    }


    @Override
    public String getCustomIdentification()
    {
        return buyerOid + msgType + subType;
    }

}