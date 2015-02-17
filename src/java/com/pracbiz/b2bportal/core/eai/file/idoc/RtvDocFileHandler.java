//*****************************************************************************
//
// File Name       :  RtvDocFileHandler.java
// Date Created    :  Nov 25, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 25, 2013 10:31:23 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.idoc;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.constants.RtvStatus;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.RtvDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.PropertiesUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class RtvDocFileHandler extends
    DefaultDocFileHandler<RtvDocMsg, RtvHolder> implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(RtvDocFileHandler.class);
    //private static int COLLECTION_DATE_RANGE = 7;
    @Override
    protected String processFormat()
    {
        return FP_IDOC;
    }

    
    @Override
    public void readFileContent(RtvDocMsg docMsg, byte[] input)
        throws Exception
    {
        log.info("start to read file content.");
        Document document = FileParserUtil.getInstance().build(input);
        RtvHolder rtv = new RtvHolder();
        initHeaderAndHeaderEx(document, docMsg, rtv);
        initDetailsAndDetailEx(document, docMsg, rtv);
        
        docMsg.setData(rtv);
        log.info("end to read file content.");
    }

    
    @Override
    protected String translate(RtvDocMsg docMsg) throws Exception
    {
        File targetFile = new File(docMsg.getContext().getWorkDir(),
            this.getTargetFilename(docMsg.getData(), FP_IDOC));
        this.createFile(docMsg.getData(), targetFile, FP_IDOC);
    
        return targetFile.getName();
    }

    
    @Override
    public byte[] getFileByte(RtvHolder data, File targetFile,
        String expectedFormat) throws Exception
    {
        return null;
    }

    
    @Override
    public String getTargetFilename(RtvHolder data, String expectedFormat)
    {
        if (expectedFormat != null
                && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            return this.successor.getTargetFilename(data, expectedFormat);
        }

        return MsgType.RTV + DOC_FILENAME_DELIMITOR
                + data.getRtvHeader().getBuyerCode() + DOC_FILENAME_DELIMITOR
                + data.getRtvHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(data.getRtvHeader().getRtvNo()) + DOC_FILENAME_DELIMITOR
                + data.getRtvHeader().getRtvOid() + ".txt";
    
    }
    
    
    
    //***********************
    //private method
    //***********************

    private void initHeaderAndHeaderEx(Document document, RtvDocMsg docMsg, RtvHolder rtv)throws Exception
    {
        RtvHeaderHolder header = new RtvHeaderHolder();
        List<Element> headerRoots = document.getRootElement().getChildren(
            "IDOC");
        
        int lineSeqNo = 1;

        Element headerRoot = headerRoots.get(0);
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        List<Element> e1edk02 = headerRoot.getChildren("E1EDK02");
        
        Element dk02 = this.getElementByChildElement(e1edk02, "QUALF", "001");
        
        if (dk02 != null)
        {
            header.setRtvNo(StringUtil.getInstance().convertDocNoWithNoLeading(dk02.getChildTextTrim("BELNR"), LEADING_ZERO));
            header.setRtvDate(DateUtil.getInstance().convertStringToDate(dk02.getChildTextTrim("DATUM"), DateUtil.DATE_FORMAT_YYYYMMDD));
        }
        
        header.setDocAction("A");
        header.setActionDate(new Date());
        //header.setCollectionDate(DateUtil.getInstance().dateAfterDays(header.getRtvDate(), COLLECTION_DATE_RANGE));
        header.setBuyerCode(docMsg.getSenderCode());
        header.setSupplierCode(docMsg.getReceiverCode());
        
        List<Element> e1edka1 = headerRoot.getChildren("E1EDKA1");
        List<Element> e1eds01 = headerRoot.getChildren("E1EDS01");
        header.setTotalCost(fileUtil.decimalValueOf(e1eds01.get(0).getChildTextTrim("SUMME")));
        header.setReasonCode(null);
        header.setReasonDesc(null);
        header.setRtvRemarks(null);
        
        
        header.setRtvOid(docMsg.getDocOid());
        BuyerHolder buyer = docMsg.getBuyer();
        SupplierHolder supplier = docMsg.getSupplier();
        this.initBuyerInfo(buyer, header);
        this.initSupplierInfo(supplier, header);
        this.initRtvHeaderExtended(headerRoot, rtv, docMsg);
        this.initRtvLocation(e1edka1, rtv, docMsg, lineSeqNo);
        header.setRtvStatus(RtvStatus.NEW);
        
        rtv.setRtvHeader(header);
       
    }
    
    
    private void initDetailsAndDetailEx(Document document, RtvDocMsg docMsg, RtvHolder rtv)throws Exception
    {
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        List<Element> headerRoots = document.getRootElement().getChildren(
            "IDOC");
        List<Element> details =  headerRoots.get(0).getChildren(
            "E1EDP01");
        
        List<RtvDetailHolder> rtvDetails = new ArrayList<RtvDetailHolder>();
        List<RtvDetailExtendedHolder> detailExList = new ArrayList<RtvDetailExtendedHolder>();
        List<RtvLocationDetailHolder> rtvLocationDetails = new ArrayList<RtvLocationDetailHolder>();
        int lineSeqNo = 1;
        for (Element detailElement : details)
        {
            RtvDetailHolder detail = new RtvDetailHolder();
            detail.setRtvOid(docMsg.getDocOid());
            detail.setLineSeqNo(lineSeqNo);
            List<Element> e1edp19 =  detailElement.getChildren(
                "E1EDP19");
            
            if (e1edp19 != null && !e1edp19.isEmpty())
            {
                Element qualf001 = getElementByChildElement(e1edp19, "QUALF" ,"001");
                
                if (qualf001 != null)
                {
                    detail
                        .setBuyerItemCode(StringUtil.getInstance()
                            .convertDocNoWithNoLeading(
                                qualf001.getChildTextTrim("IDTNR"),
                                LEADING_ZERO));
                    detail.setItemDesc(qualf001.getChildTextTrim("KTEXT"));
                }
                
                Element qualf003 = getElementByChildElement(e1edp19, "QUALF" ,"003");
                if (qualf003 != null)
                {
                    detail.setBarcode(qualf003.getChildTextTrim("IDTNR"));
                }
            }
            
            detail.setPackingFactor(BigDecimal.ONE);
            detail.setReturnBaseUnit("U");
            detail.setReturnUom(detailElement.getChildTextTrim("MENEE"));
            detail.setReturnQty(fileUtil.decimalValueOf(detailElement.getChildTextTrim("MENGE")));
            detail.setUnitCost(fileUtil.decimalValueOf(detailElement.getChildTextTrim("VPREI")));
            detail.setItemCost(fileUtil.decimalValueOf(detailElement.getChildTextTrim("NETWR")));
            if (rtv.getRtvHeader().getCollectionDate() == null)
            {
                String deliveryDate = detailElement.getChild("E1EDP20").getChildTextTrim("EDATU");
                rtv.getRtvHeader().setCollectionDate(DateUtil.getInstance().convertStringToDate(deliveryDate, DateUtil.DATE_FORMAT_YYYYMMDD));
            }
            
            List<Element> z1ekpo1s =  detailElement.getChildren(
                "Z1EKPO1");
            
            String reasonDesc = null;
            if (z1ekpo1s != null && !z1ekpo1s.isEmpty())
            {
                for (Element element : z1ekpo1s)
                {
                    Element rsntxt = element.getChild("RSNTXT");
                    if (rsntxt != null)
                    {
                        reasonDesc = rsntxt.getTextTrim();
                    }
                }
                detail.setReasonDesc(reasonDesc);
            }
            
            
            Element posex = detailElement.getChild("POSEX");
            if (posex != null)
            {
                detail.setLineRefNo(posex.getTextTrim());
            }
           
            detail.setAllEmptyStringToNull();
            rtvDetails.add(detail);
            this.initRtvLocDetail(detail, rtv, docMsg, rtvLocationDetails);
            this.initRtvDetailExtended(detailElement, rtv, docMsg, lineSeqNo, detailExList);
            lineSeqNo ++;
        }
        
        rtv.setRtvDetail(rtvDetails);
        rtv.setDetailExtended(detailExList);
        rtv.setLocationDetails(rtvLocationDetails);
    }
    
    
    private void initRtvHeaderExtended(Element headerElement, RtvHolder rtv, RtvDocMsg docMsg)throws Exception
    {
        RtvHeaderExtendedHolder termsCode = null;
        RtvHeaderExtendedHolder termsDesc = null;
      
        Element e1edk01 = headerElement.getChild("E1EDK01");
        if (e1edk01 != null)
        {
            String paymentTermsCode = e1edk01.getChildTextTrim("ZTERM");
            if (paymentTermsCode != null && !paymentTermsCode.isEmpty())
            {
                termsCode = new RtvHeaderExtendedHolder();
                termsCode.setRtvOid(docMsg.getDocOid());
                termsCode.setFieldType("S");
                termsCode.setFieldName("paymentTermCode");
                termsCode.setStringValue(paymentTermsCode);
                
                Properties pros = PropertiesUtil.getProperties("payment-terms-code.properties");
                
                termsDesc = new RtvHeaderExtendedHolder();
                termsDesc.setRtvOid(docMsg.getDocOid());
                termsDesc.setFieldType("S");
                termsDesc.setFieldName("paymentTermDesc");
                termsDesc.setStringValue(pros.get(paymentTermsCode).toString());
                
                termsCode.setAllEmptyStringToNull();
                termsDesc.setAllEmptyStringToNull();
            }
        }
        
        
        RtvHeaderExtendedHolder deliveryTerm = null;
        List<Element> e1edk17 = headerElement.getChildren("E1EDK17");
        Element deliveryTermsEl = this.getElementByChildElement(e1edk17, "QUALF", "001");
        if (deliveryTermsEl != null)
        {
            String deliveryTermCode = deliveryTermsEl.getChildTextTrim("LKOND");
            if (deliveryTermCode != null && !deliveryTermCode.isEmpty())
            {
                deliveryTerm = new RtvHeaderExtendedHolder();
                deliveryTerm.setRtvOid(docMsg.getDocOid());
                deliveryTerm.setFieldType("S");
                deliveryTerm.setFieldName("deliveryTermCode");
                deliveryTerm.setStringValue(deliveryTermCode);
                
                deliveryTerm.setAllEmptyStringToNull();
            }
        }
        
        List<Element> edi_dc40List = headerElement.getChildren("EDI_DC40");
        RtvHeaderExtendedHolder sndprn = new RtvHeaderExtendedHolder();
        sndprn.setRtvOid(docMsg.getDocOid());
        sndprn.setFieldType("S");
        sndprn.setFieldName("SNDPRN");
        sndprn.setStringValue(getElementByChildElement(edi_dc40List, "TABNAM", "EDI_DC40").getChildTextTrim("SNDPRN"));
        sndprn.setAllEmptyStringToNull();
        
        RtvHeaderExtendedHolder rcvprn = new RtvHeaderExtendedHolder();
        rcvprn.setRtvOid(docMsg.getDocOid());
        rcvprn.setFieldType("S");
        rcvprn.setFieldName("RCVPRN");
        rcvprn.setStringValue(getElementByChildElement(edi_dc40List, "TABNAM", "EDI_DC40").getChildTextTrim("RCVPRN"));
        rcvprn.setAllEmptyStringToNull();
        
        List<RtvHeaderExtendedHolder> headerExList = new ArrayList<RtvHeaderExtendedHolder>();
        if (termsCode != null)
        {
            headerExList.add(termsCode);
        }
        
        if (termsDesc != null)
        {
            headerExList.add(termsDesc);
        }
        
        if (deliveryTerm != null)
        {
            headerExList.add(deliveryTerm);
        }
        
        headerExList.add(sndprn);
        headerExList.add(rcvprn);
        rtv.setHeaderExtended(headerExList);
    }
    
    
    private void initRtvDetailExtended(Element detailElement, RtvHolder rtv, RtvDocMsg docMsg, int lineSeqNo, List<RtvDetailExtendedHolder> detailExList)throws Exception
    {
        RtvDetailExtendedHolder detailEx = new RtvDetailExtendedHolder();
        detailEx.setRtvOid(docMsg.getDocOid());
        detailEx.setLineSeqNo(lineSeqNo);
        detailEx.setFieldType("B");
        detailEx.setFieldName("offer");
        detailEx.setBoolValue(Boolean.FALSE);
        Element z1ekpo1 = detailElement.getChild("Z1EKPO1");
        if (z1ekpo1 != null)
        {
            Element aktnr = z1ekpo1.getChild("AKTNR");
            if (aktnr != null && !aktnr.getValue().isEmpty())
            {
                detailEx.setBoolValue(Boolean.TRUE);
            } 
        }
        
        detailEx.setAllEmptyStringToNull();
        detailExList.add(detailEx);
        rtv.setDetailExtended(detailExList);
    }
    
    
    private void initRtvLocDetail(RtvDetailHolder detail, RtvHolder rtv, RtvDocMsg docMsg,List<RtvLocationDetailHolder> rtvLocationDetails)throws Exception
    {
        RtvLocationHolder rtvLocation = rtv.getLocations().get(0);
        
        RtvLocationDetailHolder rtvLocationDetail = new RtvLocationDetailHolder();
        rtvLocationDetail.setRtvOid(docMsg.getDocOid());
        rtvLocationDetail.setLocationLineSeqNo(rtvLocation.getLineSeqNo());
        rtvLocationDetail.setDetailLineSeqNo(detail.getLineSeqNo());
        rtvLocationDetail.setLocationShipQty(detail.getReturnQty());
        
        rtvLocationDetail.setAllEmptyStringToNull();
        rtvLocationDetails.add(rtvLocationDetail);
       
    }
    
    
    private void initRtvLocation(List<Element> e1edka1, RtvHolder rtv, RtvDocMsg docMsg, int lineSeqNo)throws Exception
    {
        List<RtvLocationHolder> locations = new ArrayList<RtvLocationHolder>();
        Element we = this.getElementByChildElement(e1edka1, "PARVW", "WE");
        if (we != null)
        {
            RtvLocationHolder location = new RtvLocationHolder();
            location.setRtvOid(docMsg.getDocOid());
            location.setLineSeqNo(lineSeqNo);
            location.setLocationCode(we.getChildTextTrim("LIFNR"));
            location.setLocationName(we.getChildTextTrim("NAME1"));
            location.setAllEmptyStringToNull();
            locations.add(location);
        }
        
        rtv.setLocations(locations);
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
            String qualf = element.getChildText(childNode);
            if (qualf.trim().isEmpty())
            {
                continue;
            }
            
            if (qualf.trim().equals(childNodeValue.trim()))
            {
                return element;
            }
        }
        
        return null;
    }
    
}
