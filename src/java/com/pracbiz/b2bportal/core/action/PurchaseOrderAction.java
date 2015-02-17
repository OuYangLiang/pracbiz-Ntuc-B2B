package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvHeaderExHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoSummaryHolder;
import com.pracbiz.b2bportal.core.report.DefaultPoReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.report.excel.ExcelExportGenerator;
import com.pracbiz.b2bportal.core.report.excel.ExcelReportEngine;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.CnHeaderService;
import com.pracbiz.b2bportal.core.service.CnService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PoDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PoDetailService;
import com.pracbiz.b2bportal.core.service.PoHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

public class PurchaseOrderAction extends TransactionalDocsBaseAction implements ApplicationContextAware, CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderAction.class);
    private static final long serialVersionUID = 5799188780144224588L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_PO = "SEARCH_PARAMETER_PO";
    public static final String BUSINESS_RULE_FUNC_ID_SORPO = "SorPO";
    public static final String BUSINESS_RULE_FUNC_ID_CONPO = "ConPO";
    public static final String BUSINESS_RULE_FUNC_PO_CONVERT_INV = "PoConvertInv";
    public static final String DEFAULT_STORE_FOR_SUB_TYPE_2 = "PO_SUB_TYPE_2";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.PurchaseOrderAction.selectedOids";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME = "&prime;";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_CHARACTER = "'";
    private static final String RIGHT_DOUBLE_QUOTATION_MARK = "\"";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    private static final String DELIMITOR = "_";
    private static final String SESSION_SUPPLIER_DISPUTE = "SUPPLIER_DISPUTE";
    private static final String IS_EDITTING_DELIVERY_DATE = "isEditting";
    private static final String EX_FIELD_PER_START_DATE = "periodStartDate";
    private static final String EX_FIELD_PER_END_DATE = "periodEndDate";

    private static final String VLD_PTN_KEY_INVOICE_NO = "INV_NO";
    private static final String VLD_PTN_KEY_DELIVERY_NO = "DELIVERY_NO";
    private static final String B2BPC1601 = "B2BPC1601";
    private static final String B2BPC1602 = "B2BPC1602";
    private static final String B2BPC1603 = "B2BPC1603";
    private static final String B2BPC1604 = "B2BPC1604";
    private static final String B2BPC1605 = "B2BPC1605";
    private static final String B2BPC1606 = "B2BPC1606";
    private static final String B2BPC1607 = "B2BPC1607";
    private static final String B2BPC1608 = "B2BPC1608";
    private static final String UNDEFINED = "undefined";
    private static final String B2BPC1612 = "B2BPC1612";
    private static final String B2BPC1613 = "B2BPC1613";
    private static final String B2BPC1615 = "B2BPC1615";
    private static final String B2BPC1616 = "B2BPC1616";
    //private static final String B2BPC1617 = "B2BPC1617";
    private static final String B2BPC1618 = "B2BPC1618";
    private static final String B2BPC1619 = "B2BPC1619";
    //private static final String B2BPC1623 = "B2BPC1623";
    private static final String B2BPC1636 = "B2BPC1636";
    private static final String B2BPC1660 = "B2BPC1660";
    private static final String B2BPC1661 = "B2BPC1661";
    private static final String B2BPC1663 = "B2BPC1663";
    private static final String B2BPC1664 = "B2BPC1664";
    private static final String B2BPC1669 = "B2BPC1669";
    
    @Autowired transient protected PoHeaderService poHeaderService;
    @Autowired transient protected PoLocationService poLocationService;
    @Autowired transient protected PoDetailService poDetailService;
    @Autowired transient protected TermConditionService termConditionService;
    @Autowired transient protected InvoiceService invoiceService;
    @Autowired transient protected BusinessRuleService businessRuleService;
    @Autowired transient protected PoService poService;
    @Autowired transient protected ValidationConfigHelper validationConfig;
    @Autowired transient protected InvHeaderService invHeaderService;
    @Autowired transient protected MsgTransactionsService msgTransactionsService;
    @Autowired transient protected BuyerMsgSettingService buyerMsgSettingService;
    @Autowired transient protected BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient protected PoHeaderExtendedService poHeaderExtendedService;
    @Autowired transient protected PoDetailExtendedService poDetailExtendedService;
    @Autowired transient protected PoLocationDetailService poLocationDetailService;
    @Autowired transient protected PoLocationDetailExtendedService poLocationDetailExtendedService;
    @Autowired transient protected OidService oidService;
    @Autowired transient protected ControlParameterService controlParameterService;
    @Autowired transient protected DocFileHandler<PoDocMsg, PoHolder> ebxmlPoDocFileHandler;
    @Autowired transient protected DocFileHandler<PoDocMsg, PoHolder> canonicalPoDocFileHandler;
    @Autowired transient protected DocFileHandler<PoDocMsg, PoHolder> idocPoDocFileHandler;
    @Autowired transient protected SupplierMsgSettingService supplierMsgSettingService;
    @Autowired transient protected BuyerStoreService buyerStoreService;
    @Autowired transient protected PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired transient protected BuyerStoreUserService buyerStoreUserService;
    @Autowired transient protected CnHeaderService cnHeaderService;
    @Autowired transient protected CnService cnService;
    
    transient protected ApplicationContext ctx;
    transient protected InputStream rptResult;
    protected PoSummaryHolder param;
    protected String store;
    protected String docOid;
    protected String errorMsg;
    protected String poExpiredMsg;
    protected PoHeaderHolder selectedPo;
    protected PoLocationHolder selectedPoLocation;
    protected List<PoLocationHolder> locations;
    protected InvHolder invoice;
    protected String invHeaderJson;
    protected String invDetailJson;
    protected String success;
    protected Integer storeCount;
    protected String businessRulesJson;
    protected String invNo;
    protected String rptFileName;
    protected String pdfMode;
    protected PoHeaderHolder editPo;
    protected String dateError;
    protected Map<String, String> poSubTypes;
    private boolean selectAll;
    private String typePrint;
    protected boolean ggDisableInvoicePaymentInstructions;
    
    public PurchaseOrderAction()
    {
        this.initMsg();
        this.setPageId(PageId.P001.name());
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_PO);
        
        param = (PoSummaryHolder) getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_PO);
       
        try
        {
            if (null == (String)this.getSession().get(SESSION_SUPPLIER_DISPUTE))
            {
                if (BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
                {
                    boolean enableSupplierToDispute = businessRuleService.isMatchingEnableSupplierToDispute(this.getProfileOfCurrentUser().getBuyerOid());
                    if (enableSupplierToDispute)
                    {
                        this.getSession().put(SESSION_SUPPLIER_DISPUTE, "yes");
                    }
                }
                else if (BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser())
                        || BigDecimal.valueOf(5).equals(this.getUserTypeOfCurrentUser()))
                {
                    this.getSession().put(SESSION_SUPPLIER_DISPUTE, "yes");
                }
            }
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
            param = new PoSummaryHolder();
        }
        
        try
        {
            param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getPoDateFrom()));
            param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getPoDateTo()));
            param.setDeliveryDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getDeliveryDateFrom()));
            param.setDeliveryDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getDeliveryDateTo()));
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
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_PO, param);
        
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            PoSummaryHolder searchParam = this.initSearchParameter();
            this.obtainListRecordsOfPagination(poHeaderService, searchParam, MODULE_KEY_PO);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    // *****************************************************
    // generate invoice page
    // *****************************************************
    public String invoiceData()
    {
        return SUCCESS;
    }
    
    
    public void validateInitGenerateInv() 
    {
        try
        {
            boolean flag = this.hasErrors();
            if (!flag
                && (StringUtils.isBlank(docOid) || UNDEFINED.equals(docOid)))
            {
                this.errorMsg = getText(B2BPC1601);
                flag = true;
            }

            if (!flag)
            {
                selectedPo = poHeaderService
                    .selectPoHeaderByKey(new BigDecimal(docOid));
                if (selectedPo == null)
                {
                    this.errorMsg = getText(B2BPC1602);
                    flag = true;
                }
                
                TradingPartnerHolder tp = tradingPartnerService
                    .selectByBuyerAndBuyerGivenSupplierCode(selectedPo
                        .getBuyerOid(), selectedPo.getSupplierCode());
                
                if(!flag && null == tp)
                {
                    this.errorMsg = getText("B2BPC1625", new String[] {
                        selectedPo.getPoNo(), selectedPo.getBuyerCode()});
                    flag = true;
                }
                
                SupplierHolder supplier = supplierService.selectSupplierByKey(selectedPo.getSupplierOid());
                if (!flag && supplier.getLiveDate() == null)
                {
                    this.errorMsg = getText("B2BPC1667", new String[] {
                            selectedPo.getPoNo(), selectedPo.getSupplierCode()});
                    flag = true;
                }
                
                if (!flag && DateUtil.getInstance().compareDate(supplier.getLiveDate(), selectedPo.getPoDate()) > 0)
                {
                    this.errorMsg = getText("B2BPC1668", new String[] {
                            selectedPo.getPoNo()});
                    flag = true;
                }

                if (!flag&& (PoStatus.INVOICED).equals(selectedPo.getPoStatus()))
                {
                    this.errorMsg = getText(B2BPC1603, new String[]{selectedPo.getPoNo()});
                    flag = true;
                }
                
                if (!flag&& (PoStatus.OUTDATED).equals(selectedPo.getPoStatus()))
                {
                    this.errorMsg = getText("B2BPC1624", new String[]{selectedPo.getPoNo()});
                    flag = true;
                }
                
                if (!flag&& (PoStatus.CREDITED).equals(selectedPo.getPoStatus()))
                {
                    this.errorMsg = getText(B2BPC1661, new String[]{selectedPo.getPoNo()});
                    flag = true;
                }
            }
            
            if (flag)
            {
                this.addActionError(errorMsg);
            }
        }
        catch(Exception e)
        {
            handleExceptionForNoActionResult(e);
        }
    }
    
    
    public String initGenerateInv()
    {
        return selectedPo.getPoType().name();
    }
    
    
    public String initGenerateInvForSor()
    {
        try
        {
            if ("2".equals(selectedPo.getPoSubType()))
            {
                this.store = DEFAULT_STORE_FOR_SUB_TYPE_2;
                return SUCCESS;
            }
            
            locations = poLocationService
                .selectOptionalLocationsByPoOid(selectedPo.getPoOid());
            
            storeCount = locations == null ? 0 : locations.size();
            
            if(locations != null && locations.size() > 1)
            {
                return selectedPo.getPoType().name();
            }

            if(locations != null
                && locations.size() == 1)
            {
                PoLocationHolder location = locations.get(0);
                this.store = location.getLocationCode();
            }
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        
        return SUCCESS;
    }
    
    
    public void validateInitGenerateInvForSor()
    {
        try
        {
            boolean flag = this.hasErrors();
            
            if(!flag && (PoStatus.CANCELLED).equals(selectedPo.getPoStatus()))
            {
                this.errorMsg = getText(B2BPC1604,
                    new String[] {selectedPo.getPoNo()});
                flag = true;
            }

            if(!flag
                && (PoStatus.CANCELLED_INVOICED).equals(selectedPo.getPoStatus()))
            {
                this.errorMsg = getText(B2BPC1605,
                    new String[] {selectedPo.getPoNo()});
                flag = true;
            }
            
            if (!flag)
            {
                List<PoHeaderHolder> poHeaders = poHeaderService
                    .selectPoHeadersByPoNoBuyerCodeAndSupplierCode(
                        selectedPo.getPoNo(),
                        selectedPo.getBuyerCode(), selectedPo
                        .getSupplierCode());

                for (PoHeaderHolder header : poHeaders)
                {
                    if (header.getPoOid().compareTo(selectedPo.getPoOid()) == 0)
                        continue;
                    if  (PoStatus.INVOICED.equals(header.getPoStatus()) || PoStatus.PARTIAL_INVOICED.equals(header.getPoStatus()))
                    {
                        this.errorMsg = getText(B2BPC1606,
                            new String[] {selectedPo.getPoNo()});
                        flag = true;
                    }
                }
            }

            if(!flag && StringUtils.isBlank(poExpiredMsg)
                && StringUtils.isBlank(store))
            {
                Date expiration = selectedPo.getExpiryDate();
                Date current = new Date();

                if (expiration != null
                    && current.after(DateUtil.getInstance().getLastTimeOfDay(
                        expiration)))
                {
                    BuyerHolder buyer = buyerService.selectBuyerByBuyerCode(selectedPo.getBuyerCode());
                    boolean isIgnoreExpiryDate = businessRuleService.isIgnoreExpiryDate(buyer.getBuyerOid());
                    
                    if (isIgnoreExpiryDate)
                    {
                        this.poExpiredMsg = getText(B2BPC1607,
                            new String[] {selectedPo.getPoNo()});
                        flag = true;
                    }
                    else
                    {
                        this.errorMsg = getText(B2BPC1669,
                            new String[] {selectedPo.getPoNo()});
                        flag = true;
                    }
                }
            }
            
            if (flag)
            {
                this.addActionError(errorMsg);
            }
        }
        catch(Exception e)
        {
            handleExceptionForNoActionResult(e);
        }
    }
    
    
    public void validateGenerateInvForSor()
    {
        try
        {
            boolean flag = this.hasErrors();
            if (!flag && StringUtils.isBlank(store))
            {
                this.errorMsg = this.getText(B2BPC1608);
                flag = true;
            }
            
            if (!flag && StringUtils.isBlank(docOid))
            {
                this.errorMsg = getText(B2BPC1601);
                flag = true;
            }
            
            PoHeaderHolder header = poHeaderService.selectPoHeaderByKey(new BigDecimal(docOid));
            List<InvHeaderHolder> invs = invHeaderService.selectInvHeaderByBuyerSupplierPoNoAndStore(
                header.getBuyerCode(), header.getSupplierCode(), header.getPoNo(), store);

            if (!flag && invs != null && !invs.isEmpty())
            {
                for (InvHeaderHolder inv : invs)
                {
                    if (InvStatus.VOID.equals(inv.getInvStatus()))
                    {
                        PoInvGrnDnMatchingHolder param = new PoInvGrnDnMatchingHolder();
                        param.setInvOid(inv.getInvOid());
                        List<PoInvGrnDnMatchingHolder> matchings = poInvGrnDnMatchingService.select(param);
                        
                        if (null == matchings || matchings.isEmpty())
                        {
                            continue;
                        }
                        else
                        {
                            this.errorMsg = this.getText("B2BPC1629", new String[] { store });
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if (flag)
            {
                locations = poLocationService
                    .selectOptionalLocationsByPoOid(new BigDecimal(docOid));
                storeCount = locations == null ? 0 : locations.size();
                selectedPo = header;
                this.addActionError(errorMsg);
            }
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
    }
    
    
    public String generateInvForSor()
    {
        try
        {
            BigDecimal poOid = new BigDecimal(docOid);
            PoHolder po = poService.selectPoByKey(poOid);
            
            BigDecimal supplierOid = po.getPoHeader().getSupplierOid();
            SupplierHolder supplier = supplierService.selectSupplierByKey(supplierOid);
            PoHeaderHolder poHeader = po.getPoHeader();
            
            TradingPartnerHolder tradingPartner = tradingPartnerService
                .selectByBuyerAndBuyerGivenSupplierCode(poHeader.getBuyerOid(),
                    poHeader.getSupplierCode());
            TermConditionHolder term = null;
            if (tradingPartner.getTermConditionOid() == null)
            {
                term = termConditionService.selectDefaultTermConditionBySupplierOid(supplierOid);
            }
            else
            {
                term = termConditionService.selectTermConditionByKey(tradingPartner.getTermConditionOid());
            }
            
            if (StringUtils.isBlank(invNo))
            {
                invNo = this.getAutoInvNo(supplier);
            }
            BigDecimal oid = this.oidService.getOid();
            invoice = po.toInvoice(supplier, invNo, store, term, storeCount, oid);
            
            JSONObject invHeader = JSONObject.fromObject(invoice.getHeader());
            JSONArray invDetails = JSONArray.fromObject(invoice.getDetails());
            invHeaderJson = this.replaceSpecialCharactersForJson(invHeader.toString());
            invDetailJson = this.replaceSpecialCharactersForJson(invDetails.toString());
            
            List<BusinessRuleHolder> businessRules = businessRuleService
                .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                    poHeader.getBuyerOid(), BUSINESS_RULE_FUNC_PO_CONVERT_INV,
                    BUSINESS_RULE_FUNC_ID_SORPO);
            ggDisableInvoicePaymentInstructions = businessRuleService.isDisablePaymentInstructions(poHeader.getBuyerOid());
            
            JSONArray rules = JSONArray.fromObject(businessRules);
            businessRulesJson = this.replaceSpecialCharactersForJson(rules.toString());
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
       
        return SUCCESS;
    }
    
    
    //*********************************
    //consignment po generate invoice
    //*********************************
    public void validateGenerateInvForCn() 
    {
        try
        {
            boolean flag = this.hasErrors();
            if (!flag && StringUtils.isBlank(docOid))
            {
                this.errorMsg = getText(B2BPC1601);
                flag = true;
            }
            
            PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(new BigDecimal(docOid));
            if(!flag && (PoStatus.CANCELLED).equals(poHeader.getPoStatus()))
            {
                this.errorMsg = getText(B2BPC1604,
                    new String[] {poHeader.getPoNo()});
                flag = true;
            }

            if(!flag
                && (PoStatus.CANCELLED_INVOICED).equals(poHeader.getPoStatus()))
            {
                this.errorMsg = getText(B2BPC1605, new String[] {poHeader.getPoNo()});
                flag = true;
            }
            
            if (!flag)
            {
                List<PoHeaderHolder> poHeaders = poHeaderService
                .selectPoHeadersByPoNoBuyerCodeAndSupplierCode(
                    poHeader.getPoNo(),poHeader.getBuyerCode(), poHeader.getSupplierCode());

                for (PoHeaderHolder header : poHeaders)
                {
                    if (header.getPoOid().compareTo(poHeader.getPoOid()) == 0)
                        continue;
                    if  (PoStatus.INVOICED.equals(header.getPoStatus()))
                    {
                        this.errorMsg = getText(B2BPC1606, new String[] {header.getPoNo()});
                        flag = true;
                    }
                    
                    if (PoStatus.CREDITED.equals(poHeader.getPoStatus()))
                    {
                        this.errorMsg = getText(B2BPC1660, new String[] {header.getPoNo()});
                        flag = true;
                    }
                }
            }

//            if(!flag && StringUtils.isBlank(poExpiredMsg)
//                && StringUtils.isBlank(store))
//            {
//                Date expiration = selectedPo.getExpiryDate();
//                Date current = new Date();
//
//                if (expiration != null
//                    && current.after(DateUtil.getInstance().getLastTimeOfDay(
//                        expiration)))
//                {
//                    this.poExpiredMsg = getText(B2BPC1607,
//                        new String[] {selectedPo.getPoNo()});
//                    flag = true;
//                }
//            }
            
            
//            InvHeaderHolder inv = invHeaderService.selectInvHeaderByPoOid(new BigDecimal(docOid));
//
//            if (inv != null)
//            {
//                if (InvStatus.VOID.equals(inv.getInvStatus()))
//                {
//                    PoInvGrnDnMatchingHolder param = new PoInvGrnDnMatchingHolder();
//                    param.setInvOid(inv.getInvOid());
//                    List<PoInvGrnDnMatchingHolder> matchings = poInvGrnDnMatchingService.select(param);
//                    
//                    if (null != matchings && matchings.isEmpty())
//                    {
//                        this.errorMsg = this.getText("B2BPC1629", new String[] { store });
//                        flag = true;
//                    }
//                }
//            }
           
            if (flag)
            {
                log.info(errorMsg);
                this.addActionError(errorMsg);
            }
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
    }
    
    
    public String generateInvForCn()
    {
        try
        {
            BigDecimal poOid = new BigDecimal(docOid);
            PoHolder po = poService.selectPoByKey(poOid);
            
            BigDecimal supplierOid = po.getPoHeader().getSupplierOid();
            SupplierHolder supplier = supplierService.selectSupplierByKey(supplierOid);
            PoHeaderHolder poHeader = po.getPoHeader();
            
            TradingPartnerHolder tradingPartner = tradingPartnerService
                .selectByBuyerAndBuyerGivenSupplierCode(poHeader.getBuyerOid(),
                    poHeader.getSupplierCode());
            TermConditionHolder term = null;
            if (tradingPartner.getTermConditionOid() == null)
            {
                term = termConditionService.selectDefaultTermConditionBySupplierOid(supplierOid);
            }
            else
            {
                term = termConditionService.selectTermConditionByKey(tradingPartner.getTermConditionOid());
            }
            
            if (StringUtils.isBlank(invNo))
            {
                invNo = this.getAutoInvNo(supplier);
            }
            BigDecimal oid = this.oidService.getOid();
            invoice = po.toInvoice(supplier, invNo, store, term, storeCount, oid);
            
            JSONObject invHeader = JSONObject.fromObject(invoice.getHeader());
            JSONArray invDetails = JSONArray.fromObject(invoice.getDetails());
            invHeaderJson = this.replaceSpecialCharactersForJson(invHeader.toString());
            invDetailJson = this.replaceSpecialCharactersForJson(invDetails.toString());
            
            List<BusinessRuleHolder> businessRules = businessRuleService
                .selectRulesByBuyerOidAndFuncGroupAndFuncId(
                    poHeader.getBuyerOid(), BUSINESS_RULE_FUNC_PO_CONVERT_INV,
                    BUSINESS_RULE_FUNC_ID_CONPO);
            
            ggDisableInvoicePaymentInstructions = businessRuleService.isDisablePaymentInstructions(poHeader.getBuyerOid());
            
            JSONArray rules = JSONArray.fromObject(businessRules);
            businessRulesJson = this.replaceSpecialCharactersForJson(rules.toString());
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
       
        return SUCCESS;
    }
    
    
    //********************
    //save invoice
    //********************
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
            
            this.initInvHeaderExtendedsByPoHeader(poHeader);
            this.initInvDetailExtendeds();
            
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
            
            this.initInvHeaderExtendedsByPoHeader(poHeader);
            this.initInvDetailExtendeds();
            
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
    
    
    @SuppressWarnings("unchecked")
    public String initEditDeliveryDate()
    {
        try
        {
            List<String> urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            if (!urls.contains("/po/initEditDeliveryDate.action"))
            {
                errorMsg = "noPrivilege";
                return SUCCESS;
            }
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);

            if(null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }
            this.getSession().remove(SESSION_OID_PARAMETER);

            String oid = selectedOids.toString();
            editPo = poHeaderService.selectPoHeaderByKey(new BigDecimal(oid));
            //validate if the po can be edit
            
            if (null == editPo)
            {
                errorMsg = "notExist";
                return SUCCESS;
            }
            if (editPo.getPoStatus().equals(PoStatus.INVOICED) || editPo.getPoStatus().equals(PoStatus.PARTIAL_INVOICED))
            {
                errorMsg = "invoiced";
                return SUCCESS;
            }
            if (editPo.getPoStatus().equals(PoStatus.CANCELLED) || editPo.getPoStatus().equals(PoStatus.CANCELLED_INVOICED))
            {
                errorMsg = "cancelled";
                return SUCCESS;
            }
            if (editPo.getPoStatus().equals(PoStatus.OUTDATED))
            {
                errorMsg = "outDated";
                return SUCCESS;
            }
            if (editPo.getPoType().equals(PoType.CON))
            {
                errorMsg = "cnType";
                return SUCCESS;
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String saveEditDeliveryDate()
    {
        try
        {
            if ("yes".equals((String)this.getSession().get(IS_EDITTING_DELIVERY_DATE)))
            {
                errorMsg = "isEditting";
                return SUCCESS;
            }
            
            final PoHeaderHolder header = poHeaderService.selectPoHeaderByKey(editPo.getPoOid());
            
            if (null == header)
            {
                errorMsg = "notExist";
                return SUCCESS;
            }
            
            
            Date oDateFrom = DateUtil.getInstance().getFirstTimeOfDay(header.getDeliveryDateFrom());
            Date oDateTo = DateUtil.getInstance().getLastTimeOfDay(header.getDeliveryDateTo());
            Date nDateFrom = DateUtil.getInstance().getFirstTimeOfDay(editPo.getDeliveryDateFrom());
            Date nDateTo = DateUtil.getInstance().getLastTimeOfDay(editPo.getDeliveryDateTo());
            
            if (nDateFrom != null && nDateTo != null && nDateTo.before(nDateFrom))
            {
                errorMsg = "dateFromToWrong";
                return SUCCESS;
            }
            
            if ((oDateFrom == null && oDateTo == null && nDateFrom == null && nDateTo == null)
                || (oDateFrom == null && nDateFrom == null && oDateTo != null && nDateTo != null && oDateTo.compareTo(nDateTo) == 0)
                || (oDateFrom != null && nDateFrom != null && oDateFrom.compareTo(nDateFrom) == 0 && oDateTo == null && nDateTo == null)
                || (oDateFrom != null && nDateFrom != null && oDateFrom.compareTo(nDateFrom) == 0 && oDateTo != null && nDateTo != null && oDateTo.compareTo(nDateTo) == 0))
            {
                errorMsg = "notChange";
                return SUCCESS;
            }
                
            int range = businessRuleService.selectPoDeliveryDateRange(header.getBuyerOid());
            Date fromDate = DateUtil.getInstance().getFirstTimeOfDay(header.getPoDate());
            Date toDate = DateUtil.getInstance().getLastTimeOfDay(DateUtil.getInstance().dateAfterDays(header.getPoDate(), range));
            if (editPo.getDeliveryDateFrom().compareTo(toDate) > 0 || editPo.getDeliveryDateFrom().compareTo(fromDate) < 0)
            {
                dateError = getText("B2BPC1626", new String[]{DateUtil.getInstance().convertDateToString(
                    fromDate, DateUtil.DATE_FORMAT_DDMMYYYY), DateUtil.getInstance().convertDateToString(toDate, DateUtil.DATE_FORMAT_DDMMYYYY)});
                errorMsg="dateError";
                return SUCCESS;
            }
            
            
            this.getSession().put(IS_EDITTING_DELIVERY_DATE, "yes");
            
            PoHolder po = this.retrievePoHolder(editPo.getPoOid());
            po.getPoHeader().setDeliveryDateFrom(DateUtil.getInstance().getFirstTimeOfDay(editPo.getDeliveryDateFrom()));
            po.getPoHeader().setDeliveryDateTo(DateUtil.getInstance().getLastTimeOfDay(editPo.getDeliveryDateTo()));
            
            po.getPoHeader().setAllEmptyStringToNull();
            
//            final BuyerMsgSettingHolder buyerMsgSetting = buyerMsgSettingService.selectByKey(po.getPoHeader()
//                .getBuyerOid(), MsgType.PO.name());
            final SupplierMsgSettingHolder supplierMsgSetting = supplierMsgSettingService.selectByKey(po.getPoHeader()
                .getSupplierOid(), MsgType.PO.name());
            BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(po.getPoHeader().getBuyerOid());
            SupplierHolder supplier = supplierService.selectSupplierByKey(po.getPoHeader().getSupplierOid());
            //generate new pdf in supplier's doc in folder.
            BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(po.getPoHeader().getBuyerOid(), 
                    MsgType.PO.name(), header.getPoType().name());
            String pdfName = this.generatePdfAfterEdit(po, buyerMsgSettingReport, buyer, supplier);
            
            this.setPoOidForPO(po);
            //generate original file for supplier.
            String supplierFile = this.generateOriginalForSupplier(po, supplierMsgSetting, supplier, header.getPoOid());
            
            po.setPdfName(pdfName);
            po.setSupplierFileName(supplierFile);
            po.setOldPoHeader(header);
            
            //insert new po and update old po
            poService.auditInsert(this.getCommonParameter(), po);
            
            MsgTransactionsHolder msg = msgTransactionsService.selectByKey(editPo.getPoOid());
            final BatchMsg batch = this.initBatchMsg(msg, buyer, supplier, po);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //send email to buyer
//                        alertBuyer(batch, buyerMsgSetting, header.getDeliveryDateFrom(), editPo.getDeliveryDateFrom()
//                            , header.getDeliveryDateTo(), editPo.getDeliveryDateTo());
                        //send email to supplier
                        alertSupplier(batch);
                    }
                    catch(Exception e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
            }).start();
            
            this.getSession().remove(IS_EDITTING_DELIVERY_DATE);
        
        }
        catch (Exception e)
        {
            this.getSession().remove(IS_EDITTING_DELIVERY_DATE);
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    // *****************************************************
    // print pdf
    // *****************************************************
    
    protected String computePdfFilename(String originalName, String printType)
    {
        if(BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser())
            || BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
        {
            if(printType.equalsIgnoreCase("I"))
            {
                return FileUtil.getInstance().trimAllExtension(originalName)
                    + CoreCommonConstants.REPORT_NAME_FOR_STORE_EXTENSION + ".pdf";
            }
            else if(printType.equalsIgnoreCase("S"))
            {
                return FileUtil.getInstance().trimAllExtension(originalName)
                    + CoreCommonConstants.REPORT_NAME_FOR_STORE_BY_STORE_EXTENSION + ".pdf";
            }
        }
        else if((BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()) || 
                BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()) ||
                BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()) ||
                BigDecimal.valueOf(5).equals(this.getUserTypeOfCurrentUser())) 
                && printType.equalsIgnoreCase("S"))
        {
            return FileUtil.getInstance().trimAllExtension(originalName)
                + CoreCommonConstants.REPORT_NAME_FOR_SUPPLIER_EXTENSION + ".pdf";
        }
        
        return originalName;
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
    
    
    protected boolean isBuyerCompanyUser()
    {
        return BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()) || 
            BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()) ||
            BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser()) || 
            BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser());
    }
    
    
    public String printPdf()
    {
        try
        {
            String[] files = null;
            InputStream [] inputs= null;
            
            boolean selectAllStore = false;
            boolean selectAllAreaHouse = false;
            
            if (selectAll)
            {
                param = (PoSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_PO);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_PO, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<PoSummaryHolder> poList = poHeaderService.selectAllRecordToExport(param);
                
                if (poList != null && !poList.isEmpty())
                {
                    files = new String[poList.size()];
                    inputs = new InputStream[poList.size()];
                    for (int i = 0; i < poList.size(); i++)
                    {
                        PoSummaryHolder po = poList.get(i);
                        
                        int flag = DefaultReportEngine.PDF_TYPE_STANDARD;
                        if (BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser()) || 
                                BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
                        {
                            flag = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                            if (typePrint.equalsIgnoreCase("S"))
                            {
                                flag = DefaultReportEngine.PDF_TYPE_BY_STORE_STORE;
                            }
                        }
                        
                        if((BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()) || 
                            BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()) ||
                            BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()) ||
                            BigDecimal.valueOf(5).equals(this.getUserTypeOfCurrentUser())) 
                            && typePrint.equalsIgnoreCase("S"))
                        {
                            flag = DefaultReportEngine.PDF_TYPE_BY_STORE;
                        }
                        
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(po.getDocOid());
                        SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                        
                        rptFileName = this.computePdfFilename(msg.getReportFilename(), typePrint);
                        
                        File file = new File(mboxUtil.getFolderInSupplierDocInPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())), rptFileName);
                        
                        PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(po.getDocOid());
                        
                        BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                MsgType.PO.name(), poHeader.getPoType().name());
                        
                        DefaultPoReportEngine poReportEngine = null;

                        ReportEngineParameter<PoHolder> reportParameter = null;

                        if(!file.exists())
                        {
                            File docPath = new File(mboxUtil.getFolderInSupplierDocInPath(
                                supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                    msg.getCreateDate())));
                            
                            if (!docPath.isDirectory())
                            {
                                FileUtil.getInstance().createDir(docPath);
                            }
                            
                            //0 : standard pdf ,1 : for store user by item ,2 :  for supplier user pdf, 3: for store user by store.
                            if (null == poReportEngine) poReportEngine = retrieveEngine(buyerMsgSettingReport, msg.getBuyerCode());
                            if (null == reportParameter) reportParameter = poService.retrieveParameter(msg, supplier);
                            byte[] datas = poReportEngine.generateReport(reportParameter, flag);
                            
                            FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                            
                        }
                        
                        this.updateReadStatus(msg);
                        
                        
                        files[i] = file.getPath();
                        
                        UserProfileHolder user = this.getProfileOfCurrentUser();
                        boolean isBuyer = user.getUserType().equals(BigDecimal.valueOf(2)) || user.getUserType().equals(BigDecimal.valueOf(4));
                        boolean isStore = user.getUserType().equals(BigDecimal.valueOf(6)) || user.getUserType().equals(BigDecimal.valueOf(7));
                        List<BuyerStoreUserHolder> buyerStoreUsers = buyerStoreUserService.selectStoreUserByUserOid(this.getProfileOfCurrentUser().getUserOid());
                        
                        if (isBuyer || isStore)
                        {
                            if (buyerStoreUsers != null && !buyerStoreUsers.isEmpty())
                            {
                                for (BuyerStoreUserHolder buyerStoreUser : buyerStoreUsers)
                                {
                                    if (buyerStoreUser.getStoreOid().compareTo(BigDecimal.valueOf(-3)) == 0)
                                    {
                                        selectAllStore = true;
                                        continue;
                                    }
                                    if (buyerStoreUser.getStoreOid().compareTo(BigDecimal.valueOf(-4)) == 0)
                                    {
                                        selectAllAreaHouse = true;
                                        continue;
                                    }
                                    BuyerStoreHolder buyerStore = buyerStoreService.selectBuyerStoreByStoreOid(buyerStoreUser.getStoreOid());
                                    if (buyerStore == null)
                                    {
                                        continue;
                                    }
                                }
                            }
                        }
                      
                        //check buyerAdmin,buyerUser,storeAdmin and storeUser's locaion if accessed 
                        if (!(selectAllStore && selectAllAreaHouse) && this.isBuyerCompanyUser())
                        {
                            List<String> buyerStoreList = this.getBuyerStoreCodeAccess();

                            if (null == poReportEngine) poReportEngine = retrieveEngine(buyerMsgSettingReport, msg.getBuyerCode());
                            if (null == reportParameter) reportParameter = poService.retrieveParameter(msg, supplier);

                            this.retrievePoLocationDetails(reportParameter,
                                buyerStoreList);

                            this.retrievePoDetails(reportParameter);

                            //standard report 
                            if(!buyerMsgSettingReport.getCustomizedReport())
                            {
                                this.retrieveStandardReportDetails(reportParameter);
                            }

                            byte[] datas = poReportEngine.generateReport(
                                reportParameter, flag);

                            inputs[i] = new ByteArrayInputStream(datas);

                        }
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
                inputs= new InputStream[parts.length];
                
                for(int i = 0; i < files.length; i++)
                {
                    String[] partString = parts[i].split("/");
                    BigDecimal poOid = new BigDecimal(partString[0]);
                    PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(poOid);
                    String printType = partString[1];
                    
                    int flag = DefaultReportEngine.PDF_TYPE_STANDARD;
                    if (BigDecimal.valueOf(6).equals(this.getUserTypeOfCurrentUser()) || 
                            BigDecimal.valueOf(7).equals(this.getUserTypeOfCurrentUser()))
                    {
                        flag = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                        if (printType.equalsIgnoreCase("S"))
                        {
                            flag = DefaultReportEngine.PDF_TYPE_BY_STORE_STORE;
                        }
                    }
                    
                    if((BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser()) || 
                        BigDecimal.valueOf(3).equals(this.getUserTypeOfCurrentUser()) ||
                        BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()) ||
                        BigDecimal.valueOf(5).equals(this.getUserTypeOfCurrentUser())) 
                        && printType.equalsIgnoreCase("S"))
                    {
                        flag = DefaultReportEngine.PDF_TYPE_BY_STORE;
                    }
                    
                    
                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(poOid);
                    SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                    
                    rptFileName = this.computePdfFilename(msg.getReportFilename(), printType);
                    
                    File file = new File(mboxUtil.getFolderInSupplierDocInPath(
                        supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                            msg.getCreateDate())), rptFileName);
                    
                    BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                            MsgType.PO.name(), poHeader.getPoType().name());
                    
                    DefaultPoReportEngine poReportEngine = null;

                    ReportEngineParameter<PoHolder> reportParameter = null;

                    if(!file.exists())
                    {
                        File docPath = new File(mboxUtil.getFolderInSupplierDocInPath(
                            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                                msg.getCreateDate())));
                        
                        if (!docPath.isDirectory())
                        {
                            FileUtil.getInstance().createDir(docPath);
                        }
                        
                        //0 : standard pdf ,1 : for store user by item ,2 :  for supplier user pdf, 3: for store user by store.
                        if (null == poReportEngine) poReportEngine = retrieveEngine(buyerMsgSettingReport, msg.getBuyerCode());
                        if (null == reportParameter) reportParameter = poService.retrieveParameter(msg, supplier);
                        byte[] datas = poReportEngine.generateReport(reportParameter, flag);
                        
                        FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                        
                    }
                    
                    this.updateReadStatus(msg);
                    
                    
                    files[i] = file.getPath();
                    
                    UserProfileHolder user = this.getProfileOfCurrentUser();
                    boolean isBuyer = user.getUserType().equals(BigDecimal.valueOf(2)) || user.getUserType().equals(BigDecimal.valueOf(4));
                    boolean isStore = user.getUserType().equals(BigDecimal.valueOf(6)) || user.getUserType().equals(BigDecimal.valueOf(7));
                    List<BuyerStoreUserHolder> buyerStoreUsers = buyerStoreUserService.selectStoreUserByUserOid(this.getProfileOfCurrentUser().getUserOid());
                    
                    if (isBuyer || isStore)
                    {
                        if (buyerStoreUsers != null && !buyerStoreUsers.isEmpty())
                        {
                            for (BuyerStoreUserHolder buyerStoreUser : buyerStoreUsers)
                            {
                                if (buyerStoreUser.getStoreOid().compareTo(BigDecimal.valueOf(-3)) == 0)
                                {
                                    selectAllStore = true;
                                    continue;
                                }
                                if (buyerStoreUser.getStoreOid().compareTo(BigDecimal.valueOf(-4)) == 0)
                                {
                                    selectAllAreaHouse = true;
                                    continue;
                                }
                                BuyerStoreHolder buyerStore = buyerStoreService.selectBuyerStoreByStoreOid(buyerStoreUser.getStoreOid());
                                if (buyerStore == null)
                                {
                                    continue;
                                }
                            }
                        }
                    }
                  
                    //check buyerAdmin,buyerUser,storeAdmin and storeUser's locaion if accessed 
                    if (!(selectAllStore && selectAllAreaHouse) && this.isBuyerCompanyUser())
                    {
                        List<String> buyerStoreList = this.getBuyerStoreCodeAccess();

                        if (null == poReportEngine) poReportEngine = retrieveEngine(buyerMsgSettingReport, msg.getBuyerCode());
                        if (null == reportParameter) reportParameter = poService.retrieveParameter(msg, supplier);

                        this.retrievePoLocationDetails(reportParameter,
                            buyerStoreList);

                        this.retrievePoDetails(reportParameter);

                        //standard report 
                        if(!buyerMsgSettingReport.getCustomizedReport())
                        {
                            this.retrieveStandardReportDetails(reportParameter);
                        }

                        byte[] datas = poReportEngine.generateReport(
                            reportParameter, flag);

                        inputs[i] = new ByteArrayInputStream(datas);

                    }
                    
                }
            }
          
            if (!(selectAllStore && selectAllAreaHouse) && this.isBuyerCompanyUser())
            {
                //no files's create,just stream to show data.
                rptResult = new ByteArrayInputStream(PdfReportUtil.getInstance().mergePDFsByte(inputs));
            }
            else
            {
                rptResult = PdfReportUtil.getInstance().mergePDFs(files);
            }
            
            if((files != null && files.length > 1) || (inputs != null && inputs.length > 1))
            {
                rptFileName = "poReports_"+ DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
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
            @SuppressWarnings("rawtypes")
            List<ReportEngineParameter> params = new ArrayList<ReportEngineParameter>();
            @SuppressWarnings("rawtypes")
            List<ExcelReportEngine> engines = new ArrayList<ExcelReportEngine>();
            if (selectAll)
            {
                param = (PoSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_PO);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_PO, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<PoSummaryHolder> poList = poHeaderService.selectAllRecordToExport(param);
                
                if (poList != null && !poList.isEmpty())
                {
                    for (PoSummaryHolder po : poList)
                    {
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(po.getDocOid());
                        SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                        params.add(poService.retrieveParameter(msg, supplier));
                        engines.add(this.retrieveExcelEngine(MsgType.PO.name(), msg.getBuyerCode()));
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
                    BigDecimal poOid = new BigDecimal(part);
                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(poOid);
                    SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                    params.add(poService.retrieveParameter(msg, supplier));
                    engines.add(this.retrieveExcelEngine(MsgType.PO.name(), msg.getBuyerCode()));
                    this.updateReadStatus(msg);
                }
            }
            int storeFlag;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = ExcelReportEngine.EXCEL_TYPE_IS_STORE;
            }
            else
            {
                storeFlag = ExcelReportEngine.EXCEL_TYPE_STANDARD;
            }
            this.setRptResult(new ByteArrayInputStream(ExcelExportGenerator.getInstance().generateExcel(engines, params, storeFlag)));
            rptFileName = "PoReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
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
            PoSummaryHolder searchParam = this.initSearchParameter();
            int count = poHeaderService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            
            List<MsgTransactionsExHolder> summarys = poHeaderService.getListOfSummary(searchParam);
            if (summarys==null || summarys.isEmpty())
            {
                return INPUT;
            }
            
            List<PoSummaryHolder> poSummarys = new ArrayList<PoSummaryHolder>();
            
            for (MsgTransactionsExHolder msgTrans : summarys)
            {
                poSummarys.add((PoSummaryHolder)msgTrans);
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(poService.exportSummaryExcel(poSummarys, storeFlag)));
            rptFileName = "PO_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    
    //************************************
    //save credit note 
    //************************************
    public void validateSaveCn()
    {
        try
        {
            this.validateSaveCreditNote();
            
            
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
    }
    
    
    public String saveCn()
    {
        try
        {
            MsgTransactionsHolder poMsg = msgTransactionsService.selectByKey(invoice.getHeader().getPoOid());
            this.updateReadStatus(poMsg);
            
            InvHeaderHolder invHeader = invoice.getHeader();
            PoHeaderHolder poHeader = poHeaderService
                .selectPoHeaderByKey(invHeader.getPoOid());
            
            CommonParameterHolder cp = this.getCommonParameter();
            BuyerHolder buyer = buyerService.selectBuyerByKey(invoice
                .getHeader().getBuyerOid());
            SupplierHolder supplier = supplierService
                .selectSupplierByKey(invoice.getHeader().getSupplierOid());
            invoice.getHeader().setInvAmountNoVat(BigDecimalUtil.getInstance().format(
                invoice.getHeader().getInvAmountNoVat().add(invoice.getHeader().getAdditionalDiscountAmount()), 2));
           
            List<PoDetailExtendedHolder> detailExs = poDetailExtendedService.selectDetailExtendedByKey(invoice.getHeader().getPoOid());
            
            CnHolder cn = invoice.toCreditNote(poHeader, detailExs, supplier, invoice.getHeader().getInvOid(), CnStatus.NEW);
            MsgTransactionsHolder msg = cn.getHeader().toMsgTransactions();
            
            cnService.createCn(cp, cn, msg, poHeader, buyer, supplier, PoStatus.CREDITED);
            this.success = SUCCESS;
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        
        return SUCCESS;
    }
    
    
    public void validateSaveAndSentCn()
    {
        try
        {
            this.validateSaveCreditNote();
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
    }
    
    
    public String saveAndSentCn()
    {
        try
        {
            MsgTransactionsHolder poMsg = msgTransactionsService.selectByKey(invoice.getHeader().getPoOid());
            this.updateReadStatus(poMsg);
            
            InvHeaderHolder invHeader = invoice.getHeader();
            PoHeaderHolder poHeader = poHeaderService
                .selectPoHeaderByKey(invHeader.getPoOid());
            
            CommonParameterHolder cp = this.getCommonParameter();
            BuyerHolder buyer = buyerService.selectBuyerByKey(invoice
                .getHeader().getBuyerOid());
            SupplierHolder supplier = supplierService
                .selectSupplierByKey(invoice.getHeader().getSupplierOid());
            invoice.getHeader().setInvAmountNoVat(BigDecimalUtil.getInstance().format(
                invoice.getHeader().getInvAmountNoVat().add(invoice.getHeader().getAdditionalDiscountAmount()), 2));
            
            List<PoDetailExtendedHolder> detailExs = poDetailExtendedService.selectDetailExtendedByKey(invoice.getHeader().getPoOid());
            
            CnHolder cn = invoice.toCreditNote(poHeader, detailExs, supplier, invoice.getHeader().getInvOid(), CnStatus.SUBMIT);
            MsgTransactionsHolder msg = cn.getHeader().toMsgTransactions();
            cnService.createAndSentCn(cp, cn, msg, poHeader, buyer, supplier, PoStatus.CREDITED);
            this.success = SUCCESS;
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        
        return SUCCESS;
    }
    
    // *****************************************************
    // private method
    // *****************************************************
    protected void initSearchCondition() throws Exception
    {
        docStatuses = PoStatus.toMapValue(this);
        docTypes = PoType.toMapValue(this);
        readStatus = ReadStatus.toMapValue(this);
        poSubTypes = new HashMap<String, String>();
        poSubTypes.put("ZNB", "ZNB");
        poSubTypes.put("ZPO", "ZPO");
        poSubTypes.put("NB", "NB");
        
        if(null == param)
        {
            param = new PoSummaryHolder();
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
        }
        
    }
    
    
    protected String getAutoInvNo(SupplierHolder supplier) throws Exception
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
    
    
    protected String replaceSpecialCharactersForJson(String json)
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
    
    
    protected void handleExceptionForNoActionResult(Exception e)
    {
        ErrorHelper.getInstance().logError(log, this, e);
        String ticketNo = ErrorHelper.getInstance().logTicketNo(log, this,e);

        this.setErrorMsg(this.getText(
            PurchaseOrderAction.EXCEPTION_MSG_CONTENT_KEY,
            new String[] {ticketNo}));
        this.addActionError(errorMsg);
    }

    
    protected DefaultPoReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.PO.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultPoReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.PO
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultPoReportEngine.class);
    }
    
    @SuppressWarnings("rawtypes")
    protected ExcelReportEngine retrieveExcelEngine(String msgType, String buyerCode)
    {
        return ctx.getBean(appConfig.getCustomizedExcelReport(buyerCode, msgType),
            ExcelReportEngine.class);
    }
    
    
    
    private void validateSaveInvoice() throws Exception
    {
        boolean flag = this.hasErrors();
        
        invoice = new InvHolder();
        invoice.conertJsonToInvoice(invHeaderJson, invDetailJson);
        
        PoHeaderHolder header = poHeaderService.selectPoHeaderByKey(invoice.getHeader().getPoOid());
        
        if (!flag)
        {
            InvHeaderHolder oldInv = invHeaderService.selectEffectiveInvHeaderByBuyerSupplierPoNoAndStore(invoice.getHeader().getBuyerCode(), invoice.getHeader().getSupplierCode(), invoice.getHeader().getPoNo(), invoice.getHeader().getShipToCode());
            if (oldInv != null)
            {
                this.errorMsg = getText("B2BPC1627", new String[]{invoice.getHeader().getShipToCode(), invoice.getHeader().getPoNo()});
                flag = true;
            }
        }
        
        if (!flag)
        {
           
            if (PoStatus.OUTDATED.equals(header.getPoStatus()))
            {
                this.errorMsg = getText("B2BPC1631", new String[]{ header.getPoNo() });
                flag = true;
            }
        }
        
        String invNo = invoice.getHeader().getInvNo();
        if (!flag && StringUtils.isBlank(invNo))
        {
            this.errorMsg = getText(B2BPC1612);
            flag = true;
        }
        
        
        int maxInvNoLength = getAllowdInvNoLength();
        if (!flag && invNo.trim().length() > maxInvNoLength)
        {
            this.errorMsg = getText(B2BPC1613,new String[]{String.valueOf(maxInvNoLength)});
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
        Date delvDate = invoice.getHeader().getDeliveryDate() == null ? null : invoice.getHeader().getDeliveryDate();
        
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
        
        
        if (!flag && !(header.getPoSubType2() != null && header.getPoSubType2().equalsIgnoreCase("ZQO")))
        {
            if (!header.getPoType().equals(PoType.CON) && delvDate != null)
            {
                if (date.isAfterDays(poDate, delvDate, 0))
                {
                    this.errorMsg = getText(B2BPC1636);
                    flag = true;
                }
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
    
    
    private void validateSaveCreditNote() throws Exception
    {
        boolean flag = this.hasErrors();
        
        invoice = new InvHolder();
        invoice.conertJsonToInvoice(invHeaderJson, invDetailJson);
        
        PoHeaderHolder header = poHeaderService.selectPoHeaderByKey(invoice.getHeader().getPoOid());
        
        if (!flag)
        {
            CnHeaderHolder oldCn = cnHeaderService.selectEffectiveCnHeaderByPoNo(invoice.getHeader().getBuyerOid(), invoice.getHeader().getSupplierCode(), invoice.getHeader().getPoNo());
            if (oldCn != null)
            {
                this.errorMsg = getText("B2BPC1658", new String[]{invoice.getHeader().getPoNo()});
                flag = true;
            }
        }
        
        if (!flag)
        {
           
            if (PoStatus.OUTDATED.equals(header.getPoStatus()))
            {
                this.errorMsg = getText("B2BPC1631", new String[]{ header.getPoNo() });
                flag = true;
            }
        }
        
        String invNo = invoice.getHeader().getInvNo();
        if (!flag && StringUtils.isBlank(invNo))
        {
            this.errorMsg = getText(B2BPC1612);
            flag = true;
        }
        
        
        int maxInvNoLength = getAllowdInvNoLength();
        if (!flag && invNo.trim().length() > maxInvNoLength)
        {
            this.errorMsg = getText(B2BPC1613,new String[]{String.valueOf(maxInvNoLength)});
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
        Date delvDate = invoice.getHeader().getDeliveryDate() == null ? null : invoice.getHeader().getDeliveryDate();
        
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
        
        
        if (!flag && !(header.getPoSubType2() != null && header.getPoSubType2().equalsIgnoreCase("ZQO")))
        {
            if (!header.getPoType().equals(PoType.CON) && delvDate != null)
            {
                if (date.isAfterDays(poDate, delvDate, 0))
                {
                    this.errorMsg = getText(B2BPC1636);
                    flag = true;
                }
            }
        }
        
        InvHeaderExHolder invHeader = (InvHeaderExHolder)invoice.getHeader();
        if (!flag && !invHeader.isAutoInvNumber())
        {
            CnHeaderHolder param = new CnHeaderHolder();
            param.setCnNo(invoice.getHeader().getInvNo());
            param.setSupplierOid(invHeader.getSupplierOid());
            List<CnHeaderHolder> headers = cnHeaderService.select(param);
            
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
    
    
    protected PoSummaryHolder initSortField(PoSummaryHolder param)
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
    
    
    protected void retrieveStandardReportDetails(ReportEngineParameter<PoHolder> reportParameter)
    {
        Map<Integer, List<PoLocationDetailHolder>> poLocationDetailMap = new HashMap<Integer, List<PoLocationDetailHolder>>();
        for(PoLocationDetailHolder locationDetail : reportParameter.getData()
            .getLocationDetails())
        {
            if(poLocationDetailMap.containsKey(locationDetail
                .getDetailLineSeqNo()))
            {
                poLocationDetailMap.get(locationDetail.getDetailLineSeqNo())
                    .add(locationDetail);
            }
            else
            {
                List<PoLocationDetailHolder> poLocationDetailList = new ArrayList<PoLocationDetailHolder>();
                poLocationDetailList.add(locationDetail);
                poLocationDetailMap.put(locationDetail.getDetailLineSeqNo(),
                    poLocationDetailList);
            }
        }

        Map<Integer, PoDetailHolder> poDetailsMap = new HashMap<Integer, PoDetailHolder>();
        for(PoDetailHolder poDetail : reportParameter.getData().getDetails())
        {
            poDetailsMap.put(poDetail.getLineSeqNo(), poDetail);
        }

        for(PoDetailHolder detail : reportParameter.getData().getDetails())
        {
            if(poLocationDetailMap.containsKey(detail.getLineSeqNo()))
            {
                BigDecimal itemCost = BigDecimal.ZERO;
                BigDecimal orderQty = BigDecimal.ZERO;
                for(PoLocationDetailHolder locationDetail : poLocationDetailMap
                    .get(detail.getLineSeqNo()))
                { //corresponding plural subDateSource record
                    orderQty = orderQty.add(locationDetail.getLocationShipQty());
                    
                    itemCost = itemCost.add(locationDetail.getLocationShipQty()
                        .multiply(
                            detail.getOrderBaseUnit().equals("P") ? detail
                                .getPackCost() : detail.getUnitCost()));

                }
                
                detail.setOrderQty(orderQty);
                detail.setItemCost(itemCost);
            }
        }
    }
    
    
    public void retrievePoLocationDetails(ReportEngineParameter<PoHolder> reportParameter, List<String> buyerStoreList)
    {
        List<PoLocationDetailHolder> locationDetails = reportParameter
            .getData().getLocationDetails();
        Map<String, List<PoLocationDetailHolder>> map = new HashMap<String, List<PoLocationDetailHolder>>();

        for(int j = 0; j < locationDetails.size(); j++)
        { //location line seq no @@ poOid
            String key = locationDetails.get(j).getLocationLineSeqNo() + "@@"
                + locationDetails.get(j).getPoOid();
            if(map.containsKey(key))
            {
                map.get(key).add(locationDetails.get(j));
            }
            else
            {
                List<PoLocationDetailHolder> locationDetailList = new ArrayList<PoLocationDetailHolder>();
                locationDetailList.add(locationDetails.get(j));
                map.put(key, locationDetailList);
            }
        }

        List<PoLocationHolder> poLocations = reportParameter.getData()
            .getLocations();
        reportParameter.getData().getLocationDetails().clear();
        for(Iterator<PoLocationHolder> itr = poLocations.iterator(); itr
            .hasNext();)
        { //delete the location which user has not access
            PoLocationHolder poLocation = itr.next();

            if(!buyerStoreList.contains(poLocation.getLocationCode()))
            {
                itr.remove();
                continue;
            }

            //reconfirm location detail's data.
            if(map.containsKey(poLocation.getLineSeqNo() + "@@"
                + poLocation.getPoOid()))
            {
                reportParameter.getData().getLocationDetails().addAll(
                    map.get(poLocation.getLineSeqNo() + "@@"
                        + poLocation.getPoOid()));
            }

        }
    }
    
    
    protected void retrievePoDetails(ReportEngineParameter<PoHolder> reportParameter)
    {
        List<PoDetailHolder> poDetails = reportParameter.getData().getDetails();
        Map<String, List<PoDetailHolder>> detailMap = new HashMap<String, List<PoDetailHolder>>();

        for(int k = 0; k < poDetails.size(); k++)
        {
            String key = poDetails.get(k).getLineSeqNo() + "@@"
                + poDetails.get(k).getPoOid();

            if(detailMap.containsKey(key))
            {
                detailMap.get(key).add(poDetails.get(k));
            }
            else
            {
                List<PoDetailHolder> poDetailList = new ArrayList<PoDetailHolder>();
                poDetailList.add(poDetails.get(k));
                detailMap.put(key, poDetailList);
            }
        }

        reportParameter.getData().getDetails().clear();
        Map<String, String> mapKey = new HashMap<String, String>();
        for(int n = 0; n < reportParameter.getData().getLocationDetails()
            .size(); n++)
        {
            PoLocationDetailHolder locationDetail = reportParameter.getData()
                .getLocationDetails().get(n);
            String key = locationDetail.getDetailLineSeqNo() + "@@"
                + locationDetail.getPoOid();

            if(mapKey.containsKey(key))
            {
                continue;
            }
            else
            {
                mapKey.put(key, key);
            }

            if(detailMap.containsKey(key))
            {
                reportParameter.getData().getDetails().addAll(
                    detailMap.get(key));
            }
        }
    }

    
    private PoHolder retrievePoHolder(BigDecimal poOid) throws Exception
    {
        PoHolder po = poService.selectPoByKey(poOid);
        List<PoHeaderExtendedHolder> headerExtendeds = poHeaderExtendedService.selectHeaderExtendedByKey(poOid);
        List<PoDetailExtendedHolder> detailExtendeds = poDetailExtendedService.selectDetailExtendedByKey(poOid);
        List<PoLocationDetailExtendedHolder> poLocDetailExtendeds = poLocationDetailExtendedService.selectPoLocationDetailExtendedsByPoOid(poOid);
        
        po.setHeaderExtendeds(headerExtendeds);
        po.setDetailExtendeds(detailExtendeds);
        po.setPoLocDetailExtendeds(poLocDetailExtendeds);
        
        po.setOldPoOid(poOid);
        
        return po;
    }
    
    
    private void setPoOidForPO(PoHolder po) throws Exception
    {
        BigDecimal oid = oidService.getOid();
        po.getPoHeader().setPoOid(oid);
        
        for (PoDetailHolder detail : po.getDetails())
        {
            detail.setPoOid(oid);
        }
        
        for (PoLocationHolder location : po.getLocations())
        {
            location.setPoOid(oid);
        }
        
        for (PoLocationDetailHolder locDetail : po.getLocationDetails())
        {
            locDetail.setPoOid(oid);
        }
        
        if (null != po.getHeaderExtendeds() && !po.getHeaderExtendeds().isEmpty())
        {
            for (PoHeaderExtendedHolder headerEx : po.getHeaderExtendeds())
            {
                headerEx.setPoOid(oid);
            }
        }
        
        if (null != po.getDetailExtendeds() && !po.getDetailExtendeds().isEmpty())
        {
            for (PoDetailExtendedHolder detailEx : po.getDetailExtendeds())
            {
                detailEx.setPoOid(oid);
            }
        }

        if (null != po.getPoLocDetailExtendeds() && !po.getPoLocDetailExtendeds().isEmpty())
        {
            for (PoLocationDetailExtendedHolder locEx : po.getPoLocDetailExtendeds())
            {
                locEx.setPoOid(oid);
            }
        }
        
    }
    
    
    private String generatePdfAfterEdit(PoHolder po, BuyerMsgSettingReportHolder buyerMsgSettingReport, BuyerHolder buyer
        , SupplierHolder supplier) throws Exception
    {
        String template = null;
        
        if (buyerMsgSettingReport.getCustomizedReport())
        {
            template = appConfig.getCustomizedReport(
                buyer.getBuyerCode(), MsgType.PO.name(), po.getPoHeader().getPoType().name(),
                buyerMsgSettingReport.getReportTemplate());
        }
        else
        {
            template = appConfig.getStandardReport(MsgType.PO.name(), po.getPoHeader().getPoType().name(), buyerMsgSettingReport.getReportTemplate());
        }
        @SuppressWarnings("unchecked")
        DefaultReportEngine<PoHolder> engine = ctx.getBean(template, DefaultReportEngine.class);
        
        File docPath = new File(mboxUtil.getFolderInSupplierDocInPath(
            supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                new Date())));
        
        if (!docPath.isDirectory())
        {
            FileUtil.getInstance().createDir(docPath);
        }
        ReportEngineParameter<PoHolder> reportEngineParameter = new ReportEngineParameter<PoHolder>();
        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(po);
        reportEngineParameter.setSupplier(supplier);

        //0 : standard pdf 
        byte[] datas = engine.generateReport(reportEngineParameter, DefaultReportEngine.PDF_TYPE_STANDARD);
        
        String pdfName = "PO_" + po.getPoHeader().getBuyerCode() + "_" + po.getPoHeader().getSupplierCode()
            + "_" + po.getPoHeader().getPoNo() + "_" + po.getPoHeader().getPoOid() + ".pdf";
        
        FileUtil.getInstance().writeByteToDisk(datas, docPath.getPath() + PS + pdfName);
        
        return pdfName;
    }
    
    
    private String generateOriginalForSupplier(PoHolder po, SupplierMsgSettingHolder supplierMsgSetting, SupplierHolder supplier, BigDecimal oldPoOid) throws Exception
    {
        
        String fileName = "PO_" + po.getPoHeader().getBuyerCode() + "_" + po.getPoHeader().getSupplierCode()
            + "_" + po.getPoHeader().getPoNo() + "_" + po.getPoHeader().getPoOid();
        
        File docArchInPath = new File(mboxUtil.getFolderInSupplierArchInPath(supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
            new Date())));
        File docInPath = new File(mboxUtil.getSupplierInPath(supplier.getMboxId()));
        
        if (!docArchInPath.isDirectory())
        {
            FileUtil.getInstance().createDir(docArchInPath);
        }
        if (!docInPath.isDirectory())
        {
            FileUtil.getInstance().createDir(docInPath);
        }
        
        if (DocFileHandler.CANONICAL.equalsIgnoreCase(supplierMsgSetting.getFileFormat()))
        {
            fileName += ".txt";
            docArchInPath = new File(mboxUtil.getFolderInSupplierArchInPath(supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                new Date())) + PS + fileName);
            canonicalPoDocFileHandler.createFile(po, docArchInPath, supplierMsgSetting.getFileFormat());
            
            docInPath = new File(mboxUtil.getSupplierInPath(supplier.getMboxId()) + PS + fileName);
            canonicalPoDocFileHandler.createFile(po, docInPath, supplierMsgSetting.getFileFormat());
        }
        else if (DocFileHandler.EBXML.equalsIgnoreCase(supplierMsgSetting.getFileFormat()))
        {
            fileName += ".xml";
            docArchInPath = new File(mboxUtil.getFolderInSupplierArchInPath(supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                new Date())) + PS + fileName);
            ebxmlPoDocFileHandler.createFile(po, docArchInPath, supplierMsgSetting.getFileFormat());
            
            docInPath = new File(mboxUtil.getSupplierInPath(supplier.getMboxId()) + PS + fileName);
            ebxmlPoDocFileHandler.createFile(po, docInPath, supplierMsgSetting.getFileFormat());
        }
        else if (DocFileHandler.FP_IDOC.equalsIgnoreCase(supplierMsgSetting.getFileFormat()))
        {
            fileName += ".xml";
            po.setOldPoOid(oldPoOid);
            
            docArchInPath = new File(mboxUtil.getFolderInSupplierArchInPath(supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
                new Date())) + PS + fileName);
            idocPoDocFileHandler.createFile(po, docArchInPath, supplierMsgSetting.getFileFormat());
            
            docInPath = new File(mboxUtil.getSupplierInPath(supplier.getMboxId()) + PS + fileName);
            idocPoDocFileHandler.createFile(po, docInPath, supplierMsgSetting.getFileFormat());
        }
        return fileName;
    }
    
    
    private BatchMsg initBatchMsg(MsgTransactionsHolder msg, BuyerHolder buyer, 
        SupplierHolder supplier, PoHolder po)
    {
        BatchMsg batch = new BatchMsg();
        batch.setBatchType(BatchType.PO);
        batch.setInDate(new Date());
        batch.setSenderOid(buyer.getBuyerOid());
        batch.setSenderCode(buyer.getBuyerCode());
        batch.setSenderName(buyer.getBuyerName());
        batch.setBuyer(buyer);
        batch.setSupplier(supplier);
        
        PoDocMsg doc = new PoDocMsg();
        doc.setData(po);
        doc.setDocOid(msg.getDocOid());
        doc.setRefNo(msg.getMsgRefNo());
        doc.setSenderOid(msg.getBuyerOid());
        doc.setSenderCode(msg.getBuyerCode());
        doc.setSenderName(msg.getBuyerName());
        doc.setReceiverOid(msg.getSupplierOid());
        doc.setReceiverCode(msg.getSupplierCode());
        doc.setReceiverName(msg.getSupplierName());
        doc.setInDate(new Date());
        doc.setProcDate(msg.getProcDate());
        doc.setSentDate(msg.getSentDate());
        doc.setOutDate(msg.getOutDate());
        doc.setAlertDate(msg.getAlertDate());
        doc.setOriginalFilename(msg.getOriginalFilename());
        doc.setTargetFilename(msg.getExchangeFilename());
        doc.setReportFilename(msg.getReportFilename());
        doc.setRemarks(msg.getRemarks());
        doc.setValid(msg.getValid());
        doc.setActive(msg.getActive());
        doc.setAmended(true);
        doc.setBuyer(buyer);
        doc.setSupplier(supplier);
        doc.setBatch(batch);
        
        List<DocMsg> docs = new ArrayList<DocMsg>();
        docs.add(doc);
        batch.setDocs(docs);

        return batch;
    }
    
    
//    private void alertBuyer(BatchMsg batch, BuyerMsgSettingHolder buyerMsgSetting,
//        Date originalDateFrom, Date newDateFrom, Date originalDateTo, Date newDateTo) throws Exception
//    {
//        if (null == buyerMsgSetting || null == buyerMsgSetting.getRcpsAddrs()
//            || buyerMsgSetting.getRcpsAddrs().trim().isEmpty())
//        {
//            return;
//        }
//        
//        String subject = "PO Delivery Date Modified";
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("PO_NO", batch.getDocs().get(0).getRefNo());
//        map.put("BUYER_NAME", batch.getSenderName());
//        map.put("BUYER_CODE", batch.getSenderCode());
//        if (originalDateFrom == null)
//        {
//            map.put("O_DELIVERY_DATE_FROM", "");
//        }
//        else
//        {
//            map.put("O_DELIVERY_DATE_FROM", DateUtil.getInstance().convertDateToString(originalDateFrom, DateUtil.DATE_FORMAT_DDMMYYYY));
//        }
//        if (newDateFrom == null)
//        {
//            map.put("N_DELIVERY_DATE_FROM", "");
//        }
//        else
//        {
//            map.put("N_DELIVERY_DATE_FROM", DateUtil.getInstance().convertDateToString(newDateFrom, DateUtil.DATE_FORMAT_DDMMYYYY));
//        }
//        if (originalDateTo == null)
//        {
//            map.put("O_DELIVERY_DATE_TO", "");
//        }
//        else
//        {
//            map.put("O_DELIVERY_DATE_TO", DateUtil.getInstance().convertDateToString(originalDateTo, DateUtil.DATE_FORMAT_DDMMYYYY));
//        }
//        if (newDateTo == null)
//        {
//            map.put("N_DELIVERY_DATE_TO", "");
//        }
//        else
//        {
//            map.put("N_DELIVERY_DATE_TO", DateUtil.getInstance().convertDateToString(newDateTo, DateUtil.DATE_FORMAT_DDMMYYYY));
//        }
//        
//        boolean emailToStore = businessRuleService.isPoConvertInvEmailToStore(batch.getSenderOid());
//        
//        String email = "";
//        if (emailToStore)
//        {
//            List<BuyerStoreHolder>  stores = buyerStoreService.selectBuyerStoresByBuyer(batch.getDocs().get(0).getSenderCode());
//            StringBuffer storeEmail = new StringBuffer();
//            for (BuyerStoreHolder store : stores )
//            {
//                //email not provider to skip
//                if(null == store.getContactEmail() || store.getContactEmail().isEmpty())
//                {
//                    continue;
//                }
//                storeEmail.append(CoreCommonConstants.COMMA_DELIMITOR);
//                storeEmail.append(store.getContactEmail());
//            }
//            
//            email = storeEmail.toString();
//        }
//        
//        if (null != email && !email.isEmpty())
//        {
//            buyerMsgSetting.setRcpsAddrs(buyerMsgSetting.getRcpsAddrs() + email);
//        }
//        String[] emailTo = buyerMsgSetting.retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
//        emailEngine.sendHtmlEmail( emailTo, subject,"ALERT_DELIVERY_DATE_BUYER.vm", map);
//        
//    }
    
    
    private void alertSupplier(BatchMsg batch) throws Exception
    {
        Map<String, List<DocMsg>> group = this.groupBySupplierAndType(batch
            .getDocs());
        for (Map.Entry<String, List<DocMsg>> entry : group.entrySet())
        {
            int delimitorIndex = entry.getKey().indexOf(DELIMITOR);
    
            BigDecimal supplierOid = new BigDecimal(entry.getKey().substring(0,
                    delimitorIndex));
            SupplierHolder supplier = supplierService
                    .selectSupplierByKey(supplierOid);
            String type = entry.getKey().substring(delimitorIndex + 1);
    
            SupplierMsgSettingHolder setting = supplierMsgSettingService
                    .selectByKey(supplierOid, type);
            if (null == setting || null == setting.getRcpsAddrs()
                    || setting.getRcpsAddrs().trim().isEmpty())
            {
                continue;
            }
            ControlParameterHolder cp = controlParameterService.selectCacheControlParameterBySectIdAndParamId(
                CoreCommonConstants.SECT_ID_CTRL, CoreCommonConstants.PARAM_ID_HELPDESK_NO);
            
            String[] emailTo = setting.retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
            String subject = "Arrival of "+ batch.getBatchType() +"(s)-" + batch.getSenderName() + "(" + batch.getSenderCode() + ")";
            
            if (batch.calculateAmendedDocs() > 0)
            {
                subject = "Amendment of "+ batch.getBatchType() +"(s)-" + batch.getSenderName() + "(" + batch.getSenderCode() + ")";
            }
            
            Map<String, Object> map = initOutboundAlertSupplierMap(entry.getValue());
            map.put("SUPPLIER_NAME", supplier.getSupplierName());
            map.put("BUYER_GIVEN_SUPPLIER_CODE", supplier.getSupplierCode());
            map.put("BUYER_NAME", batch.getSenderName());
            map.put("BUYER_CODE", batch.getSenderCode());
            map.put("DOC_TYPE", batch.getBatchType());
            map.put("CONTACT_NO", cp.getStringValue());
            emailEngine.sendHtmlEmail(emailTo, subject, "ALERT_OUTBOUND_SUPPLIER.vm", map);
        }
    }
    
    
    private Map<String, Object> initOutboundAlertSupplierMap(List<DocMsg> list)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DocMsg> succList = new ArrayList<DocMsg>();
        if (list != null)
        {
            Iterator<DocMsg> it = list.iterator();
            while (it.hasNext())
            {
                DocMsg obj = (DocMsg) it.next();
                if (obj.isValid() && obj.isActive())
                {
                    succList.add(obj);
                }
            }
        }
        map.put("SUCC_LIST", succList);
        return map;
    }
    
    
    private Map<String, List<DocMsg>> groupBySupplierAndType(List<DocMsg> docs)
    {
        Map<String, List<DocMsg>> rlt = new HashMap<String, List<DocMsg>>();

        for (DocMsg doc : docs)
        {
            if (!(doc.isValid() && doc.isActive()))
            {
                continue;
            }
            if (DeploymentMode.REMOTE.equals(doc.getSupplier()
                    .getDeploymentMode()))
            {
                continue;
            }

            String key = doc.getReceiverOid() + DELIMITOR + doc.getMsgType();

            if (rlt.containsKey(key))
            {
                rlt.get(key).add(doc);
            }
            else
            {
                List<DocMsg> list = new ArrayList<DocMsg>();
                list.add(doc);
                rlt.put(key, list);
            }
        }

        return rlt;
    }
    
    
    protected PoSummaryHolder initSearchParameter()throws Exception
    {
        PoSummaryHolder searchParam = (PoSummaryHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_PO);
        
        if (searchParam == null)
        {
            searchParam = new PoSummaryHolder();
            
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_PO, searchParam);
        }
        
        searchParam = initSortField(searchParam);
        
        initCurrentUserSearchParam(searchParam);
        searchParam.setQuickPoFlag(false);
        
        searchParam.trimAllString();
        searchParam.setAllEmptyStringToNull();
        
        return searchParam;
    }
    
    
    protected int getAllowdInvNoLength()
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
    
    
    private InvDetailExtendedHolder initInvDetailExByPoDetailEx(PoDetailExtendedHolder poDetailEx, int lineSeqNo, BigDecimal oid)
    {
        InvDetailExtendedHolder invDetailEx = new InvDetailExtendedHolder();
        invDetailEx.setInvOid(oid);
        invDetailEx.setFieldName(poDetailEx.getFieldName());
        invDetailEx.setFieldType(poDetailEx.getFieldType());
        invDetailEx.setLineSeqNo(lineSeqNo);
        invDetailEx.setBoolValue(poDetailEx.getBoolValue());
        invDetailEx.setStringValue(poDetailEx.getStringValue());
        invDetailEx.setFloatValue(poDetailEx.getFloatValue());
        invDetailEx.setDateValue(poDetailEx.getDateValue());
        invDetailEx.setIntValue(poDetailEx.getIntValue());
        
        return invDetailEx;
    }
    
    
    private void initInvDetailExtendeds()throws Exception
    {
        List<PoDetailExtendedHolder> detailExs = poDetailExtendedService.selectDetailExtendedByKey(invoice.getHeader().getPoOid());
        Map<Integer, List<PoDetailExtendedHolder>> map = new HashMap<Integer, List<PoDetailExtendedHolder>>();
        if (detailExs != null && !detailExs.isEmpty())
        {
            for (PoDetailExtendedHolder detailEx : detailExs)
            {
                if (map.containsKey(detailEx.getLineSeqNo()))
                {
                    map.get(detailEx.getLineSeqNo()).add(detailEx);
                }
                else
                {
                    List<PoDetailExtendedHolder> detailExtends = new ArrayList<PoDetailExtendedHolder>();
                    detailExtends.add(detailEx);
                    map.put(detailEx.getLineSeqNo(), detailExtends);
                }
            }
        }
        
        List<InvDetailExtendedHolder> invDetailExs = new ArrayList<InvDetailExtendedHolder>();
        for (InvDetailHolder invDetail : invoice.getDetails())
        {
            if (map.containsKey(invDetail.getLineSeqNo()))
            {
                for(PoDetailExtendedHolder poDetailEx : map.get(invDetail.getLineSeqNo()))
                {
                    if (poDetailEx.getFieldName().equalsIgnoreCase(EX_FIELD_PER_START_DATE))
                    {
                        InvDetailExtendedHolder invDetailExPeriodStartDate = this
                            .initInvDetailExByPoDetailEx(poDetailEx,invDetail.getLineSeqNo(), invoice
                            .getHeader().getInvOid());
                        invDetailExs.add(invDetailExPeriodStartDate);
                    }
                    
                    if (poDetailEx.getFieldName().equalsIgnoreCase(EX_FIELD_PER_END_DATE))
                    {
                        InvDetailExtendedHolder invDetailExPeriodEndDate = this
                            .initInvDetailExByPoDetailEx(poDetailEx,invDetail.getLineSeqNo(), invoice
                            .getHeader().getInvOid());
                        invDetailExs.add(invDetailExPeriodEndDate);
                    }
                }
            }
        }
        
        if (!invDetailExs.isEmpty())
        {
            invoice.setDetailExtendeds(invDetailExs);
        }
        
    }
    
    
    private void initInvHeaderExtendedsByPoHeader(PoHeaderHolder poHeader)throws Exception
    {
        List<InvHeaderExtendedHolder> invHeaderExs = new ArrayList<InvHeaderExtendedHolder>();
        if (poHeader != null)
        {
            if (poHeader.getPeriodStartDate() != null)
            {
                InvHeaderExtendedHolder invHeaderEx = new InvHeaderExtendedHolder(
                    invoice.getHeader().getInvOid(), EX_FIELD_PER_START_DATE,
                    EXTENDED_TYPE_DATE, null, poHeader.getPeriodStartDate(),
                    null, null, null);

                invHeaderExs.add(invHeaderEx);
            }
            
            if (poHeader.getPeriodEndDate() != null)
            {
                InvHeaderExtendedHolder invHeaderEx = new InvHeaderExtendedHolder(
                    invoice.getHeader().getInvOid(), EX_FIELD_PER_END_DATE,
                    EXTENDED_TYPE_DATE, null, poHeader.getPeriodEndDate(),
                    null, null, null);
                
                invHeaderExs.add(invHeaderEx);
            }
        }
        
        if (!invHeaderExs.isEmpty())
        {
            invoice.setHeaderExtendeds(invHeaderExs);
        }
    }
    // *****************************************************
    // getter and setter method
    // *****************************************************
    public PoSummaryHolder getParam()
    {
        return param;
    }


    public void setParam(PoSummaryHolder param)
    {
        this.param = param;
    }


    public String getErrorMsg()
    {
        return errorMsg;
    }


    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }


    public String getPoExpiredMsg()
    {
        return poExpiredMsg;
    }


    public void setPoExpiredMsg(String poExpiredMsg)
    {
        this.poExpiredMsg = poExpiredMsg;
    }


    public PoHeaderHolder getSelectedPo()
    {
        return selectedPo;
    }


    public void setSelectedPo(PoHeaderHolder selectedPo)
    {
        this.selectedPo = selectedPo;
    }


    public String getDocOid()
    {
        return docOid;
    }


    public void setDocOid(String docOid)
    {
        this.docOid = docOid;
    }


    public List<PoLocationHolder> getLocations()
    {
        return locations;
    }


    public void setLocations(List<PoLocationHolder> locations)
    {
        this.locations = locations;
    }


    public String getStore()
    {
        return store;
    }


    public void setStore(String store)
    {
        this.store = store;
    }


    public PoLocationHolder getSelectedPoLocation()
    {
        return selectedPoLocation;
    }


    public void setSelectedPoLocation(PoLocationHolder selectedPoLocation)
    {
        this.selectedPoLocation = selectedPoLocation;
    }


    public InvHolder getInvoice()
    {
        return invoice;
    }


    public void setInvoice(InvHolder invoice)
    {
        this.invoice = invoice;
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


    public String getSuccess()
    {
        return success;
    }


    public void setSuccess(String success)
    {
        this.success = success;
    }


    public Integer getStoreCount()
    {
        return storeCount;
    }


    public void setStoreCount(Integer storeCount)
    {
        this.storeCount = storeCount;
    }


    public String getBusinessRulesJson()
    {
        return businessRulesJson;
    }


    public void setBusinessRulesJson(String businessRulesJson)
    {
        this.businessRulesJson = businessRulesJson;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
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


    public String getPdfMode()
    {
        return pdfMode;
    }


    public void setPdfMode(String pdfMode)
    {
        this.pdfMode = pdfMode;
    }


    public PoHeaderHolder getEditPo()
    {
        return editPo;
    }


    public void setEditPo(PoHeaderHolder editPo)
    {
        this.editPo = editPo;
    }


    public String getDateError()
    {
        return dateError;
    }


    public void setDateError(String dateError)
    {
        this.dateError = dateError;
    }


    public Map<String, String> getPoSubTypes()
    {
        return poSubTypes;
    }


    public void setPoSubTypes(Map<String, String> poSubTypes)
    {
        this.poSubTypes = poSubTypes;
    }


    public boolean isSelectAll()
    {
        return selectAll;
    }


    public void setSelectAll(boolean selectAll)
    {
        this.selectAll = selectAll;
    }


    public String getTypePrint()
    {
        return typePrint;
    }


    public void setTypePrint(String typePrint)
    {
        this.typePrint = typePrint;
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
