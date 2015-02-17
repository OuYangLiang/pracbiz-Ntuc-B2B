package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MatchingReportHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.report.excel.BuyerResolutionReport;
import com.pracbiz.b2bportal.core.report.excel.DnBuyerResolutionReport;
import com.pracbiz.b2bportal.core.report.excel.DnOutstandingReport;
import com.pracbiz.b2bportal.core.report.excel.MatchingReportParameter;
import com.pracbiz.b2bportal.core.report.excel.OutstandingMatchingReport;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.GrnService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingDetailService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */
public class TrackingReportAction extends ProjectBaseAction implements
    CoreCommonConstants
{
    private static final long serialVersionUID = 8494675764815650391L;
    private static final Logger log = LoggerFactory.getLogger(TrackingReportAction.class);
    
    
    private static final String EXCEL_IS_GENERATING = "isGenerating";
    private static final String MATCHING_RESOLUTION_DATA = "matchingResolutionData";
    private static final String OUTSTANDING_DATA = "outstandingData";
    private static final String BUYER_DATA = "buyerData";
    private static final String DN_OUTSTANDING_DATA = "dnOutstandingData";
    private static final String DN_RESOLUTION_DATA = "dnResolutionData";
    
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private SupplierService supplierService;
    @Autowired transient private PoService poService;
    @Autowired transient private InvoiceService invoiceService;
    @Autowired transient private GrnService grnService;
    @Autowired transient private PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired transient private PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService;
    @Autowired transient private PoInvGrnDnMatchingDetailService poInvGrnDnMatchingDetailService;
    @Autowired transient private BusinessRuleService businessRuleService;
    @Autowired transient private OutstandingMatchingReport outstandingMatchingReport;
    @Autowired transient private BuyerResolutionReport buyerResolutionReport;
    @Autowired transient private DnOutstandingReport dnOutstandingReport;
    @Autowired transient private DnBuyerResolutionReport dnBuyerResolutionReport;
    @Autowired transient private DnHeaderService dnHeaderService;
    @Autowired transient private DnService dnService;
    @Autowired transient private ControlParameterService controlParameterService;
    
    private transient InputStream rptResult;
    private String contentType;
    private String rptFileName;
    private String errorMsg;
    private String data;
    
    private MatchingReportHolder param;
    private Map<String,String> reportType;
    private Map<String,String> msgType;
    protected List<? extends Object> buyers;
    
    // *****************************************************
    // search page
    // *****************************************************
    public String init()
    {
        try
        {
            if (param == null)
            {
                param = new MatchingReportHolder();
                param.setMoreThanDays("0");
                param.setReportDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                param.setReportDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
                initSearchCriteria();
            }
        }
        catch (Exception e)
        {
            this.handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String exportExcel()
    {
        try
        {
            this.getSession().put(EXCEL_IS_GENERATING, "Y");
            byte[] datas = null;
            
            String ts = DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date());
            BuyerHolder buyer = (BuyerHolder)this.getSession().get(BUYER_DATA);
            //get data from session
            
            //Outstanding Matching Report
            if ((List<MatchingReportParameter>)this.getSession().get(OUTSTANDING_DATA) != null)
            {
                datas = outstandingMatchingReport.exportExcel(
                    (List<MatchingReportParameter>)this.getSession().get(OUTSTANDING_DATA), buyer);
                rptFileName = "Outstanding Matching Report - " + buyer.getBuyerCode() + "-" + ts + ".xls";
                contentType = "application/vnd.ms-excel";
            }
            //Resolution Report To Buyer
            if ((Map<String, byte[]>)this.getSession().get(MATCHING_RESOLUTION_DATA) != null
                && !((Map<String, byte[]>)this.getSession().get(MATCHING_RESOLUTION_DATA)).isEmpty())
            {
                datas = GZIPHelper.getInstance().zipByte((Map<String, byte[]>)this.getSession().get(MATCHING_RESOLUTION_DATA));
                
                rptFileName = "Resolution Report To Buyer - " + buyer.getBuyerCode() + "-" + ts + ".zip";
                contentType = "application/zip";
            }
            //DN Outstanding Matching Report
            if ((List<DnHolder>)this.getSession().get(DN_OUTSTANDING_DATA) != null)
            {
                datas = dnOutstandingReport.exportExcel((List<DnHolder>)this.getSession().get(DN_OUTSTANDING_DATA), buyer);
                rptFileName = "Dn Outstanding Report - " + buyer.getBuyerCode() + "-" + ts + ".xls";
                contentType = "application/vnd.ms-excel";
            }
            //DN Resolution Report to buyer
            if ((Map<String, byte[]>)this.getSession().get(DN_RESOLUTION_DATA) != null
                && !((Map<String, byte[]>)this.getSession().get(DN_RESOLUTION_DATA)).isEmpty())
            {
                datas = GZIPHelper.getInstance().zipByte((Map<String, byte[]>)this.getSession().get(DN_RESOLUTION_DATA));
                rptFileName = "Dn Resolution Report To Buyer - " + buyer.getBuyerCode() + "-" + ts + ".zip";
                contentType = "application/zip";
            }
            
            rptResult = new ByteArrayInputStream(datas);
            
            
        }
        catch(Exception e)
        {
            this.handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        finally
        {
            this.getSession().remove(EXCEL_IS_GENERATING);
            this.getSession().remove(BUYER_DATA);
            this.getSession().remove(OUTSTANDING_DATA);
            this.getSession().remove(DN_OUTSTANDING_DATA);
            this.getSession().remove(DN_RESOLUTION_DATA);
            this.getSession().remove(MATCHING_RESOLUTION_DATA);
        }
        
        return SUCCESS;
    }
    
    
    public String checkExcelData()
    {
        try
        {
            boolean flag = this.hasErrors();
            if(!flag && !"resolution".equals(param.getType()) && !"outstanding".equals(param.getType()))
            {
                data = "selecttype";
                flag = true;
            }
            
            if (!flag && param.getBuyerOid() == null)
            {
                data = "selectbuyer";
                flag = true;
            }
            
            BuyerHolder buyer = buyerService.selectBuyerByKey(param.getBuyerOid());
            
            if (!flag && null == buyer)
            {
                data = "buyernotexist";
                flag = true;
            }
            
            
//            if (!flag && !"outstanding".equals(param.getType()))
//            {
//                if (!flag && (null == param.getFromDate() || null == param.getToDate()))
//                {
//                    data = "datanull";
//                    flag = true;
//                }
//                
//                if (!flag && param.getToDate().before(param.getFromDate()))
//                {
//                    data = "tobefore";
//                    flag = true;
//                }
//            }
            
            if (!flag && "outstanding".equals(param.getType()))
            {
                if (!flag && param.getMoreThanDays().trim().isEmpty())
                {
                    data = "morethandaysnull";
                    flag = true;
                }
                
                if (!flag && !param.getMoreThanDays().trim().isEmpty() && !param.getMoreThanDays().trim().matches("\\d{0,4}"))
                {
                    data = "morethandayswrong";
                    flag = true;
                }
            }
            
            if (!flag && "resolution".equalsIgnoreCase(param.getType()))
            {
                if (!flag && (null == param.getReportDateFrom() || null == param.getReportDateTo()))
                {
                    data = "datanull";
                    flag = true;
                }
                
                if (!flag && param.getReportDateTo().before(param.getReportDateFrom()))
                {
                    data = "tobefore";
                    flag = true;
                }
                
                if (!flag)
                {
                    ControlParameterHolder maxDayOfReport = controlParameterService
                        .selectCacheControlParameterBySectIdAndParamId(
                            "CTRL", "MAX_DAY_OF_REPORT");
                    
                    int days = DateUtil.getInstance().daysAfterDate(DateUtil.getInstance().getFirstTimeOfDay(param.getReportDateFrom()), 
                        DateUtil.getInstance().getLastTimeOfDay(param.getReportDateTo()));
                    
                    if ((days + 1) > maxDayOfReport.getNumValue())
                    {
                        data = "maxDay," + maxDayOfReport.getNumValue();
                        flag = true;
                    }
                }
            }
            
            
            if (!flag && null != this.getSession().get(EXCEL_IS_GENERATING))
            {
                data = "generating";
                flag = true;
            }
            if (!flag)
            {
                param.trimAllString();
                param.setAllEmptyStringToNull();
                
                List<BusinessRuleHolder> businessRuleList = businessRuleService
                            .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                                    buyer.getBuyerOid(), "Matching", "PoInvGrnDn");
                
                boolean enableSupplierToDispute = retriveRule("EnableSupplierToDispute", businessRuleList) != null;
                
                boolean isAutoGenDnFromGI = businessRuleService.isAutoGenDnFromGI(buyer.getBuyerOid());
                
                if ("matching".equalsIgnoreCase(param.getMsgType()) && enableSupplierToDispute)
                {
                    this.getSession().put(BUYER_DATA, buyer);
                    
                    if("outstanding".equals(param.getType()))
                    {
                        List<PoInvGrnDnMatchingHolder> matchings = poInvGrnDnMatchingService.selectOutstandingRecords(
                            buyer.getBuyerOid(), null, param.getMoreThanDays(), param.getSupplierCode(), param.getSupplierName());
                        List<MatchingReportParameter> parameters = convertMatchingsToReportParameters(matchings, businessRuleList);
                        
                        if (parameters == null || parameters.isEmpty())
                        {
                            data = "empty";
                        }
                        else
                        {
                            Collections.sort(parameters, new Comparator<MatchingReportParameter>() 
                            {
                                @Override
                                public int compare(MatchingReportParameter o1,
                                    MatchingReportParameter o2)
                                {
                                    return o1.getDaysSpaned().compareTo(
                                        o2.getDaysSpaned());
                                }
                            });
                            this.getSession().put(OUTSTANDING_DATA, parameters);
                        }
                    }
                    else if("resolution".equals(param.getType()))
                    {
                        Calendar c1 = Calendar.getInstance();  
                        c1.setTime(param.getReportDateFrom());
                        Calendar c2 = Calendar.getInstance();  
                        c2.setTime(param.getReportDateTo());
                        
                        List<PoInvGrnDnMatchingHolder> allList = new ArrayList<PoInvGrnDnMatchingHolder>();
                        
                        List<PoInvGrnDnMatchingHolder> acceptedList = new ArrayList<PoInvGrnDnMatchingHolder>();
                        
                        List<PoInvGrnDnMatchingHolder> rejectedList = new ArrayList<PoInvGrnDnMatchingHolder>();
                        
                        List<PoInvGrnDnMatchingHolder> revisedList = new ArrayList<PoInvGrnDnMatchingHolder>();
                        
                        Map<String, byte[]> map = new HashMap<String, byte[]>();
                        
                        while (!c1.after(c2))
                        {
                            allList = poInvGrnDnMatchingService.selectBuyerResolutionRecords(buyer.getBuyerOid(), c1.getTime(), param.getSupplierCode(), param.getSupplierName());
                            if (allList == null || allList.isEmpty())
                            {
                                c1.add(Calendar.DATE, 1);
                                continue;
                            }
                            else
                            {
                                for (PoInvGrnDnMatchingHolder matching : allList)
                                {
                                    if (matching.getSupplierStatus().equals(PoInvGrnDnMatchingSupplierStatus.ACCEPTED))
                                    {
                                        acceptedList.add(matching);
                                    }
                                    else if (matching.getSupplierStatus().equals(PoInvGrnDnMatchingSupplierStatus.REJECTED))
                                    {
                                        rejectedList.add(matching);
                                    }
                                    else
                                    {
                                        revisedList.add(matching);
                                    }
                                }
                                
                                List<MatchingReportParameter> acceptedReports = convertMatchingsToReportParameters(acceptedList, businessRuleList);
                                List<MatchingReportParameter> rejectedReports = convertMatchingsToReportParameters(rejectedList, businessRuleList);
                                List<MatchingReportParameter> revisedReports = convertMatchingsToReportParameters(revisedList, businessRuleList);
                                
                                byte[] datas = buyerResolutionReport.exportExcel(acceptedReports, rejectedReports, revisedReports, buyer);
                                
                                map.put("Resolution Report To Buyer - " + buyer.getBuyerCode() + "-" + DateUtil.getInstance().convertDateToString(c1.getTime()) + ".xls", datas);
                            }
                            
                            c1.add(Calendar.DATE, 1);
                        }
                        
                        if (map.isEmpty())
                        {
                            data = "empty";
                        }
                        else
                        {
                            this.getSession().put(MATCHING_RESOLUTION_DATA, map);
                        }
                    }
                }
                else if ("dn".equalsIgnoreCase(param.getMsgType()) && isAutoGenDnFromGI)
                {
                    this.getSession().put(BUYER_DATA, buyer);
                    
                    if("outstanding".equals(param.getType()))
                    {
                        List<DnHeaderHolder> headers = dnHeaderService.selectOutstandingRecordsByBuyer(buyer.getBuyerOid(),
                            param.getSupplierCode(), param.getSupplierName(), param.getMoreThanDays(), null);
                        
                        if (headers == null || headers.isEmpty())
                        {
                            data = "empty";
                            return SUCCESS;
                        }
                        log.info("find " + headers.size() + " outstanding dns for buyer [" + buyer.getBuyerCode() + "]");
                        
                        List<DnHolder> dns = new ArrayList<DnHolder>();
                        
                        for (DnHeaderHolder header : headers)
                        {
                            DnHolder dn = dnService.selectDnByKey(header.getDnOid());
                            dns.add(dn);
                        }
                        
                        Collections.sort(dns, new Comparator<DnHolder>() {
                            @Override
                            public int compare(DnHolder o1, DnHolder o2)
                            {
                                return o1.getDnHeader().calculateDayElapsed().compareTo(
                                        o2.getDnHeader().calculateDayElapsed());
                            }
                        });
                        
                        this.getSession().put(DN_OUTSTANDING_DATA, dns);
                    }
                    else if("resolution".equals(param.getType()))
                    {
                        Calendar c1 = Calendar.getInstance();  
                        c1.setTime(param.getReportDateFrom());
                        Calendar c2 = Calendar.getInstance();  
                        c2.setTime(param.getReportDateTo());
                        
                        List<DnHeaderHolder> dnHeaderList = new ArrayList<DnHeaderHolder>();
                        
                        Map<String, byte[]> map = new HashMap<String, byte[]>();
                        while (!c1.after(c2))
                        {
                            dnHeaderList = dnHeaderService.selectResolutionRecordsByBuyer(
                                buyer.getBuyerOid(), c1.getTime(), param.getSupplierCode(), param.getSupplierName());
                            
                            if(dnHeaderList == null || dnHeaderList.isEmpty())
                            {
                                c1.add(Calendar.DATE, 1);
                                continue;
                            }
                            else
                            {
                                List<DnHolder> dnList = new ArrayList<DnHolder>();
                                for (DnHeaderHolder header : dnHeaderList)
                                {
                                    DnHolder dn = dnService.selectDnByKey(header.getDnOid());
                                    dn.getDnHeader().setClosed(header.getClosed());
                                    dnList.add(dn);
                                }
                                
                                byte[] datas = dnBuyerResolutionReport.exportExcel(dnList, buyer);
                                
                                map.put("Dn Resolution Report To Buyer - " + buyer.getBuyerCode() + "-" + DateUtil.getInstance().convertDateToString(c1.getTime()) + ".xls", datas);
                            }
                            
                            c1.add(Calendar.DATE, 1);
                        }
                        if (map.isEmpty())
                        {
                            data = "empty";
                        }
                        else
                        {
                            this.getSession().put(DN_RESOLUTION_DATA, map);
                        }
                    }
                }
                else
                {
                    data = "empty";
                }
            }
            
        }
        catch(Exception e)
        {
            this.handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    // *****************************************************
    // private method
    // *****************************************************
    @SuppressWarnings("unchecked")
    private void initSearchCriteria() throws Exception
    {
        reportType = new HashMap<String, String>();
        msgType = new HashMap<String, String>();
        
        List<String> urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
        
        boolean dnResolution = urls.contains("/trackingReport/dnResolution.action");
        boolean dnOutstanding = urls.contains("/trackingReport/dnOutstanding.action");
        boolean matchingResolution = urls.contains("/trackingReport/matchingResolution.action");
        boolean matchingOutstanding = urls.contains("/trackingReport/matchingOutstanding.action");
        
        
        if (dnResolution || matchingResolution)
        {
            reportType.put("resolution", "Resolution Report");
        }
        if (dnOutstanding || matchingOutstanding)
        {
            reportType.put("outstanding", "Outstanding Resolution Report");
        }
        if (matchingResolution || matchingOutstanding)
        {
            msgType.put("matching", "Po Inv Grn Dn Matching");
            if (!matchingResolution)
            {
                reportType.remove("resolution");
            }
            if (!matchingOutstanding)
            {
                reportType.remove("outstanding");
            }
        }
        if (dnResolution || dnOutstanding)
        {
            msgType.put("dn", "Debit Note");
        }
        
        buyers = buyerService.selectAvailableBuyersByUserOid(getProfileOfCurrentUser());
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
    
    
    private List<MatchingReportParameter> convertMatchingsToReportParameters(List<PoInvGrnDnMatchingHolder> matchingList, 
        List<BusinessRuleHolder> businessRuleList) throws Exception
{
    if (matchingList == null || matchingList.isEmpty())
    {
        return null;
    }
    
    List<MatchingReportParameter> parameters = new ArrayList<MatchingReportParameter>();
    
    for (PoInvGrnDnMatchingHolder matching : matchingList)
    {
        PoHolder po = poService.selectPoByKey(matching.getPoOid());
        
        InvHolder inv = null;
        if (matching.getInvOid() != null)
        {
            inv = invoiceService.selectInvoiceByKey(matching.getInvOid());
        }
        
        List<GrnHolder> grnList = new ArrayList<GrnHolder>();
        
        List<PoInvGrnDnMatchingGrnHolder> matchingGrnList = poInvGrnDnMatchingGrnService.selectByMatchOid(matching.getMatchingOid());
        for (PoInvGrnDnMatchingGrnHolder grn : matchingGrnList)
        {
            grnList.add(grnService.selectByKey(grn.getGrnOid()));
        }
        
        matching.setGrnList(matchingGrnList);
        
        List<PoInvGrnDnMatchingDetailExHolder> matchingDetailList = poInvGrnDnMatchingDetailService.selectByMatchingOid(matching.getMatchingOid());
        matching.setDetailList(matchingDetailList);
        
        SupplierHolder supplier = supplierService.selectSupplierByKey(matching.getSupplierOid());
        
        parameters.add(new MatchingReportParameter(matching, po, inv, grnList, null, businessRuleList, supplier.getSupplierSource().name(), false));
        
    }
    
    return parameters;
    
}
    
    // *****************************************************
    // handelException
    // *****************************************************
    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                new String[] { tickNo }));

        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

        msg.addMessageTarget(mt);
    }
    
    
    // *****************************************************
    // getter and setter
    // *****************************************************
    
    public Map<String, String> getReportType()
    {
        return reportType;
    }

    public MatchingReportHolder getParam()
    {
        return param;
    }

    public void setParam(MatchingReportHolder param)
    {
        this.param = param;
    }

    public void setReportType(Map<String, String> reportType)
    {
        this.reportType = reportType;
    }

    public InputStream getRptResult()
    {
        return rptResult;
    }

    public void setRptResult(InputStream rptResult)
    {
        this.rptResult = rptResult;
    }

    public String getRptFileName()
    {
        return rptFileName;
    }

    public void setRptFileName(String rptFileName)
    {
        this.rptFileName = rptFileName;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public List<? extends Object> getBuyers()
    {
        return buyers;
    }

    public void setBuyers(List<? extends Object> buyers)
    {
        this.buyers = buyers;
    }


    public Map<String, String> getMsgType()
    {
        return msgType;
    }


    public void setMsgType(Map<String, String> msgType)
    {
        this.msgType = msgType;
    }


    public String getContentType()
    {
        return contentType;
    }


    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }
    
}
