//*****************************************************************************
//
// File Name       :  JobMapper.java
// Date Created    :  Aug 26, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 26, 2013 9:50:27 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.mapper;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.JobHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public interface JobMapper extends BaseMapper<JobHolder>,
    DBActionMapper<JobHolder>, PaginatingMapper<JobHolder>
{

}
