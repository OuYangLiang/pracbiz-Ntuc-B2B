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
                    
	                moveSelection = function(from, to)
	                {
	                    var selectedOnes = registry.byId(from).getSelected();
	                    registry.byId(to).addSelected(registry.byId(from));
	                    registry.byId(to).getSelected().forEach(function(option){option.selected=false;});
	                    
	                    dojo.query(selectedOnes).animateProperty({
	                                    properties:{
	                                        backgroundColor: {start: "#ff6", end: "#fff" }
	                                    },
	                                    duration :1500
	                                }).play();
	                };

	                moveAll = function(from,to)
	                {
                        registry.byId(from).getSelected().forEach(function(option){option.selected=false;});
                        registry.byId(from).invertSelection();
                        moveSelection(from,to);
	                
	                };
	                
	                filterPrivilege = function(src,id)
                    {
                        var inputValue = dom.byId(src).value.toUpperCase();
                        
                        moveAll(id+"_hide", id);
                        registry.byId(id).getSelected().forEach(function(option){option.selected=false;});
                        registry.byId(id).invertSelection();
                        
                        array.forEach(registry.byId(id).getSelected(), function(option){
                            if (option.text.toUpperCase().indexOf(inputValue) >= 0 )
                            {
                                option.selected = false;
                            }
                        });
                        
                        moveSelection(id, id+"_hide");
                    };
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            moveAll("sup2_hide","sup2");
                            registry.byId("sup2").getSelected().forEach(function(option){option.selected=false;});
                            registry.byId("sup2").invertSelection();
                            submitForm('mainForm', '<s:url value="/supplier/saveEditSupplierShared.action" />?supplier.supplierOid=<s:property value="supplier.supplierOid"/>');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/supplier/initEditSupplierShared.action" />?supplier.supplierOid=<s:property value="supplier.supplierOid"/>');
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

                });
                
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
        <div class="title"><s:text name="supplier.edit.supplierShared" /></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token/>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.edit.supplierShared.profile" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="10%">&nbsp;&nbsp;<s:text name="supplier.edit.supplierShared.profile.supplierName" /></td>
                <td>:</td>
                <td><s:property value="supplier.supplierName"/></td>
            </tr>
            
            <tr>
                <td>&nbsp;&nbsp;<s:text name="supplier.edit.supplierShared.profile.supplierCode" /></td>
                <td>:</td>
                <td><s:property value="supplier.supplierCode"/></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
    
    <div id="supplier" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplier.edit.supplierShared.suppliers" />', width:275" >
        <table class="commtable">
            <tr>
                <td width="350px">
                    <table>
                        <tr><td>
                           <input id="sup1_filter" data-dojo-type="dijit.form.ValidationTextBox" style="width:298px" 
                               onkeyup="filterPrivilege('sup1_filter','sup1');" >
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="sup1" list="availableSuppliers" listKey="supplierOid" listValue="supplierName" multiple="multiple"
                                theme="simple" style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                            <td style="display:none;">
                                <s:select id="sup1_hide" list="#{}" multiple="multiple" theme="simple"
                                    data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                        </tr>
                    </table>
                </td>
                
                <td width="100px">
                    <p><button onclick="moveSelection('sup1','sup2');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                    <p><button onclick="moveSelection('sup2','sup1');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                </td>
                
                <td width="350px">
                    <table>
                        <tr><td>
                            <input id="sup2_filter" data-dojo-type="dijit.form.ValidationTextBox" style="width:298px" 
                                onkeyup="filterPrivilege('sup2_filter','sup2');" >
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="sup2" name="selectedSupplierOids" list="selectedSuppliers" listKey="supplierOid" listValue="supplierName"
                                multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                            <td style="display:none;">
                                <s:select id="sup2_hide" list="#{}" multiple="multiple" theme="simple"
                                    data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    </form>
</body>
</html>
