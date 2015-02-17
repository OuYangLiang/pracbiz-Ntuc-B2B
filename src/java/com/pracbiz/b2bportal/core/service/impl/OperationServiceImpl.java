package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.holder.OperationHolder;
import com.pracbiz.b2bportal.core.mapper.OperationMapper;
import com.pracbiz.b2bportal.core.service.OperationService;

public class OperationServiceImpl implements OperationService
{
    @Autowired
    private OperationMapper mapper;

    @Override
    public List<OperationHolder> select(OperationHolder param) throws Exception
    {
        return mapper.select(param);
    }

    
    @Override
    public List<OperationHolder> selectOperationByUserType(BigDecimal userTypeOid)
            throws Exception
    {
        if (null == userTypeOid)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectOperationByUserType(userTypeOid);
    }


    @Override
    public List<OperationHolder> selectOperationByRole(BigDecimal roleOid)
        throws Exception
    {
        if (null == roleOid)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectOperationByRole(roleOid);
    }
    
    
    @Override
    public List<OperationHolder> selectOperationByRoleTmp(BigDecimal roleOid)
        throws Exception
    {
        if (null == roleOid)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectOperationByRoleTmp(roleOid);
    }


    @Override
    public List<OperationHolder> selectOperationByBuyerAndUserType(
        BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception
    {
        Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("buyerOid", buyerOid);
        param.put("userTypeOid", userTypeOid);
        
        return mapper.selectOperationByBuyerAndUserType(param);
    }
    
    
    @Override
    public List<OperationHolder> selectSupplierOperationByBuyerAndUserType(
        BigDecimal buyerOid, BigDecimal userTypeOid) throws Exception
    {
        Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("buyerOid", buyerOid);
        param.put("userTypeOid", userTypeOid);
        
        return mapper.selectSupplierOperationByBuyerAndUserType(param);
    }


    @Override
    public List<OperationHolder> selectOperationBySupplierAndUserType(
        BigDecimal supplierOid, BigDecimal userTypeOid) throws Exception
    {
        Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("supplierOid", supplierOid);
        param.put("userTypeOid", userTypeOid);
        
        return mapper.selectOperationBySupplierAndUserType(param);
    }


    @Override
    public List<OperationHolder> selectOperationByUserTypes(
            List<BigDecimal> userTypes) throws Exception
    {
        Map<String, List<BigDecimal>> param = new HashMap<String, List<BigDecimal>>();
        param.put("array", userTypes);
        return mapper.selectOperationByUserTypes(param);
    }


    @Override
    public List<OperationHolder> selectOperationByUser(BigDecimal userOid)
        throws Exception
    {
        if (null == userOid)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectOperationByUser(userOid);
    }


    @Override
    public List<OperationHolder> selectOperationSharedBetweenUserTypes(
        BigDecimal userTypeOid1, BigDecimal userTypeOid2) throws Exception
    {
        if (null == userTypeOid1 || null == userTypeOid2)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("userTypeOid1", userTypeOid1);
        param.put("userTypeOid2", userTypeOid2);
        
        return mapper.selectOperationSharedBetweenUserTypes(param);
    }


}
