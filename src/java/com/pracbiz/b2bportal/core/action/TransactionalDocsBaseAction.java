package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.SummaryPageAccessType;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupSupplierHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.FavouriteListExHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.FavouriteListService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupSupplierService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.GroupUserService;
import com.pracbiz.b2bportal.core.service.SummaryFieldService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.SupplierSharedService;
import com.pracbiz.b2bportal.core.service.ToolTipService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserClassService;
import com.pracbiz.b2bportal.core.service.UserSubclassService;

public abstract class TransactionalDocsBaseAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(TransactionalDocsBaseAction.class);
    
    private static final String BUYER_OID_LIST = "BuyerOidList";

    private static final String SUPPLIER_OID_LIST = "SupplierOidList";
    
    private static final long serialVersionUID = 196037708532816750L;

    @Autowired
    protected ToolTipService toolTipService;
    @Autowired
    protected SummaryFieldService summaryFieldService;
    @Autowired
    protected SupplierService supplierService;
    @Autowired
    protected BuyerService buyerService;
    @Autowired
    protected GroupUserService groupUserService;
    @Autowired 
    protected GroupSupplierService groupSupplierService;
    @Autowired 
    protected GroupTradingPartnerService groupTradingPartnerService;
    @Autowired
    protected TradingPartnerService tradingPartnerService;
    @Autowired
    protected SupplierSharedService supplierSharedService;
    @Autowired
    protected BuyerStoreAreaService buyerStoreAreaService;
    @Autowired 
    protected GroupService groupService;
    @Autowired 
    private transient FavouriteListService favouriteListService;
    @Autowired 
    private transient BuyerStoreUserService buyerStoreUserService;
    @Autowired
    private transient UserClassService userClassService;
    @Autowired
    private transient UserSubclassService userSubclassService;
    @Autowired
    private transient AllowedAccessCompanyService allowedAccessCompanyService;

    protected List<? extends Object> buyers;
    protected List<? extends Object> suppliers;
    protected Map<String, String> docTypes;
    protected Map<String, String> docStatuses;
    protected Map<String, String> readStatus;
    protected List<FavouriteListExHolder> favouriteLists;
    protected String pageId;

    protected void obtainListRecordsOfPagination(PaginatingService<?> service_,
        MsgTransactionsExHolder param, String moduleKey) throws Exception
    {
        String accessType = getAccessType();
        Map<String, String> sortMap = summaryFieldService
            .selectSortFieldByPageIdAndAccessType(pageId, accessType, this);
        super.obtainListRecordsOfPagination(service_, param, sortMap, "docOid", moduleKey);
    }
    

    protected void handleException(Exception e)
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
    
    
    protected void initSearchCriteria() throws Exception
    {
        if (this.isCurrentUserSupplierSide())
            suppliers = supplierService.selectGrantSuppliersBySupplierSideUser(getProfileOfCurrentUser());
        
        buyers = buyerService.selectAvailableBuyersByUserOid(getProfileOfCurrentUser());
        
        favouriteLists = favouriteListService.selectFavouriteListByUserOid(getProfileOfCurrentUser().getUserOid());
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
    
    protected void initBuyerAndSuppGroup(MsgTransactionsExHolder param)throws Exception
    {
        if(!(((BigDecimal.valueOf(2).compareTo(this.getUserTypeOfCurrentUser())== 0 
                || BigDecimal.valueOf(4).compareTo(this.getUserTypeOfCurrentUser())== 0) 
                && this.getSession().get(SUPPLIER_OID_LIST) == null)
            || ((BigDecimal.valueOf(3).compareTo(this.getUserTypeOfCurrentUser())== 0 
                    || BigDecimal.valueOf(5).compareTo(this.getUserTypeOfCurrentUser())== 0) 
                    && this.getSession().get(BUYER_OID_LIST) == null)))
        {
            return;
        }
        UserProfileHolder currentUser = this.getProfileOfCurrentUser();

        List<BigDecimal> groupOidList = null;
        List<BigDecimal> tradingPartnerOidList = null;
        List<BigDecimal> supplierOidList = null;
        List<BigDecimal> buyerOidList = null;
        List<GroupUserHolder> groups = null;
        List<GroupSupplierHolder> groupSuppList = null;
        
        //current user is buyer type user.
        if(null != currentUser.getBuyerOid())
        {
            supplierOidList = new ArrayList<BigDecimal>();
            groups = groupUserService.selectGroupUserByUserOid(currentUser.getUserOid());
            
            if(null == groups || groups.isEmpty()) //get group oid lists.
            {
                supplierOidList = null;
            }
            else
            {
                groupOidList = this.getGroupOidList(groups);
                //get supplier oid list.
                groupSuppList = this.getGroupSuppList(groupOidList);
                supplierOidList = this.getSupplierOidList(groupSuppList);
            }
        }

        //current user is supplier type user.
        if(null != currentUser.getSupplierOid())
        {
            buyerOidList = new ArrayList<BigDecimal>();
            groups = groupUserService.selectGroupUserByUserOid(currentUser.getUserOid());
            
            if(null == groups || groups.isEmpty()) //get group oid lists.
            {
                buyerOidList = null;
            }
            else
            {
                groupOidList = this.getGroupOidList(groups);
                //get tp oid list.
                tradingPartnerOidList = this.getTradingPartnerOidList(groupOidList);
                //get supplier oid list
                buyerOidList = this.getBuyerOidList(tradingPartnerOidList);
            }
        }
        //save into a session
        this.getSession().put(SUPPLIER_OID_LIST, supplierOidList);
        this.getSession().put(BUYER_OID_LIST, buyerOidList);
    }
    
    
    
    protected void initCurrentUserSearchParam(MsgTransactionsExHolder searchParam)throws Exception
    {
        searchParam.setCurrentUserType(this.getUserTypeOfCurrentUser());
        searchParam.setCurrentUserOid(this.getProfileOfCurrentUser().getUserOid());
        searchParam.setCurrentUserBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        searchParam.setCurrentUserSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
        searchParam.setFullClassPriv(false);
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
                
                UserClassHolder uc = userClassService.selectByUserOidAndClassOid(searchParam.getCurrentUserOid(), BigDecimal.valueOf(-1));
                UserSubclassHolder us = userSubclassService.selectByUserOidAndSubclassOid(searchParam.getCurrentUserOid(), BigDecimal.valueOf(-1));
                
                if (uc != null || us != null)
                {
                    searchParam.setFullClassPriv(true);
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
            
            List<AllowedAccessCompanyHolder> allowedBuyerList = allowedAccessCompanyService.selectByUserOid(this.getProfileOfCurrentUser().getUserOid());
            
            if (allowedBuyerList != null && !allowedBuyerList.isEmpty())
            {
                for (AllowedAccessCompanyHolder allowedBuyer : allowedBuyerList)
                {
                    searchParam.addAccessCompanyOids(allowedBuyer.getCompanyOid());
                }
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
    
    private List<BigDecimal> getGroupOidList(List<GroupUserHolder> groups)
    {
        List<BigDecimal> groupOidList = new ArrayList<BigDecimal>();
        
        for(GroupUserHolder groupsList : groups)
        {
            groupOidList.add(groupsList.getGroupOid());
        }
        return groupOidList;
    } 
    
    private List<GroupSupplierHolder> getGroupSuppList(List<BigDecimal> groupOidList)throws Exception
    {
        List<GroupSupplierHolder> groupSuppList = null;
        //get supplier oid list.
        if(null != groupOidList && !groupOidList.isEmpty())
        {
            groupSuppList = new ArrayList<GroupSupplierHolder>();
            for(int i = 0; i < groupOidList.size(); i++)
            {
                List<GroupSupplierHolder> groupSuppList2 = groupSupplierService
                    .selectGroupSupplierByGroupOid(groupOidList.get(i));
                
                groupSuppList.addAll(groupSuppList2);
            }
        }
        return groupSuppList;
    }
    
    private List<BigDecimal> getSupplierOidList(List<GroupSupplierHolder> groupSuppList)throws Exception
    {
        List<BigDecimal> supplierOidList = null;
        if(null != groupSuppList && !groupSuppList.isEmpty())
        {
            supplierOidList = new ArrayList<BigDecimal>();
            for(GroupSupplierHolder groupSupp : groupSuppList)
            {
                if(supplierOidList.contains(groupSupp.getSupplierOid()))
                {
                    continue;
                }
                supplierOidList.add(groupSupp.getSupplierOid());
            }
        }
        return supplierOidList;
    }
    
    private List<BigDecimal> getTradingPartnerOidList(List<BigDecimal> groupOidList) throws Exception
    {
        List<GroupTradingPartnerHolder> groupTpList = null;
        List<BigDecimal> tradingPartnerOidList = null;
        if(null != groupOidList && !groupOidList.isEmpty())
        {
            for(int i = 0; i < groupOidList.size(); i++)
            {
                groupTpList = groupTradingPartnerService
                    .selectGroupTradingPartnerByGroupOid(groupOidList
                        .get(i));

                if(null != groupTpList && !groupTpList.isEmpty())
                {
                    tradingPartnerOidList = new ArrayList<BigDecimal>();
                    for(GroupTradingPartnerHolder groupTp : groupTpList)
                    {
                        if(tradingPartnerOidList.contains(groupTp
                            .getTradingPartnerOid()))
                        {
                            continue;
                        }
                        tradingPartnerOidList.add(groupTp
                            .getTradingPartnerOid());
                    }
                }
            }
        }
        return tradingPartnerOidList;
    }
    
    private List<BigDecimal> getBuyerOidList(List<BigDecimal> tradingPartnerOidList) throws Exception
    {
        List<TradingPartnerHolder> tpList = null;
        List<BigDecimal> buyerOidList = null;
        if(null != tradingPartnerOidList
            && !tradingPartnerOidList.isEmpty())
        {
            tpList = tradingPartnerService
                .selectTradingPartnerByTradingPartnerOids(tradingPartnerOidList);

            if(null != tpList && !tpList.isEmpty())
            {
                buyerOidList = new ArrayList<BigDecimal>();
                for(TradingPartnerHolder tradingPartner : tpList)
                {
                    buyerOidList.add(tradingPartner.getBuyerOid());
                }
            }
        }
        return buyerOidList;
    } 
    
    
    public List<? extends Object> getBuyers()
    {
        return buyers;
    }

    public void setBuyers(List<? extends Object> buyers)
    {
        this.buyers = buyers;
    }

    public List<? extends Object> getSuppliers()
    {
        return suppliers;
    }

    public void setSuppliers(List<? extends Object> suppliers)
    {
        this.suppliers = suppliers;
    }

    public Map<String, String> getDocTypes()
    {
        return docTypes;
    }

    public void setDocTypes(Map<String, String> docTypes)
    {
        this.docTypes = docTypes;
    }

    public Map<String, String> getDocStatuses()
    {
        return docStatuses;
    }

    public void setDocStatuses(Map<String, String> docStatuses)
    {
        this.docStatuses = docStatuses;
    }

    public String getPageId()
    {
        return pageId;
    }

    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }


    public Map<String, String> getReadStatus()
    {
        return readStatus;
    }


    public void setReadStatus(Map<String, String> readStatus)
    {
        this.readStatus = readStatus;
    }


    public List<FavouriteListExHolder> getFavouriteLists()
    {
        return favouriteLists;
    }


    public void setFavouriteLists(List<FavouriteListExHolder> favouriteLists)
    {
        this.favouriteLists = favouriteLists;
    }

}
