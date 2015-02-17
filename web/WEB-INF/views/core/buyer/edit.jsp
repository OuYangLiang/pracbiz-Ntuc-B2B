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
                "dijit/form/Select",
                "dijit/form/ValidationTextBox",
                "custom/ConfirmDialog",
                "dijit/form/CheckBox",
                "dojox/form/Uploader",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    xhr,
                    parser,
                    Select,
                    ValidationTextBox,
                    ConfirmDialog,
                    CheckBox,
                    Uploader
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
                            
                            submitForm('mainForm', '<s:url value="/buyer/saveEdit.action" />'+'?param.buyerOid=<s:property value="param.buyerOid"/>');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/buyer/initEdit.action" />'+'?param.buyerOid=<s:property value="param.buyerOid"/>');
                        }
                    );
                    
                    on(registry.byId("deploymentModeSelect"), 'change', 
                        function(value)
                        {
                            if(value=='LOCAL')
                            {
                                dom.byId("channelTr").style.display = 'none';
                                registry.byId("channelSelect").set('value','');
                            }
                            else if(value=='REMOTE')
                            {
                                dom.byId("channelTr").style.display = '';
                            }
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

        function changeAction()
        {
			var action = document.getElementById("param.active");
			var block = document.getElementById("block");
			if(action.checked)
			{
				block.style.display = "";
			}
			else
			{
				block.style.display = "none";
			}
        }

        function changeLogo()
        {
			var isChangeLogo = document.getElementById("isChangeLogo");
			var logoDiv = document.getElementById("logoDiv");
			if(isChangeLogo.checked)
			{
				logoDiv.style.display = "";
			}
			else
			{
				logoDiv.style.display = "none";
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
        <div class="title"><s:text name="buyer.edit"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <s:form id="mainForm" name="mainForm"   theme="simple"  method="post" enctype="multipart/form-data" >
    <input type="hidden" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token></s:token>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.create.panel.profile" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerCode"/></td>
                <td>:</td>
                <td><s:property value="param.buyerCode"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerName"/></td>
                <td>:</td>
                <td><s:textfield name="param.buyerName"  data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerAlias"/></td>
                <td>:</td>
                <td><s:textfield name="param.buyerAlias"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.regNo"/></td>
                <td>:</td>
                <td><s:textfield name="param.regNo"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.gstRegNo"/></td>
                <td>:</td>
                <td><s:textfield name="param.gstRegNo"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.gstPercent"/></td>
                <td>:</td>
                <td><s:textfield name="buyerEx.gstPercentStr"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="5"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.otherRegNo"/></td>
                <td>:</td>
                <td><s:textfield name="param.otherRegNo"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.branch"/></td>
                <td>:</td>
                <td><s:checkbox name="param.branch"  data-dojo-type="dijit.form.CheckBox"  /></td>
            </tr>
            <tr <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 1" >style="display: none;"</s:if>>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.active"/></td>
                <td>:</td>
                <td><s:checkbox id="param.active" name="param.active"  data-dojo-type="dijit.form.CheckBox" theme="simple" onchange="changeAction();"/></td>
            </tr>
            <tr id="block" <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 1 || !param.active" >style="display: none;"</s:if>>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.blocked"/></td>
                <td>:</td>
                <td><s:checkbox id="param.blocked" name="param.blocked"  data-dojo-type="dijit.form.CheckBox" theme="simple" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.logo"/></td>
                <td>:</td>
                <td>
                	<s:if test="param.logo != null">
	               		<img src="<s:url value="/buyer/viewLogoImage.action"><s:param name="param.buyerOid" value="%{param.buyerOid}" /></s:url>"  height="45" width="115"/>
	               	</s:if>
	               	<s:else>
	               		<s:file name="logo" type="file" data-dojo-props="showInput:'before'" data-dojo-type="dojox.form.Uploader" theme="simple"/>
	               	</s:else>
                </td>
            </tr>
            <s:if test="param.logo !=null">
            <tr >
            	<td></td>
            	<td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.changeLogo"/></td>
            	<td>:</td>
            	<td>
            	   <table>
            	   		<tbody>
            	   			<tr>
            	   				<td>
            	   					<s:checkbox id="isChangeLogo" name="isChangeLogo" data-dojo-type="dijit.form.CheckBox" theme="simple" onclick="changeLogo();"/>
            	   				</td>
            	   				<td>
            	   					<div id="logoDiv" style="display: none;">
            	                      <s:file name="logo" type="file" data-dojo-props="showInput:'before'" data-dojo-type="dojox.form.Uploader" theme="simple"/>
            	                    </div>
            	   				</td>
            	   			</tr>
            	   		</tbody>
            	   </table>
            	</td>
            </tr>
            </s:if>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.create.panel.contact" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.address"/></td>
                <td>:</td>
                <td><s:textfield name="param.address1" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="param.address2"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="param.address3"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="param.address4"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.state"/></td>
                <td>:</td>
                <td><s:textfield name="param.state"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.postalCode"/></td>
                <td>:</td>
                <td><s:textfield name="param.postalCode"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="15"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.country"/></td>
                <td>:</td>
                <td>
					<s:select id="countrySelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="param.ctryCode" list="countries" 
                        listKey="ctryCode" listValue="ctryDesc" headerKey="" headerValue="%{getText('select.select')}" />
				</td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.currency"/></td>
                <td>:</td>
                <td>
					<s:select id="currencySelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="param.currCode" list="currencies" 
                        listKey="currCode" listValue="currDesc" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/>
				</td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactName"/></td>
                <td>:</td>
                <td><s:textfield name="param.contactName" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactTel"/></td>
                <td>:</td>
                <td><s:textfield name="param.contactTel" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="30"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactMobile"/></td>
                <td>:</td>
                <td><s:textfield name="param.contactMobile"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="30" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactFax"/></td>
                <td>:</td>
                <td><s:textfield name="param.contactFax"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="30"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactEmail" /></td>
                <td>:</td>
                <td><s:textfield name="param.contactEmail" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.create.panel.communication" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.deploymentMode"/></td>
                <td width="20">:</td>
                <td><s:select id="deploymentModeSelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="param.deploymentMode" list="deploymentMode" 
                        listKey="key" listValue="%{getText(Value)}" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/></td>
            </tr>
            <tr id="channelTr" style="<s:if test='param.deploymentMode.name() != "REMOTE"'>display: none</s:if>">
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.channel"/></td>
                <td width="20">:</td>
                <td><s:select id="channelSelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="param.channel" list="channels" 
                        headerKey="" headerValue="%{getText('select.select')}" /></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.mboxId"/></td>
                <td>:</td>
                <td><s:property value="param.mboxId" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    </s:form>
</body>
</html>
