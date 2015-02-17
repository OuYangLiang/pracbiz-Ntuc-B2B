//*****************************************************************************
//
// File Name       :  ClassService.java
// Date Created    :  Oct 10, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Oct 10, 2013 6:29:13 PM $
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
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.extension.ClassExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface ClassService extends BaseService<ClassHolder>, DBActionService<ClassHolder>
{
    public int deleteClassByBuyerOid(BigDecimal buyerOid) throws Exception;

    
    public List<ClassExHolder> selectClassByUserOid(BigDecimal userOid)
        throws Exception;

    
    public List<ClassExHolder> selectTmpClassByUserOid(BigDecimal userOid)
        throws Exception;

    
    public List<ClassExHolder> selectClassByBuyerOid(BigDecimal buyerOid)
        throws Exception;
    
    
    public ClassHolder selectClassByItemCodeAndBuyerOid(String buyerItemCode,
            BigDecimal buyerOid) throws Exception;
    
    
    public void deleteByClassOid(BigDecimal classOid) throws Exception;
    
    
    public ClassHolder selectByKey(BigDecimal classOid) throws Exception;
    
    
    public ClassHolder selectByBuyerOidAndClassCode(BigDecimal buyerOid, String classCode) throws Exception;
}
