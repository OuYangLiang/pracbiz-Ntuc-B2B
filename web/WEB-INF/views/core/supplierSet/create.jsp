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
                "dojo/string",
                "dojo/_base/xhr",
                "dojo/parser",
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
                    registry,
                    on,
                    string,
                    xhr,
                    parser,
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
	                	var oldOptions = registry.byId(from).getSelected();
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            dom.byId(from).options.remove(option.index);
                            var item = new Option(option.text, option.value);
                            dom.byId(to).options.add(item);
                            item.selected = false;
                        }
                        sortSelect(to);
	                };

	                moveAll = function(from,to)
	                {
	                	dojo.query("."+from).forEach(function(node, index, nodeList){
	                        selectAll(node.options);
	                        moveSelection(from,to);
	                    });
	                };

	                var selectAll = function(options)
	                {
	                    for (var i = 0; i < options.length; i++)
	                    {
	                    	options[i].selected = true;
	                    }
	                };

	                var sortSelect = function(src)
                    {
                        var tmpAry = new Array();
                        var oldOptions = dom.byId(src).options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            tmpAry[i] = new Array();
                            tmpAry[i][0] = oldOptions[i].text;
                            tmpAry[i][1] = oldOptions[i].value;
                        }
                        
                        dom.byId(src).options.length = 0;
                        
                        tmpAry.sort(function(a, b){
                            if (a[0].toUpperCase() < b[0].toUpperCase())
                                return -1;
                            else if (a[0].toUpperCase() > b[0].toUpperCase())
                                return 1;
                                
                            return 0;
                        });
                        
                        for (var i=0;i<tmpAry.length;i++) {
                            var option = new Option(tmpAry[i][0], tmpAry[i][1]);
                            
                            dom.byId(src).options.add(option);
                        }
                    };

	                var oper1Store = new Array();
	                var oper2Store = new Array();
	                
	                filterPrivilege = function(src,id)
                    {
                    	var store;
                    	if (id=='sup1')
                    	{
                    		store = oper1Store;
                    	}
                    	else if (id=='sup2')
                    	{
                    		store = oper2Store;
                    	}
                        var inputValue;
                        dojo.query("."+src).forEach(function(node, index, nodeList){
                        	inputValue = string.trim(registry.byNode(node).get("value")).toUpperCase();
                        });
                        var oldOptions ;
                        dojo.query("."+id).forEach(function(node, index, nodeList){
                        	oldOptions = node;
                        });
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                            	store.push({text: option.text, value: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< store.length; i++)
                        {
                            if (store[i].text.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(store[i].text, store[i].value));
                                store.splice(i,1);
                                i--;
                            }
                        }
                        sortSelect('sup2');
                        
                    };
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            moveAll("sup2_hide","sup2");
                            var sup2 = dom.byId("sup2");
                            for (var i = 0 ; i < sup2.options.length; i ++)
                            {
                            	sup2.options[i].selected = true;
                            }
                            
                            //registry.byId("sup2").getSelected().forEach(function(option){option.selected=false;});
                            //registry.byId("sup2").invertSelection();
                            submitForm('mainForm', '<s:url value="/supplierSet/saveAdd.action" />');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/supplierSet/initAdd.action" />');
                        }
                    );
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/supplierSet/init.action?keepSp=Y" />');
                        }
                    });
                    
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                    		confirmDialog.show();
                        }
                    );

                    if (dom.byId("sup1"))
                    {
                        sortSelect("sup1");
                    }
                    
                    if (dom.byId("sup2"))
                    {
                        sortSelect("sup2");
                    }

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
        <div class="title"><s:text name="supplierSet.create.title" /></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token/>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplierSet.create.information" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplierSet.create.setId" /></td>
                <td>:</td>
                <td><s:textfield name="param.setId" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" /></td>
            </tr>
            
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="supplierSet.create.setDescription" /></td>
                <td>:</td>
                <td><s:textfield name="param.setDescription" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div class="space"></div>
    
    <div id="supplier" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplierSet.create.suppliers" />', width:275" >
        <table class="commtable">
            <tr>
                <td width="350px">
                    <table>
                        <tr><td>
                           <input id="sup1_filter" class="sup1_filter" data-dojo-type="dijit.form.ValidationTextBox" style="width:238px" />
                           <button type="button" onclick="filterPrivilege('sup1_filter','sup1');" data-dojo-type="dijit/form/Button">Search</button>
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="sup1" cssClass="sup1" list="availableSuppliers" listKey="supplierOid" listValue="supplierName + ' (' +supplierCode +')'" multiple="multiple"
                                theme="simple" style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                            <td style="display:none;">
                                <s:select id="sup1_hide" cssClass="sup1_hide" list="#{}" multiple="multiple" theme="simple"
                                    data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                        </tr>
                    </table>
                </td>
                
                <td width="100px">
                    <p><button  type="button" onclick="moveSelection('sup1','sup2');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                    <p><button  type="button" onclick="moveSelection('sup2','sup1');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                </td>
                
                <td width="350px">
                    <table>
                        <tr><td>
                            <input id="sup2_filter" class="sup2_filter" data-dojo-type="dijit.form.ValidationTextBox" style="width:238px" />
                            <button type="button" onclick="filterPrivilege('sup2_filter','sup2');" data-dojo-type="dijit/form/Button">Search</button>
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="sup2" cssClass="sup2" name="selectedSupplierOids" list="selectedSuppliers" listKey="supplierOid" listValue="supplierName + ' (' +supplierCode +')'"
                                multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit.form.MultiSelect" />
                            </td>
                            <td style="display:none;">
                                <s:select id="sup2_hide" cssClass="sup2_hide" list="#{}" multiple="multiple" theme="simple"
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
