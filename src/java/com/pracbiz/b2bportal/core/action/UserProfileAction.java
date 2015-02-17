package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.LastUpdateFrom;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyHolder;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.holder.UserClassTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.ClassExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SubclassExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyService;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyTmpService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserTmpService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserTmpService;
import com.pracbiz.b2bportal.core.service.ClassService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.EmailSendService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupTmpService;
import com.pracbiz.b2bportal.core.service.GroupUserTmpService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.ResetPasswordRequestHistoryService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.RoleUserTmpService;
import com.pracbiz.b2bportal.core.service.SubclassService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserClassService;
import com.pracbiz.b2bportal.core.service.UserClassTmpService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.service.UserSessionService;
import com.pracbiz.b2bportal.core.service.UserSubclassService;
import com.pracbiz.b2bportal.core.service.UserSubclassTmpService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PasswordValidateHelper;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */


public class UserProfileAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(UserProfileAction.class);
    private static final long serialVersionUID = -7610273092860010461L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_USER_PROFILE = "SEARCH_PARAMETER_USER_PROFILE";
    public static final String SESSION_KEY_USER_OID_FOR_SET_PWD = "USER_OID_FOR_SET_PWD";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.UserProfileAction.selectedOids";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final String REQUEST_OPERATE_FLAG = "operateFlag";
    private static final String REQUEST_OID_DELIMITER = "\\-";
    private static final String ONE_RECORD_TO_OPERATE_CODE="B2BPU0160";
    private static final String ALL_STORE_FLAG = "all_store_flag";
    private static final String ALL_WAREHOUSE_FLAG = "all_warehouse_flag";
    private static final String ALL_AREA_FLAG = "all_area_flag";
    private static final String ALL_CLASS_FLAG = "all_class_flag";
    private static final String ALL_SUBCLASS_FLAG = "all_subclass_flag";
    
    private static final Map<String, String> sortMap;
    
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("userName", "USER_NAME");
        sortMap.put("loginId", "LOGIN_ID");
        sortMap.put("userTypeDesc", "USER_TYPE_DESC");
        sortMap.put("actionType", "ACTION_TYPE");
        sortMap.put("actor", "ACTOR");
        sortMap.put("ctrlStatus", "CTRL_STATUS");
        sortMap.put("updateDate", "UPDATE_DATE");
        sortMap.put("active", "ACTIVE");
        sortMap.put("blocked", "BLOCKED");
        sortMap.put("company", "COMPANY");
    }

    @Autowired private transient UserProfileService userProfileService;
    @Autowired private transient UserProfileTmpService userProfileTmpService;
    @Autowired private transient UserTypeService userTypeService;
    @Autowired private transient BuyerService buyerService;
    @Autowired private transient SupplierService supplierService;
    @Autowired private transient RoleService roleService;
    @Autowired private transient RoleTmpService roleTmpService;
    @Autowired private transient GroupService groupService;
    @Autowired private transient GroupTmpService groupTmpService;
    @Autowired private transient GroupUserTmpService groupUserTmpService;
    @Autowired private transient RoleUserTmpService roleUserTmpService;
    @Autowired private transient ResetPasswordRequestHistoryService resetPasswordRequestHistoryService;
    @Autowired private transient ControlParameterService controlParameterService;
    @Autowired private transient PasswordValidateHelper passwordValidateHelper;
    @Autowired private transient UserSessionService userSessionService;
    @Autowired private transient EmailSendService emailSendService;
    @Autowired private transient BuyerStoreService buyerStoreService;
    @Autowired private transient BuyerStoreAreaService buyerStoreAreaService;
    @Autowired private transient BuyerStoreAreaUserTmpService buyerStoreAreaUserTmpService;
    @Autowired private transient BuyerStoreUserTmpService buyerStoreUserTmpService;
    @Autowired private transient BuyerStoreUserService buyerStoreUserService;
    @Autowired private transient BuyerStoreAreaUserService buyerStoreAreaUserService;
    @Autowired private transient AllowedAccessCompanyService allowedAccessCompanyService;
    @Autowired private transient AllowedAccessCompanyTmpService allowedAccessCompanyTmpService;
    @Autowired private transient ClassService classService;
    @Autowired private transient SubclassService subclassService;
    @Autowired private transient UserClassService userClassService;
    @Autowired private transient UserSubclassService userSubclassService;
    @Autowired private transient UserClassTmpService userClassTmpService;
    @Autowired private transient UserSubclassTmpService userSubclassTmpService;
    @Autowired private transient OidService oidService;

    private UserProfileTmpExHolder userProfile;
    private UserProfileExHolder oldUserProfile;
    private List<UserTypeHolder> userTypeList;
    private Map<String, String> ctrlStatusMap;
    private List<String> loginModelList;
    private List<? extends Object> availabelRolesList;
    private List<? extends Object> selectedRolesList;
    private List<? extends Object> oldSelectedRolesList;
    private List<? extends Object> availabelStoresList;
    private List<? extends Object> selectedStoresList;
    private List<? extends Object> oldSelectedStoresList;
    private List<? extends Object> availabelAreasList;
    private List<? extends Object> selectedAreasList;
    private List<? extends Object> oldSelectedAreasList;
    private List<? extends Object> availabelWareHouseList;
    private List<? extends Object> selectedWareHouseList; 
    private List<? extends Object> oldSelectedWareHouseList;
    private List<? extends Object> availabelBuyersList;
    private List<? extends Object> selectedBuyersList;
    private List<? extends Object> oldSelectedBuyersList;
    private List<? extends Object> availableClassList;
    private List<? extends Object> selectedClassList;
    private List<? extends Object> oldSelectedClassList;
    private List<? extends Object> availableSubclassList;
    private List<? extends Object> selectedSubclassList;
    private List<? extends Object> oldSelectedSubclassList;
    private List<BuyerHolder> buyerList;
    private List<SupplierHolder> supplierList;
    private List<GroupHolder> groupList;
    private boolean isAddToGroup;
    private String mkErrorInfo;
    private List<UserProfileTmpExHolder> mkCreateUserProfileList;
    private List<UserProfileTmpExHolder> mkDeleteUserProfileList;
    private List<UserProfileTmpExHolder> mkUpdateUserProfileList;
    private String confirmType;
    
    private String newPwd;
    private String loginId;
    
    public UserProfileAction()
    {
        this.initMsg();
    }

    
    public String putParamIntoSession()
    {
        try
        {
            String operate = this.getRequest().getParameter(REQUEST_OPERATE_FLAG);
            Object selectedOids = this.getRequest().getParameter(REQUEST_PARAMTER_OID);
            if ("DELETE".equals(operate) || this.validateMkOperate(selectedOids))
            {
                this.getSession().put(SESSION_OID_PARAMETER, selectedOids);
            }
            else
            {
                return ERROR;
            }
        }
        catch (Exception e)
        {
            mkErrorInfo = this.getText("message.exception.general.title");
            return ERROR;
        }

        return SUCCESS;
    }
    

    // ***************************************
    // Search for summary
    // ***************************************

    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_USER_PROFILE);

        if (userProfile == null)
        {
            userProfile = (UserProfileTmpExHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_USER_PROFILE);
        }
        
        try
        {
            this.initSearchCondition();
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));

            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);

            msg.addMessageTarget(mt);

            return FORWARD_COMMON_MESSAGE;
        }
        this.getSession().put(SESSION_KEY_SEARCH_PARAMETER_USER_PROFILE, userProfile);
        return SUCCESS;

    }


    public void validateSearch()
    {
        boolean flag = this.hasErrors();
        try
        {
            if (null == userProfile || flag)
            {
                return;
            }
            
            if (userProfile.getParamBlocked() != null && userProfile.getParamBlocked().trim().equals("-1"))
            {
                userProfile.setParamBlocked(null);
            }
            if (userProfile.getParamActive() != null && userProfile.getParamActive().trim().equals("-1"))
            {
                userProfile.setParamActive(null);
            }
            
        }
        catch (Exception e)
        {
            log.error("validateSearch:");
            log.error("Error occur on validateSearch() Report ", e);
            ErrorHelper.getInstance().logError(log, this, e);
        }
    }


    public String search()
    {
        if (null == userProfile)
        {
            userProfile = new UserProfileTmpExHolder();
        }
        try
        {
            userProfile.trimAllString();
            userProfile.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        getSession()
                .put(SESSION_KEY_SEARCH_PARAMETER_USER_PROFILE, userProfile);

        return SUCCESS;
    }


    public String data()
    {
        try
        {
            UserProfileTmpExHolder searchParam = (UserProfileTmpExHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_USER_PROFILE);

            if (null == searchParam)
            {
                searchParam = new UserProfileTmpExHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_USER_PROFILE, searchParam);
            }
            searchParam.setCurrentUserType(this.getUserTypeOfCurrentUser());
            searchParam.setCurrentBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            searchParam.setCurrentSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
            searchParam.setInit(this.isCurrentUserInit());
            
            this.obtainListRecordsOfPagination(userProfileTmpService,
                searchParam, sortMap, "userOid", null);
            

            for (BaseHolder baseItem : this.getGridRlt().getItems())
            {
                UserProfileTmpExHolder item = (UserProfileTmpExHolder) baseItem;
                if (item.getActionType() != null)
                {
                    item.setActionTypeValue(this.getText(item.getActionType().getKey()));
                }
                if (item.getCtrlStatus() != null)
                {
                    item.setCtrlStatusValue(this.getText(item.getCtrlStatus().getKey()));
                }
            }

        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }


    // ***************************************
    // Create function
    // ***************************************
    
    public String initAdd()
    {
        try
        {
            userTypeList = userTypeService
                    .selectPrivilegedSubUserTypesByUserType(this.getUserTypeOfCurrentUser());
            buyerList = buyerService.select(new BuyerHolder());
            if (isBuyer(this.getUserTypeOfCurrentUser()))
            {
                supplierList = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else
            {
                supplierList = supplierService.select(new SupplierExHolder());
            }
            
            if (userProfile == null)
            {
                userProfile = new UserProfileTmpExHolder();
            }
            userProfile.setCompanyName(this.initCompanyInfo(this.getProfileOfCurrentUser()));
            userProfile.setActive(true);
            
            if (buyerList == null)
            {
                buyerList = new ArrayList<BuyerHolder>();
            }
            if (supplierList == null)
            {
                supplierList = new ArrayList<SupplierHolder>();
            }
            if (userTypeList == null)
            {
                userTypeList = new ArrayList<UserTypeHolder>();
            }
            else
            {
                UserTypeHolder ut = (UserTypeHolder)userTypeList.get(0);
                
                this.initRolesByConditionFromUserType(ut);
                this.initStoreAreaByConditionFromUserType(ut);
                this.initClassAndSubclassFromUserType(ut);
                this.initAvaliableBuyerFromUserType(ut);
            }
            
            initSelectAllSessionInfo();
            loginModelList = appConfig.getLoginModel();
            
            if (availabelRolesList == null) availabelRolesList = new ArrayList<Object>();
            selectedRolesList = new ArrayList<Object>();
            if (availabelStoresList == null) availabelStoresList = new ArrayList<Object>();
            selectedStoresList = new ArrayList<Object>();
            if (availabelAreasList == null) availabelAreasList = new ArrayList<Object>();
            selectedAreasList = new ArrayList<Object>();
            if (groupList == null) groupList = new ArrayList<GroupHolder>();
            if (availabelWareHouseList == null) availabelWareHouseList = new ArrayList<Object>();
            selectedWareHouseList = new ArrayList<Object>();
            if (availabelBuyersList == null) availabelBuyersList = new ArrayList<Object>();
            selectedBuyersList = new ArrayList<Object>();
            if (availableClassList == null) availableClassList = new ArrayList<Object>();
            selectedClassList = new ArrayList<Object>();
            if (availableSubclassList == null) availableSubclassList = new ArrayList<Object>();
            selectedSubclassList = new ArrayList<Object>();
            
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }

    
    public void validateSaveAdd()
    {   
        try
        {
            boolean flag = this.hasErrors();
            
            if (!flag && userProfile == null)
            {
                this.addActionError(this.getText("B2BPU0126"));
                flag = true;
            }
            if (!flag && (userProfile.getLoginId() == null || "".equals(userProfile.getLoginId())))
            {
                this.addActionError(this.getText("B2BPU0136"));
                flag = true;
            }
            if (!flag)
            {
                String loginId = userProfile.getLoginId();
                if (isSupplier(userProfile.getUserType()))
                {
                    BigDecimal supplierOid = userProfile.getSupplierOid();
                    if (isSupplier(getUserTypeOfCurrentUser()))
                    {
                        supplierOid = getProfileOfCurrentUser().getSupplierOid();
                    }
                    SupplierHolder supplier = supplierService.selectSupplierByKey(supplierOid);
                    loginId = initLoginId(supplier, userProfile.getLoginId());
                }
                
                if (loginId.length() > 50)
                {
                    this.addActionError(this.getText("B2BPU0174", new String[]{loginId}));
                    flag = true;
                }
                
                if (!flag && userProfileTmpService.isLoginIdExist(loginId))
                {
                    this.addActionError(this.getText("B2BPU0120", new String[]{loginId}));
                    flag = true;
                }
            }
            if (!flag && (userProfile.getUserType().equals(BigDecimal.valueOf(2)) 
                    || userProfile.getUserType().equals(BigDecimal.valueOf(4)) 
                    || userProfile.getUserType().equals(BigDecimal.valueOf(6)) 
                    || userProfile.getUserType().equals(BigDecimal.valueOf(7)))
                    && userProfile.getBuyerOid() == null
                    && this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(1)))
            {
                this.addActionError(this.getText("B2BPU0134"));
                flag = true;
            }
            if (!flag && (userProfile.getUserType().equals(BigDecimal.valueOf(3)) 
                    || userProfile.getUserType().equals(BigDecimal.valueOf(5)))
                    && userProfile.getSupplierOid() == null
                    && !this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
            {
                this.addActionError(this.getText("B2BPU0135"));
                flag = true;
            }
            if (!flag && (selectedRolesList == null) || (selectedRolesList.isEmpty()))
            {
                this.addActionError(this.getText("B2BPU0172"));
                flag = true;
            }
            if (!flag && isBuyer(userProfile.getUserType()) && !isAddToGroup)
            {
                this.addActionError(this.getText("B2BPU0173"));
                flag = true;
            }
            if (!flag && isAddToGroup && userProfile.getGroupOid() == null)
            {
                this.addActionError(this.getText("B2BPU0164"));
                flag = true;
            }
            this.parseCompanyInfoForUser();
            //supplier admin only create max supplier user
            if (userProfile.getUserType().equals(BigDecimal.valueOf(3)) || userProfile.getUserType().equals(BigDecimal.valueOf(5)))
            {
                String maxSupplierUser = "";
                if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)) 
                        || this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(5)))
                {
                    maxSupplierUser = checkMaxSupplierUser(this.getProfileOfCurrentUser().getSupplierOid());
                }
                else
                {
                    maxSupplierUser = checkMaxSupplierUser(userProfile.getSupplierOid());
                }
                if(!maxSupplierUser.trim().isEmpty())
                {
                    this.addActionError(this.getText("B2BPU0171", new String[]{maxSupplierUser}));
                    flag = true;
                }
            }
            
            if (flag)
            {
                userTypeList = userTypeService
                        .selectPrivilegedSubUserTypesByUserType(this.getUserTypeOfCurrentUser());
                
                buyerList = buyerService.select(new BuyerHolder());
                if (isBuyer(this.getUserTypeOfCurrentUser()))
                {
                    supplierList = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
                else
                {
                    supplierList = supplierService.select(new SupplierExHolder());
                }
                userProfile = userProfile == null ? new UserProfileTmpExHolder() : userProfile;
                userProfile.setCompanyName(this.initCompanyInfo(this.getProfileOfCurrentUser()));
                
                if (buyerList == null) buyerList = new ArrayList<BuyerHolder>();
                if (supplierList == null) supplierList = new ArrayList<SupplierHolder>();
                
                if (userTypeList == null)
                {
                    userTypeList = new ArrayList<UserTypeHolder>();
                }
                
                if (userProfile != null && userProfile.getUserType() != null)
                {
                    userProfile.setIsLocked(false);
                    this.initRolesByConditionFromUserProfile(false);
                    this.initAvailableStoreAndAreaFromUserProfile();
                    this.initAvailableClassAndSubclassFromUserProfile(false);
                    this.initAvaliableBuyerFromUserProfile();
                }
                
                if (availabelRolesList == null) availabelRolesList = new ArrayList<Object>();
                
                if (selectedRolesList == null) selectedRolesList = new ArrayList<Object>();
                
                if (availabelStoresList == null) availabelStoresList = new ArrayList<Object>();
                
                if (selectedStoresList == null) selectedStoresList = new ArrayList<Object>();
                
                if(availabelWareHouseList == null) availabelWareHouseList = new ArrayList<Object>();
                
                if(selectedWareHouseList == null) selectedWareHouseList = new ArrayList<Object>();
                
                if(availableClassList == null) availableClassList = new ArrayList<Object>();
                
                if(selectedClassList == null) selectedClassList = new ArrayList<Object>();
                
                if(availableSubclassList == null) availableSubclassList = new ArrayList<Object>();
                
                if(selectedSubclassList == null) selectedSubclassList = new ArrayList<Object>();
                
                if(availabelBuyersList == null) availabelBuyersList = new ArrayList<Object>();
                
                if(selectedBuyersList == null) selectedBuyersList = new ArrayList<Object>();
                
                initListForValidateFailed();
                
                loginModelList = appConfig.getLoginModel();
                
                if (groupList == null) groupList = new ArrayList<GroupHolder>();
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public void initListForValidateFailed()
    {
        if (!selectedRolesList.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            for (Object obj : availabelRolesList)
            {
                RoleHolder role = (RoleHolder) obj;
                if (selectedRolesList.contains(role.getRoleOid().toString()))
                {
                    tmpList.add(role);
                }
            }
            
            availabelRolesList.removeAll(tmpList);
            selectedRolesList = tmpList;
        }
        if (!selectedBuyersList.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            for (Object obj : availabelBuyersList)
            {
                BuyerHolder buyer = (BuyerHolder) obj;
                if (selectedBuyersList.contains(buyer.getBuyerOid().toString()))
                {
                    tmpList.add(buyer);
                }
            }
            
            availabelBuyersList.removeAll(tmpList);
            selectedBuyersList = tmpList;
        }
        if (!selectedStoresList.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            if ("-3".equals(selectedStoresList.get(0)))
            {
                BuyerStoreHolder store = new BuyerStoreHolder();
                store.setStoreName(this.getText("user.all.store.selected"));
                store.setStoreOid(BigDecimal.valueOf(-3));
                tmpList.add(store);
                availabelStoresList.clear();
            }
            else
            {
                for (Object obj : availabelStoresList)
                {
                    BuyerStoreHolder store = (BuyerStoreHolder) obj;
                    if (selectedStoresList.contains(store.getStoreOid().toString()))
                    {
                        tmpList.add(store);
                    }
                }
                
                availabelStoresList.removeAll(tmpList);
            }
            selectedStoresList = tmpList;
        }
        if (!selectedWareHouseList.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            if ("-4".equals(selectedWareHouseList.get(0)))
            {
                BuyerStoreHolder warehouse = new BuyerStoreHolder();
                warehouse.setStoreName(this.getText("user.all.warehouse.selected"));
                warehouse.setStoreOid(BigDecimal.valueOf(-4));
                tmpList.add(warehouse);
                availabelWareHouseList.clear();
            }
            else
            {
                for (Object obj : availabelWareHouseList)
                {
                    BuyerStoreHolder wareHouse = (BuyerStoreHolder) obj;
                    if (selectedWareHouseList.contains(wareHouse.getStoreOid().toString()))
                    {
                        tmpList.add(wareHouse);
                    }
                }
                
                availabelWareHouseList.removeAll(tmpList);
            }
            selectedWareHouseList = tmpList;
        }
        if (!selectedAreasList.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            if ("-2".equals(selectedAreasList.get(0)))
            {
                BuyerStoreAreaHolder area = new BuyerStoreAreaHolder();
                area.setAreaName(getText("user.all.area.selected"));
                area.setAreaOid(BigDecimal.valueOf(-2));
                tmpList.add(area);
                availabelAreasList.clear();
            }
            else
            {
                for (Object obj : availabelAreasList)
                {
                    BuyerStoreAreaHolder area = (BuyerStoreAreaHolder) obj;
                    if (selectedAreasList.contains(area.getAreaOid()
                            .toString()))
                    {
                        tmpList.add(area);
                    }
                }
                
                availabelAreasList.removeAll(tmpList);
            }
            selectedAreasList = tmpList;
        }
        if (!selectedClassList.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            if ("-1".equals(selectedClassList.get(0)))
            {
                ClassHolder class_ = new ClassHolder();
                class_.setClassCode(getText("user.all.class.selected"));
                class_.setClassOid(BigDecimal.valueOf(-1));
                tmpList.add(class_);
                availableClassList.clear();
            }
            else
            {
                for (Object obj : availableClassList)
                {
                    ClassHolder class_ = (ClassHolder) obj;
                    if (selectedClassList.contains(class_.getClassOid()
                            .toString()))
                    {
                        tmpList.add(class_);
                    }
                }
                
                availableClassList.removeAll(tmpList);
            }
            selectedClassList = tmpList;
        }
        if (!selectedSubclassList.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            if ("-1".equals(selectedSubclassList.get(0)))
            {
                SubclassHolder subclass_ = new SubclassHolder();
                subclass_.setSubclassCode(getText("user.all.subclass.selected"));
                subclass_.setSubclassOid(BigDecimal.valueOf(-1));
                tmpList.add(subclass_);
                availableSubclassList.clear();
            }
            else
            {
                for (Object obj : availableSubclassList)
                {
                    SubclassHolder Subclass_ = (SubclassHolder) obj;
                    if (selectedSubclassList.contains(Subclass_.getSubclassOid()
                            .toString()))
                    {
                        tmpList.add(Subclass_);
                    }
                }
                
                availableSubclassList.removeAll(tmpList);
            }
            selectedSubclassList = tmpList;
        }
    }
    
    
    public String saveAdd()
    {   
        try
        {
            userProfile.trimAllString();
            userProfile.setAllEmptyStringToNull();
            
            userProfile.setUserOid(oidService.getOid());
            userProfile.setCreateDate(new Date());
            userProfile.setCreateBy(this.getLoginIdOfCurrentUser());
            userProfile.setInit(false);
            userProfile.setFailAttempts(0);
            
            if (isSupplier(userProfile.getUserType()))
            {
                SupplierHolder supplier = supplierService.selectSupplierByKey(userProfile.getSupplierOid());
                userProfile.setLoginId(initLoginId(supplier, userProfile.getLoginId()));
            }
            
            
            List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
            for (Object o : selectedRolesList)
            {
                RoleUserTmpHolder ru = new RoleUserTmpHolder();
                ru.setRoleOid(new BigDecimal(o.toString()));
                ru.setUserOid(userProfile.getUserOid());
                
                ruList.add(ru);
            }
            userProfile.setRoleUsers(ruList);
            if (isBuyer(userProfile.getUserType()) || isStore(userProfile.getUserType()))
            {
                if (selectedStoresList != null && !selectedStoresList.isEmpty())
                {
                    List<BuyerStoreUserHolder> bsuList = new ArrayList<BuyerStoreUserHolder>();
                    for (Object o : selectedStoresList)
                    {
                        BuyerStoreUserTmpHolder bsu = new BuyerStoreUserTmpHolder();
                        bsu.setUserOid(userProfile.getUserOid());
                        bsu.setStoreOid(new BigDecimal(o.toString()));
                        
                        bsuList.add(bsu);
                    }
                    userProfile.setBuyerStoreUsers(bsuList);
                }
                
                if (selectedWareHouseList != null && !selectedWareHouseList.isEmpty())
                {
                    List<BuyerStoreUserHolder> buyerWareHouseList = new ArrayList<BuyerStoreUserHolder>();
                    for (Object o : selectedWareHouseList)
                    {
                        BuyerStoreUserTmpHolder bsu = new BuyerStoreUserTmpHolder();
                        bsu.setUserOid(userProfile.getUserOid());
                        bsu.setStoreOid(new BigDecimal(o.toString()));
                        
                        buyerWareHouseList.add(bsu);
                    }
                    userProfile.setBuyerWareHouseUsers(buyerWareHouseList);
                }
                
                if (selectedAreasList != null && !selectedAreasList.isEmpty())
                {
                    List<BuyerStoreAreaUserHolder> bsauList = new ArrayList<BuyerStoreAreaUserHolder>();
                    for (Object o : selectedAreasList)
                    {
                        BuyerStoreAreaUserTmpHolder bsau = new BuyerStoreAreaUserTmpHolder();
                        bsau.setUserOid(userProfile.getUserOid());
                        bsau.setAreaOid(new BigDecimal(o.toString()));
                        
                        bsauList.add(bsau);
                    }
                    userProfile.setBuyerStoreAreaUsers(bsauList);
                }
            }
            if (isBuyer(userProfile.getUserType()))
            {
                if (selectedClassList != null && !selectedClassList.isEmpty())
                {
                    List<UserClassHolder> ucList = new ArrayList<UserClassHolder>();
                    for (Object o : selectedClassList)
                    {
                        UserClassTmpHolder uc = new UserClassTmpHolder();
                        uc.setUserOid(userProfile.getUserOid());
                        uc.setClassOid(new BigDecimal(o.toString()));
                        
                        ucList.add(uc);
                    }
                    userProfile.setUserClassList(ucList);
                }
                if (selectedSubclassList != null && !selectedSubclassList.isEmpty())
                {
                    List<UserSubclassHolder> usList = new ArrayList<UserSubclassHolder>();
                    for (Object o : selectedSubclassList)
                    {
                        UserSubclassTmpHolder us = new UserSubclassTmpHolder();
                        us.setUserOid(userProfile.getUserOid());
                        us.setSubclassOid(new BigDecimal(o.toString()));
                        
                        usList.add(us);
                    }
                    userProfile.setUserSubclassList(usList);
                }
            }
            
            
            List<AllowedAccessCompanyHolder> allowedBuyerList = new ArrayList<AllowedAccessCompanyHolder>();
            for (Object o : selectedBuyersList)
            {
                AllowedAccessCompanyTmpHolder allowedAccessCompany = new AllowedAccessCompanyTmpHolder();
                allowedAccessCompany.setUserOid(userProfile.getUserOid());
                allowedAccessCompany.setCompanyOid(new BigDecimal(o.toString()));
                
                allowedBuyerList.add(allowedAccessCompany);
            }
            userProfile.setAllowedBuyerList(allowedBuyerList);
            
            if (!isAddToGroup)
            {
                userProfile.setGroupOid(null);
            }
            
            List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
            if(userProfile.getGroupOid() != null)
            {
                
                GroupUserTmpHolder gu = new GroupUserTmpHolder();
                gu.setGroupOid(userProfile.getGroupOid());
                gu.setUserOid(userProfile.getUserOid());
                
                guList.add(gu);
            }
            userProfile.setGroupUsers(guList);
            

            userProfileService.createUserProfile(this.getCommonParameter(),
                    appConfig.getServerUrl() + "/user/", this
                            .getRequest().getRemoteAddr(), userProfile, true);
            
            log.info(this.getText(
                    "B2BPU0106",
                    new String[] { userProfile.getLoginId(),
                            this.getLoginIdOfCurrentUser() }));

        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    public static String initLoginId(SupplierHolder supplier, String loginId)
    {
        String rlt = loginId;
        return rlt + "@" + supplier.getSupplierCode();
    }
    
    
    public static String separateLoginId(SupplierHolder supplier, String loginId)
    {
        String supplierCode = loginId.substring(loginId.lastIndexOf("@") + 1);
        if (supplier != null && supplierCode.equalsIgnoreCase(supplier.getSupplierCode()))
        {
            return loginId.substring(0, loginId.lastIndexOf("@"));
        }
        return loginId;
    }
    
    
    public void validateSetPassword()
    {
        boolean flag = this.hasErrors();
        String msg = null;
        try
        {
            if (!flag && this.getRequest().getParameter("h") == null)
            {
                 msg = this.getText("B2BPU0128");
                 flag = true;
            }
            if (!flag)
            {
                ResetPasswordRequestHistoryHolder previousResetPwdRecord = new ResetPasswordRequestHistoryHolder();
                String hashValue = this.getRequest().getParameter("h");
                previousResetPwdRecord.setHashCode(hashValue);
                previousResetPwdRecord.setValid(Boolean.TRUE);
                List<? extends Object> previousResetPwdRecords = resetPasswordRequestHistoryService
                        .select(previousResetPwdRecord);
                if (previousResetPwdRecords == null
                        || previousResetPwdRecords.isEmpty())
                {
                    msg = this.getText("B2BPU0129");
                    flag = true;
                }
                else
                {
                    previousResetPwdRecord = (ResetPasswordRequestHistoryHolder) previousResetPwdRecords
                            .get(0);

                    int expireDays = controlParameterService
                            .selectCacheControlParameterBySectIdAndParamId(
                                    CoreCommonConstants.SECT_ID_CTRL,
                                    CoreCommonConstants.PARAM_ID_MAIL_EXPIRE_DAYS)
                            .getNumValue();

                    Date expireDate = DateUtil.getInstance()
                            .dateAfterDays(
                                    previousResetPwdRecord.getRequestTime(),
                                    expireDays);

                    if (!DateUtil.getInstance().isAfterDays(
                            previousResetPwdRecord.getRequestTime(),
                            new Date(), expireDays))
                    {
                        previousResetPwdRecord.setValid(Boolean.FALSE);
                        resetPasswordRequestHistoryService.auditUpdateByPrimaryKeySelective
                            (this.getMyCommonParameter(null), null, previousResetPwdRecord);
                        
                        flag = true;
                        msg = this
                                .getText(
                                        "B2BPU0130",
                                        new String[] {
                                                hashValue,
                                                DateUtil.getInstance()
                                                        .convertDateToString(
                                                                expireDate,
                                                                DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS) });
                    }
                    
                    UserProfileHolder user = userProfileService
                            .getUserProfileByLoginId(previousResetPwdRecord
                                    .getLoginId());
                    
                    if (user == null)
                    {
                        msg = this.getText("B2BPU0143");
                        flag = true;
                    }
                    else
                    {
                        this.getSession().put(SESSION_KEY_USER_OID_FOR_SET_PWD, user.getUserOid());
                    }

                }
            }
            if (flag)
            {
                this.addActionError(msg);
                this.getRequest().setAttribute("ERROR_SET_PASSWORD_FAILED", msg);
                this.getRequest().setAttribute("CLOSE_WINDOW", true);
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String setPassword()
    {
        
        return SUCCESS;
    }
    
    
    public void validateSavePassword()
    {
        boolean flag = this.hasErrors();
        String msg = null;
        try
        {
            String newPwd = (String) this.getRequest().getParameter("newPwd");
            String cfmNewPwd = (String) this.getRequest().getParameter("cfmNewPwd");
            
            if (!flag && "".equals(newPwd))
            {
                msg = this.getText("B2BPU0144");
                flag = true;
            }
            if (!flag && "".equals(cfmNewPwd))
            {
                msg = this.getText("B2BPU0145");
                flag = true;
            }
            if (!flag && !newPwd.equals(cfmNewPwd))
            {
                msg = this.getText("B2BPU0146");
                flag = true;
            }
            if (!flag)
            {
                BigDecimal userOid = (BigDecimal) this.getSession().get(
                        SESSION_KEY_USER_OID_FOR_SET_PWD);
                UserProfileHolder user = userProfileService
                        .selectUserProfileByKey(userOid);
                if (user == null)
                {
                    msg = this.getText("B2BPU0141");
                    flag = true;
                }
                else
                {
                    BeanUtils.copyProperties(user, userProfile = new UserProfileTmpExHolder());
                }
            }
            if (!flag)
            {
                flag = this.passwordValidateHelper.validatePwd(this, userProfile, newPwd);
                msg = this.passwordValidateHelper.getErrorMsg();
            }
            if (flag)
            {
                this.addActionError(msg);
                this.getRequest().setAttribute("ERROR_SET_PASSWORD_FAILED", msg);
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }

    
    public String savePassword()
    {
        try
        {
            String newPwd = (String) this.getRequest().getParameter("newPwd");
            this.setNewPwd(newPwd);
            this.setLoginId(userProfile.getLoginId());
            
            String password = EncodeUtil.getInstance().computePwd(newPwd, userProfile.getUserOid());
            UserProfileTmpHolder newUser = userProfile;

            newUser.setLoginPwd(password);
            newUser.setLastResetPwdDate(new Date());
            
            PasswordHistoryHolder passwordHistory = new PasswordHistoryHolder();
            passwordHistory.setChangeDate(new Date());
            passwordHistory.setOldLoginPwd(newUser.getLoginPwd());
            passwordHistory.setUserOid(newUser.getUserOid());
            passwordHistory.setChangeReason(REASON_CODE_PWDCHG_NORMAL_CHANGE);
            passwordHistory.setActor(newUser.getLoginId());
            
            resetPasswordRequestHistoryService.saveNewPassword(passwordHistory, newUser, userProfile, this.getMyCommonParameter(newUser));
            
            this.getSession().remove(SESSION_KEY_USER_OID_FOR_SET_PWD);
            this.getRequest().setAttribute("SET_PASSWORD_SUCCESSFULLY", this.getText("B2BPU0166"));
            
            log.info(this.getText(
                    "B2BPU0170",
                    new String[] { userProfile.getLoginId() }));
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    // ***************************************
    // View function
    // ***************************************
    
    public void validateView()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            
            if (!flag && (userProfile == null || userProfile.getUserOid() == null))
            {
                this.addActionError(this.getText("B2BPU0140"));
                flag = true;
            }
            if (!flag)
            {
                UserProfileTmpHolder upTmp = userProfileTmpService.selectUserProfileTmpByKey(userProfile.getUserOid());
                
                if (upTmp == null)
                {
                    this.addActionError(this.getText("B2BPU0141"));
                    flag = true;
                }
                else
                {
                    BeanUtils.copyProperties(upTmp, userProfile);
                    userProfile.setCompanyName(this.initCompanyInfo(upTmp));
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String view()
    {
        try
        {
            List<BuyerStoreHolder> storesList = null;
            List<BuyerStoreHolder> wareHouseList = null;
            List<BuyerStoreAreaHolder> areaList = null;
            List<ClassHolder> classList = null;
            List<SubclassExHolder> subclassList = null;
            UserTypeHolder userType = userTypeService.selectByKey(userProfile.getUserType());
            
            this.initGroupForViewUser(userProfile);
            
            if (userType != null)
                userProfile.setUserTypeDesc(userType.getUserTypeDesc());
            
            selectedRolesList = roleTmpService.selectRolesByUserOid(userProfile.getUserOid());
            if (selectedRolesList == null) selectedRolesList = new ArrayList<Object>();
            
            //allowed buyer
            selectedBuyersList = allowedAccessCompanyTmpService.selectBuyerByUserOid(userProfile.getUserOid());
            if (selectedBuyersList == null) selectedBuyersList = new ArrayList<Object>();
            
            //stores
            BuyerStoreUserTmpHolder tmpStoresList = buyerStoreUserTmpService
            .selectBuyerStoresFromTmpStoreUserByUserOidAndStoreOid(
                userProfile.getUserOid(),new BigDecimal(-3));
            
            if(tmpStoresList == null)
            {
                selectedStoresList = buyerStoreService
                .selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(
                    userProfile.getUserOid(), false);
                if(selectedStoresList == null || selectedStoresList.isEmpty())
                {
                    selectedStoresList = new ArrayList<Object>();
                }
                else
                {
                    this.formatStoreNameAndStoreCode(selectedStoresList);
                }
            }
            else
            {   //all stores
                storesList = new ArrayList<BuyerStoreHolder>();
                BuyerStoreHolder buyerStore = new BuyerStoreHolder();
                buyerStore.setStoreName(ALL_STORES);
                storesList.add(buyerStore);
                
                selectedStoresList = storesList;
            }
            

            //wareHouse
            BuyerStoreUserTmpHolder tmpWareHouseList = buyerStoreUserTmpService
            .selectBuyerStoresFromTmpStoreUserByUserOidAndStoreOid(
                userProfile.getUserOid(),new BigDecimal(-4));
            
            if(tmpWareHouseList == null)
            {
                selectedWareHouseList = buyerStoreService
                .selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(
                    userProfile.getUserOid(), true);
                if(selectedWareHouseList == null || selectedWareHouseList.isEmpty())
                {
                    selectedWareHouseList = new ArrayList<Object>();
                }
                else
                {
                    this.formatStoreNameAndStoreCode(selectedWareHouseList);
                }
            }
            else
            {   //all wareHouse
                wareHouseList = new ArrayList<BuyerStoreHolder>();
                BuyerStoreHolder buyerStore = new BuyerStoreHolder();
                buyerStore.setStoreName(ALL_WARE_HOUSE);
                wareHouseList.add(buyerStore);
                
                selectedWareHouseList = wareHouseList;
            }
            
            
            BuyerStoreAreaUserTmpHolder tmpAreaList = buyerStoreAreaUserTmpService
            .selectByUserOidAndAreaOid(userProfile.getUserOid(), new BigDecimal(-2));
            if(tmpAreaList == null)
            {
                selectedAreasList = buyerStoreAreaService.selectBuyerStoreAreaFromTmpAreaUserByUserOid(userProfile.getUserOid());
                if (selectedAreasList == null) selectedAreasList = new ArrayList<BuyerStoreAreaHolder>();
            }
            else
            {
                areaList = new ArrayList<BuyerStoreAreaHolder>();
                BuyerStoreAreaHolder buyerArea= new BuyerStoreAreaHolder();
                buyerArea.setAreaName(ALL_AREAS);
                buyerArea.setAreaCode(ALL_AREAS);
                areaList.add(buyerArea);
                selectedAreasList = areaList;
            }
            
            UserClassTmpHolder allTmpClass = userClassTmpService.selectUserClassTmpByUserOidAndClassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
            if (allTmpClass == null)
            {
                selectedClassList = classService.selectTmpClassByUserOid(userProfile.getUserOid());
            }
            else
            {
                classList = new ArrayList<ClassHolder>();
                ClassHolder class_ = new ClassHolder();
                class_.setClassOid(BigDecimal.valueOf(-1));
                class_.setClassCode(getText("user.all.class.selected"));
                classList.add(class_);
                selectedClassList = classList;
            }
            
            UserSubclassTmpHolder allTmpSubclass = userSubclassTmpService.selectUserSubclassTmpByUserOidAndSubClassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
            if (allTmpSubclass == null)
            {
                selectedSubclassList = subclassService.selectTmpSubclassExByUserOid(userProfile.getUserOid());
            }
            else
            {
                subclassList = new ArrayList<SubclassExHolder>();
                SubclassExHolder subClass = new SubclassExHolder();
                subClass.setSubclassOid(BigDecimal.valueOf(-1));
                subClass.setSubclassCode(getText("user.all.subclass.selected"));
                subclassList.add(subClass);
                selectedSubclassList = subclassList;
            }
            
            
            if(MkCtrlStatus.APPROVED.equals(userProfile.getCtrlStatus())
                    || MkCtrlStatus.REJECTED.equals(userProfile.getCtrlStatus())
                    || MkCtrlStatus.WITHDRAWN.equals(userProfile.getCtrlStatus()))
            {
                return "view";
            }
            
            userProfile.setActionTypeValue(this.getText(userProfile.getActionType().getKey()));
            userProfile.setCtrlStatusValue(this.getText(userProfile.getCtrlStatus().getKey()));
            
            if(MkCtrlStatus.PENDING.equals(userProfile.getCtrlStatus())
                && !DbActionType.UPDATE.equals(userProfile.getActionType()))
            {
                return "viewPending";
            }
            
            oldUserProfile = new UserProfileExHolder();

            BeanUtils.copyProperties(userProfileService
                    .selectUserProfileByKey(userProfile.getUserOid()),
                    oldUserProfile);

            UserTypeHolder oldUserType = userTypeService.selectByKey(oldUserProfile.getUserType());
            if (oldUserType != null)
                oldUserProfile.setUserTypeDesc(oldUserType.getUserTypeDesc());
            
            GroupHolder oldGroup = groupService.selectGroupByUserOid(userProfile.getUserOid());
            if (oldGroup != null)
                oldUserProfile.setGroupName(oldGroup.getGroupName());
            
            oldUserProfile.setCompanyName(this.initCompanyInfo(oldUserProfile));
            
            oldSelectedRolesList = roleService.selectRolesByUserOid(userProfile.getUserOid());
            if (oldSelectedRolesList == null) oldSelectedRolesList = new ArrayList<Object>();
            
            //allowed buyers
            oldSelectedBuyersList = allowedAccessCompanyService.selectBuyerByUserOid(userProfile.getUserOid());
            if (oldSelectedBuyersList == null) oldSelectedBuyersList = new ArrayList<Object>();
            
            //stores
            BuyerStoreUserHolder allStoresList = buyerStoreUserService
            .selectStoreUserByStoreOidAndUserOid(new BigDecimal(-3),
                userProfile.getUserOid());
            if(allStoresList == null)
            {
                oldSelectedStoresList = buyerStoreService
                    .selectBuyerStoresByUserOidAndIsWareHouse(userProfile
                        .getUserOid(), false);
                if(oldSelectedStoresList == null || oldSelectedStoresList.isEmpty())
                {
                    oldSelectedStoresList = new ArrayList<Object>();
                }
                else
                {
                    this.formatStoreNameAndStoreCode(oldSelectedStoresList);
                }    
            }
            else
            {  //all stores
                storesList = new ArrayList<BuyerStoreHolder>();
                BuyerStoreHolder buyerStore = new BuyerStoreHolder();
                buyerStore.setStoreName(ALL_STORES);
                storesList.add(buyerStore);
                
                oldSelectedStoresList = storesList;
            }

            
            //warehouse
            BuyerStoreUserHolder allWareHouseList = buyerStoreUserService
            .selectStoreUserByStoreOidAndUserOid(new BigDecimal(-4),
                userProfile.getUserOid());
            if(allWareHouseList == null)
            {
                //warehouse
                oldSelectedWareHouseList = buyerStoreService
                    .selectBuyerStoresByUserOidAndIsWareHouse(userProfile
                        .getUserOid(), true);
                if(oldSelectedWareHouseList == null || oldSelectedWareHouseList.isEmpty())
                {
                    oldSelectedWareHouseList = new ArrayList<Object>();
                }
                else
                {
                    this.formatStoreNameAndStoreCode(oldSelectedWareHouseList);
                }
                    
            }
            else
            {  //all stores
                wareHouseList = new ArrayList<BuyerStoreHolder>();
                BuyerStoreHolder buyerStore = new BuyerStoreHolder();
                buyerStore.setStoreName(ALL_WARE_HOUSE);
                wareHouseList.add(buyerStore);
                
                oldSelectedWareHouseList = wareHouseList;
            }
            
            List<BuyerStoreAreaHolder> oldAreasList = null;
            List<BuyerStoreAreaUserHolder> oldAreaList = buyerStoreAreaUserService.selectAreaUserByUserOid(userProfile.getUserOid());
            if (oldAreaList != null && !oldAreaList.isEmpty() && oldAreaList.size() == 1 && oldAreaList.get(0).getAreaOid().intValue() == -2)
            {   
                oldAreasList = new ArrayList<BuyerStoreAreaHolder>();
                BuyerStoreAreaHolder storeArea = new BuyerStoreAreaHolder();
                storeArea.setAreaName(ALL_AREAS);
                storeArea.setAreaCode(ALL_AREAS);
                oldAreasList.add(0,storeArea);
                oldSelectedAreasList = oldAreasList;
            }
            else
            {
                oldSelectedAreasList = buyerStoreAreaService.selectBuyerStoreAreaByUserOid(userProfile.getUserOid());
            }
            
            UserClassHolder allClass = userClassService.selectByUserOidAndClassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
            if (allClass == null)
            {
                oldSelectedClassList = classService.selectClassByUserOid(userProfile.getUserOid());
            }
            else
            {
                classList = new ArrayList<ClassHolder>();
                ClassHolder class_ = new ClassHolder();
                class_.setClassOid(BigDecimal.valueOf(-1));
                class_.setClassCode(getText("user.all.class.selected"));
                classList.add(class_);
                oldSelectedClassList = classList;
            }
            

            UserSubclassHolder allSubclass = userSubclassService.selectByUserOidAndSubclassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
            if (allSubclass == null)
            {
                oldSelectedSubclassList = subclassService.selectSubclassExByUserOid(userProfile.getUserOid());
            }
            else
            {
                subclassList = new ArrayList<SubclassExHolder>();
                SubclassExHolder subClass = new SubclassExHolder();
                subClass.setSubclassOid(BigDecimal.valueOf(-1));
                subClass.setSubclassCode(getText("user.all.subclass.selected"));
                subclassList.add(subClass);
                oldSelectedSubclassList = subclassList;
            }
            return "viewPendingUpdate";
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
            
    }
    
    
    // ***************************************
    // Edit function
    // ***************************************
    
    public List<? extends Object> getOldSelectedWareHouseList()
    {
        return oldSelectedWareHouseList;
    }


    public void validateInitEdit()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            
            if (!flag && (userProfile == null || userProfile.getUserOid() == null))
            {
                this.addActionError(this.getText("B2BPU0140"));
                flag = true;
            }
            if (!flag)
            {
                UserProfileTmpHolder upTmp = userProfileTmpService.selectUserProfileTmpByKey(userProfile.getUserOid());
                
                if (upTmp == null)
                {
                    this.addActionError(this.getText("B2BPU0141"));
                    flag = true;
                }
                if (!flag)
                {
                    BeanUtils.copyProperties(upTmp, userProfile);
                }
            }
            if (flag)
            {
                this.initSearchCondition();
                userProfile = (UserProfileTmpExHolder) getSession()
                .get(SESSION_KEY_SEARCH_PARAMETER_USER_PROFILE);
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }

    
    public String initEdit()
    {
        try
        {
            if (isSupplier(userProfile.getUserType()))
            {
                SupplierHolder supplier = supplierService.selectSupplierByKey(userProfile.getSupplierOid());
                userProfile.setLoginId(separateLoginId(supplier, userProfile.getLoginId()));
            }
            userTypeList = userTypeService.selectPrivilegedSubUserTypesByUserType(this.getUserTypeOfCurrentUser());
           
            buyerList = buyerService.select(new BuyerHolder());
            if (isBuyer(this.getUserTypeOfCurrentUser()))
            {
                supplierList = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else
            {
                supplierList = supplierService.select(new SupplierExHolder());
            }
            userProfile.setCompanyName(this.initCompanyInfo(this.getProfileOfCurrentUser()));
            List<GroupUserTmpHolder> groupUsers = groupUserTmpService.selectGroupUserTmpByUserOid(userProfile.getUserOid());
            
            if (groupUsers == null || groupUsers.isEmpty())
            {
                isAddToGroup = false;
                userProfile.setIsLocked(false);
            }
            else
            {
                this.parseGroupUserForUpdateUser(groupUsers);
            }
            this.initRolesByConditionFromUserProfile(true);
            this.initAvailableStoreAndAreaFromUserProfile();
            this.initAvailableClassAndSubclassFromUserProfile(true);
            this.initAvaliableBuyerFromUserProfile();
            
            selectedRolesList = roleTmpService.selectRolesByUserOid(userProfile.getUserOid());
            
            selectedBuyersList = allowedAccessCompanyTmpService.selectBuyerByUserOid(userProfile.getUserOid());
            
            BuyerStoreAreaUserTmpHolder allArea = buyerStoreAreaUserTmpService.selectByUserOidAndAreaOid(
                    userProfile.getUserOid(), new BigDecimal(-2));
            
            if(allArea == null)
            {   
                selectedAreasList = buyerStoreAreaService.selectBuyerStoreAreaFromTmpAreaUserByUserOid(userProfile.getUserOid());
            }
            else
            {
                //Has value all areas
                List<BuyerStoreAreaHolder> areaList = new ArrayList<BuyerStoreAreaHolder>();
                BuyerStoreAreaHolder buyerStoreArea = new BuyerStoreAreaHolder();
                buyerStoreArea.setAreaName(getText("user.all.area.selected"));
                buyerStoreArea.setAreaCode(getText("user.all.area.selected"));
                buyerStoreArea.setAreaOid(new BigDecimal(-2));
                areaList.add(buyerStoreArea);
                
                selectedAreasList = areaList;
            }
            
            //get stores
            BuyerStoreUserTmpHolder allStore = buyerStoreUserTmpService.selectBuyerStoresFromTmpStoreUserByUserOidAndStoreOid(userProfile
                    .getUserOid(), new BigDecimal(-3));
            if(allStore == null)
            {  
                selectedStoresList = buyerStoreService.selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(userProfile.getUserOid(),false);
            }
            else
            {
                List<BuyerStoreHolder> bsList = new ArrayList<BuyerStoreHolder>();
                BuyerStoreHolder buyerStore = new BuyerStoreHolder();
                buyerStore.setStoreOid(new BigDecimal(-3));
                buyerStore.setStoreCode(getText("user.all.store.selected"));
                buyerStore.setStoreName("");
                bsList.add(buyerStore);
                
                selectedStoresList = bsList;
            }
       
            //get warehouse
            BuyerStoreUserTmpHolder allWarehouse = buyerStoreUserTmpService.selectBuyerStoresFromTmpStoreUserByUserOidAndStoreOid(userProfile
                .getUserOid(), new BigDecimal(-4));
            if(allWarehouse == null)
            {
                selectedWareHouseList = buyerStoreService.selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(userProfile.getUserOid(),true);
            }
            else
            {
                List<BuyerStoreHolder> bwList = new ArrayList<BuyerStoreHolder>();
                BuyerStoreHolder buyerStore = new BuyerStoreHolder();
                buyerStore.setStoreOid(new BigDecimal(-4));
                buyerStore.setStoreCode(getText("user.all.warehouse.selected"));
                buyerStore.setStoreName("");
                bwList.add(buyerStore);
                
                selectedWareHouseList = bwList;
            }
            
            //get class
            UserClassTmpHolder allClass = userClassTmpService.selectUserClassTmpByUserOidAndClassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
            if(allClass == null)
            {
                selectedClassList = classService.selectTmpClassByUserOid(userProfile.getUserOid());
            }
            else
            {
                List<ClassHolder> classList = new ArrayList<ClassHolder>();
                ClassHolder class_ = new ClassHolder();
                class_.setClassOid(BigDecimal.valueOf(-1));
                class_.setClassCode(getText("user.all.class.selected"));
                classList.add(class_);
                selectedClassList = classList;
            }
            
            //get subclass
            UserSubclassTmpHolder allSubclass = userSubclassTmpService.selectUserSubclassTmpByUserOidAndSubClassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
            if(allSubclass == null)
            {
                selectedSubclassList = subclassService.selectTmpSubclassExByUserOid(userProfile.getUserOid());
            }
            else
            {
                List<SubclassHolder> subclassList = new ArrayList<SubclassHolder>();
                SubclassHolder subclass = new SubclassHolder();
                subclass.setSubclassOid(BigDecimal.valueOf(-1));
                subclass.setSubclassCode(getText("user.all.subclass.selected"));
                subclassList.add(subclass);
                selectedSubclassList = subclassList;
            }
            
            loginModelList = appConfig.getLoginModel();
            
            if (selectedRolesList == null) selectedRolesList = new ArrayList<Object>();
            if (availabelRolesList == null) availabelRolesList = new ArrayList<Object>();
            if (groupList == null) groupList = new ArrayList<GroupHolder>();
            if (availabelStoresList == null) availabelStoresList = new ArrayList<Object>();
            if (selectedStoresList == null) selectedStoresList = new ArrayList<Object>();
            if (availabelAreasList == null) availabelAreasList = new ArrayList<Object>();
            if (selectedAreasList == null) selectedAreasList = new ArrayList<Object>();
            if (buyerList == null) buyerList = new ArrayList<BuyerHolder>();
            if (supplierList == null) supplierList = new ArrayList<SupplierHolder>();
            if (availabelWareHouseList == null) availabelWareHouseList = new ArrayList<Object>();
            if (selectedWareHouseList == null) selectedWareHouseList = new ArrayList<Object>();
            if(availabelBuyersList == null) availabelBuyersList = new ArrayList<Object>();
            if(selectedBuyersList == null) selectedBuyersList = new ArrayList<Object>();
            if(availableClassList == null) availableClassList = new ArrayList<Object>();
            if(selectedClassList == null) selectedClassList = new ArrayList<Object>();
            if(availableSubclassList == null) availableSubclassList = new ArrayList<Object>();
            if(selectedSubclassList == null) selectedSubclassList = new ArrayList<Object>();
            
            if (!selectedRolesList.isEmpty())
            {
                List<Object> tmpList = new ArrayList<Object>();
                List<Object> assignedRoleOids = this.convertSelectedRole(selectedRolesList);
                for (Object obj : availabelRolesList)
                {
                    RoleHolder role = (RoleHolder) obj;
                    if (assignedRoleOids.contains(role.getRoleOid()))
                    {
                        tmpList.add(role);
                    }
                }
                availabelRolesList.removeAll(tmpList);
            }
            if (!selectedBuyersList.isEmpty())
            {
                List<Object> tmpList = new ArrayList<Object>();
                List<Object> assignedBuyerOids = this.convertSelectedBuyer(selectedBuyersList);
                for (Object obj : availabelBuyersList)
                {
                    BuyerHolder buyer = (BuyerHolder) obj;
                    if (assignedBuyerOids.contains(buyer.getBuyerOid()))
                    {
                        tmpList.add(buyer);
                    }
                }
                
                availabelBuyersList.removeAll(tmpList);
            }
            
            if(allStore == null)
            {
                if (!selectedStoresList.isEmpty())
                {
                    List<Object> tmpList = new ArrayList<Object>();
                    List<Object> storeOids = this.convertSelectedStore(selectedStoresList);
                    for (Object obj : availabelStoresList)
                    {
                        BuyerStoreHolder store = (BuyerStoreHolder) obj;
                        if (storeOids.contains(store.getStoreOid()))
                        {
                            tmpList.add(store);
                        }
                    }
                    availabelStoresList.removeAll(tmpList);
                }
            }
            else
            {
                availabelStoresList.clear();
            }
            
            if(allWarehouse == null)
            {
                if (!selectedWareHouseList.isEmpty())
                {
                    List<Object> tmpList = new ArrayList<Object>();
                    List<Object> warehouseOids = this.convertSelectedStore(selectedWareHouseList);
                    for (Object obj : availabelWareHouseList)
                    {
                        BuyerStoreHolder store = (BuyerStoreHolder) obj;
                        if (warehouseOids.contains(store.getStoreOid()))
                        {
                            tmpList.add(store);
                        }
                    }
                    availabelWareHouseList.removeAll(tmpList);
                }
            }
            else
            {
                availabelWareHouseList.clear();
            }
            
            if(allArea == null)
            {
                if (!selectedAreasList.isEmpty())
                {
                    List<Object> tmpList = new ArrayList<Object>();
                    List<Object> areaOids = this.convertSelectedArea(selectedAreasList); 
                    for (Object obj : availabelAreasList)
                    {
                        BuyerStoreAreaHolder area = (BuyerStoreAreaHolder) obj;
                        if (areaOids.contains(area.getAreaOid()))
                        {
                            tmpList.add(area);
                        }
                    }
                    availabelAreasList.removeAll(tmpList);
                }
            }
            else
            {
                availabelAreasList.clear();
            }
            
            if(allClass == null)
            {
                if (!selectedClassList.isEmpty())
                {
                    List<Object> tmpList = new ArrayList<Object>();
                    List<Object> classOids = this.convertSelectedClass(selectedClassList); 
                    for (Object obj : availableClassList)
                    {
                        ClassHolder class_ = (ClassHolder) obj;
                        if (classOids.contains(class_.getClassOid()))
                        {
                            tmpList.add(class_);
                        }
                    }
                    availableClassList.removeAll(tmpList);
                }
            }
            else
            {
                availableClassList.clear();
            }
            
            if(allSubclass == null)
            {
                if (!selectedSubclassList.isEmpty())
                {
                    List<Object> tmpList = new ArrayList<Object>();
                    List<Object> subclassOids = this.convertSelectedSubclass(selectedSubclassList); 
                    for (Object obj : availableSubclassList)
                    {
                        SubclassHolder subclass = (SubclassHolder) obj;
                        if (subclassOids.contains(subclass.getSubclassOid()))
                        {
                            tmpList.add(subclass);
                        }
                    }
                    availableSubclassList.removeAll(tmpList);
                }
            }
            else
            {
                availableSubclassList.clear();
            }
            
            initSelectAllSessionInfo();
        }
        catch(Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public void validateSaveEdit()
    {
        try
        {
            boolean flag = this.hasErrors();
            
            if(!flag && userProfile == null)
            {
                this.addActionError(this.getText("B2BPU0161"));
                flag = true;
            }
            if (!flag && (userProfile.getLoginId() == null || "".equals(userProfile.getLoginId())))
            {
                this.addActionError(this.getText("B2BPU0136"));
                flag = true;
            }
            String loginId = userProfile.getLoginId();
            if (isSupplier(userProfile.getUserType()))
            {
                BigDecimal supplierOid = userProfile.getSupplierOid();
                if (isSupplier(getUserTypeOfCurrentUser()))
                {
                    supplierOid = getProfileOfCurrentUser().getSupplierOid();
                }
                SupplierHolder supplier = supplierService.selectSupplierByKey(supplierOid);
                loginId = initLoginId(supplier, userProfile.getLoginId());
            }
            
            if (loginId.length() > 50)
            {
                this.addActionError(this.getText("B2BPU0174", new String[]{loginId}));
                flag = true;
            }
            
            UserProfileTmpHolder oldUser = userProfileTmpService.selectUserProfileTmpByKey(userProfile.getUserOid());
            if (!flag && !oldUser.getLoginId().equalsIgnoreCase(loginId) 
                    && userProfileTmpService.isLoginIdExist(loginId))
            {
                this.addActionError(this.getText("B2BPU0120", new String[]{loginId}));
                flag = true;
            }
            if (!flag
                    && (userProfile.getUserType().equals(BigDecimal.valueOf(2)) 
                            || userProfile.getUserType().equals(BigDecimal.valueOf(4))
                            || userProfile.getUserType().equals(BigDecimal.valueOf(6)) 
                            ||userProfile.getUserType().equals(BigDecimal.valueOf(7)))
                    && userProfile.getBuyerOid() == null
                    && this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(1)))
            {
                this.addActionError(this.getText("B2BPU0134"));
                flag = true;
            }
            if (!flag
                    && (userProfile.getUserType().equals(BigDecimal.valueOf(3)) 
                            || userProfile.getUserType().equals(BigDecimal.valueOf(5)))
                    && userProfile.getSupplierOid() == null
                    && !this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
            {
                this.addActionError(this.getText("B2BPU0135"));
                flag = true;
            }
            if (!flag && (selectedRolesList == null) || (selectedRolesList.isEmpty()))
            {
                this.addActionError(this.getText("B2BPU0172"));
                flag = true;
            }
            if (!flag && isBuyer(userProfile.getUserType()) && !isAddToGroup)
            {
                this.addActionError(this.getText("B2BPU0173"));
                flag = true;
            }
            if (!flag && isAddToGroup && userProfile.getGroupOid() == null)
            {
                this.addActionError(this.getText("B2BPU0164"));
                flag = true;
            }
            this.parseCompanyInfoForUser();
            //supplier admin only create max supplier user
            if (userProfile.getUserType().equals(BigDecimal.valueOf(3)) || userProfile.getUserType().equals(BigDecimal.valueOf(5)))
            {
                String maxSupplierUser = "";
                
                //only current login user is not 3, 5 to edit SA or SU ,and userType or supplierOid has been change. 
                if ((!this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)) 
                        && !this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(5)))
                    && ((oldUser.getSupplierOid() != null 
                        && !oldUser.getSupplierOid().equals(userProfile.getSupplierOid()))
                    || !(oldUser.getUserType().equals(BigDecimal.valueOf(3)) 
                        || oldUser.getUserType().equals(BigDecimal.valueOf(5)))))
                {
                    maxSupplierUser = checkMaxSupplierUser(userProfile.getSupplierOid());
                }
                
                if(!maxSupplierUser.trim().isEmpty())
                {
                    this.addActionError(this.getText("B2BPU0171", new String[]{maxSupplierUser}));
                    flag = true;
                }
            }
            if (flag)
            {
                userTypeList = userTypeService
                        .selectPrivilegedSubUserTypesByUserType(this.getUserTypeOfCurrentUser());
                
                buyerList = buyerService.select(new BuyerHolder());
                if (isBuyer(this.getUserTypeOfCurrentUser()))
                {
                    supplierList = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
                else
                {
                    supplierList = supplierService.select(new SupplierExHolder());
                }
                if (userTypeList == null)
                {
                    userTypeList = new ArrayList<UserTypeHolder>();
                }
                
                if (userProfile != null && userProfile.getUserType() != null)
                {
                    this.initRolesByConditionFromUserProfile(false);
                    this.initAvailableStoreAndAreaFromUserProfile();
                    this.initAvailableClassAndSubclassFromUserProfile(false);
                    this.initAvaliableBuyerFromUserProfile();
                }
                
                if (availabelRolesList == null) availabelRolesList = new ArrayList<Object>();
                if (selectedRolesList == null) selectedRolesList = new ArrayList<Object>();
                if(availabelBuyersList == null) availabelBuyersList = new ArrayList<Object>();
                if(selectedBuyersList == null) selectedBuyersList = new ArrayList<Object>();
                if (availabelStoresList == null) availabelStoresList = new ArrayList<Object>();
                if (selectedStoresList == null) selectedStoresList = new ArrayList<Object>();
                if (availabelWareHouseList == null) availabelWareHouseList = new ArrayList<Object>();
                if (selectedWareHouseList == null) selectedWareHouseList = new ArrayList<Object>();
                if (availabelAreasList == null) availabelAreasList = new ArrayList<Object>();
                if (selectedAreasList == null) selectedAreasList = new ArrayList<Object>();
                if (availableClassList == null) availableClassList = new ArrayList<Object>();
                if (selectedClassList == null) selectedClassList = new ArrayList<Object>();
                if (availableSubclassList == null) availableSubclassList = new ArrayList<Object>();
                if (selectedSubclassList == null) selectedSubclassList = new ArrayList<Object>();
                
                initListForValidateFailed();
                
                loginModelList = appConfig.getLoginModel();
                if (groupList == null) groupList = new ArrayList<GroupHolder>();
                if (buyerList == null) buyerList = new ArrayList<BuyerHolder>();
                if (supplierList == null) supplierList = new ArrayList<SupplierHolder>();
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String saveEdit()
    {
        try
        {
            UserProfileTmpHolder oldObj_ = userProfileTmpService
                    .selectUserProfileTmpByKey(userProfile.getUserOid());
            
            userProfile.trimAllString();
            userProfile.setAllEmptyStringToNull();
            
            userProfile.setUpdateDate(new Date());
            userProfile.setUpdateBy(this.getLoginIdOfCurrentUser());
            userProfile.setCreateBy(oldObj_.getCreateBy());
            userProfile.setCreateDate(oldObj_.getCreateDate());
            if (userProfile.getBlocked())
            {
                userProfile.setFailAttempts(oldObj_.getFailAttempts());
            }
            else
            {
                userProfile.setFailAttempts(0);
            }
            userProfile.setInit(oldObj_.getInit());
            userProfile.setLoginPwd(oldObj_.getLoginPwd());
            userProfile.setActionType(oldObj_.getActionType());
            userProfile.setCtrlStatus(oldObj_.getCtrlStatus());
            
            if (isSupplier(userProfile.getUserType()))
            {
                SupplierHolder supplier = supplierService.selectSupplierByKey(userProfile.getSupplierOid());
                userProfile.setLoginId(initLoginId(supplier, userProfile.getLoginId()));
            }
            
            List<RoleUserHolder> ruList = new ArrayList<RoleUserHolder>();
            for (Object o : selectedRolesList)
            {
                RoleUserTmpHolder ru = new RoleUserTmpHolder();
                ru.setRoleOid(new BigDecimal(o.toString()));
                ru.setUserOid(userProfile.getUserOid());
                
                ruList.add(ru);
            }
            userProfile.setRoleUsers(ruList);
            
            List<AllowedAccessCompanyHolder> aacList = new ArrayList<AllowedAccessCompanyHolder>();
            for (Object o : selectedBuyersList)
            {
                AllowedAccessCompanyTmpHolder aac = new AllowedAccessCompanyTmpHolder();
                aac.setCompanyOid(new BigDecimal(o.toString()));
                aac.setUserOid(userProfile.getUserOid());
                aacList.add(aac);
            }
            userProfile.setAllowedBuyerList(aacList);
            
            List<BuyerStoreUserHolder> bsuList = new ArrayList<BuyerStoreUserHolder>();
            for (Object o : selectedStoresList)
            {
                BuyerStoreUserTmpHolder bsu = new BuyerStoreUserTmpHolder();
                bsu.setUserOid(userProfile.getUserOid());
                bsu.setStoreOid(new BigDecimal(o.toString()));
                
                bsuList.add(bsu);
            }
            userProfile.setBuyerStoreUsers(bsuList);

            List<BuyerStoreUserHolder> buyerWareHouseList = new ArrayList<BuyerStoreUserHolder>();
            for (Object o : selectedWareHouseList)
            {
                BuyerStoreUserTmpHolder bsu = new BuyerStoreUserTmpHolder();
                bsu.setUserOid(userProfile.getUserOid());
                bsu.setStoreOid(new BigDecimal(o.toString()));
                
                buyerWareHouseList.add(bsu);
            }
            userProfile.setBuyerWareHouseUsers(buyerWareHouseList);
            
            List<BuyerStoreAreaUserHolder> bsauList = new ArrayList<BuyerStoreAreaUserHolder>();
            for (Object o : selectedAreasList)
            {
                BuyerStoreAreaUserTmpHolder bsau = new BuyerStoreAreaUserTmpHolder();
                bsau.setUserOid(userProfile.getUserOid());
                bsau.setAreaOid(new BigDecimal(o.toString()));
                
                bsauList.add(bsau);
            }
            userProfile.setBuyerStoreAreaUsers(bsauList);
            
            List<UserClassHolder> ucList = new ArrayList<UserClassHolder>();
            for (Object o : selectedClassList)
            {
                UserClassTmpHolder uct = new UserClassTmpHolder();
                uct.setUserOid(userProfile.getUserOid());
                uct.setClassOid(new BigDecimal(o.toString()));
                
                ucList.add(uct);
            }
            userProfile.setUserClassList(ucList);
            
            List<UserSubclassHolder> usList = new ArrayList<UserSubclassHolder>();
            for (Object o : selectedSubclassList)
            {
                UserSubclassTmpHolder ust = new UserSubclassTmpHolder();
                ust.setUserOid(userProfile.getUserOid());
                ust.setSubclassOid(new BigDecimal(o.toString()));
                usList.add(ust);
            }
            userProfile.setUserSubclassList(usList);
            
            if (!isAddToGroup)
            {
                userProfile.setGroupOid(null);
            }
            List<GroupUserHolder> guList = new ArrayList<GroupUserHolder>();
            if(userProfile.getGroupOid() != null)
            {
                
                GroupUserTmpHolder gu = new GroupUserTmpHolder();
                gu.setGroupOid(userProfile.getGroupOid());
                gu.setUserOid(userProfile.getUserOid());
                
                guList.add(gu);
            }
            userProfile.setGroupUsers(guList);
            
            oldObj_.setGroupUsers(new ArrayList<GroupUserHolder>());
            oldObj_.setRoleUsers(new ArrayList<RoleUserHolder>());
            oldObj_.setAllowedBuyerList(new ArrayList<AllowedAccessCompanyHolder>());
            oldObj_.setBuyerStoreUsers(new ArrayList<BuyerStoreUserHolder>());
            oldObj_.setBuyerStoreAreaUsers(new ArrayList<BuyerStoreAreaUserHolder>());
            oldObj_.setUserClassList(new ArrayList<UserClassHolder>());
            oldObj_.setUserSubclassList(new ArrayList<UserSubclassHolder>());
            
            List<GroupUserTmpHolder> oldGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(userProfile.getUserOid());
            for (GroupUserTmpHolder oldGroupUser : oldGroupUserList)
                oldObj_.getGroupUsers().add(oldGroupUser);

            List<RoleUserTmpHolder> oldRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(userProfile.getUserOid());
            for (RoleUserTmpHolder oldRoleUser : oldRoleUserList)
                oldObj_.getRoleUsers().add(oldRoleUser);
            
            List<AllowedAccessCompanyTmpHolder> oldAllowedAccessCompanyList = allowedAccessCompanyTmpService.selectByUserOid(userProfile.getUserOid());
            for (AllowedAccessCompanyTmpHolder oldAllowedAccessCompany : oldAllowedAccessCompanyList)
                oldObj_.getAllowedBuyerList().add(oldAllowedAccessCompany);
            
            List<BuyerStoreAreaUserTmpHolder> oldAreaUserList = buyerStoreAreaUserTmpService.selectAreaUserTmpByUserOid(userProfile.getUserOid());
            for (BuyerStoreAreaUserTmpHolder oldAreaUser : oldAreaUserList)
                oldObj_.getBuyerStoreAreaUsers().add(oldAreaUser);
            
            List<BuyerStoreUserTmpHolder> oldStoreUserList = buyerStoreUserTmpService.selectStoreUserTmpByUserOid(userProfile.getUserOid());
            for (BuyerStoreUserTmpHolder oldStoreUser : oldStoreUserList)
                oldObj_.getBuyerStoreUsers().add(oldStoreUser);
            
            List<UserClassTmpHolder> oldUserClassList = userClassTmpService.selectUserClassTmpByUserOid(userProfile.getUserOid());
            for (UserClassTmpHolder oldUserClass : oldUserClassList)
                oldObj_.getUserClassList().add(oldUserClass);
            
            List<UserSubclassTmpHolder> oldUserSubclassList = userSubclassTmpService.selectUserSubclassTmpByUserOid(userProfile.getUserOid());
            for (UserSubclassTmpHolder oldUserSubclass : oldUserSubclassList)
                oldObj_.getUserSubclassList().add(oldUserSubclass);
            
            userProfileService.updateUserProfile(this.getCommonParameter(),
                oldObj_, userProfile, appConfig.getServerUrl() + "/user/", this
                    .getRequest().getRemoteAddr());
            
            log.info(this.getText(
                    "B2BPU0109",
                    new String[] { userProfile.getLoginId(),
                            this.getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    // ***************************************
    // Init Confirm function
    // ***************************************
    
    public String initConfirm()
    {
        try
        {
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids || "".equals(selectedOids))
            {
                msg.saveError(this.getText(ONE_RECORD_TO_OPERATE_CODE));
                msg.addMessageTarget(mt);
                log.info(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                return FORWARD_COMMON_MESSAGE;
            }
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            this.mkCreateUserProfileList = new ArrayList<UserProfileTmpExHolder>();
            this.mkDeleteUserProfileList = new ArrayList<UserProfileTmpExHolder>();
            this.mkUpdateUserProfileList = new ArrayList<UserProfileTmpExHolder>();
            
            for (String part : parts)
            {
                BigDecimal userOid = new BigDecimal(part);

                UserProfileTmpHolder user = userProfileTmpService.selectUserProfileTmpByKey(userOid);
                UserProfileTmpExHolder userProfile = new UserProfileTmpExHolder();
                
                UserTypeHolder userType = userTypeService.selectByKey(user.getUserType());
                BeanUtils.copyProperties(user, userProfile);
                
                if (userType != null)
                    userProfile.setUserTypeDesc(userType.getUserTypeDesc());
                
                this.initGroupForViewUser(userProfile);
                
                userProfile.setCompanyName(this.initCompanyInfo(userProfile));
                
                
                //role 
                List<RoleTmpHolder> selectedRolesList = roleTmpService.selectRolesByUserOid(userProfile.getUserOid());
                if (selectedRolesList == null) selectedRolesList = new ArrayList<RoleTmpHolder>();
                
                userProfile.setSelectedRolesList(selectedRolesList);
                
                //allowed buyer
                List<BuyerHolder> selectedBuyersList = allowedAccessCompanyTmpService.selectBuyerByUserOid(userProfile.getUserOid());
                if (selectedBuyersList == null)
                {
                    userProfile.setSelectedBuyersList(new ArrayList<BuyerHolder>());
                }
                else
                {
                    userProfile.setSelectedBuyersList(selectedBuyersList);
                }
                
                
                BuyerStoreUserTmpHolder tmpStoresList = buyerStoreUserTmpService
                    .selectBuyerStoresFromTmpStoreUserByUserOidAndStoreOid(
                        userProfile.getUserOid(), new BigDecimal(-3));
                List<BuyerStoreHolder> selectedStoresList = null;
                BuyerStoreHolder buyerStore = null;
                if(tmpStoresList == null)
                {
                    //store
                    selectedStoresList = buyerStoreService
                        .selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(
                            userProfile.getUserOid(), false);
                    if(selectedStoresList == null || selectedStoresList.isEmpty())
                    {
                        selectedStoresList = new ArrayList<BuyerStoreHolder>();
                    }
                    else
                    {
                        this.formatStoreNameAndStoreCode(selectedStoresList);
                    }

                }
                else
                {
                    selectedStoresList = new ArrayList<BuyerStoreHolder>();
                    buyerStore = new BuyerStoreHolder();
                    buyerStore.setStoreName(ALL_STORES);
                    selectedStoresList.add(buyerStore);
                }
               
                userProfile.setSelectedStoresList(selectedStoresList);
                
                //warehouse
                BuyerStoreUserTmpHolder tmpWareHouseList = buyerStoreUserTmpService
                .selectBuyerStoresFromTmpStoreUserByUserOidAndStoreOid(
                    userProfile.getUserOid(), new BigDecimal(-4));
                List<BuyerStoreHolder> selectedWareHouseList = null;
                if(tmpWareHouseList == null)
                {
                    selectedWareHouseList = buyerStoreService
                        .selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(
                            userProfile.getUserOid(), true);
                    if(selectedWareHouseList == null || selectedWareHouseList.isEmpty())
                    {
                        selectedWareHouseList = new ArrayList<BuyerStoreHolder>();
                    }
                    else
                    {
                        this.formatStoreNameAndStoreCode(selectedWareHouseList);
                    }

                }
                else
                {
                    selectedWareHouseList = new ArrayList<BuyerStoreHolder>();
                    buyerStore = new BuyerStoreHolder();
                    buyerStore.setStoreName(ALL_WARE_HOUSE);
                    selectedWareHouseList.add(buyerStore);
                }
                
                userProfile.setSelectedWareHouseList(selectedWareHouseList);
                
                
                BuyerStoreAreaUserTmpHolder tmpAreaList = buyerStoreAreaUserTmpService
                .selectByUserOidAndAreaOid(userProfile.getUserOid(), new BigDecimal(-2));
                List<BuyerStoreAreaHolder> selectedAreasList = null;
                if(tmpAreaList == null)
                {
                    selectedAreasList = buyerStoreAreaService.selectBuyerStoreAreaFromTmpAreaUserByUserOid(userProfile.getUserOid());
                    if (selectedAreasList == null) selectedAreasList = new ArrayList<BuyerStoreAreaHolder>();
                }
                else
                {
                    selectedAreasList = new ArrayList<BuyerStoreAreaHolder>();
                    BuyerStoreAreaHolder buyerArea= new BuyerStoreAreaHolder();
                    buyerArea.setAreaName(ALL_AREAS);
                    buyerArea.setAreaCode(ALL_AREAS);
                    selectedAreasList.add(buyerArea);
                }
                
                userProfile.setSelectedAreasList(selectedAreasList);
                
                UserClassTmpHolder allTmpClass = userClassTmpService.selectUserClassTmpByUserOidAndClassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
                List<ClassExHolder> selectedTmpClassList = null;
                if (allTmpClass == null)
                {
                    selectedTmpClassList = classService.selectTmpClassByUserOid(userProfile.getUserOid());
                    if (selectedTmpClassList == null) selectedTmpClassList = new ArrayList<ClassExHolder>();
                }
                else
                {
                    selectedTmpClassList = new ArrayList<ClassExHolder>();
                    ClassExHolder class_ = new ClassExHolder();
                    class_.setClassOid(BigDecimal.valueOf(-1));
                    class_.setClassCode(getText("user.all.class.selected"));
                    selectedTmpClassList.add(class_);
                }
                
                userProfile.setSelectedClassList(selectedTmpClassList);
                
                UserSubclassTmpHolder allTmpSubclass = userSubclassTmpService.selectUserSubclassTmpByUserOidAndSubClassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
                List<SubclassExHolder> selectedTmpSubclassList = null;
                if (allTmpSubclass == null)
                {
                    selectedTmpSubclassList = subclassService.selectTmpSubclassExByUserOid(userProfile.getUserOid());
                    if (selectedTmpSubclassList == null) selectedTmpSubclassList = new ArrayList<SubclassExHolder>();
                }
                else
                {
                    selectedTmpSubclassList = new ArrayList<SubclassExHolder>();
                    SubclassExHolder subClass = new SubclassExHolder();
                    subClass.setSubclassOid(BigDecimal.valueOf(-1));
                    subClass.setSubclassCode(getText("user.all.subclass.selected"));
                    selectedTmpSubclassList.add(subClass);
                }
                
                userProfile.setSelectedSubclassList(selectedTmpSubclassList);
                
                userProfile.setActionTypeValue(this.getText(userProfile.getActionType().getKey()));
                userProfile.setCtrlStatusValue(this.getText(userProfile.getCtrlStatus().getKey()));
                
                if(MkCtrlStatus.PENDING.equals(userProfile.getCtrlStatus())
                    && DbActionType.CREATE.equals(userProfile.getActionType()))
                {
                    this.mkCreateUserProfileList.add(userProfile);
                    continue;
                }
                else if(MkCtrlStatus.PENDING.equals(userProfile.getCtrlStatus())
                        && DbActionType.DELETE.equals(userProfile.getActionType()))
                {
                    this.mkDeleteUserProfileList.add(userProfile);
                    continue;
                }
                UserProfileTmpExHolder oldUserProfile = new UserProfileTmpExHolder();

                BeanUtils.copyProperties(userProfileService
                        .selectUserProfileByKey(userProfile.getUserOid()),
                        oldUserProfile);

                UserTypeHolder oldUserType = userTypeService.selectByKey(oldUserProfile.getUserType());
                if (oldUserType != null)
                    oldUserProfile.setUserTypeDesc(oldUserType.getUserTypeDesc());
                
                GroupHolder oldGroup = groupService.selectGroupByUserOid(oldUserProfile.getUserOid());
                
                if (oldGroup != null)
                    oldUserProfile.setGroupName(oldGroup.getGroupName());
                
                oldUserProfile.setCompanyName(this.initCompanyInfo(oldUserProfile));
                //role
                List<RoleHolder> oldSelectedRolesList = roleService.selectRolesByUserOid(oldUserProfile.getUserOid());
                if (oldSelectedRolesList == null) oldSelectedRolesList = new ArrayList<RoleHolder>();
                oldUserProfile.setOldSelectedRolesList(oldSelectedRolesList);
                
                //allowed buyer
                List<BuyerHolder> oldSelectedBuyersList = allowedAccessCompanyService.selectBuyerByUserOid(userProfile.getUserOid());
                if (oldSelectedBuyersList == null || oldSelectedBuyersList.isEmpty())
                {
                    oldUserProfile.setOldSelectedBuyersList(new ArrayList<BuyerHolder>());
                }
                else
                {
                    oldUserProfile.setOldSelectedBuyersList(oldSelectedBuyersList);
                }
                
                //stores
                BuyerStoreUserHolder allStoresList = buyerStoreUserService
                .selectStoreUserByStoreOidAndUserOid(new BigDecimal(-3),
                    userProfile.getUserOid());
                List<BuyerStoreHolder> oldSelectedStoresList = null;
                if(allStoresList == null)
                {
                    oldSelectedStoresList = buyerStoreService
                        .selectBuyerStoresByUserOidAndIsWareHouse(userProfile
                            .getUserOid(), false);
                    if(oldSelectedStoresList == null || oldSelectedStoresList.isEmpty())
                    {
                        oldSelectedStoresList = new ArrayList<BuyerStoreHolder>();
                    }
                    else
                    {
                        this.formatStoreNameAndStoreCode(oldSelectedStoresList);
                    }
                }
                else
                {//all stores
                    oldSelectedStoresList = new ArrayList<BuyerStoreHolder>();
                    buyerStore = new BuyerStoreHolder();
                    buyerStore.setStoreOid(new BigDecimal(-3));
                    buyerStore.setStoreName(ALL_STORES);
                    oldSelectedStoresList.add(buyerStore);
                }

                oldUserProfile.setOldSelectedStoresList(oldSelectedStoresList);

                //wareHouse
                BuyerStoreUserHolder allWareHouseList = buyerStoreUserService
                    .selectStoreUserByStoreOidAndUserOid(new BigDecimal(-4),
                        userProfile.getUserOid());
                List<BuyerStoreHolder> oldSelectedWareHouseList = null;
                if(allWareHouseList == null)
                {
                  //warehouse with not all
                    oldSelectedWareHouseList = buyerStoreService
                        .selectBuyerStoresByUserOidAndIsWareHouse(userProfile
                            .getUserOid(), true);
                    
                    if(oldSelectedWareHouseList == null || oldSelectedWareHouseList.isEmpty())
                    {
                        oldSelectedWareHouseList = new ArrayList<BuyerStoreHolder>();
                    }
                    else
                    {
                        this.formatStoreNameAndStoreCode(oldSelectedWareHouseList);
                    }
                }
                else
                {//warehouse is selected all
                    oldSelectedWareHouseList = new ArrayList<BuyerStoreHolder>();
                    buyerStore = new BuyerStoreHolder();
                    buyerStore.setStoreOid(new BigDecimal(-3));
                    buyerStore.setStoreName(ALL_WARE_HOUSE);
                    oldSelectedWareHouseList.add(buyerStore);
                }

                oldUserProfile.setOldSelectedWareHouseList(oldSelectedWareHouseList);
                
                List<BuyerStoreAreaHolder> oldSelectedAreasList = null;
                List<BuyerStoreAreaUserHolder> areaList = buyerStoreAreaUserService.selectAreaUserByUserOid(userProfile.getUserOid());
                if(areaList != null && !areaList.isEmpty()
                    && areaList.size() == 1
                    && areaList.get(0).getAreaOid().intValue() == -2)
                {
                    oldSelectedAreasList = new ArrayList<BuyerStoreAreaHolder>();
                    BuyerStoreAreaHolder storeArea = new BuyerStoreAreaHolder();
                    storeArea.setAreaName(ALL_AREAS);
                    storeArea.setAreaCode(ALL_AREAS);
                    oldSelectedAreasList.add(0, storeArea);
                }
                else
                {
                    oldSelectedAreasList = buyerStoreAreaService
                        .selectBuyerStoreAreaByUserOid(userProfile.getUserOid());
                }
                
                oldUserProfile.setOldSelectedAreasList(oldSelectedAreasList);
                
                UserClassHolder allClass = userClassService.selectByUserOidAndClassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
                List<ClassExHolder> selectedClassList = null;
                if (allClass == null)
                {
                    selectedClassList = classService.selectClassByUserOid(userProfile.getUserOid());
                    if (selectedClassList == null) selectedClassList = new ArrayList<ClassExHolder>();
                }
                else
                {
                    selectedClassList = new ArrayList<ClassExHolder>();
                    ClassExHolder class_ = new ClassExHolder();
                    class_.setClassOid(BigDecimal.valueOf(-1));
                    class_.setClassCode(getText("user.all.class.selected"));
                    selectedClassList.add(class_);
                }
                
                oldUserProfile.setOldSelectedClassList(selectedClassList);
                
                UserSubclassHolder allSubclass = userSubclassService.selectByUserOidAndSubclassOid(userProfile.getUserOid(), BigDecimal.valueOf(-1));
                List<SubclassExHolder> selectedSubclassList = null;
                if (allSubclass == null)
                {
                    selectedSubclassList = subclassService.selectSubclassExByUserOid(userProfile.getUserOid());
                    if (selectedSubclassList == null) selectedSubclassList = new ArrayList<SubclassExHolder>();
                }
                else
                {
                    selectedSubclassList = new ArrayList<SubclassExHolder>();
                    SubclassExHolder subClass = new SubclassExHolder();
                    subClass.setSubclassOid(BigDecimal.valueOf(-1));
                    subClass.setSubclassCode(getText("user.all.subclass.selected"));
                    selectedSubclassList.add(subClass);
                }
                
                oldUserProfile.setOldSelectedSubclassList(selectedSubclassList);
                
                if (MkCtrlStatus.PENDING.equals(userProfile.getCtrlStatus())
                        && DbActionType.UPDATE.equals(userProfile
                                .getActionType()))
                {
                    userProfile.setOldVersion(oldUserProfile);
                    this.mkUpdateUserProfileList.add(userProfile);
                    continue;
                }
            }

            String reqUrl = StringUtils.remove(this.getRequest()
                .getRequestURI(), this.getRequest().getContextPath());
            
            if ("/user/initWithdraw.action".equals(reqUrl))
            {
                confirmType = "W";
            }
            else if ("/user/initReject.action".equals(reqUrl))
            {
                confirmType = "R";
            }
            else if ("/user/initApprove.action".equals(reqUrl))
            {
                confirmType = "A";
            }
            
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    // ***************************************
    // Approve function
    // ***************************************
    
    public String saveApprove()
    {
        try
        {
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids || "".equals(selectedOids))
            {
                msg.saveError(this.getText(ONE_RECORD_TO_OPERATE_CODE));
                msg.addMessageTarget(mt);
                log.info(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                return FORWARD_COMMON_MESSAGE;
            }
            
            this.getSession().remove(SESSION_OID_PARAMETER);
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            List<UserProfileTmpHolder> users = new ArrayList<UserProfileTmpHolder>();
            for (String part : parts)
            {
                BigDecimal userOid = new BigDecimal(part);
                
                UserProfileTmpHolder oldUser = userProfileTmpService.selectUserProfileTmpByKey(userOid);
                
                if (!MkCtrlStatus.PENDING.equals(oldUser.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPU0149", new String[]{oldUser.getLoginId()}));
                    continue;
                }
                if (oldUser.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser())
                        || oldUser.getUserOid().equals(this.getProfileOfCurrentUser().getUserOid()))
                {
                    msg.saveError(this.getText("B2BPU0159", new String[]{oldUser.getLoginId()}));
                    continue;
                }
                
                oldUser.setGroupUsers(new ArrayList<GroupUserHolder>());
                oldUser.setRoleUsers(new ArrayList<RoleUserHolder>());
                oldUser.setAllowedBuyerList(new ArrayList<AllowedAccessCompanyHolder>());
                oldUser.setBuyerStoreUsers(new ArrayList<BuyerStoreUserHolder>());
                oldUser.setBuyerStoreAreaUsers(new ArrayList<BuyerStoreAreaUserHolder>());
                oldUser.setUserClassList(new ArrayList<UserClassHolder>());
                oldUser.setUserSubclassList(new ArrayList<UserSubclassHolder>());
                
                List<GroupUserTmpHolder> oldGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(oldUser.getUserOid());
                BigDecimal companyOid = this.getCompanyOid(oldUser);
                String groupName = checkGroupCompanySameToUser(oldGroupUserList, companyOid, oldUser.getUserType());
                
                if (groupName != null)
                {
                    msg.saveError(this.getText("B2BPU0169", new String[]{groupName, oldUser.getLoginId()}));
                    continue;
                }
                
                for (GroupUserTmpHolder oldGroupUser : oldGroupUserList)
                    oldUser.getGroupUsers().add(oldGroupUser);

                List<RoleUserTmpHolder> oldRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(oldUser.getUserOid());
                for (RoleUserTmpHolder oldRoleUser : oldRoleUserList)
                    oldUser.getRoleUsers().add(oldRoleUser);
                
                List<AllowedAccessCompanyTmpHolder> oldAllowedAccessCompanyList = allowedAccessCompanyTmpService.selectByUserOid(oldUser.getUserOid());
                for (AllowedAccessCompanyTmpHolder oldAllowedAccessCompany : oldAllowedAccessCompanyList)
                {
                    oldUser.getAllowedBuyerList().add(oldAllowedAccessCompany);
                }
                
                List<BuyerStoreAreaUserTmpHolder> oldAreaUserList = buyerStoreAreaUserTmpService.selectAreaUserTmpByUserOid(oldUser.getUserOid());
                for (BuyerStoreAreaUserTmpHolder oldAreaUser : oldAreaUserList)
                    oldUser.getBuyerStoreAreaUsers().add(oldAreaUser);
                
                List<BuyerStoreUserTmpHolder> oldStoreUserList = buyerStoreUserTmpService.selectStoreUserTmpByUserOid(oldUser.getUserOid());
                for (BuyerStoreUserTmpHolder oldStoreUser : oldStoreUserList)
                    oldUser.getBuyerStoreUsers().add(oldStoreUser);
                
                List<UserClassTmpHolder> oldUserClassList = userClassTmpService.selectUserClassTmpByUserOid(oldUser.getUserOid());
                for (UserClassTmpHolder oldUserClass : oldUserClassList)
                    oldUser.getUserClassList().add(oldUserClass);
                
                List<UserSubclassTmpHolder> oldUserSubclassList = userSubclassTmpService.selectUserSubclassTmpByUserOid(userOid);
                for (UserSubclassTmpHolder oldUserSubclass : oldUserSubclassList)
                    oldUser.getUserSubclassList().add(oldUserSubclass);
                
                if (DbActionType.CREATE.equals(oldUser.getActionType()))
                {
                    users.add(oldUser);
                }
                
                
                userProfileService.approveUserProfile(
                    this.getCommonParameter(), appConfig.getServerUrl() + "/user/", this
                        .getRequest().getRemoteAddr(), oldUser);
                msg.saveSuccess(this.getText("B2BPU0155", new String[]{oldUser.getLoginId()}));
                
                log.info(this.getText(
                    "B2BPU0152",
                    new String[] {oldUser.getLoginId(),
                        this.getLoginIdOfCurrentUser()}));
              
            }
            if (users != null && !users.isEmpty())
            {
                emailSendService.sendSetPasswordEmailByCallable(this
                    .getCommonParameter(),  appConfig.getServerUrl() + "/user/", this.getRequest()
                    .getRemoteAddr(), users);
            }
            msg.addMessageTarget(mt);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // ***************************************
    // Reject function
    // ***************************************
    
    public String saveReject()
    {
        try
        {
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids || "".equals(selectedOids))
            {
                msg.saveError(this.getText(ONE_RECORD_TO_OPERATE_CODE));
                msg.addMessageTarget(mt);
                log.info(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                return FORWARD_COMMON_MESSAGE;
            }
            
            this.getSession().remove(SESSION_OID_PARAMETER);
            
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal userOid = new BigDecimal(part);
                
                UserProfileTmpHolder oldUser = userProfileTmpService.selectUserProfileTmpByKey(userOid);
                
                if (!MkCtrlStatus.PENDING.equals(oldUser.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPU0150", new String[]{oldUser.getLoginId()}));
                    continue;
                }
                if (oldUser.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser()) 
                        || oldUser.getUserOid().equals(this.getProfileOfCurrentUser().getUserOid()))
                {
                    msg.saveError(this.getText("B2BPU0158", new String[]{oldUser.getLoginId()}));
                    continue;
                }
                
                oldUser.setGroupUsers(new ArrayList<GroupUserHolder>());
                oldUser.setRoleUsers(new ArrayList<RoleUserHolder>());
                oldUser.setBuyerStoreUsers(new ArrayList<BuyerStoreUserHolder>());
                oldUser.setBuyerStoreAreaUsers(new ArrayList<BuyerStoreAreaUserHolder>());
                oldUser.setAllowedBuyerList(new ArrayList<AllowedAccessCompanyHolder>());
                oldUser.setUserClassList(new ArrayList<UserClassHolder>());
                oldUser.setUserSubclassList(new ArrayList<UserSubclassHolder>());
                
                List<GroupUserTmpHolder> oldGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(oldUser.getUserOid());
                for (GroupUserTmpHolder oldGroupUser : oldGroupUserList)
                    oldUser.getGroupUsers().add(oldGroupUser);

                List<RoleUserTmpHolder> oldRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(oldUser.getUserOid());
                for (RoleUserTmpHolder oldRoleUser : oldRoleUserList)
                    oldUser.getRoleUsers().add(oldRoleUser);
                
                List<AllowedAccessCompanyTmpHolder> oldAllowedAccessCompanyList = allowedAccessCompanyTmpService.selectByUserOid(oldUser.getUserOid());
                for (AllowedAccessCompanyTmpHolder oldAllowedAccessCompany : oldAllowedAccessCompanyList)
                {
                    oldUser.getAllowedBuyerList().add(oldAllowedAccessCompany);
                }

                List<BuyerStoreAreaUserTmpHolder> oldAreaUserList = buyerStoreAreaUserTmpService.selectAreaUserTmpByUserOid(oldUser.getUserOid());
                for (BuyerStoreAreaUserTmpHolder oldAreaUser : oldAreaUserList)
                    oldUser.getBuyerStoreAreaUsers().add(oldAreaUser);
                
                List<BuyerStoreUserTmpHolder> oldStoreUserList = buyerStoreUserTmpService.selectStoreUserTmpByUserOid(oldUser.getUserOid());
                for (BuyerStoreUserTmpHolder oldStoreUser : oldStoreUserList)
                    oldUser.getBuyerStoreUsers().add(oldStoreUser);
                
                List<UserClassTmpHolder> oldUserClassList = userClassTmpService.selectUserClassTmpByUserOid(oldUser.getUserOid());
                for (UserClassTmpHolder oldUserClass : oldUserClassList)
                    oldUser.getUserClassList().add(oldUserClass);
                
                List<UserSubclassTmpHolder> oldUserSubclassList = userSubclassTmpService.selectUserSubclassTmpByUserOid(oldUser.getUserOid());
                for (UserSubclassTmpHolder oldUserSubclass : oldUserSubclassList)
                    oldUser.getUserSubclassList().add(oldUserSubclass);
                
                userProfileService.rejectUserProfile(this.getCommonParameter(), oldUser);
                msg.saveSuccess(this.getText("B2BPU0156", new String[]{oldUser.getLoginId()}));
                
                log.info(this.getText(
                    "B2BPU0153",
                    new String[] {oldUser.getLoginId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            
            msg.addMessageTarget(mt);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // ***************************************
    // Withdraw function
    // ***************************************
    
    public String saveWithdraw()
    {
        try
        {
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids || "".equals(selectedOids))
            {
                msg.saveError(this.getText(ONE_RECORD_TO_OPERATE_CODE));
                msg.addMessageTarget(mt);
                log.info(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                return FORWARD_COMMON_MESSAGE;
            }
                        
            this.getSession().remove(SESSION_OID_PARAMETER);
            
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal userOid = new BigDecimal(part);
                
                UserProfileTmpHolder oldUser = userProfileTmpService.selectUserProfileTmpByKey(userOid);
                
                if (!MkCtrlStatus.PENDING.equals(oldUser.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPU0151", new String[]{oldUser.getLoginId()}));
                    continue;
                }
                if (!oldUser.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser()))
                {
                    msg.saveError(this.getText("B2BPU0116", new String[]{oldUser.getLoginId()}));
                    continue;
                }
                
                oldUser.setGroupUsers(new ArrayList<GroupUserHolder>());
                oldUser.setRoleUsers(new ArrayList<RoleUserHolder>());
                oldUser.setBuyerStoreUsers(new ArrayList<BuyerStoreUserHolder>());
                oldUser.setBuyerStoreAreaUsers(new ArrayList<BuyerStoreAreaUserHolder>());
                oldUser.setAllowedBuyerList(new ArrayList<AllowedAccessCompanyHolder>());
                oldUser.setUserClassList(new ArrayList<UserClassHolder>());
                oldUser.setUserSubclassList(new ArrayList<UserSubclassHolder>());
                
                List<GroupUserTmpHolder> oldGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(oldUser.getUserOid());
                for (GroupUserTmpHolder oldGroupUser : oldGroupUserList)
                    oldUser.getGroupUsers().add( oldGroupUser);

                List<RoleUserTmpHolder> oldRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(oldUser.getUserOid());
                for (RoleUserTmpHolder oldRoleUser : oldRoleUserList)
                    oldUser.getRoleUsers().add(oldRoleUser);
                
                List<AllowedAccessCompanyTmpHolder> oldAllowedAccessCompanyList = allowedAccessCompanyTmpService.selectByUserOid(oldUser.getUserOid());
                for (AllowedAccessCompanyTmpHolder oldAllowedAccessCompany : oldAllowedAccessCompanyList)
                    oldUser.getAllowedBuyerList().add(oldAllowedAccessCompany);

                List<BuyerStoreAreaUserTmpHolder> oldAreaUserList = buyerStoreAreaUserTmpService.selectAreaUserTmpByUserOid(oldUser.getUserOid());
                for (BuyerStoreAreaUserTmpHolder oldAreaUser : oldAreaUserList)
                    oldUser.getBuyerStoreAreaUsers().add(oldAreaUser);
                
                List<BuyerStoreUserTmpHolder> oldStoreUserList = buyerStoreUserTmpService.selectStoreUserTmpByUserOid(oldUser.getUserOid());
                for (BuyerStoreUserTmpHolder oldStoreUser : oldStoreUserList)
                    oldUser.getBuyerStoreUsers().add(oldStoreUser);
                
                List<UserClassTmpHolder> oldUserClassList = userClassTmpService.selectUserClassTmpByUserOid(oldUser.getUserOid());
                if (oldUserClassList != null)
                {
                    for (UserClassTmpHolder oldUserClass : oldUserClassList)
                        oldUser.getUserClassList().add(oldUserClass);
                }
                
                List<UserSubclassTmpHolder> oldUserSubclassList = userSubclassTmpService.selectUserSubclassTmpByUserOid(oldUser.getUserOid());
                if (oldUserSubclassList != null)
                {
                    for (UserSubclassTmpHolder oldUserSubclass : oldUserSubclassList)
                        oldUser.getUserSubclassList().add(oldUserSubclass);
                }
                
                userProfileService.withdrawUserProfile(this.getCommonParameter(), oldUser);
                msg.saveSuccess(this.getText("B2BPU0157", new String[]{oldUser.getLoginId()}));
                
                log.info(this.getText(
                    "B2BPU0154",
                    new String[] {oldUser.getLoginId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            
            msg.addMessageTarget(mt);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // ***************************************
    // Delete function
    // ***************************************
    
    public String saveDelete()
    {
        try
        {
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids || "".equals(selectedOids))
            {
                msg.saveError(this.getText(ONE_RECORD_TO_OPERATE_CODE));
                msg.addMessageTarget(mt);
                log.info(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                return FORWARD_COMMON_MESSAGE;
            }
            
            this.getSession().remove(SESSION_OID_PARAMETER);
            
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal userOid = new BigDecimal(part);
                
                UserProfileTmpHolder oldUser = userProfileTmpService.selectUserProfileTmpByKey(userOid);
                
                if (MkCtrlStatus.PENDING.equals(oldUser.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPU0114", new String[]{oldUser.getLoginId()}));
                    continue;
                }
                
                if ( null != userSessionService.selectByUserOid(oldUser.getUserOid()))
                {
                    msg.saveError(this.getText("B2BPU0113", new String[]{oldUser.getLoginId()}));
                    continue;
                }
                
                oldUser.setGroupUsers(new ArrayList<GroupUserHolder>());
                oldUser.setRoleUsers(new ArrayList<RoleUserHolder>());
                oldUser.setBuyerStoreUsers(new ArrayList<BuyerStoreUserHolder>());
                oldUser.setBuyerStoreAreaUsers(new ArrayList<BuyerStoreAreaUserHolder>());
                oldUser.setAllowedBuyerList(new ArrayList<AllowedAccessCompanyHolder>());
                oldUser.setUserClassList(new ArrayList<UserClassHolder>());
                oldUser.setUserSubclassList(new ArrayList<UserSubclassHolder>());
                
                List<GroupUserTmpHolder> oldGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(oldUser.getUserOid());
                
                if (oldGroupUserList != null && !oldGroupUserList.isEmpty())
                {
                    GroupUserTmpHolder gut = (GroupUserTmpHolder) oldGroupUserList.get(0);
                    if (!gut.getApproved() && gut.getLastUpdateFrom().equals(LastUpdateFrom.GROUP))
                    {
                        msg.saveError(this.getText("B2BPU0168", new String[]{oldUser.getLoginId()}));
                        continue;
                    }
                    for (Object oldGroupUser : oldGroupUserList)
                        oldUser.getGroupUsers().add((GroupUserHolder) oldGroupUser);
                }
                   
                List<RoleUserTmpHolder> oldRoleUserList = roleUserTmpService.selectRoleUserTmpByUserOid(oldUser.getUserOid());
                for (RoleUserTmpHolder oldRoleUser : oldRoleUserList)
                    oldUser.getRoleUsers().add(oldRoleUser);
                
                List<AllowedAccessCompanyTmpHolder> oldAllowedAccessCompanyList = allowedAccessCompanyTmpService.selectByUserOid(oldUser.getUserOid());
                for (AllowedAccessCompanyTmpHolder oldAllowedAccessCompany : oldAllowedAccessCompanyList)
                    oldUser.getAllowedBuyerList().add(oldAllowedAccessCompany);

                List<BuyerStoreAreaUserTmpHolder> oldAreaUserList = buyerStoreAreaUserTmpService.selectAreaUserTmpByUserOid(oldUser.getUserOid());
                for (BuyerStoreAreaUserTmpHolder oldAreaUser : oldAreaUserList)
                    oldUser.getBuyerStoreAreaUsers().add(oldAreaUser);
                
                List<BuyerStoreUserTmpHolder> oldStoreUserList = buyerStoreUserTmpService.selectStoreUserTmpByUserOid(oldUser.getUserOid());
                for (BuyerStoreUserTmpHolder oldStoreUser : oldStoreUserList)
                    oldUser.getBuyerStoreUsers().add(oldStoreUser);
                
                List<UserClassTmpHolder> oldUserClassList = userClassTmpService.selectUserClassTmpByUserOid(oldUser.getUserOid());
                if (oldUserClassList != null)
                {
                    for (UserClassHolder oldUserClass : oldUserClassList)
                        oldUser.getUserClassList().add(oldUserClass);
                }
                
                List<UserSubclassTmpHolder> oldUserSubclassList = userSubclassTmpService.selectUserSubclassTmpByUserOid(oldUser.getUserOid());
                if (oldUserSubclassList != null)
                {
                    for (UserSubclassHolder oldUserSubclass : oldUserSubclassList)
                        oldUser.getUserSubclassList().add(oldUserSubclass);
                }
                
                userProfileService.removeUserProfile(this.getCommonParameter(), oldUser, false);
                
                if (this.getCommonParameter().getMkMode())
                {
                    log.info(this.getText(
                            "B2BPU0148",
                            new String[] { oldUser.getLoginId(),
                                    this.getLoginIdOfCurrentUser() }));

                    msg.saveSuccess(this.getText(
                            "B2BPU0148",
                            new String[] { oldUser.getLoginId(),
                                    this.getLoginIdOfCurrentUser() }));
                }
                else
                {
                    log.info(this.getText(
                            "B2BPU0147",
                            new String[] { oldUser.getLoginId(),
                                    this.getLoginIdOfCurrentUser() }));
                    msg.saveSuccess(this.getText(
                            "B2BPU0147",
                            new String[] { oldUser.getLoginId(),
                                    this.getLoginIdOfCurrentUser() }));
                }
            }
            
            msg.addMessageTarget(mt);
            
        }
        catch (Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // ***************************************
    // Private methods
    // ***************************************

    private void parseGroupUserForUpdateUser(List<GroupUserTmpHolder> groupUsers) throws Exception
    {
        if (groupUsers.size() == 1)
        {
            GroupUserTmpHolder gu = (GroupUserTmpHolder) groupUsers.get(0);
            GroupHolder group = groupTmpService.selectGroupTmpByKey(gu.getGroupOid());
            userProfile.setGroupOid(group.getGroupOid());
            if (gu.getApproved())
            {
                userProfile.setIsLocked(false);
                isAddToGroup = true;
            }
            else if (gu.getLastUpdateFrom().equals(LastUpdateFrom.GROUP))
            {
                userProfile.setIsLocked(true);
                isAddToGroup = true;
                
                groupList = new ArrayList<GroupHolder>();
                groupList.add(group);
            }
            else if (gu.getActionType().equals(DbActionType.CREATE))
            {
                userProfile.setIsLocked(false);
                isAddToGroup = true;
            }
            else if (gu.getActionType().equals(DbActionType.DELETE))
            {
                userProfile.setIsLocked(false);
                isAddToGroup = false;
            }
        }
        else if (groupUsers.size() == 2)
        {
            userProfile.setIsLocked(false);
            for (Object o : groupUsers)
            {
                GroupUserTmpHolder gu = (GroupUserTmpHolder) o;
                if (gu.getActionType().equals(DbActionType.CREATE))
                {
                    GroupHolder group = groupService.selectGroupByKey(gu.getGroupOid());
                    userProfile.setGroupOid(group.getGroupOid());
                    isAddToGroup = true;
                }
            }
        }
    }
    
    
    private void initGroupForViewUser(UserProfileTmpExHolder userProfile) throws Exception
    {
        List<GroupUserTmpHolder> oldGroupUserList = groupUserTmpService.selectGroupUserTmpByUserOid(userProfile.getUserOid());
    
        if (oldGroupUserList != null && !oldGroupUserList.isEmpty())
        {
            for (Object o : oldGroupUserList)
            {
                GroupUserTmpHolder gut = (GroupUserTmpHolder) o;
                if (gut.getActionType().equals(DbActionType.CREATE))
                {
                    GroupTmpHolder group = groupTmpService.selectGroupTmpByKey(gut.getGroupOid());
                    if (group != null)
                        userProfile.setGroupName(group.getGroupName());
                }
            }
        }
    }
    
    
    private boolean validateMkOperate(Object selectedOids) throws Exception
    {
        if (null == selectedOids || "".equals(selectedOids))
        {
            mkErrorInfo = SESSION_PARAMETER_OID_NOT_FOUND_MSG;
            return false;
        }
        String[] parts = selectedOids.toString().split(
                REQUEST_OID_DELIMITER);
        for (String part : parts)
        {
            BigDecimal userOid = new BigDecimal(part);

            UserProfileTmpHolder oldUser = userProfileTmpService
                    .selectUserProfileTmpByKey(userOid);

            if (!MkCtrlStatus.PENDING.equals(oldUser.getCtrlStatus()))
            {
                mkErrorInfo = this.getText("B2BPU0167");
                return false;
            }
        }
        return true;
    }
    
    
    private CommonParameterHolder getMyCommonParameter(UserProfileHolder user)
    {
        CommonParameterHolder commonParam = new CommonParameterHolder();
        if (user == null)
        {
            commonParam.setLoginId("SYSTEM");
            commonParam.setClientIp(this.getRequest().getRemoteAddr());
        }
        else
        {
            commonParam.setLoginId(user.getLoginId());
            commonParam.setCurrentUserOid(user.getUserOid());
            commonParam.setClientIp(this.getRequest().getRemoteAddr());
        }
        
        return commonParam;
    }
    
    
    private void parseCompanyInfoForUser()
    {
        if (userProfile.getUserType().equals(BigDecimal.valueOf(4)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)) 
                    || this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(4)))
            {
                userProfile.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            userProfile.setSupplierOid(null);
        }
        if (userProfile.getUserType().equals(BigDecimal.valueOf(7)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(6)) 
                    || this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(7)))
            {
                userProfile.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            userProfile.setSupplierOid(null);
        }
        else if (userProfile.getUserType().equals(BigDecimal.valueOf(5)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)) 
                    || this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(5)))
            {
                userProfile.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
            }
            userProfile.setBuyerOid(null);
        }
        else if (userProfile.getUserType().equals(BigDecimal.valueOf(2)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
            {
                userProfile.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            userProfile.setSupplierOid(null);
        }
        else if (userProfile.getUserType().equals(BigDecimal.valueOf(6)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(6)))
            {
                userProfile.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            userProfile.setSupplierOid(null);
        }
        else if (userProfile.getUserType().equals(BigDecimal.valueOf(3)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
            {
                userProfile.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
            }
            userProfile.setBuyerOid(null);
        }
        else if (userProfile.getUserType().equals(BigDecimal.ONE))
        {
            userProfile.setBuyerOid(null);
            userProfile.setSupplierOid(null);
        }
        if (!userProfile.getActive())
        {
            userProfile.setBlocked(false);
            userProfile.setInactivateBy(this.getLoginIdOfCurrentUser());
            userProfile.setInactivateDate(new Date());
        }
        if (userProfile.getBlocked())
        {
            userProfile.setBlockBy(this.getLoginIdOfCurrentUser());
            userProfile.setBlockDate(new Date());
        }
    }
    
    
    private List<Object> convertSelectedRole(List<? extends Object> selectedRolesList)
    {
        List<Object> rlt = new ArrayList<Object>();
        for (Object obj : selectedRolesList)
        {
            RoleHolder role = (RoleHolder) obj;
            rlt.add(role.getRoleOid());
        }
        return rlt;
    }
    
    
    private List<Object> convertSelectedBuyer(List<? extends Object> selectedBuyersList)
    {
        List<Object> rlt = new ArrayList<Object>();
        for (Object obj : selectedBuyersList)
        {
            BuyerHolder buyer = (BuyerHolder) obj;
            rlt.add(buyer.getBuyerOid());
        }
        return rlt;
    }
    
    
    private List<Object> convertSelectedStore(List<? extends Object> selectedStoresList)
    {
        List<Object> rlt = new ArrayList<Object>();
        for (Object obj : selectedStoresList)
        {
            BuyerStoreHolder store = (BuyerStoreHolder) obj;
            store.setStoreCode( LEFT_BRACKET + store.getStoreCode() + RIGHT_BRACKET);
            if (store.getStoreName() == null)
            {
                store.setStoreName("");
            }
            rlt.add(store.getStoreOid());
        }
        return rlt;
    }
    
    
    private List<Object> convertSelectedArea(List<? extends Object> selectedAreasList)
    {
        List<Object> rlt = new ArrayList<Object>();
        for (Object obj : selectedAreasList)
        {
            BuyerStoreAreaHolder area = (BuyerStoreAreaHolder) obj;
            rlt.add(area.getAreaOid());
        }
        return rlt;
    }
    
    
    private List<Object> convertSelectedClass(List<? extends Object> selectedClassList)
    {
        List<Object> rlt = new ArrayList<Object>();
        for (Object obj : selectedClassList)
        {
            ClassHolder class_ = (ClassHolder) obj;
            rlt.add(class_.getClassOid());
        }
        return rlt;
    }
    
    
    private List<Object> convertSelectedSubclass(List<? extends Object> selectedSubclassList)
    {
        List<Object> rlt = new ArrayList<Object>();
        for (Object obj : selectedSubclassList)
        {
            SubclassHolder subclass = (SubclassHolder) obj;
            rlt.add(subclass.getSubclassOid());
        }
        return rlt;
    }
    
    
    private String initCompanyInfo(UserProfileHolder user) throws Exception
    {
        if (user.getBuyerOid() != null)
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(user.getBuyerOid());
            
            return buyer == null ? null : buyer.getBuyerName();
        }
        
        if (user.getSupplierOid() != null)
        {
            SupplierHolder supplier = supplierService.selectSupplierByKey(user.getSupplierOid());
            
            return supplier == null ? null : supplier.getSupplierName();
        }
        
        return null;
    }
    
    
    private void initSearchCondition() throws Exception
    {
        ctrlStatusMap = MkCtrlStatus.toMapValue();
        // UserProfileHolder user = this.getLoginOfUser();
        // user.getUserType();
        userTypeList = userTypeService
                .selectPrivilegedSubUserTypesByUserTypeInclusively(this.getUserTypeOfCurrentUser());

        if (userTypeList == null) userTypeList = new ArrayList<UserTypeHolder>();
    }

    
    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
        
        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        
        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
        
        msg.addMessageTarget(mt);
    }

    
    private void initStoreAreaByConditionFromUserType(UserTypeHolder ut)
            throws Exception
    {
        BuyerHolder buyer = null;
        if (ut.getUserTypeOid().equals(BigDecimal.valueOf(2)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
            {
                buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else
            {
                if (buyerList == null || buyerList.isEmpty()) return;
                buyer = (BuyerHolder) buyerList.get(0);
            }
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(6)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(6)))
            {
                buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else
            {
                if (buyerList == null || buyerList.isEmpty()) return;
                buyer = (BuyerHolder) buyerList.get(0);
            }
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(4)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)) 
                    || this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(4)))
            {
                buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else
            {
                if (buyerList == null || buyerList.isEmpty()) return;
                buyer = (BuyerHolder) buyerList.get(0);
            }
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(7)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(6)) 
                    || this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(7)))
            {
                buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else
            {
                if (buyerList == null || buyerList.isEmpty()) return;
                buyer = (BuyerHolder) buyerList.get(0);
            }
        }

        if (buyer != null)
        {
            if (this.isBuyer(getUserTypeOfCurrentUser()) || this.isStore(getUserTypeOfCurrentUser()))
            {
                availabelStoresList = buyerStoreService.selectBuyerStoresByUserOidAndIsWareHouse(
                        this.getProfileOfCurrentUser().getUserOid(), false);
            }
            else
            {
                availabelStoresList = buyerStoreService.selectBuyerStoresByBuyerAndIsWareHouse(buyer.getBuyerCode(), false);
            }
            
            
            if (this.isBuyer(getUserTypeOfCurrentUser()) || this.isStore(getUserTypeOfCurrentUser()))
            {
                availabelWareHouseList = buyerStoreService.selectBuyerStoresByUserOidAndIsWareHouse(
                        this.getProfileOfCurrentUser().getUserOid(), true);
            }
            else
            {
                availabelWareHouseList = buyerStoreService.selectBuyerStoresByBuyerAndIsWareHouse(buyer.getBuyerCode(), true);
            }
            
            
            
            if (this.isBuyer(getUserTypeOfCurrentUser()) || this.isStore(getUserTypeOfCurrentUser()))
            {
                availabelAreasList = buyerStoreAreaService.selectBuyerStoreAreaByUserOid(
                        this.getProfileOfCurrentUser().getUserOid());
            }
            else
            {
                availabelAreasList = buyerStoreAreaService.selectBuyerStoreAreaByBuyer(buyer.getBuyerCode());
            }
            
        }
        
        
        if (availabelStoresList != null && !availabelStoresList.isEmpty())
        {
            this.formatStoreNameAndStoreCode(availabelStoresList);
//            for (Object obj : availabelStoresList)
//            {
//                BuyerStoreHolder store = (BuyerStoreHolder) obj;
//                store.setStoreCode(LEFT_BRACKET + store.getStoreCode() + RIGHT_BRACKET);
//                if (store.getStoreName() == null)
//                {
//                    store.setStoreName("");
//                }
//            }
        }
        
        if (availabelWareHouseList != null && !availabelWareHouseList.isEmpty())
        {
            this.formatStoreNameAndStoreCode(availabelWareHouseList);
//            for (Object obj : availabelWareHouseList)
//            {
//                BuyerStoreHolder store = (BuyerStoreHolder) obj;
//                store.setStoreCode( LEFT_BRACKET + store.getStoreCode() + RIGHT_BRACKET);
//                if (store.getStoreName() == null)
//                {
//                    store.setStoreName("");
//                }
//            }
        }
    }
    
    private void formatStoreNameAndStoreCode(List<? extends Object> objList)
    {
        for (Object obj : objList)
        {
            BuyerStoreHolder store = (BuyerStoreHolder) obj;
            store.setStoreCode( LEFT_BRACKET + store.getStoreCode() + RIGHT_BRACKET);
            if (store.getStoreName() == null)
            {
                store.setStoreName("");
            }
        }
    }
    
    
    private void initClassAndSubclassFromUserType(UserTypeHolder ut)
            throws Exception
    {
        BuyerHolder buyer = null;
        if (ut.getUserTypeOid().equals(BigDecimal.valueOf(2)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
            {
                buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else
            {
                if (buyerList == null || buyerList.isEmpty()) return;
                buyer = (BuyerHolder) buyerList.get(0);
            }
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(4)))
        {
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)) 
                    || this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(4)))
            {
                buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else
            {
                if (buyerList == null || buyerList.isEmpty()) return;
                buyer = (BuyerHolder) buyerList.get(0);
            }
        }
        
        if (buyer != null)
        {
            List<ClassExHolder> classList = null;
            List<SubclassExHolder> subclassList = null;
            
            if (this.isBuyer(getUserTypeOfCurrentUser()))
            {
                List<BigDecimal> buyerOids = new ArrayList<BigDecimal>();
                buyerOids.add(this.getProfileOfCurrentUser().getBuyerOid());
                classList = filterClassByBuyerOids(classService.selectClassByUserOid(this.getProfileOfCurrentUser().getUserOid()), buyerOids);
                subclassList = filterSubclassByBuyerOids(subclassService.selectSubclassExByUserOid(this.getProfileOfCurrentUser().getUserOid()), buyerOids);
                
            }
            else
            {
                classList = classService.selectClassByBuyerOid(buyer.getBuyerOid());
                subclassList = subclassService.selectSubclassExByBuyerOid(buyer.getBuyerOid());
            }
            
            
            availableClassList = classList;
            availableSubclassList = subclassList;
        }
    }
    
    
    public static List<ClassExHolder> filterClassByBuyerOids(List<ClassExHolder> classList, List<BigDecimal> buyerOids)
    {
        List<ClassExHolder> result = new ArrayList<ClassExHolder>();
        if (classList == null || classList.isEmpty())
        {
            return result;
        }
        for (ClassExHolder _class : classList)
        {
            if (buyerOids.contains(_class.getBuyerOid()))
            {
                result.add(_class);
            }
        }
        
        return result;
    }
    
    
    public static List<SubclassExHolder> filterSubclassByBuyerOids(List<SubclassExHolder> subclassList, List<BigDecimal> buyerOids)
    {
        List<SubclassExHolder> result = new ArrayList<SubclassExHolder>();
        if (subclassList == null || subclassList.isEmpty())
        {
            return result;
        }
        for (SubclassExHolder subclass : subclassList)
        {
            if (buyerOids.contains(subclass.getBuyerOid()))
            {
                result.add(subclass);
            }
        }
        
        return result;
    }
    
    
    private void initAvailableStoreAndAreaFromUserProfile() throws Exception
    {
        if (isBuyer(userProfile.getUserType()) || isStore(userProfile.getUserType()))
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(userProfile.getBuyerOid());
            if (isSysAdmin(getUserTypeOfCurrentUser()))
            {
                availabelStoresList = buyerStoreService.selectBuyerStoresByBuyerAndIsWareHouse(buyer.getBuyerCode(), false);
                availabelWareHouseList = buyerStoreService.selectBuyerStoresByBuyerAndIsWareHouse(buyer.getBuyerCode(), true);
                availabelAreasList = buyerStoreAreaService.selectBuyerStoreAreaByBuyer(buyer.getBuyerCode());
            }
            else
            {
                availabelStoresList = buyerStoreService.selectBuyerStoresByUserOidAndIsWareHouse(getProfileOfCurrentUser().getUserOid(), false);
                availabelWareHouseList = buyerStoreService.selectBuyerStoresByUserOidAndIsWareHouse(getProfileOfCurrentUser().getUserOid(), true);
                availabelAreasList = buyerStoreAreaService.selectBuyerStoreAreaByUserOid(getProfileOfCurrentUser().getUserOid());
            }
            
            if(availabelStoresList != null && !availabelStoresList.isEmpty())
            {
                this.formatStoreNameAndStoreCode(availabelStoresList);
            }
            
            if(availabelWareHouseList != null && !availabelWareHouseList.isEmpty())
            {
                this.formatStoreNameAndStoreCode(availabelWareHouseList);
            }
        }
    }
    
    
    private void initAvailableClassAndSubclassFromUserProfile(boolean initFlag) throws Exception
    {
        if (isBuyer(userProfile.getUserType()) || isStore(userProfile.getUserType()))
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(userProfile.getBuyerOid());
            
            List<ClassExHolder> classList = null;
            List<SubclassExHolder> subclassList = null;
            
            if (isSysAdmin(getUserTypeOfCurrentUser()))
            {
                classList = classService.selectClassByBuyerOid(buyer.getBuyerOid());
                subclassList = subclassService.selectSubclassExByBuyerOid(buyer.getBuyerOid());
            }
            else
            {
                classList = classService.selectClassByUserOid(getProfileOfCurrentUser().getUserOid());
                subclassList = subclassService.selectSubclassExByUserOid(getProfileOfCurrentUser().getUserOid());
            }
            
            List<BigDecimal> buyerOids = new ArrayList<BigDecimal>();
            buyerOids.add(buyer.getBuyerOid());
            
            if (initFlag)
            {
                List<BuyerHolder> allowedBuyers = allowedAccessCompanyTmpService.selectBuyerByUserOid(userProfile.getUserOid());
                
                if (allowedBuyers != null && !allowedBuyers.isEmpty())
                {
                    for (BuyerHolder allowedBuyer : allowedBuyers)
                    {
                        buyerOids.add(allowedBuyer.getBuyerOid());
                        if (isSysAdmin(getUserTypeOfCurrentUser()))
                        {
                            List<ClassExHolder> cList = classService.selectClassByBuyerOid(allowedBuyer.getBuyerOid());
                            if (cList != null && !cList.isEmpty())
                            {
                                classList.addAll(cList);
                            }
                            
                            List<SubclassExHolder> sList = subclassService.selectSubclassExByBuyerOid(allowedBuyer.getBuyerOid());
                            if (sList != null && !sList.isEmpty())
                            {
                                subclassList.addAll(sList);
                            }
                        }
                    }
                }
            }
            else
            {
                if (selectedBuyersList != null && !selectedBuyersList.isEmpty())
                {
                    for (Object o : selectedBuyersList)
                    {
                        buyerOids.add(new BigDecimal((String)o));
                        if (isSysAdmin(getUserTypeOfCurrentUser()))
                        {
                            List<ClassExHolder> cList = classService.selectClassByBuyerOid(new BigDecimal((String)o));
                            if (cList != null && !cList.isEmpty())
                            {
                                classList.addAll(cList);
                            }
                            
                            List<SubclassExHolder> sList = subclassService.selectSubclassExByBuyerOid(new BigDecimal((String)o));
                            if (sList != null && !sList.isEmpty())
                            {
                                subclassList.addAll(sList);
                            }
                        }
                    }
                }

            }
            
            availableClassList = filterClassByBuyerOids(classList, buyerOids);
            availableSubclassList = filterSubclassByBuyerOids(subclassList, buyerOids);
        }
    }
    
    
    private void initRolesByConditionFromUserType(UserTypeHolder ut) throws Exception
    {
        groupList = new ArrayList<GroupHolder>();
        
        BigDecimal buyerOid = null;
        BigDecimal supplierOid = null;
        if (ut.getUserTypeOid().equals(BigDecimal.valueOf(2)) || ut.getUserTypeOid().equals(BigDecimal.valueOf(4))
                ||ut.getUserTypeOid().equals(BigDecimal.valueOf(6)) || ut.getUserTypeOid().equals(BigDecimal.valueOf(7)))
        {
            if (buyerList != null && !buyerList.isEmpty())
            {
                buyerOid = ((BuyerHolder) buyerList.get(0)).getBuyerOid();
            }
            if (this.getProfileOfCurrentUser().getBuyerOid() != null)
            {
                buyerOid = this.getProfileOfCurrentUser().getBuyerOid();
            }
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(3)) || ut.getUserTypeOid().equals(BigDecimal.valueOf(5)))
        {
            if (supplierList != null && !supplierList.isEmpty())
            {
                supplierOid = ((SupplierHolder) supplierList.get(0)).getSupplierOid();
            }
            if (this.getProfileOfCurrentUser().getSupplierOid() != null)
            {
                supplierOid = this.getProfileOfCurrentUser().getSupplierOid();
            }
        }
        
        
        if (ut.getUserTypeOid().equals(BigDecimal.valueOf(1)))
        {
            availabelRolesList = roleService.selectRolesByUserType(ut
                    .getUserTypeOid());
        }
        else if (this.getUserTypeOfCurrentUser().equals(ut.getUserTypeOid()))
        {
            availabelRolesList = roleService.selectRolesByUserOid(getProfileOfCurrentUser().getUserOid());
            GroupHolder group = groupService.selectGroupByUserOid(getProfileOfCurrentUser().getUserOid());
            if (group != null)
            {
                groupList.add(group);
            }
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(2)) || ut.getUserTypeOid().equals(BigDecimal.valueOf(4)))
        {
            availabelRolesList = roleService.selectBuyerRolesByBuyerOidAndUserType( buyerOid, ut.getUserTypeOid());
            groupList = groupService.selectGroupByBuyerOidAndUserTypeOid(buyerOid, ut.getUserTypeOid());
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(3)) || ut.getUserTypeOid().equals(BigDecimal.valueOf(5)))
        {
            availabelRolesList = roleService .selectSupplierRolesBySupplierOidAndUserType(supplierOid, ut.getUserTypeOid());
            groupList = groupService.selectGroupBySupplierOidAndUserTypeOid(supplierOid, ut.getUserTypeOid());
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(6)) || ut.getUserTypeOid().equals(BigDecimal.valueOf(7)))
        {
            availabelRolesList = roleService.selectBuyerRolesByBuyerOidAndUserType(buyerOid, ut.getUserTypeOid());
        }
    }

    
    private void initRolesByConditionFromUserProfile(boolean initFlag) throws Exception
    {
        groupList = new ArrayList<GroupHolder>();
        if (userProfile.getUserType().equals(BigDecimal.valueOf(1)))
        {
            availabelRolesList = roleService
                    .selectRolesByUserType(userProfile.getUserType());
        }
        else if (this.getUserTypeOfCurrentUser().equals(userProfile.getUserType()))
        {
            List<RoleHolder> roleList = roleService.selectRolesByUserOid(getProfileOfCurrentUser().getUserOid());
            List<BigDecimal> roleOids = new ArrayList<BigDecimal>();
            for (RoleHolder role : roleList)
            {
                roleOids.add(role.getRoleOid());
            }
            if (initFlag)//init edit
            {
                List<RoleHolder> currentRoles = roleService.selectRolesByUserOid(userProfile.getUserOid());
                if (currentRoles != null && !currentRoles.isEmpty())
                {
                    for (RoleHolder role : currentRoles)
                    {
                        if (!roleOids.contains(role.getRoleOid()))
                        {
                            roleList.add(role);
                        }
                    }
                }
            }
            else if (selectedRolesList != null && !selectedRolesList.isEmpty())// validate failed(add or edit)
            {
                for (Object obj : selectedRolesList)
                {
                    RoleHolder role = roleService.selectByKey(new BigDecimal(obj.toString()));
                    if (role != null && !roleOids.contains(role.getRoleOid()))
                    {
                        roleList.add(role);
                    }
                }
            }
            availabelRolesList = roleList;
            
            if (!userProfile.getLocked())
            {
                GroupHolder group = groupService.selectGroupByUserOid(getProfileOfCurrentUser().getUserOid());
                if (group != null)
                {
                    groupList.add(group);
                }
                GroupHolder currGroup = null;
                if (initFlag)
                {
                    currGroup = groupService.selectGroupByUserOid(userProfile.getUserOid());
                }
                else
                {
                    if (isAddToGroup && userProfile.getGroupOid() != null)
                    {
                        currGroup = groupService.selectGroupByKey(userProfile.getGroupOid());
                    }
                }
                if (currGroup != null && (groupList.isEmpty() || !groupList.get(0).getGroupOid().equals(currGroup.getGroupOid())))
                {
                    groupList.add(currGroup);
                }
            }
        }
        else if (userProfile.getUserType().equals(BigDecimal.valueOf(2)) || userProfile.getUserType().equals(BigDecimal.valueOf(4)))
        {
            if (userProfile.getBuyerOid() != null)
                availabelRolesList = roleService
                        .selectBuyerRolesByBuyerOidAndUserType(
                                userProfile.getBuyerOid(),
                                userProfile.getUserType());
            if (!userProfile.getLocked())
            {
                GroupHolder param = new GroupHolder();
                param.setUserTypeOid(userProfile.getUserType());
                param.setBuyerOid(userProfile.getBuyerOid());
                groupList = groupService.select(param);
            }
        }
        else if (userProfile.getUserType().equals(BigDecimal.valueOf(3)) || userProfile.getUserType().equals(BigDecimal.valueOf(5)))
        {
            if (userProfile.getSupplierOid() != null)
                availabelRolesList = roleService
                        .selectSupplierRolesBySupplierOidAndUserType(
                                userProfile.getSupplierOid(),
                                userProfile.getUserType());
            if (!userProfile.getLocked())
            {
                GroupHolder param = new GroupHolder();
                param.setUserTypeOid(userProfile.getUserType());
                param.setSupplierOid(userProfile.getSupplierOid());
                groupList = groupService.select(param);
            }
        }
        else if ((userProfile.getUserType().equals(BigDecimal.valueOf(6)) || userProfile.getUserType().equals(BigDecimal.valueOf(7))) && 
        		userProfile.getBuyerOid() != null)
        {
            availabelRolesList = roleService
                    .selectBuyerRolesByBuyerOidAndUserType(
                            userProfile.getBuyerOid(), userProfile.getUserType());
        }
    }
    
    
    private void initAvaliableBuyerFromUserType(UserTypeHolder ut) throws Exception
    {
        if (ut.getUserTypeOid().equals(BigDecimal.valueOf(2)) || ut.getUserTypeOid().equals(BigDecimal.valueOf(4)) 
                || ut.getUserTypeOid().equals(BigDecimal.valueOf(6)) || ut.getUserTypeOid().equals(BigDecimal.valueOf(7)))
        {
            List<BuyerHolder> allBuyers = null;
            if (BigDecimal.valueOf(1).equals(this.getUserTypeOfCurrentUser()))
            {
                allBuyers = buyerService.select(new BuyerHolder());
            }
            else
            {
                allBuyers = allowedAccessCompanyService.selectBuyerByUserOid(this.getProfileOfCurrentUser().getUserOid());
            }
            if (allBuyers == null || allBuyers.isEmpty())
            {
                return;
            }
            BuyerHolder currentBuyer = null;
            if (this.getProfileOfCurrentUser().getBuyerOid() == null)
            {
                if (buyerList == null || buyerList.isEmpty())
                {
                    return;
                }
                currentBuyer = buyerList.get(0);
            }
            else
            {
                currentBuyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            }
            for (BuyerHolder buyer : allBuyers)
            {
                if (buyer.getBuyerOid().equals(currentBuyer.getBuyerOid()))
                {
                    allBuyers.remove(buyer);
                    break;
                }
            }
            availabelBuyersList = allBuyers;
        }
    }
    
    
    private void initAvaliableBuyerFromUserProfile() throws Exception
    {
        if (userProfile.getBuyerOid() == null)
        {
            return;
        }
        List<BuyerHolder> allBuyers = null;
        if (BigDecimal.valueOf(1).equals(this.getUserTypeOfCurrentUser()))
        {
            allBuyers = buyerService.select(new BuyerHolder());
        }
        else
        {
            allBuyers = allowedAccessCompanyService.selectBuyerByUserOid(this.getProfileOfCurrentUser().getUserOid());
        }
        if (allBuyers == null || allBuyers.isEmpty())
        {
            return;
        }
        BuyerHolder currentBuyer = buyerService.selectBuyerByKey(userProfile.getBuyerOid());
        for (BuyerHolder buyer : allBuyers)
        {
            if (buyer.getBuyerOid().equals(currentBuyer.getBuyerOid()))
            {
                allBuyers.remove(buyer);
                break;
            }
        }
        availabelBuyersList = allBuyers;
    }
    
    
    private BigDecimal getCompanyOid(UserProfileHolder user)
    {
        if (isBuyer(user.getUserType()))
        {
            return user.getBuyerOid();
        }
        
        if (isSupplier(user.getUserType()))
        {
            return user.getSupplierOid();
        }
        
        return null;
    }
    
    
    private boolean isBuyer(BigDecimal userTypeOid)
    {
        if (BigDecimal.valueOf(2).equals(userTypeOid)
            || BigDecimal.valueOf(4).equals(userTypeOid))
        {
            return true;
        }
        
        return false;
    }
    
        
    private boolean isSupplier(BigDecimal userTypeOid)
    {
        if (BigDecimal.valueOf(3).equals(userTypeOid)
            || BigDecimal.valueOf(5).equals(userTypeOid))
        {
            return true;
        }
        
        return false;
    }
    
    
    private boolean isStore(BigDecimal userTypeOid)
    {
        if (BigDecimal.valueOf(6).equals(userTypeOid)
                || BigDecimal.valueOf(7).equals(userTypeOid))
        {
            return true;
        }
        return false;
    }
    
    
    private boolean isSysAdmin(BigDecimal userTypeOid)
    {
        if (BigDecimal.ONE.equals(userTypeOid))
        {
            return true;
        }
        return false;
    }
    
    
    private String checkGroupCompanySameToUser(
            List<GroupUserTmpHolder> groupUsers, BigDecimal companyOid,
            BigDecimal userTypeOid) throws Exception
    {
        List<BigDecimal> groupOids = new ArrayList<BigDecimal>();
        for (GroupUserTmpHolder groupUser : groupUsers)
        {
            if (LastUpdateFrom.USER.equals(groupUser.getLastUpdateFrom()))
            {
                continue;
            }
            
            groupOids.add(groupUser.getGroupOid());
        }
        
        if (groupOids.isEmpty())
        {
            return null;
        }
        
        for (BigDecimal groupOid : groupOids)
        {
            GroupTmpHolder group = groupTmpService.selectGroupTmpByKey(groupOid);
            
            if (companyOid == null && !userTypeOid.equals(group.getUserTypeOid()))
            {
                return group.getGroupName();
            }
            else if (isBuyer(group.getUserTypeOid()))
            {
                BigDecimal groupCompanyOid = group.getBuyerOid();
                
                if (!companyOid.equals(groupCompanyOid))
                {
                    return group.getGroupName();
                }
            }
            else if (isSupplier(group.getUserTypeOid()))
            {
                BigDecimal groupCompanyOid = group.getSupplierOid();
                
                if (!companyOid.equals(groupCompanyOid))
                {
                    return group.getGroupName();
                }
            }
        }
        
        return null;
    }
    
    private String checkMaxSupplierUser(BigDecimal supplierOid)throws Exception
    {   
        int maxSupplierUser = 0;
        ControlParameterHolder controlParameter = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(
            SECT_ID_CTRL, PARAM_ID_MAX_USER_AMOUNT_FOR_SUPPLIER);
        if(null != controlParameter && controlParameter.getStringValue() != null 
                && !controlParameter.getStringValue().trim().isEmpty())
        {
            maxSupplierUser = Integer.parseInt(controlParameter
                .getStringValue());
    
            List<UserProfileTmpHolder> userProflieList = userProfileTmpService
                .selectUsersBySupplierOid(supplierOid);
            
            if(userProflieList.size() >= maxSupplierUser)
            {
                return  maxSupplierUser + "";
            }
        }
        
        return "";
    }
    
    private void initSelectAllSessionInfo() throws Exception
    {
        if (getSession().get(ALL_STORE_FLAG) == null)
        {
            if (isSysAdmin(getUserTypeOfCurrentUser()))
            {
                getSession().put(ALL_STORE_FLAG, true);
            }
            else
            {
                BuyerStoreUserHolder allStoreFlag = buyerStoreUserService.selectStoreUserByStoreOidAndUserOid(BigDecimal.valueOf(-3), this.getProfileOfCurrentUser().getUserOid());
                if (allStoreFlag == null)
                {
                    getSession().put(ALL_STORE_FLAG, false);
                }
                else
                {
                    getSession().put(ALL_STORE_FLAG, true);
                }
            }
        }
        
        if (getSession().get(ALL_WAREHOUSE_FLAG) == null)
        {
            if (isSysAdmin(getUserTypeOfCurrentUser()))
            {
                getSession().put(ALL_WAREHOUSE_FLAG, true);
            }
            else
            {
                BuyerStoreUserHolder allWareHouseFlag = buyerStoreUserService.selectStoreUserByStoreOidAndUserOid(BigDecimal.valueOf(-4), this.getProfileOfCurrentUser().getUserOid());
                if (allWareHouseFlag == null)
                {
                    getSession().put(ALL_WAREHOUSE_FLAG, false);
                }
                else
                {
                    getSession().put(ALL_WAREHOUSE_FLAG, true);
                }
            }
        }
        
        if (getSession().get(ALL_AREA_FLAG) == null)
        {
            if (isSysAdmin(getUserTypeOfCurrentUser()))
            {
                getSession().put(ALL_AREA_FLAG, true);
            }
            else
            {
                BuyerStoreAreaUserHolder allAreaFlag = buyerStoreAreaUserService.selectByUserOidAndAreaOid(this.getProfileOfCurrentUser().getUserOid(), BigDecimal.valueOf(-2));
                if (allAreaFlag == null)
                {
                    getSession().put(ALL_AREA_FLAG, false);
                }
                else
                {
                    getSession().put(ALL_AREA_FLAG, true);
                }
            }
        }
        
        if (getSession().get(ALL_CLASS_FLAG) == null)
        {
            if (isSysAdmin(getUserTypeOfCurrentUser()))
            {
                getSession().put(ALL_CLASS_FLAG, true);
            }
            else
            {
                UserClassHolder allClassFlag = userClassService.selectByUserOidAndClassOid(this.getProfileOfCurrentUser().getUserOid(), BigDecimal.valueOf(-1));
                if (allClassFlag == null)
                {
                    getSession().put(ALL_CLASS_FLAG, false);
                }
                else
                {
                    getSession().put(ALL_CLASS_FLAG, true);
                }
            }
        }
        
        if (getSession().get(ALL_SUBCLASS_FLAG) == null)
        {
            if (isSysAdmin(getUserTypeOfCurrentUser()))
            {
                getSession().put(ALL_SUBCLASS_FLAG, true);
            }
            else
            {
                UserSubclassHolder allSubclassFlag = userSubclassService.selectByUserOidAndSubclassOid(this.getProfileOfCurrentUser().getUserOid(), BigDecimal.valueOf(-1));
                if (allSubclassFlag == null)
                {
                    getSession().put(ALL_SUBCLASS_FLAG, false);
                }
                else
                {
                    getSession().put(ALL_SUBCLASS_FLAG, true);
                }
            }
        }
    }
    // ***************************************
    // Getter and Setter methods
    // ***************************************

    public UserProfileTmpExHolder getUserProfile()
    {
        return userProfile;
    }


    public void setUserProfile(UserProfileTmpExHolder userProfile)
    {
        this.userProfile = userProfile;
    }


    public List<UserTypeHolder> getUserTypeList()
    {
        return userTypeList;
    }


    public void setUserTypeList(List<UserTypeHolder> userTypeList)
    {
        this.userTypeList = userTypeList;
    }


    public Map<String, String> getCtrlStatusMap()
    {
        return ctrlStatusMap;
    }


    public void setCtrlStatusMap(Map<String, String> ctrlStatusMap)
    {
        this.ctrlStatusMap = ctrlStatusMap;
    }


    public List<String> getLoginModelList()
    {
        return loginModelList;
    }


    public void setLoginModelList(List<String> loginModelList)
    {
        this.loginModelList = loginModelList;
    }


    public List<? extends Object> getAvailabelRolesList()
    {
        return availabelRolesList;
    }


    public void setAvailabelRolesList(List<? extends Object> availabelRolesList)
    {
        this.availabelRolesList = availabelRolesList;
    }


    public List<? extends Object> getSelectedRolesList()
    {
        return selectedRolesList;
    }


    public void setSelectedRolesList(List<? extends Object> selectedRolesList)
    {
        this.selectedRolesList = selectedRolesList;
    }


    public List<BuyerHolder> getBuyerList()
    {
        return buyerList;
    }


    public void setBuyerList(List<BuyerHolder> buyerList)
    {
        this.buyerList = buyerList;
    }


    public List<SupplierHolder> getSupplierList()
    {
        return supplierList;
    }


    public void setSupplierList(List<SupplierHolder> supplierList)
    {
        this.supplierList = supplierList;
    }


    public List<GroupHolder> getGroupList()
    {
        return groupList;
    }


    public void setGroupList(List<GroupHolder> groupList)
    {
        this.groupList = groupList;
    }


    public UserProfileExHolder getOldUserProfile()
    {
        return oldUserProfile;
    }


    public void setOldUserProfile(UserProfileExHolder oldUserProfile)
    {
        this.oldUserProfile = oldUserProfile;
    }


    public List<? extends Object> getOldSelectedRolesList()
    {
        return oldSelectedRolesList;
    }


    public void setOldSelectedRolesList(List<Object> oldSelectedRolesList)
    {
        this.oldSelectedRolesList = oldSelectedRolesList;
    }


    public String getNewPwd()
    {
        return newPwd;
    }


    public void setNewPwd(String newPwd)
    {
        this.newPwd = newPwd;
    }


    public String getLoginId()
    {
        return loginId;
    }
    

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }


    public boolean getAddToGroup()
    {
        return isAddToGroup;
    }


    public void setAddToGroup(boolean isAddToGroup)
    {
        this.isAddToGroup = isAddToGroup;
    }


    public List<UserProfileTmpExHolder> getMkCreateUserProfileList()
    {
        return mkCreateUserProfileList;
    }


    public void setMkCreateUserProfileList(
            List<UserProfileTmpExHolder> mkCreateUserProfileList)
    {
        this.mkCreateUserProfileList = mkCreateUserProfileList;
    }


    public List<UserProfileTmpExHolder> getMkDeleteUserProfileList()
    {
        return mkDeleteUserProfileList;
    }


    public void setMkDeleteUserProfileList(
            List<UserProfileTmpExHolder> mkDeleteUserProfileList)
    {
        this.mkDeleteUserProfileList = mkDeleteUserProfileList;
    }


    public List<UserProfileTmpExHolder> getMkUpdateUserProfileList()
    {
        return mkUpdateUserProfileList;
    }


    public void setMkUpdateUserProfileList(
            List<UserProfileTmpExHolder> mkUpdateUserProfileList)
    {
        this.mkUpdateUserProfileList = mkUpdateUserProfileList;
    }


    public String getMkErrorInfo()
    {
        return mkErrorInfo;
    }


    public void setMkErrorInfo(String mkErrorInfo)
    {
        this.mkErrorInfo = mkErrorInfo;
    }


    public String getConfirmType()
    {
        return confirmType;
    }


    public void setConfirmType(String confirmType)
    {
        this.confirmType = confirmType;
    }


    public List<? extends Object> getAvailabelStoresList()
    {
        return availabelStoresList;
    }


    public void setAvailabelStoresList(
            List<? extends Object> availabelStoresList)
    {
        this.availabelStoresList = availabelStoresList;
    }


    public List<? extends Object> getSelectedStoresList()
    {
        return selectedStoresList;
    }


    public void setSelectedStoresList(List<? extends Object> selectedStoresList)
    {
        this.selectedStoresList = selectedStoresList;
    }


    public List<? extends Object> getAvailabelAreasList()
    {
        return availabelAreasList;
    }


    public void setAvailabelAreasList(List<? extends Object> availabelAreasList)
    {
        this.availabelAreasList = availabelAreasList;
    }


    public List<? extends Object> getSelectedAreasList()
    {
        return selectedAreasList;
    }


    public void setSelectedAreasList(List<? extends Object> selectedAreasList)
    {
        this.selectedAreasList = selectedAreasList;
    }


    public List<? extends Object> getOldSelectedStoresList()
    {
        return oldSelectedStoresList;
    }


    public List<? extends Object> getOldSelectedAreasList()
    {
        return oldSelectedAreasList;
    }


    public List<? extends Object> getAvailabelWareHouseList()
    {
        return availabelWareHouseList;
    }


    public void setAvailabelWareHouseList(
        List<? extends Object> availabelWareHouseList)
    {
        this.availabelWareHouseList = availabelWareHouseList;
    }


    public List<? extends Object> getSelectedWareHouseList()
    {
        return selectedWareHouseList;
    }


    public void setSelectedWareHouseList(
        List<? extends Object> selectedWareHouseList)
    {
        this.selectedWareHouseList = selectedWareHouseList;
    }


    public List<? extends Object> getAvailabelBuyersList()
    {
        return availabelBuyersList;
    }


    public void setAvailabelBuyersList(List<? extends Object> availabelBuyersList)
    {
        this.availabelBuyersList = availabelBuyersList;
    }


    public List<? extends Object> getSelectedBuyersList()
    {
        return selectedBuyersList;
    }


    public void setSelectedBuyersList(List<? extends Object> selectedBuyersList)
    {
        this.selectedBuyersList = selectedBuyersList;
    }


    public List<? extends Object> getOldSelectedBuyersList()
    {
        return oldSelectedBuyersList;
    }


    public void setOldSelectedBuyersList(
            List<? extends Object> oldSelectedBuyersList)
    {
        this.oldSelectedBuyersList = oldSelectedBuyersList;
    }


    public List<? extends Object> getAvailableClassList()
    {
        return availableClassList;
    }


    public void setAvailableClassList(List<? extends Object> availableClassList)
    {
        this.availableClassList = availableClassList;
    }


    public List<? extends Object> getSelectedClassList()
    {
        return selectedClassList;
    }


    public void setSelectedClassList(List<? extends Object> selectedClassList)
    {
        this.selectedClassList = selectedClassList;
    }


    public List<? extends Object> getAvailableSubclassList()
    {
        return availableSubclassList;
    }


    public void setAvailableSubclassList(
            List<? extends Object> availableSubclassList)
    {
        this.availableSubclassList = availableSubclassList;
    }


    public List<? extends Object> getSelectedSubclassList()
    {
        return selectedSubclassList;
    }


    public void setSelectedSubclassList(List<? extends Object> selectedSubclassList)
    {
        this.selectedSubclassList = selectedSubclassList;
    }


    public List<? extends Object> getOldSelectedClassList()
    {
        return oldSelectedClassList;
    }


    public List<? extends Object> getOldSelectedSubclassList()
    {
        return oldSelectedSubclassList;
    }
    
}
