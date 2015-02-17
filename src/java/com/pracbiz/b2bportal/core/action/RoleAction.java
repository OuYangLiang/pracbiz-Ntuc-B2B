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
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.constants.UserType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.OperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.RoleTmpExHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.OperationService;
import com.pracbiz.b2bportal.core.service.RoleGroupService;
import com.pracbiz.b2bportal.core.service.RoleGroupTmpService;
import com.pracbiz.b2bportal.core.service.RoleOperationTmpService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RoleTmpService;
import com.pracbiz.b2bportal.core.service.RoleUserService;
import com.pracbiz.b2bportal.core.service.RoleUserTmpService;
import com.pracbiz.b2bportal.core.service.SupplierRoleTmpService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserTypeService;

public class RoleAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(RoleAction.class);
    private static final long serialVersionUID = 6974201401071195605L;
    private static final String SESSION_KEY_SEARCH_PARAMETER_ROLE = "SEARCH_PARAMETER_ROLE";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.RoleAction.selectedOids";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final String REQUEST_OID_DELIMITER = "\\-";
    
    
    private final String ALL_SUPPLIERS = this.getText("role.all.supplier.selected");
    public static final int MAX_LENGTH_ROLE_ID = 20;
    public static final int MAX_LENGTH_ROLE_NAME = 50;
    private static final Map<String, String> sortMap;
    
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("roleId", "ROLE_ID");
        sortMap.put("userTypeId", "USER_TYPE_ID");
        sortMap.put("roleType", "ROLE_TYPE");
        sortMap.put("company", "COMPANY");
        sortMap.put("actor", "ACTOR");
        sortMap.put("actionType", "ACTION_TYPE");
        sortMap.put("actionDate", "ACTION_DATE");
        sortMap.put("ctrlStatus", "CTRL_STATUS");
    }
    
    private RoleTmpExHolder param;
    private RoleTmpExHolder oldParam;
    
    private Map<String, String> roleTypes;
    private Map<String, String> ctrlStatuses;
    private List<UserTypeHolder> userTypes;
    private List<BuyerHolder> buyers;
    //private List<SupplierHolder> suppliers;
    private List<SupplierHolder> availableSuppliers;
    private List<? extends Object> selectedSuppliers;
    private List<? extends Object> oldSelectedSuppliers;
    private List<? extends Object> operations;
    private List<? extends Object> selectedOperations;
    private List<? extends Object> oldSelectedOperations;
    
    private String ajaxMsg;
    private String confirmType;
    List<RoleTmpHolder> createList;
    List<RoleTmpHolder> updateList;
    List<RoleTmpHolder> deleteList;
    

    @Autowired transient private OidService oidService;
    @Autowired transient private RoleService roleService;
    @Autowired transient private RoleTmpService roleTmpService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private SupplierService supplierService;
    @Autowired transient private OperationService operationService;
    @Autowired transient private RoleOperationTmpService roleOperationTmpService;
    @Autowired transient private SupplierRoleTmpService supplierRoleTmpService;
    @Autowired transient private RoleUserService roleUserService;
    @Autowired transient private RoleUserTmpService roleUserTmpService;
    @Autowired transient private RoleGroupService roleGroupService;
    @Autowired transient private RoleGroupTmpService roleGroupTmpService;

    private boolean companyChanged;
    
    public RoleAction()
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_ROLE);
        
        
        if (param == null)
        {
            param = (RoleTmpExHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_ROLE);
        }
        
        try
        {
            userTypes = userTypeService.selectPrivilegedSubUserTypesByUserTypeInclusively(this.getUserTypeOfCurrentUser());
            ctrlStatuses = MkCtrlStatus.toMapValue();
            roleTypes = new HashMap<String,String>();
            
            for (Object obj : userTypes)
            {
                UserTypeHolder userType = (UserTypeHolder) obj;
                
                if (!roleTypes.containsKey(userType.getRoleType().name()))
                {
                    roleTypes.put(userType.getRoleType().name(), userType.getRoleType().getKey());
                }
            }
            
            if (null != param && null != param.getRoleType())
            {
                Iterator<UserTypeHolder> it = userTypes.iterator();
                
                while (it.hasNext())
                {
                    UserTypeHolder type = it.next();
                    
                    if (!type.getRoleType().equals(param.getRoleType()))
                    {
                        it.remove();
                    }
                }
            }
            
            for (UserTypeHolder userType : userTypes)
            {
                if ("SysAdmin".equalsIgnoreCase(userType.getUserTypeId()))
                {
                    userType.setUserTypeId(this.getText(UserType.SYSADMIN.getKey()));
                }
                else if ("BuyerAdmin".equalsIgnoreCase(userType.getUserTypeId()))
                {
                    userType.setUserTypeId(this.getText(UserType.BUYERADMIN.getKey()));
                }
                else if ("SupplierAdmin".equalsIgnoreCase(userType.getUserTypeId()))
                {
                    userType.setUserTypeId(this.getText(UserType.SUPPLIERADMIN.getKey()));
                }
                else if ("BuyerUser".equalsIgnoreCase(userType.getUserTypeId()))
                {
                    userType.setUserTypeId(this.getText(UserType.BUYERUSER.getKey()));
                }
                else if ("SupplierUser".equalsIgnoreCase(userType.getUserTypeId()))
                {
                    userType.setUserTypeId(this.getText(UserType.SUPPLIERUSER.getKey()));
                }
                else if ("StoreAdmin".equalsIgnoreCase(userType.getUserTypeId()))
                {
                    userType.setUserTypeId(this.getText(UserType.STOREADMIN.getKey()));
                }
                else if ("StoreUser".equalsIgnoreCase(userType.getUserTypeId()))
                {
                    userType.setUserTypeId(this.getText(UserType.STOREUSER.getKey()));
                }
            }
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        this.getSession().put(SESSION_KEY_SEARCH_PARAMETER_ROLE, param);
        return SUCCESS;
    }


    public String search()
    {
        if (null == param)
        {
            param = new RoleTmpExHolder();
        }
        
        
        try
        {
            param.setAllSupplierKey(ALL_SUPPLIERS);
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_ROLE, param);
        
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            RoleTmpExHolder searchParam = (RoleTmpExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_ROLE);
            
            
            if (searchParam == null)
            {
                searchParam = new RoleTmpExHolder();
                searchParam.setAllSupplierKey(ALL_SUPPLIERS);
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_ROLE, searchParam);
            }
            searchParam.setCurrentUserTypeOid(this
                .getUserTypeOfCurrentUser());
            searchParam.setCurrentUserBuyerOid(this
                .getProfileOfCurrentUser().getBuyerOid());
            searchParam.setCurrentUserSupplierOid(this
                .getProfileOfCurrentUser().getSupplierOid());
            searchParam.setCurrentUserOid(this
                .getProfileOfCurrentUser().getUserOid());
            
            this.obtainListRecordsOfPagination(roleService, searchParam,
                sortMap, "roleOid", null);
            
            
            for (BaseHolder baseItem : this.getGridRlt().getItems())
            {
                RoleTmpExHolder item = (RoleTmpExHolder) baseItem;
                if (item.getRoleType() != null)
                {
                    item.setRoleTypeValue(this.getText(item.getRoleType().getKey()));
                }
                if (item.getActionType() != null)
                {
                    item.setActionTypeValue(this.getText(item.getActionType().getKey()));
                }
                if (item.getCtrlStatus() != null)
                {
                    item.setCtrlStatusValue(this.getText(item.getCtrlStatus().getKey()));
                }
                
                if ("SysAdmin".equalsIgnoreCase(item.getUserTypeId()))
                {
                    item.setUserTypeId(this.getText(UserType.SYSADMIN.getKey()));
                }
                else if ("BuyerAdmin".equalsIgnoreCase(item.getUserTypeId()))
                {
                    item.setUserTypeId(this.getText(UserType.BUYERADMIN.getKey()));
                }
                else if ("SupplierAdmin".equalsIgnoreCase(item.getUserTypeId()))
                {
                    item.setUserTypeId(this.getText(UserType.SUPPLIERADMIN.getKey()));
                }
                else if ("BuyerUser".equalsIgnoreCase(item.getUserTypeId()))
                {
                    item.setUserTypeId(this.getText(UserType.BUYERUSER.getKey()));
                }
                else if ("SupplierUser".equalsIgnoreCase(item.getUserTypeId()))
                {
                    item.setUserTypeId(this.getText(UserType.SUPPLIERUSER.getKey()));
                }
                else if ("StoreAdmin".equalsIgnoreCase(item.getUserTypeId()))
                {
                    item.setUserTypeId(this.getText(UserType.STOREADMIN.getKey()));
                }
                else if ("StoreUser".equalsIgnoreCase(item.getUserTypeId()))
                {
                    item.setUserTypeId(this.getText(UserType.STOREUSER.getKey()));
                }
            }
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
            userTypes = userTypeService
                .selectPrivilegedSubUserTypesByUserType(this
                    .getUserTypeOfCurrentUser());
            
            buyers = buyerService.select(new BuyerHolder());
            //suppliers = supplierService.selectActiveSuppliers();
            availableSuppliers = new ArrayList<SupplierHolder>();
            selectedSuppliers = new ArrayList<SupplierHolder>();
            
            operations = new ArrayList<Object>();
            selectedOperations = new ArrayList<Object>();
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
            boolean flag = this.hasFieldErrors();
            
            if (param == null)
            {
                param = new RoleTmpExHolder();
            }
            
            
            if (!flag)
            {
                flag = this.isOperationEmpty(selectedOperations);
            }
            
            
            if (!flag)
            {
                flag = this.isRoleIdTooLong(param.getRoleId().trim());
            }
            
            
            if (!flag)
            {
                flag = this.isRoleNameTooLong(param.getRoleName().trim());
            }
            
            
            if (!flag)
            {
                flag = this.isBuyerNotSelected(param);
            }
            
            
            if (!flag)
            {
                flag = this.isSupplierNotSelected(selectedSuppliers, param, false);
            }
            
            
            if (!flag)
            {
                flag = this.isRoleIdAlreadyExist(param.getRoleId().trim(),
                    param, selectedSuppliers, false);
            }
            
            
            if (flag)
            {
                userTypes = userTypeService
                    .selectPrivilegedSubUserTypesByUserType(this
                        .getUserTypeOfCurrentUser());
                
                operations = new ArrayList<Object>();
                buyers = buyerService.select(new BuyerHolder());
                //suppliers = supplierService.selectActiveSuppliers();
                availableSuppliers = new ArrayList<SupplierHolder>();
                
                if (null == selectedOperations)
                {
                    selectedOperations = new ArrayList<Object>();
                }
                
                if (null == selectedSuppliers)
                {
                    selectedSuppliers = new ArrayList<SupplierHolder>();
                }
                
                if (null == param || null == param.getUserTypeOid())
                {
                    return;
                }
                
                if (BigDecimal.valueOf(3).equals(param.getUserTypeOid()))
                {
                    if(BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser())
                        && !BigDecimal.valueOf(-1).equals(param.getBuyerOid()) && !this.isSelectAllSuppliers())
                    {
                        availableSuppliers = supplierService.selectSupplierByBuyerOid(param.getBuyerOid());
                        
                        if (!selectedSuppliers.isEmpty())
                        {
                            selectedSuppliers = this
                                .removeSelectedSuppliersFromAvailableList(
                                    availableSuppliers, selectedSuppliers);
                        }
                    }
                    else if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()) && !this.isSelectAllSuppliers())
                    {
                        availableSuppliers = supplierService
                            .selectSupplierByBuyerOid(getProfileOfCurrentUser()
                                .getBuyerOid());
                        
                        if (!selectedSuppliers.isEmpty())
                        {
                            selectedSuppliers = this
                                .removeSelectedSuppliersFromAvailableList(
                                    availableSuppliers, selectedSuppliers);
                        }
                    }
                }
                
                if (BigDecimal.valueOf(5).equals(param.getUserTypeOid()))
                {
                    if(BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser())
                        && !BigDecimal.valueOf(-1).equals(param.getBuyerOid()) && !this.isSelectAllSuppliers())
                    {
                        availableSuppliers = supplierService.selectSupplierByBuyerOid(param.getBuyerOid());
                        
                        if (!selectedSuppliers.isEmpty())
                        {
                            selectedSuppliers = this
                                .removeSelectedSuppliersFromAvailableList(
                                    availableSuppliers, selectedSuppliers);
                        }
                    }
                    else if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()) && !this.isSelectAllSuppliers())
                    {
                        availableSuppliers = supplierService
                            .selectSupplierByBuyerOid(getProfileOfCurrentUser()
                                .getBuyerOid());
                        
                        if (!selectedSuppliers.isEmpty())
                        {
                            selectedSuppliers = this
                                .removeSelectedSuppliersFromAvailableList(
                                    availableSuppliers, selectedSuppliers);
                        }
                    }
                }
                
                
                // handle operation list
                if(param.getUserTypeOid().equals(BigDecimal.ONE))
                {
                    operations = operationService
                        .selectOperationByUserType(param.getUserTypeOid());
                }
                else if(param.getUserTypeOid().equals(BigDecimal.valueOf(2)))
                {
                    if (BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser()))
                    {
                        if (!BigDecimal.valueOf(-1).equals(param.getBuyerOid()))
                        {
                            operations = operationService
                                .selectOperationByBuyerAndUserType(
                                    param.getBuyerOid(), param.getUserTypeOid());
                        }
                    }
                    else if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                    {
                        operations = operationService
                            .selectOperationByBuyerAndUserType(this
                                .getProfileOfCurrentUser().getBuyerOid(),
                                param.getUserTypeOid());
                    }
                }
                else if(param.getUserTypeOid().equals(BigDecimal.valueOf(6)))
                {
                    if (BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser()))
                    {
                        if (!BigDecimal.valueOf(-1).equals(param.getBuyerOid()))
                        {
                            operations = operationService
                                .selectOperationByBuyerAndUserType(
                                    param.getBuyerOid(), param.getUserTypeOid());
                        }
                    }
                    else if(BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                        || BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser()))
                    {
                        operations = operationService
                            .selectOperationByBuyerAndUserType(this
                                .getProfileOfCurrentUser().getBuyerOid(),
                                param.getUserTypeOid());
                    }
                    
                }
                else if(param.getUserTypeOid().equals(BigDecimal.valueOf(4))
                    || param.getUserTypeOid().equals(BigDecimal.valueOf(7)))
                {
                    if (BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser()))
                    {
                        if (!BigDecimal.valueOf(-1).equals(param.getBuyerOid()))
                        {
                            operations = operationService
                                .selectOperationByBuyerAndUserType(
                                    param.getBuyerOid(), param.getUserTypeOid());
                        }
                    }
                    else if(BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                        || BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser()))
                    {
                        operations = operationService
                            .selectOperationByBuyerAndUserType(this
                                .getProfileOfCurrentUser().getBuyerOid(),
                                param.getUserTypeOid());
                    }
                }
                else if (BigDecimal.valueOf(3).equals(param.getUserTypeOid()))
                {
                    if(BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser())
                        && !BigDecimal.valueOf(-1).equals(param.getBuyerOid()))
                    {
                        operations = operationService
                            .selectSupplierOperationByBuyerAndUserType(
                                param.getBuyerOid(), param.getUserTypeOid());
                    }
                    else if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                    {
                        operations = operationService
                            .selectSupplierOperationByBuyerAndUserType(this
                                .getProfileOfCurrentUser().getBuyerOid(),
                                param.getUserTypeOid());
                    }
                    else if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                    {
                        operations = operationService.selectOperationByUser(this.getProfileOfCurrentUser().getUserOid());
                    }
                    
                }
                else if (BigDecimal.valueOf(5).equals(param.getUserTypeOid()))
                {
                    if(BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser())
                        && !BigDecimal.valueOf(-1).equals(param.getBuyerOid()))
                    {
                        operations = operationService
                            .selectSupplierOperationByBuyerAndUserType(
                                param.getBuyerOid(), param.getUserTypeOid());
                    }
                    else if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                    {
                        operations = operationService
                            .selectSupplierOperationByBuyerAndUserType(this
                                .getProfileOfCurrentUser().getBuyerOid(),
                                param.getUserTypeOid());
                    }
                    else if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                    {
                        operations = operationService
                            .selectOperationBySupplierAndUserType(this
                                .getProfileOfCurrentUser().getSupplierOid(),
                                param.getUserTypeOid());
                    }
                }
                
                
                filterOperations(operations);
                
                if (null == selectedOperations)
                {
                    selectedOperations = new ArrayList<Object>();
                }
                
                if (!selectedOperations.isEmpty())
                {
                    selectedOperations = this
                        .removeSelectedOperationsFromAvailableList(operations,
                            selectedOperations);
                }
                
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String saveAdd()
    {
        try
        {
            param.setRoleType(userTypeService.selectByKey(param.getUserTypeOid()).getRoleType());
            param.setRoleOid(oidService.getOid());
            param.setCreateDate(new Date());
            param.setCreateBy(this.getLoginIdOfCurrentUser());
            if (BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser()))
            {
                param.setCreatedFromSysadmin(true);
            }
            else
            {
                param.setCreatedFromSysadmin(false);
            }
            
            
            if (BigDecimal.ONE.equals(param.getUserTypeOid()))
            {
                param.setBuyerOid(null);
            }
            else if (BigDecimal.valueOf(2).equals(param.getUserTypeOid()))
            {
                if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                {
                    param.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
            }
            else if (BigDecimal.valueOf(3).equals(param.getUserTypeOid()))
            {
                if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                {
                    param.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
                else if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                {
                    param.setBuyerOid(null);
                }
                
                List<SupplierRoleHolder> srList = new ArrayList<SupplierRoleHolder>();
                
                if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                {
                    SupplierRoleTmpHolder sr = new SupplierRoleTmpHolder();
                    sr.setRoleOid(param.getRoleOid());
                    sr.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                    
                    srList.add(sr);
                }
                else
                {
                    for (Object o : selectedSuppliers)
                    {
                        SupplierRoleTmpHolder sr = new SupplierRoleTmpHolder();
                        sr.setRoleOid(param.getRoleOid());
                        sr.setSupplierOid(new BigDecimal(o.toString()));
                        
                        srList.add(sr);
                    }
                }
                
                param.setSupplierRoles(srList);
            }
            else if (BigDecimal.valueOf(4).equals(param.getUserTypeOid()))
            {
                if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                {
                    param.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
            }
            else if (BigDecimal.valueOf(5).equals(param.getUserTypeOid()))
            {
                if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                {
                    param.setBuyerOid(null);
                    
                    List<SupplierRoleHolder> srList = new ArrayList<SupplierRoleHolder>();
                    
                    SupplierRoleTmpHolder sr = new SupplierRoleTmpHolder();
                    sr.setRoleOid(param.getRoleOid());
                    sr.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                    
                    srList.add(sr);
                    param.setSupplierRoles(srList);
                }
                else if (BigDecimal.valueOf(1).equals(this.getUserTypeOfCurrentUser()))
                {
                    List<SupplierRoleHolder> srList = new ArrayList<SupplierRoleHolder>();
                    for (Object o : selectedSuppliers)
                    {
                        SupplierRoleTmpHolder sr = new SupplierRoleTmpHolder();
                        sr.setRoleOid(param.getRoleOid());
                        sr.setSupplierOid(new BigDecimal(o.toString()));
                        
                        srList.add(sr);
                    }
                    param.setSupplierRoles(srList);
                }
                else if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                {
                    param.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                    
                    List<SupplierRoleHolder> srList = new ArrayList<SupplierRoleHolder>();
                    for (Object o : selectedSuppliers)
                    {
                        SupplierRoleTmpHolder sr = new SupplierRoleTmpHolder();
                        sr.setRoleOid(param.getRoleOid());
                        sr.setSupplierOid(new BigDecimal(o.toString()));
                        
                        srList.add(sr);
                    }
                    param.setSupplierRoles(srList);
                }
            }
            else if (BigDecimal.valueOf(6).equals(param.getUserTypeOid()))
            {
                if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()) ||
                    BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser()))
                {
                    param.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
                }
            }
            else if ((BigDecimal.valueOf(7).equals(param.getUserTypeOid())) && 
            		(BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()) ||
                            BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())))
            {
                param.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            
            List<RoleOperationHolder> roList = new ArrayList<RoleOperationHolder>();
            for (Object o : selectedOperations)
            {
                RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
                ro.setRoleOid(param.getRoleOid());
                ro.setOpnId((String) o);
                
                roList.add(ro);
            }
            param.setRoleOperations(roList);
            
            roleService.createRoleProfile(this.getCommonParameter(), param);
            
            if (this.getCommonParameter().getMkMode())
            {
                log.info(this.getText(
                    "B2BPC0109",
                    new String[] {param.getRoleId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            else
            {
                log.info(this.getText(
                    "B2BPC0108",
                    new String[] {param.getRoleId(),
                        this.getLoginIdOfCurrentUser()}));
            }
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    // *****************************************************
    // view page
    // *****************************************************
    
    public String view()
    {
        try
        {
            BeanUtils.copyProperties(roleTmpService.selectRoleByKey(param.getRoleOid()), param);
            UserTypeHolder ut = userTypeService.selectByKey(param.getUserTypeOid());
            
            param.setUserTypeId(ut.getUserTypeId());
            param.setRoleTypeValue(this.getText(param.getRoleType().getKey()));
            param.setActionTypeValue(this.getText(param.getActionType().getKey()));
            param.setCtrlStatusValue(this.getText(param.getCtrlStatus().getKey()));
            
            if(param.getRoleType().equals(RoleType.BUYER))
            {
                param.setCompany(buyerService.selectBuyerByKey(
                    param.getBuyerOid()).getBuyerName());
            }
            else if(param.getRoleType().equals(RoleType.SUPPLIER))
            {
                List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(param.getRoleOid());
                
                for (BigDecimal supOid : supOids)
                {
                    if (BigDecimal.valueOf(-1).equals(supOid))
                    {
                        param.setCompany(ALL_SUPPLIERS);
                    }
                    else
                    {
                        SupplierHolder sup = supplierService.selectSupplierByKey(supOid);
                        
                        if (null == param.getCompany())
                        {
                            param.setCompany(sup.getSupplierName());
                        }
                        else
                        {
                            param.setCompany(param.getCompany() + "</br>" + sup.getSupplierName());
                        }
                    }
                }
            }
            
            
            selectedOperations = operationService
                .selectOperationByRoleTmp(param.getRoleOid());
            
            if(MkCtrlStatus.APPROVED.equals(param.getCtrlStatus())
                || MkCtrlStatus.REJECTED.equals(param.getCtrlStatus())
                || MkCtrlStatus.WITHDRAWN.equals(param.getCtrlStatus()))
            {
                return "view";
            }
            
            if(MkCtrlStatus.PENDING.equals(param.getCtrlStatus())
                && !DbActionType.UPDATE.equals(param.getActionType()))
            {
                return "viewPending";
            }
            
            oldParam = new RoleTmpExHolder();
            BeanUtils.copyProperties(
                roleService.selectByKey(param.getRoleOid()), oldParam);
            
            oldSelectedOperations = operationService.selectOperationByRole(oldParam.getRoleOid());
            
            
            if (BigDecimal.valueOf(3).equals(param.getUserTypeOid()))
            {
                List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(param.getRoleOid());
                List<SupplierHolder> supList = new ArrayList<SupplierHolder>();
                for (BigDecimal supOid : supOids)
                {
                    SupplierHolder sup = null;
                    
                    if (BigDecimal.valueOf(-1).equals(supOid))
                    {
                        sup = new SupplierHolder();
                        sup.setSupplierName(ALL_SUPPLIERS);
                        sup.setSupplierOid(BigDecimal.valueOf(-1));
                    }
                    else
                    {
                        sup = supplierService.selectSupplierByKey(supOid);
                    }
                    
                    supList.add(sup);
                }
                
                selectedSuppliers = supList;
                
                supOids = roleService.selectSupplierOidsByRoleOid(param.getRoleOid());
                supList = new ArrayList<SupplierHolder>();
                for (BigDecimal supOid : supOids)
                {
                    SupplierHolder sup = null;
                    
                    if (BigDecimal.valueOf(-1).equals(supOid))
                    {
                        sup = new SupplierHolder();
                        sup.setSupplierName(ALL_SUPPLIERS);
                        sup.setSupplierOid(BigDecimal.valueOf(-1));
                    }
                    else
                    {
                        sup = supplierService.selectSupplierByKey(supOid);
                    }
                    
                    supList.add(sup);
                }
                
                oldSelectedSuppliers = supList;
                
                if(!((selectedSuppliers.size() == oldSelectedSuppliers.size()) && selectedSuppliers
                    .containsAll(oldSelectedSuppliers)))
                {
                    companyChanged = true;
                }
            }
            
            
            if (BigDecimal.valueOf(5).equals(param.getUserTypeOid()) && null != param.getBuyerOid())
            {
                List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(param.getRoleOid());
                List<SupplierHolder> supList = new ArrayList<SupplierHolder>();
                for (BigDecimal supOid : supOids)
                {
                    SupplierHolder sup = null;
                    
                    if (BigDecimal.valueOf(-1).equals(supOid))
                    {
                        sup = new SupplierHolder();
                        sup.setSupplierName(ALL_SUPPLIERS);
                        sup.setSupplierOid(BigDecimal.valueOf(-1));
                    }
                    else
                    {
                        sup = supplierService.selectSupplierByKey(supOid);
                    }
                    
                    supList.add(sup);
                }
                
                selectedSuppliers = supList;
                
                supOids = roleService.selectSupplierOidsByRoleOid(param.getRoleOid());
                supList = new ArrayList<SupplierHolder>();
                for (BigDecimal supOid : supOids)
                {
                    SupplierHolder sup = null;
                    
                    if (BigDecimal.valueOf(-1).equals(supOid))
                    {
                        sup = new SupplierHolder();
                        sup.setSupplierName(ALL_SUPPLIERS);
                        sup.setSupplierOid(BigDecimal.valueOf(-1));
                    }
                    else
                    {
                        sup = supplierService.selectSupplierByKey(supOid);
                    }
                    
                    supList.add(sup);
                }
                
                oldSelectedSuppliers = supList;
                
                if(!((selectedSuppliers.size() == oldSelectedSuppliers.size()) && selectedSuppliers
                    .containsAll(oldSelectedSuppliers)))
                {
                    companyChanged = true;
                }
            }
            
            return "viewPendingUpdate";
        }
        catch(Exception e)
        {
            this.handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }

    }

    
    // *****************************************************
    // edit page
    // *****************************************************
    
    @SuppressWarnings("unchecked")
    public String initEdit()
    {
        try
        {
            BeanUtils.copyProperties(
                roleTmpService.selectRoleByKey(param.getRoleOid()), param);
            
            UserTypeHolder ut = userTypeService.selectByKey(param.getUserTypeOid());
            
            param.setUserTypeId(ut.getUserTypeId());
            param.setRoleTypeValue(this.getText(param.getRoleType().getKey()));
            
            if (param.getRoleType().equals(RoleType.ADMIN))
            {
                operations = operationService.selectOperationByUserType(param
                    .getUserTypeOid());
            }
            else if (param.getRoleType().equals(RoleType.BUYER))
            {
                param.setCompany(buyerService.selectBuyerByKey(
                    param.getBuyerOid()).getBuyerName());
                operations = operationService
                    .selectOperationByBuyerAndUserType(param.getBuyerOid(),
                        param.getUserTypeOid());
            }
            else if (BigDecimal.valueOf(5).equals(param.getUserTypeOid()))
            {
                if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                {
                    List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(param.getRoleOid());
                    SupplierHolder sup = supplierService.selectSupplierByKey(supOids.get(0));
                    param.setCompany(sup.getSupplierName());
                    
                    operations = operationService
                        .selectOperationBySupplierAndUserType(sup.getSupplierOid(),
                            param.getUserTypeOid());
                }
                else if (BigDecimal.valueOf(1).equals(this.getUserTypeOfCurrentUser()) ||
                    BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                {
                    List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(param.getRoleOid());
                    List<SupplierHolder> supList = new ArrayList<SupplierHolder>();
                    
                    boolean isAllSupplier = false;
                    
                    for (BigDecimal supOid : supOids)
                    {
                        SupplierHolder sup = null;
                        
                        if (BigDecimal.valueOf(-1).equals(supOid))
                        {
                            sup = new SupplierHolder();
                            sup.setSupplierOid(BigDecimal.valueOf(-1));
                            sup.setSupplierName(ALL_SUPPLIERS);
                            isAllSupplier = true;
                        }
                        else
                        {
                            sup = supplierService.selectSupplierByKey(supOid);
                        }
                        
                        supList.add(sup);
                    }
                    
                    selectedSuppliers = supList;
                    availableSuppliers = supplierService.selectSupplierByBuyerOid(param.getBuyerOid());
                    
                    if (!isAllSupplier)
                    {
                        availableSuppliers.removeAll(selectedSuppliers);
                    }
                    
                    operations = operationService
                        .selectSupplierOperationByBuyerAndUserType(
                            param.getBuyerOid(), param.getUserTypeOid());
                }
            }
            else if (BigDecimal.valueOf(3).equals(param.getUserTypeOid()))
            {
                List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(param.getRoleOid());
                List<SupplierHolder> supList = new ArrayList<SupplierHolder>();
                
                boolean isAllSupplier = false;
                
                for (BigDecimal supOid : supOids)
                {
                    SupplierHolder sup = null;
                    
                    if (BigDecimal.valueOf(-1).equals(supOid))
                    {
                        sup = new SupplierHolder();
                        sup.setSupplierOid(BigDecimal.valueOf(-1));
                        sup.setSupplierName(ALL_SUPPLIERS);
                        isAllSupplier = true;
                    }
                    else
                    {
                        sup = supplierService.selectSupplierByKey(supOid);
                    }
                    
                    supList.add(sup);
                }
                
                selectedSuppliers = supList;
                if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                {
                    availableSuppliers = new ArrayList<SupplierHolder>();
                }
                else
                {
                    availableSuppliers = supplierService.selectSupplierByBuyerOid(param.getBuyerOid());
                }
                
                if (!isAllSupplier)
                {
                    availableSuppliers.removeAll(selectedSuppliers);
                }
                
                if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                {
                    operations = operationService.selectOperationByUser(this.getProfileOfCurrentUser().getUserOid());
                }
                else
                {
                    operations = operationService
                        .selectSupplierOperationByBuyerAndUserType(
                            param.getBuyerOid(), param.getUserTypeOid());
                }
            }
            
            filterOperations(operations);
            
            selectedOperations = operationService
                .selectOperationByRoleTmp(param.getRoleOid());
            
            if (!selectedOperations.isEmpty())
            {
                operations.removeAll(selectedOperations);
            }
            
            Comparator<OperationHolder> compOpn = new Comparator<OperationHolder>()
            {
                @Override
                public int compare(OperationHolder o1, OperationHolder o2)
                {
                    return o1.getOpnDesc().compareTo(o2.getOpnDesc());
                }
            };
            
            Comparator<SupplierHolder> compSup = new Comparator<SupplierHolder>()
            {
                @Override
                public int compare(SupplierHolder o1, SupplierHolder o2)
                {
                    return o1.getSupplierName().compareTo(o2.getSupplierName());
                }
            };
            
            if (selectedOperations != null)
            {
                Collections.sort((List<OperationHolder>)selectedOperations, compOpn);
            }
            if (operations != null)
            {
                Collections.sort((List<OperationHolder>)operations, compOpn);
            }
            if (selectedSuppliers != null)
            {
                Collections.sort((List<SupplierHolder>)selectedSuppliers, compSup);
            }
            if (availableSuppliers != null)
            {
                Collections.sort((List<SupplierHolder>)availableSuppliers, compSup);
            }
            
        }
        catch (Exception e)
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
            boolean flag = this.hasFieldErrors();
            
            if (!flag)
            {
                flag = this.isOperationEmpty(selectedOperations);
            }
            
            if (!flag)
            {
                flag = this.isRoleIdTooLong(param.getRoleId().trim());
            }
            
            if (!flag)
            {
                flag = this.isRoleNameTooLong(param.getRoleName().trim());
            }
            
            RoleTmpHolder me = roleTmpService.selectRoleByKey(param.getRoleOid());
            
            if (!flag)
            {
                flag = this.isSupplierNotSelected(selectedSuppliers, param, true);
            }
            
            if (!flag)
            {
                flag = this.isRoleIdAlreadyExist(param.getRoleId().trim(),
                    param, selectedSuppliers, true);
            }
            
            if (flag)
            {
                UserTypeHolder ut = userTypeService.selectByKey(me.getUserTypeOid());
                param.setUserTypeOid(ut.getUserTypeOid());
                param.setUserTypeId(ut.getUserTypeId());
                
                if (me.getRoleType().equals(RoleType.ADMIN))
                {
                    operations = operationService.selectOperationByUserType(me.getUserTypeOid());
                }
                else if(me.getRoleType().equals(RoleType.BUYER))
                {
                    param.setCompany(buyerService.selectBuyerByKey(
                        me.getBuyerOid()).getBuyerName());
                    
                    operations = operationService
                        .selectOperationByBuyerAndUserType(me.getBuyerOid(),
                            param.getUserTypeOid());
                }
                else if (BigDecimal.valueOf(5).equals(me.getUserTypeOid()))
                {
                    if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                    {
                        List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(param.getRoleOid());
                        SupplierHolder sup = supplierService.selectSupplierByKey(supOids.get(0));
                        param.setCompany(sup.getSupplierName());
                        
                        operations = operationService
                            .selectOperationBySupplierAndUserType(sup.getSupplierOid(),
                                param.getUserTypeOid());
                    }
                    else if (BigDecimal.valueOf(1).equals(this.getUserTypeOfCurrentUser()) ||
                        BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()))
                    {
                        if (null == selectedSuppliers)
                        {
                            selectedSuppliers = new ArrayList<Object>();
                        }
                        
                        availableSuppliers = supplierService.selectSupplierByBuyerOid(me.getBuyerOid());
                        
                        if (!this.isSelectAllSuppliers())
                        {
                            selectedSuppliers = this
                                .removeSelectedSuppliersFromAvailableList(
                                    availableSuppliers, selectedSuppliers);
                        }
                        
                        operations = operationService
                            .selectSupplierOperationByBuyerAndUserType(
                                me.getBuyerOid(), me.getUserTypeOid());
                    }
                }
                else if (BigDecimal.valueOf(3).equals(me.getUserTypeOid()))
                {
                    if (null == selectedSuppliers)
                    {
                        selectedSuppliers = new ArrayList<Object>();
                    }
                    
                    if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                    {
                        availableSuppliers = new ArrayList<SupplierHolder>();
                    }
                    else
                    {
                        availableSuppliers = supplierService.selectSupplierByBuyerOid(me.getBuyerOid());
                    }
                    
                    if (!this.isSelectAllSuppliers())
                    {
                        selectedSuppliers = this
                            .removeSelectedSuppliersFromAvailableList(
                                availableSuppliers, selectedSuppliers);
                    }
                    
                    if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                    {
                        operations = operationService.selectOperationByUser(this.getProfileOfCurrentUser().getUserOid());
                    }
                    else
                    {
                        operations = operationService
                            .selectSupplierOperationByBuyerAndUserType(
                                me.getBuyerOid(), me.getUserTypeOid());
                    }
                }
               
                
                filterOperations(operations);
                
                if (null == selectedOperations)
                {
                    selectedOperations = new ArrayList<Object>();
                }
                
                if (!selectedOperations.isEmpty())
                {
                    selectedOperations = this
                        .removeSelectedOperationsFromAvailableList(operations,
                            selectedOperations);
                }
                
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
            RoleTmpHolder me = roleTmpService.selectRoleByKey(param.getRoleOid());
            
            RoleTmpHolder cur = new RoleTmpHolder();
            BeanUtils.copyProperties(me, cur);
            
            cur.setRoleId(param.getRoleId());
            cur.setRoleName(param.getRoleName());
            cur.setUpdateBy(this.getLoginIdOfCurrentUser());
            cur.setUpdateDate(new Date());
            cur.setCreatedFromSysadmin(me.getCreatedFromSysadmin());
            
            List<RoleOperationHolder> roList = new ArrayList<RoleOperationHolder>();
            for (Object o : selectedOperations)
            {
                RoleOperationTmpHolder ro = new RoleOperationTmpHolder();
                ro.setRoleOid(cur.getRoleOid());
                ro.setOpnId((String) o);
                
                roList.add(ro);
            }
            
            cur.setRoleOperations(roList);
            me.setRoleOperations(roleOperationTmpService.selectByRole(me.getRoleOid()));
            
            
            if (BigDecimal.valueOf(3).equals(me.getUserTypeOid()))
            {
                if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                {
                    List<SupplierRoleHolder> srList = new ArrayList<SupplierRoleHolder>();
                    
                    SupplierRoleTmpHolder sr = new SupplierRoleTmpHolder();
                    sr.setRoleOid(me.getRoleOid());
                    sr.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                    srList.add(sr);
                    
                    cur.setSupplierRoles(srList);
                    
                    
                    List<SupplierRoleTmpHolder> meSrList = supplierRoleTmpService.selectByRole(me.getRoleOid());
                    me.setSupplierRoles(new ArrayList<SupplierRoleHolder>());
                    
                    for (SupplierRoleTmpHolder meSr : meSrList)
                    {
                        me.getSupplierRoles().add(meSr);
                    }
                }
                else
                {
                    List<SupplierRoleHolder> srList = new ArrayList<SupplierRoleHolder>();
                    
                    for (Object o : selectedSuppliers)
                    {
                        SupplierRoleTmpHolder sr = new SupplierRoleTmpHolder();
                        sr.setRoleOid(me.getRoleOid());
                        sr.setSupplierOid(new BigDecimal(o.toString()));
                        
                        srList.add(sr);
                    }
                    
                    cur.setSupplierRoles(srList);
                    
                    
                    List<SupplierRoleTmpHolder> meSrList = supplierRoleTmpService.selectByRole(me.getRoleOid());
                    me.setSupplierRoles(new ArrayList<SupplierRoleHolder>());
                    
                    for (SupplierRoleTmpHolder meSr : meSrList)
                    {
                        me.getSupplierRoles().add(meSr);
                    }
                }
            }
            
            if (BigDecimal.valueOf(5).equals(me.getUserTypeOid()) && !BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
            {
                List<SupplierRoleHolder> srList = new ArrayList<SupplierRoleHolder>();
                
                for (Object o : selectedSuppliers)
                {
                    SupplierRoleTmpHolder sr = new SupplierRoleTmpHolder();
                    sr.setRoleOid(me.getRoleOid());
                    sr.setSupplierOid(new BigDecimal(o.toString()));
                    
                    srList.add(sr);
                }
                
                cur.setSupplierRoles(srList);
                
                
                List<SupplierRoleTmpHolder> meSrList = supplierRoleTmpService.selectByRole(me.getRoleOid());
                me.setSupplierRoles(new ArrayList<SupplierRoleHolder>());
                
                for (SupplierRoleTmpHolder meSr : meSrList)
                {
                    me.getSupplierRoles().add(meSr);
                }
            }
            
            
            roleService.updateRoleProfile(this.getCommonParameter(), me, cur);
            
            if (this.getCommonParameter().getMkMode())
            {
                log.info(this.getText(
                    "B2BPC0111",
                    new String[] {me.getRoleId(),
                        this.getLoginIdOfCurrentUser()}));
            }
            else
            {
                log.info(this.getText(
                    "B2BPC0110",
                    new String[] {me.getRoleId(),
                        this.getLoginIdOfCurrentUser()}));
            }
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    // *****************************************************
    // delete function
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
                BigDecimal roleOid = new BigDecimal(part);
                
                RoleTmpHolder oldRole = roleTmpService.selectRoleByKey(roleOid);
                
                if (MkCtrlStatus.PENDING.equals(oldRole.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPC0103", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                if (!BigDecimal.ONE.equals(this.getUserTypeOfCurrentUser()) && oldRole.getCreatedFromSysadmin())
                {
                    msg.saveError(this.getText("B2BPC0135", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                if((BigDecimal.valueOf(5).equals(oldRole.getUserTypeOid()) || BigDecimal.valueOf(3).equals(oldRole.getUserTypeOid()))
                    && null != oldRole.getBuyerOid()
                    && BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()))
                {
                    msg.saveError(this.getText("B2BPC0136", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                if((BigDecimal.valueOf(5).equals(oldRole.getUserTypeOid()) || BigDecimal.valueOf(3).equals(oldRole.getUserTypeOid()))
                    && null == oldRole.getBuyerOid()
                    && (BigDecimal.valueOf(1).equals(this.getUserTypeOfCurrentUser()) || BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())))
                {
                    msg.saveError(this.getText("B2BPC0137", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                List<RoleUserHolder> roleUsers = roleUserService.selectRoleUserByRoleOid(roleOid);
                List<RoleUserTmpHolder> roleUserTmps = roleUserTmpService.selectRoleUserTmpByRoleOid(roleOid);
                if((roleUsers != null && !roleUsers.isEmpty()) || (roleUserTmps != null && !roleUserTmps.isEmpty()))
                {
                    msg.saveError(this.getText("B2BPC0101", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                List<RoleGroupHolder> roleGroups = roleGroupService.selectRoleGroupByRoleOid(roleOid);
                List<RoleGroupTmpHolder> roleGroupTmps = roleGroupTmpService.selectRoleGroupTmpByRoleOid(roleOid);
                if ((roleGroups != null && !roleGroups.isEmpty()) || (roleGroupTmps != null && !roleGroupTmps.isEmpty()))
                {
                    msg.saveError(this.getText("B2BPC0102", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                oldRole.setRoleOperations(roleOperationTmpService.selectByRole(oldRole.getRoleOid()));
                
                roleService.removeRoleProfile(this.getCommonParameter(), oldRole);
                
                if (this.getCommonParameter().getMkMode())
                {
                    log.info(this.getText(
                        "B2BPC0107",
                        new String[] {oldRole.getRoleId(),
                            this.getLoginIdOfCurrentUser()}));
                    
                    msg.saveSuccess(this.getText("B2BPC0127", new String[]{oldRole.getRoleId()}));
                }
                else
                {
                    log.info(this.getText(
                        "B2BPC0106",
                        new String[] {oldRole.getRoleId(),
                            this.getLoginIdOfCurrentUser()}));
                    
                    msg.saveSuccess(this.getText("B2BPC0130", new String[]{oldRole.getRoleId()}));
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
    // approve function
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
                BigDecimal roleOid = new BigDecimal(part);
                
                RoleTmpHolder oldRole = roleTmpService.selectRoleByKey(roleOid);
                
                if (!MkCtrlStatus.PENDING.equals(oldRole.getCtrlStatus()))
                {
                    this.ajaxMsg = this.getText("B2BPC0134");
                    break;
                }
                
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.ajaxMsg = this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo});
        }
        
        if (ajaxMsg == null)
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
            
            createList = new ArrayList<RoleTmpHolder>();
            updateList = new ArrayList<RoleTmpHolder>();
            deleteList = new ArrayList<RoleTmpHolder>();
            
            for (String part : parts)
            {
                BigDecimal roleOid = new BigDecimal(part);
                RoleTmpExHolder oldTmpRole = new RoleTmpExHolder();
                BeanUtils.copyProperties(roleTmpService.selectRoleByKey(roleOid), oldTmpRole);
                
                
                UserTypeHolder userType = userTypeService.selectByKey(oldTmpRole.getUserTypeOid());
                
                oldTmpRole.setUserTypeId(userType.getUserTypeId());
                oldTmpRole.setRoleTypeValue(this.getText(oldTmpRole.getRoleType().getKey()));
                oldTmpRole.setActionTypeValue(this.getText(oldTmpRole.getActionType().getKey()));
                oldTmpRole.setCtrlStatusValue(this.getText(oldTmpRole.getCtrlStatus().getKey()));
                
                
                if(oldTmpRole.getRoleType().equals(RoleType.BUYER))
                {
                    oldTmpRole.setCompany(buyerService.selectBuyerByKey(
                        oldTmpRole.getBuyerOid()).getBuyerName());
                }
                else if(oldTmpRole.getRoleType().equals(RoleType.SUPPLIER))
                {
                    List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(oldTmpRole.getRoleOid());
                    
                    for (BigDecimal supOid : supOids)
                    {
                        if (BigDecimal.valueOf(-1).equals(supOid))
                        {
                            oldTmpRole.setCompany(ALL_SUPPLIERS);
                        }
                        else
                        {
                            SupplierHolder sup = supplierService.selectSupplierByKey(supOid);
                            
                            if (null == oldTmpRole.getCompany())
                            {
                                oldTmpRole.setCompany(sup.getSupplierName());
                            }
                            else
                            {
                                oldTmpRole.setCompany(oldTmpRole.getCompany() + "</br>" + sup.getSupplierName());
                            }
                        }
                        
                    }
                }
                
                
                oldTmpRole.setSelectedOperations(operationService
                    .selectOperationByRoleTmp(oldTmpRole.getRoleOid()));
                
                if(DbActionType.CREATE.equals(oldTmpRole.getActionType()))
                {
                    createList.add(oldTmpRole);
                    continue;
                }
                
                if(DbActionType.DELETE.equals(oldTmpRole.getActionType()))
                {
                    deleteList.add(oldTmpRole);
                    continue;
                }
                
                RoleTmpExHolder oldRole = new RoleTmpExHolder();
                BeanUtils.copyProperties(
                    roleService.selectByKey(oldTmpRole.getRoleOid()), oldRole);
                
                oldRole.setSelectedOperations(operationService.selectOperationByRole(oldRole.getRoleOid()));
                
                if (BigDecimal.valueOf(3).equals(oldTmpRole.getUserTypeOid()))
                {
                    List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(oldTmpRole.getRoleOid());
                    List<SupplierHolder> supList = new ArrayList<SupplierHolder>();
                    for (BigDecimal supOid : supOids)
                    {
                        SupplierHolder sup = null;
                        
                        if (BigDecimal.valueOf(-1).equals(supOid))
                        {
                            sup = new SupplierHolder();
                            sup.setSupplierName(ALL_SUPPLIERS);
                            sup.setSupplierOid(BigDecimal.valueOf(-1));
                        }
                        else
                        {
                            sup = supplierService.selectSupplierByKey(supOid);
                        }
                        
                        supList.add(sup);
                    }
                    
                    oldTmpRole.setSelectedSuppliers(supList);
                    
                    supOids = roleService.selectSupplierOidsByRoleOid(oldTmpRole.getRoleOid());
                    supList = new ArrayList<SupplierHolder>();
                    for (BigDecimal supOid : supOids)
                    {
                        SupplierHolder sup = null;
                        
                        if (BigDecimal.valueOf(-1).equals(supOid))
                        {
                            sup = new SupplierHolder();
                            sup.setSupplierName(ALL_SUPPLIERS);
                            sup.setSupplierOid(BigDecimal.valueOf(-1));
                        }
                        else
                        {
                            sup = supplierService.selectSupplierByKey(supOid);
                        }
                        
                        supList.add(sup);
                    }
                    
                    oldRole.setSelectedSuppliers(supList);
                    
                    if (!((oldTmpRole.getSelectedSuppliers().size() == oldRole.getSelectedSuppliers().size())
                        && oldTmpRole.getSelectedSuppliers().containsAll(oldRole.getSelectedSuppliers())))
                    {
                        oldTmpRole.setCompanyChanged(true);
                    }
                }
                
                if (BigDecimal.valueOf(5).equals(oldTmpRole.getUserTypeOid()) && null != oldRole.getBuyerOid())
                {
                    List<BigDecimal> supOids = roleTmpService.selectSupplierOidsByRoleOid(oldTmpRole.getRoleOid());
                    List<SupplierHolder> supList = new ArrayList<SupplierHolder>();
                    for (BigDecimal supOid : supOids)
                    {
                        SupplierHolder sup = null;
                        
                        if (BigDecimal.valueOf(-1).equals(supOid))
                        {
                            sup = new SupplierHolder();
                            sup.setSupplierName(ALL_SUPPLIERS);
                            sup.setSupplierOid(BigDecimal.valueOf(-1));
                        }
                        else
                        {
                            sup = supplierService.selectSupplierByKey(supOid);
                        }
                        
                        supList.add(sup);
                    }
                    
                    oldTmpRole.setSelectedSuppliers(supList);
                    
                    supOids = roleService.selectSupplierOidsByRoleOid(oldTmpRole.getRoleOid());
                    supList = new ArrayList<SupplierHolder>();
                    for (BigDecimal supOid : supOids)
                    {
                        SupplierHolder sup = null;
                        
                        if (BigDecimal.valueOf(-1).equals(supOid))
                        {
                            sup = new SupplierHolder();
                            sup.setSupplierName(ALL_SUPPLIERS);
                            sup.setSupplierOid(BigDecimal.valueOf(-1));
                        }
                        else
                        {
                            sup = supplierService.selectSupplierByKey(supOid);
                        }
                        
                        supList.add(sup);
                    }
                    
                    oldRole.setSelectedSuppliers(supList);
                    
                    if (!((oldTmpRole.getSelectedSuppliers().size() == oldRole.getSelectedSuppliers().size())
                        && oldTmpRole.getSelectedSuppliers().containsAll(oldRole.getSelectedSuppliers())))
                    {
                        oldTmpRole.setCompanyChanged(true);
                    }
                }
                
                oldTmpRole.setOldVersion(oldRole);
                
                updateList.add(oldTmpRole);
                
            }
            
            String reqUrl = StringUtils.remove(this.getRequest()
                .getRequestURI(), this.getRequest().getContextPath());
            
            if ("/role/initWithdraw.action".equals(reqUrl))
            {
                confirmType = "W";
            }
            else if ("/role/initReject.action".equals(reqUrl))
            {
                confirmType = "R";
            }
            else if ("/role/initApprove.action".equals(reqUrl))
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
    
    
    public String saveApprove()
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
                BigDecimal roleOid = new BigDecimal(part);
                
                RoleTmpHolder oldRole = roleTmpService.selectRoleByKey(roleOid);
                
                if (!MkCtrlStatus.PENDING.equals(oldRole.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPC0121", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                if (oldRole.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser()))
                {
                    msg.saveError(this.getText("B2BPC0131", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                oldRole.setRoleOperations(roleOperationTmpService.selectByRole(oldRole.getRoleOid()));
                if (RoleType.SUPPLIER.equals(oldRole.getRoleType()))
                {
                    List<SupplierRoleTmpHolder> srtList = supplierRoleTmpService.selectByRole(oldRole.getRoleOid());
                    oldRole.setSupplierRoles(new ArrayList<SupplierRoleHolder>());
                    
                    for (SupplierRoleTmpHolder srt : srtList)
                    {
                        oldRole.getSupplierRoles().add(srt);
                    }
                }
                
                roleService.approveRoleProfile(this.getCommonParameter(), oldRole);
                msg.saveSuccess(this.getText("B2BPC0124", new String[]{oldRole.getRoleId()}));
                
                log.info(this.getText(
                    "B2BPC0112",
                    new String[] {oldRole.getRoleId(),
                        this.getLoginIdOfCurrentUser()}));
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
    // reject function
    // *****************************************************
    
    public String saveReject()
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
                BigDecimal roleOid = new BigDecimal(part);
                
                RoleTmpHolder oldRole = roleTmpService.selectRoleByKey(roleOid);
                
                if (!MkCtrlStatus.PENDING.equals(oldRole.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPC0122", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                if (oldRole.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser()))
                {
                    msg.saveError(this.getText("B2BPC0132", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                oldRole.setRoleOperations(roleOperationTmpService.selectByRole(oldRole.getRoleOid()));
                if (RoleType.SUPPLIER.equals(oldRole.getRoleType()))
                {
                    List<SupplierRoleTmpHolder> srtList = supplierRoleTmpService.selectByRole(oldRole.getRoleOid());
                    oldRole.setSupplierRoles(new ArrayList<SupplierRoleHolder>());
                    
                    for (SupplierRoleTmpHolder srt : srtList)
                    {
                        oldRole.getSupplierRoles().add(srt);
                    }
                }
                
                roleService.rejectRoleProfile(this.getCommonParameter(), oldRole);
                msg.saveSuccess(this.getText("B2BPC0125", new String[]{oldRole.getRoleId()}));
                
                log.info(this.getText(
                    "B2BPC0113",
                    new String[] {oldRole.getRoleId(),
                        this.getLoginIdOfCurrentUser()}));
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
    // withdraw function
    // *****************************************************
    
    public String saveWithdraw()
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
                BigDecimal roleOid = new BigDecimal(part);
                
                RoleTmpHolder oldRole = roleTmpService.selectRoleByKey(roleOid);
                
                if (!MkCtrlStatus.PENDING.equals(oldRole.getCtrlStatus()))
                {
                    msg.saveError(this.getText("B2BPC0123", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                if (!oldRole.getActor().equalsIgnoreCase(this.getLoginIdOfCurrentUser()))
                {
                    msg.saveError(this.getText("B2BPC0133", new String[]{oldRole.getRoleId()}));
                    continue;
                }
                
                oldRole.setRoleOperations(roleOperationTmpService.selectByRole(oldRole.getRoleOid()));
                if (RoleType.SUPPLIER.equals(oldRole.getRoleType()))
                {
                    List<SupplierRoleTmpHolder> srtList = supplierRoleTmpService.selectByRole(oldRole.getRoleOid());
                    oldRole.setSupplierRoles(new ArrayList<SupplierRoleHolder>());
                    
                    for (SupplierRoleTmpHolder srt : srtList)
                    {
                        oldRole.getSupplierRoles().add(srt);
                    }
                }
                
                roleService.withdrawRoleProfile(this.getCommonParameter(), oldRole);
                msg.saveSuccess(this.getText("B2BPC0126", new String[]{oldRole.getRoleId()}));
                
                log.info(this.getText(
                    "B2BPC0114",
                    new String[] {oldRole.getRoleId(),
                        this.getLoginIdOfCurrentUser()}));
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
    // private method
    // *****************************************************
    
    private boolean isSelectAllSuppliers()
    {
        if (null != selectedSuppliers && selectedSuppliers.size() == 1)
        {
            Object obj = selectedSuppliers.get(0);
            
            if (obj instanceof String)
            {
                String sup = (String) obj;
                if ("-1".equals(sup))
                {
                    List<SupplierHolder> newSups = new ArrayList<SupplierHolder>();
                    SupplierHolder newSup = new SupplierHolder();
                    newSup.setSupplierOid(BigDecimal.valueOf(-1));
                    newSup.setSupplierName(ALL_SUPPLIERS);
                    newSups.add(newSup);
                    selectedSuppliers = newSups;
                    
                    return true;
                }
            }
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
    
    
    private List<? extends Object> removeSelectedSuppliersFromAvailableList(
        List<SupplierHolder> availableList, List<? extends Object> selectedList)
    {
        List<SupplierHolder> tmpList = new ArrayList<SupplierHolder>();
        for (SupplierHolder sup : availableList)
        {
            if (selectedList.contains(sup.getSupplierOid()))
            {
                tmpList.add(sup);
            }
        }
        
        availableList.removeAll(tmpList);
        return tmpList;
    }
    
    
    private List<? extends Object> removeSelectedOperationsFromAvailableList(
        List<? extends Object> availableList, List<? extends Object> selectedList)
    {
        List<Object> tmpList = new ArrayList<Object>();
        for (Object obj : availableList)
        {
            OperationHolder oh = (OperationHolder) obj;
            if (selectedList.contains(oh.getOpnId()))
            {
                tmpList.add(oh);
            }
        }
        
        availableList.removeAll(tmpList);
        return tmpList;
    }


    private void filterOperations(List<? extends Object> targetList) throws Exception
    {
        // below codes filter operations that is not accessible of current login user.
        if (param.getUserTypeOid().equals(this.getProfileOfCurrentUser().getUserType()))
        {
            // creating role of the same user type.
            List<OperationHolder> curUserOpns = operationService
                .selectOperationByUser(getProfileOfCurrentUser()
                    .getUserOid());
            
            for (int i = 0; i < targetList.size(); i++)
            {
                if (!curUserOpns.contains(targetList.get(i)))
                {
                    targetList.remove(i);
                    i--;
                }
            }
        }
        else
        {
            // creating role of sub user type.
            List<OperationHolder> curUserOpns = operationService
                .selectOperationByUser(getProfileOfCurrentUser()
                    .getUserOid());
            List<OperationHolder> sharedOpns = operationService
                .selectOperationSharedBetweenUserTypes(param.getUserTypeOid(),
                    getProfileOfCurrentUser().getUserType());

            for (int i = 0; i < targetList.size(); i++)
            {
                if (sharedOpns.contains(targetList.get(i)) && !curUserOpns.contains(targetList.get(i)))
                {
                    targetList.remove(i);
                    i--;
                }
            }
        }
        
        // end added
    }
    
    
    private boolean isOperationEmpty(List<? extends Object> selectedOperations)
    {
        if (null == selectedOperations || selectedOperations.isEmpty())
        {
            this.addActionError(this.getText("B2BPC0105"));
            
            return true;
        }
        
        return false;
    }
    
    
    private boolean isRoleIdTooLong(String roleId)
    {
        if (roleId.trim().length() > MAX_LENGTH_ROLE_ID)
        {
            this.addActionError(this.getText("B2BPC0116"));
            
            return true;
        }
        
        return false;
    }
    
    
    private boolean isRoleNameTooLong(String roleName)
    {
        if (roleName.trim().length() > MAX_LENGTH_ROLE_NAME)
        {
            this.addActionError(this.getText("B2BPC0119"));
            
            return true;
        }
        
        return false;
    }
    
    
    private boolean isBuyerNotSelected(RoleTmpExHolder param)
    {
        BigDecimal curUserType = this.getUserTypeOfCurrentUser();
        BigDecimal targetUserType = param.getUserTypeOid();
        
        if (  (BigDecimal.valueOf(2).equals(targetUserType) 
            || BigDecimal.valueOf(4).equals(targetUserType)
            || BigDecimal.valueOf(6).equals(targetUserType)
            || BigDecimal.valueOf(7).equals(targetUserType)) 
            && BigDecimal.valueOf(-1).equals(param.getBuyerOid()) && BigDecimal.valueOf(1).equals(curUserType))
        {
            this.addActionError(this.getText("B2BPC0128"));
            return true;
        }
        
        return false;
    }
    
    
    private boolean isSupplierNotSelected(
        List<? extends Object> selectedSuppliers, RoleTmpExHolder param,
        boolean forEdit) throws Exception
    {
        BigDecimal curUserType = this.getUserTypeOfCurrentUser();
        
        if (forEdit)
        {
            RoleTmpHolder me = roleTmpService.selectRoleByKey(param.getRoleOid());
            
            if ((BigDecimal.valueOf(1).equals(curUserType) || BigDecimal.valueOf(2).equals(curUserType)) && 
                (BigDecimal.valueOf(3).equals(me.getUserTypeOid()) || BigDecimal.valueOf(5).equals(me.getUserTypeOid())) &&
                (null == selectedSuppliers || selectedSuppliers.isEmpty()))
            {
                this.addActionError(this.getText("B2BPC0129"));
                
                return true;
            }
        }
        else
        {
            BigDecimal targetUserType = param.getUserTypeOid();
            
            if ((BigDecimal.valueOf(1).equals(curUserType) || BigDecimal.valueOf(2).equals(curUserType)) && 
                (BigDecimal.valueOf(3).equals(targetUserType) || BigDecimal.valueOf(5).equals(targetUserType)) &&
                (null == selectedSuppliers || selectedSuppliers.isEmpty()))
            {
                this.addActionError(this.getText("B2BPC0129"));
                
                return true;
            }
            
        }
        
        return false;
    }
    
    
    private boolean isRoleIdAlreadyExist(String roleId, RoleTmpExHolder param,
        List<? extends Object> selectedSuppliers, boolean forEdit)
        throws Exception
    {
        BigDecimal curUserType = this.getUserTypeOfCurrentUser();
        
        if (forEdit)
        {
            RoleTmpHolder me = roleTmpService.selectRoleByKey(param.getRoleOid());
            RoleTmpHolder any = null;
            
            if (RoleType.ADMIN.equals(me.getRoleType()))
            {
                any = roleTmpService.selectAdminRoleById(roleId);
            }
            else if(RoleType.BUYER.equals(me.getRoleType()))
            {
                any = roleTmpService.selectBuyerRoleById(me.getBuyerOid(), roleId);
            }
            else if (BigDecimal.valueOf(3).equals(me.getUserTypeOid()))
            {
                if(BigDecimal.valueOf(1).equals(curUserType)
                    || BigDecimal.valueOf(2).equals(curUserType))
                {
                    for (Object objOid : selectedSuppliers)
                    {
                        BigDecimal supOid = new BigDecimal(objOid.toString());
                        any = roleTmpService.selectSupplierRoleById(supOid, roleId);
                        
                        if (null != any)
                        {
                            break;
                        }
                    }
                }
                else if (BigDecimal.valueOf(3).equals(curUserType))
                {
                    any = roleTmpService.selectSupplierRoleById(this
                        .getProfileOfCurrentUser().getSupplierOid(), roleId);
                }
            }
            else if (BigDecimal.valueOf(5).equals(me.getUserTypeOid()))
            {
                if (BigDecimal.valueOf(3).equals(curUserType))
                {
                    any = roleTmpService.selectSupplierRoleById(this
                        .getProfileOfCurrentUser().getSupplierOid(), roleId);
                }
                else if(BigDecimal.valueOf(1).equals(curUserType)
                    || BigDecimal.valueOf(2).equals(curUserType))
                {
                    for (Object objOid : selectedSuppliers)
                    {
                        BigDecimal supOid = new BigDecimal(objOid.toString());
                        any = roleTmpService.selectSupplierRoleById(supOid, roleId);
                        
                        if (null != any)
                        {
                            break;
                        }
                    }
                }
            }
            
            
            if (null != any && !any.getRoleOid().equals(me.getRoleOid()))
            {
                this.addActionError(this.getText("B2BPC0104", new String[]{param.getRoleId()}));
                
                return true;
            }
        }
        else
        {
            BigDecimal targetUserType = param.getUserTypeOid();
            RoleTmpHolder oldObj = null;
            
            if (BigDecimal.ONE.equals(targetUserType))
            {
                oldObj = roleTmpService.selectAdminRoleById(roleId);
            }
            else if (BigDecimal.valueOf(2).equals(targetUserType)
                    || BigDecimal.valueOf(4).equals(targetUserType)
                    || BigDecimal.valueOf(6).equals(targetUserType)
                    || BigDecimal.valueOf(7).equals(targetUserType))
            {
                if (BigDecimal.ONE.equals(curUserType))
                {
                    oldObj = roleTmpService.selectBuyerRoleById(param.getBuyerOid(),
                        roleId);
                }
                else
                {
                    if(BigDecimal.valueOf(2).equals(curUserType)
                        || BigDecimal.valueOf(6).equals(curUserType))
                    {
                        oldObj = roleTmpService.selectBuyerRoleById(this
                            .getProfileOfCurrentUser().getBuyerOid(), roleId);
                    }
                }
                
            }
            else if (BigDecimal.valueOf(3).equals(targetUserType))
            {
                if(BigDecimal.valueOf(1).equals(curUserType)
                    || BigDecimal.valueOf(2).equals(curUserType))
                {
                    for (Object objOid : selectedSuppliers)
                    {
                        BigDecimal supOid = new BigDecimal(objOid.toString());
                        oldObj = roleTmpService.selectSupplierRoleById(supOid, roleId);
                        
                        if (null != oldObj)
                        {
                            break;
                        }
                    }
                }
                else if (BigDecimal.valueOf(3).equals(curUserType))
                {
                    oldObj = roleTmpService.selectSupplierRoleById(this
                        .getProfileOfCurrentUser().getSupplierOid(), roleId);
                }
            }
            else if (BigDecimal.valueOf(5).equals(targetUserType))
            {
                if (BigDecimal.valueOf(3).equals(curUserType))
                {
                    oldObj = roleTmpService.selectSupplierRoleById(this
                        .getProfileOfCurrentUser().getSupplierOid(), param
                        .getRoleId());
                }
                else if (BigDecimal.valueOf(1).equals(curUserType) || BigDecimal.valueOf(2).equals(curUserType))
                {
                    for (Object objOid : selectedSuppliers)
                    {
                        BigDecimal supOid = new BigDecimal(objOid.toString());
                        oldObj = roleTmpService.selectSupplierRoleById(supOid, roleId);
                        
                        if (null != oldObj)
                        {
                            break;
                        }
                    }
                }
                
            }
            
            if (null != oldObj)
            {
                this.addActionError(this.getText("B2BPC0104", new String[]{param.getRoleId()}));
                
                return true;
            }
        }
        
        return false;
    }
    
    
    // *****************************************************
    // setter and getter method
    // *****************************************************

    public RoleTmpExHolder getParam()
    {
        return param;
    }


    public void setParam(RoleTmpExHolder param)
    {
        this.param = param;
    }


    public Map<String, String> getRoleTypes()
    {
        return roleTypes;
    }


    public Map<String, String> getCtrlStatuses()
    {
        return ctrlStatuses;
    }


    public List<UserTypeHolder> getUserTypes()
    {
        return userTypes;
    }


    public List<BuyerHolder> getBuyers()
    {
        return buyers;
    }


    public List<? extends Object> getSelectedSuppliers()
    {
        return selectedSuppliers;
    }


    public void setSelectedSuppliers(List<? extends Object> selectedSuppliers)
    {
        this.selectedSuppliers = selectedSuppliers;
    }


    public List<SupplierHolder> getAvailableSuppliers()
    {
        return availableSuppliers;
    }


    public List<? extends Object> getOperations()
    {
        return operations;
    }


    public List<? extends Object> getSelectedOperations()
    {
        return selectedOperations;
    }


    public void setSelectedOperations(List<Object> selectedOperations)
    {
        this.selectedOperations = selectedOperations;
    }


    public RoleTmpExHolder getOldParam()
    {
        return oldParam;
    }


    public void setOldParam(RoleTmpExHolder oldParam)
    {
        this.oldParam = oldParam;
    }


    public List<? extends Object> getOldSelectedOperations()
    {
        return oldSelectedOperations;
    }


    public void setOldSelectedOperations(List<Object> oldSelectedOperations)
    {
        this.oldSelectedOperations = oldSelectedOperations;
    }


    public String getAjaxMsg()
    {
        return ajaxMsg;
    }


    public List<RoleTmpHolder> getCreateList()
    {
        return createList;
    }


    public List<RoleTmpHolder> getUpdateList()
    {
        return updateList;
    }


    public List<RoleTmpHolder> getDeleteList()
    {
        return deleteList;
    }


    public String getConfirmType()
    {
        return confirmType;
    }


    public List<? extends Object> getOldSelectedSuppliers()
    {
        return oldSelectedSuppliers;
    }


    public boolean isCompanyChanged()
    {
        return companyChanged;
    }

//    public List<SupplierHolder> getSuppliers()
//    {
//        return suppliers;
//    }

}
