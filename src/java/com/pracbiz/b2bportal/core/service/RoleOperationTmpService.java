package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public interface RoleOperationTmpService extends
    BaseService<RoleOperationTmpHolder>, DBActionService<RoleOperationTmpHolder>
{
    public List<RoleOperationHolder> selectByRole(BigDecimal roleOid)
        throws Exception;
}
