//*****************************************************************************
//
// File Name       :  BaseMapper.java
// Date Created    :  Sep 24, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: 2011-07-01 10:56:27 +0800 (周五, 01 七月 2011) $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.base.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public interface BaseMapper<T extends BaseHolder>
{
    List<T> select(T record);
}
