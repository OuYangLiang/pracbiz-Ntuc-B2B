package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum InvStatus
{
    NEW("InvStatus.new"),
    SUBMIT("InvStatus.submit"),
    VOID("InvStatus.void"),
    VOID_OUTDATED("InvStatus.voidOutdated");

    private String key;


    private InvStatus(String key)
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
        for (InvStatus ms : InvStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
