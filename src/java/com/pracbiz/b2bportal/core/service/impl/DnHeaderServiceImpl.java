//*****************************************************************************
//
// File Name       :  DnHeaderServiceImpl.java
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
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.DnHeaderMapper;
import com.pracbiz.b2bportal.core.service.DnHeaderService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DnHeaderServiceImpl extends
    DBActionServiceDefaultImpl<DnHeaderHolder> implements DnHeaderService
{
    @Autowired
    private DnHeaderMapper mapper;

    
    @Override
    public DnHeaderHolder selectDnHeaderByKey(BigDecimal dnOid)
        throws Exception
    {
        if (dnOid == null)
        {
            throw new IllegalArgumentException();
        }

        DnHeaderHolder parameter = new DnHeaderHolder();
        parameter.setDnOid(dnOid);

        List<DnHeaderHolder> rlts = mapper.select(parameter);

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
        if(!(param instanceof DnSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getCountOfSummary((DnSummaryHolder)param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof DnSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary((DnSummaryHolder)param);
    }

    @Override
    public void delete(DnHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(DnHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(DnHeaderHolder oldObj_,
        DnHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(DnHeaderHolder oldObj_,
        DnHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    @Override
    public DnHeaderHolder selectDnHeaderByBuyerSupplierAndInvNo(
            BigDecimal buyerOid, BigDecimal supplierOid, String invNo)
            throws Exception
    {
        if (invNo == null || invNo.trim().isEmpty() || buyerOid == null || supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        DnHeaderHolder param = new DnHeaderHolder();
        param.setInvNo(invNo);
        param.setBuyerOid(buyerOid);
        param.setSupplierOid(supplierOid);
        List<DnHeaderHolder> rlt = mapper.select(param);
        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
        return rlt.get(0);
    }


    @Override
    public List<DnHeaderHolder> select(DnHeaderHolder param) throws Exception
    {
        return mapper.select(param);
    }

    
    @Override
    public DnHeaderHolder selectDnHeaderByDnNo(String dnNo) throws Exception
    {
        if (dnNo == null || dnNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        DnHeaderHolder param = new DnHeaderHolder();
        param.setDnNo(dnNo);
        List<DnHeaderHolder> rlt = mapper.select(param);
        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
        return rlt.get(0);
    }
    
    @Override
    public List<DnHeaderHolder> selectDnHeadersByBuyerOidInvNoAndSupplierCode(
        BigDecimal buyerOid, String invNo, String supplierCode)
        throws Exception
    {
        if (null == buyerOid || null == supplierCode
            || supplierCode.trim().isEmpty() || null == invNo
            || invNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        DnHeaderHolder param = new DnHeaderHolder();
        
        param.setBuyerOid(buyerOid);
        param.setSupplierCode(supplierCode);
        param.setInvNo(invNo);
        
        List<DnHeaderHolder> rlt = mapper.select(param);
        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
        
        return rlt;
    }

    
    @Override
    public DnHeaderHolder selectDnHeaderByInvNo(BigDecimal buyerOid,
        String buyerGivenSupplierCode, String invNo) throws Exception
    {
        
        List<DnHeaderHolder> rlt = this
            .selectDnHeadersByBuyerOidInvNoAndSupplierCode(buyerOid, invNo,
                buyerGivenSupplierCode);
        
        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
        
        for (DnHeaderHolder dnHeader : rlt)
        {
            if (DnStatus.AMENDED.equals(dnHeader.getDnStatus())
                || DnStatus.NEW.equals(dnHeader.getDnStatus()))
            {
                return dnHeader;
            }
        }
        
        return null;
    }

    
    @Override
    public List<DnHeaderHolder> selectDnHeadersByBuyerOidDnNoAndSupplierCode(
        BigDecimal buyerOid, String dnNo, String supplierCode)
        throws Exception
    {
        if (null == buyerOid || null == supplierCode
            || supplierCode.trim().isEmpty() || null == dnNo
            || dnNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        DnHeaderHolder param = new DnHeaderHolder();
        
        param.setBuyerOid(buyerOid);
        param.setSupplierCode(supplierCode);
        param.setDnNo(dnNo);
        
        List<DnHeaderHolder> rlt = mapper.select(param);
        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
        
        return rlt;
    }
    
    @Override
    public DnHeaderHolder selectDnHeaderByDnNo(BigDecimal buyerOid,
        String dnNo, String buyerGivenSupplierCode) throws Exception
    {
        List<DnHeaderHolder> rlt = this
            .selectDnHeadersByBuyerOidDnNoAndSupplierCode(buyerOid, dnNo,
                buyerGivenSupplierCode);
        
        if (rlt == null || rlt.isEmpty())
        {
            return null;
        }
 
        for (DnHeaderHolder dnHeader : rlt)
        {
            if (DnStatus.AMENDED.equals(dnHeader.getDnStatus())
                || DnStatus.NEW.equals(dnHeader.getDnStatus()))
            {
                return dnHeader;
            }
        }
        
        return null;
    }

    @Override
    public List<DnHeaderHolder> selectNoDisputeDnHeadersByBuyerAndBufferingDays(
            BigDecimal buyerOid, int bufferingDays) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("bufferingDays", bufferingDays);
        
        return mapper.selectNoDisputeDnHeadersByBuyerAndBufferingDays(map);
    }

    @Override
    public List<DnHeaderHolder> selectDisputedAndAuditUnfinishedRecord(
            BigDecimal buyerOid, BigDecimal supplierOid) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        
        if (supplierOid != null)
        {
            map.put("supplierOid", supplierOid);
        }
        return mapper.selectDisputedAndAuditUnfinishedRecord(map);
    }

    @Override
    public List<DnHeaderHolder> selectClosedDnRecordsBySupplierAndTimeRange(
            BigDecimal supplierOid, Date begin, Date end) throws Exception
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("supplierOid", supplierOid);
        map.put("begin", begin);
        map.put("end", end);
        return mapper.selectClosedDnRecordsBySupplierAndTimeRange(map);
    }
    
    @Override
    public List<DnHeaderHolder> selectResolutionRecordsByBuyer(
            BigDecimal buyerOid, Date reportDate, String supplierCode, String supplierName) throws Exception
    {
        if (buyerOid == null || reportDate == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("reportDate", reportDate);
        if (supplierCode != null && !supplierCode.isEmpty())
        {
            map.put("supplierCode", supplierCode);
        }
        if (supplierName != null && !supplierName.isEmpty())
        {
            map.put("supplierName", supplierName);
        }
        return mapper.selectResolutionRecordsByBuyer(map);
    }

    @Override
    public DnHeaderHolder selectDnHeaderByRtvNo(BigDecimal buyerOid,
        String buyerGivenSupplierCode, String rtvNo) throws Exception
    {

        if (buyerOid == null || buyerGivenSupplierCode == null || rtvNo == null)
        {
            throw new IllegalArgumentException();
        }
       
        DnHeaderHolder dnHeader = new DnHeaderHolder();
        dnHeader.setBuyerOid(buyerOid);
        dnHeader.setSupplierCode(buyerGivenSupplierCode);
        dnHeader.setRtvNo(rtvNo);
        
        List<DnHeaderHolder> rlt = mapper.select(dnHeader);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }

    @Override
    public List<DnHeaderHolder> selectOutstandingRecordsByBuyer(BigDecimal buyerOid, 
        String supplierCode, String supplierName, String moreThanDays, Date reportDate)
            throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        if (supplierCode != null && !supplierCode.isEmpty())
        {
            map.put("supplierCode", supplierCode);
        }
        if (supplierName != null && !supplierName.isEmpty())
        {
            map.put("supplierName", supplierName);
        }
        if (moreThanDays != null && !moreThanDays.isEmpty())
        {
            map.put("moreThanDays", moreThanDays);
        }
        if (reportDate != null)
        {
            map.put("reportDate", reportDate);
        }
        return mapper.selectOutstandingRecordsByBuyer(map);
    }

    @Override
    public DnHeaderHolder selectEffectiveDnHeaderByOriginalFilename(
            String originalFilename) throws Exception
    {
        if (originalFilename == null || originalFilename.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("originalFilename", originalFilename);
        return mapper.selectEffectiveDnHeaderByOriginalFilename(map);
    }

    @Override
    public DnHeaderHolder selectDnHeaderByGiNo(BigDecimal buyerOid,
        String buyerGivenSupplierCode, String giNo) throws Exception
    {

        if (buyerOid == null || buyerGivenSupplierCode == null || giNo == null)
        {
            throw new IllegalArgumentException();
        }
       
        DnHeaderHolder dnHeader = new DnHeaderHolder();
        dnHeader.setBuyerOid(buyerOid);
        dnHeader.setSupplierCode(buyerGivenSupplierCode);
        dnHeader.setGiNo(giNo);
        
        List<DnHeaderHolder> rlt = mapper.select(dnHeader);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }

    @Override
    public List<DnHeaderHolder> selectExportedDnByBuyerAndSupplierAndTimeRange(
            BigDecimal buyerOid, BigDecimal supplierOid, Date begin, Date end) throws Exception
    {
        DnHeaderHolder param = new DnHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setSupplierOid(supplierOid);
        param.setExported(true);
        param.setExportedDateFrom(begin);
        param.setExportedDateTo(end);
        return select(param);
    }

    @Override
    public List<DnSummaryHolder> selectAllRecordToExport(DnSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }

}
