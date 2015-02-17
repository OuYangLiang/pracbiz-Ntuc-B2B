package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.ItemBarcodeHolder;

public interface ItemBarcodeMapper extends BaseMapper<ItemBarcodeHolder>,
        DBActionMapper<ItemBarcodeHolder>
{
      public int deleteItemByBuyerOid(BigDecimal buyerOid);
}