package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class BuyerMsgSettingHolder extends BaseHolder
{
    private static final long serialVersionUID = 9172913996376330943L;

    private BigDecimal buyerOid;

    private String msgType;

    private String alertFrequency;

    private Short alertInterval;

    private String rcpsAddrs;
    
    private String errorRcpsAddrs;

    private Boolean excludeSucc;
    
    private String fileFormat;


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public String getMsgType()
    {
        return msgType;
    }


    public void setMsgType(String msgType)
    {
        this.msgType = msgType == null ? null : msgType.trim();
    }


    public String getAlertFrequency()
    {
        return alertFrequency;
    }


    public void setAlertFrequency(String alertFrequency)
    {
        this.alertFrequency = alertFrequency == null ? null : alertFrequency
                .trim();
    }


    public Short getAlertInterval()
    {
        return alertInterval;
    }


    public void setAlertInterval(Short alertInterval)
    {
        this.alertInterval = alertInterval;
    }


    public String getRcpsAddrs()
    {
        return rcpsAddrs;
    }


    public void setRcpsAddrs(String rcpsAddrs)
    {
        this.rcpsAddrs = rcpsAddrs == null ? null : rcpsAddrs.trim();
    }


    public String getErrorRcpsAddrs()
    {
        return errorRcpsAddrs;
    }


    public void setErrorRcpsAddrs(String errorRcpsAddrs)
    {
        this.errorRcpsAddrs = errorRcpsAddrs == null ? null : errorRcpsAddrs.trim();
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
    
    
    public String[] retrieveErrorRcpsAddrsByDelimiterChar(String delimiterChar)
    {
        if (StringUtils.isBlank(errorRcpsAddrs))
        {
            return null;
        }
        
        return StringUtils.split(errorRcpsAddrs, delimiterChar);
    }


    @Override
    public String getCustomIdentification()
    {
        return buyerOid == null ? null : buyerOid.toString() + msgType;
    }
}