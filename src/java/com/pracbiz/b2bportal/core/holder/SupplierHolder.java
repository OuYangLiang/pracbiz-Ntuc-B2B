package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.SupplierSourceType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class SupplierHolder extends BaseHolder
{
    private static final long serialVersionUID = 121547352583116273L;

    private BigDecimal supplierOid;

    private String supplierCode;

    private String supplierName;

    private String supplierAlias;
    
    private String transMode;
    
    private String regNo;
    
    private String gstRegNo;

    private BigDecimal gstPercent;

    private String otherRegNo;
    
    private SupplierSourceType supplierSource;

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

    private String updateBy;

    private Date updateDate;

    private Date blockDate;

    private String blockBy;

    private String mboxId;

    private Boolean autoInvNumber;

    private Integer startNumber;
    
    private String channel;
    
    private DeploymentMode deploymentMode;
    
    private Boolean clientEnabled;
    
    private Boolean requireReport;
    
    private Boolean requireTranslationIn;
    
    private Boolean requireTranslationOut;

    private byte[] logo;

    private List<SupplierMsgSettingHolder> msgSetting;
    
    private List<TermConditionHolder> termConditions;
    
    private List<SupplierRoleHolder> supplierRoles;
    
    private TermConditionHolder termCondition;
    
    private UserProfileHolder userProfile;
    
    private BigDecimal setOid;
    
    private Date liveDate;
    
    private List<SupplierSharedHolder> supplierShareds;
    
    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    public String getSupplierCode()
    {
        return supplierCode;
    }


    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }


    public String getSupplierAlias()
    {
        return supplierAlias;
    }


    public void setSupplierAlias(String supplierAlias)
    {
        this.supplierAlias = supplierAlias == null ? null : supplierAlias
                .trim();
    }
    
    
    public String getTransMode()
    {
        return transMode;
    }
    
    
    public void setTransMode(String transMode)
    {
        this.transMode = transMode == null ? null : transMode.trim();
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


    public SupplierSourceType getSupplierSource()
    {
        return supplierSource;
    }


    public void setSupplierSource(SupplierSourceType supplierSource)
    {
        this.supplierSource = supplierSource;
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


    public String getUpdateBy()
    {
        return updateBy;
    }


    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }


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


    public Boolean getAutoInvNumber()
    {
        return autoInvNumber;
    }


    public void setAutoInvNumber(Boolean autoInvNumber)
    {
        this.autoInvNumber = autoInvNumber;
    }


    public Integer getStartNumber()
    {
        return startNumber;
    }


    public void setStartNumber(Integer startNumber)
    {
        this.startNumber = startNumber;
    }


    public byte[] getLogo()
    {
        return logo == null ? null : (byte[])logo.clone();
    }


    public void setLogo(byte[] logo)
    {
        this.logo = logo == null ? null : (byte[])logo.clone();
    }


    @Override
    public String getCustomIdentification()
    {
        return supplierOid == null ? null : supplierOid.toString();
    }


    public List<SupplierMsgSettingHolder> getMsgSetting()
    {
        return msgSetting;
    }
    

    public void setMsgSetting(List<SupplierMsgSettingHolder> msgSetting)
    {
        this.msgSetting = msgSetting;
    }
    
    
    public void addSupplierMagSetting(SupplierMsgSettingHolder supplierMsgSetting)
    {
        if(msgSetting == null)
        {
            msgSetting = new ArrayList<SupplierMsgSettingHolder>();
        }
        msgSetting.add(supplierMsgSetting);
    }

    
    public List<TermConditionHolder> getTermConditions()
    {
        return termConditions;
    }


    public void setTermConditions(List<TermConditionHolder> termConditions)
    {
        this.termConditions = termConditions;
    }


    public void addTermCondition(TermConditionHolder termCondition)
    {
        if(termConditions == null)
        {
            this.termConditions = new ArrayList<TermConditionHolder>();
        }
        termConditions.add(termCondition);
    }

    
    public List<SupplierRoleHolder> getSupplierRoles()
    {
        return supplierRoles;
    }


    public void setSupplierRoles(List<SupplierRoleHolder> supplierRoles)
    {
        this.supplierRoles = supplierRoles;
    }


    public void addSupplierRole(SupplierRoleHolder supplierRole)
    {
        if(supplierRoles == null)
        {
            this.supplierRoles = new ArrayList<SupplierRoleHolder>();
        }
        supplierRoles.add(supplierRole);
    }
    

    public String getChannel()
    {
        return channel;
    }


    public void setChannel(String channel)
    {
        this.channel = channel;
    }


    public DeploymentMode getDeploymentMode()
    {
        return deploymentMode;
    }


    public void setDeploymentMode(DeploymentMode deploymentMode)
    {
        this.deploymentMode = deploymentMode;
    }


    public Boolean getClientEnabled()
    {
        return clientEnabled;
    }


    public void setClientEnabled(Boolean clientEnabled)
    {
        this.clientEnabled = clientEnabled;
    }


    public Boolean getRequireReport()
    {
        return requireReport;
    }


    public void setRequireReport(Boolean requireReport)
    {
        this.requireReport = requireReport;
    }


    public Boolean getRequireTranslationIn()
    {
        return requireTranslationIn;
    }


    public void setRequireTranslationIn(Boolean requireTranslationIn)
    {
        this.requireTranslationIn = requireTranslationIn;
    }


    public Boolean getRequireTranslationOut()
    {
        return requireTranslationOut;
    }


    public void setRequireTranslationOut(Boolean requireTranslationOut)
    {
        this.requireTranslationOut = requireTranslationOut;
    }


    public TermConditionHolder getTermCondition()
    {
        return termCondition;
    }


    public void setTermCondition(TermConditionHolder termCondition)
    {
        this.termCondition = termCondition;
    }


    public UserProfileHolder getUserProfile()
    {
        return userProfile;
    }


    public void setUserProfile(UserProfileHolder userProfile)
    {
        this.userProfile = userProfile;
    }


    public BigDecimal getSetOid()
    {
        return setOid;
    }


    public void setSetOid(BigDecimal setOid)
    {
        this.setOid = setOid;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getLiveDate()
    {
        return liveDate == null ? null : (Date)liveDate.clone();
    }


    public void setLiveDate(Date liveDate)
    {
        this.liveDate = liveDate == null ? null : (Date)liveDate.clone();
    }


    public List<SupplierSharedHolder> getSupplierShareds()
    {
        return supplierShareds;
    }


    public void setSupplierShareds(List<SupplierSharedHolder> supplierShareds)
    {
        this.supplierShareds = supplierShareds;
    }


    public void addSupplierShared(SupplierSharedHolder supplierShared)
    {
        if (this.supplierShareds == null)
        {
            this.supplierShareds = new ArrayList<SupplierSharedHolder>();
        }
        this.supplierShareds.add(supplierShared);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((supplierOid == null) ? 0 : supplierOid.hashCode());
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
        SupplierHolder other = (SupplierHolder)obj;
        if(supplierOid == null)
        {
            if(other.supplierOid != null)
                return false;
        }
        else if(!supplierOid.equals(other.supplierOid))
            return false;
        return true;
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return supplierCode;
    }
    
}