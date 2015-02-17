//*****************************************************************************
//
// File Name       :  GoodsReceivedNoteAction.java
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.DisputeStatus;
import com.pracbiz.b2bportal.core.constants.GrnStatus;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingBuyerStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.UserTypeHolder;
import com.pracbiz.b2bportal.core.holder.extension.GrnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.report.DefaultGrnReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.GrnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.GrnDetailService;
import com.pracbiz.b2bportal.core.service.GrnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;
import com.pracbiz.b2bportal.core.service.GrnService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.UserTypeService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.DateJsonValueProcessor;
import com.pracbiz.b2bportal.core.util.PdfReportUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GoodsReceivedNoteAction extends TransactionalDocsBaseAction
    implements ApplicationContextAware, CoreCommonConstants
{
    private static final long serialVersionUID = -3383964897092817811L;
    
    private static final Logger log = LoggerFactory.getLogger(GoodsReceivedNoteAction.class);

    private static final String SESSION_KEY_SEARCH_PARAMETER_GRN = "SEARCH_PARAMETER_GRN";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.GoodsReceivedNoteAction.selectedOids";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";
    private static final String UNDEFINED = "undefined";
    private static final String B2BPC1802 = "B2BPC1802";
    private static final String B2BPC1803 = "B2BPC1803";
    private static final String B2BPC1805 = "B2BPC1805";
    private static final String B2BPC1806 = "B2BPC1806";
    private static final String B2BPC1807 = "B2BPC1807";
    private static final String B2BPC1810 = "B2BPC1810";
    private static final String B2BPC1812 = "B2BPC1812";
    private static final String B2BPC1813 = "B2BPC1813";
    private static final String B2BPC1814 = "B2BPC1814";
    private static final String B2BPC1815 = "B2BPC1815";
    private static final String B2BPC1816 = "B2BPC1816";
    private static final String B2BPC1817 = "B2BPC1817";
    private static final String B2BPC1818 = "B2BPC1818";
    private static final String B2BPC1819 = "B2BPC1819";
    private static final String B2BPC1820 = "B2BPC1820";
    private static final String B2BPC1821 = "B2BPC1821";
    private static final String B2BPC1822 = "B2BPC1822";
    private static final String B2BPC1823 = "B2BPC1823";
    private static final String B2BPC1824 = "B2BPC1824";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME = "&prime;";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_CHARACTER = "'";
    private static final String RIGHT_DOUBLE_QUOTATION_MARK = "\"";
    private static final String RETURN_PENDING = "pending";
    private static final String RETURN_UPDATE = "update";
  
    
    transient private ApplicationContext ctx;
    transient private InputStream rptResult;
   
    private GrnSummaryHolder param;
    private String rptFileName;

    @Autowired transient private GrnHeaderService grnHeaderService;
    @Autowired transient private GrnHeaderExtendedService grnHeaderExtendedService;
    @Autowired transient private GrnDetailService grnDetailService;
    @Autowired transient private GrnDetailExtendedService grnDetailExtendedService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private UserTypeService userTypeService;
    @Autowired transient private ControlParameterService controlParameterService;
    @Autowired transient private GrnService grnService;
    @Autowired transient private PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService;
    @Autowired transient private PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired transient private CustomAppConfigHelper appConfig;
    @Autowired transient private BusinessRuleService businessRuleService;
    @Autowired transient private PoHeaderService poHeaderService;
//    @Autowired transient private BuyerStoreService buyerStoreService;
//    @Autowired transient private BuyerStoreAreaUserService buyerStoreAreaUserService;
//    @Autowired transient private BuyerStoreUserService buyerStoreUserService;
//    @Autowired transient private BuyerStoreAreaService buyerStoreAreaService;

    private GrnHolder grn;
    private String docOid;
    private String errorMsg;
    private String grnHeaderJson;
    private String grnDetailJson;
    private String success;
    private String buyerRemarks;
    private String saveType;
    private boolean selectAll;

    
    public GoodsReceivedNoteAction()
    {
        this.initMsg();
        this.setPageId(PageId.P002.name());
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
            
            if ("PO".equalsIgnoreCase(docType))
            {
                GrnHeaderHolder grnHeader = grnHeaderService.selectGrnHeaderByKey(docOid);
                PoHeaderHolder po = poHeaderService
                    .selectEffectivePoHeaderByPoNo(grnHeader.getBuyerOid(),
                        grnHeader.getPoNo(), grnHeader.getSupplierCode());
                
                if (po == null)
                {
                    errorMsg = this.getText("message.information.no.record.generator.pdf", new String[]{"PO", grnHeader.getPoNo()});;
                    return INPUT;
                }
            }
        }
        catch (Exception e) 
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
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_GRN);

        param = (GrnSummaryHolder)getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_GRN);

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
            param = new GrnSummaryHolder();
        }

        try
        {
            param.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getGrnDateFrom()));
            param.setGrnDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getGrnDateTo()));
            param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getPoDateFrom()));
            param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getPoDateTo()));
            param.setSentDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getSentDateFrom()));
            param.setSentDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getSentDateTo()));
            param.setReceivedDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getReceivedDateFrom()));
            param.setReceivedDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getReceivedDateTo()));
            param = initSortField(param);
            
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_GRN, param);

        return SUCCESS;

    }

    public String data()
    {
        try
        {
            GrnSummaryHolder searchParam = this.initSearchParameter();
            
            this.obtainListRecordsOfPagination(grnHeaderService, searchParam, MODULE_KEY_GRN);
        }
        catch(Exception e)
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
        String docType = this.getRequest().getParameter("docType");
        try
        {
            String[] files = null;
            if (selectAll)
            {
                param = (GrnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_GRN);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_GRN, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<GrnSummaryHolder> grnList = grnHeaderService.selectAllRecordToExport(param);
                
                if (grnList != null && !grnList.isEmpty())
                {
                    files = new String[grnList.size()];
                    for (int i = 0; i < grnList.size(); i++)
                    {
                        GrnSummaryHolder grn = grnList.get(i);
                        
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(grn.getDocOid());
                        
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
                            File docPath = new File(
                                mboxUtil.getFolderInSupplierDocInPath(supplier.getMboxId(),
                                    DateUtil.getInstance().getYearAndMonth(
                                        msg.getCreateDate())));
                            
                            if (!docPath.isDirectory())
                            {
                                FileUtil.getInstance().createDir(docPath);
                            }
                            
                            BuyerMsgSettingReportHolder buyerMsgSettingReport = buyerMsgSettingReportService.selectByKey(msg.getBuyerOid(), 
                                    MsgType.GRN.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                            
                            DefaultGrnReportEngine grnReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                                .getBuyerCode());
                            
                            ReportEngineParameter<GrnHolder> reportParameter = retrieveParameter(msg, supplier);

                            int isForStore = DefaultReportEngine.PDF_TYPE_STANDARD;
                            if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 || 
                                    BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                            {
                                isForStore = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                            }
                            //0 means standard pdf, 1 means for buyer store pdf 
                            byte[] datas = grnReportEngine.generateReport(reportParameter, isForStore);
                            
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
                    BigDecimal docOid = new BigDecimal(parts[i]);

                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(docOid);
                    
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
                    
                    if ("PO".equalsIgnoreCase(docType))
                    {
                        GrnHeaderHolder grnHeader = grnHeaderService.selectGrnHeaderByKey(docOid);
                        PoHeaderHolder po = poHeaderService
                            .selectEffectivePoHeaderByPoNo(grnHeader.getBuyerOid(),
                                grnHeader.getPoNo(), grnHeader.getSupplierCode());
                        docOid = po.getPoOid();
                        this.getSession().put("session.parameter.PurchaseOrderAction.selectedOids", docOid + "/I-");
                        return "PO";
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
                                MsgType.GRN.name(), CoreCommonConstants.DEFAULT_SUBTYPE);
                        
                        DefaultGrnReportEngine grnReportEngine = retrieveEngine(buyerMsgSettingReport, msg
                            .getBuyerCode());
                        
                        ReportEngineParameter<GrnHolder> reportParameter = retrieveParameter(msg, supplier);

                        int isForStore = DefaultReportEngine.PDF_TYPE_STANDARD;
                        if (BigDecimal.valueOf(6).compareTo(this.getUserTypeOfCurrentUser()) == 0 || 
                                BigDecimal.valueOf(7).compareTo(this.getUserTypeOfCurrentUser()) == 0)
                        {
                            isForStore = DefaultReportEngine.PDF_TYPE_BY_ITEM;
                        }
                        //0 means standard pdf, 1 means for buyer store pdf 
                        byte[] datas = grnReportEngine.generateReport(reportParameter, isForStore);
                        
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
                rptFileName = "grnReports_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".pdf";
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }

    
    //*************
    //audit and dispute
    //*************
    public void validateCheckAccessDisputeOrAudit()
    {
        try
        {
            boolean flag = this.hasErrors();
            
            if (!flag
                && (StringUtils.isBlank(docOid) || UNDEFINED.equals(docOid)))
            {
                this.errorMsg = getText(B2BPC1802);
                flag = true;
            }
            
           GrnHeaderHolder grnHeader =  grnHeaderService.selectGrnHeaderByKey(new BigDecimal(docOid));
           
           if (!flag && grnHeader == null)
           {
               this.errorMsg = getText(B2BPC1803);
               flag = true;
           }
           else
           {
               boolean disputeRule = businessRuleService.isSupplierCanDisputeGRN(grnHeader.getBuyerOid());
               
               if(!flag && grnHeader.getGrnStatus().equals(GrnStatus.OUTDATED))
               {
                   this.errorMsg = this.getText(B2BPC1814, new String[] {grnHeader.getGrnNo()});
                   flag = true;
               }
               
               if(!flag
                    && !(this.getUserTypeOfCurrentUser().intValue() == 3
                        || this.getUserTypeOfCurrentUser().intValue() == 5
                        || this.getUserTypeOfCurrentUser().intValue() == 6 
                        || this.getUserTypeOfCurrentUser().intValue() == 7))
                {
                    this.errorMsg = this.getText(B2BPC1824, new String[] {grnHeader.getGrnNo()});
                    flag = true;
                }
 
               if (!flag && (this.getUserTypeOfCurrentUser().intValue() == 3 || this.getUserTypeOfCurrentUser().intValue() == 5))
               {
                    PoInvGrnDnMatchingGrnHolder param = new PoInvGrnDnMatchingGrnHolder();
                    param.setGrnOid(grnHeader.getGrnOid());
                    List<PoInvGrnDnMatchingGrnHolder> results = poInvGrnDnMatchingGrnService
                       .select(param);
                    
                    if (!flag && !disputeRule)
                    {
                        this.errorMsg = this.getText(B2BPC1817);
                        flag = true;
                    }
                    
                    if (!flag && (results != null && !results.isEmpty()))
                    {
                        PoInvGrnDnMatchingHolder matching = poInvGrnDnMatchingService.selectByKey(results.get(0).getMatchingOid());
                        
                        if (matching == null)
                        {
                            this.errorMsg = this.getText(B2BPC1815, new String[] {grnHeader.getGrnNo()});
                            flag = true;
                        }
                        else
                        {
                            if (!flag && matching.getClosed())
                            {
                                this.errorMsg = this.getText(B2BPC1816, new String[] {grnHeader.getGrnNo()});
                                flag = true;
                            }
                            
                            if (!flag && matching.getSupplierStatus().equals(PoInvGrnDnMatchingSupplierStatus.ACCEPTED))
                            {
                                this.errorMsg = this.getText(B2BPC1819);
                                flag = true;
                            }
                            
                            if (!flag && !matching.getBuyerStatus().equals(PoInvGrnDnMatchingBuyerStatus.PENDING))
                            {
                                if (matching.getBuyerStatus().equals(PoInvGrnDnMatchingBuyerStatus.ACCEPTED))
                                {
                                    this.errorMsg = this.getText(B2BPC1820);
                                    flag = true;
                                }
                                else
                                {
                                    this.errorMsg = this.getText(B2BPC1821);
                                    flag = true;
                                }
                               
                            }
                            
                            if(!flag
                                && !(matching.getMatchingStatus().equals(
                                    PoInvGrnDnMatchingStatus.UNMATCHED) || matching
                                    .getMatchingStatus().equals(
                                        PoInvGrnDnMatchingStatus.QTY_UNMATCHED)))
                            {
                                this.errorMsg = this.getText(B2BPC1822);
                                flag = true;
                            }
                            
                            Date today =new Date();
                            long date = (today.getTime() - matching.getCreateDate().getTime())/1000/60/60/24;
                            
                            int bufferingDays = businessRuleService.selectGlobalMatchingJobMaxBufferingDays(matching.getBuyerOid());
                            
                            if (date > bufferingDays)
                            {
                                this.errorMsg = this.getText(B2BPC1823, new String[]{String.valueOf(bufferingDays)});
                                flag = true;
                            }
                        }
                    }
               }
               
               
               if (!flag && (this.getUserTypeOfCurrentUser().intValue() == 6 || this.getUserTypeOfCurrentUser().intValue() == 7))
               {
                   if (!flag && disputeRule)
                   {
                       this.errorMsg = this.getText(B2BPC1818);
                       flag = true;
                   }
                   
                   if (!flag && !grnHeader.getDispute())
                   {
                       this.errorMsg = this.getText(B2BPC1805, new String[]{grnHeader.getGrnNo()});
                       flag = true;
                   }
               }
               if (!flag && grnHeader.getDisputeStatus().equals(DisputeStatus.APPROVED))
               {
                   this.errorMsg =this.getText(B2BPC1806, new String[]{grnHeader.getGrnNo()});
                   flag = true;
               }
               
               else if (!flag && grnHeader.getDisputeStatus().equals(DisputeStatus.REJECTED))
               {
                   this.errorMsg = this.getText(B2BPC1807, new String[]{grnHeader.getGrnNo()});
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
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    public String  checkAccessDisputeOrAudit()
    {
        this.success = SUCCESS;
        return SUCCESS;
    }
    
    
    public String initAudit()
    {
        try
        {
            grn = new GrnHolder();
            BigDecimal grnOid = new BigDecimal(docOid);
            GrnHeaderHolder grnHeader =  grnHeaderService.selectGrnHeaderByKey(grnOid);
            List<GrnDetailHolder> grnDetails = grnDetailService.selectGrnDetailByKey(grnOid);
            
            grn.setHeader(grnHeader);
            grn.setDetails(grnDetails);
            
            this.grnFormat(grn);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        return SUCCESS;
    }
    
    
    public String initDispute()
    {
        try
        {
            grn = new GrnHolder();
            BigDecimal grnOid = new BigDecimal(docOid);
            GrnHeaderHolder grnHeader =  grnHeaderService.selectGrnHeaderByKey(grnOid);
            List<GrnDetailHolder> grnDetails = grnDetailService.selectGrnDetailByKey(grnOid);
            
            grn.setHeader(grnHeader);
            grn.setDetails(grnDetails);
            
            this.grnFormat(grn);
            
            JsonConfig jsonConfig = new JsonConfig();      
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));

            JSONObject headerJson = JSONObject.fromObject(grnHeader, jsonConfig);
            JSONArray detailJsons = JSONArray.fromObject(grnDetails, jsonConfig);
            grnHeaderJson = this.replaceSpecialCharactersForJson(headerJson
                    .toString());
            grnDetailJson = this.replaceSpecialCharactersForJson(detailJsons
                    .toString());
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        return SUCCESS;
    }
    
    
    public String saveDispute()
    {
       try
       {
           grn = new GrnHolder();
           grn.conertJsonToGrn(grnHeaderJson, grnDetailJson);
           GrnHolder oldObj = new GrnHolder();
           BeanUtils.copyProperties(grn, oldObj);
           initGrnHeader(grn.getHeader(), DisputeStatus.PENDING);
           
           grnService.auditUpdateByPrimaryKey(this.getCommonParameter(), grn, oldObj);
           this.success = SUCCESS;
       }
       catch(Exception e)
       {
           ErrorHelper.getInstance().logError(log, e);
       }
        
        return SUCCESS;
    }
    
    public void validateSaveAuditDispute()
    {
        try
        {
            boolean flag = this.hasErrors();
            if (!flag
                && (StringUtils.isBlank(docOid) || UNDEFINED.equals(docOid)))
            {
                this.errorMsg = getText(B2BPC1802);
                flag = true;
            }
            
            if (!flag && (StringUtils.isBlank(buyerRemarks) || UNDEFINED.equals(buyerRemarks)))
            {
                this.errorMsg = getText(B2BPC1810);
            }
            
            if (!flag)
            {
               GrnHeaderHolder grnHeader =  grnHeaderService.selectGrnHeaderByKey(new BigDecimal(docOid));
               
               if (grnHeader == null)
               {
                   this.errorMsg = getText(B2BPC1803);
                   flag = true;
               }
            }
            
            if (flag)
            {
                this.addActionError(errorMsg);
                this.init();
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    
    public String saveAuditDispute()
    {
       try
        {
            log.info("start to save audit dispute");
            grn = grnService.selectByKey(new BigDecimal(docOid));
            
            GrnHolder oldObj = new GrnHolder();
            BeanUtils.copyProperties(grn, oldObj);
            
            grn.getHeader().setDisputeBuyerRemarks(buyerRemarks);
            
            if (saveType.equalsIgnoreCase(DisputeStatus.APPROVED.name()))
            {
                initGrnHeader(grn.getHeader(), DisputeStatus.APPROVED);
                msg.saveSuccess(this.getText(B2BPC1812, new String[] {grn
                    .getHeader().getGrnNo()}));
            }
           
            if (saveType.equalsIgnoreCase(DisputeStatus.REJECTED.name()))
            {
                initGrnHeader(grn.getHeader(), DisputeStatus.REJECTED);
                msg.saveSuccess(this.getText(B2BPC1813, new String[] {grn
                    .getHeader().getGrnNo()}));
            }
            
            grnService.auditUpdateByPrimaryKey(this.getCommonParameter(), grn, oldObj);

            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

            msg.addMessageTarget(mt);

        }
       catch(Exception e)
       {
           ErrorHelper.getInstance().logError(log, e);
       }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    public String viewDispute()
    {
        try
        {
            grn = new GrnHolder();
            BigDecimal grnOid = new BigDecimal(docOid);
            GrnHeaderHolder grnHeader =  grnHeaderService.selectGrnHeaderByKey(grnOid);
            List<GrnDetailHolder> grnDetails = grnDetailService.selectGrnDetailByKey(grnOid);
            grn.setHeader(grnHeader);
            grn.setDetails(grnDetails);
            this.grnFormat(grn);
            
            if (grnHeader.getDisputeStatus().equals(DisputeStatus.PENDING))
            {
                return RETURN_PENDING;
            }
            else
            {
                return RETURN_UPDATE;
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return FORWARD_COMMON_MESSAGE;
        }
        
    }
    
    
    // *****************************************************
    // export excel
    // *****************************************************
    public String exportExcel()
    {
        try
        {
            List<BigDecimal> grnOids = new ArrayList<BigDecimal>();
            if (selectAll)
            {
                param = (GrnSummaryHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_GRN);
                
                if (param == null)
                {
                    initSearchCondition();
                    getSession().put(SESSION_KEY_SEARCH_PARAMETER_GRN, param);
                }
                
                param.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
                initCurrentUserSearchParam(param);
                
                List<GrnSummaryHolder> grnList = grnHeaderService.selectAllRecordToExport(param);
                
                if (grnList != null && !grnList.isEmpty())
                {
                    for (GrnSummaryHolder grn : grnList)
                    {
                        grnOids.add(grn.getDocOid());
                        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(grn.getDocOid());
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
                    BigDecimal grnOid = new BigDecimal(part);
                    grnOids.add(new BigDecimal(part));
                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(grnOid);
                    this.updateReadStatus(msg);
                }
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(grnService.exportExcel(grnOids, storeFlag)));
            rptFileName = "GrnReport_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
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
            GrnSummaryHolder searchParam = this.initSearchParameter();
            int count = grnHeaderService.getCountOfSummary(searchParam);
            searchParam.setNumberOfRecordsToSelect(count);
            searchParam.setStartRecordNum(0);
            
            List<MsgTransactionsExHolder> summarys = grnHeaderService.getListOfSummary(searchParam);
            if (summarys==null || summarys.isEmpty())
            {
                return INPUT;
            }
            
            List<GrnSummaryHolder> grnSummarys = new ArrayList<GrnSummaryHolder>();
            
            for (MsgTransactionsExHolder msgTrans : summarys)
            {
                grnSummarys.add((GrnSummaryHolder)msgTrans);
            }
            
            boolean storeFlag = false;
            if (this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(6)) == 0 
                    || this.getUserTypeOfCurrentUser().compareTo(BigDecimal.valueOf(7)) == 0)
            {
                storeFlag = true;
            }
            
            this.setRptResult(new ByteArrayInputStream(grnService.exportSummaryExcel(grnSummarys, storeFlag)));
            rptFileName = "GRN_SUMMARY_REPORT_" + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + ".xls";
            
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
    private ReportEngineParameter<GrnHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        GrnHeaderHolder header = grnHeaderService.selectGrnHeaderByKey(msg.getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
        List<GrnDetailHolder> details = grnDetailService
            .selectGrnDetailByKey(msg.getDocOid());
        List<GrnHeaderExtendedHolder> headerExtendeds = grnHeaderExtendedService
            .selectHeaderExtendedByKey(msg.getDocOid());
        
        List<GrnDetailExtendedHolder> detailExtendeds = grnDetailExtendedService.selectDetailExtendedByKey(msg.getDocOid());

        ReportEngineParameter<GrnHolder> reportEngineParameter = new ReportEngineParameter<GrnHolder>();

        GrnHolder data = new GrnHolder();

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

    
    private DefaultGrnReportEngine retrieveEngine(
            BuyerMsgSettingReportHolder buyerMsgSettingReport, String buyerCode)
    {
        if(!buyerMsgSettingReport.getCustomizedReport())
        {
            //standard report 
            return ctx.getBean(appConfig.getStandardReport(MsgType.GRN.name(), buyerMsgSettingReport.getSubType(),
                    buyerMsgSettingReport.getReportTemplate()),
                    DefaultGrnReportEngine.class);
        }

        //customized report 
        return ctx.getBean(appConfig.getCustomizedReport(buyerCode, MsgType.GRN
            .name(), buyerMsgSettingReport.getSubType(), buyerMsgSettingReport.getReportTemplate()),
            DefaultGrnReportEngine.class);
    }
    
    
    private GrnSummaryHolder initSortField(GrnSummaryHolder param)
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
        docStatuses = GrnStatus.toMapValue(this);
        if (param == null)
        {
            param = new GrnSummaryHolder();
        }
        
        if(getProfileOfCurrentUser().getBuyerOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
            {
                param.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setGrnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
        if (getProfileOfCurrentUser().getSupplierOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                    CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
            {
                param.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setGrnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
    }
    
    
    private String replaceSpecialCharactersForJson(String json)
    {
        if (json == null)
        {
            return null;
        }
        
        String result = StringUtils.replace(json, RIGHT_SINGLE_QUOTATION_MARK_CHARACTER,
            RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME);
        
        return StringUtils.replace(result,  RIGHT_DOUBLE_QUOTATION_MARK,
            RIGHT_SINGLE_QUOTATION_MARK_CHARACTER);
    }
    
    
    private void initGrnHeader(GrnHeaderHolder grnHeader, DisputeStatus disputeStauts)
    {
        if (this.getUserTypeOfCurrentUser().intValue() == 3 || this.getUserTypeOfCurrentUser().intValue() == 5)
        {
            grnHeader.setDisputeSupplierDate(new Date());
            grnHeader.setDisputeSupplierBy(this.getLoginIdOfCurrentUser());
            grnHeader.setDisputeStatus(DisputeStatus.PENDING);
            grnHeader.setDispute(true);
        }
        
        if (this.getUserTypeOfCurrentUser().intValue() == 6 || this.getUserTypeOfCurrentUser().intValue() == 7)
        {   
            grnHeader.setDisputeBuyerDate(new Date());
            grnHeader.setDisputeBuyerBy(this.getLoginIdOfCurrentUser());
            if (disputeStauts.equals(DisputeStatus.APPROVED))
            {
                grnHeader.setDisputeStatus(DisputeStatus.APPROVED);
            }
            
            if (disputeStauts.equals(DisputeStatus.REJECTED))
            {
                grnHeader.setDisputeStatus(DisputeStatus.REJECTED);
            }
        }
    }
    
    
    private void grnFormat(GrnHolder grn)throws Exception
    {
        BigDecimalUtil format = BigDecimalUtil.getInstance();
        
        for (GrnDetailHolder detail : grn.getDetails())
        {
            detail.setReceiveQty(format.format(detail.getReceiveQty(), 2));
            detail.setDeliveryQty(format.format(detail.getDeliveryQty(), 2));
        }
    }
    
    
    private GrnSummaryHolder initSearchParameter() throws Exception
    {
        GrnSummaryHolder searchParam = (GrnSummaryHolder)getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_GRN);

        if(searchParam == null)
        {
            searchParam = new GrnSummaryHolder();
            
            if(getProfileOfCurrentUser().getBuyerOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setGrnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            if (getProfileOfCurrentUser().getSupplierOid() != null)
            {
                ControlParameterHolder documentWindow = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                {
                    searchParam.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                }
                else
                {
                    searchParam.setGrnDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                }
                searchParam.setGrnDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
            }
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_GRN, searchParam);
        }
        
        searchParam.setUserTypeOid(this.getUserTypeOfCurrentUser());
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

    public GrnSummaryHolder getParam()
    {
        return param;
    }

    public void setParam(GrnSummaryHolder param)
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

    public String getDocOid()
    {
        return docOid;
    }

    public void setDocOid(String docOid)
    {
        this.docOid = docOid;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public GrnHolder getGrn()
    {
        return grn;
    }

    public void setGrn(GrnHolder grn)
    {
        this.grn = grn;
    }

    public String getGrnHeaderJson()
    {
        return grnHeaderJson;
    }

    public void setGrnHeaderJson(String grnHeaderJson)
    {
        this.grnHeaderJson = grnHeaderJson;
    }

    public String getGrnDetailJson()
    {
        return grnDetailJson;
    }

    public void setGrnDetailJson(String grnDetailJson)
    {
        this.grnDetailJson = grnDetailJson;
    }

    public String getSuccess()
    {
        return success;
    }

    public void setSuccess(String success)
    {
        this.success = success;
    }

    public String getBuyerRemarks()
    {
        return buyerRemarks;
    }

    public void setBuyerRemarks(String buyerRemarks)
    {
        this.buyerRemarks = buyerRemarks;
    }

    public String getSaveType()
    {
        return saveType;
    }

    public void setSaveType(String saveType)
    {
        this.saveType = saveType;
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
