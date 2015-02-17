package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.RunningNumberHolder;
import com.pracbiz.b2bportal.core.mapper.RunningNumberMapper;
import com.pracbiz.b2bportal.core.service.RunningNumberService;

public class RunningNumberServiceImpl extends
        DBActionServiceDefaultImpl<RunningNumberHolder> implements
        RunningNumberService
{
    @Autowired
    private RunningNumberMapper mapper;
    
    @Override
    public void insert(RunningNumberHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(RunningNumberHolder oldObj_,
            RunningNumberHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(RunningNumberHolder oldObj_,
            RunningNumberHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(RunningNumberHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public String generateNumber(BigDecimal companyOid, String numberType,
            Integer length) throws Exception
    {
        if (companyOid == null || numberType == null
                || numberType.trim().isEmpty() || length == null)
        {
            throw new IllegalArgumentException();
        }
        RunningNumberHolder param = new RunningNumberHolder();
        param.setCompanyOid(companyOid);
        param.setNumberType(numberType);
        List<RunningNumberHolder> list = mapper.select(param);
        if (list == null || list.isEmpty())
        {
            param.setMaxNumber(2);
            mapper.insert(param);
            return StringUtils.leftPad("1", length, "0");
        }
        param = list.get(0);
        Integer result = param.getMaxNumber();
        param.setMaxNumber(result + 1);
        mapper.updateByPrimaryKey(param);
        return StringUtils.leftPad(result.toString(),
                length, "0");
    }

}
