package com.pracbiz.b2bportal.core.action;

import java.io.File;
import java.io.IOException;
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

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.LogoFileChecker;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.SupplierSourceType;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.CountryHolder;
import com.pracbiz.b2bportal.core.holder.CurrencyHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierSharedHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierMsgSettingExHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.CountryService;
import com.pracbiz.b2bportal.core.service.CurrencyService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupTmpService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.SupplierSharedService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.ChannelConfigHelper;

public class SupplierAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(SupplierAction.class);
    private static final long serialVersionUID = -82549606088144109L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_SUPPLIER = "SEARCH_PARAMETER_SUPPLIER";
    private static final String SESSION_OID_PARAMETER = "session.parameter.SupplierAction.selectedOids";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String REQUEST_OID_DELIMITER = "\\-";
    private static final Map<String, String> sortMap;
    private static final List<String> hiddenMsgs;
    private static final List<String> hiddenFileFormatMsgs;
    private static final String SECT_ID_HSEKEEP = "HSEKEEP";
    private static final String PARAM_ID_INBOUND = "INBOUND";
    private static final String PARAM_ID_OUTBOUND = "OUTBOUND";
    private static final String SESSION_SUPPLIER_MSG_SETTING_INBOUND = "EDIT_SUPPLIER_MSG_SETTING_INBOUND";
    private static final String SESSION_SUPPLIER_MSG_SETTING_OUTBOUND = "EDIT_SUPPLIER_MSG_SETTING_OUTBOUND";
    private static final String SESSION_SUPPLIER_TERM_CONDITION_OIDS = "SESSION_SUPPLIER_TERM_CONDITION_OIDS";
    private static final String SESSION_SUPPLIER_SHARED_OLD = "SESSION_SUPPLIER_SHARED_OLD";
    
    private static final String GST_NO_REGEX = "^[a-zA-Z0-9-_]*$";
    private static final String INV_START_NO_REGEX = "^[0-9]*$";
    
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("supplierCode", "SUPPLIER_CODE");
        sortMap.put("supplierName", "SUPPLIER_NAME");
        sortMap.put("mboxId", "MBOX_ID");
        sortMap.put("active", "ACTIVE");
        sortMap.put("blocked", "BLOCKED");
        
        hiddenMsgs = new ArrayList<String>();
        hiddenMsgs.add("SM");
        hiddenMsgs.add("ST");
        hiddenMsgs.add("UM");
        hiddenMsgs.add("SA");
        hiddenMsgs.add("DPR");
        hiddenMsgs.add("IM");
        
        hiddenFileFormatMsgs = new ArrayList<String>();
        hiddenFileFormatMsgs.add("ITEMIN");
    }
    
    private SupplierExHolder supplier;
    private SupplierExHolder supplierEx;
    private List<CountryHolder> countries;
    private List<CurrencyHolder> currencies;
    private Map<Boolean, String> status;
    private Map<String, String> deploymentMode;
    private List<String> channels;
    private Map<String, String> source;
    
    @Autowired
    private transient SupplierService supplierService;
    @Autowired
    private transient CountryService countryService;
    @Autowired
    private transient CurrencyService currencyService;
    @Autowired
    private transient OidService oidService;
    @Autowired
    private transient SupplierMsgSettingService supplierMsgSettingService;
    @Autowired
    private transient ControlParameterService controlParameterService;
    @Autowired
    private transient TermConditionService termConditionService;
    @Autowired
    private transient TradingPartnerService tradingPartnerService;
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
    private transient SupplierSharedService supplierSharedService;

    
    private File logo;
    private String logoContentType;
    private transient InputStream imageResult; 
    private String contentType; 
    private String isStartNum;
    private String isGst;
    private Map<String,SupplierMsgSettingExHolder> inbounds;
    private Map<String,SupplierMsgSettingExHolder> outbounds;
    private List<String> inboundMsg;
    private List<String> outboundMsg;
    
    private List<TermConditionHolder> termConditions;
    private String defaultSelected;
    private List<SupplierHolder> availableSuppliers;
    private List<SupplierHolder> selectedSuppliers;
    private List<BigDecimal> selectedSupplierOids;
    
    public SupplierAction()
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_SUPPLIER);
        
        supplier = (SupplierExHolder)getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_SUPPLIER);
       
        try
        {
            status = new HashMap<Boolean, String>();
            status.put(true, this.getText("select.active"));
            status.put(false, this.getText("select.inactive"));
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);

            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));

            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);

            msg.addMessageTarget(mt);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String search()
    {
        if (null == supplier)
        {
            supplier = new SupplierExHolder();
        }
        
        if ("".equals(this.getRequest().getParameter("supplier.active")))
        {
            supplier.setActive(null);
        }

        try
        {
            supplier.trimAllString();
            supplier.setAllEmptyStringToNull();
            
            supplier.setCurrentUserTypeOid(this.getUserTypeOfCurrentUser());
            if(BigDecimal.valueOf(3).equals(supplier.getCurrentUserTypeOid())
                || BigDecimal.valueOf(5).equals(
                    supplier.getCurrentUserTypeOid()))
            {
                // For supplier admin & user
                supplier.setCurrentUserSupplierOid(this
                    .getProfileOfCurrentUser().getSupplierOid());
            }
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        getSession().remove(CommonConstants.SESSION_CHANGED);
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_SUPPLIER, supplier);

        return SUCCESS;
    }


    public String data()
    {
        try
        {
            SupplierExHolder searchParam = (SupplierExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_SUPPLIER);
            
            if (null == searchParam)
            {
                searchParam = new SupplierExHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_SUPPLIER, searchParam);
            }
            searchParam.setCurrentUserTypeOid(this.getUserTypeOfCurrentUser());
            if(BigDecimal.valueOf(3).equals(searchParam.getCurrentUserTypeOid())
                    || BigDecimal.valueOf(5).equals(
                            searchParam.getCurrentUserTypeOid()))
            {
                // For supplier admin & user
                searchParam.setCurrentUserSupplierOid(this
                        .getProfileOfCurrentUser().getSupplierOid());
            }

            this.obtainListRecordsOfPagination(supplierService, searchParam,
                sortMap, "supplierOid", null);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }


    // *****************************************************
    // init view
    // *****************************************************
    public String initView()
    {
        try
        {
            SupplierHolder currSupplier = supplierService
                .selectSupplierWithBlobsByKey(supplier.getSupplierOid());
            
            BeanUtils.copyProperties(currSupplier, supplier);

            supplier.setCtryDesc(countryService.selectByCtryCode(
                supplier.getCtryCode()).getCtryDesc());
            supplier.setCurrDesc(currencyService.selectByCurrCode(
                supplier.getCurrCode()).getCurrDesc());

            if ((supplier.getGstPercent() == null)
                || (supplier.getGstRegNo() == null))
            {
                isGst = VALUE_NO;
            }
            else
            {
                isGst = VALUE_YES;
            }

            if(supplier.getStartNumber() == null)
            {
                isStartNum = VALUE_NO;
            }
            else
            {
                isStartNum = VALUE_YES;
            }
            
            if(currSupplier.getGstPercent() != null)
            {
                supplier.setGstPercentStr(currSupplier.getGstPercent()
                    .toString());
            }
            
            if(currSupplier.getStartNumber() != null)
            {
                supplier.setStartNumberStr(currSupplier.getStartNumber()
                    .toString());
            }
        }
        catch (Exception e)
        {
            this.handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String initSelected()
    {
        return SUCCESS;
    }


    private void initSelect() throws Exception
    {
        CountryHolder coutry = new CountryHolder();
        coutry.setSortField("CTRY_DESC");
        coutry.setSortOrder("ASC");
        
        countries = countryService.select(coutry);
        
        CurrencyHolder currency = new CurrencyHolder();
        currency.setSortField("CURR_DESC");
        currency.setSortOrder("ASC");
        currencies = currencyService.select(currency);
        deploymentMode = DeploymentMode.toMapValue();
        channels = channelConfigHelper.getSupplierChannels();
        source = SupplierSourceType.toMapValue(this);
    }
   
    
    // *****************************************************
    // create page
    // *****************************************************
    public String initAdd()
    {   
        try
        {
            supplier = new SupplierExHolder();
            supplier.setCtryCode("SG");
            supplier.setCurrCode("SGD");
            supplier.setActive(true);
            isStartNum = VALUE_NO;
            isGst = VALUE_NO;
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
            supplier.trimAllString();
            if (!flag
                    && DeploymentMode.REMOTE.name().equals(
                            supplier.getDeploymentMode().name())
                    && (supplier.getChannel() == null || supplier.getChannel()
                            .equals("")))
            {
                this.addActionError(this.getText("B2BPC0489"));
                flag = true;
            }
            
            if(!flag && supplier.getMboxId().length() > 10)
            {
                this.addActionError(this.getText("B2BPC0446"));
                flag = true;
            }
            
            if(!flag && supplier.getSupplierCode().length() > 20)
            {
                this.addActionError(this.getText("B2BPC0445"));
                flag = true;
            }
            
            if(!flag
                && supplierService.selectSupplierByMboxId(supplier.getMboxId()) != null)
            {
                this.addActionError(this.getText("B2BPC0430",
                    new String[] {supplier.getMboxId()}));
                flag = true;
            }

            if(!flag
                && supplierService.selectSupplierByCode(supplier
                    .getSupplierCode()) != null)
            {
                this.addActionError(this.getText("B2BPC0425",
                    new String[] {supplier.getSupplierCode()}));
                flag = true;
            }
            
            if(!flag && supplier.getSupplierName().length() > 50)
            {
                this.addActionError(this.getText("B2BPC0447"));
                flag = true;
            }
            
            if(!flag && supplier.getSupplierAlias() != null
                && supplier.getSupplierAlias().length() > 50)
            {
                this.addActionError(this.getText("B2BPC0448"));
                flag = true;
            }
            
            if (!flag
                    && supplier.getRegNo() != null
                    && (supplier.getRegNo().length() > 50 || !Pattern.matches(
                            GST_NO_REGEX, supplier.getRegNo())))
            {
                this.addActionError(this.getText("B2BPC0449"));
                flag = true;
            }

            if (!flag
                    && supplier.getOtherRegNo() != null
                    && (supplier.getOtherRegNo().length() > 50 || !Pattern
                            .matches(GST_NO_REGEX, supplier.getOtherRegNo())))
            {
                this.addActionError(this.getText("B2BPC0500"));
                flag = true;
            }
        
            if(!flag && (supplier.getAddress1().length() > 100))
            {
                this.addActionError(this.getText("B2BPC0453"));
                flag = true;
            }
            
            if(!flag && supplier.getAddress2() != null
                && supplier.getAddress2().length() > 100)
            {
                this.addActionError(this.getText("B2BPC0454"));
                flag = true;
            }
            
            if(!flag && supplier.getAddress3() != null
                && supplier.getAddress3().length() > 100)
            {
                this.addActionError(this.getText("B2BPC0455"));
                flag = true;
            }

            if(!flag && supplier.getAddress4() != null
                && supplier.getAddress4().length() > 100)
            {
                this.addActionError(this.getText("B2BPC0456"));
                flag = true;
            }
            
            if(!flag && supplier.getState() != null
                && supplier.getState().length() > 50)
            {
                this.addActionError(this.getText("B2BPC0457"));
                flag = true;
            }
            
            if(!flag && supplier.getPostalCode() != null
                && supplier.getPostalCode().length() > 15)
            {
                this.addActionError(this.getText("B2BPC0458"));
                flag = true;
            }
            
            if(!flag && (supplier.getContactName().length() > 50))
            {
                this.addActionError(this.getText("B2BPC0459"));
                flag = true;
            }

            if(!flag && (supplier.getContactTel().length() > 30))
            {
                this.addActionError(this.getText("B2BPC0460"));
                flag = true;
            }
            
            if(!flag && supplier.getContactMobile() != null
                && supplier.getContactMobile().length() > 30)
            {
                this.addActionError(this.getText("B2BPC0461"));
                flag = true;
            }

            if(!flag && supplier.getContactFax() != null
                && supplier.getContactFax().length() > 30)
            {
                this.addActionError(this.getText("B2BPC0462"));
                flag = true;
            }

            if(!flag && supplier.getContactEmail() != null
                && supplier.getContactEmail().length() > 100)
            {
                this.addActionError(this.getText("B2BPC0463"));
                flag = true;
            }
            
            if(!flag && VALUE_YES.equalsIgnoreCase(isStartNum))
            {
                if(StringUtils.isBlank(supplier.getStartNumberStr()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0469"));
                }

                if(!flag
                    && this.matcher(INV_START_NO_REGEX,
                        supplier.getStartNumberStr()))
                {
                    this.addActionError(getText("B2BPC0444"));
                    flag = true;
                }
            }
            
            if(!flag && VALUE_YES.equalsIgnoreCase(isGst))
            {

                if(StringUtils.isBlank(supplier.getGstPercentStr()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0470"));
                }

                if(!flag
                    && matcher("^-?(?:\\d+|\\d{1,3}(?:,\\d{3})+)(?:\\.\\d+)?$",
                        supplier.getGstPercentStr()))
                {
                    this.addActionError(getText("B2BPC0472"));
                    flag = true;
                }

                if(!flag && supplier.getGstPercentStr().length() > 5)
                {
                    this.addActionError(this.getText("B2BPC0451"));
                    flag = true;
                }

                if(!flag)
                {
                    supplier.setGstPercent(new BigDecimal(supplier
                        .getGstPercentStr()));
                    if((supplier.getGstPercent().compareTo(
                        new BigDecimal("100")) > 0)
                        || (supplier.getGstPercent().compareTo(BigDecimal.ZERO)) < 0)
                    {
                        flag = true;
                        this.addActionError(this.getText("B2BPC0484"));
                    }
                }

                if(!flag && StringUtils.isBlank(supplier.getGstRegNo()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0471"));
                }

                if(!flag && (supplier.getGstRegNo().length() > 50 || !Pattern.matches(GST_NO_REGEX, supplier.getGstRegNo()
                        .toString())))
                {
                    this.addActionError(this.getText("B2BPC0450"));
                    flag = true;
                }
            }
            
            if(logo != null)
            {
                boolean logoCheck = true;
                if(logoContentType.equalsIgnoreCase("image/tif")
                        || logoContentType
                        .equalsIgnoreCase("image/jpg")
                || logoContentType
                        .equalsIgnoreCase("image/gif")
                || logoContentType
                        .equalsIgnoreCase("image/jpeg")
                || logoContentType
                        .equalsIgnoreCase("image/png")
                || logoContentType
                        .equalsIgnoreCase("image/bmp"))
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
                        this.addActionError(this.getText("B2BPC0426"));
                        flag = true;  
                    }
                }
                else
                {
                    this.addActionError(this.getText("B2BPC0426"));
                    flag = true;
                }
                
                if (!flag && logo.length() / 1024 > 100)
                {
                    this.addActionError(this.getText("B2BPC0476",new String[] {" "+logo.length()/1024}));
                    flag = true;
                }
            }
            
            if(flag)
            {
                initSelect(); 
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    

    public String saveAdd()
    {
        try
        {
            SupplierExHolder newSupplier = new SupplierExHolder();
            newSupplier.setSupplierOid(oidService.getOid());
            newSupplier.setSupplierCode(supplier.getSupplierCode());
            newSupplier.setSupplierName(supplier.getSupplierName());
            newSupplier.setSupplierAlias(supplier.getSupplierAlias());
            newSupplier.setRegNo(supplier.getRegNo());
            newSupplier.setOtherRegNo(supplier.getOtherRegNo());
            newSupplier.setSupplierSource(supplier.getSupplierSource());
        
            newSupplier.setAutoInvNumber(supplier.getAutoInvNumber());
            if(VALUE_YES.equals(isStartNum))
            {
                Integer gstStartNumber = Integer.valueOf((supplier.getStartNumberStr()));
                newSupplier.setStartNumber(gstStartNumber);
                newSupplier.setAutoInvNumber(Boolean.TRUE);
            }
            else
            {
                newSupplier.setAutoInvNumber(Boolean.FALSE);
                newSupplier.setStartNumber(null);
            }
            if(VALUE_YES.equals(isGst))
            {
                newSupplier.setGstRegNo(supplier.getGstRegNo());
                BigDecimal gstPercent = new BigDecimal(supplier.getGstPercentStr());
                newSupplier.setGstPercent(gstPercent);
            }
            else
            {
                newSupplier.setGstPercent(null);
                newSupplier.setGstRegNo(null);
            }
            newSupplier.setActive(supplier.getActive());
            newSupplier.setCreateBy(this.getLoginIdOfCurrentUser());
            newSupplier.setCreateDate(new Date());
            newSupplier.setBranch(Boolean.FALSE);
            newSupplier.setMboxId(supplier.getMboxId());
            newSupplier.setBlocked(supplier.getBlocked());
            if(supplier.getBlocked().equals(Boolean.TRUE))
            {
                newSupplier.setBlockBy(this.getLoginIdOfCurrentUser());
                newSupplier.setBlockDate(new Date());
            }
            if(logo != null)
            {
                newSupplier.setLogo(FileUtil.getInstance().readByteFromDisk(logo.getPath()));
            }
            newSupplier.setAddress1(supplier.getAddress1());
            if(!("".equals(supplier.getAddress2())) && (supplier.getAddress2()!=null))
            {
                newSupplier.setAddress2(supplier.getAddress2());
            }
            if(!("".equals(supplier.getAddress3())) && (supplier.getAddress3()!=null))
            {
                newSupplier.setAddress3(supplier.getAddress3());
            }
            if(!("".equals(supplier.getAddress4())) && (supplier.getAddress4()!=null))
            {
                newSupplier.setAddress4(supplier.getAddress4());
            }
            newSupplier.setState(supplier.getState());
            newSupplier.setPostalCode(supplier.getPostalCode());
            
            
            newSupplier.setCtryCode(supplier.getCtryCode());
            newSupplier.setCurrCode(supplier.getCurrCode());
            newSupplier.setContactName(supplier.getContactName());
            newSupplier.setContactTel(supplier.getContactTel());
            newSupplier.setContactMobile(supplier.getContactMobile());
            newSupplier.setContactFax(supplier.getContactFax());
            newSupplier.setContactEmail(supplier.getContactEmail());
            newSupplier.setChannel(supplier.getChannel());
            newSupplier.setDeploymentMode(supplier.getDeploymentMode());
            newSupplier.setClientEnabled(supplier.getClientEnabled());
            newSupplier.setRequireReport(supplier.getRequireReport());
            newSupplier.setRequireTranslationIn(supplier.getRequireTranslationIn());
            newSupplier.setRequireTranslationOut(supplier.getRequireTranslationOut());
            this.addUnExistMsgSetting(newSupplier, true);
            supplierService.insertSupplierWithMsgSetting(getCommonParameter(), newSupplier);
            log.info(this.getText("B2BPC0491", new String[]{newSupplier.getSupplierCode(), this.getLoginIdOfCurrentUser()}));
            
        }    
        catch (Exception e)
        {
            this.handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }

    
    // *****************************************************
    // edit page
    // *****************************************************
    
    public String initEdit()
    {
        try
        {
            SupplierHolder currSupplier = supplierService
                .selectSupplierWithBlobsByKey(supplier.getSupplierOid());

            initSelect();

            BeanUtils.copyProperties(currSupplier, supplier);
            
            supplier.setCtryDesc(countryService.selectByCtryCode(
                supplier.getCtryCode()).getCtryDesc());
            supplier.setCurrDesc(currencyService.selectByCurrCode(
                supplier.getCurrCode()).getCurrDesc());


            if((supplier.getGstPercent() == null)
                || (supplier.getGstRegNo() == null))
            {
                isGst = VALUE_NO;
            }
            else
            {
                isGst = VALUE_YES;
            }

            if(supplier.getStartNumber() == null)
            {
                isStartNum = VALUE_NO;
            }
            else
            {
                isStartNum = VALUE_YES;
            }

            if(currSupplier.getGstPercent() != null)
            {
                String gstPercent = (currSupplier.getGstPercent()).toString();
                supplier.setGstPercentStr(gstPercent);
            }

            if(currSupplier.getStartNumber() != null)
            {
                String startNum = (currSupplier.getStartNumber()).toString();
                supplier.setStartNumberStr(startNum);
            }

        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);

            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                new String[] {tickNo}));

            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);

            msg.addMessageTarget(mt);

            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    public void validateSaveEdit()
    {
        
        if(supplier == null)
        {
            supplier = new SupplierExHolder();
        }
       
        try
        {
            boolean flag = this.hasFieldErrors();
            supplier.trimAllString();
            if (!flag
                    && DeploymentMode.REMOTE.name().equals(
                            supplier.getDeploymentMode().name())
                    && (supplier.getChannel() == null || supplier.getChannel()
                            .equals("")))
            {
                this.addActionError(this.getText("B2BPC0489"));
                flag = true;
            }
            if(!flag && supplier.getSupplierName().length() > 50)
            {
                this.addActionError(this.getText("B2BPC0447"));
                flag = true;
            }
            
            if(!flag && supplier.getSupplierAlias() != null
                && supplier.getSupplierAlias().length() > 50)
            {
                this.addActionError(this.getText("B2BPC0448"));
                flag = true;
            }
            
            if (!flag
                    && supplier.getRegNo() != null
                    && (supplier.getRegNo().length() > 50 || !Pattern.matches(
                            GST_NO_REGEX, supplier.getRegNo())))
            {
                this.addActionError(this.getText("B2BPC0449"));
                flag = true;
            }

            if (!flag
                    && supplier.getOtherRegNo() != null
                    && (supplier.getOtherRegNo().length() > 50 || !Pattern
                            .matches(GST_NO_REGEX, supplier.getOtherRegNo())))
            {
                this.addActionError(this.getText("B2BPC0500"));
                flag = true;
            }
        
            if(!flag && (supplier.getAddress1().length() > 100))
            {
                this.addActionError(this.getText("B2BPC0453"));
                flag = true;
            }
            
            if(!flag && supplier.getAddress2() != null
                && supplier.getAddress2().length() > 100)
            {
                this.addActionError(this.getText("B2BPC0454"));
                flag = true;
            }
            
            if(!flag && supplier.getAddress3() != null
                && supplier.getAddress3().length() > 100)
            {
                this.addActionError(this.getText("B2BPC0455"));
                flag = true;
            }
            
            if(!flag && supplier.getAddress4() != null
                && supplier.getAddress4().length() > 100)
            {
                this.addActionError(this.getText("B2BPC0456"));
                flag = true;
            }
            
            if(!flag && supplier.getState() != null
                && supplier.getState().length() > 50)
            {
                this.addActionError(this.getText("B2BPC0457"));
                flag = true;
            }
            
            if(!flag && supplier.getPostalCode() != null
                && supplier.getPostalCode().length() > 15)
            {
                this.addActionError(this.getText("B2BPC0458"));
                flag = true;
            }
            
            if(!flag && (supplier.getContactName().length() > 50))
            {
                this.addActionError(this.getText("B2BPC0459"));
                flag = true;
            }
            
            if(!flag && (supplier.getContactTel().length() > 30))
            {
                this.addActionError(this.getText("B2BPC0460"));
                flag = true;
            }
            
            if(!flag && supplier.getContactMobile() != null
                && supplier.getContactMobile().length() > 30)
            {
                this.addActionError(this.getText("B2BPC0461"));
                flag = true;
            }
            
            if(!flag && supplier.getContactFax() != null
                && supplier.getContactFax().length() > 30)
            {
                this.addActionError(this.getText("B2BPC0462"));
                flag = true;
            }

            if(!flag && supplier.getContactEmail() != null
                && supplier.getContactEmail().length() > 100)
            {
                this.addActionError(this.getText("B2BPC0463"));
                flag = true;
            }
            
            if(!flag && VALUE_YES.equalsIgnoreCase(isStartNum))
            {
                if (StringUtils.isBlank(supplier.getStartNumberStr()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0469"));
                }
                
                if (!flag && matcher(INV_START_NO_REGEX, supplier.getStartNumberStr()))
                {
                    this.addActionError(getText("B2BPC0444"));
                    flag = true;
                }
            }
            
            if(!flag && VALUE_YES.equalsIgnoreCase(isGst))
            {
                
                if (StringUtils.isBlank(supplier.getGstPercentStr()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0470"));
                }
                
                if(!flag
                    && matcher("^-?(?:\\d+|\\d{1,3}(?:,\\d{3})+)(?:\\.\\d+)?$",
                        supplier.getGstPercentStr()))
                {
                    this.addActionError(getText("B2BPC0472"));
                    flag = true;
                }
                
                if(!flag && supplier.getGstPercentStr().length() > 5)
                {
                    this.addActionError(this.getText("B2BPC0451"));
                    flag = true;
                }
                
                if (!flag)
                {
                    supplier.setGstPercent(new BigDecimal(supplier.getGstPercentStr()));
                    if((supplier.getGstPercent().compareTo(BigDecimal.valueOf(100)) > 0)
                        || (supplier.getGstPercent().compareTo(BigDecimal.ZERO)) < 0)
                    {
                        flag = true;
                        this.addActionError(this.getText("B2BPC0484"));
                    }
                }
                

                if( !flag && StringUtils.isBlank(supplier.getGstRegNo()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0471"));
                }
                
                if(!flag && (supplier.getGstRegNo().length() > 50 || !Pattern.matches(GST_NO_REGEX, supplier.getGstRegNo()
                        .toString())))
                {
                    this.addActionError(this.getText("B2BPC0450"));
                    flag = true;
                }
            }
            
            if(logo != null)
            {
                boolean logoCheck = true;
                if(logoContentType.equalsIgnoreCase("image/tif")
                        || logoContentType
                        .equalsIgnoreCase("image/jpg")
                || logoContentType
                        .equalsIgnoreCase("image/gif")
                || logoContentType
                        .equalsIgnoreCase("image/jpeg")
                || logoContentType
                        .equalsIgnoreCase("image/png")
                || logoContentType
                        .equalsIgnoreCase("image/bmp"))
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
                        this.addActionError(this.getText("B2BPC0426"));
                        flag = true;  
                    }
                }
                else
                {
                    this.addActionError(this.getText("B2BPC0426"));
                    flag = true;
                }
                
                if (!flag && logo.length() / 1024 > 100)
                {
                    this.addActionError(this.getText("B2BPC0476",new String[] {" "+logo.length()/1024}));
                    flag = true;
                }
            }
            
            if(flag)
            {
                SupplierHolder me = supplierService.selectSupplierWithBlobsByKey(supplier.getSupplierOid());
                supplier.setSupplierCode(me.getSupplierCode());
                supplier.setMboxId(me.getMboxId());
                supplier.setCreateDate(me.getCreateDate());
                supplier.setLogo(me.getLogo());
                initSelect(); 
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String saveEdit()
    {
        try
        {
            SupplierHolder me = supplierService
                .selectSupplierWithBlobsByKey(supplier.getSupplierOid());
            
            supplier.setSupplierCode(me.getSupplierCode());
            supplier.setMboxId(me.getMboxId());
            supplier.setBranch(me.getBranch());
            supplier.setCreateBy(me.getCreateBy());
            supplier.setCreateDate(me.getCreateDate());
            supplier.setTransMode(me.getTransMode());
            
            supplier.setUpdateBy(this.getLoginIdOfCurrentUser());
            supplier.setUpdateDate(new Date());
            
            if(supplier.getBlocked().equals(Boolean.TRUE))
            {
                supplier.setBlockBy(this.getLoginIdOfCurrentUser());
                supplier.setBlockDate(new Date());
            }
            
            if(logo == null)
            {
                supplier.setLogo(me.getLogo());
            }
            else
            {
                supplier.setLogo(FileUtil.getInstance().readByteFromDisk(logo.getPath()));
            }
            
            if(VALUE_YES.equals(isStartNum))
            {
                Integer gstStartNumber = Integer.valueOf((supplier.getStartNumberStr()));
                supplier.setStartNumber(gstStartNumber);
                supplier.setAutoInvNumber(Boolean.TRUE);
            }
            else
            {
                supplier.setAutoInvNumber(Boolean.FALSE);
                supplier.setStartNumber(null);
            }
            
            if(VALUE_YES.equals(isGst))
            {
                supplier.setGstRegNo(supplier.getGstRegNo());
                BigDecimal gstPercent = new BigDecimal(supplier.getGstPercentStr());
                supplier.setGstPercent(gstPercent);
            }
            else
            {
                supplier.setGstPercent(null);
                supplier.setGstRegNo(null);
            }
            supplierService.auditUpdateByPrimaryKeyWithBLOBs(this.getCommonParameter(), me, supplier);
            log.info(this.getText("B2BPC0492", new String[]{supplier.getSupplierCode(), this.getLoginIdOfCurrentUser()}));
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // delete page
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
            
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal supplierOid = new BigDecimal(part);
                SupplierHolder  supplier = supplierService.selectSupplierByKey(supplierOid);
                if(supplier == null)
                {
                    msg.saveError(this.getText("B2BPC0423"));
                }
                else if (FileUtil.getInstance().isAnyFilesExistInFolder(           //appConfig.getBuyerMailboxRootPath()
                        new File(mboxUtil.getBuyerMailBox(supplier.getMboxId())))) // + File.separator + supplier.getMboxId()
                {
                    msg.saveError(this.getText("B2BPC0487",
                            new String[] { supplier.getSupplierCode() }));
                }
                else if(msgTransactionsService.selectMsgTransactionsBySupplierOid(supplierOid) == null)
                {
                    if(this.checkGroup(supplierOid) && this.checkUser(supplierOid))
                    {
                        CommonParameterHolder cp = new CommonParameterHolder();
                        BeanUtils.copyProperties(this.getCommonParameter(), cp);
                        cp.setMkMode(false);
                        supplierService.deleteSupplier(cp, supplierOid);
                        log.info(this.getText("B2BPC0493", new String[]{supplier.getSupplierCode(), this.getLoginIdOfCurrentUser()}));
                        msg.saveSuccess(this.getText("B2BPC0405", new String[]{supplier.getSupplierName()}));
                    }
                    else
                    {
                        msg.saveError(this.getText("B2BPC0486",
                                new String[] { supplier.getSupplierCode() }));
                    }
                    
                }
                else
                {
                    msg.saveError(this.getText("B2BPC0487",
                            new String[] { supplier.getSupplierCode() }));
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
    
    
    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
            new String[] {tickNo}));

        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

        msg.addMessageTarget(mt);
    }
    
    // *****************************************************
    // viewLogo page
    // *****************************************************
    public String viewImage()
    {
        try
        {
            SupplierHolder currSupplier = supplierService.selectSupplierWithBlobsByKey(supplier.getSupplierOid());
            byte[] image = currSupplier.getLogo();
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
    // msgSetting page
    // *****************************************************
    public String initEditMsgSetting()
    {
        try
        {
            inbounds = new HashMap<String, SupplierMsgSettingExHolder>();
            outbounds = new HashMap<String, SupplierMsgSettingExHolder>();
            inboundMsg = new ArrayList<String>();
            outboundMsg = new ArrayList<String>();
            if (supplier != null && supplier.getSupplierOid() != null)
            {
                SupplierHolder currSupplier = supplierService
                        .selectSupplierByKey(supplier.getSupplierOid());
                BeanUtils.copyProperties(currSupplier, supplier);
            
                SupplierMsgSettingHolder msg = new SupplierMsgSettingHolder();
                msg.setSupplierOid(supplier.getSupplierOid());
                msg.setSortField("SUPPLIER_OID,MSG_TYPE");
                msg.setSortOrder("ASC");
                List<SupplierMsgSettingHolder> supplierMsgSettings = supplierMsgSettingService.select(msg);
                if(supplierMsgSettings !=null)
                {
                    for(SupplierMsgSettingHolder o : supplierMsgSettings)
                    {
                        supplier.addSupplierMagSetting(o);
                    }
                }
                this.addUnExistMsgSetting(supplier,false);
                
                ControlParameterHolder cph = new ControlParameterHolder();
                cph.setSectId(SECT_ID_HSEKEEP);
                cph.setCatId(PARAM_ID_OUTBOUND);
                List<ControlParameterHolder> inboundTmp = controlParameterService.select(cph);
                cph.setSectId(SECT_ID_HSEKEEP);
                cph.setCatId(PARAM_ID_INBOUND);
                List<ControlParameterHolder> outboundTmp = controlParameterService.select(cph);
                
                List<SupplierMsgSettingHolder> currSupplierMsgSettings = supplier.getMsgSetting();
                if(currSupplierMsgSettings == null || currSupplierMsgSettings.isEmpty())
                {
                    return SUCCESS;
                }
                
                for(int i = 0; i < currSupplierMsgSettings.size(); i++)
                {
                    SupplierMsgSettingHolder supplierMsg = currSupplierMsgSettings.get(i);
                    
                    if (hiddenMsgs.contains(supplierMsg.getMsgType()))
                    {
                        currSupplierMsgSettings.remove(i);
                        i--;
                        
                        continue;
                    }
                    
                    if(inboundTmp != null && !inboundTmp.isEmpty())
                    {
                        for(int j=0; j < inboundTmp.size();j++)
                        {
                            cph = (ControlParameterHolder) inboundTmp.get(j);
                            if(supplierMsg.getMsgType().equals(cph.getParamId()))
                            {
                               SupplierMsgSettingExHolder supplierMsgEx = new SupplierMsgSettingExHolder();
                               BeanUtils.copyProperties(supplierMsg, supplierMsgEx);
                               if (!hiddenFileFormatMsgs.contains(supplierMsg.getMsgType()))
                               {
                                   supplierMsgEx.setFileFormatList(appConfig.getFileFormatListByMsgType(cph.getParamId()));
                               }
                               inbounds.put(supplierMsg.getMsgType(), supplierMsgEx);
                               inboundMsg.add(supplierMsg.getMsgType());
                               break;
                            }
                        }
                    }
                    
                    if(outboundTmp !=null && !outboundTmp.isEmpty())
                    {
                        for(int j=0; j < outboundTmp.size(); j++)
                        {
                            cph = (ControlParameterHolder) outboundTmp.get(j);
                            if(supplierMsg.getMsgType().equals(cph.getParamId()))
                            {
                                SupplierMsgSettingExHolder supplierMsgEx = new SupplierMsgSettingExHolder();
                                BeanUtils.copyProperties(supplierMsg, supplierMsgEx);
                                if (!hiddenFileFormatMsgs.contains(supplierMsg.getMsgType()))
                                {
                                    supplierMsgEx.setFileFormatList(appConfig.getFileFormatListByMsgType(cph.getParamId()));
                                }
                                outbounds.put(supplierMsg.getMsgType(), supplierMsgEx);
                                outboundMsg.add(supplierMsg.getMsgType());
                                break;
                            }
                        }
                    }
                    
                }
                setMsgDesc(inbounds);
                setMsgDesc(outbounds);
                this.getSession().put(SESSION_SUPPLIER_MSG_SETTING_INBOUND, inboundMsg);
                this.getSession().put(SESSION_SUPPLIER_MSG_SETTING_OUTBOUND, outboundMsg);
            }
            
        }
        catch (Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public void validateSaveEditMsgSetting()
    {
        try
        {
            boolean flag = this.hasActionErrors();
            if (!flag)
            {
                Iterator<String> out = outbounds.keySet().iterator();
                Iterator<String> in = inbounds.keySet().iterator();
                while(in.hasNext())
                {
                    String inStr = in.next().toString();
                    SupplierMsgSettingHolder inMsg = inbounds.get(inStr);
                    inMsg.trimAllString();
                    inMsg.setAllEmptyStringToNull();
                    if(inMsg.getRcpsAddrs() != null)
                    {
                        String[] emails = inMsg.getRcpsAddrs().trim().split(",");
                        boolean i = true;
                        for(String email : emails)
                        {
                            if (!Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email))
                            {
                                i = false;
                            }
                        }
                        if(!i)
                        {
                            this.addActionError(this.getText("B2BPC0480", new String[] { inStr }));
                            flag = true;
                        }
                        
                    }
                }
                while(out.hasNext())
                {
                    String outStr = out.next().toString();
                    SupplierMsgSettingHolder outMsg = outbounds.get(outStr);
                    outMsg.trimAllString();
                    outMsg.setAllEmptyStringToNull();
                    if(outMsg.getRcpsAddrs() != null)
                    {
                        String[] emails = outMsg.getRcpsAddrs().trim().split(",");
                        boolean o = true;
                        for(String email : emails)
                        {
                            if (!Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email))
                            {
                                o = false;
                            }
                        }
                        if(!o)
                        {
                            this.addActionError(this.getText("B2BPC0480", new String[] { outStr }));
                            flag = true;
                        }
                        }
                    }
                
            }
            if(flag)
            {
                SupplierHolder currSupplier = supplierService
                        .selectSupplierWithBlobsByKey(supplier.getSupplierOid());
                supplier = new SupplierExHolder();
                BeanUtils.copyProperties(currSupplier, supplier);
                inboundMsg = (List<String>) this.getSession().get(
                        SESSION_SUPPLIER_MSG_SETTING_INBOUND);
                outboundMsg = (List<String>) this.getSession().get(
                        SESSION_SUPPLIER_MSG_SETTING_OUTBOUND);
                if(inbounds != null)
                {
                    for(String key : inbounds.keySet())
                    {
                        SupplierMsgSettingExHolder setting = inbounds.get(key);
                        setting.setFileFormatList(appConfig.getFileFormatListByMsgType(key));
                    }
                }
                if(outbounds != null)
                {
                    for(String key : outbounds.keySet())
                    {
                        SupplierMsgSettingExHolder setting = outbounds.get(key);
                        setting.setFileFormatList(appConfig.getFileFormatListByMsgType(key));
                    }
                }
                setMsgDesc(inbounds);
                setMsgDesc(outbounds);
            }
        }catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    public String saveEditMsgSetting()
    {
        try{
            List<SupplierMsgSettingHolder> msgItems = new ArrayList<SupplierMsgSettingHolder>();
            
            Iterator<String> it = inbounds.keySet().iterator();
            while(it.hasNext())
            {
                String key = it.next().toString();
                SupplierMsgSettingHolder obj = inbounds.get(key);
                obj.setSupplierOid(supplier.getSupplierOid());
                obj.setExcludeSucc(false);
                obj.setMsgType(key);
                msgItems.add(obj);
            }
            
            it = outbounds.keySet().iterator();
            while(it.hasNext())
            {
                String key = it.next().toString();
                SupplierMsgSettingHolder obj = outbounds.get(key);
                obj.setSupplierOid(supplier.getSupplierOid());
                obj.setExcludeSucc(obj.getExcludeSucc() == null ? false : true);
                obj.setMsgType(key);
                msgItems.add(obj);
            }
            
            SupplierHolder currSupplier = supplierService.selectSupplierByKey(supplier.getSupplierOid());
            List<SupplierMsgSettingHolder> supplierMsgSettings = supplierMsgSettingService.selectSupplierMsgSettingBySupplierOid(supplier.getSupplierOid());
            if(supplierMsgSettings !=null &&!supplierMsgSettings.isEmpty())
            {
                for(SupplierMsgSettingHolder o : supplierMsgSettings )
                {
                    currSupplier.addSupplierMagSetting(o);
                }
            }
            SupplierHolder newSupplier = new SupplierHolder();
            BeanUtils.copyProperties(currSupplier, newSupplier);
            newSupplier.setMsgSetting(msgItems);
            supplierService.updateSupplierWithMsgSetting(this.getCommonParameter(), currSupplier, newSupplier);
            log.info(this.getText("B2BPC0494", new String[]{supplier.getSupplierCode(), this.getLoginIdOfCurrentUser()}));
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            msg.saveSuccess(this.getText("B2BPC0415", new String[]{currSupplier.getSupplierName()}));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            
            msg.addMessageTarget(mt);
            
        }catch(Exception e)
        {
            this.handleException(e);
        }
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    private void setMsgDesc(Map<String, SupplierMsgSettingExHolder> bound)
    {
        if (bound == null || bound.isEmpty())
        {
            return;
        }
        String key = null;
        for(Map.Entry<String, SupplierMsgSettingExHolder> entry : bound.entrySet())
        {
            key = entry.getKey();
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
    // edit term and condition
    // *****************************************************
    public String initEditTermAndCondition()
    {
        try
        {
            if(supplier != null)
            {
                List<TermConditionHolder> termConditionList = termConditionService.selectTermsConditionsBySupplierOid(supplier.getSupplierOid());
                termConditions  = new ArrayList<TermConditionHolder>();
                List<BigDecimal> oidList = new ArrayList<BigDecimal>();
                if(termConditionList != null)
                {
                    Iterator<TermConditionHolder> it = termConditionList.iterator();
                    while(it.hasNext())
                    {
                        TermConditionHolder termCondition = (TermConditionHolder) it.next();
                        TermConditionHolder termConditionEx = new TermConditionHolder();
                        BeanUtils.copyProperties(termCondition, termConditionEx);
                        TradingPartnerExHolder tp = new TradingPartnerExHolder();
                        tp.setTermConditionOid(termConditionEx.getTermConditionOid());
                        termConditions.add(termConditionEx);
                        oidList.add(termConditionEx.getTermConditionOid());
                    }
                }
                this.getSession().put(SESSION_SUPPLIER_TERM_CONDITION_OIDS, oidList);
            }
            return SUCCESS;
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public void validateSaveEditTermAndCondition()
    {
        try
        {
            boolean flag = this.hasActionErrors();
            List<TermConditionHolder> tmpList = new ArrayList<TermConditionHolder>();
            if (termConditions != null)
            {
                tmpList.addAll(termConditions);
            }
            List<BigDecimal> oidList = (List<BigDecimal>) this.getSession().get(SESSION_SUPPLIER_TERM_CONDITION_OIDS);
            if(!flag && tmpList != null && !tmpList.isEmpty())
            {
                termConditions.clear();
                Map<String, String> code = new HashMap<String, String>();
                if(defaultSelected == null)
                {
                    this.addActionError(this.getText("B2BPC0465"));
                    flag = true;
                }
                for(int i = 0; i < tmpList.size(); i++)
                {
                    String key = String.valueOf(i);
                    TermConditionHolder obj = tmpList.get(i);
                    if(obj != null)
                    {
                        obj.trimAllString();
                        obj.setAllEmptyStringToNull();
                        if(obj.getTermConditionCode() == null)
                        {
                            this.addActionError(this.getText("B2BPC0418", new String[]{"Terms & Conditions Code #"+(i+1)}));
                            flag = true;
                        }
                        if(!flag && !Pattern.matches("^[a-zA-Z0-9][\\w]*$", obj.getTermConditionCode()))
                        {
                            this.addActionError(this.getText("B2BPC0485", new String[]{"Terms & Conditions Code #"+(i+1)}));
                            flag = true;
                        }
                        if(!flag && (code.get(obj.getTermConditionCode()) != null))
                        {
                            this.addActionError(this.getText("B2BPC0467", new String[]{obj.getTermConditionCode(), String.valueOf(i+1)}));
                            flag = true;
                        }
                        if(!flag)
                        {
                            code.put(obj.getTermConditionCode(), key);
                        }
                        if(obj.getTermCondition1() == null || obj.getTermCondition1().length()>100)
                        {
                            this.addActionError(this.getText("B2BPC0464", new String[]{String.valueOf(i+1)}));
                            flag = true;
                        }
                        if(key.equals(defaultSelected))
                        {
                            obj.setDefaultSelected(true);
                        }
                        else
                        {
                            obj.setDefaultSelected(false);
                        }
                        if(oidList.size() >= i+1)
                        {
                            obj.setTermConditionOid(oidList.get(i));
                        }
                        termConditions.add(obj);
                    }
                }
            }
            if(flag)
            {
                termConditions.clear();
                for(int i = 0; i < tmpList.size(); i++)
                {
                    TermConditionHolder obj = tmpList.get(i);
                    if(obj != null)
                    {
                        obj.trimAllString();
                        obj.setAllEmptyStringToNull();
                        TradingPartnerExHolder tp = new TradingPartnerExHolder();
                        tp.setTermConditionOid(obj.getTermConditionOid());
                        termConditions.add(obj);
                        oidList.remove(null);
                    }
                }
            }
        }
        catch(Exception e)
        {
            this.handleException(e);
        }
    }
    
    
    public String saveEditTermAndCondition()
    {
        try
        {
            if(termConditions == null)
            {
                termConditions = new ArrayList<TermConditionHolder>();
            }
            SupplierHolder currSupplier = supplierService.selectSupplierByKey(supplier.getSupplierOid());
            List<TermConditionHolder> termList = termConditionService.selectTermsConditionsBySupplierOid(supplier.getSupplierOid());
            if(termList != null)
            {
                for(TermConditionHolder o : termList)
                {
                    currSupplier.addTermCondition(o);
                }
            }
            SupplierHolder newSupplier = new SupplierHolder();
            BeanUtils.copyProperties(currSupplier, newSupplier);
            newSupplier.setTermConditions(null);
            for(int i = 0; i < termConditions.size(); i++)
            {
                String key = String.valueOf(i);
                TermConditionHolder obj = termConditions.get(i);
                if(obj.getTermConditionOid() == null)
                {
                    obj.setTermConditionOid(oidService.getOid());
                }
                obj.setSeq(Short.parseShort(key));
                obj.setSupplierOid(supplier.getSupplierOid());
                if(key.equals(defaultSelected))
                {
                    obj.setDefaultSelected(true);
                }
                else
                {
                    obj.setDefaultSelected(false);
                }
                newSupplier.addTermCondition(obj);
            }
            supplierService.updateSupplierWithMsgSetting(this.getCommonParameter(), currSupplier, newSupplier);
            log.info(this.getText("B2BPC0495", new String[]{supplier.getSupplierCode(), this.getLoginIdOfCurrentUser()}));
        }
        catch(Exception e)
        {
           this.handleException(e);
        }
           
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public void removeTermAndCondition()
    {
        int result = 1;
        try
        {
            int index = Integer.parseInt(this.getRequest().getParameter("index"));
            List<BigDecimal> oidList = (List<BigDecimal>) this.getSession().get(SESSION_SUPPLIER_TERM_CONDITION_OIDS);
            if(oidList.size() >= index + 1 )
            {
                BigDecimal tcOid = oidList.get(index);
                TradingPartnerExHolder tp = new TradingPartnerExHolder();
                tp.setTermConditionOid(tcOid);
                List<TradingPartnerHolder> tpList = tradingPartnerService.select(tp);
                if(tpList == null || tpList.isEmpty())
                {
                    oidList.set(index, null);
                }
                else
                {
                    result = 0;
                }
            }
        }
        catch(Exception e)
        {
            result = -1;
        }
        try
        {
            ServletActionContext.getResponse().setCharacterEncoding("utf-8");
            ServletActionContext.getResponse().getWriter().write(String.valueOf(result));
            ServletActionContext.getResponse().getWriter().close();
        }
        catch (IOException e)
        {
            this.handleException(e);
        }
    }
    
    
    private void addUnExistMsgSetting (SupplierHolder supplier,boolean createFlag) throws Exception
    {
        List<ControlParameterHolder> msgList = controlParameterService
            .selectCacheControlParametersBySectId(SECT_ID_HSEKEEP);

        if(msgList == null || msgList.isEmpty())
        {
            return;
        }
        
        if(createFlag)
        {
            for(ControlParameterHolder cp : msgList)
            {
                this.addSupplierMsgSetting(supplier,cp);
            }
            
            return;
        }
        
        List<SupplierMsgSettingHolder> supplierMsgSettings = supplier.getMsgSetting();
        for (ControlParameterHolder cp : msgList)
        {
            boolean flag = false;

            if (supplierMsgSettings != null)
            {
                for (SupplierMsgSettingHolder msgSetting : supplierMsgSettings)
                {
                    if(msgSetting.getMsgType().equals(cp.getParamId()))
                    {
                        flag = true;
                        break;
                    }
                }
            }

            if (!flag)
            {
                this.addSupplierMsgSetting(supplier, cp);
            }
        }
        
    }
    
    
    private void addSupplierMsgSetting(SupplierHolder supplier,
        ControlParameterHolder cp)
    {
        SupplierMsgSettingHolder msg = new SupplierMsgSettingHolder();
        msg.setSupplierOid(supplier.getSupplierOid());
        msg.setMsgType(cp.getParamId());
        msg.setExcludeSucc(false);
        msg.setFileFormat(appConfig.getDefaultFileFormat(cp.getParamId()));
        supplier.addSupplierMagSetting(msg);
    }
    
    private boolean matcher(String reg, String source)
    {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(source);
        
        if (!matcher.matches())
        {
            return true;
        }
        
        return false;
    }
    
    
    // *****************************************************
    // edit supplier shared
    // *****************************************************
    public String initEditSupplierShared()
    {
        try
        {
            SupplierHolder currSupp = supplierService.selectSupplierByKey(supplier.getSupplierOid());
            BeanUtils.copyProperties(currSupp, supplier);
            availableSuppliers = supplierService.select(new SupplierExHolder());
            availableSuppliers.remove(currSupp);
            selectedSuppliers = new ArrayList<SupplierHolder>();
            List<SupplierSharedHolder> supplierShareds = supplierSharedService.selectBySelfSupOid(supplier.getSupplierOid());
            if (supplierShareds != null && !supplierShareds.isEmpty())
            {
                for (SupplierSharedHolder supplierShared : supplierShareds)
                {
                    SupplierHolder supp = supplierService.selectSupplierByKey(supplierShared.getGrantSupOid());
                    selectedSuppliers.add(supp);
                    availableSuppliers.remove(supp);
                    currSupp.addSupplierShared(supplierShared);
                }
            }
            this.getSession().put(SESSION_SUPPLIER_SHARED_OLD, currSupp);
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String saveEditSupplierShared()
    {
        try
        {
            SupplierHolder oldSupplier = (SupplierHolder) this.getSession().get(SESSION_SUPPLIER_SHARED_OLD);
            SupplierHolder newSupplier = new SupplierHolder();
            BeanUtils.copyProperties(oldSupplier, newSupplier);
            newSupplier.setSupplierShareds(new ArrayList<SupplierSharedHolder>());
            if (selectedSupplierOids != null && !selectedSupplierOids.isEmpty())
            {
                for (BigDecimal supplierSharedOid : selectedSupplierOids)
                {
                    SupplierSharedHolder supplierShared = new SupplierSharedHolder();
                    supplierShared.setSelfSupOid(newSupplier.getSupplierOid());
                    supplierShared.setGrantSupOid(supplierSharedOid);
                    newSupplier.addSupplierShared(supplierShared);
                }
            }
            supplierService.updateSupplierShared(this.getCommonParameter(), oldSupplier, newSupplier);
            log.info(this.getText("B2BPC0496", new String[]{newSupplier.getSupplierCode(), this.getLoginIdOfCurrentUser()}));
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    
    
    // *****************************************************
    // check if the group can delete
    // *****************************************************
    private boolean checkGroup(BigDecimal supplierOid) throws Exception
    {
        GroupTmpHolder group = new GroupTmpHolder();
        group.setSupplierOid(supplierOid);
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
    private boolean checkUser(BigDecimal supplierOid) throws Exception
    {
        UserProfileTmpExHolder userProfile = new UserProfileTmpExHolder();
        userProfile.setSupplierOid(supplierOid);
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
    // setter and getter method
    // *****************************************************
    public SupplierExHolder getSupplier()
    {
        return supplier;
    }


    public void setSupplier(SupplierExHolder supplier)
    {
        this.supplier = supplier;
    }


    public SupplierExHolder getSupplierEx()
    {
        return supplierEx;
    }


    public void setSupplierEx(SupplierExHolder supplierEx)
    {
        this.supplierEx = supplierEx;
    }


    public List<CountryHolder> getCountries()
    {
        return countries;
    }


    public void setCountries(List<CountryHolder> countries)
    {
        this.countries = countries;
    }


    public List<CurrencyHolder> getCurrencies()
    {
        return currencies;
    }


    public void setCurrencies(List<CurrencyHolder> currencies)
    {
        this.currencies = currencies;
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


    public InputStream getImageResult()
    {
        return imageResult;
    }


    public void setImageResult(InputStream imageResult)
    {
        this.imageResult = imageResult;
    }


    public String getContentType()
    {
        return contentType;
    }


    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    
    public String getIsStartNum()
    {
        return isStartNum;
    }

    
    public void setIsStartNum(String isStartNum)
    {
        this.isStartNum = isStartNum;
    }

    
    public Map<String, SupplierMsgSettingExHolder> getInbounds()
    {
        return inbounds;
    }

    
    public void setInbounds(Map<String, SupplierMsgSettingExHolder> inbounds)
    {
        this.inbounds = inbounds;
    }

    
    public Map<String, SupplierMsgSettingExHolder> getOutbounds()
    {
        return outbounds;
    }

    
    public void setOutbounds(Map<String, SupplierMsgSettingExHolder> outbounds)
    {
        this.outbounds = outbounds;
    }

    
    public String getIsGst()
    {
        return isGst;
    }

    
    public void setIsGst(String isGst)
    {
        this.isGst = isGst;
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


    public List<TermConditionHolder> getTermConditions()
    {
        return termConditions;
    }


    public void setTermConditions(List<TermConditionHolder> termConditions)
    {
        this.termConditions = termConditions;
    }


    public String getDefaultSelected()
    {
        return defaultSelected;
    }


    public void setDefaultSelected(String defaultSelected)
    {
        this.defaultSelected = defaultSelected;
    }


    public Map<Boolean, String> getStatus()
    {
        return status;
    }


    public Map<String, String> getDeploymentMode()
    {
        return deploymentMode;
    }


    public List<String> getChannels()
    {
        return channels;
    }

    public Map<String, String> getSource()
    {
        return source;
    }


    public void setSource(Map<String, String> source)
    {
        this.source = source;
    }


    public List<SupplierHolder> getAvailableSuppliers()
    {
        return availableSuppliers;
    }


    public void setAvailableSuppliers(List<SupplierHolder> availableSuppliers)
    {
        this.availableSuppliers = availableSuppliers;
    }


    public List<SupplierHolder> getSelectedSuppliers()
    {
        return selectedSuppliers;
    }


    public void setSelectedSuppliers(List<SupplierHolder> selectedSuppliers)
    {
        this.selectedSuppliers = selectedSuppliers;
    }


    public List<BigDecimal> getSelectedSupplierOids()
    {
        return selectedSupplierOids;
    }


    public void setSelectedSupplierOids(List<BigDecimal> selectedSupplierOids)
    {
        this.selectedSupplierOids = selectedSupplierOids;
    }
    
}
