//*****************************************************************************
//
// File Name       :  LoginPwdValidateHelper.java
// Date Created    :  Aug 29, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Aug 29, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.DictionaryWordHolder;
import com.pracbiz.b2bportal.core.holder.PasswordHistoryHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.DictionaryWordService;
import com.pracbiz.b2bportal.core.service.PasswordHistoryService;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class PasswordValidateHelper
{
    public static final String PARAM_ID_PWD_MAX_LENGTH="PWD_MAX_LENGTH";
    public static final String PARAM_ID_PWD_MIN_LENGTH="PWD_MIN_LENGTH";
    public static final String PARAM_ID_ALPHANUMERIC="ALPHANUMERIC";
    public static final String PARAM_ID_NO_REPEATED_CHARACTER="NO_REPEATED_CHARACTER";
    public static final String PARAM_ID_MIXTURE_CASE="MIXTURE_CASE";
    public static final String PARAM_ID_SPECIAL_CHARACTER="SPECIAL_CHARACTER";
    public static final String PARAM_ID_NOT_IN_DICT_WORD="NOT_IN_DICT_WORD";
    public static final String PARAM_ID_NOT_REPEAT_FOR_PWD_CHANGE="NOT_REPEAT_FOR_PWD_CHANGE";
    public static final String PARAM_ID_NOT_EQUAL_LOGIN_ID="NOT_EQUAL_LOGIN_ID";
    
    @Autowired
    private ControlParameterService controlParameterService;
    @Autowired
    private DictionaryWordService dictionaryWordService;
    @Autowired
    private PasswordHistoryService passwordHistoryService;
    
    private String errorMsg;
    
    public boolean validatePwd(BaseAction action,UserProfileHolder user, String password) throws Exception
    {
        List<ControlParameterHolder> validatePwdRules = controlParameterService
            .selectCacheControlParametersBySectId(CoreCommonConstants.SECT_ID_PWD_RULE);
        
        boolean flag = false;
        
        for (ControlParameterHolder rule : validatePwdRules)
        {
            if (!rule.getValid())
            {
                continue;
            }
            
            if (!flag)
            {
                flag = this.checkLength(action, rule, password);
            }
            
            if (!flag)
            {
                flag = this.isRepeatedCharacter(action, rule, password);
            }
            
            if (!flag)
            {
                flag = this.isMixtureCase(action, rule, password);
            }
            
            if (!flag)
            {
                flag = this.isAlphanumeric(action, rule, password);
            }
            
            if (!flag)
            {
                flag = this.isContainsSpecialCharacter(action, rule, password);
            }
            
            if (!flag)
            {
                flag = this.isInDictWord(action, rule, password);
            }
            
            if (!flag)
            {
                BigDecimal userOid = user.getUserOid();
                flag = this.repeatForPwd(action, rule, userOid, password);
            }
            
            if (!flag)
            {
                String loginId = user.getLoginId();
                flag = this.isEqualsLoginId(action, rule, loginId, password);
            }
        }
        
        return flag;
    }
    
    
    private boolean checkLength(BaseAction action, ControlParameterHolder rule, String password)
    {
        String paramId = rule.getParamId();
        int numValue = rule.getNumValue() == null ? 0 : rule.getNumValue();
        if (PARAM_ID_PWD_MAX_LENGTH.equalsIgnoreCase(paramId)
            && password.length() > numValue)
        {
            this.setErrorMsg(action.getText("B2BPC1002", new String[]{String.valueOf(numValue)}));
            return true;
        }
        
        if (PARAM_ID_PWD_MIN_LENGTH.equalsIgnoreCase(paramId)
            && password.length() < numValue)
        {
            this.setErrorMsg(action.getText("B2BPC1003", new String[]{String.valueOf(numValue)}));
            return true;
        }
       
        return false;
    }
    
    
    private boolean isAlphanumeric(BaseAction action, ControlParameterHolder rule, String password)
    {
        String paramId = rule.getParamId();
        if (PARAM_ID_ALPHANUMERIC.equalsIgnoreCase(paramId)
            && !password.matches("(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*"))
        {
            this.setErrorMsg(action.getText("B2BPC1004"));
            return true;
        }
        
        return false;
    }
    
    
    private boolean isRepeatedCharacter(BaseAction action, ControlParameterHolder rule, String password)
    {
        String paramId = rule.getParamId();
        
        if (PARAM_ID_NO_REPEATED_CHARACTER.equalsIgnoreCase(paramId))
        {
            
            Pattern p = Pattern.compile("(\\w)\\1+");
            Matcher m = p.matcher(password);
            if (m.find())
            {
                this.setErrorMsg(action.getText("B2BPC1005"));
                return true;
            }
        }
        
        return false;
    }
    
    
    private boolean isMixtureCase(BaseAction action, ControlParameterHolder rule, String password)
    {
        String paramId = rule.getParamId();
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        
        if (PARAM_ID_MIXTURE_CASE.equalsIgnoreCase(paramId)
            && (!password.matches(upperCaseChars) 
                || !password.matches(lowerCaseChars)))
        {
            this.setErrorMsg(action.getText("B2BPC1006"));
            return true;
        }
        
        return false;
    }
    
    
    private boolean isContainsSpecialCharacter(BaseAction action, ControlParameterHolder rule, String password)
    {
        String paramId = rule.getParamId();
        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (PARAM_ID_SPECIAL_CHARACTER.equalsIgnoreCase(paramId)
            && (!password.matches(specialChars)))
        {
            this.setErrorMsg(action.getText("B2BPC1007"));
            return true;
        }
        
        return false;
    }
    
    
    private boolean isInDictWord(BaseAction action, ControlParameterHolder rule, String password) throws Exception
    {
        String paramId = rule.getParamId();
        
        if (PARAM_ID_NOT_IN_DICT_WORD.equalsIgnoreCase(paramId))
        {
            List<DictionaryWordHolder> dictWords = this.dictionaryWordService.selectAllDictionaryWords();
            
            boolean flag = false;
            String keyWord = null;
            for (DictionaryWordHolder dictWord : dictWords)
            {
                if (StringUtils.contains(password, dictWord.getKeyWord()))
                {
                    flag = true;
                    keyWord = dictWord.getKeyWord();
                    break;
                }
            }
            
            if (flag)
            {
                this.setErrorMsg(action.getText("B2BPC108",new String[]{keyWord}));
                return true;
            }
           
        }
        
        return false;
    }
    
    
    private boolean repeatForPwd(BaseAction action, ControlParameterHolder rule, BigDecimal userOid, String password) throws Exception
    {
        String paramId = rule.getParamId();
        
        if (PARAM_ID_NOT_REPEAT_FOR_PWD_CHANGE.equalsIgnoreCase(paramId))
        {
            PasswordHistoryHolder param = new PasswordHistoryHolder();
            param.setUserOid(userOid);
            String encodePwd = EncodeUtil.getInstance().computePwd(password, userOid);
            param.setOldLoginPwd(encodePwd);
            
            List<PasswordHistoryHolder> historys = this.passwordHistoryService.select(param);
            
            if (historys == null || historys.isEmpty())
            {
                return false;
            }
            
            this.setErrorMsg(action.getText("B2BPC1009"));
            return true;
        }
        
        return false;
    }
    
    
    private boolean isEqualsLoginId(BaseAction action, ControlParameterHolder rule, String loginId, String password)
    {
        String paramId = rule.getParamId();
        if (PARAM_ID_NOT_EQUAL_LOGIN_ID.equalsIgnoreCase(paramId)
            && (password.indexOf(loginId) > -1))
        {
            this.setErrorMsg(action.getText("B2BPC1010"));
            return true;
        }
        
        return false;
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
