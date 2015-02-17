package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreAreaHolder extends BaseHolder
{
    private static final long serialVersionUID = -2801811468554272658L;
    private BigDecimal areaOid;
    private String buyerCode;
    private String areaCode;
    private String areaName;
    private Date createDate;
    private Date updateDate;


    public BigDecimal getAreaOid()
    {
        return areaOid;
    }


    public void setAreaOid(BigDecimal areaOid)
    {
        this.areaOid = areaOid;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode == null ? null : buyerCode.trim();
    }


    public String getAreaCode()
    {
        return areaCode;
    }


    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }


    public String getAreaName()
    {
        return areaName;
    }


    public void setAreaName(String areaName)
    {
        this.areaName = areaName == null ? null : areaName.trim();
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date) createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date) createDate.clone();
    }


    public Date getUpdateDate()
    {
        return updateDate == null ? null : (Date) updateDate.clone();
    }


    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate == null ? null : (Date) updateDate.clone();
    }


    @Override
    public String getCustomIdentification()
    {
        return areaCode;
    }

}
