package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.AuditTrailHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public interface AuditTrailService extends BaseService<AuditTrailHolder>,
        DBActionService<AuditTrailHolder>, PaginatingService<AuditTrailHolder>
{
    public AuditTrailHolder selectAuditTrailWithBlobsByKey(BigDecimal auditTrailOid) throws Exception;
    

    public List<AuditTrailHolder> selectWithBLOBs(AuditTrailHolder param)
            throws Exception;
    

    public void updateByPrimaryKeyWithBLOBs(AuditTrailHolder oldObj,
            AuditTrailHolder newObj) throws Exception;
}
