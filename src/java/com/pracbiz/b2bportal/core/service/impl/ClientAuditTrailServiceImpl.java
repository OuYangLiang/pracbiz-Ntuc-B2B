package com.pracbiz.b2bportal.core.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.constants.ClientActionType;
import com.pracbiz.b2bportal.core.holder.ClientAuditTrailHolder;
import com.pracbiz.b2bportal.core.mapper.ClientAuditTrailMapper;
import com.pracbiz.b2bportal.core.service.ClientAuditTrailService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.util.VelocityHelper;

public class ClientAuditTrailServiceImpl extends
        DBActionServiceDefaultImpl<ClientAuditTrailHolder> implements
        ClientAuditTrailService
{
    @Autowired
    private ClientAuditTrailMapper mapper;
    @Autowired
    private VelocityHelper velocityHelper;
    @Autowired
    private transient OidService oidService;


    @Override
    public void insert(ClientAuditTrailHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(ClientAuditTrailHolder oldObj_,
            ClientAuditTrailHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKey(ClientAuditTrailHolder oldObj_,
            ClientAuditTrailHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void delete(ClientAuditTrailHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    public void insertAuditTrail(File file, String remoteHost,
            boolean success, String errorMsg, ClientActionType actionType) throws Exception
    {
        ClientAuditTrailHolder clientAuditTrail = new ClientAuditTrailHolder();
        clientAuditTrail.setActionDate(new Date());
        clientAuditTrail.setActionStatus(success);
        clientAuditTrail.setActionType(actionType);
        clientAuditTrail.setActionSummary(errorMsg);
        clientAuditTrail.setAuditOid(oidService.getOid());
        clientAuditTrail.setBatchFileName(file.getName());
        clientAuditTrail.setClientIp(remoteHost);
        clientAuditTrail.setXmlContent(this
                .getXmlContentForAuditTrail(file));
        mapper.insert(clientAuditTrail);
    }


    private String getXmlContentForAuditTrail(File file)
            throws Exception
    {
        List<String> docFilenames = GZIPHelper.getInstance()
                .getFilenamesFromZipFilePath(file.getPath());

        return velocityHelper.getXmlContentForClientAuditTrail(file.getName(),
                docFilenames);
    }

}
