//*****************************************************************************
//
// File Name       :  AuditAccessAction.java
// Date Created    :  Oct 9, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Oct 9, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.action.GridResult;
import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.AccessActionType;
import com.pracbiz.b2bportal.core.constants.ArithmeticTerm;
import com.pracbiz.b2bportal.core.constants.PrincipalType;
import com.pracbiz.b2bportal.core.holder.AuditAccessHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.AuditAccessExHolder;
import com.pracbiz.b2bportal.core.service.AuditAccessService;
import com.pracbiz.b2bportal.core.service.UserTypeService;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class AuditAccessAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(AuditAccessAction.class);
    private static final long serialVersionUID = -1819979923932050879L;
    
    private static final String SESSION_KEY_SEARCH_PARAMETER_AUDIT_ACCESS = "SEARCH_PARAMETER_AUDIT_ACCESS";
    private static final String ACCESS_SUCC="Yes";
    private static final String ACCESS_FAILED="No";
    
    private AuditAccessExHolder param;
    
    private List<? extends Object> userTypes;
    private Map<String, String> principalTypes;
    private Map<String, String> accessActionTypes;
    private Map<String, String> arithmeticTerms;
    private static final Map<String, String> sortMap;
    
    private transient InputStream auditAccessRlt;
    private String auditAccessFile;
    private String contentType;
    
    @Autowired private transient UserTypeService userTypeService;
    @Autowired private transient AuditAccessService auditAccessService;
    
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("principalType", "PRINCIPAL_TYPE");
        sortMap.put("loginId", "LOGIN_ID");
        sortMap.put("actionDate", "ACTION_DATE");
        sortMap.put("actionType", "ACTION_TYPE");
        sortMap.put("success", "SUCCESS");
        sortMap.put("attemptNo", "ATTEMPT_NO");
        sortMap.put("clientIp", "CLIENT_IP");
        sortMap.put("errorCode", "ERROR_CODE");
    }
    
    public AuditAccessAction()
    {
        this.initMsg();
    }
    
    // ***************************************************
    // summary page
    // ***************************************************
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_AUDIT_ACCESS);
        
        param = (AuditAccessExHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_AUDIT_ACCESS);
        if (param == null)
        {
            param = new AuditAccessExHolder();
            param.setActionDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                    DateUtil.getInstance().getCurrentFirstDayOfMonth()));
            param.setActionDateTo(DateUtil.getInstance().getLastTimeOfDay(
                    DateUtil.getInstance().getCurrentLastDayOfMonth()));
        }
        
        try
        {
            param.setPrincipalType(PrincipalType.USER);
            this.initSearchCondition();
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_AUDIT_ACCESS, param);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }

    
    public String search()
    {
        if (param == null)
        {
            param = new AuditAccessExHolder();
            param.setPrincipalType(PrincipalType.USER);
        }
        
        try
        {
            param.setActionDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getActionDateFrom()));
            param.setActionDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getActionDateTo()));
            param.setArithmeticTermValue();
            param.trimAllString();
            param.setAllEmptyStringToNull();
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_AUDIT_ACCESS, param);
        
        return SUCCESS;
    }
    
    
    public String data()
    {
        AuditAccessExHolder searchParam = (AuditAccessExHolder)getSession().get(SESSION_KEY_SEARCH_PARAMETER_AUDIT_ACCESS); 
    
        if (searchParam == null)
        {
            searchParam = new AuditAccessExHolder();
            searchParam.setPrincipalType(PrincipalType.USER);
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_AUDIT_ACCESS, searchParam);
        }
        searchParam.setCurrentUserOid(getProfileOfCurrentUser().getUserOid());
        searchParam.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        searchParam.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
        searchParam.setCurrentUserTypeOid(this.getProfileOfCurrentUser().getUserType());
        
        
        
        try
        {
            super.obtainListRecordsOfPagination(auditAccessService, searchParam, sortMap, "accessOid", null);
        
            for (BaseHolder baseItem : this.getGridRlt().getItems())
            {
                AuditAccessExHolder item = (AuditAccessExHolder) baseItem;
                if (item.getSuccess() != null && item.getSuccess())
                {
                    item.setSuccessValue(ACCESS_SUCC);
                }
                else
                {
                    item.setSuccessValue(ACCESS_FAILED);
                }
                
                if (item.getActionType() != null)
                {
                    item.setActionTypeValue(this.getText(item.getActionType().getKey()));
                }
                
                if (item.getErrorCode() != null)
                {
                    item.setErrorDesc(this.getText(item.getErrorCode().getKey()));
                }
                
                if (item.getPrincipalType() != null)
                {
                    item.setPrincipalTypeValue(this.getText(item.getPrincipalType().getKey()));
                }
            }
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    public String download()
    {
        AuditAccessExHolder searchParam = (AuditAccessExHolder)getSession().get(SESSION_KEY_SEARCH_PARAMETER_AUDIT_ACCESS); 
        
        if (searchParam == null)
        {
            searchParam = new AuditAccessExHolder();
            searchParam.setPrincipalType(PrincipalType.USER);
        }
        
        searchParam.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        searchParam.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
        searchParam.setCurrentUserTypeOid(this.getProfileOfCurrentUser().getUserType());
        
        try
        {
            this.obtainListRecordsOfPagination(auditAccessService, searchParam, sortMap, "accessOid");
            @SuppressWarnings("unchecked")
            List<AuditAccessHolder> records = (List<AuditAccessHolder>)this.getGridRlt().getItems();
            
            if (records == null || records.isEmpty())
            {
                msg.setTitle(this.getText("audit.access.summary"));
                msg.saveError(this.getText("B2BPC1404"));
                MessageTargetHolder mt = new MessageTargetHolder();
                mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
                mt.setTargetURI(INIT);
                mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
                
                msg.addMessageTarget(mt);
                return FORWARD_COMMON_MESSAGE;
            }
            auditAccessFile = "AuditAccessReport_"+DateUtil.getInstance().getCurrentLogicTimeStamp() + ".xml";
            contentType = "application/octet-stream";
            byte[] datas = auditAccessService.downloadAuditAccessService(this, searchParam, records);
            auditAccessRlt = new ByteArrayInputStream(datas);
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    // ***************************************************
    // private method
    // ***************************************************
    private void initSearchCondition() throws Exception
    {
        principalTypes = PrincipalType.toMapValue();
        accessActionTypes = AccessActionType.toMapValue();
        arithmeticTerms = ArithmeticTerm.toMapValue();
        
        userTypes= userTypeService.selectPrivilegedSubUserTypesByUserTypeInclusively(this.getUserTypeOfCurrentUser());

        if (userTypes == null) userTypes = new ArrayList<UserTypeHolder>();
    }
    
    
    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
        
        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        
        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
        
        msg.addMessageTarget(mt);
    }
    
    
    @SuppressWarnings({"unchecked", "rawtypes" })
    protected void obtainListRecordsOfPagination(PaginatingService service_,
        BaseHolder param, Map<String, String> sortFieldMap,
        String itemIdentifier) throws Exception
    {
        int recordCount = service_.getCountOfSummary(param);
        param.setStartRecordNum(start);
        param.setNumberOfRecordsToSelect(recordCount);

        List<BaseHolder> recordList = service_.getListOfSummary(param);
        
        int index = start;
        for (BaseHolder bh : recordList)
            bh.setDojoIndex(++index);
        
        gridRlt = new GridResult();
        gridRlt.setIdentifier(itemIdentifier);
        gridRlt.setItems(recordList);
        gridRlt.setTotalRecords(recordCount);
    }
    
    
    // ***************************************************
    // private method
    // ***************************************************

    public AuditAccessExHolder getParam()
    {
        return param;
    }

    public void setParam(AuditAccessExHolder param)
    {
        this.param = param;
    }

    
    public Map<String, String> getPrincipalTypes()
    {
        return principalTypes;
    }


    public void setPrincipalTypes(Map<String, String> principalTypes)
    {
        this.principalTypes = principalTypes;
    }


    public Map<String, String> getAccessActionTypes()
    {
        return accessActionTypes;
    }


    public void setAccessActionTypes(Map<String, String> accessActionTypes)
    {
        this.accessActionTypes = accessActionTypes;
    }


    public List<? extends Object> getUserTypes()
    {
        return userTypes;
    }


    public void setUserTypes(List<? extends Object> userTypes)
    {
        this.userTypes = userTypes;
    }


    public Map<String, String> getArithmeticTerms()
    {
        return arithmeticTerms;
    }


    public void setArithmeticTerms(Map<String, String> arithmeticTerms)
    {
        this.arithmeticTerms = arithmeticTerms;
    }

    
    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getAuditAccessFile()
    {
        return auditAccessFile;
    }

    public void setAuditAccessFile(String auditAccessFile)
    {
        this.auditAccessFile = auditAccessFile;
    }


    public InputStream getAuditAccessRlt()
    {
        return auditAccessRlt;
    }


    public void setAuditAccessRlt(InputStream auditAccessRlt)
    {
        this.auditAccessRlt = auditAccessRlt;
    }

    
}
