<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
    
    </style>
        
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dijit/registry",
                "dojo/on",
                "dojo/string",
                "dojo/json",
                "dojo/query",
                "dojo/_base/xhr",
                "dojo/parser",
                "dijit/form/ValidationTextBox",
                "custom/InformationDialog",
                "dijit/form/CheckBox",
                "dijit/form/Select",
                "dijit/form/RadioButton",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    string,
                    JSON,
                    query,
                    xhr,
                    parser,
                    ValidationTextBox,
                    InformationDialog,
                    CheckBox,
                    Select,
                    RadioButton
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/buyer/initEditBusinessRule.action" />'+'?param.buyerOid=<s:property value="param.buyerOid"/>');
                        }
                    );
                    
                    on(registry.byId("backBtn"), 'click', 
                        function()
                        {
                    	   changeToURL('<s:url value="/buyer/init.action?keepSp=Y" />');
                        }
                    );
                    
                    on(registry.byId("funcGroup"), 'change', 
                        function(value)
                        {
                    		(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
	                    	xhr.get({
	                            url: '<s:url value="/buyer/getFuncIdSByGroup.action" />',
	                            content: {"funcGroup" : value},
	                            load: function(jsonData)
	                            {
		                            var result = JSON.parse(jsonData);
		                            var select = '<select onchange="showDiv();" name="funcId" data-dojo-type="dijit.form.Select" />';
		                            select += '<option value=""><s:text name="ALL"/></option>';
		                            for(var i = 0; i < result.length; i++)
                            	    {
		                        	    var obj = result[i];
		                        	    select += '<option value="'+obj+'">'+obj+'</option>';
		                            }
		                            select += '</select>';
		                            dom.byId('funcIdTd').innerHTML = select;
		                            if(value == "")
		                            {
		                            	dom.byId("funcIdTr").style.display = 'none';
		                            }
		                            else
		                            {
		                            	dom.byId("funcIdTr").style.display = '';
		                            }
		                            parser.parse(dom.byId('funcIdTd'));
		                            showDiv();
	                            }
	                        });
                        }
                    );
                    
                    showDiv = function()
                    {
                    	var funcGroup
                        if(document.getElementsByName("funcGroup").length == 1)//firefox got 1
                        {
	                        funcGroup = document.getElementsByName("funcGroup")[0].value;
                        }
                        else//ie got 2
                        {
	                        funcGroup = document.getElementsByName("funcGroup")[1].value;
                        }
						var funcId = document.getElementsByName("funcId")[0].value;
                    	query(".ruleDiv").forEach(function(node, index, nodeList){
                            node.style.display='none';
                        });
						if(funcId != "")
						{
							dom.byId(funcGroup+"_"+funcId).style.display = '';
						}
						else if(funcGroup != "")
						{
							query(".ruleDiv").forEach(function(node, index, nodeList){
								if(node.id.split("_")[0] == funcGroup)
								{
									node.style.display = "";
								}
	                        });
						}
						else
						{
							query(".ruleDiv").forEach(function(node, index, nodeList){
								node.style.display = "";
	                        });
						}
                    }

                    showAndHide = function(src, className, positive)
                    {
                        if(positive)
                        {
	                        if(src.checked)
	                        {
		                        query("."+className+"_tr").forEach(function(node, index, nodeList){
		                            node.style.display='';
		                        });
		                        query("."+className+"_tr_hide").forEach(function(node, index, nodeList){
		                            node.style.display='none';
		                        });
	                        }
	                        else
	                        {
	                        	query("."+className).forEach(function(node, index, nodeList){
	                                registry.byNode(node).setChecked(false);
	                            });
	                        	query("."+className+"_tr").forEach(function(node, index, nodeList){
	                                node.style.display='none';
	                            });
	                        	query("."+className+"_tr_hide").forEach(function(node, index, nodeList){
	                                node.style.display='';
	                            });
	                        }
                        }
                        else
                        {
	                        if(src.checked)
	                        {
	                        	query("."+className).forEach(function(node, index, nodeList){
	                                registry.byNode(node).setChecked(false);
	                            });
		                        query("."+className+"_tr").forEach(function(node, index, nodeList){
		                            node.style.display='none';
		                        });
	                        }
	                        else
	                        {
	                        	query("."+className+"_tr").forEach(function(node, index, nodeList){
	                                node.style.display='';
	                            });
	                        }

                        }
                    }

                    saveData = function(formName)
                    {

                    	(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
                        var buyerOid  = '<s:property value="param.buyerOid"/>';
                        
                        var ggAutoLogout = string.trim(document.getElementsByName("ggAutoLogout.ruleValue")[0].value);
                        var ggMatchingJobMinBufferingDays = string.trim(document.getElementsByName("ggMatchingJobMinBufferingDays.ruleValue")[0].value);
                        var ggMatchingJobMaxBufferingDays = string.trim(document.getElementsByName("ggMatchingJobMaxBufferingDays.ruleValue")[0].value);
                        var ggDailyPoReportJobDaysBefore = string.trim(document.getElementsByName("ggDailyPoReportJobDaysBefore.ruleValue")[0].value);
                        var ggDailyNotificationJobMissingGrnMinBufferingDays = string.trim(document.getElementsByName("ggDailyNotificationJobMissingGrnMinBufferingDays.ruleValue")[0].value);
                        var ggDailyNotificationJobMissingGrnMaxBufferingDays = string.trim(document.getElementsByName("ggDailyNotificationJobMissingGrnMaxBufferingDays.ruleValue")[0].value);
                        var ggDnGeneratingJobMatchingMaxBuffingDays = string.trim(document.getElementsByName("ggDnGeneratingJobMatchingMaxBuffingDays.ruleValue")[0].value);
                        var ggDnGeneratingJobMatchingMinBuffingDays = string.trim(document.getElementsByName("ggDnGeneratingJobMatchingMinBuffingDays.ruleValue")[0].value);
                        var ggRTVDnGeneratingJobBuffingDays = string.trim(document.getElementsByName("ggRTVDnGeneratingJobBuffingDays.ruleValue")[0].value);
                        var ggContinueProcessErrorBatch = document.getElementsByName("ggContinueProcessErrorBatch.valid")[0].checked;
						var ggRTVDnDisputeAlertWindow = string.trim(document.getElementsByName("ggRTVDnDisputeAlertWindow.ruleValue")[0].value);
						var ggDisableInvoicePaymentInstructions = document.getElementsByName("ggDisableInvoicePaymentInstructions.valid")[0].checked;
						var ggRtvGiDnQtyToleranceRadios = document.getElementsByName("ggRtvGiDnQtyToleranceType.ruleValue");
						var ggRtvGiDnQtyToleranceType = null;
						var ggRtvGiDnQtyToleranceRuleValue = document.getElementsByName("ggRtvGiDnQtyTolerance.ruleValue")[0].value;
						var ggRtvGiDnPriceToleranceRadios = document.getElementsByName("ggRtvGiDnPriceToleranceType.ruleValue");
						var ggRtvGiDnPriceToleranceType = null;
						var ggRtvGiDnPriceToleranceRuleValue = document.getElementsByName("ggRtvGiDnPriceTolerance.ruleValue")[0].value;
						var ggRtvGiDnReportGeneratingDateRange = document.getElementsByName("ggRtvGiDnReportGeneratingDateRange.ruleValue")[0].value;
						for (var i=0; i < ggRtvGiDnQtyToleranceRadios.length; i++)
						{
							if (ggRtvGiDnQtyToleranceRadios[i].checked)
								ggRtvGiDnQtyToleranceType = ggRtvGiDnQtyToleranceRadios[i].value;
						}
						for (var i=0; i < ggRtvGiDnPriceToleranceRadios.length; i++)
						{
							if (ggRtvGiDnPriceToleranceRadios[i].checked)
								ggRtvGiDnPriceToleranceType = ggRtvGiDnPriceToleranceRadios[i].value
						}
						
                        var pcispQtyEditable = document.getElementsByName("pcispQtyEditable.valid")[0].checked;
                        var pcispQtylessThanPO = document.getElementsByName("pcispQtylessThanPO.valid")[0].checked;
                        var pcispFocQtyEditable = document.getElementsByName("pcispFocQtyEditable.valid")[0].checked;
                        var pcispFocQtylessThanPO = document.getElementsByName("pcispFocQtylessThanPO.valid")[0].checked;
                        var pcispDiscountEditable = document.getElementsByName("pcispDiscountEditable.valid")[0].checked;
                        var pcispDiscountForDetailEditable = document.getElementsByName("pcispDiscountForDetailEditable.valid")[0].checked;
                        var pcispCashDiscountEditable = document.getElementsByName("pcispCashDiscountEditable.valid")[0].checked;
                        var pcispUnitPriceEditable = document.getElementsByName("pcispUnitPriceEditable.valid")[0].checked;
                        var pcispEmailToStore = document.getElementsByName("pcispEmailToStore.valid")[0].checked;
                        var pcispPdfAsAttachment = document.getElementsByName("pcispPdfAsAttachment.valid")[0].checked;
                        var pcispUnitPriceLessThanPO = document.getElementsByName("pcispUnitPriceLessThanPO.valid")[0].checked;
                        var pcicpIgnoreExpiryDate = document.getElementsByName("pcicpIgnoreExpiryDate.valid")[0].checked;
                     
                        var pcicpItemDiscountEditable = document.getElementsByName("pcicpItemDiscountEditable.valid")[0].checked;
                        var pcicpItemAmountEditable = document.getElementsByName("pcicpItemAmountEditable.valid")[0].checked;
                        var pcicpItemSharedCostEditable = document.getElementsByName("pcicpItemSharedCostEditable.valid")[0].checked;
                        var pcicpTradeDiscountEditable = document.getElementsByName("pcicpTradeDiscountEditable.valid")[0].checked;
                        var pcicpCashDiscountEditable = document.getElementsByName("pcicpCashDiscountEditable.valid")[0].checked;
                        
                        var dbAutoGenStockDn = document.getElementsByName("dbAutoGenStockDn.valid")[0].checked;
                        var dbAutoGenCostDn = document.getElementsByName("dbAutoGenCostDn.valid")[0].checked;
                        var dbAutoSendStockDn = document.getElementsByName("dbAutoSendStockDn.valid")[0].checked;
                        var dbAutoSendCostDn = document.getElementsByName("dbAutoSendCostDn.valid")[0].checked;
                        var dnNoStyle = document.getElementsByName("dnNoStyle");
                        var dnNoStyle1 = dnNoStyle[0].checked;
                        var dbFileStype = document.getElementsByName("dbFileStype");
                        var dbUnityFileStype = dbFileStype[0].checked;
                        var dbNeedTranslate = document.getElementsByName("dbNeedTranslate.valid")[0].checked;
                        var dbAutoGenDnFromGI = document.getElementsByName("dbAutoGenDnFromGI.valid")[0].checked;
                        var dbAllowSupplierDisputeMatchingDn = document.getElementsByName("dbAllowSupplierDisputeMatchingDn.valid")[0].checked;
                        var dbDiscrepancyReportToUser = document.getElementsByName("dbDiscrepancyReportToUser.valid")[0].checked;
                        var dbAutoCloseAcceptedRecord = document.getElementsByName("dbAutoCloseAcceptedRecord.valid")[0].checked;
                        var dbSendResolutionAndOutstandingByGroup = document.getElementsByName("dbSendResolutionAndOutstandingByGroup.valid")[0].checked;
                        
                        var mpigdQtyInvLessGrn = document.getElementsByName("mpigdQtyInvLessGrn.valid")[0].checked;
                        var mpigdPriceInvLessPo = document.getElementsByName("mpigdPriceInvLessPo.valid")[0].checked;
                        var mpigdQtyPoLessGrn = document.getElementsByName("mpigdQtyPoLessGrn.valid")[0].checked;
                        var mpigdAmountTolerance = string.trim(document.getElementsByName("mpigdAmountTolerance.ruleValue")[0].value);
                        var mpigdAutoApproveMatchedByDn = document.getElementsByName("mpigdAutoApproveMatchedByDn.valid")[0].checked;
                        var mpigdEnableSupplierToDispute = document.getElementsByName("mpigdEnableSupplierToDispute.valid")[0].checked;
                        var mpigdAutoApproveClosedAcceptedRecord = document.getElementsByName("mpigdAutoApproveClosedAcceptedRecord.valid")[0].checked;
                        var mpigdAutoCloseAcceptedRecord = document.getElementsByName("mpigdAutoCloseAcceptedRecord.valid")[0].checked;
                        var mpigdAutoCloseRejectedRecord = document.getElementsByName("mpigdAutoCloseRejectedRecord.valid")[0].checked;
                        var mpigdChangeInvDateToGrnDate = document.getElementsByName("mpigdChangeInvDateToGrnDate.valid")[0].checked;
                        var mpigdSkipMatching = document.getElementsByName("mpigdSkipMatching.valid")[0].checked;
                        var mpigdDiscrepancyReportToUser = document.getElementsByName("mpigdDiscrepancyReportToUser.valid")[0].checked;
                        var mpigdAutoRejectBuyerLossUnmatchedRecord = document.getElementsByName("mpigdAutoRejectBuyerLossUnmatchedRecord.valid")[0].checked;
                        var mpigdSendResolutionAndOutstandingByGroup = document.getElementsByName("mpigdSendResolutionAndOutstandingByGroup.valid")[0].checked;
                        var mpigdAutoAcceptQtyInvLessGrn = document.getElementsByName("mpigdAutoAcceptQtyInvLessGrn.valid")[0].checked;
                        var mpigdAutoAcceptPriceInvLessPo = document.getElementsByName("mpigdAutoAcceptPriceInvLessPo.valid")[0].checked;
                        
                        var smiGenAdminUser = document.getElementsByName("smiGenAdminUser.valid")[0].checked;
                        var smiAdminRole = string.trim(document.getElementsByName("smiAdminRole.ruleValue")[0].value);
                        var smiGenResultTxt = document.getElementsByName("smiGenResultTxt.valid")[0].checked;
                        
                        var iiUpdate = document.getElementsByName("iiRule")[0].checked;
                        var iiDeleteAndInsert = document.getElementsByName("iiRule")[1].checked;
                        var iiSelectOneToCompare = document.getElementsByName("iiCompareRule")[0].checked;
                        
                        var pgDeliveryDateRange = string.trim(document.getElementsByName("pgDeliveryDateRange.ruleValue")[0].value);
                        var pgNeedValidateConPo = document.getElementsByName("pgNeedValidateConPo.valid")[0].checked;
                        
                        var ggSupplierCanDisputeGRN = document.getElementsByName("ggSupplierCanDisputeGRN.valid")[0].checked;
                        var ggPreventItemsNotExistInPO = document.getElementsByName("ggPreventItemsNotExistInPO.valid")[0].checked;
                        var ggPreventItemsLessThanPO = document.getElementsByName("ggPreventItemsLessThanPO.valid")[0].checked;
                        var ggPreventItemsQtyMoreThanPO = document.getElementsByName("ggPreventItemsQtyMoreThanPO.valid")[0].checked;
                        
                        var igPreventItemsNotExistInPO = document.getElementsByName("igPreventItemsNotExistInPO.valid")[0].checked;
            
                        var gigPreventItemsNotExistInRtv = document.getElementsByName("gigPreventItemsNotExistInRtv.valid")[0].checked;
                        var gigPreventItemsLessThanRtv = document.getElementsByName("gigPreventItemsLessThanRtv.valid")[0].checked;
                        var gigPreventItemsQtyMoreThanRtv = document.getElementsByName("gigPreventItemsQtyMoreThanRtv.valid")[0].checked;

                        var dsdNeedValidateSalesData = document.getElementsByName("dsdNeedValidateSalesData.valid")[0].checked;

                        var ggDailyNotificationJobMissingGiMinBufferingDays = string.trim(document.getElementsByName("ggDailyNotificationJobMissingGiMinBufferingDays.ruleValue")[0].value);
                        var ggDailyNotificationJobMissingGiMaxBufferingDays = string.trim(document.getElementsByName("ggDailyNotificationJobMissingGiMaxBufferingDays.ruleValue")[0].value);
                        
                        var csrfToken = document.getElementById("csrfToken").value;
                        
                        if(formName == 'globalForm')
                        {
                        	xhr.get({
                                url: '<s:url value="/buyer/checkGlobal.action" />',
                                content: {
                                	"ggAutoLogout.ruleValue":ggAutoLogout,
                                    "ggMatchingJobMinBufferingDays.ruleValue":ggMatchingJobMinBufferingDays,
                                    "ggMatchingJobMaxBufferingDays.ruleValue":ggMatchingJobMaxBufferingDays,
                                    "ggDailyPoReportJobDaysBefore.ruleValue":ggDailyPoReportJobDaysBefore,
                                    "ggDailyNotificationJobMissingGrnMinBufferingDays.ruleValue":ggDailyNotificationJobMissingGrnMinBufferingDays,
                                    "ggDailyNotificationJobMissingGrnMaxBufferingDays.ruleValue":ggDailyNotificationJobMissingGrnMaxBufferingDays,
                                    "ggDnGeneratingJobMatchingMaxBuffingDays.ruleValue":ggDnGeneratingJobMatchingMaxBuffingDays,
                                    "ggDnGeneratingJobMatchingMinBuffingDays.ruleValue":ggDnGeneratingJobMatchingMinBuffingDays,
                                    "ggRTVDnGeneratingJobBuffingDays.ruleValue":ggRTVDnGeneratingJobBuffingDays,
                                    "ggRTVDnDisputeAlertWindow.ruleValue":ggRTVDnDisputeAlertWindow,
                                    "ggDailyNotificationJobMissingGiMinBufferingDays.ruleValue":ggDailyNotificationJobMissingGiMinBufferingDays,
                                    "ggDailyNotificationJobMissingGiMaxBufferingDays.ruleValue":ggDailyNotificationJobMissingGiMaxBufferingDays,
                                    "ggDisableInvoicePaymentInstructions.valid":ggDisableInvoicePaymentInstructions,
                                    "ggRtvGiDnQtyToleranceType.ruleValue":ggRtvGiDnQtyToleranceType,
                                    "ggRtvGiDnQtyTolerance.ruleValue":ggRtvGiDnQtyToleranceRuleValue,
                                    "ggRtvGiDnPriceToleranceType.ruleValue":ggRtvGiDnPriceToleranceType,
                                    "ggRtvGiDnPriceTolerance.ruleValue":ggRtvGiDnPriceToleranceRuleValue,
                                    "ggRtvGiDnReportGeneratingDateRange.ruleValue":ggRtvGiDnReportGeneratingDateRange
                                    },
                                load: function(jsonData)
                                {
                                    if(JSON.parse(jsonData))
                                    {
                                        var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
                                        infoDialog.show()
                                        return;
                                    }
		                        	xhr.get({
		                                url: '<s:url value="/buyer/saveBusinessRule.action" />',
		                                content: {
		                                    "param.buyerOid": buyerOid,
		                                    "ggAutoLogout.ruleValue":ggAutoLogout,
		                                    "ggMatchingJobMinBufferingDays.ruleValue":ggMatchingJobMinBufferingDays,
		                                    "ggMatchingJobMaxBufferingDays.ruleValue":ggMatchingJobMaxBufferingDays,
		                                    "ggDailyPoReportJobDaysBefore.ruleValue":ggDailyPoReportJobDaysBefore,
		                                    "ggDailyNotificationJobMissingGrnMinBufferingDays.ruleValue":ggDailyNotificationJobMissingGrnMinBufferingDays,
		                                    "ggDailyNotificationJobMissingGrnMaxBufferingDays.ruleValue":ggDailyNotificationJobMissingGrnMaxBufferingDays,
		                                    "ggDnGeneratingJobMatchingMaxBuffingDays.ruleValue":ggDnGeneratingJobMatchingMaxBuffingDays,
		                                    "ggDnGeneratingJobMatchingMinBuffingDays.ruleValue":ggDnGeneratingJobMatchingMinBuffingDays,
		                                    "ggRTVDnGeneratingJobBuffingDays.ruleValue":ggRTVDnGeneratingJobBuffingDays,
		                                    "ggRTVDnDisputeAlertWindow.ruleValue":ggRTVDnDisputeAlertWindow,
		                                    "ggContinueProcessErrorBatch.valid":ggContinueProcessErrorBatch,
		                                    "ggDailyNotificationJobMissingGiMinBufferingDays.ruleValue":ggDailyNotificationJobMissingGiMinBufferingDays,
		                                    "ggDailyNotificationJobMissingGiMaxBufferingDays.ruleValue":ggDailyNotificationJobMissingGiMaxBufferingDays,
		                                    "ggDisableInvoicePaymentInstructions.valid":ggDisableInvoicePaymentInstructions,
		                                    "ggRtvGiDnQtyToleranceType.ruleValue":ggRtvGiDnQtyToleranceType,
		                                    "ggRtvGiDnQtyTolerance.ruleValue":ggRtvGiDnQtyToleranceRuleValue,
		                                    "ggRtvGiDnPriceToleranceType.ruleValue":ggRtvGiDnPriceToleranceType,
		                                    "ggRtvGiDnPriceTolerance.ruleValue":ggRtvGiDnPriceToleranceRuleValue,
		                                    "ggRtvGiDnReportGeneratingDateRange.ruleValue":ggRtvGiDnReportGeneratingDateRange,
		                                   	"csrfToken":csrfToken
		                                    },
		                                load: function(jsonData)
		                                {
		                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
		                                    infoDialog.show();
		                                }
		                            });
                                }
                        	});
                        }
                        if(formName == 'sorPOForm')
                        {
                        	xhr.get({
                                url: '<s:url value="/buyer/saveBusinessRule.action" />',
                                content: {
                                    "param.buyerOid": buyerOid,
                                    "pcispQtyEditable.valid":pcispQtyEditable,
                                    "pcispQtylessThanPO.valid":pcispQtylessThanPO,
                                    "pcispFocQtyEditable.valid":pcispFocQtyEditable,
                                    "pcispFocQtylessThanPO.valid":pcispFocQtylessThanPO,
                                    "pcispDiscountEditable.valid":pcispDiscountEditable,
                                    "pcispDiscountForDetailEditable.valid":pcispDiscountForDetailEditable,
                                    "pcispCashDiscountEditable.valid":pcispCashDiscountEditable,
                                    "pcispUnitPriceEditable.valid":pcispUnitPriceEditable,
                                    "pcispEmailToStore.valid":pcispEmailToStore,
                                    "pcispPdfAsAttachment.valid":pcispPdfAsAttachment,
                                    "pcispUnitPriceLessThanPO.valid":pcispUnitPriceLessThanPO,
                                    "pcicpIgnoreExpiryDate.valid":pcicpIgnoreExpiryDate,
                                    "csrfToken":csrfToken
                                    },
                                load: function(jsonData)
                                {
                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
                                    infoDialog.show();
                                }
                            });
                        }
                        if(formName == 'conPOForm')
                        {
                        	xhr.get({
                                url: '<s:url value="/buyer/saveBusinessRule.action" />',
                                content: {
                                    "param.buyerOid": buyerOid,
                                    "pcicpItemDiscountEditable.valid":pcicpItemDiscountEditable,
                                    "pcicpItemAmountEditable.valid":pcicpItemAmountEditable,
                                    "pcicpItemSharedCostEditable.valid":pcicpItemSharedCostEditable,
                                    "pcicpTradeDiscountEditable.valid":pcicpTradeDiscountEditable,
                                    "pcicpCashDiscountEditable.valid":pcicpCashDiscountEditable,
                                    "csrfToken":csrfToken
                                    },
                                load: function(jsonData)
                                {
                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
                                    infoDialog.show()
                                }
                            });
                        }
                        if(formName == 'backendForm')
                        {
                        	xhr.get({
                                url: '<s:url value="/buyer/saveBusinessRule.action" />',
                                content: {
                                    "param.buyerOid": buyerOid,
                                    "dbAutoGenStockDn.valid":dbAutoGenStockDn,
                                    "dbAutoGenCostDn.valid":dbAutoGenCostDn,
                                    "dbAutoSendStockDn.valid":dbAutoSendStockDn,
                                    "dbAutoSendCostDn.valid":dbAutoSendCostDn,
                                    "dnNoStyle1.valid":dnNoStyle1,
                                    "dbUnityFileStype.valid":dbUnityFileStype,
                                    "dbNeedTranslate.valid":dbNeedTranslate,
                                    "dbAutoGenDnFromGI.valid":dbAutoGenDnFromGI,
                                    "dbAllowSupplierDisputeMatchingDn.valid":dbAllowSupplierDisputeMatchingDn,
                                    "dbDiscrepancyReportToUser.valid":dbDiscrepancyReportToUser,
                                    "dbAutoCloseAcceptedRecord.valid":dbAutoCloseAcceptedRecord,
                                    "dbSendResolutionAndOutstandingByGroup.valid":dbSendResolutionAndOutstandingByGroup,
                                    "csrfToken":csrfToken
                                    },
                                load: function(jsonData)
                                {
                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
                                    infoDialog.show()
                                }
                            });
                        }
                        if(formName == 'poInvGrnDnForm')
                        {
                        	xhr.get({
                                url: '<s:url value="/buyer/checkPoInvGrnDnMatching.action" />',
                                content: {
                                    "mpigdAmountTolerance.ruleValue":mpigdAmountTolerance
                                    },
                                load: function(jsonData)
                                {
                                    if(JSON.parse(jsonData))
                                    {
	                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
	                                    infoDialog.show()
	                                    return;
                                    }
		                        	xhr.get({
		                                url: '<s:url value="/buyer/saveBusinessRule.action" />',
		                                content: {
		                                    "param.buyerOid": buyerOid,
		                                    "mpigdQtyInvLessGrn.valid":mpigdQtyInvLessGrn,
		                                    "mpigdPriceInvLessPo.valid":mpigdPriceInvLessPo,
		                                    "mpigdQtyPoLessGrn.valid":mpigdQtyPoLessGrn,
		                                    "mpigdAmountTolerance.ruleValue":mpigdAmountTolerance,
		                                    "mpigdAutoApproveMatchedByDn.valid":mpigdAutoApproveMatchedByDn,
		                                    "mpigdEnableSupplierToDispute.valid":mpigdEnableSupplierToDispute,
		                                    "mpigdAutoApproveClosedAcceptedRecord.valid":mpigdAutoApproveClosedAcceptedRecord,
		                                    "mpigdAutoCloseAcceptedRecord.valid":mpigdAutoCloseAcceptedRecord,
		                                    "mpigdAutoCloseRejectedRecord.valid":mpigdAutoCloseRejectedRecord,
		                                    "mpigdChangeInvDateToGrnDate.valid":mpigdChangeInvDateToGrnDate,
		                                    "mpigdSkipMatching.valid":mpigdSkipMatching,
		                                    "mpigdDiscrepancyReportToUser.valid":mpigdDiscrepancyReportToUser,
		                                    "mpigdAutoRejectBuyerLossUnmatchedRecord.valid":mpigdAutoRejectBuyerLossUnmatchedRecord,
		                                    "mpigdSendResolutionAndOutstandingByGroup.valid":mpigdSendResolutionAndOutstandingByGroup,
		                                    "mpigdAutoAcceptQtyInvLessGrn.valid":mpigdAutoAcceptQtyInvLessGrn,
		                                    "mpigdAutoAcceptPriceInvLessPo.valid":mpigdAutoAcceptPriceInvLessPo,
		                                    "csrfToken":csrfToken
		                                    },
		                                load: function(jsonData)
		                                {
		                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
		                                    infoDialog.show()
		                                }
		                            });
                                }
                            });
                        }
                        if(formName == 'importForm')
                        {
                        	xhr.get({
                                url: '<s:url value="/buyer/checkImportMatching.action" />',
                                content: {
                                    "smiAdminRole.ruleValue":smiAdminRole
                                    },
                                load: function(jsonData)
                                {
                                    if(JSON.parse(jsonData))
                                    {
	                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
	                                    infoDialog.show()
	                                    return;
                                    }
		                        	xhr.get({
		                                url: '<s:url value="/buyer/saveBusinessRule.action" />',
		                                content: {
		                                    "param.buyerOid": buyerOid,
		                                    "smiGenAdminUser.valid":smiGenAdminUser,
		                                    "smiAdminRole.ruleValue":smiAdminRole,
		                                    "smiGenResultTxt.valid":smiGenResultTxt,
		                                    "csrfToken":csrfToken
		                                    },
		                                load: function(jsonData)
		                                {
		                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
		                                    infoDialog.show()
		                                }
		                            });
                                }
                            });
                        }
                        if(formName == 'itemForm')
                        {
                        	xhr.get({
                                url: '<s:url value="/buyer/saveBusinessRule.action" />',
                                content: {
                                    "param.buyerOid": buyerOid,
                                    "iiUpdate.valid":iiUpdate,
                                    "iiDeleteAndInsert.valid":iiDeleteAndInsert,
                                    "iiSelectOneToCompare.valid":iiSelectOneToCompare,
                                    "csrfToken":csrfToken
                                    },
                                load: function(jsonData)
                                {
                                   	var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
                                    infoDialog.show()
                                }
                            });
                        }
                        if(formName == 'poGlobalForm')
                        {
                        	xhr.get({
                                url: '<s:url value="/buyer/checkPoGlobal.action" />',
                                content: {
                                	"param.buyerOid": buyerOid,
                                	"pgDeliveryDateRange.ruleValue":pgDeliveryDateRange,
                                	"csrfToken":csrfToken
                                    },
                                load: function(jsonData)
                                {
                                    if(JSON.parse(jsonData))
                                    {
                                        var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
                                        infoDialog.show()
                                        return;
                                    }
                                    xhr.get({
                                        url: '<s:url value="/buyer/saveBusinessRule.action" />',
                                        content: {
                                            "param.buyerOid": buyerOid,
                                            "pgDeliveryDateRange.ruleValue":pgDeliveryDateRange,
                                            "pgNeedValidateConPo.valid":pgNeedValidateConPo,
                                            "csrfToken":csrfToken
                                            },
                                        load: function(jsonData)
                                        {
                                            var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
                                            infoDialog.show()
                                        }
                                    });
                                }
                            });
                        }
                        if(formName == 'grnGlobalForm')
                        {
	                        xhr.get({
	                            url: '<s:url value="/buyer/saveBusinessRule.action" />',
	                            content: {
	                                "param.buyerOid": buyerOid,
	                                "ggSupplierCanDisputeGRN.valid":ggSupplierCanDisputeGRN,
	                                "ggPreventItemsNotExistInPO.valid":ggPreventItemsNotExistInPO,
	                                "ggPreventItemsLessThanPO.valid":ggPreventItemsLessThanPO,
	                                "ggPreventItemsQtyMoreThanPO.valid":ggPreventItemsQtyMoreThanPO,
	                                "csrfToken":csrfToken
	                                },
	                            load: function(jsonData)
	                            {
	                                var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
	                                infoDialog.show()
	                            }
	                        });
                        }
                        if(formName == 'invGlobalForm')
                        {
	                        xhr.get({
	                            url: '<s:url value="/buyer/saveBusinessRule.action" />',
	                            content: {
	                                "param.buyerOid": buyerOid,
	                                "igPreventItemsNotExistInPO.valid":igPreventItemsNotExistInPO,
	                                "csrfToken":csrfToken
	                                },
	                            load: function(jsonData)
	                            {
	                                var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
	                                infoDialog.show()
	                            }
	                        });
                        }
                        if(formName == 'giGlobalForm')
                        {
	                        xhr.get({
	                            url: '<s:url value="/buyer/saveBusinessRule.action" />',
	                            content: {
	                                "param.buyerOid": buyerOid,
	                                "gigPreventItemsNotExistInRtv.valid":gigPreventItemsNotExistInRtv,
	                                "gigPreventItemsLessThanRtv.valid":gigPreventItemsLessThanRtv,
	                                "gigPreventItemsQtyMoreThanRtv.valid":gigPreventItemsQtyMoreThanRtv,
	                                "csrfToken":csrfToken
	                                },
	                            load: function(jsonData)
	                            {
	                                var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
	                                infoDialog.show()
	                            }
	                        });
                        }
                        if(formName == 'dsdGlobalForm')
                        {
	                        xhr.get({
	                            url: '<s:url value="/buyer/saveBusinessRule.action" />',
	                            content: {
	                                "param.buyerOid": buyerOid,
	                                "dsdNeedValidateSalesData.valid":dsdNeedValidateSalesData,
	                                "csrfToken":csrfToken
	                                },
	                            load: function(jsonData)
	                            {
	                                var infoDialog = new InformationDialog({message: JSON.parse(jsonData)});
	                                infoDialog.show()
	                            }
	                        });
                        }
                    }
                    
                });

    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button data-dojo-type="dijit.form.Button" id="backBtn" ><s:text name="button.back" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="buyer.editBusinessRule"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
    </div>
    
    <div >
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
	</div>
    <s:token></s:token>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.profile"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td width="2px"><span class="required">*</span> </td>
       				<td width="30%">&nbsp;&nbsp;<s:text name="buyer.editBusinessRule.panel.profile.buyer"/></td>
				    <td>:</td>
					<td><s:property value="param.buyerCode" /> / <s:property value="param.buyerName" /></td>
				</tr>
				<tr>
					<td width="2px"><span class="required">*</span> </td>
       				<td>&nbsp;&nbsp;<s:text name="buyer.editBusinessRule.panel.profile.ruleGroup"/></td>
				    <td>:</td>
					<td>
                        <s:select id="funcGroup" name="funcGroup" list="funcGroups" data-dojo-type="dijit.form.Select" theme="simple" headerKey="" headerValue="%{getText('ALL')}"></s:select>
                    </td>
				</tr>
				<tr id="funcIdTr" style="display:<s:if test='funcGroup == null || funcGroup == ""'>none</s:if>">
                    <td width="2px"><span class="required">*</span> </td>
                    <td>&nbsp;&nbsp;<s:text name="buyer.editBusinessRule.panel.profile.ruleId"/></td>
                    <td>:</td>
                    <td id="funcIdTd">
                        <s:select id="funcId" onchange="showDiv();" name="funcId" list="funcIds" data-dojo-type="dijit.form.Select" theme="simple" headerKey="" headerValue="%{getText('ALL')}"></s:select>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
	
	<div class="space"></div>
	
    <div class="ruleDiv" id="Global_Global" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.global"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
                    <td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('globalForm');" ><s:text name="button.save" /></button>
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggAutoLogout.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggAutoLogout.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggMatchingJobMinBufferingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggMatchingJobMinBufferingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggMatchingJobMaxBufferingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggMatchingJobMaxBufferingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggDailyPoReportJobDaysBefore.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggDailyPoReportJobDaysBefore.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggDailyNotificationJobMissingGrnMinBufferingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggDailyNotificationJobMissingGrnMinBufferingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggDailyNotificationJobMissingGrnMaxBufferingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggDailyNotificationJobMissingGrnMaxBufferingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggDnGeneratingJobMatchingMinBuffingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggDnGeneratingJobMatchingMinBuffingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggDnGeneratingJobMatchingMaxBuffingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggDnGeneratingJobMatchingMaxBuffingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="ggRTVDnGeneratingJobBuffingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggRTVDnGeneratingJobBuffingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggRTVDnDisputeAlertWindow.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggRTVDnDisputeAlertWindow.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggContinueProcessErrorBatch.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="ggContinueProcessErrorBatch.valid"  data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggDailyNotificationJobMissingGiMinBufferingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggDailyNotificationJobMissingGiMinBufferingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggDailyNotificationJobMissingGiMaxBufferingDays.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggDailyNotificationJobMissingGiMaxBufferingDays.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>(Days)
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggDisableInvoicePaymentInstructions.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="ggDisableInvoicePaymentInstructions.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggRtvGiDnQtyTolerance.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                    	<input name="ggRtvGiDnQtyToleranceType.ruleValue"  data-dojo-type="dijit/form/RadioButton" value="true" <s:if test='ggRtvGiDnQtyToleranceType.ruleValue == "true" or ggRtvGiDnQtyToleranceType.ruleValue == null'>checked="checked"</s:if>/>
                    	Amount
                    	<input name="ggRtvGiDnQtyToleranceType.ruleValue"  data-dojo-type="dijit/form/RadioButton" value="false" <s:if test='ggRtvGiDnQtyToleranceType.ruleValue == "false"'>checked="checked"</s:if>/>
                    	Percent
                        <s:textfield name="ggRtvGiDnQtyTolerance.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggRtvGiDnPriceTolerance.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                    	<input name="ggRtvGiDnPriceToleranceType.ruleValue"  data-dojo-type="dijit/form/RadioButton" value="true" <s:if test='ggRtvGiDnPriceToleranceType.ruleValue == "true" or ggRtvGiDnPriceToleranceType.ruleValue == null'>checked="checked"</s:if>/>
                    	Amount
                    	<input name="ggRtvGiDnPriceToleranceType.ruleValue"  data-dojo-type="dijit/form/RadioButton" value="false" <s:if test='ggRtvGiDnPriceToleranceType.ruleValue == "false"'>checked="checked"</s:if>/>
                    	Percent
                        <s:textfield name="ggRtvGiDnPriceTolerance.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggRtvGiDnReportGeneratingDateRange.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="ggRtvGiDnReportGeneratingDateRange.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="6"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
	
	<div class="space"></div>
	
    <div class="ruleDiv" id="PoConvertInv_SorPO" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.sorPo"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
                    <td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('sorPOForm');" ><s:text name="button.save" /></button>
                    </td>
                </tr>
   				<tr>
                    <td align="left" width="500"><s:property value="pcispQtyEditable.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox onchange="showAndHide(this,'pcispQtyEditable',true);" theme="simple" name="pcispQtyEditable.valid" data-dojo-type="dijit.form.CheckBox"></s:checkbox>
                    </td>
                </tr>
                <tr style="display:<s:if test='!pcispQtyEditable.valid'>none</s:if>" class="pcispQtyEditable_tr" >
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="pcispQtylessThanPO.ruleDesc"/></td>
                    <td><span class="require"></span></td>
                    <td>
                        <s:checkbox cssClass="pcispQtyEditable" name="pcispQtylessThanPO.valid"  data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="pcispFocQtyEditable.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox onchange="showAndHide(this,'pcispFocQtyEditable',true);" name="pcispFocQtyEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr style="display:<s:if test='!pcispFocQtyEditable.valid'>none</s:if>" class="pcispFocQtyEditable_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="pcispFocQtylessThanPO.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox cssClass="pcispFocQtyEditable" name="pcispFocQtylessThanPO.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="pcispUnitPriceEditable.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox onchange="showAndHide(this,'pcispUnitPriceEditable',true);" name="pcispUnitPriceEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr style="display:<s:if test='!pcispUnitPriceEditable.valid'>none</s:if>" class="pcispUnitPriceEditable_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="pcispUnitPriceLessThanPO.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox cssClass="pcispUnitPriceEditable" name="pcispUnitPriceLessThanPO.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500">Summary discount information can be changed</td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcispDiscountEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>Trade&nbsp;&nbsp;
                        <s:checkbox name="pcispCashDiscountEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>Cash
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500">Detail discount information can be changed</td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcispDiscountForDetailEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="pcispEmailToStore.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcispEmailToStore.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="pcispPdfAsAttachment.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcispPdfAsAttachment.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="pcicpIgnoreExpiryDate.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcicpIgnoreExpiryDate.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
	
	<div class="space"></div>
	
    <div class="ruleDiv" id="PoConvertInv_ConPO" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.conPo"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
				<tr>
                    <td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('conPOForm');" ><s:text name="button.save" /></button>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500">Summary discount information can be changed</td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcicpTradeDiscountEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>Trade&nbsp;&nbsp;
                        <s:checkbox name="pcicpCashDiscountEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"></s:checkbox>Cash
                    </td>
                </tr>
				<tr>
                    <td align="left" width="500"><s:property value="pcicpItemDiscountEditable.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcicpItemDiscountEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
				<tr>
                    <td align="left" width="500"><s:property value="pcicpItemAmountEditable.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcicpItemAmountEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
				<tr>
                    <td align="left" width="500"><s:property value="pcicpItemSharedCostEditable.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pcicpItemSharedCostEditable.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
	
    <div class="space"></div>
    
    <div class="ruleDiv" id="Dn_Backend" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.backend"/>', width:275" style="width:99%">
        <table class="commtable">
            <tbody>
                <tr>
                    <td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('backendForm');" ><s:text name="button.save" /></button>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbAutoGenStockDn.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbAutoGenStockDn.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbAutoGenCostDn.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbAutoGenCostDn.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbAutoSendStockDn.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbAutoSendStockDn.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbAutoSendCostDn.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbAutoSendCostDn.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dnNoStyle1.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <input type="radio" name="dnNoStyle" checked="checked" data-dojo-type="dijit.form.RadioButton"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbNeedTranslate.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox onchange="showAndHide(this,'dbUnityFileStype',true);" name="dbNeedTranslate.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr style="display:<s:if test='!dbNeedTranslate.valid'>none</s:if>" class="dbUnityFileStype_tr">
                    <td align="left" width="500"><s:property value="dbUnityFileStype.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <input type="radio" name="dbFileStype" checked="checked" data-dojo-type="dijit.form.RadioButton"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbAutoGenDnFromGI.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbAutoGenDnFromGI.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbAllowSupplierDisputeMatchingDn.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbAllowSupplierDisputeMatchingDn.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbDiscrepancyReportToUser.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbDiscrepancyReportToUser.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbAutoCloseAcceptedRecord.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbAutoCloseAcceptedRecord.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="dbSendResolutionAndOutstandingByGroup.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dbSendResolutionAndOutstandingByGroup.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
	
	<div class="space"></div>
	
    <div class="ruleDiv" id="Matching_PoInvGrnDn" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.PoInvGrnDn"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('poInvGrnDnForm');" ><s:text name="button.save" /></button>
                    </td>
				</tr>
   				<tr>
       				<td align="left" width="500"><s:property value="mpigdQtyInvLessGrn.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
					<td>
                        <s:checkbox name="mpigdQtyInvLessGrn.valid" onchange="showAndHide(this,'mpigdQtyInvLessGrn',true);" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
				</tr>
   				<tr style="display:<s:if test='!mpigdQtyInvLessGrn.valid'>none</s:if>" class="mpigdQtyInvLessGrn_tr" >
       				<td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdQtyPoLessGrn.ruleDesc"/></td>
                    <td><span class="required"></span></td>
					<td>
                        <s:checkbox name="mpigdQtyPoLessGrn.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
				</tr>
   				<tr>
       				<td align="left" width="500"><s:property value="mpigdPriceInvLessPo.ruleDesc"/></td>
                    <td><span class="required"></span></td>
					<td>
                        <s:checkbox name="mpigdPriceInvLessPo.valid" onchange="showAndHide(this,'mpigdPriceInvLessPo',true);" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
				</tr>
   				<tr>
       				<td align="left" width="500"><s:property value="mpigdAmountTolerance.ruleDesc"/></td>
                    <td><span class="required"></span></td>
					<td>
                        <s:textfield name="mpigdAmountTolerance.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="8"/>
                    </td>
				</tr>
   				<tr style="display: none;">
       				<td align="left" width="500"><s:property value="mpigdMatchedReceipts.ruleDesc"/></td>
                    <td><span class="required"></span></td>
					<td>
                        <s:textfield name="mpigdMatchedReceipts.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                    </td>
				</tr>
   				<tr style="display: none;">
       				<td align="left" width="500"><s:property value="mpigdUnmatchedReceipts.ruleDesc"/></td>
                    <td><span class="required"></span></td>
					<td>
                        <s:textfield name="mpigdUnmatchedReceipts.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                    </td>
				</tr>
				<tr>
                    <td align="left" width="500"><s:property value="mpigdAutoApproveMatchedByDn.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdAutoApproveMatchedByDn.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="mpigdChangeInvDateToGrnDate.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdChangeInvDateToGrnDate.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="mpigdSkipMatching.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdSkipMatching.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
				<tr>
                    <td align="left" width="500"><s:property value="mpigdEnableSupplierToDispute.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox onchange="showAndHide(this,'mpigdEnableSupplierToDispute',true);" name="mpigdEnableSupplierToDispute.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
				<tr style="display:<s:if test='!mpigdEnableSupplierToDispute.valid'>none</s:if>" class="mpigdEnableSupplierToDispute_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdAutoApproveClosedAcceptedRecord.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdAutoApproveClosedAcceptedRecord.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
				<tr style="display:<s:if test='!mpigdEnableSupplierToDispute.valid'>none</s:if>" class="mpigdEnableSupplierToDispute_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdAutoCloseAcceptedRecord.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdAutoCloseAcceptedRecord.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
				<tr style="display:<s:if test='!mpigdEnableSupplierToDispute.valid'>none</s:if>" class="mpigdEnableSupplierToDispute_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdAutoCloseRejectedRecord.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdAutoCloseRejectedRecord.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr style="display:<s:if test='!mpigdEnableSupplierToDispute.valid'>none</s:if>" class="mpigdEnableSupplierToDispute_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdDiscrepancyReportToUser.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdDiscrepancyReportToUser.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr style="display:<s:if test='!mpigdEnableSupplierToDispute.valid'>none</s:if>" class="mpigdEnableSupplierToDispute_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdAutoRejectBuyerLossUnmatchedRecord.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdAutoRejectBuyerLossUnmatchedRecord.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr style="display:<s:if test='!mpigdEnableSupplierToDispute.valid'>none</s:if>" class="mpigdEnableSupplierToDispute_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdSendResolutionAndOutstandingByGroup.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdSendResolutionAndOutstandingByGroup.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr style="display:<s:if test='!mpigdEnableSupplierToDispute.valid'>none</s:if>" class="mpigdEnableSupplierToDispute_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdAutoAcceptQtyInvLessGrn.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdAutoAcceptQtyInvLessGrn.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr style="display:<s:if test='!mpigdEnableSupplierToDispute.valid'>none</s:if>" class="mpigdEnableSupplierToDispute_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="mpigdAutoAcceptPriceInvLessPo.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="mpigdAutoAcceptPriceInvLessPo.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
	
	<div class="space"></div>
	
    <div class="ruleDiv" id="SM_Importing" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.SM"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('importForm');" ><s:text name="button.save" /></button>
                    </td>
				</tr>
   				<tr>
       				<td align="left" width="535"><s:property value="smiGenAdminUser.ruleDesc"/></td>
                    <td><span class="required"></span></td>
					<td>
                        <s:checkbox name="smiGenAdminUser.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
				</tr>
   				<tr>
       				<td align="left" width="535"><s:property value="smiAdminRole.ruleDesc"/></td>
                    <td><span class="required"></span></td>
					<td>
                        <s:textfield name="smiAdminRole.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="20"/>
                    </td>
				</tr>
				<tr>
                    <td align="left" width="535"><s:property value="smiGenResultTxt.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:checkbox name="smiGenResultTxt.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
	
	<div class="space"></div>
	
    <div class="ruleDiv" id="ITEM_Importing" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.ITEM"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('itemForm');" ><s:text name="button.save" /></button>
                    </td>
				</tr>
   				<tr>
                    <td align="left" width="500"><s:property value="iiUpdate.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:if test='iiUpdate.valid || !iiDeleteAndInsert.valid'>
                            <input onchange="showAndHide(this,'iiRule',true);" type="radio" name="iiRule" checked="checked" data-dojo-type="dijit.form.RadioButton"/>
                        </s:if>
                        <s:else>
                            <input onchange="showAndHide(this,'iiRule',true);" type="radio" name="iiRule" data-dojo-type="dijit.form.RadioButton"/>
                        </s:else>
                    </td>
                </tr>
                <tr style="display:<s:if test='iiDeleteAndInsert.valid'>none</s:if>" class="iiRule_tr">
                    <td align="left" width="500" style="text-indent: 20px;"><s:property value="iiSelectOneToCompare.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:if test='iiSelectOneToCompare.valid'>
                            <input type="radio" name="iiCompareRule"  checked="checked" data-dojo-type="dijit.form.RadioButton"/>
                        </s:if>
                        <s:else>
                            <input type="radio" name="iiCompareRule" data-dojo-type="dijit.form.RadioButton"/>
                        </s:else>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="iiDeleteAndInsert.ruleDesc"/></td>
                    <td><span class="required"></span></td>
                    <td>
                        <s:if test='iiDeleteAndInsert.valid'>
                            <input type="radio" name="iiRule"  checked="checked" data-dojo-type="dijit.form.RadioButton"/>
                        </s:if>
                        <s:else>
                            <input type="radio" name="iiRule" data-dojo-type="dijit.form.RadioButton"/>
                        </s:else>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
    <div class="space"></div>
    <div class="ruleDiv" id="PO_Global" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.PO"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('poGlobalForm');" ><s:text name="button.save" /></button>
                    </td>
				</tr>
				<tr>
                    <td align="left" width="500"><s:property value="pgDeliveryDateRange.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:textfield name="pgDeliveryDateRange.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="20"/>(Days)
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="pgNeedValidateConPo.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="pgNeedValidateConPo.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
    <div class="space"></div>
    <div class="ruleDiv" id="GRN_Global" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.GRN"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('grnGlobalForm');" ><s:text name="button.save" /></button>
                    </td>
				</tr>
				<tr>
                    <td align="left" width="500"><s:property value="ggSupplierCanDisputeGRN.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="ggSupplierCanDisputeGRN.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
				<tr>
                    <td align="left" width="500"><s:property value="ggPreventItemsNotExistInPO.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="ggPreventItemsNotExistInPO.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggPreventItemsLessThanPO.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="ggPreventItemsLessThanPO.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="ggPreventItemsQtyMoreThanPO.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="ggPreventItemsQtyMoreThanPO.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
    <div class="space"></div>
    <div class="ruleDiv" id="INV_Global" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.INV"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('invGlobalForm');" ><s:text name="button.save" /></button>
                    </td>
				</tr>
				<tr>
                    <td align="left" width="500"><s:property value="igPreventItemsNotExistInPO.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="igPreventItemsNotExistInPO.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
    <div class="space"></div>
    <div class="ruleDiv" id="GI_Global" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.GI"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('giGlobalForm');" ><s:text name="button.save" /></button>
                    </td>
				</tr>
				<tr>
                    <td align="left" width="500"><s:property value="gigPreventItemsNotExistInRtv.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="gigPreventItemsNotExistInRtv.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="gigPreventItemsLessThanRtv.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="gigPreventItemsLessThanRtv.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr>
                    <td align="left" width="500"><s:property value="gigPreventItemsQtyMoreThanRtv.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="gigPreventItemsQtyMoreThanRtv.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
	<div class="space"></div>
    <div class="ruleDiv" id="DSD_Global" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.DSD"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td colspan="3" align="right">
                        <button data-dojo-type="dijit.form.Button" onclick="saveData('dsdGlobalForm');" ><s:text name="button.save" /></button>
                    </td>
				</tr>
				<tr>
                    <td align="left" width="500"><s:property value="dsdNeedValidateSalesData.ruleDesc"/></td>
                    <td width="40px"><span class="required"></span></td>
                    <td>
                        <s:checkbox name="dsdNeedValidateSalesData.valid" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
			</tbody>
		</table>
	</div>
    
    <div class="space"></div>
</body>
</html>
