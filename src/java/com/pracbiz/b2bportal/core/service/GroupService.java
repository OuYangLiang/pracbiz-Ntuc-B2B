package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.MakerCheckerService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.GroupHolder;
import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.GroupTmpExHolder;

public interface GroupService extends BaseService<GroupHolder>,
    DBActionService<GroupHolder>, MakerCheckerService<GroupHolder, GroupTmpHolder>,
    PaginatingService<GroupTmpExHolder>
{
    public List<GroupHolder> selectGroupsByUserType(BigDecimal userTypeOid)
        throws Exception;

    
    public GroupHolder selectGroupByUserOid(BigDecimal userOid)
        throws Exception;

    
    public void createGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder group) throws Exception;

    
    public GroupHolder selectGroupByKey(BigDecimal groupOid) throws Exception;

    
    public void updateGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder oldGroup, GroupTmpHolder currentGroup) throws Exception;

    
    public void approveGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder tmpGroup) throws Exception;

    
    public void rejectGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder tmpGroup) throws Exception;

    
    public void withdrawGroupProfile(CommonParameterHolder cp,
        GroupTmpHolder tmpGroup) throws Exception;

    
    public void removeGroupProfile(CommonParameterHolder commonParam,
        GroupTmpHolder oldGroup) throws Exception;
    
    
    public List<GroupHolder> selectGroupByBuyerOidAndUserTypeOid(
            BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception;
    
    
    public List<GroupHolder> selectGroupBySupplierOidAndUserTypeOid(
            BigDecimal supplierOid, BigDecimal userTypeOid) throws Exception;
    
    
    public List<GroupHolder> selectGroupByBuyerOidAndAccessUrl(
            BigDecimal buyerOid, String accessUrl) throws Exception;

}
