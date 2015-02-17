package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;




import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.extension.BusinessRuleExHolder;

public interface BusinessRuleService extends BaseService<BusinessRuleHolder>,
        DBActionService<BusinessRuleHolder>
{
    public List<BusinessRuleHolder> selectRulesByBuyerOidAndFuncGroupAndFuncId(
        BigDecimal buyerOid, String funcGroup, String funcId) throws Exception;


    public List<String> selectFuncGroups() throws Exception;


    public List<String> selectFuncIdsByGroup(String funcGroup) throws Exception;
    

    public BusinessRuleExHolder selectRulesByKey(
            BigDecimal buyerOid, String funcGroup, String funcId, String ruleId)
            throws Exception;
    
    
    public Integer selectGlobalMatchingJobMinBufferingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalMatchingJobMaxBufferingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalDailyPoReportJobDaysBefore(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalDailyNotificationJobMissingGrnMinBufferingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalDailyNotificationJobMissingGrnMaxBufferingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalDnGeneratingJobMatchingMaxBuffingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalDnGeneratingJobMatchingMinBuffingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalRTVDnGeneratingJobBuffingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isContinueProcessErrorBatch(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoGenDnFromGI(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoApproveMatchedByDn(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoSendStockDn(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoSendCostDn(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isSupplierCanDisputeGRN(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isGenResultTxt(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isGenAdminUser(BigDecimal buyerOid) throws Exception;
    
    
    public String selectSMAdminRole(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isDnNeedTranslate(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoGenCostDn(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoGenStockDn(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isSendDnResolutionAndOutstandingByGroup(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isMatchingEnableSupplierToDispute(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectMatchingQtyRule(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isMatchingQtyInvLessGrn(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isMatchingPriceInvLessPo(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isMatchingAutoCloseAcceptedRecord(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isMatchingAutoApproveClosedAcceptedRecord(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isSendMatchingResolutionAndOutstandingByGroup(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isMatchingChangeInvDateToFirstGrnDate(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectPoDeliveryDateRange(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isPoConvertInvEmailToStore(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isItemDeleteAndInsert(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isItemUpdate(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isItemSelectOneToCompare(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isItemSelectAllToCompare(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isSkipMatching(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isPreventINVItemsNotExistInPO(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isPreventGRNItemsNotExistInPO(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isPreventGRNItemsLessThanPO(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isPreventGRNItemsQtyMoreThanPO(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isPreventGIItemsNotExistInRtv(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isPreventGIItemsLessThanRtv(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isPreventGIItemsQtyMoreThanRtv(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoAcceptQtyInvLessGrn(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoAcceptPriceInvLessPo(BigDecimal buyerOid) throws Exception;
    
    
    public String selectDnTranslateFileStyle(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isNeedValidateDailySalesDataLogic(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isNeedValidateConPoDataLogic(BigDecimal buyerOid)throws Exception;
    
    
    public Integer selectGlobalDailyNotificationJobMissingGiMinBufferingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalDailyNotificationJobMissingGiMaxBufferingDays(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isSendMatchingDiscrepancyReportToUser(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isSendDnDiscrepancyReportToUser(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoCloseAcceptedDnRecord(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoCloseRejectedMatchingRecord(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAllowSupplierDisputeMatchingDn(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isAutoRejectBuyerLossUnmatchedRecord(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isDisablePaymentInstructions(BigDecimal buyerOid) throws Exception;

    
    public Boolean isRtvGiDnQtyToleranceTypeWithAmount(BigDecimal buyerOid) throws Exception;
    
    
    public Boolean isRtvGiDnPriceToleranceTypeWithAmount(BigDecimal buyerOid) throws Exception;
    
    
    public BigDecimal selectGlobalRtvGiDnQtyTolerance(BigDecimal buyerOid) throws Exception;
    
    
    public BigDecimal selectGlobalRtvGiDnPriceTolerance(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalRtvGiDnReportGeneratingDateRange(BigDecimal buyerOid) throws Exception;

    
    public Boolean isIgnoreExpiryDate(BigDecimal buyerOid) throws Exception;
    
    
    public Integer selectGlobalZipFileWhenSizeExceed(BigDecimal buyerOid) throws Exception;

}
