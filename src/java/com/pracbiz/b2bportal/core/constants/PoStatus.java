package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum PoStatus
{
    NEW("PoStatus.new"),

    INVOICED("PoStatus.invoiced"),
    
    PARTIAL_INVOICED("PoStatus.partialInvoiced"),
    
    CANCELLED("PoStatus.cancelled"),
    
    CANCELLED_INVOICED("PoStatus.cancelledInvoiced"),
    
    AMENDED("PoStatus.amended"),
    
    OUTDATED("PoStatus.outdated"),
    
    CREDITED("PoStatus.credited");

    private String key;


    private PoStatus(String key)
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
        for (PoStatus ms : PoStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
