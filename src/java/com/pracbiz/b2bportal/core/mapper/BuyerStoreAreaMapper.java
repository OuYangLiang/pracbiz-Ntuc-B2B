package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;

public interface BuyerStoreAreaMapper extends BaseMapper<BuyerStoreAreaHolder>,
        DBActionMapper<BuyerStoreAreaHolder>
{
    List<BuyerStoreAreaHolder> selectBuyerStoreAreasByUserOid(BigDecimal userOid);
    
    
    List<BuyerStoreAreaHolder> selectBuyerStoreAreaFromTmpAreaUserByUserOid(BigDecimal userOid);

}