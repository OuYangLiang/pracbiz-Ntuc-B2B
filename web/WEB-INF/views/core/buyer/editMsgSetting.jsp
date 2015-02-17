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
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            
                            submitForm('mainForm', '<s:url value="/buyer/saveEditMsgSetting.action" />'+'?param.buyerOid=<s:property value="param.buyerOid"/>');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/buyer/initEditMsgSetting.action" />'+'?param.buyerOid=<s:property value="param.buyerOid"/>');
                        }
                    );
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/buyer/init.action?keepSp=Y" />');
                        }
                    });
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                    		confirmDialog.show();
                        }
                    );
                });
         function selectReport(item, bound, subType)
         {
             var name = bound + '.' + item + '.subTypeReportMap.' + subType + '.customizedReport';
             var obj = document.getElementsByName(name);
             var standard = document.getElementById(item+'_'+subType+'_reportTemplate_1');
             var customized = document.getElementById(item+'_'+subType+'_reportTemplate_2');
             if(obj[0].checked)
             {
                 if(customized != null)
                 {
                	 customized.style.display = 'none';
                 }
            	 if(standard != null)
            	 {
            		 standard.style.display = '';
            	 }
             }
             else
             {
                 if(standard != null)
                 {
                     standard.style.display = 'none';
                 }
            	 if(customized != null)
                 {
                     customized.style.display = '';
                 }
             }
         } 
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="saveBtn" ><s:text name="button.save" /></button>
            <button data-dojo-type="dijit.form.Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="buyer.editMsgSetting"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
    </div>
    
    <s:form id="mainForm" name="mainForm"   theme="simple"  method="post" enctype="multipart/form-data" >
    <input type="hidden" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
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
               	<td style="font-weight:bold; width:12%">&nbsp;&nbsp;<s:text name="buyer.editMsgSetting.panel.msgType" /></td>
               	<td style="font-weight:bold; width:8%"><s:text name="buyer.editMsgSetting.panel.fileFormat" /></td>
               	<td style="font-weight:bold; width:14%"><s:text name="buyer.editMsgSetting.panel.subType" /></td>
               	<td style="font-weight:bold; width:10%"><s:text name="buyer.editMsgSetting.panel.report" /></td>
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
	               	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.init">
	               	<td>
                        <s:select  data-dojo-type="dijit/form/Select" theme="simple" name='inbounds.%{item}.fileFormat' list="inbounds[#item].fileFormatList"></s:select>
                    </td>
                    <td colspan="3">
                        <table class="commtable">
                        
                            <s:iterator value="inbounds[#item].subTypeReportMap" status="innerStatus"  id="innerItem">
                            <tr>
                                <td width="30%">
                                    <s:if test="#innerItem.value.subType!='default'">
                                        <s:property value="#innerItem.value.subType"/>
                                    </s:if>
                                </td>
			                    <td width="25%">
			                        <s:if test="!#innerItem.value.customized.isEmpty()">
			                            <s:if test="!#innerItem.value.customizedReport">
					                        <input data-dojo-type="dijit/form/RadioButton"  onchange="selectReport('<s:property value="#item"/>','inbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
					                           name="inbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="false" checked='checked'/>Standard<br/>
					                        <input data-dojo-type="dijit/form/RadioButton"  onchange="selectReport('<s:property value="#item"/>','inbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
					                           name="inbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="true"/>Customized
			                            </s:if>
			                            <s:else>
					                        <input data-dojo-type="dijit/form/RadioButton"  onchange="selectReport('<s:property value="#item"/>','inbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
					                           name="inbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="false"/>Standard<br/>
					                        <input data-dojo-type="dijit/form/RadioButton"  onchange="selectReport('<s:property value="#item"/>','inbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
					                           name="inbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="true" checked='checked'/>Customized
			                            </s:else>
			                        </s:if>
			                    </td>
			                    <td width="45%">
			                       <s:if test="!#innerItem.value.customizedReport">
			                           <s:if test="!#innerItem.value.standard.isEmpty()">
			                               <span style="display:inline;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_1"><s:select  data-dojo-type="dijit/form/Select" 
			                               name='inbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.standardReportTemplate' 
			                                  list="#innerItem.value.standard" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
			                           </s:if>
			                           <s:if test="!#innerItem.value.customized.isEmpty()">
			                               <span style="display:none;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_2"><s:select  data-dojo-type="dijit/form/Select" 
			                               name='inbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.customizedReportTemplate' 
			                                  list="#innerItem.value.customized" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
			                           </s:if>
			                       </s:if>
			                       <s:else>
			                           <s:if test="!#innerItem.value.customized.isEmpty()">
			                               <span style="display:inline;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_2"><s:select  data-dojo-type="dijit/form/Select" 
			                               name='inbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.customizedReportTemplate' 
			                                  list="#innerItem.value.customized" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
			                           </s:if>
			                           <s:if test="!#innerItem.value.standard.isEmpty()">
			                               <span style="display:none;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_1"><s:select  data-dojo-type="dijit/form/Select" 
			                               name='inbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.standardReportTemplate' 
			                                  list="#innerItem.value.standard" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
			                           </s:if>
			                       </s:else>
			                    </td>
                            </tr>
                            </s:iterator>
                        </table>
                    </td>
                    </s:if>
                    <s:else>
                    <td>
                        <s:select readonly='true' data-dojo-type="dijit/form/Select" theme="simple" name='inbounds.%{item}.fileFormat' list="inbounds[#item].fileFormatList"></s:select>
                    </td>
                    <td colspan="3">
                        <table class="commtable">
                        
                            <s:iterator value="inbounds[#item].subTypeReportMap" status="innerStatus"  id="innerItem">
                            <tr>
                                <td width="30%">
                                    <s:if test="#innerItem.value.subType!='default'">
                                        <s:property value="#innerItem.value.subType"/>
                                    </s:if>
                                </td>
                                <td width="25%">
                                    <s:if test="!#innerItem.value.customized.isEmpty()">
                                        <s:if test="!#innerItem.value.customizedReport">
                                            <input data-dojo-type="dijit/form/RadioButton" readonly onchange="selectReport('<s:property value="#item"/>','inbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="inbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="false" checked='checked'/>Standard<br/>
                                            <input data-dojo-type="dijit/form/RadioButton" readonly onchange="selectReport('<s:property value="#item"/>','inbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="inbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="true"/>Customized
                                        </s:if>
                                        <s:else>
                                            <input data-dojo-type="dijit/form/RadioButton" readonly onchange="selectReport('<s:property value="#item"/>','inbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="inbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="false"/>Standard<br/>
                                            <input data-dojo-type="dijit/form/RadioButton" v onchange="selectReport('<s:property value="#item"/>','inbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="inbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="true" checked='checked'/>Customized
                                        </s:else>
                                    </s:if>
                                </td>
                                <td width="45%">
                                   <s:if test="!#innerItem.value.customizedReport">
                                       <s:if test="!#innerItem.value.standard.isEmpty()">
                                           <span style="display:inline;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_1"><s:select  data-dojo-type="dijit/form/Select" 
                                           name='inbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.standardReportTemplate'  readonly='true'
                                              list="#innerItem.value.standard" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                       <s:if test="!#innerItem.value.customized.isEmpty()">
                                           <span style="display:none;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_2"><s:select  data-dojo-type="dijit/form/Select" 
                                           name='inbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.customizedReportTemplate'  readonly='true'
                                              list="#innerItem.value.customized" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                   </s:if>
                                   <s:else>
                                       <s:if test="!#innerItem.value.customized.isEmpty()">
                                           <span style="display:inline;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_2"><s:select  data-dojo-type="dijit/form/Select" 
                                           name='inbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.customizedReportTemplate'  readonly='true'
                                              list="#innerItem.value.customized" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                       <s:if test="!#innerItem.value.standard.isEmpty()">
                                           <span style="display:none;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_1"><s:select  data-dojo-type="dijit/form/Select" 
                                           name='inbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.standardReportTemplate'  readonly='true'
                                              list="#innerItem.value.standard" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                   </s:else>
                                </td>
                            </tr>
                            </s:iterator>
                        </table>
                    </td>
                    </s:else>
                    <td>
                        <table class="commtable">
                            <tr>
                                <td><s:text name="buyer.editMsgSetting.panel.frequency" /></td>
                                <td>
                                    <s:if test="inbounds[#item].alertFrequency!='INTERVAL'">
                                        <input data-dojo-type="dijit/form/RadioButton" checked="checked" type="radio" name="inbounds.<s:property value="#item" />.alertFrequency" value="DOC" /><s:text name="buyer.editMsgSetting.panel.perDoc" />
                                    </s:if>
                                    <s:else>
                                        <input data-dojo-type="dijit/form/RadioButton" type="radio" name="inbounds.<s:property value="#item" />.alertFrequency" value="DOC" /><s:text name="buyer.editMsgSetting.panel.perDoc" />
                                    </s:else>
                                    <s:if test="inbounds[#item].alertFrequency=='INTERVAL'">
                                        <input data-dojo-type="dijit/form/RadioButton" checked="checked" type="radio" name="inbounds.<s:property value="#item" />.alertFrequency" value="INTERVAL" /><s:text name="buyer.editMsgSetting.panel.perInterval" />
                                    </s:if>
                                    <s:else>
                                        <input data-dojo-type="dijit/form/RadioButton" type="radio" name="inbounds.<s:property value="#item" />.alertFrequency" value="INTERVAL" /><s:text name="buyer.editMsgSetting.panel.perInterval" />
                                    </s:else>
                                </td>
                                <td rowspan="2">
                                    <input style=" width:50px" name="inbounds.<s:property value="#item" />.alertIntervalValue" value="<s:property value="inbounds[#item].alertIntervalValue"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="6"/>
                                    <s:text name="buyer.editMsgSetting.panel.alertInterval"></s:text>
                                </td>
                            </tr>
                            <tr>
                                <td><s:text name="buyer.editMsgSetting.panel.email" /></td>
                                <td><input name="inbounds.<s:property value="#item" />.rcpsAddrs" value="<s:property value="inbounds[#item].rcpsAddrs"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="255" style="width:225px;"/></td>
                            </tr>
                        </table>
                    </td>
	           	</tr>
	           	<tr><td colspan="7"><hr color="#E5F2FE" size="0.5px"></td></tr>
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
               	<td style="font-weight:bold; width:12%">&nbsp;&nbsp;<s:text name="buyer.editMsgSetting.panel.msgType" /></td>
               	<td style="font-weight:bold; width:8%"><s:text name="buyer.editMsgSetting.panel.fileFormat" /></td>
               	<td style="font-weight:bold; width:14%"><s:text name="buyer.editMsgSetting.panel.subType" /></td>
                <td style="font-weight:bold; width:10%"><s:text name="buyer.editMsgSetting.panel.report" /></td>
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
                    <s:if test="#session.SESSION_CURRENT_USER_PROFILE.init">
	               	<td>
                        <s:select  data-dojo-type="dijit/form/Select" name='outbounds.%{item}.fileFormat' list="outbounds[#item].fileFormatList"></s:select>
                    </td>
                    <td colspan="3">
                        <table class="commtable">
                            <s:iterator value="outbounds[#item].subTypeReportMap" status="innerStatus"  id="innerItem">
                            <tr>
                                <td width="30%">
                                    <s:if test="#innerItem.value.subType!='default'">
		                                <s:property value="#innerItem.value.subType"/>
                                    </s:if>
                                </td>
                                <td width="25%">
                                    <s:if test="!#innerItem.value.customized.isEmpty()">
                                        <s:if test="!#innerItem.value.customizedReport">
                                            <input data-dojo-type="dijit/form/RadioButton"  onchange="selectReport('<s:property value="#item"/>','outbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="outbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="false" checked='checked'/>Standard<br/>
                                            <input data-dojo-type="dijit/form/RadioButton"  onchange="selectReport('<s:property value="#item"/>','outbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="outbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="true"/>Customized
                                        </s:if>
                                        <s:else>
                                            <input data-dojo-type="dijit/form/RadioButton"  onchange="selectReport('<s:property value="#item"/>','outbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="outbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="false"/>Standard<br/>
                                            <input data-dojo-type="dijit/form/RadioButton"  onchange="selectReport('<s:property value="#item"/>','outbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="outbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="true" checked='checked'/>Customized
                                        </s:else>
                                    </s:if>
                                </td>
                                <td width="45%">
                                   <s:if test="!#innerItem.value.customizedReport">
                                       <s:if test="!#innerItem.value.standard.isEmpty()">
                                           <span style="display:inline;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_1"><s:select  data-dojo-type="dijit/form/Select" 
                                           name='outbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.standardReportTemplate' 
                                              list="#innerItem.value.standard" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                       <s:if test="!#innerItem.value.customized.isEmpty()">
                                           <span style="display:none;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_2"><s:select  data-dojo-type="dijit/form/Select" 
                                           name='outbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.customizedReportTemplate' 
                                              list="#innerItem.value.customized" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                   </s:if>
                                   <s:else>
                                       <s:if test="!#innerItem.value.customized.isEmpty()">
                                           <span style="display:inline;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_2"><s:select  data-dojo-type="dijit/form/Select" 
                                           name='outbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.customizedReportTemplate' 
                                              list="#innerItem.value.customized" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                       <s:if test="!#innerItem.value.standard.isEmpty()">
                                           <span style="display:none;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_1"><s:select  data-dojo-type="dijit/form/Select" 
                                           name='outbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.standardReportTemplate' 
                                              list="#innerItem.value.standard" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                   </s:else>
                                </td>
                            </tr>
                            </s:iterator>
                        </table>
                    </td>
                    </s:if>
                    <s:else>
	               	<td>
                        <s:select readonly='true'  data-dojo-type="dijit/form/Select" name='outbounds.%{item}.fileFormat' list="outbounds[#item].fileFormatList"></s:select>
                    </td>
                    <td colspan="3">
                        <table class="commtable">
                            <s:iterator value="outbounds[#item].subTypeReportMap" status="innerStatus"  id="innerItem">
                            <tr>
                                <td width="30%">
                                    <s:if test="#innerItem.value.subType!='default'">
		                                <s:property value="#innerItem.value.subType"/>
                                    </s:if>
                                </td>
                                <td width="25%">
                                    <s:if test="!#innerItem.value.customized.isEmpty()">
                                        <s:if test="!#innerItem.value.customizedReport">
                                            <input data-dojo-type="dijit/form/RadioButton" readonly  onchange="selectReport('<s:property value="#item"/>','outbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="outbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="false" checked='checked'/>Standard<br/>
                                            <input data-dojo-type="dijit/form/RadioButton" readonly  onchange="selectReport('<s:property value="#item"/>','outbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="outbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="true"/>Customized
                                        </s:if>
                                        <s:else>
                                            <input data-dojo-type="dijit/form/RadioButton" readonly onchange="selectReport('<s:property value="#item"/>','outbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="outbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="false"/>Standard<br/>
                                            <input data-dojo-type="dijit/form/RadioButton" readonly onchange="selectReport('<s:property value="#item"/>','outbounds','<s:property value="#innerItem.value.subType"/>');" type="radio" 
                                               name="outbounds.<s:property value="#item"/>.subTypeReportMap.<s:property value="#innerItem.value.subType"/>.customizedReport" value="true" checked='checked'/>Customized
                                        </s:else>
                                    </s:if>
                                </td>
                                <td width="45%">
                                   <s:if test="!#innerItem.value.customizedReport">
                                       <s:if test="!#innerItem.value.standard.isEmpty()">
                                           <span style="display:inline;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_1"><s:select readonly='true' data-dojo-type="dijit/form/Select" 
                                           name='outbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.standardReportTemplate' 
                                              list="#innerItem.value.standard" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                       <s:if test="!#innerItem.value.customized.isEmpty()">
                                           <span style="display:none;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_2"><s:select readonly='true' data-dojo-type="dijit/form/Select" 
                                           name='outbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.customizedReportTemplate' 
                                              list="#innerItem.value.customized" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                   </s:if>
                                   <s:else>
                                       <s:if test="!#innerItem.value.customized.isEmpty()">
                                           <span style="display:inline;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_2"><s:select readonly='true' data-dojo-type="dijit/form/Select" 
                                           name='outbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.customizedReportTemplate' 
                                              list="#innerItem.value.customized" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                       <s:if test="!#innerItem.value.standard.isEmpty()">
                                           <span style="display:none;" id="<s:property value="#item"/>_<s:property value="#innerItem.value.subType"/>_reportTemplate_1"><s:select readonly='true' data-dojo-type="dijit/form/Select" 
                                           name='outbounds.%{item}.subTypeReportMap.%{#innerItem.value.subType}.standardReportTemplate' 
                                              list="#innerItem.value.standard" listKey="Key" listValue="Key" headerKey="" headerValue="%{getText('select.select')}" theme="simple"></s:select></span>
                                       </s:if>
                                   </s:else>
                                </td>
                            </tr>
                            </s:iterator>
                        </table>
                    </td>
                    </s:else>
	               	<td>
                        <table class="commtable">
                            <tr>
                                <td><s:text name="buyer.editMsgSetting.panel.frequency" /></td>
                                <td><s:text name="buyer.editMsgSetting.panel.perBatch" /></td>
                            </tr>
                            <tr>
                                <s:if test="outbounds[#item].basicData">
	                                <td><s:text name="buyer.editMsgSetting.panel.email" /></td>
	                                <td><input name="outbounds.<s:property value="#item" />.rcpsAddrs" value="<s:property value="outbounds[#item].rcpsAddrs"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="255"  style="width:225px;"/></td>
                                </s:if>
                                <s:else>
	                                <td colspan="2">
	                                    <table>
	                                        <tr>
				                                <td><s:text name="buyer.editMsgSetting.panel.successEmail" /></td>
				                                <td><input name="outbounds.<s:property value="#item" />.rcpsAddrs" value="<s:property value="outbounds[#item].rcpsAddrs"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="255"  style="width:225px;"/></td>
	                                        </tr>
	                                        <tr>
				                                <td><s:text name="buyer.editMsgSetting.panel.errorEmail" /></td>
				                                <td><input name="outbounds.<s:property value="#item" />.errorRcpsAddrs" value="<s:property value="outbounds[#item].errorRcpsAddrs"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="255"  style="width:225px;"/></td>
	                                        </tr>
	                                    </table>
	                                </td>
                                </s:else>
                            </tr>
                        </table>
                    </td>
	           	</tr>
	           	<tr height="0.5px;"><td colspan="7"><hr color="#E5F2FE" size="0.5px"></td></tr>
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
                <td>
                    <s:textfield name="mpigdMatchedRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdUnmatchedRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdUnmatchedRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdMatchingJobRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdMatchingJobRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdResolutionRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdResolutionRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdOutstandingRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdOutstandingRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdMissingGrnNotificationRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdMissingGrnNotificationRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdPriceDiscrepancyRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdPriceDiscrepancyRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdQtyDiscrepancyRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdQtyDiscrepancyRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdDefaultRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdDefaultRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdMissingGiNotificationRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdMissingGiNotificationRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="mpigdRtvGiDnExceptionReportRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="mpigdRtvGiDnExceptionReportRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
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
                <td>
                    <s:textfield name="mpigdInvoiceExportingRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
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
                <td>
                    <s:textfield name="dbResolutionRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="dbOutstandingRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="dbOutstandingRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="dbPriceDiscrepancyRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="dbPriceDiscrepancyRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="dbQtyDiscrepancyRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="dbQtyDiscrepancyRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
            <tr>
                <td align="left" width="500"><s:property value="dbDnExportingRecipients.ruleDesc"/></td>
                <td><span class="required"></span></td>
                <td>
                    <s:textfield name="dbDnExportingRecipients.ruleValue" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="255"/>
                </td>
            </tr>
        </tbody>
        </table>
    </div>
    </s:form>
</body>
</html>
