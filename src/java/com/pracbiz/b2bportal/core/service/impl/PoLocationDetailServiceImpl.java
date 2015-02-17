package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.mapper.PoLocationDetailMapper;
import com.pracbiz.b2bportal.core.service.PoLocationDetailService;

public class PoLocationDetailServiceImpl extends
    DBActionServiceDefaultImpl<PoLocationDetailHolder> implements
    PoLocationDetailService
{
    @Autowired transient private PoLocationDetailMapper mapper;
    
    @Override
    public void insert(PoLocationDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PoLocationDetailHolder oldObj_,
        PoLocationDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(PoLocationDetailHolder oldObj_,
        PoLocationDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(PoLocationDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    
    @Override
    public List<PoLocationDetailHolder> selectPoLocationDetailsByPoOidAndLineSeqNo(
        Integer lineSeqNo, BigDecimal poOid) throws Exception
    {
        if (poOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoLocationDetailHolder parameter = new PoLocationDetailHolder();
        parameter.setLocationLineSeqNo(lineSeqNo);
        parameter.setPoOid(poOid);
        
        return mapper.select(parameter);
    }

    
    @Override
    public List<PoLocationDetailHolder> selectPoLocationDetailsByPoOid(
        BigDecimal poOid) throws Exception
    {
        if (poOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoLocationDetailHolder parameter = new PoLocationDetailHolder();
        parameter.setPoOid(poOid);
        
        return mapper.select(parameter);
    }

    @Override
    public List<PoLocationDetailHolder> selectPoLocationDetailByPoOidAndDetailLineSeqNo(
        Integer lineSeqNo, BigDecimal poOid) throws Exception
    {

        if (poOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoLocationDetailHolder parameter = new PoLocationDetailHolder();
        parameter.setDetailLineSeqNo(lineSeqNo);
        parameter.setPoOid(poOid);
        
        return mapper.select(parameter);
    }

    
    @Override
    public List<PoLocationDetailHolder> selectPoLocationDetailByPoOidAndLocLineSeqNo(
        BigDecimal poOid, Integer lineSeqNo) throws Exception
    {
        if (poOid == null || lineSeqNo == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoLocationDetailHolder parameter = new PoLocationDetailHolder();
        parameter.setPoOid(poOid);
        parameter.setLocationLineSeqNo(lineSeqNo);
        
        return mapper.select(parameter);
    }
    
    

}
