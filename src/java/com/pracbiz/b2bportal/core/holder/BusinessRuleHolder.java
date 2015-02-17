package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class BusinessRuleHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -6712350190465648896L;

    private BigDecimal ruleOid;

    private String funcGroup;

    private String funcId;

    private String ruleId;

    private String ruleDesc;

    private String ruleDescKey;

    public BigDecimal getRuleOid() {
        return ruleOid;
    }

    public void setRuleOid(BigDecimal ruleOid) {
        this.ruleOid = ruleOid;
    }

    public String getFuncGroup() {
        return funcGroup;
    }

    public void setFuncGroup(String funcGroup) {
        this.funcGroup = funcGroup == null ? null : funcGroup.trim();
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId == null ? null : funcId.trim();
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc == null ? null : ruleDesc.trim();
    }

    public String getRuleDescKey() {
        return ruleDescKey;
    }

    public void setRuleDescKey(String ruleDescKey) {
        this.ruleDescKey = ruleDescKey == null ? null : ruleDescKey.trim();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(ruleOid);
    }
}