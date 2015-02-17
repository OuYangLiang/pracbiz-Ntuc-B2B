//*****************************************************************************
//
// File Name       :  WatonsPoFileValidator.java
// Date Created    :  Dec 19, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 19, 2013 10:15:00 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator.watsons;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.eai.file.validator.PoFileValidator;
import com.pracbiz.b2bportal.core.eai.file.watsons.PoDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class WatsonsPoFileValidator extends PoFileValidator implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(WatsonsPoFileValidator.class);
    @Autowired private PoDocFileHandler watsonsPoDocFileHandler;
    
    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input)
        throws Exception
    {
        return null;
    }

    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            watsonsPoDocFileHandler.readFileContent((PoDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
}
