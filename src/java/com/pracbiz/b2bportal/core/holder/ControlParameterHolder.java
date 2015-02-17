package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class ControlParameterHolder extends BaseHolder
{
    private static final long serialVersionUID = -7704089437269449852L;

    private BigDecimal paramOid;

    private String sectId;

    private String paramId;

    private String catId;

    private String paramDesc;

    private String stringValue;

    private Integer numValue;

    private Date dateValue;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;

    private Boolean valid;


    public BigDecimal getParamOid()
    {
        return paramOid;
    }


    public void setParamOid(BigDecimal paramOid)
    {
        this.paramOid = paramOid;
    }


    public String getSectId()
    {
        return sectId;
    }


    public void setSectId(String sectId)
    {
        this.sectId = sectId == null ? null : sectId.trim();
    }


    public String getParamId()
    {
        return paramId;
    }


    public void setParamId(String paramId)
    {
        this.paramId = paramId == null ? null : paramId.trim();
    }


    public String getCatId()
    {
        return catId;
    }


    public void setCatId(String catId)
    {
        this.catId = catId == null ? null : catId.trim();
    }


    public String getParamDesc()
    {
        return paramDesc;
    }


    public void setParamDesc(String paramDesc)
    {
        this.paramDesc = paramDesc == null ? null : paramDesc.trim();
    }


    public String getStringValue()
    {
        return stringValue;
    }


    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue == null ? null : stringValue.trim();
    }


    public Integer getNumValue()
    {
        return numValue;
    }


    public void setNumValue(Integer numValue)
    {
        this.numValue = numValue;
    }


    public Date getDateValue()
    {
        return dateValue == null ? null : (Date)dateValue.clone();
    }


    public void setDateValue(Date dateValue)
    {
        this.dateValue = dateValue == null ? null : (Date)dateValue.clone();
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


    public Boolean getValid()
    {
        return valid;
    }


    public void setValid(Boolean valid)
    {
        this.valid = valid;
    }


    @Override
    public String getCustomIdentification()
    {
        return paramOid == null ? null : paramOid.toString();
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return this.sectId + "_" + this.getParamId();
    }

}