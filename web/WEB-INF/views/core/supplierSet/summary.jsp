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
                "dojo/string",
                "dojox/data/QueryReadStore",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojox/grid/enhanced/plugins/IndirectSelection",
                "dojo/parser",
                "dojo/_base/xhr",
                "custom/CustomDateTextBox",
                "dojo/_base/array",
                "custom/InformationDialog",
                "custom/ConfirmDialog",
                "dojo/domReady!"
                ], 
                function(
                    B2BPortalBase,
                    dom,
                    registry,
                    on,
                    string,
                    QueryReadStore,
                    EnhancedGrid,
                    Pagination,
                    IndirectSelection,
                    parser,
                    xhr,
                    CustomDateTextBox,
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
                    
                    var store = new QueryReadStore({url: '<s:url value="/supplierSet/data.action" />'});
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        keepSelection: false,
                        structure: [
                            [
                            { name: "<s:text name='supplierSet.summary.grid.setId' />", field: "setId", width: "15%", formatter:
	                            function(field, index, cell){
	                            	<s:if test="#session.permitUrl.contains('/supplierSet/initEdit.action')" >
		                                return "<a href=\"javascript:changeToURL('<s:url value="/supplierSet/initEdit.action"/>?param.setOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'setOid') + "');\">" + field + "</a>";
		                            </s:if>
		                            <s:elseif test="#session.permitUrl.contains('/supplierSet/view.action')" >
		                                return "<a href=\"javascript:changeToURL('<s:url value="/supplierSet/view.action"/>?param.setOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'setOid') + "');\">" + field + "</a>";
		                            </s:elseif>
		                            <s:else>
		                                return field;
		                            </s:else>
                            }},
                            { name: "<s:text name='supplierSet.summary.grid.setDescription' />", field: "setDescription", width: "40%"},
                            { name: "<s:text name='supplierSet.summary.grid.suppliers' />", field: "supplierListString", width: "40%", formatter : 
                                function(field, index, cell){
                                if (field == null || "" == field)
                                {
                                    return '&nbsp;';
                                }
                                
                                var companies = field.split("new-line");
                                var rlt = companies[0];
                                
                                for (var i=1; i<companies.length; i++)
                                {
                                    rlt = rlt + "</br>" + companies[i];
                                }
                                
                                return rlt;}
                            },
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
                    
                    grid.canSort=function(col){if(col==4)return false;else return true;};
                    grid.startup();

                    
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
                                url: '<s:url value="/supplierSet/search.action" />',
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
                                changeToURL('<s:url value="/supplierSet/initAdd.action" />');
                            }
                        );
                    }

                    if (dom.byId("deleteBtn"))
                    {
                        on(registry.byId("deleteBtn"), 'click', 
                            function()
                            {
                        		console.log(grid.selection.getSelected().length);
                                //changeToURL('<s:url value="/supplierSet/delete.action" />');
                                if (grid.selection.getSelected().length < 1)
                                {
                                	 var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2506' />"});
                                     infoDialog.show();
                                     return;
                                }

                                var confirmDialog = new ConfirmDialog({
	                                message: '<s:text name="alert.delete.records" />',
	                                yesBtnPressed: function(){
	                                	var oids = "";
	                           			array.forEach(grid.selection.getSelected(), function(item){
	                                        try
	                                        {
	                                            var oid = grid.store.getValue(item, 'setOid');
	                                            oids = oids + oid + '-';
	                                        }
	                                        catch (e)
	                                        {
	                                            // there may be a bug in dojo's selection plugin if select multiple records from different pages.
	                                            console.log(e);
	                                        }
	                                    });
										var csrfToken = dom.byId("csrfToken").value;
	                                    xhr.get({
	                                        	url : '<s:url value="/supplierSet/putParamIntoSession.action" />',
	                                        	content: {selectedOids: oids},
	                                        	load: function(data)
	                                        	{
	                                        		changeToURL('<s:url value="/supplierSet/delete.action" />?csrfToken='+csrfToken);
	                                        	}
	                                        });
		                                }});
                                
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
            <s:if test="#session.permitUrl.contains('/supplierSet/search.action')" >
           	    <button data-dojo-type="dijit.form.Button" id="searchBtn" ><s:text name="button.search" /></button>
           	</s:if>
           	<s:if test="#session.permitUrl.contains('/supplierSet/initAdd.action')" >
                <button data-dojo-type="dijit.form.Button" id="createBtn" ><s:text name="button.create" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/supplierSet/delete.action')" >
                <button data-dojo-type="dijit.form.Button" id="deleteBtn" ><s:text name="button.delete" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="supplierSet.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
        <table class="commtable">
            <tbody>
             <tr>
                <td><s:text name="supplierSet.summary.searcharea.setId" /></td>
                <td>:</td>
                <td><s:textfield name="param.setId" data-dojo-type="dijit.form.TextBox" maxlength="20" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="supplierSet.summary.searcharea.setDescription" /></td>
                <td>:</td>
                <td><s:textfield name="param.setDescription" data-dojo-type="dijit.form.TextBox" maxlength="150" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="supplierSet.summary.searcharea.supplier" /></td>
                <td>:</td>
                <td><s:textfield name="param.supplierListString" data-dojo-type="dijit.form.TextBox" maxlength="150" theme="simple"/></td>
             </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="audit.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" ></div>
    <div id="div1" ></div>
</body>
</html>
