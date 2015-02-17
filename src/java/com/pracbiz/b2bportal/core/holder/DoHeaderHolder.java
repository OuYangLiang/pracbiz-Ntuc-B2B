package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;
import java.util.Date;

public class DoHeaderHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = 4456708680668242758L;

    private BigDecimal doOid;

    private String doNo;

    private String docAction;

    private Date actionDate;

    private Date doDate;

    private String poNo;

    private Date poDate;

    private Date deliveryDate;

    private Date expiryDate;

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

    private String doRemarks;

    public BigDecimal getDoOid() {
        return doOid;
    }

    public void setDoOid(BigDecimal doOid) {
        this.doOid = doOid;
    }

    public String getDoNo() {
        return doNo;
    }

    public void setDoNo(String doNo) {
        this.doNo = doNo == null ? null : doNo.trim();
    }

    public String getDocAction() {
        return docAction;
    }

    public void setDocAction(String docAction) {
        this.docAction = docAction == null ? null : docAction.trim();
    }

    public Date getActionDate() {
        return actionDate  == null ? null : (Date)actionDate.clone();
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate == null ? null : (Date)actionDate.clone();
    }

    public Date getDoDate() {
        return doDate == null ? null : (Date)doDate.clone();
    }

    public void setDoDate(Date doDate) {
        this.doDate = doDate == null ? null : (Date)doDate.clone();
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo == null ? null : poNo.trim();
    }

    public Date getPoDate() {
        return poDate == null ? null : (Date)poDate.clone();
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate == null ? null : (Date)poDate.clone();
    }

    public Date getDeliveryDate() {
        return deliveryDate == null ? null : (Date)deliveryDate.clone();
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate == null ? null : (Date)deliveryDate.clone();
    }

    public Date getExpiryDate() {
        return expiryDate == null ? null : (Date)expiryDate.clone();
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate == null ? null : (Date)expiryDate.clone();
    }

    public BigDecimal getBuyerOid() {
        return buyerOid;
    }

    public void setBuyerOid(BigDecimal buyerOid) {
        this.buyerOid = buyerOid;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode == null ? null : buyerCode.trim();
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }

    public String getBuyerAddr1() {
        return buyerAddr1;
    }

    public void setBuyerAddr1(String buyerAddr1) {
        this.buyerAddr1 = buyerAddr1 == null ? null : buyerAddr1.trim();
    }

    public String getBuyerAddr2() {
        return buyerAddr2;
    }

    public void setBuyerAddr2(String buyerAddr2) {
        this.buyerAddr2 = buyerAddr2 == null ? null : buyerAddr2.trim();
    }

    public String getBuyerAddr3() {
        return buyerAddr3;
    }

    public void setBuyerAddr3(String buyerAddr3) {
        this.buyerAddr3 = buyerAddr3 == null ? null : buyerAddr3.trim();
    }

    public String getBuyerAddr4() {
        return buyerAddr4;
    }

    public void setBuyerAddr4(String buyerAddr4) {
        this.buyerAddr4 = buyerAddr4 == null ? null : buyerAddr4.trim();
    }

    public String getBuyerCity() {
        return buyerCity;
    }

    public void setBuyerCity(String buyerCity) {
        this.buyerCity = buyerCity == null ? null : buyerCity.trim();
    }

    public String getBuyerState() {
        return buyerState;
    }

    public void setBuyerState(String buyerState) {
        this.buyerState = buyerState == null ? null : buyerState.trim();
    }

    public String getBuyerCtryCode() {
        return buyerCtryCode;
    }

    public void setBuyerCtryCode(String buyerCtryCode) {
        this.buyerCtryCode = buyerCtryCode == null ? null : buyerCtryCode.trim();
    }

    public String getBuyerPostalCode() {
        return buyerPostalCode;
    }

    public void setBuyerPostalCode(String buyerPostalCode) {
        this.buyerPostalCode = buyerPostalCode == null ? null : buyerPostalCode.trim();
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode == null ? null : deptCode.trim();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getSubDeptCode() {
        return subDeptCode;
    }

    public void setSubDeptCode(String subDeptCode) {
        this.subDeptCode = subDeptCode == null ? null : subDeptCode.trim();
    }

    public String getSubDeptName() {
        return subDeptName;
    }

    public void setSubDeptName(String subDeptName) {
        this.subDeptName = subDeptName == null ? null : subDeptName.trim();
    }

    public BigDecimal getSupplierOid() {
        return supplierOid;
    }

    public void setSupplierOid(BigDecimal supplierOid) {
        this.supplierOid = supplierOid;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public String getSupplierAddr1() {
        return supplierAddr1;
    }

    public void setSupplierAddr1(String supplierAddr1) {
        this.supplierAddr1 = supplierAddr1 == null ? null : supplierAddr1.trim();
    }

    public String getSupplierAddr2() {
        return supplierAddr2;
    }

    public void setSupplierAddr2(String supplierAddr2) {
        this.supplierAddr2 = supplierAddr2 == null ? null : supplierAddr2.trim();
    }

    public String getSupplierAddr3() {
        return supplierAddr3;
    }

    public void setSupplierAddr3(String supplierAddr3) {
        this.supplierAddr3 = supplierAddr3 == null ? null : supplierAddr3.trim();
    }

    public String getSupplierAddr4() {
        return supplierAddr4;
    }

    public void setSupplierAddr4(String supplierAddr4) {
        this.supplierAddr4 = supplierAddr4 == null ? null : supplierAddr4.trim();
    }

    public String getSupplierCity() {
        return supplierCity;
    }

    public void setSupplierCity(String supplierCity) {
        this.supplierCity = supplierCity == null ? null : supplierCity.trim();
    }

    public String getSupplierState() {
        return supplierState;
    }

    public void setSupplierState(String supplierState) {
        this.supplierState = supplierState == null ? null : supplierState.trim();
    }

    public String getSupplierCtryCode() {
        return supplierCtryCode;
    }

    public void setSupplierCtryCode(String supplierCtryCode) {
        this.supplierCtryCode = supplierCtryCode == null ? null : supplierCtryCode.trim();
    }

    public String getSupplierPostalCode() {
        return supplierPostalCode;
    }

    public void setSupplierPostalCode(String supplierPostalCode) {
        this.supplierPostalCode = supplierPostalCode == null ? null : supplierPostalCode.trim();
    }

    public String getShipToCode() {
        return shipToCode;
    }

    public void setShipToCode(String shipToCode) {
        this.shipToCode = shipToCode == null ? null : shipToCode.trim();
    }

    public String getShipToName() {
        return shipToName;
    }

    public void setShipToName(String shipToName) {
        this.shipToName = shipToName == null ? null : shipToName.trim();
    }

    public String getShipToAddr1() {
        return shipToAddr1;
    }

    public void setShipToAddr1(String shipToAddr1) {
        this.shipToAddr1 = shipToAddr1 == null ? null : shipToAddr1.trim();
    }

    public String getShipToAddr2() {
        return shipToAddr2;
    }

    public void setShipToAddr2(String shipToAddr2) {
        this.shipToAddr2 = shipToAddr2 == null ? null : shipToAddr2.trim();
    }

    public String getShipToAddr3() {
        return shipToAddr3;
    }

    public void setShipToAddr3(String shipToAddr3) {
        this.shipToAddr3 = shipToAddr3 == null ? null : shipToAddr3.trim();
    }

    public String getShipToAddr4() {
        return shipToAddr4;
    }

    public void setShipToAddr4(String shipToAddr4) {
        this.shipToAddr4 = shipToAddr4 == null ? null : shipToAddr4.trim();
    }

    public String getShipToCity() {
        return shipToCity;
    }

    public void setShipToCity(String shipToCity) {
        this.shipToCity = shipToCity == null ? null : shipToCity.trim();
    }

    public String getShipToState() {
        return shipToState;
    }

    public void setShipToState(String shipToState) {
        this.shipToState = shipToState == null ? null : shipToState.trim();
    }

    public String getShipToCtryCode() {
        return shipToCtryCode;
    }

    public void setShipToCtryCode(String shipToCtryCode) {
        this.shipToCtryCode = shipToCtryCode == null ? null : shipToCtryCode.trim();
    }

    public String getShipToPostalCode() {
        return shipToPostalCode;
    }

    public void setShipToPostalCode(String shipToPostalCode) {
        this.shipToPostalCode = shipToPostalCode == null ? null : shipToPostalCode.trim();
    }

    public String getDoRemarks() {
        return doRemarks;
    }

    public void setDoRemarks(String doRemarks) {
        this.doRemarks = doRemarks == null ? null : doRemarks.trim();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(doOid);
    }
}