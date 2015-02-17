package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.MakerCheckerService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierAdminRolloutHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileExHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;

public interface UserProfileService extends BaseService<UserProfileHolder>,
    DBActionService<UserProfileHolder>,
    MakerCheckerService<UserProfileHolder, UserProfileTmpHolder>,
    PaginatingService<UserProfileExHolder>
{
    public UserProfileHolder getUserProfileByLoginId(String loginId) throws Exception;

    
    public void createUserProfile(CommonParameterHolder cp, String requestUrl, String clientIp, UserProfileTmpHolder user, boolean sendEmail)
            throws Exception;
    
    
    public void createUserProfileForImportSupplier(String requestUrl,
            UserProfileTmpHolder user, boolean sendEmail) throws Exception;
    
    
    public void updateMyProfile(CommonParameterHolder cp,UserProfileHolder oldUser,UserProfileTmpHolder newUser)
            throws Exception;

    
    public void updateUserProfile(CommonParameterHolder cp,
            UserProfileTmpHolder oldObj_, UserProfileTmpHolder newObj_, String requestUrl, String clientIp)
            throws Exception;
    
    
    public UserProfileHolder selectUserProfileByKey(BigDecimal userOid)
            throws Exception;


    public void removeUserProfile(CommonParameterHolder cp,
            UserProfileTmpHolder oldObj, boolean force) throws Exception;


    public void approveUserProfile(CommonParameterHolder cp, String requestUrl,
            String clientIp, UserProfileTmpHolder tmpUser) throws Exception;
    
    
    public void rejectUserProfile(CommonParameterHolder cp,
            UserProfileTmpHolder tmpUser) throws Exception;
    
    
    public void withdrawUserProfile(CommonParameterHolder cp,
            UserProfileTmpHolder tmpUser) throws Exception;


    public List<UserProfileHolder> selectUsersByTmpGroupOid(BigDecimal groupOid)
        throws Exception;
    
    
    public List<UserProfileHolder> selectUsersByGroupOid(BigDecimal groupOid)
        throws Exception;


    public List<UserProfileExHolder> selectUsersByTmpStoreOidAndUserTypes(
            BigDecimal storeOid, List<BigDecimal> userTypes) throws Exception;


    public List<UserProfileExHolder> selectUsersByStoreOidAndUserTypes(
            BigDecimal storeOid, List<BigDecimal> userTypes) throws Exception;


    public List<UserProfileExHolder> selectUsersByTmpAreaOidAndUserTypes(
            BigDecimal areaOid, List<BigDecimal> userTypes) throws Exception;


    public List<UserProfileExHolder> selectUsersByAreaOidAndUserTypes(
            BigDecimal areaOid, List<BigDecimal> userTypes) throws Exception;

    
    public List<UserProfileExHolder> selectUsersByBuyerAndUserType(
            BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception;
    
    
    public List<UserProfileExHolder> selectUsersByBuyerAndUserTypes(
            BigDecimal buyerOid, List<BigDecimal> userTypes) throws Exception;
    
    
    public void importUsers(List<UserProfileTmpExHolder> users, List<UserProfileTmpExHolder> updateUsers,
            MsgTransactionsHolder msg, TransactionBatchHolder batch) throws Exception;
    
    
    public void resetSupplierAdminPwd(List<UserProfileHolder> supplierAdmins,
            List<SupplierAdminRolloutHolder> supplierAdminRolloutList,
            TransactionBatchHolder batch, MsgTransactionsHolder msg,
            Map<BigDecimal, String> userMsgEmailMap) throws Exception;
    
    
    public List<UserProfileExHolder> selectPriceAuditUsersByMatchingOid(
            BigDecimal matchingOid) throws Exception;
    
    
    public List<UserProfileExHolder> selectQtyAuditUsersByMatchingOid(
            BigDecimal matchingOid) throws Exception;
    
    
    public List<UserProfileExHolder> selectCanCloseUsersByMatchingOid(
            BigDecimal matchingOid) throws Exception;
    
    
    public List<UserProfileExHolder> selectPriceAuditUsersByDnOid(
            BigDecimal dnOid) throws Exception;
    
    
    public List<UserProfileExHolder> selectQtyAuditUsersByDnOid(
            BigDecimal dnOid) throws Exception;
    
    
    public List<UserProfileExHolder> selectCanCloseUsersByDnOid(
            BigDecimal dnOid) throws Exception;
    
    
    public List<UserProfileExHolder> selectStoreUsersByBuyerOidAndStoreOid(BigDecimal buyerOid, String storeCode)
            throws Exception;
    
    
    public List<UserProfileExHolder> selectWarehouseUsersByBuyerOidAndStoreOid(BigDecimal buyerOid, String storeCode)
            throws Exception;
    
    
    public List<UserProfileHolder> selectUsersBySupplierOid(BigDecimal supplierOid) throws Exception;
}
