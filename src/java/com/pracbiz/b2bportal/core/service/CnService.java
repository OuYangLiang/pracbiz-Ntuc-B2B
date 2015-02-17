//*****************************************************************************
//
// File Name       :  CnService.java
// Date Created    :  Feb 19, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Feb 19, 2014 7:09:59 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public interface CnService extends DBActionService<CnHolder>
{
    public void createCn(CommonParameterHolder cp, CnHolder cn,
        MsgTransactionsHolder msg, PoHeaderHolder poHeader, BuyerHolder buyer,
        SupplierHolder supplier, PoStatus poStatus) throws Exception;
    
    
    public void createAndSentCn(CommonParameterHolder cp, CnHolder cn,
        MsgTransactionsHolder msg, PoHeaderHolder poHeader, BuyerHolder buyer,
        SupplierHolder supplier, PoStatus poStatus) throws Exception;
    
    public CnHolder selectByKey(BigDecimal cnOid) throws Exception;
}
