package com.pracbiz.b2bportal.test.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.test.base.BaseServiceTestCase;

public class ControlParameterServiceTest extends BaseServiceTestCase
{

    private ControlParameterHolder cp1, cp2;
    
    private static final String SECT_ID1 = "SECT_ID1";
    private static final String PARAM_ID1 = "PARAM_ID1";
    private static final String SECT_ID2 = "SECT_ID2";
    private static final String PARAM_ID2 = "PARAM_ID2";
    
    @Autowired
    private OidService oidService;
    @Autowired
    private ControlParameterService controlParameterService;
    
    public ControlParameterServiceTest()
    {
        oidService = ctx.getBean("oidService", OidService.class);
        controlParameterService = ctx.getBean("controlParameterService", ControlParameterService.class);
    }
    
    
    public void testBasicCrd() throws Exception
    {
        int originalNumOfRecords = 0;
        
        List<? extends Object> records = controlParameterService.selectAllControlParameters();
        
        if (null != records && !records.isEmpty())
        {
            originalNumOfRecords = records.size();
        }
        controlParameterService.insert(cp1);
        controlParameterService.insert(cp2);
        List<? extends BaseHolder> list = controlParameterService.select(new ControlParameterHolder());
        assertEquals(originalNumOfRecords + 2, list.size());
        ControlParameterHolder rlt = controlParameterService.selectControlParameterByKey(cp1.getParamOid());
        assertNotNull(rlt);
        assertEquals(cp1.getSectId(), rlt.getSectId());
        rlt = controlParameterService.selectCacheControlParameterBySectIdAndParamId(cp1.getSectId(), cp1.getParamId());
        assertNotNull(rlt);
        assertEquals(cp1.getCatId(), rlt.getCatId());
        list = controlParameterService.selectCacheControlParametersBySectId(cp1.getSectId());
        assertNotNull(rlt);
        assertEquals(cp1.getCatId(), rlt.getCatId());
        controlParameterService.delete(cp1);
        controlParameterService.delete(cp2);
        rlt = controlParameterService.selectControlParameterByKey(cp1.getParamOid());
        assertNull(rlt);
        rlt = controlParameterService.selectControlParameterByKey(cp2.getParamOid());
        assertNull(rlt);
    }
    
    
    public void testUpdate() throws Exception
    {
        controlParameterService.insert(cp1);
        ControlParameterHolder cp = new ControlParameterHolder();
        BeanUtils.copyProperties(cp1, cp);
        cp.setParamDesc(null);
        cp.setSectId(SECT_ID2);
        controlParameterService.updateByPrimaryKeySelective(cp1, cp);
        ControlParameterHolder rlt = controlParameterService.selectControlParameterByKey(cp.getParamOid());
        assertNotNull(rlt);
        assertNotNull(rlt.getParamDesc());
        assertEquals(cp.getSectId(), rlt.getSectId());
        controlParameterService.updateByPrimaryKey(rlt, cp);
        rlt = controlParameterService.selectControlParameterByKey(cp.getParamOid());
        assertNotNull(rlt);
        assertNull(rlt.getParamDesc());
        controlParameterService.delete(rlt);
    }
    
    
    @Before
    public void setUp() throws Exception
    {
        initControlParameter();
    }
    
    public void initControlParameter() throws Exception
    {
        cp1 = new ControlParameterHolder();
        cp1.setParamOid(oidService.getOid());
        cp1.setParamDesc("PARAM_DESC1");
        cp1.setSectId(SECT_ID1);
        cp1.setParamId(PARAM_ID1);
        cp1.setCreateBy(this.getCommonParameter(false).getLoginId());
        cp1.setCreateDate(new Date());
        cp1.setValid(true);
        
        cp2 = new ControlParameterHolder();
        cp2.setParamOid(oidService.getOid());
        cp2.setParamDesc("PARAM_DESC2");
        cp2.setSectId(SECT_ID2);
        cp2.setParamId(PARAM_ID2);
        cp2.setCreateBy(this.getCommonParameter(false).getLoginId());
        cp2.setCreateDate(new Date());
        cp2.setValid(true);
    }
    
    
    private CommonParameterHolder getCommonParameter(boolean mkMode)
    {
        CommonParameterHolder cp = new CommonParameterHolder();
        cp.setCurrentUserOid(BigDecimal.ONE);
        cp.setLoginId("admin1");
        cp.setMkMode(mkMode);
        cp.setClientIp("localhost");
        
        return cp;
    }
}
