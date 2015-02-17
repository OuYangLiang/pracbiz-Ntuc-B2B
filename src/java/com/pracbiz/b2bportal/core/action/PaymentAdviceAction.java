//*****************************************************************************
//
// File Name       :  PaymentAdvice.java
// Date Created    :  2012-12-13
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2012-12-13 $
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
import com.pracbiz.b2bportal.core.constants.PnStatus;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PnHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PnSummaryHolder;
import com.pracbiz.b2bportal.core.report.DefaultPnReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PnDetailService;
import com.pracbiz.b2bportal.core.service.PnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.PnHeaderService;
import com.pracbiz.b2bportal.core.service.PnService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class PaymentAdviceAction extends TransactionalDocsBaseAction implements ApplicationContextAware, CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(PaymentAdviceAction.class);
    private static final long serialVersionUID = -5100551458138961326L;
    
    private static final String SESSION_KEY_SEARCH_PARAMETER_PN = "SEARCH_PARAMETER_PN";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.PaymentAdviceAction.selectedOids";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";

    @Autowired transient private PnHeaderService pnHeaderService;
    @Autowired transient private PnDetailService pnDetailService;
    @Autowired transient private PnHeaderExtendedService pnHeaderExtendedService;
    @Autowired transient private PnDetailExtendedService pnDetailExtendedService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private PnService pnService;
    
    transient private ApplicationContext ctx;
    transient private InputStream rptResult;
    private PnSummaryHolder param;
    private String rptFileName;
    private boolean selectAll;

    public PaymentAdviceAction()
    {
        this.initMsg();
        this.setPageId(PageId.P007.name());
    }
    
    
    
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
    
    // *****************************************************
    // summary page
    // *****************************************************
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_PN);
        
        param = (PnSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_PN);
        
        try
        {
            this.initSearchCriteria();
            this.initSearchCondition();
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    public String search()
    {
        if (null == param)
        {
            param = new PnSummaryHolder();
        }
        
        try
        {
            param.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getPnDateFrom()));
            param.setPnDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getPnDateTo()));
            param.setSentDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getSentDateFrom()));
            param.setSentDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getSentDateTo()));
            param.setReceivedDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getReceivedDateFrom()));
            param.setReceivedDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getReceivedDateTo()));
            param = initSortField(param);
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_PN, param);
        
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            PnSummaryHolder searchParam = this.initSearchParameter();
            this.obtainListRecordsOfPagination(pnHeaderService, searchParam, MODULE_KEY_PN);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    // *****************************************************
    // print pdf
    // *****************************************************
    public String printPdf()
    {
        try
        {
            String[] files = null;
            if (selectAll)
            {
                param = (PnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_PN);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_PN, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<PnSummaryHolder> pnList = pnHeaderService.selectAllRecordToExport(param);
                
                if (pnList != null && !pnList.isEmpty())
                {
                    files = new String[pnList.size()];
                    for (int i = 0; i < pnList.size(); i++)
                    {
                        PnSummaryHolder pn = pnList.get(i);
                        
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(pn.getDocOid());
                        
                        SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                        
                        rptFileName = msg.getReportFilename();
                        
                        
                        // File file = new File(appConfig.getSupplierMailboxRootPath()
                        //+ PS + supplier.getMboxId() 
                        //+ PS + "doc" + PS + "in" + PS
                        //+ DateUtil.getInstance().getYearAndMonth(
                        //    msg.getCreateDate())), msg.getReportFilename());
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
                                    MsgType.PN.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                            
                            DefaultPnReportEngine pnReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                                .getBuyerCode());
                            
                            ReportEngineParameter<PnHolder> reportParameter = retrieveParameter(msg, supplier);

                            byte[] datas = pnReportEngine.generateReport(reportParameter, DefaultReportEngine.PDF_TYPE_STANDARD);//0 means standard pdf 
                            
                            FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                            
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
                    BigDecimal pnOid = new BigDecimal(parts[i]);

                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(pnOid);
                    
                    SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                    
                    rptFileName = msg.getReportFilename();
                    
                    
                    // File file = new File(appConfig.getSupplierMailboxRootPath()
                    //+ PS + supplier.getMboxId() 
                    //+ PS + "doc" + PS + "in" + PS
                    //+ DateUtil.getInstance().getYearAndMonth(
                    //    msg.getCreateDate())), msg.getReportFilename());
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
                                MsgType.PN.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                        
                        DefaultPnReportEngine pnReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                            .getBuyerCode());
                        
                        ReportEngineParameter<PnHolder> reportParameter = retrieveParameter(msg, supplier);

                        byte[] datas = pnReportEngine.generateReport(reportParameter, DefaultReportEngine.PDF_TYPE_STANDARD);//0 means standard pdf 
                        
                        FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                        
                    }
                    
                    if(msg.getReadStatus() != null
                        && msg.getReadStatus().equals(ReadStatus.UNREAD) 
                        && this.getUserTypeOfCurrentUser() != null )
                    {
                        UserTypeHolder userType = userTypeService.selectByKey(this.getUserTypeOfCurrentUser());
                        
                        if(userType.getUserTypeId().equalsIgnoreCase(USER_TYPE_ID_SUPPLIER_ADMIN)
                            || userType.getUserTypeId().equalsIgnoreCase(USER_TYPE_ID_SUPPLIER_USER))
                        {
                            msg.setReadStatus(ReadStatus.READ);
                            msg.setReadDate(new Date());
                            msgTransactionsService.updateByPrimaryKeySelective(null, msg);
                        }
                        
                    }
                    
                    files[i] = file.getPath();
                }
            }
            
            rptResult = PdfReportUtil.getInstance().mergePDFs(files);
            
            if(files != null && files.length > 1)
            {
                rptFileName = "pnReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
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
            List<BigDecimal> invOids = new ArrayList<BigDecimal>();
            if (selectAll)
            {
                param = (PnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_PN);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_PN, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<PnSummaryHolder> pnList = pnHeaderService.selectAllRecordToExport(param);
                
                if (pnList != null && !pnList.isEmpty())
                {
                    for (PnSummaryHolder pn : pnList)
                    {
                        invOids.add(pn.getDocOid());
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(pn.getDocOid());
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
                    BigDecimal invOid = new BigDecimal(part);
                    invOids.add(new BigDecimal(part));
                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(invOid);
                    this.updateReadStatus(msg);
                }
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(pnService.exportExcel(invOids, storeFlag)));
            rptFileName = "PnReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    //************************************
    //Export Summary Excel
    //************************************
    public String exportSummaryExcel()
    {
        try
        {
            PnSummaryHolder searchParam = this.initSearchParameter();
            int count = pnHeaderService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            
            List<MsgTransactionsExHolder> summarys = pnHeaderService.getListOfSummary(searchParam);
            if (summarys==null || summarys.isEmpty())
            {
                return INPUT;
            }
            
            List<PnSummaryHolder> pnSummarys = new ArrayList<PnSummaryHolder>();
            
            for (MsgTransactionsExHolder msgTrans : summarys)
            {
                pnSummarys.add((PnSummaryHolder)msgTrans);
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(pnService.exportSummaryExcel(pnSummarys, storeFlag)));
            rptFileName = "PN_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    // *****************************************************
    // private method
    // *****************************************************
    private ReportEngineParameter<PnHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        PnHeaderHolder header = pnHeaderService.selectPnHeaderByKey(msg.getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
        List<PnDetailHolder> details = pnDetailService
            .selectPnDetailByKey(msg.getDocOid());
        List<PnHeaderExtendedHolder> headerExtendeds = pnHeaderExtendedService
            .selectHeaderExtendedByKey(msg.getDocOid());
        
        List<PnDetailExtendedHolder> detailExtendeds = pnDetailExtendedService
            .selectDetailExtendedByKey(msg.getDocOid());
        
        ReportEngineParameter<PnHolder> reportEngineParameter = new ReportEngineParameter<PnHolder>();

        PnHolder data = new PnHolder();

        data.setPnHeader(header);
        data.setDetails(details);
        data.setHeaderExtendeds(headerExtendeds);
        data.setDetailExtendeds(detailExtendeds);

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    private DefaultPnReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.PN.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultPnReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.PN
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultPnReportEngine.class);
    }

    private PnSummaryHolder initSortField(PnSummaryHolder param)
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
    
    private void initSearchCondition() throws Exception
    {
        readStatus = ReadStatus.toMapValue(this);
        docStatuses = PnStatus.toMapValue(this);
        if (param == null)
        {
            param = new PnSummaryHolder();
        }
        if(getProfileOfCurrentUser().getBuyerOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
            {
                param.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setPnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
        if (getProfileOfCurrentUser().getSupplierOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
            {
                param.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setPnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
    }
    
    private PnSummaryHolder initSearchParameter() throws Exception
    {
        PnSummaryHolder searchParam = (PnSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_PN);
    
        if (searchParam == null)
        {
            searchParam = new PnSummaryHolder();
            
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setPnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setPnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setPnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_PN, searchParam);
        }
    
        searchParam = initSortField(searchParam);
        initCurrentUserSearchParam(searchParam);
        searchParam.trimAllString();
        searchParam.setAllEmptyStringToNull();
        
        return searchParam;
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
    
    // *****************************************************
    // setter and getter
    // *****************************************************

    public PnSummaryHolder getParam()
    {
        return param;
    }

    
    public void setParam(PnSummaryHolder param)
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


    public boolean isSelectAll()
    {
        return selectAll;
    }


    public void setSelectAll(boolean selectAll)
    {
        this.selectAll = selectAll;
    }

}
