package com.pracbiz.b2bportal.customized.unity.eai.backend.job.util;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.outbound.DnDocMsg;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public abstract class DnGenerator implements CoreCommonConstants
{
    protected static final Logger log = LoggerFactory.getLogger(DnGenerator.class);
    protected BusinessRuleService businessRuleService;
    private boolean generateFlag = false;
    private DnHolder dnHolder;
    private File targetFile;
    
    
    public DnGenerator(BusinessRuleService businessRuleService)
    {
        this.businessRuleService = businessRuleService;
    }


    public void generateDn(PoHolder poHolder, InvHolder invHolder, GrnHolder grnHolder,
            BigDecimal dnOid, String expectedFormat,
            DocFileHandler<DnDocMsg, DnHolder> dnDocFileHandler, String out)
            throws Exception
    {
        DnHolder dn = new DnHolder();
        dn.setDnHeader(this.computDnHeader(dnOid, grnHolder.getHeader(), invHolder
                .getHeader()));
        dn.setDnDetail(this.computDnDetail(dnOid, dn.getDnHeader(),poHolder, invHolder, grnHolder));
        
        if (!dn.getDnDetail().isEmpty())
        {
            setGenerateFlag(true);
        }
        setDnHolder(dn);
    }

    
    public abstract boolean autoGenerateDn(BigDecimal buyerOid)
            throws Exception;

    
    public abstract DnHeaderHolder computDnHeader(BigDecimal dnOid,
            GrnHeaderHolder grnHeader, InvHeaderHolder invHeader)
            throws Exception;


    public abstract List<DnDetailExHolder> computDnDetail(BigDecimal dnOid,
            DnHeaderHolder dnHeader, PoHolder poHolder, InvHolder invHolder,
            GrnHolder grnHolder) throws Exception;
    
    
    public boolean isGenerateFlag() throws Exception
    {
        return generateFlag;
    }


    public void setGenerateFlag(boolean generateFlag) throws Exception
    {
        this.generateFlag = generateFlag;
    }


    public DnHolder getDnHolder()
    {
        return dnHolder;
    }


    public void setDnHolder(DnHolder dnHolder)
    {
        this.dnHolder = dnHolder;
    }


    public File getTargetFile()
    {
        return targetFile;
    }


    public void setTargetFile(File targetFile)
    {
        this.targetFile = targetFile;
    }
    
}
