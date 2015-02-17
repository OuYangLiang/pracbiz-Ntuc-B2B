package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.mapper.TermConditionMapper;
import com.pracbiz.b2bportal.core.service.TermConditionService;

/**
 * TODO To provide an overview of this class.
 *
 * @author GeJianKui
 */
public class TermConditionServiceImpl extends
    DBActionServiceDefaultImpl<TermConditionHolder> implements
    TermConditionService
{
    @Autowired private TermConditionMapper mapper;
    
    @Override
    public List<TermConditionHolder> select(TermConditionHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(TermConditionHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(TermConditionHolder oldObj_,
        TermConditionHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(TermConditionHolder oldObj_,
        TermConditionHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(TermConditionHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<TermConditionHolder> selectTermsConditionsBySupplierOid(
        BigDecimal supplierOid) throws Exception
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        TermConditionHolder param = new TermConditionHolder();
        param.setSupplierOid(supplierOid);
        return mapper.select(param);
    }


    @Override
    public TermConditionHolder selectDefaultTermConditionBySupplierOid(
        BigDecimal supplierOid) throws Exception
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        TermConditionHolder param = new TermConditionHolder();
        param.setSupplierOid(supplierOid);
        
        List<TermConditionHolder> rlt = mapper.select(param);
        
        if (rlt != null && !rlt.isEmpty())
        {
            for (TermConditionHolder tc : rlt)
            {
                if (tc.getDefaultSelected())
                {
                    return tc;
                }
            }
        }
        
        return null; 
    }


    @Override
    public TermConditionHolder selectTermsConditionBySupplierOidAndTcCode(
            BigDecimal supplierOid, String tcCode) throws Exception
    {
        if (supplierOid == null || tcCode == null || "".equals(tcCode))
        {
            throw new IllegalArgumentException();
        }
        
        TermConditionHolder param = new TermConditionHolder();
        param.setSupplierOid(supplierOid);
        param.setTermConditionCode(tcCode);
        List<TermConditionHolder> rlt = mapper.select(param);
        if (rlt != null && rlt.size() == 1)
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public TermConditionHolder selectTermConditionByKey(BigDecimal tcOid)
            throws Exception
    {
        if (tcOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        TermConditionHolder param = new TermConditionHolder();
        param.setTermConditionOid(tcOid);
        
        List<TermConditionHolder> rlt = mapper.select(param);
        if (rlt != null && rlt.size() == 1)
        {
            return rlt.get(0);
        }
        return null;
    }

}
