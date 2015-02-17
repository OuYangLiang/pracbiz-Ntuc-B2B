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
                     QueryReadStore,
                     EnhancedGrid,
                     Pagination,
                     IndirectSelection,
                     parser,
                     xhr,
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
                
                    var store = new QueryReadStore({url: '<s:url value="/group/data.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='group.summary.grid.No' />", field: "dojoIndex", width: "3%"},
                            { name: "<s:text name='group.summary.grid.groupId' />", field: "groupId", width: "10%", formatter:
                            function(field, index, cell){
                                var hasEditPri = <s:if test="#session.permitUrl.contains('/group/initEdit.action')" >true</s:if><s:else>false</s:else>;
                                var hasViewPri = <s:if test="#session.permitUrl.contains('/group/view.action')" >true</s:if><s:else>false</s:else>;
                                var isPending  = cell.grid.store.getValue(cell.grid.getItem(index), 'ctrlStatus') === 'PENDING';
                                
                                var groupOid = cell.grid.store.getValue(cell.grid.getItem(index), 'groupOid');
                                var actor = cell.grid.store.getValue(cell.grid.getItem(index), 'actor');
                                var actionType = cell.grid.store.getValue(cell.grid.getItem(index), 'actionType');
                                
                                var curLoginId = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.loginId" />';
                                
                                if (isPending)
                                {
                                    if ((actionType=='CREATE' || actionType==='UPDATE') && curLoginId==actor)
                                    {
                                        return "<a href=\"javascript:changeToURL('<s:url value="/group/initEdit.action"/>?param.groupOid=" + groupOid + "');\">" + field + "</a>";
                                    }
                                    else if (hasViewPri)
	                                {
	                                    return "<a href=\"javascript:changeToURL('<s:url value="/group/view.action"/>?param.groupOid=" + groupOid + "');\">" + field + "</a>";
	                                }
	                                else
	                                {
	                                    return field;
	                                }
                                }
                                
                                if (hasEditPri)
                                {
                                    return "<a href=\"javascript:changeToURL('<s:url value="/group/initEdit.action"/>?param.groupOid=" + groupOid + "');\">" + field + "</a>";
                                }
                                else if (hasViewPri)
                                {
                                    return "<a href=\"javascript:changeToURL('<s:url value="/group/view.action"/>?param.groupOid=" + groupOid + "');\">" + field + "</a>";
                                }
                                else
                                {
                                    return field;
                                }
                            }},
                            { name: "<s:text name='group.summary.grid.company' />", field: "company", width: "15%"},
                            { name: "<s:text name='group.summary.grid.userType' />", field: "userTypeId", width: "7%"},
                            { name: "<s:text name='group.summary.grid.groupType' />", field: "groupType", width: "6%", formatter: 
                                function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'groupTypeValue');
                                }
                            },
                            { name: "<s:text name='group.summary.grid.actionType' />", field: "actionType", width: "6%", formatter:
                                function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'actionTypeValue');
                                }
                            },
                            { name: "<s:text name='group.summary.grid.actor' />", field: "actor", width: "5%"}
                            <s:if test="#session.commonParam.mkMode" >
                            ,
                            { name: "<s:text name='group.summary.grid.ctrlStatus' />", field: "ctrlStatus", width: "8%", formatter:
                                function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'ctrlStatusValue');
                                }
                            },
                            { name: "<s:text name='group.summary.grid.actionDate' />", field: "actionDate", width: "13%"}
                            </s:if>
                            
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

                    var getOids = function()
                    {
                        var oids = "";
                        
                        array.forEach(grid.selection.getSelected(), function(item){
                            try
                            {
                                var oid = store.getValue(item, 'groupOid');
                                oids = oids + oid + '-';
                            }
                            catch (e)
                            {
                                // there may be a bug in dojo's selection plugin if select multiple records from different pages.
                                //alert(e);
                            }
                        });
                        
                        oids = oids.substring(0, oids.length-1);
                        return oids;
                    };

                    var anyRecordSelected = function()
                    {
                        return getOids() != "";
                    };
                    
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.delete.records" />',
                        yesBtnPressed: function(){
                            var csrfToken = dom.byId("csrfToken").value;
                        	fnDelete('<s:url value="/group/saveDelete.action" />?csrfToken='+csrfToken);
                        }
                    });

                    var fnDelete = function(url)
                    {
                        var oids = getOids();
                        
                        xhr.get({
                            url: '<s:url value="/group/putParamIntoSession.action" />',
                            content: {selectedOids: oids},
                            load: function(data)
                            {
                                changeToURL(url);
                            }
                        });
                    };
                    
                    var fn = function(url)
                    {
                        var oids = getOids();
                        
                        xhr.get({
                            url: '<s:url value="/group/putParamIntoSession.action" />',
                            content: {selectedOids: oids},
                            load: function(data)
                            {
                                xhr.get({
                                    url: '<s:url value="/group/checkConfirm.action" />',
                                    load: function(rlt)
                                    {
                                        console.log("confirm ----->>> " + rlt);
                                        if (rlt == '"pass"')
                                        {
                                            changeToURL(url);
                                        }
                                        else
                                        {
                                            (new InformationDialog({message: rlt})).show();
                                        }
                                    }
                                });
                            }
                        });
                    };
                    
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
                                    url: '<s:url value="/group/search.action" />',
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
                                changeToURL('<s:url value="/group/initAdd.action" />');
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
                    
                    
                    if (dom.byId("approveBtn"))
                    {
                        on(registry.byId("approveBtn"), 'click', 
                            function()
                            {
                        	   if (!anyRecordSelected())
                        	   {
                        		    infoDialog.show();
                        		    return;
                               }

                                fn('<s:url value="/group/initApprove.action" />');
                            }
                        );
                    }
                    
                    
                    if (dom.byId("rejectBtn"))
                    {
                        on(registry.byId("rejectBtn"), 'click', 
                            function()
                            {
		                       	 if (!anyRecordSelected())
		                         {
			                         infoDialog.show();
			                         return;
		                         }
                                fn('<s:url value="/group/initReject.action" />');
                            }
                        );
                    }
                    
                    
                    if (dom.byId("withdrawBtn"))
                    {
                        on(registry.byId("withdrawBtn"), 'click', 
                            function()
                            {
                        	   if (!anyRecordSelected())
	                           {
	                               infoDialog.show();
	                               return;
	                           }
                               fn('<s:url value="/group/initWithdraw.action" />');
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
        	<s:if test="#session.permitUrl.contains('/group/search.action')" >
            	<button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/group/initAdd.action')" >
            	<button data-dojo-type="dijit/form/Button" id="createBtn" ><s:text name="button.create" /></button>
            </s:if>
            <s:if test="#session.commonParam.mkMode" >
            <s:if test="#session.permitUrl.contains('/group/saveApprove.action')" >
            	<button data-dojo-type="dijit/form/Button" id="approveBtn" ><s:text name="button.approve" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/group/saveReject.action')" >
            	<button data-dojo-type="dijit/form/Button" id="rejectBtn" ><s:text name="button.reject" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/group/saveWithdraw.action')" >
            	<button data-dojo-type="dijit/form/Button" id="withdrawBtn" ><s:text name="button.withdraw" /></button>
            </s:if>
            </s:if>
            <s:if test="#session.permitUrl.contains('/group/saveDelete.action')" >
            	<button data-dojo-type="dijit/form/Button" id="deleteBtn" ><s:text name="button.delete" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="group.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
        <table class="commtable">
            <tbody>

             <tr>
                <td><s:text name="group.summary.searcharea.groupId" /></td>
                <td>:</td>
                <td><s:textfield name="param.groupId" data-dojo-type="dijit/form/TextBox" maxlength="20" theme="simple"/></td>
             </tr>
            <tr>
                <td><s:text name="group.summary.searcharea.company" /></td>
                <td>:</td>
                <td><s:textfield name="param.company" data-dojo-type="dijit/form/TextBox" maxlength="20" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="group.summary.searcharea.userType" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit/form/Select" name="param.userTypeOid" list="userTypes" 
                        listKey="userTypeOid" listValue="userTypeId" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
             <s:if test="#session.commonParam.mkMode" >   
             <tr>
                <td><s:text name="group.summary.searcharea.groupCtrlStatus" /></td>
                <td>:</td>
                <td>
                	<s:select data-dojo-type="dijit/form/Select" name="param.ctrlStatus" list="ctrlStatuses" 
                        listKey="key" listValue="%{getText(Value)}" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
             </tr>
             </s:if>
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="group.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
