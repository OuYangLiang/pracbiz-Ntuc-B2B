package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.core.holder.OperationHolder;

public interface OperationMapper extends BaseMapper<OperationHolder>
{
    List<OperationHolder> selectOperationByUserType(BigDecimal userTypeOid);

    
    List<OperationHolder> selectOperationByRole(BigDecimal roleOid);

    
    List<OperationHolder> selectOperationByRoleTmp(BigDecimal roleOid);
    
    
    public List<OperationHolder> selectOperationByBuyerAndUserType(
        Map<String, BigDecimal> param);
    
    
    public List<OperationHolder> selectSupplierOperationByBuyerAndUserType(
        Map<String, BigDecimal> param);

    
    public List<OperationHolder> selectOperationBySupplierAndUserType(
        Map<String, BigDecimal> param);
    
    
    public List<OperationHolder> selectOperationByUserTypes(Map<String, List<BigDecimal>> param);
    
    
    public List<OperationHolder> selectOperationByUser(BigDecimal userOid);
    
    
    public List<OperationHolder> selectOperationSharedBetweenUserTypes(Map<String, BigDecimal> param);
}