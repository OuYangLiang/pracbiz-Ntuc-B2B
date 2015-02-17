package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.constants.GiStatus;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.GiSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.GiHeaderMapper;
import com.pracbiz.b2bportal.core.service.GiHeaderService;

public class GiHeaderServiceImpl extends
        DBActionServiceDefaultImpl<GiHeaderHolder> implements GiHeaderService
{
    @Autowired GiHeaderMapper mapper;
    
    @Override
    public int getCountOfSummary(MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof GiSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
    
        return mapper.getCountOfSummary((GiSummaryHolder)param);
    }


    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(MsgTransactionsExHolder param)
            throws Exception
    {
        if(!(param instanceof GiSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary(param);
    }


    @Override
    public void insert(GiHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(GiHeaderHolder oldObj_,
            GiHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(GiHeaderHolder oldObj_,
            GiHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(GiHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<GiHeaderHolder> selectGiHeadersWithoutDn(BigDecimal buyerOid)
            throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectGiHeadersWithoutDn(buyerOid);
    }


    @Override
    public GiHeaderHolder selectGiHeaderByGiNo(BigDecimal buyerOid,
        String giNo, String buyerGivenSupplierCode) throws Exception
    {
        List<GiHeaderHolder> rlt = this
            .selectGiHeadersByBuyerOidGrnNoAndSupplierCode(buyerOid, giNo,
                buyerGivenSupplierCode);

        if(null == rlt || rlt.isEmpty())
        {
            return null;
        }

        for(GiHeaderHolder gih : rlt)
        {
            if(GiStatus.NEW.equals(gih.getGiStatus())
                || GiStatus.AMENDED.equals(gih.getGiStatus()))
            {
                return gih;
            }
        }
        return null;
    }


    @Override
    public List<GiHeaderHolder> selectGiHeadersByBuyerOidGrnNoAndSupplierCode(
        BigDecimal buyerOid, String giNo, String supplierCode)
        throws Exception
    {
        if(null == buyerOid || null == giNo || giNo.isEmpty()
            || null == supplierCode || supplierCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        GiHeaderHolder param = new GiHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setGiNo(giNo);
        param.setSupplierCode(supplierCode);
        
        List<GiHeaderHolder> rlt = mapper.select(param);
        
        if (null == rlt || rlt.isEmpty())
        {
           return null; 
        }
        
        return rlt; 
    }


    @Override
    public GiHeaderHolder selectGiHeaderByKey(BigDecimal giOid) throws Exception
    {
        if (giOid == null)
        {
            throw new IllegalArgumentException();
        }

        GiHeaderHolder parameter = new GiHeaderHolder();
        parameter.setGiOid(giOid);

        List<GiHeaderHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }

        return null;
    }


    @Override
    public List<GiSummaryHolder> selectAllRecordToExport(GiSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }

}
