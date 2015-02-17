//*****************************************************************************
//
// File Name       :  ConnectionADparamHolder.java
// Date Created    :  Mar 15, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Mar 15, 2013 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder.extension;

import java.io.Serializable;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class ActiveDirectoryHolder implements Serializable
{
    private static final long serialVersionUID = 6971322384014654850L;
    private String domain;
    private String hostname;
    private String port;
    private String userName;
    private String password;

    /**
     * Getter of domain.
     * 
     * @return Returns the domain.
     */
    public String getDomain()
    {
        return domain;
    }

    /**
     * Setter of domain.
     * 
     * @param domain The domain to set.
     */
    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    /**
     * Getter of hostname.
     * 
     * @return Returns the hostname.
     */
    public String getHostname()
    {
        return hostname;
    }

    /**
     * Setter of hostname.
     * 
     * @param hostname The hostname to set.
     */
    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    /**
     * Getter of port.
     * 
     * @return Returns the port.
     */
    public String getPort()
    {
        return port;
    }

    /**
     * Setter of port.
     * 
     * @param port The port to set.
     */
    public void setPort(String port)
    {
        this.port = port;
    }

    /**
     * Getter of userName.
     * 
     * @return Returns the userName.
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Setter of userName.
     * 
     * @param userName The userName to set.
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * Getter of password.
     * 
     * @return Returns the password.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Setter of password.
     * 
     * @param password The password to set.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

}
