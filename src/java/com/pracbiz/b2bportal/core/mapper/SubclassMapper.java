package com.pracbiz.b2bportal.core.mapper;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.SubclassExHolder;

public interface SubclassMapper extends BaseMapper<SubclassHolder>,
        DBActionMapper<SubclassHolder>
{

    public int deleteByBuyerOid(BigDecimal buyerOid);

    List<SubclassExHolder> selectSubclassExByUserOid(BigDecimal userOid);
    
    
    List<SubclassExHolder> selectTmpSubclassExByUserOid(BigDecimal userOid);
    
    
    List<SubclassExHolder> selectSubclassExByBuyerOid(BigDecimal buyerOid);
    
    
    SubclassHolder selectSubclassByItemCodeAndBuyerOid(Map<String, Object> map);

}
