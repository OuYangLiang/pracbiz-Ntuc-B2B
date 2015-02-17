package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class PoLocationDetailHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -3496685864823042701L;

    private BigDecimal poOid;

    private Integer locationLineSeqNo;

    private Integer detailLineSeqNo;

    private BigDecimal locationShipQty;

    private BigDecimal locationFocQty;
    
    private String lineRefNo;

    public BigDecimal getPoOid() {
        return poOid;
    }

    public void setPoOid(BigDecimal poOid) {
        this.poOid = poOid;
    }

    public Integer getLocationLineSeqNo() {
        return locationLineSeqNo;
    }

    public void setLocationLineSeqNo(Integer locationLineSeqNo) {
        this.locationLineSeqNo = locationLineSeqNo;
    }

    public Integer getDetailLineSeqNo() {
        return detailLineSeqNo;
    }

    public void setDetailLineSeqNo(Integer detailLineSeqNo) {
        this.detailLineSeqNo = detailLineSeqNo;
    }

    public BigDecimal getLocationShipQty() {
        return locationShipQty;
    }

    public void setLocationShipQty(BigDecimal locationShipQty) {
        this.locationShipQty = locationShipQty;
    }

    public BigDecimal getLocationFocQty() {
        return locationFocQty;
    }

    public void setLocationFocQty(BigDecimal locationFocQty) {
        this.locationFocQty = locationFocQty;
    }
    
    public String getLineRefNo()
    {
        return lineRefNo;
    }

    public void setLineRefNo(String lineRefNo)
    {
        this.lineRefNo = lineRefNo;
    }

    public String toStringWithDelimiterCharacter(String delimiterChar, PoLocationHolder location)
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(detailLineSeqNo).append(delimiterChar)
        .append(locationLineSeqNo).append(delimiterChar)
        .append(location.getLocationCode()).append(delimiterChar)
        .append(location.getLocationName()).append(delimiterChar)
        .append(locationShipQty).append(delimiterChar)
        .append(locationFocQty).append(delimiterChar)
        .append(lineRefNo);
        
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(poOid + "" + locationLineSeqNo + "" + detailLineSeqNo);
    }
}