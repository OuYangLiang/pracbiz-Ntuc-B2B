//*****************************************************************************
//
// File Name       :  DnStatus.java
// Date Created    :  2013-7-29
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2013-7-29 下午04:36:24 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum CnStatus
{
    NEW("CnStatus.new"),
    SUBMIT("CnStatus.submit"),
    VOID("CnStatus.void"),
    VOID_OUTDATED("CnStatus.voidOutdated");
    
    
    private String key;


    private CnStatus(String key)
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
        for (CnStatus ms : CnStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
