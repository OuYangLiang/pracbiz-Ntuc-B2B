package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

public enum GroupType 
{
    BUYER("GroupType.Buyer"),

    SUPPLIER("GroupType.Supplier");

    private String key;


    private GroupType(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return key;
    }


    public static Map<String, String> toMapValue()
    {
        Map<String, String> rlt = new HashMap<String, String>();
        for (GroupType rt : GroupType.values())
        {
            rlt.put(rt.name(), rt.getKey());
        }
        return rlt;
    }
}
