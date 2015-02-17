package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.extension.UserProfileExHolder;

public interface UserProfileMapper extends BaseMapper<UserProfileHolder>,
    DBActionMapper<UserProfileHolder>, PaginatingMapper<UserProfileExHolder>
{
    List<UserProfileHolder> selectUsersByTmpGroupOid(BigDecimal groupOid);
    
    
    List<UserProfileHolder> selectUsersByGroupOid(BigDecimal groupOid);
    
    
    List<UserProfileExHolder> selectUsersByTmpStoreOidAndUserTypes(Map<String, Object> param);
    
    
    List<UserProfileExHolder> selectUsersByStoreOidAndUserTypes(Map<String, Object> param);
    
    
    List<UserProfileExHolder> selectUsersByTmpAreaOidAndUserTypes(Map<String, Object> param);
    
    
    List<UserProfileExHolder> selectUsersByAreaOidAndUserTypes(Map<String, Object> param);
    
    
    List<UserProfileExHolder> selectUsersByBuyerAndUserType(
            Map<String, BigDecimal> param);
    
    
    List<UserProfileExHolder> selectUsersByBuyerAndUserTypes(
            Map<String, Object> param);
    
    
    List<UserProfileExHolder> selectPriceAuditUsersByMatchingOid(BigDecimal matchingOid);
    
    
    List<UserProfileExHolder> selectQtyAuditUsersByMatchingOid(BigDecimal matchingOid);
    
    
    List<UserProfileExHolder> selectCanCloseUsersByMatchingOid(BigDecimal matchingOid);
    
    
    List<UserProfileExHolder> selectPriceAuditUsersByDnOid(BigDecimal dnOid);
    
    
    List<UserProfileExHolder> selectQtyAuditUsersByDnOid(BigDecimal dnOid);
    
    
    List<UserProfileExHolder> selectCanCloseUsersByDnOid(BigDecimal dnOid);
    
    
    List<UserProfileExHolder> selectStoreUsersByBuyerOidAndStoreOid(Map<String, Object> map);
    
    
    List<UserProfileExHolder> selectWarehouseUsersByBuyerOidAndStoreOid(Map<String, Object> map);
}