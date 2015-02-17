//*****************************************************************************
//
// File Name       :  Mechanisms.java
// Date Created    :  Oct 8, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Oct 8, 2013 3:16:25 PM $
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
public enum EmailAuthMechanisms 
{
    LOGIN("LOGIN"),
    PLAIN("PLAIN"),
    DIGEST_MD5("DIGEST-MD5"),
    NTLM("NTLM");
    
    private String key;
    private EmailAuthMechanisms(String key)
    {
        this.key = key;
    }
    
    public String getKey()
    {
        return key;
    }
    public void setKey(String key)
    {
        this.key = key;
    }
    
    
    public static Map<String, String> toMapValue()
    {
        Map<String, String> rlt = new HashMap<String, String>();
        for(EmailAuthMechanisms mechanisms : EmailAuthMechanisms.values())
        {
            rlt.put(mechanisms.name(), mechanisms.getKey());
        }
        return rlt;
    }
    
}
