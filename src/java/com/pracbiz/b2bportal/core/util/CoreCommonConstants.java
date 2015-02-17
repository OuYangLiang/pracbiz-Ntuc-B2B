//*****************************************************************************
//
// File Name       :  CoreCommonConstants.java
// Date Created    :  Aug 29, 2012
// Last Changed By :  $Author: xuchengqing $
// Last Changed On :  $Date: 2011-07-01 10:56:27 +0800 (周五, 01 七月 2011) $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.io.File;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public interface CoreCommonConstants extends
com.pracbiz.b2bportal.base.util.CommonConstants
{
    public static final String PS = File.separator;
    public static final String DOC_FILENAME_DELIMITOR = "_";
    public static final String COMMA_DELIMITOR = ",";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String LEADING_ZERO = "0";
    public static final String DEFAULT_SUBTYPE = "default";
    public static final String SYSTEM = "SYSTEM";
    
    /*################# common session key #########################*/
    public static final String SESSION_KEY_USER = "SESSION_CURRENT_USER_PROFILE";
    public static final String SESSION_KEY_MENU = "MENU";
    public static final String SESSION_MENU_LIST = "menuList";
    public static final String SESSION_CURRENT_MENU = "currentMenu";
    public static final String SESSION_KEY_MODULE_ID = "moduleId";
    public static final String SESSION_KEY_CLIENT_IP = "clientIp";
    public static final String SESSION_KEY_USER_AGENT = "userAgent";
    public static final String SESSION_KEY_PERMIT_URL = "permitUrl";
    public static final String SESSION_KEY_HELP_INFO="helpExHolder";
    public static final String SESSION_KEY_LOGIN_WITH_MSG="loginWithMsg";
    public static final String SESSION_KEY_COMMON_PARAM = "commonParam";
    public static final String SESSION_KEY_STRUTS_LOCALE="WW_TRANS_I18N_LOCALE";
    public static final String SESSION_KEY_ROLE_LIST="roleList";

    
    /*################# ntlm auth key #########################*/
    public static final String NTLM_DOMAIN = "ntlm.auth.domain";
    public static final String NTLM_DOMAIN_CONTROLLER = "ntlm.auth.domain.controller";
    public static final String NTLM_DOMAIN_CONTROLLER_NAME = "ntlm.auth.domain.controller.name";
    public static final String NTLM_SERVICE_ACCOUNT = "ntlm.auth.service.account";
    public static final String NTLM_SERVICE_PWD = "ntlm.auth.service.password";
    public static final String JCIFS_NETBIOS_CACHE_POLICY = "jcifs.netbios.cachePolicy";
    public static final String JCIFS_SMB_CLIENT_SO_TIME_OUT="jcifs.smb.client.soTimeout";
    public static final String AUTHORIZATION = "Authorization";
    public static final String NTLM_USER_ACCOUNT = "ntlmUserAccount";
    public static final String NTLM_CACHE_NAME = "ntlmChallengeCache";
    public static final String OS_PROPERTYS_USER_NAME="user.name";
    public static final String TYPE_AUTH_ID_AD = "AD";
    public static final String TYPE_AUTH_ID_PASSWORD = "PASSWORD";
    public static final String CLIENT_USER_NAME="CLIENT_USER_NAME";
    /*################# control parameter key #########################*/
    public static final String SECT_ID_CTRL="CTRL";
    public static final String SECT_ID_PWD_RULE="PWD_RULE";
    public static final String SECT_ID_HSEKEEP="HSEKEEP";
    
    public static final String PARAM_ID_HELPDESK_NO="HELPDESK_NO";
    public static final String PARAM_ID_HELPDESK_EMAIL="HELPDESK_EMAIL";
    public static final String PARAM_ID_PWD_EXPIRE_DAYS="PWD_EXPIRE_DAYS";
    public static final String PARAM_ID_REPEATED_LOGIN="REPEATED_LOGIN";
    public static final String PARAM_ID_MAKER_CHECKER="MAKER_CHECKER";
    public static final String PARAM_ID_MAX_ATTEMPT_LOGIN="MAX_ATTEMPT_LOGIN";
    public static final String PARAM_ID_MAIL_EXPIRE_DAYS="EMAIL_EXPIRE_PERIOD";
    public static final String PARAM_ID_AUTO_LOGOUT = "AUTO_LOGOUT";
    public static final String PARAM_ID_LOG_FILE_PATH = "LOG_FILE_PATH";
    public static final String PARAM_ID_LOG_FILE_NAME = "LOG_FILE_NAME";
    public static final String PARAM_DEFAULT_PAGE_SIZE = "DEFAULT_PAGE_SIZE";
    public static final String PARAM_PAGE_SIZES = "PAGE_SIZES";
    public static final String PARAM_ID_MAX_USER_AMOUNT_FOR_SUPPLIER = "MAX_USER_AMOUNT_FOR_SUPPLIER";
    public static final String PARAM_ID_CURRENT_GST = "CURRENT_GST";
    public static final String PARAM_ID_NEW_GST = "NEW_GST";
    public static final String PARAM_ID_NEW_GST_FROM_DATE = "NEW_GST_FROM_DATE";
    
    /*################# password changed reason ###############*/
    public static final String CODE_FORGOT_PASSWORD="C";
    public static final String REASON_CODE_PWDCHG_NORMAL_CHANGE="A";
    public static final String CODE_FIRST_TIEM_TO_LOGIN_SYSTEM="B";
    public static final String CODE_PASSWORD_EXPIRED="D";
    public static final String CODE_MODIFY_VIA_MYPROFILE="E";
    
    /*################# password blocked reason ###############*/
    public static final String MULTIPLE_FAILED_LOGIN_ATTEMPTS="A";
    
    
    /*################# invoke action name ###############*/
    public static final String ACTION_AUTO_LOGIN = "autoLogin.action";
    public static final String ACTION_HOME = "home.action";
    public static final String ACTION_AD_LOGIN = "adLogin.action";
    
    /*################# file record type ################*/
    public static final String VERTICAL_SEPARATE = "|";
    public static final String END_LINE = "\r\n";
    
    /*################# extended field type ################*/
    public static final String EXTENDED_TYPE_BOOLEAN = "B";
    public static final String EXTENDED_TYPE_FLOAT = "F";
    public static final String EXTENDED_TYPE_INTEGER = "I";
    public static final String EXTENDED_TYPE_STRING = "S";
    public static final String EXTENDED_TYPE_DATE = "D";
    
    /*################# generate invoice ################*/
    public static final String INV_QTY_BASE_PACK="P";
    public static final String INV_QTY_BASE_UNIT="U";
    public static final String SUMMARY_DATA_FORMAT="yyyy-MM-dd HH:mm:ss";

    
    /*################# user type id ################*/
    public static final String USER_TYPE_ID_SUPPLIER_ADMIN = "SupplierAdmin";
    public static final String USER_TYPE_ID_SUPPLIER_USER = "SupplierUser";
    public static final String USER_TYPE_ID_BUYER_ADMIN = "BuyerAdmin";
    public static final String USER_TYPE_ID_BUYER_USER = "BuyerUser";
    
    /*################# request parameter ################*/
    public static final String REQUEST_PARAMTER_OID = "selectedOids";
    public static final String REQUEST_OID_DELIMITER = "\\-";
    
    /*################# summary init sort field ################*/
    public static final String SORT_ORDER_DESC = "DESC";
    public static final String SORT_FIELD_CREATE_DATE = "CREATE_DATE";
    public static final String SORT_FIELD_SENT_DATE = "SENT_DATE";
    
    /*################# mailbox folder ################*/
    public static final String DIR_IN = "in";
    public static final String DIR_OUT = "out";
    public static final String DIR_INVALID = "invalid";
    public static final String DIR_ARCHIVE = "archive";
    public static final String DIR_WORKING = "working";
    public static final String DIR_EAI = "eai";
    public static final String DIR_DOC = "doc";
    public static final String DIR_INDICATOR = "indicator";
    public static final String DIR_DISPATCHER = "dispatcher";
    public static final String DIR_PENDING = "pending";
    public static final String DIR_ON_HOLD = "on-hold";
    public static final String DIR_OUTBOUND = "outbound";
    public static final String DIR_TMP = "tmp";
    
    public static final String USER_GENDER_MALE="M";
    public static final String USER_GENDER_FEMALE="F";
    
    
    /*################# buyer store access list ################*/
    public static final String BUYER_STORE_ACCESS_LIST = "buyerStoreAccessList";
    
    /*################# report template extension for buyer store type user ################*/
    public static final String REPORT_TEMPLATE_FOR_STORE_EXTENSION = "_STORE";
    public static final String REPORT_TEMPLATE_FOR_STORE_BY_STORE_EXTENSION = "_BY_STORE_STORE";
    public static final String REPORT_TEMPLATE_FOR_SUPPLIER_EXTENSION = "_BY_STORE";
    
    /*################# report template extension for buyer store type user ################*/
    public static final String REPORT_NAME_FOR_STORE_EXTENSION = "_LOCATION";
    public static final String REPORT_NAME_FOR_STORE_BY_STORE_EXTENSION = "_LOCATION_BYLOCATION";
    public static final String REPORT_NAME_FOR_SUPPLIER_EXTENSION = "_BYLOCATION";
    
    /*################# supplier shared oid list ################*/
    public static final String SUPPLIER_SHARED_OID_LIST = "SupplierSharedOidList";
    
    /*################# buyer store ################*/
    public static final String ALL_AREAS = "All Areas";
    public static final String ALL_STORES = "All Stores";
    public static final String ALL_WARE_HOUSE = "All WareHouse";
    
    /*################# module key ################*/
    public static final String MODULE_KEY_BUYER = "module_key_buyer";
    public static final String MODULE_KEY_PO = "module_key_po";
    public static final String MODULE_KEY_INV = "module_key_inv";
    public static final String MODULE_KEY_GRN = "module_key_grn";
    public static final String MODULE_KEY_DN = "module_key_dn";
    public static final String MODULE_KEY_PN = "module_key_pn";
    public static final String MODULE_KEY_RTV = "module_key_rtv";
    public static final String MODULE_KEY_MATCH = "module_key_match";
    public static final String MODULE_KEY_GI = "module_key_gi";
    public static final String MODULE_KEY_CC = "module_key_cc";
    public static final String MODULE_KEY_JOB_CONTROL = "module_key_job_control";
    public static final String MODULE_KEY_SALES = "module_key_sales";
    public static final String MODULE_KEY_CN = "module_key_cn";
    
    
    /*################# validation pattern key ##############*/
    public static final String VLD_PTN_KEY_BUYER_CODE = "BUYER_CODE";
    public static final String VLD_PTN_KEY_SUPPLIER_CODE = "SUPPLIER_CODE";
    
    /*################# func_group ##############*/
    public static final String FUNC_GROUP_GLOBAL = "Global";
    public static final String FUNC_GROUP_DN = "Dn";
    public static final String FUNC_GROUP_MATCHING = "Matching";
    public static final String FUNC_GROUP_GRN = "GRN";
    public static final String FUNC_GROUP_GI = "GI";
    public static final String FUNC_GROUP_ITEM = "ITEM";
    public static final String FUNC_GROUP_SM = "SM";
    public static final String FUNC_GROUP_PO = "PO";
    public static final String FUNC_GROUP_PO_CONVERT_INV = "PoConvertInv";
    public static final String FUNC_GROUP_INV = "INV";
    public static final String FUNC_GROUP_DSD = "DSD";
    
    /*################# func_id ##############*/
    public static final String FUNC_ID_GLOBAL = "Global";
    public static final String FUNC_ID_BACKEND = "Backend";
    public static final String FUNC_ID_POINVGRNDN = "PoInvGrnDn";
    public static final String FUNC_ID_IMPORTING = "Importing";
    public static final String FUNC_ID_SORPO = "SorPO";
    
}
