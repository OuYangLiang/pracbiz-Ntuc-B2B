package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingBuyerStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingInvStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.DocSubclassHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.FavouriteListExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingExHolder;
import com.pracbiz.b2bportal.core.mapper.DocSubclassMapper;
import com.pracbiz.b2bportal.core.report.DefaultPoReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.FavouriteListService;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PoDetailService;
import com.pracbiz.b2bportal.core.service.PoHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingDetailService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserClassService;
import com.pracbiz.b2bportal.core.service.UserSubclassService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

public class PoInvGrnDnMatchingAction extends ProjectBaseAction implements ApplicationContextAware
{
    private static final Logger log = LoggerFactory.getLogger(PoInvGrnDnMatchingAction.class);
    private static final long serialVersionUID = 8827920059116676395L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING = "SEARCH_PARAMETER_PIGD_MATCHING";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.PoInvGrnDnMatchingAction.selectedOids";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final Map<String, String> sortMap;
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    private static final String SESSION_SUPPLIER_DISPUTE = "SUPPLIER_DISPUTE";
    public static final String BUYER_ITEM_CODES = "BUYER_ITEM_CODES";
    public static final String BUYER_ALL_GRANTED = "BUYER_ALL_GRANTED";
    private static final String AUDIT_TYPE = "auditType";
    private static final String IS_ALL_AUDITED = "isAllAudited";
    private static final String IS_REVISED = "isRevised";
    private static final String IF_ACCEPT_REJECT = "ifAcceptReject";
    private static final String CAN_CLOSE_RECORD = "canCloseRecord";
    private static final String SYSTEM = "SYSTEM";

    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("buyerCode", "BUYER_CODE");
        sortMap.put("buyerName", "BUYER_NAME");
        sortMap.put("supplierCode", "SUPPLIER_CODE");
        sortMap.put("supplierName", "SUPPLIER_NAME");
        sortMap.put("poStoreCode", "PO_STORE_CODE");
        sortMap.put("poNo", "PO_NO");
        sortMap.put("poAmt", "PO_AMT");
        sortMap.put("invNo", "INV_NO");
        sortMap.put("invAmt", "INV_AMT");
        sortMap.put("grnNo", "GRN_NO");
        sortMap.put("grnAmt", "GRN_AMT");
        sortMap.put("dnNo", "DN_NO");
        sortMap.put("dnAmt", "DN_AMT");
        sortMap.put("matchingStatus", "MATCHING_STATUS");
        sortMap.put("matchingDate", "MATCHING_DATE");
        sortMap.put("buyerStatus", "BUYER_STATUS");
        sortMap.put("priceStatus", "PRICE_STATUS");
        sortMap.put("qtyStatus", "QTY_STATUS");
        sortMap.put("supplierStatus", "SUPPLIER_STATUS");
        sortMap.put("closed", "CLOSED");
        sortMap.put("invStatus", "INV_STATUS");
    }
    
    private PoInvGrnDnMatchingExHolder param;
    private Map<String, String> matchingStatus;
    private Map<String, String> invStatus;
    private Map<String, String> supplierStatus;
    private Map<String, String> buyerStatus;
    private Map<String, String> priceStatus;
    private Map<String, String> qtyStatus;
    private Map<Boolean, String> closedStatus;
    private Map<Boolean, String> revisedStatus;
    private List<PoInvGrnDnMatchingDetailExHolder> details;
    protected List<FavouriteListExHolder> favouriteLists;
    private List<String> buyerItemCodes;
    
    @Autowired
    private transient PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired
    private transient BuyerService buyerService;
    @Autowired
    private transient SupplierService supplierService;
    @Autowired
    private transient GroupUserService groupUserService;
    @Autowired
    private transient GroupSupplierService groupSupplierService;
    @Autowired
    private transient GroupTradingPartnerService groupTradingPartnerService;
    @Autowired
    private transient MsgTransactionsService msgTransactionsService;
    @Autowired
    private transient ControlParameterService controlParameterService;
//    @Autowired 
//    transient private BuyerStoreService buyerStoreService;
//    @Autowired 
//    transient private BuyerStoreAreaUserService buyerStoreAreaUserService;
//    @Autowired 
//    transient private BuyerStoreUserService buyerStoreUserService;
//    @Autowired 
//    transient private BuyerStoreAreaService buyerStoreAreaService;
//    @Autowired
//    transient private GroupService groupService;
    @Autowired 
    transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired 
    transient private PoHeaderService poHeaderService;
    @Autowired 
    transient private PoLocationService poLocationService;
    @Autowired 
    transient private PoDetailService poDetailService;
    @Autowired 
    transient private PoHeaderExtendedService poHeaderExtendedService;
    @Autowired 
    transient private PoDetailExtendedService poDetailExtendedService;
    @Autowired 
    transient private PoLocationDetailService poLocationDetailService;
    @Autowired 
    transient private PoLocationDetailExtendedService poLocationDetailExtendedService;
    @Autowired
    private transient FavouriteListService favouriteListService;
    @Autowired
    private transient BusinessRuleService businessRuleService;
    @Autowired
    private transient InvHeaderService invHeaderService;
    @Autowired
    private transient PoInvGrnDnMatchingDetailService poInvGrnDnMatchingDetailService;
    @Autowired
    private transient ItemService itemService;
    @Autowired
    private transient UserClassService userClassService;
    @Autowired
    private transient UserSubclassService userSubclassService;
    @Autowired
    private transient BuyerStoreUserService buyerStoreUserService;
    @Autowired
    transient private DocSubclassMapper docSubclassMapper;

    
    transient private ApplicationContext ctx;
    private String result;
    private transient InputStream rptResult;
    private String rptFileName;
    private boolean selectAll;
    
    
    public PoInvGrnDnMatchingAction()
    {
        this.initMsg();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx)
        throws BeansException
    {
        this.ctx = ctx;
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
    
    private void initDefaultSearchCondition(PoInvGrnDnMatchingExHolder param) throws Exception
    {
        ControlParameterHolder documentWindow = controlParameterService
            .selectCacheControlParameterBySectIdAndParamId(CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
        
        if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
        {
            param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
        }
        else
        {
            param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
        }
        
        param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        
        param.setStatusPending(false);
        param.setStatusMatched(false);
        param.setStatusMatchedByDn(false);
        param.setStatusUnmatched(false);
        param.setStatusPriceUnmatched(false);
        param.setStatusQtyUnmatched(false);
        param.setStatusAmountUnmatched(false);
        param.setStatusOutdated(false);
        param.setStatusInsInv(false);
        param.setPendingForClosing(false);
        
        if (null != (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE) 
            && "yes".equalsIgnoreCase((String)this.getSession().get(SESSION_SUPPLIER_DISPUTE)))
            param.setEnableSupplierDispute(true);

        if (this.isCurrentUserBuyerSide())
        {
            @SuppressWarnings("unchecked")
            List<String> urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            
            if (param.isEnableSupplierDispute())
            {
                boolean auditPrice = urls.contains("/poInvGrnDnMatching/initAuditPrice.action");
                boolean auditQty = urls.contains("/poInvGrnDnMatching/initAuditQty.action");
                boolean doClose = urls.contains("/poInvGrnDnMatching/saveClose.action");
                
                if (auditPrice && !auditQty && !doClose)
                {
                    param.setStatusUnmatched(true);
                    param.setStatusPriceUnmatched(true);
                    param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
                    param.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.PENDING);
                }
                else if (!auditPrice && auditQty && !doClose)
                {
                    param.setStatusUnmatched(true);
                    param.setStatusQtyUnmatched(true);
                    param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
                    param.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.PENDING);
                }
                else if (!auditPrice && !auditQty && doClose)
                {
                    param.setClosed(false);
                    param.setStatusUnmatched(true);
                    param.setStatusPriceUnmatched(true);
                    param.setStatusQtyUnmatched(true);
                    param.setPendingForClosing(true);
                }
                
                else if (urls.contains("/poInvGrnDnMatching/saveAudit.action"))
                {
                    param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
                }
            }
            else
            {
                boolean doClose = urls.contains("/poInvGrnDnMatching/saveClose.action");
                
                if (doClose)
                {
                    param.setClosed(false);
                }
            }
            
        }
        
        param.setSortField("MATCHING_OID");
        param.setSortOrder("DESC");
    }
    
    
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING);
        
        try
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
            
            if (param == null)
            {
                param = (PoInvGrnDnMatchingExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING);
                
                if (null == param)
                {
                    param = new PoInvGrnDnMatchingExHolder();
                    initDefaultSearchCondition(param);
                }
            }
            
            matchingStatus = PoInvGrnDnMatchingStatus.toMapValue(this);
            invStatus = PoInvGrnDnMatchingInvStatus.toMapValue(this);
            supplierStatus = PoInvGrnDnMatchingSupplierStatus.toMapValue(this);
            buyerStatus = PoInvGrnDnMatchingBuyerStatus.toMapValue(this);
            priceStatus = PoInvGrnDnMatchingPriceStatus.toMapValue(this);
            qtyStatus = PoInvGrnDnMatchingQtyStatus.toMapValue(this);
            closedStatus = new HashMap<Boolean, String>();
            closedStatus.put(true, "YES");
            closedStatus.put(false, "NO");
            revisedStatus = new HashMap<Boolean, String>();
            revisedStatus.put(true, "YES");
            revisedStatus.put(false, "NO");
            
            if (this.isCurrentUserSupplierSide())
                param.setSupplierList(supplierService.selectGrantSuppliersBySupplierSideUser(getProfileOfCurrentUser()));
            
            param.setBuyerList(buyerService.selectAvailableBuyersByUserOid(getProfileOfCurrentUser()));
            
            favouriteLists = favouriteListService.selectFavouriteListByUserOid(getProfileOfCurrentUser().getUserOid());
        }
        catch (Exception e)
        {
            this.handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        
        this.getSession().put(SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING, param);
        return SUCCESS;
    }
    

    public String data()
    {
        try
        {
            param = (PoInvGrnDnMatchingExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING);
            if (param == null)
            {
                param = new PoInvGrnDnMatchingExHolder();
                
                initDefaultSearchCondition(param);
                
            }
            param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
            initCurrentUserSearchParam(param);
            
            if (null != (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE) 
                && "yes".equalsIgnoreCase((String)this.getSession().get(SESSION_SUPPLIER_DISPUTE)))
                param.setEnableSupplierDispute(true);
            
            this.obtainListRecordsOfPagination(poInvGrnDnMatchingService, param,
                    sortMap, "matchingOid", MODULE_KEY_MATCH);
            for (BaseHolder baseItem : this.getGridRlt().getItems())
            {
                PoInvGrnDnMatchingExHolder item = (PoInvGrnDnMatchingExHolder) baseItem;
                if (item.getMatchingStatus() != null)
                {
                    item.setMatchingStatusValue(this.getText(item.getMatchingStatus().getKey()));
                }
                if (item.getInvStatus() != null)
                {
                    item.setInvStatusValue(this.getText(item.getInvStatus().getKey()));
                }
                if (item.getSupplierStatus() != null)
                {
                    item.setSupplierStatusValue(this.getText(item.getSupplierStatus().getKey()));
                }
                if (item.getBuyerStatus() != null)
                {
                    item.setBuyerStatusValue(this.getText(item.getBuyerStatus().getKey()));
                }
                if (item.getPriceStatus() != null)
                {
                    item.setPriceStatusValue(this.getText(item.getPriceStatus().getKey()));
                }
                if (item.getQtyStatus() != null)
                {
                    item.setQtyStatusValue(this.getText(item.getQtyStatus().getKey()));
                }
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }
    
    
    public String search()
    {
        if (null == param)
        {
            param = new PoInvGrnDnMatchingExHolder();
        }
        
        param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getPoDateFrom()));
        param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getPoDateTo()));
        param.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getInvDateFrom()));
        param.setInvDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getInvDateTo()));
        param.setDnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getDnDateFrom()));
        param.setDnDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getDnDateTo()));
        param.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getGrnDateFrom()));
        param.setGrnDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getGrnDateTo()));
        param.setMatchingDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getMatchingDateFrom()));
        param.setMatchingDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getMatchingDateTo()));
        if ("".equals(this.getRequest().getParameter("param.closed")))
        {
            param.setClosed(null);
        }
        if ("".equals(this.getRequest().getParameter("param.revised")))
        {
            param.setRevised(null);
        }
        
        param.setSortField("MATCHING_OID");
        param.setSortOrder("DESC");
        try
        {
            param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
            initCurrentUserSearchParam(param);
            
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING, param);

        return SUCCESS;
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
    
    
    // *****************************************************
    // approve function of unity
    // *****************************************************
    public String approve() throws Exception
    {
        try
        {
            PoInvGrnDnMatchingHolder holder = poInvGrnDnMatchingService.selectByKey(param.getMatchingOid());
            
            if ("yes".equals(this.getSession().get(SESSION_SUPPLIER_DISPUTE)))
            {
                if (PoInvGrnDnMatchingStatus.PENDING.equals(holder.getMatchingStatus()))
                {
                    this.result = "pending";
                    return SUCCESS;
                }
                if (PoInvGrnDnMatchingStatus.MATCHED.equals(holder.getMatchingStatus()))
                {
                    this.result = "matched";
                    return SUCCESS;
                }
                if (PoInvGrnDnMatchingStatus.INSUFFICIENT_INV.equals(holder.getMatchingStatus()))
                {
                    this.result = "insufficientInv";
                    return SUCCESS;
                }
                if (PoInvGrnDnMatchingStatus.OUTDATED.equals(holder.getMatchingStatus()))
                {
                    this.result = "outdated";
                    return SUCCESS;
                }
                if (PoInvGrnDnMatchingInvStatus.APPROVED.equals(holder.getInvStatus()) || PoInvGrnDnMatchingInvStatus.SYS_APPROVED.equals(holder.getInvStatus()))
                {
                    this.result = "approved";
                    return SUCCESS;
                }
                if (!holder.getRevised())
                {
                    if (PoInvGrnDnMatchingSupplierStatus.PENDING.equals(holder.getSupplierStatus()))
                    {
                        this.result = "supplierpending";
                        return SUCCESS;
                    }
                    if (PoInvGrnDnMatchingBuyerStatus.PENDING.equals(holder.getBuyerStatus()) && PoInvGrnDnMatchingSupplierStatus.ACCEPTED.equals(holder.getSupplierStatus()))
                    {
                        this.result = "supplieraccept";
                        return SUCCESS;
                    }
                    if (PoInvGrnDnMatchingBuyerStatus.PENDING.equals(holder.getBuyerStatus()) && PoInvGrnDnMatchingSupplierStatus.REJECTED.equals(holder.getSupplierStatus()))
                    {
                        this.result = "buyerpending";
                        return SUCCESS;
                    }
                    if (PoInvGrnDnMatchingBuyerStatus.REJECTED.equals(holder.getBuyerStatus()))
                    {
                        this.result = "rejected";
                        return SUCCESS;
                    }
                }
            }
            if (null == param.getInvStatusActionRemarks() || param.getInvStatusActionRemarks().length() == 0 || param.getInvStatusActionRemarks().length() > 255)
            {
                this.result = "remarks";
                return SUCCESS;
            }
            this.checkInvFileExist(holder);
            poInvGrnDnMatchingService.changeInvDateToFirstGrnDate(holder);
            PoInvGrnDnMatchingHolder newHolder = new PoInvGrnDnMatchingHolder();
            BeanUtils.copyProperties(holder, newHolder);
            newHolder.setInvStatus(PoInvGrnDnMatchingInvStatus.APPROVED);
            newHolder.setInvStatusActionDate(new Date());
            newHolder.setInvStatusActionRemarks(param.getInvStatusActionRemarks());
            newHolder.setInvStatusActionBy(getProfileOfCurrentUser().getLoginId());
            poInvGrnDnMatchingService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), holder, newHolder);
            poInvGrnDnMatchingService.moveFile(holder);
            this.result = "1";
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            this.result = "0";
        }
        return SUCCESS;
    }
    
    
    public String checkAcceptOrReject()
    {
        try
        {
            PoInvGrnDnMatchingHolder holder = poInvGrnDnMatchingService.selectByKey(param.getMatchingOid());
            
            if (holder.getClosed())
            {
                this.result = "closed";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingStatus.PENDING.equals(holder.getMatchingStatus()))
            {
                this.result = "pending";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingStatus.MATCHED.equals(holder.getMatchingStatus()))
            {
                this.result = "matched";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingStatus.INSUFFICIENT_INV.equals(holder.getMatchingStatus()))
            {
                this.result = "insufficientInv";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingStatus.OUTDATED.equals(holder.getMatchingStatus()))
            {
                this.result = "outdated";
                return SUCCESS;
            }
            if (holder.getRevised())
            {
                this.result = "revised";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingSupplierStatus.ACCEPTED.equals(holder.getSupplierStatus()))
            {
                this.result = "accepted";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingSupplierStatus.REJECTED.equals(holder.getSupplierStatus()))
            {
                this.result = "rejected";
                return SUCCESS;
            }
            this.result = "1";
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            this.result = "0";
        }
        
        return SUCCESS;
    }
    

    public String saveAcceptOrReject()
    {
        try
        {
            PoInvGrnDnMatchingHolder holder = poInvGrnDnMatchingService.selectByKey(param.getMatchingOid());
            
            if (holder.getClosed())
            {
                this.result = "closed";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingStatus.PENDING.equals(holder.getMatchingStatus()))
            {
                this.result = "pending";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingStatus.MATCHED.equals(holder.getMatchingStatus()))
            {
                this.result = "matched";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingStatus.INSUFFICIENT_INV.equals(holder.getMatchingStatus()))
            {
                this.result = "insufficientInv";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingStatus.OUTDATED.equals(holder.getMatchingStatus()))
            {
                this.result = "outdated";
                return SUCCESS;
            }
            if (holder.getRevised())
            {
                this.result = "revised";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingSupplierStatus.ACCEPTED.equals(holder.getSupplierStatus()))
            {
                this.result = "accepted";
                return SUCCESS;
            }
            if (PoInvGrnDnMatchingSupplierStatus.REJECTED.equals(holder.getSupplierStatus()))
            {
                this.result = "rejected";
                return SUCCESS;
            }
            if (null == param.getSupplierStatusActionRemarks() || param.getSupplierStatusActionRemarks().length() == 0 || param.getSupplierStatusActionRemarks().length() > 255)
            {
                this.result = "remarks";
                return SUCCESS;
            }
            
            PoInvGrnDnMatchingHolder newHolder = new PoInvGrnDnMatchingHolder();
            BeanUtils.copyProperties(holder, newHolder);
            
            if ("accept".equalsIgnoreCase(param.getSupplierStatusValue()))
            {
                //remove the inv file from buyer's tmp folder.
                BuyerHolder buyer = buyerService.selectBuyerByKey(holder.getBuyerOid());
                MsgTransactionsHolder msg = msgTransactionsService.selectByKey(holder.getInvOid());
                File source = new File(mboxUtil.getBuyerMailBox(buyer.getMboxId()) + PS + "tmp" + PS + msg.getExchangeFilename());
                if (source.exists() && source.isFile())
                {
                    try
                    {
                    	FileUtil.getInstance().deleleAllFile(source);
                    }
                    catch (IOException e)
                    {
                    	throw new Exception(e);
                    }
                }
                
                //recover po status to new, amended or partial invoiced
                List<PoHeaderHolder> pos = poHeaderService.selectPoHeadersByPoNo(holder.getBuyerOid(), holder.getPoNo(), holder.getSupplierCode());
                PoHeaderHolder oldObj = poHeaderService.selectPoHeaderByKey(holder.getPoOid());
                PoHeaderHolder newObj = new PoHeaderHolder();
                BeanUtils.copyProperties(oldObj, newObj);
                
                InvHeaderHolder inv = new InvHeaderHolder();
                inv.setPoOid(holder.getPoOid());
                List<InvHeaderHolder> invs = invHeaderService.select(inv);
                if (invs.size() > 1)
                {
                    newObj.setPoStatus(PoStatus.PARTIAL_INVOICED);
                }
                else if (invs.size() == 1)
                {
                    if (pos.size() == 1)
                    {
                        newObj.setPoStatus(PoStatus.NEW);
                    }
                    if (pos.size() > 1)
                    {
                        newObj.setPoStatus(PoStatus.AMENDED);
                    }
                }
                poHeaderService.auditUpdateByPrimaryKey(this.getCommonParameter(), oldObj, newObj);
                
                //change invoice status to void
                InvHeaderHolder oldInv = invHeaderService.selectInvHeaderByKey(holder.getInvOid());
                InvHeaderHolder newInv = new InvHeaderHolder();
                BeanUtils.copyProperties(oldInv, newInv);
                newInv.setInvStatus(InvStatus.VOID);
                invHeaderService.auditUpdateByPrimaryKey(this.getCommonParameter(), oldInv, newInv);
                
                newHolder.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.ACCEPTED);
            }
            else if ("reject".equalsIgnoreCase(param.getSupplierStatusValue()))
            {
                // auto accept same price or qty items.
                boolean priceInvLessPo = businessRuleService.isMatchingPriceInvLessPo(holder.getBuyerOid());
                boolean qtyInvLessGrn = businessRuleService.isMatchingQtyInvLessGrn(holder.getBuyerOid());
                
                boolean autoAcceptedQtyInvLessGrn = businessRuleService.isAutoAcceptQtyInvLessGrn(holder.getBuyerOid());
                boolean autoAcceptPriceInvLessPo = businessRuleService.isAutoAcceptPriceInvLessPo(holder.getBuyerOid());
                
                boolean isPriceUmatched = PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(holder.getMatchingStatus());
                boolean isQtyUnmatched = PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(holder.getMatchingStatus());
                boolean isUnmatched = PoInvGrnDnMatchingStatus.UNMATCHED.equals(holder.getMatchingStatus());
                
                List<PoInvGrnDnMatchingDetailExHolder> matchingDetails = poInvGrnDnMatchingDetailService.selectByMatchingOid(holder.getMatchingOid());
                
                for (PoInvGrnDnMatchingDetailExHolder detail : matchingDetails)
                {
                    PoInvGrnDnMatchingDetailExHolder newObj = new PoInvGrnDnMatchingDetailExHolder();
                    BeanUtils.copyProperties(detail, newObj);
                    
                    BigDecimal poPrice = (null == detail.getPoPrice()) ? BigDecimal.ZERO : detail.getPoPrice();
                    BigDecimal invPrice = (null == detail.getInvPrice()) ? BigDecimal.ZERO : detail.getInvPrice();
                    BigDecimal invQty = (null == detail.getInvQty()) ? BigDecimal.ZERO : detail.getInvQty();
                    BigDecimal grnQty = (null == detail.getGrnQty()) ? BigDecimal.ZERO : detail.getGrnQty();
                    
                    if (isPriceUmatched || isUnmatched)
                    {
                        if (invPrice.compareTo(poPrice) == 0)
                        {
                            newObj.setPriceStatus(PoInvGrnDnMatchingPriceStatus.ACCEPTED);
                            newObj.setPriceStatusActionBy(SYSTEM);
                            newObj.setPriceStatusActionDate(new Date());
                        }
                        else if ((priceInvLessPo || autoAcceptPriceInvLessPo) && invPrice.compareTo(poPrice) < 0)
                        {
                            newObj.setPriceStatus(PoInvGrnDnMatchingPriceStatus.ACCEPTED);
                            newObj.setPriceStatusActionBy(SYSTEM);
                            newObj.setPriceStatusActionDate(new Date());
                        }
                    }
                    if (isQtyUnmatched || isUnmatched)
                    {
                        if (invQty.compareTo(grnQty) == 0)
                        {
                            newObj.setQtyStatus(PoInvGrnDnMatchingQtyStatus.ACCEPTED);
                            newObj.setQtyStatusActionBy(SYSTEM);
                            newObj.setQtyStatusActionDate(new Date());
                        }
                        else if ((qtyInvLessGrn || autoAcceptedQtyInvLessGrn) && invQty.compareTo(grnQty) < 0)
                        {
                            newObj.setQtyStatus(PoInvGrnDnMatchingQtyStatus.ACCEPTED);
                            newObj.setQtyStatusActionBy(SYSTEM);
                            newObj.setQtyStatusActionDate(new Date());
                        }
                    }
                    
                    newObj.setPoNo(holder.getPoNo());
                    newObj.setPoStoreCode(holder.getPoStoreCode());
                    poInvGrnDnMatchingDetailService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), detail, newObj);
                    
                    if (!PoInvGrnDnMatchingPriceStatus.PENDING.equals(newObj.getPriceStatus()))
                    {
                        ItemHolder item = itemService.selectItemByBuyerOidAndBuyerItemCode(holder.getBuyerOid(), newObj.getBuyerItemCode());
                        if (item != null && item.getSubclassCode() != null && !item.getSubclassCode().isEmpty())
                        {
                            DocSubclassHolder docSubclass = new DocSubclassHolder();
                            docSubclass.setDocOid(holder.getMatchingOid());
                            docSubclass.setClassCode(item.getClassCode());
                            docSubclass.setSubclassCode(item.getSubclassCode());
                            docSubclass.setAuditFinished(true);
                            
                            docSubclassMapper.updateByPrimaryKeySelective(docSubclass);
                        }
                    }
                }
                
                matchingDetails = poInvGrnDnMatchingDetailService.selectByMatchingOid(holder.getMatchingOid());
                
                if (isPriceUmatched || isUnmatched)
                {
                    newHolder.setPriceStatus(PoInvGrnDnMatchingPriceStatus.ACCEPTED);
                    for (PoInvGrnDnMatchingDetailExHolder detail : matchingDetails)
                    {
                        if (PoInvGrnDnMatchingPriceStatus.PENDING.equals(detail.getPriceStatus()))
                        {
                            newHolder.setPriceStatus(PoInvGrnDnMatchingPriceStatus.PENDING);
                            break;
                        }
                    }
                }
                if (isQtyUnmatched || isUnmatched)
                {
                    newHolder.setQtyStatus(PoInvGrnDnMatchingQtyStatus.ACCEPTED);
                    for (PoInvGrnDnMatchingDetailExHolder detail : matchingDetails)
                    {
                        if (PoInvGrnDnMatchingQtyStatus.PENDING.equals(detail.getQtyStatus()))
                        {
                            newHolder.setQtyStatus(PoInvGrnDnMatchingQtyStatus.PENDING);
                            break;
                        }
                    }
                }
                
                if (isPriceUmatched && PoInvGrnDnMatchingPriceStatus.ACCEPTED.equals(newHolder.getPriceStatus()))
                {
                    newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.ACCEPTED);
                }
                
                if (isQtyUnmatched && PoInvGrnDnMatchingQtyStatus.ACCEPTED.equals(newHolder.getQtyStatus()))
                {
                    newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.ACCEPTED);
                }
                
                if (isUnmatched && PoInvGrnDnMatchingPriceStatus.ACCEPTED.equals(newHolder.getPriceStatus())
                    && PoInvGrnDnMatchingQtyStatus.ACCEPTED.equals(newHolder.getQtyStatus()))
                {
                    newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.ACCEPTED);
                }
                
                newHolder.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
            }
            
            newHolder.setSupplierStatusActionDate(new Date());
            newHolder.setSupplierStatusActionRemarks(param.getSupplierStatusActionRemarks());
            newHolder.setSupplierStatusActionBy(this.getProfileOfCurrentUser().getLoginId());
            poInvGrnDnMatchingService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), holder, newHolder);
            
            if (PoInvGrnDnMatchingBuyerStatus.ACCEPTED.equals(newHolder.getBuyerStatus()))
            {
                //auto close matching record
                boolean autoCloseAcceptedRecord = businessRuleService.isMatchingAutoCloseAcceptedRecord(newHolder.getBuyerOid());
                if (autoCloseAcceptedRecord)
                {
                    this.closeMatchingRecord(newHolder);
                }
            }
            if ("accept".equalsIgnoreCase(param.getSupplierStatusValue()))
            {
                this.result = "rejectsuccess";
            }
            else
            {
                this.result = "1";
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            this.result = "0";
        }
        return SUCCESS;
    }
    
    
    public String initClose()
    {
        try
        {
            String oid = this.getRequest().getParameter("matchingOid");
            if (null == oid)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }

            if (null != this.getSession().get(CAN_CLOSE_RECORD))
            {
                this.getSession().remove(CAN_CLOSE_RECORD);
            }
            BigDecimal matchingOid = new BigDecimal(oid);
            details = poInvGrnDnMatchingDetailService.selectByMatchingOid(matchingOid);
            
            PoInvGrnDnMatchingHolder matching = poInvGrnDnMatchingService.selectByKey(matchingOid);
            
            param = new PoInvGrnDnMatchingExHolder();
            
            BeanUtils.copyProperties(matching, param);
            
            BigDecimal buyerOid = poInvGrnDnMatchingService.selectByKey(details.get(0).getMatchingOid()).getBuyerOid();
            boolean qtyInvLessGrn = businessRuleService.isMatchingQtyInvLessGrn(buyerOid);
            
            // unit's initclose
            if (null == (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE))
            {
                for (PoInvGrnDnMatchingDetailExHolder detail : details)
                {
                    if (null != detail.getPriceStatus())
                    {
                        detail.setPriceStatusValue(this.getText(detail.getPriceStatus().getKey()));
                    }
                    if (null != detail.getQtyStatus())
                    {
                        detail.setQtyStatusValue(this.getText(detail.getQtyStatus().getKey()));
                    }
                    
                    if (null == detail.getInvPrice() || null == detail.getPoPrice())
                    {
                        detail.setPriceMatched(true);
                    }
                    else
                    {
                        if (detail.getPoPrice().compareTo(detail.getInvPrice()) == 0)
                        {
                            detail.setPriceMatched(true);
                        }
                        else
                        {
                            detail.setPriceMatched(false);
                        }
                    }
                    
                    if (null != detail.getInvQty() && null != detail.getGrnQty() && null != detail.getPoQty())
                    {
                        if (detail.getPoQty().compareTo(detail.getInvQty()) == 0)
                        {
                            if (qtyInvLessGrn && detail.getGrnQty().compareTo(detail.getInvQty()) > 0)
                            {
                                detail.setQtyMatched(true);
                            }
                            else if (detail.getGrnQty().compareTo(detail.getInvQty()) != 0)
                            {
                                detail.setQtyMatched(false);
                            }
                        }
                        else
                        {
                            detail.setQtyMatched(false);
                        }
                    }
                }
                this.getSession().put(IS_REVISED, "unit");
            }
            //fairprice's init close
            else
            {
                if (matching.getRevised())
                {
                    //get all old matching records.
                    PoInvGrnDnMatchingExHolder param = new PoInvGrnDnMatchingExHolder();
                    param.setPoNo(matching.getPoNo());
                    param.setPoStoreCode(matching.getPoStoreCode());
                    param.setMatchingStatus(PoInvGrnDnMatchingStatus.OUTDATED);
                    param.setRevised(false);
                    param.setAllEmptyStringToNull();
                    List<PoInvGrnDnMatchingHolder> matchings = poInvGrnDnMatchingService.select(param);
                    
                    //sort by matching date to get the last matching record.
                    Collections.sort(matchings, new Comparator<PoInvGrnDnMatchingHolder>()
                    {
                        @Override
                        public int compare(PoInvGrnDnMatchingHolder m1, PoInvGrnDnMatchingHolder m2)
                        {
                            return m1.getMatchingDate().before(m2.getMatchingDate()) ? 1 : -1;
                        }
                    });
                    
                    //get last matching record's details.
                    List<PoInvGrnDnMatchingDetailExHolder> oldDetails = poInvGrnDnMatchingDetailService.selectByMatchingOid(matchings.get(0).getMatchingOid());
                    
                    //compare two details' price and qty.
                    for (PoInvGrnDnMatchingDetailExHolder detail : details)
                    {
                        for (PoInvGrnDnMatchingDetailExHolder oldDetail : oldDetails)
                        {
                            if (detail.getBuyerItemCode().equals(oldDetail.getBuyerItemCode()))
                            {
                                if (oldDetail.getPriceStatus() != null)
                                {
                                    oldDetail.setPriceStatusValue(this.getText(oldDetail.getPriceStatus().getKey()));
                                }
                                if (oldDetail.getQtyStatus() != null)
                                {
                                    oldDetail.setQtyStatusValue(this.getText(oldDetail.getQtyStatus().getKey()));
                                }
                                
                                if (detail.getInvPrice() == null || oldDetail.getInvPrice() == null)
                                {
                                    detail.setPriceMatched(true);
                                }
                                else
                                {
                                    if (detail.getInvPrice().compareTo(oldDetail.getInvPrice()) == 0)
                                    {
                                        detail.setPriceMatched(true);
                                    }
                                    else
                                    {
                                        detail.setPriceMatched(false);
                                    }
                                }
                                
                                if ( null == detail.getInvQty() || null == oldDetail.getInvQty())
                                {
                                    detail.setQtyMatched(true);
                                }
                                else
                                {
                                    if (detail.getInvQty().compareTo(oldDetail.getInvQty()) == 0)
                                    {
                                        detail.setQtyMatched(true);
                                    }
                                    else
                                    {
                                        detail.setQtyMatched(false);
                                    }
                                }
                                
                                detail.setOldDetail(oldDetail);
                            }
                        }
                    }
                    
                    this.getSession().put(IS_REVISED, "yes");
                }
                else
                {
                    for (PoInvGrnDnMatchingDetailExHolder detail : details)
                    {
                        if (null != detail.getPriceStatus())
                        {
                            detail.setPriceStatusValue(this.getText(detail.getPriceStatus().getKey()));
                        }
                        if (null != detail.getQtyStatus())
                        {
                            detail.setQtyStatusValue(this.getText(detail.getQtyStatus().getKey()));
                        }
                        
                        if (null == detail.getInvPrice() || null == detail.getPoPrice())
                        {
                            detail.setPriceMatched(true);
                        }
                        else
                        {
                            if (detail.getPoPrice().compareTo(detail.getInvPrice()) == 0)
                            {
                                detail.setPriceMatched(true);
                            }
                            else
                            {
                                detail.setPriceMatched(false);
                            }
                        }
                        
                        if (null != detail.getInvQty() && null != detail.getGrnQty() && null != detail.getPoQty())
                        {
                            if (detail.getPoQty().compareTo(detail.getInvQty()) == 0)
                            {
                                if (qtyInvLessGrn && detail.getGrnQty().compareTo(detail.getInvQty()) > 0)
                                {
                                    detail.setQtyMatched(true);
                                }
                                else if (detail.getGrnQty().compareTo(detail.getInvQty()) != 0)
                                {
                                    detail.setQtyMatched(false);
                                }
                            }
                            else
                            {
                                detail.setQtyMatched(false);
                            }
                        }
                    }
                    this.getSession().put(IS_REVISED, "no");
                }
            }
            
            if (PoInvGrnDnMatchingStatus.OUTDATED.equals(matching.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.PENDING.equals(matching.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.INSUFFICIENT_INV.equals(matching.getMatchingStatus())
                || matching.getClosed())
            {
                this.getSession().put(CAN_CLOSE_RECORD, "no");
            }
            
            this.getSession().put(SESSION_OID_PARAMETER, oid);
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String saveClose()
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
                boolean flag = false;
                BigDecimal matchingOid = new BigDecimal(part);
                PoInvGrnDnMatchingHolder matching = poInvGrnDnMatchingService.selectByKey(matchingOid);
                if (!flag && null == matching)
                {
                    msg.saveError(this.getText("B2BPC2120",
                            new String[] { part }));
                    flag = true;
                }
                if (!flag && matching.getClosed())
                {
                    msg.saveError(this.getText("B2BPC2121",
                        new String[] { matching.getPoNo(), matching.getPoStoreCode() }));
                    flag = true;
                }
                if (!flag && PoInvGrnDnMatchingStatus.PENDING.equals(matching.getMatchingStatus()))
                {
                    msg.saveError(this.getText("B2BPC2134",
                        new String[] { matching.getPoNo(), matching.getPoStoreCode() }));
                    flag = true;
                }
                if (!flag && PoInvGrnDnMatchingStatus.INSUFFICIENT_INV.equals(matching.getMatchingStatus()))
                {
                    msg.saveError(this.getText("B2BPC2118",
                        new String[] { matching.getPoNo(), matching.getPoStoreCode() }));
                    flag = true;
                }
                if (!flag && PoInvGrnDnMatchingStatus.OUTDATED.equals(matching.getMatchingStatus()))
                {
                    msg.saveError(this.getText("B2BPC2119",
                        new String[] { matching.getPoNo(), matching.getPoStoreCode() }));
                    flag = true;
                }
                if (!matching.getRevised() && null != (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE))
                {
                    if (!flag && PoInvGrnDnMatchingSupplierStatus.PENDING.equals(matching.getSupplierStatus()))
                    {
                        msg.saveError(this.getText("B2BPC2116",
                            new String[] { matching.getPoNo(), matching.getPoStoreCode() }));
                        flag = true;
                    }
                    if (!flag && PoInvGrnDnMatchingSupplierStatus.REJECTED.equals(matching.getSupplierStatus()) 
                        && PoInvGrnDnMatchingBuyerStatus.PENDING.equals(matching.getBuyerStatus()))
                    {
                        msg.saveError(this.getText("B2BPC2117",
                            new String[] { matching.getPoNo(), matching.getPoStoreCode() }));
                        flag = true;
                    }
                }
                if (!flag)
                {
                    msg.saveSuccess(this.getText("B2BPC2115",
                        new String[] { matching.getPoNo(), matching.getPoStoreCode() }));
                    if (param != null && null != param.getClosedRemarks())
                    {
                        matching.setClosedRemarks(param.getClosedRemarks());
                    }
                    this.closeMatchingRecord(matching);
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
    
    
    @SuppressWarnings("unchecked")
    public String initAuditPrice()
    {
        try
        {
            String oid = this.getRequest().getParameter("matchingOid");
            if (null == oid)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }

            BigDecimal matchingOid = new BigDecimal(oid);
            details = poInvGrnDnMatchingDetailService.selectByMatchingOid(matchingOid);
            
            PoInvGrnDnMatchingHolder holder = poInvGrnDnMatchingService.selectByKey(matchingOid);
            
            param = new PoInvGrnDnMatchingExHolder();
            param.setSupplierStatusActionRemarks(holder.getSupplierStatusActionRemarks());
            if (PoInvGrnDnMatchingSupplierStatus.REJECTED.equals(holder.getSupplierStatus())
                && PoInvGrnDnMatchingBuyerStatus.PENDING.equals(holder.getBuyerStatus()))
            {
                param.setSupplierStatusValue(this.getText(holder.getSupplierStatus().getKey()));
                param.setBuyerStatusValue(this.getText(holder.getBuyerStatus().getKey()));
                param.setPriceStatusValue(this.getText(holder.getPriceStatus().getKey()));
            }
            
            boolean isBuyer = BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                                || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser());
            
            
            if (isBuyer && null == this.getSession().get(BUYER_ALL_GRANTED) && null == this.getSession().get(BUYER_ITEM_CODES))
            {
                BigDecimal userOid = this.getProfileOfCurrentUser().getUserOid();
                List<UserClassHolder> classes = userClassService.selectByUserOid(userOid);
                List<UserSubclassHolder> subClasses = userSubclassService.selectByUserOid(userOid);
                if (classes.isEmpty() && subClasses.isEmpty())
                {
                    buyerItemCodes = new ArrayList<String>();
                    this.getSession().put(BUYER_ITEM_CODES, buyerItemCodes);
                }
                else if ((!classes.isEmpty() && new BigDecimal(-1).equals(classes.get(0).getClassOid())) || (!subClasses.isEmpty() && new BigDecimal(-1).equals(subClasses.get(0).getSubclassOid())))
                {
                    this.getSession().put(BUYER_ALL_GRANTED, "true");
                }
                else
                {
                    buyerItemCodes = itemService.selectActiveItemsByUserOid(userOid);
                    this.getSession().put(BUYER_ITEM_CODES, buyerItemCodes);
                }
            }
            
            BigDecimal buyerOid = poInvGrnDnMatchingService.selectByKey(details.get(0).getMatchingOid()).getBuyerOid();
            boolean priceInvLessPo = businessRuleService.isMatchingPriceInvLessPo(buyerOid);
            
            for (PoInvGrnDnMatchingDetailExHolder detail : details)
            {
                if (detail.getPriceStatus() != null)
                {
                    detail.setPriceStatusValue(this.getText(detail.getPriceStatus().getKey()));
                }
                
                if (detail.getInvPrice() == null || detail.getPoPrice() == null)
                {
                    detail.setPriceMatched(true);
                }
                else
                {
                    if (!priceInvLessPo)
                    {
                        if (detail.getPoPrice().compareTo(detail.getInvPrice()) == 0)
                        {
                            detail.setPriceMatched(true);
                        }
                        else
                        {
                            detail.setPriceMatched(false);
                        }
                    }
                    else
                    {
                        if (detail.getPoPrice().compareTo(detail.getInvPrice()) >= 0)
                        {
                            detail.setPriceMatched(true);
                        }
                        else
                        {
                            detail.setPriceMatched(false);
                        }
                    }
                }
                
                if (isBuyer)
                {
                    if ("true".equals(this.getSession().get(BUYER_ALL_GRANTED)))
                    {
                        detail.setPrivileged(true);
                    }
                    else if (((List<String>)this.getSession().get(BUYER_ITEM_CODES)).contains(detail.getBuyerItemCode() + "-" + holder.getBuyerOid()))
                    {
                        detail.setPrivileged(true);
                    }
                    else
                    {
                        detail.setPrivileged(false);
                    }
                }
                
                if (PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(holder.getMatchingStatus()))
                {
                    detail.setPrivileged(false);
                }
            
                if (PoInvGrnDnMatchingSupplierStatus.ACCEPTED.equals(holder.getSupplierStatus())
                    || PoInvGrnDnMatchingSupplierStatus.PENDING.equals(holder.getSupplierStatus())
                    || PoInvGrnDnMatchingStatus.INSUFFICIENT_INV.equals(holder.getMatchingStatus())
                    || PoInvGrnDnMatchingStatus.OUTDATED.equals(holder.getMatchingStatus())
                    || PoInvGrnDnMatchingStatus.MATCHED.equals(holder.getMatchingStatus()))
                {
                    detail.setIsJustShow(true);
                    detail.setPrivileged(false);
                }
            }
            
            String isAllAudited = null;
            
            if (PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(holder.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.UNMATCHED.equals(holder.getMatchingStatus()))
            {
                for (PoInvGrnDnMatchingDetailExHolder detail : details)
                {
                    if (PoInvGrnDnMatchingPriceStatus.PENDING.equals(detail.getPriceStatus()) && detail.getPrivileged())
                    {
                        isAllAudited = "no";
                        break;
                    }
                }
                if (isAllAudited == null)
                {
                    isAllAudited = "yes";
                }
            }
            else
            {
                isAllAudited = "yes";
            }
            
            this.getSession().put(AUDIT_TYPE, "price");
            this.getSession().put(SESSION_OID_PARAMETER, oid);
            this.getSession().put(IS_ALL_AUDITED, isAllAudited);
            
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String initAuditQty()
    {
        try
        {
            String oid = this.getRequest().getParameter("matchingOid");
            if (null == oid)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }
            BigDecimal matchingOid = new BigDecimal(oid);
            details = poInvGrnDnMatchingDetailService.selectByMatchingOid(matchingOid);
            
            PoInvGrnDnMatchingHolder holder = poInvGrnDnMatchingService.selectByKey(matchingOid);
            
            param = new PoInvGrnDnMatchingExHolder();
            param.setSupplierStatusActionRemarks(holder.getSupplierStatusActionRemarks());
            if (PoInvGrnDnMatchingSupplierStatus.REJECTED.equals(holder.getSupplierStatus())
                && PoInvGrnDnMatchingBuyerStatus.PENDING.equals(holder.getBuyerStatus()))
            {
                param.setSupplierStatusValue(this.getText(holder.getSupplierStatus().getKey()));
                param.setBuyerStatusValue(this.getText(holder.getBuyerStatus().getKey()));
                param.setQtyStatusValue(this.getText(holder.getQtyStatus().getKey()));
            }
            
            BigDecimal buyerOid = poInvGrnDnMatchingService.selectByKey(details.get(0).getMatchingOid()).getBuyerOid();
            //1: QtyInvLessGrn and QtyPoLessGrn   2:QtyInvLessGrn and !QtyPoLessGrn   3:!QtyInvLessGrn
            int matchingQtyRule = businessRuleService.selectMatchingQtyRule(buyerOid);
            
            for (PoInvGrnDnMatchingDetailExHolder detail : details)
            {
                if (detail.getQtyStatus() != null)
                {
                    detail.setQtyStatusValue(this.getText(detail.getQtyStatus().getKey()));
                }
                
                BigDecimal poQty = (null == detail.getPoQty()) ? BigDecimal.ZERO : detail.getPoQty();
                BigDecimal invQty = (null == detail.getInvQty()) ? BigDecimal.ZERO : detail.getInvQty();
                BigDecimal grnQty = (null == detail.getGrnQty()) ? BigDecimal.ZERO : detail.getGrnQty();
               
                if (matchingQtyRule == 1)
                {
                    if (grnQty.compareTo(invQty) >= 0)
                    {
                        detail.setQtyMatched(true);
                    }
                    else
                    {
                        detail.setQtyMatched(false);
                    }
                }
                
                if (matchingQtyRule == 2)
                {
                    if (grnQty.compareTo(invQty) >= 0 && grnQty.compareTo(poQty) <= 0)
                    {
                        detail.setQtyMatched(true);
                    }
                    else
                    {
                        detail.setQtyMatched(false);
                    }
                }
                
                if(matchingQtyRule == 3)
                {
                    if (grnQty.compareTo(invQty) == 0 && grnQty.compareTo(poQty) <= 0)
                    {
                        detail.setQtyMatched(true);
                    }
                    else
                    {
                        detail.setQtyMatched(false);
                    }
                }
                
                if (PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(holder.getMatchingStatus()))
                {
                    detail.setPrivileged(false);
                }
                else
                {
                    detail.setPrivileged(true);
                }
                
                if (PoInvGrnDnMatchingSupplierStatus.ACCEPTED.equals(holder.getSupplierStatus())
                    || PoInvGrnDnMatchingSupplierStatus.PENDING.equals(holder.getSupplierStatus())
                    || PoInvGrnDnMatchingStatus.INSUFFICIENT_INV.equals(holder.getMatchingStatus())
                    || PoInvGrnDnMatchingStatus.OUTDATED.equals(holder.getMatchingStatus())
                    || PoInvGrnDnMatchingStatus.MATCHED.equals(holder.getMatchingStatus()))
                {
                    detail.setIsJustShow(true);
                    detail.setPrivileged(false);
                }
            }
            
            String isAllAudited = null;
            
            if (PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(holder.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.UNMATCHED.equals(holder.getMatchingStatus()))
            {
                for (PoInvGrnDnMatchingDetailExHolder detail : details)
                {
                    if (PoInvGrnDnMatchingQtyStatus.PENDING.equals(detail.getQtyStatus()) && detail.getPrivileged())
                    {
                        isAllAudited = "no";
                        break;
                    }
                }
                if (isAllAudited == null)
                {
                    isAllAudited = "yes";
                }
            }
            else
            {
                isAllAudited = "yes";
            }
            this.getSession().put(AUDIT_TYPE, "qty");
            this.getSession().put(SESSION_OID_PARAMETER, oid);
            this.getSession().put(IS_ALL_AUDITED, isAllAudited);
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String saveAudit()
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
            
            boolean isPrice = "price".equals(parts[0].split(COMMA_DELIMITOR)[4]);
            boolean isQty = "qty".equals(parts[0].split(COMMA_DELIMITOR)[4]);
            
            BigDecimal matchingOid = new BigDecimal(selectedOids.toString().split(
                REQUEST_OID_DELIMITER)[0].split(COMMA_DELIMITOR)[1]);
            PoInvGrnDnMatchingHolder pigd = poInvGrnDnMatchingService.selectByKey(matchingOid);

            for (String part : parts)
            {
                String[] objects = part.split(COMMA_DELIMITOR);
                
                int seq = Integer.parseInt(objects[2]);
                
                PoInvGrnDnMatchingDetailExHolder detail = poInvGrnDnMatchingDetailService.selectByMatchingOidAndSeq(matchingOid, seq);
                
                if (null == detail)
                {
                    this.result = "notExist";
                    return SUCCESS;
                }
                
                if (!PoInvGrnDnMatchingBuyerStatus.PENDING.equals(pigd.getBuyerStatus()))
                {
                    this.result = "allAudit";
                    return SUCCESS;
                }
                
                
                if ("reject".equalsIgnoreCase(objects[0]) && (null == objects[3] || objects[3].length() == 0 || objects[3].length() > 255))
                {
                    this.result = "remarks";
                    return SUCCESS;
                }
                
                PoInvGrnDnMatchingDetailExHolder newObj = new PoInvGrnDnMatchingDetailExHolder();
                BeanUtils.copyProperties(detail, newObj);
                if (isPrice)
                {
                    if ("accept".equalsIgnoreCase(objects[0]))
                    {
                        newObj.setPriceStatus(PoInvGrnDnMatchingPriceStatus.ACCEPTED);
                    }
                    if ("reject".equalsIgnoreCase(objects[0]))
                    {
                        newObj.setPriceStatus(PoInvGrnDnMatchingPriceStatus.REJECTED);
                    }
                    newObj.setPriceStatusActionDate(new Date());
                    newObj.setPriceStatusActionBy(this.getProfileOfCurrentUser().getLoginId());
                    newObj.setPriceStatusActionRemarks( objects[3].trim().isEmpty() ? null : objects[3].trim());
                    
                    //update the doc sub class audit_finished.
                    
                    ItemHolder item = itemService.selectItemByBuyerOidAndBuyerItemCode(pigd.getBuyerOid(), newObj.getBuyerItemCode());
                    if (item != null && item.getSubclassCode() != null && !item.getSubclassCode().isEmpty())
                    {
                        DocSubclassHolder docSubclass = new DocSubclassHolder();
                        docSubclass.setDocOid(matchingOid);
                        docSubclass.setClassCode(item.getClassCode());
                        docSubclass.setSubclassCode(item.getSubclassCode());
                        docSubclass.setAuditFinished(true);
                        
                        docSubclassMapper.updateByPrimaryKeySelective(docSubclass);
                    }
                }
                if (isQty)
                {
                    if ("accept".equalsIgnoreCase(objects[0]))
                    {
                        newObj.setQtyStatus(PoInvGrnDnMatchingQtyStatus.ACCEPTED);
                    }
                    if ("reject".equalsIgnoreCase(objects[0]))
                    {
                        newObj.setQtyStatus(PoInvGrnDnMatchingQtyStatus.REJECTED);
                    }
                    newObj.setQtyStatusActionDate(new Date());
                    newObj.setQtyStatusActionBy(this.getProfileOfCurrentUser().getLoginId());
                    newObj.setQtyStatusActionRemarks( objects[3].trim().isEmpty() ? null : objects[3].trim());
                }
                
                newObj.setPoNo(pigd.getPoNo());
                newObj.setPoStoreCode(pigd.getPoStoreCode());
                poInvGrnDnMatchingDetailService.auditUpdateByPrimaryKey(this.getCommonParameter(), detail, newObj);
            }

            //judge if all items have been audited 0:not all audited  1:accept  2:rejected 3:unmatched price all audited  4:unmatched qty all audit
            //judge if all audit, unmatched, price unmatched, qty unmatched.
            
            int isAllItemsAudited = 0;
            List<PoInvGrnDnMatchingDetailExHolder> matchings = poInvGrnDnMatchingDetailService.selectByMatchingOid(matchingOid);
            PoInvGrnDnMatchingHolder matchingHolder = pigd;
            for (PoInvGrnDnMatchingDetailExHolder matching : matchings)
            {
                if (PoInvGrnDnMatchingStatus.UNMATCHED.equals(matchingHolder.getMatchingStatus())
                    && (PoInvGrnDnMatchingPriceStatus.PENDING.equals(matching.getPriceStatus()) 
                        || PoInvGrnDnMatchingQtyStatus.PENDING.equals(matching.getQtyStatus())))
                {
                    isAllItemsAudited = 0;
                    break;
                }
                else if ((isPrice && PoInvGrnDnMatchingPriceStatus.PENDING.equals(matching.getPriceStatus()) && PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(matchingHolder.getMatchingStatus()))
                        || (isQty && PoInvGrnDnMatchingQtyStatus.PENDING.equals(matching.getQtyStatus()) && PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(matchingHolder.getMatchingStatus())))
                {
                        isAllItemsAudited = -2;
                        break;
                }
                else
                {
                    isAllItemsAudited = -1;
                }
            }
            //judge if all price or all qty status audited
            if (isAllItemsAudited == 0)
            {
                String info = "";
                PoInvGrnDnMatchingHolder oldHolder = pigd;
                PoInvGrnDnMatchingHolder newHolder = new PoInvGrnDnMatchingHolder();
                BeanUtils.copyProperties(oldHolder, newHolder);
                for (PoInvGrnDnMatchingDetailExHolder matching : matchings)
                {
                    if ((isPrice && PoInvGrnDnMatchingPriceStatus.PENDING.equals(matching.getPriceStatus()))
                       || (isQty && PoInvGrnDnMatchingQtyStatus.PENDING.equals(matching.getQtyStatus())))
                    {
                        info = "pending";
                        break;
                    }
                }
                if (!"pending".equals(info))
                {
                    for (PoInvGrnDnMatchingDetailExHolder matching : matchings)
                    {
                        if ((isPrice && PoInvGrnDnMatchingPriceStatus.REJECTED.equals(matching.getPriceStatus()))
                            || (isQty && PoInvGrnDnMatchingQtyStatus.REJECTED.equals(matching.getQtyStatus())))
                         {
                             info = "reject";
                             break;
                         }
                         else
                         {
                             info = "accept";
                         }
                    }
                    if ("reject".equals(info))
                    {
                        if (isPrice)
                        {
                            newHolder.setPriceStatus(PoInvGrnDnMatchingPriceStatus.REJECTED);
                        }
                        if (isQty)
                        {
                            newHolder.setQtyStatus(PoInvGrnDnMatchingQtyStatus.REJECTED);
                        }
                    }
                    else if("accept".equals(info))
                    {
                        if (isPrice)
                        {
                            newHolder.setPriceStatus(PoInvGrnDnMatchingPriceStatus.ACCEPTED);
                        }
                        if (isQty)
                        {
                            newHolder.setQtyStatus(PoInvGrnDnMatchingQtyStatus.ACCEPTED);
                        }
                    }
                
                    //update the price status or qty status of matching record.
                    poInvGrnDnMatchingService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), oldHolder, newHolder);
                }
            }
            
            else if (isAllItemsAudited == -1)
            {
                //judge accpet or reject the price status and qty status.
                for (PoInvGrnDnMatchingDetailExHolder matching : matchings)
                {
                    if (isPrice)
                    {
                        if (PoInvGrnDnMatchingStatus.UNMATCHED.equals(matchingHolder.getMatchingStatus())
                            && PoInvGrnDnMatchingPriceStatus.REJECTED.equals(matching.getPriceStatus()))
                        {
                            isAllItemsAudited = 2;
                            break;
                        }
                        else if (PoInvGrnDnMatchingPriceStatus.REJECTED.equals(matching.getPriceStatus()))
                        {
                            isAllItemsAudited = 2;
                            break;
                        }
                        else
                        {
                            isAllItemsAudited = 1;
                        }
                    }
                    if (isQty)
                    {
                        if (PoInvGrnDnMatchingStatus.UNMATCHED.equals(matchingHolder.getMatchingStatus())
                            && PoInvGrnDnMatchingQtyStatus.REJECTED.equals(matching.getQtyStatus()))
                        {
                            isAllItemsAudited = 2;
                            break;
                        }
                        else if (PoInvGrnDnMatchingQtyStatus.REJECTED.equals(matching.getQtyStatus()))
                        {
                            isAllItemsAudited = 2;
                            break;
                        }
                        else
                        {
                            isAllItemsAudited = 1;
                        }
                    }
                }
                PoInvGrnDnMatchingHolder oldHolder = pigd;
                PoInvGrnDnMatchingHolder newHolder = new PoInvGrnDnMatchingHolder();
                BeanUtils.copyProperties(oldHolder, newHolder);
                if (isAllItemsAudited == 1)
                {
                    if (isPrice)
                    {
                        newHolder.setPriceStatus(PoInvGrnDnMatchingPriceStatus.ACCEPTED);
                    }
                    else
                    {
                        newHolder.setQtyStatus(PoInvGrnDnMatchingQtyStatus.ACCEPTED);
                    }
                }
                if (isAllItemsAudited == 2)
                {
                    if (isPrice)
                    {
                        newHolder.setPriceStatus(PoInvGrnDnMatchingPriceStatus.REJECTED);
                    }
                    else
                    {
                        newHolder.setQtyStatus(PoInvGrnDnMatchingQtyStatus.REJECTED);
                    }
                }
                
                //update the buyer status
                if (PoInvGrnDnMatchingStatus.UNMATCHED.equals(newHolder.getMatchingStatus()))
                {
                    if (PoInvGrnDnMatchingPriceStatus.REJECTED.equals(newHolder.getPriceStatus()) 
                        || PoInvGrnDnMatchingQtyStatus.REJECTED.equals(newHolder.getQtyStatus()))
                    {
                        newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.REJECTED);
                    }
                    else
                    {
                        newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.ACCEPTED);
                    }
                }
                else if (PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(newHolder.getMatchingStatus()))
                {
                    if (PoInvGrnDnMatchingPriceStatus.REJECTED.equals(newHolder.getPriceStatus()))
                    {
                        newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.REJECTED);
                    }
                    else if (PoInvGrnDnMatchingPriceStatus.ACCEPTED.equals(newHolder.getPriceStatus()))
                    {
                        newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.ACCEPTED);
                    }
                }
                else if (PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(newHolder.getMatchingStatus()))
                {
                    if (PoInvGrnDnMatchingQtyStatus.REJECTED.equals(newHolder.getQtyStatus()))
                    {
                        newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.REJECTED);
                    }
                    else if (PoInvGrnDnMatchingQtyStatus.ACCEPTED.equals(newHolder.getQtyStatus()))
                    {
                        newHolder.setBuyerStatus(PoInvGrnDnMatchingBuyerStatus.ACCEPTED);
                    }
                }
                
                poInvGrnDnMatchingService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), oldHolder, newHolder);
                
                if (PoInvGrnDnMatchingBuyerStatus.ACCEPTED.equals(newHolder.getBuyerStatus()))
                {
                    //auto close matching record
                    boolean autoCloseAcceptedRecord = businessRuleService.isMatchingAutoCloseAcceptedRecord(newHolder.getBuyerOid());
                    if (autoCloseAcceptedRecord)
                    {
                        this.closeMatchingRecord(newHolder);
                    }
                }
                if (PoInvGrnDnMatchingBuyerStatus.REJECTED.equals(newHolder.getBuyerStatus()))
                {
                    boolean autoCloseRejectedRecord = businessRuleService.isAutoCloseRejectedMatchingRecord(newHolder.getBuyerOid());
                    if (autoCloseRejectedRecord)
                    {
                        this.closeMatchingRecord(newHolder);
                    }
                }
            }
            
            this.result = "1";
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String viewDetails()
    {
        try
        {
            details = poInvGrnDnMatchingDetailService.selectByMatchingOid(param.getMatchingOid());
            
            PoInvGrnDnMatchingHolder matching = poInvGrnDnMatchingService.selectByKey(param.getMatchingOid());
            param = new PoInvGrnDnMatchingExHolder();
            
            BeanUtils.copyProperties(matching, param);
            
            BigDecimal buyerOid = poInvGrnDnMatchingService.selectByKey(param.getMatchingOid()).getBuyerOid();
            boolean priceInvLessPo = businessRuleService.isMatchingPriceInvLessPo(buyerOid);
            //1: QtyInvLessGrn and QtyPoLessGrn   2:QtyInvLessGrn and !QtyPoLessGrn   3:!QtyInvLessGrn
            int matchingQtyRule = businessRuleService.selectMatchingQtyRule(buyerOid);
            
            for (PoInvGrnDnMatchingDetailExHolder detail : details)
            {
                if (detail.getPriceStatus() != null)
                {
                    detail.setPriceStatusValue(this.getText(detail.getPriceStatus().getKey()));
                }
                if (detail.getQtyStatus() != null)
                {
                    detail.setQtyStatusValue(this.getText(detail.getQtyStatus().getKey()));
                }
                //price matched?
                if (detail.getInvPrice() == null || detail.getPoPrice() == null)
                {
                    detail.setPriceMatched(true);
                }
                else
                {
                    if (!priceInvLessPo)
                    {
                        if (detail.getPoPrice().compareTo(detail.getInvPrice()) == 0)
                        {
                            detail.setPriceMatched(true);
                        }
                        else
                        {
                            detail.setPriceMatched(false);
                        }
                    }
                    else
                    {
                        if (detail.getPoPrice().compareTo(detail.getInvPrice()) >= 0)
                        {
                            detail.setPriceMatched(true);
                        }
                        else
                        {
                            detail.setPriceMatched(false);
                        }
                    }
                }
                
                // qty matched?
                BigDecimal poQty = (null == detail.getPoQty()) ? BigDecimal.ZERO : detail.getPoQty();
                BigDecimal invQty = (null == detail.getInvQty()) ? BigDecimal.ZERO : detail.getInvQty();
                BigDecimal grnQty = (null == detail.getGrnQty()) ? BigDecimal.ZERO : detail.getGrnQty();
               
                if (matchingQtyRule == 1)
                {
                    if (grnQty.compareTo(invQty) >= 0)
                    {
                        detail.setQtyMatched(true);
                    }
                    else
                    {
                        detail.setQtyMatched(false);
                    }
                }
                
                if (matchingQtyRule == 2)
                {
                    if (grnQty.compareTo(invQty) >= 0 && grnQty.compareTo(poQty) <= 0)
                    {
                        detail.setQtyMatched(true);
                    }
                    else
                    {
                        detail.setQtyMatched(false);
                    }
                }
                
                if(matchingQtyRule == 3)
                {
                    if (grnQty.compareTo(invQty) == 0 && grnQty.compareTo(poQty) <= 0)
                    {
                        detail.setQtyMatched(true);
                    }
                    else
                    {
                        detail.setQtyMatched(false);
                    }
                }
            }
            
            PoInvGrnDnMatchingHolder holder = poInvGrnDnMatchingService.selectByKey(param.getMatchingOid());
            if(holder.getClosed() || PoInvGrnDnMatchingStatus.PENDING.equals(holder.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.MATCHED.equals(holder.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.INSUFFICIENT_INV.equals(holder.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.OUTDATED.equals(holder.getMatchingStatus())
                || holder.getRevised()
                || PoInvGrnDnMatchingSupplierStatus.ACCEPTED.equals(holder.getSupplierStatus())
                || PoInvGrnDnMatchingSupplierStatus.REJECTED.equals(holder.getSupplierStatus()))
            {
                this.getSession().put(IF_ACCEPT_REJECT, "no");
            }
            else
            {
                this.getSession().put(IF_ACCEPT_REJECT, "yes");
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
    // approve function
    // *****************************************************
    public String exportExcel()
    {
        try
        {
            List<BigDecimal> matchingOids = new ArrayList<BigDecimal>();
            if (selectAll)
            {
                param = (PoInvGrnDnMatchingExHolder) getSession().get(
                        SESSION_KEY_SEARCH_PARAMETER_PIGD_MATCHING);
                if (param == null)
                {
                    param = new PoInvGrnDnMatchingExHolder();
                    
                    initDefaultSearchCondition(param);
                    
                    param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                    initCurrentUserSearchParam(param);
                }
                
                List<PoInvGrnDnMatchingHolder> poInvGrnDnMatchingList = poInvGrnDnMatchingService.selectAllRecordToExport(param);
                if (poInvGrnDnMatchingList != null && !poInvGrnDnMatchingList.isEmpty())
                {
                    for (PoInvGrnDnMatchingHolder holder : poInvGrnDnMatchingList)
                    {
                        matchingOids.add(holder.getMatchingOid());
                    }
                }
            }
            else
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
                    matchingOids.add(new BigDecimal(part));
                }
            }
            
            boolean isBuyer = this.isCurrentUserBuyerAdmin() || this.isCurrentUserBuyerUser();
            
            this.setRptResult(new ByteArrayInputStream(poInvGrnDnMatchingService.exportExcel(matchingOids, isBuyer)));
            this.setRptFileName("MatchingReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls");
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        return SUCCESS;
    }
    
    
    public String printPdf() throws Exception
    {
        String docType = this.getRequest().getParameter("docType");
        String docOid = this.getRequest().getParameter("docOid");
        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(new BigDecimal(docOid));
        SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
        //String path = appConfig.getSupplierMailboxRootPath() + PS + supplier.getMboxId() + PS + "doc" + PS;
        String path = mboxUtil.getSupplierDocPath(supplier.getMboxId()) + PS;
        String yyyyMM = DateUtil.getInstance().convertDateToString(msg.getSentDate(), "yyyyMM");
        if ("PO".equals(docType))
        {
            String matchingOid = this.getRequest().getParameter("matchingOid");
            ReportEngineParameter<PoHolder> reportParameter = null;
            InputStream [] inputs= new InputStream[1];
            rptFileName = msg.getReportFilename();
            PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(new BigDecimal(docOid));
            BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                    MsgType.PO.name(), poHeader.getPoType().name());
            
            DefaultPoReportEngine poReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                .getBuyerCode());
            
            reportParameter = retrieveParameter(msg, supplier, matchingOid);
            
            int flag = DefaultReportEngine.PDF_TYPE_STANDARD;
            if ("yes".equals(this.getSession().get(SESSION_SUPPLIER_DISPUTE)))
            {
                if (BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
                {
                    flag = DefaultReportEngine.PDF_TYPE_BY_STORE_STORE;
                }
                if((BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(5).equals(this.getUserTypeOfCurrentUser())))
                {
                    flag = DefaultReportEngine.PDF_TYPE_BY_STORE;
                }
            }
            
            byte[] datas = poReportEngine.generateReport(reportParameter, flag);
            
            inputs[0] = new ByteArrayInputStream(datas);
            
            rptResult = new ByteArrayInputStream(PdfReportUtil.getInstance().mergePDFsByte(inputs));
            
            return "PO";
        }
        else if ("GRN".equals(docType))
        {
            path = path + "in" + PS + yyyyMM;
        }
        else if ("INV".equals(docType))
        {
            path = path + "out" + PS + yyyyMM;
        }
        else if ("DN".equals(docType))
        {
            path = path + "in" + PS + yyyyMM;
        }

        if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 
            || BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
        {
            rptFileName = FileUtil.getInstance().trimAllExtension(msg.getReportFilename()) + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".pdf";
        }
        
        File file = new File(path + PS + rptFileName);
        
        if(!file.exists())
        {
            if ("GRN".equals(docType))
            {
                this.getSession().put("session.parameter.GoodsReceivedNoteAction.selectedOids", docOid);
                return "GRN";
            }
            else if ("INV".equals(docType))
            {
                this.getSession().put("session.parameter.eInvoice.selectedOids", docOid);
                return "INV";
            }
            else if ("DN".equals(docType))
            {
                this.getSession().put("session.parameter.DebitNoteAction.selectedOids", docOid);
                return "DN";
            }
        }
        
        rptFileName = file.getName();
        rptResult = new FileInputStream(file);

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

    private ReportEngineParameter<PoHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier, String matchingOid) throws Exception
    {
        PoInvGrnDnMatchingHolder  matching =  poInvGrnDnMatchingService.selectByKey(new BigDecimal(matchingOid));
        
        PoHeaderHolder header = poHeaderService.selectPoHeaderByKey(msg.getDocOid());
        header.setTotalCost(matching.getPoAmt());
        
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
        List<PoDetailHolder> details = poDetailService
            .selectPoDetailsByPoOid(msg.getDocOid());
        List<PoHeaderExtendedHolder> headerExtendeds = poHeaderExtendedService
            .selectHeaderExtendedByKey(msg.getDocOid());

        List<PoDetailExtendedHolder> detailExtendeds = poDetailExtendedService
            .selectDetailExtendedByKey(msg.getDocOid());
        
        List<PoLocationHolder> locations = poLocationService
            .selectLocationsByPoOidAndStoreCode(msg.getDocOid(), matching.getPoStoreCode());
       
        List<PoLocationDetailHolder> locationDetails = new ArrayList<PoLocationDetailHolder>();
        
        Map<Integer, PoDetailHolder> detailMap = new HashMap<Integer, PoDetailHolder>();
        
        for (PoDetailHolder detail : details)
        {
            detailMap.put(detail.getLineSeqNo(), detail);
        }
            
        
        for (PoLocationHolder location : locations)
        {
            locationDetails.addAll(poLocationDetailService
            .selectPoLocationDetailByPoOidAndLocLineSeqNo(msg.getDocOid(), location.getLineSeqNo()));
        }
        
        retrieveDetailData(details, locationDetails, detailMap, locations);
        
        List<PoLocationDetailExtendedHolder> poLocDetailExtendeds = poLocationDetailExtendedService
            .selectPoLocationDetailExtendedsByPoOid(msg.getDocOid());
    
        ReportEngineParameter<PoHolder> reportEngineParameter = new ReportEngineParameter<PoHolder>();

        PoHolder data = new PoHolder();

        data.setPoHeader(header);
        data.setDetails(details);
        data.setHeaderExtendeds(headerExtendeds);
        data.setDetailExtendeds(detailExtendeds);
        data.setLocations(locations);
        data.setLocationDetails(locationDetails);
        data.setPoLocDetailExtendeds(poLocDetailExtendeds);
        
        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    
    private DefaultPoReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.PO.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultPoReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.PO
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultPoReportEngine.class);
    }
    
    
    private void retrieveDetailData(List<PoDetailHolder> details,
        List<PoLocationDetailHolder> locationDetails,
        Map<Integer, PoDetailHolder> detailMap, List<PoLocationHolder> locations)
    {
        details.clear();
        for (PoLocationDetailHolder locationDetail : locationDetails)
        {
            if (detailMap.containsKey(locationDetail.getDetailLineSeqNo()))
            {
               details.add(detailMap.get(locationDetail.getDetailLineSeqNo()));
            }
        }
        
        Map<Integer, List<PoLocationDetailHolder>> poLocationDetailMap = new HashMap<Integer, List<PoLocationDetailHolder>>();
        
        for (PoLocationDetailHolder locationDetail : locationDetails)
        {
            if(poLocationDetailMap.containsKey(locationDetail
                .getDetailLineSeqNo()))
            {
                poLocationDetailMap.get(locationDetail.getDetailLineSeqNo())
                    .add(locationDetail);
            }
            else
            {
                List<PoLocationDetailHolder> poLocationDetailList = new ArrayList<PoLocationDetailHolder>();
                poLocationDetailList.add(locationDetail);
                poLocationDetailMap.put(locationDetail.getDetailLineSeqNo(),
                    poLocationDetailList);
            }
        }
        
        
        Map<Integer, PoLocationHolder> poLocationMap = new HashMap<Integer, PoLocationHolder>();
        for (PoLocationHolder poLocation : locations)
        {
           poLocationMap.put(poLocation.getLineSeqNo(), poLocation);
        }
        
        
        for (PoDetailHolder detail : details)
        {
            if(poLocationDetailMap.containsKey(detail.getLineSeqNo()))
            {
                for(PoLocationDetailHolder locationDetail : poLocationDetailMap.get(detail.getLineSeqNo()))
                { //corresponding plural subDateSource record

                    detail.setOrderQty(locationDetail.getLocationShipQty());
                    
                    BigDecimal itemCost = locationDetail.getLocationShipQty()
                        .multiply(
                            detail.getOrderBaseUnit().equals("P") ? detail
                                .getPackCost() : detail.getUnitCost());
                    
                    detail.setItemCost(itemCost);
                }
            }
        }
    }
    
    
    private void approveMatchingRecord(PoInvGrnDnMatchingHolder holder) throws Exception
    {
        this.checkInvFileExist(holder);
        poInvGrnDnMatchingService.changeInvDateToFirstGrnDate(holder);
        PoInvGrnDnMatchingHolder newHolder = new PoInvGrnDnMatchingHolder();
        BeanUtils.copyProperties(holder, newHolder);
        newHolder.setInvStatus(PoInvGrnDnMatchingInvStatus.SYS_APPROVED);
        newHolder.setInvStatusActionDate(new Date());
        newHolder.setInvStatusActionBy(getProfileOfCurrentUser().getLoginId());
        poInvGrnDnMatchingService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), holder, newHolder);
        poInvGrnDnMatchingService.moveFile(holder);
    }

    
    private void closeMatchingRecord(PoInvGrnDnMatchingHolder holder) throws Exception
    {
        PoInvGrnDnMatchingHolder newObj = new PoInvGrnDnMatchingHolder();
        BeanUtils.copyProperties(holder, newObj);
        newObj.setClosedDate(new Date());
        newObj.setClosed(true);
        newObj.setClosedBy(SYSTEM);
        newObj.setAllEmptyStringToNull();
        poInvGrnDnMatchingService.auditUpdateByPrimaryKey(this.getCommonParameter(), holder, newObj);
        
        if (PoInvGrnDnMatchingBuyerStatus.ACCEPTED.equals(holder.getBuyerStatus()))
        {
            //auto approve invoice
            boolean autoApproveClosedAcceptedRecord = businessRuleService.isMatchingAutoApproveClosedAcceptedRecord(newObj.getBuyerOid());
            if (autoApproveClosedAcceptedRecord && (PoInvGrnDnMatchingBuyerStatus.ACCEPTED.equals(newObj.getBuyerStatus()) || holder.getRevised()))
            {
                this.approveMatchingRecord(newObj);
            }
        }
    }
    
    
    private void checkInvFileExist(PoInvGrnDnMatchingHolder holder) throws Exception
    {
        BuyerHolder buyer = buyerService.selectBuyerByKey(holder.getBuyerOid());
        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(holder.getInvOid());
        File source = new File(mboxUtil.getBuyerMailBox(buyer.getMboxId()) + PS + "tmp" + PS + msg.getExchangeFilename());
        if (!source.exists())
        {
            throw new Exception("Invoice file is not existent in tmp folder");
        }
    }
    
    
    private PoInvGrnDnMatchingExHolder initIndexParameters(PoInvGrnDnMatchingExHolder param)
    {
        return null;
    }
    
    public PoInvGrnDnMatchingExHolder getParam()
    {
        return param;
    }


    public void setParam(PoInvGrnDnMatchingExHolder param)
    {
        this.param = param;
    }


    public Map<String, String> getMatchingStatus()
    {
        return matchingStatus;
    }


    public void setMatchingStatus(Map<String, String> matchingStatus)
    {
        this.matchingStatus = matchingStatus;
    }


    public Map<String, String> getInvStatus()
    {
        return invStatus;
    }


    public void setInvStatus(Map<String, String> invStatus)
    {
        this.invStatus = invStatus;
    }


    public String getResult()
    {
        return result;
    }


    public void setResult(String result)
    {
        this.result = result;
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


    public boolean isSelectAll()
    {
        return selectAll;
    }


    public void setSelectAll(boolean selectAll)
    {
        this.selectAll = selectAll;
    }

    public List<FavouriteListExHolder> getFavouriteLists()
    {
        return favouriteLists;
    }

    public void setFavouriteLists(List<FavouriteListExHolder> favouriteLists)
    {
        this.favouriteLists = favouriteLists;
    }

    public Map<String, String> getSupplierStatus()
    {
        return supplierStatus;
    }

    public void setSupplierStatus(Map<String, String> supplierStatus)
    {
        this.supplierStatus = supplierStatus;
    }

    public Map<String, String> getBuyerStatus()
    {
        return buyerStatus;
    }

    public void setBuyerStatus(Map<String, String> buyerStatus)
    {
        this.buyerStatus = buyerStatus;
    }

    public Map<String, String> getPriceStatus()
    {
        return priceStatus;
    }

    public void setPriceStatus(Map<String, String> priceStatus)
    {
        this.priceStatus = priceStatus;
    }

    public Map<String, String> getQtyStatus()
    {
        return qtyStatus;
    }

    public void setQtyStatus(Map<String, String> qtyStatus)
    {
        this.qtyStatus = qtyStatus;
    }

    public Map<Boolean, String> getClosedStatus()
    {
        return closedStatus;
    }

    public List<PoInvGrnDnMatchingDetailExHolder> getDetails()
    {
        return details;
    }

    public void setDetails(List<PoInvGrnDnMatchingDetailExHolder> details)
    {
        this.details = details;
    }

    public Map<Boolean, String> getRevisedStatus()
    {
        return revisedStatus;
    }

    public void setRevisedStatus(Map<Boolean, String> revisedStatus)
    {
        this.revisedStatus = revisedStatus;
    }
    
}
