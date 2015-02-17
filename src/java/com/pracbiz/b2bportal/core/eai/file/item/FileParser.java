package com.pracbiz.b2bportal.core.eai.file.item;

import java.io.File;
import java.util.List;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;

public abstract class FileParser
{
    protected FileParser successor;


    public abstract List<String> validateItemInFile(String fileFormat,
            File file, String fileName, BaseAction action) throws Exception;
    
    public abstract void translate(DocMsg doc) throws Exception;
    
    
    public abstract String itemInFileContent(String fileFormat);


    public void setSuccessor(FileParser successor)
    {
        this.successor = successor;
    }
    
    
}
