package com.pracbiz.b2bportal.core.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.eai.file.canonical.CnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.extension.CnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.CnHeaderMapper;
import com.pracbiz.b2bportal.core.service.CnHeaderService;
import com.pracbiz.b2bportal.core.service.CnService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public class CnHeaderServiceImpl extends
    DBActionServiceDefaultImpl<CnHeaderHolder> implements CnHeaderService, 
    ApplicationContextAware, CoreCommonConstants
{
    @Autowired CnHeaderMapper mapper;
    ApplicationContext ctx;
    @Autowired CnDocFileHandler canonicalCnDocFileHandler;
    @Autowired CnService cnService;
    @Autowired SupplierMsgSettingService supplierMsgSettingService;
    @Autowired MailBoxUtil mboxUtil;
    @Autowired SupplierService supplierService;
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private CnHeaderService getMeBean()
    {
        return ctx.getBean("cnHeaderService", CnHeaderService.class);
    }
    
    @Override
    public List<CnHeaderHolder> select(CnHeaderHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public int getCountOfSummary(MsgTransactionsExHolder param)
            throws Exception
    {
        return mapper.getCountOfSummary(param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
            MsgTransactionsExHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }

    @Override
    public void insert(CnHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(CnHeaderHolder oldObj_,
            CnHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(CnHeaderHolder oldObj_,
            CnHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(CnHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public CnHeaderHolder selectEffectiveCnHeaderByKey(BigDecimal buyerOid,
            String buyerGivenSupplierCode, String cnNo) throws Exception
    {
        if (buyerOid == null || buyerGivenSupplierCode == null 
                || buyerGivenSupplierCode.trim().isEmpty() || cnNo == null 
                || cnNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        CnHeaderHolder param = new CnHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setSupplierCode(buyerGivenSupplierCode);
        param.setCnNo(cnNo);
        
        List<CnHeaderHolder> list = select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        
        for (CnHeaderHolder rlt : list)
        {
            if (CnStatus.NEW.equals(rlt.getCtrlStatus()) 
                    || CnStatus.SUBMIT.equals(rlt.getCtrlStatus()))
            {
                return rlt;
            }
        }
        
        return null;
    }

    @Override
    public CnHeaderHolder selectCnHeaderByKey(BigDecimal cnOid)
            throws Exception
    {
        if (cnOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        CnHeaderHolder param = new CnHeaderHolder();
        param.setCnOid(cnOid);
        
        List<CnHeaderHolder> list = select(param);
        
        if (list == null || list.isEmpty())
        {
            return null;
        }
        
        return list.get(0);
    }

    @Override
    public CnHeaderHolder selectEffectiveCnHeaderByPoNo(BigDecimal buyerOid,
        String buyerGivenSupplierCode, String poNo) throws Exception
    {
        if (buyerOid == null || buyerGivenSupplierCode == null 
                || buyerGivenSupplierCode.trim().isEmpty() || poNo == null 
                || poNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        CnHeaderHolder param = new CnHeaderHolder();
        param.setBuyerOid(buyerOid);
        param.setSupplierCode(buyerGivenSupplierCode);
        param.setPoNo(poNo);
        
        List<CnHeaderHolder> list = select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        
        for (CnHeaderHolder rlt : list)
        {
            if (CnStatus.NEW.equals(rlt.getCtrlStatus()) 
                    || CnStatus.SUBMIT.equals(rlt.getCtrlStatus()))
            {
                return rlt;
            }
        }
        
        return null;
    }

    @Override
    public void sendCreditNote(CommonParameterHolder cp, BigDecimal cnOid)
            throws Exception
    {
        if (cnOid == null)
        {
            throw new IllegalArgumentException();
        }
        CnHolder cn = cnService.selectByKey(cnOid);
        
        CnHeaderHolder cnHeader = cn.getHeader();
        
        CnHeaderHolder newCnHeader = new CnHeaderHolder();
        
        BeanUtils.copyProperties(cnHeader, newCnHeader);
        
        newCnHeader.setCtrlStatus(CnStatus.SUBMIT);
        
        getMeBean().auditUpdateByPrimaryKey(cp, cnHeader, newCnHeader);
        
        SupplierHolder supplier = supplierService.selectSupplierByKey(cnHeader.getSupplierOid());
        
        SupplierMsgSettingHolder setting = supplierMsgSettingService.selectByKey(supplier.getSupplierOid(), MsgType.CN.name());
        
        if (setting == null)
        {
            throw new Exception("Can not obtain Credit Note msg setting for supplier " + cnHeader.getSupplierCode());
        }
        
        String targetFilename = canonicalCnDocFileHandler.getTargetFilename(cn, setting.getFileFormat());
        File targetFile = new File(mboxUtil.getSupplierOutPath(supplier.getMboxId()) + PS + targetFilename);
        canonicalCnDocFileHandler.createFile(cn, targetFile, setting.getFileFormat());
    }


    @Override
    public List<CnSummaryHolder> selectAllRecordToExport(CnSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }
    
}
