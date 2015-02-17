package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */

public interface UserTypeService extends BaseService<UserTypeHolder>
{
    public UserTypeHolder selectByKey(BigDecimal userTypeOid) throws Exception;

    
    public List<UserTypeHolder> selectPrivilegedSubUserTypesByUserType(
        BigDecimal userTypeOid) throws Exception;
    
    public List<UserTypeHolder> selectPrivilegedSubUserTypesByUserTypeInclusively(
        BigDecimal userTypeOid) throws Exception;

    public Boolean checkOperateEquativeUserType(BigDecimal userTypeOid)
            throws Exception;
    
}
