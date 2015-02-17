//*****************************************************************************
//
// File Name       :  GroupSupplierTmpService.java
// Date Created    :  Sep 6, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 6, 2012 $
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
import com.pracbiz.b2bportal.core.holder.GroupSupplierTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public interface GroupSupplierTmpService extends
    DBActionService<GroupSupplierTmpHolder>
{
    public List<GroupSupplierTmpHolder> selectGroupSupplierTmpByGroupOid(
        BigDecimal groupOid) throws Exception;

}
