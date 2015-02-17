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
                "dijit/registry",
                "dojo/on",
                "dojox/data/QueryReadStore",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojox/grid/enhanced/plugins/IndirectSelection",
                "dojo/parser",
                "dojo/_base/xhr",
                "custom/InformationDialog",
                "custom/ConfirmDialog",
                "dijit/form/Select",
                "dojo/_base/array",
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
                    IndirectSelection,
                    parser,
                    xhr,
                    InformationDialog,
                    ConfirmDialog,
                    Select,
                    array
                    )
                {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                
                    var store = new QueryReadStore({url: '<s:url value="/supplier/data.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='supplier.summary.grid.no' />", field: "dojoIndex", width: "5%"},
                            { name: "<s:text name='supplier.summary.grid.supplierName' />", field: "supplierName", width: "19%", formatter: 
                            function(field, index, cell){
                                var hasEditPri = <s:if test="#session.permitUrl.contains('/supplier/initEdit.action')" >true</s:if><s:else>false</s:else>;
                                var hasViewPri = <s:if test="#session.permitUrl.contains('/supplier/view.action')" >true</s:if><s:else>false</s:else>;
                                var supplierOid = cell.grid.store.getValue(cell.grid.getItem(index), 'supplierOid');
                                
                                if (hasEditPri)
                                {
                                    return "<a href=\"javascript:changeToURL('<s:url value="/supplier/initEdit.action"/>?supplier.supplierOid=" + supplierOid + "');\">" + field + "</a>";
                                }
                                else if (hasViewPri)
                                {
                                    return "<a href=\"javascript:changeToURL('<s:url value="/supplier/view.action"/>?supplier.supplierOid=" + supplierOid + "');\">" + field + "</a>";
                                }
                                else
                                {
                                    return field;
                                }
                            }}, 
                            { name: "<s:text name='supplier.summary.grid.supplierCode' />", field: "supplierCode", width: "14%"},
                            { name: "<s:text name='supplier.summary.grid.mailBox' />", field: "mboxId", width: "10%"},
                            { name: "<s:text name='supplier.summary.grid.active' />", field: "active", width: "6%", formatter:
                            function(field, index, cell){
                                var status = cell.grid.store.getValue(cell.grid.getItem(index), 'active');
                                return status==true ?"<s:text name='select.active' />":"<s:text name='select.inactive' />";
                            }},
                            { name: "<s:text name='supplier.summary.grid.blocked' />", field: "blocked", width: "6%", formatter:
                            function(field, index, cell){
                                var status = cell.grid.store.getValue(cell.grid.getItem(index), 'blocked');
                                return status==true ?"<s:text name='select.yes' />":"<s:text name='select.no' />";
                            }}
                            <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType != 5" >
	                           	<s:if test="#session.permitUrl.contains('/supplier/initEditMsgSetting.action') || #session.permitUrl.contains('/supplier/initViewMsgSetting.action')" >
	                            ,{ name: "<s:text name='supplier.summary.grid.msg' />", field: "supplierOid", width: "10%", formatter: 
	                            function(field, index, cell){
	                                <s:if test="#session.permitUrl.contains('/supplier/initEditMsgSetting.action')" >
	                                    return "<a href=\"javascript:changeToURL('<s:url value="/supplier/initEditMsgSetting.action"/>?supplier.supplierOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierOid') + "');\">Edit</a>";
	                                </s:if>
	                                <s:elseif test="#session.permitUrl.contains('/supplier/initViewMsgSetting.action')" >
	                                    return "<a href=\"javascript:changeToURL('<s:url value="/supplier/initViewMsgSetting.action"/>?supplier.supplierOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierOid') + "');\">View</a>";
	                                </s:elseif>
	                            }}
	                           	</s:if>
	                           	<s:if test="#session.permitUrl.contains('/supplier/initEditTermAndCondition.action')" >
	                            ,{ name: "<s:text name='supplier.summary.grid.termAndCondition' />", field: "supplierOid", width: "10%", formatter: 
	                            function(field, index, cell){
	                            	return "<a href=\"javascript:changeToURL('<s:url value="/supplier/initEditTermAndCondition.action"/>?supplier.supplierOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'supplierOid') + "');\">Edit</a>";
	                            }}
	                           	</s:if>
                            </s:if>
                            ]
                        ],
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width:'3%', styles:'text-align: center;'},
                            pagination: {  
                            	<s:if test = 'supplier != null'>
	                                defaultPage: <s:property value="supplier.requestPage" />,
	                            </s:if>
	                            defaultPageSize:<s:if test = 'supplier == null || supplier.pageSize == 0'>
	                                                <s:property value="#session.commonParam.defaultPageSize"/>
	                                            </s:if>
	                                            <s:else><s:property value="supplier.pageSize" /></s:else>,
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
                    
                    //sort column
                    grid.canSort=function(col){if(col==2 || col==8 || col==9)return false;else return true;};
                    grid.startup();
                    
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.delete.records" />',
                        yesBtnPressed: function(){
                            var csrfToken = dom.byId("csrfToken").value;
                            fn('<s:url value="/supplier/saveDelete.action" />?csrfToken='+csrfToken);
                        }
                    });
                    
                  
                    var anyRecordSelected = function()
                    {
                        return grid.selection.getSelected().length != 0;
                    }
                  
                    var fn = function(url)
                    {
                    	if (!anyRecordSelected())
                        {
                            infoDialog.show();
                            return;
                        }
                    	
                        var oids = "";
                        
                        array.forEach(grid.selection.getSelected(), function(item){
	                        try
	                        {
	                            var oid = store.getValue(item, 'supplierOid');
	                            oids = oids + oid + '-';
	                        }
	                        catch(e)
	                        {
	                            // there may be a bug in dojo's selection plugin if select multiple records from different pages.
	                            // alert(e);
	                        }
                        });
                        
                        oids = oids.substring(0, oids.length-1);
                        
                        xhr.get({
                                url: '<s:url value="/supplier/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                load: function(data)
                                {
                                    changeToURL(url);
                                }
                        });
                    };
                    
                    
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
                                url: '<s:url value="/supplier/search.action" />',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    grid.setQuery();
                                }
                            });
                        });
                    }
                    
                    if (dom.byId("createBtn"))
                    {
                        on(registry.byId("createBtn"), 'click', function(){
                            changeToURL('<s:url value="/supplier/initAdd.action" />');
                        });
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
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
         <s:if test="#session.permitUrl.contains('/supplier/search.action')" >
            <button id="searchBtn" data-dojo-type="dijit.form.Button" ><s:text name="button.search" /></button>
         </s:if>
          <s:if test="#session.permitUrl.contains('/supplier/initAdd.action')" >
            <button id="createBtn" data-dojo-type="dijit.form.Button" ><s:text name="button.create" /></button>
         </s:if>
         <s:if test="#session.permitUrl.contains('/supplier/saveDelete.action')" >
            <button id="deleteBtn" data-dojo-type="dijit.form.Button" ><s:text name="button.delete" /></button>
         </s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'Supplier Profile Summary', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
        <table class="commtable">
            <tbody>
                <tr height="25px">
                    <td><s:text name='supplier.summary.searcharea.supplierCode' /></td>
                    <td>:</td>
                    <td><s:textfield name="supplier.supplierCode" data-dojo-type="dijit.form.TextBox" theme="simple"/></td>
                </tr>    
                <tr height="25px">
                    <td><s:text name='supplier.summary.searcharea.supplierName' /></td>
                    <td>:</td>
                    <td><s:textfield name="supplier.supplierName" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
                </tr>    
                <tr height="25px">
                    <td><s:text name='supplier.summary.searcharea.mailBox' /></td>
                    <td>:</td>
                    <td><s:textfield name="supplier.mboxId" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
                </tr>
                <tr>
                <td><s:text name="supplier.summary.searcharea.active" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="supplier.active" list="status" 
                        listKey="key" listValue="value" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title">Result Area</div>
    </div>
    <div id="grid" ></div>
</body>
</html>
