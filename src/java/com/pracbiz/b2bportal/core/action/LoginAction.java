package com.pracbiz.b2bportal.core.action;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.SessionMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.AuditAccessErrorCode;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.ReserveMessageHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.HelpInfoExHolder;
import com.pracbiz.b2bportal.core.holder.extension.ModuleExHolder;
import com.pracbiz.b2bportal.core.interceptor.CsrfInterceptor;
import com.pracbiz.b2bportal.core.service.AuditAccessService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.LoginService;
import com.pracbiz.b2bportal.core.service.ReserveMessageService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.service.UserSessionService;
import com.pracbiz.b2bportal.core.service.UserTypeService;


public class LoginAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(LoginAction.class);
    private static final long serialVersionUID = -350030151197869926L;
    
    public static final String REQUEST_PARAMETER_KEY_LOGIN_ID = "loginId";
    public static final String REQUEST_PARAMETER_KEY_PASSWORD = "password";
    public static final String MSG_ERROR_LOGIN_FAILED = "ERROR_LOGIN_FAILED";
    public static final String B2BPC0706="B2BPC0706";
    
    public static final String ACTION_RESULT_LOGIN_WITH_OUT_MSG="loginWithOutMsg";
    public static final String ACTION_RESULT_PASSWORD_EXPIRED ="passwordExpired";
    public static final String SESSION_KEY_TIMES_OF_FAILED_ATTEMPT_LOGIN="FAILED_ATTEMPT_LOGIN";
    private static final String SESSION_KEY_LAYOUT_THEME = "layoutTheme";
    private static final String DEFAULT_LAYOUTTHEME = "claro";
    
    // --------- services -----------
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private ReserveMessageService reserveMessageService;
    @Autowired transient private LoginService loginService;
    @Autowired transient private UserProfileService userProfileService;
    @Autowired transient private UserProfileTmpService userProfileTmpService;
    @Autowired transient private RoleService roleService;
    @Autowired transient private GroupService groupService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private AuditAccessService auditAccessService;
    @Autowired transient private UserSessionService userSessionService;
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private SupplierService supplierService;
    
    private List<ReserveMessageHolder> messages;
    private UserProfileHolder user;

    
    public LoginAction()
    {
        initMsg();
    }
    
    
    public String init() // home.action
    {
        try
        {
            return ACTION_RESULT_LOGIN_WITH_OUT_MSG;
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
            
            return FORWARD_COMMON_MESSAGE;
        }
    }
    
    
    public String main()
    {
        try
        {
            
            if (this.getProfileOfCurrentUser().getBuyerOid() != null)
            {
                messages = reserveMessageService.selectValidMessageByAnnouncementType("BUYER");
            }
            else if (this.getProfileOfCurrentUser().getSupplierOid() != null)
            {
                messages = reserveMessageService.selectValidMessageByAnnouncementType("SUPPLIER");
            }
            else
            {
                messages = reserveMessageService.selectValidMessages();
            }
           
            return SUCCESS;
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                new String[] {tickNo}));

            return FORWARD_COMMON_MESSAGE;
        }
    }
    
    
    public String login()
    {
        return SUCCESS;
    }
    
    
    public void validateLoginWithMsg()
    {
    	try
    	{
    		String loginId  = (String) this.getRequest().getParameter(REQUEST_PARAMETER_KEY_LOGIN_ID);
            String password = (String) this.getRequest().getParameter(REQUEST_PARAMETER_KEY_PASSWORD);
            String clientIp = this.getRequest().getRemoteAddr();
            
            // check whether login id and password are entered
            if (StringUtils.isBlank(loginId) || StringUtils.isBlank(password))
            {
                this.init();
                setErrorMsg(this.getText("B2BPC0726"));
                
                return;
            }
            

            if(log.isInfoEnabled())
            {
                String currentTime = DateUtil.getInstance().convertDateToString(new Date(), "yyyy/MM/dd HH:mm:ss");
                log.info(this.getText("B2BPC0701", new String[]{loginId, clientIp, currentTime}));
            }
            
            
            user = userProfileService.getUserProfileByLoginId(loginId.trim());
            
            // check whether user exists
            if (null == user)
            {
                log.error(this.getText("B2BPC0705", new String[]{loginId}));
                this.init();
                setErrorMsg(this.getText("B2BPC0736"));
                
                return;
            }
            
            
            ControlParameterHolder maxAttmptLogin = controlParameterService.selectCacheControlParameterBySectIdAndParamId(
                    SECT_ID_CTRL, PARAM_ID_MAX_ATTEMPT_LOGIN);
                
            int maxAttempts = maxAttmptLogin.getNumValue();
            int failAttempts = user.getFailAttempts() == null ? 0 : user.getFailAttempts();
            
            // check password
            String encodePwd = null;
            
            try
            {
            	encodePwd = EncodeUtil.getInstance().computePwd(password, user.getUserOid());
            }
            catch (UnsupportedEncodingException e)
            {
            	throw new Exception(e);
            }
            
            if (!encodePwd.equals(user.getLoginPwd()))
            {
                try
                {
                	auditAccessService.createAuditAuccessForLoginFailed(user, clientIp, AuditAccessErrorCode.F0001);
                }
                catch (Exception e)
                {
                	ErrorHelper.getInstance().logError(log, e);
                }
                
                UserProfileTmpHolder oldObj = new UserProfileTmpHolder();
                UserProfileTmpHolder newObj = new UserProfileTmpHolder();
                BeanUtils.copyProperties(user, oldObj);
                BeanUtils.copyProperties(user, newObj);
                
                newObj.setFailAttempts(failAttempts + 1);
                
                if (maxAttempts == newObj.getFailAttempts())
                {
                	final HelpInfoExHolder helpInfo = (HelpInfoExHolder)this.getSession().get(SESSION_KEY_HELP_INFO);
                	
                    newObj.setBlocked(true);
                    newObj.setBlockRemarks(MULTIPLE_FAILED_LOGIN_ATTEMPTS);
                    newObj.setBlockDate(new Date());
                    newObj.setBlockBy("SYSTEM");
                    
                    //send email to user alert account blocked.
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                log.info("start to send email to alert user account has been blocked.");
                                sendBlockedAlertEmail(user, helpInfo);
                                log.info("start to send email to alert user account has been blocked successful.");
                            }
                            catch(Exception e)
                            {
                                ErrorHelper.getInstance().logError(log, e);
                            }
                        }
                    }).start();
                }
                
                this.userProfileService.updateByPrimaryKeySelective(oldObj, newObj);
                this.userProfileTmpService.updateByPrimaryKeySelective(oldObj, newObj);
                
                log.error(this.getText("B2BPC0730", new String[]{maxAttempts+""}));
                this.init();
                setErrorMsg(this.getText("B2BPC0736"));
                
                return;
            }
            
            
            // check duplicated login
            ControlParameterHolder repeatedLogin = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL,
                        PARAM_ID_REPEATED_LOGIN);
            
            if (!repeatedLogin.getValid() && null != userSessionService.selectByUserOid(user.getUserOid()))
            {
                log.error(this.getText("B2BPC0710", new String[]{loginId}));
                this.init();
                setErrorMsg(this.getText("B2BPC0710", new String[]{loginId}));
                
                return;
            }
            
            
            // check roles
            List<RoleHolder> roles = new ArrayList<RoleHolder>();
            
            GroupHolder group = groupService.selectGroupByUserOid(user.getUserOid());
            if (group == null)
            {
                roles = roleService.selectRolesByUserOid(user.getUserOid());
            }
            else
            {
                roles = roleService.selectRolesByGroupOid(group.getGroupOid());
            }
            
            if (null == roles || roles.isEmpty())
            {
                log.error(this.getText(group == null ? "B2BPC0734" : "B2BPC0735", new String[]{loginId}));
                this.init();
                setErrorMsg(this.getText(group == null ? "B2BPC0734" : "B2BPC0735", new String[]{loginId}));
                
                return;
            }
            
    		
            // check whether user is active
            if (!user.getActive())
            {
                try
                {
                	auditAccessService.createAuditAuccessForLoginFailed(user, clientIp, AuditAccessErrorCode.F0003);
                }
                catch (Exception e)
                {
                	ErrorHelper.getInstance().logError(log, e);
                }
                
                log.error(this.getText("B2BPC0708", new String[]{loginId}));
                this.init();
                setErrorMsg(this.getText("B2BPC0708", new String[]{loginId}));
                
                return;
            }
            
            
            // check whether user is blocked
            if (user.getBlocked())
            {
            	try
                {
            		auditAccessService.createAuditAuccessForLoginFailed(user, clientIp, AuditAccessErrorCode.F0002);
                }
                catch (Exception e)
                {
                	ErrorHelper.getInstance().logError(log, e);
                }
                
                log.error(getText("B2BPC0709", new String[]{loginId}));
                this.init();
                setErrorMsg(getText("B2BPC0709", new String[]{loginId}));
                
                return;
            }
            
            
            // check whether user company is inactive or blocked
            if (user.getBuyerOid() != null)
            {
                BuyerHolder buyer = buyerService.selectBuyerByKey(user.getBuyerOid());
                
                if (buyer == null)
                {                                           
                    log.error(getText("B2BPC0727", new String[]{loginId}));
                    this.init();
                    setErrorMsg(getText("B2BPC0727", new String[]{loginId}));
                    
                    return;
                }
                
                if (!buyer.getActive())
                {
                    try
                    {
                    	auditAccessService.createAuditAuccessForLoginFailed(user, clientIp, AuditAccessErrorCode.F0004);
                    }
                    catch (Exception e)
                    {
                    	ErrorHelper.getInstance().logError(log, e);
                    }
                    
                    log.error(getText("B2BPC0728", new String[]{loginId}));
                    this.init();
                    setErrorMsg(getText("B2BPC0728", new String[]{loginId}));
                    
                    return;
                }
                
                if (buyer.getBlocked())
                {
                    try
                    {
                    	auditAccessService.createAuditAuccessForLoginFailed(user, clientIp, AuditAccessErrorCode.F0005);
                    }
                    catch (Exception e)
                    {
                    	ErrorHelper.getInstance().logError(log, e);
                    }
                    
                    log.error(getText("B2BPC0722", new String[]{loginId}));
                    this.init();
                    setErrorMsg(getText("B2BPC0722", new String[]{loginId}));
                    
                    return;
                }
            }
            
            if (user.getSupplierOid() != null)
            {
                SupplierHolder supplier = supplierService.selectSupplierByKey(user
                    .getSupplierOid());
                
                if (supplier == null)
                {                                           
                    log.error(getText("B2BPC0727", new String[]{loginId}));
                    this.init();
                    setErrorMsg(getText("B2BPC0727", new String[]{loginId}));
                    
                    return;
                }
                
                if (!supplier.getActive())
                {
                    try
                    {
                    	auditAccessService.createAuditAuccessForLoginFailed(user, clientIp, AuditAccessErrorCode.F0004);
                    }
                    catch (Exception e)
                    {
                    	ErrorHelper.getInstance().logError(log, e);
                    }
                    
                    log.error(getText("B2BPC0728", new String[]{loginId}));
                    this.init();
                    setErrorMsg(getText("B2BPC0728", new String[]{loginId}));
                    
                    return;
                }
                
                if (supplier.getBlocked())
                {
                    try
                    {
                    	auditAccessService.createAuditAuccessForLoginFailed(user, clientIp, AuditAccessErrorCode.F0005);
                    }
                    catch (Exception e)
                    {
                    	ErrorHelper.getInstance().logError(log, e);
                    }
                    
                    log.error(getText("B2BPC0722", new String[]{loginId}));
                    this.init();
                    setErrorMsg(getText("B2BPC0722", new String[]{loginId}));
                    
                    return;
                }
            }
            
    	}
    	catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    public String loginWithMsg()
    {
        try
        {
            String rlt = this.loginProcess();
            
            return rlt;
        }
        catch (Exception e)
        {
            this.init();
            String msg = this.getText(B2BPC0706);
            setErrorMsg(msg);
            ErrorHelper.getInstance().logError(log, this, e);
            return INPUT;
        }
    }
    
    
    public void validateLoginWithOutMsg()
    {
        this.validateLoginWithMsg();
    }
    
    
    public String loginWithOutMsg()
    {
        try
        {
            String rlt = this.loginProcess();
            
            return rlt;
        }
        catch (Exception e)
        {
            this.init();
            String msg = this.getText(B2BPC0706);
            setErrorMsg(msg);
            ErrorHelper.getInstance().logError(log, this, e);
            return INPUT;
        }
    }
    
    
    public String logout()
    {
        try
        {
            CommonParameterHolder cp = (CommonParameterHolder)this.getSession().get(SESSION_KEY_COMMON_PARAM);
            UserProfileHolder user = (UserProfileHolder) this.getSession().get(SESSION_KEY_USER);
            String sessionId = this.getRequest().getSession().getId();
            loginService.doLogout(cp, user, sessionId);
            
            // remove userInfo from session
            if (this.getSession().get(SESSION_KEY_MENU) != null)
                this.getSession().remove(SESSION_KEY_MENU);
    
            if (this.getSession().get(SESSION_KEY_USER_AGENT) != null)
                this.getSession().remove(SESSION_KEY_USER_AGENT);
    
            if (this.getSession().get(SESSION_KEY_CLIENT_IP) != null)
                this.getSession().remove(SESSION_KEY_CLIENT_IP);
    
            if (this.getSession().get(SESSION_KEY_USER) != null)
                this.getSession().remove(SESSION_KEY_USER);
    
            if (this.getSession().get(SESSION_KEY_COMMON_PARAM) != null)
                this.getSession().remove(SESSION_KEY_COMMON_PARAM);
    
            if (this.getSession().get(SESSION_KEY_PERMIT_URL) != null)
                this.getSession().remove(SESSION_KEY_PERMIT_URL);
            
            Locale locale = (Locale)this.getSession().get(SESSION_KEY_STRUTS_LOCALE);
            
            this.getSession().clear();
            this.getRequest().getSession().invalidate();
            if (user != null)
            {
                log.info(this.getText("B2BPC0733", new String[] {
                        user.getLoginId(),
                        this.getRequest().getRemoteAddr(),
                        DateUtil.getInstance().convertDateToString(new Date(),
                                DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS) }));
            }
            
            this.getRequest().getSession().setAttribute(SESSION_KEY_STRUTS_LOCALE, locale);
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
        
        return SUCCESS;
    }
    
    
    //*******************************************************
    //  private method 
    //*******************************************************
    
    private void setErrorMsg(String errorMsg)
    {
        this.addActionError(errorMsg);
        this.getRequest().setAttribute(MSG_ERROR_LOGIN_FAILED, errorMsg);
    }
    
    
    private String loginProcess() throws Exception
    {
    	// following 2 lines of code renew the session id.
    	((SessionMap<String, Object>) this.getSession()).invalidate();
    	this.getSession().put("AUTHENTICATED", Boolean.TRUE);
    	
        String loginId = user.getLoginId();
        String loginModule = user.getLoginMode();
        
        if (TYPE_AUTH_ID_PASSWORD.equals(loginModule))
        {
            //verify whether login password expired
            ControlParameterHolder passwordExpired = controlParameterService.selectCacheControlParameterBySectIdAndParamId(
                    SECT_ID_CTRL, PARAM_ID_PWD_EXPIRE_DAYS);
            
            if (passwordExpired != null && user.getLastResetPwdDate() != null && 
        		!(DateUtil.getInstance().isAfterDays(user.getLastResetPwdDate(), new Date(), passwordExpired.getNumValue())))
            {
                log.info(this.getText("B2BPC0712",new String[]{loginId}));  
                this.getRequest().setAttribute(REQUEST_PARAMETER_KEY_LOGIN_ID, loginId);
                this.getSession().put(SESSION_KEY_LAYOUT_THEME, DEFAULT_LAYOUTTHEME);
                return ACTION_RESULT_PASSWORD_EXPIRED;
            }
            
        }
        
        
        ControlParameterHolder makerChecker = controlParameterService
    		.selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL, PARAM_ID_MAKER_CHECKER);
        ControlParameterHolder autoLogout = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL, PARAM_ID_AUTO_LOGOUT);
        ControlParameterHolder pageSizes = controlParameterService
    		.selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL, PARAM_PAGE_SIZES);
        ControlParameterHolder defaultPageSize = controlParameterService
    		.selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL, PARAM_DEFAULT_PAGE_SIZE);
        
        
        CommonParameterHolder cp = new CommonParameterHolder();
        cp.setLoginId(loginId);
        cp.setMkMode(user.getUserType().equals(BigDecimal.ONE) ? makerChecker.getValid() : false);
        cp.setCurrentUserOid(user.getUserOid());
        cp.setClientIp(this.getRequest().getRemoteAddr());
        cp.setTimeout(autoLogout.getNumValue());
        cp.setPageSizes(pageSizes.getStringValue());
        cp.setDefaultPageSize(defaultPageSize.getStringValue());
        cp.setOperateEquativeUserType(userTypeService.checkOperateEquativeUserType(user.getUserType()));
        
        //retrieve the menu list 
        List<ModuleExHolder> menus = loginService.selectMenusByUserOid(this, user.getUserOid()); 
        List<String> operations = loginService.selectgetPermitURLsByUserOid(user.getUserOid(), menus);
        
        //update login information
        UserProfileHolder newUser = new UserProfileHolder();
        BeanUtils.copyProperties(user, newUser);
        newUser.setFailAttempts(0);
        newUser.setLastLoginDate(new Date());
        
        //retrieve role information
        List<RoleHolder> roleList = null;
        GroupHolder group = groupService.selectGroupByUserOid(newUser.getUserOid());
        if (group == null)
        {
            roleList = roleService.selectRolesByUserOid(newUser.getUserOid());
        }
        else
        {
            roleList = roleService.selectRolesByGroupOid(group.getGroupOid());
        }
        
        loginService.doLogin(cp, newUser, user, this.getHttpSession().getId());
        this.getSession().put(SESSION_KEY_MENU, menus);
        this.getSession().put(SESSION_KEY_CLIENT_IP, this.getRequest().getRemoteAddr());
        this.getSession().put(SESSION_KEY_USER_AGENT,this.getRequest().getHeader("User-Agent"));
        this.getSession().put(SESSION_KEY_USER, newUser);
        this.getSession().put(SESSION_KEY_COMMON_PARAM, cp);
        this.getSession().put(SESSION_KEY_PERMIT_URL, operations);
        this.getSession().put(SESSION_KEY_ROLE_LIST, roleList);
        this.getSession().put(CsrfInterceptor.CSRF_TOKEN,
            EncodeUtil.getInstance().computeSha2Digest(EncodeUtil.getInstance().generateSecureBytes(16)));
        
        if (log.isInfoEnabled())
        {
            String clienIp = this.getRequest().getRemoteAddr();
            String currentTime = DateUtil.getInstance().convertDateToString(new Date(), "yyyy/MM/dd HH:mm:ss");
            String[] msg = {loginId, clienIp, currentTime};
            log.info(this.getText("B2BPC0702", msg));
        }
        
        return SUCCESS;
    }
    
    
    private void sendBlockedAlertEmail(UserProfileHolder user, HelpInfoExHolder helpInfo) throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("loginId", user.getLoginId());
        param.put("name", user.getUserName());
        param.put("helpdeskNumber", helpInfo.getHelpNo());
        param.put("helpdeskEmail", helpInfo.getHelpEmail());
        param.put("userType", user.getUserType());
        param.put("serverUrl", appConfig.getServerUrl());

		emailEngine.sendHtmlEmail(new String[] { user.getEmail() },
						"Portal : Login account blocked due to repeated login attempt failure",
						"mail_user_blocked_alert.vm", param);
	}
    
    
    //*******************************************************
    //  setter and getter   
    //*******************************************************

    public UserProfileHolder getUser()
    {
        return user;
    }


    public void setUser(UserProfileHolder user)
    {
        this.user = user;
    }


    public List<ReserveMessageHolder> getMessages()
    {
        return messages;
    }


    public void setMessages(List<ReserveMessageHolder> messages)
    {
        this.messages = messages;
    }

}
