package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;







import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;







import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.extension.BusinessRuleExHolder;
import com.pracbiz.b2bportal.core.mapper.BusinessRuleMapper;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class BusinessRuleServiceImpl extends
    DBActionServiceDefaultImpl<BusinessRuleHolder> implements
    BusinessRuleService, CoreCommonConstants
{
    @Autowired transient private BusinessRuleMapper mapper;
    
    
    @Override
    public void insert(BusinessRuleHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
        
    }

    @Override
    public void updateByPrimaryKeySelective(BusinessRuleHolder oldObj_,
        BusinessRuleHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
        
    }

    @Override
    public void updateByPrimaryKey(BusinessRuleHolder oldObj_,
        BusinessRuleHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
        
    }

    @Override
    public void delete(BusinessRuleHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
        
    }

    @Override
    public List<BusinessRuleHolder> selectRulesByBuyerOidAndFuncGroupAndFuncId(
        BigDecimal buyerOid,String funcGroup,String funcId) throws Exception
    {
        if (buyerOid == null || StringUtils.isBlank(funcId))
        {
            throw new IllegalArgumentException();
        }
        
        BusinessRuleExHolder parameter = new BusinessRuleExHolder();
        parameter.setBuyerOid(buyerOid);
        parameter.setFuncId(funcId);
        parameter.setFuncGroup(funcGroup);
        
        return mapper.selectRulesByBuyerOidAndFuncGroupAndFuncId(parameter);
    }

    @Override
    public List<String> selectFuncGroups() throws Exception
    {
        List<String> list = new ArrayList<String>();
        Map<String, String> map = new HashMap<String, String>();
        List<BusinessRuleHolder> rlt = this.select(new BusinessRuleHolder());
        if(rlt == null)
        {
            return list;
        }
        Iterator<BusinessRuleHolder> it = rlt.iterator();
        while(it.hasNext())
        {
            BusinessRuleHolder obj = it.next();
            if(!map.keySet().contains(obj.getFuncGroup()))
            {
                map.put(obj.getFuncGroup(), obj.getFuncGroup());
                list.add(obj.getFuncGroup());
            }
        }
        return list;
    }


    @Override
    public List<String> selectFuncIdsByGroup(String funcGroup) throws Exception
    {
        List<String> list = new ArrayList<String>();
        Map<String, String> map = new HashMap<String, String>();
        BusinessRuleHolder param = new BusinessRuleHolder();
        if(funcGroup != null && !funcGroup.trim().isEmpty())
        {
            param.setFuncGroup(funcGroup);
        }
        List<BusinessRuleHolder> rlt = this.select(param);
        if(rlt == null)
        {
            return list;
        }
        Iterator<BusinessRuleHolder> it = rlt.iterator();
        while(it.hasNext())
        {
            BusinessRuleHolder obj = it.next();
            if(!map.keySet().contains(obj.getFuncId()))
            {
                map.put(obj.getFuncId(), obj.getFuncId());
                list.add(obj.getFuncId());
            }
        }
        return list;
    }


    @Override
    public List<BusinessRuleHolder> select(BusinessRuleHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public BusinessRuleExHolder selectRulesByKey(
            BigDecimal buyerOid, String funcGroup, String funcId, String ruleId)
            throws Exception
    {
        if (buyerOid == null || funcGroup == null || funcGroup.trim().isEmpty()
                || funcId == null || funcId.trim().isEmpty() || ruleId == null
                || ruleId.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        BusinessRuleExHolder parameter = new BusinessRuleExHolder();
        parameter.setBuyerOid(buyerOid);
        parameter.setFuncId(funcId);
        parameter.setFuncGroup(funcGroup);
        parameter.setRuleId(ruleId);
        return mapper.selectRulesByBuyerOidAndFuncGroupAndFuncIdAndRuleId(parameter);
    }

    
    private String selectStringByBuyer(BigDecimal buyerOid, String funcGroup,
            String funcId, String ruleId) throws Exception
    {
        BusinessRuleExHolder rule = this.selectRulesByKey(buyerOid, funcGroup, funcId, ruleId);
        if (rule == null)
        {
            return null;
        }
        return rule.getStringValue();
    }

    
    private Boolean selectBooleanByBuyer(BigDecimal buyerOid, String funcGroup,
            String funcId, String ruleId) throws Exception
    {
        BusinessRuleExHolder rule = this.selectRulesByKey(buyerOid, funcGroup, funcId, ruleId);
        if (rule == null)
        {
            return false;
        }
        return true;
    }

    
    private Integer selectIntegerByBuyer(BigDecimal buyerOid, String funcGroup,
            String funcId, String ruleId) throws Exception
    {
        BusinessRuleExHolder rule = this.selectRulesByKey(buyerOid, funcGroup, funcId, ruleId);
        if (rule == null)
        {
            return 0;
        }
        return Integer.parseInt(rule.getStringValue());
    }
    
    
    private BigDecimal selectBigDecimalByBuyer(BigDecimal buyerOid, String funcGroup,
        String funcId, String ruleId) throws Exception
    {
        String value = selectStringByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "RtvGiDnPriceTolerance");
        if (value != null)
        {
            return BigDecimalUtil.getInstance().convertStringToBigDecimal(value, 2);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public Integer selectGlobalMatchingJobMinBufferingDays(BigDecimal buyerOid)
            throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, "Global", "MatchingJobMinBufferingDays");
    }

    
    @Override
    public Integer selectGlobalMatchingJobMaxBufferingDays(BigDecimal buyerOid)
            throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "MatchingJobMaxBufferingDays");
    }

    
    @Override
    public Integer selectGlobalDailyPoReportJobDaysBefore(BigDecimal buyerOid)
            throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "DailyPoReportJobDaysBefore");
    }

    
    @Override
    public Integer selectGlobalDailyNotificationJobMissingGrnMinBufferingDays(
            BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "DailyNotificationJobMissingGrnMinBufferingDays");
    }

    
    @Override
    public Integer selectGlobalDailyNotificationJobMissingGrnMaxBufferingDays(
            BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "DailyNotificationJobMissingGrnMaxBufferingDays");
    }

    
    @Override
    public Integer selectGlobalDnGeneratingJobMatchingMaxBuffingDays(
            BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "DnGeneratingJobInvMaxBuffingDays");
    }
    
    
    @Override
    public Integer selectGlobalDnGeneratingJobMatchingMinBuffingDays(
            BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "DnGeneratingJobInvMinBuffingDays");
    }

    
    @Override
    public Integer selectGlobalRTVDnGeneratingJobBuffingDays(
            BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "RTVDnGeneratingJobBuffingDays");
    }
    
    
    @Override
    public Boolean isContinueProcessErrorBatch(BigDecimal buyerOid)
            throws Exception

    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "ContinueProcessErrorBatch");
    }
    
    
    @Override
    public Boolean isAutoGenDnFromGI(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "AutoGenDnFromGI");
    }

    
    @Override
    public Boolean isAutoApproveMatchedByDn(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "AutoApproveMatchedByDn");
    }

    
    @Override
    public Boolean isAutoSendStockDn(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "AutoSendStockDn");
    }

    
    @Override
    public Boolean isAutoSendCostDn(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "AutoSendCostDn");
    }
    
    
    @Override
    public Boolean isSupplierCanDisputeGRN(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GRN, FUNC_ID_GLOBAL, "SupplierCanDisputeGRN");
    }
    
    
    @Override
    public Boolean isGenResultTxt(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_SM, FUNC_ID_IMPORTING, "GenResultTxt");
    }
    
    
    @Override
    public Boolean isGenAdminUser(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_SM, FUNC_ID_IMPORTING, "GenAdminUser");
    }
    
    
    @Override
    public String selectSMAdminRole(BigDecimal buyerOid) throws Exception
    {
        return selectStringByBuyer(buyerOid, FUNC_GROUP_SM, FUNC_ID_IMPORTING, "AdminRole");
    }
    
    
    @Override
    public Boolean isDnNeedTranslate(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "NeedTranslate");
    }
    
    
    @Override
    public Boolean isAutoGenCostDn(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "AutoGenCostDn");
    }
    
    
    @Override
    public Boolean isAutoGenStockDn(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "AutoGenStockDn");
    }
    
    
    @Override
    public Boolean isSendDnResolutionAndOutstandingByGroup(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "SendResolutionAndOutstandingByGroup");
    }
    
    
    @Override
    public Boolean isMatchingEnableSupplierToDispute(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "EnableSupplierToDispute");
    }
    
    
    @Override
    public Integer selectMatchingQtyRule(BigDecimal buyerOid) throws Exception
    {
        boolean QtyInvLessGrn =  selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "QtyInvLessGrn");
        boolean QtyPoLessGrn = selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "QtyPoLessGrn");
        
        //1: QtyInvLessGrn and QtyPoLessGrn   2:QtyInvLessGrn and !QtyPoLessGrn   3:!QtyInvLessGrn
        if (QtyInvLessGrn && QtyPoLessGrn)
        {
            return 1;
        }
        else if (QtyInvLessGrn && !QtyPoLessGrn)
        {
            return 2;
        }
        else
        {
            return 3;
        }
    }
    
    
    @Override
    public Boolean isMatchingPriceInvLessPo(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "PriceInvLessPo");
    }
    
    
    @Override
    public Boolean isMatchingAutoCloseAcceptedRecord(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "AutoCloseAcceptedRecord");
    }
    
    
    @Override
    public Boolean isMatchingAutoApproveClosedAcceptedRecord(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "AutoApproveClosedAcceptedRecord");
    }
    
    
    @Override
    public Boolean isSendMatchingResolutionAndOutstandingByGroup(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "SendResolutionAndOutstandingByGroup");
    }
    
    
    @Override
    public Integer selectPoDeliveryDateRange(BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_PO, FUNC_ID_GLOBAL, "DeliveryDateRange");
    }
    
    
    @Override
    public Boolean isPoConvertInvEmailToStore(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_PO_CONVERT_INV, FUNC_ID_SORPO, "EmailToStore");
    }

    
    @Override
    public Boolean isItemDeleteAndInsert(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_ITEM, FUNC_ID_IMPORTING, "DeleteAndInsert");
    }

    
    @Override
    public Boolean isItemUpdate(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_ITEM, FUNC_ID_IMPORTING, "Update");
    }

    
    @Override
    public Boolean isItemSelectOneToCompare(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_ITEM, FUNC_ID_IMPORTING, "SelectOneToCompare");
    }

    
    @Override
    public Boolean isItemSelectAllToCompare(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_ITEM, FUNC_ID_IMPORTING, "SelectAllToCompare");
    }

	@Override
    public Boolean isSkipMatching(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "SkipMatching");
    }

	
    @Override
    public Boolean isPreventINVItemsNotExistInPO(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_INV, FUNC_ID_GLOBAL, "PreventItemsNotExistInPO");
    }

    
    @Override
    public Boolean isPreventGRNItemsNotExistInPO(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GRN, FUNC_ID_GLOBAL, "PreventItemsNotExistInPO");
    }
    
    
    @Override
    public Boolean isPreventGIItemsNotExistInRtv(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GI, FUNC_ID_GLOBAL, "PreventItemsNotExistInRtv");
    }

    @Override
    public Boolean isMatchingQtyInvLessGrn(BigDecimal buyerOid)
        throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "QtyInvLessGrn");
    }
    
    
    @Override
    public Boolean isAutoAcceptQtyInvLessGrn(BigDecimal buyerOid)
        throws Exception
    {
        boolean autoAcceptInvLessGrn = selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "AutoAcceptQtyInvLessGrn");
        
        if (!isMatchingQtyInvLessGrn(buyerOid) && autoAcceptInvLessGrn)
        {
            return true;
        }
        
        return false;
    }

    @Override
    public Boolean isAutoAcceptPriceInvLessPo(BigDecimal buyerOid)
        throws Exception
    {
        boolean autoAcceptInvLessPo = selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "AutoAcceptPriceInvLessPo");
        
        if (!isMatchingPriceInvLessPo(buyerOid) && autoAcceptInvLessPo)
        {
            return true;
        }
        return false;
    }

    @Override
    public String selectDnTranslateFileStyle(BigDecimal buyerOid)
            throws Exception
    {
        List<BusinessRuleHolder> dnBusinessRules = selectRulesByBuyerOidAndFuncGroupAndFuncId(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND);
        
        for (BusinessRuleHolder obj : dnBusinessRules)
        {
            if ("UnityFileStype".equals(obj.getRuleId()))
            {
                return "UnityFileStype";
            }
        }
        
        return "";
    }

    @Override
    public Boolean isPreventGRNItemsLessThanPO(BigDecimal buyerOid)
        throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GRN, FUNC_ID_GLOBAL, "PreventItemsLessThanPO");
    }

    @Override
    public Boolean isPreventGRNItemsQtyMoreThanPO(BigDecimal buyerOid)
        throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GRN, FUNC_ID_GLOBAL, "PreventItemsQtyMoreThanPO");
    }

    @Override
    public Boolean isPreventGIItemsLessThanRtv(BigDecimal buyerOid)
        throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GI, FUNC_ID_GLOBAL, "PreventItemsLessThanRtv");
    }

    @Override
    public Boolean isPreventGIItemsQtyMoreThanRtv(BigDecimal buyerOid)
        throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GI, FUNC_ID_GLOBAL, "PreventItemsQtyMoreThanRtv");
    }

    @Override
    public Boolean isMatchingChangeInvDateToFirstGrnDate(BigDecimal buyerOid)
        throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "ChangeInvDateToGrnDate");
    }

    @Override
    public Boolean isNeedValidateDailySalesDataLogic(BigDecimal buyerOid)
        throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DSD, FUNC_ID_GLOBAL, "NeedValidate");
    }
    
    @Override
    public Boolean isNeedValidateConPoDataLogic(BigDecimal buyerOid)
        throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_PO, FUNC_ID_GLOBAL, "NeedValidateConsignmentPo");
    }

    @Override
    public Integer selectGlobalDailyNotificationJobMissingGiMinBufferingDays(
        BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "DailyNotificationJobMissingGiMinBufferingDays");
    }

    @Override
    public Integer selectGlobalDailyNotificationJobMissingGiMaxBufferingDays(
        BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "DailyNotificationJobMissingGiMaxBufferingDays");

    }

    @Override
    public Boolean isSendMatchingDiscrepancyReportToUser(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "DiscrepancyReportToUser");
    }

    @Override
    public Boolean isSendDnDiscrepancyReportToUser(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "DiscrepancyReportToUser");
    }

    @Override
    public Boolean isAutoCloseAcceptedDnRecord(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "AutoCloseAcceptedRecord");
    }

    @Override
    public Boolean isAutoCloseRejectedMatchingRecord(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING, FUNC_ID_POINVGRNDN, "AutoCloseRejectedRecord");
    }

    @Override
    public Boolean isAllowSupplierDisputeMatchingDn(BigDecimal buyerOid)
            throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_DN, FUNC_ID_BACKEND, "AllowSupplierDisputeMatchingDn");
    }
    
	@Override
	public Boolean isAutoRejectBuyerLossUnmatchedRecord(BigDecimal buyerOid)
			throws Exception 
	{
		return selectBooleanByBuyer(buyerOid, FUNC_GROUP_MATCHING,FUNC_ID_POINVGRNDN, "AutoRejectBuyerLossUnmatchedRecord");
	}

	@Override
	public Boolean isDisablePaymentInstructions(BigDecimal buyerOid)
			throws Exception 
	{
		return selectBooleanByBuyer(buyerOid, FUNC_GROUP_GLOBAL,FUNC_ID_GLOBAL, "DisableInvoicePaymentInstructions");
	}


    @Override
    public Boolean isRtvGiDnQtyToleranceTypeWithAmount(BigDecimal buyerOid)
        throws Exception
    {
        String type = selectStringByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "RtvGiDnQtyToleranceType");
        if (type != null && type.equalsIgnoreCase("true"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    @Override
    public Boolean isRtvGiDnPriceToleranceTypeWithAmount(BigDecimal buyerOid)
        throws Exception
    {
        String type = selectStringByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "RtvGiDnPriceToleranceType");
        if (type != null && type.equalsIgnoreCase("true"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    @Override
    public BigDecimal selectGlobalRtvGiDnQtyTolerance(BigDecimal buyerOid)
        throws Exception
    {
        return selectBigDecimalByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "RtvGiDnQtyTolerance");
    }

    @Override
    public BigDecimal selectGlobalRtvGiDnPriceTolerance(BigDecimal buyerOid)
        throws Exception
    {
        return selectBigDecimalByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "RtvGiDnPriceTolerance");
    }

    @Override
    public Integer selectGlobalRtvGiDnReportGeneratingDateRange(
        BigDecimal buyerOid) throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "RtvGiDnReportGeneratingDateRange");
    }

    @Override
    public Boolean isIgnoreExpiryDate(BigDecimal buyerOid) throws Exception
    {
        return selectBooleanByBuyer(buyerOid, FUNC_GROUP_PO_CONVERT_INV, FUNC_ID_SORPO, "IgnoreExpiryDate");
    }

    @Override
    public Integer selectGlobalZipFileWhenSizeExceed(BigDecimal buyerOid)
        throws Exception
    {
        return selectIntegerByBuyer(buyerOid, FUNC_GROUP_GLOBAL, FUNC_ID_GLOBAL, "ZipFileWhenSizeExceed");
    }
    
}
