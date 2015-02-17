<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <script>
        var selectAllSupplier = <s:property value="selectAllSupplier"/>;
        var selectAllTp = <s:property value="selectAllTp"/>;
        require(
                [
                "custom/B2BPortalBase",
                "custom/ConfirmDialog",
                "dojo/dom",
                "dojo/dom-style",
                "dijit/registry",
                "dojo/on",
                "dojo/_base/array",
                "dojo/_base/xhr",
                "dojo/parser",
                "dijit/form/Select",
                "dijit/form/CheckBox",
                "dijit/form/RadioButton",
                "dijit/form/ValidationTextBox",
                "dijit/form/MultiSelect",
                "dojo/dom-construct",
                "dojo/NodeList-fx",
                "dijit/form/FilteringSelect",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    ConfirmDialog,
                    dom,
                    domStyle,
                    registry,
                    on,
                	array,
                    xhr,
                    parser,
                    Select,
                    CheckBox,
                    RadioButton,
                    ValidationTextBox,
                    MultiSelect,
                    domConstruct,
                    fx,
                    FilteringSelect
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
                    initData();
                    function initData()
                    {
						var uts = registry.byId('userTypeSelect');
						hideProcess(uts.value);
                    }
                    
                    function hideProcess(value)
                    {
                    	if (2 == value)
                        {
                            // Buyer Admin
                            domStyle.set('buyer', 'display', 'none');
                            domStyle.set('supplier', 'display', 'none');
                            domStyle.set('buyerList', 'display', '');
                            domStyle.set('supplierList', 'display', 'none');
							domStyle.set('tps', 'display', 'none');
							domStyle.set('supps', 'display', '');
                        }
                        else if (3 == value)
                        {
                            // Supplier Admin
                            domStyle.set('buyer', 'display', 'none');
                            domStyle.set('supplier', 'display', 'none');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('supplierList', 'display', '');
                            domStyle.set('tps', 'display', '');
                            domStyle.set('supps', 'display', 'none');

                        }
                        else if (4 == value)
                        {
                            // Buyer User
                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 2" >
                            	domStyle.set('buyerList', 'display', '');
                            	domStyle.set('buyer', 'display', 'none');
                            </s:if>
                            <s:else>
                            	domStyle.set('buyerList', 'display', 'none');
                            	domStyle.set('buyer', 'display', '');
                            </s:else>
                            domStyle.set('supplier', 'display', 'none');
                            domStyle.set('supplierList', 'display', 'none');
							domStyle.set('tps', 'display', 'none');
							domStyle.set('supps', 'display', '');

                        }
                        else if (5 == value)
                        {
                            // Supplier User
                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 3" >
                            	domStyle.set('supplierList', 'display', '');
                            	domStyle.set('supplier', 'display', 'none');
                            </s:if>
                            <s:else>
                            	domStyle.set('supplierList', 'display', 'none');
                            	domStyle.set('supplier', 'display', '');
                            </s:else>
                           	domStyle.set('buyer', 'display', 'none');
                            domStyle.set('buyerList', 'display', 'none');
                            domStyle.set('tps', 'display', '');
                            domStyle.set('supps', 'display', 'none');

                        }
                    }
                    
                    on(registry.byId("userTypeSelect"), 'change', 
                        function(value)
                        {
                            // clear items from select.
                            dom.byId("roleAvailabel").length=0;
                            dom.byId("roleAssigned").length=0;
                            dom.byId("userAssigned").length=0;
                            dom.byId("suppAssigned").length=0;
                            dom.byId("tpAssigned").length=0;
                            
                        	var companyOid;
                        	if (value == 2 || value == 6)
                        	{
                        		companyOid = registry.byId('buyerListSelect').value;
                            }
                        	if (value == 3)
                        	{
                        		companyOid = registry.byId('supplierListSelect').value;
                            }
                            if (value == 4)
                            {
                            	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 2" >
                            		companyOid = registry.byId('buyerListSelect').value;
                            	</s:if>
                            	<s:else>
	                            	companyOid = '<s:property value="%{#session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';
                            	</s:else>
                            }
                            
                            if (value == 5)
                            {
                            	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 3" >
                            		companyOid = registry.byId('supplierListSelect').value;
                            	</s:if>
                            	<s:else>
                            		companyOid = '<s:property value="%{#session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';
                            	</s:else>
                            }
                            
                            hideProcess(value);
                            console.log("value == >>" + value);
                            
                            xhr.get({
                                url: '<s:url value="/ajax/findRolesByUser.action" />',
                                handleAs: "json",
                                content: {userTypeOid: value, companyOid: companyOid},
                                load: function(data)
                                {
                                    var avail = dom.byId('roleAvailabel');
                                    if (data == null)
                                    {
                                    	return;
                                    }
                                    
                                    for (var i=0; i < data.length; i++)
                                    {
                                        var option = new Option(data[i].roleName + '(' + data[i].roleId + ')', data[i].roleOid);
                                        avail.options.add(option);
                                    }
                                }
                            });
                            
                        }
                    );
                    
                    on(registry.byId("buyerListSelect"), 'change', 
                        function(value)
                        {
                            // clear items from select.
                            dom.byId("roleAvailabel").length=0;
                            dom.byId("roleAssigned").length=0;
                            dom.byId("userAssigned").length=0;
                            dom.byId("suppAssigned").length=0;
                        
                            var companyOid = registry.byId('buyerListSelect').value;
                            var userTypeOid = registry.byId('userTypeSelect').value;
                            xhr.get({
                                url: '<s:url value="/ajax/findRolesByBuyer.action" />',
                                handleAs: "json",
                                content: {companyOid: companyOid, userTypeOid:userTypeOid},
                                load: function(data)
                                {
                                    var avail = dom.byId('roleAvailabel');
                                    if (data == null)
                                    {
                                    	return;
                                    }
                                    
                                    for (var i=0; i < data.length; i++)
                                    {
                                        var option = new Option(data[i].roleName + '(' + data[i].roleId + ')', data[i].roleOid);
                                        avail.options.add(option);
                                    }
                                }
                            });
                        }
                    );
                    
                    on(registry.byId("supplierListSelect"), 'change', 
                        function(value)
                        {
                            // clear items from select.
                            dom.byId("roleAvailabel").length=0;
                            dom.byId("roleAssigned").length=0;
                            dom.byId("userAssigned").length=0;
                            dom.byId("tpAssigned").length=0;
                            
                            var companyOid = registry.byId('supplierListSelect').value;
                            var userTypeOid = registry.byId('userTypeSelect').value;
                            
                            xhr.get({
                                url: '<s:url value="/ajax/findRolesBySupplier.action" />',
                                handleAs: "json",
                                content: {companyOid: companyOid, userTypeOid:userTypeOid},
                                
                                load: function(data)
                                {
                                 	if (data == null)
                                    {
                                    	return;
                                    }
                                    var avail = dom.byId('roleAvailabel');
                                    for (var i=0; i < data.length; i++)
                                    {
                                        var option = new Option(data[i].roleName + '(' + data[i].roleId + ')', data[i].roleOid);
                                        avail.options.add(option);
                                    }
                                }
                            });
                        }
                    );
           
					
                    on(registry.byId("addRoleBtn"), 'click', 
                        function()
                        {
                            var selectedRoles = registry.byId("roleAvailabel").getSelected();
                            registry.byId("roleAssigned").addSelected(registry.byId("roleAvailabel"));
                            
                            registry.byId("roleAssigned").getSelected().forEach(function(option){option.selected=false;});
                            
                            dojo.query(selectedRoles).animateProperty({
                                            properties:{
                                                backgroundColor: {start: "#ff6", end: "#fff" }
                                            },
                                            duration :1500
                                        }).play();
                        }
                    );
                    
                    on(registry.byId("delRoleBtn"), 'click', 
                        function()
                        {
                            var selectedRoles = registry.byId("roleAssigned").getSelected();
                            registry.byId("roleAvailabel").addSelected(registry.byId("roleAssigned"));
                            
                            registry.byId("roleAvailabel").getSelected().forEach(function(option){option.selected=false;});
                            
                            dojo.query(selectedRoles).animateProperty({
                                            properties:{
                                                backgroundColor: {start: "#ff6", end: "#fff" }
                                            },
                                            duration :1500
                                        }).play();
                        }
                    );
                    
                    
                    
                    on(registry.byId("addUsersBtn"),'click',
                    	function()
                    	{
                    	    var buyerOid="";
                    	    var supplierOid="";
                    	    var value = registry.byId('userTypeSelect').value;
                            if (value == 2 || value == 4)
                            {
                                buyerOid = registry.byId('buyerListSelect').value;
                            }
                            if (value == 3 || value == 5)
                            {
                                supplierOid = registry.byId('supplierListSelect').value;
                            }
                            
                            console.log("buyerOid==>" + buyerOid + " supplierOid === >" + supplierOid);
                            var selectedOperations = registry.byId("userTypeSelect").value;
                            var url = '<s:url value="/popup/popupViewUser.action" />?userProfile.userType=' 
                                        + selectedOperations + "&userProfile.buyerOid=" + buyerOid + "&userProfile.supplierOid="+supplierOid;
                            window.open (url,'','width=800,height=600,scrollbars=1');
                    	}
                    );
                    
                    on(registry.byId("delUsersBtn"),'click',
                    	function()
                    	{
                    		var user = dijit.byId("userAssigned");
                            array.forEach(user.getSelected(),
                               	function(item)
                               	{
                               		if(item)
                                    {
                                        console.log("remove user == >>" + item.text);
                                        user.domNode.options.remove(item.index);   
                                    }       		
                               	}
                        	);

                    	}
                    );
                    
                    on(registry.byId("addSuppBtn"),'click',
                    	function()
                    	{
                    	    var buyerOid="";
                    	    var value = registry.byId('userTypeSelect').value;
                            if (value == 2)
                            {
                                buyerOid = registry.byId('buyerListSelect').value;
                            }
                         
                            if (value == 4)
                            {
                            	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 2" >
                            		buyerOid = registry.byId('buyerListSelect').value;
                            	</s:if>
                            	<s:else>
                                	buyerOid = '<s:property value="%{#session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';
                            	</s:else>
                            }
                    		
                    		var url = '<s:url value="/popup/popupViewSupplier.action" />?supplier.buyerOid='+buyerOid;
                    		window.open (url,'','width=800,height=600,scrollbars=1');
                    	}
                    );
                    
                    on(registry.byId("delSuppBtn"),'click',
                    	function()
                    	{
                    		var supplier = dijit.byId("suppAssigned");
                            array.forEach(supplier.getSelected(),
                               	function(item)
                               	{
                               		if(item)
                                    {
                                        supplier.domNode.options.remove(item.index);   
                                    }       
                               	}
                        	);
                    	}
                    );
                    
                    on(registry.byId("delAllSuppBtn"),'click',
                        function()
                        {
                            dom.byId("suppAssigned").length=0;
                            selectAllSupplier = false;
                        }
                    );
                    
                    on(registry.byId("addTpBtn"),'click',
                    	function()
                    	{
                            var supplierOid="";
                            var value = registry.byId('userTypeSelect').value;
                            if (value == 3 || value == 5)
                            {
                                supplierOid = registry.byId('supplierListSelect').value;
                            }
                            
                    		var url = '<s:url value="/popup/popupViewTradingPartner.action" />?tradingPartner.supplierOid='+supplierOid;
                    		window.open (url,'','width=800,height=600,scrollbars=1');
                    	}
                    );
                    
                    on(registry.byId("delTpBtn"),'click',
                    	function()
                    	{
                    		var tp = dijit.byId("tpAssigned");
                    		console.log(tp);
                            array.forEach(tp.getSelected(),
                               	function(item)
                               	{
                               		if(item)
                               		{
                               			console.log("remove trading partner == >>" + item.text);
                               			tp.domNode.options.remove(item.index);   
                               		}		
                               	}
                        	);

                    	}
                    );
                    
                    on(registry.byId("delAllTpBtn"),'click',
                    	function()
                    	{
                    		dom.byId("tpAssigned").length=0;
                    		selectAllTp = false;

                    	}
                    );
                    
                    on(registry.byId("saveBtn"), 'click', 
                        function()
                        {   
	                    	selectAll("roleAssigned");
	                        selectAll("userAssigned");
	                        selectAll("suppAssigned");
	                        selectAll("tpAssigned");

                            submitForm('mainForm', '<s:url value="/group/saveAdd.action" />?selectAllSupplier='+selectAllSupplier+'&selectAllTp='+selectAllTp);
                        }
                    );
                    
                    on(registry.byId("resetBtn"), 'click', 
                        function()
                        {
                            changeToURL('<s:url value="/group/initAdd.action" />');
                        }
                    );
                    
                            
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="B2BPU0107" />',
                        yesBtnPressed: function(){
                            changeToURL('<s:url value="/group/init.action?keepSp=Y" />');
                        }
                    });
                    
                    on(registry.byId("cancelBtn"),'click',
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
            <button id="saveBtn" data-dojo-type="dijit/form/Button" ><s:text name='button.save' /></button>
            <button id="resetBtn" data-dojo-type="dijit/form/Button" ><s:text name='button.reset' /></button>
            <button id="cancelBtn" data-dojo-type="dijit/form/Button" ><s:text name='button.cancel' /></button>
        </td></tr></tbody></table>
    </div>
	<!-- here is message area -->
	 
    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    
    <div class="pageBar">
        <div class="title"><s:text name='group.create.pageBar' /></div>
    </div>
    <form id="mainForm" name="mainForm" method="post" >
    <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
    <s:token />
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.create.profileInfo" />', width:275">
    	<table class="commtable">
            <tbody> 
            	<tr>
            		<td width="2px"><span class="required">*</span></td>
                	<td width="30%"><s:text name="group.create.profileInfo.groupId" /></td>
                	<td width="2%">:</td>
                	<td><s:textfield name="groupProfile.groupId"  required="true"  data-dojo-type="dijit/form/ValidationTextBox" maxlength="20" theme="simple"/></td>
             	</tr>
             	<tr>
            		<td width="2px"><span class="required">*</span></td>
                	<td width="30%"><s:text name="group.create.profileInfo.groupDesc" /></td>
                	<td width="2%">:</td>
                	<td><s:textfield name="groupProfile.groupName"  required="true"  data-dojo-type="dijit.form.ValidationTextBox" maxlength="50" theme="simple"/></td>
             	</tr>
                <tr height="25px">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='group.create.profileInfo.userType' /></td>
                    <td width="2%">:</td>
                    <td>
                    	<s:select id="userTypeSelect" name="groupProfile.userTypeOid" 
                    			list="userTypes" listKey="userTypeOid" 
                    			listValue="userTypeDesc" data-dojo-type="dijit/form/Select"
                    			theme="simple"/>
					</td>
                </tr>
                <tr height="25px" id="supplier" style="display:none">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='group.create.profileInfo.supplier' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="groupProfile.supplierName" /></td>
                </tr>
                <tr height="25px" id="buyer" style="display:none">
                	<td width="2px"><span class="required">*</span></td>
                    <td width="30%"><s:text name='group.create.profileInfo.buyer' /></td>
                    <td width="2%">:</td>
                    <td><s:property value="groupProfile.buyerName" /></td>
                </tr>
                
            </tbody>
        </table>
    </div>
    <div class="space"></div>
     <!--supplier company-->
    <div id="supplierList" style="display:none" data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.create.company.supplier" />', width:275">
    	<table class="commtable">
            <tbody> 
                <tr height="25px">
                	<td width="2px"></td>
					<td width="30%"><s:text name="group.create.company.selected"/></td>
					<td width ="2%">:</td>
                    <td>
                    	<s:select name="groupProfile.supplierOid" list="suppliers" id="supplierListSelect"
								listKey="supplierOid" listValue="supplierName"
								data-dojo-type="dijit/form/FilteringSelect" theme="simple"/>
					</td>
                </tr>
            </tbody>
        </table>
    </div>
    <!--buyer company-->
    <div id="buyerList" style="display:none" data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.create.company.buyer" />', width:275">
    	<table class="commtable">
            <tbody>
                <tr height="25px">
                	<td width="2px"></td>
					<td width="30%"><s:text name="group.create.company.selected"/></td>
					<td width ="2%">:</td>
                    <td>
                    	<s:select name="groupProfile.buyerOid" list="buyers"
								listKey="buyerOid" listValue="buyerName"  id="buyerListSelect"
								data-dojo-type="dijit/form/FilteringSelect" theme="simple"/>
					</td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="space"></div>
    <!--group role-->
    <div id="roles" data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.create.role" />', width:275">
  		<table class="commtable">
       		<tr>
	        	<td width="350px">
	            	<table>
				   		<tr>
				   			<td style="font-weight:bold">
				   				<s:text name="group.create.role.available"/>
				   			</td>
				   		</tr>
	                	<tr>
	                  		<td>
								<s:select id="roleAvailabel" name="availabelRoles" 
									list="availabelRoles" multiple="true"
									listKey="roleOid" listValue="roleName + '(' + roleId + ')'"
									data-dojo-type="dijit/form/MultiSelect"
									cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " 
									theme="simple"/>
							</td>
	                	</tr>
	            	</table>
	            </td>
	            <td width="100px">
	              <p><button id="addRoleBtn" data-dojo-type="dijit/form/Button">&gt;</button></p>
	              <p><button id="delRoleBtn" data-dojo-type="dijit/form/Button">&lt;</button></p>
	            </td>
	            <td width="350px">
	            	<table>
				    	<tr>
				    		<td style="font-weight:bold">
				    			<s:text name="group.create.role.assigned"/>
				    		</td>
				    	</tr>
	                	<tr>
	                 		<td>
								<s:select id="roleAssigned" name="selectedRoles" 
									list="selectedRoles" multiple="true"
									listKey="roleOid" listValue="roleName + '(' + roleId + ')'"
									data-dojo-type="dijit/form/MultiSelect"
									cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " 
									theme="simple"/>
							</td>
	                	</tr>
	            	</table>
	            </td>
	        </tr>
        </table>
    </div>
    <div class="space"></div>
    <!--user assigned to group-->
	<div id="users" data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.create.user" />', width:275" >
   		<table class="commtable">
    		<tr>
        		<td width="300px">
        			<s:select id="userAssigned" name="selectedUsers" 
        					list="selectedUsers" multiple="true"
							listKey="userOid" listValue="userName"
							data-dojo-type="dijit/form/MultiSelect"
							cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " 
							theme="simple"/>
        		</td>
		 		<td width="100px">
		 			<p><button id="addUsersBtn" data-dojo-type="dijit/form/Button" ><s:text name="button.select"/></button></p>
		    		<p><button id="delUsersBtn" data-dojo-type="dijit/form/Button" ><s:text name="button.delete"/></button></p>
				</td>
				<td width="300px">
				</td>
       		</tr>
   		</table>
	</div>
	<div class="space"></div>
	<!--tp assigned to group-->
	<div id="tps" data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.create.tp" />', width:275" >
   		<table class="commtable">
    		<tr>
        		<td width="300px">
        			<s:select id="tpAssigned" name="selectedTps" 
        					list="selectedTps" multiple="true"
							listKey="tradingPartnerOid" listValue="tradingPartnerDesc"
							data-dojo-type="dijit/form/MultiSelect"
							cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " 
							theme="simple"/>
        		</td>
		 		<td width="100px">
		 			<p><button id="addTpBtn" data-dojo-type="dijit/form/Button" ><s:text name="button.select"/></button></p>
		    		<p><button id="delTpBtn" data-dojo-type="dijit/form/Button" ><s:text name="button.delete"/></button></p>
		    		<p><button id="delAllTpBtn" data-dojo-type="dijit/form/Button" ><s:text name="button.deleteAll"/></button></p>
				</td>
				<td width="300px">
				</td>
       		</tr>
   		</table>
	</div>
	<div class="space"></div>
	<!--supp assigned to group-->
	<div id="supps" data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.create.supp" />', width:275" >
   		<table class="commtable">
    		<tr>
        		<td width="300px">
        			<s:select id="suppAssigned" name="selectedSupps" 
        					list="selectedSupps" multiple="true"
							listKey="supplierOid" listValue="supplierName"
							data-dojo-type="dijit/form/MultiSelect"
							cssStyle="width:300px; height:200px; font-family:Arial, Helvetica, sans-serif; " 
							theme="simple"/>
        		</td>
		 		<td width="100px">
		 			<p><button id="addSuppBtn" data-dojo-type="dijit/form/Button" ><s:text name="button.select"/></button></p>
		    		<p><button id="delSuppBtn" data-dojo-type="dijit/form/Button" ><s:text name="button.delete"/></button></p>
		    		<p><button id="delAllSuppBtn" data-dojo-type="dijit/form/Button" ><s:text name="button.deleteAll"/></button></p>
				</td>
				<td width="300px">
				</td>
       		</tr>
   		</table>
	</div>
    </form>
</body>
</html>
