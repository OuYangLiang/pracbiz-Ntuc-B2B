package com.pracbiz.b2bportal.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.holder.UserSessionHolder;
import com.pracbiz.b2bportal.core.service.StartupService;
import com.pracbiz.b2bportal.core.service.UserSessionService;

public class StartupServiceImpl implements StartupService
{
    @Autowired UserSessionService userSessionService;

    @Override
    public void init() throws Exception
    {
        userSessionService.delete(new UserSessionHolder());
    }
    
}
