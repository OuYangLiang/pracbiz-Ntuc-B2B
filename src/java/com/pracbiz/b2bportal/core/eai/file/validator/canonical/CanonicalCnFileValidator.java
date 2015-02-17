package com.pracbiz.b2bportal.core.eai.file.validator.canonical;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.eai.file.canonical.CnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.CnFileValidator;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.CnDocMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class CanonicalCnFileValidator extends CnFileValidator implements
        CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(CanonicalCnFileValidator.class);
    @Autowired CnDocFileHandler canonicalCnDocFileHandler;
    
    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input)
            throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            canonicalCnDocFileHandler.readFileContent((CnDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }

}
