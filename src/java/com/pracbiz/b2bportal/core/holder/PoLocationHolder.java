package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class PoLocationHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = 6048671052424682498L;

    private BigDecimal poOid;

    private Integer lineSeqNo;

    private String locationCode;

    private String locationName;

    private String locationAddr1;

    private String locationAddr2;

    private String locationAddr3;

    private String locationAddr4;

    private String locationCity;

    private String locationState;

    private String locationCtryCode;

    private String locationPostalCode;
    
    private String locationContactTel;

    public BigDecimal getPoOid() {
        return poOid;
    }

    public void setPoOid(BigDecimal poOid) {
        this.poOid = poOid;
    }

    public Integer getLineSeqNo() {
        return lineSeqNo;
    }

    public void setLineSeqNo(Integer lineSeqNo) {
        this.lineSeqNo = lineSeqNo;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode == null ? null : locationCode.trim();
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName == null ? null : locationName.trim();
    }

    public String getLocationAddr1() {
        return locationAddr1;
    }

    public void setLocationAddr1(String locationAddr1) {
        this.locationAddr1 = locationAddr1 == null ? null : locationAddr1.trim();
    }

    public String getLocationAddr2() {
        return locationAddr2;
    }

    public void setLocationAddr2(String locationAddr2) {
        this.locationAddr2 = locationAddr2 == null ? null : locationAddr2.trim();
    }

    public String getLocationAddr3() {
        return locationAddr3;
    }

    public void setLocationAddr3(String locationAddr3) {
        this.locationAddr3 = locationAddr3 == null ? null : locationAddr3.trim();
    }

    public String getLocationAddr4() {
        return locationAddr4;
    }

    public void setLocationAddr4(String locationAddr4) {
        this.locationAddr4 = locationAddr4 == null ? null : locationAddr4.trim();
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity == null ? null : locationCity.trim();
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState == null ? null : locationState.trim();
    }

    public String getLocationCtryCode() {
        return locationCtryCode;
    }

    public void setLocationCtryCode(String locationCtryCode) {
        this.locationCtryCode = locationCtryCode == null ? null : locationCtryCode.trim();
    }

    public String getLocationPostalCode() {
        return locationPostalCode;
    }

    public void setLocationPostalCode(String locationPostalCode) {
        this.locationPostalCode = locationPostalCode == null ? null : locationPostalCode.trim();
    }
    
    public String getLocationContactTel()
    {
        return locationContactTel;
    }

    public void setLocationContactTel(String locationContactTel)
    {
        this.locationContactTel = locationContactTel;
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(poOid + "" + lineSeqNo);
    }
}