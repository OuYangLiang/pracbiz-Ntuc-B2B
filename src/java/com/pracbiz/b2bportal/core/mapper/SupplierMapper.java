package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;

public interface SupplierMapper extends BaseMapper<SupplierHolder>,
    DBActionMapper<SupplierHolder>, PaginatingMapper<SupplierHolder>
{
    List<SupplierHolder> selectSupplierByGroupOidAndBuyerOid(
        Map<String, BigDecimal> param);

    List<SupplierHolder> selectSupplierByTmpGroupOidAndBuyerOid(
        Map<String, BigDecimal> param);
    
    List<SupplierHolder> selectSupplierByBuyerOid(BigDecimal buyerOid);

    List<SupplierHolder> selectWithBLOBs(SupplierHolder record);
    
    int updateByPrimaryKeyWithBLOBs(SupplierHolder record);
    

    List<SupplierHolder> selectSupplierByBuyerOidAndUserOid(Map<String, BigDecimal> param);
    
    
    int updateBySupplierOid(Map<String, Object> supplierOids);
}