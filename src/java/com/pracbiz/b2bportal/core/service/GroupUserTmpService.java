package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.GroupUserTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface GroupUserTmpService extends BaseService<GroupUserTmpHolder>,
    DBActionService<GroupUserTmpHolder>
{
    public List<GroupUserTmpHolder> selectGroupUserTmpByUserOid(
        BigDecimal userOid) throws Exception;

    public List<GroupUserTmpHolder> selectGroupUserTmpByGroupOid(
        BigDecimal groupOid) throws Exception;

}
