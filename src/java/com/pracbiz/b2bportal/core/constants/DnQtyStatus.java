package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum DnQtyStatus {
    PENDING("PoInvGrnDnMatchingQtyStatus.pending"), 
    ACCEPTED("PoInvGrnDnMatchingQtyStatus.accepted"), 
    REJECTED("PoInvGrnDnMatchingQtyStatus.rejected");

    private String key;


    private DnQtyStatus(String key)
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
        for (DnQtyStatus ms : DnQtyStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
