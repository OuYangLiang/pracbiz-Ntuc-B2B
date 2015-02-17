package com.pracbiz.b2bportal.core.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.service.ForgetPasswordService;
import com.pracbiz.b2bportal.core.service.ResetPasswordRequestHistoryService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class ForgetPasswordServiceImpl implements ForgetPasswordService
{

    @Autowired
    private ResetPasswordRequestHistoryService resetPasswordRequestHistoryService;

    public static final String TEMPLATE_MAIL_FORGET_PASSWORD_LINK = "mail_forgetPassword_link.vm";
    public static final String TEMPLATE_MAIL_RESET_PASSWORD_RESULT = "mail_resetPassword_success.vm";


    @Override
    public void resetPassword(
            ResetPasswordRequestHistoryHolder previousResetPwdRecord,
            ResetPasswordRequestHistoryHolder newResetPwdRecord,
            CommonParameterHolder cp, UserProfileHolder userProfile,
            EmailEngine emailEngine, Map<String, Object> map) throws Exception
    {
        resetPasswordRequestHistoryService.resetPassword(
                previousResetPwdRecord, newResetPwdRecord, cp);
        
        emailEngine.sendHtmlEmail(new String[] { userProfile
                .getEmail() }, "Confirm Reset password",
                TEMPLATE_MAIL_FORGET_PASSWORD_LINK, map);
    }


    @Override
    public void saveNewPassword(PasswordHistoryHolder passwordHistory,
            UserProfileTmpHolder newUserProfile, UserProfileTmpHolder userProfile,
            CommonParameterHolder cp, EmailEngine emailEngine,
            Map<String, Object> map) throws Exception
    {
        resetPasswordRequestHistoryService.saveNewPassword(passwordHistory,
                newUserProfile, userProfile, cp);
        
        emailEngine.sendEmailWithoutAttachment(new String[] { userProfile
                .getEmail() }, "Reset password email success",
                TEMPLATE_MAIL_RESET_PASSWORD_RESULT, map);
    }

}
