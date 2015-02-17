package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.FavouriteListSupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.FavouriteListExHolder;
import com.pracbiz.b2bportal.core.service.FavouriteListService;
import com.pracbiz.b2bportal.core.service.FavouriteListSupplierService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.PasswordValidateHelper;

public class MyProfileAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(MyProfileAction.class);
    private static final long serialVersionUID = -1122781147023009848L;
    private static final String RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME = "&prime;";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_CHARACTER = "'";
    private static final String RIGHT_DOUBLE_QUOTATION_MARK = "\"";
    private UserProfileHolder userProfile;
    private UserProfileTmpHolder userProfileTmp;
    
    @Autowired private transient UserProfileService userProfileService;
    @Autowired private transient UserProfileTmpService userProfileTmpService;
    @Autowired private transient PasswordValidateHelper passwordValidateHelper;
    @Autowired private transient SupplierService supplierService;
    @Autowired private transient FavouriteListService favouriteListService;
    @Autowired private transient FavouriteListSupplierService favouriteListSupplierService;

    private String currentPwd;// current pwd
    private String newPwd;// new pwd
    private String confirmPwd;// confirm pwd
    private String availableSupplierList;
    
    private String isChangePwd;

    public MyProfileAction()
    {
        this.initMsg();
    }


    public String initEdit()
    {
        clearSearchParameter("LAST_UPDATE_DATE");
        try
        {
            UserProfileHolder sessionUser = (UserProfileHolder)getSession().get(SESSION_KEY_USER);
            if (userProfile == null)
            {
                userProfile = new UserProfileHolder();
            }
            BeanUtils.copyProperties(sessionUser, userProfile);
            
            if (userProfile.getUserType().equals(BigDecimal.valueOf(2)) || userProfile.getUserType().equals(BigDecimal.valueOf(4)))
            {
                List<SupplierHolder> allAvailableSuppliers = supplierService.selectAvailableSuppliersByUserOid(getProfileOfCurrentUser());
                List<FavouriteListExHolder> favouriteLists = favouriteListService.selectFavouriteListByUserOid(userProfile.getUserOid());
                
                if (favouriteLists == null)
                {
                    favouriteLists = new ArrayList<FavouriteListExHolder>();
                }
                else
                {
                    for (FavouriteListExHolder favouriteList : favouriteLists)
                    {
                        setValueForFavouriteListForEdit(allAvailableSuppliers, favouriteList);
                    }
                }
                userProfile.setFavouriteLists(favouriteLists);
                
                availableSupplierList = replaceSpecialCharactersForJson(JSONArray.fromObject(allAvailableSuppliers).toString());
            }
            else
            {
                userProfile.setFavouriteLists(new ArrayList<FavouriteListExHolder>());
            }
            
            
            UserProfileTmpHolder user = userProfileTmpService
                .selectUserProfileTmpByLoginId(userProfile.getLoginId());
            
            if (user.getUpdateDate() != null)
            {
                this.getSession().put("LAST_UPDATE_DATE", user.getUpdateDate());
            }

            if (userProfile.getSupplierOid() != null)
            {
                SupplierHolder supplier = supplierService.selectSupplierByKey(userProfile.getSupplierOid());
                userProfile.setLoginId(UserProfileAction.separateLoginId(supplier, userProfile.getLoginId()));
            }
            
            sortAvailableSupplierAndSelectedSupplier();
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));

            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText("button.back"));
            mt.setTargetURI("initEdit");

            msg.addMessageTarget(mt);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    private void sortAvailableSupplierAndSelectedSupplier()
    {
        if (userProfile.getFavouriteLists() != null && !userProfile.getFavouriteLists().isEmpty())
        {
            Comparator<SupplierHolder> comparator = new Comparator<SupplierHolder>()
            {
                @Override
                public int compare(SupplierHolder o1, SupplierHolder o2)
                {
                    return o1.getSupplierName().compareTo(o2.getSupplierName());
                }

            };
            
            
            for (FavouriteListExHolder favouriteList : userProfile.getFavouriteLists())
            {
                if (favouriteList.getAvailableSupplierList() != null && !favouriteList.getAvailableSupplierList().isEmpty())
                {
                    Collections.sort(favouriteList.getAvailableSupplierList(), comparator);
                }
                if (favouriteList.getSelectedSupplierList() != null && !favouriteList.getSelectedSupplierList().isEmpty())
                {
                    Collections.sort(favouriteList.getSelectedSupplierList(), comparator);
                }
            }
        }
    }
    
    
    private void setValueForFavouriteListForEdit(List<SupplierHolder> allAvailableSuppliers, FavouriteListExHolder favouriteList) throws Exception
    {
        List<FavouriteListSupplierHolder> favouriteListSuppliers = favouriteListSupplierService.selectFavouriteListSupplierByListOid(favouriteList.getListOid());
        List<BigDecimal> selectedSupplierOids = new ArrayList<BigDecimal>();
        if (favouriteListSuppliers != null)
        {
            for (FavouriteListSupplierHolder holder : favouriteListSuppliers)
            {
                selectedSupplierOids.add(holder.getSupplierOid());
            }
        }
        setValueForFavouriteListBySelectedSupplierOids(favouriteList, allAvailableSuppliers, selectedSupplierOids);
    }
    
    
    private void setValueForFavouriteListBySelectedSupplierOids(FavouriteListExHolder favouriteList, List<SupplierHolder> allAvailableSuppliers, List<BigDecimal> selectedSupplierOids) throws Exception
    {
        List<SupplierHolder> availableSupplierList = new ArrayList<SupplierHolder>();
        List<SupplierHolder> selectedSupplierList = new ArrayList<SupplierHolder>();
        
        if (allAvailableSuppliers != null && !allAvailableSuppliers.isEmpty())
        {
            if (selectedSupplierOids == null || selectedSupplierOids.isEmpty())
            {
                availableSupplierList = allAvailableSuppliers;
            }
            else
            {
                for (SupplierHolder holder : allAvailableSuppliers)
                {
                    if (selectedSupplierOids.contains(holder.getSupplierOid()))
                    {
                        selectedSupplierList.add(holder);
                    }
                }
                
                
                for (SupplierHolder holder : allAvailableSuppliers)
                {
                    if (selectedSupplierOids.contains(holder.getSupplierOid()))
                    {
                        continue;
                    }
                    availableSupplierList.add(holder);
                }
            }
        }
        
        favouriteList.setAvailableSupplierList(availableSupplierList);
        favouriteList.setSelectedSupplierList(selectedSupplierList);
    }
    

    public void validateSaveEdit()
    {
        boolean flag = this.hasErrors();

        try
        {
            userProfile.trimAllString();
            SupplierHolder supplier = null;
            if (getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)) || getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(5)))
            {
                supplier = supplierService.selectSupplierByKey(getProfileOfCurrentUser().getSupplierOid());
                userProfile.setLoginId(UserProfileAction.initLoginId(supplier, userProfile.getLoginId()));
            }
            UserProfileHolder oldUser = userProfileService
                .getUserProfileByLoginId(this.getLoginIdOfCurrentUser());
            
            // To check whether the new login id already exists in database.
            if(!flag
                && !oldUser.getLoginId().equalsIgnoreCase(
                    userProfile.getLoginId())
                && userProfileTmpService.isLoginIdExist(userProfile
                    .getLoginId()))
            {
                this.addActionError(getText("B2BPC0905", new String[]{userProfile.getLoginId()}));
                flag = true;
            }
            
            // pwd validation
            if(!flag && VALUE_YES.equalsIgnoreCase(isChangePwd))
            {
                if (currentPwd == null || "".equals(currentPwd.trim()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0918"));
                }

                if (newPwd == null || "".equals(newPwd.trim()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0919"));
                }

                if (confirmPwd == null || "".equals(confirmPwd.trim()))
                {
                    flag = true;
                    this.addActionError(getText("B2BPC0920"));
                }
                
                if(!flag
                    && this.passwordValidateHelper.validatePwd(this, oldUser,
                        newPwd))
                {
                        this.addActionError(this.passwordValidateHelper
                                .getErrorMsg());
                        flag = true;
                }
                
                if (!flag && !newPwd.trim().equals(confirmPwd))
                {
                    this.addActionError(getText("B2BPC0913"));
                    flag = true;
                }

                if(!flag
                    && !oldUser.getLoginPwd().equalsIgnoreCase(
                        EncodeUtil.getInstance().computePwd(currentPwd,
                            oldUser.getUserOid())))
                {

                    this.addActionError(getText("B2BPC0912"));
                    flag = true;

                }
            }

            if (!flag && (userProfile.getSalutation().length() > 20))
            {
                this.addActionError(getText("B2BPC0908"));
                flag = true;
            }

            if (!flag && (userProfile.getUserName().length() > 50))
            {
                this.addActionError(getText("B2BPC0907"));
                flag = true;
            }
            
            Date currentUpdateDate = oldUser.getUpdateDate();
            String currDate = currentUpdateDate == null ? "" : DateUtil
                .getInstance().convertDateToString(currentUpdateDate ,
                    "dd/MM/yyyy HH:mm:ss SSS");
            
            Date lastUpdateDate = (Date) this.getSession().get("LAST_UPDATE_DATE");
            String oldDate = lastUpdateDate == null ? "" : DateUtil
                .getInstance().convertDateToString(lastUpdateDate,
                    "dd/MM/yyyy HH:mm:ss SSS");
            
            if(!flag && !(currDate.equals(oldDate)))
            {
                this.addActionError(getText("B2BPC0921"));
                flag = true;
            }
            
            if (!flag)
            {
                List<FavouriteListExHolder> favouriteLists = userProfile.getFavouriteLists();
                if (favouriteLists != null)
                {
                    List<String> listCodes = new ArrayList<String>();
                    for (FavouriteListExHolder favouriteList : favouriteLists)
                    {
                        if (favouriteList == null)
                        {
                            continue;
                        }
                        if (favouriteList.getListCode() == null || favouriteList.getListCode().trim().isEmpty())
                        {
                            this.addActionError(getText("B2BPC0922"));
                            flag = true;
                        }
                        else
                        {
                            if (listCodes.contains(favouriteList.getListCode().trim()))
                            {
                                this.addActionError(getText("B2BPC0924"));
                                flag = true;
                            }
                            else
                            {
                                listCodes.add(favouriteList.getListCode().trim());
                            }
                        }
                        if (favouriteList.getSelectedSupplierOids() == null || favouriteList.getSelectedSupplierOids().isEmpty())
                        {
                            this.addActionError(getText("B2BPC0923"));
                            flag = true;
                        }
                        if (flag)
                        {
                            break;
                        }
                    }
                }
            }
            
            
            if (flag)
            {
                currentPwd = null;
                newPwd = null;
                confirmPwd = null;
                userProfile.setUserType(oldUser.getUserType());
                userProfile.setSupplierOid(oldUser.getSupplierOid());
                if (getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)) || getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(4)))
                {
                    List<SupplierHolder> allAvailableSuppliers = supplierService.selectAvailableSuppliersByUserOid(getProfileOfCurrentUser());
                    List<FavouriteListExHolder> favouriteLists = userProfile.getFavouriteLists();
                    if (favouriteLists == null)
                    {
                        userProfile.setFavouriteLists(new ArrayList<FavouriteListExHolder>());
                    }
                    else
                    {
                        List<FavouriteListExHolder> tmpFavouriteLists = new ArrayList<FavouriteListExHolder>();
                        for (FavouriteListExHolder favouriteList : favouriteLists)
                        {
                            if (favouriteList != null)
                            {
                                tmpFavouriteLists.add(favouriteList);
                            }
                        }
                        for (FavouriteListExHolder favouriteList : tmpFavouriteLists)
                        {
                            if (favouriteList == null)
                            {
                                continue;
                            }
                            setValueForFavouriteListBySelectedSupplierOids(favouriteList, allAvailableSuppliers, favouriteList.getSelectedSupplierOids());
                        }
                        userProfile.setFavouriteLists(tmpFavouriteLists);
                    }
                    availableSupplierList = replaceSpecialCharactersForJson(JSONArray.fromObject(allAvailableSuppliers).toString());
                }
                else
                {
                    userProfile.setFavouriteLists(new ArrayList<FavouriteListExHolder>());
                }
                if (getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)) || getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(5)))
                {
                    userProfile.setLoginId(UserProfileAction.separateLoginId(supplier, userProfile.getLoginId()));
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
        
        UserProfileHolder oldUser = this.getProfileOfCurrentUser();
        try
        {
            UserProfileTmpHolder newUser = new UserProfileTmpHolder();
            BeanUtils.copyProperties(oldUser, newUser);
            newUser.setLoginId(userProfile.getLoginId());
            newUser.setSalutation(userProfile.getSalutation());
            newUser.setUserName(userProfile.getUserName());
            newUser.setTel(userProfile.getTel());
            newUser.setMobile(userProfile.getMobile());
            newUser.setEmail(userProfile.getEmail());
            newUser.setUpdateBy(this.getLoginIdOfCurrentUser());
            newUser.setUpdateDate(new Date());
            if(VALUE_YES.equals(isChangePwd))
            {
                newUser.setLoginPwd(EncodeUtil.getInstance().computePwd(newPwd,
                        oldUser.getUserOid()));
            }
            newUser.setLastResetPwdDate(new Date());
            
            if (oldUser.getUserType().equals(BigDecimal.valueOf(2)) || oldUser.getUserType().equals(BigDecimal.valueOf(4)))
            {
                List<SupplierHolder> allAvailableSuppliers = supplierService.selectAvailableSuppliersByUserOid(getProfileOfCurrentUser());
                List<FavouriteListExHolder> favouriteLists = favouriteListService.selectFavouriteListByUserOid(oldUser.getUserOid());
                
                if (favouriteLists != null)
                {
                    for (FavouriteListExHolder favouriteList : favouriteLists)
                    {
                        setValueForFavouriteListForEdit(allAvailableSuppliers, favouriteList);
                        favouriteList.setAvailableSupplierList(null);
                    }
                }
                oldUser.setFavouriteLists(favouriteLists);
                
                
                favouriteLists = userProfile.getFavouriteLists();
                if (favouriteLists != null)
                {
                    for (FavouriteListExHolder favouriteList : favouriteLists)
                    {
                        if (favouriteList == null)
                        {
                            continue;
                        }
                        setValueForFavouriteListBySelectedSupplierOids(favouriteList, allAvailableSuppliers, favouriteList.getSelectedSupplierOids());
                        favouriteList.setAvailableSupplierList(null);
                        favouriteList.trimAllString();
                    }
                }
                newUser.setFavouriteLists(favouriteLists);
            }
            
            newUser.trimAllString();
            newUser.setAllEmptyStringToNull();
            userProfileService.updateMyProfile(this.getCommonParameter(), oldUser, newUser);
            
            MessageTargetHolder messageTarget1 = new MessageTargetHolder(
                    this.getText("button.back"), "initEdit");
            messageTarget1.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION,
                    com.pracbiz.b2bportal.base.util.CommonConstants.VALUE_YES);

            msg.setTitle(this.getText("myprofile.edit"));

            msg.saveSuccess(this.getText("B2BPC0902", new String[] {userProfile.getLoginId() }));
            msg.addMessageTarget(messageTarget1);
            
            getSession().put(SESSION_KEY_USER, newUser);

        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
            msg.saveError(this.getText("B2BPC0911",
                new String[] {"" + this.getLoginIdOfCurrentUser()}));
            
            MessageTargetHolder mt = new MessageTargetHolder("button.back", "initEdit");
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            
            msg.addMessageTarget(mt);
            
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    private String replaceSpecialCharactersForJson(String json)
    {
        if (json == null)
        {
            return null;
        }
        
        String result = StringUtils.replace(json, RIGHT_SINGLE_QUOTATION_MARK_CHARACTER,
            RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME);
        
        return StringUtils.replace(result, RIGHT_DOUBLE_QUOTATION_MARK,
            RIGHT_SINGLE_QUOTATION_MARK_CHARACTER);
    }


    // ****************************************************
    // setter and getter methods
    // ****************************************************
    
    public UserProfileHolder getUserProfile()
    {
        return userProfile;
    }


    public void setUserProfile(UserProfileHolder userProfile)
    {
        this.userProfile = userProfile;
    }


    public UserProfileTmpHolder getUserProfileTmp()
    {
        return userProfileTmp;
    }


    public void setUserProfileTmp(UserProfileTmpHolder userProfileTmp)
    {
        this.userProfileTmp = userProfileTmp;
    }
    
    
    public String getCurrentPwd()
    {
        return currentPwd;
    }


    public void setCurrentPwd(String currentPwd)
    {
        this.currentPwd = currentPwd;
    }


    public String getNewPwd()
    {
        return newPwd;
    }


    public void setNewPwd(String newPwd)
    {
        this.newPwd = newPwd;
    }


    public String getConfirmPwd()
    {
        return confirmPwd;
    }


    public void setConfirmPwd(String confirmPwd)
    {
        this.confirmPwd = confirmPwd;
    }


    public String getIsChangePwd()
    {
        return isChangePwd;
    }


    public void setIsChangePwd(String isChangePwd)
    {
        this.isChangePwd = isChangePwd;
    }


    public String getAvailableSupplierList()
    {
        return availableSupplierList;
    }


    public void setAvailableSupplierList(String availableSupplierList)
    {
        this.availableSupplierList = availableSupplierList;
    }

}
