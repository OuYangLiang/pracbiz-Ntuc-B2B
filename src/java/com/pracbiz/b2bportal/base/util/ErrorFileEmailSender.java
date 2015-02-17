package com.pracbiz.b2bportal.base.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;

public class ErrorFileEmailSender
{
    public static final String ERROR_FILE_EMAIL_TEMPLATE = "FILE_ERROR.vm";
    @Autowired
    private EmailEngine emailEngine;


    public void sendEmail(BuyerMsgSettingHolder setting,
            List<String> errorFiles, Map<File, List<String>> errContent)
    {
        if (null == setting || null == setting.getRcpsAddrs()
                || setting.getRcpsAddrs().trim().isEmpty())
        {
            return;
        }

        String[] mailTo = getEmailAddr(setting.getRcpsAddrs());

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("ERROR_FILES", errorFiles);

        if (null != errContent)
        {
            Map<String, Object> errFileContent = new HashMap<String, Object>();

            for (Map.Entry<File, List<String>> entry : errContent.entrySet())
            {
                errFileContent.put(entry.getKey().getName(), entry.getValue());
            }

            param.put("ERROR_FILE_MAP", errFileContent);
        }

        emailEngine.sendHtmlEmail(mailTo,
                "File Upload Notification - Upload Error", "supplier_master_error.vm",
                param);
    }


    private String[] getEmailAddr(String addrs)
    {
        if (null == addrs || "".equals(addrs.trim()))
        {
            return null;
        }

        String[] addrses = (addrs.trim()).split(",");

        for (String add : addrses)
        {
            add = add.trim();
        }

        return addrses;
    }
}
