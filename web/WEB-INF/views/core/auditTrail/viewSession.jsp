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

    function selectSession(sessionId)
    {
        window.opener.document.getElementById('param.sessionId').value=sessionId;
        window.close();
        window.opener.document.getElementById('param.sessionId').focus();
    }
    
        require(
                [
                "dojo/dom",
                "dijit/registry",
                "dojo/on",
                "dojox/data/QueryReadStore",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojo/parser",
                "dojo/_base/xhr",
                "custom/InformationDialog",
                "custom/ConfirmDialog",
                "dijit/form/Select",
                "custom/CustomDateTextBox",
                "dojo/string",
                "dojo/domReady!"
                ], 
                function(
                    dom,
                    registry,
                    on,
                    QueryReadStore,
                    EnhancedGrid,
                    Pagination,
                    parser,
                    xhr,
                    InformationDialog,
                    ConfirmDialog,
                    Select,
                    CustomDateTextBox,
                    string
                    )
                {
                    parser.parse();
                
                    var store = new QueryReadStore({url: '<s:url value="/auditTrail/dataSession.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        keepSelection: false,
                        structure: [
                            [
                            { name: "<s:text name='audit.summary.grid.No' />", field: "dojoIndex", width: "40px"},
                            { name: "<s:text name='audit.summary.grid.sessionId' />", field: "sessionId", width: "15%", formatter:
                            function(field, index, cell){
                                return "<a href=\"javascript:selectSession(" + cell.grid.store.getValue(cell.grid.getItem(index), 'sessionId') + ");\">" + field + "</a>";
                            }},
                            { name: "<s:text name='audit.summary.searcharea.sessionId.grid.loginId' />", field: "loginId", width: "15%"},
                            { name: "<s:text name='audit.summary.searcharea.sessionId.grid.startDdate' />", field: "startDate", width: "10%"},
                            { name: "<s:text name='audit.summary.searcharea.sessionId.grid.endDate' />", field: "endDate", width: "10%"}
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
                        }
                    }, "grid");
                    
                    grid.canSort=function(col){if(col==1)return false;else return true;};
                    grid.startup();
                    

                    if (dom.byId("searchBtn"))
                    {
                        on(registry.byId("searchBtn"), 'click', function(){
							if(!grid._isLoaded)
							{
								return;
							}
							                            
	                    	var dateFrom = string.trim(document.getElementById("auditSession.dateFrom").value);
	                        var dateTo = string.trim(document.getElementById("auditSession.dateTo").value);
	                        if(dateFrom.length > 0 && dateTo.length > 0)
	                        {
		                        var fromDate = dateFrom.split("/");
		                        var toDate = dateTo.split("/");
		                        if(new Date(fromDate[2], fromDate[1], fromDate[0])>new Date(toDate[2], toDate[1], toDate[0]))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0133' />"});
		                            infoDialog.show();
		                            return;
		                        }
	                        }
                            xhr.get({
                                url: '<s:url value="/auditTrail/searchSession.action" />',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    grid.setQuery();
                                }
                            });
                        });
                    }

                });

        function clearDate(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).reset();
        }
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
           	<button data-dojo-type="dijit.form.Button" id="searchBtn" ><s:text name="button.search" /></button>
           	<button data-dojo-type="dijit.form.Button" id="closeBtn" onclick="javascript:window.close();"><s:text name="button.close" /></button>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="audit.summary.searcharea.sessionId.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
             <tr>
                <td><s:text name="audit.summary.searcharea.sessionId.summary.loginId" /></td>
                <td>:</td>
                <td><s:textfield name="auditSession.loginId" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="audit.summary.searcharea.sessionId.summary.startDate" /></td>
                <td>:</td>
                <td><label for="Dfrom1"><s:text name="Value.from" /></label>
                    <input type="text" id="auditSession.dateFrom" name="auditSession.dateFrom" 
                    onkeydown="javascript:document.getElementById('param.dateFrom').blur();" 
                    data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
                    <button  data-dojo-type="dijit.form.Button" id="clearDateFrom" type="button" onclick="clearDate('auditSession.dateFrom');">Clear</button>
                    <label for="Dto1"><s:text name="Value.to" /></label>
                    <input type="text" id="auditSession.dateTo" name="auditSession.dateTo" 
                    onkeydown="javascript:document.getElementById('param.dateTo').blur();" 
                    data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
                    <button  data-dojo-type="dijit.form.Button" id="clearDateTo" type="button" onclick="clearDate('auditSession.dateTo');">Clear</button>
                </td>
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
</body>
</html>
