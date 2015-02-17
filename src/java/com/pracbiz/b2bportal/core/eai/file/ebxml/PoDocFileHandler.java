//*****************************************************************************
//
// File Name       :  PoDocFileHandler.java
// Date Created    :  Nov 26, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 26, 2012 9:05:57 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.ebxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EbxmlParseHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.JdomXmlHelper;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
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
 * @author ouyang
 */
public class PoDocFileHandler extends DefaultDocFileHandler<PoDocMsg, PoHolder>
        implements CoreCommonConstants
{
    private static final String BUYER_ASSIGNED_IDENTIFIER_FOR_PARTY = "BUYER_ASSIGNED_IDENTIFIER_FOR_PARTY";
    private final static Logger log = LoggerFactory.getLogger(PoDocFileHandler.class);


    @Override
    protected String processFormat()
    {
        return EBXML;
    }


    @Override
    public void readFileContent(PoDocMsg docMsg, byte[] input)
            throws Exception
    {
        log.info(" :::: start to read ebxml PO source file.");
        Document document = FileParserUtil.getInstance().build(input);
        PoHolder po = new PoHolder();
        initHeader(document, docMsg, po);
        initDetailsAndOthers(document, po);
        initPoHeaderExtended(document, po);
        
        docMsg.setData(po);
        log.info(" :::: end to read ebxml PO source file.");
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
                + StringUtil.getInstance().convertDocNo(data.getPoHeader().getPoNo()) + DOC_FILENAME_DELIMITOR
                + data.getPoHeader().getPoOid() + ".xml";
    }


    @Override
    protected String translate(PoDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
                this.getTargetFilename(docMsg.getData(), EBXML));
        this.createFile(docMsg.getData(), targetFile, EBXML);

        return targetFile.getName();
    }


    @Override
    public byte[] getFileByte(PoHolder data, File targetFile, String expectedFormat)
            throws Exception
    {   
        PoHolder poHolder = data;
        Document document = new Document();
        // HDR
        if (poHolder.getPoHeader() != null)
        {
            this.createNamespace(poHolder, document);
            this.createHeader(poHolder, document);

        }

        if (poHolder.getDetails() != null)
        {
            createDetails(poHolder, document);
        }

        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        XMLOutputter out = new XMLOutputter(format);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        try
        {
            out.output(document, bos);
            return bos.toByteArray();
        }
        finally
        {
            bos.close();
            bos = null;
        }
    }


    public void initHeader(Document document, DocMsg docMsg, PoHolder po) throws Exception
    {
        PoHeaderHolder header = new PoHeaderHolder();
        String poDate = document.getRootElement().getAttributeValue(
                "creationDate");
        header.setPoDate(EbxmlParseHelper.getInstance().convertStringToDate(
                poDate, EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS));
        header.setActionDate(EbxmlParseHelper.getInstance()
                .convertStringToDate(poDate,
                        EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS));
        
        String deliveryDateFrom = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyStartDate");
        header.setDeliveryDateFrom(EbxmlParseHelper.getInstance()
                .convertStringToDate(deliveryDateFrom,
                        EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS));
       
        String deliveryDateTo = EbxmlParseHelper.getInstance().obtainValueFrom(
            document, "lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyEndDate");
        header.setDeliveryDateTo(EbxmlParseHelper.getInstance()
            .convertStringToDate(deliveryDateTo,
                EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS));
        
        String poNo = EbxmlParseHelper
                .getInstance()
                .obtainValueFrom(document,
                        "typedEntityIdentification.entityIdentification.uniqueCreatorIdentification");
        header.setPoNo(poNo);

        String ctryCode = EbxmlParseHelper.getInstance().obtainValueFrom(
                document,
                "buyer.partyInformation.nameAndAddress.countryISOcode");
        header.setBuyerCtryCode(ctryCode);
        String buyerName = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "buyer.partyInformation.nameAndAddress.name");
        header.setBuyerName(buyerName);

        String supplierCode = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "seller.alternatePartyIdentification");
        header.setSupplierCode(supplierCode);
        String supCtryCode = EbxmlParseHelper.getInstance().obtainValueFrom(
                document,
                "seller.partyInformation.nameAndAddress.countryISOcode");
        header.setSupplierCtryCode(supCtryCode);
        String supplierName = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "seller.partyInformation.nameAndAddress.name");
        header.setSupplierName(supplierName);

        String shipToCode = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "shipParty.alternatePartyIdentification");
        if (shipToCode != null
                && !shipToCode.toLowerCase(Locale.getDefault()).contains(
                        "ignore"))
        {
            header.setShipToCode(shipToCode);
            String shipToCtryCode = EbxmlParseHelper
                    .getInstance()
                    .obtainValueFrom(document,
                            "shipParty.partyInformation.nameAndAddress.countryISOcode");
            header.setShipToCtryCode(shipToCtryCode);
            String shipToName = EbxmlParseHelper.getInstance().obtainValueFrom(
                    document, "shipParty.partyInformation.nameAndAddress.name");
            header.setShipToName(shipToName);
        }
        String remarks = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "remarks");
        header.setOrderRemarks(remarks);
        header.setPoOid(docMsg.getDocOid());
        header.setPoType(PoType.SOR);
        header.setDocAction("A");
        header.setPoSubType("1");
        header.setAdditionalDiscountAmount(BigDecimal.ZERO);
        header.setPoStatus(PoStatus.NEW);
        header.setBuyerCode(docMsg.getSenderCode());
        header.setBuyerOid(docMsg.getSenderOid());
        header.setSupplierOid(docMsg.getReceiverOid());
        
        BuyerHolder buyer = docMsg.getBuyer();
        this.initBuyerInfo(buyer, header);
        SupplierHolder supplier = docMsg.getSupplier();
        this.initSupplierInfo(supplier, header);
        
        po.setPoHeader(header);
    }


    public void initDetailsAndOthers(Document document, PoHolder po) throws Exception
    {
        String detailNode = "lineItem";
        List<Element> poItems = document.getRootElement().getChildren(
                detailNode);

        if (poItems == null || poItems.isEmpty())
        {
            return;
        }

        List<PoDetailHolder> details = new ArrayList<PoDetailHolder>();
        Map<String, PoLocationHolder> locMap = new HashMap<String, PoLocationHolder>();
        List<PoLocationDetailHolder> locDetails = new ArrayList<PoLocationDetailHolder>();
        List<PoDetailExtendedHolder> detailExtendeds = new ArrayList<PoDetailExtendedHolder>();
        BigDecimal totalCost = BigDecimal.ZERO;
        for (int i = 0; i < poItems.size(); i++)
        {
            PoDetailHolder detail = new PoDetailHolder();
            detail.setLineSeqNo(i + 1);
            detail.setPoOid(po.getPoHeader().getPoOid());
            String packingFactor = EbxmlParseHelper
                    .getInstance()
                    .obtainValueFrom(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemHierarchy.quantityOfNextLevelTradeItemWithinInnerPack");
            BigDecimal pf = EbxmlParseHelper.getInstance()
                    .convertStringToBigDecimal(packingFactor, 2);
            String unitCostStr = EbxmlParseHelper.getInstance()
                    .obtainValueFrom(document,
                            detailNode + "(" + i + ").netPrice.amount");
            BigDecimal unitCost = EbxmlParseHelper.getInstance()
                    .convertStringToBigDecimal(unitCostStr, 4);
            detail.setPackingFactor(pf);
            if (pf.compareTo(BigDecimal.ONE) == 0)
            {
                detail.setOrderBaseUnit("U");
                detail.setUnitCost(unitCost);
                detail.setPackCost(unitCost);
            }
            else
            {
                detail.setOrderBaseUnit("P");
                detail.setPackCost(unitCost);
                //detail.setUnitCost(unitCost.divide(pf));
                detail.setUnitCost(unitCost.divide(pf, 4, RoundingMode.HALF_EVEN));
            }
            detail.setNetUnitCost(unitCost);
            String qtyStr = EbxmlParseHelper.getInstance().obtainValueFrom(
                    document, detailNode + "(" + i + ").requestedQuantity");
            BigDecimal qty = EbxmlParseHelper.getInstance()
                    .convertStringToBigDecimal(qtyStr, 4);
            detail.setOrderQty(qty);
            String barcode = EbxmlParseHelper
                    .getInstance()
                    .obtainValueFrom(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemIdentification.alternateItemIdentification");
            detail.setBarcode(barcode);
            String buyerItemCode = EbxmlParseHelper
                    .getInstance()
                    .obtainValueFrom(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemIdentification.additionalItemIdentification");
            detail.setBuyerItemCode(buyerItemCode);
            String brand = EbxmlParseHelper
                    .getInstance()
                    .obtainValueFrom(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradeItemDescription.brandName");
            detail.setBrand(brand);
            String itemDesc = EbxmlParseHelper
                    .getInstance()
                    .obtainValueFrom(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradeItemDescription.descriptionShort.description.text");
            detail.setItemDesc(itemDesc);
            String orderUom = EbxmlParseHelper
                    .getInstance()
                    .obtainValueFrom(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradingPartnerNeutralTradeItemInformation.packagingType.packagingTypeCode");
            detail.setOrderUom(orderUom);
            String itemCostStr = EbxmlParseHelper.getInstance()
                    .obtainValueFrom(document,
                            detailNode + "(" + i + ").netAmount.amount");
            BigDecimal itemCost = EbxmlParseHelper.getInstance()
                    .convertStringToBigDecimal(itemCostStr, 4);
            detail.setItemCost(itemCost);
            totalCost = totalCost.add(itemCost);
            detail.setCostDiscountAmount(BigDecimal.ZERO);
            detail.setNetUnitCost(detail.getUnitCost());
            detail.setNetPackCost(detail.getPackCost());
            detail.setFocQty(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, detailNode + "(" + i + ").freeQuantity", 0));

            String curr = EbxmlParseHelper.getInstance().obtainValueFrom(
                    document,
                    detailNode + "(" + i
                            + ").netAmount.amount.@currencyISOcode");
            PoDetailExtendedHolder detailEx = new PoDetailExtendedHolder();
            detailEx.setPoOid(detail.getPoOid());
            detailEx.setLineSeqNo(detail.getLineSeqNo());
            detailEx.setFieldName("itemPriceCurrency");
            detailEx.setFieldType("S");
            detailEx.setStringValue(curr);
            detailExtendeds.add(detailEx);

            List<Element> shiptoDetails = poItems.get(i).getChildren("shiptoDetails");
            if (shiptoDetails != null)
            {
                for (int m = 0; m < shiptoDetails.size(); m++)
                {
                    Element shiptoParty = shiptoDetails.get(m).getChild("shiptoParty");
                    Element deliveryQty = shiptoDetails.get(m).getChild("deliveryQuantity");
                    Element freeQuantity = shiptoDetails.get(m).getChild("freeQuantity");
                    
                    String additionalShipToCode = "";
                    Element additionalPartyIdentification = shiptoParty.getChild("additionalPartyIdentification");
                    if (additionalPartyIdentification == null)
                    {
                        additionalShipToCode = EbxmlParseHelper.getInstance().trimString(shiptoParty.getChild("alternatePartyIdentification").getText());
                    }
                    else
                    {
                        additionalShipToCode = EbxmlParseHelper.getInstance().trimString(additionalPartyIdentification.getText());
                    }
                    String shiptoCode = additionalShipToCode;
                    PoLocationHolder loc = null;
                    if (locMap.containsKey(shiptoCode))
                    {
                        loc = locMap.get(shiptoCode);
                    }
                    else
                    {
                        loc = new PoLocationHolder();
                        String locCtryCode = EbxmlParseHelper.getInstance().trimString(shiptoParty
                                .getChild("partyInformation")
                                .getChild("nameAndAddress")
                                .getChild("countryISOcode").getText());
                        String locName = EbxmlParseHelper.getInstance().trimString(shiptoParty
                                .getChild("partyInformation")
                                .getChild("nameAndAddress").getChild("name")
                                .getText());
                        loc.setPoOid(detail.getPoOid());
                        loc.setLineSeqNo(locMap.size() + 1);
                        loc.setLocationCode(shiptoCode);
                        loc.setLocationCtryCode(locCtryCode);
                        loc.setLocationName(locName);
                        locMap.put(shiptoCode, loc);
                    }
                    loc.trimAllString();
                    loc.setAllEmptyStringToNull();
                    String shipQtyStr = EbxmlParseHelper.getInstance().trimString(deliveryQty.getText());
                    BigDecimal shipQty = EbxmlParseHelper.getInstance()
                            .convertStringToBigDecimal(shipQtyStr, 4);
                    
                    BigDecimal focQty = null;
                    if (null == freeQuantity)
                    {
                        focQty = BigDecimal.ZERO;
                    }
                    else
                    {
                        String focQtyStr = EbxmlParseHelper.getInstance().trimString(freeQuantity.getText());
                        focQty = EbxmlParseHelper.getInstance()
                                .convertStringToBigDecimal(focQtyStr, 4);
                    }
                    

                    PoLocationDetailHolder locDetail = new PoLocationDetailHolder();
                    locDetail.setPoOid(detail.getPoOid());
                    locDetail.setLocationShipQty(shipQty);
                    locDetail.setLocationLineSeqNo(loc.getLineSeqNo());
                    locDetail.setDetailLineSeqNo(detail.getLineSeqNo());
                    locDetail.setLocationFocQty(focQty);
                    locDetails.add(locDetail);
                }
            }
            details.add(detail);
        }
        List<PoLocationHolder> locs = new ArrayList<PoLocationHolder>();
        for (Entry<String, PoLocationHolder> entry : locMap.entrySet())
        {
            locs.add(entry.getValue());
        }
        po.getPoHeader().setTotalCost(totalCost);
        po.getPoHeader().setNetCost(totalCost);
        po.setDetails(details);
        po.setLocations(locs);
        po.setLocationDetails(locDetails);
        po.setDetailExtendeds(detailExtendeds);
    }


    private void initPoHeaderExtended(Document document, PoHolder po)
    {
        List<PoHeaderExtendedHolder> headerExts = new ArrayList<PoHeaderExtendedHolder>();
        PoHeaderExtendedHolder glnExtend = new PoHeaderExtendedHolder();
        String gln = EbxmlParseHelper.getInstance().obtainValueFrom(document,
                "buyer.gln");
        glnExtend.setStringValue(gln);
        glnExtend.setPoOid(po.getPoHeader().getPoOid());
        glnExtend.setFieldType("S");
        glnExtend.setFieldName("buyerGln");
        headerExts.add(glnExtend);

        PoHeaderExtendedHolder telFaxExtend = new PoHeaderExtendedHolder();
        String telFax = EbxmlParseHelper
                .getInstance()
                .obtainValueFrom(document,
                        "seller.partyInformation.contact.communicationChannel(0).@communicationNumber");
        telFaxExtend.setStringValue(telFax);
        telFaxExtend.setPoOid(po.getPoHeader().getPoOid());
        telFaxExtend.setFieldType("S");
        telFaxExtend.setFieldName("contactTel");
        headerExts.add(telFaxExtend);

        PoHeaderExtendedHolder emailExtend = new PoHeaderExtendedHolder();
        String email = EbxmlParseHelper
                .getInstance()
                .obtainValueFrom(document,
                        "seller.partyInformation.contact.communicationChannel(1).@communicationNumber");
        emailExtend.setStringValue(email);
        emailExtend.setPoOid(po.getPoHeader().getPoOid());
        emailExtend.setFieldType("S");
        emailExtend.setFieldName("contactEmail");
        headerExts.add(emailExtend);
        
        po.setHeaderExtendeds(headerExts);
    }


    private void createNamespace(PoHolder po, Document document)
    {
        Namespace sanc = Namespace.getNamespace("sanc",
                "http://www.sanc.org.sg/schemas/ean");
        Element root = new Element("order", sanc);

        Namespace xsi = Namespace.getNamespace("xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        root.addNamespaceDeclaration(xsi);
        root.setAttribute("schemaLocation",
                "http://www.sanc.org.sg/schemas/ean Order.xsd", xsi);
        root.setAttribute("documentStatus", "COPY");
        root.setAttribute("creationDate", DateUtil.getInstance()
                .convertDateToString(po.getPoHeader().getPoDate())
                + "T00:00:00");
        root.setAttribute("TypeOfOrder", "MULTI_SHIP");

        document.setRootElement(root);
    }


    private void createHeader(PoHolder po, Document document)
    {
        if (null != po.getPoHeader().getDeliveryDateFrom())
        {
            JdomXmlHelper.getInstance().setValueToDoc(
                document,
                "movementDate",
                DateUtil.getInstance().convertDateToString(
                    po.getPoHeader().getDeliveryDateFrom())
                    + "T00:00:00");
            JdomXmlHelper.getInstance().setValueToDoc(document, "movementDateType",
                "REQUESTED_DELIVERY");
        }
        JdomXmlHelper.getInstance().setValueToDoc(document, "typedEntityIdentification.@entityType", "ORDER");
        JdomXmlHelper
                .getInstance()
                .setValueToDoc(
                        document,
                        "typedEntityIdentification.entityIdentification.uniqueCreatorIdentification",
                        po.getPoHeader().getPoNo());

        String gln = "", tel = "", email = "";

        List<PoHeaderExtendedHolder> headerExs = po.getHeaderExtendeds();
        if (headerExs != null)
        {
            for (PoHeaderExtendedHolder ex : headerExs)
            {
                if (ex.getFieldName().equals("buyerGln"))
                {
                    gln = EbxmlParseHelper.getInstance().convertValue(ex.getStringValue(), 13, "0");
                }
                else if (ex.getFieldName().equals("contactTel"))
                {
                    tel = EbxmlParseHelper.getInstance().convertValue(ex.getStringValue(), 1, "-");
                }
                else if (ex.getFieldName().equals("contactEmail"))
                {
                    email = EbxmlParseHelper.getInstance().convertValue(ex.getStringValue(), 1, "-");
                }
            }
        }
        gln = EbxmlParseHelper.getInstance().convertValue(gln, 13, "0");
        tel = EbxmlParseHelper.getInstance().convertValue(tel, 1, "-");
        email = EbxmlParseHelper.getInstance().convertValue(email, 1, "-");
        JdomXmlHelper
                .getInstance()
                .setValueToDoc(
                        document,
                        "typedEntityIdentification.entityIdentification.contentOwner.gln",
                        gln);
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.gln", gln);
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "buyer.partyInformation.partyRole", "BUYER");
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "buyer.partyInformation.nameAndAddress.countryISOcode",
                po.getPoHeader().getBuyerCtryCode());
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "buyer.partyInformation.nameAndAddress.languageOfTheParty",
                "EN");
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "buyer.partyInformation.nameAndAddress.name",
                po.getPoHeader().getBuyerName());

        JdomXmlHelper.getInstance().setValueToDoc(document,
                "seller.alternatePartyIdentification",
                po.getPoHeader().getSupplierCode());
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "seller.alternatePartyIdentification.@type",
                BUYER_ASSIGNED_IDENTIFIER_FOR_PARTY);
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "seller.partyInformation.partyRole", "SUPPLIER");
        JdomXmlHelper
                .getInstance()
                .setValueToDoc(
                        document,
                        "seller.partyInformation.contact.communicationChannel(0).@communicationChannelCode",
                        "TELEFAX");
        JdomXmlHelper
                .getInstance()
                .setValueToDoc(
                        document,
                        "seller.partyInformation.contact.communicationChannel(0).@communicationNumber",
                        tel);
        JdomXmlHelper
                .getInstance()
                .setValueToDoc(
                        document,
                        "seller.partyInformation.contact.communicationChannel(1).@communicationChannelCode",
                        "EMAIL");
        JdomXmlHelper
                .getInstance()
                .setValueToDoc(
                        document,
                        "seller.partyInformation.contact.communicationChannel(1).@communicationNumber",
                        email);
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.contact.personOrDepartmentName.description.@language", "en");
        JdomXmlHelper
                .getInstance()
                .setValueToDoc(
                        document,
                        "seller.partyInformation.contact.personOrDepartmentName.description.text",
                        "-");
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "seller.partyInformation.nameAndAddress.countryISOcode",
                EbxmlParseHelper.getInstance().convertValue(po.getPoHeader().getSupplierCtryCode(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "seller.partyInformation.nameAndAddress.languageOfTheParty",
                "EN");
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "seller.partyInformation.nameAndAddress.name",
                EbxmlParseHelper.getInstance().convertValue(po.getPoHeader().getSupplierName(), 1, "-"));
        
        this.createShipParty(po, document);
    }


    private void createShipParty(PoHolder po, Document document)
    {

        JdomXmlHelper.getInstance().setValueToDoc(document,
                "shipParty.@identificationType", "SHIP_TO");
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "shipParty.alternatePartyIdentification.@type",
                BUYER_ASSIGNED_IDENTIFIER_FOR_PARTY);
        List<PoLocationHolder> locations = po.getLocations();
        if (locations != null && locations.size() > 1)
        {
            JdomXmlHelper.getInstance().setValueToDoc(document,
                    "shipParty.alternatePartyIdentification",
                    "Ignore -- Multi Location Shipment");
        }
        else
        {
            JdomXmlHelper.getInstance().setValueToDoc(document,
                    "shipParty.alternatePartyIdentification",
                    EbxmlParseHelper.getInstance().convertValue(po.getPoHeader().getShipToCode(), 1, "-"));
        }
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "shipParty.partyInformation.partyRole", "CORPORATE_IDENTITY");
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "shipParty.partyInformation.nameAndAddress.countryISOcode",
                EbxmlParseHelper.getInstance().convertValue(po.getPoHeader().getShipToCtryCode(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "shipParty.partyInformation.nameAndAddress.languageOfTheParty",
                "EN");
        JdomXmlHelper.getInstance().setValueToDoc(document,
                "shipParty.partyInformation.nameAndAddress.name",
                EbxmlParseHelper.getInstance().convertValue(po.getPoHeader().getShipToName(), 1, "-"));
    }


    private void createDetails(PoHolder po, Document document) throws Exception
    {
        String detailNode = "lineItem";
        List<PoDetailHolder> details = po.getDetails();

        if (details == null || details.isEmpty())
        {
            return;
        }
        List<PoDetailExtendedHolder> detailExs = po.getDetailExtendeds();
        Map<Integer, List<PoDetailExtendedHolder>> dexsMap = convertDetailExToMap(detailExs);

        Map<Integer, PoLocationHolder> locsMap = convertLocsToMap(po
                .getLocations());
        Map<Integer, List<PoLocationDetailHolder>> locDetailMap = convertLocDetailsToMap(po
                .getLocationDetails());
        for (int i = 0; i < details.size(); i++)
        {
            PoDetailHolder detail = details.get(i);
            JdomXmlHelper.getInstance().setValueToDoc(document,
                    detailNode + "(" + i + ").@number", String.valueOf(i + 1));
            List<PoDetailExtendedHolder> exs = dexsMap.get(i + 1);
            String curr = "";
            if (exs != null)
            {
                for (PoDetailExtendedHolder ex : exs)
                {
                    if (ex.getFieldName().toLowerCase(Locale.getDefault())
                            .equals("itempricecurrency"))
                    {
                        curr = EbxmlParseHelper.getInstance().convertValue(ex.getStringValue(), 1, "-");
                    }
                }
            }
            JdomXmlHelper.getInstance()
                    .setValueToDoc(
                            document,
                            detailNode + "(" + i
                                    + ").netPrice.amount.@currencyISOcode",
                            curr);
            if (detail.getOrderBaseUnit().equals("U"))
            {
                JdomXmlHelper.getInstance().setValueToDoc(document,
                        detailNode + "(" + i + ").netPrice.amount",
                        detail.getUnitCost().toString());
            }
            else if (detail.getOrderBaseUnit().equals("P"))
            {
                JdomXmlHelper.getInstance().setValueToDoc(document,
                        detailNode + "(" + i + ").netPrice.amount",
                        detail.getPackCost().toString());
            }
            JdomXmlHelper.getInstance().setValueToDoc(
                document,
                detailNode + "(" + i + ").requestedQuantity",
                BigDecimalUtil.getInstance()
                    .convertBigDecimalToStringIntegerWithNoScale(
                        detail.getOrderQty(), 4));
            log.info("requestedQuantity : " +  BigDecimalUtil.getInstance()
                .convertBigDecimalToStringIntegerWithNoScale(
                    detail.getOrderQty(), 4));
            JdomXmlHelper
                    .getInstance()
                    .setValueToDoc(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemIdentification.alternateItemIdentification",
                            EbxmlParseHelper.getInstance().convertValue(detail.getBarcode(), 1, "-"));
            JdomXmlHelper
                    .getInstance()
                    .setValueToDoc(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemIdentification.additionalItemIdentification",
                            EbxmlParseHelper.getInstance().convertValue(detail.getBuyerItemCode(), 1, "-"));
            JdomXmlHelper
                    .getInstance()
                    .setValueToDoc(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradeItemDescription.brandName",
                                EbxmlParseHelper.getInstance().convertValue(detail.getBrand(), 1, "-"));
            JdomXmlHelper
                    .getInstance()
                    .setValueToDoc(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradeItemDescription.descriptionShort.description.@language",
                            "en");
            JdomXmlHelper
                    .getInstance()
                    .setValueToDoc(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradeItemDescription.descriptionShort.description.text",
                            detail.getItemDesc());
            JdomXmlHelper
                    .getInstance()
                    .setValueToDoc(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradingPartnerNeutralTradeItemInformation.packagingType.packagingTypeCode",
                            detail.getOrderUom());
            JdomXmlHelper
                    .getInstance()
                    .setValueToDoc(
                            document,
                            detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemHierarchy.quantityOfNextLevelTradeItemWithinInnerPack",
                                    detail.getPackingFactor().toBigInteger().toString());
            JdomXmlHelper.getInstance().setValueToDoc(
                    document,
                    detailNode + "(" + i
                            + ").netAmount.amount.@currencyISOcode", curr);
            JdomXmlHelper.getInstance().setValueToDoc(document,
                    detailNode + "(" + i + ").netAmount.amount",
                    detail.getItemCost().toString());
            JdomXmlHelper.getInstance().setValueToDoc(
                document,
                detailNode + "(" + i + ").freeQuantity",
                BigDecimalUtil.getInstance()
                    .convertBigDecimalToStringIntegerWithNoScale(
                        detail.getFocQty(), 4));

            // Ship to party
            List<PoLocationDetailHolder> locDetails = locDetailMap.get(detail
                    .getLineSeqNo());

            for (int j = 0; j < locDetails.size(); j++)
            {
                PoLocationDetailHolder locD = locDetails.get(j);
                PoLocationHolder loc = locsMap.get(locD.getLocationLineSeqNo());
                String node = detailNode + "(" + i
                        + ").shiptoDetails(" + j + ").shiptoParty";
                JdomXmlHelper.getInstance().setValueToDoc(document,
                        node + ".alternatePartyIdentification.@type",
                        BUYER_ASSIGNED_IDENTIFIER_FOR_PARTY);
                
                JdomXmlHelper.getInstance().setValueToDoc(document,
                        node + ".alternatePartyIdentification",
                        loc.getLocationCode());
               
                if (null != po.getPoHeader().getDeliveryDateFrom())
                {
                    JdomXmlHelper.getInstance().setValueToDoc(document,
                        node + ".partyInformation.partyDates.partyStartDate", DateUtil.getInstance().convertDateToString(po.getPoHeader().getDeliveryDateFrom()) + "T00:00:00");
                }
                if (null != po.getPoHeader().getDeliveryDateTo())
                {
                    JdomXmlHelper.getInstance().setValueToDoc(document,
                        node + ".partyInformation.partyDates.partyEndDate", DateUtil.getInstance().convertDateToString(po.getPoHeader().getDeliveryDateTo()) + "T00:00:00");
                }
                JdomXmlHelper.getInstance().setValueToDoc(document,
                        node + ".partyInformation.partyRole", "SHIP_TO");
                JdomXmlHelper
                        .getInstance()
                        .setValueToDoc(
                                document,
                                node
                                + ".partyInformation.nameAndAddress.countryISOcode",
                                EbxmlParseHelper.getInstance().convertValue(loc.getLocationCtryCode(), 1, "-"));
                JdomXmlHelper
                        .getInstance()
                        .setValueToDoc(
                                document,
                                node
                                        + ".partyInformation.nameAndAddress.languageOfTheParty",
                                "en");
                JdomXmlHelper.getInstance().setValueToDoc(document,
                        node + ".partyInformation.nameAndAddress.name",
                        EbxmlParseHelper.getInstance().convertValue(loc.getLocationName(), 1, "-"));
                JdomXmlHelper.getInstance().setValueToDoc(
                    document,
                    detailNode + "(" + i + ").shiptoDetails(" + j
                        + ").deliveryQuantity",
                    BigDecimalUtil.getInstance()
                        .convertBigDecimalToStringIntegerWithNoScale(
                            locD.getLocationShipQty(), 4));
                JdomXmlHelper.getInstance().setValueToDoc(
                    document,
                    detailNode + "(" + i + ").shiptoDetails(" + j
                        + ").freeQuantity",
                    BigDecimalUtil.getInstance()
                        .convertBigDecimalToStringIntegerWithNoScale(
                            locD.getLocationFocQty(), 4));
               
            }
        }
        JdomXmlHelper.getInstance().setValueToDoc(document, "totalLineItem",
                String.valueOf(details.size()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "remarks",
                po.getPoHeader().getOrderRemarks());
    }


    private Map<Integer, List<PoDetailExtendedHolder>> convertDetailExToMap(
            List<PoDetailExtendedHolder> detailExs)
    {
        Map<Integer, List<PoDetailExtendedHolder>> rlt = new HashMap<Integer, List<PoDetailExtendedHolder>>();

        if (detailExs != null)
        {
            for (PoDetailExtendedHolder ex : detailExs)
            {
                if (rlt.containsKey(ex.getLineSeqNo()))
                {
                    List<PoDetailExtendedHolder> exs = rlt.get(ex.getLineSeqNo());
                    exs.add(ex);
                }
                else
                {
                    List<PoDetailExtendedHolder> exs = new ArrayList<PoDetailExtendedHolder>();
                    exs.add(ex);
                    rlt.put(ex.getLineSeqNo(), exs);
                }
            }
        }
        return rlt;
    }


    private Map<Integer, List<PoLocationDetailHolder>> convertLocDetailsToMap(
            List<PoLocationDetailHolder> locDes)
    {
        Map<Integer, List<PoLocationDetailHolder>> rlt = new HashMap<Integer, List<PoLocationDetailHolder>>();

        for (PoLocationDetailHolder ld : locDes)
        {
            if (rlt.containsKey(ld.getDetailLineSeqNo()))
            {
                List<PoLocationDetailHolder> lds = rlt.get(ld
                        .getDetailLineSeqNo());
                lds.add(ld);
            }
            else
            {
                List<PoLocationDetailHolder> lds = new ArrayList<PoLocationDetailHolder>();
                lds.add(ld);
                rlt.put(ld.getDetailLineSeqNo(), lds);
            }
        }
        return rlt;
    }


    private Map<Integer, PoLocationHolder> convertLocsToMap(
            List<PoLocationHolder> locs)
    {
        Map<Integer, PoLocationHolder> rlt = new HashMap<Integer, PoLocationHolder>();

        for (PoLocationHolder loc : locs)
        {
            rlt.put(loc.getLineSeqNo(), loc);
        }
        return rlt;
    }


    @Override
    protected String getXSDSchema()
    {
        return "xsd/ebxml/po/Order.xsd";
    }

    
//    public static void main(String[] args) throws Exception
//    {
//        PoDocFileHandler handler = new PoDocFileHandler();
//        PoDocMsg docMsg = new PoDocMsg();
//        handler.readFileContent(docMsg, new File("E:/PO_FP_78928_26627410.xml"));
//        handler.createFile(docMsg.getData(), new File("d:/po.xml"), "ebxml");
//    }
    
    
}
