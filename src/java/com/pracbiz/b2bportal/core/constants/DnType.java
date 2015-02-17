//*****************************************************************************
//
// File Name       :  DnType.java
// Date Created    :  2012-12-25
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-25 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public enum DnType
{
    STK_QOC ("DnType.stkQoc"),
    STK_RTV ("DnType.stkRtv"),
    CST_CR  ("DnType.cstCr"),
    CST_IOC ("DnType.cstIoc"),
    CON ("DnType.con"),
    SVC ("DnType.svc");
    
    private String key;
    
    private DnType(String key)
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
        for (DnType ms : DnType.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
    
}
