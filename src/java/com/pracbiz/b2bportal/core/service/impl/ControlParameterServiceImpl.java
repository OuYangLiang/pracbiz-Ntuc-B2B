package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.mapper.ControlParameterMapper;
import com.pracbiz.b2bportal.core.service.ControlParameterService;

public class ControlParameterServiceImpl extends
    DBActionServiceDefaultImpl<ControlParameterHolder> implements
    ControlParameterService
{
    @Autowired
    private ControlParameterMapper controlParameterMapper;
    
    
    @Override
    public List<ControlParameterHolder> select(ControlParameterHolder param) throws Exception
    {
        return controlParameterMapper.select(param);
    }


    @Override
    public void insert(ControlParameterHolder newObj_) throws Exception
    {
        controlParameterMapper.insert(newObj_);
    }
    

    @Override
    public void updateByPrimaryKeySelective(ControlParameterHolder oldObj_,
        ControlParameterHolder newObj_) throws Exception
    {
        controlParameterMapper.updateByPrimaryKeySelective(newObj_);
    }

    
    @Override
    public void updateByPrimaryKey(ControlParameterHolder oldObj_,
        ControlParameterHolder newObj_) throws Exception
    {
        controlParameterMapper.updateByPrimaryKey(newObj_);
    }
    

    @Override
    public void delete(ControlParameterHolder oldObj_) throws Exception
    {
        controlParameterMapper.delete(oldObj_);
    }


    @Override
    public ControlParameterHolder selectControlParameterByKey(
        BigDecimal paramOid) throws Exception
    {
        if(paramOid == null)
        {
            throw new IllegalArgumentException();
        }

        ControlParameterHolder param = new ControlParameterHolder();
        param.setParamOid(paramOid);

        List<ControlParameterHolder> rlts = this.controlParameterMapper.select(param);
        if(rlts != null && rlts.size() == 1)
        {
            return rlts.get(0);
        }

        return null;
    }
    
    
    @Override
    public List<ControlParameterHolder> selectAllControlParameters()
        throws Exception
    {
        return controlParameterMapper.getAllControlParameters();
    }

    
    @Override
    public ControlParameterHolder selectCacheControlParameterBySectIdAndParamId(
        String sectId_, String paramId_) throws Exception
    {
        if (StringUtils.isBlank(sectId_) || StringUtils.isBlank(paramId_))
        {
            throw new IllegalArgumentException();
        }
        
        List<ControlParameterHolder> rlts = controlParameterMapper.getAllControlParameters();
        if (rlts == null || rlts.size() < 1) 
        {
            return new ControlParameterHolder();
        }
        
        for (ControlParameterHolder param : rlts)
        {
            if (sectId_.equalsIgnoreCase(param.getSectId()) && 
                    paramId_.equalsIgnoreCase(param.getParamId()))
            {
                return param;
            }
        }
        
        return new ControlParameterHolder();
    }


    @Override
    public List<ControlParameterHolder> selectCacheControlParametersBySectId(
        String sectId) throws Exception
    {
        if (StringUtils.isBlank(sectId))
        {
            throw new IllegalArgumentException();
        }
        
        List<ControlParameterHolder> controls = new ArrayList<ControlParameterHolder>();
        
        List<ControlParameterHolder> rlts = controlParameterMapper.getAllControlParameters();
        if (rlts == null || rlts.size() < 1) 
        {
            return controls;
        }
        
        
        for (ControlParameterHolder param : rlts)
        {
            if (sectId.equalsIgnoreCase(param.getSectId()))
            {
                controls.add(param);
            }
        }
        
        return controls;
    }


    @Override
    public List<ControlParameterHolder> selectControlParametersByCatId(String catId)
            throws Exception
    {
        if (StringUtils.isBlank(catId))
        {
            throw new IllegalArgumentException();
        }
        
        List<ControlParameterHolder> controls = new ArrayList<ControlParameterHolder>();
        
        List<ControlParameterHolder> rlts = controlParameterMapper.getAllControlParameters();
        if (rlts == null || rlts.size() < 1) 
        {
            return controls;
        }
        
        
        for (ControlParameterHolder param : rlts)
        {
            if (catId.equalsIgnoreCase(param.getCatId()))
            {
                controls.add(param);
            }
        }
        
        return controls;
    }


    @Override
    public ControlParameterHolder selectControlParameterByParametersBySectIdAndParamId(
        String sectId, String paramId) throws Exception
    {
        if (StringUtils.isBlank(sectId) || StringUtils.isBlank(paramId))
        {
            throw new IllegalArgumentException();
        }
        
        ControlParameterHolder param = new ControlParameterHolder();
        param.setSectId(sectId);
        param.setParamId(paramId);
        List<ControlParameterHolder> rlts = controlParameterMapper.select(param);
        if (rlts == null || rlts.size() < 1) 
        {
            return null;
        }
        
        return rlts.get(0);
    }
    
}
