package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public interface RoleOperationService extends BaseService<RoleOperationHolder>,
    DBActionService<RoleOperationHolder>
{
    public List<RoleOperationHolder> selectByRole(BigDecimal roleOid)
        throws Exception;
}
