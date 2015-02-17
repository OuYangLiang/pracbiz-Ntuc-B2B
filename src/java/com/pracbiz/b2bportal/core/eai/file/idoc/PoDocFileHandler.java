package com.pracbiz.b2bportal.core.eai.file.idoc;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class PoDocFileHandler extends DefaultDocFileHandler<PoDocMsg, PoHolder> implements
        CoreCommonConstants
{
    private final static Logger log = LoggerFactory.getLogger(PoDocFileHandler.class);
    @Autowired
    protected SupplierService supplierService;
    @Autowired
    protected MailBoxUtil mboxUtil;
    
    
    @Override
    public void createFile(PoHolder data, File targetFile, String expectedFormat)
            throws Exception
    {
        if (expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            this.successor.createFile(data, targetFile, expectedFormat);
            return;
        }
        
        File oldFile = this.getOldPoFile(data);
        
        if (!oldFile.isFile() || !oldFile.exists())
        {
            return;
        }
        Document document = FileParserUtil.getInstance().build(FileUtil.getInstance().readByteFromDisk(oldFile.getPath()));
        Element root = document.getRootElement().getChild("IDOC");
        List<Element> e1edp01Elements = root.getChildren("E1EDP01");
        
        for (int i = 0; i < e1edp01Elements.size() ; i++)
        {
            Element element = e1edp01Elements.get(i).getChild("E1EDP20").getChild("EDATU");
            element.setText(DateUtil.getInstance().convertDateToString(data.getPoHeader().getDeliveryDateFrom(), DateUtil.DATE_FORMAT_YYYYMMDD));
        }
        
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        format.setExpandEmptyElements(true);
        XMLOutputter out = new XMLOutputter(format);
        
        FileOutputStream fos = new FileOutputStream(targetFile);
        
        try
        {
            out.output(document, fos);
        }
        finally
        {
            fos.close();
            fos = null;
        }

        //EbxmlParseHelper.getInstance().validateEbxml(targetFile, getXSDSchema());
    }
    
    
    @Override
    public byte[] getFileByte(PoHolder data, File targetFile, String expectedFormat)
    {
        return null;
    }
    
    
    @Override
    protected String getXSDSchema()
    {
        return "xsd/idoc/inv/Idocs.xsd";
    }
    
    
    private File getOldPoFile(PoHolder data) throws Exception
    {
        SupplierHolder supplier = supplierService.selectSupplierByKey(data.getPoHeader().getSupplierOid());
        
        String oldFilePath = mboxUtil.getFolderInSupplierArchInPath(supplier.getMboxId(), DateUtil.getInstance().getYearAndMonth(
            new Date())) + PS + this.getOldFileName(data);
        
        return new File(oldFilePath);
    }
    

    private String getOldFileName(PoHolder data)
    {
        return MsgType.PO + DOC_FILENAME_DELIMITOR
            + data.getPoHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
            + data.getPoHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
            + StringUtil.getInstance().convertDocNo(data.getPoHeader().getPoNo()) 
            + DOC_FILENAME_DELIMITOR + data.getOldPoOid() + ".xml";
    }
    
    
    @Override
    public String getTargetFilename(PoHolder data, String expectedFormat)
    {
        if (expectedFormat != null
                && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }

        return MsgType.PO + DOC_FILENAME_DELIMITOR
                + data.getPoHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
                + data.getPoHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(data.getPoHeader().getPoNo()) 
                + DOC_FILENAME_DELIMITOR + data.getPoHeader().getPoOid() + ".xml";
    }

    @Override
    protected String processFormat()
    {
        return FP_IDOC;
    }

    @Override
    public void readFileContent(PoDocMsg docMsg, byte[] input)
            throws Exception
    {
        log.info(" :::: start to read po source file .");
        Document document = FileParserUtil.getInstance().build(input);
        PoHolder po = new PoHolder();
        initHolder(document, docMsg, po);
        docMsg.setData(po);
        log.info(" :::: end to read po source file .");
        
    }

    @Override
    protected String translate(PoDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
                this.getTargetFilename(docMsg.getData(), FP_IDOC));
        this.createFile(docMsg.getData(), targetFile, FP_IDOC);

        return targetFile.getName();
    }
    
    
    private void initHolder(Document document, DocMsg docMsg, PoHolder po) throws Exception
    {
        PoHeaderHolder header = new PoHeaderHolder();
        header.setPoOid(docMsg.getDocOid());
        header.setDocAction("A");
        header.setActionDate(new Date());
        header.setPoType(PoType.SOR);
        header.setBuyerCode(docMsg.getSenderCode());
        header.setSupplierCode(docMsg.getReceiverCode());
        BuyerHolder buyer = docMsg.getBuyer();
        SupplierHolder supplier = docMsg.getSupplier();
        this.initBuyerInfo(buyer, header);
        this.initSupplierInfo(supplier, header);
        header.setAdditionalDiscountAmount(BigDecimal.ZERO);
        header.setPoStatus(PoStatus.NEW);
        header.setPoSubType("1");
        
        Map<String, PoDetailHolder> detailMap = new HashMap<String, PoDetailHolder>();
        List<PoDetailHolder> detailList = new ArrayList<PoDetailHolder>();
        Map<String, PoLocationHolder> locationMap = new HashMap<String, PoLocationHolder>();
        List<PoLocationHolder> locationList = new ArrayList<PoLocationHolder>();
        List<PoLocationDetailHolder> locationDetailList = new ArrayList<PoLocationDetailHolder>();
        
        Map<String, PoLocationDetailHolder> locDetailMap = new HashMap<String, PoLocationDetailHolder>();
        
        Element root = document.getRootElement().getChild("IDOC");
        
        List<Element> edi_dc40List = root.getChildren("EDI_DC40");
        PoHeaderExtendedHolder headerExSndprn = new PoHeaderExtendedHolder();
        headerExSndprn.setPoOid(docMsg.getDocOid());
        headerExSndprn.setFieldName("SNDPRN");
        headerExSndprn.setFieldType("S");
        headerExSndprn.setStringValue(getElementByChildElement(edi_dc40List, "TABNAM", "EDI_DC40").getChildTextTrim("SNDPRN"));
        
        PoHeaderExtendedHolder headerExRcvprn = new PoHeaderExtendedHolder();
        headerExRcvprn.setPoOid(docMsg.getDocOid());
        headerExRcvprn.setFieldName("RCVPRN");
        headerExRcvprn.setFieldType("S");
        headerExRcvprn.setStringValue(getElementByChildElement(edi_dc40List, "TABNAM", "EDI_DC40").getChildTextTrim("RCVPRN"));
        List<PoHeaderExtendedHolder> headerExtendeds = new ArrayList<PoHeaderExtendedHolder>();
        headerExtendeds.add(headerExSndprn);
        headerExtendeds.add(headerExRcvprn);
        
        if (null != root.getChildren("E1EDS01"))
        {
            List<Element> e1eds01List = root.getChildren("E1EDS01");
            if (null != getElementByChildElement(e1eds01List, "SUMID", "002") && null != getElementByChildElement(e1eds01List, "SUMID", "002").getChildren("SUMME"))
            {
                BigDecimal totalCost = BigDecimalUtil.getInstance().convertStringToBigDecimal(
                    getElementByChildElement(e1eds01List, "SUMID", "002").getChildTextTrim("SUMME"), 4);
                header.setTotalCost(totalCost);
                header.setNetCost(header.getTotalCost());
            }
        }
        
        List<Element> e1edk14Elements = root.getChildren("E1EDK14");
        
        String poType = getElementByChildElement(e1edk14Elements, "QUALF", "013").getChildTextTrim("ORGID");
        
        header.setPoSubType2(poType);
            
        if (!poType.equalsIgnoreCase("ZNB"))
        {
            List<Element> e1edka1Elements = root.getChildren("E1EDKA1");
            Element shipToCodeEl = getElementByChildElement(e1edka1Elements, "PARVW", "WE");
            if (shipToCodeEl != null)
            {
                String shipToCode = shipToCodeEl.getChildTextTrim("LIFNR");
                header.setShipToCode(shipToCode.trim());
            }
            
            Element shipToNameEl = getElementByChildElement(e1edka1Elements, "PARVW", "WE");
            if (shipToNameEl != null)
            {
                String shipToName = shipToNameEl.getChildTextTrim("NAME1");
                header.setShipToName(shipToName);
            }
            
            PoLocationHolder location = new PoLocationHolder();
            location.setPoOid(header.getPoOid());
            location.setLineSeqNo(1);
            location.setLocationCode(header.getShipToCode());
            location.setLocationName(header.getShipToName());
            location.setAllEmptyStringToNull();
            locationList.add(location);
            
            if (poType.equalsIgnoreCase("ZQO"))
            {
                Element supplierEl = getElementByChildElement(e1edka1Elements, "PARVW", "LF");
                Element invNoEl = supplierEl.getChild("BNAME");
                
                if (invNoEl != null && !invNoEl.getTextTrim().isEmpty())
                {
                    PoHeaderExtendedHolder headerExBname = new PoHeaderExtendedHolder();
                    headerExBname.setPoOid(header.getPoOid());
                    headerExBname.setFieldName("BNAME");
                    headerExBname.setFieldType("S");
                    headerExBname.setStringValue(invNoEl.getTextTrim());
                    headerExtendeds.add(headerExBname);
                }
            }
        }
        
        List<Element> e1edk02Elements = root.getChildren("E1EDK02"); 
        
        String poNo = getElementByChildElement(e1edk02Elements, "QUALF", "001").getChildTextTrim("BELNR");
        header.setPoNo(StringUtil.getInstance().convertDocNoWithNoLeading(poNo, LEADING_ZERO));
        
        String poDate = getElementByChildElement(e1edk02Elements, "QUALF", "001").getChildTextTrim("DATUM");
        header.setPoDate(DateUtil.getInstance().convertStringToDate(poDate, DateUtil.DATE_FORMAT_YYYYMMDD));

        String remarks = null;
        List<Element> e1edkt1Elements = root.getChildren("E1EDKT1");
        Element tdid = getElementByChildElement(e1edkt1Elements, "TDID", "F17");
        if (tdid != null)
        {
            Element e1edkt2 = tdid.getChild("E1EDKT2");
            
            if (e1edkt2 != null)
            {
                remarks = e1edkt2.getChildTextTrim("TDLINE");
            }
        }
        header.setOrderRemarks(remarks);
       
        List<Element> e1edp01Elements = root.getChildren("E1EDP01");
        
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (int i = 0; i < e1edp01Elements.size() ; i++)
        {
            Element element = e1edp01Elements.get(i);
            
            List<Element> e1edp19Elements  = element.getChildren("E1EDP19");
            
            PoDetailHolder detail = null;
            PoLocationHolder location = null;
            
            String buyerItemCode = StringUtil.getInstance()
                .convertDocNoWithNoLeading(
                    getElementByChildElement(e1edp19Elements, "QUALF", "001")
                        .getChildTextTrim("IDTNR"), LEADING_ZERO);
            String itemDesc = getElementByChildElement(e1edp19Elements, "QUALF", "001").getChildTextTrim("KTEXT");
            Element barcodeEl = getElementByChildElement(e1edp19Elements, "QUALF", "003");
            String barcode = null;
            if (barcodeEl != null)
            {
                barcode = barcodeEl.getChildTextTrim("IDTNR");
            }
         
            BigDecimal orderQty = BigDecimalUtil.getInstance().convertStringToBigDecimal(element.getChildTextTrim("MENGE"), 4) == null 
                ? BigDecimal.ZERO
                : BigDecimalUtil.getInstance().convertStringToBigDecimal(element.getChildTextTrim("MENGE"), 4);
            String orderUom = element.getChildTextTrim("MENEE");
            
            BigDecimal unitCost = null;
            Element vpreiElement = element.getChild("VPREI");
            if (vpreiElement != null)
            {
                unitCost = BigDecimalUtil.getInstance().convertStringToBigDecimal(vpreiElement.getTextTrim(), 4);
            }
            
            BigDecimal itemCost = BigDecimalUtil.getInstance().convertStringToBigDecimal(element.getChildTextTrim("NETWR"), 4) == null
                ? BigDecimal.ZERO
                : BigDecimalUtil.getInstance().convertStringToBigDecimal(element.getChildTextTrim("NETWR"), 4);
            String lineRefNo = element.getChildTextTrim("POSEX");
            String deliveryDate = element.getChild("E1EDP20").getChildTextTrim("EDATU");
            header.setDeliveryDateFrom(DateUtil.getInstance().convertStringToDate(deliveryDate, DateUtil.DATE_FORMAT_YYYYMMDD));
            header.setDeliveryDateTo(DateUtil.getInstance().convertStringToDate(deliveryDate, DateUtil.DATE_FORMAT_YYYYMMDD));
            
            if (detailMap.containsKey(buyerItemCode))
            {
                detail = detailMap.get(buyerItemCode);
                if(unitCost != null
                    && (unitCost.compareTo(BigDecimal.ZERO) > 0)
                    && (detail.getUnitCost().compareTo(BigDecimal.ZERO) == 0))
                {
                    detail.setUnitCost(unitCost);
                }
                detail.setOrderQty(detail.getOrderQty().add(orderQty));
                detail.setItemCost(detail.getItemCost().add(itemCost));
            }
            else
            {
                detail = new PoDetailHolder();
                detail.setPoOid(docMsg.getDocOid());
                detail.setLineSeqNo(detailList.size() + 1);
                detail.setBuyerItemCode(buyerItemCode);
                detail.setItemDesc(itemDesc);
                detail.setBarcode(barcode);
                detail.setOrderQty(orderQty);
                detail.setOrderUom(orderUom);
                detail.setUnitCost(unitCost == null ? BigDecimal.ZERO : unitCost);
                detail.setNetUnitCost(unitCost == null ? BigDecimal.ZERO : unitCost);
                detail.setPackCost(unitCost == null ? BigDecimal.ZERO : unitCost);
                detail.setPackingFactor(BigDecimal.ONE);
                detail.setNetPackCost(unitCost == null ? BigDecimal.ZERO : unitCost);
                detail.setOrderBaseUnit("U");
                detail.setItemCost(itemCost);
                detail.setCostDiscountAmount(BigDecimal.ZERO);
                detailMap.put(buyerItemCode, detail);
                
                detail.setAllEmptyStringToNull();
                
                if (null == detail.getItemCost())
                    detail.setItemCost(BigDecimal.ZERO);
                detailList.add(detail);
            }
            
            if (poType.equalsIgnoreCase("ZNB"))
            {
                List<Element> e1edpa1Elements = element.getChildren("E1EDPA1");
                String locationCode = getElementByChildElement(e1edpa1Elements, "PARVW", "WE").getChildTextTrim("LIFNR");
                String locationName = getElementByChildElement(e1edpa1Elements, "PARVW", "WE").getChildTextTrim("NAME1");
                if (locationMap.containsKey(locationCode))
                {
                    location = locationMap.get(locationCode);
                }
                else
                {
                    location = new PoLocationHolder();
                    location.setPoOid(header.getPoOid());
                    location.setLineSeqNo(locationList.size() + 1);
                    location.setLocationCode(locationCode);
                    location.setLocationName(locationName);
                    location.setAllEmptyStringToNull();
                    locationMap.put(locationCode, location);
                    locationList.add(location);
                }
            }
            else
            {
                location = locationList.get(0);
            }
            
            String key = detail.getLineSeqNo() + "-" + location.getLineSeqNo();
            if (locDetailMap.containsKey(key))
            {
                locDetailMap.get(key).setLocationShipQty(locDetailMap.get(key).getLocationShipQty().add(orderQty));
            }
            else
            {
                PoLocationDetailHolder locationDetail = new PoLocationDetailHolder();
                locationDetail.setPoOid(header.getPoOid());
                locationDetail.setLocationLineSeqNo(location.getLineSeqNo());
                locationDetail.setDetailLineSeqNo(detail.getLineSeqNo());
                locationDetail.setLocationShipQty(orderQty);
                locationDetail.setLineRefNo(lineRefNo);
                locationDetail.setAllEmptyStringToNull();
                locationDetailList.add(locationDetail);
                
                locDetailMap.put(key, locationDetail);
            }
            
            totalCost = totalCost.add(detail.getItemCost());
        }
        
        if (null == header.getTotalCost() || null == header.getNetCost())
        {
            header.setTotalCost(totalCost);
            header.setNetCost(totalCost);
        }
        
        header.setAllEmptyStringToNull();
        po.setPoHeader(header);
        po.setDetails(detailList);
        po.setLocations(locationList);
        po.setLocationDetails(locationDetailList);
        po.setHeaderExtendeds(headerExtendeds);
    }
    
    
    private Element getElementByChildElement(List<Element> elements, String childNode, String childNodeValue)
    {
        if (elements == null || elements.isEmpty() || childNode == null || childNode.trim().isEmpty() 
                || childNodeValue == null || childNodeValue.trim().isEmpty())
        {
            return null;
        }
        
        for (Element element : elements)
        {
            String qualf = element.getChildTextTrim(childNode);
            
            if (qualf.equals(childNodeValue.trim()))
            {
                return element;
            }
        }
        
        return null;
    }
    
    
//    public static void main(String[] args) throws Exception
//    {
//        PoDocFileHandler handler = new PoDocFileHandler();
//        PoDocMsg doc = new PoDocMsg();
//        handler.readFileContent(doc, new File("D:/PO_FPCSJ_SUPPLIER_26808586.xml"));
//    }

}
