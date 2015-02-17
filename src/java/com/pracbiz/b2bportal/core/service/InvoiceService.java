package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvSummaryHolder;


public interface InvoiceService extends DBActionService<InvHolder>
{
    public String doRunningNumber(SupplierHolder supplier) throws Exception;
    
    
    public void createInvoice(CommonParameterHolder cp, InvHolder invoice,
        MsgTransactionsHolder msg, PoHeaderHolder poHeader, BuyerHolder buyer,
        SupplierHolder supplier) throws Exception;
    
    public byte[] generatePdf(InvHolder invoice, BuyerHolder buyer,
        SupplierHolder supplier, MsgTransactionsHolder msg,BuyerMsgSettingReportHolder setting) throws Exception;
    
    public void createAndSentInvoice(CommonParameterHolder cp, InvHolder invoice,
        MsgTransactionsHolder msg, PoHeaderHolder poHeader, BuyerHolder buyer,
        SupplierHolder supplier) throws Exception;
    
    public void sentInvoice(CommonParameterHolder cp, InvHolder invoice,
        MsgTransactionsHolder msg, SupplierHolder supplier)throws Exception;
    
    public InvHolder selectInvoiceByKey(BigDecimal invOid) throws Exception;
    
    
    public void editInvoice(CommonParameterHolder cp,InvHolder invoice) throws Exception;
    
    
    public void editAndSendInvoice(CommonParameterHolder cp, InvHolder invoice) throws Exception;
    
    
    public byte[] exportExcel(List<BigDecimal> invOids, boolean storeFlag) throws Exception;
    
    
    public byte[] exportSummaryExcel(List<InvSummaryHolder> params, boolean storeFlag) throws Exception;
}
