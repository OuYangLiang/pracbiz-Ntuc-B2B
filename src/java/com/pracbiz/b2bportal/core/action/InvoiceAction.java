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
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.InvType;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingBuyerStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvHeaderExHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.report.DefaultInvReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.InvDetailExtendedService;
import com.pracbiz.b2bportal.core.service.InvDetailService;
import com.pracbiz.b2bportal.core.service.InvHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PoDetailService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingDetailService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.DateJsonValueProcessor;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class InvoiceAction extends TransactionalDocsBaseAction implements
    ApplicationContextAware, CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(InvoiceAction.class);
    private static final long serialVersionUID = -6881976946188481680L;
    public final static byte[] lock = new byte[0];
    
    private static final String SESSION_KEY_SEARCH_PARAMETER_INV = "SEARCH_PARAMETER_INV";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.eInvoice.selectedOids";
    private static final String B2BPC1202 = "B2BPC1202";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME = "&prime;";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_CHARACTER = "'";
    private static final String RIGHT_DOUBLE_QUOTATION_MARK = "\"";
    public static final String BUSINESS_RULE_FUNC_ID_SORPO = "SorPO";
    public static final String BUSINESS_RULE_FUNC_ID_CONPO = "ConPO";
    public static final String BUSINESS_RULE_FUNC_PO_CONVERT_INV = "PoConvertInv";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    private static final String VLD_PTN_KEY_INVOICE_NO = "INV_NO";
    private static final String VLD_PTN_KEY_DELIVERY_NO = "DELIVERY_NO";
    private static final String B2BPC1612 = "B2BPC1612";
    private static final String B2BPC1613 = "B2BPC1613";
    private static final String B2BPC1615 = "B2BPC1615";
    private static final String B2BPC1616 = "B2BPC1616";
    private static final String B2BPC1618 = "B2BPC1618";
    private static final String B2BPC1619 = "B2BPC1619";
    private static final String B2BPC1636 = "B2BPC1636";
    private static final String B2BPC1663 = "B2BPC1663";
    private static final String B2BPC1664 = "B2BPC1664";
    private static final String OLD_INV_OID_TO_GENERATE = "oldInvOidToGenerate";
    public static final String BUSINESS_RULE_FUNC_MATCHING = "Matching";
    public static final String BUSINESS_RULE_FUNC_ID_PO_INV_GRN_DN = "PoInvGrnDn";
    public static final String BUSINESS_RULE_ID = "EnableSupplierToDispute";
    public static final String SESSION_SUPPLIER_DISPUTE = "SUPPLIER_DISPUTE";

    @Autowired transient private InvHeaderService invHeaderService;
    @Autowired transient private InvDetailService invDetailService;
    @Autowired transient private InvHeaderExtendedService invHeaderExtendedService;
    @Autowired transient private InvDetailExtendedService invDetailExtendedService;
    @Autowired transient private InvoiceService invoiceService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private BusinessRuleService businessRuleService;
    @Autowired transient private PoHeaderService poHeaderService;
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired transient private PoInvGrnDnMatchingDetailService poInvGrnDnMatchingDetailService;
    @Autowired transient private PoService poService;
    @Autowired transient private TermConditionService termConditionService;
    @Autowired transient private OidService oidService;
    @Autowired transient private ValidationConfigHelper validationConfig;
    @Autowired transient private PoDetailService poDetailService;
    @Autowired transient private SupplierMsgSettingService supplierMsgSettingService;
    @Autowired @Qualifier("canonicalInvDocFileHandler") transient private DocFileHandler<?, InvHolder> canonicalInvDocFileHandler;
    

    private InvSummaryHolder param;
    private String rptFileName;
    private Map<String, String> invTypes;
    private Map<String, String> invStatus;
    transient private ApplicationContext ctx;
    transient private InputStream rptResult;
    private String invHeaderJson;
    private String invDetailJson;
    private String businessRulesJson;
    private String errorMsg;
    private InvHolder invoice;
    private String success;
    private Map<String, String> matchingStatus;
    private boolean selectAll;
    private boolean ggDisableInvoicePaymentInstructions;
    

    public InvoiceAction()
    {
        this.initMsg();
        this.setPageId(PageId.P004.name());
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_INV);

        if (param == null)
        {
            param = (InvSummaryHolder)getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_INV);
        }
        try
        {
            if (null == (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE))
            {
                if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()))
                {
                    boolean enableSupplierToDispute = businessRuleService.isMatchingEnableSupplierToDispute(this.getProfileOfCurrentUser().getBuyerOid());
                    if (enableSupplierToDispute)
                    {
                        this.getSession().put(SESSION_SUPPLIER_DISPUTE, "yes");
                    }
                }
                else if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser())
                        || BigDecimal.valueOf(5).equals(this.getUserTypeOfCurrentUser())
                        || BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())
                        || BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
                {
                    this.getSession().put(SESSION_SUPPLIER_DISPUTE, "yes");
                }
            }
            this.initSearchCondition();
            this.initSearchCriteria();
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        this.getSession().put(SESSION_KEY_SEARCH_PARAMETER_INV, param);
        return SUCCESS;
    }

    public String search()
    {
        if(null == param)
        {
            param = new InvSummaryHolder();
        }

        try
        {
            param.setInvDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getInvDateFrom()));
            param.setInvDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getInvDateTo()));
            param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getPoDateFrom()));
            param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getPoDateTo()));
            param.setSentDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getSentDateFrom()));
            param.setSentDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getSentDateTo()));
            param.setReceivedDateFrom(DateUtil.getInstance().getFirstTimeOfDay(
                param.getReceivedDateFrom()));
            param.setReceivedDateTo(DateUtil.getInstance().getLastTimeOfDay(
                param.getReceivedDateTo()));
            
            List<PoInvGrnDnMatchingStatus> statusList = new ArrayList<PoInvGrnDnMatchingStatus>();
            if (param.isStatusPending())
            {
                statusList.add(PoInvGrnDnMatchingStatus.PENDING);
            }
            if (param.isStatusMatched())
            {
                statusList.add(PoInvGrnDnMatchingStatus.MATCHED);
            }
            if (param.isStatusMatchedByDn())
            {
                statusList.add(PoInvGrnDnMatchingStatus.MATCHED_BY_DN);
            }
            if (param.isStatusAmountUnmatched())
            {
                statusList.add(PoInvGrnDnMatchingStatus.AMOUNT_UNMATCHED);
            }
            if (param.isStatusUnmatched())
            {
                statusList.add(PoInvGrnDnMatchingStatus.UNMATCHED);
            }
            if (param.isStatusPriceUnmatched())
            {
                statusList.add(PoInvGrnDnMatchingStatus.PRICE_UNMATCHED);
            }
            if (param.isStatusQtyUnmatched())
            {
                statusList.add(PoInvGrnDnMatchingStatus.QTY_UNMATCHED);
            }
            if (param.isStatusOutdated())
            {
                statusList.add(PoInvGrnDnMatchingStatus.OUTDATED);
            }
            if (param.isStatusInsInv())
            {
                statusList.add(PoInvGrnDnMatchingStatus.INSUFFICIENT_INV);
            }
            
            param.setStatusList(statusList.isEmpty() ? null : statusList);
            
            param = initSortField(param);
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_INV, param);

        return SUCCESS;
    }

    public String data()
    {
        try
        {
            param = (InvSummaryHolder)getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_INV);

            if(param == null)
            {
                this.initSearchCondition();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_INV, param);
            }
            param = initSortField(param);

            initCurrentUserSearchParam(param);
            
            param.trimAllString();
            param.setAllEmptyStringToNull();

            this.obtainListRecordsOfPagination(invHeaderService, param, MODULE_KEY_INV);
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    // *****************************************************
    // sent inovice
    // *****************************************************
    public String sent()
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
            BigDecimal invOid = null;

            
            
            for(int i = 0; i < parts.length; i++)
            {
                invOid = new BigDecimal(parts[i]);
                
                InvHeaderHolder invHeader = invHeaderService
                .selectInvHeaderByKey(invOid);
                
                List<InvDetailHolder> invDetail = invDetailService.selectInvDetailByKey(invOid);
                List<InvDetailExtendedHolder> invDex = invDetailExtendedService.selectDetailExtendedByKey(invOid);
                List<InvHeaderExtendedHolder> invHex = invHeaderExtendedService.selectHeaderExtendedByKey(invOid);

                //sent invoice with ctrl status is NEW.
                if(invHeader.getInvStatus().equals(InvStatus.NEW))
                {
                    invHeader.setInvStatus(InvStatus.SUBMIT);
    
                    MsgTransactionsHolder msg = invHeader.toMsgTransactions();
                    CommonParameterHolder cp = this.getCommonParameter();
    
                    InvHolder invoice = new InvHolder();
                    invoice.setHeader(invHeader);
                    invoice.setHeaderExtendeds(invHex);
                    invoice.setDetails(invDetail);
                    invoice.setDetailExtendeds(invDex);
    
                    SupplierHolder supplier = supplierService
                        .selectSupplierByKey(invoice.getHeader().getSupplierOid());
                    invoiceService.sentInvoice(cp, invoice, msg, supplier);
                    init();
                    search();
                }
                else
                {
                    this.addActionError(getText(B2BPC1202, new String[]{invHeader.getInvStatus().name()}));
                    init();
                    search();
                    return INPUT;
                }
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
    // print pdf
    // *****************************************************
    public String printPdf()
    {
        try
        {
            String[] files = null;
            if (selectAll)
            {
                param = (InvSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_INV);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_INV, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<InvSummaryHolder> invList = invHeaderService.selectAllRecordToExport(param);
                
                if (invList != null && !invList.isEmpty())
                {
                    files = new String[invList.size()];
                    for (int i = 0; i < invList.size(); i++)
                    {
                        InvSummaryHolder inv = invList.get(i);
                        
                        MsgTransactionsHolder msg = msgTransactionsService
                            .selectByKey(inv.getDocOid());

                        SupplierHolder supplier = supplierService
                            .selectSupplierByKey(msg.getSupplierOid());
                        
                        rptFileName = msg.getReportFilename();

                        File file = new File(mboxUtil.getFolderInSupplierDocOutPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())), msg.getReportFilename());

                        if(!file.exists())
                        {
                            File docPath = new File(mboxUtil.getFolderInSupplierDocOutPath(
                                supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                    msg.getCreateDate())));
                            
                            if (!docPath.isDirectory())
                            {
                                FileUtil.getInstance().createDir(docPath);
                            }
                            
                            InvHeaderHolder invHeader = invHeaderService.selectInvHeaderByKey(inv.getDocOid());
                            BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                    MsgType.INV.name(), invHeader.getInvType().name());
                            DefaultInvReportEngine invReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                                .getBuyerCode());

                            ReportEngineParameter<InvHolder> reportParameter = retrieveParameter(
                                msg, supplier);

                            byte[] datas = invReportEngine
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
                    
                    if ("PO".equalsIgnoreCase(docType))
                    {
                        InvHeaderHolder invHeader = invHeaderService.selectInvHeaderByKey(docOid);
                        PoHeaderHolder po = poHeaderService.selectPoHeaderByKey(invHeader.getPoOid());
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
                        
                        InvHeaderHolder invHeader = invHeaderService.selectInvHeaderByKey(docOid);
                        BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                MsgType.INV.name(), invHeader.getInvType().name());
                        DefaultInvReportEngine invReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                            .getBuyerCode());

                        ReportEngineParameter<InvHolder> reportParameter = retrieveParameter(
                            msg, supplier);

                        byte[] datas = invReportEngine
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
                rptFileName = "invReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
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
    // edit invoice
    // *****************************************************
    public String checkInvoiceStatus()
    {
        try
        {
            InvHeaderHolder invHeader = invHeaderService.selectInvHeaderByKey(param.getInvOid());
            if (invHeader == null)
            {
                this.setErrorMsg(this.getText("B2BPC1203"));
            }
            else if (!InvStatus.NEW.equals(invHeader.getInvStatus()))
            {
                this.setErrorMsg(this.getText("B2BPC1204"));
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        return SUCCESS;
    }

    
    public String initEdit()
    {
        try
        {
            InvHeaderHolder invHeader = invHeaderService.selectInvHeaderByKey(param.getInvOid());
            return invHeader.getInvType().name();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
    }

    
    //************
    //init SOR inv
    //************
    public String initEdtSorInv()
    {
        try
        {
            InvHeaderHolder header = invHeaderService
                    .selectInvHeaderByKey(param.getInvOid());
            SupplierHolder supplier = supplierService.selectSupplierByKey(header.getSupplierOid());
            header.setSupplierBizRegNo(supplier.getRegNo());
            header.setSupplierVatRegNo(supplier.getGstRegNo());
            header.setInvAmountNoVat(BigDecimalUtil.getInstance().format(header.getInvAmountNoVat().subtract(header.getAdditionalDiscountAmount()), 2));
            PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(header.getPoOid());
            InvHeaderExHolder headerEx = new InvHeaderExHolder();
            headerEx.setPoType(poHeader.getPoType());
            headerEx.setPoStatus(poHeader.getPoStatus());
            BeanUtils.copyProperties(header, headerEx);
            headerEx.setAutoInvNumber(supplier.getAutoInvNumber());
            headerEx.setInvTotalBeforeAdditional(header.getInvAmountNoVat().add(header.getAdditionalDiscountAmount()));
            headerEx.setTotalPay(header.getInvAmountWithVat().subtract(header.getCashDiscountAmount()));
            List<InvDetailHolder> details = invDetailService
                    .selectInvDetailByKey(param.getInvOid());
            
           
            invoice = new InvHolder();
            invoice.setHeader(headerEx);
            invoice.setDetails(details);
            this.invoiceFormat(invoice);
            JsonConfig jsonConfig = new JsonConfig();      
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));

            JSONObject invHeader = JSONObject.fromObject(headerEx, jsonConfig);
            JSONArray invDetails = JSONArray.fromObject(details, jsonConfig);
            invHeaderJson = this.replaceSpecialCharactersForJson(invHeader
                    .toString());
            invDetailJson = this.replaceSpecialCharactersForJson(invDetails
                    .toString());
            List<BusinessRuleHolder> businessRules = businessRuleService
                    .selectRulesByBuyerOidAndFuncGroupAndFuncId(header
                            .getBuyerOid(), BUSINESS_RULE_FUNC_PO_CONVERT_INV,
                            BUSINESS_RULE_FUNC_ID_SORPO);
            ggDisableInvoicePaymentInstructions = businessRuleService.isDisablePaymentInstructions(poHeader.getBuyerOid());

            JSONArray rules = JSONArray.fromObject(businessRules);
            businessRulesJson = this.replaceSpecialCharactersForJson(rules
                    .toString());
        }
        catch (Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        return SUCCESS;
    }
    
    
    //************
    //init CN inv
    //************
    public String initEdtCnInv()
    {
        try
        {
            BigDecimalUtil format = BigDecimalUtil.getInstance();
            InvHeaderHolder header = invHeaderService
                    .selectInvHeaderByKey(param.getInvOid());
            SupplierHolder supplier = supplierService.selectSupplierByKey(header.getSupplierOid());
            header.setSupplierBizRegNo(supplier.getRegNo());
            header.setSupplierVatRegNo(supplier.getGstRegNo());
            header.setInvAmountNoVat(format.format(header.getInvAmountNoVat().subtract(header.getAdditionalDiscountAmount()), 2));
            PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(header.getPoOid());
            InvHeaderExHolder headerEx = new InvHeaderExHolder();
            headerEx.setPoType(poHeader.getPoType());
            headerEx.setPoStatus(poHeader.getPoStatus());
            BeanUtils.copyProperties(header, headerEx);
            headerEx.setAutoInvNumber(supplier.getAutoInvNumber());
            headerEx.setInvTotalBeforeAdditional(format.format(header.getInvAmountNoVat().add(header.getAdditionalDiscountAmount()), 2));
            headerEx.setTotalPay(format.format(header.getInvAmountWithVat().subtract(header.getCashDiscountAmount()), 2));
            List<InvDetailHolder> details = invDetailService
                    .selectInvDetailByKey(param.getInvOid());
            List<PoDetailHolder> poDetails = poDetailService.selectPoDetailsByPoOid(header.getPoOid());
                
            Map<Integer, PoDetailHolder> detailsMap = new HashMap<Integer, PoDetailHolder>();
            for (PoDetailHolder detail : poDetails)
            {
               detailsMap.put(detail.getLineSeqNo(), detail);
            }
            
            for (InvDetailHolder invDetail : details)
            {
                Integer key = invDetail.getLineSeqNo();
                if (detailsMap.containsKey(key))
                {
                    invDetail.setTotalPoSell(format.format(detailsMap.get(key)
                        .getItemRetailAmount() == null ? BigDecimal.ZERO : detailsMap.get(key).getItemRetailAmount(), 2));
                    invDetail.setTotalCustomerDisc(format.format(detailsMap.get(key)
                        .getRetailDiscountAmount() == null ? BigDecimal.ZERO : detailsMap.get(key).getRetailDiscountAmount(), 2));
                }
                BigDecimal conTotalCost = invDetail.getNetAmount().add(invDetail.getItemSharedCost());
                invDetail.setConTotalCost(format.format(conTotalCost, 2));
            }
            
            invoice = new InvHolder();
            invoice.setHeader(headerEx);
            invoice.setDetails(details);
            this.invoiceFormat(invoice);
            JsonConfig jsonConfig = new JsonConfig();      
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));

            JSONObject invHeader = JSONObject.fromObject(headerEx, jsonConfig);
            JSONArray invDetails = JSONArray.fromObject(details, jsonConfig);
            invHeaderJson = this.replaceSpecialCharactersForJson(invHeader
                    .toString());
            invDetailJson = this.replaceSpecialCharactersForJson(invDetails
                    .toString());
            List<BusinessRuleHolder> businessRules = businessRuleService
                    .selectRulesByBuyerOidAndFuncGroupAndFuncId(header
                            .getBuyerOid(), BUSINESS_RULE_FUNC_PO_CONVERT_INV,
                            BUSINESS_RULE_FUNC_ID_CONPO);
            ggDisableInvoicePaymentInstructions = businessRuleService.isDisablePaymentInstructions(poHeader.getBuyerOid());

            JSONArray rules = JSONArray.fromObject(businessRules);
            businessRulesJson = this.replaceSpecialCharactersForJson(rules
                    .toString());
        }
        catch (Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        return SUCCESS;
    }
    
    
    public void validateSave()
    {
        try
        {
            this.validateSaveInvoice();
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
    }
    
    
    public String save()
    {
        try
        {
            invoice.getHeader().setInvAmountNoVat(BigDecimalUtil.getInstance().format(
                invoice.getHeader().getInvAmountNoVat().add(invoice.getHeader().getAdditionalDiscountAmount()), 2));
            invoiceService.editInvoice(this.getCommonParameter(), invoice);
            this.success = SUCCESS;
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        
        return SUCCESS;
    }
    
    
    private void updateReadStatus(MsgTransactionsHolder msg) throws Exception
    {
        if ((BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
            || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()))
            && !ReadStatus.READ.equals(msg.getReadStatus()))
        {
            msg.setReadStatus(ReadStatus.READ);
            msg.setReadDate(new Date());
            msgTransactionsService.updateByPrimaryKeySelective(null, msg);
        }
    }
    
    
    public void validateSaveAndSent()
    {
        try
        {
            this.validateSaveInvoice();
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
    }   
    
    
    public String saveAndSent()
    {
        try
        {
            invoice.getHeader().setInvAmountNoVat(BigDecimalUtil.getInstance().format(
                invoice.getHeader().getInvAmountNoVat().add(invoice.getHeader().getAdditionalDiscountAmount()), 2));
            invoiceService.editAndSendInvoice(this.getCommonParameter(), invoice);
            this.success = SUCCESS;
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        
        return SUCCESS;
    }
    
    // *****************************************************
    // generate new invoice for void invoice
    // *****************************************************
    
    public String checkMatchingRecordExist()
    {
        try
        {
            PoInvGrnDnMatchingHolder matching = poInvGrnDnMatchingService.selectEffectiveRecordByInvOid(param.getInvOid());
            if (matching == null)
            {
                this.errorMsg = "matchingNotExist";
                return SUCCESS;
            }
            else
            {
                getSession().put(OLD_INV_OID_TO_GENERATE, param.getInvOid());
            }
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String initGenerateInv()
    {
        try
        {
            BigDecimal invOid = (BigDecimal)this.getSession().get(OLD_INV_OID_TO_GENERATE);
            InvHolder oldInv = invoiceService.selectInvoiceByKey(invOid);
            PoInvGrnDnMatchingHolder matching = poInvGrnDnMatchingService.selectEffectiveRecordByInvOid(invOid);
            List<PoInvGrnDnMatchingDetailExHolder> matchingDetailList = poInvGrnDnMatchingDetailService.selectByMatchingOid(matching.getMatchingOid());
            PoHolder po = poService.selectPoByKey(matching.getPoOid());
            SupplierHolder supplier = supplierService.selectSupplierByKey(matching.getSupplierOid());
            
            TradingPartnerHolder tradingPartner = tradingPartnerService
                    .selectByBuyerAndBuyerGivenSupplierCode(po.getPoHeader()
                            .getBuyerOid(), po.getPoHeader().getSupplierCode());
            TermConditionHolder term = null;
            if (tradingPartner.getTermConditionOid() == null)
            {
                term = termConditionService
                        .selectDefaultTermConditionBySupplierOid(matching
                                .getSupplierOid());
            }
            else
            {
                term = termConditionService
                        .selectTermConditionByKey(tradingPartner
                                .getTermConditionOid());
            }
                
            BigDecimal oid = oidService.getOid();
            
            invoice = po.toInvoice(supplier, this.getAutoInvNo(supplier), matching.getPoStoreCode(), term, po.getLocations().size(), oid);
            
            resetInvDetail(invoice.getDetails(), matchingDetailList, oldInv.getDetails());
            
            BigDecimal invAmountNoVat = BigDecimalUtil.getInstance().format(
                invoice.calculateInvAmountNoVat().subtract(invoice.getHeader().getAdditionalDiscountAmount()), 2);
            
            invoice.getHeader().setInvAmountNoVat(BigDecimalUtil.getInstance().format(invAmountNoVat, 2));
            BigDecimal vatAmount = invAmountNoVat.multiply(invoice.getHeader().getVatRate().divide(BigDecimal.valueOf(100)));
            invoice.getHeader().setVatAmount(BigDecimalUtil.getInstance().format(vatAmount, 2));
            invoice.getHeader().setInvAmountWithVat(BigDecimalUtil.getInstance().format(vatAmount.add(invAmountNoVat), 2));
            
            this.invoiceFormat(invoice);
            
            JsonConfig jsonConfig = new JsonConfig();      
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));

            JSONObject invHeader = JSONObject.fromObject(invoice.getHeader(), jsonConfig);
            JSONArray invDetails = JSONArray.fromObject(invoice.getDetails(), jsonConfig);
            invHeaderJson = this.replaceSpecialCharactersForJson(invHeader
                    .toString());
            invDetailJson = this.replaceSpecialCharactersForJson(invDetails
                    .toString());
            List<BusinessRuleHolder> businessRules = businessRuleService
                    .selectRulesByBuyerOidAndFuncGroupAndFuncId(invoice.getHeader()
                            .getBuyerOid(), BUSINESS_RULE_FUNC_PO_CONVERT_INV,
                            BUSINESS_RULE_FUNC_ID_SORPO);

            JSONArray rules = JSONArray.fromObject(businessRules);
            businessRulesJson = this.replaceSpecialCharactersForJson(rules.toString());
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public void validateSaveGenerateInv()
    {
        try
        {
            this.validateSaveGenerateInvoice();
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
    }
    
    
    public String saveGenerateInv()
    {
        try
        {
            MsgTransactionsHolder poMsg = msgTransactionsService.selectByKey(invoice.getHeader().getPoOid());
            this.updateReadStatus(poMsg);
            
            InvHeaderHolder invHeader = invoice.getHeader();
            PoHeaderHolder poHeader = poHeaderService
                .selectPoHeaderByKey(invHeader.getPoOid());
            invHeader.setInvStatus(InvStatus.NEW);
            
            MsgTransactionsHolder msg = invHeader.toMsgTransactions();
            
            CommonParameterHolder cp = this.getCommonParameter();
            BuyerHolder buyer = buyerService.selectBuyerByKey(invoice
                .getHeader().getBuyerOid());
            SupplierHolder supplier = supplierService
                .selectSupplierByKey(invoice.getHeader().getSupplierOid());
            
            invoice.getHeader().setInvAmountNoVat(BigDecimalUtil.getInstance().format(
                invoice.getHeader().getInvAmountNoVat().add(invoice.getHeader().getAdditionalDiscountAmount()), 2));
            invoiceService.createInvoice(cp, invoice, msg, poHeader, buyer,
                supplier);
            this.success = SUCCESS;
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        
        return SUCCESS;
    }
    
    
    public void validateSaveAndSentGenerateInv()
    {
        try
        {
            this.validateSaveGenerateInvoice();
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
    }
    
    
    public String saveAndSentGenerateInv()
    {
        try
        {
            MsgTransactionsHolder poMsg = msgTransactionsService.selectByKey(invoice.getHeader().getPoOid());
            this.updateReadStatus(poMsg);
            
            InvHeaderHolder invHeader = invoice.getHeader();
            PoHeaderHolder poHeader = poHeaderService
                .selectPoHeaderByKey(invHeader.getPoOid());
            invHeader.setInvStatus(InvStatus.SUBMIT);
            
            MsgTransactionsHolder msg = invHeader.toMsgTransactions();
            CommonParameterHolder cp = this.getCommonParameter();
            BuyerHolder buyer = buyerService.selectBuyerByKey(invoice
                .getHeader().getBuyerOid());
            SupplierHolder supplier = supplierService
                .selectSupplierByKey(invoice.getHeader().getSupplierOid());
            
            invoice.getHeader().setInvAmountNoVat(BigDecimalUtil.getInstance().format(
                invoice.getHeader().getInvAmountNoVat().add(invoice.getHeader().getAdditionalDiscountAmount()), 2));
            invoiceService.createAndSentInvoice(cp, invoice, msg, poHeader, buyer,
                supplier);
            this.success = SUCCESS;
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        
        return SUCCESS;
    }
    
    
    private String getAutoInvNo(SupplierHolder supplier) throws Exception
    {
        String invNo = null;
        if (!supplier.getAutoInvNumber())
        {
            return null;
        }
        
        do 
        {
            invNo = invoiceService.doRunningNumber(supplier);
        }
        while (invHeaderService.selectInvHeaderByInvNo(invNo) != null);
        return invNo;
    }
    
    
    private void validateSaveGenerateInvoice() throws Exception
    {
        boolean flag = this.hasErrors();
        invoice = new InvHolder();
        invoice.conertJsonToInvoice(invHeaderJson, invDetailJson);
        
        if (!flag)
        {
            InvHeaderHolder oldInv = invHeaderService.selectEffectiveInvHeaderByBuyerSupplierPoNoAndStore(invoice.getHeader().getBuyerCode(), invoice.getHeader().getSupplierCode(), invoice.getHeader().getPoNo(), invoice.getHeader().getShipToCode());
            if (oldInv != null)
            {
                this.errorMsg = getText("B2BPC1627", new String[]{invoice.getHeader().getShipToCode(), invoice.getHeader().getPoNo()});
                flag = true;
            }
        }
        
        PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(invoice.getHeader().getPoOid());
        if (!flag && poHeader == null)
        {
            this.errorMsg = getText("B2BPC1630");
            flag = true;
        }
        
        if (!flag && PoStatus.OUTDATED.equals(poHeader.getPoStatus()))
        {
            this.errorMsg = getText("B2BPC1631", new String[]{ poHeader.getPoNo() });
            flag = true;
        }
        
        if (!flag && PoStatus.CANCELLED.equals(poHeader.getPoStatus()))
        {
            this.errorMsg = getText("B2BPC1632", new String[]{ poHeader.getPoNo() });
            flag = true;
        }
        
        String invNo = invoice.getHeader().getInvNo();
        if (!flag && StringUtils.isBlank(invNo))
        {
            this.errorMsg = getText(B2BPC1612);
            flag = true;
        }
        
        if (!flag && invNo.trim().length() > 20)
        {
            this.errorMsg = getText(B2BPC1613,new String[]{"20"});
            flag = true;
        }
        
        if (!flag)
        {
            Pattern pattern = Pattern.compile(validationConfig.getCachePattern(VLD_PTN_KEY_INVOICE_NO));
            Matcher matcher = pattern.matcher(invNo);
            if (!matcher.matches())
            {
                this.errorMsg = getText(B2BPC1616);
                flag = true;
            }
        }
        
        DateUtil date = DateUtil.getInstance();
        Date poDate = invoice.getHeader().getPoDate();
        Date invDate = invoice.getHeader().getInvDate();
        Date delvDate = invoice.getHeader().getDeliveryDate();
        
        if (!flag && date.isAfterDays(poDate, invDate, 0))
        {
            this.errorMsg = getText(B2BPC1619);
            flag = true;
        }

        if (!flag && date.isAfterDays(invDate, new Date(), 0))
        {
            this.errorMsg = getText(B2BPC1618);
            flag = true;
        }
        
        if (!flag && !(poHeader.getPoSubType2() != null && poHeader.getPoSubType2().equalsIgnoreCase("ZQO")))
        {
            if (date.isAfterDays(poDate, delvDate, 0))
            {
                this.errorMsg = getText(B2BPC1636);
                flag = true;
            }
        }
        
        InvHeaderExHolder invHeader = (InvHeaderExHolder)invoice.getHeader();
        if (!flag && !invHeader.isAutoInvNumber())
        {
            InvHeaderHolder paramer = new InvHeaderHolder();
            paramer.setInvNo(invoice.getHeader().getInvNo());
            paramer.setSupplierOid(invHeader.getSupplierOid());
            
            List<InvHeaderHolder> headers = invHeaderService.select(paramer);

            if (headers != null && !headers.isEmpty())
            {
                this.errorMsg = getText(B2BPC1615);
                flag = true;
            }
        }
        
        if (flag)
        {
            this.addActionError(errorMsg);
        }
    }
    
    
    private void resetInvDetail(List<InvDetailHolder> invDetailList, 
        List<PoInvGrnDnMatchingDetailExHolder> matchingDetailList, List<InvDetailHolder> oldInvs)
    {
        for (InvDetailHolder invDetail : invDetailList)
        {
            for (PoInvGrnDnMatchingDetailHolder matchingDetail : matchingDetailList)
            {
                if (invDetail.getBuyerItemCode().equalsIgnoreCase(matchingDetail.getBuyerItemCode()))
                {
                    if (PoInvGrnDnMatchingPriceStatus.ACCEPTED.equals(matchingDetail.getPriceStatus()))
                    {
                        invDetail.setUnitPrice(matchingDetail.getInvPrice());
                    }
                    
                    if (PoInvGrnDnMatchingQtyStatus.ACCEPTED.equals(matchingDetail.getQtyStatus()))
                    {
                        invDetail.setInvQty(matchingDetail.getInvQty());
                    }
                    else
                    {
                        if (null == matchingDetail.getInvPrice() || BigDecimal.ZERO.compareTo(matchingDetail.getInvPrice()) == 0)
                        {
                            invDetail.setInvQty(matchingDetail.getInvQty());
                        }
                        else
                        {
                            invDetail.setInvQty(matchingDetail.getGrnQty());
                        }
                    }
                }
            }
            
            BigDecimal qty = invDetail.getInvQty() == null ? BigDecimal.ZERO : invDetail.getInvQty();
            BigDecimal price = invDetail.getUnitPrice() == null ? BigDecimal.ZERO : invDetail.getUnitPrice();
            invDetail.setItemAmount(qty.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP));
            for (InvDetailHolder oldDetail : oldInvs)
            {
                if (oldDetail.getBuyerItemCode().equalsIgnoreCase(invDetail.getBuyerItemCode()))
                {
                    invDetail.setDiscountAmount(invDetail.getItemAmount().multiply(oldDetail.getDiscountPercent()).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    invDetail.setNetAmount((invDetail.getItemAmount().subtract(invDetail.getDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    invDetail.setDiscountPercent(oldDetail.getDiscountPercent().setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
            
        }
    }

    
    public String voidInvoice()
    {
        try
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
                boolean flag = false;
                BigDecimal invOid = new BigDecimal(part);
                MsgTransactionsHolder msgTrans = msgTransactionsService.selectByKey(invOid);
                BuyerHolder buyer = buyerService.selectBuyerByKey(msgTrans.getBuyerOid());
                SupplierHolder supplier = supplierService.selectSupplierByKey(msgTrans.getSupplierOid());
                InvHeaderHolder invHeader = invHeaderService.selectInvHeaderByKey(invOid);
                
                //only sor invoice can be voided.
                if (!flag)
                {
                    PoHeaderHolder poHeader = null;
                    if (invHeader.getPoOid() == null)
                    {
                        poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(invHeader.getBuyerOid(), invHeader.getPoNo(), invHeader.getSupplierCode());
                    }
                    else
                    {
                        poHeader = poHeaderService.selectPoHeaderByKey(invHeader.getPoOid());
                    }

                    if (poHeader != null && poHeader.getPoType().equals(PoType.CON))
                    {
                        msg.saveError(this.getText("B2BPC1218",
                            new String[] { invHeader.getInvNo() }));
                        flag = true;
                    }
                }
                
                if (!flag && InvStatus.NEW.equals(invHeader.getInvStatus()))
                {
                    invHeaderService.voidInvoice(invOid, this.getCommonParameter());
                    msg.saveSuccess(this.getText("B2BPC2146",
                            new String[] { invHeader.getPoNo(), invHeader.getShipToCode() }));
                }
                else if (!flag && (InvStatus.VOID.equals(invHeader.getInvStatus()) || InvStatus.VOID_OUTDATED.equals(invHeader.getInvStatus())))
                {
                    msg.saveError(this.getText("B2BPC1213",
                            new String[] { invHeader.getInvNo() }));
                    flag = true;
                }
                //invoice file has not been arrive buyer side, maybe under processing.
                else if (!flag && msgTrans.getExchangeFilename() == null)
                {
                    SupplierMsgSettingHolder suppSetting = supplierMsgSettingService .selectByKey(supplier.getSupplierOid(), MsgType.INV.name());
                    String expectedFormat = suppSetting.getFileFormat();
                    InvHolder inv = new InvHolder();
                    inv.setHeader(invHeader);
                    String originalFilename = canonicalInvDocFileHandler.getTargetFilename(inv, expectedFormat);
                    boolean fileExist = FileUtil.getInstance().isFileExist(mboxUtil.getSupplierOutPath(supplier.getMboxId()), originalFilename);
                    if (fileExist)
                    {
                        RandomAccessFile input = null;
                        try
                        {
                            String indiPath = mboxUtil.getSupplierIndiOutboundPath();
                            
                            String lockFile = indiPath + PS + "lock";
                            input = new RandomAccessFile(lockFile, "rw");
                            FileChannel channel = input.getChannel();
                            FileLock lock = channel.tryLock();
                            
                            if (lock == null)
                            {
                                Thread.sleep(1000);
                                lock = channel.tryLock();
                                if (lock == null)
                                {
                                    msg.saveError(this.getText("B2BPC1211", new String[] { invHeader.getInvNo() }));
                                    flag = true;
                                }
                            }
                            
                            if (!flag)
                            {
                                String batchNo = FileUtil.getInstance().trimAllExtension("");
                                String tokName = supplier.getMboxId() + "#" + batchNo;
                                File tokFile = new File(indiPath, tokName + ".tok");
                                File procFile = new File(indiPath, tokName + ".tok.proc");
                                if (tokFile.exists() || procFile.exists())
                                {
                                    msg.saveError(this.getText("B2BPC1211", new String[] { invHeader.getInvNo() }));
                                    flag = true;
                                }
                                else
                                {
                                    invHeaderService.voidInvoice(invOid, this.getCommonParameter());
                                }
                                
                            }
                            
                            if (lock != null)
                            {
                                lock.release();
                                channel.close();
                            }
                        }
                        finally
                        {
                            if (input != null)
                            {
                                input.close();
                                input = null;
                            }
                        }
                        
                        //remove the inv file from supplier in folder
                        if (!flag)
                        {
                            File original = new File(mboxUtil.getSupplierOutPath(supplier.getMboxId()) + PS + originalFilename);
                            FileUtil.getInstance().deleleAllFile(original);
                            msg.saveSuccess(this.getText("B2BPC2146",
                                    new String[] { invHeader.getPoNo(), invHeader.getShipToCode() }));
                        }
                    }
                    else
                    {
                        msg.saveError(this.getText("B2BPC1211", new String[] { invHeader.getInvNo() }));
                        flag = true;
                    }
                }
                //invoice has arrived in buyer tmp file. maybe doing matching.
                else if (!flag && msgTrans.getExchangeFilename() != null)
                {
                    PoInvGrnDnMatchingHolder matching = poInvGrnDnMatchingService.selectEffectiveRecordByInvOid(invOid);
                    
                    if (matching != null)
                    {
                        if (!(PoInvGrnDnMatchingStatus.UNMATCHED.equals(matching.getMatchingStatus())
                                || PoInvGrnDnMatchingStatus.QTY_UNMATCHED.equals(matching.getMatchingStatus())
                                || PoInvGrnDnMatchingStatus.PRICE_UNMATCHED.equals(matching.getMatchingStatus())))
                        {
                            msg.saveError(this.getText("B2BPC1215",
                                    new String[] {matching.getInvNo()}));
                            flag = true;
                        }
                        else if (!flag && matching.getRevised())
                        {
                            msg.saveError(this.getText("B2BPC2149",
                                    new String[] { matching.getPoNo(), matching.getPoStoreCode() }));
                            flag = true;
                        }
                        else if (!flag && !PoInvGrnDnMatchingBuyerStatus.REJECTED.equals(matching.getBuyerStatus()))
                        {
                            msg.saveError(this.getText("B2BPC1216",
                                    new String[] {matching.getInvNo()}));
                            flag = true;
                        }
                    }
                    
                    if (!flag)
                    {
                        File invFile = new File(mboxUtil.getBuyerTmpPath(buyer.getMboxId()), msgTrans.getExchangeFilename());
                        if (invFile.exists())
                        {
                            RandomAccessFile input = null;
                            try
                            {
                                input = new RandomAccessFile(invFile, "rw");
                                FileChannel channel = input.getChannel();
                                FileLock lock = channel.tryLock();
                                if (lock == null)
                                {
                                    msg.saveError(this.getText("B2BPC1211", new String[] { invHeader.getInvNo() }));
                                    flag = true;
                                }
                                else
                                {
                                    invHeaderService.voidInvoice(invOid, this.getCommonParameter());
                                    
                                    lock.release();
                                    channel.close();
                                }
                                
                            }
                            finally
                            {
                                if (input != null)
                                {
                                    input.close();
                                    input = null;
                                }
                            }
                            
                            //remove the inv file from buyer's tmp folder.
                            if (!flag)
                            {
                                FileUtil.getInstance().deleleAllFile(invFile);
                                
                                msg.saveSuccess(this.getText("B2BPC2146",
                                        new String[] { invHeader.getPoNo(), invHeader.getShipToCode() }));
                            }
                        }
                        else
                        {
                            msg.saveError(this.getText("B2BPC1217", new String[] { invHeader.getInvNo() }));
                            flag = true;
                        }
                    }
                }
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
            handleException(e);
        }
        return FORWARD_COMMON_MESSAGE;
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
                param = (InvSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_INV);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_INV, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<InvSummaryHolder> invList = invHeaderService.selectAllRecordToExport(param);
                
                if (invList != null && !invList.isEmpty())
                {
                    for (InvSummaryHolder inv : invList)
                    {
                        invOids.add(inv.getDocOid());
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(inv.getDocOid());
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
            
            this.setRptResult(new ByteArrayInputStream(invoiceService.exportExcel(invOids, storeFlag)));
            rptFileName = "InvReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
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
            InvSummaryHolder searchParam = this.initSearchParameter();
            int count = invHeaderService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            
            List<MsgTransactionsExHolder> summarys = invHeaderService.getListOfSummary(searchParam);
            if (summarys==null || summarys.isEmpty())
            {
                return INPUT;
            }
            
            List<InvSummaryHolder> invSummarys = new ArrayList<InvSummaryHolder>();
            
            for (MsgTransactionsExHolder msgTrans : summarys)
            {
                invSummarys.add((InvSummaryHolder)msgTrans);
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(invoiceService.exportSummaryExcel(invSummarys, storeFlag)));
            rptFileName = "INV_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
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
        invTypes = InvType.toMapValue(this);
        invStatus = InvStatus.toMapValue(this);
        
        BigDecimal userType = this.getUserTypeOfCurrentUser();
        if (BigDecimal.valueOf(2).compareTo(userType) == 0 || BigDecimal.valueOf(4).compareTo(userType) == 0
            || BigDecimal.valueOf(6).compareTo(userType) == 0 || BigDecimal.valueOf(7).compareTo(userType) == 0)
        {
            invStatus.remove("NEW");
        }
        
        readStatus = ReadStatus.toMapValue(this);
        if (param == null)
        {
            param = new InvSummaryHolder();
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
            param.setCanBeVoid(false);
            param.setStatusPending(false);
            param.setStatusMatched(false);
            param.setStatusMatchedByDn(false);
            param.setStatusUnmatched(false);
            param.setStatusPriceUnmatched(false);
            param.setStatusQtyUnmatched(false);
            param.setStatusAmountUnmatched(false);
            param.setStatusOutdated(false);
            param.setStatusInsInv(false);
        }
    }
    
    private ReportEngineParameter<InvHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        InvHeaderHolder header = invHeaderService.selectInvHeaderByKey(msg
            .getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerByKey(msg.getBuyerOid());
        List<InvDetailHolder> details = invDetailService
            .selectInvDetailByKey(msg.getDocOid());
        List<InvHeaderExtendedHolder> headerExtendeds = invHeaderExtendedService
            .selectHeaderExtendedByKey(msg.getDocOid());

        List<InvDetailExtendedHolder> detailExtendeds = invDetailExtendedService
            .selectDetailExtendedByKey(msg.getDocOid());


        ReportEngineParameter<InvHolder> reportEngineParameter = new ReportEngineParameter<InvHolder>();

        InvHolder data = new InvHolder();

        data.setHeader(header);
        data.setDetails(details);
        data.setHeaderExtendeds(headerExtendeds);
        data.setDetailExtendeds(detailExtendeds);

        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }

    private DefaultInvReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.INV.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultInvReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.INV
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultInvReportEngine.class);
    }
    
    private InvSummaryHolder initSortField(InvSummaryHolder param)
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

    private String replaceSpecialCharactersForJson(String json)
    {
        if (json == null)
        {
            return null;
        }
        
        String result = StringUtils.replace(json, RIGHT_SINGLE_QUOTATION_MARK_CHARACTER,
            RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME);
        
        return StringUtils.replace(result, RIGHT_DOUBLE_QUOTATION_MARK,
            RIGHT_SINGLE_QUOTATION_MARK_CHARACTER);
    }
    
    private void handleExceptionForNoActionResult(Exception e)
    {
        ErrorHelper.getInstance().logError(log, this, e);
        String ticketNo = ErrorHelper.getInstance().logTicketNo(log, this,e);

        this.setErrorMsg(this.getText(
            PurchaseOrderAction.EXCEPTION_MSG_CONTENT_KEY,
            new String[] {ticketNo}));
        this.addActionError(errorMsg);
    }
    
    private void validateSaveInvoice() throws Exception
    {
        boolean flag = this.hasErrors();
        invoice = new InvHolder();
        invoice.conertJsonToInvoice(invHeaderJson, invDetailJson);
        if (flag)
        {
            this.setErrorMsg(errorMsg);
        }
        if (!flag &&(invoice.getHeader().getInvNo() == null || invoice.getHeader().getInvNo().trim().isEmpty()))
        {
            this.setErrorMsg(this.getText("B2BPC1205"));
            flag = true;
        }
        InvHeaderHolder header = invHeaderService.selectInvHeaderByKey(invoice.getHeader().getInvOid());
        if (!flag && !header.getInvNo().equalsIgnoreCase(invoice.getHeader().getInvNo()))
        {
            //InvHeaderHolder header = invHeaderService.selectInvHeaderByKey(invoice.getHeader().getInvOid());
            InvHeaderHolder param = new InvHeaderHolder();
            param.setBuyerOid(header.getBuyerOid());
            param.setSupplierOid(header.getSupplierOid());
            param.setInvNo(invoice.getHeader().getInvNo());
            List<InvHeaderHolder> result = invHeaderService.select(param);
            if (result != null && !result.isEmpty())
            {
                this.setErrorMsg(this.getText("B2BPC1206", new String[]{invoice.getHeader().getInvNo()}));
                flag = true;
            }
        }
        DateUtil date = DateUtil.getInstance();
        Date poDate = invoice.getHeader().getPoDate();
        Date invDate = invoice.getHeader().getInvDate();
        Date delvDate = invoice.getHeader().getDeliveryDate() == null ? null : invoice.getHeader().getDeliveryDate();
        String invNo = invoice.getHeader().getInvNo();
        
        PoHeaderHolder poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(header.getBuyerOid(), header.getPoNo(), header.getSupplierCode());
        
        if (!flag && date.isAfterDays(poDate, invDate, 0))
        {
            this.errorMsg = getText(B2BPC1619);
            flag = true;
        }

        if (!flag && date.isAfterDays(invDate, new Date(), 0))
        {
            this.errorMsg = getText(B2BPC1618);
            flag = true;
        }
        
        if (!flag && !(poHeader.getPoSubType2() != null && poHeader.getPoSubType2().equalsIgnoreCase("ZQO")))
        {
            if (!PoType.CON.equals(poHeader.getPoType()) && delvDate != null)
            {
                if (date.isAfterDays(poDate, delvDate, 0))
                {
                    this.errorMsg = getText(B2BPC1636);
                    flag = true;
                }
            }
        }
        
        if (!flag)
        {
            Pattern pattern = Pattern.compile(validationConfig.getCachePattern(VLD_PTN_KEY_INVOICE_NO));
            Matcher matcher = pattern.matcher(invNo);
            if (!matcher.matches())
            {
                this.errorMsg = getText(B2BPC1616);
                flag = true;
            }
            
            int maxInvNoLength = getAllowdInvNoLength();
            if (!flag && (invNo.length() > maxInvNoLength))
            {
                this.errorMsg = getText(B2BPC1613, new String[]{String.valueOf(maxInvNoLength)});
                flag = true;
            }
        }
        
        String deliveryNo = invoice.getHeader().getDeliveryNo();
        if (!flag && !StringUtils.isBlank(deliveryNo))
        {
           if (deliveryNo.length() > 20)
           {
        	   this.errorMsg = getText(B2BPC1663);
               flag = true;
           }
           else
           {
        	   Pattern pattern = Pattern.compile(validationConfig.getCachePattern(VLD_PTN_KEY_DELIVERY_NO));
               Matcher matcher = pattern.matcher(deliveryNo);
               if (!matcher.matches())
               {
                   this.errorMsg = getText(B2BPC1664);
                   flag = true;
               }
           }
        }
        
        if (flag)
        {
            this.addActionError(errorMsg);
        }
        
    }
    
    private void invoiceFormat(InvHolder invoice)throws Exception
    {
        BigDecimalUtil format = BigDecimalUtil.getInstance();
        InvHeaderHolder header = invoice.getHeader();
        invoice.getHeader().setInvAmountNoVat(format.format(header.getInvAmountNoVat(), 2));
        invoice.getHeader().setVatAmount(format.format(header.getVatAmount(), 2));
        invoice.getHeader().setInvAmountWithVat(format.format(header.getInvAmountWithVat(), 2));

        
        
        for (InvDetailHolder detail : invoice.getDetails())
        {
            detail.setInvQty(format.format(detail.getInvQty(), 2));
            detail.setFocQty(format.format(detail.getFocQty()==null?BigDecimal.ZERO : detail.getFocQty(), 2));
            detail.setUnitPrice(format.format(detail.getUnitPrice(), 2));
            detail.setDiscountAmount(format.format(detail.getDiscountAmount(), 2));
            detail.setItemAmount(format.format(detail.getItemAmount(), 2));
            detail.setNetAmount(format.format(detail.getNetAmount(), 2));
            detail.setItemGrossAmount(format.format(detail.getItemGrossAmount(), 2));
        }
    }
    
    
    private InvSummaryHolder initSearchParameter() throws Exception
    {
        InvSummaryHolder searchParam = (InvSummaryHolder)getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_INV);

        if(searchParam == null)
        {
            searchParam = new InvSummaryHolder();
            
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
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_INV, searchParam);
        }
        searchParam.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
        
        searchParam = initSortField(searchParam);

        initCurrentUserSearchParam(searchParam);
        if (null != searchParam.getMatchingStatus())
        {
            searchParam.setMatchingStatusValue(searchParam.getMatchingStatus().name());
        }
        
        searchParam.trimAllString();
        searchParam.setAllEmptyStringToNull();
        
        return searchParam;
    }
    
    
    private int getAllowdInvNoLength()
    {
        int maxInvNoLenth = 1;
        try
        {
            maxInvNoLenth = Integer.parseInt(appConfig.getMaxInvNoLength());
            
            if (maxInvNoLenth > 20)
            {
                maxInvNoLenth = 20;
            }
            
            if (maxInvNoLenth < 1)
            {
                maxInvNoLenth = 1;
            }
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return 20;
        }
        
        return maxInvNoLenth;
    }
    
    
    // *****************************************************
    // setter and getter
    // *****************************************************

    public InvSummaryHolder getParam()
    {
        return param;
    }

    
    public void setParam(InvSummaryHolder param)
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

    
    public Map<String, String> getInvTypes()
    {
        return invTypes;
    }

    
    public void setInvTypes(Map<String, String> invTypes)
    {
        this.invTypes = invTypes;
    }

    
    public Map<String, String> getInvStatus()
    {
        return invStatus;
    }

    
    public void setInvStatus(Map<String, String> invStatus)
    {
        this.invStatus = invStatus;
    }

    
    
    public String getInvHeaderJson()
    {
        return invHeaderJson;
    }
    
    

    public void setInvHeaderJson(String invHeaderJson)
    {
        this.invHeaderJson = invHeaderJson;
    }
    
    

    public String getInvDetailJson()
    {
        return invDetailJson;
    }
    
    

    public void setInvDetailJson(String invDetailJson)
    {
        this.invDetailJson = invDetailJson;
    }
    
    

    public String getErrorMsg()
    {
        return errorMsg;
    }
    
    

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    
    
    public String getBusinessRulesJson()
    {
        return businessRulesJson;
    }

    
    public void setBusinessRulesJson(String businessRulesJson)
    {
        this.businessRulesJson = businessRulesJson;
    }


    public InvHolder getInvoice()
    {
        return invoice;
    }


    public void setInvoice(InvHolder invoice)
    {
        this.invoice = invoice;
    }


    public String getSuccess()
    {
        return success;
    }


    public void setSuccess(String success)
    {
        this.success = success;
    }


    public Map<String, String> getMatchingStatus()
    {
        return matchingStatus;
    }


    public void setMatchingStatus(Map<String, String> matchingStatus)
    {
        this.matchingStatus = matchingStatus;
    }

    
    public boolean isSelectAll()
    {
        return selectAll;
    }

    
    public void setSelectAll(boolean selectAll)
    {
        this.selectAll = selectAll;
    }

    
	public boolean isGgDisableInvoicePaymentInstructions() 
	{
		return ggDisableInvoicePaymentInstructions;
	}

	
    public void setGgDisableInvoicePaymentInstructions(boolean ggDisableInvoicePaymentInstructions)
    {
        this.ggDisableInvoicePaymentInstructions = ggDisableInvoicePaymentInstructions;
    }
    

}
