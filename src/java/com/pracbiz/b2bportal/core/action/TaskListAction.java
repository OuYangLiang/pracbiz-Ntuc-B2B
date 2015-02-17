package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;
import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TaskListHolder;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.GroupTmpExHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingExHolder;
import com.pracbiz.b2bportal.core.holder.extension.RoleTmpExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.RoleService;
import com.pracbiz.b2bportal.core.service.UserClassService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.service.UserSubclassService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */
public class TaskListAction extends TransactionalDocsBaseAction implements
    CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(TaskListAction.class);
    private static final long serialVersionUID = -6146964096051467016L;
    
    private static final String BUYER_ITEM_CODES = "BUYER_ITEM_CODES";
    private static final String BUYER_ALL_GRANTED = "BUYER_ALL_GRANTED";
    private static final String SESSION_SUPPLIER_DISPUTE = "SUPPLIER_DISPUTE";
    
    private final String ALL_SUPPLIERS = this.getText("role.all.supplier.selected");
    
    @Autowired private transient UserProfileTmpService userProfileTmpService;
    @Autowired private transient RoleService roleService;
    @Autowired private transient GroupService groupService;
    @Autowired private transient InvHeaderService invHeaderService;
    @Autowired private transient PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired private transient UserClassService userClassService;
    @Autowired private transient UserSubclassService userSubclassService;
    @Autowired private transient DnHeaderService dnHeaderService;
    @Autowired private transient ItemService itemService;
    @Autowired private transient BusinessRuleService businessRuleService;
    
    private int roleCount;
    private int userCount;
    private int groupCount;
    private int invCount;
    private TaskListHolder holder;
    
    
    private List<String> urls;
    
    public String init()
    {
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String getPendingUserCount()
    {
        try
        {
            urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            if (urls.contains("/user/saveApprove.action") || urls.contains("/user/saveReject.action"))
            {
                UserProfileTmpExHolder param = new UserProfileTmpExHolder();
                param.setCtrlStatus(MkCtrlStatus.PENDING);
                param.setCurrentUserType(this.getUserTypeOfCurrentUser());
                param.trimAllString();
                param.setAllEmptyStringToNull();
                userCount = userProfileTmpService.getCountOfSummary(param);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String getPendingRoleCount()
    {
        try
        {
            urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            if (urls.contains("/role/saveApprove.action") || urls.contains("/role/saveReject.action"))
            {
                RoleTmpExHolder param = new RoleTmpExHolder();
                param.setAllSupplierKey(ALL_SUPPLIERS);
                param.setCtrlStatus(MkCtrlStatus.PENDING);
                param.setCurrentUserTypeOid(this.getUserTypeOfCurrentUser());
                param.trimAllString();
                param.setAllEmptyStringToNull();
                roleCount = roleService.getCountOfSummary(param);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String getPendingGroupCount()
    {
        try
        {
            urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            if (urls.contains("/group/saveApprove.action") || urls.contains("/group/saveReject.action"))
            {
                GroupTmpExHolder param = new GroupTmpExHolder();
                param.setCtrlStatus(MkCtrlStatus.PENDING);
                param.setCurrentUserTypeOid(this.getUserTypeOfCurrentUser());
                param.trimAllString();
                param.setAllEmptyStringToNull();
                groupCount = groupService.getCountOfSummary(param);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        
        return SUCCESS;
    }

    
    @SuppressWarnings("unchecked")
    public String getUnreadInvoiceCount()
    {
        try
        {
            urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            if (urls.contains("/inv/print.action"))
            {
                InvSummaryHolder param = new InvSummaryHolder();
                param.setReadStatus(ReadStatus.UNREAD);
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                
                param = initSortField(param);

                initCurrentUserSearchParam(param);
                
                param.trimAllString();
                param.setAllEmptyStringToNull();
                
                invCount = invHeaderService.getCountOfSummary(param);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String getPendingPIGDCount()
    {
        try
        {
            int matchingCloseCount = 0;
            int matchingAuditPriceCount = 0;
            int matchingAuditQtyCount = 0;
            int matchingApproveInvCount = 0;
            int matchingSuppCount = 0;
            
            boolean isBuyer = this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(2)) == 0 
                || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(4)) == 0;
            boolean isStore = this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0;
            boolean isSupplier = this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(3)) == 0 
                || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(5)) == 0;
            
            this.setEnableSuppleirDispute();
            
            //init class and subclass info
            if (isBuyer && null == this.getSession().get(BUYER_ALL_GRANTED) && null == this.getSession().get(BUYER_ITEM_CODES))
            {
                BigDecimal userOid = this.getProfileOfCurrentUser().getUserOid();
                List<UserClassHolder> classes = userClassService.selectByUserOid(userOid);
                List<UserSubclassHolder> subClasses = userSubclassService.selectByUserOid(userOid);
                if (classes.isEmpty() && subClasses.isEmpty())
                {
                    List<String> buyerItemCodes = new ArrayList<String>();
                    this.getSession().put(BUYER_ITEM_CODES, buyerItemCodes);
                }
                else if ((!classes.isEmpty() && new BigDecimal(-1).equals(classes.get(0).getClassOid())) || (!subClasses.isEmpty() && new BigDecimal(-1).equals(subClasses.get(0).getSubclassOid())))
                {
                    this.getSession().put(BUYER_ALL_GRANTED, "true");
                }
                else
                {
                    List<String> buyerItemCodes = itemService.selectActiveItemsByUserOid(userOid);
                    this.getSession().put(BUYER_ITEM_CODES, buyerItemCodes);
                }
            }
            
            
            urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            
            if (isBuyer)
            {
                if (urls.contains("/poInvGrnDnMatching/initAuditPrice.action"))
                {
                    PoInvGrnDnMatchingExHolder param = new PoInvGrnDnMatchingExHolder();
                    param.setStatusPriceUnmatched(true);
                    param.setStatusUnmatched(true);
                    param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
                    param.setPriceStatus(PoInvGrnDnMatchingPriceStatus.PENDING);
                    param.setPriceApprove(true);
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    matchingAuditPriceCount = poInvGrnDnMatchingService.getCountOfSummary(param);
//                    pigds = poInvGrnDnMatchingService.getListOfSummary(param);
                    
//                    List<BigDecimal> lst = new ArrayList<BigDecimal>();
//                    for (BigDecimal oid : auditPriceOids)
//                    {
//                        PoInvGrnDnMatchingHolder holder = poInvGrnDnMatchingService.selectByKey(oid);
//                        List<PoInvGrnDnMatchingDetailExHolder> details = poInvGrnDnMatchingDetailService.selectByMatchingOid(oid);
//                        if (details != null && !details.isEmpty())
//                        {
//                            boolean flag = false;
//                            for (PoInvGrnDnMatchingDetailExHolder detail : details)
//                            {
//                                if ("true".equals(this.getSession().get(BUYER_ALL_GRANTED)))
//                                {
//                                    flag = true;
//                                    break;
//                                }
//                                else if (((List<String>)this.getSession().get(BUYER_ITEM_CODES)).contains(detail.getBuyerItemCode() + "-" + holder.getBuyerOid()))
//                                {
//                                    flag = true;
//                                    break;
//                                }
//                            }
//                            if (!flag)
//                            {
//                                lst.add(oid);
//                                matchingAuditPriceCount--;
//                            }
//                        }
//                    }
//                    
//                    auditPriceOids.removeAll(lst);
                }
                if (urls.contains("/poInvGrnDnMatching/initAuditQty.action"))
                {
                    PoInvGrnDnMatchingExHolder param = new PoInvGrnDnMatchingExHolder();
                    param.setStatusQtyUnmatched(true);
                    param.setStatusUnmatched(true);
                    param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
                    param.setQtyStatus(PoInvGrnDnMatchingQtyStatus.PENDING);
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    matchingAuditQtyCount = poInvGrnDnMatchingService.getCountOfSummary(param);
                    
                }
                if (urls.contains("/poInvGrnDnMatching/saveClose.action"))
                {
                    PoInvGrnDnMatchingExHolder param = new PoInvGrnDnMatchingExHolder();
                    param.setPendingForClosing(true);
                    param.setStatusUnmatched(true);
                    param.setStatusPriceUnmatched(true);
                    param.setStatusQtyUnmatched(true);
                    param.setStatusAmountUnmatched(true);
                    if (null != (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE) 
                        && "yes".equalsIgnoreCase((String)this.getSession().get(SESSION_SUPPLIER_DISPUTE)))
                        param.setEnableSupplierDispute(true);
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    matchingCloseCount = poInvGrnDnMatchingService.getCountOfSummary(param);
                }
                if (urls.contains("/poInvGrnDnMatching/approve.action"))
                {
                    PoInvGrnDnMatchingExHolder param = new PoInvGrnDnMatchingExHolder();
                    param.setStatusUnmatched(true);
                    param.setStatusPriceUnmatched(true);
                    param.setStatusQtyUnmatched(true);
                    param.setStatusAmountUnmatched(true);
                    param.setPendingForApproving(true);
                    if (null != (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE) 
                        && "yes".equalsIgnoreCase((String)this.getSession().get(SESSION_SUPPLIER_DISPUTE)))
                        param.setEnableSupplierDispute(true);
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    matchingApproveInvCount = poInvGrnDnMatchingService.getCountOfSummary(param);
                }
            }
            
            if (isSupplier)
            {
                if (urls.contains("/poInvGrnDnMatching/saveAcceptOrReject.action"))
                {
                    PoInvGrnDnMatchingExHolder param = new PoInvGrnDnMatchingExHolder();
                    param.setStatusUnmatched(true);
                    param.setStatusPriceUnmatched(true);
                    param.setStatusQtyUnmatched(true);
                    param.setStatusAmountUnmatched(true);
                    param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.PENDING);
                    param.setRevised(false);
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    matchingSuppCount = poInvGrnDnMatchingService.getCountOfSummary(param);
                }
            }
            
            if (isStore)
            {
                if (urls.contains("/poInvGrnDnMatching/initAuditQty.action"))
                {
                    PoInvGrnDnMatchingExHolder param = new PoInvGrnDnMatchingExHolder();
                    param.setStatusQtyUnmatched(true);
                    param.setStatusUnmatched(true);
                    param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
                    param.setQtyStatus(PoInvGrnDnMatchingQtyStatus.PENDING);
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                    
                    matchingAuditQtyCount = poInvGrnDnMatchingService.getCountOfSummary(param);
                }
            }
            
            holder = new TaskListHolder();
            holder.setMatchingApproveInvCount(matchingApproveInvCount);
            holder.setMatchingAuditPriceCount(matchingAuditPriceCount);
            holder.setMatchingAuditQtyCount(matchingAuditQtyCount);
            holder.setMatchingCloseCount(matchingCloseCount);
            holder.setMatchingSuppCount(matchingSuppCount);
            
//            if (!closeOids.isEmpty())
//            {
//                this.getSession().put(TASKLIST_MATCHING_CLOSE_OIDS, closeOids);
//            }
//            if (!auditPriceOids.isEmpty())
//            {
//                this.getSession().put(TASKLIST_MATCHING_AUDIT_PRICE_OIDS, auditPriceOids);
//            }
//            if (!auditQtyOids.isEmpty())
//            {
//                this.getSession().put(TASKLIST_MATCHING_AUDIT_QTY_OIDS, auditQtyOids);
//            }
//            if (!appInvOids.isEmpty())
//            {
//                this.getSession().put(TASKLIST_MATCHING_APPROVE_INV_OIDS, appInvOids);
//            }
//            if (!suppOids.isEmpty())
//            {
//                this.getSession().put(TASKLIST_MATCHING_SUPP_OIDS, suppOids);
//            }
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String getPendingDnCount()
    {
        try
        {
            int suppCount = 0;
            int priceCount = 0;
            int qtyCount = 0;
            int closeCount = 0;
            
            boolean isBuyer = this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(2)) == 0 
                || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(4)) == 0;
            boolean isStore = this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0;
            boolean isSupplier = this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(3)) == 0 
                || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(5)) == 0;
            
            
            urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            if (isBuyer)
            {
                if (urls.contains("/dn/saveAuditPrice.action"))
                {
                    DnSummaryHolder param = new DnSummaryHolder();
                    param.setClosed(false);
                    param.setDispute(true);
                    param.setPriceDisputed(true);
                    param.setPriceStatus(DnPriceStatus.PENDING);
                    param.setPriceApprove(true);
                    
                    initCurrentUserSearchParam(param);
                    
                    priceCount = dnHeaderService.getCountOfSummary(param);
                    
                }
                if (urls.contains("/dn/saveAuditQty.action"))
                {
                    DnSummaryHolder param = new DnSummaryHolder();
                    param.setDispute(true);
                    param.setClosed(false);
                    param.setQtyDisputed(true);
                    param.setQtyStatus(DnQtyStatus.PENDING);
                    
                    initCurrentUserSearchParam(param);
                    
                    qtyCount = dnHeaderService.getCountOfSummary(param);
                }
                if (urls.contains("/dn/saveClose.action"))
                {
                    DnSummaryHolder param = new DnSummaryHolder();
                    param.setDispute(true);
                    param.setClosed(false);
                    param.setPendingForClosing(true);
                    
                    initCurrentUserSearchParam(param);
                    
                    closeCount = dnHeaderService.getCountOfSummary(param);
                }
            }
            if (isSupplier)
            {
                if (urls.contains("/dn/saveDispute.action"))
                {
                    DnSummaryHolder param = new DnSummaryHolder();
                    param.setClosed(false);
                    param.setDispute(false);
                    param.setDnType(DnType.STK_RTV.name());
                    
                    initCurrentUserSearchParam(param);
                    
                    suppCount = dnHeaderService.getCountOfSummary(param);
                }
            }
            if (isStore)
            {
                if (urls.contains("/dn/saveAuditQty.action"))
                {
                    DnSummaryHolder param = new DnSummaryHolder();
                    param.setDispute(true);
                    param.setClosed(false);
                    param.setQtyDisputed(true);
                    
                    initCurrentUserSearchParam(param);
                    
                    qtyCount = dnHeaderService.getCountOfSummary(param);
                }
            }
            holder = new TaskListHolder();
            holder.setDnSuppCount(suppCount);
            holder.setDnPriceCount(priceCount);
            holder.setDnQtyCount(qtyCount);
            holder.setDnCloseCount(closeCount);
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    // ***************************************
    // private methods
    // ***************************************
    private void initCurrentUserSearchParam(PoInvGrnDnMatchingExHolder searchParam)throws Exception
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
        //current is buyer user.
        if (searchParam.getCurrentUserType().compareTo(BigDecimal.valueOf(2)) == 0 || searchParam.getCurrentUserType().compareTo(BigDecimal.valueOf(4)) == 0)
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
        if ((searchParam.getCurrentUserType().compareTo(BigDecimal.valueOf(6)) == 0 
                || searchParam.getCurrentUserType().compareTo(BigDecimal.valueOf(7)) == 0)
                && searchParam.getBuyerStoreAccessList().isEmpty())
        {
            searchParam.setVisiable(false);
        }
        //current is supplier user.
        if (searchParam.getCurrentUserType().compareTo(BigDecimal.valueOf(3)) == 0 || searchParam.getCurrentUserType().compareTo(BigDecimal.valueOf(5)) == 0)
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
        
        if (searchParam.isPendingForClosing())
        {
            searchParam.setSupplierStatus(null);
            searchParam.setBuyerStatus(null);
            searchParam.setRevised(null);
        }
        
        if (null != (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE) 
            && "yes".equalsIgnoreCase((String)this.getSession().get(SESSION_SUPPLIER_DISPUTE)))
            searchParam.setEnableSupplierDispute(true);
    }
    
    
    private InvSummaryHolder initSortField(InvSummaryHolder param)
    {
        param.setSortField(SORT_FIELD_SENT_DATE);
        param.setSortOrder(SORT_ORDER_DESC);
        
        if(null != getProfileOfCurrentUser().getSupplierOid()) 
        {
            param.setSortField(SORT_FIELD_CREATE_DATE);
            param.setSortOrder(SORT_ORDER_DESC);
        }
        
        return param;
    }
    
    
    private void setEnableSuppleirDispute() throws Exception
    {
        if (null == (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE))
        {
            if (this.isCurrentUserBuyerSide())
            {
                boolean enableSupplierToDispute = businessRuleService.isMatchingEnableSupplierToDispute(this.getProfileOfCurrentUser().getBuyerOid());
                if (enableSupplierToDispute)
                {
                    this.getSession().put(SESSION_SUPPLIER_DISPUTE, "yes");
                }
            }
            else if (this.isCurrentUserSupplierSide())
            {
                this.getSession().put(SESSION_SUPPLIER_DISPUTE, "yes");
            }
        }
    }
    
    
    // ***************************************
    // Getter and Setter methods
    // ***************************************
    
    public int getRoleCount()
    {
        return roleCount;
    }
    
    
    public void setRoleCount(int roleCount)
    {
        this.roleCount = roleCount;
    }
    
    
    public int getUserCount()
    {
        return userCount;
    }
    
    
    public void setUserCount(int userCount)
    {
        this.userCount = userCount;
    }
    
    
    public int getGroupCount()
    {
        return groupCount;
    }
    
    
    public void setGroupCount(int groupCount)
    {
        this.groupCount = groupCount;
    }


    public int getInvCount()
    {
        return invCount;
    }


    public void setInvCount(int invCount)
    {
        this.invCount = invCount;
    }


    public TaskListHolder getHolder()
    {
        return holder;
    }


    public void setHolder(TaskListHolder holder)
    {
        this.holder = holder;
    }

}
