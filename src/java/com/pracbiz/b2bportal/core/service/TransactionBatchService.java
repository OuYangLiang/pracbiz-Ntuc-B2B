package com.pracbiz.b2bportal.core.service;

import java.util.List;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerStoreExHolder;

public interface TransactionBatchService extends
    DBActionService<TransactionBatchHolder>
{
    public void insertOutboundBatch(BatchMsg batch) throws Exception;

    public void insertInboundBatch(BatchMsg batch) throws Exception;

    public void insertStoreMaster(MsgTransactionsHolder msg,
        List<BuyerStoreExHolder> recordList, List<BuyerStoreHolder> deleteList)
        throws Exception;
    
    public TransactionBatchHolder selectByBatchFileName(String batchFilename);
}
