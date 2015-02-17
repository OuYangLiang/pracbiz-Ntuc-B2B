package com.pracbiz.b2bportal.core.report.excel;

import java.math.BigDecimal;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class MatchingReportDetail
{
    private String itemCode;
    private String barcode;
    private String uom;
    private String itemDesc;

    private BigDecimal poPrice;
    private BigDecimal invPrice;

    private BigDecimal poQty;
    private BigDecimal grnQty;
    private BigDecimal invQty;

    private BigDecimal poAmt;
    private BigDecimal dnAmt;
    private BigDecimal invAmt;
    private BigDecimal expInvAmt;


    public String getItemCode()
    {
        return itemCode;
    }


    public void setItemCode(String itemCode)
    {
        this.itemCode = itemCode;
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


    public String getItemDesc()
    {
        return itemDesc;
    }


    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc;
    }


    public BigDecimal getPoPrice()
    {
        return poPrice;
    }


    public void setPoPrice(BigDecimal poPrice)
    {
        this.poPrice = poPrice;
    }


    public BigDecimal getInvPrice()
    {
        return invPrice;
    }


    public void setInvPrice(BigDecimal invPrice)
    {
        this.invPrice = invPrice;
    }


    public BigDecimal getPoQty()
    {
        return poQty;
    }


    public void setPoQty(BigDecimal poQty)
    {
        this.poQty = poQty;
    }


    public BigDecimal getGrnQty()
    {
        return grnQty;
    }


    public void setGrnQty(BigDecimal grnQty)
    {
        this.grnQty = grnQty;
    }


    public BigDecimal getInvQty()
    {
        return invQty;
    }


    public void setInvQty(BigDecimal invQty)
    {
        this.invQty = invQty;
    }


    public BigDecimal getPoAmt()
    {
        return poAmt;
    }


    public void setPoAmt(BigDecimal poAmt)
    {
        this.poAmt = poAmt;
    }


    public BigDecimal getDnAmt()
    {
        return dnAmt;
    }


    public void setDnAmt(BigDecimal dnAmt)
    {
        this.dnAmt = dnAmt;
    }


    public BigDecimal getInvAmt()
    {
        return invAmt;
    }


    public void setInvAmt(BigDecimal invAmt)
    {
        this.invAmt = invAmt;
    }


    public BigDecimal getExpInvAmt()
    {
        return expInvAmt;
    }


    public void setExpInvAmt(BigDecimal expInvAmt)
    {
        this.expInvAmt = expInvAmt;
    }
}
