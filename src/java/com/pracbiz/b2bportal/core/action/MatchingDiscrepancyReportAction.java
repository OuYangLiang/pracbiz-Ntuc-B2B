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
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MatchingReportHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.report.excel.MatchingReportParameter;
import com.pracbiz.b2bportal.core.report.excel.SummaryMatchingReport;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerService;
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
public class MatchingDiscrepancyReportAction extends ProjectBaseAction implements
    CoreCommonConstants
{
    private static final long serialVersionUID = -8425541921402382568L;
    private static final Logger log = LoggerFactory.getLogger(MatchingDiscrepancyReportAction.class);
    
    
    private static final String EXCEL_IS_GENERATING = "isGenerating";
    private static final String DISCREPANCY_DATA = "discrepancyData";
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
    @Autowired transient private SummaryMatchingReport summaryMatchingReport;
    
    private transient InputStream rptResult;
    private String rptFileName;
    private String errorMsg;
    private String data;
    
    private MatchingReportHolder param;
    private Map<String,String> reportType;
    protected List<? extends Object> buyers;
    protected List<? extends Object> suppliers;
    private Map<String, String> matchingDiscrepancy;
    
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
            
            //Matching Discrepancy Report
            if ((List<MatchingReportParameter>)this.getSession().get(DISCREPANCY_DATA) != null)
            {
                datas = summaryMatchingReport.exportExcel(
                    (List<MatchingReportParameter>)this.getSession().get(DISCREPANCY_DATA), buyer, "Matching Discrepancy Report");
                rptFileName = "Matching Discrepancy Report - " + buyer.getBuyerCode() + " - " + ts;
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
            this.getSession().remove(DISCREPANCY_DATA);
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
                
                if (enableSupplierToDispute)
                {
                    BigDecimal supplierOid = param.getSupplierOid();
                    this.getSession().put(BUYER_DATA, buyer);
                    
                    List<PoInvGrnDnMatchingHolder> matchingList = poInvGrnDnMatchingService.selectDiscrepancyRecordByBuyerAndSupplier(buyer.getBuyerOid(), supplierOid, param.getDiscrepancyType(), this.getUserTypeOfCurrentUser());
                    
                    List<PoInvGrnDnMatchingHolder> priceDiscrepancyList = new ArrayList<PoInvGrnDnMatchingHolder>();
                    List<PoInvGrnDnMatchingHolder> qtyDiscrepancyList = new ArrayList<PoInvGrnDnMatchingHolder>();
                    for (PoInvGrnDnMatchingHolder matching : matchingList)
                    {
                        if (matching.getMatchingStatus().equals(PoInvGrnDnMatchingStatus.PRICE_UNMATCHED))
                        {
                            priceDiscrepancyList.add(matching);
                        }
                        else if (matching.getMatchingStatus().equals(PoInvGrnDnMatchingStatus.QTY_UNMATCHED))
                        {
                            qtyDiscrepancyList.add(matching);
                        }
                        else if (matching.getMatchingStatus().equals(PoInvGrnDnMatchingStatus.UNMATCHED))
                        {
                            if (matching.getPriceStatus().equals(PoInvGrnDnMatchingPriceStatus.PENDING))
                            {
                                priceDiscrepancyList.add(matching);
                            }
                            if (matching.getQtyStatus().equals(PoInvGrnDnMatchingQtyStatus.PENDING))
                            {
                                qtyDiscrepancyList.add(matching);
                            }
                        }
                    }
                    
                    List<MatchingReportParameter> parameters = new ArrayList<MatchingReportParameter>();
                    
                    if ("price".equalsIgnoreCase(param.getDiscrepancyType()))
                    {
                        parameters = convertMatchingsToReportParameters(priceDiscrepancyList, businessRuleList);
                    }
                    else if ("qty".equalsIgnoreCase(param.getDiscrepancyType()))
                    {
                        parameters = convertMatchingsToReportParameters(qtyDiscrepancyList, businessRuleList);
                    }
                    else
                    {
                        if (BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())
                            || BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
                        {
                            parameters = convertMatchingsToReportParameters(qtyDiscrepancyList, businessRuleList);
                        }
                        else
                        {
                            parameters = convertMatchingsToReportParameters(matchingList, businessRuleList);
                        }
                    }
                    
                    if (parameters == null || parameters.isEmpty())
                    {
                        data = "empty";
                    }
                    else
                    {
                        this.getSession().put(DISCREPANCY_DATA, parameters);
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
    private void initSearchCriteria() throws Exception
    {
        matchingDiscrepancy = new HashMap<String, String>();
        
        if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
            || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()))
        {
            matchingDiscrepancy.put("price", "PRICE DISCREPANCY");
            matchingDiscrepancy.put("qty", "QTY DISCREPANCY");
        }
        
        if (BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())
            || BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
        {
            matchingDiscrepancy.put("qty", "QTY DISCREPANCY");
        }
        
        List<SupplierHolder> availableSuppliers = supplierService.selectAvailableSuppliersByUserOid(getProfileOfCurrentUser());
        
        if (!availableSuppliers.isEmpty())
        {
            suppliers = availableSuppliers;
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

    public List<? extends Object> getSuppliers()
    {
        return suppliers;
    }

    public void setSuppliers(List<? extends Object> suppliers)
    {
        this.suppliers = suppliers;
    }


    public Map<String, String> getMatchingDiscrepancy()
    {
        return matchingDiscrepancy;
    }


    public void setMatchingDiscrepancy(Map<String, String> matchingDiscrepancy)
    {
        this.matchingDiscrepancy = matchingDiscrepancy;
    }
    
    
}
