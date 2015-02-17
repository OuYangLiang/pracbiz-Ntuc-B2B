package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

public enum SummaryPageAccessType
{

    B("SummaryPageAccessType.Buyer"),
    
    S("SummaryPageAccessType.Supplier");

    private String key;


    private SummaryPageAccessType(String key)
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
        for (SummaryPageAccessType rt : SummaryPageAccessType.values())
        {
            rlt.put(rt.name(), rt.getKey());
        }
        return rlt;
    }
}
