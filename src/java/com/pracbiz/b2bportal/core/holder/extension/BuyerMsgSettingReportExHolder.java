package com.pracbiz.b2bportal.core.holder.extension;

import java.util.Map;

import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;

public class BuyerMsgSettingReportExHolder extends BuyerMsgSettingReportHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<String, String> standard;
    private Map<String, String> customized;
    private String standardReportTemplate;
    private String customizedReportTemplate;


    public Map<String, String> getStandard()
    {
        return standard;
    }


    public void setStandard(Map<String, String> standard)
    {
        this.standard = standard;
    }


    public Map<String, String> getCustomized()
    {
        return customized;
    }


    public void setCustomized(Map<String, String> customized)
    {
        this.customized = customized;
    }


    public String getStandardReportTemplate()
    {
        return standardReportTemplate;
    }


    public void setStandardReportTemplate(String standardReportTemplate)
    {
        this.standardReportTemplate = standardReportTemplate;
    }


    public String getCustomizedReportTemplate()
    {
        return customizedReportTemplate;
    }


    public void setCustomizedReportTemplate(String customizedReportTemplate)
    {
        this.customizedReportTemplate = customizedReportTemplate;
    }



}
