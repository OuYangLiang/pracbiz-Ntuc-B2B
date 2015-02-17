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
import com.pracbiz.b2bportal.core.eai.message.outbound.DnDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.RtvHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author yinchi
 */
public class DnDocFileHandler extends DefaultDocFileHandler<DnDocMsg, DnHolder>
        implements CoreCommonConstants
{

    @Autowired transient private BuyerStoreService buyerStoreService;
    @Autowired transient private SupplierService supplierService;
    @Autowired transient private RtvHeaderExtendedService rtvHeaderExtendedService;
    @Autowired transient private RtvHeaderService rtvHeaderService;
    
    @Override
    protected String processFormat()
    {
        return FP_IDOC;
    }


    @Override
    public void readFileContent(DnDocMsg docMsg, byte[] input)
            throws Exception
    {
        
    }


    @Override
    public String getTargetFilename(DnHolder data, String expectedFormat)
    {
        if (expectedFormat != null
                && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }

        return  MsgType.DN.name() + DOC_FILENAME_DELIMITOR
                + data.getDnHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
                + data.getDnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(data.getDnHeader().getDnNo()) + DOC_FILENAME_DELIMITOR
                + data.getDnHeader().getDnOid() + ".xml";
    }


    @Override
    protected String translate(DnDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
                this.getTargetFilename(docMsg.getData(), FP_IDOC));
        this.createFile(docMsg.getData(), targetFile, FP_IDOC);

        return targetFile.getName();
    }


    @Override
    public byte[] getFileByte(DnHolder data, File targetFile, String expectedFormat)
            throws Exception
    {   
        DnHolder dnHolder = data;
        Document document = new Document();
        // HDR
        if (dnHolder.getDnHeader() != null)
        {
            this.createNamespace(document);
            this.createHeader(dnHolder, document);

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


    private void createHeader(DnHolder dn, Document document) throws Exception
    {
        DnHeaderHolder header = dn.getDnHeader();
        
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
        
        RtvHeaderHolder rtvHeader = rtvHeaderService.selectRtvHeaderByRtvNo(
            header.getBuyerOid(), header.getRtvNo(), header.getSupplierCode());
        List<RtvHeaderExtendedHolder> headerExs = rtvHeaderExtendedService.selectHeaderExtendedByKey(rtvHeader.getRtvOid());
        if (null == header.getRtvNo())
        {
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.SNDPRN", "");
        }
        else
        {
            String sndprn = "";
            if (headerExs != null && !headerExs.isEmpty())
            {
                for (RtvHeaderExtendedHolder extended : headerExs)
                {
                    if ("RCVPRN".equals(extended.getFieldName()) && null != extended.getStringValue())
                    {
                        sndprn = extended.getStringValue();
                        break;
                    }
                }
            }
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.SNDPRN", sndprn);
        }
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.RCVPOR", "SAP_TEC");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.RCVPRT", "LS");
        
        if (null == header.getRtvNo())
        {
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.RCVPRN", "");
        }
        else
        {
            String rcvprn = "";
            if (headerExs != null && !headerExs.isEmpty())
            {
                for (RtvHeaderExtendedHolder extended : headerExs)
                {
                    if ("SNDPRN".equals(extended.getFieldName()) && null != extended.getStringValue())
                    {
                        rcvprn = extended.getStringValue();
                        break;
                    }
                }
            }
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.RCVPRN", rcvprn);
        }
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.CREDAT", "");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.CRETIM", "");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.EDI_DC40.SERIAL", "");
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.CURCY", "SGD");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.ZTERM", "T17N");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.BSART", "CRME");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK01.BELNR", header.getDnNo());
        
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
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(2).NAME1", header.getStoreName());
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(2).KNREF", this.mergeLocationCode(header.getStoreCode(), map));
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(3).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(3).PARVW", "BK");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(3).PARTN", buyerCode);
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDKA1(3).PAORG", buyerCode);
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(0).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(0).QUALF", "009");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(0).BELNR", header.getDnNo());
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(1).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(1).QUALF", "087");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK02(1).BELNR", header.getDnNo());
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(0).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(0).IDDAT", "012");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(0).DATUM", DateUtil.getInstance().convertDateToString(header.getDnDate(), DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC).substring(0, 8));
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(1).@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(1).IDDAT", "024");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK03(1).DATUM", DateUtil.getInstance().convertDateToString(header.getDnDate(), DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC).substring(0, 8));
        
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
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK04.MWSBT", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(header.getTotalVat(), 2));
        }
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK14.@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK14.QUALF", "011");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDK14.ORGID", buyerCode);
        
        if (dn.getDnDetail() != null)
        {
            createDetails(dn, document);
        }
        
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01.@SEGMENT", "1");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01.SUMID", "011");
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01.SUMME", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(header.getTotalCostWithVat(), 2));
        JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDS01.WAERQ", "SGD");
        
    }
    
    
    private void createDetails(DnHolder dn, Document document)
    {
        List<DnDetailExHolder> details = dn.getDnDetail();
        for (int i = 0; i < details.size(); i++)
        {
            DnDetailHolder detail = details.get(i);
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").POSEX", formatLineSeqNo(String.valueOf(detail.getLineSeqNo() * 10)));
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").MENGE", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getDebitQty(), 4));
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").MENEE", String.valueOf(detail.getOrderUom()));
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").BMNG2", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getDebitQty(), 4));
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").VPREI", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getNetUnitCost(), 2));
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").NETWR", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getItemCost(), 2));
            
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP02.@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP02.QUALF", "001");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP02.BELNR", dn.getDnHeader().getRtvNo());
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP02.ZEILE", detail.getLineRefNo() != null ? detail.getLineRefNo() : formatLineSeqNo(String.valueOf(detail.getLineSeqNo() * 10)));
            
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(0).@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(0).QUALF", "003");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(0).BETRG", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getItemCost(), 2));
            
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(1).@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(1).QUALF", "005");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(1).BETRG", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getItemCost(), 2));
            
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(2).@SEGMENT", "1");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(2).QUALF", "002");
            JdomXmlHelper.getInstance().setValueToDoc(document, "IDOC.E1EDP01(" + i + ").E1EDP26(2).BETRG", BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(detail.getItemGrossCost(), 2));
        }
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

    
//    public static void main(String[] args) throws Exception
//    {
//        DnDocFileHandler handler = new DnDocFileHandler();
//        DnDocMsg docMsg = new DnDocMsg();
//        DnHeaderHolder header = new DnHeaderHolder();
//        header.setInvDate(new Date());
//        header.setInvNo("001");
//        header.setSupplierCode("1006");
//        header.setSupplierName("YC Company");
//        header.setBuyerCode("FP");
//        header.setPoNo("5156465");
//        header.setVatRate(new BigDecimal(7));
//        header.setTotalVat(new BigDecimal(12));
//        
//        DnDetailHolder detail = new DnDetailHolder();
//        detail.setLineSeqNo(10);
//        
//        DnDetailHolder detail1 = new DnDetailHolder();
//        detail1.setLineSeqNo(10);
//        
//        List<DnDetailHolder> details = new ArrayList<DnDetailHolder>();
//        details.add(detail);
//        details.add(detail1);
//        
//        DnHolder inv = new DnHolder();
//        inv.setDnHeader(header);
//        inv.setDnDetail(details);
//        docMsg.setData(inv);
//        
//        handler.createFile(docMsg.getData(), new File("d:/dn.xml"), "idoc");
//    }
    
    
}
