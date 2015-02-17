package com.pracbiz.b2bportal.core.holder;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class LogFileHolder extends BaseHolder
{

    private static final long serialVersionUID = 4479414697561087532L;
    private String fileName;
    private Long fileSize;
    private Date lastModifiedTime;
    private Integer totalLine;
    private String hashCode;

    private String searchFileName;
    private Date searchLastModifiedFrom;
    private Date searchLastModifiedTo;
    private String stringContent;

    public String getFileName()
    {
        return fileName;
    }


    public void setFileName(String fileName)
    {
        this.fileName = fileName == null ? null : fileName.trim();
    }


    public Long getFileSize()
    {
        return fileSize;
    }


    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    
    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Date getLastModifiedTime()
    {
        return lastModifiedTime == null ? null : (Date) lastModifiedTime
                .clone();
    }


    public void setLastModifiedTime(Date lastModifiedTime)
    {
        this.lastModifiedTime = lastModifiedTime == null ? null
                : (Date) lastModifiedTime.clone();
    }


    public Integer getTotalLine()
    {
        return totalLine;
    }


    public void setTotalLine(Integer totalLine)
    {
        this.totalLine = totalLine;
    }


    public String getSearchFileName()
    {
        return searchFileName;
    }


    public void setSearchFileName(String searchFileName)
    {
        this.searchFileName = searchFileName == null ? null : searchFileName.trim();
    }


    public Date getSearchLastModifiedFrom()
    {
        return searchLastModifiedFrom == null ? null : (Date) searchLastModifiedFrom
                .clone();
    }


    public void setSearchLastModifiedFrom(Date searchLastModifiedFrom)
    {
        this.searchLastModifiedFrom = searchLastModifiedFrom == null ? null : (Date) searchLastModifiedFrom
                .clone();
    }


    public Date getSearchLastModifiedTo()
    {
        return searchLastModifiedTo == null ? null : (Date) searchLastModifiedTo
                .clone();
    }


    public void setSearchLastModifiedTo(Date searchLastModifiedTo)
    {
        this.searchLastModifiedTo = searchLastModifiedTo == null ? null : (Date) searchLastModifiedTo
                .clone();
    }


    @Override
    public String getCustomIdentification()
    {
        return fileName == null ? null : fileName;
    }


    public String getStringContent()
    {
        return stringContent;
    }


    public void setStringContent(String stringContent)
    {
        this.stringContent = stringContent == null ? null : stringContent.trim();
    }


    public String getHashCode()
    {
        return hashCode;
    }


    public void setHashCode(String hashCode)
    {
        this.hashCode = hashCode == null ? null : hashCode.trim();
    }

}
