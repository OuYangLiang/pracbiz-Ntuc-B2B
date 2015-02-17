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







import org.springframework.beans.factory.annotation.Autowired;







import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.mapper.RtvLocationMapper;
import com.pracbiz.b2bportal.core.report.excel.MissingGiReportParameter;
import com.pracbiz.b2bportal.core.report.excel.RtvGiDnReportParameter;
import com.pracbiz.b2bportal.core.service.RtvLocationService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author yinchi
 */
public class RtvLocationServiceImpl extends
    DBActionServiceDefaultImpl<RtvLocationHolder> implements RtvLocationService
{
    @Autowired
    private RtvLocationMapper mapper;

    @Override
    public void delete(RtvLocationHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(RtvLocationHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(RtvLocationHolder oldObj_,
        RtvLocationHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(RtvLocationHolder oldObj_,
        RtvLocationHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public List<RtvLocationHolder> selectRtvLocationByRtvOid(BigDecimal rtvOid)
        throws Exception
    {
        if (null == rtvOid)
        {
            throw new IllegalArgumentException();
        }
        
        RtvLocationHolder param = new RtvLocationHolder();
        param.setRtvOid(rtvOid);
        
        List<RtvLocationHolder> rlt = mapper.select(param);
        
        if (null != rlt && !rlt.isEmpty())
        {
            return rlt;
        }
        
        return null;
    }

    @Override
    public List<MissingGiReportParameter> selectMisssingGiReportRecords(
        BigDecimal buyerOid, String supplierCode, Date begin, Date end)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("supplierCode", supplierCode);
        map.put("begin", begin);
        map.put("end", end);
        
        return mapper.selectMissingGiReportRecords(map);
    }
    
    @Override
    public List<RtvGiDnReportParameter> selectRtvGiDnWarningReportData(BigDecimal buyerOid, Date searchDate)
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("buyerOid", buyerOid);
        param.put("searchDate", searchDate);
        
        return mapper.selectRtvGiDnWarningReportData(param);
    }

}
