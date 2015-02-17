//*****************************************************************************
//
// File Name       :  PoDetailExtendedService.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-11 $
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
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface PoDetailExtendedService extends
    DBActionService<PoDetailExtendedHolder>
{
    public List<PoDetailExtendedHolder> selectDetailExtendedByKeyAndLineSeqNo(BigDecimal poOid,Integer lineSeqNo)throws Exception;

    public List<PoDetailExtendedHolder> selectDetailExtendedByKey(BigDecimal poOid)throws Exception;
}
