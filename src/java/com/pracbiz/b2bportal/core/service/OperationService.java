package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.core.holder.OperationHolder;

public interface OperationService extends BaseService<OperationHolder>
{
    public List<OperationHolder> selectOperationByUserType(
        BigDecimal userTypeOid) throws Exception;
    
    
    public List<OperationHolder> selectOperationByBuyerAndUserType(
        BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception;
    
    
    public List<OperationHolder> selectSupplierOperationByBuyerAndUserType(
        BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception;

    public List<OperationHolder> selectOperationBySupplierAndUserType(
        BigDecimal supplierOid, BigDecimal userTypeOid) throws Exception;

    
    public List<OperationHolder> selectOperationByRole(BigDecimal roleOid)
        throws Exception;

    
    public List<OperationHolder> selectOperationByRoleTmp(BigDecimal roleOid)
        throws Exception;
    

    public List<OperationHolder> selectOperationByUserTypes(
            List<BigDecimal> userTypes) throws Exception;
    
    
    public List<OperationHolder> selectOperationByUser(BigDecimal userOid)
        throws Exception;
    
    
    public List<OperationHolder> selectOperationSharedBetweenUserTypes(
        BigDecimal userTypeOid1, BigDecimal userTypeOid2) throws Exception;
}
