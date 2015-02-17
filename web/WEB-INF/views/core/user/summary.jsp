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
                "dijit/form/Select",
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
                    Select,
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
                	
                    var store = new QueryReadStore({url: '<s:url value="/user/data.action" />'});
                    
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        rowHeight: 50,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='user.summary.grid.No' />", field: "dojoIndex", width: "3%"},
                            { name: "<s:text name='user.summary.grid.userName' />", field: "userName", width: "10%", formatter: 
                            function(field, index, cell){
                            	var hasEditPri = <s:if test="#session.permitUrl.contains('/user/initEdit.action')" >true</s:if><s:else>false</s:else>;
                                var hasViewPri = <s:if test="#session.permitUrl.contains('/user/view.action')" >true</s:if><s:else>false</s:else>;
                                var isPending  = cell.grid.store.getValue(cell.grid.getItem(index), 'ctrlStatus') === 'PENDING';
                                
                                var userOid = cell.grid.store.getValue(cell.grid.getItem(index), 'userOid');
                                var actor = cell.grid.store.getValue(cell.grid.getItem(index), 'actor');
                                var actionType = cell.grid.store.getValue(cell.grid.getItem(index), 'actionType');
                                var userType = cell.grid.store.getValue(cell.grid.getItem(index), 'userType');
                                var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
                                var curLoginId = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.loginId" />';
                                var curUserOid = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userOid" />';
                                var operateEquativeUserType = <s:if test="#session.commonParam.operateEquativeUserType" >true</s:if><s:else>false</s:else>;
                                
                                if (isPending)
                                {
                                    if ((actionType=='CREATE' || actionType==='UPDATE') && curLoginId==actor)
                                    {
                                    	return "<a href=\"javascript:edit('" + userOid + "');\">" + field + "</a>";
                                    }
                                    else if (hasViewPri)
	                                {
                                    	return "<a href=\"javascript:view('" + userOid + "');\">" + field + "</a>";
	                                }
	                                else
	                                {
	                                    return field;
	                                }
                                }
                                if (hasEditPri && userOid != curUserOid && !((userType == curUserType) && !operateEquativeUserType))
                                {
                                	return "<a href=\"javascript:edit('" + userOid + "');\">" + field + "</a>";
                                }
                                else if (hasViewPri)
                                {
                                	return "<a href=\"javascript:view('" + userOid + "');\">" + field + "</a>";
                                }
                                else
                                {
                                    return field;
                                }
                              }  
                            },
                            { name: "<s:text name='user.summary.grid.loginId' />", field: "loginId", width: "10%"},
                            { name: "<s:text name='user.summary.grid.userType' />", field: "userTypeDesc", width: "10%"},
                            { name: "<s:text name='user.summary.grid.company' />", field: "company", width: "17%"},
                            { name: "<s:text name='user.summary.grid.active' />", field: "active", width: "6%", formatter:
                            function(field, index, cell){
                                var status = cell.grid.store.getValue(cell.grid.getItem(index), 'active');
                                return status==true ?"<s:text name='select.active' />":"<s:text name='select.inactive' />";
                            }},
                            { name: "<s:text name='user.summary.grid.blocked' />", field: "blocked", width: "6%", formatter:
                            function(field, index, cell){
                                var status = cell.grid.store.getValue(cell.grid.getItem(index), 'blocked');
                                return status==true ?"<s:text name='select.yes' />":"<s:text name='select.no' />";
                            }},
                            { name: "<s:text name='user.summary.grid.actionType' />", field: "actionType", width: "8%", formatter:
                                function(field, index, cell){
                            		return cell.grid.store.getValue(cell.grid.getItem(index), 'actionTypeValue');
                        		}
                        	},
                            { name: "<s:text name='user.summary.grid.actor' />", field: "actor", width: "10%"},
                            <s:if test="#session.commonParam.mkMode" >
                            { name: "<s:text name='user.summary.grid.ctrlStatus' />", field: "ctrlStatus", width: "10%", formatter:
                                function(field, index, cell){
                                	return cell.grid.store.getValue(cell.grid.getItem(index), 'ctrlStatusValue');
                            	}
                            },</s:if>
                            { name: "<s:text name='user.summary.grid.updateDate' />", field: "updateDate", width: "15%"}
                            
                            ]
                        ],
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width:'5%', styles:'text-align: center;'},
                            pagination: {
                                <s:if test = 'userProfile != null'>
                                    defaultPage: <s:property value="userProfile.requestPage" />,
                                </s:if>
                                defaultPageSize:<s:if test = 'userProfile == null || userProfile.pageSize == 0'>
                                                    <s:property value="#session.commonParam.defaultPageSize"/>
                                                </s:if>
                                                <s:else><s:property value="userProfile.pageSize" /></s:else>,
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
                            var value = this.store.getValue(item, 'userOid');
                            //alert(value);
                        }
                    }, "grid");
                    
                    //grid.canSort=function(col){if(col==2 || col==8 || col==9)return false;else return true;};
                    
                    grid.startup();

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
	                                url: '<s:url value="/user/search.action" />',
	                                form: dom.byId("searchForm"),
	                                load: function(rlt)
	                                {
	                                    grid.setQuery();
	                                }
	                            });
	                        }
	                    );
                    }
                    if (dom.byId("create"))
                    {
	                    on(registry.byId("create"), 'click', 
	                        function()
	                        {
	                            changeToURL('<s:url value="/user/initAdd.action" />');
	                        }
	                    );
                    }

                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.delete.records" />',
                        yesBtnPressed: function(){
                            var csrfToken = dom.byId("csrfToken").value;
                            fn('<s:url value="/user/saveDelete.action" />?csrfToken='+csrfToken, 'DELETE');
                        }
                    });

                    var fn = function(url, operate)
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
	                            var oid = store.getValue(item, 'userOid');
	                            oids = oids + oid + '-';
                            }
                            catch (e)
                            {
                                //do nothing, just have a bug in dojo
                            }
                        });
                        
                        oids = oids.substring(0, oids.length-1);
                        
                        xhr.get({
                                url: '<s:url value="/user/putParamIntoSession.action" />',
                                content: {selectedOids: oids, operateFlag: operate},
                                load: function(data)
                                {
                                    if (data != "x")
                                    {
                                    	new InformationDialog({message: data}).show();
                                    }
                                    else
                                    {
                                    	changeToURL(url);
                                    }
                                }
                            });
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
	                    on(registry.byId("approveBtn"), 'click', function(){
	                    	fn('<s:url value="/user/initApprove.action" />', 'APPROVE');
	                    });
                    }

                    if (dom.byId("rejectBtn"))
                    {
	                    on(registry.byId("rejectBtn"), 'click', function(){
	                    	fn('<s:url value="/user/initReject.action" />', 'REJECT');
	                    });
                    }

                    if (dom.byId("withdrawBtn"))
                    {
	                    on(registry.byId("withdrawBtn"), 'click', function(){
	                    	fn('<s:url value="/user/initWithdraw.action" />', 'WITHDRAW');
	                    });
                    }

                });
        
                function view(userOid)
                {
                	changeToURL('<s:url value="/user/view.action" />' + "?userProfile.userOid=" + userOid);
                }
                function edit(userOid)
                {
                	changeToURL('<s:url value="/user/initEdit.action" />' + "?userProfile.userOid=" + userOid);
                }
    </script>
</head>

<body class="claro">
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/user/search.action')" >
	            <button id="searchBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.search' /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/user/initAdd.action')" >
            	<button id="create" data-dojo-type="dijit.form.Button" ><s:text name='button.create' /></button>
            </s:if>
            <s:if test="#session.commonParam.mkMode" >
            <s:if test="#session.permitUrl.contains('/user/saveApprove.action')" >
            	<button id="approveBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.approve' /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/user/saveReject.action')" >
            	<button id="rejectBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.reject' /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/user/saveWithdraw.action')" >
            	<button id="withdrawBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.withdraw' /></button>
            </s:if>
            </s:if>
            <s:if test="#session.permitUrl.contains('/user/saveDelete.action')" >
            	<button id="deleteBtn" data-dojo-type="dijit.form.Button" ><s:text name='button.delete' /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
	<!-- here is message area -->
	<div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="user.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
        <table class="commtable">
            <tbody>
                <tr height="25px">
                    <td><s:text name='user.summary.searcharea.loginId' /></td>
                    <td>:</td>
                    <td><s:textfield name="userProfile.loginId" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
                </tr>    
                <tr height="25px">
                    <td><s:text name='user.summary.searcharea.userName' /></td>
                    <td>:</td>
                    <td><s:textfield name="userProfile.userName" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
                </tr>    
                <s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 1 
                    || #session.SESSION_CURRENT_USER_PROFILE.userType == 2" >   
                <tr height="25px">
                    <td><s:text name='user.summary.searcharea.company' /></td>
                    <td>:</td>
                    <td><s:textfield name="userProfile.company" data-dojo-type="dijit.form.TextBox" theme="simple" /></td>
                </tr>    
                </s:if>
                <tr height="25px">
                    <td><s:text name='user.summary.searcharea.userType' /></td>
                    <td>:</td>
                    <td>
                    	<s:select name="userProfile.userType" list="userTypeList"
								listKey="userTypeOid" listValue="userTypeDesc" headerKey=""
								headerValue="%{getText('select.all')}" data-dojo-type="dijit.form.Select" theme="simple"/>
					</td>
                </tr>
                <s:if test="#session.commonParam.mkMode" >
                <tr height="25px">
                    <td><s:text name='user.summary.searcharea.ctrlStatus' /></td>
                    <td>:</td>
                    <td>
                    	<s:select name="userProfile.ctrlStatus" list="ctrlStatusMap"
								listKey="key" listValue="%{getText(Value)}" headerKey=""
								headerValue="%{getText('select.all')}" data-dojo-type="dijit.form.Select" theme="simple"/>
					</td>
                </tr>
                </s:if>
                <tr height="25px">
                    <td><s:text name='user.summary.searcharea.active' /></td>
                    <td>:</td>
                    <td>
                    	<s:select name="userProfile.paramActive" list="#{1:getText('select.active'),0:getText('select.inactive')}"
								listKey="key" listValue="value" headerKey="-1"
								headerValue="%{getText('select.all')}" data-dojo-type="dijit.form.Select" theme="simple"/>
					</td>
                </tr>
                <tr height="25px">
                    <td><s:text name='user.summary.searcharea.blocked' /></td>
                    <td>:</td>
                    <td>
                    	<s:select name="userProfile.paramBlocked" list="#{1:getText('select.yes'),0:getText('select.no')}"
								listKey="key" listValue="value" headerKey="-1"
								headerValue="%{getText('select.all')}" data-dojo-type="dijit.form.Select" theme="simple"/>
					</td>
                </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name='user.summary.grid.rltmsg' /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
