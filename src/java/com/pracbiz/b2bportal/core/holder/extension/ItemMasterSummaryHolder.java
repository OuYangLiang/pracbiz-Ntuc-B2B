package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.core.constants.ItemStatus;

public class ItemMasterSummaryHolder extends MsgTransactionsExHolder
{

    private static final long serialVersionUID = 1L;
    private BigDecimal ItemOid;
    private String fileName;
    private ItemStatus itemStatus;
    private String tradingPartner;


    public BigDecimal getItemOid()
    {
        return ItemOid;
    }


    public void setItemOid(BigDecimal itemOid)
    {
        ItemOid = itemOid;
    }


    public String getFileName()
    {
        return fileName;
    }


    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    public ItemStatus getItemStatus()
    {
        return itemStatus;
    }


    public void setItemStatus(ItemStatus itemStatus)
    {
        this.itemStatus = itemStatus;
    }


    public String getTradingPartner()
    {
        return tradingPartner;
    }


    public void setTradingPartner(String tradingPartner)
    {
        this.tradingPartner = tradingPartner;
    }

}
