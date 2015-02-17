//*****************************************************************************
//
// File Name       :  CcHeaderHolder.java
// Date Created    :  Dec 23, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 23, 2013 5:19:27 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;


import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.CcStatus;
import com.pracbiz.b2bportal.core.constants.InvType;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CcHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    private static final long serialVersionUID = -2242051166071059661L;

    private BigDecimal invOid;

    private String invNo;

    private String docAction;

    private Date actionDate;

    private InvType invType;

    private Date invDate;

    private String poNo;

    private Date poDate;

    private Date deliveryDate;

    private BigDecimal buyerOid;

    private String buyerCode;

    private String buyerName;

    private String buyerAddr1;

    private String buyerAddr2;

    private String buyerAddr3;

    private String buyerAddr4;

    private String buyerCity;

    private String buyerState;

    private String buyerCtryCode;

    private String buyerPostalCode;
    
    private String buyerBizRegNo;
    
    private String buyerVatRegNo;

    private String deptCode;

    private String deptName;

    private String subDeptCode;

    private String subDeptName;

    private BigDecimal supplierOid;

    private String supplierCode;

    private String supplierName;

    private String supplierAddr1;

    private String supplierAddr2;

    private String supplierAddr3;

    private String supplierAddr4;

    private String supplierCity;

    private String supplierState;

    private String supplierCtryCode;

    private String supplierPostalCode;

    private String storeCode;

    private String storeName;

    private String storeAddr1;

    private String storeAddr2;

    private String storeAddr3;

    private String storeAddr4;

    private String storeCity;

    private String storeState;

    private String storeCtryCode;

    private String storePostalCode;

    private String payTermCode;

    private String payTermDesc;

    private String payInstruct;

    private BigDecimal additionalDiscountAmount;

    private BigDecimal additionalDiscountPercent;

    private BigDecimal invAmountNoVat;

    private BigDecimal vatAmount;

    private BigDecimal invAmountWithVat;

    private BigDecimal vatRate;

    private String invRemarks;

    private CcStatus ccStatus;
    
    private String footerLine1;
    private String footerLine2;
    private String footerLine3;
    private String footerLine4;
    
    private Boolean duplicate;
    
    public String computePdfFilename()
    {
        return MsgType.CC.name() + DOC_FILENAME_DELIMITOR
            + this.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.getSupplierCode() + DOC_FILENAME_DELIMITOR 
            + StringUtil.getInstance().convertDocNo(this.getInvNo())
            + DOC_FILENAME_DELIMITOR + this.getInvOid() + ".pdf";
    }


    public BigDecimal getInvOid()
    {
        return invOid;
    }


    public void setInvOid(BigDecimal invOid)
    {
        this.invOid = invOid;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo == null ? null : invNo.trim();
    }


    public String getDocAction()
    {
        return docAction;
    }


    public void setDocAction(String docAction)
    {
        this.docAction = docAction == null ? null : docAction.trim();
    }


    public Date getActionDate()
    {
        return actionDate == null ? null : (Date)actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date)actionDate.clone();
    }


    public InvType getInvType()
    {
        return invType;
    }


    public void setInvType(InvType invType)
    {
        this.invType = invType;
    }


    public Date getInvDate()
    {
        return invDate == null ? null : (Date)invDate.clone();
    }


    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date)invDate.clone();
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo == null ? null : poNo.trim();
    }


    public Date getPoDate()
    {
        return poDate == null ? null : (Date)poDate.clone();
    }


    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date)poDate.clone();
    }


    public Date getDeliveryDate()
    {
        return deliveryDate == null ? null : (Date)deliveryDate.clone();
    }


    public void setDeliveryDate(Date deliveryDate)
    {
        this.deliveryDate = deliveryDate == null ? null : (Date)deliveryDate
            .clone();
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode == null ? null : buyerCode.trim();
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }


    public String getBuyerAddr1()
    {
        return buyerAddr1;
    }


    public void setBuyerAddr1(String buyerAddr1)
    {
        this.buyerAddr1 = buyerAddr1 == null ? null : buyerAddr1.trim();
    }


    public String getBuyerAddr2()
    {
        return buyerAddr2;
    }


    public void setBuyerAddr2(String buyerAddr2)
    {
        this.buyerAddr2 = buyerAddr2 == null ? null : buyerAddr2.trim();
    }


    public String getBuyerAddr3()
    {
        return buyerAddr3;
    }


    public void setBuyerAddr3(String buyerAddr3)
    {
        this.buyerAddr3 = buyerAddr3 == null ? null : buyerAddr3.trim();
    }


    public String getBuyerAddr4()
    {
        return buyerAddr4;
    }


    public void setBuyerAddr4(String buyerAddr4)
    {
        this.buyerAddr4 = buyerAddr4 == null ? null : buyerAddr4.trim();
    }


    public String getBuyerCity()
    {
        return buyerCity;
    }


    public void setBuyerCity(String buyerCity)
    {
        this.buyerCity = buyerCity == null ? null : buyerCity.trim();
    }


    public String getBuyerState()
    {
        return buyerState;
    }


    public void setBuyerState(String buyerState)
    {
        this.buyerState = buyerState == null ? null : buyerState.trim();
    }


    public String getBuyerCtryCode()
    {
        return buyerCtryCode;
    }


    public void setBuyerCtryCode(String buyerCtryCode)
    {
        this.buyerCtryCode = buyerCtryCode == null ? null : buyerCtryCode
            .trim();
    }


    public String getBuyerPostalCode()
    {
        return buyerPostalCode;
    }


    public void setBuyerPostalCode(String buyerPostalCode)
    {
        this.buyerPostalCode = buyerPostalCode == null ? null : buyerPostalCode
            .trim();
    }


    public String getDeptCode()
    {
        return deptCode;
    }


    public void setDeptCode(String deptCode)
    {
        this.deptCode = deptCode == null ? null : deptCode.trim();
    }


    public String getDeptName()
    {
        return deptName;
    }


    public void setDeptName(String deptName)
    {
        this.deptName = deptName == null ? null : deptName.trim();
    }


    public String getSubDeptCode()
    {
        return subDeptCode;
    }


    public void setSubDeptCode(String subDeptCode)
    {
        this.subDeptCode = subDeptCode == null ? null : subDeptCode.trim();
    }


    public String getSubDeptName()
    {
        return subDeptName;
    }


    public void setSubDeptName(String subDeptName)
    {
        this.subDeptName = subDeptName == null ? null : subDeptName.trim();
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    public String getSupplierCode()
    {
        return supplierCode;
    }


    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }


    public String getSupplierAddr1()
    {
        return supplierAddr1;
    }


    public void setSupplierAddr1(String supplierAddr1)
    {
        this.supplierAddr1 = supplierAddr1 == null ? null : supplierAddr1
            .trim();
    }


    public String getSupplierAddr2()
    {
        return supplierAddr2;
    }


    public void setSupplierAddr2(String supplierAddr2)
    {
        this.supplierAddr2 = supplierAddr2 == null ? null : supplierAddr2
            .trim();
    }


    public String getSupplierAddr3()
    {
        return supplierAddr3;
    }


    public void setSupplierAddr3(String supplierAddr3)
    {
        this.supplierAddr3 = supplierAddr3 == null ? null : supplierAddr3
            .trim();
    }


    public String getSupplierAddr4()
    {
        return supplierAddr4;
    }


    public void setSupplierAddr4(String supplierAddr4)
    {
        this.supplierAddr4 = supplierAddr4 == null ? null : supplierAddr4
            .trim();
    }


    public String getSupplierCity()
    {
        return supplierCity;
    }


    public void setSupplierCity(String supplierCity)
    {
        this.supplierCity = supplierCity == null ? null : supplierCity.trim();
    }


    public String getSupplierState()
    {
        return supplierState;
    }


    public void setSupplierState(String supplierState)
    {
        this.supplierState = supplierState == null ? null : supplierState
            .trim();
    }


    public String getSupplierCtryCode()
    {
        return supplierCtryCode;
    }


    public void setSupplierCtryCode(String supplierCtryCode)
    {
        this.supplierCtryCode = supplierCtryCode == null ? null
            : supplierCtryCode.trim();
    }


    public String getSupplierPostalCode()
    {
        return supplierPostalCode;
    }


    public void setSupplierPostalCode(String supplierPostalCode)
    {
        this.supplierPostalCode = supplierPostalCode == null ? null
            : supplierPostalCode.trim();
    }


    public String getStoreCode()
    {
        return storeCode;
    }


    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
    }


    public String getStoreName()
    {
        return storeName;
    }


    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }


    public String getStoreAddr1()
    {
        return storeAddr1;
    }


    public void setStoreAddr1(String storeAddr1)
    {
        this.storeAddr1 = storeAddr1;
    }


    public String getStoreAddr2()
    {
        return storeAddr2;
    }


    public void setStoreAddr2(String storeAddr2)
    {
        this.storeAddr2 = storeAddr2;
    }


    public String getStoreAddr3()
    {
        return storeAddr3;
    }


    public void setStoreAddr3(String storeAddr3)
    {
        this.storeAddr3 = storeAddr3;
    }


    public String getStoreAddr4()
    {
        return storeAddr4;
    }


    public void setStoreAddr4(String storeAddr4)
    {
        this.storeAddr4 = storeAddr4;
    }


    public String getStoreCity()
    {
        return storeCity;
    }


    public void setStoreCity(String storeCity)
    {
        this.storeCity = storeCity;
    }


    public String getStoreState()
    {
        return storeState;
    }


    public void setStoreState(String storeState)
    {
        this.storeState = storeState;
    }


    public String getStoreCtryCode()
    {
        return storeCtryCode;
    }


    public void setStoreCtryCode(String storeCtryCode)
    {
        this.storeCtryCode = storeCtryCode;
    }


    public String getStorePostalCode()
    {
        return storePostalCode;
    }


    public void setStorePostalCode(String storePostalCode)
    {
        this.storePostalCode = storePostalCode;
    }


    public String getPayTermCode()
    {
        return payTermCode;
    }


    public void setPayTermCode(String payTermCode)
    {
        this.payTermCode = payTermCode == null ? null : payTermCode.trim();
    }


    public String getPayTermDesc()
    {
        return payTermDesc;
    }


    public void setPayTermDesc(String payTermDesc)
    {
        this.payTermDesc = payTermDesc == null ? null : payTermDesc.trim();
    }


    public String getPayInstruct()
    {
        return payInstruct;
    }


    public void setPayInstruct(String payInstruct)
    {
        this.payInstruct = payInstruct == null ? null : payInstruct.trim();
    }


    public BigDecimal getAdditionalDiscountAmount()
    {
        return additionalDiscountAmount;
    }


    public void setAdditionalDiscountAmount(BigDecimal additionalDiscountAmount)
    {
        this.additionalDiscountAmount = additionalDiscountAmount;
    }


    public BigDecimal getAdditionalDiscountPercent()
    {
        return additionalDiscountPercent;
    }


    public void setAdditionalDiscountPercent(
        BigDecimal additionalDiscountPercent)
    {
        this.additionalDiscountPercent = additionalDiscountPercent;
    }


    public BigDecimal getInvAmountNoVat()
    {
        return invAmountNoVat;
    }


    public void setInvAmountNoVat(BigDecimal invAmountNoVat)
    {
        this.invAmountNoVat = invAmountNoVat;
    }


    public BigDecimal getVatAmount()
    {
        return vatAmount;
    }


    public void setVatAmount(BigDecimal vatAmount)
    {
        this.vatAmount = vatAmount;
    }


    public BigDecimal getInvAmountWithVat()
    {
        return invAmountWithVat;
    }


    public void setInvAmountWithVat(BigDecimal invAmountWithVat)
    {
        this.invAmountWithVat = invAmountWithVat;
    }


    public BigDecimal getVatRate()
    {
        return vatRate;
    }


    public void setVatRate(BigDecimal vatRate)
    {
        this.vatRate = vatRate;
    }


    public String getInvRemarks()
    {
        return invRemarks;
    }


    public void setInvRemarks(String invRemarks)
    {
        this.invRemarks = invRemarks == null ? null : invRemarks.trim();
    }


    public String getFooterLine1()
    {
        return footerLine1;
    }


    public void setFooterLine1(String footerLine1)
    {
        this.footerLine1 = footerLine1;
    }


    public String getFooterLine2()
    {
        return footerLine2;
    }


    public void setFooterLine2(String footerLine2)
    {
        this.footerLine2 = footerLine2;
    }


    public String getFooterLine3()
    {
        return footerLine3;
    }


    public void setFooterLine3(String footerLine3)
    {
        this.footerLine3 = footerLine3;
    }


    public String getFooterLine4()
    {
        return footerLine4;
    }


    public void setFooterLine4(String footerLine4)
    {
        this.footerLine4 = footerLine4;
    }


    public CcStatus getCcStatus()
    {
        return ccStatus;
    }


    public void setCcStatus(CcStatus ccStatus)
    {
        this.ccStatus = ccStatus;
    }

    

    public String getBuyerBizRegNo()
    {
        return buyerBizRegNo;
    }


    public void setBuyerBizRegNo(String buyerBizRegNo)
    {
        this.buyerBizRegNo = buyerBizRegNo;
    }


    public String getBuyerVatRegNo()
    {
        return buyerVatRegNo;
    }


    public void setBuyerVatRegNo(String buyerVatRegNo)
    {
        this.buyerVatRegNo = buyerVatRegNo;
    }


    public Boolean getDuplicate()
    {
        return duplicate;
    }


    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }


    public MsgTransactionsHolder toMsgTransactions()
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();
        msg.setDocOid(invOid);
        msg.setMsgType(MsgType.CC.name());
        msg.setMsgRefNo(invNo);
        msg.setBuyerOid(buyerOid);
        msg.setBuyerCode(buyerCode);
        msg.setBuyerName(buyerName);
        msg.setSupplierOid(supplierOid);
        msg.setSupplierCode(supplierCode);
        msg.setSupplierName(supplierName);
        msg.setCreateDate(new Date());
        msg.setReadStatus(ReadStatus.UNREAD);
        msg.setActive(true);
        msg.setValid(true);
        msg.setReportFilename(this.computePdfFilename());
        
        return msg;
    }
    
    public String toStringWithDelimiterCharacter(String delimiterChar) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(invNo).append(delimiterChar)
            .append(docAction).append(delimiterChar)
            .append(DateUtil.getInstance().convertDateToString(actionDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
            .append(invType).append(delimiterChar)
            .append(DateUtil.getInstance().convertDateToString(invDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
            .append(poNo).append(delimiterChar)
            .append(DateUtil.getInstance().convertDateToString(poDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
            .append(DateUtil.getInstance().convertDateToString(deliveryDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
            .append(buyerCode).append(delimiterChar)
            .append(buyerName).append(delimiterChar)
            .append(buyerAddr1).append(delimiterChar)
            .append(buyerAddr2).append(delimiterChar)
            .append(buyerAddr3).append(delimiterChar)
            .append(buyerAddr4).append(delimiterChar)
            .append(buyerCity).append(delimiterChar)
            .append(buyerState).append(delimiterChar)
            .append(buyerCtryCode).append(delimiterChar)
            .append(buyerPostalCode).append(delimiterChar)
            .append(supplierCode).append(delimiterChar)
            .append(supplierName).append(delimiterChar)
            .append(supplierAddr1).append(delimiterChar)
            .append(supplierAddr2).append(delimiterChar)
            .append(supplierAddr3).append(delimiterChar)
            .append(supplierAddr4).append(delimiterChar)
            .append(supplierCity).append(delimiterChar)
            .append(supplierState).append(delimiterChar)
            .append(supplierCtryCode).append(delimiterChar)
            .append(supplierPostalCode).append(delimiterChar)
            .append(buyerBizRegNo).append(delimiterChar)
            .append(buyerVatRegNo).append(delimiterChar)
            .append(payTermCode).append(delimiterChar)
            .append(payTermDesc).append(delimiterChar)
            .append(payInstruct).append(delimiterChar)
            .append(additionalDiscountAmount).append(delimiterChar)
            .append(additionalDiscountPercent).append(delimiterChar)
            .append(invAmountNoVat).append(delimiterChar)
            .append(vatAmount).append(delimiterChar)
            .append(invAmountWithVat).append(delimiterChar)
            .append(vatRate).append(delimiterChar)
            .append(invRemarks);
        
        return buffer.toString();
    }
    
    @Override
    public String getLogicalKey()
    {
        return this.getInvNo();
    }


    @Override
    public String getCustomIdentification()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
