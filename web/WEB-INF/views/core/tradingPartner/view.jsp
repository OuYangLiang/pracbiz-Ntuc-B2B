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
                "dijit/form/Button",
                "dojo/parser",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    Button,
                    parser
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
	                    
	                on(registry.byId("cancelBtn"), "click", function(){
	                   changeToURL('<s:url value="/tp/init.action?keepSp=Y" />');
	                });
                });
                
    </script>
</head>

<body class="claro">
	
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <button id="cancelBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.cancel' /></button>
        </td></tr></tbody></table>
    </div>
	<!-- here is message area -->
	 
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <div class="pageBar">
        <div class="title"><s:text name='tp.view.pageBar' /></div>
    </div>
    <form id="mainForm" name="mainForm" method="post" >
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="tp.create.buyerarea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.buyerarea.buyerCode' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.buyerCode" />
					</td>
                </tr>
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.buyerarea.buyerName' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.buyerName" />
					</td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.buyerarea.contactPerson' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.buyerContactPerson" />
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.buyerarea.email' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.buyerContactEmail" />
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.buyerarea.telephone' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.buyerContactTel" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.view.buyerarea.mobile' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.buyerContactMobile" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.view.buyerarea.fax' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.buyerContactFax" />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="tp.view.supplierarea.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.supplierarea.supplierCode' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.supplierCode" />
					</td>
                </tr>
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.supplierarea.supplierName' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.supplierName" />
					</td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.supplierarea.contactPerson' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.supplierContactPerson" />
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.supplierarea.email' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.supplierContactEmail" />
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.supplierarea.telephone' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.supplierContactTel" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.view.supplierarea.mobile' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.supplierContactMobile" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.view.supplierarea.fax' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.supplierContactFax" />
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required"></span></td>
                    <td width="30%"><s:text name='tp.view.supplierarea.termsConditions' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.termCondition1" />
                    </td>
                </tr>
                <tr height="25px">
                	<td colspan="3"></td>
                    <td>
                    	<s:property value="tradingPartner.termCondition2" />
                    </td>
                </tr>
                <tr height="25px">
                	<td colspan="3"></td>
                    <td>
                    	<s:property value="tradingPartner.termCondition3" />
                    </td>
                </tr>
                <tr height="25px">
                	<td colspan="3"></td>
                    <td>
                    	<s:property value="tradingPartner.termCondition4" />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div id="supplierList" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="tp.view.integration.title" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.integration.buyerSupplierCode' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.buyerSupplierCode" />
                    </td>
                </tr>
                <tr height="25px">
                	<td><span class="required">*</span></td>
                    <td width="30%"><s:text name='tp.view.integration.supplierBuyerCode' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:property value="tradingPartner.supplierBuyerCode" />
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%">
                    <s:text name='tp.create.integration.concessive' /><br>
                    <span class="required"><s:text name='tp.concessive.description' /></span>
                    </td>
                    <td width="2%">:</td>
                    <td>
                    	<s:if test="tradingPartner.concessive">
                    		<s:text name='Value.Yes' />
                    	</s:if>
                    	<s:else>
                    		<s:text name='Value.No' />
                    	</s:else>
                    </td>
                </tr>
                <tr height="25px">
                	<td></td>
                    <td width="30%"><s:text name='tp.create.integration.status' /></td>
                    <td>:</td>
                    <td>
                    	<s:if test="tradingPartner.active">
                    		<s:text name='Value.enable' />
                    	</s:if>
                    	<s:else>
                    		<s:text name='Value.disable' />
                    	</s:else>
					</td>
                </tr>
            </tbody>
        </table>
    </div>
    </form>
</body>
</html>
