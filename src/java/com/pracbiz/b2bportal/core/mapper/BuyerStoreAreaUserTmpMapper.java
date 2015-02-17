package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;

public interface BuyerStoreAreaUserTmpMapper extends BaseMapper<BuyerStoreAreaUserTmpHolder>,
        DBActionMapper<BuyerStoreAreaUserTmpHolder>
{
    List<BuyerStoreAreaUserTmpHolder> selectBuyerStoreAreaFromTmpAreaUserByUserOidAndAreaOid(
        Map<String, BigDecimal> map);
}