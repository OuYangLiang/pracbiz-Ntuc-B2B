package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SupplierMsgSettingHolder extends BaseHolder
{
    private static final long serialVersionUID = -6583809608484327472L;

    private String msgType;

    private BigDecimal supplierOid;

    private String rcpsAddrs;

    private Boolean excludeSucc;
    
    private String fileFormat;


    public String getMsgType()
    {
        return msgType;
    }


    public void setMsgType(String msgType)
    {
        this.msgType = msgType == null ? null : msgType.trim();
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    public String getRcpsAddrs()
    {
        return rcpsAddrs;
    }


    public void setRcpsAddrs(String rcpsAddrs)
    {
        this.rcpsAddrs = rcpsAddrs == null ? null : rcpsAddrs.trim();
    }


    public Boolean getExcludeSucc()
    {
        return excludeSucc;
    }


    public void setExcludeSucc(Boolean excludeSucc)
    {
        this.excludeSucc = excludeSucc;
    }


    public String getFileFormat()
    {
        return fileFormat;
    }


    public void setFileFormat(String fileFormat)
    {
        this.fileFormat = fileFormat;
    }

    public String[] retrieveRcpsAddrsByDelimiterChar(String delimiterChar)
    {
        if (StringUtils.isBlank(rcpsAddrs))
        {
            return null;
        }
        
        return StringUtils.split(rcpsAddrs, delimiterChar);
    }

    @Override
    public String getCustomIdentification()
    {
        return supplierOid == null ? null : supplierOid.toString() + msgType;
    }

}