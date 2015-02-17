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
                 "custom/ConfirmDialog",
                 "dijit/Tooltip",
                 "custom/CustomSummaryGrid",
                 "custom/SelectAllConfirmDialog",
                 "dojo/number",
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
                     ConfirmDialog,
                     Tooltip,
                     CustomSummaryGrid,
                     SelectAllConfirmDialog,
                     number
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
                    		'<s:url value="/sales/data.action" />',
                            '<s:url value="/ajax/findSummaryFieldAndTooltips.action"/>',
                            [
								{ field: "<s:text name='sales.summary.grid.totalQty' />", method: formatNum},
								{ field: "<s:text name='sales.summary.grid.totalGrossSalesAmount' />", method: formatNum},
								{ field: "<s:text name='sales.summary.grid.totalDiscountAmount' />", method: formatNum},
								{ field: "<s:text name='sales.summary.grid.totalVatAmount' />", method: formatNum},
								{ field: "<s:text name='sales.summary.grid.totalNetSalesAmount' />", method: formatNum}
                            ]);

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
                              	
                        	 	var validSalesDateFrom = dijit.byId("param.salesDateFrom").getValue();
		                        var validSalesDateTo = dijit.byId("param.salesDateTo").getValue();
                        	    if(null != validSalesDateTo && new Date(validSalesDateFrom) > new Date(validSalesDateTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3101' />"});
		                            infoDialog.show();
		                            return;
		                        }

                        	    var validPeriodStartDateFrom = dijit.byId("param.periodStartDateFrom").getValue();
		                        var validPeriodStartDateTo = dijit.byId("param.periodStartDateTo").getValue();
                        	    if(null != validPeriodStartDateTo && new Date(validPeriodStartDateFrom) > new Date(validPeriodStartDateTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3102' />"});
		                            infoDialog.show();
		                            return;
		                        }

                        	    var validPeriodEndDateFrom = dijit.byId("param.periodEndDateFrom").getValue();
		                        var validPeriodEndDateTo = dijit.byId("param.periodEndDateTo").getValue();
                        	    if(null != validPeriodEndDateTo && new Date(validPeriodEndDateFrom) > new Date(validPeriodEndDateTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3103' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                       
		                        var buyOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

			                    if(buyOid != null && buyOid != (""))
			                    {
			                        var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
			                        var validSentTo = dijit.byId("param.sentDateTo").getValue();
		                        	if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
			                        {
			                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3104' />"});
			                            infoDialog.show();
			                            return;
			                        }
			                       
			                    }

								var supplierOid='<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';
		                        
		                        if(supplierOid != null && supplierOid != (""))
				                {
			                        var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
			                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
		                        	if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
			                        {
			                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3105' />"});
			                            infoDialog.show();
			                            return;
			                        } 
				                }
		                        
                                xhr.post({
                                    url: '<s:url value="/sales/search.action" />',
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
                        	var validSalesDateFrom = dijit.byId("param.salesDateFrom").getValue();
	                        var validSalesDateTo = dijit.byId("param.salesDateTo").getValue();
                    	    if(null != validSalesDateTo && new Date(validSalesDateFrom) > new Date(validSalesDateTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3101' />"});
	                            infoDialog.show();
	                            return;
	                        }

                    	    var validPeriodStartDateFrom = dijit.byId("param.periodStartDateFrom").getValue();
	                        var validPeriodStartDateTo = dijit.byId("param.periodStartDateTo").getValue();
                    	    if(null != validPeriodStartDateTo && new Date(validPeriodStartDateFrom) > new Date(validPeriodStartDateTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3102' />"});
	                            infoDialog.show();
	                            return;
	                        }

                    	    var validPeriodEndDateFrom = dijit.byId("param.periodEndDateFrom").getValue();
	                        var validPeriodEndDateTo = dijit.byId("param.periodEndDateTo").getValue();
                    	    if(null != validPeriodEndDateTo && new Date(validPeriodEndDateFrom) > new Date(validPeriodEndDateTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3103' />"});
	                            infoDialog.show();
	                            return;
	                        }
	                       
	                        var buyOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

		                    if(buyOid != null && buyOid != (""))
		                    {
		                        var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
		                        var validSentTo = dijit.byId("param.sentDateTo").getValue();
	                        	if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3104' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                       
		                    }

							var supplierOid='<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';
	                        
	                        if(supplierOid != null && supplierOid != (""))
			                {
		                        var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
		                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
	                        	if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3105' />"});
		                            infoDialog.show();
		                            return;
		                        } 
			                }

	                        xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=pdf&msgType=DSD',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
				                        submitFormInNewWindow('searchForm', '<s:url value="/sales/print.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	var salesOids = customGrid.getSelectedItemDocOids();
							 var oids = "";
							 dojo.forEach(salesOids, function(item)
							 {
							 	oids = oids + item.salesOid + '-';
							 });
	                        oids = oids.substring(0, oids.length-1);
	                        xhr.get({
	                                url: '<s:url value="/sales/putParamIntoSession.action" />',
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
                                               url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=DSD',
                                               handleAs: "json",
                                               form: dom.byId("searchForm"),
                                               load: function(printDocMsg)
                                               {
                                               	if (printDocMsg == 'success')
                                               	{
				                                    window.open('<s:url value="/sales/print.action" />');
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
								 var salesOids = customGrid.getSelectedItemDocOids();
								 var oids = "";
								 dojo.forEach(salesOids, function(item)
								 {
								 	oids = oids + item.salesOid + '-';
								 });
		                        oids = oids.substring(0, oids.length-1);
		                        xhr.get({
		                                url: '<s:url value="/sales/putParamIntoSession.action" />',
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
		                                            url: '<s:url value="/ajax/checkDocCount.action" />?printType=pdf&msgType=DSD',
		                                            handleAs: "json",
		                                            form: dom.byId("searchForm"),
		                                            load: function(printDocMsg)
		                                            {
		                                            	if (printDocMsg == 'success')
		                                            	{
						                                    window.open('<s:url value="/sales/print.action" />');
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

                    var selectType = new SelectAllConfirmDialog({
                        message: '<s:text name="B2BPC1665" />',
                        yesBtnPressed: function(){
                        	var validSalesDateFrom = dijit.byId("param.salesDateFrom").getValue();
	                        var validSalesDateTo = dijit.byId("param.salesDateTo").getValue();
                    	    if(null != validSalesDateTo && new Date(validSalesDateFrom) > new Date(validSalesDateTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3101' />"});
	                            infoDialog.show();
	                            return;
	                        }

                    	    var validPeriodStartDateFrom = dijit.byId("param.periodStartDateFrom").getValue();
	                        var validPeriodStartDateTo = dijit.byId("param.periodStartDateTo").getValue();
                    	    if(null != validPeriodStartDateTo && new Date(validPeriodStartDateFrom) > new Date(validPeriodStartDateTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3102' />"});
	                            infoDialog.show();
	                            return;
	                        }

                    	    var validPeriodEndDateFrom = dijit.byId("param.periodEndDateFrom").getValue();
	                        var validPeriodEndDateTo = dijit.byId("param.periodEndDateTo").getValue();
                    	    if(null != validPeriodEndDateTo && new Date(validPeriodEndDateFrom) > new Date(validPeriodEndDateTo))
	                        {
	                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3103' />"});
	                            infoDialog.show();
	                            return;
	                        }
	                       
	                        var buyOid = '<c:out value="${session.SESSION_CURRENT_USER_PROFILE.buyerOid}"/>';

		                    if(buyOid != null && buyOid != (""))
		                    {
		                        var validSentFrom = dijit.byId("param.sentDateFrom").getValue();
		                        var validSentTo = dijit.byId("param.sentDateTo").getValue();
	                        	if(null != validSentTo && new Date(validSentFrom) > new Date(validSentTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3104' />"});
		                            infoDialog.show();
		                            return;
		                        }
		                       
		                    }

							var supplierOid='<c:out value="${session.SESSION_CURRENT_USER_PROFILE.supplierOid}"/>';
	                        
	                        if(supplierOid != null && supplierOid != (""))
			                {
		                        var validReceivedFrom = dijit.byId("param.receivedDateFrom").getValue();
		                        var validReceivedTo = dijit.byId("param.receivedDateTo").getValue();
	                        	if(null != validReceivedTo && new Date(validReceivedFrom) > new Date(validReceivedTo))
		                        {
		                            var infoDialog = new InformationDialog({message: "<s:text name='B2BPC3105' />"});
		                            infoDialog.show();
		                            return;
		                        } 
			                }

	                        xhr.post({
                                url: '<s:url value="/ajax/checkDocCount.action" />?selectAll=true&printType=excel&msgType=DSD',
                                handleAs: "json",
                                form: dom.byId("searchForm"),
                                load: function(printDocMsg)
                                {
                                	if (printDocMsg == 'success')
                                	{
			                            submitForm('searchForm', '<s:url value="/sales/exportExcel.action" />?selectAll=true');
                                	}
                                	else
                                	{
                                		new InformationDialog({message: printDocMsg}).show();
                                	}
                                }
                            });
                        },
                        noBtnPressed: function(){
                        	 var salesOids = customGrid.getSelectedItemDocOids();
                             var oids = "";
                             dojo.forEach(salesOids, function(item)
                             {
                                oids = oids + item.salesOid + '-';
                             });
                            oids = oids.substring(0, oids.length-1);
	                        xhr.get({
                                url: '<s:url value="/sales/putParamIntoSession.action" />',
                                content: {selectedOids: oids},
                                load: function(data)
                                {
                                	if (data!='x')
                                       {
                                           (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
                                       }
                                       else
                                       {
                                    	   xhr.post({
                                               url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=DSD',
                                               handleAs: "json",
                                               form: dom.byId("searchForm"),
                                               load: function(printDocMsg)
                                               {
                                               	if (printDocMsg == 'success')
                                               	{
		                                           changeToURL('<s:url value="/sales/exportExcel.action" />');
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

                    if (dom.byId("exportExcel"))
                    {
                    	(new B2BPortalBase()).resetTimeout(
        	                    '<s:property value="#session.commonParam.timeout" />',
        	                    '<s:url value="/logout.action" />'); 
						on(registry.byId("exportExcel"),'click',
							function()
                            {
							 var selected = customGrid.getCountOfSelected();
							 if (selected > 0 && selected == customGrid.getCountOfRow())
	                      	 {
	                      		 selectType.show();
	                      	 }
							 else
							 {
								 if(selected < 1)
								 {
									var infoDialog = new InformationDialog({message: '<s:text name="alert.select.any" />'});
		                   			infoDialog.show();
		                   			return; 
								 }
								 var salesOids = customGrid.getSelectedItemDocOids();
	                             var oids = "";
	                             dojo.forEach(salesOids, function(item)
	                             {
	                                oids = oids + item.salesOid + '-';
	                             });
	                            oids = oids.substring(0, oids.length-1);
		                        xhr.get({
		                                url: '<s:url value="/sales/putParamIntoSession.action" />',
		                                content: {selectedOids: oids},
		                                load: function(data)
		                                {
		                                	if (data!='x')
	                                        {
	                                            (new InformationDialog({message: '<s:text name="message.exception.no.privilege" />'})).show();
	                                        }
	                                        else
	                                        {
	                                        	xhr.post({
			                                        url: '<s:url value="/ajax/checkDocCount.action" />?printType=excel&msgType=DSD',
			                                        handleAs: "json",
			                                        form: dom.byId("searchForm"),
			                                        load: function(printDocMsg)
			                                        {
			                                        	if (printDocMsg == 'success')
			                                        	{
				                                            changeToURL('<s:url value="/sales/exportExcel.action" />');
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

                    hideDiv = function(src)
                    {
                        registry.byId(src).hide();
                    };
                    
                    refreshGrid = function()
                    {
                    	customGrid.grid.setQuery();
                    }

                });

        function clearButton(src)
        {
            var registry = require("dijit/registry");
            registry.byId(src).attr("aria-valuenow", null);
    		registry.byId(src).attr("value", null);
        }

        var formatNum = function(field,index,cell)
        {
          var number = require("dojo/number");
          console.log(field);
		  if(field==null)
		    {
				return "";
		    }
		    var num = number.format(field, {
		        pattern: "#,###.00"
		      });
		    return num;
        }

    </script>
    
</head>

<body>
    <!-- Button Area -->
    <div>
        <table class="btnContainer"><tbody><tr><td>
        	<s:if test="#session.permitUrl.contains('/sales/search.action')" >
            	<button data-dojo-type="dijit/form/Button" id="searchBtn" ><s:text name="button.search" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/sales/print.action')" >
            	<button data-dojo-type="dijit/form/Button" id="printBtn" ><s:text name="button.print" /></button>
            </s:if>
            <s:if test="#session.permitUrl.contains('/sales/exportExcel.action')" >
            	<button data-dojo-type="dijit/form/Button" id="exportExcel" ><s:text name="button.exportExcel" /></button>
            </s:if>
        </td></tr></tbody></table>
    </div>
    
    <!-- Search Area -->
   	<div id="errorMsg">
	</div>
    
    <div data-dojo-type="dijit/TitlePane" data-dojo-props="title:'<s:text name="sales.summary.searcharea.headerLabel" />', width:275">
        <form id="searchForm" name="searchForm" method="post" >
        <table class="commtable">
            <tbody>
				<tr>
                	<td width="100"><s:text name="sales.summary.searcharea.buyer" /></td>
                	<td>:</td>
                	<td>
                    	<s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.buyerOid" list="buyers" 
                        	listKey="buyerOid" listValue="buyerName" 
                        	headerKey="-1" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                	<td width="100"><s:text name="sales.summary.searcharea.storeCode" /></td>
                    <td>:</td>
                    <td>
                        <s:textfield id="param.storeCode" name="param.storeCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearStoreCode" type="button" onclick="clearButton('param.storeCode');"><s:text name='button.clear' /></button>
                    </td>
             	</tr>
             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >   
				<tr>
                	<td><s:text name="sales.summary.searcharea.supplierName" /></td>
                	<td>:</td>
                	<td>
                        <s:textfield name="param.supplierName" data-dojo-type="dijit/form/TextBox" 
                            maxlength="100" theme="simple" id="param.supplierName"/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSupplierName" type="button" onclick="clearButton('param.supplierName');"><s:text name='button.clear' /></button>
                	</td>
                	<td><s:text name="sales.summary.searcharea.supplierCode" /></td>
                	<td>:</td>
                	<td>
                	   <s:textfield name="param.supplierCode" data-dojo-type="dijit/form/TextBox" 
                            maxlength="20" theme="simple" id="param.supplierCode"/>
                       <button  data-dojo-type="dijit.form.Button" id="clearSupplierCode" type="button" onclick="clearButton('param.supplierCode');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	</s:if>
             	<s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null && suppliers != null" >  
             	<tr>
                	<td><s:text name="sales.summary.searcharea.supplier" /></td>
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
                	<td><s:text name="sales.summary.searcharea.salesNo" /></td>
                	<td>:</td>
                	<td>
                		<s:textfield name="param.salesNo" data-dojo-type="dijit/form/TextBox" 
                			maxlength="20" theme="simple" id="param.salesNo"/>
                		<button  data-dojo-type="dijit.form.Button" id="clearSalesNo" type="button" onclick="clearButton('param.salesNo');"><s:text name='button.clear' /></button>	
                	</td>
                	<td width="100"><s:text name="sales.summary.searcharea.salesDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.salesDateFrom" name="param.salesDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.salesDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.salesDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearsalesDateDateFrom" type="button" onclick="clearButton('param.salesDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.salesDateTo" name="param.salesDateTo" 
                        onkeydown="javascript:document.getElementById('param.salesDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.salesDateTo" format="yyyy-MM-dd"/>'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearsalesDateDateTo" type="button" onclick="clearButton('param.salesDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	<tr>
                	<td width="100"><s:text name="sales.summary.searcharea.periodStartDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.periodStartDateFrom" name="param.periodStartDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.periodStartDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.periodStartDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearPeriodStartDateFrom" type="button" onclick="clearButton('param.periodStartDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.periodStartDateTo" name="param.periodStartDateTo" 
                        onkeydown="javascript:document.getElementById('param.periodStartDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.periodStartDateTo" format="yyyy-MM-dd"/>'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearPeriodStartDateTo" type="button" onclick="clearButton('param.periodStartDateTo');"><s:text name='button.clear' /></button>
                	</td>
                	<td width="100"><s:text name="sales.summary.searcharea.periodEndDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.periodEndDateFrom" name="param.periodEndDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.periodEndDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.periodEndDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearPeriodEndDateFrom" type="button" onclick="clearButton('param.periodEndDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.periodEndDateTo" name="param.periodEndDateTo" 
                        onkeydown="javascript:document.getElementById('param.periodEndDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.periodEndDateTo" format="yyyy-MM-dd"/>'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearPeriodEndDateTo" type="button" onclick="clearButton('param.periodEndDateTo');"><s:text name='button.clear' /></button>
                	</td>
             	</tr>
             	<tr>
             		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.buyerOid != null" >  
                	<td width="100"><s:text name="po.summary.searcharea.sentDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.sentDateFrom" name="param.sentDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.sentDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.sentDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearSentDateFrom" type="button" onclick="clearButton('param.sentDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.sentDateTo" name="param.sentDateTo" 
                        onkeydown="javascript:document.getElementById('param.sentDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.sentDateTo" format="yyyy-MM-dd" />'/>
                        <button  data-dojo-type="dijit.form.Button" id="clearSentDateTo" type="button" onclick="clearButton('param.sentDateTo');"><s:text name='button.clear' /></button>
                	</td>
             		</s:if>
             		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.supplierOid != null" >  
                	<td width="100"><s:text name="po.summary.searcharea.receivedDate" /></td>
                	<td>:</td>
                	<td>
                		<label for="Dfrom1"><s:text name="Value.from" /></label>
		                <input type="text" id="param.receivedDateFrom" name="param.receivedDateFrom" 
		                	onkeydown="javascript:document.getElementById('param.receivedDateFrom').blur();" 
		                	data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
		                	value='<s:date name="param.receivedDateFrom" format="yyyy-MM-dd" />'/>
		                <button  data-dojo-type="dijit.form.Button" id="clearReceivedDateFrom" type="button" onclick="clearButton('param.receivedDateFrom');"><s:text name='button.clear' /></button>
		                <label for="Dto1"><s:text name="Value.to" /></label>
		                <input type="text" id="param.receivedDateTo" name="param.receivedDateTo" 
                        onkeydown="javascript:document.getElementById('param.receivedDateTo').blur();" 
                        data-dojo-type="custom/CustomDateTextBox" data-dojo-props='constraints:{datePattern:"dd/MM/yyyy"},style:{width: 80}'
                        value='<s:date name="param.receivedDateTo" format="yyyy-MM-dd" />'/>
						<button  data-dojo-type="dijit.form.Button" id="clearReceivedDateTo" type="button" onclick="clearButton('param.receivedDateTo');"><s:text name='button.clear' /></button>
                	</td>
             		</s:if>
             	</tr>
             </tbody>
             </table>
             <table class="commtable">
             <tbody>
             	<tr>
                	<td width="100"><s:text name="sales.summary.searcharea.salesType" /></td>
                	<td>:</td>
                	<td>
		                <s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.salesDataType" list="docTypes" 
                        	listKey="key" listValue="value" 
                        	headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
                	<td width="100"><s:text name="sales.summary.searcharea.readStatus" /></td>
                	<td>:</td>
                	<td>
		                <s:select data-dojo-type="dijit/form/FilteringSelect" 
                    		name="param.readStatus" list="readStatus" 
                        	listKey="key" listValue="value" 
                        	headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                	</td>
             		<s:if test="#session.SESSION_CURRENT_USER_PROFILE.userType == 2 || #session.SESSION_CURRENT_USER_PROFILE.userType == 4" >   
                    <td width="100"><s:text name="sales.summary.searcharea.favouriteList" /></td>
                    <td>:</td>
                    <td>
                        <s:select data-dojo-type="dijit/form/FilteringSelect" 
                            name="param.listOid" list="favouriteLists" 
                            listKey="listOid" listValue="listCode" 
                            headerKey="" headerValue="%{getText('select.all')}" theme="simple"/>
                    </td>
                	</s:if>
                </tr>
            </tbody>
        </table>
        </form>
    </div>
    
    <div class="space"></div>
    
    <!-- Recrod Area -->
    <div class="pageBar">
        <div class="title"><s:text name="sales.summary.grid.rltmsg" /></div>
    </div>
    <div id="grid" class="grid"></div>
</body>
</html>
