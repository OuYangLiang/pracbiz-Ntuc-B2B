package com.pracbiz.b2bportal.core.action;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.LogoFileChecker;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.FrequencyType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.BuyerRuleHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.CountryHolder;
import com.pracbiz.b2bportal.core.holder.CurrencyHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.BusinessRuleExHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerExHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerMsgSettingExHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerMsgSettingReportExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.CountryMapper;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerRuleService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.CurrencyService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupTmpService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.ChannelConfigHelper;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

public class BuyerAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(BuyerAction.class);
    private static final String MSG_CODE_B2BPC0310 = "B2BPC0310";
    private static final long serialVersionUID = 4154929062041916560L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_BUYER = "SEARCH_PARAMETER_BUYER";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.BuyerAction.selectedOids";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final String REQUEST_OID_DELIMITER = "\\-";
    private static final String SECT_ID_HSEKEEP = "HSEKEEP";
    private static final String PARAM_ID_INBOUND = "INBOUND";
    private static final String PARAM_ID_OUTBOUND = "OUTBOUND";
    private static final String SESSION_BUYER_MSG_SETTING_INBOUND = "EDIT_BUYER_MSG_SETTING_INBOUND";
    private static final String SESSION_BUYER_MSG_SETTING_OUTBOUND = "EDIT_BUYER_MSG_SETTING_OUTBOUND";

    private static final Map<String, String> sortMap;

    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("buyerOid", "BUYER_OID");
        sortMap.put("buyerName", "BUYER_NAME");
        sortMap.put("buyerCode", "BUYER_CODE");
        sortMap.put("mboxId", "MBOX_ID");
        sortMap.put("channel", "CHANNEL");
        sortMap.put("active", "ACTIVE");
        sortMap.put("updateDate", "UPDATE_DATE");
    }

    private BuyerHolder param;
    private BuyerExHolder buyerEx;
    private Map<Boolean, String> status;
    private List<? extends Object> countries;
    private List<? extends Object> currencies;
    private Map<String, String> deploymentMode;
    private List<String> channels;
    // Edit Buyer Msg Setting
    private Map<String, BuyerMsgSettingExHolder> inbounds;
    private Map<String, BuyerMsgSettingExHolder> outbounds;
    private List<String> inboundMsg;
    private List<String> outboundMsg;
    //edit business rule
    private List<String> funcGroups;
    private List<String> funcIds;
    private String funcGroup;
    private String funcId;
    private BusinessRuleExHolder ggAutoLogout;
    private BusinessRuleExHolder ggMatchingJobMinBufferingDays;
    private BusinessRuleExHolder ggMatchingJobMaxBufferingDays;
    private BusinessRuleExHolder ggDailyPoReportJobDaysBefore;
    private BusinessRuleExHolder ggDailyNotificationJobMissingGrnMinBufferingDays;
    private BusinessRuleExHolder ggDailyNotificationJobMissingGrnMaxBufferingDays;
    private BusinessRuleExHolder ggDnGeneratingJobMatchingMaxBuffingDays;
    private BusinessRuleExHolder ggDnGeneratingJobMatchingMinBuffingDays;
    private BusinessRuleExHolder ggRTVDnGeneratingJobBuffingDays;
    private BusinessRuleExHolder ggContinueProcessErrorBatch;
    private BusinessRuleExHolder ggDailyNotificationJobMissingGiMinBufferingDays;
    private BusinessRuleExHolder ggDailyNotificationJobMissingGiMaxBufferingDays;
    private BusinessRuleExHolder ggRTVDnDisputeAlertWindow;
    private BusinessRuleExHolder ggDisableInvoicePaymentInstructions;
    private BusinessRuleExHolder ggRtvGiDnQtyTolerance;
    private BusinessRuleExHolder ggRtvGiDnQtyToleranceType;
    private BusinessRuleExHolder ggRtvGiDnPriceTolerance;
    private BusinessRuleExHolder ggRtvGiDnPriceToleranceType;
    private BusinessRuleExHolder ggRtvGiDnReportGeneratingDateRange;
    private BusinessRuleExHolder pcispQtyEditable;
    private BusinessRuleExHolder pcispQtylessThanPO;
    private BusinessRuleExHolder pcispFocQtyEditable;
    private BusinessRuleExHolder pcispDiscountEditable;
    private BusinessRuleExHolder pcispDiscountForDetailEditable;
    private BusinessRuleExHolder pcispCashDiscountEditable;
    private BusinessRuleExHolder pcispFocQtylessThanPO;
    private BusinessRuleExHolder pcispUnitPriceEditable;
    private BusinessRuleExHolder pcispEmailToStore;
    private BusinessRuleExHolder pcispPdfAsAttachment;
    private BusinessRuleExHolder pcispUnitPriceLessThanPO;
    private BusinessRuleExHolder pcicpItemDiscountEditable;
    private BusinessRuleExHolder pcicpItemAmountEditable;
    private BusinessRuleExHolder pcicpItemSharedCostEditable;
    private BusinessRuleExHolder pcicpTradeDiscountEditable;
    private BusinessRuleExHolder pcicpCashDiscountEditable;
    private BusinessRuleExHolder pcicpIgnoreExpiryDate;
    private BusinessRuleExHolder pgDeliveryDateRange;
    private BusinessRuleExHolder dbAutoGenStockDn;
    private BusinessRuleExHolder dbAutoGenCostDn;
    private BusinessRuleExHolder dbAutoSendStockDn;
    private BusinessRuleExHolder dbAutoSendCostDn;
    private BusinessRuleExHolder dnNoStyle1;
	private BusinessRuleExHolder dbUnityFileStype;
    private BusinessRuleExHolder dbNeedTranslate;
    private BusinessRuleExHolder dbAutoGenDnFromGI;
    private BusinessRuleExHolder dbAllowSupplierDisputeMatchingDn;
    private BusinessRuleExHolder dbDiscrepancyReportToUser;
    private BusinessRuleExHolder dbAutoCloseAcceptedRecord;
    private BusinessRuleExHolder dbSendResolutionAndOutstandingByGroup;
    private BusinessRuleExHolder dbResolutionRecipients;
    private BusinessRuleExHolder dbOutstandingRecipients;
    private BusinessRuleExHolder dbPriceDiscrepancyRecipients;
    private BusinessRuleExHolder dbQtyDiscrepancyRecipients;
    private BusinessRuleExHolder dbDnExportingRecipients;
    private BusinessRuleExHolder mpigdQtyInvLessGrn;
    private BusinessRuleExHolder mpigdPriceInvLessPo;
    private BusinessRuleExHolder mpigdQtyPoLessGrn;
    private BusinessRuleExHolder mpigdAutoAcceptQtyInvLessGrn;
    private BusinessRuleExHolder mpigdAutoAcceptPriceInvLessPo;
    private BusinessRuleExHolder mpigdAmountTolerance;
    private BusinessRuleExHolder mpigdAutoApproveMatchedByDn;
    private BusinessRuleExHolder mpigdEnableSupplierToDispute;
    private BusinessRuleExHolder mpigdAutoApproveClosedAcceptedRecord;
    private BusinessRuleExHolder mpigdAutoCloseAcceptedRecord;
    private BusinessRuleExHolder mpigdAutoCloseRejectedRecord;
    private BusinessRuleExHolder mpigdChangeInvDateToGrnDate;
    private BusinessRuleExHolder mpigdSkipMatching;
    private BusinessRuleExHolder mpigdDiscrepancyReportToUser;
    private BusinessRuleExHolder mpigdAutoRejectBuyerLossUnmatchedRecord;
    private BusinessRuleExHolder mpigdSendResolutionAndOutstandingByGroup;
    private BusinessRuleExHolder mpigdMatchedRecipients;
    private BusinessRuleExHolder mpigdUnmatchedRecipients;
    private BusinessRuleExHolder mpigdDefaultRecipients;
    private BusinessRuleExHolder mpigdMatchingJobRecipients;
    private BusinessRuleExHolder mpigdResolutionRecipients;
    private BusinessRuleExHolder mpigdOutstandingRecipients;
    private BusinessRuleExHolder mpigdInvoiceExportingRecipients;
    private BusinessRuleExHolder mpigdMissingGrnNotificationRecipients;
    private BusinessRuleExHolder mpigdPriceDiscrepancyRecipients;
    private BusinessRuleExHolder mpigdQtyDiscrepancyRecipients;
    private BusinessRuleExHolder mpigdMissingGiNotificationRecipients;
    private BusinessRuleExHolder mpigdRtvGiDnExceptionReportRecipients;
    private BusinessRuleExHolder smiGenAdminUser;
    private BusinessRuleExHolder smiAdminRole;
    private BusinessRuleExHolder smiGenResultTxt;
    private BusinessRuleExHolder iiUpdate;
    private BusinessRuleExHolder iiDeleteAndInsert;
    private BusinessRuleExHolder iiSelectOneToCompare;
    private BusinessRuleExHolder ggSupplierCanDisputeGRN;
    private BusinessRuleExHolder ggPreventItemsNotExistInPO;
    private BusinessRuleExHolder ggPreventItemsLessThanPO;
    private BusinessRuleExHolder ggPreventItemsQtyMoreThanPO;
    private BusinessRuleExHolder igPreventItemsNotExistInPO;
    private BusinessRuleExHolder gigPreventItemsNotExistInRtv;
    private BusinessRuleExHolder gigPreventItemsLessThanRtv;
    private BusinessRuleExHolder gigPreventItemsQtyMoreThanRtv;
    private BusinessRuleExHolder pgNeedValidateConPo;
    private BusinessRuleExHolder dsdNeedValidateSalesData;
    private String result;
    

    @Autowired
    private transient BuyerService buyerService;
    @Autowired
    private transient CountryMapper countryMapper;
    @Autowired
    private transient OidService oidService;
    @Autowired
    private transient ControlParameterService controlParameterService;
    @Autowired
    private transient BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private transient CurrencyService currencyService;
    @Autowired
    private transient GroupService groupService;
    @Autowired
    private transient GroupTmpService groupTmpService;
    @Autowired
    private transient UserProfileService userProfileService;
    @Autowired
    private transient UserProfileTmpService userProfileTmpService;
    @Autowired
    private transient ChannelConfigHelper channelConfigHelper;
    @Autowired
    private transient MsgTransactionsService msgTransactionsService;
    @Autowired
    private transient CustomAppConfigHelper appConfig;
    @Autowired
    private transient BusinessRuleService businessRuleService;
    @Autowired
    private transient BuyerRuleService buyerRuleService;
    @Autowired
    private transient ValidationConfigHelper validationConfig;
    @Autowired
    private transient BuyerMsgSettingReportService buyerMsgSettingReportService;
    
    private static final String VLD_PTN_KEY = "ROLE_ID";

    private File logo;
    private String logoContentType;

    private transient InputStream imageResult;
    private String contentType;


    public BuyerAction()
    {
        this.initMsg();
    }


    public String putParamIntoSession()
    {
        this.getSession().put(SESSION_OID_PARAMETER,
                this.getRequest().getParameter(REQUEST_PARAMTER_OID));

        return SUCCESS;
    }


    // *****************************************************
    // summary page
    // *****************************************************
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_BUYER);

        param = (BuyerHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_BUYER);

        try
        {
            status = new HashMap<Boolean, String>();
            status.put(true, this.getText("select.active"));
            status.put(false, this.getText("select.inactive"));
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
            param = new BuyerHolder();
        }

        if ("".equals(this.getRequest().getParameter("param.active")))
        {
            param.setActive(null);
        }

        try
        {
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        getSession().remove(CommonConstants.SESSION_CHANGED);
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_BUYER, param);

        return SUCCESS;
    }


    public String data()
    {
        try
        {
            BuyerHolder searchParam = (BuyerHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_BUYER);
            if (searchParam == null)
            {
                searchParam = new BuyerHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_BUYER, searchParam);
            }
            searchParam.setRequestPage(start / count + 1);
            searchParam.setPageSize(count);
            BuyerExHolder exParam = new BuyerExHolder();
            BeanUtils.copyProperties(searchParam, exParam);
            exParam.setCurrentUserBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            this.obtainListRecordsOfPagination(buyerService, exParam,
                    sortMap, "buyerOid", MODULE_KEY_BUYER);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }


    // *****************************************************
    // create page
    // *****************************************************
    public String initAdd()
    {
        try
        {
            param = new BuyerHolder();
            param.setCtryCode("SG");
            param.setCurrCode("SGD");
            param.setActive(true);
            initSelect();
        }
        catch (Exception e)
        {
            this.handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }


    public void validateSaveAdd()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            param.trimAllString();
            buyerEx.trimAllString();
            if (!flag
                    && DeploymentMode.REMOTE.name().equals(
                            param.getDeploymentMode().name())
                    && (param.getChannel() == null || param.getChannel()
                            .equals("")))
            {
                this.addActionError(this.getText("B2BPC0323"));
                flag = true;
            }
            if (!flag
                    && buyerService
                            .selectBuyerByBuyerCode(param.getBuyerCode()) != null)
            {
                this.addActionError(this.getText("B2BPC0301",
                        new String[] { param.getBuyerCode() }));
                flag = true;
            }
            if (!flag
                    && buyerService.selectBuyerByMboxId(param.getMboxId()) != null)
            {
                this.addActionError(this.getText("B2BPC0302",
                        new String[] { param.getMboxId() }));
                flag = true;
            }
            if (buyerEx.getGstPercentStr() != null
                    && !"".equals(buyerEx.getGstPercentStr()))
            {
                try
                {
                    param.setGstPercent(new BigDecimal(buyerEx
                            .getGstPercentStr()));
                    if (param.getGstPercent().compareTo(new BigDecimal("100")) > 0
                            || param.getGstPercent().compareTo(BigDecimal.ZERO) < 0)
                    {
                        this.addActionError(this.getText(MSG_CODE_B2BPC0310));
                        flag = true;
                    }
                }
                catch (Exception e)
                {
                    this.addActionError(this.getText(MSG_CODE_B2BPC0310));
                    flag = true;
                }
            }
            if (logo != null)
            {
                boolean logoCheck = true;
                if (logoContentType.equalsIgnoreCase("image/jpg")
                        || logoContentType.equalsIgnoreCase("image/jpeg")
                        || logoContentType.equalsIgnoreCase("image/gif")
                        || logoContentType.equalsIgnoreCase("image/png")
                        || logoContentType.equalsIgnoreCase("image/bmp")
                        || logoContentType.equalsIgnoreCase("image/pjpeg"))
                {
                    if (logoContentType.equalsIgnoreCase("image/jpg"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_JPG);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/jpeg"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_JPG);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/gif"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_GIF);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/png"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_PNG);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/pjpeg"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_JPG);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/bmp"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_BMP);
                    }
                    
                    if (!logoCheck)
                    {
                        this.addActionError(this.getText("B2BPC0326"));
                        flag = true;  
                    }
                
                }
                else
                {
                    this.addActionError(this.getText("B2BPC0326"));
                    flag = true;
                }
                
                
                if (!flag && logo.length() / 1024 > 100)
                {
                    this.addActionError(this.getText("B2BPC0327"));
                    flag = true;
                }
            }
            if (flag)
            {
                initSelect();
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }


    public String saveAdd()
    {
        try
        {
            param.setBuyerOid(oidService.getOid());
            param.setCreateBy(this.getCommonParameter().getLoginId());
            param.setCreateDate(new Date());
            if (!param.getActive())
            {
                param.setBlocked(false);
            }
            if (logo != null)
            {
                param.setLogo(FileUtil.getInstance().readByteFromDisk(
                        logo.getPath()));
            }
            this.addUnExistMsgSetting(param, true);
            buyerService.insertBuyerWithMsgSetting(getCommonParameter(), param);
            log.info(this.getText("B2BPC0346", new String[]{param.getBuyerCode(), this.getLoginIdOfCurrentUser()}));
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    // *****************************************************
    // init view or create
    // *****************************************************
    public String initViewOrEdit()
    {
        try
        {
            if (param != null && param.getBuyerOid() != null)
            {
                param = buyerService.selectBuyerWithBlobsByKey(param
                        .getBuyerOid());
                initSelect();
                buyerEx = new BuyerExHolder();
                BeanUtils.copyProperties(param, buyerEx);
                buyerEx
                        .setGstPercentStr(buyerEx.getGstPercent() == null ? null
                                : String.valueOf(buyerEx.getGstPercent()
                                        .doubleValue()));
                for (Object country : countries)
                {
                    if (((CountryHolder) country).getCtryCode().equals(
                            buyerEx.getCtryCode()))
                    {
                        buyerEx.setCtryDesc(((CountryHolder) country)
                                .getCtryDesc());
                    }
                }
            }
        }
        catch (Exception e)
        {
            this.handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    // *****************************************************
    // init view or create
    // *****************************************************
    public void validateSaveEdit()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            param.trimAllString();
            buyerEx.trimAllString();
            if (!flag
                    && DeploymentMode.REMOTE.name().equals(
                            param.getDeploymentMode().name())
                    && (param.getChannel() == null || param.getChannel()
                            .equals("")))
            {
                this.addActionError(this.getText("B2BPC0323",
                        new String[] { param.getBuyerCode() }));
                flag = true;
            }
            if (buyerEx.getGstPercentStr() != null
                    && !"".equals(buyerEx.getGstPercentStr()))
            {
                try
                {
                    param.setGstPercent(new BigDecimal(buyerEx
                            .getGstPercentStr()));
                    if (param.getGstPercent().compareTo(new BigDecimal("100")) > 0
                            || param.getGstPercent().compareTo(BigDecimal.ZERO) < 0)
                    {
                        this.addActionError(this.getText(MSG_CODE_B2BPC0310));
                        flag = true;
                    }
                }
                catch (Exception e)
                {
                    this.addActionError(this.getText(MSG_CODE_B2BPC0310));
                    flag = true;
                }
            }
            if (logo != null)
            {
                boolean logoCheck = true;
                if (logoContentType.equalsIgnoreCase("image/jpg")
                        || logoContentType.equalsIgnoreCase("image/jpeg")
                        || logoContentType.equalsIgnoreCase("image/gif")
                        || logoContentType.equalsIgnoreCase("image/png")
                        || logoContentType.equalsIgnoreCase("image/bmp")
                        || logoContentType.equalsIgnoreCase("image/pjpeg"))
                {
                    if (logoContentType.equalsIgnoreCase("image/jpg"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_JPG);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/jpeg"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_JPG);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/gif"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_GIF);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/png"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_PNG);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/pjpeg"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_JPG);
                    }
                    
                    if (logoContentType.equalsIgnoreCase("image/bmp"))
                    {
                        logoCheck = LogoFileChecker.getInstance().isFileAValidLogo(logo, LogoFileChecker.FILETYPE_BMP);
                    }
                    
                    if (!logoCheck)
                    {
                        this.addActionError(this.getText("B2BPC0326"));
                        flag = true;  
                    }
                
                }
                else
                {
                    this.addActionError(this.getText("B2BPC0326"));
                    flag = true;
                }
                
                
                if (!flag && logo.length() / 1024 > 100)
                {
                    this.addActionError(this.getText("B2BPC0327"));
                    flag = true;
                }
            }
            if (flag)
            {
                initSelect();
                BuyerHolder obj = buyerService.selectBuyerWithBlobsByKey(param
                        .getBuyerOid());
                if (obj != null)
                {
                    param.setMboxId(obj.getMboxId());
                    param.setLogo(obj.getLogo());
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }


    public String saveEdit()
    {
        try
        {
            param.setUpdateBy(this.getCommonParameter().getLoginId());
            param.setUpdateDate(new Date());
            if (!param.getActive())
            {
                param.setBlocked(false);
            }
            BuyerHolder oldObj = buyerService.selectBuyerWithBlobsByKey(param
                    .getBuyerOid());
            if (logo == null)
            {
                param.setLogo(oldObj.getLogo());
            }
            else
            {
                param.setLogo(FileUtil.getInstance().readByteFromDisk(
                        logo.getPath()));
            }
            param.setCreateBy(oldObj.getCreateBy());
            param.setCreateDate(oldObj.getCreateDate());
            param.setMboxId(oldObj.getMboxId());
            param.setBuyerCode(oldObj.getBuyerCode());
            buyerService.auditUpdateByPrimaryKeyWithBLOBs(this.getCommonParameter(), oldObj, param);
            log.info(this.getText("B2BPC0347", new String[]{param.getBuyerCode(), this.getLoginIdOfCurrentUser()}));
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    // *****************************************************
    // delete buyer
    // *****************************************************
    public String saveDelete()
    {
        try
        {
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
                BigDecimal buyerOid = new BigDecimal(part);
                BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
                if (buyer == null)
                {
                    msg.saveError(this.getText("B2BPC0304",
                            new String[] { part }));

                }
                else if (msgTransactionsService
                        .selectMsgTransactionsByBuyerOid(buyerOid) == null)
                {
                    if (this.checkGroup(buyerOid) && this.checkUser(buyerOid))
                    {
                        CommonParameterHolder cp = new CommonParameterHolder();
                        BeanUtils.copyProperties(this.getCommonParameter(), cp);
                        cp.setMkMode(false);
                        buyerService.deleteBuyer(cp,
                                buyerOid);
                        log.info(this.getText("B2BPC0348", new String[]{buyer.getBuyerCode(), this.getLoginIdOfCurrentUser()}));
                        msg.saveSuccess(this.getText("B2BPC0303",
                                new String[] { buyer.getBuyerName() }));
                    }
                    else
                    {
                        msg.saveError(this.getText("B2BPC0340",
                                new String[] { buyer.getBuyerCode() }));
                    }
                }
                else
                {
                    msg.saveError(this.getText("B2BPC0342",
                            new String[] { buyer.getBuyerCode() }));
                }
            }

            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

            msg.addMessageTarget(mt);

        }
        catch (Exception e)
        {
            handleException(e);
        }

        return FORWARD_COMMON_MESSAGE;
    }


    // *****************************************************
    // edit buyer message setting
    // *****************************************************
    public String initEditMsgSetting()
    {
        try
        {
            inbounds = new HashMap<String, BuyerMsgSettingExHolder>();
            outbounds = new HashMap<String, BuyerMsgSettingExHolder>();
            inboundMsg = new ArrayList<String>();
            outboundMsg = new ArrayList<String>();
            if (param != null && param.getBuyerOid() != null)
            {
                param = buyerService.selectBuyerByKey(param.getBuyerOid());
                BuyerMsgSettingHolder msg = new BuyerMsgSettingHolder();
                msg.setBuyerOid(param.getBuyerOid());
                msg.setSortField("MSG_TYPE");
                msg.setSortOrder("ASC");
                List<BuyerMsgSettingHolder> buyerMsgSettings = buyerMsgSettingService
                        .select(msg);
                if (buyerMsgSettings != null)
                {
                    for (BuyerMsgSettingHolder holder : buyerMsgSettings)
                    {
                        param.addBuyerMsgSetting(holder);
                    }
                }
                this.addUnExistMsgSetting(param, false);
                
                ControlParameterHolder cph = new ControlParameterHolder();
                cph.setSectId(SECT_ID_HSEKEEP);
                cph.setCatId(PARAM_ID_INBOUND);
                List<? extends Object> inboundTmp = controlParameterService.select(cph);
                cph.setSectId(SECT_ID_HSEKEEP);
                cph.setCatId(PARAM_ID_OUTBOUND);
                List<? extends Object> outboundTmp = controlParameterService.select(cph);

                List<BuyerMsgSettingHolder> currBuyerMsgSettings = param
                        .getMsgSetting();
                if (currBuyerMsgSettings != null
                        && !currBuyerMsgSettings.isEmpty())
                {
                    for (int i = 0; i < currBuyerMsgSettings.size(); i++)
                    {
                        BuyerMsgSettingHolder buyerMsg = currBuyerMsgSettings
                                .get(i);
                        if (outboundTmp != null && !outboundTmp.isEmpty())
                        {
                            for (int j = 0; j < outboundTmp.size(); j++)
                            {
                                cph = (ControlParameterHolder) outboundTmp
                                        .get(j);
                                if (buyerMsg.getMsgType().equals(
                                        cph.getParamId()))
                                {
                                    BuyerMsgSettingExHolder buyerMsgEx = new BuyerMsgSettingExHolder();
                                    BeanUtils.copyProperties(buyerMsg,
                                            buyerMsgEx);
                                    buyerMsgEx.setFileFormatList(appConfig.getFileFormatListByMsgType(cph.getParamId()));
                                    buyerMsgEx.setSubTypeReportMap(obtainSubTypeReports(param.getBuyerOid(), param.getBuyerCode(), buyerMsg.getMsgType()));
                                    outbounds.put(buyerMsg.getMsgType(),
                                            buyerMsgEx);
                                    outboundMsg.add(buyerMsg.getMsgType());
                                    break;
                                }
                            }
                        }

                        if (inboundTmp != null && !inboundTmp.isEmpty())
                        {
                            for (int j = 0; j < inboundTmp.size(); j++)
                            {
                                cph = (ControlParameterHolder) inboundTmp
                                        .get(j);
                                if (buyerMsg.getMsgType().equals(
                                        cph.getParamId()))
                                {
                                    BuyerMsgSettingExHolder buyerMsgEx = new BuyerMsgSettingExHolder();
                                    BeanUtils.copyProperties(buyerMsg,
                                            buyerMsgEx);
                                    buyerMsgEx.setFileFormatList(appConfig.getFileFormatListByMsgType(cph.getParamId()));
                                    buyerMsgEx.setSubTypeReportMap(obtainSubTypeReports(param.getBuyerOid(), param.getBuyerCode(), buyerMsg.getMsgType()));
                                    buyerMsgEx.setAlertIntervalValue(buyerMsgEx
                                            .getAlertInterval() == null ? null
                                            : String.valueOf(buyerMsgEx
                                                    .getAlertInterval()));
                                    inbounds.put(buyerMsg.getMsgType(),
                                            buyerMsgEx);
                                    inboundMsg.add(buyerMsg.getMsgType());
                                    break;
                                }
                            }
                        }
                    }
                    setMsgDescAndBasicFlag(inbounds);
                    setMsgDescAndBasicFlag(outbounds);
                    this.getSession().put(SESSION_BUYER_MSG_SETTING_INBOUND,
                            inboundMsg);
                    this.getSession().put(SESSION_BUYER_MSG_SETTING_OUTBOUND,
                            outboundMsg);
                }
                initBusinessRules(param.getBuyerOid());
            }
            
            
        }
        catch (Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    private Map<String, BuyerMsgSettingReportExHolder> obtainSubTypeReports(BigDecimal buyerOid, String buyerCode, String msgType) throws Exception
    {
        Map<String, BuyerMsgSettingReportExHolder> result = new HashMap<String, BuyerMsgSettingReportExHolder>();
        List<String> subTypes = appConfig.getSubTypesByMsgType(msgType);
        if (subTypes != null && !subTypes.isEmpty())
        {
            for (String subType : subTypes)
            {
                BuyerMsgSettingReportExHolder buyerMsgSettingReportEx = new BuyerMsgSettingReportExHolder();
                buyerMsgSettingReportEx.setBuyerOid(buyerOid);
                buyerMsgSettingReportEx.setMsgType(msgType);
                buyerMsgSettingReportEx.setSubType(subType);
                BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(buyerOid, msgType, subType);
                if (buyerMsgSettingReport != null)
                {
                    BeanUtils.copyProperties(buyerMsgSettingReport, buyerMsgSettingReportEx);
                }
                buyerMsgSettingReportEx.setStandard(appConfig.getStandardReports(msgType, subType));
                buyerMsgSettingReportEx.setCustomized(appConfig.getCustomizedReports(buyerCode, msgType, subType));
                
                if (buyerMsgSettingReportEx.getCustomizedReport() != null && buyerMsgSettingReportEx.getCustomizedReport())
                {
                    buyerMsgSettingReportEx.setCustomizedReportTemplate(buyerMsgSettingReportEx.getReportTemplate());
                }
                else
                {
                    buyerMsgSettingReportEx.setStandardReportTemplate(buyerMsgSettingReportEx.getReportTemplate());
                }
                result.put(subType, buyerMsgSettingReportEx);
            }
        }
        
        return result;
    }


    @SuppressWarnings("unchecked")
    public void validateSaveEditMsgSetting()
    {
        try
        {
            boolean flag = this.hasActionErrors();
            if (!flag)
            {
                Iterator<String> it = inbounds.keySet().iterator();
                while (it.hasNext())
                {
                    String key = it.next().toString();
                    BuyerMsgSettingExHolder obj = inbounds.get(key);
                    obj.trimAllString();
                    obj.setAllEmptyStringToNull();
                    if (FrequencyType.INTERVAL.name().equals(
                            obj.getAlertFrequency()))
                    {
                        if (obj.getAlertIntervalValue() == null
                                || ""
                                        .equals(obj.getAlertIntervalValue()
                                                .trim()))
                        {
                            this.addActionError(this.getText("B2BPC0329",
                                    new String[] { key }));
                            flag = true;
                        }
                        if (!flag
                                && (!Pattern.matches("[0-9]{1,6}", obj
                                        .getAlertIntervalValue().trim())
                                        || Integer
                                                .parseInt(obj
                                                        .getAlertIntervalValue()
                                                        .trim()) <= 0 || Integer
                                        .parseInt(obj.getAlertIntervalValue()
                                                .trim()) > 24))
                        {
                            this.addActionError(this.getText("B2BPC0330",
                                    new String[] { key }));
                            flag = true;
                        }
                        if (!flag)
                        {
                            obj.setAlertInterval(Short.parseShort(obj.getAlertIntervalValue().trim()));
                        }
                    }
                    else
                    {
                        obj.setAlertInterval(null);
                    }
                    if (obj.getRcpsAddrs() != null && !checkEmail(obj.getRcpsAddrs()))
                    {
                        this.addActionError(this.getText("B2BPC0333",
                                new String[] { key }));
                        flag = true;
                    }
                    if (obj.getErrorRcpsAddrs() != null && !checkEmail(obj.getErrorRcpsAddrs()))
                    {
                        this.addActionError(this.getText("B2BPC0384",
                                new String[] { key }));
                        flag = true;
                    }
                }

                it = outbounds.keySet().iterator();
                while (it.hasNext())
                {
                    String key = it.next().toString();
                    BuyerMsgSettingExHolder obj = outbounds.get(key);
                    obj.trimAllString();
                    obj.setAllEmptyStringToNull();
                    if (obj.getRcpsAddrs() != null && !checkEmail(obj.getRcpsAddrs()))
                    {
                        this.addActionError(this.getText("B2BPC0333",
                                new String[] { key }));
                        flag = true;
                    }
                    if (obj.getErrorRcpsAddrs() != null && !checkEmail(obj.getErrorRcpsAddrs()))
                    {
                        this.addActionError(this.getText("B2BPC0384",
                                new String[] { key }));
                        flag = true;
                    }
                }
                
                if (!checkEmail(mpigdMatchedRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0353"));
                    flag = true;
                }
                if (!checkEmail(mpigdUnmatchedRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0354"));
                    flag = true;
                }
                if (!checkEmail(mpigdDefaultRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0355"));
                    flag = true;
                }
                if (!checkEmail(mpigdMatchingJobRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0356"));
                    flag = true;
                }
                if (!checkEmail(mpigdResolutionRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0357"));
                    flag = true;
                }
                if (!checkEmail(mpigdOutstandingRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0358"));
                    flag = true;
                }
                if (!checkEmail(mpigdInvoiceExportingRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0359"));
                    flag = true;
                }
                if (!checkEmail(mpigdMissingGrnNotificationRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0362"));
                    flag = true;
                }
                if (!checkEmail(mpigdPriceDiscrepancyRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0379"));
                    flag = true;
                }
                if (!checkEmail(mpigdQtyDiscrepancyRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0380"));
                    flag = true;
                }
                if (!checkEmail(dbResolutionRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0360"));
                    flag = true;
                }
                if (!checkEmail(dbOutstandingRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0361"));
                    flag = true;
                }
                if (!checkEmail(dbPriceDiscrepancyRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0381"));
                    flag = true;
                }
                if (!checkEmail(dbQtyDiscrepancyRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0382"));
                    flag = true;
                }
                if (!checkEmail(dbDnExportingRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0383"));
                    flag = true;
                }
                if (!checkEmail(mpigdMissingGiNotificationRecipients.getRuleValue()))
                {
                    this.addActionError(this.getText("B2BPC0376"));
                    flag = true;
                }
            }
            if (flag)
            {
                param = buyerService.selectBuyerByKey(param.getBuyerOid());
                inboundMsg = (List<String>) this.getSession().get(
                        SESSION_BUYER_MSG_SETTING_INBOUND);
                outboundMsg = (List<String>) this.getSession().get(
                        SESSION_BUYER_MSG_SETTING_OUTBOUND);
                if(inbounds != null)
                {
                    for(String key : inbounds.keySet())
                    {
                        BuyerMsgSettingExHolder setting = inbounds.get(key);
                        Map<String, BuyerMsgSettingReportExHolder> subTypeReportMap = setting.getSubTypeReportMap();
                        if (subTypeReportMap != null && !subTypeReportMap.isEmpty())
                        {
                            for (Map.Entry<String, BuyerMsgSettingReportExHolder> entry : subTypeReportMap.entrySet())
                            {
                                BuyerMsgSettingReportExHolder buyerMsgSettingReport  = entry.getValue();
                                buyerMsgSettingReport.setSubType(entry.getKey());
                                if (null == buyerMsgSettingReport.getCustomizedReport())
                                {
                                    buyerMsgSettingReport.setCustomizedReport(false);
                                }
                                buyerMsgSettingReport.setCustomized(appConfig.getCustomizedReports(param.getBuyerCode(), key, buyerMsgSettingReport.getSubType()));
                                buyerMsgSettingReport.setStandard(appConfig.getStandardReports(key, buyerMsgSettingReport.getSubType()));
                            }
                        }
                        setting.setFileFormatList(appConfig.getFileFormatListByMsgType(key));
                    }
                }
                if(outbounds != null)
                {
                    for(String key : outbounds.keySet())
                    {
                        BuyerMsgSettingExHolder setting = outbounds.get(key);
                        Map<String, BuyerMsgSettingReportExHolder> subTypeReportMap = setting.getSubTypeReportMap();
                        if (subTypeReportMap != null && !subTypeReportMap.isEmpty())
                        {
                            for (Map.Entry<String, BuyerMsgSettingReportExHolder> entry : subTypeReportMap.entrySet())
                            {
                                BuyerMsgSettingReportExHolder buyerMsgSettingReport  = entry.getValue();
                                buyerMsgSettingReport.setSubType(entry.getKey());
                                if (null == buyerMsgSettingReport.getCustomizedReport())
                                {
                                    buyerMsgSettingReport.setCustomizedReport(false);
                                }
                                buyerMsgSettingReport.setCustomized(appConfig.getCustomizedReports(param.getBuyerCode(), key, buyerMsgSettingReport.getSubType()));
                                buyerMsgSettingReport.setStandard(appConfig.getStandardReports(key, buyerMsgSettingReport.getSubType()));
                            }
                        }
                        setting.setFileFormatList(appConfig.getFileFormatListByMsgType(key));
                    }
                }
                setMsgDescAndBasicFlag(inbounds);
                setMsgDescAndBasicFlag(outbounds);
                
                List<BusinessRuleHolder> businessRules = businessRuleService
                        .select(new BusinessRuleHolder());
                if(businessRules == null || businessRules.isEmpty())
                {
                    return;
                }
                Iterator<BusinessRuleHolder> it = businessRules.iterator();
                while(it.hasNext())
                {
                    BusinessRuleHolder obj = it.next();
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "MatchedRecipients".equals(obj.getRuleId()))
                    {
                        mpigdMatchedRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "UnmatchedRecipients".equals(obj.getRuleId()))
                    {
                        mpigdUnmatchedRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "DefaultRecipients".equals(obj.getRuleId()))
                    {
                        mpigdDefaultRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "MatchingJobRecipients".equals(obj.getRuleId()))
                    {
                        mpigdMatchingJobRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "ResolutionRecipients".equals(obj.getRuleId()))
                    {
                        mpigdResolutionRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "OutstandingRecipients".equals(obj.getRuleId()))
                    {
                        mpigdOutstandingRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "InvoiceExportingRecipients".equals(obj.getRuleId()))
                    {
                        mpigdInvoiceExportingRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "MissingGrnNotificationRecipients".equals(obj.getRuleId()))
                    {
                        mpigdMissingGrnNotificationRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "PriceDiscrepancyRecipients".equals(obj.getRuleId()))
                    {
                        mpigdPriceDiscrepancyRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "QtyDiscrepancyRecipients".equals(obj.getRuleId()))
                    {
                        mpigdQtyDiscrepancyRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "ResolutionRecipients".equals(obj.getRuleId()))
                    {
                        dbResolutionRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "OutstandingRecipients".equals(obj.getRuleId()))
                    {
                        dbOutstandingRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "PriceDiscrepancyRecipients".equals(obj.getRuleId()))
                    {
                        dbPriceDiscrepancyRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "QtyDiscrepancyRecipients".equals(obj.getRuleId()))
                    {
                        dbQtyDiscrepancyRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                    if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "DnExportingRecipients".equals(obj.getRuleId()))
                    {
                        dbDnExportingRecipients.setRuleDesc(obj.getRuleDesc());
                    }
                }
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }


    public String saveEditMsgSetting()
    {
        try
        {
            List<BuyerMsgSettingHolder> msgItems = new ArrayList<BuyerMsgSettingHolder>();

            Iterator<String> it = outbounds.keySet().iterator();
            while (it.hasNext())
            {
                String key = it.next().toString();
                BuyerMsgSettingExHolder obj = outbounds.get(key);
                obj.setBuyerOid(param.getBuyerOid());
                obj.setAlertFrequency(FrequencyType.BATCH.name());
                obj.setExcludeSucc(obj.getExcludeSucc() == null ? false : true);
                
                Map<String, BuyerMsgSettingReportExHolder> subTypeReportMap = obj.getSubTypeReportMap();
                if (subTypeReportMap != null && !subTypeReportMap.isEmpty())
                {
                    for (Map.Entry<String, BuyerMsgSettingReportExHolder> entry : subTypeReportMap.entrySet())
                    {
                        BuyerMsgSettingReportExHolder buyerMsgSettingReport  = entry.getValue();
                        buyerMsgSettingReport.setMsgType(key);
                        buyerMsgSettingReport.setSubType(entry.getKey());
                        buyerMsgSettingReport.setBuyerOid(param.getBuyerOid());
                        if (null == buyerMsgSettingReport.getCustomizedReport())
                        {
                            buyerMsgSettingReport.setCustomizedReport(false);
                        }
                        if (buyerMsgSettingReport.getCustomizedReport())
                        {
                            buyerMsgSettingReport.setReportTemplate(buyerMsgSettingReport.getCustomizedReportTemplate());
                        }
                        else
                        {
                            buyerMsgSettingReport.setReportTemplate(buyerMsgSettingReport.getStandardReportTemplate());
                        }
                    }
                }
                
                obj.setMsgType(key);
                msgItems.add(obj);
            }

            it = inbounds.keySet().iterator();
            while (it.hasNext())
            {
                String key = it.next().toString();
                BuyerMsgSettingExHolder obj = inbounds.get(key);
                obj.setBuyerOid(param.getBuyerOid());
                obj.setExcludeSucc(false);
                Map<String, BuyerMsgSettingReportExHolder> subTypeReportMap = obj.getSubTypeReportMap();
                if (subTypeReportMap != null && !subTypeReportMap.isEmpty())
                {
                    for (Map.Entry<String, BuyerMsgSettingReportExHolder> entry : subTypeReportMap.entrySet())
                    {
                        BuyerMsgSettingReportExHolder buyerMsgSettingReport  = entry.getValue();
                        buyerMsgSettingReport.setMsgType(key);
                        buyerMsgSettingReport.setSubType(entry.getKey());
                        buyerMsgSettingReport.setBuyerOid(param.getBuyerOid());
                        if (null == buyerMsgSettingReport.getCustomizedReport())
                        {
                            buyerMsgSettingReport.setCustomizedReport(false);
                        }
                        if (buyerMsgSettingReport.getCustomizedReport())
                        {
                            buyerMsgSettingReport.setReportTemplate(buyerMsgSettingReport.getCustomizedReportTemplate());
                        }
                        else
                        {
                            buyerMsgSettingReport.setReportTemplate(buyerMsgSettingReport.getStandardReportTemplate());
                        }
                    }
                }
                obj.setMsgType(key);
                msgItems.add(obj);
            }

            param = buyerService.selectBuyerByKey(param.getBuyerOid());
            List<BuyerMsgSettingHolder> buyerMsgSettings = buyerMsgSettingService
                    .selectBuyerMsgSettingsByBuyerOid(param.getBuyerOid());
            if (buyerMsgSettings != null && !buyerMsgSettings.isEmpty())
            {
                for (BuyerMsgSettingHolder o : buyerMsgSettings)
                {
                    param.addBuyerMsgSetting(o);
                }
            }
            BuyerHolder newBuyer = new BuyerHolder();
            BeanUtils.copyProperties(param, newBuyer);
            newBuyer.setMsgSetting(msgItems);
            
            List<BuyerRuleHolder> oldBuyerRules = buyerRuleService
                    .selectByBuyerOid(param.getBuyerOid());
            List<BuyerRuleHolder> newBuyerRules = new ArrayList<BuyerRuleHolder>();
            newBuyerRules.addAll(oldBuyerRules);
            initNewBuyerRulesWhenEditMsgSetting(oldBuyerRules, newBuyerRules);
            param.setBuyerRules(oldBuyerRules);
            newBuyer.setBuyerRules(newBuyerRules);
            
            buyerService.updateBuyerWithMsgSetting(this.getCommonParameter(),
                    param, newBuyer);
            log.info(this.getText("B2BPC0349", new String[]{param.getBuyerCode(), this.getLoginIdOfCurrentUser()}));
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            msg.saveSuccess(this.getText("B2BPC0331", new String[] { param
                    .getBuyerName() }));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

            msg.addMessageTarget(mt);
        }
        catch (Exception e)
        {
            this.handleException(e);
        }
        return FORWARD_COMMON_MESSAGE;
    }
    
    private void setMsgDescAndBasicFlag(Map<String, BuyerMsgSettingExHolder> bound)
    {
        if (bound == null || bound.isEmpty())
        {
            return;
        }
        String key = null;
        for (Map.Entry<String, BuyerMsgSettingExHolder> entry : bound.entrySet())
        {
            key = entry.getKey();
            entry.getValue().setBasicData(appConfig.isBasicData(key));
            if ("PO".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.PO"));
            }
            else if ("GRN".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.GRN"));
            }
            else if ("RTV".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.RTV"));
            }
            else if ("DN".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.DN"));
            }
            else if ("PN".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.PN"));
            }
            else if ("INV".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.INV"));
            }
            else if ("DO".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.DO"));
            }
            else if ("SM".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.SM"));
            }
            else if ("ST".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.ST"));
            }
            else if ("UM".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.UM"));
            }
            else if ("SA".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.SA"));
            }
            else if ("DPR".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.DPR"));
            }
            else if ("ITEMIN".equalsIgnoreCase(key))
            {
            	bound.get(key).setMsgDesc(this.getText("MsgType.ITEMIN"));
            }
            else if ("CC".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.CC"));
            }
            else if ("DSD".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.DSD"));
            }
            else if ("GI".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.GI"));
            }
            else if ("IM".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.IM"));
            }
            else if ("POCN".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.POCN"));
            }
            else if ("CN".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.CN"));
            }
            else if ("SL".equalsIgnoreCase(key))
            {
                bound.get(key).setMsgDesc(this.getText("MsgType.SL"));
            }
        }
    }


    // *****************************************************
    // view logo image
    // *****************************************************
    public String viewImage()
    {
        try
        {
            param = buyerService.selectBuyerWithBlobsByKey(param.getBuyerOid());
            byte[] image = param.getLogo();
            if (image != null)
            {
                contentType = "multipart/form-data";
                imageResult = new java.io.ByteArrayInputStream(image);
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        return SUCCESS;
    }


    // *****************************************************
    // edit business rule
    // *****************************************************
    public String initEditBusinessRule() throws Exception
    {
        try
        {
            param = buyerService.selectBuyerByKey(param.getBuyerOid());
            funcGroups = businessRuleService.selectFuncGroups();
            funcIds = businessRuleService.selectFuncIdsByGroup(null);
            initBusinessRules(param.getBuyerOid());
        }
        catch(Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }

    public String getFuncIdSByGroup() throws Exception
    {
        try
        {
            funcIds = businessRuleService.selectFuncIdsByGroup(funcGroup);
        }
        catch(Exception e)
        {
            handleException(e);
        }
        return SUCCESS;
    }
    
    public String checkPoInvGrnDnMatching() throws Exception
    {
        boolean flag = false;
        if (!flag && mpigdAmountTolerance.getRuleValue() != null && !mpigdAmountTolerance.getRuleValue().trim().isEmpty())
        {
            try
            {
                BigDecimal amountTolerance = new BigDecimal(mpigdAmountTolerance.getRuleValue());
                if (amountTolerance.compareTo(BigDecimal.ZERO) < 0)
                {
                    result = this.getText("B2BPC0345");
                    flag = true;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0345");
                flag = true;
            }
            
        }
        return SUCCESS;
    }
    
    
    private boolean checkEmail(String emailStr) throws Exception
    {
        if (emailStr == null || emailStr.trim().isEmpty())
        {
            return true;
        }
        String emailPattern = appConfig.getEmailPattern();
        String[] emails = emailStr.trim().split(",");
        for (String email : emails)
        {
            if (!Pattern.matches(emailPattern, email.trim()))
            {
                return false;
            }
        }
        return true;
    }
    
    
    public String checkGlobal() throws Exception
    {
        if (!ggAutoLogout.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggAutoLogout.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0351");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0351");
                return SUCCESS;
            }
        }
        if (!ggMatchingJobMinBufferingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggMatchingJobMinBufferingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0368");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0368");
                return SUCCESS;
            }
        }
        if (!ggMatchingJobMaxBufferingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggMatchingJobMaxBufferingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0369");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0369");
                return SUCCESS;
            }
        }
        if (!ggDailyPoReportJobDaysBefore.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggDailyPoReportJobDaysBefore.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0370");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0370");
                return SUCCESS;
            }
        }
        if (!ggDailyNotificationJobMissingGrnMinBufferingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggDailyNotificationJobMissingGrnMinBufferingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0371");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0371");
                return SUCCESS;
            }
        }
        if (!ggDailyNotificationJobMissingGrnMaxBufferingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggDailyNotificationJobMissingGrnMaxBufferingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0372");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0372");
                return SUCCESS;
            }
        }
        if (!ggDnGeneratingJobMatchingMaxBuffingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggDnGeneratingJobMatchingMaxBuffingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0373");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0373");
                return SUCCESS;
            }
        }
        if (!ggDnGeneratingJobMatchingMinBuffingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggDnGeneratingJobMatchingMinBuffingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0375");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0375");
                return SUCCESS;
            }
        }
        if (!ggRTVDnGeneratingJobBuffingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggRTVDnGeneratingJobBuffingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0374");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0374");
                return SUCCESS;
            }
        }
        if (!ggRTVDnDisputeAlertWindow.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggRTVDnDisputeAlertWindow.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0385");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0385");
                return SUCCESS;
            }
        }
        if (!ggDailyNotificationJobMissingGiMinBufferingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggDailyNotificationJobMissingGiMinBufferingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0377");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0377");
                return SUCCESS;
            }
        }
        if (!ggDailyNotificationJobMissingGiMaxBufferingDays.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggDailyNotificationJobMissingGiMaxBufferingDays.getRuleValue());
                if (value < 0)
                {
                    result = this.getText("B2BPC0378");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0378");
                return SUCCESS;
            }
        }
        if (!ggRtvGiDnPriceTolerance.getRuleValue().trim().isEmpty())
        {
            try
            {
                if (ggRtvGiDnPriceToleranceType.getRuleValue().equalsIgnoreCase("false"))
                {
                    BigDecimal value = BigDecimalUtil.getInstance().convertStringToBigDecimal(ggRtvGiDnPriceTolerance.getRuleValue(), 2);
                    if (value.compareTo(new BigDecimal(100)) > 1 || value.compareTo(new BigDecimal(0)) < 0)
                    {
                        result = this.getText("B2BPC0388");
                        return SUCCESS;
                    }
                }
                else
                {
                    BigDecimal value = BigDecimalUtil.getInstance().convertStringToBigDecimal(ggRtvGiDnPriceTolerance.getRuleValue(), 2);
                    if (value.compareTo(BigDecimal.ZERO) < 0)
                    {
                        result = this.getText("B2BPC0389");
                        return SUCCESS;
                    }
                }
            }
            catch (Exception e)
            {
                if (ggRtvGiDnPriceToleranceType.getRuleValue().equalsIgnoreCase("false"))
                {
                    result = this.getText("B2BPC0388");
                    return SUCCESS;
                }
                else
                {
                    result = this.getText("B2BPC0389");
                    return SUCCESS;
                }
            }
        }
        if (!ggRtvGiDnQtyTolerance.getRuleValue().trim().isEmpty())
        {
            try
            {
                if (ggRtvGiDnQtyToleranceType.getRuleValue().trim().equalsIgnoreCase("false"))
                {
                    BigDecimal value = BigDecimalUtil.getInstance().convertStringToBigDecimal(ggRtvGiDnQtyTolerance.getRuleValue(), 2);
                    if (value.compareTo(new BigDecimal(100)) > 1 || value.compareTo(new BigDecimal(0)) < 0)
                    {
                        result = this.getText("B2BPC0388");
                        return SUCCESS;
                    }
                }
                else
                {
                    BigDecimal value = BigDecimalUtil.getInstance().convertStringToBigDecimal(ggRtvGiDnQtyTolerance.getRuleValue(), 2);
                    if (value.compareTo(BigDecimal.ZERO) < 0)
                    {
                        result = this.getText("B2BPC0389");
                        return SUCCESS;
                    }
                }
            }
            catch (Exception e)
            {
                if (ggRtvGiDnPriceToleranceType.getRuleValue().trim().equalsIgnoreCase("false"))
                {
                    result = this.getText("B2BPC0388");
                    return SUCCESS;
                }
                else
                {
                    result = this.getText("B2BPC0389");
                    return SUCCESS;
                }
            }
        }
        
        if (!ggRtvGiDnReportGeneratingDateRange.getRuleValue().trim().isEmpty())
        {
            try
            {
                int value = Integer.parseInt(ggRtvGiDnReportGeneratingDateRange.getRuleValue().trim());
                if (value < 0)
                {
                    result = this.getText("B2BPC0390");
                    return SUCCESS;
                }
            }
            catch (Exception e)
            {
                result = this.getText("B2BPC0390");
                return SUCCESS;
            }
        }
        
        return SUCCESS;
    }
    
    
    public String checkPoGlobal() throws Exception
    {
        if (pgDeliveryDateRange.getRuleValue().trim().isEmpty())
        {
            return SUCCESS;
        }
        try
        {
            if (Integer.parseInt(pgDeliveryDateRange.getRuleValue()) < 0)
            {
                result = this.getText("B2BPC0367");
            }
        }
        catch (Exception e)
        {
            result = this.getText("B2BPC0367");
        }
        return SUCCESS;
    }
    
    
    public String checkImportMatching() throws Exception
    {
        if (smiAdminRole.getRuleValue().trim().isEmpty())
        {
            return SUCCESS;
        }
        try
        {
            Pattern pattern = Pattern.compile(validationConfig
                    .getCachePattern(VLD_PTN_KEY));
            Matcher matcher = pattern.matcher(smiAdminRole.getRuleValue().trim());
            if (!matcher.matches())
            {
                result = this.getText("B2BPC0366");
            }
        }
        catch (Exception e)
        {
            result = this.getText("B2BPC0366");
        }
        return SUCCESS;
    }

    
    public String saveBusinessRule() throws Exception
    {
        try
        {
            BuyerHolder oldBuyer = buyerService.selectBuyerByKey(param
                .getBuyerOid());
            BuyerHolder newBuyer = new BuyerHolder();
            List<BuyerRuleHolder> oldBuyerRules = buyerRuleService
                .selectByBuyerOid(oldBuyer.getBuyerOid());
            List<BuyerRuleHolder> newBuyerRules = new ArrayList<BuyerRuleHolder>();
            newBuyerRules.addAll(oldBuyerRules);
            BeanUtils.copyProperties(oldBuyer, newBuyer);
            initNewBuyerRules(oldBuyerRules, newBuyerRules);
            oldBuyer.setBuyerRules(oldBuyerRules);
            newBuyer.setBuyerRules(newBuyerRules);
            buyerService.updateBuyerWithBuyerRule(this.getCommonParameter(),
                oldBuyer, newBuyer);
            result = this.getText("B2BPC0343", new String[] {oldBuyer
                .getBuyerName()});
            log.info(this.getText("B2BPC0350", new String[]{oldBuyer.getBuyerCode(), this.getLoginIdOfCurrentUser()}));
        }
        catch(Exception e)
        {
            handleException(e);
            result = this.getText("B2BPC0344");
        }
        return SUCCESS;
    }

    
    private void initNewBuyerRules(List<BuyerRuleHolder> oldBuyerRules,
        List<BuyerRuleHolder> newBuyerRules) throws Exception
    {
        if(ggAutoLogout != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggAutoLogout,
                "ggAutoLogout", 2);
        }
        if(ggMatchingJobMinBufferingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggMatchingJobMinBufferingDays,
                    "ggMatchingJobMinBufferingDays", 2);
        }
        if(ggMatchingJobMaxBufferingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggMatchingJobMaxBufferingDays,
                    "ggMatchingJobMaxBufferingDays", 2);
        }
        if(ggDailyPoReportJobDaysBefore != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggDailyPoReportJobDaysBefore,
                    "ggDailyPoReportJobDaysBefore", 2);
        }
        if(ggDailyNotificationJobMissingGrnMinBufferingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggDailyNotificationJobMissingGrnMinBufferingDays,
                    "ggDailyNotificationJobMissingGrnMinBufferingDays", 2);
        }
        if(ggDailyNotificationJobMissingGrnMaxBufferingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggDailyNotificationJobMissingGrnMaxBufferingDays,
                    "ggDailyNotificationJobMissingGrnMaxBufferingDays", 2);
        }
        if(ggDnGeneratingJobMatchingMaxBuffingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggDnGeneratingJobMatchingMaxBuffingDays,
                    "ggDnGeneratingJobMatchingMaxBuffingDays", 2);
        }
        if(ggDnGeneratingJobMatchingMinBuffingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggDnGeneratingJobMatchingMinBuffingDays,
                    "ggDnGeneratingJobMatchingMinBuffingDays", 2);
        }
        if(ggRTVDnGeneratingJobBuffingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggRTVDnGeneratingJobBuffingDays,
                    "ggRTVDnGeneratingJobBuffingDays", 2);
        }
        if(ggRTVDnDisputeAlertWindow != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggRTVDnDisputeAlertWindow,
                    "ggRTVDnDisputeAlertWindow", 2);
        }
        if(ggContinueProcessErrorBatch != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggContinueProcessErrorBatch,
            "ggContinueProcessErrorBatch", 0);
        }
        if(ggDisableInvoicePaymentInstructions != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggDisableInvoicePaymentInstructions,
                    "ggDisableInvoicePaymentInstructions", 2);
        }
        if(ggRtvGiDnQtyTolerance != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggRtvGiDnQtyTolerance,
                    "ggRtvGiDnQtyTolerance", 2);
        }
        if(ggRtvGiDnQtyToleranceType != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggRtvGiDnQtyToleranceType,
                    "ggRtvGiDnQtyToleranceType", 2);
        }
        if(ggRtvGiDnPriceTolerance != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggRtvGiDnPriceTolerance,
                    "ggRtvGiDnPriceTolerance", 2);
        }
        if(ggRtvGiDnPriceToleranceType != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggRtvGiDnPriceToleranceType,
                    "ggRtvGiDnPriceToleranceType", 2);
        }
        if(ggRtvGiDnReportGeneratingDateRange != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggRtvGiDnReportGeneratingDateRange,
                    "ggRtvGiDnReportGeneratingDateRange", 2);
        }
        if(pcispQtyEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, pcispQtyEditable,
                    "pcispQtyEditable", 0);
        }
        if(pcispQtylessThanPO != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, pcispQtylessThanPO,
                "pcispQtylessThanPO", 0);
        }
        if(pcispFocQtyEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, pcispFocQtyEditable,
                "pcispFocQtyEditable", 0);
        }
        if(pcispDiscountEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, pcispDiscountEditable,
                    "pcispDiscountEditable", 0);
        }
        if(pcispDiscountForDetailEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, pcispDiscountForDetailEditable,
                    "pcispDiscountForDetailEditable", 0);
        }
        if(pcispCashDiscountEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, pcispCashDiscountEditable,
                    "pcispCashDiscountEditable", 0);
        }
        if(pcispFocQtylessThanPO != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                pcispFocQtylessThanPO, "pcispFocQtylessThanPO", 0);
        }
        if(pcispUnitPriceEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                pcispUnitPriceEditable, "pcispUnitPriceEditable", 0);
        }
        if(pcispEmailToStore != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pcispEmailToStore, "pcispEmailToStore", 0);
        }
        if(pcispPdfAsAttachment != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pcispPdfAsAttachment, "pcispPdfAsAttachment", 0);
        }
        if(pcispUnitPriceLessThanPO != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                pcispUnitPriceLessThanPO, "pcispUnitPriceLessThanPO", 0);
        }
        if(pcicpItemDiscountEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pcicpItemDiscountEditable, "pcicpItemDiscountEditable", 0);
        }
        if(pcicpItemAmountEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pcicpItemAmountEditable, "pcicpItemAmountEditable", 0);
        }
        if(pcicpItemSharedCostEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pcicpItemSharedCostEditable, "pcicpItemSharedCostEditable", 0);
        }
        if(pcicpTradeDiscountEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pcicpTradeDiscountEditable, "pcicpTradeDiscountEditable", 0);
        }
        if(pcicpCashDiscountEditable != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pcicpCashDiscountEditable, "pcicpCashDiscountEditable", 0);
        }
        if(pcicpIgnoreExpiryDate != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pcicpIgnoreExpiryDate, "pcicpIgnoreExpiryDate", 0);
        }
        if(pgDeliveryDateRange != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules,
                    pgDeliveryDateRange, "pgDeliveryDateRange", 2);
        }
        if(dbAutoGenStockDn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbAutoGenStockDn,
                "dbAutoGenStockDn", 0);
        }
        if(dbAutoGenCostDn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbAutoGenCostDn,
            "dbAutoGenCostDn", 0);
        }
        if(dbAutoSendStockDn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbAutoSendStockDn,
            "dbAutoSendStockDn", 0);
        }
        if(dbAutoSendCostDn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbAutoSendCostDn,
            "dbAutoSendCostDn", 0);
        }
        if(dnNoStyle1 != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dnNoStyle1,
                    "dnNoStyle1", 2);
        }
        if(dbUnityFileStype != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbUnityFileStype,
                    "dbUnityFileStype", 2);
        }
        if(dbNeedTranslate != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbNeedTranslate,
                    "dbNeedTranslate", 2);
        }
        if(dbAutoGenDnFromGI != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbAutoGenDnFromGI,
                    "dbAutoGenDnFromGI", 0);
        }
        if(dbAllowSupplierDisputeMatchingDn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbAllowSupplierDisputeMatchingDn,
                    "dbAllowSupplierDisputeMatchingDn", 0);
        }
        if(dbDiscrepancyReportToUser != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbDiscrepancyReportToUser,
                    "dbDiscrepancyReportToUser", 0);
        }
        if(dbAutoCloseAcceptedRecord != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbAutoCloseAcceptedRecord,
                    "dbAutoCloseAcceptedRecord", 0);
        }
        if(dbSendResolutionAndOutstandingByGroup != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbSendResolutionAndOutstandingByGroup,
                    "dbSendResolutionAndOutstandingByGroup", 0);
        }
        if(dbResolutionRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbResolutionRecipients,
                    "dbResolutionRecipients", 2);
        }
        if(dbOutstandingRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbOutstandingRecipients,
                    "dbOutstandingRecipients", 2);
        }
        if(dbPriceDiscrepancyRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbPriceDiscrepancyRecipients,
                    "dbPriceDiscrepancyRecipients", 2);
        }
        if(dbQtyDiscrepancyRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbQtyDiscrepancyRecipients,
                    "dbQtyDiscrepancyRecipients", 2);
        }
        if(dbDnExportingRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dbDnExportingRecipients,
                    "dbDnExportingRecipients", 2);
        }
        if(mpigdQtyInvLessGrn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdQtyInvLessGrn,
                "mpigdQtyInvLessGrn", 0);
        }
        if(mpigdPriceInvLessPo != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdPriceInvLessPo,
                    "mpigdPriceInvLessPo", 0);
        }
        if(mpigdQtyPoLessGrn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdQtyPoLessGrn,
                    "mpigdQtyPoLessGrn", 0);
        }
        if(mpigdAutoAcceptQtyInvLessGrn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdAutoAcceptQtyInvLessGrn,
                    "mpigdAutoAcceptQtyInvLessGrn", 0);
        }
        if(mpigdAutoAcceptPriceInvLessPo != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdAutoAcceptPriceInvLessPo,
                    "mpigdAutoAcceptPriceInvLessPo", 0);
        }
        if(mpigdAmountTolerance != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdAmountTolerance,
            "mpigdAmountTolerance", 1);
        }
        if(mpigdMatchedRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdMatchedRecipients,
            "mpigdMatchedRecipients", 2);
        }
        if(mpigdUnmatchedRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdUnmatchedRecipients,
            "mpigdUnmatchedRecipients", 2);
        }
        if(mpigdEnableSupplierToDispute != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdEnableSupplierToDispute,
                    "mpigdEnableSupplierToDispute", 2);
        }
        if(mpigdAutoApproveClosedAcceptedRecord != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdAutoApproveClosedAcceptedRecord,
                    "mpigdAutoApproveClosedAcceptedRecord", 2);
        }
        if(mpigdAutoCloseAcceptedRecord != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdAutoCloseAcceptedRecord,
                    "mpigdAutoCloseAcceptedRecord", 2);
        }
        if(mpigdAutoCloseRejectedRecord != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdAutoCloseRejectedRecord,
                    "mpigdAutoCloseRejectedRecord", 2);
        }
        if(mpigdChangeInvDateToGrnDate != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdChangeInvDateToGrnDate,
                    "mpigdChangeInvDateToGrnDate", 2);
        }
        if(mpigdSkipMatching != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdSkipMatching,
                    "mpigdSkipMatching", 2);
        }
        if(mpigdDiscrepancyReportToUser != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdDiscrepancyReportToUser,
                    "mpigdDiscrepancyReportToUser", 2);
        }
        if(mpigdAutoRejectBuyerLossUnmatchedRecord != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdAutoRejectBuyerLossUnmatchedRecord,
                    "mpigdAutoRejectBuyerLossUnmatchedRecord", 2);
        }
        if(mpigdSendResolutionAndOutstandingByGroup != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdSendResolutionAndOutstandingByGroup,
                    "mpigdSendResolutionAndOutstandingByGroup", 2);
        }
        if(mpigdDefaultRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdDefaultRecipients,
                    "mpigdDefaultRecipients", 2);
        }
        if(mpigdMatchingJobRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdMatchingJobRecipients,
                    "mpigdMatchingJobRecipients", 2);
        }
        if(mpigdResolutionRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdResolutionRecipients,
                    "mpigdResolutionRecipients", 2);
        }
        if(mpigdOutstandingRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdOutstandingRecipients,
                    "mpigdOutstandingRecipients", 2);
        }
        if(mpigdInvoiceExportingRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdInvoiceExportingRecipients,
                    "mpigdInvoiceExportingRecipients", 2);
        }
        if(mpigdMissingGrnNotificationRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdMissingGrnNotificationRecipients,
                    "mpigdMissingGrnNotificationRecipients", 2);
        }
        if(mpigdPriceDiscrepancyRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdPriceDiscrepancyRecipients,
                    "mpigdPriceDiscrepancyRecipients", 2);
        }
        if(mpigdQtyDiscrepancyRecipients != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdQtyDiscrepancyRecipients,
                    "mpigdQtyDiscrepancyRecipients", 2);
        }
        if(mpigdAutoApproveMatchedByDn != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, mpigdAutoApproveMatchedByDn,
                    "mpigdAutoApproveMatchedByDn", 2);
        }
        if(smiGenAdminUser != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, smiGenAdminUser,
                    "smiGenAdminUser", 2);
        }
        if(smiAdminRole != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, smiAdminRole,
                    "smiAdminRole", 2);
        }
        if(smiGenResultTxt != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, smiGenResultTxt,
                    "smiGenResultTxt", 2);
        }
        if(iiUpdate != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, iiUpdate,
                    "iiUpdate", 2);
        }
        if(iiDeleteAndInsert != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, iiDeleteAndInsert,
                    "iiDeleteAndInsert", 2);
        }
        if(iiSelectOneToCompare != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, iiSelectOneToCompare,
                    "iiSelectOneToCompare", 2);
        }
        if(ggSupplierCanDisputeGRN != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggSupplierCanDisputeGRN,
                    "ggSupplierCanDisputeGRN", 2);
        }
        if(ggPreventItemsNotExistInPO != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggPreventItemsNotExistInPO,
                    "ggPreventItemsNotExistInPO", 2);
        }
        if(ggPreventItemsLessThanPO != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggPreventItemsLessThanPO,
                    "ggPreventItemsLessThanPO", 2);
        }
        if(ggPreventItemsQtyMoreThanPO != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggPreventItemsQtyMoreThanPO,
                    "ggPreventItemsQtyMoreThanPO", 2);
        }
        if(igPreventItemsNotExistInPO != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, igPreventItemsNotExistInPO,
                    "igPreventItemsNotExistInPO", 2);
        }
        if(gigPreventItemsNotExistInRtv != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, gigPreventItemsNotExistInRtv,
                    "gigPreventItemsNotExistInRtv", 2);
        }
        if(gigPreventItemsLessThanRtv != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, gigPreventItemsLessThanRtv,
                    "gigPreventItemsLessThanRtv", 2);
        }
        if(gigPreventItemsQtyMoreThanRtv != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, gigPreventItemsQtyMoreThanRtv,
                    "gigPreventItemsQtyMoreThanRtv", 2);
        }
        if(pgNeedValidateConPo != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, pgNeedValidateConPo,
                    "pgNeedValidateConPo", 2);
        }
        if(dsdNeedValidateSalesData != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, dsdNeedValidateSalesData,
                    "dsdNeedValidateSalesData", 2);
        }
        if(ggDailyNotificationJobMissingGiMinBufferingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggDailyNotificationJobMissingGiMinBufferingDays,
                    "ggDailyNotificationJobMissingGiMinBufferingDays", 2);
        }
        if(ggDailyNotificationJobMissingGiMaxBufferingDays != null)
        {
            addNewBuyerRule(oldBuyerRules, newBuyerRules, ggDailyNotificationJobMissingGiMaxBufferingDays,
                    "ggDailyNotificationJobMissingGiMaxBufferingDays", 2);
        }
    }
    
    private void initNewBuyerRulesWhenEditMsgSetting(List<BuyerRuleHolder> oldBuyerRules,
            List<BuyerRuleHolder> newBuyerRules) throws Exception
    {
    
        if(mpigdMatchedRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdMatchedRecipients,
            "mpigdMatchedRecipients"/*, 2*/);
        }
        if(mpigdUnmatchedRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdUnmatchedRecipients,
            "mpigdUnmatchedRecipients"/*, 2*/);
        }
        if(mpigdDefaultRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdDefaultRecipients,
                    "mpigdDefaultRecipients"/*, 2*/);
        }
        if(mpigdMatchingJobRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdMatchingJobRecipients,
                    "mpigdMatchingJobRecipients"/*, 2*/);
        }
        if(mpigdResolutionRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdResolutionRecipients,
                    "mpigdResolutionRecipients"/*, 2*/);
        }
        if(mpigdOutstandingRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdOutstandingRecipients,
                    "mpigdOutstandingRecipients"/*, 2*/);
        }
        if(mpigdInvoiceExportingRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdInvoiceExportingRecipients,
                    "mpigdInvoiceExportingRecipients"/*, 2*/);
        }
        if(mpigdMissingGrnNotificationRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdMissingGrnNotificationRecipients,
                    "mpigdMissingGrnNotificationRecipients"/*, 2*/);
        }
        if(mpigdPriceDiscrepancyRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdPriceDiscrepancyRecipients,
                    "mpigdPriceDiscrepancyRecipients"/*, 2*/);
        }
        if(mpigdQtyDiscrepancyRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdQtyDiscrepancyRecipients,
                    "mpigdQtyDiscrepancyRecipients"/*, 2*/);
        }
        if(mpigdRtvGiDnExceptionReportRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdRtvGiDnExceptionReportRecipients,
                    "mpigdRtvGiDnExceptionReportRecipients"/*, 2*/);
        }
        if(dbResolutionRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, dbResolutionRecipients,
                    "dbResolutionRecipients"/*, 2*/);
        }
        if(dbOutstandingRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, dbOutstandingRecipients,
                    "dbOutstandingRecipients"/*, 2*/);
        }
        if(dbPriceDiscrepancyRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, dbPriceDiscrepancyRecipients,
                    "dbPriceDiscrepancyRecipients"/*, 2*/);
        }
        if(dbQtyDiscrepancyRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, dbQtyDiscrepancyRecipients,
                    "dbQtyDiscrepancyRecipients"/*, 2*/);
        }
        if(dbDnExportingRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, dbDnExportingRecipients,
                    "dbDnExportingRecipients"/*, 2*/);
        }
        if(mpigdMissingGiNotificationRecipients != null)
        {
            addNewBuyerRuleForMsgSetting(oldBuyerRules, newBuyerRules, mpigdMissingGiNotificationRecipients,
                    "mpigdMissingGiNotificationRecipients"/*, 2*/);
        }
    }

    private void addNewBuyerRule(List<BuyerRuleHolder> oldBuyerRules,
        List<BuyerRuleHolder> newBuyerRules,
        BusinessRuleExHolder bussinessRule, String sessionName, int typeFlag) throws Exception
    {
        bussinessRule.trimAllString();
        bussinessRule.setAllEmptyStringToNull();
        bussinessRule.setRuleOid(((BusinessRuleExHolder)this.getSession().get(
            sessionName)).getRuleOid());
        Iterator<BuyerRuleHolder> it = oldBuyerRules.iterator();
        boolean flag = true;
        while(it.hasNext())
        {
            BuyerRuleHolder obj = it.next();
            if(obj.getRuleOid().compareTo(bussinessRule.getRuleOid()) == 0)
            {
                newBuyerRules.remove(obj);
                flag = false;
                if(bussinessRule.getRuleValue() != null || (bussinessRule.getValid() != null && bussinessRule.getValid()))
                {
                    BuyerRuleHolder holder = new BuyerRuleHolder();
                    holder.setBuyerOid(obj.getBuyerOid());
                    holder.setRuleOid(bussinessRule.getRuleOid());
                    if (typeFlag == 1)
                    {
                        if (!bussinessRule.getRuleValue().trim().isEmpty())
                        {
                            holder.setNumValue(new BigDecimal(bussinessRule.getRuleValue()));
                        }
                    }
                    else if (typeFlag == 2)
                    {
                        holder.setStringValue(bussinessRule.getRuleValue());
                    }
                    newBuyerRules.add(holder);
                }
            }
        }
        if (flag
                && (bussinessRule.getRuleValue() != null || (bussinessRule
                        .getValid() != null && bussinessRule.getValid())))
        {
            BuyerRuleHolder holder = new BuyerRuleHolder();
            holder.setBuyerOid(param.getBuyerOid());
            holder.setRuleOid(bussinessRule.getRuleOid());
            if (typeFlag == 1)
            {
                holder.setNumValue(new BigDecimal(bussinessRule.getRuleValue()));
            }
            else if (typeFlag == 2)
            {
                holder.setStringValue(bussinessRule.getRuleValue());
            }
            newBuyerRules.add(holder);
        }
    }
    
    private void addNewBuyerRuleForMsgSetting(List<BuyerRuleHolder> oldBuyerRules,
            List<BuyerRuleHolder> newBuyerRules,
            BusinessRuleExHolder bussinessRule, String sessionName /*, int typeFlag*/) throws Exception
    {
        bussinessRule.trimAllString();
        bussinessRule.setAllEmptyStringToNull();
        bussinessRule.setRuleOid(((BusinessRuleExHolder)this.getSession().get(
                sessionName)).getRuleOid());
        Iterator<BuyerRuleHolder> it = oldBuyerRules.iterator();
        boolean flag = true;
        while(it.hasNext())
        {
            BuyerRuleHolder obj = it.next();
            if(obj.getRuleOid().compareTo(bussinessRule.getRuleOid()) == 0)
            {
                newBuyerRules.remove(obj);
                flag = false;
                BuyerRuleHolder holder = new BuyerRuleHolder();
                holder.setBuyerOid(obj.getBuyerOid());
                holder.setRuleOid(bussinessRule.getRuleOid());
                holder.setStringValue(bussinessRule.getRuleValue());
                newBuyerRules.add(holder);
            }
        }
        if (flag)
        {
            BuyerRuleHolder holder = new BuyerRuleHolder();
            holder.setBuyerOid(param.getBuyerOid());
            holder.setRuleOid(bussinessRule.getRuleOid());
            holder.setStringValue(bussinessRule.getRuleValue());
            newBuyerRules.add(holder);
        }
    }

    private void initBusinessRules(BigDecimal buyerOid) throws Exception
    {
        List<BusinessRuleHolder> businessRules = businessRuleService
            .select(new BusinessRuleHolder());
        List<BuyerRuleHolder> buyerRules = buyerRuleService
            .selectByBuyerOid(buyerOid);
        if(businessRules == null || businessRules.isEmpty())
        {
            return;
        }
        Iterator<BusinessRuleHolder> it = businessRules.iterator();
        while(it.hasNext())
        {
            BusinessRuleExHolder obj = new BusinessRuleExHolder();
            BeanUtils.copyProperties(it.next(), obj);
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "AutoLogout".equals(obj.getRuleId()))
            {
                ggAutoLogout = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggAutoLogout", ggAutoLogout);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "MatchingJobMinBufferingDays".equals(obj.getRuleId()))
            {
                ggMatchingJobMinBufferingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggMatchingJobMinBufferingDays", ggMatchingJobMinBufferingDays);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "MatchingJobMaxBufferingDays".equals(obj.getRuleId()))
            {
                ggMatchingJobMaxBufferingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggMatchingJobMaxBufferingDays", ggMatchingJobMaxBufferingDays);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DailyPoReportJobDaysBefore".equals(obj.getRuleId()))
            {
                ggDailyPoReportJobDaysBefore = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggDailyPoReportJobDaysBefore", ggDailyPoReportJobDaysBefore);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DailyNotificationJobMissingGrnMinBufferingDays".equals(obj.getRuleId()))
            {
                ggDailyNotificationJobMissingGrnMinBufferingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggDailyNotificationJobMissingGrnMinBufferingDays", ggDailyNotificationJobMissingGrnMinBufferingDays);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DailyNotificationJobMissingGrnMaxBufferingDays".equals(obj.getRuleId()))
            {
                ggDailyNotificationJobMissingGrnMaxBufferingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggDailyNotificationJobMissingGrnMaxBufferingDays", ggDailyNotificationJobMissingGrnMaxBufferingDays);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DnGeneratingJobMatchingMaxBuffingDays".equals(obj.getRuleId()))
            {
                ggDnGeneratingJobMatchingMaxBuffingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggDnGeneratingJobMatchingMaxBuffingDays", ggDnGeneratingJobMatchingMaxBuffingDays);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DnGeneratingJobMatchingMinBuffingDays".equals(obj.getRuleId()))
            {
                ggDnGeneratingJobMatchingMinBuffingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggDnGeneratingJobMatchingMinBuffingDays", ggDnGeneratingJobMatchingMinBuffingDays);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "RTVDnGeneratingJobBuffingDays".equals(obj.getRuleId()))
            {
                ggRTVDnGeneratingJobBuffingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggRTVDnGeneratingJobBuffingDays", ggRTVDnGeneratingJobBuffingDays);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "ContinueProcessErrorBatch".equals(obj.getRuleId()))
            {
                ggContinueProcessErrorBatch = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggContinueProcessErrorBatch", ggContinueProcessErrorBatch);
            }
            if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "RTVDnDisputeAlertWindow".equals(obj.getRuleId()))
            {
            	ggRTVDnDisputeAlertWindow = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggRTVDnDisputeAlertWindow", ggRTVDnDisputeAlertWindow);
            }
            else if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DisableInvoicePaymentInstructions".equals(obj.getRuleId()))
            {
            	ggDisableInvoicePaymentInstructions = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggDisableInvoicePaymentInstructions", ggDisableInvoicePaymentInstructions);
            }
            else if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "RtvGiDnQtyTolerance".equals(obj.getRuleId()))
            {
                ggRtvGiDnQtyTolerance = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggRtvGiDnQtyTolerance", ggRtvGiDnQtyTolerance);
            }
            else if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "RtvGiDnQtyToleranceType".equals(obj.getRuleId()))
            {
                ggRtvGiDnQtyToleranceType = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggRtvGiDnQtyToleranceType", ggRtvGiDnQtyToleranceType);
            }
            else if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "RtvGiDnPriceTolerance".equals(obj.getRuleId()))
            {
                ggRtvGiDnPriceTolerance = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggRtvGiDnPriceTolerance", ggRtvGiDnPriceTolerance);
            }
            else if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "RtvGiDnPriceToleranceType".equals(obj.getRuleId()))
            {
                ggRtvGiDnPriceToleranceType = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggRtvGiDnPriceToleranceType", ggRtvGiDnPriceToleranceType);
            }
            else if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "RtvGiDnReportGeneratingDateRange".equals(obj.getRuleId()))
            {
                ggRtvGiDnReportGeneratingDateRange = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggRtvGiDnReportGeneratingDateRange", ggRtvGiDnReportGeneratingDateRange);
            }
            if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "QtyEditable".equals(obj.getRuleId()))
            {
                pcispQtyEditable = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispQtyEditable", pcispQtyEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "QtylessThanPO".equals(obj.getRuleId()))
            {
                pcispQtylessThanPO = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispQtylessThanPO", pcispQtylessThanPO);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "FocQtyEditable".equals(obj.getRuleId()))
            {
                pcispFocQtyEditable = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispFocQtyEditable",
                    pcispFocQtyEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "DiscountEditable".equals(obj.getRuleId()))
            {
                pcispDiscountEditable = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispDiscountEditable",
                        pcispDiscountEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "DiscountForDetailEditable".equals(obj.getRuleId()))
            {
                pcispDiscountForDetailEditable = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispDiscountForDetailEditable",
                        pcispDiscountForDetailEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "CashDiscountEditable".equals(obj.getRuleId()))
            {
                pcispCashDiscountEditable = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispCashDiscountEditable",
                        pcispCashDiscountEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "FocQtylessThanPO".equals(obj.getRuleId()))
            {
                pcispFocQtylessThanPO = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispFocQtylessThanPO",
                    pcispFocQtylessThanPO);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "UnitPriceEditable".equals(obj.getRuleId()))
            {
                pcispUnitPriceEditable = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispUnitPriceEditable",
                    pcispUnitPriceEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "EmailToStore".equals(obj.getRuleId()))
            {
                pcispEmailToStore = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispEmailToStore",
                        pcispEmailToStore);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "PdfAsAttachment".equals(obj.getRuleId()))
            {
                pcispPdfAsAttachment = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("pcispPdfAsAttachment",
                        pcispPdfAsAttachment);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "UnitPriceLessThanPO".equals(obj.getRuleId()))
            {
                pcispUnitPriceLessThanPO = initBusinessRuleValue(obj,
                    buyerRules, 0);
                this.getSession().put("pcispUnitPriceLessThanPO",
                    pcispUnitPriceLessThanPO);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "SorPO".equals(obj.getFuncId()) && "IgnoreExpiryDate".equals(obj.getRuleId()))
            {
                pcicpIgnoreExpiryDate = initBusinessRuleValue(obj,
                    buyerRules, 0);
                this.getSession().put("pcicpIgnoreExpiryDate",
                    pcicpIgnoreExpiryDate);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "ConPO".equals(obj.getFuncId()) && "ItemDiscountEditable".equals(obj.getRuleId()))
            {
                pcicpItemDiscountEditable = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("pcicpItemDiscountEditable",
                        pcicpItemDiscountEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "ConPO".equals(obj.getFuncId()) && "ItemAmountEditable".equals(obj.getRuleId()))
            {
                pcicpItemAmountEditable = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("pcicpItemAmountEditable",
                        pcicpItemAmountEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "ConPO".equals(obj.getFuncId()) && "ItemSharedCostEditable".equals(obj.getRuleId()))
            {
                pcicpItemSharedCostEditable = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("pcicpItemSharedCostEditable",
                        pcicpItemSharedCostEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "ConPO".equals(obj.getFuncId()) && "TradeDiscountEditable".equals(obj.getRuleId()))
            {
                pcicpTradeDiscountEditable = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("pcicpTradeDiscountEditable",
                        pcicpTradeDiscountEditable);
            }
            else if("PoConvertInv".equals(obj.getFuncGroup()) && "ConPO".equals(obj.getFuncId()) && "CashDiscountEditable".equals(obj.getRuleId()))
            {
                pcicpCashDiscountEditable = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("pcicpCashDiscountEditable",
                        pcicpCashDiscountEditable);
            }
            else if("PO".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DeliveryDateRange".equals(obj.getRuleId()))
            {
                pgDeliveryDateRange = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("pgDeliveryDateRange",
                        pgDeliveryDateRange);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "AutoGenStockDn".equals(obj.getRuleId()))
            {
                dbAutoGenStockDn = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbAutoGenStockDn",
                        dbAutoGenStockDn);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "AutoGenCostDn".equals(obj.getRuleId()))
            {
                dbAutoGenCostDn = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbAutoGenCostDn",
                        dbAutoGenCostDn);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "AutoSendStockDn".equals(obj.getRuleId()))
            {
                dbAutoSendStockDn = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbAutoSendStockDn",
                        dbAutoSendStockDn);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "AutoSendCostDn".equals(obj.getRuleId()))
            {
                dbAutoSendCostDn = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbAutoSendCostDn",
                        dbAutoSendCostDn);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "DnNoStyle1".equals(obj.getRuleId()))
            {
                dnNoStyle1 = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("dnNoStyle1",
                        dnNoStyle1);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "UnityFileStype".equals(obj.getRuleId()))
            {
                dbUnityFileStype = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("dbUnityFileStype",
                        dbUnityFileStype);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "NeedTranslate".equals(obj.getRuleId()))
            {
                dbNeedTranslate = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("dbNeedTranslate",
                        dbNeedTranslate);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "AutoGenDnFromGI".equals(obj.getRuleId()))
            {
                dbAutoGenDnFromGI = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbAutoGenDnFromGI",
                        dbAutoGenDnFromGI);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "AllowSupplierDisputeMatchingDn".equals(obj.getRuleId()))
            {
                dbAllowSupplierDisputeMatchingDn = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbAllowSupplierDisputeMatchingDn",
                        dbAllowSupplierDisputeMatchingDn);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "DiscrepancyReportToUser".equals(obj.getRuleId()))
            {
                dbDiscrepancyReportToUser = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbDiscrepancyReportToUser",
                        dbDiscrepancyReportToUser);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "AutoCloseAcceptedRecord".equals(obj.getRuleId()))
            {
                dbAutoCloseAcceptedRecord = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbAutoCloseAcceptedRecord",
                        dbAutoCloseAcceptedRecord);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "SendResolutionAndOutstandingByGroup".equals(obj.getRuleId()))
            {
                dbSendResolutionAndOutstandingByGroup = initBusinessRuleValue(obj,
                        buyerRules, 0);
                this.getSession().put("dbSendResolutionAndOutstandingByGroup",
                        dbSendResolutionAndOutstandingByGroup);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "ResolutionRecipients".equals(obj.getRuleId()))
            {
                dbResolutionRecipients = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("dbResolutionRecipients",
                        dbResolutionRecipients);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "OutstandingRecipients".equals(obj.getRuleId()))
            {
                dbOutstandingRecipients = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("dbOutstandingRecipients",
                        dbOutstandingRecipients);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "PriceDiscrepancyRecipients".equals(obj.getRuleId()))
            {
                dbPriceDiscrepancyRecipients = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("dbPriceDiscrepancyRecipients",
                        dbPriceDiscrepancyRecipients);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "QtyDiscrepancyRecipients".equals(obj.getRuleId()))
            {
                dbQtyDiscrepancyRecipients = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("dbQtyDiscrepancyRecipients",
                        dbQtyDiscrepancyRecipients);
            }
            else if("Dn".equals(obj.getFuncGroup()) && "Backend".equals(obj.getFuncId()) && "DnExportingRecipients".equals(obj.getRuleId()))
            {
                dbDnExportingRecipients = initBusinessRuleValue(obj,
                        buyerRules, 2);
                this.getSession().put("dbDnExportingRecipients",
                        dbDnExportingRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "QtyInvLessGrn".equals(obj.getRuleId()))
            {
                mpigdQtyInvLessGrn = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("mpigdQtyInvLessGrn", mpigdQtyInvLessGrn);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "PriceInvLessPo".equals(obj.getRuleId()))
            {
                mpigdPriceInvLessPo = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("mpigdPriceInvLessPo", mpigdPriceInvLessPo);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "QtyPoLessGrn".equals(obj.getRuleId()))
            {
                mpigdQtyPoLessGrn = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("mpigdQtyPoLessGrn", mpigdQtyPoLessGrn);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "AutoAcceptQtyInvLessGrn".equals(obj.getRuleId()))
            {
                mpigdAutoAcceptQtyInvLessGrn = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("mpigdAutoAcceptQtyInvLessGrn", mpigdAutoAcceptQtyInvLessGrn);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "AutoAcceptPriceInvLessPo".equals(obj.getRuleId()))
            {
                mpigdAutoAcceptPriceInvLessPo = initBusinessRuleValue(obj, buyerRules, 0);
                this.getSession().put("mpigdAutoAcceptPriceInvLessPo", mpigdAutoAcceptPriceInvLessPo);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "AmountTolerance".equals(obj.getRuleId()))
            {
                mpigdAmountTolerance = initBusinessRuleValue(obj, buyerRules, 1);
                this.getSession().put("mpigdAmountTolerance", mpigdAmountTolerance);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "MatchedRecipients".equals(obj.getRuleId()))
            {
                mpigdMatchedRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdMatchedRecipients", mpigdMatchedRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "UnmatchedRecipients".equals(obj.getRuleId()))
            {
                mpigdUnmatchedRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdUnmatchedRecipients", mpigdUnmatchedRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "EnableSupplierToDispute".equals(obj.getRuleId()))
            {
                mpigdEnableSupplierToDispute = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdEnableSupplierToDispute", mpigdEnableSupplierToDispute);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "AutoApproveClosedAcceptedRecord".equals(obj.getRuleId()))
            {
                mpigdAutoApproveClosedAcceptedRecord = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdAutoApproveClosedAcceptedRecord", mpigdAutoApproveClosedAcceptedRecord);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "AutoCloseAcceptedRecord".equals(obj.getRuleId()))
            {
                mpigdAutoCloseAcceptedRecord = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdAutoCloseAcceptedRecord", mpigdAutoCloseAcceptedRecord);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "AutoCloseRejectedRecord".equals(obj.getRuleId()))
            {
                mpigdAutoCloseRejectedRecord = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdAutoCloseRejectedRecord", mpigdAutoCloseRejectedRecord);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "ChangeInvDateToGrnDate".equals(obj.getRuleId()))
            {
                mpigdChangeInvDateToGrnDate = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdChangeInvDateToGrnDate", mpigdChangeInvDateToGrnDate);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "SkipMatching".equals(obj.getRuleId()))
            {
                mpigdSkipMatching = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdSkipMatching", mpigdSkipMatching);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "DiscrepancyReportToUser".equals(obj.getRuleId()))
            {
                mpigdDiscrepancyReportToUser = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdDiscrepancyReportToUser", mpigdDiscrepancyReportToUser);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "AutoRejectBuyerLossUnmatchedRecord".equals(obj.getRuleId()))
            {
                mpigdAutoRejectBuyerLossUnmatchedRecord = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdAutoRejectBuyerLossUnmatchedRecord", mpigdAutoRejectBuyerLossUnmatchedRecord);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "SendResolutionAndOutstandingByGroup".equals(obj.getRuleId()))
            {
                mpigdSendResolutionAndOutstandingByGroup = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdSendResolutionAndOutstandingByGroup", mpigdSendResolutionAndOutstandingByGroup);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "DefaultRecipients".equals(obj.getRuleId()))
            {
                mpigdDefaultRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdDefaultRecipients", mpigdDefaultRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "MatchingJobRecipients".equals(obj.getRuleId()))
            {
                mpigdMatchingJobRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdMatchingJobRecipients", mpigdMatchingJobRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "ResolutionRecipients".equals(obj.getRuleId()))
            {
                mpigdResolutionRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdResolutionRecipients", mpigdResolutionRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "OutstandingRecipients".equals(obj.getRuleId()))
            {
                mpigdOutstandingRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdOutstandingRecipients", mpigdOutstandingRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "InvoiceExportingRecipients".equals(obj.getRuleId()))
            {
                mpigdInvoiceExportingRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdInvoiceExportingRecipients", mpigdInvoiceExportingRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "MissingGrnNotificationRecipients".equals(obj.getRuleId()))
            {
                mpigdMissingGrnNotificationRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdMissingGrnNotificationRecipients", mpigdMissingGrnNotificationRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "PriceDiscrepancyRecipients".equals(obj.getRuleId()))
            {
                mpigdPriceDiscrepancyRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdPriceDiscrepancyRecipients", mpigdPriceDiscrepancyRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "QtyDiscrepancyRecipients".equals(obj.getRuleId()))
            {
                mpigdQtyDiscrepancyRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdQtyDiscrepancyRecipients", mpigdQtyDiscrepancyRecipients);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "AutoApproveMatchedByDn".equals(obj.getRuleId()))
            {
                mpigdAutoApproveMatchedByDn = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdAutoApproveMatchedByDn", mpigdAutoApproveMatchedByDn);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "RtvGiDnExceptionReportRecipients".equals(obj.getRuleId()))
            {
                mpigdRtvGiDnExceptionReportRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdRtvGiDnExceptionReportRecipients", mpigdRtvGiDnExceptionReportRecipients);
            }
            else if("SM".equals(obj.getFuncGroup()) && "Importing".equals(obj.getFuncId()) && "GenAdminUser".equals(obj.getRuleId()))
            {
                smiGenAdminUser = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("smiGenAdminUser", smiGenAdminUser);
            }
            else if("SM".equals(obj.getFuncGroup()) && "Importing".equals(obj.getFuncId()) && "AdminRole".equals(obj.getRuleId()))
            {
                smiAdminRole = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("smiAdminRole", smiAdminRole);
            }
            else if("SM".equals(obj.getFuncGroup()) && "Importing".equals(obj.getFuncId()) && "GenResultTxt".equals(obj.getRuleId()))
            {
                smiGenResultTxt = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("smiGenResultTxt", smiGenResultTxt);
            }
            else if("ITEM".equals(obj.getFuncGroup()) && "Importing".equals(obj.getFuncId()) && "Update".equals(obj.getRuleId()))
            {
                iiUpdate = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("iiUpdate", iiUpdate);
            }
            else if("ITEM".equals(obj.getFuncGroup()) && "Importing".equals(obj.getFuncId()) && "DeleteAndInsert".equals(obj.getRuleId()))
            {
                iiDeleteAndInsert = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("iiDeleteAndInsert", iiDeleteAndInsert);
            }
            else if("ITEM".equals(obj.getFuncGroup()) && "Importing".equals(obj.getFuncId()) && "SelectOneToCompare".equals(obj.getRuleId()))
            {
                iiSelectOneToCompare = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("iiSelectOneToCompare", iiSelectOneToCompare);
            }
            else if("GRN".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "SupplierCanDisputeGRN".equals(obj.getRuleId()))
            {
                ggSupplierCanDisputeGRN = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggSupplierCanDisputeGRN", ggSupplierCanDisputeGRN);
            }
            else if("GRN".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "PreventItemsNotExistInPO".equals(obj.getRuleId()))
            {
                ggPreventItemsNotExistInPO = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggPreventItemsNotExistInPO", ggPreventItemsNotExistInPO);
            }
            else if("GRN".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "PreventItemsLessThanPO".equals(obj.getRuleId()))
            {
                ggPreventItemsLessThanPO = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggPreventItemsLessThanPO", ggPreventItemsLessThanPO);
            }
            else if("GRN".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "PreventItemsQtyMoreThanPO".equals(obj.getRuleId()))
            {
                ggPreventItemsQtyMoreThanPO = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggPreventItemsQtyMoreThanPO", ggPreventItemsQtyMoreThanPO);
            }
            else if("INV".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "PreventItemsNotExistInPO".equals(obj.getRuleId()))
            {
                igPreventItemsNotExistInPO = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("igPreventItemsNotExistInPO", igPreventItemsNotExistInPO);
            }
            else if("GI".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "PreventItemsNotExistInRtv".equals(obj.getRuleId()))
            {
                gigPreventItemsNotExistInRtv = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("gigPreventItemsNotExistInRtv", gigPreventItemsNotExistInRtv);
            }
            else if("GI".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "PreventItemsLessThanRtv".equals(obj.getRuleId()))
            {
                gigPreventItemsLessThanRtv = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("gigPreventItemsLessThanRtv", gigPreventItemsLessThanRtv);
            }
            else if("GI".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "PreventItemsQtyMoreThanRtv".equals(obj.getRuleId()))
            {
                gigPreventItemsQtyMoreThanRtv = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("gigPreventItemsQtyMoreThanRtv", gigPreventItemsQtyMoreThanRtv);
            }
            else if("PO".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "NeedValidateConsignmentPo".equals(obj.getRuleId()))
            {
                pgNeedValidateConPo = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("pgNeedValidateConPo", pgNeedValidateConPo);
            }
            else if("DSD".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "NeedValidate".equals(obj.getRuleId()))
            {
                dsdNeedValidateSalesData = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("dsdNeedValidateSalesData", dsdNeedValidateSalesData);
            }
            else if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DailyNotificationJobMissingGiMinBufferingDays".equals(obj.getRuleId()))
            {
                ggDailyNotificationJobMissingGiMinBufferingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggDailyNotificationJobMissingGiMinBufferingDays", ggDailyNotificationJobMissingGiMinBufferingDays);
            }
            else if("Global".equals(obj.getFuncGroup()) && "Global".equals(obj.getFuncId()) && "DailyNotificationJobMissingGiMaxBufferingDays".equals(obj.getRuleId()))
            {
                ggDailyNotificationJobMissingGiMaxBufferingDays = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("ggDailyNotificationJobMissingGiMaxBufferingDays", ggDailyNotificationJobMissingGiMaxBufferingDays);
            }
            else if("Matching".equals(obj.getFuncGroup()) && "PoInvGrnDn".equals(obj.getFuncId()) && "MissingGiNotificationRecipients".equals(obj.getRuleId()))
            {
                mpigdMissingGiNotificationRecipients = initBusinessRuleValue(obj, buyerRules, 2);
                this.getSession().put("mpigdMissingGiNotificationRecipients", mpigdMissingGiNotificationRecipients);
            } 
        }

    }

    private BusinessRuleExHolder initBusinessRuleValue(
        BusinessRuleExHolder businessRule, List<BuyerRuleHolder> buyerRules, int typeFlag)
        throws Exception
    {
        if(buyerRules == null || buyerRules.isEmpty())
        {
            return businessRule;
        }
        Iterator<BuyerRuleHolder> it = buyerRules.iterator();
        while(it.hasNext())
        {
            BuyerRuleHolder obj = it.next();
            if(obj.getRuleOid().compareTo(businessRule.getRuleOid()) == 0)
            {
                businessRule.setValid(true);
                if (typeFlag == 1 && obj.getNumValue() != null)
                {
                    businessRule.setRuleValue(obj.getNumValue().toString());
                }
                else if (typeFlag == 2 && obj.getStringValue() != null)
                {
                    businessRule.setRuleValue(obj.getStringValue());
                }
                return businessRule;
            }
        }
        businessRule.setValid(false);
        return businessRule;
    }


    // *****************************************************
    // init country list and channel list
    // *****************************************************
    private void initSelect() throws Exception
    {
        CountryHolder country = new CountryHolder();
        country.setSortField("CTRY_DESC");
        country.setSortOrder("ASC");
        countries = countryMapper.select(country);
        CurrencyHolder currency = new CurrencyHolder();
        currency.setSortField("CURR_DESC");
        currency.setSortOrder("ASC");
        currencies = currencyService.select(currency);
        deploymentMode = DeploymentMode.toMapValue();
        channels = channelConfigHelper.getBuyerChannels();
    }

    
    // *****************************************************
    // check if the group can delete
    // *****************************************************
    private boolean checkGroup(BigDecimal buyerOid) throws Exception
    {
        GroupTmpHolder group = new GroupTmpHolder();
        group.setBuyerOid(buyerOid);
        List<GroupHolder> groupList = groupService.select(group);
        List<GroupTmpHolder> groupTmpList = groupTmpService.select(group);
        if(groupList != null)
        {
            Iterator<GroupHolder> it = groupList.iterator();
            while(it.hasNext())
            {
                GroupHolder tmp = it.next();
                group.setGroupOid(tmp.getGroupOid());
                List<GroupTmpHolder> obj = groupTmpService.select(group);
                if(obj == null || obj.isEmpty())
                {
                    return false;
                }
            }
        }
        if(groupTmpList != null)
        {
            Iterator<GroupTmpHolder> it = groupTmpList.iterator();
            while(it.hasNext())
            {
                GroupTmpHolder tmp = it.next();
                group.setGroupOid(tmp.getGroupOid());
                List<GroupHolder> obj = groupService.select(group);
                if(obj == null || obj.isEmpty())
                {
                    if(DbActionType.CREATE.equals(tmp.getActionType()) && MkCtrlStatus.PENDING.equals(tmp.getCtrlStatus()))
                    {
                        return true;
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    
    // *****************************************************
    // check if the user can delete
    // *****************************************************
    private boolean checkUser(BigDecimal buyerOid) throws Exception
    {
        UserProfileTmpExHolder userProfile = new UserProfileTmpExHolder();
        userProfile.setBuyerOid(buyerOid);
        List<UserProfileHolder> userProfileList = userProfileService.select(userProfile);
        List<UserProfileTmpHolder> userProfileTmpList = userProfileTmpService.select(userProfile);
        if(userProfileList != null)
        {
            Iterator<UserProfileHolder> it = userProfileList.iterator();
            while(it.hasNext())
            {
                UserProfileHolder tmp = it.next();
                userProfile.setUserOid(tmp.getUserOid());
                List<UserProfileTmpHolder> obj = userProfileTmpService.select(userProfile);
                if(obj == null || obj.isEmpty())
                {
                    return false;
                }
            }
        }
        if(userProfileTmpList != null)
        {
            Iterator<UserProfileTmpHolder> it = userProfileTmpList.iterator();
            while(it.hasNext())
            {
                UserProfileTmpHolder tmp = it.next();
                userProfile.setUserOid(tmp.getUserOid());
                List<UserProfileHolder> obj = userProfileService.select(userProfile);
                if(obj == null || obj.isEmpty())
                {
                    if(DbActionType.CREATE.equals(tmp.getActionType()) && MkCtrlStatus.PENDING.equals(tmp.getCtrlStatus()))
                    {
                        return true;
                    }
                    return false;
                }
            }
        }
        return true;
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


    // *****************************************************
    // add msg setting which does not exist in buyer_msg_setting.
    // *****************************************************
    private void addUnExistMsgSetting(BuyerHolder buyer, boolean createFlag)
            throws Exception
    {
        List<BuyerMsgSettingHolder> buyerMsgSettings = null;
        List<ControlParameterHolder> msgList = controlParameterService
                .selectCacheControlParametersBySectId(SECT_ID_HSEKEEP);
        if (msgList == null || msgList.isEmpty())
        {
            return;
        }

        if (createFlag)
        {
            for (int i = 0; i < msgList.size(); i++)
            {
                this.addBuyerMsgSetting(buyer, (ControlParameterHolder) msgList
                        .get(i));
            }

            return;
        }

        buyerMsgSettings = buyer.getMsgSetting();
        for (int i = 0; i < msgList.size(); i++)
        {
            boolean flag = false;

            ControlParameterHolder cp = (ControlParameterHolder) msgList.get(i);
            if (buyerMsgSettings != null)
            {
                for (int j = 0; j < buyerMsgSettings.size(); j++)
                {
                    BuyerMsgSettingHolder buyerMsg = (BuyerMsgSettingHolder) buyerMsgSettings
                            .get(j);
                    if (buyerMsg.getMsgType().equals(cp.getParamId()))
                    {
                        flag = true;
                        break;
                    }
                }
            }

            if (!flag)
            {
                this.addBuyerMsgSetting(buyer, cp);
            }
        }

        if (buyerMsgSettings != null)
        {
            Iterator<BuyerMsgSettingHolder> it = buyerMsgSettings.iterator();
            while (it.hasNext())
            {
                BuyerMsgSettingHolder obj = it.next();
                boolean flag = false;
                for (int i = 0; i < msgList.size(); i++)
                {
                    if (obj.getMsgType().equals(msgList.get(i).getParamId()))
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    it.remove();
                    buyerMsgSettings.remove(obj);
                }
            }
        }
    }


    private void addBuyerMsgSetting(BuyerHolder buyer, ControlParameterHolder cp)
    {
        BuyerMsgSettingHolder msg = new BuyerMsgSettingHolder();
        msg.setBuyerOid(buyer.getBuyerOid());
        msg.setMsgType(cp.getParamId());
        if("INBOUND".equals(cp.getCatId().toUpperCase()))
        {
            msg.setAlertFrequency("DOC");
        }
        else
        {
            msg.setAlertFrequency("BATCH");
        }
        msg.setExcludeSucc(false);
        msg.setFileFormat(appConfig.getDefaultFileFormat(cp.getParamId()));
        buyer.addBuyerMsgSetting(msg);
    }


    // *****************************************************
    // getter and setter
    // *****************************************************
    public Map<Boolean, String> getStatus()
    {
        return status;
    }


    public BuyerHolder getParam()
    {
        return param;
    }


    public void setParam(BuyerHolder param)
    {
        this.param = param;
    }


    public List<? extends Object> getCountries()
    {
        return countries;
    }


    public Map<String, String> getDeploymentMode()
    {
        return deploymentMode;
    }


    public List<String> getChannels()
    {
        return channels;
    }


    public InputStream getImageResult()
    {
        return imageResult;
    }


    public String getContentType()
    {
        return contentType;
    }


    public File getLogo()
    {
        return logo;
    }


    public void setLogo(File logo)
    {
        this.logo = logo;
    }


    public String getLogoContentType()
    {
        return logoContentType;
    }


    public void setLogoContentType(String logoContentType)
    {
        this.logoContentType = logoContentType;
    }


    public BuyerExHolder getBuyerEx()
    {
        return buyerEx;
    }


    public void setBuyerEx(BuyerExHolder buyerEx)
    {
        this.buyerEx = buyerEx;
    }


    public List<? extends Object> getCurrencies()
    {
        return currencies;
    }


    public List<String> getInboundMsg()
    {
        return inboundMsg;
    }


    public void setInboundMsg(List<String> inboundMsg)
    {
        this.inboundMsg = inboundMsg;
    }


    public List<String> getOutboundMsg()
    {
        return outboundMsg;
    }


    public void setOutboundMsg(List<String> outboundMsg)
    {
        this.outboundMsg = outboundMsg;
    }


    public Map<String, BuyerMsgSettingExHolder> getInbounds()
    {
        return inbounds;
    }


    public void setInbounds(Map<String, BuyerMsgSettingExHolder> inbounds)
    {
        this.inbounds = inbounds;
    }


    public Map<String, BuyerMsgSettingExHolder> getOutbounds()
    {
        return outbounds;
    }


    public void setOutbounds(Map<String, BuyerMsgSettingExHolder> outbounds)
    {
        this.outbounds = outbounds;
    }


    public List<String> getFuncGroups()
    {
        return funcGroups;
    }


    public void setFuncGroups(List<String> funcGroups)
    {
        this.funcGroups = funcGroups;
    }


    public List<String> getFuncIds()
    {
        return funcIds;
    }


    public void setFuncIds(List<String> funcIds)
    {
        this.funcIds = funcIds;
    }


    public String getFuncGroup()
    {
        return funcGroup;
    }


    public void setFuncGroup(String funcGroup)
    {
        this.funcGroup = funcGroup;
    }


    public String getFuncId()
    {
        return funcId;
    }


    public void setFuncId(String funcId)
    {
        this.funcId = funcId;
    }


    public BusinessRuleExHolder getPcispQtyEditable()
    {
        return pcispQtyEditable;
    }


    public void setPcispQtyEditable(BusinessRuleExHolder pcispQtyEditable)
    {
        this.pcispQtyEditable = pcispQtyEditable;
    }


    public BusinessRuleExHolder getPcispQtylessThanPO()
    {
        return pcispQtylessThanPO;
    }


    public void setPcispQtylessThanPO(BusinessRuleExHolder pcispQtylessThanPO)
    {
        this.pcispQtylessThanPO = pcispQtylessThanPO;
    }


    public BusinessRuleExHolder getPcispFocQtyEditable()
    {
        return pcispFocQtyEditable;
    }


    public void setPcispFocQtyEditable(BusinessRuleExHolder pcispFocQtyEditable)
    {
        this.pcispFocQtyEditable = pcispFocQtyEditable;
    }


    public BusinessRuleExHolder getPcispDiscountEditable()
    {
        return pcispDiscountEditable;
    }


    public void setPcispDiscountEditable(BusinessRuleExHolder pcispDiscountEditable)
    {
        this.pcispDiscountEditable = pcispDiscountEditable;
    }


    public BusinessRuleExHolder getPcispDiscountForDetailEditable()
    {
        return pcispDiscountForDetailEditable;
    }


    public void setPcispDiscountForDetailEditable(
            BusinessRuleExHolder pcispDiscountForDetailEditable)
    {
        this.pcispDiscountForDetailEditable = pcispDiscountForDetailEditable;
    }


    public BusinessRuleExHolder getPcispCashDiscountEditable()
    {
        return pcispCashDiscountEditable;
    }


    public void setPcispCashDiscountEditable(
            BusinessRuleExHolder pcispCashDiscountEditable)
    {
        this.pcispCashDiscountEditable = pcispCashDiscountEditable;
    }


    public BusinessRuleExHolder getPcispFocQtylessThanPO()
    {
        return pcispFocQtylessThanPO;
    }


    public void setPcispFocQtylessThanPO(BusinessRuleExHolder pcispFocQtylessThanPO)
    {
        this.pcispFocQtylessThanPO = pcispFocQtylessThanPO;
    }


    public BusinessRuleExHolder getPcispUnitPriceEditable()
    {
        return pcispUnitPriceEditable;
    }


    public void setPcispUnitPriceEditable(
            BusinessRuleExHolder pcispUnitPriceEditable)
    {
        this.pcispUnitPriceEditable = pcispUnitPriceEditable;
    }


    public BusinessRuleExHolder getPcispEmailToStore()
    {
        return pcispEmailToStore;
    }


    public void setPcispEmailToStore(BusinessRuleExHolder pcispEmailToStore)
    {
        this.pcispEmailToStore = pcispEmailToStore;
    }


    public BusinessRuleExHolder getPcispPdfAsAttachment()
    {
        return pcispPdfAsAttachment;
    }


    public void setPcispPdfAsAttachment(BusinessRuleExHolder pcispPdfAsAttachment)
    {
        this.pcispPdfAsAttachment = pcispPdfAsAttachment;
    }


    public BusinessRuleExHolder getPcispUnitPriceLessThanPO()
    {
        return pcispUnitPriceLessThanPO;
    }


    public BusinessRuleExHolder getPcicpItemDiscountEditable()
    {
        return pcicpItemDiscountEditable;
    }


    public void setPcicpItemDiscountEditable(
            BusinessRuleExHolder pcicpItemDiscountEditable)
    {
        this.pcicpItemDiscountEditable = pcicpItemDiscountEditable;
    }


    public BusinessRuleExHolder getPcicpItemAmountEditable()
    {
        return pcicpItemAmountEditable;
    }


    public void setPcicpItemAmountEditable(
            BusinessRuleExHolder pcicpItemAmountEditable)
    {
        this.pcicpItemAmountEditable = pcicpItemAmountEditable;
    }


    public BusinessRuleExHolder getPcicpItemSharedCostEditable()
    {
        return pcicpItemSharedCostEditable;
    }


    public void setPcicpItemSharedCostEditable(
            BusinessRuleExHolder pcicpItemSharedCostEditable)
    {
        this.pcicpItemSharedCostEditable = pcicpItemSharedCostEditable;
    }


    public BusinessRuleExHolder getPcicpTradeDiscountEditable()
    {
        return pcicpTradeDiscountEditable;
    }


    public void setPcicpTradeDiscountEditable(
            BusinessRuleExHolder pcicpTradeDiscountEditable)
    {
        this.pcicpTradeDiscountEditable = pcicpTradeDiscountEditable;
    }


    public BusinessRuleExHolder getPcicpCashDiscountEditable()
    {
        return pcicpCashDiscountEditable;
    }


    public void setPcicpCashDiscountEditable(
            BusinessRuleExHolder pcicpCashDiscountEditable)
    {
        this.pcicpCashDiscountEditable = pcicpCashDiscountEditable;
    }


    public void setPcispUnitPriceLessThanPO(
            BusinessRuleExHolder pcispUnitPriceLessThanPO)
    {
        this.pcispUnitPriceLessThanPO = pcispUnitPriceLessThanPO;
    }

    
    public BusinessRuleExHolder getPgDeliveryDateRange()
    {
        return pgDeliveryDateRange;
    }


    public void setPgDeliveryDateRange(BusinessRuleExHolder pgDeliveryDateRange)
    {
        this.pgDeliveryDateRange = pgDeliveryDateRange;
    }


    public BusinessRuleExHolder getMpigdQtyInvLessGrn()
    {
        return mpigdQtyInvLessGrn;
    }


    public void setMpigdQtyInvLessGrn(BusinessRuleExHolder mpigdQtyInvLessGrn)
    {
        this.mpigdQtyInvLessGrn = mpigdQtyInvLessGrn;
    }


    public BusinessRuleExHolder getMpigdPriceInvLessPo()
    {
        return mpigdPriceInvLessPo;
    }


    public void setMpigdPriceInvLessPo(BusinessRuleExHolder mpigdPriceInvLessPo)
    {
        this.mpigdPriceInvLessPo = mpigdPriceInvLessPo;
    }


    public BusinessRuleExHolder getMpigdQtyPoLessGrn()
    {
        return mpigdQtyPoLessGrn;
    }


    public void setMpigdQtyPoLessGrn(BusinessRuleExHolder mpigdQtyPoLessGrn)
    {
        this.mpigdQtyPoLessGrn = mpigdQtyPoLessGrn;
    }


    public BusinessRuleExHolder getMpigdAutoAcceptQtyInvLessGrn()
    {
        return mpigdAutoAcceptQtyInvLessGrn;
    }


    public void setMpigdAutoAcceptQtyInvLessGrn(
            BusinessRuleExHolder mpigdAutoAcceptQtyInvLessGrn)
    {
        this.mpigdAutoAcceptQtyInvLessGrn = mpigdAutoAcceptQtyInvLessGrn;
    }


    public BusinessRuleExHolder getMpigdAutoAcceptPriceInvLessPo()
    {
        return mpigdAutoAcceptPriceInvLessPo;
    }


    public void setMpigdAutoAcceptPriceInvLessPo(
            BusinessRuleExHolder mpigdAutoAcceptPriceInvLessPo)
    {
        this.mpigdAutoAcceptPriceInvLessPo = mpigdAutoAcceptPriceInvLessPo;
    }


    public BusinessRuleExHolder getMpigdAmountTolerance()
    {
        return mpigdAmountTolerance;
    }


    public void setMpigdAmountTolerance(BusinessRuleExHolder mpigdAmountTolerance)
    {
        this.mpigdAmountTolerance = mpigdAmountTolerance;
    }


    public String getResult()
    {
        return result;
    }


    public void setResult(String result)
    {
        this.result = result;
    }


    public BusinessRuleExHolder getGgAutoLogout()
    {
        return ggAutoLogout;
    }


    public void setGgAutoLogout(BusinessRuleExHolder ggAutoLogout)
    {
        this.ggAutoLogout = ggAutoLogout;
    }


    public BusinessRuleExHolder getGgMatchingJobMinBufferingDays()
    {
        return ggMatchingJobMinBufferingDays;
    }


    public void setGgMatchingJobMinBufferingDays(
            BusinessRuleExHolder ggMatchingJobMinBufferingDays)
    {
        this.ggMatchingJobMinBufferingDays = ggMatchingJobMinBufferingDays;
    }


    public BusinessRuleExHolder getGgMatchingJobMaxBufferingDays()
    {
        return ggMatchingJobMaxBufferingDays;
    }


    public void setGgMatchingJobMaxBufferingDays(
            BusinessRuleExHolder ggMatchingJobMaxBufferingDays)
    {
        this.ggMatchingJobMaxBufferingDays = ggMatchingJobMaxBufferingDays;
    }


    public BusinessRuleExHolder getGgDailyPoReportJobDaysBefore()
    {
        return ggDailyPoReportJobDaysBefore;
    }


    public void setGgDailyPoReportJobDaysBefore(
            BusinessRuleExHolder ggDailyPoReportJobDaysBefore)
    {
        this.ggDailyPoReportJobDaysBefore = ggDailyPoReportJobDaysBefore;
    }


    public BusinessRuleExHolder getGgDailyNotificationJobMissingGrnMinBufferingDays()
    {
        return ggDailyNotificationJobMissingGrnMinBufferingDays;
    }


    public void setGgDailyNotificationJobMissingGrnMinBufferingDays(
            BusinessRuleExHolder ggDailyNotificationJobMissingGrnMinBufferingDays)
    {
        this.ggDailyNotificationJobMissingGrnMinBufferingDays = ggDailyNotificationJobMissingGrnMinBufferingDays;
    }


    public BusinessRuleExHolder getGgDailyNotificationJobMissingGrnMaxBufferingDays()
    {
        return ggDailyNotificationJobMissingGrnMaxBufferingDays;
    }


    public void setGgDailyNotificationJobMissingGrnMaxBufferingDays(
            BusinessRuleExHolder ggDailyNotificationJobMissingGrnMaxBufferingDays)
    {
        this.ggDailyNotificationJobMissingGrnMaxBufferingDays = ggDailyNotificationJobMissingGrnMaxBufferingDays;
    }



    public BusinessRuleExHolder getGgDnGeneratingJobMatchingMaxBuffingDays()
    {
        return ggDnGeneratingJobMatchingMaxBuffingDays;
    }


    public void setGgDnGeneratingJobMatchingMaxBuffingDays(
            BusinessRuleExHolder ggDnGeneratingJobMatchingMaxBuffingDays)
    {
        this.ggDnGeneratingJobMatchingMaxBuffingDays = ggDnGeneratingJobMatchingMaxBuffingDays;
    }


    public BusinessRuleExHolder getGgDnGeneratingJobMatchingMinBuffingDays()
    {
        return ggDnGeneratingJobMatchingMinBuffingDays;
    }


    public void setGgDnGeneratingJobMatchingMinBuffingDays(
            BusinessRuleExHolder ggDnGeneratingJobMatchingMinBuffingDays)
    {
        this.ggDnGeneratingJobMatchingMinBuffingDays = ggDnGeneratingJobMatchingMinBuffingDays;
    }


    public BusinessRuleExHolder getGgRTVDnGeneratingJobBuffingDays()
    {
        return ggRTVDnGeneratingJobBuffingDays;
    }


    public void setGgRTVDnGeneratingJobBuffingDays(
            BusinessRuleExHolder ggRTVDnGeneratingJobBuffingDays)
    {
        this.ggRTVDnGeneratingJobBuffingDays = ggRTVDnGeneratingJobBuffingDays;
    }


    public BusinessRuleExHolder getGgContinueProcessErrorBatch()
    {
        return ggContinueProcessErrorBatch;
    }


    public void setGgContinueProcessErrorBatch(
            BusinessRuleExHolder ggContinueProcessErrorBatch)
    {
        this.ggContinueProcessErrorBatch = ggContinueProcessErrorBatch;
    }


    public BusinessRuleExHolder getDbAutoGenStockDn()
    {
        return dbAutoGenStockDn;
    }


    public void setDbAutoGenStockDn(BusinessRuleExHolder dbAutoGenStockDn)
    {
        this.dbAutoGenStockDn = dbAutoGenStockDn;
    }


    public BusinessRuleExHolder getDbAutoGenCostDn()
    {
        return dbAutoGenCostDn;
    }


    public void setDbAutoGenCostDn(BusinessRuleExHolder dbAutoGenCostDn)
    {
        this.dbAutoGenCostDn = dbAutoGenCostDn;
    }


    public BusinessRuleExHolder getDbAutoSendStockDn()
    {
        return dbAutoSendStockDn;
    }


    public void setDbAutoSendStockDn(BusinessRuleExHolder dbAutoSendStockDn)
    {
        this.dbAutoSendStockDn = dbAutoSendStockDn;
    }


    public BusinessRuleExHolder getDbAutoSendCostDn()
    {
        return dbAutoSendCostDn;
    }


    public void setDbAutoSendCostDn(BusinessRuleExHolder dbAutoSendCostDn)
    {
        this.dbAutoSendCostDn = dbAutoSendCostDn;
    }


    public BusinessRuleExHolder getMpigdMatchedRecipients()
    {
        return mpigdMatchedRecipients;
    }


    public void setMpigdMatchedRecipients(
            BusinessRuleExHolder mpigdMatchedRecipients)
    {
        this.mpigdMatchedRecipients = mpigdMatchedRecipients;
    }


    public BusinessRuleExHolder getMpigdUnmatchedRecipients()
    {
        return mpigdUnmatchedRecipients;
    }


    public void setMpigdUnmatchedRecipients(
            BusinessRuleExHolder mpigdUnmatchedRecipients)
    {
        this.mpigdUnmatchedRecipients = mpigdUnmatchedRecipients;
    }


    public BusinessRuleExHolder getMpigdEnableSupplierToDispute()
    {
        return mpigdEnableSupplierToDispute;
    }


    public void setMpigdEnableSupplierToDispute(
            BusinessRuleExHolder mpigdEnableSupplierToDispute)
    {
        this.mpigdEnableSupplierToDispute = mpigdEnableSupplierToDispute;
    }


    public BusinessRuleExHolder getMpigdAutoApproveClosedAcceptedRecord()
    {
        return mpigdAutoApproveClosedAcceptedRecord;
    }


    public void setMpigdAutoApproveClosedAcceptedRecord(
            BusinessRuleExHolder mpigdAutoApproveClosedAcceptedRecord)
    {
        this.mpigdAutoApproveClosedAcceptedRecord = mpigdAutoApproveClosedAcceptedRecord;
    }


    public BusinessRuleExHolder getMpigdAutoCloseAcceptedRecord()
    {
        return mpigdAutoCloseAcceptedRecord;
    }


    public void setMpigdAutoCloseAcceptedRecord(
            BusinessRuleExHolder mpigdAutoCloseAcceptedRecord)
    {
        this.mpigdAutoCloseAcceptedRecord = mpigdAutoCloseAcceptedRecord;
    }

    
    public BusinessRuleExHolder getMpigdAutoCloseRejectedRecord()
    {
        return mpigdAutoCloseRejectedRecord;
    }


    public void setMpigdAutoCloseRejectedRecord(
            BusinessRuleExHolder mpigdAutoCloseRejectedRecord)
    {
        this.mpigdAutoCloseRejectedRecord = mpigdAutoCloseRejectedRecord;
    }


    public BusinessRuleExHolder getMpigdChangeInvDateToGrnDate()
    {
        return mpigdChangeInvDateToGrnDate;
    }


    public void setMpigdChangeInvDateToGrnDate(
            BusinessRuleExHolder mpigdChangeInvDateToGrnDate)
    {
        this.mpigdChangeInvDateToGrnDate = mpigdChangeInvDateToGrnDate;
    }


    public BusinessRuleExHolder getMpigdSkipMatching()
    {
        return mpigdSkipMatching;
    }


    public void setMpigdSkipMatching(BusinessRuleExHolder mpigdSkipMatching)
    {
        this.mpigdSkipMatching = mpigdSkipMatching;
    }


    public BusinessRuleExHolder getMpigdDiscrepancyReportToUser()
    {
        return mpigdDiscrepancyReportToUser;
    }


    public void setMpigdDiscrepancyReportToUser(
            BusinessRuleExHolder mpigdDiscrepancyReportToUser)
    {
        this.mpigdDiscrepancyReportToUser = mpigdDiscrepancyReportToUser;
    }


    public BusinessRuleExHolder getMpigdAutoRejectBuyerLossUnmatchedRecord()
    {
        return mpigdAutoRejectBuyerLossUnmatchedRecord;
    }


    public void setMpigdAutoRejectBuyerLossUnmatchedRecord(
            BusinessRuleExHolder mpigdAutoRejectBuyerLossUnmatchedRecord)
    {
        this.mpigdAutoRejectBuyerLossUnmatchedRecord = mpigdAutoRejectBuyerLossUnmatchedRecord;
    }


    public BusinessRuleExHolder getMpigdSendResolutionAndOutstandingByGroup()
    {
        return mpigdSendResolutionAndOutstandingByGroup;
    }


    public void setMpigdSendResolutionAndOutstandingByGroup(
            BusinessRuleExHolder mpigdSendResolutionAndOutstandingByGroup)
    {
        this.mpigdSendResolutionAndOutstandingByGroup = mpigdSendResolutionAndOutstandingByGroup;
    }


    public BusinessRuleExHolder getMpigdDefaultRecipients()
    {
        return mpigdDefaultRecipients;
    }


    public void setMpigdDefaultRecipients(
            BusinessRuleExHolder mpigdDefaultRecipients)
    {
        this.mpigdDefaultRecipients = mpigdDefaultRecipients;
    }


    public BusinessRuleExHolder getMpigdMatchingJobRecipients()
    {
        return mpigdMatchingJobRecipients;
    }


    public void setMpigdMatchingJobRecipients(
            BusinessRuleExHolder mpigdMatchingJobRecipients)
    {
        this.mpigdMatchingJobRecipients = mpigdMatchingJobRecipients;
    }


    public BusinessRuleExHolder getMpigdResolutionRecipients()
    {
        return mpigdResolutionRecipients;
    }


    public void setMpigdResolutionRecipients(
            BusinessRuleExHolder mpigdResolutionRecipients)
    {
        this.mpigdResolutionRecipients = mpigdResolutionRecipients;
    }


    public BusinessRuleExHolder getMpigdOutstandingRecipients()
    {
        return mpigdOutstandingRecipients;
    }


    public void setMpigdOutstandingRecipients(
            BusinessRuleExHolder mpigdOutstandingRecipients)
    {
        this.mpigdOutstandingRecipients = mpigdOutstandingRecipients;
    }


    public BusinessRuleExHolder getMpigdInvoiceExportingRecipients()
    {
        return mpigdInvoiceExportingRecipients;
    }


    public void setMpigdInvoiceExportingRecipients(
            BusinessRuleExHolder mpigdInvoiceExportingRecipients)
    {
        this.mpigdInvoiceExportingRecipients = mpigdInvoiceExportingRecipients;
    }


    public BusinessRuleExHolder getMpigdMissingGrnNotificationRecipients()
    {
        return mpigdMissingGrnNotificationRecipients;
    }


    public void setMpigdMissingGrnNotificationRecipients(
            BusinessRuleExHolder mpigdMissingGrnNotificationRecipients)
    {
        this.mpigdMissingGrnNotificationRecipients = mpigdMissingGrnNotificationRecipients;
    }


    public BusinessRuleExHolder getMpigdPriceDiscrepancyRecipients()
    {
        return mpigdPriceDiscrepancyRecipients;
    }


    public void setMpigdPriceDiscrepancyRecipients(
            BusinessRuleExHolder mpigdPriceDiscrepancyRecipients)
    {
        this.mpigdPriceDiscrepancyRecipients = mpigdPriceDiscrepancyRecipients;
    }


    public BusinessRuleExHolder getMpigdQtyDiscrepancyRecipients()
    {
        return mpigdQtyDiscrepancyRecipients;
    }


    public void setMpigdQtyDiscrepancyRecipients(
            BusinessRuleExHolder mpigdQtyDiscrepancyRecipients)
    {
        this.mpigdQtyDiscrepancyRecipients = mpigdQtyDiscrepancyRecipients;
    }


    public BusinessRuleExHolder getMpigdAutoApproveMatchedByDn()
    {
        return mpigdAutoApproveMatchedByDn;
    }


    public void setMpigdAutoApproveMatchedByDn(
            BusinessRuleExHolder mpigdAutoApproveMatchedByDn)
    {
        this.mpigdAutoApproveMatchedByDn = mpigdAutoApproveMatchedByDn;
    }


    public BusinessRuleExHolder getSmiGenAdminUser()
    {
        return smiGenAdminUser;
    }


    public void setSmiGenAdminUser(BusinessRuleExHolder smiGenAdminUser)
    {
        this.smiGenAdminUser = smiGenAdminUser;
    }


    public BusinessRuleExHolder getSmiAdminRole()
    {
        return smiAdminRole;
    }


    public void setSmiAdminRole(BusinessRuleExHolder smiAdminRole)
    {
        this.smiAdminRole = smiAdminRole;
    }


    public BusinessRuleExHolder getSmiGenResultTxt()
    {
        return smiGenResultTxt;
    }


    public void setSmiGenResultTxt(BusinessRuleExHolder smiGenResultTxt)
    {
        this.smiGenResultTxt = smiGenResultTxt;
    }


    public BusinessRuleExHolder getDnNoStyle1()
    {
        return dnNoStyle1;
    }


    public void setDnNoStyle1(BusinessRuleExHolder dnNoStyle1)
    {
        this.dnNoStyle1 = dnNoStyle1;
    }


    public BusinessRuleExHolder getDbUnityFileStype()
    {
        return dbUnityFileStype;
    }


    public void setDbUnityFileStype(BusinessRuleExHolder dbUnityFileStype)
    {
        this.dbUnityFileStype = dbUnityFileStype;
    }


    public BusinessRuleExHolder getDbNeedTranslate()
    {
        return dbNeedTranslate;
    }


    public void setDbNeedTranslate(BusinessRuleExHolder dbNeedTranslate)
    {
        this.dbNeedTranslate = dbNeedTranslate;
    }


    public BusinessRuleExHolder getDbAutoGenDnFromGI()
    {
        return dbAutoGenDnFromGI;
    }


    public void setDbAutoGenDnFromGI(BusinessRuleExHolder dbAutoGenDnFromGI)
    {
        this.dbAutoGenDnFromGI = dbAutoGenDnFromGI;
    }


    public BusinessRuleExHolder getDbAllowSupplierDisputeMatchingDn()
    {
        return dbAllowSupplierDisputeMatchingDn;
    }


    public void setDbAllowSupplierDisputeMatchingDn(
            BusinessRuleExHolder dbAllowSupplierDisputeMatchingDn)
    {
        this.dbAllowSupplierDisputeMatchingDn = dbAllowSupplierDisputeMatchingDn;
    }


    public BusinessRuleExHolder getDbDiscrepancyReportToUser()
    {
        return dbDiscrepancyReportToUser;
    }


    public void setDbDiscrepancyReportToUser(
            BusinessRuleExHolder dbDiscrepancyReportToUser)
    {
        this.dbDiscrepancyReportToUser = dbDiscrepancyReportToUser;
    }


    public BusinessRuleExHolder getDbAutoCloseAcceptedRecord()
    {
        return dbAutoCloseAcceptedRecord;
    }


    public void setDbAutoCloseAcceptedRecord(
            BusinessRuleExHolder dbAutoCloseAcceptedRecord)
    {
        this.dbAutoCloseAcceptedRecord = dbAutoCloseAcceptedRecord;
    }


    public BusinessRuleExHolder getDbSendResolutionAndOutstandingByGroup()
    {
        return dbSendResolutionAndOutstandingByGroup;
    }


    public void setDbSendResolutionAndOutstandingByGroup(
            BusinessRuleExHolder dbSendResolutionAndOutstandingByGroup)
    {
        this.dbSendResolutionAndOutstandingByGroup = dbSendResolutionAndOutstandingByGroup;
    }


    public BusinessRuleExHolder getDbResolutionRecipients()
    {
        return dbResolutionRecipients;
    }


    public void setDbResolutionRecipients(
            BusinessRuleExHolder dbResolutionRecipients)
    {
        this.dbResolutionRecipients = dbResolutionRecipients;
    }


    public BusinessRuleExHolder getDbOutstandingRecipients()
    {
        return dbOutstandingRecipients;
    }


    public void setDbOutstandingRecipients(
            BusinessRuleExHolder dbOutstandingRecipients)
    {
        this.dbOutstandingRecipients = dbOutstandingRecipients;
    }


    public BusinessRuleExHolder getDbPriceDiscrepancyRecipients()
    {
        return dbPriceDiscrepancyRecipients;
    }


    public void setDbPriceDiscrepancyRecipients(
            BusinessRuleExHolder dbPriceDiscrepancyRecipients)
    {
        this.dbPriceDiscrepancyRecipients = dbPriceDiscrepancyRecipients;
    }


    public BusinessRuleExHolder getDbQtyDiscrepancyRecipients()
    {
        return dbQtyDiscrepancyRecipients;
    }


    public void setDbQtyDiscrepancyRecipients(
            BusinessRuleExHolder dbQtyDiscrepancyRecipients)
    {
        this.dbQtyDiscrepancyRecipients = dbQtyDiscrepancyRecipients;
    }


    public BusinessRuleExHolder getDbDnExportingRecipients()
    {
        return dbDnExportingRecipients;
    }


    public void setDbDnExportingRecipients(
            BusinessRuleExHolder dbDnExportingRecipients)
    {
        this.dbDnExportingRecipients = dbDnExportingRecipients;
    }


    public BusinessRuleExHolder getIiUpdate()
    {
        return iiUpdate;
    }


    public void setIiUpdate(BusinessRuleExHolder iiUpdate)
    {
        this.iiUpdate = iiUpdate;
    }


    public BusinessRuleExHolder getIiDeleteAndInsert()
    {
        return iiDeleteAndInsert;
    }


    public void setIiDeleteAndInsert(BusinessRuleExHolder iiDeleteAndInsert)
    {
        this.iiDeleteAndInsert = iiDeleteAndInsert;
    }


    public BusinessRuleExHolder getIiSelectOneToCompare()
    {
        return iiSelectOneToCompare;
    }


    public void setIiSelectOneToCompare(BusinessRuleExHolder iiSelectOneToCompare)
    {
        this.iiSelectOneToCompare = iiSelectOneToCompare;
    }


    public BusinessRuleExHolder getGgSupplierCanDisputeGRN()
    {
        return ggSupplierCanDisputeGRN;
    }


    public void setGgSupplierCanDisputeGRN(
            BusinessRuleExHolder ggSupplierCanDisputeGRN)
    {
        this.ggSupplierCanDisputeGRN = ggSupplierCanDisputeGRN;
    }


    public BusinessRuleExHolder getGgPreventItemsNotExistInPO()
    {
        return ggPreventItemsNotExistInPO;
    }


    public void setGgPreventItemsNotExistInPO(
            BusinessRuleExHolder ggPreventItemsNotExistInPO)
    {
        this.ggPreventItemsNotExistInPO = ggPreventItemsNotExistInPO;
    }


    public BusinessRuleExHolder getIgPreventItemsNotExistInPO()
    {
        return igPreventItemsNotExistInPO;
    }


    public void setIgPreventItemsNotExistInPO(
            BusinessRuleExHolder igPreventItemsNotExistInPO)
    {
        this.igPreventItemsNotExistInPO = igPreventItemsNotExistInPO;
    }


    public BusinessRuleExHolder getGigPreventItemsNotExistInRtv()
    {
        return gigPreventItemsNotExistInRtv;
    }


    public void setGigPreventItemsNotExistInRtv(
            BusinessRuleExHolder gigPreventItemsNotExistInRtv)
    {
        this.gigPreventItemsNotExistInRtv = gigPreventItemsNotExistInRtv;
    }


    public BusinessRuleExHolder getGgPreventItemsLessThanPO()
    {
        return ggPreventItemsLessThanPO;
    }


    public void setGgPreventItemsLessThanPO(
        BusinessRuleExHolder ggPreventItemsLessThanPO)
    {
        this.ggPreventItemsLessThanPO = ggPreventItemsLessThanPO;
    }


    public BusinessRuleExHolder getGgPreventItemsQtyMoreThanPO()
    {
        return ggPreventItemsQtyMoreThanPO;
    }


    public void setGgPreventItemsQtyMoreThanPO(
        BusinessRuleExHolder ggPreventItemsQtyMoreThanPO)
    {
        this.ggPreventItemsQtyMoreThanPO = ggPreventItemsQtyMoreThanPO;
    }


    public BusinessRuleExHolder getGigPreventItemsLessThanRtv()
    {
        return gigPreventItemsLessThanRtv;
    }


    public void setGigPreventItemsLessThanRtv(
        BusinessRuleExHolder gigPreventItemsLessThanRtv)
    {
        this.gigPreventItemsLessThanRtv = gigPreventItemsLessThanRtv;
    }


    public BusinessRuleExHolder getGigPreventItemsQtyMoreThanRtv()
    {
        return gigPreventItemsQtyMoreThanRtv;
    }


    public void setGigPreventItemsQtyMoreThanRtv(
        BusinessRuleExHolder gigPreventItemsQtyMoreThanRtv)
    {
        this.gigPreventItemsQtyMoreThanRtv = gigPreventItemsQtyMoreThanRtv;
    }


    public BusinessRuleExHolder getPgNeedValidateConPo()
    {
        return pgNeedValidateConPo;
    }


    public void setPgNeedValidateConPo(BusinessRuleExHolder pgNeedValidateConPo)
    {
        this.pgNeedValidateConPo = pgNeedValidateConPo;
    }


    public BusinessRuleExHolder getDsdNeedValidateSalesData()
    {
        return dsdNeedValidateSalesData;
    }


    public void setDsdNeedValidateSalesData(
        BusinessRuleExHolder dsdNeedValidateSalesData)
    {
        this.dsdNeedValidateSalesData = dsdNeedValidateSalesData;
    }


    public BusinessRuleExHolder getGgDailyNotificationJobMissingGiMinBufferingDays()
    {
        return ggDailyNotificationJobMissingGiMinBufferingDays;
    }


    public void setGgDailyNotificationJobMissingGiMinBufferingDays(
        BusinessRuleExHolder ggDailyNotificationJobMissingGiMinBufferingDays)
    {
        this.ggDailyNotificationJobMissingGiMinBufferingDays = ggDailyNotificationJobMissingGiMinBufferingDays;
    }


    public BusinessRuleExHolder getGgDailyNotificationJobMissingGiMaxBufferingDays()
    {
        return ggDailyNotificationJobMissingGiMaxBufferingDays;
    }


    public void setGgDailyNotificationJobMissingGiMaxBufferingDays(
        BusinessRuleExHolder ggDailyNotificationJobMissingGiMaxBufferingDays)
    {
        this.ggDailyNotificationJobMissingGiMaxBufferingDays = ggDailyNotificationJobMissingGiMaxBufferingDays;
    }


    public BusinessRuleExHolder getMpigdMissingGiNotificationRecipients()
    {
        return mpigdMissingGiNotificationRecipients;
    }


    public void setMpigdMissingGiNotificationRecipients(
        BusinessRuleExHolder mpigdMissingGiNotificationRecipients)
    {
        this.mpigdMissingGiNotificationRecipients = mpigdMissingGiNotificationRecipients;
    }

    
    public BusinessRuleExHolder getGgRTVDnDisputeAlertWindow() {
  		return ggRTVDnDisputeAlertWindow;
  	}


  	public void setGgRTVDnDisputeAlertWindow(
  			BusinessRuleExHolder ggRTVDnDisputeAlertWindow) {
  		this.ggRTVDnDisputeAlertWindow = ggRTVDnDisputeAlertWindow;
  	}


	public BusinessRuleExHolder getGgDisableInvoicePaymentInstructions() {
		return ggDisableInvoicePaymentInstructions;
	}


	public void setGgDisableInvoicePaymentInstructions(
			BusinessRuleExHolder ggDisableInvoicePaymentInstructions) {
		this.ggDisableInvoicePaymentInstructions = ggDisableInvoicePaymentInstructions;
	}


    public BusinessRuleExHolder getGgRtvGiDnQtyTolerance()
    {
        return ggRtvGiDnQtyTolerance;
    }


    public void setGgRtvGiDnQtyTolerance(BusinessRuleExHolder ggRtvGiDnQtyTolerance)
    {
        this.ggRtvGiDnQtyTolerance = ggRtvGiDnQtyTolerance;
    }


    public BusinessRuleExHolder getGgRtvGiDnPriceTolerance()
    {
        return ggRtvGiDnPriceTolerance;
    }


    public void setGgRtvGiDnPriceTolerance(
        BusinessRuleExHolder ggRtvGiDnPriceTolerance)
    {
        this.ggRtvGiDnPriceTolerance = ggRtvGiDnPriceTolerance;
    }


    public BusinessRuleExHolder getGgRtvGiDnReportGeneratingDateRange()
    {
        return ggRtvGiDnReportGeneratingDateRange;
    }


    public void setGgRtvGiDnReportGeneratingDateRange(
        BusinessRuleExHolder ggRtvGiDnReportGeneratingDateRange)
    {
        this.ggRtvGiDnReportGeneratingDateRange = ggRtvGiDnReportGeneratingDateRange;
    }


    public BusinessRuleExHolder getGgRtvGiDnQtyToleranceType()
    {
        return ggRtvGiDnQtyToleranceType;
    }


    public void setGgRtvGiDnQtyToleranceType(
        BusinessRuleExHolder ggRtvGiDnQtyToleranceType)
    {
        this.ggRtvGiDnQtyToleranceType = ggRtvGiDnQtyToleranceType;
    }


    public BusinessRuleExHolder getGgRtvGiDnPriceToleranceType()
    {
        return ggRtvGiDnPriceToleranceType;
    }


    public void setGgRtvGiDnPriceToleranceType(
        BusinessRuleExHolder ggRtvGiDnPriceToleranceType)
    {
        this.ggRtvGiDnPriceToleranceType = ggRtvGiDnPriceToleranceType;
    }


    public BusinessRuleExHolder getMpigdRtvGiDnExceptionReportRecipients()
    {
        return mpigdRtvGiDnExceptionReportRecipients;
    }


    public void setMpigdRtvGiDnExceptionReportRecipients(
        BusinessRuleExHolder mpigdRtvGiDnExceptionReportRecipients)
    {
        this.mpigdRtvGiDnExceptionReportRecipients = mpigdRtvGiDnExceptionReportRecipients;
    }


    public BusinessRuleExHolder getPcicpIgnoreExpiryDate()
    {
        return pcicpIgnoreExpiryDate;
    }


    public void setPcicpIgnoreExpiryDate(BusinessRuleExHolder pcicpIgnoreExpiryDate)
    {
        this.pcicpIgnoreExpiryDate = pcicpIgnoreExpiryDate;
    }

}
