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
                "dojo/dom-style",
                "dijit/registry",
                "dojo/on",
                "dojo/parser",
                "dijit/form/Select",
                "dijit/form/ValidationTextBox",
                "custom/ConfirmDialog",                
                "dijit/form/CheckBox",
                "dojox/form/Uploader",
                "dijit/form/RadioButton",
                "dojo/NodeList-dom",
                "dojo/NodeList-fx",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    domStyle,
                    registry,
                    on,
                    parser,
                    Select,
                    ValidationTextBox,
                    ConfirmDialog,
                    CheckBox,
                    Uploader,
                    RadioButton,
                    fx
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                
					if(dom.byId("gstOn"))
					{
					    on(dom.byId("gstOn"), 'click', function(){
					        if (dom.byId('gstOn').checked)
					        {
								domStyle.set('gstRegNo', 'display', '');
								domStyle.set('gstPercent', 'display', '');
					        }
							else
							{
								domStyle.set('gstRegNo', 'display', 'none');
								domStyle.set('gstPercent', 'display', 'none');
							}
					    });
					}
                	 
                    if(dom.byId("gstOff"))
                    {
                        on(dom.byId("gstOff"), 'click', function(){
                            if (dom.byId('gstOff').checked)
                            {
                                domStyle.set('gstRegNo', 'display', 'none');
                                domStyle.set('gstPercent', 'display', 'none');
                            }
                            else
                            {
								domStyle.set('gstRegNo', 'display', '');
								domStyle.set('gstPercent', 'display', '');
                            }
                        });
                    }
                    
					if (dom.byId("autoInvOn"))
					{
					    on(dom.byId("autoInvOn"), 'click', function(){
							if (dom.byId('autoInvOn').checked)
							{
                                domStyle.set('startNumber', 'display', '');
							}
							else
							{
                                domStyle.set('startNumber', 'display', 'none');
							}
					    });
					}
                         
                         
                    if (dom.byId("autoInvOff"))
                    {
                        on(dom.byId("autoInvOff"), 'click', function(){
                            if (dom.byId('autoInvOff').checked)
                            {
                                domStyle.set('startNumber', 'display', 'none');
                            }
                            else
                            {
                                domStyle.set('startNumber', 'display', '');
                            }
                        });
                    }
                     
                    on(registry.byId("active"), 'change', function(value){
                        if (value == true)
                        {
                            domStyle.set('blocked',"display","");
                        }
                        else
                        {
                            domStyle.set('blocked',"display","none");
                        }
                    });

                    on(registry.byId("deploymentModeSelect"), 'change', 
                   		function(value)
                        {
                            if(value=='LOCAL')
                            {
                                dom.byId("channelTr").style.display = 'none';
                                dom.byId("clientDiv").style.display = '';
                                registry.byId("channelSelect").set('value','');
                            }
                            else if(value=='REMOTE')
                            {
                                dom.byId("channelTr").style.display = '';
                                dom.byId("clientDiv").style.display = 'none';
                                registry.byId("clientEnabled").set('value',false);
                                document.getElementsByName("supplier.requireTranslationIn")[0].checked=true;
                                document.getElementsByName("supplier.requireTranslationIn")[1].checked=false;
                                registry.byId("requireReport").set('value',false);
                                registry.byId("requireTranslationOut").set('value',false);
                            }
                            else
                            {
                                dom.byId("channelTr").style.display = 'none';
                                registry.byId("channelSelect").set('value','');
                                dom.byId("clientDiv").style.display = 'none';
                                registry.byId("clientEnabled").set('value',false);
                                document.getElementsByName("supplier.requireTranslationIn")[0].checked=true;
                                document.getElementsByName("supplier.requireTranslationIn")[1].checked=false;
                                registry.byId("requireReport").set('value',false);
                                registry.byId("requireTranslationOut").set('value',false);
                            }
                        }
                    );
                    
                    if(dom.byId("isChangeLogo"))
                    {
                    	on(registry.byId("isChangeLogo"), 'change', function(value){
                            if (value == false)
                            {
                                domStyle.set('block',"display","none");
                            }
                            else
                            {
                                domStyle.set('block',"display","");
                            }
                        });
                    } 
                    
                    on(registry.byId("saveBtn"), 'click', function(){
                        submitForm('mainForm', '<s:url value="/supplier/saveEdit.action" />'+'?supplier.supplierOid=<s:property value="supplier.supplierOid"/>&supplier.liveDate=<s:property value="supplier.liveDate" />');
                    });
                    
                    on(registry.byId("resetBtn"), 'click', function(){
                        changeToURL('<s:url value="/supplier/initEdit.action" />'+'?supplier.supplierOid=<s:property value="supplier.supplierOid"/>');
                    });
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/supplier/init.action?keepSp=Y" />');
                        }
                    });
                    
                    on(registry.byId("cancelBtn"), 'click', function(){
                        confirmDialog.show();
                    });
                });
    </script>
</head>

<body class="claro">
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button data-dojo-type="dijit.form.Button" id="saveBtn" ><s:text name="button.save" /></button>
            <button data-dojo-type="dijit.form.Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button data-dojo-type="dijit.form.Button" id="cancelBtn"><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="supplier.update.panel.profile"/></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <s:form id="mainForm" name="mainForm"   theme="simple"  method="post" enctype="multipart/form-data" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token/>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.summary.summary" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.supplierCode"/></td>
                <td>:</td>
                <td><s:property value="supplier.supplierCode"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.supplierName"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.supplierName"  data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.supplierAlias"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.supplierAlias"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.regNo"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.regNo"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.otherRegNo"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.otherRegNo"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.source"/></td>
                <td>:</td>
                <td><s:select data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="supplier.supplierSource" 
                        list="source" theme="simple"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.gst"/></td>
                <td>:</td>
                <td>
                	<input type="radio" name="isGst" <s:if test='isGst == "Y"'>checked="checked"</s:if>
                        id="gstOn" value="Y" data-dojo-type="dijit.form.RadioButton"/>
					<label for="on">Turn On</label> 
					
					<input type="radio" id="gstOff" <s:if test='isGst == "N"'>checked="checked"</s:if>
                        name="isGst" value="N" data-dojo-type="dijit.form.RadioButton"/>
					<label for="off" >Turn Off</label>
                </td>
            </tr>
            
            <tr id="gstRegNo" <s:if test='isGst == "N"'>style="display: none;"</s:if>>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.gstRegNo"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.gstRegNo" data-dojo-props="required: true"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="50"/></td>
            </tr>
            
            
            <tr id="gstPercent" <s:if test='isGst == "N"'>style="display: none;"</s:if>>
               <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.gstPercent"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.gstPercentStr" data-dojo-props="required: true"  data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="5"/></td>
            </tr>
            
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.autoInvNum"/></td>
                <td>:</td>
                <td>
                    <input type="radio" name="isStartNum" <s:if test='isStartNum == "Y"'>checked="checked"</s:if>
                        id="autoInvOn" value="Y" data-dojo-type="dijit.form.RadioButton"/>
					<label>Turn On</label> 
					
                    <input type="radio" id="autoInvOff" <s:if test='isStartNum == "N"'>checked="checked"</s:if>
                        name="isStartNum" value="N" data-dojo-type="dijit.form.RadioButton"/>
					<label>Turn Off</label>
                </td>
            </tr>
            
            <tr id="startNumber" <s:if test='isStartNum == "N"'>style="display: none;"</s:if>>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.startNum"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.startNumberStr" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" maxlength="50"/></td>
            </tr>
            
            <tr <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 1 && #session.SESSION_CURRENT_USER_PROFILE.userType != 2" >style="display: none;"</s:if>>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.active"/></td>
                <td>:</td>
                <td><s:checkbox name="supplier.active" id="active" data-dojo-type="dijit.form.CheckBox" theme="simple" /></td>
            </tr>
            
            <tr id="blocked" <s:if test="(#session.SESSION_CURRENT_USER_PROFILE.userType != 1 && #session.SESSION_CURRENT_USER_PROFILE.userType != 2) || !supplier.active" >style="display: none;"</s:if>>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.block"/></td>
                <td>:</td>
                <td><s:checkbox name="supplier.blocked"  data-dojo-type="dijit.form.CheckBox" theme="simple" /></td>
            </tr>
           <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.logo"/></td>
                <td>:</td>
                <td>
                	<s:if test="supplier.logo != null">
	               		<img src="<s:url value="/supplier/viewLogoImage.action"><s:param name="supplier.supplierOid" value="%{supplier.supplierOid}" /></s:url>"  height="45" width="115"/>
	               	</s:if>
	               	<s:else>
	               		<s:file name="logo" type="file" data-dojo-props="showInput:'before'" data-dojo-type="dojox.form.Uploader" theme="simple"/>
	               	</s:else>
                </td>
            </tr>
            <s:if test="supplier.logo !=null">
            <tr >
            	<td></td>
            	<td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.changeLogo"/></td>
            	<td>:</td>
            	<td>
            	   <table>
            	   		<tbody>
            	   			<tr>
            	   				<td>
            	   					<s:checkbox id="isChangeLogo" name="isChangeLogo" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
            	   				</td>
            	   				<td>
            	   					<div id="block" style="display: none;">
            	                      <s:file name="logo" type="file" data-dojo-props="showInput:'before'" data-dojo-type="dojox.form.Uploader" theme="simple"/>
            	                    </div>
            	   				</td>
            	   			</tr>
            	   		</tbody>
            	   </table>
            	</td>
            </tr>
            </s:if>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.dateCreated"/></td>
                <td>:</td>
                <td><s:property value="supplier.createDate" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.create.panel.contact" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.address"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.address1" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="supplier.address2"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="supplier.address3"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;</td>
                <td></td>
                <td><s:textfield name="supplier.address4"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.state"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.state"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.postalCode"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.postalCode"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="15"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.country"/></td>
                <td>:</td>
                <td>
					<s:select id="countrySelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="supplier.ctryCode" list="countries" 
                        listKey="ctryCode" listValue="ctryDesc" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/>
				</td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.currency"/></td>
                <td>:</td>
                <td>
					<s:select id="currencySelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="supplier.currCode" list="currencies" 
                        listKey="currCode" listValue="currDesc" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/>
				</td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactName"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.contactName" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="50"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactTel"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.contactTel" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="30"/></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactMobile"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.contactMobile"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="30" /></td>
            </tr>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactFax"/></td>
                <td>:</td>
                <td><s:textfield name="supplier.contactFax"  data-dojo-type="dijit.form.ValidationTextBox"  maxlength="30"/></td>
            </tr>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.contact.contactEmail" /></td>
                <td>:</td>
                <td><s:textfield name="supplier.contactEmail" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox"  maxlength="100"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.create.panel.communication" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.communication.deploymentMode"/></td>
                <td width="20">:</td>
                <td><s:select id="deploymentModeSelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="supplier.deploymentMode" list="deploymentMode" 
                        listKey="key" listValue="%{getText(Value)}" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/></td>
            </tr>
            <tr id="channelTr" style="<s:if test='supplier != null && supplier.deploymentMode != null && supplier.deploymentMode.name() != "REMOTE"'>display: none</s:if>">
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.communication.channel"/></td>
                <td width="20">:</td>
                <td><s:select id="channelSelect" data-dojo-type="dijit.form.Select" data-dojo-props="required: true,maxHeight:-1" name="supplier.channel" list="channels" 
                         headerKey="" headerValue="%{getText('select.select')}" theme="simple"/></td>
            </tr>
            <tr>
                <td width="20"><span class="required">*</span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.communication.mboxId"/></td>
                <td width="20">:</td>
                <td><s:property value="supplier.mboxId"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
            
    <div id="clientDiv"  data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.create.panel.client.document.setup" />', width:275" style="width:99%;
        <s:if test='supplier == null || supplier.deploymentMode == null || supplier.deploymentMode.name() != "LOCAL"'>display: none</s:if>">
        <table class="commtable">
        <tbody>
            <tr>
                <td></td>
                <td>&nbsp;&nbsp;<s:text name="supplier.create.panel.profile.clientEnabled"/></td>
                <td>:</td>
                <td><s:checkbox id="clientEnabled" name="supplier.clientEnabled"  data-dojo-type="dijit.form.CheckBox" theme="simple" /></td>
            </tr>
            
            <tr>
                <td width="20"><span class="required"></span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.client.document.setup.inbound"/></td>
                <td width="20">:</td>
                <td>
                    <s:if test="supplier.requireTranslationIn == null || !supplier.requireTranslationIn">
                        <input name="supplier.requireTranslationIn"  data-dojo-type="dijit/form/RadioButton" value="false" checked="checked"/>
                        <s:text name="supplier.create.panel.client.document.setup.standard"></s:text>
                        <input name="supplier.requireTranslationIn"  data-dojo-type="dijit/form/RadioButton" value="true"/>
                        <s:text name="supplier.create.panel.client.document.setup.translated"></s:text>
                    </s:if>
                    <s:else>
                        <input name="supplier.requireTranslationIn"  data-dojo-type="dijit/form/RadioButton" value="false"/>
                        <s:text name="supplier.create.panel.client.document.setup.standard"></s:text>
                        <input name="supplier.requireTranslationIn"  data-dojo-type="dijit/form/RadioButton" value="true" checked="checked"/>
                        <s:text name="supplier.create.panel.client.document.setup.translated"></s:text>
                    </s:else>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    <s:checkbox id="requireReport" name="supplier.requireReport"  data-dojo-type="dijit.form.CheckBox" theme="simple" />
                    <s:text name="supplier.create.panel.client.document.setup.pdf"></s:text>
                </td>
            </tr>
            <tr>
                <td width="20"><span class="required"></span></td>
                <td width="300">&nbsp;&nbsp;<s:text name="supplier.create.panel.client.document.setup.outbound"/></td>
                <td width="20">:</td>
                <td><s:checkbox id="requireTranslationOut" name="supplier.requireTranslationOut"  data-dojo-type="dijit.form.CheckBox" theme="simple" />
                <s:text name="supplier.create.panel.client.document.setup.require.translation"/></td>
            </tr>
        </tbody>
        </table>
    </div>   
    </s:form>
</body>
</html>
