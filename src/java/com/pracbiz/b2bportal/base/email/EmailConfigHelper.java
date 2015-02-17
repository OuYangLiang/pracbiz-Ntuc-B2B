package com.pracbiz.b2bportal.base.email;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;

public class EmailConfigHelper
{
    protected static final Logger log = LoggerFactory.getLogger(EmailConfigHelper.class);
//    private static final String EMAIL_CONFIG_HOLDER_KEY = "EMAIL_CONFIG_HOLDER";
    private static final String CTRL_PARAMETER_EMAIL_CONFIG = "EMAIL_CON"; 
//    private static final String SENDER_PREFIX = "mailSender.sender";
//    private static final String DEFAULT_SENDER_NO = "1";
    
    // email configuration
    private ControlParameterHolder smtpHost;
    private ControlParameterHolder smtpPort;
    private ControlParameterHolder smtpUser;
    private ControlParameterHolder smtpPassword ;
    private ControlParameterHolder smtpProtocol;
    private ControlParameterHolder needAuth;
    private ControlParameterHolder needEhlo;
    private ControlParameterHolder connectType;
    private ControlParameterHolder socketFacClass;
    private ControlParameterHolder socketFacFallback;
    private ControlParameterHolder senderName;
    private ControlParameterHolder senderAddr;
    private ControlParameterHolder replyToAddr;
    private ControlParameterHolder adminAddr;
    private ControlParameterHolder authMechanisms;
    
    protected Map<String, Object> configMap;
    @Autowired ControlParameterService controlParameterService;
    
    public EmailConfigHolder getEmailConfig()
    {
        return this.getEmailConfigHolder();
    }
//    public EmailConfigHolder getEmailConfig(String sendNo)
//    {
//        return getEmailConfigHolder(sendNo);
//    }
//
//
//    public String getSmtpHost(String sendNo)
//    {
//        return getEmailConfig(sendNo).getHost();
//    }
//
//
//    public String getUserName(String sendNo)
//    {
//        return getEmailConfig(sendNo).getUserName();
//    }
//
//
//    public String getPassword(String sendNo)
//    {
//        return getEmailConfig(sendNo).getPassword();
//    }
//
//
//    public String getProtocol(String sendNo)
//    {
//        return getEmailConfig(sendNo).getProtocol();
//    }
//
//
//    public int getPort(String sendNo)
//    {
//        return getEmailConfig(sendNo).getPort();
//    }
//
//
//    public Properties getProperties(String sendNo)
//    {
//        return getEmailConfig(sendNo).getProperties();
//    }


    public String getSmtpHost()
    {
        return getEmailConfigHolder().getHost();
    }


    public String getUserName()
    {
        return getEmailConfigHolder().getUserName();
    }


    public String getPassword()
    {
        return getEmailConfigHolder().getPassword();
    }


    public String getProtocol()
    {
        return getEmailConfigHolder().getProtocol();
    }


    public int getPort()
    {
        return getEmailConfigHolder().getPort();
    }


    public Properties getProperties()
    {
        return getEmailConfigHolder().getProperties();
    }


    public String getSenderName()
    {
        return getEmailConfigHolder().getSenderName();
    }


    public String getSenderAddr()
    {
        return getEmailConfigHolder().getSenderAddr();
    }


    public String getReplyToAddr()
    {
        return getEmailConfigHolder().getReplyToAddr();
    }
    
    public String getAdminAddr()
    {
        return getEmailConfigHolder().getAdminAddr();
    }


//    protected void initConfigMap()
//    {
//        try
//        {
//            this.configMap.put(EMAIL_CONFIG_HOLDER_KEY, getEmailConfigHolder());
//        }
//        catch(Exception e)
//        {
//            ErrorHelper.getInstance().logError(log, e);
//        }
//        
//    }


    // ----------- private method --------------//
    private EmailConfigHolder getEmailConfigHolder()
    {
        initEmailConfig();
        EmailConfigHolder config = new EmailConfigHolder();

//        String siteNode = SENDER_PREFIX + DEFAULT_SENDER_NO + ".";

        config.setHost(smtpHost.getStringValue());
        config.setUserName(smtpUser.getStringValue());
        config.setPassword(smtpPassword.getStringValue());
        config.setProtocol(smtpProtocol.getStringValue());

        String port = smtpPort.getStringValue();
        try
        {
            config.setPort(Integer.parseInt(port.trim()));
        }
        catch (Exception e)
        {
            config.setPort(EmailConfigHolder.DEFAULT_SMTP_PORT);
        }
        config.setSocketFactoryClass(socketFacClass.getStringValue());
        config.setSocketFactoryFullback(socketFacFallback.getValid().toString());
        config.setNeedAuth(needAuth.getValid().toString());
        config.setNeedEhlo(needEhlo.getValid().toString());
        config.setConnectType(connectType.getStringValue().trim());
        config.setSenderName(senderName.getStringValue().trim());
        config.setSenderAddr(senderAddr.getStringValue().trim());
        config.setReplyToAddr(replyToAddr.getStringValue().trim());
        config.setAdminAddr(adminAddr.getStringValue().trim());
        config.setAuthMechanisms(authMechanisms.getStringValue().trim());
        
        return config;
    }


//    private EmailConfigHolder getEmailConfigHolder(String senderNo)
//    {
//        EmailConfigHolder config = new EmailConfigHolder();
//
//        String siteNode = SENDER_PREFIX + senderNo + ".";
//
//        config.setHost(this.stringValue(siteNode + "smtpHost"));
//        config.setUserName(this.stringValue(siteNode + "smtpUser"));
//        config.setPassword(this.stringValue(siteNode + "smtpPassword"));
//        config.setProtocol(this.stringValue(siteNode + "smtpProtocol"));
//
//        String port = this.stringValue(siteNode + "smtpPort");
//        try
//        {
//            config.setPort((new Integer(port)).intValue());
//        }
//        catch (Exception e)
//        {
//            config.setPort(EmailConfigHolder.DEFAULT_SMTP_PORT);
//        }
//        config.setSocketFactoryClass(this.stringValue(siteNode
//                + "socketFactoryClass"));
//        config.setSocketFactoryFullback(this.stringValue(siteNode
//                + "socketFactoryFallback"));
//        config.setNeedAuth(this.stringValue(siteNode + "needAuth"));
//        config.setNeedEhlo(this.stringValue(siteNode + "needEhlo"));
//        config.setViaSSL(this.stringValue(siteNode + "viaSSL"));
//
//        config.setSenderName(this.stringValue(siteNode + "senderName"));
//        config.setSenderAddr(this.stringValue(siteNode + "senderAddr"));
//        config.setReplyToAddr(this.stringValue(siteNode + "replyToAddr"));
//
//        return config;
//    }
    
    private void initEmailConfig()
    {
        try
        {
            smtpHost = controlParameterService
                     .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_HOST");
            smtpPort = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_PORT");
            smtpUser = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_USER");
            smtpPassword = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_PASSWORD");
            smtpProtocol = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_PROTOCOL");
            needAuth = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "NEED_AUTH");
            needEhlo = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "NEED_EHLO");
            connectType = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "CONNECT_TYPE");
            socketFacClass = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SOCKET_FACTORY_CLASS");
            socketFacFallback = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SOCKET_FACTORY_FALLBACK");
            senderName = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SENDER_NAME");
            senderAddr = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SENDER_ADDR");
            replyToAddr = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "REPLY_TO");
            adminAddr = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "ADMIN_ADDR");
            authMechanisms = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "AUTH_MECHANISMS");
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
}
