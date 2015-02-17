package com.pracbiz.b2bportal.core.constants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public enum ClientActionType 
{
    INBOX("INBOX"), 
    
    OUTBOX("OUTBOX");
    
    
    private String key;


    private ClientActionType(String key)
    {
        this.key = key;
    }
    

    public String getKey()
    {
        return key;
    }
}
