package com.pracbiz.b2bportal.core.eai.file.validator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.core.eai.message.DocMsg;

public abstract class SalesFileValidator extends FileValidator
{
    private static final Logger log = LoggerFactory.getLogger(SalesFileValidator.class);
    
    @Override
    protected List<String> validateLogic(DocMsg docMsg)
            throws Exception
    {
        return null;
    }
}
