//*****************************************************************************
//
// File Name       :  CreditClaimAction.java
// Date Created    :  2013-12-25
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2013-12-25 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.CcStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.CcDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcDetailHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CcHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.CcSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.report.DefaultCcReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.CcDetailExtendedService;
import com.pracbiz.b2bportal.core.service.CcDetailService;
import com.pracbiz.b2bportal.core.service.CcHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.CcHeaderService;
import com.pracbiz.b2bportal.core.service.CcService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

public class CreditClaimAction  extends TransactionalDocsBaseAction implements CoreCommonConstants, ApplicationContextAware
{
    /**
     * 
     */
    private static final long serialVersionUID = 500297030099141612L;

    private static final Logger log = LoggerFactory.getLogger(CreditClaimAction.class);
    
    public static final String SESSION_KEY_SEARCH_PARAMETER_CC = "SEARCH_PARAMETER_CC";
    
    private static final String SESSION_OID_PARAMETER = "session.parameter.CreditClaimAction.selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    
    transient private ApplicationContext ctx;
    transient private InputStream rptResult;
    private String rptFileName;
    
    private Map<String, String> ccStatuses;
    
    @Autowired transient private CcHeaderService ccHeaderService;
    @Autowired transient private CcDetailService ccDetailService;
    @Autowired transient private CcHeaderExtendedService ccHeaderExtendedService;
    @Autowired transient private CcDetailExtendedService ccDetailExtendedService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private PoHeaderService poHeaderService;
    @Autowired transient private CcService ccService;
    
    private CcSummaryHolder param;
    private String errorMsg;
    private boolean selectAll;

    public CreditClaimAction()
    {
        this.initMsg();
        this.setPageId(PageId.P010.name());
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

            BigDecimal docOid = new BigDecimal(parts[0]);
            
            CcHeaderHolder cc = ccHeaderService.selectCcHeaderByKey(docOid);
            if ("PO".equalsIgnoreCase(docType))
            {
                PoHeaderHolder po = poHeaderService
                    .selectEffectivePoHeaderByPoNo(cc.getBuyerOid(),
                        cc.getPoNo(), cc.getSupplierCode());
                if (po == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"PO", cc.getPoNo()});;
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_CC);
        
        param = (CcSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_CC);
        
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
            param = new CcSummaryHolder();
        }
        
        try
        {
            param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getPoDateFrom()));
            param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getPoDateTo()));
            param.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getInvDateFrom()));
            param.setInvDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getInvDateTo()));
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
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_CC, param);
        
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            CcSummaryHolder searchParam = this.initSearchParameter();
            
            this.obtainListRecordsOfPagination(ccHeaderService, searchParam, MODULE_KEY_CC);
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
                param = (CcSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_CC);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_CC, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<CcSummaryHolder> ccList = ccHeaderService.selectAllRecordToExport(param);
                
                if (ccList != null && !ccList.isEmpty())
                {
                    files = new String[ccList.size()];
                    for (int i = 0; i < ccList.size(); i++)
                    {
                        CcSummaryHolder cc = ccList.get(i);
                        
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(cc.getDocOid());
                        
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
                                    MsgType.CC.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                            
                            DefaultCcReportEngine ccReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                                .getBuyerCode());
                            
                            ReportEngineParameter<CcHolder> reportParameter = retrieveParameter(msg, supplier);

                            int isForStore = DefaultReportEngine.PDF_TYPE_STANDARD;
                            if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 || 
                                    BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                            {
                                isForStore = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                            }
                            //0 means standard pdf, 1 means for buyer store pdf 
                            byte[] datas = ccReportEngine.generateReport(reportParameter, isForStore);
                            
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
                    
                    
                    CcHeaderHolder cc = ccHeaderService.selectCcHeaderByKey(docOid);
                    if ("PO".equalsIgnoreCase(docType))
                    {
                        PoHeaderHolder po = poHeaderService
                            .selectEffectivePoHeaderByPoNo(cc.getBuyerOid(),
                                cc.getPoNo(), cc.getSupplierCode());
                        docOid = po.getPoOid();
                        this.getSession().put("session.parameter.PurchaseOrderAction.selectedOids", docOid + "/I-");
                        return "PO";
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
                                MsgType.CC.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                        
                        DefaultCcReportEngine ccReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                            .getBuyerCode());
                        
                        ReportEngineParameter<CcHolder> reportParameter = retrieveParameter(msg, supplier);

                        int isForStore = DefaultReportEngine.PDF_TYPE_STANDARD;
                        if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 || 
                                BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                        {
                            isForStore = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                        }
                        //0 means standard pdf, 1 means for buyer store pdf 
                        byte[] datas = ccReportEngine.generateReport(reportParameter, isForStore);
                        
                        FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                        
                    } 
                    files[i] = file.getPath();
                }
            }
            
            rptResult = PdfReportUtil.getInstance().mergePDFs(files);
            
            if(files != null && files.length > 1)
            {
                rptFileName = "CreditClaimReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
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
                param = (CcSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_CC);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_CC, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<CcSummaryHolder> ccList = ccHeaderService.selectAllRecordToExport(param);
                
                if (ccList != null && !ccList.isEmpty())
                {
                    for (CcSummaryHolder cc : ccList)
                    {
                        invOids.add(cc.getDocOid());
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(cc.getDocOid());
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
            
            this.setRptResult(new ByteArrayInputStream(ccService.exportExcel(invOids, storeFlag)));
            rptFileName = "CreditClaimReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
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
            CcSummaryHolder searchParam = this.initSearchParameter();
            int count = ccHeaderService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            
            List<MsgTransactionsExHolder> summarys = ccHeaderService.getListOfSummary(searchParam);
            if (summarys==null || summarys.isEmpty())
            {
                return INPUT;
            }
            
            List<CcSummaryHolder> ccSummarys = new ArrayList<CcSummaryHolder>();
            
            for (MsgTransactionsExHolder msgTrans : summarys)
            {
                ccSummarys.add((CcSummaryHolder)msgTrans);
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(ccService.exportSummaryExcel(ccSummarys, storeFlag)));
            rptFileName = "CC_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
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
    private ReportEngineParameter<CcHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        CcHeaderHolder header = ccHeaderService.selectCcHeaderByKey(msg.getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
        List<CcDetailHolder> details = ccDetailService.selectCcDetailByKey(msg.getDocOid());
        List<CcHeaderExtendedHolder> headerExtendeds = ccHeaderExtendedService.selectHeaderExtendedByKey(msg.getDocOid());
        List<CcDetailExtendedHolder> detailExtendeds = ccDetailExtendedService.selectDetailExtendedByKey(msg.getDocOid());
       
        ReportEngineParameter<CcHolder> reportEngineParameter = new ReportEngineParameter<CcHolder>();

        CcHolder data = new CcHolder();

        data.setCcHeader(header);
        data.setDetails(details);
        data.setHeaderExtendeds(headerExtendeds);
        data.setDetailExtendeds(detailExtendeds);

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    private DefaultCcReportEngine retrieveEngine(
        BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.CC.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                DefaultCcReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.CC
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultCcReportEngine.class);
    }
    
    private CcSummaryHolder initSortField(CcSummaryHolder param)
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
        ccStatuses = CcStatus.toMapValue(this);
        if (param == null)
        {
            param = new CcSummaryHolder();
        }
        if(getProfileOfCurrentUser().getBuyerOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
            {
                param.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setInvDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
        if (getProfileOfCurrentUser().getSupplierOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
            {
                param.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setInvDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
    }
    
    private CcSummaryHolder initSearchParameter() throws Exception
    {
        CcSummaryHolder searchParam = (CcSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_CC);
        
        if (searchParam == null)
        {
            searchParam = new CcSummaryHolder();
            
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setInvDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setInvDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_CC, searchParam);
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


    public CcSummaryHolder getParam()
    {
        return param;
    }


    public void setParam(CcSummaryHolder param)
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


    public Map<String, String> getCcStatuses()
    {
        return ccStatuses;
    }


    public void setCcStatuses(Map<String, String> ccStatuses)
    {
        this.ccStatuses = ccStatuses;
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
