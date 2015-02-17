package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.CountryHolder;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerStoreExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;
import com.pracbiz.b2bportal.core.service.AllowedAccessCompanyService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.CountryService;
import com.pracbiz.b2bportal.core.service.GroupService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserTypeService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreAction extends ProjectBaseAction
{
    private static final String LOCKED_ASSIGNED_USER_OIDS = "LOCKED_ASSIGNED_USER_OIDS";
    private static final Logger log = LoggerFactory.getLogger(BuyerStoreAction.class);
    private static final long serialVersionUID = -2617224849481154512L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_BUYERSTORE = "SEARCH_PARAMETER_BUYERSTORE";
    private static final String SESSION_OID_PARAMETER = "session.parameter.BuyerStoreAction.selectedOid";

    private BuyerStoreExHolder param;
    private List<? extends Object> assignedUsers;
    private List<BuyerStoreHolder> storeList;

    private List<? extends Object> availableUsers;

    private static final Map<String, String> sortMap;
    private List<BuyerHolder> buyers;

    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("storeOid", "S.STORE_OID");
        sortMap.put("storeName", "S.STORE_NAME");
        sortMap.put("storeCode", "S.STORE_CODE");
        sortMap.put("areaCode", "A.AREA_CODE");
        sortMap.put("storeCtryName", "C.CTRY_DESC");
        sortMap.put("storeState", "S.STORE_STATE");
        sortMap.put("storeCity", "S.STORE_CITY");
    }

    @Autowired
    private transient BuyerStoreService buyerStoreService;
    @Autowired
    private transient BuyerStoreAreaService buyerStoreAreaService;
    @Autowired
    private transient CountryService countryService;
    @Autowired
    private transient UserProfileService userProfileService;
    @Autowired 
    private transient BuyerService buyerService;
    @Autowired
    private transient GroupService groupService;
    @Autowired
    private transient GroupTradingPartnerService groupTradingPartnerService;
    @Autowired
    private transient UserTypeService userTypeService;
    @Autowired
    private transient AllowedAccessCompanyService allowedAccessCompanyService;
    
    private transient InputStream rptResult;
    private String rptFileName;
    private String buyerOid;
    
    public BuyerStoreAction()
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_BUYERSTORE);
        try
        {
            param = (BuyerStoreExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_BUYERSTORE);
            
            //current is buyer type user.
            if (this.getProfileOfCurrentUser().getBuyerOid() != null)
            {
            	buyers = allowedAccessCompanyService.selectBuyerByUserOid(this.getProfileOfCurrentUser().getUserOid());
            	BuyerHolder buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            	buyers.add(buyer);
            }
            //current is supplier type user.
            if (this.getProfileOfCurrentUser().getSupplierOid() != null)
            {
                buyers = new ArrayList<BuyerHolder>();
                GroupHolder group = groupService.selectGroupByUserOid(this.getProfileOfCurrentUser().getUserOid());
                if (group == null)
                {
                    buyers = buyerService.selectBuyerBySupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                }
                else
                {
                    List<GroupTradingPartnerHolder> groupTpList = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(group.getGroupOid());
                    if (groupTpList != null && groupTpList.size() == 1 
                            && groupTpList.get(0).getTradingPartnerOid().compareTo(BigDecimal.valueOf(-1)) == 0)
                    {
                        buyers = buyerService.selectBuyerBySupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                    }
                    else
                    {
                        buyers = buyerService.selectBuyersByGroupOid(group.getGroupOid());
                    }
                }
            }
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String search()
    {
        if (null == param)
        {
            param = new BuyerStoreExHolder();
        }

        try
        {
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_BUYERSTORE, param);

        return SUCCESS;
    }


    public String data()
    {
        try
        {
            BuyerStoreExHolder searchParam = (BuyerStoreExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_BUYERSTORE);
            if (searchParam == null)
            {
                searchParam = new BuyerStoreExHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_BUYERSTORE, searchParam);
            }
            searchParam.setCurrentUserType(getUserTypeOfCurrentUser());
            searchParam.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
            initCompany(searchParam);
            this.obtainListRecordsOfPagination(buyerStoreService, searchParam,
                    sortMap, "storeOid", null);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }
    
    
    private void initCompany(BuyerStoreExHolder searchParam) throws Exception
    {
        //current is buyer type user or store type user.
        if (this.getProfileOfCurrentUser().getBuyerOid() != null)
        {
        	BuyerHolder buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
        	searchParam.setBuyerCode(buyer.getBuyerCode());
        }
        //current is supplier type user.
        else if (this.getProfileOfCurrentUser().getSupplierOid() != null)
        {
            if (searchParam.getBuyerCode() == null)
            {
                searchParam.setBuyerCode("-1");
            }
            List<BuyerHolder> buyerList = new ArrayList<BuyerHolder>();
            GroupHolder group = groupService.selectGroupByUserOid(this.getProfileOfCurrentUser().getUserOid());
           
            if (group == null)
            {
                buyerList = buyerService.selectBuyerBySupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
            }
            else
            {
                List<GroupTradingPartnerHolder> groupTpList = groupTradingPartnerService.selectGroupTradingPartnerByGroupOid(group.getGroupOid());
                if (groupTpList != null && groupTpList.size() == 1 
                        && groupTpList.get(0).getTradingPartnerOid().compareTo(BigDecimal.valueOf(-1)) == 0)
                {
                    buyerList = buyerService.selectBuyerBySupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
                }
                else
                {
                    buyerList = buyerService.selectBuyersByGroupOid(group.getGroupOid());
                }
            }
            if (!buyerList.isEmpty())
            {
                for (BuyerHolder buyer : buyerList)
                {
                    searchParam.addBuyerCode(buyer.getBuyerCode());
                }
            }
        }
    }


    public void validateViewStore()
    {
        try
        {
            boolean flag = this.hasFieldErrors();

            if (!flag && (param == null || param.getStoreOid() == null))
            {
                this.addActionError(this.getText("B2BPC2301"));
                flag = true;
            }
            if (!flag)
            {
                BuyerStoreHolder bs = buyerStoreService
                        .selectBuyerStoreByStoreOid(param.getStoreOid());

                if (bs == null)
                {
                    this.addActionError(this.getText("B2BPC2302"));
                    flag = true;
                }
                else
                {
                    BeanUtils.copyProperties(bs, param);
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }


    public String viewStore()
    {
        try
        {
            if (param.getAreaOid() != null)
            {
                BuyerStoreAreaHolder bsa = buyerStoreAreaService
                        .selectBuyerStoreAreaByKey(param.getAreaOid());
                if (bsa != null)
                {
                    param.setAreaCode(bsa.getAreaCode());
                    param.setAreaName(bsa.getAreaName());
                }
            }
            if (!StringUtils.isEmpty(param.getStoreCtryCode()))
            {
                CountryHolder ctry = countryService.selectByCtryCode(param
                        .getStoreCtryCode());
                param.setStoreCtryName(ctry.getCtryDesc());
            }
            List<BigDecimal> userTypes = initUserTypesBasedOnCurrentUser();
            assignedUsers = userProfileService
                    .selectUsersByStoreOidAndUserTypes(param.getStoreOid(),
                            userTypes);
            if (assignedUsers == null)
            {
                assignedUsers = new ArrayList<UserProfileHolder>();
            }
        }
        catch (Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public void validateViewArea()
    {
        try
        {
            boolean flag = this.hasFieldErrors();

            if (!flag && (param == null || param.getAreaOid() == null))
            {
                this.addActionError(this.getText("B2BPC2303"));
                flag = true;
            }
            if (!flag)
            {
                BuyerStoreAreaHolder bsa = buyerStoreAreaService
                        .selectBuyerStoreAreaByKey(param.getAreaOid());

                if (bsa == null)
                {
                    this.addActionError(this.getText("B2BPC2304"));
                    flag = true;
                }
                else
                {
                    param.setAreaCode(bsa.getAreaCode());
                    param.setAreaName(bsa.getAreaName());
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }


    public String viewArea()
    {
        try
        {
            storeList = buyerStoreService.selectBuyerStoresByAreaOid(param
                    .getAreaOid());
            if (storeList == null)
            {
                storeList = new ArrayList<BuyerStoreHolder>();
            }
            List<BigDecimal> userTypes = initUserTypesBasedOnCurrentUser();
            assignedUsers = userProfileService
                    .selectUsersByAreaOidAndUserTypes(param.getAreaOid(),
                            userTypes);
            if (assignedUsers == null)
            {
                assignedUsers = new ArrayList<UserProfileHolder>();
            }
        }
        catch (Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public String assignStore()
    {
        try
        {
            BuyerStoreHolder bs = buyerStoreService
                    .selectBuyerStoreByStoreOid(param.getStoreOid());
            BeanUtils.copyProperties(bs, param);

            if (param.getAreaOid() != null)
            {
                BuyerStoreAreaHolder bsa = buyerStoreAreaService
                        .selectBuyerStoreAreaByKey(param.getAreaOid());
                if (bsa != null)
                {
                    param.setAreaCode(bsa.getAreaCode());
                    param.setAreaName(bsa.getAreaName());
                }
            }
            if (!StringUtils.isEmpty(param.getStoreCtryCode()))
            {
                CountryHolder ctry = countryService.selectByCtryCode(param
                        .getStoreCtryCode());
                param.setStoreCtryName(ctry.getCtryDesc());
            }
            List<UserProfileExHolder> usersByTmpStore = userProfileService
                    .selectUsersByTmpStoreOidAndUserTypes(param.getStoreOid(),
                            initUserTypesBasedOnCurrentUser());
            List<UserProfileExHolder> usersByStore = userProfileService
                    .selectUsersByStoreOidAndUserTypes(param.getStoreOid(),
                            initUserTypesBasedOnCurrentUser());

            this.initAssignScreen(usersByTmpStore, usersByStore);
        }
        catch (Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public void validateSaveAssignStore()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            if (assignedUsers == null)
            {
                assignedUsers = new ArrayList<Object>();
            }

            if (flag)
            {
                BuyerStoreHolder bs = buyerStoreService
                        .selectBuyerStoreByStoreOid(param.getStoreOid());
                BeanUtils.copyProperties(bs, param);

                if (param.getAreaOid() != null)
                {
                    BuyerStoreAreaHolder bsa = buyerStoreAreaService
                            .selectBuyerStoreAreaByKey(param.getAreaOid());
                    if (bsa != null)
                    {
                        param.setAreaCode(bsa.getAreaCode());
                        param.setAreaName(bsa.getAreaName());
                    }
                }
                if (!StringUtils.isEmpty(param.getStoreCtryCode()))
                {
                    CountryHolder ctry = countryService.selectByCtryCode(param
                            .getStoreCtryCode());
                    param.setStoreCtryName(ctry.getCtryDesc());
                }
                List<BigDecimal> userTypes = initUserTypesBasedOnCurrentUser();
                List<UserProfileExHolder> usersByTmpStore = userProfileService
                        .selectUsersByTmpStoreOidAndUserTypes(
                                param.getStoreOid(), userTypes);
                List<UserProfileExHolder> usersByStore = userProfileService
                        .selectUsersByStoreOidAndUserTypes(param.getStoreOid(),
                                userTypes);
                this.initAssignScreen(usersByTmpStore, usersByStore);
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }


    public String saveAssignStore()
    {
        try
        {
            List<BigDecimal> newUserOidList = new ArrayList<BigDecimal>();
            List<BigDecimal> newUserOidTmpList = new ArrayList<BigDecimal>();
            List<BuyerStoreUserTmpHolder> addList = null;
            List<BuyerStoreUserTmpHolder> deleteList = new ArrayList<BuyerStoreUserTmpHolder>();
            for (Object o : assignedUsers)
            {
                newUserOidList.add(new BigDecimal(Double.valueOf(o.toString())));
            }
            
            List<UserProfileExHolder> oldBuyerUserList = userProfileService.selectUsersByStoreOidAndUserTypes(param.getStoreOid(),
                    initUserTypesBasedOnCurrentUser());
            if (oldBuyerUserList == null)
            {
                addList = this.initBuyerStoreUserTmpHolder(newUserOidList, param.getStoreOid());
            }
            else
            {
                List<BigDecimal> oldUserOidList = this.retriveUserOid(oldBuyerUserList);
                newUserOidTmpList.addAll(newUserOidList);
                newUserOidTmpList.removeAll(oldUserOidList);
                addList = this.initBuyerStoreUserTmpHolder(newUserOidTmpList, param.getStoreOid());
                oldUserOidList.removeAll(newUserOidList);
                deleteList = this.initBuyerStoreUserTmpHolder(oldUserOidList, param.getStoreOid());
            }
            buyerStoreService.assignUserToStore(this.getCommonParameter(), addList, deleteList);
            log.info(this.getText(
                    "B2BPC2307",
                    new String[] {param.getStoreCode(),
                        this.getLoginIdOfCurrentUser()}));
        }
        catch (Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public void validateAssignArea()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            BuyerStoreHolder bs = null;
            if (param.getStoreOid() != null)
            {
                bs = buyerStoreService
                        .selectBuyerStoreByStoreOid(param.getStoreOid());
            }
            
            if (!flag
                    && ((bs == null && param.getAreaOid() == null) || (bs != null && bs
                            .getAreaOid() == null)))
            {
                this.addActionError(this.getText("B2BPC2306"));
                flag = true;
            }
            if (!flag)
            {
                if (bs != null)
                {
                    BeanUtils.copyProperties(bs, param);
                }
            
                BuyerStoreAreaHolder bsa = buyerStoreAreaService
                        .selectBuyerStoreAreaByKey(param.getAreaOid());

                if (bsa == null)
                {
                    this.addActionError(this.getText("B2BPC2304"));
                    flag = true;
                }
                else
                {
                    param.setAreaCode(bsa.getAreaCode());
                    param.setAreaName(bsa.getAreaName());
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }


    public String assignArea()
    {
        try
        {
            storeList = buyerStoreService.selectBuyerStoresByAreaOid(param
                    .getAreaOid());
            if (storeList == null)
            {
                storeList = new ArrayList<BuyerStoreHolder>();
            }
            List<BigDecimal> userTypes = initUserTypesBasedOnCurrentUser();
            List<UserProfileExHolder> usersByTmpArea = userProfileService
                    .selectUsersByTmpAreaOidAndUserTypes(param.getAreaOid(), userTypes);
            List<UserProfileExHolder> usersByArea = userProfileService
                    .selectUsersByAreaOidAndUserTypes(param.getAreaOid(), userTypes);
            this.initAssignScreen(usersByTmpArea, usersByArea);
            
        }
        catch (Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }


    public void validateSaveAssignArea()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            if (assignedUsers == null)
            {
                assignedUsers = new ArrayList<Object>();
            }

            if (flag)
            {
                BuyerStoreAreaHolder bsa = buyerStoreAreaService
                        .selectBuyerStoreAreaByKey(param.getAreaOid());

                if (bsa == null)
                {
                    this.addActionError(this.getText("B2BPC2304"));
                    flag = true;
                }
                else
                {
                    param.setAreaCode(bsa.getAreaCode());
                    param.setAreaName(bsa.getAreaName());
                }
                storeList = buyerStoreService.selectBuyerStoresByAreaOid(param
                        .getAreaOid());
                if (storeList == null)
                {
                    storeList = new ArrayList<BuyerStoreHolder>();
                }
                
                List<BigDecimal> userTypes = initUserTypesBasedOnCurrentUser();
                List<UserProfileExHolder> usersByTmpArea = userProfileService
                        .selectUsersByTmpAreaOidAndUserTypes(param.getAreaOid(), userTypes);
                List<UserProfileExHolder> usersByArea = userProfileService
                        .selectUsersByAreaOidAndUserTypes(param.getAreaOid(), userTypes);

                this.initAssignScreen(usersByTmpArea, usersByArea);
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }
    
    
    public String saveAssignArea()
    {
        try
        {
            List<BigDecimal> newUserOidList = new ArrayList<BigDecimal>();
            List<BigDecimal> newUserOidTmpList = new ArrayList<BigDecimal>();
            List<BuyerStoreAreaUserTmpHolder> addList = null;
            List<BuyerStoreAreaUserTmpHolder> deleteList = new ArrayList<BuyerStoreAreaUserTmpHolder>();
            for (Object o : assignedUsers)
            {
                newUserOidList.add(new BigDecimal(Double.valueOf(o.toString())));
            }
            List<UserProfileExHolder> oldBuyerUserList = userProfileService.selectUsersByAreaOidAndUserTypes(param.getAreaOid(), 
                    this.initUserTypesBasedOnCurrentUser());
            if (oldBuyerUserList == null)
            {
                addList = this.initBuyerStoreAreaUserTmpHolder(newUserOidList, param.getAreaOid());
            }
            else
            {
                List<BigDecimal> oldUserOidList = this.retriveUserOid(oldBuyerUserList);
                newUserOidTmpList.addAll(newUserOidList);
                newUserOidTmpList.removeAll(oldUserOidList);
                addList = this.initBuyerStoreAreaUserTmpHolder(newUserOidTmpList, param.getAreaOid());
                oldUserOidList.removeAll(newUserOidList);
                deleteList = this.initBuyerStoreAreaUserTmpHolder(oldUserOidList, param.getAreaOid());
            }
            buyerStoreAreaService.assignUserToArea(this.getCommonParameter(), addList, deleteList);
            log.info(this.getText(
                    "B2BPC2308",
                    new String[] {param.getAreaCode(),
                        this.getLoginIdOfCurrentUser()}));
        }
        catch (Exception e)
        {
            handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String exportExcel()
    {
        InputStream is = null;
        BuyerHolder buyer = null;
        byte[] bytes = null;
        try
        {
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(2)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 )
            {
                buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            }
            
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(3)) == 0 
                || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(5)) == 0)
            {
                buyer = buyerService.selectBuyerByKey(new BigDecimal(buyerOid));
            }
            
            if (buyer == null)
            {
                throw new IllegalArgumentException();
            }
            
            bytes = buyerStoreService.exportExcel(buyer.getBuyerCode());
            if (bytes == null)
            {
                this.addActionError(this.getText("B2BPC2310", new String[]{buyer.getBuyerCode()}));
                this.init();
                return INPUT;
            }
            
            is = new ByteArrayInputStream(bytes);
            this.setRptResult(is);
            
            rptFileName = "report_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch(IOException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
        }
        return SUCCESS;
    }
    
    
    //*************************************************
    //private method
    //*************************************************
    private void initAssignScreen(List<UserProfileExHolder> usersByT,
            List<UserProfileExHolder> users) throws Exception
    {
        availableUsers = this.initAvailableUsers(usersByT, users);
        assignedUsers = this.initAssignedUsers(usersByT, users);
        if (assignedUsers == null || assignedUsers.isEmpty())
        {
            assignedUsers = new ArrayList<Object>();
        }
        else
        {
            List<UserProfileTmpExHolder> tmpList = new ArrayList<UserProfileTmpExHolder>();
            List<Object> assignedUserOid = this
                    .convertAssignedUsers(assignedUsers);
            for (Object obj : availableUsers)
            {
                UserProfileTmpExHolder user = (UserProfileTmpExHolder) obj;
                if (assignedUserOid.contains(user.getUserOid()))
                {
                    tmpList.add(user);
                }
            }
            availableUsers.removeAll(tmpList);
        }
    }


    private List<UserProfileTmpExHolder> initAvailableUsers(List<UserProfileExHolder> usersByT,
            List<UserProfileExHolder> users) throws Exception
    {
        List<UserProfileExHolder> availableUsers = userProfileService.selectUsersByBuyerAndUserTypes(
                this.getProfileOfCurrentUser().getBuyerOid(), initUserTypesBasedOnCurrentUser());
        List<UserProfileTmpExHolder> rlt = new ArrayList<UserProfileTmpExHolder>();
        
        Map<BigDecimal, UserProfileExHolder> usersByTmpStoreMap = convertUserByStoreToMap(usersByT);
        Map<BigDecimal, UserProfileExHolder> usersNoExistTmp = new HashMap<BigDecimal, UserProfileExHolder>();
        for (UserProfileExHolder userByStore : users)
        {
            if (usersByTmpStoreMap.containsKey(userByStore.getUserOid()))
            {
                continue;
            }
            usersNoExistTmp.put(userByStore.getUserOid(), userByStore);
        }
        for (UserProfileExHolder availableUser : availableUsers)
        {
            UserProfileTmpExHolder user = new UserProfileTmpExHolder();
            BeanUtils.copyProperties(availableUser, user);
            if (usersNoExistTmp.containsKey(user.getUserOid()))
            {
                user.setIsLocked(true);
            }
            rlt.add(user);
        }
        return rlt;
    }


    private List<UserProfileTmpExHolder> initAssignedUsers(
            List<UserProfileExHolder> usersByT, List<UserProfileExHolder> users)
            throws Exception
    {
        List<UserProfileTmpExHolder> rlt = new ArrayList<UserProfileTmpExHolder>();
        List<BigDecimal> lockedUserOids = new ArrayList<BigDecimal>();

        if (usersByT == null || usersByT.isEmpty())
        {
            return null;
        }
        if (users == null || users.isEmpty())
        {
            for (UserProfileHolder userByTmpStore : usersByT)
            {
                UserProfileTmpExHolder user = new UserProfileTmpExHolder();
                BeanUtils.copyProperties(userByTmpStore, user);
                user.setIsLocked(true);
                lockedUserOids.add(user.getUserOid());
                rlt.add(user);
            }
            return rlt;
        }
        Map<BigDecimal, UserProfileExHolder> usersByStoreMap = convertUserByStoreToMap(usersByT);
        for (UserProfileExHolder userByTmpStore : users)
        {
            UserProfileTmpExHolder user = new UserProfileTmpExHolder();
            BeanUtils.copyProperties(userByTmpStore, user);
            user.setUserTypeDesc(userByTmpStore.getUserTypeDesc());
            if (!usersByStoreMap.containsKey(user.getUserOid()) 
                    || user.getUserOid().equals(getProfileOfCurrentUser().getUserOid()))
            {
                user.setIsLocked(true);
                lockedUserOids.add(user.getUserOid());
            }
            rlt.add(user);
        }
        getSession().put(LOCKED_ASSIGNED_USER_OIDS, lockedUserOids);
        return rlt;
    }


    private List<BigDecimal> initUserTypesBasedOnCurrentUser() throws Exception
    {
        List<BigDecimal> rlt = new ArrayList<BigDecimal>();
        rlt.add(this.getUserTypeOfCurrentUser());
        List<UserTypeHolder> userTypes = userTypeService.selectPrivilegedSubUserTypesByUserType(this.getUserTypeOfCurrentUser());
        for (UserTypeHolder userType : userTypes)
        {
            rlt.add(userType.getUserTypeOid());
        }
        return rlt;
        
    }
    
    
    private Map<BigDecimal, UserProfileExHolder> convertUserByStoreToMap(
            List<UserProfileExHolder> users)
    {
        Map<BigDecimal, UserProfileExHolder> rlt = new HashMap<BigDecimal, UserProfileExHolder>();
        for (UserProfileExHolder user : users)
        {
            rlt.put(user.getUserOid(), user);
        }
        return rlt;
    }
    
    
    private List<BigDecimal> retriveUserOid(List<UserProfileExHolder> users)
    {
        List<BigDecimal> rlt = new ArrayList<BigDecimal>();
        for (UserProfileExHolder user : users)
        {
            rlt.add(user.getUserOid());
        }
        return rlt;
    }
    
    
    private List<BuyerStoreUserTmpHolder> initBuyerStoreUserTmpHolder(List<BigDecimal> userOids,BigDecimal storeOid)
    {
        List<BuyerStoreUserTmpHolder> result = new ArrayList<BuyerStoreUserTmpHolder>();
        if (userOids == null || userOids.isEmpty())
        {
            return result;
        }
        
        @SuppressWarnings("unchecked")
        List<BigDecimal> lockedUserOids = (List<BigDecimal>) getSession().get(LOCKED_ASSIGNED_USER_OIDS);
        for (BigDecimal userOid : userOids)
        {
            if (lockedUserOids.contains(userOid))
            {
                continue;
            }
            BuyerStoreUserTmpHolder bsu = new BuyerStoreUserTmpHolder();
            bsu.setUserOid(userOid);
            bsu.setStoreOid(storeOid);
            result.add(bsu);
        }
        return result;
    }
    
    
    private List<BuyerStoreAreaUserTmpHolder> initBuyerStoreAreaUserTmpHolder(List<BigDecimal> userOids,BigDecimal areaOid)
    {
        List<BuyerStoreAreaUserTmpHolder> result = new ArrayList<BuyerStoreAreaUserTmpHolder>();
        if (userOids == null || userOids.isEmpty())
        {
            return result;
        }
        
        @SuppressWarnings("unchecked")
        List<BigDecimal> lockedUserOids = (List<BigDecimal>) getSession().get(LOCKED_ASSIGNED_USER_OIDS);
        for (BigDecimal userOid : userOids)
        {
            if (lockedUserOids.contains(userOid))
            {
                continue;
            }
            BuyerStoreAreaUserTmpHolder bsu = new BuyerStoreAreaUserTmpHolder();
            bsu.setUserOid(userOid);
            bsu.setAreaOid(areaOid);
            result.add(bsu);
        }
        return result;
    }


    private List<Object> convertAssignedUsers(
            List<? extends Object> assignedUser)
    {
        List<Object> rlt = new ArrayList<Object>();
        for (Object obj : assignedUser)
        {
            UserProfileHolder user = (UserProfileHolder) obj;
            rlt.add(user.getUserOid());
        }
        return rlt;
    }


    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);

        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                new String[] { tickNo }));

        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

        msg.addMessageTarget(mt);
    }


    public BuyerStoreExHolder getParam()
    {
        return param;
    }


    public void setParam(BuyerStoreExHolder param)
    {
        this.param = param;
    }


    public List<? extends Object> getAssignedUsers()
    {
        return assignedUsers;
    }


    public void setAssignedUsers(List<? extends Object> assignedUsers)
    {
        this.assignedUsers = assignedUsers;
    }


    public List<? extends Object> getAvailableUsers()
    {
        return availableUsers;
    }


    public void setAvailableUsers(List<? extends Object> availableUsers)
    {
        this.availableUsers = availableUsers;
    }


    public List<BuyerStoreHolder> getStoreList()
    {
        return storeList;
    }


    public void setStoreList(List<BuyerStoreHolder> storeList)
    {
        this.storeList = storeList;
    }


    public List<BuyerHolder> getBuyers()
    {
        return buyers;
    }


    public void setBuyers(List<BuyerHolder> buyers)
    {
        this.buyers = buyers;
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


    public String getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(String buyerOid)
    {
        this.buyerOid = buyerOid;
    }

}
