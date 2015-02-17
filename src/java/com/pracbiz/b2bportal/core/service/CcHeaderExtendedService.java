//*****************************************************************************
//
// File Name       :  CcHeaderExtendedService.java
// Date Created    :  2013-12-24
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2013-12-24 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;


import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.CcHeaderExtendedHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface CcHeaderExtendedService extends
    DBActionService<CcHeaderExtendedHolder>
{
    public List<CcHeaderExtendedHolder> selectHeaderExtendedByKey(BigDecimal invOid);
}
