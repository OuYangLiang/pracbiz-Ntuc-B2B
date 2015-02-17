package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.holder.SupplierSharedHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.SupplierSharedService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class ProjectBaseAction extends BaseAction implements CoreCommonConstants
{
    private static final long serialVersionUID = -1887138507672768142L;
    
    protected static final String BACK_TO_LIST = "button.back.to.list";
    protected static final String INIT = "init";
    @Autowired private transient BuyerStoreService buyerStoreService;
    @Autowired private transient BuyerStoreAreaUserService buyerStoreAreaUserService;
    @Autowired private transient BuyerStoreUserService buyerStoreUserService;
    @Autowired private transient BuyerService buyerService;
    @Autowired private transient BuyerStoreAreaService buyerStoreAreaService;

    protected String getLoginIdOfCurrentUser()
    {
        return ((UserProfileHolder) this.getSession().get(SESSION_KEY_USER))
                .getLoginId();
    }


    protected UserProfileHolder getProfileOfCurrentUser()
    {
        return (UserProfileHolder) this.getSession().get(SESSION_KEY_USER);
    }


    protected BigDecimal getUserTypeOfCurrentUser()
    {
        return getProfileOfCurrentUser().getUserType();
    }
    
    
    protected boolean isCurrentUserInit()
    {
        return getProfileOfCurrentUser().getInit();
    }
    
    
    protected boolean isCurrentUserBuyerAdmin()
    {
        return BigDecimal.valueOf(2).equals(getUserTypeOfCurrentUser());
    }
    
    
    protected boolean isCurrentUserBuyerUser()
    {
        return BigDecimal.valueOf(4).equals(getUserTypeOfCurrentUser());
    }
    
    
    protected boolean isCurrentUserStoreAdmin()
    {
        return BigDecimal.valueOf(6).equals(getUserTypeOfCurrentUser());
    }
    
    
    protected boolean isCurrentUserStoreUser()
    {
        return BigDecimal.valueOf(7).equals(getUserTypeOfCurrentUser());
    }
    
    
    protected boolean isCurrentUserSupplierAdmin()
    {
        return BigDecimal.valueOf(3).equals(getUserTypeOfCurrentUser());
    }
    
    
    protected boolean isCurrentUserSupplierUser()
    {
        return BigDecimal.valueOf(5).equals(getUserTypeOfCurrentUser());
    }
    
    
    protected boolean isCurrentUserBuyerSide()
    {
        return this.isCurrentUserBuyerAdmin() ||
                this.isCurrentUserBuyerUser() ||
                this.isCurrentUserStoreAdmin()||
                this.isCurrentUserStoreUser();
    }
    
    
    protected boolean isCurrentUserSupplierSide()
    {
        return this.isCurrentUserSupplierAdmin() || this.isCurrentUserSupplierUser();
    }


    protected CommonParameterHolder getCommonParameter()
    {
        return (CommonParameterHolder) this.getSession().get(SESSION_KEY_COMMON_PARAM);
    }
    
    
    protected List<String> getBuyerStoreCodeAccess() throws Exception
    {
        UserProfileHolder user = this.getProfileOfCurrentUser();
        if (user == null || user.getBuyerOid() == null)
        {
            return new ArrayList<String>();
        }
        List<String> storeCodeList = new ArrayList<String>();
        storeCodeList.add("");
        List<BuyerStoreHolder> storeList = this.getBuyerStoreAccess();
        if (storeList == null || storeList.isEmpty())
        {
            return storeCodeList;
        }
        for (BuyerStoreHolder store : storeList)
        {
            if (storeCodeList.contains(store.getStoreCode()))
            {
                continue;
            }
            storeCodeList.add(store.getStoreCode());
        }
        return storeCodeList;
    }
    
    
    protected List<BuyerStoreHolder> getBuyerStoreAccess() throws Exception
    {
        @SuppressWarnings("unchecked")
        List<BuyerStoreHolder> result = (List<BuyerStoreHolder>) this.getSession().get(BUYER_STORE_ACCESS_LIST);
        
        if (result != null)
        {
            return result;
        }
        result = new ArrayList<BuyerStoreHolder>();
        
        UserProfileHolder user = this.getProfileOfCurrentUser();
        boolean isBuyer = user.getUserType().equals(BigDecimal.valueOf(2)) || user.getUserType().equals(BigDecimal.valueOf(4));
        boolean isStore = user.getUserType().equals(BigDecimal.valueOf(6)) || user.getUserType().equals(BigDecimal.valueOf(7));
        boolean isSupplier = user.getUserType().equals(BigDecimal.valueOf(3)) || user.getUserType().equals(BigDecimal.valueOf(5));
        
        if (isBuyer || isStore)
        {
            List<String> storeCodeList = new ArrayList<String>();
            storeCodeList.add("");
            
            BuyerHolder buyer = buyerService.selectBuyerByKey(user.getBuyerOid());
            
            List<BuyerStoreAreaUserHolder> buyerStoreAreaUsers = buyerStoreAreaUserService.selectAreaUserByUserOid(user.getUserOid());
            if (buyerStoreAreaUsers != null && !buyerStoreAreaUsers.isEmpty())
            {
                boolean allArea = false;
                for (BuyerStoreAreaUserHolder buyerStoreAreaUser : buyerStoreAreaUsers)
                {
                    if (buyerStoreAreaUser.getAreaOid().equals(BigDecimal.valueOf(-2)))
                    {
                        allArea = true;
                        break;
                    }
                }
                if (allArea)
                {
                    List<BuyerStoreAreaHolder> buyerStoreAreas = buyerStoreAreaService.selectBuyerStoreAreaByBuyer(buyer.getBuyerCode());
                    if (buyerStoreAreas != null && !buyerStoreAreas.isEmpty())
                    {
                        for (BuyerStoreAreaHolder buyerStoreArea : buyerStoreAreas)
                        {
                            List<BuyerStoreHolder> buyerStores = buyerStoreService.selectBuyerStoresByAreaOid(buyerStoreArea.getAreaOid());
                            for (BuyerStoreHolder buyerStore : buyerStores)
                            {
                                if (!storeCodeList.contains(buyerStore.getStoreCode()))
                                {
                                    storeCodeList.add(buyerStore.getStoreCode());
                                    result.add(buyerStore);
                                }
                            }
                        }
                    }
                }
                else
                {
                    for (BuyerStoreAreaUserHolder buyerStoreAreaUser : buyerStoreAreaUsers)
                    {
                        List<BuyerStoreHolder> buyerStores = buyerStoreService.selectBuyerStoresByAreaOid(buyerStoreAreaUser.getAreaOid());
                        if (buyerStores == null || buyerStores.isEmpty())
                        {
                            continue;
                        }
                        for (BuyerStoreHolder buyerStore : buyerStores)
                        {
                            if (!storeCodeList.contains(buyerStore.getStoreCode()))
                            {
                                storeCodeList.add(buyerStore.getStoreCode());
                                result.add(buyerStore);
                            }
                        }
                    }
                }
            }
            
            List<BuyerStoreUserHolder> buyerStoreUsers = buyerStoreUserService.selectStoreUserByUserOid(user.getUserOid());
            boolean selectAllStore = false;
            boolean selectAllAreaHouse = false;
            if (buyerStoreUsers != null && !buyerStoreUsers.isEmpty())
            {
                for (BuyerStoreUserHolder buyerStoreUser : buyerStoreUsers)
                {
                    if (buyerStoreUser.getStoreOid().compareTo(BigDecimal.valueOf(-3)) == 0)
                    {
                        selectAllStore = true;
                        continue;
                    }
                    if (buyerStoreUser.getStoreOid().compareTo(BigDecimal.valueOf(-4)) == 0)
                    {
                        selectAllAreaHouse = true;
                        continue;
                    }
                    BuyerStoreHolder buyerStore = buyerStoreService.selectBuyerStoreByStoreOid(buyerStoreUser.getStoreOid());
                    if (buyerStore == null)
                    {
                        continue;
                    }
                    if (!storeCodeList.contains(buyerStore.getStoreCode()))
                    {
                        storeCodeList.add(buyerStore.getStoreCode());
                        result.add(buyerStore);
                    }
                }
            }
            
            if (selectAllStore)
            {
                List<BuyerStoreHolder> storeList = buyerStoreService.selectBuyerStoresByBuyerAndIsWareHouse(buyer.getBuyerCode(), false);
                for (BuyerStoreHolder buyerStore : storeList)
                {
                    if (!storeCodeList.contains(buyerStore.getStoreCode()))
                    {
                        storeCodeList.add(buyerStore.getStoreCode());
                        result.add(buyerStore);
                    }
                }
            }
            
            if (selectAllAreaHouse)
            {
                List<BuyerStoreHolder> storeList = buyerStoreService.selectBuyerStoresByBuyerAndIsWareHouse(buyer.getBuyerCode(), true);
                for (BuyerStoreHolder buyerStore : storeList)
                {
                    if (!storeCodeList.contains(buyerStore.getStoreCode()))
                    {
                        storeCodeList.add(buyerStore.getStoreCode());
                        result.add(buyerStore);
                    }
                }
            }
        }
        else if (isSupplier)
        {
            List<BuyerHolder> availableBuyerList = buyerService.selectAvailableBuyersByUserOid(getProfileOfCurrentUser());
            if (availableBuyerList != null && !availableBuyerList.isEmpty())
            {
                for (BuyerHolder buyer : availableBuyerList)
                {
                    result.addAll(buyerStoreService.selectBuyerStoresByBuyer(buyer.getBuyerCode()));
                }
            }
        }
        Collections.sort(result, new Comparator<BuyerStoreHolder>(){

            @Override
            public int compare(BuyerStoreHolder o1, BuyerStoreHolder o2)
            {
                return (o1.getStoreName() == null? "" : o1.getStoreName()).compareToIgnoreCase(o2.getStoreName()==null ? "" : o2.getStoreName());
            }
            
        });
        this.getSession().put(BUYER_STORE_ACCESS_LIST, result);
        return result;
    }

    
    protected List<BigDecimal> getSupplierSharedOids(SupplierSharedService supplierSharedService) throws Exception
    {
        List<BigDecimal> supplierSharedOids = new ArrayList<BigDecimal>();
        List<SupplierSharedHolder> supplierShareds = supplierSharedService.selectByGrantSupOid(this.getProfileOfCurrentUser().getSupplierOid());
        if (supplierShareds != null)
        {
            for(SupplierSharedHolder supplierShared : supplierShareds)
            {
                supplierSharedOids.add(supplierShared.getSelfSupOid());
            }
        }
        supplierSharedOids.add(this.getProfileOfCurrentUser().getSupplierOid());
        return supplierSharedOids;
    }

}
