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
                	
                	
                    var store = new QueryReadStore({url: '<s:url value="/popup/supplierDataForDn.action" />'});

                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='group.popuSupplier.summary.grid.No' />", field: "dojoIndex", width: "5%"},
                            { name: "<s:text name='group.popuSupplier.summary.grid.supplierCode' />", field: "buyerSupplierCode", width: "40%",formatter:
                            function(field, index, cell){
                                var addr1 = cell.grid.store.getValue(cell.grid.getItem(index), 'supplierAddr1');
                                var addr2 = cell.grid.store.getValue(cell.grid.getItem(index), 'supplierAddr2');
                                var addr3 = cell.grid.store.getValue(cell.grid.getItem(index), 'supplierAddr3');
                                var addr4 = cell.grid.store.getValue(cell.grid.getItem(index), 'supplierAddr4');
                                var addr = (addr1 == null ? "" : addr1) + "<br/>" + (addr2 == null ? "" : addr2) + "<br/>" 
                                + (addr3 == null ? "" : addr3) + "<br/>" + (addr4 == null ? "" : addr4);
                                return "<a href=\"javascript:selectSupp('"+
                                cell.grid.store.getValue(cell.grid.getItem(index), 'buyerSupplierCode') + "','" + 
                                cell.grid.store.getValue(cell.grid.getItem(index), 'supplierName') + "','" +
                                addr +"');\">" + field + "</a>";
                            }},
                            { name: "<s:text name='group.popuSupplier.summary.grid.supplierName' />", field: "supplierName", width: "40%"}
                           
                            ]
                        ],
                        
                        plugins: {
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
                               	return;
                           	}
                           	
                            xhr.post({
                                url: '<s:url value="/popup/searchSuppliersForDn.action" />?tradingPartner.buyerOid=<s:property value="tradingPartner.buyerOid"/>',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    grid.setQuery();
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
        
        function selectSupp(supplierCodeVal, supplierNameVal, supplierAddrVal)
        {
            var supplierCode = opener.document.getElementById('supplierCode');
            var supplierName = opener.document.getElementById('supplierName');
            var supplierAddr = opener.document.getElementById('supplierAddr');
            supplierCode.value = supplierCodeVal;
            supplierName.innerHTML = supplierNameVal;
            supplierAddr.innerHTML = supplierAddrVal;
            window.close();

        };
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
	<div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.popuSupplier.summary.summary" />', width:275">
	<form id="searchForm" name="searchForm" method="post" >
		<table class="commtable">
    		<tbody>
        		<tr id="supplierCode" height="25px">
            		<td><s:text name='group.popuSupplier.summary.searcharea.supplierCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="tradingPartner.buyerSupplierCode" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>    
        		<tr id="supplierName" height="25px">
            		<td><s:text name='group.popuSupplier.summary.searcharea.supplierName' /></td>
            		<td>:</td>
            		<td><s:textfield name="tradingPartner.supplierName" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
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
