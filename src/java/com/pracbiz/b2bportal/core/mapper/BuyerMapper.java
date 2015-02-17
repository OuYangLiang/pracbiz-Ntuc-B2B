package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;

public interface BuyerMapper extends BaseMapper<BuyerHolder>,
    DBActionMapper<BuyerHolder>, PaginatingMapper<BuyerHolder>
{
    public List<BuyerHolder> selectBuyerBySupplierOid(BigDecimal supplierOid);
    
    List<BuyerHolder> selectWithBLOBs(BuyerHolder record);
    
    int updateByPrimaryKeyWithBLOBs(BuyerHolder record);
    
    List<BuyerHolder> selectBuyerByBuyerOids(Map<String, List<BigDecimal>> buyerOids);
    
    List<BuyerHolder> selectBuyersByGroupOid(BigDecimal groupOid);
    
    List<BuyerHolder> selectBuyersBySetOid(Map<String, BigDecimal> setOid);
    
    List<BuyerHolder> selectBuyersByGroupOidAndSupplierOid(Map<String, BigDecimal> setOid);
}