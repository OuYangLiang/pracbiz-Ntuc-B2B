<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dojo/dom-style",
                "dijit/registry",
                "dojo/on",
                "dojo/_base/xhr",
                "dijit/form/Button",
                "dojo/parser",
                "dijit/form/Select",
                "dijit/form/ValidationTextBox",
                "dijit/form/MultiSelect",
                "custom/ConfirmDialog",
                "dojo/_base/array",
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
                    xhr,
                    Button,
                    parser,
                    Select,
                    ValidationTextBox,
                    MultiSelect,
                    ConfirmDialog,
                    array,
                    fx
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                    
                    
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                    	   changeToURL('<s:url value="/supplierSet/init.action?keepSp=Y" />');
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
        <div class="title"><s:text name="supplierSet.view.title" /></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <s:token/>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplierSet.create.information" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="15%">&nbsp;&nbsp;<s:text name="supplierSet.create.setId" /></td>
                <td width="5%">:</td>
                <td><s:property value="param.setId"/></td>
            </tr>
            
            <tr>
                <td>&nbsp;&nbsp;<s:text name="supplierSet.create.setDescription" /></td>
                <td>:</td>
                <td><s:property value="param.setDescription"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
    
    <div id="supplier" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplierSet.create.suppliers" />', width:275" >
        <table class="commtable">
            <s:iterator value="selectedSuppliers" id="item">
            <tr>
                <td width="350px">
                    <s:property value="#item.supplierName"/>
                </td>
            </tr>
            </s:iterator>
        </table>
    </div>
    </form>
</body>
</html>
