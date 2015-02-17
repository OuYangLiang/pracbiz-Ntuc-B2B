package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoSummaryHolder;
import com.pracbiz.b2bportal.core.mapper.PoHeaderMapper;
import com.pracbiz.b2bportal.core.service.PoHeaderService;

public class PoHeaderServiceImpl extends
    DBActionServiceDefaultImpl<PoHeaderHolder> implements PoHeaderService
{
    @Autowired
    private PoHeaderMapper mapper;

    @Override
    public int getCountOfSummary(MsgTransactionsExHolder param)
        throws Exception
    {
        if(!(param instanceof PoSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getCountOfSummary((PoSummaryHolder)param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof PoSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary((PoSummaryHolder)param);
    }

    @Override
    public void delete(PoHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(PoHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(PoHeaderHolder oldObj_,
        PoHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(PoHeaderHolder oldObj_,
        PoHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public List<PoHeaderHolder> selectPoHeadersByPoNoBuyerCodeAndSupplierCode(
            String poNo, String buyerCode, String supplierCode)
            throws Exception
    {
        if (StringUtils.isBlank(poNo) || StringUtils.isBlank(buyerCode)
                || StringUtils.isBlank(supplierCode))
        {
            throw new IllegalArgumentException();
        }

        PoHeaderHolder parameter = new PoHeaderHolder();
        parameter.setPoNo(poNo);
        parameter.setBuyerCode(buyerCode);
        parameter.setSupplierCode(supplierCode);
        parameter.setSortField("PO_DATE");
        parameter.setSortOrder("DESC");

        return mapper.select(parameter);
    }

    @Override
    public PoHeaderHolder selectPoHeaderByKey(BigDecimal docOid)
        throws Exception
    {
        if (docOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoHeaderHolder parameter = new PoHeaderHolder();
        parameter.setPoOid(docOid);
        
        List<PoHeaderHolder> poHeaders = mapper.select(parameter);
        
        if (poHeaders != null && poHeaders.size() == 1)
        {
            return poHeaders.get(0);
        }
        
        return null;
    }

    
    @Override
    public List<PoHeaderHolder> selectLocalBuyerPoHeaderNotInPoInvGrnDnMatching()
    {
        return mapper.selectBuyerLocalPoHeaderNotInPoInvGrnDnMatching();
    }
    
    
    @Override
    public List<PoHeaderHolder> selectPoHeaderWhichIsNotFullyInPoInvGrnDnMatching(
        BigDecimal buyerOid) throws Exception
    {
        if (null == buyerOid)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectPoHeaderWhichIsNotFullyInPoInvGrnDnMatching(buyerOid);
    }
    
    
    @Override
    public PoHeaderHolder selectEffectivePoHeaderByPoNo(BigDecimal buyerOid,
        String poNo, String buyerGivenSupplierCode) throws Exception
    {
        List<PoHeaderHolder> rlt = this
            .selectPoHeadersByPoNo(buyerOid, poNo,
                buyerGivenSupplierCode);

        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
        
        PoHeaderHolder header = null;
        for (PoHeaderHolder poh : rlt)
        {
            if(PoStatus.NEW.equals(poh.getPoStatus()) || PoStatus.INVOICED.equals(poh.getPoStatus()) 
                    || PoStatus.CREDITED.equals(poh.getPoStatus())
                || PoStatus.PARTIAL_INVOICED.equals(poh.getPoStatus()) || PoStatus.AMENDED.equals(poh.getPoStatus()))
            {
                header = poh;
            }
            if(PoStatus.CANCELLED.equals(poh.getPoStatus()) && header == null)
            {
                header = poh;
            }
        }
        
        return header;
    }

    
    @Override
    public List<PoHeaderHolder> selectPoHeadersByPoNo(
        BigDecimal buyerOid, String poNo, String supplierCode) throws Exception
    {
        if(buyerOid == null || null == poNo || poNo.trim().isEmpty()
            || null == supplierCode
            || supplierCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        PoHeaderHolder param = new PoHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setPoNo(poNo);
        param.setSupplierCode(supplierCode);
        
        List<PoHeaderHolder> rlt = mapper.select(param);
        
        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
        
        return rlt;
    }

    
    @Override
    public List<BaseHolder> getListOfSummaryQuickPo(PoSummaryHolder param)
        throws Exception
    {
        if(param == null)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummaryQuickPo(param);
    
    }

    
    @Override
    public List<PoHeaderHolder> selectPoHeaderToGenerateBatchInvoice(Date createDateFrom,
            Date createDateTo) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("createDateFrom", createDateFrom);
        map.put("createDateTo", createDateTo);
        
        return mapper.selectPoHeaderToGenerateBatchInvoice(map);
    }

    @Override
    public List<PoSummaryHolder> selectAllRecordToExport(PoSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }
    
}
