<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
    @import "<s:url value='/js/dojo-root/dojox/grid/resources/Grid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/resources/claroGrid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/%{#session.layoutTheme}/EnhancedGrid.css' />";
    @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css' />";
    </style>
        
    <script>
        require(
                [
                "dojo/dom",
                "dojo/dom-style",
                "dijit/registry",
                "dojo/on",
                "dijit/form/Button",
                "dijit/form/Select",
                "dojox/data/QueryReadStore",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojox/grid/_SelectionPreserver",
                "dojox/grid/enhanced/plugins/IndirectSelection",
                "dojox/grid/cells",
                "dojo/parser",
                "dojo/_base/xhr",
                "dojo/_base/window",
                "dojo/_base/array",
                "dojo/domReady!"
                ], 
                function(
                    dom,
                    domStyle,
                    registry,
                    on,
                    Button,
                    Select,
                    QueryReadStore,
                    EnhancedGrid,
                    Pagination,
                    _SelectionPreserver,
                    IndirectSelection,
                    cells,
                    parser,
                    xhr,
                    win,
                    array
                    )
                {
                	parser.parse();
                	/*initData();
                    function initData()
                    {
						var value = '<s:property value="userProfile.userType"/>';
						hideProcess(value);
                    }
                    
                    function hideProcess(value)
                    {
                    	if (2 == value || 4 == value)
                        {
                            // Buyer
                            domStyle.set('supplierCode', 'display', 'none');
                            domStyle.set('supplierName', 'display', 'none');
                           	domStyle.set('buyerCode', 'display', '');
                            domStyle.set('buyerName', 'display', '');
                        }
                        else if (3 == value || 5 == value)
                        {
                            // Supplier
                            domStyle.set('supplierCode', 'display', '');
                            domStyle.set('supplierName', 'display', '');
                           	domStyle.set('buyerCode', 'display', 'none');
                            domStyle.set('buyerName', 'display', 'none');
                        }
                        
                    }*/
                	
                    var store = new QueryReadStore({url: '<s:url value="/popup/userData.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='group.popuUser.summary.grid.No' />", field: "dojoIndex", width: "5	%"},
                            { name: "<s:text name='group.popuUser.summary.grid.userName' />", field: "userName", width: "40%"},
                            { name: "<s:text name='group.popuUser.summary.grid.loginId' />", field: "loginId", width: "40%"}
                            ]
                        ],
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width:'5%', styles:'text-align: center;'},
                            pagination: {  
                                defaultPageSize: <s:property value="#session.commonParam.defaultPageSize"/>,
                                pageSizes: [<s:property value="#session.commonParam.pageSizes"/>],
                                position: "bottom",
                                maxPageStep: 7,
                                gotoButton: true,
                                description: true,
                                sizeSwitch: true,
                                pageStepper: true
                            }
                        }
                        
                    }, "grid");
                    
                
                    
                    grid.startup();
                    
                    on(registry.byId("searchBtn"), 'click', 
                        function()
                        {
                        	if(!grid._isLoaded)
                           	{
                               	return;
                           	}
                           	
                            var parameter='userProfile.userType=<s:property value="userProfile.userType"/>'
                                        +'&userProfile.buyerOid=<s:property value="userProfile.buyerOid"/>'
                                        +'&userProfile.supplierOid=<s:property value="userProfile.supplierOid"/>'
                                        +'&userProfile.groupOid=<s:property value="userProfile.groupOid"/>';
                            xhr.post({
                                url: '<s:url value="/popup/searchUsers.action" />?' + parameter,
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    grid.setQuery();
                                }
                            });
                        }
                    );
                    
                    
                    on(registry.byId("addBtn"), 'click', 
                        function()
                        {
                        	array.forEach(grid.selection.getSelected(), function(item){
                                var oid = store.getValue(item, 'userOid');
                               	var userName= store.getValue(item,'userName');
                               	var user = opener.document.getElementById("userAssigned");
								
                               	var hasItem = false;
                               	array.forEach(user.options,
                               		function(item, idx, arr){
                               			var value = item.value;
                               			if (value == oid)
                               			{
                               				hasItem = true;
                               			}
                               		}
                               	);
                               	if (!hasItem)
                               	{
                               		var operation = opener.document.createElement('option');
                               		operation.innerHTML = userName;//for ff
                               		operation.label = userName;// for ie
            						operation.value = oid;
            						user.appendChild(operation);
                               	}
                            });
                  
                        }
                    );

                    on(registry.byId("colseBtn"), 'click', 
                        function()
                        {
	   						window.close();
	   					}
                    );
                    
                });
                
    </script>
</head>

<body>
	<!-- Button Area -->
	<div>
		<table class="btnContainer">
		<tbody>
		<tr>
			<td>
    			<button id="searchBtn" data-dojo-type="dijit/form/Button" ><s:text name='button.search' /></button>
    			<button id="addBtn" data-dojo-type="dijit/form/Button" ><s:text name='button.add' /></button>
    			<button id="colseBtn" data-dojo-type="dijit/form/Button" ><s:text name='button.close' /></button>
    		</td>
    	</tr>
    	</tbody>
		</table>
	</div>
	<!-- here is message area -->
	<div class="required">
		<s:actionerror />
		<s:fielderror />
	</div>
	<!-- Search Area -->
	<div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.popuUser.summary.summary" />', width:275">
	<form id="searchForm" name="searchForm" method="post" >
		<table class="commtable">
    		<tbody>
    		  <tr id="buyerCode" height="25px">
                    <td><s:text name='group.popuUser.summary.searcharea.loginId' /></td>
                    <td>:</td>
                    <td><s:textfield name="userProfile.loginId" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
                </tr>    
                <tr id="buyerName" height="25px">
                    <td><s:text name='group.popuUser.summary.searcharea.userName' /></td>
                    <td>:</td>
                    <td><s:textfield name="userProfile.userName" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
                </tr>    
    		 <!--
        		<tr id="buyerCode" height="25px">
            		<td><s:text name='group.popuUser.summary.searcharea.buyerCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="userProfile.buyerCode" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>    
        		<tr id="buyerName" height="25px">
            		<td><s:text name='group.popuUser.summary.searcharea.buyerName' /></td>
            		<td>:</td>
            		<td><s:textfield name="userProfile.buyerName" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>    
        		<tr id="supplierCode" height="25px">
            		<td><s:text name='group.popuUser.summary.searcharea.supplierCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="userProfile.supplierCode" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>    
        		<tr id="supplierName" height="25px">
            		<td><s:text name='group.popuUser.summary.searcharea.supplierName' /></td>
            		<td>:</td>
            		<td><s:textfield name="userProfile.supplierName" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>
              -->
    		</tbody>
		</table>
	</form>
	</div>

	<div class="space"></div>
	<!-- Recrod Area -->
	<div class="pageBar">
		<div class="title"><s:text name='group.popuUser.summary.grid.rltmsg' /></div>
	</div>
	<div id="grid" ></div>
</body>
</html>
