package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class PoInvGrnDnMatchingDetailHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal matchingOid;
    private Integer seq;
    private String buyerItemCode;
    private String itemDesc;
    private String barcode;
    private String uom;
    private BigDecimal poPrice;
    private BigDecimal poQty;
    private BigDecimal poAmt;
    private BigDecimal invPrice;
    private BigDecimal invQty;
    private BigDecimal invAmt;
    private BigDecimal grnQty;
    private BigDecimal dnAmt;
    private PoInvGrnDnMatchingPriceStatus priceStatus;
    private String priceStatusActionRemarks;
    private Date priceStatusActionDate;
    private String priceStatusActionBy;
    private PoInvGrnDnMatchingQtyStatus qtyStatus;
    private String qtyStatusActionRemarks;
    private Date qtyStatusActionDate;
    private String qtyStatusActionBy;
    
    private BigDecimal expInvAmt;
    
    private String poNo;
    private String poStoreCode;


    public BigDecimal getMatchingOid()
    {
        return matchingOid;
    }


    public void setMatchingOid(BigDecimal matchingOid)
    {
        this.matchingOid = matchingOid;
    }


    public Integer getSeq()
    {
        return seq;
    }


    public void setSeq(Integer seq)
    {
        this.seq = seq;
    }


    public String getBuyerItemCode()
    {
        return buyerItemCode;
    }


    public void setBuyerItemCode(String buyerItemCode)
    {
        this.buyerItemCode = buyerItemCode;
    }


    public String getItemDesc()
    {
        return itemDesc;
    }


    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc;
    }


    public String getBarcode()
    {
        return barcode;
    }


    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }


    public String getUom()
    {
        return uom;
    }


    public void setUom(String uom)
    {
        this.uom = uom;
    }


    public BigDecimal getPoPrice()
    {
        return poPrice;
    }


    public void setPoPrice(BigDecimal poPrice)
    {
        this.poPrice = poPrice;
    }


    public BigDecimal getPoQty()
    {
        return poQty;
    }


    public void setPoQty(BigDecimal poQty)
    {
        this.poQty = poQty;
    }


    public BigDecimal getPoAmt()
    {
        return poAmt;
    }


    public void setPoAmt(BigDecimal poAmt)
    {
        this.poAmt = poAmt;
    }


    public BigDecimal getInvPrice()
    {
        return invPrice;
    }


    public void setInvPrice(BigDecimal invPrice)
    {
        this.invPrice = invPrice;
    }


    public BigDecimal getInvQty()
    {
        return invQty;
    }


    public void setInvQty(BigDecimal invQty)
    {
        this.invQty = invQty;
    }


    public BigDecimal getInvAmt()
    {
        return invAmt;
    }


    public void setInvAmt(BigDecimal invAmt)
    {
        this.invAmt = invAmt;
    }


    public BigDecimal getGrnQty()
    {
        return grnQty;
    }


    public void setGrnQty(BigDecimal grnQty)
    {
        this.grnQty = grnQty;
    }


    public BigDecimal getDnAmt()
    {
        return dnAmt;
    }


    public void setDnAmt(BigDecimal dnAmt)
    {
        this.dnAmt = dnAmt;
    }

    
    public String getPriceStatusActionRemarks()
    {
        return priceStatusActionRemarks;
    }


    public void setPriceStatusActionRemarks(String priceStatusActionRemarks)
    {
        this.priceStatusActionRemarks = priceStatusActionRemarks;
    }


    public Date getPriceStatusActionDate()
    {
        return priceStatusActionDate == null ? null : (Date)priceStatusActionDate.clone() ;
    }


    public void setPriceStatusActionDate(Date priceStatusActionDate)
    {
        this.priceStatusActionDate = priceStatusActionDate == null ? null : (Date)priceStatusActionDate.clone();
    }


    public String getPriceStatusActionBy()
    {
        return priceStatusActionBy;
    }


    public void setPriceStatusActionBy(String priceStatusActionBy)
    {
        this.priceStatusActionBy = priceStatusActionBy;
    }


    public PoInvGrnDnMatchingPriceStatus getPriceStatus()
    {
        return priceStatus;
    }


    public void setPriceStatus(PoInvGrnDnMatchingPriceStatus priceStatus)
    {
        this.priceStatus = priceStatus;
    }


    public PoInvGrnDnMatchingQtyStatus getQtyStatus()
    {
        return qtyStatus;
    }


    public void setQtyStatus(PoInvGrnDnMatchingQtyStatus qtyStatus)
    {
        this.qtyStatus = qtyStatus;
    }


    public String getQtyStatusActionRemarks()
    {
        return qtyStatusActionRemarks;
    }


    public void setQtyStatusActionRemarks(String qtyStatusActionRemarks)
    {
        this.qtyStatusActionRemarks = qtyStatusActionRemarks;
    }


    public Date getQtyStatusActionDate()
    {
        return qtyStatusActionDate == null ? null : (Date)qtyStatusActionDate.clone();
    }


    public void setQtyStatusActionDate(Date qtyStatusActionDate)
    {
        this.qtyStatusActionDate = qtyStatusActionDate == null ? null : (Date)qtyStatusActionDate.clone();
    }


    public String getQtyStatusActionBy()
    {
        return qtyStatusActionBy;
    }


    public void setQtyStatusActionBy(String qtyStatusActionBy)
    {
        this.qtyStatusActionBy = qtyStatusActionBy;
    }


    public BigDecimal getExpInvAmt()
    {
        return expInvAmt;
    }


    public void setExpInvAmt(BigDecimal expInvAmt)
    {
        this.expInvAmt = expInvAmt;
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }


    public String getPoStoreCode()
    {
        return poStoreCode;
    }


    public void setPoStoreCode(String poStoreCode)
    {
        this.poStoreCode = poStoreCode;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(matchingOid) + seq;
    }

    
    @Override
    public String getLogicalKey()
    {
        return poNo + "_" + poStoreCode + "_" + seq;
    }
}
