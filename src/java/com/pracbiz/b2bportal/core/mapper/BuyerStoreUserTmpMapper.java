package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;

public interface BuyerStoreUserTmpMapper extends
        BaseMapper<BuyerStoreUserTmpHolder>,
        DBActionMapper<BuyerStoreUserTmpHolder>
{
    public List<BuyerStoreUserTmpHolder> selectStoreUserTmpByStoreOid(
            BigDecimal storeOid);
}