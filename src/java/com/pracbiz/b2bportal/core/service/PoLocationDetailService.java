package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;

public interface PoLocationDetailService extends DBActionService<PoLocationDetailHolder>
{
    public List<PoLocationDetailHolder> selectPoLocationDetailsByPoOidAndLineSeqNo(
        Integer lineSeqNo, BigDecimal poOid) throws Exception;
    
    
    public List<PoLocationDetailHolder> selectPoLocationDetailsByPoOid(
        BigDecimal poOid) throws Exception;
    
    
    public List<PoLocationDetailHolder> selectPoLocationDetailByPoOidAndDetailLineSeqNo(
        Integer lineSeqNo, BigDecimal poOid) throws Exception;
    
    
    public List<PoLocationDetailHolder> selectPoLocationDetailByPoOidAndLocLineSeqNo(
        BigDecimal poOid, Integer lineSeqNo) throws Exception;
}
