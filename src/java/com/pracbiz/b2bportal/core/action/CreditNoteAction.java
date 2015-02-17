package com.pracbiz.b2bportal.core.action;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.CnSummaryHolder;
import com.pracbiz.b2bportal.core.report.DefaultCnReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.CnHeaderService;
import com.pracbiz.b2bportal.core.service.CnService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

public class CreditNoteAction extends TransactionalDocsBaseAction implements
        ApplicationContextAware
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(CreditNoteAction.class);
    transient private ApplicationContext ctx;
    private static final String SESSION_KEY_SEARCH_PARAMETER_CN = "SEARCH_PARAMETER_CN";
    private static final String SESSION_OID_PARAMETER = "session.parameter.CreditNoteAction.selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    
    private CnSummaryHolder param;
    private Map<String, String> cnStatus;
    private Map<String, String> readStatus;
    private transient InputStream rptResult;
    private String rptFileName;
    private String errorMsg;
    private boolean selectAll;
    
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private CnHeaderService cnHeaderService;
    @Autowired transient private CnService cnService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private PoHeaderService poHeaderService;
    

    public CreditNoteAction()
    {
        this.initMsg();
        this.setPageId(PageId.P013.name());
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
    
    public String init()
    {   
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_CN);
        
        param = (CnSummaryHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_CN);
        
        try
        {
            this.initSearchCondition();
            this.initSearchCriteria();
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
            param = new CnSummaryHolder();
        }
        
        try
        {
            
            param.setCnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getCnDateFrom()));
            param.setCnDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getCnDateTo()));
            param.setSentDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getSentDateFrom()));
            param.setSentDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getSentDateTo()));
            param.setReceivedDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getReceivedDateFrom()));
            param.setReceivedDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getReceivedDateTo()));
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_CN, param);
        
        return SUCCESS;
    }

    
    public String data()
    {
        try
        {
            CnSummaryHolder searchParam = this.initSearchParameter();
            this.obtainListRecordsOfPagination(cnHeaderService, searchParam, MODULE_KEY_CN);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    private void initSearchCondition() throws Exception
    {
        readStatus = ReadStatus.toMapValue(this);
        cnStatus = CnStatus.toMapValue(this);
        
        if(null == param)
        {
            param = new CnSummaryHolder();
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CoreCommonConstants.SECT_ID_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    param.setCnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    param.setCnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                param.setCnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CoreCommonConstants.SECT_ID_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    param.setCnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    param.setCnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                param.setCnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
        }
    }
    
    
    private CnSummaryHolder initSearchParameter() throws Exception
    {
        CnSummaryHolder searchParam = (CnSummaryHolder)getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_CN);

        if(searchParam == null)
        {
            searchParam = new CnSummaryHolder();

            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CoreCommonConstants.SECT_ID_CTRL, "DOCUMENT_WINDOW_BUYER");
                if(null == documentWindow.getNumValue()
                    || documentWindow.getNumValue() > 7
                    || documentWindow.getNumValue() < 1)
                {
                    searchParam.setCnDateFrom(DateUtil.getInstance()
                        .getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setCnDateFrom(DateUtil.getInstance()
                        .getFirstTimeOfDay(
                            DateUtil.getInstance().dateAfterDays(new Date(),
                                -documentWindow.getNumValue() + 1)));
                }
                searchParam.setCnDateTo(DateUtil.getInstance()
                    .getLastTimeOfDay(new Date()));
            }
            if(getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CoreCommonConstants.SECT_ID_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if(null == documentWindow.getNumValue()
                    || documentWindow.getNumValue() > 14
                    || documentWindow.getNumValue() < 1)
                {
                    searchParam.setCnDateFrom(DateUtil.getInstance()
                        .getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setCnDateFrom(DateUtil.getInstance()
                        .getFirstTimeOfDay(
                            DateUtil.getInstance().dateAfterDays(new Date(),
                                -documentWindow.getNumValue() + 1)));
                }
                searchParam.setCnDateTo(DateUtil.getInstance()
                    .getLastTimeOfDay(new Date()));
            }
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_CN, searchParam);
        }

        searchParam.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
        initCurrentUserSearchParam(searchParam);
        searchParam.trimAllString();
        searchParam.setAllEmptyStringToNull();
        
        return searchParam;
    }
    
    
    public String printPdf()
    {
        try
        {
            String[] files = null;
            
            if (selectAll)
            {
                param = (CnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_CN);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_CN, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<CnSummaryHolder> cnList = cnHeaderService.selectAllRecordToExport(param);
                
                if (cnList != null && !cnList.isEmpty())
                {
                    files = new String[cnList.size()];
                    for (int i = 0; i < cnList.size(); i++)
                    {
                        CnSummaryHolder cn = cnList.get(i);
                        
                        MsgTransactionsHolder msg = msgTransactionsService
                            .selectByKey(cn.getDocOid());

                        SupplierHolder supplier = supplierService
                            .selectSupplierByKey(msg.getSupplierOid());
                        
                        rptFileName = msg.getReportFilename();

                        File file = new File(mboxUtil.getFolderInSupplierDocOutPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())), msg.getReportFilename());
                        
                        CnHeaderHolder cnHeader = cnHeaderService.selectCnHeaderByKey(cn.getDocOid());
                        
                        if(!file.exists())
                        {
                            File docPath = new File(mboxUtil.getFolderInSupplierDocOutPath(
                                supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                    msg.getCreateDate())));
                            
                            if (!docPath.isDirectory())
                            {
                                FileUtil.getInstance().createDir(docPath);
                            }
                            
                            BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                    MsgType.CN.name(), cnHeader.getCnType().name());
                            DefaultCnReportEngine cnReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                                .getBuyerCode());

                            ReportEngineParameter<CnHolder> reportParameter = retrieveParameter(
                                msg, supplier);

                            byte[] datas = cnReportEngine.generateReport(reportParameter, DefaultReportEngine.PDF_TYPE_STANDARD);

                            FileUtil.getInstance().writeByteToDisk(datas, file.getPath());

                        }
                        
                        if(msg.getReadStatus() != null
                            && msg.getReadStatus().equals(ReadStatus.UNREAD)
                            && this.getUserTypeOfCurrentUser() != null)
                        {
                            UserTypeHolder userType = userTypeService.selectByKey(this
                                .getUserTypeOfCurrentUser());

                            if(userType.getUserTypeId().equalsIgnoreCase(
                                USER_TYPE_ID_BUYER_ADMIN)
                                || userType.getUserTypeId().equalsIgnoreCase(
                                    USER_TYPE_ID_BUYER_USER))
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

                    MsgTransactionsHolder msg = msgTransactionsService
                        .selectByKey(docOid);

                    SupplierHolder supplier = supplierService
                        .selectSupplierByKey(msg.getSupplierOid());
                    
                    rptFileName = msg.getReportFilename();

                    File file = new File(mboxUtil.getFolderInSupplierDocOutPath(
                        supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                            msg.getCreateDate())), msg.getReportFilename());
                    
                    CnHeaderHolder cnHeader = cnHeaderService.selectCnHeaderByKey(docOid);
                    
                    if ("PO".equalsIgnoreCase(docType))
                    {
                        PoHeaderHolder po = poHeaderService
                            .selectEffectivePoHeaderByPoNo(cnHeader.getBuyerOid(),
                                    cnHeader.getPoNo(), cnHeader.getSupplierCode());
                        docOid = po.getPoOid();
                        this.getSession().put("session.parameter.PurchaseOrderAction.selectedOids", docOid + "/I-");
                        return "PO";
                    }
                    
                    if(!file.exists())
                    {
                        
                        File docPath = new File(mboxUtil.getFolderInSupplierDocOutPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())));
                        
                        if (!docPath.isDirectory())
                        {
                            FileUtil.getInstance().createDir(docPath);
                        }
                        
                        BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                MsgType.CN.name(), cnHeader.getCnType().name());
                        DefaultCnReportEngine cnReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                            .getBuyerCode());

                        ReportEngineParameter<CnHolder> reportParameter = retrieveParameter(
                            msg, supplier);

                        byte[] datas = cnReportEngine.generateReport(reportParameter, DefaultReportEngine.PDF_TYPE_STANDARD);

                        FileUtil.getInstance().writeByteToDisk(datas, file.getPath());

                    }
                    
                    if(msg.getReadStatus() != null
                        && msg.getReadStatus().equals(ReadStatus.UNREAD)
                        && this.getUserTypeOfCurrentUser() != null)
                    {
                        UserTypeHolder userType = userTypeService.selectByKey(this
                            .getUserTypeOfCurrentUser());

                        if(userType.getUserTypeId().equalsIgnoreCase(
                            USER_TYPE_ID_BUYER_ADMIN)
                            || userType.getUserTypeId().equalsIgnoreCase(
                                USER_TYPE_ID_BUYER_USER))
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
                rptFileName = "cnReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
            }

        }
        catch(Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

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

            BigDecimal oid = new BigDecimal(parts[0]);
            
            CnHeaderHolder cnHeader = cnHeaderService.selectCnHeaderByKey(oid);
            
            if ("PO".equalsIgnoreCase(docType))
            {
                PoHeaderHolder po = poHeaderService
                    .selectEffectivePoHeaderByPoNo(cnHeader.getBuyerOid(),
                            cnHeader.getPoNo(), cnHeader.getSupplierCode());
                
                if (po == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"PO", cnHeader.getPoNo()});;
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
    
    
    protected ReportEngineParameter<CnHolder> retrieveParameter(
            MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
    
        ReportEngineParameter<CnHolder> reportEngineParameter = new ReportEngineParameter<CnHolder>();

        CnHolder data = cnService.selectByKey(msg.getDocOid());

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    
    protected DefaultCnReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.CN.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultCnReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.CN
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultCnReportEngine.class);
    }
    
    
    public String send()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);

            if(null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }

            this.getSession().remove(SESSION_OID_PARAMETER);

            String[] parts = selectedOids.toString().split(
                REQUEST_OID_DELIMITER);
            BigDecimal cnOid = null;
            
            for(int i = 0; i < parts.length; i++)
            {
                cnOid = new BigDecimal(parts[i]);
                
                CnHeaderHolder cnHeader = cnHeaderService.selectCnHeaderByKey(cnOid);
                
                if(cnHeader.getCtrlStatus().equals(CnStatus.NEW))
                {
                    cnHeaderService.sendCreditNote(this.getCommonParameter(), cnOid);
                    init();
                    search();
                }
                else
                {
                    this.addActionError(getText("B2BPC3001", new String[]{cnHeader.getCtrlStatus().name()}));
                    init();
                    search();
                    return INPUT;
                }
            }

        }
        catch(Exception e)
        {   
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    public CnSummaryHolder getParam()
    {
        return param;
    }

    public void setParam(CnSummaryHolder param)
    {
        this.param = param;
    }

    public Map<String, String> getCnStatus()
    {
        return cnStatus;
    }

    public Map<String, String> getReadStatus()
    {
        return readStatus;
    }

    public InputStream getRptResult()
    {
        return rptResult;
    }

    public String getRptFileName()
    {
        return rptFileName;
    }

    public String getErrorMsg()
    {
        return errorMsg;
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
