//*****************************************************************************
//
// File Name       :  GnupgUtilWrapper.java
// Date Created    :  Mar 18, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Mar 18, 2013 6:16:22 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;


/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public final class GnupgUtilWrapper
{
    private static GnupgUtilWrapper instance;
    
    public static final String SUPPLIER_USER_ID_PREFIX = "S_";
    public static final String SUPPLIER_USER_ID_SUFFIX = "_S@pracbiz.com";
    
    public static final String CHANNEL_USER_ID_PREFIX = "N_";
    public static final String CHANNEL_USER_ID_SUFFIX = "_N@pracbiz.com";
    
    public static final String CLIENT_USER_ID_PREFIX = "C_";
    public static final String CLIENT_USER_ID_SUFFIX = "_C@pracbiz.com";
    
    private GnupgUtilWrapper(){}
    
    public static GnupgUtilWrapper getInstance()
    {
        synchronized(GnupgUtilWrapper.class)
        {
            if (null == instance)
            {
                instance = new GnupgUtilWrapper();
            }
            
            return instance;
        }
    }
    
    //*****************************************************
    // public methods
    //*****************************************************
    
    public String wrapSupplierUserId(String portalId, String mboxId)
    {
        return SUPPLIER_USER_ID_PREFIX + portalId + "_" + mboxId + SUPPLIER_USER_ID_SUFFIX;
    }
    
    
    public String wrapChannelMbox(String mboxId)
    {
        return CHANNEL_USER_ID_PREFIX + mboxId + CHANNEL_USER_ID_SUFFIX;
    }
    
    
    public String wrapClientId(String mboxId)
    {
        return CLIENT_USER_ID_PREFIX + mboxId + CLIENT_USER_ID_SUFFIX;
    }

}
