//*****************************************************************************
//
// File Name       :  JobService.java
// Date Created    :  Aug 26, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 26, 2013 10:12:33 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.JobHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public interface JobService extends BaseService<JobHolder>,
    DBActionService<JobHolder>, PaginatingService<JobHolder>
{
    public JobHolder selectByKey(BigDecimal jobOid) throws Exception;
}
