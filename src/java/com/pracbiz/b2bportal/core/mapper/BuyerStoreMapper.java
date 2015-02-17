package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;

public interface BuyerStoreMapper extends BaseMapper<BuyerStoreHolder>,
        DBActionMapper<BuyerStoreHolder>, PaginatingMapper<BuyerStoreHolder>
{
    List<BuyerStoreHolder> selectBuyerStoresByUserOid(BigDecimal userOid);
    
    
    List<BuyerStoreHolder> selectBuyerStoresFromTmpStoreUserByUserOid(BigDecimal userOid);
    
    
    List<BuyerStoreHolder> selectBuyerStoresWithoutAreaByBuyer(String buyerCode);
    
    
    List<BuyerStoreHolder> selectBuyerStoresWithoutAreaByBuyerAndIsWareHouse(BuyerStoreHolder param);
    
    
    List<BuyerStoreHolder> selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(Map<String, Object> map);
    
    
    List<BuyerStoreHolder> selectBuyerStoresByUserOidAndIsWareHouse(Map<String, Object> map);
    
    
    List<BuyerStoreHolder> selectBuyerStoresByBuyerAndIsWareHouseAndAreaOid(Map<String, Object> map);
    
}