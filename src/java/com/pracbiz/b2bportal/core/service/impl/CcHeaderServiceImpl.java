//*****************************************************************************
//
// File Name       :  CcHeaderServiceImpl.java
// Date Created    :  2013-12-24
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2013-12-24 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;


import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.constants.CcStatus;
import com.pracbiz.b2bportal.core.holder.CcHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.CcSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.CcHeaderMapper;
import com.pracbiz.b2bportal.core.service.CcHeaderService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CcHeaderServiceImpl extends
    DBActionServiceDefaultImpl<CcHeaderHolder> implements CcHeaderService
{
    @Autowired
    private CcHeaderMapper mapper;
    

    @Override
    public CcHeaderHolder selectCcHeaderByKey(BigDecimal invOid)
        throws Exception
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }

        CcHeaderHolder parameter = new CcHeaderHolder();
        parameter.setInvOid(invOid);

        List<CcHeaderHolder> rlts = mapper.select(parameter);

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
        if(!(param instanceof CcSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getCountOfSummary((CcSummaryHolder)param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof CcSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary((CcSummaryHolder)param);
    }

    @Override
    public void delete(CcHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(CcHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(CcHeaderHolder oldObj_,
        CcHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(CcHeaderHolder oldObj_,
        CcHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    @Override
    public List<CcHeaderHolder> select(CcHeaderHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public CcHeaderHolder selectCcHeaderByInvNo(String invNo)
            throws Exception
    {
        if (invNo == null || invNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        CcHeaderHolder param = new CcHeaderHolder();
        param.setInvNo(invNo);
        List<CcHeaderHolder> result = this.select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        return result.get(0);
    }
    

    @Override
    public List<CcHeaderHolder> selectCcHeaderByBuyerSupplierPoNoAndStore(
            String buyerCode, String buyerSupplierCode, String poNo,
            String storeCode) throws Exception
    {
        if (buyerCode == null || buyerCode.trim().isEmpty()
                || buyerSupplierCode == null
                || buyerSupplierCode.trim().isEmpty() || poNo == null
                || poNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        CcHeaderHolder param = new CcHeaderHolder();
        param.setBuyerCode(buyerCode);
        param.setSupplierCode(buyerSupplierCode);
        param.setPoNo(poNo);
        param.setStoreCode(storeCode);
        return select(param);
    }

    @Override
    public CcHeaderHolder selectEffectiveCcHeaderByInNo(BigDecimal buyerOid,
        String buyerSupplierCode, String invNo) throws Exception
    {
        if(buyerOid == null || buyerSupplierCode == null
            || buyerSupplierCode.trim().isEmpty() || invNo == null
            || invNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        CcHeaderHolder param = new CcHeaderHolder();
        param.setInvNo(invNo);
        param.setBuyerOid(buyerOid);
        param.setSupplierCode(buyerSupplierCode);
        
        List<CcHeaderHolder> result = mapper.select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        
        for (CcHeaderHolder rlt : result)
        {
            if (CcStatus.NEW.equals(rlt.getCcStatus()) || CcStatus.AMENDED.equals(rlt.getCcStatus()))
            {
                return rlt;
            }
        }
        
        return null;
    }

    @Override
    public List<CcSummaryHolder> selectAllRecordToExport(CcSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }

}
