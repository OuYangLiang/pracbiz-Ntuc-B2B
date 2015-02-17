//*****************************************************************************
//
// File Name       :  DeploymentMode.java
// Date Created    :  Nov 27, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 27, 2012 10:32:02 AM$
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
 * @author ouyang
 */
public enum DeploymentMode
{
    LOCAL("DeploymentMode.Local"),
    REMOTE("DeploymentMode.Remote");
    
    private String key;
    
    private DeploymentMode(String key)
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
        for (DeploymentMode ms : DeploymentMode.values())
        {
            rlt.put(ms.name(), ms.getKey());
        }
        return rlt;
    }
}
