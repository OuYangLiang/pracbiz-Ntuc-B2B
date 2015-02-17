//*****************************************************************************
//
// File Name       :  GiDetailExtendedService.java
// Date Created    :  Nov 13, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 13, 2013 3:11:02 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.GiDetailExtendedHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public interface GiDetailExtendedService extends
DBActionService<GiDetailExtendedHolder>,BaseService<GiDetailExtendedHolder>
{
    public List<GiDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal giOid)throws Exception;
}
