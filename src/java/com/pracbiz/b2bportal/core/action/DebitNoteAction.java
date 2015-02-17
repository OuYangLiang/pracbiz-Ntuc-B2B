//*****************************************************************************
//
// File Name       :  DebitNoteAction.java
// Date Created    :  2012-12-13
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-13$
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
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.DnBuyerStatus;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.DocSubclassHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserClassHolder;
import com.pracbiz.b2bportal.core.holder.UserSubclassHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnHeaderExHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.mapper.DocSubclassMapper;
import com.pracbiz.b2bportal.core.report.DefaultDnReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.DnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.DnDetailService;
import com.pracbiz.b2bportal.core.service.DnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.GiHeaderService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.service.UserClassService;
import com.pracbiz.b2bportal.core.service.UserSubclassService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.DateJsonValueProcessor;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class DebitNoteAction extends TransactionalDocsBaseAction implements ApplicationContextAware
{
    private static final Logger log = LoggerFactory.getLogger(DebitNoteAction.class);
    private static final String B2BPC2204 = "B2BPC2204";

    private static final long serialVersionUID = 7522413695063453527L;
    
    private static final String SESSION_KEY_SEARCH_PARAMETER_DN = "SEARCH_PARAMETER_DN";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.DebitNoteAction.selectedOids";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final String REQUEST_OID_DELIMITER = "\\-";
    public static final String SESSION_KEY_SEARCH_PARAMETER_DN_POPUP_SUPPLIER = "SEARCH_PARAMETER_CREATE_GROUP_POPUP_SUPPLIER";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    private static final String SESSION_ACTION_TYPE = "actionType";
    
    @Autowired transient private DnService dnService;
    @Autowired transient private DnHeaderService dnHeaderService;
    @Autowired transient private DnDetailService dnDetailService;
    @Autowired transient private DnHeaderExtendedService dnHeaderExtendedService;
    @Autowired transient private DnDetailExtendedService dnDetailExtendedService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private OidService oidService;
    @Autowired transient private TradingPartnerService tradingPartnerService;
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private ItemService itemService;
    @Autowired transient private UserClassService userClassService;
    @Autowired transient private UserSubclassService userSubclassService;
    @Autowired transient private PoHeaderService poHeaderService;
    @Autowired transient private InvHeaderService invHeaderService;
    @Autowired transient private RtvHeaderService rtvHeaderService;
    @Autowired transient private GiHeaderService giHeaderService;
    @Autowired transient private BusinessRuleService businessRuleService;
    @Autowired transient private DocSubclassMapper docSubclassMapper;
    
    transient private ApplicationContext ctx;
    transient private InputStream rptResult;
    private Map<String, String> dnTypes;
    private Map<String, String> dnStatus;
    private DnSummaryHolder param;
    private String rptFileName;
    private String result;
    private DnHeaderExHolder dnHeaderEx;
    private List<DnDetailExHolder> dnDetailExs;
    private TradingPartnerExHolder tradingPartner;
    private static final Map<String, String> suppStortMap;
    private BigDecimal docOid;
    private DnHolder dn;
    private String errorMsg;
    private String dnHeaderJson;
    private String dnDetailJson;
    private String actionType;
    private String displayType;
    private Map<String, String> buyerStatus;
    private Map<String, String> priceStatus;
    private Map<String, String> qtyStatus;
    private boolean selectAll;
    
    static
    {
        suppStortMap = new HashMap<String, String>();
        suppStortMap.put("supplierCode", "SUPPLIER_CODE");
        suppStortMap.put("supplierName", "SUPPLIER_NAME");
        suppStortMap.put("disputeWindow", "DISPUTE_WINDOW");
    }

    public DebitNoteAction()
    {
        this.initMsg();
        this.setPageId(PageId.P005.name());
    }
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
        throws BeansException
    {
        this.ctx = ctx;
    }
    
    public String putParamIntoSession()
    {   
        this.getSession().put(SESSION_KEY_SEARCH_PARAMETER_DN,param);
     
        return SUCCESS;
    }
    
    public String putOidsParamIntoSession()
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

            BigDecimal oid = new BigDecimal(parts[0]);
            
            DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(oid);
            
            if ("PO".equalsIgnoreCase(docType))
            {
                PoHeaderHolder po = poHeaderService
                    .selectEffectivePoHeaderByPoNo(dnHeader.getBuyerOid(),
                        dnHeader.getPoNo(), dnHeader.getSupplierCode());
                
                if (po == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"PO", dnHeader.getPoNo()});;
                    return INPUT;
                }
            }
            
            if ("INV".equalsIgnoreCase(docType))
            {
                InvHeaderHolder inv = invHeaderService
                    .selectEffectiveInvHeaderByInNo(dnHeader.getBuyerOid(),
                        dnHeader.getSupplierCode(), dnHeader.getInvNo());
                
                if (inv == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"INV", dnHeader.getInvNo()});;
                    return INPUT;
                }
                
                if(BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()))
                {
                    if (!inv.getInvStatus().equals(InvStatus.SUBMIT))
                    {
                        errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"INV", dnHeader.getInvNo()});;
                        return INPUT;
                    }
                }
            }  
            
            if ("RTV".equalsIgnoreCase(docType))
            {
                RtvHeaderHolder rtv = rtvHeaderService
                    .selectRtvHeaderByRtvNo(dnHeader.getBuyerOid(),
                        dnHeader.getRtvNo(), dnHeader.getSupplierCode());
                
                if (rtv == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"RTV", dnHeader.getRtvNo()});;
                    return INPUT;
                }
            }
            
            if ("GI".equalsIgnoreCase(docType))
            {
                GiHeaderHolder gi = giHeaderService.selectGiHeaderByGiNo(
                    dnHeader.getBuyerOid(), dnHeader.getGiNo(),
                    dnHeader.getSupplierCode());
                
                if(gi == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"GI", dnHeader.getGiNo()});;
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_DN);
        if (param == null)
        {
            param = (DnSummaryHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_DN);
        }
        
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
        this.getSession().put(SESSION_KEY_SEARCH_PARAMETER_DN, param);
        return SUCCESS;
    }
    
    public String search()
    {
        if (null == param)
        {
            param = new DnSummaryHolder();
        }
        
        if ("".equals(this.getRequest().getParameter("param.dispute")))
        {
            param.setDispute(null);
        }
        
        if ("".equals(this.getRequest().getParameter("param.closed")))
        {
            param.setClosed(null);
        }
        
        if ("".equals(this.getRequest().getParameter("param.priceDisputed")))
        {
            param.setPriceDisputed(null);
        }
        
        if ("".equals(this.getRequest().getParameter("param.qtyDisputed")))
        {
            param.setQtyDisputed(null);
        }
        
        try
        {
            param.setDnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getDnDateFrom()));
            param.setDnDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getDnDateTo()));
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
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_DN, param);
        
        return SUCCESS;
    }

    
    public String data()
    {
        try
        {
            DnSummaryHolder searchParam = this.initSearchParameter();
            
            this.obtainListRecordsOfPagination(dnHeaderService, searchParam, MODULE_KEY_DN);
            Map<BigDecimal, Boolean> allowDisputeConfigMap = new HashMap<BigDecimal, Boolean>();
            for (BaseHolder baseItem : this.getGridRlt().getItems())
            {
                DnSummaryHolder item = (DnSummaryHolder) baseItem;
                if (allowDisputeConfigMap.containsKey(item.getBuyerOid()))
                {
                    item.setAllowMatchingDnDispute(allowDisputeConfigMap.get(item.getBuyerOid()));
                }
                else
                {
                    Boolean allowDispute = businessRuleService.isAllowSupplierDisputeMatchingDn(item.getBuyerOid());
                    item.setAllowMatchingDnDispute(allowDispute);
                    allowDisputeConfigMap.put(item.getBuyerOid(), allowDispute);
                }
            }
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
            int pdfType = 0;
            
            if (selectAll)
            {
                param = (DnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_DN);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_DN, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<BigDecimal> dnOids = new ArrayList<BigDecimal>();
                
                List<DnSummaryHolder> dnList = dnHeaderService.selectAllRecordToExport(param);
                
                if (dnList != null && !dnList.isEmpty())
                {
                    for (DnSummaryHolder dn : dnList)
                    {
                        dnOids.add(dn.getDocOid());
                    }
                }
                
                if (!dnOids.isEmpty())
                {
                    files = new String[dnOids.size()];
                    for (int i = 0; i < dnOids.size(); i++)
                    {
                        BigDecimal docOid = dnOids.get(i);
                        
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(docOid);
                        
                        SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                        
                        rptFileName = msg.getReportFilename();
                        
                        if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 
                            || BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                        {
                            rptFileName = FileUtil.getInstance().trimAllExtension(rptFileName) + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".pdf";
                            pdfType = DefaultReportEngine.PDF_TYPE_BY_STORE_STORE;
                        }
                        
                        File file = new File(mboxUtil.getFolderInSupplierDocInPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())), rptFileName);
                        
                        if(!file.exists())
                        {
                            File docPath = new File(
                                mboxUtil.getFolderInSupplierDocInPath(supplier.getMboxId(),
                                    DateUtil.getInstance().getYearAndMonth(
                                        msg.getCreateDate())));
         
                            if (!docPath.isDirectory())
                            {
                                FileUtil.getInstance().createDir(docPath);
                            }
                            
                            BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                    MsgType.DN.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                            
                            DefaultDnReportEngine dnReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                                .getBuyerCode());
                            
                            ReportEngineParameter<DnHolder> reportParameter = retrieveParameter(msg, supplier);
                            
                            byte[] datas = dnReportEngine.generateReport(reportParameter, pdfType);//0 means standard pdf 
                            
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
                    BigDecimal oid = new BigDecimal(parts[i]);

                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(oid);
                    
                    SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                    
                    rptFileName = msg.getReportFilename();
                    
                    if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 
                        || BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                    {
                        rptFileName = FileUtil.getInstance().trimAllExtension(rptFileName) + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".pdf";
                        pdfType = DefaultReportEngine.PDF_TYPE_BY_STORE_STORE;
                    }
                    
                    File file = new File(mboxUtil.getFolderInSupplierDocInPath(
                        supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                            msg.getCreateDate())), rptFileName);
                    
                    
                    DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(oid);
                    
                    if ("PO".equalsIgnoreCase(docType))
                    {
                        PoHeaderHolder po = poHeaderService
                            .selectEffectivePoHeaderByPoNo(dnHeader.getBuyerOid(),
                                dnHeader.getPoNo(), dnHeader.getSupplierCode());
                        oid = po.getPoOid();
                        this.getSession().put("session.parameter.PurchaseOrderAction.selectedOids", oid + "/I-");
                        return "PO";
                    }
                    
                    if ("INV".equalsIgnoreCase(docType))
                    {
                        InvHeaderHolder inv = invHeaderService
                            .selectEffectiveInvHeaderByInNo(dnHeader.getBuyerOid(),
                                dnHeader.getSupplierCode(), dnHeader.getInvNo());
                        oid = inv.getInvOid();
                        this.getSession().put("session.parameter.eInvoice.selectedOids", oid);
                        return "INV";
                    }  
                    
                    if ("RTV".equalsIgnoreCase(docType))
                    {
                        RtvHeaderHolder rtv = rtvHeaderService
                            .selectRtvHeaderByRtvNo(dnHeader.getBuyerOid(),
                                dnHeader.getRtvNo(), dnHeader.getSupplierCode());
                        oid = rtv.getRtvOid();
                        this.getSession().put("session.parameter.GoodsReturnNoteAction.selectedOids", oid);
                        return "RTV";
                    }
                    
                    if ("GI".equalsIgnoreCase(docType))
                    {
                        GiHeaderHolder gi = giHeaderService.selectGiHeaderByGiNo(
                            dnHeader.getBuyerOid(), dnHeader.getGiNo(),
                            dnHeader.getSupplierCode());
                        oid = gi.getGiOid();
                        this.getSession().put("session.parameter.GoodsIssueAction.selectedOids", oid);
                        return "GI";
                    }
                    

                    if(!file.exists())
                    {
                        File docPath = new File(
                            mboxUtil.getFolderInSupplierDocInPath(supplier.getMboxId(),
                                DateUtil.getInstance().getYearAndMonth(
                                    msg.getCreateDate())));
     
                        if (!docPath.isDirectory())
                        {
                            FileUtil.getInstance().createDir(docPath);
                        }
                        
                        BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                MsgType.DN.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                        
                        DefaultDnReportEngine dnReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                            .getBuyerCode());
                        
                        ReportEngineParameter<DnHolder> reportParameter = retrieveParameter(msg, supplier);
                        
                        byte[] datas = dnReportEngine.generateReport(reportParameter, pdfType);//0 means standard pdf 
                        
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
                rptFileName = "dnReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
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
    // approve and reject debit note
    // *****************************************************
    public String approve()
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
            for (String part : parts)
            {
                BigDecimal dnOid = new BigDecimal(part);
                DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(dnOid);
                if (DnStatus.OUTDATED.equals(dnHeader.getDnStatus()))
                {
                    msg.saveError(this.getText("B2BPC2230", new String[]{dnHeader.getDnNo()}));
                    continue;
                }
                DnHeaderHolder newDnHeader = new DnHeaderHolder();
                BeanUtils.copyProperties(dnHeader, newDnHeader);
                newDnHeader.setMarkSentToSupplier(true);
                dnHeaderService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), dnHeader, newDnHeader);
                msg.saveSuccess(this.getText("B2BPC2231", new String[]{dnHeader.getDnNo()}));
            }
            
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            msg.addMessageTarget(mt);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        return FORWARD_COMMON_MESSAGE;
    }

    
    public String reject()
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
            
            for (String part : parts)
            {
                BigDecimal dnOid = new BigDecimal(part);
                DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(dnOid);
                DnHeaderHolder newDnHeader = new DnHeaderHolder();
                BeanUtils.copyProperties(dnHeader, newDnHeader);
                newDnHeader.setMarkSentToSupplier(false);
                dnHeaderService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), dnHeader, newDnHeader);
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        return SUCCESS;
    }
    
    
    public String checkSentStatus()
    {
        try
        {
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            
            if(null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }
            
            String[] parts = selectedOids.toString().split(
                    REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal dnOid = new BigDecimal(part);
                DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(dnOid);
                if (dnHeader.getSentToSupplier())
                {
                    result = "1";
                }
                else if(dnHeader.getMarkSentToSupplier())
                {
                    result = "2";
                }
                if (result != null && !result.trim().isEmpty())
                {
                    return SUCCESS;
                }
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // create debit note
    // *****************************************************
    public String initAdd() throws Exception
    {   
        dnTypes = DnType.toMapValue(this);
        return SUCCESS;
    }
    
    
    public void validateSaveAdd()
    {
        try
        {
            boolean flag = this.hasActionErrors();
            dnHeaderEx.trimAllString();
            dnHeaderEx.setAllEmptyStringToNull();
            dnDetailExs.removeAll(Collections.singleton(null));
            if (!flag)
            {
                if (dnHeaderEx.getDnNo() == null)
                {
                    this.addActionError(this.getText("B2BPC2221"));
                    flag = true;
                }
                if (!flag && dnHeaderService.selectDnHeaderByDnNo(dnHeaderEx.getDnNo()) != null)
                {
                    this.addActionError(this.getText("B2BPC2222"));
                    flag = true;
                }
                if (dnHeaderEx.getDnType() == null)
                {
                    this.addActionError(this.getText("B2BPC2223"));
                    flag = true;
                }
                if (dnHeaderEx.getDnDate() == null)
                {
                    this.addActionError(this.getText("B2BPC2203"));
                    flag = true;
                }
                if (dnHeaderEx.getSupplierCode() == null)
                {
                    this.addActionError(this.getText("B2BPC2224"));
                    flag = true;
                }
                if (dnHeaderEx.getVatRateVal() == null)
                {
                    this.addActionError(this.getText(B2BPC2204));
                    flag = true;
                }
                else
                {
                    try
                    {
                        dnHeaderEx.setVatRate(new BigDecimal(dnHeaderEx.getVatRateVal()));
                    }
                    catch (Exception e)
                    {
                        this.addActionError(this.getText(B2BPC2204));
                        flag = true;
                    }
                }
                for (DnDetailExHolder dnDetailEx : dnDetailExs)
                {
                    dnDetailEx.setPackingFactor(BigDecimal.ONE);
                    dnDetailEx.setDebitBaseUnit("P");
                    dnDetailEx.trimAllString();
                    dnDetailEx.setAllEmptyStringToNull();
                    if (dnDetailEx.getLineSeqNoVal() == null)
                    {
                        this.addActionError(this.getText("B2BPC2209"));
                        flag = true;
                    }
                    else
                    {
                        try
                        {
                            dnDetailEx.setLineSeqNo(Integer.parseInt(dnDetailEx.getLineSeqNoVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2210"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getBuyerItemCode() == null)
                    {
                        this.addActionError(this.getText("B2BPC2212"));
                        flag = true;
                    }
                    if (dnDetailEx.getDebitQtyVal() != null)
                    {
                        BigDecimal debitQty = new BigDecimal(dnDetailEx.getDebitQtyVal());
                        try
                        {
                            dnDetailEx.setDebitQty(debitQty);
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2214"));
                            flag = true;
                        }
                        
                        if (!flag && !((debitQty.compareTo(BigDecimal.ZERO) >= 0)&& (debitQty.compareTo(BigDecimal.valueOf(999999.99)) < 1)))
                        {
                            this.addActionError(this.getText("B2BPC2215"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getUnitCostVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setUnitCost(new BigDecimal(dnDetailEx.getUnitCostVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2215"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getCostDiscountAmountVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setCostDiscountAmount(new BigDecimal(dnDetailEx.getCostDiscountAmountVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2216"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getCostDiscountPercentVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setCostDiscountPercent(new BigDecimal(dnDetailEx.getCostDiscountPercentVal()));
                        }
                        catch (Exception e) 
                        {
                            this.addActionError(this.getText("B2BPC2217"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getNetUnitCostVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setNetUnitCost(new BigDecimal(dnDetailEx.getNetUnitCostVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2218"));
                            flag = true;
                        }
                    }
                }
                if (!flag)
                {
                    for (int i = 0; i < dnDetailExs.size(); i++)
                    {
                        boolean tmpFlag = false;
                        DnDetailExHolder dnDetailEx = dnDetailExs.get(i);
                        for (int j = 0; j < dnDetailExs.size(); j++)
                        {
                            DnDetailExHolder dnDetailTmp = dnDetailExs.get(j);
                            if (dnDetailEx.getLineSeqNo().compareTo(dnDetailTmp.getLineSeqNo()) == 0 && i != j)
                            {
                                this.addActionError(this.getText("B2BPC2211"));
                                flag = true;
                                tmpFlag = true;
                                break;
                            }
                        }
                        if (tmpFlag)
                        {
                            break;
                        }
                    }
                    for (int i = 0; i < dnDetailExs.size(); i++)
                    {
                        boolean tmpFlag = false;
                        DnDetailExHolder dnDetailEx = dnDetailExs.get(i);
                        for (int j = 0; j < dnDetailExs.size(); j++)
                        {
                            DnDetailExHolder dnDetailTmp = dnDetailExs.get(j);
                            if (dnDetailEx.getBuyerItemCode().equals(dnDetailTmp.getBuyerItemCode()) &&  i != j)
                            {
                                this.addActionError(this.getText("B2BPC2213"));
                                flag = true;
                                tmpFlag = true;
                                break;
                            }
                        }
                        if (tmpFlag)
                        {
                            break;
                        }
                    }
                }
            }
            if (flag)
            {
                dnTypes = DnType.toMapValue(this);
            }
        }
        catch (Exception e)
        {
            dnTypes = DnType.toMapValue(this);
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }
    
    
    public String saveAdd()
    {
        try
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(this.getProfileOfCurrentUser().getBuyerOid());
            dnHeaderEx.setDnOid(oidService.getOid());
            dnHeaderEx.setBuyerOid(buyer.getBuyerOid());
            dnHeaderEx.setBuyerCode(buyer.getBuyerCode());
            dnHeaderEx.setBuyerName(buyer.getBuyerName());
            dnHeaderEx.setBuyerAddr1(buyer.getAddress1());
            dnHeaderEx.setBuyerAddr2(buyer.getAddress2());
            dnHeaderEx.setBuyerAddr3(buyer.getAddress3());
            dnHeaderEx.setBuyerAddr4(buyer.getAddress4());
            dnHeaderEx.setBuyerState(buyer.getState());
            dnHeaderEx.setBuyerCtryCode(buyer.getCtryCode());
            dnHeaderEx.setBuyerPostalCode(buyer.getPostalCode());
            TradingPartnerHolder tp = tradingPartnerService.selectByBuyerAndBuyerGivenSupplierCode(buyer.getBuyerOid(), dnHeaderEx.getSupplierCode());
            SupplierHolder supplier = supplierService.selectSupplierByKey(tp.getSupplierOid());
            dnHeaderEx.setSupplierOid(supplier.getSupplierOid());
            dnHeaderEx.setSupplierName(supplier.getSupplierName());
            dnHeaderEx.setSupplierAddr1(supplier.getAddress1());
            dnHeaderEx.setSupplierAddr2(supplier.getAddress2());
            dnHeaderEx.setSupplierAddr3(supplier.getAddress3());
            dnHeaderEx.setSupplierAddr4(supplier.getAddress4());
            dnHeaderEx.setSupplierState(supplier.getState());
            dnHeaderEx.setSupplierCtryCode(supplier.getCtryCode());
            dnHeaderEx.setSupplierPostalCode(supplier.getPostalCode());
            dnHeaderEx.setDocAction("A");
            dnHeaderEx.setActionDate(new Date());
            dnHeaderEx.setSentToSupplier(false);
            dnHeaderEx.setMarkSentToSupplier(false);
            dnHeaderEx.setDnStatus(DnStatus.NEW);
            dnHeaderEx.setDuplicate(false);
            DnHolder dnHolder = new DnHolder();
            dnHolder.setDnHeader(dnHeaderEx);
            
            List<DnDetailExHolder> dnDetails = new ArrayList<DnDetailExHolder>();
            for (DnDetailExHolder dnDetailEx : dnDetailExs)
            {
                DnDetailExHolder dnDetail = new DnDetailExHolder();
                BeanUtils.copyProperties(dnDetailEx, dnDetail);
                dnDetail.setDnOid(dnHeaderEx.getDnOid());
                dnDetail.setItemCost(dnDetailEx.getItemCostVal() == null ? null : new BigDecimal(dnDetailEx.getItemCostVal()));
                dnDetail.setPackingFactor(BigDecimal.ONE);
                dnDetail.setDebitBaseUnit("P");
                dnDetail.setPoNo(dnHeaderEx.getPoNo());
                dnDetail.setPoDate(dnHeaderEx.getPoDate());
                dnDetail.setInvNo(dnHeaderEx.getInvNo());
                dnDetail.setInvDate(dnHeaderEx.getInvDate());
                dnDetails.add(dnDetail);
            }
            dnHolder.setDnDetail(dnDetails);
            
            String originalFilename = MsgType.DN.name() + DOC_FILENAME_DELIMITOR
                    + dnHeaderEx.getBuyerCode() + DOC_FILENAME_DELIMITOR
                    + dnHeaderEx.getSupplierCode() + DOC_FILENAME_DELIMITOR
                    + StringUtil.getInstance().convertDocNo(dnHeaderEx.getDnNo()) + ".txt";
            
            MsgTransactionsHolder msg = dnHeaderEx.convertToMsgTransaction(originalFilename);
            msg.setGeneratedOnPortal(true);
            dnService.insertDnWithMsgTransaction(this.getCommonParameter(), dnHolder, msg);
            log.info(this.getText("B2BPC2225", new String[]{this.getLoginIdOfCurrentUser(), dnHeaderEx.getDnNo()}));
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // edit debit note
    // *****************************************************
    public String initEdit() throws Exception
    {
        Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
        
        if(null == selectedOids)
        {
            throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
        }
        BigDecimalUtil decimalUtil =  BigDecimalUtil.getInstance();
        String[] parts = selectedOids.toString().split(
                REQUEST_OID_DELIMITER);
        
        BigDecimal dnOid = new BigDecimal(parts[0]);
        DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(dnOid);
        List<DnDetailExHolder> dnDetails = dnDetailService.selectDnDetailByKey(dnOid);
        dnHeaderEx = new DnHeaderExHolder();
        BeanUtils.copyProperties(dnHeader, dnHeaderEx);
        dnHeaderEx.setTotalCost(decimalUtil.format(dnHeader.getTotalCost(), 2));
        dnHeaderEx.setTotalCostWithVat(decimalUtil.format(dnHeader.getTotalCostWithVat(), 2));
        dnHeaderEx.setTotalVat(decimalUtil.format(dnHeader.getTotalVat(), 2));
        dnHeaderEx.setVatRateVal(dnHeader.getVatRate() == null ? null : dnHeader.getVatRate().toString());
        dnDetailExs = new ArrayList<DnDetailExHolder>();
        
        for (DnDetailHolder dnDetail : dnDetails)
        {
            DnDetailExHolder dnDetailEx = new DnDetailExHolder();
            BeanUtils.copyProperties(dnDetail, dnDetailEx);
            dnDetailEx.setLineSeqNoVal(dnDetail.getLineSeqNo() == null ? null : dnDetail.getLineSeqNo().toString());
            dnDetailEx.setDebitQtyVal(decimalUtil.convertBigDecimalToStringIntegerWithNoScale(dnDetail.getDebitQty(), 2));
            dnDetailEx.setUnitCostVal(decimalUtil.convertBigDecimalToStringIntegerWithNoScale(dnDetail.getUnitCost(), 2));
            dnDetailEx.setCostDiscountAmountVal(decimalUtil.convertBigDecimalToStringIntegerWithNoScale(dnDetail.getCostDiscountAmount(), 2));
            dnDetailEx.setCostDiscountPercentVal(decimalUtil.convertBigDecimalToStringIntegerWithNoScale(dnDetail.getCostDiscountPercent(), 2));
            dnDetailEx.setNetUnitCostVal(decimalUtil.convertBigDecimalToStringIntegerWithNoScale(dnDetail.getNetUnitCost(), 2));
            dnDetailEx.setItemCostVal(decimalUtil.convertBigDecimalToStringIntegerWithNoScale(dnDetail.getItemCost(), 2));
            dnDetailExs.add(dnDetailEx);
        }
        
        return SUCCESS;
    }
    
    
    public void validateSaveEdit()
    {
        try
        {
            boolean flag = this.hasActionErrors();
            dnHeaderEx.trimAllString();
            dnHeaderEx.setAllEmptyStringToNull();
            dnDetailExs.removeAll(Collections.singleton(null));
            if (!flag)
            {
                if (dnHeaderEx.getDnDate() == null)
                {
                    this.addActionError(this.getText("B2BPC2203"));
                    flag = true;
                }
                if (dnHeaderEx.getVatRateVal() == null)
                {
                    this.addActionError(this.getText(B2BPC2204));
                    flag = true;
                }
                else
                {
                    try
                    {
                        dnHeaderEx.setVatRate(new BigDecimal(dnHeaderEx.getVatRateVal()));
                    }
                    catch (Exception e)
                    {
                        this.addActionError(this.getText(B2BPC2204));
                        flag = true;
                    }
                }
                for (DnDetailExHolder dnDetailEx : dnDetailExs)
                {
                    dnDetailEx.setPackingFactor(BigDecimal.ONE);
                    dnDetailEx.setDebitBaseUnit("P");
                    dnDetailEx.trimAllString();
                    dnDetailEx.setAllEmptyStringToNull();
                    if (dnDetailEx.getLineSeqNoVal() == null)
                    {
                        this.addActionError(this.getText("B2BPC2209"));
                        flag = true;
                    }
                    else
                    {
                        try
                        {
                            dnDetailEx.setLineSeqNo(Integer.parseInt(dnDetailEx.getLineSeqNoVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2210"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getBuyerItemCode() == null)
                    {
                        this.addActionError(this.getText("B2BPC2212"));
                        flag = true;
                    }
                    if (dnDetailEx.getDebitQtyVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setDebitQty(new BigDecimal(dnDetailEx.getDebitQtyVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2214"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getUnitCostVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setUnitCost(new BigDecimal(dnDetailEx.getUnitCostVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2215"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getCostDiscountAmountVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setCostDiscountAmount(new BigDecimal(dnDetailEx.getCostDiscountAmountVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2216"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getCostDiscountPercentVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setCostDiscountPercent(new BigDecimal(dnDetailEx.getCostDiscountPercentVal()));
                        }
                        catch (Exception e) 
                        {
                            this.addActionError(this.getText("B2BPC2217"));
                            flag = true;
                        }
                    }
                    if (dnDetailEx.getNetUnitCostVal() != null)
                    {
                        try
                        {
                            dnDetailEx.setNetUnitCost(new BigDecimal(dnDetailEx.getNetUnitCostVal()));
                        }
                        catch (Exception e)
                        {
                            this.addActionError(this.getText("B2BPC2218"));
                            flag = true;
                        }
                    }
                }
                if (!flag)
                {
                    for (int i = 0; i < dnDetailExs.size(); i++)
                    {
                        boolean tmpFlag = false;
                        DnDetailExHolder dnDetailEx = dnDetailExs.get(i);
                        for (int j = 0; j < dnDetailExs.size(); j++)
                        {
                            DnDetailExHolder dnDetailTmp = dnDetailExs.get(j);
                            if (dnDetailEx.getLineSeqNo().compareTo(dnDetailTmp.getLineSeqNo()) == 0 && i != j)
                            {
                                this.addActionError(this.getText("B2BPC2211"));
                                flag = true;
                                tmpFlag = true;
                                break;
                            }
                        }
                        if (tmpFlag)
                        {
                            break;
                        }
                    }
                    for (int i = 0; i < dnDetailExs.size(); i++)
                    {
                        boolean tmpFlag = false;
                        DnDetailExHolder dnDetailEx = dnDetailExs.get(i);
                        for (int j = 0; j < dnDetailExs.size(); j++)
                        {
                            DnDetailExHolder dnDetailTmp = dnDetailExs.get(j);
                            if (dnDetailEx.getBuyerItemCode().equals(dnDetailTmp.getBuyerItemCode()) &&  i != j)
                            {
                                this.addActionError(this.getText("B2BPC2213"));
                                flag = true;
                                tmpFlag = true;
                                break;
                            }
                        }
                        if (tmpFlag)
                        {
                            break;
                        }
                    }
                }
            }
            DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(dnHeaderEx.getDnOid());
            dnHeaderEx.setDnNo(dnHeader.getDnNo());
            dnHeaderEx.setDnType(dnHeader.getDnType());
            
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }
    
    
    public String saveEdit()
    {
        try
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(dnHeaderEx
                    .getBuyerOid());
            dnHeaderEx.setBuyerCode(buyer.getBuyerCode());
            dnHeaderEx.setBuyerName(buyer.getBuyerName());
            dnHeaderEx.setBuyerAddr1(buyer.getAddress1());
            dnHeaderEx.setBuyerAddr2(buyer.getAddress2());
            dnHeaderEx.setBuyerAddr3(buyer.getAddress3());
            dnHeaderEx.setBuyerAddr4(buyer.getAddress4());
            dnHeaderEx.setBuyerState(buyer.getState());
            dnHeaderEx.setBuyerCtryCode(buyer.getCtryCode());
            dnHeaderEx.setBuyerPostalCode(buyer.getPostalCode());
            TradingPartnerHolder tp = tradingPartnerService.selectByBuyerAndBuyerGivenSupplierCode(buyer.getBuyerOid(), dnHeaderEx.getSupplierCode());
            SupplierHolder supplier = supplierService.selectSupplierByKey(tp.getSupplierOid());
            dnHeaderEx.setSupplierOid(supplier.getSupplierOid());
            dnHeaderEx.setSupplierName(supplier.getSupplierName());
            dnHeaderEx.setSupplierAddr1(supplier.getAddress1());
            dnHeaderEx.setSupplierAddr2(supplier.getAddress2());
            dnHeaderEx.setSupplierAddr3(supplier.getAddress3());
            dnHeaderEx.setSupplierAddr4(supplier.getAddress4());
            dnHeaderEx.setSupplierState(supplier.getState());
            dnHeaderEx.setSupplierCtryCode(supplier.getCtryCode());
            dnHeaderEx.setSupplierPostalCode(supplier.getPostalCode());
            dnHeaderEx.setDocAction("A");
            dnHeaderEx.setActionDate(new Date());
            dnHeaderEx.setSentToSupplier(false);
            dnHeaderEx.setMarkSentToSupplier(false);
            DnHolder oldDnHolder = dnService.selectDnByKey(dnHeaderEx.getDnOid());
            DnHeaderHolder oldHeader = oldDnHolder.getDnHeader();
            dnHeaderEx.setDocAction(oldHeader.getDocAction());
            dnHeaderEx.setActionDate(oldHeader.getActionDate());
            dnHeaderEx.setReasonCode(oldHeader.getReasonCode());
            dnHeaderEx.setReasonDesc(oldHeader.getReasonDesc());
            List<DnDetailExHolder> oldDnDetails = oldDnHolder.getDnDetail();
            DnHolder dnHolder = new DnHolder();
            dnHolder.setDnHeader(dnHeaderEx);
            List<DnDetailExHolder> dnDetails = new ArrayList<DnDetailExHolder>();
            for (DnDetailExHolder dnDetailEx : dnDetailExs)
            {
                DnDetailExHolder dnDetail = new DnDetailExHolder();
                BeanUtils.copyProperties(dnDetailEx, dnDetail);
                dnDetail.setDnOid(dnHeaderEx.getDnOid());
                dnDetail.setItemCost(dnDetailEx.getItemCostVal() == null ? null : new BigDecimal(dnDetailEx.getItemCostVal()));
                boolean flag = false;
                for (DnDetailExHolder oldDetail : oldDnDetails)
                {
                    if (oldDetail.getBuyerItemCode().equals(dnDetail.getBuyerItemCode()))
                    {
                        dnDetail.setBrand(oldDetail.getBrand());
                        dnDetail.setColourCode(oldDetail.getColourCode());
                        dnDetail.setColourDesc(oldDetail.getColourDesc());
                        dnDetail.setSizeCode(oldDetail.getSizeCode());
                        dnDetail.setSizeDesc(oldDetail.getSizeDesc());
                        dnDetail.setPackingFactor(oldDetail.getPackingFactor());
                        dnDetail.setDebitBaseUnit(oldDetail.getDebitBaseUnit());
                        dnDetail.setRetailDiscountAmount(oldDetail.getRetailDiscountAmount());
                        dnDetail.setItemSharedCode(oldDetail.getItemSharedCode());
                        dnDetail.setItemGrossCost(oldDetail.getItemGrossCost());
                        dnDetail.setRetailPrice(oldDetail.getRetailPrice());
                        dnDetail.setItemRetailAmount(oldDetail.getItemRetailAmount());
                        oldDnDetails.remove(oldDetail);
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                {
                    dnDetail.setPackingFactor(BigDecimal.ONE);
                    dnDetail.setDebitBaseUnit("P");
                }
                dnDetail.setPoNo(dnHeaderEx.getPoNo());
                dnDetail.setPoDate(dnHeaderEx.getPoDate());
                dnDetail.setInvNo(dnHeaderEx.getInvNo());
                dnDetail.setInvDate(dnHeaderEx.getInvDate());
                dnDetails.add(dnDetail);
            }
            dnHolder.setDnDetail(dnDetails);
            
            String originalFilename = MsgType.DN.name() + DOC_FILENAME_DELIMITOR
                    + dnHeaderEx.getBuyerCode() + DOC_FILENAME_DELIMITOR
                    + dnHeaderEx.getSupplierCode() + DOC_FILENAME_DELIMITOR
                    + StringUtil.getInstance().convertDocNo(dnHeaderEx.getDnNo()) + ".txt";
            
            dnService.updateDnWithMsgTransaction(this.getCommonParameter(), oldDnHolder,
                    dnHolder, dnHeaderEx.convertToMsgTransaction(originalFilename));
            String yyyymm = DateUtil.getInstance().getYearAndMonth(
                    new Date());
            //appConfig.getSupplierMailboxRootPath() + PS
            //+ supplier.getMboxId() + PS + "doc" + PS + "in" + PS + yyyymm
            File reportFile = new File(mboxUtil.getFolderInSupplierDocInPath(supplier.getMboxId(), yyyymm) + PS
                    + dnHeaderEx.computePdfFilename());
            if (reportFile.exists() && !reportFile.delete())
            {
                throw new IOException("field to delete file [" + reportFile + "]");
            }
            log.info(this.getText("B2BPC2226", new String[]{dnHeaderEx.getDnNo(), this.getLoginIdOfCurrentUser()}));
        }
        catch (IOException e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // delete debit note
    // *****************************************************
    public String saveDelete()
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
            
            for (String part : parts)
            {
                BigDecimal dnOid = new BigDecimal(part);
                DnHolder dnHolder = dnService.selectDnByKey(dnOid);
                if (dnHolder == null || dnHolder.getDnHeader() == null)
                {
                    continue;
                }
                if (dnHolder.getDnHeader().getMarkSentToSupplier())
                {
                    msg.saveError(this.getText("B2BPC2227", new String[]{dnHolder.getDnHeader().getDnNo()}));
                    continue;
                }
                dnService.auditDelete(this.getCommonParameter(), dnHolder);
                msg.saveSuccess(this.getText("B2BPC2228", new String[]{dnHolder.getDnHeader().getDnNo()}));
                log.info(this.getText("B2BPC2229", new String[]{dnHolder.getDnHeader().getDnNo(), this.getLoginIdOfCurrentUser()}));
            }
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            msg.addMessageTarget(mt);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // *****************************************************
    // popup suppliers summary page
    // *****************************************************
    public String initViewSupplier()
    {
        try
        {
            clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_DN_POPUP_SUPPLIER);
            if (tradingPartner == null && (SupplierExHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_DN_POPUP_SUPPLIER) == null)
            {
                tradingPartner = new TradingPartnerExHolder();
            }
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_DN_POPUP_SUPPLIER, tradingPartner);
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    

    public String searchSupplier()
    {
        if (null == tradingPartner)
        {
            tradingPartner = new TradingPartnerExHolder();
        }
        try
        {
            tradingPartner.trimAllString();
            tradingPartner.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        getSession()
                .put(SESSION_KEY_SEARCH_PARAMETER_DN_POPUP_SUPPLIER, tradingPartner);

        return SUCCESS;
    }

    
    public String supplierData() throws Exception
    {
        try
        {
            
            TradingPartnerExHolder searchParam = (TradingPartnerExHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_DN_POPUP_SUPPLIER);
            
            if (null == searchParam)
            {
                searchParam = new TradingPartnerExHolder();
                searchParam.setActive(true);
                searchParam.setFilterWithSupplier(true);
            }
            
            List<SupplierHolder> availableSuppliers = supplierService.selectAvailableSuppliersByUserOid(getProfileOfCurrentUser());
            if (availableSuppliers != null && !availableSuppliers.isEmpty())
            {
                for (SupplierHolder supplier : availableSuppliers)
                {
                    searchParam.addSupplierOid(supplier.getSupplierOid());
                }
            }
            
            searchParam.setActive(true);
            searchParam.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            this.obtainListRecordsOfPagination(tradingPartnerService,
                searchParam, suppStortMap, "tradingPartnerOid", null);

        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    

    
    // *****************************************************
    // dispute debit note
    // *****************************************************
    public String initDispute()
    {
        try
        {
            if (actionType != null && !actionType.trim().isEmpty())
            {
                this.getSession().put(SESSION_ACTION_TYPE, actionType);
                
                if (actionType.equals("price") && (null == this.getSession().get(PoInvGrnDnMatchingAction.BUYER_ALL_GRANTED) 
                        && null == this.getSession().get(PoInvGrnDnMatchingAction.BUYER_ITEM_CODES)))
                {
                    BigDecimal userOid = this.getProfileOfCurrentUser().getUserOid();
                    List<UserClassHolder> classes = userClassService.selectByUserOid(userOid);
                    List<UserSubclassHolder> subClasses = userSubclassService.selectByUserOid(userOid);
                    if (classes.isEmpty() && subClasses.isEmpty())
                    {
                        this.getSession().put(PoInvGrnDnMatchingAction.BUYER_ITEM_CODES, new ArrayList<String>());
                    }
                    else if ((!classes.isEmpty() && new BigDecimal(-1).equals(classes.get(0).getClassOid())) || (!subClasses.isEmpty() && new BigDecimal(-1).equals(subClasses.get(0).getSubclassOid())))
                    {
                        this.getSession().put(PoInvGrnDnMatchingAction.BUYER_ALL_GRANTED, "true");
                    }
                    else
                    {
                        this.getSession().put(PoInvGrnDnMatchingAction.BUYER_ITEM_CODES, itemService.selectActiveItemsByUserOid(userOid));
                    }
                }
                
            }
            
            String allItemGrant = (String) this.getSession().get(PoInvGrnDnMatchingAction.BUYER_ALL_GRANTED);
            @SuppressWarnings("unchecked")
            List<String> itemCodeList = (List<String>) this.getSession().get(PoInvGrnDnMatchingAction.BUYER_ITEM_CODES);
            
            dn = new DnHolder();
            
            DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(docOid);
            dn.setDnHeader(dnHeader);
            
            List<DnDetailExHolder> dnDetailList = dnDetailService.selectDnDetailByKey(docOid);
            for (DnDetailExHolder detail : dnDetailList)
            {
                if (actionType != null)
                {
                    detail.setPrivileged(true);
                    detail.setShowOnInit(true);
                    if ((dnHeader.getClosed() != null && dnHeader.getClosed()) || dnHeader.getDispute() == null || !dnHeader.getDispute())
                    {
                        detail.setPrivileged(false);
                    }
                    if (actionType.equals("price"))
                    {
                        if (!detail.isDisputePriceChanged())
                        {
                            detail.setPrivileged(false);
                            detail.setShowOnInit(false);
                        }
                        else if (!((allItemGrant != null && allItemGrant.equals("true")) || itemCodeList.contains(detail.getBuyerItemCode() + "-" + dnHeader.getBuyerOid())))
                        {
                            detail.setPrivileged(false);
                            detail.setShowOnInit(false);
                        }
                        else if (dnHeader.getDispute() != null && dnHeader.getDispute() 
                            && !dnHeader.getBuyerStatus().equals(DnBuyerStatus.PENDING))
                        {
                            detail.setPrivileged(false);
                        }
                    }
                    if (actionType.equals("qty"))
                    {
                        if (!detail.isDisputeQtyChanged())
                        {
                            detail.setPrivileged(false);
                            detail.setShowOnInit(false);
                        }
                        else if (dnHeader.getDispute() != null && dnHeader.getDispute() 
                            && !dnHeader.getBuyerStatus().equals(DnBuyerStatus.PENDING))
                        {
                            detail.setPrivileged(false);
                        }
                    }
                    if (actionType.equals("close"))
                    {
                        if (dnHeader.getClosed() != null && dnHeader.getClosed())
                        {
                            detail.setPrivileged(false);
                        }
                        else if (dnHeader.getDispute() != null && dnHeader.getDispute() 
                                && dnHeader.getBuyerStatus().equals(DnBuyerStatus.PENDING))
                        {
                            detail.setPrivileged(false);
                        }
                    }
                }
                if (displayType.equals("2"))
                {
                    detail.setPrivileged(false);
                }
                detail.setDebitQty(BigDecimalUtil.getInstance().format(detail.getDebitQty(), 2));
                detail.setDisputeQty(BigDecimalUtil.getInstance().format(detail.getDisputeQty(), 2));
                detail.setUnitCost(BigDecimalUtil.getInstance().format(detail.getUnitCost(), 2));
                detail.setDisputePrice(BigDecimalUtil.getInstance().format(detail.getDisputePrice(), 2));
                detail.setConfirmQty(BigDecimalUtil.getInstance().format(detail.getConfirmQty(), 2));
                detail.setConfirmPrice(BigDecimalUtil.getInstance().format(detail.getConfirmPrice(), 2));
                
            }
            dn.setDnDetail(dnDetailList);
            
            JsonConfig jsonConfig = new JsonConfig();    
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));

            JSONObject headerJson = JSONObject.fromObject(dnHeader, jsonConfig);
            JSONArray detailJsons = JSONArray.fromObject(dnDetailList, jsonConfig);
            
            dnHeaderJson = StringUtil.getInstance().replaceSpecialCharactersForJson(headerJson.toString());
            dnDetailJson = StringUtil.getInstance().replaceSpecialCharactersForJson(detailJsons.toString());
        }
        catch (Exception e)
        {
            handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public void validateSaveDispute()
    {
        dn = new DnHolder();
        dn.conertJsonToDn(dnHeaderJson, dnDetailJson);
        if (!dn.isDisputeValueChanged())
        {
            errorMsg = getText("B2BPC2239");
            this.addActionError(errorMsg);
            return;
        }
        
        List<DnDetailExHolder> dnDetailList = dn.getDnDetail();
        List<Integer> noRemarksLines = new ArrayList<Integer>();
        
        for (DnDetailExHolder detail : dnDetailList)
        {
            
            try
            {
                detail.trimAllString();
                detail.setAllEmptyStringToNull();
                
            }
            catch (Exception e)
            {
                ErrorHelper.getInstance().logTicketNo(log, e);
                errorMsg = "exception";
                this.addActionError(errorMsg);
                return;
            }
            
            if ((detail.isDisputePriceChanged() && detail.getDisputePriceRemarks() == null)
                    ||(detail.isDisputeQtyChanged() && detail.getDisputeQtyRemarks() == null))
            {
                noRemarksLines.add(detail.getLineSeqNo());
            }
        }
        
        if (!noRemarksLines.isEmpty())
        {
            errorMsg = getText("B2BPC2240", new String[]{noRemarksLines.toString()});
            this.addActionError(errorMsg);
            return;
        }
    }
    
    
    public String saveDispute()
    {
        try
        {
            dn.getDnHeader().setDispute(true);
            dn.getDnHeader().setDisputeDate(new Date());
            dn.getDnHeader().setDisputeBy(getLoginIdOfCurrentUser());
            dn.getDnHeader().setPriceDisputed(dn.isDisputePriceChanged());
            dn.getDnHeader().setQtyDisputed(dn.isDisputeQtyChanged());
            
            DnHolder oldDn = dnService.selectDnByKey(dn.getDnHeader().getDnOid());
            dnService.auditUpdateByPrimaryKey(this.getCommonParameter(), oldDn, dn);
            
            for (DnDetailExHolder detail : dn.getDnDetail())
            {
                if (!detail.isDisputePriceChanged())
                {
                  //update the doc sub class audit_finished.
                    ItemHolder item = itemService.selectItemByBuyerOidAndBuyerItemCode(dn.getDnHeader().getBuyerOid(), detail.getBuyerItemCode());
                    if (item != null && item.getSubclassCode() != null && !item.getSubclassCode().isEmpty())
                    {
                        DocSubclassHolder docSubclass = new DocSubclassHolder();
                        docSubclass.setDocOid(dn.getDnHeader().getDnOid());
                        docSubclass.setClassCode(item.getClassCode());
                        docSubclass.setSubclassCode(item.getSubclassCode());
                        docSubclass.setAuditFinished(true);
                        
                        docSubclassMapper.updateByPrimaryKeySelective(docSubclass);
                    }
                }
            }
         
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
            errorMsg = "exception";
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // audit debit note
    // *****************************************************
    
    public void validateSaveAudit()
    {
        dn = new DnHolder();
        dn.conertJsonToDn(dnHeaderJson, dnDetailJson);
        
        List<DnDetailExHolder> dnDetailList = dn.getDnDetail();
        List<Integer> noRemarksLines = new ArrayList<Integer>();
        
        for (DnDetailExHolder detail : dnDetailList)
        {
            try
            {
                detail.trimAllString();
                detail.setAllEmptyStringToNull();
            }
            catch (Exception e)
            {
                ErrorHelper.getInstance().logTicketNo(log, e);
                errorMsg = "exception";
                this.addActionError(errorMsg);
                return;
            }
            if ((detail.getPriceStatus().equals(DnPriceStatus.REJECTED) && detail.getPriceStatusActionRemarks() == null) 
                    || (detail.getQtyStatus().equals(DnQtyStatus.REJECTED) && detail.getQtyStatusActionRemarks() == null))
            {
                noRemarksLines.add(detail.getLineSeqNo());
            }
        }
        
        if (!noRemarksLines.isEmpty())
        {
            errorMsg = getText("B2BPC2241", new String[]{noRemarksLines.toString()});
            this.addActionError(errorMsg);
            return;
        }
    }
    
    
    public String saveAudit()
    {
        try
        {
            List<DnDetailExHolder> detailList = dn.getDnDetail();
            
            for (DnDetailExHolder detail : detailList)
            {
                if (!detail.getPriceStatus().equals(DnPriceStatus.PENDING) && detail.getPriceStatusActionBy() == null)
                {
                    detail.setPriceStatusActionBy(this.getLoginIdOfCurrentUser());
                    detail.setPriceStatusActionDate(new Date());
                }
                else if (!detail.getQtyStatus().equals(DnQtyStatus.PENDING) && detail.getQtyStatusActionBy() == null)
                {
                    detail.setQtyStatusActionBy(this.getLoginIdOfCurrentUser());
                    detail.setQtyStatusActionDate(new Date());
                }
                
                if (detail.getPriceStatus().equals(DnPriceStatus.REJECTED))
                {
                    detail.setConfirmPrice(detail.getUnitCost());
                }
                else if (detail.getPriceStatus().equals(DnPriceStatus.ACCEPTED))
                {
                    detail.setConfirmPrice(detail.getDisputePrice());
                }
                
                if (detail.getQtyStatus().equals(DnQtyStatus.REJECTED))
                {
                    detail.setConfirmQty(detail.getDebitQty());
                }
                else if (detail.getQtyStatus().equals(DnQtyStatus.ACCEPTED))
                {
                    detail.setConfirmQty(detail.getDisputeQty());
                }
                
                if (!DnPriceStatus.PENDING.equals(detail.getPriceStatus()))
                {
                    //update the doc sub class audit_finished.
                    ItemHolder item = itemService.selectItemByBuyerOidAndBuyerItemCode(dn.getDnHeader().getBuyerOid(), detail.getBuyerItemCode());
                    if (item != null && item.getSubclassCode() != null && !item.getSubclassCode().isEmpty())
                    {
                        DocSubclassHolder docSubclass = new DocSubclassHolder();
                        docSubclass.setDocOid(dn.getDnHeader().getDnOid());
                        docSubclass.setClassCode(item.getClassCode());
                        docSubclass.setSubclassCode(item.getSubclassCode());
                        docSubclass.setAuditFinished(true);
                        
                        docSubclassMapper.updateByPrimaryKeySelective(docSubclass);
                    }
                }
            }
            
            dn.getDnHeader().setPriceStatus(dn.computePriceStatus());
            dn.getDnHeader().setQtyStatus(dn.computeQtyStatus());
            dn.getDnHeader().setBuyerStatus(dn.computeBuyerStatus());
            DnHolder oldDn = dnService.selectDnByKey(dn.getDnHeader().getDnOid());
            dnService.auditUpdateByPrimaryKey(this.getCommonParameter(), oldDn, dn);
            
            boolean isAutoCloseAcceptedDnRecord = businessRuleService.isAutoCloseAcceptedDnRecord(dn.getDnHeader().getBuyerOid());
            if (isAutoCloseAcceptedDnRecord && dn.getDnHeader().getBuyerStatus().equals(DnBuyerStatus.ACCEPTED))
            {
                dn.getDnHeader().setClosed(true);
                dn.getDnHeader().setClosedBy(CoreCommonConstants.SYSTEM);
                dn.getDnHeader().setClosedDate(new Date());
                dnService.saveClose(this.getCommonParameter(), dn, true);
            }
         
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
            errorMsg = "exception";
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // close debit note
    // *****************************************************
    public void validateSaveClose()
    {
        try
        {
            dn = new DnHolder();
            dn.conertJsonToDn(dnHeaderJson, dnDetailJson);
            
            DnHeaderHolder oldDnHeader = dnHeaderService.selectDnHeaderByKey(dn.getDnHeader().getDnOid());
            
            if (oldDnHeader.getClosed() != null && oldDnHeader.getClosed())
            {
                errorMsg = getText("B2BPC2234", new String[]{oldDnHeader.getDnNo()});
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
            errorMsg = "exception";
        }
        
        if (errorMsg != null)
        {
            this.addActionError(errorMsg);
        }
    }
    
    
    public String saveClose()
    {
        try
        {
            DnHeaderHolder dnHeader = dn.getDnHeader();
            dnHeader.setClosed(true);
            dnHeader.setClosedBy(getLoginIdOfCurrentUser());
            dnHeader.setClosedDate(new Date());
            
            boolean generateNew = dn.getDnHeader().getBuyerStatus().equals(DnBuyerStatus.ACCEPTED) || dn.isConfirmValueChanged();
            if (generateNew)
            {
                BigDecimal totalCost = BigDecimal.ZERO;
                
                for (DnDetailHolder detail : dn.getDnDetail())
                {
                    detail.setItemCost(detail.getConfirmQty().multiply(detail.getConfirmPrice()));
                    totalCost = totalCost.add(detail.getItemCost());
                }
                if (totalCost.compareTo(BigDecimal.valueOf(99999999999.9999)) > 0)
                {
                    errorMsg = "Confirm total cost is more than 99999999999.9999, can not save";
                    return SUCCESS;
                }
            }
            dnService.saveClose(this.getCommonParameter(), dn, generateNew);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
            errorMsg = "exception";
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
                param = (DnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_DN);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_DN, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<DnSummaryHolder> dnList = dnHeaderService.selectAllRecordToExport(param);
                
                if (dnList != null && !dnList.isEmpty())
                {
                    for (DnSummaryHolder dn : dnList)
                    {
                        invOids.add(dn.getDocOid());
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(dn.getDnOid());
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
            
            this.setRptResult(new ByteArrayInputStream(dnService.exportExcel(invOids, storeFlag)));
            rptFileName = "DebitNoteReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
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
            DnSummaryHolder searchParam = this.initSearchParameter();
            int count = dnHeaderService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            
            List<MsgTransactionsExHolder> summarys = dnHeaderService.getListOfSummary(searchParam);
            if (summarys==null || summarys.isEmpty())
            {
                return INPUT;
            }
            
            List<DnSummaryHolder> dnSummarys = new ArrayList<DnSummaryHolder>();
            
            for (MsgTransactionsExHolder msgTrans : summarys)
            {
                dnSummarys.add((DnSummaryHolder)msgTrans);
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(dnService.exportSummaryExcel(dnSummarys, storeFlag)));
            rptFileName = "DN_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
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
    private void initSearchCondition() throws Exception
    {
        dnTypes = DnType.toMapValue(this);
        readStatus = ReadStatus.toMapValue(this);
        dnStatus = DnStatus.toMapValue(this);
        buyerStatus = DnBuyerStatus.toMapValue(this);
        qtyStatus = DnQtyStatus.toMapValue(this);
        priceStatus = DnPriceStatus.toMapValue(this);
        
        if(null == param)
        {
            param = new DnSummaryHolder();
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    param.setDnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    param.setDnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                param.setDnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                            CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    param.setDnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    param.setDnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                param.setDnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
        }
    }
    
    
    private ReportEngineParameter<DnHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        DnHeaderHolder header = dnHeaderService.selectDnHeaderByKey(msg.getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
        List<DnDetailExHolder> details = dnDetailService
            .selectDnDetailByKey(msg.getDocOid());
        List<DnHeaderExtendedHolder> headerExtendeds = dnHeaderExtendedService
            .selectHeaderExtendedByKey(msg.getDocOid());
        
        List<DnDetailExtendedHolder> detailExtendeds = dnDetailExtendedService.selectDetailExtendedByKey(msg.getDocOid());

        ReportEngineParameter<DnHolder> reportEngineParameter = new ReportEngineParameter<DnHolder>();

        DnHolder data = new DnHolder();

        data.setDnHeader(header);
        data.setDnDetail(details);
        data.setHeaderExtended(headerExtendeds);
        data.setDetailExtended(detailExtendeds);

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    private DefaultDnReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.DN.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultDnReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.DN
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultDnReportEngine.class);
    }
    
    private DnSummaryHolder initSortField(DnSummaryHolder param)
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
    
    
    private DnSummaryHolder initSearchParameter() throws Exception
    {
        DnSummaryHolder searchParam = (DnSummaryHolder)getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_DN);

        if(searchParam == null)
        {
            searchParam = new DnSummaryHolder();

            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if(null == documentWindow.getNumValue()
                    || documentWindow.getNumValue() > 7
                    || documentWindow.getNumValue() < 1)
                {
                    searchParam.setDnDateFrom(DateUtil.getInstance()
                        .getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setDnDateFrom(DateUtil.getInstance()
                        .getFirstTimeOfDay(
                            DateUtil.getInstance().dateAfterDays(new Date(),
                                -documentWindow.getNumValue() + 1)));
                }
                searchParam.setDnDateTo(DateUtil.getInstance()
                    .getLastTimeOfDay(new Date()));
            }
            if(getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if(null == documentWindow.getNumValue()
                    || documentWindow.getNumValue() > 14
                    || documentWindow.getNumValue() < 1)
                {
                    searchParam.setDnDateFrom(DateUtil.getInstance()
                        .getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setDnDateFrom(DateUtil.getInstance()
                        .getFirstTimeOfDay(
                            DateUtil.getInstance().dateAfterDays(new Date(),
                                -documentWindow.getNumValue() + 1)));
                }
                searchParam.setDnDateTo(DateUtil.getInstance()
                    .getLastTimeOfDay(new Date()));
            }
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_DN, searchParam);
        }
        if(this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(3)) == 0
            || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(5)) == 0)
        {
            searchParam.setSupplierFlag(true);
        }

        searchParam.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
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


    public DnSummaryHolder getParam()
    {
        return param;
    }


    public void setParam(DnSummaryHolder param)
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

    public Map<String, String> getDnTypes()
    {
        return dnTypes;
    }

    public void setDnTypes(Map<String, String> dnTypes)
    {
        this.dnTypes = dnTypes;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public DnHeaderExHolder getDnHeaderEx()
    {
        return dnHeaderEx;
    }

    public void setDnHeaderEx(DnHeaderExHolder dnHeaderEx)
    {
        this.dnHeaderEx = dnHeaderEx;
    }

    public List<DnDetailExHolder> getDnDetailExs()
    {
        return dnDetailExs;
    }

    public void setDnDetailExs(List<DnDetailExHolder> dnDetailExs)
    {
        this.dnDetailExs = dnDetailExs;
    }


    public TradingPartnerExHolder getTradingPartner()
    {
        return tradingPartner;
    }

    
    public void setTradingPartner(TradingPartnerExHolder tradingPartner)
    {
        this.tradingPartner = tradingPartner;
    }

    
    public void setDocOid(BigDecimal docOid)
    {
        this.docOid = docOid;
    }


    public String getErrorMsg()
    {
        return errorMsg;
    }


    public String getDnHeaderJson()
    {
        return dnHeaderJson;
    }


    public void setDnHeaderJson(String dnHeaderJson)
    {
        this.dnHeaderJson = dnHeaderJson;
    }


    public String getDnDetailJson()
    {
        return dnDetailJson;
    }


    public void setDnDetailJson(String dnDetailJson)
    {
        this.dnDetailJson = dnDetailJson;
    }


    public DnHolder getDn()
    {
        return dn;
    }


    public void setDn(DnHolder dn)
    {
        this.dn = dn;
    }


    public Map<String, String> getBuyerStatus()
    {
        return buyerStatus;
    }


    public void setBuyerStatus(Map<String, String> buyerStatus)
    {
        this.buyerStatus = buyerStatus;
    }


    public Map<String, String> getPriceStatus()
    {
        return priceStatus;
    }


    public void setPriceStatus(Map<String, String> priceStatus)
    {
        this.priceStatus = priceStatus;
    }


    public Map<String, String> getQtyStatus()
    {
        return qtyStatus;
    }


    public void setQtyStatus(Map<String, String> qtyStatus)
    {
        this.qtyStatus = qtyStatus;
    }


    public Map<String, String> getDnStatus()
    {
        return dnStatus;
    }

    public String getActionType()
    {
        return actionType;
    }

    public void setActionType(String actionType)
    {
        this.actionType = actionType;
    }

    public String getDisplayType()
    {
        return displayType;
    }

    public void setDisplayType(String displayType)
    {
        this.displayType = displayType;
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
