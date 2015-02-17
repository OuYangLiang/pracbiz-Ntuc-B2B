package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class SummaryPageSettingHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -4743207122243356657L;

    private BigDecimal settingOid;

    private String pageId;

    private String accessType;

    private String pageDesc;

    public BigDecimal getSettingOid() {
        return settingOid;
    }

    public void setSettingOid(BigDecimal settingOid) {
        this.settingOid = settingOid;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId == null ? null : pageId.trim();
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType == null ? null : accessType.trim();
    }

    public String getPageDesc() {
        return pageDesc;
    }

    public void setPageDesc(String pageDesc) {
        this.pageDesc = pageDesc == null ? null : pageDesc.trim();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(settingOid);
    }
}