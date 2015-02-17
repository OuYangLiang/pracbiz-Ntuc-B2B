//*****************************************************************************
//
// File Name       :  AuditAccessService.java
// Date Created    :  Sep 26, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 26, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.util.List;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.constants.AuditAccessErrorCode;
import com.pracbiz.b2bportal.core.holder.AuditAccessHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.extension.AuditAccessExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public interface AuditAccessService extends BaseService<AuditAccessHolder>,
    DBActionService<AuditAccessHolder>,PaginatingService<AuditAccessHolder>
{
    public void createAuditAuccessForLoginFailed(UserProfileHolder user, String clientIp,AuditAccessErrorCode errorCode) throws Exception;
    
    public byte[] downloadAuditAccessService(BaseAction action,AuditAccessExHolder param, List<AuditAccessHolder> records) throws Exception;
}
