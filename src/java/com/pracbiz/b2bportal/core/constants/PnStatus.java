//*****************************************************************************
//
// File Name       :  PnStatus.java
// Date Created    :  2013-7-30
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2013-7-30 上午09:15:22 $
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
public enum PnStatus
{
    NEW("PnStatus.new"),
    OUTDATED("PnStatus.outdated"),
    AMENDED("PnStatus.amended");

    private String key;


    private PnStatus(String key)
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
        for (PnStatus ms : PnStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
