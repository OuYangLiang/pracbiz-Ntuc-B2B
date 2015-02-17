//*****************************************************************************
//
// File Name       :  MailBoxUtil.java
// Date Created    :  Apr 16, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Apr 16, 2013 11:55:58 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class MailBoxUtil implements CoreCommonConstants
{   
    private static final Logger log = LoggerFactory.getLogger(MailBoxUtil.class);
    
    private String buyerMboxRoot;
    private String supplierMboxRoot;
    private String channelMboxRoot;
    
    //******************
    //supplier mail box 
    //******************
    //suppliermailboxroot/supplier
    public String getSupplierMailBox(String mboxId)
    {
        return getSupplierMboxRoot() + PS + mboxId;
    }
    
    //supplier/out
    public String getSupplierOutPath(String mboxId)
    {
        return getSupplierMailBox(mboxId) + PS + DIR_OUT;
    }    
    
    //supplier/in
    public String getSupplierInPath(String mboxId)
    {
        return getSupplierMailBox(mboxId) + PS + DIR_IN;
    }
    
    //supplier/archive
    public String getSupplierArchivePath(String mboxId)
    {
        return getSupplierMailBox(mboxId) + PS + DIR_ARCHIVE;
    }
    
    //supplier/archive/in/yyyymm
    public String getFolderInSupplierArchInPath(String mboxId, String yyyymm)
    {
        return getSupplierArchivePath(mboxId) + PS + DIR_IN + PS + yyyymm;
    }
    
    //supplier/archive/out/yyyymm
    public String getFolderInSupplierArchOutPath(String mboxId, String yyyymm)
    {
        return getSupplierArchivePath(mboxId) + PS + DIR_OUT + PS + yyyymm;
    }
    
    //supplier/invalid
    public String getSupplierInvalidPath(String mboxId)
    {
        return getSupplierMailBox(mboxId) + PS + DIR_INVALID;
    }
    
    //supplier/invalid/yyyymm
    public String getFolderInSupplierInvalidPath(String mboxId, String yyyymm)
    {
        return getSupplierInvalidPath(mboxId) + PS + yyyymm;
    }
    
    //supplier/working 
    public String getSupplierWorkingPath(String mboxId)
    {
        return getSupplierMailBox(mboxId) + PS + DIR_WORKING;
    }
    
    //supplier/working/out
    public String getSupplierWorkingOutPath(String mboxId)
    {
        return getSupplierWorkingPath(mboxId) + PS + DIR_OUT;
    }
    
    //supplier/working/in
    public String getSupplierWorkingInPath(String mboxId)
    {
        return getSupplierWorkingPath(mboxId) + PS + DIR_IN;
    }
    
    //supplier/doc
    public String getSupplierDocPath(String mboxId)
    {
        return getSupplierMailBox(mboxId) + PS + DIR_DOC;
    }
    
    //supplier/doc/in/yyyymm
    public String getFolderInSupplierDocInPath(String mboxId,String yyyymm)
    {
        return getSupplierDocPath(mboxId) + PS + DIR_IN + PS +yyyymm;
    }
    
    //supplier/doc/out/yyyymm
    public String getFolderInSupplierDocOutPath(String mboxId,String yyyymm)
    {
        return getSupplierDocPath(mboxId) + PS + DIR_OUT + PS +yyyymm;
    }
    
    //supplier/eai
    public String getSupplierEaiPath(String mboxId)
    {
        return getSupplierMailBox(mboxId) + PS + DIR_EAI;
    }
    
    //supplier/eai/in
    public String getSupplierEaiInPath(String mboxId)
    {
        return getSupplierEaiPath(mboxId) + PS + DIR_IN;
    }
    
    //supplier/eai/in/pending
    public String getSupplierEaiInPendingPath(String mboxId)
    {
        return getSupplierEaiInPath(mboxId) + PS + DIR_PENDING;
    }
    
    //supplier/eai/out
    public String getSupplierEaiOutPath(String mboxId)
    {
        return getSupplierEaiPath(mboxId) + PS + DIR_OUT;
    }
    
    //suppliermailboxroot/indicator/outbound
    public String getSupplierIndiOutboundPath()
    {
        return getSupplierMboxRoot() + PS + DIR_INDICATOR + PS + DIR_OUTBOUND;
    }
    
    //supplier/tmp
    public String getSupplierTmp(String mboxId)
    {
        return getSupplierMailBox(mboxId) + PS + DIR_TMP;
    }
    
    //******************
    //buyer mail box
    //******************
    public String getBuyerMailBox(String mboxId)
    {
        return getBuyerMboxRoot() + PS + mboxId;
    }
    
    //buyer/out
    public String getBuyerOutPath(String mboxId)
    {
        return getBuyerMailBox(mboxId) + PS + DIR_OUT;
    }    
    
    //buyer/in
    public String getBuyerInPath(String mboxId)
    {
        return getBuyerMailBox(mboxId) + PS + DIR_IN;
    }
    
    //buyer/archive
    public String getBuyerArchivePath(String mboxId)
    {
        return getBuyerMailBox(mboxId) + PS + DIR_ARCHIVE;
    }
    
    //buyer/archive/out
    public String getBuyerArchOutPath(String mboxId)
    {
        return getBuyerArchivePath(mboxId) + PS + DIR_OUT;
    }
    
    //buyer/archive/out/yyyymm
    public String getFolderInBuyerArchOutPath(String mboxId, String yyyymm)
    {
        return getBuyerArchOutPath(mboxId) + PS + yyyymm;
    }
    
    //buyer/archive/in
    public String getBuyerArchInPath(String mboxId)
    {
        return getBuyerArchivePath(mboxId) + PS + DIR_IN;
    }
    
    //buyer/archive/in/yyyymm
    public String getFolderInBuyerArchInPath(String mboxId, String yyyymm)
    {
        return getBuyerArchInPath(mboxId) + PS + yyyymm;
    }
    
    //buyer/invalid
    public String getBuyerInvalidPath(String mboxId)
    {
        return getBuyerMailBox(mboxId) + PS + DIR_INVALID;
    }
    
    //buyer/invalid/yyyymm
    public String getFolderInBuyerInvalidPath(String mboxId, String yyyymm)
    {
        return getBuyerInvalidPath(mboxId) + PS + yyyymm;
    }
    
    //buyer/working 
    public String getBuyerWorkingPath(String mboxId)
    {
        return getBuyerMailBox(mboxId) + PS + DIR_WORKING;
    }
    
    //buyer/working/out
    public String getBuyerWorkingOutPath(String mboxId)
    {
        return getBuyerWorkingPath(mboxId) + PS + DIR_OUT;
    }
    
    //buyer/working/in
    public String getBuyerWorkingInPath(String mboxId)
    {
        return getBuyerWorkingPath(mboxId) + PS + DIR_IN;
    }
    
    //buyer/eai
    public String getBuyerEaiPath(String mboxId)
    {
        return getBuyerMailBox(mboxId) + PS + DIR_EAI;
    }
    
    //buyer/on-hold
    public String getBuyerOnHoldPath(String mboxId)
    {
        return getBuyerMailBox(mboxId) + PS + DIR_ON_HOLD;
    }
    
    //buyermailboxroot/indicator/outbound
    public String getBuyerIndiOutboundPath()
    {
        return getBuyerMboxRoot() + PS + DIR_INDICATOR + PS + DIR_OUTBOUND;
    }
    
    //buyermailboxroot/indicator/outbound
    public String getBuyerTmpPath(String mboxId)
    {
        return getBuyerMailBox(mboxId) + PS + DIR_TMP;
    }
    
    //*********************
    //channel mail box
    //*********************
    //channel/indicator/dispatcher
    public String getChannelIndicatorDispatcherPath()
    {
        return getChannelMboxRoot() + PS + DIR_INDICATOR + PS + DIR_DISPATCHER;
    }
    
    //channel/webportal/out
    public String getChannelOutPath(String channel)
    {
        return getChannelMboxRoot() + PS + channel + PS + DIR_OUT;
    }
    
    //channel/webportal/in
    public String getChannelInPath(String channel)
    {
        return getChannelMboxRoot() + PS + channel + PS + DIR_IN;
    }
    
    //channel/webportal/archive
    public String getChannelArchivePath(String channel)
    {
        return getChannelMboxRoot() + PS + channel + PS + DIR_ARCHIVE;
    }
    
    //*********************
    //mail box root path
    //*********************
    public String getBuyerMboxRoot()
    {
        return this.buyerMboxRoot;
    }
    
    
    public void setBuyerMboxRoot(String buyerMboxRoot)
    {
        this.buyerMboxRoot = buyerMboxRoot;
    }
    
    
    public String getSupplierMboxRoot()
    {
        return this.supplierMboxRoot;
    }
    
    
    public void setSupplierMboxRoot(String supplierMboxRoot)
    {
        this.supplierMboxRoot = supplierMboxRoot;
    }
    
    
    public String getChannelMboxRoot()
    {
        return this.channelMboxRoot;
    }
    
    
    public void setChannelMboxRoot(String channelMboxRoot)
    {
        this.channelMboxRoot = channelMboxRoot;
    }
    
    public static void main(String[] args)
    {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("E:/Workspaces/Eclipse/EC-Portal/config/core/spring/applicationContext.xml");
        MailBoxUtil mboxUtil=(MailBoxUtil)ctx.getBean("mboxUtil");
        ChannelConfigHelper channelConfig = (ChannelConfigHelper)ctx.getBean("channelConfig");
        String supplier = "1000";
        String channel = "WEBPORTAL1";
        
        String yyyymm = "201204";
        log.info(mboxUtil.getSupplierMboxRoot());
        log.info(mboxUtil.getSupplierMailBox(supplier));
        log.info(mboxUtil.getSupplierEaiPath(supplier));
        log.info(mboxUtil.getSupplierEaiInPath(supplier));
        log.info(mboxUtil.getSupplierEaiOutPath(supplier));
        log.info(mboxUtil.getSupplierInPath(supplier));
        log.info(mboxUtil.getSupplierInvalidPath(supplier));
        log.info(mboxUtil.getSupplierOutPath(supplier));
        log.info(mboxUtil.getSupplierWorkingPath(supplier));
        log.info(mboxUtil.getSupplierWorkingInPath(supplier));
        log.info(mboxUtil.getSupplierWorkingOutPath(supplier));
        log.info(mboxUtil.getSupplierEaiInPendingPath(supplier));
        log.info(mboxUtil.getSupplierDocPath(supplier));
        log.info(mboxUtil.getFolderInSupplierDocInPath(supplier, yyyymm));
        log.info(mboxUtil.getFolderInSupplierDocOutPath(supplier, yyyymm));
        log.info(mboxUtil.getSupplierArchivePath(supplier));
        log.info(mboxUtil.getFolderInSupplierArchInPath(supplier, yyyymm));
        log.info(mboxUtil.getFolderInSupplierArchOutPath(supplier, yyyymm));
        log.info(mboxUtil.getFolderInSupplierInvalidPath(supplier, yyyymm));
//        log.info(new File(mboxUtil.getSupplierMboxRoot()).isDirectory());
//        log.info(new File(mboxUtil.getSupplierMboxRoot()).isFile());
        
        
        log.info("********************************");
        String buyer = "ROB";
        log.info(mboxUtil.getBuyerMboxRoot());
        log.info(mboxUtil.getBuyerMailBox(buyer));
        log.info(mboxUtil.getBuyerArchivePath(buyer));
        log.info(mboxUtil.getBuyerArchOutPath(buyer));
        log.info(mboxUtil.getBuyerArchInPath(buyer));
        log.info(mboxUtil.getFolderInBuyerArchOutPath(buyer, yyyymm));
        log.info(mboxUtil.getFolderInBuyerArchInPath(buyer, yyyymm));
        log.info(mboxUtil.getBuyerEaiPath(buyer));
        log.info(mboxUtil.getBuyerInPath(buyer));
        log.info(mboxUtil.getBuyerOutPath(buyer));
        log.info(mboxUtil.getBuyerWorkingPath(buyer));
        log.info(mboxUtil.getBuyerWorkingOutPath(buyer));
        log.info(mboxUtil.getBuyerWorkingInPath(buyer));
        log.info(mboxUtil.getBuyerInvalidPath(buyer));
        log.info(mboxUtil.getFolderInBuyerInvalidPath(buyer, yyyymm));
        
        log.info("********************************");
        log.info(mboxUtil.getChannelMboxRoot());
        log.info(mboxUtil.getChannelInPath(channelConfig.getChannelMailbox(channel)));
        log.info(mboxUtil.getChannelOutPath(channelConfig.getChannelMailbox(channel)));
        log.info(mboxUtil.getChannelIndicatorDispatcherPath());
    }
}
