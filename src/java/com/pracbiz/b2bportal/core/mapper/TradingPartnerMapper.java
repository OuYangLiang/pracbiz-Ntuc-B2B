package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;

public interface TradingPartnerMapper extends BaseMapper<TradingPartnerHolder>,
    DBActionMapper<TradingPartnerHolder>,
    PaginatingMapper<TradingPartnerExHolder>
{
    List<TradingPartnerExHolder> selectTradingPartnerByTmpGroupOidAndSupplierOid(
        Map<String, BigDecimal> param);

    
    List<TradingPartnerExHolder> selectTradingPartnerByGroupOidAndSupplierOid(
        Map<String, BigDecimal> param);
    
    
    List<TradingPartnerExHolder> selectTradingPartnerBySupplierOid(
            Map<String, BigDecimal> param);

    
    List<TradingPartnerExHolder> selectTradingPartnerTradingPartnerOids(
        TradingPartnerExHolder record);
    
    
    List<TradingPartnerExHolder> selectTradingPartnerBySupplierOids(
        Map<String, List<BigDecimal>> param);
}