//*****************************************************************************
//
// File Name       :  ChangePasswordService.java
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

package com.pracbiz.b2bportal.core.service;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;


/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public interface ChangePasswordService
{
    public void doChangePassword(CommonParameterHolder commPara_,
        UserProfileHolder newObj_, UserProfileHolder oldObj_) throws Exception;

}
