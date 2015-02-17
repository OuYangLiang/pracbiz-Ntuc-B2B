//*****************************************************************************
//
// File Name       :  ItemBarcodeService.java
// Date Created    :  Dec 3, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 3, 2013 10:04:23 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.ItemBarcodeHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public interface ItemBarcodeService extends DBActionService<ItemBarcodeHolder>
{
    public int deleteItemBarcodeByBuyerOid(BigDecimal buyerOid) throws Exception;
    
    
    public int deleteItemBarcodeByItemOid(BigInteger itemOid) throws Exception;
}
