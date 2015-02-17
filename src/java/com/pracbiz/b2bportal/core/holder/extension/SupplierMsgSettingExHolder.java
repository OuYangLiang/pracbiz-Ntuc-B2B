package com.pracbiz.b2bportal.core.holder.extension;

import java.util.List;

import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;

public class SupplierMsgSettingExHolder extends SupplierMsgSettingHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String msgDesc;
    private List<String> fileFormatList;
    
    public String getMsgDesc()
    {
        return msgDesc;
    }
    public void setMsgDesc(String msgDesc)
    {
        this.msgDesc = msgDesc;
    }
    public List<String> getFileFormatList()
    {
        return fileFormatList;
    }
    public void setFileFormatList(List<String> fileFormatList)
    {
        this.fileFormatList = fileFormatList;
    }
    
}
