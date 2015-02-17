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
                "custom/CustomSummaryGrid",
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
                    array,
                    CustomSummaryGrid
                    )
                {
                    parser.parse();
                
                    (new B2BPortalBase()).init(
	                    '<c:out value="${session.helpExHolder.helpNo}"/>',
	                    '<c:out value="${session.helpExHolder.helpEmail}"/>',
	                    '<s:property value="#session.commonParam.timeout" />',
	                    '<s:url value="/logout.action" />');
                
                    var customGrid = new CustomSummaryGrid();
                    customGrid.setPageSize('<s:property value="#session.commonParam.pageSizes"/>');
                    customGrid.setDefaultPageSize(<s:if test = 'param == null || param.pageSize == 0'>
                                                    '<s:property value="#session.commonParam.defaultPageSize"/>'
                                                </s:if>
                                                <s:else>
                                                    '<s:property value="param.pageSize" />'
                                                </s:else>);
                    customGrid.setDefaultPage(<s:if test = 'param != null'>
                                                '<s:property value="param.requestPage" />'
                                            </s:if>);
                    customGrid.initGrid(
                            '<s:property value="pageId"/>',
                            '<s:url value="/itemIn/data.action" />',
                            '<s:url value="/ajax/findSummaryFieldAndTooltips.action"/>',
                            [{field: '<s:text name="itemMaster.summary.grid.file" />', method: formatHref}]);
                    
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var confirmDelete = new ConfirmDialog({
                        message: '<s:text name="alert.delete.records" />',
                        yesBtnPressed: function(){
                            fn('<s:url value="/itemIn/delete.action" />');
                        }
                    });
                    
                    var confirmSend = new ConfirmDialog({
                        message: '<s:text name="alert.send.records" />',
                        yesBtnPressed: function(){
                            fn('<s:url value="/itemIn/send.action" />');
                        }
                    });

                    var anyRecordSelected = function()
                    {
                        return customGrid.getCountOfSelected() != 0;
                    }


                    var fn = function(url)
                    {
                        if (!anyRecordSelected())
                        {
                            infoDialog.show();
                            return;
                        }
                    
                        var oids = "";
                        dojo.forEach(customGrid.getSelectedItemDocOids(), function(item)
                        {
                           oids = oids + item.docOid + '-';
                        });
                        
                        oids = oids.substring(0, oids.length-1);
                        xhr.get({
                                url: '<s:url value="/itemIn/putParamIntoSession.action" />',
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
                    	(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />');
                        on(registry.byId("searchBtn"), 'click', function(){
                        	if(!customGrid.grid._isLoaded)
                            {
                        	 	var infoDialog = new InformationDialog({message: "<s:text name='alert.summary.search.not.finished'/>"});
                               	infoDialog.show();
                                return;
                            }
                            
                        	var createDateFrom = dijit.byId("param.createDateFrom").getValue();
                            var createDateTo = dijit.byId("param.createDateTo").getValue();
                            if(null != createDateFrom && new Date(createDateFrom) > new Date(createDateTo))
                            {
                                var infoDialog = new InformationDialog({message: "<s:text name='B2BPC1201' />"});
                                infoDialog.show();
                                return;
                            }
                            
                            xhr.post({
                                url: '<s:url value="/itemIn/search.action" />',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    customGrid.grid.setQuery();
                                }
                            });
                        });
                    }
                    
                    if (dom.byId("uploadBtn"))
                    {
	                    on(registry.byId("uploadBtn"), 'click', 
	                        function()
	                        {
	                            changeToURL('<s:url value="/itemIn/initUpload.action" />');
	                        }
	                    );
                    }
                    
                    
                    if(dom.byId("sentBtn"))
                    {
                        on(registry.byId("sentBtn"),'click',
                           function()
                           {
	                        	if (!anyRecordSelected())
	                            {
	                                infoDialog.show();
	                                return;
	                            }
	                        
	                            confirmSend.show();
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
                            
                                confirmDelete.show();
                            }
                        );
                    }
                    
                });
        function clearButton(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).attr("aria-valuenow", null);
            registry.byId(src).attr("value", null);
        }
        
        var formatHref = function(field,index,cell)
        {
        	var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
        	<s:if test="#session.permitUrl.contains('/itemIn/downloadOriginalFile.action')" >
                return "<a href=\"javascript:doPrint("+docOid+");\">"+ field +"</a>"
            </s:if>
            <s:else>
                return field;
            </s:else>
        }
        
        function doPrint(oid)
        {   
        	var B2BPortalBase = require("custom/B2BPortalBase");
        	(new B2BPortalBase()).resetTimeout(
                    '<s:property value="#session.commonParam.timeout" />',
                    '<s:url value="/logout.action" />');
        	var xhr = require("dojo/_base/xhr");
            var InformationDialog = require("custom/InformationDialog");
            changeToURL('<s:url value="/itemIn/downloadOriginalFile.action" />?param.itemOid='+oid);
        }
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/itemIn/search.action')" >
            	<button data-dojo-type="dijit.form.Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
        	<s:if test="#session.permitUrl.contains('/itemIn/initUpload.action')" >
            	<button data-dojo-type="dijit.form.Button" id="uploadBtn" ><s:text name="button.upload" /></button>
            </s:if>
        	<s:if test="#session.permitUrl.contains('/itemIn/send.action')" >
            	<button data-dojo-type="dijit.form.Button" id="sentBtn" ><s:text name="button.sent" /></button>
            </s:if>
        	<s:if test="#session.permitUrl.contains('/itemIn/delete.action')" >
            	<button data-dojo-type="dijit.form.Button" id="deleteBtn" ><s:text name="button.delete" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="itemMaster.in.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
             <s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null" > 
             <tr>
                <td><s:text name="itemMaster.summary.searchArea.Buyer" /></td>
                <td>:</td>
                <td><s:select data-dojo-type="dijit/form/Select" 
                            name="param.buyerCode" list="buyers" 
                            listKey="buyerCode" listValue="buyerName+'(' + buyerCode + ')'" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/></td>
                <s:if test="suppliers != null" >  
             	<tr>
                	<td><s:text name="itemMaster.summary.searchArea.supplier" /></td>
                	<td>:</td>
                	<td>
                	   <s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.supplierOid" list="suppliers" 
                        	listKey="supplierOid" listValue="supplierName + ' (' + supplierCode + ')'" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
             	</tr>
             	</s:if>
             </tr>
             </s:if>
             <s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >   
             <tr>
                 <td><s:text name="itemMaster.summary.searchArea.supplierName" /></td>
                 <td>:</td>
                 <td>
                     <s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                         maxlength="100" theme="simple" id="param.supplierName"/>
                     <button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
                 </td>
             </tr>
             <tr>
                 <td><s:text name="itemMaster.summary.searchArea.SupplierCode" /></td>
                 <td>:</td>
                 <td>
                    <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                         maxlength="20" theme="simple" id="param.supplierCode"/>
                    <button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
                 </td>
             </tr>
             </s:if>
             <tr>
                <td><s:text name="itemMaster.summary.fileName" /></td>
                <td>:</td>
                <td><s:textfield name="param.originalFilename" id="param.originalFilename" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/>
                <button  data-dojo-type="dijit.form.Button" type="button" onclick="clearButton('param.originalFilename');"><s:text name='button.clear' /></button></td>
             </tr>
             <tr>
                 <td><s:text name="itemMaster.summary.createDate" /></td>
                 <td>:</td>
                 <td>
                     <label for="Dfrom1"><s:text name="Value.from" /></label>
                     <input type="text" id="param.createDateFrom" name="param.createDateFrom" 
                        onkeydown="javascript:document.getElementById('param.createDateFrom').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.createDateFrom" format="yyyy-MM-dd" />'/>
                     <button  data-dojo-type="dijit.form.Button" type="button" onclick="clearButton('param.createDateFrom');"><s:text name='button.clear' /></button>
                     <label for="Dto1"><s:text name="Value.to" /></label>
                     <input type="text" id="param.createDateTo" name="param.createDateTo" 
                     onkeydown="javascript:document.getElementById('param.createDateTo').blur();" 
                     data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                     value='<s:date name="param.createDateTo" format="yyyy-MM-dd" />'/>
                     <button  data-dojo-type="dijit.form.Button" type="button" onclick="clearButton('param.createDateTo');"><s:text name='button.clear' /></button>
                 </td>
             </tr>
             <tr>
                <td><s:text name="itemMaster.summary.status" /></td>
                <td>:</td>
                <td>
                    <s:select data-dojo-type="dijit.form.Select" name="param.itemStatus" list="status" 
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
        <div class="title"><s:text name="itemMaster.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" ></div>
</body>
</html>
