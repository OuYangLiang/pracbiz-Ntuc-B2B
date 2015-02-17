//*****************************************************************************
//
// File Name       :  ItemServiceImpl.java
// Date Created    :  Oct 10, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Oct 10, 2013 5:45:06 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.eai.file.ItemFileParser;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.ItemBarcodeHolder;
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;
import com.pracbiz.b2bportal.core.holder.extension.ClassExHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SubclassExHolder;
import com.pracbiz.b2bportal.core.mapper.ItemMapper;
import com.pracbiz.b2bportal.core.service.ClassService;
import com.pracbiz.b2bportal.core.service.ItemBarcodeService;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SubclassService;
import com.pracbiz.b2bportal.core.service.TransactionBatchService;
import com.pracbiz.b2bportal.core.service.UserClassService;
import com.pracbiz.b2bportal.core.service.UserClassTmpService;
import com.pracbiz.b2bportal.core.service.UserSubclassService;
import com.pracbiz.b2bportal.core.service.UserSubclassTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class ItemServiceImpl extends DBActionServiceDefaultImpl<ItemHolder>
    implements ItemService
{
    @Autowired
    private OidService oidService;
    @Autowired
    private ClassService classService;
    @Autowired
    private SubclassService subclassService;
    @Autowired
    private ItemBarcodeService itemBarcodeService;
    @Autowired
    private UserClassService userClassService;
    @Autowired
    private UserClassTmpService userClassTmpService;
    @Autowired
    private UserSubclassService userSubclassService;
    @Autowired
    private UserSubclassTmpService userSubclassTmpService;
    @Autowired
    private MsgTransactionsService msgTransactionsService;
    @Autowired
    private TransactionBatchService transactionBatchService;
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
    
    @Autowired
    private ItemMapper mapper; 
    
    private static final String batchType = "IM";
    
    @Override
    public void deleteAndInsertItem(BuyerHolder buyer, Map<String, List<String>> fileMap , TransactionBatchHolder transBatch, String fileFormat,  List<String> successList)
        throws Exception
    {
        transactionBatchService.insert(transBatch);
        
        log.info("start to delete and insert item");
        
        log.info("Deleting barcode table...");
        itemBarcodeService.deleteItemBarcodeByBuyerOid(buyer.getBuyerOid());
        
        log.info("Deleting item records...");
        this.deleteItemByBuyerOid(buyer.getBuyerOid());
        
        for (Entry<String, List<String>> fileEntry : fileMap.entrySet())
        {
            MsgTransactionsExHolder msgTrans = this.initMsgTrans(fileEntry.getKey(),  buyer);

            Map<String, ItemHolder> insertItemMap = ItemFileParser.getInstance().initiateFileItemMap(buyer,
                msgTrans, fileEntry.getValue(), fileFormat);
            
            msgTransactionsService.insert(msgTrans);
            
            long i = 0;
            for(Map.Entry<String, ItemHolder> entry : insertItemMap.entrySet())
            {
                this.insert(entry.getValue());
                if (null != entry.getValue().getBarcodes() && !entry.getValue().getBarcodes().isEmpty())
                {
                    for (String barcode : entry.getValue().getBarcodes())
                    {
                        ItemBarcodeHolder itemBarcode = new ItemBarcodeHolder();
                        itemBarcode.setItemOid(entry.getValue().getItemOid());
                        itemBarcode.setBarcode(barcode);
                        itemBarcodeService.insert(itemBarcode);
                    }
                }
                
                i++;
                if ( (i % 10000) == 0)
                    log.info( ((i / 10000) * 10) + " thousand records inserted...");
            }
            
            successList.add(fileEntry.getKey());
        }
        
        
        log.info("end to delete and insert item");
        
        log.info("Handling class & subClass...");
        processClassAndSubclass(buyer.getBuyerOid());
        
    }
    
    
    @Override
    public void updateItem(BuyerHolder buyer, Map<String, List<String>> fileMap ,TransactionBatchHolder transBatch, String fileFormat, List<String> successList)
        throws Exception
    {
        log.info("start to update item .");
        
        transactionBatchService.insert(transBatch);
        
        for (Entry<String, List<String>> fileEntry : fileMap.entrySet())
        {
            MsgTransactionsExHolder msgTrans = this.initMsgTrans(fileEntry.getKey(), buyer);
            
            Map<String, ItemHolder> insertMap = ItemFileParser.getInstance().initiateFileItemMap(buyer,
                msgTrans, fileEntry.getValue(), fileFormat);
            
            Map<BigInteger, ItemHolder> updateItemMap = null;
            if (insertMap != null && !insertMap.isEmpty())
            {
               updateItemMap = this.filterUpdateItemMap(buyer.getBuyerOid(), insertMap);
            }
            
            msgTransactionsService.insert(msgTrans);
            
            if (updateItemMap != null && !updateItemMap.isEmpty())
            {
                for (Map.Entry<BigInteger, ItemHolder> updateEntry : updateItemMap.entrySet())
                {
                    this.updateByPrimaryKey(null, updateEntry.getValue());
                    itemBarcodeService.deleteItemBarcodeByItemOid(updateEntry.getValue().getItemOid());
                    
                    if (null != updateEntry.getValue().getBarcodes() && !updateEntry.getValue().getBarcodes().isEmpty())
                    {
                        for (String barcode : updateEntry.getValue().getBarcodes())
                        {
                            ItemBarcodeHolder itemBarcode = new ItemBarcodeHolder();
                            itemBarcode.setItemOid(updateEntry.getValue().getItemOid());
                            itemBarcode.setBarcode(barcode);
                            itemBarcodeService.insert(itemBarcode);
                        }
                    }
                    
                }
            }
            
            if (insertMap != null && !insertMap.isEmpty())
            {
                for (Map.Entry<String, ItemHolder> insertEntry : insertMap.entrySet())
                {
                    this.insert(insertEntry.getValue());
                    
                    if (null != insertEntry.getValue().getBarcodes())
                    {
                        for (String barcode : insertEntry.getValue().getBarcodes())
                        {
                            ItemBarcodeHolder itemBarcode = new ItemBarcodeHolder();
                            itemBarcode.setItemOid(insertEntry.getValue().getItemOid());
                            itemBarcode.setBarcode(barcode);
                            itemBarcodeService.insert(itemBarcode);
                        }
                    }
                    
                }
            }
            
            log.info("end to update item .");

            //get from db to reinsert class and subclass
            //initClassAndSubClassForUpdate(classMap, subClassMap, buyer);
            
            log.info("start to insert class and subClass for update");
            processClassAndSubclass(buyer.getBuyerOid());
            //processClassAndSubclass(classMap, subClassMap, buyer.getBuyerOid());
            
            successList.add(fileEntry.getKey());
        }
        
    }
    
    private void processClassAndSubclass(BigDecimal buyerOid) throws Exception
    {
        List<ItemHolder> itemList = this.selectClassAndSubclassCodeByBuyerOid(buyerOid);
        Map<String, ClassHolder> classMap = new HashMap<String, ClassHolder>();
        Map<String, List<SubclassHolder>> subClassMapUseClassCodeAsKey = new HashMap<String, List<SubclassHolder>>();
        Map<String, Map<String, SubclassHolder>> relationMap = new HashMap<String, Map<String, SubclassHolder>>();
        
        if (itemList != null)
        {
            for (ItemHolder item : itemList)
            {
                if (item.getClassCode() == null || item.getClassCode().trim().isEmpty())
                {
                    continue;
                }
                
                if (!classMap.containsKey(item.getClassCode().toUpperCase()))
                {
                    classMap.put(item.getClassCode().toUpperCase(), new ClassHolder(oidService.getOid(), 
                            item.getClassCode(), item.getClassDesc(), buyerOid));
                }
                
                if (item.getSubclassCode() != null && !item.getSubclassCode().isEmpty())
                {
                    if (subClassMapUseClassCodeAsKey.containsKey(item.getClassCode().toUpperCase()))
                    {
                        subClassMapUseClassCodeAsKey.get(item.getClassCode().toUpperCase()).add(new SubclassHolder(oidService.getOid(), 
                                item.getSubclassCode(), item.getSubclassDesc(), 
                                classMap.get(item.getClassCode().toUpperCase()).getClassOid()));
                    }
                    else
                    {
                        List<SubclassHolder> subclassList = new ArrayList<SubclassHolder>();

                        subclassList.add(new SubclassHolder(oidService.getOid(), 
                            item.getSubclassCode(), item.getSubclassDesc(), 
                            classMap.get(item.getClassCode().toUpperCase()).getClassOid()));
                        subClassMapUseClassCodeAsKey.put(item.getClassCode().toUpperCase(), subclassList);
                    }
                }
            }
        }
        
        if (subClassMapUseClassCodeAsKey != null && !subClassMapUseClassCodeAsKey.isEmpty())
        {
            for (Map.Entry<String, List<SubclassHolder>> entry : subClassMapUseClassCodeAsKey.entrySet())
            {
                Map<String, SubclassHolder> subMap = new HashMap<String, SubclassHolder>();
                for (SubclassHolder subclass : entry.getValue())
                {
                    if (!subMap.containsKey(subclass.getSubclassCode().toUpperCase()))
                    {
                        subMap.put(subclass.getSubclassCode().toUpperCase(), subclass);
                    }
                }
                relationMap.put(entry.getKey(), subMap);
            }
        }
        
        
        List<ClassHolder> insertClassList = new ArrayList<ClassHolder>();
        List<BigDecimal> deleteClassOids = new ArrayList<BigDecimal>();
        List<SubclassHolder> insertSubclassList = new ArrayList<SubclassHolder>();
        List<BigDecimal> deleteSubclassOids = new ArrayList<BigDecimal>();
        
        List<ClassExHolder> oldClassList = classService.selectClassByBuyerOid(buyerOid);
        
        if (classMap != null && !classMap.isEmpty())
        {
            if (oldClassList == null || oldClassList.isEmpty())
            {
                for (Map.Entry<String, ClassHolder> entry : classMap.entrySet())
                {
                    insertClassList.add(entry.getValue());
                }
            }
            else
            {
                for (Map.Entry<String, ClassHolder> entry : classMap.entrySet())
                {
                    boolean exist = false;
                    for (ClassExHolder oldClass : oldClassList)
                    {
                        if (entry.getKey().equalsIgnoreCase(oldClass.getClassCode()))
                        {
                            entry.getValue().setClassOid(oldClass.getClassOid());
                            exist = true;
                            break;
                        }
                    }
                    
                    if (!exist)
                    {
                        insertClassList.add(entry.getValue());
                    }
                }
            }
        }
        
        if (oldClassList != null && !oldClassList.isEmpty())
        {
            if (classMap == null || classMap.isEmpty())
            {
                for (ClassExHolder oldClass : oldClassList)
                {
                    deleteClassOids.add(oldClass.getClassOid());
                }
            }
            else
            {
                for (ClassExHolder oldClass : oldClassList)
                {
                    boolean exist = false;
                    for (Map.Entry<String, ClassHolder> entry : classMap.entrySet())
                    {
                        if (entry.getKey().equalsIgnoreCase(oldClass.getClassCode()))
                        {
                            exist = true;
                            break;
                        }
                    }
                    
                    if (!exist)
                    {
                        deleteClassOids.add(oldClass.getClassOid());
                    }
                }
            }
        }
        
        
        Map<String, List<SubclassHolder>> subclassMapUseClassCodeAsKey = new HashMap<String, List<SubclassHolder>>();
        
        if (relationMap != null && !relationMap.isEmpty())
        {
            for (Map.Entry<String, Map<String, SubclassHolder>> subclassEntry : relationMap.entrySet())
            {
                List<SubclassHolder> scList = new ArrayList<SubclassHolder>();
                for (Map.Entry<String, SubclassHolder> entry : subclassEntry.getValue().entrySet())
                {
                    scList.add(entry.getValue());
                }
                subclassMapUseClassCodeAsKey.put(subclassEntry.getKey(), scList);
            }
        }
        
        List<SubclassExHolder> oldSubclassList = subclassService.selectSubclassExByBuyerOid(buyerOid);
        
        Map<String, List<SubclassExHolder>> oldSubclassMapUseClassCodeAsKey = new HashMap<String, List<SubclassExHolder>>();
        
        if (oldSubclassList != null && !oldSubclassList.isEmpty())
        {
            for (SubclassExHolder oldSubclass : oldSubclassList)
            {
                String classCode = oldSubclass.getClassCode().trim().toUpperCase();
                if (oldSubclassMapUseClassCodeAsKey.containsKey(classCode))
                {
                    oldSubclassMapUseClassCodeAsKey.get(classCode).add(oldSubclass);
                }
                else
                {
                    List<SubclassExHolder> tmpOldSubclassList = new ArrayList<SubclassExHolder>();
                    tmpOldSubclassList.add(oldSubclass);
                    oldSubclassMapUseClassCodeAsKey.put(classCode, tmpOldSubclassList);
                }
            }
        }
        

        if (subclassMapUseClassCodeAsKey != null && !subclassMapUseClassCodeAsKey.isEmpty())
        {
            for (Map.Entry<String, List<SubclassHolder>> entry : subclassMapUseClassCodeAsKey.entrySet())
            {
                String classCode = entry.getKey();
                List<SubclassExHolder> oldSubclassHolderList = oldSubclassMapUseClassCodeAsKey.get(classCode);
                if (oldSubclassHolderList == null || oldSubclassHolderList.isEmpty())
                {
                    for (SubclassHolder subclass : entry.getValue())
                    {
                        subclass.setClassOid(classMap.get(classCode).getClassOid());
                        insertSubclassList.add(subclass);
                    }
                }
                else
                {
                    for (SubclassHolder subclass : entry.getValue())
                    {
                        boolean exist = false;
                        for (SubclassExHolder oldSubclass : oldSubclassHolderList)
                        {
                            if (subclass.getSubclassCode().equalsIgnoreCase(oldSubclass.getSubclassCode()))
                            {
                                exist = true;
                                break;
                            }
                        }
                        
                        if (!exist)
                        {
                            subclass.setClassOid(classMap.get(classCode).getClassOid());
                            insertSubclassList.add(subclass);
                        }
                    }
                }
            }
        }
        
        if (oldSubclassMapUseClassCodeAsKey != null && !oldSubclassMapUseClassCodeAsKey.isEmpty())
        {
            for (Map.Entry<String, List<SubclassExHolder>> entry : oldSubclassMapUseClassCodeAsKey.entrySet())
            {
                List<SubclassHolder> subclassList = subclassMapUseClassCodeAsKey.get(entry.getKey());
                if (subclassList == null || subclassList.isEmpty())
                {
                    for (SubclassExHolder oldSubclass : entry.getValue())
                    {
                        deleteSubclassOids.add(oldSubclass.getSubclassOid());
                    }
                }
                else
                {
                    for (SubclassExHolder oldSubclass : entry.getValue())
                    {
                        boolean exist = false;
                        for (SubclassHolder subclass : subclassList)
                        {
                            if (subclass.getSubclassCode().equalsIgnoreCase(oldSubclass.getSubclassCode()))
                            {
                                exist = true;
                                break;
                            }
                        }
                        
                        if (!exist)
                        {
                            deleteSubclassOids.add(oldSubclass.getSubclassOid());
                        }
                    }
                }
            }
        }
        
        if (!deleteSubclassOids.isEmpty())
        {
            for (BigDecimal subclassOid : deleteSubclassOids)
            {
                userSubclassService.deleteBySubclassOid(subclassOid);
                
                userSubclassTmpService.deleteBySubclassOid(subclassOid);
                
                subclassService.deleteBySubclassOid(subclassOid);
            }
        }
        
        if (!deleteClassOids.isEmpty())
        {
            for (BigDecimal classOid : deleteClassOids)
            {
                userClassService.deleteByClassOid(classOid);
                
                userClassTmpService.deleteByClassOid(classOid);
                
                classService.deleteByClassOid(classOid);
            }
        }
        
        if (!insertClassList.isEmpty())
        {
            for (ClassHolder clazz : insertClassList)
            {
                classService.insert(clazz);
            }
        }
        
        if (!insertSubclassList.isEmpty())
        {
            for (SubclassHolder subclass : insertSubclassList)
            {
                subclassService.insert(subclass);
            }
        }
        
    }
    
    
    @Override
    public void delete(ItemHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
        
    }
    
    
    @Override
    public void insert(ItemHolder newObj) throws Exception
    {
        mapper.insert(newObj);
        
    }
    
    
    @Override
    public void updateByPrimaryKey(ItemHolder oldObj, ItemHolder newObj)
        throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
        
    }
    
    
    @Override
    public void updateByPrimaryKeySelective(ItemHolder oldObj, ItemHolder newObj)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public List<ItemHolder> select(ItemHolder param) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    @Override
    public List<ItemHolder> selectClassAndSubclassCodeByBuyerOid(BigDecimal buyerOid) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        ItemHolder item = new ItemHolder();
        item.setBuyerOid(buyerOid);
        
        return mapper.selectClassAndSubclassCodeByBuyerOid(item);
    }

    
    @Override
    public int selectCountOfItem(ItemHolder item) throws Exception
    {
        return mapper.selectCountOfItem(item);
    }
    
    
    @Override
    public int selectCountOfItemByBuyer(BigDecimal buyerOid) throws Exception
    {
        ItemHolder param = new ItemHolder();
        param.setBuyerOid(buyerOid);
        return mapper.selectCountOfItem(param);
    }
    
    
    
    @Override
    public ItemHolder selectItemByBuyerOidAndBuyerItemCode(BigDecimal buyerOid,
        String buyerItemCode) throws Exception
    {
        if (buyerOid == null || buyerItemCode == null || buyerItemCode.isEmpty())
        {
             throw new IllegalArgumentException();
        }
        ItemHolder item = new ItemHolder();
        item.setBuyerOid(buyerOid);
        item.setBuyerItemCode(buyerItemCode);
        
        List<ItemHolder> rlt = mapper.select(item);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }
    

    @Override
    public void deleteItemByBuyerOid(BigDecimal buyerOid) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        ItemHolder item = new ItemHolder();
        item.setBuyerOid(buyerOid);
        mapper.delete(item);
        
    }


    @Override
    public List<String> selectActiveItemsByUserOid(BigDecimal userOid)
        throws Exception
    {
        if (null == userOid)
        {
            throw new IllegalArgumentException();
        }
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("userOid", userOid);
        return mapper.selectActiveItemsByUserOid(map);
    }


    @Override
    public List<ItemHolder> selectBuyerItemCodeByBuyerOid(BigDecimal buyerOid)
        throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.selectBuyerItemCodeByBuyerOid(buyerOid);
    }

    
    private MsgTransactionsExHolder initMsgTrans(String fileName, BuyerHolder buyer) throws Exception
    {
        MsgTransactionsExHolder msgTrans = new MsgTransactionsExHolder();
        
        msgTrans.setDocOid(oidService.getOid());
        msgTrans.setActive(true);
        msgTrans.setValid(true);
        msgTrans.setMsgType(batchType);
        msgTrans.setCreateDate(new Date());
        msgTrans.setBuyerOid(buyer.getBuyerOid());
        msgTrans.setBuyerCode(buyer.getBuyerCode());
        msgTrans.setBuyerName(buyer.getBuyerName());
        String spliteFileName = FileUtil.getInstance().trimAllExtension(fileName).split("_")[2];
        msgTrans.setMsgRefNo(spliteFileName);
        
        return msgTrans;
        
    }
    
    
    private Map<BigInteger, ItemHolder> filterUpdateItemMap(BigDecimal buyerOid, Map<String, ItemHolder> fileItemMap) throws Exception
    {
        Map<BigInteger, ItemHolder> rlt = new HashMap<BigInteger, ItemHolder>();
        
        Iterator<ItemHolder> it = fileItemMap.values().iterator();
        
        while (it.hasNext())
        {
            ItemHolder curObj = it.next();
            
            ItemHolder oldItem = this.selectItemByBuyerOidAndBuyerItemCode(buyerOid, curObj.getBuyerItemCode());
            
            if (null != oldItem)
            {
                curObj.setItemOid(oldItem.getItemOid());
                rlt.put(oldItem.getItemOid(), curObj);
                it.remove();
            }
            
        }
        
        return rlt;
    }
    
}

