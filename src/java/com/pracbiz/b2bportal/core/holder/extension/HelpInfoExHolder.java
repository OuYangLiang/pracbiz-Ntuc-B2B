package com.pracbiz.b2bportal.core.holder.extension;

import java.io.Serializable;


public class HelpInfoExHolder implements Serializable
{
    private static final long serialVersionUID = 6917626570884470118L;
    private String helpNo;
    private String helpEmail;


    public String getHelpNo()
    {
        return helpNo;
    }


    public void setHelpNo(String helpNo)
    {
        this.helpNo = helpNo;
    }


    public String getHelpEmail()
    {
        return helpEmail;
    }


    public void setHelpEmail(String helpEmail)
    {
        this.helpEmail = helpEmail;
    }

}
