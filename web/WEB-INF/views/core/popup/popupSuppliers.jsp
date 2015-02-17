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
                	
                	
                    var store = new QueryReadStore({url: '<s:url value="/popup/supplierData.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='group.popuSupplier.summary.grid.No' />", field: "dojoIndex", width: "5%"},
                            { name: "<s:text name='group.popuSupplier.summary.grid.supplierCode' />", field: "supplierCode", width: "40%"},
                            { name: "<s:text name='group.popuSupplier.summary.grid.supplierName' />", field: "supplierName", width: "40%"}
                           
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
                        },
                        
                        onSelected: function(index){
                            var item = this.getItem(index);
                            var value = this.store.getValue(item, 'supplierOid');
                            //alert(value);
                        }
                    }, "grid");
                    
                
                    
                    grid.startup();
                    
                    on(registry.byId("searchBtn"), 'click', 
                        function()
                        {
                        	if(!grid._isLoaded)
                           	{
                        	 	var infoDialog = new InformationDialog({message: "<s:text name='alert.summary.search.not.finished'/>"});
                               	infoDialog.show();
                               	return;
                           	}
                           	
                            xhr.post({
                                url: '<s:url value="/popup/searchSuppliers.action" />?supplier.buyerOid=<s:property value="supplier.buyerOid"/>',
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
                    	   opener.selectAllSupplier = false;
                           var supp = opener.document.getElementById("suppAssigned");
                           if (supp.options.length == 1 && supp.options[0].value == -1)
                           {
                        	   supp.length = 0;
                           }
                    	   array.forEach(grid.selection.getSelected(),function(item){
                                var oid = store.getValue(item, 'supplierOid');
                               	var suppName= store.getValue(item,'supplierName');

                               	var hasItem = false;
                               	array.forEach(supp.options,
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
                               		operation.innerHTML = suppName;//for ff
                                    operation.label = suppName;//for ie
            						operation.value = oid;
            						supp.appendChild(operation);
                               	}
                            });
                    	   sortSelect(supp);
                        }
                    );
                    on(registry.byId("addAllBtn"), 'click', 
                        function()
                        {
                    	   var supp = opener.document.getElementById("suppAssigned");
                    	   supp.length = 0;
                    	   var operation = opener.document.createElement('option');
                           operation.innerHTML = "<s:text name='group.all.supplier.selected' />";//for ff
                           operation.label = "<s:text name='group.all.supplier.selected' />";//for ie
                           operation.value = -1;
                    	   supp.appendChild(operation);
                    	   opener.selectAllSupplier = true;
                    	   window.close();
                        }
                    );

                    on(registry.byId("colseBtn"), 'click', 
                        function()
                        {
	   						window.close();
	   					}
                    );
                    
                });
        function compareFun(a,b)
        {
            if (a[0].length == 0 || b[0].length == 0)
            {
                return 1;
            }
            if (a[0].toUpperCase().charCodeAt(0) > b[0].toUpperCase().charCodeAt(0))
            {
                return 1;
            }
            return -1;
        }
        
        function sortSelect(selElem) 
        {
            var tmpAry = new Array();
            for (var i=0;i<selElem.options.length;i++) {
                    tmpAry[i] = new Array();
                    tmpAry[i][0] = selElem.options[i].text;
                    tmpAry[i][1] = selElem.options[i].value;
            }
            tmpAry.sort(compareFun);
            selElem.length = 0;
            for (var i=0;i<tmpAry.length;i++) {
                    var operation = opener.document.createElement('option');
                    operation.innerHTML = tmpAry[i][0];//for ff
                    operation.label = tmpAry[i][0];//for ie
                    operation.value = tmpAry[i][1];
                    selElem.appendChild(operation);
            }
            return;
        }
                
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
    			<button id="addAllBtn" data-dojo-type="dijit/form/Button" ><s:text name='button.addAll' /></button>
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
	<div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.popuSupplier.summary.summary" />', width:275">
	<form id="searchForm" name="searchForm" method="post" >
		<table class="commtable">
    		<tbody>
        		<tr id="supplierCode" height="25px">
            		<td><s:text name='group.popuSupplier.summary.searcharea.supplierCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="supplier.supplierCode" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>    
        		<tr id="supplierName" height="25px">
            		<td><s:text name='group.popuSupplier.summary.searcharea.supplierName' /></td>
            		<td>:</td>
            		<td><s:textfield name="supplier.supplierName" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr> 
    		</tbody>
		</table>
	</form>
	</div>

	<div class="space"></div>
	<!-- Recrod Area -->
	<div class="pageBar">
		<div class="title"><s:text name='group.popuSupplier.summary.grid.rltmsg' /></div>
	</div>
	<div id="grid" ></div>
</body>
</html>
