package com.pracbiz.b2bportal.base.email;

import java.util.Locale;
import java.util.Properties;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class EmailConfigHolder extends BaseHolder
{
    private static final long serialVersionUID = -8606673470337002182L;
    public static final int DEFAULT_SMTP_PORT = 25;
    private static final String MAIL_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
    private static final String MAIL_SMTP_SOCKETFACTORY_CLASS_FALLBACK = "mail.smtp.socketFactory.fallback";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_EHLO = "mail.smtp.ehlo";
    private static final String MAIL_SMTP_AUTH_MECHANISMS = "mail.smtp.auth.mechanisms";
    private static final String MAIL_SMTP_STARTTLES_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_STARTTLES_REQUIRED = "mail.smtp.starttls.required";
    private static final String PROPERTITY_VALUE_TRUE = "true";
    
            
    private String host;
    
    private String userName;
    
    private String password;
    
    private String protocol;
    
    private int port;
    
    private String needAuth;
    
    private String needEhlo;
    
    private String connectType;
    
    private String socketFactoryClass;
    
    private String socketFactoryFullback;
    
    private String senderName;
    
    private String senderAddr;
    
    private String replyToAddr;    
    
    private String adminAddr;

    private String authMechanisms;
    
    
    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getNeedAuth()
    {
        return needAuth;
    }

    public void setNeedAuth(String needAuth)
    {
        this.needAuth = needAuth;
    }

    public String getConnectType()
    {
        return connectType;
    }

    public void setConnectType(String connectType)
    {
        this.connectType = connectType;
    }

    public String getSocketFactoryClass()
    {
        return socketFactoryClass;
    }

    public void setSocketFactoryClass(String socketFactoryClass)
    {
        this.socketFactoryClass = socketFactoryClass;
    }

    public String getSocketFactoryFullback()
    {
        return socketFactoryFullback;
    }

    public void setSocketFactoryFullback(String socketFactoryFullback)
    {
        this.socketFactoryFullback = socketFactoryFullback;
    }
    
    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public String getSenderAddr()
    {
        return senderAddr;
    }

    public void setSenderAddr(String senderAddr)
    {
        this.senderAddr = senderAddr;
    }

    public String getReplyToAddr()
    {
        return replyToAddr;
    }

    public void setReplyToAddr(String replyToAddr)
    {
        this.replyToAddr = replyToAddr;
    }

    public String getNeedEhlo()
    {
        return needEhlo;
    }

    public void setNeedEhlo(String needEhlo)
    {
        this.needEhlo = needEhlo;
    }
    
    public String getAdminAddr()
    {
        return adminAddr;
    }

    public void setAdminAddr(String adminAddr)
    {
        this.adminAddr = adminAddr;
    }

    
    public String getAuthMechanisms()
    {
        return authMechanisms;
    }

    public void setAuthMechanisms(String authMechanisms)
    {
        this.authMechanisms = authMechanisms;
    }
    
    public Properties getProperties()
    {
        Properties pro = new Properties();
        pro.put(MAIL_SMTP_AUTH, this.getNeedAuth().trim().toLowerCase(Locale.US));
        pro.put(MAIL_SMTP_EHLO, this.getNeedEhlo().trim().toLowerCase(Locale.US));
        
        if (this.getNeedAuth().trim().toLowerCase(Locale.US).equalsIgnoreCase("true"))
        {
            if (getConnectType().trim().equalsIgnoreCase("TLS"))
            {
                pro.put(MAIL_SMTP_AUTH_MECHANISMS, this.getAuthMechanisms());
                pro.put(MAIL_SMTP_STARTTLES_ENABLE, PROPERTITY_VALUE_TRUE);
                pro.put(MAIL_SMTP_STARTTLES_REQUIRED, PROPERTITY_VALUE_TRUE);
            }
            else if ((getConnectType().trim().equalsIgnoreCase("SSL")))
            {
                pro.put(MAIL_SMTP_SOCKETFACTORY_CLASS,getSocketFactoryClass());
                pro.put(MAIL_SMTP_SOCKETFACTORY_CLASS_FALLBACK, getSocketFactoryFullback().trim().toLowerCase(Locale.US));
            }
        }
       
//        pro.put("mail.debug", "true");
        return pro;
    }
    @Override
    public String getCustomIdentification()
    {
        // TODO Auto-generated method stub
        return null;
    }

    
}
