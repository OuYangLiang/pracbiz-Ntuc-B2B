package com.pracbiz.b2bportal.core.holder.extension;

import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;

public class BuyerMsgSettingExHolder extends BuyerMsgSettingHolder
{
    private static final long serialVersionUID = -2665403772866103504L;
    private String alertIntervalValue;
    private String msgDesc;
    private boolean basicData;
    private List<String> fileFormatList;
    private Map<String, BuyerMsgSettingReportExHolder> subTypeReportMap;


    public String getAlertIntervalValue()
    {
        return alertIntervalValue;
    }


    public void setAlertIntervalValue(String alertIntervalValue)
    {
        this.alertIntervalValue = alertIntervalValue;
    }


    public String getMsgDesc()
    {
        return msgDesc;
    }


    public void setMsgDesc(String msgDesc)
    {
        this.msgDesc = msgDesc;
    }


    public boolean isBasicData()
    {
        return basicData;
    }


    public void setBasicData(boolean basicData)
    {
        this.basicData = basicData;
    }


    public List<String> getFileFormatList()
    {
        return fileFormatList;
    }


    public void setFileFormatList(List<String> fileFormatList)
    {
        this.fileFormatList = fileFormatList;
    }


    public Map<String, BuyerMsgSettingReportExHolder> getSubTypeReportMap()
    {
        return subTypeReportMap;
    }


    public void setSubTypeReportMap(
            Map<String, BuyerMsgSettingReportExHolder> subTypeReportMap)
    {
        this.subTypeReportMap = subTypeReportMap;
    }

}
