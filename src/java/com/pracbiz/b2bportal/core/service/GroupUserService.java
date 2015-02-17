package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.GroupUserHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface GroupUserService extends BaseService<GroupUserHolder>,
    DBActionService<GroupUserHolder>
{
    public List<GroupUserHolder> selectGroupUserByUserOid(BigDecimal userOid)
        throws Exception;

    
    public List<GroupUserHolder> selectGroupUserByGroupOid(BigDecimal groupOid)
        throws Exception;
    
    
    public GroupUserHolder selectByUserOid(BigDecimal userOid)
        throws Exception;

}
