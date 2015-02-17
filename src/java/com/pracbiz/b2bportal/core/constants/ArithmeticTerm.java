//*****************************************************************************
//
// File Name       :  LoginFailedAttemptString.java
// Date Created    :  Oct 10, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Oct 10, 2012 $
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
public enum ArithmeticTerm
{
    E("ArithmeticTerm.equal"),

    GE("ArithmeticTerm.greaterThanOrEqual"),
    
    G("ArithmeticTerm.greaterThan"),
    
    LE("ArithmeticTerm.lessThanOrEqual"),
    
    L("ArithmeticTerm.lessThan");

    private String key;


    private ArithmeticTerm(String key)
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
        for (ArithmeticTerm ms : ArithmeticTerm.values())
        {
            rlt.put(ms.name(), ms.getKey());
        }
        return rlt;
    }
}
