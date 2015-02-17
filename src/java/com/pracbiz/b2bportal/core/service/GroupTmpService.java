package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;

public interface GroupTmpService extends BaseService<GroupTmpHolder>,
    DBActionService<GroupTmpHolder>
{
    public List<GroupTmpHolder> selectGroupsByUserType(BigDecimal userTypeOid)
        throws Exception;

    
    public GroupTmpHolder selectGroupTmpByUserOid(BigDecimal userOid)
        throws Exception;

    
    
    public GroupTmpHolder selectGroupTmpByKey(BigDecimal groupOid)
        throws Exception;

    
    public boolean isGroupIdExist(String groupId, BigDecimal companyOid,
        boolean isBuyer) throws Exception;

    
    public List<GroupTmpHolder> selectGroupBySupplierOidAndUserTypeOid(
            BigDecimal supplierOid, BigDecimal userTypeOid) throws Exception;
}
