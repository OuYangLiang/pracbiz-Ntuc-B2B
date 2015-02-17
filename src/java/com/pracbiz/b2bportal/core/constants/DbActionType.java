package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */

public enum DbActionType
{
    CREATE("DbActionType.Create"),

    UPDATE("DbActionType.Update"),
    
    DELETE("DbActionType.Delete");

    private String key;


    private DbActionType(String key)
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
