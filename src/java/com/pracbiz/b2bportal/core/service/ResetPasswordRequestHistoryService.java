package com.pracbiz.b2bportal.core.service;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface ResetPasswordRequestHistoryService extends
    BaseService<ResetPasswordRequestHistoryHolder>,
    DBActionService<ResetPasswordRequestHistoryHolder>
{
    public void resetPassword(
        ResetPasswordRequestHistoryHolder previousResetPwdRecord,
        ResetPasswordRequestHistoryHolder newResetPwdRecord,
        CommonParameterHolder cp) throws Exception;

    public void saveNewPassword(PasswordHistoryHolder passwordHistory,
        UserProfileTmpHolder userProfile, UserProfileTmpHolder oldUserProfile,
        CommonParameterHolder cp) throws Exception;
    

    public void resetPasswordWithoutAudit(
            ResetPasswordRequestHistoryHolder previousResetPwdRecord,
            ResetPasswordRequestHistoryHolder newResetPwdRecord)
            throws Exception;
            
}
