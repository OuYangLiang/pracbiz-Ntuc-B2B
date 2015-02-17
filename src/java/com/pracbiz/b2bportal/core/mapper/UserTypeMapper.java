package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;

public interface UserTypeMapper extends BaseMapper<UserTypeHolder>
{
    public List<UserTypeHolder> selectPrivilegedSubUserTypesByUserType(
        BigDecimal userTypeOid);
    
    
    public List<UserTypeHolder> selectPrivilegedSubUserTypesByUserTypeInclusively(
        BigDecimal userTypeOid);
}