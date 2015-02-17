package com.pracbiz.b2bportal.core.eai.file.ebxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EbxmlParseHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.JdomXmlHelper;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.InvType;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class InvDocFileHandler extends DefaultDocFileHandler<InvDocMsg, InvHolder> implements
        CoreCommonConstants
{
    private final static Logger log = LoggerFactory.getLogger(InvDocFileHandler.class);
    @Autowired private transient PoHeaderService poHeaderService;
    
    @Override
    public String getTargetFilename(InvHolder data, String expectedFormat)
    {
        if(expectedFormat != null
                && !processFormat().equalsIgnoreCase(expectedFormat))
            {
                return this.successor.getTargetFilename(data, expectedFormat);
            }
            
            return MsgType.INV + DOC_FILENAME_DELIMITOR
                + data.getHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
                + data.getHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(data.getHeader().getInvNo()) + DOC_FILENAME_DELIMITOR + data.getHeader().getInvOid()
                + ".xml";
    }

    @Override
    protected String processFormat()
    {
        return EBXML;
    }

    @Override
    public void readFileContent(InvDocMsg docMsg, byte[] input)
            throws Exception
    {
        log.info(" :::: start to read ebxml Invoice source file.");
        Document document = FileParserUtil.getInstance().build(input);
        InvHolder inv = new InvHolder();
        initHeader(document, docMsg, inv);
        initDetailsAndOthers(document, inv);
        docMsg.setData(inv);
        log.info(" :::: end to read ebxml Invoice source file.");
    }

    @Override
    protected String translate(InvDocMsg docMsg)
        throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
                this.getTargetFilename(docMsg.getData(), null));
        this.createFile(docMsg.getData(), targetFile, null);

        return targetFile.getName();
    }

    
    @Override
    public byte[] getFileByte(InvHolder data, File targetFile,
        String expectedFormat) throws Exception
    {
        InvHolder inv = data;
        Document document = new Document();
        
        if (inv.getHeader() != null)
        {
            this.createNamespace(inv, document);
            this.createHeader(inv, document);
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
    
    
    
    public void initHeader(Document document, DocMsg docMsg, InvHolder inv) throws Exception
    {
        InvHeaderHolder header = new InvHeaderHolder();
        header.setInvNo(EbxmlParseHelper.getInstance().obtainValueFrom(document, "requestForPaymentIdentification.uniqueCreatorIdentification"));
        header.setDocAction("A");
        String creationDate = document.getRootElement().getAttributeValue("creationDate");
        header.setActionDate(EbxmlParseHelper.getInstance().convertStringToDate(creationDate, EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS));
        header.setInvDate(EbxmlParseHelper.getInstance().convertStringToDate(creationDate, EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS));
        header.setPoNo(EbxmlParseHelper.getInstance().obtainValueFrom(document, "orderIdentification.referenceIdentification"));
        header.setPoDate(EbxmlParseHelper.getInstance().obtainDateFrom(document, "orderIdentification.referenceDateOnly", null));
        header.setDeliveryDate(EbxmlParseHelper.getInstance().obtainDateFrom(document, "deliveryNote.referenceDateOnly", null));
        header.setBuyerName(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.name"));
        header.setBuyerAddr1(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.streetAddressOne"));
        header.setBuyerAddr2(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.streetAddressTwo"));
        header.setBuyerAddr3(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.streetAddressThree"));
        header.setBuyerAddr4(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.streetAddressFour"));
        header.setBuyerCity(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.city"));
        header.setBuyerState(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.state"));
        header.setBuyerCtryCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.countryISOCode"));
        header.setBuyerPostalCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.postalCode"));
        header.setSupplierName(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.name"));
        header.setSupplierAddr1(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.streetAddressOne"));
        header.setSupplierAddr2(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.streetAddressTwo"));
        header.setSupplierAddr3(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.streetAddressThree"));
        header.setSupplierAddr4(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.streetAddressFour"));
        header.setSupplierCity(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.city"));
        header.setSupplierState(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.state"));
        header.setSupplierCtryCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.countryISOCode"));
        header.setSupplierPostalCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.postalCode"));
        header.setSupplierBizRegNo(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.bizRegNo"));
        header.setSupplierVatRegNo(EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.partyTaxInformation.taxRegistrationNumber"));
        header.setShipToCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.store.@code"));
        header.setShipToName(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.store"));
        header.setShipToAddr1(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.address1"));
        header.setShipToAddr2(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.address2"));
        header.setShipToAddr3(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.address3"));
        header.setShipToAddr4(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.address4"));
        header.setShipToCity(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.city"));
        header.setShipToState(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.state"));
        header.setShipToCtryCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.ctryCode"));
        header.setShipToPostalCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.postalCode"));
        header.setPayTermCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.creditTerms.@code"));
        header.setPayTermDesc(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.creditTerms"));
        header.setPayInstruct(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.payInstruct"));
        header.setAdditionalDiscountAmount(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "allowanceCharge.monetaryAmountOrPercentage.amount", 2));
        header.setAdditionalDiscountPercent(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "extension.discountPercent", 2));
        header.setInvAmountNoVat(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "netAmount.amount", 2));
        header.setVatAmount(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "seller.partyInformation.partyTaxInformation.taxAmount.amount", 2));
        header.setInvAmountWithVat(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "totalAmount.amount", 2));
        header.setVatRate(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "seller.partyInformation.partyTaxInformation.taxPercent", 2));
        header.setInvRemarks(EbxmlParseHelper.getInstance().obtainValueFrom(document, "remarks"));
        header.setFooterLine1(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.footer.line[number=1]"));
        header.setFooterLine2(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.footer.line[number=2]"));
        header.setFooterLine3(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.footer.line[number=3]"));
        header.setFooterLine4(EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.footer.line[number=4]"));
        header.setInvStatus(InvStatus.NEW);
        header.setInvOid(docMsg.getDocOid());
        header.setSupplierCode(docMsg.getSenderCode());
        header.setSupplierOid(docMsg.getSenderOid());
        header.setBuyerCode(docMsg.getReceiverCode());
        header.setBuyerOid(docMsg.getReceiverOid());
        String invType = EbxmlParseHelper.getInstance().obtainValueFrom(document, "invoiceType");
        try
        {
            header.setInvType(InvType.valueOf(invType));
        }
        catch (Exception e)
        {
            List<PoHeaderHolder> poHeaders = poHeaderService
                    .selectPoHeadersByPoNoBuyerCodeAndSupplierCode(header.getPoNo(), header.getBuyerCode(), header.getSupplierCode());
            if (poHeaders != null && !poHeaders.isEmpty())
            {
                PoHeaderHolder poHeader = poHeaders.get(0);
                header.setInvType(InvType.valueOf(poHeader.getPoType().name()));
            }
        }
        inv.setHeader(header);
    }
    
    
    public void initDetailsAndOthers(Document document, InvHolder inv)
    {
        List<Element> lineItems = document.getRootElement().getChildren("lineItem");
        List<InvDetailHolder> detailList = new ArrayList<InvDetailHolder>();
        for (int i = 0; i < lineItems.size(); i++)
        {
            InvDetailHolder detail = new InvDetailHolder();
            detail.setLineSeqNo(i + 1);
            detail.setInvOid(inv.getHeader().getInvOid());
            detail.setBuyerItemCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").tradeItemIdentification.alternateTradeItemIdentification[type=BUYER_ASSIGNED].value"));
            detail.setSupplierItemCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification[type=SUPPLIER_ASSIGNED].value"));
            detail.setBarcode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification[type=BUYER_ASSIGNED].value"));
            detail.setItemDesc(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradeItemDescription.descriptionShort.description.text"));
            detail.setBrand(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradeItemDescription.brandName"));
            detail.setColourCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorCodeValue"));
            detail.setColourDesc(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorDescription.description.text"));
            detail.setSizeCode(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.sizeCodeValue"));
            detail.setSizeDesc(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.descriptiveSize.description.text"));
            detail.setPackingFactor(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemHierarchy.quantityOfInnerPack", 2));
            detail.setInvBaseUnit(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.baseUnit"));
            detail.setInvUom(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.uom"));
            detail.setInvQty(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").invoicedQuantity", 2));
            detail.setFocBaseUnit(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.focBaseUnit"));
            detail.setFocUom(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.focUom"));
            detail.setFocQty(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").shiptoDetails.freeQuantity", 2));
            detail.setUnitPrice(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").unitPrice.amount", 2));
            detail.setDiscountAmount(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").extension.discountAmount.amount", 2));
            detail.setDiscountPercent(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").extension.discountPercent.amount", 2));
            detail.setNetPrice(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").netPrice.amount", 2));
            detail.setItemAmount(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").totalLineAmount.amount", 2));
            detail.setNetAmount(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").extension.netAmount.amount", 2));
            detail.setItemSharedCost(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").extension.itemSharedCost.amount", 2));
            detail.setItemGrossAmount(EbxmlParseHelper.getInstance().obtainBigDecimalFrom(document, "lineItem(" + i + ").extension.itemGrossAmount.amount", 2));
            detail.setItemRemarks(EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.itemRemarks"));
            detailList.add(detail);
        }
        inv.setDetails(detailList);
    }

    
    private void createNamespace(InvHolder inv, Document document)
    {
        Namespace sanc = Namespace.getNamespace("sanc",
                "http://www.sanc.org.sg/schemas/ean");
        Element root = new Element("invoice", sanc);

        Namespace xsi = Namespace.getNamespace("xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        root.addNamespaceDeclaration(xsi);
        root.setAttribute("schemaLocation",
                "http://www.sanc.org.sg/schemas/ean Invoice.xsd", xsi);
        root.setAttribute("documentStatus", "COPY");
        root.setAttribute("creationDate", DateUtil.getInstance()
                .convertDateToString(inv.getHeader().getInvDate()) + "T00:00:00");

        document.setRootElement(root);
    }


    private void createHeader(InvHolder inv, Document document)
    {
        InvHeaderHolder header = inv.getHeader();
        JdomXmlHelper.getInstance().setValueToDoc(document, "invoiceType", header.getInvType().name());
        JdomXmlHelper.getInstance().setValueToDoc(document, "requestForPaymentIdentification.uniqueCreatorIdentification", header.getInvNo());
        JdomXmlHelper.getInstance().setValueToDoc(document, "requestForPaymentIdentification.contentOwner.alternatePartyIdentification", "-");
        JdomXmlHelper.getInstance().setValueToDoc(document, "requestForPaymentIdentification.contentOwner.alternatePartyIdentification.@type", "SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
        createBuyer(inv, document);
        createSupplier(inv, document);
        createLineItems(inv, document);
        JdomXmlHelper.getInstance().setValueToDoc(document, "allowanceCharge.monetaryAmountOrPercentage.amount", String.valueOf(header.getAdditionalDiscountAmount()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "allowanceCharge.@allowanceChargeType", "ALLOWANCE_GLOBAL");
        JdomXmlHelper.getInstance().setValueToDoc(document, "allowanceCharge.@allowanceOrChargeType", "ALLOWANCE");
        JdomXmlHelper.getInstance().setValueToDoc(document, "allowanceCharge.@settlementType", "OFF_INVOICE");
        JdomXmlHelper.getInstance().setValueToDoc(document, "allowanceCharge.monetaryAmountOrPercentage.amount.@currencyISOCode", "---");
        JdomXmlHelper.getInstance().setValueToDoc(document, "orderIdentification.referenceDateOnly", DateUtil.getInstance().convertDateToString(header.getPoDate()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "orderIdentification.referenceIdentification", header.getPoNo());
        JdomXmlHelper.getInstance().setValueToDoc(document, "deliveryNote.referenceDateOnly", DateUtil.getInstance().convertDateToString(header.getDeliveryDate()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "deliveryNote.referenceIdentification", "-");
        JdomXmlHelper.getInstance().setValueToDoc(document, "netAmount.amount.@currencyISOCode", "---");
        JdomXmlHelper.getInstance().setValueToDoc(document, "netAmount.amount", String.valueOf(header.getInvAmountNoVat()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "totalAmount.amount.@currencyISOCode", "---");
        JdomXmlHelper.getInstance().setValueToDoc(document, "totalAmount.amount", String.valueOf(header.getInvAmountWithVat()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "remarks", header.getInvRemarks() == null ? "" : header.getInvRemarks());
        createExtension(inv, document);
    }
    
    
    private void createLineItems(InvHolder inv, Document document)
    {
        List<InvDetailHolder> details = inv.getDetails();
        for (int i = 0; i < details.size(); i++)
        {
            InvDetailHolder detail = details.get(i);
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").@number", String.valueOf(i + 1));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").tradeItemIdentification.alternateTradeItemIdentification.@type", "BUYER_ASSIGNED");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").tradeItemIdentification.alternateTradeItemIdentification.value", EbxmlParseHelper.getInstance().convertValue(detail.getBuyerItemCode(), 1, "-"));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification(0).@type", "BUYER_ASSIGNED");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification(0).value", EbxmlParseHelper.getInstance().convertValue(detail.getBarcode(), 1, "-"));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification(1).@type", "SUPPLIER_ASSIGNED");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification(1).value", EbxmlParseHelper.getInstance().convertValue(detail.getSupplierItemCode(), 1, "-"));
            createItemInformation(detail, document, i);
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").invoicedQuantity", String.valueOf(detail.getInvQty()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").unitPrice.amount", String.valueOf(detail.getUnitPrice()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").unitPrice.amount.@currencyISOCode", "---");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").netPrice.amount", String.valueOf(detail.getNetPrice()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").netPrice.amount.@currencyISOCode", "---");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").totalLineAmount.amount", String.valueOf(detail.getItemAmount()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").totalLineAmount.amount.@currencyISOCode", "---");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.uom", detail.getInvUom());
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.baseUnit", detail.getInvBaseUnit());
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.focBaseUnit", detail.getFocBaseUnit());
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.focUom", detail.getFocUom());
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.discountAmount.amount", String.valueOf(detail.getDiscountAmount()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.discountAmount.amount.@currencyISOCode", "---");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.discountPercent.amount", String.valueOf(detail.getDiscountPercent()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.discountPercent.amount.@currencyISOCode", "---");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.netAmount.amount", String.valueOf(detail.getNetAmount()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.netAmount.amount.@currencyISOCode", "---");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.itemSharedCost.amount", String.valueOf(detail.getItemSharedCost()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.itemSharedCost.amount.@currencyISOCode", "---");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.itemGrossAmount.amount", String.valueOf(detail.getItemGrossAmount()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.itemGrossAmount.amount.@currencyISOCode", "---");
            JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").extension.itemRemarks", detail.getItemRemarks());
        }
    }
    
    
    private void createItemInformation(InvDetailHolder detail, Document document, int i)
    {
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradeItemDescription.brandName", EbxmlParseHelper.getInstance().convertValue(detail.getBrand(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradeItemDescription.descriptionShort.description.@language", "en");
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradeItemDescription.descriptionShort.description.text", EbxmlParseHelper.getInstance().convertValue(detail.getItemDesc(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorCodeListAgency", String.valueOf(6));
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorCodeValue", EbxmlParseHelper.getInstance().convertValue(detail.getColourCode(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorDescription.description.@language", "en");
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorDescription.description.text", EbxmlParseHelper.getInstance().convertValue(detail.getColourDesc(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemHierarchy.quantityOfInnerPack", String.valueOf(detail.getPackingFactor()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.sizeCodeListAgency", String.valueOf(2));
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.sizeCodeValue", EbxmlParseHelper.getInstance().convertValue(detail.getSizeCode(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.descriptiveSize.description.@language", "en");
        JdomXmlHelper.getInstance().setValueToDoc(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.descriptiveSize.description.text", EbxmlParseHelper.getInstance().convertValue(detail.getSizeDesc(), 1, "-"));
    }

    
    
    @Override
    protected String getXSDSchema()
    {
        return "xsd/ebxml/inv/Invoice.xsd";
    }


    private void createBuyer(InvHolder inv, Document document)
    {
        InvHeaderHolder header = inv.getHeader();
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.gln", "0000000000000");
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.additionalPartyIdentification", header.getBuyerCode());
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.additionalPartyIdentification.@type", "SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.partyRole", "BUYER");
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.city", EbxmlParseHelper.getInstance().convertValue(header.getBuyerCity(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.countryISOCode", EbxmlParseHelper.getInstance().convertValue(header.getBuyerCtryCode(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.languageOfTheParty", "en");
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.name", header.getBuyerName());
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.postalCode", EbxmlParseHelper.getInstance().convertValue(header.getBuyerPostalCode(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.state", EbxmlParseHelper.getInstance().convertValue(header.getBuyerState(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.streetAddressOne", EbxmlParseHelper.getInstance().convertValue(header.getBuyerAddr1(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.streetAddressTwo", EbxmlParseHelper.getInstance().convertValue(header.getBuyerAddr2(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.streetAddressThree", EbxmlParseHelper.getInstance().convertValue(header.getBuyerAddr3(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "buyer.partyInformation.nameAndAddress.streetAddressFour", EbxmlParseHelper.getInstance().convertValue(header.getBuyerAddr4(), 1, "-"));
    }
    
    
    private void createSupplier(InvHolder inv, Document document)
    {
        InvHeaderHolder header = inv.getHeader();
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.alternatePartyIdentification", header.getSupplierCode());
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.alternatePartyIdentification.@type", "BUYER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.partyTaxInformation.taxAmount.amount", header.getVatAmount() == null ? "0.00" : String.valueOf(header.getVatAmount()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.partyTaxInformation.taxAmount.amount.@currencyISOCode", "---");
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.partyTaxInformation.taxAuthority", "-");
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.partyTaxInformation.taxPercent", header.getVatRate() == null ? "0.00" : String.valueOf(header.getVatRate()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.partyTaxInformation.taxRegistrationNumber", EbxmlParseHelper.getInstance().convertValue(header.getSupplierVatRegNo(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.partyTaxInformation.typeOfTaxRegistration", "-");
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.partyRole", "SELLER");
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.city", EbxmlParseHelper.getInstance().convertValue(header.getSupplierCity(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.countryISOCode", EbxmlParseHelper.getInstance().convertValue(header.getSupplierCtryCode(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.languageOfTheParty", "en");
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.name", header.getSupplierName());
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.postalCode", EbxmlParseHelper.getInstance().convertValue(header.getSupplierPostalCode(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.state", EbxmlParseHelper.getInstance().convertValue(header.getSupplierState(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.streetAddressOne", EbxmlParseHelper.getInstance().convertValue(header.getSupplierAddr1(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.streetAddressTwo", EbxmlParseHelper.getInstance().convertValue(header.getSupplierAddr2(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.streetAddressThree", EbxmlParseHelper.getInstance().convertValue(header.getSupplierAddr3(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "seller.partyInformation.nameAndAddress.streetAddressFour", EbxmlParseHelper.getInstance().convertValue(header.getSupplierAddr4(), 1, "-"));
    }

    
    private void createExtension(InvHolder inv, Document document)
    {
        InvHeaderHolder header = inv.getHeader();
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.store.@code", header.getShipToCode());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.store", header.getShipToName());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.creditTerms.@code", header.getPayTermCode() == null ? "" : header.getPayTermCode());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.creditTerms", header.getPayTermDesc());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.footer.line(0).@number", String.valueOf(1));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.footer.line(0)", EbxmlParseHelper.getInstance().convertValue(header.getFooterLine1(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.footer.line(1).@number", String.valueOf(2));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.footer.line(1)", EbxmlParseHelper.getInstance().convertValue(header.getFooterLine2(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.footer.line(2).@number", String.valueOf(3));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.footer.line(2)", EbxmlParseHelper.getInstance().convertValue(header.getFooterLine3(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.footer.line(3).@number", String.valueOf(4));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.footer.line(3)", EbxmlParseHelper.getInstance().convertValue(header.getFooterLine4(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.bizRegNo", EbxmlParseHelper.getInstance().convertValue(header.getSupplierBizRegNo(), 1, "-"));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.payInstruct", header.getPayInstruct());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.discountPercent.amount", String.valueOf(header.getAdditionalDiscountPercent()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.discountPercent.amount.@currencyISOCode", "---");
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.storeInformation.address1", header.getShipToAddr1());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.storeInformation.address2", header.getShipToAddr2());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.storeInformation.address3", header.getShipToAddr3());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.storeInformation.address4", header.getShipToAddr4());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.storeInformation.city", header.getShipToCity());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.storeInformation.state", header.getShipToState());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.storeInformation.ctryCode", header.getShipToCtryCode());
        JdomXmlHelper.getInstance().setValueToDoc(document, "extension.storeInformation.postalCode", header.getShipToPostalCode());
    }

}
