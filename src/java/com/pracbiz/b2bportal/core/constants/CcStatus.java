package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;


import com.pracbiz.b2bportal.base.action.BaseAction;

public enum CcStatus
{
    NEW("CcStatus.new"),
    AMENDED("CcStatus.amended"),
    OUTDATED("CcStatus.outdated");

    private String key;


    private CcStatus(String key)
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
        for (CcStatus ms : CcStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
