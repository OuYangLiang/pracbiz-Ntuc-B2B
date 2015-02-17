package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class TradingPartnerAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(TradingPartnerAction.class);
    private static final long serialVersionUID = 7459499021033194123L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_TRADING_PARTNER = "SEARCH_PARAMETER_TRADING_PARTNER";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final String SESSION_OID_PARAMETER = "session.parameter.TradingPartnerAction.selectedOids";
    private static final String SESSION_SUPPLIER_OID_PARAMETER = "session.parameter.TradingPartnerAction.supplierOid";
    private static final String SESSION_TC_CODE_PARAMETER = "session.parameter.TradingPartnerAction.termConditionCode";
    private static final String REQUEST_PARAMTER_OID = "selectedOids";
    private static final String REQUEST_OID_DELIMITER = "\\-";
    private static final String NO_VALID_BUYER_SUPPLIER = "B2BPC0529";

    private static final Map<String, String> sortMap;

    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("buyerSupplierCode", "T.BUYER_SUPPLIER_CODE");
        sortMap.put("buyerCode", "B.BUYER_CODE");
        sortMap.put("buyerName", "B.BUYER_NAME");
        sortMap.put("supplierCode", "S.SUPPLIER_CODE");
        sortMap.put("supplierName", "S.SUPPLIER_NAME");
        sortMap.put("active", "T.ACTIVE");
        sortMap.put("updateDate", "T.UPDATE_DATE");
    }

    private TradingPartnerExHolder tradingPartner;
    private List<BuyerHolder> buyerList;
    private List<SupplierHolder> supplierList;
    private List<? extends Object> termsConditionsList;

    @Autowired transient TradingPartnerService tradingPartnerService;
    @Autowired transient GroupTradingPartnerService groupTradingPartnerService;
    @Autowired transient BuyerService buyerService;
    @Autowired transient SupplierService supplierService;
    @Autowired transient TermConditionService termConditionService;
    @Autowired transient OidService oidService;


    public TradingPartnerAction()
    {
        this.initMsg();
    }

    
    public String putParamIntoSession()
    {
        if (this.getRequest().getParameter(REQUEST_PARAMTER_OID) == null)
        {
            String supplierOid = this.getRequest().getParameter("supplierOid");
            String termConditionCode = this.getRequest().getParameter("termConditionCode");
            this.getSession().put(SESSION_SUPPLIER_OID_PARAMETER, supplierOid);
            this.getSession().put(SESSION_TC_CODE_PARAMETER, termConditionCode);
        }
        else
        {
            this.getSession().put(SESSION_OID_PARAMETER,
                    this.getRequest().getParameter(REQUEST_PARAMTER_OID));
        }

        return SUCCESS;
    }

    // *****************************************************
    // trading partner summary page
    // *****************************************************
    
    public String init()
    {
        try
        {
            clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_TRADING_PARTNER);

            if (tradingPartner == null)
            {
                tradingPartner = (TradingPartnerExHolder) getSession().get(
                        SESSION_KEY_SEARCH_PARAMETER_TRADING_PARTNER);
            }
            else
            {
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_TRADING_PARTNER,
                        tradingPartner);
            }

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

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_TRADING_PARTNER,
                tradingPartner);

        return SUCCESS;
    }


    public String data() throws Exception
    {
        try
        {

            TradingPartnerExHolder searchParam = (TradingPartnerExHolder) getSession()
                    .get(SESSION_KEY_SEARCH_PARAMETER_TRADING_PARTNER);

            if (null == searchParam)
            {
                searchParam = new TradingPartnerExHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_TRADING_PARTNER,searchParam);
            }
            searchParam.setBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            searchParam.setSupplierOid(this.getProfileOfCurrentUser().getSupplierOid());
            
            this.obtainListRecordsOfPagination(tradingPartnerService,
                    searchParam, sortMap, "tradingPartnerOid", null);
            
            for (BaseHolder baseItem : this.getGridRlt().getItems())
            {
                TradingPartnerExHolder item = (TradingPartnerExHolder) baseItem;
                if (item.getActive())
                {
                    item.setActiveStatusValue(this.getText("Value.active"));
                }
                else
                {
                    item.setActiveStatusValue(this.getText("Value.inactive"));
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


    // ***************************************
    // Create Function
    // ***************************************

    public String initAdd()
    {
        try
        {
            this.initBuyersAndSuppliers(this.getProfileOfCurrentUser().getBuyerOid(),
                    this.getProfileOfCurrentUser().getSupplierOid(), true);
            
            if (null == buyerList || buyerList.isEmpty()
                    || null == supplierList || supplierList.isEmpty())
            {
                msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
                MessageTargetHolder mt = new MessageTargetHolder();
                mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
                mt.setTargetURI(INIT);
                mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
                msg.saveError(this.getText(NO_VALID_BUYER_SUPPLIER));
                msg.addMessageTarget(mt);
                log.info(this.getText(NO_VALID_BUYER_SUPPLIER));
                return FORWARD_COMMON_MESSAGE;
            }
            tradingPartner = new TradingPartnerExHolder();
            BuyerHolder buyer = (BuyerHolder) buyerList.get(0);
            
            if (buyer != null)
            {
                tradingPartner.setBuyerOid(buyer.getBuyerOid());
                tradingPartner.setBuyerContactTel(buyer.getContactTel());
                tradingPartner.setBuyerContactPerson(buyer.getContactName());
                tradingPartner.setBuyerContactMobile(buyer.getContactMobile());
                tradingPartner.setBuyerContactFax(buyer.getContactFax());
                tradingPartner.setBuyerContactEmail(buyer.getContactEmail());
            }
            
            SupplierHolder supplier = (SupplierHolder) supplierList.get(0);
            
            if (supplier != null)
            {
                tradingPartner.setSupplierOid(supplier.getSupplierOid());
                tradingPartner.setSupplierContactTel(supplier.getContactTel());
                tradingPartner.setSupplierContactPerson(supplier.getContactName());
                tradingPartner.setSupplierContactMobile(supplier.getContactMobile());
                tradingPartner.setSupplierContactFax(supplier.getContactFax());
                tradingPartner.setSupplierContactEmail(supplier.getContactEmail());
                
                TermConditionHolder tc = termConditionService.selectDefaultTermConditionBySupplierOid(supplier.getSupplierOid());
                
                if (tc != null)
                {
                    tradingPartner.setTermCondition1(tc.getTermCondition1());
                    tradingPartner.setTermCondition2(tc.getTermCondition2());
                    tradingPartner.setTermCondition3(tc.getTermCondition3());
                    tradingPartner.setTermCondition4(tc.getTermCondition4());
                    this.getSession().put(SESSION_TC_CODE_PARAMETER, tc.getTermConditionCode());
                }
                this.getSession().put(SESSION_SUPPLIER_OID_PARAMETER, tradingPartner.getSupplierOid());
            }
            
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    

    public void validateSaveAdd()
    {
        try
        {
            boolean flag = this.hasErrors();
            
            if (!flag && tradingPartner == null)
            {
                this.addActionError(this.getText("B2BPC0517"));
                flag = true;
            }
            if (!flag)
            {
                Object supplierOid = this.getSession().get(SESSION_SUPPLIER_OID_PARAMETER);
                Object tcCode = this.getSession().get(SESSION_TC_CODE_PARAMETER);
                if (supplierOid == null)
                {
                    flag = true;
                }
                if (!flag && tcCode != null && !tcCode.equals(""))
                {
                    TermConditionHolder tc = termConditionService
                            .selectTermsConditionBySupplierOidAndTcCode(
                                    new BigDecimal(supplierOid.toString()),
                                    tcCode.toString());
                    if (tc == null)
                    {
                        flag = true;
                    }
                    else
                    {
                        tradingPartner.setTermConditionOid(tc.getTermConditionOid());
                    }
                }
                    
                if (flag)
                {
                    this.addActionError(this.getText("B2BPC0518"));
                }
            }
            if (!flag && tradingPartner.getBuyerSupplierCode().length() > 10)
            {
                this.addActionError(this.getText("B2BPC0521",
                        new String[] { tradingPartner.getBuyerSupplierCode() }));
                flag = true;
            }
            if (!flag && tradingPartner.getSupplierBuyerCode().length() > 10)
            {
                this.addActionError(this.getText("B2BPC0522",
                        new String[] { tradingPartner.getSupplierBuyerCode() }));
                flag = true;
            }
            
            if(!flag && (null != tradingPartnerService
                .selectByBuyerAndBuyerGivenSupplierCode(
                    tradingPartner.getBuyerOid(),
                    tradingPartner.getBuyerSupplierCode())))
            {
                this.addActionError(this.getText("B2BPC0530",
                        new String[] { tradingPartner
                                .getBuyerSupplierCode() }));
                flag = true;
            }
            
            if (!flag)
            {
                TradingPartnerExHolder param = new TradingPartnerExHolder();
                param.setSupplierOid(tradingPartner.getSupplierOid());
                param.setSupplierBuyerCode(tradingPartner.getSupplierBuyerCode());
                List<TradingPartnerHolder> rlt = tradingPartnerService.select(param);
                if (rlt != null && !rlt.isEmpty())
                {
                    this.addActionError(this.getText("B2BPC0531",
                            new String[] { tradingPartner
                                    .getSupplierBuyerCode() }));
                    flag = true;
                }
            }
            
            if (flag)
            {
                this.initBuyersAndSuppliers(this.getProfileOfCurrentUser().getBuyerOid(),
                        this.getProfileOfCurrentUser().getSupplierOid(), true);

                if (buyerList == null) buyerList = new ArrayList<BuyerHolder>();
                if (supplierList == null) supplierList = new ArrayList<SupplierHolder>();
                if (tradingPartner == null) tradingPartner = new TradingPartnerExHolder();
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String saveAdd()
    {
        try
        {
            tradingPartner.trimAllString();
            tradingPartner.setAllEmptyStringToNull();
            tradingPartner.setTradingPartnerOid(oidService.getOid());
            tradingPartner.setCreateDate(new Date());
            tradingPartner.setCreateBy(this.getLoginIdOfCurrentUser());
            
            tradingPartnerService.auditInsert(getCommonParameter(), tradingPartner);
            
            this.getSession().remove(SESSION_SUPPLIER_OID_PARAMETER);
            this.getSession().remove(SESSION_TC_CODE_PARAMETER);
            
            log.info(this.getText(
                    "B2BPC0534",
                    new String[] { tradingPartner.getBuyerSupplierCode(),
                            this.getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    // ***************************************
    // View function
    // ***************************************
    
    public void validateView()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            
            if (!flag && (tradingPartner == null || tradingPartner.getTradingPartnerOid() == null))
            {
                this.addActionError(this.getText("B2BPC0519"));
                flag = true;
            }
            if (!flag)
            {
                TradingPartnerHolder tp = tradingPartnerService
                        .selectTradingPartnerByKey(tradingPartner
                                .getTradingPartnerOid());
                
                if (tp == null)
                {
                    this.addActionError(this.getText("B2BPC0520"));
                    flag = true;
                }
                else
                {
                    BeanUtils.copyProperties(tp, tradingPartner);
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String view()
    {
        try
        {
            BuyerHolder buyer = buyerService.selectBuyerByKey(tradingPartner.getBuyerOid());
            tradingPartner.setBuyerCode(buyer.getBuyerCode());
            tradingPartner.setBuyerName(buyer.getBuyerName());
            SupplierHolder supplier = supplierService.selectSupplierByKey(tradingPartner.getSupplierOid());
            tradingPartner.setSupplierCode(supplier.getSupplierCode());
            tradingPartner.setSupplierName(supplier.getSupplierName());
            
            if (tradingPartner.getTermConditionOid() != null)
            {
                TermConditionHolder tc = termConditionService.selectTermConditionByKey(tradingPartner.getTermConditionOid());
                
                tradingPartner.setTermCondition1(tc.getTermCondition1());
                tradingPartner.setTermCondition2(tc.getTermCondition2());
                tradingPartner.setTermCondition3(tc.getTermCondition3());
                tradingPartner.setTermCondition4(tc.getTermCondition4());
            }
            
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    // ***************************************
    // Edit function
    // ***************************************
    
    public void validateInitEdit()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            
            if (!flag && (tradingPartner == null || tradingPartner.getTradingPartnerOid() == null))
            {
                this.addActionError(this.getText("B2BPC0519"));
                flag = true;
            }
            if (!flag)
            {
                TradingPartnerHolder tp = tradingPartnerService
                        .selectTradingPartnerByKey(tradingPartner
                                .getTradingPartnerOid());
                
                if (tp == null)
                {
                    this.addActionError(this.getText("B2BPC0520"));
                    flag = true;
                }
                else
                {
                    BeanUtils.copyProperties(tp, tradingPartner);
                }
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String initEdit()
    {
        try
        {
            this.initBuyersAndSuppliers(tradingPartner.getBuyerOid(),
                    tradingPartner.getSupplierOid(), false);
            
            if (null == buyerList || buyerList.isEmpty() 
                    || null == supplierList || supplierList.isEmpty())
            {
                msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
                MessageTargetHolder mt = new MessageTargetHolder();
                mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
                mt.setTargetURI(INIT);
                mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
                msg.saveError(this.getText(NO_VALID_BUYER_SUPPLIER));
                msg.addMessageTarget(mt);
                log.info(this.getText(NO_VALID_BUYER_SUPPLIER));
                return FORWARD_COMMON_MESSAGE;
            }

            if (tradingPartner.getTermConditionOid() != null)
            {
                TermConditionHolder tc = termConditionService.selectTermConditionByKey(tradingPartner.getTermConditionOid());
                
                tradingPartner.setTermCondition1(tc.getTermCondition1());
                tradingPartner.setTermCondition2(tc.getTermCondition2());
                tradingPartner.setTermCondition3(tc.getTermCondition3());
                tradingPartner.setTermCondition4(tc.getTermCondition4());
                this.getSession().put(SESSION_TC_CODE_PARAMETER, tc.getTermConditionCode());
            }
            this.getSession().put(SESSION_SUPPLIER_OID_PARAMETER, tradingPartner.getSupplierOid());
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public void validateSaveEdit()
    {
        try
        {
            boolean flag = this.hasErrors();
            
            if (!flag && (tradingPartner == null || tradingPartner.getTradingPartnerOid() == null))
            {
                this.addActionError(this.getText("B2BPC0517"));
                flag = true;
            }
            if (!flag)
            {
                Object supplierOid = this.getSession().get(SESSION_SUPPLIER_OID_PARAMETER);
                Object tcCode = this.getSession().get(SESSION_TC_CODE_PARAMETER);
                if (supplierOid == null)
                {
                    flag = true;
                }
                if (!flag && tcCode != null && !tcCode.equals(""))
                {
                    TermConditionHolder tc = termConditionService
                            .selectTermsConditionBySupplierOidAndTcCode(
                                    new BigDecimal(supplierOid.toString()),
                                    tcCode.toString());
                    
                    if (tc == null)
                    {
                        flag = true;
                    }
                    else
                    {
                        tradingPartner.setTermConditionOid(tc.getTermConditionOid());
                    }
                }
                if (flag)
                {
                    this.addActionError(this.getText("B2BPC0518"));
                }
            }
            if (!flag && tradingPartner.getBuyerSupplierCode().length() > 10)
            {
                this.addActionError(this.getText("B2BPC0521",
                        new String[] { tradingPartner.getBuyerSupplierCode() }));
                flag = true;
            }
            if (!flag && tradingPartner.getSupplierBuyerCode().length() > 10)
            {
                this.addActionError(this.getText("B2BPC0522",
                        new String[] { tradingPartner.getSupplierBuyerCode() }));
                flag = true;
            }
            if (!flag)
            {
                TradingPartnerExHolder param = new TradingPartnerExHolder();
                param.setBuyerOid(tradingPartner.getBuyerOid());
                param.setBuyerSupplierCode(tradingPartner.getBuyerSupplierCode());
                List<TradingPartnerHolder> rlt = tradingPartnerService.select(param);
                if (rlt != null && !rlt.isEmpty())
                {
                    TradingPartnerHolder tp = (TradingPartnerHolder) rlt.get(0);
                    if (!tp.getTradingPartnerOid().equals(tradingPartner.getTradingPartnerOid()))
                    {
                        this.addActionError(this.getText("B2BPC0530",
                                new String[] { tradingPartner
                                        .getBuyerSupplierCode() }));
                        flag = true;
                    }
                }
            }
            
            if (!flag)
            {
                TradingPartnerExHolder param = new TradingPartnerExHolder();
                param.setSupplierOid(tradingPartner.getSupplierOid());
                param.setSupplierBuyerCode(tradingPartner.getSupplierBuyerCode());
                List<TradingPartnerHolder> rlt = tradingPartnerService.select(param);
                if (rlt != null && !rlt.isEmpty())
                {
                    TradingPartnerHolder tp = (TradingPartnerHolder) rlt.get(0);
                    if (!tp.getTradingPartnerOid().equals(tradingPartner.getTradingPartnerOid()))
                    {
                        this.addActionError(this.getText("B2BPC0531",
                                new String[] { tradingPartner
                                        .getSupplierBuyerCode() }));
                        flag = true;
                    }
                }
            }
            if (flag)
            {
                this.initBuyersAndSuppliers(tradingPartner.getBuyerOid(),
                        tradingPartner.getSupplierOid(), false);
                
                if (buyerList == null) buyerList = new ArrayList<BuyerHolder>();
                if (supplierList == null) supplierList = new ArrayList<SupplierHolder>();
                if (tradingPartner == null) tradingPartner = new TradingPartnerExHolder();
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
        }
    }
    
    
    public String saveEdit()
    {
        try
        {
            tradingPartner.trimAllString();
            tradingPartner.setAllEmptyStringToNull();
            tradingPartner.setUpdateDate(new Date());
            tradingPartner.setUpdateBy(this.getLoginIdOfCurrentUser());
            
            TradingPartnerHolder oldTp = tradingPartnerService
                    .selectTradingPartnerByKey(tradingPartner
                            .getTradingPartnerOid());
            tradingPartner.setCreateBy(oldTp.getCreateBy());
            tradingPartner.setCreateDate(oldTp.getCreateDate());
            
            tradingPartnerService.auditUpdateByPrimaryKey(
                    getCommonParameter(), oldTp, tradingPartner);
            this.getSession().remove(SESSION_SUPPLIER_OID_PARAMETER);
            this.getSession().remove(SESSION_TC_CODE_PARAMETER);
            
            log.info(this.getText(
                    "B2BPC0533",
                    new String[] { tradingPartner.getBuyerSupplierCode(),
                            this.getLoginIdOfCurrentUser() }));
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    // ***************************************
    // Delete function
    // ***************************************
    
    public String saveDelete()
    {

        try
        {
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            Object selectedOids = this.getSession().get(SESSION_OID_PARAMETER);
            if (null == selectedOids || "".equals(selectedOids))
            {
                msg.saveError(this.getText("alert.select.any"));
                msg.addMessageTarget(mt);
                log.info(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                return FORWARD_COMMON_MESSAGE;
            }
            
            this.getSession().remove(SESSION_OID_PARAMETER);
            
            
            String[] parts = selectedOids.toString().split(REQUEST_OID_DELIMITER);
            
            for (String part : parts)
            {
                BigDecimal tpOid = new BigDecimal(part);
                
                TradingPartnerHolder tp = tradingPartnerService.selectTradingPartnerByKey(tpOid);

                if (tp == null)
                {
                    msg.saveError(this.getText("B2BPC0520"));
                    continue;
                }
                
                tp.setGroupTradingPartners(new ArrayList<GroupTradingPartnerHolder>());
                
                List<GroupTradingPartnerHolder> rlt = groupTradingPartnerService.selectGroupTradingPartnerByTradingPartnerOid(tpOid);
                for (GroupTradingPartnerHolder o : rlt)
                    tp.getGroupTradingPartners().add(o);
                
                tradingPartnerService.removeTradingPartner(getCommonParameter(), tp);
                
                log.info(this.getText(
                        "B2BPC0523",
                        new String[] { tp.getBuyerSupplierCode(),
                                this.getLoginIdOfCurrentUser() }));
                msg.saveSuccess(this.getText(
                        "B2BPC0523",
                        new String[] { tp.getBuyerSupplierCode(),
                                this.getLoginIdOfCurrentUser() }));
            }
            
            msg.addMessageTarget(mt);
            
        }
        catch (Exception e)
        {
            handleException(e);
        }
        
        return FORWARD_COMMON_MESSAGE;
    
    }
    
    // ***************************************
    // Popup Term condition function
    // ***************************************

    public String viewTermCondition()
    {
        try
        {
            String msg = null;
            boolean flag = false;
            if (tradingPartner == null || tradingPartner.getSupplierOid() == null)
            {
                msg = this.getText("B2BPC0502");
                flag = true;
            }
            
            if (!flag)
            {
                termsConditionsList = termConditionService
                        .selectTermsConditionsBySupplierOid(tradingPartner
                                .getSupplierOid());
                if (termsConditionsList == null || termsConditionsList.isEmpty())
                {
                    msg = this.getText("B2BPC0503");
                    flag = true;
                    termsConditionsList = new ArrayList<Object>();
                }
            }

            if (!flag && tradingPartner.getTradingPartnerOid() != null
                    && this.getSession().get(SESSION_TC_CODE_PARAMETER) != null)
            {
                TermConditionHolder tc = termConditionService
                        .selectTermsConditionBySupplierOidAndTcCode(
                                new BigDecimal(this.getSession().get(SESSION_SUPPLIER_OID_PARAMETER).toString()),
                                this.getSession().get(SESSION_TC_CODE_PARAMETER).toString());
                
                if (tc != null)
                {
                    tradingPartner.setTermConditionOid(tc.getTermConditionOid());
                }
            }
                
            if (flag)
            {
                this.getRequest().setAttribute("ERROR_POPUP_TERM_CONDITION_FAILED", msg);
            }
            
        }
        catch (Exception e)
        {
            handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    // ***************************************
    // Private methods
    // ***************************************

    private void initBuyersAndSuppliers(BigDecimal buyerOid,
            BigDecimal supplierOid, Boolean isCreate) throws Exception
    {
        if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(3)))
        {
            supplierList = new ArrayList<SupplierHolder>();
            supplierList.add(supplierService.selectSupplierByKey(supplierOid));
            buyerList = buyerService.selectActiveBuyers();
            return;
        }

        if (this.getUserTypeOfCurrentUser().equals(BigDecimal.ONE))
        {
            buyerList = buyerService.selectActiveBuyers();
        }
        else if (this.getUserTypeOfCurrentUser().equals(BigDecimal.valueOf(2)))
        {
            buyerList = new ArrayList<BuyerHolder>();
            buyerList.add(buyerService.selectBuyerByKey(buyerOid));
        }
        if (isCreate)
        {
            supplierList = supplierService.selectActiveSuppliers();
        }
        else
        {
            supplierList = new ArrayList<SupplierHolder>();
            supplierList.add(supplierService.selectSupplierByKey(supplierOid));
        }
    }
    
    
    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);

        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                new String[] { tickNo }));

        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

        msg.addMessageTarget(mt);
    }

    // ***************************************
    // Getter & Setter methods
    // ***************************************

    public TradingPartnerExHolder getTradingPartner()
    {
        return tradingPartner;
    }


    public void setTradingPartner(TradingPartnerExHolder tradingPartner)
    {
        this.tradingPartner = tradingPartner;
    }


    public List<BuyerHolder> getBuyerList()
    {
        return buyerList;
    }


    public void setBuyerList(List<BuyerHolder> buyerList)
    {
        this.buyerList = buyerList;
    }


    public List<SupplierHolder> getSupplierList()
    {
        return supplierList;
    }


    public void setSupplierList(List<SupplierHolder> supplierList)
    {
        this.supplierList = supplierList;
    }


    public List<? extends Object> getTermsConditionsList()
    {
        return termsConditionsList;
    }


    public void setTermsConditionsList(List<Object> termsConditionsList)
    {
        this.termsConditionsList = termsConditionsList;
    }
    
    
}
