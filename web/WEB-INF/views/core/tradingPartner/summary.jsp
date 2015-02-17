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
                "custom/B2BPortalBase",
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
                "custom/InformationDialog",
                "custom/ConfirmDialog",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
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
                    array,
                    InformationDialog,
                    ConfirmDialog
                    )
                {
                	parser.parse();
                	
                	(new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                	
                	
                    var store = new QueryReadStore({url: '<s:url value="/tp/data.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='tp.summary.grid.No' />", field: "dojoIndex", width: "5%"},
                            { name: "<s:text name='tp.summary.grid.buyerSupplierCode' />", field: "buyerSupplierCode", width: "20%", formatter:
                            function(field, index, cell){
                            	var hasEditPri = <s:if test="#session.permitUrl.contains('/tp/initEdit.action')" >true</s:if><s:else>false</s:else>;
                                var hasViewPri = <s:if test="#session.permitUrl.contains('/tp/view.action')" >true</s:if><s:else>false</s:else>;
                                
                                var tradingPartnerOid = cell.grid.store.getValue(cell.grid.getItem(index), 'tradingPartnerOid');
                                
                                if (hasEditPri)
                                {
                                	return "<a href=\"javascript:edit('" + tradingPartnerOid + "');\">" + field + "</a>";
                                }
                                else if (hasViewPri)
                                {
                                	return "<a href=\"javascript:view('" + tradingPartnerOid + "');\">" + field + "</a>";
                                }
                                else
                                {
                                    return field;
                                }
                              }
                            },
                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 2" >
                            { name: "<s:text name='tp.summary.grid.buyerCode' />", field: "buyerCode", width: "20%"},
                            { name: "<s:text name='tp.summary.grid.buyerName' />", field: "buyerName", width: "30%"},
                            </s:if>
                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 3" >
                            { name: "<s:text name='tp.summary.grid.supplierCode' />", field: "supplierCode", width: "20%"},
                            { name: "<s:text name='tp.summary.grid.supplierName' />", field: "supplierName", width: "30%"},
                            </s:if>
                            { name: "<s:text name='tp.summary.grid.status' />", field: "active", width: "10%", formatter:
                                function(field, index, cell){
                            		return cell.grid.store.getValue(cell.grid.getItem(index), 'activeStatusValue');
                        		}
                        	}
                           
                            ]
                        ],
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width:'5%', styles:'text-align: center;'},
                            pagination: {  
                            	<s:if test = 'tradingPartner != null'>
	                                defaultPage: <s:property value="tradingPartner.requestPage" />,
	                            </s:if>
	                            defaultPageSize:<s:if test = 'tradingPartner == null || tradingPartner.pageSize == 0'>
	                                                <s:property value="#session.commonParam.defaultPageSize"/>
	                                            </s:if>
	                                            <s:else><s:property value="tradingPartner.pageSize" /></s:else>,
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
                    
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.delete.records" />',
                        yesBtnPressed: function(){
                            var oids = "";
                            array.forEach(grid.selection.getSelected(), function(item){
                                try
                                {
                                    var oid = store.getValue(item, 'tradingPartnerOid');
                                    oids = oids + oid + '-';
                                }
                                catch (e)
                                {
                                    //do nothing, just have a bug in dojo
                                }
                            });
                                
                            oids = oids.substring(0, oids.length-1);
                            var csrfToken = dom.byId("csrfToken").value;
                            xhr.get({
                                url: '<s:url value="/tp/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                load: function(data){
                                    changeToURL('<s:url value="/tp/saveDelete.action" />?csrfToken='+csrfToken);
                                }
                            });
                        }
                    });

                    var anyRecordSelected = function()
                    {
                        return grid.selection.getSelected().length != 0;
                    }
                    
                    if (dom.byId("searchBtn"))
                    {
	                    on(registry.byId("searchBtn"), 'click', 
	                        function()
	                        {
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
	                                url: '<s:url value="/tp/search.action" />',
	                                form: dom.byId("searchForm"),
	                                load: function(rlt)
	                                {
	                                    grid.setQuery();
	                                }
	                            });
	                        }
	                    );
                    }
                    
                    if (dom.byId("createBtn"))
                    {
	                    on(registry.byId("createBtn"), 'click', 
	                        function()
	                        {
	                            changeToURL('<s:url value="/tp/initAdd.action" />');
	                        }
	                    );
                    }

                    

                    if (dom.byId("deleteBtn"))
                    {
	                    on(registry.byId("deleteBtn"), 'click', function(){
	                    	if (!anyRecordSelected())
                            {
                                infoDialog.show();
                                return;
                            }
                        
                            confirmDialog.show();
	                    });
                    }
                    
                });
        
		        function view(tpOid)
		        {
		        	changeToURL('<s:url value="/tp/view.action" />' + "?tradingPartner.tradingPartnerOid=" + tpOid);
		        }
		        function edit(tpOid)
		        {
		        	changeToURL('<s:url value="/tp/initEdit.action" />' + "?tradingPartner.tradingPartnerOid=" + tpOid);
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
    			<s:if test="#session.permitUrl.contains('/tp/search.action')" >
	            <button id="searchBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.search' /></button>
           		</s:if>
    			<s:if test="#session.permitUrl.contains('/tp/initAdd.action')" >
            	<button id="createBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.create' /></button>
            	</s:if>
            	<s:if test="#session.permitUrl.contains('/tp/saveDelete.action')" >
	            <button id="deleteBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.delete' /></button>
	            </s:if>
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
	<div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="group.popuTradingPartner.summary.summary" />', width:275">
	<form id="searchForm" name="searchForm" method="post" >
	<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
		<table class="commtable">
    		<tbody>
    			<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 2 && #session.SESSION_CURRENT_USER_PROFILE.userType != 4" >
    			<tr id="buyerCode" height="25px">
            		<td><s:text name='group.popuTradingPartner.summary.searcharea.buyerCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="tradingPartner.buyerCode" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
        		</tr>    
        		</s:if>
        		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 3 && #session.SESSION_CURRENT_USER_PROFILE.userType != 5" >
        		<tr id="supplierCode" height="25px">
            		<td><s:text name='group.popuTradingPartner.summary.searcharea.supplierCode' /></td>
            		<td>:</td>
            		<td><s:textfield name="tradingPartner.supplierCode" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
        		</tr>    
        		</s:if>
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
