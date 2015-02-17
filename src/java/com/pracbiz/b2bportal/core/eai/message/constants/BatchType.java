//*****************************************************************************
//
// File Name       :  BatchType.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 10:28:18 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public enum BatchType
{
    PO("BatchType.PO"),

    GRN("BatchType.GRN"),

    DN("BatchType.DN"),

    PN("BatchType.PN"),

    RTV("BatchType.RTV"),
    
    POCN("BatchType.POCN"),
    
    GI("BatchType.GI"),
    
    CC("BatchType.CC"),
    
    DSD("BatchType.DSD"),
    
    INV("BatchType.INV"),
    
    CN("BatchType.CN");

    private String key;

    private BatchType(String key)
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
        for(BatchType bt : BatchType.values())
        {
            rlt.put(bt.name(), bt.getKey());
        }
        return rlt;
    }
}
