//*****************************************************************************
//
// File Name       :  PoFileHandler.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 4:45:12 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.canonical;

import java.io.File;

import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author yinchi
 */
public class PocnDocFileHandler extends DefaultDocFileHandler<PoDocMsg, PoHolder> implements
    CoreCommonConstants
{
    
    @Override
    protected String processFormat()
    {
        return CANONICAL;
    }

    @Override
    public byte[] getFileByte(PoHolder data, File targetFile, String expectedFormat)
        throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTargetFilename(PoHolder data, String expectedFormat)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void readFileContent(PoDocMsg docMsg, byte[] input)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected String translate(PoDocMsg docMsg) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    
}
