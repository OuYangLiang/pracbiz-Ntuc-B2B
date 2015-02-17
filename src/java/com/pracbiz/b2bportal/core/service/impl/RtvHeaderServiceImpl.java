//*****************************************************************************
//
// File Name       :  RtvHeaderServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-11 $
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
import com.pracbiz.b2bportal.core.constants.RtvStatus;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.RtvSummaryHolder;
import com.pracbiz.b2bportal.core.mapper.RtvHeaderMapper;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class RtvHeaderServiceImpl extends
    DBActionServiceDefaultImpl<RtvHeaderHolder> implements RtvHeaderService
{
    @Autowired
    private RtvHeaderMapper mapper;
    

    @Override
    public RtvHeaderHolder selectRtvHeaderByKey(BigDecimal rtvOid)
        throws Exception
    {
        if (rtvOid == null)
        {
            throw new IllegalArgumentException();
        }

        RtvHeaderHolder parameter = new RtvHeaderHolder();
        parameter.setRtvOid(rtvOid);

        List<RtvHeaderHolder> rlts = mapper.select(parameter);

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
        if(!(param instanceof RtvSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getCountOfSummary((RtvSummaryHolder)param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof RtvSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary((RtvSummaryHolder)param);
    }

    @Override
    public void delete(RtvHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(RtvHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(RtvHeaderHolder oldObj_,
        RtvHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(RtvHeaderHolder oldObj_,
        RtvHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public RtvHeaderHolder selectRtvHeaderByRtvNo(BigDecimal buyerOid,
        String rtvNo, String buyerGivenSupplierCode) throws Exception
    {
        List<RtvHeaderHolder> rlt = this
            .selectRtvHeadersByBuyerOidRtvNoAndSupplierCode(buyerOid, rtvNo,
                buyerGivenSupplierCode);
        
        if (null == rlt || rlt.isEmpty())
        {
            return null;
        }
        
        for (RtvHeaderHolder rtvh : rlt)
        {
            if(RtvStatus.NEW.equals(rtvh.getRtvStatus())
                || RtvStatus.AMENDED.equals(rtvh.getRtvStatus()))
            {
                return rtvh;
            }
        }
        return null;
    }

    @Override
    public List<RtvHeaderHolder> selectRtvHeadersByBuyerOidRtvNoAndSupplierCode(
        BigDecimal buyerOid, String rtvNo, String buyerGivenSupplierCode)
        throws Exception
    {
        if(null == buyerOid || null == rtvNo || rtvNo.isEmpty()
            || null == buyerGivenSupplierCode
            || buyerGivenSupplierCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        RtvHeaderHolder param = new RtvHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setRtvNo(rtvNo);
        param.setSupplierCode(buyerGivenSupplierCode);
        
        List<RtvHeaderHolder> rlt = mapper.select(param);
        if (null == rlt || rlt.isEmpty())
        {
            return null;
        }
        
        return rlt;
    }

    @Override
    public List<RtvSummaryHolder> selectAllRecordToExport(RtvSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }

}
