package com.pracbiz.b2bportal.core.holder;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;

public class BuyerHolder extends BaseHolder
{
    private static final long serialVersionUID = -5453200690984047074L;

    private BigDecimal buyerOid;

    private String buyerCode;

    private String buyerName;

    private String buyerAlias;

    private String regNo;

    private String gstRegNo;

    private BigDecimal gstPercent;

    private String otherRegNo;

    private Boolean branch;

    private String address1;

    private String address2;

    private String address3;

    private String address4;

    private String state;

    private String postalCode;

    private String ctryCode;

    private String currCode;

    private String contactName;

    private String contactTel;

    private String contactMobile;

    private String contactFax;

    private String contactEmail;

    private Boolean active;

    private Boolean blocked;

    private String blockRemarks;

    private Date createDate;

    private String createBy;

    private Date inactivateDate;

    private String inactivateBy;

    private String updateBy;

    private Date updateDate;

    private Date blockDate;

    private String blockBy;

    private String mboxId;

    private String channel;
    
    private DeploymentMode deploymentMode;

    private byte[] logo;
    
    private List<BuyerMsgSettingHolder> msgSetting;
    
    private List<BuyerOperationHolder> buyerOperations;
    
    private List<BuyerGivenSupplierOperationHolder> buyerGivenSupplierOperations;
    
    private List<BuyerRuleHolder> buyerRules;
    
    
    public void addBuyerOperation(BuyerOperationHolder buyerOperationHolder)
    {
        if(null == buyerOperations)
        {
            buyerOperations = new LinkedList<BuyerOperationHolder>();
        }

        buyerOperations.add(buyerOperationHolder);
    }

    public void addBuyerGivenSupplierOperation(
        BuyerGivenSupplierOperationHolder buyerGivenSupplierOperationHolder)
    {
        if(null == buyerGivenSupplierOperations)
        {
            buyerGivenSupplierOperations = new LinkedList<BuyerGivenSupplierOperationHolder>();
        }
        
        buyerGivenSupplierOperations.add(buyerGivenSupplierOperationHolder);
    }
    
    //*************************************
    //get buy logo
    //*************************************
    public  Image initBuyerLogo()
    {
        Image image = null;
        InputStream logos = null;
        try
        {
            if(null == this.getLogo() || 0 == this.getLogo().length)
            {
                return null;
            }   
            
            logos = new ByteArrayInputStream(this.getLogo());
            image = ImageIO.read(logos);
            logos.reset();
        }
        catch (Exception e)
        {
            log.error( ":::: Faild to convert byte[] from database to image for company logo.",e);
        }
        finally
        {
            try
            {
                if (logos != null)
                {
                    logos.close();
                }
            }
            catch (IOException e)
            {
                log.error(log + " :::: Faild to close InputStream.",e);
            }
        }

        return image;
    }


    public List<BuyerOperationHolder> getBuyerOperations()
    {
        return buyerOperations;
    }


    public void setBuyerOperations(List<BuyerOperationHolder> buyerOperations)
    {
        this.buyerOperations = buyerOperations;
    }


    public List<BuyerGivenSupplierOperationHolder> getBuyerGivenSupplierOperations()
    {
        return buyerGivenSupplierOperations;
    }


    public void setBuyerGivenSupplierOperations(
        List<BuyerGivenSupplierOperationHolder> buyerGivenSupplierOperations)
    {
        this.buyerGivenSupplierOperations = buyerGivenSupplierOperations;
    }


    public List<BuyerMsgSettingHolder> getMsgSetting()
    {
        return msgSetting;
    }


    public void setMsgSetting(List<BuyerMsgSettingHolder> msgSetting)
    {
        this.msgSetting = msgSetting;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode == null ? null : buyerCode.trim();
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }


    public String getBuyerAlias()
    {
        return buyerAlias;
    }


    public void setBuyerAlias(String buyerAlias)
    {
        this.buyerAlias = buyerAlias == null ? null : buyerAlias.trim();
    }


    public String getRegNo()
    {
        return regNo;
    }


    public void setRegNo(String regNo)
    {
        this.regNo = regNo == null ? null : regNo.trim();
    }


    public String getGstRegNo()
    {
        return gstRegNo;
    }


    public void setGstRegNo(String gstRegNo)
    {
        this.gstRegNo = gstRegNo == null ? null : gstRegNo.trim();
    }


    public BigDecimal getGstPercent()
    {
        return gstPercent;
    }


    public void setGstPercent(BigDecimal gstPercent)
    {
        this.gstPercent = gstPercent;
    }


    public String getOtherRegNo()
    {
        return otherRegNo;
    }


    public void setOtherRegNo(String otherRegNo)
    {
        this.otherRegNo = otherRegNo == null ? null : otherRegNo.trim();
    }


    public Boolean getBranch()
    {
        return branch;
    }


    public void setBranch(Boolean branch)
    {
        this.branch = branch;
    }


    public String getAddress1()
    {
        return address1;
    }


    public void setAddress1(String address1)
    {
        this.address1 = address1 == null ? null : address1.trim();
    }


    public String getAddress2()
    {
        return address2;
    }


    public void setAddress2(String address2)
    {
        this.address2 = address2 == null ? null : address2.trim();
    }


    public String getAddress3()
    {
        return address3;
    }


    public void setAddress3(String address3)
    {
        this.address3 = address3 == null ? null : address3.trim();
    }


    public String getAddress4()
    {
        return address4;
    }


    public void setAddress4(String address4)
    {
        this.address4 = address4 == null ? null : address4.trim();
    }


    public String getState()
    {
        return state;
    }


    public void setState(String state)
    {
        this.state = state == null ? null : state.trim();
    }


    public String getPostalCode()
    {
        return postalCode;
    }


    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode == null ? null : postalCode.trim();
    }


    public String getCtryCode()
    {
        return ctryCode;
    }


    public void setCtryCode(String ctryCode)
    {
        this.ctryCode = ctryCode == null ? null : ctryCode.trim();
    }


    public String getCurrCode()
    {
        return currCode;
    }


    public void setCurrCode(String currCode)
    {
        this.currCode = currCode == null ? null : currCode.trim();
    }


    public String getContactName()
    {
        return contactName;
    }


    public void setContactName(String contactName)
    {
        this.contactName = contactName == null ? null : contactName.trim();
    }


    public String getContactTel()
    {
        return contactTel;
    }


    public void setContactTel(String contactTel)
    {
        this.contactTel = contactTel == null ? null : contactTel.trim();
    }


    public String getContactMobile()
    {
        return contactMobile;
    }


    public void setContactMobile(String contactMobile)
    {
        this.contactMobile = contactMobile == null ? null : contactMobile
                .trim();
    }


    public String getContactFax()
    {
        return contactFax;
    }


    public void setContactFax(String contactFax)
    {
        this.contactFax = contactFax == null ? null : contactFax.trim();
    }


    public String getContactEmail()
    {
        return contactEmail;
    }


    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail == null ? null : contactEmail.trim();
    }


    public Boolean getActive()
    {
        return active;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getBlocked()
    {
        return blocked;
    }


    public void setBlocked(Boolean blocked)
    {
        this.blocked = blocked;
    }


    public String getBlockRemarks()
    {
        return blockRemarks;
    }


    public void setBlockRemarks(String blockRemarks)
    {
        this.blockRemarks = blockRemarks == null ? null : blockRemarks.trim();
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date) createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date) createDate.clone();
    }


    public String getCreateBy()
    {
        return createBy;
    }


    public void setCreateBy(String createBy)
    {
        this.createBy = createBy == null ? null : createBy.trim();
    }


    public Date getInactivateDate()
    {
        return inactivateDate == null ? null : (Date)inactivateDate.clone();
    }


    public void setInactivateDate(Date inactivateDate)
    {
        this.inactivateDate = inactivateDate == null ? null : (Date)inactivateDate.clone();
    }


    public String getInactivateBy()
    {
        return inactivateBy;
    }


    public void setInactivateBy(String inactivateBy)
    {
        this.inactivateBy = inactivateBy == null ? null : inactivateBy.trim();
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


    public Date getBlockDate()
    {
        return blockDate == null ? null : (Date)blockDate.clone();
    }


    public void setBlockDate(Date blockDate)
    {
        this.blockDate = blockDate == null ? null : (Date)blockDate.clone();
    }


    public String getBlockBy()
    {
        return blockBy;
    }


    public void setBlockBy(String blockBy)
    {
        this.blockBy = blockBy == null ? null : blockBy.trim();
    }


    public String getMboxId()
    {
        return mboxId;
    }


    public void setMboxId(String mboxId)
    {
        this.mboxId = mboxId == null ? null : mboxId.trim();
    }


    public String getChannel()
    {
        return channel;
    }


    public void setChannel(String channel)
    {
        this.channel = channel == null ? null : channel.trim();
    }


    public byte[] getLogo()
    {
        return logo == null ? null : (byte[])logo.clone();
    }


    public void setLogo(byte[] logo)
    {
        this.logo = logo == null ? null : (byte[])logo.clone();
    }
    
    
    public void addBuyerMsgSetting(BuyerMsgSettingHolder buyerMsgSetting)
    {
        if(msgSetting == null)
        {
            msgSetting = new ArrayList<BuyerMsgSettingHolder>();
        }
        msgSetting.add(buyerMsgSetting);
    }

    public DeploymentMode getDeploymentMode()
    {
        return deploymentMode;
    }


    public void setDeploymentMode(DeploymentMode deploymentMode)
    {
        this.deploymentMode = deploymentMode;
    }


    public List<BuyerRuleHolder> getBuyerRules()
    {
        return buyerRules;
    }

    public void setBuyerRules(List<BuyerRuleHolder> buyerRules)
    {
        this.buyerRules = buyerRules;
    }

    @Override
    public String getCustomIdentification()
    {
        return buyerOid == null ? null : buyerOid.toString();
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return buyerCode;
    }

    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((buyerOid == null) ? 0 : buyerOid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        BuyerHolder other = (BuyerHolder)obj;
        if(buyerOid == null)
        {
            if(other.buyerOid != null)
                return false;
        }
        else if(!buyerOid.equals(other.buyerOid))
            return false;
        return true;
    }

}