package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SupplierSharedHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface SupplierSharedService extends
        BaseService<SupplierSharedHolder>,
        DBActionService<SupplierSharedHolder>
{
    public List<SupplierSharedHolder> selectBySelfSupOid(BigDecimal selfSupOid)
            throws Exception;
    
    
    public List<SupplierSharedHolder> selectByGrantSupOid(BigDecimal grantSupOid)
    throws Exception;
}
