package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.extension.ClassExHolder;

public interface ClassMapper extends BaseMapper<ClassHolder>,
        DBActionMapper<ClassHolder>
{
    List<ClassExHolder> selectClassByUserOid(BigDecimal userOid);
    
    
    List<ClassExHolder> selectTmpClassByUserOid(BigDecimal userOid);
    
    
    List<ClassExHolder> selectClassByBuyerOid(BigDecimal buyerOid);
    
    
    ClassHolder selectClassByItemCodeAndBuyerOid(Map<String, Object> map);
}
