package com.pracbiz.b2bportal.core.mapper;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileTmpExHolder;

public interface UserProfileTmpMapper extends BaseMapper<UserProfileTmpHolder>,
    DBActionMapper<UserProfileTmpHolder>,
    PaginatingMapper<UserProfileTmpExHolder>
{

}