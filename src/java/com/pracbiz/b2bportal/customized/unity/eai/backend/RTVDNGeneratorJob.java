package com.pracbiz.b2bportal.customized.unity.eai.backend;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DnBuyerStatus;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.eai.backend.BaseJob;
import com.pracbiz.b2bportal.core.eai.backend.SupplierMasterImportJob;
import com.pracbiz.b2bportal.core.eai.file.canonical.DnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.GiDetailService;
import com.pracbiz.b2bportal.core.service.GiHeaderService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.RtvDetailService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class RTVDNGeneratorJob extends BaseJob implements 
        CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(RTVDNGeneratorJob.class);
    private static final String ID = "[RTVDNGeneratorJob]";

    private BuyerService buyerService;
    private OidService oidService;
    private StandardEmailSender standardEmailSender;
    private GiHeaderService giHeaderService;
    private GiDetailService giDetailService;
    private RtvHeaderService rtvHeaderService;
    private RtvDetailService rtvDetailService;
    private BusinessRuleService businessRuleService;
    private DnService dnService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private MailBoxUtil mboxUtil;
    private DnDocFileHandler dnDocFileHandler;
    private DnHeaderService dnHeaderService;
    private BuyerStoreService buyerStoreService;
    private SupplierService supplierService;
    

    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        giHeaderService = this.getBean("giHeaderService", GiHeaderService.class);
        giDetailService = this.getBean("giDetailService", GiDetailService.class);
        rtvHeaderService = this.getBean("rtvHeaderService", RtvHeaderService.class);
        rtvDetailService = this.getBean("rtvDetailService", RtvDetailService.class);
        oidService = this.getBean("oidService", OidService.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
        dnService = this.getBean("dnService", DnService.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        dnDocFileHandler = this.getBean("canonicalDnDocFileHandler", DnDocFileHandler.class);
        dnHeaderService = this.getBean("dnHeaderService", DnHeaderService.class);
        buyerStoreService = this.getBean("buyerStoreService", BuyerStoreService.class);
        supplierService = this.getBean("supplierService", SupplierService.class);
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
    
    
    protected void realProcess()
    {
        log.info(":::: Start to process.");
        
        List<BuyerHolder> buyerList = null;
        try
        {
            buyerList = buyerService.selectActiveBuyers();
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
        if (buyerList == null || buyerList.isEmpty())
        {
            log.info(":::: No buyer exist in system.");
            return;
        }
        
        for (BuyerHolder buyer : buyerList)
        {
            try
            {
                generateDnFromGI(buyer);
                autoExportNoDisputeDn(buyer);
                
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }
        
        log.info(":::: process successfully.");
    }
    
    
    private void generateDnFromGI(BuyerHolder buyer) throws Exception
    {
        boolean autoGenDnFromGi = businessRuleService.isAutoGenDnFromGI(buyer.getBuyerOid());
        
        if (!autoGenDnFromGi)
        {
            return;
        }
        
        List<GiHeaderHolder> canGenerateDnGiHeaders = giHeaderService.selectGiHeadersWithoutDn(buyer.getBuyerOid());
        
        if (canGenerateDnGiHeaders == null || canGenerateDnGiHeaders.isEmpty())
        {
            log.info("Find 0 GIs to generate DN for buyer [" + buyer.getBuyerCode() + "]");
            return;
        }
        
        BuyerMsgSettingHolder msgSetting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), MsgType.DN.name());
        
        if (msgSetting == null || msgSetting.getFileFormat().trim().isEmpty())
        {
            log.info("can not obtain the DN file format for buyer [" + buyer.getBuyerCode() + "]");
            
            return;
        }
        
        String fileFormat = msgSetting.getFileFormat();
        
        String rootPath = mboxUtil.getBuyerMailBox(buyer.getMboxId());
        
        List<File> files = new ArrayList<File>();
        
        for (GiHeaderHolder giHeader : canGenerateDnGiHeaders)
        {
            List<GiDetailHolder> giDetailList = giDetailService.selectByGiOid(giHeader.getGiOid());
            
            if (giDetailList == null || giDetailList.isEmpty())
            {
                log.info("Could not get detail for GI [" + giHeader.getGiNo() + "]");
                continue;
            }
            
            if (giHeader.getRtvNo() == null)
            {
                log.info("Could not find the corresponding RTV for GI [" + giHeader.getGiNo() + "]");
                continue;
            }
            
            RtvHeaderHolder rtvHeader = rtvHeaderService.
                    selectRtvHeaderByRtvNo(giHeader.getBuyerOid(), giHeader.getRtvNo(), giHeader.getSupplierCode());
            
            if (rtvHeader == null)
            {
                log.info("Could not find the corresponding RTV for GI [" + giHeader.getGiNo() + "]");
                continue;
            }
            
            SupplierHolder supplier = supplierService.selectSupplierByKey(rtvHeader.getSupplierOid());
            if (supplier.getLiveDate() == null || DateUtil.getInstance().compareDate(supplier.getLiveDate(), rtvHeader.getRtvDate()) > 0)
            {
               log.info("Supplier [" + supplier.getSupplierCode() + "] is not alive or RTV[" + rtvHeader.getRtvNo() + "] is before live date, will not generate DN");
               continue;
            }
            
            List<RtvDetailHolder> rtvDetailList = rtvDetailService.selectRtvDetailByKey(rtvHeader.getRtvOid());
            
            BigDecimal dnOid = oidService.getOid();
            
            DnHolder dn = new DnHolder();
            
            BuyerStoreHolder store = null;
            if (giHeader.getIssuingStoreCode() != null)
            {
                store = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(rtvHeader.getBuyerCode(), giHeader.getIssuingStoreCode());
            }
            
            DnHeaderHolder dnHeader = generateDnHeaderFromGi(dnOid, rtvHeader.getRtvNo(), rtvHeader, giHeader, store, supplier.getGstPercent());
            
            dn.setDnHeader(dnHeader);
            
            dn.setDnDetail(generateDnDetailFromGi(dnOid, dnHeader, giDetailList, rtvDetailList));
            
            dn.getDnHeader().setMarkSentToSupplier(true);
            dn.getDnHeader().setSentToSupplier(true);

            String originalFilename = MsgType.DN.name() + DOC_FILENAME_DELIMITOR + buyer.getBuyerCode() 
                    + DOC_FILENAME_DELIMITOR + dn.getDnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                    + StringUtil.getInstance().convertDocNo(dn.getDnHeader().getDnNo()) + "."
                    + FileUtil.getInstance().getExtension(dnDocFileHandler.getTargetFilename(dn, fileFormat));
            
            File originalFile = new File(rootPath, originalFilename);
            files.add(originalFile);
            dnDocFileHandler.createFile(dn, originalFile, fileFormat);
            
            MsgTransactionsHolder msg = dn.getDnHeader().convertToMsgTransaction(originalFilename);
            msg.setGeneratedOnPortal(true);
            dnService.insertDnWithMsgTransaction(null, dn, msg);
            
            dnService.createDnClassInfo(dn.getDnHeader(), dn.getDnDetail());
        }
        
        if (files.isEmpty())
        {
            return;
        }
        
        String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
        
        String batchNo = BatchType.DN.name() + DOC_FILENAME_DELIMITOR + buyer.getBuyerCode() + DOC_FILENAME_DELIMITOR + ts;
        String zipFileName = batchNo + ".zip";
        
        String outPath = mboxUtil.getBuyerOutPath(buyer.getMboxId());
        
        GZIPHelper.getInstance().doZip(files, outPath, zipFileName);
        
        
        for (File file : files)
        {
            FileUtil.getInstance().deleleAllFile(file);
        }
        
    }
    
    
    private DnHeaderHolder generateDnHeaderFromGi(BigDecimal dnOid, String dnNo, RtvHeaderHolder rtvHeader, GiHeaderHolder giHeader, BuyerStoreHolder store, BigDecimal vatRate)
    {
        DnHeaderHolder dnHeader = new DnHeaderHolder();
        dnHeader.setDnOid(dnOid);
        dnHeader.setDnNo(dnNo);
        dnHeader.setDocAction("A");
        dnHeader.setActionDate(new Date());
        dnHeader.setDnType(DnType.STK_RTV.name());
        dnHeader.setDnDate(new Date());
        dnHeader.setRtvNo(rtvHeader.getRtvNo());
        dnHeader.setRtvDate(rtvHeader.getRtvDate());
        dnHeader.setGiNo(giHeader.getGiNo());
        dnHeader.setGiDate(giHeader.getGiDate());
        dnHeader.setBuyerOid(rtvHeader.getBuyerOid());
        dnHeader.setBuyerCode(rtvHeader.getBuyerCode());
        dnHeader.setBuyerName(rtvHeader.getBuyerName());
        dnHeader.setBuyerAddr1(rtvHeader.getBuyerAddr1());
        dnHeader.setBuyerAddr2(rtvHeader.getBuyerAddr2());
        dnHeader.setBuyerAddr3(rtvHeader.getBuyerAddr3());
        dnHeader.setBuyerAddr4(rtvHeader.getBuyerAddr4());
        dnHeader.setBuyerCity(rtvHeader.getBuyerCity());
        dnHeader.setBuyerState(rtvHeader.getBuyerState());
        dnHeader.setBuyerCtryCode(rtvHeader.getBuyerCtryCode());
        dnHeader.setBuyerPostalCode(rtvHeader.getBuyerPostalCode());
        dnHeader.setSupplierOid(rtvHeader.getSupplierOid());
        dnHeader.setSupplierCode(rtvHeader.getSupplierCode());
        dnHeader.setSupplierName(rtvHeader.getSupplierName());
        dnHeader.setSupplierAddr1(rtvHeader.getSupplierAddr1());
        dnHeader.setSupplierAddr2(rtvHeader.getSupplierAddr2());
        dnHeader.setSupplierAddr3(rtvHeader.getSupplierAddr3());
        dnHeader.setSupplierAddr4(rtvHeader.getSupplierAddr4());
        dnHeader.setSupplierCity(rtvHeader.getSupplierCity());
        dnHeader.setSupplierState(rtvHeader.getSupplierState());
        dnHeader.setSupplierCtryCode(rtvHeader.getSupplierCtryCode());
        dnHeader.setSupplierPostalCode(rtvHeader.getSupplierPostalCode());
        dnHeader.setStoreCode(giHeader.getIssuingStoreCode());
        
        if (store != null)
        {
            dnHeader.setStoreName(store.getStoreName());
            dnHeader.setStoreAddr1(store.getStoreAddr1());
            dnHeader.setStoreAddr2(store.getStoreAddr2());
            dnHeader.setStoreAddr3(store.getStoreAddr3());
            dnHeader.setStoreAddr4(store.getStoreAddr4());
            dnHeader.setStoreCity(store.getStoreCity());
            dnHeader.setStoreState(store.getStoreState());
            dnHeader.setStoreCtryCode(store.getStoreCtryCode());
            dnHeader.setStorePostalCode(store.getStorePostalCode());
        }
        
        dnHeader.setDeptCode(rtvHeader.getDeptCode());
        dnHeader.setDeptName(rtvHeader.getDeptName());
        dnHeader.setSubDeptCode(rtvHeader.getSubDeptCode());
        dnHeader.setSubDeptName(rtvHeader.getSubDeptName());
        dnHeader.setVatRate(vatRate == null ? BigDecimal.ZERO : vatRate);
        dnHeader.setDnStatus(DnStatus.NEW);
        dnHeader.setDuplicate(false);
        dnHeader.setPriceStatus(DnPriceStatus.PENDING);
        dnHeader.setQtyStatus(DnQtyStatus.PENDING);
        dnHeader.setBuyerStatus(DnBuyerStatus.PENDING);
        dnHeader.setDispute(false);
        dnHeader.setExported(false);
        dnHeader.setClosed(false);
        dnHeader.setPriceDisputed(false);
        dnHeader.setQtyDisputed(false);
        return dnHeader;
    }
    
    
    private List<DnDetailExHolder> generateDnDetailFromGi(BigDecimal dnOid, DnHeaderHolder dnHeader, List<GiDetailHolder> giDetailList, List<RtvDetailHolder> rtvDetailList)   
    {
        List<DnDetailExHolder> dnDetails = new ArrayList<DnDetailExHolder>();
        
        BigDecimal totalCost = BigDecimal.ZERO;
        
        int index = 1;
        for (GiDetailHolder giDetail : giDetailList)
        {
            RtvDetailHolder rtvDetail = null;
            for (RtvDetailHolder detail : rtvDetailList)
            {
                if (detail.getBuyerItemCode().equalsIgnoreCase(giDetail.getBuyerItemCode()))
                {
                    rtvDetail = detail;
                }
            }
            
            DnDetailExHolder dnDetail = new DnDetailExHolder();
            dnDetail.setDnOid(dnOid);
            dnDetail.setLineSeqNo(index++);
            dnDetail.setBuyerItemCode(giDetail.getBuyerItemCode());
            dnDetail.setSupplierItemCode(giDetail.getSupplierItemCode());
            dnDetail.setBarcode(giDetail.getBarcode());
            dnDetail.setItemDesc(rtvDetail == null ? giDetail.getItemDesc() : rtvDetail.getItemDesc());
            dnDetail.setBrand(giDetail.getBrand());
            dnDetail.setColourCode(giDetail.getColourCode());
            dnDetail.setColourDesc(giDetail.getColourDesc());
            dnDetail.setSizeCode(giDetail.getSizeCode());
            dnDetail.setSizeDesc(giDetail.getSizeDesc());
            dnDetail.setPackingFactor(giDetail.getPackingFactor());
            dnDetail.setDebitBaseUnit(giDetail.getRtvBaseUnit());
            dnDetail.setOrderUom(giDetail.getRtvUom());
            dnDetail.setDebitQty(giDetail.getIssuedQty());
            dnDetail.setLineRefNo(rtvDetail == null ? null : rtvDetail.getLineRefNo());
            
            BigDecimal unitCost = giDetail.getUnitCost();
            if ((unitCost == null || unitCost.equals(BigDecimal.ZERO)) && rtvDetail!= null)
            {
                unitCost = rtvDetail.getUnitCost();
            }
            
            dnDetail.setUnitCost(unitCost == null ? BigDecimal.ZERO : unitCost);
            dnDetail.setCostDiscountAmount(BigDecimal.ZERO);
            dnDetail.setCostDiscountPercent(BigDecimal.ZERO);
            dnDetail.setNetUnitCost(dnDetail.getUnitCost());
            dnDetail.setItemCost(dnDetail.getDebitQty().multiply(
                    dnDetail.getUnitCost()));
            
            dnDetail.setDisputePrice(dnDetail.getUnitCost());
            dnDetail.setDisputeQty(dnDetail.getDebitQty());
            dnDetail.setPriceStatus(DnPriceStatus.PENDING);
            dnDetail.setQtyStatus(DnQtyStatus.PENDING);
            
            dnDetail.setConfirmPrice(dnDetail.getUnitCost());
            dnDetail.setConfirmQty(dnDetail.getDebitQty());
            
            dnDetails.add(dnDetail);
            
            totalCost = totalCost.add(dnDetail.getItemCost());
        }
        
        dnHeader.setTotalCost(totalCost);
        dnHeader.setTotalVat(dnHeader.getTotalCost().multiply(dnHeader.getVatRate()).divide(BigDecimal.valueOf(100)));
        dnHeader.setTotalCostWithVat(dnHeader.getTotalCost().add(dnHeader.getTotalVat()));
        
        return dnDetails;
    }


    private void autoExportNoDisputeDn(BuyerHolder buyer) throws Exception
    {
        int toleranceDays = businessRuleService.selectGlobalRTVDnGeneratingJobBuffingDays(buyer.getBuyerOid());
        List<DnHeaderHolder> dnHeaders = dnHeaderService.selectNoDisputeDnHeadersByBuyerAndBufferingDays(buyer.getBuyerOid(), toleranceDays);
        
        if (dnHeaders == null || dnHeaders.isEmpty())
        {
            log.info("find 0 No dispute DN for buyer [" + buyer.getBuyerCode() + "] to export");
            
            return;
        }
        
        log.info("find " + dnHeaders.size() + " dns need to export to buyer [" + buyer.getBuyerCode() + "]");
        
        BuyerMsgSettingHolder msgSetting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), MsgType.DN.name());
        
        if (msgSetting == null || msgSetting.getFileFormat().trim().isEmpty())
        {
            log.info("can not obtain the DN file format for buyer [" + buyer.getBuyerCode() + "]");
            
            return;
        }
        
        String fileFormat = msgSetting.getFileFormat();
        
        
        for (DnHeaderHolder dnHeader : dnHeaders)
        {
            DnHolder dn = dnService.selectDnByKey(dnHeader.getDnOid());
            String originalFilename = "CN" + DOC_FILENAME_DELIMITOR + buyer.getBuyerCode() 
                    + DOC_FILENAME_DELIMITOR + dn.getDnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                    + StringUtil.getInstance().convertDocNo(dn.getDnHeader().getDnNo()) + "."
                    + FileUtil.getInstance().getExtension(dnDocFileHandler.getTargetFilename(dn, fileFormat));
            
            File originalFile = new File(mboxUtil.getBuyerInPath(buyer.getMboxId()), originalFilename);
            
            String archivePath = mboxUtil.getFolderInBuyerArchInPath(buyer.getMboxId(), 
                DateUtil.getInstance().getYearAndMonth(new Date()));
            
            File rlt = new File(archivePath);
            
            if (!rlt.isDirectory())
            {
                FileUtil.getInstance().createDir(rlt);
            }
            
            File archiveFile = new File(mboxUtil.getFolderInBuyerArchInPath(buyer.getMboxId(), 
                DateUtil.getInstance().getYearAndMonth(new Date())), originalFilename);
            
            dnDocFileHandler.createFile(dn, originalFile, fileFormat);
            dnDocFileHandler.createFile(dn, archiveFile, fileFormat);
            
            dnHeader.setExported(true);
            dnHeader.setExportedDate(new Date());
            dnHeader.setClosed(true);
            dnHeader.setClosedBy(SYSTEM);
            dnHeader.setClosedDate(new Date());
            
            dnHeaderService.updateByPrimaryKey(null, dnHeader);
        }
    }
}
