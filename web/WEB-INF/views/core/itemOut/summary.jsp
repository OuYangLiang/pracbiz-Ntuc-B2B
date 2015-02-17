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
                            '<s:url value="/itemOut/data.action" />',
                            '<s:url value="/ajax/findSummaryFieldAndTooltips.action"/>',
                            [{field: '<s:text name="itemMaster.summary.grid.file" />', method: formatHref}]);
                    
                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    var confirmDelete = new ConfirmDialog({
                        message: '<s:text name="alert.delete.records" />',
                        yesBtnPressed: function(){
                            fn('<s:url value="/itemOut/delete.action" />');
                        }
                    });
                    
                    var confirmSend = new ConfirmDialog({
                        message: '<s:text name="alert.send.records" />',
                        yesBtnPressed: function(){
                            fn('<s:url value="/itemOut/send.action" />');
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
                    
                        var itemOids = customGrid.getSelectedItemDocOids();
                        var oids = "";
                        dojo.forEach(itemOids, function(item)
                        {
                           oids = oids + item.itemOid + '-';
                        });
                        
                        oids = oids.substring(0, oids.length-1);
                        xhr.get({
                                url: '<s:url value="/itemOut/putParamIntoSession.action" />',
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
                        on(registry.byId("searchBtn"), 'click', function(){
                        	(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
                        	if(!customGrid.grid._isLoaded)
                            {
                                return;
                            }
                            
                            xhr.post({
                                url: '<s:url value="/itemOut/search.action" />',
                                form: dom.byId("searchForm"),
                                load: function(rlt)
                                {
                                    customGrid.grid.setQuery();
                                }
                            });
                        });
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
            return "<a href=\"javascript:doPrint("+docOid+");\">"+ field +"</a>"
        }
        
        function doPrint(oid)
        {   
        	var B2BPortalBase = require("custom/B2BPortalBase");
        	(new B2BPortalBase()).resetTimeout(
                    '<s:property value="#session.commonParam.timeout" />',
                    '<s:url value="/logout.action" />');
        	var xhr = require("dojo/_base/xhr");
            var InformationDialog = require("custom/InformationDialog");
            changeToURL('<s:url value="/itemOut/downloadOriginalFile.action" />?param.itemOid='+oid);
        }
                
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/itemOut/search.action')" >
            	<button data-dojo-type="dijit.form.Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>

    <!-- Search Area -->
    <div data-dojo-type="dijit.TitlePane" data-dojo-props="title:'<s:text name="itemMaster.out.summary" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
             <tr>
                <td><s:text name="itemMaster.summary.searchArea.Buyer" /></td>
                <td>:</td>
                <td><s:select data-dojo-type="dijit/form/Select" 
                            name="param.buyerOid" list="buyers" 
                            listKey="buyerOid" listValue="buyerName" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/></td>
             </tr>
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
                <td><s:textfield name="param.originalFilename" data-dojo-type="dijit.form.TextBox" maxlength="50" theme="simple"/></td>
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
