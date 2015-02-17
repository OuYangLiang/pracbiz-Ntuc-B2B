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
                	
                	
                    var store = new QueryReadStore({url: '<s:url value="/popup/tradingPartnerData.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='group.popuTradingPartner.summary.grid.No' />", field: "dojoIndex", width: "5%"},
                            { name: "<s:text name='group.popuTradingPartner.summary.grid.buyerSupplierCode' />", field: "buyerSupplierCode", width: "20%"},
                            { name: "<s:text name='group.popuTradingPartner.summary.grid.buyerCode' />", field: "buyerCode", width: "20%"},
                            { name: "<s:text name='group.popuTradingPartner.summary.grid.buyerName' />", field: "buyerName", width: "20%"},
                            { name: "<s:text name='group.popuTradingPartner.summary.grid.supplierCode' />", field: "supplierCode", width: "20%"},
                            { name: "<s:text name='group.popuTradingPartner.summary.grid.supplierName' />", field: "supplierName", width: "20%"}
                           
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
                            var value = this.store.getValue(item, 'tradingPartnerOid');
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
                                url: '<s:url value="/popup/searchTradingPartners.action" />?tradingPartner.supplierOid=<s:property value="tradingPartner.supplierOid"/>',
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
                    	    opener.selectAllTp = false;
                            var tradingPartner = opener.document.getElementById("tpAssigned");
                            if (tradingPartner.options.length == 1 && tradingPartner.options[0].value == -1)
                            {
                            	tradingPartner.length = 0;
                            }
                            array.forEach(grid.selection.getSelected(), function(item){
                                var oid = store.getValue(item, 'tradingPartnerOid');
                                var buyerName =  store.getValue(item,'buyerName');
                               	var supplierName= store.getValue(item,'supplierName');
                               	var suppBuyerCode = store.getValue(item,'supplierBuyerCode');
                               	var buyerSuppCode = store.getValue(item,'buyerSupplierCode');

                               	var hasItem = false;
                               	array.forEach(tradingPartner.options,
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
                               		var tpDesc = buyerName;
                               		if (suppBuyerCode != null && suppBuyerCode != "")
                               		{
                               			tpDesc = tpDesc + ' ('+ suppBuyerCode +')';
                               		}
                               		
                               		tpDesc = tpDesc + ' - ' + supplierName ;
                               		
                               		if (buyerSuppCode != null && buyerSuppCode != "")
                               		{
                               			tpDesc = tpDesc + ' ('+ buyerSuppCode +')';
                               		}
                               		
                               		operation.innerHTML = tpDesc;//for ff
                               		operation.label = tpDesc;//for ie
            						operation.value = oid;
            						tradingPartner.appendChild(operation);
                               	}
                            });
                  
                            sortSelect(tradingPartner);
                        }
                    );

                    
                    on(registry.byId("addAllBtn"), 'click', 
                        function()
                        {
                    	    var tradingPartner = opener.document.getElementById("tpAssigned");
                    	    tradingPartner.length=0;
                    	    var operation = opener.document.createElement('option');
                            operation.innerHTML = "<s:text name='group.all.tradingPartner.selected' />";//for ff
                            operation.label = "<s:text name='group.all.tradingPartner.selected' />";//for ie
                            operation.value = -1;
                    	    tradingPartner.appendChild(operation);
                    	    opener.selectAllTp = true;
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
	<div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.popuTradingPartner.summary.summary" />', width:275">
	<form id="searchForm" name="searchForm" method="post" >
		<table class="commtable">
    		<tbody>
    			<tr id="buyerCode" height="25px">
            		<td><s:text name='group.popuTradingPartner.summary.searcharea.buyerSupplierCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="tradingPartner.buyerSupplierCode" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>
    			<tr id="buyerCode" height="25px">
            		<td><s:text name='group.popuTradingPartner.summary.searcharea.buyerCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="tradingPartner.buyerCode" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>    
        		<tr id="buyerName" height="25px">
            		<td><s:text name='group.popuTradingPartner.summary.searcharea.buyerName' /></td>
            		<td>:</td>
            		<td><s:textfield name="tradingPartner.buyerName" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>    
        		<tr id="supplierCode" height="25px">
            		<td><s:text name='group.popuTradingPartner.summary.searcharea.supplierCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="tradingPartner.supplierCode" data-dojo-type="dijit/form/TextBox" theme="simple" /></td>
        		</tr>    
        		<tr id="supplierName" height="25px">
            		<td><s:text name='group.popuTradingPartner.summary.searcharea.supplierName' /></td>
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
		<div class="title"><s:text name='group.popuTradingPartner.summary.grid.rltmsg' /></div>
	</div>
	<div id="grid" ></div>
</body>
</html>
