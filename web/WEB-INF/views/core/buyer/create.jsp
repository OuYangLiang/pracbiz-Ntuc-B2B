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
                            
                            submitForm('mainForm', '<s:url value="/buyer/saveAdd.action" />');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/buyer/initAdd.action" />');
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
        <div class="title"><s:text name="buyer.create"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post"  enctype="multipart/form-data"  >
    <input type="hidden" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token></s:token>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.create.panel.profile" />', width:275" style="width:99%">
        <table class="commtable" >
        <tbody>
            <tr>
                <td><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerCode"/></td>
                <td>:</td>
                <td><s:textfield name="param.buyerCode" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="20"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerName"/></td>
                <td>:</td>
                <td><s:textfield name="param.buyerName"  data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.buyerAlias"/></td>
                <td>:</td>
                <td><s:textfield name="param.buyerAlias"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.regNo"/></td>
                <td>:</td>
                <td><s:textfield name="param.regNo"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.gstRegNo"/></td>
                <td>:</td>
                <td><s:textfield name="param.gstRegNo"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.gstPercent"/></td>
                <td>:</td>
                <td><s:textfield name="buyerEx.gstPercentStr"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="5"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.otherRegNo"/></td>
                <td>:</td>
                <td><s:textfield name="param.otherRegNo"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.branch"/></td>
                <td>:</td>
                <td><s:checkbox name="param.branch"  data-dojo-type="dijit.form.CheckBox" theme="simple" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.active"/></td>
                <td>:</td>
                <td><s:checkbox id="param.active" name="param.active"  data-dojo-type="dijit.form.CheckBox" theme="simple" onchange="changeAction();"/></td>
            </tr>
            <tr id="block">
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.blocked"/></td>
                <td>:</td>
                <td><s:checkbox id="param.blocked" name="param.blocked"  data-dojo-type="dijit.form.CheckBox" theme="simple" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.profile.logo"/></td>
                <td>:</td>
                <td><s:file  name="logo" data-dojo-type="dojox.form.Uploader"  data-dojo-props="showInput:'before'" theme="simple"/></td>
            </tr>
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
                <td><s:textfield name="param.address1" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="param.address2"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="param.address3"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="param.address4"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.state"/></td>
                <td>:</td>
                <td><s:textfield name="param.state"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.postalCode"/></td>
                <td>:</td>
                <td><s:textfield name="param.postalCode"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="15"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.country"/></td>
                <td>:</td>
                <td>
					<s:select id="countrySelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="param.ctryCode" list="countries" 
                        listKey="ctryCode" listValue="ctryDesc" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/>
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
                <td><s:textfield name="param.contactName" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="50"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactTel"/></td>
                <td>:</td>
                <td><s:textfield name="param.contactTel" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="30"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactMobile"/></td>
                <td>:</td>
                <td><s:textfield name="param.contactMobile"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="30" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactFax"/></td>
                <td>:</td>
                <td><s:textfield name="param.contactFax"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="30"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.contact.contactEmail" /></td>
                <td>:</td>
                <td><s:textfield name="param.contactEmail" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="100"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.create.panel.communication" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.deploymentMode"/></td>
                <td>:</td>
                <td><s:select id="deploymentModeSelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="param.deploymentMode" list="deploymentMode" 
                        listKey="key" listValue="%{getText(Value)}" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/></td>
            </tr>
            <tr id="channelTr" style="<s:if test='param.deploymentMode != null && param.deploymentMode.name() != "REMOTE"'>display: none</s:if>">
                <td><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.channel"/></td>
                <td>:</td>
                <td><s:select id="channelSelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="param.channel" list="channels" 
                         headerKey="" headerValue="%{getText('select.select')}" theme="simple"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="buyer.create.panel.communication.mboxId"/></td>
                <td>:</td>
                <td><s:textfield name="param.mboxId" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="10"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    </form>
</body>
</html>
