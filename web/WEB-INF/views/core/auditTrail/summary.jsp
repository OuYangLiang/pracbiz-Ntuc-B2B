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
                "dojo/parser",
                "dojo/_base/xhr",
                "custom/InformationDialog",
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
                    parser,
                    xhr,
                    InformationDialog
                    )
                {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                    
                    var store = new QueryReadStore({url: '<s:url value="/auditTrail/data.action" />'});
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        keepSelection: false,
                        structure: [
                            [
                            { name: "<s:text name='audit.summary.grid.No' />", field: "dojoIndex", width: "3%"},
                            { name: "<s:text name='audit.summary.grid.oid' />", field: "auditTrailOid", width: "7%", formatter:
	                            function(field, index, cell){
                            	   return "<a href=\"#\" onclick=\"javascript:window.open('<s:url value='/popup/showXmlContent.action' />?param.auditTrailOid=" + cell.grid.store.getValue(cell.grid.getItem(index), 'auditTrailOid') + "','','width=800,height=600,scrollbars=1')\">" + field + "</a>";
                            }},
                            { name: "<s:text name='audit.summary.grid.recordKey' />", field: "recordKey", width: "15%"},
                            { name: "<s:text name='audit.summary.grid.sessionId' />", field: "sessionId", width: "13%"},
                            { name: "<s:text name='audit.summary.grid.userType' />", field: "userTypeDesc", width: "13%"},
                            { name: "<s:text name='audit.summary.grid.actor' />", field: "actor", width: "10%"},
                            { name: "<s:text name='audit.summary.grid.actionDate' />", field: "actionDate", width: "12%"},
                            { name: "<s:text name='audit.summary.grid.action' />", field: "actorAction", width: "8%"},
                            { name: "<s:text name='audit.summary.grid.recordType' />", field: "recordType", width: "16%"},
                            { name: "<s:text name='audit.summary.grid.accessIp' />", field: "clientIp", width: "13%"}
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
                    
                    grid.canSort=function(col){if(col==1)return false;else return true;};
                    grid.startup();


                    function checkDate()
                    {
                    	var dateFrom = string.trim(document.getElementById("param.dateFrom").value);
                        var dateTo = string.trim(document.getElementById("param.dateTo").value);
                        if(dateFrom.length > 0 && dateTo.length > 0)
                        {
                            var fromDate = dateFrom.split("/");
                            var toDate = dateTo.split("/");
                            if(new Date(fromDate[2], fromDate[1], fromDate[0])>new Date(toDate[2], toDate[1], toDate[0]))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1501' />"});
                                infoDialog.show();
                                return;
                            }
                        }
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
                            
                        	checkDate();
                            xhr.post({
                                url: '<s:url value="/auditTrail/search.action" />',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    grid.setQuery();
                                }
                            });
                        });
                    }
                    
                    if (dom.byId("downloadBtn"))
                    {
	                    on(registry.byId("downloadBtn"), 'click', 
                    		function()
                            {
	                    		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
		                    	xhr.get({
	                                url: '<s:url value="/auditTrail/checkDownload.action" />',
	                                handleAs: 'json',
	                                load: function(rlt)
	                                {
	                                    if(rlt == 'noResult')
	                                    {
	                                        new InformationDialog({message: "<s:text name='B2BPC1502' />"}).show();
	                                    }
	                                    else
	                                    {
                                    	   changeToURL('<s:url value="/auditTrail/saveDownload.action" />');
	                                    }
	                                }
	                            });
                            }
	                    );
                    }
                    
                });

        function clearDate(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).attr("aria-valuenow", null);
            registry.byId(src).attr("value", null);
        }
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <s:if test="#session.permitUrl.contains('/auditTrail/search.action')" >
           	    <button data-dojo-type="dijit.form.Button" id="searchBtn" ><s:text name="button.search" /></button>
           	</s:if>
           	<s:if test="#session.permitUrl.contains('/auditTrail/saveDownload.action')" >
           	    <button data-dojo-type="dijit.form.Button" id="downloadBtn" ><s:text name="button.download" /></button>
           	</s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="audit.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
             <tr>
                <td><s:text name="audit.summary.searcharea.userType" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit/form/Select" name="param.userTypeOid" list="userTypes" 
                        listKey="userTypeOid" listValue="userTypeId" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.summary.searcharea.action" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit/form/Select" name="param.actorAction" list="actorActions" 
                        listKey="key" listValue="key" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.summary.searcharea.actionDate" /></td>
                <td>:</td>
                <td><label for="Dfrom1"><s:text name="Value.from" /></label>
                    <input type="text" id="param.dateFrom" name="param.dateFrom" value='<s:date name="param.dateFrom" format="yyyy-MM-dd" />'
                    onkeydown="javascript:document.getElementById('param.dateFrom').blur();" 
                    data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}' />
                    <button  data-dojo-type="dijit.form.Button" id="clearDateFrom" type="button" onclick="clearDate('param.dateFrom');">Clear</button>
                    <label for="Dto1"><s:text name="Value.to" /></label>
                    <input type="text" id="param.dateTo" name="param.dateTo" 
                    onkeydown="javascript:document.getElementById('param.dateTo').blur();" value='<s:date name="param.dateTo" format="yyyy-MM-dd" />'
                    data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
                    <button  data-dojo-type="dijit.form.Button" id="clearDateTo" type="button" onclick="clearDate('param.dateTo');">Clear</button>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.summary.searcharea.actor" /></td>
                <td>:</td>
                <td><s:textfield name="param.actor" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="audit.summary.searcharea.accessIp" /></td>
                <td>:</td>
                <td><s:textfield name="param.clientIp" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="audit.summary.searcharea.sessionId" /></td>
                <td>:</td>
                <td><s:textfield id="param.sessionId" name="param.sessionId" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
                <button data-dojo-type="dijit.form.Button" onClick="javascript:window.open ('<s:url value='/popup/viewSession.action'/>','','width=800,height=600,scrollbars=1')"><s:text name="button.select" /></button>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.summary.searcharea.recordKey" /></td>
                <td>:</td>
                <td><s:textfield name="param.recordKey" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="audit.summary.searcharea.recordType" /></td>
                <td>:</td>
                <td><s:select data-dojo-type="dijit/form/Select" name="param.recordType" list="recordTypes" 
                        listKey="value" listValue="key" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/></td>
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
