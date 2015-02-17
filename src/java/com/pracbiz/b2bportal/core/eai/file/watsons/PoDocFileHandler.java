//*****************************************************************************
//
// File Name       :  PoFileHandler.java
// Date Created    :  Dec 17, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 17, 2013 10:17:30 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.watsons;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EbxmlParseHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PoDocFileHandler extends DefaultDocFileHandler<PoDocMsg, PoHolder>
    implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(PoDocFileHandler.class);

    private String ELEMENT_ITEM = "Item(";
    @Override
    public byte[] getFileByte(PoHolder data, File targetFile, String expectedFormat)
        throws Exception
    {
        return null;

    }

    @Override
    public String getTargetFilename(PoHolder data, String expectedFormat)
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }

        return MsgType.PO
            + DOC_FILENAME_DELIMITOR
            + data.getPoHeader().getBuyerCode()
            + DOC_FILENAME_DELIMITOR
            + data.getPoHeader().getSupplierCode()
            + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(
                data.getPoHeader().getPoNo()) + DOC_FILENAME_DELIMITOR
            + data.getPoHeader().getPoOid() + ".xml";
    }

    @Override
    protected String processFormat()
    {
        return WATSONS;
    }

    @Override
    public void readFileContent(PoDocMsg docMsg, byte[] input)
        throws Exception
    {
        log.info(" :::: start to read Po Idoc source file.");
        Document document = FileParserUtil.getInstance().build(input);
        PoHolder po = new PoHolder();
        initHeader(document, docMsg, po);
        initDetailsAndOthers(document, po);
        
        docMsg.setData(po);
        log.info(" :::: end to read Po Idoc source file.");
    }

    @Override
    protected String translate(PoDocMsg docMsg) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    private void initHeader(Document document, PoDocMsg docMsg, PoHolder po)throws Exception
    {
        PoHeaderHolder header = new PoHeaderHolder();
        
        EbxmlParseHelper ebxmlHelper = EbxmlParseHelper.getInstance();
        String poNo = ebxmlHelper.obtainValueFrom(document, "Header.PONumber");
        
        Date poDate = ebxmlHelper.obtainDateFrom(document,
            "Header.DocumentDate", DateUtil.EBXML_DATE_FORMAT_YYYYMMDDHHMMSS);
        
        Date deliveryDate = ebxmlHelper.obtainDateFrom(document,
            "Header.ExptDeliveryDate",DateUtil.EBXML_DATE_FORMAT_YYYYMMDDHHMMSS);
        
        String remarks = ebxmlHelper.obtainValueFrom(document, "Header.Remarks");
        remarks = (remarks == null || remarks.isEmpty())? null : remarks.substring(remarks.lastIndexOf('}') + 1, remarks.length());
        
        header.setOrderRemarks(remarks);
        BigDecimal totalCost = ebxmlHelper.obtainBigDecimalFrom(document, "Summary.TotalLineItemsAmount", 4);
        
        header.setPoOid(docMsg.getDocOid());
        header.setPoNo(poNo);
        header.setPoDate(poDate);
        header.setDeliveryDateFrom(deliveryDate);
        header.setDeliveryDateTo(deliveryDate);
        header.setActionDate(new Date());
        header.setDocAction("A");
        header.setPoType(PoType.SOR);
        header.setPoSubType("1");
        header.setPoStatus(PoStatus.NEW);
        header.setAdditionalDiscountAmount(BigDecimal.ZERO);
        header.setBuyerCode(docMsg.getSenderCode());
        header.setSupplierCode(docMsg.getReceiverCode());
        header.setTotalCost(totalCost);
        header.setNetCost(totalCost);
        
        
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, header);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, header);
        
        header.setAllEmptyStringToNull();
        po.setPoHeader(header);
    }
    
    
    private void initDetailsAndOthers(Document document, PoHolder po)throws Exception
    {
        EbxmlParseHelper ebxmlHelper = EbxmlParseHelper.getInstance();
        
        PoDetailHolder poDetail = null;
        List<Element> details = document.getRootElement().getChildren("Item");
        Map<String, PoLocationHolder> locMap = new HashMap<String, PoLocationHolder>();
        List<PoDetailHolder> poDetails = new ArrayList<PoDetailHolder>();
        List<PoDetailExtendedHolder> poDetailExs = new ArrayList<PoDetailExtendedHolder>();
        List<PoLocationDetailHolder> poLocDetails = new ArrayList<PoLocationDetailHolder>();
        List<PoLocationHolder> poLocations = new ArrayList<PoLocationHolder>();
        
        int locLineSeqNo = 1;
        for (int i = 0 ; i < details.size(); i ++ )
        {
            poDetail = new PoDetailHolder();
            
            Integer lineSeqNo = Integer.valueOf(ebxmlHelper.obtainValueFrom(
                document, "Item(" + i + ").LineItemNum"));
            poDetail.setLineSeqNo(lineSeqNo);
            
            String barcode = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").EANItem.EANItemNo");
            poDetail.setBarcode(barcode);
            
            String buyerItemCode = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").ItemCode");
            poDetail.setBuyerItemCode(buyerItemCode);
            
            String supplierItemCode = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").SupplierItemCode");
            poDetail.setSupplierItemCode(supplierItemCode);
            
            String brand = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").Brand");
            poDetail.setBrand(brand);
            
            String itemDesc = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").ItemDescription");
            poDetail.setItemDesc(itemDesc);
            
            String orderUom = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").UnitSize.@uom" );
            poDetail.setOrderUom(orderUom);
            
            BigDecimal packingFac = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").PackingQty", 2);
            poDetail.setPackingFactor(packingFac);
            
            poDetail.setOrderBaseUnit("P");
            
            BigDecimal packCost = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").GrossUnitPrice", 4);
            poDetail.setPackCost(packCost);
            
            BigDecimal netPackCost = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").NetUnitPrice", 4);
            poDetail.setNetPackCost(netPackCost);
            
            BigDecimal retailPrice = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").UnitSellingPrice", 4);
            poDetail.setRetailPrice(retailPrice);
            
            BigDecimal orderQty = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").OrderedQty", 4);
            poDetail.setOrderQty(orderQty);
            
            BigDecimal focQty = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").FreeQty", 2);
            poDetail.setFocQty(focQty);
            
            BigDecimal itemCost = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").LineItemAmt", 4);
            poDetail.setItemCost(itemCost);
            
            String itemRemarks = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").RemarksinDetail");
            itemRemarks = (itemRemarks == null || itemRemarks.isEmpty()) ? null : itemRemarks.substring(itemRemarks.lastIndexOf('}') + 1, itemRemarks.length());
            poDetail.setItemRemarks(itemRemarks);;
            
            BigDecimal unitCost = packCost.divide(packingFac);
            poDetail.setUnitCost(unitCost);
            
            BigDecimal netUnitCost = netPackCost.divide(packingFac);
            poDetail.setNetUnitCost(netUnitCost);
            
            poDetail.setCostDiscountAmount(BigDecimal.ZERO);
            //*************
            //po location
            //*************
            PoLocationHolder poLocation = new PoLocationHolder();
            PoLocationDetailHolder poLocDetail = new PoLocationDetailHolder();
            String locationCode = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.ID");
            poLocation.setLocationCode(locationCode);
            
            String locationName = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.NameAndAddress.Name");
            poLocation.setLocationName(locationName);
            
            String locationAddr1 = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.NameAndAddress.Address1");
            poLocation.setLocationAddr1(locationAddr1);
            
            String locationAddr2 = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.NameAndAddress.Address2");
            poLocation.setLocationAddr2(locationAddr2);
            
            String locationAddr3 = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.NameAndAddress.Address3");
            poLocation.setLocationAddr3(locationAddr3);
            
            String locationCtryCode = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.NameAndAddress.Country");
            poLocation.setLocationCtryCode(locationCtryCode);
            
            String locationPostalCode = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.NameAndAddress.Postal");
            poLocation.setLocationPostalCode(locationPostalCode);
            
            String locationContactTel = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.Contact.Phone");
            poLocation.setLocationContactTel(locationContactTel);
            
            //*******************
            //po detailEx
            //*******************
            String unitSize = ebxmlHelper.obtainValueFrom(document, ELEMENT_ITEM + i + ").UnitSize");
            if (unitSize != null && !unitSize.isEmpty())
            {
                PoDetailExtendedHolder poDetailEx = new PoDetailExtendedHolder();
                poDetailEx.setPoOid(po.getPoHeader().getPoOid());
                poDetailEx.setLineSeqNo(lineSeqNo);
                poDetailEx.setFieldName("UnitSize");
                poDetailEx.setFieldType("I");
                poDetailEx.setIntValue(Integer.valueOf(unitSize));
                poDetailEx.setAllEmptyStringToNull();
                poDetailExs.add(poDetailEx);
            }
            
            //******************
            //po locationDetail
            //******************
            if (locMap.containsKey(locationCode))
            {
                poLocDetail.setLocationLineSeqNo(locMap.get(locationCode).getLineSeqNo());
            }
            else
            {
                poLocation.setPoOid(po.getPoHeader().getPoOid());
                poLocation.setLineSeqNo(locLineSeqNo);
                locMap.put(locationCode, poLocation);
                poLocDetail.setLocationLineSeqNo(locLineSeqNo);
                locLineSeqNo ++ ;
            }
           
            BigDecimal locationShipQty = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.Qty", 2);
            poLocDetail.setLocationShipQty(locationShipQty);
            BigDecimal locationFocQty = ebxmlHelper.obtainBigDecimalFrom(document, ELEMENT_ITEM + i + ").DeliveryDetail.FreeQty", 2);
            poLocDetail.setLocationFocQty(locationFocQty);
            poLocDetail.setDetailLineSeqNo(lineSeqNo);
            
            
            poDetail.setPoOid(po.getPoHeader().getPoOid());
            poLocDetail.setPoOid(po.getPoHeader().getPoOid());
            poDetail.setAllEmptyStringToNull();
            poLocDetail.setAllEmptyStringToNull();
            
            poDetails.add(poDetail);
            poLocDetails.add(poLocDetail);
            
        }
        
        if (locMap.size() == 1)
        {
            for (Map.Entry<String, PoLocationHolder> entry : locMap.entrySet())
            {
                po.getPoHeader().setShipToCode(entry.getValue().getLocationCode());
                po.getPoHeader().setShipToName(entry.getValue().getLocationName());
                po.getPoHeader().setAllEmptyStringToNull();
            }
        }
        
        for (Map.Entry<String, PoLocationHolder> entry : locMap.entrySet())
        {
            entry.getValue().setAllEmptyStringToNull();
            poLocations.add(entry.getValue());
        }
        
      
        po.setLocations(poLocations);
        po.setDetails(poDetails);
        if (!poDetailExs.isEmpty())
        {
            po.setDetailExtendeds(poDetailExs);
        }
        po.setLocationDetails(poLocDetails);
    }

//    
//    public static void main(String[] args) throws Exception
//    {
//        EbxmlParseHelper ebxmlHelper = EbxmlParseHelper.getInstance();
//        Document document = FileParserUtil.getInstance().build(new File("C:/Documents and Settings/liyong/桌面/watsons/PO_S_WAT001_B0003_0120700074.xml"));
//        String itemRemarks = ebxmlHelper.obtainValueFrom(document, "Item(" + 0 + ").RemarksinDetail");
//        itemRemarks = "";
//        System.out.println("itemRemarks : " + itemRemarks);
//        itemRemarks = itemRemarks.isEmpty() ? null : itemRemarks.substring(itemRemarks.lastIndexOf('}') + 1, itemRemarks.length());
//        System.out.println(itemRemarks);
//        
//    }
}
