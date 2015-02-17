package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum InvType
{
    SOR("InvType.sor"),

    CON("InvType.con"),
    
    SVC("InvType.svc");
    
    private String key;


    private InvType(String key)
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
        for (InvType rt : InvType.values())
        {
            rlt.put(rt.name(), action.getText(rt.getKey()));
        }
        return rlt;
    }
    
    
    public static InvType getInvTypeByPoType(PoType poType)
    {
        Map<PoType,InvType> rlt = init();
        
        return rlt.get(poType);
    }
    
    private static Map<PoType,InvType> init()
    {
        Map<PoType,InvType> rlt = new HashMap<PoType, InvType>();
        rlt.put(PoType.CON, InvType.CON);
        rlt.put(PoType.SOR, InvType.SOR);
        rlt.put(PoType.SVC, InvType.SVC);
        
        return rlt;
    }
}
