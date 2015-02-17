//*****************************************************************************
//
// File Name       :  GrnHeaderServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-27 $
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

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.GrnStatus;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.GrnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.GrnHeaderMapper;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GrnHeaderServiceImpl extends
    DBActionServiceDefaultImpl<GrnHeaderHolder> implements GrnHeaderService
{
    @Autowired
    private GrnHeaderMapper mapper;
    

    @Override
    public GrnHeaderHolder selectGrnHeaderByKey(BigDecimal grnOid) throws Exception
    {
        if (grnOid == null)
        {
            throw new IllegalArgumentException();
        }

        GrnHeaderHolder parameter = new GrnHeaderHolder();
        parameter.setGrnOid(grnOid);

        List<GrnHeaderHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }

        return null;
    }

    @Override
    public int getCountOfSummary(MsgTransactionsExHolder param)
        throws Exception
    {
        if(!(param instanceof GrnSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getCountOfSummary((GrnSummaryHolder)param);
        
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof GrnSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary((GrnSummaryHolder)param);
        
    }

    @Override
    public void delete(GrnHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(GrnHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(GrnHeaderHolder oldObj_,
        GrnHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(GrnHeaderHolder oldObj_,
        GrnHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    @Override
    public List<GrnHeaderHolder> select(GrnHeaderHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public List<GrnHeaderHolder> selectGrnHeaderByPoNoBuyerSupplierAndStoreCode(
            String poNo, BigDecimal buyerOid, BigDecimal supplierOid,
            String receiveStoreCode) throws Exception
    {
        if (poNo == null || poNo.trim().isEmpty() || receiveStoreCode == null
                || receiveStoreCode.trim().isEmpty() || buyerOid == null
                || supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        GrnHeaderHolder param = new GrnHeaderHolder();
        param.setPoNo(poNo);
        param.setBuyerOid(buyerOid);
        param.setSupplierOid(supplierOid);
        param.setReceiveStoreCode(receiveStoreCode);
        return this.select(param);
    }
    
    
    @Override
    public List<GrnHeaderHolder> selectGrnHeadersByPoNoAndStoreCode(
        BigDecimal buyerOid, String buyerGiveSupplierCode, String poNo,
        String storeCode) throws Exception
    {
        if (null == buyerOid || null == buyerGiveSupplierCode
            || buyerGiveSupplierCode.trim().isEmpty() || null == poNo
            || poNo.trim().isEmpty() || null == storeCode
            || storeCode.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        GrnHeaderHolder param = new GrnHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setSupplierCode(buyerGiveSupplierCode);
        param.setPoNo(poNo);
        param.setReceiveStoreCode(storeCode);
        
        List<GrnHeaderHolder> grns = this.select(param);
        
        if (null == grns || grns.isEmpty())
        {
            return null;
        }
        
        for (int i = 0; i < grns.size(); i++)
        {
            GrnHeaderHolder grn = grns.get(i);
            if (GrnStatus.OUTDATED.equals(grn.getGrnStatus()))
            {
                grns.remove(i);
                i--;
            }
        }
        
        return grns.isEmpty() ? null : grns;
    }
    
    
    @Override
    public List<GrnHeaderHolder> selectGrnHeadersByPoNo(BigDecimal buyerOid,
        String buyerGiveSupplierCode, String poNo) throws Exception
    {
        if (null == buyerOid || null == buyerGiveSupplierCode
            || buyerGiveSupplierCode.trim().isEmpty() || null == poNo
            || poNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        GrnHeaderHolder param = new GrnHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setSupplierCode(buyerGiveSupplierCode);
        param.setPoNo(poNo);
        
        List<GrnHeaderHolder> grns = this.select(param);
        
        if (null == grns || grns.isEmpty())
        {
            return null;
        }
        
        for (int i = 0; i < grns.size(); i++)
        {
            GrnHeaderHolder grn = grns.get(i);
            if (GrnStatus.OUTDATED.equals(grn.getGrnStatus()))
            {
                grns.remove(i);
                i--;
            }
        }
        
        return grns.isEmpty() ? null : grns;
    }
    

    @Override
    public List<GrnHeaderHolder> selectGrnHeaderByPoNo(String poNo)
            throws Exception
    {
        if (poNo == null || poNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        GrnHeaderHolder param = new GrnHeaderHolder();
        param.setPoNo(poNo);
        return this.select(param);
    }


    @Override
    public Date selectMaxGrnReceiveDateByBuyerSupplierPoReceiveStoreCode(
            String buyerCode, String supplierCode, String poNo,
            String receiveStoreCode) throws Exception
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("buyerCode", buyerCode);
        map.put("supplierCode", supplierCode);
        map.put("poNo", poNo);
        map.put("receiveStoreCode", receiveStoreCode);
        return mapper.selectMaxGrnReceiveDateByBuyerSupplierPoReceiveStoreCode(map);
    }

    @Override
    public GrnHeaderHolder selectGrnHeaderByGrnNo(BigDecimal buyerOid,
        String grnNo, String buyerGivenSupplierCode) throws Exception
    {
        
        List<GrnHeaderHolder> rlt = this
            .selectGrnHeadersByBuyerOidGrnNoAndSupplierCode(buyerOid, grnNo,
                buyerGivenSupplierCode);

        if (null == rlt || rlt.isEmpty())
        {
           return null; 
        }
        
        for (GrnHeaderHolder gnh : rlt)
        {
            if(GrnStatus.NEW.equals(gnh.getGrnStatus())
                || GrnStatus.AMENDED.equals(gnh.getGrnStatus()))
            {
                return gnh;
            }
        }
        
        return null; 
    }

    @Override
    public List<GrnHeaderHolder> selectGrnHeadersByBuyerOidGrnNoAndSupplierCode(
        BigDecimal buyerOid, String grnNo, String supplierCode)
        throws Exception
    {
        if(null == buyerOid || null == grnNo || grnNo.isEmpty()
            || null == supplierCode || supplierCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        GrnHeaderHolder param = new GrnHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setGrnNo(grnNo);
        param.setSupplierCode(supplierCode);
        
        List<GrnHeaderHolder> rlt = mapper.select(param);
        
        if (null == rlt || rlt.isEmpty())
        {
           return null; 
        }
        
        return rlt; 
    }

    @Override
    public List<GrnHeaderHolder> selectGrnHeaderByStoreTypeUserCurrentDay(BigDecimal userOid, BigDecimal buyerOid)
        throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        Date disputeBuyerDateFrom = DateUtil.getInstance().getFirstTimeOfDay(new Date());
        Date disputeBuyerDateTo = DateUtil.getInstance().getLastTimeOfDay(new Date());
        map.put("userOid", userOid);
        map.put("buyerOid", buyerOid);
        map.put("disputeBuyerDateFrom", disputeBuyerDateFrom);
        map.put("disputeBuyerDateTo", disputeBuyerDateTo);
        
        return mapper.selectGrnHeaderByStoreTypeUser(map);
    }

    @Override
    public List<GrnSummaryHolder> selectAllRecordToExport(GrnSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }
    
}
