package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum PoInvGrnDnMatchingInvStatus
{
    PENDING("MatchingActionStatus.pending"),
    APPROVED("MatchingActionStatus.approved"),
    SYS_APPROVED("MatchingActionStatus.sysapproved");

    private String key;


    private PoInvGrnDnMatchingInvStatus(String key)
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
        for (PoInvGrnDnMatchingInvStatus ms : PoInvGrnDnMatchingInvStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
