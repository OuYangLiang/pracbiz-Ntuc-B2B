//*****************************************************************************
//
// File Name       :  PropertiesUtil.java
// Date Created    :  Nov 25, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 25, 2013 3:17:28 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.ErrorHelper;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public final class PropertiesUtil
{

    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
    
    private PropertiesUtil()
    {
        
    }
    
    public static Properties getProperties(String strFilePath)
    {
        Properties prop = null;
        InputStream is;
        prop = new Properties();
        is = PropertiesUtil.class.getClassLoader().getResourceAsStream(
            strFilePath);
        if(is == null)
        {
            return null;
        }
        try
        {
            prop.load(is);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
               ErrorHelper.getInstance().logError(log, e);
            }
        }

        return prop;
    }

}
