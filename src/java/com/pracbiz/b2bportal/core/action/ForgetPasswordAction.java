package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.ForgetPasswordService;
import com.pracbiz.b2bportal.core.service.ResetPasswordRequestHistoryService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.UserProfileService;
import com.pracbiz.b2bportal.core.service.UserProfileTmpService;
import com.pracbiz.b2bportal.core.util.PasswordValidateHelper;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class ForgetPasswordAction extends ProjectBaseAction
{

    private static final Logger log = LoggerFactory.getLogger(ForgetPasswordAction.class);
    private static final long serialVersionUID = 1L;
    public static final String PREVIOUS_RESET_PWD_RECORD = "previousResetPwdRecord";
    private static final String SECT_ID_CTRL = "CTRL";
    private static final String DATA_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String MSG_ERROR_FORGET_PASSWORD = "MSG_ERROR_FORGET_PASSWORD";

    private UserProfileHolder userProfile;
    private ResetPasswordRequestHistoryHolder previousResetPwdRecord;

    @Autowired
    private transient UserProfileService userProfileService;
    @Autowired
    private transient UserProfileTmpService userProfileTmpService;
    @Autowired
    private transient ForgetPasswordService forgetPasswordService;
    @Autowired
    private transient ResetPasswordRequestHistoryService resetPasswordRequestHistoryService;
    @Autowired
    private transient ControlParameterService controlParameterService;
    @Autowired
    private transient BuyerService buyerService;
    @Autowired
    private transient SupplierService supplierService;
    @Autowired
    private transient PasswordValidateHelper passwordValidateHelper;

    private String newPassword;

    private String confirmPassword;

    private transient CommonParameterHolder cp;


    public String init()
    {
        return SUCCESS;
    }


    // *****************************************************
    // init reset password and send email link.
    // *****************************************************
    public void validateResetPassword()
    {
        boolean flag = this.hasErrors();
        if (userProfile == null || userProfile.getLoginId() == null
                || "".equals(userProfile.getLoginId().trim()))
        {
            this.setErrorMsg(this.getText("B2BPC0809"));
        }
        else
        {
            try
            {
                log.info(this.getText("B2BPC0802", new String[] {
                        userProfile.getLoginId(),
                        this.getRequest().getRemoteAddr() }));
                String loginId = userProfile.getLoginId();
                userProfile = userProfileService
                        .getUserProfileByLoginId(userProfile.getLoginId());
                if (userProfile == null)
                {
                    log.info(this.getText("B2BPC0810", new String[] { loginId,
                            this.getRequest().getRemoteAddr() }));
                    this.setErrorMsg(this.getText("B2BPC0811",
                            new String[] { loginId }));
                    flag = true;
                }
                if (!flag)
                {
                    if (!flag && !userProfile.getActive())
                    {
                        this.setErrorMsg(this.getText("B2BPC0824",
                                new String[] { userProfile.getLoginId() }));
                        flag = true;
                    }
                    if (!flag && userProfile.getBlocked())
                    {
                        this.setErrorMsg(this.getText("B2BPC0825",
                                new String[] { userProfile.getLoginId() }));
                        flag = true;
                    }
                }
                if (!flag)
                {
                    previousResetPwdRecord = new ResetPasswordRequestHistoryHolder();
                    previousResetPwdRecord.setLoginId(userProfile.getLoginId());
                    previousResetPwdRecord.setValid(Boolean.TRUE);
                    List<? extends Object> resetPasswordRequestHistorys = resetPasswordRequestHistoryService
                            .select(previousResetPwdRecord);
                    if (resetPasswordRequestHistorys == null
                            || resetPasswordRequestHistorys.isEmpty())
                    {
                        previousResetPwdRecord = null;
                    }
                    else
                    {
                        previousResetPwdRecord = (ResetPasswordRequestHistoryHolder) resetPasswordRequestHistorys
                                .get(0);
                        int reqInvl = controlParameterService
                                .selectCacheControlParameterBySectIdAndParamId(
                                        SECT_ID_CTRL, "REQ_INVL").getNumValue();
                        Date canResetDate = new Date(previousResetPwdRecord
                                .getRequestTime().getTime()
                                + reqInvl * 60L * 1000);
                        if ((new Date()).before(canResetDate))
                        {
                            log.info(this.getText("B2BPC0812", new String[] {
                                    String.valueOf(reqInvl),
                                    DateUtil.getInstance().convertDateToString(
                                            previousResetPwdRecord
                                                    .getRequestTime(),
                                            DATA_FORMAT),
                                    DateUtil.getInstance().convertDateToString(
                                            new Date(), DATA_FORMAT) }));
                            this
                                    .setErrorMsg(this
                                            .getText(
                                                    "B2BPC0812",
                                                    new String[] {
                                                            String
                                                                    .valueOf(reqInvl),
                                                            DateUtil
                                                                    .getInstance()
                                                                    .convertDateToString(
                                                                            previousResetPwdRecord
                                                                                    .getRequestTime(),
                                                                            DATA_FORMAT),
                                                            DateUtil
                                                                    .getInstance()
                                                                    .convertDateToString(
                                                                            new Date(),
                                                                            DATA_FORMAT) }));
                            flag = true;
                        }
                    }

                }
            }
            catch (Exception e)
            {
                ErrorHelper.getInstance().logError(log, this, e);
                try
                {
                    this
                            .setErrorMsg(this
                                    .getText(
                                            "B2BPC0814",
                                            new String[] { controlParameterService
                                                    .selectCacheControlParameterBySectIdAndParamId(
                                                            SECT_ID_CTRL,
                                                            PARAM_ID_HELPDESK_NO)
                                                    .getStringValue() }));
                }
                catch (Exception e1)
                {
                    ErrorHelper.getInstance().logError(log, this, e1);
                }
            }
        }
    }


    public String resetPassword()
    {
        try
        {
            byte[] nonceBytes = EncodeUtil.getInstance()
                    .generateSecureBytes(16);
            String hashValue = EncodeUtil.getInstance().computeSha2Digest(
                    nonceBytes);
            ResetPasswordRequestHistoryHolder newResetPwdRecord = new ResetPasswordRequestHistoryHolder();
            newResetPwdRecord.setHashCode(hashValue);
            newResetPwdRecord.setLoginId(userProfile.getLoginId());
            newResetPwdRecord.setClientIp(this.getRequest().getRemoteAddr());
            newResetPwdRecord.setRequestTime(new Date());
            newResetPwdRecord.setValid(Boolean.TRUE);
            this.initCommonParameter();
            String url =  appConfig.getServerUrl() + "/forgetPassword/";
            String resetUrl = url + "confirmResetPassword.action?h=" + hashValue;

            Date expireDate = DateUtil.getInstance().dateAfterDays(
                    newResetPwdRecord.getRequestTime(),
                    controlParameterService
                            .selectCacheControlParameterBySectIdAndParamId(
                                    SECT_ID_CTRL, "EMAIL_EXPIRE_PERIOD")
                            .getNumValue());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", userProfile.getUserName());
            map.put("date", DateUtil.getInstance().convertDateToString(
                    new Date(), DATA_FORMAT));
            map.put("resetUrl", resetUrl);
            map.put("expireDate", DateUtil.getInstance().convertDateToString(
                    expireDate, DATA_FORMAT));
            map.put("helpdeskNumber", controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            SECT_ID_CTRL, PARAM_ID_HELPDESK_NO).getStringValue());
            map.put("helpdeskEmail", controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            SECT_ID_CTRL, PARAM_ID_HELPDESK_EMAIL).getStringValue());
            map.put("companyName", getCompanyName(userProfile.getUserType()));
            forgetPasswordService.resetPassword(previousResetPwdRecord,
                    newResetPwdRecord, cp, userProfile, emailEngine, map);
            this.addActionMessage(this.getText("B2BPC0804"));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        return SUCCESS;
    }


    // *****************************************************
    // confirm reset password.
    // *****************************************************
    public void validateConfirmResetPassword()
    {
        log.info(this.getText("B2BPC0805", this.getRequest().getRemoteAddr()));
        try
        {
            boolean isValidRequest = true;

            if (this.getRequest() == null)
            {
                log.info(this.getText("B2BPC0815", new String[] {
                        this.getRequest().getRemoteAddr(), "Post", "Get" }));
                isValidRequest = false;
            }
            if (isValidRequest && this.getRequest().getParameter("h") == null)
            {
                log.info(this.getText("B2BPC0816", new String[] { this
                        .getRequest().getParameter("h") }));
                isValidRequest = false;
            }
            if (isValidRequest)
            {
                previousResetPwdRecord = new ResetPasswordRequestHistoryHolder();
                String h = this.getRequest().getParameter("h");
                previousResetPwdRecord.setHashCode(h);
                previousResetPwdRecord.setValid(Boolean.TRUE);
                List<? extends Object> previousResetPwdRecords = resetPasswordRequestHistoryService
                        .select(previousResetPwdRecord);
                if (previousResetPwdRecords == null
                        || previousResetPwdRecords.isEmpty())
                {
                    log.info(this.getText("B2BPC0816", new String[] { h }));
                    isValidRequest = false;
                }
                else
                {
                    previousResetPwdRecord = (ResetPasswordRequestHistoryHolder) previousResetPwdRecords
                            .get(0);
                    Date expireDate = DateUtil
                            .getInstance()
                            .dateAfterDays(
                                    previousResetPwdRecord.getRequestTime(),
                                    controlParameterService
                                            .selectCacheControlParameterBySectIdAndParamId(
                                                    SECT_ID_CTRL,
                                                    "PWD_EXPIRE_DAYS")
                                            .getNumValue());
                    if ((new Date()).after(expireDate))
                    {
                        log.info(this.getText("B2BPC0817", new String[] {
                                h,
                                DateUtil.getInstance().convertDateToString(
                                        expireDate, DATA_FORMAT) }));
                        previousResetPwdRecord.setValid(Boolean.FALSE);
                        resetPasswordRequestHistoryService
                                .updateByPrimaryKeySelective(null,
                                        previousResetPwdRecord);
                        isValidRequest = false;
                    }
                }
            }
            if (!isValidRequest)
            {
                this.addActionError(this.getText("B2BPC0821"));
            }
        }
        catch (Exception e)
        {
            log.error(getText("B2BPC0818"), e);
            ErrorHelper.getInstance().logError(log, this, e);
        }
    }


    public String confirmResetPassword()
    {
        try
        {
            this.getSession().put(PREVIOUS_RESET_PWD_RECORD,
                    previousResetPwdRecord);
            log.info(this.getText("B2BPC0806",
                    new String[] { previousResetPwdRecord.getLoginId() }));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        return SUCCESS;
    }


    // *****************************************************
    // save new password
    // *****************************************************
    public void validateSaveNewPassword()
    {
        try
        {
            previousResetPwdRecord = (ResetPasswordRequestHistoryHolder) this
                    .getSession().get(PREVIOUS_RESET_PWD_RECORD);
            boolean flag = this.hasActionErrors();
            if (previousResetPwdRecord == null)
            {
                this.setErrorMsg(this.getText("B2BPC0829"));
                flag = true;
            }
            if (!flag)
            {
                log.info(this.getText("B2BPC0807",
                        new String[] { previousResetPwdRecord.getLoginId() }));
                userProfile = userProfileService
                        .getUserProfileByLoginId(previousResetPwdRecord
                                .getLoginId());
                if (userProfile == null)
                {
                    this.setErrorMsg("B2BPC0819");
                    flag = true;
                }
            }
            if (!flag && "".equals(newPassword))
            {
                this.setErrorMsg(this.getText("B2BPC0830"));
                flag = true;
            }
            if (!flag && "".equals(confirmPassword))
            {
                this.setErrorMsg(this.getText("B2BPC0831"));
                flag = true;
            }
            if (!flag && !newPassword.equals(confirmPassword))
            {
                this.setErrorMsg(this.getText("B2BPC0820"));
                flag = true;
            }
            if (!flag
                    && passwordValidateHelper.validatePwd(
                            new ForgetPasswordAction(), userProfile,
                            newPassword))
            {
                this.setErrorMsg(passwordValidateHelper.getErrorMsg());
                flag = true;
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            try
            {
                this.setErrorMsg(this.getText("B2BPC0822",
                        new String[] { controlParameterService
                                .selectCacheControlParameterBySectIdAndParamId(
                                        SECT_ID_CTRL, PARAM_ID_HELPDESK_NO)
                                .getStringValue() }));
            }
            catch (Exception e1)
            {
                ErrorHelper.getInstance().logError(log, this, e1);
            }
        }
    }


    public String saveNewPassword()
    {
        try
        {
            String newPwd = EncodeUtil.getInstance().computePwd(
                    newPassword, userProfile.getUserOid());
            PasswordHistoryHolder passwordHistory = new PasswordHistoryHolder();
            passwordHistory.setChangeDate(new Date());
            passwordHistory.setOldLoginPwd(userProfile.getLoginPwd()==null ? newPwd : userProfile.getLoginPwd());
            passwordHistory.setUserOid(userProfile.getUserOid());
            passwordHistory.setChangeReason("C");
            passwordHistory.setActor(userProfile.getLoginId());

            UserProfileTmpHolder newUserProfile = userProfileTmpService
                    .selectUserProfileTmpByLoginId(userProfile.getLoginId());

            newUserProfile.setLoginPwd(newPwd);
            newUserProfile.setUpdateBy(userProfile.getLoginId());
            newUserProfile.setUpdateDate(new Date());
            newUserProfile.setLastResetPwdDate(new Date());
            initCommonParameter();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", userProfile.getUserName());
            map.put("companyName", getCompanyName(userProfile.getUserType()));
            UserProfileTmpHolder oldUserProfile = new UserProfileTmpHolder();
            BeanUtils.copyProperties(userProfile, oldUserProfile);
            forgetPasswordService.saveNewPassword(passwordHistory,
                    newUserProfile, oldUserProfile, cp, emailEngine, map);

            this.getSession().remove(PREVIOUS_RESET_PWD_RECORD);
            log.info(this.getText("B2BPC0808", new String[] { userProfile
                    .getLoginId() }));
            this.addActionMessage(this.getText("B2BPC0828"));
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            try
            {
                this.setErrorMsg(this.getText("B2BPC0822",
                        new String[] { controlParameterService
                                .selectCacheControlParameterBySectIdAndParamId(
                                        SECT_ID_CTRL, PARAM_ID_HELPDESK_NO)
                                .getStringValue() }));
            }
            catch (Exception e1)
            {
                ErrorHelper.getInstance().logError(log, this, e);
            }
        }
        return SUCCESS;
    }


    // *****************************************************
    // get company name by userType
    // *****************************************************
    private String getCompanyName(BigDecimal userType) throws Exception
    {
        String companyName = "";
        if ("3".equals(String.valueOf(userType))
                || "5".equals(String.valueOf(userType)))
        {
            SupplierExHolder supplier = new SupplierExHolder();
            supplier.setSupplierOid(userProfile.getSupplierOid());
            companyName = ((SupplierHolder) supplierService.select(supplier)
                    .get(0)).getSupplierName();
        }
        else if ("2".equals(String.valueOf(userType))
                || "4".equals(String.valueOf(userType)))
        {
            BuyerHolder buyer = new BuyerHolder();
            buyer.setBuyerOid(userProfile.getBuyerOid());
            companyName = ((BuyerHolder) buyerService.select(buyer).get(0))
                    .getBuyerName();
        }
        return companyName;
    }


    // *****************************************************
    // get company name by userType
    // *****************************************************
    private void initCommonParameter()
    {
        cp = new CommonParameterHolder();
        cp.setLoginId(userProfile.getLoginId());
        cp.setCurrentUserOid(userProfile.getUserOid());
        cp.setClientIp(this.getRequest().getRemoteAddr());
        cp.setMkMode(false);
    }


    private void setErrorMsg(String errorMsg)
    {
        this.addActionError(errorMsg);
        this.getRequest().setAttribute(MSG_ERROR_FORGET_PASSWORD, errorMsg);
    }


    // *****************************************************
    // getter and setter.
    // *****************************************************
    public UserProfileHolder getUserProfile()
    {
        return userProfile;
    }


    public void setUserProfile(UserProfileHolder userProfile)
    {
        this.userProfile = userProfile;
    }


    public String getNewPassword()
    {
        return newPassword;
    }


    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }


    public String getConfirmPassword()
    {
        return confirmPassword;
    }


    public void setConfirmPassword(String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }

}
