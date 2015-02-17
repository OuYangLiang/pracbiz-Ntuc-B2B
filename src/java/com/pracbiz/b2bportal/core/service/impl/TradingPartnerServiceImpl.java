//*****************************************************************************
//
// File Name       :  TradingPartnerServiceImpl.java
// Date Created    :  Sep 4, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 4, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerTmpHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.mapper.GroupTradingPartnerMapper;
import com.pracbiz.b2bportal.core.mapper.GroupTradingPartnerTmpMapper;
import com.pracbiz.b2bportal.core.mapper.TradingPartnerMapper;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class TradingPartnerServiceImpl extends
    DBActionServiceDefaultImpl<TradingPartnerHolder> implements
    TradingPartnerService, ApplicationContextAware
{
    private ApplicationContext ctx;
    @Autowired
    private TradingPartnerMapper mapper;
    @Autowired
    private GroupTradingPartnerMapper groupTradingPartnerMapper;
    @Autowired
    private GroupTradingPartnerTmpMapper groupTradingPartnerTmpMapper;
    
    @Override
    public List<TradingPartnerHolder> select(TradingPartnerHolder param)
        throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(TradingPartnerHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(TradingPartnerHolder oldObj_,
        TradingPartnerHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(TradingPartnerHolder oldObj_,
        TradingPartnerHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(TradingPartnerHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    
    @Override
    public int getCountOfSummary(TradingPartnerExHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }

    
    @Override
    public List<TradingPartnerExHolder> getListOfSummary(
        TradingPartnerExHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public List<TradingPartnerExHolder> selectTradingPartnerByGroupOidAndSupplierOid(
        BigDecimal groupOid, BigDecimal supplierOid) throws Exception
    {
        if (groupOid == null || supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String,BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("groupOid", groupOid);
        param.put("supplierOid", supplierOid);
        
        return mapper.selectTradingPartnerByGroupOidAndSupplierOid(param);
    }


    @Override
    public List<TradingPartnerExHolder> selectTradingPartnerByTmpGroupOidAndSupplierOid(BigDecimal groupOid, BigDecimal supplierOid)
        throws Exception
    {
        if (groupOid == null || supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String,BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("groupOid", groupOid);
        param.put("supplierOid", supplierOid);
        
        return mapper.selectTradingPartnerByTmpGroupOidAndSupplierOid(param);
    }


    @Override
    public List<TradingPartnerHolder> selectTradingPartnerByTradingPartnerOids(
        List<BigDecimal> tradingPartnerOids) throws Exception
    {
        if (tradingPartnerOids == null || tradingPartnerOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        TradingPartnerExHolder param = new TradingPartnerExHolder();
        param.setTradingPartnerOids(tradingPartnerOids);
        
        return  mapper.select(param);
    }


    @Override
    public TradingPartnerHolder selectTradingPartnerByKey(
            BigDecimal tradingPartnerOid) throws Exception
    {
        if (tradingPartnerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        TradingPartnerExHolder param = new TradingPartnerExHolder();
        param.setTradingPartnerOid(tradingPartnerOid);
        
        List<TradingPartnerHolder> rlt = mapper.select(param);
        
        if (rlt != null && rlt.size() == 1)
        {
            return rlt.get(0);
        }
        return null;
    }


    @Override
    public List<TradingPartnerExHolder> selectTradingPartnerTradingPartnerOids(
        TradingPartnerExHolder record) throws Exception
    {
        if (record.getTradingPartnerOids() == null
            || record.getTradingPartnerOids().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectTradingPartnerTradingPartnerOids(record);
    }


    @Override
    public void removeTradingPartner(CommonParameterHolder cp,
            TradingPartnerHolder tp) throws Exception
    {
        GroupTradingPartnerTmpHolder param = new GroupTradingPartnerTmpHolder();
        param.setTradingPartnerOid(tp.getTradingPartnerOid());
        groupTradingPartnerMapper.delete(param);
        groupTradingPartnerTmpMapper.delete(param);
        
        this.getMeBean().auditDelete(cp, tp);
    }


    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private TradingPartnerService getMeBean()
    {
        return ctx.getBean("tradingPartnerService", TradingPartnerService.class);
    }


    @Override
    public TradingPartnerHolder selectByBuyerAndBuyerGivenSupplierCode(
        BigDecimal buyerOid, String buyerGivenSupplierCode) throws Exception
    {
        if(null == buyerOid || null == buyerGivenSupplierCode
            || buyerGivenSupplierCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        TradingPartnerExHolder param = new TradingPartnerExHolder();
        param.setBuyerOid(buyerOid);
        param.setBuyerSupplierCode(buyerGivenSupplierCode);
        
        List<TradingPartnerHolder> rlt = mapper.select(param);
        
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        return null;
    }


    @Override
    public List<TradingPartnerHolder> selectBySupplierOid(BigDecimal supplierOid)
        throws Exception
    {
        if (null == supplierOid )
        {
            throw new IllegalArgumentException();
        }
        
        TradingPartnerExHolder param = new TradingPartnerExHolder();
        param.setSupplierOid(supplierOid);
        
        return mapper.select(param);
    }


    @Override
    public List<TradingPartnerHolder> selectByBuyerOid(BigDecimal buyerOid)
        throws Exception
    {
        if (null == buyerOid )
        {
            throw new IllegalArgumentException();
        }
        
        TradingPartnerExHolder param = new TradingPartnerExHolder();
        param.setBuyerOid(buyerOid);
        
        return mapper.select(param);
    }


    @Override
    public List<TradingPartnerExHolder> selectTradingPartnerBySupplierOid(
            BigDecimal supplierOid) throws Exception
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String,BigDecimal> param = new HashMap<String, BigDecimal>();
        param.put("supplierOid", supplierOid);
        
        return mapper.selectTradingPartnerBySupplierOid(param);
    }


    @Override
    public List<TradingPartnerExHolder> selectBySupplierOids(
        List<BigDecimal> supplierOids) throws Exception
    {
        if (null == supplierOids || supplierOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, List<BigDecimal>> param = new HashMap<String, List<BigDecimal>>();
        param.put("supplierOids", supplierOids);
        
        return mapper.selectTradingPartnerBySupplierOids(param);
    }

}
