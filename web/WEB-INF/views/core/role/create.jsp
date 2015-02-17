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
                "dijit/form/FilteringSelect",
                "dijit/form/ValidationTextBox",
                "custom/ConfirmDialog",
                "dojo/_base/array",
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
                    FilteringSelect,
                    ValidationTextBox,
                    ConfirmDialog,
                    array
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
	                    
	                var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
	                
	                var selectAll = function(src)
                    {
                        var oldOptions = dom.byId(src).options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            oldOptions[i].selected = true;
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

                    
                    var getOperation = function(userType, buyerOid)
                    {
                        xhr.get({
                                url: '<s:url value="/ajax/findOperations.action" />',
                                handleAs: "json",
                                content: {userTypeOid: userType, companyOid: buyerOid},
                                load: function(data)
                                {
                                    var s1 = dom.byId('s1');
                                    for (var i=0; i < data.length; i++)
                                    {
                                        var option = new Option(data[i].opnDesc, data[i].opnId);
                                        s1.options.add(option);
                                    }
                                }
                            });
                    }
                    
                    var getSuppliers = function(buyerOid)
                    {
                        xhr.get({
                                url: '<s:url value="/ajax/findSupplierByBuyer.action" />',
                                handleAs: "json",
                                content: {companyOid: buyerOid},
                                load: function(data)
                                {
                                    var s1 = dom.byId('sup1');
                                    for (var i=0; i < data.length; i++)
                                    {
                                        var option = new Option(data[i].supplierName, data[i].supplierOid);
                                        s1.options.add(option);
                                    }
                                    
                                    sortSelect('sup1');
                                }
                            });
                        
                    };
                    
                    //var showSupplierList = function()
                    //{
                    //    if (dom.byId('supplierList'))
                    //        domStyle.set('supplierList', 'display', '');
                    //}
                    
                    //var hideSupplierList = function()
                    //{
                    //    if (dom.byId('supplierList'))
                    //        domStyle.set('supplierList', 'display', 'none');
                    //}
                    
                    var showBuyer = function()
                    {
                        if (dom.byId('buyer'))
                            domStyle.set('buyer', 'display', '');
                    }
                    
                    var hideBuyer = function()
                    {
                        if (dom.byId('buyer'))
                            domStyle.set('buyer', 'display', 'none');
                    }
                    
                    var showSupplier = function()
                    {
                        if (dom.byId('supplier'))
                            domStyle.set('supplier', 'display', '');
                    }
                    
                    var hideSupplier = function()
                    {
                        if (dom.byId('supplier'))
                            domStyle.set('supplier', 'display', 'none');
                    }
                    
                    var buyerChanged = function(buyerOid){
                        // clear items from select.
                        dom.byId("s1").length=0;
                        dom.byId("s2").length=0;
                        dom.byId("sup1").length=0;
                        dom.byId("sup2").length=0;
                        
                        if ("-1" == buyerOid)
                        {
                            return;
                        }
                        
                        getSuppliers(buyerOid);
                        getOperation(registry.byId('userTypeSelect').value, buyerOid);
                    };
                    
                    
                    var userTypeChange = function(value)
                    {
                        // clear items from select.
                        dom.byId("s1").length=0;
                        dom.byId("s2").length=0;
                    
                        if ("" == value)
                        {
                            hideBuyer();
                            hideSupplier();
                            //hideSupplierList();
                        }
                        else if (1 == value)
                        {
                            hideBuyer();
                            hideSupplier();
                            //hideSupplierList();
                            getOperation(1, 0);
                        }
                        else if (2 == value)
                        {
                            if (curUserType == 2)
                            {
                                hideBuyer();
                                hideSupplier();
                                
                                getOperation(value, 0);
                            }
                            else if (curUserType == 1)
                            {
                                showBuyer();
	                            hideSupplier();
	                            //hideSupplierList();
	                            
	                            if (registry.byId('buyerSelect').value != '-1')
	                            {
	                                getOperation(value, registry.byId('buyerSelect').value);
	                            }
                            }
                        }
                        else if (3 == value)
                        {
                            if (curUserType == 2)
                            {
                                hideBuyer();
                                showSupplier();
                                //hideSupplierList();
                                
                                buyerChanged('<s:property value="#session.SESSION_CURRENT_USER_PROFILE.buyerOid" />');
                            }
                            else if (curUserType == 1)
                            {
                                showBuyer();
                                showSupplier();
                                //hideSupplierList();
                                
                                if (registry.byId('buyerSelect').value != '-1')
                                {
                                    getOperation(value, registry.byId('buyerSelect').value);
                                }
                            }
                            else if (curUserType == 3)
                            {
                                hideBuyer();
                                hideSupplier();
                                
                                getOperation(value, 0);
                            }
                        }
                        else if (4 == value)
                        {
                            if (curUserType == 1)
                            {
                                showBuyer();
                                hideSupplier();
                                //hideSupplierList();
                                
                                if (registry.byId('buyerSelect').value != '-1')
                                {
                                    getOperation(value, registry.byId('buyerSelect').value);
                                }
                            }
                            else if (curUserType == 2)
                            {
                                hideBuyer();
                                hideSupplier();
                                //hideSupplierList();
                            
                                getOperation(value, '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.buyerOid" />');
                            }
                        }
                        else if (5 == value)
                        {
                            if (curUserType == 1)
                            {
                                showBuyer();
                                showSupplier();
                                //showSupplierList();
                            
                                if (registry.byId('buyerSelect').value != '-1')
                                {
                                    getOperation(value, registry.byId('buyerSelect').value);
                                }
                            }
                            else if (curUserType == 2)
                            {
                                hideBuyer();
                                showSupplier();
                                //hideSupplierList();
                                
                                buyerChanged('<s:property value="#session.SESSION_CURRENT_USER_PROFILE.buyerOid" />');
                            }
                            else if (curUserType == 3)
                            {
                                hideBuyer();
                                hideSupplier();
                                //hideSupplierList();
                                
                                getOperation(value, '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.supplierOid" />');
                            }
                        }
                        else if (6 == value)
                        {
                            if (curUserType == 1)
                            {
                                showBuyer();
	                            hideSupplier();
	                            //hideSupplierList();
	                            
	                            if (registry.byId('buyerSelect').value != '-1')
	                            {
	                                getOperation(value, registry.byId('buyerSelect').value);
	                            }
                            }
                            else if (curUserType == 2)
                            {
                                hideBuyer();
                                hideSupplier();
                                //hideSupplierList();
                            
                                getOperation(value, '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.buyerOid" />');
                            }
                        }
                        else if (7 == value)
                        {
                            if (curUserType == 1)
                            {
                                showBuyer();
                                hideSupplier();
                                //hideSupplierList();
                                
                                if (registry.byId('buyerSelect').value != '-1')
	                            {
	                                getOperation(value, registry.byId('buyerSelect').value);
	                            }
                            }
                            else if (curUserType == 2 || curUserType == 6)
                            {
                                hideBuyer();
                                hideSupplier();
                                //hideSupplierList();
                            
                                getOperation(value, '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.buyerOid" />');
                            }
                        }
                    }
                    
                    on(registry.byId("userTypeSelect"), 'change', 
                        function(value)
                        {
                            userTypeChange(value);
                        }
                    );
                    
                    if (dom.byId("buyerSelect")){
	                    on(registry.byId("buyerSelect"), 'change', 
	                        function(value)
	                        {
	                            buyerChanged(value);
	                        }
	                    );
	                }
	                
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
                        var oldOptions = dom.byId(from).options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                        
                            var item = new Option(option.text, option.value);
                        
                            dom.byId(to).options.add(item);
                        }
                        
                        dom.byId(from).options.length=0;
                        sortSelect(to);
	                };
	                
	                var sup1Store = new Array();
                    var sup2Store = new Array();
                    var s1Store = new Array();
                    var s2Store = new Array();
	                
                    selectAllSup = function()
                    {
                        var userType = registry.byId("userTypeSelect").value;
                        
                        if (userType != 3 && userType != 5)
                           return;
                    
                        dom.byId("sup1_filter").value = "";
                        dom.byId("sup2_filter").value = "";
                        
                        dom.byId("sup1").options.length = 0;
                        dom.byId("sup2").options.length = 0;
                        
                        sup1Store = new Array();
                        sup2Store = new Array();
                        
                        var s1 = dom.byId('sup2');
                        dom.byId("sup2").options.add(new Option("<s:text name='role.all.supplier.selected' />", -1));
                    };
                    
                    deSelectAllSup = function()
                    {
                        var userType = registry.byId("userTypeSelect").value;
                        
                        if (userType != 3 && userType != 5)
                           return;
                    
                        dom.byId("sup1_filter").value = "";
                        dom.byId("sup2_filter").value = "";
                        
                        dom.byId("sup1").options.length = 0;
                        dom.byId("sup2").options.length = 0;
                        
                        sup1Store = new Array();
                        sup2Store = new Array();
                        
                        if (curUserType == 1)
                        {
                            if (registry.byId('buyerSelect').value != '-1')
                            {
                                getSuppliers(registry.byId('buyerSelect').value);
                            }
                        }
                        else if (curUserType == 2)
                        {
                            getSuppliers('<s:property value="#session.SESSION_CURRENT_USER_PROFILE.buyerOid" />');
                        }
                    };
                    
                    
                    filterSup1 = function()
                    {
                        var inputValue = dom.byId("sup1_filter").value.toUpperCase();
                        oldOptions = dom.byId("sup1").options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                                sup1Store.push({supplierName: option.text, supplierOid: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< sup1Store.length; i++)
                        {
                            if (sup1Store[i].supplierName.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(sup1Store[i].supplierName, sup1Store[i].supplierOid));
                                sup1Store.splice(i,1);
                                i--;
                            }
                        }
                        
                        sortSelect("sup1");
                    };
                    
                    filterSup2 = function()
                    {
                        var inputValue = dom.byId("sup2_filter").value.toUpperCase();
                        oldOptions = dom.byId("sup2").options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                                sup2Store.push({supplierName: option.text, supplierOid: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< sup2Store.length; i++)
                        {
                            if (sup2Store[i].supplierName.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(sup2Store[i].supplierName, sup2Store[i].supplierOid));
                                sup2Store.splice(i,1);
                                i--;
                            }
                        }
                        
                        sortSelect("sup2");
                    };
                    
                    filterP1 = function()
                    {
                        var inputValue = dom.byId("s1_filter").value.toUpperCase();
                        oldOptions = dom.byId("s1").options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                                s1Store.push({supplierName: option.text, supplierOid: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< s1Store.length; i++)
                        {
                            if (s1Store[i].supplierName.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(s1Store[i].supplierName, s1Store[i].supplierOid));
                                s1Store.splice(i,1);
                                i--;
                            }
                        }
                        
                        sortSelect("s1");
                    };
                    
                    filterP2 = function()
                    {
                        var inputValue = dom.byId("s2_filter").value.toUpperCase();
                        oldOptions = dom.byId("s2").options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                                s2Store.push({supplierName: option.text, supplierOid: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< s2Store.length; i++)
                        {
                            if (s2Store[i].supplierName.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(s2Store[i].supplierName, s2Store[i].supplierOid));
                                s2Store.splice(i,1);
                                i--;
                            }
                        }
                        
                        sortSelect("s2");
                    };
                    
	                
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            if (registry.byId('userTypeSelect').value == 3)
                            {
                                for (var i = 0; i< sup2Store.length; i++)
		                        {
		                            oldOptions.add(new Option(sup2Store[i].supplierName, sup2Store[i].supplierOid));
		                        }
                                
                                selectAll("sup2");
                            }
                            
                            if (registry.byId('userTypeSelect').value == 5 && (curUserType == 1 || curUserType == 2))
                            {
                                for (var i = 0; i< sup2Store.length; i++)
                                {
                                    oldOptions.add(new Option(sup2Store[i].supplierName, sup2Store[i].supplierOid));
                                }
                                
                                selectAll("sup2");
                            }
                        
                    	    for (var i = 0; i< s2Store.length; i++)
                            {
                                oldOptions.add(new Option(s2Store[i].supplierName, s2Store[i].supplierOid));
                            }
                            selectAll("s2");
                        
                            submitForm('mainForm', '<s:url value="/role/saveAdd.action" />');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/role/initAdd.action" />');
                        }
                    );
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                        	changeToURL('<s:url value="/role/init.action?keepSp=Y" />');
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
            <button data-dojo-type="dijit/form/Button" id="saveBtn" ><s:text name="button.save" /></button>
            <button data-dojo-type="dijit/form/Button" id="resetBtn" ><s:text name="button.reset" /></button>
            <button data-dojo-type="dijit/form/Button" id="cancelBtn" ><s:text name="button.cancel" /></button>
        </td></tr></tbody></table>
    </div>
    
    
    <div class="pageBar">
        <div class="title"><s:text name="role.create.title" /></div>
    </div>
    
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token/>
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="role.create.profile" />', width:275" style="width:99%">
        <table class="commtable">
        <tbody>
            <tr>
                <td width="2px"><span class="required">*</span> </td>
                <td width="30%">&nbsp;&nbsp;<s:text name="role.create.profile.userType" /></td>
                <td>:</td>
                <td>
                    <s:select id="userTypeSelect" data-dojo-type="dijit/form/FilteringSelect" name="param.userTypeOid" list="userTypes" 
                        listKey="userTypeOid" listValue="userTypeId" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/>
                </td>
            </tr>
            
            <tr id="buyer" <s:if test="(param.userTypeOid==2 && #session.SESSION_CURRENT_USER_PROFILE.userType == 1) 
							            || (param.userTypeOid==3 && #session.SESSION_CURRENT_USER_PROFILE.userType == 1) 
							            || (param.userTypeOid==4 && #session.SESSION_CURRENT_USER_PROFILE.userType == 1) 
							            || (param.userTypeOid==5 && #session.SESSION_CURRENT_USER_PROFILE.userType == 1) 
							            || (param.userTypeOid==6 && #session.SESSION_CURRENT_USER_PROFILE.userType == 1) 
							            || (param.userTypeOid==7 && #session.SESSION_CURRENT_USER_PROFILE.userType == 1)" >
                        style=""</s:if><s:else>style="display:none"</s:else>>
                <td><span class="required">*</span> </td>
                <td>&nbsp;&nbsp;<s:text name="role.create.profile.buyer" /></td>
                <td>:</td>
                <td>
                    <s:select id="buyerSelect" data-dojo-type="dijit/form/FilteringSelect" name="param.buyerOid" list="buyers" 
                        listKey="buyerOid" listValue="buyerName" headerKey="-1" headerValue="%{getText('select.select')}" theme="simple"/>
                </td>
            </tr>
            
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="role.create.profile.roleId" /></td>
                <td>:</td>
                <td><s:textfield name="param.roleId" data-dojo-props="required: true" data-dojo-type="dijit/form/ValidationTextBox" theme="simple" /></td>
            </tr>
            
            <tr>
                <td><span class="required">*</span></td>
                <td>&nbsp;&nbsp;<s:text name="role.create.profile.roleDesc" /></td>
                <td>:</td>
                <td><s:textfield name="param.roleName" data-dojo-props="required: true" data-dojo-type="dijit/form/ValidationTextBox" theme="simple" /></td>
            </tr>
        </tbody>
        </table>
    </div>
    
    <div id="supplier" data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="role.create.profile.supplier" />', width:275" 
        <s:if test="(param.userTypeOid==3 && #session.SESSION_CURRENT_USER_PROFILE.userType !=3 ) || (param.userTypeOid==5 && #session.SESSION_CURRENT_USER_PROFILE.userType == 1) || (param.userTypeOid==5 && #session.SESSION_CURRENT_USER_PROFILE.userType == 2) " >style=""</s:if><s:else>style="display:none"</s:else>>
        <table class="commtable">
            <tr>
                <td width="350px">
                    <table>
                        <tr><td>
                           <input id="sup1_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" />
                           <button onclick="filterSup1();" data-dojo-type="dijit/form/Button">Search</button>
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="sup1" list="availableSuppliers" listKey="supplierOid" listValue="supplierName" multiple="multiple"
                                theme="simple" style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit/form/MultiSelect" />
                            </td>
                        </tr>
                    </table>
                </td>
                
                <td width="100px">
                    <p><button onclick="selectAllSup();" data-dojo-type="dijit/form/Button"><s:text name="button.addAll" /></button></p>
                    <p><button onclick="moveSelection('sup1','sup2');" data-dojo-type="dijit/form/Button">&gt;</button></p>
                    <p><button onclick="moveSelection('sup2','sup1');" data-dojo-type="dijit/form/Button">&lt;</button></p>
                    <p><button onclick="deSelectAllSup();" data-dojo-type="dijit/form/Button"><s:text name="button.deleteAll" /></button></p>
                </td>
                
                <td width="350px">
                    <table>
                        <tr><td>
                            <input id="sup2_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" />
                            <button onclick="filterSup2();" data-dojo-type="dijit/form/Button">Search</button>
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="sup2" name="selectedSuppliers" list="selectedSuppliers" listKey="supplierOid" listValue="supplierName"
                                multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit/form/MultiSelect" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    
    <div class="space"></div>
            
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="role.create.privilege" />', width:275" style="width:99%">
        <table class="commtable">
            <tr>
                <td width="350px">
                    <table>
                        <tr><td>
	                       <input id="s1_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterP1();" data-dojo-type="dijit/form/Button">Search</button>
                        </td></tr>
                        
                        <tr><td>
                            <s:select id="s1" cssClass="s1" list="operations" listKey="opnId" listValue="opnDesc" multiple="multiple"
                                theme="simple" style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit/form/MultiSelect" />
                            </td>
                        </tr>
                    </table>
                </td>
                
                <td width="100px">
                    <p><button onclick="moveAll('s1','s2');" data-dojo-type="dijit/form/Button"><s:text name="button.addAll" /></button></p>
                    <p><button onclick="moveSelection('s1','s2');" data-dojo-type="dijit/form/Button">&gt;</button></p>
                    <p><button onclick="moveSelection('s2','s1');" data-dojo-type="dijit/form/Button">&lt;</button></p>
                    <p><button onclick="moveAll('s2','s1');" data-dojo-type="dijit/form/Button"><s:text name="button.deleteAll" /></button></p>
                </td>
                
                <td width="350px">
                    <table>
                        <tr><td>
	                       <input id="s2_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px" >
	                       <button onclick="filterP2();" data-dojo-type="dijit/form/Button">Search</button>
	                    </td></tr>
                        
                        <tr><td>
                            <s:select id="s2" cssClass="s2" name="selectedOperations" list="selectedOperations" listKey="opnId" listValue="opnDesc" multiple="multiple" theme="simple"
                                style="width:300px; height:300px; font-family:Arial, Helvetica, sans-serif;" 
                                data-dojo-type="dijit/form/MultiSelect" />
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
