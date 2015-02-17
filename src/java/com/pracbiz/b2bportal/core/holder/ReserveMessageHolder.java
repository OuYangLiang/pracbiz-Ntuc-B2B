package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class ReserveMessageHolder extends BaseHolder
{
    private static final long serialVersionUID = -1020789102250779908L;

    private BigDecimal rsrvMsgOid;

    private String title;

    private String content;

    private Date validFrom;

    private Date validTo;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;
    
    private String announcementType;


    public BigDecimal getRsrvMsgOid()
    {
        return rsrvMsgOid;
    }


    public void setRsrvMsgOid(BigDecimal rsrvMsgOid)
    {
        this.rsrvMsgOid = rsrvMsgOid;
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title == null ? null : title.trim();
    }


    public String getContent()
    {
        return content;
    }


    public void setContent(String content)
    {
        this.content = content == null ? null : content.trim();
    }

    @JSON(format="yyyy-MM-dd")
    public Date getValidFrom()
    {
        return validFrom == null ? null : (Date)validFrom.clone();
    }


    public void setValidFrom(Date validFrom)
    {
        this.validFrom = validFrom == null ? null : (Date)validFrom.clone();
    }

    @JSON(format="yyyy-MM-dd")
    public Date getValidTo()
    {
        return validTo == null ? null : (Date)validTo.clone();
    }

    
    public void setValidTo(Date validTo)
    {
        this.validTo = validTo == null ? null : (Date)validTo.clone();
    }


    public String getCreateBy()
    {
        return createBy;
    }


    public void setCreateBy(String createBy)
    {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    
    public Date getCreateDate()
    {
        return createDate == null ? null : (Date)createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date)createDate.clone();
    }


    public String getUpdateBy()
    {
        return updateBy;
    }


    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy == null ? null : updateBy.trim();
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


    @Override
    public String getCustomIdentification()
    {
        return rsrvMsgOid == null ? null : rsrvMsgOid.toString();
    }


    public String getAnnouncementType()
    {
        return announcementType;
    }


    public void setAnnouncementType(String announcementType)
    {
        this.announcementType = announcementType;
    }
    
}