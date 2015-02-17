package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.ItemHolder;

public interface ItemMapper extends BaseMapper<ItemHolder>, DBActionMapper<ItemHolder>
{
    public int selectCountOfItem(ItemHolder item);
    
    
    public List<ItemHolder> selectClassAndSubclassCodeByBuyerOid(ItemHolder item);
    
    
    public List<ItemHolder> selectItemByBuyerOidAndBuyerItemCode(ItemHolder item) ;
    
    
    public List<ItemHolder> selectBuyerItemCodeByBuyerOid(BigDecimal buyerOid);
    
    
    public List<String> selectActiveItemsByUserOid(Map<String, BigDecimal> map);
}
