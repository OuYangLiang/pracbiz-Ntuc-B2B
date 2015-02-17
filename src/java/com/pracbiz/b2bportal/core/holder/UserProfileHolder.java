package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.holder.extension.FavouriteListExHolder;

public class UserProfileHolder extends BaseHolder
{
    private static final long serialVersionUID = -4274516987973102329L;

    private BigDecimal userOid;

    private String userName;

    private String salutation;

    private String loginId;

    private String loginPwd;

    private String loginMode;

    private String gender;

    private String tel;

    private String mobile;

    private String fax;

    private String email;

    private Integer failAttempts;

    private Date lastResetPwdDate;

    private Date lastLoginDate;

    private Boolean active;

    private Boolean blocked;

    private String blockRemarks;

    private Boolean init;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;

    private Date blockDate;

    private String blockBy;

    private Date inactivateDate;

    private String inactivateBy;

    private BigDecimal userType;

    private BigDecimal buyerOid;

    private BigDecimal supplierOid;

    private List<RoleUserHolder> roleUsers;
    
    private List<GroupUserHolder> groupUsers;
    
    private List<BuyerStoreUserHolder> buyerStoreUsers;
    
    private List<BuyerStoreUserHolder> buyerWareHouseUsers;
    
    private List<BuyerStoreAreaUserHolder> buyerStoreAreaUsers;
    
    private List<AllowedAccessCompanyHolder> allowedBuyerList;
    
    private List<FavouriteListExHolder> favouriteLists;
    
    private List<UserClassHolder> userClassList;
    
    private List<UserSubclassHolder> userSubclassList;

    public BigDecimal getUserOid()
    {
        return userOid;
    }


    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }


    public String getUserName()
    {
        return userName;
    }


    public void setUserName(String userName)
    {
        this.userName = userName == null ? null : userName.trim();
    }


    public String getSalutation()
    {
        return salutation;
    }


    public void setSalutation(String salutation)
    {
        this.salutation = salutation == null ? null : salutation.trim();
    }


    public String getLoginId()
    {
        return loginId;
    }


    public void setLoginId(String loginId)
    {
        this.loginId = loginId == null ? null : loginId.trim();
    }


    public String getLoginPwd()
    {
        return loginPwd;
    }


    public void setLoginPwd(String loginPwd)
    {
        this.loginPwd = loginPwd == null ? null : loginPwd.trim();
    }


    public String getLoginMode()
    {
        return loginMode;
    }


    public void setLoginMode(String loginMode)
    {
        this.loginMode = loginMode == null ? null : loginMode.trim();
    }


    public String getGender()
    {
        return gender;
    }


    public void setGender(String gender)
    {
        this.gender = gender == null ? null : gender.trim();
    }


    public String getTel()
    {
        return tel;
    }


    public void setTel(String tel)
    {
        this.tel = tel == null ? null : tel.trim();
    }


    public String getMobile()
    {
        return mobile;
    }


    public void setMobile(String mobile)
    {
        this.mobile = mobile == null ? null : mobile.trim();
    }


    public String getFax()
    {
        return fax;
    }


    public void setFax(String fax)
    {
        this.fax = fax == null ? null : fax.trim();
    }


    public String getEmail()
    {
        return email;
    }


    public void setEmail(String email)
    {
        this.email = email == null ? null : email.trim();
    }


    public Integer getFailAttempts()
    {
        return failAttempts;
    }


    public void setFailAttempts(Integer failAttempts)
    {
        this.failAttempts = failAttempts;
    }


    public Date getLastResetPwdDate()
    {
        return lastResetPwdDate == null ? null : (Date)lastResetPwdDate.clone();
    }


    public void setLastResetPwdDate(Date lastResetPwdDate)
    {
        this.lastResetPwdDate = lastResetPwdDate == null ? null : (Date)lastResetPwdDate.clone();
    }


    public Date getLastLoginDate()
    {
        return lastLoginDate == null ? null : (Date)lastLoginDate.clone();
    }


    public void setLastLoginDate(Date lastLoginDate)
    {
        this.lastLoginDate = lastLoginDate == null ? null : (Date)lastLoginDate.clone();
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


    public Boolean getInit()
    {
        return init;
    }


    public void setInit(Boolean init)
    {
        this.init = init;
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


    public BigDecimal getUserType()
    {
        return userType;
    }


    public void setUserType(BigDecimal userType)
    {
        this.userType = userType;
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


    public List<RoleUserHolder> getRoleUsers()
    {
        return roleUsers;
    }


    public void setRoleUsers(List<RoleUserHolder> roleUsers)
    {
        this.roleUsers = roleUsers;
    }


    public void addRoleUser(RoleUserHolder roleUser)
    {
        if(roleUsers == null)
        {
            this.roleUsers = new ArrayList<RoleUserHolder>();
        }
        roleUsers.add(roleUser);
    }
    
    
    public List<GroupUserHolder> getGroupUsers()
    {
        return groupUsers;
    }


    public void setGroupUsers(List<GroupUserHolder> groupUsers)
    {
        this.groupUsers = groupUsers;
    }


    public List<BuyerStoreUserHolder> getBuyerStoreUsers()
    {
        return buyerStoreUsers;
    }


    public void setBuyerStoreUsers(List<BuyerStoreUserHolder> buyerStoreUsers)
    {
        this.buyerStoreUsers = buyerStoreUsers;
    }


    public List<BuyerStoreAreaUserHolder> getBuyerStoreAreaUsers()
    {
        return buyerStoreAreaUsers;
    }


    public void setBuyerStoreAreaUsers(
            List<BuyerStoreAreaUserHolder> buyerStoreAreaUsers)
    {
        this.buyerStoreAreaUsers = buyerStoreAreaUsers;
    }


    public List<BuyerStoreUserHolder> getBuyerWareHouseUsers()
    {
        return buyerWareHouseUsers;
    }


    public void setBuyerWareHouseUsers(
        List<BuyerStoreUserHolder> buyerWareHouseUsers)
    {
        this.buyerWareHouseUsers = buyerWareHouseUsers;
    }


    public List<AllowedAccessCompanyHolder> getAllowedBuyerList()
    {
        return allowedBuyerList;
    }


    public void setAllowedBuyerList(List<AllowedAccessCompanyHolder> allowedBuyerList)
    {
        this.allowedBuyerList = allowedBuyerList;
    }



    public List<FavouriteListExHolder> getFavouriteLists()
    {
        return favouriteLists;
    }


    public void setFavouriteLists(List<FavouriteListExHolder> favouriteLists)
    {
        this.favouriteLists = favouriteLists;
    }


    public List<UserClassHolder> getUserClassList()
    {
        return userClassList;
    }


    public void setUserClassList(List<UserClassHolder> userClassList)
    {
        this.userClassList = userClassList;
    }


    public List<UserSubclassHolder> getUserSubclassList()
    {
        return userSubclassList;
    }


    public void setUserSubclassList(List<UserSubclassHolder> userSubclassList)
    {
        this.userSubclassList = userSubclassList;
    }


    @Override
    public String getCustomIdentification()
    {
        return userOid == null ? null : userOid.toString();
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return loginId;
    }
    
    
    public void addUserClass(UserClassHolder userClass)
    {
        if (this.userClassList == null)
        {
            userClassList = new ArrayList<UserClassHolder>();
        }
        
        userClassList.add(userClass);
    }
    
    
    public void addUserSubclass(UserSubclassHolder userSubclass)
    {
        if (this.userSubclassList == null)
        {
            userSubclassList = new ArrayList<UserSubclassHolder>();
        }
        
        userSubclassList.add(userSubclass);
    }
}