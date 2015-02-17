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
public enum AuditAccessErrorCode
{
    F0001("AuditAccessErrorCode.F0001"),
    F0002("AuditAccessErrorCode.F0002"),
    F0003("AuditAccessErrorCode.F0003"),
    F0004("AuditAccessErrorCode.F0004"),
    F0005("AuditAccessErrorCode.F0005");

    private String key;


    private AuditAccessErrorCode(String key)
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
        for (AuditAccessErrorCode ms : AuditAccessErrorCode.values())
        {
            rlt.put(ms.name(), ms.getKey());
        }
        return rlt;
    }
}
