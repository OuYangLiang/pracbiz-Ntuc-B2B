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
                "dijit/form/Select",
                "custom/CustomDateTextBox",
                "dojo/string",
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
                    Select,
                    CustomDateTextBox,
                    string
                    )
                {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                
                    var store = new QueryReadStore({url: '<s:url value="/auditAccess/data.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='audit.access.summary.grid.No' />", field: "dojoIndex", width:"3%"},
                            { name: "<s:text name='audit.access.summary.grid.accessParty' />", field: "principalType", width: "6%",
                            	formatter:  function(field, index, cell){
                            	    return cell.grid.store.getValue(cell.grid.getItem(index), 'principalTypeValue');
                                }
                            },
                            { name: "<s:text name='audit.access.summary.grid.loginId' />", field: "loginId", width: "11%"},
                            { name: "<s:text name='audit.access.summary.grid.actionDate' />", field: "actionDate", width: "10%"},
                            { name: "<s:text name='audit.access.summary.grid.actionType' />", field: "actionType", width: "6%",
                            	formatter:  function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'actionTypeValue');
                                }
                            },
                            { name: "<s:text name='audit.access.summary.grid.success' />", field: "success", width: "6%",
                            	formatter:  function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'successValue');
                                }
                            },
                            { name: "<s:text name='audit.access.summary.grid.attemptNo' />", field: "attemptNo", width: "8%"},
                            { name: "<s:text name='audit.access.summary.grid.clientIp' />", field: "clientIp", width: "10%"},
                            { name: "<s:text name='audit.access.summary.grid.errorCode' />", field: "errorCode", width: "10%",
                            	formatter:  function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'errorDesc');
                                }
                            }
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
                    
                    grid.canSort=function(col){if(col==2)return false;else return true;};
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
    						
                            var attemptNo = string.trim(document.getElementById("param.attemptNo").value);
                            if(attemptNo !=null && attemptNo!= "" && (attemptNo.length > 8 ||isNaN(attemptNo)|| parseInt(attemptNo) != attemptNo) )
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1401'/>"});
                                infoDialog.show();
                                return;
                            }
                            if (attemptNo !=null && attemptNo!= "")
                            {
                            	document.getElementById("param.attemptNo").value=parseInt(attemptNo);
                            }
                            
                            var dateFrom = string.trim(document.getElementById("param.dateFrom").value);
                            var dateTo = string.trim(document.getElementById("param.dateTo").value);
                            if (dateFrom.length > 0)
                            {
                            	var fromDate = dateFrom.split("/");
                            	if(new Date(fromDate[2], fromDate[1]-1, fromDate[0])>new Date())
                                {
                                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1403' />"});
                                    infoDialog.show();
                                    return;
                                }
                            }
                            
                            if(dateFrom.length > 0 && dateTo.length > 0)
                            {
                                var fromDate = dateFrom.split("/");
                                var toDate = dateTo.split("/");
                                if(new Date(fromDate[2], fromDate[1]-1, fromDate[0])>new Date(toDate[2], toDate[1]-1, toDate[0]))
                                {
                                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1402' />"});
                                    infoDialog.show();
                                    return;
                                }
                            }
                            xhr.post({
                                url: '<s:url value="/auditAccess/search.action" />',
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
                        	   changeToURL('<s:url value="/auditAccess/saveDownload.action" />');
                            }   
                        );
                    }
                    
                    on(registry.byId("clearDateFrom"), 'click', 
                        function()
                        {
                            registry.byId("param.dateFrom").attr("aria-valuenow", null);
                            registry.byId("param.dateFrom").attr("value", null);
                        }   
                    );
                    
                    on(registry.byId("clearDateTo"), 'click', 
                        function()
                        {
                            registry.byId("param.dateTo").attr("aria-valuenow", null);
                            registry.byId("param.dateTo").attr("value", null);
                        }   
                    );
                    
                });

    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
            <s:if test="#session.permitUrl.contains('/auditAccess/search.action')" >
           	    <button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
           	</s:if>
           	<s:if test="#session.permitUrl.contains('/auditAccess/saveDownload.action')" >
           	    <button data-dojo-type="dijit/form/Button" id="downloadBtn" ><s:text name="button.download" /></button>
           	</s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="audit.access.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
              <tr>
                <td><s:text name="audit.access.summary.searcharea.accessParty" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit/form/Select" name="param.principalType" list="principalTypes" 
                        listKey="key" listValue="%{getText(Value)}" theme="simple"/>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.access.summary.searcharea.userType" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit/form/Select" name="param.userTypeOid" list="userTypes" 
                        listKey="userTypeOid" listValue="userTypeId" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.access.summary.searcharea.actionType" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit/form/Select" name="param.actionType" list="accessActionTypes" 
                        listKey="key" listValue="%{getText(Value)}" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.access.summary.searcharea.attemptNo" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit/form/Select" name="param.arithmeticTerm" list="arithmeticTerms" 
                        listKey="key" listValue="%{getText(Value)}" headerKey="" headerValue="%{getText('select.select')}" theme="simple"/>
                    <s:textfield id="param.attemptNo" name="param.attemptNo" data-dojo-type="dijit/form/TextBox" maxlength="8" theme="simple"/>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.access.summary.searcharea.actionDate" /></td>
                <td>:</td>
                <td><label for="Dfrom1"><s:text name="Value.from" /></label>
                    <input type="text" id="param.dateFrom" name="param.actionDateFrom" value='<s:date name="param.actionDateFrom" format="yyyy-MM-dd" />'
                    onkeydown="javascript:document.getElementById('param.dateFrom').blur();" 
                    data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
                    <button  data-dojo-type="dijit/form/Button" id="clearDateFrom" >Clear</button>
                    <label for="Dto1"><s:text name="Value.to" /></label>
                    <input type="text" id="param.dateTo" name="param.actionDateTo" value='<s:date name="param.actionDateTo" format="yyyy-MM-dd" />'
                    onkeydown="javascript:document.getElementById('param.dateTo').blur();" 
                    data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
                    <button data-dojo-type="dijit/form/Button" id="clearDateTo" >Clear</button>
                </td>
             </tr>
             <tr>
                <td><s:text name="audit.access.summary.searcharea.loginId" /></td>
                <td>:</td>
                <td><s:textfield name="param.loginId" data-dojo-type="dijit/form/TextBox" maxlength="50" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="audit.access.summary.searcharea.accessIp" /></td>
                <td>:</td>
                <td><s:textfield name="param.clientIp" data-dojo-type="dijit/form/TextBox" maxlength="50" theme="simple"/></td>
             </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="audit.access.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
