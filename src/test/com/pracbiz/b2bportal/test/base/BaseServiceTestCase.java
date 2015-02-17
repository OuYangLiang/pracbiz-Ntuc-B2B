package com.pracbiz.b2bportal.test.base;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class BaseServiceTestCase extends TestCase
{
    protected static final Logger LOG = Logger
        .getLogger(BaseServiceTestCase.class);

    protected static ApplicationContext ctx = TestEnvironment.getInstance()
        .getApplicationContext();

}
