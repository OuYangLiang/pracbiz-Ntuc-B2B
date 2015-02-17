package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */

public enum MkCtrlStatus 
{
    PENDING("MkCtrlStatus.Pending"),

    REJECTED("MkCtrlStatus.Rejected"),
    
    APPROVED("MkCtrlStatus.Approved"),
    
    WITHDRAWN("MkCtrlStatus.Withdrawn");


    private String key;


    private MkCtrlStatus(String key)
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
        for (MkCtrlStatus ms : MkCtrlStatus.values())
        {
            rlt.put(ms.name(), ms.getKey());
        }
        return rlt;
    }
}
