//*****************************************************************************
//
// File Name       :  FieldContentValidator.java
// Date Created    :  Jul 18, 2011
// Last Changed By :  $Author: $
// Last Changed On :  $Date: $
// Revision        :  $Rev: $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2011.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator.util;

import java.io.Serializable;


/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming_2009
 */
public interface FieldContentValidator extends Serializable
{
    public String validate(String value, String desc);
}
