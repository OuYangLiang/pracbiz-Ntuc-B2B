package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.InvType;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class InvHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    private static final long serialVersionUID = 4882161949539630124L;

    private BigDecimal invOid;

    private String invNo;

    private String docAction;

    private Date actionDate;

    private InvType invType;

    private Date invDate;

    private String poNo;

    private Date poDate;
    
    private String deliveryNo;

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

    private String supplierBizRegNo;

    private String supplierVatRegNo;

    private String shipToCode;

    private String shipToName;

    private String shipToAddr1;

    private String shipToAddr2;

    private String shipToAddr3;

    private String shipToAddr4;

    private String shipToCity;

    private String shipToState;

    private String shipToCtryCode;

    private String shipToPostalCode;

    private String payTermCode;

    private String payTermDesc;

    private String payInstruct;

    private BigDecimal additionalDiscountAmount;

    private BigDecimal additionalDiscountPercent;
    
    private BigDecimal cashDiscountAmount;

    private BigDecimal cashDiscountPercent;

    private BigDecimal invAmountNoVat;

    private BigDecimal vatAmount;

    private BigDecimal invAmountWithVat;

    private BigDecimal vatRate;

    private String invRemarks;

    private BigDecimal poOid;
    
    private InvStatus invStatus;
    
    private String oldInvNo;
    
    private String footerLine1;
    private String footerLine2;
    private String footerLine3;
    private String footerLine4;
    
    public String computePdfFilename()
    {
        return MsgType.INV.name() + DOC_FILENAME_DELIMITOR
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
        return actionDate == null ? null : (Date) actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date) actionDate.clone();
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
        return invDate == null ? null : (Date) invDate.clone();
    }


    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date) invDate.clone();
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
        return poDate == null ? null : (Date) poDate.clone();
    }


    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date) poDate.clone();
    }


    public Date getDeliveryDate()
    {
        return deliveryDate == null ? null : (Date) deliveryDate.clone();
    }


    public void setDeliveryDate(Date deliveryDate)
    {
        this.deliveryDate = deliveryDate == null ? null : (Date) deliveryDate
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


    public String getSupplierBizRegNo()
    {
        return supplierBizRegNo;
    }


    public void setSupplierBizRegNo(String supplierBizRegNo)
    {
        this.supplierBizRegNo = supplierBizRegNo == null ? null
                : supplierBizRegNo.trim();
    }


    public String getSupplierVatRegNo()
    {
        return supplierVatRegNo;
    }


    public void setSupplierVatRegNo(String supplierVatRegNo)
    {
        this.supplierVatRegNo = supplierVatRegNo == null ? null
                : supplierVatRegNo.trim();
    }


    public String getShipToCode()
    {
        return shipToCode;
    }


    public void setShipToCode(String shipToCode)
    {
        this.shipToCode = shipToCode == null ? null : shipToCode.trim();
    }


    public String getShipToName()
    {
        return shipToName;
    }


    public void setShipToName(String shipToName)
    {
        this.shipToName = shipToName == null ? null : shipToName.trim();
    }


    public String getShipToAddr1()
    {
        return shipToAddr1;
    }


    public void setShipToAddr1(String shipToAddr1)
    {
        this.shipToAddr1 = shipToAddr1 == null ? null : shipToAddr1.trim();
    }


    public String getShipToAddr2()
    {
        return shipToAddr2;
    }


    public void setShipToAddr2(String shipToAddr2)
    {
        this.shipToAddr2 = shipToAddr2 == null ? null : shipToAddr2.trim();
    }


    public String getShipToAddr3()
    {
        return shipToAddr3;
    }


    public void setShipToAddr3(String shipToAddr3)
    {
        this.shipToAddr3 = shipToAddr3 == null ? null : shipToAddr3.trim();
    }


    public String getShipToAddr4()
    {
        return shipToAddr4;
    }


    public void setShipToAddr4(String shipToAddr4)
    {
        this.shipToAddr4 = shipToAddr4 == null ? null : shipToAddr4.trim();
    }


    public String getShipToCity()
    {
        return shipToCity;
    }


    public void setShipToCity(String shipToCity)
    {
        this.shipToCity = shipToCity == null ? null : shipToCity.trim();
    }


    public String getShipToState()
    {
        return shipToState;
    }


    public void setShipToState(String shipToState)
    {
        this.shipToState = shipToState == null ? null : shipToState.trim();
    }


    public String getShipToCtryCode()
    {
        return shipToCtryCode;
    }


    public void setShipToCtryCode(String shipToCtryCode)
    {
        this.shipToCtryCode = shipToCtryCode == null ? null : shipToCtryCode
                .trim();
    }


    public String getShipToPostalCode()
    {
        return shipToPostalCode;
    }


    public void setShipToPostalCode(String shipToPostalCode)
    {
        this.shipToPostalCode = shipToPostalCode == null ? null
                : shipToPostalCode.trim();
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

    
    public BigDecimal getCashDiscountAmount()
    {
        return cashDiscountAmount;
    }

    
    public void setCashDiscountAmount(BigDecimal cashDiscountAmount)
    {
        this.cashDiscountAmount = cashDiscountAmount;
    }

    
    public BigDecimal getCashDiscountPercent()
    {
        return cashDiscountPercent;
    }

    
    public void setCashDiscountPercent(BigDecimal cashDiscountPercent)
    {
        this.cashDiscountPercent = cashDiscountPercent;
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


    public BigDecimal getPoOid()
    {
        return poOid;
    }


    public void setPoOid(BigDecimal poOid)
    {
        this.poOid = poOid;
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
    
    
    public InvStatus getInvStatus()
    {
        return invStatus;
    }

    public void setInvStatus(InvStatus invStatus)
    {
        this.invStatus = invStatus;
    }

    public String getOldInvNo()
    {
        return oldInvNo;
    }

    public void setOldInvNo(String oldInvNo)
    {
        this.oldInvNo = oldInvNo;
    }
    
    public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public MsgTransactionsHolder toMsgTransactions()
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();
        msg.setDocOid(invOid);
        msg.setMsgType(MsgType.INV.name());
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
        msg.setGeneratedOnPortal(true);
        
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
            .append(deliveryNo).append(delimiterChar)
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
            .append(supplierBizRegNo).append(delimiterChar)
            .append(supplierVatRegNo).append(delimiterChar)
            .append(shipToCode).append(delimiterChar)
            .append(shipToName).append(delimiterChar)
            .append(shipToAddr1).append(delimiterChar)
            .append(shipToAddr2).append(delimiterChar)
            .append(shipToAddr3).append(delimiterChar)
            .append(shipToAddr4).append(delimiterChar)
            .append(shipToCity).append(delimiterChar)
            .append(shipToState).append(delimiterChar)
            .append(shipToCtryCode).append(delimiterChar)
            .append(shipToPostalCode).append(delimiterChar)

            .append(payTermCode).append(delimiterChar)
            .append(payTermDesc).append(delimiterChar)
            .append(payInstruct).append(delimiterChar)
            .append(additionalDiscountAmount).append(delimiterChar)
            .append(additionalDiscountPercent).append(delimiterChar)
            .append(invAmountNoVat).append(delimiterChar)
            .append(vatAmount).append(delimiterChar)
            .append(invAmountWithVat).append(delimiterChar)
            .append(vatRate).append(delimiterChar)
            .append(invRemarks).append(delimiterChar)
            .append(cashDiscountAmount).append(delimiterChar)
            .append(cashDiscountPercent);
        
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(invOid);
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return this.getInvNo();
    }
}