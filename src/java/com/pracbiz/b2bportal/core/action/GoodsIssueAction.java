//*****************************************************************************
//
// File Name       :  GoodsIssueAction.java
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
import com.pracbiz.b2bportal.core.constants.GiStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.GiSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.report.DefaultGiReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.GiDetailExtendedService;
import com.pracbiz.b2bportal.core.service.GiDetailService;
import com.pracbiz.b2bportal.core.service.GiHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.GiHeaderService;
import com.pracbiz.b2bportal.core.service.GiService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

public class GoodsIssueAction  extends TransactionalDocsBaseAction implements CoreCommonConstants, ApplicationContextAware
{
    private static final Logger log = LoggerFactory.getLogger(GoodsIssueAction.class);
    private static final long serialVersionUID = -6564850224388546964L;
    
    public static final String SESSION_KEY_SEARCH_PARAMETER_GI = "SEARCH_PARAMETER_GI";
    
    private static final String SESSION_OID_PARAMETER = "session.parameter.GoodsIssueAction.selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    
    transient private ApplicationContext ctx;
    transient private InputStream rptResult;
    private String rptFileName;
    
    @Autowired transient private GiHeaderService giHeaderService;
    @Autowired transient private GiDetailService giDetailService;
    @Autowired transient private GiHeaderExtendedService giHeaderExtendedService;
    @Autowired transient private GiDetailExtendedService giDetailExtendedService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private RtvHeaderService rtvHeaderService;
    @Autowired transient private GiService giService;
    
    private GiSummaryHolder param;
    private String errorMsg;
    private boolean selectAll;

    public GoodsIssueAction()
    {
        this.initMsg();
        this.setPageId(PageId.P009.name());
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
    
    public String checkOtherPdfData()
    {
        Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
        String docType = this.getRequest().getParameter("docType");
        
        try
        {
            if(null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }

            String[] parts = selectedOids.toString().split(
                REQUEST_OID_DELIMITER);

            if ("RTV".equalsIgnoreCase(docType))
            {
                BigDecimal docOid = new BigDecimal(parts[0]);

                GiHeaderHolder gi = giHeaderService.selectGiHeaderByKey(docOid);
                RtvHeaderHolder rtv = rtvHeaderService
                    .selectRtvHeaderByRtvNo(gi.getBuyerOid(),
                            gi.getRtvNo(), gi.getSupplierCode());
                
               if (rtv == null)
               {
                   errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"RTV",gi.getRtvNo()});;
                   return INPUT;
               }
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            errorMsg = this.getText("message.exception.general.title");
            return INPUT;
        }
        
        return SUCCESS;
    }

    
    
    // *****************************************************
    // summary page
    // *****************************************************
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_GI);
        
        param = (GiSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_GI);
        
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
    
    // *****************************************************
    // search
    // *****************************************************
    public String search()
    {
        if (null == param)
        {
            param = new GiSummaryHolder();
        }
        
        try
        {
            param.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getRtvDateFrom()));
            param.setRtvDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getRtvDateTo()));
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
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_GI, param);
        
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            GiSummaryHolder searchParam = this.initSearchParameter();
            this.obtainListRecordsOfPagination(giHeaderService, searchParam, MODULE_KEY_GI);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    // *****************************************************
    // print
    // *****************************************************
    public String printPdf()
    {
        try
        {
            String[] files = null;
            if (selectAll)
            {
                param = (GiSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_GI);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_GI, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<GiSummaryHolder> giList = giHeaderService.selectAllRecordToExport(param);
                
                if (giList != null && !giList.isEmpty())
                {
                    files = new String[giList.size()];
                    for (int i = 0; i < giList.size(); i++)
                    {
                        GiSummaryHolder gi = giList.get(i);
                        
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(gi.getDocOid());
                        
                        this.updateReadStatus(msg);
                        
                        SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                        
                        rptFileName = msg.getReportFilename();
                        if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 
                                || BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                        {
                            rptFileName = FileUtil.getInstance().trimAllExtension(rptFileName) + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".pdf";
                        }
                        
                        File file = new File(mboxUtil.getFolderInSupplierDocInPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())), rptFileName);
                        
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
                                    MsgType.GI.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                            
                            DefaultGiReportEngine giReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                                .getBuyerCode());
                            
                            ReportEngineParameter<GiHolder> reportParameter = retrieveParameter(msg, supplier);

                            int isForStore = DefaultReportEngine.PDF_TYPE_STANDARD;
                            if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 || 
                                    BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                            {
                                isForStore = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                            }
                            //0 means standard pdf, 1 means for buyer store pdf 
                            byte[] datas = giReportEngine.generateReport(reportParameter, isForStore);
                            
                            FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                            
                        } 
                        
                        files[i] = file.getPath();
                    }
                }
            }
            else
            {
                Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
                String docType = this.getRequest().getParameter("docType");
                
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

                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(docOid);
                    
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
                    
                    SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                    
                    rptFileName = msg.getReportFilename();
                    if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 
                            || BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                    {
                        rptFileName = FileUtil.getInstance().trimAllExtension(rptFileName) + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".pdf";
                    }
                    
                    File file = new File(mboxUtil.getFolderInSupplierDocInPath(
                        supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                            msg.getCreateDate())), rptFileName);
                    
                    if ("RTV".equalsIgnoreCase(docType))
                    {
                        GiHeaderHolder gi = giHeaderService.selectGiHeaderByKey(docOid);
                        RtvHeaderHolder rtv = rtvHeaderService
                            .selectRtvHeaderByRtvNo(gi.getBuyerOid(),
                                    gi.getRtvNo(), gi.getSupplierCode());
                        docOid = rtv.getRtvOid();
                        this.getSession().put("session.parameter.GoodsReturnNoteAction.selectedOids", docOid);
                        return "RTV";
                    }

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
                                MsgType.GI.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                        
                        DefaultGiReportEngine giReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                            .getBuyerCode());
                        
                        ReportEngineParameter<GiHolder> reportParameter = retrieveParameter(msg, supplier);

                        int isForStore = DefaultReportEngine.PDF_TYPE_STANDARD;
                        if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 || 
                                BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                        {
                            isForStore = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                        }
                        //0 means standard pdf, 1 means for buyer store pdf 
                        byte[] datas = giReportEngine.generateReport(reportParameter, isForStore);
                        
                        FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                        
                    } 
                    
                    files[i] = file.getPath();
                    
                }
            }
            
            rptResult = PdfReportUtil.getInstance().mergePDFs(files);
            
            if(files != null && files.length > 1)
            {
                rptFileName = "giReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
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
    // private method
    // *****************************************************
    private ReportEngineParameter<GiHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        GiHeaderHolder header = giHeaderService.selectGiHeaderByKey(msg.getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
        List<GiDetailHolder> details = giDetailService.selectGiDetailByKey(msg.getDocOid());
        List<GiHeaderExtendedHolder> headerExtendeds = giHeaderExtendedService.selectHeaderExtendedByKey(msg.getDocOid());
        List<GiDetailExtendedHolder> detailExtendeds = giDetailExtendedService.selectDetailExtendedByKey(msg.getDocOid());
       
        ReportEngineParameter<GiHolder> reportEngineParameter = new ReportEngineParameter<GiHolder>();

        GiHolder data = new GiHolder();

        data.setGiHeader(header);
        data.setDetails(details);
        data.setHeaderExtended(headerExtendeds);
        data.setDetailExtended(detailExtendeds);

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    private DefaultGiReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.GI.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultGiReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.GI
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultGiReportEngine.class);
    }
    
    private GiSummaryHolder initSortField(GiSummaryHolder param)
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
        docStatuses = GiStatus.toMapValue(this);
        if (param == null)
        {
            param = new GiSummaryHolder();
        }
        if(getProfileOfCurrentUser().getBuyerOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
            {
                param.setGiDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setGiDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setGiDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
        if (getProfileOfCurrentUser().getSupplierOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
            {
                param.setGiDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setGiDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setGiDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
    }

    
    // *****************************************************
    // export excel
    // *****************************************************
    public String exportExcel()
    {
        try
        {
            List<BigDecimal> giOids = new ArrayList<BigDecimal>();
            if (selectAll)
            {
                param = (GiSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_GI);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_GI, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<GiSummaryHolder> giList = giHeaderService.selectAllRecordToExport(param);
                
                if (giList != null && !giList.isEmpty())
                {
                    for (GiSummaryHolder gi : giList)
                    {
                        giOids.add(gi.getDocOid());
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(gi.getDocOid());
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
                    BigDecimal giOid = new BigDecimal(part);
                    giOids.add(new BigDecimal(part));
                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(giOid);
                    this.updateReadStatus(msg);
                }
            }
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(giService.exportExcel(giOids, storeFlag)));
            rptFileName = "GoodsIssueReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
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
            GiSummaryHolder searchParam = this.initSearchParameter();
            int count = giHeaderService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            
            List<MsgTransactionsExHolder> summarys = giHeaderService.getListOfSummary(searchParam);
            if (summarys==null || summarys.isEmpty())
            {
                return INPUT;
            }
            
            List<GiSummaryHolder> giSummarys = new ArrayList<GiSummaryHolder>();
            
            for (MsgTransactionsExHolder msgTrans : summarys)
            {
                giSummarys.add((GiSummaryHolder)msgTrans);
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(giService.exportSummaryExcel(giSummarys, storeFlag)));
            rptFileName = "GI_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    
    private GiSummaryHolder initSearchParameter() throws Exception
    {
        GiSummaryHolder searchParam = (GiSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_GI);
        
        if (searchParam == null)
        {
            searchParam = new GiSummaryHolder();
            
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setGiDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setGiDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setGiDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setGiDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setGiDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setGiDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_GI, searchParam);
        }
        
        searchParam = initSortField(searchParam);
        searchParam.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
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
    // getter and setter
    // *****************************************************
    public InputStream getRptResult()
    {
        return rptResult;
    }

    
    public GiSummaryHolder getParam()
    {
        return param;
    }

    public void setParam(GiSummaryHolder param)
    {
        this.param = param;
    }

    public void setRptResult(InputStream rptResult)
    {
        this.rptResult = rptResult;
    }

    
    public String getRptFileName()
    {
        return rptFileName;
    }


    public String getErrorMsg()
    {
        return errorMsg;
    }


    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
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
