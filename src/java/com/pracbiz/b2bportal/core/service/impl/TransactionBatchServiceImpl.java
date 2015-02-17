package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.visitor.TxnLoggerVisitor;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.extension.BuyerStoreExHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreAreaMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreUserMapper;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreUserTmpMapper;
import com.pracbiz.b2bportal.core.mapper.MsgTransactionsMapper;
import com.pracbiz.b2bportal.core.mapper.TransactionBatchMapper;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.TransactionBatchService;

public class TransactionBatchServiceImpl extends DBActionServiceDefaultImpl<TransactionBatchHolder> 
             implements TransactionBatchService
{
    @Autowired  private TransactionBatchMapper mapper;
    @Autowired  private OidService oidService;
    @Autowired  private MsgTransactionsMapper msgMapper;
    @Autowired  private TxnLoggerVisitor txnLoggerVisitor;
    
    @Autowired private BuyerStoreUserMapper buyerStoreUserMapper;
    @Autowired private BuyerStoreUserTmpMapper buyerStoreUserTmpMapper;
    @Autowired private BuyerStoreMapper buyerStoreMapper;
    @Autowired private BuyerStoreAreaMapper buyerStoreAreaMapper;
    
    
    @Override
    public void delete(TransactionBatchHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void insert(TransactionBatchHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
        
    }

    @Override
    public void updateByPrimaryKey(TransactionBatchHolder oldObj_,
        TransactionBatchHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKeySelective(TransactionBatchHolder oldObj_,
        TransactionBatchHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void insertOutboundBatch(BatchMsg batch) throws Exception
    {
        TransactionBatchHolder transactionBatch = new TransactionBatchHolder();
        transactionBatch.setBatchOid(oidService.getOid());
        transactionBatch.setBatchNo(batch.getBatchNo());
        transactionBatch.setBatchFilename(batch.getBatchFileName());
        transactionBatch.setCreateDate(batch.getInDate());
        
        this.insert(transactionBatch);
        
        for(DocMsg docMsg:batch.getDocs())
        {
            if (docMsg.isOriginalFileGeneratedOnPortal())
            {
                MsgTransactionsHolder msg = this.selectMsgByKey(docMsg.getDocOid());
                
                msg.setProcDate(docMsg.getProcDate());
                msg.setOutDate(docMsg.getOutDate());
                msg.setExchangeFilename(docMsg.getTargetFilename());
                msg.setReportFilename(docMsg.getReportFilename());
                msg.setActive(docMsg.isActive());
                msg.setValid(docMsg.isValid());
                msg.setRemarks(docMsg.getRemarks());
                msg.setBatchOid(transactionBatch.getBatchOid());
                msg.setGeneratedOnPortal(true);
                
                msgMapper.updateByPrimaryKeySelective(msg);
            }
            else
            {
                MsgTransactionsHolder msg = docMsg.convertToMsgTransactions();
                msg.setBatchOid(transactionBatch.getBatchOid());
                msg.setGeneratedOnPortal(false);
                
                msgMapper.insert(msg);
                if (msg.getActive() && msg.getValid())
                {
                    docMsg.accept(txnLoggerVisitor);
                }
            }
        }
        
    }

    
    @Override
    public void insertInboundBatch(BatchMsg batch) throws Exception
    {
        for (DocMsg docMsg : batch.getDocs())
        {
            if (docMsg.isOriginalFileGeneratedOnPortal())
            {
                MsgTransactionsHolder msg = this.selectMsgByKey(docMsg.getDocOid());
                
                msg.setProcDate(docMsg.getProcDate());
                msg.setOutDate(docMsg.getOutDate());
                msg.setExchangeFilename(docMsg.getTargetFilename());
                msg.setActive(docMsg.isActive());
                msg.setValid(docMsg.isValid());
                msg.setRemarks(docMsg.getRemarks());
                msg.setGeneratedOnPortal(true);
                
                msgMapper.updateByPrimaryKeySelective(msg);
            }
            else
            {
                MsgTransactionsHolder msg = docMsg.convertToMsgTransactions();
                msg.setGeneratedOnPortal(false);
                
                msgMapper.insert(msg);
                if (msg.getActive() && msg.getValid())
                {
                    docMsg.accept(txnLoggerVisitor);
                }
            }
        }
    }
    
    
    private MsgTransactionsHolder selectMsgByKey(BigDecimal docOid)
        throws Exception
    {
        if(docOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        MsgTransactionsExHolder param = new MsgTransactionsExHolder();
        param.setDocOid(docOid);
        
        List<MsgTransactionsHolder> rlt = msgMapper.select(param);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }

    
    @Override
    public void insertStoreMaster(MsgTransactionsHolder msg,
        List<BuyerStoreExHolder> recordList, List<BuyerStoreHolder> deleteList)
        throws Exception
    {
        msgMapper.insert(msg);
        
        for (BuyerStoreExHolder store : recordList)
        {
            BuyerStoreHolder oldStore = this.selectStoreByBuyerAndCode(store.getBuyerCode(), store.getStoreCode());
            
            if (store.getAreaCode() != null)
            {
                BuyerStoreAreaHolder area = this.selectStoreAreaByBuyerAndCode(store.getBuyerCode(), store.getAreaCode());
                if (area == null)
                {
                    area = new BuyerStoreAreaHolder();
                    area.setAreaOid(oidService.getOid());
                    area.setAreaCode(store.getAreaCode());
                    area.setAreaName(null);
                    area.setBuyerCode(store.getBuyerCode());
                    area.setCreateDate(new Date());
                    
                    buyerStoreAreaMapper.insert(area);
                    
                    store.setAreaOid(area.getAreaOid());
                    
                }

                store.setAreaOid(area.getAreaOid());
            }
            
            if (null == oldStore)
            {
                store.setCreateDate(new Date());
                store.setStoreOid(oidService.getOid());
                buyerStoreMapper.insert(store);
            }
            else
            {
                store.setUpdateDate(new Date());
                store.setStoreOid(oldStore.getStoreOid());
                store.setCreateDate(oldStore.getCreateDate());
                buyerStoreMapper.updateByPrimaryKey(store);
            }
        }
        
        for (BuyerStoreHolder store : deleteList)
        {
            BuyerStoreUserTmpHolder storeUserTmp = new BuyerStoreUserTmpHolder();
            storeUserTmp.setStoreOid(store.getStoreOid());
            buyerStoreUserMapper.delete(storeUserTmp);
            buyerStoreUserTmpMapper.delete(storeUserTmp);
            buyerStoreMapper.delete(store);
        }
    }
    
    
    private BuyerStoreHolder selectStoreByBuyerAndCode(String buyerCode, String storeCode)
    {
        BuyerStoreExHolder param = new BuyerStoreExHolder();
        param.setBuyerCode(buyerCode);
        param.setStoreCode(storeCode);
        
        List<BuyerStoreHolder> rlt = buyerStoreMapper.select(param);
        
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }
    
    private BuyerStoreAreaHolder selectStoreAreaByBuyerAndCode(String buyerCode, String areaCode)
    {
        BuyerStoreAreaHolder param = new BuyerStoreAreaHolder();
        param.setBuyerCode(buyerCode);
        param.setAreaCode(areaCode);
        
        List<BuyerStoreAreaHolder> rlt = buyerStoreAreaMapper.select(param);
        
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }

    @Override
    public TransactionBatchHolder selectByBatchFileName(String batchFilename)
    {
        if (batchFilename == null || batchFilename.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        TransactionBatchHolder param = new TransactionBatchHolder();
        param.setBatchFilename(batchFilename);
        List<TransactionBatchHolder> batchs = mapper.select(param);
        
        if (batchs == null || batchs.isEmpty())
        {
            return null;
        }
        
        return batchs.get(0);
    }

}
