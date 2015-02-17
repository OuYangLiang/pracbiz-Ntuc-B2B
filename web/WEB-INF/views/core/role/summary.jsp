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
                "dojox/data/QueryReadStore",
                "dojox/grid/EnhancedGrid",
                "dojox/grid/enhanced/plugins/Pagination",
                "dojox/grid/enhanced/plugins/IndirectSelection",
                "dojo/parser",
                "dojo/_base/xhr",
                "dijit/form/Select",
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
                    QueryReadStore,
                    EnhancedGrid,
                    Pagination,
                    IndirectSelection,
                    parser,
                    xhr,
                    Select,
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
	                    
	                var curUserType = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.userType" />';
	                var operateEquativeUserType = <s:if test="#session.commonParam.operateEquativeUserType">true</s:if><s:else>false</s:else>;
	                
                    var store = new QueryReadStore({url: '<s:url value="/role/data.action" />'});
                    var grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            <s:if test="#session.commonParam.mkMode" >
                            { name: "<s:text name='role.summary.grid.No.' />", field: "dojoIndex", width: "30px"},
                            { name: "<s:text name='role.summary.grid.roleId' />", field: "roleId", width: "10%", formatter:
                            function(field, index, cell){
                                var hasEditPri = <s:if test="#session.permitUrl.contains('/role/initEdit.action')" >true</s:if><s:else>false</s:else>;
                                var hasViewPri = <s:if test="#session.permitUrl.contains('/role/view.action')" >true</s:if><s:else>false</s:else>;
                                var isPending  = cell.grid.store.getValue(cell.grid.getItem(index), 'ctrlStatus') === 'PENDING';
                                
                                var roleOid = cell.grid.store.getValue(cell.grid.getItem(index), 'roleOid');
                                var actor = cell.grid.store.getValue(cell.grid.getItem(index), 'actor');
                                var actionType = cell.grid.store.getValue(cell.grid.getItem(index), 'actionType');
                                var curLoginId = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.loginId" />';
                                var userType = cell.grid.store.getValue(cell.grid.getItem(index), 'userTypeId');
                                var buyerOid = cell.grid.store.getValue(cell.grid.getItem(index), 'buyerOid');
                                
                                var isSameUserType = (curUserType == 1 && userType == 'SysAdmin') || (curUserType == 2 && userType == 'BuyerAdmin') || (curUserType == 3 && userType == 'SupplierAdmin');
                                
                                var viewFormat = "<a href=\"javascript:changeToURL('<s:url value="/role/view.action"/>?param.roleOid=" + roleOid + "');\">" + field + "</a>";
                                var editFormat = "<a href=\"javascript:changeToURL('<s:url value="/role/initEdit.action"/>?param.roleOid=" + roleOid + "');\">" + field + "</a>";
                                
                                if ((curUserType ==1) && hasViewPri && (userType == 'SupplierAdmin' && buyerOid == null))
                                {
                                    return viewFormat;
                                }
                                
                                if ((curUserType ==1) && hasViewPri && (userType == 'SupplierUser' && buyerOid == null))
                                {
                                    return viewFormat;
                                }
                                
                                if (isSameUserType && !operateEquativeUserType && hasViewPri)
                                {
                                    return viewFormat;
                                }
                                
                                if (isPending)
                                {
                                    if (hasEditPri && ((actionType==='CREATE' || actionType==='UPDATE') && curLoginId===actor))
                                    {
                                        return editFormat;
                                    }
                                    else if (hasViewPri)
                                    {
                                        return viewFormat;
                                    }
                                    else
                                    {
                                        return field;
                                    }
                                }
                                
                                
                                if (hasEditPri)
                                {
                                    return editFormat;
                                }
                                else if (hasViewPri)
                                {
                                    return viewFormat;
                                }
                                else
                                {
                                    return field;
                                }
                            }},
                            { name: "<s:text name='role.summary.grid.userType' />", field: "userTypeId", width: "8%"},
                            { name: "<s:text name='role.summary.grid.roleType' />", field: "roleType", width: "8%", formatter: 
                                function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'roleTypeValue');
                                }
                            },
                            { name: "<s:text name='role.summary.grid.company' />", field: "company", width: "20%", formatter:
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
                                    
                                    return rlt;
                                }
                            
                            },
                            { name: "<s:text name='role.summary.grid.actor' />", field: "actor", width: "5%"},
                            { name: "<s:text name='role.summary.grid.actionType' />", field: "actionType", width: "8%", formatter:
                                function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'actionTypeValue');
                                }
                            },
                            { name: "<s:text name='role.summary.grid.actionDate' />", field: "actionDate", width: "13%"},
                            { name: "<s:text name='role.summary.grid.ctrlStatus' />", field: "ctrlStatus", width: "10%", formatter:
                                function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'ctrlStatusValue');
                                }
                            }
                            </s:if>
                            <s:else>
                            { name: "<s:text name='role.summary.grid.No.' />", field: "dojoIndex", width: "30px"},
                            { name: "<s:text name='role.summary.grid.roleId' />", field: "roleId", width: "15%", formatter:
                            function(field, index, cell){
                                var hasEditPri = <s:if test="#session.permitUrl.contains('/role/initEdit.action')" >true</s:if><s:else>false</s:else>;
                                var hasViewPri = <s:if test="#session.permitUrl.contains('/role/view.action')" >true</s:if><s:else>false</s:else>;
                                var isPending  = cell.grid.store.getValue(cell.grid.getItem(index), 'ctrlStatus') === 'PENDING';
                                
                                var roleOid = cell.grid.store.getValue(cell.grid.getItem(index), 'roleOid');
                                var actor = cell.grid.store.getValue(cell.grid.getItem(index), 'actor');
                                var actionType = cell.grid.store.getValue(cell.grid.getItem(index), 'actionType');
                                var isCreatedFromSysadmin = cell.grid.store.getValue(cell.grid.getItem(index), 'createdFromSysadmin');
                                var curLoginId = '<s:property value="#session.SESSION_CURRENT_USER_PROFILE.loginId" />';
                                var userType = cell.grid.store.getValue(cell.grid.getItem(index), 'userTypeId');
                                var buyerOid = cell.grid.store.getValue(cell.grid.getItem(index), 'buyerOid');
                                
                                var isSameUserType = (curUserType == 1 && userType == 'SysAdmin') || (curUserType == 2 && userType == 'BuyerAdmin') || (curUserType == 3 && userType == 'SupplierAdmin');
                                
                                var viewFormat = "<a href=\"javascript:changeToURL('<s:url value="/role/view.action"/>?param.roleOid=" + roleOid + "');\">" + field + "</a>";
                                var editFormat = "<a href=\"javascript:changeToURL('<s:url value="/role/initEdit.action"/>?param.roleOid=" + roleOid + "');\">" + field + "</a>";
                                
                                if ((curUserType == 1 || curUserType == 2) && hasViewPri && (userType == 'SupplierUser' && buyerOid == null))
                                {
                                    // Sysadmin or Buyeradmin can only view the role if it is created by supplier admin.
                                    // (Supplier user role will have buyerOid = null if it is created by supplier admin.
                                    return viewFormat;
                                }
                                
                                if ((curUserType == 1 || curUserType == 2) && hasViewPri && (userType == 'SupplierAdmin' && buyerOid == null))
                                {
                                    // Sysadmin or Buyeradmin can only view the role if it is created by supplier admin.
                                    // (Supplier admin role will have buyerOid = null if it is created by supplier admin.
                                    return viewFormat;
                                }
                                
                                if ((curUserType == 3) && hasViewPri && (userType == 'SupplierAdmin' && buyerOid != null))
                                {
                                    return viewFormat;
                                }
                                
                                if ((curUserType == 3) && hasViewPri && (userType == 'SupplierUser' && buyerOid != null))
                                {
                                    return viewFormat;
                                }
                                
                                if (isSameUserType && !operateEquativeUserType && hasViewPri)
                                {
                                    return viewFormat;
                                }
                                
                                if (isPending)
                                {
                                    if (hasEditPri && ((actionType==='CREATE' || actionType==='UPDATE') && curLoginId===actor))
                                    {
                                        return editFormat;
                                    }
                                    else if (hasViewPri)
                                    {
                                        return viewFormat;
                                    }
                                    else
                                    {
                                        return field;
                                    }
                                }
                                
                                
                                if (curUserType ==1)
                                {
                                    if (hasEditPri)
	                                {
	                                    return editFormat;
	                                }
	                                else if (hasViewPri)
	                                {
	                                    return viewFormat;
	                                }
	                                else
	                                {
	                                    return field;
	                                }
                                }
                                else
                                {
                                    if (hasEditPri && !isCreatedFromSysadmin)
                                    {
                                        return editFormat;
                                    }
                                    else if (hasViewPri)
                                    {
                                        return viewFormat;
                                    }
                                    else
                                    {
                                        return field;
                                    }
                                }
                            }},
                            { name: "<s:text name='role.summary.grid.userType' />", field: "userTypeId", width: "14%"},
                            { name: "<s:text name='role.summary.grid.roleType' />", field: "roleType", width: "12%", formatter: 
                                function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'roleTypeValue');
                                }
                            },
                            { name: "<s:text name='role.summary.grid.company' />", field: "company", width: "20%", formatter:
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
                                    
                                    return rlt;
                                }
                            
                            },
                            { name: "<s:text name='role.summary.grid.actor' />", field: "actor", width: "12%"},
                            { name: "<s:text name='role.summary.grid.actionType' />", field: "actionType", width: "12%", formatter:
                                function(field, index, cell){
                                    return cell.grid.store.getValue(cell.grid.getItem(index), 'actionTypeValue');
                                }
                            }
                            </s:else>
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
                                var oid = store.getValue(item, 'roleOid');
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
                        return grid.selection.getSelected().length != 0;
                    };
                    
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var confirmDialog = new ConfirmDialog({
                        message: '<s:text name="alert.delete.records" />',
                        yesBtnPressed: function(){
                            var csrfToken = dom.byId("csrfToken").value;
                            fnDelete('<s:url value="/role/saveDelete.action" />?csrfToken='+csrfToken);
                        }
                    });
                    
                    var fnDelete = function(url)
                    {
                        var oids = getOids();
                        
                        xhr.get({
                            url: '<s:url value="/role/putParamIntoSession.action" />',
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
                            url: '<s:url value="/role/putParamIntoSession.action" />',
                            content: {selectedOids: oids},
                            load: function(data)
                            {
                                xhr.get({
                                    url: '<s:url value="/role/checkConfirm.action" />',
                                    load: function(rlt)
                                    {
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
                    
                    
                    // buttons functions
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
                                    url: '<s:url value="/role/search.action" />',
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
                                changeToURL('<s:url value="/role/initAdd.action" />');
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
                    
                    
                    if (dom.byId("approveBtn"))
                    {
                        on(registry.byId("approveBtn"), 'click', function(){
                            if (!anyRecordSelected())
                            {
                                infoDialog.show();
                                return;
                            }
                        
                            fn('<s:url value="/role/initApprove.action" />');
                        });
                    }
                    
                    
                    if (dom.byId("rejectBtn"))
                    {
                        on(registry.byId("rejectBtn"), 'click', function(){
                            if (!anyRecordSelected())
                            {
                                infoDialog.show();
                                return;
                            }
                            
                            fn('<s:url value="/role/initReject.action" />');
                        });
                    }
                    
                    
                    if (dom.byId("withdrawBtn"))
                    {
                        on(registry.byId("withdrawBtn"), 'click', function(){
                            if (!anyRecordSelected())
                            {
                                infoDialog.show();
                                return;
                            }
                            
                            fn('<s:url value="/role/initWithdraw.action" />');
                        });
                    }


                    if (dom.byId("roleTypeSelect"))
                    {
                        on(registry.byId("roleTypeSelect"), 'change', function(value){
                            xhr.get({
                                url: '<s:url value="/ajax/findUserTypes.action" />',
                                content: {"roleType": value},
                                handleAs: 'json',
                                load: function(userTypeList)
                                {
                                	dijit.byId("userTypeSelect").options.length=0;

                                	dijit.byId("userTypeSelect").addOption(new Option('<s:text name="select.all" />', ""));
									for (var i=0; i < userTypeList.length; i++)
									{
										
										var item = userTypeList[i];
				                        
										dijit.byId("userTypeSelect").addOption(new Option(item.userTypeId, item.userTypeOid));
									}
                                    
                                	
                                }
                            });
                        });
                    }
                    
                });
                
    </script>
    
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        <s:if test="#session.permitUrl.contains('/role/search.action')" >
            <button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
        </s:if>
        <s:if test="#session.permitUrl.contains('/role/initAdd.action')" >
            <button data-dojo-type="dijit/form/Button" id="createBtn" ><s:text name="button.create" /></button>
        </s:if>
        <s:if test="#session.commonParam.mkMode" >
        <s:if test="#session.permitUrl.contains('/role/saveApprove.action')" >
            <button data-dojo-type="dijit/form/Button" id="approveBtn" ><s:text name="button.approve" /></button>
        </s:if>
        <s:if test="#session.permitUrl.contains('/role/saveReject.action')" >
            <button data-dojo-type="dijit/form/Button" id="rejectBtn" ><s:text name="button.reject" /></button>
        </s:if>
        <s:if test="#session.permitUrl.contains('/role/saveWithdraw.action')" >
            <button data-dojo-type="dijit/form/Button" id="withdrawBtn" ><s:text name="button.withdraw" /></button>
        </s:if>
        </s:if>
        <s:if test="#session.permitUrl.contains('/role/saveDelete.action')" >
            <button data-dojo-type="dijit/form/Button" id="deleteBtn" ><s:text name="button.delete" /></button>
        </s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="role.summary.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
        <table class="commtable">
            <tbody>
            <tr>
                <td><s:text name="role.summary.searcharea.roleType" /></td>
                <td>:</td>
                <td>
                    <s:select id="roleTypeSelect" data-dojo-type="dijit/form/Select" name="param.roleType" list="roleTypes" 
                        listKey="key" listValue="%{getText(Value)}" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
            </tr>
            
            <tr>
                <td><s:text name="role.summary.searcharea.userType" /></td>
                <td>:</td>
                <td>
                    <s:select id="userTypeSelect" data-dojo-type="dijit/form/Select" name="param.userTypeOid" list="userTypes" 
                        listKey="userTypeOid" listValue="userTypeId" headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                </td>
            </tr>
            
            <tr>
                <td><s:text name="role.summary.searcharea.company" /></td>
                <td>:</td>
                <td><s:textfield name="param.company" data-dojo-type="dijit/form/TextBox" maxlength="50" theme="simple"/></td>
            </tr>
            
            <tr>
                <td><s:text name="role.summary.searcharea.roleId" /></td>
                <td>:</td>
                <td><s:textfield name="param.roleId" data-dojo-type="dijit/form/TextBox" maxlength="20" theme="simple"/></td>
            </tr>
            
            <!-- <tr>
                <td><s:text name="role.summary.searcharea.roleDesc" /></td>
                <td>:</td>
                <td><s:textfield name="param.roleName" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
            </tr> -->
             
            <s:if test="#session.commonParam.mkMode == true" >
            <tr>
                <td><s:text name="role.summary.searcharea.ctrlStatus" /></td>
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
        <div class="title"><s:text name="role.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
