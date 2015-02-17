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
                 "dojo/parser",
                 "dojo/_base/xhr",
                 "custom/InformationDialog",
                 "custom/SelectAllConfirmDialog",
                 "custom/ConfirmDialog",
                 "dijit/Tooltip",
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
                     parser,
                     xhr,
                     InformationDialog,
                     SelectAllConfirmDialog,
                     ConfirmDialog,
                     Tooltip,
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
                    		'<s:url value="/cn/data.action" />',
                            '<s:url value="/ajax/findSummaryFieldAndTooltips.action"/>',
                            [{field: '<s:text name="cn.summary.grid.cnNo" />',method: formatHref},
                             {field: '<s:text name="cn.summary.grid.poNo" />',method: formatHrefPo},
                             {field: '<s:text name="cn.summary.grid.generatedOnPortal" />',method: formatGeneratedOnPortal}]);

                    var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                    
                    if (dom.byId("searchBtn"))
                    {
                        on(registry.byId("searchBtn"), 'click', 
                            function()
                            {
                        		(new B2BPortalBase()).resetTimeout(
            	                    '<s:property value="#session.commonParam.timeout" />',
            	                    '<s:url value="/logout.action" />');
								if(!customGrid.grid._isLoaded)
								{
								 	var infoDialog = new InformationDialog({message: "<s:text name='alert.summary.search.not.finished'/>"});
                                   	infoDialog.show();
									return;
								}
                            	
	                        	var validCnFrom = dijit.byId("param.cnDateFrom").getValue();
		                        var validCnTo = dijit.byId("param.cnDateTo").getValue();
		                        if(null != validCnTo && new Date(validCnFrom) > new Date(validCnTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                        
                                var supplierOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';

                                if(supplierOid != null && supplierOid != (""))
                                {
                                	var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
                                    var validSentTo = dijit.byId("param.sentDateTo").getValue();
                                    if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
                                    {
                                        var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
                                        infoDialog.show();
                                        return;
                                    }
                                }

                                var buyerOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

                                if(buyerOid != null && buyerOid != (""))
                                {
                                	var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
                                    var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
                                    if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
                                    {
                                        var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
                                        infoDialog.show();
                                        return;
                                    }
                                }

			                    
                                xhr.post({
                                    url: '<s:url value="/cn/search.action" />',
                                    form: dom.byId("searchForm"),
                                    load: function(rlt)
                                    {
                                    	customGrid.grid.setQuery();
                                    }
                                });
                            }
                        );
                    }

                    var selectPDFType = new SelectAllConfirmDialog({
                        message: '<s:text name="B2BPC1665" />',
                        yesBtnPressed: function(){
                        	var validCnFrom = dijit.byId("param.cnDateFrom").getValue();
	                        var validCnTo = dijit.byId("param.cnDateTo").getValue();
	                        if(null != validCnTo && new Date(validCnFrom) > new Date(validCnTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
	                            infoDialog.show();
	                            return;
	                        }
	                        
                            var supplierOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';

                            if(supplierOid != null && supplierOid != (""))
                            {
                            	var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
                                var validSentTo = dijit.byId("param.sentDateTo").getValue();
                                if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
                                {
                                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
                                    infoDialog.show();
                                    return;
                                }
                            }

                            var buyerOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

                            if(buyerOid != null && buyerOid != (""))
                            {
                            	var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
                                var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
                                if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
                                {
                                    var infoDialog = new InformationDialog({message: "<s:text name='B2BPC2201' />"});
                                    infoDialog.show();
                                    return;
                                }
                            }

                            xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=pdf&msgType=CN',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
				                        submitFormInNewWindow('searchForm', '<s:url value="/cn/print.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	var cnOids = customGrid.getSelectedItemDocOids();
                            var oids = "";
                            dojo.forEach(cnOids, function(item)
                            {
                               oids = oids + item.docOid + '-';
                            });
                           oids = oids.substring(0, oids.length-1);
                           xhr.get({
                               url: '<s:url value="/cn/putParamIntoSession.action" />',
                               content: {selectedOids: oids},
                               sync : true,
                               load: function(data)
                               {
                                   if (data!='x')
                                   {
                                       (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                   }
                                   else
                                   {
                                	   xhr.post({
                                           url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=CN',
                                           handleAs: "json",
                                           form: dom.byId("searchForm"),
                                           load: function(printDocMsg)
                                           {
                                           	if (printDocMsg == 'success')
                                           	{
		                                       window.open('<s:url value="/cn/print.action" />');
                                           	}
                                           	else
                                           	{
                                           		new InformationDialog({message: printDocMsg}).show();
                                           	}
                                           }
                                       });
                                   }
                               }
                           });
                        }
                    });
                    
                    if (dom.byId("printBtn"))
                    {
                        on(registry.byId("printBtn"), 'click',
                           function()
                           {
                             (new B2BPortalBase()).resetTimeout(
                                    '<s:property value="#session.commonParam.timeout" />',
                                    '<s:url value="/logout.action" />');
                             var selected = customGrid.getCountOfSelected();
                             if (selected > 0 && selected == customGrid.getCountOfRow())
	                      	 {
								 selectPDFType.show();
	                      	 }
							 else
							 {
								 if(selected < 1)
	                             {
	                                var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
	                                infoDialog.show();
	                                return; 
	                             }
	                             var cnOids = customGrid.getSelectedItemDocOids();
	                             var oids = "";
	                             dojo.forEach(cnOids, function(item)
	                             {
	                                oids = oids + item.docOid + '-';
	                             });
	                            oids = oids.substring(0, oids.length-1);
	                            xhr.get({
	                                    url: '<s:url value="/cn/putParamIntoSession.action" />',
	                                    content: {selectedOids: oids},
	                                    sync : true,
	                                    load: function(data)
	                                    {
	                                        if (data!='x')
	                                        {
	                                            (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
	                                        }
	                                        else
	                                        {
	                                        	xhr.post({
		                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=CN',
		                                            handleAs: "json",
		                                            form: dom.byId("searchForm"),
		                                            load: function(printDocMsg)
		                                            {
		                                            	if (printDocMsg == 'success')
		                                            	{
				                                            window.open('<s:url value="/cn/print.action" />');
		                                            	}
		                                            	else
		                                            	{
		                                            		new InformationDialog({message: printDocMsg}).show();
		                                            	}
		                                            }
		                                        });
	                                        }
	                                    }
	                                });
							 }
                           });
                    }
                    
                    if(dom.byId("sentBtn"))
                    {
                        on(registry.byId("sentBtn"),'click',
                           function()
                           { 
                             (new B2BPortalBase()).resetTimeout(
                                    '<s:property value="#session.commonParam.timeout" />',
                                    '<s:url value="/logout.action" />');
                             var selected = customGrid.getCountOfSelected();
                             if(selected < 1)
                             {
                                var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
                                infoDialog.show();
                                return; 
                             }
                             var cnOids = customGrid.getSelectedItemDocOids();
                             var oids = "";
                             dojo.forEach(cnOids, function(item)
                             {
                                oids = oids + item.docOid + '-';
                             });
                            oids = oids.substring(0, oids.length-1);
                            var confirmDialog = new ConfirmDialog({
                                message: '<s:text name="B2BPC3002" />',
                                yesBtnPressed: function(){
                                    xhr.get({
                                        url: '<s:url value="/cn/putParamIntoSession.action" />',
                                        content: {selectedOids: oids},
                                        load: function(data)
                                        {
                                            if (data!='x')
                                            {
                                                (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                            }
                                            else
                                            {
                                                changeToURL('<s:url value="/cn/send.action"/>?keepSp=Y');
                                            }
                                        }
                                    });
                                }
                            });
                            confirmDialog.show();
                           });      
                    }

                });

        function clearButton(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).attr("aria-valuenow", null);
    		registry.byId(src).attr("value", null);
        }

        //format dnNo as hype link 
        var formatHref = function(field,index,cell)
        {
     	   var bool = '<s:property value="#session.permitUrl.contains(\'/cn/print.action\')" />';
     	   var duplicate = cell.grid.store.getValue(cell.grid.getItem(index), 'duplicate');
           if(bool == 'true')
           {
         	  var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
       		  return "<a href=\"javascript:doPrint("+docOid+", 'DN');\">"+ field +"</a>";
           }
           else
           {
                 return field;
           }
        }
        
      //format PoNo as hype link 
        var formatHrefPo = function(field,index,cell)
        {
           var printCn = '<s:property value="#session.permitUrl.contains(\'/cn/print.action\')" />';
           var printPo = '<s:property value="#session.permitUrl.contains(\'/po/print.action\')" />';
           if(printCn == 'true' && printPo == 'true')
           {
              var docOid = cell.grid.store.getValue(cell.grid.getItem(index), 'docOid');
              return "<a href=\"javascript:doPrint("+docOid+", 'PO');\">"+ (field == null?'':field) +"</a>";
           }
           else
           {
               return (field == null?'':field);
           }
        }

        //format GeneratedOnPortal as hype link 
        var formatGeneratedOnPortal = function(field,index,cell)
        {
            console.log(field);
           if (field)
           {
               return 'Portal';
       	   }
       	   else
       	   {
       		   return 'Upload';
           }
        }
        function doPrint(oid, docType)
        {   
            var B2BPortalBase = require("custom/B2BPortalBase");
            (new B2BPortalBase()).resetTimeout(
                    '<s:property value="#session.commonParam.timeout" />',
                    '<s:url value="/logout.action" />');
            var xhr = require("dojo/_base/xhr");
            var InformationDialog = require("custom/InformationDialog");
            xhr.get({
                url: '<s:url value="/cn/putParamIntoSession.action" />',
                sync: true,
                content: {selectedOids: oid},
                load: function(data)
                {
                    if (data!='x')
                    {
                        (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                    }
                    else
                    {
                        xhr.post({
                                url: '<s:url value="/cn/checkOtherPdfData.action" />?docType='+docType,
                                content: {selectedOids: oid},
                                sync: true,
                                load: function(data)
                                {
                                    if (data!='x')
                                    {
                                        (new InformationDialog({message: data})).show();
                                    }
                                    else
                                    {
                                         window.open('<s:url value="/cn/print.action" />?docType='+docType);
                                    }
                                }
                            });
                    }
                }
            });
        }

        
    </script>
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/cn/search.action')" >
            	<button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/cn/print.action')" >
                <button data-dojo-type="dijit/form/Button" id="printBtn" ><s:text name="button.print" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/cn/send.action')" >
                <button data-dojo-type="dijit/form/Button" id="sentBtn" ><s:text name="button.submit" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
    <!-- here is message area -->
	<div class="required">
        <s:actionerror />
        <s:fielderror />
    </div>
    <!-- Search Area -->
    <div id="tooltip"></div>
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="cn.summary.searcharea.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
		<input type="hidden" id="csrfToken" name="csrfToken" value="<s:property value='#session.csrfToken' />" />
		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4">
		<table class="commtable">
            <tbody>
				<tr>
                	<td width="100"><s:text name="cn.summary.searcharea.buyer" /></td>
                	<td width="10">:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                    <td width="100"><s:text name="cn.summary.searcharea.storeCode" /></td>
                    <td>:</td>
                    <td>
                        <s:textfield name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.storeCode"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
                <tr>
                	<td width="100"><s:text name="cn.summary.searcharea.supplierName" /></td>
                	<td width="10">:</td>
                	<td>
                    	<s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.supplierName"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
                	</td>
                    <td><s:text name="cn.summary.searcharea.supplierCode" /></td>
                    <td>:</td>
                    <td>
                       <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple" id="param.supplierCode"/>
                       <button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
                <tr>
                	<td width="100"><s:text name="cn.summary.searcharea.cnNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.cnNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.cnNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearCnNo" type="button" onclick="clearButton('param.cnNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="cn.summary.searcharea.cnDate" /></td>
                	<td width="10">:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.cnDateFrom" name="param.cnDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.cnDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.cnDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearDnDateFrom" type="button" onclick="clearButton('param.cnDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.cnDateTo" name="param.cnDateTo" 
                        onkeydown="javascript:document.getElementById('param.cnDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.cnDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearDnDateTo" type="button" onclick="clearButton('param.cnDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	</tbody>
             </table>
             <table class="commtable">
             <tbody>
             	<tr>
             		<td width="100"><s:text name="cn.summary.searcharea.poNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.poNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.poNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="cn.summary.searcharea.receivedDate" /></td>
                	<td>:</td>
                	<td>
                        <label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.receivedDateFrom" name="param.receivedDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.receivedDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.receivedDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearReceivedDateFrom" type="button" onclick="clearButton('param.receivedDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.receivedDateTo" name="param.receivedDateTo" 
                        onkeydown="javascript:document.getElementById('param.receivedDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.receivedDateTo" format="yyyy-MM-dd" />'/>
                		<button  data-dojo-type="dijit.form.Button" id="clearReceivedDateTo" type="button" onclick="clearButton('param.receivedDateTo');"><s:text name='button.clear' /></button>
                    </td>
             	</tr>
            </tbody>
         </table>
        <table class="commtable">
             <tbody>
                 <tr>
                    <td width="100"><s:text name="cn.summary.searcharea.cnStatus" /></td>
                    <td width="10">:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.ctrlStatus" list="cnStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td width="100"><s:text name="cn.summary.searcharea.readStatus" /></td>
                    <td width="10">:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.readStatus" list="readStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                    <td width="100"><s:text name="po.summary.searcharea.favouriteList" /></td>
                    <td width="10">:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.listOid" list="favouriteLists" 
                            listKey="listOid" listValue="listCode" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
             </tbody>
        </table>
		</s:if>
		
		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 3 || #session.SESSION_CURRENT_USER_PROFILE.userType == 5">
		<table class="commtable">
            <tbody>
				<tr>
                	<td width="100"><s:text name="cn.summary.searcharea.buyer" /></td>
                	<td width="10">:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                    <td width="100"><s:text name="cn.summary.searcharea.storeCode" /></td>
                    <td>:</td>
                    <td>
                        <s:textfield name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.storeCode"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
                </tr>
                <s:if test="suppliers != null" >  
             	<tr>
                	<td><s:text name="cn.summary.searcharea.supplier" /></td>
                	<td>:</td>
                	<td>
                	   <s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.supplierOid" list="suppliers" 
                        	listKey="supplierOid" listValue="supplierName + ' (' + supplierCode + ')'" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
             	</tr>
             	</s:if>
                <tr>
                	<td width="100"><s:text name="cn.summary.searcharea.cnNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.cnNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.cnNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearCnNo" type="button" onclick="clearButton('param.cnNo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="cn.summary.searcharea.cnDate" /></td>
                	<td width="10">:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.cnDateFrom" name="param.cnDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.cnDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.cnDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearDnDateFrom" type="button" onclick="clearButton('param.cnDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.cnDateTo" name="param.cnDateTo" 
                        onkeydown="javascript:document.getElementById('param.cnDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.cnDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearDnDateTo" type="button" onclick="clearButton('param.cnDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	</tbody>
             </table>
             <table class="commtable">
             <tbody>
             	<tr>
             		<td width="100"><s:text name="cn.summary.searcharea.poNo" /></td>
                	<td width="10">:</td>
                	<td>
                		<s:textfield name="param.poNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.poNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearPoNo" type="button" onclick="clearButton('param.poNo');"><s:text name='button.clear' /></button>
                	</td>
                    <td width="100"><s:text name="cn.summary.searcharea.sentDate" /></td>
                    <td width="10">:</td>
                    <td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.sentDateFrom" name="param.sentDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.sentDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
		                	value='<s:date name="param.sentDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearSentDateFrom" type="button" onclick="clearButton('param.sentDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.sentDateTo" name="param.sentDateTo" 
                        onkeydown="javascript:document.getElementById('param.sentDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"}'
                        value='<s:date name="param.sentDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSentDateTo" type="button" onclick="clearButton('param.sentDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
            </tbody>
         </table>
        <table class="commtable">
             <tbody>
                 <tr>
                    <td width="100"><s:text name="cn.summary.searcharea.cnStatus" /></td>
                    <td width="10">:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/Select" 
                            name="param.ctrlStatus" list="cnStatus" 
                            listKey="key" listValue="value" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                </tr>
             </tbody>
        </table>
		</s:if>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="cn.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" class="grid"></div>
</body>
</html>
