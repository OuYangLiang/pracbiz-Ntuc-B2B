package com.pracbiz.b2bportal.core.service;

import java.io.File;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.constants.ClientActionType;
import com.pracbiz.b2bportal.core.holder.ClientAuditTrailHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author Gejiankui
 */
public interface ClientAuditTrailService extends
        DBActionService<ClientAuditTrailHolder>
{
    public void insertAuditTrail(File file, String remoteHost,
            boolean success, String errorMsg, ClientActionType actionType) throws Exception;
}
