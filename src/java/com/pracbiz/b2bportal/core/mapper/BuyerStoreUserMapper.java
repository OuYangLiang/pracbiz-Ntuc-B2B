package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;

public interface BuyerStoreUserMapper extends BaseMapper<BuyerStoreUserHolder>,
        DBActionMapper<BuyerStoreUserHolder>
{
    public List<BuyerStoreUserHolder> selectStoreUserByStoreOid(
            BigDecimal storeOid);
    
    int defineRelationBetweenUserAndStore(Map<String, BigDecimal> map);
}