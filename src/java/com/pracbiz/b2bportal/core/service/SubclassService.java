package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.SubclassExHolder;

public interface SubclassService extends BaseService<SubclassHolder>,
        DBActionService<SubclassHolder>
{
    public List<SubclassExHolder> selectSubclassExByUserOid(BigDecimal userOid)
            throws Exception;
    
    
    public List<SubclassExHolder> selectTmpSubclassExByUserOid(BigDecimal userOid)
            throws Exception;


    public List<SubclassExHolder> selectSubclassExByBuyerOid(BigDecimal buyerOid)
            throws Exception;
    
    
    public int deleteSubClassByBuyerOid(BigDecimal buyerOid) throws Exception;
    
    
    public SubclassHolder selectSubclassExByItemCodeAndBuyerOid(
            String buyerItemCode, BigDecimal buyerOid) throws Exception;
    
    
    public void deleteBySubclassOid(BigDecimal subclassOid) throws Exception;
    
    
    public SubclassHolder selectByClassOidSubclassCode(BigDecimal classOid, String subclassCode) throws Exception;
    
}
