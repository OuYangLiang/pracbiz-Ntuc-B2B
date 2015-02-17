package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class PoInvMappingHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = 1109941652018835781L;

    private BigDecimal poOid;

    private BigDecimal invOid;

    public BigDecimal getPoOid() {
        return poOid;
    }

    public void setPoOid(BigDecimal poOid) {
        this.poOid = poOid;
    }

    public BigDecimal getInvOid() {
        return invOid;
    }

    public void setInvOid(BigDecimal invOid) {
        this.invOid = invOid;
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(poOid + "" + invOid);
    }
}