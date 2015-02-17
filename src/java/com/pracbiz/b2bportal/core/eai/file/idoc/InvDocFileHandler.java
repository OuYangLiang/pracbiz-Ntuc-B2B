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

package com.pracbiz.b2bportal.core.eai.file.idoc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.JdomXmlHelper;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.PoHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author yinchi
 */
public class InvDocFileHandler extends DefaultDocFileHandler<InvDocMsg, InvHolder>
        implements CoreCommonConstants
{
    @Autowired transient private BuyerStoreService buyerStoreService;
    @Autowired transient private SupplierService supplierService;
    @Autowired transient private PoHeaderExtendedService poHeaderExtendedSerivce;
    @Autowired transient private PoHeaderService poHeaderService;
    
    @Override
    protected String processFormat()
    {
        return FP_IDOC;
    }


    @Override
    public void readFileContent(InvDocMsg docMsg, byte[] input)
            throws Exception
    {
        
    }


    @Override
    public String getTargetFilename(InvHolder data, String expectedFormat)
    {
        if (expectedFormat != null
                && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }

        return MsgType.INV + DOC_FILENAME_DELIMITOR
                + data.getHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
                + data.getHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(data.getHeader().getInvNo()) + DOC_FILENAME_DELIMITOR
                + data.getHeader().getInvOid() + ".xml";
    }


    @Override
    protected String translate(InvDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
                this.getTargetFilename(docMsg.getData(), FP_IDOC));
        this.createFile(docMsg.getData(), targetFile, FP_IDOC);

        return targetFile.getName();
    }


    @Override
    public byte[] getFileByte(InvHolder data, File targetFile, String expectedFormat)
            throws Exception
    {   
        InvHolder invHolder = data;
        Document document = new Document();
        // HDR
        if (invHolder.getHeader() != null)
        {
            this.createNamespace(document);
            this.createHeader(invHolder, document);

        }


        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        format.setExpandEmptyElements(true);
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

    private void createNamespace(Document document)
    {
        Element root = new Element("INVOIC01");

        document.setRootElement(root);
    }


    private void createHeader(InvHolder inv, Document document) throws Exception
    {
        InvHeaderHolder header = inv.getHeader();
        
        String buyerCode = "";
        if (header.getBuyerCode().toUpperCase().contains("FP"))
        {
            buyerCode = "FP10";
        }
        else if (header.getBuyerCode().toUpperCase().contains("CH"))
        {
            buyerCode = "CH10";
        }
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.@BEGIN", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.TABNAM", "EDI_DC40");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.MANDT", "");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.DOCNUM", "");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.DOCREL", "");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.STATUS", "");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.DIRECT", "2");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.OUTMOD", "2");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.IDOCTYP", "INVOIC01");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.MESTYP", "INVOIC");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.STDMES", "INVOIC");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.SNDPOR", "XML_PLU");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.SNDPRT", "LS");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.SNDPFC", "LF");
        
        PoHeaderHolder poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(header.getBuyerOid(), header.getPoNo(), header.getSupplierCode());
        List<PoHeaderExtendedHolder> headerExs = poHeaderExtendedSerivce.selectHeaderExtendedByKey(poHeader.getPoOid());
        String sndprn = "";
        if (headerExs != null && !headerExs.isEmpty())
        {
            for (PoHeaderExtendedHolder extended : headerExs)
            {
                if ("RCVPRN".equals(extended.getFieldName()) && null != extended.getStringValue())
                {
                    sndprn = extended.getStringValue();
                    break;
                }
            }
        }
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.SNDPRN", sndprn);
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.RCVPOR", "SAP_TEC");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.RCVPRT", "LS");
        
        String rcvprn = "";
        if (headerExs != null && !headerExs.isEmpty())
        {
            for (PoHeaderExtendedHolder extended : headerExs)
            {
                if ("SNDPRN".equals(extended.getFieldName()) && null != extended.getStringValue())
                {
                    rcvprn = extended.getStringValue();
                    break;
                }
            }
        }
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.RCVPRN", rcvprn);
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.CREDAT", "");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.CRETIM", "");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.SERIAL", "");
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.CURCY", "SGD");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.ZTERM", "T17N");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.BSART", "INVO");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.BELNR", header.getInvNo());
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(0).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(0).PARVW", "RS");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(0).PARTN", formatSupplierCode(header.getSupplierCode()));
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(0).PAORG", buyerCode);
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(1).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(1).PARVW", "RE");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(1).PARTN", buyerCode);
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(1).PAORG", buyerCode);
        
        List<BuyerStoreHolder> allBuyerStores = buyerStoreService.selectBuyerStoresByBuyer(header.getBuyerCode());
        Map<String, BuyerStoreHolder> map = this.convertStoreListToMapUseStoreCodeAsKey(allBuyerStores);
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(2).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(2).PARVW", "WE");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(2).NAME1", header.getShipToName());
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(2).KNREF", this.mergeLocationCode(header.getShipToCode(), map));
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(3).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(3).PARVW", "BK");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(3).PARTN", buyerCode);
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(3).PAORG", buyerCode);
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(0).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(0).QUALF", "009");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(0).BELNR", header.getInvNo());
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(1).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(1).QUALF", "087");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(1).BELNR", header.getInvNo());
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(0).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(0).IDDAT", "012");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(0).DATUM", DateUtil.getInstance().convertDateToString(header.getInvDate(), DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC).substring(0, 8));
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(1).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(1).IDDAT", "024");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(1).DATUM", DateUtil.getInstance().convertDateToString(header.getInvDate(), DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC).substring(0, 8));
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK04.@SEGMENT", "1");
        SupplierHolder supplier = supplierService.selectSupplierByKey(header.getSupplierOid());
        if (null == supplier.getGstPercent())
        {
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK04.MWSKZ", "P0");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK04.MSATZ", "");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK04.MWSBT", "");
        }
        else
        {
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK04.MWSKZ", "P7");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK04.MSATZ", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(header.getVatRate(), 3));
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK04.MWSBT", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(header.getVatAmount(), 2));
        }
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK14.@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK14.QUALF", "011");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK14.ORGID", buyerCode);
        
        if (inv.getDetails() != null)
        {
            createDetails(inv, document);
        }
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01(0).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01(0).SUMID", "011");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01(0).SUMME", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(header.getInvAmountWithVat(), 2));
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01(0).WAERQ", "SGD");
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01(1).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01(1).SUMID", "012");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01(1).SUMME", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(header.getCashDiscountAmount(), 2));
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01(1).WAERQ", "SGD");
        
    }
    
    
    private void createDetails(InvHolder inv, Document document)
    {
        List<InvDetailHolder> details = inv.getDetails();
        for (int i = 0; i < details.size(); i++)
        {
            InvDetailHolder detail = details.get(i);
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").POSEX", formatLineSeqNo(String.valueOf(detail.getLineSeqNo() * 10)));
            if (null != detail.getNetPrice() && detail.getNetPrice().compareTo(BigDecimal.ZERO) != 0)
            {
                JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").MENGE", String.valueOf(detail.getInvQty()));
            }
            else
            {
                JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").MENGE", String.valueOf(detail.getFocQty()));
            }
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").MENEE", String.valueOf(detail.getInvUom()));
            if (null != detail.getNetPrice() && detail.getNetPrice().compareTo(BigDecimal.ZERO) != 0)
            {
                JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").BMNG2", String.valueOf(detail.getInvQty()));
            }
            else
            {
                JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").MENGE", String.valueOf(detail.getFocQty()));
            }
            if (null != detail.getNetPrice() && detail.getNetPrice().compareTo(BigDecimal.ZERO) != 0)
            {
                JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").VPREI", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getNetPrice(), 2));
            }
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").NETWR", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getNetAmount(), 2));
            
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP02.@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP02.QUALF", "001");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP02.BELNR", inv.getHeader().getPoNo());
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP02.ZEILE", detail.getLineRefNo() != null ? detail.getLineRefNo() : formatLineSeqNo(String.valueOf(detail.getLineSeqNo() * 10)));
            
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(0).@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(0).QUALF", "003");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(0).BETRG", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getNetAmount(), 2));
            
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(1).@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(1).QUALF", "005");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(1).BETRG", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getNetAmount(), 2));
            
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(2).@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(2).QUALF", "002");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(2).BETRG", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getItemGrossAmount(), 2));
        }
    }
    

    @Override
    protected String getXSDSchema()
    {
        return "xsd/idoc/inv/Idocs.xsd";
    }
    
    
    private Map<String, BuyerStoreHolder> convertStoreListToMapUseStoreCodeAsKey(List<BuyerStoreHolder> list)
    {
        if (list == null || list.isEmpty())
        {
            return null;
        }
        Map<String, BuyerStoreHolder> map = new HashMap<String, BuyerStoreHolder>();
        for (BuyerStoreHolder store : list)
        {
            map.put(store.getStoreCode().toUpperCase(), store);
        }
        return map;
    }
    
    
    private String mergeLocationCode(String locationCode, Map<String, BuyerStoreHolder> map)
    {
        String result = locationCode;
        if (locationCode == null || map == null || map.isEmpty() || !map.containsKey(locationCode.toUpperCase(Locale.US)))
        {
            return result;
        }
        BuyerStoreHolder store = map.get(locationCode.toUpperCase(Locale.US));
        return (store.getIsWareHouse() ? "WH" : "ST") + result;
    }
    
    
    private String formatLineSeqNo(String seq)
    {
        if (seq.length() > 5)
        {
            return seq;
        }
        
        StringBuffer value = new StringBuffer();
        for (int i = 0; i < (6 - seq.length()); i++)
        {
            value.append("0");
        }
        
        return value.append(seq).toString();
    }
    
    
    private String formatSupplierCode(String supplierCode)
    {
        if (supplierCode.length() > 10)
        {
            return supplierCode;
        }
        
        StringBuffer value = new StringBuffer();
        for (int i = 0; i < (10 - supplierCode.length()); i++)
        {
            value.append("0");
        }
        
        return value.append(supplierCode).toString();
    }
    
    
//    public static void main(String[] args) throws Exception
//    {
//        
//        InvDocFileHandler handler = new InvDocFileHandler();
//        InvDocMsg docMsg = new InvDocMsg();
//        InvHeaderHolder header = new InvHeaderHolder();
//        header.setInvDate(new Date());
//        header.setInvNo("001");
//        header.setSupplierCode("1004");
//        header.setSupplierName("SINGAPORE POOLS (PTE) LTD");
//        header.setBuyerCode("FP");
//        header.setShipToCode("1");
//        header.setPoNo("001");
//        header.setVatRate(new BigDecimal(7));
//        header.setVatAmount(new BigDecimal(850));
//        header.setInvAmountWithVat(new BigDecimal(666.33));
//        
//        InvDetailHolder detail = new InvDetailHolder();
//        detail.setLineSeqNo(1);
//        detail.setInvQty(new BigDecimal(20));
//        detail.setNetPrice(new BigDecimal(20));
//        detail.setNetAmount(new BigDecimal(600));
//        detail.setItemGrossAmount(new BigDecimal(0));
//        
//        InvDetailHolder detail1 = new InvDetailHolder();
//        detail1.setLineSeqNo(2);
//        detail1.setInvQty(new BigDecimal(10));
//        detail1.setNetPrice(new BigDecimal(1));
//        detail1.setNetAmount(new BigDecimal(50));
//        detail1.setItemGrossAmount(new BigDecimal(0));
//        
//        InvDetailHolder detail2 = new InvDetailHolder();
//        detail2.setLineSeqNo(5);
//        detail2.setInvQty(new BigDecimal(20));
//        detail2.setNetPrice(new BigDecimal(20));
//        detail2.setNetAmount(new BigDecimal(200));
//        detail2.setItemGrossAmount(new BigDecimal(0));
//        
//        List<InvDetailHolder> details = new ArrayList<InvDetailHolder>();
//        details.add(detail);
//        details.add(detail1);
//        details.add(detail2);
//        
//        InvHolder inv = new InvHolder();
//        inv.setHeader(header);
//        inv.setDetails(details);
//        docMsg.setData(inv);
//        
//        handler.createFile(docMsg.getData(), new File("d:/inv.xml"), "idoc");
//    }
    
    
}
