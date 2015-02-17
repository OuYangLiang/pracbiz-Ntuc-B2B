package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SupplierAdminRolloutHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface SupplierAdminRolloutService extends
        BaseService<SupplierAdminRolloutHolder>,
        DBActionService<SupplierAdminRolloutHolder>
{
    public SupplierAdminRolloutHolder selectByKey(BigDecimal supplierOid)
            throws Exception;
}
