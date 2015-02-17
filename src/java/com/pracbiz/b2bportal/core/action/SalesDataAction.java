//*****************************************************************************
//
// File Name       :  InvoiceAction.java
// Date Created    :  2012-12-13
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-13 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.constants.SalesDataType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SalesHolder;
import com.pracbiz.b2bportal.core.holder.SalesLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.SalesSummaryHolder;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultSalesReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.SalesDateLocationDetailService;
import com.pracbiz.b2bportal.core.service.SalesDateService;
import com.pracbiz.b2bportal.core.service.SalesDetailService;
import com.pracbiz.b2bportal.core.service.SalesHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.SalesHeaderService;
import com.pracbiz.b2bportal.core.service.SalesLocationService;
import com.pracbiz.b2bportal.core.service.SalesService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author YinChi
 */
public class SalesDataAction extends TransactionalDocsBaseAction implements
    ApplicationContextAware, CoreCommonConstants
{
    private static final long serialVersionUID = -7204832667780050099L;
    private static final Logger log = LoggerFactory.getLogger(SalesDataAction.class);
    
    public static final String SESSION_KEY_SEARCH_PARAMETER_SALES = "SEARCH_PARAMETER_SALES";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    private static final String SESSION_OID_PARAMETER = "session.parameter.sales.selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private SalesHeaderService salesHeaderService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private SalesDetailService salesDetailService;
    @Autowired transient private SalesHeaderExtendedService salesHeaderExtendedService;
    @Autowired transient private SalesDateService salesDateService;
    @Autowired transient private SalesLocationService salesLocationService;
    @Autowired transient private SalesDateLocationDetailService salesDateLocationDetailService;
    @Autowired transient private SalesService salesService;
    transient private ApplicationContext ctx;
    transient private InputStream rptResult;
    
    private SalesSummaryHolder param;
    private String rptFileName;
    private boolean selectAll;

    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
        throws BeansException
    {
        this.ctx = ctx;
    }
    
    public String putParamIntoSession()
    {
        this.getSession().put(SESSION_OID_PARAMETER,
            this.getRequest().getParameter(REQUEST_PARAMTER_OID));

        return SUCCESS;
    }

    public SalesDataAction()
    {
        this.initMsg();
        this.setPageId(PageId.P012.name());
    }

    
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_SALES);

        param = (SalesSummaryHolder)getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_SALES);

        try
        {
            this.initSearchCriteria();
            this.initSearchCondition();
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    public String search()
    {
        if(null == param)
        {
            param = new SalesSummaryHolder();
        }

        try
        {
            param.setSalesDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getSalesDateFrom()));
            param.setSalesDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getSalesDateTo()));
            param.setPeriodStartDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getPeriodStartDateFrom()));
            param.setPeriodStartDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getPeriodStartDateTo()));
            param.setPeriodEndDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getPeriodEndDateFrom()));
            param.setPeriodEndDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getPeriodEndDateTo()));
            param.setSentDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getSentDateFrom()));
            param.setSentDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getSentDateTo()));
            param.setReceivedDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getReceivedDateFrom()));
            param.setReceivedDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getReceivedDateTo()));
            
            
            param = initSortField(param);
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_SALES, param);

        return SUCCESS;
    }
    
    public String data()
    {
        try
        {
            param = (SalesSummaryHolder)getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_SALES);

            if(param == null)
            {
                this.initSearchCondition();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_SALES, param);
            }
            param = initSortField(param);

            initCurrentUserSearchParam(param);
            
            param.trimAllString();
            param.setAllEmptyStringToNull();

            this.obtainListRecordsOfPagination(salesHeaderService, param, MODULE_KEY_SALES);
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    public String printPdf()
    {
        try
        {
            String[] files = null;
            if (selectAll)
            {
                param = (SalesSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_SALES);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_SALES, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<SalesSummaryHolder> salesList = salesHeaderService.selectAllRecordToExport(param);
                
                if (salesList != null && !salesList.isEmpty())
                {
                    files = new String[salesList.size()];
                    for (int i = 0; i < salesList.size(); i++)
                    {
                        SalesSummaryHolder sales = salesList.get(i);
                        
                        MsgTransactionsHolder msg = msgTransactionsService
                            .selectByKey(sales.getDocOid());

                        SupplierHolder supplier = supplierService
                            .selectSupplierByKey(msg.getSupplierOid());
                        
                        rptFileName = msg.getReportFilename();

                        File file = new File(mboxUtil.getFolderInSupplierDocInPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())), msg.getReportFilename());
                        

                        if(!file.exists())
                        {
                            File docPath = new File(mboxUtil.getFolderInSupplierDocInPath(
                                supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                    msg.getCreateDate())));
                            
                            if (!docPath.isDirectory())
                            {
                                FileUtil.getInstance().createDir(docPath);
                            }
                            
                            BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                MsgType.DSD.name(), CoreCommonConstants.DEFAULT_SUBTYPE);

                            DefaultSalesReportEngine salesReportEngine = retrieveEngine(
                                buyerMsgSettingReport, msg.getBuyerCode());

                            ReportEngineParameter<SalesHolder> reportParameter = retrieveParameter(
                                msg, supplier);

                            byte[] datas = salesReportEngine
                                .generateReport(reportParameter, DefaultReportEngine.PDF_TYPE_STANDARD);//  //0 means standard pdf

                            FileUtil.getInstance().writeByteToDisk(datas,
                                file.getPath());

                        }
                        
                        this.updateReadStatus(msg);

                        files[i] = file.getPath();
                    }
                }
            }
            else
            {
                Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);

                if(null == selectedOids)
                {
                    throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                }

                this.getSession().remove(SESSION_OID_PARAMETER);

                String[] parts = selectedOids.toString().split(
                    REQUEST_OID_DELIMITER);

                files = new String[parts.length];

                for(int i = 0; i < files.length; i++)
                {
                    BigDecimal docOid = new BigDecimal(parts[i]);

                    MsgTransactionsHolder msg = msgTransactionsService
                        .selectByKey(docOid);

                    SupplierHolder supplier = supplierService
                        .selectSupplierByKey(msg.getSupplierOid());
                    
                    rptFileName = msg.getReportFilename();

                    File file = new File(mboxUtil.getFolderInSupplierDocInPath(
                        supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                            msg.getCreateDate())), msg.getReportFilename());
                    

                    if(!file.exists())
                    {
                        File docPath = new File(mboxUtil.getFolderInSupplierDocInPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())));
                        
                        if (!docPath.isDirectory())
                        {
                            FileUtil.getInstance().createDir(docPath);
                        }
                        
                        BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                            MsgType.DSD.name(), CoreCommonConstants.DEFAULT_SUBTYPE);

                        DefaultSalesReportEngine salesReportEngine = retrieveEngine(
                            buyerMsgSettingReport, msg.getBuyerCode());

                        ReportEngineParameter<SalesHolder> reportParameter = retrieveParameter(
                            msg, supplier);

                        byte[] datas = salesReportEngine
                            .generateReport(reportParameter, DefaultReportEngine.PDF_TYPE_STANDARD);//  //0 means standard pdf

                        FileUtil.getInstance().writeByteToDisk(datas,
                            file.getPath());

                    }
                    
                    if(msg.getReadStatus() != null
                        && msg.getReadStatus().equals(ReadStatus.UNREAD)
                        && this.getUserTypeOfCurrentUser() != null)
                    {
                        UserTypeHolder userType = userTypeService.selectByKey(this
                            .getUserTypeOfCurrentUser());

                        if(userType.getUserTypeId().equalsIgnoreCase(
                                USER_TYPE_ID_SUPPLIER_ADMIN)
                            || userType.getUserTypeId().equalsIgnoreCase(
                                USER_TYPE_ID_SUPPLIER_USER))
                        {
                            msg.setReadStatus(ReadStatus.READ);
                            msg.setReadDate(new Date());
                            msgTransactionsService.updateByPrimaryKeySelective(
                                null, msg);
                        }

                    }
                    files[i] = file.getPath();
                }
            }

            rptResult = PdfReportUtil.getInstance().mergePDFs(files);

            if(files != null && files.length > 1)
            {
                rptFileName = "SalesReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
            }

        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    // *****************************************************
    // export excel
    // *****************************************************
    public String exportExcel()
    {
        try
        {
            List<BigDecimal> salesOids = new ArrayList<BigDecimal>();
            if (selectAll)
            {
                param = (SalesSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_SALES);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_SALES, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<SalesSummaryHolder> salesList = salesHeaderService.selectAllRecordToExport(param);
                
                if (salesList != null && !salesList.isEmpty())
                {
                    for (SalesSummaryHolder sales : salesList)
                    {
                        salesOids.add(sales.getDocOid());
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(sales.getDocOid());
                        this.updateReadStatus(msg);
                    }
                }
            }
            else
            {
                Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
                if (null == selectedOids)
                {
                    throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                }
                
                this.getSession().remove(SESSION_OID_PARAMETER);
                
                String[] parts = selectedOids.toString().split(
                        REQUEST_OID_DELIMITER);
                
                for (String part : parts)
                {
                    BigDecimal salesOid = new BigDecimal(part);
                    salesOids.add(new BigDecimal(part));
                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(salesOid);
                    this.updateReadStatus(msg);
                }
            }
            
            this.setRptResult(new ByteArrayInputStream(salesService.exportExcel(salesOids)));
            rptFileName = "SalesReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    // *****************************************************
    // private method
    // *****************************************************

    protected SalesSummaryHolder initSortField(SalesSummaryHolder param)
    {
        param.setSortField(SORT_FIELD_SENT_DATE);
        param.setSortOrder(SORT_ORDER_DESC);
        
        if(null != getProfileOfCurrentUser().getSupplierOid()) 
        {
            param.setSortField(SORT_FIELD_CREATE_DATE);
            param.setSortOrder(SORT_ORDER_DESC);
        }
        
        return param;
    }
    
    private void updateReadStatus(MsgTransactionsHolder msg) throws Exception
    {
        if ((BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser())
            || BigDecimal.valueOf(5).equals(this.getUserTypeOfCurrentUser()))
            && !ReadStatus.READ.equals(msg.getReadStatus()))
        {
            msg.setReadStatus(ReadStatus.READ);
            msg.setReadDate(new Date());
            msgTransactionsService.updateByPrimaryKeySelective(null, msg);
        }
    }
    
    private void initSearchCondition() throws Exception
    {
        docTypes = SalesDataType.toMapValue(this);
        readStatus = ReadStatus.toMapValue(this);
        if (param == null)
        {
            param = new SalesSummaryHolder();
        }
        if(getProfileOfCurrentUser().getBuyerOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
            {
                param.setSalesDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setSalesDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setSalesDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
        if (getProfileOfCurrentUser().getSupplierOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
            {
                param.setSalesDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setSalesDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setSalesDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
    }
    
    private ReportEngineParameter<SalesHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        SalesHeaderHolder header = salesHeaderService.selectSalesHeaderByKey(msg
            .getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerByKey(msg.getBuyerOid());
        List<SalesDetailHolder> details = salesDetailService
            .selectSalesDetailByKey(msg.getDocOid());
        List<SalesHeaderExtendedHolder> headerExtendeds = salesHeaderExtendedService
            .selectSalesHeaderExtendedByKey(msg.getDocOid());

        List<SalesLocationHolder> locs = salesLocationService.selectSalesLocationByKey(msg.getDocOid());
        List<SalesDateHolder> salesDates = salesDateService.selectSalesDateByKey(msg.getDocOid());
        List<SalesDateLocationDetailHolder> salesDateLocs = salesDateLocationDetailService.selectSalesLocationDetailByKey(msg.getDocOid());

        ReportEngineParameter<SalesHolder> reportEngineParameter = new ReportEngineParameter<SalesHolder>();

        SalesHolder data = new SalesHolder();
        data.setSalesHeader(header);
        data.setHeaderExtendeds(headerExtendeds);
        data.setDetails(details);
        data.setSalesDates(salesDates);
        data.setLocations(locs);
        data.setSalesDateLocationDetail(salesDateLocs);

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    private DefaultSalesReportEngine retrieveEngine(
        BuyerMsgSettingReportHolder buyerMsgReportSetting, String buyerCode)
    {
        if(!buyerMsgReportSetting.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.DSD.name(), buyerMsgReportSetting.getSubType(),
                buyerMsgReportSetting.getReportTemplate()),
                DefaultSalesReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.DSD.name(), buyerMsgReportSetting.getSubType(),
            buyerMsgReportSetting.getReportTemplate()),
            DefaultSalesReportEngine.class);
    }
    
    // *****************************************************
    // private method
    // *****************************************************
    public SalesSummaryHolder getParam()
    {
        return param;
    }

    public void setParam(SalesSummaryHolder param)
    {
        this.param = param;
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

    public boolean isSelectAll()
    {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll)
    {
        this.selectAll = selectAll;
    }
    
}
