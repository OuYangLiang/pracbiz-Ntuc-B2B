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
                "custom/CustomDateTextBox",
                "dijit/form/TextBox",
                "custom/InformationDialog",
                "dojo/string",
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
                    CustomDateTextBox,
                    TextBox,
                    InformationDialog,
                    string,
                    array
                    )
                {
                	parser.parse();
                	
                	(new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                	
                    var store = new QueryReadStore({url: '<s:url value="/log/data.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='log.summary.grid.No' />", field: "dojoIndex", width: "5%"},
                            { name: "<s:text name='log.summary.grid.fileName' />", field: "fileName", width: "20%", formatter: 
                            function(field, index, cell){
                                var hasViewPri = <s:if test="#session.permitUrl.contains('/log/view.action')" >true</s:if><s:else>false</s:else>;
                                
                                var hashCode = cell.grid.store.getValue(cell.grid.getItem(index), 'hashCode');
                                
                                if (hasViewPri)
                                {
                                	return "<a href=\"javascript:view('" + hashCode + "');\">" + field + "</a>";
                                }
                                else
                                {
                                    return field;
                                }
                              }  
                            },
                            { name: "<s:text name='log.summary.grid.fileSize' />", field: "fileSize", width: "25%", formatter: 
                                function(field, index, cell)
                                {
                                	return field + " kb"
                                }
                            },
                            { name: "<s:text name='log.summary.grid.lastUpdate' />", field: "lastModifiedTime", width: "15%"}
                            ]
                        ],
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width:'3%', styles:'text-align: center;'},
                            pagination: {  
                            	<s:if test = 'logFile != null'>
	                                defaultPage: <s:property value="logFile.requestPage" />,
	                            </s:if>
	                            defaultPageSize:<s:if test = 'logFile == null || logFile.pageSize == 0'>
	                                                <s:property value="#session.commonParam.defaultPageSize"/>
	                                            </s:if>
	                                            <s:else><s:property value="logFile.pageSize" /></s:else>,
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
                            var value = this.store.getValue(item, 'hashCode');
                            //alert(value);
                        }
                    }, "grid");
                    
                    //grid.canSort=function(col){if(col==2 || col==8 || col==9)return false;else return true;};
                    
                    grid.startup();

                    var anyRecordSelected = function()
                    {
                        return grid.selection.getSelected().length != 0;
                    }

                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
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
	                        	
		                    	var validFrom = string.trim(dom.byId("fromDate").value);
		                        var validTo = string.trim(dom.byId("toDate").value);
		                        var fromDate = validFrom.split("/");
		                        var toDate = validTo.split("/");
		                        if(new Date(fromDate[2], fromDate[1], fromDate[0])>new Date(toDate[2], toDate[1], toDate[0]))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1302' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                        if(new Date(fromDate[2], fromDate[1]-1, fromDate[0]) > new Date())
                                {
                                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1303' />"});
                                    infoDialog.show();
                                    return;
                                }
	                            xhr.post({
	                                url: '<s:url value="/log/search.action" />',
	                                form: dom.byId("searchForm"),
	                                load: function(rlt)
	                                {
	                                    grid.setQuery();
	                                }
	                            });
	                        }
	                    );
                    }

                    if (dom.byId("clearFromDate"))
                    {
	                    on(registry.byId("clearFromDate"), 'click', 
	                        function()
	                        {
		                    	registry.byId('fromDate').attr("aria-valuenow", null);
	                    		registry.byId('fromDate').attr("value", null);
	                        }
	                    );
                    }

                    if (dom.byId("clearToDate"))
                    {
	                    on(registry.byId("clearToDate"), 'click', 
	                        function()
	                        {
	                    		registry.byId('toDate').attr("aria-valuenow", null);
	                    		registry.byId('toDate').attr("value", null);
	                        }
	                    );
                    }

                    if (dom.byId("downloadBtn"))
                    {
	                    on(registry.byId("downloadBtn"), 'click', 
                    		function()
                            {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                                if (!anyRecordSelected())
                                {
                                    infoDialog.show();
                                    return;
                                }
                            
                                var oids = "";
                                array.forEach(grid.selection.getSelected(), function(item){
                                	try
                                    {
        	                            var oid = store.getValue(item, 'hashCode');
        	                            oids = oids + oid + '|';
                                    }
                                    catch (e)
                                    {
                                        //do nothing, just have a bug in dojo
                                    }
                                });
                                
                                oids = oids.substring(0, oids.length-1);
                                xhr.get({
                                        url: '<s:url value="/log/putParamIntoSession.action" />',
                                        content: {selectedFiles: oids},
                                        load: function(data)
                                        {
                                           	changeToURL('<s:url value="/log/saveDownload.action" />');
                                        }
                                    });
                            }
	                    );
                    }
					
                });
        
                function view(hashCode)
                {
                	changeToURL('<s:url value="/log/view.action" />' + "?logFile.hashCode=" + hashCode);
                }
    </script>
</head>

<body class="claro">
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/log/search.action')" >
	            <button id="searchBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.search' /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/log/saveDownload.action')" >
            <button id="downloadBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.download' /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
	<!-- here is message area -->
	<div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="log.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="get" >
        <table class="commtable">
            <tbody>
                <tr height="25px">
                    <td><s:text name='log.summary.searcharea.fileName' /></td>
                    <td>:</td>
                    <td><s:textfield name="logFile.searchFileName" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
                </tr>    
                <tr height="25px">
                    <td><s:text name='log.summary.searcharea.lastUpdate' /></td>
                    <td>:</td>
                    <td>
                    	<label for="fromDate"><s:text name='log.summary.searcharea.lastUpdateFrom' />:</label>
                    	<input id="fromDate" name="logFile.searchLastModifiedFrom" data-dojo-type="custom/CustomDateTextBox" 
                    		data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'  
                    		value='<s:date name="logFile.searchLastModifiedFrom" format="yyyy-MM-dd" />'/>
                    	<button id="clearFromDate" data-dojo-type="dijit.form.Button" type="button"><s:text name='button.clear' /></button>
                    	<label for="toDate"><s:text name='log.summary.searcharea.lastUpdateTo' />:</label>
                    	<input id="toDate" name="logFile.searchLastModifiedTo" data-dojo-type="custom/CustomDateTextBox" 
                    		data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                    		value='<s:date name="logFile.searchLastModifiedTo" format="yyyy-MM-dd" />' />
                    	<button id="clearToDate" data-dojo-type="dijit.form.Button" type="button"><s:text name='button.clear' /></button>
                    </td>
                </tr>    
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name='log.summary.grid.rltmsg' /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
