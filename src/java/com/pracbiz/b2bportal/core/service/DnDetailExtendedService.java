//*****************************************************************************
//
// File Name       :  DnDetailExtendedService.java
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
import com.pracbiz.b2bportal.core.holder.DnDetailExtendedHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface DnDetailExtendedService extends
    DBActionService<DnDetailExtendedHolder>
{
    public List<DnDetailExtendedHolder> selectDetailExtendedByKeyAndLineSeqNo(
        BigDecimal dnOid, Integer lineSeqNo) throws Exception;

    public List<DnDetailExtendedHolder> selectDetailExtendedByKey(
        BigDecimal dnOid) throws Exception;
}
