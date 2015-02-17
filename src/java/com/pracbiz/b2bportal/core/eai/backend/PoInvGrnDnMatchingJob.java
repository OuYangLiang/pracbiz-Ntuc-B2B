package com.pracbiz.b2bportal.core.eai.backend;

/*import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingActionStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.report.matching.PoInvGrnDnReport;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.DnDetailService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.GrnDetailService;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;
import com.pracbiz.b2bportal.core.service.InvDetailService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PoDetailService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;*/

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class PoInvGrnDnMatchingJob //extends QuartzJobBean implements
        //CoreCommonConstants
{
    /*private static final Logger log = LoggerFactory
            .getLogger(PoInvGrnDnMatchingJob.class);
    private static final String ID = "[PoInvGrnDnMatchingJob]";
    private static boolean isRunning = false;

    private PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    private PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService;
    private PoHeaderService poHeaderService;
    private PoDetailService poDetailService;
    private InvHeaderService invHeaderService;
    private InvDetailService invDetailService;
    private PoLocationService poLocationService;
    private PoLocationDetailService poLocationDetailService;
    private GrnHeaderService grnHeaderService;
    private GrnDetailService grnDetailService;
    private DnHeaderService dnHeaderService;
    private DnDetailService dnDetailService;
    private BusinessRuleService businessRuleService;
    private OidService oidService;
    private SupplierService supplierService;
    private StandardEmailSender standardEmailSender;
    private int expiredDays;


    @Override
    protected void executeInternal(JobExecutionContext arg0)
            throws JobExecutionException
    {
        if (isRunning)
        {
            log.info(":::: Previous job is still running.");
            return;
        }

        isRunning = true;

        try
        {
            process();
        }
        finally
        {
            isRunning = false;
        }
    }


    private void process()
    {
        log.info(":::: Start to process.");
        try
        {
            handlePoInvGrnDnMatching();
            log.info(":::: process successfully.");
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }


    private void handlePoInvGrnDnMatching() throws Exception
    {
        List<PoInvGrnDnMatchingHolder> newMatchingList = this
                .retrieveDocsForNewMatchingRecord();
        List<PoInvGrnDnMatchingHolder> oldMatchingList = this
                .retrieveDocsForOldMatchingRecord();
        if (newMatchingList == null && oldMatchingList == null)
        {
            log.debug(":::: No record need store into po-inv-grn-dn matching.");
            return;
        }
        poInvGrnDnMatchingService.updateMatchingList(newMatchingList,
                oldMatchingList);
    }


    private List<PoInvGrnDnMatchingHolder> retrieveDocsForOldMatchingRecord()
            throws Exception
    {
        List<PoInvGrnDnMatchingHolder> noMatchedRecords = poInvGrnDnMatchingService
                .selectMatchingRecordNotMatchedStatus();
        if (noMatchedRecords == null || noMatchedRecords.isEmpty())
        {
            log.info(":::: No matching record found within no matched status");
            return null;
        }
        log.info(":::: " + noMatchedRecords.size()
                + " matching record(s) found within no matched status");

        List<PoInvGrnDnMatchingHolder> rlt = new ArrayList<PoInvGrnDnMatchingHolder>();
        for (PoInvGrnDnMatchingHolder pigdm : noMatchedRecords)
        {
            if (pigdm.getClosed() != null && pigdm.getClosed())
            {
                log.info(":::: The record PO No. [" + pigdm.getPoNo()
                        + "] PO store code [" + pigdm.getPoStoreCode()
                        + "] has been closed, skip it.");
                continue;
            }
            if (!pigdm.getMatchingStatus().equals(
                    PoInvGrnDnMatchingStatus.PENDING)
                    && !DateUtil.getInstance().isAfterDays(
                            pigdm.getCreateDate(), new Date(), expiredDays))
            {
                log.info(":::: The record PO No. [" + pigdm.getPoNo()
                        + "] PO store code [" + pigdm.getPoStoreCode()
                        + "] has been expired, no need to handle");
                pigdm.setClosed(true);
                pigdm.setCloseDate(new Date());
                rlt.add(pigdm);
                continue;
            }
            List<GrnHeaderHolder> grnHeaderList = new ArrayList<GrnHeaderHolder>();
            DnHeaderHolder dnHeader = null;
            InvHeaderHolder invHeader = null;
            if (pigdm.getMatchingStatus().equals(PoInvGrnDnMatchingStatus.PENDING))
            {
                if (pigdm.getInvOid() == null)
                {
                    invHeader = invHeaderService
                            .selectInvHeaderByPoOidAndStoreCode(
                                    pigdm.getPoOid(), pigdm.getPoStoreCode());
                    if (invHeader != null)
                    {
                        dnHeader = retrieveDnHeader(pigdm.getBuyerOid(),
                                pigdm.getSupplierOid(), invHeader.getInvNo());
                    }
                }
                else if (pigdm.getDnOid() == null)
                {
                    dnHeader = retrieveDnHeader(pigdm.getBuyerOid(),
                            pigdm.getSupplierOid(), pigdm.getInvNo());
                }
                grnHeaderList = grnHeaderService
                        .selectGrnHeaderByPoNoBuyerSupplierAndStoreCode(
                                pigdm.getPoNo(), pigdm.getBuyerOid(),
                                pigdm.getSupplierOid(), pigdm.getPoStoreCode());
            }
            else if (!pigdm.getMatchingStatus().equals(PoInvGrnDnMatchingStatus.MATCHED))
            {
                if (pigdm.getDnOid() == null)
                {
                    dnHeader = retrieveDnHeader(pigdm.getBuyerOid(),
                            pigdm.getSupplierOid(), pigdm.getInvNo());
                }
                grnHeaderList = grnHeaderService
                        .selectGrnHeaderByPoNoBuyerSupplierAndStoreCode(
                                pigdm.getPoNo(), pigdm.getBuyerOid(),
                                pigdm.getSupplierOid(), pigdm.getPoStoreCode());
            }

            List<GrnHeaderHolder> newGrnList = retrieveNewGrnRecords(
                    grnHeaderList, pigdm.getMatchingOid());
            rlt.add(this.otherDocsForOldMatchingRecord(pigdm, newGrnList,
                    dnHeader, invHeader));
        }
        return rlt;
    }


    private List<PoInvGrnDnMatchingHolder> retrieveDocsForNewMatchingRecord()
            throws Exception
    {
        List<PoHeaderHolder> poHeaderList = poHeaderService
                .selectLocalBuyerPoHeaderNotInPoInvGrnDnMatching();
        if (poHeaderList == null || poHeaderList.isEmpty())
        {
            log.info(":::: There is no po not existed in matching.");
            return null;
        }
        log.info(":::: There are " + poHeaderList.size()
                + " POs not existed in matching.");
        List<PoInvGrnDnMatchingHolder> rlt = new ArrayList<PoInvGrnDnMatchingHolder>();
        for (PoHeaderHolder poHeader : poHeaderList)
        {
            if ("1".equals(poHeader.getPoSubType()))
            {
                List<PoLocationHolder> poLocs = poLocationService
                        .selectLocationsByPoOid(poHeader.getPoOid());
                if (poLocs == null)
                {
                    log.debug(":::: There is no po location within sub type 1.");
                    return null;
                }
                for (PoLocationHolder poLoc : poLocs)
                {
                    InvHeaderHolder invHeader = invHeaderService
                            .selectInvHeaderByPoOidAndStoreCode(
                                    poHeader.getPoOid(),
                                    poLoc.getLocationCode());
                    List<GrnHeaderHolder> grnHeaderList = grnHeaderService
                            .selectGrnHeaderByPoNoBuyerSupplierAndStoreCode(
                                    poHeader.getPoNo(), poHeader.getBuyerOid(),
                                    poHeader.getSupplierOid(),
                                    poLoc.getLocationCode());
                    DnHeaderHolder dnHeader = null;
                    if (invHeader != null)
                    {
                        dnHeader = retrieveDnHeader(poHeader.getBuyerOid(),
                                poHeader.getSupplierOid(), invHeader.getInvNo());
                    }
                    rlt.add(generateMatchingRecord(poHeader, invHeader,
                            grnHeaderList, dnHeader, poLoc.getLocationCode()));
                }
            }
            else if ("2".equals(poHeader.getPoSubType()))
            {
                InvHeaderHolder invHeader = invHeaderService
                        .selectInvHeaderByPoOid(poHeader.getPoOid());
                List<GrnHeaderHolder> grnHeaderList = grnHeaderService
                        .selectGrnHeaderByPoNo(poHeader.getPoNo());
                DnHeaderHolder dnHeader = null;
                if (invHeader != null)
                {
                    dnHeader = retrieveDnHeader(poHeader.getBuyerOid(),
                            poHeader.getSupplierOid(), invHeader.getInvNo());
                }
                rlt.add(generateMatchingRecord(poHeader, invHeader,
                        grnHeaderList, dnHeader, poHeader.getShipToCode()));
            }
            else
            {
                log.debug(":::: Unknow po sub type " + poHeader.getPoSubType()
                        + " for po [" + poHeader.getPoNo() + "].");
            }
        }
        return rlt;
    }


    private DnHeaderHolder retrieveDnHeader(BigDecimal buyerOid, BigDecimal supplierOid, String invNo)
            throws Exception
    {
        BusinessRuleHolder businessRule = businessRuleService
                .selectRulesByBuyerOidAndFuncGroupAndFuncIdAndRuleId(buyerOid,
                        "Matching", "PoInvGrnDn", "CheckAmtPoInvDn");
        if (businessRule != null)
        {
            DnHeaderHolder dn = dnHeaderService.selectDnHeaderByBuyerSupplierAndInvNo(buyerOid, supplierOid, invNo);
            if (dn != null && dn.getSentToSupplier())
            {
                return dn;
            }
        }
        return null;
    }


    private List<GrnHeaderHolder> retrieveNewGrnRecords(
            List<GrnHeaderHolder> grnList, BigDecimal matchingOid)
            throws Exception
    {
        if (grnList == null || grnList.isEmpty())
        {
            return null;
        }
        List<PoInvGrnDnMatchingGrnHolder> poInvGrnDnMathcingGrnList = poInvGrnDnMatchingGrnService
                .selectByMatchOid(matchingOid);
        if (poInvGrnDnMathcingGrnList == null
                || poInvGrnDnMathcingGrnList.isEmpty())
        {
            return grnList;
        }
        List<String> grnNoList = new ArrayList<String>();
        List<GrnHeaderHolder> rlt = new ArrayList<GrnHeaderHolder>();
        for (PoInvGrnDnMatchingGrnHolder pigdmg : poInvGrnDnMathcingGrnList)
        {
            grnNoList.add(pigdmg.getGrnNo());
        }
        for (GrnHeaderHolder grnHeader : grnList)
        {
            if (grnNoList.contains(grnHeader.getGrnNo()))
            {
                rlt.add(grnHeader);
            }
        }
        grnList.removeAll(rlt);
        return grnList;
    }


    private PoInvGrnDnMatchingHolder generateMatchingRecord(
            PoHeaderHolder poHeader, InvHeaderHolder invHeader,
            List<GrnHeaderHolder> grnHeaderList, DnHeaderHolder dnHeader,
            String storeCode) throws Exception
    {
        PoInvGrnDnMatchingHolder pigdMatching = new PoInvGrnDnMatchingHolder();
        pigdMatching.setMatchingOid(oidService.getOid());
        pigdMatching.setBuyerOid(poHeader.getBuyerOid());
        pigdMatching.setBuyerCode(poHeader.getBuyerCode());
        pigdMatching.setBuyerName(poHeader.getBuyerName());
        pigdMatching.setSupplierOid(poHeader.getSupplierOid());
        pigdMatching.setSupplierCode(poHeader.getSupplierCode());
        pigdMatching.setSupplierName(poHeader.getSupplierName());
        pigdMatching.setPoOid(poHeader.getPoOid());
        pigdMatching.setPoNo(poHeader.getPoNo());
        pigdMatching.setPoDate(poHeader.getPoDate());
        pigdMatching.setPoStoreCode(storeCode);
        pigdMatching.setClosed(false);
        if (invHeader != null)
        {
            pigdMatching.setInvOid(invHeader.getInvOid());
            pigdMatching.setInvNo(invHeader.getInvNo());
            pigdMatching.setInvDate(invHeader.getInvDate());
            pigdMatching.setInvAmt(invHeader.getInvAmountNoVat());
        }
        if (grnHeaderList != null && !grnHeaderList.isEmpty())
        {
            pigdMatching
                    .setGrnList(new ArrayList<PoInvGrnDnMatchingGrnHolder>());
            for (GrnHeaderHolder grnHeader : grnHeaderList)
            {
                PoInvGrnDnMatchingGrnHolder pigdmg = new PoInvGrnDnMatchingGrnHolder();
                pigdmg.setMatchingOid(pigdMatching.getMatchingOid());
                pigdmg.setGrnOid(grnHeader.getGrnOid());
                pigdmg.setGrnNo(grnHeader.getGrnNo());
                pigdmg.setGrnDate(grnHeader.getGrnDate());
                pigdmg.setGrnAmt(grnHeader.getTotalCost());
                pigdMatching.getGrnList().add(pigdmg);
            }
        }
        if (dnHeader != null)
        {
            pigdMatching.setDnOid(dnHeader.getDnOid());
            pigdMatching.setDnNo(dnHeader.getDnNo());
            pigdMatching.setDnDate(dnHeader.getDnDate());
            pigdMatching.setDnAmt(dnHeader.getTotalCost());
        }
        PoInvGrnDnMatchingStatus matchingStatus = this.toMatch(pigdMatching,
                poHeader, invHeader, grnHeaderList, dnHeader);
        pigdMatching.setMatchingStatus(matchingStatus);
        if (matchingStatus.equals(PoInvGrnDnMatchingStatus.MATCHED))
        {
            pigdMatching.setActionStatus(PoInvGrnDnMatchingActionStatus.SYS_APPROVED);
            pigdMatching.setClosed(true);
            pigdMatching.setCloseDate(new Date());
        }
        else
        {
            pigdMatching.setActionStatus(PoInvGrnDnMatchingActionStatus.PENDING);
        }
        return pigdMatching;
    }


    private PoInvGrnDnMatchingHolder otherDocsForOldMatchingRecord(
            PoInvGrnDnMatchingHolder oldMatching,
            List<GrnHeaderHolder> grnHeaderList, DnHeaderHolder dnHeader,
            InvHeaderHolder invHeader) throws Exception
    {
        if (grnHeaderList != null && !grnHeaderList.isEmpty())
        {
            oldMatching
                    .setGrnList(new ArrayList<PoInvGrnDnMatchingGrnHolder>());
            for (GrnHeaderHolder grnHeader : grnHeaderList)
            {
                PoInvGrnDnMatchingGrnHolder pigdmg = new PoInvGrnDnMatchingGrnHolder();
                pigdmg.setMatchingOid(oldMatching.getMatchingOid());
                pigdmg.setGrnOid(grnHeader.getGrnOid());
                pigdmg.setGrnNo(grnHeader.getGrnNo());
                pigdmg.setGrnDate(grnHeader.getGrnDate());
                pigdmg.setGrnAmt(grnHeader.getTotalCost());
                oldMatching.getGrnList().add(pigdmg);
            }
        }
        if (dnHeader != null)
        {
            oldMatching.setDnOid(dnHeader.getDnOid());
            oldMatching.setDnNo(dnHeader.getDnNo());
            oldMatching.setDnDate(dnHeader.getDnDate());
            oldMatching.setDnAmt(dnHeader.getTotalCost());
        }
        if (invHeader != null)
        {
            oldMatching.setInvOid(invHeader.getInvOid());
            oldMatching.setInvNo(invHeader.getInvNo());
            oldMatching.setInvAmt(invHeader.getInvAmountNoVat());
            oldMatching.setInvDate(invHeader.getInvDate());
        }
        
        if (!oldMatching.getMatchingStatus().equals(
                PoInvGrnDnMatchingStatus.MATCHED)
                && ((oldMatching.getGrnList() != null
                && !oldMatching.getGrnList().isEmpty())
                || dnHeader != null || invHeader != null))
        {
            PoInvGrnDnMatchingStatus matchingStatus = this.toMatch(oldMatching,
                    null, invHeader, grnHeaderList, dnHeader);
            oldMatching.setMatchingStatus(matchingStatus);
            if (matchingStatus.equals(PoInvGrnDnMatchingStatus.MATCHED))
            {
                oldMatching.setActionStatus(PoInvGrnDnMatchingActionStatus.SYS_APPROVED);
                oldMatching.setClosed(true);
                oldMatching.setCloseDate(new Date());
            }
        }
        
        return oldMatching;
    }


    private PoInvGrnDnMatchingStatus toMatch(
            PoInvGrnDnMatchingHolder poInvGrnDnMatching,
            PoHeaderHolder poHeader, InvHeaderHolder invHeader,
            List<GrnHeaderHolder> grnHeaderList, DnHeaderHolder dnHeader) throws Exception
    {
        log.info(":::: Start to match po inv grn and dn docs.");
        List<PoInvGrnDnMatchingHolder> poInvGrnDnMatchingList = new ArrayList<PoInvGrnDnMatchingHolder>();
        List<InvHolder> invHolderList = new ArrayList<InvHolder>();
        List<DnHolder> dnHolderList = new ArrayList<DnHolder>();
        List<PoInvGrnDnMatchingGrnHolder> poInvGrnDnMathcingGrnList = null;
        List<GrnHolder> grnHolderList = null;
        PoHolder poHolder = null;
        // if po is null, that mean this matching record existed in db
        if (poHeader == null)
        {
            poInvGrnDnMathcingGrnList = poInvGrnDnMatchingGrnService
                    .selectByMatchOid(poInvGrnDnMatching.getMatchingOid());
            if (poInvGrnDnMatching.getInvOid() == null
                    || ((poInvGrnDnMathcingGrnList == null || poInvGrnDnMathcingGrnList
                            .isEmpty()) && (grnHeaderList == null || grnHeaderList
                            .isEmpty())))
            {
                return PoInvGrnDnMatchingStatus.PENDING;
            }
            
            poHolder = initPoHolderForOldMatching(poInvGrnDnMatching.getPoOid());
            InvHolder invHolder = initInvHolderForOldMatching(poInvGrnDnMatching.getInvOid());
            grnHolderList = initGrnHolderListForOldMatching(poInvGrnDnMatching.getMatchingOid(), grnHeaderList);
            DnHolder dnHolder = initDnHolderForOldMatching(poInvGrnDnMatching.getDnOid());

            if (poInvGrnDnMathcingGrnList == null
                    || poInvGrnDnMathcingGrnList.isEmpty())
            {
                poInvGrnDnMathcingGrnList = new ArrayList<PoInvGrnDnMatchingGrnHolder>();
            }
            if (grnHeaderList != null && !grnHeaderList.isEmpty())
            {
                for (GrnHeaderHolder grnHeader : grnHeaderList)
                {
                    PoInvGrnDnMatchingGrnHolder pigdmg = new PoInvGrnDnMatchingGrnHolder();
                    pigdmg.setMatchingOid(poInvGrnDnMatching.getMatchingOid());
                    pigdmg.setGrnOid(grnHeader.getGrnOid());
                    pigdmg.setGrnNo(grnHeader.getGrnNo());
                    pigdmg.setGrnAmt(grnHeader.getTotalCost());
                    poInvGrnDnMathcingGrnList.add(pigdmg);
                }
            }

            invHolderList.add(invHolder);
            dnHolderList.add(dnHolder);
        }
        // if po not null, this is new matching record
        else
        {
            poHolder = initPoHolderForNewMatching(poInvGrnDnMatching.getPoOid());
            poHolder.setPoHeader(poHeader);
            
            if (invHeader == null || grnHeaderList == null || grnHeaderList.isEmpty())
            {
                poInvGrnDnMatching.setPoAmt(poHolder.amtOfStore(poInvGrnDnMatching.getPoStoreCode()));
                return PoInvGrnDnMatchingStatus.PENDING;
            }
            grnHolderList = initGrnHolderListForNewMatching(grnHeaderList);
            
            InvHolder invHolder = initInvHolderForNewMatching(poInvGrnDnMatching.getInvOid());
            invHolder.setHeader(invHeader);
            if (poInvGrnDnMatching.getDnOid() != null)
            {
                DnHolder dnHolder = initDnHolderForNewMatching(poInvGrnDnMatching.getDnOid());
                dnHolder.setDnHeader(dnHeader);
                dnHolderList.add(dnHolder);
            }
            invHolderList.add(invHolder);
            
            poInvGrnDnMathcingGrnList = poInvGrnDnMatching.getGrnList();
        }
        
        List<BusinessRuleHolder> businessRules = businessRuleService
                .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                        poInvGrnDnMatching.getBuyerOid(), "Matching",
                        "PoInvGrnDn");
        poInvGrnDnMatchingList.add(poInvGrnDnMatching);
        
        PoInvGrnDnReport poInvGrnDnReport = PoInvGrnDnReport
                .initPoInvGrnDnDataSource(poInvGrnDnMatchingList,
                        poInvGrnDnMathcingGrnList, poHolder, invHolderList,
                        grnHolderList, dnHolderList, businessRules,
                        supplierService.selectSupplierByKey(poInvGrnDnMatching
                                .getSupplierOid()));
        poInvGrnDnMatching.setPoAmt(poInvGrnDnReport.getSummaries().get(0)
                .getPoAmt());
        log.debug("::: Matching status is "
                + poInvGrnDnReport.getSummaries().get(0).getMatchingStatus()
                + ".");
        return poInvGrnDnReport.getSummaries().get(0).getMatchingStatus();
    }

    
    private void initPoLocationInfo(PoHolder poHolder, BigDecimal poOid) throws Exception
    {
        List<PoLocationHolder> poLocs = poLocationService
                .selectLocationsByPoOid(poOid);
        List<PoLocationDetailHolder> poLocDetails = poLocationDetailService
                .selectPoLocationDetailsByPoOid(poOid);
        poHolder.setLocations(poLocs);
        poHolder.setLocationDetails(poLocDetails);
    }
    
    
    private PoHolder initPoHolderForNewMatching(BigDecimal poOid) throws Exception
    {
        List<PoDetailHolder> poDetails = poDetailService.selectPoDetailsByPoOid(poOid);
        PoHolder poHolder = new PoHolder();
        poHolder.setDetails(poDetails);
        this.initPoLocationInfo(poHolder, poOid);
        return poHolder;
    }
    
    
    private PoHolder initPoHolderForOldMatching(BigDecimal poOid) throws Exception
    {
        PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(poOid);
        List<PoDetailHolder> poDetails = poDetailService.selectPoDetailsByPoOid(poOid);
        PoHolder poHolder = new PoHolder();
        poHolder.setPoHeader(poHeader);
        poHolder.setDetails(poDetails);
        this.initPoLocationInfo(poHolder, poOid);
        return poHolder;
    }
    
    
    private InvHolder initInvHolderForNewMatching(BigDecimal invOid) throws Exception
    {
        List<InvDetailHolder> invDetails = invDetailService.selectInvDetailByKey(invOid);
        InvHolder invHolder = new InvHolder();
        invHolder.setDetails(invDetails);
        return invHolder;
    }
    
    
    private InvHolder initInvHolderForOldMatching(BigDecimal invOid) throws Exception
    {
        InvHeaderHolder invHeader = invHeaderService.selectInvHeaderByKey(invOid);
        List<InvDetailHolder> invDetails = invDetailService.selectInvDetailByKey(invOid);
        InvHolder invHolder = new InvHolder();
        invHolder.setHeader(invHeader);
        invHolder.setDetails(invDetails);
        return invHolder;
    }
    

    private List<GrnHolder> initGrnHolderListForNewMatching(
            List<GrnHeaderHolder> grnHeaderList) throws Exception
    {
        if (grnHeaderList == null || grnHeaderList.isEmpty())
        {
            return null;
        }
        List<GrnHolder> grnList = new ArrayList<GrnHolder>();
        for (GrnHeaderHolder grnHeader : grnHeaderList)
        {
            GrnHolder grn = new GrnHolder();
            List<GrnDetailHolder> grnDetails = grnDetailService
                    .selectGrnDetailByKey(grnHeader.getGrnOid());
            grn.setHeader(grnHeader);
            grn.setDetails(grnDetails);
            grnList.add(grn);
        }
        return grnList;
    }
    
    
    private List<GrnHolder> initGrnHolderListForOldMatching(
            BigDecimal matchingOid, List<GrnHeaderHolder> grnHeaderList)
            throws Exception
    {
        List<PoInvGrnDnMatchingGrnHolder> poInvGrnDnMathcingGrnList = poInvGrnDnMatchingGrnService
                .selectByMatchOid(matchingOid);
        List<GrnHolder> grnList = new ArrayList<GrnHolder>();
        
        if (poInvGrnDnMathcingGrnList != null
                && !poInvGrnDnMathcingGrnList.isEmpty())
        {
            for (PoInvGrnDnMatchingGrnHolder pigdmg : poInvGrnDnMathcingGrnList)
            {
                GrnHolder grn = new GrnHolder();
                GrnHeaderHolder grnHeader = grnHeaderService
                        .selectGrnHeaderByKey(pigdmg.getGrnOid());
                List<GrnDetailHolder> grnDetails = grnDetailService
                        .selectGrnDetailByKey(grnHeader.getGrnOid());
                grn.setHeader(grnHeader);
                grn.setDetails(grnDetails);
                grnList.add(grn);
            }
        }
        if (grnHeaderList != null && !grnHeaderList.isEmpty())
        {
            for (GrnHeaderHolder grnHeader : grnHeaderList)
            {
                GrnHolder grn = new GrnHolder();
                List<GrnDetailHolder> grnDetails = grnDetailService
                        .selectGrnDetailByKey(grnHeader.getGrnOid());
                grn.setHeader(grnHeader);
                grn.setDetails(grnDetails);
                grnList.add(grn);
            }
        }
        return grnList;
    }
    
    
    public DnHolder initDnHolderForNewMatching(BigDecimal dnOid) throws Exception
    {
        List<DnDetailHolder> dnDetails = dnDetailService.selectDnDetailByKey(dnOid);
        DnHolder dnHolder = new DnHolder();
        dnHolder.setDnDetail(dnDetails);
        return dnHolder;
    }

    
    public DnHolder initDnHolderForOldMatching(BigDecimal dnOid) throws Exception
    {
        if (dnOid == null)
        {
            return null;
        }
        DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(dnOid);
        List<DnDetailHolder> dnDetails = dnDetailService.selectDnDetailByKey(dnOid);
        DnHolder dnHolder = new DnHolder();
        dnHolder.setDnHeader(dnHeader);
        dnHolder.setDnDetail(dnDetails);
        return dnHolder;
    }

    // *****************************************************
    // getter and setter
    // *****************************************************

    public void setPoInvGrnDnMatchingService(
            PoInvGrnDnMatchingService poInvGrnDnMatchingService)
    {
        this.poInvGrnDnMatchingService = poInvGrnDnMatchingService;
    }


    public void setPoInvGrnDnMatchingGrnService(
            PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService)
    {
        this.poInvGrnDnMatchingGrnService = poInvGrnDnMatchingGrnService;
    }


    public void setPoHeaderService(PoHeaderService poHeaderService)
    {
        this.poHeaderService = poHeaderService;
    }


    public void setInvHeaderService(InvHeaderService invHeaderService)
    {
        this.invHeaderService = invHeaderService;
    }


    public void setPoLocationService(PoLocationService poLocationService)
    {
        this.poLocationService = poLocationService;
    }


    public void setGrnHeaderService(GrnHeaderService grnHeaderService)
    {
        this.grnHeaderService = grnHeaderService;
    }


    public void setDnHeaderService(DnHeaderService dnHeaderService)
    {
        this.dnHeaderService = dnHeaderService;
    }


    public void setBusinessRuleService(BusinessRuleService businessRuleService)
    {
        this.businessRuleService = businessRuleService;
    }


    public void setOidService(OidService oidService)
    {
        this.oidService = oidService;
    }


    public void setPoDetailService(PoDetailService poDetailService)
    {
        this.poDetailService = poDetailService;
    }


    public void setInvDetailService(InvDetailService invDetailService)
    {
        this.invDetailService = invDetailService;
    }


    public void setGrnDetailService(GrnDetailService grnDetailService)
    {
        this.grnDetailService = grnDetailService;
    }


    public void setDnDetailService(DnDetailService dnDetailService)
    {
        this.dnDetailService = dnDetailService;
    }


    public void setSupplierService(SupplierService supplierService)
    {
        this.supplierService = supplierService;
    }


    public void setStandardEmailSender(StandardEmailSender standardEmailSender)
    {
        this.standardEmailSender = standardEmailSender;
    }


    public void setPoLocationDetailService(
            PoLocationDetailService poLocationDetailService)
    {
        this.poLocationDetailService = poLocationDetailService;
    }


    public void setExpiredDays(int expiredDays)
    {
        this.expiredDays = expiredDays;
    }*/

}
