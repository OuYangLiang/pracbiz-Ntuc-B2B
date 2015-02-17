//*****************************************************************************
//
// File Name       :  AuditAccessServiceImpl.java
// Date Created    :  Sep 26, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 26, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.AccessActionType;
import com.pracbiz.b2bportal.core.constants.AuditAccessErrorCode;
import com.pracbiz.b2bportal.core.constants.PrincipalType;
import com.pracbiz.b2bportal.core.holder.AuditAccessHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.extension.AuditAccessExHolder;
import com.pracbiz.b2bportal.core.mapper.AuditAccessMapper;
import com.pracbiz.b2bportal.core.service.AuditAccessService;
import com.pracbiz.b2bportal.core.service.OidService;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class AuditAccessServiceImpl extends DBActionServiceDefaultImpl<AuditAccessHolder> implements AuditAccessService
{
    private static final String TEMPLATE_AUDIT_ACCESS = "audit_access_template_file.vm";
    private static final String DATE_FORMATE="yyyy-MM-dd 'T' HH:mm:ss";
    
    @Autowired private AuditAccessMapper mapper;
    @Autowired private VelocityEngine velocityEngine;
    @Autowired private OidService oidService;
    
    @Override
    public List<AuditAccessHolder> select(AuditAccessHolder param)
        throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(AuditAccessHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(AuditAccessHolder oldObj_,
        AuditAccessHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(AuditAccessHolder oldObj_,
        AuditAccessHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(AuditAccessHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public int getCountOfSummary(AuditAccessHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<AuditAccessHolder> getListOfSummary(AuditAccessHolder param)
        throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public byte[] downloadAuditAccessService(BaseAction action,AuditAccessExHolder param,List<AuditAccessHolder> records) throws Exception
    {
        Map<String,Object> map = new HashMap<String,Object>();
        
        Map<String,Object> searchCondition = this.getSearchCondition(action, param);
        if (records == null || records.isEmpty())
        {
            return null;
        }
        
        searchCondition.put("TOTAL_RECORD", records.size());
        map.put("AUDIT_ACCESS", searchCondition);
        
        List<Map<String,Object>> details = this.getDetails(action, records);
        map.put("RECORDS", details);
        
        String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, TEMPLATE_AUDIT_ACCESS, map);
        return result.getBytes(CommonConstants.ENCODING_UTF8);
    }
    
    
    private Map<String,Object> getSearchCondition(BaseAction action,AuditAccessExHolder param)
    {
        Map<String,Object> searchCondition = new HashMap<String, Object>();
        searchCondition.put("REPORT_DATE", DateUtil.getInstance().convertDateToString(new Date(),DATE_FORMATE));
        if (param.getPrincipalType() != null)
        {
            searchCondition.put("ACCESS_TYPE", param.getPrincipalType().name());
        }
        
        searchCondition.put("USER_TYPE", param.getUserType());
        searchCondition.put("ACTION_TYPE", param.getActionType());
        searchCondition.put("ACTION_DATE_FROM", DateUtil.getInstance().convertDateToString(param.getActionDateFrom(),DATE_FORMATE));
        searchCondition.put("ACTION_DATE_TO", DateUtil.getInstance().convertDateToString(param.getActionDateTo(),DATE_FORMATE));
        if (param.getArithmeticTerm() != null)
        {
            String attempt = action.getText(param.getArithmeticTerm().getKey()) + " " + param.getAttemptNo();
            searchCondition.put("ATTEMPT_NO", attempt);
        }
        
        searchCondition.put("LOGIN_ID", param.getLoginId());
        searchCondition.put("CLIENT_IP", param.getClientIp());
        return searchCondition;
    }
    
    private List<Map<String,Object>> getDetails(BaseAction action,List<AuditAccessHolder> records)
    {
        List<Map<String,Object>> details = new ArrayList<Map<String,Object>>();
        
        for (AuditAccessHolder auditAccess : records)
        {
            Map<String,Object> detail = new HashMap<String, Object>();
            if (auditAccess.getPrincipalType() != null)
            {
                detail.put("ACCESS_TYPE", auditAccess.getPrincipalType().name());
            }
            
            detail.put("LOGIN_ID", auditAccess.getLoginId());
            detail.put("PRINCIPAL_OID", auditAccess.getPrincipalOid());
            if (auditAccess.getActionType() != null)
            {
                detail.put("ACTION_TYPE", auditAccess.getActionType().name());
            }
      
            detail.put("ACTION_DATE", DateUtil.getInstance().convertDateToString(auditAccess.getActionDate(),DATE_FORMATE));
            detail.put("SUCCESS", auditAccess.getSuccess());
            detail.put("ATTEMPT_NO", auditAccess.getAttemptNo());
            if (auditAccess.getErrorCode() != null)
            {
                String errorDesc = action.getText(auditAccess.getErrorCode().getKey());
                detail.put("ERROR_DESC", errorDesc);
            }
            
            detail.put("CLIENT_IP", auditAccess.getClientIp());
            details.add(detail);
        }
        
        return details;
    }



    @Override
    public void createAuditAuccessForLoginFailed(
        UserProfileHolder user, String clientIp, AuditAccessErrorCode errorCode)
        throws Exception
    {
        AuditAccessHolder auditAccess =  mapper.selectLastRecordByLoginId(user.getLoginId());
        
        if (auditAccess == null)
        {
            auditAccess = new AuditAccessHolder();
            auditAccess.setAccessOid(oidService.getOid());
            auditAccess.setPrincipalType(PrincipalType.USER);
            auditAccess.setPrincipalOid(user.getUserOid());
            auditAccess.setLoginId(user.getLoginId());
            auditAccess.setClientIp(clientIp);
            auditAccess.setSuccess(false);
            auditAccess.setActionType(AccessActionType.IN);
            auditAccess.setActionDate(new Date());
            auditAccess.setErrorCode(errorCode);
            auditAccess.setAttemptNo(1);
            auditAccess.setUserTypeOid(user.getUserType());
            auditAccess.setCompanyOid(user.getBuyerOid() == null ? 
                    (user.getSupplierOid() == null ? null : user.getSupplierOid()) : user.getBuyerOid());
        }
        else
        {
            auditAccess.setAccessOid(oidService.getOid());
            auditAccess.setActionDate(new Date());
            auditAccess.setErrorCode(errorCode);
            auditAccess.setSuccess(false);
            auditAccess.setActionType(AccessActionType.IN);
            int attemptNo = auditAccess.getAttemptNo() == null ? 1 : auditAccess.getAttemptNo() + 1;
            auditAccess.setAttemptNo(attemptNo);
        }
        
        mapper.insert(auditAccess);
    }


}
