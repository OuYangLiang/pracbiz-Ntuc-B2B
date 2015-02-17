//*****************************************************************************
//
// File Name       :  AccessActionType.java
// Date Created    :  Sep 26, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 26, 2012 $
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

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public enum AccessActionType
{
    IN("AccessActionType.In"),

    OUT("AccessActionType.Out");

    private String key;


    private AccessActionType(String key)
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
        for (AccessActionType ms : AccessActionType.values())
        {
            rlt.put(ms.name(), ms.getKey());
        }
        return rlt;
    }
}
