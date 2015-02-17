package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.GroupType;
import com.pracbiz.b2bportal.core.constants.LastUpdateFrom;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerTmpHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.GroupTmpExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;
import com.pracbiz.b2bportal.core.service.GroupSupplierTmpService;
import com.pracbiz.b2bportal.core.service.GroupTmpService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerTmpService;
import com.pracbiz.b2bportal.core.service.GroupUserTmpService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PopupSupplierService;
import com.pracbiz.b2bportal.core.service.RoleGroupTmpService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.service.UserTypeService;

public class GroupAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(GroupAction.class);
    private static final long serialVersionUID = 6974201401071195605L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_GROUP = "SEARCH_PARAMETER_GROUP";
    public static final String SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_USER = "SEARCH_PARAMETER_CREATE_GROUP_POPUP_USER";
    public static final String SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_SUPPLIER = "SEARCH_PARAMETER_CREATE_GROUP_POPUP_SUPPLIER";
    public static final String SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_TRADING_PARTNER = "SEARCH_PARAMETER_CREATE_GROUP_POPUP_TRADING_PARTNER";
    public static final String REQUEST_PARAMETER_UPDATE_DATE="groupProfile.lastUpdateDate";
    public static final String DATE_FORMATER="dd/MM/yyyy HH:mm:ss SSS";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.GroupAction.selectedOids";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final String REQUEST_OID_DELIMITER = "\\-";
    
    private static final String BACK_TO_LIST = "button.back.to.list";
    private static final String INIT = "init";
    private static final Map<String, String> sortMap;
    private static final Map<String, String> userSortMap;
    private static final Map<String, String> suppStortMap;
    private static final Map<String, String> tradingPartnerStortMap;
    
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("groupId", "GROUP_ID");
        sortMap.put("userTypeId", "USER_TYPE_ID");
        sortMap.put("groupType", "GROUP_TYPE");
        sortMap.put("actionType", "ACTION_TYPE");
        sortMap.put("actor", "ACTOR");
        sortMap.put("ctrlStatus", "CTRL_STATUS");
        sortMap.put("actionDate", "ACTION_DATE");
        sortMap.put("company", "COMPANY");
        
        userSortMap = new HashMap<String, String>();
        userSortMap.put("loginId", "LOGIN_ID");
        userSortMap.put("userName", "USER_NAME");
        
        suppStortMap = new HashMap<String, String>();
        suppStortMap.put("supplierCode", "SUPPLIER_CODE");
        suppStortMap.put("supplierName", "SUPPLIER_NAME");
        
        tradingPartnerStortMap = new HashMap<String, String>(); 
        tradingPartnerStortMap.put("buyerSupplierCode", "BUYER_SUPPLIER_CODE");
        tradingPartnerStortMap.put("buyerCode", "BUYER_CODE");
        tradingPartnerStortMap.put("buyerName", "BUYER_NAME");
        tradingPartnerStortMap.put("supplierCode", "SUPPLIER_CODE");
        tradingPartnerStortMap.put("supplierName", "SUPPLIER_NAME");
    }
    
    private GroupTmpExHolder param;
    private GroupTmpExHolder groupProfile;
    private GroupTmpExHolder oldGroupProfile;
    
    private UserProfileExHolder userProfile;
    private SupplierExHolder supplier;
    private TradingPartnerExHolder tradingPartner;
    
    private List<? extends Object> userTypes;
    private List<? extends Object> buyers;
    private List<? extends Object> suppliers;
    private List<? extends Object> availabelRoles;
    private List<? extends Object> selectedRoles;
    private List<? extends Object> selectedUsers;
    private List<? extends Object> selectedTps;
    private List<? extends Object> selectedSupps;
    private List<? extends Object> tradingPartners;
    
    private List<? extends Object> oldSelectedRoles;
    private List<? extends Object> oldSelectedUsers;
    private List<? extends Object> oldSelectedTps;
    private List<? extends Object> oldSelectedSupps;
    private Map<String, String> ctrlStatuses;
    
    private String ajaxMsg;
    private String confirmType;
    List<GroupTmpHolder> createList;
    List<GroupTmpHolder> updateList;
    List<GroupTmpHolder> deleteList;
    private boolean selectAllSupplier;
    private boolean selectAllTp;

    @Autowired transient private OidService oidService;
    @Autowired transient private GroupService groupService;
    @Autowired transient private GroupTmpService groupTmpService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private SupplierService supplierService;
    @Autowired transient private RoleService roleService;
    @Autowired transient private UserProfileService userProfileService;
    @Autowired transient private UserProfileTmpService userProfileTmpService;
    @Autowired transient private TradingPartnerService tradingPartnerService;
    @Autowired transient private GroupUserTmpService groupUserTmpService;
    @Autowired transient private GroupSupplierTmpService groupSupplierTmpService;
    @Autowired transient private GroupTradingPartnerTmpService groupTradingPartnerTmpService;
    @Autowired transient private GroupTradingPartnerService groupTradingPartnerService;
    @Autowired transient private RoleGroupTmpService roleGroupTmpService;
    @Autowired transient private PopupSupplierService popupSupplierService;
    @Autowired transient private GroupSupplierService groupSupplierService;

    public GroupAction()
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_GROUP);
        
        if (param == null)
        {
            param = (GroupTmpExHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_GROUP);
        }
        
        try
        {
            this.initUserTypes();
            ctrlStatuses = MkCtrlStatus.toMapValue();
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        this.getSession().put(SESSION_KEY_SEARCH_PARAMETER_GROUP, param);
        return SUCCESS;
    }
    
    
    public String search()
    {
        if (null == param)
        {
            param = new GroupTmpExHolder();
        }
        
        try
        {
            param.trimAllString();
            param.setAllEmptyStringToNull();
            param.setCurrentUserTypeOid(this.getUserTypeOfCurrentUser());
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_GROUP, param);
        
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            GroupTmpExHolder searchParam = (GroupTmpExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_GROUP);
            
            if (searchParam == null)
            {
                searchParam = new GroupTmpExHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_GROUP, searchParam);
            }
            
            searchParam.setCurrentUserTypeOid(this
                .getUserTypeOfCurrentUser());
            searchParam.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            searchParam.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());

            this.obtainListRecordsOfPagination(groupService, searchParam,
                sortMap, "groupOid", null);
            
            
            for (BaseHolder baseItem : this.getGridRlt().getItems())
            {
                GroupTmpExHolder item = (GroupTmpExHolder) baseItem;
                if (item.getGroupType() != null)
                {
                    item.setGroupTypeValue(this.getText(item.getGroupType().getKey()));
                }
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
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
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
            initUserTypes();
         
            if(this.getUserTypeOfCurrentUser().equals(BigDecimal.ONE))
            {
                // If current user is type of system admin
                buyers = buyerService.select(new BuyerHolder());
                suppliers = supplierService.select(new SupplierExHolder());
            }
            else
            {
                this.initBuyerSupplierInfo(this.getProfileOfCurrentUser());
                if(this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
                {
                    suppliers = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
                else if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
                {
                    SupplierExHolder param = new SupplierExHolder();
                    param.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                    suppliers = supplierService.select(param);
                }
            }
            
            if (buyers == null) buyers = new ArrayList<Object>();
            if (suppliers == null) suppliers = new ArrayList<Object>();
            
            if (selectedRoles == null) selectedRoles = new ArrayList<Object>();
            if (selectedSupps == null) selectedSupps = new ArrayList<Object>();
            if (selectedTps == null) selectedTps = new ArrayList<Object>();
            if (selectedUsers == null) selectedUsers = new ArrayList<Object>();
            
            UserTypeHolder userType = (UserTypeHolder)userTypes.get(0);
            
            if (userType != null)
            {
                initRolesByConditionFromUserType((UserTypeHolder)userTypes.get(0));
            }
            
            if (availabelRoles == null) availabelRoles = new ArrayList<Object>();
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
        boolean flag = this.hasErrors();
        try
        {
            boolean isBuyer = this.isBuyer(groupProfile.getUserTypeOid());
            boolean isSupplier = this.isSupplier(groupProfile.getUserTypeOid());
            
            if (isBuyer && this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
            {
                groupProfile.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            
            if (isSupplier && this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
            {
                groupProfile.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
            }
            
            if (!flag)
            {
                boolean exist = false;
                if (isBuyer)
                {
                    BigDecimal companyOid = groupProfile.getBuyerOid() == null ? this
                        .getProfileOfCurrentUser().getBuyerOid() : groupProfile
                        .getBuyerOid();
                        
                    exist = groupTmpService.isGroupIdExist(
                        groupProfile.getGroupId(), companyOid, true);
                    if(this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
                    {
                        groupProfile.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                    }
                }
                
                if (isSupplier)
                {
                    BigDecimal companyOid = groupProfile.getSupplierOid() == null ? this
                        .getProfileOfCurrentUser().getSupplierOid() : groupProfile
                        .getSupplierOid();
                        
                    exist = groupTmpService.isGroupIdExist(groupProfile.getGroupId(),companyOid,false);
                    if(this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
                    {
                        groupProfile.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                    }
                }
                
                if (exist)
                {
                    this.addActionError(this.getText("B2BPC0203", new String[]{groupProfile.getGroupId()}));
                    flag = true;
                }
            }
            
            if (!flag && (selectedRoles == null || selectedRoles.isEmpty()))
            {
                this.addActionError(getText("B2BPC0214"));
                flag = true;
            }
            
            if (!flag)
            {
                
                if (isBuyer && (selectedSupps == null || selectedSupps.isEmpty()))
                {
                    this.addActionError(getText("B2BPC0216"));
                    flag = true;
                }
                
                if (isSupplier && (selectedTps == null || selectedTps.isEmpty()))
                {
                    this.addActionError(getText("B2BPC0217"));
                    flag = true;
                }
            }
            
            if (flag)
            {
                this.initPageDataForAddFailed();
            }
          
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String saveAdd()
    {
        try
        {
            BigDecimal groupOid = this.oidService.getOid();
            //groupProfile.trimAllString();
            //groupProfile.setAllEmptyStringToNull();
            groupProfile.setGroupOid(groupOid);
            
            BigDecimal userTypOid = groupProfile.getUserTypeOid();
            UserTypeHolder userType = userTypeService.selectByKey(userTypOid);
            GroupType groupType = userType.getGroupType();
            groupProfile.setGroupType(groupType);
            
            groupProfile.setCreateDate(new Date());
            groupProfile.setCreateBy(this.getLoginIdOfCurrentUser());
            
            if (groupProfile.getGroupType().equals(GroupType.BUYER))
            {
                groupProfile.setSupplierOid(null);
                List<GroupSupplierHolder> groupSuppliers = new ArrayList<GroupSupplierHolder>();
                if (selectAllSupplier  && this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(1)))
                {
                    GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
                    groupSupplier.setGroupOid(groupOid);
                    groupSupplier.setSupplierOid(new BigDecimal(-1));
                    groupSuppliers.add(groupSupplier);
                }
                else
                {
                    for (Object obj : selectedSupps)
                    {
                        GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
                        groupSupplier.setGroupOid(groupOid);
                        groupSupplier.setSupplierOid(new BigDecimal(obj.toString()));
                        groupSuppliers.add(groupSupplier);
                    }
                }
                groupProfile.setGroupSuppliers(groupSuppliers);
                if(this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
                {
                    groupProfile.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
            }
            else if (groupProfile.getGroupType().equals(GroupType.SUPPLIER))
            {
                groupProfile.setBuyerOid(null);
                List<GroupTradingPartnerHolder> groupTradingPartners = new ArrayList<GroupTradingPartnerHolder>();
                if (selectAllTp && (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(1))|| 
                        this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2))))
                {
                    GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder(); 
                    groupTradingPartner.setGroupOid(groupOid);
                    groupTradingPartner.setTradingPartnerOid(new BigDecimal(-1));
                    groupTradingPartners.add(groupTradingPartner);
                }
                else
                {
                    for (Object obj : selectedTps)
                    {
                        GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder(); 
                        groupTradingPartner.setGroupOid(groupOid);
                        groupTradingPartner.setTradingPartnerOid(new BigDecimal(obj.toString()));
                        groupTradingPartners.add(groupTradingPartner);
                    }
                }
                groupProfile.setGroupTradingPartners(groupTradingPartners);
                if(this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
                {
                    groupProfile.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                }
            }
            
            
            List<RoleGroupHolder> roleGroups = new ArrayList<RoleGroupHolder>();
            for (Object obj : selectedRoles)
            {
                RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
                roleGroup.setGroupOid(groupOid);
                roleGroup.setRoleOid(new BigDecimal(obj.toString()));
                roleGroups.add(roleGroup);
            }
            
            groupProfile.setRoleGroups(roleGroups);
            
            List<GroupUserHolder> groupUsers = new ArrayList<GroupUserHolder>();
            
            if (selectedUsers != null && !selectedUsers.isEmpty())
            {
                for (Object obj : selectedUsers)
                {
                    GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
                    groupUser.setGroupOid(groupOid);
                    groupUser.setUserOid(new BigDecimal(obj.toString()));
                    groupUser.setLastUpdateFrom(LastUpdateFrom.GROUP);
                    groupUser.setActionType(DbActionType.CREATE);
                    groupUser.setApproved(false);
                    groupUsers.add(groupUser);
                }
            }
            groupProfile.setGroupUsers(groupUsers);
            
            groupService.createGroupProfile(this.getCommonParameter(), groupProfile);
            
            if (this.getCommonParameter().getMkMode())
            {
                log.info(this.getText(
                    "B2BPC0205",
                    new String[] {groupProfile.getGroupId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            else
            {
                log.info(this.getText(
                    "B2BPC0204",
                    new String[] {groupProfile.getGroupId(),
                        this.getLoginIdOfCurrentUser()}));
            }
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    // *****************************************************
    // view group page
    // *****************************************************
    public void validateView()
    {
        boolean flag = this.hasErrors();
        try
        {
            if (!flag && (param == null || param.getGroupOid() == null))
            {
                this.addActionError(this.getText("B2BPC0220"));
                flag = true;
            }
            
            if (!flag)
            {
                GroupTmpHolder groupTmp = groupTmpService.selectGroupTmpByKey(param.getGroupOid());
                if (groupTmp == null)
                {
                    this.addActionError(this.getText("B2BPC0221"));
                    flag = true;
                }
                else
                {
                    groupProfile = new GroupTmpExHolder();
                    BeanUtils.copyProperties(groupTmp, groupProfile);
                    this.initBuyerSupplierInfo(this.getProfileOfCurrentUser());
                }
            
            }
            
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
        
    }
    
    
    public String view()
    {
        try
        {
            BigDecimal userTypeOid = groupProfile.getUserTypeOid();
            UserTypeHolder userType = userTypeService.selectByKey(userTypeOid);
            
            if (userType != null)
            {
                String userTypeId = userType.getUserTypeDesc();
                groupProfile.setUserTypeId(userTypeId);
            }
            
            BigDecimal groupOid = groupProfile.getGroupOid();
            boolean isBuyer = this.isBuyer(userTypeOid);
            boolean isSupplier = this.isSupplier(userTypeOid);
            
            if (isBuyer)
            {
                List<GroupSupplierTmpHolder> allGroupSuppliers = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(groupOid);
                if (allGroupSuppliers != null && allGroupSuppliers.size() == 1 
                        && allGroupSuppliers.get(0).getSupplierOid().equals(BigDecimal.valueOf(-1)))
                {
                    selectedSupps = initAllSupplier();
                }
                else
                {
                    selectedSupps = supplierService.selectSupplierByTmpGroupOidAndBuyerOid(groupOid, groupProfile.getBuyerOid());
                }
                BuyerHolder buyer = buyerService.selectBuyerByKey(groupProfile.getBuyerOid());
                groupProfile.setBuyerName(buyer.getBuyerName());
            }
            
            if (isSupplier)
            {
                List<GroupTradingPartnerTmpHolder> allGroupTps = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(groupOid);
                if (allGroupTps != null && allGroupTps.size() == 1 
                        && allGroupTps.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                {
                    selectedTps = initAllTradingPartner();
                }
                else
                {
                    selectedTps = this.getTradingPartnerByGroupOid(groupProfile, false);
                }
                SupplierHolder supplier = supplierService.selectSupplierByKey(groupProfile.getSupplierOid());
                groupProfile.setSupplierName(supplier.getSupplierName());
            }
            
            selectedUsers = this.getTmpUsers(groupOid);
            selectedRoles = roleService.selectRolesByTmpGroupOid(groupOid);
            
            if (selectedSupps == null) selectedSupps = new ArrayList<Object>();
            if (selectedTps == null) selectedTps = new ArrayList<Object>();
            if (selectedRoles == null) selectedRoles = new ArrayList<Object>();
            if (selectedUsers == null) selectedUsers = new ArrayList<Object>();
            sortSupplier(selectedSupps);
            sortTradingPartner(selectedTps);
            
            
            MkCtrlStatus ctrlStatus = groupProfile.getCtrlStatus();
            if (MkCtrlStatus.APPROVED.equals(ctrlStatus)
                || MkCtrlStatus.REJECTED.equals(ctrlStatus)
                || MkCtrlStatus.WITHDRAWN.equals(ctrlStatus))
            {
                return "view";
            }
            
            DbActionType actionType = groupProfile.getActionType();
            String actionKey = actionType.getKey();
            String actionValue = getText(actionKey);
            groupProfile.setActionTypeValue(actionValue);
            
            String ctrlStatusKey = ctrlStatus.getKey();
            String ctrlStatusValue = getText(ctrlStatusKey);
            groupProfile.setCtrlStatusValue(ctrlStatusValue);
            
            if (MkCtrlStatus.PENDING.equals(ctrlStatus)
                && !DbActionType.UPDATE.equals(actionType))
            {
                return "viewPending"; 
            }
            
            oldGroupProfile = new GroupTmpExHolder();
            GroupHolder group = groupService.selectGroupByKey(groupOid);
            BeanUtils.copyProperties(group, oldGroupProfile);
            
            UserTypeHolder oldUserType = userTypeService.selectByKey(userTypeOid);
            if (oldUserType != null)
            {
                String userTypeId = oldUserType.getUserTypeDesc();
                oldGroupProfile.setUserTypeId(userTypeId);
            }
            
            if (isBuyer)
            {
                BigDecimal buyerOid = oldGroupProfile.getBuyerOid();
                List<GroupSupplierHolder> allGroupSuppliers = groupSupplierService.selectGroupSupplierByGroupOid(groupOid);
                if (allGroupSuppliers != null && allGroupSuppliers.size() == 1 
                        && allGroupSuppliers.get(0).getSupplierOid().equals(BigDecimal.valueOf(-1)))
                {
                    oldSelectedSupps = initAllSupplier();
                }
                else
                {
                    oldSelectedSupps = supplierService.selectSupplierByGroupOidAndBuyerOid(groupOid, buyerOid);
                }
                BuyerHolder buyer = buyerService.selectBuyerByKey(oldGroupProfile.getBuyerOid());
                oldGroupProfile.setBuyerName(buyer.getBuyerName());
            }
            
            if (isSupplier)
            {
                List<GroupTradingPartnerHolder> allGroupTps = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(groupOid);
                if (allGroupTps != null && allGroupTps.size() == 1 
                        && allGroupTps.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                {
                    oldSelectedTps = initAllTradingPartner();
                }
                else
                {
                    oldSelectedTps = this.getTradingPartnerByGroupOid(oldGroupProfile, true);
                }
                SupplierHolder supplier = supplierService.selectSupplierByKey(oldGroupProfile.getSupplierOid());
                oldGroupProfile.setSupplierName(supplier.getSupplierName());
            }
            
            oldSelectedUsers = this.getUsers(groupOid);
            oldSelectedRoles = roleService.selectRolesByGroupOid(groupOid);
            
            if (oldSelectedSupps == null) oldSelectedSupps = new ArrayList<Object>();
            if (oldSelectedTps == null) oldSelectedTps = new ArrayList<Object>();
            if (oldSelectedRoles == null) oldSelectedRoles = new ArrayList<Object>();
            oldSelectedUsers = new ArrayList<Object>();
            sortSupplier(oldSelectedSupps);
            sortTradingPartner(oldSelectedTps);
            
            return "viewPendingUpdate";
        }
        catch(Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        
        }
        
    }
    
    
    // *****************************************************
    // edit group page
    // *****************************************************
    public void validateInitEdit()
    {
        boolean flag = this.hasErrors();
        try
        {
            if (!flag && (param == null || param.getGroupOid() == null))
            {
                this.addActionError(this.getText("B2BPC0220"));
                flag = true;
            }
            
            if (!flag)
            {
                GroupTmpHolder groupTmp = groupTmpService.selectGroupTmpByKey(param.getGroupOid());
                if (groupTmp == null)
                {
                    this.addActionError(this.getText("B2BPC0221"));
                    flag = true;
                }
                else
                {
                    groupProfile = new GroupTmpExHolder();
                    BeanUtils.copyProperties(groupTmp, groupProfile);
                }
            
            }
            
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
        
    }
    
    
    public String initEdit()
    {
        try
        {
            initUserTypes();
            
            UserTypeHolder userType = userTypeService.selectByKey(groupProfile.getUserTypeOid());
            groupProfile.setUserTypeId(userType.getUserTypeDesc());
            String lastUpdateDate = groupProfile.getUpdateDate() == null ? "" : DateUtil
                .getInstance().convertDateToString(groupProfile.getUpdateDate(),
                    DATE_FORMATER);
            groupProfile.setLastUpdateDate(lastUpdateDate);
            BigDecimal userTypeOid = userType.getUserTypeOid();
            BigDecimal groupOid = groupProfile.getGroupOid();
            
            boolean isBuyer = this.isBuyer(userTypeOid);
            boolean isSupplier = this.isSupplier(userTypeOid);
            
            if (isBuyer)
            {
                BigDecimal buyerOid = groupProfile.getBuyerOid();
                List<GroupSupplierTmpHolder> allGroupSuppliers = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(groupOid);
                if (allGroupSuppliers != null && allGroupSuppliers.size() == 1 
                        && allGroupSuppliers.get(0).getSupplierOid().equals(BigDecimal.valueOf(-1)))
                {
                    selectAllSupplier = true;
                    selectedSupps = initAllSupplier();
                }
                else
                {
                    selectedSupps = supplierService.selectSupplierByTmpGroupOidAndBuyerOid(groupOid, buyerOid);
                }
                availabelRoles = roleService.selectBuyerRolesByBuyerOidAndUserType(buyerOid, userTypeOid);
            }
            
            if (isSupplier)
            {
                BigDecimal supplierOid = groupProfile.getSupplierOid();
                List<GroupTradingPartnerTmpHolder> allGroupTps = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(groupOid);
                if (allGroupTps != null && allGroupTps.size() == 1 
                        && allGroupTps.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                {
                    selectAllTp = true;
                    selectedTps = initAllTradingPartner();
                }
                else
                {
                    selectedTps = this.getTradingPartnerByGroupOid(groupProfile, false);
                }
                availabelRoles = roleService.selectSupplierRolesBySupplierOidAndUserType(supplierOid,userTypeOid);
            }
            
            if(this.getUserTypeOfCurrentUser().equals(BigDecimal.ONE))
            {
                // If current user is type of system admin
                buyers = buyerService.select(new BuyerHolder());
                suppliers = supplierService.select(new SupplierExHolder());
            }
            else
            {
                this.initBuyerSupplierInfo(this.getProfileOfCurrentUser());
                if(this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
                {
                    BuyerHolder param = new BuyerHolder();
                    param.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                    buyers = buyerService.select(param);
                    suppliers = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
                else if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
                {
                    SupplierExHolder param = new SupplierExHolder();
                    param.setSupplierOid(groupProfile.getSupplierOid());
                    suppliers = supplierService.select(param);
                }
            }
            selectedUsers = this.getTmpUsers(groupOid);
            selectedRoles = roleService.selectRolesByTmpGroupOid(groupOid);
            if (selectedRoles == null) selectedRoles = new ArrayList<Object>();
            if (selectedUsers == null) selectedUsers = new ArrayList<Object>();
            if (selectedSupps == null) selectedSupps = new ArrayList<Object>();
            if (selectedTps == null) selectedTps = new ArrayList<Object>();
            if (buyers == null) buyers = new ArrayList<Object>(); 
            if (suppliers == null) suppliers = new ArrayList<Object>();
            
            if (!selectedRoles.isEmpty())
            {
                Iterator<? extends Object> itrator = availabelRoles.iterator();
                while (itrator.hasNext())
                {
                    RoleHolder role = (RoleHolder)itrator.next();
                    for (Object obj : selectedRoles)
                    {
                        RoleHolder selectedRole = (RoleHolder)obj;
                        String roleOid = selectedRole.getRoleOid().toString();
                        if ((role.getRoleOid().toString()).equals(roleOid))
                        {
                            itrator.remove();
                            break;
                        }
                    }
                }
            }
            Collections.sort(selectedSupps, new Comparator<Object>()
                    {

                        @Override
                        public int compare(Object o1, Object o2)
                        {
                            String suppName1 = ((SupplierHolder)o1).getSupplierName();
                            String suppName2 = ((SupplierHolder)o2).getSupplierName();
                            return suppName1.compareToIgnoreCase(suppName2);
                        }
                
                    });
            Collections.sort(selectedTps, new Comparator<Object>()
                    {
                
                        @Override
                        public int compare(Object o1, Object o2)
                        {
                            String tpName1 = ((TradingPartnerExHolder)o1).getTradingPartnerDesc();
                            String tpName2 = ((TradingPartnerExHolder)o2).getTradingPartnerDesc();
                            return tpName1.compareToIgnoreCase(tpName2);
                        }
                
                    });
            
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
        boolean flag = this.hasErrors();
        try
        {
            GroupTmpHolder group = groupTmpService
                .selectGroupTmpByKey(groupProfile.getGroupOid());
            
            oldGroupProfile = new GroupTmpExHolder();
            BeanUtils.copyProperties(group, oldGroupProfile);
            
            boolean isBuyer = this.isBuyer(oldGroupProfile.getUserTypeOid());
            boolean isSupplier = this.isSupplier(oldGroupProfile.getUserTypeOid());
            
            if (!flag)
            {
                
                boolean exist = false;
                if(isBuyer
                    && !group.getGroupId().equalsIgnoreCase(
                        groupProfile.getGroupId()))
                {
                    BigDecimal companyOid = groupProfile.getBuyerOid() == null ? this
                        .getProfileOfCurrentUser().getBuyerOid() : groupProfile
                        .getBuyerOid();
                    exist = groupTmpService.isGroupIdExist(groupProfile.getGroupId(),companyOid,true);
                }
                
                if(isSupplier
                    && !group.getGroupId().equalsIgnoreCase(
                        groupProfile.getGroupId()))
                {
                    BigDecimal companyOid = groupProfile.getSupplierOid() == null ? this
                        .getProfileOfCurrentUser().getSupplierOid() : groupProfile
                        .getSupplierOid();
                    exist = groupTmpService.isGroupIdExist(groupProfile.getGroupId(),companyOid,false);
                }
                
                if (exist)
                {
                    this.addActionError(this.getText("B2BPC0203", new String[]{groupProfile.getGroupId()}));
                    flag = true;
                }

                
                String currDate = group.getUpdateDate() == null ? "" : DateUtil
                    .getInstance().convertDateToString(group.getUpdateDate(),
                        DATE_FORMATER);
                
                String oldDate = this.getRequest().getParameter(
                    REQUEST_PARAMETER_UPDATE_DATE);
                

                if (!flag && !currDate.equals(oldDate))
                {
                    this.addActionError(this.getText("B2BPC0222"));
                    flag = true;
                }
            }
            
            if (!flag && (selectedRoles == null || selectedRoles.isEmpty()))
            {
                this.addActionError(getText("B2BPC0214"));
                flag = true;
            }
            
            if (!flag)
            {
                if (isBuyer && (selectedSupps == null || selectedSupps.isEmpty()))
                {
                    this.addActionError(getText("B2BPC0216"));
                    flag = true;
                }
                
                if (isSupplier && (selectedTps == null || selectedTps.isEmpty()))
                {
                    this.addActionError(getText("B2BPC0217"));
                    flag = true;
                }
            }
            
            if (flag)
            {
                this.initPageDataForEditFailed();
            }
          
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
        
    }
    
    
    public String saveEdit()
    {
        try
        {
            groupProfile.trimAllString();
            groupProfile.setAllEmptyStringToNull();
            
            BigDecimal userTypOid = oldGroupProfile.getUserTypeOid();
            UserTypeHolder userType = userTypeService.selectByKey(userTypOid);
            GroupType groupType = userType.getGroupType();
            
            GroupTmpHolder currGroup = new GroupTmpHolder();
            BeanUtils.copyProperties(oldGroupProfile, currGroup);
            currGroup.setGroupType(groupType);
            currGroup.setGroupId(groupProfile.getGroupId());
            currGroup.setGroupName(groupProfile.getGroupName());
            currGroup.setUserTypeOid(userTypOid);
            currGroup.setUpdateDate(new Date());
            currGroup.setUpdateBy(this.getLoginIdOfCurrentUser());
            currGroup.setBuyerOid(groupProfile.getBuyerOid());
            currGroup.setSupplierOid(groupProfile.getSupplierOid());
            
            BigDecimal groupOid = groupProfile.getGroupOid();
            
            if (currGroup.getGroupType().equals(GroupType.BUYER))
            {
                currGroup.setSupplierOid(null);
                List<GroupSupplierHolder> groupSuppliers = new ArrayList<GroupSupplierHolder>();
                if (selectAllSupplier  && this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(1)))
                {
                    GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
                    groupSupplier.setGroupOid(groupOid);
                    groupSupplier.setSupplierOid(new BigDecimal(-1));
                    groupSuppliers.add(groupSupplier);
                }
                else
                {
                    for (Object obj : selectedSupps)
                    {
                        GroupSupplierTmpHolder groupSupplier = new GroupSupplierTmpHolder();
                        groupSupplier.setGroupOid(groupOid);
                        groupSupplier.setSupplierOid(new BigDecimal(obj.toString()));
                        groupSuppliers.add(groupSupplier);
                    }
                }
                currGroup.setGroupSuppliers(groupSuppliers);
                if(this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
                {
                    currGroup.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
            }
            else if (currGroup.getGroupType().equals(GroupType.SUPPLIER))
            {
                currGroup.setBuyerOid(null);
                List<GroupTradingPartnerHolder> groupTradingPartners = new ArrayList<GroupTradingPartnerHolder>();
                if (selectAllTp && (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(1)) 
                        || this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2))))
                {
                    GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder(); 
                    groupTradingPartner.setGroupOid(groupOid);
                    groupTradingPartner.setTradingPartnerOid(new BigDecimal(-1));
                    groupTradingPartners.add(groupTradingPartner);
                }
                else
                {
                    for (Object obj : selectedTps)
                    {
                        GroupTradingPartnerTmpHolder groupTradingPartner = new GroupTradingPartnerTmpHolder(); 
                        groupTradingPartner.setGroupOid(groupOid);
                        groupTradingPartner.setTradingPartnerOid(new BigDecimal(obj.toString()));
                        groupTradingPartners.add(groupTradingPartner);
                    }
                }
                currGroup.setGroupTradingPartners(groupTradingPartners);
                if(this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
                {
                    currGroup.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                }
            }
            
            
            List<RoleGroupHolder> roleGroups = new ArrayList<RoleGroupHolder>();
            for (Object obj : selectedRoles)
            {
                RoleGroupTmpHolder roleGroup = new RoleGroupTmpHolder();
                roleGroup.setGroupOid(groupOid);
                roleGroup.setRoleOid(new BigDecimal(obj.toString()));
                roleGroups.add(roleGroup);
            }
            
            currGroup.setRoleGroups(roleGroups);
            
            List<GroupUserHolder> groupUsers = new ArrayList<GroupUserHolder>();
            
            if (selectedUsers != null && !selectedUsers.isEmpty())
            {
                for (Object obj : selectedUsers)
                {
                    GroupUserTmpHolder groupUser = new GroupUserTmpHolder();
                    groupUser.setGroupOid(groupOid);
                    groupUser.setUserOid(new BigDecimal(obj.toString()));
                    groupUsers.add(groupUser);
                }
            }
            currGroup.setGroupUsers(groupUsers);
            
            
            List<RoleGroupTmpHolder> roles = roleGroupTmpService.selectRoleGroupTmpByGroupOid(groupOid);
                
            if (roles != null)
            {
                oldGroupProfile.addRoleGroups(roles);
            }
            
            List<GroupSupplierTmpHolder> suppliers = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(groupOid);
            if (suppliers != null)
            {
                oldGroupProfile.addGroupSuppliers(suppliers);
            }
            
            List<GroupUserTmpHolder> users = groupUserTmpService.selectGroupUserTmpByGroupOid(groupOid);
            if (users != null)
            {
                oldGroupProfile.addGroupUsers(users);
            }
            
            List<GroupTradingPartnerTmpHolder> tps = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(groupOid);
            if (tps != null)
            {
                oldGroupProfile.addGroupTradingPartners(tps);
            }
            
            groupService.updateGroupProfile(getCommonParameter(), oldGroupProfile, currGroup);
            
            if (this.getCommonParameter().getMkMode())
            {
                log.info(this.getText(
                    "B2BPC0207",
                    new String[] {groupProfile.getGroupId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            else
            {
                log.info(this.getText(
                    "B2BPC0206",
                    new String[] {groupProfile.getGroupId(),
                        this.getLoginIdOfCurrentUser()}));
            }
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    // *****************************************************
    // action confirm page
    // *****************************************************
    public String checkConfirm()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal groupOid = new BigDecimal(part);
                
                GroupTmpHolder group = groupTmpService.selectGroupTmpByKey(groupOid);
                if (!MkCtrlStatus.PENDING.equals(group.getCtrlStatus()))
                {
                    this.ajaxMsg = this.getText("B2BPC0236");
                    break;
                }
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.ajaxMsg = this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo});
        }
        
        if (StringUtils.isBlank(ajaxMsg))
        {
            ajaxMsg = "pass";
        }
        
        return SUCCESS;
    }
    
    
    public String initConfirm()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            createList = new ArrayList<GroupTmpHolder>();
            updateList = new ArrayList<GroupTmpHolder>();
            deleteList = new ArrayList<GroupTmpHolder>();
            
            for (String part : parts)
            {
                BigDecimal groupOid = new BigDecimal(part);
                GroupTmpExHolder currTmpGroup = new GroupTmpExHolder();
                BeanUtils.copyProperties(groupTmpService.selectGroupTmpByKey(groupOid), currTmpGroup);
                
                UserTypeHolder userType = userTypeService.selectByKey(currTmpGroup.getUserTypeOid());
                currTmpGroup.setUserTypeId(userType.getUserTypeDesc());
                currTmpGroup.setGroupTypeValue(this.getText(currTmpGroup.getGroupType().getKey()));
                currTmpGroup.setActionTypeValue(this.getText(currTmpGroup.getActionType().getKey()));
                currTmpGroup.setCtrlStatusValue(this.getText(currTmpGroup.getCtrlStatus().getKey()));
                
                boolean isBuyer = this.isBuyer(currTmpGroup.getUserTypeOid());
                boolean isSupplier = this.isSupplier(currTmpGroup.getUserTypeOid());
                if (isBuyer)
                {
                    BigDecimal buyerOid = currTmpGroup.getBuyerOid();
                    BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
                    currTmpGroup.setCompany(buyer.getBuyerName());
                    List<SupplierHolder> suppliers = null;
                    List<GroupSupplierTmpHolder> allGroupSuppliers = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(groupOid);
                    if (allGroupSuppliers != null && allGroupSuppliers.size() == 1 
                            && allGroupSuppliers.get(0).getSupplierOid().equals(BigDecimal.valueOf(-1)))
                    {
                        suppliers = initAllSupplier();
                    }
                    else
                    {
                        suppliers = supplierService.selectSupplierByTmpGroupOidAndBuyerOid(groupOid, buyerOid);
                    }
                    sortSupplier(suppliers);
                    currTmpGroup.setSuppliers(suppliers);
                }
                
                if (isSupplier)
                {
                    BigDecimal supplierOid = currTmpGroup.getSupplierOid();
                    SupplierHolder supplier = supplierService.selectSupplierByKey(supplierOid);
                    currTmpGroup.setCompany(supplier.getSupplierName());
                    List<TradingPartnerHolder> tps = new ArrayList<TradingPartnerHolder>();
                    List<GroupTradingPartnerTmpHolder> allGroupTps = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(groupOid);
                    if (allGroupTps != null && allGroupTps.size() == 1 
                            && allGroupTps.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                    {
                        tps = initAllTradingPartner();
                    }
                    else
                    {
                        tps = this.getTradingPartnerByGroupOid(currTmpGroup, false);
                    }
                    sortTradingPartner(tps);
                    currTmpGroup.setTradingPartners(tps);
                }
                
                List<UserProfileTmpExHolder> users = this.getTmpUsers(groupOid);
                if (users == null) users = new ArrayList<UserProfileTmpExHolder>();
                currTmpGroup.setUsers(users);
                
                List<RoleHolder> roles = roleService.selectRolesByTmpGroupOid(groupOid);
                if (roles == null) roles = new ArrayList<RoleHolder>();
                currTmpGroup.setRoles(roles);
                
                if(DbActionType.CREATE.equals(currTmpGroup.getActionType()))
                {
                    createList.add(currTmpGroup);
                    continue;
                }
                
                if(DbActionType.DELETE.equals(currTmpGroup.getActionType()))
                {
                    deleteList.add(currTmpGroup);
                    continue;
                }
                
                
                GroupTmpExHolder oldGroup = new GroupTmpExHolder();
                GroupHolder group = groupService.selectGroupByKey(groupOid);
                BeanUtils.copyProperties(group, oldGroup);
                
                if (isBuyer)
                {
                    BigDecimal buyerOid = oldGroup.getBuyerOid();
                    BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
                    oldGroup.setCompany(buyer.getBuyerName());
                    List<SupplierHolder> suppliers = null;
                    List<GroupSupplierHolder> allGroupSuppliers = groupSupplierService.selectGroupSupplierByGroupOid(groupOid);
                    if (allGroupSuppliers != null && allGroupSuppliers.size() == 1 
                            && allGroupSuppliers.get(0).getSupplierOid().equals(BigDecimal.valueOf(-1)))
                    {
                        suppliers = initAllSupplier();
                    }
                    else
                    {
                        suppliers = supplierService.selectSupplierByGroupOidAndBuyerOid(groupOid, buyerOid);
                    }
                    sortSupplier(suppliers);
                    oldGroup.setSuppliers(suppliers);
                }
                
                if (isSupplier)
                {
                    BigDecimal supplierOid = oldGroup.getSupplierOid();
                    SupplierHolder supplier = supplierService.selectSupplierByKey(supplierOid);
                    oldGroup.setCompany(supplier.getSupplierName());
                    List<TradingPartnerHolder> tps = new ArrayList<TradingPartnerHolder>();
                    List<GroupTradingPartnerHolder> allGroupTps = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(groupOid);
                    if (allGroupTps != null && allGroupTps.size() == 1 
                            && allGroupTps.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                    {
                        tps = initAllTradingPartner();
                    }
                    else
                    {
                        tps = getTradingPartnerByGroupOid(oldGroup, true);
                    }
                    sortTradingPartner(tps);
                    oldGroup.setTradingPartners(tps);
                }
                
                List<UserProfileTmpExHolder> oldUsers = this.getUsers(groupOid);
                oldUsers = new ArrayList<UserProfileTmpExHolder>();
                oldGroup.setUsers(oldUsers);
                
                List<RoleHolder> oldRoles = roleService.selectRolesByGroupOid(groupOid);
                if (oldRoles == null) oldRoles = new ArrayList<RoleHolder>();
                oldGroup.setRoles(oldRoles);
                
                currTmpGroup.setOldVersion(oldGroup);
                
                updateList.add(currTmpGroup);
                
            }
            
            
            String reqUrl = StringUtils.remove(this.getRequest()
                .getRequestURI(), this.getRequest().getContextPath());
            
            if ("/group/initWithdraw.action".equals(reqUrl))
            {
                confirmType = "W";
            }
            else if ("/group/initReject.action".equals(reqUrl))
            {
                confirmType = "R";
            }
            else if ("/group/initApprove.action".equals(reqUrl))
            {
                confirmType = "A";
            }
        
        }
        catch(Exception e)
        {
            this.handleException(e);
        }
        
        
        return SUCCESS;
    }
    
    // *****************************************************
    // approve function
    // *****************************************************
    public String saveApprove()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            MessageTargetHolder mt = this.getMessage(selectedOids);
            
            if (null == selectedOids)
            {
                return FORWARD_COMMON_MESSAGE;
            }
        
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal groupOid = new BigDecimal(part);
                GroupTmpHolder oldGroup = groupTmpService.selectGroupTmpByKey(groupOid);
            
                if (!MkCtrlStatus.PENDING.equals(oldGroup.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPC0231", new String[]{oldGroup.getGroupId()}));
                    continue;
                }
                
                if (oldGroup.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser()))
                {
                    msg.saveError(this.getText("B2BPU0159", new String[]{oldGroup.getGroupId()}));
                    continue;
                }
                
                List<GroupUserTmpHolder> users = groupUserTmpService.selectGroupUserTmpByGroupOid(groupOid);
                BigDecimal companyOid = this.getCompanyOid(oldGroup);
                String userName = checkUserCompanySameToGroup(users, companyOid);
                if (userName != null)
                {
                    msg.saveError(this.getText("B2BPC0238", new String[]{userName,oldGroup.getGroupId()}));
                    continue;
                }
                
                if (users != null)
                {
                    oldGroup.addGroupUsers(users);
                }
                
                List<RoleGroupTmpHolder> roles = roleGroupTmpService.selectRoleGroupTmpByGroupOid(groupOid);
                
                if (roles != null)
                {
                    oldGroup.addRoleGroups(roles);
                }
                
                List<GroupSupplierTmpHolder> suppliers = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(groupOid);
                if (suppliers != null)
                {
                    oldGroup.addGroupSuppliers(suppliers);
                }
                
                List<GroupTradingPartnerTmpHolder> tps = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(groupOid);
                if (tps != null)
                {
                    oldGroup.addGroupTradingPartners(tps);
                }
                
                groupService.approveGroupProfile(this.getCommonParameter(), oldGroup);
                msg.saveSuccess(this.getText("B2BPC0226", new String[]{oldGroup.getGroupId()}));
                
                log.info(this.getText(
                    "B2BPC0208",
                    new String[] {oldGroup.getGroupId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            
            msg.addMessageTarget(mt);
        }
        catch(Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // *****************************************************
    // reject function
    // *****************************************************
    public String saveReject()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            MessageTargetHolder mt = this.getMessage(selectedOids);
            
            if (null == selectedOids)
            {
                return FORWARD_COMMON_MESSAGE;
            }
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal groupOid = new BigDecimal(part);
                GroupTmpHolder oldGroup = groupTmpService.selectGroupTmpByKey(groupOid);
            
                if (!MkCtrlStatus.PENDING.equals(oldGroup.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPC0224", new String[]{oldGroup.getGroupId()}));
                    continue;
                }
                
                if (oldGroup.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser()))
                {
                    msg.saveError(this.getText("B2BPC0232", new String[]{oldGroup.getGroupId()}));
                    continue;
                }
                
                List<RoleGroupTmpHolder> roles = roleGroupTmpService.selectRoleGroupTmpByGroupOid(groupOid);
                
                if (roles != null)
                {
                    oldGroup.addRoleGroups(roles);
                }
                
                List<GroupSupplierTmpHolder> suppliers = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(groupOid);
                if (suppliers != null)
                {
                    oldGroup.addGroupSuppliers(suppliers);
                }
                
                List<GroupUserTmpHolder> users = groupUserTmpService.selectGroupUserTmpByGroupOid(groupOid);
                if (users != null)
                {
                    oldGroup.addGroupUsers(users);
                }
                
                List<GroupTradingPartnerTmpHolder> tps = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(groupOid);
                if (tps != null)
                {
                    oldGroup.addGroupTradingPartners(tps);
                }
                
                groupService.rejectGroupProfile(this.getCommonParameter(), oldGroup);
                msg.saveSuccess(this.getText("B2BPC0227", new String[]{oldGroup.getGroupId()}));
                
                log.info(this.getText(
                    "B2BPC0209",
                    new String[] {oldGroup.getGroupId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            
            msg.addMessageTarget(mt);
        }
        catch(Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // *****************************************************
    // withdraw function
    // *****************************************************
    public String saveWithdraw()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            MessageTargetHolder mt = this.getMessage(selectedOids);
            
            if (null == selectedOids)
            {
                return FORWARD_COMMON_MESSAGE;
            }
        
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal groupOid = new BigDecimal(part);
                GroupTmpHolder oldGroup = groupTmpService.selectGroupTmpByKey(groupOid);
            
                if (!MkCtrlStatus.PENDING.equals(oldGroup.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPC0225", new String[]{oldGroup.getGroupId()}));
                    continue;
                }
                
                if (!oldGroup.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser()))
                {
                    msg.saveError(this.getText("B2BPC0233", new String[]{oldGroup.getGroupId()}));
                    continue;
                }
                
                List<RoleGroupTmpHolder> roles = roleGroupTmpService.selectRoleGroupTmpByGroupOid(groupOid);
                
                if (roles != null)
                {
                        oldGroup.addRoleGroups(roles);
                }
                
                List<GroupSupplierTmpHolder> suppliers = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(groupOid);
                if (suppliers != null)
                {
                        oldGroup.addGroupSuppliers(suppliers);
                }
                
                List<GroupUserTmpHolder> users = groupUserTmpService.selectGroupUserTmpByGroupOid(groupOid);
                if (users != null)
                {
                    oldGroup.addGroupUsers(users);
                }
                
                List<GroupTradingPartnerTmpHolder> tps = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(groupOid);
                if (tps != null)
                {
                    oldGroup.addGroupTradingPartners(tps);
                }
                
                groupService.withdrawGroupProfile(this.getCommonParameter(), oldGroup);
                msg.saveSuccess(this.getText("B2BPC0228", new String[]{oldGroup.getGroupId()}));
                
                log.info(this.getText(
                    "B2BPC0210",
                    new String[] {oldGroup.getGroupId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            
            msg.addMessageTarget(mt);
        }
        catch(Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // *****************************************************
    // delete function
    // *****************************************************
    public String saveDelete()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            MessageTargetHolder mt = this.getMessage(selectedOids);
            
            if (null == selectedOids)
            {
                return FORWARD_COMMON_MESSAGE;
            }
        
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal groupOid = new BigDecimal(part);
                GroupTmpHolder oldGroup = groupTmpService.selectGroupTmpByKey(groupOid);
            
                if (MkCtrlStatus.PENDING.equals(oldGroup.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPC0234", new String[]{oldGroup.getGroupId()}));
                    continue;
                }
                
                boolean invalid = false;
                List<GroupUserTmpHolder> users = groupUserTmpService.selectGroupUserTmpByGroupOid(groupOid);
                if (users != null)
                {
                    for (GroupUserTmpHolder obj : users)
                    {
                        if (!obj.getApproved()
                            && LastUpdateFrom.USER.equals(obj.getLastUpdateFrom()))
                        {
                            invalid = true;
                            break;
                        }
                    }
                    
                    oldGroup.addGroupUsers(users);
                }
                
                if (invalid)
                {
                    msg.saveError(this.getText("B2BPC0237", new String[]{oldGroup.getGroupId()}));
                    continue;
                }
                
                List<RoleGroupTmpHolder> roles = roleGroupTmpService.selectRoleGroupTmpByGroupOid(groupOid);
                
                if (roles != null)
                {
                    oldGroup.addRoleGroups(roles);
                }
                
                List<GroupSupplierTmpHolder> suppliers = groupSupplierTmpService.selectGroupSupplierTmpByGroupOid(groupOid);
                if (suppliers != null)
                {
                    oldGroup.addGroupSuppliers(suppliers);
                }
                
                
                List<GroupTradingPartnerTmpHolder> tps = groupTradingPartnerTmpService.selectGroupTradingPartnerTmpByGroupOid(groupOid);
                if (tps != null)
                {
                    oldGroup.addGroupTradingPartners(tps);
                }
                
                groupService.removeGroupProfile(this.getCommonParameter(), oldGroup);

                if (this.getCommonParameter().getMkMode())
                {
                    log.info(this.getText(
                        "B2BPC0202",
                        new String[] {oldGroup.getGroupId(),
                            this.getLoginIdOfCurrentUser()}));
                    msg.saveSuccess(this.getText("B2BPC0229", new String[]{oldGroup.getGroupId()}));
                }
                else
                {
                    log.info(this.getText(
                        "B2BPC0201",
                        new String[] {oldGroup.getGroupId(),
                            this.getLoginIdOfCurrentUser()}));
                    msg.saveSuccess(this.getText("B2BPC0235", new String[]{oldGroup.getGroupId()}));
                }
            }
            
            msg.addMessageTarget(mt);
        }
        catch(Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // *****************************************************
    // popup user summary page
    // *****************************************************
    public String initViewUser()
    {
        try
        {
            clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_USER);
            
            if (userProfile == null)
            {
                userProfile = (UserProfileExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_USER);
            }
            else
            {
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_USER, userProfile);
            }
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    

    public String searchUser()
    {
        if (null == userProfile)
        {
            userProfile = new UserProfileExHolder();
        }
        try
        {
            userProfile.trimAllString();
            userProfile.setAllEmptyStringToNull();
            if (userProfile.getUserType() == null
                    || (userProfile.getUserType().equals(BigDecimal.valueOf(-1))))
            {
                userProfile.setCurrentUserType(this.getUserTypeOfCurrentUser());
            }
            
            //userProfile.setBlocked(false);
            //userProfile.setActive(true);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        getSession()
                .put(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_USER, userProfile);

        return SUCCESS;
    }

    
    public String userData() throws Exception
    {
        try
        {
            
            UserProfileExHolder searchParam = (UserProfileExHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_USER);
            if (null == searchParam)
            {
                searchParam = new UserProfileExHolder();
                searchParam.setCurrentUserType(this.getUserTypeOfCurrentUser());
            }
            this.obtainListRecordsOfPagination(userProfileService,
                searchParam, userSortMap, "userOid", null);

        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }

    
    // *****************************************************
    // popup suppliers summary page
    // *****************************************************
    public String initViewSupplier()
    {
        try
        {
            clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_SUPPLIER);
            
            if (supplier == null && (SupplierExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_SUPPLIER) == null)
            {
                supplier = new SupplierExHolder();
            }
            getSession().remove(CommonConstants.SESSION_CHANGED);
            getSession()
                    .put(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_SUPPLIER, supplier);
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    

    public String searchSupplier()
    {
        if (null == supplier)
        {
            supplier = new SupplierExHolder();
        }
        try
        {
            supplier.trimAllString();
            supplier.setAllEmptyStringToNull();
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        getSession().remove(CommonConstants.SESSION_CHANGED);
        getSession()
                .put(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_SUPPLIER, supplier);

        return SUCCESS;
    }

    
    public String supplierData() throws Exception
    {
        try
        {
            
            SupplierExHolder searchParam = (SupplierExHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_SUPPLIER);
            
            if (null == searchParam)
            {
                searchParam = new SupplierExHolder();
            }
            searchParam.setActive(true);
            if (!BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser()))
            {
                searchParam.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            searchParam.setCurrentUserTypeOid(this.getUserTypeOfCurrentUser());
            searchParam.setCurrentUserOid(this.getProfileOfCurrentUser().getUserOid());
            this.obtainListRecordsOfPagination(popupSupplierService,
                searchParam, suppStortMap, "supplierOid", null);

        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    public String searchAllSuppliersForGroup() throws Exception
    {
        //system admin create buyer group
        if (this.getUserTypeOfCurrentUser().equals(BigDecimal.ONE))
        {
            suppliers = supplierService.selectSupplierByBuyerOid(supplier.getBuyerOid());
        }
        //buyer admin create buyer group
        else
        {
            suppliers = new ArrayList<SupplierHolder>();
            GroupHolder group = groupService.selectGroupByUserOid(this.getProfileOfCurrentUser().getUserOid());
            if (group == null)
            {
                return SUCCESS;
            }
            List<GroupSupplierHolder> groupSuppliers = groupSupplierService.selectGroupSupplierByGroupOid(group.getGroupOid());
            if (groupSuppliers == null || groupSuppliers.isEmpty())
            {
                return SUCCESS;
            }
            if (groupSuppliers.size() == 1 && groupSuppliers.get(0).getSupplierOid().compareTo(BigDecimal.valueOf(-1)) == 0)
            {
                suppliers = supplierService.selectSupplierByBuyerOid(supplier.getBuyerOid());
            }
            else
            {
                suppliers = supplierService.selectSupplierByBuyerOidAndUserOid(supplier.getBuyerOid(), 
                        this.getProfileOfCurrentUser().getUserOid());
            }
        }
        return SUCCESS;
    }
    

    // *****************************************************
    // popup trading partner summary page
    // *****************************************************
    public String initViewTradingPartner()
    {
        try
        {
            clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_TRADING_PARTNER);
            
            if (tradingPartner == null)
            {
                tradingPartner = (TradingPartnerExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_TRADING_PARTNER);
            }
            else
            {
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_TRADING_PARTNER, tradingPartner);
            }
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    

    public String searchTradingPartner()
    {
        if (null == tradingPartner)
        {
            tradingPartner = new TradingPartnerExHolder();
        }
        try
        {
            tradingPartner.trimAllString();
            tradingPartner.setAllEmptyStringToNull();
            //tradingPartner.setActive(true);
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        getSession()
                .put(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_TRADING_PARTNER, tradingPartner);

        return SUCCESS;
    }


    public String tradingPartnerData() throws Exception
    {
        try
        {
            
            TradingPartnerExHolder searchParam = (TradingPartnerExHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_CREATE_GROUP_POPUP_TRADING_PARTNER);
            
            if (null == searchParam)
            {
                searchParam = new TradingPartnerExHolder();
                if (!BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser()))
                {
                    searchParam.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                }
            }
            
            if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
            {
                GroupHolder group = groupService.selectGroupByUserOid(this.getProfileOfCurrentUser().getUserOid());
                
                if (group != null)
                {
                    List<GroupTradingPartnerHolder> groupTradingPartners = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(group.getGroupOid());
                    if (groupTradingPartners != null)
                    {
                        if (groupTradingPartners.size() == 1 
                                && groupTradingPartners.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                        {
                            List<TradingPartnerExHolder> tpList = this.getAllTradingPartners(searchParam);
                            if (tpList != null)
                            {
                                for (TradingPartnerExHolder tp : tpList)
                                {
                                    searchParam.addTradingPartnerOid(tp.getTradingPartnerOid());
                                }
                            }
                        }
                        else
                        {
                            for (GroupTradingPartnerHolder groupTradingPartner : groupTradingPartners)
                            {
                                searchParam.addTradingPartnerOid(groupTradingPartner.getTradingPartnerOid());
                            }
                        }
                    }
                }
            }
            this.obtainListRecordsOfPagination(tradingPartnerService,
                searchParam, tradingPartnerStortMap, "tradingPartnerOid", null);

        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    public String searchAllTradingPartnersForGroup() throws Exception
    {
        TradingPartnerExHolder param = new TradingPartnerExHolder();
        param.setSupplierOid(tradingPartner.getSupplierOid());
        if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
        {
            GroupHolder group = groupService.selectGroupByUserOid(this.getProfileOfCurrentUser().getUserOid());
            if (group != null)
            {
                List<GroupTradingPartnerHolder> groupTradingPartners = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(group.getGroupOid());
                if (groupTradingPartners != null && groupTradingPartners.size() == 1 
                        && groupTradingPartners.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                {
                    tradingPartners = this.getAllTradingPartners(param);
                    return SUCCESS;
                }
                if (groupTradingPartners != null && !groupTradingPartners.isEmpty())
                {
                    for (GroupTradingPartnerHolder groupTradingPartner : groupTradingPartners)
                    {
                        param.addTradingPartnerOid(groupTradingPartner.getTradingPartnerOid());
                    }
                }
            }
        }
        tradingPartners = this.getAllTradingPartners(param);
        return SUCCESS;
    }
    
    
    private List<TradingPartnerExHolder> getAllTradingPartners(TradingPartnerExHolder param) throws Exception
    {
        int count = tradingPartnerService.getCountOfSummary(param);
        param.setStartRecordNum(0);
        param.setNumberOfRecordsToSelect(count);
        return tradingPartnerService.getListOfSummary(param);
    }

    
    // *****************************************************
    // private method
    // *****************************************************
    private void initUserTypes() throws Exception
    {
        List<UserTypeHolder> tmpUserTypes = new ArrayList<UserTypeHolder>();
        List<UserTypeHolder> allowedUserTypes = userTypeService.selectPrivilegedSubUserTypesByUserType(this.getUserTypeOfCurrentUser());
        if (allowedUserTypes != null && !allowedUserTypes.isEmpty())
        {
            if (BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser()))
            {
                for (UserTypeHolder userType : allowedUserTypes)
                {
                    if (BigDecimal.valueOf(2).equals(userType.getUserTypeOid()) 
                            || BigDecimal.valueOf(3).equals(userType.getUserTypeOid())
                            || BigDecimal.valueOf(4).equals(userType.getUserTypeOid())
                            || BigDecimal.valueOf(5).equals(userType.getUserTypeOid()))
                    {
                        tmpUserTypes.add(userType);
                    }
                }
            }
            else if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
            {
                for (UserTypeHolder userType : allowedUserTypes)
                {
                    if (BigDecimal.valueOf(3).equals(userType.getUserTypeOid()) 
                            || BigDecimal.valueOf(4).equals(userType.getUserTypeOid())
                            || BigDecimal.valueOf(5).equals(userType.getUserTypeOid()))
                    {
                        tmpUserTypes.add(userType);
                    }
                }
            }
            else if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
            {
                for (UserTypeHolder userType : allowedUserTypes)
                {
                    if (BigDecimal.valueOf(5).equals(userType.getUserTypeOid()))
                    {
                        tmpUserTypes.add(userType);
                    }
                }
            }
            else if (BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser()))
            {
                for (UserTypeHolder userType : allowedUserTypes)
                {
                    if (BigDecimal.valueOf(7).equals(userType.getUserTypeOid()))
                    {
                        tmpUserTypes.add(userType);
                    }
                }
            }
        }
        userTypes = tmpUserTypes;
    }
    
    
    private void initBuyerSupplierInfo(UserProfileHolder user) throws Exception
    {
        groupProfile = groupProfile == null ? new GroupTmpExHolder() : groupProfile;
        
        if (user.getBuyerOid() != null)
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(user.getBuyerOid());
            
            groupProfile.setBuyerName(buyer == null ? null : buyer.getBuyerName());
        }
        
        if (user.getSupplierOid() != null)
        {
            SupplierHolder supplier = supplierService.selectSupplierByKey(user.getSupplierOid());
            
            groupProfile.setSupplierName(supplier == null ? null : supplier.getSupplierName());
        }
        
    }
    
    
    private void initPageDataForAddFailed() throws Exception
    {
        if (userTypes == null || userTypes.isEmpty())
        {
            initUserTypes();
        }

        buyers = buyerService.select(new BuyerHolder());
        if (this.getUserTypeOfCurrentUser().equals(BigDecimal.ONE))
        {
            suppliers = supplierService.select(new SupplierExHolder());
        }
        else if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
        {
            suppliers = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        }
        else if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
        {
            SupplierExHolder param = new SupplierExHolder();
            param.setSupplierOid(groupProfile.getSupplierOid());
            suppliers = supplierService.select(param);
        }
        this.initBuyerSupplierInfo(this.getProfileOfCurrentUser());
        
        this.initRolesByConditionFromGroupProfile();
        if(availabelRoles == null)
            availabelRoles = new ArrayList<Object>();
        
        if(!selectedRoles.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            for(Object obj : availabelRoles)
            {
                RoleHolder role = (RoleHolder)obj;
            
                if (selectedRoles.contains(role.getRoleOid().toString()))
                {
                    tmpList.add(role);
                }
               
            }
            availabelRoles.removeAll(tmpList);
            selectedRoles = tmpList;
        }
        
        if (!selectedSupps.isEmpty())
        {
            if (selectedSupps.size() == 1 
                    && BigDecimal.valueOf(-1).equals(new BigDecimal((String) selectedSupps.get(0))))
            {
                selectedSupps = initAllSupplier();
            }
            else
            {
                List<BigDecimal> oids = new ArrayList<BigDecimal>();
                for (Object obj : selectedSupps)
                {
                    String oid = (String)obj;
                    oids.add(new BigDecimal(oid));
                }
                
                selectedSupps = this.supplierService.selectSupplierBySupplierOids(oids);
            }
        }
        
        if (!selectedTps.isEmpty())
        {
            if (selectedTps.size() == 1 
                    && BigDecimal.valueOf(-1).equals(new BigDecimal((String) selectedTps.get(0))))
            {
                
                selectedTps = initAllTradingPartner();
            }
            else
            {
                TradingPartnerExHolder param = new TradingPartnerExHolder();
                for (Object obj : selectedTps)
                {
                    String oid = (String)obj;
                    param.addTradingPartnerOid(new BigDecimal(oid));
                }
                selectedTps.clear();
                selectedTps = this.getTradingPartners(param);
            }
        }
        
        if (!selectedUsers.isEmpty())
        {
            List<BigDecimal> oids = new ArrayList<BigDecimal>();
            for (Object obj : selectedUsers)
            {
                String oid = (String)obj;
                oids.add(new BigDecimal(oid));
            }
            
            selectedUsers = userProfileTmpService.selectUsersByUserOids(oids);
        
        }
        
        
        if(selectedRoles == null)
            selectedRoles = new ArrayList<Object>();
        
        if(selectedSupps == null)
            selectedSupps = new ArrayList<Object>();
        selectedTps = new ArrayList<Object>();
        if(selectedUsers == null)
            selectedUsers = new ArrayList<Object>();
        if(buyers == null)
            buyers = new ArrayList<Object>();
        if(suppliers == null)
            suppliers = new ArrayList<Object>();
        sortSupplier(selectedSupps);
        sortTradingPartner(selectedTps);
    }
    
    
    private void initPageDataForEditFailed() throws Exception
    {
        param = new GroupTmpExHolder();
        param.setGroupOid(oldGroupProfile.getGroupOid());
        
        UserTypeHolder userType = this.userTypeService.selectByKey(oldGroupProfile.getUserTypeOid());
        groupProfile.setUserTypeId(userType.getUserTypeDesc());
        groupProfile.setUserTypeOid(userType.getUserTypeOid());
        
        buyers = buyerService.select(new BuyerHolder());
        if (this.getUserTypeOfCurrentUser().equals(BigDecimal.ONE))
        {
            suppliers = supplierService.select(new SupplierExHolder());
        }
        else if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
        {
            suppliers = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        }
        else if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
        {
            SupplierExHolder param = new SupplierExHolder();
            param.setSupplierOid(oldGroupProfile.getSupplierOid());
            suppliers = supplierService.select(param);
        }
        boolean isBuyer = this.isBuyer(oldGroupProfile.getUserTypeOid());
        boolean isSupplier = this.isSupplier(oldGroupProfile.getUserTypeOid());
        if (isBuyer)
        {
            BuyerHolder buyer = this.buyerService.selectBuyerByKey(oldGroupProfile.getBuyerOid());
            groupProfile.setBuyerName(buyer.getBuyerName());
        }
        
        if (isSupplier)
        {
            SupplierHolder supplier = this.supplierService.selectSupplierByKey(oldGroupProfile.getSupplierOid());
            groupProfile.setSupplierName(supplier.getSupplierName());
        }
        
        this.initRolesByConditionFromGroupProfile();

        if(availabelRoles == null)
            availabelRoles = new ArrayList<Object>();
        
        if(!selectedRoles.isEmpty())
        {
            List<Object> tmpList = new ArrayList<Object>();
            for(Object obj : availabelRoles)
            {
                RoleHolder role = (RoleHolder)obj;
                if (selectedRoles.contains(role.getRoleOid().toString()))
                {
                    tmpList.add(role);
                }
               
            }
            availabelRoles.removeAll(tmpList);
            selectedRoles = tmpList;
        }
        
        if (!selectedSupps.isEmpty())
        {
            if (selectedSupps.size() == 1 
                    && BigDecimal.valueOf(-1).equals(new BigDecimal((String) selectedSupps.get(0))))
            {
                selectedSupps = initAllSupplier();
            }
            else
            {
                List<BigDecimal> oids = new ArrayList<BigDecimal>();
                for (Object obj : selectedSupps)
                {
                    String oid = (String)obj;
                    oids.add(new BigDecimal(oid));
                }
                
                selectedSupps = this.supplierService.selectSupplierBySupplierOids(oids);
            }
        }
        
        if (!selectedTps.isEmpty())
        {
            if (selectedTps.size() == 1 
                    && BigDecimal.valueOf(-1).equals(new BigDecimal((String) selectedTps.get(0))))
            {
                selectedTps = initAllTradingPartner();
            }
            else
            {
                TradingPartnerExHolder param = new TradingPartnerExHolder();
                for (Object obj : selectedTps)
                {
                    String oid = (String)obj;
                    param.addTradingPartnerOid(new BigDecimal(oid));
                }
                selectedTps.clear();
                selectedTps = this.getTradingPartners(param);
            }
        }
        
        if (!selectedUsers.isEmpty())
        {
            List<BigDecimal> oids = new ArrayList<BigDecimal>();
            for (Object obj : selectedUsers)
            {
                String oid = (String)obj;
                oids.add(new BigDecimal(oid));
            }
            
            selectedUsers = this.getTmpUsers(oldGroupProfile.getGroupOid(), oids);
        }
        
        
        if(selectedRoles == null)
            selectedRoles = new ArrayList<Object>();
        
        if(selectedSupps == null)
            selectedSupps = new ArrayList<Object>();
        if(selectedTps == null)
            selectedTps = new ArrayList<Object>();
        selectedUsers = new ArrayList<Object>();
        if(buyers == null)
            buyers = new ArrayList<Object>();
        if(suppliers == null)
            suppliers = new ArrayList<Object>();
        sortSupplier(selectedSupps);
        sortTradingPartner(selectedTps);
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
    
    
    private MessageTargetHolder getMessage(Object selectedOids)
    {
        msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
        
        if (null == selectedOids)
        {
            msg.saveError(this.getText("B2BPC0230"));
            msg.addMessageTarget(mt);
            log.info(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
        }
        
        this.getSession().remove(SESSION_OID_PARAMETER);
        
        return mt;
    }
    
    
    private List<Object> getTradingPartners(TradingPartnerExHolder tradingPartner) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        List<TradingPartnerExHolder> rlts = tradingPartnerService.selectTradingPartnerTradingPartnerOids(tradingPartner);

        for (TradingPartnerExHolder tp : rlts)
        {
            this.initTradingPartnerDesc(tp);
            list.add(tp);
        }
        
        return list;
    }
    
    
    private void initRolesByConditionFromUserType(UserTypeHolder ut) throws Exception
    {
        if (ut.getUserTypeOid().equals(BigDecimal.valueOf(1)))
        {
            availabelRoles = roleService.selectRolesByUserType(ut
                    .getUserTypeOid());
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(2))
                && this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(1)))
        {
            if (buyers == null || buyers.isEmpty()) return;
                
            BuyerHolder buyer = (BuyerHolder) buyers.get(0);

            if (buyer != null)
            {
                availabelRoles = roleService
                .selectBuyerRolesByBuyerOidAndUserType(
                        buyer.getBuyerOid(), ut.getUserTypeOid());
            }
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(4)))
        {
            availabelRoles = roleService
                    .selectBuyerRolesByBuyerOidAndUserType(this
                            .getProfileOfCurrentUser().getBuyerOid(), ut
                            .getUserTypeOid());
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(3)))
        {
            if (suppliers == null || suppliers.isEmpty()) return;
            
            SupplierHolder supplier = (SupplierHolder) suppliers.get(0);

            if (supplier != null)
                availabelRoles = roleService
                        .selectSupplierRolesBySupplierOidAndUserType(
                                supplier.getSupplierOid(), ut.getUserTypeOid());
        }
        else if (ut.getUserTypeOid().equals(BigDecimal.valueOf(5)))
        {
            availabelRoles = roleService
                    .selectSupplierRolesBySupplierOidAndUserType(this
                            .getProfileOfCurrentUser().getSupplierOid(), ut
                            .getUserTypeOid());
        }
    }
    
    
    private void initRolesByConditionFromGroupProfile() throws Exception
    {
        if (groupProfile.getUserTypeOid().equals(BigDecimal.valueOf(1)))
        {
            availabelRoles = roleService
                    .selectRolesByUserType(groupProfile.getUserTypeOid());
        }
        else if (groupProfile.getUserTypeOid().equals(BigDecimal.valueOf(2)))
        {
            if (groupProfile.getBuyerOid() != null)
                availabelRoles = roleService
                    .selectBuyerRolesByBuyerOidAndUserType(
                        groupProfile.getBuyerOid(),
                        groupProfile.getUserTypeOid());
        }
        else if (groupProfile.getUserTypeOid().equals(BigDecimal.valueOf(4)))
        {
            if (groupProfile.getBuyerOid() != null)
                availabelRoles = roleService
                    .selectBuyerRolesByBuyerOidAndUserType(
                            groupProfile.getBuyerOid(),
                            groupProfile.getUserTypeOid());
        }
        else if (groupProfile.getUserTypeOid().equals(BigDecimal.valueOf(3)))
        {
            if (groupProfile.getSupplierOid() != null)
                availabelRoles = roleService
                        .selectSupplierRolesBySupplierOidAndUserType(
                            groupProfile.getSupplierOid(),
                            groupProfile.getUserTypeOid());
        }
        else if (groupProfile.getUserTypeOid().equals(BigDecimal.valueOf(5)))
        {
            if (groupProfile.getSupplierOid() != null)
                availabelRoles = roleService
                    .selectSupplierRolesBySupplierOidAndUserType(
                            groupProfile.getSupplierOid(),
                            groupProfile.getUserTypeOid());
        }
    }
    
    
    private List<TradingPartnerHolder> getTradingPartnerByGroupOid(GroupTmpExHolder group, boolean flag) throws Exception
    {
        
        List<TradingPartnerExHolder> rlts = null;
        if (flag)
        {
            rlts = tradingPartnerService.selectTradingPartnerByGroupOidAndSupplierOid(group.getGroupOid(), group.getSupplierOid());
        }
        else
        {
            rlts = tradingPartnerService.selectTradingPartnerByTmpGroupOidAndSupplierOid(group.getGroupOid(), group.getSupplierOid());
        }
        
        if (rlts == null)
        {
            return null;
        }
            
        List<TradingPartnerHolder> realRlt = new ArrayList<TradingPartnerHolder>();
        
        for (TradingPartnerExHolder tp : rlts)
        {
            this.initTradingPartnerDesc(tp);
            realRlt.add(tp);
        }
        
        return realRlt;
    }
    
    
    private void initTradingPartnerDesc(TradingPartnerExHolder tp)
    {
        String supplierName = tp.getSupplierName();
        String buyerName = tp.getBuyerName();
        String buyerSuppCode = tp.getBuyerSupplierCode();
        String suppBuyerCode = tp.getSupplierBuyerCode();
        
        StringBuffer desc = new StringBuffer();
        desc.append(buyerName);
        if (StringUtils.isNotBlank(suppBuyerCode))
        {
            desc.append("(" + suppBuyerCode + ")");
        }
        
        desc.append(" - ");
        desc.append(supplierName);
        
        if (StringUtils.isNotBlank(buyerSuppCode))
        {
            desc.append("(" + buyerSuppCode + ")");
        }
        
        tp.setTradingPartnerDesc(desc.toString());
        
    }
    
    
    private List<UserProfileTmpExHolder> getTmpUsers(BigDecimal groupOid) throws Exception
    {
        GroupTmpHolder groupTmp = groupTmpService.selectGroupTmpByKey(groupOid);
        List<GroupUserTmpHolder> groupUsers =  groupUserTmpService.selectGroupUserTmpByGroupOid(groupOid);
        Map<BigDecimal, GroupUserTmpHolder> groupUserMap = new HashMap<BigDecimal, GroupUserTmpHolder>();
        List<BigDecimal> userOids = new ArrayList<BigDecimal>();
        for (GroupUserTmpHolder groupUser : groupUsers)
        {
            //there are 2 cases can cause a t_group_user meet requirements for the first 3 conditions, one is edit a group ,delete a user, and another is 
            //delete the group,if the group is pending,when we edit the group again, the group user need not to display, but when view, it should display.
            if (!groupUser.getApproved() 
                && DbActionType.DELETE.equals(groupUser.getActionType())
                && LastUpdateFrom.GROUP.equals(groupUser.getLastUpdateFrom())
                && groupTmp.getActionType().equals(DbActionType.UPDATE))
            {
                continue;
            }
            userOids.add(groupUser.getUserOid());
            groupUserMap.put(groupUser.getUserOid(), groupUser);
        }
        
        if (userOids == null || userOids.isEmpty())
        {
            return null;
        }
        
        List<UserProfileTmpHolder> rlts = userProfileTmpService.selectUsersByUserOids(userOids);
        
        List<UserProfileTmpExHolder> users = new ArrayList<UserProfileTmpExHolder>();
        for (UserProfileTmpHolder userTmp : rlts)
        {
            UserProfileTmpExHolder user = new UserProfileTmpExHolder();
            BeanUtils.copyProperties(userTmp, user);
            GroupUserTmpHolder groupUser = groupUserMap.get(userTmp.getUserOid());
            if (!groupUser.getApproved()
                && LastUpdateFrom.USER.equals(groupUser.getLastUpdateFrom()))
            {
                user.setIsLocked(true);
            }
            users.add(user);
        }
        
        return users;
    }
    
    
    private List<UserProfileTmpExHolder> getUsers(BigDecimal groupOid) throws Exception
    {
        List<GroupUserTmpHolder> groupUsers =  groupUserTmpService.selectGroupUserTmpByGroupOid(groupOid);
        Map<BigDecimal, GroupUserTmpHolder> groupUserMap = new HashMap<BigDecimal, GroupUserTmpHolder>();
        List<BigDecimal> userOids = new ArrayList<BigDecimal>();
        for (GroupUserTmpHolder groupUser : groupUsers)
        {
            userOids.add(groupUser.getUserOid());
            groupUserMap.put(groupUser.getUserOid(), groupUser);
        }
        
        List<UserProfileHolder> rlts = userProfileService.selectUsersByGroupOid(groupOid);
        
        List<UserProfileTmpExHolder> users = new ArrayList<UserProfileTmpExHolder>();
        for (UserProfileHolder user : rlts)
        {
            UserProfileTmpExHolder userTmp = new UserProfileTmpExHolder();
            BeanUtils.copyProperties(user, userTmp);
            
            GroupUserTmpHolder groupUser = groupUserMap.get(userTmp.getUserOid());
            
            if (!groupUser.getApproved()
                && LastUpdateFrom.USER.equals(groupUser.getLastUpdateFrom()))
            {
                userTmp.setIsLocked(true);
            }
            users.add(userTmp);
        }
        
        return users;
    }
    
    
    private List<UserProfileTmpExHolder> getTmpUsers(BigDecimal groupOid, List<BigDecimal> userOids) throws Exception
    {
        List<GroupUserTmpHolder> groupUsers =  groupUserTmpService.selectGroupUserTmpByGroupOid(groupOid);
        Map<BigDecimal, GroupUserTmpHolder> groupUserMap = new HashMap<BigDecimal, GroupUserTmpHolder>();
        for (GroupUserTmpHolder groupUser : groupUsers)
        {
            if (!groupUser.getApproved() 
                && DbActionType.DELETE.equals(groupUser.getActionType())
                && LastUpdateFrom.GROUP.equals(groupUser.getLastUpdateFrom()))
            {
                continue;
            }
            groupUserMap.put(groupUser.getUserOid(), groupUser);
        }
        
        List<UserProfileTmpHolder> rlts = userProfileTmpService.selectUsersByUserOids(userOids);
        
        List<UserProfileTmpExHolder> users = new ArrayList<UserProfileTmpExHolder>();
        for (UserProfileTmpHolder userTmp : rlts)
        {
            UserProfileTmpExHolder user = new UserProfileTmpExHolder();
            BeanUtils.copyProperties(userTmp, user);
            GroupUserTmpHolder groupUser = groupUserMap.get(userTmp.getUserOid());
            
            if (groupUser != null 
                && !groupUser.getApproved()
                && LastUpdateFrom.USER.equals(groupUser.getLastUpdateFrom()))
            {
                user.setIsLocked(true);
            }
            users.add(user);
        }
        
        return users;
    }
    
    
    private BigDecimal getCompanyOid(GroupHolder group)
    {
        if (isBuyer(group.getUserTypeOid()))
        {
            return group.getBuyerOid();
        }
        
        if (isSupplier(group.getUserTypeOid()))
        {
            return group.getSupplierOid();
        }
        
        return null;
    }
    
    
    private String checkUserCompanySameToGroup(List<GroupUserTmpHolder> groupUsers, BigDecimal companyOid) throws Exception
    {
        List<BigDecimal> userOids = new ArrayList<BigDecimal>();
        for (GroupUserTmpHolder groupUser : groupUsers)
        {
            if (LastUpdateFrom.GROUP.equals(groupUser.getLastUpdateFrom()))
            {
                continue;
            }
            
            userOids.add(groupUser.getUserOid());
        }
        
        if (userOids.isEmpty())
        {
            return null;
        }
        
        List<UserProfileTmpHolder> users = userProfileTmpService.selectUsersByUserOids(userOids);
        if (users == null || users.isEmpty())
        {
            return null;
        }
        
        for (UserProfileTmpHolder user : users)
        {
            if (isBuyer(user.getUserType()))
            {
                BigDecimal userCompanyOid = user.getBuyerOid();
                
                if (!companyOid.toString().equals(userCompanyOid.toString()))
                {
                    return user.getUserName();
                }
            }

            
            if (isSupplier(user.getUserType()))
            {
                BigDecimal userCompanyOid = user.getSupplierOid();
                
                if (!companyOid.toString().equals(userCompanyOid.toString()))
                {
                    return user.getUserName();
                }
            }
        }
        
        return null;
    }
    
    private void sortSupplier(List<? extends Object> suppList)
    {
        Collections.sort(suppList, new Comparator<Object>()
                {

                    @Override
                    public int compare(Object o1, Object o2)
                    {
                        String suppName1 = ((SupplierHolder)o1).getSupplierName();
                        String suppName2 = ((SupplierHolder)o2).getSupplierName();
                        return suppName1.compareToIgnoreCase(suppName2);
                    }
            
                });
    }
    
    private void sortTradingPartner(List<? extends Object> tpList)
    {
        Collections.sort(tpList, new Comparator<Object>()
                {
            
            @Override
            public int compare(Object o1, Object o2)
            {
                String tpName1 = ((TradingPartnerExHolder)o1).getTradingPartnerDesc();
                String tpName2 = ((TradingPartnerExHolder)o2).getTradingPartnerDesc();
                return tpName1.compareToIgnoreCase(tpName2);
            }
            
                });
    }
    
    
    private List<SupplierHolder> initAllSupplier()
    {
        List<SupplierHolder> list = new ArrayList<SupplierHolder>();
        SupplierHolder supplier = new SupplierHolder();
        supplier.setSupplierName(this.getText("group.all.supplier.selected"));
        supplier.setSupplierOid(BigDecimal.valueOf(-1));
        list.add(supplier);
        return list;
    }
    
    
    private List<TradingPartnerHolder> initAllTradingPartner()
    {
        List<TradingPartnerHolder> list = new ArrayList<TradingPartnerHolder>();
        TradingPartnerExHolder tp = new TradingPartnerExHolder();
        tp.setTradingPartnerDesc(this.getText("group.all.tradingPartner.selected"));
        tp.setTradingPartnerOid(new BigDecimal(-1));
        list.add(tp);
        return list;
    }
    
    
    // *****************************************************
    // setter and getter method
    // *****************************************************

    /**
     * Getter of param.
     * 
     * @return Returns the param.
     */
    public GroupTmpExHolder getParam()
    {
        return param;
    }

    /**
     * Setter of param.
     * 
     * @param param The param to set.
     */
    public void setParam(GroupTmpExHolder param)
    {
        this.param = param;
    }

    /**
     * Getter of userTypes.
     * 
     * @return Returns the userTypes.
     */
    public List<? extends Object> getUserTypes()
    {
        return userTypes;
    }

    /**
     * Setter of userTypes.
     * 
     * @param userTypes The userTypes to set.
     */
    public void setUserTypes(List<Object> userTypes)
    {
        this.userTypes = userTypes;
    }

    /**
     * Getter of buyers.
     * 
     * @return Returns the buyers.
     */
    public List<? extends Object> getBuyers()
    {
        return buyers;
    }

    /**
     * Setter of buyers.
     * 
     * @param buyers The buyers to set.
     */
    public void setBuyers(List<Object> buyers)
    {
        this.buyers = buyers;
    }

    /**
     * Getter of suppliers.
     * 
     * @return Returns the suppliers.
     */
    public List<? extends Object> getSuppliers()
    {
        return suppliers;
    }

    /**
     * Setter of suppliers.
     * 
     * @param suppliers The suppliers to set.
     */
    public void setSuppliers(List<Object> suppliers)
    {
        this.suppliers = suppliers;
    }

    /**
     * Getter of availabelRoles.
     * 
     * @return Returns the availabelRoles.
     */
    public List<? extends Object> getAvailabelRoles()
    {
        return availabelRoles;
    }

    /**
     * Setter of availabelRoles.
     * 
     * @param availabelRoles The availabelRoles to set.
     */
    public void setAvailabelRoles(List<Object> availabelRoles)
    {
        this.availabelRoles = availabelRoles;
    }

    /**
     * Getter of selectedUsers.
     * 
     * @return Returns the selectedUsers.
     */
    public List<? extends Object> getSelectedUsers()
    {
        return selectedUsers;
    }

    /**
     * Setter of selectedUsers.
     * 
     * @param selectedUsers The selectedUsers to set.
     */
    public void setSelectedUsers(List<Object> selectedUsers)
    {
        this.selectedUsers = selectedUsers;
    }

    /**
     * Getter of selectedTps.
     * 
     * @return Returns the selectedTps.
     */
    public List<? extends Object> getSelectedTps()
    {
        return selectedTps;
    }

    /**
     * Setter of selectedTps.
     * 
     * @param selectedTps The selectedTps to set.
     */
    public void setSelectedTps(List<Object> selectedTps)
    {
        this.selectedTps = selectedTps;
    }

    /**
     * Getter of selectedSupps.
     * 
     * @return Returns the selectedSupps.
     */
    public List<? extends Object> getSelectedSupps()
    {
        return selectedSupps;
    }

    /**
     * Setter of selectedSupps.
     * 
     * @param selectedSupps The selectedSupps to set.
     */
    public void setSelectedSupps(List<Object> selectedSupps)
    {
        this.selectedSupps = selectedSupps;
    }

    /**
     * Getter of selectedRoles.
     * 
     * @return Returns the selectedRoles.
     */
    public List<? extends Object> getSelectedRoles()
    {
        return selectedRoles;
    }

    /**
     * Setter of selectedRoles.
     * 
     * @param selectedRoles The selectedRoles to set.
     */
    public void setSelectedRoles(List<Object> selectedRoles)
    {
        this.selectedRoles = selectedRoles;
    }

    /**
     * Getter of userProfile.
     * 
     * @return Returns the userProfile.
     */
    public UserProfileExHolder getUserProfile()
    {
        return userProfile;
    }

    /**
     * Setter of userProfile.
     * 
     * @param userProfile The userProfile to set.
     */
    public void setUserProfile(UserProfileExHolder userProfile)
    {
        this.userProfile = userProfile;
    }

    /**
     * Getter of groupProfile.
     * 
     * @return Returns the groupProfile.
     */
    public GroupTmpExHolder getGroupProfile()
    {
        return groupProfile;
    }

    /**
     * Setter of groupProfile.
     * 
     * @param groupProfile The groupProfile to set.
     */
    public void setGroupProfile(GroupTmpExHolder groupProfile)
    {
        this.groupProfile = groupProfile;
    }

    /**
     * Getter of supplier.
     * 
     * @return Returns the supplier.
     */
    public SupplierExHolder getSupplier()
    {
        return supplier;
    }

    /**
     * Setter of supplier.
     * 
     * @param supplier The supplier to set.
     */
    public void setSupplier(SupplierExHolder supplier)
    {
        this.supplier = supplier;
    }

    /**
     * Getter of tradingPartner.
     * 
     * @return Returns the tradingPartner.
     */
    public TradingPartnerExHolder getTradingPartner()
    {
        return tradingPartner;
    }

    /**
     * Setter of tradingPartner.
     * 
     * @param tradingPartner The tradingPartner to set.
     */
    public void setTradingPartner(TradingPartnerExHolder tradingPartner)
    {
        this.tradingPartner = tradingPartner;
    }


    /**
     * Getter of oldGroupProfile.
     * @return Returns the oldGroupProfile.
     */
    public GroupTmpExHolder getOldGroupProfile()
    {
        return oldGroupProfile;
    }


    /**
     * Setter of oldGroupProfile.
     * @param oldGroupProfile The oldGroupProfile to set.
     */
    public void setOldGroupProfile(GroupTmpExHolder oldGroupProfile)
    {
        this.oldGroupProfile = oldGroupProfile;
    }


    /**
     * Getter of oldSelectedRoles.
     * @return Returns the oldSelectedRoles.
     */
    public List<? extends Object> getOldSelectedRoles()
    {
        return oldSelectedRoles;
    }


    /**
     * Setter of oldSelectedRoles.
     * @param oldSelectedRoles The oldSelectedRoles to set.
     */
    public void setOldSelectedRoles(List<Object> oldSelectedRoles)
    {
        this.oldSelectedRoles = oldSelectedRoles;
    }


    /**
     * Getter of oldSelectedUsers.
     * @return Returns the oldSelectedUsers.
     */
    public List<? extends Object> getOldSelectedUsers()
    {
        return oldSelectedUsers;
    }


    /**
     * Setter of oldSelectedUsers.
     * @param oldSelectedUsers The oldSelectedUsers to set.
     */
    public void setOldSelectedUsers(List<Object> oldSelectedUsers)
    {
        this.oldSelectedUsers = oldSelectedUsers;
    }


    /**
     * Getter of oldSelectedTps.
     * @return Returns the oldSelectedTps.
     */
    public List<? extends Object> getOldSelectedTps()
    {
        return oldSelectedTps;
    }


    /**
     * Setter of oldSelectedTps.
     * @param oldSelectedTps The oldSelectedTps to set.
     */
    public void setOldSelectedTps(List<Object> oldSelectedTps)
    {
        this.oldSelectedTps = oldSelectedTps;
    }


    /**
     * Getter of oldSelectedSupps.
     * @return Returns the oldSelectedSupps.
     */
    public List<? extends Object> getOldSelectedSupps()
    {
        return oldSelectedSupps;
    }


    /**
     * Setter of oldSelectedSupps.
     * @param oldSelectedSupps The oldSelectedSupps to set.
     */
    public void setOldSelectedSupps(List<Object> oldSelectedSupps)
    {
        this.oldSelectedSupps = oldSelectedSupps;
    }


    /**
     * Getter of ctrlStatuses.
     * @return Returns the ctrlStatuses.
     */
    public Map<String, String> getCtrlStatuses()
    {
        return ctrlStatuses;
    }


    /**
     * Setter of ctrlStatuses.
     * @param ctrlStatuses The ctrlStatuses to set.
     */
    public void setCtrlStatuses(Map<String, String> ctrlStatuses)
    {
        this.ctrlStatuses = ctrlStatuses;
    }


    /**
     * Getter of createList.
     * @return Returns the createList.
     */
    public List<GroupTmpHolder> getCreateList()
    {
        return createList;
    }


    /**
     * Setter of createList.
     * @param createList The createList to set.
     */
    public void setCreateList(List<GroupTmpHolder> createList)
    {
        this.createList = createList;
    }


    /**
     * Getter of updateList.
     * @return Returns the updateList.
     */
    public List<GroupTmpHolder> getUpdateList()
    {
        return updateList;
    }


    /**
     * Setter of updateList.
     * @param updateList The updateList to set.
     */
    public void setUpdateList(List<GroupTmpHolder> updateList)
    {
        this.updateList = updateList;
    }


    /**
     * Getter of deleteList.
     * @return Returns the deleteList.
     */
    public List<GroupTmpHolder> getDeleteList()
    {
        return deleteList;
    }


    /**
     * Setter of deleteList.
     * @param deleteList The deleteList to set.
     */
    public void setDeleteList(List<GroupTmpHolder> deleteList)
    {
        this.deleteList = deleteList;
    }


    /**
     * Getter of confirmType.
     * @return Returns the confirmType.
     */
    public String getConfirmType()
    {
        return confirmType;
    }


    /**
     * Setter of confirmType.
     * @param confirmType The confirmType to set.
     */
    public void setConfirmType(String confirmType)
    {
        this.confirmType = confirmType;
    }


    /**
     * Getter of ajaxMsg.
     * @return Returns the ajaxMsg.
     */
    public String getAjaxMsg()
    {
        return ajaxMsg;
    }


    /**
     * Setter of ajaxMsg.
     * @param ajaxMsg The ajaxMsg to set.
     */
    public void setAjaxMsg(String ajaxMsg)
    {
        this.ajaxMsg = ajaxMsg;
    }


    public List<? extends Object> getTradingPartners()
    {
        return tradingPartners;
    }


    public void setTradingPartners(List<? extends Object> tradingPartners)
    {
        this.tradingPartners = tradingPartners;
    }


    public boolean isSelectAllSupplier()
    {
        return selectAllSupplier;
    }


    public void setSelectAllSupplier(boolean selectAllSupplier)
    {
        this.selectAllSupplier = selectAllSupplier;
    }


    public boolean isSelectAllTp()
    {
        return selectAllTp;
    }


    public void setSelectAllTp(boolean selectAllTp)
    {
        this.selectAllTp = selectAllTp;
    }


}
