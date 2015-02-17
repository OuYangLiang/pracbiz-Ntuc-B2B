package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum SupplierSourceType 
{
    LOCAL("SupplierSourceType.local"),
    
    OVERSEA("SupplierSourceType.oversea");
    
    private String key;
    
    private SupplierSourceType(String key)
    {
        this.key = key;
    }

    private String getKey()
    {
        return key;
    }
    
    public static Map<String, String> toMapValue(BaseAction action)
    {
        Map<String,String> rlt = new HashMap<String,String>();
        for (SupplierSourceType ms : SupplierSourceType.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
