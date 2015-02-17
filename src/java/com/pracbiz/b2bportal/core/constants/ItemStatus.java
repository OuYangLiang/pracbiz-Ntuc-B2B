package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public enum ItemStatus 
{
    NEW("ItemStatus.new"),
    
    SENT("ItemStatus.sent");
    
    private String key;


    private ItemStatus(String key)
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
        for (ItemStatus ms : ItemStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
