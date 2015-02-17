package com.pracbiz.b2bportal.base.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

public class StandardEmailSender implements CommonConstants
{
    public static final String STANDARD_EMAIL_TEMPLATE = "ALERT_EXCEPTION.vm";
    @Autowired
    private CustomAppConfigHelper appConfig;
    @Autowired
    private EmailEngine emailEngine;


    public void sendStandardEmail(String module, String tickNumber, Exception e)
    {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("TIME", DateUtil.getInstance().convertDateToString(
                new Date(), "yyyy-MM-dd HH:mm:ss"));
        param.put("ID", module);
        param.put("TICK_NO", tickNumber);
        param.put("EXCEPTION", ErrorHelper.getInstance().getStackTrace(e));

        String adminEmailAddr = appConfig.getAdminEmail();

        if (null == adminEmailAddr || adminEmailAddr.trim().isEmpty())
        {
            return;
        }

        emailEngine.sendEmailWithoutAttachment(
                parseEmailAddress(adminEmailAddr), "ERROR NOTIFICATION",
                STANDARD_EMAIL_TEMPLATE, param);
    }


    private String[] parseEmailAddress(String address)
    {
        String[] receiver = address.split(",");

        for (String each : receiver)
        {
            each = each.trim();
        }

        return receiver;
    }

}
