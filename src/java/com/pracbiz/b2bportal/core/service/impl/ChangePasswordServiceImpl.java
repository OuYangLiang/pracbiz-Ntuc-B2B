//*****************************************************************************
//
// File Name       :  ChangePasswordServiceImpl.java
// Date Created    :  Aug 30, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Aug 30, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.service.ChangePasswordService;
import com.pracbiz.b2bportal.core.service.PasswordHistoryService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class ChangePasswordServiceImpl implements ChangePasswordService, CoreCommonConstants
{
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private PasswordHistoryService passwordHistoryService;
    @Autowired
    private UserProfileTmpService userProfileTmpService;
    
    @Override
    public void doChangePassword(CommonParameterHolder commPara_,
        UserProfileHolder newObj_, UserProfileHolder oldObj_) throws Exception
    {
        UserProfileTmpExHolder newUserTmpEx = new UserProfileTmpExHolder();
        BeanUtils.copyProperties(newObj_, newUserTmpEx);
        
        UserProfileTmpExHolder oldUserTmpEx = new UserProfileTmpExHolder();
        BeanUtils.copyProperties(oldObj_, oldUserTmpEx);
        
        userProfileService.auditUpdateByPrimaryKeySelective(commPara_, oldUserTmpEx,
            newUserTmpEx);
        
        UserProfileTmpHolder oldTmp = userProfileTmpService.selectUserProfileTmpByKey(newUserTmpEx.getUserOid());
        UserProfileTmpHolder newTmp = new UserProfileTmpHolder();
        BeanUtils.copyProperties(oldTmp, newTmp);
        newTmp.setLoginPwd(newUserTmpEx.getLoginPwd());
        newTmp.setUpdateBy(newUserTmpEx.getUserName());
        newTmp.setUpdateDate(newUserTmpEx.getUpdateDate());
        userProfileTmpService.updateByPrimaryKeySelective(oldTmp, newTmp);
        PasswordHistoryHolder pwdHis_ = new PasswordHistoryHolder();
        pwdHis_.setUserOid(newObj_.getUserOid());
        pwdHis_.setChangeDate(new java.util.Date());
        pwdHis_.setOldLoginPwd(newObj_.getLoginPwd());
        pwdHis_.setActor(newObj_.getLoginId());
        pwdHis_.setChangeReason(CODE_PASSWORD_EXPIRED);

        passwordHistoryService.auditInsert(commPara_, pwdHis_);
    }
    
}
