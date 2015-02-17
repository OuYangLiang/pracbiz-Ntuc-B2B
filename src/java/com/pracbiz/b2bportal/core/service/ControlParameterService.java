/**
 *   File Name       :  ControlParameterService.java
 *   Date Created    :  2012-08-37 16:08:781
 *   Last Changed By :  $Author: jiangming $
 *   Last Changed On :  $Date: 2012-08-37 16:08:781 $
 *   Revision        :  $Revision: 1.0 $
 *   Release         :  $Name: $
 *   Description     :  *
 *   PracBiz Pte Ltd.  Copyright (c) 2007-2008.  All Rights Reserved. 
 *
 **/

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;

/**
 * @author jiangming
 * @name ControlParameterService.java
 */
public interface ControlParameterService extends
    BaseService<ControlParameterHolder>,
    DBActionService<ControlParameterHolder>
{

    public List<ControlParameterHolder> selectAllControlParameters()
        throws Exception;

    
    public List<ControlParameterHolder> selectCacheControlParametersBySectId(
        String sectId) throws Exception;

    
    public ControlParameterHolder selectControlParameterByKey(
        BigDecimal paramOid) throws Exception;

    
    public ControlParameterHolder selectCacheControlParameterBySectIdAndParamId(
        String sectId_, String paramId_) throws Exception;

    
    public List<ControlParameterHolder> selectControlParametersByCatId(String catId) throws Exception;
    
    
    public ControlParameterHolder selectControlParameterByParametersBySectIdAndParamId(
        String sectId, String paramId) throws Exception;
}