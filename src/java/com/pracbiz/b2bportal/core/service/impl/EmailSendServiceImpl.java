//*****************************************************************************
//
// File Name       :  EmailSendServiceImpl.java
// Date Created    :  2012-2-24
// Last Changed By :  $Author:GeJianKui $
// Last Changed On :  $Date:{date} $
// Revision        :  $Rev: $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.core.eai.file.SupplierMasterFileParserUtil;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.EmailSendService;
import com.pracbiz.b2bportal.core.service.ResetPasswordRequestHistoryService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class EmailSendServiceImpl implements EmailSendService
{
    private static final String SET_PWD_EMAIL_TEMPLATE = "mail_setPassword_link.vm";
    private static final String SET_PWD_EMAIL_SUBJECT = "Suppliers Portal: Creation of New User";
    private Boolean noAudit;
    
    @Autowired
    private EmailEngine emailEngine;
    @Autowired
    private ResetPasswordRequestHistoryService resetPasswordRequestHistoryService;
    @Autowired
    private ControlParameterService controlParameterService;

    public static class SendEmailCallableClass implements
            Callable<Map<String, Boolean>>
    {
        private final String[] emails;
        private final String subj;
        private final Map<String, Object> param;
        private final EmailEngine emailEngine;
        private final String emailTemplate;


        public SendEmailCallableClass(final String[] emails, final String subj,
                final String emailTemplate, final Map<String, Object> param,
                final EmailEngine emailEngine)
        {
            this.emails = emails.clone();
            this.subj = subj;
            this.param = param;
            this.emailTemplate = emailTemplate;
            this.emailEngine = emailEngine;
        }


        @Override
        public Map<String, Boolean> call() throws Exception
        {
            Map<String, Boolean> rltMap = new HashMap<String, Boolean>();
            boolean rlt = emailEngine.sendHtmlEmail(emails, subj,
                    emailTemplate, param);
            rltMap.put("RESULT", rlt);
            return rltMap;
        }
    }


    @Override
    public boolean sendSetPasswordEmailByCallable(CommonParameterHolder cp,
            String requestUrl, String clientIp, List<UserProfileTmpHolder> users)
            throws Exception
    {
        ExecutorService es = Executors.newFixedThreadPool(1);
        List<Future<Map<String, Boolean>>> threadRlt = new ArrayList<Future<Map<String, Boolean>>>();
        
        for (UserProfileTmpHolder user : users)
        {
            //if this user doesn't have a email address, in other word, it is not specified, ignore it.
            if (user.getEmail().equalsIgnoreCase(SupplierMasterFileParserUtil.NOT_SPECIFIED))
            {
                continue;
            }
            String hashValue = EncodeUtil.getInstance().computeSha2Digest(
                    EncodeUtil.getInstance().generateSecureBytes(16));
            ResetPasswordRequestHistoryHolder newResetPwdRecord = new ResetPasswordRequestHistoryHolder();
            newResetPwdRecord.setHashCode(hashValue);
            newResetPwdRecord.setLoginId(user.getLoginId());
            newResetPwdRecord.setClientIp(clientIp);
            newResetPwdRecord.setRequestTime(new Date());
            newResetPwdRecord.setValid(Boolean.TRUE);
    
            if (noAudit == null || !noAudit)
            {
                resetPasswordRequestHistoryService.resetPassword(null,
                        newResetPwdRecord, cp);
            }
            else
            {
                resetPasswordRequestHistoryService.resetPasswordWithoutAudit(
                        null, newResetPwdRecord);
            }
            String hyperlink = requestUrl.substring(0,
                    requestUrl.lastIndexOf('/') + 1)
                    + "setPassword.action?h="
                    + hashValue;
            Date expireDate = DateUtil.getInstance().dateAfterDays(
                    newResetPwdRecord.getRequestTime(),
                    controlParameterService
                            .selectCacheControlParameterBySectIdAndParamId(
                                    CoreCommonConstants.SECT_ID_CTRL,
                                    CoreCommonConstants.PARAM_ID_MAIL_EXPIRE_DAYS)
                            .getNumValue());
    
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("loginId", user.getLoginId());
            map.put("name", user.getUserName());
            map.put("hyperlink", hyperlink);
            map.put("expireDate",
                    DateUtil.getInstance().convertDateToString(expireDate,
                            DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
            map.put("helpdeskNumber",
                    controlParameterService
                            .selectCacheControlParameterBySectIdAndParamId(
                                    CoreCommonConstants.SECT_ID_CTRL,
                                    CoreCommonConstants.PARAM_ID_HELPDESK_NO)
                            .getStringValue());
            map.put("helpdeskEmail",
                    controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CoreCommonConstants.SECT_ID_CTRL,
                            CoreCommonConstants.PARAM_ID_HELPDESK_EMAIL)
                            .getStringValue());
    
            SendEmailCallableClass secc = new SendEmailCallableClass(
                    new String[] { user.getEmail() }, SET_PWD_EMAIL_SUBJECT,
                    SET_PWD_EMAIL_TEMPLATE, map, emailEngine);
    
            Future<Map<String, Boolean>> fut = es.submit(secc);
            threadRlt.add(fut);
        }
        es.shutdown();

//        for (Future<Map<String, Boolean>> fut : threadRlt)
//        {
//            Map<String, Boolean> m = fut.get();
//        }

        return true;
    }


    public void setNoAudit(Boolean noAudit)
    {
        this.noAudit = noAudit;
    }
    
}
