package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.constants.RoleType;
import com.pracbiz.b2bportal.core.constants.SummaryPageAccessType;
import com.pracbiz.b2bportal.core.constants.UserType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.OperationHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.SummaryFieldHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.ToolTipHolder;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.CcSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.ClassExHolder;
import com.pracbiz.b2bportal.core.holder.extension.CnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.GiSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.GrnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.RtvSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.SalesSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.SubclassExHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserTmpService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.CcHeaderService;
import com.pracbiz.b2bportal.core.service.ClassService;
import com.pracbiz.b2bportal.core.service.CnHeaderService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.GiHeaderService;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.OperationService;
import com.pracbiz.b2bportal.core.service.PnHeaderService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.SalesHeaderService;
import com.pracbiz.b2bportal.core.service.SubclassService;
import com.pracbiz.b2bportal.core.service.SummaryFieldService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.core.service.ToolTipService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserClassService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserSubclassService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class AjaxAction extends ProjectBaseAction implements CoreCommonConstants
{
    private static final long serialVersionUID = 5138691087742256202L;
    private static final Logger log = LoggerFactory.getLogger(AjaxAction.class);
    
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    private static final String SESSION_KEY_SEARCH_PARAMETER_PO = "SEARCH_PARAMETER_PO";
    private static final String SESSION_KEY_SEARCH_PARAMETER_RTV = "SEARCH_PARAMETER_RTV";
    private static final String SESSION_KEY_SEARCH_PARAMETER_GRN = "SEARCH_PARAMETER_GRN";
    private static final String SESSION_KEY_SEARCH_PARAMETER_INV = "SEARCH_PARAMETER_INV";
    private static final String SESSION_KEY_SEARCH_PARAMETER_DN = "SEARCH_PARAMETER_DN";
    private static final String SESSION_KEY_SEARCH_PARAMETER_PN = "SEARCH_PARAMETER_PN";
    private static final String SESSION_KEY_SEARCH_PARAMETER_GI = "SEARCH_PARAMETER_GI";
    private static final String SESSION_KEY_SEARCH_PARAMETER_CC = "SEARCH_PARAMETER_CC";
    private static final String SESSION_KEY_SEARCH_PARAMETER_SALES = "SEARCH_PARAMETER_SALES";
    private static final String SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING = "SEARCH_PARAMETER_PIGD_MATCHING";
    private static final String SESSION_KEY_SEARCH_PARAMETER_CN = "SEARCH_PARAMETER_CN";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER_PO = "session.parameter.PurchaseOrderAction.selectedOids";
    private static final String SESSION_OID_PARAMETER_RTV = "session.parameter.GoodsReturnNoteAction.selectedOids";
    private static final String SESSION_OID_PARAMETER_GRN = "session.parameter.GoodsReceivedNoteAction.selectedOids";
    private static final String SESSION_OID_PARAMETER_INV = "session.parameter.eInvoice.selectedOids";
    private static final String SESSION_OID_PARAMETER_DN = "session.parameter.DebitNoteAction.selectedOids";
    private static final String SESSION_OID_PARAMETER_PN = "session.parameter.PaymentAdviceAction.selectedOids";
    private static final String SESSION_OID_PARAMETER_GI = "session.parameter.GoodsIssueAction.selectedOids";
    private static final String SESSION_OID_PARAMETER_CC = "session.parameter.CreditClaimAction.selectedOids";
    private static final String SESSION_OID_PARAMETER_DSD = "session.parameter.sales.selectedOids";
    private static final String SESSION_OID_PARAMETER_CN = "session.parameter.CreditNoteAction.selectedOids";
    private static final String SESSION_OID_PARAMETER_MATCHING = "session.parameter.PoInvGrnDnMatchingAction.selectedOids";
    private static final String TASKLIST_DN_PRICE_OIDS = "tasklistDnPriceOids";
    private static final String TASKLIST_DN_QTY_OIDS = "tasklistDnQtyOids";
    private static final String TASKLIST_DN_CLOSE_OIDS = "tasklistDnCloseOids";
    private static final String TASKLIST_DN_SUPP_OIDS = "tasklistDnSuppOids";
    private static final String TASKLIST_MATCHING_AUDIT_PRICE_OIDS = "tasklistMatchingAuditPriceOids";
    private static final String TASKLIST_MATCHING_AUDIT_QTY_OIDS = "tasklistMatchingAuditQtyOids";
    private static final String TASKLIST_MATCHING_CLOSE_OIDS = "tasklistMatchingCloseOids";
    private static final String TASKLIST_MATCHING_APPROVE_INV_OIDS = "tasklistMatchingApproveInvOids";
    private static final String TASKLIST_MATCHING_SUPP_OIDS = "tasklistMatchingSuppOids";

    private BigDecimal userTypeOid;
    private BigDecimal companyOid;
    private String companyOids;
    private BigDecimal groupOid;
    private BigDecimal supplierOid;
    private String pageId;
    private String fieldId;
    private String msgType;
    private String emailAddrs;
    private String emailRlt;
    private RoleType roleType;
    private String printDocMsg;
    private String printType;
    private boolean selectAll;
    
    private List<? extends Object> operations;
    private List<? extends Object> roles;
    private List<? extends Object> groups;
    private List<? extends Object> selectedRoles;
    private List<? extends Object> suppliers;
    private List<? extends Object> users;
    private List<? extends Object> tradingPartners;
    private List<? extends Object> stores;
    private List<? extends Object> areas;
    private List<? extends Object> classes;
    private List<? extends Object> subclasses;
    private List<? extends Object> availableBuyerList;
    private List<? extends Object> userTypeList;
    private SupplierHolder supplier;
    private BuyerHolder buyer;
    private TermConditionHolder termCondition;
    private Map<String,List<ToolTipHolder>> tooltips;
    private List<SummaryFieldHolder> gridLayoutList;
    private Map<String, String> sortMap;
    
    @Autowired transient OperationService operationService;
    @Autowired transient RoleService roleService;
    @Autowired transient GroupService groupService;
    @Autowired transient UserTypeService userTypeService;
    @Autowired transient UserProfileService userProfileService;
    @Autowired transient SupplierService supplierService;
    @Autowired transient TradingPartnerService tradingPartnerService;
    @Autowired transient BuyerService buyerService;
    @Autowired transient TermConditionService termConditionService;
    @Autowired transient SummaryFieldService summaryFieldService;
    @Autowired transient ToolTipService toolTipService;
    @Autowired transient BuyerStoreAreaService buyerStoreAreaService;
    @Autowired transient BuyerStoreService buyerStoreService;
    @Autowired transient BuyerStoreAreaUserTmpService buyerStoreAreaUserTmpService;
    @Autowired transient AllowedAccessCompanyService allowedAccessCompanyService;
    @Autowired transient ClassService classService;
    @Autowired transient SubclassService subclassService;
    @Autowired transient ControlParameterService controlParameterService;
    @Autowired transient PoHeaderService poHeaderService;
    @Autowired transient GroupSupplierService groupSupplierService;
    @Autowired transient GroupTradingPartnerService groupTradingPartnerService;
    @Autowired transient BuyerStoreUserService buyerStoreUserService;
    @Autowired transient RtvHeaderService rtvHeaderService;
    @Autowired transient GrnHeaderService grnHeaderService;
    @Autowired transient InvHeaderService invHeaderService;
    @Autowired transient DnHeaderService dnHeaderService;
    @Autowired transient PnHeaderService pnHeaderService;
    @Autowired transient GiHeaderService giHeaderService;
    @Autowired transient CcHeaderService ccHeaderService;
    @Autowired transient SalesHeaderService salesHeaderService;
    @Autowired transient CnHeaderService cnHeaderService;
    @Autowired transient PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired transient GroupUserService groupUserService;
    @Autowired transient UserClassService userClassService;
    @Autowired transient UserSubclassService userSubclassService;
    
    
    
    @SuppressWarnings("unchecked")
    public String findOperations()
    {
        try
        {
            if (getProfileOfCurrentUser().getUserType().equals(userTypeOid))
            {
                List<OperationHolder> curUserOpns = operationService.selectOperationByUser(getProfileOfCurrentUser().getUserOid());
                
                operations = curUserOpns;
                
                Comparator<OperationHolder> compOpn = new Comparator<OperationHolder>()
                {
                    @Override
                    public int compare(OperationHolder o1, OperationHolder o2)
                    {
                        return o1.getOpnDesc().compareTo(o2.getOpnDesc());
                    }
                };
                
                Collections.sort((List<OperationHolder>)operations, compOpn);
                
                return SUCCESS;
            }
            
            if (BigDecimal.valueOf(1).equals(userTypeOid))
            {
                operations = operationService.selectOperationByUserType(userTypeOid);
            }
            else if(userTypeOid.equals(BigDecimal.valueOf(2))
                || userTypeOid.equals(BigDecimal.valueOf(4))
                || userTypeOid.equals(BigDecimal.valueOf(6))
                || userTypeOid.equals(BigDecimal.valueOf(7)))
            {
                operations = operationService.selectOperationByBuyerAndUserType(companyOid, userTypeOid);
            }
            else if (userTypeOid.equals(BigDecimal.valueOf(3)))
            {
                operations = operationService.selectSupplierOperationByBuyerAndUserType(companyOid, userTypeOid);
            }
            else if (userTypeOid.equals(BigDecimal.valueOf(5)))
            {
                if (BigDecimal.valueOf(3).equals(this.getProfileOfCurrentUser().getUserType()))
                {
                    operations = operationService.selectOperationBySupplierAndUserType(companyOid, userTypeOid);
                }
                else if (BigDecimal.valueOf(1).equals(this.getProfileOfCurrentUser().getUserType()) ||
                    BigDecimal.valueOf(2).equals(this.getProfileOfCurrentUser().getUserType()))
                {
                    operations = operationService.selectSupplierOperationByBuyerAndUserType(companyOid, userTypeOid);
                }
            }
            else
            {
                throw new Exception("Unknow userType Oid");
            }
            
            
            // below codes filter operations that is not accessible of current login user.
            if (userTypeOid.equals(this.getProfileOfCurrentUser().getUserType()))
            {
                // creating role of the same user type.
                List<OperationHolder> curUserOpns = operationService
                    .selectOperationByUser(getProfileOfCurrentUser()
                        .getUserOid());
                
                for (int i = 0; i < operations.size(); i++)
                {
                    if (!curUserOpns.contains(operations.get(i)))
                    {
                        operations.remove(i);
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
                    .selectOperationSharedBetweenUserTypes(userTypeOid,
                        getProfileOfCurrentUser().getUserType());

                for (int i = 0; i < operations.size(); i++)
                {
                    if (sharedOpns.contains(operations.get(i)) && !curUserOpns.contains(operations.get(i)))
                    {
                        operations.remove(i);
                        i--;
                    }
                }
            }
            
            Comparator<OperationHolder> compOpn = new Comparator<OperationHolder>()
            {
                @Override
                public int compare(OperationHolder o1, OperationHolder o2)
                {
                    return o1.getOpnDesc().compareTo(o2.getOpnDesc());
                }
            };
            
            Collections.sort((List<OperationHolder>)operations, compOpn);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }

    
    public String findRolesByUser()
    {
        try
        {
            if (userTypeOid == null)
            {
                return SUCCESS;
            }
            if (userTypeOid.equals(BigDecimal.valueOf(1)))
            {
                roles = roleService.selectRolesByUserType(userTypeOid);
            }
            else if (userTypeOid.equals(getProfileOfCurrentUser().getUserType()))
            {
                roles = roleService.selectRolesByUserOid(getProfileOfCurrentUser().getUserOid());
            }
            else if (userTypeOid.equals(BigDecimal.valueOf(2))
                    || userTypeOid.equals(BigDecimal.valueOf(4))
                    || userTypeOid.equals(BigDecimal.valueOf(6))
                    || userTypeOid.equals(BigDecimal.valueOf(7)))
            {
                roles = roleService.selectBuyerRolesByBuyerOidAndUserType(companyOid, userTypeOid);
                
            }
            else if (userTypeOid.equals(BigDecimal.valueOf(3)) || userTypeOid.equals(BigDecimal.valueOf(5)))
            {
                roles = roleService.selectSupplierRolesBySupplierOidAndUserType(companyOid, userTypeOid);
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findRolesByBuyer()
    {
        try
        {
            if (companyOid == null || userTypeOid == null)
            {
                return SUCCESS;
            }
            roles = roleService.selectBuyerRolesByBuyerOidAndUserType(companyOid, userTypeOid);
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findRolesBySupplier()
    {
        try
        {
            if (companyOid == null || userTypeOid == null)
            {
                return SUCCESS;
            }
            roles = roleService.selectSupplierRolesBySupplierOidAndUserType(companyOid, userTypeOid);
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    

    public String findGroupsByUser()
    {
        try
        {
            if (userTypeOid == null)
            {
                return SUCCESS;
            }
            if (userTypeOid.equals(getProfileOfCurrentUser().getUserType()))
            {
                List<GroupHolder> tmpGroups = new ArrayList<GroupHolder>();
                GroupHolder group = groupService.selectGroupByUserOid(getProfileOfCurrentUser().getUserOid());
                if (group != null)
                {
                    tmpGroups.add(group);
                }
                groups = tmpGroups;
            }
            else if (userTypeOid.equals(BigDecimal.valueOf(2)) || userTypeOid.equals(BigDecimal.valueOf(4)))
            {
                GroupHolder param = new GroupHolder();
                param.setUserTypeOid(userTypeOid);
                param.setBuyerOid(companyOid);
                groups = groupService.select(param);
            }
            else if (userTypeOid.equals(BigDecimal.valueOf(3)) || userTypeOid.equals(BigDecimal.valueOf(5)))
            {
                GroupHolder param = new GroupHolder();
                param.setUserTypeOid(userTypeOid);
                param.setSupplierOid(companyOid);
                groups = groupService.select(param);
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findSupplierByKey()
    {
        try
        {
            if (companyOid == null)
            {
                return SUCCESS;
            }
            supplier = supplierService.selectSupplierByKey(companyOid);
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String findSupplierByBuyer()
    {
        try
        {
            suppliers = supplierService.selectSupplierByBuyerOid(companyOid);
            
            Comparator<SupplierHolder> compSup = new Comparator<SupplierHolder>()
            {
                @Override
                public int compare(SupplierHolder o1, SupplierHolder o2)
                {
                    return o1.getSupplierName().compareTo(o2.getSupplierName());
                }
            };
            
            Collections.sort((List<SupplierHolder>)suppliers, compSup);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        
        return SUCCESS;
    }
    
    
    public String findBuyerByKey()
    {
        try
        {
            if (companyOid == null)
            {
                return SUCCESS;
            }
            buyer = buyerService.selectBuyerByKey(companyOid);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findDatasByBuyerOrSupplier()
    {
        
        try
        {
            if (userTypeOid.equals(BigDecimal.valueOf(2)) || userTypeOid.equals(BigDecimal.valueOf(4)))
            {
                roles = roleService.selectBuyerRolesByBuyerOidAndUserType(companyOid, userTypeOid);
                suppliers = supplierService.selectSupplierByTmpGroupOidAndBuyerOid(groupOid, companyOid);
            }
            if (userTypeOid.equals(BigDecimal.valueOf(3)) || userTypeOid.equals(BigDecimal.valueOf(5)))
            {
                roles = roleService.selectSupplierRolesBySupplierOidAndUserType(companyOid, userTypeOid);
                tradingPartners = tradingPartnerService.selectTradingPartnerByTmpGroupOidAndSupplierOid(groupOid, companyOid);
            }
            
            if (roles == null || roles.isEmpty())
            {
                return SUCCESS;
            }
            
            tradingPartners = new ArrayList<TradingPartnerExHolder>();
            users = new ArrayList<UserProfileHolder>();
            selectedRoles = new ArrayList<RoleHolder>();
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
   
    
    public String findDefaultTermConditionBySupplier()
    {
        try
        {
            if (companyOid == null)
            {
                return SUCCESS;
            }
            termCondition = termConditionService.selectDefaultTermConditionBySupplierOid(companyOid);
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findSummaryFieldAndTooltips()
    {
        try
        {
            String accessType = getAccessType();
            if(accessType == null)
            {
                Exception e = new Exception(
                    "Sorry, you do not have permission to access this page!- "
                        + TransactionalDocsBaseAction.class.getName()
                        + ".obtainListRecordsOfPagination()");
                
                ErrorHelper.getInstance().logTicketNo(log, this, e);
            }
            
            gridLayoutList = this.summaryFieldService
                .selectSummaryAvailableFieldsByPageIdAndAccessType(pageId,
                    accessType, this);
            sortMap = this.summaryFieldService
                .selectSortFieldByPageIdAndAccessType(pageId, accessType,
                    this);
            tooltips = this.toolTipService.selectTooltipsByPageIdAndAccessType(
                pageId, accessType, this);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        
        return SUCCESS;
    }
    
    
    
    
    
    public String findAreasByUser()
    {
        try
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(companyOid);
            UserProfileHolder user = this.getProfileOfCurrentUser();
            if (user.getUserType().equals(BigDecimal.ONE))
            {
                areas = buyerStoreAreaService.selectBuyerStoreAreaByBuyer(buyer.getBuyerCode());
            }
            else
            {
                areas = buyerStoreAreaService.selectBuyerStoreAreaByUserOid(user.getUserOid());
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findStoresByUser()
    {
        try
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(companyOid);
            UserProfileHolder user = this.getProfileOfCurrentUser();
            if (user.getUserType().equals(BigDecimal.ONE))
            {
                stores = buyerStoreService.selectBuyerStoresByBuyerAndIsWareHouse(buyer.getBuyerCode(), false);
            }
            else
            {
                stores = buyerStoreService.selectBuyerStoresByUserOidAndIsWareHouse(user.getUserOid(), false);
            }
            
           
            if(stores != null && !stores.isEmpty())
            {
                for (Object obj : stores)
                {
                    BuyerStoreHolder store = (BuyerStoreHolder) obj;
                    store.setStoreCode( LEFT_BRACKET + store.getStoreCode() + RIGHT_BRACKET);
                    if (store.getStoreName() == null)
                    {
                        store.setStoreName("");
                    }
                }
            }  
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findSubclassByUser()
    {
        try
        {
            UserProfileHolder user = this.getProfileOfCurrentUser();
            
            List<BigDecimal> buyerOids = new ArrayList<BigDecimal>();
            String[] companyArray = companyOids.split(",");
            for (String buyerOid : companyArray)
            {
                buyerOids.add(new BigDecimal(buyerOid));
            }
            
            List<SubclassExHolder> allSubclass = new ArrayList<SubclassExHolder>();
            
            if (user.getUserType().equals(BigDecimal.ONE))
            {
                for (BigDecimal buyerOid : buyerOids)
                {
                    List<SubclassExHolder> sList = subclassService.selectSubclassExByBuyerOid(buyerOid);
                    if (sList != null && !sList.isEmpty())
                    {
                        allSubclass.addAll(sList);
                    }
                }
                
                subclasses = allSubclass;
            }
            else
            {
                subclasses = UserProfileAction.filterSubclassByBuyerOids(subclassService.selectSubclassExByUserOid(user.getUserOid()), buyerOids);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findClassByUser()
    {
        try
        {
            UserProfileHolder user = this.getProfileOfCurrentUser();
            
            List<BigDecimal> buyerOids = new ArrayList<BigDecimal>();
            String[] companyArray = companyOids.split(",");
            for (String buyerOid : companyArray)
            {
                buyerOids.add(new BigDecimal(buyerOid));
            }
            
            List<ClassExHolder> allClass = new ArrayList<ClassExHolder>();
            
            if (user.getUserType().equals(BigDecimal.ONE))
            {
                for (BigDecimal buyerOid : buyerOids)
                {
                    List<ClassExHolder> cList = classService.selectClassByBuyerOid(buyerOid);
                    if (cList != null && !cList.isEmpty())
                    {
                        allClass.addAll(cList);
                    }
                }
                
                classes = allClass;
            }
            else
            {
                classes = UserProfileAction.filterClassByBuyerOids(classService.selectClassByUserOid(user.getUserOid()), buyerOids);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findWareHouseByUser()
    {
        try
        {   
            BuyerHolder buyer = buyerService.selectBuyerByKey(companyOid);
            UserProfileHolder user = this.getProfileOfCurrentUser();
            if (user.getUserType().equals(BigDecimal.ONE))
            {
                stores = buyerStoreService.selectBuyerStoresByBuyerAndIsWareHouse(buyer.getBuyerCode(), true);
            }
            else
            {
                stores = buyerStoreService.selectBuyerStoresByUserOidAndIsWareHouse(user.getUserOid(), true);
            }
            
            if(stores != null && !stores.isEmpty())
            {
                for (Object obj : stores)
                {
                    BuyerStoreHolder store = (BuyerStoreHolder) obj;
                    store.setStoreCode(LEFT_BRACKET + store.getStoreCode() + RIGHT_BRACKET);
                    if (store.getStoreName() == null)
                    {
                        store.setStoreName("");
                    }
                }
            }  
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String findAvailableBuyersByBuyerOid()
    {
        try
        {   
            if (companyOid == null)
            {
                return SUCCESS;
            }
            BuyerHolder currentBuyer = buyerService.selectBuyerByKey(companyOid);
            if (currentBuyer == null)
            {
                return SUCCESS;
            }
            List<BuyerHolder> allBuyers = null;
            if (BigDecimal.valueOf(1).equals(this.getProfileOfCurrentUser().getUserType()))
            {
                allBuyers = buyerService.select(new BuyerHolder());
            }
            else
            {
                allBuyers = allowedAccessCompanyService.selectBuyerByUserOid(this.getProfileOfCurrentUser().getUserOid());
            }
            if (allBuyers == null || allBuyers.isEmpty())
            {
                return SUCCESS;
            }
            for (BuyerHolder buyer : allBuyers)
            {
                if (buyer.getBuyerOid().equals(currentBuyer.getBuyerOid()))
                {
                    allBuyers.remove(buyer);
                    break;
                }
            }
            availableBuyerList = allBuyers;
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, this, e);
        }
        return SUCCESS;
        
    }
    
    
    public String sentTestMailForMsgtype()
    {
        try
        {
            if (null == emailAddrs || emailAddrs.trim().isEmpty())
            {
                emailRlt = this.getText("B2BPC0498", new String[] {msgType});
                return SUCCESS;
            }
            
            String[] emails = emailAddrs.split(",");
            
            for (String email : emails)
            {
                email = email.trim();
                
                if (!email.trim().matches("^[\\.a-zA-Z0-9_-]+@[\\.a-zA-Z0-9_-]+(\\.[\\.a-zA-Z0-9_-]+)+$"))
                {
                    emailRlt = this.getText("B2BPC0499", new String[]{email.trim()});
                    return SUCCESS;
                }
            }
            
            emailEngine.sendHtmlEmail(emails, "Test alert message for " + msgType, "testMsgEmail.vm", new HashMap<String, Object>());
            emailRlt = this.getText("B2BPC0497");
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        
        return SUCCESS;
    }
    
    
    public String findBuyerByCurrentSupplier()
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        } 
      
        List<TradingPartnerExHolder> tps= null;
        try
        {
            tps = tradingPartnerService.selectTradingPartnerBySupplierOid(supplierOid);
            
            if (tps == null)
            {
                return SUCCESS;
            }
            
            Map<BigDecimal, BigDecimal> buyerMap = null;
            List<BuyerHolder> buyers = null;
            if (tps.size() > 1)
            {
                buyerMap = new HashMap<BigDecimal, BigDecimal>();
                buyers = new ArrayList<BuyerHolder>();
                for (TradingPartnerExHolder tp : tps)
                {
                    if (buyerMap.containsKey(tp.getBuyerOid()))
                    {
                        continue;
                    }
                    else
                    {
                        buyers.add(buyerService.selectBuyerByKey(tp.getBuyerOid()));
                        buyerMap.put(tp.getBuyerOid(), tp.getBuyerOid());
                    }
                }
            }
            
            if (tps.size() == 1)
            {
                buyers = new ArrayList<BuyerHolder>();
                buyers.add(buyerService.selectBuyerByKey(tps.get(0).getBuyerOid()));
            }
            
            if (buyers != null && !buyers.isEmpty())
            {
                availableBuyerList = buyers;
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        
        return SUCCESS;
    }
    
    
    private String getAccessType()
    {
        UserProfileHolder currentUser = this.getProfileOfCurrentUser();
        if(currentUser.getBuyerOid() != null)
        {
            return SummaryPageAccessType.B.name();
        }

        if(currentUser.getSupplierOid() != null)
        {
            return SummaryPageAccessType.S.name();
        }

        return null;
    }
    
    
    protected UserProfileHolder getProfileOfCurrentUser()
    {
        return (UserProfileHolder) this.getSession().get(CoreCommonConstants.SESSION_KEY_USER);
    }
    
    
    public String findUserTypes()
    {
        try
        {
            List<UserTypeHolder> userTypes = userTypeService.selectPrivilegedSubUserTypesByUserTypeInclusively(this.getProfileOfCurrentUser().getUserType());
            
            Iterator<UserTypeHolder> it = userTypes.iterator();
            
            while (it.hasNext())
            {
                UserTypeHolder type = it.next();
                
                if (!type.getRoleType().equals(roleType))
                {
                    it.remove();
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
            userTypeList = userTypes;
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String checkDocCount()
    {
        try
        {
            int docCount = 0;
            
            ControlParameterHolder maxPdfCount = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_CTRL, "MAX_PDF_COUNT");
            
            ControlParameterHolder maxExcelCount = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_CTRL, "MAX_EXCEL_COUNT");
            
            ControlParameterHolder maxSummaryExcelCount = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_CTRL, "MAX_SUMMARY_EXCEL_COUNT");
            
            if ("PO".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    PoSummaryHolder param = (PoSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_PO);
                    
                    if (param == null)
                    {
                        param = new PoSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_PO, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<PoSummaryHolder> poList = poHeaderService.selectAllRecordToExport(param);
                    docCount = poList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_PO);
                }
            }
            
            if ("RTV".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    RtvSummaryHolder param = (RtvSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_RTV);
                    
                    if (param == null)
                    {
                        param = new RtvSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_RTV, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<RtvSummaryHolder> rtvList = rtvHeaderService.selectAllRecordToExport(param);
                    docCount = rtvList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_RTV);
                }
            }
            
            if ("GRN".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    GrnSummaryHolder param = (GrnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_GRN);
                    
                    if (param == null)
                    {
                        param = new GrnSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_GRN, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<GrnSummaryHolder> grnList = grnHeaderService.selectAllRecordToExport(param);
                    docCount = grnList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_GRN);
                }
            }
            
            if ("INV".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    InvSummaryHolder param = (InvSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_INV);
                    
                    if (param == null)
                    {
                        param = new InvSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_INV, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<InvSummaryHolder> invList = invHeaderService.selectAllRecordToExport(param);
                    docCount = invList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_INV);
                }
            }
            
            if ("DN".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    DnSummaryHolder param = (DnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_DN);
                    
                    if (param == null)
                    {
                        param = new DnSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_DN, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<BigDecimal> dnOids = new ArrayList<BigDecimal>();
                    
                    if (null != param.getTaskListType())
                    {
                        List<BigDecimal> taskListOids = new ArrayList<BigDecimal>();
                        if ("price".equalsIgnoreCase(param.getTaskListType()))
                        {
                            taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_DN_PRICE_OIDS);
                        }
                        else if ("qty".equalsIgnoreCase(param.getTaskListType()))
                        {
                            taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_DN_QTY_OIDS);
                        }
                        else if ("close".equalsIgnoreCase(param.getTaskListType()))
                        {
                            taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_DN_CLOSE_OIDS);
                        }
                        else if ("supplierHandle".equalsIgnoreCase(param.getTaskListType()))
                        {
                            taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_DN_SUPP_OIDS);
                        }
                        dnOids.addAll(taskListOids);
                    }
                    else
                    {
                        List<DnSummaryHolder> dnList = dnHeaderService.selectAllRecordToExport(param);
                        
                        if (dnList != null && !dnList.isEmpty())
                        {
                            for (DnSummaryHolder dn : dnList)
                            {
                                dnOids.add(dn.getDocOid());
                            }
                        }
                    }
                    docCount = dnOids.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_DN);
                }
            }
            
            if ("PN".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    PnSummaryHolder param = (PnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_PN);
                    
                    if (param == null)
                    {
                        param = new PnSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_PN, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<PnSummaryHolder> pnList = pnHeaderService.selectAllRecordToExport(param);
                    docCount = pnList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_PN);
                }
            }
            
            if ("GI".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    GiSummaryHolder param = (GiSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_GI);
                    
                    if (param == null)
                    {
                        param = new GiSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_GI, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<GiSummaryHolder> giList = giHeaderService.selectAllRecordToExport(param);
                    docCount = giList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_GI);
                }
            }
            
            if ("CC".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    CcSummaryHolder param = (CcSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_CC);
                    
                    if (param == null)
                    {
                        param = new CcSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_CC, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<CcSummaryHolder> ccList = ccHeaderService.selectAllRecordToExport(param);
                    docCount = ccList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_CC);
                }
            }
            
            if ("DSD".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    SalesSummaryHolder param = (SalesSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_SALES);
                    
                    if (param == null)
                    {
                        param = new SalesSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_SALES, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<SalesSummaryHolder> salesList = salesHeaderService.selectAllRecordToExport(param);
                    docCount = salesList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_DSD);
                }
            }
            
            if ("CN".equalsIgnoreCase(msgType))
            {
                if (selectAll)
                {
                    CnSummaryHolder param = (CnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_CN);
                    
                    if (param == null)
                    {
                        param = new CnSummaryHolder();
                        getSession().put(SESSION_KEY_SEARCH_PARAMETER_CN, param);
                    }
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    List<CnSummaryHolder> cnList = cnHeaderService.selectAllRecordToExport(param);
                    docCount = cnList.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_CN);
                }
            }
            
            if ("MATCHING".equalsIgnoreCase(msgType))
            {
                List<BigDecimal> matchingOids = new ArrayList<BigDecimal>();
                if (selectAll)
                {
                    PoInvGrnDnMatchingExHolder param = (PoInvGrnDnMatchingExHolder) getSession().get(
                        SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING);
                if (param == null)
                {
                    param = new PoInvGrnDnMatchingExHolder();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING, param);
                }
                
                if (null != param.getMatchingStatus())
                {
                    param.setMatchingStatusValue(param.getMatchingStatus().name());
                }
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                if (null != param.getTaskListType())
                {
                    List<BigDecimal> taskListOids = new ArrayList<BigDecimal>();
                    param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.PENDING);
                    param.setPoDateFrom(null);
                    param.setPoDateTo(null);
                    if ("auditPrice".equalsIgnoreCase(param.getTaskListType()))
                    {
                        taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_MATCHING_AUDIT_PRICE_OIDS);
                    }
                    else if ("auditQty".equalsIgnoreCase(param.getTaskListType()))
                    {
                        taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_MATCHING_AUDIT_QTY_OIDS);
                    }
                    else if ("buyerClose".equalsIgnoreCase(param.getTaskListType()))
                    {
                        taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_MATCHING_CLOSE_OIDS);
                    }
                    else if ("buyerApprove".equalsIgnoreCase(param.getTaskListType()))
                    {
                        taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_MATCHING_APPROVE_INV_OIDS);
                    }
                    else if ("supplierHandle".equalsIgnoreCase(param.getTaskListType()))
                    {
                        taskListOids = (List<BigDecimal>)this.getSession().get(TASKLIST_MATCHING_SUPP_OIDS);
                    }
                    matchingOids.addAll(taskListOids);
                }
                else
                {
                    List<PoInvGrnDnMatchingHolder> poInvGrnDnMatchingList = poInvGrnDnMatchingService.selectAllRecordToExport(param);
                    if (poInvGrnDnMatchingList != null && !poInvGrnDnMatchingList.isEmpty())
                    {
                        for (PoInvGrnDnMatchingHolder holder : poInvGrnDnMatchingList)
                        {
                            matchingOids.add(holder.getMatchingOid());
                        }
                    }
                }
                docCount = matchingOids.size();
                }
                else
                {
                    docCount = this.getDocCountFromSession(SESSION_OID_PARAMETER_MATCHING);
                }
            }
            
            if ("pdf".equalsIgnoreCase(printType))
            {
                if (maxPdfCount.getNumValue() >= docCount)
                {
                    printDocMsg = "success";
                }
                else
                {
                    this.removeSessionOids();
                    printDocMsg = "The count of record(s) to print pdf cannot be more than " + maxPdfCount.getNumValue() + ".";
                }
            }
            else if ("excel".equalsIgnoreCase(printType))
            {
                if (maxExcelCount.getNumValue() >= docCount)
                {
                    printDocMsg = "success";
                }
                else
                {
                    this.removeSessionOids();
                    printDocMsg = "The count of record(s) to export excel cannot be more than " + maxExcelCount.getNumValue() + ".";
                }
            }
            else if ("sExcel".equalsIgnoreCase(printType))
            {
                if (maxSummaryExcelCount.getNumValue() >= docCount)
                {
                    printDocMsg = "success";
                }
                else
                {
                    this.removeSessionOids();
                    printDocMsg = "The count of record(s) to export summary excel cannot be more than " + maxSummaryExcelCount.getNumValue() + ".";
                }
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        return SUCCESS;
    }
    
    
    private void removeSessionOids()
    {
        this.getSession().remove(SESSION_OID_PARAMETER_PO);
        this.getSession().remove(SESSION_OID_PARAMETER_RTV);
        this.getSession().remove(SESSION_OID_PARAMETER_GRN);
        this.getSession().remove(SESSION_OID_PARAMETER_INV);
        this.getSession().remove(SESSION_OID_PARAMETER_DN);
        this.getSession().remove(SESSION_OID_PARAMETER_PN);
        this.getSession().remove(SESSION_OID_PARAMETER_GI);
        this.getSession().remove(SESSION_OID_PARAMETER_CC);
        this.getSession().remove(SESSION_OID_PARAMETER_DSD);
        this.getSession().remove(SESSION_OID_PARAMETER_CN);
        this.getSession().remove(SESSION_OID_PARAMETER_MATCHING);
    }
    
    
    private int getDocCountFromSession(String key) throws Exception
    {
        Object selectedOids = this.getSession().get(key);

        if(null == selectedOids)
        {
            throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
        }

        String[] parts = selectedOids.toString().split(
            REQUEST_OID_DELIMITER);
        
        return parts.length;
    }
    
    
    protected void initCurrentUserSearchParam(PoInvGrnDnMatchingExHolder searchParam)throws Exception
    {
        searchParam.setCurrentUserType(this.getUserTypeOfCurrentUser());
        searchParam.setCurrentUserOid(this.getProfileOfCurrentUser().getUserOid());
        searchParam.setCurrentUserBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        searchParam.setCurrentUserSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
        GroupUserHolder groupUser = groupUserService.selectByUserOid(this.getProfileOfCurrentUser().getUserOid());
        searchParam.setCurrentUserGroupOid(groupUser == null ? null : groupUser.getGroupOid());
        searchParam.setFullGroupPriv(false);
        searchParam.setVisiable(true);
        searchParam.setValidSupplierSet(false);
        searchParam.setFullClassPriv(false);
        
        boolean isBuyer = searchParam.getCurrentUserType().equals(BigDecimal.valueOf(2)) 
            || searchParam.getCurrentUserType().equals(BigDecimal.valueOf(4));
        boolean isSupplier = searchParam.getCurrentUserType().equals(BigDecimal.valueOf(3)) 
            || searchParam.getCurrentUserType().equals(BigDecimal.valueOf(5));
        boolean isStore = searchParam.getCurrentUserType().equals(BigDecimal.valueOf(6)) 
            || searchParam.getCurrentUserType().equals(BigDecimal.valueOf(7));
        
        //current is buyer user.
        if (isBuyer)
        {
            if (searchParam.getCurrentUserGroupOid() == null)//current buyer user not in a group.
            {
                searchParam.setVisiable(false);
            }
            else//current buyer user in a group, and group has all suppliers.
            {
                searchParam.setVisiable(true);
                if (searchParam.getListOid() == null)
                {
                    List<GroupSupplierHolder> groupSuppliers = groupSupplierService.selectGroupSupplierByGroupOid(searchParam.getCurrentUserGroupOid());
                    if (groupSuppliers != null && groupSuppliers.size() == 1 && groupSuppliers.get(0).getSupplierOid().equals(BigDecimal.valueOf(-1)))
                    {
                        searchParam.setFullGroupPriv(true);
                    }
                }
            }
            
            UserClassHolder uc = userClassService.selectByUserOidAndClassOid(searchParam.getCurrentUserOid(), BigDecimal.valueOf(-1));
            UserSubclassHolder us = userSubclassService.selectByUserOidAndSubclassOid(searchParam.getCurrentUserOid(), BigDecimal.valueOf(-1));
            
            if (uc != null || us != null)
            {
                searchParam.setFullClassPriv(true);
            }
        }
        //current is store user and has no store assigned.
        if (isStore || isBuyer)
        {
            BuyerStoreUserHolder allStore = buyerStoreUserService.selectStoreUserByStoreOidAndUserOid(BigDecimal.valueOf(-3), this.getProfileOfCurrentUser().getUserOid());
            BuyerStoreUserHolder allWareHouse = buyerStoreUserService.selectStoreUserByStoreOidAndUserOid(BigDecimal.valueOf(-4), this.getProfileOfCurrentUser().getUserOid());
            
            if (allStore == null || allWareHouse == null)
            {
                searchParam.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
            }
            else
            {
                searchParam.setAllStoreFlag(true);
            }
        }
        //current is supplier user.
        if (isSupplier)
        {
            searchParam.setVisiable(true);
            if (searchParam.getCurrentUserGroupOid() != null)//current supplier user in a group, and group has all trading partners.
            {
                List<GroupTradingPartnerHolder> groupTradingPartners = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(searchParam.getCurrentUserGroupOid());
                if (groupTradingPartners != null && groupTradingPartners.size() == 1 && groupTradingPartners.get(0).getTradingPartnerOid().compareTo(BigDecimal.valueOf(-1)) == 0)
                {
                    searchParam.setFullGroupPriv(true);
                }
            }

            SupplierHolder  supplier = supplierService.selectSupplierByKey(this.getProfileOfCurrentUser().getSupplierOid());
            
            if (null != supplier.getSetOid() && searchParam.getCurrentUserGroupOid() == null)
            {
                searchParam.setValidSupplierSet(true);
                searchParam.setSetOid(supplier.getSetOid());
            }
        }
    }
    
    
    private void initCurrentUserSearchParam(MsgTransactionsExHolder searchParam)throws Exception
    {
        searchParam.setCurrentUserType(this.getUserTypeOfCurrentUser());
        searchParam.setCurrentUserOid(this.getProfileOfCurrentUser().getUserOid());
        searchParam.setCurrentUserBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        searchParam.setCurrentUserSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
        GroupHolder group = groupService.selectGroupByUserOid(this.getProfileOfCurrentUser().getUserOid());
        if (group != null)
        {
            searchParam.setCurrentUserGroupOid(group.getGroupOid());
        }
        searchParam.setFullGroupPriv(false);
        searchParam.setVisiable(true);
        searchParam.setValidSupplierSet(false);
        
        boolean isBuyer = searchParam.getCurrentUserType().equals(BigDecimal.valueOf(2)) 
                    || searchParam.getCurrentUserType().equals(BigDecimal.valueOf(4));
        boolean isSupplier = searchParam.getCurrentUserType().equals(BigDecimal.valueOf(3)) 
                    || searchParam.getCurrentUserType().equals(BigDecimal.valueOf(5));
        boolean isStore = searchParam.getCurrentUserType().equals(BigDecimal.valueOf(6)) 
                    || searchParam.getCurrentUserType().equals(BigDecimal.valueOf(7));
        
        //current is buyer user.
        if (isBuyer)
        {
            if (searchParam.getCurrentUserGroupOid() == null)//current buyer user not in a group.
            {
                searchParam.setVisiable(false);
            }
            else//current buyer user in a group, and group has all suppliers.
            {
                searchParam.setVisiable(true);
                if (searchParam.getListOid() == null)
                {
                    List<GroupSupplierHolder> groupSuppliers = groupSupplierService.selectGroupSupplierByGroupOid(searchParam.getCurrentUserGroupOid());
                    if (groupSuppliers != null && groupSuppliers.size() == 1 && groupSuppliers.get(0).getSupplierOid().equals(BigDecimal.valueOf(-1)))
                    {
                        searchParam.setFullGroupPriv(true);
                    }
                }
            }
        }
        //current is store user and has no store assigned.
        if (isStore || isBuyer)
        {
            BuyerStoreUserHolder allStore = buyerStoreUserService.selectStoreUserByStoreOidAndUserOid(BigDecimal.valueOf(-3), this.getProfileOfCurrentUser().getUserOid());
            BuyerStoreUserHolder allWareHouse = buyerStoreUserService.selectStoreUserByStoreOidAndUserOid(BigDecimal.valueOf(-4), this.getProfileOfCurrentUser().getUserOid());
            
            if (allStore == null || allWareHouse == null)
            {
                searchParam.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
            }
            else
            {
                searchParam.setAllStoreFlag(true);
            }
        }
        //current is supplier user.
        if (isSupplier)
        {
            searchParam.setVisiable(true);
            if (searchParam.getCurrentUserGroupOid() != null)//current supplier user in a group, and group has all trading partners.
            {
                List<GroupTradingPartnerHolder> groupTradingPartners = groupTradingPartnerService.
                        selectGroupTradingPartnerByGroupOid(searchParam.getCurrentUserGroupOid());
                if (groupTradingPartners != null && groupTradingPartners.size() == 1 
                        && groupTradingPartners.get(0).getTradingPartnerOid().equals(BigDecimal.valueOf(-1)))
                {
                    searchParam.setFullGroupPriv(true);
                }
            }
            
            SupplierHolder  supplier = supplierService.selectSupplierByKey(this.getProfileOfCurrentUser().getSupplierOid());
            
            if (null != supplier.getSetOid() && searchParam.getCurrentUserGroupOid() == null)
            {
                searchParam.setValidSupplierSet(true);
                searchParam.setSetOid(supplier.getSetOid());
            }
        }
    }
    
    // *****************************************************
    // setter and getter method
    // *****************************************************

    public void setUserTypeOid(BigDecimal userTypeOid)
    {
        this.userTypeOid = userTypeOid;
    }


    public List<? extends Object> getOperations()
    {
        return operations;
    }


    public List<? extends Object> getRoles()
    {
        return roles;
    }


    public List<? extends Object> getGroups()
    {
        return groups;
    }


    public void setGroups(List<Object> groups)
    {
        this.groups = groups;
    }


    public BigDecimal getCompanyOid()
    {
        return companyOid;
    }


    public void setCompanyOid(BigDecimal companyOid)
    {
        this.companyOid = companyOid;
    }


    public String getCompanyOids()
    {
        return companyOids;
    }


    public void setCompanyOids(String companyOids)
    {
        this.companyOids = companyOids;
    }


    public RoleType getRoleType()
    {
        return roleType;
    }


    public void setRoleType(RoleType roleType)
    {
        this.roleType = roleType;
    }


    public List<? extends Object> getSelectedRoles()
    {
        return selectedRoles;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }


    public SupplierHolder getSupplier()
    {
        return supplier;
    }


    public BuyerHolder getBuyer()
    {
        return buyer;
    }


    public List<? extends Object> getSuppliers()
    {
        return suppliers;
    }

    public void setSuppliers(List<Object> suppliers)
    {
        this.suppliers = suppliers;
    }


    public List<? extends Object> getUsers()
    {
        return users;
    }


    public void setUsers(List<Object> users)
    {
        this.users = users;
    }


    public List<? extends Object> getTradingPartners()
    {
        return tradingPartners;
    }


    public void setTradingPartners(List<Object> tradingPartners)
    {
        this.tradingPartners = tradingPartners;
    }

    
    public TermConditionHolder getTermCondition()
    {
        return termCondition;
    }


    public Map<String,List<ToolTipHolder>> getTooltips()
    {
        return tooltips;
    }


    public void setTooltips(Map<String,List<ToolTipHolder>> tooltips)
    {
        this.tooltips = tooltips;
    }


    public List<SummaryFieldHolder> getGridLayoutList()
    {
        return gridLayoutList;
    }


    public void setGridLayoutList(List<SummaryFieldHolder> gridLayoutList)
    {
        this.gridLayoutList = gridLayoutList;
    }


    public List<? extends Object> getUserTypeList()
    {
        return userTypeList;
    }


    public void setUserTypeList(List<? extends Object> userTypeList)
    {
        this.userTypeList = userTypeList;
    }


    public String getPageId()
    {
        return pageId;
    }


    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }


    public String getFieldId()
    {
        return fieldId;
    }


    public void setFieldId(String fieldId)
    {
        this.fieldId = fieldId;
    }


    public Map<String, String> getSortMap()
    {
        return sortMap;
    }


    public void setSortMap(Map<String, String> sortMap)
    {
        this.sortMap = sortMap;
    }


    public List<? extends Object> getStores()
    {
        return stores;
    }


    public List<? extends Object> getAreas()
    {
        return areas;
    }


    public List<? extends Object> getAvailableBuyerList()
    {
        return availableBuyerList;
    }


    public void setAvailableBuyerList(List<? extends Object> availableBuyerList)
    {
        this.availableBuyerList = availableBuyerList;
    }


    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }


    public void setEmailAddrs(String emailAddrs)
    {
        this.emailAddrs = emailAddrs;
    }
    
    
    public InputStream getEmailRlt() throws UnsupportedEncodingException
    {
        if (null == emailRlt || emailRlt.trim().isEmpty())
            return null;
        
        return new ByteArrayInputStream(emailRlt.getBytes(CommonConstants.ENCODING_UTF8));
    }
    
    
    public List<? extends Object> getClasses()
    {
        return classes;
    }


    public List<? extends Object> getSubclasses()
    {
        return subclasses;
    }


    public void setSubclasses(List<? extends Object> subclasses)
    {
        this.subclasses = subclasses;
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    public String getPrintDocMsg()
    {
        return printDocMsg;
    }


    public void setPrintDocMsg(String printDocMsg)
    {
        this.printDocMsg = printDocMsg;
    }


    public String getPrintType()
    {
        return printType;
    }


    public void setPrintType(String printType)
    {
        this.printType = printType;
    }


    public boolean isSelectAll()
    {
        return selectAll;
    }


    public void setSelectAll(boolean selectAll)
    {
        this.selectAll = selectAll;
    }
    
}
