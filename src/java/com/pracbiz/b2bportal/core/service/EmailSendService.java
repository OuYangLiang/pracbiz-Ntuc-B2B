//*****************************************************************************
//
// File Name       :  EmailSendService.java
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

package com.pracbiz.b2bportal.core.service;

import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface EmailSendService
{

    public boolean sendSetPasswordEmailByCallable(CommonParameterHolder cp,
            String requestUrl, String clientIp, List<UserProfileTmpHolder> users)
            throws Exception;

    
    public void setNoAudit(Boolean noAudit);
    
}
