package com.pracbiz.b2bportal.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;
import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.PoBatchSummary;
import com.pracbiz.b2bportal.core.eai.message.PoBatchSummaryLine;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.extension.BusinessRuleExHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerRuleService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.NotificationService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class NotificationServiceImpl implements NotificationService
{
    @Autowired BuyerMsgSettingService buyerMsgSettingService;
    @Autowired SupplierMsgSettingService supplierMsgSettingService;
    @Autowired SupplierService supplierService;
    @Autowired EmailEngine emailEngine;
    @Autowired StandardEmailSender standardEmailSender;
    @Autowired BuyerStoreService buyerStoreService;
    @Autowired BuyerRuleService buyerRuleService;
    @Autowired BusinessRuleService businessRuleService;
    @Autowired BuyerService buyerService;
    @Autowired MailBoxUtil mboxUtil;
    @Autowired ControlParameterService controlParameterService;
    @Autowired MsgTransactionsService msgTransactionsService;
    private static final String DELIMITOR = "_";
    private static final String  BUSINESS_RULE_FUNC_GROUP = "PoConvertInv";
    private static final String  BUSINESS_RULE_FUNC_ID = "SorPO";
    private static final String  BUSINESS_RULE_RULE_ID = "EmailToStore";
    
    
    @Override
    public void sendOutboundNotificationEmail(BatchMsg batch) throws Exception
    {

        this.outboundAlertBuyer(batch);
        this.outboundAlertSuppliers(batch);
    }
    
    
    @Override
    public void sendInboundNotificationEmail(BatchMsg batch) throws Exception
    {
        
        this.inboundAlertBuyers(batch);
        this.inboundAlertSupplier(batch);
    }


    private Map<String, List<DocMsg>> groupBySupplierAndType(List<DocMsg> docs)
    {
        Map<String, List<DocMsg>> rlt = new HashMap<String, List<DocMsg>>();

        for (DocMsg doc : docs)
        {
            if (!(doc.isValid() && doc.isActive()))
            {
                continue;
            }
            if (DeploymentMode.REMOTE.equals(doc.getSupplier()
                    .getDeploymentMode()))
            {
                continue;
            }

            String key = doc.getReceiverOid() + DELIMITOR + doc.getMsgType();

            if (rlt.containsKey(key))
            {
                rlt.get(key).add(doc);
            }
            else
            {
                List<DocMsg> list = new ArrayList<DocMsg>();
                list.add(doc);
                rlt.put(key, list);
            }
        }

        return rlt;
    }


    private void outboundAlertSuppliers(BatchMsg batch) throws Exception
    {
        Map<String, List<DocMsg>> group = this.groupBySupplierAndType(batch
                .getDocs());
        for (Map.Entry<String, List<DocMsg>> entry : group.entrySet())
        {
            int delimitorIndex = entry.getKey().indexOf(DELIMITOR);

            BigDecimal supplierOid = new BigDecimal(entry.getKey().substring(0,
                    delimitorIndex));
            SupplierHolder supplier = supplierService
                    .selectSupplierByKey(supplierOid);
            String type = entry.getKey().substring(delimitorIndex + 1);

            SupplierMsgSettingHolder setting = supplierMsgSettingService
                    .selectByKey(supplierOid, type);
            if (null == setting || null == setting.getRcpsAddrs()
                    || setting.getRcpsAddrs().trim().isEmpty())
            {
                continue;
            }
            ControlParameterHolder cp = controlParameterService.selectCacheControlParameterBySectIdAndParamId(
                CoreCommonConstants.SECT_ID_CTRL, CoreCommonConstants.PARAM_ID_HELPDESK_NO);
            
            String[] emailTo = setting.retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
            String subject = "Arrival of "+ batch.getBatchType() +"(s)-" + batch.getSenderName() + "(" + batch.getSenderCode() + ")";
            
            if (batch.calculateAmendedDocs() > 0)
            {
                subject = "Amendment of "+ batch.getBatchType() +"(s)-" + batch.getSenderName() + "(" + batch.getSenderCode() + ")";
            }
            
            Map<String, Object> map = initOutboundAlertSupplierMap(entry.getValue());
            map.put("SUPPLIER_NAME", supplier.getSupplierName());
            map.put("BUYER_GIVEN_SUPPLIER_CODE", supplier.getSupplierCode());
            map.put("BUYER_NAME", batch.getSenderName());
            map.put("BUYER_CODE", batch.getSenderCode());
            map.put("DOC_TYPE", batch.getBatchType());
            map.put("CONTACT_NO", cp.getStringValue());
            emailEngine.sendHtmlEmail(emailTo, subject, "ALERT_OUTBOUND_SUPPLIER.vm", map);
        }
    }


    private void outboundAlertBuyer(BatchMsg batch) throws Exception
    {
        if (DeploymentMode.REMOTE.equals(batch.getBuyer().getDeploymentMode()))
        {
            return;
        }

        BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(
                batch.getSenderOid(), batch.getBatchType().name());

        if (null == setting || null == setting.getRcpsAddrs()
                || (setting.getRcpsAddrs().trim().isEmpty() && setting.getErrorRcpsAddrs().trim().isEmpty()))
        {
            return;
        }
        String[] emailTo = setting.retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
        String[] errorEmailTo = setting.retrieveErrorRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
        String subject = "Batch Run Status Report - "
                + batch.getBatchNo();
        Map<String, Object> map = initOutboundAlertBuyerMap(batch.getDocs());
        List<Object> errorList = (List<Object>) map.get("ERROR_LIST");
        List<Object> inactiveList = (List<Object>) map.get("INACTIVE_LIST");
        
        boolean errorFlag = false;
        if (!errorList.isEmpty() || !inactiveList.isEmpty())
        {
            errorFlag = true;
            subject = subject + "(Unsuccess Count: " + (errorList.size() + inactiveList.size()) + ")";
        }
        map.put("BUYER_NAME", batch.getSenderName());
        map.put("BUYER_CODE", batch.getSenderCode());
        map.put("BUTCH_FILE_NAME", batch.getBatchFileName());
        map.put("SOURCE_FILE_NAMES", batch.getSourceFileNames());
        map.put("TRANSFORMED_BY", batch.getTransformedBy());
        
        if (BatchType.PO.equals(batch.getBatchType()))
        {
            if (null != batch.getPoBatchSummary())
            {
                this.initPoBatchSummaryInfo(batch, map);
            }
            
            String attachedFileName = "PoSummaryReport - " + batch.getBatchNo() + ".xls";
            emailEngine.sendEmailWithAttachedDocuments(emailTo, subject, "ALERT_OUTBOUND_BUYER.vm", map, new String[] {attachedFileName}, new byte[][] {getPoSummaryReport(batch)});
            
            if (errorFlag && errorEmailTo != null && errorEmailTo.length != 0)
            {
                emailEngine.sendEmailWithAttachedDocuments(errorEmailTo, subject, "ALERT_OUTBOUND_BUYER.vm", map, new String[] {attachedFileName}, new byte[][] {getPoSummaryReport(batch)});
            }
            
            BusinessRuleHolder buisnessRule = businessRuleService
                .selectRulesByKey(batch
                    .getSenderOid(), BUSINESS_RULE_FUNC_GROUP,
                    BUSINESS_RULE_FUNC_ID, BUSINESS_RULE_RULE_ID);
            
            if (buisnessRule != null)
            {
                this.alertBuyerStore(batch, subject);
            }
        }
        else
        {
            emailEngine.sendHtmlEmail( emailTo, subject,"ALERT_OUTBOUND_BUYER.vm", map);
            
            if (errorFlag && errorEmailTo != null && errorEmailTo.length != 0)
            {
                emailEngine.sendHtmlEmail( errorEmailTo, subject,"ALERT_OUTBOUND_BUYER.vm", map);
            }
        }
        
        Date alertDate = new Date();
        if (batch.getDocs() != null && !batch.getDocs().isEmpty())
        {
            for (DocMsg docMsg : batch.getDocs())
            {
                if (!docMsg.isActive() || !docMsg.isValid())
                {
                    continue;
                }
                MsgTransactionsHolder msg = msgTransactionsService.selectByKey(docMsg.getDocOid());
                msg.setAlertDate(alertDate);
                msgTransactionsService.updateByPrimaryKey(null, msg);
            }
        }
    }
    
    
    private void initPoBatchSummaryInfo(BatchMsg batch, Map<String, Object> map)
    {
        PoBatchSummary batchSummary = batch.getPoBatchSummary();
        
        List<String> unFindedPos = new ArrayList<String>();
        List<String> extraPos = new ArrayList<String>();
        List<String> unMatchedPos = new ArrayList<String>();
        
        for (DocMsg doc : batch.getDocs())
        {
            if (!(doc instanceof PoDocMsg))
            {
                continue;
            }
            
            PoDocMsg poDoc = (PoDocMsg) doc;
            
            PoBatchSummaryLine item = batchSummary.findItemByPoNo(poDoc.getRefNo());
            
            if (null == item)
            {
                extraPos.add(poDoc.getRefNo());
                continue;
            }
            
            if (item.getNumOfItems().intValue() != poDoc.getData().getDetails().size())
            {
                unMatchedPos.add(item.getPoNo()
                    + " --- Number of items does not match, Report file: ["
                    + item.getNumOfItems().intValue() + "], actually: ["
                    + poDoc.getData().getDetails().size() + "].");
                
                batchSummary.removeItemByPoNo(poDoc.getRefNo());
                continue;
            }
            
            if (item.getTotalItemQty().compareTo(poDoc.getData().calculateTotalQty()) != 0)    
            {
                unMatchedPos.add(item.getPoNo()
                    + " --- Total item quantity does not match, Report file: ["
                    + item.getTotalItemQty() + "], actually: ["
                    + poDoc.getData().calculateTotalQty() + "].");
                
                batchSummary.removeItemByPoNo(poDoc.getRefNo());
                continue;
            }
            
            if (item.getTotalAmt().compareTo(poDoc.getData().getPoHeader().getTotalCost()) != 0)
            {
                unMatchedPos.add(item.getPoNo()
                    + " --- Total amount does not match, Report file: ["
                    + item.getTotalAmt() + "], actually: ["
                    + poDoc.getData().getPoHeader().getTotalCost() + "].");
                
                batchSummary.removeItemByPoNo(poDoc.getRefNo());
                continue;
            }
            
            batchSummary.removeItemByPoNo(poDoc.getRefNo());
        }
        
        for (PoBatchSummaryLine item: batchSummary.getItems())
        {
            unFindedPos.add(item.getPoNo());
        }
        
        if (!unFindedPos.isEmpty())
        {
            map.put("UNFINDED_POS", unFindedPos);
        }
        if (!extraPos.isEmpty())
        {
            map.put("EXTRA_POS", extraPos);
        }
        if (!unMatchedPos.isEmpty())
        {
            map.put("UNMATCHED_POS", unMatchedPos);
        }
        
        
    }


    private Map<String, Object> initOutboundAlertBuyerMap(List<DocMsg> list)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DocMsg> succList = new ArrayList<DocMsg>();
        List<DocMsg> errorList = new ArrayList<DocMsg>();
        List<DocMsg> inactiveList = new ArrayList<DocMsg>();
        if (list != null)
        {
            Iterator<DocMsg> it = list.iterator();
            while (it.hasNext())
            {
                DocMsg obj = (DocMsg) it.next();
                if (obj.isValid())
                {
                    if (obj.isActive())
                    {
                        succList.add(obj);
                    }
                    else
                    {
                        inactiveList.add(obj);
                    }
                }
                else
                {
                    errorList.add(obj);
                }
            }
        }
        map.put("TOTAL_LIST", list);
        map.put("SUCC_LIST", succList);
        map.put("ERROR_LIST", errorList);
        map.put("INACTIVE_LIST", inactiveList);
        return map;
    }


    private Map<String, Object> initOutboundAlertSupplierMap(List<DocMsg> list)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DocMsg> succList = new ArrayList<DocMsg>();
        if (list != null)
        {
            Iterator<DocMsg> it = list.iterator();
            while (it.hasNext())
            {
                DocMsg obj = (DocMsg) it.next();
                if (obj.isValid() && obj.isActive())
                {
                    succList.add(obj);
                }
            }
        }
        map.put("SUCC_LIST", succList);
        return map;
    }
    
    
    private byte[] getPoSummaryReport(BatchMsg batch) throws Exception
    {
        List<DocMsg> docMsgs = batch.getDocs();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        WritableSheet ws = wwb.createSheet("PO Summary Report-" + batch.getBatchNo(), 0);
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.CENTRE);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        format3.setAlignment(Alignment.RIGHT);
        WritableCellFormat format4 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(Alignment.CENTRE);
        //summary description
        ws.addCell(new Label(0, 0, "Message Type:", format1));
        ws.mergeCells(0, 0, 1, 0);
        ws.addCell(new Label(0, 1, "Batch No:", format1));
        ws.mergeCells(0, 1, 1, 1);
        ws.addCell(new Label(0, 2, "Batch file name:", format1));
        ws.mergeCells(0, 2, 1, 2);
        ws.addCell(new Label(0, 3, "Uploaded date:", format1));
        ws.mergeCells(0, 3, 1, 3);
        ws.addCell(new Label(2, 0, batch.getBatchType().name(), format2));
        ws.mergeCells(2, 0, 3, 0);
        ws.addCell(new Label(2, 1, batch.getBatchNo(), format2));
        ws.mergeCells(2, 1, 3, 1);
        ws.addCell(new Label(2, 2, batch.getBatchFileName(), format2));
        ws.mergeCells(2, 2, 3, 2);
        ws.addCell(new Label(2, 3, DateUtil.getInstance().convertDateToString(batch.getInDate(), DateUtil.DEFAULT_DATE_FORMAT), format2));
        ws.mergeCells(2, 3, 3, 3);
        //header description
        ws.addCell(new Label(0, 6, "S/No", format1));
        ws.addCell(new Label(1, 6, "Message File Name", format1));
        ws.addCell(new Label(2, 6, "reference No", format1));
        ws.addCell(new Label(3, 6, "Doc Date", format1));
        ws.addCell(new Label(4, 6, "Supplier Code", format1));
        ws.addCell(new Label(5, 6, "Supplier Name", format1));
        ws.addCell(new Label(6, 6, "Total No of Items", format1));
        ws.addCell(new Label(7, 6, "Total Net Amount", format1));
        //items
        int row = 7;
        int totalItems = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (docMsgs != null)
        {
            for (int i = 0; i < docMsgs.size(); i ++)
            {
                DocMsg docMsg = docMsgs.get(i);
                if (!(docMsg.isActive() && docMsg.isValid()))
                {
                    continue;
                }
                PoHolder poHolder = ((PoDocMsg) docMsg).getData();
                SupplierHolder supplier = supplierService.selectSupplierByKey(poHolder.getPoHeader().getSupplierOid());
                ws.addCell(new Label(0, row, String.valueOf(i + 1), format4));
                ws.addCell(new Label(1, row, docMsg.getOriginalFilename(), format2));
                ws.addCell(new Label(2, row, docMsg.getRefNo(), format4));
                ws.addCell(new Label(3, row, DateUtil.getInstance().convertDateToString(poHolder.getPoHeader().getPoDate(), DateUtil.DEFAULT_DATE_FORMAT), format4));
                ws.addCell(new Label(4, row, supplier == null ? "" : supplier.getSupplierCode(), format4));
                ws.addCell(new Label(5, row, supplier == null ? "" : supplier.getSupplierName(), format4));
                ws.addCell(new Label(6, row, String.valueOf(poHolder.getDetails().size()), format4));
                ws.addCell(new Label(7, row, String.valueOf(poHolder.getPoHeader().getNetCost()), format3));
                row ++;
                totalItems += poHolder.getDetails().size();
                totalAmount = totalAmount.add(poHolder.getPoHeader().getNetCost());
            }
        }
        //footer description
        ws.addCell(new Label(5, row, "Total:", format1));
        ws.addCell(new Label(6, row, String.valueOf(totalItems), format4));
        ws.addCell(new Label(7, row, String.valueOf(totalAmount), format3));
        
        ws.setColumnView(0, 10);
        ws.setColumnView(1, 35);
        ws.setColumnView(2, 20);
        ws.setColumnView(3, 20);
        ws.setColumnView(4, 20);
        ws.setColumnView(5, 20);
        ws.setColumnView(6, 20);
        ws.setColumnView(7, 20);
        wwb.write();
        wwb.close();
        
        return out.toByteArray();
    }
    
    
    private void alertBuyerStore(BatchMsg batch,String subject)throws Exception
    {
        List<BuyerStoreHolder>  stores = buyerStoreService.selectBuyerStoresByBuyer(batch.getDocs().get(0).getSenderCode());
        
        Map<String, List<String>> storeMap =  getStoreEmails(stores);
        
        Map<String, List<DocMsg>> emailMap = new HashMap<String, List<DocMsg>>();
        
        for (DocMsg doc : batch.getDocs())
        {   
            if (!(doc instanceof PoDocMsg))
            {   
                continue;
            }
           
            PoDocMsg poDoc =(PoDocMsg) doc;
            
            if (!poDoc.isActive() || !poDoc.isValid())
            {
                continue;
            }
            
            if (poDoc.getData().getPoHeader().getPoType().equals(PoType.SOR) 
                && poDoc.getData().getPoHeader().getPoSubType().equals("1"))
            {
                
                for (PoLocationHolder poLocation : poDoc.getData().getLocations())
                {
                    //this buyer has this store access.
                    if (storeMap.containsKey(poLocation.getLocationCode()))
                    {
                        if (emailMap.containsKey(poLocation.getLocationCode()))
                        {
                            emailMap.get(poLocation.getLocationCode()).add(doc);
                        }
                        else
                        {
                            List<DocMsg> docs = new ArrayList<DocMsg>();
                            docs.add(doc);
                            emailMap.put(poLocation.getLocationCode(), docs);
                        }
                    }
                }
            }
        }
        
        if (!emailMap.isEmpty())
        {
            for (Map.Entry<String,List<DocMsg>> entry : emailMap.entrySet())
            {
                String [] emailTo = (String[])storeMap.get(entry.getKey()).toArray(new String[]{});
                
                Map<String, Object> map = initAlertBuyerStoreMap(entry.getValue());
                map.put("STORE_CODE", entry.getKey());
                
                List<String> succList = (List<String>)map.get("SUCC_LIST");
                
                if(!succList.isEmpty())
                {
                    emailEngine.sendHtmlEmail( emailTo, subject,"ALERT_BUYER_STORE.vm", map);
                }
            }
        }
    }
    
    
    private Map<String, List<String>> getStoreEmails(List<BuyerStoreHolder>  stores)
    {
        Map<String, List<String>> storeMap =  new HashMap<String, List<String>>();
        for (BuyerStoreHolder store : stores )
        {
            //email not provider to skip
            if(null == store.getContactEmail() || store.getContactEmail().isEmpty())
            {
                continue;
            }
           
            if (storeMap.containsKey(store.getStoreCode()))
            {
                //this email not exist in map
                if(storeMap.get(store.getStoreCode()).indexOf(
                    store.getContactEmail().trim().toLowerCase()) == -1)
                {
                    storeMap.get(store.getStoreCode()).add(
                        store.getContactEmail().trim().toLowerCase());
                }
            }
            else
            {
                List<String> emailAddrs = new ArrayList<String>();
                emailAddrs.add(store.getContactEmail().trim().toLowerCase());
               
                storeMap.put(store.getStoreCode(), emailAddrs);
            }
        }
        return storeMap;
    }
    
    
    private Map<String, Object> initAlertBuyerStoreMap(List<DocMsg> list)throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> succList = new ArrayList<String>();
        if (list != null)
        {
            Iterator<DocMsg> it = list.iterator();
            while (it.hasNext())
            {
                DocMsg obj = (DocMsg) it.next();
                if (obj.isValid() && obj.isActive())
                {
                    succList.add(obj.getRefNo() + " (" + obj.getOriginalFilename() + ")");
                }
                
            }
        }
        map.put("SUCC_LIST", succList);
        return map;
    }


    private Map<String, List<DocMsg>> groupByBuyerAndType(List<DocMsg> docs)
    {
        Map<String, List<DocMsg>> rlt = new HashMap<String, List<DocMsg>>();

        for (DocMsg doc : docs)
        {
            if (!(doc.isValid() && doc.isActive()))
            {
                continue;
            }
            if (DeploymentMode.REMOTE
                    .equals(doc.getBuyer().getDeploymentMode()))
            {
                continue;
            }

            String key = doc.getReceiverOid() + DELIMITOR + doc.getMsgType();

            if (rlt.containsKey(key))
            {
                rlt.get(key).add(doc);
            }
            else
            {
                List<DocMsg> list = new ArrayList<DocMsg>();
                list.add(doc);
                rlt.put(key, list);
            }
        }

        return rlt;
    }


    private Map<String, List<DocMsg>> groupByType(List<DocMsg> docs)
    {
        Map<String, List<DocMsg>> rlt = new HashMap<String, List<DocMsg>>();

        for (DocMsg doc : docs)
        {
            String key = doc.getMsgType().name();

            if (rlt.containsKey(key))
            {
                rlt.get(key).add(doc);
            }
            else
            {
                List<DocMsg> list = new ArrayList<DocMsg>();
                list.add(doc);
                rlt.put(key, list);
            }
        }

        return rlt;
    }


    private void inboundAlertBuyers(BatchMsg batch) throws Exception
    {
        Map<String, List<DocMsg>> group = this.groupByBuyerAndType(batch
                .getDocs());

        for (Map.Entry<String, List<DocMsg>> entry : group.entrySet())
        {
            int delimitorIndex = entry.getKey().indexOf(DELIMITOR);

            BigDecimal buyerOid = new BigDecimal(entry.getKey().substring(0,
                    delimitorIndex));
            String type = entry.getKey().substring(delimitorIndex + 1);

            BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(
                    buyerOid, type);
            BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
            if (null == setting || "INTERVAL".equals(setting.getAlertFrequency().toString()) || null == setting.getRcpsAddrs()
                    || setting.getRcpsAddrs().trim().isEmpty())
            {
                continue;
            }
            String[] emailTo = setting.retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
            String subject = "Arrival of document(s) - " + type + "(s)";
            Map<String, Object> map = initInboundAlertBuyerMap(entry.getValue());
            map.put("BUYER_NAME", buyer.getBuyerName());
            map.put("BUYER_CODE", buyer.getBuyerCode());
            
            ControlParameterHolder cp = controlParameterService.selectCacheControlParameterBySectIdAndParamId(
                CoreCommonConstants.SECT_ID_CTRL, CoreCommonConstants.PARAM_ID_HELPDESK_NO);
            map.put("CONTACT_NO", cp.getStringValue());
            
            String[] attachedFileNames = null;
            byte[][] attachedContent = null;
            if (MsgType.INV.name().equalsIgnoreCase(type))
            {
                BusinessRuleExHolder rule = businessRuleService.selectRulesByKey(buyerOid, "PoConvertInv", "SorPO", "PdfAsAttachment");
                if (rule != null)
                {
                    List<DocMsg> succList = (List<DocMsg>) map.get("SUCC_LIST");
                    attachedFileNames = new String[succList.size()];
                    attachedContent = new byte[succList.size()][];
                    String yyyymm = DateUtil.getInstance().getYearAndMonth(new Date());
                    String docPath = mboxUtil.getFolderInSupplierDocOutPath(batch.getSupplier().getMboxId(), yyyymm);
                    for (int i = 0; i < succList.size(); i++)
                    {
                        DocMsg doc = succList.get(0);
                        attachedFileNames[i] = doc.getReportFilename();
                        attachedContent[i] = IOUtils.toByteArray(new FileInputStream(new File(docPath, doc.getReportFilename())));
                    }
                }
            }
            if (attachedFileNames == null || attachedContent == null)
            {
                emailEngine.sendHtmlEmail(emailTo, subject, "ALERT_INBOUND_BUYER.vm", map);
            }
            else
            {
                emailEngine.sendEmailWithAttachedDocuments(emailTo, subject, "ALERT_INBOUND_BUYER.vm", map, attachedFileNames, attachedContent);
            }
        }
    }


    @SuppressWarnings("unchecked")
    private void inboundAlertSupplier(BatchMsg batch) throws Exception
    {
        if (DeploymentMode.REMOTE.equals(batch.getSupplier()
                .getDeploymentMode()))
        {
            return;
        }
        Map<String, List<DocMsg>> group = this.groupByType(batch.getDocs());

        for (Map.Entry<String, List<DocMsg>> entry : group.entrySet())
        {
            SupplierMsgSettingHolder setting = supplierMsgSettingService
                    .selectByKey(batch.getSenderOid(), entry.getKey());

            if (null == setting || null == setting.getRcpsAddrs()
                    || setting.getRcpsAddrs().trim().isEmpty())
            {
                return;
            }
            
            String[] emailTo = setting.retrieveRcpsAddrsByDelimiterChar(CoreCommonConstants.COMMA_DELIMITOR);
            String subject = "Summary of Dispatch Status - " + entry.getKey()
                    + "(s)";
            Map<String, Object> map = initInboundAlertSupplierMap(entry.getValue());
            
            List<DocMsg> errorList = (List<DocMsg>)map.get("ERROR_LIST");
            List<DocMsg> inactiveList = (List<DocMsg>)map.get("INACTIVE_LIST");
            if (errorList.isEmpty() && inactiveList.isEmpty() && setting.getExcludeSucc())
            {
                return;
            }
            if (!errorList.isEmpty() || !inactiveList.isEmpty())
            {
                subject = subject + "  (Unsuccess Count: " + (errorList.size() + inactiveList.size()) + ")";
            }
            
            map.put("SUPPLIER_NAME", batch.getSenderName());
            map.put("BUYER_GIVEN_SUPPLIER_CODE", batch.getSenderCode());
            emailEngine.sendHtmlEmail(emailTo, subject, "ALERT_INBOUND_SUPPLIER.vm", map);
        }
        
        Date alertDate = new Date();
        if (batch.getDocs() != null && !batch.getDocs().isEmpty())
        {
            for (DocMsg docMsg : batch.getDocs())
            {
                if (!docMsg.isActive() || !docMsg.isValid())
                {
                    continue;
                }
                MsgTransactionsHolder msg = msgTransactionsService.selectByKey(docMsg.getDocOid());
                msg.setAlertDate(alertDate);
                msgTransactionsService.updateByPrimaryKey(null, msg);
            }
        }
    }


    private Map<String, Object> initInboundAlertSupplierMap(List<DocMsg> list)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DocMsg> succList = new ArrayList<DocMsg>();
        List<DocMsg> errorList = new ArrayList<DocMsg>();
        List<DocMsg> inactiveList = new ArrayList<DocMsg>();
        if (list != null)
        {
            Iterator<DocMsg> it = list.iterator();
            while (it.hasNext())
            {
                DocMsg obj = (DocMsg) it.next();
                if (obj.isValid())
                {
                    if (obj.isActive())
                    {
                        succList.add(obj);
                    }
                    else
                    {
                        inactiveList.add(obj);
                    }
                }
                else
                {
                    errorList.add(obj);
                }
            }
        }
        map.put("TOTAL_LIST", list);
        map.put("SUCC_LIST", succList);
        map.put("ERROR_LIST", errorList);
        map.put("INACTIVE_LIST", inactiveList);
        return map;
    }

    
    private Map<String, Object> initInboundAlertBuyerMap(List<DocMsg> list)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DocMsg> succList = new ArrayList<DocMsg>();
        if (list != null)
        {
            Iterator<DocMsg> it = list.iterator();
            while (it.hasNext())
            {
                DocMsg obj = (DocMsg) it.next();
                if (obj.isValid() && obj.isActive())
                {
                    succList.add(obj);
                }
            }
        }
        map.put("SUCC_LIST", succList);
        return map;
    }
}
