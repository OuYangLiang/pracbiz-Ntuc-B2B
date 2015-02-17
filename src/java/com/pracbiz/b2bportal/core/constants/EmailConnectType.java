//*****************************************************************************
//
// File Name       :  ConnectType.java
// Date Created    :  Oct 8, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Oct 8, 2013 4:00:02 PM $
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

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public enum EmailConnectType
{
    SSL,
    TLS,
    NONE;
    
    public static Map<String, String> toMapValue()
    {
        Map<String, String> rlt = new HashMap<String, String>();
        for(EmailConnectType connectType : EmailConnectType.values())
        {
            rlt.put(connectType.name(), connectType.name());
        }
        return rlt;
    }
}
