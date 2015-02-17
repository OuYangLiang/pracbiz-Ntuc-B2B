package com.pracbiz.b2bportal.base.email;


import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

public class EmailEngine
{
    private static final String APP_URL = "appUrl";
    private static final String MAIL_TO = "Mail To :::: [";
    private static final String MAIL_SUBJECT = "Mail Subject :::: [";
    private static final String MAIL_SENDER = "[With Sender :::: [";
    private static final String MAIL_CONTENT = "[Mail Content :::: [";
    private static final String NEW_LINE_WITH_BRACKET = "],\n";
    
    private static final String START_LOG_INFO = "Start to send email notification";
    
    protected static final Logger log = LoggerFactory.getLogger(EmailEngine.class);
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private EmailConfigHelper cfg;
    @Autowired
    private CustomAppConfigHelper appConfig;
    

    public boolean sendEmailWithoutAttachment(String[] mailTo_,
        String templateNameSubject_, Map<String, Object> fillValuesSubject_,
        String templateNameContent_, Map<String, Object> fillValuesContent_)
    {
        try
        {
            fillValuesContent_.put(APP_URL, appConfig.getServerUrl());
            
            String subject_ = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, templateNameSubject_, fillValuesSubject_);

            String content_ = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, templateNameContent_, fillValuesContent_);

            return sendEmailWithoutAttachment(mailTo_, subject_, content_);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);

            return false;
        }

    }
        

    public boolean sendEmailWithoutAttachment(String[] mailTo_,
        String subject_, String templateNameContent_,
        Map<String, Object> fillValuesContent_)
    {
        try
        {
            fillValuesContent_.put(APP_URL, appConfig.getServerUrl());
            
            String content_ = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, templateNameContent_, fillValuesContent_);

            return sendEmailWithoutAttachment(mailTo_, subject_, content_);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);

            return false;
        }

    }
    

    public boolean sendEmailWithoutAttachment(String[] mailTo_,
        String subject_, String content_)
    {
        try
        {
            StringBuffer sbMail = new StringBuffer(200);
            sbMail.append(MAIL_TO + Arrays.asList(mailTo_)
                + NEW_LINE_WITH_BRACKET + MAIL_SUBJECT + subject_
                + NEW_LINE_WITH_BRACKET + MAIL_SENDER + cfg.getEmailConfig()
                + NEW_LINE_WITH_BRACKET + MAIL_CONTENT + content_ + "]");

            if (log.isInfoEnabled())
            {
                log.info(START_LOG_INFO);
                log.info(sbMail.toString());
            }
            
            
            initMailSender();

            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
            messageHelper.setTo(mailTo_);
            messageHelper.setSubject(subject_);
            messageHelper.setText(content_, false);

            // setup reply to email address
            if (cfg.getReplyToAddr() != null
                    && cfg.getReplyToAddr().trim().length() > 0)
            {
                messageHelper.setReplyTo(cfg.getReplyToAddr().trim());
            }

            // setup send from
            if (cfg.getSenderName() != null
                    && cfg.getSenderName().trim().length() > 0)
            {
                if (cfg.getSenderAddr() != null
                        && cfg.getSenderAddr().trim().length() > 0)
                {
                    messageHelper.setFrom(new InternetAddress(cfg
                            .getSenderAddr(), cfg.getSenderName()));
                }
                else
                {
                    messageHelper.setFrom(cfg.getSenderName());
                }
            }
            else
            {
                messageHelper.setFrom(cfg.getUserName());
            }

            mailSender.send(mailMessage);

            return true;
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);

            return false;
        }

    }
    

    public boolean sendHtmlEmail(final String[] mailTo_, final String subject_,
        final String templateNameContent_,
        final Map<String, Object> fillValuesContent_)
    {
        try
        {
            fillValuesContent_.put(APP_URL, appConfig.getServerUrl());
            
            final String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, templateNameContent_, fillValuesContent_);
            
            StringBuffer sbMail = new StringBuffer(200);
            sbMail.append(MAIL_TO + Arrays.asList(mailTo_)
                + NEW_LINE_WITH_BRACKET + MAIL_SUBJECT + subject_
                + NEW_LINE_WITH_BRACKET + MAIL_SENDER + cfg.getEmailConfig()
                + NEW_LINE_WITH_BRACKET + MAIL_CONTENT + text + "]");
            
            if (log.isInfoEnabled())
            {
                log.info(START_LOG_INFO);
                log.info(sbMail.toString());
            }
            
            
            initMailSender();
            
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception
                {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(
                            mimeMessage);

                    messageHelper.setTo(mailTo_);
                    messageHelper.setSubject(subject_);
                    messageHelper.setText(text, true);

                    // setup reply to email address
                    if (cfg.getReplyToAddr() != null
                            && cfg.getReplyToAddr().trim().length() > 0)
                    {
                        messageHelper.setReplyTo(cfg.getReplyToAddr().trim());
                    }

                    // setup send from
                    if (cfg.getSenderName() != null
                            && cfg.getSenderName().trim().length() > 0)
                    {
                        if (cfg.getSenderAddr() != null
                                && cfg.getSenderAddr().trim().length() > 0)
                        {
                            messageHelper.setFrom(new InternetAddress(cfg
                                    .getSenderAddr(), cfg.getSenderName()));
                        }
                        else
                        {
                            messageHelper.setFrom(cfg.getSenderName());
                        }
                    }
                    else
                    {
                        messageHelper.setFrom(cfg.getUserName());
                    }
                }
            };

            this.mailSender.send(preparator);

            return true;
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);

            return false;
        }
        
    }
    

    public boolean sendEmailWithAttachedDocuments(final String[] mailTo_,
        final String subject_, final String templateNameContent_,
        final Map<String, Object> fillValuesContent_, final File[] documents)
    {
        try
        {
            fillValuesContent_.put(APP_URL, appConfig.getServerUrl());
            
            final String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, templateNameContent_, fillValuesContent_);
            
            StringBuffer sbMail = new StringBuffer(200);
            sbMail.append(MAIL_TO + Arrays.asList(mailTo_)
                + NEW_LINE_WITH_BRACKET + MAIL_SUBJECT + subject_
                + NEW_LINE_WITH_BRACKET + MAIL_SENDER + cfg.getEmailConfig()
                + NEW_LINE_WITH_BRACKET + MAIL_CONTENT + text + "]");
            
            if (log.isInfoEnabled())
            {
                log.info(START_LOG_INFO);
                log.info(sbMail.toString());
            }
            
            
            initMailSender();
            
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception
                {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(
                            mimeMessage, true);

                    messageHelper.setTo(mailTo_);
                    messageHelper.setSubject(subject_);
                    messageHelper.setText(text, true);
                    for (File doc : documents)
                    {
                        messageHelper.addAttachment(doc.getName(), doc);
                    }

                    // setup reply to email address
                    if (cfg.getReplyToAddr() != null
                            && cfg.getReplyToAddr().trim().length() > 0)
                    {
                        messageHelper.setReplyTo(cfg.getReplyToAddr().trim());
                    }

                    // setup send from
                    if (cfg.getSenderName() != null
                            && cfg.getSenderName().trim().length() > 0)
                    {
                        if (cfg.getSenderAddr() != null
                                && cfg.getSenderAddr().trim().length() > 0)
                        {
                            messageHelper.setFrom(new InternetAddress(cfg
                                    .getSenderAddr(), cfg.getSenderName()));
                        }
                        else
                        {
                            messageHelper.setFrom(cfg.getSenderName());
                        }
                    }
                    else
                    {
                        messageHelper.setFrom(cfg.getUserName());
                    }
                }
            };

            this.mailSender.send(preparator);

            return true;
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);

            return false;
        }
        
    }
    

    public boolean sendEmailWithAttachedDocuments(final String[] mailTo_,
        final String subject_, final String templateNameContent_,
        final Map<String, Object> fillValuesContent_, final String[] filenames,
        final byte[][] fileContents)
    {
        try
        {
            fillValuesContent_.put(APP_URL, appConfig.getServerUrl());
            
            final String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, templateNameContent_, fillValuesContent_);
            
            StringBuffer sbMail = new StringBuffer(200);
            sbMail.append(MAIL_TO + Arrays.asList(mailTo_)
                + NEW_LINE_WITH_BRACKET + MAIL_SUBJECT + subject_
                + NEW_LINE_WITH_BRACKET + MAIL_SENDER + cfg.getEmailConfig()
                + NEW_LINE_WITH_BRACKET + MAIL_CONTENT + text + "]");
            
            if (log.isInfoEnabled())
            {
                log.info(START_LOG_INFO);
                log.info(sbMail.toString());
            }
            
            
            initMailSender();
            
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception
                {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(
                            mimeMessage, true);

                    messageHelper.setTo(mailTo_);
                    messageHelper.setSubject(subject_);
                    messageHelper.setText(text, true);

                    for (int i = 0; i < filenames.length; i++)
                    {
                        String filename = filenames[i];
                        byte[] content = fileContents[i];

                        messageHelper.addAttachment(filename,
                                new ByteArrayResource(content));
                    }

                    // setup reply to email address
                    if (cfg.getReplyToAddr() != null
                            && cfg.getReplyToAddr().trim().length() > 0)
                    {
                        messageHelper.setReplyTo(cfg.getReplyToAddr().trim());
                    }

                    // setup send from
                    if (cfg.getSenderName() != null
                            && cfg.getSenderName().trim().length() > 0)
                    {
                        if (cfg.getSenderAddr() != null
                                && cfg.getSenderAddr().trim().length() > 0)
                        {
                            messageHelper.setFrom(new InternetAddress(cfg
                                    .getSenderAddr(), cfg.getSenderName()));
                        }
                        else
                        {
                            messageHelper.setFrom(cfg.getSenderName());
                        }
                    }
                    else
                    {
                        messageHelper.setFrom(cfg.getUserName());
                    }
                }
            };
             
            this.mailSender.send(preparator);
     
            return true;
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);

            return false;
        }
        
    }
    
    
    private void initMailSender() throws Exception
    {
        mailSender.setHost(cfg.getSmtpHost());
        mailSender.setUsername(cfg.getUserName());
        mailSender.setPassword(cfg.getPassword());
        mailSender.setProtocol(cfg.getProtocol());
        mailSender.setPort(cfg.getPort());
        
        Properties props = cfg.getProperties();
        Enumeration<?> e = props.propertyNames();

        while (e.hasMoreElements()) {
          String key = (String) e.nextElement();
          
          mailSender.getJavaMailProperties().put(key, props.getProperty(key));
        }
    }
    
}
