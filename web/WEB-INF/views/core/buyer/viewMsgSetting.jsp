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
                "dojo/_base/xhr",
                "dojo/parser",
                "dijit/form/ValidationTextBox",
                "custom/ConfirmDialog",
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
                    xhr,
                    parser,
                    ValidationTextBox,
                    ConfirmDialog,
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
                    
                    
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/buyer/init.action?keepSp=Y" />');
                        }
                    });
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                    		changeToURL('<s:url value="/buyer/init.action?keepSp=Y" />');
                        }
                    );
                });
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="buyer.viewMsgSetting"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
    </div>
    
    <s:form id="mainForm" name="mainForm"   theme="simple"  method="post" enctype="multipart/form-data" >
    <s:token></s:token>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editMsgSetting.panel.profile"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td width="2px"><span class="required">*</span> </td>
       				<td width="30%">&nbsp;&nbsp;<s:text name="buyer.editMsgSetting.panel.profile.buyerName"/></td>
				    <td>:</td>
					<td><s:property value="param.buyerCode" /> / <s:property value="param.buyerName" /></td>
				</tr>
				<tr>
					<td width="2px"><span class="required">*</span> </td>
			       				<td>&nbsp;&nbsp;<s:text name="buyer.editMsgSetting.panel.profile.mailBox"/></td>
				    <td>:</td>
					<td><s:property value="param.mboxId" /></td>
				</tr>
			</tbody>
		</table>
	</div>
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editMsgSetting.panel.inbound" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
				<td style="font-weight:bold; width:3%">&nbsp;&nbsp;<s:text name="buyer.editMsgSetting.panel.no" /></td>
               	<td style="font-weight:bold; width:10%">&nbsp;&nbsp;<s:text name="buyer.editMsgSetting.panel.msgType" /></td>
               	<td style="font-weight:bold; width:10%"><s:text name="buyer.editMsgSetting.panel.fileFormat" /></td>
               	<td style="font-weight:bold; width:14%"><s:text name="buyer.editMsgSetting.panel.report" /></td>
               	<td style="font-weight:bold; width:20%"><s:text name="buyer.editMsgSetting.panel.reportName" /></td>
               	<td style="font-weight:bold"><s:text name="buyer.editMsgSetting.panel.email.alerts" /></td>
           	</tr>
	         <s:iterator value="inboundMsg" status="status"  id="item">
	           	<tr class="space"></tr>
			    <tr>
					<td>&nbsp;&nbsp;<s:property value="#status.index+1"/></td>
	               	<td>&nbsp;&nbsp;<span id="<s:property value="#item+#status.index+1"/>" onmouseover="dijit.Tooltip.defaultPosition=['above', 'below']"><s:property value="#item" /></span>
	               	<span data-dojo-type="dijit/Tooltip" data-dojo-props="connectId:'<s:property value="#item+#status.index+1"/>'">
	               	   <s:property value="inbounds[#item].msgDesc"/>
	               	</span>
	               	</td>
	               	<td>
	               	   <s:property value="inbounds[#item].fileFormat"/>
                    </td>
                    <td>
                        <s:if test="!inbounds[#item].customizedReport">
                            Standard
                        </s:if>
                        <s:else>
                            Customized
                        </s:else>
                    </td>
                    <td>
                        <s:if test="inbounds[#item].reportTemplate != null">
	                        <s:property value="inbounds[#item].reportTemplate"/>
                        </s:if>
                        <s:else>
                            normal
                        </s:else>
                    </td>
                    <td>
                        <table class="commtable">
                            <tr>
                                <td width="30%"><s:text name="buyer.editMsgSetting.panel.frequency" /></td>
                                <td width="30%">
                                    <s:if test="inbounds[#item].alertFrequency!='INTERVAL'">
                                        <s:text name="buyer.editMsgSetting.panel.perDoc" />
                                    </s:if>
                                    <s:else>
                                        <s:text name="buyer.editMsgSetting.panel.perInterval" />
                                    </s:else>
                                </td>
                                <td width="40%" rowspan="2">
                                    <s:if test="inbounds[#item].alertFrequency=='INTERVAL'">
                                    <s:property value="inbounds[#item].alertIntervalValue"/>
                                    <s:text name="buyer.editMsgSetting.panel.alertInterval"></s:text>
                                    </s:if>
                                </td>
                            </tr>
                            <tr>
                                <td><s:text name="buyer.editMsgSetting.panel.email" /></td>
                                <td><s:property value="inbounds[#item].rcpsAddrs"/></td>
                            </tr>
                        </table>
                    </td>
	           	</tr>
           	</s:iterator>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editMsgSetting.panel.outbound" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
				<td style="font-weight:bold; width:3%">&nbsp;&nbsp;<s:text name="buyer.editMsgSetting.panel.no" /></td>
               	<td style="font-weight:bold; width:10%">&nbsp;&nbsp;<s:text name="buyer.editMsgSetting.panel.msgType" /></td>
               	<td style="font-weight:bold; width:10%"><s:text name="buyer.editMsgSetting.panel.fileFormat" /></td>
                <td style="font-weight:bold; width:14%"><s:text name="buyer.editMsgSetting.panel.report" /></td>
                <td style="font-weight:bold; width:20%"><s:text name="buyer.editMsgSetting.panel.reportName" /></td>
               	<td style="font-weight:bold"><s:text name="buyer.editMsgSetting.panel.email.alerts" /></td>
           	</tr>
           	<s:iterator value="outboundMsg" status="status"  id="item">
	           	<tr class="space"></tr>
			    <tr>
					<td>&nbsp;&nbsp;<s:property value="#status.index+1"/></td>
	               	<td>&nbsp;&nbsp;<span id="<s:property value="#item+#status.index+1"/>" onmouseover="dijit.Tooltip.defaultPosition=['above', 'below']"><s:property value="#item" /></span>
                    <span data-dojo-type="dijit/Tooltip" data-dojo-props="connectId:'<s:property value="#item+#status.index+1"/>'">
                       <s:property value="outbounds[#item].msgDesc"/>
                    </span>
                    </td>
	               	<td>
                        <s:property value="outbounds[#item].fileFormat"/>
                    </td>
                    <td>
                         <s:if test="!outbounds[#item].customizedReport">
                             Standard
                         </s:if>
                         <s:else>
                             Customized
                         </s:else>
                    </td>
                    <td>
                       <s:if test="outbounds[#item].reportTemplate != null">
                            <s:property value="outbounds[#item].reportTemplate"/>
                        </s:if>
                        <s:else>
                            normal
                        </s:else>
                    </td>
	               	<td>
                        <table class="commtable">
                            <tr>
                                <td width="30%"><s:text name="buyer.editMsgSetting.panel.frequency" /></td>
                                <td width="30%"><s:text name="buyer.editMsgSetting.panel.perBatch" /></td>
                                <td width="40%" rowspan="2">
                                    <s:if test='outbounds[#item].excludeSucc==true' >
                                        <s:text name="buyer.editMsgSetting.panel.exclude.success" />
                                    </s:if>
                                    
                                </td>
                            </tr>
                            <tr>
                                <td><s:text name="buyer.editMsgSetting.panel.successEmail" /></td>
                                <td><s:property value="outbounds[#item].rcpsAddrs"/></td>
                            </tr>
                            <tr>
                                <td><s:text name="buyer.editMsgSetting.panel.errorEmail" /></td>
                                <td><s:property value="outbounds[#item].errorRcpsAddrs"/></td>
                            </tr>
                        </table>
                    </td>
	           	</tr>
           	</s:iterator>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.PoInvGrnDn" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td align="left" width="500"><s:property value="mpigdMatchedRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdMatchedRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdUnmatchedRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdUnmatchedRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdMatchingJobRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdMatchingJobRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdResolutionRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdResolutionRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdOutstandingRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdOutstandingRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdMissingGrnNotificationRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdMissingGrnNotificationRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdPriceDiscrepancyRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdPriceDiscrepancyRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdQtyDiscrepancyRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdQtyDiscrepancyRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdDefaultRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdDefaultRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdMissingGiNotificationRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdMissingGiNotificationRecipients.ruleValue"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.dataExport" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td align="left" width="500"><s:property value="mpigdInvoiceExportingRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="mpigdInvoiceExportingRecipients.ruleValue"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.editBusinessRule.panel.dn" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td align="left" width="500"><s:property value="dbResolutionRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="dbResolutionRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="dbOutstandingRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="dbOutstandingRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="dbPriceDiscrepancyRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="dbPriceDiscrepancyRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="dbQtyDiscrepancyRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="dbQtyDiscrepancyRecipients.ruleValue"/></td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="dbDnExportingRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td><s:property value="dbDnExportingRecipients.ruleValue"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    </s:form>
</body>
</html>
