package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */
public class RtvLocationDetailHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = 4895474153620141360L;

    private BigDecimal rtvOid;

    private Integer locationLineSeqNo;

    private Integer detailLineSeqNo;

    private BigDecimal locationShipQty;

    private BigDecimal locationFocQty;


    public BigDecimal getRtvOid()
    {
        return rtvOid;
    }

    public void setRtvOid(BigDecimal rtvOid)
    {
        this.rtvOid = rtvOid;
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
    
    public String toStringWithDelimiterCharacter(String delimiterChar, RtvLocationHolder location)
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(detailLineSeqNo).append(delimiterChar)
        .append(locationLineSeqNo).append(delimiterChar)
        .append(location.getLocationCode()).append(delimiterChar)
        .append(location.getLocationName()).append(delimiterChar)
        .append(locationShipQty).append(delimiterChar)
        .append(locationFocQty);
        
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(rtvOid + "" + locationLineSeqNo + "" + detailLineSeqNo);
    }
}