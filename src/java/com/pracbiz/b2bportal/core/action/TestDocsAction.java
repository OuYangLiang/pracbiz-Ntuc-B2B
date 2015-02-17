package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TermConditionService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;


public class TestDocsAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(TestDocsAction.class);
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String GENERATE_BATCH_INVOICE_PO_LIST = "TestDocsAction.generateBatchInvoicePoList";
    private MsgTransactionsExHolder param;
    
    private String message;
    private int poCount;
    
    @Autowired
    private transient PoHeaderService poHeaderService;
    @Autowired
    private transient PoService poService;
    @Autowired
    private transient SupplierService supplierService;
    @Autowired
    private transient InvoiceService invoiceService;
    @Autowired 
    transient protected TermConditionService termConditionService;
    @Autowired 
    transient protected TradingPartnerService tradingPartnerService;
    @Autowired 
    transient protected OidService oidService;
    @Autowired 
    transient protected PoLocationService poLocationService;
    @Autowired 
    transient protected MsgTransactionsService msgTransactionsService;
    @Autowired 
    transient protected BuyerService buyerService;
    

    public String initGenerateBatchInvoice()
    {
        return SUCCESS;
    }
    
    public String checkGenerateBatchInvoice()
    {
        try
        {
            param.setCreateDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getCreateDateFrom()));
            param.setCreateDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getCreateDateTo()));
            
            List<PoHeaderHolder> poHeaders = poHeaderService.selectPoHeaderToGenerateBatchInvoice(param.getCreateDateFrom(), param.getCreateDateTo());
            
            List<BigDecimal> poOidList = new ArrayList<BigDecimal>();
            if (poHeaders == null || poHeaders.isEmpty())
            {
                message = "There is no PO records in the selected time range";
                return SUCCESS;
            }
            
            poCount = poHeaders.size();
            
            for (PoHeaderHolder poHeader : poHeaders)
            {
                poOidList.add(poHeader.getPoOid());
            }

            this.getSession().put(GENERATE_BATCH_INVOICE_PO_LIST, poOidList);
            message = "Find " + poHeaders.size() + " POs to generate batch invoice.";
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            message = "exception occurs";
        }
        return SUCCESS;
    }
    
    public String saveGenerateBatchInvoice()
    {
        try
        {
            int successCount = 0;
            int errorCount = 0;
            int invCount = 0;
            
            List<BigDecimal> poOidList = (List<BigDecimal>) this.getSession().get(GENERATE_BATCH_INVOICE_PO_LIST);
            
            for (BigDecimal poOid : poOidList)
            {
                try
                {
                    PoHolder po = poService.selectPoByKey(poOid);
                    PoHeaderHolder poHeader = po.getPoHeader();
                    
                    if (poHeader.getPoType().equals(PoType.SOR))
                    {
                        if ("2".equals(poHeader.getPoSubType()))
                        {
                            generateInvoice(po, null);
                            invCount++;
                        }
                        else if ("1".equals(poHeader.getPoSubType()))
                        {
                            List<PoLocationHolder> locations = poLocationService.selectOptionalLocationsByPoOid(poHeader.getPoOid());
                            if (locations == null || locations.isEmpty())
                            {
                                continue;
                            }
                            for (PoLocationHolder location : locations)
                            {
                                generateInvoice(po, location.getLocationCode());
                                invCount++;
                            }
                        }
                        else
                        {
                            continue;
                        }
                    }
                    else if (poHeader.getPoType().equals(PoType.CON))
                    {
                        generateInvoice(po, null);
                        invCount++;
                    }
                    successCount++;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    errorCount++;
                }
            }
            
            message = successCount + " POs processed successfully, generate " + invCount + " Invoices ," + errorCount + " POs processed failed.";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            message = "exception occurs";
        }
        return SUCCESS;
    }
    
    
    private void generateInvoice(PoHolder po, String storeCode) throws Exception
    {
        log.info("begin generate invoice for po [" + po.getPoHeader().getPoNo() +"], " +
        		"buyer [" + po.getPoHeader().getBuyerCode() +"], supplier[" + po.getPoHeader().getSupplierCode()+"]");
        
        BigDecimal buyerOid = po.getPoHeader().getBuyerOid();
        BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
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
        
        BigDecimal oid = oidService.getOid();
        InvHolder inv = po.toInvoice(supplier, "volt" + oid, storeCode, term, 0, oid);
        inv.getHeader().setDeliveryDate(DateUtil.getInstance().dateAfterDays(poHeader.getPoDate(), 7));
        inv.getHeader().setInvStatus(InvStatus.SUBMIT);
        
        invoiceService.createAndSentInvoice(this.getCommonParameter(), inv, inv.getHeader().toMsgTransactions(), poHeader, buyer, supplier);
    }


    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getPoCount()
    {
        return poCount;
    }

    public void setPoCount(int poCount)
    {
        this.poCount = poCount;
    }

    public MsgTransactionsExHolder getParam()
    {
        return param;
    }

    public void setParam(MsgTransactionsExHolder param)
    {
        this.param = param;
    }
    
    
}
