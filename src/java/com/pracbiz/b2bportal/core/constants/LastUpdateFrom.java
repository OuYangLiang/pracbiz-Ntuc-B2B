//*****************************************************************************
//
// File Name       :  LastUpdateFrom.java
// Date Created    :  Sep 21, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 21, 2012 $
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
public enum LastUpdateFrom
{
    GROUP("LastUpdateFrom.Group"),

    USER("LastUpdateFrom.User");

    private String key;


    private LastUpdateFrom(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return key;
    }


    public static Map<String, String> toMapValue()
    {
        Map<String, String> rlt = new HashMap<String, String>();
        for (LastUpdateFrom rt : LastUpdateFrom.values())
        {
            rlt.put(rt.name(), rt.getKey());
        }
        return rlt;
    }
}
