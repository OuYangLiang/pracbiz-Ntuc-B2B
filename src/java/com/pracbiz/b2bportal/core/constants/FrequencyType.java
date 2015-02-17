package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

public enum FrequencyType 
{
    BATCH("AlertFrequency.Batch"),
    DOC("AlertFrequency.Doc"),
    INTERVAL("AlertFrequency.Interval");
    
    private String key;


    private FrequencyType(String key)
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
        for (DbActionType ms : DbActionType.values())
        {
            rlt.put(ms.name(), ms.getKey());
        }
        return rlt;
    }
}
