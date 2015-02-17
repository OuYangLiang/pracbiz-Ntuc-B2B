//*****************************************************************************
//
// File Name       :  BaseActionTestCase.java
// Date Created    :  Oct 15, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Oct 15, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.test.base;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.pracbiz.b2bportal.base.action.BaseAction;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class BaseActionTestCase extends StrutsSpringJUnit4TestCase<BaseAction>
{
    
}
