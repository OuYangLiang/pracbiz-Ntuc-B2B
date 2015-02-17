package com.pracbiz.b2bportal.core.action;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






import net.sf.json.JSONArray;
import net.sf.json.JSONObject;






import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;






import com.pracbiz.b2bportal.base.action.GridResult;
import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.constants.SummaryPageAccessType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoSummaryHolder;
import com.pracbiz.b2bportal.core.report.DefaultPoReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;


public class QuickPurchaseOrderAction extends PurchaseOrderAction
{
    private static final long serialVersionUID = -1335758748187477394L;
    
    private static final Logger log = LoggerFactory.getLogger(QuickPurchaseOrderAction.class);
    
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.PurchaseOrderAction.selectedOids";
    private static final String IS_EDITTING_INVOICE_NO = "isEditting";
    private static final String VLD_PTN_KEY_INVOICE_NO = "INV_NO";
    private static final String B2BPC1613 = "B2BPC1613";
    private static final String B2BPC1620 = "B2BPC1620";
    private static final String B2BPC1627 = "B2BPC1627";
    private static final String B2BPC1647 = "B2BPC1647";
    private static final String B2BPC1648 = "B2BPC1648";
    private static final String B2BPC1649 = "B2BPC1649";
    private static final String B2BPC1650 = "B2BPC1650";
    private static final String B2BPC1651 = "B2BPC1651";
    private static final String B2BPC1656 = "B2BPC1656";
    private static final String B2BPC1702 = "B2BPC1702";
    private static final String B2BPC1703 = "B2BPC1703";
    private static final String B2BPC1704 = "B2BPC1704";
    private static final String B2BPC1706 = "B2BPC1706";
    private static final String FORWORD_ACK_MESSAGE = "ackMsg";
    
    private PoHeaderExtendedHolder editPoHex;
    
    private List<InvHolder> invoiceList;
    private String op;
    private boolean selectType;
    private List<String> successes;
    private List<String> faileds;
    private String docType;
    private String invNo;
    private String maxInvNoLength;
    private boolean selectAll;
    
    
    public QuickPurchaseOrderAction()
    {
        this.initMsg();
        this.setPageId(PageId.P011.name());
    }
    
    public String init()
    {
        super.init();
        if (docStatuses.containsKey(PoStatus.PARTIAL_INVOICED.name()))
        {
            docStatuses.remove(PoStatus.PARTIAL_INVOICED.name());
        }
        
        return SUCCESS;
    }
    
    
    public String putParamIntoSession()
    {
        this.getSession().put(SESSION_OID_PARAMETER,
            this.getRequest().getParameter(REQUEST_PARAMTER_OID));
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            PoSummaryHolder searchParam = this.initSearchParameter();
            searchParam.setQuickPoFlag(true);
            this.obtainListRecordsOfPagination(poHeaderService, searchParam, MODULE_KEY_PO);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }

    public String generateInvForSor()
    {
        try
        {
            BigDecimal poOid = new BigDecimal(docOid);
            PoHolder po = poService.selectPoByKey(poOid);

            BigDecimal supplierOid = po.getPoHeader().getSupplierOid();
            SupplierHolder supplier = supplierService
                    .selectSupplierByKey(supplierOid);
            PoHeaderHolder poHeader = po.getPoHeader();

            TradingPartnerHolder tradingPartner = tradingPartnerService
                    .selectByBuyerAndBuyerGivenSupplierCode(poHeader.getBuyerOid(),
                    poHeader.getSupplierCode());
            TermConditionHolder term = null;
            if(tradingPartner.getTermConditionOid() == null)
            {
                term = termConditionService
                    .selectDefaultTermConditionBySupplierOid(supplierOid);
            }
            else
            {
                term = termConditionService
                    .selectTermConditionByKey(tradingPartner
                    .getTermConditionOid());
            }
            
            List<PoHeaderExtendedHolder> poHeaderExs = poHeaderExtendedService.selectHeaderExtendedByKey(poOid);
            if (poHeaderExs != null && !poHeaderExs.isEmpty() && !supplier.getAutoInvNumber())
            {
                for (PoHeaderExtendedHolder poHeaderEx : poHeaderExs)
                {
                    if (poHeaderEx.getFieldName().equalsIgnoreCase("BNAME"))
                    {
                        invNo = poHeaderEx.getStringValue();
                    }
                }
            }

            if(StringUtils.isBlank(invNo))
            {
                invNo = getAutoInvNo(supplier);
            }
            BigDecimal oid = this.oidService.getOid();
            invoice = po.toInvoice(supplier, invNo, store, term, storeCount,
                oid);

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
            businessRulesJson = this.replaceSpecialCharactersForJson(rules
                .toString());
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }

        return SUCCESS;
    }
    
    
    //**************************
    //Batch Generator Invoice
    //**************************
    public String checkInitBatchGenerateInv()
    {
        try
        {
            boolean allEmpty = true;
            PoHolder po = null;
            List<PoHeaderExtendedHolder> headerExs = null;
            StringBuffer sb = new StringBuffer();
            if (selectType)
            {
                PoSummaryHolder searchParam = this.initSearchParameter();
                searchParam.setQuickPoFlag(true);
                int count = poHeaderService.getCountOfSummary(searchParam);
                searchParam.setStartRecordNum(0);
                searchParam.setNumberOfRecordsToSelect(count);
                List<BaseHolder> list= poHeaderService.getListOfSummaryQuickPo(searchParam);
                
                for (BaseHolder lst : list)
                {
                    PoSummaryHolder poSummary = (PoSummaryHolder)lst;
                    if (poSummary.getInvoiceNo() != null && !poSummary.getInvoiceNo().isEmpty())
                    {
                        allEmpty = false;
                    }
                    if (poSummary.getPoStatus().equals(PoStatus.OUTDATED))
                    {
                        errorMsg = this.getText(B2BPC1650);
                        this.getSession().remove(SESSION_OID_PARAMETER);
                        return INPUT;
                    }
                    if (poSummary.getPoStatus().equals(PoStatus.INVOICED))
                    {
                        errorMsg = this.getText(B2BPC1647);
                        this.getSession().remove(SESSION_OID_PARAMETER);
                        return INPUT;
                    }
                    sb.append(poSummary.getPoOid()).append("-");
                }
            }
            else
            {
                Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
                if(null == selectedOids)
                {
                    throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                }
                String[] parts = selectedOids.toString().split(
                    REQUEST_OID_DELIMITER);
                for (int i = 0 ; i < parts.length; i++)
                {
                    BigDecimal poOid = new BigDecimal(parts[i]);
                    po = poService.selectPoByKey(poOid);
                    headerExs = poHeaderExtendedService.selectHeaderExtendedByKey(poOid);
                    for (PoHeaderExtendedHolder headerEx : headerExs)
                    {
                        if (headerEx.getFieldName().equalsIgnoreCase("BNAME") && headerEx.getStringValue() != null && !headerEx.getStringValue().trim().isEmpty())
                        {
                            invNo = headerEx.getStringValue();
                            allEmpty = false;
                        }
                    }
                    if (po.getPoHeader().getPoStatus().equals(PoStatus.OUTDATED))
                    {
                        errorMsg = this.getText(B2BPC1650);
                        this.getSession().remove(SESSION_OID_PARAMETER);
                        return INPUT;
                    }
                    if (po.getPoHeader().getPoStatus().equals(PoStatus.INVOICED))
                    {
                        errorMsg = this.getText(B2BPC1647);
                        this.getSession().remove(SESSION_OID_PARAMETER);
                        return INPUT;
                    }
                }
            }
            
            if (allEmpty)
            {
               errorMsg = this.getText(B2BPC1648);
               this.getSession().remove(SESSION_OID_PARAMETER);
               return INPUT;
            }
            
            if (sb != null && !sb.toString().isEmpty())
            {
                this.getSession().put(SESSION_OID_PARAMETER, sb.toString());
            }
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }
        
        return SUCCESS;
        
    }
    
    
    public String initBatchGenerateInv()
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
            invoiceList = new ArrayList<InvHolder>();
            
            for (int i = 0 ; i < parts.length; i++)
            {
                BigDecimal poOid = new BigDecimal(parts[i]);
                
                PoHolder po = poService.selectPoByKey(poOid);
                List<PoHeaderExtendedHolder> headerExs = poHeaderExtendedService.selectHeaderExtendedByKey(poOid);
                SupplierHolder supplier = supplierService.selectSupplierByKey(po.getPoHeader().getSupplierOid());
                BigDecimal oid = this.oidService.getOid();
                TradingPartnerHolder tradingPartner = tradingPartnerService
                    .selectByBuyerAndBuyerGivenSupplierCode(po.getPoHeader().getBuyerOid(),
                        po.getPoHeader().getSupplierCode());
                TermConditionHolder term = null;
                if (tradingPartner.getTermConditionOid() == null)
                {
                    term = termConditionService.selectDefaultTermConditionBySupplierOid(supplier.getSupplierOid());
                }
                else
                {
                    term = termConditionService.selectTermConditionByKey(tradingPartner.getTermConditionOid());
                }

                invoice = po.toInvoice(supplier, null, po
                    .getLocations().get(0).getLocationCode(), term, po
                    .getLocations().size(), oid);
                
                if (headerExs != null)
                {
                    for (PoHeaderExtendedHolder headerEx : headerExs)
                    {
                        if (headerEx.getFieldName().equalsIgnoreCase("BNAME"))
                        {
                            invoice.getHeader().setInvNo(headerEx.getStringValue());
                        }
                    }
                }
                invoiceList.add(invoice);
            }
        }
        catch(Exception e)
        {
            this.handleExceptionForNoActionResult(e);
        }

        return SUCCESS;
    }
    
    
    public String saveBatchGenerateInv()
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
            
            successes = new ArrayList<String>();
            faileds = new ArrayList<String>();
            
            for (int i = 0 ; i < parts.length; i++)
            {
                BigDecimal poOid = new BigDecimal(parts[i]);
                
                PoHolder po = poService.selectPoByKey(poOid);
                List<PoHeaderExtendedHolder> headerExs = poHeaderExtendedService.selectHeaderExtendedByKey(poOid);
                SupplierHolder supplier = supplierService.selectSupplierByKey(po.getPoHeader().getSupplierOid());
                BigDecimal oid = this.oidService.getOid();
                TradingPartnerHolder tradingPartner = tradingPartnerService
                    .selectByBuyerAndBuyerGivenSupplierCode(po.getPoHeader().getBuyerOid(),
                        po.getPoHeader().getSupplierCode());
                TermConditionHolder term = null;
                if (tradingPartner.getTermConditionOid() == null)
                {
                    term = termConditionService.selectDefaultTermConditionBySupplierOid(supplier.getSupplierOid());
                }
                else
                {
                    term = termConditionService.selectTermConditionByKey(tradingPartner.getTermConditionOid());
                }
                invoice = po.toInvoice(supplier, null, po
                    .getLocations().get(0).getLocationCode(), term, po
                    .getLocations().size(), oid);
                
                for (PoHeaderExtendedHolder headerEx : headerExs)
                {
                    if (headerEx.getFieldName().equalsIgnoreCase("BNAME"))
                    {
                        invoice.getHeader().setInvNo(headerEx.getStringValue());
                    }
                }
                
                boolean validateFailed = this.validateInvoice(invoice, faileds);
                if (validateFailed)
                {
                    continue;
                }
                
                if (op.equalsIgnoreCase("CS"))
                {
                    this.createAndSentInvoice(invoice);
                }
                else
                {
                    this.saveInvoice(invoice);
                }
                
                successes.add(this.getText(B2BPC1620, new String[]{invoice.getHeader().getPoNo(), invoice.getHeader().getInvNo()}));
            }
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return FORWORD_ACK_MESSAGE;
    }
   
   
    
    //************************************
    //Export Summary Excel
    //************************************
    public String exportSummaryExcel()
    {
        try
        {
            PoSummaryHolder searchParam = this.initSearchParameter();
            searchParam.setQuickPoFlag(true);
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
            rptFileName = "QUICK_PO_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    //*******************************
    //Edit Invoice No
    //*******************************
    @SuppressWarnings("unchecked")
    public String initEditInvoiceNo()
    {
        try
        {
            List<String> urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
            if (!urls.contains("/quickPo/initEditInvoiceNo.action"))
            {
                errorMsg = this.getText("message.exception.no.privilege"); //noPrivilege
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
                errorMsg = this.getText(B2BPC1702);//"notExist";
                return SUCCESS;
            }
            if (editPo.getPoStatus().equals(PoStatus.INVOICED) || editPo.getPoStatus().equals(PoStatus.PARTIAL_INVOICED))
            {
                errorMsg = this.getText(B2BPC1703);//"invoiced";
                return SUCCESS;
            }
            if (editPo.getPoStatus().equals(PoStatus.CANCELLED) || editPo.getPoStatus().equals(PoStatus.CANCELLED_INVOICED))
            {
                errorMsg = this.getText(B2BPC1704); //"cancelled";
                return SUCCESS;
            }
            if (editPo.getPoStatus().equals(PoStatus.OUTDATED))
            {
                errorMsg = this.getText(B2BPC1706);//"outdated";
                return SUCCESS;
            }
            
            List<PoHeaderExtendedHolder> poHexs = poHeaderExtendedService.selectHeaderExtendedByKey(new BigDecimal(oid));
            for (PoHeaderExtendedHolder poHex : poHexs)
            {
                if (poHex.getFieldName().equalsIgnoreCase("BNAME"))
                {
                    editPoHex = poHex;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String saveEditInvoiceNo()
    {
        try
        {
            if ("yes".equals((String)this.getSession().get(IS_EDITTING_INVOICE_NO)))
            {
                errorMsg = "isEditting";
                return INPUT;
            }
            final PoHeaderHolder header = poHeaderService.selectPoHeaderByKey(editPoHex.getPoOid());
            if (null == header)
            {
                errorMsg = "notExist";
                return INPUT;
            }
            Pattern pattern = Pattern.compile(validationConfig.getCachePattern(VLD_PTN_KEY_INVOICE_NO));
            Matcher matcher = pattern.matcher(editPoHex.getStringValue());
            if (!matcher.matches())
            {
                this.errorMsg = "invNoReg";
                return INPUT;
            }
            
            InvHeaderHolder inv = invHeaderService
                .selectEffectiveInvHeaderByInNo(header.getBuyerOid(),
                    header.getSupplierCode(), editPoHex.getStringValue());
            if (inv != null)
            {
                errorMsg = "invExist";
                return INPUT;
            }
            
            
            int maxInvNoLen = this.getAllowdInvNoLength();
            
            if (editPoHex.getStringValue().trim().length() > maxInvNoLen)
            {
                errorMsg = "invNoExceed"; 
                maxInvNoLength = String.valueOf(maxInvNoLen);
                return INPUT;
            }
            
            this.getSession().put(IS_EDITTING_INVOICE_NO, "yes");
            boolean exists = false;
            CommonParameterHolder cp = this.getCommonParameter();
            PoHeaderExtendedHolder newHex = new PoHeaderExtendedHolder();
            PoHeaderExtendedHolder oldHex = new PoHeaderExtendedHolder();
            List<PoHeaderExtendedHolder> poHexs = poHeaderExtendedService.selectHeaderExtendedByKey(editPoHex.getPoOid());
            
            for (PoHeaderExtendedHolder poHex : poHexs)
            {
                if (poHex.getFieldName().equalsIgnoreCase("BNAME"))
                {
                    exists = true;
                    BeanUtils.copyProperties(poHex, newHex);
                    BeanUtils.copyProperties(poHex, oldHex);
                    newHex.setStringValue(editPoHex.getStringValue());
                    break;
                }
            }
            if (!exists)
            {
                newHex.setPoOid(editPoHex.getPoOid());
                newHex.setFieldName("BNAME");
                newHex.setFieldType("S");
                newHex.setStringValue(editPoHex.getStringValue());
                poHeaderExtendedService.auditInsert(cp, newHex);
            }
            else
            {
                poHeaderExtendedService.auditUpdateByPrimaryKeySelective(cp, oldHex, newHex);
            }
            this.getSession().remove(IS_EDITTING_INVOICE_NO);
        }
        catch (Exception e)
        {
            this.getSession().remove(IS_EDITTING_INVOICE_NO);
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    //*************************
    //Print Pdf
    //*************************
    public String printPdf()
    {

        try
        {
            String[] files = null;
            InputStream [] inputs= null;
            int flag = DefaultReportEngine.PDF_TYPE_STANDARD;
            
            if (selectAll)
            {
                param = (PoSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_PO);
                
                if (param == null)
                {
                    super.initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_PO, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<PoSummaryHolder> poList = poHeaderService.selectAllRecordToExport(param);
                
                if (poList != null && !poList.isEmpty())
                {
                    files = new String[poList.size()];
                    inputs= new InputStream[poList.size()];
                    for (int i = 0; i < poList.size(); i++)
                    {
                        PoSummaryHolder po = poList.get(i);
                        
                        flag = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                        
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(po.getDocOid());
                        SupplierHolder supplier = supplierService.selectSupplierByKey(msg.getSupplierOid());
                        
                        rptFileName = this.computePdfFilename(msg.getReportFilename(), "I");
                        
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
                        
                        //check buyerAdmin,buyerUser,storeAdmin and storeUser's locaion if accessed 
                        if (this.isBuyerCompanyUser() && !"INV".equalsIgnoreCase(docType))
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
                    
                    if ("INV".equalsIgnoreCase(docType))
                    {
                        InvHeaderHolder inv = invHeaderService.selectEffectiveInvHeaderByInNo(poHeader.getBuyerOid(), poHeader.getSupplierCode(), invNo);
                        docOid = inv.getInvOid().toString();
                        
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
                        
                        //0 : standard pdf ,1 : for store user by item ,2 :  for supplier user pdf, 3: for store user by store.
                        if (null == poReportEngine) poReportEngine = retrieveEngine(buyerMsgSettingReport, msg.getBuyerCode());
                        if (null == reportParameter) reportParameter = poService.retrieveParameter(msg, supplier);
                        byte[] datas = poReportEngine.generateReport(reportParameter, flag);
                        
                        FileUtil.getInstance().writeByteToDisk(datas, file.getPath());
                        
                    }
                    
                    this.updateReadStatus(msg);
                    
                    
                    files[i] = file.getPath();
                    
                    //check buyerAdmin,buyerUser,storeAdmin and storeUser's locaion if accessed 
                    if (this.isBuyerCompanyUser() && !"INV".equalsIgnoreCase(docType))
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
          
            if (this.isBuyerCompanyUser())
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
            
            String[] partString = parts[0].split("/");
            BigDecimal poOid = new BigDecimal(partString[0]);
            
            if ("INV".equalsIgnoreCase(docType))
            {
                PoHeaderHolder po = poHeaderService.selectPoHeaderByKey(poOid);
                InvHeaderHolder invoice = null;
                
                if (invNo != null && !invNo.isEmpty())
                {
                    invoice = invHeaderService.selectEffectiveInvHeaderByInNo(po.getBuyerOid(), po.getSupplierCode(), invNo);
                }
                else
                {
                    errorMsg = this.getText("B2BPC1657");
                    return INPUT;
                }
                
                if (invoice == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"INV", invNo});
                    return INPUT;
                }
                
                if(BigDecimal.valueOf(2).equals(this.getUserTypeOfCurrentUser())
                    || BigDecimal.valueOf(4).equals(this.getUserTypeOfCurrentUser()))
                {
                    if (!invoice.getInvStatus().equals(InvStatus.SUBMIT))
                    {
                        errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"INV", invoice.getInvNo()});;
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
   
    
    //*****************************************************
    // private method
    //*****************************************************
    protected void obtainListRecordsOfPagination(PaginatingService<?> service_,
        MsgTransactionsExHolder param, String moduleKey) throws Exception
    {
        String accessType = getAccessType();
        Map<String, String> sortMap = summaryFieldService
            .selectSortFieldByPageIdAndAccessType(pageId, accessType, this);
        this.obtainListRecordsOfPagination(service_, param, sortMap, "docOid", moduleKey);
    }
    
    

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void obtainListRecordsOfPagination(PaginatingService service_,
        BaseHolder param, Map<String, String> sortFieldMap,
        String itemIdentifier, String moduleKey) throws Exception
    {
        param.setRequestPage(start / count + 1);
        param.setPageSize(count);
        this.getHttpSession().removeAttribute(CommonConstants.SESSION_CHANGED);
        
        int recordCount = service_.getCountOfSummary(param);
        
        calculateRecordNum(param);

        initSorting(param, sortFieldMap);

        List<BaseHolder> recordList = ((PoHeaderService)service_).getListOfSummaryQuickPo((PoSummaryHolder)param);
        
        if (moduleKey != null && !moduleKey.isEmpty())
        {
            this.getSession().put(moduleKey, this.convertBaseHolderToList(recordList));
        }
        
        int index = start;
        for (BaseHolder bh : recordList)
            bh.setDojoIndex(++index);
        
        gridRlt = new GridResult();
        gridRlt.setIdentifier(itemIdentifier);
        gridRlt.setItems(recordList);
        gridRlt.setTotalRecords(recordCount);
    }
    
    
    private String getAccessType()
    {
        UserProfileHolder currentUser = this.getProfileOfCurrentUser();
        if(currentUser.getBuyerOid() != null)
        {
            return SummaryPageAccessType.B.name();
        }

        if(currentUser.getSupplierOid() != null)
        {
            return SummaryPageAccessType.S.name();
        }

        return null;
    }
    
    
    private void initSorting(BaseHolder param, Map<String, String> sortFieldMap)
    {
        if (sortFieldMap != null && sort != null && !sort.isEmpty())
        {
            String field = null;
            String order = null;

            if (sort.startsWith("-"))
            {
                field = sortFieldMap.get(sort.substring(1));
                order = "DESC";
            }
            else
            {
                field = sortFieldMap.get(sort);
                order = "ASC";
            }

            if (field != null)
            {
                param.setSortField(field);
                param.setSortOrder(order);
            }
        }
    }
    
    
    private void calculateRecordNum(BaseHolder param)
    {
        param.setStartRecordNum(start);
        param.setNumberOfRecordsToSelect(count);
    }
    
    
    private List<String> convertBaseHolderToList(List<BaseHolder> recordList)
    {
        if (recordList == null || recordList.isEmpty())
        {
            return new ArrayList<String>();
        }
        List<String> oidList = new ArrayList<String>();
        for (BaseHolder holder : recordList)
        {
            oidList.add(holder.getCustomIdentification().replaceAll(" ", ""));
        }
        return oidList;
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
    
    
    private void createAndSentInvoice(InvHolder invoice)throws Exception
    {
        MsgTransactionsHolder poMsg = msgTransactionsService.selectByKey(invoice.getHeader().getPoOid());
        this.updateReadStatus(poMsg);
        
        InvHeaderHolder invHeader = invoice.getHeader();
        PoHeaderHolder poHeader = poHeaderService
            .selectPoHeaderByKey(invHeader.getPoOid());
        invHeader.setInvStatus(InvStatus.SUBMIT);
        
        MsgTransactionsHolder newMsg = invHeader.toMsgTransactions();
        CommonParameterHolder cp = this.getCommonParameter();
        BuyerHolder buyer = buyerService.selectBuyerByKey(invoice
            .getHeader().getBuyerOid());
        SupplierHolder supplier = supplierService
            .selectSupplierByKey(invoice.getHeader().getSupplierOid());
        invoice.getHeader().setInvAmountNoVat(BigDecimalUtil.getInstance().format(
            invoice.getHeader().getInvAmountNoVat().add(invoice.getHeader().getAdditionalDiscountAmount()), 2));
        invoiceService.createAndSentInvoice(cp, invoice, newMsg, poHeader, buyer,
            supplier);
        
    }
    
    
    private void saveInvoice(InvHolder invoice) throws Exception
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
    }
    

    private boolean validateInvoice(InvHolder invoice, List<String> failds) throws Exception
    {
        boolean flag = false;
        if (!flag && (invoice.getHeader().getInvNo() == null || invoice.getHeader().getInvNo().isEmpty()))
        {
            failds.add(this.getText(B2BPC1656, new String[] {invoice.getHeader().getPoNo()})
                + " " 
                + this.getText(B2BPC1649));
            flag = true;
        }
        
        if (!flag )
        {
            InvHeaderHolder inv = invHeaderService
                .selectEffectiveInvHeaderByInNo(invoice.getHeader()
                .getBuyerOid(), invoice.getHeader().getSupplierCode(),
                invoice.getHeader().getInvNo());
            
            if (inv != null)
            {
                failds.add(this.getText(B2BPC1656, new String[] {invoice.getHeader().getPoNo()})
                    + " "
                    + this.getText(B2BPC1651, new String[] {invoice.getHeader().getInvNo()}));
                flag = true;
            }
        }
        
        if (!flag && (invoice.getHeader().getInvNo() != null && !invoice.getHeader().getInvNo().isEmpty()))
        {
            int maxInvNoLength = getAllowdInvNoLength();
            if (invoice.getHeader().getInvNo().length() > maxInvNoLength)
            {
                failds.add(this.getText(B2BPC1656, new String[] {invoice.getHeader().getPoNo()})
                    + " " 
                    + this.getText(B2BPC1613, new String[] {String.valueOf(maxInvNoLength)}));
                flag = true;
            }
        }
        
        if (!flag)
        {
            InvHeaderHolder oldInv = invHeaderService
                    .selectEffectiveInvHeaderByBuyerSupplierPoNoAndStore(invoice
                    .getHeader().getBuyerCode(), invoice.getHeader()
                    .getSupplierCode(), invoice.getHeader().getPoNo(), invoice
                    .getHeader().getShipToCode());
            if (oldInv != null)
            {
                failds.add(this.getText(B2BPC1656, new String[] {invoice.getHeader().getPoNo()})
                    + " "
                    + getText(B2BPC1627, new String[] {invoice.getHeader().getShipToCode(),invoice.getHeader().getPoNo()}));
                flag = true;
            }
        }
        
        return flag;
    }
    
    
    //***********************
    //getter and setter
    //***********************
    public PoHeaderExtendedHolder getEditPoHex()
    {
        return editPoHex;
    }


    public void setEditPoHex(PoHeaderExtendedHolder editPoHex)
    {
        this.editPoHex = editPoHex;
    }


    public List<InvHolder> getInvoiceList()
    {
        return invoiceList;
    }


    public void setInvoiceList(List<InvHolder> invoiceList)
    {
        this.invoiceList = invoiceList;
    }


    public String getOp()
    {
        return op;
    }


    public void setOp(String op)
    {
        this.op = op;
    }


    public boolean isSelectType()
    {
        return selectType;
    }


    public void setSelectType(boolean selectType)
    {
        this.selectType = selectType;
    }
    
    

    public List<String> getSuccesses()
    {
        return successes;
    }


    public void setSuccesses(List<String> successes)
    {
        this.successes = successes;
    }


    public List<String> getFaileds()
    {
        return faileds;
    }


    public void setFaileds(List<String> faileds)
    {
        this.faileds = faileds;
    }


    public String getDocType()
    {
        return docType;
    }


    public void setDocType(String docType)
    {
        this.docType = docType;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    public String getMaxInvNoLength()
    {
        return maxInvNoLength;
    }


    public void setMaxInvNoLength(String maxInvNoLength)
    {
        this.maxInvNoLength = maxInvNoLength;
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
