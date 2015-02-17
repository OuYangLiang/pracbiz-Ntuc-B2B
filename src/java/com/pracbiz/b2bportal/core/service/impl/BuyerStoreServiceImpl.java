//*****************************************************************************
//
// File Name       :  BuyerStoreServiceImpl.java
// Date Created    :  2013-1-14
// Last Changed By :  $Author: xuchengqing $
// Last Changed On :  $Date: 2011-07-01 10:56:27 +0800 (周五, 01 七月 2011) $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreMapper;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserTmpService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class BuyerStoreServiceImpl extends
        DBActionServiceDefaultImpl<BuyerStoreHolder> implements
        BuyerStoreService
{
    @Autowired
    private BuyerStoreMapper mapper;
    @Autowired
    private BuyerStoreUserService buyerStoreUserService;
    @Autowired
    private BuyerStoreUserTmpService buyerStoreUserTmpService;
    @Autowired
    private BuyerStoreAreaUserService buyerStoreAreaUserService;
    @Autowired
    private BuyerStoreAreaUserTmpService buyerStoreAreaUserTmpService;
    
    private WritableCellFormat  format0;
    private WritableCellFormat format1;
    private WritableCellFormat  format2;

    @Override
    public BuyerStoreHolder selectBuyerStoreByBuyerCodeAndStoreCode(String buyerCode,
            String storeCode) throws Exception
    {
        if (buyerCode == null || buyerCode.isEmpty() || storeCode == null
                || storeCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        BuyerStoreHolder param = new BuyerStoreHolder();
        param.setBuyerCode(buyerCode);
        param.setStoreCode(storeCode);

        List<BuyerStoreHolder> rlt = mapper.select(param);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        return null;
    }


    @Override
    public BuyerStoreHolder selectBuyerStoreByStoreOid(BigDecimal storeOid)
            throws Exception
    {
        if (storeOid == null)
        {
            throw new IllegalArgumentException();
        }

        BuyerStoreHolder param = new BuyerStoreHolder();
        param.setStoreOid(storeOid);

        List<BuyerStoreHolder> rlt = mapper.select(param);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        return null;
    }
    

    @Override
    public List<BuyerStoreHolder> selectBuyerStoresByAreaOid(BigDecimal areaOid)
            throws Exception
    {
        if (areaOid == null)
        {
            throw new IllegalArgumentException();
        }

        BuyerStoreHolder param = new BuyerStoreHolder();
        param.setAreaOid(areaOid);

        return mapper.select(param);
    }
    

    @Override
    public List<BuyerStoreHolder> selectBuyerStoresWithoutAreaByBuyer(
            String buyerCode) throws Exception
    {
        if (buyerCode == null || buyerCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        return mapper.selectBuyerStoresWithoutAreaByBuyer(buyerCode);
    }


    @Override
    public void delete(BuyerStoreHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(BuyerStoreHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(BuyerStoreHolder oldObj,
            BuyerStoreHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(BuyerStoreHolder oldObj,
            BuyerStoreHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public List<BuyerStoreHolder> select(BuyerStoreHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public int getCountOfSummary(BuyerStoreHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<BuyerStoreHolder> getListOfSummary(BuyerStoreHolder param)
            throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public void assignUserToStore(CommonParameterHolder cp,
            List<BuyerStoreUserTmpHolder> addStoreUsers,
            List<BuyerStoreUserTmpHolder> deleteStoreUsers) throws Exception
    {
        if (addStoreUsers != null)
        {
            for (BuyerStoreUserTmpHolder obj : addStoreUsers)
            {
                buyerStoreUserService.insert(obj);
                buyerStoreUserTmpService.insert(obj);
            }
        }
        
        if (deleteStoreUsers == null || deleteStoreUsers.isEmpty())
        {
            return;
        }
        
        for (BuyerStoreUserTmpHolder obj : deleteStoreUsers)
        {
            int type = buyerStoreUserService.defineRelationBetweenUserAndStore(obj.getStoreOid(), obj.getUserOid());
            //select this store;
            if (type == 1)
            {
                buyerStoreUserService.delete(obj);
                buyerStoreUserTmpService.delete(obj);
            }
            //select all stores
            else if (type == -3 || type == -4)
            {
                List<BuyerStoreHolder> allStores = this.selectBuyerStoresByBuyerAndIsWareHouse(
                        this.selectBuyerStoreByStoreOid(obj.getStoreOid()).getBuyerCode(), type == -3 ? false : true);
                
                for (BuyerStoreHolder store : allStores)
                {
                    if (store.getStoreOid().equals(obj.getStoreOid()))
                    {
                        allStores.remove(store);
                        break;
                    }
                }
                List<BuyerStoreUserTmpHolder> result = this.convertToBuyerStoreUserTmp(allStores, obj.getUserOid());
                
                BuyerStoreUserTmpHolder param = new BuyerStoreUserTmpHolder();
                param.setUserOid(obj.getUserOid());
                param.setStoreOid(BigDecimal.valueOf(type));
                buyerStoreUserService.delete(param);
                buyerStoreUserTmpService.delete(param);
                
                if (result == null || result.isEmpty())
                {
                    continue;
                }
                for (BuyerStoreUserTmpHolder tmp : result)
                {
                    buyerStoreUserService.insert(tmp);
                    buyerStoreUserTmpService.insert(tmp);
                }
            }
            //select an area containing this store
            else if (type == 2)
            {
                BigDecimal areaOid = buyerStoreAreaUserService.selectAreaUserByUserOid(obj.getUserOid()).get(0).getAreaOid();
                List<BuyerStoreHolder> allStores = this.selectBuyerStoresByAreaOid(areaOid);
                for (BuyerStoreHolder store : allStores)
                {
                    if (store.getStoreOid().equals(obj.getStoreOid()))
                    {
                        allStores.remove(store);
                        break;
                    }
                }
                
                List<BuyerStoreUserTmpHolder> result = this.convertToBuyerStoreUserTmp(allStores, obj.getUserOid());
                
                BuyerStoreAreaUserTmpHolder param = new BuyerStoreAreaUserTmpHolder();
                param.setUserOid(obj.getUserOid());
                buyerStoreAreaUserService.delete(param);
                buyerStoreAreaUserTmpService.delete(param);
                
                if (result == null || result.isEmpty())
                {
                    continue;
                }
                for (BuyerStoreUserTmpHolder tmp : result)
                {
                    buyerStoreUserService.insert(tmp);
                    buyerStoreUserTmpService.insert(tmp);
                }
            }
            //select all areas containing this store
            else if (type == 3)
            {
                List<BuyerStoreHolder> allStoreList = this.selectBuyerStoresByBuyer(
                        this.selectBuyerStoreByStoreOid(obj.getStoreOid()).getBuyerCode());
                List<BuyerStoreHolder> allStores = new ArrayList<BuyerStoreHolder>();
                for (BuyerStoreHolder store : allStoreList)
                {
                    if (store.getAreaOid() != null && !store.getStoreOid().equals(obj.getStoreOid()))
                    {
                        allStores.add(store);
                    }
                }
                
                List<BuyerStoreUserTmpHolder> result = this.convertToBuyerStoreUserTmp(allStores, obj.getUserOid());
                
                BuyerStoreAreaUserTmpHolder param = new BuyerStoreAreaUserTmpHolder();
                param.setUserOid(obj.getUserOid());
                buyerStoreAreaUserService.delete(param);
                buyerStoreAreaUserTmpService.delete(param);
                
                if (result == null || result.isEmpty())
                {
                    continue;
                }
                for (BuyerStoreUserTmpHolder tmp : result)
                {
                    buyerStoreUserService.insert(tmp);
                    buyerStoreUserTmpService.insert(tmp);
                }
            }
        }
    }
    
    
    private List<BuyerStoreUserTmpHolder> convertToBuyerStoreUserTmp(List<BuyerStoreHolder> buyerStoreList, BigDecimal userOid)
    {
        List<BuyerStoreUserTmpHolder> result = new ArrayList<BuyerStoreUserTmpHolder>();
        if (buyerStoreList == null)
        {
            return result;
        }
        for (BuyerStoreHolder obj : buyerStoreList)
        {
            BuyerStoreUserTmpHolder holder = new BuyerStoreUserTmpHolder();
            holder.setStoreOid(obj.getStoreOid());
            holder.setUserOid(userOid);
            result.add(holder);
        }
        return result;
    }
    

    @Override
    public List<BuyerStoreHolder> selectBuyerStoresByBuyer(String buyerCode)
            throws Exception
    {
        if (buyerCode == null || buyerCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        BuyerStoreHolder param = new BuyerStoreHolder();
        param.setBuyerCode(buyerCode);

        return mapper.select(param);
    }


    @Override
    public List<BuyerStoreHolder> selectBuyerStoresByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }

        return mapper.selectBuyerStoresByUserOid(userOid);
    }


    @Override
    public List<BuyerStoreHolder> selectBuyerStoresFromTmpStoreUserByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }

        return mapper.selectBuyerStoresFromTmpStoreUserByUserOid(userOid);
    }
    
    


    @Override
    public List<BuyerStoreHolder> selectBuyerStoresByBuyerAndIsWareHouse(
        String buyerCode, boolean isWareHouse) throws Exception
    {
        if (buyerCode == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreHolder param = new BuyerStoreHolder();
        param.setBuyerCode(buyerCode);
        param.setIsWareHouse(isWareHouse);

        return mapper.select(param);
    }

    

    @Override
    public List<BuyerStoreHolder> selectBuyerStoresByBuyerCodeAndAreaOidAndIsWareHouse(
        String buyerCode, BigDecimal areaOid, boolean isWareHouse) throws Exception
    {
        if (areaOid == null || buyerCode == null || buyerCode.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }

        BuyerStoreHolder param = new BuyerStoreHolder();
        param.setBuyerCode(buyerCode);
        param.setAreaOid(areaOid);
        param.setIsWareHouse(isWareHouse);
        
        return mapper.select(param);
    }


    @Override
    public List<BuyerStoreHolder> selectBuyerStoresWithoutAreaByBuyerAndIsWareHouse(
        String buyerCode, boolean isWareHouse) throws Exception
    {
        if (buyerCode == null || buyerCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        BuyerStoreHolder buyerStore = new BuyerStoreHolder();
        buyerStore.setBuyerCode(buyerCode);
        buyerStore.setIsWareHouse(isWareHouse);
        return mapper.selectBuyerStoresWithoutAreaByBuyerAndIsWareHouse(buyerStore);
    }
    

    @Override
    public List<BuyerStoreHolder> selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(
        BigDecimal userOid, boolean isWareHouse) throws Exception
    {
        
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userOid", userOid);
        map.put("isWareHouse", isWareHouse);

        return mapper.selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(map);
    }


    @Override
    public List<BuyerStoreHolder> selectBuyerStoresByUserOidAndIsWareHouse(
        BigDecimal userOid, boolean isWareHouse) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userOid", userOid);
        map.put("isWareHouse", isWareHouse);
        
        return mapper.selectBuyerStoresByUserOidAndIsWareHouse(map);
    }


    @Override
    public List<BuyerStoreHolder> selectBuyerStoresByBuyerAndIsWareHouseAndAreaOid(
        String buyerCode, boolean isWareHouse, List<BigDecimal> areaOids)
        throws Exception
    {
        if (buyerCode == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerCode", buyerCode);
        map.put("isWareHouse", isWareHouse);
        map.put("areaOids", areaOids);
        if(areaOids == null || areaOids.isEmpty())
        {
            map.put("areaOids", null);
        }
        
        return mapper.selectBuyerStoresByBuyerAndIsWareHouseAndAreaOid(map);
    
    }


    @Override
    public byte[] exportExcel(String buyerCode) throws Exception
    {

        if (buyerCode == null || buyerCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        List<String> rlt = this.initSheetByBuyerCode(buyerCode, wwb, sheetIndex);
      
        wwb.write();
        wwb.close();
        
        if (rlt != null && !rlt.isEmpty())
        {
            return null;
        }
        
        return out.toByteArray();
    }
    
    private List<String> initSheetByBuyerCode(String buyerCode, WritableWorkbook wwb, int sheetIndex) throws Exception
    {
        List<BuyerStoreHolder> buyerStores = this.selectBuyerStoresByBuyer(buyerCode);
        WritableSheet sheet = wwb.createSheet("BUYER_STORES(" +buyerCode+ ")", sheetIndex);
        sheet.getSettings().setZoomFactor(100);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        List<String> errors = new ArrayList<String>();
        if (buyerStores == null || buyerStores.isEmpty())
        {
            errors.add("There has no any stores for this buyer[" +buyerCode+ "].");
        }
        else
        {
            initCellFormat();
            
            initHeaderTitle(sheet);
            
            initBuyerStoresCell(sheet, buyerStores);
        }
        return errors;
    }
    
    
    private void initCellFormat()throws Exception
    {
        format0 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format0.setAlignment(Alignment.CENTRE);
        format0.setBorder(Border.ALL, BorderLineStyle.THIN);
        format0.setBackground(Colour.ICE_BLUE);
        format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setAlignment(Alignment.CENTRE);
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setAlignment(Alignment.LEFT);
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setIndentation(1);
    }
    
    
    private void initHeaderTitle(WritableSheet sheet)throws Exception
    {
        sheet.setColumnView(0, 15);
        sheet.addCell(new Label(0, 0, "STORE CODE", format0));
        sheet.setColumnView(1, 20);
        sheet.addCell(new Label(1, 0, "STORE NAME", format0));
        sheet.setColumnView(2, 25);
        sheet.addCell(new Label(2, 0, "TELEPHONE", format0));
        sheet.setColumnView(3, 25);
        sheet.addCell(new Label(3, 0, "ADDRESS1", format0));
        sheet.setColumnView(4, 25);
        sheet.addCell(new Label(4, 0, "ADDRESS2", format0));
        sheet.setColumnView(5, 25);
        sheet.addCell(new Label(5, 0, "ADDRESS3", format0));
        sheet.setColumnView(6, 25);
        sheet.addCell(new Label(6, 0, "ADDRESS4", format0));
        sheet.setColumnView(7, 25);
        sheet.addCell(new Label(7, 0, "ADDRESS5", format0));
        sheet.setColumnView(8, 15);
        sheet.addCell(new Label(8, 0, "CITY", format0));
        sheet.setColumnView(9, 15);
        sheet.addCell(new Label(9, 0, "STATE", format0));
        sheet.setColumnView(10, 15);
        sheet.addCell(new Label(10, 0, "COUNTRY CODE", format0));
        sheet.setColumnView(11, 15);
        sheet.addCell(new Label(11, 0, "POSTAL CODE", format0));
        sheet.setColumnView(12, 15);
        sheet.addCell(new Label(12, 0, "ISWAREHOUSE", format0));
    }
    
    
    private void initBuyerStoresCell(WritableSheet sheet, List<BuyerStoreHolder> buyerStores)throws Exception
    {
        BuyerStoreHolder buyerStore = null;
        for (int i = 0 ; i < buyerStores.size(); i++)
        {
            buyerStore = buyerStores.get(i);
            sheet.addCell(new Label(0, (i + 1), buyerStore.getStoreCode(), format1));
            sheet.addCell(new Label(1, (i + 1), buyerStore.getStoreName(), format2));
            sheet.addCell(new Label(2, (i + 1), buyerStore.getContactTel(), format2));
            sheet.addCell(new Label(3, (i + 1), buyerStore.getStoreAddr1(), format2));
            sheet.addCell(new Label(4, (i + 1), buyerStore.getStoreAddr2(), format2));
            sheet.addCell(new Label(5, (i + 1), buyerStore.getStoreAddr3(), format2));
            sheet.addCell(new Label(6, (i + 1), buyerStore.getStoreAddr4(), format2));
            sheet.addCell(new Label(7, (i + 1), buyerStore.getStoreAddr5(), format2));
            sheet.addCell(new Label(8, (i + 1), buyerStore.getStoreCity(), format2));
            sheet.addCell(new Label(9, (i + 1), buyerStore.getStoreState(), format2));
            sheet.addCell(new Label(10, (i + 1), buyerStore.getStoreCtryCode(), format1));
            sheet.addCell(new Label(11, (i + 1), buyerStore.getStorePostalCode(), format1));
            sheet.addCell(new Label(12, (i + 1), buyerStore.getIsWareHouse() ? "Y" : "N", format1));
        }
    }

}
