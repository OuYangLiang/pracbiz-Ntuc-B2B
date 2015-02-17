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
                
                    var store = new QueryReadStore({url: '<s:url value="/buyer/data.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='buyer.summary.grid.No' />", field: "dojoIndex", width: "20px"},
                            { name: "<s:text name='buyer.summary.grid.buyerName' />", field: "buyerName", width: "18%", formatter:
                            function(field, index, cell){
                                <s:if test="#session.permitUrl.contains('/buyer/initEdit.action')" >
	                                return "<a href=\"javascript:changeToURL('<s:url value="/buyer/initEdit.action"/>?param.buyerOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'buyerOid') + "');\">" + field + "</a>";
	                            </s:if>
	                            <s:elseif test="#session.permitUrl.contains('/buyer/view.action')" >
	                                return "<a href=\"javascript:changeToURL('<s:url value="/buyer/view.action"/>?param.buyerOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'buyerOid') + "');\">" + field + "</a>";
	                            </s:elseif>
	                            <s:else>
	                                return field;
	                            </s:else>
                            }},
                            { name: "<s:text name='buyer.summary.grid.buyerCode' />", field: "buyerCode", width: "15%"},
                            { name: "<s:text name='buyer.summary.grid.mailBox' />", field: "mboxId", width: "15%"},
                            { name: "<s:text name='buyer.summary.grid.channel' />", field: "channel", width: "10%"},
                            { name: "<s:text name='buyer.summary.grid.active' />", field: "active", width: "7%", formatter:
                            function(field, index, cell){
                                var status = cell.grid.store.getValue(cell.grid.getItem(index), 'active');
                                return status==true ?"<s:text name='select.active' />":"<s:text name='select.inactive' />";
                            }},
                           	<s:if test="#session.permitUrl.contains('/buyer/initEditMsgSetting.action') || #session.permitUrl.contains('/buyer/initViewMsgSetting.action')" >
                            { name: "<s:text name='buyer.summary.grid.msgSetting' />", field: "", width: "10%", formatter:
                            function(field, index, cell){
                            	<s:if test="#session.permitUrl.contains('/buyer/initEditMsgSetting.action')" >
	                                return "<a href=\"javascript:changeToURL('<s:url value="/buyer/initEditMsgSetting.action"/>?param.buyerOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'buyerOid') + "');\">Edit</a>";
                            	</s:if>
                            	<s:elseif test="#session.permitUrl.contains('/buyer/initViewMsgSetting.action')" >
                            	    return "<a href=\"javascript:changeToURL('<s:url value="/buyer/initViewMsgSetting.action"/>?param.buyerOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'buyerOid') + "');\">View</a>";
                            	</s:elseif>
                            }},
                            </s:if>
                           	<s:if test="#session.permitUrl.contains('/buyer/initEditBusinessRule.action')" >
                            { name: "<s:text name='buyer.summary.grid.businessRule' />", field: "", width: "10%", formatter:
                            function(field, index, cell){
                                return "<a href=\"javascript:changeToURL('<s:url value="/buyer/initEditBusinessRule.action"/>?param.buyerOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'buyerOid') + "');\">Edit</a>";
                            }},
                            </s:if>
                            { name: "<s:text name='buyer.summary.grid.updateDate' />", field: "updateDate", width: "13%"}
                            ]
                        ],
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width:'3%', styles:'text-align: center;'},
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
                    
                    grid.canSort=function(col){if(col==2 ||col==7 || col==8)return false;else return true;};
                    grid.startup();
                    
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.delete.records" />',
                        yesBtnPressed: function(){
                            fn('<s:url value="/buyer/saveDelete.action" />');
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
                                var oid = store.getValue(item, 'buyerOid');
                                oids = oids + oid + '-';
                            }
                            catch (e)
                            {
                                // there may be a bug in dojo's selection plugin if select multiple records from different pages.
                                //alert(e);
                            }
                        });
                        
                        oids = oids.substring(0, oids.length-1);
                        
                        xhr.get({
                                url: '<s:url value="/buyer/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                load: function(data)
                                {
                                	if (data!='x')
                               		{
                                		(new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                               		}
                                	else
                               		{
	                               	    changeToURL(url);
                               		}
                                }
                            });
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
                                url: '<s:url value="/buyer/search.action" />',
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
	                    on(registry.byId("createBtn"), 'click', 
	                        function()
	                        {
	                            changeToURL('<s:url value="/buyer/initAdd.action" />');
	                        }
	                    );
                    }


                    if (dom.byId("deleteBtn"))
                    {
                        on(registry.byId("deleteBtn"), 'click', 
                            function()
                            {
                                if (!anyRecordSelected())
                                {
                                    infoDialog.show();
                                    return;
                                }
                            
                                confirmDialog.show();
                            }
                        );
                    }

                });
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/buyer/search.action') " >
            	<button data-dojo-type="dijit.form.Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
        	<s:if test="#session.permitUrl.contains('/buyer/initAdd.action') && #session.SESSION_CURRENT_USER_PROFILE.userType == 1" >
            	<button data-dojo-type="dijit.form.Button" id="createBtn" ><s:text name="button.create" /></button>
            </s:if>
        	<s:if test="#session.permitUrl.contains('/buyer/saveDelete.action') && #session.SESSION_CURRENT_USER_PROFILE.userType == 1" >
            	<button data-dojo-type="dijit.form.Button" id="deleteBtn" ><s:text name="button.delete" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="buyer.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
             <tr>
                <td><s:text name="buyer.summary.searcharea.buyerCode" /></td>
                <td>:</td>
                <td><s:textfield name="param.buyerCode" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="buyer.summary.searcharea.buyerName" /></td>
                <td>:</td>
                <td><s:textfield name="param.buyerName" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="buyer.summary.searcharea.mailBox" /></td>
                <td>:</td>
                <td><s:textfield name="param.mboxId" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="buyer.summary.searcharea.active" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.active" list="status" 
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
        <div class="title"><s:text name="buyer.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
