package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum ReadStatusReportActionStatus
{
    DN("ReadStatusReportActionStatus.DN"),
    GRN("ReadStatusReportActionStatus.GRN"),
    PN("ReadStatusReportActionStatus.PN"),
    PO("ReadStatusReportActionStatus.PO"),
    RTV("ReadStatusReportActionStatus.RTV");

    private String key;


    private ReadStatusReportActionStatus(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return key;
    }
    
    
    public static Map<String, String> toMapValue(BaseAction action)
    {
        Map<String,String> rlt = new HashMap<String,String>();
        for (ReadStatusReportActionStatus rsr : ReadStatusReportActionStatus.values())
        {
            rlt.put(rsr.name(), action.getText(rsr.getKey()));
        }
        return rlt;
    }
}
