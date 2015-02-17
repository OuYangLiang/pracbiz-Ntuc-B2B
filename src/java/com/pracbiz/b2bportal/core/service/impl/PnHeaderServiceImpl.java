//*****************************************************************************
//
// File Name       :  PnHeaderServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:2012-12-11 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.constants.PnStatus;
import com.pracbiz.b2bportal.core.holder.PnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PnSummaryHolder;
import com.pracbiz.b2bportal.core.mapper.PnHeaderMapper;
import com.pracbiz.b2bportal.core.service.PnHeaderService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PnHeaderServiceImpl extends
    DBActionServiceDefaultImpl<PnHeaderHolder> implements PnHeaderService
{
    @Autowired
    private PnHeaderMapper mapper;
    

    @Override
    public PnHeaderHolder selectPnHeaderByKey(BigDecimal pnOid)
        throws Exception
    {
        if (pnOid == null)
        {
            throw new IllegalArgumentException();
        }

        PnHeaderHolder parameter = new PnHeaderHolder();
        parameter.setPnOid(pnOid);

        List<PnHeaderHolder> rlts = mapper.select(parameter);

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
        if(!(param instanceof PnSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getCountOfSummary((PnSummaryHolder)param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof PnSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary((PnSummaryHolder)param);
    }

    @Override
    public void delete(PnHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(PnHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PnHeaderHolder oldObj_,
        PnHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PnHeaderHolder oldObj_,
        PnHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public PnHeaderHolder selectPnHeaderByPnNo(BigDecimal buyerOid,
        String pnNo, String buyerGivenSupplierCode) throws Exception
    {
        
        List<PnHeaderHolder> rlt = this
            .selectPnHeadersByBuyerOidPnNoAndSupplierCode(buyerOid, pnNo,
                buyerGivenSupplierCode);
        
        if(null == rlt || rlt.isEmpty())
        {
            return null;
        }
        
        for (PnHeaderHolder pnh : rlt)
        {
            if(PnStatus.NEW.equals(pnh.getPnStatus())
                || PnStatus.AMENDED.equals(pnh.getPnStatus()))
            {
                return pnh;
            }
        }
        
        return null;
    }

    @Override
    public List<PnHeaderHolder> selectPnHeadersByBuyerOidPnNoAndSupplierCode(
        BigDecimal buyerOid, String pnNo, String supplierCode) throws Exception
    {
        if(null == buyerOid || null == pnNo || pnNo.isEmpty()
            || null == supplierCode
            || supplierCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        PnHeaderHolder param = new PnHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setPnNo(pnNo);
        param.setSupplierCode(supplierCode);
        
        List<PnHeaderHolder> rlt = mapper.select(param);
        if(null == rlt || rlt.isEmpty())
        {
            return null;
        }
        
        return rlt;
    }

    @Override
    public List<PnSummaryHolder> selectAllRecordToExport(PnSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }

}
