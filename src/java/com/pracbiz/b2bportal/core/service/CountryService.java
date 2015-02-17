//*****************************************************************************
//
// File Name       :  CountryService.java
// Date Created    :  Sep 21, 2012
// Last Changed By :  $Author: xuchengqing $
// Last Changed On :  $Date: 2011-07-01 10:56:27 +0800 (周五, 01 七月 2011) $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.core.holder.CountryHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public interface CountryService extends BaseService<CountryHolder>
{
    public CountryHolder selectByCtryCode(String ctryCode) throws Exception;
}
