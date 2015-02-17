package com.pracbiz.b2bportal.core.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.EmailAuthMechanisms;
import com.pracbiz.b2bportal.core.constants.EmailConnectType;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.holder.BuyerGivenSupplierOperationHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerOperationHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.OperationHolder;
import com.pracbiz.b2bportal.core.holder.ReserveMessageHolder;
import com.pracbiz.b2bportal.core.holder.SummaryFieldHolder;
import com.pracbiz.b2bportal.core.holder.SummaryPageSettingHolder;
import com.pracbiz.b2bportal.core.holder.ToolTipHolder;
import com.pracbiz.b2bportal.core.holder.extension.SummaryFieldExHolder;
import com.pracbiz.b2bportal.core.holder.extension.ToolTipExHolder;
import com.pracbiz.b2bportal.core.service.BuyerGivenSupplierOperationService;
import com.pracbiz.b2bportal.core.service.BuyerOperationService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.OperationService;
import com.pracbiz.b2bportal.core.service.ReserveMessageService;
import com.pracbiz.b2bportal.core.service.SummaryFieldService;
import com.pracbiz.b2bportal.core.service.SummaryPageSettingService;
import com.pracbiz.b2bportal.core.service.ToolTipService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class AdministrationAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(AdministrationAction.class);
    private static final long serialVersionUID = -2597273750132365802L;
    private static final String CTRL_PARAMETER_EMAIL_CONFIG = "EMAIL_CON"; 
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    private static final String CTRL_PARAMETER_PWD_RULE = "PWD_RULE";
    private static final String PARAM_ID_OUTBOUND = "OUTBOUND";
    private static final String PARAM_ID_INBOUND = "INBOUND";
    private static final String SESSION_OUTBOUND = "SESSION_OUTBOUND";
    private static final String SESSION_INBOUND = "SESSION_INBOUND";
    private static final String SESSION_KEY_SEARCH_PARAMETER_MSG_MANAGEMENT = "SESSION_KEY_SEARCH_PARAMETER_MSG_MANAGEMENT";
    private static final String SESSION_OID_PARAMETER = "session.parameter.AdministrationAction.selectedOids";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String REQUEST_OID_DELIMITER = "\\-";
    private static final Map<String, String> sortMap;
    
    private static final String APP_URL = "appUrl";
    private static final String MAIL_TO = "Mail To :::: [";
    private static final String MAIL_SUBJECT = "Mail Subject :::: [";
    private static final String MAIL_SENDER = "[With Sender :::: [";
    private static final String MAIL_CONTENT = "[Mail Content :::: [";
    private static final String NEW_LINE_WITH_BRACKET = "],\n";
    
    private static final String MAIL_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
    private static final String MAIL_SMTP_SOCKETFACTORY_CLASS_FALLBACK = "mail.smtp.socketFactory.fallback";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_EHLO = "mail.smtp.ehlo";
    private static final String MAIL_SMTP_AUTH_MECHANISMS = "mail.smtp.auth.mechanisms";
    private static final String MAIL_SMTP_STARTTLES_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_STARTTLES_REQUIRED = "mail.smtp.starttls.required";
    private static final String PROPERTITY_VALUE_TRUE = "true";
    private static final String MAIL_TEST_SUBJECT = "Test alert message for Email Config";
    private static final String MAIL_SMTP_CON_TIMEOUT = "mail.smtp.connectiontimeout";
    private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    private static final String TIMEOUT_MILLSECONDS = "30000";

    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("rmOid", "RM_OID");
        sortMap.put("title", "TITLE");
        sortMap.put("validFrom", "VALID_FROM");
        sortMap.put("validTo", "VALID_TO");
        sortMap.put("createBy", "CREATE_BY");
        sortMap.put("updateDate", "UPDATE_DATE");
        sortMap.put("announcementType", "ANNOUNCEMENT_TYPE");
    }

    @Autowired
    private transient ControlParameterService controlParameterService;
    @Autowired
    private transient ReserveMessageService reserveMessageService;
    @Autowired
    private transient OidService oidService;
    @Autowired
    private transient SummaryPageSettingService summaryPageSettingService;
    @Autowired
    private transient SummaryFieldService summaryFieldService;
    @Autowired
    private transient ToolTipService toolTipService;
    @Autowired
    private transient BuyerService buyerService;
    @Autowired
    private transient OperationService operationService;
    @Autowired
    private transient BuyerOperationService buyerOperationService;
    @Autowired
    private transient BuyerGivenSupplierOperationService buyerGivenSupplierOperationService;
    @Autowired
    private transient VelocityEngine velocityEngine;
    private transient JavaMailSenderImpl mailSender;

    // ajax result
    private List<String> result;
    private Map<String, ControlParameterHolder> reset;
    private Map<String, List<ControlParameterHolder>> resetHseKeep;

    // common configuration
    private ControlParameterHolder pwdExpPeri;
    private ControlParameterHolder emailExpPeri;
    private ControlParameterHolder maxAttLogin;
    private ControlParameterHolder helpDeskNo;
    private ControlParameterHolder helpDeskEmail;
    private ControlParameterHolder reqInterval;
    private ControlParameterHolder repeatedLogin;
    private ControlParameterHolder makerChecker;
    private ControlParameterHolder autoLoginOut;
    private ControlParameterHolder logFileName;
    private ControlParameterHolder logFilePath;
    private ControlParameterHolder maxUserAmountForSupplier;
    private ControlParameterHolder defaultPageSize;
    private ControlParameterHolder pageSizes;
    private ControlParameterHolder documentWindowForBuyer;
    private ControlParameterHolder documentWindowForSupplier;
    private ControlParameterHolder currentGst;
    private ControlParameterHolder newGst;
    private ControlParameterHolder newGstFromDate;
    private ControlParameterHolder maxPdfCount;
    private ControlParameterHolder maxExcelCount;
    private ControlParameterHolder maxSummaryExcelCount;
    private ControlParameterHolder maxDayOfReport;
    private ControlParameterHolder fileSizeLimit;
    

    // house keeping
    private List<ControlParameterHolder> outbounds;
    private List<ControlParameterHolder> inbounds;

    // password rule
    private ControlParameterHolder maxLength;
    private ControlParameterHolder minLength;
    private ControlParameterHolder alphanumeric;
    private ControlParameterHolder noRepeat;
    private ControlParameterHolder mixtureCase;
    private ControlParameterHolder specialCharacter;
    private ControlParameterHolder notDictWord;
    private ControlParameterHolder notRepeatForPwdHis;
    private ControlParameterHolder notEqualLoginId;

    // message management
    private ReserveMessageHolder reserveMessage;
    private ReserveMessageHolder createResMsg;
    private ReserveMessageHolder editResMsg;

    // messgae summary page setting
    private Map<String, String> pages;
    private Map<String, List<SummaryFieldExHolder>> fields;
    private SummaryPageSettingHolder summaryPageSetting;
    private String jsonStr;

    //edit buyer and supplier operation
    private BigDecimal buyerOid;
    private List<BuyerHolder> buyers;
    private List<OperationHolder> buyerOperations;
    private List<OperationHolder> buyerSelectedOperations;
    private List<OperationHolder> supplierOperations;
    private List<OperationHolder> supplierSelectedOperations;
    private List<String> buyerSelectedOperationOids;
    private List<String> supplierSelectedOperationOids;
    
    // email configuration
    private ControlParameterHolder smtpHost;
    private ControlParameterHolder smtpPort;
    private ControlParameterHolder smtpUser;
    private ControlParameterHolder smtpPassword ;
    private ControlParameterHolder smtpProtocol;
    private ControlParameterHolder needAuth;
    private ControlParameterHolder needEhlo;
    private ControlParameterHolder connectType;
    private ControlParameterHolder socketFacClass;
    private ControlParameterHolder socketFacFallback;
    private ControlParameterHolder senderName;
    private ControlParameterHolder senderAddr;
    private ControlParameterHolder replyToAddr;
    private ControlParameterHolder adminAddr;
    private ControlParameterHolder authMechanisms;
    private Map<String, String> mechanisms;
    private Map<String, String> connectTypes;
    
    public String init()
    {
        return SUCCESS;
    }


    // *****************************************************
    // edit common configuration
    // *****************************************************
    public String initCommonFig()
    {
        try
        {
            reset = new HashMap<String, ControlParameterHolder>();
            pwdExpPeri = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "PWD_EXPIRE_DAYS");
            emailExpPeri = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "EMAIL_EXPIRE_PERIOD");
            maxAttLogin = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "MAX_ATTEMPT_LOGIN");
            helpDeskNo = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "HELPDESK_NO");
            helpDeskEmail = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "HELPDESK_EMAIL");
            reqInterval = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "REQ_INVL");
            repeatedLogin = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "REPEATED_LOGIN");
            logFileName = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "LOG_FILE_NAME");
            logFilePath = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "LOG_FILE_PATH");
            makerChecker = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "MAKER_CHECKER");
            autoLoginOut = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "AUTO_LOGOUT");
            maxUserAmountForSupplier = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_CTRL, "MAX_USER_AMOUNT_FOR_SUPPLIER");
            defaultPageSize = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_CTRL, "DEFAULT_PAGE_SIZE");
            pageSizes = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_CTRL, "PAGE_SIZES");
            documentWindowForBuyer = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
            documentWindowForSupplier = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
            maxPdfCount = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "MAX_PDF_COUNT");
            maxExcelCount = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "MAX_EXCEL_COUNT");
            maxSummaryExcelCount = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "MAX_SUMMARY_EXCEL_COUNT");
            currentGst = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "CURRENT_GST");
            newGst = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "NEW_GST");
            newGstFromDate = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "NEW_GST_FROM_DATE");
            maxDayOfReport = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "MAX_DAY_OF_REPORT");
            fileSizeLimit = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "ZIP_FILE_SIZE_LIMIT");
            
            reset.put("pwdExpPeri", pwdExpPeri);
            reset.put("emailExpPeri", emailExpPeri);
            reset.put("maxAttLogin", maxAttLogin);
            reset.put("helpDeskNo", helpDeskNo);
            reset.put("helpDeskEmail", helpDeskEmail);
            reset.put("reqInterval", reqInterval);
            reset.put("repeatedLogin", repeatedLogin);
            reset.put("logFileName", logFileName);
            reset.put("logFilePath", logFilePath);
            reset.put("makerChecker", makerChecker);
            reset.put("autoLoginOut", autoLoginOut);
            reset.put("maxUserAmountForSupplier", maxUserAmountForSupplier);
            reset.put("defaultPageSize", defaultPageSize);
            reset.put("pageSizes", pageSizes);
            reset.put("documentWindowForBuyer", documentWindowForBuyer);
            reset.put("documentWindowForSupplier", documentWindowForSupplier);
            reset.put("maxPdfCount", maxPdfCount);
            reset.put("maxExcelCount", maxExcelCount);
            reset.put("maxSummaryExcelCount", maxSummaryExcelCount);
            reset.put("currentGst", currentGst);
            reset.put("newGst", newGst);
            reset.put("newGstFromDate", newGstFromDate);
            reset.put("maxDayOfReport", maxDayOfReport);
            reset.put("fileSizeLimit", fileSizeLimit);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String saveCommonFig()
    {
        try
        {
            reset = new HashMap<String, ControlParameterHolder>();
            log.info(this.getText("B2BPA0118", new String[] { this
                    .getLoginIdOfCurrentUser() }));
            result = new ArrayList<String>();
            if (!(new File(logFilePath.getStringValue())).isDirectory())
            {
                result.add(this.getText("B2BPA0154"));
                return SUCCESS;
            }
            this.updateControlParameter(pwdExpPeri, CTRL_PARAMETER_CTRL,
                    "PWD_EXPIRE_DAYS");
            this.updateControlParameter(emailExpPeri, CTRL_PARAMETER_CTRL,
                    "EMAIL_EXPIRE_PERIOD");
            this.updateControlParameter(maxAttLogin, CTRL_PARAMETER_CTRL,
                    "MAX_ATTEMPT_LOGIN");
            this.updateControlParameter(helpDeskNo, CTRL_PARAMETER_CTRL,
                    "HELPDESK_NO");
            this.updateControlParameter(helpDeskEmail, CTRL_PARAMETER_CTRL,
                    "HELPDESK_EMAIL");
            this.updateControlParameter(reqInterval, CTRL_PARAMETER_CTRL,
                    "REQ_INVL");
            this.updateControlParameter(repeatedLogin, CTRL_PARAMETER_CTRL,
                    "REPEATED_LOGIN");
            this.updateControlParameter(makerChecker, CTRL_PARAMETER_CTRL,
                    "MAKER_CHECKER");
            this.updateControlParameter(autoLoginOut, CTRL_PARAMETER_CTRL,
                    "AUTO_LOGOUT");
            this.updateControlParameter(logFileName, CTRL_PARAMETER_CTRL,
                    "LOG_FILE_NAME");
            this.updateControlParameter(logFilePath, CTRL_PARAMETER_CTRL,
                    "LOG_FILE_PATH");
            this.updateControlParameter(maxUserAmountForSupplier, CTRL_PARAMETER_CTRL,
                    "MAX_USER_AMOUNT_FOR_SUPPLIER");
            this.updateControlParameter(defaultPageSize, CTRL_PARAMETER_CTRL,
                    "DEFAULT_PAGE_SIZE");
            this.updateControlParameter(pageSizes, CTRL_PARAMETER_CTRL,
                    "PAGE_SIZES");
            this.updateControlParameter(documentWindowForBuyer, CTRL_PARAMETER_CTRL,
                    "DOCUMENT_WINDOW_BUYER");
            this.updateControlParameter(documentWindowForSupplier, CTRL_PARAMETER_CTRL,
                    "DOCUMENT_WINDOW_SUPPLIER");
            this.updateControlParameter(maxPdfCount, CTRL_PARAMETER_CTRL,
                    "MAX_PDF_COUNT");
            this.updateControlParameter(maxExcelCount, CTRL_PARAMETER_CTRL,
                    "MAX_EXCEL_COUNT");
            this.updateControlParameter(maxSummaryExcelCount, CTRL_PARAMETER_CTRL,
                    "MAX_SUMMARY_EXCEL_COUNT");
            this.updateControlParameter(currentGst, CTRL_PARAMETER_CTRL,
                    "CURRENT_GST");
            this.updateControlParameter(newGst, CTRL_PARAMETER_CTRL,
                    "NEW_GST");
            this.updateControlParameter(newGstFromDate, CTRL_PARAMETER_CTRL,
                    "NEW_GST_FROM_DATE");
            this.updateControlParameter(maxDayOfReport, CTRL_PARAMETER_CTRL,
                    "MAX_DAY_OF_REPORT");
            this.updateControlParameter(fileSizeLimit, CTRL_PARAMETER_CTRL,
                    "ZIP_FILE_SIZE_LIMIT");
            result.add(this.getText("B2BPA0139"));
            log.info(this.getText("B2BPA0119", new String[] { this
                    .getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0140"));
        }
        return SUCCESS;
    }


    // *****************************************************
    // edit house keeping
    // *****************************************************
    public String initHouseKeeping()
    {
        try
        {
            resetHseKeep = new HashMap<String, List<ControlParameterHolder>>();
            outbounds = controlParameterService
                    .selectControlParametersByCatId(PARAM_ID_OUTBOUND);
            inbounds = controlParameterService
                    .selectControlParametersByCatId(PARAM_ID_INBOUND);
            resetHseKeep.put("outbounds", outbounds);
            resetHseKeep.put("inbounds", inbounds);
            this.getSession().put(SESSION_OUTBOUND, outbounds);
            this.getSession().put(SESSION_INBOUND, inbounds);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        return SUCCESS;
    }


    @SuppressWarnings("unchecked")
    public String saveHouseKeeping()
    {
        try
        {
            log.info(this.getText("B2BPA0122", new String[] { this
                    .getLoginIdOfCurrentUser() }));
            result = new ArrayList<String>();
            List<ControlParameterHolder> sessionOutbounds = (List<ControlParameterHolder>) this
                    .getSession().get(SESSION_OUTBOUND);
            List<ControlParameterHolder> sessionInbounds = (List<ControlParameterHolder>) this
                    .getSession().get(SESSION_INBOUND);
            for (int i = 0; i < outbounds.size(); i++)
            {
                ControlParameterHolder obj = outbounds.get(i);
                obj.setParamOid(sessionOutbounds.get(i).getParamOid());
                ControlParameterHolder old = controlParameterService
                        .selectControlParameterByKey(obj.getParamOid());
                controlParameterService.auditUpdateByPrimaryKeySelective(this
                        .getCommonParameter(), old, obj);
            }
            for (int i = 0; i < inbounds.size(); i++)
            {
                ControlParameterHolder obj = inbounds.get(i);
                obj.setParamOid(sessionInbounds.get(i).getParamOid());
                ControlParameterHolder old = controlParameterService
                        .selectControlParameterByKey(obj.getParamOid());
                controlParameterService.auditUpdateByPrimaryKeySelective(this
                        .getCommonParameter(), old, obj);
            }
            result.add(this.getText("B2BPA0141"));
            log.info(this.getText("B2BPA0123", new String[] { this
                    .getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0142"));
        }
        return SUCCESS;
    }


    // *****************************************************
    // edit password rule
    // *****************************************************
    public String initPasswordRule()
    {
        try
        {
            reset = new HashMap<String, ControlParameterHolder>();
            maxLength = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE, "PWD_MAX_LENGTH");
            minLength = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE, "PWD_MIN_LENGTH");
            alphanumeric = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE, "ALPHANUMERIC");
            noRepeat = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE, "NO_REPEATED_CHARACTER");
            mixtureCase = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE, "MIXTURE_CASE");
            specialCharacter = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE, "SPECIAL_CHARACTER");
            notDictWord = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE, "NOT_IN_DICT_WORD");
            notRepeatForPwdHis = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE,
                            "NOT_REPEAT_FOR_PWD_CHANGE");
            notEqualLoginId = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_PWD_RULE, "NOT_EQUAL_LOGIN_ID");
            reset.put("maxLength", maxLength);
            reset.put("minLength", minLength);
            reset.put("alphanumeric", alphanumeric);
            reset.put("noRepeat", noRepeat);
            reset.put("mixtureCase", mixtureCase);
            reset.put("specialCharacter", specialCharacter);
            reset.put("notDictWord", notDictWord);
            reset.put("notRepeatForPwdHis", notRepeatForPwdHis);
            reset.put("notEqualLoginId", notEqualLoginId);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String savePasswordRule()
    {
        try
        {
            log.info(this.getText("B2BPA0120", new String[] { this
                    .getLoginIdOfCurrentUser() }));
            result = new ArrayList<String>();
            this.updateControlParameter(maxLength, CTRL_PARAMETER_PWD_RULE,
                    "PWD_MAX_LENGTH");
            this.updateControlParameter(minLength, CTRL_PARAMETER_PWD_RULE,
                    "PWD_MIN_LENGTH");
            this.updateControlParameter(alphanumeric, CTRL_PARAMETER_PWD_RULE,
                    "ALPHANUMERIC");
            this.updateControlParameter(noRepeat, CTRL_PARAMETER_PWD_RULE,
                    "NO_REPEATED_CHARACTER");
            this.updateControlParameter(mixtureCase, CTRL_PARAMETER_PWD_RULE,
                    "MIXTURE_CASE");
            this.updateControlParameter(specialCharacter,
                    CTRL_PARAMETER_PWD_RULE, "SPECIAL_CHARACTER");
            this.updateControlParameter(notDictWord, CTRL_PARAMETER_PWD_RULE,
                    "NOT_IN_DICT_WORD");
            this.updateControlParameter(notRepeatForPwdHis,
                    CTRL_PARAMETER_PWD_RULE, "NOT_REPEAT_FOR_PWD_CHANGE");
            this.updateControlParameter(notEqualLoginId,
                    CTRL_PARAMETER_PWD_RULE, "NOT_EQUAL_LOGIN_ID");
            result.add(this.getText("B2BPA0143"));
            log.info(this.getText("B2BPA0121", new String[] { this
                    .getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0144"));
        }
        return SUCCESS;
    }


    // *****************************************************
    // message management summary
    // *****************************************************
    public String initMessageManagement()
    {
        return SUCCESS;
    }


    public String data()
    {
        try
        {
            ReserveMessageHolder searchParam = (ReserveMessageHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_MSG_MANAGEMENT);
            if (searchParam == null)
            {
                searchParam = new ReserveMessageHolder();
            }
            this.obtainListRecordsOfPagination(reserveMessageService,
                    searchParam, sortMap, "rsrvMsgOid", null);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }


    public String search()
    {
        if (null == reserveMessage)
        {
            reserveMessage = new ReserveMessageHolder();
        }
        try
        {
            reserveMessage.trimAllString();
            reserveMessage.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_MSG_MANAGEMENT,
                reserveMessage);

        return SUCCESS;
    }


    // *****************************************************
    // init view or edit reserve message
    // *****************************************************
    public String initViewOrEdit()
    {
        try
        {
            if (reserveMessage != null
                    && reserveMessage.getRsrvMsgOid() != null)
            {
                reserveMessage = reserveMessageService
                        .selectReserveMessageByKey(reserveMessage
                                .getRsrvMsgOid());
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        return SUCCESS;
    }


    // *****************************************************
    // create new reserve message
    // *****************************************************
    public String saveAddResMsg()
    {
        try
        {
            log.info(this.getText("B2BPA0124", new String[] { this
                    .getLoginIdOfCurrentUser() }));
            result = new ArrayList<String>();
            if (createResMsg != null)
            {
                createResMsg.setRsrvMsgOid(oidService.getOid());
                createResMsg.setCreateBy(this.getLoginIdOfCurrentUser());
                createResMsg.setCreateDate(new java.util.Date());
                reserveMessageService.auditInsert(this.getCommonParameter(),
                        createResMsg);
                result.add("Reserve Announcement [" + createResMsg.getTitle()
                        + "] has been created successfully.");
            }
            log.info(this.getText("B2BPA0125", new String[] { this
                    .getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0145"));
        }
        return SUCCESS;
    }


    // *****************************************************
    // edit message management
    // *****************************************************
    public String saveEditResMsg()
    {
        try
        {
            log.info(this.getText("B2BPA0129", new String[] { this
                    .getLoginIdOfCurrentUser() }));
            result = new ArrayList<String>();
            if (editResMsg != null)
            {
                editResMsg.setUpdateBy(this.getLoginIdOfCurrentUser());
                editResMsg.setUpdateDate(new java.util.Date());
                ReserveMessageHolder oldObj = reserveMessageService
                        .selectReserveMessageByKey(editResMsg.getRsrvMsgOid());
                reserveMessageService.auditUpdateByPrimaryKeySelective(this
                        .getCommonParameter(), oldObj, editResMsg);
                result.add(this.getText("B2BPA0151", new String[] { editResMsg
                        .getTitle() }));
            }
            log.info(this.getText("B2BPA0130", new String[] { this
                    .getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0146"));
        }
        return SUCCESS;
    }


    // *****************************************************
    // delete function of message management
    // *****************************************************
    public String saveDelete()
    {
        try
        {
            log.info(this.getText("B2BPA0127", new String[] { this
                    .getLoginIdOfCurrentUser() }));
            result = new ArrayList<String>();
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }

            this.getSession().remove(SESSION_OID_PARAMETER);

            String[] parts = selectedOids.toString().split(
                    REQUEST_OID_DELIMITER);

            for (String part : parts)
            {
                BigDecimal rsrvMsgOid = new BigDecimal(part);
                ReserveMessageHolder obj = reserveMessageService
                        .selectReserveMessageByKey(rsrvMsgOid);
                if (obj == null)
                {
                    result.add(this.getText("B2BPA0149",
                            new String[] { rsrvMsgOid.toString() }));
                }
                else
                {
                    reserveMessageService.auditDelete(
                            this.getCommonParameter(), obj);
                    result.add(this.getText("B2BPA0148", new String[] { obj
                            .getTitle() }));
                }

            }
            log.info(this.getText("B2BPA0128", new String[] { this
                    .getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0147"));
        }
        return SUCCESS;
    }


    public String putParamIntoSession()
    {
        this.getSession().put(SESSION_OID_PARAMETER,
                this.getRequest().getParameter(REQUEST_PARAMTER_OID));

        return SUCCESS;
    }


    private void updateControlParameter(
            ControlParameterHolder controlParameter, String sectId,
            String paramId) throws Exception
    {
        if (controlParameter != null)
        {
            controlParameter.setUpdateBy(this.getLoginIdOfCurrentUser());
            controlParameter.setUpdateDate(new java.util.Date());
            controlParameter.setSectId(sectId);
            controlParameter.setParamId(paramId);
            ControlParameterHolder oldObj = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(sectId,
                            paramId);
            controlParameter.setParamOid(oldObj.getParamOid());
            
            controlParameterService.auditUpdateByPrimaryKeySelective(this
                    .getCommonParameter(), oldObj, controlParameter);
        }
    }


    // *****************************************************
    // MsgSummaryPageSetting
    // *****************************************************
    public String initMsgSummaryPageSetting()
    {
        try
        {
            Map<String, String> pagesMap = PageId.toMapValue(this);
            List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String,String>>(pagesMap.entrySet());
            
            Collections.sort(list, new Comparator<Entry<String, String>>() 
    		{
    			public int compare(Entry<String, String > s1, Entry<String, String> s2)
    			{
    				return s1.getValue().compareTo(s2.getValue());
    			}
    		});
            
            pages = new LinkedHashMap<String, String>();
            for (Map.Entry<String, String> entry : list)
            {
            	pages.put(entry.getKey(), entry.getValue());
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String getSummaryFields()
    {
        try
        {
            fields = new HashMap<String, List<SummaryFieldExHolder>>();
            List<SummaryPageSettingHolder> settings = summaryPageSettingService
                    .selectByPageId(summaryPageSetting.getPageId());
            Iterator<SummaryPageSettingHolder> it = settings.iterator();
            List<ToolTipHolder> addToolTips = toolTipService.select(new ToolTipHolder());
            while (it.hasNext())
            {
                SummaryPageSettingHolder setting = it.next();
                BigDecimal settingOid = setting.getSettingOid();
                List<SummaryFieldHolder> fs = summaryFieldService
                        .selectBySettingOid(settingOid);
                if (fs == null)
                {
                    fs = new ArrayList<SummaryFieldHolder>();
                }
                List<ToolTipExHolder> allToolTips = getAllToolTipsByFileds(fs);
                List<SummaryFieldExHolder> list = new ArrayList<SummaryFieldExHolder>();
                Iterator<SummaryFieldHolder> fit = fs.iterator();
                while (fit.hasNext())
                {
                    SummaryFieldExHolder field = new SummaryFieldExHolder();
                    BeanUtils.copyProperties(fit.next(), field);
                    field.setFieldLabel(this.getText(field.getFieldLabelKey()));
                    this.setAddToolTips(addToolTips, field);
                    field.calculateOtherToolTips(allToolTips, this);
                    list.add(field);
                }
                
                if ("B".equals(setting.getAccessType()))
                {
                    fields.put("B", list);
                }
                if ("S".equals(setting.getAccessType()))
                {
                    fields.put("S", list);
                }
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    private void setAddToolTips(List<ToolTipHolder> toolTips,SummaryFieldExHolder field)
    {
        if (toolTips == null)
        {
            return;
        }
        List<ToolTipExHolder> toolTipExs = new ArrayList<ToolTipExHolder>();
        for (ToolTipHolder obj : toolTips)
        {
            if(obj.getFieldOid().compareTo(field.getFieldOid()) == 0)
            {
                ToolTipExHolder toolTipEx = new ToolTipExHolder();
                BeanUtils.copyProperties(obj, toolTipEx);
                toolTipExs.add(toolTipEx);
            }
        }
        field.setSelectedToolTips(toolTipExs);
    }
    
    private List<ToolTipExHolder> getAllToolTipsByFileds(List<SummaryFieldHolder> fields)
    {
        if(fields == null || fields.isEmpty())
        {
            return null;
        }
        List<ToolTipExHolder> list = new ArrayList<ToolTipExHolder>();
        for(SummaryFieldHolder field : fields)
        {
            ToolTipExHolder obj = new ToolTipExHolder();
            obj.setTooltipFieldOid(field.getFieldOid());
            obj.setTooltipFieldLabel(this.getText(field.getFieldLabelKey()));
            list.add(obj);
        }
        return list;
    }


    public String saveMsgSummaryPageSetting()
    {
        try
        {
            log.info(this.getText("B2BPA0158", new String[] { this
                    .getLoginIdOfCurrentUser() }));
            JSONArray array = JSONArray.fromObject(jsonStr);
            result = new ArrayList<String>();
            for (int i = 0; i < array.size(); i++)
            {
                SummaryFieldExHolder field = (SummaryFieldExHolder) JSONObject
                        .toBean((JSONObject) array.get(i),
                                SummaryFieldExHolder.class);
                SummaryFieldHolder oldField = summaryFieldService
                        .selectByKey(field.getFieldOid());
                List<ToolTipHolder> oldTips = toolTipService
                        .selectByFieldOid(field.getFieldOid());
                oldField.setToolTips(oldTips);
                SummaryFieldHolder newField = new SummaryFieldHolder();
                BeanUtils.copyProperties(oldField, newField);
                newField.setAvailable(field.getAvailable());
                newField.setFieldWidth(field.getFieldWidth());
                newField.setSortable(field.getSortable());
                newField.setShowOrder(field.getShowOrder());
                List<ToolTipHolder> newTips = new ArrayList<ToolTipHolder>();
                for(int j = 0; j < field.getToolTips().size(); j++)
                {
                    
                    ToolTipHolder obj = (ToolTipHolder) JSONObject
                    .toBean((JSONObject) JSONObject.fromObject(field.getToolTips().get(j)), 
                            ToolTipHolder.class);
                    newTips.add(obj);
                }
                newField.setToolTips(newTips);
                summaryFieldService.updateWithToolTip(
                        this.getCommonParameter(), oldField, newField);
            }
            result.add(this.getText("B2BPA0160"));
            log.info(this.getText("B2BPA0159", new String[] { this
                    .getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0161"));
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // edit common configuration
    // *****************************************************
    public String initEditRetailerPrivilege() throws Exception
    {
        try
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.ONE))
            {
                // If current user is type of system admin
                buyers = buyerService.select(new BuyerHolder());
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String getPrivilegeDetail() throws Exception
    {
        try
        {
            List<BigDecimal> buyerUserTypes = new ArrayList<BigDecimal>();
            buyerUserTypes.add(BigDecimal.valueOf(2));
            buyerUserTypes.add(BigDecimal.valueOf(4));
            buyerOperations = operationService
                    .selectOperationByUserTypes(buyerUserTypes);
            buyerSelectedOperations = refactorBuyerOperations(buyerOperations,
                    buyerOperationService.selectByBuyerOid(buyerOid));
            List<BigDecimal> supplierUserTypes = new ArrayList<BigDecimal>();
            supplierUserTypes.add(BigDecimal.valueOf(3));
            supplierUserTypes.add(BigDecimal.valueOf(5));
            supplierOperations = operationService
                    .selectOperationByUserTypes(supplierUserTypes);
            supplierSelectedOperations = refactorBuyerGivenSupplierOperations(
                    supplierOperations, buyerGivenSupplierOperationService
                            .selectByBuyerOid(buyerOid));
            
            Comparator<OperationHolder> comp = new Comparator<OperationHolder>()
            {
                @Override
                public int compare(OperationHolder o1, OperationHolder o2)
                {
                    return o1.getOpnDesc().compareTo(o2.getOpnDesc());
                }
            };
            
            Collections.sort(buyerOperations, comp);
            Collections.sort(buyerSelectedOperations, comp);
            Collections.sort(supplierOperations, comp);
            Collections.sort(supplierSelectedOperations, comp);
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String savePrivilege() throws Exception
    {
        try
        {
            log
                    .info(this.getText("B2BPA0165", new String[] {
                            this.getLoginIdOfCurrentUser(),
                            String.valueOf(buyerOid) }));
            result = new ArrayList<String>();
            BuyerHolder oldBuyer = buyerService.selectBuyerByKey(buyerOid);
            BuyerHolder newBuyer = new BuyerHolder();
            BeanUtils.copyProperties(oldBuyer, newBuyer);

            if (buyerSelectedOperationOids == null)
            {
                buyerSelectedOperationOids = new ArrayList<String>();
            }
            List<BuyerOperationHolder> oldBuyerOperations = buyerOperationService
                    .selectByBuyerOid(buyerOid);
            List<BuyerOperationHolder> newBuyerOperations = new ArrayList<BuyerOperationHolder>();
            for (String opnId : buyerSelectedOperationOids)
            {
                BuyerOperationHolder obj = new BuyerOperationHolder();
                obj.setBuyerOid(buyerOid);
                obj.setOpnId(opnId);
                newBuyerOperations.add(obj);
            }
            oldBuyer.setBuyerOperations(oldBuyerOperations);
            newBuyer.setBuyerOperations(newBuyerOperations);
            buyerService.updateBuyerWithBuyerOperation(this
                    .getCommonParameter(), oldBuyer, newBuyer);

            if (supplierSelectedOperationOids == null)
            {
                supplierSelectedOperationOids = new ArrayList<String>();
            }
            oldBuyer.setBuyerOperations(null);
            newBuyer.setBuyerOperations(null);
            List<BuyerGivenSupplierOperationHolder> oldBuyerGivenSupplierOperations = buyerGivenSupplierOperationService
                    .selectByBuyerOid(buyerOid);
            List<BuyerGivenSupplierOperationHolder> newBuyerGivenSupplierOperations = new ArrayList<BuyerGivenSupplierOperationHolder>();
            for (String opnId : supplierSelectedOperationOids)
            {
                BuyerGivenSupplierOperationHolder obj = new BuyerGivenSupplierOperationHolder();
                obj.setBuyerOid(buyerOid);
                obj.setOpnId(opnId);
                newBuyerGivenSupplierOperations.add(obj);
            }
            oldBuyer
                    .setBuyerGivenSupplierOperations(oldBuyerGivenSupplierOperations);
            newBuyer
                    .setBuyerGivenSupplierOperations(newBuyerGivenSupplierOperations);
            buyerService.updateBuyerWithBuyerGivenSupplierOperation(this
                    .getCommonParameter(), oldBuyer, newBuyer);
            result.add(this.getText("B2BPA0167"));
            log
                    .info(this.getText("B2BPA0166", new String[] {
                            this.getLoginIdOfCurrentUser(),
                            String.valueOf(buyerOid) }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0168"));
        }
        return SUCCESS;
    }


    private List<OperationHolder> refactorBuyerOperations(
            List<OperationHolder> all, List<BuyerOperationHolder> selected)
    {
        List<OperationHolder> selectedOperations = new ArrayList<OperationHolder>();
        if (all == null || all.isEmpty() || selected == null
                || selected.isEmpty())
        {
            return selectedOperations;
        }
        List<OperationHolder> allTmp = new ArrayList<OperationHolder>();
        allTmp.addAll(all);
        Iterator<OperationHolder> it = allTmp.iterator();
        while (it.hasNext())
        {
            OperationHolder obj = it.next();
            for (BuyerOperationHolder holder : selected)
            {
                if (obj.getOpnId().equalsIgnoreCase(holder.getOpnId()))
                {
                    all.remove(obj);
                    selectedOperations.add(obj);
                    break;
                }
            }
        }
        return selectedOperations;
    }


    private List<OperationHolder> refactorBuyerGivenSupplierOperations(
            List<OperationHolder> all,
            List<BuyerGivenSupplierOperationHolder> selected)
    {
        List<OperationHolder> selectedOperations = new ArrayList<OperationHolder>();
        if (all == null || all.isEmpty() || selected == null
                || selected.isEmpty())
        {
            return selectedOperations;
        }
        List<OperationHolder> allTmp = new ArrayList<OperationHolder>();
        allTmp.addAll(all);
        Iterator<OperationHolder> it = allTmp.iterator();
        while (it.hasNext())
        {
            OperationHolder obj = it.next();
            for (BuyerGivenSupplierOperationHolder holder : selected)
            {
                if (obj.getOpnId().equalsIgnoreCase(holder.getOpnId()))
                {
                    all.remove(obj);
                    selectedOperations.add(obj);
                    break;
                }
            }
        }
        return selectedOperations;
    }

    public String initEmailConfig()
    {
        try
        {
            reset = new HashMap<String, ControlParameterHolder>();
            smtpHost = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_HOST");
            smtpPort = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_PORT");
            smtpUser = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_USER");
            smtpPassword = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_PASSWORD");
            smtpProtocol = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SMTP_PROTOCOL");
            needAuth = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "NEED_AUTH");
            authMechanisms = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "AUTH_MECHANISMS");
            needEhlo = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "NEED_EHLO");
            connectType = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "VIA_SSL");
            socketFacClass = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SOCKET_FACTORY_CLASS");
            socketFacFallback = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SOCKET_FACTORY_FALLBACK");
            senderName = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SENDER_NAME");
            senderAddr = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_EMAIL_CONFIG, "SENDER_ADDR");
            replyToAddr = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_EMAIL_CONFIG, "REPLY_TO");
            adminAddr = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_EMAIL_CONFIG, "ADMIN_ADDR");
            
            reset.put("smtpHost", smtpHost);
            reset.put("smtpPort", smtpPort);
            reset.put("smtpUser", smtpUser);
            reset.put("smtpPassword", smtpPassword);
            reset.put("smtpProtocol", smtpProtocol);
            reset.put("needAuth", needAuth);
            reset.put("needEhlo", needEhlo);
            reset.put("connectType", connectType);
            reset.put("socketFacClass", socketFacClass);
            reset.put("socketFacFallback", socketFacFallback);
            reset.put("senderName", senderName);
            reset.put("senderAddr", senderAddr);
            reset.put("replyToAddr", replyToAddr);
            reset.put("adminAddr", adminAddr);
            reset.put("authMechanisms", authMechanisms);
            
            initEmailParameter();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String saveEmailConfig()
    {
        try
        {
            reset = new HashMap<String, ControlParameterHolder>();
            log.info(this.getText("B2BPA0118", new String[] { this
                    .getLoginIdOfCurrentUser() }));
            result = new ArrayList<String>();
            
            this.updateControlParameter(smtpHost, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SMTP_HOST");
            this.updateControlParameter(smtpPort, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SMTP_PORT");
            this.updateControlParameter(smtpUser, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SMTP_USER");
            this.updateControlParameter(smtpPassword, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SMTP_PASSWORD");
            this.updateControlParameter(smtpProtocol, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SMTP_PROTOCOL");
            this.updateControlParameter(needAuth, CTRL_PARAMETER_EMAIL_CONFIG,
                    "NEED_AUTH");
            this.updateControlParameter(needEhlo, CTRL_PARAMETER_EMAIL_CONFIG,
                    "NEED_EHLO");
            this.updateControlParameter(connectType, CTRL_PARAMETER_EMAIL_CONFIG,
                    "CONNECT_TYPE");
            this.updateControlParameter(socketFacClass, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SOCKET_FACTORY_CLASS");
            this.updateControlParameter(socketFacFallback, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SOCKET_FACTORY_FALLBACK");
            this.updateControlParameter(senderName, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SENDER_NAME");
            this.updateControlParameter(senderAddr, CTRL_PARAMETER_EMAIL_CONFIG,
                    "SENDER_ADDR");
            this.updateControlParameter(replyToAddr, CTRL_PARAMETER_EMAIL_CONFIG,
                    "REPLY_TO");
            this.updateControlParameter(adminAddr, CTRL_PARAMETER_EMAIL_CONFIG,
                    "ADMIN_ADDR");
            this.updateControlParameter(authMechanisms, CTRL_PARAMETER_EMAIL_CONFIG,
                    "AUTH_MECHANISMS");
            result.add(this.getText("B2BPA0139"));
            log.info(this.getText("B2BPA0119", new String[] { this
                    .getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            result.add(this.getText("B2BPA0140"));
        }
        return SUCCESS;
    }
    
    private void  initEmailParameter()
    {
        mechanisms = EmailAuthMechanisms.toMapValue();
        connectTypes = EmailConnectType.toMapValue();
    }
    
    public String sentTestMailForEmailConfig()
    {
        try
        {
            boolean testSuccess = false;
            String[] emails = null;
            result = new ArrayList<String>();
            if (adminAddr == null || adminAddr.getStringValue().isEmpty())
            {
                emails = new String[1];
                emails[0] = smtpUser.getStringValue();
                testSuccess = this.sendHtmlEmail(emails, "Test alert message for Email Config", "testMsgEmail.vm", new HashMap<String, Object>());
            }
            else
            {
                emails = adminAddr.getStringValue().split(",");
                
                testSuccess = this.sendHtmlEmail(emails, MAIL_TEST_SUBJECT, "testMsgEmail.vm", new HashMap<String, Object>());
            }
            
            if (testSuccess)
            {
                result.add(this.getText("B2BPA0188"));
            }
            else
            {
                result.add(this.getText("B2BPA0189"));
            }
           
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        
        return SUCCESS;
    }
    
    
    private boolean sendHtmlEmail(final String[] mailTo_, final String subject_,
        final String templateNameContent_,
        final Map<String, Object> fillValuesContent_)
    {
        try
        {
            fillValuesContent_.put(APP_URL, appConfig.getServerUrl());
            
            final String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, templateNameContent_, fillValuesContent_);
            
            StringBuffer sbMail = new StringBuffer(200);
            sbMail.append(MAIL_TO + Arrays.asList(mailTo_)
                + NEW_LINE_WITH_BRACKET + MAIL_SUBJECT + subject_
                + NEW_LINE_WITH_BRACKET + MAIL_SENDER 
                + NEW_LINE_WITH_BRACKET + MAIL_CONTENT + text + "]");
            
            if (log.isInfoEnabled())
            {
                log.info(sbMail.toString());
            }
            
            initMailSender();
            
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception
                {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(
                            mimeMessage);

                    messageHelper.setTo(mailTo_);
                    messageHelper.setSubject(subject_);
                    messageHelper.setText(text, true);

                    // setup reply to email address
                    if (replyToAddr.getStringValue() != null
                            && replyToAddr.getStringValue().trim().length() > 0)
                    {
                        messageHelper.setReplyTo(replyToAddr.getStringValue().trim());
                    }

                    // setup send from
                    if (senderName.getStringValue() != null
                            && senderName.getStringValue().trim().length() > 0)
                    {
                        if (senderAddr.getStringValue() != null
                                && senderAddr.getStringValue().trim().length() > 0)
                        {
                            messageHelper.setFrom(new InternetAddress(senderAddr.getStringValue().trim(), senderName.getStringValue().trim()));
                        }
                        else
                        {
                            messageHelper.setFrom(senderAddr.getStringValue().trim());
                        }
                    }
                    else
                    {
                        messageHelper.setFrom(smtpUser.getStringValue().trim());
                    }
                }
            };

            this.mailSender.send(preparator);

            return true;
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return false;
        }
    }
    
    private void initMailSender() 
    {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost.getStringValue().trim());
        mailSender.setUsername(smtpUser.getStringValue().trim());
        mailSender.setPassword(smtpPassword.getStringValue());
        mailSender.setProtocol(smtpProtocol.getStringValue().trim());
        mailSender.setPort(Integer.parseInt(smtpPort.getStringValue().trim()));
        
        Properties props = this.getProperties();
        Enumeration<?> e = props.propertyNames();
        
        while (e.hasMoreElements()) {
          String key = (String) e.nextElement();
          mailSender.getJavaMailProperties().put(key, props.getProperty(key));
        }
    }
    
    private Properties getProperties()
    {
        Properties pro = new Properties();
        pro.put(MAIL_SMTP_AUTH, needAuth.getValid().toString().toLowerCase(Locale.US));
        pro.put(MAIL_SMTP_EHLO, needEhlo.getValid().toString().toLowerCase(Locale.US));
        
        
        if (needAuth.getValid())
        {
            if (connectType.getStringValue().trim().equalsIgnoreCase(EmailConnectType.TLS.name()))
            {
                pro.put(MAIL_SMTP_AUTH_MECHANISMS, authMechanisms.getStringValue());
                pro.put(MAIL_SMTP_STARTTLES_ENABLE, PROPERTITY_VALUE_TRUE);
                pro.put(MAIL_SMTP_STARTTLES_REQUIRED, PROPERTITY_VALUE_TRUE);
            }
            else if(connectType.getStringValue().trim().equalsIgnoreCase(EmailConnectType.SSL.name()))
            {
                pro.put(MAIL_SMTP_SOCKETFACTORY_CLASS,socketFacClass.getStringValue());
                pro.put(MAIL_SMTP_SOCKETFACTORY_CLASS_FALLBACK, socketFacFallback.getValid().toString().toLowerCase(Locale.US));
            }
        }
        
        pro.put(MAIL_SMTP_CON_TIMEOUT, TIMEOUT_MILLSECONDS);
        pro.put(MAIL_SMTP_TIMEOUT, TIMEOUT_MILLSECONDS);
        pro.put("mail.debug", PROPERTITY_VALUE_TRUE);
        return pro;
    }
    // *****************************************************
    // getter and setter
    // *****************************************************
    public ControlParameterHolder getPwdExpPeri()
    {
        return pwdExpPeri;
    }


    public void setPwdExpPeri(ControlParameterHolder pwdExpPeri)
    {
        this.pwdExpPeri = pwdExpPeri;
    }


    public ControlParameterHolder getEmailExpPeri()
    {
        return emailExpPeri;
    }


    public void setEmailExpPeri(ControlParameterHolder emailExpPeri)
    {
        this.emailExpPeri = emailExpPeri;
    }


    public ControlParameterHolder getMaxAttLogin()
    {
        return maxAttLogin;
    }


    public void setMaxAttLogin(ControlParameterHolder maxAttLogin)
    {
        this.maxAttLogin = maxAttLogin;
    }


    public ControlParameterHolder getHelpDeskNo()
    {
        return helpDeskNo;
    }


    public void setHelpDeskNo(ControlParameterHolder helpDeskNo)
    {
        this.helpDeskNo = helpDeskNo;
    }


    public ControlParameterHolder getHelpDeskEmail()
    {
        return helpDeskEmail;
    }


    public void setHelpDeskEmail(ControlParameterHolder helpDeskEmail)
    {
        this.helpDeskEmail = helpDeskEmail;
    }


    public ControlParameterHolder getReqInterval()
    {
        return reqInterval;
    }


    public void setReqInterval(ControlParameterHolder reqInterval)
    {
        this.reqInterval = reqInterval;
    }


    public ControlParameterHolder getRepeatedLogin()
    {
        return repeatedLogin;
    }


    public void setRepeatedLogin(ControlParameterHolder repeatedLogin)
    {
        this.repeatedLogin = repeatedLogin;
    }


    public ControlParameterHolder getMakerChecker()
    {
        return makerChecker;
    }


    public void setMakerChecker(ControlParameterHolder makerChecker)
    {
        this.makerChecker = makerChecker;
    }


    public ControlParameterHolder getAutoLoginOut()
    {
        return autoLoginOut;
    }


    public void setAutoLoginOut(ControlParameterHolder autoLoginOut)
    {
        this.autoLoginOut = autoLoginOut;
    }


    public List<String> getResult()
    {
        return result;
    }


    public void setResult(List<String> result)
    {
        this.result = result;
    }


    public ControlParameterHolder getMaxLength()
    {
        return maxLength;
    }


    public void setMaxLength(ControlParameterHolder maxLength)
    {
        this.maxLength = maxLength;
    }


    public ControlParameterHolder getMinLength()
    {
        return minLength;
    }


    public void setMinLength(ControlParameterHolder minLength)
    {
        this.minLength = minLength;
    }


    public ControlParameterHolder getAlphanumeric()
    {
        return alphanumeric;
    }


    public void setAlphanumeric(ControlParameterHolder alphanumeric)
    {
        this.alphanumeric = alphanumeric;
    }


    public ControlParameterHolder getNoRepeat()
    {
        return noRepeat;
    }


    public void setNoRepeat(ControlParameterHolder noRepeat)
    {
        this.noRepeat = noRepeat;
    }


    public ControlParameterHolder getMixtureCase()
    {
        return mixtureCase;
    }


    public void setMixtureCase(ControlParameterHolder mixtureCase)
    {
        this.mixtureCase = mixtureCase;
    }


    public ControlParameterHolder getSpecialCharacter()
    {
        return specialCharacter;
    }


    public void setSpecialCharacter(ControlParameterHolder specialCharacter)
    {
        this.specialCharacter = specialCharacter;
    }


    public ControlParameterHolder getNotDictWord()
    {
        return notDictWord;
    }


    public void setNotDictWord(ControlParameterHolder notDictWord)
    {
        this.notDictWord = notDictWord;
    }


    public ControlParameterHolder getNotRepeatForPwdHis()
    {
        return notRepeatForPwdHis;
    }


    public void setNotRepeatForPwdHis(ControlParameterHolder notRepeatForPwdHis)
    {
        this.notRepeatForPwdHis = notRepeatForPwdHis;
    }


    public ControlParameterHolder getNotEqualLoginId()
    {
        return notEqualLoginId;
    }


    public void setNotEqualLoginId(ControlParameterHolder notEqualLoginId)
    {
        this.notEqualLoginId = notEqualLoginId;
    }


    public List<ControlParameterHolder> getOutbounds()
    {
        return outbounds;
    }


    public void setOutbounds(List<ControlParameterHolder> outbounds)
    {
        this.outbounds = outbounds;
    }


    public List<ControlParameterHolder> getInbounds()
    {
        return inbounds;
    }


    public void setInbounds(List<ControlParameterHolder> inbounds)
    {
        this.inbounds = inbounds;
    }


    public ReserveMessageHolder getReserveMessage()
    {
        return reserveMessage;
    }


    public void setReserveMessage(ReserveMessageHolder reserveMessage)
    {
        this.reserveMessage = reserveMessage;
    }


    public ReserveMessageHolder getCreateResMsg()
    {
        return createResMsg;
    }


    public void setCreateResMsg(ReserveMessageHolder createResMsg)
    {
        this.createResMsg = createResMsg;
    }


    public ReserveMessageHolder getEditResMsg()
    {
        return editResMsg;
    }


    public void setEditResMsg(ReserveMessageHolder editResMsg)
    {
        this.editResMsg = editResMsg;
    }


    public Map<String, ControlParameterHolder> getReset()
    {
        return reset;
    }


    public void setReset(Map<String, ControlParameterHolder> reset)
    {
        this.reset = reset;
    }


    public ControlParameterHolder getLogFileName()
    {
        return logFileName;
    }


    public void setLogFileName(ControlParameterHolder logFileName)
    {
        this.logFileName = logFileName;
    }


    public ControlParameterHolder getLogFilePath()
    {
        return logFilePath;
    }


    public void setLogFilePath(ControlParameterHolder logFilePath)
    {
        this.logFilePath = logFilePath;
    }


	public Map<String, String> getPages() 
	{
		return pages;
	}


	public void setPages(Map<String, String> pages) 
	{
		this.pages = pages;
	}


	public Map<String, List<SummaryFieldExHolder>> getFields()
    {
        return fields;
    }


    public void setFields(Map<String, List<SummaryFieldExHolder>> fields)
    {
        this.fields = fields;
    }


    public SummaryPageSettingHolder getSummaryPageSetting()
    {
        return summaryPageSetting;
    }


    public void setSummaryPageSetting(
            SummaryPageSettingHolder summaryPageSetting)
    {
        this.summaryPageSetting = summaryPageSetting;
    }


    public SummaryPageSettingService getSummaryPageSettingService()
    {
        return summaryPageSettingService;
    }


    public void setSummaryPageSettingService(
            SummaryPageSettingService summaryPageSettingService)
    {
        this.summaryPageSettingService = summaryPageSettingService;
    }


    public SummaryFieldService getSummaryFieldService()
    {
        return summaryFieldService;
    }


    public void setSummaryFieldService(SummaryFieldService summaryFieldService)
    {
        this.summaryFieldService = summaryFieldService;
    }


    public String getJsonStr()
    {
        return jsonStr;
    }


    public void setJsonStr(String jsonStr)
    {
        this.jsonStr = jsonStr;
    }


    public List<BuyerHolder> getBuyers()
    {
        return buyers;
    }


    public void setBuyers(List<BuyerHolder> buyers)
    {
        this.buyers = buyers;
    }


    public List<OperationHolder> getBuyerOperations()
    {
        return buyerOperations;
    }


    public void setBuyerOperations(List<OperationHolder> buyerOperations)
    {
        this.buyerOperations = buyerOperations;
    }


    public List<OperationHolder> getBuyerSelectedOperations()
    {
        return buyerSelectedOperations;
    }


    public void setBuyerSelectedOperations(
            List<OperationHolder> buyerSelectedOperations)
    {
        this.buyerSelectedOperations = buyerSelectedOperations;
    }


    public List<OperationHolder> getSupplierOperations()
    {
        return supplierOperations;
    }


    public void setSupplierOperations(List<OperationHolder> supplierOperations)
    {
        this.supplierOperations = supplierOperations;
    }


    public List<OperationHolder> getSupplierSelectedOperations()
    {
        return supplierSelectedOperations;
    }


    public void setSupplierSelectedOperations(
            List<OperationHolder> supplierSelectedOperations)
    {
        this.supplierSelectedOperations = supplierSelectedOperations;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public List<String> getBuyerSelectedOperationOids()
    {
        return buyerSelectedOperationOids;
    }


    public void setBuyerSelectedOperationOids(
            List<String> buyerSelectedOperationOids)
    {
        this.buyerSelectedOperationOids = buyerSelectedOperationOids;
    }

    
    public List<String> getSupplierSelectedOperationOids()
    {
        return supplierSelectedOperationOids;
    }


    public void setSupplierSelectedOperationOids(
            List<String> supplierSelectedOperationOids)
    {
        this.supplierSelectedOperationOids = supplierSelectedOperationOids;
    }


    public Map<String, List<ControlParameterHolder>> getResetHseKeep()
    {
        return resetHseKeep;
    }


    public void setResetHseKeep(
            Map<String, List<ControlParameterHolder>> resetHseKeep)
    {
        this.resetHseKeep = resetHseKeep;
    }


    public ControlParameterHolder getMaxUserAmountForSupplier()
    {
        return maxUserAmountForSupplier;
    }


    public void setMaxUserAmountForSupplier(
            ControlParameterHolder maxUserAmountForSupplier)
    {
        this.maxUserAmountForSupplier = maxUserAmountForSupplier;
    }


    public ControlParameterHolder getDefaultPageSize()
    {
        return defaultPageSize;
    }


    public void setDefaultPageSize(ControlParameterHolder defaultPageSize)
    {
        this.defaultPageSize = defaultPageSize;
    }


    public ControlParameterHolder getPageSizes()
    {
        return pageSizes;
    }


    public void setPageSizes(ControlParameterHolder pageSizes)
    {
        this.pageSizes = pageSizes;
    }
    
    
    public ControlParameterHolder getDocumentWindowForBuyer()
    {
        return documentWindowForBuyer;
    }


    public void setDocumentWindowForBuyer(
        ControlParameterHolder documentWindowForBuyer)
    {
        this.documentWindowForBuyer = documentWindowForBuyer;
    }


    public ControlParameterHolder getDocumentWindowForSupplier()
    {
        return documentWindowForSupplier;
    }


    public void setDocumentWindowForSupplier(
        ControlParameterHolder documentWindowForSupplier)
    {
        this.documentWindowForSupplier = documentWindowForSupplier;
    }


    public ControlParameterHolder getCurrentGst()
    {
        return currentGst;
    }


    public void setCurrentGst(ControlParameterHolder currentGst)
    {
        this.currentGst = currentGst;
    }


    public ControlParameterHolder getNewGst()
    {
        return newGst;
    }


    public void setNewGst(ControlParameterHolder newGst)
    {
        this.newGst = newGst;
    }


    public ControlParameterHolder getNewGstFromDate()
    {
        return newGstFromDate;
    }


    public void setNewGstFromDate(ControlParameterHolder newGstFromDate)
    {
        this.newGstFromDate = newGstFromDate;
    }


    public ControlParameterHolder getSmtpHost()
    {
        return smtpHost;
    }


    public void setSmtpHost(ControlParameterHolder smtpHost)
    {
        this.smtpHost = smtpHost;
    }

    

    public ControlParameterHolder getSmtpPort()
    {
        return smtpPort;
    }


    public void setSmtpPort(ControlParameterHolder smtpPort)
    {
        this.smtpPort = smtpPort;
    }


    public ControlParameterHolder getSmtpUser()
    {
        return smtpUser;
    }


    public void setSmtpUser(ControlParameterHolder smtpUser)
    {
        this.smtpUser = smtpUser;
    }


    public ControlParameterHolder getSmtpPassword()
    {
        return smtpPassword;
    }


    public void setSmtpPassword(ControlParameterHolder smtpPassword)
    {
        this.smtpPassword = smtpPassword;
    }


    public ControlParameterHolder getSmtpProtocol()
    {
        return smtpProtocol;
    }


    public void setSmtpProtocol(ControlParameterHolder smtpProtocol)
    {
        this.smtpProtocol = smtpProtocol;
    }


    public ControlParameterHolder getNeedAuth()
    {
        return needAuth;
    }


    public void setNeedAuth(ControlParameterHolder needAuth)
    {
        this.needAuth = needAuth;
    }


    public ControlParameterHolder getNeedEhlo()
    {
        return needEhlo;
    }


    public void setNeedEhlo(ControlParameterHolder needEhlo)
    {
        this.needEhlo = needEhlo;
    }


    public ControlParameterHolder getViaSSL()
    {
        return connectType;
    }


    public void setViaSSL(ControlParameterHolder viaSSL)
    {
        this.connectType = viaSSL;
    }


    public ControlParameterHolder getSocketFacClass()
    {
        return socketFacClass;
    }


    public void setSocketFacClass(ControlParameterHolder socketFacClass)
    {
        this.socketFacClass = socketFacClass;
    }


    public ControlParameterHolder getSocketFacFallback()
    {
        return socketFacFallback;
    }


    public void setSocketFacFallback(ControlParameterHolder socketFacFallback)
    {
        this.socketFacFallback = socketFacFallback;
    }


    public ControlParameterHolder getSenderName()
    {
        return senderName;
    }


    public void setSenderName(ControlParameterHolder senderName)
    {
        this.senderName = senderName;
    }


    public ControlParameterHolder getSenderAddr()
    {
        return senderAddr;
    }


    public void setSenderAddr(ControlParameterHolder senderAddr)
    {
        this.senderAddr = senderAddr;
    }


    public ControlParameterHolder getReplyToAddr()
    {
        return replyToAddr;
    }


    public void setReplyToAddr(ControlParameterHolder replyToAddr)
    {
        this.replyToAddr = replyToAddr;
    }


    public ControlParameterHolder getAdminAddr()
    {
        return adminAddr;
    }


    public void setAdminAddr(ControlParameterHolder adminAddr)
    {
        this.adminAddr = adminAddr;
    }


    public Map<String, String> getMechanisms()
    {
        return mechanisms;
    }


    public void setMechanisms(Map<String, String> mechanisms)
    {
        this.mechanisms = mechanisms;
    }

    
    public ControlParameterHolder getConnectType()
    {
        return connectType;
    }


    public void setConnectType(ControlParameterHolder connectType)
    {
        this.connectType = connectType;
    }


    public Map<String, String> getConnectTypes()
    {
        return connectTypes;
    }


    public void setConnectTypes(Map<String, String> connectTypes)
    {
        this.connectTypes = connectTypes;
    }


    public ControlParameterHolder getAuthMechanisms()
    {
        return authMechanisms;
    }


    public void setAuthMechanisms(ControlParameterHolder authMechanisms)
    {
        this.authMechanisms = authMechanisms;
    }


    public ControlParameterHolder getMaxPdfCount()
    {
        return maxPdfCount;
    }


    public void setMaxPdfCount(ControlParameterHolder maxPdfCount)
    {
        this.maxPdfCount = maxPdfCount;
    }


    public ControlParameterHolder getMaxExcelCount()
    {
        return maxExcelCount;
    }


    public void setMaxExcelCount(ControlParameterHolder maxExcelCount)
    {
        this.maxExcelCount = maxExcelCount;
    }


    public ControlParameterHolder getMaxSummaryExcelCount()
    {
        return maxSummaryExcelCount;
    }


    public void setMaxSummaryExcelCount(ControlParameterHolder maxSummaryExcelCount)
    {
        this.maxSummaryExcelCount = maxSummaryExcelCount;
    }


    public ControlParameterHolder getMaxDayOfReport()
    {
        return maxDayOfReport;
    }


    public void setMaxDayOfReport(ControlParameterHolder maxDayOfReport)
    {
        this.maxDayOfReport = maxDayOfReport;
    }


    public ControlParameterHolder getFileSizeLimit()
    {
        return fileSizeLimit;
    }


    public void setFileSizeLimit(ControlParameterHolder fileSizeLimit)
    {
        this.fileSizeLimit = fileSizeLimit;
    }
    
}
