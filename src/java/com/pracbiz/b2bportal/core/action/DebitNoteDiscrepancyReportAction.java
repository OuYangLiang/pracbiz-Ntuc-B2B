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
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.MatchingReportHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.report.excel.DnDiscrepancyReport;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */
public class DebitNoteDiscrepancyReportAction extends ProjectBaseAction implements
    CoreCommonConstants
{
    private static final long serialVersionUID = 633581913862973669L;

    private static final Logger log = LoggerFactory.getLogger(DebitNoteDiscrepancyReportAction.class);
    
    
    private static final String EXCEL_IS_GENERATING = "isGenerating";
    private static final String DISCREPANCY_DATA = "discrepancyData";
    private static final String BUYER_DATA = "buyerData";
    
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private SupplierService supplierService;
    @Autowired transient private DnHeaderService dnHeaderService;
    @Autowired transient private BusinessRuleService businessRuleService;
    @Autowired transient private DnDiscrepancyReport dnDiscrepancyReport;
    
    private transient InputStream rptResult;
    private String rptFileName;
    private String errorMsg;
    private String data;
    
    private MatchingReportHolder param;
    private Map<String,String> reportType;
    protected List<? extends Object> buyers;
    protected List<? extends Object> suppliers;
    private Map<String, String> dnDiscrepancy;
    
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
            if ((List<DnHeaderHolder>)this.getSession().get(DISCREPANCY_DATA) != null)
            {
                datas = dnDiscrepancyReport.exportExcel(
                    (List<DnHeaderHolder>)this.getSession().get(DISCREPANCY_DATA), buyer);
                rptFileName = "DN Discrepancy Report - " + buyer.getBuyerCode() + "-" + ts;
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
                
                boolean isAutoGenDnFromGI = businessRuleService.isAutoGenDnFromGI(buyer.getBuyerOid());
                
                if (isAutoGenDnFromGI)
                {
                    BigDecimal supplierOid = param.getSupplierOid();
                    this.getSession().put(BUYER_DATA, buyer);
                    
                    List<DnHeaderHolder> dnHeaderList = dnHeaderService.selectDisputedAndAuditUnfinishedRecord(buyer.getBuyerOid(), supplierOid);
                    
                    List<DnHeaderHolder> datas = new ArrayList<DnHeaderHolder>();
                    
                    if (dnHeaderList != null && !dnHeaderList.isEmpty())
                    {
                        List<DnHeaderHolder> priceDiscrepancyList = new ArrayList<DnHeaderHolder>();
                        List<DnHeaderHolder> qtyDiscrepancyList = new ArrayList<DnHeaderHolder>();
                        
                        for (DnHeaderHolder header : dnHeaderList)
                        {
                            if (header.getPriceDisputed() != null && header.getPriceDisputed())
                            {
                                priceDiscrepancyList.add(header);
                            }
                            if (header.getQtyDisputed() != null && header.getQtyDisputed())
                            {
                                qtyDiscrepancyList.add(header);
                            }
                        }
                        
                        if ("price".equalsIgnoreCase(param.getDiscrepancyType()))
                        {
                            datas = priceDiscrepancyList;
                        }
                        else if ("qty".equalsIgnoreCase(param.getDiscrepancyType()))
                        {
                            datas = qtyDiscrepancyList;
                        }
                        else
                        {
                            if (BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())
                                || BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
                            {
                                datas = qtyDiscrepancyList;
                            }
                            else
                            {
                                for (DnHeaderHolder header : dnHeaderList)
                                {
                                    if (header.getPriceDisputed() != null && header.getQtyDisputed() != null)
                                    {
                                        datas.add(header);
                                    }
                                } 
                            }
                        }
                    }
                    
                    if(datas == null || datas.isEmpty())
                    {
                        data = "empty";
                    }
                    else
                    {
                        this.getSession().put(DISCREPANCY_DATA, datas);
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
        dnDiscrepancy = new HashMap<String, String>();
        
        if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
            || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()))
        {
            dnDiscrepancy.put("price", "PRICE DISCREPANCY");
            dnDiscrepancy.put("qty", "QTY DISCREPANCY");
        }
        
        if (BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())
            || BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
        {
            dnDiscrepancy.put("qty", "QTY DISCREPANCY");
        }
        
        List<SupplierHolder> availableSuppliers = supplierService.selectAvailableSuppliersByUserOid(getProfileOfCurrentUser());
        
        if (!availableSuppliers.isEmpty())
        {
            suppliers = availableSuppliers;
        }
        
        buyers = buyerService.selectAvailableBuyersByUserOid(getProfileOfCurrentUser());
        
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


    public Map<String, String> getDnDiscrepancy()
    {
        return dnDiscrepancy;
    }


    public void setDnDiscrepancy(Map<String, String> dnDiscrepancy)
    {
        this.dnDiscrepancy = dnDiscrepancy;
    }
    
}
