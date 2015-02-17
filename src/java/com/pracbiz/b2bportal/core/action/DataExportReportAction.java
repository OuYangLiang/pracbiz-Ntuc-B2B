package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MatchingReportHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.report.excel.DnExportingReport;
import com.pracbiz.b2bportal.core.report.excel.InvoiceExportingReport;
import com.pracbiz.b2bportal.core.report.excel.MatchingReportParameter;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
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
public class DataExportReportAction extends ProjectBaseAction implements
    CoreCommonConstants
{
    private static final long serialVersionUID = -2418279319336133499L;
    private static final Logger log = LoggerFactory.getLogger(DataExportReportAction.class);
    
    
    private static final String EXCEL_IS_GENERATING = "isGenerating";
    private static final String INVOICE_DATA = "invoiceData";
    private static final String DN_DATA = "dnData";
    private static final String BUYER_DATA = "buyerData";
    
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private SupplierService supplierService;
    @Autowired transient private PoService poService;
    @Autowired transient private InvoiceService invoiceService;
    @Autowired transient private GrnService grnService;
    @Autowired transient private PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired transient private PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService;
    @Autowired transient private PoInvGrnDnMatchingDetailService poInvGrnDnMatchingDetailService;
    @Autowired transient private BusinessRuleService businessRuleService;
    @Autowired transient private InvoiceExportingReport invoiceExportingReport;
    @Autowired transient private DnHeaderService dnHeaderService;
    @Autowired transient private DnExportingReport dnExportingReport;
    @Autowired transient private ControlParameterService controlParameterService;
    
    private transient InputStream rptResult;
    private String rptFileName;
    private String errorMsg;
    private String data;
    
    private MatchingReportHolder param;
    private Map<String,String> msgType;
    protected List<? extends Object> buyers;
    protected List<? extends Object> suppliers;
    
    
    public DataExportReportAction()
    {
        this.initMsg();
    }
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
                param.setFromDate(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                param.setToDate(DateUtil.getInstance().getLastTimeOfDay(new Date()));
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
            
            //Invoice Exporting Report
            if ((List<MatchingReportParameter>)this.getSession().get(INVOICE_DATA) != null)
            {
                datas = invoiceExportingReport.exportExcel(
                    (List<MatchingReportParameter>)this.getSession().get(INVOICE_DATA), buyer);
                rptFileName = "Invoice Export Report - " + buyer.getBuyerCode() + "-" + ts;
            }
            //DN Exporting Report
            if ((List<DnHeaderHolder>)this.getSession().get(DN_DATA) != null)
            {
                datas = dnExportingReport.exportExcel(
                    (List<DnHeaderHolder>)this.getSession().get(DN_DATA), buyer);
                rptFileName = "DN Export Report - " + buyer.getBuyerCode() + "-" + ts;
            }
            
            if (datas != null && datas.length > 0)
            {
                rptResult = new ByteArrayInputStream(datas);
            }
            
            
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
            this.getSession().remove(INVOICE_DATA);
            this.getSession().remove(DN_DATA);
        }
        
        return SUCCESS;
    }
    
    
    public String checkExcelData()
    {
        try
        {
            boolean flag = this.hasErrors();
            
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
            
            if (!flag && null != param.getSupplierOid())
            {
                SupplierHolder supplier = supplierService.selectSupplierByKey(param.getSupplierOid());
                if (null == supplier)
                {
                    data = "suppliernotexist";
                    flag = true;
                }
            }
            
            if (!flag && (null == param.getFromDate() || null == param.getToDate()))
            {
                data = "datanull";
                flag = true;
            }
            
            if (!flag && param.getToDate().before(param.getFromDate()))
            {
                data = "tobefore";
                flag = true;
            }
            
            if (!flag)
            {
                ControlParameterHolder maxDayOfReport = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        "CTRL", "MAX_DAY_OF_REPORT");
                
                int days = DateUtil.getInstance().daysAfterDate(DateUtil.getInstance().getFirstTimeOfDay(param.getFromDate()), 
                    DateUtil.getInstance().getLastTimeOfDay(param.getToDate()));
                
                if ((days + 1) > maxDayOfReport.getNumValue())
                {
                    data = "maxDay," + maxDayOfReport.getNumValue();
                    flag = true;
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
                
                BigDecimal supplierOid = param.getSupplierOid();
                
                if ("inv".equalsIgnoreCase(param.getMsgType()))
                {
                    this.getSession().put(BUYER_DATA, buyer);
                    
                    List<PoInvGrnDnMatchingHolder> matchings = poInvGrnDnMatchingService.
                        selectApprovedMatchingRecordByInvStatusActionDateRange(buyer.getBuyerOid(), 
                            DateUtil.getInstance().getFirstTimeOfDay(param.getFromDate()), DateUtil.getInstance().getLastTimeOfDay(param.getToDate()),
                            supplierOid);
                    List<MatchingReportParameter> parameters = convertMatchingsToReportParameters(matchings, businessRuleList);
                    if (parameters == null || parameters.isEmpty())
                    {
                        data = "empty";
                    }
                    else
                    {
                        this.getSession().put(INVOICE_DATA, parameters);
                    }
                    
                }
                else if ("dn".equalsIgnoreCase(param.getMsgType()))
                {
                    this.getSession().put(BUYER_DATA, buyer);
                    
                    List<DnHeaderHolder> parameters = dnHeaderService.selectExportedDnByBuyerAndSupplierAndTimeRange(buyer.getBuyerOid(), supplierOid, 
                        DateUtil.getInstance().getFirstTimeOfDay(param.getFromDate()), DateUtil.getInstance().getLastTimeOfDay(param.getToDate()));
                    
                    if (parameters == null || parameters.isEmpty())
                    {
                        data = "empty";
                    }
                    else
                    {
                        this.getSession().put(DN_DATA, parameters);
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
        List<String> urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
        msgType = new HashMap<String, String>();
        if (urls.contains("/dataExportReport/invExport.action"))
        {
            msgType.put("inv", "E-Invoice");
        }
        if (urls.contains("/dataExportReport/dnExport.action"))
        {
            msgType.put("dn", "Debit Note");
        }
        
        List<SupplierHolder> availableSuppliers = supplierService.selectAvailableSuppliersByUserOid(getProfileOfCurrentUser());
        
        if (!availableSuppliers.isEmpty())
        {
            suppliers = availableSuppliers;
        }
        
        buyers = buyerService.selectAvailableBuyersByUserOid(getProfileOfCurrentUser());
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
    
    public Map<String, String> getMsgType()
    {
        return msgType;
    }

    public MatchingReportHolder getParam()
    {
        return param;
    }

    public void setParam(MatchingReportHolder param)
    {
        this.param = param;
    }

    public void setMsgType(Map<String, String> msgType)
    {
        this.msgType = msgType;
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

    public List<? extends Object> getSuppliers()
    {
        return suppliers;
    }

    public void setSuppliers(List<? extends Object> suppliers)
    {
        this.suppliers = suppliers;
    }
    
}
