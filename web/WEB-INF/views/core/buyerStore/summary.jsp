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
    	var storeOid = null;
        require(
                [
                "custom/B2BPortalBase",
                "dojo/dom",
                "dijit/registry",
                "dojo/on",
                "dojox/data/QueryReadStore",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojo/parser",
                "dojo/_base/xhr",
                "custom/InformationDialog",
                "dijit/Dialog",
                "dijit/form/Select",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    QueryReadStore,
                    EnhancedGrid,
                    Pagination,
                    parser,
                    xhr,
                    InformationDialog,
                    Dialog,
                    Select
                    )
                {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                
                    var store = new QueryReadStore({url: '<s:url value="/buyerStore/data.action" />'});

                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null" >  
                            { name: "<s:text name='store.summary.grid.buyerName' />", field: "buyerName", width: "10%"},
                            { name: "<s:text name='store.summary.grid.buyerCode' />", field: "buyerCode", width: "10%"},
                            </s:if>
                            { name: "<s:text name='store.summary.grid.storeCode' />", field: "storeCode", width: "10%", formatter:
                            function(field, index, cell){
	                            <s:if test="#session.permitUrl.contains('/buyerStore/viewStore.action')" >
	                                return "<a href=\"javascript:changeToURL('<s:url value="/buyerStore/viewStore.action"/>?param.storeOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'storeOid') + "');\">" + field + "</a>";
	                            </s:if>
	                            <s:else>
	                                return field;
	                            </s:else>
                            }},
                            { name: "<s:text name='store.summary.grid.storeName' />", field: "storeName", width: "22%"},
                            { name: "<s:text name='store.summary.grid.areaCode' />", field: "areaCode", width: "16%", formatter:
                            function(field, index, cell){
                            	<s:if test="#session.permitUrl.contains('/buyerStore/viewArea.action')" >
                            		if (field != null)
                            		{
	                                	return "<a href=\"javascript:changeToURL('<s:url value="/buyerStore/viewArea.action"/>?param.areaOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'areaOid') + "');\">" + field + "</a>";
                            		}
                            		else
                            		{
                                		return field;
                            		}
	                            </s:if>
	                            <s:else>
	                                return field;
	                            </s:else>
                            }},
                            { name: "<s:text name='store.summary.grid.storeCity' />", field: "storeCity", width: "16%"},
                            { name: "<s:text name='store.summary.grid.storeState' />", field: "storeState", width: "16%"},
                            { name: "<s:text name='store.summary.grid.ctryName' />", field: "storeCtryName", width: "16%"}
                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >  
                            ,{ name: "<s:text name='store.summary.grid.assign' />", field: "", width: "6%", formatter:
                            function(field, index, cell){
                            	<s:if test="#session.permitUrl.contains('/buyerStore/assignStore.action')" >
                            		var storeOid = cell.grid.store.getValue(cell.grid.getItem(index), 'storeOid');
                            		var areaOid = cell.grid.store.getValue(cell.grid.getItem(index), 'areaOid');
                            		if (areaOid == null)
                            		{
                            			return '<a href="#" onclick="assign(' + storeOid + ', false);">Assign</a>';
                            		}
	                                return '<a href="#" onclick="assign(' + storeOid + ', true);">Assign</a>';
	                            </s:if>
	                            <s:else>
	                                return "Assign";
	                            </s:else>
                            }}
                            </s:if>
                            ]
                        ],
                        
                        plugins: {
                            pagination: {  
		                    	<s:if test = 'param != null'>
			                        defaultPage: <s:property value="param.requestPage" />,
			                    </s:if>
                                defaultPageSize:<s:if test = 'param == null || param.pageSize == 0'>
			                                        <s:property value="#session.commonParam.defaultPageSize"/>
			                                    </s:if>
			                                    <s:else><s:property value="param.pageSize" /></s:else>,
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
                    
                    grid.canSort=function(col){if(col==7)return false;else return true;};
                    grid.startup();
                    
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var anyRecordSelected = function()
                    {
                        return grid.selection.getSelected().length != 0;
                    }


                    if (dom.byId("searchBtn"))
                    {
                        on(registry.byId("searchBtn"), 'click', function(){

                        	(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                            if(!grid._isLoaded)
                            {
                             	var infoDialog = new InformationDialog({message: "<s:text name='alert.summary.search.not.finished'/>"});
                               	infoDialog.show();
                                return;
                            }
                            
                            xhr.post({
                                url: '<s:url value="/buyerStore/search.action" />',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    grid.setQuery();
                                }
                            });
                        });
                    }
                    
                    if (dom.byId("assignStoreBtn"))
                    {
	                    on(registry.byId("assignStoreBtn"), 'click', 
	                        function()
	                        {
	                        	var url = '<s:url value="/buyerStore/assignStore.action" />?param.storeOid=' + storeOid;
	                            changeToURL(url);
	                        }
	                    );
    				}
                    
                    if (dom.byId("assignAreaBtn"))
                    {
	                    on(registry.byId("assignAreaBtn"), 'click', 
	                        function()
	                        {
	                    		var url = '<s:url value="/buyerStore/assignArea.action" />?param.storeOid=' + storeOid;
	                            changeToURL(url);
	                        }
	                    );
                    }


                    var buyerDialog = new Dialog({title: "Select Buyer", style : "width : 300px"});
                    
                    if (dom.byId("exportBtn"))
                    {
	                    on(registry.byId("exportBtn"), 'click', 
	                        function()
	                        {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
		                    	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >
		                    		var url = '<s:url value="/buyerStore/exportStoreExcel.action" />';
	                            	changeToURL(url);
	                    		</s:if>
	                    		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null" >
		                    		xhr.post({
		                            	url: '<s:url value="/ajax/findBuyerByCurrentSupplier.action" />',
		                                handleAs: "json",
		                                content: {supplierOid: '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.supplierOid" />'},
		                                load:function(jsonData)
		                                {
			                                 if (jsonData == null)
			                                 {
			                                	 var infoDialog = new InformationDialog({message: '<s:text name="B2BPC2309" />'});
			                                	 infoDialog.show();
			                                	 return;
			                                 }
			                                 
			                                 if (jsonData != null && jsonData.length > 1)
			                                 {
				                                 console.log(jsonData.length);
												 var contents = "";
				                                 for (var i = 0 ; i < jsonData.length; i++)
				                                 {
				                                	contents = contents + '<div><input type="radio" data-dojo-type="dijit/form/RadioButton" onClick="hide(this)" value="'+jsonData[i].buyerCode+'" id="'+jsonData[i].buyerOid+'">&nbsp;&nbsp;'+jsonData[i].buyerCode+'&nbsp;&nbsp;&nbsp;&nbsp;('+jsonData[i].buyerName+')</input><div>'
				                                 }
				                                 buyerDialog.set("content", contents);
				                                 buyerDialog.show();
			                                 }

			                                 if (jsonData != null && jsonData.length == 1)
			                                 {
												 var url = '<s:url value="/buyerStore/exportStoreExcel.action" />?buyerOid='+ jsonData[0].buyerOid;
			                                 	 changeToURL(url);
			                                 }
		                                }
		                    		});
	                			</s:if>
	                        }
	                    );
                    }

                    hide = function(obj)
                    {
                        buyerDialog.hide();
                        var buyerOid = obj.id;
                        var url = '<s:url value="/buyerStore/exportStoreExcel.action" />?buyerOid='+ buyerOid;
                    	changeToURL(url);
                    }


                    assign = function(oid, isArea)
                    {
                        if (!isArea)
                        {
                        	var url = '<s:url value="/buyerStore/assignStore.action" />?param.storeOid=' + oid;
                            changeToURL(url);
                        }
                        else
                        {
                        	dijit.byId('assigninfo').show();
                            storeOid = oid;
                        }
                    }
                });
                
    </script>
</head>

<body>
	<div data-dojo-type="dijit.Dialog" id="assigninfo" title="Prompt Dialog">
		<table>
			<tr><td colspan="2">Do You want to assign user(s) for Store or Area?</td></tr>
		    <tr class="space"><td></td></tr>
		    <tr>
				<td style="text-align:right"><button data-dojo-type="dijit.form.Button" id="assignStoreBtn">For Store</button></td>
		        <td style="text-align:left"><button data-dojo-type="dijit.form.Button" id="assignAreaBtn">For Area</button></td>
			</tr>
		</table>
	</div>	
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/buyerStore/search.action')" >
            	<button data-dojo-type="dijit.form.Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/buyerStore/exportStoreExcel.action')" >
            	<button data-dojo-type="dijit.form.Button" id="exportBtn" ><s:text name="button.exportExcel" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>

    <div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="store.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 3 || #session.SESSION_CURRENT_USER_PROFILE.userType == 5">
            <tr>
                <td><s:text name="store.summary.searcharea.buyer" /></td>
                <td>:</td>
                <s:if test="buyers.size() == 1">
	                <td>
	                    <s:select data-dojo-type="dijit/form/FilteringSelect" 
	                        name="param.buyerCode" list="buyers" 
	                        listKey="buyerCode" listValue="buyerName" 
	                        theme="simple"/>
	                </td>
                </s:if>
                <s:else>
                	<td>
	                    <s:select data-dojo-type="dijit/form/FilteringSelect" 
	                        name="param.buyerCode" list="buyers" 
	                        listKey="buyerCode" listValue="buyerName" 
	                        headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                </s:else>
             </tr>
             </s:if>
             <tr>
                <td><s:text name="store.summary.searcharea.storeCode" /></td>
                <td>:</td>
                <td><s:textfield name="param.storeCode" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="store.summary.searcharea.storeName" /></td>
                <td>:</td>
                <td><s:textfield name="param.storeName" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="store.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
