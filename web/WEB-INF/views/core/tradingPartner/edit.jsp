<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dijit/registry",
                "dojo/on",
                "dojo/_base/xhr",
                "dijit/form/Button",
                "dojo/parser",
                "dijit/form/Select",
                "dijit/form/CheckBox",
                "dijit/form/ValidationTextBox",
                "custom/ConfirmDialog",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    xhr,
                    Button,
                    parser,
                    Select,
                    CheckBox,
                    ValidationTextBox,
                    ConfirmDialog
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');

                    
                    on(registry.byId("buyerListSelect"), 'change', 
                        function(value)
                        {
                        	xhr.get({
                                url: '<s:url value="/ajax/findBuyerByKey.action" />',
                                handleAs: "json",
                                content: {companyOid: value},
                                load: function(data)
                                {
                                	dom.byId('buyerContactPerson').value=data.contactName;
                                	dom.byId('buyerContactEmail').value=data.contactEmail;
                                	dom.byId('buyerContactTel').value=data.contactTel;
                                	dom.byId('buyerContactMobile').value=data.contactMobile;
                                	dom.byId('buyerContactFax').value=data.contactFax;
                                }
                            });
                            
                        }
                    );
                    
                    on(registry.byId("supplierListSelect"), 'change', 
                        function(value)
                        {
                        	xhr.get({
                                url: '<s:url value="/ajax/findSupplierByKey.action" />',
                                handleAs: "json",
                                content: {companyOid: value},
                                load: function(data)
                                {
                                	dom.byId('supplierContactPerson').value=data.contactName;
                                	dom.byId('supplierContactEmail').value=data.contactEmail;
                                	dom.byId('supplierContactTel').value=data.contactTel;
                                	dom.byId('supplierContactMobile').value=data.contactMobile;
                                	dom.byId('supplierContactFax').value=data.contactFax;
                                }
                            });

                        	xhr.get({
                                url: '<s:url value="/ajax/findDefaultTermConditionBySupplier.action" />',
                                handleAs: "json",
                                content: {companyOid: value},
                                load: function(data)
                                {
                                	var tcCode = null;
                                    if (data == null)
                                    {
                                    	dom.byId('termCondition1').value="";
	                                    dom.byId('termCondition2').value="";
	                                    dom.byId('termCondition3').value="";
	                                    dom.byId('termCondition4').value="";
                                    }
                                    else
                                    {
	                                	dom.byId('termCondition1').value=data.termCondition1;
	                                    dom.byId('termCondition2').value=data.termCondition2;
	                                    dom.byId('termCondition3').value=data.termCondition3;
	                                    dom.byId('termCondition4').value=data.termCondition4;
	                                    tcCode = data.termConditionCode;
                                    }
                                    xhr.get({
                                        url: '<s:url value="/tp/putParamIntoSession.action" />',
                                        content: {termConditionCode: tcCode, supplierOid: value},
                                        load: function(data)
                                        {
	                                        
                                        }
                                    });
                                }
                            });
                        }
                    );
                    
                    on(registry.byId("selectTermsConditions"),'click',
                    	function()
                    	{
                    		var supplierOid = registry.byId("supplierListSelect").get("value");
                    		var url = '<s:url value="/popup/popupViewTermCondition.action" />?tradingPartner.supplierOid=' + supplierOid + 
                    					'&tradingPartner.tradingPartnerOid='+'<s:property value="tradingPartner.tradingPartnerOid" />';
                    		window.open (url,'','width=600,height=400,scrollbars=1');
                    	}
                    );
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            
                            submitForm('mainForm', '<s:url value="/tp/saveEdit.action" />?tradingPartner.tradingPartnerOid='+'<s:property value="tradingPartner.tradingPartnerOid" />');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/tp/initEdit.action" />?tradingPartner.tradingPartnerOid='+'<s:property value="tradingPartner.tradingPartnerOid" />');
                        }
                    );
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/tp/init.action?keepSp=Y" />');
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

<body class="claro">
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button id="saveBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.save' /></button>
            <button id="resetBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.reset' /></button>
            <button id="cancelBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.cancel' /></button>
        </td></tr></tbody></table>
    </div>
	<!-- here is message area -->
	 
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <div class="pageBar">
        <div class="title"><s:text name='tp.edit.pageBar' /></div>
    </div>
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token />
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="tp.edit.buyerarea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.buyerarea.buyer' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2" >
	                    	<s:select id="buyerListSelect" name="tradingPartner.buyerOid" list="buyerList"
									listKey="buyerOid" listValue="buyerName" data-dojo-props="readOnly: true"
									data-dojo-type="dijit.form.Select" theme="simple"/>
						</s:if>
						<s:else>
							<s:select id="buyerListSelect" name="tradingPartner.buyerOid" list="buyerList"
									listKey="buyerOid" listValue="buyerName" data-dojo-props="readOnly: true"
									data-dojo-type="dijit.form.Select" theme="simple"/>
						</s:else>
					</td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.buyerarea.contactPerson' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.buyerContactPerson" id="buyerContactPerson" maxlength="50" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.buyerarea.email' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.buyerContactEmail" id="buyerContactEmail" maxlength="100" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.buyerarea.telephone' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.buyerContactTel" id="buyerContactTel" maxlength="30" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.edit.buyerarea.mobile' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.buyerContactMobile" id="buyerContactMobile" maxlength="30" data-dojo-type="dijit.form.TextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.edit.buyerarea.fax' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.buyerContactFax" id="buyerContactFax" maxlength="30" data-dojo-type="dijit.form.TextBox" theme="simple"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="tp.edit.supplierarea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.supplierarea.supplier' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 3" >
	                    	<s:select id="supplierListSelect" name="tradingPartner.supplierOid" list="supplierList"
									listKey="supplierOid" listValue="supplierName" data-dojo-props="readOnly: true"
									data-dojo-type="dijit.form.Select" theme="simple"/>
						</s:if>
						<s:else>
							<s:select id="supplierListSelect" name="tradingPartner.supplierOid" list="supplierList"
									listKey="supplierOid" listValue="supplierName" data-dojo-props="readOnly: true"
									data-dojo-type="dijit.form.Select" theme="simple"/>
						</s:else>
					</td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.supplierarea.contactPerson' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.supplierContactPerson" id="supplierContactPerson" maxlength="50" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.supplierarea.email' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.supplierContactEmail" id="supplierContactEmail" maxlength="100" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.supplierarea.telephone' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.supplierContactTel" id="supplierContactTel" maxlength="30" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.edit.supplierarea.mobile' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.supplierContactMobile" id="supplierContactMobile" maxlength="30" data-dojo-type="dijit.form.TextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.edit.supplierarea.fax' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.supplierContactFax" id="supplierContactFax" maxlength="30" data-dojo-type="dijit.form.TextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.edit.supplierarea.termsConditions' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.termCondition1" id="termCondition1" readonly="true" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                    	<button id="selectTermsConditions" data-dojo-type="dijit.form.Button" ><s:text name='button.select' /></button>
                    </td>
                </tr>
                <tr height="25px">
                	<td colspan="3"></td>
                    <td>
                    	<s:textfield name="tradingPartner.termCondition2" id="termCondition2" readonly="true" data-dojo-type="dijit.form.TextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td colspan="3"></td>
                    <td>
                    	<s:textfield name="tradingPartner.termCondition3" id="termCondition3" readonly="true" data-dojo-type="dijit.form.TextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td colspan="3"></td>
                    <td>
                    	<s:textfield name="tradingPartner.termCondition4" id="termCondition4" readonly="true" data-dojo-type="dijit.form.TextBox" theme="simple"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div id="supplierList" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="tp.edit.integration.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.edit.integration.buyerSupplierCode' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:hidden name="tradingPartner.buyerSupplierCode"/>
                    	<s:property value="tradingPartner.buyerSupplierCode" />
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required"></span></td>
                    <td width="30%"><s:text name='tp.edit.integration.supplierBuyerCode' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:textfield name="tradingPartner.supplierBuyerCode" maxlength="10" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.edit.integration.concessive' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:checkbox id="concessive" name="tradingPartner.concessive" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.edit.integration.status' /></td>
                    <td>:</td>
                    <td>
                    	<s:select name="tradingPartner.active" list="#{true:getText('select.enable'),false:getText('select.disable')}"
								listKey="key" listValue="value" data-dojo-type="dijit.form.Select" theme="simple"/>
					</td>
                </tr>
            </tbody>
        </table>
    </div>
    </form>
</body>
</html>
