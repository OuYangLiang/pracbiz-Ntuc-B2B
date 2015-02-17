package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.RoleOperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleOperationTmpHolder;
import com.pracbiz.b2bportal.core.mapper.RoleOperationTmpMapper;
import com.pracbiz.b2bportal.core.service.RoleOperationTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class RoleOperationTmpServiceImpl extends
    DBActionServiceDefaultImpl<RoleOperationTmpHolder> implements
    RoleOperationTmpService
{
    @Autowired
    private RoleOperationTmpMapper mapper;

    @Override
    public void insert(RoleOperationTmpHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    
    @Override
    public void updateByPrimaryKeySelective(RoleOperationTmpHolder oldObj_,
        RoleOperationTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    
    @Override
    public void updateByPrimaryKey(RoleOperationTmpHolder oldObj_, RoleOperationTmpHolder newObj_)
        throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }
    

    @Override
    public void delete(RoleOperationTmpHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    
    @Override
    public List<RoleOperationHolder> selectByRole(BigDecimal roleOid)
        throws Exception
    {
        RoleOperationTmpHolder param = new RoleOperationTmpHolder();
        param.setRoleOid(roleOid);

        List<RoleOperationTmpHolder> objRlt = this.select(param);
        List<RoleOperationHolder> rlt = new ArrayList<RoleOperationHolder>();

        if(null != objRlt)
        {
            for(RoleOperationTmpHolder obj : objRlt)
            {
                rlt.add(obj);
            }
        }

        return rlt;
    }

    
    @Override
    public List<RoleOperationTmpHolder> select(RoleOperationTmpHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

}
