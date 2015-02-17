package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */

public enum ActorAction
{
    CREATE("ActorActionType.Create"),

    UPDATE("ActorActionType.Update"),
    
    DELETE("ActorActionType.Delete"),
    
    APPROVE("ActorActionType.Approve"),

    REJECT("ActorActionType.Reject"),
    
    WITHDRAW("ActorActionType.Withdraw");

    private String key;


    private ActorAction(String key)
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
        for (ActorAction ms : ActorAction.values())
        {
            rlt.put(ms.name(), ms.getKey());
        }
        return rlt;
    }
}
