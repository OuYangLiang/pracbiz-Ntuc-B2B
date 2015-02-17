package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class TradingPartnerHolder extends BaseHolder
{
    private static final long serialVersionUID = 4078465459745506468L;

    private BigDecimal tradingPartnerOid;

    private String buyerSupplierCode;

    private String supplierBuyerCode;

    private String buyerContactPerson;

    private String buyerContactTel;

    private String buyerContactMobile;

    private String buyerContactFax;

    private String buyerContactEmail;

    private String supplierContactPerson;

    private String supplierContactTel;

    private String supplierContactMobile;

    private String supplierContactFax;

    private String supplierContactEmail;

    private Boolean active;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;

    private Boolean concessive;

    private BigDecimal buyerOid;

    private BigDecimal supplierOid;

    private BigDecimal termConditionOid;

    private List<GroupTradingPartnerHolder> groupTradingPartners;

    public BigDecimal getTradingPartnerOid()
    {
        return tradingPartnerOid;
    }


    public void setTradingPartnerOid(BigDecimal tpOid)
    {
        this.tradingPartnerOid = tpOid;
    }


    public String getBuyerSupplierCode()
    {
        return buyerSupplierCode;
    }


    public void setBuyerSupplierCode(String buyerSupplierCode)
    {
        this.buyerSupplierCode = buyerSupplierCode == null ? null
                : buyerSupplierCode.trim();
    }


    public String getSupplierBuyerCode()
    {
        return supplierBuyerCode;
    }


    public void setSupplierBuyerCode(String supplierBuyerCode)
    {
        this.supplierBuyerCode = supplierBuyerCode == null ? null
                : supplierBuyerCode.trim();
    }


    public String getBuyerContactPerson()
    {
        return buyerContactPerson;
    }


    public void setBuyerContactPerson(String buyerContactPerson)
    {
        this.buyerContactPerson = buyerContactPerson == null ? null
                : buyerContactPerson.trim();
    }


    public String getBuyerContactTel()
    {
        return buyerContactTel;
    }


    public void setBuyerContactTel(String buyerContactTel)
    {
        this.buyerContactTel = buyerContactTel == null ? null : buyerContactTel
                .trim();
    }


    public String getBuyerContactMobile()
    {
        return buyerContactMobile;
    }


    public void setBuyerContactMobile(String buyerContactMobile)
    {
        this.buyerContactMobile = buyerContactMobile == null ? null
                : buyerContactMobile.trim();
    }


    public String getBuyerContactFax()
    {
        return buyerContactFax;
    }


    public void setBuyerContactFax(String buyerContactFax)
    {
        this.buyerContactFax = buyerContactFax == null ? null : buyerContactFax
                .trim();
    }


    public String getBuyerContactEmail()
    {
        return buyerContactEmail;
    }


    public void setBuyerContactEmail(String buyerContactEmail)
    {
        this.buyerContactEmail = buyerContactEmail == null ? null
                : buyerContactEmail.trim();
    }


    public String getSupplierContactPerson()
    {
        return supplierContactPerson;
    }


    public void setSupplierContactPerson(String supplierContactPerson)
    {
        this.supplierContactPerson = supplierContactPerson == null ? null
                : supplierContactPerson.trim();
    }


    public String getSupplierContactTel()
    {
        return supplierContactTel;
    }


    public void setSupplierContactTel(String supplierContactTel)
    {
        this.supplierContactTel = supplierContactTel == null ? null
                : supplierContactTel.trim();
    }


    public String getSupplierContactMobile()
    {
        return supplierContactMobile;
    }


    public void setSupplierContactMobile(String supplierContactMobile)
    {
        this.supplierContactMobile = supplierContactMobile == null ? null
                : supplierContactMobile.trim();
    }


    public String getSupplierContactFax()
    {
        return supplierContactFax;
    }


    public void setSupplierContactFax(String supplierContactFax)
    {
        this.supplierContactFax = supplierContactFax == null ? null
                : supplierContactFax.trim();
    }


    public String getSupplierContactEmail()
    {
        return supplierContactEmail;
    }


    public void setSupplierContactEmail(String supplierContactEmail)
    {
        this.supplierContactEmail = supplierContactEmail == null ? null
                : supplierContactEmail.trim();
    }


    public Boolean getActive()
    {
        return active;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date)createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date)createDate.clone();
    }


    public String getCreateBy()
    {
        return createBy;
    }


    public void setCreateBy(String createBy)
    {
        this.createBy = createBy == null ? null : createBy.trim();
    }


    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate()
    {
        return updateDate == null ? null : (Date)updateDate.clone();
    }


    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate == null ? null : (Date)updateDate.clone();
    }


    public String getUpdateBy()
    {
        return updateBy;
    }


    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }


    public Boolean getConcessive()
    {
        return concessive;
    }


    public void setConcessive(Boolean concessive)
    {
        this.concessive = concessive;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    public BigDecimal getTermConditionOid()
    {
        return termConditionOid;
    }


    public void setTermConditionOid(BigDecimal termConditionOid)
    {
        this.termConditionOid = termConditionOid;
    }


    @Override
    public String getCustomIdentification()
    {
        return tradingPartnerOid == null ? null : tradingPartnerOid.toString();
    }


    public List<GroupTradingPartnerHolder> getGroupTradingPartners()
    {
        return groupTradingPartners;
    }


    public void setGroupTradingPartners(
            List<GroupTradingPartnerHolder> groupTradingPartners)
    {
        this.groupTradingPartners = groupTradingPartners;
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return this.buyerOid + "_" + this.getBuyerSupplierCode();
    }

}