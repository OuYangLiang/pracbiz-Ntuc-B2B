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
                "dijit/form/CheckBox",
                "dijit/form/FilteringSelect",
                "dijit/form/RadioButton",
                "dijit/form/ValidationTextBox",
                "dijit/form/MultiSelect",
                "custom/ConfirmDialog",
                "dojo/NodeList-fx",
                "dojo/json",
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
                    CheckBox,
                    FilteringSelect,
                    RadioButton,
                    ValidationTextBox,
                    MultiSelect,
                    ConfirmDialog,
                    fx,
                    JSON
                    )
                {
                    parser.parse();
                    
                    (new B2BPortalBase()).init(
                        '<c:out value="${session.helpExHolder.helpNo}"/>',
                        '<c:out value="${session.helpExHolder.helpEmail}"/>',
                        '<s:property value="#session.commonParam.timeout" />',
                        '<s:url value="/logout.action" />');
                    
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
                    
                    initData();
                    function initData()
                    {
                        var uts = registry.byId('userTypeSelect');
                        var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
                        hideProcess(uts.value, curUserType);

                        sortSelect(dom.byId('buyerAvailabel'));
                        sortSelect(dom.byId('buyerAssigned'));
                        sortSelect(dom.byId('roleAvailabel'));
                        sortSelect(dom.byId('roleAssigned'));
                        sortSelect(dom.byId('areaAvailabel'));
                        sortSelect(dom.byId('areaAssigned'));
                        sortSelect(dom.byId('storeAvailabel'));
                        sortSelect(dom.byId('storeAssigned'));
                        sortSelect(dom.byId('wareHouseAvailabel'));
                        sortSelect(dom.byId('wareHouseAssigned'));
                        sortSelect(dom.byId('classAvailable'));
                        sortSelect(dom.byId('classAssigned'));
                        sortSelect(dom.byId('subclassAvailable'));
                        sortSelect(dom.byId('subclassAssigned'));
                    }
                    
                    function hideProcess(value, curUserType)
                    {
                        if (1 == value)
                        {
                            // System Admin
                            domStyle.set('buyer', 'display', 'none');
                            domStyle.set('supplier', 'display', 'none');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('supplierList', 'display', 'none');
                            domStyle.set('groupList', 'display', 'none');
                            domStyle.set('storeList', 'display', 'none');
                            domStyle.set('classArea', 'display', 'none');
                            domStyle.set('buyerAssignList', 'display', 'none');
                        }
                        else if (2 == value)
                        {
                            // Buyer Admin
                            domStyle.set('buyer', 'display', '');
                            domStyle.set('supplier', 'display', 'none');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('supplierList', 'display', 'none');
                            domStyle.set('groupList', 'display', '');
                            domStyle.set('storeList', 'display', '');
                            domStyle.set('classArea', 'display', '');
                            domStyle.set('buyerAssignList', 'display', '');
                            if (curUserType != 2)
                            {
                                domStyle.set('buyer', 'display', 'none');
                                domStyle.set('buyerList', 'display', '');
                            }
                        }
                        else if (3 == value)
                        {
                            // Supplier Admin
                            domStyle.set('buyer', 'display', 'none');
                            domStyle.set('supplier', 'display', '');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('supplierList', 'display', 'none');
                            domStyle.set('groupList', 'display', '');
                            domStyle.set('storeList', 'display', 'none');
                            domStyle.set('classArea', 'display', 'none');
                            domStyle.set('buyerAssignList', 'display', 'none');
                            if (curUserType != 3)
                            {
                                domStyle.set('supplier', 'display', 'none');
                                domStyle.set('supplierList', 'display', '');
                            }
                        }
                        else if (4 == value)
                        {
                            // Buyer User
                            domStyle.set('buyer', 'display', '');
                            domStyle.set('supplier', 'display', 'none');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('supplierList', 'display', 'none');
                            domStyle.set('groupList', 'display', '');
                            domStyle.set('storeList', 'display', '');
                            domStyle.set('classArea', 'display', '');
                            domStyle.set('buyerAssignList', 'display', '');
                            if (curUserType != 2 && curUserType != 4)
                            {
                                domStyle.set('buyer', 'display', 'none');
                                domStyle.set('buyerList', 'display', '');
                            }
                        }
                        else if (5 == value)
                        {
                            // Supplier User
                            domStyle.set('buyer', 'display', 'none');
                            domStyle.set('supplier', 'display', '');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('supplierList', 'display', 'none');
                            domStyle.set('groupList', 'display', '');
                            domStyle.set('storeList', 'display', 'none');
                            domStyle.set('classArea', 'display', 'none');
                            domStyle.set('buyerAssignList', 'display', 'none');
                            if (curUserType != 3 && curUserType != 5)
                            {
                                domStyle.set('supplier', 'display', 'none');
                                domStyle.set('supplierList', 'display', '');
                            }
                        }
                        else if (6 == value)
                        {
                            // Buyer Store Admin
                            domStyle.set('buyer', 'display', '');
                            domStyle.set('supplier', 'display', 'none');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('supplierList', 'display', 'none');
                            domStyle.set('groupList', 'display', 'none');
                            domStyle.set('storeList', 'display', '');
                            domStyle.set('classArea', 'display', 'none');
                            domStyle.set('buyerAssignList', 'display', '');
                            if (curUserType != 2 && curUserType != 6)
                            {
                                domStyle.set('buyer', 'display', 'none');
                                domStyle.set('buyerList', 'display', '');
                            }
                        }
                        else if (7 == value)
                        {
                            // Buyer Store User
                            domStyle.set('buyer', 'display', '');
                            domStyle.set('supplier', 'display', 'none');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('supplierList', 'display', 'none');
                            domStyle.set('groupList', 'display', 'none');
                            domStyle.set('storeList', 'display', '');
                            domStyle.set('classArea', 'display', 'none');
                            domStyle.set('buyerAssignList', 'display', '');
                            if (curUserType != 2 && curUserType != 6 && curUserType != 7)
                            {
                                domStyle.set('buyer', 'display', 'none');
                                domStyle.set('buyerList', 'display', '');
                            }
                        }
                        
                        if (dom.byId("addToGroup"))
                        {
                            var value = registry.byId("addToGroup").checked;
                            if (value == true)
                            {
                                domStyle.set('group',"display","");
                            }
                            else
                            {
                                domStyle.set('group',"display","none");
                            }
                        }

                        var active = registry.byId("active").checked;
                        if (active == true)
                        {
                            domStyle.set('blocked',"display","");
                        }
                        else
                        {
                            domStyle.set('blocked',"display","none");
                        }
                    };
                    
                    
                    
                    
                    findRole = function (value, companyOid)
                    {
                        xhr.get({
                            url: '<s:url value="/ajax/findRolesByUser.action" />',
                            handleAs: "json",
                            content: {userTypeOid: value, companyOid: companyOid},
                            load: function(data)
                            {
                                var avail = dom.byId('roleAvailabel');
                                for (var i=0; i < data.length; i++)
                                {
                                    var option = new Option(data[i].roleName + '(' + data[i].roleId + ')', data[i].roleOid);
                                    avail.options.add(option);
                                }
                                sortSelect(avail);
                            }
                        });
                    }
                    
                    findArea = function (companyOid)
                    {
                        xhr.get({
                            url: '<s:url value="/ajax/findAreasByUser.action" />',
                            handleAs: "json",
                            content: {companyOid: companyOid},
                            load: function(data)
                            {
                                var avail = dom.byId('areaAvailabel');
                                for (var i=0; i < data.length; i++)
                                {
                                    if(data[i].areaOid == -2 || data[i].areaOid == -1)
                                    {
                                        continue;
                                    }
                                    var option = new Option(data[i].areaCode, data[i].areaOid);
                                    avail.options.add(option);
                                }
                                sortSelect(avail);
                            }
                        });
                    }
                    
                    findStore = function (companyOid)
                    {
                        xhr.get({
                            url: '<s:url value="/ajax/findStoresByUser.action" />',
                            handleAs: "json",
                            content: {companyOid: companyOid},
                            load: function(data)
                            {
                                var avail = dom.byId('storeAvailabel');
                                for (var i=0; i < data.length; i++)
                                {
                                    if(data[i].storeOid != -4)
                                    {
                                         var option = new Option(data[i].storeName  + data[i].storeCode , data[i].storeOid);
                                         avail.options.add(option);
                                    }
                                }
                                sortSelect(avail);
                            }
                        });
                    }
                    
                    findWarehouse = function (companyOid)
                    {
                        xhr.get({
                            url: '<s:url value="/ajax/findWareHouseByUser.action" />',
                            handleAs: "json",
                            content: {companyOid: companyOid},
                            load: function(data)
                            {
                                var avail = dom.byId('wareHouseAvailabel');
                                for (var i=0; i < data.length; i++)
                                {
                                    if(data[i].storeOid != -4)
                                    {
                                         var option = new Option(data[i].storeName + data[i].storeCode, data[i].storeOid);
                                         avail.options.add(option);
                                    }
                                }
                                sortSelect(avail);
                            }
                        });
                    }
                    
                    findClass = function ()
                    {
                    	var companyOid = registry.byId('buyerListSelect').value;
                        selectAll("buyerAssigned");
                        var assignedBuyers = registry.byId('buyerAssigned').get('value');
                        
                        xhr.get({
                            url: '<s:url value="/ajax/findClassByUser.action" />',
                            handleAs: "json",
                            content: {companyOids: companyOid + "," + assignedBuyers},
                            load: function(data)
                            {
                                var avail = dom.byId('classAvailable');
                                for (var i=0; i < data.length; i++)
                                {
                                    if(data[i].classOid != -1)
                                    {
                                         var option = new Option(data[i].classCode + '[' + data[i].buyerCode + ']', data[i].classOid);
                                         avail.options.add(option);
                                    }
                                }
                                sortSelect(avail);
                            }
                        });
                    }
                    
                    findSubclass = function ()
                    {
                    	var companyOid = registry.byId('buyerListSelect').value;
                        selectAll("buyerAssigned");
                        var assignedBuyers = registry.byId('buyerAssigned').get('value');
                        
                        xhr.get({
                            url: '<s:url value="/ajax/findSubclassByUser.action" />',
                            handleAs: "json",
                            content: {companyOids: companyOid + "," + assignedBuyers},
                            load: function(data)
                            {
                                var avail = dom.byId('subclassAvailable');
                                for (var i=0; i < data.length; i++)
                                {
                                    if(data[i].subclassOid != -1)
                                    {
                                         var option = new Option(data[i].subclassCode + '(' + data[i].classCode + ')[' + data[i].buyerCode + ']', data[i].subclassOid);
                                         avail.options.add(option);
                                    }
                                }
                                sortSelect(avail);
                            }
                        });
                    }
                    
                    findAvailableBuyer = function (companyOid)
                    {
                        xhr.get({
                            url: '<s:url value="/ajax/findAvailableBuyersByBuyerOid.action" />',
                            handleAs: "json",
                            content: {companyOid: companyOid},
                            load: function(data)
                            {
                                var avail = dom.byId('buyerAvailabel');
                                for (var i=0; i < data.length; i++)
                                {
                                     var option = new Option(data[i].buyerName, data[i].buyerOid);
                                     avail.options.add(option);
                                }
                                sortSelect(avail);
                            }
                        });
                    }
                    
                    findGroup = function (userTypeOid, companyOid)
                    {
                        xhr.get({
                            url: '<s:url value="/ajax/findGroupsByUser.action" />',
                            handleAs: "json",
                            content: {userTypeOid: userTypeOid, companyOid: companyOid},
                            load: function(data)
                            {
                                var group = registry.byId('group');
                                if (data == null || data == "")
                                {
                                    group.addOption( { label: "", value: ""});
                                }
                                
                                for (var i=0; i < data.length; i++)
                                {
                                    group.addOption( { label:data[i].groupName, value: data[i].groupOid } );
                                }
                            }
                        });
                    }
                    
                    
                    moveSelection = function(from, to)
                    {
                        
                        registry.byId(to).addSelected(registry.byId(from));
                        sortSelect(to);
                    };

                    moveAll = function(from,to)
                    {
                        var allFlag = false;
                        if(from == 'areaAvailabel')
                        {
                            allFlag = <s:property value="#session.all_area_flag"/>;
                            if (allFlag)
                            {
                                dom.byId(to).options.length=0;
                                dom.byId(to).options.add(new Option("<s:text name='user.all.area.selected' />", -2));
                            }
                        }
                        else if(from == 'storeAvailabel')
                        {
                            allFlag = <s:property value="#session.all_store_flag"/>;
                            if (allFlag)
                            {
                                dom.byId(to).options.length=0;
                                dom.byId(to).options.add(new Option("<s:text name='user.all.store.selected' />", -3));
                            }
                        }
                        else if(from == 'wareHouseAvailabel')
                        {
                            allFlag = <s:property value="#session.all_warehouse_flag"/>;
                            if (allFlag)
                            {
                                dom.byId(to).options.length=0;
                                dom.byId(to).options.add(new Option("<s:text name='user.all.warehouse.selected' />", -4));
                            }
                        }
                        else if(from == 'classAvailable')
                        {
                            allFlag = <s:property value="#session.all_class_flag"/>;
                            if (allFlag)
                            {
                                dom.byId(to).options.length=0;
                                dom.byId(to).options.add(new Option("<s:text name='user.all.class.selected' />", -1));
                            } 
                        }
                        else if(from == 'subclassAvailable')
                        {
                            allFlag = <s:property value="#session.all_subclass_flag"/>;
                            if (allFlag)
                            {
                                dom.byId(to).options.length=0;
                                dom.byId(to).options.add(new Option("<s:text name='user.all.subclass.selected' />", -1));
                            }
                        }
                        
                        if (!allFlag)
                        {
                            var oldOptions = dom.byId(from).options;
                            
                            for (var i = 0; i < oldOptions.length; i++)
                            {
                                var option = oldOptions[i];
                            
                                var item = new Option(option.text, option.value);
                            
                                dom.byId(to).options.add(item);
                            }
                        }
                        
                        dom.byId(from).options.length=0;
                        sortSelect(to);
                    };
                    
                    removeAll = function (from, to)
                    {
                        
                        var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
                        if (curUserType ==1)
                        {
                            companyOid = registry.byId('buyerListSelect').value;
                        }
                        else
                        {
                            companyOid = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.buyerOid" />';
                        }
                        dom.byId(to).options.length=0;
                        if(from == 'areaAssigned')
                        {
                            findArea(companyOid);
                        }
                        else if(from == 'storeAssigned')
                        {
                            findStore(companyOid);
                        }
                        else if(from == 'wareHouseAssigned')
                        {
                            findWarehouse(companyOid);
                        }
                        else if(from == 'classAssigned')
                        {
                            findClass();
                        }
                        else if(from == 'subclassAssigned')
                        {
                            findSubclass();
                        }
                        
                        dom.byId(from).options.length=0;
                        
                    }

                    var searchList = new Array();
                    filterFunction = function(searchId, selectId)
                    {
                        var inputValue = dom.byId(searchId).value.toUpperCase();
                        oldOptions = dom.byId(selectId).options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                            	searchList.push({storeName: option.text, storeOid: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< searchList.length; i++)
                        {
                            if (searchList[i].storeName.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(searchList[i].storeName, searchList[i].storeOid));
                                searchList.splice(i,1);
                                i--;
                            }
                        }
                        
                        sortSelect(selectId);
                    };


                    var searchRightList = new Array();
                    filterRightFunction = function(searchId, selectId)
                    {
                        var inputValue = dom.byId(searchId).value.toUpperCase();
                        oldOptions = dom.byId(selectId).options;
                        
                        for (var i = 0; i < oldOptions.length; i++)
                        {
                            var option = oldOptions[i];
                            
                            if (option.text.toUpperCase().indexOf(inputValue) < 0)
                            {
                            	searchRightList.push({storeName: option.text, storeOid: option.value});
                                oldOptions.remove(i);
                                i--;
                            }
                        }
                        
                        for (var i = 0; i< searchRightList.length; i++)
                        {
                            if (searchRightList[i].storeName.toUpperCase().indexOf(inputValue) >=0)
                            {
                                oldOptions.add(new Option(searchRightList[i].storeName, searchRightList[i].storeOid));
                                searchRightList.splice(i,1);
                                i--;
                            }
                        }
                        
                        sortSelect(selectId);
                    };
                    
                    
                    setLoginIdSuffix = function()
                    {
                    	var userType = registry.byId("userTypeSelect").value;
                    	if (userType==3 || userType==5)
                    	{
                    		var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
                            if (curUserType ==3 || curUserType ==5)
                            {
                                companyOid = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.supplierOid" />';
                            }
                            else
                            {
                                companyOid = registry.byId('supplierListSelect').value;
                            }
                    		xhr.get({
                                url: '<s:url value="/ajax/findSupplierByKey.action" />',
                                handleAs: "json",
                                content: {companyOid: companyOid},
                                load: function(data)
                                {
                                	if (data == null)
                                	{
                                		return;
                                	}
                                	else
                                	{
	                                    dom.byId("loginIdSuffix").innerHTML='@'+data.supplierCode;
			                    		dom.byId("userProfile.loginId").maxLength = (49-data.supplierCode.length);
                                	}
                                }
                            });
                    	}
                    	else
                    	{
                    		dom.byId("userProfile.loginId").maxLength = 50;
                    		dom.byId("loginIdSuffix").innerHTML='';
                    	}
                    }
                    
                    
                    on(registry.byId("userTypeSelect"), 'change', 
                        function(value)
                        {
                            // clear items from select.
                            dom.byId("roleAvailabel").length=0;
                            dom.byId("roleAssigned").length=0;
                            dom.byId("areaAvailabel").length=0;
                            dom.byId("areaAssigned").length=0;
                            dom.byId("storeAvailabel").length=0;
                            dom.byId("storeAssigned").length=0;
                            dom.byId("wareHouseAvailabel").length=0;
                            dom.byId("wareHouseAssigned").length=0;
                            dom.byId("classAvailable").length=0;
                            dom.byId("classAssigned").length=0;
                            dom.byId("subclassAvailable").length=0;
                            dom.byId("subclassAssigned").length=0;
                            dom.byId("buyerAvailabel").length=0;
                            dom.byId("buyerAssigned").length=0;
                            var companyOid;
                            var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
                            if (value == 2 || value == 6 || value == 4 || value == 7)
                            {
                                if (curUserType ==1)
                                {
                                    companyOid = registry.byId('buyerListSelect').value;
                                }
                                else
                                {
                                    companyOid = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.buyerOid" />';
                                }
                            }
                            if (value == 3 || value == 5)
                            {
                                if (curUserType ==1 || curUserType ==2)
                                {
                                    companyOid = registry.byId('supplierListSelect').value;
                                }
                                else
                                {
                                    companyOid = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.supplierOid" />';
                                }
                            }
                            if (value != 1 && (companyOid == null || companyOid == ""))
                            {
                                alert('<s:text name="B2BPU0163" />');
                                return;
                            }
                            hideProcess(value, curUserType);
                            findRole(value, companyOid);
                            
                            if (value == 2 || value == 4 || value == 6 || value == 7)
                            {
                                findArea(companyOid);
                                findStore(companyOid);
                                findWarehouse(companyOid);
                                findAvailableBuyer(companyOid);
                            }
                            
                            if (value == 2 || value == 4)
                            {
                                findClass();
                                findSubclass(companyOid);
                            }
                                
                            if (1 != value && 6 != value && 7 != value)
                            {
                                registry.byId("group").removeOption(registry.byId("group").getOptions());
                                findGroup(value, companyOid);
                            }
                            setLoginIdSuffix();
                        }
                    );
                    
                    if (dom.byId("buyerListSelect"))
                    {
                        on(registry.byId("buyerListSelect"), 'change', 
                            function(value)
                            {
                                // clear items from select.
                                dom.byId("roleAvailabel").length=0;
                                dom.byId("roleAssigned").length=0;
                                dom.byId("areaAvailabel").length=0;
                                dom.byId("areaAssigned").length=0;
                                dom.byId("storeAvailabel").length=0;
                                dom.byId("storeAssigned").length=0;
                                dom.byId("wareHouseAvailabel").length=0;
                                dom.byId("wareHouseAssigned").length=0;
                                dom.byId("classAvailable").length=0;
                                dom.byId("classAssigned").length=0;
                                dom.byId("subclassAvailable").length=0;
                                dom.byId("subclassAssigned").length=0;
                                dom.byId("buyerAvailabel").length=0;
                                dom.byId("buyerAssigned").length=0;
                                var companyOid = registry.byId('buyerListSelect').value;
                                var userTypeOid = registry.byId('userTypeSelect').value;
                                
                                findRole(userTypeOid, companyOid);
                                findArea(companyOid);
                                findStore(companyOid);
                                findWarehouse(companyOid);
                                findAvailableBuyer(companyOid);
                                
                                if (userTypeOid == 2 || userTypeOid == 4)
                                {
                                    findClass();
                                    findSubclass();
                                }
                                

                                registry.byId("group").removeOption(registry.byId("group").getOptions());
                                findGroup(userTypeOid, companyOid);
                            }
                        );
                    }
                    
                    if (dom.byId("supplierListSelect"))
                    {
                        on(registry.byId("supplierListSelect"), 'change', 
                            function(value)
                            {
                                // clear items from select.
                                dom.byId("roleAvailabel").length=0;
                                dom.byId("roleAssigned").length=0;
                                var companyOid = registry.byId('supplierListSelect').value;
                                var userTypeOid = registry.byId('userTypeSelect').value;
                                
                                findRole(userTypeOid, companyOid);

                                registry.byId("group").removeOption(registry.byId("group").getOptions());
                                findGroup(userTypeOid, companyOid);
                                setLoginIdSuffix();
                            }
                        );
                    }
                        
                    on(registry.byId("active"), 'change', 
                        function(value)
                        {
                            if (value == true)
                            {
                                domStyle.set('blocked',"display","");
                            }
                            else
                            {
                                domStyle.set('blocked',"display","none");
                            }
                        }
                    );
                    
                    changeBuyerAssigned = function()
                    {
                    	// clear items from select.
                        dom.byId("classAvailable").length=0;
                        dom.byId("classAssigned").length=0;
                        dom.byId("subclassAvailable").length=0;
                        dom.byId("subclassAssigned").length=0;
                        var userTypeOid = registry.byId('userTypeSelect').value;
                        
                        if (userTypeOid == 2 || userTypeOid == 4)
                        {
                            findClass();
                            findSubclass();
                        }
                    };
                    
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {
                            selectAll("roleAssigned");
                            selectAll("storeAssigned");
                            selectAll("wareHouseAssigned");
                            selectAll("areaAssigned");
                            selectAll("buyerAssigned");
                            selectAll("classAssigned");
                            selectAll("subclassAssigned");
                        
                            submitForm('mainForm', '<s:url value="/user/saveAdd.action" />');
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/user/initAdd.action" />');
                        }
                    );
                    
                    if (dom.byId("addToGroup"))
                    {
                        on(registry.byId("addToGroup"), 'change', 
                            function(value)
                            {
                                if (value == true)
                                {
                                    domStyle.set('group',"display","");
                                }
                                else
                                {
                                    domStyle.set('group',"display","none");
                                }
                            }
                        );
                    }
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.cancel.without.save" />',
                        yesBtnPressed: function(){
                            changeToURL('<s:url value="/user/init.action?keepSp=Y" />');
                        }
                    });
                    on(registry.byId("cancelBtn"), 'click', 
                        function()
                        {
                            confirmDialog.show();
                        }
                    );
                    setLoginIdSuffix();
                });


                var checkExsit = function(assigned, avail, data)
                {
                     
                     for (var i=0; i < data.length; i++)
                     {
                        var isExist = false;
                        for(var j = 0; j < assigned.options.length; j++)
                        {
                            if(assigned.options[j].value == data[i].storeOid)
                            {
                               isExist = true;
                            }
                        }
                        if(!isExist)
                        {
                            var option = new Option(data[i].storeName + data[i].storeCode , data[i].storeOid);
                            avail.options.add(option);
                        }
                     }
                }

                 function getJsonObject(value)
                {
                    var value2string = JSON.stringify(value.replace(/&quot;/ig,'"').replace(/&amp;/ig,"&")); 
                    var value2Json = JSON.parse(value2string);
                    var value2Object = eval('('+value2Json+')');
                    return value2Object;
                } 
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
        <div class="title"><s:text name='user.create.pageBar' /></div>
    </div>
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token />
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.create.usertypearea.title" />', width:275">
        <table class="commtable">
            <tbody> 
                <tr height="25px">
                    <td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.create.usertypearea.userType' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:select id="userTypeSelect" name="userProfile.userType" list="userTypeList"
                                listKey="userTypeOid" listValue="userTypeDesc"
                                data-dojo-type="dijit.form.Select" theme="simple"/>
                    </td>
                </tr>
                
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.create.contactarea.title" />', width:275">
        <table class="commtable">
            <tbody> 
                <tr height="25px" id="supplier" style="display:none">
                    <td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.create.contactarea.supplier' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="userProfile.companyName" /></td>
                </tr>
                <tr height="25px" id="buyer" style="display:none">
                    <td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.create.contactarea.buyer' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="userProfile.companyName" /></td>
                </tr>
                <tr height="25px">
                    <td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.create.contactarea.loginMode' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:select name="userProfile.loginMode" list="loginModelList" 
                            data-dojo-type="dijit.form.Select" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px">
                    <td><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.create.contactarea.loginId' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:textfield id="userProfile.loginId" name="userProfile.loginId" maxlength="50" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple"/>
                        <span id="loginIdSuffix"></span>
                    </td>
                </tr>
                <tr height="25px">
                    <td></td>
                    <td width="30%"><s:text name='user.create.contactarea.salutation' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:textfield name="userProfile.salutation" maxlength="20" data-dojo-type="dijit.form.TextBox" theme="simple" />
                    </td>
                </tr>
                <tr height="25px">
                    <td><span class="required"></span></td>
                    <td width="30%"><s:text name='user.create.contactarea.sex' /></td>
                    <td width="2%">:</td>
                    <td>
                        <input name="userProfile.gender"  data-dojo-type="dijit/form/RadioButton" value="M" <s:if test='userProfile.gender == "M"'>checked="checked"</s:if>/>
                        <s:text name="radio.male"></s:text>
                        <input name="userProfile.gender"  data-dojo-type="dijit/form/RadioButton" value="F" <s:if test='userProfile.gender == "F"'>checked="checked"</s:if>/>
                        <s:text name="radio.female"></s:text>
                    </td>
                </tr>
                <tr height="25px">
                    <td><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.create.contactarea.name' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:textfield name="userProfile.userName" maxlength="50" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" />
                    </td>
                </tr>
                <tr height="25px">
                    <td><span class="required">*</span></td>
                    <td width="30%"><s:text name='user.create.contactarea.email' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:textfield name="userProfile.email" maxlength="100" data-dojo-props="required: true" data-dojo-type="dijit.form.ValidationTextBox" theme="simple" />
                    </td>
                </tr>
                <tr height="25px">
                    <td></td>
                    <td width="30%"><s:text name='user.create.contactarea.telephone' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:textfield name="userProfile.tel" maxlength="30" data-dojo-type="dijit.form.TextBox" theme="simple" />
                    </td>
                </tr>
                <tr height="25px">
                    <td></td>
                    <td width="30%"><s:text name='user.create.contactarea.fax' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:textfield name="userProfile.fax" maxlength="30" data-dojo-type="dijit.form.TextBox" theme="simple" />
                    </td>
                </tr>
                <tr height="25px">
                    <td></td>
                    <td width="30%"><s:text name='user.create.contactarea.mobile' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:textfield name="userProfile.mobile" maxlength="30" data-dojo-type="dijit.form.TextBox" theme="simple" />
                    </td>
                </tr>
                <tr height="25px">
                    <td></td>
                    <td width="30%"><s:text name='user.create.contactarea.active' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:checkbox id="active" name="userProfile.active" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
                <tr height="25px" id="blocked">
                    <td></td>
                    <td width="30%"><s:text name='user.create.contactarea.block' /></td>
                    <td width="2%">:</td>
                    <td>
                        <s:checkbox name="userProfile.blocked" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div id="supplierList" style="display:none" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.create.supplierarea.title" />', width:275">
        <table class="commtable">
            <tbody> 
                <tr height="25px">
                    <td>
                        <s:select name="userProfile.supplierOid" list="supplierList" id="supplierListSelect"
                                listKey="supplierOid" listValue="supplierName"
                                data-dojo-type="dijit.form.FilteringSelect"  theme="simple"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div id="buyerList" style="display:none" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.create.buyerarea.title" />', width:275">
        <table class="commtable">
            <tbody>
                <tr height="25px">
                    <td>
                        <s:select name="userProfile.buyerOid" list="buyerList" id="buyerListSelect"
                                listKey="buyerOid" listValue="buyerName" 
                                data-dojo-type="dijit.form.FilteringSelect" theme="simple"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" id="buyerAssignList" data-dojo-props="title:'<s:text name="user.create.assgin.buyer.title" />', width:275">
        <table class="commtable">
            <tr>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.assgin.buyer.available"/></td></tr>
                    <tr>
                        <td>
                        <s:select id="buyerAvailabel" name="availabelBuyersList" list="availabelBuyersList" multiple="true"
                                listKey="buyerOid" listValue="buyerName"
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
                <td width="100px">
                  <p><button type="button" onclick="moveSelection('buyerAvailabel','buyerAssigned');changeBuyerAssigned();" data-dojo-type="dijit.form.Button">&gt;</button></p>
                  <p><button type="button" onclick="moveSelection('buyerAssigned','buyerAvailabel');changeBuyerAssigned();" data-dojo-type="dijit.form.Button">&lt;</button></p>
                </td>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.assgin.buyer.assign"/></td></tr>
                    <tr>
                        <td>
                        <s:select id="buyerAssigned" name="selectedBuyersList" list="selectedBuyersList" multiple="true"
                                listKey="buyerOid" listValue="buyerName"
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
              </tr>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.create.rolearea.title" />', width:275">
        <table class="commtable">
            <tr>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.rolearea.available"/></td></tr>
                    <tr>
                        <td>
                        <s:select id="roleAvailabel" name="availabelRolesList" list="availabelRolesList" multiple="true"
                                listKey="roleOid" listValue="roleName + '(' + roleId + ')'"
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
                <td width="100px">
                   <p><button type="button" onclick="moveSelection('roleAvailabel','roleAssigned');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                   <p><button type="button" onclick="moveSelection('roleAssigned','roleAvailabel');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                </td>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.rolearea.assigned"/></td></tr>
                    <tr>
                        <td>
                        <s:select id="roleAssigned" name="selectedRolesList" list="selectedRolesList" multiple="true"
                                listKey="roleOid" listValue="roleName + '(' + roleId + ')'"
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
              </tr>
        </table>
    </div>
    <div class="space"></div>
    <div data-dojo-type="dijit.TitlePane" style="display:none" id="storeList" data-dojo-props="title:'<s:text name="user.create.store.title" />', width:275">
        <table class="commtable">
            <tr>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.area.available"/></td></tr>
                    <tr><td>
	                       <input id="areaAvailabel_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterFunction('areaAvailabel_filter', 'areaAvailabel');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <s:select id="areaAvailabel" name="availabelAreasList" list="availabelAreasList" multiple="true"
                                listKey="areaOid" listValue="areaCode" 
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
                <td width="100px">
                  <p><button type="button" onclick="moveAll('areaAvailabel','areaAssigned');" data-dojo-type="dijit/form/Button"><s:text name="button.addAll" /></button></p>
                  <p><button type="button" onclick="moveSelection('areaAvailabel','areaAssigned');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                  <p><button type="button" onclick="moveSelection('areaAssigned','areaAvailabel');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                  <p><button type="button" onclick="removeAll('areaAssigned','areaAvailabel');" data-dojo-type="dijit/form/Button"><s:text name="button.deleteAll" /></button></p>
                </td>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.area.assigned"/></td></tr>
                    <tr><td>
	                       <input id="areaAssigned_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterRightFunction('areaAssigned_filter', 'areaAssigned');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <s:select id="areaAssigned" name="selectedAreasList" list="selectedAreasList" multiple="true"
                                listKey="areaOid" listValue="areaCode" 
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
              </tr>
            <tr>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.store.available"/></td></tr>
                    <tr><td>
	                       <input id="storeAvailabel_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterFunction('storeAvailabel_filter', 'storeAvailabel');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <s:select id="storeAvailabel" name="availabelStoresList" list="availabelStoresList" multiple="true"
                                listKey="storeOid" listValue="storeName + storeCode" 
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
                <td width="100px">
                  <p><button type="button" onclick="moveAll('storeAvailabel','storeAssigned');" data-dojo-type="dijit/form/Button"><s:text name="button.addAll" /></button></p>
                  <p><button type="button" onclick="moveSelection('storeAvailabel','storeAssigned');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                  <p><button type="button" onclick="moveSelection('storeAssigned','storeAvailabel');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                  <p><button type="button" onclick="removeAll('storeAssigned','storeAvailabel');" data-dojo-type="dijit/form/Button"><s:text name="button.deleteAll" /></button></p>
                </td>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.store.assigned"/></td></tr>
                    <tr><td>
	                       <input id="storeAssigned_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterRightFunction('storeAssigned_filter', 'storeAssigned');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <s:select id="storeAssigned" name="selectedStoresList" list="selectedStoresList" multiple="true"
                                listKey="storeOid" listValue="#this.storeName == '[All Store]' ? storeName : storeName + storeCode" 
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
              </tr>
              <tr>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.warehouse.available"/></td></tr>
                    <tr><td>
	                       <input id="wareHouseAvailabel_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterFunction('wareHouseAvailabel_filter', 'wareHouseAvailabel');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <s:select id="wareHouseAvailabel" name="availabelWareHouseList" list="availabelWareHouseList" multiple="true"
                                listKey="storeOid" listValue="storeName + storeCode" 
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
                <td width="100px">
                  <p><button type="button" onclick="moveAll('wareHouseAvailabel','wareHouseAssigned');" data-dojo-type="dijit/form/Button"><s:text name="button.addAll" /></button></p>
                  <p><button type="button" onclick="moveSelection('wareHouseAvailabel','wareHouseAssigned');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                  <p><button type="button" onclick="moveSelection('wareHouseAssigned','wareHouseAvailabel');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                  <p><button type="button" onclick="removeAll('wareHouseAssigned','wareHouseAvailabel');" data-dojo-type="dijit/form/Button"><s:text name="button.deleteAll" /></button></p>
                </td>
                <td width="350px">
                <table>
                    <tr><td style="font-weight:bold"><s:text name="user.create.warehouse.assigned"/></td></tr>
                    <tr><td>
	                       <input id="wareHouseAssigned_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterRightFunction('wareHouseAssigned_filter', 'wareHouseAssigned');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <s:select id="wareHouseAssigned" name="selectedWareHouseList" list="selectedWareHouseList" multiple="true"
                                listKey="storeOid" listValue="#this.storeName == '[All Warehouse]' ? storeName : storeName + storeCode"
                                data-dojo-type="dijit.form.MultiSelect"
                                cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                        </td>
                    </tr>
                </table>
                </td>
              </tr>
        </table>
    </div>
    <div class="space"></div>
     <div data-dojo-type="dijit.TitlePane" style="display:none" id="classArea" data-dojo-props="title:'<s:text name="user.create.class.title" />', width:275">
        <table class="commtable">
            <tr>
               <td width="350px">
               <table>
                   <tr><td style="font-weight:bold"><s:text name="user.create.class.available"/></td></tr>
                   <tr><td>
	                       <input id="classAvailable_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterFunction('classAvailable_filter', 'classAvailable');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                   <tr>
                       <td>
                       <s:select id="classAvailable" name="availableClassList" list="availableClassList" multiple="true"
                               listKey="classOid" listValue="classCode+'['+buyerCode+']'" 
                               data-dojo-type="dijit.form.MultiSelect"
                               cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                       </td>
                   </tr>
               </table>
               </td>
               <td width="100px">
                 <p><button type="button" onclick="moveAll('classAvailable','classAssigned');" data-dojo-type="dijit/form/Button"><s:text name="button.addAll" /></button></p>
                 <p><button type="button" onclick="moveSelection('classAvailable','classAssigned');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                 <p><button type="button" onclick="moveSelection('classAssigned','classAvailable');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                 <p><button type="button" onclick="removeAll('classAssigned','classAvailable');" data-dojo-type="dijit/form/Button"><s:text name="button.deleteAll" /></button></p>
               </td>
               <td width="350px">
               <table>
                   <tr><td style="font-weight:bold"><s:text name="user.create.class.assigned"/></td></tr>
                   <tr><td>
	                       <input id="classAssigned_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterRightFunction('classAssigned_filter', 'classAssigned');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                   <tr>
                       <td>
                       <s:select id="classAssigned" name="selectedClassList" list="selectedClassList" multiple="true"
                               listKey="classOid" listValue="#this.classCode == '[All Class]'? classCode : classCode+'['+buyerCode+']'" 
                               data-dojo-type="dijit.form.MultiSelect"
                               cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                       </td>
                   </tr>
               </table>
               </td>
             </tr>
            <tr>
               <td width="350px">
               <table>
                   <tr><td style="font-weight:bold"><s:text name="user.create.subclass.available"/></td></tr>
                   <tr><td>
	                       <input id="subclassAvailable_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterFunction('subclassAvailable_filter', 'subclassAvailable');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                   <tr>
                       <td>
                       <s:select id="subclassAvailable" name="availableSubclassList" list="availableSubclassList" multiple="true"
                               listKey="subclassOid" listValue="subclassCode+'('+classCode+')['+buyerCode+']'" 
                               data-dojo-type="dijit.form.MultiSelect"
                               cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                       </td>
                   </tr>
               </table>
               </td>
               <td width="100px">
                 <p><button type="button" onclick="moveAll('subclassAvailable','subclassAssigned');" data-dojo-type="dijit/form/Button"><s:text name="button.addAll" /></button></p>
                 <p><button type="button" onclick="moveSelection('subclassAvailable','subclassAssigned');" data-dojo-type="dijit.form.Button">&gt;</button></p>
                 <p><button type="button" onclick="moveSelection('subclassAssigned','subclassAvailable');" data-dojo-type="dijit.form.Button">&lt;</button></p>
                 <p><button type="button" onclick="removeAll('subclassAssigned','subclassAvailable');" data-dojo-type="dijit/form/Button"><s:text name="button.deleteAll" /></button></p>
               </td>
               <td width="350px">
               <table>
                   <tr><td style="font-weight:bold"><s:text name="user.create.subclass.assigned"/></td></tr>
                   <tr><td>
	                       <input id="subclassAssigned_filter" data-dojo-type="dijit/form/ValidationTextBox" style="width:238px"  >
	                       <button onclick="filterRightFunction('subclassAssigned_filter', 'subclassAssigned');" data-dojo-type="dijit/form/Button">Search</button>
                        </td>
                    </tr>
                   <tr>
                       <td>
                       <s:select id="subclassAssigned" name="selectedSubclassList" list="selectedSubclassList" multiple="true"
                               listKey="subclassOid" listValue="#this.subclassCode == '[All Subclass]'?subclassCode : subclassCode + '('+classCode+')['+buyerCode+']'" 
                               data-dojo-type="dijit.form.MultiSelect"
                               cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " theme="simple"/>
                       </td>
                   </tr>
               </table>
               </td>
             </tr>
        </table>
    </div>
    <div class="space"></div>
    <div id="groupList" data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.create.grouparea.title" />', width:275">
        <table class="commtable">
            <tbody> 
                <tr height="25px">
                    <td>
                        <s:checkbox name="addToGroup" id="addToGroup" data-dojo-type="dijit.form.CheckBox" theme="simple"/>
                        <s:text name="user.create.group.isAddToGroup" />
                        <s:select id="group" name="userProfile.groupOid" list="groupList"
                                listKey="groupOid" listValue="groupName" multiple="false"
                                data-dojo-type="dijit.form.Select" theme="simple"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    </form>
</body>
</html>
