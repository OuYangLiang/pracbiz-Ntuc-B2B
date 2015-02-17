package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

public enum RoleType
{
    ADMIN("RoleType.Admin"),

    BUYER("RoleType.Buyer"),
    
    SUPPLIER("RoleType.Supplier");

    private String key;


    private RoleType(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return key;
    }
    
    
    public static Map<String, String> toMapValue()
    {
        Map<String,String> rlt = new HashMap<String,String>();
        for (RoleType rt : RoleType.values())
        {
            rlt.put(rt.name(), rt.getKey());
        }
        return rlt;
    }
}
