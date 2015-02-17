package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierRoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.extension.RoleTmpExHolder;
import com.pracbiz.b2bportal.core.mapper.RoleTmpMapper;
import com.pracbiz.b2bportal.core.mapper.SupplierRoleTmpMapper;
import com.pracbiz.b2bportal.core.service.RoleTmpService;

public class RoleTmpServiceImpl extends
    DBActionServiceDefaultImpl<RoleTmpHolder> implements RoleTmpService
{
    @Autowired private RoleTmpMapper mapper;
    @Autowired private SupplierRoleTmpMapper supplierRoleTmpMapper;

    @Override
    public List<RoleTmpHolder> select(RoleTmpHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public RoleTmpHolder selectAdminRoleById(String roleId) throws Exception
    {
        if (null == roleId)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectAdminRoleById(roleId);
    }


    @Override
    public RoleTmpHolder selectBuyerRoleById(BigDecimal buyerOid, String roleId)
            throws Exception
    {
        RoleTmpExHolder param = new RoleTmpExHolder();
        param.setBuyerOid(buyerOid);
        param.setRoleId(roleId);
        
        List<RoleTmpHolder> rlt = this.select(param);
        if (null != rlt && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public RoleTmpHolder selectSupplierRoleById(BigDecimal supplierOid,
            String roleId) throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleId", roleId);
        param.put("supplierOid", supplierOid);
        
        return mapper.selectSupplierRoleById(param);
    }

    
    @Override
    public List<RoleTmpHolder> selectRolesByUserType(BigDecimal userTypeOid) throws Exception
    {
        if (userTypeOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RoleTmpExHolder param = new RoleTmpExHolder();
        param.setUserTypeOid(userTypeOid);
        
        return this.select(param);
    }


    @Override
    public RoleTmpHolder selectRoleByKey(BigDecimal roleOid) throws Exception
    {
        RoleTmpExHolder param = new RoleTmpExHolder();
        param.setRoleOid(roleOid);
        
        List<RoleTmpHolder> rlt = this.select(param);
        if (null != rlt && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public List<RoleTmpHolder> selectRolesByUserOid(BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectRolesByUserOid(userOid);
    }


    @Override
    public void insert(RoleTmpHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(RoleTmpHolder oldObj_,
        RoleTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(RoleTmpHolder oldObj_, RoleTmpHolder newObj_)
        throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(RoleTmpHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<RoleTmpHolder> selectRolesByRoleOids(List<BigDecimal> roleOids)
        throws Exception
    {
        //???
        return null;
    }
    
    
    public List<BigDecimal> selectSupplierOidsByRoleOid(BigDecimal roleOid)
        throws Exception
    {
        SupplierRoleTmpHolder param = new SupplierRoleTmpHolder();
        param.setRoleOid(roleOid);
        
        List<SupplierRoleTmpHolder> srList = supplierRoleTmpMapper.select(param);
        
        if (srList == null || srList.isEmpty())
        {
            return null;
        }
        
        List<BigDecimal> rlt = new ArrayList<BigDecimal>();
        
        for (SupplierRoleTmpHolder sr : srList)
        {
            rlt.add(sr.getSupplierOid());
        }
        
        return rlt;
    }

}
