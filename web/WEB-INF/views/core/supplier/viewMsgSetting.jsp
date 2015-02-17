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
                    
                    
                });
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn"><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="supplier.view.msgSetting"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <s:form id="mainForm" name="mainForm"   theme="simple"  method="post" enctype="multipart/form-data" >
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
               	<td style="font-weight:bold;">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.fileFormat" /></td>
           	</tr>
	        <s:iterator value="inboundMsg" status="status" id="item">
	        	<tr class="space"></tr>
			    <tr>
	               	<td style="width:15%">&nbsp;&nbsp;<span id="<s:property value="#item+#status.index+1"/>" onmouseover="dijit.Tooltip.defaultPosition=['above', 'below']"><s:property value="#item" /></span>
                    <span data-dojo-type="dijit/Tooltip" data-dojo-props="connectId:'<s:property value="#item+#status.index+1"/>'">
                       <s:property value="inbounds[#item].msgDesc"/>
                    </span>
                    </td>
	               	<td width="60%">
	               		<table class="commtable">
	               			<tr>
	               				<td width="40%">
	               				    <s:property value="inbounds[#item].rcpsAddrs"/>
	               				</td>
	               			</tr>
	               		</table>
	               	</td>
	               	<td width="25%">
	               	   <s:property value="inbounds[#item].fileFormat"/>
                    </td>
	           	</tr>
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
                <td style="font-weight:bold;">&nbsp;&nbsp;<s:text name="supplier.edit.msgSetting.fileFormat" /></td>
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
	               				<td width="40%">
	               				    <s:property value="outbounds[#item].rcpsAddrs"/>
	               				</td>
	               				<td>
	               					<s:if test='outbounds[#item].excludeSucc==true' >
										<s:text name="supplier.edit.msgSetting.exclude.success" />
									</s:if>
               					</td>
	               			</tr>
	               		</table>
	               	</td>
	               	<td>
	               	   <s:property value="outbounds[#item].fileFormat"/>
                    </td>
	           	</tr>
           	</s:iterator>
           	
        </tbody>
        </table>
    </div>
    </s:form>
</body>
</html>
