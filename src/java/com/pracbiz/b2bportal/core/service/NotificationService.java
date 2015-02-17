package com.pracbiz.b2bportal.core.service;

import com.pracbiz.b2bportal.core.eai.message.BatchMsg;

public interface NotificationService
{
    public void sendOutboundNotificationEmail(BatchMsg batch) throws Exception;
    
    
    public void sendInboundNotificationEmail(BatchMsg batch) throws Exception;
}
