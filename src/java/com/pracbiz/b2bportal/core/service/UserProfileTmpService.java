package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */

public interface UserProfileTmpService extends
    BaseService<UserProfileTmpHolder>, DBActionService<UserProfileTmpHolder>,
    PaginatingService<UserProfileTmpExHolder>
{
    public boolean isLoginIdExist(String loginId) throws Exception;

    
    public UserProfileTmpHolder selectUserProfileTmpByKey(BigDecimal userOid)
        throws Exception;

    
    public UserProfileTmpHolder selectUserProfileTmpByLoginId(String loginId)
        throws Exception;

    
    public List<UserProfileTmpHolder> selectUsersByUserOids(List<BigDecimal> userOids)
        throws Exception;
    
    
    public List<UserProfileTmpHolder> selectUsersBySupplierOid(BigDecimal supplierOid)
        throws Exception;
}
