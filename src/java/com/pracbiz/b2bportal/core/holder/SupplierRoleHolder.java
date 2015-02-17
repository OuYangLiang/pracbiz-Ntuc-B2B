//*****************************************************************************
//
// File Name       :  SupplierRole.java
// Date Created    :  Mar 6, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Mar 6, 2013 10:55:31 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class SupplierRoleHolder extends BaseHolder
{
    private static final long serialVersionUID = 1L;
    private BigDecimal supplierOid;
    private BigDecimal roleOid;

    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }

    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }

    public BigDecimal getRoleOid()
    {
        return roleOid;
    }

    public void setRoleOid(BigDecimal roleOid)
    {
        this.roleOid = roleOid;
    }

    @Override
    public String getCustomIdentification()
    {
        return supplierOid.toString() + roleOid.toString();
    }

}
