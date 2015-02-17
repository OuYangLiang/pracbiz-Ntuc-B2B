//*****************************************************************************
//
// File Name       :  GiDocFileConvert.java
// Date Created    :  Nov 12, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 12, 2013 3:40:46 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.convert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.outbound.SalesDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SalesHolder;
import com.pracbiz.b2bportal.core.holder.SalesLocationHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author YinChi
 */
public class SalesDataDocFileConvert implements CoreCommonConstants
{
   
    private static String SPACE = " ";
    
    public List<File> createFile(File filePath, Map<String, List<String[]>> map, BuyerHolder buyer, 
        String sourceContent, DocFileHandler<SalesDocMsg, SalesHolder> canonicalSalesDocFileHandler) throws Exception
    {
        List<File> fileLst = new ArrayList<File>();
        
        String fileName = null;
        
        for (Map.Entry<String, List<String[]>> entry : map.entrySet())
        {
            String salesNo = entry.getKey().split("@@")[0].trim();
            fileName = filePath
                + File.separator
                + "DSD_"
                + buyer.getBuyerCode()
                + "_"
                + entry.getKey().split("-")[0].trim()
                + "_"
                + salesNo + ".txt";
            
            File file = new File(fileName);

            if (file.exists())
            {
                List<File> files = new ArrayList<File>();
                String newSourceFile = "source"
                    + DOC_FILENAME_DELIMITOR
                    + DateUtil.getInstance().convertDateToString(new Date(),
                      DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC) 
                    + ".txt";
                
                while(FileUtil.getInstance().isFileExist(filePath.getPath(), newSourceFile))
                {
                    newSourceFile = "source"
                        + DOC_FILENAME_DELIMITOR
                        + DateUtil.getInstance().convertDateToString(new Date(),
                          DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC) 
                        + ".txt";
                }
                File newSrcFile = new File(newSourceFile);
                byte[] bytes = sourceContent.getBytes(CommonConstants.ENCODING_UTF8);
                FileUtil.getInstance().writeByteToDisk(bytes, newSrcFile.getPath());
                
                files.add(file);
                files.add(newSrcFile);
                GZIPHelper.getInstance().doStandardZip(files, "DSD", buyer.getBuyerCode(), filePath.getPath());
            }
            
            //****** write target file *********
            
            SalesHolder salesHolder = this.initSalesHolder(entry.getValue(), buyer, salesNo);
            
            canonicalSalesDocFileHandler.createFile(salesHolder, file, DocFileHandler.CANONICAL);
            
            if (!fileLst.contains(file))
            {
                fileLst.add(file);
            }
        }
        
        return fileLst;
    }

    public Map<String, List<String[]>> readFile(File file) throws Exception
    {
        InputStream inputStream = null;
        Map<String, List<String[]>> dsdMap = null;
        try
        {
            Map<String , byte[]> fileMap = new HashMap<String, byte[]>();
            if (file.getName().endsWith(".zip"))
            {
                fileMap = GZIPHelper.getInstance().readFileFromZip(file);
            }
            else
            {
                fileMap.put(file.getName(), FileUtils.readFileToByteArray(file));
            }
            
            InputStreamReader isr = null;
            CSVReader reader = null;
            List<String[]> contents = null;
            
            dsdMap = new HashMap<String, List<String[]>>();
            for (Map.Entry<String, byte[]> entry : fileMap.entrySet())
            {
                String currentLogicTime = DateUtil.getInstance().getCurrentLogicTimeStampWithoutMin();
                try
                {
                    inputStream = new ByteArrayInputStream(entry.getValue());
                    isr = new InputStreamReader(inputStream, CommonConstants.ENCODING_UTF8);
                    reader = new CSVReader(isr, ',','\"');
                    contents = reader.readAll();
                }
                finally
                {
                    if (reader != null)
                    {
                        reader.close();
                        reader = null;
                    }
                    if (isr != null)
                    {
                        isr.close();
                        isr = null;
                    }
                }
                for (int i = 1; i < contents.size(); i++)
                {
                    String[] content = contents.get(i);
                    if (content.length != 15)
                    {
                        continue;
                    }
                    
                    String supplierCode = content[14].trim();
                    String salesNo = supplierCode + "-" + currentLogicTime;
                    String key = salesNo + "@@" + entry.getKey() + "@@" + DateUtil.getInstance().convertDateToString(DateUtil.getInstance().convertStringToDate(content[4].trim(),DateUtil.DATE_FORMAT_MMDDYYYY));
                    if (dsdMap.containsKey(key))
                    {
                        dsdMap.get(key).add(content);
                    }
                    else
                    {
                        List<String[]> lst = new ArrayList<String[]>();
                        lst.add(content);
                        dsdMap.put(key, lst);
                    }
                }
            }
        }
        finally
        {
           if (inputStream != null)
           {
               inputStream.close();
           }
        }
        
        return dsdMap;
    }
    
    private SalesHolder initSalesHolder(List<String[]> lines, BuyerHolder buyer, String salesNo)
    {
        FileParserUtil util = FileParserUtil.getInstance();
        SalesHolder data = new SalesHolder();
        
        SalesHeaderHolder header = new SalesHeaderHolder();
        List<SalesDetailHolder> details = new ArrayList<SalesDetailHolder>();
        List<SalesLocationHolder> locs = new ArrayList<SalesLocationHolder>();
        List<SalesDateLocationDetailHolder> locDetails = new ArrayList<SalesDateLocationDetailHolder>();
        
        Map<String, List<String[]>> detailMap = new HashMap<String, List<String[]>>();
        String detailKey = null;
        
        Map<String, Integer> locSeqMap = new HashMap<String, Integer>();
        int locSeq = 1;
        
        Map<Date, Integer> dateSeqMap = new HashMap<Date, Integer>();
        int dateSeq = 1;
        
        BigDecimal totalSalesQty = BigDecimal.ZERO;
        BigDecimal totalItemSalesAmt = BigDecimal.ZERO;
        BigDecimal totalSalesDisAmt = BigDecimal.ZERO;
        BigDecimal totalItemNetSalesAmt = BigDecimal.ZERO;
        BigDecimal totalVatAmt = BigDecimal.ZERO;
        
        for (String[] contents : lines)
        {
            detailKey = contents[7].trim() + "@@"+contents[6].trim();
            if (detailMap.containsKey(detailKey))
            {
                detailMap.get(detailKey).add(contents);
            }
            else
            {
                List<String[]> cons = new ArrayList<String[]>();
                cons.add(contents);
                detailMap.put(detailKey, cons);
            }
            
            if (!locSeqMap.containsKey(contents[1].trim()))
            {
                locSeqMap.put(contents[1].trim(), locSeq);
                locSeq++;
            }
            
            if (!dateSeqMap.containsKey(DateUtil.getInstance().convertStringToDate(contents[4].trim(), DateUtil.DATE_FORMAT_MMDDYYYY)))
            {
                dateSeqMap.put(DateUtil.getInstance().convertStringToDate(contents[4].trim(), DateUtil.DATE_FORMAT_MMDDYYYY), dateSeq);
                dateSeq++;
            }
            totalSalesQty = totalSalesQty.add(util.decimalValueOf(contents[9]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[9]));
            totalItemSalesAmt = totalItemSalesAmt.add(util.decimalValueOf(contents[10]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[10]));
            totalSalesDisAmt = totalSalesDisAmt.add(util.decimalValueOf(contents[11]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[11]));
            totalItemNetSalesAmt = totalItemNetSalesAmt.add(util.decimalValueOf(contents[12]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[12]));
            totalVatAmt = totalVatAmt.add(util.decimalValueOf(contents[13]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[13]));
            
        }
        
        int detailLineSeq = 1;
        //init sales details
        for (Map.Entry<String, List<String[]>> entry : detailMap.entrySet())
        {
            BigDecimal detailSalesQty = BigDecimal.ZERO;
            BigDecimal detailItemSalesAmt = BigDecimal.ZERO;
            BigDecimal detailSalesDisAmt = BigDecimal.ZERO;
            BigDecimal detailItemNetSalesAmt = BigDecimal.ZERO;
            BigDecimal detailVatAmt = BigDecimal.ZERO;
            
            SalesDetailHolder detail = new SalesDetailHolder();
            
            for (String[] contents : entry.getValue())
            {
                detailSalesQty = detailSalesQty.add(util.decimalValueOf(contents[9]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[9]));
                detailItemSalesAmt = detailItemSalesAmt.add(util.decimalValueOf(contents[10]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[10]));
                detailSalesDisAmt = detailSalesDisAmt.add(util.decimalValueOf(contents[11]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[11]));
                detailItemNetSalesAmt = detailItemNetSalesAmt.add(util.decimalValueOf(contents[12]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[12]));
                detailVatAmt = detailVatAmt.add(util.decimalValueOf(contents[13]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[13]));
            }
            detail.setLineSeqNo(detailLineSeq);
            detail.setPosId(entry.getValue().get(0)[2].trim());
            detail.setReceiptNo(entry.getValue().get(0)[3].trim());
            detail.setBusinessDate(util.dateValueOf(entry.getValue().get(0)[4].trim(), DateUtil.DATE_FORMAT_MMDDYYYY));
            detail.setReceiptDate(util.dateValueOf(entry.getValue().get(0)[4].trim() + SPACE +entry.getValue().get(0)[5].trim(), DateUtil.DATE_FORMAT_MMDDYYYYHHMMSS));
            detail.setBarcode(entry.getValue().get(0)[6].trim());
            detail.setBuyerItemCode(entry.getValue().get(0)[7].trim());
            detail.setItemDesc(entry.getValue().get(0)[8].trim());
            detail.setPackingFactor(BigDecimal.ONE);
            detail.setSalesUom("UNIT");
            detail.setSalesBaseUnit("U");
            detail.setSalesQty(detailSalesQty);
            detail.setItemSalesAmount(detailItemSalesAmt);
            if (detailSalesQty.compareTo(BigDecimal.ZERO) == 0)
            {
                detail.setSalesPrice(BigDecimal.ZERO);
            }
            else
            {
                detail.setSalesPrice(BigDecimalUtil.getInstance().format(BigDecimal.valueOf((detailItemSalesAmt.doubleValue())/(detailSalesQty.doubleValue())), 4));
            }
            detail.setSalesDiscountAmount(detailSalesDisAmt);
            detail.setItemNetSalesAmount(detailItemNetSalesAmt);
            detail.setVatAmount(detailVatAmt);
            
            detailLineSeq++;
            details.add(detail);
        }
        
        Map<String, SalesDateLocationDetailHolder> groupLocDetail = new HashMap<String, SalesDateLocationDetailHolder>();
        
        for (String[] contents : lines)
        {
            //init loc and loc detail
            SalesLocationHolder loc = new SalesLocationHolder();
            SalesDateLocationDetailHolder locDetail = new SalesDateLocationDetailHolder();
            
            for (SalesDetailHolder detail : details)
            {
                if (detail.getBuyerItemCode().equals(contents[7].trim()) && contents[6].trim().equals(detail.getBarcode()))
                {
                    loc.setDetailLineSeqNo(detail.getLineSeqNo());
                    int dateLineSeq = dateSeqMap.get(DateUtil.getInstance().convertStringToDate(contents[4], DateUtil.DATE_FORMAT_MMDDYYYY));
                    locDetail.setDateLineSeqNo(dateLineSeq);
                }
            }
            loc.setLineSeqNo(locSeqMap.get(contents[1].trim()));
            loc.setLocationCode(contents[1].trim());
            locDetail.setDetailLineSeqNo(loc.getDetailLineSeqNo());
            locDetail.setLocationLineSeqNo(loc.getLineSeqNo());
            
            String key = locDetail.getDetailLineSeqNo() + "@@" + locDetail.getLocationLineSeqNo() + "@@" + locDetail.getDateLineSeqNo();
            if (groupLocDetail.containsKey(key))
            {
                SalesDateLocationDetailHolder locDetailHolder = groupLocDetail.get(key);
                locDetailHolder.setSalesQty(locDetailHolder.getSalesQty().add(util.decimalValueOf(contents[9]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[9])));
                locDetailHolder.setSalesAmount(locDetailHolder.getSalesAmount().add(util.decimalValueOf(contents[9]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[10])));
                locDetailHolder.setSalesDiscountAmount(locDetailHolder.getSalesDiscountAmount().add(util.decimalValueOf(contents[9]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[11])));
                locDetailHolder.setVatAmount(locDetailHolder.getVatAmount().add(util.decimalValueOf(contents[9]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[13])));
                locDetailHolder.setSalesNetAmount(locDetailHolder.getSalesNetAmount().add(util.decimalValueOf(contents[9]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[12])));
            }
            else
            {
                SalesDateLocationDetailHolder locDetailHolder = new SalesDateLocationDetailHolder();
                locDetailHolder.setDetailLineSeqNo(locDetail.getDetailLineSeqNo());
                locDetailHolder.setDateLineSeqNo(locDetail.getDateLineSeqNo());
                locDetailHolder.setLocationLineSeqNo(locDetail.getLocationLineSeqNo());
                locDetailHolder.setSalesQty(util.decimalValueOf(contents[9]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[9]));
                locDetailHolder.setSalesAmount(util.decimalValueOf(contents[10]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[10]));
                locDetailHolder.setSalesDiscountAmount(util.decimalValueOf(contents[11]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[11]));
                locDetailHolder.setVatAmount(util.decimalValueOf(contents[13]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[13]));
                locDetailHolder.setSalesNetAmount(util.decimalValueOf(contents[12]) == null ? BigDecimal.ZERO : util.decimalValueOf(contents[12]));
            
                groupLocDetail.put(key, locDetailHolder);
                locs.add(loc);
            }
        }
        
        for (Map.Entry<String, SalesDateLocationDetailHolder> entry : groupLocDetail.entrySet())
        {
            // delete the data which sales qty is 0;
            if (entry.getValue().getSalesQty().compareTo(BigDecimal.ZERO) == 0)
            {
                continue;
            }
            locDetails.add(entry.getValue());
        }
        
        
        //init sales header
        header.setSalesNo(salesNo);
        header.setBuyerCode(buyer.getBuyerCode());
        header.setSupplierCode(salesNo.split("-")[0]);
        header.setDocAction("A");
        header.setSalesDataType("SOR");
        header.setActionDate(new Date());
        header.setSalesDate(details.get(0).getBusinessDate());
        if (locSeqMap.size() == 1)
        {
            for(Map.Entry<String, Integer> map : locSeqMap.entrySet())
            {
                header.setStoreCode(map.getKey());
            }
        }
        header.setTotalQty(totalSalesQty);
        header.setTotalGrossSalesAmount(totalItemSalesAmt);
        header.setTotalDiscountAmount(totalSalesDisAmt);
        header.setTotalNetSalesAmount(totalItemNetSalesAmt);
        header.setTotalVatAmount(totalVatAmt);
        header.setPeriodStartDate(DateUtil.getInstance().getFirstTimeOfDay(header.getSalesDate()));
        header.setPeriodEndDate(DateUtil.getInstance().getLastTimeOfDay(header.getSalesDate()));
        
        data.setSalesHeader(header);
        data.setDetails(details);
        data.setLocations(locs);
        data.setSalesDateLocationDetail(locDetails);
        
        return data;
    }
}
