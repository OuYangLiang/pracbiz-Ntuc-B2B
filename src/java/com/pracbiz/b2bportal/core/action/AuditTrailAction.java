package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.ActorAction;
import com.pracbiz.b2bportal.core.holder.AuditTrailHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.AuditSessionExHolder;
import com.pracbiz.b2bportal.core.holder.extension.AuditTrailExHolder;
import com.pracbiz.b2bportal.core.service.AuditSessionService;
import com.pracbiz.b2bportal.core.service.AuditTrailService;
import com.pracbiz.b2bportal.core.service.UserTypeService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class AuditTrailAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(AuditTrailAction.class);
    private static final long serialVersionUID = 1L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_AUDIT_TRAIL = "SEARCH_PARAMETER_AUDIT_TRAIL";
    public static final String SESSION_KEY_SEARCH_PARAMETER_AUDIT_SESSION = "SEARCH_PARAMETER_AUDIT_SESSION";
    
    private static final String ELEMENT_AUDIT_SEARCH_CRITERION_KEY = "key";
    private static final String ELEMENT_AUDIT_SEARCH_CRITERION_SUB_KEY = "subKey";
    private static final String ELEMENT_AUDIT_SEARCH_CRITERION_USER_TYPE = "userType";
    private static final String ELEMENT_AUDIT_RECORD_SET = "recordSet";
    private static final String ELEMENT_RECORD = "record";
    private static final String ELEMENT_FIELD = "field";
    
    private static final String ATTR_RECORD_TOTAL = "total";
    private static final String ATTR_RECORD_NUMBER = "number";
    private static final String ATTR_SEARCH_NAME = "name";
    private static final String ATTR_RECORD_HAS_SUBKEY = "hasSubKey";
    private static final String ATTR_RECORD_SEARCH_CRITERION_ACTION = "action";
    private static final String ATTR_RECORD_SEARCH_CRITERION_ACTION_DATE = "actionDate";
    private static final String ATTR_RECORD_SEARCH_CRITERION_ACTION_DATE_FROM = "from";
    private static final String ATTR_RECORD_SEARCH_CRITERION_ACTION_DATE_TO = "to";
    private static final String ATTR_RECORD_SEARCH_CRITERION_ACTOR = "actor";
    private static final String ATTR_RECORD_SEARCH_CRITERION_COMPANY = "company";
    private static final String ATTR_RECORD_SEARCH_CRITERION_ACCESS_IP = "accessIP";
    private static final String ATTR_RECORD_SEARCH_CRITERION_SESSION_ID = "sessionID";
    private static final String ATTR_RECORD_SEARCH_CRITERION_RECORD_TYPE = "recordType";
    
    private static final String REPORT_FILE_NAME = "AuditTrailReport_";
    
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    
    
    private static final Map<String, String> sortMap, sortMapSession;
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("auditTrailOid", "ADT_OID");
        sortMap.put("sessionId", "SESSION_ID");
        sortMap.put("userTypeDesc", "USER_TYPE_DESC");
        sortMap.put("actor", "ACTOR");
        sortMap.put("actionDate", "ACTION_DATE");
        sortMap.put("actorAction", "ACTOR_ACTION");
        sortMap.put("recordKey", "RECORD_KEY");
        sortMap.put("recordType", "RECORD_TYPE");
        sortMap.put("clientIp", "CLIENT_IP");
    }
    static
    {
        sortMapSession = new HashMap<String, String>();
        sortMapSession.put("sessionId", "SESSION_ID");
        sortMapSession.put("loginId", "LOGIN_ID");
        sortMapSession.put("startDate", "START_DATE");
        sortMapSession.put("endDate", "END_DATE");
    }

    private AuditTrailExHolder param;
    private AuditSessionExHolder auditSession;
    private List<UserTypeHolder> userTypes;
    private Map<String, String> actorActions;
    private Map<String, String> recordTypes;

    @Autowired
    private transient UserTypeService userTypeService;
    @Autowired
    private transient AuditTrailService auditTrailService;
    @Autowired
    private transient AuditSessionService auditSessionService;

    private transient InputStream rptResult;
    private String rptFileName;
    
    private String downloadFlag;
    
    public AuditTrailAction()
    {
        this.initMsg();
    }


    // *****************************************************
    // summary page
    // *****************************************************
    
    public String init()
    {
        try
        {
            clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_AUDIT_TRAIL);
            recordTypes = this.initRecordTypes();
            userTypes= userTypeService.selectPrivilegedSubUserTypesByUserTypeInclusively(this.getUserTypeOfCurrentUser());
            if (userTypes == null) userTypes = new ArrayList<UserTypeHolder>();
            actorActions = ActorAction.toMapValue();
            param = new AuditTrailExHolder();
            initAuditTrailQuery(param);
            param.setDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                    DateUtil.getInstance().getCurrentFirstDayOfMonth()));
            param.setDateTo(DateUtil.getInstance().getLastTimeOfDay(
                    DateUtil.getInstance().getCurrentLastDayOfMonth()));
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_AUDIT_TRAIL, param);
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
        if (null == param)
        {
            param = new AuditTrailExHolder();
        }
        try
        {
            param.trimAllString();
            param.setAllEmptyStringToNull();
            if (param.getDateTo() != null)
            {
                param.setDateTo(DateUtil.getInstance().getMaxTimeOfDate(
                        param.getDateTo()));
            }
            initAuditTrailQuery(param);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_AUDIT_TRAIL, param);

        return SUCCESS;
    }


    public String data()
    {
        try
        {
            AuditTrailExHolder searchParam = (AuditTrailExHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_AUDIT_TRAIL);

            if (searchParam == null)
            {
                searchParam = new AuditTrailExHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_AUDIT_TRAIL, searchParam);
            }
            searchParam.setCurrentUserOid(getProfileOfCurrentUser().getUserOid());
            searchParam.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            searchParam.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
            searchParam.setCurrentUserTypeOid(this.getProfileOfCurrentUser().getUserType());
            
            this.obtainListRecordsOfPagination(auditTrailService, searchParam,
                    sortMap, "auditTrailOid", "module_key_auditTrail");
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }


    // *****************************************************
    // select session
    // *****************************************************
    public String initSession()
    {
        try
        {
            clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_AUDIT_SESSION);
            auditSession = new AuditSessionExHolder();
            initAuditSessionQuery(auditSession);
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_AUDIT_SESSION,
                    auditSession);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String searchSession()
    {
        if (null == auditSession)
        {
            auditSession = new AuditSessionExHolder();
        }
        try
        {
            auditSession.trimAllString();
            auditSession.setAllEmptyStringToNull();
            if (auditSession.getDateTo() != null)
            {
                auditSession.setDateTo(DateUtil.getInstance().getMaxTimeOfDate(
                        auditSession.getDateTo()));
            }
            initAuditSessionQuery(auditSession);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_AUDIT_SESSION,
                auditSession);

        return SUCCESS;
    }


    public String dataSession()
    {
        try
        {
            AuditSessionExHolder searchParam = (AuditSessionExHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_AUDIT_SESSION);

            if (searchParam == null)
            {
                searchParam = new AuditSessionExHolder();
            }
            this.obtainListRecordsOfPagination(auditSessionService,
                    searchParam, sortMapSession, "sessionOid", null);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }

    // *****************************************************
    // download records
    // *****************************************************
    public String saveDownload()
    {
        try
        {
            AuditTrailExHolder searchParam = (AuditTrailExHolder) getSession()
            .get(SESSION_KEY_SEARCH_PARAMETER_AUDIT_TRAIL);

            if (searchParam == null)
            {
                searchParam = new AuditTrailExHolder();
            }
            int count = auditTrailService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            List<AuditTrailHolder> list = auditTrailService.getListOfSummary(searchParam);
            
            Document doc = new Document();
            Element root = new Element("auditTrailReport");
            root.setAttribute("reportDate", DateUtil.getInstance().convertDateToString(new Date(), DATE_FORMAT ));
            doc.setRootElement(root);
            Element searchCriterion = new Element("searchCriterion");
            Element key = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_KEY);
            key.setAttribute(ATTR_SEARCH_NAME, ELEMENT_AUDIT_SEARCH_CRITERION_USER_TYPE);
            key.addContent(convertNullToEmpty(searchParam.getUserTypeOid()));
            searchCriterion.addContent(key);
            key = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_KEY);
            key.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_ACTION);
            key.addContent(convertNullToEmpty(searchParam.getActorAction()));
            searchCriterion.addContent(key);
            key = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_KEY);
            key.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_ACTION_DATE);
            key.setAttribute(ATTR_RECORD_HAS_SUBKEY, "true");
            Element subKey = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_SUB_KEY);
            subKey.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_ACTION_DATE_FROM);
            subKey.addContent(convertNullToEmpty(DateUtil.getInstance().convertDateToString(searchParam.getDateFrom(), DATE_FORMAT)));
            key.addContent(subKey);
            subKey = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_SUB_KEY);
            subKey.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_ACTION_DATE_TO);
            subKey.addContent(convertNullToEmpty(DateUtil.getInstance().convertDateToString(searchParam.getDateTo(), DATE_FORMAT)));
            key.addContent(subKey);
            searchCriterion.addContent(key);
            key = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_KEY);
            key.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_ACTOR);
            key.addContent(convertNullToEmpty(searchParam.getActor()));
            searchCriterion.addContent(key);
            key = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_KEY);
            key.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_COMPANY);
            key.addContent(convertNullToEmpty(searchParam.getCompany()));
            searchCriterion.addContent(key);
            key = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_KEY);
            key.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_ACCESS_IP);
            key.addContent(convertNullToEmpty(searchParam.getClientIp()));
            searchCriterion.addContent(key);
            key = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_KEY);
            key.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_SESSION_ID);
            key.addContent(convertNullToEmpty(searchParam.getSessionId()));
            searchCriterion.addContent(key);
            key = new Element(ELEMENT_AUDIT_SEARCH_CRITERION_KEY);
            key.setAttribute(ATTR_SEARCH_NAME, ATTR_RECORD_SEARCH_CRITERION_RECORD_TYPE);
            key.addContent(convertNullToEmpty(searchParam.getRecordType()));
            searchCriterion.addContent(key);
            root.addContent(searchCriterion);
            Element recordSet = new Element(ELEMENT_AUDIT_RECORD_SET);
            recordSet.setAttribute(ATTR_RECORD_TOTAL, String.valueOf(list.size()));
            root.addContent(recordSet);
            for(int i = 0; i < list.size(); i++)
            {
                recordSet.addContent(getRecord((AuditTrailExHolder)list.get(i), i+1));
            }
            this.setRptResult(outputDoc(doc));
            this.setRptFileName(REPORT_FILE_NAME+DateUtil.getInstance().getCurrentLogicTimeStamp()+".xml");
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        return SUCCESS;
    }
    
    
    private Element getRecord(AuditTrailExHolder obj, int number) throws Exception
    {
        Element record = new Element(ELEMENT_RECORD); 
        record.setAttribute(ATTR_RECORD_NUMBER, String.valueOf(number));
        Element field = new Element(ELEMENT_FIELD);
        field.setAttribute(ATTR_SEARCH_NAME, "auditOid");
        field.addContent(String.valueOf(obj.getAuditTrailOid()));
        record.addContent(field);
        field = new Element(ELEMENT_FIELD);
        field.setAttribute(ATTR_SEARCH_NAME, "sessionID");
        field.addContent(obj.getSessionId());
        record.addContent(field);
        field = new Element(ELEMENT_FIELD);
        field.setAttribute(ATTR_SEARCH_NAME, "userID(OID)");
        field.addContent(obj.getActor()+"("+obj.getActorOid()+")");
        record.addContent(field);
        field = new Element(ELEMENT_FIELD);
        field.setAttribute(ATTR_SEARCH_NAME, "timeStamp");
        field.addContent(DateUtil.getInstance().convertDateToString(obj.getActionDate(), DATE_FORMAT)+"");
        record.addContent(field);
        field = new Element(ELEMENT_FIELD);
        field.setAttribute(ATTR_SEARCH_NAME, "action");
        field.addContent(obj.getActorAction()+"");
        record.addContent(field);
        field = new Element(ELEMENT_FIELD);
        field.setAttribute(ATTR_SEARCH_NAME, "recordType");
        field.addContent(obj.getRecordType());
        record.addContent(field);
        field = new Element(ELEMENT_FIELD);
        field.setAttribute(ATTR_SEARCH_NAME, "recordStatus");
        field.addContent(obj.getRecordStatus()==null ? "" : obj.getRecordStatus().name());
        record.addContent(field);
        field = new Element(ELEMENT_FIELD);
        field.setAttribute(ATTR_SEARCH_NAME, "accessIP");
        field.addContent(obj.getClientIp());
        record.addContent(field);
        
        return record;
    }
    
    private InputStream outputDoc(Document doc) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            (new XMLOutputter(Format.getPrettyFormat())).output(doc, os);
            return new ByteArrayInputStream(os.toByteArray());
        }
        finally
        {
            os.close();
            os = null;
        }

    }
    
    
    public String checkDownload()
    {
        try
        {
            AuditTrailExHolder searchParam = (AuditTrailExHolder) getSession().get(SESSION_KEY_SEARCH_PARAMETER_AUDIT_TRAIL);
            List<AuditTrailHolder> list = auditTrailService.getListOfSummary(searchParam);
            if(list == null || list.isEmpty())
            {
                downloadFlag = "noResult";
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public void getXmlDocument()
    {
        try
        {
            AuditTrailHolder audit = auditTrailService.selectAuditTrailWithBlobsByKey(param.getAuditTrailOid());
            if(audit != null)
            {
                HttpServletResponse response = ServletActionContext.getResponse();
                response.setContentType("text/xml;charset=utf-8"); 
                response.setCharacterEncoding("UTF-8"); 
                response.setHeader("Cache-Control", "no-cache");   
                PrintWriter out=response.getWriter();  
                out.print(audit.getXmlContent());
                out.flush();
                out.close();
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
    }
    
    
    public String showXmlContent()
    {
        return SUCCESS;
    }


    // *****************************************************
    // handelException
    // *****************************************************
    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                new String[] { tickNo }));

        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

        msg.addMessageTarget(mt);
    }
    
    
    private String convertNullToEmpty(Object o)
    {
        return o == null ? "" : String.valueOf(o);
    }
    
    private void initAuditTrailQuery(AuditTrailExHolder trail)
    {
        String userTypeOid = String.valueOf(this.getProfileOfCurrentUser().getUserType());
        if("2".equals(userTypeOid))
        {
            trail.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        }
        if("3".equals(userTypeOid))
        {
            trail.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
        }
        if("4".equals(userTypeOid) || "5".equals(userTypeOid))
        {
            trail.setActorOid(this.getProfileOfCurrentUser().getUserOid());
        }
    }
    
    private void initAuditSessionQuery(AuditSessionExHolder session)
    {
        String userTypeOid = String.valueOf(this.getProfileOfCurrentUser().getUserType());
        if("2".equals(userTypeOid))
        {
            session.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        }
        if("3".equals(userTypeOid))
        {
            session.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
        }
        if("4".equals(userTypeOid) || "5".equals(userTypeOid))
        {
            session.setUserOid(this.getProfileOfCurrentUser().getUserOid());
        }
    }
    
    
    private Map<String, String> initRecordTypes()
    {
        Map<String, String> rlt = new TreeMap<String, String>();
        rlt.put("Buyer", "Buyer");
        rlt.put("Credit Claim", "CcHeader");
        rlt.put("Control Parameter", "ControlParameter");
        rlt.put("Debit Note", "Dn");
        rlt.put("Debit Note Header", "DnHeader");
        rlt.put("Goods Receipt Note", "Grn");
        rlt.put("Group", "Group");
        rlt.put("Invoice", "Inv");
        rlt.put("Invoice Header", "InvHeader");
        rlt.put("Job Control", "Job");
        rlt.put("Msg Transactions", "MsgTransactions");
        rlt.put("PasswordHistory", "PasswordHistory");
        rlt.put("PO", "Po");
        rlt.put("PO Header", "PoHeader");
        rlt.put("PoInvGrnDnMatching", "PoInvGrnDnMatching");
        rlt.put("PoInvGrnDnMatchingDetail", "PoInvGrnDnMatchingDetail");
        rlt.put("ResetPasswordRequestHistory", "ResetPasswordRequestHistory");
        rlt.put("Role", "Role");
        rlt.put("Summary Field", "SummaryField");
        rlt.put("User Profile", "UserProfile");
        rlt.put("Supplier", "Supplier");
        rlt.put("Trading Partner", "TradingPartner");
        rlt.put("Supplier Group", "SupplierSet");
        rlt.put("ItemMaster", "ItemMaster");
        
        return rlt;
    }
    

    //*****************************************************
    // getter and setter
    //*****************************************************
    
    public List<UserTypeHolder> getUserTypes()
    {
        return userTypes;
    }


    public void setUserTypes(List<UserTypeHolder> userTypes)
    {
        this.userTypes = userTypes;
    }


    public Map<String, String> getActorActions()
    {
        return actorActions;
    }


    public void setActorActions(Map<String, String> actorActions)
    {
        this.actorActions = actorActions;
    }


    public AuditSessionExHolder getAuditSession()
    {
        return auditSession;
    }


    public void setAuditSession(AuditSessionExHolder auditSession)
    {
        this.auditSession = auditSession;
    }


    public AuditTrailExHolder getParam()
    {
        return param;
    }


    public void setParam(AuditTrailExHolder param)
    {
        this.param = param;
    }


    public InputStream getRptResult()
    {
        return rptResult;
    }


    public void setRptResult(InputStream rptResult)
    {
        this.rptResult = rptResult;
    }


    public String getRptFileName()
    {
        return rptFileName;
    }


    public void setRptFileName(String rptFileName)
    {
        this.rptFileName = rptFileName;
    }


    public String getDownloadFlag()
    {
        return downloadFlag;
    }


    public void setDownloadFlag(String downloadFlag)
    {
        this.downloadFlag = downloadFlag;
    }
    

    public Map<String, String> getRecordTypes()
    {
        return recordTypes;
    }
    
}
