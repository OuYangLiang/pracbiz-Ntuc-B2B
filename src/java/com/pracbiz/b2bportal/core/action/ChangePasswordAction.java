package com.pracbiz.b2bportal.core.action;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.service.ChangePasswordService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PasswordValidateHelper;


public class ChangePasswordAction extends ProjectBaseAction implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(ChangePasswordAction.class);
    private static final long serialVersionUID = 1943077783075627793L;
    private String loginId;
    private String newPwd;

    @Autowired transient private UserProfileService userProfileService;
    @Autowired transient private ChangePasswordService changePasswordService;
    @Autowired transient private PasswordValidateHelper passwordValidateHelper;


    public ChangePasswordAction()
    {
        this.initMsg();
    }

    
    public void validateSaveEdit()
    {
        boolean flag = this.hasErrors();
        String msg = null;
        try
        {
            String loginId = this.getRequest().getParameter("loginId");
            if (!flag && StringUtils.isBlank(loginId))
            {
                msg = getText("B2BPC0809");
                flag = true;
            }
            
            if (!flag)
            {
                UserProfileHolder user = this.userProfileService.getUserProfileByLoginId(loginId);
                
                if (user == null)
                {
                    msg = getText("B2BPC0819");
                    flag = true;
                    return;
                }
                
                String currPwd = (String) this.getRequest().getParameter("currPwd");
                String newPwd = (String) this.getRequest().getParameter("newPwd");
                String cfmNewPwd = (String) this.getRequest().getParameter("cfmNewPwd");
                
                if (!flag && StringUtils.isBlank(currPwd))
                {
                    msg = getText("B2BPC0729");
                    flag = true;
                }
                
                if (!flag && StringUtils.isBlank(newPwd))
                {
                    msg = getText("B2BPC0731");
                    flag = true;
                }
                
                String encodeCurrPwd = EncodeUtil.getInstance().computePwd(currPwd, user.getUserOid());
                if (!flag && !encodeCurrPwd.equals(user.getLoginPwd()))
                {
                    msg = getText("B2BPC0714");
                    flag = true;
                }
                
                if (!flag && currPwd.equals(newPwd))
                {
                    msg = getText("B2BPC0715");
                    flag = true;
                }
                
                if (!flag)
                {
                    flag = this.passwordValidateHelper.validatePwd(this, user, newPwd);
                    msg = this.passwordValidateHelper.getErrorMsg();
                }
                
                if (!flag && !newPwd.equals(cfmNewPwd))
                {
                    msg = getText("B2BPC0716");
                    flag = true;
                }
            }
            
        }
        catch (Exception e)
        {
            flag = true;
            log.error("validateSaveEdit:");
            log.error("Error occur on validateSaveEdit() Report ", e);
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            msg = this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo});
        }
        finally
        {
            if (log.isInfoEnabled())
            {
                log.info(msg);
            }
            
            if (flag)
            {
                this.addActionError(msg);
                
                this.getRequest().setAttribute("ERROR_CHANGE_PASSWORD_FAILED", msg);
            }
            
        }
    }
    

    public String saveEdit()
    {
        try
        {
            UserProfileHolder oldObj = this.userProfileService.getUserProfileByLoginId(loginId);
            String newPwd = (String) this.getRequest().getParameter("newPwd");
            this.setNewPwd(newPwd);
            
            String password =EncodeUtil.getInstance().computePwd(newPwd, oldObj.getUserOid());
            UserProfileHolder newObj = new UserProfileHolder();
            BeanUtils.copyProperties(oldObj, newObj);
            
            newObj.setLoginPwd(password.trim());
            newObj.setUpdateDate(new Date());
            newObj.setUpdateBy(oldObj.getLoginId());
            newObj.setLastResetPwdDate(new Date());
            newObj.trimAllString();
            CommonParameterHolder cp = new CommonParameterHolder();
            cp.setLoginId(oldObj.getLoginId());
            cp.setCurrentUserOid(oldObj.getUserOid());
            cp.setClientIp(this.getRequest().getRemoteAddr());
            cp.setMkMode(false);
            changePasswordService.doChangePassword(cp, newObj, oldObj);
            if (log.isInfoEnabled())
            {
                String date = DateUtil.getInstance().convertDateToString(new Date(),"dd/MM/yyyy HH:mm:ss");
                String ip = this.getRequest().getRemoteAddr();
                log.info(getText("B2BPC0703", new String[]{loginId,ip,date}));
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }

    //*****************************************************
    // getter and setter method
    //*****************************************************
    public String getLoginId()
    {
        return loginId;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }


    /**
     * Getter of newPwd.
     * @return Returns the newPwd.
     */
    public String getNewPwd()
    {
        return newPwd;
    }


    /**
     * Setter of newPwd.
     * @param newPwd The newPwd to set.
     */
    public void setNewPwd(String newPwd)
    {
        this.newPwd = newPwd;
    }

}
