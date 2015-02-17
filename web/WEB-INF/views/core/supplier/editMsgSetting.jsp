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
                "custom/ConfirmDialog",
                "custom/InformationDialog",
                "dijit/form/ValidationTextBox",
                "dijit/form/CheckBox",
                "dojo/NodeList-dom",
                "dojo/NodeList-fx",
                "dijit/form/Select",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    xhr,
                    parser,
                    ConfirmDialog,
                    InformationDialog,
                    ValidationTextBox,
                    CheckBox,
                    fx,
                    Select
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
                            submitForm('mainForm', '<s:url value="/supplier/saveEditMsgSetting.action" />'+'?supplier.supplierOid=<s:property value="supplier.supplierOid"/>&supplier.supplierCode=<s:property value="supplier.supplierCode"/>');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/supplier/initEditMsgSetting.action" />'+'?supplier.supplierOid=<s:property value="supplier.supplierOid"/>');
                        }
                    );
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/supplier/init.action?keepSp=Y" />');
                        }
                    });
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                    		confirmDialog.show();
                        }
                    );
                    
                    var emailFn = function(msgType)
                    {
                         xhr.get({
                             url: '<s:url value="/ajax/sentTestMailForMsgtype.action" />',
                             content: {msgType: msgType, emailAddrs: dom.byId('emailAdds' + msgType).value},
                             load: function(data)
                             {
                                 var infoDialog = new InformationDialog({message: data});
                                 
                                 infoDialog.show();
                                 
                                 on.once(dom.byId('btn' + msgType), 'click', function(){
		                            emailFn(msgType);
		                         });
                             }
                         });
                    };
                    
                    <s:iterator value="inboundMsg" status="status" id="item">
                        on.once(dom.byId('btn' + '<s:property value="#item" />'), 'click', function(){
                            var msgType = '<s:property value="#item" />';
                            
                            emailFn(msgType);
                        });
                    </s:iterator>
                    
                    <s:iterator value="outboundMsg" status="status" id="item">
                        on.once(dom.byId('btn' + '<s:property value="#item" />'), 'click', function(){
                            var msgType = '<s:property value="#item" />';
                            
                            emailFn(msgType);
                        });
                    </s:iterator>
                    
                });
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="saveBtn" ><s:text name="button.save" /></button>
            <button data-dojo-type="dijit.form.Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn"><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="supplier.edit.msgSetting"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <s:form id="mainForm" name="mainForm"   theme="simple"  method="post" enctype="multipart/form-data" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token/>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.edit.msgSetting.profile"/>', width:275" style="width:99%">
		<table class="commtable">
			<tbody>
   				<tr>
					<td width="2px"><span class="required">*</span> </td>
       				<td width="30%">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.profile.supplier"/></td>
				    <td>:</td>
					<td><s:property value="supplier.supplierName" /></td>
				</tr>
				<tr>
					<td width="2px"><span class="required">*</span> </td>
			       	<td>&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.profile.mbox"/></td>
				    <td>:</td>
					<td><s:property value="supplier.mboxId" /></td>
				</tr>
			</tbody>
		</table>
	</div>
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.edit.msgSetting.inbound" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
				<td style="font-weight:bold; width:15%">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.inbound.messageType" /></td>
               	<td style="font-weight:bold; width:60%">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.inbound.emailAlert" /></td>
               	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.init">
                	<td style="font-weight:bold;">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.fileFormat" /></td>
                </s:if>
                <s:else>
                   	<td style="font-weight:bold;display:none;">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.fileFormat" /></td>
                </s:else>
           	</tr>
	        <s:iterator value="inboundMsg" status="status" id="item">
	        	<tr class="space"></tr>
			    <tr>
	               	<td style="width:15%">&nbsp;&nbsp;<span id="<s:property value="#item+#status.index+1"/>" onmouseover="dijit.Tooltip.defaultPosition=['above', 'below']"><s:property value="#item" /></span>
                    <span data-dojo-type="dijit/Tooltip" data-dojo-props="connectId:'<s:property value="#item+#status.index+1"/>'">
                       <s:property value="inbounds[#item].msgDesc"/>
                    </span>
                    </td>
	               	<td style="width:60%">
	               		<table class="commtable">
	               			<tr>
	               				<td>
	               				    <input id="emailAdds<s:property value="#item" />" name="inbounds.<s:property value="#item" />.rcpsAddrs" value="<s:property value="inbounds[#item].rcpsAddrs"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="255" style="width:300px;"/>
	               				    <button id="btn<s:property value="#item" />" data-dojo-type="dijit/form/Button" ><s:text name="button.sentTestMsg" /></button>
	               				</td>
	               			</tr>
	               		</table>
	               	</td>
	               
	               <s:if test="inbounds[#item].fileFormatList != null">
                    <s:if test="#session.SESSION_CURRENT_USER_PROFILE.init">
	                   	<td>
                        	<s:select name='inbounds.%{item}.fileFormat' list="inbounds[#item].fileFormatList"   data-dojo-type="dijit/form/Select" theme="simple"></s:select>
                   	 	</td>
	                </s:if>
	                <s:else>
                   		<td>
                        	<s:select name='inbounds.%{item}.fileFormat' list="inbounds[#item].fileFormatList"   data-dojo-type="dijit/form/Select" theme="simple" cssStyle="display:none;"></s:select>
                   	 	</td>
	                </s:else>
	               </s:if>
	           	</tr>
	           	<tr><td colspan="7"><hr color="#E5F2FE" size="0.5px"></td></tr>
           	</s:iterator>   
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.edit.msgSetting.oubound" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td style="font-weight:bold; width:15%">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.oubound.messageType" /></td>
                <td style="font-weight:bold; width:60%">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.oubound.emailAlert" /></td>
                <s:if test="#session.SESSION_CURRENT_USER_PROFILE.init">
                	<td style="font-weight:bold;">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.fileFormat" /></td>
                </s:if>
                <s:else>
                   	<td style="font-weight:bold;display:none;" >&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.fileFormat" /></td>
                </s:else>
           	</tr>
           	<s:iterator value="outboundMsg" status="status"  id="item">
	           	<tr class="space"></tr>
			    <tr>
	               	<td style="width:15%">&nbsp;&nbsp;<span id="<s:property value="#item+#status.index+1"/>" onmouseover="dijit.Tooltip.defaultPosition=['above', 'below']"><s:property value="#item" /></span>
                    <span data-dojo-type="dijit/Tooltip" data-dojo-props="connectId:'<s:property value="#item+#status.index+1"/>'">
                       <s:property value="outbounds[#item].msgDesc"/>
                    </span>
                    </td>
	               	<td style="width:15%">
	               		<table class="commtable">
	               	    	<tr>
	               				<td>
	               				    <input id="emailAdds<s:property value="#item" />" name="outbounds.<s:property value="#item" />.rcpsAddrs" value="<s:property value="outbounds[#item].rcpsAddrs"/>" data-dojo-type="dijit.form.ValidationTextBox" maxlength="255" style="width:300px;"/>
	               				    <button id="btn<s:property value="#item" />" data-dojo-type="dijit/form/Button" ><s:text name="button.sentTestMsg" /></button>
	               				</td>
	               				<td>
	               					<s:if test='outbounds[#item].excludeSucc==true' >
										<input type="checkbox" checked="checked" name='outbounds.<s:property value="#item" />.excludeSucc' value="true"  data-dojo-type="dijit.form.CheckBox"/>
									</s:if>
									<s:else>
										<input type="checkbox"  name='outbounds.<s:property value="#item" />.excludeSucc' value="false"  data-dojo-type="dijit.form.CheckBox"/>
									</s:else>
	               					<s:text name="supplier.edit.msgSetting.exclude.success" />
               					</td>
	               			</tr>
	               		</table>
	               	</td>
	               	<s:property value=""/>
	               	<s:if test="outbounds[#item].fileFormatList != null">
                    <s:if test="#session.SESSION_CURRENT_USER_PROFILE.init">
                    	<td>
                       	 	<s:select name='outbounds.%{item}.fileFormat' list="outbounds[#item].fileFormatList"  data-dojo-type="dijit/form/Select" theme="simple"></s:select>
                    	</td>
                    </s:if>
                    <s:else>
                    	<td>
                       	 	<s:select name='outbounds.%{item}.fileFormat' list="outbounds[#item].fileFormatList"  data-dojo-type="dijit/form/Select" theme="simple" cssStyle="display:none;"></s:select>
                    	</td>
                    </s:else>
                    </s:if>
	           	</tr>
	           	<tr><td colspan="7"><hr color="#E5F2FE" size="0.5px"></td></tr>
           	</s:iterator>
           	
        </tbody>
        </table>
    </div>
    </s:form>
</body>
</html>
