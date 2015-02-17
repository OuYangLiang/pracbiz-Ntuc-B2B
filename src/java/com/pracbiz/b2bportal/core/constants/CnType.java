package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum CnType
{
    CON("CnType.con");
    
    private String key;


    private CnType(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return key;
    }


    public static Map<String, String> toMapValue(BaseAction action)
    {
        Map<String, String> rlt = new HashMap<String, String>();
        for (CnType rt : CnType.values())
        {
            rlt.put(rt.name(), action.getText(rt.getKey()));
        }
        return rlt;
    }
}
