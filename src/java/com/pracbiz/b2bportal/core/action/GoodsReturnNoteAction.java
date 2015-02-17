//*****************************************************************************
//
// File Name       :  GoodReturnNoteAction.java
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
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.constants.RtvStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.RtvSummaryHolder;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultRtvReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.RtvDetailExtendedService;
import com.pracbiz.b2bportal.core.service.RtvDetailService;
import com.pracbiz.b2bportal.core.service.RtvHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailExtendedService;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailService;
import com.pracbiz.b2bportal.core.service.RtvLocationService;
import com.pracbiz.b2bportal.core.service.RtvService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

public class GoodsReturnNoteAction  extends TransactionalDocsBaseAction implements CoreCommonConstants, ApplicationContextAware
{
    private static final Logger log = LoggerFactory.getLogger(GoodsReturnNoteAction.class);
    private static final long serialVersionUID = -6564850224388546964L;
    
    public static final String SESSION_KEY_SEARCH_PARAMETER_RTV = "SEARCH_PARAMETER_RTV";
    
    private static final String SESSION_OID_PARAMETER = "session.parameter.GoodsReturnNoteAction.selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    
    transient private ApplicationContext ctx;
    transient private InputStream rptResult;
    private String rptFileName;
    private boolean selectAll;
    
    @Autowired transient private RtvHeaderService rtvHeaderService;
    @Autowired transient private RtvDetailService rtvDetailService;
    @Autowired transient private RtvHeaderExtendedService rtvHeaderExtendedService;
    @Autowired transient private RtvDetailExtendedService rtvDetailExtendedService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private RtvLocationService rtvLocationService;
    @Autowired transient private RtvLocationDetailService rtvLocationDetailService;
    @Autowired transient private RtvLocationDetailExtendedService rtvLocationDetailExtendedService;
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private InvHeaderService invHeaderService;
    @Autowired transient private RtvService rtvService;
    
    private RtvSummaryHolder param;
    private String errorMsg;

    public GoodsReturnNoteAction()
    {
        this.initMsg();
        this.setPageId(PageId.P003.name());
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
        String docType = this.getRequest().getParameter("docType");
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            
            if(null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }

            String[] parts = selectedOids.toString().split(
                REQUEST_OID_DELIMITER);
            
            BigDecimal docOid = new BigDecimal(parts[0]);
            
            if ("INV".equalsIgnoreCase(docType))
            {
                RtvHeaderHolder rtvHeader = rtvHeaderService.selectRtvHeaderByKey(docOid);
                InvHeaderHolder inv  = invHeaderService.selectEffectiveInvHeaderByInNo(
                    rtvHeader.getBuyerOid(), rtvHeader.getSupplierCode(),
                    rtvHeader.getInvNo());
                
                if (inv == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"INV", rtvHeader.getInvNo()});;
                    return INPUT;
                }
                
                if(BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()))
                {
                    if (!inv.getInvStatus().equals(InvStatus.SUBMIT))
                    {
                        errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"INV", rtvHeader.getInvNo()});;
                        return INPUT;
                    }
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_RTV);
        
        param = (RtvSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_RTV);
        
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
            param = new RtvSummaryHolder();
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
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_RTV, param);
        
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            RtvSummaryHolder searchParam = this.initSearchParameter();
            this.obtainListRecordsOfPagination(rtvHeaderService, searchParam, MODULE_KEY_RTV);
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
        String docType = this.getRequest().getParameter("docType");
        try
        {
            String[] files = null;
            
            if (selectAll)
            {
                param = (RtvSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_RTV);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_RTV, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<RtvSummaryHolder> rtvList = rtvHeaderService.selectAllRecordToExport(param);
                
                if (rtvList != null && !rtvList.isEmpty())
                {
                    files = new String[rtvList.size()];
                    for (int i = 0; i < rtvList.size(); i++)
                    {
                        RtvSummaryHolder rtv = rtvList.get(i);
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(rtv.getDocOid());
                        
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
                                    MsgType.RTV.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                            
                            DefaultRtvReportEngine rtvReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                                .getBuyerCode());
                            
                            ReportEngineParameter<RtvHolder> reportParameter = retrieveParameter(msg, supplier);

                            int isForStore = DefaultReportEngine.PDF_TYPE_STANDARD;
                            if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 || 
                                    BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                            {
                                isForStore = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                            }
                            //0 means standard pdf, 1 means for buyer store pdf 
                            byte[] datas = rtvReportEngine.generateReport(reportParameter, isForStore);
                            
                            FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                            
                        } 
                        
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
                    
                    if ("INV".equalsIgnoreCase(docType))
                    {
                        RtvHeaderHolder rtvHeader = rtvHeaderService.selectRtvHeaderByKey(docOid);
                        InvHeaderHolder inv = invHeaderService
                            .selectEffectiveInvHeaderByInNo(
                                rtvHeader.getBuyerOid(),
                                rtvHeader.getSupplierCode(), rtvHeader.getInvNo());
                        docOid = inv.getInvOid();
                        this.getSession().put("session.parameter.eInvoice.selectedOids", docOid);
                        return "INV";
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
                                MsgType.RTV.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                        
                        DefaultRtvReportEngine rtvReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                            .getBuyerCode());
                        
                        ReportEngineParameter<RtvHolder> reportParameter = retrieveParameter(msg, supplier);

                        int isForStore = DefaultReportEngine.PDF_TYPE_STANDARD;
                        if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 || 
                                BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                        {
                            isForStore = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                        }
                        //0 means standard pdf, 1 means for buyer store pdf 
                        byte[] datas = rtvReportEngine.generateReport(reportParameter, isForStore);
                        
                        FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                        
                    } 
                    
                    files[i] = file.getPath();
                    
                }
            }
            
            
            rptResult = PdfReportUtil.getInstance().mergePDFs(files);
            
            if(files != null && files.length > 1)
            {
                rptFileName = "rtvReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
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
            List<BigDecimal> giOids = new ArrayList<BigDecimal>();
            if (selectAll)
            {
                param = (RtvSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_RTV);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_RTV, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<RtvSummaryHolder> rtvList = rtvHeaderService.selectAllRecordToExport(param);
                
                if (rtvList != null && !rtvList.isEmpty())
                {
                    for (RtvSummaryHolder rtv : rtvList)
                    {
                        giOids.add(rtv.getDocOid());
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(rtv.getDocOid());
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
            
            this.setRptResult(new ByteArrayInputStream(rtvService.exportExcel(giOids, storeFlag)));
            rptFileName = "RtvReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
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
            RtvSummaryHolder searchParam = this.initSearchParameter();
            int count = rtvHeaderService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            
            List<MsgTransactionsExHolder> summarys = rtvHeaderService.getListOfSummary(searchParam);
            if (summarys==null || summarys.isEmpty())
            {
                return INPUT;
            }
            
            List<RtvSummaryHolder> rtvSummarys = new ArrayList<RtvSummaryHolder>();
            
            for (MsgTransactionsExHolder msgTrans : summarys)
            {
                rtvSummarys.add((RtvSummaryHolder)msgTrans);
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(rtvService.exportSummaryExcel(rtvSummarys, storeFlag)));
            rptFileName = "RTV_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
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
    private ReportEngineParameter<RtvHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        RtvHeaderHolder header = rtvHeaderService.selectRtvHeaderByKey(msg.getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
        List<RtvDetailHolder> details = rtvDetailService.selectRtvDetailByKey(msg.getDocOid());
        List<RtvHeaderExtendedHolder> headerExtendeds = rtvHeaderExtendedService.selectHeaderExtendedByKey(msg.getDocOid());
        List<RtvDetailExtendedHolder> detailExtendeds = rtvDetailExtendedService.selectDetailExtendedByKey(msg.getDocOid());
        List<RtvLocationHolder> locations = rtvLocationService.selectRtvLocationByRtvOid(msg.getDocOid());
        List<RtvLocationDetailHolder> locationDetails = rtvLocationDetailService.selectRtvLocationDetailByRtvOid(msg.getDocOid());
        List<RtvLocationDetailExtendedHolder> rtvLocDetailExtendeds = rtvLocationDetailExtendedService.selectRtvLocationDetailExByRtvOid(msg.getDocOid());
       
        ReportEngineParameter<RtvHolder> reportEngineParameter = new ReportEngineParameter<RtvHolder>();

        RtvHolder data = new RtvHolder();

        data.setRtvHeader(header);
        data.setRtvDetail(details);
        data.setHeaderExtended(headerExtendeds);
        data.setDetailExtended(detailExtendeds);
        data.setLocations(locations);
        data.setLocationDetails(locationDetails);
        data.setRtvLocDetailExtendeds(rtvLocDetailExtendeds);

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    private DefaultRtvReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.RTV.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultRtvReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.RTV
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultRtvReportEngine.class);
    }
    
    private RtvSummaryHolder initSortField(RtvSummaryHolder param)
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
        docStatuses = RtvStatus.toMapValue(this);
        if (param == null)
        {
            param = new RtvSummaryHolder();
        }
        if(getProfileOfCurrentUser().getBuyerOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
            {
                param.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setRtvDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
        if (getProfileOfCurrentUser().getSupplierOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
            {
                param.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setRtvDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
    }
    
    
    private RtvSummaryHolder initSearchParameter() throws Exception
    {
        RtvSummaryHolder searchParam = (RtvSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_RTV);
    
        if (searchParam == null)
        {
            searchParam = new RtvSummaryHolder();
            
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setRtvDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setRtvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setRtvDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_RTV, searchParam);
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

    public RtvSummaryHolder getParam()
    {
        return param;
    }


    public void setParam(RtvSummaryHolder param)
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
