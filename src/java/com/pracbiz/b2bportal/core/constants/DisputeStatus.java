//*****************************************************************************
//
// File Name       :  DisputeStatus.java
// Date Created    :  Oct 30, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Oct 30, 2013 9:24:25 AM $
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

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public enum DisputeStatus
{
    
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");
    
    private String key;
    private DisputeStatus(String key)
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
        for (DisputeStatus ms : DisputeStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
