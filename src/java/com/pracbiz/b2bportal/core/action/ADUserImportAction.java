//*****************************************************************************
//
// File Name       :  ADUserImportAction.java
// Date Created    :  Mar 14, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Mar 14, 2013 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.naming.CommunicationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.ActiveDirectoryHolder;
import com.pracbiz.b2bportal.core.holder.extension.AdGroupHolder;
import com.pracbiz.b2bportal.core.holder.extension.AdUserHolder;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.LDAPUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class ADUserImportAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(ADUserImportAction.class);
    private static final long serialVersionUID = 3162869872710524981L;
    private static final String SESSION_GROUP_PARAMETER = "session.parameter.ADUserImportAction.selectedGroups";
    private static final String REQUEST_PARAMTER_GROUP = "selectedGroups";
    private static final String REQUEST_GROUP_DELIMITER = "\\-";
    public static final String SESSION_KEY_SEARCH_PARAMETER_AD_USER = "SEARCH_PARAMETER_AD_USER";
    
    @Autowired private transient LDAPUtil ldapUtil;
    @Autowired private transient OidService oidService;
    @Autowired private transient UserProfileService userProfileService;
    @Autowired private transient UserProfileTmpService userProfileTmpService;
    
    private ActiveDirectoryHolder param;
    private boolean isSucc;
    private List<? extends AdGroupHolder> groups;
    private String errorMsg;
    private String allUser;

    
    public ADUserImportAction()
    {
        this.initMsg();
    }
    
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_AD_USER);
        
        param = (ActiveDirectoryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_AD_USER);
        
        return SUCCESS;
    }
    
    public String putParamIntoSession()
    {
        this.getSession().put(SESSION_GROUP_PARAMETER,
            this.getRequest().getParameter(REQUEST_PARAMTER_GROUP));

        return SUCCESS;
    }
    
    public String initGroupData()
    {
        try
        {
            errorMsg = this.validateInit();
            
            if (StringUtils.isBlank(errorMsg))
            {
                groups = ldapUtil.getOrganizationalUnit(param);
                isSucc = true;
            }
        }
        catch (CommunicationException e)
        {
            errorMsg = this.getText("B2BPU0208");
            ErrorHelper.getInstance().logError(log, e);
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            errorMsg = this.getText("B2BPU0207", new String[]{tickNo});
        }
        
        return SUCCESS;
    }
    
    
    public void validateImportUsers()
    {
        String errorMsg = this.validateInit();
        
        if (StringUtils.isNotBlank(errorMsg))
        {
            this.addActionError(errorMsg);
        }
    }

    
    
    public String importUsers()
    {
       
        try
        {
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_AD_USER, param);
            List<AdUserHolder> users = null;
            if(StringUtils.isBlank(allUser))
            {
                users = ldapUtil.getAllUsers(param);
            }
            else
            {
                String[] parts = REQUEST_PARAMTER_GROUP.split(REQUEST_GROUP_DELIMITER);
                for (String part : parts)
                {
                    List<AdUserHolder> rlts = ldapUtil.getUsersByGroup(param, part);
                    if (rlts == null || rlts.isEmpty())
                    {
                        continue;
                    }
                    
                    if (users != null)
                    {
                        users.addAll(rlts);
                        continue;
                    }
                    
                    users = rlts;
                }
                
            }
            
            if(users == null || users.isEmpty())
            {
                msg.saveError(this.getText("B2BPU0211"));
            }
            else
            {
                for (AdUserHolder user : users)
                {
                    if (userProfileTmpService.isLoginIdExist(user.getUserName()))
                    {
                        msg.saveError(this.getText("B2BPU0212",new String[]{user.getUserName() }));
                        continue;
                    }
                    
                    UserProfileTmpHolder userProfile = this.initUserProfile(user);
                    userProfileService.createUserProfile(this.getCommonParameter(), null, null, userProfile, false);
                    msg.saveSuccess(this.getText("B2BPU0210", new String[]{userProfile.getLoginId(),this.getLoginIdOfCurrentUser() }));
                    
                    log.info(this.getText(
                        "B2BPU0210",
                        new String[] {userProfile.getLoginId(),
                            this.getLoginIdOfCurrentUser()}));
                }
                
                msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
                MessageTargetHolder mt = new MessageTargetHolder();
                mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
                mt.setTargetURI(INIT);
                msg.addMessageTarget(mt);
            }

        }
        catch(Exception e)
        {
            this.handleException(e);
        }

        return FORWARD_COMMON_MESSAGE;
    }
    
    
    private UserProfileTmpHolder initUserProfile(AdUserHolder user) throws Exception
    {
        UserProfileTmpHolder userProfile = new UserProfileTmpHolder();
        userProfile.setLoginId(user.getUserName());
        userProfile.setUserName(user.getUserName());
        userProfile.setEmail(user.getEmail());
        userProfile.setLoginMode(TYPE_AUTH_ID_AD);
        userProfile.setUserOid(oidService.getOid());
        userProfile.setCreateDate(new Date());
        userProfile.setCreateBy(this.getLoginIdOfCurrentUser());
        userProfile.setInit(false);
        userProfile.setFailAttempts(0);
        userProfile.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
        userProfile.setUserType(new BigDecimal(4));
        userProfile.setActive(true);
        userProfile.setBlocked(false);
        userProfile.setGender(USER_GENDER_MALE);
        return userProfile;
    }

    private String validateInit()
    {
        boolean flag = this.hasErrors();
        if(!flag && param == null)
        {
            return this.getText("B2BPU0201");
        }

        if(!flag && StringUtils.isBlank(param.getDomain()))
        {
            return this.getText("B2BPU0202");
        }

        if(!flag && StringUtils.isBlank(param.getHostname()))
        {
            return this.getText("B2BPU0203");
        }

        if(!flag
            && (StringUtils.isBlank(param.getPort()) || !NumberUtils
                .isNumber(param.getPort())))
        {
            return this.getText("B2BPU0204");
        }

        if(!flag && StringUtils.isBlank(param.getUserName()))
        {
            return this.getText("B2BPU0205");
        }

        if(!flag && StringUtils.isBlank(param.getPassword()))
        {
            return this.getText("B2BPU0206");
        }

        return null;
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

    
    // *****************************************************
    // setter and getter method
    // *****************************************************
    /**
     * Getter of param.
     * @return Returns the param.
     */
    public ActiveDirectoryHolder getParam()
    {
        return param;
    }

    /**
     * Setter of param.
     * @param param The param to set.
     */
    public void setParam(ActiveDirectoryHolder param)
    {
        this.param = param;
    }

    /**
     * Getter of isSucc.
     * @return Returns the isSucc.
     */
    public boolean isSucc()
    {
        return isSucc;
    }

    /**
     * Setter of isSucc.
     * @param isSucc The isSucc to set.
     */
    public void setSucc(boolean isSucc)
    {
        this.isSucc = isSucc;
    }

    /**
     * Getter of groups.
     * @return Returns the groups.
     */
    public List<? extends AdGroupHolder> getGroups()
    {
        return groups;
    }

    /**
     * Setter of groups.
     * @param groups The groups to set.
     */
    public void setGroups(List<? extends AdGroupHolder> groups)
    {
        this.groups = groups;
    }

    /**
     * Getter of errorMsg.
     * @return Returns the errorMsg.
     */
    public String getErrorMsg()
    {
        return errorMsg;
    }

    /**
     * Setter of errorMsg.
     * @param errorMsg The errorMsg to set.
     */
    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }
    
}



