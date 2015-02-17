//*****************************************************************************
//
// File Name       :  SupplierRoleTmpService.java
// Date Created    :  Mar 6, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Mar 6, 2013 9:41:56 PM$
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
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public interface SupplierRoleTmpService extends
        BaseService<SupplierRoleTmpHolder>,
        DBActionService<SupplierRoleTmpHolder>
{
    public List<SupplierRoleTmpHolder> selectByRole(BigDecimal roleOid)
        throws Exception;
}
