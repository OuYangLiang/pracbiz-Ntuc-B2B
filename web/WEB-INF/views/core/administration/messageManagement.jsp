<%@ include file="/WEB-INF/views/common/taglibs-include.jsp"%>

<html>
<head>
    <title><s:text name="ntuc.web.portal"/></title>
    
    <style type="text/css">
        @import "<s:url value='/js/dojo-root/dojox/grid/resources/Grid.css' />";
        @import "<s:url value='/js/dojo-root/dojox/grid/resources/claroGrid.css' />";
        @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/%{#session.layoutTheme}/EnhancedGrid.css' />";
        @import "<s:url value='/js/dojo-root/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css' />";
        @import "<s:url value='/js/dojo-root/dijit/themes/claro/claro.css' />";
        @import "<s:url value='/js/dojo-root/dojo/resources/dojo.css' />";
       
        @import "<s:url value='/css/claro/overlay.css' />";
        @import "<s:url value='/css/claro/common.css' />";
        @import "<s:url value='/js/dojo-root/dojox/form/resources/CheckedMultiSelect.css' />";
    </style>
     <script type="text/javascript" src="<s:url value='/js/dojo-root/dojo/dojo.js' />"
        data-dojo-config="parseOnLoad: false, async: true, isDebug: true"></script>
     <script type="text/javascript">
     require(
             [
             "dojo/dom",
             "dijit/registry",
             "dojo/string",
             "dijit/layout/ContentPane", 
             "dojox/data/QueryReadStore",
             "dojox/grid/EnhancedGrid",
             "dojox/grid/enhanced/plugins/Pagination",
             "dojox/grid/enhanced/plugins/IndirectSelection",
             "dojo/parser",
             "dojo/_base/xhr",
             "custom/InformationDialog",
             "custom/ConfirmDialog",
             "custom/CustomDateTextBox",
             "dijit/TitlePane",
             "dijit/form/TextBox",
             "dojo/_base/array",
             "dijit/form/Select",
             "dojo/domReady!"
             ], 
             function(
                 dom,
                 registry,
                 string,
                 ContentPane,
                 QueryReadStore,
                 EnhancedGrid,
                 Pagination,
                 IndirectSelection,
                 parser,
                 xhr,
                 InformationDialog,
                 ConfirmDialog,
                 CustomDateTextBox,
                 TitlePane,
                 TextBox,
                 array,
                 Select
                 )
             {
            parser.parse();
            
            initGrid = function()
            {
                var grid = registry.byId("grid");
                if(typeof grid == "undefined")
                {
                    var store = new QueryReadStore({url: '<s:url value="/admin/dataMsg.action" />'});
                    grid = new EnhancedGrid({
                        store: store,
                        autoHeight: true,
                        selectionMode: "multiple",
                        keepSelection: true,
                        structure: [
                            [
                            { name: "<s:text name='admin.msgManagerment.result.row' />", field: "dojoIndex", width: "40px"},
                            { name: "<s:text name='admin.msgManagerment.result.title' />", field: "title", width: "20%", formatter:
                                function(field, index, cell){
                                     <s:if test="#session.permitUrl.contains('/admin/initEditMsg.action')" >
                                         return "<a href='#' onclick='editResMessage("+cell.grid.store.getValue(cell.grid.getItem(index), 'rsrvMsgOid')+");' >"+field+"</a>";
                                     </s:if>
                                     <s:elseif test="#session.permitUrl.contains('/admin/viewMsg.action')" >
                                         return "<a href='#' onclick='viewResMessage("+cell.grid.store.getValue(cell.grid.getItem(index), 'rsrvMsgOid')+");' >"+field+"</a>";
                                     </s:elseif>
                                     <s:else>
                                         return field;
                                     </s:else>
                                }},
                            { name: "<s:text name='admin.msgManagerment.result.start' />", field: "validFrom", width: "20%"},
                            { name: "<s:text name='admin.msgManagerment.result.end' />", field: "validTo", width: "20%"},
                            { name: "<s:text name='admin.msgManagerment.result.actor' />", field: "createBy", width: "20%"},
                            { name: "<s:text name='admin.msgManagerment.result.lastUpdateTime' />", field: "updateDate", width: "20%"},
                            { name: "<s:text name='admin.msgManagerment.result.announcementType' />", field: "announcementType", width: "20%"}
                            ]
                        ],
                        
                        plugins: {
                            indirectSelection: {headerSelector:true, width:'3%', styles:'text-align: center;'},
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
                    
                    grid.canSort=function(col){if(col==2)return false;else return true;};
                    grid.startup();
                }
                return grid;
            }  

            searchResMessage = function()
            {
                var validFrom = document.getElementById("reserveMessage.validFrom");
                var validTo = document.getElementById("reserveMessage.validTo");
                if(validFrom && validTo)
                {
                    var fromDate = string.trim(validFrom.value).split("/");
                    var toDate = string.trim(validTo.value).split("/");
                    if(new Date(fromDate[2], fromDate[1], fromDate[0])>new Date(toDate[2], toDate[1], toDate[0]))
                    {
                        var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0133' />"});
                        infoDialog.show();
                        return;
                    }
                }
                var grid = initGrid();
                xhr.get({
                    url: '<s:url value="/admin/searchMsg.action" />',
                    form: dom.byId("searchForm"),
                    load: function(rlt)
                    {
                        grid.setQuery();
                    }
                });

            };

            viewResMessage = function(rsrvMsgOid)
            {
                xhr.get({
                    url: '<s:url value="/admin/viewMsg.action" />',
                    content:{'reserveMessage.rsrvMsgOid' : rsrvMsgOid},
                    handleAs: "json",
                    load: function(rlt)
                    {
                        dom.byId("viewResMsg.title").textContent=rlt.title;
                        dom.byId("viewResMsg.announcementType").textContent=rlt.announcementType;
                        dom.byId("viewResMsg.validFrom").textContent=rlt.validFrom;
                        dom.byId("viewResMsg.validTo").textContent=rlt.validTo;
                        dom.byId("viewResMsg.content").textContent=rlt.content;
                        registry.byId("viewResMsg").show();
                    }
               });
            };

            
            createResMessage = function()
            {
                document.getElementById("createResMsg.title").value="";
                document.getElementById("createResMsg.announcementType").value="";
                document.getElementById("createResMsg.validFrom").value="";
                document.getElementById("createResMsg.validTo").value="";
                document.getElementById("createResMsg.content").value="";
                registry.byId("createResMsg").show();
            };

            saveCreateResMessage = function()
            {
                var title = string.trim(document.getElementById("createResMsg.title").value);
                var validFrom = string.trim(document.getElementById("createResMsg.validFrom").value);
                var validTo = string.trim(document.getElementById("createResMsg.validTo").value);
                var content = string.trim(document.getElementById("createResMsg.content").value);
                if(title.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0150' />"});
                    infoDialog.show();
                    return;
                }
                if(validFrom.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0156' />"});
                    infoDialog.show();
                    return;
                }
                if(validTo.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0157' />"});
                    infoDialog.show();
                    return;
                }
                var fromDate = validFrom.split("/");
                var toDate = validTo.split("/");
                if(new Date(fromDate[2], fromDate[1], fromDate[0])>new Date(toDate[2], toDate[1], toDate[0]))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0133' />"});
                    infoDialog.show();
                    return;
                }
                if(content.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0152' />"});
                    infoDialog.show();
                    return;
                }
                if(content.length >255)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0134' />"});
                    infoDialog.show();
                    return;
                }

                var csrfToken = document.getElementById("csrfToken").value;
                xhr.get({
                    url: '<s:url value="/admin/saveAddMsg.action" />',
                    form: dom.byId("createResMsgForm"),
                    handleAs: "json",
                    load: function(jsonData)
                    {
                        var infoDialog = new InformationDialog({message: jsonData});
                        infoDialog.show();
                        registry.byId("createResMsg").hide();
                        searchResMessage();
                    }
               });
            };

            editResMessage = function(rsrvMsgOid)
            {
                xhr.get({
                    url: '<s:url value="/admin/initEditMsg.action" />',
                    content:{'reserveMessage.rsrvMsgOid' : rsrvMsgOid},
                    handleAs: "json",
                    load: function(rlt)
                    {
                        console.log(dom.byId("editResMsg.announcementType").option);
                        dom.byId("editResMsg.title").value=rlt.title;
                        dom.byId("editResMsg.announcementType").value=rlt.announcementType;
                        registry.byId("editResMsg.validFrom").set('displayedValue', rlt.validFrom.substr(8,2)+"/"+rlt.validFrom.substr(5,2)+"/"+rlt.validFrom.substr(0,4));
                        registry.byId("editResMsg.validTo").set('displayedValue', rlt.validTo.substr(8,2)+"/"+rlt.validTo.substr(5,2)+"/"+rlt.validTo.substr(0,4));
                        dom.byId("editResMsg.content").value = rlt.content;
                        document.getElementById("editResMsgForm").name = rsrvMsgOid;
                        registry.byId("editResMsg").show();
                    }
               });
            };
            
            saveEditResMessage = function()
            {
                var title = string.trim(document.getElementById("editResMsg.title").value);
                var validFrom = string.trim(document.getElementById("editResMsg.validFrom").value);
                var validTo = string.trim(document.getElementById("editResMsg.validTo").value);
                var content = string.trim(document.getElementById("editResMsg.content").value);
                var rsrvMsgOid = document.getElementById("editResMsgForm").name;
                if(title.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0150' />"});
                    infoDialog.show();
                    return;
                }
                if(validFrom.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0156' />"});
                    infoDialog.show();
                    return;
                }
                if(validTo.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0157' />"});
                    infoDialog.show();
                    return;
                }
                var fromDate = validFrom.split("/");
                var toDate = validTo.split("/");
                if(new Date(fromDate[2], fromDate[1], fromDate[0])>new Date(toDate[2], toDate[1], toDate[0]))
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0133' />"});
                    infoDialog.show();
                    return;
                }
                if(content.length == 0)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0152' />"});
                    infoDialog.show();
                    return;
                }
                if(content.length >255)
                {
                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPA0134' />"});
                    infoDialog.show();
                    return;
                }
                xhr.get({
                    url: '<s:url value="/admin/saveEditMsg.action" />?editResMsg.rsrvMsgOid='+rsrvMsgOid,
                    form: dom.byId("editResMsgForm"),
                    handleAs: "json",
                    load: function(jsonData)
                    {
                        var infoDialog = new InformationDialog({message: jsonData});
                        infoDialog.show();
                        registry.byId("editResMsg").hide();
                        searchResMessage();
                    }
               });
            };


            deleteResMessage = function()
            {
                var grid = initGrid();
                if (grid.selection.getSelected().length == 0)
                {
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    infoDialog.show();
                    return;
                }

                var fn = function(url)
                {
                    var oids = "";
                    array.forEach(grid.selection.getSelected(), function(item){
                        try
                        {
                            var oid = grid.store.getValue(item, 'rsrvMsgOid');
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
                            url: '<s:url value="/admin/putParamIntoSession.action" />',
                            content: {selectedOids: oids},
                            load: function(data)
                            {
                                xhr.get({
                                    url: url,
                                    handleAs: "json",
                                    load: function(jsonData)
                                    {
                                        var infoDialog = new InformationDialog({message: jsonData});
                                        infoDialog.show();
                                        searchResMessage();
                                    }
                               });
                            }
                        });

                    grid.selection.deselectAll();
                }
                
                var confirmDialog = new ConfirmDialog({
                    message: '<s:text name="alert.delete.records" />',
                    yesBtnPressed: function(){
                    	var csrfToken = dom.byId("csrfToken").value;
                    	
                        fn('<s:url value="/admin/saveDeleteMsg.action" />?csrfToken='+csrfToken);
                    }
                });
                confirmDialog.show();
            };

            clearDate = function(src)
            {
                registry.byId(src).attr("aria-valuenow", null);
                registry.byId(src).attr("value", null);
            };

            hideDiv = function(src)
            {
                registry.byId(src).hide();
            };

            searchResMessage();
       }); 


     </script>
    <div dojoType="dijit.Dialog" id="viewResMsg" title="View Message Information" style="width:50%">
	    <table class="commtable">
	        <tr>
	            <td width="2px"><span class="required">*</span></td>
	            <td width="10%"><s:text name="admin.msgManagerment.summary.msgTitle" /></td>
	            <td>:</td>
	            <td width="20%"><span id="viewResMsg.title"></span></td>
              	<td width="2px"><span class="required">*</span></td>
	            <td width="10%"><s:text name="admin.msgManagerment.summary.msgType"/></td>
	            <td>:</td>
	            <td><span id="viewResMsg.announcementType"></span></td>
	        </tr>
	        <tr>
	            <td><span class="required">*</span></td>
	            <td>Valid Period</td>
	            <td>:</td>
	            <td colspan="5"><label for="Dfrom"><s:text name="Value.from" /></label><span id="viewResMsg.validFrom"></span>
	                <label for="Dto"><s:text name="Value.to" /></label><span id="viewResMsg.validTo"></span>
	            </td>
	        </tr>
	        <tr>
	            <td style="vertical-align:text-top; line-height:23px"><span class="required">*</span></td>
	            <td style="vertical-align:text-top; line-height:23px"><s:text name="admin.msgManagerment.summary.msgContent" /></td>
	            <td style="vertical-align:text-top; line-height:23x">:</td>
	            <td style="line-height:23px; font:Arial, Helvetica, sans-serif"><span id="viewResMsg.content"></span></td>
	        </tr>
	        <tr class="space"><td></td></tr>
	        <tr>
	            <td colspan="7" style="text-align:left">
	               <button dojoType="dijit.form.Button" id="viewResMsg.ok" onclick="hideDiv('viewResMsg');"><s:text name="button.ok"/></button>
               </td>
	       </tr>
	    </table>
	</div>      
	
    <div dojoType="dijit.Dialog" id="createResMsg" title="Create Announcement Information" style="width:50%">
        <form action="" id="createResMsgForm" method="get">
          <input type="hidden"  id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
		    <table class="commtable">
		        <tr>
		            <td width="2px"><span class="required">*</span></td>
		            <td width="10%"><s:text name="admin.msgManagerment.summary.msgTitle" /></td>
		            <td>:</td>
		            <td width="20%"><input id="createResMsg.title" name="createResMsg.title" maxlength="50"/></td>
		            <td width="2px"><span class="required">*</span></td>
		            <td width="10%"><s:text name="admin.msgManagerment.summary.msgType"/></td>
		            <td>:</td>
		            <td ><s:select id="createResMsg.announcementType" name="createResMsg.announcementType" list="#{'BUYER':'BUYER','SUPPLIER':'SUPPLIER'}" 
		            	 listKey="key" listValue="value" headerKey="BOTH" headerValue="BOTH"
		            	 data-dojo-type="dijit.form.Select" theme="simple">
		            </s:select></td>
		        </tr>
		        <tr>
		            <td><span class="required">*</span></td>
		            <td>Valid Period</td>
		            <td>:</td>
		            <td colspan="5"><label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="createResMsg.validFrom" name="createResMsg.validFrom" 
		                onkeydown="javascript:document.getElementById('createResMsg.validFrom').blur();" 
		                data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="createResMsg.validTo" name="createResMsg.validTo" 
                        onkeydown="javascript:document.getElementById('createResMsg.validTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
		            </td>
		        </tr>
		        <tr>
		            <td style="vertical-align:text-top; line-height:23px"><span class="required">*</span></td>
		            <td style="vertical-align:text-top; line-height:23px"><s:text name="admin.msgManagerment.summary.msgContent" /></td>
		            <td style="vertical-align:text-top; line-height:23x">:</td>
		            <td style="line-height:23px; font:Arial, Helvetica, sans-serif" colspan="5"><textarea cols="50%" rows="5" id="createResMsg.content" name="createResMsg.content"></textarea></td>
		        </tr>
		        <tr class="space"><td></td></tr>
		        <tr>
		            <td colspan="7" style="text-align:left">
		               <button dojoType="dijit.form.Button" onclick="saveCreateResMessage();"><s:text name="button.save"/></button>
	               </td>
		       </tr>
		    </table>
	    </form>
	</div>      
	
    <div dojoType="dijit.Dialog" id="editResMsg" title="Edit Announcement Information" style="width:50%">
        <form action="" id="editResMsgForm">
          <input type="hidden"  name="csrfToken" value="<s:property value='#session.csrfToken' />" />
		    <table class="commtable">
		        <tr>
		            <td width="2px"><span class="required">*</span></td>
		            <td width="10%"><s:text name="admin.msgManagerment.summary.msgTitle" /></td>
		            <td>:</td>
		            <td width="10%"><input id="editResMsg.title" name="editResMsg.title" maxlength="50"/> </td>
		            <td width="2px"><span class="required">*</span></td>
		            <td width="10%"><s:text name="admin.msgManagerment.summary.msgType"/></td>
		            <td>:</td>
		            <td ><s:select id="editResMsg.announcementType" name="editResMsg.announcementType" list="#{'BUYER':'BUYER','SUPPLIER':'SUPPLIER','BOTH':'BOTH'}" 
		            	 listKey="key" listValue="value">
		            </s:select></td>
		        </tr>
		        <tr>
		            <td><span class="required">*</span></td>
		            <td>Valid Period</td>
		            <td>:</td>
		            <td colspan="5"><label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="editResMsg.validFrom" name="editResMsg.validFrom" 
                        onkeydown="javascript:document.getElementById('editResMsg.validFrom').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="editResMsg.validTo" name="editResMsg.validTo" 
                        onkeydown="javascript:document.getElementById('editResMsg.validTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
		            </td>
		        </tr>
		        <tr>
		            <td style="vertical-align:text-top; line-height:23px"><span class="required">*</span></td>
		            <td style="vertical-align:text-top; line-height:23px"><s:text name="admin.msgManagerment.summary.msgContent" /></td>
		            <td style="vertical-align:text-top; line-height:23x">:</td>
		            <td style="line-height:23px; font:Arial, Helvetica, sans-serif" colspan="5"><textarea cols="50%" rows="5" id="editResMsg.content" name="editResMsg.content" ></textarea></td>
		        </tr>
		        <tr class="space"><td></td></tr>
		        <tr>
		            <td colspan="7" style="text-align:left">
		               <button dojoType="dijit.form.Button" onclick="saveEditResMessage();"><s:text name="button.save"/></button>
	               </td>
		       </tr>
		    </table>
	   </form>
	</div>      
</head>

<body class="claro">
    <!-- Button Area -->
    <table class="btnContainer"><tbody><tr><td>
        <s:if test="#session.permitUrl.contains('/admin/searchMsg.action')" >
            <button data-dojo-type="dijit.form.Button" id="searchResMsgBtn" onclick="searchResMessage();" ><s:text name="button.search" /></button>
        </s:if>
        <s:if test="#session.permitUrl.contains('/admin/initAddMsg.action')" >
            <button data-dojo-type="dijit.form.Button" id="createResMsgBtn" onclick="createResMessage();"><s:text name="button.create" /></button>
        </s:if>
        <s:if test="#session.permitUrl.contains('/admin/saveDeleteMsg.action')" >
            <button data-dojo-type="dijit.form.Button" id="deleteResMsgBtn" onclick="deleteResMessage();"><s:text name="button.delete" /></button>
        </s:if>
    </td></tr></tbody></table>
    
    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="admin.msgManagerment.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
             <tr>
                <td><s:text name="admin.msgManagerment.summary.msgTitle" /></td>
                <td>:</td>
                <td width="20%"><s:textfield name="reserveMessage.title" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
                <td width="10%"><s:text name="admin.msgManagerment.summary.msgType" /></td>
                <td>:</td>
                <td><s:select name="reserveMessage.announcementType" data-dojo-type="dijit.form.Select" 
                	list="#{'BOTH':'BOTH','BUYER':'BUYER','SUPPLIER':'SUPPLIER'}" listKey="key" listValue="value" headerKey="-1" headerValue="ALL" labelposition="left" theme="simple"/></td>
             </tr>
             <tr>
                <td><s:text name="admin.msgManagerment.summary.validPeriod" /></td>
                <td>:</td>
                <td colspan="4"><label for="fromDate"><s:text name="Value.from" /></label>
                      <input type="text" id="reserveMessage.validFrom" name="reserveMessage.validFrom" 
                        onkeydown="javascript:document.getElementById('reserveMessage.validFrom').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
                      <button  data-dojo-type="dijit.form.Button" id="clearValidFrom" type="button" onclick="clearDate('reserveMessage.validFrom');">Clear</button>
                      <label for="toDate"><s:text name="Value.to" /></label>
                      <input type="text" id="reserveMessage.validTo" name="reserveMessage.validTo" 
                        onkeydown="javascript:document.getElementById('reserveMessage.validTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'/>
                      <button data-dojo-type="dijit.form.Button" id="clearValidTo" type="button" onclick="clearDate('reserveMessage.validTo');">Clear</button>
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