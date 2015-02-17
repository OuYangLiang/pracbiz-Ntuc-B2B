package com.pracbiz.b2bportal.core.eai.file.canonical;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.item.FileParser;
import com.pracbiz.b2bportal.core.eai.message.inbound.ItemInDocMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class ItemInDocFileHandler extends
        DefaultDocFileHandler<ItemInDocMsg, BaseHolder> implements
        CoreCommonConstants
{
    @Autowired FileParser cktangFileParser;
    
    public String translate(ItemInDocMsg doc) throws Exception
    {
        cktangFileParser.translate(doc);
        if (doc.getTargetFilename() == null)
        {
            throw new Exception("can not translate item master file with file format [" + doc.getOutputFormat() + "]");
        }
        return null;
    }

    @Override
    public byte[] getFileByte(BaseHolder data, File targetFile,
            String expectedFormat) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTargetFilename(BaseHolder data, String expectedFormat)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String processFormat()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void readFileContent(ItemInDocMsg docMsg, byte[] input)
            throws Exception
    {
        // TODO Auto-generated method stub
        
    }

}
