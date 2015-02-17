package com.pracbiz.b2bportal.core.holder;

import java.io.Serializable;

public class RestErrorHolder implements Serializable
{
    private static final long serialVersionUID = 8205883566583060590L;

    private String errorCode;

    private String errorDesc;

    /**
     * Getter of errorCode.
     * 
     * @return Returns the errorCode.
     */
    public String getErrorCode()
    {
        return errorCode;
    }

    /**
     * Setter of errorCode.
     * 
     * @param errorCode The errorCode to set.
     */
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     * Getter of errorDesc.
     * 
     * @return Returns the errorDesc.
     */
    public String getErrorDesc()
    {
        return errorDesc;
    }

    /**
     * Setter of errorDesc.
     * 
     * @param errorDesc The errorDesc to set.
     */
    public void setErrorDesc(String errorDesc)
    {
        this.errorDesc = errorDesc;
    }

}