//*****************************************************************************
//
// File Name       :  NewPoInvGrnDnMatchingJob.java
// Date Created    :  Jul 30, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Jul 30, 2013 11:20:55 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.backend;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.constants.GrnStatus;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingBuyerStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingInvStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.DocSubclassHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.mapper.DocSubclassMapper;
import com.pracbiz.b2bportal.core.report.excel.MatchingReportParameter;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;
import com.pracbiz.b2bportal.core.service.GrnService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingDetailService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class NewPoInvGrnDnMatchingJob extends BaseJob implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory
        .getLogger(NewPoInvGrnDnMatchingJob.class);

    private static final String ID = "[NewPoInvGrnDnMatchingJob]";
    
    
    private BuyerService buyerService;
    private PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    private PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService;
    private PoService poService;
    private PoHeaderService poHeaderService;
    private InvoiceService invoiceService;
    private InvHeaderService invHeaderService;
    private PoLocationService poLocationService;
    private GrnService grnService;
    private GrnHeaderService grnHeaderService;
    private DnService dnService;
    private DnHeaderService dnHeaderService;
    private BusinessRuleService businessRuleService;
    private SupplierService supplierService;
    private StandardEmailSender standardEmailSender;
    private MailBoxUtil mboxUtil;
    private MsgTransactionsService msgTransactionsService;
    private PoInvGrnDnMatchingDetailService poInvGrnDnMatchingDetailService;
    private BuyerStoreService buyerStoreService;
    private ItemService itemService;
    private DocSubclassMapper docSubclassMapper;
    
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        poInvGrnDnMatchingService = this.getBean("poInvGrnDnMatchingService", PoInvGrnDnMatchingService.class);
        poInvGrnDnMatchingGrnService = this.getBean("poInvGrnDnMatchingGrnService", PoInvGrnDnMatchingGrnService.class);
        poService = this.getBean("poService", PoService.class);
        poHeaderService = this.getBean("poHeaderService", PoHeaderService.class);
        invoiceService = this.getBean("invoiceService", InvoiceService.class);
        invHeaderService = this.getBean("invHeaderService", InvHeaderService.class);
        poLocationService = this.getBean("poLocationService", PoLocationService.class);
        grnService = this.getBean("grnService", GrnService.class);
        grnHeaderService = this.getBean("grnHeaderService", GrnHeaderService.class);
        dnService = this.getBean("dnService", DnService.class);
        dnHeaderService = this.getBean("dnHeaderService", DnHeaderService.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
        supplierService = this.getBean("supplierService", SupplierService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        msgTransactionsService = this.getBean("msgTransactionsService", MsgTransactionsService.class);
        poInvGrnDnMatchingDetailService = this.getBean("poInvGrnDnMatchingDetailService", PoInvGrnDnMatchingDetailService.class);
        buyerStoreService = this.getBean("buyerStoreService", BuyerStoreService.class);
        itemService = this.getBean("itemService", ItemService.class);
        docSubclassMapper = this.getBean("docSubclassMapper", DocSubclassMapper.class);
    }
    
    
    @Override
    protected void process()
    {
        try
        {
            synchronized(SupplierMasterImportJob.lock)
            {
                while (SupplierMasterImportJob.isAnyJobRunning)
                {
                    try
                    {
                        SupplierMasterImportJob.lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
                
                SupplierMasterImportJob.isAnyJobRunning = true;
            }
            
            realProcess();
        }
        finally
        {
            synchronized (SupplierMasterImportJob.lock)
            {
                SupplierMasterImportJob.isAnyJobRunning = false;

                SupplierMasterImportJob.lock.notifyAll();
            }
        }
    
    }
    
    
    private void realProcess()
    {
        log.info("Start to process.");
        
        List<BuyerHolder> buyers = null;
        
        try
        {
            buyers = buyerService.selectActiveBuyers();
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return;
        }
        
        for (BuyerHolder buyer : buyers)
        {
            if (!buyer.getDeploymentMode().equals(DeploymentMode.LOCAL))
            {
                continue;
            }
            
            try
            {
                boolean skipMatching = businessRuleService.isSkipMatching(buyer.getBuyerOid());
                if (skipMatching)
                {
                    continue;
                }
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
                continue;
            }
            
            log.info("Start to process for buyer" + this.formatBuyerName(buyer) + ".");
            
            this.processBuyer(buyer);
            
            log.info("Buyer" + this.formatBuyerName(buyer) + " processed.");
        }
        
        log.info("Process ended.");
    
    }
    
    
    private void processBuyer(BuyerHolder buyer)
    {
        List<BusinessRuleHolder> businessRuleList = null;
        int expiryDays = 0;
        int toleranceDays = 0;
        try
        {
            expiryDays = businessRuleService.selectGlobalMatchingJobMinBufferingDays(buyer.getBuyerOid());
            toleranceDays = businessRuleService.selectGlobalMatchingJobMaxBufferingDays(buyer.getBuyerOid());
            
            businessRuleList = businessRuleService
                    .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                            buyer.getBuyerOid(), "Matching", "PoInvGrnDn");
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
                        
        this.transferNewDocsIntoMatchingTable(buyer);
        
        this.handleDuplicateInvIntoMatchingTable(buyer);
        
        this.deleteOutdatePoMatchingRecord(buyer);
        
        this.handleDocsRelationships(buyer, businessRuleList, toleranceDays);
        
        this.performMatching(buyer, businessRuleList, expiryDays, toleranceDays);
    }
    
    
    private void transferNewDocsIntoMatchingTable(BuyerHolder buyer)
    {
        log.info("Move documents from separate tables to matching table for buyer" + this.formatBuyerName(buyer) + ".");
        
        List<PoHeaderHolder> newPoHeaders = null;
        
        try
        {
            newPoHeaders = poHeaderService.selectPoHeaderWhichIsNotFullyInPoInvGrnDnMatching(buyer.getBuyerOid());
            
            if (null == newPoHeaders || newPoHeaders.isEmpty())
            {
                log.info("Find 0 POs to move to matching table for buyer"+this.formatBuyerName(buyer) + ", skip");
                return;
            }
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        log.info("Find "+ newPoHeaders.size() + " POs to move to matching table for buyer"+this.formatBuyerName(buyer));
        for (PoHeaderHolder poHeader : newPoHeaders)
        {
            try
            {
                SupplierHolder supplier = supplierService.selectSupplierByKey(poHeader.getSupplierOid());
                if (supplier.getLiveDate() == null || DateUtil.getInstance().compareDate(supplier.getLiveDate(), poHeader.getPoDate()) > 0)
                {
                   log.info("Supplier[" + supplier.getSupplierCode() + "] is not alive or po date is earlier than live date, PO[" + poHeader.getPoNo() + "], skip");
                   continue;
                }
                
                boolean isIgnoreExpiryDate = businessRuleService.isIgnoreExpiryDate(buyer.getBuyerOid());
                
                if (!isIgnoreExpiryDate && poHeader.getExpiryDate() != null 
                    && (new Date()).after(DateUtil.getInstance().getLastTimeOfDay(poHeader.getExpiryDate())))
                {
                    log.info("PO[" + poHeader.getPoDate() + "] is expired, skip");
                    continue;
                }
                
                this.transferNewDocIntoMatchingTable(poHeader, buyer);
            }
            catch(Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }
    
    
    private void transferNewDocIntoMatchingTable(PoHeaderHolder poHeader, BuyerHolder buyer) throws Exception
    {
        List<RandomAccessFile> randomFileList = new ArrayList<RandomAccessFile>();
        List<FileChannel> channelList = new ArrayList<FileChannel>();
        List<FileLock> lockList = new ArrayList<FileLock>();
        
        List<PoInvGrnDnMatchingHolder> matchingList = new ArrayList<PoInvGrnDnMatchingHolder>();
        PoHolder poHolder = poService.selectPoByKey(poHeader.getPoOid());
        if ("1".equals(poHeader.getPoSubType()))
        {
            List<PoLocationHolder> poLocations = poLocationService
                .selectLocationsByPoOid(poHeader.getPoOid());
            
            if (null == poLocations || poLocations.isEmpty())
            {
                throw new Exception("Error location info for PO: buyer oid ["
                    + poHeader.getBuyerOid() + "], supplier  code ["
                    + poHeader.getSupplierCode() + ",  po no [" + poHeader.getPoNo()
                    + "].");
            }
            
            for (PoLocationHolder poLocation : poLocations)
            {
                if (poHolder.amtOfStore(poLocation.getLocationCode()).compareTo(BigDecimal.ZERO) == 0)
                {
                    log.info("Amount is 0 of PO [" + poHeader.getPoNo() + "] store [" + poLocation.getLocationCode() + "], treat it as free of charge, skip");
                    continue;
                }
                
                PoInvGrnDnMatchingHolder oldObj = poInvGrnDnMatchingService
                    .selectByPoOidAndStore(poHeader.getPoOid(),poLocation.getLocationCode());
                
                if (null != oldObj)
                {
                    log.info("PO [" + poHeader.getPoNo() + "] store [" + poLocation.getLocationCode() + "] has already in matching table, skip");
                    continue;
                }
                
                List<GrnHeaderHolder> grnHeaders = grnHeaderService
                    .selectGrnHeadersByPoNoAndStoreCode(poHeader.getBuyerOid(),
                        poHeader.getSupplierCode(), poHeader.getPoNo(), poLocation.getLocationCode());
                
                
                if (null == grnHeaders || grnHeaders.isEmpty())
                {
                    log.info("Can not find GRNs for PO [" + poHeader.getPoNo() + "] store [" + poLocation.getLocationCode() + "], skip");
                    continue;
                }
                List<GrnHolder> grnHolderList = new ArrayList<GrnHolder>();
                for (GrnHeaderHolder grnHeader : grnHeaders)
                {
                    grnHolderList.add(grnService.selectByKey(grnHeader.getGrnOid()));
                }
                
                InvHeaderHolder invHeader = invHeaderService
                    .selectInvHeaderByPoOidAndStoreCode(poHeader.getPoOid(),
                        poLocation.getLocationCode());
                
                InvHolder invHolder = null;
                //when do matching, user can not do void for this invoice.
                if (invHeader != null)
                {
                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(invHeader.getInvOid());
                    if (msg == null || msg.getExchangeFilename() == null || msg.getExchangeFilename().isEmpty())
                    {
                        log.info("Invoice for PO [" + poHeader.getPoNo() + "] store [" + poLocation.getLocationCode() + "] is under processing, skip");
                        continue;
                    }
                    File invFile = new File(mboxUtil.getBuyerTmpPath(buyer.getMboxId()), msg.getExchangeFilename());
                    if (invFile.exists())
                    {
                        RandomAccessFile input = null;
                        input = new RandomAccessFile(invFile, "rw");
                        randomFileList.add(input);
                        FileChannel channel = input.getChannel();
                        channelList.add(channel);
                        FileLock lock = channel.tryLock();
                        if (lock != null)
                        {
                            invHolder = invoiceService.selectInvoiceByKey(invHeader.getInvOid());
                            lockList.add(lock);
                        }
                    }
                }
                
                
                
                DnHolder dnHolder = null;
                if (null != invHeader)
                {
                    DnHeaderHolder dnHeader = dnHeaderService
                        .selectDnHeaderByInvNo(poHeader.getBuyerOid(),
                            poHeader.getSupplierCode(), invHeader.getInvNo());
                    
                    if (dnHeader != null && dnHeader.getSentToSupplier())
                    {
                        dnHolder = dnService.selectDnByKey(dnHeader.getDnOid());
                    }
                }
                
                String storeName = poLocation.getLocationName();
                if (storeName == null || storeName.trim().isEmpty())
                {
                    BuyerStoreHolder store = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(poHeader.getBuyerCode(), poLocation.getLocationCode());
                    if (store != null)
                    {
                        storeName = store.getStoreName();
                    }
                }
                PoInvGrnDnMatchingHolder matching = new PoInvGrnDnMatchingHolder(
                        poHolder, poLocation.getLocationCode(), storeName, poHolder.amtOfStore(poLocation.getLocationCode()), 
                        invHolder, grnHolderList, dnHolder);
                
                matchingList.add(matching);
            }
        }
        else if ("2".equals(poHeader.getPoSubType()))
        {
            if (poHeader.getTotalCost().compareTo(BigDecimal.ZERO) == 0)
            {
                return;
            }
            List<GrnHeaderHolder> grnHeaders = grnHeaderService
                .selectGrnHeadersByPoNo(poHeader.getBuyerOid(),
                    poHeader.getSupplierCode(), poHeader.getPoNo());
            
            if (null == grnHeaders || grnHeaders.isEmpty())
            {
                return;
            }
            List<GrnHolder> grnHolderList = new ArrayList<GrnHolder>();
            for (GrnHeaderHolder grnHeader : grnHeaders)
            {
                grnHolderList.add(grnService.selectByKey(grnHeader.getGrnOid()));
            }
            
            
            InvHeaderHolder invHeader = invHeaderService
                .selectInvHeaderByPoOid(poHeader.getPoOid());
            InvHolder invHolder = null;
            DnHolder dnHolder = null;
            //when do matching, user can not do void for this invoice.
            if (invHeader != null)
            {
                MsgTransactionsHolder msg = msgTransactionsService.selectByKey(invHeader.getInvOid());
                File invFile = new File(mboxUtil.getBuyerTmpPath(buyer.getMboxId()), msg.getExchangeFilename());
                if (invFile.exists())
                {
                    RandomAccessFile input = null;
                    input = new RandomAccessFile(invFile, "rw");
                    randomFileList.add(input);
                    FileChannel channel = input.getChannel();
                    channelList.add(channel);
                    FileLock lock = channel.tryLock();
                    if (lock != null)
                    {
                        invHolder = invoiceService.selectInvoiceByKey(invHeader.getInvOid());
                        lockList.add(lock);
                    }
                }
                DnHeaderHolder dnHeader = dnHeaderService
                        .selectDnHeaderByInvNo(poHeader.getBuyerOid(),
                                poHeader.getSupplierCode(), invHeader.getInvNo());
                
                if (dnHeader != null && dnHeader.getSentToSupplier())
                {
                    dnHolder = dnService.selectDnByKey(dnHeader.getDnOid());
                }
            }
            
            String storeName = poHeader.getShipToName();
            if (storeName == null || storeName.trim().isEmpty())
            {
                BuyerStoreHolder store = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(poHeader.getBuyerCode(), poHeader.getShipToCode());
                if (store != null)
                {
                    storeName = store.getStoreName();
                }
            }
            PoInvGrnDnMatchingHolder matching = new PoInvGrnDnMatchingHolder(
                poHolder, poHeader.getShipToCode(), storeName, poHeader.getTotalCost(),
                invHolder, grnHolderList, dnHolder);
            
            matchingList.add(matching);
        }
        else
        {
            throw new Exception("Unknown po sub type value for PO: buyer oid ["
                + poHeader.getBuyerOid() + "], supplier code ["
                + poHeader.getSupplierCode() + ", po no [" + poHeader.getPoNo()
                + "].");
        }
        
        for (PoInvGrnDnMatchingHolder matching : matchingList)
        {
            for (PoInvGrnDnMatchingGrnHolder grn : matching.getGrnList() )
            {
                if (null == grn.getGrnAmt() || BigDecimal.ZERO.compareTo(grn.getGrnAmt()) == 0)
                    grn.setGrnAmt(grnService.computeGrnAmtByGrnOidAndPoOid(grn.getGrnOid(), matching.getPoOid()));
            }
            
            log.info("Creating matching records: " + this.formatMatchingDesc(matching));
            this.initAcceptFlag(matching, buyer);
            poInvGrnDnMatchingService.createNewMatchingRecord(matching);
        }
        
        for (FileLock lock : lockList)
        {
            if (lock != null)
            {
                lock.release();
            }
        }
        for (FileChannel channel : channelList)
        {
            if (channel != null)
            {
                channel.close();
            }
        }
        for (RandomAccessFile file : randomFileList)
        {
            if (file != null)
            {
                file.close();
            }
        }
    }
    
    
    private void handleDuplicateInvIntoMatchingTable(BuyerHolder buyer)
    {
        log.info("Handling duplicate invoice of existing matching records for buyer" + this.formatBuyerName(buyer) + ".");
        List<PoInvGrnDnMatchingHolder> existingMatchingRecords = null;
        try
        {
            existingMatchingRecords = poInvGrnDnMatchingService.selectVoidInvoiceMatchingRecords(buyer.getBuyerOid());
            
            if (existingMatchingRecords == null || existingMatchingRecords.isEmpty())
            {
                return;
            }
            log.info("[" + existingMatchingRecords.size() + "] matching records found.");
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        for (PoInvGrnDnMatchingHolder oldMatching : existingMatchingRecords)
        {
            RandomAccessFile input = null;
            FileChannel channel = null;
            FileLock lock = null;
            try
            {
                InvHeaderHolder effectiveInv = invHeaderService.selectEffectiveInvHeaderByBuyerSupplierPoNoAndStore(oldMatching.getBuyerCode(), 
                        oldMatching.getSupplierCode(), oldMatching.getPoNo(), oldMatching.getPoStoreCode());
                if (effectiveInv == null || !effectiveInv.getInvStatus().equals(InvStatus.SUBMIT))
                {
                    continue;
                }
                
                //when do matching, user can not do void for this invoice.
                MsgTransactionsHolder msg = msgTransactionsService.selectByKey(effectiveInv.getInvOid());
                if (msg == null || msg.getExchangeFilename() == null || msg.getExchangeFilename().isEmpty())
                {
                    continue;
                }
                File invFile = new File(mboxUtil.getBuyerTmpPath(buyer.getMboxId()), msg.getExchangeFilename());
                if (invFile.exists())
                {
                    input = new RandomAccessFile(invFile, "rw");
                    channel = input.getChannel();
                    lock = channel.tryLock();
                    if (lock == null)
                    {
                        continue;
                    }
                }
                
                log.info("Find duplicate invoice of record: " + this.formatMatchingDesc(oldMatching));
                
                PoHolder po = poService.selectPoByKey(oldMatching.getPoOid());
                
                InvHolder inv = invoiceService.selectInvoiceByKey(effectiveInv.getInvOid());
                
                List<GrnHolder> grnList = new ArrayList<GrnHolder>();
                List<PoInvGrnDnMatchingGrnHolder> matchingGrnList = poInvGrnDnMatchingGrnService.selectByMatchOid(oldMatching.getMatchingOid());
                for (PoInvGrnDnMatchingGrnHolder matchingGrn : matchingGrnList)
                {
                    GrnHolder grn = grnService.selectByKey(matchingGrn.getGrnOid());
                    grnList.add(grn);
                }
                
                PoInvGrnDnMatchingHolder matching = new PoInvGrnDnMatchingHolder(
                        po, oldMatching.getPoStoreCode(), oldMatching.getPoStoreName(), po.getPoHeader().getTotalCost(),
                        inv, grnList, null);
                    
                
                for (PoInvGrnDnMatchingGrnHolder grn : matching.getGrnList() )
                {
                    if (null == grn.getGrnAmt() || BigDecimal.ZERO.compareTo(grn.getGrnAmt()) == 0)
                        grn.setGrnAmt(grnService.computeGrnAmtByGrnOidAndPoOid(grn.getGrnOid(), matching.getPoOid()));
                }
                
                List<PoInvGrnDnMatchingDetailExHolder> matchingDetailList = poInvGrnDnMatchingDetailService.selectByMatchingOid(oldMatching.getMatchingOid());
                SupplierHolder supplier = supplierService.selectSupplierByKey(oldMatching.getSupplierOid());
                InvHolder currentInv = po.toInvoice(supplier, oldMatching.getInvNo(), oldMatching.getPoStoreCode(), null, null, null);
                
                this.resetInvDetail(currentInv.getDetails(), matchingDetailList);
                
                boolean flag = false;
                for (InvDetailHolder currentDetail : currentInv.getDetails())
                {
                    BigDecimal expUnitPrice = currentDetail.getUnitPrice() == null ? BigDecimal.ZERO : currentDetail.getUnitPrice();
                    BigDecimal expQty = currentDetail.getInvQty() == null ? BigDecimal.ZERO : currentDetail.getInvQty();
                    if (flag)
                    {
                        break;
                    }
                    for (InvDetailHolder newDetail : inv.getDetails())
                    {
                        BigDecimal unitPrice = newDetail.getUnitPrice() == null ? BigDecimal.ZERO : newDetail.getUnitPrice();
                        BigDecimal qty = newDetail.getInvQty() == null ? BigDecimal.ZERO : newDetail.getInvQty();
                        
                        if (currentDetail.getBuyerItemCode().equalsIgnoreCase(newDetail.getBuyerItemCode())
                            && (expUnitPrice.compareTo(unitPrice) != 0
                                || expQty.compareTo(qty) != 0))
                        {
                            matching.setRevised(false);
                            flag = true;
                            break;
                        }
                        else
                        {
                            matching.setRevised(true);
                            matching.setRevisedDate(new Date());
                        }
                    }
                }
                
                log.info("Creating matching records: " + this.formatMatchingDesc(matching));
                oldMatching.setMatchingStatus(PoInvGrnDnMatchingStatus.OUTDATED);
                oldMatching.setClosed(true);
                oldMatching.setClosedBy(CoreCommonConstants.SYSTEM);
                oldMatching.setClosedDate(new Date());
                poInvGrnDnMatchingService.updateByPrimaryKeySelective(null, oldMatching);
                this.initAcceptFlag(matching, buyer);
                poInvGrnDnMatchingService.createNewMatchingRecord(matching);
            }
            catch(Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
            finally
            {
                try
                {
                    if (lock != null)
                    {
                        lock.release();
                        lock = null;
                    }
                    
                    if (channel != null)
                    {
                        channel.close();
                        channel = null;
                    }
                    
                    if (input != null)
                    {
                        input.close();
                        input = null;
                    }
                }
                catch (IOException e)
                {
                    String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                    standardEmailSender.sendStandardEmail(ID, tickNo, e);
                }
            }
        }
    }
    
    
    private void resetInvDetail(List<InvDetailHolder> invDetailList, List<PoInvGrnDnMatchingDetailExHolder> matchingDetailList)
    {
        for (InvDetailHolder invDetail : invDetailList)
        {
            BigDecimal originalQty = invDetail.getInvQty();
            for (PoInvGrnDnMatchingDetailHolder matchingDetail : matchingDetailList)
            {
                if (invDetail.getBuyerItemCode().equalsIgnoreCase(matchingDetail.getBuyerItemCode()))
                {
                    if (PoInvGrnDnMatchingPriceStatus.ACCEPTED.equals(matchingDetail.getPriceStatus()))
                    {
                        invDetail.setUnitPrice(matchingDetail.getInvPrice());
                    }
                    
                    if (PoInvGrnDnMatchingQtyStatus.ACCEPTED.equals(matchingDetail.getQtyStatus()))
                    {
                        invDetail.setInvQty(matchingDetail.getInvQty());
                    }
                    else
                    {
                        if (null == matchingDetail.getInvPrice() || BigDecimal.ZERO.compareTo(matchingDetail.getInvPrice()) == 0)
                        {
                            invDetail.setInvQty(matchingDetail.getInvQty());
                        }
                        else
                        {
                            invDetail.setInvQty(matchingDetail.getGrnQty());
                        }
                    }
                }
            }
            
            BigDecimal qty = invDetail.getInvQty() == null ? BigDecimal.ZERO : invDetail.getInvQty();
            BigDecimal price = invDetail.getUnitPrice() == null ? BigDecimal.ZERO : invDetail.getUnitPrice();
            invDetail.setItemAmount(qty.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP));
            if (originalQty == null || BigDecimal.ZERO.compareTo(originalQty) == 0)
            {
                invDetail.setDiscountAmount(BigDecimal.ZERO);
            }
            else
            {
                BigDecimal discountPercent = qty.divide(originalQty, 2, BigDecimal.ROUND_HALF_UP);
                invDetail.setDiscountAmount(invDetail.getDiscountAmount().multiply(discountPercent).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            invDetail.setNetAmount(invDetail.getItemAmount());
            
        }
    }
    
    
    private void deleteOutdatePoMatchingRecord(BuyerHolder buyer)
    {
        log.info("Handling delete outdate po of existing matching records for buyer" + this.formatBuyerName(buyer) + ".");
        List<PoInvGrnDnMatchingHolder> existingMatchingRecords = null;
        try
        {
            existingMatchingRecords = poInvGrnDnMatchingService.selectOutDatedPoRecords(buyer.getBuyerOid());
            
            if (existingMatchingRecords == null || existingMatchingRecords.isEmpty())
            {
                return;
            }
            log.info("[" + existingMatchingRecords.size() + "] matching records found.");
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        for (PoInvGrnDnMatchingHolder oldMatching : existingMatchingRecords)
        {
            log.info("Find outdate po of record: " + this.formatMatchingDesc(oldMatching));
            try
            {
                poInvGrnDnMatchingService.deleteRecordByKey(oldMatching.getMatchingOid());
            }
            catch(Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
        
    }
    
    
    private void handleDocsRelationships(BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList, int toleranceDays)
    {
        log.info("Handling document relationships of existing matching records for buyer" + this.formatBuyerName(buyer) + ".");
        
        List<PoInvGrnDnMatchingHolder> existingMatchingRecords = null;
        
        try
        {
            existingMatchingRecords = poInvGrnDnMatchingService
                .selectMatchingRecordsWhichCanDoMatching(buyer.getBuyerOid(), 0, toleranceDays);
            
            if (null == existingMatchingRecords || existingMatchingRecords.isEmpty())
            {
                return;
            }
            
            log.info("[" + existingMatchingRecords.size() + "] matching records found.");
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        for (PoInvGrnDnMatchingHolder oldMatching : existingMatchingRecords)
        {
            log.info("Handling document relationships of record: " + this.formatMatchingDesc(oldMatching));
            RandomAccessFile input = null;
            FileChannel channel = null;
            FileLock lock = null;
            try
            {
                boolean outdatedFlag = this.removeOutdatedDocFromExistingMatchingRecords(oldMatching);
                
                boolean newComingFlag = this.addInNewlyComingDocsToExistingMatchingRecord(oldMatching);
                
                if (outdatedFlag || newComingFlag)
                {
                    PoHolder po = poService.selectPoByKey(oldMatching.getPoOid());
                    
                    InvHolder inv = null; 
                    if (oldMatching.getInvOid() != null)
                    {
                      //when do matching, user can not do void for this invoice.
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(oldMatching.getInvOid());
                        if (msg == null || msg.getExchangeFilename() == null || msg.getExchangeFilename().isEmpty())
                        {
                            continue;
                        }
                        File invFile = new File(mboxUtil.getBuyerTmpPath(buyer.getMboxId()), msg.getExchangeFilename());
                        if (invFile.exists())
                        {
                            input = new RandomAccessFile(invFile, "rw");
                            channel = input.getChannel();
                            lock = channel.tryLock();
                            if (lock != null)
                            {
                                inv = invoiceService.selectInvoiceByKey(oldMatching.getInvOid());
                            }
                        }
                    }
                    
                    List<GrnHolder> grnList = new ArrayList<GrnHolder>();
                    for (PoInvGrnDnMatchingGrnHolder matchingGrn : oldMatching.getGrnList())
                    {
                        grnList.add(grnService.selectByKey(matchingGrn.getGrnOid()));
                    }
                    
                    DnHolder dn = null;
                    if (oldMatching.getDnOid() != null)
                    {
                        dn = dnService.selectDnByKey(oldMatching.getDnOid());
                    }
                    
                    PoInvGrnDnMatchingHolder matching = new PoInvGrnDnMatchingHolder(
                            po, oldMatching.getPoStoreCode(), oldMatching.getPoStoreName(), po.amtOfStore(oldMatching.getPoStoreCode()),
                            inv, grnList, dn);
                    
                    matching.setMatchingOid(oldMatching.getMatchingOid());
                    this.initAcceptFlag(matching, buyer);
                    poInvGrnDnMatchingService.updateDocRelationshipsForMatchingRecord(matching);
                    
                    if (oldMatching.getMatchingDate() !=  null)
                    {
                        SupplierHolder supplier = supplierService.selectSupplierByKey(matching.getSupplierOid());
                        
                        MatchingReportParameter report = new MatchingReportParameter(
                                matching, po, inv, grnList,
                                dn, businessRuleList, supplier.getSupplierSource()
                                .name(), true);
                        
                        matching.setMatchingStatus(report.getMatchingResult().getMatchingStatus());
                        matching.setMatchingDate(new Date());
                        
                        processAfterDoMatching(matching, businessRuleList, buyer);
                    }
                    
                    this.initAcceptFlag(matching, buyer);
                    poInvGrnDnMatchingService.updateDocRelationshipsForMatchingRecord(matching);
                }
                
            }
            catch(Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
            finally
            {
                try
                {
                    if (lock != null)
                    {
                        lock.release();
                        lock = null;
                    }
                    
                    if (channel != null)
                    {
                        channel.close();
                        channel = null;
                    }
                    
                    if (input != null)
                    {
                        input.close();
                        input = null;
                    }
                }
                catch (IOException e)
                {
                    String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                    standardEmailSender.sendStandardEmail(ID, tickNo, e);
                }
            }
        }
        
    }
    
    
    private boolean removeOutdatedDocFromExistingMatchingRecords(
        PoInvGrnDnMatchingHolder matching) throws Exception
    {
        boolean isGrnOutdated = false;
        boolean isDnOutdated  = false;
        
        List<PoInvGrnDnMatchingGrnHolder> matchingGrns = 
            poInvGrnDnMatchingGrnService.selectByMatchOid(matching.getMatchingOid());
        
        if (matchingGrns != null)
        {
            for (int i = 0; i < matchingGrns.size(); i++)
            {
                PoInvGrnDnMatchingGrnHolder matchingGrn = matchingGrns.get(i);
                GrnHeaderHolder grnHeader = grnHeaderService.selectGrnHeaderByKey(matchingGrn.getGrnOid());
                
                if (GrnStatus.OUTDATED.equals(grnHeader.getGrnStatus()))
                {
                    isGrnOutdated = true;
                    
                    matchingGrns.remove(i);
                    i--;
                }
            }
        }
        
        if (null != matching.getDnOid())
        {
            DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(matching.getDnOid());
            
            if (DnStatus.OUTDATED.equals(dnHeader.getDnStatus()))
            {
                isDnOutdated = true;
                
                matching.setDnOid(null);
                matching.setDnNo(null);
                matching.setDnDate(null);
                matching.setDnAmt(null);
            }
        }
        
        matching.setGrnList(matchingGrns);
        
        if (isGrnOutdated || isDnOutdated)
        {
            log.info("Removing outdated docs from existing Matching Record: " + this.formatMatchingDesc(matching));
        }
        
        return isGrnOutdated || isDnOutdated;
    }
    
    
    private boolean addInNewlyComingDocsToExistingMatchingRecord(PoInvGrnDnMatchingHolder matching) throws Exception
    {
        boolean isGrnCome = false;
        boolean isDnCome  = false;
        boolean isInvCome = false;
        
        PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(matching.getPoOid());
        
        List<PoInvGrnDnMatchingGrnHolder> matchingGrns = matching.getGrnList();
        
        List<GrnHeaderHolder> grnHeaders = null;
        if ("1".equals(poHeader.getPoSubType()))
        {
            grnHeaders = grnHeaderService
                .selectGrnHeadersByPoNoAndStoreCode(matching.getBuyerOid(),
                    matching.getSupplierCode(), matching.getPoNo(), matching.getPoStoreCode());
        }
        else if ("2".equals(poHeader.getPoSubType()))
        {
            grnHeaders = grnHeaderService
                .selectGrnHeadersByPoNo(matching.getBuyerOid(),
                    matching.getSupplierCode(), matching.getPoNo());
        }
        else
        {
            throw new Exception("Unknown po sub type value for PO: buyer oid ["
                + poHeader.getBuyerOid() + "], supplier code ["
                + poHeader.getSupplierCode() + ", po no [" + poHeader.getPoNo()
                + "].");
        }
        
        
        int sizeOfNewGrn = grnHeaders == null ? 0 : grnHeaders.size();
        int sizeOfOldGrn = matchingGrns == null ? 0 : matchingGrns.size();
        
        if (sizeOfNewGrn != 0 && sizeOfNewGrn != sizeOfOldGrn)
        {
            isGrnCome = true;
            
            matching.setGrnList( new ArrayList<PoInvGrnDnMatchingGrnHolder>() );
            
            for (GrnHeaderHolder grnHeader : grnHeaders)
            {
                PoInvGrnDnMatchingGrnHolder pigdmg = new PoInvGrnDnMatchingGrnHolder();
                pigdmg.setMatchingOid(matching.getMatchingOid());
                pigdmg.setGrnOid(grnHeader.getGrnOid());
                pigdmg.setGrnNo(grnHeader.getGrnNo());
                pigdmg.setGrnDate(grnHeader.getGrnDate());
                pigdmg.setGrnAmt(grnHeader.getTotalCost());
                
                if (null == grnHeader.getTotalCost() || BigDecimal.ZERO.compareTo(grnHeader.getTotalCost()) == 0)
                    pigdmg.setGrnAmt(grnService.computeGrnAmtByGrnOidAndPoOid(grnHeader.getGrnOid(), matching.getPoOid()));
                
                matching.getGrnList().add(pigdmg);
            }
        }
        
        
        InvHeaderHolder invHeader = null;
        if (null == matching.getInvOid())
        {
            if ("1".equals(poHeader.getPoSubType()))
            {
                invHeader = invHeaderService
                    .selectInvHeaderByPoOidAndStoreCode(matching.getPoOid(),
                        matching.getPoStoreCode());
            }
            else if ("2".equals(poHeader.getPoSubType()))
            {
                invHeader = invHeaderService
                    .selectInvHeaderByPoOid(poHeader.getPoOid());
            }
            else
            {
                throw new Exception("Unknown po sub type value for PO: buyer oid ["
                    + poHeader.getBuyerOid() + "], supplier code ["
                    + poHeader.getSupplierCode() + ", po no [" + poHeader.getPoNo()
                    + "].");
            }
            
            
            if (null != invHeader)
            {
                isInvCome = true;
                
                matching.setInvOid(invHeader.getInvOid());
                matching.setInvNo(invHeader.getInvNo());
                matching.setInvDate(invHeader.getInvDate());
                matching.setInvAmt(invHeader.getInvAmountNoVat());
            }
        }
        
        DnHeaderHolder dnHeader = null;
        if (null == matching.getDnOid() && null != matching.getInvOid())
        {
            dnHeader = dnHeaderService
                .selectDnHeaderByInvNo(matching.getBuyerOid(),
                    matching.getSupplierCode(), matching.getInvNo());
            
            if (dnHeader != null && !dnHeader.getSentToSupplier())
                dnHeader = null;
            
            if (null != dnHeader)
            {
                isDnCome = true;
                
                matching.setDnOid( dnHeader.getDnOid() );
                matching.setDnNo( dnHeader.getDnNo() );
                matching.setDnDate( dnHeader.getDnDate() );
                matching.setDnAmt( dnHeader.getTotalCost() );
            }
        }
        
        
        if (isGrnCome || isDnCome || isInvCome)
        {
            log.info("Adding newly coming docs to existing Matching Record: " + this.formatMatchingDesc(matching));
        }
        
        return isGrnCome || isDnCome || isInvCome;
    }
    
    
    private void performMatching(BuyerHolder buyer, List<BusinessRuleHolder> businessRuleList, int expiryDays, int toleranceDays)
    {
        log.info("Performing matching procedure of satisfied matching records for buyer" + this.formatBuyerName(buyer) + ".");
        
        List<PoInvGrnDnMatchingHolder> existingMatchingRecords = null;
        
        try
        {
            existingMatchingRecords = poInvGrnDnMatchingService.selectMatchingRecordsWhichCanDoMatching(buyer.getBuyerOid(),  expiryDays, toleranceDays);
            
            if (null == existingMatchingRecords || existingMatchingRecords.isEmpty())
            {
                return;
            }
            
            log.info("[" + existingMatchingRecords.size() + "] matching records found.");
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            return;
        }
        
        for (PoInvGrnDnMatchingHolder matching : existingMatchingRecords)
        {
            try
            {
                if (matching.getMatchingDate() == null)
                {
                    this.performMatching(buyer, matching, businessRuleList);
                }
            }
            catch(Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
    }
    
    
    private void performMatching(BuyerHolder buyer, PoInvGrnDnMatchingHolder matching, List<BusinessRuleHolder> businessRuleList) throws Exception
    {
        log.info("Performing matching procedure for Matching Record: " + this.formatMatchingDesc(matching));
        
        if (null == matching.getInvOid())
        {
            matching.setMatchingStatus(PoInvGrnDnMatchingStatus.INSUFFICIENT_INV);
            matching.setMatchingDate(new Date());
            this.initAcceptFlag(matching, buyer);
            
            poInvGrnDnMatchingService.updateByPrimaryKeySelective(null, matching);
            
            return;
        }
        
        PoHolder po = poService.selectPoByKey(matching.getPoOid());
        
        InvHolder inv = null;
        if (matching.getInvOid() != null)
        {
            inv = invoiceService.selectInvoiceByKey(matching.getInvOid());
        }
        
        List<PoInvGrnDnMatchingGrnHolder> matchingGrns = poInvGrnDnMatchingGrnService
            .selectByMatchOid(matching.getMatchingOid());
        matching.setGrnList(matchingGrns);
        
        List<PoInvGrnDnMatchingDetailExHolder> matchingDetails = poInvGrnDnMatchingDetailService.selectByMatchingOid(matching.getMatchingOid());
        matching.setDetailList(matchingDetails);
        
        List<GrnHolder> grns = new ArrayList<GrnHolder>();
        for (PoInvGrnDnMatchingGrnHolder matchingGrn : matchingGrns)
        {
            grns.add(grnService.selectByKey(matchingGrn.getGrnOid()));
        }
        
        DnHolder dn = null;
        if (null != matching.getDnOid())
        {
            dn = dnService.selectDnByKey(matching.getDnOid());
        }
        
        
        List<BusinessRuleHolder> businessRules = businessRuleService
            .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                    matching.getBuyerOid(), "Matching", "PoInvGrnDn");
        
        SupplierHolder supplier = supplierService.selectSupplierByKey(matching.getSupplierOid());
        
        
        MatchingReportParameter report = new MatchingReportParameter(matching,
                po, inv, grns, dn, businessRules, supplier.getSupplierSource()
                        .name(), true);
        matching.setMatchingStatus(report.getMatchingResult().getMatchingStatus());
        matching.setMatchingDate(new Date());
        
        processAfterDoMatching(matching, businessRuleList, buyer);
        
        this.initAcceptFlag(matching, buyer);
        
        poInvGrnDnMatchingService.updateByPrimaryKeySelective(null, matching);
        
    }
    
    
    private void processAfterDoMatching(PoInvGrnDnMatchingHolder matching, List<BusinessRuleHolder> businessRuleList, BuyerHolder buyer) throws Exception
    {
        if (matching.getRevised() == null || !matching.getRevised())
        {
            processUnmatched(matching, buyer);
        }
        
        
        boolean approveInv = false;
        
        boolean autoClose = false;
        if (PoInvGrnDnMatchingStatus.MATCHED.equals(matching.getMatchingStatus()) 
                || PoInvGrnDnMatchingStatus.MATCHED_BY_DN.equals(matching.getMatchingStatus()))
        {
            autoClose = true;
        }
        else if (PoInvGrnDnMatchingBuyerStatus.ACCEPTED.equals(matching.getBuyerStatus()))
        {
            autoClose = businessRuleService.isMatchingAutoCloseAcceptedRecord(buyer.getBuyerOid());
        }
        else if (PoInvGrnDnMatchingBuyerStatus.REJECTED.equals(matching.getBuyerStatus()))
        {
            autoClose = businessRuleService.isAutoCloseRejectedMatchingRecord(buyer.getBuyerOid());
        }
        
        
        if (autoClose)
        {
            matching.setClosed(true);
            matching.setClosedDate(new Date());
            matching.setClosedBy(CoreCommonConstants.SYSTEM);
            
            boolean autoApproveMatchedByDN = retriveRule("AutoApproveMatchedByDn", businessRuleList) != null;
            
            if (PoInvGrnDnMatchingStatus.MATCHED.equals(matching.getMatchingStatus()))
            {
                approveInv = true;
            }
            else if (PoInvGrnDnMatchingStatus.MATCHED_BY_DN.equals(matching.getMatchingStatus()) && autoApproveMatchedByDN)
            {
                approveInv = true;
            }
            else
            {
                approveInv = businessRuleService.isMatchingAutoApproveClosedAcceptedRecord(buyer.getBuyerOid());
            }
            
            if (approveInv)
            {
                //business rule change inv date to first grn date
                poInvGrnDnMatchingService.changeInvDateToFirstGrnDate(matching);
                
                matching.setInvStatus(PoInvGrnDnMatchingInvStatus.SYS_APPROVED);
                matching.setInvStatusActionDate(new Date());
                
                poInvGrnDnMatchingService.moveFile(matching);
            }
        }
        
    }
    
    
    private String formatBuyerName(BuyerHolder buyer)
    {
        return " [" + buyer.getBuyerName() + " (" + buyer.getBuyerCode() + ") ]";
    }
    
    
    private String formatMatchingDesc(PoInvGrnDnMatchingHolder matching)
    {
        return "Buyer-Code [" + matching.getBuyerCode()
            + "], Buyer-Given-Supplier-Code [" + matching.getSupplierCode()
            + "], Po No. [" + matching.getPoNo() + "], Store ["
            + (matching.getPoStoreCode() == null ? "" : matching
            .getPoStoreCode()) + "].";
    }
    
    
    private BusinessRuleHolder retriveRule(String ruleId, List<BusinessRuleHolder> businessRuleList)
    {

        if (businessRuleList == null)
        {
            return null;
        }
        for (BusinessRuleHolder businessRule : businessRuleList)
        {
            if (ruleId.equalsIgnoreCase(businessRule.getRuleId()))
            {
                return businessRule;
            }
        }
        return null;
    }
    
    
    /*if accept flag is false, supplier can not accept this record when it is unmatched.*/
    public void initAcceptFlag(PoInvGrnDnMatchingHolder matching, BuyerHolder buyer) throws Exception
    {
        boolean isMatchedPoPriceMoreThanInv = businessRuleService.isMatchingPriceInvLessPo(buyer.getBuyerOid());
        boolean isMatchedGrnQtyMoreThanInv = businessRuleService.isMatchingQtyInvLessGrn(buyer.getBuyerOid());
        matching.setAcceptFlag(true);
        
        if ((!isMatchedPoPriceMoreThanInv && matching.existPriceInvLessPo())
                || (!isMatchedGrnQtyMoreThanInv && matching.existQtyGrnMoreThanInv()))
        {
            matching.setAcceptFlag(false);
            return;
        }
    }
    
    
    public void processUnmatched(PoInvGrnDnMatchingHolder matching, BuyerHolder buyer) throws Exception
    {
        boolean isAutoRejectBuyerLossUnmatchedRecord= businessRuleService.isAutoRejectBuyerLossUnmatchedRecord(buyer.getBuyerOid());
        boolean isMatchedPoPriceMoreThanInv = businessRuleService.isMatchingPriceInvLessPo(buyer.getBuyerOid());
        boolean isMatchedGrnQtyMoreThanInv = businessRuleService.isMatchingQtyInvLessGrn(buyer.getBuyerOid());
        
        if (PoInvGrnDnMatchingStatus.UNMATCHED.equals(matching.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(matching.getMatchingStatus())
                || PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(matching.getMatchingStatus()))
        {
            if (((!isMatchedPoPriceMoreThanInv && matching.existPriceInvLessPo())
                    || (!isMatchedGrnQtyMoreThanInv && matching.existQtyGrnMoreThanInv()))
                    && isAutoRejectBuyerLossUnmatchedRecord)
            {
                matching.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
                matching.setSupplierStatusActionBy(CoreCommonConstants.SYSTEM);
                matching.setSupplierStatusActionDate(new Date());
            }
        }
        
        if (PoInvGrnDnMatchingSupplierStatus.REJECTED.equals(matching.getSupplierStatus()))
        {
            boolean isAutoAcceptPriceInvLessPo = businessRuleService.isAutoAcceptPriceInvLessPo(buyer.getBuyerOid());
            boolean isAutoAcceptQtyInvLessGrn = businessRuleService.isAutoAcceptQtyInvLessGrn(buyer.getBuyerOid());
            
            for (PoInvGrnDnMatchingDetailExHolder detail : matching.getDetailList())
            {
                BigDecimal poPrice = detail.getPoPrice() == null ? BigDecimal.ZERO : detail.getPoPrice();
                BigDecimal invPrice = detail.getInvPrice() == null ? BigDecimal.ZERO : detail.getInvPrice();
                BigDecimal grnQty = detail.getGrnQty() == null ? BigDecimal.ZERO : detail.getGrnQty();
                BigDecimal invQty = detail.getInvQty() == null ? BigDecimal.ZERO : detail.getInvQty();
                
                if ((isAutoAcceptPriceInvLessPo && poPrice.compareTo(invPrice) > 0) || poPrice.compareTo(invPrice) == 0) 
                {
                    detail.setPriceStatus(PoInvGrnDnMatchingPriceStatus.ACCEPTED);
                    
                    ItemHolder item = itemService.selectItemByBuyerOidAndBuyerItemCode(matching.getBuyerOid(), detail.getBuyerItemCode());
                    if (item != null && item.getSubclassCode() != null && !item.getSubclassCode().isEmpty())
                    {
                        DocSubclassHolder docSubclass = new DocSubclassHolder();
                        docSubclass.setDocOid(matching.getMatchingOid());
                        docSubclass.setClassCode(item.getClassCode());
                        docSubclass.setSubclassCode(item.getSubclassCode());
                        docSubclass.setAuditFinished(true);
                        
                        docSubclassMapper.updateByPrimaryKeySelective(docSubclass);
                    }
                }
                
                if ((isAutoAcceptQtyInvLessGrn && grnQty.compareTo(invQty) > 0) || grnQty.compareTo(invQty) == 0) 
                {
                    detail.setQtyStatus(PoInvGrnDnMatchingQtyStatus.ACCEPTED);
                }
            }
            
            matching.computeBuyerSideStatus();
        }
    }
    
}
