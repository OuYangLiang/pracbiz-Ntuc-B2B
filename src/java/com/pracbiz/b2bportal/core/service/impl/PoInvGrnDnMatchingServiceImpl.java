package com.pracbiz.b2bportal.core.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.DocContextRef;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.DocClassHolder;
import com.pracbiz.b2bportal.core.holder.DocSubclassHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.BusinessRuleExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingExHolder;
import com.pracbiz.b2bportal.core.mapper.DocClassMapper;
import com.pracbiz.b2bportal.core.mapper.DocSubclassMapper;
import com.pracbiz.b2bportal.core.mapper.PoInvGrnDnMatchingDetailMapper;
import com.pracbiz.b2bportal.core.mapper.PoInvGrnDnMatchingGrnMapper;
import com.pracbiz.b2bportal.core.mapper.PoInvGrnDnMatchingMapper;
import com.pracbiz.b2bportal.core.report.excel.MatchingReportParameter;
import com.pracbiz.b2bportal.core.report.excel.StandardMatchingReport;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ClassService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.GrnService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingDetailService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.service.SubclassService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public class PoInvGrnDnMatchingServiceImpl extends
        DBActionServiceDefaultImpl<PoInvGrnDnMatchingHolder> implements
        PoInvGrnDnMatchingService, CoreCommonConstants
{
    @Autowired
    private OidService oidService;
    @Autowired
    private PoInvGrnDnMatchingMapper poInvGrnDnMatchingMapper;
    @Autowired
    private PoInvGrnDnMatchingGrnMapper poInvGrnDnMatchingGrnMapper;
    @Autowired
    private PoInvGrnDnMatchingDetailMapper poInvGrnDnMatchingDetailMapper;
    @Autowired
    private PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService; 
    @Autowired
    private BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private MsgTransactionsService msgTransactionsService ;
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private transient PoService poService;
    @Autowired
    private transient BusinessRuleService businessRuleService;
    @Autowired
    private transient SupplierService supplierService;
    @Autowired
    private transient MailBoxUtil mboxUtil;
    @Autowired
    private transient DnService dnService;
    @Autowired
    private transient InvoiceService invoiceService;
    @Autowired
    private transient GrnService grnService;
    @Autowired
    private transient PoInvGrnDnMatchingDetailService poInvGrnDnMatchingDetailService;
    @Autowired
    private transient StandardMatchingReport standardMatchingReport;
    @Autowired 
    transient private DocFileHandler<InvDocMsg, InvHolder> ebxmlInvDocFileHandler;
    @Autowired 
    transient private DocFileHandler<InvDocMsg, InvHolder> canonicalInvDocFileHandler;
    @Autowired 
    transient private DocFileHandler<InvDocMsg, InvHolder> idocInvDocFileHandler;
    @Autowired
    transient private ClassService classService;
    @Autowired
    transient private SubclassService subclassService;
    @Autowired
    transient private DocClassMapper docClassMapper;
    @Autowired
    transient private DocSubclassMapper docSubclassMapper;
    
    @Override
    public List<PoInvGrnDnMatchingHolder> select(PoInvGrnDnMatchingHolder param)
            throws Exception
    {
        return poInvGrnDnMatchingMapper.select(param);
    }


    @Override
    public void delete(PoInvGrnDnMatchingHolder oldObj) throws Exception
    {
        poInvGrnDnMatchingMapper.delete(oldObj);
    }


    @Override
    public void insert(PoInvGrnDnMatchingHolder newObj) throws Exception
    {
        poInvGrnDnMatchingMapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(PoInvGrnDnMatchingHolder oldObj,
            PoInvGrnDnMatchingHolder newObj) throws Exception
    {
        poInvGrnDnMatchingMapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(PoInvGrnDnMatchingHolder oldObj,
            PoInvGrnDnMatchingHolder newObj) throws Exception
    {
        poInvGrnDnMatchingMapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public int getCountOfSummary(PoInvGrnDnMatchingExHolder param)
            throws Exception
    {
        return poInvGrnDnMatchingMapper.getCountOfSummary(param);
    }


    @Override
    public List<PoInvGrnDnMatchingExHolder> getListOfSummary(
            PoInvGrnDnMatchingExHolder param) throws Exception
    {
        return poInvGrnDnMatchingMapper.getListOfSummary(param);
    }


    @Override
    public PoInvGrnDnMatchingHolder selectByKey(BigDecimal matchingOid) throws Exception
    {
        if(matchingOid == null)
        {
            throw new IllegalArgumentException();
        }
        PoInvGrnDnMatchingHolder param = new PoInvGrnDnMatchingHolder();
        param.setMatchingOid(matchingOid);
        List<PoInvGrnDnMatchingHolder> list = this.select(param);
        if(list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }


    @Override
    public void moveFile(PoInvGrnDnMatchingHolder holder) throws Exception
    {
        BuyerHolder buyer = buyerService.selectBuyerByKey(holder.getBuyerOid());
        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(holder.getInvOid());
        File source = new File(mboxUtil.getBuyerMailBox(buyer.getMboxId()) + PS + "tmp" + PS + msg.getExchangeFilename());//appConfig.getBuyerMailboxRootPath() + PS + buyer.getMboxId()
        // copy file to buyer in folder
        FileUtil.getInstance().copyFile(source, new File(getTargetPath(buyer, MsgType.INV.name()), msg.getExchangeFilename()), true);
        // move file to buyer archive in folder
        FileUtil.getInstance().moveFile(source, getArchiveInPath(buyer));
    }
    
    
    private File getTargetPath(BuyerHolder buyer, String type) throws Exception
    {
        File rlt = null;

        BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), type);
        if ("INTERVAL".equals(setting.getAlertFrequency().toString()))
        {
            rlt = new File(mboxUtil.getBuyerMailBox(buyer.getMboxId()) + PS + "on-hold");//appConfig.getBuyerMailboxRootPath() + PS + buyer.getMboxId() + PS + "on-hold"
        }
        else
        {
            rlt = new File(mboxUtil.getBuyerInPath(buyer.getMboxId()));//appConfig.getBuyerMailboxRootPath() + PS + buyer.getMboxId() + PS + "in"
        }

        if (!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }

        return rlt;
    }
    
    
    private String getArchiveInPath(BuyerHolder buyer) throws Exception
    {
        File rlt = new File(mboxUtil.getFolderInBuyerArchInPath(buyer.getMboxId(), 
            DateUtil.getInstance().getYearAndMonth(new Date())));
        if (!rlt.isDirectory())
        {
            FileUtil.getInstance().createDir(rlt);
        }
        return rlt.getAbsolutePath();
    }

    
    @Override
    public byte[] exportExcel(List<BigDecimal> matchingOids, boolean isBuyer) throws Exception
    {
        if (matchingOids == null || matchingOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        List<PoInvGrnDnMatchingHolder> poInvGrnDnMatchingList = new ArrayList<PoInvGrnDnMatchingHolder>();
        for (BigDecimal matchingOid : matchingOids)
        {
            poInvGrnDnMatchingList.add(this.selectByKey(matchingOid));
        }
        Map<String, List<PoInvGrnDnMatchingHolder>> mapUseBuyerCodeAsKey = this
                .groupPoInvGrnDnMatchingListByBuyerCode(poInvGrnDnMatchingList);
        List<MatchingReportParameter> reports = new ArrayList<MatchingReportParameter>();
        Map<String, List<BusinessRuleHolder>> businessRuleMap = new HashMap<String, List<BusinessRuleHolder>>();
        Map<String, BigDecimal> toleranceMap = new HashMap<String, BigDecimal>();
        for (Map.Entry<String, List<PoInvGrnDnMatchingHolder>> entry1 : mapUseBuyerCodeAsKey
                .entrySet())
        {
            BuyerHolder buyer = buyerService.selectBuyerByBuyerCode(entry1
                    .getKey());

            List<BusinessRuleHolder> businessRuleList = businessRuleService
                    .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                            buyer.getBuyerOid(), "Matching", "PoInvGrnDn");
            businessRuleMap.put(entry1.getKey(), businessRuleList);

            BusinessRuleExHolder tolerance = businessRuleService
                    .selectRulesByKey(
                            buyer.getBuyerOid(), "Matching", "PoInvGrnDn",
                            "AmountTolerance");
            toleranceMap.put(buyer.getBuyerCode(), tolerance == null ? BigDecimal.ZERO : tolerance.getNumValue());

            Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> mapUsePoOidAsKey = this
                    .groupPoInvGrnDnMatchingListByPo(entry1.getValue());
            for (Map.Entry<BigDecimal, List<PoInvGrnDnMatchingHolder>> entry2 : mapUsePoOidAsKey
                    .entrySet())
            {
                BigDecimal poOid = entry2.getKey();
                List<PoInvGrnDnMatchingHolder> poInvGrnDnMatchingResultList = entry2
                        .getValue();
                PoHolder poHolder = poService.selectPoByKey(poOid);

                SupplierHolder supplier = supplierService
                        .selectSupplierByKey(poHolder.getPoHeader()
                                .getSupplierOid());
                
                for (PoInvGrnDnMatchingHolder holder : poInvGrnDnMatchingResultList)
                {
                    
                    List<PoInvGrnDnMatchingGrnHolder> poInvGrnDnMathcingGrnList = new ArrayList<PoInvGrnDnMatchingGrnHolder>();
                    InvHolder invHolder = null;
                    DnHolder dnHolder = null;
                    List<GrnHolder> grnHolderList = new ArrayList<GrnHolder>();
                    if (holder.getInvOid() != null)
                    {
                        invHolder = invoiceService
                                .selectInvoiceByKey(holder.getInvOid());
                    }
                    if (holder.getDnOid() != null)
                    {
                        dnHolder = dnService.selectDnByKey(holder
                                .getDnOid());
                    }
                    poInvGrnDnMathcingGrnList
                    .addAll(poInvGrnDnMatchingGrnService
                            .selectByMatchOid(holder
                                    .getMatchingOid()));
                    if (poInvGrnDnMathcingGrnList != null
                            && !poInvGrnDnMathcingGrnList.isEmpty())
                    {
                        for (PoInvGrnDnMatchingGrnHolder poInvGrnDnMathcingGrn : poInvGrnDnMathcingGrnList)
                        {
                            GrnHolder grnHolder = grnService
                                    .selectByKey(poInvGrnDnMathcingGrn
                                            .getGrnOid());
                            grnHolderList.add(grnHolder);
                            
                        }
                    }
                    holder.setGrnList(poInvGrnDnMathcingGrnList);
                    holder.setDetailList(poInvGrnDnMatchingDetailService.selectByMatchingOid(holder.getMatchingOid()));
                    MatchingReportParameter poInvGrnDnReport = new MatchingReportParameter(
                            holder, poHolder, invHolder, grnHolderList,
                            dnHolder,
                            businessRuleMap.get(holder.getBuyerCode()),
                            supplier.getSupplierSource().name(), false);
                    reports.add(poInvGrnDnReport);
                }
            }
        }
        
        return standardMatchingReport.exportExcel(reports, businessRuleMap,
                toleranceMap, isBuyer);
    }
    
    
    private Map<String, List<PoInvGrnDnMatchingHolder>> groupPoInvGrnDnMatchingListByBuyerCode(List<PoInvGrnDnMatchingHolder> list) throws Exception
    {
        Map<String, List<PoInvGrnDnMatchingHolder>> map = new HashMap<String, List<PoInvGrnDnMatchingHolder>>();
        if (list == null || list.isEmpty())
        {
            return map;
        }
        for (PoInvGrnDnMatchingHolder holder : list)
        {
            if (map.containsKey(holder.getBuyerCode()))
            {
                map.get(holder.getBuyerCode()).add(holder);
            }
            else
            {
                List<PoInvGrnDnMatchingHolder> poInvGrnDnMatchingList = new ArrayList<PoInvGrnDnMatchingHolder>();
                poInvGrnDnMatchingList.add(holder);
                map.put(holder.getBuyerCode(), poInvGrnDnMatchingList);
            }
        }
        return map;
    }
    
    
    private Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> groupPoInvGrnDnMatchingListByPo(List<PoInvGrnDnMatchingHolder> list) throws Exception
    {
        Map<BigDecimal, List<PoInvGrnDnMatchingHolder>> map = new HashMap<BigDecimal, List<PoInvGrnDnMatchingHolder>>();
        if (list == null || list.isEmpty())
        {
            return map;
        }
        for (PoInvGrnDnMatchingHolder holder : list)
        {
            if (map.containsKey(holder.getPoOid()))
            {
                map.get(holder.getPoOid()).add(holder);
            }
            else
            {
                List<PoInvGrnDnMatchingHolder> poInvGrnDnMatchingList = new ArrayList<PoInvGrnDnMatchingHolder>();
                poInvGrnDnMatchingList.add(holder);
                map.put(holder.getPoOid(), poInvGrnDnMatchingList);
            }
        }
        return map;
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectAllRecordToExport(PoInvGrnDnMatchingHolder holder)
    {
        return poInvGrnDnMatchingMapper.selectAllRecordToExport(holder);
    }


    @Override
    public PoInvGrnDnMatchingHolder selectByPoOidAndStore(BigDecimal poOid,
        String store) throws Exception
    {
        if (null == poOid || null == store || store.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        PoInvGrnDnMatchingHolder param = new PoInvGrnDnMatchingHolder();
        param.setPoOid(poOid);
        param.setPoStoreCode(store);
        
        List<PoInvGrnDnMatchingHolder> rlt = this.select(param);
        
        if (null != rlt && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public void createNewMatchingRecord(PoInvGrnDnMatchingHolder param)
        throws Exception
    {
        param.setMatchingOid(oidService.getOid());
        poInvGrnDnMatchingMapper.insert(param);
        
        if (null != param.getGrnList())
        {
            for (PoInvGrnDnMatchingGrnHolder matchingGrn : param.getGrnList())
            {
                matchingGrn.setMatchingOid(param.getMatchingOid());
                poInvGrnDnMatchingGrnMapper.insert(matchingGrn);
            }
        }
        
        if (null != param.getDetailList())
        {
            for (PoInvGrnDnMatchingDetailHolder matchingDetail : param.getDetailList())
            {
                matchingDetail.setMatchingOid(param.getMatchingOid());
                poInvGrnDnMatchingDetailMapper.insert(matchingDetail);
            }
            
            this.createNewMatchingClassInfo(param);
        }
        
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectMatchingRecordsWhichCanDoMatching(
        BigDecimal buyerOid, int expiryDays, int toleranceDays) throws Exception
    {
        if (null == buyerOid || toleranceDays < 0)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("expiryDays", expiryDays);
        map.put("toleranceDays", toleranceDays);
        
        return poInvGrnDnMatchingMapper.selectMatchingRecordsWhichCanDoMatching(map);
    }


    @Override
    public void updateDocRelationshipsForMatchingRecord(
        PoInvGrnDnMatchingHolder param) throws Exception
    {
        if (null == param || param.getMatchingOid() == null)
        {
            throw new IllegalArgumentException();
        }
        PoInvGrnDnMatchingDetailHolder matchingDetail = new PoInvGrnDnMatchingDetailHolder();
        matchingDetail.setMatchingOid(param.getMatchingOid());
        poInvGrnDnMatchingDetailMapper.delete(matchingDetail);
        
        DocClassHolder docClass = new DocClassHolder();
        docClass.setDocOid(param.getMatchingOid());
        docClassMapper.delete(docClass);
        
        DocSubclassHolder docSubClass = new DocSubclassHolder();
        docSubClass.setDocOid(param.getMatchingOid());
        docSubclassMapper.delete(docSubClass);
        
        PoInvGrnDnMatchingGrnHolder matchingGrn = new PoInvGrnDnMatchingGrnHolder();
        matchingGrn.setMatchingOid(param.getMatchingOid());
        poInvGrnDnMatchingGrnMapper.delete(matchingGrn);
        
        PoInvGrnDnMatchingHolder matching = new PoInvGrnDnMatchingHolder();
        matching.setMatchingOid(param.getMatchingOid());
        poInvGrnDnMatchingMapper.delete(matching);
        
        poInvGrnDnMatchingMapper.insert(param);
        
        if (null != param.getGrnList())
        {
            for (PoInvGrnDnMatchingGrnHolder grn : param.getGrnList())
            {
                grn.setMatchingOid(param.getMatchingOid());
                poInvGrnDnMatchingGrnMapper.insert(grn);
            }
        }
        
        if (null != param.getDetailList())
        {
            for (PoInvGrnDnMatchingDetailHolder detail : param.getDetailList())
            {
                detail.setMatchingOid(param.getMatchingOid());
                poInvGrnDnMatchingDetailMapper.insert(detail);
            }
            
            this.createNewMatchingClassInfo(param);
        }
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectVoidInvoiceMatchingRecords(
            BigDecimal buyerOid) throws Exception
    {
        if (null == buyerOid)
        {
            throw new IllegalArgumentException();
        }
        
        return poInvGrnDnMatchingMapper.selectVoidInvoiceMatchingRecords(buyerOid);
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectOutDatedPoRecords(
            BigDecimal buyerOid) throws Exception
    {
        if (null == buyerOid)
        {
            throw new IllegalArgumentException();
        }
        
        return poInvGrnDnMatchingMapper.selectOutDatedPoRecords(buyerOid);
    }


    @Override
    public void deleteRecordByKey(BigDecimal matchingOid) throws Exception
    {
        if (null == matchingOid)
        {
            throw new IllegalArgumentException();
        }
        PoInvGrnDnMatchingDetailHolder matchingDetail = new PoInvGrnDnMatchingDetailHolder();
        matchingDetail.setMatchingOid(matchingOid);
        poInvGrnDnMatchingDetailMapper.delete(matchingDetail);
        
        PoInvGrnDnMatchingGrnHolder matchingGrn = new PoInvGrnDnMatchingGrnHolder();
        matchingGrn.setMatchingOid(matchingOid);
        poInvGrnDnMatchingGrnMapper.delete(matchingGrn);
        
        PoInvGrnDnMatchingHolder matching = new PoInvGrnDnMatchingHolder();
        matching.setMatchingOid(matchingOid);
        poInvGrnDnMatchingMapper.delete(matching);
        
        
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectMatchingRecordByMatchingDateRangeUnionAllPending(BigDecimal buyerOid,
            Date begin, Date end, BigDecimal supplierOid) throws Exception
    {
        PoInvGrnDnMatchingHolder param  = new PoInvGrnDnMatchingHolder();
        param.setBuyerOid(buyerOid);
        param.setMatchingDateFrom(begin);
        param.setMatchingDateTo(end);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("matchingDateFrom", begin);
        map.put("matchingDateTo", end);
        map.put("supplierOid", supplierOid);
        return poInvGrnDnMatchingMapper.selectMatchingRecordByMatchingDateRangeUnionAllPending(map);
    }
    
    
    @Override
    public List<PoInvGrnDnMatchingHolder> selectApprovedMatchingRecordByInvStatusActionDateRange(BigDecimal buyerOid,
            Date begin, Date end, BigDecimal supplierOid) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("begin", begin);
        map.put("end", end);
        map.put("supplierOid", supplierOid);
        return poInvGrnDnMatchingMapper.selectApprovedMatchingRecordByInvStatusActionDateRange(map);
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectOutstandingRecords(
            BigDecimal buyerOid, Date reportDate, String moreThanDays, String supplierCode, String supplierName) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        
        if (reportDate != null)
        {
            map.put("reportDate", reportDate);
        }
        if (moreThanDays != null)
        {
            map.put("moreThanDays", moreThanDays);
        }
        if (supplierCode != null)
        {
            map.put("supplierCode", supplierCode);
        }
        if (supplierName != null)
        {
            map.put("supplierName", supplierName);
        }
        return poInvGrnDnMatchingMapper.selectOutstandingRecords(map);
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectSupplierRejectedRecordsBySupplierStatusActionDateRangeAndBuyer(
            BigDecimal buyerOid, Date begin, Date end, BigDecimal supplierOid) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoInvGrnDnMatchingHolder param  = new PoInvGrnDnMatchingHolder();
        param.setBuyerOid(buyerOid);
        param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.REJECTED);
        param.setSupplierStatusActionDateFrom(begin);
        param.setSupplierStatusActionDateTo(end);
        if (null != supplierOid)
        {
            param.setSupplierOid(supplierOid);
        }
        
        return select(param);
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectSupplierAcceptedRecordsBySupplierStatusActionDateRangeAndBuyer(
            BigDecimal buyerOid, Date begin, Date end, BigDecimal supplierOid) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoInvGrnDnMatchingHolder param  = new PoInvGrnDnMatchingHolder();
        param.setBuyerOid(buyerOid);
        param.setSupplierStatus(PoInvGrnDnMatchingSupplierStatus.ACCEPTED);
        param.setSupplierStatusActionDateFrom(begin);
        param.setSupplierStatusActionDateTo(end);
        if (null != supplierOid)
        {
            param.setSupplierOid(supplierOid);
        }
        
        return select(param);
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectBuyerResolutionRecords(BigDecimal buyerOid,
            Date reportDate, String supplierCode, String supplierName) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        if (reportDate != null)
        {
            map.put("reportDate", reportDate);
        }
        if (supplierCode != null && !supplierCode.trim().isEmpty())
        {
            map.put("supplierCode", supplierCode);
        }
        if (supplierName != null && !supplierName.trim().isEmpty())
        {
            map.put("supplierName", supplierName);
        }
        return poInvGrnDnMatchingMapper.selectBuyerResolutionRecords(map);
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectSupplierResolutionRecords(BigDecimal supplierOid) throws Exception
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        return poInvGrnDnMatchingMapper.selectSupplierResolutionRecords(supplierOid);
    }


    @Override
    public List<PoInvGrnDnMatchingHolder> selectDiscrepancyRecordByBuyerAndSupplier(
            BigDecimal buyerOid, BigDecimal supplierOid, String discrepancyType,
            BigDecimal currentUserType) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("supplierOid", supplierOid);
        map.put("discrepancyType", discrepancyType);
        map.put("currentUserType", currentUserType);
        return poInvGrnDnMatchingMapper.selectDiscrepancyRecordByBuyerAndSupplier(map);
    }

    
    @Override
    public List<PoInvGrnDnMatchingHolder> selectMatchingRecordByMatchingDateRangeAndBuyer(
            Date begin, Date end, BigDecimal buyerOid) throws Exception
    {
        PoInvGrnDnMatchingHolder param = new PoInvGrnDnMatchingHolder();
        param.setMatchingDateFrom(begin);
        param.setMatchingDateTo(end);
        param.setBuyerOid(buyerOid);
        return select(param);
    }


    @Override
    public PoInvGrnDnMatchingHolder selectEffectiveRecordByInvOid(BigDecimal invOid)
            throws Exception
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoInvGrnDnMatchingHolder param = new PoInvGrnDnMatchingHolder();
        param.setInvOid(invOid);
        List<PoInvGrnDnMatchingHolder> list = this.select(param);
        
        if (list == null || list.isEmpty())
        {
            return null;
        }
        
        for (PoInvGrnDnMatchingHolder matching : list)
        {
            if (!PoInvGrnDnMatchingStatus.OUTDATED.equals(matching.getMatchingStatus()))
            {
                return matching;
            }
        }
        
        return null;
    }


    @Override
    public void changeInvDateToFirstGrnDate(PoInvGrnDnMatchingHolder holder)
        throws Exception
    {
        boolean isChange = businessRuleService.isMatchingChangeInvDateToFirstGrnDate(holder.getBuyerOid());
        if (!isChange)
        {
            return;
        }
        //get all matching grns
        List<PoInvGrnDnMatchingGrnHolder> grns = poInvGrnDnMatchingGrnService.selectByMatchOid(holder.getMatchingOid());
        if (grns ==null || grns.isEmpty())
        {
            return;
        }
        
        BuyerHolder buyer = buyerService.selectBuyerByKey(holder.getBuyerOid());
        SupplierHolder supplier = supplierService.selectSupplierByKey(holder.getSupplierOid());
        
        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(holder.getInvOid());
        File source = new File(mboxUtil.getBuyerMailBox(buyer.getMboxId()) + PS + "tmp" + PS + msg.getExchangeFilename());
        
        BuyerMsgSettingHolder buyerMsgSetting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), MsgType.INV.name());
        
        //init DocMsg and get original inv file content
        InvDocMsg docMsg = new InvDocMsg();
        DocContextRef context = new DocContextRef();
        context.setWorkDir(mboxUtil.getBuyerMailBox(buyer.getMboxId()) + PS + "tmp" + PS);
        docMsg.setContext(context);
        docMsg.setOriginalFilename(msg.getExchangeFilename());
        docMsg.setDocOid(holder.getInvOid());
        docMsg.setBuyer(buyer);
        docMsg.setSupplier(supplier);
        
        if (DocFileHandler.CANONICAL.equalsIgnoreCase(buyerMsgSetting.getFileFormat()))
        {
            docMsg.setOutputFormat(DocFileHandler.CANONICAL);
            canonicalInvDocFileHandler.parseSourceFile(docMsg);
        }
        else if (DocFileHandler.EBXML.equalsIgnoreCase(buyerMsgSetting.getFileFormat()))
        {
            docMsg.setOutputFormat(DocFileHandler.EBXML);
            docMsg.setSenderOid(supplier.getSupplierOid());
            docMsg.setSenderCode(supplier.getSupplierCode());
            docMsg.setReceiverOid(buyer.getBuyerOid());
            docMsg.setReceiverCode(buyer.getBuyerCode());
            ebxmlInvDocFileHandler.parseSourceFile(docMsg);
        }
        else
        {
            InvHolder invoice = invoiceService.selectInvoiceByKey(holder.getInvOid());
            docMsg.setData(invoice);
        }
        //delete original inv file in tmp folder
        FileUtil.getInstance().deleleAllFile(source);
        
        //sort the grns by grn date
        Collections.sort(grns, new Comparator<PoInvGrnDnMatchingGrnHolder>()
        {
            @Override
            public int compare(PoInvGrnDnMatchingGrnHolder m1, PoInvGrnDnMatchingGrnHolder m2)
            {
                return m1.getGrnDate().before(m2.getGrnDate()) ? -1 : 1;
            }
        });
        
        //replace inv date to first grn date
        docMsg.getData().getHeader().setInvDate(grns.get(0).getGrnDate());
        
        //create new inv file in buyer's tmp folder
        if (DocFileHandler.CANONICAL.equalsIgnoreCase(buyerMsgSetting.getFileFormat()))
        {
            docMsg.setInputFormat(DocFileHandler.CANONICAL);
            canonicalInvDocFileHandler.createTargerFile(docMsg);
        }
        else if (DocFileHandler.EBXML.equalsIgnoreCase(buyerMsgSetting.getFileFormat()))
        {
            docMsg.setInputFormat(DocFileHandler.EBXML);
            ebxmlInvDocFileHandler.createTargerFile(docMsg);
        }
        else
        {
            docMsg.setInputFormat(DocFileHandler.FP_IDOC);
            idocInvDocFileHandler.createTargerFile(docMsg);
        }
        
    }
    
    
    private void createNewMatchingClassInfo(PoInvGrnDnMatchingHolder param) throws Exception
    {
      //insert matchingClass
        List<String> classCodes = new ArrayList<String>();
        for(PoInvGrnDnMatchingDetailExHolder matchingDetail : param.getDetailList())
        {
            ClassHolder classHolder = classService.selectClassByItemCodeAndBuyerOid(
                matchingDetail.getBuyerItemCode(), param.getBuyerOid());
            if (classHolder == null || classCodes.contains(classHolder.getClassCode()))
            {
                continue;
            }
            classCodes.add(classHolder.getClassCode());
        }
        
        if (!classCodes.isEmpty())
        {
            for (String classCode : classCodes)
            {
                DocClassHolder matchingClass = new DocClassHolder();
                matchingClass.setDocOid(param.getMatchingOid());
                matchingClass.setClassCode(classCode);
                
                docClassMapper.insert(matchingClass);
            }
        }
        
        //insert matchingSubClass
        Map<String, SubclassHolder> subclassMap = new HashMap<String, SubclassHolder>();
        for(PoInvGrnDnMatchingDetailExHolder matchingDetail : param.getDetailList())
        {
            SubclassHolder subclassHolder = subclassService.selectSubclassExByItemCodeAndBuyerOid(
                matchingDetail.getBuyerItemCode(), param.getBuyerOid());
            if (subclassHolder == null || subclassMap.containsKey(subclassHolder.getSubclassCode()))
            {
                continue;
            }
            subclassMap.put(subclassHolder.getSubclassCode(), subclassHolder);
        }
        
        if (!subclassMap.isEmpty())
        {
            for (Map.Entry<String, SubclassHolder> entry : subclassMap.entrySet())
            {
                DocSubclassHolder matchingSubclass = new DocSubclassHolder();
                matchingSubclass.setDocOid(param.getMatchingOid());
                matchingSubclass.setSubclassCode(entry.getKey());
                ClassHolder classHolder = classService.selectByKey(entry.getValue().getClassOid());
                matchingSubclass.setClassCode(classHolder.getClassCode());
                matchingSubclass.setAuditFinished(false);
                docSubclassMapper.insert(matchingSubclass);
            }
        }
    }
}
