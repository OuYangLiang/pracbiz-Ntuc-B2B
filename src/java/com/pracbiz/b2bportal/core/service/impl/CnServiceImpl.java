//*****************************************************************************
//
// File Name       :  CnServiceImpl.java
// Date Created    :  Feb 19, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Feb 19, 2014 7:11:19 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;


import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.CnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CnDetailHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.mapper.CnDetailExtendedMapper;
import com.pracbiz.b2bportal.core.mapper.CnDetailMapper;
import com.pracbiz.b2bportal.core.mapper.CnHeaderMapper;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.CnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.CnDetailService;
import com.pracbiz.b2bportal.core.service.CnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.CnHeaderService;
import com.pracbiz.b2bportal.core.service.CnService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;


/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CnServiceImpl extends DBActionServiceDefaultImpl<CnHolder> implements CnService, ApplicationContextAware, CoreCommonConstants
{

    @Autowired transient private MailBoxUtil mboxUtil;
    @Autowired transient private PoHeaderService poHeaderService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private CnHeaderService cnHeaderService;
    @Autowired transient private CnHeaderExtendedService cnHeaderExtendedService;
    @Autowired transient private CnDetailService cnDetailService;
    @Autowired transient private CnDetailExtendedService cnDetailExtendedService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private SupplierMsgSettingService supplierMsgSettingService;
    @Autowired transient private CustomAppConfigHelper appConfig;
    @Autowired @Qualifier("canonicalCnDocFileHandler") DocFileHandler<?, CnHolder> canonicalCnDocFileHandler;
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private SupplierService supplierService;
    @Autowired CnHeaderMapper cnHeaderMapper;
    @Autowired CnDetailMapper cnDetailMapper;
    @Autowired CnDetailExtendedMapper cnDetailExtendedMapper;
    
    private ApplicationContext context;
    
    
    
    @Override
    public void setApplicationContext(ApplicationContext context)
        throws BeansException
    {
        this.context = context;
        
    }
    
    
    public CnService getMeBean()
    {
        return context.getBean("cnService",CnService.class);
    }


    @Override
    public void insert(CnHolder newObj_) throws Exception
    {
        CnHeaderHolder header = newObj_.getHeader();
        header.setAllEmptyStringToNull();
        cnHeaderMapper.insert(header);
        
        for (CnDetailHolder detail: newObj_.getDetailList())
        {
            detail.setAllEmptyStringToNull();
            cnDetailMapper.insert(detail);
        }
        
        for (CnDetailExtendedHolder detailEx : newObj_.getDetailExtendedList())
        {
            detailEx.setAllEmptyStringToNull();
            cnDetailExtendedMapper.insert(detailEx);
        }

    }


    @Override
    public void updateByPrimaryKeySelective(CnHolder oldObj_, CnHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKey(CnHolder oldObj_, CnHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void delete(CnHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void createCn(CommonParameterHolder cp, CnHolder cn,
        MsgTransactionsHolder msg, PoHeaderHolder poHeader, BuyerHolder buyer,
        SupplierHolder supplier, PoStatus poStatus) throws Exception
    {
        this.insertCn(cp, cn, msg, poHeader, poStatus);
        
        String mailboxRoot =  mboxUtil.getSupplierMboxRoot();
        String suppMboxId = supplier.getMboxId();

        this.generateCn(cn, buyer, supplier, msg, mailboxRoot, suppMboxId);
    
    }
    
    
    private void insertCn(CommonParameterHolder cp, CnHolder cn,
        MsgTransactionsHolder msg, PoHeaderHolder poHeader, PoStatus poStatus) throws Exception
    {
        PoHeaderHolder newObj = new PoHeaderHolder();
        BeanUtils.copyProperties(poHeader, newObj);
        newObj.setPoStatus(poStatus);
        newObj.setActionDate(new Date());
        newObj.setAllEmptyStringToNull();
        poHeaderService.auditUpdateByPrimaryKey(cp, poHeader, newObj);
        
        msg.setAllEmptyStringToNull();
        msgTransactionsService.auditInsert(cp, msg);
        this.getMeBean().auditInsert(cp, cn);
    }
    
    
    private void generateCn(CnHolder cn, BuyerHolder buyer, SupplierHolder supplier, MsgTransactionsHolder msg,String mailboxRoot,String suppMboxId) throws Exception
    {
        CnHeaderHolder header = cn.getHeader();
        BuyerMsgSettingReportHolder buyerSetting = buyerMsgSettingReportService.selectByKey(
            buyer.getBuyerOid(), MsgType.CN.name(), header.getCnType().name());
        
        byte[] content = this.generatePdf(cn, buyer, supplier, msg, buyerSetting);
        String docFilePath = mailboxRoot + File.separator + suppMboxId
            + File.separator + "doc" + File.separator + DIR_OUT + File.separator
            + DateUtil.getInstance().getYearAndMonth(header.getCnDate());
        
        if (!new File(docFilePath).exists())
        {
            FileUtil.getInstance().createDir(new File(docFilePath));
        }
            
        String rptFilePath = docFilePath + File.separator + header.computePdfFilename();
        
        FileUtil.getInstance().writeByteToDisk(content,rptFilePath);
    }
    
    
    public byte[] generatePdf(CnHolder cn, BuyerHolder buyer,
        SupplierHolder supplier, MsgTransactionsHolder msg, BuyerMsgSettingReportHolder setting) throws Exception
    {
          CnHeaderHolder header = cn.getHeader();
          
          String template = null;
          
          if (setting.getCustomizedReport())
          {
              template = appConfig.getCustomizedReport(
                  header.getBuyerCode(), MsgType.CN.name(), setting.getSubType(),
                  setting.getReportTemplate());
          }
          else
          {
              template = appConfig.getStandardReport(MsgType.CN.name(), setting.getSubType(), setting.getReportTemplate());
          }
        
          @SuppressWarnings("unchecked")
          DefaultReportEngine<CnHolder> engine = context.getBean(template, DefaultReportEngine.class);
          
          ReportEngineParameter<CnHolder>  parameter = new ReportEngineParameter<CnHolder>();
          parameter.setBuyer(buyer);
          parameter.setData(cn);
          parameter.setMsgTransactions(msg);
          parameter.setSupplier(supplier);
          return engine.generateReport(parameter, DefaultReportEngine.PDF_TYPE_STANDARD);//0 means standard pdf 
    }
    
    
    @Override
    public void createAndSentCn(CommonParameterHolder cp,
        CnHolder cn, MsgTransactionsHolder msg, PoHeaderHolder poHeader,
        BuyerHolder buyer, SupplierHolder supplier, PoStatus poStatus) throws Exception
    {
        SupplierMsgSettingHolder suppSetting = supplierMsgSettingService
            .selectByKey(supplier.getSupplierOid(), MsgType.CN.name());
        if (suppSetting == null)
        {
            throw new Exception("Supplier ["+ supplier.getSupplierCode() +"] Msg Setting of MsgType CN does not config.");
        }
        String expectedFormat = suppSetting.getFileFormat();
        String mailboxRoot = mboxUtil.getSupplierMboxRoot();
        String suppMboxId = supplier.getMboxId();
        
        String fileName = canonicalCnDocFileHandler.getTargetFilename(cn, expectedFormat);
        msg.setOriginalFilename(fileName);
        
        this.insertCn(cp, cn, msg, poHeader, poStatus);
        this.generateCn(cn, buyer, supplier, msg, mailboxRoot, suppMboxId);
        
        // create cn file
        String filePath = mailboxRoot + File.separator + suppMboxId + File.separator + DIR_OUT + File.separator + fileName;
        File targetFile = new File(filePath);
        canonicalCnDocFileHandler.createFile(cn, targetFile, expectedFormat);
    }


    @Override
    public CnHolder selectByKey(BigDecimal cnOid) throws Exception
    {
        if (cnOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        CnHeaderHolder header = cnHeaderService.selectCnHeaderByKey(cnOid);
        List<CnHeaderExtendedHolder> headerExs = cnHeaderExtendedService.selectByKey(cnOid);
        List<CnDetailHolder> details = cnDetailService.selectByCnOid(cnOid);
        List<CnDetailExtendedHolder> detailExs = cnDetailExtendedService.selectByCnOid(cnOid);
        
        CnHolder cn = new CnHolder();
        cn.setHeader(header);
        cn.setHeaderExtendedList(headerExs);
        cn.setDetailList(details);
        cn.setDetailExtendedList(detailExs);
        
        return cn;
    }

}
