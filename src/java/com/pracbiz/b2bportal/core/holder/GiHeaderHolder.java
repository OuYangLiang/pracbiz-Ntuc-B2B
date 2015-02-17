package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.GiStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class GiHeaderHolder extends BaseHolder implements CoreCommonConstants
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal giOid;
    private String giNo;
    private String docAction;
    private Date actionDate;
    private Date giDate;
    private String rtvNo;
    private Date rtvDate;
    private Date createDate;
    private BigDecimal buyerOid;
    private String buyerCode;
    private String buyerName;
    private BigDecimal supplierOid;
    private String supplierCode;
    private String supplierName;
    private String issuingStoreCode;
    private String issuingStoreName;
    private BigDecimal totalIssuedQty;
    private String collectedBy;
    private Integer itemCount;
    private BigDecimal totalCost;
    private String giRemarks;
    private Boolean duplicate;
    private GiStatus giStatus;


    public String computePdfFilename()
    {
        return MsgType.GI.name() + DOC_FILENAME_DELIMITOR
            + this.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.getSupplierCode() + DOC_FILENAME_DELIMITOR 
            + StringUtil.getInstance().convertDocNo(this.getGiNo())
            + DOC_FILENAME_DELIMITOR + this.getGiOid() + ".pdf";
    }
    
    
    public BigDecimal getGiOid()
    {
        return giOid;
    }


    public void setGiOid(BigDecimal giOid)
    {
        this.giOid = giOid;
    }


    public String getGiNo()
    {
        return giNo;
    }


    public void setGiNo(String giNo)
    {
        this.giNo = giNo;
    }


    public String getDocAction()
    {
        return docAction;
    }


    public void setDocAction(String docAction)
    {
        this.docAction = docAction;
    }


    public Date getActionDate()
    {
        return actionDate == null ? null : (Date) actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date) actionDate.clone();
    }


    public Date getGiDate()
    {
        return giDate == null ? null : (Date) giDate.clone();
    }


    public void setGiDate(Date giDate)
    {
        this.giDate = giDate == null ? null : (Date) giDate.clone();
    }


    public String getRtvNo()
    {
        return rtvNo;
    }


    public void setRtvNo(String rtvNo)
    {
        this.rtvNo = rtvNo;
    }


    public Date getRtvDate()
    {
        return rtvDate == null ? null : (Date) rtvDate.clone();
    }


    public void setRtvDate(Date rtvDate)
    {
        this.rtvDate = rtvDate == null ? null : (Date) rtvDate.clone();
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date) createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date) createDate.clone();
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
        this.buyerCode = buyerCode;
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
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
        this.supplierCode = supplierCode;
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }


    public String getIssuingStoreCode()
    {
        return issuingStoreCode;
    }


    public void setIssuingStoreCode(String issuingStoreCode)
    {
        this.issuingStoreCode = issuingStoreCode;
    }


    public String getIssuingStoreName()
    {
        return issuingStoreName;
    }


    public void setIssuingStoreName(String issuingStoreName)
    {
        this.issuingStoreName = issuingStoreName;
    }


    public BigDecimal getTotalIssuedQty()
    {
        return totalIssuedQty;
    }


    public void setTotalIssuedQty(BigDecimal totalIssuedQty)
    {
        this.totalIssuedQty = totalIssuedQty;
    }


    public String getCollectedBy()
    {
        return collectedBy;
    }


    public void setCollectedBy(String collectedBy)
    {
        this.collectedBy = collectedBy;
    }


    public Integer getItemCount()
    {
        return itemCount;
    }


    public void setItemCount(Integer itemCount)
    {
        this.itemCount = itemCount;
    }


    public BigDecimal getTotalCost()
    {
        return totalCost;
    }


    public void setTotalCost(BigDecimal totalCost)
    {
        this.totalCost = totalCost;
    }


    public String getGiRemarks()
    {
        return giRemarks;
    }


    public void setGiRemarks(String giRemarks)
    {
        this.giRemarks = giRemarks;
    }
    

    public Boolean getDuplicate()
    {
        return duplicate;
    }


    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }


    public GiStatus getGiStatus()
    {
        return giStatus;
    }


    public void setGiStatus(GiStatus giStatus)
    {
        this.giStatus = giStatus;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(giOid);
    }
}