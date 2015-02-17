package com.pracbiz.b2bportal.core.service;

import java.util.Map;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface ForgetPasswordService
{
    public void resetPassword(
            ResetPasswordRequestHistoryHolder previousResetPwdRecord,
            ResetPasswordRequestHistoryHolder newResetPwdRecord,
            CommonParameterHolder cp, UserProfileHolder userProfile,
            EmailEngine emailEngine, Map<String, Object> map) throws Exception;


    public void saveNewPassword(PasswordHistoryHolder passwordHistory,
            UserProfileTmpHolder userProfile, UserProfileTmpHolder oldUserProfile,
            CommonParameterHolder cp, EmailEngine emailEngine,
            Map<String, Object> map) throws Exception;
}
