package com.pracbiz.b2bportal.core.service.impl;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.mapper.ResetPasswordRequestHistoryMapper;
import com.pracbiz.b2bportal.core.service.PasswordHistoryService;
import com.pracbiz.b2bportal.core.service.ResetPasswordRequestHistoryService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class ResetPasswordRequestHistoryServiceImpl extends
    DBActionServiceDefaultImpl<ResetPasswordRequestHistoryHolder> implements
    ResetPasswordRequestHistoryService, ApplicationContextAware
{
    private ApplicationContext ctx;
    
    @Autowired
    private ResetPasswordRequestHistoryMapper resetPasswordRequestHistoryMapper;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileTmpService userProfileTmpService;
    @Autowired
    private PasswordHistoryService passwordHistoryService;


    @Override
    public List<ResetPasswordRequestHistoryHolder> select(
        ResetPasswordRequestHistoryHolder param) throws Exception
    {
        return resetPasswordRequestHistoryMapper.select(param);
    }


    @Override
    public void delete(ResetPasswordRequestHistoryHolder oldObj)
        throws Exception
    {
        resetPasswordRequestHistoryMapper.delete(oldObj);
    }


    @Override
    public void insert(ResetPasswordRequestHistoryHolder newObj)
        throws Exception
    {
        resetPasswordRequestHistoryMapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(ResetPasswordRequestHistoryHolder oldObj,
        ResetPasswordRequestHistoryHolder newObj) throws Exception
    {
        resetPasswordRequestHistoryMapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(
        ResetPasswordRequestHistoryHolder oldObj,
        ResetPasswordRequestHistoryHolder newObj) throws Exception
    {
        resetPasswordRequestHistoryMapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public void resetPassword(
        ResetPasswordRequestHistoryHolder previousResetPwdRecord,
        ResetPasswordRequestHistoryHolder newResetPwdRecord,
        CommonParameterHolder cp) throws Exception
    {
        if(previousResetPwdRecord != null)
        {
            ResetPasswordRequestHistoryHolder oldObj = previousResetPwdRecord;
            previousResetPwdRecord.setValid(Boolean.FALSE);
            getMeBean().auditUpdateByPrimaryKeySelective(cp, oldObj,
                previousResetPwdRecord);
        }
        getMeBean().auditInsert(cp, newResetPwdRecord);
    }
    
    
    @Override
    public void resetPasswordWithoutAudit(
            ResetPasswordRequestHistoryHolder previousResetPwdRecord,
            ResetPasswordRequestHistoryHolder newResetPwdRecord)
            throws Exception
    {
        if(previousResetPwdRecord != null)
        {
            previousResetPwdRecord.setValid(Boolean.FALSE);
            resetPasswordRequestHistoryMapper
                    .updateByPrimaryKeySelective(previousResetPwdRecord);
        }
        resetPasswordRequestHistoryMapper.insert(newResetPwdRecord);
    }


    @Override
    public void saveNewPassword(PasswordHistoryHolder passwordHistory,
        UserProfileTmpHolder newUserProfile, UserProfileTmpHolder userProfile,
        CommonParameterHolder cp) throws Exception
    {
        ResetPasswordRequestHistoryHolder oldObj = new ResetPasswordRequestHistoryHolder();
        oldObj.setLoginId(userProfile.getLoginId());
        oldObj.setValid(true);
        List<ResetPasswordRequestHistoryHolder> rlt = this.select(oldObj);
        if(rlt != null && !rlt.isEmpty())
        {
            oldObj = rlt.get(0);
            ResetPasswordRequestHistoryHolder newObj = oldObj;
            newObj.setValid(false);
            this.getMeBean().auditUpdateByPrimaryKeySelective(cp, oldObj,
                newObj);
        }
        passwordHistoryService.auditInsert(cp, passwordHistory);
        userProfileService.auditUpdateByPrimaryKeySelective(cp, userProfile,
            newUserProfile);
        userProfileTmpService.auditUpdateByPrimaryKeySelective(cp, userProfile,
            newUserProfile);
    }
    

    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private ResetPasswordRequestHistoryService getMeBean()
    {
        return ctx.getBean("resetPasswordRequestHistoryService", ResetPasswordRequestHistoryService.class);
    }
    
}