//*****************************************************************************
//
// File Name       :  PoLocationServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2012-12-11$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.mapper.PoLocationMapper;
import com.pracbiz.b2bportal.core.report.excel.MissingGrnReportParameter;
import com.pracbiz.b2bportal.core.service.PoLocationService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PoLocationServiceImpl extends
    DBActionServiceDefaultImpl<PoLocationHolder> implements PoLocationService
{
    @Autowired
    private PoLocationMapper mapper;

    @Override
    public void delete(PoLocationHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(PoLocationHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PoLocationHolder oldObj_,
        PoLocationHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PoLocationHolder oldObj_,
        PoLocationHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public List<PoLocationHolder> selectLocationsByPoOid(BigDecimal poOid)
        throws Exception
    {
        if (poOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoLocationHolder parameter = new PoLocationHolder();
        parameter.setPoOid(poOid);
        
        return mapper.select(parameter);
    }
    
    
    @Override
    public List<PoLocationHolder> selectOptionalLocations(PoLocationHolder parameter) throws Exception
    {
        return mapper.selectOptionalLocations(parameter);
    }
    
    
    @Override
    public List<PoLocationHolder> selectOptionalLocationsByPoOid(
        BigDecimal poOid) throws Exception
    {
        if (poOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoLocationHolder parameter = new PoLocationHolder();
        parameter.setPoOid(poOid);
        
        return this.selectOptionalLocations(parameter);
    }

    @Override
    public List<PoLocationHolder> selectLocationsByPoOidAndStoreCode(
        BigDecimal poOid, String locationCode) throws Exception
    {
        if (poOid == null || StringUtils.isBlank(locationCode))
        {
            throw new IllegalArgumentException();
        }
        
        PoLocationHolder parameter = new PoLocationHolder();
        parameter.setPoOid(poOid);
        parameter.setLocationCode(locationCode);
        
        List<PoLocationHolder> rlts = mapper.select(parameter);
        
        if (rlts != null )
        {
            return rlts;
        }
        
        return null;
    }

    @Override
    public PoLocationHolder selectLocationByPoOidAndLineSeqNo(BigDecimal poOid,
        int lineSeqNo) throws Exception
    {
        if (poOid == null || 0 == lineSeqNo)
        {
            throw new IllegalArgumentException();
        }
        
        PoLocationHolder parameter = new PoLocationHolder();
        parameter.setPoOid(poOid);
        parameter.setLineSeqNo(lineSeqNo);
        
        List<PoLocationHolder> rlts = mapper.select(parameter);
        
        if (rlts != null && rlts.size() == 1)
        {
            return rlts.get(0);
        }
        
        return null;
    }

    @Override
    public List<MissingGrnReportParameter> selectMissingGrnReprotRecords(
            BigDecimal buyerOid, String supplierCode, Date begin, Date end)
            throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("supplierCode", supplierCode);
        map.put("begin", begin);
        map.put("end", end);
        
        return mapper.selectMissingGrnReprotRecords(map);
    }

}
